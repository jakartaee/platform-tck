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

package com.sun.ts.tests.ejb30.bb.session.stateful.callback.method.annotated;

import jakarta.ejb.EJBContext;
import jakarta.ejb.Remote;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateful;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import jakarta.ejb.PostActivate;
import jakarta.ejb.PrePassivate;
import javax.annotation.Resource;
import jakarta.ejb.Remove;
//@todo
//import jakarta.ejb.Init;

import com.sun.ts.tests.ejb30.common.callback.CallbackIF;
import com.sun.ts.tests.ejb30.common.callback.CallbackBeanBase;
import com.sun.ts.tests.ejb30.common.helper.TLogger;

@Stateful(name = "CallbackBean")
@Remote({ CallbackIF.class })
public class CallbackBean extends CallbackBeanBase
    implements CallbackIF, java.io.Serializable {

  @Resource
  private SessionContext sctx;

  public CallbackBean() {
    super();
  }

  public EJBContext getEJBContext() {
    return this.sctx;
  }

  // ================= callback methods ====================================
  @PostConstruct
  @PostActivate
  private void ejbCreate() throws RuntimeException {
    this.setPostConstructCalled(true);
    TLogger.log("PostConstruct or PostActivate method called: " + this);
    if (this.getEJBContext() != null) {
      this.setInjectionDone(true);
    }
    // try {
    // this.getEJBContext().setRollbackOnly();
    // } catch (IllegalStateException e) {
    // //just log it. The test will fail inside the business method.
    // TLogger.log("WARN: failed to setRollbackOnly inside PostConstruct or
    // PostActivate");
    // }
  }

  @PreDestroy
  @PrePassivate
  private void ejbRemove() throws RuntimeException {
    this.setPreDestroyCalled(true);
    TLogger.log("PreDestroy or PrePassivate method called: " + this);
  }

  @Remove
  public void removeFoo() {
  }

  // ================== business methods ====================================
}
