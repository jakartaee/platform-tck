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

package com.sun.ts.tests.concurrency.spec.ManagedScheduledExecutorService.inheritedapi;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.util.TestUtil;
import java.util.*;
import javax.ejb.EJB;
import com.sun.ts.tests.concurrency.api.common.counter.*;

public class Client extends EETest {

  @EJB(beanName = "TestEjb")
  static private TestEjbRemote testEjb;

  @EJB(beanName = "CounterSingleton")
  static private CounterRemote counter;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup");
    counter.reset();
  }

  public void cleanup() throws Fault {
    TestUtil.logTrace("cleanup");
  }

  /*
   * @testName: testApiSubmit
   * 
   * @assertion_ids: CONCURRENCY:SPEC:44.1
   * 
   * @test_Strategy:
   */
  public void testApiSubmit() throws Fault {
    testEjb.testApiSubmit();
  }

  /*
   * @testName: testApiExecute
   * 
   * @assertion_ids: CONCURRENCY:SPEC:44.2
   * 
   * @test_Strategy:
   */
  public void testApiExecute() throws Fault {
    testEjb.testApiExecute();
  }

  /*
   * @testName: testApiInvokeAll
   * 
   * @assertion_ids: CONCURRENCY:SPEC:44.3
   * 
   * @test_Strategy:
   */
  public void testApiInvokeAll() throws Fault {
    testEjb.testApiInvokeAll();
  }

  /*
   * @testName: testApiInvokeAny
   * 
   * @assertion_ids: CONCURRENCY:SPEC:44.4
   * 
   * @test_Strategy:
   */
  public void testApiInvokeAny() throws Fault {
    testEjb.testApiInvokeAny();
  }

  /*
   * @testName: testApiSchedule
   * 
   * @assertion_ids: CONCURRENCY:SPEC:44.5
   * 
   * @test_Strategy:
   */
  public void testApiSchedule() throws Fault {
    testEjb.testApiSchedule();
  }

  /*
   * @testName: testApiScheduleAtFixedRate
   * 
   * @assertion_ids: CONCURRENCY:SPEC:44.6
   * 
   * @test_Strategy:
   */
  public void testApiScheduleAtFixedRate() throws Fault {
    testEjb.testApiScheduleAtFixedRate();
  }

  /*
   * @testName: testApiScheduleWithFixedDelay
   * 
   * @assertion_ids: CONCURRENCY:SPEC:44.7
   * 
   * @test_Strategy:
   */
  public void testApiScheduleWithFixedDelay() throws Fault {
    testEjb.testApiScheduleWithFixedDelay();
  }

}
