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

import org.eclipse.swt.widgets.*;

import com.novocode.naf.app.*;
import com.novocode.naf.data.Shadow;
import com.novocode.naf.resource.*;
import com.novocode.naf.swt.custom.FramedComposite;


/**
 * A composite widget with a border.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Feb 9, 2004
 */

public final class NGFramedComposite extends NGComposite
{
  private Shadow shadow = Shadow.ETCHED_IN;


  @ConfProperty public void setShadow(Shadow s) { this.shadow = s; }
  public Shadow getShadow() { return shadow; }


  public Control createControl(Composite parent, NGComponent parentComp, ShellWindowInstance wi, WidgetData pwd) throws NAFException
  {
    FramedComposite comp = new FramedComposite(parent, shadow.style);
    return comp;
  }
}
