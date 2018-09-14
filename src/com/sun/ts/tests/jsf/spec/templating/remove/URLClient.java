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
package com.sun.ts.tests.jsf.spec.templating.remove;

import com.gargoylesoftware.htmlunit.html.HtmlLabel;
import java.io.PrintWriter;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import java.util.Formatter;

public class URLClient extends BaseHtmlUnitClient {

  private static final String CONTEXT_ROOT = "/jsf_templating_remove_web";

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
   * @testName: templateUIRemoveTagTest
   * @assertion_ids: PENDING
   * @test_Strategy: Validate that any child of the ui:remove tag is not
   *                 rendered.
   * 
   * @since 2.0
   */
  public void templateUIRemoveTagTest() throws Fault {
    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    // Test Defaults.
    HtmlPage pageOne = getPage(CONTEXT_ROOT + "/faces/removeOne.xhtml");

    HtmlLabel seen = (HtmlLabel) super.getElementOfTypeIncludingId(pageOne,
        "label", "Rendered");

    if (super.validateExistence("Rendered", "label", seen, formatter)) {
      handleTestStatus(messages);
      formatter.close();
      return;
    }

    HtmlLabel notSeen = (HtmlLabel) super.getElementOfTypeIncludingId(pageOne,
        "label", "Not-Rendered");

    if (!(notSeen == null)) {
      formatter.format(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unexpected HtmlLabel Rendered! %n"
          + "Expected not label to be rendered! %n" + "Instead found: %s %n",
          notSeen.asText());
    }

    handleTestStatus(messages);
    formatter.close();

  } // END templateInsertUICompositeTest
} // END URLClient
