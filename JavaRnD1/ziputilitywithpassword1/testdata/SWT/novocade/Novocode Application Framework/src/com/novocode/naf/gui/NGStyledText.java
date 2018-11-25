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
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;

import com.novocode.naf.app.*;
import com.novocode.naf.data.Alignment;
import com.novocode.naf.gui.event.*;
import com.novocode.naf.model.*;
import com.novocode.naf.resource.*;


/**
 * A text editor with styled text support.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Mar 1, 2004
 */

public final class NGStyledText extends NGWidget
{
  private boolean multi, readOnly, wrap, hscroll, vscroll, fullSelection, border = true;
  private Alignment alignment = Alignment.LEFT;


  @ConfProperty
  public void setMulti(boolean b) { this.multi = b; }
  public boolean isMulti() { return multi; }
  
  
  @ConfProperty
  public void setReadOnly(boolean b) { this.readOnly = b; }
  public boolean isReadOnly() { return readOnly; }
  
  
  @ConfProperty
  public void setWrap(boolean b) { this.wrap = b; }
  public boolean getWrap() { return wrap; }
  
  
  @ConfProperty("align")
  public void setAlignment(Alignment a) { this.alignment = a; }
  public Alignment getAlignment() { return alignment; }
  
  
  @ConfProperty("hscroll")
  public void setHScroll(boolean b) { this.hscroll = b; }
  public boolean getHScroll() { return hscroll; }
  
  
  @ConfProperty("vscroll")
  public void setVScroll(boolean b) { this.vscroll = b; }
  public boolean getVScroll() { return vscroll; }
  
  
  @ConfProperty
  public void setFullSelection(boolean b) { this.fullSelection = b; }
  public boolean getFullSelection() { return fullSelection; }
  
  
  @ConfProperty
  public void setBorder(boolean b) { this.border = b; }
  public boolean getBorder() { return border; }


  public Control createControl(Composite parent, NGComponent parentComp, ShellWindowInstance wi, WidgetData pwd) throws NAFException
  {
    // [TODO] Implement read StyledText model
    int style = alignment.style | (multi ? SWT.MULTI : SWT.SINGLE);
    if(hscroll) style |= SWT.H_SCROLL;
    if(vscroll) style |= SWT.V_SCROLL;
    if(wrap) style |= SWT.WRAP;
    if(readOnly) style |= SWT.READ_ONLY;
    if(fullSelection) style |= SWT.FULL_SELECTION;
    if(border) style |= SWT.BORDER;

    final StyledText text = new StyledText(parent, style);

    final IObjectModel<String> textModel = getModel("text", wi.models, DefaultStringModel.PTYPE_FULL);
    if(textModel != null)
    {
      final boolean[] lock = new boolean[1];
      final IChangeListener cl = new IChangeListener()
      {
        public void stateChanged(ChangeEvent e)
        {
          if(e instanceof StringChangeEvent)
          {
            StringChangeEvent ee = (StringChangeEvent)e;
            switch(ee.action)
            {
              case StringChangeEvent.ADD:
              {
                try
                {
                  lock[0] = true;
                  text.append(ee.data);
                }
                finally { lock[0] = false; }
              }
              break;

              default:
              {
                String s = textModel.getValue();
                if(s == null) s = "";
                if(s.equals(text.getText())) return;
                try
                {
                  lock[0] = true;
                  text.setText(s);
                }
                finally { lock[0] = false; }
              }
              break;
            }
          }
          else
          {
            String s = textModel.getValue();
            if(s == null) s = "";
            if(s.equals(text.getText())) return;
            try
            {
              lock[0] = true;
              text.setText(s);
            }
            finally { lock[0] = false; }
          }
        }
      };
      SWTUtil.registerModel(text, textModel, cl);
      cl.stateChanged(null);
      text.addModifyListener(new ModifyListener()
      {
        public void modifyText(ModifyEvent e)
        {
          if(!lock[0]) textModel.setValue(text.getText());
        }
      });
    }

    return text;
  }
}
