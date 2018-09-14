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

/*
 * @(#)URLClient.java	1.36 02/11/04
 */

package com.sun.ts.tests.jsp.spec.configuration.encoding;

import java.io.PrintWriter;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

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

    setContextRoot("/jsp_config_encode_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: jspConfigurationEncodingTest
   * 
   * @assertion_ids: JSP:SPEC:253
   * 
   * @test_Strategy: Validate that if a JSP configuration element specifies an
   * encoding for a group of pages, the encoding is properly set for those pages
   * when no encoding is set by the page(s) themselves.
   */
  public void jspConfigurationEncodingTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_encode_web/NoEncodingSpecifiedTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Content-Type:text/plain;charset=UTF-8");
    invoke();
  }

  /*
   * @testName: jspConfigurationSameEncodingTest
   * 
   * @assertion_ids: JSP:SPEC:146
   * 
   * @test_Strategy: Validate that if a JSP configuration element specifies an
   * encoding for a group of pages, and the target page specifies the same
   * encoding as that of the property group, no translation error occurs and the
   * encoding is properly set.
   */
  public void jspConfigurationSameEncodingTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_encode_web/SameEncodingSpecifiedTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Content-Type:text/plain;charset=UTF-8");
    invoke();
  }

  /*
   * @testName: jspConfigurationDifferentEncodingTest
   * 
   * @assertion_ids: JSP:SPEC:145
   * 
   * @test_Strategy: Validate that if a JSP configuration element specifies an
   * encoding for a group of pages, and the target page specifies a different
   * encoding as that of the property group, a translation error occurs.
   */
  public void jspConfigurationDifferentEncodingTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_encode_web/DifferentEncodingSpecifiedTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_encode_web/DifferentEncodingSpecifiedTest.jspx HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }
}
