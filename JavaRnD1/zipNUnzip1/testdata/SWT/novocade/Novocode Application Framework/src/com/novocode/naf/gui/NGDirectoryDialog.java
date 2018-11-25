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

import org.eclipse.swt.widgets.*;

import com.novocode.naf.app.*;
import com.novocode.naf.data.DataDecoder;
import com.novocode.naf.model.*;
import com.novocode.naf.resource.ConfProperty;


/**
 * A directory dialog.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 7, 2003
 */

public final class NGDirectoryDialog extends NGDialog
{
  private String message;


  @ConfProperty public void setMessage(String s) { this.message = s; }
  public String getMessage() { return message; }


  public DialogWindowInstance createWindowInstance(NAFApplication app, ModelMap models, WindowInstance parent) throws NAFException
  {
    DirectoryDialog dialog = new DirectoryDialog(parent.getShell(false), modality.style);
    String currentTitle = getCurrentTitle(models);
    if(currentTitle != null) dialog.setText(currentTitle);
    if(message != null) dialog.setMessage(DataDecoder.decodeBackslashEscapes(message));
    else
    {
      IObjectReadModel<String> messageModel = getModel("message", models, DefaultStringModel.PTYPE_READ);
      if(messageModel != null)
      {
        String s = messageModel.getValue();
        if(s != null) dialog.setMessage(s);
      }
    }
    return new DialogWindowInstance(this, models, app, dialog);
  }


  public void openDialog(DialogWindowInstance dialog) throws NAFException
  {
    DirectoryDialog d = (DirectoryDialog)dialog.dialog;
    IObjectModel<String> model = getModel("directory", dialog.models, DefaultStringModel.PTYPE_FULL);
    if(model != null) d.setFilterPath(model.getValue());
    String dir = d.open();
    if(dir != null)
    {
      if(model != null) model.setValue(dir);
      notifyActionListeners("ok", dialog);
    }
    else notifyActionListeners("cancel", dialog);
  }
}
