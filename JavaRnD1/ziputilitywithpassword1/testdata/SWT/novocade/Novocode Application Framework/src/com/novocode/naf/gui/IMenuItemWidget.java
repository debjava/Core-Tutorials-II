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

import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import com.novocode.naf.app.NAFException;


/**
 * This interface is implemented by NGWidgets which can create a MenuItem.
 *
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Nov 17, 2004
 * @version $Id: IMenuItemWidget.java 234 2004-11-17 17:31:11 +0000 (Wed, 17 Nov 2004) szeiger $
 */

public interface IMenuItemWidget
{
  public MenuItem createMenuItem(Menu parent, WindowInstance wi) throws NAFException;
}
