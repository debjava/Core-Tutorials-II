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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.novocode.naf.app.NAFException;


/**
 * A Persister can convert models to and from XML.
 *
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Oct 13, 2004
 * @version $Id: IPersister.java 271 2004-12-16 14:52:05 +0000 (Thu, 16 Dec 2004) szeiger $
 */

public interface IPersister<T>
{
  public abstract void toModel(Element e, T model) throws NAFException;


  public abstract Element toDOM(Document doc, T model) throws NAFException;
}
