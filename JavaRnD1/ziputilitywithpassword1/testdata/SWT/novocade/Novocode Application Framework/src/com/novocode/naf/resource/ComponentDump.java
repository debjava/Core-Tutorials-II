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

package com.novocode.naf.resource;

import java.io.PrintStream;
import java.io.PrintWriter;


/**
 * Contains utility methods to print a dump of a node tree.
 *
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Mar 28, 2008
 * @version $Id$
 */

public class ComponentDump
{
  private ComponentDump() {}


  public static void dump(NGNode n, PrintStream out, String indent)
  {
    PrintWriter wr = new PrintWriter(out);
    try { dump(n, wr, indent); } finally { wr.flush(); }
  }


  public static void dump(NGNode n, PrintWriter out, String indent)
  {
    String cln = n.getClass().getName();
    int sep = cln.lastIndexOf('.');
    if(sep != -1) cln = cln.substring(sep+1);
    out.print(indent+cln);
    boolean first = true;
    StringBuffer b = new StringBuffer();
    out.print(b);
    for(ConfPropertyDescriptor mp : ConfPropertyManager.forClass(n.getClass()).getAttributePropertyDescriptors())
    {
      if(mp.getReadMethod() == null) continue;
      String pval = mp.getSimple(n);
      if(pval == null) continue;
      String pn = mp.getXMLPropertyName();
      if(pn.equals(":resource") || pn.equals(":elementName")) continue;
      out.print(first ? ':' : ',');
      first = false;
      out.print(' '+pn+'='+pval);
    }
    out.println();
    for(NGNode ch : n.getChildren()) dump(ch, out, indent + "    ");
  }
}
