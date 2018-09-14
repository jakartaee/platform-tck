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

package com.sun.ts.tests.ejb30.misc.datasource.twowars;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.tests.servlet.common.client.AbstractUrlClient;

public class Client extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/ejb3_misc_datasource_twowars_web";

  private static final String SERVLET_NAME = "TestServlet";

  private static final String CONTEXT_ROOT_2 = "/two_standalone_component_web";

  private static final String SERVLET_NAME_2 = "TestServlet2";

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  /**
   * Entry point for same-VM execution. In different-VM execution, the main
   * method delegates to this method.
   */
  @Override
  public Status run(String args[], PrintWriter out, PrintWriter pw) {
    setServletName(SERVLET_NAME);
    setContextRoot(CONTEXT_ROOT);
    return super.run(args, out, pw);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /*
   * @testName: servletPostConstruct
   * 
   * @test_Strategy: access various data sources from servlet.
   */
  public void servletPostConstruct() throws Exception {
    TEST_PROPS.setProperty(APITEST, "servletPostConstruct");
    invoke();
  }

  /*
   * @testName: servletPostConstruct2
   * 
   * @test_Strategy: access various data sources from servlet2.
   */
  public void servletPostConstruct2() throws Exception {
    setServletName(SERVLET_NAME_2);
    setContextRoot(CONTEXT_ROOT_2);
    TEST_PROPS.setProperty(APITEST, "servletPostConstruct2");
    invoke();
  }

  /*
   * @testName: ejbPostConstruct
   * 
   * @test_Strategy: access various data sources from ejb.
   */
  public void ejbPostConstruct() throws Exception {
    TEST_PROPS.setProperty(APITEST, "ejbPostConstruct");
    invoke();
  }
}
