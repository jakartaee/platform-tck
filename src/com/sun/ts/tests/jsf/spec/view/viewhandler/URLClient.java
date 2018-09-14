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

package com.sun.ts.tests.jsf.spec.view.viewhandler;

import com.gargoylesoftware.htmlunit.html.HtmlInput;
import java.io.PrintWriter;
import java.util.Formatter;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import java.io.IOException;

public class URLClient extends BaseHtmlUnitClient {

  private static final String CONTEXT_ROOT = "/jsf_view_viewhandler_web";

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
   * @testName: viewHandlerCreateViewTest
   * 
   * @assertion_ids: JSF:SPEC:97; JSF:SPEC:97.1; JSF:SPEC:97.2; JSF:SPEC:97.3
   * 
   * @test_Strategy: Verify on an initial request that the ViewId has not been
   *                 changed. By Setting the suffix as ".jsf" and then looking
   *                 for the correct suffix in the ViewHandler.createView()
   *                 method. Verify that the default suffix is ".jsp". By
   *                 calling the Wrapped.createView() this must support ".jsp"
   *                 as the default suffix.
   * 
   * 
   * @since 1.2
   */
  public void viewHandlerCreateViewTest() throws Fault {
    StringBuilder messages = new StringBuilder(64);

    try {
      getPage(CONTEXT_ROOT + "/greetings.jsf");

    } catch (Exception e) {
      messages.append(JSFTestUtil.FAIL + "with unexpected return value!");
      handleTestStatus(messages);
      return;
    }

  } // END viewHandlerCreateViewTest

  /**
   * @testName: viewHandlerRestoreViewTest
   * 
   * @assertion_ids: JSF:SPEC:1; JSF:SPEC:102; JSF:SPEC:1.2.2
   * 
   * @test_Strategy: Verify on an initial request that the ViewId has not been
   *                 changed. By Setting the suffix as ".jsf" and then looking
   *                 for the correct suffix in the ViewHandler.restoreView()
   *                 method. Verify that the default suffix is ".jsp". By
   *                 calling the Wrapped.restoreView() this must support ".jsp"
   *                 as the default suffix.
   * 
   * @since 1.2
   */
  public void viewHandlerRestoreViewTest() throws Fault {
    StringBuilder messages = new StringBuilder(64);
    Formatter formatter = new Formatter(messages);

    HtmlPage page = getPage(CONTEXT_ROOT + "/greetings.jsf");
    HtmlInput button = (HtmlInput) page.getHtmlElementById("helloForm:userNo");

    if (!validateExistence(button.getId(), "helloForm:userNo", button,
        formatter)) {
      handleTestStatus(messages);
      formatter.close();
      return;
    }

    try {
      button.click();

    } catch (IOException e) {
      formatter.format(
          "Unexpected exception when clicking the Submit" + "button: %s %n", e);
      handleTestStatus(messages);
      formatter.close();
      return;
    }

    // display test result
    handleTestStatus(messages);
    formatter.close();

  } // END viewHandlerRestoreViewTest

} // END URLClient
