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
package com.sun.ts.tests.jsf.spec.jstl.fnsplit;

import java.io.PrintWriter;
import java.util.Formatter;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;

public class URLClient extends BaseHtmlUnitClient {

  private static final String SERVLET_MAPPING = "/faces";

  private static final String CONTEXT_ROOT = "/jsf_jstl_fnsplit_web"
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
   * @testName: jstlFunctionsplitTagTest
   * @assertion_ids: PENDING
   * @test_Strategy: Validate "fn:split" JSTL function tag.
   * 
   *                 Test to make sure the following:
   * 
   *                 case1: 'one-two-three' == fn:join(fn:split('one|two|three',
   *                 '|'), '-') - true case2: '' == fn:join(fn:split(null, '|'),
   *                 '-') - true case3: 'one|two|three' ==
   *                 fn:join(fn:split('one|two|three', null), '-') - true case4:
   *                 '' == fn:join(fn:split('', '|'), '-') - true case5:
   *                 'one|two|three' == fn:join(fn:split('one|two|three', ''),
   *                 '-') - true
   * 
   * @since 2.0
   */
  public void jstlFunctionsplitTagTest() throws Fault {
    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    HtmlPage page = getPage(CONTEXT_ROOT + "/fnsplit.xhtml");

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

    validateElementValue(testTwo, "true", formatter);

    // -------------------------------------------------------------- case 3
    HtmlSpan testThree = (HtmlSpan) getElementOfTypeIncludingId(page, TAG_NAME,
        "case3");

    if (!validateExistence("case3", TAG_NAME, testThree, formatter)) {
      handleTestStatus(messages);
      return;
    }

    validateElementValue(testThree, "true", formatter);

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

    handleTestStatus(messages);

  } // END jstlFunctionsplitTagTest //
}
