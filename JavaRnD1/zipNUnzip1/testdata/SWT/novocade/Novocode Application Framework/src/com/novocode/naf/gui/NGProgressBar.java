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
import com.novocode.naf.gui.event.*;
import com.novocode.naf.model.*;
import com.novocode.naf.resource.*;


/**
 * A progress bar.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Jan 2, 2004
 */

public final class NGProgressBar extends NGWidget
{
  private int min, max = 100;
  private boolean indeterminate, smooth;
  private Orientation orientation = Orientation.HORIZONTAL;


  @ConfProperty
  public void setOrientation(Orientation o) { this.orientation = o; }
  public Orientation getOrientation() { return orientation; }
  
  
  @ConfProperty
  public void setIndeterminate(boolean b) { this.indeterminate = b; }
  public boolean isIndeterminate() { return indeterminate; }
  
  
  @ConfProperty
  public void setSmooth(boolean b) { this.smooth = b; }
  public boolean isSmooth() { return smooth; }
  
  
  @ConfProperty
  public void setMin(int i) { this.min = i; }
  public int getMin() { return min; }
  
  
  @ConfProperty
  public void setMax(int i) { this.max = i; }
  public int getMax() { return max; }
  

  public Control createControl(Composite parent, NGComponent parentComp, ShellWindowInstance wi, WidgetData pwd) throws NAFException
  {
    int style = orientation.style;
    if(indeterminate) style |= SWT.INDETERMINATE;
    if(smooth) style |= SWT.SMOOTH;
    final ProgressBar pb = new ProgressBar(parent, style);
    pb.setMinimum(min);
    pb.setMaximum(max);

    final IProgressReadModel progressModel = getModel("progress", wi.models, IProgressReadModel.class);
    if(progressModel != null)
    {
      IChangeListener cl = new IChangeListener()
      {
        public void stateChanged(ChangeEvent e)
        {
          int newMin = progressModel.getMin();
          int newMax = progressModel.getMax();
          int newProgress = progressModel.getProgress();
          if(newMin != pb.getMinimum()) pb.setMinimum(newMin);
          if(newMax != pb.getMaximum()) pb.setMaximum(newMax);
          if(newProgress != pb.getSelection()) pb.setSelection(newProgress);
        }
      };
      SWTUtil.registerModel(pb, progressModel, cl);
      cl.stateChanged(null);
    }
    else hookUpReadModel(Integer.TYPE,
      (IModel)getModel("value", wi.models, IIntReadModel.class, IObjectReadModel.class),
      pb, "setSelection", "getSelection");

    return pb;
  }
}
