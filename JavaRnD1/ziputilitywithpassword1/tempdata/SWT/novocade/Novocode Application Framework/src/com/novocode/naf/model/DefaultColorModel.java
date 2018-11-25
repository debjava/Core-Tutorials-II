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

import org.eclipse.swt.graphics.RGB;


/**
 * Default implementation of the IColorModel interface.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 9, 2003
 */

public class DefaultColorModel extends DefaultModel implements IObjectModel<RGB>
{
  public static final PType<IObjectModel<RGB>> PTYPE_FULL = new PType<IObjectModel<RGB>>(IObjectModel.class, DefaultColorModel.class, RGB.class);
  public static final PType<IObjectReadModel<RGB>> PTYPE_READ = new PType<IObjectReadModel<RGB>>(IObjectReadModel.class, DefaultColorModel.class, RGB.class);
  public static final PType<IObjectModifyModel<RGB>> PTYPE_MODIFY = new PType<IObjectModifyModel<RGB>>(IObjectModifyModel.class, DefaultColorModel.class, RGB.class);


  private volatile int r, g, b;


  public DefaultColorModel() {}
  
  
  public DefaultColorModel(RGB rgb)
  {
    r = rgb.red;
    g = rgb.green;
    b = rgb.blue;
  }


  public RGB getValue()
  {
    return new RGB(r, g, b);
  }
  

  public synchronized void setValue(RGB rgb)
  {
    if(rgb.red != r || rgb.green != g || rgb.blue != b)
    {
      r = rgb.red;
      g = rgb.green;
      b = rgb.blue;
      notifyListeners();
    }
  }
}
