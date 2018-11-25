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

package com.novocode.naf.gui.layout;

import java.util.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.novocode.naf.app.NAFException;
import com.novocode.naf.data.SizeMeasure;
import com.novocode.naf.data.SizeMeasurePair;
import com.novocode.naf.gui.*;
import com.novocode.naf.resource.ConfProperty;


/**
 * Layout creator for SWT FormLayout.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 14, 2003
 * @version $Id: NGFormLayout.java 409 2008-05-03 19:31:39Z szeiger $
 */

public class NGFormLayout extends NGLayout
{
  private static final Logger LOGGER = LoggerFactory.getLogger(NGFormLayout.class);

  private static final String DATA_PREFIX    = NGFormLayout.class.getName();
  private static final String DATA_VISIBLE   = DATA_PREFIX + ".visible";
  private static final String DATA_INVISIBLE = DATA_PREFIX + ".invisible";

  public enum InvisibilityPolicy { EMPTY, HIDE, DISPOSE }

  public enum AdjustWindowSize { ADJUST, MIN, NO }

  private static final SizeMeasure DEFAULT_SPACING  = new SizeMeasure(0);
  private static final SizeMeasure DEFAULT_MARGIN   = DEFAULT_SPACING;


  private SizeMeasurePair margin = new SizeMeasurePair(DEFAULT_MARGIN);
  private SizeMeasure spacing = DEFAULT_SPACING;
  private InvisibilityPolicy invisibilityPolicy = InvisibilityPolicy.EMPTY;
  private AdjustWindowSize adjustWindowSize = AdjustWindowSize.ADJUST;


  @ConfProperty
  public void setMargin(SizeMeasurePair a) { this.margin = new SizeMeasurePair(a); }


  @ConfProperty("hmargin")
  public void setHorizontalMargin(SizeMeasure s) { this.margin.setHorizonal(s); }
  public SizeMeasure getHorizontalMargin() { return margin.getHorizonal(); }


  @ConfProperty("vmargin")
  public void setVerticalMargin(SizeMeasure s) { this.margin.setVertical(s); }
  public SizeMeasure getVerticalMargin() { return margin.getVertical(); }


  @ConfProperty
  public void setInvisibilityPolicy(InvisibilityPolicy p) { this.invisibilityPolicy = p; }
  public InvisibilityPolicy getInvisibilityPolicy() { return invisibilityPolicy; }


  @ConfProperty
  public void setAdjustWindowSize(AdjustWindowSize a) { this.adjustWindowSize = a; }
  public AdjustWindowSize getAdjustWindowSize() { return adjustWindowSize; }


  @ConfProperty
  public void setSpacing(SizeMeasure s) { this.spacing = s; }
  public SizeMeasure getSpacing() { return spacing; }


  public void assignLayout(Composite comp, ShellWindowInstance wi) throws NAFException
  {
    fixConnections(comp);
    int embase = SWTUtil.computeEMBase(comp);
    FormLayout l = new FormLayout();
    l.marginWidth  = margin.getHorizonal().compute(embase);
    l.marginHeight = margin.getVertical().compute(embase);
    l.spacing      = spacing.compute(embase);
    comp.setLayout(l);
  }


  @Override
  public void assignLayoutData(Control control, Map<String, String> attributes, Map<String, Control> siblings, Control[] siblingsList, int siblingsIndex) throws NAFException
  {
    // [TODO] Update minimum shell size when the layout changes

    int embase = SWTUtil.computeEMBase(control.getParent());
    String spec = attributes.get("spec");
    String left = attributes.get("left");
    String right = attributes.get("right");
    String top = attributes.get("top");
    String bottom = attributes.get("bottom");
    String width = attributes.get("width");
    String height = attributes.get("height");
    FormData data = FormDataCreator.createFormData(embase, spec, left, right, top, bottom, width, height, siblings, siblingsList, siblingsIndex);

    switch(invisibilityPolicy)
    {
      case HIDE:
        FormData invData = new FormData();
        invData.left   = new FormAttachment(0, 0);
        invData.right  = new FormAttachment(0, 0);
        invData.top    = new FormAttachment(0, 0);
        invData.bottom = new FormAttachment(0, 0);
        control.setData(DATA_VISIBLE, data);
        control.setData(DATA_INVISIBLE, invData);
        control.setLayoutData(control.getVisible() ? data : invData);
        break;
        
      case DISPOSE:
        // [TOOD] Implement INVISIBILITY_POLICY_DISPOSE
        break;
        
      case EMPTY:
        control.setLayoutData(data);
        break;
    }
  }


