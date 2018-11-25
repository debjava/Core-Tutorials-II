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

import com.novocode.naf.gui.event.ActionEvent;
import com.novocode.naf.gui.event.IActionListener;
import com.novocode.naf.gui.event.IChangeListener;


/**
 * Default implementation of the IObjectModel interface.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since May 30, 2004
 */

public class DefaultObjectModel<T> extends DefaultModel implements IObjectModel<T>
{
  protected volatile T value;


  public DefaultObjectModel() {}
  
  
  public DefaultObjectModel(T o)
  {
    this.value = o;
  }
  
  
  public T getValue()
  {
    return value;
  }


  public synchronized void setValue(T o)
  {
    if(this.value != o)
    {
      this.value = o;
      notifyListeners();
    }
  }


  public IBooleanModel getIsValueModel(final T trueValue, final T falseValue)
  {
    return new IBooleanModel()
    {
      public boolean getBoolean()
      {
        if(value == null) return trueValue == null;
        if(trueValue == null) return false;
        return value.equals(trueValue);
      }
      public void setBoolean(boolean b)
      {
        setValue(b ? trueValue : falseValue);
      }
      public void addChangeListener(IChangeListener listener) { DefaultObjectModel.this.addChangeListener(listener); }
      public void removeChangeListener(IChangeListener listener) { DefaultObjectModel.this.removeChangeListener(listener); }
    };
  }


  public IActionListener getSetToValueActionListener(final T actionValue)
  {
    return new IActionListener()
    {
      public void performAction(ActionEvent e)
      {
        setValue(actionValue);
      }
    };
  }
}
