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

package com.sun.ts.tests.jsp.spec.configuration.general;

import java.io.PrintWriter;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

public class URLClient extends AbstractUrlClient {
  private static final String CONTEXT_ROOT = "/jsp_config_general_web";

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    URLClient theTests = new URLClient();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  /**
   * Entry point for same-VM execution. In different-VM execution, the main
   * method delegates to this method.
   */
  public Status run(String args[], PrintWriter out, PrintWriter err) {

    setContextRoot(CONTEXT_ROOT);

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: moreSpecificMappingTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy:
   */

  public void moreSpecificMappingTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/specific/svr/willNotSee.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "In mapped servlet");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/specific/svr/page/willSee.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "In coda1|In coda1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "In mapped servlet");
    invoke();
  }

  /*
   * @testName: identicalMappingTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy:
   */

  public void identicalMappingTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/identical/willSee.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "In coda1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "In mapped servlet");
    invoke();
  }

  /*
   * @testName: mostSpecificMappingTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy:
   */

  public void mostSpecificMappingTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/mostSpecific/page/willSee.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "In prelude1|In prelude2|In prelude3|el is enabled|In coda1|In coda2|In coda3");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "In mapped servlet");
    invoke();
  }
}
