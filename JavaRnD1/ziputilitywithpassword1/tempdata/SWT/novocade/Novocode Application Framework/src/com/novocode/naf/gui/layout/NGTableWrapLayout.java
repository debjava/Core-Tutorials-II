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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.forms.widgets.TableWrapData;

import com.novocode.naf.app.NAFException;
import com.novocode.naf.data.DataDecoder;
import com.novocode.naf.data.SizeMeasure;
import com.novocode.naf.data.SizeMeasurePair;
import com.novocode.naf.data.SizeMeasureQuadruple;
import com.novocode.naf.gui.SWTUtil;
import com.novocode.naf.gui.ShellWindowInstance;
import com.novocode.naf.resource.ConfProperty;


/**
 * Layout creator for Eclipse Forms TableWrapLayout.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Nov 10, 2004
 * @version $Id: NGTableWrapLayout.java 409 2008-05-03 19:31:39Z szeiger $
 */

public class NGTableWrapLayout extends NGLayout
{
  private static final SizeMeasure defaultIndent  = new SizeMeasure(0);
  private static final SizeMeasure defaultHint    = new SizeMeasure(SWT.DEFAULT);

  public enum HorizAlign
  {
    LEFT      (TableWrapData.LEFT),
    CENTER    (TableWrapData.CENTER),
    RIGHT     (TableWrapData.RIGHT),
    FILL      (TableWrapData.FILL),
    FILL_GRAB (TableWrapData.FILL_GRAB);

    public final int code;
    HorizAlign(int code) { this.code = code; }
  }

  public enum VertAlign
  {
    TOP       (TableWrapData.TOP),
    MIDDLE    (TableWrapData.MIDDLE),
    BOTTOM    (TableWrapData.BOTTOM),
    FILL      (TableWrapData.FILL),
    FILL_GRAB (TableWrapData.FILL_GRAB);

    public final int code;
    VertAlign(int code) { this.code = code; }
  }

  private int columns = 1;
  private SizeMeasurePair spacing = new SizeMeasurePair(new SizeMeasure(0));
  private SizeMeasureQuadruple margin = new SizeMeasureQuadruple(new SizeMeasure(0));
  private boolean equalWidth;


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
  public void setSpacing(SizeMeasurePair a) { this.spacing = new SizeMeasurePair(a); }


  @ConfProperty("hspacing")
  public void setHorizontalSpacing(SizeMeasure s) { this.spacing.setHorizonal(s); }
  public SizeMeasure getHorizontalSpacing() { return spacing.getHorizonal(); }


  @ConfProperty("vspacing")
  public void setVerticalSpacing(SizeMeasure s) { this.spacing.setVertical(s); }
  public SizeMeasure getVerticalSpacing() { return spacing.getVertical(); }

  
  @ConfProperty
  public void setColumns(int i) { this.columns = i; }
  public int getColumns() { return columns; }
  
  
  @ConfProperty
  public void setEqualWidth(boolean b) { this.equalWidth = b; }
  public boolean isEqualWidth() { return equalWidth; }

  
  public void assignLayout(Composite comp, ShellWindowInstance wi) throws NAFException
  {
    int embase = SWTUtil.computeEMBase(comp);
    TableWrapLayout l = new TableWrapLayout();
    l.numColumns = columns;
    l.makeColumnsEqualWidth = equalWidth;
    l.horizontalSpacing = spacing.getHorizonal().compute(embase);
    l.verticalSpacing   = spacing.getVertical().compute(embase);
    l.leftMargin        = margin.getLeft().compute(embase);
    l.rightMargin       = margin.getRight().compute(embase);
    l.topMargin         = margin.getTop().compute(embase);
    l.bottomMargin      = margin.getBottom().compute(embase);
    comp.setLayout(l);
  }


  @Override
  public void assignLayoutData(Control control, Map<String, String> attributes, Map<String, Control> siblings, Control[] siblingsList, int siblingsIndex) throws NAFException
  {
    int embase = SWTUtil.computeEMBase(control.getParent());

    TableWrapData d = new TableWrapData(
      /* align   */ DataDecoder.decodeEnum(attributes.get("halign"), HorizAlign.class, HorizAlign.LEFT).code,
      /* valign  */ DataDecoder.decodeEnum(attributes.get("valign"), VertAlign.class, VertAlign.TOP).code,
      /* rowspan */ DataDecoder.decodeNumber(attributes.get("vspan"), 1),
      /* colspan */ DataDecoder.decodeNumber(attributes.get("hspan"), 1));
    d.indent         = DataDecoder.decodeLength(attributes.get("hindent"), defaultIndent).compute(embase);
    d.maxWidth       = DataDecoder.decodeLength(attributes.get("max-width"), defaultHint).compute(embase);
    d.maxHeight      = DataDecoder.decodeLength(attributes.get("max-height"), defaultHint).compute(embase);
    d.heightHint     = DataDecoder.decodeLength(attributes.get("height"), defaultHint).compute(embase);
    d.grabHorizontal = DataDecoder.decodeBoolean(attributes.get("hgrab"), d.grabHorizontal);
    d.grabVertical   = DataDecoder.decodeBoolean(attributes.get("vgrab"), d.grabVertical);
    control.setLayoutData(d);
  }
}
