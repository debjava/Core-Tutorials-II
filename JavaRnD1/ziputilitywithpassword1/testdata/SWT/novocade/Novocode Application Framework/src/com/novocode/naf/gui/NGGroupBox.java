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
import com.novocode.naf.data.Shadow;
import com.novocode.naf.model.DefaultStringModel;
import com.novocode.naf.resource.*;


/**
 * A composite widget with an etched border and an optional title.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Feb 8, 2004
 */

public final class NGGroupBox extends NGComposite
{
  private Shadow shadow = Shadow.DEFAULT;
  private String text;


  @ConfProperty public void setText(String s) { this.text = s; }
  public String getText() { return text; }


  @ConfProperty public void setShadow(Shadow s) { this.shadow = s; }
  public Shadow getShadow() { return shadow; }


  public Control createControl(Composite parent, NGComponent parentComp, ShellWindowInstance wi, WidgetData pwd) throws NAFException
  {
    Group group = new Group(parent, shadow.style);
    if(text != null) group.setText(DataDecoder.decodeAccessKey(DataDecoder.decodeBackslashEscapes(text)));
    hookUpTextReadModel(getModel("text", wi.models, DefaultStringModel.PTYPE_READ),
      "text", group, "setText", "getText", true);
    return group;
  }
}
