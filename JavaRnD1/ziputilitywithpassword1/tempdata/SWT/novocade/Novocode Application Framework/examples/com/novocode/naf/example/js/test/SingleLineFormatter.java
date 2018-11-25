/*******************************************************************************
 * Copyright (c) 2008 Stefan Zeiger and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.novocode.com/legal/epl-v10.html
 * 
 * Contributors:
 *     Stefan Zeiger (szeiger@novocode.com) - initial API and implementation
 *******************************************************************************/

package com.novocode.naf.example.js.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.LogRecord;


public class SingleLineFormatter extends java.util.logging.Formatter
{
  private static final SimpleDateFormat DF = new SimpleDateFormat("HH:mm:ss");
  private static final String PAD = "                                        ";


  @Override
  public synchronized String format(LogRecord record)
  {
    StringBuilder b = new StringBuilder();
    b.append(DF.format(new Date(record.getMillis()))).append(' ');
    append(b, record.getLevel(), 4, false);
    b.append(' ');
    append(b, Thread.currentThread().getName(), 4, true);
    b.append('|');
    append(b, record.getLoggerName(), 25, true);
    b.append(' ');
    b.append(record.getMessage());
    b.append('\n');
    return b.toString();
  }


  private static void append(StringBuilder b, Object o, int len, boolean right)
  {
    String s = (o == null) ? null : o.toString();
    int l = s.length();
    if(right)
    {
      if(l > len) b.append(s.substring(l-len));
      else
      {
        if(len > l) b.append(PAD.substring(0, len-l));
        b.append(s);
      }
    }
    else
    {
      if(l > len) b.append(s.substring(0, len));
      else
      {
        b.append(s);
        if(len > l) b.append(PAD.substring(0, len-l));
      }
    }
  }
}
