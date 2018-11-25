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


/**
 * A dialog that was created from NGComponent blueprints.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 8, 2003
 */

public final class DialogWindowInstance extends WindowInstance
{
  public final Dialog dialog;
  private Shell parentShell;


  public DialogWindowInstance(NGDialog blueprint, ModelMap models, NAFApplication application, Dialog dialog)
  {
    super(blueprint, models, application);
    this.dialog = dialog;
    this.parentShell = dialog.getParent();
  }


  public void open()
  {
    try
    {
      if(Thread.currentThread() == swtThread) ((NGDialog)blueprint).openDialog(this);
      else
      {
        display.asyncExec(new Runnable()
        {
          public void run()
          {
            ((NGDialog)blueprint).openDialog(DialogWindowInstance.this);
          }
        });
      }
    }
    catch(SWTException ignored) {}
  }


  public Shell getShell(boolean createDummy)
  {
    return parentShell;
  }


  public Widget getDisposeSender()
  {
    return null;
  }
}
