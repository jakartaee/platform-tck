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

package com.sun.ts.tests.ejb30.common.migration.threetwo;

import javax.ejb.CreateException;
import javax.ejb.EJBContext;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

abstract public class MigrationBeanBase implements SessionBean {
  private SessionContext sessionContext;

  protected EJBContext getEJBContext() {
    return this.sessionContext;
  }

  public void ejbCreate() throws CreateException {
  }

  public void setSessionContext(javax.ejb.SessionContext sessionContext) {
    this.sessionContext = sessionContext;
  }

  public void ejbRemove() {
  }

  public void ejbPassivate() {
  }

  public void ejbActivate() {
  }

  //////////////////////////////////////////////////////////////////////
  // TwoRemoteIF methods
  //////////////////////////////////////////////////////////////////////
  public String from2RemoteClient() {
    return "from2RemoteClient";
  }

  public void remoteSameTxContext() {
    getEJBContext().setRollbackOnly();
  }

  //////////////////////////////////////////////////////////////////////
  // TwoLocalIF methods
  //////////////////////////////////////////////////////////////////////
  public String from2LocalClient() {
    return "from2LocalClient";
  }

  public void localSameTxContext() {
    getEJBContext().setRollbackOnly();
  }

}
