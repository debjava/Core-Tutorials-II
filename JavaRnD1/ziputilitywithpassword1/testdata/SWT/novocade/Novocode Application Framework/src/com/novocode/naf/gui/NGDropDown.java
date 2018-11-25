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
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.novocode.naf.app.*;
import com.novocode.naf.data.DataDecoder;
import com.novocode.naf.gui.event.ActionEvent;
import com.novocode.naf.gui.event.IActionListener;
import com.novocode.naf.resource.NGComponent;
import com.novocode.naf.resource.ConfProperty;


/**
 * A drop-down menu button.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 4, 2003
 */

public final class NGDropDown extends NGWidget implements IToolItemWidget
{
  private static final Logger LOGGER = LoggerFactory.getLogger(NGDropDown.class);

  private String text, imageResource;


  @ConfProperty public void setText(String s) { this.text = s;}
  public String getText() { return text; }


  @ConfProperty("image") public void setImageResource(String s) { this.imageResource = s; }
  public String getImageResource() { return imageResource; }


  public ToolItem createToolItem(NGToolBar parentComp, final ToolBar bar, final ShellWindowInstance wi) throws NAFException
  {
    final ToolItem item = new ToolItem(bar, SWT.DROP_DOWN);
    parentComp.addRegisteredImage(imageResource, item, wi.imageManager);
    if(text != null) item.setText(DataDecoder.decodeAccessKey(text));
    else if((bar.getStyle() & SWT.RIGHT) != 0) item.setText("");

    final IActionListener actionModel = getModel("action", wi.models, IActionListener.class);
    final ActionEvent ae = (actionModel == null) ? null : new ActionEvent(this, getModelBinding("action").getAttribute("command"), wi);

    final Menu[] menu = new Menu[1];
    final boolean[] dirty = new boolean[1];
    registerContentModels(item, wi.models, dirty);
    item.addListener(SWT.Selection, new Listener ()
    {
      public void handleEvent (Event event)
      {
        if(event.detail == SWT.ARROW)
        {
          if(menu[0] == null || dirty[0])
          {
            dirty[0] = false;
            if(menu[0] != null)
            {
              LOGGER.debug("Disposing of dynamic menu");
              menu[0].dispose();
            }
            LOGGER.debug("Creating dynamic menu");
            menu[0] = new Menu (wi.getShell(true), SWT.POP_UP);
            addMenuChildren(menu[0], wi);
          }
          Rectangle rect = item.getBounds();
          Point pt = bar.toDisplay(new Point(rect.x, rect.y + rect.height));
          menu[0].setLocation(pt.x, pt.y);
          menu[0].setVisible(true);
        }
        else if(actionModel != null)
        {
          actionModel.performAction(ae);
        }
      }
    });

    item.addListener(SWT.Dispose, new Listener()
    {
      public void handleEvent(Event event)
      {
        if(menu[0] != null)
        {
          menu[0].dispose();
          menu[0] = null;
        }
      }
    });

    //addModels(item, wi);
    return item;
  }


  private void addMenuChildren(Menu parent, ShellWindowInstance wi)
  {
    for(NGComponent ch : getExpandedRoleChildren(null, wi.models))
    {
      if(!(ch instanceof IMenuItemWidget))
        throw new NAFException("Cannot create a menu item from a "+ch.getClass()+" object");
      MenuItem item = ((IMenuItemWidget)ch).createMenuItem(parent, wi);
      ((NGWidget)ch).addModels(item, wi, this);
    }
  }
}
