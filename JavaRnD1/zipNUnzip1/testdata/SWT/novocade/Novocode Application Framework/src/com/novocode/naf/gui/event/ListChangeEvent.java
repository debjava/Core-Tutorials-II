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
 * ChangeEvent for list-based models (e.g. StringListModel, MetaModel).
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Jan 4, 2004
 */

public class ListChangeEvent extends ChangeEvent
{
  private static final long serialVersionUID = -5805008911894686080L;

  public static final int ADD_ONE_END      = 0;
  public static final int ADD_ONE_IDX      = 1;
  public static final int ADD_MULTI_END    = 2;
  public static final int ADD_MULTI_IDX    = 3;
  public static final int SET_ONE_IDX      = 4;
  public static final int REMOVE_ONE_IDX   = 5;
  public static final int REMOVE_MULTI_IDX = 6;

  public final int action, idx, count;
  public final Object data;

  public ListChangeEvent(Object source, int action, int idx, int count, Object data)
  {
    super(source);
    this.action = action;
    this.idx = idx;
    this.count = count;
    this.data = data;
  }
}
