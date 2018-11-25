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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.novocode.naf.app.NAFException;
import com.novocode.naf.data.DataDecoder;
import com.novocode.naf.gui.event.DefaultActionBroadcaster;
import com.novocode.naf.gui.event.IActionListener;
import com.novocode.naf.gui.event.IActionSource;
import com.novocode.naf.model.*;
import com.novocode.naf.model.ModelBinding.BoundModelFactory;


/**
 * Base class for all NAF GUI components.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Feb 20, 2004
 */

public abstract class NGModelComponent extends NGComponent
{
  private static final HashSet<String> EMPTY_STRING_SET = new HashSet<String>();

  private HashMap<String, ModelBinding> modelBindings;
  private Map<String, ModelBinding> simpleModelBindings;
  private boolean initialized;


  @ConfProperty(prefix = "m")
  public void setSimpleModelBindings(Map<String, ModelBinding> p) { simpleModelBindings = p; }
  public Map<String, ModelBinding> getSimpleModelBindings() { return simpleModelBindings; }


  protected Map<String, ModelBinding> getModelBindingsMap()
  {
    if(!initialized)
    {
      if(simpleModelBindings != null)
      {
        if(modelBindings == null) modelBindings = new HashMap<String, ModelBinding>();
        modelBindings.putAll(simpleModelBindings);
      }
      initialized = true;
    }
    return modelBindings;
  }


  @ConfProperty("model")
  public void setModelBindings(ModelBinding[] mbs)
  {
    if(modelBindings == null) modelBindings = new HashMap<String, ModelBinding>();
    else modelBindings.clear();
    for(ModelBinding mb : mbs) addModelBinding(mb);
  }
  public void addModelBinding(ModelBinding mb)
  {
    if(modelBindings == null) modelBindings = new HashMap<String, ModelBinding>();
    modelBindings.put(mb.getType(), mb);
  }


  @SuppressWarnings("unchecked")
  public final <T, U extends T> U getModel(String type, ModelMap models, Class<T> modelClass)
  {
    return (U)getModel(type, models, new Type[] { modelClass });
  }


  @SuppressWarnings("unchecked")
  public final <T> T getModel(String type, ModelMap models, PType<T> modelType)
  {
    return (T)getModel(type, models, new Type[] { modelType });
  }


  public final Object getModel(String type, ModelMap models, Type... modelTypes)
  {
    if(getModelBindingsMap() == null) return null;
    ModelBinding mb = modelBindings.get(type);
    if(mb == null) return null;
    String mid = mb.getID();
    Object model = mid == null ? null : models.get(mid);
    if(model == null)
    {
      BoundModelFactory bmf = mb.getBoundModelFactory();
      if(bmf != null)
        model = bmf.createModel(modelTypes);
      else if(mb.getCreate() != ModelBinding.Create.NO)
        model = createDefaultModel(mb, modelTypes);
      if(model != null && mid != null) models.put(mid, model);
    }
    return model;
  }
  
  
  public final ModelBinding getModelBinding(String type)
  {
    if(getModelBindingsMap() == null) return null;
    ModelBinding mb = modelBindings.get(type);
    return mb;
  }


  public final Set<String> getModelTypes()
  {
    if(getModelBindingsMap() == null) return EMPTY_STRING_SET;
    return modelBindings.keySet();
  }

  
  protected Object createDefaultModel(ModelBinding mb, Type[] modelTypes)
  {
    if(modelTypes.length == 0)
      throw new NAFException("Cannot create model for default model for model type \""+mb.getType()+"\" without a model class");
    for(Type t : modelTypes)
    {
      if(t instanceof PType) return ((PType<?>)t).newInstance();
      if(t instanceof ParameterizedType) t = ((ParameterizedType)t).getRawType();
      if(t == IIntReadModel.class || t == IIntModifyModel.class || t == IIntModel.class) return new DefaultIntModel();
      if(t == IStringListModel.class) return new DefaultStringListModel();
      if(t == IBooleanReadModel.class || t == IBooleanModifyModel.class || t == IBooleanModel.class)
        return new DefaultBooleanModel(DataDecoder.decodeBoolean(mb.getAttribute("initial"), false));
      if(t == IItemListModel.class) return new DefaultItemListModel();
      if(t == IObjectReadModel.class || t == IObjectModifyModel.class || t == IObjectModel.class) return new DefaultObjectModel<Object>();
      if(t == IActionSource.class || t == IActionListener.class) return new DefaultActionBroadcaster(mb.getID());
      if(t == IProgressModifyModel.class || t == IProgressReadModel.class || t == IProgressModel.class) return new DefaultProgressModel();
    }
    throw new NAFException("No default model for model type \""+mb.getType()+"\" available for any of the requested model classes");
  }
}
