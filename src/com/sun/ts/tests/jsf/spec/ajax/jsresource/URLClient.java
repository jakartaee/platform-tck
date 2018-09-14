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
package com.sun.ts.tests.jsf.spec.ajax.jsresource;

import java.io.PrintWriter;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlScript;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;
import java.util.Formatter;

public class URLClient extends BaseHtmlUnitClient {

  private static final String CONTEXT_ROOT = "/jsf_ajax_jsresource_web";

  private static final String RES_NAME = "jsf.js";

  private static final String LIB_NAME = "javax.faces";

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
   * @testName: ajaxPDLResourceTest
   * 
   * @assertion_ids: JSF:SPEC:225; JSF:SPEC:226; JSF:SPEC:227
   * 
   * @test_Strategy: Validate that the jsf.js Resource is available via the
   *                 "Page Declaration Language Approach".
   * 
   * @since 2.0
   */
  public void ajaxPDLResourceTest() throws Fault {

    this.validateScript(getPage(CONTEXT_ROOT + "/faces/pdlApproach.xhtml"));

  }// End ajaxPDLResourceTest

  // ------------------------------------------------------------------------------------------------------------
  // private methods

  private void validateScript(HtmlPage page) throws Fault {
    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);
    String script = "script";

    // Test by Resource name.
    HtmlScript resn = (HtmlScript) getElementOfTypeIncludingSrc(page, script,
        RES_NAME);

    if (resn == null) {
      formatter.format("Unexpected Test Result For %s Tag! %n"
          + "Expected Src Attribute to contain: %s %n", script, RES_NAME);
    }

    // Test by Resource Library name.
    HtmlScript resl = (HtmlScript) getElementOfTypeIncludingSrc(page, script,
        LIB_NAME);

    if (resl == null) {
      formatter.format("Unexpected Test Result For %s Tag! %n"
          + "Expected Src Attribute to contain: %s %n", script, LIB_NAME);
    }

    handleTestStatus(messages);
    formatter.close();
  }
} // END URLClient
