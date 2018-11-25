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
 * A model that contains print data from a PrintDialog.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 12, 2003
 */

public interface IPrintDataModel extends IModel
{
  public int getStartPage();
  public int getEndPage();
  public int getScope();
  public boolean getPrintToFile();
  public PrinterData getPrinterData();
  
  public void setStartPage(int i);
  public void setEndPage(int i);
  public void setScope(int i);
  public void setPrintToFile(boolean b);
  public void setPrinterData(PrinterData p);
  public void setAll(int start, int end, int scope, boolean printToFile, PrinterData data);
}
