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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import com.novocode.naf.app.NAFException;
import com.novocode.naf.data.DataDecoder;
import com.novocode.naf.data.SizeMeasure;
import com.novocode.naf.data.SizeMeasurePair;
import com.novocode.naf.gui.SWTUtil;
import com.novocode.naf.gui.ShellWindowInstance;
import com.novocode.naf.resource.ConfProperty;


/**
 * Layout creator for SWT GridLayout.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Nov 30, 2003
 */

public class NGGridLayout extends NGLayout
{
  private static final SizeMeasure NULL_SIZE    = new SizeMeasure(0);
  private static final SizeMeasure DEFAULT_SIZE = new SizeMeasure(SWT.DEFAULT);

  private SizeMeasurePair spacing = new SizeMeasurePair(NULL_SIZE), margin = new SizeMeasurePair(NULL_SIZE);
  private int columns = 1;
  private boolean equalWidth;


  @ConfProperty
  public void setMargin(SizeMeasurePair a) { this.margin = new SizeMeasurePair(a); }


  @ConfProperty("hmargin")
  public void setHorizontalMargin(SizeMeasure s) { this.margin.setHorizonal(s); }
  public SizeMeasure getHorizontalMargin() { return margin.getHorizonal(); }


  @ConfProperty("vmargin")
  public void setVerticalMargin(SizeMeasure s) { this.margin.setVertical(s); }
  public SizeMeasure getVerticalMargin() { return margin.getVertical(); }

  
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
    GridLayout l = new GridLayout(columns, equalWidth);
    l.horizontalSpacing = spacing.getHorizonal().compute(embase);
    l.verticalSpacing   = spacing.getVertical().compute(embase);
    l.marginWidth       = margin.getHorizonal().compute(embase);
    l.marginHeight      = margin.getVertical().compute(embase);
    comp.setLayout(l);
  }


  @Override
  public void assignLayoutData(Control control, Map<String, String> attributes, Map<String, Control> siblings, Control[] siblingsList, int siblingsIndex) throws NAFException
  {
    int embase = SWTUtil.computeEMBase(control.getParent());
    GridData d = new GridData();
    d.horizontalAlignment       = decodeAlignment(attributes.get("halign"), GridData.BEGINNING);
    d.verticalAlignment         = decodeAlignment(attributes.get("valign"), GridData.CENTER);
    if("fill-grab".equals(attributes.get("halign"))) d.grabExcessHorizontalSpace = true;
    if("fill-grab".equals(attributes.get("valign"))) d.grabExcessVerticalSpace = true;
    d.grabExcessHorizontalSpace = DataDecoder.decodeBoolean(attributes.get("hgrab"), d.grabExcessHorizontalSpace);
    d.grabExcessVerticalSpace   = DataDecoder.decodeBoolean(attributes.get("vgrab"), d.grabExcessVerticalSpace);
    d.heightHint                = DataDecoder.decodeLength(attributes.get("height"), DEFAULT_SIZE).compute(embase);
    d.widthHint                 = DataDecoder.decodeLength(attributes.get("width"), DEFAULT_SIZE).compute(embase);
    String mw = attributes.get("minWidth");
    String mh = attributes.get("minHeight");
    d.minimumWidth              = "auto".equals(mw) ? SWT.DEFAULT : DataDecoder.decodeLength(mw, NULL_SIZE).compute(embase);
    d.minimumHeight             = "auto".equals(mh) ? SWT.DEFAULT : DataDecoder.decodeLength(mh, NULL_SIZE).compute(embase);
    d.horizontalIndent          = DataDecoder.decodeLength(attributes.get("hindent"), NULL_SIZE).compute(embase);
    d.verticalIndent            = DataDecoder.decodeLength(attributes.get("vindent"), NULL_SIZE).compute(embase);
    d.horizontalSpan            = DataDecoder.decodeNumber(attributes.get("hspan"), 1);
    d.verticalSpan              = DataDecoder.decodeNumber(attributes.get("vspan"), 1);
    control.setLayoutData(d);
  }


  private int decodeAlignment(String s, final int def) throws NAFException
  {
    if(s == null) return def;
    s = s.trim();
    if(s.length() == 0) return def;

    if(s.equals("beginning")) return GridData.BEGINNING;
    else if(s.equals("center")) return GridData.CENTER;
    else if(s.equals("end")) return GridData.END;
    else if(s.equals("fill") || s.equals("fill-grab")) return GridData.FILL;
    else throw new NAFException("Illegal GridData alignment \""+s+"\"");
  }
}
