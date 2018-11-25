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
import com.novocode.naf.resource.NGComponent;


/**
 * Default implementation of the IItemListModel interface.
 * This implementation is synchronized.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 19, 2003
 */

public class DefaultItemListModel extends DefaultModel implements IItemListModel
{
  private static final NGComponent[] EMPTY_COMPONENT_ARRAY = new NGComponent[0];


  private volatile NGComponent[] items = EMPTY_COMPONENT_ARRAY;


  public DefaultItemListModel() {}
  
  
  public DefaultItemListModel(NGComponent[] items)
  {
    if(items != null) this.items = items;
  }


  public synchronized void add(NGComponent item)
  {
    NGComponent[] n = new NGComponent[items.length+1];
    System.arraycopy(items, 0, n, 0, items.length);
    n[items.length] = item;
    items = n;
    notifyListeners(new ListChangeEvent(this, ListChangeEvent.ADD_ONE_END, -1, -1, item));
  }


  public synchronized void add(int idx, NGComponent item)
  {
    NGComponent[] n = new NGComponent[items.length+1];
    System.arraycopy(items, 0, n, 0, idx);
    System.arraycopy(items, idx, n, idx+1, items.length-idx);
    n[idx] = item;
    items = n;
    notifyListeners(new ListChangeEvent(this, ListChangeEvent.ADD_ONE_IDX, idx, -1, item));
  }


  public synchronized void add(NGComponent[] newItems)
  {
    NGComponent[] n = new NGComponent[items.length + newItems.length];
    System.arraycopy(items, 0, n, 0, items.length);
    System.arraycopy(newItems, 0, n, items.length, newItems.length);
    items = n;
    notifyListeners(new ListChangeEvent(this, ListChangeEvent.ADD_MULTI_END, -1, -1, newItems));
  }


  public synchronized void add(int idx, NGComponent[] newItems)
  {
    NGComponent[] n = new NGComponent[items.length + newItems.length];
    System.arraycopy(items, 0, n, 0, idx);
    System.arraycopy(items, idx, n, idx+newItems.length, items.length-idx);
    System.arraycopy(newItems, 0, n, idx, newItems.length);
    items = n;
    notifyListeners(new ListChangeEvent(this, ListChangeEvent.ADD_MULTI_IDX, idx, -1, newItems));
  }


  public void clear()
  {
    set(EMPTY_COMPONENT_ARRAY);
  }

  
  public int size()
  {
    return items.length;
  }


  public NGComponent[] getItems()
  {
    return items;
  }


  public synchronized void set(int idx, NGComponent item)
  {
    if(items[idx] != item)
    {
      items[idx] = item;
      notifyListeners(new ListChangeEvent(this, ListChangeEvent.SET_ONE_IDX, idx, -1, item));
    }
  }


  public synchronized void set(NGComponent[] items)
  {
    if(items == null) items = EMPTY_COMPONENT_ARRAY;
    if(!quickEqual(this.items, items))
    {
      this.items = items;
      notifyListeners();
    }
  }


  public synchronized void remove(int idx)
  {
    NGComponent[] n = new NGComponent[items.length-1];
    System.arraycopy(items, 0, n, 0, idx);
    System.arraycopy(items, idx+1, n, idx, items.length-idx-1);
    items = n;
    notifyListeners(new ListChangeEvent(this, ListChangeEvent.REMOVE_ONE_IDX, idx, -1, null));
  }


  public synchronized void remove(int idx, int count)
  {
    NGComponent[] n = new NGComponent[items.length-count];
    System.arraycopy(items, 0, n, 0, idx);
    System.arraycopy(items, idx+count, n, idx, items.length-idx-count);
    items = n;
    notifyListeners(new ListChangeEvent(this, ListChangeEvent.REMOVE_MULTI_IDX, idx, count, null));
  }


  private static boolean quickEqual(Object[] s1, Object[] s2)
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
