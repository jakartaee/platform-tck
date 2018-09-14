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
package com.sun.ts.tests.jsf.spec.render.outputformat;

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

  private static final String CONTEXT_ROOT = "/jsf_render_outputformat_web";

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
   * @testName: outputFormatRenderEncodeTest
   * @assertion_ids: PENDING
   * @test_Strategy: Validate the rendering of the h:OutputFormat tag and its
   *                 attributes. case 1: - The "id", "value", & "styleClass"
   *                 attributes are defined. Test the styleClass. case 2: - The
   *                 "id" & "value" our defined. The value is defined to utilize
   *                 the child components (f:param) in the rendered output.
   *                 Validate the message is rendered correctly. case 3: -
   *                 Attributes for "id" & "value" are defined. Verify that the
   *                 default value of the escape attribute is 'true' and that
   *                 text containing angle brackets is rendered with escape
   *                 characters. case 4: - Attributes for "id" & "value" &
   *                 "escape" are defined. Verify that when the escape attribute
   *                 is set to 'true', text containing angle brackets is
   *                 rendered with escape characters. case 5: - Attributes for
   *                 "id" & "value" & "escape" are defined. Verify that when the
   *                 escape attribute is set to 'false', text containing angle
   *                 brackets is rendered without escape characters. case 6: -
   *                 The "id" & "value" our defined. The value is defined to
   *                 utilize the child components (f:param) in the rendered
   *                 output. Validate the message is rendered correctly, from a
   *                 backing bean. case 7: - The "binding" attribute is set. Set
   *                 to make sure that the correct "id", "value", & "class" are
   *                 set by the backing bean.
   * 
   * 
   * @since 1.2
   */
  public void outputFormatRenderEncodeTest() throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    List<HtmlPage> pages = new ArrayList<HtmlPage>();
    pages.add(getPage(CONTEXT_ROOT + "/faces/encodetest.jsp"));
    pages.add(getPage(CONTEXT_ROOT + "/faces/encodetest_facelet.xhtml"));

    for (HtmlPage page : pages) {
      // ---------------------------------------------------------- case 1
      HtmlSpan format_one = (HtmlSpan) getElementOfTypeIncludingId(page, "span",
          "formatter1");

      if (!validateExistence("formatter1", "span", format_one, formatter)) {
        handleTestStatus(messages);
        return;
      }

      String clazzAttrOne = format_one.getAttribute("class");
      if (!"text".equals(clazzAttrOne)) {
        formatter.format(
            "Unexpected rendered value for the "
                + "\"styleClass\" attribute on the '%s' tag with the "
                + "id of '%s'. %nExpected: '%s' %nFound: '%s' %n",
            "h:outputFormat", "formatter1", "text", clazzAttrOne);
      }

      // ---------------------------------------------------------- case 2
      String result_two = "Technology: JSF, Tag: h:outputFormat";

      HtmlSpan format_two = (HtmlSpan) getElementOfTypeIncludingId(page, "span",
          "formatter2");

      if (!validateExistence("formatter2", "span", format_two, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!result_two.equals(format_two.asText())) {
        formatter.format("Unexpected rendered value! %n" + "Expected: '%s' %n"
            + "Recieved: '%s' %n", result_two, format_two.asText());
      }
      // ---------------------------------------------------------- case 3

      HtmlSpan format_three = (HtmlSpan) getElementOfTypeIncludingId(page,
          "span", "formatter3");

      if (!validateExistence("formatter3", "span", format_three, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!"<default>".equals(format_three.asText())) {
        formatter.format("Unexpected rendered value! %n" + "Expected: '%s' %n"
            + "Recieved: '%s' %n", "<default>", format_three.asText());
      }

      // ---------------------------------------------------------- case 4
      HtmlSpan fomat_four = (HtmlSpan) getElementOfTypeIncludingId(page, "span",
          "formatter4");

      if (!validateExistence("formatter4", "span", fomat_four, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!"<true>".equals(fomat_four.asText())) {
        formatter.format("Unexpected rendered value! %n" + "Expected: '%s' %n"
            + "Recieved: '%s' %n", "<true>", fomat_four.asText());
      }
      // ---------------------------------------------------------- case 5
      HtmlSpan format_five = (HtmlSpan) getElementOfTypeIncludingId(page,
          "span", "formatter5");

      if (!validateExistence("formatter5", "span", format_five, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!"".equals(format_five.asText())) {
        formatter.format("Unexpected rendered value! %n" + "Expected: '%s' %n"
            + "Recieved: '%s' %n", "", format_five.asText());
      }

      // ---------------------------------------------------------- case 6
      String result_six = "Technology: JSF, Tag: f:param";

      HtmlSpan format_six = (HtmlSpan) getElementOfTypeIncludingId(page, "span",
          "formatter6");

      if (!validateExistence("formatter6", "span", format_six, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!result_six.equals(format_six.asText())) {
        formatter.format("Unexpected rendered value! %n" + "Expected: '%s' %n"
            + "Recieved: '%s' %n", result_six, format_six.asText());
      }

      // ---------------------------------------------------------- case 7
      String result_seven = "100-50";

      HtmlSpan format_seven = (HtmlSpan) getElementOfTypeIncludingTitle(page,
          "span", "formatter7");

      if (!validateExistence("formatter7", "span", format_six, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!result_seven.equals(format_seven.asText())) {
        formatter.format("Unexpected rendered value! %n" + "Expected: '%s' %n"
            + "Recieved: '%s' %n", result_six, format_six.asText());
      }

      String clazzAttrSeven = format_seven.getAttribute("class");
      if (!"text".equals(clazzAttrSeven)) {
        formatter.format(
            "Unexpected rendered value for the "
                + "\"styleClass\" attribute on the '%s' tag with the "
                + "id of '%s'. %nExpected: '%s' %nFound: '%s' %n",
            "h:outputFormat", "formatter7", "text", clazzAttrSeven);
      }

      // Handle any test messages.
      handleTestStatus(messages);
    }
  } // END outputLinkRenderEncodeTest

  /**
   * @testName: outputFormatRenderPassthroughTest
   * @assertion_ids: PENDING
   * @test_Strategy: Ensure the attributes that are considered passthrough by
   *                 the specification are rendered as is.
   * 
   * @since 1.2
   */
  public void outputFormatRenderPassthroughTest() throws Fault {

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

      HtmlSpan format_one = (HtmlSpan) getElementOfTypeIncludingId(page, "span",
          "formatter1");

      if (!validateExistence("formatter1", "span", format_one, formatter)) {
        handleTestStatus(messages);
        return;
      }

      validateAttributeSet(control, format_one,
          new String[] { "id", "value", "name", "type" }, formatter);

      handleTestStatus(messages);
    }
  } // END outputLinkRenderPassthroughTest
} // END URLClient
