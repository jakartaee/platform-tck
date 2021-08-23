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
package com.sun.ts.tests.jsf.spec.flows.basicimplicit;

import java.io.PrintWriter;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;

public class URLClient extends BaseHtmlUnitClient {

  private static final String CONTEXT_ROOT = "/jsf_flows_basicimplicit_web";

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
   * @testName: facesFlowImplicitTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate the following when using Implicit Navigation:
   *                 Entering a Flow Navigating a Flow Flow configuration
   *                 *_flow.xml file. Flow configuration via *.class file
   *                 Exiting a Flow
   * 
   * @since 2.2
   */
  public void facesFlowImplicitTest() throws Fault {
    StringBuilder messages = new StringBuilder(128);

    HtmlPage page = getPage(CONTEXT_ROOT + "/faces/index.xhtml");

    doesPageContain(page, "Page with link to flow entry", messages);

    page = doClick((HtmlSubmitInput) page.getElementById("start"));

    doesPageContain(page, "First page in the flow", messages);
    doesPageContain(page, "basicFlow", messages);

    page = getPage(CONTEXT_ROOT + "/faces/index.xhtml");

    doesPageContain(page, "Page with link to flow entry", messages);

    page = doClick((HtmlSubmitInput) page.getElementById("start"));

    doesPageContain(page, "First page in the flow", messages);
    doesPageContain(page, "basicFlow", messages);

  } // END facesFlowImplicitTest

} // END URLClient
