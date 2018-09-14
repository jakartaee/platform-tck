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

package com.sun.ts.tests.securityapi.ham.customform.expression;

import java.io.PrintWriter;

import com.gargoylesoftware.htmlunit.Page;
import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.securityapi.common.client.BaseHtmlUnitClient;

public class Client extends BaseHtmlUnitClient {

  private static final long serialVersionUID = -1092294751303245196L;

  protected String pageBase = "/securityapi_ham_customform_expression_web";

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
    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /*
   * @testName: testLoginToContinueuseForwardToLoginExpression
   *
   * @assertion_ids: Security:JAVADOC:165
   *
   * @test_Strategy: 1. The useForwardToLoginExpression value is ${false} 1.
   * Send request to access servlet 2. Redirect to correct login page
   */
  public void testLoginToContinueuseForwardToLoginExpression() throws Fault {
    try {
      String pageSecForward = pageBase + "/servlet";
      String pageLogin = pageBase + "/login.jsf";

      Page page = getPage(pageSecForward);

      if (!page.getWebResponse().getContentAsString().contains("Login")) {
        TestUtil.logErr("Could not find login page");
        throw new Fault(
            "testLoginToContinueuseForwardToLoginExpression failed.");
      }

      if (!page.getWebResponse().getWebRequest().getUrl().toString()
          .contains(pageLogin)) {
        TestUtil
            .logErr(page.getWebResponse().getWebRequest().getUrl().toString());
        TestUtil.logErr("Not redirect to the login uri.");
        throw new Fault(
            "testLoginToContinueuseForwardToLoginExpression failed.");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("testLoginToContinueuseForwardToLoginExpression failed: ",
          e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("cleanup");
  }
}
