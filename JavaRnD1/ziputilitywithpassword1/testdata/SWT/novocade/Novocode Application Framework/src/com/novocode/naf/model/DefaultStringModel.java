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

import com.novocode.naf.gui.event.StringChangeEvent;


/**
 * Default implementation of the IStringModel interface.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 5, 2003
 */

public class DefaultStringModel extends DefaultObjectModel<String>
{
  public static final PType<IObjectModel<String>> PTYPE_FULL = new PType<IObjectModel<String>>(IObjectModel.class, DefaultStringModel.class, String.class);
  public static final PType<IObjectReadModel<String>> PTYPE_READ = new PType<IObjectReadModel<String>>(IObjectReadModel.class, DefaultStringModel.class, String.class);
  public static final PType<IObjectModifyModel<String>> PTYPE_MODIFY = new PType<IObjectModifyModel<String>>(IObjectModifyModel.class, DefaultStringModel.class, String.class);


  public DefaultStringModel() {}
  
  
  public DefaultStringModel(String s) { super(s); }
  
  
  public synchronized void append(String s)
  {
    if(this.value == null)
    {
      this.value = s;
      notifyListeners();
    }
    else
    {
      this.value += s;
      notifyListeners(new StringChangeEvent(this, StringChangeEvent.ADD, s));
    }
  }


  public IObjectModel<String> getForwarderModel()
  {
    return new DefaultStringModel(value)
    {
      public void setValue(String s)
      {
        super.setValue(s);
        internalSetValue(s);
      }
    };
  }
  
  
  private void internalSetValue(String s) { setValue(s); }
}
