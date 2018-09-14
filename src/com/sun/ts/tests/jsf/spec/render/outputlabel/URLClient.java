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
package com.sun.ts.tests.jsf.spec.render.outputlabel;

import com.gargoylesoftware.htmlunit.html.HtmlLabel;
import java.io.PrintWriter;
import java.util.Formatter;
import java.util.TreeMap;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;
import java.util.ArrayList;
import java.util.List;

public class URLClient extends BaseHtmlUnitClient {

  private static final String CONTEXT_ROOT = "/jsf_render_outputlabel_web";

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
   * @testName: outputLabelRenderEncodeTest
   * @assertion_ids: PENDING
   * @test_Strategy: Validate the rendering of the h:OutputLabel tag and its
   *                 attributes. case 1: - The "id", "for" & "styleClass"
   *                 attributes are defined. case 2: - Attributes for "id" &
   *                 "value" are defined. Verify that the default value of the
   *                 escape attribute is 'true' and that text containing angle
   *                 brackets is rendered with escape characters. case 3: -
   *                 Attributes for "id" & "value" & "escape" are defined.
   *                 Verify that when the escape attribute is set to 'true',
   *                 text containing angle brackets is rendered with escape
   *                 characters. case 4: - Attributes for "id" & "value" &
   *                 "escape" are defined. Verify that when the escape attribute
   *                 is set to 'false', text containing angle brackets is
   *                 rendered without escape characters. case 5: - Using the
   *                 binding attribute to tie to a backing bean. make sure that
   *                 the value is rendered correctly.
   * 
   * @since 1.2
   */
  public void outputLabelRenderEncodeTest() throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    List<HtmlPage> pages = new ArrayList<HtmlPage>();
    pages.add(getPage(CONTEXT_ROOT + "/faces/encodetest.jsp"));
    pages.add(getPage(CONTEXT_ROOT + "/faces/encodetest_facelet.xhtml"));

    for (HtmlPage page : pages) {
      // ---------------------------------------------------------- case 1
      HtmlLabel label_one = (HtmlLabel) getElementOfTypeIncludingId(page,
          "label", "labelOne");

      if (!validateExistence("labelOne", "label", label_one, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!"text".equals(label_one.getAttribute("class"))) {
        formatter.format(
            "Unexpected rendered value for the "
                + "\"styleClass\" attribute on the '%s' tag with the "
                + "id of '%s'. %nExpected: '%s' %nFound: '%s' %n",
            "h:label", "labelOne", "text", label_one.getAttribute("class"));
      }

      if (!"name".equals(label_one.getForAttribute())) {
        formatter.format(
            "Unexpected rendered value for the \"for\" "
                + "attribute on the '%s' tag with the id of '%s'. %n"
                + "Expected: '%s' %nFound: '%s' %n",
            "h:label", "labelOne", "name", label_one.getForAttribute());
      }

      // ---------------------------------------------------------- case 2

      HtmlLabel label_two = (HtmlLabel) getElementOfTypeIncludingId(page,
          "label", "labelTwo");

      if (!validateExistence("labelTwo", "label", label_two, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!"<default>".equals(label_two.asText())) {
        formatter.format(
            "Unexpected rendered value for the " + "\"value\" attribute. %n"
                + "Expected: '%s' %n" + "Recieved: '%s' %n",
            "<default>", label_two.asText());
      }

      // ---------------------------------------------------------- case 3
      HtmlLabel label_three = (HtmlLabel) getElementOfTypeIncludingId(page,
          "label", "labelThree");

      if (!validateExistence("labelThree", "label", label_three, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!"<true>".equals(label_three.asText())) {
        formatter.format(
            "Unexpected rendered value for the " + "\"value\" attribute. %n"
                + "Expected: '%s' %n" + "Recieved: '%s' %n",
            "<true>", label_three.asText());
      }
      // ---------------------------------------------------------- case 4
      HtmlLabel label_four = (HtmlLabel) getElementOfTypeIncludingId(page,
          "label", "labelFour");

      if (!validateExistence("labelFour", "label", label_four, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!"".equals(label_four.asText())) {
        formatter.format(
            "Unexpected rendered value for the " + "\"value\" attribute. %n"
                + "Expected: '%s' %n" + "Recieved: '%s' %n",
            "", label_four.asText());
      }

      // ---------------------------------------------------------- case 5
      HtmlLabel label_five = (HtmlLabel) getElementOfTypeIncludingId(page,
          "label", "labelFive");

      String expected = "This is an Output UIComponent";

      if (!validateExistence("labelFive", "label", label_five, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!expected.equals(label_five.asText())) {
        formatter
            .format(
                "Unexpected value for '%s' Attribute! %n" + "Expected: '%s' %n"
                    + "Recieved: '%s' %n",
                "value", expected, label_five.asText());
      }

      if (!"text".equals(label_five.getAttribute("class"))) {
        formatter.format(
            "Unexpected value for '%s'! %n " + "Expected: '%s' %n"
                + "Found: '%s' %n",
            "labelFive", "text", label_one.getAttribute("class"));
      }

      handleTestStatus(messages);
    }
  } // END outputLinkRenderEncodeTest

  /**
   * @testName: outputLabelRenderPassthroughTest
   * @assertion_ids: PENDING
   * @test_Strategy: Ensure the attributes that are considered passthrough by
   *                 the specification are rendered as is.
   * 
   * @since 1.2
   */
  public void outputLabelRenderPassthroughTest() throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    TreeMap<String, String> control = new TreeMap<String, String>();
    control.put("accesskey", "P");
    control.put("dir", "LTR");
    control.put("lang", "en");
    control.put("onblur", "js1");
    control.put("onclick", "js2");
    control.put("ondblclick", "js3");
    control.put("onfocus", "js4");
    control.put("onkeydown", "js5");
    control.put("onkeypress", "js6");
    control.put("onkeyup", "js7");
    control.put("onmousedown", "js8");
    control.put("onmousemove", "js9");
    control.put("onmouseout", "js10");
    control.put("onmouseover", "js11");
    control.put("onmouseup", "js12");
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

      HtmlLabel output1 = (HtmlLabel) getElementOfTypeIncludingId(page, "label",
          "output1");

      if (!validateExistence("output1", "label", output1, formatter)) {
        handleTestStatus(messages);
        return;
      }

      validateAttributeSet(control, output1,
          new String[] { "id", "value", "name", "type" }, formatter);

      handleTestStatus(messages);
    }
  } // END outputLinkRenderPassthroughTest
} // END URLClient
