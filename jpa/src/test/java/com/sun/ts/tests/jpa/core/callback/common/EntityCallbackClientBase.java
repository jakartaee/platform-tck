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

package com.sun.ts.tests.jpa.core.callback.common;

import java.sql.SQLException;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

public abstract class EntityCallbackClientBase extends PMClientBase {
  protected EntityCallbackClientBase() {
    super();
  }

  protected Object txShouldRollback(Object b, String testName) throws Fault {
    String reason = "";
    try {
      TestUtil.logTrace("Persisting: " + b.getClass().getName());
      getEntityManager().persist(b);
      TestUtil.logTrace("Committing: " + b.getClass().getName() + " changes");
      getEntityTransaction().commit();
      reason = "Expecting ArithmeticException from callback method, but got none.";
      throw new Fault(reason);
    } catch (ArithmeticException e) {
      reason = "EntityCallbackClientBase: Got expected exception: "
          + e.toString();
      TestUtil.logTrace(reason);
      if (!getEntityTransaction().isActive()) {
        reason = "No Transaction was active, even though one was previously started";
        throw new Fault(reason, e);
      }
    } catch (Exception e) {
      reason = "EntityCallbackClientBase: Expecting ArithmeticException, but got unexpected exception: ["
          + e.toString() + "]";
      throw new Fault(reason, e);
    }
    TestUtil.logTrace("Clearing cache");
    clearCache();
    TestUtil.logTrace("Executing find");

    Object p2 = null;
    try {
      //Transaction is marked as rollback. TransactionManger is in an undefined state. SQLException is a possibility
      p2 = getEntityManager().find(b.getClass(), testName);
    } catch (RuntimeException e) {
      Throwable cause = e;
      while (cause != null && !SQLException.class.isInstance(cause)) {
        cause = cause.getCause();
      }
      if (cause == null) {
       throw new Fault(e);
      }
    }
    if (p2 == null) {
      reason = "EntityCallbackClientBase: Got expected result: entity with id "
          + testName + " was not found.";
      TestUtil.logTrace(reason);
    } else {
      reason = "EntityCallbackClientBase: Unexpected result: found entity with id "
          + testName;
      throw new Fault(reason);
    }
    return b;
  }
}
