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
 * Default implementation of the IProgressModel interface.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Nov 14, 2004
 */

public class DefaultProgressModel extends DefaultModel implements IProgressModel
{
  private volatile int min, max, progress;


  public DefaultProgressModel() { this(0, 100, 0); }


  public DefaultProgressModel(int min, int max, int progress)
  {
    this.min = min;
    this.max = max;
    this.progress = progress;
  }


  public int getMin()
  {
    return min;
  }


  public int getMax()
  {
    return max;
  }


  public int getProgress()
  {
    return progress;
  }


  public synchronized void setMin(int i)
  {
    if(this.min != i)
    {
      this.min = i;
      notifyListeners();
    }
  }


  public synchronized void setMax(int i)
  {
    if(this.max != i)
    {
      this.max = i;
      notifyListeners();
    }
  }


  public synchronized void setProgress(int i)
  {
    if(this.progress != i)
    {
      this.progress = i;
      notifyListeners();
    }
  }


  public synchronized void setAll(int min, int max, int progress)
  {
    if(this.min != min || this.max != max || this.progress != progress)
    {
      this.min = min;
      this.max = max;
      this.progress = progress;
      notifyListeners();
    }
  }
}
