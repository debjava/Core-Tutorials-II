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

import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.*;

import com.novocode.naf.app.NAFApplication;
import com.novocode.naf.model.ModelMap;
import com.novocode.naf.resource.*;


/**
 * A shell that was created from NGComponent blueprints.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 8, 2003
 */

public final class ShellWindowInstance extends WindowInstance
{
  public final Shell shell;


  ShellWindowInstance(NGComponent blueprint, ModelMap models, NAFApplication application, Shell shell)
  {
    super(blueprint, models, application);
    this.shell = shell;
  }


  public void open()
  {
    try
    {
      if(Thread.currentThread() == swtThread) shell.open();
      else
      {
        display.asyncExec(new Runnable()
        {
          public void run()
          {
            shell.open();
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
      if(Thread.currentThread() == swtThread) shell.close();
      else
      {
        display.asyncExec(new Runnable()
        {
          public void run()
          {
            shell.close();
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
      if(Thread.currentThread() == swtThread) shell.dispose();
      else
      {
        display.asyncExec(new Runnable()
        {
          public void run()
          {
            shell.dispose();
          }
        });
      }
    }
    catch(SWTException ignored) {}
  }
  
  
  public Shell getShell(boolean createDummy)
  {
    return shell;
  }


  public Widget getDisposeSender()
  {
    return shell;
  }
}
