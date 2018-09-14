/*
 * Copyright (c) 2012, 2018 Oracle and/or its affiliates. All rights reserved.
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
 * $Id:$
 */
package com.sun.ts.tests.servlet.spec.annotationservlet.webservletapi;

import com.sun.javatest.Status;
import com.sun.ts.tests.servlet.common.client.AbstractUrlClient;
import java.io.PrintWriter;

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
    setContextRoot("/servlet_annotationservlet_webservletapi_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Run test */

  /*
   * @testName: test1
   *
   * @assertion_ids: Servlet:SPEC:221; Servlet:SPEC:221.1; Servlet:SPEC:221.2;
   * Servlet:SPEC:221.3; Servlet:SPEC:221.6; Servlet:SPEC:221.7;
   * Servlet:SPEC:221.9; Servlet:SPEC:221.10; Servlet:JAVADOC:825;
   *
   * @test_Strategy: Create a servlet Servlet1; Define Servlet1 using
   * annotation @WebServlet(String); Add Servlet1 to ServletContext
   * programmatically using different name; Invoke Servlet1 at the URL specified
   * by @WebServlet; Verify that it works as defined at @WebServlet; Invoke
   * Servlet1 at the URL specified by API programing; Veriy Servlet1 is invoked
   * properly; Verify that servlet name is set correctly; Verify that servlet
   * name is set to the default name;
   */
  public void test1() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/Servlet1URL HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Servlet1_INVOKED"
        + "|servletname=com.sun.ts.tests.servlet.spec.annotationservlet.webservlet.Servlet1"
        + "|isAsyncSupported=false");
    invoke();

    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/Servlet1APIURL HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "Servlet1_INVOKED" + "|servletname=Servlet1API|isAsyncSupported=false");
    invoke();
  }

  /*
   * @testName: test2
   *
   * @assertion_ids: Servlet:SPEC:221; Servlet:SPEC:221.1; Servlet:SPEC:221.2;
   * Servlet:SPEC:221.3; Servlet:SPEC:221.4; Servlet:SPEC:221.6;
   * Servlet:SPEC:221.7; Servlet:SPEC:221.9; Servlet:SPEC:221.10;
   * Servlet:JAVADOC:825;
   *
   * @test_Strategy: Create a servlet Servlet2; Define Servlet2 using
   * annotation @WebServlet(value=""); Add Servlet2 to ServletContext
   * programmatically under different name; Invoke Servlet2 at any of the URLs
   * specified by @WebServlet; Verify that it is invoked properly; Invoke
   * Servlet2 at the URLs specified in program; Veriy Servlet2 is invoked
   * properly; Verify that servlet name is set properly;
   */
  public void test2() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/Servlet2URL1 HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Servlet2_INVOKED"
        + "|servletname=com.sun.ts.tests.servlet.spec.annotationservlet.webservlet.Servlet2"
        + "|isAsyncSupported=false");
    invoke();

    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/Servlet2URL2 HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Servlet2_INVOKED"
        + "|servletname=com.sun.ts.tests.servlet.spec.annotationservlet.webservlet.Servlet2"
        + "|isAsyncSupported=false");
    invoke();

    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/test/xyz HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Servlet2_INVOKED"
        + "|servletname=com.sun.ts.tests.servlet.spec.annotationservlet.webservlet.Servlet2"
        + "|isAsyncSupported=false");
    invoke();

    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/x/y/t.html HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Servlet2_INVOKED"
        + "|servletname=com.sun.ts.tests.servlet.spec.annotationservlet.webservlet.Servlet2"
        + "|isAsyncSupported=false");
    invoke();

    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/Servlet2APIURL HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "Servlet2_INVOKED" + "|servletname=Servlet2API|isAsyncSupported=false");
    invoke();

    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/Servlet2APIURL2 HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "Servlet2_INVOKED" + "|servletname=Servlet2API|isAsyncSupported=false");
    invoke();

    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/test2/indext.xml HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "Servlet2_INVOKED" + "|servletname=Servlet2API|isAsyncSupported=false");
    invoke();

    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/ServletAPIURL2/xyz HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Servlet2_INVOKED"
        + "|servletname=Servlet2API" + "|isAsyncSupported=false");
    invoke();
  }

  /*
   * @testName: test3
   *
   * @assertion_ids: Servlet:SPEC:221; Servlet:SPEC:221.1; Servlet:SPEC:221.2;
   * Servlet:SPEC:221.3; Servlet:SPEC:221.4; Servlet:SPEC:221.7;
   * Servlet:SPEC:221.9; Servlet:SPEC:221.10; Servlet:JAVADOC:819;
   * Servlet:JAVADOC:822; Servlet:JAVADOC:825;
   *
   * @test_Strategy: Create a servlet Servlet3; Define Servlet3 using annotation
   * 
   * @WebServlet(value="", initParams={}, name="") Add Servlet3 to
   * ServletContext programmatically under different name; Invoke Servlet3 at
   * the URL specified by @WebServlet; Verify that it is properly invoked;
   * Invoke Servlet3 at the URL specified by program; Veriy Servlet3 is invoked
   * and -- value is set correctly -- all @initParams are passed correctly. --
   * servlet name is set correctly
   */
  public void test3() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/Servlet3URL HTTP/1.1");
    TEST_PROPS.setProperty(UNORDERED_SEARCH_STRING,
        "Servlet3_INVOKED|initParams:" + "|name1=value1|name2=value2"
            + "|servletname=Servlet3" + "|isAsyncSupported=false");
    invoke();

    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/Servlet3APIURL HTTP/1.1");
    TEST_PROPS.setProperty(UNORDERED_SEARCH_STRING,
        "Servlet3_INVOKED|initParams:" + "|name1=servlet3|name2=servlet3again"
            + "|servletname=Servlet3API" + "|isAsyncSupported=false");
    invoke();
  }
}
