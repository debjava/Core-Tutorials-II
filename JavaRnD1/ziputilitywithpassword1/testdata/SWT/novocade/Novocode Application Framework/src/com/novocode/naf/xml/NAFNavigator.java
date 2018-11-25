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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.jaxen.DefaultNavigator;
import org.jaxen.XPath;
import org.jaxen.saxpath.SAXPathException;
import org.jaxen.util.SingleObjectIterator;

import com.novocode.naf.resource.NGNode;


/**
 * A Navigator implementation which allows Jaxen to traverse
 * NGNode hierarchies.
 *
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Nov 17, 2004
 * @version $Id: NAFNavigator.java 389 2008-04-05 16:38:54Z szeiger $
 */

public class NAFNavigator extends DefaultNavigator
{
  private static final long serialVersionUID = 3690194356430714930L;
  private static final NAFNavigator instance = new NAFNavigator();


  public static final class Attr
  {
    public final NGNode parent;
    public final String name;
    public final String value;

    Attr(NGNode parent, String name, String value)
    {
      this.parent = parent;
      this.name = name;
      this.value = value;
    }

    public boolean equals(Object o)
    {
      if(!(o instanceof Attr)) return false;
      Attr a = (Attr)o;
      return parent == a.parent && name.equals(a.name);
    }

    public int hashCode()
    {
      return parent.hashCode() + name.hashCode();
    }

    public String toString()
    {
      return name+"=\""+value+"\"";
    }
  }


  public static final class Document
  {
    public final NGNode root;

    Document(NGNode root) { this.root = root; }

    public boolean equals(Object o)
    {
      if(!(o instanceof Document)) return false;
      return root == ((Document)o).root;
    }

    public int hashCode() { return root.hashCode(); }

    public String toString() { return "Resource " + root.getResource().getURL(); }
  }


  private NAFNavigator() {}


  public static NAFNavigator getInstance() { return instance; }


  @Override
  public String getElementNamespaceUri(Object o)
  {
    return null;
  }


  @Override
  public String getElementName(Object o)
  {
    String s = ((NGNode)o).getElementName();
    if(s == null)
    {
      s = o.getClass().getName();
      int i = s.lastIndexOf('.');
      if(i != -1) s = s.substring(i+1);
    }
    return s;
  }


  @Override
  public String getElementQName(Object o)
  {
    return getElementName(o);
  }


  @Override
  public String getAttributeNamespaceUri(Object o)
  {
    return null;
  }


  @Override
  public String getAttributeName(Object o)
  {
    return ((Attr)o).name;
  }


  @Override
  public String getAttributeQName(Object o)
  {
    return getAttributeName(o);
  }


  @Override
  public boolean isDocument(Object o)
  {
    return o instanceof Document;
  }


  @Override
  public boolean isElement(Object o)
  {
    return o instanceof NGNode;
  }


  @Override
  public boolean isAttribute(Object o)
  {
    return o instanceof Attr;
  }


  @Override
  public boolean isNamespace(Object o)
  {
    return false;
  }


  @Override
  public boolean isComment(Object o)
  {
    return false;
  }


  @Override
  public boolean isText(Object o)
  {
    return false;
  }


  @Override
  public boolean isProcessingInstruction(Object o)
  {
    return false;
  }


  @Override
  public String getCommentStringValue(Object o)
  {
    return null;
  }


  @Override
  public String getElementStringValue(Object o)
  {
    return "";
  }


  @Override
  public String getAttributeStringValue(Object o)
  {
    return ((Attr)o).value;
  }


  public String getNamespaceStringValue(Object o)
  {
    return null;
  }


  @Override
  public String getTextStringValue(Object o)
  {
    return null;
  }


  @Override
  public String getNamespacePrefix(Object o)
  {
    return null;
  }


  @Override
  public XPath parseXPath(String s) throws SAXPathException
  {
    return new NAFXPath(s);
  }


  @Override
  public Object getDocumentNode(Object o)
  {
    if(o instanceof Document) return o;
    NGNode n;
    if(o instanceof Attr) n = ((Attr)o).parent;
    else n = (NGNode)o;
    if(n == null) return null;
    while(n.getParent() != null) { n = n.getParent(); }
    return new Document(n);
  }


  @Override
  public Iterator<?> getAttributeAxisIterator(Object o)
  {
    if(!(o instanceof NGNode)) return null;
    NGNode c = (NGNode)o;
    List<Attr> l = new ArrayList<Attr>();
    String id = c.getID();
    if(id != null) l.add(new Attr(c, "id", id));
    // [TODO] Add "role" and other attributes
    return l.iterator();
  }


  @Override
  public Iterator<?> getChildAxisIterator(Object o)
  {
    if(o instanceof Document) return new SingleObjectIterator(((Document)o).root);
    if(!(o instanceof NGNode)) return null;
    NGNode c = (NGNode)o;
    List<NGNode> l = new ArrayList<NGNode>();
    l.addAll(Arrays.asList(c.getChildren()));
    return l.iterator();
  }


  @Override
  public Iterator<?> getParentAxisIterator(Object o)
  {
    if(o instanceof Attr) return new SingleObjectIterator(((Attr)o).parent);
    if(o instanceof NGNode)
    {
      NGNode c = (NGNode)o;
      if(c.getParent() != null) return new SingleObjectIterator(c.getParent());
      else return new SingleObjectIterator(new Document(c));
    }
    return null;
  }
}
