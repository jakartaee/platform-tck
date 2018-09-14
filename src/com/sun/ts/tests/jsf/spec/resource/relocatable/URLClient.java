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
package com.sun.ts.tests.jsf.spec.resource.relocatable;

import java.io.PrintWriter;
import java.util.Formatter;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlScript;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;

public class URLClient extends BaseHtmlUnitClient {

  private static final String NL = System.getProperty("line.seperator", "\n");

  private static final String CONTEXT_ROOT = "/jsf_resource_relocatable_web";

  private static final String SCRIPT = "script";

  private static final String SCRIPT_NAME = "hello.js";

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
   * @testName: relocatableHeadTest
   * @assertion_ids: PENDING
   * @test_Strategy: Validate the a resource that specifies "head" as the target
   *                 is rendered in the "<head>" of the page.
   * 
   * @since 2.0
   */
  public void relocatableHeadTest() throws Fault {

    HtmlPage page = getPage(CONTEXT_ROOT + "/faces/reloc-head.xhtml");
    this.testRelocatableElement(page, "head");

  }

  /**
   * @testName: relocatableBodyTest
   * @assertion_ids: PENDING
   * @test_Strategy: Validate the a resource that specifies "body" as the target
   *                 is rendered in the "<body>" of the page.
   * 
   * @since 2.0
   */
  public void relocatableBodyTest() throws Fault {

    HtmlPage page = getPage(CONTEXT_ROOT + "/faces/reloc-body.xhtml");
    this.testRelocatableElement(page, "body");
  }

  /**
   * @testName: relocatableFormTest
   * @assertion_ids: PENDING
   * @test_Strategy: Validate the a resource that specifies "form" as the target
   *                 is rendered in the "<form>" of the page.
   * 
   * @since 2.0
   */
  public void relocatableFormTest() throws Fault {

    HtmlPage page = getPage(CONTEXT_ROOT + "/faces/reloc-form.xhtml");
    this.testRelocatableElement(page, "form");
  }

  // ---------------------------------------------------------- private
  // methods
  private void testRelocatableElement(HtmlPage page, String expected)
      throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    HtmlScript htmlscript = (HtmlScript) getElementOfTypeIncludingSrc(page,
        SCRIPT, SCRIPT_NAME);

    String result = htmlscript.getParentNode().getNodeName();

    if (!expected.equals(result)) {
      formatter.format(
          "Test FAILED.  Unexpected test result!" + NL
              + "Expected Parent Tag: '%s'" + NL + "Received Parent Tag: '%s",
          expected, result);
    }

    handleTestStatus(messages);
  }
}
