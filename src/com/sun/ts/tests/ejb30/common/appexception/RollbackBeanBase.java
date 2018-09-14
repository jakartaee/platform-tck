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
package com.sun.ts.tests.ejb30.common.appexception;

import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.transaction.UserTransaction;
import static javax.transaction.Status.STATUS_ACTIVE;
import static javax.transaction.Status.STATUS_COMMITTED;
import static javax.transaction.Status.STATUS_COMMITTING;
import static javax.transaction.Status.STATUS_MARKED_ROLLBACK;
import static javax.transaction.Status.STATUS_NO_TRANSACTION;
import static javax.transaction.Status.STATUS_PREPARED;
import static javax.transaction.Status.STATUS_PREPARING;
import static javax.transaction.Status.STATUS_ROLLEDBACK;
import static javax.transaction.Status.STATUS_ROLLING_BACK;
import static javax.transaction.Status.STATUS_UNKNOWN;

public class RollbackBeanBase implements RollbackIF {
  @EJB(beanName = "AppExceptionBean", description = "just to see if descripton field works or not.  It should map to <ejb-ref>/<description> xml element.")
  protected AppExceptionIF appExceptionBean;

  @EJB(beanName = "AppExceptionBean")
  protected AppExceptionLocalIF appExceptionLocalBean;

  @Resource
  protected UserTransaction ut;

  protected static final Map txStatusCodes = new HashMap();

  static {
    txStatusCodes.put(STATUS_ACTIVE, "STATUS_ACTIVE");
    txStatusCodes.put(STATUS_COMMITTED, "STATUS_COMMITTED");
    txStatusCodes.put(STATUS_COMMITTING, "STATUS_COMMITTING");
    txStatusCodes.put(STATUS_MARKED_ROLLBACK, "STATUS_MARKED_ROLLBACK");
    txStatusCodes.put(STATUS_NO_TRANSACTION, "STATUS_NO_TRANSACTION");
    txStatusCodes.put(STATUS_PREPARED, "STATUS_PREPARED");
    txStatusCodes.put(STATUS_PREPARING, "STATUS_PREPARING");
    txStatusCodes.put(STATUS_ROLLEDBACK, "STATUS_ROLLEDBACK");
    txStatusCodes.put(STATUS_ROLLING_BACK, "STATUS_ROLLING_BACK");
    txStatusCodes.put(STATUS_UNKNOWN, "STATUS_UNKNOWN");
  }

  //////////////////////////////////////////////////////////////////////
  // interface methods
  //////////////////////////////////////////////////////////////////////
  public void checkedAppException()
      throws CheckedAppException, TestFailedException {
    try {
      ut.begin();
      appExceptionBean.checkedAppException();
      throw new TestFailedException(
          "Didn't get expected exception: " + "CheckedAppException");
    } catch (CheckedAppException e) {
      checkStatus(ut, STATUS_ACTIVE);
    } catch (Exception e) {
      throw new TestFailedException("Got unexpected exception: ", e);
    } finally {
      try {
        ut.rollback();
      } catch (Exception e) {
        // ignore
      }
    }
  }

  public void checkedAppExceptionLocal()
      throws CheckedAppException, TestFailedException {
    try {
      ut.begin();
      appExceptionLocalBean.checkedAppException();
      throw new TestFailedException(
          "Didn't get expected exception: " + "CheckedAppException");
    } catch (CheckedAppException e) {
      checkStatus(ut, STATUS_ACTIVE);
    } catch (Exception e) {
      throw new TestFailedException("Got unexpected exception: ", e);
    } finally {
      try {
        ut.rollback();
      } catch (Exception e) {
        // ignore
      }

    }
  }

  public void uncheckedAppException()
      throws UncheckedAppException, TestFailedException {
    try {
      ut.begin();
      appExceptionBean.uncheckedAppException();
      throw new TestFailedException(
          "Didn't get expected exception: " + "UncheckedAppException");
    } catch (UncheckedAppException e) {
      checkStatus(ut, STATUS_ACTIVE);
    } catch (Exception e) {
      throw new TestFailedException("Got unexpected exception: ", e);
    } finally {
      try {
        ut.rollback();
      } catch (Exception e) {
        // ignore
      }

    }
  }

