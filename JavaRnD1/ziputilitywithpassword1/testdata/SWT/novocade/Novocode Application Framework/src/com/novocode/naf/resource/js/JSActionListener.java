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

package com.novocode.naf.resource.js;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;

import com.novocode.naf.gui.event.ActionEvent;
import com.novocode.naf.gui.event.IActionListener;


/**
 * IActionListener which calls a JavaScript function.
 *
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since May 6, 2008
 * @version $Id: JSActionListener.java 417 2008-05-06 22:27:03Z szeiger $
 */

public class JSActionListener implements IActionListener
{
  private final ScriptableObject scope, thisObj;
  private final BaseFunction func;


  public JSActionListener(BaseFunction func, ScriptableObject scope, ScriptableObject thisObj)
  {
    this.func = func;
    this.scope = scope;
    this.thisObj = thisObj;
  }


  @Override
  public void performAction(ActionEvent e)
  {
    Context cx = Context.enter();
    try
    {
      func.call(cx, scope, thisObj, new Object[] { e });
    }
    finally { cx.exit(); }
  }
}
