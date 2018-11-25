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

package com.novocode.naf.gui.event;

import java.lang.annotation.*;


/**
 * Marks methods for which ActionListeners can be created automatically
 * with an ActionMethodMapper.
 *
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 19, 2004
 * @version $Id: ActionMethod.java 285 2004-12-19 10:56:39 +0000 (Sun, 19 Dec 2004) szeiger $
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ActionMethod
{
  // The id of the model; the method name is used if set to null or ""
  String value() default "";
}
