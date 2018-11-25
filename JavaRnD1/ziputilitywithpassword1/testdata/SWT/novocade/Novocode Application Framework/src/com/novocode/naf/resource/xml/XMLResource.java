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

package com.novocode.naf.resource.xml;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import com.novocode.naf.resource.Import;
import com.novocode.naf.resource.Resource;
import com.novocode.naf.resource.ResourceManager;


/**
 * Extended Resource with import management.
 *
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Apr 24, 2008
 * @version $Id: XMLResource.java 408 2008-05-02 22:13:45Z szeiger $
 */

final class XMLResource extends Resource
{
  private final LinkedList<Import> imports = new LinkedList<Import>();


  public XMLResource(ResourceManager resourceManager, URL url)
  {
    super(resourceManager, url);
  }


  List<Import> getImports() { return imports; }


  void addImport(Import i) { imports.add(i); }


  Class<?> resolveLocalName(String name)
  {
    for(Import im : imports)
    {
      Class<?> cl = im.classFor(name);
      if(cl != null) return cl;
    }
    return null;
  }
}
