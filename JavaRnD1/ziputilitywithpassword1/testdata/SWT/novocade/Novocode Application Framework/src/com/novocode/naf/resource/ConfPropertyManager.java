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

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.novocode.naf.app.NAFException;


/**
 * Manages the set of XML properties for a class.
 *
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 8, 2004
 * @version $Id: ConfPropertyManager.java 407 2008-04-27 19:09:53Z szeiger $
 */

public final class ConfPropertyManager
{
  private static final Logger LOGGER = LoggerFactory.getLogger(ConfPropertyManager.class);
  private static final HashMap<Class<?>, ConfPropertyManager> xmlPropertyManagers = new HashMap<Class<?>, ConfPropertyManager>();

  private final HashMap<String, ConfPropertyDescriptor> attrProperties = new HashMap<String, ConfPropertyDescriptor>();
  private final HashMap<String, ConfPropertyDescriptor> elemProperties = new HashMap<String, ConfPropertyDescriptor>();
  private final ArrayList<ConfPropertyDescriptor> attrPrefixProperties = new ArrayList<ConfPropertyDescriptor>();
  private final ConfPropertyDescriptor nestedElementsPropertyDescriptor;
  private final ConfPropertyDescriptor otherPropertyDescriptor;
  private final Class<?> clazz;

  private HashSet<String> required;


  public static ConfPropertyManager forClass(Class<?> clazz) throws NAFException
  {
    ConfPropertyManager xpm = xmlPropertyManagers.get(clazz);
    if(xpm == null)
    {
      try
      {
        xpm = new ConfPropertyManager(clazz);
      }
      catch(IntrospectionException ex) { throw new NAFException("Error introspecting "+clazz.getName()+" for XML properties", ex); }
      xmlPropertyManagers.put(clazz, xpm);
    }
    return xpm;
  }


  private ConfPropertyManager(Class<?> clazz) throws IntrospectionException
  {
    this.clazz = clazz;
    for(PropertyDescriptor pd : Introspector.getBeanInfo(clazz).getPropertyDescriptors())
      addProperty(pd);
    nestedElementsPropertyDescriptor = elemProperties.get(":nested");
    otherPropertyDescriptor = elemProperties.get(":other");
  }


  private void addProperty(PropertyDescriptor pd)
  {
    ConfProperty p = null;
    Method wm = pd.getWriteMethod(), rm = pd.getReadMethod(), am = null;
    if(wm != null) { p = wm.getAnnotation(ConfProperty.class); am = wm; }
    if(p == null && rm != null) { p = rm.getAnnotation(ConfProperty.class); am = rm; }
    if(p == null) return;

    if(LOGGER.isDebugEnabled())
      LOGGER.debug("Found XMLProperty in {} for property {}", clazz.getName(), pd.getName());
    ConfPropertyDescriptor xpd = new ConfPropertyDescriptor(clazz, pd, p, am);
    String name = xpd.getXMLPropertyName();
    if(name != null)
    {
      if(xpd.isCompound()) elemProperties.put(xpd.getXMLPropertyName(), xpd);
      else
      {
        if(p.required())
        {
          if(required == null) required = new HashSet<String>();
          required.add(name);
        }
        attrProperties.put(xpd.getXMLPropertyName(), xpd);
      }
    }
    String prefix = xpd.getXMLPropertyPrefix();
    if(prefix != null) attrPrefixProperties.add(xpd);
  }


  public Collection<ConfPropertyDescriptor> getAttributePropertyDescriptors()
  {
    return attrProperties.values();
  }


  public Collection<ConfPropertyDescriptor> getElementPropertyDescriptors()
  {
    return elemProperties.values();
  }


  public ConfPropertyDescriptor getNestedElementsPropertyDescriptor()
  {
    return nestedElementsPropertyDescriptor;
  }


  public ConfPropertyDescriptor getAttributePropertyDescriptor(String name, char prefixSeparator)
  {
    ConfPropertyDescriptor pd = attrProperties.get(name);
    if(pd != null) return pd;
    for(ConfPropertyDescriptor pd2 : attrPrefixProperties)
      if(name.startsWith(pd2.getXMLPropertyPrefix()+prefixSeparator)) return pd2;
    if(name.startsWith(":")) return null; // Don't use :other for pseudo-properties
    return otherPropertyDescriptor;
  }


  public ConfPropertyDescriptor getElementPropertyDescriptor(String name)
  {
    return elemProperties.get(name);
  }


  public Set<String> getRequiredCopy()
  {
    if(required == null) return null;
    return new HashSet<String>(required);
  }
}
