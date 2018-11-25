/*******************************************************************************
 * Copyright (c) 2007 Stefan Zeiger and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.novocode.com/legal/epl-v10.html
 * 
 * Contributors:
 *     Stefan Zeiger (szeiger@novocode.com) - initial API and implementation
 *******************************************************************************/

package com.novocode.naf.gui.layout;

import com.novocode.naf.app.NAFException;
import com.novocode.naf.gui.GUIResourceManager;
import com.novocode.naf.resource.ConfigurableFactory;
import com.novocode.naf.resource.Resource;
import com.novocode.naf.resource.ConfProperty;


/**
 * The factory which is used by NGWidget to create NGLayouts.
 *
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Mar 22, 2008
 * @version $Id$
 */

public class LayoutFactory implements ConfigurableFactory
{
  private String type;
  private Resource resource;


  @ConfProperty
  public void setType(String type) { this.type = type; }
  public String getType() { return type; }


  @ConfProperty(":resource")
  public void setResource(Resource resource) { this.resource = resource; }
  public Resource getResource() { return resource; }


  @Override
  public NGLayout createInstance() throws NAFException
  {
    GUIResourceManager rm = (GUIResourceManager)resource.getResourceManager();
    NGLayout layout = rm.createLayout(type);
    return layout;
  }
}