  public void uncheckedAppExceptionLocal()
      throws UncheckedAppException, TestFailedException {
    try {
      ut.begin();
      appExceptionLocalBean.uncheckedAppException();
      throw new TestFailedException(
          "Didn't get expected exception: " + "UncheckedAppException");
    } catch (UncheckedAppException e) {
      checkStatus(ut, STATUS_ACTIVE);
    } catch (Exception e) {
      throw new TestFailedException("Got unexpected exception: ", e);
    } finally {
      try {
        ut.rollback();
      } catch (Exception e) {
        // ignore
      }

    }
  }

  public void uncheckedRollbackAppException()
      throws UncheckedRollbackAppException, TestFailedException {
    try {
      ut.begin();
      appExceptionBean.uncheckedRollbackAppException();
      throw new TestFailedException(
          "Didn't get expected exception: " + "UncheckedRollbackAppException");
    } catch (UncheckedRollbackAppException e) {
      checkStatus(ut, STATUS_MARKED_ROLLBACK);
    } catch (Exception e) {
      throw new TestFailedException("Got unexpected exception: ", e);
    } finally {
      try {
        ut.rollback();
      } catch (Exception e) {
        // ignore
      }

    }
  }

  public void uncheckedRollbackAppExceptionLocal()
      throws UncheckedRollbackAppException, TestFailedException {
    try {
      ut.begin();
      appExceptionLocalBean.uncheckedRollbackAppException();
      throw new TestFailedException(
          "Didn't get expected exception: " + "UncheckedRollbackAppException");
    } catch (UncheckedRollbackAppException e) {
      checkStatus(ut, STATUS_MARKED_ROLLBACK);
    } catch (Exception e) {
      throw new TestFailedException("Got unexpected exception: ", e);
    } finally {
      try {
        ut.rollback();
      } catch (Exception e) {
        // ignore
      }

    }
  }

  public void checkedRollbackAppException()
      throws CheckedRollbackAppException, TestFailedException {
    try {
      ut.begin();
      appExceptionBean.checkedRollbackAppException();
      throw new TestFailedException(
          "Didn't get expected exception: " + "CheckedRollbackAppException");
    } catch (CheckedRollbackAppException e) {
      checkStatus(ut, STATUS_MARKED_ROLLBACK);
    } catch (Exception e) {
      throw new TestFailedException("Got unexpected exception: ", e);
    } finally {
      try {
        ut.rollback();
      } catch (Exception e) {
        // ignore
      }

    }
  }

  public void checkedRollbackAppExceptionLocal()
      throws CheckedRollbackAppException, TestFailedException {
    try {
      ut.begin();
      appExceptionLocalBean.checkedRollbackAppException();
      throw new TestFailedException(
          "Didn't get expected exception: " + "CheckedRollbackAppException");
    } catch (CheckedRollbackAppException e) {
      checkStatus(ut, STATUS_MARKED_ROLLBACK);
    } catch (Exception e) {
      throw new TestFailedException("Got unexpected exception: ", e);
    } finally {
      try {
        ut.rollback();
      } catch (Exception e) {
        // ignore
      }

    }
  }

  //////////////////////////////////////////////////////////////////////
  // for /at/ and /override/ only
  //////////////////////////////////////////////////////////////////////
  public void atUncheckedAppException()
      throws AtUncheckedAppException, TestFailedException {
    try {
      ut.begin();
      appExceptionBean.atUncheckedAppException();
      throw new TestFailedException(
          "Didn't get expected exception: " + "AtUncheckedAppException");
    } catch (AtUncheckedAppException e) {
      checkStatus(ut, STATUS_ACTIVE);
    } catch (Exception e) {
      throw new TestFailedException("Got unexpected exception: ", e);
    } finally {
      try {
        ut.rollback();
      } catch (Exception e) {
        // ignore
      }

    }
  }

  public void atUncheckedAppExceptionLocal()
      throws AtUncheckedAppException, TestFailedException {
    try {
      ut.begin();
      appExceptionLocalBean.atUncheckedAppException();
      throw new TestFailedException(
          "Didn't get expected exception: " + "AtUncheckedAppException");
    } catch (AtUncheckedAppException e) {
      checkStatus(ut, STATUS_ACTIVE);
    } catch (Exception e) {
      throw new TestFailedException("Got unexpected exception: ", e);
    } finally {
      try {
        ut.rollback();
      } catch (Exception e) {
        // ignore
      }

    }
  }

