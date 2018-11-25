/*******************************************************************************
 * Copyright (c) 2008 Stefan Zeiger and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.novocode.com/legal/epl-v10.html
 * 
 * Contributors:
 *     Stefan Zeiger (szeiger@novocode.com) - initial API and implementation
 *******************************************************************************/

package com.novocode.naf.gui;

import java.util.LinkedList;
import java.util.StringTokenizer;

import com.novocode.naf.app.*;
import com.novocode.naf.resource.*;


/**
 * Base class for widgets that can contain text inside the element tag.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Mar 26, 2008
 * @version $Id$
 */

public abstract class NGTextContainer extends NGWidget
{
  protected LinkedList<String> childTokens;
  private String text, cachedText;
  private boolean initialized;


  public static final class BR implements ConfigurableObject
  {
  }


  protected final String getCachedText() throws NAFException
  {
    if(!initialized)
    {
      if(text != null) cachedText = text;
      else if(childTokens != null)
      {
        StringBuffer b = new StringBuffer();
        for(String s : childTokens) b.append(s);
        cachedText = b.toString().trim().replace((char)160,' ');
        if(cachedText.length() == 0) cachedText = null;
      }
      initialized = true;
    }
    return cachedText;
  }


  @ConfProperty(":text")
  public void setTextContent(String[] a) { for(String s : a) addTextContent(s); }
  public String[] getTextContent() { return childTokens == null ? null : childTokens.toArray(new String[childTokens.size()]); }
  public void addTextContent(String text)
  {
    text = text.replace('\r', ' ').replace('\n', ' ').replace('\t', ' ');
    if(childTokens == null) childTokens = new LinkedList<String>();
    else if(text.startsWith(" ") && !childTokens.isEmpty())
    {
      String prev = childTokens.getLast();
      if(!" ".equals(prev) && !"\n".equals(prev)) childTokens.add(" ");
    }
    boolean first = true;
    for(StringTokenizer tok = new StringTokenizer(text); tok.hasMoreTokens();)
    {
      String s = tok.nextToken();
      if(s.length() == 0) continue;
      if(!first) childTokens.add(" ");
      childTokens.add(s);
      first = false;
    }
    if(text.endsWith(" ")) childTokens.add(" ");
  }


  @ConfProperty(value = "br")
  public BR[] getBRs() { return null; }
  public void addBR(BR br)
  {
    if(childTokens == null) childTokens = new LinkedList<String>();
    else if(childTokens.size() != 0 && " ".equals(childTokens.getLast())) childTokens.removeLast();
    childTokens.add("\n");
  }


  @ConfProperty
  public void setText(String text) { this.text = text; }
  public String getText() { return text; }
}
