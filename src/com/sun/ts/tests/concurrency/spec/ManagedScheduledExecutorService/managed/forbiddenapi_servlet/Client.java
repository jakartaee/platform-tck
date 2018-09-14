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

package com.sun.ts.tests.concurrency.spec.ManagedScheduledExecutorService.managed.forbiddenapi_servlet;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.util.TestUtil;
import java.util.*;
import java.net.*;
import com.sun.ts.tests.concurrency.common.*;

public class Client extends EETest {

  private static final String urlString = "/forbiddenapi_web/testServlet";

  private static final String PROTOCOL = "http";

  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private String hostname;

  private int portnum;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /**
   * @class.setup_props: all.props; all properties;
   */
  public void setup(String[] args, Properties p) throws Fault {
    try {
      hostname = p.getProperty(WEBSERVERHOSTPROP);
      portnum = Integer.parseInt(p.getProperty(WEBSERVERPORTPROP));
    } catch (Exception e) {
      throw new Fault(e);
    }
  }

  public void cleanup() throws Fault {
    TestUtil.logTrace("cleanup");
  }

  /*
   * @testName: testAwaitTermination
   * 
   * @assertion_ids: CONCURRENCY:SPEC:57.1
   * 
   * @test_Strategy:
   */
  public void testAwaitTermination() throws Fault {
    try {
      ConcurrencyTestUtils.sendClientRequest2Url(PROTOCOL, hostname, portnum,
          urlString,
          ConcurrencyTestUtils.SERVLET_OP_FORBIDDENAPI_TESTAWAITTERMINATION);
    } catch (Exception e) {
      throw new Fault(e);
    }
  }

  /*
   * @testName: testIsShutdown
   * 
   * @assertion_ids: CONCURRENCY:SPEC:57.2
   * 
   * @test_Strategy:
   */
  public void testIsShutdown() throws Fault {
    try {
      ConcurrencyTestUtils.sendClientRequest2Url(PROTOCOL, hostname, portnum,
          urlString,
          ConcurrencyTestUtils.SERVLET_OP_FORBIDDENAPI_TESTISSHUTDOWN);
    } catch (Exception e) {
      throw new Fault(e);
    }
  }

  /*
   * @testName: testIsTerminated
   * 
   * @assertion_ids: CONCURRENCY:SPEC:57.3
   * 
   * @test_Strategy:
   */
  public void testIsTerminated() throws Fault {
    try {
      ConcurrencyTestUtils.sendClientRequest2Url(PROTOCOL, hostname, portnum,
          urlString,
          ConcurrencyTestUtils.SERVLET_OP_FORBIDDENAPI_TESTISTERMINATED);
    } catch (Exception e) {
      throw new Fault(e);
    }
  }

  /*
   * @testName: testShutdown
   * 
   * @assertion_ids: CONCURRENCY:SPEC:57.4
   * 
   * @test_Strategy:
   */
  public void testShutdown() throws Fault {
    try {
      ConcurrencyTestUtils.sendClientRequest2Url(PROTOCOL, hostname, portnum,
          urlString, ConcurrencyTestUtils.SERVLET_OP_FORBIDDENAPI_TESTSHUTDOWN);
    } catch (Exception e) {
      throw new Fault(e);
    }
  }

  /*
   * @testName: testShutdownNow
   * 
   * @assertion_ids: CONCURRENCY:SPEC:57.5
   * 
   * @test_Strategy:
   */
  public void testShutdownNow() throws Fault {
    try {
      ConcurrencyTestUtils.sendClientRequest2Url(PROTOCOL, hostname, portnum,
          urlString,
          ConcurrencyTestUtils.SERVLET_OP_FORBIDDENAPI_TESTSHUTDOWNNOW);
    } catch (Exception e) {
      throw new Fault(e);
    }
  }
}
