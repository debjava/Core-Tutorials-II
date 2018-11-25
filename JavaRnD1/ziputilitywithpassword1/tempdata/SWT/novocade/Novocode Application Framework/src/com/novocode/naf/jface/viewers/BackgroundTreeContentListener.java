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

package com.novocode.naf.jface.viewers;


/**
 * A listener which can be attached to a BackgroundTreeContentProvider.
 *
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 15, 2004
 * @version $Id: BackgroundTreeContentListener.java 271 2004-12-16 14:52:05 +0000 (Thu, 16 Dec 2004) szeiger $
 */

public interface BackgroundTreeContentListener
{
  public void pendingGet(Object element);
  public void pendingHas(Object element);
  public void pendingHasFinished(Object element);
  public void pendingGetFinished(Object element);
}
