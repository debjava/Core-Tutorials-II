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

import java.net.URL;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.novocode.naf.app.NAFException;
import com.novocode.naf.color.ColorData;
import com.novocode.naf.gui.event.*;
import com.novocode.naf.gui.image.*;
import com.novocode.naf.model.IModel;


/**
 * Contains static utility methods for SWT widget creation and manipulation.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 5, 2003
 * @version $Id: SWTUtil.java 396 2008-04-09 23:10:49Z szeiger $
 */

public final class SWTUtil
{
  private static final Logger LOGGER = LoggerFactory.getLogger(SWTUtil.class);

  private static final boolean IS_MAC = "carbon".equals(SWT.getPlatform());


  private static final HashMap<String, Integer> keycodes = new HashMap<String, Integer>();

  static
  {
    keycodes.put("shift",    new Integer(SWT.SHIFT));
    keycodes.put("ctrl",     new Integer(IS_MAC ? SWT.COMMAND : SWT.CTRL));
    keycodes.put("alt",      new Integer(SWT.ALT));
    keycodes.put("left",     new Integer(SWT.ARROW_LEFT));
    keycodes.put("right",    new Integer(SWT.ARROW_RIGHT));
    keycodes.put("up",       new Integer(SWT.ARROW_UP));
    keycodes.put("down",     new Integer(SWT.ARROW_DOWN));
    keycodes.put("f1",       new Integer(SWT.F1));
    keycodes.put("f2",       new Integer(SWT.F2));
    keycodes.put("f3",       new Integer(SWT.F3));
    keycodes.put("f4",       new Integer(SWT.F4));
    keycodes.put("f5",       new Integer(SWT.F5));
    keycodes.put("f6",       new Integer(SWT.F6));
    keycodes.put("f7",       new Integer(SWT.F7));
    keycodes.put("f8",       new Integer(SWT.F8));
    keycodes.put("f9",       new Integer(SWT.F9));
    keycodes.put("f10",      new Integer(SWT.F10));
    keycodes.put("f11",      new Integer(SWT.F11));
    keycodes.put("f12",      new Integer(SWT.F12));
    keycodes.put("pause",    new Integer(SWT.PAUSE));
    keycodes.put("insert",   new Integer(SWT.INSERT));
    keycodes.put("home",     new Integer(SWT.HOME));
    keycodes.put("end",      new Integer(SWT.END));
    keycodes.put("pageup",   new Integer(SWT.PAGE_UP));
    keycodes.put("pagedown", new Integer(SWT.PAGE_DOWN));
    keycodes.put("plus",     new Integer('+'));
    keycodes.put("minus",    new Integer('-'));
  }


  private SWTUtil() {}


  public static void setRegisteredForeground(Control control, ColorData c)
  {
    if(c.getType() == ColorData.TYPE_SYSTEM_COLOR) control.setForeground(control.getDisplay().getSystemColor(c.systemColorCode));
    else if(c.getType() == ColorData.TYPE_CUSTOM_COLOR)
    {
      final Color color = c.createCustomColor(control.getDisplay());
      control.setForeground(color);
      control.addDisposeListener(new DisposeListener()
      {
        public void widgetDisposed(DisposeEvent e)
        {
          LOGGER.debug("Disposing of fgcolor {}", color);
          color.dispose();
        }
      });
    }
  }


  public static void setRegisteredBackground(Control control, ColorData c)
  {
    if(c.getType() == ColorData.TYPE_SYSTEM_COLOR) control.setBackground(control.getDisplay().getSystemColor(c.systemColorCode));
    else if(c.getType() == ColorData.TYPE_CUSTOM_COLOR)
    {
      final Color color = c.createCustomColor(control.getDisplay());
      control.setBackground(color);
      control.addDisposeListener(new DisposeListener()
      {
        public void widgetDisposed(DisposeEvent e)
        {
          LOGGER.debug("Disposing of bgcolor {}", color);
          color.dispose();
        }
      });
    }
  }


  public static boolean getEnabled(Widget w)
  {
    if(w instanceof Control) return ((Control)w).getEnabled();
    else if(w instanceof ToolItem) return ((ToolItem)w).getEnabled();
    else if(w instanceof MenuItem) return ((MenuItem)w).getEnabled();
    else throw new NAFException("Can't perform getEnabled() on a "+w.getClass().getName()+" object");
  }


