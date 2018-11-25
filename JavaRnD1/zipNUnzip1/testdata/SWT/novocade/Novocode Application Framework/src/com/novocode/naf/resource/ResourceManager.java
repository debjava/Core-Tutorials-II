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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import com.novocode.naf.app.*;
import com.novocode.naf.resource.xml.XMLResourceLoader;


/**
 * Reads NAF resource files and creates Objects from them.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Nov 24, 2003
 * @version $Id: ResourceManager.java 405 2008-04-25 17:59:28Z szeiger $
 */

public class ResourceManager
{
  private final Map<String, ResourceLoader> loadersForSuffix = new HashMap<String, ResourceLoader>();
  private final Map<String, Resource> resources = new HashMap<String, Resource>();
  private final LinkedList<Import> globalImports = new LinkedList<Import>();


  /**
   * Create a new ResourceManager.
   */

  public ResourceManager() throws NAFException
  {
    setLoaderForSuffix("naf", new XMLResourceLoader(this));
    addGlobalImport(new Import(NGGroup.class.getName(), "group"));
    addGlobalImport(new Import(NGInclude.class.getName(), "include"));
  }


  /**
   * Add a global import for name resolution to the top of the import list (so
   * that later imports override earlier ones). Note that ResourceLoaders are
   * not required to use the global name resolver. The basic XMLResourceLoader
   * does but the JSResourceLoader has its own import mechanism.
   */
  public void addGlobalImport(Import imp) { globalImports.addFirst(imp); }


  public Class<?> resolveGlobalName(String name)
  {
    for(Import im : globalImports)
    {
      Class<?> cl = im.classFor(name);
      if(cl != null) return cl;
    }
    return null;
  }


  /**
   * Get an object from a Resource. The resource is loaded if it hasn't been
   * cached yet. The URL may contain a fragment in which case an object with
   * the specific ID is retrieved from the resource. Without a fragment, the
   * root object of the resource is returned. If the resource or the fragment
   * ID is not found, a NAFException is thrown.
   */
  public final Object getObject(URL absUrl) throws NAFException
  {
    String urlStr = absUrl.toExternalForm();
    String baseStr = urlStr;
    int sep = baseStr.indexOf('#');
    if(sep != -1) baseStr = baseStr.substring(0, sep);

    URL baseUrl;
    try { baseUrl = (baseStr == urlStr) ? absUrl : new URL(baseStr); }
    catch(MalformedURLException ex)
    {
      throw new NAFException("Error creating URL object for "+baseStr, ex);
    }

    Resource res = getResource(baseUrl, baseStr);

    if(sep == -1) return res.getRootObject();

    String ref = absUrl.getRef();
    Object o = res.getObject(ref);
    if(o == null)
      throw new NAFException("NAF resource file "+baseStr+" doesn't contain id \""+ref+"\"");
    return o;
  }


  /**
   * Get a cached Resource object or load it if it hasn't been cached yet.
   * If the file specified by the URL does not exist, a NAFException is thrown.
   * The URL must not contain a fragment.
   */
  public final Resource getResource(URL baseUrl) throws NAFException
  {
    String baseStr = baseUrl.toExternalForm();
    if(baseStr.indexOf('#') != -1)
      throw new NAFException("Base URL must not contain a fragment: "+baseStr);
    return getResource(baseUrl, baseStr);
  }


  private Resource getResource(URL baseUrl, String baseStr) throws NAFException
  {
    Resource res = resources.get(baseStr);
    if(res == null)
    {
      res = findLoaderFor(baseUrl).readResource(baseUrl, this);
      resources.put(baseStr, res);
    }
    return res;
  }


  private ResourceLoader findLoaderFor(URL url) throws NAFException
  {
    String path = url.getPath();
    int sep = path.lastIndexOf('.');
    if(sep == -1) throw new NAFException("Can't find loader for URL "+url+": No suffix");
    String suffix = path.substring(sep+1);
    ResourceLoader l = getLoaderForSuffix(suffix);
    if(l == null) throw new NAFException("Can't find loader for URL "+url+": No loader for suffix \""+suffix+"\"");
    return l;
  }


  public final void setLoaderForSuffix(String suffix, ResourceLoader loader)
  {
    loadersForSuffix.put(suffix, loader);
  }


  public final ResourceLoader getLoaderForSuffix(String suffix)
  {
    return loadersForSuffix.get(suffix);
  }
}
