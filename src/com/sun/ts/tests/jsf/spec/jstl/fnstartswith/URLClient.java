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
package com.sun.ts.tests.jsf.spec.jstl.fnstartswith;

import java.io.PrintWriter;
import java.util.Formatter;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;

public class URLClient extends BaseHtmlUnitClient {

  private static final String SERVLET_MAPPING = "/faces";

  private static final String CONTEXT_ROOT = "/jsf_jstl_fnstartswith_web"
      + SERVLET_MAPPING;

  private static final String TAG_NAME = "span";

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
   * @testName: jstlFunctionStartsWithTagTest
   * @assertion_ids: PENDING
   * @test_Strategy: Validate "fn:contains" JSTL function tag.
   * 
   *                 Test to make sure the following:
   * 
   *                 case1: 'gammaray', 'gammaray' = true case2: 'gammaray',
   *                 'GammaRay' = false case3: null, 'a beta' = false case4:
   *                 'alpha beta', null = true case5: 'gamma ray', 'gamma',=
   *                 true case6: 'gamma ray', 'Gamma r' = false case7: 'gamma
   *                 ray', '' = true case8: '', 'gamma ray' = false
   * 
   * @since 2.0
   */
  public void jstlFunctionStartsWithTagTest() throws Fault {
    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    HtmlPage page = getPage(CONTEXT_ROOT + "/fnendswith.xhtml");

    // -------------------------------------------------------------- case 1
    HtmlSpan testOne = (HtmlSpan) getElementOfTypeIncludingId(page, TAG_NAME,
        "case1");

    if (!validateExistence("case1", TAG_NAME, testOne, formatter)) {
      handleTestStatus(messages);
      return;
    }

    validateElementValue(testOne, "true", formatter);

    // -------------------------------------------------------------- case 2
    HtmlSpan testTwo = (HtmlSpan) getElementOfTypeIncludingId(page, TAG_NAME,
        "case2");

    if (!validateExistence("case2", TAG_NAME, testTwo, formatter)) {
      handleTestStatus(messages);
      return;
    }

    validateElementValue(testTwo, "false", formatter);

    // -------------------------------------------------------------- case 3
    HtmlSpan testThree = (HtmlSpan) getElementOfTypeIncludingId(page, TAG_NAME,
        "case3");

    if (!validateExistence("case3", TAG_NAME, testThree, formatter)) {
      handleTestStatus(messages);
      return;
    }

    validateElementValue(testThree, "false", formatter);

    // -------------------------------------------------------------- case 4
    HtmlSpan testFour = (HtmlSpan) getElementOfTypeIncludingId(page, TAG_NAME,
        "case4");

    if (!validateExistence("case4", TAG_NAME, testFour, formatter)) {
      handleTestStatus(messages);
      return;
    }

    validateElementValue(testFour, "true", formatter);

    // -------------------------------------------------------------- case 5
    HtmlSpan testFive = (HtmlSpan) getElementOfTypeIncludingId(page, TAG_NAME,
        "case5");

    if (!validateExistence("case5", TAG_NAME, testFive, formatter)) {
      handleTestStatus(messages);
      return;
    }

    validateElementValue(testFive, "true", formatter);

    // -------------------------------------------------------------- case 6
    HtmlSpan testSix = (HtmlSpan) getElementOfTypeIncludingId(page, TAG_NAME,
        "case6");

    if (!validateExistence("case6", TAG_NAME, testSix, formatter)) {
      handleTestStatus(messages);
      return;
    }

    validateElementValue(testSix, "false", formatter);

    // -------------------------------------------------------------- case 7
    HtmlSpan testSeven = (HtmlSpan) getElementOfTypeIncludingId(page, TAG_NAME,
        "case7");

    if (!validateExistence("case7", TAG_NAME, testSeven, formatter)) {
      handleTestStatus(messages);
      return;
    }

    validateElementValue(testSeven, "true", formatter);

    // -------------------------------------------------------------- case 8
    HtmlSpan testEight = (HtmlSpan) getElementOfTypeIncludingId(page, TAG_NAME,
        "case8");

    if (!validateExistence("case8", TAG_NAME, testEight, formatter)) {
      handleTestStatus(messages);
      return;
    }

    validateElementValue(testEight, "false", formatter);

    handleTestStatus(messages);

  } // END jstlFunctionStartsWithTagTest //
}
