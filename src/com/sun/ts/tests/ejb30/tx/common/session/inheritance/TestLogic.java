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
package com.sun.ts.tests.ejb30.tx.common.session.inheritance;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import javax.ejb.EJBException;
import javax.ejb.EJBTransactionRequiredException;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

public class TestLogic {
  protected static final String fooReturnValue = "foo";

  protected static final String barReturnValue = "bar";

  public static void aBeanRemote(StringBuilder reason)
      throws TestFailedException {
    boolean pass1 = false;
    boolean pass2 = false;
    TxRemoteIF beanRemote = (TxRemoteIF) ServiceLocator
        .lookupByShortNameNoTry("abeanRemote");
    Helper.getLogger().info("About to invoke abeanRemote.foo()");
    String result = beanRemote.foo();
    if (fooReturnValue.equals(result)) {
      pass1 = true;
      reason.append(" Got expected result when invoking ");
      reason.append(beanRemote);
    } else {
      reason.append("Expecting ").append(fooReturnValue);
      reason.append(", but got ").append(result);
    }
    Helper.getLogger().info("About to invoke abeanRemote.bar()");
    result = null;
    try {
      result = beanRemote.bar();
      reason.append("Expecting javax.ejb.EJBTransactionRequiredException");
      reason.append(", but got no exception.");
    } catch (EJBTransactionRequiredException e) {
      pass2 = true;
      reason.append(" Got expected ").append(e.toString())
          .append(" when invoking ");
      reason.append(beanRemote).append(" bar()");
    }
    if (!(pass1 && pass2)) {
      throw new TestFailedException(reason.toString());
    }
  }

  public static void aBeanLocal(StringBuilder reason, TxLocalIF... b)
      throws TestFailedException {
    boolean pass1 = false;
    boolean pass2 = false;

    TxLocalIF beanLocal = b.length == 0 ? null : b[0];

    if (beanLocal == null) {
      beanLocal = (TxLocalIF) ServiceLocator
          .lookupByShortNameNoTry("abeanLocal");
    }
    Helper.getLogger().info("About to invoke abeanLocal.foo()");
    String result = beanLocal.foo();
    if (fooReturnValue.equals(result)) {
      pass1 = true;
      reason.append(" Got expected result when invoking ");
      reason.append(beanLocal);
    } else {
      reason.append("Expecting ").append(fooReturnValue);
      reason.append(", but got ").append(result);
    }
    result = null;
    try {
      result = beanLocal.bar();
      reason.append("Expecting javax.ejb.EJBTransactionRequiredException");
      reason.append(", but got no exception.");
    } catch (EJBTransactionRequiredException e) {
      pass2 = true;
      reason.append(" Got expected ").append(e.toString())
          .append(" when invoking ");
      reason.append(beanLocal).append(" bar()");
    }
    if (!(pass1 && pass2)) {
      throw new TestFailedException(reason.toString());
    }
  }

  public static void bBeanRemote(StringBuilder reason)
      throws TestFailedException {
    boolean pass1 = false;
    boolean pass2 = false;
    TxRemoteIF beanRemote = (TxRemoteIF) ServiceLocator
        .lookupByShortNameNoTry("bbeanRemote");
    UserTransaction ut = (UserTransaction) ServiceLocator
        .lookupNoTry("java:comp/UserTransaction");
    Helper.getLogger().info("About to invoke bbeanRemote.foo()");

    // BBean.foo() has REQUIRES_NEW, and calls setRollbackOnly(). So the new
    // tx must rollback without affecting the ut started from servlet.
    // The servlet must also get the normal result from business method.
    String result = null;
    try {
      ut.begin();
      result = beanRemote.foo();
      int status = ut.getStatus();
      if (status == Status.STATUS_ACTIVE) {
        reason.append(" Got expected UserTransaction status: active ");
        reason.append(status);
        if (fooReturnValue.equals(result)) {
          pass1 = true;
          reason.append(" Got expected result when invoking ");
          reason.append(beanRemote);
        } else {
          reason.append("Expecting ").append(fooReturnValue);
          reason.append(", but got ").append(result);
        }
      } else {
        reason.append("Expecting UserTransaction status to be active,");
        reason.append(", but got ").append(status);
      }
      ut.rollback();
    } catch (Exception e) {
      throw new TestFailedException(reason.toString(), e);
    }

    Helper.getLogger().info("About to invoke bbeanRemote.bar()");
    result = null;
    try {
      result = beanRemote.bar();
      reason.append("Expecting javax.ejb.EJBTransactionRequiredException");
      reason.append(", but got no exception.");
    } catch (EJBTransactionRequiredException e) {
      pass2 = true;
      reason.append(" Got expected ").append(e.toString())
          .append(" when invoking ");
      reason.append(beanRemote).append(" bar()");
    }
    if (!(pass1 && pass2)) {
      throw new TestFailedException(reason.toString());
    }
  }

