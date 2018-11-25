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

package com.novocode.naf.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;

import com.novocode.naf.app.*;
import com.novocode.naf.gui.event.*;
import com.novocode.naf.model.*;
import com.novocode.naf.resource.*;


/**
 * An SWT List control (text only).
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 19, 2003
 */

public final class NGStringList extends NGWidget
{
  private static final String[] NO_STRINGS = new String[0];
  
  
  private boolean multi, keepBottomVisible;


  @ConfProperty
  public void setMulti(boolean b) { this.multi = b; }
  public boolean getMulti() { return multi; }
  
  
  @ConfProperty
  public void setKeepBottomVisible(boolean b) { this.keepBottomVisible = b; }
  public boolean getKeepBottomVisible() { return keepBottomVisible; }


  public Control createControl(Composite parent, NGComponent parentComp, final ShellWindowInstance wi, WidgetData pwd) throws NAFException
  {
    int style = multi ? SWT.MULTI : SWT.SINGLE;
    final List list = new List(parent, style | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
    //list.setItems(new String[] { "" });
    //int itemHeight = list.getItemHeight();
    //int trimHeight = list.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
    //list.setSize(SWT.DEFAULT, 5 * itemHeight + trimHeight);

    // [TODO] Add widget-backed model as default "items" model
    final IStringListModel slModel = getModel("items", wi.models, IStringListModel.class);
    if(slModel != null)
    {
      final IChangeListener cl = new IChangeListener()
      {
        public void stateChanged(ChangeEvent e)
        {
          int newTop, itemCount;
          boolean isBottom;
          if(keepBottomVisible) // [TODO] Handle keepBottomVisible correctly when the widget is hidden
          {
            int visItemCount = list.getSize().y/list.getItemHeight();
            itemCount = list.getItemCount();
            newTop = itemCount - visItemCount + 1;
            isBottom = newTop - list.getTopIndex() <= visItemCount/5 + 1;
            // visItemCount/5 allows for some tolerance when a List widget is made smaller
          }
          else
          {
            newTop = 0;
            itemCount = 0;
            isBottom = false;
          }

          if(e instanceof ListChangeEvent) // partial refresh if action is known
          {
            ListChangeEvent ee = (ListChangeEvent)e;
            switch(ee.action)
            {
              case ListChangeEvent.ADD_ONE_END:
              {
                list.add((String)ee.data);
                if(isBottom) list.setTopIndex(newTop + 1);
                break;
              }
              case ListChangeEvent.ADD_ONE_IDX:
              {
                list.add((String)ee.data, ee.idx);
                if(isBottom) list.setTopIndex(newTop + 1);
                break;
              }
              case ListChangeEvent.ADD_MULTI_END:
              {
                String[] sa = (String[])ee.data;
                int sal = sa.length;
                try
                {
                  list.setRedraw(false);
                  for(int i=0; i<sal; i++) list.add(sa[i]);
                  if(isBottom) list.setTopIndex(newTop + sal);
                }
                finally { list.setRedraw(true); }
                break;
              }
              case ListChangeEvent.ADD_MULTI_IDX:
              {
                String[] sa = (String[])ee.data;
                int sal = sa.length;
                try
                {
                  list.setRedraw(false);
                  for(int i=0; i<sal; i++) list.add(sa[i], ee.idx + i);
                  if(isBottom) list.setTopIndex(newTop + sal);
                }
                finally { list.setRedraw(true); }
                break;
              }
              default: // unknown action -> full refresh
              {
                String[] s = slModel.getStringArray();
                if(s == null) s = NO_STRINGS;
                list.setItems(s);
                if(isBottom) list.setTopIndex(newTop + s.length - itemCount);
                break;
              }
            }
          }
          else // unknown event -> full refresh
          {
            String[] s = slModel.getStringArray();
            if(s == null) s = NO_STRINGS;
            list.setItems(s);
            if(isBottom) list.setTopIndex(newTop + s.length - itemCount);
          }
        }
      };
      SWTUtil.registerModel(list, slModel, cl);
      cl.stateChanged(null);
    }

    final IObjectModifyModel<String> sdaModel = getModel("string-default-action", wi.models, DefaultStringModel.PTYPE_MODIFY);
    final IActionListener daModel = getModel("default-action", wi.models, IActionListener.class);

    if(sdaModel != null || daModel != null)
    {
      list.addSelectionListener(new SelectionAdapter()
      {
        public void widgetDefaultSelected(SelectionEvent e)
        {
          String s = list.getItem(list.getSelectionIndex());
          if(sdaModel != null) sdaModel.setValue(s);
          if(daModel != null) daModel.performAction(new ActionEvent(NGStringList.this, s, wi));
        }
      });
    }
    
    return list;
  }
}
