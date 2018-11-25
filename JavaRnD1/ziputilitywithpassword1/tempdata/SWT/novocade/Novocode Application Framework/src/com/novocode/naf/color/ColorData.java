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

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.novocode.naf.app.NAFException;


/**
 * Contains a decoded color which is either a system color
 * or a RGB color code.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Feb 28, 2004
 */

public final class ColorData
{
  private static final Logger LOGGER = LoggerFactory.getLogger(ColorData.class);

  public static final int TYPE_INHERIT      = 0;
  public static final int TYPE_DEFAULT      = 1;
  public static final int TYPE_SYSTEM_COLOR = 2;
  public static final int TYPE_CUSTOM_COLOR = 3;

  
  public static final ColorData INHERIT = new ColorData(true);
  public static final ColorData DEFAULT = new ColorData(false);

  
  public final int systemColorCode;


  private int red, green, blue, baseType, type;
  private IColorDecorator[] decorators;


  public ColorData(int systemColorCode)
  {
    this.systemColorCode = systemColorCode;
    this.baseType = TYPE_SYSTEM_COLOR;
    this.type = TYPE_SYSTEM_COLOR;
  }

    
  public ColorData(int red, int green, int blue)
  {
    this.systemColorCode = -1;
    this.baseType = TYPE_CUSTOM_COLOR;
    this.type = TYPE_CUSTOM_COLOR;
    this.red = red;
    this.green = green;
    this.blue = blue;
  }


  private ColorData(boolean b)
  {
    this.systemColorCode = -1;
    this.type = b ? TYPE_INHERIT : TYPE_DEFAULT;
  }


  public void setDecorators(IColorDecorator[] decorators) throws NAFException
  {
    if(type == TYPE_DEFAULT || type == TYPE_INHERIT)
      throw new NAFException("Cannot decorate colors \"default\" and \"inherit\"");
    this.type = TYPE_CUSTOM_COLOR;
    this.decorators = decorators;
  }
  
  
  public int getType() { return type; }


  public Color createCustomColor(Device device)
  {
    if(decorators == null) return new Color(device, red, green, blue);
    else
    {
      PreciseColor c;
      if(baseType == TYPE_SYSTEM_COLOR)
      {
        Color color = device.getSystemColor(systemColorCode);
        LOGGER.debug("System color: {}", color);
        c = new PreciseColor(color.getRed(), color.getGreen(), color.getBlue());
      }
      else c = new PreciseColor(red, green, blue);
      for(int i=0; i<decorators.length; i++)
      {
        if(LOGGER.isDebugEnabled()) LOGGER.debug("Before applying decorator "+i+": "+c);
        decorators[i].applyTo(c);
      }
      LOGGER.debug("After applying all decorators: {}", c);
      c.convertToModel(PreciseColor.MODEL_RGB_INT);
      return new Color(device, c.rgbInt[0], c.rgbInt[1], c.rgbInt[2]);
    }
  }
  
  
  public String toString()
  {
    switch(type)
    {
      case TYPE_DEFAULT: return null;
      case TYPE_INHERIT: return "inherit";
      case TYPE_SYSTEM_COLOR: return ColorDecoder.sysColorStringFor(systemColorCode);
      case TYPE_CUSTOM_COLOR:
        String r = Integer.toHexString(red);
        String g = Integer.toHexString(green);
        String b = Integer.toHexString(blue);
        if(r.length() == 1) r = '0'+r;
        if(g.length() == 1) g = '0'+g;
        if(b.length() == 1) b = '0'+b;
        return '#'+r+g+b;
    }
    return null;
  }
}
