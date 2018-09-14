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
package com.sun.ts.tests.jsf.spec.flows.basicmethodcall;

import java.io.PrintWriter;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;

public class URLClient extends BaseHtmlUnitClient {

  private static final String CONTEXT_ROOT = "/jsf_flows_basicmethodcall_web";

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
   * @testName: facesFlowBasicMethodCallTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy:
   * 
   * @since 2.2
   */
  public void facesFlowBasicMethodCallTest() throws Fault {

    this.goTest("start_a");
    this.goTest("start_b");

  } // END facesFlowBasicMethodCallTest

  // --------------------------- private methods

  private void goTest(String startId) throws Fault {
    StringBuilder messages = new StringBuilder(128);

    HtmlPage page = getPage(CONTEXT_ROOT + "/faces/index.xhtml");

    doesPageContain(page, "Outside of flow", messages);

    page = doClick((HtmlSubmitInput) page.getElementById(startId));

    page = doClick(
        (HtmlSubmitInput) page.getElementById("outcome-from-method"));

    doesPageContain(page, "Last page in the flow", messages);

    page = getPage(CONTEXT_ROOT + "/faces/index.xhtml");

    page = doClick((HtmlSubmitInput) page.getElementById("start_a"));

    page = doClick(
        (HtmlSubmitInput) page.getElementById("outcome-from-markup"));

    doesPageContain(page, "voidMethod called in flow-a", messages);

  } // END goTest

} // END URLClient
