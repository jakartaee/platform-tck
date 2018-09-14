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
package com.sun.ts.tests.ejb30.lite.appexception.stateless.override;

import javax.ejb.EJB;

import com.sun.ts.tests.ejb30.common.appexception.AppExceptionIF;

public class Client
    extends com.sun.ts.tests.ejb30.lite.appexception.common.ClientBase {

  @Override
  @EJB(beanInterface = NoInterfaceAppExceptionBean.class, beanName = "NoInterfaceAppExceptionBean")
  protected void setNoInterfaceBean(AppExceptionIF noInterfaceBean) {
    this.noInterfaceBean = noInterfaceBean;
  }

  // tests used by both */appexception/annotated/ and */appexception/override/

  /*
   * @testName: atCheckedRollbackAppExceptionTest
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: atCheckedRollbackAppExceptionTestLocal
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: atUncheckedRollbackAppExceptionTest
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: atUncheckedRollbackAppExceptionTestLocal
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: checkedAppExceptionTest
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: checkedAppExceptionTest2
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: checkedAppExceptionTestLocal
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: checkedRollbackAppExceptionTest
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: checkedRollbackAppExceptionTestLocal
   * 
   * @test_Strategy:
   *
   */

  // tests used by */appexception/override/

  /*
   * @testName: uncheckedAppExceptionTest
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: uncheckedAppExceptionTest2
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: uncheckedAppExceptionTestLocal
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: uncheckedRollbackAppExceptionTest
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: uncheckedRollbackAppExceptionTestLocal
   * 
   * @test_Strategy:
   *
   */
}
