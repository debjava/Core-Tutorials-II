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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.novocode.naf.app.*;
import com.novocode.naf.data.DataDecoder;
import com.novocode.naf.gui.event.ChangeEvent;
import com.novocode.naf.gui.event.IChangeListener;
import com.novocode.naf.model.IItemListModel;
import com.novocode.naf.resource.NGComponent;
import com.novocode.naf.resource.NGGroup;
import com.novocode.naf.resource.ConfProperty;


/**
 * A menu.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Nov 24, 2003
 */

public final class NGMenu extends NGWidget implements IToolItemWidget, IMenuItemWidget
{
  private static final Logger LOGGER = LoggerFactory.getLogger(NGMenu.class);

  private String accelerator, text, imageResource;


  @ConfProperty
  public void setText(String s) { this.text = s; }
  public String getText() { return text; }


  @ConfProperty
  public void setAccelerator(String s) { this.accelerator = s; }
  public String getAccelerator() { return accelerator; }
  
  
  @ConfProperty("image")
  public void setImageResource(String s) { this.imageResource = s; }
  public String getImageResource() { return imageResource; }


  public ToolItem createToolItem(NGToolBar parentComp, final ToolBar bar, final ShellWindowInstance wi) throws NAFException
  {
    final ToolItem item = new ToolItem(bar, SWT.PUSH);
    parentComp.addRegisteredImage(imageResource, item, wi.imageManager);
    if(text != null) item.setText(DataDecoder.decodeAccessKey(text));
    else if((bar.getStyle() & SWT.RIGHT) != 0) item.setText("");

    final Menu[] menu = new Menu[1];
    final boolean[] dirty = new boolean[1];
    registerContentModels(item, wi.models, dirty);
    item.addListener(SWT.Selection, new Listener ()
    {
      public void handleEvent (Event event)
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


  public Menu createMenuBar(Shell shell, final ShellWindowInstance wi) throws NAFException
  {
    final Menu mbar = new Menu(shell, SWT.BAR);
    addMenuChildren(mbar, wi);
    
    IChangeListener cl = new IChangeListener()
    {
      public void stateChanged(ChangeEvent e)
      {
        try
        {
          mbar.getParent().setRedraw(false);
          // [TODO] React to ItemListModel for partial updates
          for(MenuItem mi : mbar.getItems()) mi.dispose();
          addMenuChildren(mbar, wi);
        }
        finally { mbar.getParent().setRedraw(true); }
      }
    };

    for(NGComponent c : getExpandedStaticRoleChildren(null, wi.models))
    {
      if(!(c instanceof NGGroup)) continue;
      IItemListModel contentModel = ((NGGroup)c).getContentModel(wi.models);
      if(contentModel != null) SWTUtil.registerModel(mbar, contentModel, cl);
    }

    return mbar;
  }


  public MenuItem createMenuItem(Menu parent, final WindowInstance wi) throws NAFException
  {
    MenuItem i = new MenuItem(parent, SWT.CASCADE);
    addCommonMenuFeatures(i, wi, DataDecoder.decodeAccessKey(text), getAccelerator(), imageResource);
    Menu menu = createMenu(wi, SWT.DROP_DOWN);
    //-- if(getModel("items", wi.models) == null && menu.getItemCount() == 0) i.setEnabled(false);
    i.setMenu(menu);
    return i;
  }
  

  public Menu createMenu(final WindowInstance wi, int menuStyle) throws NAFException
  {
    final Menu menu = new Menu(wi.getShell(true), menuStyle);
    final boolean[] dirty = { false };
    registerContentModels(menu, wi.models, dirty);
    menu.addListener(SWT.Show, new Listener()
    {
      public void handleEvent(Event event)
      {
        if(dirty[0])
        {
          LOGGER.debug("(Re)creating dynamic menu items");
          dirty[0] = false;
          for(MenuItem m : menu.getItems()) m.dispose();
          addMenuChildren(menu, wi);
        }
      }
    });

    // Add children now because if the menu is attached to a menu item in a
    // menu bar, the item gets disabled if it doesn't have any children. This
    // works only if the list of children is never empty.
    // [TODO] Make it work for empty lists, too
    addMenuChildren(menu, wi);

    return menu;
  }


  private void addMenuChildren(Menu parent, WindowInstance wi)
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
