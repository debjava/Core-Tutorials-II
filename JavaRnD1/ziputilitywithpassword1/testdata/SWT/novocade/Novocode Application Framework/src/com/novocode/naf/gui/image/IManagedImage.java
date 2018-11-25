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

package com.novocode.naf.gui.image;

import org.eclipse.swt.graphics.Image;

import com.novocode.naf.app.NAFException;


/**
 * A wrapper for SWT Image objects with reference counting.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Apr 13, 2004
 */

public interface IManagedImage
{
  public String getName();

  public Image acquire() throws NAFException;

  public void release() throws NAFException;
}