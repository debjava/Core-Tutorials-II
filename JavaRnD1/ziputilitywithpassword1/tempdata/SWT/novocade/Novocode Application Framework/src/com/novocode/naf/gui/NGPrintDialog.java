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

import org.eclipse.swt.printing.*;

import com.novocode.naf.app.*;
import com.novocode.naf.model.*;


/**
 * A print dialog.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 12, 2003
 */

public final class NGPrintDialog extends NGDialog
{
  public DialogWindowInstance createWindowInstance(NAFApplication app, ModelMap models, WindowInstance parent) throws NAFException
  {
    PrintDialog dialog = new PrintDialog(parent.getShell(false), modality.style);
    String currentTitle = getCurrentTitle(models);
    if(currentTitle != null) dialog.setText(currentTitle);
    return new DialogWindowInstance(this, models, app, dialog);
  }


  public void openDialog(DialogWindowInstance dialog) throws NAFException
  {
    PrintDialog pd = (PrintDialog)dialog.dialog;
    IPrintDataModel pdModel = getModel("print", dialog.models, DefaultPrintDataModel.PTYPE);
    if(pdModel != null)
    {
      pd.setStartPage(pdModel.getStartPage());
      pd.setEndPage(pdModel.getEndPage());
      pd.setScope(pdModel.getScope());
      pd.setPrintToFile(pdModel.getPrintToFile());
    }
    PrinterData data = pd.open();
    if(data != null)
    {
      if(pdModel != null) pdModel.setAll(pd.getStartPage(), pd.getEndPage(), pd.getScope(), pd.getPrintToFile(), data);
      notifyActionListeners("ok", dialog);
    }
    else notifyActionListeners("cancel", dialog);
  }
}
