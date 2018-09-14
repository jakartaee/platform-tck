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

package com.sun.ts.tests.ejb30.bb.localaccess.common;

import static com.sun.ts.tests.ejb30.bb.localaccess.common.Constants.SERVER_MSG;
import static javax.transaction.Status.STATUS_ACTIVE;
import static javax.transaction.Status.STATUS_MARKED_ROLLBACK;
import static javax.transaction.Status.STATUS_UNKNOWN;

import javax.ejb.EJBContext;
import javax.ejb.EJBTransactionRolledbackException;
import javax.naming.NamingException;
import javax.transaction.UserTransaction;

import com.sun.ts.tests.ejb30.common.calc.CalculatorException;
import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.ejb30.common.helper.TLogger;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;

public abstract class TestBeanBase implements TestBeanIF {
  // abstract protected LocalIF getStatelessLocalBean();
  // abstract protected DefaultLocalIF getStatelessDefaultLocalBean();
  // abstract protected StatefulLocalIF getStatefulLocalBean();
  // abstract protected StatefulDefaultLocalIF getStatefulDefaultLocalBean();
  abstract public EJBContext getEJBContext();

  /**
   * Returns the same String to caller. Since java.lang.String is immutable, the
   * same String instance, instead of a copy, may be returned to the caller. So
   * this method is currently not used in test.
   */
  public String passByValueTest2(String obj) {
    return obj;
  }

  public StringBuffer passByValueTest3(StringBuffer obj) {
    return obj;
  }

  public void passByReferenceTest(String[] args, String refName, Class klass)
      throws TestFailedException {
    CommonIF bean = lookup(refName, klass);
    try {
      bean.passByReferenceTest(args);
    } finally {
      try {
        bean.remove();
      } catch (Exception e) {
        // ignore
      }
    }
    String expected = SERVER_MSG;
    String actual = args[0];
    if (expected.equals(actual)) {
      // passed
      TLogger.log(true, "Got expected value '" + expected + "'");
    } else {
      throw new TestFailedException(
          "Expect '" + expected + "', but actual '" + actual + "'");
    }
  }

  public void exceptionTest(String refName, Class klass)
      throws TestFailedException {
    CommonIF bean = lookup(refName, klass);
    boolean status = true;
    String reason = null;
    UserTransaction ut = getEJBContext().getUserTransaction();
    try {
      ut.begin();
      bean.exceptionTest();
      status = false;
      reason = "Expect CalculatorException, but got no exception.";
    } catch (CalculatorException e) {
      int code = STATUS_UNKNOWN;
      try {
        code = ut.getStatus();
        if (code == STATUS_ACTIVE) {
          status = true;
          reason = "Good, got expected exception and tx status.";
        } else {
          status = false;
          reason = "Got expected CalculatorException, but tx status " + code
              + " is not expected " + STATUS_ACTIVE;
        }
      } catch (Exception e2) {
        status = false;
        reason = "Failed to get ut.getStatus " + e2;
      }
    } catch (Exception e) {
      status = false;
      reason = "Expecting CalculatorException, but got " + e;
    } finally {
      try {
        bean.remove();
      } catch (Exception e) {
        // ignore
      }
      try {
        ut.rollback();
      } catch (Exception e) {
        // ignore
      }
    }
    if (!status) {
      throw new TestFailedException(reason);
    }
  }

  public void runtimeExceptionTest(String refName, Class klass)
      throws TestFailedException {
    boolean status = true;
    String reason = null;
    CommonIF bean = lookup(refName, klass);
    UserTransaction ut = getEJBContext().getUserTransaction();
    try {
      ut.begin();
      bean.runtimeExceptionTest();
      status = false;
      reason = "Expect javax.ejb.EJBTransactionRolledbackException, but got no exception.";
    } catch (EJBTransactionRolledbackException e) {
      int code = STATUS_UNKNOWN;
      try {
        code = ut.getStatus();
        if (code == STATUS_MARKED_ROLLBACK) {
          status = true;
          reason = "Good, got expected exception and tx status.";
        } else {
          status = false;
          reason = "Got expected EJBTransactionRolledbackException, but tx status "
              + code + " is not expected " + STATUS_MARKED_ROLLBACK;
        }
      } catch (Exception e2) {
        status = false;
        reason = "Failed to get ut.getStatus " + e2;
      }
    } catch (Exception e) {
      status = false;
      reason = "Expecting EJBTransactionRolledbackException, but got " + e;
    } finally {
      try {
        bean.remove();
      } catch (Exception e) {
        // ignore
      }
      try {
        ut.rollback();
      } catch (Exception e) {
        // ignore
      }
    }
    TLogger.log(status, reason);
  }

  protected CommonIF lookup(String refName, Class klass)
      throws TestFailedException {
    CommonIF bean = null;
    if (refName != null) {
      refName = "java:comp/env/" + refName;
    }
    try {
      bean = (CommonIF) ServiceLocator.lookup(refName, klass);
    } catch (NamingException e) {
      throw new TestFailedException(
          "Testbean Failed to look up bean with name '" + refName
              + " and type '" + klass + "'",
          e);
    }
    return bean;
  }

  /**
   * Resets the value of the first element in the String array. This change
   * should not affect the value in Client.
   */
  public void passByValueTest(String[] args) throws TestFailedException {
    args[0] = SERVER_MSG;
  }

}
