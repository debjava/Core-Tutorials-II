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

package com.novocode.naf.gui;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.novocode.naf.app.*;


/**
 * Manages a pool of shared fonts.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 6, 2003
 */

public final class FontManager
{
  private static final Logger LOGGER = LoggerFactory.getLogger(FontManager.class);

  private Map<String, ManagedFont> fonts;
  private Map<Font, ManagedFont> reverse;
  private Display display;
  private boolean autoDispose;


  private static final class ManagedFont
  {
    String resname;
    Font font;
    FontData fontData;
    int references;
  }


  /**
   * Create a new FontManager.
   */

  public FontManager(Display display, boolean autoDispose)
  {
    this.fonts = new HashMap<String, ManagedFont>();
    this.reverse = new HashMap<Font, ManagedFont>();
    this.display = display;
    this.autoDispose = autoDispose;

    display.addListener(SWT.Dispose, new Listener()
    {
      public void handleEvent(Event event)
      {
        disposeAll();
      }
    });
  }


  public synchronized Font getFont(FontData[] fds) throws NAFException
  {
    String fdstring;
    if(fds.length == 1) fdstring = fds[0].toString();
    else
    {
      StringBuilder buf = new StringBuilder();
      for(int i=0; i<fds.length; i++) buf.append("*__FD__*").append(fds[i]);
      fdstring = buf.toString();
    }

    ManagedFont mi = fonts.get(fdstring);
    if(mi == null)
    {
      mi = new ManagedFont();
      try
      {
        mi.font = new Font(display, fds);
      }
      catch(SWTException ex)
      {
        throw new NAFException("Error loading font with font data \""+fdstring+"\"", ex);
      }
      mi.resname = fdstring;
      fonts.put(fdstring, mi);
      reverse.put(mi.font, mi);
    }
    mi.references++;
    return mi.font;
  }


  public synchronized void returnFont(Font font) throws NAFException
  {
    if(font.isDisposed()) return;
    ManagedFont mi = reverse.get(font);
    if(mi == null)
      throw new NAFException("The font supplied to returnFont() is not managed by this FontManager");
    if(mi.references == 0)
      throw new NAFException("Can't return font: No references left");
    mi.references--;
    if(autoDispose && mi.references == 0)
    {
      LOGGER.debug("FontManager: Disposing of font {}", mi.resname);
      mi.font.dispose();
      fonts.remove(mi.resname);
      reverse.remove(mi.font);
    }
  }


  public synchronized void disposeUnallocatedFonts()
  {
    for(Iterator<ManagedFont> it = fonts.values().iterator(); it.hasNext();)
    {
      ManagedFont mi = it.next();
      if(mi.references == 0)
      {
        LOGGER.debug("FontManager: Disposing of font {}", mi.resname);
        mi.font.dispose();
        it.remove();
        reverse.remove(mi.font);
      }
    }
  }


  public synchronized void disposeAll()
  {
    for(ManagedFont mi : fonts.values())
    {
      LOGGER.debug("FontManager: Disposing of font {}", mi.resname);
      mi.font.dispose();
    }
    fonts = new HashMap<String, ManagedFont>();
    reverse = new HashMap<Font, ManagedFont>();
  }


  public static double decodeFontSize(FontData base, final String sizeString)
  {
    String s = sizeString;
    boolean em = false, inch = false, cm = false, mm = false;
    s = s.trim();
    if(s.endsWith("em"))
    {
      em = true;
      s = s.substring(0, s.length()-2).trim();
    }
    else if(s.endsWith("in"))
    {
      inch = true;
      s = s.substring(0, s.length()-2).trim();
    }
    else if(s.endsWith("cm"))
    {
      cm = true;
      s = s.substring(0, s.length()-2).trim();
    }
    else if(s.endsWith("mm"))
    {
      mm = true;
      s = s.substring(0, s.length()-2).trim();
    }
    else if(s.endsWith("pt"))
    {
      s = s.substring(0, s.length()-2).trim();
    }
    if(base == null && em) throw new NAFException("Cannot decode font size in \"em\" measure without a base font");
    try
    {
      double d = Double.parseDouble(s);
      if(inch) d *= 72.0;
      if(cm) d *= 72.0/2.54;
      if(mm) d *= 72.0/25.4;
      if(em) d *= base.getHeight();
      return d;
    }
    catch(NumberFormatException ex)
    {
      throw new NAFException("Error parsing font size \""+sizeString+"\"");
    }
  }


  public static FontData[] decodeFontSpec(FontData[] base, final String spec) throws NAFException
  {
    if(spec == null || spec.length() == 0)
    {
      if(base == null)
        throw new NAFException("Cannot create font data without either a base font or a font specification string");
      else return base;
    }
    StringTokenizer tok = new StringTokenizer(spec, ",");
    String name = null, height = null;
    int style = 0;
    boolean normal = false;
    while(tok.hasMoreElements())
    {
      String s = tok.nextToken().trim();
      if(s.length() == 0) continue;
      if(s.equals("normal")) normal = true;
      else if(s.equals("bold")) style |= SWT.BOLD;
      else if(s.equals("italic")) style |= SWT.ITALIC;
      else if(Character.isDigit(s.charAt(0))) height = s;
      else name = s;
    }
    
    if(base == null)
    {
      int ptHeight = (height == null) ? -1 : (int)decodeFontSize(null, height);
      if(ptHeight == -1) throw new NAFException("Cannot create non-derived font without a font height");
      return new FontData[] { new FontData(name, ptHeight, style) };
    }
    else
    {
      FontData[] fda = new FontData[base.length];
      for(int i=0; i<fda.length; i++)
      {
        int ptHeight = (height == null) ? -1 : (int)decodeFontSize(base[i], height);
        FontData fd = new FontData(base[i].toString());
        if(name != null) fd.setName(name);
        if(ptHeight != -1) fd.setHeight(ptHeight);
        if(style != 0 || normal) fd.setStyle(style);
        fda[i] = fd;
      }
      return fda;
    }
  }
}
