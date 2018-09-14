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

package com.sun.ts.tests.jsp.spec.core_syntax.implicitobjects;

import java.io.PrintWriter;
import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
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

    setGeneralURI("/jsp/spec/core_syntax/implicitobjects");
    setContextRoot("/jsp_coresyntx_implicitobjects_web");

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
   * @assertion_ids: JSP:SPEC:15
   * 
   * @test_Strategy: Validate that the object associated with the session
   * scripting variable is of type javax.servlet.http.HttpSession and that a
   * method can be called against it.
   */

  public void checkSessionTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "checkSession");
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
    TEST_PROPS.setProperty(TEST_NAME, testName + "Test");
    TEST_PROPS.setProperty(GOLDENFILE, testName + ".gf");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_implicitobjects_web/" + testName + " HTTP/1.0");
    invoke();
  }

  /*
   * @testName: checkExceptionTest
   * 
   * @assertion_ids: JSP:SPEC:17
   * 
   * @test_Strategy: Validate that the object associated with the exception
   * scripting variable is of an instance of the exception type thrown (a
   * subclass of java.lang.Throwable).
   */

  public void checkExceptionTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "checkException");
    TEST_PROPS.setProperty(IGNORE_STATUS_CODE, "true");
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
    TEST_PROPS.setProperty(STANDARD, "checkOut");
    invoke();
  }

  /*
   * @testName: checkPageTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that the object associated with the page scripting
   * variable is of type java.lang.Object.
   */

  public void checkPageTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "checkPage");
    invoke();
  }

  /*
   * @testName: checkPageContextTest
   * 
   * @assertion_ids: JSP:SPEC:14
   * 
   * @test_Strategy: Validate that the object associated with the pageContext
   * scripting variable is of type javax.servlet.jsp.PageContext and that a
   * method can be called against it.
   */

  public void checkPageContextTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "checkPageContext");
    invoke();
  }

  /*
   * @testName: checkRequestTest
   * 
   * @assertion_ids: JSP:SPEC:12
   * 
   * @test_Strategy: Validate that the object associated with the request
   * scripting variable is of type javax.servlet.Request (parent class of
   * HttpServletRequest) and that a method can be called against it.
   */

  public void checkRequestTest() throws Fault {
    String testName = "checkRequest";
    TEST_PROPS.setProperty(TEST_NAME, testName + "Test");
    TEST_PROPS.setProperty(GOLDENFILE, testName + ".gf");
    TEST_PROPS.setProperty(REQUEST, "GET /jsp_coresyntx_implicitobjects_web/"
        + testName + ".jsp?Years=2 HTTP/1.0");
    invoke();
  }

  /*
   * @testName: checkResponseTest
   * 
   * @assertion_ids: JSP:SPEC:13
   * 
   * @test_Strategy: Validate that the object associated with the response
   * scripting variable is of type javax.servlet.Response (parent class of
   * HttpServletResponse) and that a method can be called against it.
   */

  public void checkResponseTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "checkResponse");
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "TestHeader:Method call OK");
    invoke();
  }

  /*
   * @testName: checkApplicationTest
   * 
   * @assertion_ids: JSP:SPEC:16
   * 
   * @test_Strategy: Validate that the object associated with the application
   * scripting variable is of type javax.servlet.ServletContext that a method
   * can be called against it.
   */

  public void checkApplicationTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "checkApplication");
    invoke();
  }

}
