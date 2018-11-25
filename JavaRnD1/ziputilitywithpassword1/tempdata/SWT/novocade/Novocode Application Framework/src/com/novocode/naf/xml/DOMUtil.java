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

package com.novocode.naf.xml;

import java.io.*;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;
import org.xml.sax.InputSource;

import com.novocode.naf.app.NAFException;


/**
 * Contains helper methods for DOM manipulation.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Jan 11, 2004
 * @version $Id: DOMUtil.java 227 2004-11-10 13:37:01 +0000 (Wed, 10 Nov 2004) szeiger $
 */

public final class DOMUtil
{
  private static final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
  private static final DocumentBuilder docbuilder;
  static
  {
    try
    {
      factory.setNamespaceAware(true);
      factory.setIgnoringComments(true);
      docbuilder = factory.newDocumentBuilder();
    }
    catch(ParserConfigurationException ex)
    {
      throw new NAFException("Error configuring XML parser", ex);
    }
  }

  private static final TransformerFactory tFactory = TransformerFactory.newInstance();
  private static final Transformer transformer;
  static
  {
    try
    {
      //transformer = tFactory.newTransformer(new StreamSource(new StringReader(indentTransformerXslt)));
      transformer = tFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty(OutputKeys.METHOD, "xml");
    }
    catch(TransformerConfigurationException ex)
    {
      throw new NAFException("Error configuring XML transformer", ex);
    }
  }

  
  private DOMUtil() {}
  
  
  public static Element addTextElement(Document doc, Node parent, String name, String value)
  {
    Element e = createTextElement(doc, name, value);
    parent.appendChild(e);
    return e;
  }


  public static Element createTextElement(Document doc, String name, String value)
  {
    Element e = doc.createElement(name);
    if(value != null && value.length() != 0) e.appendChild(doc.createTextNode(value));
    return e;
  }
  
  
  public static String getText(Element e)
  {
    if(e.getFirstChild() == null) return null;
    else return e.getTextContent();
  }


  public static void dumpNode(Node n, OutputStream out) throws NAFException
  {
    try
    {
      StreamResult result = new StreamResult(out);
      DOMSource source = new DOMSource(n);
      transformer.transform(source, result);
    }
    catch(TransformerException ex)
    {
      throw new NAFException("Error dumping DOM node: "+ex, ex);
    }
  }
  
  
  public static void dumpNode(Node n, Writer out) throws NAFException
  {
    try
    {
      StreamResult result = new StreamResult(out);
      DOMSource source = new DOMSource(n);
      transformer.transform(source, result);
    }
    catch(TransformerException ex)
    {
      throw new NAFException("Error dumping DOM node: "+ex, ex);
    }
  }
  
  
  public static Document newDocument()
  {
    return docbuilder.newDocument();
  }
  
  
  public static Document parseDocument(InputStream in) throws NAFException
  {
    try
    {
      return docbuilder.parse(in);
    }
    catch(Exception ex) { throw new NAFException("Error parsing XML document", ex); }
  }
  
  
  public static Document parseDocument(String s) throws NAFException
  {
    try
    {
      return docbuilder.parse(new InputSource(new StringReader(s)));
    }
    catch(Exception ex) { throw new NAFException("Error parsing XML document", ex); }
  }
  
  
  public static Map<String, String> parseProcessingInstructionAttributes(ProcessingInstruction pi) throws NAFException
  {
    HashMap<String, String> attrs = new HashMap<String, String>();
    String s = pi.getData().trim();
    while(s.length() > 0)
    {
      int idx = s.indexOf('=');
      if(idx == -1) break;
      String key = s.substring(0, idx).trim();
      if(s.length() <= idx+1)
        throw new NAFException("Error parsing processing instruction attributes: Attribute value is missing");
      if(s.charAt(idx+1) != '"')
        throw new NAFException("Error parsing processing instruction attributes: Attribute value is not quoted");
      int end = s.indexOf('"', idx+2);
      if(end == -1)
        throw new NAFException("Error parsing processing instruction attributes: Attribute value end quote is missing");
      attrs.put(key, s.substring(idx+2, end));
      s = s.substring(end+1);
    }
    return attrs;
  }
}
