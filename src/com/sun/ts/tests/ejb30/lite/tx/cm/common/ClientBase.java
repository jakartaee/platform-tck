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
import com.sun.ts.tests.ejb30.tx.common.session.cm.LocalTestBeanBase;

abstract public class ClientBase extends EJBLiteClientBase {
  protected LocalTestBeanBase testBean; // injected in subclass

  /*
   * testName: localMandatoryTest
   * 
   * @test_Strategy: client -> remote bm slsb -> local cm slsb
   */
  public void localMandatoryTest() throws TestFailedException {
    appendReason(testBean.localMandatoryTest());
  }

  /*
   * testName: localNeverTest
   * 
   * @test_Strategy: client -> remote bm slsb -> local cm slsb
   */
  public void localNeverTest() throws TestFailedException {
    appendReason(testBean.localNeverTest());
  }

  /*
   * testName: localSupportsTest
   * 
   * @test_Strategy: client -> remote bm slsb -> local cm slsb
   */
  public void localSupportsTest() throws TestFailedException {
    appendReason(testBean.localSupportsTest());
  }

  /*
   * testName: localIllegalGetSetRollbackOnlyNeverTest
   * 
   * @test_Strategy: client -> remote bm bean -> local cm bean
   */
  public void localIllegalGetSetRollbackOnlyNeverTest()
      throws TestFailedException {
    appendReason(testBean.localIllegalGetSetRollbackOnlyNeverTest());
  }

  /*
   * testName: localIllegalGetSetRollbackOnlyNotSupportedTest
   * 
   * @test_Strategy: client -> remote bm bean -> local cm bean
   */
  public void localIllegalGetSetRollbackOnlyNotSupportedTest()
      throws TestFailedException {
    appendReason(testBean.localIllegalGetSetRollbackOnlyNotSupportedTest());
  }

  /*
   * testName: localRequiresNewRemoveTest
   * 
   * @test_Strategy: client -> remote bm slsb -> local cm sfsb
   */
  public void localRequiresNewRemoveTest() throws TestFailedException {
    appendReason(testBean.localRequiresNewRemoveTest());
  }

  /*
   * testName: localRequiresNewTest
   * 
   * @test_Strategy: client -> remote bm slsb -> local cm slsb
   */
  public void localRequiresNewTest() throws TestFailedException {
    appendReason(testBean.localRequiresNewTest());
  }

  /*
   * testName: localSystemExceptionTest
   * 
   * @test_Strategy: client -> remote bm slsb -> local cm sfsb
   */
  public void localSystemExceptionTest() throws TestFailedException {
    appendReason(testBean.localSystemExceptionTest());
  }
}
