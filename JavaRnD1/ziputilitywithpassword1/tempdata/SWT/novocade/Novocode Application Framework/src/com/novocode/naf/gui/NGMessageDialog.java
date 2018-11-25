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
import com.novocode.naf.model.DefaultStringModel;
import com.novocode.naf.model.IObjectReadModel;
import com.novocode.naf.model.ModelMap;
import com.novocode.naf.resource.ConfProperty;


/**
 * A message dialog.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 8, 2003
 */

public final class NGMessageDialog extends NGDialog
{
  private String message;
  private Icon icon = Icon.NONE;
  private Button[] buttons;

  public enum Icon
  {
    NONE (SWT.NONE), ERROR (SWT.ICON_ERROR), INFORMATION (SWT.ICON_INFORMATION),
    QUESTION (SWT.ICON_QUESTION), WARNING (SWT.ICON_WARNING), WORKING (SWT.ICON_WORKING);
    
    public final int style;
    Icon(int style) { this.style = style; }
  }

  public enum Button
  {
    OK (SWT.OK), CANCEL (SWT.CANCEL), YES (SWT.YES), NO (SWT.NO),
    RETRY (SWT.RETRY), ABORT (SWT.ABORT), IGNORE (SWT.IGNORE);
    
    public final int style;
    Button(int style) { this.style = style; }

    public static int style(Button[] a)
    {
      if(a == null) return 0;
      int res = 0;
      for(Button b : a) if(b != null) res |= b.style;
      return res;
    }
  }


  @ConfProperty
  public void setMessage(String s) { this.message = s; }
  public String getMessage() { return message; }
  
  
  @ConfProperty
  public void setIcon(Icon i) { this.icon = i; }
  public Icon getIcon() { return icon; }
  
  
  @ConfProperty
  public void setButtons(Button[] i) { this.buttons = i; }
  public Button[] getButtons() { return buttons; }


  public DialogWindowInstance createWindowInstance(NAFApplication app, ModelMap models, WindowInstance parent) throws NAFException
  {
    MessageBox dialog = new MessageBox(parent.getShell(false), modality.style | icon.style | Button.style(buttons));
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
    MessageBox mb = (MessageBox)dialog.dialog;
    int result = mb.open();
    String mname = null;
    switch(result)
    {
      case SWT.OK:     mname = "ok";     break;
      case SWT.CANCEL: mname = "cancel"; break;
      case SWT.YES:    mname = "yes";    break;
      case SWT.NO:     mname = "no";     break;
      case SWT.RETRY:  mname = "retry";  break;
      case SWT.ABORT:  mname = "abort";  break;
      case SWT.IGNORE: mname = "ignore"; break;
    }
    notifyActionListeners(mname, dialog);
  }
}
