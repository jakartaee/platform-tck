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
package com.sun.ts.tests.jsf.spec.composite.valueholder;

import com.gargoylesoftware.htmlunit.html.HtmlInput;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Formatter;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;

public class URLClient extends BaseHtmlUnitClient {

  private static final String SERVLET_MAPPING = "/faces";

  private static final String CONTEXT_ROOT = "/jsf_composite_valueholder_web"
      + SERVLET_MAPPING;

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
   * @testName: compositeValueHolderTest
   * @assertion_ids: PENDING
   * @test_Strategy: Test the following cases,
   * 
   *                 case 1 - Validate that if the 'name' attribute is only
   *                 specified no exception is thrown.
   * 
   *                 case 2 - Validate that if both 'target' and 'name'
   *                 attributes are specified that no exceptions are thrown.
   * 
   * 
   * @since 2.0
   */
  public void compositeValueHolderTest() throws Fault {
    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    /*
     * Test Values in the form of: componentId, testvalue, expected returned
     * value.
     */
    String[][] testValues = { { "firstname", "peter", "Peter" },
        { "lastname", "griffin", "Griffin" } };

    // ---------- Test Case 1
    HtmlPage pageOne = getPage(CONTEXT_ROOT + "/caseOne.xhtml");
    this.testValueHolder(pageOne, testValues, formatter, messages);

    // ---------- Test Case 2
    HtmlPage pageTwo = getPage(CONTEXT_ROOT + "/caseTwo.xhtml");
    this.testValueHolder(pageTwo, testValues, formatter, messages);

    handleTestStatus(messages);

  } // END compositeValueHolderTest

  // ---------------------------------------------------------- private
  // methods
  private void testValueHolder(HtmlPage page, String[][] testvalues,
      Formatter formatter, StringBuilder messages) throws Fault {

    // Constants
    String buttonId = "button";
    String input = "input";

    String compId, value;
    for (int i = 0; i < testvalues.length; i++) {
      compId = testvalues[i][0];
      value = testvalues[i][1];

      HtmlInput empty = (HtmlTextInput) getInputIncludingId(page, compId);

      if (!validateExistence(compId, input, empty, formatter)) {
        handleTestStatus(messages);
        return;
      }

      // Fill In Field.
      empty.setValueAttribute(value);
    }

    // Get the Button Component.
    HtmlInput button = (HtmlSubmitInput) getInputIncludingId(page, buttonId);

    if (!validateExistence(buttonId, input, button, formatter)) {
      handleTestStatus(messages);
      return;
    }

    // Validate that the changes.
    try {
      HtmlPage postBack = (HtmlPage) button.click();
      String expected;
      for (int i = 0; i < testvalues.length; i++) {
        compId = testvalues[i][0];
        expected = testvalues[i][2];

        HtmlInput filled = (HtmlTextInput) getInputIncludingId(postBack,
            compId);

        if (!validateExistence(compId, input, filled, formatter)) {
          handleTestStatus(messages);
          return;
        }

        validateElementValue(filled, expected, formatter);

      }
    } catch (IOException e) {
      throw new Fault(e);
    }
  }
}
