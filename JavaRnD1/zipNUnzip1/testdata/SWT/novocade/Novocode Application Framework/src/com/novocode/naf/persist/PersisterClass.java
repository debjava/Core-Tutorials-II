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

import java.lang.annotation.*;


/**
 * An annotation which assigns a persister to a model class.
 *
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 20, 2004
 * @version $Id: PersisterClass.java 286 2004-12-20 16:26:48 +0000 (Mon, 20 Dec 2004) szeiger $
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PersisterClass
{
  /**
   * The persister class which is used to persist instances
   * of the annotated type.
   */
  Class<? extends IPersister> value();
}
