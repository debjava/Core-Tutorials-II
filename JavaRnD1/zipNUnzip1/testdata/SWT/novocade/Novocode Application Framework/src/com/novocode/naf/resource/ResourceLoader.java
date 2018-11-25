/*******************************************************************************
 * Copyright (c) 2008 Stefan Zeiger and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.novocode.com/legal/epl-v10.html
 * 
 * Contributors:
 *     Stefan Zeiger (szeiger@novocode.com) - initial API and implementation
 *******************************************************************************/

package com.novocode.naf.resource;

import java.net.URL;

import com.novocode.naf.app.NAFException;


/**
 * Interface to be implemented by classes which load resources.
 *
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Apr 10, 2008
 * @version $Id: ResourceLoader.java 397 2008-04-10 12:50:40Z szeiger $
 */

public interface ResourceLoader
{
  public Resource readResource(URL url, ResourceManager resourceManager) throws NAFException;
}
