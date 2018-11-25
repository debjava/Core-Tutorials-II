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

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;

import com.novocode.naf.app.*;
import com.novocode.naf.data.DataDecoder;
import com.novocode.naf.gui.event.*;
import com.novocode.naf.model.*;
import com.novocode.naf.resource.*;


/**
 * A toogle / checkbox / show/hide button.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 4, 2003
 */

public final class NGToggle extends NGWidget implements IToolItemWidget, IMenuItemWidget
{
  private Style style = Style.CHECKBOX;
  private String text, showText, hideText, imageResource, accelerator;
  
  public enum Style { CHECKBOX, TOGGLE, SHOW_HIDE }

  
  @ConfProperty("image")
  public void setImageResource(String s) { imageResource = s; }
  public String getImageResource() { return imageResource; }

  
  @ConfProperty
  public void setShowText(String s) { showText = s; }
  public String getShowText() { return showText; }


  @ConfProperty
  public void setHideText(String s) { hideText = s; }
  public String getHideText() { return hideText; }


  @ConfProperty
  public void setText(String s) { text = s; }
  public String getText() { return text; }


  @ConfProperty
  public void setStyle(Style s) { style = s; }
  public Style getStyle() { return style; }


  @ConfProperty
  public void setAccelerator(String s) { accelerator = s; }
  public String getAccelerator() { return accelerator; }


  public Control createControl(Composite parent, NGComponent parentComp, ShellWindowInstance wi, WidgetData pwd) throws NAFException
  {
    if(style == Style.SHOW_HIDE)
    {
      String showText2 = showText;
      String hideText2 = hideText;
      String text2 = text == null ? "" : text;
      if(showText2 == null)
      {
        if(text2.endsWith(" ")) showText2 = text2 + ">> ";
        else showText2 = text2 + " >>";
      }
      if(hideText2 == null)
      {
        if(text2.startsWith(" ")) hideText2 = " <<" + text2;
        else hideText2 = "<< " + text2;
      }
  
      final Composite comp = new Composite(parent, SWT.NONE);
      final StackLayout sl = new StackLayout();
      comp.setLayout(sl);
  
      final Button showButton = new Button(comp, SWT.PUSH);
      final Button hideButton = new Button(comp, SWT.PUSH);
      SWTUtil.setRegisteredImage(imageResource, getResource().getURL(), showButton, wi.imageManager, false);
      SWTUtil.setRegisteredImage(imageResource, getResource().getURL(), hideButton, wi.imageManager, false);
      SWTUtil.setText(showButton, DataDecoder.decodeAccessKey(showText2));
      SWTUtil.setText(hideButton, DataDecoder.decodeAccessKey(hideText2));
  
      IBooleanModel showModel = getModel("selected", wi.models, IBooleanModel.class);
      if(showModel == null) showModel = new DefaultBooleanModel();
      final IBooleanModel fShowModel = showModel;
  
      final boolean[] state = new boolean[1];
      IChangeListener cl = new IChangeListener()
      {
        public void stateChanged(ChangeEvent e)
        {
          boolean val = fShowModel.getBoolean();
          state[0] = val;
          Control c = val ? hideButton : showButton;
          if(sl.topControl != c)
          {
            boolean focus = (sl.topControl != null && sl.topControl.isFocusControl());
            sl.topControl = c;
            comp.layout();
            if(focus) c.setFocus();
          }
        }
      };
      SWTUtil.registerModel(comp, showModel, cl);
      cl.stateChanged(null);
  
      Listener selectionListener = new Listener()
      {
        public void handleEvent (Event e)
        {
          state[0] = (e.widget == showButton);
          fShowModel.setBoolean(state[0]);
        }
      };
  
      showButton.addListener(SWT.Selection, selectionListener);
      hideButton.addListener(SWT.Selection, selectionListener);
  
      return comp;
    }
    else
    {
      final Button b = new Button(parent, style == Style.CHECKBOX ? SWT.CHECK : SWT.TOGGLE);
      SWTUtil.setRegisteredImage(imageResource, getResource().getURL(), b, wi.imageManager, false);
      if(text != null) b.setText(DataDecoder.decodeAccessKey(text));
      addToggleModels(b, wi);
      return b;
    }
  }


  public ToolItem createToolItem(NGToolBar parentComp, final ToolBar bar, ShellWindowInstance wi) throws NAFException
  {
    ToolItem item = new ToolItem(bar, SWT.CHECK);
    parentComp.addRegisteredImage(imageResource, item, wi.imageManager);
    if(text != null) item.setText(DataDecoder.decodeAccessKey(text));
    else if((bar.getStyle() & SWT.RIGHT) != 0) item.setText("");
    //addModels(item, wi);
    addToggleModels(item, wi);
    return item;
  }


  public MenuItem createMenuItem(Menu parent, WindowInstance wi) throws NAFException
  {
    WidgetData pwd = WidgetData.forWidget(parent);
    MenuItem i = new MenuItem(parent, SWT.CHECK);
    addCommonMenuFeatures(i, wi, DataDecoder.decodeAccessKey(text), accelerator, imageResource);
    addModels(i, wi, pwd == null ? null : pwd.getNGWidget());
    addToggleModels(i, wi);
    return i;
  }


  private void addToggleModels(final Widget widget, WindowInstance wi)
  {
    final IBooleanModel checkedModel = getModel("selected", wi.models, IBooleanModel.class);
    if(checkedModel != null)
    {
      final boolean[] lock = new boolean[1];
      IChangeListener cl = new IChangeListener()
      {
        public void stateChanged(ChangeEvent e)
        {
          try
          {
            lock[0] = true;
            SWTUtil.setSelectionIfChanged(widget, checkedModel.getBoolean());
          }
          finally { lock[0] = false; }
        }
      };
      SWTUtil.registerModel(widget, checkedModel, cl);
      cl.stateChanged(null);

      widget.addListener(SWT.Selection, new TypedListener(new SelectionAdapter()
      {
        public void widgetSelected(SelectionEvent e)
        {
          if(!lock[0]) checkedModel.setBoolean(SWTUtil.getSelection(widget));
        }
      }));
    }
  }
}
