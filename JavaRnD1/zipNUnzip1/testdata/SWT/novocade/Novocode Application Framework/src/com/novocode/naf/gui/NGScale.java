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

import org.eclipse.swt.widgets.*;

import com.novocode.naf.app.*;
import com.novocode.naf.data.Orientation;
import com.novocode.naf.model.*;
import com.novocode.naf.resource.*;


/**
 * A scale control.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Jan 3, 2004
 * @version $Id: NGScale.java 412 2008-05-04 18:27:58Z szeiger $
 */

public final class NGScale extends NGWidget
{
  private int min, max = 15, increment = 1, pageIncrement = 4;
  private Orientation orientation = Orientation.HORIZONTAL;


  @ConfProperty
  public void setOrientation(Orientation o) { this.orientation = o; }
  public Orientation getOrientation() { return orientation; }


  @ConfProperty
  public void setMin(int i) { this.min = i; }
  public int getMin() { return min; }


  @ConfProperty
  public void setMax(int i) { this.max = i; }
  public int getMax() { return max; }


  @ConfProperty
  public void setIncrement(int i) { this.increment = i; }
  public int getIncrement() { return increment; }


  @ConfProperty
  public void setPageIncrement(int i) { this.pageIncrement = i; }
  public int getPageIncrement() { return pageIncrement; }


  public Control createControl(Composite parent, NGComponent parentComp, ShellWindowInstance wi, WidgetData pwd) throws NAFException
  {
    final Scale scale = new Scale(parent, orientation.style);
    scale.setMinimum(min);
    scale.setMaximum(max);
    scale.setIncrement(increment);
    scale.setPageIncrement(pageIncrement);

    // [TODO] Allow a ScaleModel to be used
    IIntModel valueModel = getModel("value", wi.models, IIntModel.class);
    hookUpIIntModel(valueModel, scale, "getSelection", "setSelection");

    return scale;
  }
}
