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
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.*;

import com.novocode.naf.app.*;
import com.novocode.naf.data.DataDecoder;
import com.novocode.naf.gui.event.*;
import com.novocode.naf.resource.*;


/**
 * An SWT Link.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Mar 19, 2005
 */

public final class NGLink extends NGTextContainer
{
  public static final class A implements ConfigurableObject
  {
    private String text, href;

    @ConfProperty(":text")
    public String[] getTextContent() { return new String[] { text }; }
    public void addTextContent(String s) { text = (text == null) ? s : (text + s); }

    @ConfProperty
    public void setHref(String s) { href = s; }
  }


  @ConfProperty(value = "a")
  public A[] getAs() { return null; }
  public void addA(A a)
  {
    if(childTokens == null) childTokens = new LinkedList<String>();
    String s = a.text == null ? "" : a.text;
    if(a.href != null) childTokens.add("<a href=\""+a.href+"\">"+s+"</a>");
    else childTokens.add("<a>"+s+"</a>");
  }


  public Control createControl(Composite parent, NGComponent parentComp, final ShellWindowInstance wi, WidgetData pwd) throws NAFException
  {
    Link link = new Link(parent, SWT.NONE);

    String s = getCachedText();
    if(s != null) link.setText(DataDecoder.decodeAccessKey(s));
    
    final IActionListener actionModel = getModel("action", wi.models, IActionListener.class);
    if(actionModel != null)
    {
      link.addListener(SWT.Selection, new Listener()
      {
        public void handleEvent(Event event)
        {
          actionModel.performAction(new ActionEvent(NGLink.this, event.text, wi));
        }
      });
    }
    else
    {
      link.addListener(SWT.Selection, new Listener()
      {
        public void handleEvent(Event event)
        {
          Program.launch(event.text);
        }
      });
    }

    return link;
  }
}
