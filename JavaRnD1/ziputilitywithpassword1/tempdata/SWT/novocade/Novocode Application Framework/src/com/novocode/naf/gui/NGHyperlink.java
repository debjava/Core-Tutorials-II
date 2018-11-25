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

import org.eclipse.swt.SWT;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.*;

import com.novocode.naf.app.*;
import com.novocode.naf.data.Alignment;
import com.novocode.naf.data.DataDecoder;
import com.novocode.naf.gui.event.*;
import com.novocode.naf.resource.*;
import com.novocode.naf.swt.custom.Hyperlink;


/**
 * A hyperlink label.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Mar 1, 2004
 */

public final class NGHyperlink extends NGTextContainer
{
  private DisplayStyle displayStyle = DisplayStyle.DEFAULT;
  private Alignment alignment = Alignment.LEFT;
  private String href;
  private boolean acceptFocus;

  public enum DisplayStyle { DEFAULT, MOUSEOVER, QUIET }


  @ConfProperty("href")
  public void setHRef(String s) { this.href = s; }
  public String getHRef() { return href; }


  @ConfProperty
  public void setAcceptFocus(boolean b) { this.acceptFocus = b; }
  public boolean getAcceptFocus() { return acceptFocus; }


  @ConfProperty("align")
  public void setAlignment(Alignment a) { this.alignment = a; }
  public Alignment getAlignment() { return alignment; }


  @ConfProperty
  public void setDisplayStyle(DisplayStyle d) { this.displayStyle = d; }
  public DisplayStyle getDisplayStyle() { return displayStyle; }


  public Control createControl(Composite parent, NGComponent parentComp, ShellWindowInstance wi, WidgetData pwd) throws NAFException
  {
    int swtstyle = alignment.style;
    if(!acceptFocus) swtstyle |= SWT.NO_FOCUS;
    Hyperlink hl = new Hyperlink(parent, swtstyle);

    String s = getCachedText();
    if(s != null) hl.setText(DataDecoder.decodeAccessKey(DataDecoder.decodeBackslashEscapes(s)));
    final String usedHRef = (href == null) ? s : href;
    
    final IActionListener actionModel = getModel("action", wi.models, IActionListener.class);
    if(actionModel != null)
    {
      String cmd = getModelBinding("action").getAttribute("command");
      if(cmd == null) cmd = usedHRef;
      final ActionEvent ae = new ActionEvent(this, cmd, wi);

      hl.addListener(SWT.Selection, new Listener()
      {
        public void handleEvent(Event event)
        {
          actionModel.performAction(ae);
        }
      });
    }
    else
    {
      hl.addListener(SWT.Selection, new Listener()
      {
        public void handleEvent(Event event)
        {
          Program.launch(usedHRef);
        }
      });
    }

    switch(displayStyle)
    {
      case QUIET:
        hl.setHoverUnderline(null);
        hl.setHoverForeground(null);
        hl.setActiveForeground(null);
        hl.setActiveUnderline(null);
        break;
        
      case DEFAULT:
        hl.setUnderline(hl.getForeground());
        hl.setHoverUnderline(hl.getForeground());
        hl.setHoverForeground(hl.getForeground());
        break;
        
      case MOUSEOVER:
        // do nothing
        break;
    }

    return hl;
  }
}
