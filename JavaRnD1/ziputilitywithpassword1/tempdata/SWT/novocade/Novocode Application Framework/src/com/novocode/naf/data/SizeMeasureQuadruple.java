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

package com.novocode.naf.data;


/**
 * A quadruple of left, right, top and bottom SizeMeasures.
 *
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 17, 2004
 * @version $Id: SizeMeasureQuadruple.java 283 2004-12-18 16:14:43 +0000 (Sat, 18 Dec 2004) szeiger $
 */

public final class SizeMeasureQuadruple
{
  private SizeMeasure lsize, rsize, tsize, bsize;


  public SizeMeasureQuadruple(SizeMeasure m) { lsize = m; rsize = m; tsize = m; bsize = m; }

  public SizeMeasureQuadruple(SizeMeasure h, SizeMeasure v) { lsize = h; rsize = h; tsize = v; bsize = v; }

  public SizeMeasureQuadruple(SizeMeasure l, SizeMeasure r, SizeMeasure t, SizeMeasure b) { lsize = l; rsize = r; tsize = t; bsize = b; }

  public SizeMeasureQuadruple(SizeMeasureQuadruple q) { lsize = q.lsize; rsize = q.rsize; tsize = q.tsize; bsize = q.bsize; }


  public void setAll(SizeMeasure m) { lsize = m; rsize = m; tsize = m; bsize = m; }

  public void setHorizonal(SizeMeasure m) { lsize = m; rsize = m; }

  public void setVertical(SizeMeasure m) { tsize = m; bsize = m; }

  public void setLeft(SizeMeasure m) { lsize = m; }
  public SizeMeasure getLeft() { return lsize; }

  public void setRight(SizeMeasure m) { rsize = m; }
  public SizeMeasure getRight() { return rsize; }

  public void setTop(SizeMeasure m) { tsize = m; }
  public SizeMeasure getTop() { return tsize; }

  public void setBottom(SizeMeasure m) { bsize = m; }
  public SizeMeasure getBottom() { return bsize; }


  public String toString() { return lsize + " " + rsize + " " + tsize+ " " + bsize; }
}
