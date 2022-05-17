/*
 * Copyright (c) 2022 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb30.lite.tx.cm.stateless.rw;

import com.sun.ts.tests.ejb30.lite.tx.cm.common.RWJsfClientBase;
import com.sun.ts.tests.ejb30.lite.tx.cm.common.RWTestBeanBase;

import jakarta.ejb.EJB;

@jakarta.inject.Named("client")
@jakarta.enterprise.context.RequestScoped
public class JsfClient extends RWJsfClientBase {

  @SuppressWarnings("unused")
  @EJB(beanInterface = TestBean.class, beanName = "TestBean")
  private void setTestBean(RWTestBeanBase b) {
    testBean = b;
  }

  /*
   * @testName: mandatory
   * 
   * @test_Strategy:
   */
  /*
   * @testName: required
   * 
   * @test_Strategy:
   */
  /*
   * @testName: requiredNoExistingTransaction
   * 
   * @test_Strategy:
   */
  /*
   * @testName: supports
   * 
   * @test_Strategy:
   */
  /*
   * @testName: requiresNew
   * 
   * @test_Strategy:
   */
}
