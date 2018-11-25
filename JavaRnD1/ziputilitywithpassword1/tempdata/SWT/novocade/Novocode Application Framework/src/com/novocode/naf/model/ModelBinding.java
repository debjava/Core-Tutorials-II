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

package com.novocode.naf.model;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Map.Entry;

import com.novocode.naf.app.NAFException;
import com.novocode.naf.resource.ConfigurableObject;
import com.novocode.naf.resource.ConfProperty;


/**
 * A model binding with parameters.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Jan 29, 2004
 */

public final class ModelBinding implements ConfigurableObject
{
  public enum Create { DEFAULT, NO, WIDGET }
  
  private String id, type;
  private Create create = Create.DEFAULT;
  private boolean persist;
  private Map<String, String> attributes;
  private BoundModelFactory boundModelFactory;


  public static interface BoundModelFactory
  {
    public Object createModel(Type[] modelTypes) throws NAFException;
  }


  public ModelBinding() throws NAFException
  {
  }


  public ModelBinding(String id, String type) throws NAFException
  {
    setID(id);
    setType(type);
  }


  @ConfProperty(value = "id")
  public void setID(String id)
  {
    if(id == null || id.isEmpty()) throw new NAFException("id must not be empty");
    this.id = id;
  }
  public String getID() { return id; }


  @ConfProperty(required = true)
  public void setType(String type)
  {
    if(type == null || type.isEmpty()) throw new NAFException("type must not be empty");
    this.type = type;
  }
  public String getType() { return type; }


  @ConfProperty
  public void setCreate(Create c) { create = c; }
  public Create getCreate() { return create; }


  @ConfProperty
  public void setPersist(boolean b) { persist = b; }
  public boolean isPersist() { return persist; }


  @ConfProperty(":other")
  public void setAttributes(Map<String, String> p) { attributes = p; }
  public Map<String, String> getAttributes() { return attributes; }


  public String getAttribute(String key)
  {
    if(attributes == null) return null;
    else return attributes.get(key);
  }


  @ConfProperty(":attrName")
  public void setAttrName(String s) { setType(s); }

  @ConfProperty(":attrValue")
  public void setAttrValue(String s) { setID(s); }


  @ConfProperty("bind")
  public void setBoundModelFactory(BoundModelFactory o) { boundModelFactory = o; }
  public BoundModelFactory getBoundModelFactory() { return boundModelFactory; }


  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append("[ModelBinding: type="+type+" id="+getID()+" create="+create+" persist="+persist);
    if(attributes != null)
    {
      for(Entry<String, String> me : attributes.entrySet())
        sb.append(' ').append(me.getKey()).append('=').append(me.getValue());
    }
    sb.append(']');
    return sb.toString();
  }
}
