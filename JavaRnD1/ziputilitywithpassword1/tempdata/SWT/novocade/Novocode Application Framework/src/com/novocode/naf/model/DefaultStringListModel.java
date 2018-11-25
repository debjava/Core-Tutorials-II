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

import com.novocode.naf.gui.event.ListChangeEvent;


/**
 * Default implementation of the IStringListModel interface.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 19, 2003
 */

public class DefaultStringListModel extends DefaultModel implements IStringListModel
{
  private static final String[] EMPTY_STRING_ARRAY = new String[0];


  private volatile String[] strings = EMPTY_STRING_ARRAY;


  public DefaultStringListModel() {}
  
  
  public DefaultStringListModel(String[] strings)
  {
    if(strings != null) this.strings = strings;
  }


  public synchronized void add(String s)
  {
    String[] n = new String[strings.length+1];
    System.arraycopy(strings, 0, n, 0, strings.length);
    n[strings.length] = s;
    strings = n;
    notifyListeners(new ListChangeEvent(this, ListChangeEvent.ADD_ONE_END, -1, -1, s));
  }


  public void clear()
  {
    set(EMPTY_STRING_ARRAY);
  }

  
  public int size()
  {
    return strings.length;
  }


  public String[] getStringArray()
  {
    return strings;
  }


  public synchronized void set(String[] s)
  {
    if(!quickEqual(this.strings, s))
    {
      this.strings = s;
      notifyListeners();
    }
  }


  //public void add(int idx, String string)
  //public void remove(int idx);
  //public void remove(int startInclusive, int endInclusive);
  //public void remove(int[] indices);
  //public void set(int idx, String string);


  private static boolean quickEqual(String[] s1, String[] s2)
  {
    if(s1 == null && s2 == null) return true;
    if(s1 == null || s2 == null) return false;
    if(s1.length != s2.length) return false;
    for(int i=0; i<s1.length; i++)
    {
      if(s1[i] != s2[i]) return false;
    }
    return true;
  }
}
