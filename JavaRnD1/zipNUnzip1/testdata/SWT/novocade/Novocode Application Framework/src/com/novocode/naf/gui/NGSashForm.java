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
import org.eclipse.swt.widgets.*;

import com.novocode.naf.app.*;
import com.novocode.naf.data.Orientation;
import com.novocode.naf.data.Shadow;
import com.novocode.naf.data.SizeMeasure;
import com.novocode.naf.gui.event.ChangeEvent;
import com.novocode.naf.gui.event.IChangeListener;
import com.novocode.naf.model.IObjectModel;
import com.novocode.naf.resource.*;
import com.novocode.naf.swt.custom.LiveSashForm;


/**
 * A sash form (a group of widgets with movable dividers).
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Feb 5, 2004
 */

public final class NGSashForm extends NGWidget
{
  private static final SizeMeasure DEFAULT_SASH_WIDTH = new SizeMeasure(3);

  private Orientation orientation = Orientation.HORIZONTAL;
  private SizeMeasure sashWidth = DEFAULT_SASH_WIDTH;
  private boolean live = true;
  private Shadow[] childShadow;
  private int[] weights;


  @ConfProperty
  public void setOrientation(Orientation o) { this.orientation = o; }
  public Orientation getOrientation() { return orientation; }
  
  
  @ConfProperty
  public void setLive(boolean b) { this.live = b; }
  public boolean isLive() { return live; }
  
  
  @ConfProperty
  public void setChildShadow(Shadow[] a) { this.childShadow = a; }
  public Shadow[] getChildShadow() { return childShadow; }

  
  @ConfProperty
  public void setSashWidth(SizeMeasure s)
  {
    if(s == null) s = DEFAULT_SASH_WIDTH;
    this.sashWidth = s;
  }
  public SizeMeasure getSashWidth() { return sashWidth; }

  
  @ConfProperty
  public void setWeights(int[] a) { this.weights = a; }
  public int[] getWeights() { return weights; }

  
  public Control createControl(Composite parent, NGComponent parentComp, ShellWindowInstance wi, WidgetData pwd) throws NAFException
  {
    int style = orientation.style;
    if(!live) style |= LiveSashForm.NO_LIVE_UPDATE;
    final LiveSashForm sf = new LiveSashForm(parent, style);
    int embase = SWTUtil.computeEMBase(sf);
    sf.sashWidth = sashWidth.compute(embase);
    
    return sf;
  }
  
  
  protected void configureControl(final Control control, final ShellWindowInstance wi, WidgetData wd, WidgetData pwd)
  {
    super.configureControl(control, wi, wd, pwd);
    addDefaultChildren((Composite)control, wi, wd);

    final LiveSashForm sf = (LiveSashForm)control;
    if(childShadow != null)
    {
      Control[] cc = sf.getChildren();
      for(int i=0; i<cc.length; i++)
      {
        if(i >= childShadow.length) break;
        sf.setChildBorder(cc[i], childShadow[i].style);
      }
    }

    final IObjectModel<int[]> weightsModel = getModel("weights", wi.models, IObjectModel.class);
    if(weightsModel != null)
    {
      final boolean[] lock = new boolean[1];
      IChangeListener cl = new IChangeListener()
      {
        public void stateChanged(ChangeEvent e)
        {
          int[] weights = weightsModel.getValue();
          if(weights == null) return;
          if(eq(weights, sf.getWeights())) return;
          try
          {
            lock[0] = true;
            sf.setWeights(weights);
          }
          finally { lock[0] = false; }
        }
      };
      SWTUtil.registerModel(sf, weightsModel, cl);
      cl.stateChanged(null);

      sf.addListener(SWT.Selection, new Listener()
      {
        public void handleEvent(Event event)
        {
          if(lock[0]) return;
          int[] weights = sf.getWeights();
          if(!eq(weightsModel.getValue(), weights)) weightsModel.setValue(weights);
        }
      });
    }
    else if(weights != null) sf.setWeights(weights);
}

  
  private static boolean eq(int[] a1, int[] a2)
  {
    if(a1 == null && a2 == null) return true;
    if(a1 == null || a2 == null) return false;
    for(int i=0; i<a1.length; i++)
      if(a1[i] != a2[i]) return false;
    return true;
  }
}
