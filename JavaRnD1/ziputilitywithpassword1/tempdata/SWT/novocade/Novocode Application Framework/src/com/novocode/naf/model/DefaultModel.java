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

import com.novocode.naf.gui.event.*;


/**
 * Default implementation of the IModel interface.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Jan 3, 2004
 */

public abstract class DefaultModel implements IModel
{
  private IChangeListener multiplexer;
  private ChangeEvent changeEvent = new ChangeEvent(this);


  public final void addChangeListener(IChangeListener listener)
  {
    multiplexer = ChangeBroadcaster.add(multiplexer, listener);
  }


  public final void removeChangeListener(IChangeListener listener)
  {
    multiplexer = ChangeBroadcaster.remove(multiplexer, listener);
  }
  
  
  protected final void notifyListeners()
  {
    ChangeBroadcaster.dispatch(multiplexer, changeEvent);
  }
  
  
  protected final void notifyListeners(ChangeEvent event)
  {
    ChangeBroadcaster.dispatch(multiplexer, event);
  }
}
