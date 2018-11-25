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
 * Contains a decoded size which can depend on "em" and "%" measures.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 16, 2003
 * @version $Id: SizeMeasure.java 283 2004-12-18 16:14:43 +0000 (Sat, 18 Dec 2004) szeiger $
 */

public final class SizeMeasure
{
  public final double baseSize;
  public final double emFactor;
  public final double scaleFactor;

  
  public SizeMeasure(double baseSize, double emFactor, double scaleFactor)
  {
    this.baseSize = baseSize;
    this.emFactor= emFactor;
    this.scaleFactor = scaleFactor;
  }
  
  
  public SizeMeasure(double baseSize)
  {
    this.baseSize = baseSize;
    this.emFactor= 0.0;
    this.scaleFactor = 0.0;
  }
  
  
  public int compute(int emSize, int scaleSize)
  {
    double d = baseSize + (emFactor * emSize);
    if(scaleSize > 0) d += (scaleFactor * scaleSize);
    if(d < 0) return (int)(d - 0.5);
    else return (int)(d + 0.5);
  }

  
  public int compute(int emSize)
  {
    return compute(emSize, 0);
  }

  
  public String toString()
  {
    if(scaleFactor != 0) return (scaleFactor*100)+"%";
    else if(emFactor != 0) return emFactor+"em";
    else return baseSize+"px";
  }
}
