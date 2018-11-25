/*******************************************************************************
 * Copyright (c) 2008 Stefan Zeiger and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.novocode.com/legal/epl-v10.html
 * 
 * Contributors:
 *     Stefan Zeiger (szeiger@novocode.com) - initial API and implementation
 *******************************************************************************/

package com.novocode.naf.resource.js;

import java.io.File;
import java.net.URL;

import com.novocode.naf.app.NAFApplication;


/**
 * Standalone launcher for JavaScript NAF applications.
 *
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since May 6, 2008
 * @version $Id: JSRunner.java 417 2008-05-06 22:27:03Z szeiger $
 */

public class JSRunner
{
  public static void main(String[] args) throws Exception
  {
    String res = args[0];
    URL url;
    if(res.startsWith("res:/"))
      url = JSRunner.class.getClassLoader().getResource(res.substring(5));
    else url = new File(res).toURI().toURL();

    NAFApplication app = new NAFApplication(JSRunner.class);
    try
    {
      JSResourceLoader.addTo(app);
      app.getResource(url);
    }
    finally { app.dispose(); }
  }
}
