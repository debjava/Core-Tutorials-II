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
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import com.novocode.naf.app.*;
import com.novocode.naf.model.IIntModel;
import com.novocode.naf.resource.*;


/**
 * A TabFolder or CTabFolder widget.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Feb 28, 2004
 * @version $Id: NGTabFolder.java 412 2008-05-04 18:27:58Z szeiger $
 */

public final class NGTabFolder extends NGWidget
{
  private TabPosition tabPosisiton = TabPosition.TOP;
  private Type type = Type.AUTO;
  private boolean border, single, flat, close, simple;

  public enum TabPosition
  {
    TOP (SWT.TOP), BOTTOM (SWT.BOTTOM);
    public final int style;
    TabPosition(int style) { this.style = style; }
  }
  
  public enum Type { AUTO, NATIVE, CUSTOM }
  

  @ConfProperty public void setType(Type t) { this.type = t; }
  public Type getType() { return type; }
  
  
  @ConfProperty public void setTabPosition(TabPosition t) { this.tabPosisiton = t; }
  public TabPosition getTabPosition() { return tabPosisiton; }
  
  
  @ConfProperty public void setBorder(boolean b) { this.border = b; }
  public boolean getBorder() { return border; }

  
  @ConfProperty public void setSingle(boolean b) { this.single = b; }
  public boolean getSingle() { return single; }

  
  @ConfProperty public void setFlat(boolean b) { this.flat = b; }
  public boolean getFlat() { return flat; }

  
  @ConfProperty public void setClose(boolean b) { this.close = b; }
  public boolean getClose() { return close; }

  
  @ConfProperty public void setSimple(boolean b) { this.simple = b; }
  public boolean getSimple() { return simple; }

  
  public Control createControl(Composite parent, NGComponent parentComp, ShellWindowInstance wi, WidgetData pwd) throws NAFException
  {
    int style = tabPosisiton.style;
    if(border) style |= SWT.BORDER;

    IIntModel pageModel = getModel("page", wi.models, IIntModel.class);

    boolean custom = (type == Type.CUSTOM) || (type == Type.AUTO && (single || flat || close));
    if(custom)
    {
      style |= (single ? SWT.SINGLE : SWT.MULTI);
      if(flat) style |= SWT.FLAT;
      if(close) style |= SWT.CLOSE;
      final CTabFolder tf = new CTabFolder(parent, style);
      tf.setSimple(simple);
      
      // [TODO] Support all CTabFolder / CTabItem features

      List<NGWidget> chlist = getExpandedRoleChildren(null, wi.models);
      for(NGWidget ch : chlist)
      {
        Map<String, String> props = new HashMap<String, String>();
        if(ch.getLayoutDataAttributes() != null) props.putAll(ch.getLayoutDataAttributes());
        CTabItem item = new CTabItem(tf, SWT.NONE);
        Control control = ch.createAndConfigureControl(tf, this, wi);
        item.setControl(control);
        control.pack();
        String text = props.get("text");
        String tooltip = props.get("tooltip");
        String imageRes = props.get("image");
        if(text != null) item.setText(text);
        if(tooltip != null) item.setToolTipText(tooltip);
        if(imageRes != null) SWTUtil.setRegisteredImage(imageRes, getResource().getURL(), item, wi.imageManager, true);
      }
      tf.pack();

      if(pageModel != null) hookUpIIntModel(pageModel, tf, "getSelectionIndex", "setSelection");
      else tf.setSelection(0);

      return tf;
    }
    else
    {
      final TabFolder tf = new TabFolder(parent, style);
      
      List<NGWidget> chlist = getExpandedRoleChildren(null, wi.models);
      for(NGWidget ch : chlist)
      {
        Map<String, String> props = new HashMap<String, String>();
        if(ch.getLayoutDataAttributes() != null) props.putAll(ch.getLayoutDataAttributes());
        TabItem item = new TabItem(tf, SWT.NONE);
        Control control = ch.createAndConfigureControl(tf, this, wi);
        item.setControl(control);
        control.pack();
        String text = props.get("text");
        String tooltip = props.get("tooltip");
        String imageRes = props.get("image");
        if(text != null) item.setText(text);
        if(tooltip != null) item.setToolTipText(tooltip);
        if(imageRes != null) SWTUtil.setRegisteredImage(imageRes, getResource().getURL(), item, wi.imageManager, true);
      }
      tf.pack();
  
      if(pageModel != null) hookUpIIntModel(pageModel, tf, "getSelectionIndex", "setSelection");
      else tf.setSelection(0);
  
      return tf;
    }
  }
}
