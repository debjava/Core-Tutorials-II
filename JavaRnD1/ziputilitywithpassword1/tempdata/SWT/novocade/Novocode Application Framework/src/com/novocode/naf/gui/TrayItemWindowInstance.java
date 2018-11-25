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
import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.*;

import com.novocode.naf.app.NAFApplication;
import com.novocode.naf.model.ModelMap;
import com.novocode.naf.resource.*;


/**
 * A tray item that was created from NGComponent blueprints.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since May 23, 2004
 */

public final class TrayItemWindowInstance extends WindowInstance
{
  private Shell menuShell;
  private TrayItem item;
  

  TrayItemWindowInstance(NGComponent blueprint, TrayItem item, ModelMap models, NAFApplication application)
  {
    super(blueprint, models, application);
    this.item = item;
  }


  public void open()
  {
    try
    {
      if(Thread.currentThread() == swtThread) item.setVisible(true);
      else
      {
        display.asyncExec(new Runnable()
        {
          public void run()
          {
            item.setVisible(true);
          }
        });
      }
    }
    catch(SWTException ignored) {}
  }


  public void close()
  {
    try
    {
      if(Thread.currentThread() == swtThread) item.setVisible(false);
      else
      {
        display.asyncExec(new Runnable()
        {
          public void run()
          {
            item.setVisible(false);
          }
        });
      }
    }
    catch(SWTException ignored) {}
  }
  
  
  public void dispose()
  {
    try
    {
      if(Thread.currentThread() == swtThread)
      {
        item.dispose();
        if(menuShell != null) menuShell.dispose();
      }
      else
      {
        display.asyncExec(new Runnable()
        {
          public void run()
          {
            item.dispose();
            if(menuShell != null) menuShell.dispose();
          }
        });
      }
    }
    catch(SWTException ignored) {}
  }
  
  
  public Shell getShell(boolean createDummy)
  {
    if(!createDummy) return null;
    if(menuShell == null)
    {
      if(Thread.currentThread() == swtThread) menuShell = new Shell(display, SWT.NONE);
      else
      {
        display.syncExec(new Runnable()
        {
          public void run()
          {
            menuShell = new Shell(display, SWT.NONE);
          }
        });
      }
    }
    return menuShell;
  }


  public Widget getDisposeSender()
  {
    return item;
  }
}