  public static void setEnabledIfChanged(Widget w, boolean b)
  {
    if(w instanceof Control)
    {
      Control control = (Control)w;
      if(control.getEnabled() != b) control.setEnabled(b);
    }
    else if(w instanceof ToolItem)
    {
      ToolItem item = (ToolItem)w;
      if(item.getEnabled() != b) item.setEnabled(b);
    }
    else if(w instanceof MenuItem)
    {
      MenuItem item = (MenuItem)w;
      if(item.getEnabled() != b) item.setEnabled(b);
    }
    else throw new NAFException("Can't perform setEnabledIfChanged() on a "+w.getClass().getName()+" object");
  }


  public static boolean getSelection(Widget w)
  {
    if(w instanceof Button) return ((Button)w).getSelection();
    else if(w instanceof ToolItem) return ((ToolItem)w).getSelection();
    else if(w instanceof MenuItem) return ((MenuItem)w).getSelection();
    else throw new NAFException("Can't perform getSelection() on a "+w.getClass().getName()+" object");
  }


  public static String getText(Widget w)
  {
    if(w instanceof Button) return ((Button)w).getText();
    else if(w instanceof ToolItem) return ((ToolItem)w).getText();
    else if(w instanceof MenuItem) return ((MenuItem)w).getText();
    else throw new NAFException("Can't perform getText() on a "+w.getClass().getName()+" object");
  }


  public static void setText(Widget w, String s)
  {
    if(w instanceof Button) ((Button)w).setText(s);
    else if(w instanceof ToolItem) ((ToolItem)w).setText(s);
    else if(w instanceof MenuItem) ((MenuItem)w).setText(s);
    else throw new NAFException("Can't perform setText() on a "+w.getClass().getName()+" object");
  }


  public static String getTooltip(Widget w)
  {
    if(w instanceof Control) return ((Control)w).getToolTipText();
    else if(w instanceof ToolItem) return ((ToolItem)w).getToolTipText();
    else throw new NAFException("Can't perform getTooltip() on a "+w.getClass().getName()+" object");
  }


  public static void setTooltip(Widget w, String s)
  {
    if(w instanceof Control) ((Control)w).setToolTipText(s);
    else if(w instanceof ToolItem) ((ToolItem)w).setToolTipText(s);
    else throw new NAFException("Can't perform setTooltip() on a "+w.getClass().getName()+" object");
  }


  public static void setSelectionIfChanged(Widget w, boolean b)
  {
    if(w instanceof Button)
    {
      Button item = (Button)w;
      if(item.getSelection() != b) item.setSelection(b);
    }
    else if(w instanceof ToolItem)
    {
      ToolItem item = (ToolItem)w;
      if(item.getSelection() != b) item.setSelection(b);
    }
    else if(w instanceof MenuItem)
    {
      MenuItem item = (MenuItem)w;
      if(item.getSelection() != b) item.setSelection(b);
    }
    else throw new NAFException("Can't perform setSelectionIfChanged() on a "+w.getClass().getName()+" object");
  }


  public static Image getRegisteredImage(String imgres, URL baseURL, final Widget widget,
    final ResourceImageManager imageManager, boolean useAlpha) throws NAFException
  {
    if(imgres == null || imgres.length() == 0) return null;
    imgres = ResourceImageManager.absoluteURIFor(baseURL, imgres);
    
    final IManagedImage managedImage =
      useAlpha ? imageManager.getAlphaRemappedImage(imgres) : imageManager.getMaskRemappedImage(imgres);
    Image image = managedImage.acquire();
    widget.addDisposeListener(new DisposeListener()
    {
      public void widgetDisposed(DisposeEvent e)
      {
        managedImage.release();
      }
    });
    return image;
  }


  public static void setRegisteredImage(String imgres, URL baseURL, final Widget widget,
    final ResourceImageManager imageManager, boolean useAlpha) throws NAFException
  {
    if(imgres == null) return;
    if(!(widget instanceof Item || widget instanceof Decorations || widget instanceof Item
         || widget instanceof Button || widget instanceof Label || widget instanceof CLabel)) return;
    imgres = ResourceImageManager.absoluteURIFor(baseURL, imgres);

    if(imgres.length() > 0)
    {
      final IManagedImage managedImage =
        useAlpha ? imageManager.getAlphaRemappedImage(imgres) : imageManager.getMaskRemappedImage(imgres);
      Image img = managedImage.acquire();
      if(widget instanceof Item) ((Item)widget).setImage(img);
      else if(widget instanceof Button) ((Button)widget).setImage(img);
      else if(widget instanceof Label) ((Label)widget).setImage(img);
      else if(widget instanceof CLabel) ((CLabel)widget).setImage(img);
      else if(widget instanceof Item) ((Item)widget).setImage(img);
      else /* widget instanceof Decorations */ ((Decorations)widget).setImage(img);
      widget.addDisposeListener(new DisposeListener()
      {
        public void widgetDisposed(DisposeEvent e)
        {
          managedImage.release();
        }
      });
    }
  }


