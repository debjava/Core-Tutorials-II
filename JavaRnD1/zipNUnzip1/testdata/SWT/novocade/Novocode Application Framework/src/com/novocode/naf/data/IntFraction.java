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
 * Represents a fraction of two integers.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 18, 2003
 * @version $Id: IntFraction.java 283 2004-12-18 16:14:43 +0000 (Sat, 18 Dec 2004) szeiger $
 */

public final class IntFraction
{
  public final int numerator;
  public final int denominator;


  public IntFraction(int numerator, int denominator)
  {
    this.numerator = numerator;
    this.denominator = denominator;
  }


  public String toString()
  {
    if(denominator == 100) return numerator + "%";
    else return numerator + "/" + denominator;
  }
}
