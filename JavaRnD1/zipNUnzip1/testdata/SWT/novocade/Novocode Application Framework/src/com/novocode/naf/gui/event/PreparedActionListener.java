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



/**
 * An IActionListener implementation which delegates ActionEvents
 * to a parent IActionListener and can perform a prepared IActionEVent
 * specified at creation time.
 *
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 8, 2004
 * @version $Id: PreparedActionListener.java 257 2004-12-08 17:47:46 +0000 (Wed, 08 Dec 2004) szeiger $
 */

public final class PreparedActionListener implements IActionListener
{
  private final IActionListener parent;
  private final ActionEvent preparedEvent;


  public PreparedActionListener(IActionListener parent, ActionEvent preparedEvent)
  {
    this.parent = parent;
    this.preparedEvent = preparedEvent;
  }


  public void performPreparedAction()
  {
    parent.performAction(preparedEvent);
  }


  public void performAction(ActionEvent event)
  {
    parent.performAction(event);
  }
}