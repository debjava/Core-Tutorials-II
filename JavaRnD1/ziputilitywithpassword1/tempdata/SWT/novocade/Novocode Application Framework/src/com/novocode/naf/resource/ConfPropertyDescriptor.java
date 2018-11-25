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

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.novocode.naf.app.NAFException;
import com.novocode.naf.data.DefaultDataConverter;
import com.novocode.naf.data.IDataConverter;
import com.novocode.naf.data.StringArrayDataConverter;


/**
 * A descriptor for a ConfProperty.
 *
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 17, 2004
 * @version $Id: ConfPropertyDescriptor.java 409 2008-05-03 19:31:39Z szeiger $
 */

public final class ConfPropertyDescriptor
{
  private Method getter, setter, adder;
  private IDataConverter conv;
  private Class<?> propertyClass, propertyComponentClass;
  private Class<? extends ConfigurableFactory> factory;
  private String xmlPropertyName, prefix;
  private boolean isMap;


  public ConfPropertyDescriptor(Class<?> clazz, PropertyDescriptor pd, ConfProperty p, Method annotatedMethod)
  {
    xmlPropertyName = p.value();
    if(xmlPropertyName.isEmpty()) xmlPropertyName = null;
    prefix = p.prefix();
    if(prefix.isEmpty()) prefix = null;
    if(prefix == null && xmlPropertyName == null) xmlPropertyName = pd.getName();
    propertyClass = pd.getPropertyType();
    propertyComponentClass = propertyClass;

    setter = pd.getWriteMethod();
    getter = pd.getReadMethod();

    if(propertyComponentClass.isArray())
    {
      propertyComponentClass = propertyComponentClass.getComponentType();
      String adderName = p.adder();
      if(adderName.isEmpty()) adderName = null;
      if(adderName == null)
      {
        if(setter != null && setter.getName().startsWith("set")) adderName = "add"+setter.getName().substring(3);
        else if(getter != null && getter.getName().startsWith("get")) adderName = "add"+getter.getName().substring(3);
        if(adderName != null && adderName.endsWith("s")) adderName = adderName.substring(0, adderName.length()-1);
      }
      if(adderName != null)
      {
        try { adder = clazz.getMethod(adderName, propertyComponentClass); }
        catch(NoSuchMethodException ex)
        {
          if(!p.adder().isEmpty())
            throw new NAFException("Declared adder method \""+p.adder()+"\" was not found in class "+clazz.getName());
        }
      }
    }

    if(ConfigurableObject.class.isAssignableFrom(propertyComponentClass))
    {
      if(p.factory() != ConfigurableFactory.class) factory = p.factory();
    }
    else if(propertyComponentClass == Map.class)
    {
      isMap = true;
      Type propertyType = (setter != null) ? setter.getGenericParameterTypes()[0] : getter.getGenericReturnType();
      if(propertyType instanceof ParameterizedType)
      {
        ParameterizedType pt = (ParameterizedType)propertyType;
        Type[] gparams = pt.getActualTypeArguments();
        if(gparams[0] != String.class) throw new NAFException("Map properties must have keys of type String");
        if(gparams[1] instanceof Class) propertyComponentClass = (Class<?>)gparams[1];
        else if(gparams[1] instanceof ParameterizedType)
          propertyComponentClass = (Class<?>)((ParameterizedType)gparams[1]).getRawType();
        else throw new NAFException("Illegal type for Map values");
      }
      else
      {
        // Assume Map<String, String> for a raw Map type
        propertyComponentClass = String.class;
      }
    }
    else if(propertyComponentClass == Properties.class)
    {
      isMap = true;
      propertyComponentClass = String.class;
    }
    else
    {
      if(propertyClass == String[].class)
        conv = StringArrayDataConverter.getDefaultInstance();
      else
        conv = DefaultDataConverter.getInstance();
    }
  }


  public Method getReadMethod() { return getter; }


  public Method getAddMethod() { return adder; }


  public Method getWriteMethod() { return setter; }


  public String getXMLPropertyName() { return xmlPropertyName; }


  public String getXMLPropertyPrefix() { return prefix; }


  public void addCompound(Object target, Object compound)
  {
    try
    {
      if(isArray())
      {
        if(adder == null) throw new NAFException("Property "+getReadableName()+" does not have an adder method");
        adder.invoke(target, compound);
      }
      else setter.invoke(target, compound);
    }
    catch(Exception ex)
    {
      Throwable cause = ex.getCause();
      if(cause instanceof RuntimeException) throw (RuntimeException)cause;
      else throw new NAFException("Error invoking adder method for property "+target.getClass().getName()+":"+getReadableName()+": "+ex, ex);
    }
  }


