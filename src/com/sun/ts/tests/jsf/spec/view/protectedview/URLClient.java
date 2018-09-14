/*
 * Copyright (c) 2012, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsf.spec.view.protectedview;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Formatter;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

public class URLClient extends BaseHtmlUnitClient {

  private static final String CONTEXT_ROOT = "/jsf_view_protectedview_web";

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
   * @testName: viewProtectedViewNonAccessPointTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that a that we can not gain access to a Protected
   *                 View from out side that views web-app.
   * 
   * @since 2.2
   */
  public void viewProtectedViewNonAccessPointTest() throws Fault {
    StringBuilder messages = new StringBuilder(64);
    Formatter formatter = new Formatter(messages);

    throwExceptionOnFailingStatusCode = false;

    HtmlPage page = getPage(new WebClient(),
        CONTEXT_ROOT + "/faces/views/protected.xhtml");

    HtmlAnchor anchor = (HtmlAnchor) getElementOfTypeIncludingId(page, "a",
        "linkOne");

    if (validateExistence("linkOne", "a", anchor, formatter)) {
      formatter.format("We should not be able to gain access to a "
          + "Protected View from outside of the Protected View's " + "webapp!");
      handleTestStatus(messages);
      formatter.close();
      return;
    }

    formatter.close();

  } // END viewProtectedViewNonAccessPointTest

  /**
   * @testName: viewProtectedViewSameWebAppAccessTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that we are able to gain access to a protected
   *                 view from inside the same web-app through a non-protected
   *                 view.
   * 
   * @since 2.2
   */
  public void viewProtectedViewSameWebAppAccessTest() throws Fault {
    StringBuilder messages = new StringBuilder(64);
    Formatter formatter = new Formatter(messages);
    String result;
    String expected = "This is a Protected View!";

    HtmlPage page = getPage(new WebClient(),
        CONTEXT_ROOT + "/faces/views/public.xhtml");

    HtmlAnchor anchor = (HtmlAnchor) getElementOfTypeIncludingId(page, "a",
        "linkOne");

    if (!validateExistence("linkOne", "a", anchor, formatter)) {
      handleTestStatus(messages);
      return;
    }

    try {
      result = anchor.click().getWebResponse().getContentAsString();

      if (!result.contains(expected)) {
        formatter.format("Error occured during clicking of Link!");
        handleTestStatus(messages);
      }

    } catch (IOException e) {
      formatter.format("Error occured during clicking of Link!");
      e.printStackTrace();
      handleTestStatus(messages);
      formatter.close();
    }

    formatter.close();

  } // END viewProtectedViewNonAccessPointTest

} // END URLClient
