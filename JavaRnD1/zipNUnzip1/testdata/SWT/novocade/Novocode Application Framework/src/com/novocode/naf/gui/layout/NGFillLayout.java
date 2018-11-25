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

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;

import com.novocode.naf.app.NAFException;
import com.novocode.naf.data.Orientation;
import com.novocode.naf.data.SizeMeasure;
import com.novocode.naf.data.SizeMeasurePair;
import com.novocode.naf.gui.SWTUtil;
import com.novocode.naf.gui.ShellWindowInstance;
import com.novocode.naf.resource.ConfProperty;


/**
 * Layout creator for SWT FillLayout.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Nov 29, 2003
 */

public final class NGFillLayout extends NGLayout
{
  private static final SizeMeasure DEFAULT_MARGIN  = new SizeMeasure(0);
  private static final SizeMeasure DEFAULT_SPACING = DEFAULT_MARGIN;


  private Orientation orientation = Orientation.VERTICAL;
  private SizeMeasurePair margin = new SizeMeasurePair(DEFAULT_MARGIN);
  private SizeMeasure spacing = DEFAULT_SPACING;


  @ConfProperty
  public void setMargin(SizeMeasurePair a) { this.margin = new SizeMeasurePair(a); }


  @ConfProperty("hmargin")
  public void setHorizontalMargin(SizeMeasure s) { this.margin.setHorizonal(s); }
  public SizeMeasure getHorizontalMargin() { return margin.getHorizonal(); }


  @ConfProperty("vmargin")
  public void setVerticalMargin(SizeMeasure s) { this.margin.setVertical(s); }
  public SizeMeasure getVerticalMargin() { return margin.getVertical(); }


  @ConfProperty
  public void setSpacing(SizeMeasure s) { this.spacing = s; }
  public SizeMeasure getSpacing() { return spacing; }


  @ConfProperty
  public void setOrientation(Orientation o) { this.orientation = o; }
  public Orientation getOrientation() { return orientation; }


  public void assignLayout(Composite comp, ShellWindowInstance wi) throws NAFException
  {
    int embase = SWTUtil.computeEMBase(comp);
    FillLayout l = new FillLayout(orientation.style);
    if(margin != null)
    {
      l.marginWidth  = margin.getHorizonal().compute(embase);
      l.marginHeight = margin.getVertical().compute(embase);
      l.spacing      = spacing.compute(embase);
    }
    comp.setLayout(l);
  }
}
