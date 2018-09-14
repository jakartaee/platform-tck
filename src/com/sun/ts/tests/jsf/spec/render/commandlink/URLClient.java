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
package com.sun.ts.tests.jsf.spec.render.commandlink;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Formatter;
import java.util.TreeMap;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;
import java.util.ArrayList;
import java.util.List;

public class URLClient extends BaseHtmlUnitClient {

  private static final String CONTEXT_ROOT = "/jsf_render_clink_web";

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
   * @testName: clinkRenderEncodeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Ensure proper CommandLink rendering: - case 1: Anchor has
   *                 href value of '#' Anchor display value is "Click Me1"
   *                 Anchor onclick attribute value has a non-zero length
   * 
   *                 - case 2: Anchor has href value of '#' Anchor display value
   *                 is "Click Me2" The styleClass tag attribute is rendered as
   *                 the class attribute on the rendered anchor Anchor onclick
   *                 attribute value has a non-zero length
   * 
   *                 - case 3: Anchor has href value of '#' Anchor has display
   *                 value of "Click Me3" which is specified as a nested
   *                 HtmlOutput tag. Anchor onclick attribute value has a
   *                 non-zero length
   * 
   *                 - case 4: REMOVED CODE DUE TO BUG ID:6460959
   * 
   *                 - case 5: CommandLink has the disabled attribute set to
   *                 true. Ensure that: A span element is rendered instead of an
   *                 anchor The span has no onclick content The display value of
   *                 the span is 'Disabled Link' The styleClass tag attribute is
   *                 rendered as the class attribute on the rendered span
   *                 element
   * 
   *                 - case 6: CommandLink has the disabled attribute set to
   *                 true with a nested output component as the link's textual
   *                 value.
   * 
   *                 - case 7: CommandLink is tied to a backend bean via the
   *                 "binding" attribute. Test to make sure that the "title",
   *                 "shape" & "styleclass" are set and rendered correctly.
   * 
   * @since 1.2
   */
  public void clinkRenderEncodeTest() throws Fault {

    StringBuilder messages = new StringBuilder(64);
    Formatter formatter = new Formatter(messages);

    List<HtmlPage> pages = new ArrayList<HtmlPage>();
    pages.add(getPage(CONTEXT_ROOT + "/faces/encodetest.jsp"));
    pages.add(getPage(CONTEXT_ROOT + "/faces/encodetest_facelet.xhtml"));

    for (HtmlPage page : pages) {

      HtmlAnchor link1 = (HtmlAnchor) getElementOfTypeIncludingId(page, "a",
          "link1");

      if (link1 == null) {
        formatter.format("Unable to find achor with ID containing 'link1'. %n");
      } else {
        if (!"#".equals(link1.getHrefAttribute())) {
          formatter.format(
              "Unexpected value for attribute 'href'. "
                  + "Expected '#', received: '%s' %n",
              link1.getHrefAttribute());
        }

        if (!"Click Me1".equals(link1.asText())) {
          formatter.format("Unexpected anchor text. %n" + "Expected: '%s' %n"
              + "Received: '%s' %n", "Click Me1", link1.asText());
        }

        if (link1.getOnClickAttribute().length() < 0) {
          formatter.format("Expected some render specific content "
              + "to be rendered in the 'onclick' attribute for "
              + "the anchor, but no content was found. %n");
        }
      }

      HtmlAnchor link2 = (HtmlAnchor) getElementOfTypeIncludingId(page, "a",
          "link2");

      if (link2 == null) {
        formatter.format("Unable to find achor with ID containing 'link1'. %n");
      } else {
        if (!"#".equals(link2.getHrefAttribute())) {
          formatter.format(
              "Unexpected value for attribute 'href'. "
                  + "Expected '#', received: '%s' %n",
              link2.getHrefAttribute());
        }

        if (!"Click Me2".equals(link2.asText())) {
          formatter.format("Unexpected anchor text. %n" + "Expected '%s' %n"
              + "Received: '%s' %n", "Click Me2", link2.asText());
        }

        if (!"sansserif".equals(link2.getAttribute("class"))) {
          formatter.format(
              "Unexpected value for class attribute. "
                  + "Expected 'sansserif' %n" + "Received: '%s'",
              link2.getAttribute("style"));
        }

        if (link2.getOnClickAttribute().length() < 0) {
          formatter.format("Expected some render specific content "
              + "to be rendered in the 'onclick' attribute for "
              + "the anchor, but no content was found. %n");
        }
      }

      HtmlAnchor link3 = (HtmlAnchor) getElementOfTypeIncludingId(page, "a",
          "link3");

      if (link3 == null) {
        formatter.format("Unable to find achor with ID containing 'link3'. %n");
      } else {
        if (!"#".equals(link3.getHrefAttribute())) {
          formatter.format(
              "Unexpected value for attribute 'href'. "
                  + "Expected '#', received: '%s' %n",
              link3.getHrefAttribute());
        }

        if (!"Click Me3".equals(link3.asText())) {
          formatter.format("Unexpected anchor text.  "
              + "Expected 'Click M3'to be the anchor text "
              + "when specified as a nested child (HtmlOutput), "
              + "but received: '%s' %n", link3.asText());
        }

        if (link3.getOnClickAttribute().length() < 0) {
          formatter.format("Expected some render specific content "
              + "to be rendered in the 'onclick' attribute for "
              + "the anchor, but no content was found. %n");
        }
      }

      // ----------------------------------------------------------- case
      // 5
      HtmlSpan span = (HtmlSpan) getElementOfTypeIncludingId(page, "span",
          "link5");

      if (span == null) {
        formatter.format("Unable to find a span element with an ID"
            + " containing 'link5' %n");
      } else {
        if (!"sansserif".equals(span.getAttribute("class"))) {
          formatter.format("Unexpected value for class attribute "
              + "for the rendered span element when CommandLink "
              + "is disabled. Expected 'sansserif'%n, " + "but received '%s'%n",
              span.getAttribute("class"));
        }

        if (!"Disabled Link".equals(span.asText())) {
          formatter.format(
              "Unexpected textual value for rendered "
                  + "span element when command link is disabled%n.  "
                  + "Expected 'Disabled Link'%n, but received '%s'%n",
              span.asText());
        }

        if (span.getOnClickAttribute().length() > 0) {
          formatter.format("Expected no render specific content "
              + "to be rendered in the 'onclick' attribute for "
              + "the span element, but content was found. %n");
        }
      }

      // ----------------------------------------------------------- case
      // 6
      HtmlSpan span2 = (HtmlSpan) getElementOfTypeIncludingId(page, "span",
          "link6");

      if (span2 == null) {
        formatter.format("Unable to find a span element with an ID"
            + " containing 'link6' %n");
      } else {
        if (!"Disabled Link(Nested)".equals(span2.asText())) {
          formatter.format("Unexpected textual value for rendered "
              + "span element when command link is disabled. "
              + "Expected 'Disabled Link(Nested)', " + "but received '%s' %n",
              span2.asText());
        }
      }

      // ----------------------------------------------------------- case
      // 7
      HtmlAnchor span7 = (HtmlAnchor) getElementOfTypeIncludingId(page, "a",
          "link7");

      if (span7 == null) {
        formatter.format(
            "Unable to find a span element with an ID" + " containing 'link7'");
      } else {
        if (!"sansserif".equals(span7.getAttribute("class"))) {
          formatter.format("Unexpected value for class attribute " + "for "
              + "the rendered anchor element when CommandLink is "
              + "disabled. Expected 'sansserif'%n, " + "but received '%s'%n",
              span7.getAttribute("class"));
        }

        if (!"rectangle".equals(span7.getShapeAttribute())) {
          formatter.format("Unexpected value for shape attribute "
              + "for the rendered anchor element when "
              + "CommandLink is disabled. Expected 'gone'%n, "
              + "but received '%s'%n", span7.getShapeAttribute());
        }

        if (!"gone".equals(span7.getAttribute("title"))) {
          formatter.format("Unexpected value for title attribute "
              + "for the rendered anchor element when "
              + "CommandLink is disabled. Expected 'gone'%n, "
              + "but received '%s'%n", span7.getAttribute("title"));
        }
      }

      handleTestStatus(messages);
    }
  } // END clinkRenderEncodeTest

