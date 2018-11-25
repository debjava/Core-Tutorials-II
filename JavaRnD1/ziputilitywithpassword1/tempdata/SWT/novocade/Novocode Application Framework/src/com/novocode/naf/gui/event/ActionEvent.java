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

import java.util.EventObject;

import com.novocode.naf.gui.*;
import com.novocode.naf.resource.*;


/**
 * Tells a listener that an action has been triggered.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 2, 2003
 */

public class ActionEvent extends EventObject
{
  private static final long serialVersionUID = -7834066518715369104L;

  public final NGComponent source;
  public final String command;
  public final WindowInstance windowInstance;


  public ActionEvent(NGComponent source, String command, WindowInstance windowInstance)
  {
    super(source);
    this.source = source;
    this.command = command;
    this.windowInstance = windowInstance;
  }
}
