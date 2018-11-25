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

import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;

import com.novocode.naf.app.*;
import com.novocode.naf.gui.event.*;
import com.novocode.naf.jface.viewers.BackgroundTreeContentProvider;
import com.novocode.naf.model.*;
import com.novocode.naf.resource.*;


/**
 * A JFace TreeViewer.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since May 30, 2004
 */

public final class NGTree extends NGWidget
{
  private static final String TREEVIEWER_DATA = NGTree.class.getName()+".treeViewer";

  private boolean multi = true, hscroll = true, vscroll = true, hashLookup = true;
  private boolean check, border;


  @ConfProperty
  public void setCheck(boolean b) { check = b; }
  public boolean isCheck() { return check; }

  
  @ConfProperty
  public void setMulti(boolean b) { multi = b; }
  public boolean isMulti() { return multi; }


  @ConfProperty("hscroll")
  public void setHScroll(boolean b) { this.hscroll = b; }
  public boolean getHScroll() { return hscroll; }
  
  
  @ConfProperty("vscroll")
  public void setVScroll(boolean b) { this.vscroll = b; }
  public boolean getVScroll() { return vscroll; }

  
  @ConfProperty
  public void setBorder(boolean b) { border = b; }
  public boolean getBorder() { return border; }
  
  
  @ConfProperty
  public void setHashLookup(boolean b) { hashLookup = b; }
  public boolean getHashLookup() { return hashLookup; }


  public Control createControl(Composite parent, NGComponent parentComp, ShellWindowInstance wi, WidgetData pwd) throws NAFException
  {
    int style = multi ? SWT.MULTI : SWT.SINGLE;
    if(hscroll) style |= SWT.H_SCROLL;
    if(vscroll) style |= SWT.V_SCROLL;
    if(border) style |= SWT.BORDER;
    if(check) style |= SWT.CHECK;
    final TreeViewer tv = new TreeViewer(parent, style);
    tv.setUseHashlookup(hashLookup);
    final Tree tree = tv.getTree();
    tree.setData(TREEVIEWER_DATA, tv);

    final ITreeContentProvider contentProvider = getModel("content-provider", wi.models, ITreeContentProvider.class);
    if(contentProvider != null)
    {
      tv.setContentProvider(contentProvider);
      if(contentProvider instanceof BackgroundTreeContentProvider)
        ((BackgroundTreeContentProvider)contentProvider).setTreeViewer(tv);
    }

    final IBaseLabelProvider labelProvider = getModel("label-provider", wi.models, IBaseLabelProvider.class);
    if(labelProvider != null) tv.setLabelProvider(labelProvider);

    final ViewerFilter filter = getModel("filter", wi.models, ViewerFilter.class);
    if(filter != null) tv.addFilter(filter);

    final IObjectReadModel<?> contentModel = getModel("content", wi.models, IObjectReadModel.class);
    if(contentModel != null)
    {
      final IChangeListener cl = new IChangeListener()
      {
        public void stateChanged(ChangeEvent e)
        {
          tv.setInput(contentModel.getValue());
        }
      };
      SWTUtil.registerModel(tree, contentModel, cl);
      cl.stateChanged(null);
    }
    
    final IActionSource expandAllSource = getModel("expand-all", wi.models, IActionSource.class);
    if(expandAllSource != null)
    {
      SWTUtil.registerModel(tree, expandAllSource, new IActionListener()
      {
        public void performAction(ActionEvent e)
        {
          tv.expandAll();
        }
      });
    }

    final IActionSource collapseAllSource = getModel("collapse-all", wi.models, IActionSource.class);
    if(collapseAllSource != null)
    {
      SWTUtil.registerModel(tree, collapseAllSource, new IActionListener()
      {
        public void performAction(ActionEvent e)
        {
          tv.collapseAll();
        }
      });
    }

    /*final IActionListener daModel = (IActionListener)getModel("default-action", wi.models);

    if(daModel != null)
    {
      table.addSelectionListener(new SelectionAdapter()
      {
        public void widgetDefaultSelected(SelectionEvent e)
        {
          TableItem ti = table.getItem(table.getSelectionIndex());
          daModel.performAction(new ActionEvent(NGTree.this, ti.getText(), wi));
        }
      });
    }*/
    
    return tree;
  }
}
