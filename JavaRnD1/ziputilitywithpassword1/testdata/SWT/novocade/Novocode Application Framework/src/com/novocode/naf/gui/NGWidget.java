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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

import com.novocode.naf.app.NAFException;
import com.novocode.naf.color.*;
import com.novocode.naf.data.DataDecoder;
import com.novocode.naf.gui.event.*;
import com.novocode.naf.model.*;
import com.novocode.naf.resource.*;


/**
 * Base class for all NAF widgets.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Feb 20, 2004
 * @version $Id: NGWidget.java 412 2008-05-04 18:27:58Z szeiger $
 */

public abstract class NGWidget extends NGModelComponent
{
  private static final String WIDGETDATA_NAME = WidgetData.class.getName();

  private String tooltip, font;
  private ColorData fgcolor, bgcolor;
  private boolean focus;
  private Map<String, String> layoutDataAttributes;


  protected static class WidgetData
  {
    private NGWidget ngw;
    protected Color givenFG, givenBG;

    protected WidgetData(NGWidget ngw) { this.ngw = ngw; }

    protected NGWidget getNGWidget() { return ngw; }

    protected static final WidgetData forWidget(Widget w)
    {
      if(w == null) return null;
      else return (WidgetData)w.getData(WIDGETDATA_NAME);
    }

    protected void attachTo(Widget w) { w.setData(WIDGETDATA_NAME, this); }
  }


  /**
   * Set this widget's tooltip.
   * 
   * Note that not all widgets will actually display tooltips.
   * 
   * @param tooltip The tooltip to set, or <em>null</em> for no tooltip.
   */
  @ConfProperty
  public final void setTooltip(String tooltip) { this.tooltip = tooltip; }
  /** Return this widget's tooltip */
  public final String getTooltip() { return tooltip; }


  /** Set this widget's background color */
  @ConfProperty("bgcolor")
  public final void setBackgroundColor(ColorData data) { bgcolor = data; }
  /** Return this widget's background color */
  public final ColorData getBackgroundColor() { return bgcolor; }


  /** Set this widget's foreground color */
  @ConfProperty("fgcolor")
  public final void setForegroundColor(ColorData data) { fgcolor = data; }
  /** Return this widget's foreground color */
  public final ColorData getForegroundColor() { return fgcolor; }


  /**
   * Set the focus flag.
   * 
   * @param b true if this widget should request focus when it is shown.
   */
  @ConfProperty
  public final void setFocus(boolean b) { focus = b; }
  /** Return true if this widget should request focus when it is shown */
  public final boolean getFocus() { return focus; }


  /**
   * Set this widget's font.
   * 
   * Note that not all widgets actually use a font. The font can still be
   * inherited by children though.
   */
  @ConfProperty
  public final void setFont(String string)
  {
    font = string;
    if(font == null) return;
    font = font.trim();
    if(font.length() == 0) font = null;
  }
  /**
   * Return this widget's font.
   */
  public final String getFont() { return font; }


  /** Set the layout data attributes */
  @ConfProperty(value = "layout-data", prefix = "ld")
  public final void setLayoutDataAttributes(Map<String, String> p) { layoutDataAttributes = p; }
  /** Return the layout data attributes */
  public final Map<String, String> getLayoutDataAttributes() { return layoutDataAttributes; }


  protected final void addCommonMenuFeatures(MenuItem i, WindowInstance wi, String label,
    String accelerator, String imageResource) throws NAFException
  {
    // Set accelerator and compute label
    int acc = SWTUtil.decodeAccelerator(accelerator);
    if(acc != 0)
    {
      i.setAccelerator(acc);
      label += '\t' + accelerator;
    }
    
    SWTUtil.setRegisteredImage(imageResource, getResource().getURL(), i, wi.imageManager, true);
    i.setText(label);
  }


  protected final void addCommonFeatures(Widget w, WindowInstance wi, NGWidget parentNGC) throws NAFException
  {
    if(tooltip != null && tooltip.length() > 0) SWTUtil.setTooltip(w, tooltip);

    addModels(w, wi, parentNGC);
    gatherPersistModels(wi);
  }


  private void gatherPersistModels(WindowInstance wi)
  {
    Map<String, ModelBinding> m = getModelBindingsMap();
    if(m != null)
      for(ModelBinding mb : m.values())
        if(mb.isPersist()) wi.addPersistModel(mb.getID());
  }


  protected WidgetData createWidgetData()
  {
    return new WidgetData(this);
  }


