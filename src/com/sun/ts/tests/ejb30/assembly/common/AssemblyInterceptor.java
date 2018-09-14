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

package com.sun.ts.tests.ejb30.assembly.common;

import com.sun.ts.tests.ejb30.common.helper.TLogger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class AssemblyInterceptor {

  public AssemblyInterceptor() {
    super();
  }

  @PostConstruct
  protected void myCreate(InvocationContext inv) throws RuntimeException {
  }

  @PreDestroy
  protected void myRemove(InvocationContext inv) throws RuntimeException {
  }

  @AroundInvoke
  protected Object intercept(InvocationContext inv) throws Exception {
    TLogger.log("In AroundInvoke of " + this);
    final int additional = 100;
    Object[] params = inv.getParameters();
    Object[] newParams = null;
    if (params != null && params.length == 2) {
      if (params[0] instanceof Integer && params[1] instanceof Integer) {
        newParams = new Integer[2];
        newParams[0] = (Integer) params[0] + additional;
        newParams[1] = (Integer) params[1] + additional;
        inv.setParameters(newParams);
      }
    }
    return inv.proceed();
  }
}
