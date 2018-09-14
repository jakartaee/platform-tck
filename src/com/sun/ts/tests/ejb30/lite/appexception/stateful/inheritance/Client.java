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
package com.sun.ts.tests.ejb30.lite.appexception.stateful.inheritance;

import com.sun.ts.tests.ejb30.lite.appexception.common.inheritance.InheritanceIF;

public class Client extends
    com.sun.ts.tests.ejb30.lite.appexception.common.inheritance.ClientBase {

  @Override
  protected InheritanceIF getBean() {
    return (InheritanceIF) lookup(BEAN_LOOKUP_NAME, "InheritanceBean", null);
  }

  /*
   * @testName: uncheckedAppException1
   * 
   * @test_Strategy:
   */
  /*
   * @testName: uncheckedAppException2
   * 
   * @test_Strategy:
   */
  /*
   * @testName: uncheckedAppException3
   * 
   * @test_Strategy:
   */
  /*
   * @testName: uncheckedSystemException4
   * 
   * @test_Strategy:
   */
  /*
   * @testName: uncheckedSystemException5
   * 
   * @test_Strategy:
   */
  /*
   * @testName: uncheckedAppException6
   * 
   * @test_Strategy:
   */
  /*
   * @testName: uncheckedAppException7
   * 
   * @test_Strategy:
   */
}
