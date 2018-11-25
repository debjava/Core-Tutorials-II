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

package com.novocode.naf.xml;

import org.jaxen.BaseXPath;
import org.jaxen.JaxenException;


/**
 * An XPath implementation for NAF widget hierarchies.
 *
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Nov 17, 2004
 * @version $Id: NAFXPath.java 237 2004-11-18 23:46:14 +0000 (Thu, 18 Nov 2004) szeiger $
 */

public class NAFXPath extends BaseXPath
{
  private static final long serialVersionUID = 3832622897914329144L;


  public NAFXPath(String xpathExpression) throws JaxenException
  {
    super(xpathExpression, NAFNavigator.getInstance());
  }
} 
