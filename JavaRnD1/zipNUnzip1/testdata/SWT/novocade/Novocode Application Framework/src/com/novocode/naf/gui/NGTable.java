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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.*;

import com.novocode.naf.app.*;
import com.novocode.naf.gui.event.*;
import com.novocode.naf.gui.image.ImageDescriptor;
import com.novocode.naf.model.*;
import com.novocode.naf.resource.*;


/**
 * A JFace TableViewer.
 *
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Nov 28, 2004
 * @version $Id: NGTable.java 416 2008-05-05 17:51:40Z szeiger $
 */

public final class NGTable extends NGWidget
{
  private static final String TABLEVIEWER_DATA = NGTable.class.getName()+".tableViewer";

  private boolean multi = true, hscroll = true, vscroll = true, border, check = false;
  private boolean fullSelection = false, hideSelection = false, virtual = false;
  private boolean hashLookup = true, linesVisible, headerVisible;
  private List<TableColumnData> columns;
  private Map<String, String> images;


  @ConfProperty
  public void setMulti(boolean b) { multi = b; }
  public boolean isMulti() { return multi; }


  @ConfProperty
  public void setCheck(boolean b) { check = b; }
  public boolean isCheck() { return check; }


  @ConfProperty
  public void setVirtual(boolean b) { virtual = b; }
  public boolean isVirtual() { return virtual; }


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
  public void setFullSelection(boolean b) { fullSelection = b; }
  public boolean getFullSelection() { return fullSelection; }


  @ConfProperty
  public void setHideSelection(boolean b) { hideSelection = b; }
  public boolean getHideSelection() { return hideSelection; }


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


  @ConfProperty("image")
  public void setImages(ImageDescriptor[] imgds)
  {
    if(images != null) images.clear();
    for(ImageDescriptor imgd : imgds) addImage(imgd);
  }

  public void addImage(ImageDescriptor imgd)
  {
    String iid = imgd.getID(), ires = imgd.getImageResource();
    if(images == null) images = new HashMap<String, String>();
    images.put(iid, ires);
  }


  public Control createControl(Composite parent, NGComponent parentComp, final ShellWindowInstance wi, WidgetData pwd) throws NAFException
  {
    int style = multi ? SWT.MULTI : SWT.SINGLE;
    if(hscroll) style |= SWT.H_SCROLL;
    if(vscroll) style |= SWT.V_SCROLL;
    if(check) style |= SWT.CHECK;
    if(border) style |= SWT.BORDER;
    if(fullSelection) style |= SWT.FULL_SELECTION;
    if(hideSelection) style |= SWT.HIDE_SELECTION;
    final TableViewer tv = new TableViewer(parent, style);
    tv.setUseHashlookup(hashLookup);
    final Table table = tv.getTable();
    table.setData(TABLEVIEWER_DATA, tv);
    table.setHeaderVisible(headerVisible);
    table.setLinesVisible(linesVisible);
    return table;
  }

  protected void configureControl(final Control control, final ShellWindowInstance wi, WidgetData wd, WidgetData pwd)
  {
    super.configureControl(control, wi, wd, pwd);

    final Table table = (Table)control;
    final TableViewer tv = (TableViewer)table.getData(TABLEVIEWER_DATA);

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
      if(columns.size() > 0)
      {
        TableColumnData lastCol = columns.get(columns.size()-1);
        if(!lastCol.resizable && lastCol.width.compute(embase) == 0)
        {
          table.addControlListener(new ControlAdapter()
          {
            public void controlResized(ControlEvent e)
            {
              int w = table.getClientArea().width;
              TableColumn[] tcs = table.getColumns();
              tcs[tcs.length-1].setWidth(w);
            }
          });
        }
      }
    }
    else
    {
      Map<String, Image> imageInstances;
      if(images != null)
      {
        imageInstances = new HashMap<String, Image>();
        for(Map.Entry<String, String> entry : images.entrySet())
        {
          Image img = SWTUtil.getRegisteredImage(entry.getValue(), getResource().getURL(), table, wi.imageManager, false);
          imageInstances.put(entry.getKey(), img);
        }
      }
      else imageInstances = null;

      final ITableModel tableModel = getModel("table", wi.models, DefaultTableModel.PTYPE);
      if(tableModel != null)
      {
        final Map<String, Image> finalImageInstances = imageInstances;
        final IChangeListener cl = new IChangeListener()
        {
          public void stateChanged(ChangeEvent e)
          {
            try
            {
              table.setRedraw(false);

              TableColumn[] tcs = table.getColumns();
              for(int i=0; i<tcs.length; i++) tcs[i].dispose();
              ITableModel.TableModelColumn[] tmcs = tableModel.getColumns();
              for(int i=0; i<tmcs.length; i++)
              {
                ITableModel.TableModelColumn tmc = tmcs[i];
                TableColumn tc = new TableColumn(table, 0);
                tc.setText(tmc.getText());
                Image img = null;
                if(finalImageInstances != null)
                  img = finalImageInstances.get(tmc.getImageID());
                if(img != tc.getImage()) tc.setImage(img);
                tc.pack();
              }
            }
            finally { table.setRedraw(true); }
          }
        };
        SWTUtil.registerModel(table, tableModel, cl);
        cl.stateChanged(null);
      }
    }

    final IContentProvider contentProvider = getModel("content-provider", wi.models, IContentProvider.class);
    if(contentProvider != null) tv.setContentProvider(contentProvider);

    final IBaseLabelProvider labelProvider = getModel("label-provider", wi.models, IBaseLabelProvider.class);
    if(labelProvider != null) tv.setLabelProvider(labelProvider);

    final ViewerFilter filter = getModel("filter", wi.models, ViewerFilter.class);
    if(filter != null) tv.addFilter(filter);

    final IIntModel selectedModel = getModel("selected", wi.models, IIntModel.class);
    hookUpIIntModel(selectedModel, table, "getSelectionIndex", "setSelection");

    final IObjectReadModel<?> contentModel = getModel("content", wi.models, IObjectReadModel.class);
    if(contentModel != null)
    {
      final IChangeListener cl = new IChangeListener()
      {
        public void stateChanged(ChangeEvent e)
        {
          tv.setInput(contentModel.getValue());
          if(selectedModel != null) selectedModel.setInt(table.getSelectionIndex());
        }
      };
      SWTUtil.registerModel(table, contentModel, cl);
      cl.stateChanged(null);
    }

    final IActionListener daModel = getModel("default-action", wi.models, IActionListener.class);
    if(daModel != null)
    {
      table.addSelectionListener(new SelectionAdapter()
      {
        public void widgetDefaultSelected(SelectionEvent e)
        {
          TableItem ti = table.getItem(table.getSelectionIndex());
          daModel.performAction(new ActionEvent(NGTable.this, ti.getText(), wi));
        }
      });
    }
  }
}
