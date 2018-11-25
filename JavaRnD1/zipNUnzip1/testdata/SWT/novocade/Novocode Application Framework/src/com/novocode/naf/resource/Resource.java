/*******************************************************************************
 * Copyright (c) 2007 Stefan Zeiger and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.novocode.com/legal/epl-v10.html
 * 
 * Contributors:
 *     Stefan Zeiger (szeiger@novocode.com) - initial API and implementation
 *******************************************************************************/

package com.novocode.naf.resource;

import java.net.URL;
import java.util.HashMap;


/**
 * Base functionality for resource files.
 *
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Mar 9, 2008
 * @version $Id$
 */

public class Resource
{
  protected final ResourceManager resourceManager;
  protected final URL url;

  private final HashMap<String, Object> objectsByID = new HashMap<String, Object>();
  private Object rootObject;


  public Resource(ResourceManager resourceManager, URL url)
  {
    this.resourceManager = resourceManager;
    this.url = url;
  }


  public ResourceManager getResourceManager() { return resourceManager; }


  public URL getURL() { return url; }



  public void putObject(String id, Object o) { objectsByID.put(id, o); }
  public Object getObject(String id) { return objectsByID.get(id); }


  public void setRootObject(Object o) { rootObject = o; }
  public Object getRootObject() { return rootObject; }
}
