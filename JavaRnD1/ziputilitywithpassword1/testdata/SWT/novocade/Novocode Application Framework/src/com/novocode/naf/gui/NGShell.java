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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.novocode.naf.app.*;
import com.novocode.naf.data.SizeMeasure;
import com.novocode.naf.gui.event.*;
import com.novocode.naf.gui.image.*;
import com.novocode.naf.model.*;
import com.novocode.naf.resource.*;


/**
 * A window.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Nov 27, 2003
 * @version $Id: NGShell.java 416 2008-05-05 17:51:40Z szeiger $
 */

public final class NGShell extends NGWindow
{
  private static final Logger LOGGER = LoggerFactory.getLogger(NGShell.class);

  private static final SizeMeasure DEFAULT_SIZE = new SizeMeasure(SWT.DEFAULT);


  private String imageResource;
  private SizeMeasure width = DEFAULT_SIZE, height = DEFAULT_SIZE;
  private SizeMeasure minWidth = DEFAULT_SIZE, minHeight = DEFAULT_SIZE;
  private String[] imageResources = new String[0];
  private Trim[] trim = new Trim[] { Trim.CLOSE, Trim.TITLE, Trim.MIN };
  private boolean resize = true;
  private Position position = Position.DEFAULT;

  
  public enum Position
  {
    DEFAULT, CENTER_PARENT, CENTER_PARENT_CLIENT_AREA, CENTER_DISPLAY, CENTER_DISPLAY_CLIENT_AREA, FULL_DISPLAY
  }
  
  public enum Trim
  {
    BORDER (SWT.BORDER), CLOSE (SWT.CLOSE), MIN (SWT.MIN), BORDERLESS (SWT.NO_TRIM),
    TITLE (SWT.TITLE), SHELL (SWT.CLOSE|SWT.TITLE|SWT.MIN), DIALOG (SWT.DIALOG_TRIM),
    // HSCROLL (SWT.H_SCROLL), VSCROLL (SWT.V_SCROLL),
    TOP (SWT.ON_TOP), TOOL (SWT.TOOL);
    
    public final int style;
    Trim(int style) { this.style = style; }
    
    public static int style(Trim[] a)
    {
      if(a == null) return 0;
      int res = 0;
      for(Trim t : a) if(t != null) res |= t.style;
      return res;
    }
  }


  public NGShell()
  {
    modality = Modality.MODELESS;
  }


  @ConfProperty
  public void setResize(boolean b) { resize = b; }
  public boolean getResize() { return resize; }


  @ConfProperty
  public void setPosition(Position i) { position = i; }
  public Position getPosition() { return position; }


  @ConfProperty("image")
  public void setImageResource(String s) { imageResource = s; }
  public String getImageResource() { return imageResource; }

  
  @ConfProperty("images")
  public void setImageResources(String[] a) { imageResources = a; }
  public String[] getImageResources() { return imageResources; }

  
  @ConfProperty
  public void setTrim(Trim[] a) { trim = a; }
  public Trim[] getTrim() { return trim; }

  
  @ConfProperty
  public void setWidth(SizeMeasure m)
  {
    if(m == null) m = DEFAULT_SIZE;
    width = m;
  }
  public SizeMeasure getWidth() { return width; }


  @ConfProperty
  public void setHeight(SizeMeasure m)
  {
    if(m == null) m = DEFAULT_SIZE;
    height = m;
  }
  public SizeMeasure getHeight() { return height; }


  // [TODO] Support "initial" value for minWidth and minHeight
  @ConfProperty
  public void setMinWidth(SizeMeasure m)
  {
    if(m == null) m = DEFAULT_SIZE;
    minWidth = m;
  }
  public SizeMeasure getMinWidth() { return minWidth; }


  @ConfProperty
  public void setMinHeight(SizeMeasure m)
  {
    if(m == null) m = DEFAULT_SIZE;
    minHeight = m;
  }
  public SizeMeasure getMinHeight() { return minHeight; }


