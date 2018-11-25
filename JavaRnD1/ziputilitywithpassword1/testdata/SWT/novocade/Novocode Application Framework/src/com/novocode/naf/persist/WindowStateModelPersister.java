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

package com.novocode.naf.persist;

import org.eclipse.swt.graphics.Rectangle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.novocode.naf.app.NAFException;
import com.novocode.naf.model.IWindowStateModel;


/**
 * Persister for string models.
 *
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Oct 23, 2004
 * @version $Id: WindowStateModelPersister.java 271 2004-12-16 14:52:05 +0000 (Thu, 16 Dec 2004) szeiger $
 */

public class WindowStateModelPersister<T extends IWindowStateModel> implements IPersister<T>
{
  public void toModel(Element e, T model) throws NAFException
  {
    String stateStr = e.getAttribute("state");
    int state;
    if("maximized".equals(stateStr)) state = IWindowStateModel.STATE_MAXIMIZED;
    else if("minimized-after-maximized".equals(stateStr)) state = IWindowStateModel.STATE_MINIMIZED_AFTER_MAXIMIZED;
    else if("minimized-after-pluralized".equals(stateStr)) state = IWindowStateModel.STATE_MINIMIZED_AFTER_PLURALIZED;
    else if("pluralized".equals(stateStr)) state = IWindowStateModel.STATE_PLURALIZED;
    else state = IWindowStateModel.STATE_DEFAULTBOUNDS;
    model.setState(state);
    if(state != IWindowStateModel.STATE_DEFAULTBOUNDS)
    {
      int x = Integer.parseInt(e.getAttribute("x"));
      int y = Integer.parseInt(e.getAttribute("y"));
      int w = Integer.parseInt(e.getAttribute("w"));
      int h = Integer.parseInt(e.getAttribute("h"));
      Rectangle bounds = new Rectangle(x, y, w, h);
      model.setPluralizedBounds(bounds);
    }
  }


  public Element toDOM(Document doc, T model) throws NAFException
  {
    Element e = doc.createElement("window");
    int state = model.getState();
    String stateStr;
    switch(state)
    {
      case IWindowStateModel.STATE_MAXIMIZED: stateStr = "maximized"; break;
      case IWindowStateModel.STATE_MINIMIZED_AFTER_MAXIMIZED: stateStr = "minimized-after-maximized"; break;
      case IWindowStateModel.STATE_MINIMIZED_AFTER_PLURALIZED: stateStr = "minimized-after-pluralized"; break;
      case IWindowStateModel.STATE_PLURALIZED: stateStr = "pluralized"; break;
      case IWindowStateModel.STATE_DEFAULTBOUNDS: stateStr = "defaultbounds"; break;
      default: throw new NAFException("Unknown IWindowModel state value "+state);
    }
    e.setAttribute("state", stateStr);
    if(state != IWindowStateModel.STATE_DEFAULTBOUNDS)
    {
      Rectangle bounds = model.getPluralizedBounds();
      if(bounds != null)
      {
        e.setAttribute("x", String.valueOf(bounds.x));
        e.setAttribute("y", String.valueOf(bounds.y));
        e.setAttribute("w", String.valueOf(bounds.width));
        e.setAttribute("h", String.valueOf(bounds.height));
      }
    }
    return e;
  }
}
