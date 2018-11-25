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

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

import com.novocode.naf.app.*;
import com.novocode.naf.model.*;


/**
 * A font dialog.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 12, 2003
 */

public final class NGFontDialog extends NGDialog
{
  public DialogWindowInstance createWindowInstance(NAFApplication app, ModelMap models, WindowInstance parent) throws NAFException
  {
    FontDialog dialog = new FontDialog(parent.getShell(false), modality.style);
    String currentTitle = getCurrentTitle(models);
    if(currentTitle != null) dialog.setText(currentTitle);
    return new DialogWindowInstance(this, models, app, dialog);
  }


  public void openDialog(DialogWindowInstance dialog) throws NAFException
  {
    FontDialog fd = (FontDialog)dialog.dialog;
    IObjectModel<RGB> colorModel = getModel("color", dialog.models, DefaultColorModel.PTYPE_FULL);
    IObjectModel<FontData[]> fontModel = getModel("font", dialog.models, DefaultFontModel.PTYPE_FULL);
    if(colorModel != null) fd.setRGB(colorModel.getValue());
    if(fontModel != null) fd.setFontList(fontModel.getValue());
    fd.open();
    RGB selectedColor = fd.getRGB();
    FontData[] selectedFont = fd.getFontList();
    if(selectedFont != null)
    {
      if(colorModel != null && selectedColor != null) colorModel.setValue(selectedColor);
      if(fontModel != null) fontModel.setValue(selectedFont);
      notifyActionListeners("ok", dialog);
    }
    else notifyActionListeners("cancel", dialog);
  }
}
