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

package com.novocode.naf.data;

import com.novocode.naf.color.ColorData;
import com.novocode.naf.color.ColorDecoder;


/**
 * A simple data converter which can make direct translations of String,
 * int, boolean, int[], ColorData, ColorData[], Enum, Enum[], SizeMeasure,
 * SizeMeasurePair and SizeMeasureQuadruple.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Oct 6, 2004
 * @version $Id: DefaultDataConverter.java 395 2008-04-07 18:54:39Z szeiger $
 */

public class DefaultDataConverter implements IDataConverter
{
  private static final DefaultDataConverter instance = new DefaultDataConverter();


  public static DefaultDataConverter getInstance() { return instance; }


  private DefaultDataConverter() {}


  public String toExternal(Class<?> type, Object value) throws Exception
  {
    if(value == null) return null;
    else if(type == int[].class)
    {
      int[] ia = (int[])value;
      StringBuilder buf = new StringBuilder();
      for(int i=0; i<ia.length; i++)
      {
        if(i != 0) buf.append(", ");
        buf.append(ia[i]);
      }
      return buf.toString();
    }
    else if(type == ColorData[].class)
    {
      ColorData[] cda = (ColorData[])value;
      StringBuilder buf = new StringBuilder();
      for(int i=0; i<cda.length; i++)
      {
        if(i != 0) buf.append(", ");
        buf.append(cda[i]);
      }
      return buf.toString();
    }
    else if(Enum.class.isAssignableFrom(type))
      return value.toString().toLowerCase().replace('_', '-');
    else if(Enum[].class.isAssignableFrom(type))
    {
      Enum<?>[] ea = (Enum[])value;
      StringBuilder buf = new StringBuilder();
      for(int i=0; i<ea.length; i++)
      {
        if(i != 0) buf.append(", ");
        buf.append(ea[i].toString().toLowerCase().replace('_', '-'));
      }
      return buf.toString();
    }
    else return value.toString();
  }


  @SuppressWarnings("unchecked")
  public Object toInternal(Class<?> type, String value) throws Exception
  {
    if(type == Integer.TYPE) return Integer.valueOf(value);
    else if(type == Boolean.TYPE) return Boolean.valueOf(DataDecoder.decodeBoolean(value, false));
    else if(type == ColorData[].class) return ColorDecoder.decodeColorArray(value);
    else if(type == ColorData.class) return ColorDecoder.decodeColor(value);
    else if(type == int[].class) return DataDecoder.decodeNumberArray(value);
    else if(type == SizeMeasure.class) return DataDecoder.decodeLength(value, null);
    else if(type == SizeMeasurePair.class) return DataDecoder.decodeLengthPair(value, null);
    else if(type == SizeMeasureQuadruple.class) return DataDecoder.decodeLengthQuadruple(value, null);
    else if(Enum.class.isAssignableFrom(type))
      return DataDecoder.decodeEnum(value, (Class<? extends Enum>)type);
    else if(Enum[].class.isAssignableFrom(type))
      return DataDecoder.decodeEnumArray(value, (Class<? extends Enum>)(type.getComponentType()));
    else return value;
  }
}
