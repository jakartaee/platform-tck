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
package com.sun.ts.tests.jsf.spec.templating.insert;

import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import java.io.PrintWriter;
import java.util.Formatter;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;
import java.util.TreeMap;

public class URLClient extends BaseHtmlUnitClient {

  private static final String CONTEXT_ROOT = "/jsf_templating_insert_web";

  private static final TreeMap<String, String> EXPECTED_OVERRIDES = new TreeMap<String, String>();

  private static final TreeMap<String, String> EXPECTED_DEFAULTS = new TreeMap<String, String>();

  {
    EXPECTED_DEFAULTS.put("title", "Default Title");
    EXPECTED_DEFAULTS.put("heading", "Default Heading");
    EXPECTED_DEFAULTS.put("content", "Default Content");

    EXPECTED_OVERRIDES.put("title", "OverRide Title");
    EXPECTED_OVERRIDES.put("heading", "OverRide Heading");
    EXPECTED_OVERRIDES.put("content", "OverRide Content");
  }

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
   * @testName: templateInsertUICompositeTest
   * @assertion_ids: PENDING
   * @test_Strategy: Validate the correct values are inserted when being called
   *                 from ui:composite tag in the using page.
   * 
   * @since 2.0
   */
  public void templateInsertUICompositeTest() throws Fault {

    // Test Defaults.
    HtmlPage pageOne = (getPage(
        CONTEXT_ROOT + "/faces/compositionPgOne.xhtml"));

    this.testPage(pageOne, EXPECTED_DEFAULTS);

    HtmlPage pageTwo = (getPage(
        CONTEXT_ROOT + "/faces/compositionPgTwo.xhtml"));

    this.testPage(pageTwo, EXPECTED_OVERRIDES);

  } // END templateInsertUICompositeTest

  /**
   * @testName: templateInsertUICompositeNegTest
   * @assertion_ids: PENDING
   * @test_Strategy: Validate JSF disregards everything outside of the
   *                 composition tag.
   * 
   * @since 2.0
   */
  public void templateInsertUICompositeNegTest() throws Fault {
    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    // Test Defaults.
    HtmlPage pageThree = (getPage(
        CONTEXT_ROOT + "/faces/compositionPgThree.xhtml"));

    this.testPage(pageThree, EXPECTED_OVERRIDES);

    HtmlDivision top = (HtmlDivision) getElementOfTypeIncludingId(pageThree,
        "div", "IGNORED_TOP");

    HtmlDivision bottom = (HtmlDivision) getElementOfTypeIncludingId(pageThree,
        "div", "IGNORED_BOTTOM");

    if (!(top == null)) {
      formatter.format("Unexpected Value Found! %n" + "Found: " + top.asText()
          + "%n" + "This String should not have been found JSF should ignore"
          + "everything outside of the uicompsite tags!");
    }

    if (!(bottom == null)) {
      formatter.format(
          "Unexpected Value Found! %n" + "Found: " + bottom.asText() + "%n"
              + "This String should not have been found JSF should ignore"
              + "everything outside of the uicompsite tags!");
    }

    handleTestStatus(messages);

  } // END templateInsertUICompositeNegTest

  /**
   * @testName: templateInsertUIDecorateTest
   * @assertion_ids: PENDING
   * @test_Strategy: Validate the correct values are inserted when being called
   *                 from ui:decorate tag in the using page.
   * 
   * @since 2.0
   */
  public void templateInsertUIDecorateTest() throws Fault {
    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    HtmlPage page = (getPage(CONTEXT_ROOT + "/faces/decoratePgOne.xhtml"));

    HtmlDivision template = (HtmlDivision) getElementOfTypeIncludingId(page,
        "div", "title_header_content");

    validateExistence("PROCESSED_TOP", "div", template, formatter);

    this.testString("Default TitleDefault HeadingDefault Content",
        template.asText(), formatter);

    handleTestStatus(messages);
  } // END templateInsertUIDecorateTest

  /**
   * @testName: templateInsertUIDecorateOutSideTest
   * @assertion_ids: PENDING
   * @test_Strategy: Validate JSF renders everything outside of the decorate
   *                 tag.
   * 
   * @since 2.0
   */
  public void templateInsertUIDecorateOutSideTest() throws Fault {
    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    // Test Defaults.
    HtmlPage page = (getPage(CONTEXT_ROOT + "/faces/decoratePgTwo.xhtml"));

    HtmlDivision top = (HtmlDivision) getElementOfTypeIncludingId(page, "div",
        "PROCESSED_TOP");

    validateExistence("PROCESSED_TOP", "div", top, formatter);

    HtmlDivision bottom = (HtmlDivision) getElementOfTypeIncludingId(page,
        "div", "PROCESSED_BOTTOM");

    validateExistence("PROCESSED_BOTTOM", "div", bottom, formatter);

    handleTestStatus(messages);
  } // END templateInsertUIDecorateOutSideTest

  // ---------------------------------------------------------- private
  // methods
  private Formatter testString(String key, String value, Formatter formatter) {

    if (!key.equals(value)) {
      formatter.format(
          "Unexpected Value!" + "%n" + "Expected: '%s' %n" + "Found: '%s'", key,
          value);
    }

    return formatter;
  }

  private void testPage(HtmlPage page, TreeMap<String, String> expected)
      throws Fault {
    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    checkForNull(expected, "expected");
    checkForNull(page, "page");

    // Test the title tag.
    String expTitle = expected.get("title");
    String title = page.getTitleText();

    this.testString(expTitle, title, formatter);

    // Test the heading.
    String expHead = expected.get("heading");
    HtmlDivision head = (HtmlDivision) getElementOfTypeIncludingId(page, "div",
        "heading");

    validateExistence("heading", "div", head, formatter);
    String headingValue = head.asText();

    this.testString(expHead, headingValue, formatter);

    // Test the content.
    String expContent = expected.get("content");
    HtmlDivision content = (HtmlDivision) getElementOfTypeIncludingId(page,
        "div", "content");

    validateExistence("content", "div", content, formatter);
    String contentValue = content.asText();

    this.testString(expContent, contentValue, formatter);

    handleTestStatus(messages);
  }

  public static void checkForNull(Object aObject, String argName) {
    if (aObject == null) {
      throw new NullPointerException(argName + " Argument is Null!");
    }
  }
} // END URLClient
