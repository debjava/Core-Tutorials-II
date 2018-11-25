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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TableTreeViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;

import com.novocode.naf.app.*;
import com.novocode.naf.gui.event.*;
import com.novocode.naf.model.*;
import com.novocode.naf.resource.*;


/**
 * A JFace TableTreeViewer.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 04, 2004
 * @deprecated Use NGTree instead
 */

public final class NGTableTree extends NGWidget
{
  private static final String TABLETREEVIEWER_DATA = NGTableTree.class.getName()+".tableTreeViewer";

  private boolean multi = true, hscroll = true, vscroll = true, hashLookup = true;
  private boolean check, border, headerVisible, linesVisible;
  private List<TableColumnData> columns;


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


  @ConfProperty
  public void setLinesVisible(boolean b) { linesVisible = b; }
  public boolean getLinesVisible() { return linesVisible; }


  @ConfProperty
  public void setHeaderVisible(boolean b) { headerVisible = b; }
  public boolean getHeaderVisible() { return headerVisible; }


  @ConfProperty("column")
  public void setColumns(TableColumnData[] cds)
  {
    if(columns != null) columns.clear();
    for(TableColumnData cd : cds) addColumn(cd);
  }

  public void addColumn(TableColumnData cd)
  {
    if(columns == null) columns = new ArrayList<TableColumnData>();
    columns.add(cd);
  }


  public Control createControl(Composite parent, NGComponent parentComp, ShellWindowInstance wi, WidgetData pwd) throws NAFException
  {
    int style = multi ? SWT.MULTI : SWT.SINGLE;
    if(hscroll) style |= SWT.H_SCROLL;
    if(vscroll) style |= SWT.V_SCROLL;
    if(border) style |= SWT.BORDER;
    if(check) style |= SWT.CHECK;
    final TableTreeViewer tv = new TableTreeViewer(parent, style);
    final org.eclipse.swt.custom.TableTree tableTree = tv.getTableTree();
    final Table table = tableTree.getTable();
    table.setHeaderVisible(headerVisible);
    table.setLinesVisible(linesVisible);
    tv.setUseHashlookup(hashLookup);
    tableTree.setData(TABLETREEVIEWER_DATA, tv);
    return tableTree;
  }

  protected void configureControl(final Control control, final ShellWindowInstance wi, WidgetData wd, WidgetData pwd)
  {
    super.configureControl(control, wi, wd, pwd);
    
    final org.eclipse.swt.custom.TableTree tableTree = (org.eclipse.swt.custom.TableTree)control;
    final TableTreeViewer tv = (TableTreeViewer)tableTree.getData(TABLETREEVIEWER_DATA);
    final Table table = tableTree.getTable();

    if(columns != null)
    {
      int embase = SWTUtil.computeEMBase(table);
      for(TableColumnData cd : columns)
      {
        TableColumn tc = new TableColumn(table, 0);
        tc.setText(cd.text);
        if(cd.imgres != null)
          tc.setImage(SWTUtil.getRegisteredImage(cd.imgres, getResource().getURL(), table, wi.imageManager, false));
        tc.setResizable(cd.resizable);
        tc.setAlignment(cd.align.style);
        if(cd.width != null) tc.setWidth(cd.width.compute(embase));
        else tc.pack();
      }
    }

    final ITreeContentProvider contentProvider = getModel("content-provider", wi.models, ITreeContentProvider.class);
    if(contentProvider != null) tv.setContentProvider(contentProvider);

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
      SWTUtil.registerModel(tableTree, contentModel, cl);
      cl.stateChanged(null);
    }
    
    final IActionSource expandAllSource = getModel("expand-all", wi.models, IActionSource.class);
    if(expandAllSource != null)
    {
      SWTUtil.registerModel(tableTree, expandAllSource, new IActionListener()
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
      SWTUtil.registerModel(tableTree, collapseAllSource, new IActionListener()
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
  }
}
