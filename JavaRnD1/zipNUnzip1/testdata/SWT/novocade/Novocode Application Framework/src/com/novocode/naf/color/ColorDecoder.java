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

import java.lang.reflect.Field;
import java.util.*;

import org.eclipse.swt.SWT;

import com.novocode.naf.app.NAFException;
import com.novocode.naf.data.DataDecoder;


/**
 * Contains utility methods to decode color specifications from Strings.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Apr 26, 2004
 */

public class ColorDecoder
{
  private static final Map<String, Integer> fwdSysColors = new HashMap<String, Integer>();
  private static final Map<Integer, String> revSysColors = new HashMap<Integer, String>();
  static
  {
    try
    {
      for(Field f : SWT.class.getDeclaredFields())
      {
        String n = f.getName();
        if(!n.startsWith("COLOR_")) continue;
        n = n.substring(6).toLowerCase().replace('_', '-');
        Integer ii = (Integer)f.get(null);
        fwdSysColors.put(n, ii);
        revSysColors.put(ii, n);
      }
    }
    catch(IllegalAccessException ex) { throw new NAFException("Error reading system colors", ex); }
  }


  private ColorDecoder() {}

  
  public static Integer sysColorFor(String s)
  {
    return fwdSysColors.get(s.toLowerCase().replace(' ', '-').replace('_', '-'));
  }
  
  
  public static String sysColorStringFor(int i)
  {
    return revSysColors.get(new Integer(i));
  }
  
  
  public static ColorData[] decodeColorArray(String s) throws NAFException
  {
    String[] names = DataDecoder.decodeStringArray(s, ",", true, false);
    if(names.length == 1 && names[0].length() == 0) return null;
    ColorData[] cd = new ColorData[names.length];
    for(int i=0; i<names.length; i++) cd[i] = decodeColor(names[i]);
    return cd;
  }


  public static ColorData decodeColor(String s) throws NAFException
  {
    if(s == null || s.length() == 0 || s.equals("default")) return ColorData.DEFAULT;
    s = s.trim();

    String[] values = DataDecoder.decodeStringArray(s, ":", true, true);
    s = values[0];
    ColorData cd;

    if(s.equals("inherit")) cd = ColorData.INHERIT;
    else if(s.startsWith("#"))
    {
      if(s.length() != 7) throw new NAFException("Illegal color code "+s+": '#' must be followed by 6 hex digits");
      String rs = s.substring(1,3);
      String gs = s.substring(3,5);
      String bs = s.substring(5);
      try
      {
        int ri = Integer.parseInt(rs, 16);
        int gi = Integer.parseInt(gs, 16);
        int bi = Integer.parseInt(bs, 16);
        cd = new ColorData(ri, gi, bi);
      }
      catch(NumberFormatException ex)
      {
        throw new NAFException("Illegal color code "+s+": '#' must be followed by 6 hex digits");
      }
    }
    else
    {
      Integer id = sysColorFor(s);
      if(id == null) throw new NAFException("Unknown system color name "+s);
      else cd = new ColorData(id.intValue());
    }

    if(values.length > 1)
    {
      IColorDecorator[] decorators = new IColorDecorator[values.length-1];
      for(int i=0; i<decorators.length; i++)
      {
        String dn = values[i+1];
        IColorDecorator deco;
        if(dn.equals("lighter")) deco = LightenColorDecorator.DECO_LIGHTEN_10_PERCENT;
        else if(dn.equals("darker")) deco = DarkenColorDecorator.DECO_DARKEN_10_PERCENT;
        else if(dn.equals("invert")) deco = InvertColorDecorator.DECO_INVERT;
        else if(dn.equals("warmer")) deco = RGBMatrixColorDecorator.DECO_WARMER;
        else if(dn.equals("cooler")) deco = RGBMatrixColorDecorator.DECO_COOLER;
        else throw new NAFException("Illegal color decorator \""+dn+"\"");
        decorators[i] = deco;
      }
      cd.setDecorators(decorators);
    }
    
    return cd;
  }
}
