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
package com.sun.ts.tests.jsf.spec.flows.basicflowcall;

import java.io.PrintWriter;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;

public class URLClient extends BaseHtmlUnitClient {

  private static final String CONTEXT_ROOT = "/jsf_flows_basicflowcall_web";

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
   * @testName: facesFlowCallTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate: Entering a Flow Navigating a Flow Flow
   *                 configuration *_flow.xml file. Flow configuration via
   *                 *.class file Exiting a Flow
   * 
   * @since 2.2
   */
  public void facesFlowCallTest() throws Fault {
    StringBuilder messages = new StringBuilder(128);

    // Outside the Flow structure. (/index.xhtml)
    HtmlPage page = getPage(CONTEXT_ROOT + "/faces/index.xhtml");
    doesPageContain(page, "Outside of flow", messages);

    // First page of Flow. (flow-a/flow-a.xhtml)
    page = doClick((HtmlSubmitInput) page.getElementById("start_a"));

    doesPageContain(page, "First page in the flow", messages);
    doesPageContain(page, "Flow_a_Bean", messages);
    doesPageMatch(page, "(?s).*Has a flow:\\s+true\\..*", messages, true);
    checkSpanValue(page, "param1FromFlowB", "", messages);
    checkSpanValue(page, "param2FromFlowB", "", messages);

    // Enter the second page of the Flow. (flow-a/next-a.xhtml)
    page = doClick((HtmlSubmitInput) page.getElementById("next_a"));

    doesPageContain(page, "Second page in the flow", messages);

    HtmlTextInput input = (HtmlTextInput) page.getElementById("input");
    String value = "" + System.currentTimeMillis();
    input.setValueAttribute(value);

    // Enter Last Page of Flow. ((flow-a/next-b.xhtml))
    page = doClick((HtmlSubmitInput) page.getElementById("next"));

    doesPageContain(page, value, messages);

    // Enter flow-b, passing parameters.
    page = doClick((HtmlSubmitInput) page.getElementById("callB"));

    doesPageContain(page, "Flow_b_Bean", messages);
    doesPageNotContain(page, "Flow_a_Bean", messages);
    checkSpanValue(page, "param1FromFlowA", "param1Value", messages);
    checkSpanValue(page, "param2FromFlowA", "param2Value", messages);

    // Enter second page of Flow-b
    page = doClick((HtmlSubmitInput) page.getElementById("next_a"));

    doesPageContain(page, "Second page in the flow", messages);

    input = (HtmlTextInput) page.getElementById("input");
    value = "" + System.currentTimeMillis();
    input.setValueAttribute(value);

    // Enter last page of Flow-b
    page = doClick((HtmlSubmitInput) page.getElementById("next"));

    doesPageContain(page, value, messages);

    // Enter flow-a, passing parameters.
    page = doClick((HtmlSubmitInput) page.getElementById("callA"));

    checkSpanValue(page, "param1FromFlowB", "param1Value", messages);
    checkSpanValue(page, "param2FromFlowB", "param2Value", messages);

    // Enter second page of Flow-b
    page = doClick((HtmlSubmitInput) page.getElementById("next_a"));

    doesPageContain(page, "Second page in the flow", messages);

    // Enter last page of Flow-a
    page = doClick((HtmlSubmitInput) page.getElementById("next"));

    page = doClick((HtmlSubmitInput) page.getElementById("return"));

    /*
     * PENDING(edburns): when the work to complete the navigation rule stack is
     * complete, uncomment this. doesPageMatch(page,
     * "(?s).*flowScope value,\\s+should be " + "empty:\\s+\\..", messages,
     * true); doesPageMatch(page, "(?s).*Has a flow:\\s+false\\..*", messages,
     * true);
     */

    handleTestStatus(messages);

  } // END facesFlowCallTest

} // END URLClient
