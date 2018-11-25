/*******************************************************************************
 * Copyright (c) 2004 Stefan Zeiger and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.novocode.com/legal/epl-v10.html
 * 
 * Contributors:
 *     Stefan Zeiger (szeiger@novocode.com) - initial API and implementation
 *******************************************************************************/

package com.novocode.naf.gui;

import java.util.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.novocode.naf.app.*;
import com.novocode.naf.color.ColorData;
import com.novocode.naf.data.Alignment;
import com.novocode.naf.data.DataDecoder;
import com.novocode.naf.data.Orientation;
import com.novocode.naf.data.Shadow;
import com.novocode.naf.gui.event.*;
import com.novocode.naf.gui.image.ImageDescriptor;
import com.novocode.naf.model.*;
import com.novocode.naf.resource.*;


/**
 * A non-native text and/or image label with advanced features.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Jan 4, 2004
 */

public final class NGCLabel extends NGTextContainer
{
  private static final Logger LOGGER = LoggerFactory.getLogger(NGCLabel.class);

  private Shadow shadow = Shadow.NONE;
  private Alignment alignment = Alignment.LEFT;
  private String imageResource;
  private Map<String, String> images;
  private ColorData[] gradientColors;
  private Orientation gradientOrientation = Orientation.HORIZONTAL;
  private int[] gradientWeights;
  private int[] cachedGradientWeights;
  private boolean initialized;


  @ConfProperty("image")
  public void setImageResource(String imageResource) { this.imageResource = imageResource; }
  public String getImageResource() { return imageResource; }


  @ConfProperty("align")
  public void setAlignment(Alignment alignment) { this.alignment = alignment; }
  public Alignment getAlignment() { return alignment; }


  @ConfProperty
  public void setShadow(Shadow s) throws NAFException
  {
    s.checkStyleMask(SWT.SHADOW_NONE|SWT.SHADOW_IN|SWT.SHADOW_OUT, false);
    this.shadow = s;
  }
  public Shadow getShadow() { return shadow; }


  @ConfProperty
  public void setGradientOrientation(Orientation ori) { this.gradientOrientation = ori; }
  public Orientation getGradientOrientation() { return gradientOrientation; }


  @ConfProperty
  public void setGradientColors(ColorData[] cda) { this.gradientColors = cda; }
  public ColorData[] getGradientColors() { return gradientColors; }


  @ConfProperty
  public void setGradientWeights(int[] weights)
  {
    this.gradientWeights = weights;
    this.cachedGradientWeights = weights;
  }
  public int[] getGradientWeights() { return gradientWeights; }


  @ConfProperty("image")
  public void setImages(ImageDescriptor[] imgds)
  {
    if(images != null) images.clear();
    for(ImageDescriptor imgd : imgds) addImage(imgd);
  }

  public void addImage(ImageDescriptor imgd)
  {
    String iid = imgd.getID(), ires = imgd.getImageResource();
    if(images == null) images = new HashMap<String, String>();
    images.put(iid, ires);
  }


