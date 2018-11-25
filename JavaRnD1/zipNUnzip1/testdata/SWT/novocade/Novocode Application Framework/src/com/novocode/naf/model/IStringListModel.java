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


/**
 * A model that contains a list of strings of which one or more
 * can be selected.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 19, 2003
 */

public interface IStringListModel extends IModel
{
  // [TODO] Add the accessor methods which are currently commented out

  public void add(String string);
  //public void add(int idx, String string);

  //public void remove(int idx);
  //public void remove(int startInclusive, int endInclusive);
  //public void remove(int[] indices);
  public void clear();

  //public void set(int idx, String string);
  public void set(String[] strings);
  
  public int size();
  public String[] getStringArray();
}
