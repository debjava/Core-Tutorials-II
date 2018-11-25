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
 * Default implementation of the ITableModel interface.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Jan 12, 2004
 */

public class DefaultTableModel extends DefaultModel implements ITableModel
{
  public static final PType<ITableModel> PTYPE = new PType<ITableModel>(ITableModel.class, DefaultTableModel.class);


  public class DefaultTableModelColumn implements ITableModel.TableModelColumn
  {
    private volatile String text, imageID;


    public DefaultTableModelColumn() {}
    
    
    public DefaultTableModelColumn(String text)
    {
      this.text = text;
    }
  
  
    public DefaultTableModelColumn(String text, String imageID)
    {
      this.text = text;
      this.imageID = imageID;
    }
  
  
    public String getText() { return text; }
  
  
    public String getImageID() { return imageID; }


    public synchronized void setText(String text)
    {
      if(this.text != text)
      {
        this.text = text;
        notifyListeners();
      }
    }


    public synchronized void setImageID(String imageID)
    {
      if(this.imageID != imageID)
      {
        this.imageID = imageID;
        notifyListeners();
      }
    }
  }


  private volatile TableModelColumn[] columns = null;


  public DefaultTableModel() {}
  
  
  public synchronized void setColumns(TableModelColumn[] columns)
  {
    if(columns != this.columns)
    {
      this.columns = columns;
      notifyListeners();
    }
  }


  public TableModelColumn[] getColumns()
  {
    return columns;
  }
}
