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
package com.sun.ts.tests.jsf.spec.ajax.keyword;

import java.io.IOException;
import java.io.PrintWriter;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

public class URLClient extends BaseHtmlUnitClient {

  private static final String CONTEXT_ROOT = "/jsf_ajax_keyword_web";

  private static final String EXPECTED = "testtext";

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
   * @testName: ajaxAllKeywordTest
   * @assertion_ids: PENDING
   * @test_Strategy: Unsure the keyword 'all' works correctly with the f:ajax
   *                 tag as value to 'execute' and 'render' attributes.
   * 
   * @since 2.0
   */
  public void ajaxAllKeywordTest() throws Fault {

    List<HtmlPage> pages = new ArrayList<HtmlPage>();
    pages.add(getPage(CONTEXT_ROOT + "/faces/ajaxAllKeyword1.xhtml"));
    pages.add(getPage(CONTEXT_ROOT + "/faces/ajaxAllKeyword2.xhtml"));
    pages.add(getPage(CONTEXT_ROOT + "/faces/ajaxAllKeyword3.xhtml"));

    String buttonId = "allKeyword";
    String spanId = "out";

    this.validateKeyword(pages, buttonId, spanId, EXPECTED);

  }// End ajaxAllKeywordTest

  /**
   * @testName: ajaxThisKeywordTest
   * @assertion_ids: PENDING
   * @test_Strategy: Unsure the keyword 'this' works correctly with the f:ajax
   *                 tag as value to 'execute' and 'render' attributes.
   * 
   * @since 2.0
   */
  public void ajaxThisKeywordTest() throws Fault {
    List<HtmlPage> pages = new ArrayList<HtmlPage>();
    pages.add(getPage(CONTEXT_ROOT + "/faces/ajaxThisKeyword1.xhtml"));
    pages.add(getPage(CONTEXT_ROOT + "/faces/ajaxThisKeyword2.xhtml"));
    pages.add(getPage(CONTEXT_ROOT + "/faces/ajaxThisKeyword3.xhtml"));

    String buttonId = "thisKeyword";
    String spanId = "out";

    this.validateKeyword(pages, buttonId, spanId, EXPECTED);
  } // End ajaxThisKeywordTest

  /**
   * @testName: ajaxFormKeywordTest
   * @assertion_ids: PENDING
   * @test_Strategy: Unsure the keyword 'form' works correctly with the f:ajax
   *                 tag as value to 'execute' and 'render' attributes.
   * 
   * @since 2.0
   */
  public void ajaxFormKeywordTest() throws Fault {
    List<HtmlPage> pages = new ArrayList<HtmlPage>();
    pages.add(getPage(CONTEXT_ROOT + "/faces/ajaxFormKeyword1.xhtml"));
    pages.add(getPage(CONTEXT_ROOT + "/faces/ajaxFormKeyword2.xhtml"));
    pages.add(getPage(CONTEXT_ROOT + "/faces/ajaxFormKeyword3.xhtml"));

    String buttonId = "formKeyword";
    String spanId = "out";

    this.validateKeyword(pages, buttonId, spanId, EXPECTED);
  } // End ajaxThisKeywordTest

  /**
   * @testName: ajaxNoneKeywordTest
   * @assertion_ids: PENDING
   * @test_Strategy: Unsure the keyword 'none' works correctly with the f:ajax
   *                 tag as value to 'execute' and 'render' attributes.
   * 
   * @since 2.0
   */
  public void ajaxNoneKeywordTest() throws Fault {
    List<HtmlPage> pages = new ArrayList<HtmlPage>();
    pages.add(getPage(CONTEXT_ROOT + "/faces/ajaxNoneKeyword1.xhtml"));
    pages.add(getPage(CONTEXT_ROOT + "/faces/ajaxNoneKeyword2.xhtml"));
    pages.add(getPage(CONTEXT_ROOT + "/faces/ajaxNoneKeyword3.xhtml"));

    String buttonId = "noneKeyword";
    String spanId = "out";

    this.validateKeyword(pages, buttonId, spanId, EXPECTED);
  } // End ajaxThisKeywordTest

  // ---------------------------------------------------------- private
  // methods
  private void validateKeyword(List<HtmlPage> pages, String buttonId,
      String spanId, String expectedValue) throws Fault {
    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    String span = "span";
    for (HtmlPage page : pages) {
      HtmlSpan output = (HtmlSpan) getElementOfTypeIncludingId(page, span,
          spanId);

      if (!validateExistence(spanId, span, output, formatter)) {
        handleTestStatus(messages);
        return;
      }

      // First we'll check the first page was output correctly
      validateElementValue(output, expectedValue, formatter);

      // Submit the ajax request
      HtmlSubmitInput button = (HtmlSubmitInput) getElementOfTypeIncludingId(
          page, "input", buttonId);
      try {
        button.click();
      } catch (IOException ex) {
        formatter.format("Unexpected Execption thrown while clicking '%s'.",
            button.getId());
        ex.printStackTrace();
      }

      // Check that the ajax request succeeds - if the page is rewritten,
      // this will be the same
      validateElementValue(output, expectedValue, formatter);

      handleTestStatus(messages);
    }
  }
} // END URLClient
