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

package com.novocode.naf.app;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;

import com.novocode.naf.gui.*;
import com.novocode.naf.gui.image.*;
import com.novocode.naf.model.ModelMap;
import com.novocode.naf.resource.Resource;


/**
 * Main application class for NAF applications.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 2, 2003
 */

public final class NAFApplication
{
  private final URL baseURL;
  private final GUIResourceManager resourceManager;
  private final ResourceImageManager imageManager;
  private final FontManager fontManager;
  private final Display display;
  //private Map managedWindows = new HashMap();
  private int liveWindows = 0;
  private boolean disposed;


  public NAFApplication(Class<?> baseClass) throws NAFException
  {
    this(baseURLFor(baseClass));
  }


  public NAFApplication(URL baseURL) throws NAFException
  {
    this.baseURL = baseURL;
    this.display = new Display();
    this.resourceManager = new GUIResourceManager();
    this.imageManager = new ResourceImageManager(baseURL, display, true);
    this.fontManager = new FontManager(display, true);
  }


  private static URL baseURLFor(Class<?> baseClass)
  {
    String n = baseClass.getName();
    int sep = n.lastIndexOf('.');
    if(sep != -1) n = n.substring(sep+1);
    return baseClass.getResource(n+".class");
  }


  public synchronized void dispose()
  {
    if(disposed) return;
    display.dispose(); /* implicitly calls imageManager.disposeAll() and fontManager.disposeAll() */
    disposed = true;
  }


  public Display getDisplay() { return display; }


  public Resource getResource(String resName) throws NAFException
  {
    try
    {
      return resourceManager.getResource(new URL(baseURL, resName));
    }
    catch(MalformedURLException ex)
    {
      throw new NAFException("Error creating URL object for resource name \""+resName+"\"", ex);
    }
  }


  public Resource getResource(URL resURL) throws NAFException
  {
    try
    {
      return resourceManager.getResource(new URL(baseURL, resURL.toExternalForm()));
    }
    catch(MalformedURLException ex)
    {
      throw new NAFException("Error creating URL object for resource URL \""+resURL+"\"", ex);
    }
  }


  public Object getResourceObject(String resName) throws NAFException
  {
    try
    {
      return resourceManager.getObject(new URL(baseURL, resName));
    }
    catch(MalformedURLException ex)
    {
      throw new NAFException("Error creating URL object for resource name \""+resName+"\"", ex);
    }
  }


  public Object getResourceObject(URL resURL) throws NAFException
  {
    try
    {
      return resourceManager.getObject(new URL(baseURL, resURL.toExternalForm()));
    }
    catch(MalformedURLException ex)
    {
      throw new NAFException("Error creating URL object for resource URL \""+resURL+"\"", ex);
    }
  }


  @SuppressWarnings("unchecked")
  public <T> T getResourceObject(String resName, Class<T> clazz) throws NAFException
  {
    Object obj = getResourceObject(resName);
    if(obj == null) return null;
    if(!clazz.isInstance(obj))
      throw new NAFException("Resource object "+resName+" of type "+obj.getClass().getName()+" cannot be cast to "+clazz.getName());
    return (T)obj;
  }


  @SuppressWarnings("unchecked")
  public <T> T getResourceObject(URL resURL, Class<T> clazz) throws NAFException
  {
    Object obj = getResourceObject(resURL);
    if(obj == null) return null;
    if(!clazz.isInstance(obj))
      throw new NAFException("Resource object "+resURL+" of type "+obj.getClass().getName()+" cannot be cast to "+clazz.getName());
    return (T)obj;
  }


  public void runMainWindow(IWindowInstanceWidget component, ModelMap models) throws NAFException
  {
    WindowInstance wi = createInstance(component, models);
    wi.open();
    runApp();
  }


  public WindowInstance createInstance(IWindowInstanceWidget component, ModelMap models) throws NAFException
  {
    return createInstance(component, models, null);
  }


  public WindowInstance createInstance(IWindowInstanceWidget component, ModelMap models, WindowInstance parent) throws NAFException
  {
    if(models == null) models = (parent == null) ? new ModelMap() : parent.models;
    WindowInstance wi = component.createWindowInstance(this, models, parent);
    if(wi != null) manageWindow(wi);
    return wi;
  }


  private void manageWindow(final WindowInstance wi)
  {
    Widget disposeSender = wi.getDisposeSender();
    if(disposeSender != null)
    {
      liveWindows++;
      //managedWindows.put(disposeSender, wi);
      disposeSender.addDisposeListener(new DisposeListener()
      {
        public void widgetDisposed(DisposeEvent e)
        {
          liveWindows--;
          //managedWindows.remove(wi.shell);
        }
      });
    }
  }


  public void runApp()
  {
    while(liveWindows > 0)
    {
      if(!display.readAndDispatch()) display.sleep();
    }
  }


  public GUIResourceManager getResourceManager() { return resourceManager; }


  public ResourceImageManager getImageManager() { return imageManager; }


  public FontManager getFontManager() { return fontManager; } 
}
