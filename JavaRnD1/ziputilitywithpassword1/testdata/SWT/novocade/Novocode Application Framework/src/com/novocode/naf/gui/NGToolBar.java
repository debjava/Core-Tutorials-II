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

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import com.novocode.naf.app.*;
import com.novocode.naf.data.Orientation;
import com.novocode.naf.data.Shadow;
import com.novocode.naf.gui.image.*;
import com.novocode.naf.resource.*;


/**
 * A tool bar.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 4, 2003
 */

public final class NGToolBar extends NGWidget
{
  private boolean gray, acceptFocus = true;
  private TextAlign textAlign = TextAlign.BELOW;
  private Shadow shadow = Shadow.NONE;
  private Orientation orientation = Orientation.HORIZONTAL;

  public enum TextAlign { BELOW, RIGHT }


  @ConfProperty
  public void setOrientation(Orientation o) { orientation = o; }
  public Orientation getOrientation() { return orientation; }


  @ConfProperty
  public void setTextAlign(TextAlign ta) { textAlign = ta; }
  public TextAlign getTextAlign() { return textAlign; }


  @ConfProperty
  public void setShadow(Shadow s)
  {
    s.checkStyleMask(SWT.SHADOW_NONE|SWT.SHADOW_OUT, false);
    shadow = s;
  }
  public Shadow getShadow() { return shadow; }


  @ConfProperty
  public void setGray(boolean b) { gray = b; }
  public boolean getGray() { return gray; }
  
  
  @ConfProperty
  public void setAcceptFocus(boolean b) { acceptFocus = b; }
  public boolean getAcceptFocus() { return acceptFocus; }


  public Control createControl(Composite parent, NGComponent parentComp, ShellWindowInstance wi, WidgetData pwd) throws NAFException
  {
    int style = SWT.WRAP | SWT.FLAT | orientation.style | shadow.style;
    if(textAlign == TextAlign.RIGHT) style |= SWT.RIGHT;
    if(!acceptFocus) style |= SWT.NO_FOCUS;
    // [TODO] Make FLAT, BORDER and WRAP configurable

    Composite deco = parent;
    while(deco != null && !(deco instanceof Decorations)) deco = deco.getParent();

    ToolBar bar = new ToolBar(parent, style);

    List<NGWidget> chlist = getExpandedRoleChildren(null, wi.models);
    for(NGWidget ch : chlist) createToolItemFor(ch, bar, wi);
    bar.pack();
    return bar;
  }


  private void createToolItemFor(NGWidget w, final ToolBar bar, ShellWindowInstance wi) throws NAFException
  {
    if(w instanceof IToolItemWidget)
    {
      ToolItem i = ((IToolItemWidget)w).createToolItem(this, bar, wi);
      w.addCommonFeatures(i, wi, this);
    }
    else
    {
      ToolItem sep = new ToolItem(bar, SWT.SEPARATOR);
      /* [WORKAROUND] Create Composite to fill the entire toolbar height */
      Composite comp = new Composite(bar, SWT.NONE);
      GridLayout gl = new GridLayout();
      gl.horizontalSpacing = 0;
      gl.verticalSpacing = 0;
      gl.marginHeight = 0;
      gl.marginWidth = 0;
      comp.setLayout(gl);
      Control control = w.createAndConfigureControl(comp, this, wi);
      GridData gd = new GridData();
      gd.horizontalAlignment = GridData.FILL;
      gd.verticalAlignment = GridData.CENTER;
      gd.grabExcessHorizontalSpace = true;
      gd.grabExcessVerticalSpace = true;
      gd.minimumHeight = control.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
      control.setLayoutData(gd);
      w.addModels(control, wi, this);
      w.addCommonFeatures(control, wi, this);
      //control.pack();
      comp.pack();
      sep.setWidth(comp.getSize().x);
      sep.setControl(comp);
    }
  }

  
  public final void addRegisteredImage(String imgres, final ToolItem ti, final ResourceImageManager imageManager) throws NAFException
  {
    if(imgres == null || imgres.length() == 0 || imageManager == null) return;
    imgres = ResourceImageManager.absoluteURIFor(getResource().getURL(), imgres);

    if(gray)
    {
      final IManagedImage mhotImg = imageManager.getMaskRemappedImage(imgres);
      final IManagedImage mImg = imageManager.getMaskRemappedGrayImage(imgres);
      Image hotImg = mhotImg.acquire();
      Image img = mImg.acquire();
      ti.setImage(img);
      ti.setHotImage(hotImg);
      ti.addDisposeListener(new DisposeListener()
      {
        public void widgetDisposed(DisposeEvent e)
        {
          mhotImg.release();
          mImg.release();
        }
      });
    }
    else
    {
      final IManagedImage mImg = imageManager.getMaskRemappedImage(imgres);
      Image img = mImg.acquire();
      ti.setImage(img);
      ti.addDisposeListener(new DisposeListener()
      {
        public void widgetDisposed(DisposeEvent e)
        {
          mImg.release();
        }
      });
    }
  }
}
