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

package com.novocode.naf.resource;

import com.novocode.naf.app.*;


/**
 * A factory for creating ConfigurableObjects which is itself a ConfigurableObject.
 * This can be used when the exact class of a ConfigurableObject depends on a
 * configuration attribute. Only simple attributes will be set on a factory.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Mar 22, 2008
 * @version $Id: ConfigurableFactory.java 391 2008-04-07 18:43:51Z szeiger $
 */

public interface ConfigurableFactory extends ConfigurableObject
{
  public abstract Object createInstance() throws NAFException;
}
