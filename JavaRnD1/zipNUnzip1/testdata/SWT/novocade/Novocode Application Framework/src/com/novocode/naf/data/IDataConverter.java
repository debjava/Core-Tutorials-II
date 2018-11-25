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

package com.novocode.naf.data;


/**
 * Classes implementing IDataConverter convert values between the internal form
 * used in the setter and getter methods of an NGComponent and the external
 * string form used in XML attributes.
 *
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Oct 6, 2004
 * @version $Id: IDataConverter.java 338 2005-06-04 19:28:23 +0000 (Sat, 04 Jun 2005) szeiger $
 */

public interface IDataConverter
{
  public abstract String toExternal(Class<?> type, Object value) throws Exception;

  public abstract Object toInternal(Class<?> type, String value) throws Exception;
}
