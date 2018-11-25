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

package com.novocode.naf.gui.event;

import com.novocode.naf.app.*;
import com.novocode.naf.model.IObjectModifyModel;
import com.novocode.naf.model.IObjectReadModel;


/**
 * An ActionListener which sets the value of an IObjectModifyModel.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 16, 2004
 * @version $Id: SetObjectModelValueActionListener.java 270 2004-12-16 02:01:54 +0000 (Thu, 16 Dec 2004) szeiger $
 */

public class SetObjectModelValueActionListener<T> implements IActionListener
{
  private IObjectModifyModel<T> om;
  private T value;
  private IObjectReadModel<T> source;

  
  public SetObjectModelValueActionListener(IObjectModifyModel<T> om, T value)
  {
    this.om = om;
    this.value = value;
  }
  

  public SetObjectModelValueActionListener(IObjectModifyModel<T> om, IObjectReadModel<T> source)
  {
    this.om = om;
    this.source = source;
  }
  

  public void performAction(ActionEvent e) throws NAFException
  {
    om.setValue(source != null ? source.getValue() : value);
  }
}
