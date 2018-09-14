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

import java.util.logging.Level;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.EJBException;
import javax.ejb.EJBTransactionRequiredException;
import javax.ejb.SessionContext;
import javax.transaction.NotSupportedException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;

/**
 * A test bean superclass for ejb lite where only local ejb refs are used. It
 * does not implement TestIF to avoid having to implementing all test methods
 * that are only applicable in remote case.
 */
abstract public class LocalTestBeanBase {
  @EJB(name = "localTxBean")
  protected LocalTxIF localTxBean;

  @Resource
  private SessionContext sctx;

  public EJBContext getEJBContext() {
    return sctx;
  }

  public String localMandatoryTest() throws TestFailedException {
    String reason = null;
    try {
      localTxBean.localMandatoryTest();
      reason = "Expecting javax.ejb.EJBTransactionRequiredException, but got none";
      throw new TestFailedException(reason);
    } catch (EJBTransactionRequiredException e) {
      reason = "Got expected EJBTransactionRequiredException";
      Helper.getLogger().info(reason);
    }
    return reason;
  }

  public String localSupportsTest() throws TestFailedException {
    UserTransaction ut = getEJBContext().getUserTransaction();
    try {
      ut.begin();
      return localTxBean.localSupportsTest();
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

  public String localIllegalGetSetRollbackOnlyNeverTest()
      throws TestFailedException {
    return localTxBean.localIllegalGetSetRollbackOnlyNeverTest();
  }

  public String localIllegalGetSetRollbackOnlyNotSupportedTest()
      throws TestFailedException {
    UserTransaction ut = getEJBContext().getUserTransaction();
    try {
      ut.begin();
      return localTxBean.localIllegalGetSetRollbackOnlyNotSupportedTest();
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

  public String localNeverTest() throws TestFailedException {
    String reason = null;
    UserTransaction ut = getEJBContext().getUserTransaction();
    try {
      ut.begin();
      localTxBean.neverTest();
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
    return reason;
  }

  public String localRequiresNewTest() throws TestFailedException {
    String result = null;
    UserTransaction ut = getEJBContext().getUserTransaction();
    try {
      ut.begin();
      localTxBean.localRequiresNewTest(); // rollback in this method
      int status = ut.getStatus();
      result = Helper.assertEquals("Check tx status",
          TestUtil.getTransactionStatus(Status.STATUS_ACTIVE),
          TestUtil.getTransactionStatus(status));
      Helper.getLogger().logp(Level.FINE, "LocalTestBeanBase",
          "localRequiresNewTest", "Checking tx status, result: " + result);
    } catch (Exception e) {
      throw new TestFailedException(e);
    } finally {
      try {
        ut.rollback();
      } catch (Exception igore) {
      }
    }
    return result;
  }

  public String localRequiresNewRemoveTest() throws TestFailedException {
    // for sfsb only. sfsb will override this method with meaningful impl
    throw new IllegalStateException(
        "This method should not be called for stateless beans");
  }

  public void remove() {
    // for sfsb only. sfsb will override this method with meaningful impl
    throw new IllegalStateException(
        "This method should not be called for stateless beans");
  }

  public String localSystemExceptionTest() throws TestFailedException {
    String result = null;
    UserTransaction ut = getEJBContext().getUserTransaction();
    try {
      ut.begin();
      String s = localTxBean.systemExceptionTest();
      throw new TestFailedException(
          "Expecting EJBTransactionRolledbackException, but got no exception: "
              + s);
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

}