  protected void configureControl(final Control control, final ShellWindowInstance wi, WidgetData wd, WidgetData pwd)
  {
    if(fgcolor == null || fgcolor.getType() == ColorData.TYPE_INHERIT)
    {
      if(pwd != null && pwd.givenFG != null)
      {
        control.setForeground(pwd.givenFG);
        wd.givenFG = pwd.givenFG;
      }
    }
    else
    {
      SWTUtil.setRegisteredForeground(control, fgcolor);
      if(fgcolor.getType() != ColorData.TYPE_DEFAULT) wd.givenFG = control.getForeground();
    }

    if(bgcolor == null || bgcolor.getType() == ColorData.TYPE_INHERIT)
    {
      if(pwd != null && pwd.givenBG != null)
      {
        control.setBackground(pwd.givenBG);
        wd.givenBG = pwd.givenBG;
      }
    }
    else
    {
      SWTUtil.setRegisteredBackground(control, bgcolor);
      if(bgcolor.getType() != ColorData.TYPE_DEFAULT) wd.givenBG = control.getBackground();
    }

    addPopupMenu(control, wi);
    if(font != null)
    {
      FontData[] base = control.getFont().getFontData();
      FontData[] fd = FontManager.decodeFontSpec(base, font);
      SWTUtil.setRegisteredFont(fd, control, wi.fontManager);
    }
    if(focus) control.setFocus();
  }


  protected final void addDefaultChildren(final Composite composite, final ShellWindowInstance wi, final WidgetData wd)
  {
    final IChangeListener cl = new IChangeListener()
    {
      public void stateChanged(ChangeEvent e)
      {
        try
        {
          composite.setRedraw(false);
          // [TODO] React to ItemListModel for partial updates
          for(Control c : composite.getChildren()) c.dispose();
          addDefaultChildrenStatic(composite, wi, wd);
          composite.layout(true);
        }
        finally { composite.setRedraw(true); }
      }
    };

    for(NGComponent c : getExpandedStaticRoleChildren(null, wi.models))
    {
      if(!(c instanceof NGGroup)) continue;
      IItemListModel contentModel = ((NGGroup)c).getContentModel(wi.models);
      if(contentModel != null) SWTUtil.registerModel(composite, contentModel, cl);
    }

    addDefaultChildrenStatic(composite, wi, wd);
  }


  protected Map<String, Control> addDefaultChildrenStatic(Composite composite, ShellWindowInstance wi, WidgetData wd)
  {
    HashMap<String, Control> chmap = new HashMap<String, Control>();
    for(NGComponent ch : getExpandedRoleChildren(null, wi.models))
    {
      Control chctrl = ((NGWidget)ch).createAndConfigureControl(composite, this, wi);
      if(ch.getID() != null) chmap.put(ch.getID(), chctrl);
    }
    return chmap;
  }


  protected Control createControl(Composite parent, NGComponent parentComp, ShellWindowInstance wi, WidgetData pwd) throws NAFException
  {
    throw new NAFException("Cannot create a <"+getElementName()+"> control with a "+parent.getClass().getName()+" parent");
  }


  public final Control createAndConfigureControl(Composite parent, NGComponent parentComp, ShellWindowInstance wi) throws NAFException
  {
    WidgetData pwd = WidgetData.forWidget(parent);
    Control c = createControl(parent, parentComp, wi, pwd);
    WidgetData wd = createWidgetData();
    wd.attachTo(c);
    addCommonFeatures(c, wi, (parentComp instanceof NGWidget ? (NGWidget)parentComp : null));
    configureControl(c, wi, wd, pwd);
    return c;
  }


