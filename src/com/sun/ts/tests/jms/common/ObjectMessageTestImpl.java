/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

/*
 * $Id$
 */

package com.sun.ts.tests.jms.common;

import java.io.Serializable;
import javax.jms.*;

/**
 * Class Declaration.
 * 
 * 
 * @see
 *
 * @author
 * @version 1.2, 09/26/00
 */
public class ObjectMessageTestImpl extends MessageTestImpl
    implements ObjectMessage {
  private Serializable object;

  /**
   * Class Constructor.
   * 
   * 
   * @see
   */
  public ObjectMessageTestImpl() {
    super();
  }

  /**
   * Method Declaration.
   * 
   * 
   * @param object
   *
   * @exception JMSException
   *
   * @see
   */
  public void setObject(Serializable object) throws JMSException {
    this.object = object;
  }

  /**
   * Method Declaration.
   * 
   * 
   * @return
   *
   * @exception JMSException
   *
   * @see
   */
  public Serializable getObject() throws JMSException {
    return object;
  }

}
