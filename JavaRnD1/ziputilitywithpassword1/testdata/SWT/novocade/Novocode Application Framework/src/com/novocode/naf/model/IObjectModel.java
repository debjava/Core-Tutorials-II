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

import com.novocode.naf.persist.ObjectModelPersister;
import com.novocode.naf.persist.PersisterClass;


/**
 * A model that contains an object which can be read and modified.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since May 30, 2004
 */

@PersisterClass(ObjectModelPersister.class)
public interface IObjectModel<T> extends IObjectReadModel<T>, IObjectModifyModel<T>
{
}
