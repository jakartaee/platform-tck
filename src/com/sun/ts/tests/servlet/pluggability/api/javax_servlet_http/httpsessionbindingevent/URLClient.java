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
package com.sun.ts.tests.servlet.pluggability.api.javax_servlet_http.httpsessionbindingevent;

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
    setContextRoot("/servlet_pluh_httpsessionbindingevent_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: addedTest
   * 
   * @assertion_ids: Servlet:JAVADOC:309;Servlet:JAVADOC:307;Servlet:JAVADOC:308
   * 
   * @test_Strategy: Client calls a servlet that adds an attribute. The listener
   * should detect the add and writes the values returned by the getName,
   * getSession(), and getValue() methods to a static log. Servlet then reads
   * the log and verifies the result
   */
  public void addedTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "addedTest");
    invoke();
  }

  /*
   * @testName: removedTest
   * 
   * @assertion_ids: Servlet:JAVADOC:310;Servlet:JAVADOC:307;Servlet:JAVADOC:308
   * 
   * @test_Strategy: Client calls a servlet that adds/removes an attribute. The
   * listener should detect the changes and writes the values returned by the
   * getName, getSession(), and getValue() methods to a static log. Servlet then
   * reads the log and verifies the result
   */
  public void removedTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "removedTest");
    invoke();
  }

  /*
   * @testName: replacedTest
   * 
   * @assertion_ids: Servlet:JAVADOC:311;Servlet:JAVADOC:307;Servlet:JAVADOC:308
   * 
   * @test_Strategy: Client calls a servlet that adds/replaces an attribute. The
   * listener should detect the changes and writes the values returned by the
   * getName, getSession(), and getValue() methods to a static log. Servlet then
   * reads the log and verifies the result
   */
  public void replacedTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "replacedTest");
    invoke();
  }

  /*
   * @testName: constructor_StringTest
   * 
   * @assertion_ids: Servlet:JAVADOC:305
   * 
   * @test_Strategy: Servlet creates an object using the 2 argument method.
   */
  public void constructor_StringTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "constructor_StringTest");
    invoke();
  }

  /*
   * @testName: constructor_String_ObjectTest
   * 
   * @assertion_ids: Servlet:JAVADOC:306
   * 
   * @test_Strategy: Servlet creates an object using the 3 argument method.
   */
  public void constructor_String_ObjectTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "constructor_String_ObjectTest");
    invoke();
  }
}