  public void setControlVisibility(Control control, boolean visible, WindowInstance wi)
  {
    switch(invisibilityPolicy)
    {
      case HIDE:
        if(visible)
        {
          control.setLayoutData(control.getData(DATA_VISIBLE));
          layoutParent(control, wi);
          control.setVisible(visible);
        }
        else
        {
          control.setVisible(visible);
          control.setLayoutData(control.getData(DATA_INVISIBLE));
          layoutParent(control, wi);
        }
        break;
        
      case DISPOSE:
        // [TOOD] Implement INVISIBILITY_POLICY_DISPOSE
        break;
        
      case EMPTY:
        control.setVisible(visible);
        break;
    }
  }


  private void layoutParent(Control control, WindowInstance wi)
  {
    Composite comp = control.getParent();
    if(invisibilityPolicy == InvisibilityPolicy.HIDE) fixConnections(comp);
    comp.layout();

    if((wi.getShell(true).getStyle() | SWT.RESIZE) == 0) // not resizable
    {
      control.getShell().pack();
    }
    else // resizable
    {
      switch(adjustWindowSize)
      {
        case ADJUST:
          control.getShell().pack(); // [TODO] Resize shell correctly; Handle maximized shells
          break;
          
        case MIN:
          control.getShell().pack(); // [TODO] Resize shell correctly; Handle maximized shells
          break;
          
        case NO:
          // do nothing
          break;
      }
    }
  }


  private void fixConnections(Composite comp)
  {
    LOGGER.debug("Fixing connections for {}", comp);
    Control[] children = comp.getChildren();
    for(int i=0; i<children.length; i++)
    {
      FormData fd = (FormData)children[i].getData(DATA_VISIBLE);
      if(children[i].getLayoutData() == children[i].getData(DATA_INVISIBLE)) continue;
      if(fd == null) continue;

      FormAttachment newRight = fd.right, newLeft = fd.left, newTop = fd.top, newBottom = fd.bottom;
      FormAttachment fa;

      fa = fd.left;
      while(fa != null && fa.control != null && fa.control.getLayoutData() == fa.control.getData(DATA_INVISIBLE))
      {
        FormData fd2 = (FormData)fa.control.getData(DATA_VISIBLE);
        if(fd2 == null) break;
        fa = fd2.left;
      }
      if(fa != fd.left) newLeft = cloneFormAttachment(fa);

      fa = fd.right;
      while(fa != null && fa.control != null && fa.control.getLayoutData() == fa.control.getData(DATA_INVISIBLE))
      {
        FormData fd2 = (FormData)fa.control.getData(DATA_VISIBLE);
        if(fd2 == null) break;
        fa = fd2.right;
      }
      if(fa != fd.right) newRight = cloneFormAttachment(fa);

      fa = fd.top;
      while(fa != null && fa.control != null && fa.control.getLayoutData() == fa.control.getData(DATA_INVISIBLE))
      {
        FormData fd2 = (FormData)fa.control.getData(DATA_VISIBLE);
        if(fd2 == null) break;
        fa = fd2.top;
      }
      if(fa != fd.top) newTop = cloneFormAttachment(fa);

      fa = fd.bottom;
      while(fa != null && fa.control != null && fa.control.getLayoutData() == fa.control.getData(DATA_INVISIBLE))
      {
        FormData fd2 = (FormData)fa.control.getData(DATA_VISIBLE);
        if(fd2 == null) break;
        fa = fd2.bottom;
      }
      if(fa != fd.bottom) newBottom = cloneFormAttachment(fa);

      if(newRight != fd.right || newLeft != fd.left || newTop != fd.top || newBottom != fd.bottom)
      {
        FormData fdn = new FormData(fd.width, fd.height);
        fdn.top = newTop;
        fdn.bottom = newBottom;
        fdn.left = newLeft;
        fdn.right = newRight;
        if(LOGGER.isDebugEnabled()) LOGGER.debug("  Fixing "+children[i]+" from ["+fd+"] to ["+fdn+"]");
        children[i].setLayoutData(fdn);
      }
      else if(fd != children[i].getLayoutData()) children[i].setLayoutData(fd);
    }
  }
  
  
  private FormAttachment cloneFormAttachment(FormAttachment fa)
  {
    FormAttachment fan = new FormAttachment(fa.control, fa.offset, fa.alignment);
    fan.numerator = fa.numerator;
    fan.denominator = fa.denominator;
    return fan;
  }
}
