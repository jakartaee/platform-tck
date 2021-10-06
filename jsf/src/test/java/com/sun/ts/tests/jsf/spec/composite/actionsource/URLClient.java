/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.jsf.spec.composite.actionsource;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Formatter;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;

public class URLClient extends BaseHtmlUnitClient {

  private static final String SERVLET_MAPPING = "/faces";

  private static final String CONTEXT_ROOT = "/jsf_composite_actionsource_web"
      + SERVLET_MAPPING;

  private static final String EXPECTED = "PASSED";

  private static final String SPAN = "span";

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
   * @testName: compositeActionSourceTagTest
   * @assertion_ids: PENDING
   * @test_Strategy: Test the following cases,
   * 
   *                 case 1 - Validate that if both 'target' and 'name'
   *                 attributes are specified that no exceptions are thrown.
   * 
   *                 case 2 - Validate that if the 'name' attribute is only
   *                 specified no exception is thrown.
   * 
   * @since 2.0
   */
  public void compositeActionSourceTagTest() throws Fault {
    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    // ----- case 1
    HtmlPage pageOne = getPage(CONTEXT_ROOT + "/targetandname.xhtml");
    this.testActionSource(pageOne, "button", formatter, messages);

    // ----- case 2
    HtmlPage pageTwo = getPage(CONTEXT_ROOT + "/notargets.xhtml");
    this.testActionSource(pageTwo, "testOne", formatter, messages);

    handleTestStatus(messages);

  } // END compositeActionSourceTagTest

  private void testActionSource(HtmlPage page, String buttonId,
      Formatter formatter, StringBuilder messages) throws Fault {

    HtmlSubmitInput button = (HtmlSubmitInput) getInputIncludingId(page,
        buttonId);

    if (!validateExistence(buttonId, "input", button, formatter)) {
      handleTestStatus(messages);
      return;
    }

    try {
      HtmlPage postBack = (HtmlPage) button.click();
      HtmlSpan span = (HtmlSpan) getElementOfTypeIncludingId(postBack, SPAN,
          "result");

      if (!validateExistence("result", SPAN, span, formatter)) {
        handleTestStatus(messages);
        return;
      }

      validateElementValue(span, EXPECTED, formatter);

    } catch (IOException e) {
      throw new Fault(e);
    }
  }
}
