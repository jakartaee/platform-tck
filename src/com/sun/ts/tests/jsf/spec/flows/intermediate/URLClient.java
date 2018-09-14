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
 * $Id$
 */
package com.sun.ts.tests.jsf.spec.flows.intermediate;

import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;

public class URLClient extends BaseHtmlUnitClient {

  private static final String CONTEXT_ROOT = "/jsf_flows_intermediate_web";

  public static void main(String[] args) {
    URLClient theTests = new URLClient();
    Status s = theTests.run(args, new PrintWriter(System.out, true),
        new PrintWriter(System.err, true));
    s.exit();
  }

  public Status run(String[] args, PrintWriter out, PrintWriter err) {
    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /**
   * @testName: facesFlowScopeXMLTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that Flowscope is handled correctly when
   *                 configuration is setup by {flowname}-flow.xml file.
   * 
   * @since 2.2
   */
  public void facesFlowScopeXMLTest() throws Fault {
    goTest("maintain-customer-record");
    goTest("maintain-customer-record");

  } // END facesFlowScopeXMLTest

  /**
   * @testName: facesFlowScopeJAVATest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that Flowscope is handled correctly when
   *                 configuration is setup by {flowname}.java file.
   * @since 2.2
   */
  public void facesFlowScopeJAVATest() throws Fault {
    goTest("maintain-customer-record-java");
    goTest("maintain-customer-record-java");

  } // END facesFlowScopeJAVATest

  // ---------------------------------- private methods

  private void goTest(String button) throws Fault {
    StringBuilder messages = new StringBuilder(128);

    HtmlPage page = getPage(CONTEXT_ROOT + "/faces/index.xhtml");
    doesPageContain(page, "Outside of flow", messages);

    page = doClick((HtmlSubmitInput) page.getElementById(button));

    page = doClick((HtmlSubmitInput) page.getElementById("createCustomer"));

    doesPageContain(page, "View customer page", messages);
    doesPageMatch(page, "(?s).*Customer Id:\\s+([0-9])+.*", messages, true);

    page = doClick((HtmlSubmitInput) page.getElementById("upgrade"));

    doesPageContain(page, "View customer page", messages);
    doesPageMatch(page, "(?s).*Customer is upgraded:\\s+true.*", messages,
        true);

    page = doClick((HtmlSubmitInput) page.getElementById("exit"));
    doesPageContain(page, "return page", messages);
    doesPageContain(page, "Finalizer called", messages);
  }

} // END URLClient
