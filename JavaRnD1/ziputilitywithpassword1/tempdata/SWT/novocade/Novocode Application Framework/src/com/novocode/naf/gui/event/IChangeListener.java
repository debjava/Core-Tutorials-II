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

import java.util.EventListener;


/**
 * A listener which listens for ChangeEvents.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 2, 2003
 */

public interface IChangeListener extends EventListener
{
  public void stateChanged(ChangeEvent e);
}
