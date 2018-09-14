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

package com.sun.ts.tests.jsp.spec.configuration.includes;

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

    setContextRoot("/jsp_config_includes_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: jspConfigurationIncludesTest
   * 
   * @assertion_ids: JSP:SPEC:147;JSP:SPEC:148;JSP:SPEC:149
   * 
   * @test_Strategy: Validate the following: - The container properly recognizes
   * prelude and coda configuration elements. - Prelude includes are includes at
   * the beginning of the target JSP(s) identified by the url-pattern, and are
   * included in the order they appear in the deployment descriptor. - Coda
   * includes are inserted at the end of the target JSP(s) identified by the
   * url-pattern, and are included in the order they appear in the deployment
   * descriptor. - Validate with both standard syntax JSPs and JSP documents.
   */
  public void jspConfigurationIncludesTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_includes_web/IncludesTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Prelude1|JSP Body|Coda1");
    invoke();
  }

  /*
   * @testName: jspConfigurationIncludes2Test
   * 
   * @assertion_ids: JSP:SPEC:147;JSP:SPEC:148;JSP:SPEC:149
   * 
   * @test_Strategy: Validate the same as above test.
   */
  public void jspConfigurationIncludes2Test() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_includes_web/two/Includes2Test.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "Prelude1|Prelude2|JSP Body 2|Coda1|Coda2");
    invoke();
  }

  /*
   * @testName: jspConfigurationIncludes3Test
   * 
   * @assertion_ids: JSP:SPEC:147;JSP:SPEC:148;JSP:SPEC:149
   * 
   * @test_Strategy: Validate the same as above test. JSP.1.10.4 states that
   * implicit includes can use either the same syntax as the including page, or
   * a different syntax.
   */
  public void jspConfigurationIncludes3Test() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_includes_web/three/Includes3Test.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "Prelude2|Prelude3|JSP Body 3|Coda2|Coda3");
    invoke();
  }
}
