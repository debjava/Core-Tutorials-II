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

import org.eclipse.swt.SWT;

import com.novocode.naf.app.NAFException;


/**
 * A shadow enumeration which is used by several widgets.
 *
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Oct 30, 2004
 * @version $Id: Shadow.java 369 2008-03-26 00:29:28Z szeiger $
 */

public enum Shadow
{
  NONE       (SWT.SHADOW_NONE),
  IN         (SWT.SHADOW_IN),
  OUT        (SWT.SHADOW_OUT),
  ETCHED_IN  (SWT.SHADOW_ETCHED_IN),
  ETCHED_OUT (SWT.SHADOW_ETCHED_OUT),
  DEFAULT    (SWT.NONE);


  public final int style;


  Shadow(int style) { this.style = style; }


  public void checkStyleMask(int mask, boolean allowDefault) throws NAFException
  {
    if(this == DEFAULT)
    {
      if(allowDefault) return;
    }
    else
    {
      if((style & mask) != 0) return;
    }

    throw new NAFException("Shadow must be one of "+createStyleMaskString(mask, allowDefault));
  }


  private static String createStyleMaskString(int mask, boolean allowDefault)
  {
    StringBuilder sb = new StringBuilder();
    if(allowDefault) sb.append("DEFAULT");
    for(Shadow sh : Shadow.values())
    {
      if(sh == DEFAULT) continue;
      if((mask & sh.style) != 0)
      {
        if(sb.length() != 0) sb.append(", ");
        sb.append(sh.name());
      }
    }
    return sb.toString();
  }
}
