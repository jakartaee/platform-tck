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
package com.sun.ts.tests.jsf.spec.render.outputtext;

import java.io.PrintWriter;
import java.util.Formatter;
import java.util.TreeMap;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;
import java.util.ArrayList;
import java.util.List;

public class URLClient extends BaseHtmlUnitClient {

  private static final String CONTEXT_ROOT = "/jsf_render_outputtext_web";

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
   * @testName: outputTextRenderEncodeTest
   * @assertion_ids: PENDING
   * @test_Strategy: Validate the rendering of text fields (OutputText): case 1:
   *                 - Only the id and styleClass attributes are defined. case
   *                 2: - Attributes for id, value, class are defined. Verify
   *                 that the default value of the escape attribute is 'true'
   *                 and that text containing angle brackets is rendered with
   *                 escape characters. case 3: - Attributes for id, value,
   *                 class, and escape are defined. Verify that when the escape
   *                 attribute is set to 'true', text containing angle brackets
   *                 is rendered with escape characters. case 4: - Attributes
   *                 for id, value, class, and escape are defined. Verify that
   *                 when the escape attribute is set to 'false', text
   *                 containing angle brackets is rendered without escape
   *                 characters. case 5: - Using the binding attribute to tie to
   *                 a backing bean make sure the following holds true. The
   *                 styleClass attribute rendered correctly.
   * 
   * @since 1.2
   */
  public void outputTextRenderEncodeTest() throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    List<HtmlPage> pages = new ArrayList<HtmlPage>();
    pages.add(getPage(CONTEXT_ROOT + "/faces/encodetest.jsp"));
    pages.add(getPage(CONTEXT_ROOT + "/faces/encodetest_facelet.xhtml"));

    for (HtmlPage page : pages) {
      // ----------------------------------------------------------- text1

      HtmlSpan output1 = (HtmlSpan) getElementOfTypeIncludingId(page, "span",
          "text1");

      if (!validateExistence("text1", "span", output1, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!"text".equals(output1.getAttribute("class"))) {
        formatter.format(
            "Unexpected value for '%s' attribute on " + "%s! %n"
                + "Expected: '%s' %n" + "Found: '%s' %n",
            "styleClass", "text1", "text", output1.getAttribute("class"));
      }

      // ----------------------------------------------------------- text2

      HtmlSpan text2 = (HtmlSpan) getElementOfTypeIncludingId(page, "span",
          "text2");

      if (!validateExistence("text2", "input", text2, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!"<p>".equals(text2.asText())) {
        formatter.format(
            "Unexpected value for'%s' attribute on " + "%s! %n"
                + "Expected: '%s' %n" + "Found: '%s' %n",
            "value", "text2", "<p>", text2.asText());
      }

      // ----------------------------------------------------------- text3

      HtmlSpan text3 = (HtmlSpan) getElementOfTypeIncludingId(page, "span",
          "text3");

      if (!validateExistence("text3", "input", text3, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!"<p>".equals(text3.asText())) {
        formatter.format(
            "Unexpected value for '%s' attribute! on " + "%s %n"
                + "Expected: '%s' %n" + "Found: '%s' %n",
            "value", "text3", "<p>", text3.asText());
      }

      // ----------------------------------------------------------- text4

      HtmlSpan text4 = (HtmlSpan) getElementOfTypeIncludingId(page, "span",
          "text4");

      if (!validateExistence("text4", "input", text4, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if ("<p>".equals(text4.asText())) {
        formatter.format(
            "Unexpected value for '%s' attribute! on " + "%s %n"
                + "Expected: '%s' %n" + "Found: '%s' %n",
            "value", "text4", "", text4.asText());
      }

      // ----------------------------------------------------------- text5
      HtmlSpan output5 = (HtmlSpan) getElementOfTypeIncludingId(page, "span",
          "text5");

      if (!validateExistence("text5", "span", output1, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!"text".equals(output5.getAttribute("class"))) {
        formatter.format(
            "Unexpected value for '%s' attribute on " + "%s! %n"
                + "Expected: '%s' %n" + "Found: '%s' %n",
            "styleClass", "text5", "text", output5.getAttribute("class"));
      }

      handleTestStatus(messages);
    }
  } // END outputTextRenderEncodeTest

  /**
   * @testName: outputTextRenderPassthroughTest
   * @assertion_ids: PENDING
   * @test_Strategy: Ensure the attributes that are considered passthrough by
   *                 the specification are rendered as is.
   * 
   * @since 1.2
   */
  public void outputTextRenderPassthroughTest() throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    TreeMap<String, String> control = new TreeMap<String, String>();
    control.put("dir", "LTR");
    control.put("lang", "en");
    control.put("style", "Color: red;");
    control.put("title", "title");

    List<HtmlPage> pages = new ArrayList<HtmlPage>();
    pages.add(getPage(CONTEXT_ROOT + "/faces/passthroughtest.jsp"));
    pages.add(getPage(CONTEXT_ROOT + "/faces/passthroughtest_facelet.xhtml"));

    for (HtmlPage page : pages) {
      // Facelet Specific PassThrough options
      if (page.getTitleText().contains("facelet")) {
        control.put("foo", "bar");
        control.put("singleatt", "singleAtt");
        control.put("manyattone", "manyOne");
        control.put("manyatttwo", "manyTwo");
        control.put("manyattthree", "manyThree");
      }

      HtmlSpan output1 = (HtmlSpan) getElementOfTypeIncludingId(page, "span",
          "output1");
      if (!validateExistence("output1", "span", output1, formatter)) {
        handleTestStatus(messages);
        return;
      }

      validateAttributeSet(control, output1,
          new String[] { "id", "value", "name", "type" }, formatter);

      handleTestStatus(messages);
    }
  } // END outputTextRenderPassthroughTest
} // END URLClient