  public static void bBeanLocal(StringBuilder reason)
      throws TestFailedException {
    boolean pass1 = false;
    boolean pass2 = false;
    TxLocalIF beanlocal = (TxLocalIF) ServiceLocator
        .lookupByShortNameNoTry("bbeanLocal");
    UserTransaction ut = (UserTransaction) ServiceLocator
        .lookupNoTry("java:comp/UserTransaction");
    Helper.getLogger().info("About to invoke bbeanlocal.foo()");

    // BBean.foo() has REQUIRES_NEW, and calls setRollbackOnly(). So the new
    // tx must rollback without affecting the ut started from servlet.
    // The servlet must also get the normal result from business method.
    String result = null;
    try {
      ut.begin();
      result = beanlocal.foo();
      int status = ut.getStatus();
      if (status == Status.STATUS_ACTIVE) {
        reason.append(" Got expected UserTransaction status: active ");
        reason.append(status);
        if (fooReturnValue.equals(result)) {
          pass1 = true;
          reason.append(" Got expected result when invoking ");
          reason.append(beanlocal);
        } else {
          reason.append("Expecting ").append(fooReturnValue);
          reason.append(", but got ").append(result);
        }
      } else {
        reason.append("Expecting UserTransaction status to be active,");
        reason.append(", but got ").append(status);
      }
      ut.rollback();
    } catch (Exception e) {
      throw new TestFailedException(reason.toString(), e);
    }

    Helper.getLogger().info("About to invoke bbeanlocal.bar()");
    result = null;
    try {
      result = beanlocal.bar();
      reason.append("Expecting javax.ejb.EJBTransactionRequiredException");
      reason.append(", but got no exception.");
    } catch (EJBTransactionRequiredException e) {
      pass2 = true;
      reason.append(" Got expected ").append(e.toString())
          .append(" when invoking ");
      reason.append(beanlocal).append(" bar()");
    }
    if (!(pass1 && pass2)) {
      throw new TestFailedException(reason.toString());
    }
  }

  public static void cBeanRemote(StringBuilder reason)
      throws TestFailedException {
    boolean pass1 = false;
    boolean pass2 = false;
    TxRemoteIF beanRemote = (TxRemoteIF) ServiceLocator
        .lookupByShortNameNoTry("cbeanRemote");
    UserTransaction ut = (UserTransaction) ServiceLocator
        .lookupNoTry("java:comp/UserTransaction");
    Helper.getLogger().info("About to invoke cbeanRemote.foo()");

    String result = null;
    try {
      ut.begin();
      result = beanRemote.foo();
      int status = ut.getStatus();
      if (status == Status.STATUS_ACTIVE) {
        reason.append(" Got expected UserTransaction status: active ");
        reason.append(status);
        if (fooReturnValue.equals(result)) {
          pass1 = true;
          reason.append(" Got expected result when invoking ");
          reason.append(beanRemote);
        } else {
          reason.append("Expecting ").append(fooReturnValue);
          reason.append(", but got ").append(result);
        }
      } else {
        reason.append("Expecting UserTransaction status to be active,");
        reason.append(", but got ").append(status);
      }
      ut.rollback();
    } catch (Exception e) {
      throw new TestFailedException(reason.toString(), e);
    }

    Helper.getLogger().info("About to invoke cbeanRemote.bar()");
    result = null;
    try {
      ut.begin();
      result = beanRemote.bar();
      int status = ut.getStatus();
      if (status == Status.STATUS_ACTIVE) {
        reason.append(" Got expected UserTransaction status: active ");
        reason.append(status);
        if (barReturnValue.equals(result)) {
          pass2 = true;
          reason.append(" Got expected result when invoking ");
          reason.append(beanRemote);
        } else {
          reason.append("Expecting ").append(barReturnValue);
          reason.append(", but got ").append(result);
        }
      } else {
        reason.append("Expecting UserTransaction status to be active,");
        reason.append(", but got ").append(status);
      }
      ut.rollback();
    } catch (Exception e) {
      throw new TestFailedException(reason.toString(), e);
    }
    if (!(pass1 && pass2)) {
      throw new TestFailedException(reason.toString());
    }
  }

