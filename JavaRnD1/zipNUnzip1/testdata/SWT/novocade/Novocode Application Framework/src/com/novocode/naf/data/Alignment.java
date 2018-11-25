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


/**
 * An alignment enumeration which is used by several widgets.
 *
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Oct 30, 2004
 * @version $Id: Alignment.java 219 2004-11-01 13:58:31 +0000 (Mon, 01 Nov 2004) szeiger $
 */

public enum Alignment
{
  LEFT (SWT.LEFT), CENTER (SWT.CENTER), RIGHT (SWT.RIGHT);
  
  public final int style;
  Alignment(int style) { this.style = style; }
}
