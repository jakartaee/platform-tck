/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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
 * $URL$ $LastChangedDate$
 */

package com.sun.ts.tests.servlet.api.jakarta_servlet.scattributeevent;

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
    setContextRoot("/servlet_js_scattributeevent_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: constructorTest
   * 
   * @assertion_ids: Servlet:JAVADOC:112
   * 
   * @test_Strategy: Servlet instanciate the constructor
   */

  public void constructorTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "constructorTest");
    invoke();
  }

  /*
   * @testName: addedTest
   * 
   * @assertion_ids: Servlet:JAVADOC:113;Servlet:JAVADOC:114;Servlet:JAVADOC:117
   * 
   * @test_Strategy: Servlet adds an attribute. The listener should detect the
   * add and write a message out to a static log. Servlet then reads the log and
   * verifys the result. It also verifies the request and context that changed
   *
   */

  public void addedTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "addedTest");
    invoke();
  }

  /*
   * @testName: removedTest
   * 
   * @assertion_ids: Servlet:JAVADOC:113;Servlet:JAVADOC:115;Servlet:JAVADOC:117
   * 
   * @test_Strategy: Servlet adds/removes an attribute. The listener should
   * detect the add and write a message out to a static log. Servlet then reads
   * the log and verifys the result. It also verifies the request and context
   * that changed
   */

  public void removedTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "removedTest");
    invoke();
  }

  /*
   * @testName: replacedTest
   * 
   * @assertion_ids: Servlet:JAVADOC:113;Servlet:JAVADOC:116;Servlet:JAVADOC:117
   * 
   * @test_Strategy: Servlet adds/replaces an attribute. The listener should
   * detect the add and write a message out to a static log. Servlet then reads
   * the log and verifys the result. It also verifies the request and context
   * that changed
   */

  public void replacedTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "replacedTest");
    invoke();
  }
}
