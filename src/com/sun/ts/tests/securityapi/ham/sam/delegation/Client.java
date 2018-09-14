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

package com.sun.ts.tests.securityapi.ham.sam.delegation;

import java.io.PrintWriter;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.securityapi.common.client.BaseHtmlUnitClient;

public class Client extends BaseHtmlUnitClient {

  private static final long serialVersionUID = -1092294751303245196L;

  protected String pageBase = "/securityapi_ham_sam_delegation_web";

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
   * @testName: testSAMDelegatesHAM
   *
   * @assertion_ids: Security:SPEC:2.3-5; Security:SPEC:2.3-6;
   * Security:SPEC:2.3-7;
   *
   *
   * @test_Strategy: 1. Send request to access servlet 2. Make sure the HAM
   * validateRequest method is called by a class implemetned ServerAuthModule
   * interface. That can prove: (1) The container supplied a "bridge"
   * ServerAuthModule that integrates HttpAuthenticationMechanism with JSAPIC
   * (2) The bridge module delegate to the HttpAuthenticationMechanism when the
   * bridge module¡¯s methods are invoked (3) The bridge module convert back
   * and forth.
   */
  public void testSAMDelegatesHAM() throws Fault {
    try {

      String pageSec = pageBase + "/Servlet?name=tom&password=secret1";

      WebClient webClient = new WebClient();

      Page page = getPage(webClient, pageSec);

      TestUtil.logMsg(
          "responseContent:" + page.getWebResponse().getContentAsString());

      String responseContent = page.getWebResponse().getContentAsString();
      // Check to make sure we are authenticated by checking the page
      // content. The servlet should output "The user principal is: tom"
      String searchString = "The caller class has ServerAuthModule interface";
      if (responseContent.indexOf(searchString) == -1) {
        TestUtil
            .logErr("Not found correct caller information.  Page received:");
        TestUtil.logErr(responseContent);
        TestUtil.logErr("(Should say: \"" + searchString + "\")");
        throw new Fault("testSAMDelegatesHAM failed.");
      }
      TestUtil.logMsg("The caller class has ServerAuthModule interface");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("testSAMDelegatesHAM failed: ", e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("cleanup");
  }
}
