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
import org.eclipse.swt.widgets.*;

import com.novocode.naf.app.*;
import com.novocode.naf.data.DataDecoder;
import com.novocode.naf.gui.event.*;
import com.novocode.naf.model.*;
import com.novocode.naf.resource.*;


/**
 * A push button / toolbar button / menu item.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Nov 26, 2003
 */

public final class NGButton extends NGTextContainer implements IToolItemWidget, IMenuItemWidget
{
  private String accelerator, imageResource;
  private boolean defaultButton;
  private Type type = Type.DEFAULT;

  public enum Type { DEFAULT, ARROW_UP, ARROW_DOWN, ARROW_LEFT, ARROW_RIGHT }
  private static final int[] typeStyles =
    { SWT.PUSH, SWT.ARROW|SWT.UP, SWT.ARROW|SWT.DOWN, SWT.ARROW|SWT.LEFT, SWT.ARROW|SWT.RIGHT };


  @ConfProperty("image")
  public void setImageResource(String imageResource) { this.imageResource = imageResource; }
  public String getImageResource() { return imageResource; }


  @ConfProperty
  public void setAccelerator(String accelerator) { this.accelerator = accelerator; }
  public String getAccelerator() { return accelerator; }


  @ConfProperty
  public void setType(Type type) { this.type = type; }
  public Type getType() { return type; }


  @ConfProperty("default")
  public void setDefaultButton(boolean defaultButton) { this.defaultButton = defaultButton; }
  public boolean isDefaultButton() { return defaultButton; }


  public Control createControl(Composite parent, NGComponent parentComp, ShellWindowInstance wi, WidgetData pwd) throws NAFException
  {
    Button b = new Button(parent, typeStyles[type.ordinal()] | SWT.CENTER);
    SWTUtil.setRegisteredImage(imageResource, getResource().getURL(), b, wi.imageManager, false);
    String s = getCachedText();
    if(s != null) b.setText(DataDecoder.decodeAccessKey(s));
    if(defaultButton) wi.shell.setDefaultButton(b);
    addButtonModels(b, wi);
    return b;
  }


  public ToolItem createToolItem(NGToolBar parentComp, final ToolBar bar, ShellWindowInstance wi) throws NAFException
  {
    ToolItem i = new ToolItem(bar, typeStyles[type.ordinal()]);
    String s = getCachedText();
    if(s != null) i.setText(DataDecoder.decodeAccessKey(s));
    else if((bar.getStyle() & SWT.RIGHT) != 0) i.setText("");
    parentComp.addRegisteredImage(imageResource, i, wi.imageManager);
    addButtonModels(i, wi);
    //addModels(i, wi);
    return i;
  }


  public MenuItem createMenuItem(Menu parent, WindowInstance wi) throws NAFException
  {
    MenuItem i = new MenuItem(parent, SWT.PUSH);
    if(defaultButton) parent.setDefaultItem(i);
    addCommonMenuFeatures(i, wi, DataDecoder.decodeAccessKey(getCachedText()), accelerator, imageResource);
    addButtonModels(i, wi);
    return i;
  }


  private void addButtonModels(final Widget widget, final WindowInstance wi)
  {
    final IActionListener actionModel = getModel("action", wi.models, IActionListener.class);
    if(actionModel != null)
    {
      final ActionEvent ae = new ActionEvent(this, getModelBinding("action").getAttribute("command"), wi);

      widget.addListener(SWT.Selection, new Listener()
      {
        public void handleEvent(Event e)
        {
          actionModel.performAction(ae);
        }
      });
    }
    
    final NGComponent openWin = getFirstExpandedRoleChild("open", wi.models);
    if(openWin != null)
    {
      if(!(openWin instanceof IWindowInstanceWidget))
        throw new NAFException("Cannot assign non-IWindowInstanceWidget resource to role \"open\" of NGButton");
      widget.addListener(SWT.Selection, new Listener()
      {
        public void handleEvent (Event e)
        {
          WindowInstance dwi = ((IWindowInstanceWidget)openWin).createWindowInstance(wi.application, wi.models, wi);
          dwi.open();
        }
      });
    }

    hookUpTextReadModel(getModel("text", wi.models, DefaultStringModel.PTYPE_READ),
      "text", widget, "setText", "getText", true);
  }
}
