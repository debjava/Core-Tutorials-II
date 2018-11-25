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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.*;

import com.novocode.naf.app.*;
import com.novocode.naf.data.Shadow;
import com.novocode.naf.resource.*;
import com.novocode.naf.swt.custom.SizeGrip;


/**
 * A size grip control.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Mar 8, 2004
 */

public final class NGSizeGrip extends NGWidget
{
  private Shadow shadow = Shadow.NONE;
  private boolean flat;


  @ConfProperty
  public void setShadow(Shadow s)
  {
    s.checkStyleMask(SWT.SHADOW_NONE|SWT.SHADOW_IN, false);
    this.shadow = s;
  }
  public Shadow getShadow() { return shadow; }


  @ConfProperty
  public void setFlat(boolean b) { this.flat = b; }
  public boolean isFlat() { return flat; }


  public Control createControl(final Composite parent, NGComponent parentComp, ShellWindowInstance wi, WidgetData pwd) throws NAFException
  {
    final SizeGrip g = new SizeGrip(parent, (flat ? SWT.FLAT : SWT.NONE) | shadow.style);

    g.getShell().addListener(SWT.Show, new Listener()
    {
      public void handleEvent(Event event)
      {
        // If shell has an enforced minimum size, enforce it also in the SizeGrip
        Point shellMinSize =  g.getShell().getMinimumSize();
        if(shellMinSize != null) g.setMinimumShellSize(shellMinSize);
      }
    });

    return g;
  }
}
