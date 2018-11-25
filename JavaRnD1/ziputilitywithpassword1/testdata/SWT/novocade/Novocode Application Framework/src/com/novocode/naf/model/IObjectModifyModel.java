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

package com.novocode.naf.model;


/**
 * A model that contains an object which can be modified.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Apr 22 2004
 */

public interface IObjectModifyModel<T> extends IModel
{
  public void setValue(T o);
}
