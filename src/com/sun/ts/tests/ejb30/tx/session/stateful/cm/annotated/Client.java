/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb30.tx.session.stateful.cm.annotated;

import com.sun.ts.tests.ejb30.tx.common.session.cm.ClientBase;
import com.sun.javatest.Status;
import com.sun.ts.tests.ejb30.tx.common.session.cm.TestIF;
import jakarta.ejb.EJB;

public class Client extends ClientBase {

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  @Override
  public void cleanup() {
    removeBeans();
  }

  /*
   * @testName: mandatoryTest
   * 
   * @assertion_ids: EJB:JAVADOC:233; EJB:JAVADOC:234; EJB:JAVADOC:235;
   * EJB:JAVADOC:236; EJB:JAVADOC:237; EJB:JAVADOC:238
   * 
   * @test_Strategy: client -> remote bm slsb -> remote cm slsb
   */

  /*
   * @testName: localMandatoryTest
   * 
   * @assertion_ids: EJB:JAVADOC:233; EJB:JAVADOC:234; EJB:JAVADOC:235;
   * EJB:JAVADOC:236; EJB:JAVADOC:237; EJB:JAVADOC:238
   * 
   * @test_Strategy: client -> remote bm slsb -> local cm slsb
   */

  /*
   * @testName: neverTest
   * 
   * @assertion_ids: EJB:JAVADOC:233; EJB:JAVADOC:234; EJB:JAVADOC:235;
   * EJB:JAVADOC:236; EJB:JAVADOC:237; EJB:JAVADOC:238
   * 
   * @test_Strategy: client -> remote bm slsb -> remote cm slsb
   */

  /*
   * @testName: localNeverTest
   * 
   * @assertion_ids: EJB:JAVADOC:233; EJB:JAVADOC:234; EJB:JAVADOC:235;
   * EJB:JAVADOC:236; EJB:JAVADOC:237; EJB:JAVADOC:238
   * 
   * @test_Strategy: client -> remote bm slsb -> local cm slsb
   */

  /*
   * @testName: localRequiresNewRemoveTest
   * 
   * @assertion_ids: EJB:JAVADOC:233; EJB:JAVADOC:234; EJB:JAVADOC:235;
   * EJB:JAVADOC:236; EJB:JAVADOC:237; EJB:JAVADOC:238
   * 
   * @test_Strategy: client -> remote bm slsb -> local cm sfsb
   */

  /*
   * @testName: supportsTest
   * 
   * @test_Strategy: client -> remote bm sfsb -> remote cm sfsb
   */

  /*
   * @testName: localSupportsTest
   * 
   * @test_Strategy: client -> remote bm sfsb -> local cm sfsb
   */

  /*
   * @testName: illegalGetSetRollbackOnlyNeverTest
   * 
   * @test_Strategy: client -> remote bm bean -> local cm bean
   */

  /*
   * @testName: localIllegalGetSetRollbackOnlyNeverTest
   * 
   * @test_Strategy: client -> remote bm bean -> local cm bean
   */

  /*
   * @testName: illegalGetSetRollbackOnlyNotSupportedTest
   * 
   * @test_Strategy: client -> remote bm bean -> local cm bean
   */

  /*
   * @testName: localIllegalGetSetRollbackOnlyNotSupportedTest
   * 
   * @test_Strategy: client -> remote bm bean -> local cm bean
   */

  /*
   * @testName: systemExceptionTest
   * 
   * @test_Strategy: client -> remote bm bean -> local cm bean
   */
}
