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

package com.sun.ts.tests.ejb30.common.allowed;

import javax.annotation.Resource;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.ejb.SessionContext;
import javax.ejb.Timeout;
import javax.ejb.TransactionAttribute;

public abstract class CallbackAllowedBeanBase
    implements CallbackAllowedIF, CallbackAllowedLocalIF {
  protected SessionContext sessionContext;

  protected Properties results;

  abstract public Properties runOperations(SessionContext sctx);

  public void timeout(javax.ejb.Timer timer) {
  }

  public void ejbCreate() {
    this.results = null;
    this.results = runOperations(this.sessionContext);
  }

  public void setSessionContext(SessionContext sc) {
    this.sessionContext = sc;
  }

  public void remove() {
  }

  public Properties getResults() {
    return this.results;
  }

}
