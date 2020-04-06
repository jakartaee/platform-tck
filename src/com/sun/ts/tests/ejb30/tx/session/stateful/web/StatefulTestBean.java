/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb30.tx.session.stateful.web;

import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.Remote;
import jakarta.ejb.Remove;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateful;
import jakarta.ejb.Local;
import jakarta.ejb.Remote;
import com.sun.ts.tests.ejb30.tx.common.web.LocalIF;
import com.sun.ts.tests.ejb30.tx.common.web.RemoteIF;

import javax.annotation.Resource;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;

@Stateful(name = "StatefulTestBean")
@Remote({ RemoteIF.class })
@Local({ LocalIF.class })

public class StatefulTestBean implements LocalIF {
  public StatefulTestBean() {
  }

  @Remove
  public void remove() {
  }

  @Resource(name = "sessionContext")
  private SessionContext sessionContext;

  protected SessionContext getSessionContext() {
    return sessionContext;
  }

  public void required() {
    getSessionContext().setRollbackOnly();
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
  public void requiresNew() {
    getSessionContext().setRollbackOnly();
  }

  @TransactionAttribute(TransactionAttributeType.MANDATORY)
  public void mandatory() {
    getSessionContext().setRollbackOnly();
  }

  @TransactionAttribute(TransactionAttributeType.NEVER)
  public void never() {
  }

}
