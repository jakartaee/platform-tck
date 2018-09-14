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
package com.sun.ts.tests.jsf.spec.composite.insertchildren;

import java.io.PrintWriter;
import java.util.Formatter;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlTableDataCell;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;

public class URLClient extends BaseHtmlUnitClient {

  private static final String SERVLET_MAPPING = "/faces";

  private static final String CONTEXT_ROOT = "/jsf_composite_insertchildren_web"
      + SERVLET_MAPPING;

  public static void main(String[] args) {
    URLClient theTests = new URLClient();
    Status s = theTests.run(args, new PrintWriter(System.out, true),
        new PrintWriter(System.err, true));
    s.exit();
  }

  @Override
  public Status run(String[] args, PrintWriter out, PrintWriter err) {
    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */
  /**
   * @testName: compositeRenderUsingPageChildrenTagTest
   * @assertion_ids: PENDING
   * @test_Strategy: Test the following:
   * 
   *                 case 1 - Validate that Any child components or template
   *                 text within the composite component tag in the using page
   *                 is rendered into the output at the point indicated by this
   *                 tag's placement within the <composite:implementation>
   *                 section.
   * 
   * @since 2.0
   */
  public void compositeRenderUsingPageChildrenTagTest() throws Fault {
    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    // ----- case 1
    HtmlPage pageOne = getPage(CONTEXT_ROOT + "/testOne.xhtml");

    HtmlSpan span = (HtmlSpan) getElementOfTypeIncludingId(pageOne, "span",
        "result");

    if (!validateExistence("result", "span", span, formatter)) {
      handleTestStatus(messages);
      return;
    }

    // Validate that the Span element's value is correct.
    validateElementValue(span, "PASSED", formatter);

    if (null != span.getEnclosingElement("td")) {
      HtmlTableDataCell enclosing = (HtmlTableDataCell) span
          .getEnclosingElement("td");

      if (!validateExistence("tckCell", "td", enclosing, formatter)) {
        handleTestStatus(messages);
        return;
      }

      String result = enclosing.getId();
      String expected = "tckCell";
      if (!expected.equals(result)) {
        formatter.format("Unexpected Test Result For %s! %n" + "Expected: %s %n"
            + "Found: %s %n", HtmlTableDataCell.TAG_NAME, expected, result);
      }
    }

    handleTestStatus(messages);

  } // END compositeRenderUsingPageChildrenTagTest
}
