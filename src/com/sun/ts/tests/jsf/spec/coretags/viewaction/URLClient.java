/*
 * Copyright (c) 2012, 2018 Oracle and/or its affiliates. All rights reserved.
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
 * $Id: $
 */
package com.sun.ts.tests.jsf.spec.coretags.viewaction;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

public class URLClient extends BaseHtmlUnitClient {

  private static final String CONTEXT_ROOT = "/jsf_coretags_viewaction_web";

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
   * @testName: viewActionTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy:
   * 
   * @since 2.2
   */
  public void viewActionTest() throws Fault {
    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    HtmlPage page = getPage(CONTEXT_ROOT + "/faces/test_facelet.xhtml");
    HtmlInput text1 = (HtmlInput) getInputIncludingId(page, "id");
    HtmlInput button1 = (HtmlInput) getElementOfTypeIncludingId(page, "input",
        "form1:button1");

    try {
      text1.setValueAttribute("Orsen Scott Card");
      page = (HtmlPage) button1.click();

    } catch (IOException e) {
      formatter.format("Unexpected exception clicking button1: %s%n", e);
      handleTestStatus(messages);
      formatter.close();
      return;
    }

    // validate the redirection worked!
    HtmlSpan span = (HtmlSpan) getElementOfTypeIncludingId(page, "span",
        "result");

    if (!validateExistence(span.getId(), "span", span, formatter)) {
      handleTestStatus(messages);
      formatter.close();
      return;
    }

    String message1 = span.asText().trim();
    if (!"Test Passed".equals(message1)) {
      formatter.format(JSFTestUtil.FAIL);
    }

    formatter.close();
    handleTestStatus(messages);

  } // END selectItemsValueTest
} // END URLClient