  protected final void addModels(final Widget widget, final WindowInstance wi, final NGWidget parentNGC)
  {
    // [TODO] Make addModels() private and ensure it is called exactly once for every widget
    final IBooleanReadModel disabledModel = getModel("disabled", wi.models, IBooleanReadModel.class);
    final IBooleanReadModel enabledModel = getModel("enabled", wi.models, IBooleanReadModel.class);
    if(disabledModel != null && enabledModel != null)
      throw new NAFException("Models \"disabled\" and \"enabled\" may not be used together");
    if(disabledModel != null)
    {
      IChangeListener cl = new IChangeListener()
      {
        public void stateChanged(ChangeEvent e)
        {
          SWTUtil.setEnabledIfChanged(widget, !disabledModel.getBoolean());
        }
      };
      SWTUtil.registerModel(widget, disabledModel, cl);
      cl.stateChanged(null);
    }
    if(enabledModel != null)
    {
      IChangeListener cl = new IChangeListener()
      {
        public void stateChanged(ChangeEvent e)
        {
          SWTUtil.setEnabledIfChanged(widget, enabledModel.getBoolean());
        }
      };
      SWTUtil.registerModel(widget, enabledModel, cl);
      cl.stateChanged(null);
    }

    final IBooleanReadModel visibleModel = getModel("visible", wi.models, IBooleanReadModel.class);
    if(visibleModel != null && widget instanceof Control)
    {
      final Control ctrl = (Control)widget;
      IChangeListener cl = new IChangeListener()
      {
        public void stateChanged(ChangeEvent e)
        {
          boolean b = visibleModel.getBoolean();
          if(ctrl.getVisible() != b)
          {
            if(parentNGC != null) parentNGC.setChildControlVisibility(ctrl, b, wi);
            else ctrl.setVisible(b);
          }
        }
      };
      SWTUtil.registerModel(widget, visibleModel, cl);
      cl.stateChanged(null);
    }

    final IObjectReadModel<String> tooltipModel = getModel("tooltip", wi.models, DefaultStringModel.PTYPE_READ);
    if(tooltipModel != null && (widget instanceof Control || widget instanceof ToolItem))
    {
      IChangeListener cl = new IChangeListener()
      {
        public void stateChanged(ChangeEvent e)
        {
          String s = tooltipModel.getValue();
          if(s == null) s = "";
          if(!s.equals(SWTUtil.getTooltip(widget))) SWTUtil.setTooltip(widget, s);
        }
      };
      SWTUtil.registerModel(widget, tooltipModel, cl);
      cl.stateChanged(null);
    }
  }


  protected void setChildControlVisibility(Control ctrl, boolean visible, WindowInstance wi)
  {
    ctrl.setVisible(visible);
  }


  protected final void registerContentModels(Widget widget, ModelMap models, final boolean[] dirty)
  {
    IChangeListener cl = new IChangeListener()
    {
      public void stateChanged(ChangeEvent e) { dirty[0] = true; }
    };
    for(NGComponent c : getExpandedStaticRoleChildren(null, models))
    {
      if(!(c instanceof NGGroup)) continue;
      IItemListModel contentModel = ((NGGroup)c).getContentModel(models);
      if(contentModel != null) SWTUtil.registerModel(widget, contentModel, cl);
    }
  }


  private void addPopupMenu(Control ctrl, WindowInstance wi) throws NAFException
  {
    NGComponent popup = getFirstExpandedRoleChild("popup", wi.models);
    if(popup == null) return;
    if(!(popup instanceof NGMenu))
      throw new NAFException("Only NGMenu objects can be assigned to role \"popup\"");
    ctrl.setMenu(((NGMenu)popup).createMenu(wi, SWT.POP_UP));
  }


  protected final void hookUpIIntModel(final IIntModel mdl, final Control control, String getterName, String setterName)
  {
    if(mdl == null) return;
    Class<?> clazz = control.getClass();
    final Method setter = getMethodOrNull(clazz, setterName, Integer.TYPE);
    final Method getter = getMethodOrNull(clazz, getterName);
    final Method addSelectionListener = getMethodOrNull(clazz, "addSelectionListener", SelectionListener.class);
    final boolean[] lock = new boolean[1];
    if(setter != null)
    {
      IChangeListener cl = new IChangeListener()
      {
        public void stateChanged(ChangeEvent e)
        {
          try
          {
            int i = mdl.getInt();
            if(getter != null && i == (Integer)getter.invoke(control)) return;
            try
            {
              lock[0] = true;
              setter.invoke(control, i);
            }
            finally { lock[0] = false; }
          }
          catch(IllegalAccessException ex) { throw new NAFException(ex); }
          catch(InvocationTargetException ex) { throw new NAFException(ex); }
        }
      };
      SWTUtil.registerModel(control, mdl, cl);
      cl.stateChanged(null);
    }

    try
    {
      if(addSelectionListener != null && getter != null) addSelectionListener.invoke(control, new SelectionAdapter()
      {
        public void widgetSelected(SelectionEvent e)
        {
          if(lock[0]) return;
          try { mdl.setInt((Integer)getter.invoke(control)); }
          catch(IllegalAccessException ex) { throw new NAFException(ex); }
          catch(InvocationTargetException ex) { throw new NAFException(ex); }
        }
      });
    }
    catch(IllegalAccessException ex) { throw new NAFException(ex); }
    catch(InvocationTargetException ex) { throw new NAFException(ex); }
  }


