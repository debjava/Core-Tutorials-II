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

import org.eclipse.swt.graphics.FontData;



/**
 * Default implementation of the IFontModel interface.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 12, 2003
 */

public class DefaultFontModel extends DefaultObjectModel<FontData[]>
{
  public static final PType<IObjectModel<FontData[]>> PTYPE_FULL = new PType<IObjectModel<FontData[]>>(IObjectModel.class, DefaultFontModel.class, FontData[].class);
  public static final PType<IObjectReadModel<FontData[]>> PTYPE_READ = new PType<IObjectReadModel<FontData[]>>(IObjectReadModel.class, DefaultFontModel.class, FontData[].class);
  public static final PType<IObjectModifyModel<FontData[]>> PTYPE_MODIFY = new PType<IObjectModifyModel<FontData[]>>(IObjectModifyModel.class, DefaultFontModel.class, FontData[].class);


  public DefaultFontModel() {}
  
  
  public DefaultFontModel(FontData[] fd) { super(fd); }


  public DefaultFontModel(FontData fd) { super(new FontData[] { fd }); }


  public FontData getFirstFontData()
  {
    return (value == null || value.length == 0) ? null : value[0];
  }
  

  public synchronized void setValue(FontData[] fd)
  {
    if(!eq(fd, this.value))
    {
      this.value = fd;
      notifyListeners();
    }
  }


  private static boolean eq(FontData[] fd1, FontData[] fd2)
  {
    if(fd1 == null && fd2 == null) return true;
    if(fd1 == null || fd2 == null) return false;
    if(fd1.length != fd2.length) return false;
    for(int i=0; i<fd1.length; i++)
    {
      if(!fd1[i].equals(fd2[i])) return false;
    }
    return true;
  }
}
