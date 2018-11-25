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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.novocode.naf.app.NAFException;
import com.novocode.naf.gui.event.ActionEvent;
import com.novocode.naf.gui.event.ActionMethod;
import com.novocode.naf.gui.event.DefaultActionBroadcaster;
import com.novocode.naf.gui.event.IActionBroadcaster;
import com.novocode.naf.gui.event.IActionListener;


/**
 * A container for a set of named models.
 *
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Jan 14, 2005
 * @version $Id: ModelMap.java 417 2008-05-06 22:27:03Z szeiger $
 */

public final class ModelMap
{
  private static final Logger LOGGER = LoggerFactory.getLogger(ModelMap.class);
  private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

  private HashMap<String, Object> map = new HashMap<String,Object>();


  public ModelMap()
  {
  }


  @SuppressWarnings("unchecked")
  public <T> T get(String name)
  {
    return (T)map.get(name);
  }


  public <T> T put(String name, T model)
  {
    map.put(name, model);
    return model;
  }
  
  
  public Set<Map.Entry<String, Object>> entrySet()
  {
    return map.entrySet();
  }


  /**
   * Add ActionListeners for all static and instance methods in the specified
   * target object which are marked with an ActionMethod annotation.
   */
  public void addAutoModels(Object target) throws NAFException
  {
    addAutoModels(target, target.getClass());
  }


  /**
   * Add ActionListeners for all static methods in the specified target
   * class which are marked with an ActionMethod annotation.
   */
  public void addAutoModels(Class<?> clazz) throws NAFException
  {
    addAutoModels(null, clazz);
  }


  private void addAutoModels(final Object target, Class<?> clazz) throws NAFException
  {
    for(final Method method : clazz.getMethods())
    {
      ActionMethod am = method.getAnnotation(ActionMethod.class);
      if(am == null) continue;
      IActionListener al = actionListenerFor(target, clazz, method);
      String modelName = am.value();
      if(modelName == null || modelName.length() == 0) modelName = method.getName();
      if(LOGGER.isDebugEnabled())
        LOGGER.debug("Creating ActionListener for method "+method.getName()+" as model "+modelName);
      Object old = get(modelName);
      if(old == null) put(modelName, al);
      else if(old instanceof IActionBroadcaster) ((IActionBroadcaster)old).addActionListener(al);
      else if(old instanceof IActionListener)
      {
        DefaultActionBroadcaster bc = new DefaultActionBroadcaster();
        bc.addActionListener((IActionListener)old);
        bc.addActionListener(al);
        put(modelName, bc);
      }
      else throw new NAFException("Model "+modelName+" already has a non-IActionListener object attached");
    }
    for(final Field field : clazz.getFields())
    {
      ModelField mf = field.getAnnotation(ModelField.class);
      if(mf == null) continue;
      String modelName = mf.value();
      if(modelName == null || modelName.length() == 0) modelName = field.getName();
      Object old = get(modelName);
      Object model;
      try { model = field.get(target); }
      catch(Exception ex) { throw new NAFException("Error reading model field "+field.getName(), ex); }
      if(old != null && model != null)
      {
        if(old != model)
          throw new NAFException("Model "+modelName+" already has an object attached which differs from the annotated ModelField");
      }
      else if(old != null)
      {
        if(LOGGER.isDebugEnabled())
          LOGGER.debug("Setting field "+field.getName()+" to existing model "+modelName);
        try { field.set(target, old); }
        catch(Exception ex) { throw new NAFException("Error setting model field "+field.getName(), ex); }
      }
      else if(model != null)
      {
        if(LOGGER.isDebugEnabled())
          LOGGER.debug("Adding field "+field.getName()+" as model "+modelName);
        put(modelName, model);
      }
      else
      {
        if(LOGGER.isDebugEnabled())
          LOGGER.debug("Creating model "+modelName+" and setting field "+field.getName()+" to it");
        Class<?> ftype = field.getType();
        try { model = ftype.newInstance(); }
        catch(Exception ex)
        {
          throw new NAFException("Error instantiating model field "+field.getName()+" of type "+ftype.getName(), ex);
        }
        try { field.set(target, model); }
        catch(Exception ex) { throw new NAFException("Error setting model field "+field.getName(), ex); }
        put(modelName, model);
      }
    }
  }


  private static final IActionListener actionListenerFor(final Object target, Class<?> clazz, final Method method) throws NAFException
  {
    final String qname = clazz.getName()+"."+method.getName();
    Class<?>[] paramTypes = method.getParameterTypes();
    if(paramTypes.length == 0)
    {
      return new IActionListener()
      {
        public void performAction(ActionEvent e)
        {
          try { method.invoke(target, EMPTY_OBJECT_ARRAY); }
          catch(Exception ex) { throw new NAFException("Error invoking method "+qname, ex); }
        }
      };
    }
    else if(paramTypes.length == 1 && paramTypes[0] == ActionEvent.class)
    {
      return new IActionListener()
      {
        public void performAction(ActionEvent e)
        {
          try { method.invoke(target, e); }
          catch(Exception ex) { throw new NAFException("Error invoking method "+qname, ex); }
        }
      };
    }
    else throw new NAFException("ActionMethod "+qname+" must have a signature () or (ActionEvent)");
  }
}
