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

import java.util.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;

import com.novocode.naf.app.NAFException;
import com.novocode.naf.data.DataDecoder;
import com.novocode.naf.data.Orientation;
import com.novocode.naf.data.SizeMeasure;
import com.novocode.naf.data.SizeMeasureQuadruple;
import com.novocode.naf.gui.SWTUtil;
import com.novocode.naf.gui.ShellWindowInstance;
import com.novocode.naf.resource.ConfProperty;


/**
 * Layout creator for SWT RowLayout.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 14, 2003
 */

public class NGRowLayout extends NGLayout
{
  private static final SizeMeasure DEFAULT_HINT    = new SizeMeasure(SWT.DEFAULT);


  private Orientation orientation = Orientation.HORIZONTAL;
  private SizeMeasureQuadruple margin = new SizeMeasureQuadruple(new SizeMeasure(0));
  private SizeMeasure spacing = new SizeMeasure(0, 0.3, 0);
  private boolean wrap = true, pack = true, fill, justify;


  @ConfProperty
  public void setMargin(SizeMeasureQuadruple q) { this.margin = new SizeMeasureQuadruple(q); }


  @ConfProperty("lmargin")
  public void setLeftMargin(SizeMeasure s) { this.margin.setLeft(s); }
  public SizeMeasure getLeftMargin() { return margin.getLeft(); }


  @ConfProperty("rmargin")
  public void setRightMargin(SizeMeasure s) { this.margin.setRight(s); }
  public SizeMeasure getRightMargin() { return margin.getRight(); }


  @ConfProperty("tmargin")
  public void setTopMargin(SizeMeasure s) { this.margin.setTop(s); }
  public SizeMeasure getTopMargin() { return margin.getTop(); }


  @ConfProperty("bmargin")
  public void setBottomMargin(SizeMeasure s) { this.margin.setBottom(s); }
  public SizeMeasure getBottomMargin() { return margin.getBottom(); }


  @ConfProperty
  public void setOrientation(Orientation o) { this.orientation = o; }
  public Orientation getOrientation() { return orientation; }


  @ConfProperty
  public void setSpacing(SizeMeasure s) { this.spacing = s; }
  public SizeMeasure getSpacing() { return spacing; }


  @ConfProperty
  public void setWrap(boolean b) { this.wrap = b; }
  public boolean isWrap() { return wrap; }


  @ConfProperty
  public void setPack(boolean b) { this.pack = b; }
  public boolean isPack() { return pack; }

  
  @ConfProperty
  public void setFill(boolean b) { this.fill = b; }
  public boolean isFill() { return fill; }

  
  @ConfProperty
  public void setJustify(boolean b) { this.justify = b; }
  public boolean isJustify() { return justify; }

  
  public void assignLayout(Composite comp, ShellWindowInstance wi) throws NAFException
  {
    int embase = SWTUtil.computeEMBase(comp);
    RowLayout l = new RowLayout();
    l.type         = orientation.style;
    l.marginLeft   = margin.getLeft().compute(embase);
    l.marginRight  = margin.getRight().compute(embase);
    l.marginTop    = margin.getTop().compute(embase);
    l.marginBottom = margin.getBottom().compute(embase);
    l.spacing      = spacing.compute(embase);
    l.wrap         = wrap;
    l.pack         = pack;
    l.fill         = fill;
    l.justify      = justify;
    comp.setLayout(l);
  }


  @Override
  public void assignLayoutData(Control control, Map<String, String> attributes, Map<String, Control> siblings, Control[] siblingsList, int siblingsIndex) throws NAFException
  {
    int embase = SWTUtil.computeEMBase(control.getParent());
    int width  = DataDecoder.decodeLength(attributes.get("width"), DEFAULT_HINT).compute(embase);
    int height = DataDecoder.decodeLength(attributes.get("height"), DEFAULT_HINT).compute(embase);
    control.setLayoutData(new RowData(width, height));
  }
}
