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

import java.lang.reflect.Type;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;

import com.novocode.naf.app.*;
import com.novocode.naf.data.Alignment;
import com.novocode.naf.data.DataDecoder;
import com.novocode.naf.model.*;
import com.novocode.naf.resource.*;


/**
 * A text or image label.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 6, 2003
 */

public final class NGLabel extends NGTextContainer
{
  private Alignment alignment = Alignment.LEFT;
  private boolean wrap;
  private String imageResource;


  @ConfProperty("align")
  public void setAlignment(Alignment a) { this.alignment = a; }
  public Alignment getAlignment() { return alignment; }

  
  @ConfProperty
  public void setWrap(boolean b) { this.wrap = b; }
  public boolean getWrap() { return wrap; }
  
  
  @ConfProperty("image")
  public void setImageResource(String s) { this.imageResource = s; }
  public String getImageResource() { return imageResource; }


  public Control createControl(Composite parent, NGComponent parentComp, ShellWindowInstance wi, WidgetData pwd) throws NAFException
  {
    int style = alignment.style;
    if(wrap) style |= SWT.WRAP;
    final Label label = new Label(parent, style);
    SWTUtil.setRegisteredImage(imageResource, getResource().getURL(), label, wi.imageManager, false);
    String s = getCachedText();
    if(s != null) label.setText(DataDecoder.decodeAccessKey(DataDecoder.decodeBackslashEscapes(s)));
    IObjectReadModel<?> textModel =
      (IObjectReadModel<?>)getModel("text", wi.models, new Type[] { DefaultStringModel.PTYPE_READ, IObjectReadModel.class });
    hookUpTextReadModel(textModel, "text", label, "setText", "getText", false);
    return label;
  }
}
