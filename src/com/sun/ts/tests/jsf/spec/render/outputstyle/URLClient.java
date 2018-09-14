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
package com.sun.ts.tests.jsf.spec.render.outputstyle;

import com.gargoylesoftware.htmlunit.html.HtmlLink;
import java.io.PrintWriter;
import java.util.Formatter;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;
import java.util.List;

public class URLClient extends BaseHtmlUnitClient {

  private static final String CONTEXT_ROOT = "/jsf_render_outputstyle_web";

  private static final String REL = "stylesheet";

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
   * @testName: outputStyleEncodeTest
   * @assertion_ids: PENDING
   * @test_Strategy: Validate the following test cases.
   * 
   *                 case 1: Make sure that there is a given number of 'link'
   *                 elements (2) tags rendered in the head of the response.
   * 
   *                 case 2: Make sure that the correct attributes are rendered
   *                 when we call for a style sheet with no Library.
   * 
   *                 case 3: Make sure that the correct attributes are rendered
   *                 when we call for a style sheet with library specified.
   * 
   *                 case 4: Make sure that the correct value is set when we
   *                 have a media attribute.
   * 
   * @since 2.0
   */
  public void outputStyleEncodeTest() throws Fault {
    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    // ------------------------------------------------------------ case 1
    HtmlPage pageOne = getPage(
        CONTEXT_ROOT + "/faces/encodetest_facelet.xhtml");

    List<HtmlLink> links = pageOne.getDocumentElement()
        .getHtmlElementsByTagName("link");

    if (2 != links.size()) {
      formatter.format("Wrong number of '%s' elements rendered. %n"
          + "Expected: %s %n" + "Received: " + links.size(), "link", "2");
    }

    // ------------------------------------------------------------ case 2

    HtmlPage pageTwo = getPage(
        CONTEXT_ROOT + "/faces/encodetest_facelet_1.xhtml");
    List<HtmlLink> linkTwo = pageTwo.getDocumentElement()
        .getHtmlElementsByTagName("link");

    String expected1 = "/jsf_render_outputstyle_web/faces/"
        + "javax.faces.resource/night.css";

    this.testLink(linkTwo.get(0), expected1, formatter);

    // ------------------------------------------------------------ case 3

    HtmlPage pageThree = getPage(
        CONTEXT_ROOT + "/faces/encodetest_facelet_2.xhtml");

    List<HtmlLink> linkThree = pageThree.getDocumentElement()
        .getHtmlElementsByTagName("link");

    String expected2 = "/jsf_render_outputstyle_web/faces/"
        + "javax.faces.resource/morning.css";

    String expectedLib = "ln=cssLibrary";

    this.testLink(linkThree.get(0), expected2, formatter);
    this.testLink(linkThree.get(0), expectedLib, formatter);

    // ------------------------------------------------------------ case 4
    HtmlPage pageOneA = getPage(
        CONTEXT_ROOT + "/faces/encodetest_facelet.xhtml");

    List<HtmlLink> link = pageOne.getDocumentElement()
        .getHtmlElementsByTagName("link");

    String mediaOne = link.get(0).getAttribute("media");
    this.testAtts("screen", mediaOne, "media", formatter);

    String mediaTwo = link.get(1).getAttribute("media");
    this.testAtts("all", mediaTwo, "media", formatter);

    handleTestStatus(messages);

  } // END outputStyleEncodeTest

  // -------------------------------------------------------- private methods
  // Test HtmlLink, attributes.
  private void testLink(HtmlLink link, String hrefExpected,
      Formatter formatter) {
    // test for href attribute
    String hrefresult = link.getHrefAttribute();

    this.testAtts(hrefExpected, hrefresult, "href", formatter);

    // test for rel attribute this is a constant.
    String relResult = link.getAttribute("rel");
    this.testAtts(REL, relResult, "rel", formatter);
  }

  // Test two String Objects for equality.
  private void testAtts(String expected, String result, String attName,
      Formatter formatter) {

    if (!result.contains(expected)) {
      formatter.format("Unexpected result for '%s' attribute! %n"
          + "Expected: %s %n" + "Received: %s %n", attName, expected, result);
    }
  }
} // END URLClient
