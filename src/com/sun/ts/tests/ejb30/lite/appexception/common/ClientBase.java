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
package com.sun.ts.tests.ejb30.lite.appexception.common;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import com.sun.ts.tests.ejb30.common.appexception.AppExceptionIF;
import com.sun.ts.tests.ejb30.common.appexception.AtCheckedAppException;
import com.sun.ts.tests.ejb30.common.appexception.AtCheckedRollbackAppException;
import com.sun.ts.tests.ejb30.common.appexception.AtUncheckedAppException;
import com.sun.ts.tests.ejb30.common.appexception.AtUncheckedRollbackAppException;
import com.sun.ts.tests.ejb30.common.appexception.CheckedAppException;
import com.sun.ts.tests.ejb30.common.appexception.CheckedRollbackAppException;
import com.sun.ts.tests.ejb30.common.appexception.CommonIF;
import com.sun.ts.tests.ejb30.common.appexception.RollbackIF;
import com.sun.ts.tests.ejb30.common.appexception.UncheckedAppException;
import com.sun.ts.tests.ejb30.common.appexception.UncheckedRollbackAppException;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase;

abstract public class ClientBase extends EJBLiteClientBase {
  @EJB(description = "It should map to <ejb-ref>/<description> xml element.", beanName = "AppExceptionBean")
  protected AppExceptionIF bean;

  @EJB(beanName = "RollbackBean")
  protected RollbackIF rollbackBean;

  // injected in subclass
  protected AppExceptionIF noInterfaceBean;

  protected List<CommonIF> appExceptionBeans = new ArrayList<CommonIF>();

  abstract protected void setNoInterfaceBean(AppExceptionIF noInterfaceBean);

  @SuppressWarnings("unused")
  @PostConstruct
  private void postConstruct() {
    appExceptionBeans.add(bean);
    appExceptionBeans.add(noInterfaceBean);
  }

  // tests used by both */appexception/annotated/ and */appexception/override/

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

  /*
   * testName: checkedAppExceptionTest
   * 
   * @test_Strategy:
   *
   */
  public void checkedAppExceptionTest() throws TestFailedException {
    for (CommonIF b : appExceptionBeans) {
      try {
        b.checkedAppException();
        throw new TestFailedException(
            "Did not get expected exception: CheckedAppException");
      } catch (CheckedAppException e) {
        appendReason("Got expected exception: ", e);
      }
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

  // tests used by */appexception/annotated/

  /*
   * testName: atCheckedAppExceptionTest
   * 
   * @test_Strategy:
   *
   */
  public void atCheckedAppExceptionTest() throws TestFailedException {
    for (CommonIF b : appExceptionBeans) {
      try {
        b.atCheckedAppException();
        throw new TestFailedException(
            "Did not get expected exception: AtCheckedAppException");
      } catch (AtCheckedAppException e) {
        appendReason("Got expected exception: ", e);
      }
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
    for (CommonIF b : appExceptionBeans) {
      try {
        b.atUncheckedAppException();
        throw new TestFailedException(
            "Did not get expected exception: AtUncheckedAppException");
      } catch (AtUncheckedAppException e) {
        appendReason("Got expected exception: ", e);
      }
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

  // tests used by */appexception/override/

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
      appendReason("Got expected exception: ", e);
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
}