  public static void setRegisteredFont(final FontData[] fds, final Control control, final FontManager fontManager) throws NAFException
  {
    final Font font = (fds == null) ? null : fontManager.getFont(fds);

    control.setFont(font);

    if(font != null)
    {
      control.addDisposeListener(new DisposeListener()
      {
        public void widgetDisposed(DisposeEvent e)
        {
          fontManager.returnFont(font);
        }
      });
    }
  }


  public static void setRegisteredCursor(final Cursor cursor, final Control control) throws NAFException
  {
    control.setCursor(cursor);

    if(cursor != null)
    {
      control.addDisposeListener(new DisposeListener()
      {
        public void widgetDisposed(DisposeEvent e)
        {
          cursor.dispose();
        }
      });
    }
  }


  public static int computeEMBase(Composite comp)
  {
    GC gc = new GC(comp);
    try
    {
      return gc.getFontMetrics().getHeight();
    }
    finally { gc.dispose(); }
  }


  public static final void registerModel(Widget widget, final IModel model, IChangeListener cl)
  {
    final IChangeListener swtl = new SWTChangeAdapter(cl, widget.getDisplay());
    widget.addDisposeListener(new DisposeListener()
    {
      public void widgetDisposed(DisposeEvent e)
      {
        model.removeChangeListener(swtl);
      }
    });
    model.addChangeListener(swtl);
  }


  public static final void registerModel(Widget widget, final IActionSource model, IActionListener l)
  {
    final IActionListener swtl = new SWTActionAdapter(l, widget.getDisplay());
    widget.addDisposeListener(new DisposeListener()
    {
      public void widgetDisposed(DisposeEvent e)
      {
        model.removeActionListener(swtl);
      }
    });
    model.addActionListener(swtl);
  }

  
  public static final void registerShellEventFilter(final Shell shell, final int eventType, final Listener listener)
  {
    final Display display = shell.getDisplay();
    final Listener shellListener = new Listener()
    {
      public void handleEvent(Event event)
      {
        if(!(event.widget instanceof Control)) return;
        if(((Control)(event.widget)).getShell() != shell) return;
        listener.handleEvent(event);
      }
    };
    shell.addDisposeListener(new DisposeListener()
    {
      public void widgetDisposed(DisposeEvent e)
      {
        display.removeFilter(eventType, shellListener);
      }
    });
    display.addFilter(eventType, shellListener);
  }

  
  public static int decodeAccelerator(String accelerator)
  {
    if(accelerator == null || accelerator.length() == 0) return 0;
    int acc = 0;
    boolean expectKey = true;
    for(StringTokenizer tok = new StringTokenizer(accelerator, "+", true); tok.hasMoreTokens();)
    {
      String t = tok.nextToken();
      if(!expectKey && t.equals("+")) { expectKey = true; continue; }
      if(t.length() == 1) acc |= Character.toLowerCase(t.charAt(0));
      else
      {
        Integer i = keycodes.get(t.toLowerCase());
        if(i == null) throw new NAFException("Unknown accelerator key \""+t+"\" in accelerator string \""+accelerator+"\"");
        else acc |= i.intValue();
      }
      expectKey = false;
    }
    return acc;
  }


  public static int decodeMouseButton(String s)
  {
    if(s == null || s.length() == 0) return 0;
    if(s.length() == 1)
    {
      char c = s.charAt(0);
      if(c >= '1' && c <= '9') return c-'0';
    }
    else
    {
      s = s.toLowerCase();
      if(s.equals("primary") || s.equals("left")) return 1;
      else if(s.equals("middle")) return 2;
      else if(s.equals("secondary") || s.equals("right")) return 3;
      else if(s.equals("back")) return 4;
      else if(s.equals("forward")) return 5;
    }
    throw new NAFException("Unknown mouse button name \""+s+"\"");
  }
}