  public WindowInstance createWindowInstance(NAFApplication app, ModelMap models, WindowInstance parentWI) throws NAFException
  {
    Shell parentShell = (parentWI == null) ? null : parentWI.getShell(false);
    Display display = app.getDisplay();
    ResourceImageManager imageManager = app.getImageManager();
    int style = Trim.style(trim) | modality.style;
    if(resize) style |= SWT.MAX | SWT.RESIZE;
    final Shell shell = parentShell == null ? new Shell(display, style) : new Shell(parentShell, style);
    ShellWindowInstance wi = new ShellWindowInstance(this, models, app, shell);

    if(title != null) shell.setText(title);
    NGComponent menubarComponent = getFirstExpandedRoleChild("menubar", models);
    if(menubarComponent != null)
    {
      if(!(menubarComponent instanceof NGMenu))
        throw new NAFException("Only NGMenu objects can be assigned to role \"menubar\" in NGWindow");
      Menu bar = ((NGMenu)menubarComponent).createMenuBar(shell, wi);
      shell.setMenuBar(bar);
    }

    if((imageResource != null && imageResource.length() > 0) || imageResources.length != 0)
    {
      int imgnum = imageResources.length;
      if(imageResource != null && imageResource.length() > 0) imgnum++;
      final IManagedImage[] managedImages = new IManagedImage[imgnum];
      final Image[] images = new Image[imgnum];
      for(int i=0; i<imageResources.length; i++)
      {
        managedImages[i] = imageManager.getMaskRemappedImage(ResourceImageManager.absoluteURIFor(getResource().getURL(), imageResources[i]));
        images[i] = managedImages[i].acquire();
      }
      if(imageResources.length != imgnum)
      {
        managedImages[imgnum-1] = imageManager.getMaskRemappedImage(ResourceImageManager.absoluteURIFor(getResource().getURL(), imageResource));
        images[imgnum-1] = managedImages[imgnum-1].acquire();
      }

      shell.setImages(images);
      shell.addDisposeListener(new DisposeListener()
      {
        public void widgetDisposed(DisposeEvent e)
        {
          for(int i=0; i<managedImages.length; i++) managedImages[i].release();
        }
      });
    }

    WidgetData wd = createWidgetData();
    wd.attachTo(shell);
    configureControl(shell, wi, wd, null);
    //addDefaultChildren(shell, wi, wd);
    addCommonFeatures(shell, wi, null);

    shell.pack();

    // [TODO] Improve min size handling
    final int embase = SWTUtil.computeEMBase(shell);
    final Point minShellSize = shell.getSize();
    if(width.baseSize >= 0 || height.baseSize >= 0)
    {
      int w = minShellSize.x, h = minShellSize.y;
      if(width.baseSize >= 0) w = width.compute(embase, w);
      if(height.baseSize >= 0) h = height.compute(embase, h);
      shell.setSize(w, h);
    }
    
    int computedMinWidth = minWidth.compute(embase);
    int computedMinHeight = minHeight.compute(embase);
    if(computedMinWidth != SWT.DEFAULT || computedMinHeight != SWT.DEFAULT)
    {
      Point p = shell.getMinimumSize();
      int w = p.x, h = p.y;
      if(computedMinWidth != SWT.DEFAULT) w = computedMinWidth;
      if(computedMinHeight != SWT.DEFAULT) h = computedMinHeight;
      shell.setMinimumSize(w, h);
    }


    /*if(keepMinimumSize)
    {
      ((WindowWidgetData)WidgetData.forWidget(shell)).minSize = minShellSize;
      final boolean[] lock = new boolean[1];
      shell.addControlListener(new ControlAdapter()
      {
        public void controlResized(ControlEvent e)
        {
          if(lock[0]) return;
          if(shell.getMaximized()) return;
          lock[0] = true;
          try
          {
            Point size = shell.getSize();
            if(size.x < minShellSize.x || size.y < minShellSize.y)
              shell.setSize(new Point(Math.max(size.x, minShellSize.x), Math.max(size.y, minShellSize.y)));
          }
          finally { lock[0] = false; }
        }
      });
    }*/

    final IWindowStateModel windowStateModel = getModel("window", wi.models, DefaultWindowStateModel.PTYPE);
    int wstate;
    if(windowStateModel != null && (wstate = windowStateModel.getState()) != IWindowStateModel.STATE_DEFAULTBOUNDS)
    {
      Rectangle wbounds = windowStateModel.getPluralizedBounds();
      Rectangle dbounds = display.getBounds();
      if(wbounds.width > dbounds.width) wbounds.width = dbounds.width;
      if(wbounds.height > dbounds.height) wbounds.height = dbounds.height;
      if(wbounds.x < dbounds.x) wbounds.x = dbounds.x;
      else if(wbounds.x > dbounds.x + dbounds.width - wbounds.width)
        wbounds.x = dbounds.x + dbounds.width - wbounds.height;
      if(wbounds.y < dbounds.y) wbounds.y = dbounds.y;
      else if(wbounds.y > dbounds.y + dbounds.height - wbounds.height)
        wbounds.y = dbounds.y + dbounds.height - wbounds.height;

      shell.setLocation(wbounds.x, wbounds.y);
      shell.setSize(wbounds.width, wbounds.height);
      if(wstate == IWindowStateModel.STATE_MAXIMIZED || wstate == IWindowStateModel.STATE_MINIMIZED_AFTER_MAXIMIZED)
        shell.setMaximized(true);
    }
    else if(position != Position.DEFAULT)
    {
      Rectangle bounds = null;

      switch(position)
      {
        case FULL_DISPLAY:
          shell.setBounds(display.getBounds());
          break;

        case CENTER_PARENT:
          if(parentShell == null) bounds = display.getBounds();
          else bounds = parentShell.getBounds();
          break;

        case CENTER_PARENT_CLIENT_AREA:
          if(parentShell == null) bounds = display.getClientArea();
          else
          {
            Rectangle ca = parentShell.getClientArea();
            Rectangle loc = parentShell.getBounds();
            Rectangle tr = parentShell.computeTrim(ca.x, ca.y, ca.width, ca.height);
            bounds = new Rectangle(loc.x - tr.x, loc.y - tr.y, ca.width, ca.height);
          }
          break;

        case CENTER_DISPLAY:
          bounds = display.getBounds();
          break;

        case CENTER_DISPLAY_CLIENT_AREA:
          bounds = display.getClientArea();
          break;
      }

      if(bounds != null)
      {
        Rectangle rect = shell.getBounds();
        int x = bounds.x + (bounds.width - rect.width) / 2;
        int y = bounds.y + (bounds.height - rect.height) / 2;
        shell.setLocation(x, y);
      }
    }

    if(windowStateModel != null)
    {
      /* [TODO] Pluralized -> Minimized and Minimized -> Pluralized do not trigger an event.
       * These transitions need to be recognized. If everything else fails, check the state when
       * the shell is being closed. */
      shell.addControlListener(new ControlAdapter()
      {
        public void controlMoved(ControlEvent e)
        {
          int state = windowStateModel.getState();
          if(shell.getMaximized()) state = IWindowStateModel.STATE_MAXIMIZED;
          else if(shell.getMinimized())
          {
            if(state == IWindowStateModel.STATE_MAXIMIZED)
              state = IWindowStateModel.STATE_MINIMIZED_AFTER_MAXIMIZED;
            else if(state == IWindowStateModel.STATE_PLURALIZED || state == IWindowStateModel.STATE_DEFAULTBOUNDS) // [TODO] support creating maximized shells
              state = IWindowStateModel.STATE_MINIMIZED_AFTER_PLURALIZED;
          }
          else
          {
            state = IWindowStateModel.STATE_PLURALIZED;
            windowStateModel.setPluralizedBounds(shell.getBounds());
          }
          windowStateModel.setState(state);
        }
  
        public void controlResized(ControlEvent e)
        {
          int state = windowStateModel.getState();
          if(shell.getMaximized()) state = IWindowStateModel.STATE_MAXIMIZED;
          else if(shell.getMinimized())
          {
            if(state == IWindowStateModel.STATE_MAXIMIZED)
              state = IWindowStateModel.STATE_MINIMIZED_AFTER_MAXIMIZED;
            else if(state == IWindowStateModel.STATE_PLURALIZED)
              state = IWindowStateModel.STATE_MINIMIZED_AFTER_PLURALIZED;
          }
          else
          {
            state = IWindowStateModel.STATE_PLURALIZED;
            windowStateModel.setPluralizedBounds(shell.getBounds());
          }
          windowStateModel.setState(state);
        }
      });
  
      final IChangeListener cl = new IChangeListener()
      {
        public void stateChanged(ChangeEvent e)
        {
          Rectangle rect = windowStateModel.getPluralizedBounds();
          int state = windowStateModel.getState();
          switch(state)
          {
            case IWindowStateModel.STATE_PLURALIZED:
              if(shell.getMaximized()) shell.setMaximized(false);
              if(shell.getMinimized()) shell.setMinimized(false);
              if(!shell.getBounds().equals(rect)) shell.setBounds(rect);
              break;
            case IWindowStateModel.STATE_MAXIMIZED:
              if(!shell.getMaximized()) shell.setMaximized(true);
              break;
            case IWindowStateModel.STATE_MINIMIZED_AFTER_MAXIMIZED:
            case IWindowStateModel.STATE_MINIMIZED_AFTER_PLURALIZED:
              if(!shell.getMinimized()) shell.setMinimized(true);
              break;
            case IWindowStateModel.STATE_DEFAULTBOUNDS:
              // do nothing
              break;
          }
        }
      };
      SWTUtil.registerModel(shell, windowStateModel, cl);
      
      if(windowStateModel.getState() == IWindowStateModel.STATE_DEFAULTBOUNDS)
      {
        windowStateModel.setPluralizedBounds(shell.getBounds()); // [TODO] support creating maximized shells
        if(shell.getMinimized()) windowStateModel.setState(IWindowStateModel.STATE_MINIMIZED_AFTER_PLURALIZED);
        else if(shell.getMaximized()) windowStateModel.setState(IWindowStateModel.STATE_MAXIMIZED);
        else windowStateModel.setState(IWindowStateModel.STATE_PLURALIZED);
      }
    }

    Map<Integer, PreparedActionListener> keyDownMap = null;
    Map<Integer, PreparedActionListener> mouseDownMap = null;
    Map<Integer, PreparedActionListener> mouseUpMap = null;
    for(String type : getModelTypes())
    {
      if(type.startsWith("KeyDown "))
      {
        String accel = type.substring(8);
        IActionListener actionListener = getModel(type, wi.models, IActionListener.class);
        if(actionListener != null)
        {
          int code = SWTUtil.decodeAccelerator(accel);
          if(code != 0)
          {
            ActionEvent ae = new ActionEvent(this, getModelBinding(type).getAttribute("command"), wi);
            if(keyDownMap == null) keyDownMap = new HashMap<Integer, PreparedActionListener>();
            keyDownMap.put(code, new PreparedActionListener(actionListener, ae));
            if(LOGGER.isDebugEnabled()) LOGGER.debug("Adding global KeyDown listener for "+accel+" (code="+code+")");
          }
        }
      }
      else if(type.startsWith("MouseDown "))
      {
        String button = type.substring(10);
        IActionListener actionListener = getModel(type, wi.models, IActionListener.class);
        if(actionListener != null)
        {
          int code = SWTUtil.decodeMouseButton(button);
          if(code != 0)
          {
            ActionEvent ae = new ActionEvent(this, getModelBinding(type).getAttribute("command"), wi);
            if(mouseDownMap == null) mouseDownMap = new HashMap<Integer, PreparedActionListener>();
            mouseDownMap.put(code, new PreparedActionListener(actionListener, ae));
            if(LOGGER.isDebugEnabled()) LOGGER.debug("Adding global MouseDown listener for "+button+" (code="+code+")");
          }
        }
      }
      else if(type.startsWith("MouseUp "))
      {
        String button = type.substring(8);
        IActionListener actionListener = getModel(type, wi.models, IActionListener.class);
        if(actionListener != null)
        {
          int code = SWTUtil.decodeMouseButton(button);
          if(code != 0)
          {
            ActionEvent ae = new ActionEvent(this, getModelBinding(type).getAttribute("command"), wi);
            if(mouseUpMap == null) mouseUpMap = new HashMap<Integer, PreparedActionListener>();
            mouseUpMap.put(code, new PreparedActionListener(actionListener, ae));
            if(LOGGER.isDebugEnabled()) LOGGER.debug("Adding global MouseUp listener for "+button+" (code="+code+")");
          }
        }
      }
    }
    if(keyDownMap != null)
    {
      final Map<Integer, PreparedActionListener> finalMap = keyDownMap;
      SWTUtil.registerShellEventFilter(shell, SWT.KeyDown, new Listener()
      {
        public void handleEvent(Event event)
        {
          int code = event.stateMask | (event.keyCode != 0 ? event.keyCode : event.character);
          if(LOGGER.isDebugEnabled())
          {
            LOGGER.debug("Event: "+event);
            LOGGER.debug("KeyDown in shell '"+title+"': keyCode="+event.keyCode+", character="+(int)(event.character)+", code="+code+", stateMask="+event.stateMask);
          }
          PreparedActionListener action = finalMap.get(code);
          if(action != null)
          {
            action.performPreparedAction();
            event.doit = false;
          }
        }
      });
    }
    if(mouseDownMap != null)
    {
      final Map<Integer, PreparedActionListener> finalMap = mouseDownMap;
      SWTUtil.registerShellEventFilter(shell, SWT.MouseDown, new Listener()
      {
        public void handleEvent(Event event)
        {
          if(LOGGER.isDebugEnabled()) LOGGER.debug("MouseDown in shell '"+title+"': button="+event.button+" event="+event);
          PreparedActionListener action = finalMap.get(event.button);
          if(action != null)
          {
            action.performPreparedAction();
            event.doit = false;
          }
        }
      });
    }
    if(mouseUpMap != null)
    {
      final Map<Integer, PreparedActionListener> finalMap = mouseUpMap;
      SWTUtil.registerShellEventFilter(shell, SWT.MouseUp, new Listener()
      {
        public void handleEvent(Event event)
        {
          if(LOGGER.isDebugEnabled()) LOGGER.debug("MouseUp in shell '"+title+"': button="+event.button+" event="+event);
          PreparedActionListener action = finalMap.get(event.button);
          if(action != null)
          {
            action.performPreparedAction();
            event.doit = false;
          }
        }
      });
    }

    hookUpTextReadModel(getModel("title", wi.models, DefaultStringModel.PTYPE_READ),
      "title", shell, "setText", "getText", false);

    final IActionListener closeModel = getModel("close", wi.models, IActionListener.class);
    if(closeModel != null)
    {
      final ActionEvent ae = new ActionEvent(this, getModelBinding("close").getAttribute("command"), wi);
      shell.addShellListener(new ShellAdapter()
      {
        public void shellClosed(ShellEvent e)
        {
          closeModel.performAction(ae);
          e.doit = false;
        }
      });
    }
    
    return wi;
  }
}
