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

import com.novocode.naf.gui.event.IChangeListener;


/**
 * Default implementation of the IBooleanModel interface.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 2, 2003
 */

public class DefaultBooleanModel extends DefaultModel implements IBooleanModel, IObjectModel<Boolean>
{
  private volatile boolean b;
  private IBooleanModel negatedModel;


  public DefaultBooleanModel() {}


  public DefaultBooleanModel(boolean b)
  {
    this.b = b;
  }


  public boolean getBoolean()
  {
    return b;
  }

  public synchronized void setBoolean(boolean b)
  {
    if(this.b != b)
    {
      this.b = b;
      notifyListeners();
    }
  }


  public synchronized IBooleanModel negatedModel()
  {
    if(negatedModel == null)
    {
      negatedModel = new IBooleanModel()
      {
        public boolean getBoolean() { return !DefaultBooleanModel.this.getBoolean(); }
        public void setBoolean(boolean b) { DefaultBooleanModel.this.setBoolean(!b); }
        public void addChangeListener(IChangeListener listener) { DefaultBooleanModel.this.addChangeListener(listener); }
        public void removeChangeListener(IChangeListener listener) { DefaultBooleanModel.this.removeChangeListener(listener); }
      };
    }
    return negatedModel;
  }


  public Boolean getValue()
  {
    return b ? Boolean.TRUE : Boolean.FALSE;
  }


  public void setValue(Boolean o)
  {
    b = (o == Boolean.TRUE);
  }
}
