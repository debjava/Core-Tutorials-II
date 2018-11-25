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

package com.novocode.naf.resource;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;

import com.novocode.naf.app.NAFException;


/**
 * An <code>&lt;import&gt;</code> statement.
 *
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Nov 6, 2004
 * @version $Id: Import.java 405 2008-04-25 17:59:28Z szeiger $
 */

public final class Import
{
  private String as, clazz, prefix, suffix, unqualifiedClass;
  private boolean initialized;


  public Import(String className, String as)
  {
    this.clazz = className;
    this.as = as;
    init();
  }


  public Import(Element e)
  {
    Attr a = e.getAttributeNode("class");
    this.clazz = (a == null) ? null : a.getValue();
    a = e.getAttributeNode("as");
    this.as = (a == null) ? null : a.getValue();
    init();
  }


  private void init() throws NAFException
  {
    if(clazz == null || clazz.length() == 0)
      throw new NAFException("An <import> element must contain a non-empty \"class\" attribute");
    if(clazz.indexOf('*') != -1 && as != null)
      throw new NAFException("An <import> element with a class pattern may not contain an \"as\" attribute");

    int sep = clazz.indexOf('*');
    if(sep != -1)
    {
      prefix = clazz.substring(0, sep);
      suffix = clazz.substring(sep+1);
    }
    else
    {
      sep = clazz.lastIndexOf('.');
      if(sep != -1) unqualifiedClass = clazz.substring(sep+1);
      else unqualifiedClass = clazz;
    }
  }


  private String classNameFor(String s)
  {
    if(!initialized)
    {
      init();
      initialized = true;
    }
    if(as != null && as.equals(s)) return clazz;
    else if(prefix != null) return prefix + s + suffix;
    else if(unqualifiedClass.equals(s)) return clazz;
    else return null;
  }
  
  
  public Class<?> classFor(String s)
  {
    String n = classNameFor(s);
    if(n == null) return null;
    try { return Class.forName(n); }
    catch(ClassNotFoundException ex) { return null; }
  }
}
