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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Listener implementation which logs diagnosis messages.
 *
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 15, 2004
 * @version $Id: DummyBackgroundTreeContentListener.java 371 2008-03-26 22:40:15Z szeiger $
 */

public class DummyBackgroundTreeContentListener implements BackgroundTreeContentListener
{
  private static final Logger LOGGER = LoggerFactory.getLogger(DummyBackgroundTreeContentListener.class);


  public void pendingGet(Object element)
  {
    LOGGER.info("Pending GET operation for element {}", element);
  }


  public void pendingHas(Object element)
  {
    LOGGER.info("Pending HAS operation for element {}", element);
  }


  public void pendingGetFinished(Object element)
  {
    LOGGER.info("Finished pending GET operation for element {}", element);
  }

  
  public void pendingHasFinished(Object element)
  {
    LOGGER.info("Finished pending HAS operation for element {}", element);
  }
}