  private static Method getMethodOrNull(Class<?> cl, String name, Class<?>... parameterTypes)
  {
    if(name == null) return null;
    try { return cl.getMethod(name, parameterTypes); }
    catch(NoSuchMethodException ex) { return null; }
  }


  @SuppressWarnings("unchecked")
  protected final void hookUpReadModel(final Class<?> paramType, final IModel mdl,
    final Widget widget, String setterName, String getterName)
  {
    if(mdl == null) return;
    Class<?> clazz = widget.getClass();
    final Method setter = getMethodOrNull(clazz, setterName, paramType);
    if(setter == null)
      throw new NAFException("Setter "+setterName+"("+paramType.getName()+") not found in class "+clazz.getName());
    final Method getter = getMethodOrNull(clazz, getterName);
    IChangeListener cl;
    if(mdl instanceof IIntReadModel)
    {
      final IIntReadModel imdl = (IIntReadModel)mdl;
      cl = new IChangeListener()
      {
        public void stateChanged(ChangeEvent e)
        {
          try
          {
            int i = imdl.getInt();
            if(getter != null)
            {
              int old = (Integer)getter.invoke(widget);
              if(i == old) return;
            }
            setter.invoke(widget, i);
          }
          catch(IllegalAccessException ex) { throw new NAFException(ex); }
          catch(InvocationTargetException ex) { throw new NAFException(ex); }
        }
      };
    }
    else if(mdl instanceof IBooleanReadModel)
    {
      final IBooleanReadModel bmdl = (IBooleanReadModel)mdl;
      cl = new IChangeListener()
      {
        public void stateChanged(ChangeEvent e)
        {
          try
          {
            boolean b = bmdl.getBoolean();
            if(getter != null)
            {
              boolean old = (Boolean)getter.invoke(widget);
              if(b == old) return;
            }
            setter.invoke(widget, b);
          }
          catch(IllegalAccessException ex) { throw new NAFException(ex); }
          catch(InvocationTargetException ex) { throw new NAFException(ex); }
        }
      };
    }
    else
    {
      final IObjectReadModel<?> omdl = (IObjectReadModel<?>)mdl;
      cl = new IChangeListener()
      {
        public void stateChanged(ChangeEvent e)
        {
          try
          {
            Object o = omdl.getValue();
            if(getter != null)
            {
              Object old = getter.invoke(widget);
              if(o == null && old == null) return;
              if(o != null && o.equals(old)) return;
            }
            setter.invoke(widget, o);
          }
          catch(IllegalAccessException ex) { throw new NAFException(ex); }
          catch(InvocationTargetException ex) { throw new NAFException(ex); }
        }
      };
    }

    SWTUtil.registerModel(widget, mdl, cl);
    cl.stateChanged(null);
  }


  @SuppressWarnings("unchecked")
  protected final void hookUpTextReadModel(final IObjectReadModel<?> mdl, String modelType,
    final Widget widget, String setterName, String getterName, final boolean decodeAccessKey)
  {
    if(mdl == null) return;
    ModelBinding mb = getModelBinding(modelType);
    Class<?> clazz = widget.getClass();
    final Method setter = getMethodOrNull(clazz, setterName, String.class);
    if(setter == null)
      throw new NAFException("Setter "+setterName+"(java.lang.String) not found in class "+clazz.getName());
    final Method getter = getMethodOrNull(clazz, getterName);
    IChangeListener cl;
    final String prefix = mb.getAttribute("prefix");
    final String suffix = mb.getAttribute("suffix");
    cl = new IChangeListener()
    {
      public void stateChanged(ChangeEvent e)
      {
        try
        {
          Object val = mdl.getValue();
          String s = val == null ? null : val.toString();
          if(s == null) s = "";
          if(prefix != null) s = prefix + s;
          if(suffix != null) s = s + suffix;
          if(decodeAccessKey) s = DataDecoder.decodeAccessKey(s);
          if(getter != null)
          {
            Object old = getter.invoke(widget);
            if(s.equals(old)) return;
          }
          setter.invoke(widget, s);
        }
        catch(IllegalAccessException ex) { throw new NAFException(ex); }
        catch(InvocationTargetException ex) { throw new NAFException(ex); }
      }
    };

    SWTUtil.registerModel(widget, mdl, cl);
    cl.stateChanged(null);
  }
}
