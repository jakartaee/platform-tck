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

package com.sun.ts.tests.jsp.spec.tagfiles.implicitobjects;

import java.io.PrintWriter;
import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

public class URLClient extends AbstractUrlClient {
  private static final String CONTEXT_ROOT = "/jsp_tagfiles_implicitobjects_web";

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
   * @testName: checkSessionTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that the object associated with the session
   * scripting variable is of type javax.servlet.http.HttpSession and that a
   * method can be called against it.
   */

  public void checkSessionTest() throws Fault {
    String testName = "checkSession";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "true|checkSession");
    invoke();
  }

  /*
   * @testName: checkConfigTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that the object associated with the config
   * scripting variable is of type javax.servlet.ServletConfig and that a method
   * can be called against it.
   */

  public void checkConfigTest() throws Fault {
    String testName = "checkConfig";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/checkConfig" + " HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "true|param1 is:|hobbit|param2 is:|gollum");
    invoke();
  }

  /*
   * @testName: checkOutTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that the object associated with the out scripting
   * variable is of type javax.servlet.jsp.JspWriter.
   */

  public void checkOutTest() throws Fault {
    String testName = "checkOut";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "true");
    invoke();
  }

  /*
   * @testName: checkJspContextTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that the object associated with the jspContext
   * scripting variable is of type javax.servlet.jsp.JspContext and that a
   * method can be called against it.
   */

  public void checkJspContextTest() throws Fault {
    String testName = "checkJspContext";
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagfiles_implicitobjects_web/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "true|true");
    invoke();
  }

  /*
   * @testName: checkRequestTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that the object associated with the request
   * scripting variable is of type javax.servlet.Request (parent class of
   * HttpServletRequest) and that a method can be called against it.
   */

  public void checkRequestTest() throws Fault {
    String testName = "checkRequest";
    TEST_PROPS.setProperty(REQUEST, "GET /jsp_tagfiles_implicitobjects_web/"
        + testName + ".jsp?Years=2 HTTP/1.0");
    TEST_PROPS.setProperty(SEARCH_STRING, "true|HTTP|1.0|2");
    invoke();
  }

  /*
   * @testName: checkResponseTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that the object associated with the response
   * scripting variable is of type javax.servlet.Response (parent class of
   * HttpServletResponse) and that a method can be called against it.
   */

  public void checkResponseTest() throws Fault {
    String testName = "checkResponse";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "true");
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "TestHeader:Method call OK");
    invoke();
  }

  /*
   * @testName: checkApplicationTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that the object associated with the application
   * scripting variable is of type javax.servlet.ServletContext that a method
   * can be called against it.
   */
  public void checkApplicationTest() throws Fault {
    String testName = "checkApplication";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "true|param1|bilbo");
    invoke();
  }

}
