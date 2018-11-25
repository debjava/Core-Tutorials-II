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
 * A color decorator which lightens a color.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Apr 26, 2004
 */

public class LightenColorDecorator extends DarkenColorDecorator
{
  public static final IColorDecorator DECO_LIGHTEN_10_PERCENT = new LightenColorDecorator(0.025);


  public LightenColorDecorator(double shift)
  {
    super(shift);
  }


  public void applyTo(PreciseColor c)
  {
    InvertColorDecorator.DECO_INVERT.applyTo(c);
    super.applyTo(c);
    InvertColorDecorator.DECO_INVERT.applyTo(c);
  }
}
