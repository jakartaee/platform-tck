/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.servlet.api.javax_servlet.servletconfig;

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

    setServletName("TestServlet");
    setContextRoot("/servlet_js_servletconfig_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Run test */

  /*
   * @testName: getServletConfigInitParameterNamesTest
   *
   * @assertion_ids: Servlet:SPEC:7; Servlet:JAVADOC:266; Servlet:JAVADOC:261;
   *
   * @test_Strategy: Set init parameters in the web.xml file and check for the
   * enumerated values in the servlet.
   */
  public void getServletConfigInitParameterNamesTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getServletConfigInitParameterNames");
    invoke();
  }

  /*
   * @testName: getServletConfigInitParameterTest
   *
   * @assertion_ids: Servlet:SPEC:7; Servlet:JAVADOC:266; Servlet:JAVADOC:259;
   *
   * @test_Strategy: Set init parameters in the web.xml file and check for the
   * value in the servlet.
   */
  public void getServletConfigInitParameterTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getServletConfigInitParameter");
    invoke();
  }

  /*
   * @testName: getServletConfigInitParameterTestNull
   *
   * @assertion_ids: Servlet:SPEC:7; Servlet:JAVADOC:266; Servlet:JAVADOC:260;
   *
   * @test_Strategy: Set No init parameter anywhere named:
   * "Nothing_is_set_for_Negative_compatibility_test_only" anywhere and check
   * for the Verify that ServletConfig.getInitParameter(name) return null.
   */
  public void getServletConfigInitParameterTestNull() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getServletConfigInitParameterNull");
    invoke();
  }

  /*
   * @testName: getServletContextTest
   *
   * @assertion_ids: Servlet:SPEC:7; Servlet:JAVADOC:266; Servlet:JAVADOC:258;
   * Servlet:JAVADOC:219;
   *
   * @test_Strategy: Try to get the ServletContext for this servlet itself
   */
  public void getServletContextTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getServletContext");
    invoke();
  }

  /*
   * @testName: getServletNameTest
   *
   * @assertion_ids: Servlet:SPEC:7; Servlet:JAVADOC:266; Servlet:JAVADOC:257;
   *
   * @test_Strategy: Try to get the ServletName for this servlet itself
   */
  public void getServletNameTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getServletName");
    invoke();
  }
}
