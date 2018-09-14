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

package com.sun.ts.tests.ejb30.bb.session.stateless.callback.method.annotated;

import javax.ejb.EJBContext;
import javax.ejb.SessionContext;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import com.sun.ts.tests.ejb30.common.callback.CallbackIF;
import com.sun.ts.tests.ejb30.common.callback.CallbackBeanBase;

public class EjbCreateCallbackBeanBase extends CallbackBeanBase
    implements CallbackIF {

  @Resource
  private SessionContext sctx;

  public EJBContext getEJBContext() {
    return this.sctx;
  }

  // ================= callback methods ====================================
  // @PostConstruct
  public void ejbCreate() {
    this.setPostConstructCalled(true);
    if (this.getEJBContext() != null) {
      this.setInjectionDone(true);
    }
  }

  @PreDestroy
  protected void ejbRemove() throws RuntimeException {
    this.setPreDestroyCalled(true);
  }

  // ================== business methods ====================================

}
