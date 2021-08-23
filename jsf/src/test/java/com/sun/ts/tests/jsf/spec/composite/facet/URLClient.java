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
package com.sun.ts.tests.jsf.spec.composite.facet;

import java.io.PrintWriter;
import java.util.Formatter;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;

public class URLClient extends BaseHtmlUnitClient {

  private static final String SERVLET_MAPPING = "/faces";

  private static final String CONTEXT_ROOT = "/jsf_composite_facet_web"
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
   * @testName: compositeFacetsTagTest
   * @assertion_ids: PENDING
   * @test_Strategy: Test the following:
   * 
   *                 case 1 - Validate that if the following attributes are
   *                 legal and the correct output is rendered.
   * 
   *                 composite:facet: name displayName required preferred expert
   *                 shortDescription
   * 
   *                 composite:insertFacet name
   * 
   * @since 2.0
   */
  public void compositeFacetsTagTest() throws Fault {
    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    // ----- case 1
    HtmlPage pageOne = getPage(CONTEXT_ROOT + "/facetsOne.xhtml");

    HtmlSpan span = (HtmlSpan) getElementOfTypeIncludingId(pageOne, SPAN,
        "result");

    if (!validateExistence("result", SPAN, span, formatter)) {
      handleTestStatus(messages);
      return;
    }

    validateElementValue(span, EXPECTED, formatter);

    handleTestStatus(messages);

  } // END compositeFacetsTagTest
}
