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

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.novocode.naf.app.*;
import com.novocode.naf.model.IBooleanReadModel;
import com.novocode.naf.resource.*;


/**
 * A CoolBar widget.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Feb 16, 2004
 */

public final class NGCoolBar extends NGWidget
{
  private static final Logger LOGGER = LoggerFactory.getLogger(NGCoolBar.class);

  private boolean resizable, border, flat, recursivePopup;


  @ConfProperty public void setResizable(boolean b) { this.resizable = b; }
  public boolean isResizable() { return resizable; }

  
  @ConfProperty public void setBorder(boolean b) { this.border = b; }
  public boolean getBorder() { return border; }

  
  @ConfProperty public void setFlat(boolean b) { this.flat = b; }
  public boolean isFlat() { return flat; }

  
  @ConfProperty public void setRecursivePopup(boolean b) { this.recursivePopup = b; }
  public boolean getRecursivePopup() { return recursivePopup; }

  
  public Control createControl(Composite parent, NGComponent parentComp, ShellWindowInstance wi, WidgetData pwd) throws NAFException
  {
    int style = SWT.NONE;
    if(border) style |= SWT.BORDER;
    if(flat) style |= SWT.FLAT;
    final CoolBar bar = new CoolBar(parent, style);
    return bar;
  }
  

  protected void configureControl(final Control control, final ShellWindowInstance wi, WidgetData wd, WidgetData pwd)
  {
    super.configureControl(control, wi, wd, pwd);
    final CoolBar bar = (CoolBar)control;

    /* [TODO] Implement chevron dropdown for resizable=true
     * as shown in http://dev.eclipse.org/viewcvs/index.cgi/~checkout~/platform-swt-home/snippits/snippet140.html */
    List<NGWidget> chlist = getExpandedRoleChildren(null, wi.models);
    for(NGWidget ch : chlist)
    {
      CoolItem item = new CoolItem(bar, resizable ? SWT.DROP_DOWN : SWT.NONE);
      wd.attachTo(item);
      Control itemControl = ch.createAndConfigureControl(bar, this, wi);
      item.setControl(itemControl);
      itemControl.pack();
      Point size = itemControl.getSize();
      Point itemSize = item.computeSize(size.x, size.y);
      item.setSize(itemSize);
      if(!resizable) item.setMinimumSize(size);
    }

    bar.pack();
    final Point oldSize = bar.computeSize(SWT.DEFAULT, SWT.DEFAULT);
    bar.addListener(SWT.Resize, new Listener()
    {
      public void handleEvent(Event event)
      {
        Point newSize = bar.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        if(newSize.y != oldSize.y)
        {
          if(LOGGER.isDebugEnabled()) LOGGER.debug("Preferred size: "+newSize+"; Actual size: "+bar.getSize());
          bar.getParent().layout(true);
        }
        //oldSize.x = newSize.x;
        oldSize.y = newSize.y;
      }
    });

    hookUpReadModel(Boolean.TYPE, getModel("locked", wi.models, IBooleanReadModel.class), bar, "setLocked", "getLocked");
  }
}
