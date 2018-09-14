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

package com.sun.ts.tests.ejb30.bb.mdb.callback.listener.descriptor;

import com.sun.ts.tests.ejb30.common.helper.TLogger;
import javax.interceptor.InvocationContext;

/**
 * A callback listerner for mdb beans. Callback methods may throw
 * RuntimeException. They are declared in the throws list, though not necessary.
 */
public class MDBCallbackListener {

  public MDBCallbackListener() {
    super();
  }

  // @PostConstruct
  private void myCreate(InvocationContext inv) throws RuntimeException {
    CallbackBean bean = (CallbackBean) inv.getTarget();
    bean.setPostConstructCalled(true);
    TLogger.log("PostConstruct method in " + this + " called for bean " + bean);
    if (bean.getEJBContext() != null) {
      bean.setInjectionDone(true);
    }
    try {
      inv.proceed();
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  // @PreDestroy
  private void myRemove(InvocationContext inv) throws RuntimeException {
    Object bean = inv.getTarget();
    if (bean instanceof CallbackBean) {
      CallbackBean b = (CallbackBean) bean;
      b.setPreDestroyCalled(true);
      TLogger.log("PreDestroy method in " + this + " called for bean " + bean);
    }
    try {
      inv.proceed();
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  // @AroundInvoke
  private Object intercept(InvocationContext inv) throws Exception {
    return inv.proceed();
  }
}
