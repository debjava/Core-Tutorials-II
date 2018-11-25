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
 * A color decorator which darkens a color.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Apr 26, 2004
 */

public class DarkenColorDecorator implements IColorDecorator
{
  public static final IColorDecorator DECO_DARKEN_10_PERCENT = new DarkenColorDecorator(0.025);


  private double shift;


  public DarkenColorDecorator(double shift)
  {
    this.shift = shift;
  }


  public void applyTo(PreciseColor c)
  {
    c.convertToModel(PreciseColor.MODEL_HSV);
    c.hsv[2] = Math.max(0.0, c.hsv[2] - shift);
  }
}
