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
package com.sun.ts.tests.ejb30.lite.tx.cm.singleton.annotated;

import com.sun.ts.tests.ejb30.tx.common.session.cm.LocalTestBeanBase;
import javax.ejb.EJB;

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
   * @test_Strategy:
   */
  /*
   * @testName: localNeverTest
   * 
   * @test_Strategy:
   */
  /*
   * @testName: localSupportsTest
   * 
   * @test_Strategy:
   */
  /*
   * @testName: localIllegalGetSetRollbackOnlyNeverTest
   * 
   * @test_Strategy:
   */
  /*
   * @testName: localIllegalGetSetRollbackOnlyNotSupportedTest
   * 
   * @test_Strategy:
   */
  /*
   * @testName: localSystemExceptionTest
   * 
   * @test_Strategy:
   */
  /*
   * @testName: localRequiresNewTest
   * 
   * @test_Strategy:
   */
}
