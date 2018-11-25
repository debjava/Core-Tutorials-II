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

package com.novocode.naf.jface.viewers;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * An ITreeContentProvider which delegates getElements(), getChildren()
 * and hasChildren() method calls to another ITreeContentProvider in a
 * background thread to provide timely feedback for long-running queries.
 *
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 8, 2004
 * @version $Id: BackgroundTreeContentProvider.java 371 2008-03-26 22:40:15Z szeiger $
 */

public final class BackgroundTreeContentProvider implements ITreeContentProvider
{
  private static final Logger LOGGER = LoggerFactory.getLogger(BackgroundTreeContentProvider.class);

  private static final int DEFAULT_TIME_GET = 100;
  private static final int DEFAULT_TIME_HAS =  10;

  private static final int METHOD_GETCHILDREN = 0;
  private static final int METHOD_GETELEMENTS = 1;
  private static final int METHOD_HASCHILDREN = 2;

  private final ITreeContentProvider parent;
  private final ArrayList<PendingGet> pendingGetList = new ArrayList<PendingGet>();
  private final ArrayList<PendingHas> pendingHasList = new ArrayList<PendingHas>();

  private volatile TreeViewer treeViewer;
  private volatile Display display;
  private volatile int timeGet = DEFAULT_TIME_GET;
  private volatile int timeHas = DEFAULT_TIME_HAS;
  private volatile ArrayList<BackgroundTreeContentListener> listeners;
  private volatile boolean disposed;


  private static abstract class Pending
  {
    abstract void updateResult(Object value);
  }


  private final class PendingGet extends Pending
  {
    final Object parent;
    volatile Object[] result = new Object[] { this };

    PendingGet(Object parent, DelegateThread r)
    {
      this.parent = parent;
      r.pending = this;
      pendingGetList.add(this);
    }

    void updateResult(Object value)
    {
      // [TODO] Start background hasChildren() operations on all children and cache their results
      result = (Object[])value;
      treeViewer.refresh(parent);
      removePending(this, pendingGetList);
      if(listeners != null)
        for(BackgroundTreeContentListener l : listeners) l.pendingGetFinished(parent);
    }
  }


  private final class PendingHas extends Pending
  {
    final Object element;
    volatile boolean result = true;

    PendingHas(Object element, DelegateThread r)
    {
      this.element = element;
      r.pending = this;
      pendingHasList.add(this);
    }

    void updateResult(Object value)
    {
      if(value != Boolean.TRUE)
      {
        result = false;
        treeViewer.refresh(element);
      }
      removePending(this, pendingHasList);
      if(listeners != null)
        for(BackgroundTreeContentListener l : listeners) l.pendingHasFinished(element);
    }
  }


  private final class DelegateThread extends Thread
  {
    final int method;
    final Object arg;
    volatile Object res;
    volatile RuntimeException ex;
    volatile boolean done;
    volatile Pending pending;

    DelegateThread(int method, Object arg)
    {
      this.method = method;
      this.arg = arg;
      setDaemon(true);
      try { setPriority(getPriority()+1); } catch(Throwable ignored) {}
    }

    public void run()
    {
      long t0 = System.currentTimeMillis();
      try
      {
        switch(method)
        {
          case METHOD_GETCHILDREN: res = parent.getChildren(arg); break;
          case METHOD_GETELEMENTS: res = parent.getElements(arg); break;
          case METHOD_HASCHILDREN: res = parent.hasChildren(arg) ? Boolean.TRUE : Boolean.FALSE; break;
        }
      }
      catch(RuntimeException ex) { DelegateThread.this.ex = ex; }
      if(LOGGER.isDebugEnabled())
      {
        long t1 = System.currentTimeMillis();
        LOGGER.debug("Method "+method+" took "+(t1-t0)+"ms");
      }

      boolean processPending;
      synchronized(DelegateThread.this)
      {
        done = true;
        DelegateThread.this.notify();
        processPending = (pending != null);
      }

      if(processPending)
      {
        display.asyncExec(new Runnable()
        {
          public void run()
          {
            if(disposed || treeViewer.getTree().isDisposed()) return;
            LOGGER.debug("Processing PENDING state");
            pending.updateResult(res);
            LOGGER.debug("PENDING state processed");
          }
        });
      }
    }
  }


