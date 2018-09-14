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
package com.sun.ts.tests.jsf.spec.render.outputscript;

import java.io.PrintWriter;
import java.util.Formatter;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlScript;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;
import java.util.List;

public class URLClient extends BaseHtmlUnitClient {

  private static final String CONTEXT_ROOT = "/jsf_render_outputscript_web";

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
   * @testName: outputScriptEncodeTest
   * @assertion_ids: PENDING
   * @test_Strategy: Validate the following test cases.
   * 
   *                 case 1: Return the resource and render it when just the
   *                 'name' attribute is specified.
   * 
   *                 case 2: Return the resource and render it when 'name' &
   *                 'library' attributes are specified.
   * 
   * @since 2.0
   */
  public void outputScriptEncodeTest() throws Fault {
    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    HtmlPage page = getPage(CONTEXT_ROOT + "/faces/encodetest_facelet.xhtml");

    // -------------------------------------------------------------- case 1

    String[] expected = { "hello.js", "goodbye.js" };

    List<HtmlScript> scripts = page.getDocumentElement()
        .getHtmlElementsByTagName("script");

    boolean hjs = false, gjs = false;
    for (HtmlScript hs : scripts) {
      if (!hjs) {
        hjs = hs.getSrcAttribute().contains(expected[0]);
      }

      if (!gjs) {
        gjs = hs.getSrcAttribute().contains(expected[1]);
      }
    }

    if (!hjs) {
      formatter.format("Did not find script hello.js resource!");
    }

    if (!gjs) {
      formatter.format("Did not find script goodbye.js resource!");
    }

    handleTestStatus(messages);
  } // END outputScriptEncodeTest
} // END URLClient