  public void atCheckedAppException()
      throws AtCheckedAppException, TestFailedException {
    try {
      ut.begin();
      appExceptionBean.atCheckedAppException();
      throw new TestFailedException(
          "Didn't get expected exception: " + "AtCheckedAppException");
    } catch (AtCheckedAppException e) {
      checkStatus(ut, STATUS_ACTIVE);
    } catch (Exception e) {
      throw new TestFailedException("Got unexpected exception: ", e);
    } finally {
      try {
        ut.rollback();
      } catch (Exception e) {
        // ignore
      }

    }
  }

  public void atCheckedAppExceptionLocal()
      throws AtCheckedAppException, TestFailedException {
    try {
      ut.begin();
      appExceptionLocalBean.atCheckedAppException();
      throw new TestFailedException(
          "Didn't get expected exception: " + "AtCheckedAppException");
    } catch (AtCheckedAppException e) {
      checkStatus(ut, STATUS_ACTIVE);
    } catch (Exception e) {
      throw new TestFailedException("Got unexpected exception: ", e);
    } finally {
      try {
        ut.rollback();
      } catch (Exception e) {
        // ignore
      }

    }
  }

  public void atUncheckedRollbackAppException()
      throws AtUncheckedRollbackAppException, TestFailedException {
    try {
      ut.begin();
      appExceptionBean.atUncheckedRollbackAppException();
      throw new TestFailedException("Didn't get expected exception: "
          + "AtUncheckedRollbackAppException");
    } catch (AtUncheckedRollbackAppException e) {
      checkStatus(ut, STATUS_MARKED_ROLLBACK);
    } catch (Exception e) {
      throw new TestFailedException("Got unexpected exception: ", e);
    } finally {
      try {
        ut.rollback();
      } catch (Exception e) {
        // ignore
      }

    }
  }

  public void atUncheckedRollbackAppExceptionLocal()
      throws AtUncheckedRollbackAppException, TestFailedException {
    try {
      ut.begin();
      appExceptionLocalBean.atUncheckedRollbackAppException();
      throw new TestFailedException("Didn't get expected exception: "
          + "AtUncheckedRollbackAppException");
    } catch (AtUncheckedRollbackAppException e) {
      checkStatus(ut, STATUS_MARKED_ROLLBACK);
    } catch (Exception e) {
      throw new TestFailedException("Got unexpected exception: ", e);
    } finally {
      try {
        ut.rollback();
      } catch (Exception e) {
        // ignore
      }

    }
  }

  public void atCheckedRollbackAppException()
      throws AtCheckedRollbackAppException, TestFailedException {
    try {
      ut.begin();
      appExceptionBean.atCheckedRollbackAppException();
      throw new TestFailedException(
          "Didn't get expected exception: " + "AtCheckedRollbackAppException");
    } catch (AtCheckedRollbackAppException e) {
      checkStatus(ut, STATUS_MARKED_ROLLBACK);
    } catch (Exception e) {
      throw new TestFailedException("Got unexpected exception: ", e);
    } finally {
      try {
        ut.rollback();
      } catch (Exception e) {
        // ignore
      }

    }
  }

  public void atCheckedRollbackAppExceptionLocal()
      throws AtCheckedRollbackAppException, TestFailedException {
    try {
      ut.begin();
      appExceptionLocalBean.atCheckedRollbackAppException();
      throw new TestFailedException(
          "Didn't get expected exception: " + "AtCheckedRollbackAppException");
    } catch (AtCheckedRollbackAppException e) {
      checkStatus(ut, STATUS_MARKED_ROLLBACK);
    } catch (Exception e) {
      throw new TestFailedException("Got unexpected exception: ", e);
    } finally {
      try {
        ut.rollback();
      } catch (Exception e) {
        // ignore
      }
    }
  }

  //////////////////////////////////////////////////////////////////////
  protected void checkStatus(UserTransaction ut, int expected)
      throws TestFailedException {
    try {
      int actual = ut.getStatus();
      if (actual == expected) {
        // good
      } else {
        throw new TestFailedException("Got expected application exception,"
            + "expected tx status code " + expected + "("
            + txStatusCodes.get(expected) + ")" + ", but actual " + actual + "("
            + txStatusCodes.get(actual) + ")");
      }
    } catch (TestFailedException e) {
      throw e;
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
