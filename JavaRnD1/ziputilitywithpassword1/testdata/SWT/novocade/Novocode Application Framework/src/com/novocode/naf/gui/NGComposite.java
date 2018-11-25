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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.widgets.*;

import com.novocode.naf.app.*;
import com.novocode.naf.gui.layout.LayoutFactory;
import com.novocode.naf.gui.layout.NGFillLayout;
import com.novocode.naf.gui.layout.NGLayout;
import com.novocode.naf.resource.*;


/**
 * A composite widget (a group of controls with a layout).
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 4, 2003
 */

public class NGComposite extends NGWidget
{
  private NGLayout nglayout;


  @ConfProperty(factory = LayoutFactory.class)
  public final void setLayout(NGLayout layout) { this.nglayout = layout; }
  public final NGLayout getLayout()
  {
    if(nglayout == null) nglayout = new NGFillLayout();
    return nglayout;
  }


  @Override
  public Control createControl(Composite parent, NGComponent parentComp, ShellWindowInstance wi, WidgetData pwd) throws NAFException
  {
    Composite comp = new Composite(parent, 0 /* | SWT.NO_RADIO_GROUP */);
    return comp;
  }


  @Override
  protected void configureControl(final Control control, final ShellWindowInstance wi, WidgetData wd, WidgetData pwd)
  {
    super.configureControl(control, wi, wd, pwd);
    addDefaultChildren((Composite)control, wi, wd);
  }


  protected Map<String, Control> addDefaultChildrenStatic(Composite composite, ShellWindowInstance wi, WidgetData wd)
  {
    Map<String, Control> chmap = super.addDefaultChildrenStatic(composite, wi, wd);

    Control[] chctrls = composite.getChildren();
    NGLayout l = getLayout();
    for(int i=0; i<chctrls.length; i++)
    {
      NGWidget ch = WidgetData.forWidget(chctrls[i]).getNGWidget();
      Map<String, String> ldAttributes = new HashMap<String, String>();
      if(l.getDefaultLayoutDataAttributes() != null) ldAttributes.putAll(l.getDefaultLayoutDataAttributes());
      Map<String, String> chld = ch.getLayoutDataAttributes();
      if(chld != null) ldAttributes.putAll(chld);
      l.assignLayoutData(chctrls[i], ldAttributes, chmap, chctrls, i);
    }
    
    l.assignLayout(composite, wi);
    return chmap;
  }


  @Override
  protected final void setChildControlVisibility(Control ctrl, boolean visible, WindowInstance wi)
  {
    getLayout().setControlVisibility(ctrl, visible, wi);
  }
}
