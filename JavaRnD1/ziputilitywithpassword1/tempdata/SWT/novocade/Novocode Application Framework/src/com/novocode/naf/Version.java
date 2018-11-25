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

package com.novocode.naf;


/**
 * The Novocode Application Framework version number.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Nov 22, 2003
 * @version $Id: Version.java 371 2008-03-26 22:40:15Z szeiger $
 */

public class Version
{
  /* Avoid public constructor */
  private Version() {}


  /**
   * Provides the version of the Novocode Application Framework
   * against which an application was compiled.
   */

  public static final String STATIC_VERSION = "0.4";


  /**
   * Returns the version of the Novocode Application Framework at run-time.
   */

  public static String getRuntimeVersion() { return STATIC_VERSION; }
}
