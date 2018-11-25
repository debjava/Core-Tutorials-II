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

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.*;

import com.novocode.naf.app.*;
import com.novocode.naf.color.ColorData;
import com.novocode.naf.data.Orientation;
import com.novocode.naf.resource.*;
import com.novocode.naf.swt.custom.ScaledImage;


/**
 * A scaled image.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Mar 21, 2005
 */

public final class NGScaledImage extends NGWidget
{
  private String imageResource;
  private ColorData[] gradientColors;
  private Orientation gradientOrientation = Orientation.HORIZONTAL;
  private int[] gradientWeights;
  private int[] cachedGradientWeights;
  private boolean initialized;


  @ConfProperty("image")
  public void setImageResource(String s) { this.imageResource = s; }
  public String getImageResource() { return imageResource; }


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
        }
        else if(gradientColors.length != gradientWeights.length+1)
          throw new NAFException("Length of gradientWeights must be one less than length of gradientColors");
      }
      if(gradientColors != null) setBackgroundColor(ColorData.DEFAULT); //[TODO] Don't modify externally visible property to achieve this
      initialized = true;
    }

    final ScaledImage si = new ScaledImage(parent, SWT.NONE);

    if(imageResource != null)
    {
      si.setImage(SWTUtil.getRegisteredImage(imageResource, getResource().getURL(), si, wi.imageManager, true));
    }

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
          else colors[i] = si.getBackground();
        }
        else // TYPE_DEFAULT
        {
          colors[i] = si.getBackground();
        }
        if(allocList.size() != 0)
        {
          si.addDisposeListener(new DisposeListener()
          {
            public void widgetDisposed(DisposeEvent e)
            {
              for(Color c : allocList) c.dispose();
            }
          });
        }
      }
      si.setBackground(colors, cachedGradientWeights, gradientOrientation == Orientation.VERTICAL);
    }

    return si;
  }
}
