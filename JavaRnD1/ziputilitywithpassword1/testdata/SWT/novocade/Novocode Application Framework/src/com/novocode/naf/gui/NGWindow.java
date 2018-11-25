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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.novocode.naf.app.NAFException;
import com.novocode.naf.model.DefaultStringModel;
import com.novocode.naf.model.IObjectReadModel;
import com.novocode.naf.model.ModelMap;
import com.novocode.naf.resource.NGComponent;
import com.novocode.naf.resource.ConfProperty;


/**
 * Base class for windows.
 *
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Oct 30, 2004
 * @version $Id: NGWindow.java 412 2008-05-04 18:27:58Z szeiger $
 */

public abstract class NGWindow extends NGComposite implements IWindowInstanceWidget
{
  public enum Modality
  {
    MODELESS          (SWT.MODELESS),
    PRIMARY_MODAL     (SWT.PRIMARY_MODAL),
    APPLICATION_MODAL (SWT.APPLICATION_MODAL),
    SYSTEM_MODAL      (SWT.SYSTEM_MODAL);

    public final int style;
    Modality(int style) { this.style = style; }
  }


  protected String title;
  protected Modality modality;


  @ConfProperty
  public void setModality(Modality m) { this.modality = m; }
  public Modality getModality() { return modality; }


  @ConfProperty
  public void setTitle(String s) { this.title = s; }
  public String getTitle() { return title; }
  

  protected final String getCurrentTitle(ModelMap models)
  {
    if(title != null) return title;
    IObjectReadModel<String> titleModel = getModel("title", models, DefaultStringModel.PTYPE_READ);
    if(titleModel != null) return titleModel.getValue();
    return null;
  }


  @Override
  public Control createControl(Composite parent, NGComponent parentComp, ShellWindowInstance wi, WidgetData pwd) throws NAFException
  {
    throw new NAFException("Cannot create a <"+getElementName()+"> control with a "+parent.getClass().getName()+" parent");
  }
}
