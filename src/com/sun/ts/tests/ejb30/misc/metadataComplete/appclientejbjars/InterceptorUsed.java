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

package com.sun.ts.tests.ejb30.misc.metadataComplete.appclientejbjars;

import com.sun.ts.tests.ejb30.common.helper.TLogger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

/**
 * An interceptor configured as default interceptor.
 */
public class InterceptorUsed {

  public InterceptorUsed() {
    super();
  }

  /**
   * This interceptor method only intercepts any business method that has
   * parameters int, int.
   */
  @AroundInvoke
  protected Object intercept(InvocationContext inv) throws Exception {
    Object[] params = inv.getParameters();
    if (params != null && params.length == 2 && params[0] instanceof Integer
        && params[1] instanceof Integer) {
      TLogger.log(
          "Interceptor class: " + this + "\n" + "bean class: " + inv.getTarget()
              + "\n" + "business method: " + inv.getMethod() + "\n");
      Integer p1 = (Integer) params[0];
      Integer p2 = (Integer) params[1];
      Object[] newParams = new Integer[] { p1 + 100, p2 + 100 };
      inv.setParameters(newParams);
    }
    return inv.proceed();
  }
}
