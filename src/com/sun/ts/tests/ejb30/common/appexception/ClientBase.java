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
import java.util.Properties;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.tests.ejb30.common.helper.TLogger;
import javax.ejb.EJB;

abstract public class ClientBase extends EETest {
  @EJB(description = "It should map to <ejb-ref>/<description> xml element.")
  private static AppExceptionIF bean;

  @EJB
  private static RollbackIF rollbackBean;

  protected Properties props;

  /*
   * @class.setup_props:
   */
  public void setup(String[] args, Properties p) {
    props = p;
  }

  public void cleanup() {
  }
  //////////////////////////////////////////////////////////////////////
  // Tests whose name does NOT begin with "at" are for /annotated/
  // directory. It means these exceptions are NOT annotated with
  // @ApplicationException; they are configured as application exceptions
  //////////////////////////////////////////////////////////////////////

  /*
   * testName: checkedAppExceptionTest
   * 
   * @test_Strategy:
   *
   */
  public void checkedAppExceptionTest() throws TestFailedException {
    try {
      bean.checkedAppException();
      throw new TestFailedException(
          "Did not get expected exception: CheckedAppException");
    } catch (CheckedAppException e) {
      TLogger.log("Got expected exception: " + e);
    }
  }

  /*
   * testName: checkedAppExceptionTest2
   * 
   * @test_Strategy:
   *
   */
  public void checkedAppExceptionTest2() throws TestFailedException {
    try {
      rollbackBean.checkedAppException();
    } catch (CheckedAppException e) {
      throw new TestFailedException("Unexpected exception:", e);
    }
  }

  /*
   * testName: checkedAppExceptionTestLocal
   * 
   * @test_Strategy:
   *
   */
  public void checkedAppExceptionTestLocal() throws TestFailedException {
    try {
      rollbackBean.checkedAppExceptionLocal();
    } catch (CheckedAppException e) {
      throw new TestFailedException("Unexpected exception:", e);
    }
  }

  /*
   * testName: uncheckedAppExceptionTest
   * 
   * @test_Strategy:
   *
   */
  public void uncheckedAppExceptionTest() throws TestFailedException {
    try {
      bean.uncheckedAppException();
      throw new TestFailedException(
          "Did not get expected exception: UncheckedAppException");
    } catch (UncheckedAppException e) {
      TLogger.log("Got expected exception: " + e);
    }
  }

  /*
   * testName: uncheckedAppExceptionTest2
   * 
   * @test_Strategy:
   *
   */
  public void uncheckedAppExceptionTest2() throws TestFailedException {
    try {
      rollbackBean.uncheckedAppException();
    } catch (UncheckedAppException e) {
      throw new TestFailedException("Unexpected exception:", e);
    }
  }

  /*
   * testName: uncheckedAppExceptionTestLocal
   * 
   * @test_Strategy:
   *
   */
  public void uncheckedAppExceptionTestLocal() throws TestFailedException {
    try {
      rollbackBean.uncheckedAppExceptionLocal();
    } catch (UncheckedAppException e) {
      throw new TestFailedException("Unexpected exception:", e);
    }
  }

  /*
   * testName: checkedRollbackAppExceptionTest
   * 
   * @test_Strategy:
   *
   */
  public void checkedRollbackAppExceptionTest() throws TestFailedException {
    try {
      rollbackBean.checkedRollbackAppException();
    } catch (CheckedRollbackAppException e) {
      throw new TestFailedException("Unexpected exception:", e);
    }
  }

  /*
   * testName: checkedRollbackAppExceptionTestLocal
   * 
   * @test_Strategy:
   *
   */
  public void checkedRollbackAppExceptionTestLocal()
      throws TestFailedException {
    try {
      rollbackBean.checkedRollbackAppExceptionLocal();
    } catch (CheckedRollbackAppException e) {
      throw new TestFailedException("Unexpected exception:", e);
    }
  }

  /*
   * testName: uncheckedRollbackAppExceptionTest
   * 
   * @test_Strategy:
   *
   */
  public void uncheckedRollbackAppExceptionTest() throws TestFailedException {
    try {
      rollbackBean.uncheckedRollbackAppException();
    } catch (UncheckedRollbackAppException e) {
      throw new TestFailedException("Unexpected exception:", e);
    }
  }

  /*
   * testName: uncheckedRollbackAppExceptionTestLocal
   * 
   * @test_Strategy:
   *
   */
  public void uncheckedRollbackAppExceptionTestLocal()
      throws TestFailedException {
    try {
      rollbackBean.uncheckedRollbackAppExceptionLocal();
    } catch (UncheckedRollbackAppException e) {
      throw new TestFailedException("Unexpected exception:", e);
    }
  }

