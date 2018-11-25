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
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.novocode.naf.app.*;
import com.novocode.naf.data.Alignment;
import com.novocode.naf.gui.event.*;
import com.novocode.naf.model.*;
import com.novocode.naf.model.swt.SWTTextStringModel;
import com.novocode.naf.resource.*;


/**
 * A text box (text input field).
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 5, 2003
 */

public final class NGText extends NGWidget
{
  private static final Logger LOGGER = LoggerFactory.getLogger(NGText.class);

  private boolean multi, readOnly, wrap, hscroll, vscroll, border = true;
  private Alignment alignment = Alignment.LEFT;
  private Type type = Type.DEFAULT;

  public enum Type { DEFAULT, PASSWORD, SEARCH }

  
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
  
  
  @ConfProperty
  public void setType(Type t) { this.type = t; }
  public Type getType() { return type; }
  
  
  @ConfProperty("hscroll")
  public void setHScroll(boolean b) { this.hscroll = b; }
  public boolean getHScroll() { return hscroll; }
  
  
  @ConfProperty("vscroll")
  public void setVScroll(boolean b) { this.vscroll = b; }
  public boolean getVScroll() { return vscroll; }
  
  
  @ConfProperty
  public void setBorder(boolean b) { this.border = b; }
  public boolean getBorder() { return border; }

  
  @SuppressWarnings("unchecked")
  public Control createControl(Composite parent, NGComponent parentComp, ShellWindowInstance wi, WidgetData pwd) throws NAFException
  {
    int style = alignment.style | (multi ? SWT.MULTI : SWT.SINGLE);
    if(hscroll) style |= SWT.H_SCROLL;
    if(vscroll) style |= SWT.V_SCROLL;
    if(wrap) style |= SWT.WRAP;
    if(readOnly) style |= SWT.READ_ONLY;
    if(border) style |= SWT.BORDER;
    if(type == Type.PASSWORD) style |= SWT.PASSWORD;
    else if(type == Type.SEARCH) style |= SWT.SEARCH;
    final Text text = new Text(parent, style);

    final IObjectReadModel<String> textReadModel = getModel("text", wi.models, DefaultStringModel.PTYPE_READ);
    if(textReadModel != null)
    {
      if((textReadModel instanceof SWTTextStringModel) && (((SWTTextStringModel)textReadModel).getControl() == null))
      {
        ((SWTTextStringModel)textReadModel).setControl(text);
        LOGGER.debug("Attached to SWTTextStringModel in component "+getID());
      }
      else
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
                  String s = textReadModel.getValue();
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
              String s = textReadModel.getValue();
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
        SWTUtil.registerModel(text, textReadModel, cl);
        cl.stateChanged(null);
        if(!readOnly)
        {
          final IObjectModifyModel<String> textModifyModel = readOnly ? null : (IObjectModifyModel<String>)textReadModel;
          text.addModifyListener(new ModifyListener()
          {
            public void modifyText(ModifyEvent e)
            {
              if(!lock[0]) textModifyModel.setValue(text.getText());
            }
          });
        }
      }
    }

    final IActionListener defaultSelectModel = getModel("default-select", wi.models, IActionListener.class);
    if(defaultSelectModel != null)
    {
      final ActionEvent ae = new ActionEvent(this, getModelBinding("default-select").getAttribute("command"), wi);
      text.addListener(SWT.DefaultSelection, new Listener()
      {
        public void handleEvent(Event e)
        {
          defaultSelectModel.performAction(ae);
        }
      });
    }

    return text;
  }


  @Override
  protected Object createDefaultModel(ModelBinding mb, java.lang.reflect.Type[] modelTypes)
  {
    if("text".equals(mb.getType()))
    {
      if(mb.getCreate() == ModelBinding.Create.WIDGET) return new SWTTextStringModel();
      else return new DefaultStringModel();
    }
    else return super.createDefaultModel(mb, modelTypes);
  }
}
