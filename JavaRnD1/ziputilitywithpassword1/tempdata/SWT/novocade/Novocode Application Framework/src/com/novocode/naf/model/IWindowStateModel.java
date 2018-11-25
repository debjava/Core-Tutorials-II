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

import com.novocode.naf.persist.PersisterClass;
import com.novocode.naf.persist.WindowStateModelPersister;

import org.eclipse.swt.graphics.Rectangle;


/**
 * A model that contains a window state that can be read and modified.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Oct 15, 2004
 */

@PersisterClass(WindowStateModelPersister.class)
public interface IWindowStateModel extends IModel
{
  public static final int STATE_DEFAULTBOUNDS              = 0;
  public static final int STATE_PLURALIZED                 = 1;
  public static final int STATE_MAXIMIZED                  = 2;
  public static final int STATE_MINIMIZED_AFTER_PLURALIZED = 3;
  public static final int STATE_MINIMIZED_AFTER_MAXIMIZED  = 4;

  public Rectangle getPluralizedBounds();
  public int getState();

  public void setPluralizedBounds(Rectangle r);
  public void setState(int i);
}
