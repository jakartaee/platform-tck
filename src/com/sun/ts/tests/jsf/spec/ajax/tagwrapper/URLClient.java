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
package com.sun.ts.tests.jsf.spec.ajax.tagwrapper;

import com.gargoylesoftware.htmlunit.html.HtmlInput;
import java.io.IOException;
import java.io.PrintWriter;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;
import java.util.Formatter;

public class URLClient extends BaseHtmlUnitClient {

  private static final String CONTEXT_ROOT = "/jsf_ajax_tagwrapper_web";

  private static final String SPAN = "span";

  private static final String NL = System.getProperty("line.seperator", "\n");

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
   * @testName: ajaxTagWrappingTest
   * @assertion_ids: PENDING
   * @test_Strategy: Unsure that the ajax tag supports being "wrapped" around
   *                 multiple components (enabling Ajax for many components).
   * 
   * @since 2.0
   */
  public void ajaxTagWrappingTest() throws Fault {
    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    HtmlPage page = getPage(CONTEXT_ROOT + "/faces/ajaxTagWrap.xhtml");

    // First we'll check the first page was output correctly
    this.validateSpanTag(page, "out1", "0");
    this.validateSpanTag(page, "checkedvalue", "false");
    this.validateSpanTag(page, "outtext", "");

    // Submit the ajax request
    HtmlInput button1 = (HtmlInput) getElementOfTypeIncludingId(page, "input",
        "button1");

    if (!validateExistence("button1", "input", button1, formatter)) {
      handleTestStatus(messages);
      return;
    }

    try {
      button1.click();
    } catch (IOException ex) {
      formatter.format("Unexpected Execption thrown while clicking '%s'.",
          button1.getId());
      ex.printStackTrace();
    }

    // Check that the ajax request succeeds - eventually.
    this.validateSpanTag(page, "out1", "1");

    // // Check on the text field
    HtmlInput intext = ((HtmlInput) getElementOfTypeIncludingId(page, "input",
        "intext"));

    if (!validateExistence("input", "input", intext, formatter)) {
      handleTestStatus(messages);
      return;
    }
    try {
      intext.focus();
      intext.type("test");
      intext.blur();
    } catch (IOException ex) {
      formatter.format("Unexpected Test failing when setting one or "
          + "more of the following attributes: focus, type, or blur");
      ex.printStackTrace();
    }

    this.validateSpanTag(page, "outtext", "test");

    // Check the checkbox

    HtmlInput checkbox = (HtmlInput) getElementOfTypeIncludingId(page, "input",
        "checkbox");

    if (!validateExistence("checkbox", "input", checkbox, formatter)) {
      handleTestStatus(messages);
      return;
    }

    checkbox.setChecked(true);

    if (!checkbox.isChecked()) {
      formatter.format(
          "Unexpected value for '%s'!" + NL + "Expected: '%s'" + NL
              + "Received: '%s'" + NL,
          checkbox.getId(), "true", checkbox.isChecked());
    }

    handleTestStatus(messages);
  }// End ajaxAllKeywordTest

  // ---------------------------------------------------------- private
  // methods
  /**
   * Test for a the give @String "expectedValue" to match the value of the
   * named @HtmlSpan "element "spanID".
   * 
   * @param page
   *          - @HtmlPage that contains @HtmlSpan element.
   * @param expectedValue
   *          - The expected result.
   * @param formatter
   *          - used to gather test result output.
   */
  private void validateSpanTag(HtmlPage page, String spanId,
      String expectedValue) throws Fault {
    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    HtmlSpan output = (HtmlSpan) getElementOfTypeIncludingId(page, SPAN,
        spanId);

    if (!validateExistence(spanId, SPAN, output, formatter)) {
      handleTestStatus(messages);
      return;
    }
    validateElementValue(output, expectedValue, formatter);

  }// End validateSpanTag
} // END URLClient