  public static void cBeanLocal(StringBuilder reason)
      throws TestFailedException {
    boolean pass1 = false;
    boolean pass2 = false;
    TxLocalIF beanlocal = (TxLocalIF) ServiceLocator
        .lookupByShortNameNoTry("cbeanLocal");
    UserTransaction ut = (UserTransaction) ServiceLocator
        .lookupNoTry("java:comp/UserTransaction");
    Helper.getLogger().info("About to invoke cbeanlocal.foo()");

    String result = null;
    try {
      ut.begin();
      result = beanlocal.foo();
      int status = ut.getStatus();
      if (status == Status.STATUS_ACTIVE) {
        reason.append(" Got expected UserTransaction status: active ");
        reason.append(status);
        if (fooReturnValue.equals(result)) {
          pass1 = true;
          reason.append(" Got expected result when invoking ");
          reason.append(beanlocal);
        } else {
          reason.append("Expecting ").append(fooReturnValue);
          reason.append(", but got ").append(result);
        }
      } else {
        reason.append("Expecting UserTransaction status to be active,");
        reason.append(", but got ").append(status);
      }
      ut.rollback();
    } catch (Exception e) {
      throw new TestFailedException(reason.toString(), e);
    }

    Helper.getLogger().info("About to invoke cbeanlocal.bar()");
    result = null;
    try {
      ut.begin();
      result = beanlocal.bar();
      int status = ut.getStatus();
      if (status == Status.STATUS_ACTIVE) {
        reason.append(" Got expected UserTransaction status: active ");
        reason.append(status);
        if (barReturnValue.equals(result)) {
          pass2 = true;
          reason.append(" Got expected result when invoking ");
          reason.append(beanlocal);
        } else {
          reason.append("Expecting ").append(barReturnValue);
          reason.append(", but got ").append(result);
        }
      } else {
        reason.append("Expecting UserTransaction status to be active,");
        reason.append(", but got ").append(status);
      }
      ut.rollback();
    } catch (Exception e) {
      throw new TestFailedException(reason.toString(), e);
    }
    if (!(pass1 && pass2)) {
      throw new TestFailedException(reason.toString());
    }
  }

  public static void dBeanRemote(StringBuilder reason)
      throws TestFailedException {
    boolean pass1 = false;
    boolean pass2 = false;
    TxRemoteIF beanRemote = (TxRemoteIF) ServiceLocator
        .lookupByShortNameNoTry("dbeanRemote");
    UserTransaction ut = (UserTransaction) ServiceLocator
        .lookupNoTry("java:comp/UserTransaction");
    Helper.getLogger().info("About to invoke dbeanRemote.foo()");

    String result = null;
    try {
      ut.begin();
      try {
        result = beanRemote.foo();
        reason.append("Expecting javax.ejb.EJBException");
        reason.append(", but got no exception.");
      } catch (EJBException ejbe) {
        pass1 = true;
        reason.append(" Got expected exception: ").append(ejbe.toString());
      }
      ut.rollback();
    } catch (Exception e) {
      throw new TestFailedException(reason.toString(), e);
    }

    Helper.getLogger().info("About to invoke dbeanRemote.bar()");
    result = null;
    try {
      ut.begin();
      result = beanRemote.bar();
      int status = ut.getStatus();
      if (status == Status.STATUS_ACTIVE) {
        reason.append(" Got expected UserTransaction status: active ");
        reason.append(status);
        if (barReturnValue.equals(result)) {
          pass2 = true;
          reason.append(" Got expected result when invoking ");
          reason.append(beanRemote);
        } else {
          reason.append("Expecting ").append(barReturnValue);
          reason.append(", but got ").append(result);
        }
      } else {
        reason.append("Expecting UserTransaction status to be active,");
        reason.append(", but got ").append(status);
      }
      ut.rollback();
    } catch (Exception e) {
      throw new TestFailedException(reason.toString(), e);
    }
    if (!(pass1 && pass2)) {
      throw new TestFailedException(reason.toString());
    }
  }

