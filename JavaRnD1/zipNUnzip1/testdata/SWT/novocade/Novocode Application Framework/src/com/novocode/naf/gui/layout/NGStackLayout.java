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

package com.novocode.naf.gui.layout;

import java.util.Map;

import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.*;

import com.novocode.naf.app.NAFException;
import com.novocode.naf.data.SizeMeasure;
import com.novocode.naf.data.SizeMeasurePair;
import com.novocode.naf.gui.SWTUtil;
import com.novocode.naf.gui.ShellWindowInstance;
import com.novocode.naf.gui.event.ChangeEvent;
import com.novocode.naf.gui.event.IChangeListener;
import com.novocode.naf.model.DefaultIntModel;
import com.novocode.naf.model.IIntModel;
import com.novocode.naf.model.ModelBinding;
import com.novocode.naf.resource.ConfProperty;


/**
 * Layout creator for SWT StackLayout.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Apr 24, 2004
 * @version $Id: NGStackLayout.java 412 2008-05-04 18:27:58Z szeiger $
 */

public class NGStackLayout extends NGLayout
{
  private SizeMeasurePair margin = new SizeMeasurePair(new SizeMeasure(0));
  private ModelBinding topModelBinding;
  private Map<String, ModelBinding> simpleModelBindings;


  @ConfProperty
  public void setMargin(SizeMeasurePair a) { this.margin = new SizeMeasurePair(a); }


  @ConfProperty("hmargin")
  public void setHorizontalMargin(SizeMeasure s) { this.margin.setHorizonal(s); }
  public SizeMeasure getHorizontalMargin() { return margin.getHorizonal(); }


  @ConfProperty("vmargin")
  public void setVerticalMargin(SizeMeasure s) { this.margin.setVertical(s); }
  public SizeMeasure getVerticalMargin() { return margin.getVertical(); }


  @ConfProperty("model")
  public void setModelBindings(ModelBinding[] mbs)
  {
    topModelBinding = null;
    for(ModelBinding mb : mbs) addModelBinding(mb);
  }
  public void addModelBinding(ModelBinding mb)
  {
    if("top".equals(mb.getType())) topModelBinding = mb;
  }


  @ConfProperty(prefix = "m")
  public void setSimpleModelBindings(Map<String, ModelBinding> p)
  {
    simpleModelBindings = p;
    topModelBinding = (p == null) ? null : p.get("top");
  }
  public Map<String, ModelBinding> getSimpleModelBindings() { return simpleModelBindings; }


  public void assignLayout(final Composite comp, ShellWindowInstance wi) throws NAFException
  {
    int embase = SWTUtil.computeEMBase(comp);
    final StackLayout l = new StackLayout();
    l.marginWidth  = margin.getHorizonal().compute(embase);
    l.marginHeight = margin.getVertical().compute(embase);
    comp.setLayout(l);
    if(topModelBinding != null)
    {
      IIntModel model = wi.models.get(topModelBinding.getID());
      if(model == null && (topModelBinding.getCreate() != ModelBinding.Create.NO))
        model = new DefaultIntModel();
      if(model != null)
      {
        final IIntModel finalModel = model;
        final Control[] ch = comp.getChildren();
        IChangeListener cl = new IChangeListener()
        {
          public void stateChanged(ChangeEvent e)
          {
            int i = finalModel.getInt();
            if(i < 0) i = 0;
            else if(i >= ch.length) i = ch.length-1;
            Control c = ch[i];
            if(l.topControl != c)
            {
              l.topControl = c;
              comp.layout();
            }
          }
        };
        SWTUtil.registerModel(comp, finalModel, cl);
        cl.stateChanged(null);
      }
    }
  }
}
