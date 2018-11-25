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

import java.lang.annotation.Annotation;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.novocode.naf.app.NAFException;
/*import com.novocode.naf.model.IIntArrayModel;
import com.novocode.naf.model.IIntModel;
import com.novocode.naf.model.IObjectModel;
import com.novocode.naf.model.IStringModel;
import com.novocode.naf.model.IWindowStateModel;*/
import com.novocode.naf.model.ModelMap;
import com.novocode.naf.resource.xml.XMLResourceLoader;
import com.novocode.naf.xml.DOMUtil;


/**
 * Reads and writes persistent data of models.
 *
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Oct 17, 2004
 * @version $Id: PersistenceManager.java 397 2008-04-10 12:50:40Z szeiger $
 */

public final class PersistenceManager
{
  private static final Logger LOGGER = LoggerFactory.getLogger(PersistenceManager.class);


  @SuppressWarnings("unchecked")
  public <T> Document createDOM(ModelMap models) throws NAFException
  {
    Document doc = DOMUtil.newDocument();
    Element rootE = doc.createElementNS(XMLResourceLoader.NAF_NAMESPACE_URI, "naf:persist");
    doc.appendChild(rootE);
    for(Map.Entry<String, Object> entry : models.entrySet())
    {
      String modelName = entry.getKey();
      T model = (T)entry.getValue();
      Class<?> modelClass = model.getClass();
      IPersister<T> p = persisterFor(model);
      if(p == null) throw new NAFException("No persister found for class "+modelClass.getName());
      Element contentE = p.toDOM(doc, model);
      Element wrapE = doc.createElementNS(XMLResourceLoader.NAF_NAMESPACE_URI, "naf:model");
      rootE.appendChild(wrapE);
      wrapE.appendChild(contentE);
      wrapE.setAttribute("id", modelName);
      //wrapE.setAttribute("class", modelClass.getName());
      String persisterClassName = p.getClass().getName();
      if(persisterClassName.startsWith("com.novocode.naf.persist."))
        persisterClassName = persisterClassName.substring(24);
      wrapE.setAttribute("persister", persisterClassName);
    }
    return doc;
  }


  @SuppressWarnings("unchecked")
  public <T> void restoreFromDOM(ModelMap models, Document doc) throws NAFException
  {
    Element rootE = doc.getDocumentElement();
    if(!"persist".equals(rootE.getLocalName()) || !XMLResourceLoader.NAF_NAMESPACE_URI.equals(rootE.getNamespaceURI()))
      throw new NAFException("Cannot restore from DOM. Supplied Document does not contain persisted NAF data");
    for(Node n = rootE.getFirstChild(); n != null; n = n.getNextSibling())
    {
      if(n.getNodeType() != Node.ELEMENT_NODE
        || !XMLResourceLoader.NAF_NAMESPACE_URI.equals(rootE.getNamespaceURI())
        || !"model".equals(n.getLocalName()))
        continue;
      Element e = (Element)n;
      String modelName = e.getAttribute("id");
      T model = models.get(modelName);
      if(model == null) continue;
      //String modelClass = e.getAttribute("class");
      String persisterClassName = e.getAttribute("persister");
      if(persisterClassName.startsWith("."))
        persisterClassName = "com.novocode.naf.persist" + persisterClassName;
      IPersister<T> p;
      try
      {
        Class<IPersister<T>> cl = (Class<IPersister<T>>)Class.forName(persisterClassName);
        p = cl.newInstance();
      }
      catch(Exception ex)
      {
        throw new NAFException("Error instantiating Persister class "+persisterClassName+": "+ex, ex);
      }
      Element childE = null;
      for(Node nn = e.getFirstChild(); nn != null; nn = nn.getNextSibling())
      {
        if(nn.getNodeType() == Node.ELEMENT_NODE)
        {
          childE = (Element)nn;
          break;
        }
      }
      if(childE == null)
        throw new NAFException("Cannot read persistent data for model "+modelName+": No data element found");
      p.toModel(childE, model);
    }
  }


  @SuppressWarnings("unchecked")
  public <T> IPersister<T> persisterFor(T model) throws NAFException
  {
    PersisterClass p = getTransitiveAnnotation(model.getClass(), PersisterClass.class);
    if(p == null) return null;
    if(LOGGER.isDebugEnabled())
      LOGGER.debug("Found @Persister("+p.value().getName()+") for "+model.getClass().getName());
    try { return p.value().newInstance(); }
    catch(Exception ex) { throw new NAFException("Error instantiating persister", ex); }

    /*if(model instanceof IIntArrayModel) return new IntArrayModelPersister();
    else if(model instanceof IStringModel) return new StringModelPersister();
    else if(model instanceof IIntModel) return new IntModelPersister();
    else if(model instanceof IWindowStateModel) return new WindowStateModelPersister();
    else if(model instanceof IObjectModel) return new ObjectModelPersister();
    else return null;*/
  }


  private static <A extends Annotation> A getTransitiveAnnotation(Class<?> type, Class<A> annotationClass)
  {
    A a = type.getAnnotation(annotationClass);
    if(a != null) return a;
    for(Class<?> iface : type.getInterfaces())
    {
      a = getTransitiveAnnotation(iface, annotationClass);
      if(a != null) return a;
    }
    Class<?> superClass = type.getSuperclass();
    if(superClass != null)
    {
      a = getTransitiveAnnotation(superClass, annotationClass);
      if(a != null) return a;
    }
    return null;
  }
}
