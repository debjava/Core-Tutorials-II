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

package com.novocode.naf.app;


/**
 * Exception thrown by various NAF classes.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Nov 22, 2003
 */

public class NAFException extends RuntimeException
{
  private static final long serialVersionUID = 1;


  /**
   * Create a new NAFException without details.
   */

  public NAFException()
  {
    super();
  }


  /**
   * Create a new NAFException with the specified message.
   * 
   * @param message Message
   */

  public NAFException(String message)
  {
    super(message);
  }


  /**
   * Create a new NAFException with the specified cause.
   * 
   * @param cause Cause
   */

  public NAFException(Throwable cause)
  {
    super(cause);
  }


  /**
   * Create a new NAFException with the specified message and cause.
   * 
   * @param message Message
   * @param cause Cause
   */

  public NAFException(String message, Throwable cause)
  {
    super(message, cause);
  }
}
