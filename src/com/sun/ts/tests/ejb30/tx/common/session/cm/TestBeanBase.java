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

package com.sun.ts.tests.ejb30.tx.common.session.cm;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.transaction.UserTransaction;
import javax.ejb.EJBTransactionRequiredException;
import javax.transaction.NotSupportedException;
import javax.transaction.Status;
import javax.transaction.SystemException;

abstract public class TestBeanBase extends LocalTestBeanBase implements TestIF {
  @EJB(name = "txBean")
  protected TxIF txBean;

  protected void mandatoryTestOverloaded(TxIF b) throws TestFailedException {
    // subclasses may override to have different logic
    b.mandatoryTest(
        "The tx attribute of mandatoryTest() must not apply to this method.");
  }

  protected void neverTestOverloaded(TxIF b) throws TestFailedException {
    // subclasses may override to have different logic
    String reason = "";
    UserTransaction ut = getEJBContext().getUserTransaction();
    try {
      ut.begin();
      b.neverTest(
          "The tx attribute of neverTest() must not apply to this method");
    } catch (Exception e) {
      reason = "Unexpected exception: " + e;
      throw new TestFailedException(reason);
    } finally {
      try {
        ut.rollback();
      } catch (Exception e) {
        // ignore
      }
    }
  }

  /////////////////////////////////////////////////////////////////////////

  public void mandatoryTest() throws TestFailedException {
    String reason = "";
    try {
      txBean.mandatoryTest();
      reason += "Expecting javax.ejb.EJBTransactionRequiredException, but got none";
      throw new TestFailedException(reason);
    } catch (EJBTransactionRequiredException e) {
      reason += "Got expected javax.ejb.EJBTransactionRequiredException";
      Helper.getLogger().info(reason);
    }
    mandatoryTestOverloaded(txBean);
  }

  public String supportsTest() throws TestFailedException {
    // Starts ut before calling supportsTest, which is CMT + SUPPORTS, and
    // EJBContext.setRollbackOnly should cause IllegalStateException, even
    // there IS an active tx.
    UserTransaction ut = getEJBContext().getUserTransaction();
    try {
      ut.begin();
      return txBean.supportsTest();
    } catch (NotSupportedException e) {
      throw new TestFailedException(e);
    } catch (SystemException e) {
      throw new TestFailedException(e);
    } finally {
      try {
        ut.rollback();
      } catch (Exception ignore) {
        throw new TestFailedException(ignore);
      }
    }
  }

  public String illegalGetSetRollbackOnlyNeverTest()
      throws TestFailedException {
    return txBean.illegalGetSetRollbackOnlyNeverTest();
  }

  public String illegalGetSetRollbackOnlyNotSupportedTest()
      throws TestFailedException {
    UserTransaction ut = getEJBContext().getUserTransaction();
    try {
      ut.begin();
      return txBean.illegalGetSetRollbackOnlyNotSupportedTest();
    } catch (NotSupportedException e) {
      throw new TestFailedException(e);
    } catch (SystemException e) {
      throw new TestFailedException(e);
    } finally {
      try {
        ut.rollback();
      } catch (Exception ignore) {
        throw new TestFailedException(ignore);
      }
    }
  }

  public String systemExceptionTest() throws TestFailedException {
    String result = null;
    UserTransaction ut = getEJBContext().getUserTransaction();
    try {
      ut.begin();
      String s = txBean.systemExceptionTest();
      throw new TestFailedException(
          "Expecting EJBTransactionRolledbackException, but got no exception.");
    } catch (NotSupportedException e) {
      throw new TestFailedException(e);
    } catch (SystemException e) {
      throw new TestFailedException(e);
    } catch (javax.ejb.EJBTransactionRolledbackException e) {
      result = "Got expected EJBTransactionRolledbackException.";
      Integer status = null;
      try {
        status = ut.getStatus();
      } catch (SystemException ex) {
        throw new TestFailedException(result + " Failed to get tx status", ex);
      }
      if (status == Status.STATUS_MARKED_ROLLBACK) {
        return result + " Got expected tx status " + status;
      } else {
        throw new TestFailedException(result + " but tx status is " + status);
      }
    } finally {
      try {
        ut.rollback();
      } catch (Exception ignore) {
        throw new TestFailedException(ignore);
      }
    }
  }

  public void neverTest() throws TestFailedException {
    String reason = null;
    UserTransaction ut = getEJBContext().getUserTransaction();
    try {
      ut.begin();
      txBean.neverTest();
      reason = "Expecting javax.ejb.EJBException, but got none";
      throw new TestFailedException(reason);
    } catch (EJBException e) {
      reason = "Got expected javax.ejb.EJBException";
      Helper.getLogger().info(reason);
    } catch (NotSupportedException ex) {
      throw new TestFailedException(
          "Expecting javax.ejb.EJBException, but got " + ex);
    } catch (SystemException ex) {
      throw new TestFailedException(
          "Expecting javax.ejb.EJBException, but got " + ex);
    } finally {
      try {
        ut.rollback();
      } catch (Exception e) {
        // ignore
      }
    }
    neverTestOverloaded(txBean);
  }

  public void requiresNewTest() throws TestFailedException {
  }

  // to be overridden by certain subclasses to give a meaningful implementation
  public void sameMethodDifferentTxAttr() throws TestFailedException {
  }
}
