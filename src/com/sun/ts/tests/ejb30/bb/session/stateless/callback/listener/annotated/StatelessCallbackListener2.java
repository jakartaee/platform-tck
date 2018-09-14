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

package com.sun.ts.tests.ejb30.bb.session.stateless.callback.listener.annotated;

import com.sun.ts.tests.ejb30.common.callback.InterceptorHelper;
import com.sun.ts.tests.ejb30.common.helper.TLogger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

/**
 * A callback listerner for stateless session beans. Two callback annotations
 * are applied on the same method.
 */
public class StatelessCallbackListener2 {

  public StatelessCallbackListener2() {
    super();
  }

  @PostConstruct
  @PreDestroy
  private void sharedCallback(InvocationContext inv) {
    Callback2Bean bean = (Callback2Bean) inv.getTarget();
    bean.setPostConstructOrPreDestroyCalled(true);
    TLogger.log("PostConstruct or PreDestroy method in " + this
        + " called for bean " + bean);
    InterceptorHelper.recordExceptionFromGetParameters(inv, bean, false);
    InterceptorHelper.recordExceptionFromSetParameters(inv, bean, false);
    try {
      inv.proceed();
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  @AroundInvoke
  private Object intercept(InvocationContext inv) throws Exception {
    return inv.proceed();
  }
}
