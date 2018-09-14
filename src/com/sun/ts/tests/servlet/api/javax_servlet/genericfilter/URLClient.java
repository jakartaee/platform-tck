/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
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
 * $Id: URLClient.java 62833 2011-05-18 13:13:23Z djiao $
 */

package com.sun.ts.tests.servlet.api.javax_servlet.genericfilter;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.tests.servlet.common.client.AbstractUrlClient;

public class URLClient extends AbstractUrlClient {
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

    setContextRoot("/servlet_js_genericfilter_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Run test */
  /*
   * @testName: initFilterTest
   *
   * @assertion_ids: NA;
   *
   * @test_Strategy: Client attempts to access a servlet and the filter
   * configured for that servlet should be invoked.
   */

  public void initFilterTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "InitFilterTest");
    invoke();
  }

  /*
   * @testName: initFilterConfigTest
   *
   * @assertion_ids: NA;
   *
   * @test_Strategy: Client attempts to access a servlet and the filter
   * configured for that servlet should be invoked.
   */

  public void initFilterConfigTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "InitFilterConfigTest");
    invoke();
  }

  /*
   * @testName: GetFilterNameTest
   *
   * @assertion_ids: NA;
   *
   * @test_Strategy: Client attempts to access a servlet and the filter
   * configured for that servlet should be invoked.
   */

  public void GetFilterNameTest() throws Fault {
    String testName = "GetFilterNameTest";
    TEST_PROPS.setProperty(APITEST, testName);
    invoke();
  }

  /*
   * @testName: GetInitParamTest
   *
   * @assertion_ids: NA;
   *
   * @test_Strategy: Client attempts to access a servlet and the filter
   * configured for that servlet should be invoked.
   */

  public void GetInitParamTest() throws Fault {
    String testName = "GetInitParamTest";
    TEST_PROPS.setProperty(APITEST, testName);
    invoke();
  }

  /*
   * @testName: GetInitParamNamesTest
   *
   * @assertion_ids: NA;
   *
   * @test_Strategy: Client attempts to access a servlet and the filter
   * configured for that servlet should be invoked.
   */

  public void GetInitParamNamesTest() throws Fault {
    String testName = "GetInitParamNamesTest";
    TEST_PROPS.setProperty(APITEST, testName);
    invoke();
  }

  /*
   * @testName: GetInitParamNamesNullTest
   *
   * @assertion_ids: NA;
   *
   * @test_Strategy: Client attempts to access a servlet and the filter
   * configured for that servlet should be invoked.
   */

  public void GetInitParamNamesNullTest() throws Fault {
    String testName = "GetInitParamNamesNullTest";
    TEST_PROPS.setProperty(APITEST, testName);
    invoke();
  }

  /*
   * @testName: GetInitParamNullTest
   *
   * @assertion_ids: NA;
   *
   * @test_Strategy: Client attempts to access a servlet and the filter
   * configured for that servlet should be invoked.
   */

  public void GetInitParamNullTest() throws Fault {
    String testName = "GetInitParamNullTest";
    TEST_PROPS.setProperty(APITEST, testName);
    invoke();
  }

  /*
   * @testName: GetServletContextTest
   *
   * @assertion_ids: NA;
   *
   * @test_Strategy: Client attempts to access a servlet and the filter
   * configured for that servlet should be invoked.
   */

  public void GetServletContextTest() throws Fault {
    String testName = "GetServletContextTest";
    TEST_PROPS.setProperty(APITEST, testName);
    invoke();
  }
}
