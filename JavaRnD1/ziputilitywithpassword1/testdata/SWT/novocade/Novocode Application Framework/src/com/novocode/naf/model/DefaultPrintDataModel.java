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

import org.eclipse.swt.printing.PrinterData;


/**
 * Default implementation of the IPrintDataModel interface.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 12, 2003
 */

public class DefaultPrintDataModel extends DefaultModel implements IPrintDataModel
{
  public static final PType<IPrintDataModel> PTYPE = new PType<IPrintDataModel>(IPrintDataModel.class, DefaultPrintDataModel.class);

  private volatile int start, end, scope;
  private boolean printToFile;
  private PrinterData data;


  public DefaultPrintDataModel() {}
  
  
  public DefaultPrintDataModel(int start, int end, int scope, boolean printToFile, PrinterData data)
  {
    this.start = start;
    this.end = end;
    this.scope = scope;
    this.printToFile = printToFile;
    this.data = data;
  }


  public int getStartPage() { return start; }
  
  
  public int getEndPage() { return end; }
    
    
  public int getScope() { return scope; }
  
  
  public boolean getPrintToFile() { return printToFile; }
  
  
  public PrinterData getPrinterData() { return data; }
  

  public void setStartPage(int i)
  {
    if(start != i)
    {
      start = i;
      notifyListeners();
    }
  }


  public void setEndPage(int i)
  {
    if(end != i)
    {
      end = i;
      notifyListeners();
    }
  }


  public void setScope(int i)
  {
    if(scope != i)
    {
      scope = i;
      notifyListeners();
    }
  }


  public void setPrintToFile(boolean b)
  {
    if(printToFile != b)
    {
      printToFile = b;
      notifyListeners();
    }
  }


  public void setPrinterData(PrinterData p)
  {
    if(!eq(data, p))
    {
      data = p;
      notifyListeners();
    }
  }
  
  
  public void setAll(int start, int end, int scope, boolean printToFile, PrinterData data)
  {
    if(this.start != start || this.end != end || this.scope != scope || this.printToFile != printToFile || !eq(this.data, data))
    {
      this.start = start;
      this.end = end;
      this.scope = scope;
      this.printToFile = printToFile;
      this.data = data;
      notifyListeners();
    }
  }


  private static boolean eq(PrinterData p1, PrinterData p2)
  {
    if(p1 == null && p2 == null) return true;
    if(p1 == null || p2 == null) return false;
    return p1.equals(p2);
  }
}
