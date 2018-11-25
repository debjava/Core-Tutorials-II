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

package com.novocode.naf.resource;

import java.lang.annotation.*;


/**
 * Marks setter methods for properties which can be set by
 * attributes in NAF resource files.
 *
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 8, 2004
 * @version $Id: ConfProperty.java 398 2008-04-23 16:40:20Z szeiger $
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ConfProperty
{
  /**
   * The XML property name. If this is not set, the JavaBeans property
   * name will be used.
   */
  String value() default "";

  /**
   * The factory for creating a ConfigurableObject. If this is set to the
   * default value ConfigurableFactory.class, the property component type
   * is instantiated directly instead of using a factory.
   */
  Class<? extends ConfigurableFactory> factory() default ConfigurableFactory.class;

  /**
   * The attribute prefix for adding key/value pairs directly to a Properties
   * object. If this is set, there is no fallback to the JavaBeans property
   * name for value(). If value() is not also set, only the prefix mapping is
   * done for this property.
   */
  String prefix() default "";

  /**
   * The name of the "add" method for this property. If unspecified, the name
   * is formed automatically by replacing the initial "set" or "get" from the
   * setter or getter method (whichever is available) by "add" and removing
   * a trailing "s".
   */
  String adder() default "";

  /**
   * Indicates that this property must be set. This is only supported for named
   * simple properties (no prefix properties; no compound properties).
   */
  boolean required() default false;
}
