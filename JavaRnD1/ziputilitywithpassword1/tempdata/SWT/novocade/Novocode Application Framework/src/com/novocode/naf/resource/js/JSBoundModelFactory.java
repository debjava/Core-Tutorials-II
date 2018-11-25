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

package com.novocode.naf.resource.js;

import java.lang.reflect.Type;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.ScriptableObject;

import com.novocode.naf.app.NAFException;
import com.novocode.naf.gui.event.IActionListener;
import com.novocode.naf.model.ModelBinding.BoundModelFactory;


/**
 * BoundModelFactory for JavaScript model objects.
 *
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since May 6, 2008
 * @version $Id: JSBoundModelFactory.java 417 2008-05-06 22:27:03Z szeiger $
 */

final class JSBoundModelFactory implements BoundModelFactory
{
  private final Object value;
  private final ScriptableObject scope, thisObj;


  public JSBoundModelFactory(Object value, ScriptableObject scope, ScriptableObject thisObj)
  {
    this.value = value;
    this.scope = scope;
    this.thisObj = thisObj;
  }


  @Override
  public Object createModel(Type[] modelTypes) throws NAFException
  {
    for(Type t : modelTypes)
    {
      if(!(t instanceof Class)) continue;
      Class<?> cl = (Class<?>)t;
      if(value instanceof NativeJavaObject && cl.isInstance(((NativeJavaObject)value).unwrap()))
        return ((NativeJavaObject)value).unwrap();
      if(cl.isAssignableFrom(IActionListener.class) && value instanceof BaseFunction)
        return new JSActionListener((BaseFunction)value, scope, thisObj);
    }
    throw new NAFException("Cannot bind JS model: Supplied object "+value+" cannot be converted to any of the requested model types");
  }
}
