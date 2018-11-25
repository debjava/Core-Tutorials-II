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
 * Default implementation of the IIntModel interface.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Jan 2, 2004
 */

public class DefaultIntModel extends DefaultModel implements IIntModel, IObjectModel<Integer>
{
  private volatile int value;


  public DefaultIntModel() {}


  public DefaultIntModel(int i)
  {
    this.value = i;
  }


  public int getInt()
  {
    return value;
  }


  public synchronized void setInt(int i)
  {
    if(this.value != i)
    {
      this.value = i;
      notifyListeners();
    }
  }
  
  
  public Integer getValue()
  {
    return new Integer(value);
  }


  public void setValue(Integer i)
  {
    setInt(i.intValue());
  }


  public IBooleanModel getIsValueModel(final int trueValue, final int falseValue)
  {
    return new IBooleanModel()
    {
      public boolean getBoolean()
      {
        return value == trueValue;
      }
      public void setBoolean(boolean b)
      {
        setInt(b ? trueValue : falseValue);
      }
      public void addChangeListener(IChangeListener listener) { DefaultIntModel.this.addChangeListener(listener); }
      public void removeChangeListener(IChangeListener listener) { DefaultIntModel.this.removeChangeListener(listener); }
    };
  }


  public IActionListener getSetToValueActionListener(final int actionValue)
  {
    return new IActionListener()
    {
      public void performAction(ActionEvent e)
      {
        setInt(actionValue);
      }
    };
  }
  
  
  public IObjectReadModel<String> getStringReadModel(final String[] strings)
  {
    return new IObjectReadModel<String>()
    {
      public String getValue()
      {
        int i = value;
        if(i < 0) i = 0;
        else if(i > strings.length-1) i = strings.length-1;
        return strings[i];
      }
      public void addChangeListener(IChangeListener listener) { DefaultIntModel.this.addChangeListener(listener); }
      public void removeChangeListener(IChangeListener listener) { DefaultIntModel.this.removeChangeListener(listener); }
    };
  }
}
