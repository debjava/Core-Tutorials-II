/*******************************************************************************
 * Copyright (c) 2007 Stefan Zeiger and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.novocode.com/legal/epl-v10.html
 * 
 * Contributors:
 *     Stefan Zeiger (szeiger@novocode.com) - initial API and implementation
 *******************************************************************************/

package com.novocode.naf.gui;

import com.novocode.naf.data.Alignment;
import com.novocode.naf.data.SizeMeasure;
import com.novocode.naf.resource.ConfigurableObject;
import com.novocode.naf.resource.ConfProperty;


/**
 * A column in an NGTable or NGTableTree
 *
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Mar 15, 2008
 */

public final class TableColumnData implements ConfigurableObject
{
  String text, imgres;
  boolean resizable = true;
  SizeMeasure width;
  Alignment align = Alignment.LEFT;

  @ConfProperty("image")
  public void setImageResource(String s) { imgres = s; }
  public String getImageResource() { return imgres; }

  @ConfProperty("text")
  public void setText(String s) { text = s; }
  public String getText() { return text; }

  @ConfProperty("resize")
  public void setResizable(boolean b) { resizable = b; }
  public boolean isResizable() { return resizable; }

  @ConfProperty("align")
  public void setAlignment(Alignment a) { align = a; }
  public Alignment getAlignment() { return align; }

  @ConfProperty("width")
  public void setWidth(SizeMeasure sm) { width = sm; }
  public SizeMeasure getWidth() { return width; }
}
