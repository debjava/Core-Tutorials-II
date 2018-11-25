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

import java.util.*;

import com.novocode.naf.app.*;
import com.novocode.naf.gui.layout.*;
import com.novocode.naf.resource.*;


/**
 * Creates GUI blueprints from NAF resource files.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Nov 24, 2003
 */

public final class GUIResourceManager extends ResourceManager
{
  private Map<String, Class<?>> layoutClasses = new HashMap<String, Class<?>>();


  /**
   * Create a new ResourceManager.
   */

  public GUIResourceManager() throws NAFException
  {
    addGlobalImport(new Import("com.novocode.naf.gui.NG*", null));

    setLayoutClass("fill",  NGFillLayout.class);
    setLayoutClass("grid",  NGGridLayout.class);
    setLayoutClass("row",   NGRowLayout.class);
    setLayoutClass("form",  NGFormLayout.class);
    setLayoutClass("stack", NGStackLayout.class);
    setLayoutClass("table-wrap", NGTableWrapLayout.class);
  }


  public void setLayoutClass(String name, Class<?> clazz)
  {
    if(clazz == null) layoutClasses.remove(name);
    else layoutClasses.put(name, clazz);
  }


  public NGLayout createLayout(String layoutType) throws NAFException
  {
    Class<?> lcClass = layoutClasses.get(layoutType);
    if(lcClass == null) throw new NAFException("Unsupported layout type \""+layoutType+"\"");
    try { return (NGLayout)lcClass.newInstance(); }
    catch(Exception ex) { throw new NAFException("Error instantiating layout class "+lcClass.getName(), ex); }
  }
}
