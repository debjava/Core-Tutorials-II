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

package com.novocode.naf.gui;

import com.novocode.naf.app.NAFApplication;
import com.novocode.naf.app.NAFException;
import com.novocode.naf.model.ModelMap;


/**
 * This interface is implemented by NGWidgets which can create a WindowInstance.
 *
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 6, 2004
 * @version $Id: IWindowInstanceWidget.java 294 2005-01-14 21:14:50 +0000 (Fri, 14 Jan 2005) szeiger $
 */

public interface IWindowInstanceWidget
{
  public WindowInstance createWindowInstance(NAFApplication app, ModelMap models, WindowInstance parent) throws NAFException;
}
