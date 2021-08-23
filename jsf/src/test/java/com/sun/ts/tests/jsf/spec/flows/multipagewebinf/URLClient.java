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
package com.sun.ts.tests.jsf.spec.flows.multipagewebinf;

import java.io.PrintWriter;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;

public class URLClient extends BaseHtmlUnitClient {

  private static final String CONTEXT_ROOT = "/jsf_flows_multipagewebinf_web";

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
   * XXX Revisit tests in this client for CTS 8 and JSF 2.3. See bug ID 21033845
   * for details.
   * 
   * @testName: facesFlowWebInfEntryExitTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: Validate the following when Flow config file is packaged in
   *                 WEB-INF directory. (WEB-INF/'{taskflowname}'/flow-xml.
   * 
   *                 -Entry to Flow, Exit from Flow
   * 
   * @since 2.2
   */
  public void facesFlowWebInfEntryExitTest() throws Fault {
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

  } // END facesFlowWebInfEntryExitTest

  /**
   * @testName: facesFlowWebInfScopeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate the following when Flow config file is packaged in
   *                 WEB-INF directory. (WEB-INF/'{taskflowname}'/flow-xml.
   * 
   *                 -FlowScope
   * 
   * @since 2.2
   */
  public void facesFlowWebInfScopeTest() throws Fault {
    StringBuilder messages = new StringBuilder(128);

    HtmlPage page = getPage(CONTEXT_ROOT + "/faces/index.xhtml");

    doesPageContain(page, "Page with link to flow entry", messages);

    page = doClick((HtmlSubmitInput) page.getElementById("start"));

    doesPageContain(page, "First page in the flow", messages);
    doesPageContain(page, "basicFlow", messages);

    page = doClick((HtmlSubmitInput) page.getElementById("next_a"));

    HtmlTextInput input = (HtmlTextInput) page.getElementById("input");
    final String flowScopeValue = "Value in faces flow scope";
    input.setValueAttribute(flowScopeValue);

    page = doClick((HtmlSubmitInput) page.getElementById("next"));

    doesPageContain(page, flowScopeValue, messages);

    page = doClick((HtmlSubmitInput) page.getElementById("return"));

    doesPageContain(page, "return page", messages);
    doesPageNotContain(page, flowScopeValue, messages);

  } // End facesFlowWebInfScopeTest

} // END URLClient
