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

package com.novocode.naf.gui.event;


/**
 * A source for ActionEvents.
 *
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Nov 17, 2004
 * @version $Id: IActionSource.java 233 2004-11-17 00:28:57 +0000 (Wed, 17 Nov 2004) szeiger $
 */

public interface IActionSource
{
  public void addActionListener(IActionListener listener);
  public void removeActionListener(IActionListener listener);
}
