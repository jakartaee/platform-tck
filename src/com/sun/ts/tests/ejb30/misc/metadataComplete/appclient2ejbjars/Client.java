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

package com.sun.ts.tests.ejb30.misc.metadataComplete.appclient2ejbjars;

import com.sun.ts.tests.ejb30.common.helper.TLogger;
import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;

import com.sun.ts.tests.ejb30.common.calc.RemoteCalculator;

public class Client extends
    com.sun.ts.tests.ejb30.misc.metadataComplete.appclientejbjars.Client {
  // injected in descriptor
  private static RemoteCalculator statelessAnnotationUsedRemoteCalculatorBean;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @testName: annotationNotProcessedForStateless
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   */

  /*
   * @testName: annotationNotProcessedForStateful
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   */

  /*
   * @testName: annotationNotProcessedForAppclient
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   */

  /*
   * @testName: annotationProcessedForStateless
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   */
  public void annotationProcessedForStateless() throws Fault {
    int param1 = 0;
    int param2 = 0;
    int additionByInterceptor = 100 + 100;
    int postConstructCallsCount = 1;
    int expected = param1 + param2 + additionByInterceptor
        + postConstructCallsCount;
    int actual = statelessAnnotationUsedRemoteCalculatorBean.remoteAdd(param1,
        param2);
    if (actual == expected) {
      TLogger.log("Got expected result : " + actual);
    } else {
      throw new Fault(
          "Expecting result: " + expected + ", but actual is " + actual);
    }
  }
}
