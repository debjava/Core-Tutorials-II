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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.novocode.naf.app.NAFException;
import com.novocode.naf.model.ModelMap;


/**
 * A node which contains other NGComponents and organizes them according to
 * their "role" property. NGComponents also know how to flatten themselves
 * to create statically and dynamically expanded lists of children.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Nov 24, 2003
 * @version $Id: NGComponent.java 401 2008-04-24 22:53:11Z szeiger $
 */

public abstract class NGComponent extends NGNode
{
  private HashMap<String, ArrayList<NGComponent>> roleChildren;
  private HashMap<String, ArrayList<NGComponent>> expandedRoleChildren;
  private HashMap<String, ArrayList<NGComponent>> expandedStaticRoleChildren;
  private String role;
  private Boolean cachedIsDynamic;

  
  @ConfProperty
  public void setRole(String s) { role = (s == null || s.length() == 0) ? null : s; }
  public String getRole() { return role; }


  public void expandSelf(List<? super NGComponent> l, ModelMap models, boolean expandDynamicGroups)
  {
    l.add(this);
  }


  @Override
  public void addChild(NGNode ch)
  {
    cachedIsDynamic = null;
    super.addChild(ch);
    if(!(ch instanceof NGComponent))
      throw new NAFException("NGComponents can only have NGComponent children");
    NGComponent chc = (NGComponent)ch;
    if(roleChildren == null) roleChildren = new HashMap<String, ArrayList<NGComponent>>();
    ArrayList<NGComponent> cl = roleChildren.get(chc.role);
    if(cl == null)
    {
      cl = new ArrayList<NGComponent>();
      roleChildren.put(chc.role, cl);
    }
    cl.add(chc);
  }


  public final List<NGComponent> getUnexpandedRoleChildren(String role)
  {
    if(roleChildren == null) return null;
    return roleChildren.get(role);
  }


  @SuppressWarnings("unchecked")
  public final <T extends NGComponent> List<T> getExpandedRoleChildren(String role, ModelMap models)
  {
    if(isDynamic()) return (List<T>)createExpandedRoleChildren(role, models);

    if(expandedRoleChildren == null) expandedRoleChildren = new HashMap<String, ArrayList<NGComponent>>();
    ArrayList<NGComponent> l = expandedRoleChildren.get(role);
    if(l == null)
    {
      l = createExpandedRoleChildren(role, models);
      expandedRoleChildren.put(role, l);
    }
    return (List<T>)l;
  }


  private final ArrayList<NGComponent> createExpandedRoleChildren(String role, ModelMap models)
  {
    List<NGComponent> src = getUnexpandedRoleChildren(role);
    ArrayList<NGComponent> l = new ArrayList<NGComponent>();
    if(src != null)
      for(NGComponent ch : src) ch.expandSelf(l, models, true);
    return l;
  }


  @SuppressWarnings("unchecked")
  public final <T extends NGComponent> List<T> getExpandedStaticRoleChildren(String role, ModelMap models)
  {
    if(expandedStaticRoleChildren == null) expandedStaticRoleChildren = new HashMap<String, ArrayList<NGComponent>>();
    ArrayList<NGComponent> l = expandedStaticRoleChildren.get(role);
    if(l == null)
    {
      l = createExpandedStaticRoleChildren(role, models);
      expandedStaticRoleChildren.put(role, l);
    }
    return (List<T>)l;
  }


  private final ArrayList<NGComponent> createExpandedStaticRoleChildren(String role, ModelMap models)
  {
    List<NGComponent> src = getUnexpandedRoleChildren(role);
    ArrayList<NGComponent> l = new ArrayList<NGComponent>();
    if(src != null)
      for(NGComponent ch : src) ch.expandSelf(l, models, false);
    return l;
  }


  public final NGComponent getFirstExpandedRoleChild(String role, ModelMap models)
  {
    //[TODO] Optimize for dynamic components where the expanded role children aren't cached
    List<NGComponent> l = getExpandedRoleChildren(role, models);
    return l.isEmpty() ? null : l.get(0);
  }


  @Override
  public final String toString()
  {
    StringWriter swr = new StringWriter();
    PrintWriter pwr = new PrintWriter(swr);
    ComponentDump.dump(this, pwr, "");
    pwr.flush();
    return swr.getBuffer().toString();
  }


  protected boolean isDynamic()
  {
    if(cachedIsDynamic == null)
    {
      cachedIsDynamic = Boolean.FALSE;
      for(NGNode n : getChildren())
      {
        if(!(n instanceof NGComponent)) continue;
        if(((NGComponent)n).isDynamic())
        {
          cachedIsDynamic = Boolean.TRUE;
          break;
        }
      }
    }
    return cachedIsDynamic;
  }
}
