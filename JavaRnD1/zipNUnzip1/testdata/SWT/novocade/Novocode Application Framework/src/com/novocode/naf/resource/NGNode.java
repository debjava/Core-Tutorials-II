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

package com.novocode.naf.resource;

import java.util.ArrayList;

import com.novocode.naf.app.*;


/**
 * Base class for resource objects which form a hierarchy.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Mar 28, 2008
 * @version $Id$
 */

public abstract class NGNode implements ConfigurableObject, Cloneable
{
  private String elementName;
  private NGNode parent;
  private Resource resource;
  private String id;
  private ArrayList<NGNode> children;
  private NGNode[] cachedChildrenArray;

  
  @ConfProperty("id")
  public void setID(String id) { this.id = id; }
  public String getID() { return id; }


  @ConfProperty(":parent")
  public final void setParent(NGNode c) { parent = c; }
  public final NGNode getParent() { return parent; }


  @ConfProperty(":elementName")
  public final void setElementName(String s) { elementName = s; }
  public final String getElementName() { return elementName; }


  @ConfProperty(":resource")
  public void setResource(Resource resource) { this.resource = resource; }
  public Resource getResource() { return resource; }


  @ConfProperty(value = ":nested", adder = "addChild")
  public final void setChildren(NGNode[] a)
  {
    if(a == null) children = null;
    else
    {
      if(children == null) children = new ArrayList<NGNode>(a.length);
      for(NGNode ch : a) addChild(ch);
    }
  }
  public void addChild(NGNode ch)
  {
    if(children == null) children = new ArrayList<NGNode>();
    children.add(ch);
  }
  public NGNode[] getChildren()
  {
    if(cachedChildrenArray == null)
    {
      cachedChildrenArray =
        children == null ? new NGNode[0] : children.toArray(new NGNode[children.size()]);
    }
    return cachedChildrenArray;
  }


  public Object clone()
  {
    try { return super.clone(); } catch(CloneNotSupportedException ex) { throw new NAFException(ex); }
  }
}
