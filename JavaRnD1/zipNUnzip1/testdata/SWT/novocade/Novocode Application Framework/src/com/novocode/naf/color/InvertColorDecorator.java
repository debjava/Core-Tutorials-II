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

package com.novocode.naf.color;


/**
 * A color decorator which inverts a color.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Apr 26, 2004
 */

public class InvertColorDecorator implements IColorDecorator
{
  public static final IColorDecorator DECO_INVERT = new InvertColorDecorator();


  private InvertColorDecorator()
  {
  }


  public void applyTo(PreciseColor c)
  {
    c.convertToModel(PreciseColor.MODEL_RGB);
    c.rgb[0] = 1.0 - c.rgb[0];
    c.rgb[1] = 1.0 - c.rgb[1];
    c.rgb[2] = 1.0 - c.rgb[2];
  }
}