  public void setValue(Object target, Object value) throws NAFException
  {
    if(setter == null) throw new NAFException("Property "+getReadableName()+" does not have a simple setter method");
    try
    {
      setter.invoke(target, value);
    }
    catch(Exception ex)
    {
      Throwable cause = ex.getCause();
      if(cause instanceof RuntimeException) throw (RuntimeException)cause;
      else throw new NAFException("Error invoking setter method for property "+target.getClass().getName()+":"+getReadableName()+": "+ex, ex);
    }
  }


  public void setSimple(Object target, String value) throws NAFException
  {
    if(setter == null) throw new NAFException("Property "+getReadableName()+" does not have a simple setter method");
    Object converted;
    try
    {
      converted = conv.toInternal(propertyClass, value);
    }
    catch(Exception ex)
    {
      throw new NAFException("Error converting data for property "+target.getClass().getName()+":"+getReadableName()+": "+ex, ex);
    }
    setValue(target, converted);
  }


  public String getSimple(Object target) throws NAFException
  {
    if(getter == null) throw new NAFException("Property "+getReadableName()+" is write-only");
    try
    {
      return conv.toExternal(propertyClass, getValue(target));
    }
    catch(Exception ex)
    {
      throw new NAFException("Error converting data for property "+target.getClass().getName()+":"+getReadableName()+": "+ex, ex);
    }
  }


  public Object getValue(Object target) throws NAFException
  {
    if(getter == null) throw new NAFException("Property "+getReadableName()+" is write-only");
    try
    {
      return getter.invoke(target, new Object[0]);
    }
    catch(Exception ex)
    {
      Throwable cause = ex.getCause();
      if(cause instanceof RuntimeException) throw (RuntimeException)cause;
      else throw new NAFException("Error invoking getter method for property "+target.getClass().getName()+":"+getReadableName()+": "+ex, ex);
    }
  }


  @SuppressWarnings("unchecked")
  public Object addMapValue(ConfigurableObject target, Resource resource, Object map, String name, Object value)
  {
    boolean isInst = propertyComponentClass.isInstance(value);
    if(!isInst && propertyComponentClass != String.class && !(value instanceof String && ConfigurableObject.class.isAssignableFrom(propertyComponentClass)))
      throw new NAFException("Value of type "+value.getClass().getName()+
        " cannot be added to map with value type "+propertyComponentClass.getName());
    if(map == null)
      map = (propertyClass == Properties.class) ? new Properties() : new HashMap();
    if(isInst) ((Map)map).put(name, value);
    else if(propertyComponentClass == String.class) ((Map)map).put(name, value == null ? null : String.valueOf(value));
    else // Convert String value to ConfigurableObject
    {
      ConfigurableObject co;
      try { co = (ConfigurableObject)propertyComponentClass.newInstance(); }
      catch(Exception ex) { throw new NAFException("Error instantiating "+propertyComponentClass.getName(), ex); }
      ConfPropertyManager propertyManager = ConfPropertyManager.forClass(propertyComponentClass);
      if(propertyManager != null)
      {
        ConfPropertyDescriptor pd = propertyManager.getAttributePropertyDescriptor(":resource", '.');
        if(pd != null) pd.setValue(co, resource);
        pd = propertyManager.getAttributePropertyDescriptor(":elementName", '.');
        if(pd != null) pd.setValue(co, name);
        pd = propertyManager.getAttributePropertyDescriptor(":parent", '.');
        if(pd != null) pd.setValue(co, target);
        pd = propertyManager.getAttributePropertyDescriptor(":attrName", '.');
        if(pd != null) pd.setValue(co, name);
        pd = propertyManager.getAttributePropertyDescriptor(":attrValue", '.');
        if(pd != null) pd.setValue(co, value);
        ((Map)map).put(name, co);
      }
    }
    return map;
  }


  public boolean isCompound()
  {
    return conv == null;
  }


  public boolean isArray()
  {
    return propertyClass.isArray();
  }


  public boolean isMap()
  {
    return isMap;
  }


  @SuppressWarnings("unchecked")
  public Class<? extends ConfigurableObject> getInstanceType()
  {
    if(conv != null) return null;
    return (Class< ? extends ConfigurableObject>)propertyComponentClass;
  }


  public Class<? extends ConfigurableFactory> getFactory() { return factory; }


  private String getReadableName()
  {
    if(xmlPropertyName != null) return xmlPropertyName;
    else return prefix + ".*";
  }


  public String removePrefixFrom(String name)
  {
    if(prefix == null) return name;
    return name.substring(prefix.length()+1);
  }


  public Class<?> getPropertyClass() { return propertyClass; }
}
