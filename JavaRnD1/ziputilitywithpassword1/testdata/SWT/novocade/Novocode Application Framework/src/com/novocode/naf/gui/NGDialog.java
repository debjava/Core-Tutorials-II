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

import com.novocode.naf.app.*;
import com.novocode.naf.gui.event.*;
import com.novocode.naf.model.ModelMap;


/**
 * Base class for special system dialogs which take over the SWT event handling thread.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 7, 2003
 */

public abstract class NGDialog extends NGWindow
{
  protected NGDialog()
  {
    modality = Modality.PRIMARY_MODAL;
  }


  @Override
  public abstract DialogWindowInstance createWindowInstance(NAFApplication app, ModelMap models, WindowInstance parent) throws NAFException;


  public abstract void openDialog(DialogWindowInstance dialog) throws NAFException;


  protected void notifyActionListeners(String action, WindowInstance dialog)
  {
    if(action == null) return;
    IActionListener actionListener = getModel(action, dialog.models, IActionListener.class);
    if(actionListener == null) actionListener = getModel("action", dialog.models, IActionListener.class);
    if(actionListener != null)
    {
      ActionEvent ae = new ActionEvent(this, action, dialog);
      actionListener.performAction(ae);
    }
  }
}
