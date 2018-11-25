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

package com.novocode.naf.resource;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import com.novocode.naf.app.*;
import com.novocode.naf.model.ModelMap;


/**
 * A component that includes another component into the current list.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Nov 25, 2003
 */

public class NGInclude extends NGComponent
{
  private String ref;
  private URL refURL;


  @ConfProperty(value = "ref", required = true)
  public void setReference(String s)
  {
    ref = s;
    try { refURL = new URL(getResource().getURL(), ref); }
    catch(MalformedURLException ex)
    {
      throw new NAFException("Error creating URL object for reference \""+ref+"\"", ex);
    }
  }

  public String getReference() { return ref; }


  @Override
  public void expandSelf(List<? super NGComponent> l, ModelMap models, boolean expandDynamicGroups) throws NAFException
  {
    Object res = getResource().getResourceManager().getObject(refURL);
    if(res == null) throw new NAFException("Error resolving reference \""+ref+"\"");
    if(!(res instanceof NGComponent))
      throw new NAFException("Reference \""+ref+"\" resolves to object of type "+res.getClass().getName()+" instead of NGComponent");
    ((NGComponent)res).expandSelf(l, models, expandDynamicGroups);
  }
}
