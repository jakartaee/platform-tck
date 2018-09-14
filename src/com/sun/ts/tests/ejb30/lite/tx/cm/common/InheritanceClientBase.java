/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.lite.tx.cm.common;

import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase;
import com.sun.ts.tests.ejb30.tx.common.session.inheritance.TestLogic;
import com.sun.ts.tests.ejb30.tx.common.session.inheritance.TxLocalIF;
import javax.ejb.EJB;
import javax.ejb.EJBs;

@EJBs({
    @EJB(name = "abeanLocal", beanName = "ABean", beanInterface = TxLocalIF.class, description = "<local ABean>"),

    @EJB(name = "bbeanLocal", beanName = "BBean", beanInterface = TxLocalIF.class, description = "<local BBean>"),

    @EJB(name = "cbeanLocal", beanName = "CBean", beanInterface = TxLocalIF.class, description = "<local CBean>"),

    @EJB(name = "dbeanLocal", beanName = "DBean", beanInterface = TxLocalIF.class, description = "<local DBean>"),

    @EJB(name = "ebeanLocal", beanName = "EBean", beanInterface = TxLocalIF.class, description = "<local EBean>"),

    @EJB(name = "fbeanLocal", beanName = "FBean", beanInterface = TxLocalIF.class, description = "<local FBean>") })
abstract public class InheritanceClientBase extends EJBLiteClientBase {
  /*
   * testName: aBeanLocal
   * 
   * @test_Strategy:
   */
  public void aBeanLocal() throws TestFailedException {
    TestLogic.aBeanLocal(getReasonBuffer(),
        (TxLocalIF) lookup("abeanLocal", null, null));
  }

  /*
   * testName: bBeanLocal
   * 
   * @test_Strategy:
   */
  public void bBeanLocal() throws TestFailedException {
    // no op in embed ejb, since the client cannot get UserTransaction
    if (getContainer() != null) {
      return;
    }
    TestLogic.bBeanLocal(getReasonBuffer());
  }

  /*
   * testName: cBeanLocal
   * 
   * @test_Strategy:
   */
  public void cBeanLocal() throws TestFailedException {
    if (getContainer() != null) {
      return;
    }
    TestLogic.cBeanLocal(getReasonBuffer());
  }

  /*
   * testName: dBeanLocal
   * 
   * @test_Strategy:
   */
  public void dBeanLocal() throws TestFailedException {
    if (getContainer() != null) {
      return;
    }
    TestLogic.dBeanLocal(getReasonBuffer());
  }

  /*
   * testName: eBeanLocal
   * 
   * @test_Strategy:
   */
  public void eBeanLocal() throws TestFailedException {
    if (getContainer() != null) {
      return;
    }
    TestLogic.eBeanLocal(getReasonBuffer());
  }

  /*
   * testName: overloadedMethodsTxLocal
   * 
   * @test_Strategy:
   */
  public void overloadedMethodsTxLocal() throws TestFailedException {
    TestLogic.overloadedMethodsTxLocal(getReasonBuffer(),
        (TxLocalIF) lookup("fbeanLocal", null, null));
  }
}