  public Control createControl(Composite parent, NGComponent parentComp, ShellWindowInstance wi, WidgetData pwd) throws NAFException
  {
    if(!initialized)
    {
      if(gradientColors != null)
      {
        if(gradientWeights == null)
        {
          cachedGradientWeights = new int[gradientColors.length-1];
          for(int i=0; i<cachedGradientWeights.length-1; i++)
            cachedGradientWeights[i] = (int)(100.0 / cachedGradientWeights.length * (i+1));
          if(cachedGradientWeights.length != 0) cachedGradientWeights[cachedGradientWeights.length-1] = 100;
          if(LOGGER.isDebugEnabled())
          {
            StringBuffer buf = new StringBuffer();
            buf.append("NGCLabel: Constructed cachedGradientWeights array:");
            for(int i=0; i<cachedGradientWeights.length; i++) buf.append(' ').append(cachedGradientWeights[i]);
            LOGGER.debug(buf.toString());
          }
        }
        else if(gradientColors.length != gradientWeights.length+1)
          throw new NAFException("Length of gradientWeights must be one less than length of gradientColors");
      }
      if(gradientColors != null) setBackgroundColor(ColorData.DEFAULT); //[TODO] Don't modify externally visible property to achieve this
      initialized = true;
    }

    int style = alignment.style | shadow.style;
    final int[] maxImgHeight = new int[1];
    Map<String, Image> imageInstances = null;

    final CLabel label = new CLabel(parent, style)
    {
      public Point computeSize(int wHint, int hHint, boolean changed)
      {
        Point p = super.computeSize(wHint, hHint, changed);
        int mih = maxImgHeight[0];
        if(mih != 0)
        {
          mih += 6; // 6 = 2 * Clabel.vIndent
          if(LOGGER.isDebugEnabled()) LOGGER.debug("p.y = "+p.y+", mih = "+mih);
          if(mih > p.y) p.y = mih;
        }
        return p;
      }
    };
    if(images != null && getModel("clabel", wi.models, DefaultCLabelModel.PTYPE) != null)
    {
      imageInstances = new HashMap<String, Image>();
      for(Map.Entry<String, String> entry : images.entrySet())
      {
        Image img = SWTUtil.getRegisteredImage(entry.getValue(), getResource().getURL(), label, wi.imageManager, false);
        imageInstances.put(entry.getKey(), img);
        if(img != null)
        {
          int ih = img.getBounds().height;
          if(ih > maxImgHeight[0]) maxImgHeight[0] = ih;
        }
      }
    }
    String s = getCachedText();
    SWTUtil.setRegisteredImage(imageResource, getResource().getURL(), label, wi.imageManager, true);
    if(s != null) label.setText(DataDecoder.decodeBackslashEscapes(s));

    if(gradientColors != null)
    {
      final ArrayList<Color> allocList = new ArrayList<Color>();
      Color[] colors = new Color[gradientColors.length];
      for(int i=0; i<colors.length; i++)
      {
        ColorData c = gradientColors[i];
        if(c.getType() == ColorData.TYPE_SYSTEM_COLOR) colors[i] = wi.display.getSystemColor(c.systemColorCode);
        else if(c.getType() == ColorData.TYPE_CUSTOM_COLOR)
        {
          colors[i] = c.createCustomColor(wi.display);
          allocList.add(colors[i]);
        }
        else if(c.getType() == ColorData.TYPE_INHERIT)
        {
          if(pwd != null && pwd.givenBG != null) colors[i] = pwd.givenBG;
          else colors[i] = label.getBackground();
        }
        else // TYPE_DEFAULT
        {
          colors[i] = label.getBackground();
        }
        if(allocList.size() != 0)
        {
          label.addDisposeListener(new DisposeListener()
          {
            public void widgetDisposed(DisposeEvent e)
            {
              for(Color c : allocList) c.dispose();
            }
          });
        }
      }
      label.setBackground(colors, cachedGradientWeights, gradientOrientation == Orientation.VERTICAL);
    }

    final IObjectReadModel<String> textModel = getModel("text", wi.models, DefaultStringModel.PTYPE_READ);
    final ICLabelModel clabelModel = getModel("clabel", wi.models, DefaultCLabelModel.PTYPE);
    final Map<String, Image> finalImageInstances = imageInstances;

    if(textModel != null && clabelModel != null)
      throw new NAFException("Only one of clabel model and text model may be specified for a NGCLabel");

    hookUpTextReadModel(textModel, "text", label, "setText", "getText", false);

    if(clabelModel != null)
    {
      final IChangeListener cl = new IChangeListener()
      {
        public void stateChanged(ChangeEvent e)
        {
          String text = clabelModel.getText();
          Image img = null;
          if(finalImageInstances != null)
            img = finalImageInstances.get(clabelModel.getImageID());
          if(text == null) text = "";
          if(!text.equals(label.getText())) label.setText(text);
          if(img != label.getImage()) label.setImage(img);
        }
      };
      SWTUtil.registerModel(label, clabelModel, cl);
      cl.stateChanged(null);
    }

    return label;
  }
}
