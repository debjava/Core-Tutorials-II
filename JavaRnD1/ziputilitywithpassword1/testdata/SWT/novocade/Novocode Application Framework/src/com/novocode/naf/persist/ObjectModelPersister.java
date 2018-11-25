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

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.novocode.naf.app.NAFException;
import com.novocode.naf.model.IObjectModel;
import com.novocode.naf.xml.DOMUtil;


/**
 * Persister for object models which uses Java 1.4's bean persistence layer.
 *
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Oct 22, 2004
 * @version $Id: ObjectModelPersister.java 395 2008-04-07 18:54:39Z szeiger $
 */

public class ObjectModelPersister<T extends IObjectModel<S>, S> implements IPersister<T>
{
  @SuppressWarnings("unchecked")
  public void toModel(Element e, T model) throws NAFException
  {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    DOMUtil.dumpNode(e, out);
    ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
    XMLDecoder dec = new XMLDecoder(in);
    S value = (S)dec.readObject();
    model.setValue(value);
  }


  public Element toDOM(Document doc, T model) throws NAFException
  {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    XMLEncoder enc = new XMLEncoder(out);
    enc.writeObject(model.getValue());
    enc.close();
    ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
    Element e = DOMUtil.parseDocument(in).getDocumentElement();
    return (Element)doc.importNode(e, true);
  }
}