  /**
   * @testName: clinkRenderDecodeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy:
   * 
   * @since 1.2
   */
  public void clinkRenderDecodeTest() throws Fault {
    StringBuilder messages = new StringBuilder(64);
    Formatter formatter = new Formatter(messages);

    List<HtmlPage> pages = new ArrayList<HtmlPage>();
    pages.add(getPage(CONTEXT_ROOT + "/faces/decodetest.jsp"));
    pages.add(getPage(CONTEXT_ROOT + "/faces/decodetest_facelet.xhtml"));

    for (HtmlPage page : pages) {
      HtmlAnchor link1 = (HtmlAnchor) getElementOfTypeIncludingId(page, "a",
          "link1");
      try {
        HtmlPage postBack = (HtmlPage) link1.click();
        // Check to see if an error header was returned
        String msgHeader = postBack.getWebResponse()
            .getResponseHeaderValue("actionEvent");
        if (msgHeader != null) {
          formatter.format(msgHeader + "%n");
        } else {
          // Check for non-error header
          msgHeader = postBack.getWebResponse()
              .getResponseHeaderValue("actionEventOK");
          if (msgHeader == null) {
            formatter.format("ActionListener was not invoked "
                + "when CommandButton 'command1' was clicked.%n");
          }

        }
      } catch (IOException e) {
        throw new Fault(e);
      }

      handleTestStatus(messages);
    }

  } // END clinkRenderDecodeTest

