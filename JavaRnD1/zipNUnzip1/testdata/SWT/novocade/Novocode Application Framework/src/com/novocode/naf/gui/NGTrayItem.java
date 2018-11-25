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
import com.novocode.naf.gui.event.ActionEvent;
import com.novocode.naf.gui.event.IActionListener;
import com.novocode.naf.model.ModelMap;
import com.novocode.naf.resource.NGComponent;
import com.novocode.naf.resource.ConfProperty;


/**
 * A System Tray item.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since May 22, 2004
 */

public final class NGTrayItem extends NGWidget implements IWindowInstanceWidget
{
  private String imageResource;


  @ConfProperty("image")
  public void setImageResource(String s) { imageResource = s; }
  public String getImageResource() { return imageResource; }

  
  public TrayItemWindowInstance createWindowInstance(final NAFApplication app, final ModelMap models, final WindowInstance parent) throws NAFException
  {
    final Tray tray = app.getDisplay().getSystemTray();
    if(tray == null) return null;

    final TrayItem item = new TrayItem(tray, SWT.NONE);
    if(getTooltip() != null) item.setToolTipText(getTooltip());
    SWTUtil.setRegisteredImage(imageResource, getResource().getURL(), item, app.getImageManager(), false);

    final TrayItemWindowInstance wi = new TrayItemWindowInstance(this, item, models, app);

    final IActionListener actionModel = getModel("action", models, IActionListener.class);
    if(actionModel != null)
    {
      final ActionEvent ae = new ActionEvent(this, getModelBinding("action").getAttribute("command"), wi);
      item.addListener(SWT.Selection, new Listener()
      {
        public void handleEvent(Event e)
        {
          actionModel.performAction(ae);
        }
      });
    }

    final IActionListener defaultActionModel = getModel("default-action", wi.models, IActionListener.class);
    if(defaultActionModel != null)
    {
      final ActionEvent ae = new ActionEvent(this, getModelBinding("default-action").getAttribute("command"), wi);
      item.addListener(SWT.DefaultSelection, new Listener()
      {
        public void handleEvent(Event e)
        {
          defaultActionModel.performAction(ae);
        }
      });
    }

    addPopupMenu(item, wi);
    addModels(item, wi, null);
    return wi;
  }


  private void addPopupMenu(TrayItem item, WindowInstance wi) throws NAFException
  {
    NGComponent popup = getFirstExpandedRoleChild("popup", wi.models);
    if(popup == null) return;
    if(!(popup instanceof NGMenu))
      throw new NAFException("Only NGMenu objects can be assigned to role \"popup\"");
    final Menu menu = ((NGMenu)popup).createMenu(wi, SWT.POP_UP);
    item.addListener(SWT.Dispose, new Listener()
    {
      public void handleEvent(Event event)
      {
        menu.dispose();
      }
    });
    item.addListener(SWT.MenuDetect, new Listener ()
    {
      public void handleEvent (Event event)
      {
        menu.setVisible(true);
      }
    });
  }
}
