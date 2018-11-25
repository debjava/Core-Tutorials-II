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

package com.novocode.naf.model;

import java.lang.annotation.*;


/**
 * Marks fields which can be added automatically to a ModelMap.
 *
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Apr 4, 2008
 * @version $Id: ModelField.java 390 2008-04-05 22:14:46Z szeiger $
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ModelField
{
  // The id of the model; the method name is used if set to null or ""
  String value() default "";
}
