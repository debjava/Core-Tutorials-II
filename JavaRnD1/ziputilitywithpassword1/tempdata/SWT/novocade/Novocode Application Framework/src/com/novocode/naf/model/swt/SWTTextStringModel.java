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

package com.novocode.naf.model.swt;

import org.eclipse.swt.events.*;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Text;

import com.novocode.naf.gui.event.StringChangeEvent;
import com.novocode.naf.model.*;


/**
 * SWT Text control based implementation of the StringModel interface.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Feb 19, 2004
 */

public final class SWTTextStringModel extends DefaultModel implements IObjectModel<String>
{
  private Text control;
  private Display display;
  private Thread swtThread;


  public SWTTextStringModel()
  {
  }


  public void setControl(final Text control)
  {
    this.control = control;
    this.display = control.getDisplay();
    this.swtThread = display.getThread();

    control.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        notifyListeners();
      }
    });
  }
  
  
  public Text getControl() { return control; }

  
  public synchronized void append(final String s)
  {
    if(Thread.currentThread() == swtThread) control.append(s);
    else display.syncExec(new Runnable()
    {
      public void run() { control.append(s); }
    });
    notifyListeners(new StringChangeEvent(this, StringChangeEvent.ADD, s));
  }


  public String getValue()
  {
    return control.getText();
  }


  public synchronized void setValue(final String s)
  {
    if(Thread.currentThread() == swtThread) control.setText(s);
    else display.syncExec(new Runnable()
    {
      public void run() { control.setText(s); }
    });
    notifyListeners();
  }
}
