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
package com.sun.ts.tests.jsf.spec.jstl.iftag;

import java.io.PrintWriter;
import java.util.Formatter;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;

public class URLClient extends BaseHtmlUnitClient {

  private static final String CONTEXT_ROOT = "/jsf_jstl_iftag_web";

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
   * @testName: jstlCoreIfTagTest
   * @assertion_ids: PENDING
   * @test_Strategy: Validate "c:choose, c:when, c:otherwise" JSTL core tags.
   * 
   *                 Test to make sure the following equations process
   *                 correctly.
   * 
   *                 "#{true == true}", "#{false == true}", "#{1 &gt; 0}" "#{1
   *                 &lt; 0}", "#{vTrue}", "#{vFalse}"
   * 
   * @since 2.0
   */
  public void jstlCoreIfTagTest() throws Fault {
    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    HtmlPage page = getPage(CONTEXT_ROOT + "/faces/iftag_facelet.xhtml");

    // -------------------------------------------------------------- case 1
    HtmlSpan testOne = (HtmlSpan) getElementOfTypeIncludingId(page, TAG_NAME,
        "test1");

    if (!validateExistence("test1", TAG_NAME, testOne, formatter)) {
      handleTestStatus(messages);
      return;
    }

    validateElementValue(testOne, "true", formatter);

    // -------------------------------------------------------------- case 2
    HtmlSpan testTwo = (HtmlSpan) getElementOfTypeIncludingId(page, TAG_NAME,
        "test2");

    if (!validateExistence("test2", TAG_NAME, testTwo, formatter)) {
      handleTestStatus(messages);
      return;
    }

    validateElementValue(testTwo, "false", formatter);

    // -------------------------------------------------------------- case 3
    HtmlSpan testThree = (HtmlSpan) getElementOfTypeIncludingId(page, TAG_NAME,
        "test3");

    if (!validateExistence("test3", TAG_NAME, testThree, formatter)) {
      handleTestStatus(messages);
      return;
    }

    validateElementValue(testThree, "true", formatter);

    // -------------------------------------------------------------- case 4
    HtmlSpan testFour = (HtmlSpan) getElementOfTypeIncludingId(page, TAG_NAME,
        "test4");

    if (!validateExistence("test4", TAG_NAME, testFour, formatter)) {
      handleTestStatus(messages);
      return;
    }

    validateElementValue(testFour, "false", formatter);

    // -------------------------------------------------------------- case 5
    HtmlSpan testFive = (HtmlSpan) getElementOfTypeIncludingId(page, TAG_NAME,
        "test5");

    if (!validateExistence("test5", TAG_NAME, testFive, formatter)) {
      handleTestStatus(messages);
      return;
    }

    validateElementValue(testFive, "true", formatter);

    // -------------------------------------------------------------- case 6
    HtmlSpan testSix = (HtmlSpan) getElementOfTypeIncludingId(page, TAG_NAME,
        "test6");

    if (!validateExistence("test6", TAG_NAME, testSix, formatter)) {
      handleTestStatus(messages);
      return;
    }

    validateElementValue(testSix, "false", formatter);

    handleTestStatus(messages);

  } // END jstlCoreIfTagTest //

  /**
   * @testName: jstlCoreURITest
   * @assertion_ids: PENDING
   * @test_Strategy: Validate the following statement from the JSTL Core docs.
   * 
   *                 The pre JSF 2.0 version of Facelets incorrectly declared
   *                 the taglib uri to be http://java.sun.com/jstl/core. For
   *                 backwards compatibility implementations must correctly
   *                 handle inclusions with the incorrect uri, and the correct
   *                 uri, declared here.
   * 
   * @since 2.2
   */
  public void jstlCoreURITest() throws Fault {
    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    HtmlPage uriTest = getPage(CONTEXT_ROOT + "/faces/uri_test_facelet.xhtml");

    // ------------------------------ JSTL 1.0
    HtmlSpan oldWay = (HtmlSpan) getElementOfTypeIncludingId(uriTest, TAG_NAME,
        "old_way");

    if (!validateExistence("old_way", TAG_NAME, oldWay, formatter)) {
      handleTestStatus(messages);
      return;
    }

    validateElementValue(oldWay, "true", formatter);

    // ------------------------------ JSTL 1.1+
    HtmlSpan newWay = (HtmlSpan) getElementOfTypeIncludingId(uriTest, TAG_NAME,
        "new_way");

    if (!validateExistence("new_way", TAG_NAME, newWay, formatter)) {
      handleTestStatus(messages);
      return;
    }

    validateElementValue(newWay, "true", formatter);

    handleTestStatus(messages);

  } // END jstlCoreIfTagTest //
}
