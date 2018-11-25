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
 * ChangeEvent for StringModels.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Jan 4, 2004
 */

public class StringChangeEvent extends ChangeEvent
{
  private static final long serialVersionUID = -1370175968841464740L;

  public static final int ADD = 0;

  public final int action;
  public final String data;

  public StringChangeEvent(Object source, int action, String data)
  {
    super(source);
    this.action = action;
    this.data = data;
  }
}
