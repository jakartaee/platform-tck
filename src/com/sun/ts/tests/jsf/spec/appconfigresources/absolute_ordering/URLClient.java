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
package com.sun.ts.tests.jsf.spec.appconfigresources.absolute_ordering;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;
import java.io.PrintWriter;
import java.util.Formatter;

public class URLClient extends BaseHtmlUnitClient {

  private static final String CONTEXT_ROOT = "/jsf_appconfigresources_absolute_ordering_web";;

  private static final String SPAN = "span";

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

  // ------------------------------------------------------------ Test Methods
  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */
  /**
   * @testName: testDocumentAbsoluteOrdering
   * @assertion_ids: PENDING
   * @test_Strategy: Test to make sure that the ordering of artifacts is
   *                 correct, when using the <absolute-ordering> tag inside the
   *                 applicationFacesConfig, in this case all other ordering
   *                 preferences must be ignored.
   * 
   * @since 2.0
   */
  public void testDocumentAbsoluteOrdering() throws Fault {
    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    HtmlPage page = getPage(CONTEXT_ROOT + "/faces/test.xhtml");

    // -------------------------------------------------------------- case 1
    String expected = "Order Correct: true";
    HtmlSpan result = (HtmlSpan) getElementOfTypeIncludingId(page, SPAN,
        "status");

    if (!validateExistence("result", SPAN, result, formatter)) {
      handleTestStatus(messages);
      return;
    }

    super.validateElementValue(result, expected, formatter);

    handleTestStatus(messages);
  }
}
