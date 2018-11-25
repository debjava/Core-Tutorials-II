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

package com.novocode.naf.model.test;

import com.novocode.naf.gui.event.ChangeEvent;
import com.novocode.naf.gui.event.IChangeListener;
import com.novocode.naf.model.DefaultModel;



public class BroadcastPerformance
{
  private static final class Mdl extends DefaultModel
  {
    void dispatch() { notifyListeners(); }
  }

  private static final int NUM = 15000000;

  public static void main(String[] args)
  {
    test(false);
    test(true);
  }

  static void test(boolean log)
  {
    Mdl m = new Mdl();
    long t0, t1;

    for(int ln=0; ln<4; ln++)
    {
      t0 = System.currentTimeMillis();
      for(int i=NUM; i>= 0; i--) m.dispatch();
      t1 = System.currentTimeMillis();
      if(log) System.out.println(ln+" listeners: "+(((double)(t1-t0))/NUM)*1000000.0+"ns");
      m.addChangeListener(new IChangeListener() { public void stateChanged(ChangeEvent e) {} });
    }
  }
}
