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

package com.novocode.naf.model;


/**
 * Default implementation of the ICLabelModel interface.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Jan 5, 2004
 */

public class DefaultCLabelModel extends DefaultModel implements ICLabelModel
{
  public static final PType<ICLabelModel> PTYPE = new PType<ICLabelModel>(ICLabelModel.class, DefaultCLabelModel.class);


  private volatile String text, imageName;


  public DefaultCLabelModel() {}
  
  
  public DefaultCLabelModel(String text)
  {
    this.text = text;
  }
  
  
  public DefaultCLabelModel(String text, String imageName)
  {
    this.text = text;
    this.imageName = imageName;
  }
  
  
  public String getText()
  {
    return text;
  }


  public String getImageID()
  {
    return imageName;
  }


  public synchronized void setText(String text)
  {
    if(this.text != text)
    {
      this.text = text;
      notifyListeners();
    }
  }


  public synchronized void setImageID(String imageName)
  {
    if(this.imageName != imageName)
    {
      this.imageName = imageName;
      notifyListeners();
    }
  }


  public synchronized void setTextAndImageID(String text, String imageName)
  {
    if(this.text != text || this.imageName != imageName)
    {
      this.text = text;
      this.imageName = imageName;
      notifyListeners();
    }
  }
}
