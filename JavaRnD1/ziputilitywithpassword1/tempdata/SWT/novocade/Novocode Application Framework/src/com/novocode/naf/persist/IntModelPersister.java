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
import com.novocode.naf.model.IIntModel;
import com.novocode.naf.xml.DOMUtil;


/**
 * Persister for int models.
 *
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Oct 24, 2004
 * @version $Id: IntModelPersister.java 271 2004-12-16 14:52:05 +0000 (Thu, 16 Dec 2004) szeiger $
 */

public class IntModelPersister<T extends IIntModel> implements IPersister<T>
{
  public void toModel(Element e, T model) throws NAFException
  {
    model.setInt(Integer.parseInt(DOMUtil.getText(e)));
  }


  public Element toDOM(Document doc, T model) throws NAFException
  {
    return DOMUtil.createTextElement(doc, "int", String.valueOf(model.getInt()));
  }
}
