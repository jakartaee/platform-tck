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

package com.sun.ts.tests.ejb30.bb.session.stateful.callback.listener.annotated;

import com.sun.ts.tests.ejb30.common.helper.TLogger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.ejb.PostActivate;
import javax.ejb.PrePassivate;

/**
 * A callback listerner for stateful session beans. Two callback annotations are
 * applied on the same method.
 */
public class StatefulCallbackListener2 {

  public StatefulCallbackListener2() {
    super();
  }

  /**
   * 4 callback annotations are applied on the same method
   */
  @PostConstruct
  @PreDestroy
  @PostActivate
  @PrePassivate
  private void sharedCallback(InvocationContext inv) throws RuntimeException {
    Callback2Bean bean = (Callback2Bean) inv.getTarget();
    bean.setPostConstructOrPreDestroyCalled(true);
    TLogger.log("PostConstruct, PreDestroy, PostActivateor or PrePassivate "
        + "method in " + this + " called for bean " + bean);
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
