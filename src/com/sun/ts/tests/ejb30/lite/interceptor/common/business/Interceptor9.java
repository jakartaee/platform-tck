/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.lite.interceptor.common.business;

import java.util.logging.Level;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import com.sun.ts.tests.ejb30.common.appexception.AtCheckedRollbackAppException;
import com.sun.ts.tests.ejb30.common.helper.Helper;

public class Interceptor9 {
  private static final String simpleName = "Interceptor9";

  @AroundInvoke
  protected Object interceptInInterceptor9(InvocationContext inv)
      throws Exception {
    Helper.getLogger().logp(Level.FINE, simpleName, "interceptInInterceptor9",
        "In interceptInInterceptor9, business method: "
            + inv.getMethod().getName());
    try {
      return inv.proceed();
    } finally {
      throw new AtCheckedRollbackAppException(
          "The transaction should roll back after this application exception.");
    }
  }
}