  /**
   * @testName: clinkRenderPassthroughTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Ensure those attributes marked as passthrough are indeed
   *                 visible in the rendered markup as specified in the JSP
   *                 page.
   * 
   * @since 1.2
   */
  public void clinkRenderPassthroughTest() throws Fault {

    TreeMap<String, String> control = new TreeMap<String, String>();
    control.put("accesskey", "U");
    control.put("charset", "ISO-8859-1");
    control.put("coords", "31,45");
    control.put("dir", "LTR");
    control.put("hreflang", "en");
    control.put("lang", "en");
    control.put("onblur", "js1");
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
    control.put("rel", "somevalue");
    control.put("rev", "revsomevalue");
    control.put("shape", "rect");
    control.put("style", "Color: red;");
    control.put("tabindex", "0");
    control.put("title", "sample");
    control.put("type", "type");

    TreeMap<String, String> controlSpan = new TreeMap<String, String>();
    controlSpan.put("accesskey", "U");
    controlSpan.put("dir", "LTR");
    controlSpan.put("lang", "en");
    controlSpan.put("onblur", "js1");
    controlSpan.put("ondblclick", "js3");
    controlSpan.put("onfocus", "js4");
    controlSpan.put("onkeydown", "js5");
    controlSpan.put("onkeypress", "js6");
    controlSpan.put("onkeyup", "js7");
    controlSpan.put("onmousedown", "js8");
    controlSpan.put("onmousemove", "js9");
    controlSpan.put("onmouseout", "js10");
    controlSpan.put("onmouseover", "js11");
    controlSpan.put("onmouseup", "js12");
    controlSpan.put("style", "Color: red;");
    controlSpan.put("tabindex", "0");
    controlSpan.put("title", "sample");
 

    StringBuilder messages = new StringBuilder(64);
    Formatter formatter = new Formatter(messages);

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
        controlSpan.put("foo", "bar");
        controlSpan.put("singleatt", "singleAtt");
        controlSpan.put("manyattone", "manyOne");
        controlSpan.put("manyatttwo", "manyTwo");
        controlSpan.put("manyattthree", "manyThree");
      }

      HtmlAnchor anchor = (HtmlAnchor) getElementOfTypeIncludingId(page, "a",
          "link1");
      HtmlSpan span = (HtmlSpan) getElementOfTypeIncludingId(page, "span",
          "link2");

      if (anchor == null) {
        formatter.format("Unable to find rendered anchor with ID "
            + "containing 'link1' %n");
      }

      if (span == null) {
        formatter.format(
            "Unable to find rendered span with ID " + "containing 'link2' %n");
      }

      if (span == null || anchor == null) {
        return;
      }

      validateAttributeSet(control, anchor,
          new String[] { "name", "id", "value", "href", "onclick" }, formatter);

      validateAttributeSet(controlSpan, span,
          new String[] { "name", "id", "value", "href", "onclick" }, formatter);

      handleTestStatus(messages);
    }

  } // END clinkRenderPassthroughTest
} // END URLClient
