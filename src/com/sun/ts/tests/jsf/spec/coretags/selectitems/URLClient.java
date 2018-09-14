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
package com.sun.ts.tests.jsf.spec.coretags.selectitems;

import com.gargoylesoftware.htmlunit.html.HtmlOption;
import java.io.PrintWriter;
import java.util.Formatter;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;
import java.util.ArrayList;

public class URLClient extends BaseHtmlUnitClient {

  private static final String CONTEXT_ROOT = "/jsf_coretags_selectitems_web";

  private static final String SELECT = "select";

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
   * @testName: selectItemsValueTest
   * @assertion_ids: PENDING
   * @test_Strategy: Validate the 'value' attribute gets handled properly.
   * 
   *                 case 1: - Verify that we get the correct number of Options
   *                 are returned for both a List and Array ojbects. case 2: -
   *                 Verifiy that the itemLabelEscaped does set normal JSF HTML
   *                 escaping or not.
   * 
   * @since 2.0
   */
  public void selectItemsValueTest() throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    List<HtmlPage> pages = new ArrayList<HtmlPage>();
    pages.add(getPage(CONTEXT_ROOT + "/faces/test_facelet.xhtml"));

    // -------------------------------------------------------------- case 1
    for (HtmlPage page : pages) {
      List<String> testCase = new ArrayList<String>();
      testCase.add("caseOne");
      testCase.add("caseTwo");

      for (String tCase : testCase) {

        HtmlSelect checkbox1 = (HtmlSelect) getElementOfTypeIncludingId(page,
            SELECT, tCase);

        if (!validateExistence(tCase, SELECT, checkbox1, formatter)) {
          handleTestStatus(messages);
          return;
        }

        if (checkbox1.getOptionSize() != 3) {
          formatter.format("Expected the number of options "
              + "rendered for the component %s to be '%d', but " + "found '%d'",
              3, tCase, checkbox1.getOptionSize());
        }
      }
    }

    // --------------------------------------------------------------- case
    // 2
    for (HtmlPage page : pages) {
      HtmlSelect checkbox = (HtmlSelect) getElementOfTypeIncludingId(page,
          SELECT, "caseThree");

      if (!validateExistence("caseThree", SELECT, checkbox, formatter)) {
        handleTestStatus(messages);
        return;
      }

      HtmlOption optOne = checkbox.getOptionByValue("Escape Characters");
      String expected = "&gt;&lt;&amp;";
      String result = optOne.asXml();

      if (!result.contains(expected)) {
        formatter.format(
            "Unexpected value for Option's label" + TestUtil.NEW_LINE
                + "Expected: '%s'" + TestUtil.NEW_LINE + "Found: '%s'",
            expected, result);
      }

    }
    handleTestStatus(messages);
  } // END selectItemsValueTest
} // END URLClient
