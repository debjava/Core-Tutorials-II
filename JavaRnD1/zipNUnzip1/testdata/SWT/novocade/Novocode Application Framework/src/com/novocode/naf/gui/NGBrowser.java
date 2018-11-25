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
import org.eclipse.swt.browser.*;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.novocode.naf.app.*;
import com.novocode.naf.gui.event.ActionEvent;
import com.novocode.naf.gui.event.ChangeEvent;
import com.novocode.naf.gui.event.IActionListener;
import com.novocode.naf.gui.event.IActionSource;
import com.novocode.naf.gui.event.IChangeListener;
import com.novocode.naf.model.DefaultStringModel;
import com.novocode.naf.model.IBooleanModifyModel;
import com.novocode.naf.model.IObjectModifyModel;
import com.novocode.naf.model.IObjectReadModel;
import com.novocode.naf.model.IProgressModifyModel;
import com.novocode.naf.resource.*;


/**
 * A Browser widget.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Oct 24, 2004
 * @version $Id: NGBrowser.java 417 2008-05-06 22:27:03Z szeiger $
 */

public final class NGBrowser extends NGWidget
{
  private boolean border;

  
  @ConfProperty public void setBorder(boolean b) { this.border = b; }
  public boolean getBorder() { return border; }

  
  @SuppressWarnings("unchecked")
  public Control createControl(Composite parent, NGComponent parentComp, ShellWindowInstance wi, WidgetData pwd) throws NAFException
  {
    Browser browser;
    
    try
    {
      browser = new Browser(parent, border ? SWT.BORDER : SWT.NONE);
    }
    catch(Throwable t)
    {
      CLabel cl = new CLabel(parent, SWT.CENTER);
      cl.setText("Browser widget is not available");
      return cl;
    }

    final Browser finalBrowser = browser;
    final IObjectReadModel<String> urlReadModel = getModel("url", wi.models, DefaultStringModel.PTYPE_READ);
    if(urlReadModel != null)
    {
      final boolean[] lock = new boolean[1];
      IChangeListener cl = new IChangeListener()
      {
        public void stateChanged(ChangeEvent e)
        {
          String url = urlReadModel.getValue();
          if(url == null) url = "";
          String oldUrl = finalBrowser.getUrl();
          if(oldUrl == null) oldUrl = "";
          if(url.equals(oldUrl)) return;
          try
          {
            lock[0] = true;
            finalBrowser.setUrl(url);
          }
          finally { lock[0] = false; }
        }
      };
      SWTUtil.registerModel(browser, urlReadModel, cl);
      cl.stateChanged(null);
      if(urlReadModel instanceof IObjectModifyModel)
      {
        final IObjectModifyModel<String> urlModifyModel = (IObjectModifyModel<String>)urlReadModel;
        browser.addLocationListener(new LocationListener()
        {
          public void changing(LocationEvent event)
          {
          }

          public void changed(LocationEvent event)
          {
            if(!event.top) return;
            if(!lock[0]) urlModifyModel.setValue(event.location);
          }
        });
      }
    }
    
    final IBooleanModifyModel backEnabledModel = getModel("back-enabled", wi.models, IBooleanModifyModel.class);
    final IBooleanModifyModel forwardEnabledModel = getModel("forward-enabled", wi.models, IBooleanModifyModel.class);
    final IBooleanModifyModel changingModel = getModel("changing", wi.models, IBooleanModifyModel.class);
    if(backEnabledModel != null || forwardEnabledModel != null || changingModel != null)
    {
      browser.addLocationListener(new LocationListener()
      {
        public void changing(LocationEvent event)
        {
          if(changingModel != null) changingModel.setBoolean(true);
          if(backEnabledModel != null) backEnabledModel.setBoolean(finalBrowser.isBackEnabled());
          if(forwardEnabledModel != null) forwardEnabledModel.setBoolean(finalBrowser.isForwardEnabled());
        }

        public void changed(LocationEvent event)
        {
          if(backEnabledModel != null) backEnabledModel.setBoolean(finalBrowser.isBackEnabled());
          if(forwardEnabledModel != null) forwardEnabledModel.setBoolean(finalBrowser.isForwardEnabled());
          if(changingModel != null) changingModel.setBoolean(false);
        }
      });
      if(backEnabledModel != null) backEnabledModel.setBoolean(finalBrowser.isBackEnabled());
      if(forwardEnabledModel != null) forwardEnabledModel.setBoolean(finalBrowser.isForwardEnabled());
      if(changingModel != null) changingModel.setBoolean(false);
    }

    final IObjectModifyModel<String> titleModifyModel = getModel("title", wi.models, DefaultStringModel.PTYPE_MODIFY);
    if(titleModifyModel != null)
    {
      browser.addTitleListener(new TitleListener()
      {
        public void changed(TitleEvent event)
        {
          titleModifyModel.setValue(event.title);
        }
      });
    }

    final IObjectModifyModel<String> statusModifyModel = getModel("status-text", wi.models, DefaultStringModel.PTYPE_MODIFY);
    if(statusModifyModel != null)
    {
      browser.addStatusTextListener(new StatusTextListener()
      {
        public void changed(StatusTextEvent event)
        {
          statusModifyModel.setValue(event.text);
        }
      });
    }

    final IProgressModifyModel progressModifyModel = getModel("progress-reset", wi.models, IProgressModifyModel.class);
    if(progressModifyModel != null)
    {
      browser.addProgressListener(new ProgressListener()
      {
        public void changed(ProgressEvent event)
        {
          progressModifyModel.setAll(0, event.total, event.current);
        }

        public void completed(ProgressEvent event)
        {
          progressModifyModel.setAll(0, 100, 0);
        }
      });
    }
    
    final IActionSource backSource = getModel("back", wi.models, IActionSource.class);
    if(backSource != null)
    {
      SWTUtil.registerModel(browser, backSource, new IActionListener()
      {
        public void performAction(ActionEvent e)
        {
          finalBrowser.back();
        }
      });
    }
    
    final IActionSource forwardSource = getModel("forward", wi.models, IActionSource.class);
    if(forwardSource != null)
    {
      SWTUtil.registerModel(browser, forwardSource, new IActionListener()
      {
        public void performAction(ActionEvent e)
        {
          finalBrowser.forward();
        }
      });
    }
    
    final IActionSource stopSource = getModel("stop", wi.models, IActionSource.class);
    if(stopSource != null)
    {
      SWTUtil.registerModel(browser, stopSource, new IActionListener()
      {
        public void performAction(ActionEvent e)
        {
          finalBrowser.stop();
        }
      });
    }
    
    final IActionSource refreshSource = getModel("refresh", wi.models, IActionSource.class);
    if(refreshSource != null)
    {
      SWTUtil.registerModel(browser, refreshSource, new IActionListener()
      {
        public void performAction(ActionEvent e)
        {
          finalBrowser.refresh();
        }
      });
    }
    
    return browser;
  }
}
