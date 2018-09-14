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
package com.sun.ts.tests.ejb30.bb.session.stateful.annotation.appexception.override;

import com.sun.javatest.Status;

import com.sun.ts.tests.ejb30.common.appexception.ClientBase;

public class Client extends ClientBase {
  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  } //////////////////////////////////////////////////////////////////////
  /*
   * @testName: checkedAppExceptionTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: checkedAppExceptionTest2
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: checkedAppExceptionTestLocal
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: uncheckedAppExceptionTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: uncheckedAppExceptionTest2
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: uncheckedAppExceptionTestLocal
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: checkedRollbackAppExceptionTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: checkedRollbackAppExceptionTestLocal
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: uncheckedRollbackAppExceptionTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: uncheckedRollbackAppExceptionTestLocal
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */

  // omit tests "at*" that don't involve rollback attribute override

  /*
   * @testName: atCheckedRollbackAppExceptionTest
   * 
   * @assertion_ids:EJB:JAVADOC:6
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: atCheckedRollbackAppExceptionTestLocal
   * 
   * @assertion_ids:EJB:JAVADOC:6
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: atUncheckedRollbackAppExceptionTest
   * 
   * @assertion_ids:EJB:JAVADOC:6
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: atUncheckedRollbackAppExceptionTestLocal
   * 
   * @assertion_ids:EJB:JAVADOC:6
   * 
   * @test_Strategy:
   *
   */
}