  public static void dBeanLocal(StringBuilder reason)
      throws TestFailedException {
    boolean pass1 = false;
    boolean pass2 = false;
    TxLocalIF beanLocal = (TxLocalIF) ServiceLocator
        .lookupByShortNameNoTry("dbeanLocal");
    UserTransaction ut = (UserTransaction) ServiceLocator
        .lookupNoTry("java:comp/UserTransaction");
    Helper.getLogger().info("About to invoke dbeanLocal.foo()");

    String result = null;
    try {
      ut.begin();
      try {
        result = beanLocal.foo();
        reason.append("Expecting javax.ejb.EJBException");
        reason.append(", but got no exception.");
      } catch (EJBException ejbe) {
        pass1 = true;
        reason.append(" Got expected exception: ").append(ejbe.toString());
      }
      ut.rollback();
    } catch (Exception e) {
      throw new TestFailedException(reason.toString(), e);
    }

    Helper.getLogger().info("About to invoke dbeanLocal.bar()");
    result = null;
    try {
      ut.begin();
      result = beanLocal.bar();
      int status = ut.getStatus();
      if (status == Status.STATUS_ACTIVE) {
        reason.append(" Got expected UserTransaction status: active ");
        reason.append(status);
        if (barReturnValue.equals(result)) {
          pass2 = true;
          reason.append(" Got expected result when invoking ");
          reason.append(beanLocal);
        } else {
          reason.append("Expecting ").append(barReturnValue);
          reason.append(", but got ").append(result);
        }
      } else {
        reason.append("Expecting UserTransaction status to be active,");
        reason.append(", but got ").append(status);
      }
      ut.rollback();
    } catch (Exception e) {
      throw new TestFailedException(reason.toString(), e);
    }
    if (!(pass1 && pass2)) {
      throw new TestFailedException(reason.toString());
    }
  }

  public static void eBeanRemote(StringBuilder reason)
      throws TestFailedException {
    boolean pass1 = false;
    boolean pass2 = false;
    TxRemoteIF beanRemote = (TxRemoteIF) ServiceLocator
        .lookupByShortNameNoTry("ebeanRemote");
    UserTransaction ut = (UserTransaction) ServiceLocator
        .lookupNoTry("java:comp/UserTransaction");
    Helper.getLogger().info("About to invoke ebeanRemote.foo()");

    String result = null;
    try {
      ut.begin();
      try {
        result = beanRemote.foo();
        reason.append("Expecting javax.ejb.EJBException");
        reason.append(", but got no exception.");
      } catch (EJBException ejbe) {
        pass1 = true;
        reason.append(" Got expected exception: ").append(ejbe.toString());
      }
      ut.rollback();
    } catch (Exception e) {
      throw new TestFailedException(reason.toString(), e);
    }

    Helper.getLogger().info("About to invoke ebeanRemote.bar()");
    result = null;
    try {
      result = beanRemote.bar();
      reason.append("Expecting javax.ejb.EJBTransactionRequiredException");
      reason.append(", but got no exception.");
    } catch (EJBTransactionRequiredException e) {
      pass2 = true;
      reason.append(" Got expected ").append(e.toString())
          .append(" when invoking ");
      reason.append(beanRemote).append(" bar()");
    }
    if (!(pass1 && pass2)) {
      throw new TestFailedException(reason.toString());
    }
  }

  public static void eBeanLocal(StringBuilder reason)
      throws TestFailedException {
    boolean pass1 = false;
    boolean pass2 = false;
    TxLocalIF beanLocal = (TxLocalIF) ServiceLocator
        .lookupByShortNameNoTry("ebeanLocal");
    UserTransaction ut = (UserTransaction) ServiceLocator
        .lookupNoTry("java:comp/UserTransaction");
    Helper.getLogger().info("About to invoke ebeanLocal.foo()");

    String result = null;
    try {
      ut.begin();
      try {
        result = beanLocal.foo();
        reason.append("Expecting javax.ejb.EJBException");
        reason.append(", but got no exception.");
      } catch (EJBException ejbe) {
        pass1 = true;
        reason.append(" Got expected exception: ").append(ejbe.toString());
      }
      ut.rollback();
    } catch (Exception e) {
      throw new TestFailedException(reason.toString(), e);
    }

    Helper.getLogger().info("About to invoke ebeanLocal.bar()");
    result = null;
    try {
      result = beanLocal.bar();
      reason.append("Expecting javax.ejb.EJBTransactionRequiredException");
      reason.append(", but got no exception.");
    } catch (EJBTransactionRequiredException e) {
      pass2 = true;
      reason.append(" Got expected ").append(e.toString())
          .append(" when invoking ");
      reason.append(beanLocal).append(" bar()");
    }
    if (!(pass1 && pass2)) {
      throw new TestFailedException(reason.toString());
    }
  }

