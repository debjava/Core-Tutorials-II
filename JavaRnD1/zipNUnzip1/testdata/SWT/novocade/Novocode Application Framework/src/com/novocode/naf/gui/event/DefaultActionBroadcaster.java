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

package com.novocode.naf.gui.event;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.LoggerFactory;

import org.slf4j.Logger;


/**
 * Default implementation of the IActionBroadcaster interface.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 5, 2003
 */

public class DefaultActionBroadcaster implements IActionBroadcaster
{
  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultActionBroadcaster.class);

  private List<IActionListener> listeners;
  private String name;


  public DefaultActionBroadcaster() {}


  public DefaultActionBroadcaster(String name) { this.name = name; }


  public void performAction(ActionEvent e)
  {
    if(LOGGER.isDebugEnabled())
      LOGGER.debug("[DefaultActionBroadcaster invoked: name="+name+", id="+e.source.getID()+", command="+e.command+"]");
    if(listeners == null) return;
    for(IActionListener l : listeners) l.performAction(e);
  }


  public final synchronized void addActionListener(IActionListener listener)
  {
    if(listeners == null) listeners = new LinkedList<IActionListener>();
    listeners.add(listener);
  }


  public final synchronized void removeActionListener(IActionListener listener)
  {
    if(listeners == null) return;
    listeners.remove(listener);
  }
}
