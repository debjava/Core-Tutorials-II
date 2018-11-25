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

import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.*;

import com.novocode.naf.app.*;
import com.novocode.naf.model.*;


/**
 * A color dialog.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 9, 2003
 */

public final class NGColorDialog extends NGDialog
{
  public DialogWindowInstance createWindowInstance(NAFApplication app, ModelMap models, WindowInstance parent) throws NAFException
  {
    ColorDialog dialog = new ColorDialog(parent.getShell(false), modality.style);
    String currentTitle = getCurrentTitle(models);
    if(currentTitle != null) dialog.setText(currentTitle);
    return new DialogWindowInstance(this, models, app, dialog);
  }


  public void openDialog(DialogWindowInstance dialog) throws NAFException
  {
    ColorDialog cd = (ColorDialog)dialog.dialog;
    IObjectModel<RGB> model = getModel("color", dialog.models, DefaultColorModel.PTYPE_FULL);
    if(model != null) cd.setRGB(model.getValue());
    RGB selected = cd.open();
    if(selected != null)
    {
      if(model != null) model.setValue(selected);
      notifyActionListeners("ok", dialog);
    }
    else notifyActionListeners("cancel", dialog);
  }
}
