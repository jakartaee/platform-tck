/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.lite.tx.cm.stateful.sessionsync;

import static com.sun.ts.tests.ejb30.lite.tx.cm.stateful.sessionsync.SessionSyncIF.aroundInvoke1;
import static com.sun.ts.tests.ejb30.lite.tx.cm.stateful.sessionsync.SessionSyncIF.aroundInvoke2;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class Interceptor1 {

  @SuppressWarnings("unused")
  @AroundInvoke
  private Object aroundInvoke(InvocationContext inv) throws Exception {
    Object result = null;
    SessionSyncIF b = (SessionSyncIF) inv.getTarget();
    b.addToHistory(aroundInvoke1);

    try {
      result = inv.proceed();
    } finally {
      b.addToHistory(aroundInvoke2);
    }

    return result;
  }
}
