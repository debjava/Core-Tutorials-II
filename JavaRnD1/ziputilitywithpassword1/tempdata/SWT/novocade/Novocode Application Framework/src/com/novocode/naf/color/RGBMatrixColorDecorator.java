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
 * A color decorator which makes a color warmer or colder.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Apr 26, 2004
 */

public class RGBMatrixColorDecorator implements IColorDecorator
{
  public static final IColorDecorator DECO_WARMER = new RGBMatrixColorDecorator(1.025, 0.0, 0.0, 0.0, 0.0, 1.025, 0.0, 0.0, 0.0, 0.0, 0.95, 0.0);
  public static final IColorDecorator DECO_COOLER = new RGBMatrixColorDecorator(0.975, 0.0, 0.0, 0.0, 0.0, 0.975, 0.0, 0.0, 0.0, 0.0, 1.05, 0.0);


  private double rr, rg, rb, rc, gr, gg, gb, gc, br, bg, bb, bc;


  public RGBMatrixColorDecorator(
    double rr, double rg, double rb, double rc,
    double gr, double gg, double gb, double gc,
    double br, double bg, double bb, double bc)
  {
    this.rr = rr;
    this.rg = rg;
    this.rb = rb;
    this.rc = rc;
    this.gr = gr;
    this.gg = gg;
    this.gb = gb;
    this.gc = gc;
    this.br = br;
    this.bg = bg;
    this.bb = bb;
    this.bc = bc;
  }


  public void applyTo(PreciseColor c)
  {
    c.convertToModel(PreciseColor.MODEL_RGB);
    double r = c.rgb[0] * rr + c.rgb[1] * rg + c.rgb[2] * rb + rc;
    double g = c.rgb[0] * gr + c.rgb[1] * gg + c.rgb[2] * gb + gc;
    double b = c.rgb[0] * br + c.rgb[1] * bg + c.rgb[2] * bb + bc;
    c.rgb[0] = Math.max(0, Math.min(1.0, r));
    c.rgb[1] = Math.max(0, Math.min(1.0, g));
    c.rgb[2] = Math.max(0, Math.min(1.0, b));
  }
}
