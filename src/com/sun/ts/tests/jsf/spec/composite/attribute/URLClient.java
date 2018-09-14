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
package com.sun.ts.tests.jsf.spec.composite.attribute;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Formatter;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;
import java.util.ArrayList;
import java.util.List;

public class URLClient extends BaseHtmlUnitClient {

  private static final String SERVLET_MAPPING = "/faces";

  private static final String CONTEXT_ROOT = "/jsf_composite_attribute_web"
      + SERVLET_MAPPING;

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
   * @testName: compositeAttributeTagTest
   * @assertion_ids: PENDING
   * @test_Strategy: Test to make sure the following attributes can be used. -
   *                 name - targets - default - displayName - required -
   *                 preferred - expert - shortDescription - method-signature -
   *                 type
   * 
   * @since 2.0
   */
  public void compositeAttributeTagTest() throws Fault {
    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    HtmlPage pageOne = getPage(CONTEXT_ROOT + "/attributeTest.xhtml");
    HtmlPage pageTwo = getPage(CONTEXT_ROOT + "/attributeTestTwo.xhtml");

    String bandName = "Rush";
    String albumName = "Hemispheres";
    List<String> songs = new ArrayList<String>();
    songs.add("Cygnus X-1 Book II");
    songs.add("Circumstances");
    songs.add("The Trees");
    songs.add("La Villa Strangiato");

    /*
     * -------------------------------------------------------------- case 1
     * 
     * Validate that using only the 'name' attribute for the cc.attribute tag
     * that the interface attributes are picked up correctly by the
     * implementation.
     */

    HtmlSpan testOneA = (HtmlSpan) getElementOfTypeIncludingId(pageOne,
        TAG_NAME, "artist");

    HtmlSpan testOneB = (HtmlSpan) getElementOfTypeIncludingId(pageOne,
        TAG_NAME, "album");

    HtmlSelect testOneC = (HtmlSelect) getElementOfTypeIncludingId(pageOne,
        "select", "tracks");

    if (!validateExistence("artist", TAG_NAME, testOneA, formatter)) {
      handleTestStatus(messages);
      return;
    }

    if (!validateExistence("album", TAG_NAME, testOneB, formatter)) {
      handleTestStatus(messages);
      return;
    }

    if (!validateExistence("tracks", "select", testOneC, formatter)) {
      handleTestStatus(messages);
      return;
    }

    validateElementValue(testOneA, bandName, formatter);
    validateElementValue(testOneB, albumName, formatter);
    validateSelectOptions(testOneC, songs, formatter);

    /*
     * -------------------------------------------------------------- case 2
     * 
     * Validate that when we nest the cc.attribute tags inside another
     * cc.attribute tag that the model attribute and targets attributes work
     * correctly and render the correct response.
     */
    HtmlSpan testTwoA = (HtmlSpan) getElementOfTypeIncludingId(pageOne,
        TAG_NAME, "artist");

    HtmlSpan testTwoB = (HtmlSpan) getElementOfTypeIncludingId(pageOne,
        TAG_NAME, "album");

    HtmlSelect testTwoC = (HtmlSelect) getElementOfTypeIncludingId(pageOne,
        "select", "tracks");

    if (!validateExistence("artist", TAG_NAME, testTwoA, formatter)) {
      handleTestStatus(messages);
      return;
    }

    if (!validateExistence("album", TAG_NAME, testTwoB, formatter)) {
      handleTestStatus(messages);
      return;
    }

    if (!validateExistence("tracks", "select", testTwoC, formatter)) {
      handleTestStatus(messages);
      return;
    }

    validateElementValue(testTwoA, bandName, formatter);
    validateElementValue(testTwoB, albumName, formatter);
    validateSelectOptions(testTwoC, songs, formatter);

    /*
     * -------------------------------------------------------------- case 3
     * 
     * Validate the method-signature attribute, using a backing bean with a
     * method that excepts a ActionEvent.
     */
    HtmlSpan testThreeA = (HtmlSpan) getElementOfTypeIncludingId(pageTwo,
        TAG_NAME, "artist");

    HtmlSpan testThreeB = (HtmlSpan) getElementOfTypeIncludingId(pageTwo,
        TAG_NAME, "album");

    HtmlSelect testThreeC = (HtmlSelect) getElementOfTypeIncludingId(pageTwo,
        "select", "tracks");

    if (!validateExistence("artist", TAG_NAME, testThreeA, formatter)) {
      handleTestStatus(messages);
      return;
    }

    if (!validateExistence("album", TAG_NAME, testThreeB, formatter)) {
      handleTestStatus(messages);
      return;
    }

    if (!validateExistence("tracks", "select", testThreeC, formatter)) {
      handleTestStatus(messages);
      return;
    }

    validateElementValue(testThreeA, bandName, formatter);
    validateElementValue(testThreeB, albumName, formatter);
    validateSelectOptions(testThreeC, songs, formatter);

    HtmlSubmitInput button = (HtmlSubmitInput) getInputIncludingId(pageTwo,
        "erase");

    try {
      HtmlPage postBack = (HtmlPage) button.click();
      HtmlTextArea tArea = (HtmlTextArea) getElementOfTypeIncludingId(postBack,
          "textarea", "comments");
      String comment = tArea.asText();
      String expected = "You Pressed ERASE!";

      if (!comment.equals(expected)) {
        formatter.format(
            "Unexpected text for %s! %n" + "Expected: %s %n" + "Found: %s %n",
            tArea.getId(), expected, comment);
      }
    } catch (IOException e) {
      throw new Fault(e);
    }

    handleTestStatus(messages);

  } // END compositeClasspathPKGTest //
}
