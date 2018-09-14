/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.lite.tx.cm.stateful.annotated;

import javax.ejb.EJB;

import com.sun.ts.tests.ejb30.tx.common.session.cm.LocalTestBeanBase;

public class Client
    extends com.sun.ts.tests.ejb30.lite.tx.cm.common.ClientBase {
  @SuppressWarnings("unused")
  @EJB(beanInterface = TestBean.class, beanName = "TestBean")
  private void setTestBean(LocalTestBeanBase b) {
    this.testBean = b;
  }

  /*
   * @testName: localMandatoryTest
   * 
   * @test_Strategy: client -> local bm sfsb -> local cm sfsb
   */
  /*
   * @testName: localNeverTest
   * 
   * @test_Strategy: client -> local bm sfsb -> local cm sfsb
   */
  /*
   * @testName: localSupportsTest
   * 
   * @test_Strategy: client -> local bm sfsb -> local cm sfsb
   */
  /*
   * @testName: localIllegalGetSetRollbackOnlyNeverTest
   * 
   * @test_Strategy: client -> local bm bean -> local cm bean
   */
  /*
   * @testName: localIllegalGetSetRollbackOnlyNotSupportedTest
   * 
   * @test_Strategy: client -> local bm bean -> local cm bean
   */
  /*
   * @testName: localSystemExceptionTest
   * 
   * @test_Strategy: client -> remote bm sfsb -> local cm sfsb
   */
  /*
   * @testName: localRequiresNewTest
   * 
   * @test_Strategy: client -> remote bm sfsb -> local cm sfsb
   */
}
