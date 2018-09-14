/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.concurrency.spec.ManagedScheduledExecutorService.managed.forbiddenapi;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.util.TestUtil;
import java.util.*;
import javax.ejb.EJB;

public class Client extends EETest {

  @EJB(beanName = "TestEjb", name = "testEjb")
  static private TestEjbRemote testEjb;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup");
  }

  public void cleanup() throws Fault {
    TestUtil.logTrace("cleanup");
  }

  /*
   * @testName: testAwaitTermination
   * 
   * @assertion_ids: CONCURRENCY:SPEC:56;CONCURRENCY:SPEC:57.1
   * 
   * @test_Strategy:
   */
  public void testAwaitTermination() throws Fault {
    testEjb.testAwaitTermination();
  }

  /*
   * @testName: testIsShutdown
   * 
   * @assertion_ids: CONCURRENCY:SPEC:56;CONCURRENCY:SPEC:57.2
   * 
   * @test_Strategy:
   */
  public void testIsShutdown() throws Fault {
    testEjb.testIsShutdown();
  }

  /*
   * @testName: testIsTerminated
   * 
   * @assertion_ids: CONCURRENCY:SPEC:56;CONCURRENCY:SPEC:57.3
   * 
   * @test_Strategy:
   */
  public void testIsTerminated() throws Fault {
    testEjb.testIsTerminated();
  }

  /*
   * @testName: testShutdown
   * 
   * @assertion_ids: CONCURRENCY:SPEC:56;CONCURRENCY:SPEC:57.4
   * 
   * @test_Strategy:
   */
  public void testShutdown() throws Fault {
    testEjb.testShutdown();
  }

  /*
   * @testName: testShutdownNow
   * 
   * @assertion_ids: CONCURRENCY:SPEC:56;CONCURRENCY:SPEC:57.5
   * 
   * @test_Strategy:
   */
  public void testShutdownNow() throws Fault {
    testEjb.testShutdownNow();
  }
}
