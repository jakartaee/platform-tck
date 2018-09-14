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

package com.sun.ts.tests.ejb30.bb.session.stateless.callback.method.descriptor;

import javax.ejb.EJBContext;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.annotation.Resource;

import com.sun.ts.tests.ejb30.common.callback.CallbackIF;
import com.sun.ts.tests.ejb30.common.callback.CallbackBeanBase;
import com.sun.ts.tests.ejb30.common.helper.TLogger;

@Stateless(name = "CallbackBean")
@Remote({ CallbackIF.class })
public class CallbackBean extends CallbackBeanBase implements CallbackIF {

  @Resource
  private SessionContext sctx;

  public CallbackBean() {
    super();
  }

  public EJBContext getEJBContext() {
    return this.sctx;
  }

  // ================= callback methods ====================================
  // @PostConstruct
  private void ejbCreate() throws RuntimeException {
    this.setPostConstructCalled(true);
    TLogger.log("PostConstruct method called: " + this);
    if (this.getEJBContext() != null) {
      this.setInjectionDone(true);
    } else {
      TLogger.log("Inside PostConstruct method SessionContext is null.");
    }
  }

  // @PreDestroy
  void ejbRemove() throws RuntimeException {
    this.setPreDestroyCalled(true);
    TLogger.log("PreDestroy method called: " + this);
  }

  // ================== business methods ====================================

  public boolean isInjectionDoneTest() {
    if (getEJBContext() == null) {
      TLogger.log("SessionContext is null inside business method "
          + "isInjectionDoneTest " + this);
    } else {
      TLogger.log("SessionContext has been injected when business method "
          + "isInjectionDoneTest is called " + this);
    }
    boolean retValue;

    retValue = super.isInjectionDoneTest();
    return retValue;
  }

}
