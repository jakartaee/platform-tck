/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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
import static jakarta.transaction.Status.STATUS_ACTIVE;

/**
 * A bean base class for tests in appexception/override/ directory, where the
 * rollback attribute of some application exceptions are overridden with
 * ejb-jar.xml. Therefore some methods will behave differently.
 */
public class RollbackOverrideBeanBase extends RollbackBeanBase {
  @Override
  public void atUncheckedRollbackAppException()
      throws AtUncheckedRollbackAppException, TestFailedException {
    try {
      ut.begin();
      appExceptionBean.atUncheckedRollbackAppException();
      throw new TestFailedException("Didn't get expected exception: "
          + "AtUncheckedRollbackAppException");
    } catch (AtUncheckedRollbackAppException e) {
      checkStatus(ut, STATUS_ACTIVE);
    } catch (Exception e) {
      throw new TestFailedException("Got unexpected exception: " + e);
    } finally {
      try {
        ut.rollback();
      } catch (Exception e) {
        // ignore
      }

    }
  }

  @Override
  public void atUncheckedRollbackAppExceptionLocal()
      throws AtUncheckedRollbackAppException, TestFailedException {
    try {
      ut.begin();
      appExceptionLocalBean.atUncheckedRollbackAppException();
      throw new TestFailedException("Didn't get expected exception: "
          + "AtUncheckedRollbackAppException");
    } catch (AtUncheckedRollbackAppException e) {
      checkStatus(ut, STATUS_ACTIVE);
    } catch (Exception e) {
      throw new TestFailedException("Got unexpected exception: " + e);
    } finally {
      try {
        ut.rollback();
      } catch (Exception e) {
        // ignore
      }

    }
  }

  @Override
  public void atCheckedRollbackAppException()
      throws AtCheckedRollbackAppException, TestFailedException {
    try {
      ut.begin();
      appExceptionBean.atCheckedRollbackAppException();
      throw new TestFailedException(
          "Didn't get expected exception: " + "AtCheckedRollbackAppException");
    } catch (AtCheckedRollbackAppException e) {
      checkStatus(ut, STATUS_ACTIVE);
    } catch (Exception e) {
      throw new TestFailedException("Got unexpected exception: " + e);
    } finally {
      try {
        ut.rollback();
      } catch (Exception e) {
        // ignore
      }

    }
  }

  @Override
  public void atCheckedRollbackAppExceptionLocal()
      throws AtCheckedRollbackAppException, TestFailedException {
    try {
      ut.begin();
      appExceptionLocalBean.atCheckedRollbackAppException();
      throw new TestFailedException(
          "Didn't get expected exception: " + "AtCheckedRollbackAppException");
    } catch (AtCheckedRollbackAppException e) {
      checkStatus(ut, STATUS_ACTIVE);
    } catch (Exception e) {
      throw new TestFailedException("Got unexpected exception: " + e);
    } finally {
      try {
        ut.rollback();
      } catch (Exception e) {
        // ignore
      }
    }
  }
}
