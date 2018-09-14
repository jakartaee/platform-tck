/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.securityapi.ham.workflow.validaterequest;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.securityapi.common.client.BaseHtmlUnitClient;

import java.io.PrintWriter;
import java.util.Properties;

public class Client extends BaseHtmlUnitClient {

  // Shared test variables:
  private Properties props = null;

  // Constants:
  private static final String CLASS_TRACE_HEADER = "[Client]: ";

  private String pageServletBase = "/securityapi_ham_workflow_validaterequest_web";

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
  public Status run(String args[], PrintWriter out, PrintWriter err) {

    Client theTests = new Client();

    return super.run(args, out, err);
  }

  /**
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   *
   */
  // Note:Based on the input argument setup will intialize JSP or servlet pages
  public void setup(String[] args, Properties p) throws Fault {
    super.setup(args, p);

    props = p;
  }

  /*
   * @testName: testCallValidateRequestBeforeService
   *
   * @assertion_ids: Security:SPEC:2.2-1
   *
   * @test_Strategy: Send request with authentication with correct usr/pwd,
   * response from HAM.validate() and service() is in correct sequence
   *
   */
  public void testCallValidateRequestBeforeService() throws Fault {

    String pageSec = pageServletBase
        + "/ServletForValidateRequest?name=tom&password=secret1";

    WebClient webClient = new WebClient();

    Page page = getPage(webClient, pageSec);

    logMessage("response statusToken:" + page.getWebResponse().getStatusCode());
    logMessage("responseContent:" + page.getWebResponse().getContentAsString());

    String responseContent = page.getWebResponse().getContentAsString();

    String searchString = "This is in HAM validateRequest method."
        + "Inside Servlet doGet." + "principal name=tom";
    if (!responseContent.contains(searchString)) {
      TestUtil.logErr("Should get the messages in correct sequence");
      throw new Fault("testCallValidateRequestBeforeService failed.");
    }
  }

  /**
   * Simple wrapper around TestUtil.logMessage().
   * 
   * @param message
   *          - the message to log
   */
  private static void logMessage(String message) {
    TestUtil.logMsg(CLASS_TRACE_HEADER + message);
  }

}
