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

package com.sun.ts.tests.securityapi.ham.sam.obtainbean;

import java.io.PrintWriter;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.securityapi.common.client.BaseHtmlUnitClient;

public class Client extends BaseHtmlUnitClient {

  private static final long serialVersionUID = -1092294751303245196L;

  protected String pageBase = "/securityapi_ham_sam_obtainbean_web";

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
   * @testName: testSAMObtainBean
   *
   * @assertion_ids: Security:SPEC:2.5-1; Security:SPEC:2.5-2;
   *
   *
   * @test_Strategy: 1. Send request to access servlet 2. Make sure the SAM
   * validateRequest method can obtain reference to the CDI BeanManager
   * programmatically 3. Make sure be able to use that reference to obtain a
   * valid bean
   */
  public void testSAMObtainBean() throws Fault {
    try {
      String pageSec = pageBase + "/servlet";

      WebClient webClient = new WebClient();

      Page page = getPage(webClient, pageSec);

      TestUtil.logMsg(
          "responseContent:" + page.getWebResponse().getContentAsString());

      String responseContent = page.getWebResponse().getContentAsString();
      // Check to make sure the HAM validateRequest can use CDI bean manager to
      // obtain bean
      String searchString = "The CDI services is fully available, ServerAuthModule method can obtain bean through CDI.";
      if (responseContent.indexOf(searchString) == -1) {
        TestUtil.logErr("Not get expect result.  Page received:");
        TestUtil.logErr(responseContent);
        TestUtil.logErr("(Should say: \"" + searchString + "\")");
        throw new Fault("testSAMObtainBean failed.");
      }
      TestUtil.logMsg("SAM can use CDI bean manager to obtain bean");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("testSAMObtainBean failed: ", e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("cleanup");
  }
}
