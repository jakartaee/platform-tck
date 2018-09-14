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
package com.sun.ts.tests.ejb30.lite.appexception.common.inheritance;

import com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase;
import javax.ejb.EJB;
import javax.ejb.EJBException;

/**
 * Exception hierarchy used in this test client:
 *
 * RuntimeException AtUncheckedAppException @ApplicationException() Exception1
 * No annotation Exception2 No annotation Exception3
 * <application-exception>(inherited=false) Exception4 a system exception
 * Exception5 a system exception Exception6
 * <application-exception>(inherited=true) Exception7 No annotation
 */
abstract public class ClientBase extends EJBLiteClientBase {
  protected static final String BEAN_LOOKUP_NAME = "ejb/InheritanceBean";

  @EJB(name = BEAN_LOOKUP_NAME, beanName = "InheritanceBean")
  private InheritanceIF bean;

  /**
   * For stateful and stateless bean, a system exception in previous test method
   * may cause the bean instance to be discarded, and the next test (if in the
   * same VM) may get NoSuchObectLocalException. Stateful and Stateless
   * directory can override this method to always look up to ensure a new
   * reference is used in each test.
   */
  protected InheritanceIF getBean() {
    return bean;
  }

  /*
   * testName: uncheckedAppException1
   * 
   * @test_Strategy:
   */
  public void uncheckedAppException1() {
    try {
      getBean().uncheckedAppException1();
      throw new RuntimeException("Expecting Exception1, but got none.");
    } catch (Exception1 e) {
      appendReason("Got the expected ", e);
    }
  }

  /*
   * testName: uncheckedAppException2
   * 
   * @test_Strategy:
   */
  public void uncheckedAppException2() {
    try {
      getBean().uncheckedAppException2();
      throw new RuntimeException("Expecting Exception2, but got none.");
    } catch (Exception2 e) {
      appendReason("Got the expected ", e);
    }
  }

  /*
   * testName: uncheckedAppException3
   * 
   * @test_Strategy:
   */
  public void uncheckedAppException3() {
    try {
      getBean().uncheckedAppException3();
      throw new RuntimeException("Expecting Exception3, but got none.");
    } catch (Exception3 e) {
      appendReason("Got the expected ", e);
    }
  }

  /*
   * testName: uncheckedSystemException4
   * 
   * @test_Strategy:
   */
  public void uncheckedSystemException4() {
    try {
      getBean().uncheckedSystemException4();
      throw new RuntimeException("Expecting EJBException, but got none.");
    } catch (EJBException e) {
      Throwable cause = e.getCause();
      if (cause instanceof Exception4) {
        appendReason("Got expected EJBException and Exception4.");
      } else {
        throw new RuntimeException(
            "Expecting EJBException with Exception4, but got " + cause);
      }
    }
  }

  /*
   * testName: uncheckedSystemException5
   * 
   * @test_Strategy:
   */
  public void uncheckedSystemException5() {
    try {
      getBean().uncheckedSystemException5();
      throw new RuntimeException("Expecting EJBException, but got none.");
    } catch (EJBException e) {
      Throwable cause = e.getCause();
      if (cause instanceof Exception5) {
        appendReason("Got expected EJBException and Exception5.");
      } else {
        throw new RuntimeException(
            "Expecting EJBException with Exception5, but got " + cause);
      }
    }
  }

  /*
   * testName: uncheckedAppException6
   * 
   * @test_Strategy:
   */
  public void uncheckedAppException6() {
    try {
      getBean().uncheckedAppException6();
      throw new RuntimeException("Expecting Exception6, but got none.");
    } catch (Exception6 e) {
      appendReason("Got the expected ", e);
    }
  }

  /*
   * testName: uncheckedAppException7
   * 
   * @test_Strategy:
   */
  public void uncheckedAppException7() {
    try {
      getBean().uncheckedAppException7();
      throw new RuntimeException("Expecting Exception7, but got none.");
    } catch (Exception7 e) {
      appendReason("Got the expected ", e);
    }
  }
}
