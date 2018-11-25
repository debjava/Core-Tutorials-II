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

package com.novocode.naf.gui.event;

import java.util.concurrent.CopyOnWriteArrayList;


/**
 * IChangeListener implementation that simplifies listener handling
 * in custom model implementations.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Mar 27, 2008
 * @version $Id$
 */

public final class ChangeBroadcaster implements IChangeListener
{
  // Listeners list may be changed in a stateChanged() call, so we need a snapshot iterator
  private CopyOnWriteArrayList<IChangeListener> listeners = new CopyOnWriteArrayList<IChangeListener>();


  private ChangeBroadcaster(IChangeListener l) { listeners.add(l); }


  public void stateChanged(ChangeEvent event)
  {
    for(IChangeListener l : listeners) l.stateChanged(event);
  }


  public static IChangeListener add(IChangeListener multiplexer, IChangeListener clientListener)
  {
    if(multiplexer == null) return clientListener;
    ChangeBroadcaster b =
      multiplexer instanceof ChangeBroadcaster ? ((ChangeBroadcaster)multiplexer) : new ChangeBroadcaster(multiplexer);
    b.listeners.add(clientListener);
    return b;
  }


  public static IChangeListener remove(IChangeListener multiplexer, IChangeListener clientListener)
  {
    if(multiplexer == null) return null;
    else if(multiplexer instanceof ChangeBroadcaster)
    {
      ChangeBroadcaster b = ((ChangeBroadcaster)multiplexer);
      b.listeners.remove(clientListener);
      if(b.listeners.size() == 1) return b.listeners.get(0);
      else return b;
    }
    else if(multiplexer == clientListener) return null;
    else return multiplexer;
  }


  public static void dispatch(IChangeListener multiplexer, ChangeEvent event)
  {
    if(multiplexer != null) multiplexer.stateChanged(event);
  }
}
