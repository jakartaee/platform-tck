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

package com.sun.ts.tests.ejb30.common.busiface;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

abstract public class SessionBeanLocalBeanBase extends BusinessLocal1Base
    implements BusinessLocalIF1, SessionBean {
  protected SessionContext sessionContext;

  public void setSessionContext(SessionContext sessionContext) {
    this.sessionContext = sessionContext;
  }

  public void ejbRemove() {
  }

  public void ejbPassivate() {
  }

  public void ejbActivate() {
  }

  // ////////////////////////////////////////////////////////////////////
  @Override
  public String[] businessMethodLocal1(String[] s) {
    if (sessionContext == null) {
      throw new IllegalStateException("SessionContext is null.");
    }

    String[] retValue;

    retValue = super.businessMethodLocal1(s);
    return retValue;
  }

}
