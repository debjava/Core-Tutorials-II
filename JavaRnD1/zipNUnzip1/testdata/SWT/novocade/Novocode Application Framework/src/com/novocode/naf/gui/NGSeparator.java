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
import org.eclipse.swt.widgets.*;

import com.novocode.naf.app.*;
import com.novocode.naf.data.Orientation;
import com.novocode.naf.data.Shadow;
import com.novocode.naf.resource.*;
import com.novocode.naf.swt.custom.CustomSeparator;


/**
 * A separator.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 2, 2003
 */

public final class NGSeparator extends NGWidget implements IToolItemWidget, IMenuItemWidget
{
  private Orientation orientation = Orientation.VERTICAL;
  private Shadow shadow = Shadow.DEFAULT;
  private boolean nativ = true;


  @ConfProperty
  public void setOrientation(Orientation o) { this.orientation = o; }
  public Orientation getOrientation() { return orientation; }


  @ConfProperty
  public void setShadow(Shadow s)
  {
    s.checkStyleMask(SWT.SHADOW_IN|SWT.SHADOW_OUT|SWT.SHADOW_NONE, true);
    this.shadow = s;
  }
  public Shadow getShadow() { return shadow; }


  @ConfProperty
  public void setNative(boolean b) { this.nativ = b; }
  public boolean getNative() { return nativ; }


  public Control createControl(Composite parent, NGComponent parentComp, ShellWindowInstance wi, WidgetData pwd) throws NAFException
  {
    if(nativ)
      return new Label(parent, SWT.SEPARATOR | shadow.style | orientation.style);
    else
      return new CustomSeparator(parent, shadow.style | orientation.style);
  }


  public ToolItem createToolItem(NGToolBar parentComp, final ToolBar bar, ShellWindowInstance wi) throws NAFException
  {
    return new ToolItem(bar, SWT.SEPARATOR);
  }


  public MenuItem createMenuItem(Menu parent, WindowInstance wi) throws NAFException
  {
    return new MenuItem(parent, SWT.SEPARATOR);
  }
}
