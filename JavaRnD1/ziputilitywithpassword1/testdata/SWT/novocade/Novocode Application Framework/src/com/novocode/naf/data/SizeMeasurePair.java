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
 * A pair of one horizontal and one vertical SizeMeasure.
 *
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 17, 2004
 * @version $Id: SizeMeasurePair.java 283 2004-12-18 16:14:43 +0000 (Sat, 18 Dec 2004) szeiger $
 */

public final class SizeMeasurePair
{
  private SizeMeasure hsize, vsize;


  public SizeMeasurePair(SizeMeasure m) { hsize = m; vsize = m; }

  public SizeMeasurePair(SizeMeasure h, SizeMeasure v) { hsize = h; vsize = v; }

  public SizeMeasurePair(SizeMeasurePair p) { hsize = p.hsize; vsize = p.vsize; }


  public void setAll(SizeMeasure m) { hsize = m; vsize = m; }
  
  public void setHorizonal(SizeMeasure m) { hsize = m; }
  public SizeMeasure getHorizonal() { return hsize; }

  public void setVertical(SizeMeasure m) { vsize = m; }
  public SizeMeasure getVertical() { return vsize; }
  
  
  public String toString() { return hsize + " " + vsize; }
}
