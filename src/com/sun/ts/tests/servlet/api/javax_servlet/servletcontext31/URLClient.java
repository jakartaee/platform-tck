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

/*
 * $Id:$
 */
package com.sun.ts.tests.servlet.api.javax_servlet.servletcontext31;

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

    setContextRoot("/servlet_js_servletcontext31_web");
    setServletName("TestServlet");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   * logical.hostname.servlet;
   */
  /* Run test */
  /*
   * @testName: getVirtualServerNameTest
   *
   * @assertion_ids: Servlet:JAVADOC:954;
   *
   * @test_Strategy: 1. Create a Servlet Servlet, define it in web.xml; 2.
   * verify ServletContext.getVirtualServerName() returns correctly;
   */
  public void getVirtualServerNameTest() throws Fault {
    String expected_virtualservername = _props
        .getProperty("logical.hostname.servlet").trim();
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/" + getServletName()
            + "?testname=getVirtualServerNameTest&VirtualServerNamePlease="
            + expected_virtualservername + " HTTP/1.0");
    invoke();
  }
}
