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

package com.novocode.naf.gui.event;

import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Display;


/**
 * An adapter for change listeners which reschedules events to the
 * SWT event handling thread if they don't already occur on this
 * thread.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Jan 3, 2004
 */

public final class SWTChangeAdapter implements IChangeListener
{
  private IChangeListener parent;
  private Display display;
  private Thread swtThread;
  
  
  public SWTChangeAdapter(IChangeListener parent, Display display)
  {
    this.parent = parent;
    this.display = display;
    this.swtThread = display.getThread();
  }


  public void stateChanged(final ChangeEvent e)
  {
    try
    {
      if(Thread.currentThread() == swtThread) parent.stateChanged(e);
      else
      {
        display.asyncExec(new Runnable()
        {
          public void run()
          {
            try { parent.stateChanged(e); } catch(SWTException ignored) {}
          }
        });
      }
    }
    catch(SWTException ignored) {}
  }
}
