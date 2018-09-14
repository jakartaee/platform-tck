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

package com.sun.ts.tests.jsf.api.javax_faces.application.applicationWrapperISE;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;
import java.net.URL;

public final class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_appl_applicationWrapperISE_web";

  public static void main(String[] args) {
    URLClient theTests = new URLClient();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  public Status run(String args[], PrintWriter out, PrintWriter err) {
    setContextRoot(CONTEXT_ROOT);
    setServletName(DEFAULT_SERVLET_NAME);
    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /**
   * @testName: applicationWrapperAddELResolverISETest
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:2678
   * @test_Strategy: Ensure an IllegalStateException is thrown if
   *                 addELResolver() is called after the application has been
   *                 initialized.
   * @since 1.2
   */
  public void applicationWrapperAddELResolverISETest() throws Fault {
    // Make Client Call before running the test.
    String clientside = "/faces/applicationtest.jsp";
    this.getPage(new WebClient(),
        "http://" + _hostname + ":" + _port + CONTEXT_ROOT + clientside);

    TEST_PROPS.setProperty(APITEST, "applicationWrapperAddELResolverISETest");
    invoke();
  }

  /**
   * @testName: applicationWrapperSetResourceHandlerISETest
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:2679
   * @test_Strategy: Ensure an IllegalStateException is thrown if
   *                 setResourceHandler() is called after the application has
   *                 received a request.
   * 
   * @since 2.0
   */
  public void applicationWrapperSetResourceHandlerISETest() throws Fault {
    // Make Client Call before running the test.
    String clientside = "/faces/applicationtest.jsp";
    this.getPage(new WebClient(),
        "http://" + _hostname + ":" + _port + CONTEXT_ROOT + clientside);

    TEST_PROPS.setProperty(APITEST,
        "applicationWrapperSetResourceHandlerISETest");
    invoke();
  }

  /**
   * @testName: applicationWrapperSetStateManagerISETest
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:2680
   * @test_Strategy: Ensure an IllegalStateException is thrown if
   *                 setStatemanager() is called after the application has
   *                 received a request.
   * 
   * @since 2.0
   */
  public void applicationWrapperSetStateManagerISETest() throws Fault {
    // Make Client Call before running the test.
    String clientside = "/faces/applicationtest.jsp";
    this.getPage(new WebClient(),
        "http://" + _hostname + ":" + _port + CONTEXT_ROOT + clientside);

    TEST_PROPS.setProperty(APITEST, "applicationWrapperSetStateManagerISETest");
    invoke();
  }

  /**
   * @testName: applicationWrapperSetViewHandlerISETest
   * @assertion_ids: JSF:JAVADOC:190; JSF:JAVADOC:2683
   * @test_Strategy: Ensure an IllegalStateException is thrown if
   *                 setViewHandler() is called after the application has
   *                 received a request.
   * 
   * @since 2.0
   */
  public void applicationWrapperSetViewHandlerISETest() throws Fault {
    // Make Client Call before running the test.
    String clientside = "/faces/applicationtest.jsp";
    this.getPage(new WebClient(),
        "http://" + _hostname + ":" + _port + CONTEXT_ROOT + clientside);

    TEST_PROPS.setProperty(APITEST, "applicationWrapperSetViewHandlerISETest");
    invoke();
  }

  // ----------------------------- Private/Protected Methods
  // ----------------------

  protected HtmlPage getPage(WebClient client, String path) throws Fault {

    try {
      return (HtmlPage) client.getPage(new URL(path));
    } catch (Exception e) {
      throw new Fault(e);
    }
  }

} // end of URLClient