  public static void overloadedMethodsTxLocal(StringBuilder reason,
      TxLocalIF... b) throws TestFailedException {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;

    TxLocalIF beanLocal = b.length == 0 ? null : b[0];

    if (beanLocal == null) {
      beanLocal = (TxLocalIF) ServiceLocator
          .lookupByShortNameNoTry("fbeanLocal");
    }

    Helper.getLogger().info(
        "About to invoke fbeanLocal.foo(), which has tx attribute Mandatory.");
    try {
      String result = beanLocal.foo();
      reason.append("Expecting javax.ejb.EJBTransactionRequiredException");
      reason.append(", but got no exception.");
    } catch (EJBTransactionRequiredException e) {
      pass1 = true;
      reason.append(" Got expected ").append(e.toString())
          .append(" when invoking ");
      reason.append(beanLocal).append(" foo()");
    }

    Helper.getLogger().info(
        "About to invoke fbeanLocal.foo(String), which has tx attribute Required.");
    try {
      String result = beanLocal.foo(fooReturnValue);
      String expected = fooReturnValue;
      if (expected.equals(result)) {
        pass2 = true;
        reason.append(" Got expected result: ").append(expected);
        reason.append(", when invoking ").append(beanLocal);
        reason.append(" foo(String)");
      } else {
        reason.append("Expecting ").append(expected);
        reason.append(", but got: ").append(result);
      }
    } catch (EJBException e) {
      reason.append(" Got unexpected ").append(e.toString())
          .append(" when invoking ");
      reason.append(beanLocal).append(" foo(String)");
    }

    Helper.getLogger().info(
        "About to invoke fbeanLocal.foo(String, String), which has tx attribute Required.");
    try {
      String result = beanLocal.foo(fooReturnValue, fooReturnValue);
      String expected = fooReturnValue + fooReturnValue;
      if (expected.equals(result)) {
        pass3 = true;
        reason.append(" Got expected result: ").append(expected);
        reason.append(", when invoking ").append(beanLocal);
        reason.append(" foo(String, String");
      } else {
        reason.append("Expecting ").append(expected);
        reason.append(", but got: ").append(result);
      }
    } catch (EJBException e) {
      reason.append(" Got unexpected ").append(e.toString())
          .append(" when invoking ");
      reason.append(beanLocal).append(" foo(String, String)");
    }
    if (!(pass1 && pass2 && pass3)) {
      throw new TestFailedException(reason.toString());
    }
  }

  public static void overloadedMethodsTxRemote(StringBuilder reason)
      throws TestFailedException {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;
    TxRemoteIF beanRemote = (TxRemoteIF) ServiceLocator
        .lookupByShortNameNoTry("fbeanRemote");
    Helper.getLogger().info(
        "About to invoke fbeanRemote.foo(), which has tx attribute Mandatory.");
    try {
      String result = beanRemote.foo();
      reason.append("Expecting javax.ejb.EJBTransactionRequiredException");
      reason.append(", but got no exception.");
    } catch (EJBTransactionRequiredException e) {
      pass1 = true;
      reason.append(" Got expected ").append(e.toString())
          .append(" when invoking ");
      reason.append(beanRemote).append(" foo()");
    }

    Helper.getLogger().info(
        "About to invoke fbeanRemote.foo(String), which has tx attribute Required.");
    try {
      String result = beanRemote.foo(fooReturnValue);
      String expected = fooReturnValue;
      if (expected.equals(result)) {
        pass2 = true;
        reason.append(" Got expected result: ").append(expected);
        reason.append(", when invoking ").append(beanRemote);
        reason.append(" foo(String)");
      } else {
        reason.append("Expecting ").append(expected);
        reason.append(", but got: ").append(result);
      }
    } catch (EJBException e) {
      reason.append(" Got unexpected ").append(e.toString())
          .append(" when invoking ");
      reason.append(beanRemote).append(" foo(String)");
    }

    Helper.getLogger().info(
        "About to invoke fbeanRemote.foo(String, String), which has tx attribute Required.");
    try {
      String result = beanRemote.foo(fooReturnValue, fooReturnValue);
      String expected = fooReturnValue + fooReturnValue;
      if (expected.equals(result)) {
        pass3 = true;
        reason.append(" Got expected result: ").append(expected);
        reason.append(", when invoking ").append(beanRemote);
        reason.append(" foo(String, String");
      } else {
        reason.append("Expecting ").append(expected);
        reason.append(", but got: ").append(result);
      }
    } catch (EJBException e) {
      reason.append(" Got unexpected ").append(e.toString())
          .append(" when invoking ");
      reason.append(beanRemote).append(" foo(String, String)");
    }
    if (!(pass1 && pass2 && pass3)) {
      throw new TestFailedException(reason.toString());
    }
  }
}
