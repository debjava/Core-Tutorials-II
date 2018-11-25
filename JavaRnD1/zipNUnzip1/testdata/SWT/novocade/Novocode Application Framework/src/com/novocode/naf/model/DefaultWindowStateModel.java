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

import org.eclipse.swt.graphics.Rectangle;


/**
 * Default implementation of the IWindowStateModel interface.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Oct 15, 2004
 */

public class DefaultWindowStateModel extends DefaultModel implements IWindowStateModel
{
  public static final PType<IWindowStateModel> PTYPE = new PType<IWindowStateModel>(IWindowStateModel.class, DefaultWindowStateModel.class);

  private int pluralizedX, pluralizedY, pluralizedWidth, pluralizedHeight, state;


  public DefaultWindowStateModel() {}


  public Rectangle getPluralizedBounds()
  {
    return new Rectangle(pluralizedX, pluralizedY, pluralizedWidth, pluralizedHeight);
  }


  public int getState()
  {
    return state;
  }


  public synchronized void setPluralizedBounds(Rectangle r)
  {
    if(r.x != pluralizedX || r.y != pluralizedY || r.width != pluralizedWidth || r.height != pluralizedHeight)
    {
      pluralizedX = r.x;
      pluralizedY = r.y;
      pluralizedWidth = r.width;
      pluralizedHeight = r.height;
      notifyListeners();
    }
  }


  public synchronized void setState(int i)
  {
    if(i != state)
    {
      state = i;
      notifyListeners();
    }
  }
}
