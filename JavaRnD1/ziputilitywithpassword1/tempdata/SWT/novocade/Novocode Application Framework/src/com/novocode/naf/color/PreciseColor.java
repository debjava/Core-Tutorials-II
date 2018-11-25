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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.novocode.naf.app.NAFException;


/**
 * A color representation which uses "double" values for the
 * color components and which supports multiple color models.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Apr 26, 2004
 */

public final class PreciseColor
{
  private static final Logger LOGGER = LoggerFactory.getLogger(PreciseColor.class);

  public static final int MODEL_RGB     = 0;
  public static final int MODEL_RGB_INT = 1;
  public static final int MODEL_HSV     = 2;

  public final double[] rgb    = new double[3];
  public final double[] hsv    = new double[3];
  public final int[]    rgbInt = new int[3];

  private int currentModel;


  public PreciseColor(int model)
  {
    currentModel = model;
  }


  public PreciseColor(int red, int green, int blue)
  {
    currentModel = MODEL_RGB_INT;
    rgbInt[0] = red;
    rgbInt[1] = green;
    rgbInt[2] = blue;
  }


  /**
   * Converts the current color in the current model to the specified model
   * and makes that the current model. Nothing is done if the specified model
   * is already the current model.
   */

  public void convertToModel(int model)
  {
    if(model == currentModel) return;

    LOGGER.debug("PreciseColor: Converting from {} to {}", currentModel, model);
    LOGGER.debug("Before: {}", this);

    switch((currentModel << 8) | model)
    {
      case (MODEL_RGB << 8) | MODEL_RGB_INT:
        rgbToRgbInt();
        break;
      case (MODEL_RGB << 8) | MODEL_HSV:
        rgbToHsv();
        break;
      case (MODEL_RGB_INT << 8) | MODEL_RGB:
        rgbIntToRgb();
        break;
      case (MODEL_RGB_INT << 8) | MODEL_HSV:
        rgbIntToRgb();
        rgbToHsv();
        break;
      case (MODEL_HSV << 8) | MODEL_RGB:
        hsvToRgb();
        break;
      case (MODEL_HSV << 8) | MODEL_RGB_INT:
        hsvToRgb();
        rgbToRgbInt();
        break;
      default:
        throw new NAFException("PreciseColor cannot convert from model "+currentModel+" to model "+model);
    }
    
    LOGGER.debug("After: {}", this);
    currentModel = model;
  }


  /**
   * Makes the specified model the current model without conversion.
   */

  public void setCurrentModel(int model)
  {
    currentModel = model;
  }


  public String toString()
  {
    return "[PreciseColor model="+currentModel+" 0.rgb=["+rgb[0]+", "+rgb[1]+", "+rgb[2]+"], 1.rgbInt=["+rgbInt[0]+", "+rgbInt[1]+", "+rgbInt[2]+"], 2.hsv=["+hsv[0]+", "+hsv[1]+", "+hsv[2]+"]]";
  }


  private void rgbToRgbInt()
  {
    rgbInt[0] = Math.max(0, Math.min(255, (int)(rgb[0]*255.0 + 0.5)));
    rgbInt[1] = Math.max(0, Math.min(255, (int)(rgb[1]*255.0 + 0.5)));
    rgbInt[2] = Math.max(0, Math.min(255, (int)(rgb[2]*255.0 + 0.5)));
  }


  private void rgbIntToRgb()
  {
    rgb[0] = rgbInt[0] / 255.0;
    rgb[1] = rgbInt[1] / 255.0;
    rgb[2] = rgbInt[2] / 255.0;
  }


  private void hsvToRgb()
  {
    double h = hsv[0], s = hsv[1], v = hsv[2];
    if (s == 0) rgb[0] = rgb[1] = rgb[2] = v;
    else
    {
      h = (h - Math.floor(h)) * 6.0;
      double f = h - Math.floor(h);
      double p = v * (1.0 - s);
      double q = v * (1.0 - s * f);
      double t = v * (1.0 - (s * (1.0 - f)));
      switch ((int) h)
      {
        case 0: rgb[0] = v; rgb[1] = t; rgb[2] = p; break;
        case 1: rgb[0] = q; rgb[1] = v; rgb[2] = p; break;
        case 2: rgb[0] = p; rgb[1] = v; rgb[2] = t; break;
        case 3: rgb[0] = p; rgb[1] = q; rgb[2] = v; break;
        case 4: rgb[0] = t; rgb[1] = p; rgb[2] = v; break;
        case 5: rgb[0] = v; rgb[1] = p; rgb[2] = q; break;
      }
    }
  }


  private void rgbToHsv()
  {
    double r = rgb[0], g = rgb[1], b = rgb[2];
    double cmax = (r > g) ? r : g;
    if (b > cmax) cmax = b;
    double cmin = (r < g) ? r : g;
    if (b < cmin) cmin = b;
    hsv[2] = cmax;
    hsv[1] = (cmax != 0) ? (cmax - cmin) / cmax : 0;
    if (hsv[1] == 0) hsv[0] = 0;
    else
    {
      double dc = cmax - cmin;
      double rc = (cmax-r) / dc, gc = (cmax-g) / dc, bc = (cmax-b) / dc;
      double h;
      if(r == cmax) h = bc - gc;
      else if(g == cmax) h = 2.0 + rc - bc;
      else h = 4.0 + gc - rc;
      h = h / 6.0;
      if(h < 0) h = h + 1.0;
      hsv[0] = h;
    }
  }
}
