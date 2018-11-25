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

package com.novocode.naf.model;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.novocode.naf.app.NAFException;


/**
 * Implementation of ParameterizedType which is used for creating default models.
 *
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since May 4, 2008
 * @version $Id: PType.java 413 2008-05-05 16:57:55Z szeiger $
 */

public final class PType<T> implements ParameterizedType
{
  private final Class<T> rawType;
  private final Class<T> instType;
  private final Type[] actualTypeArguments;

  @SuppressWarnings("unchecked")
  public PType(Class rawType, Class instType, Type... actualTypeArguments)
  {
    this.rawType = rawType;
    this.instType = instType;
    this.actualTypeArguments = actualTypeArguments;
  }

  @Override
  public Type[] getActualTypeArguments() { return actualTypeArguments; }

  @Override
  public Type getOwnerType() { return null; }

  @Override
  public Class<T> getRawType() { return rawType; }

  @Override
  public boolean equals(Object obj)
  {
    if(this == obj) return true;
    if(!(obj instanceof ParameterizedType)) return false;
    ParameterizedType p = (ParameterizedType)obj;
    if(!rawType.equals(p.getRawType())) return false;
    Type[] pa = p.getActualTypeArguments();
    int len =actualTypeArguments.length;
    if(len != pa.length) return false;
    for(int i=0; i<len; i++)
      if(!actualTypeArguments[i].equals(pa[i])) return false;
    return true;
  }

  @Override
  public int hashCode()
  {
    return 42 + rawType.hashCode();
  }

  public T newInstance()
  {
    try
    {
      return instType.newInstance();
    }
    catch(InstantiationException ex)
    {
      throw new NAFException("Error creating default model instance for class "+instType.getName(), ex);
    }
    catch(IllegalAccessException ex)
    {
      throw new NAFException("Error creating default model instance for class "+instType.getName(), ex);
    }
  }
}
