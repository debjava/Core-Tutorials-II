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

package com.novocode.naf.gui.layout;

import java.util.Map;

import org.eclipse.swt.widgets.*;

import com.novocode.naf.app.NAFException;
import com.novocode.naf.gui.*;
import com.novocode.naf.resource.*;


/**
 * Base class for classes which create layout managers and layout data.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Nov 29, 2003
 */

public abstract class NGLayout implements ConfigurableObject
{
  private Map<String, String> defaultLayoutDataAttributes;


  public abstract void assignLayout(Composite comp, ShellWindowInstance wi) throws NAFException;
  
  
  public void assignLayoutData(Control control, Map<String, String> attributes, Map<String, Control> siblings, Control[] siblingsList, int siblingsIndex) throws NAFException
  {
  }


  public void setControlVisibility(Control control, boolean visible, WindowInstance wi)
  {
    control.setVisible(visible);
  }


  @ConfProperty(prefix = "ld")
  public final void setDefaultLayoutDataAttributes(Map<String, String> m) { defaultLayoutDataAttributes = m; }
  public final Map<String, String> getDefaultLayoutDataAttributes() { return defaultLayoutDataAttributes; }
}