  private Object getChildrenOrElements(int method, Object element)
  {
    if(element instanceof PendingGet) return null;
    for(PendingGet p : pendingGetList)
    {
      if(p.parent == element) return p.result;
    }
    DelegateThread t = new DelegateThread(method, element);
    t.start();
    try
    {
      synchronized(t)
      {
        long t0 = System.currentTimeMillis();
        t.wait(timeGet);
        if(LOGGER.isDebugEnabled())
        {
          long t1 = System.currentTimeMillis();
          LOGGER.debug("waited "+(t1-t0)+"ms for GET");
        }
        if(!t.done)
        {
          if(listeners != null)
            for(BackgroundTreeContentListener l : listeners) l.pendingGet(element);
          LOGGER.debug("FEEDBACK_TIME has passed");
          return new PendingGet(element, t).result;
        }
      }
      if(t.ex != null) throw t.ex;
      else return t.res;
    }
    catch(InterruptedException ex)
    {
      throw new RuntimeException("InterruptedException while waiting for DelegateThread", ex);
    }
  }


  public BackgroundTreeContentProvider(ITreeContentProvider parent, TreeViewer treeViewer)
  {
    this.parent = parent;
    setTreeViewer(treeViewer);
  }


  public BackgroundTreeContentProvider(ITreeContentProvider parent)
  {
    this.parent = parent;
  }


  public Object[] getChildren(Object element)
  {
    LOGGER.debug("getChildren() called for {}", element);
    return (Object[])getChildrenOrElements(METHOD_GETCHILDREN, element);
  }


  public Object[] getElements(Object element)
  {
    LOGGER.debug("getElements() called for {}", element);
    return (Object[])getChildrenOrElements(METHOD_GETELEMENTS, element);
  }


  public Object getParent(Object element)
  {
    LOGGER.debug("getParent() called for {}", element);
    if(element instanceof PendingGet) return ((PendingGet)element).parent;
    return parent.getParent(element);
  }


  public boolean hasChildren(Object element)
  {
    LOGGER.debug("hasChildren() called for {}", element);
    if(element instanceof PendingGet) return false;
    for(PendingHas p : pendingHasList)
    {
      if(p.element == element) return p.result;
    }
    for(PendingGet p : pendingGetList)
    {
      if(p.parent == element) return p.result.length > 0;
    }
    DelegateThread t = new DelegateThread(METHOD_HASCHILDREN, element);
    t.start();
    try
    {
      synchronized(t)
      {
        t.wait(timeHas);
        if(!t.done)
        {
          if(listeners != null)
            for(BackgroundTreeContentListener l : listeners) l.pendingHas(element);
          LOGGER.debug("HASCHILDREN_TIME has passed for element {}", element);
          return new PendingHas(element, t).result;
        }
      }
      if(t.ex != null) throw t.ex;
      else return t.res == Boolean.TRUE;
    }
    catch(InterruptedException ex)
    {
      throw new RuntimeException("InterruptedException while waiting for DelegateThread", ex);
    }
  }


  public void dispose()
  {
    disposed = true;
    parent.dispose();
  }


  public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
  {
    LOGGER.debug("inputChanged from {} to {}", oldInput, newInput);
    parent.inputChanged(viewer, oldInput, newInput);
  }


  public static final boolean isPending(Object element)
  {
    return element instanceof PendingGet;
  }


  private static <T extends Pending> void removePending(Pending pending, Iterable<T> pendingList)
  {
    for(Iterator<T> it = pendingList.iterator(); it.hasNext();)
    {
      if(it.next() == pending) { it.remove(); return; }
    }
  }
  
  
  public void addBackgroundTreeContentListener(BackgroundTreeContentListener l)
  {
    if(l == null) return;
    if(listeners == null) listeners = new ArrayList<BackgroundTreeContentListener>();
    listeners.add(l);
  }
  
  
  public void removeBackgroundTreeContentListener(BackgroundTreeContentListener l)
  {
    if(listeners == null || l == null) return;
    for(Iterator<BackgroundTreeContentListener> it = listeners.iterator(); it.hasNext();)
    {
      if(it.next() == l) { it.remove(); return; }
    }
  }
  
  
  public void setTimeGet(int millis) { timeGet = millis; }
  
  
  public void setTimeHas(int millis) { timeHas = millis; }
  
  
  public void setTreeViewer(TreeViewer tv)
  {
    treeViewer = tv;
    display = tv.getTree().getDisplay();
  }
}
