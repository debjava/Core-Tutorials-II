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

import java.util.*;

import com.novocode.naf.model.IItemListModel;
import com.novocode.naf.model.ModelMap;


/**
 * A group of NGComponents.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Nov 24, 2003
 */

public class NGGroup extends NGModelComponent
{
  @Override
  public void expandSelf(List<? super NGComponent> l, ModelMap models, boolean expandDynamicGroups)
  {
    IItemListModel m = getContentModel(models);
    if(m != null)
    {
      if(expandDynamicGroups)
        for(NGComponent ch : m.getItems()) ch.expandSelf(l, models, expandDynamicGroups);
      else
        l.add(this);
    }
    else
    {
      List<NGComponent> chl = getUnexpandedRoleChildren(null);
      if(chl != null) for(NGComponent ch : chl) ch.expandSelf(l, models, expandDynamicGroups);
    }
  }


  public IItemListModel getContentModel(ModelMap models)
  {
    return getModel("content", models, IItemListModel.class);
  }


  @Override
  protected boolean isDynamic()
  {
    if(getModelBinding("content") != null) return true;
    else return super.isDynamic();
  }
}
