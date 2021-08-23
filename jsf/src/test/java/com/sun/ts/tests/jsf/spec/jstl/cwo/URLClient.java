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
 * $Id: URLClient.java 56144 2008-11-03 20:26:49Z dougd $
 */
package com.sun.ts.tests.jsf.spec.jstl.cwo;

import java.io.PrintWriter;
import java.util.Formatter;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;

public class URLClient extends BaseHtmlUnitClient {

  private static final String CONTEXT_ROOT = "/jsf_jstl_cwotags_web";

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
   * @testName: jstlCoreCWOTagTest
   * @assertion_ids: PENDING
   * @test_Strategy: Validate "c:choose, c:when, c:otherwise" JSTL core tags.
   * 
   *                 case 1: Simple validation that when's body content is
   *                 processed if the test evaluates to true. case 2: Validate
   *                 that the first when that evaluates to true processes its
   *                 body content. case 3: Validate otherwise is processed if no
   *                 when case evaluates to true
   * 
   * @since 2.0
   */
  public void jstlCoreCWOTagTest() throws Fault {
    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    HtmlPage page = getPage(CONTEXT_ROOT + "/faces/cwo_facelet.xhtml");

    // -------------------------------------------------------------- case 1
    HtmlSpan testOne = (HtmlSpan) getElementOfTypeIncludingId(page, "span",
        "test1");

    if (!validateExistence("test1", "span", testOne, formatter)) {
      handleTestStatus(messages);
      return;
    }

    validateElementValue(testOne, "PASSED", formatter);

    // -------------------------------------------------------------- case 2
    HtmlSpan testTwo = (HtmlSpan) getElementOfTypeIncludingId(page, "span",
        "test2");

    if (!validateExistence("test2", "span", testTwo, formatter)) {
      handleTestStatus(messages);
      return;
    }

    validateElementValue(testTwo, "PASSED", formatter);

    // -------------------------------------------------------------- case 3
    HtmlSpan testThree = (HtmlSpan) getElementOfTypeIncludingId(page, "span",
        "test3");

    if (!validateExistence("test3", "span", testThree, formatter)) {
      handleTestStatus(messages);
      return;
    }

    validateElementValue(testThree, "PASSED", formatter);

    handleTestStatus(messages);

  } // END jstlCoreCWOTagTest //
}
