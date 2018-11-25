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

package com.novocode.naf.gui.event;

import java.net.MalformedURLException;
import java.net.URL;

import com.novocode.naf.app.*;
import com.novocode.naf.app.NAFApplication;
import com.novocode.naf.gui.*;


/**
 * ActionListener which opens a dialog.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 8, 2003
 * @version $Id: OpenDialogActionListener.java 417 2008-05-06 22:27:03Z szeiger $
 */

public class OpenDialogActionListener implements IActionListener
{
  private NAFApplication app;
  private URL baseURL;


  public OpenDialogActionListener(NAFApplication app, URL baseURL)
  {
    this.app = app;
    this.baseURL = baseURL;
  }
  

  public void performAction(ActionEvent e) throws NAFException
  {
    URL resURL;
    URL invocationBaseURL = (baseURL != null) ? baseURL : e.source.getResource().getURL();
    try
    {
      resURL = new URL(invocationBaseURL, e.command);
    }
    catch(MalformedURLException ex)
    {
      throw new NAFException("Error creating URL object for resource \""+e.command+"\"", ex);
    }
    IWindowInstanceWidget ngd = app.getResourceObject(resURL, IWindowInstanceWidget.class);
    WindowInstance wi = e.windowInstance;
    WindowInstance dwi = ngd.createWindowInstance(wi.application, wi.models, wi);
    dwi.open();
  }
}