  //////////////////////////////////////////////////////////////////////
  // Tests whose name begin with "at" are for /override/ and /annotated/
  // directories. It means these exceptions are annotated with
  ////////////////////////////////////////////////////////////////////// @ApplicationException
  //////////////////////////////////////////////////////////////////////
  /*
   * testName: atCheckedAppExceptionTest
   * 
   * @test_Strategy:
   *
   */
  public void atCheckedAppExceptionTest() throws TestFailedException {
    try {
      bean.atCheckedAppException();
      throw new TestFailedException(
          "Did not get expected exception: AtCheckedAppException");
    } catch (AtCheckedAppException e) {
      TLogger.log("Got expected exception: " + e);
    }
  }

  /*
   * testName: atCheckedAppExceptionTest2
   * 
   * @test_Strategy:
   *
   */
  public void atCheckedAppExceptionTest2() throws TestFailedException {
    try {
      rollbackBean.atCheckedAppException();
    } catch (AtCheckedAppException e) {
      throw new TestFailedException("Unexpected exception", e);
    }
  }

  /*
   * testName: atCheckedAppExceptionTestLocal
   * 
   * @test_Strategy:
   *
   */
  public void atCheckedAppExceptionTestLocal() throws TestFailedException {
    try {
      rollbackBean.atCheckedAppExceptionLocal();
    } catch (AtCheckedAppException e) {
      throw new TestFailedException("Unexpected exception", e);
    }
  }

  /*
   * testName: atUncheckedAppExceptionTest
   * 
   * @test_Strategy:
   *
   */
  public void atUncheckedAppExceptionTest() throws TestFailedException {
    try {
      bean.atUncheckedAppException();
      throw new TestFailedException(
          "Did not get expected exception: AtUncheckedAppException");
    } catch (AtUncheckedAppException e) {
      TLogger.log("Got expected exception: " + e);
    }
  }

  /*
   * testName: atUncheckedAppExceptionTest2
   * 
   * @test_Strategy:
   *
   */
  public void atUncheckedAppExceptionTest2() throws TestFailedException {
    try {
      rollbackBean.atUncheckedAppException();
    } catch (AtUncheckedAppException e) {
      throw new TestFailedException("Unexpected exception:", e);
    }
  }

  /*
   * testName: atUncheckedAppExceptionTestLocal
   * 
   * @test_Strategy:
   *
   */
  public void atUncheckedAppExceptionTestLocal() throws TestFailedException {
    try {
      rollbackBean.atUncheckedAppExceptionLocal();
    } catch (AtUncheckedAppException e) {
      throw new TestFailedException("Unexpected exception:", e);
    }
  }

  /*
   * testName: atCheckedRollbackAppExceptionTest
   * 
   * @test_Strategy:
   *
   */
  public void atCheckedRollbackAppExceptionTest() throws TestFailedException {
    try {
      rollbackBean.atCheckedRollbackAppException();
    } catch (AtCheckedRollbackAppException e) {
      throw new TestFailedException("Unexpected exception:", e);
    }
  }

  /*
   * testName: atCheckedRollbackAppExceptionTestLocal
   * 
   * @test_Strategy:
   *
   */
  public void atCheckedRollbackAppExceptionTestLocal()
      throws TestFailedException {
    try {
      rollbackBean.atCheckedRollbackAppExceptionLocal();
    } catch (AtCheckedRollbackAppException e) {
      throw new TestFailedException("Unexpected exception:", e);
    }
  }

  /*
   * testName: atUncheckedRollbackAppExceptionTest
   * 
   * @test_Strategy:
   *
   */
  public void atUncheckedRollbackAppExceptionTest() throws TestFailedException {
    try {
      rollbackBean.atUncheckedRollbackAppException();
    } catch (AtUncheckedRollbackAppException e) {
      throw new TestFailedException("Unexpected exception:", e);
    }
  }

  /*
   * testName: atUncheckedRollbackAppExceptionTestLocal
   * 
   * @test_Strategy:
   *
   */
  public void atUncheckedRollbackAppExceptionTestLocal()
      throws TestFailedException {
    try {
      rollbackBean.atUncheckedRollbackAppExceptionLocal();
    } catch (AtUncheckedRollbackAppException e) {
      throw new TestFailedException("Unexpected exception:", e);
    }
  }
}
