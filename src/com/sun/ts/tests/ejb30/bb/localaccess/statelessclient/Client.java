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

package com.sun.ts.tests.ejb30.bb.localaccess.statelessclient;

import java.util.Properties;
import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.ejb30.bb.localaccess.common.ClientBase;
import static com.sun.ts.tests.ejb30.bb.localaccess.common.Constants.CLIENT_MSG;
import com.sun.ts.tests.ejb30.bb.localaccess.common.TestBeanIF;
import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import javax.ejb.EJB;

public class Client extends ClientBase {
  @EJB
  private static TestBeanIF bean;

  protected TestBeanIF getBean() {
    return bean;
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void cleanup() {
  }

  /*
   * @testName: passByValueTest
   * 
   * @assertion_ids: EJB:JAVADOC:84
   * 
   * @test_Strategy: the (remote) test bean modifies the value of String array.
   * This change should not affect the value in Client.
   */

  /////////////////////////////////////////////////////////////////////////
  // 1 localStateless
  /////////////////////////////////////////////////////////////////////////
  /*
   * @testName: passByReferenceTest1
   * 
   * @assertion_ids: EJB:JAVADOC:84
   * 
   * @test_Strategy:
   */

  /*
   * @testName: exceptionTest1
   * 
   * @assertion_ids: EJB:JAVADOC:84
   * 
   * @test_Strategy:
   */

  /*
   * @testName: runtimeExceptionTest1
   * 
   * @assertion_ids: EJB:JAVADOC:84; EJB:JAVADOC:87; EJB:JAVADOC:88
   * 
   * @test_Strategy:
   */

  /////////////////////////////////////////////////////////////////////////
  // 2 defaultdefaultLocalStateless
  /////////////////////////////////////////////////////////////////////////
  /*
   * @testName: passByReferenceTest2
   * 
   * @assertion_ids: EJB:JAVADOC:84
   * 
   * @test_Strategy:
   */

  /*
   * @testName: exceptionTest2
   * 
   * @assertion_ids: EJB:JAVADOC:84
   * 
   * @test_Strategy:
   */

  /*
   * @testName: runtimeExceptionTest2
   * 
   * @assertion_ids: EJB:JAVADOC:84; EJB:JAVADOC:87; EJB:JAVADOC:88
   * 
   * @test_Strategy:
   */

  /////////////////////////////////////////////////////////////////////////
  // 3 localStateful
  /////////////////////////////////////////////////////////////////////////
  /*
   * @testName: passByReferenceTest3
   * 
   * @assertion_ids: EJB:JAVADOC:84
   * 
   * @test_Strategy:
   */

  /*
   * @testName: exceptionTest3
   * 
   * @assertion_ids: EJB:JAVADOC:84
   * 
   * @test_Strategy:
   */

  /*
   * @testName: runtimeExceptionTest3
   * 
   * @assertion_ids: EJB:JAVADOC:84; EJB:JAVADOC:87; EJB:JAVADOC:88
   * 
   * @test_Strategy:
   */

  /////////////////////////////////////////////////////////////////////////
  // 4 defaultdefaultLocalStateful
  /////////////////////////////////////////////////////////////////////////
  /*
   * @testName: passByReferenceTest4
   * 
   * @assertion_ids: EJB:JAVADOC:84
   * 
   * @test_Strategy:
   */

  /*
   * @testName: exceptionTest4
   * 
   * @assertion_ids: EJB:JAVADOC:84
   * 
   * @test_Strategy:
   */

  /*
   * @testName: runtimeExceptionTest4
   * 
   * @assertion_ids: EJB:JAVADOC:84; EJB:JAVADOC:87; EJB:JAVADOC:88
   * 
   * @test_Strategy:
   */

  //////////////////////////////////////////////////////////////////////
  // localStateless2
  //////////////////////////////////////////////////////////////////////
  /*
   * @testName: passByReferenceTest5
   * 
   * @assertion_ids: EJB:JAVADOC:84
   * 
   * @test_Strategy:
   */
  /*
   * @testName: exceptionTest5
   * 
   * @assertion_ids: EJB:JAVADOC:84
   * 
   * @test_Strategy:
   */
  /*
   * @testName: runtimeExceptionTest5
   * 
   * @assertion_ids: EJB:JAVADOC:84; EJB:JAVADOC:87; EJB:JAVADOC:88
   * 
   * @test_Strategy:
   */

}
