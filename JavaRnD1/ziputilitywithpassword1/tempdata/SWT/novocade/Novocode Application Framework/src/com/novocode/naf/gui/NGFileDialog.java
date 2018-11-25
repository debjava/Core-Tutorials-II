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
import com.novocode.naf.model.ModelMap;
import com.novocode.naf.resource.ConfProperty;


/**
 * A file dialog.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 9, 2003
 */

public final class NGFileDialog extends NGDialog
{
  private String[] extensions;
  private boolean multi;
  private Mode mode = Mode.OPEN;


  public enum Mode { OPEN, SAVE }


  @ConfProperty
  public void setExtensions(String[] a) { this.extensions = a; }
  public String[] getExtensions() { return extensions; }


  @ConfProperty public void setMode(Mode m) { this.mode = m; }
  public Mode getMode() { return mode; }

  
  @ConfProperty public void setMulti(boolean b) { this.multi = b; }
  public boolean isMulti() { return multi; }


  public DialogWindowInstance createWindowInstance(NAFApplication app, ModelMap models, WindowInstance parent) throws NAFException
  {
    FileDialog dialog = new FileDialog(parent.getShell(false),
      modality.style | (mode == Mode.SAVE ? SWT.SAVE : SWT.OPEN) | (multi ? SWT.MULTI : SWT.NONE));
    String currentTitle = getCurrentTitle(models);
    if(currentTitle != null) dialog.setText(currentTitle);
    return new DialogWindowInstance(this, models, app, dialog);
  }


  public void openDialog(DialogWindowInstance dialog) throws NAFException
  {
    FileDialog d = (FileDialog)dialog.dialog;
    // [TODO] implement models
    /*StringModel model = (StringModel)getModel("directory", dialog.models);
    if(model != null) d.setFilterPath(model.getString());*/
    d.open();
    /*String mname;
    if(dir != null)
    {
      mname = "ok";
      if(model != null) model.setString(dir);
    }
    else // dialog cancelled
    {
      mname = "cancel";
    }

    ActionListener actionListener = (ActionListener)getModel(mname, dialog.models);
    if(actionListener == null) actionListener = (ActionListener)getModel("action", dialog.models);
    if(actionListener != null)
    {
      ActionEvent ae = new ActionEvent(this, mname, dialog);
      actionListener.performAction(ae);
    }*/
  }
}
