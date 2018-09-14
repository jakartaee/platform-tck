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
package com.sun.ts.tests.jsf.spec.render.outputlink;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import java.io.PrintWriter;
import java.util.Formatter;
import java.util.TreeMap;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;
import java.util.ArrayList;
import java.util.List;

public class URLClient extends BaseHtmlUnitClient {

  private static final String CONTEXT_ROOT = "/jsf_render_outputlink_web";

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
   * @testName: outputLinkRenderEncodeTest
   * @assertion_ids: PENDING
   * @test_Strategy: Validate the rendering of the h:OutputLink tag and its
   *                 attributes. case 1: - Only the id and styleClass attributes
   *                 are defined. case 2: - Test to make sure that "target"
   *                 attribute is rendered and set to the correct value. case 3:
   *                 - Using the binding attribute make sure the following is
   *                 rendered, id and styleClass.
   * 
   * @since 1.2
   */
  public void outputLinkRenderEncodeTest() throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    List<HtmlPage> pages = new ArrayList<HtmlPage>();
    pages.add(getPage(CONTEXT_ROOT + "/faces/encodetest.jsp"));
    pages.add(getPage(CONTEXT_ROOT + "/faces/encodetest_facelet.xhtml"));

    for (HtmlPage page : pages) {
      // ---------------------------------------------------------- case 1
      HtmlAnchor output1 = (HtmlAnchor) getElementOfTypeIncludingId(page, "a",
          "case_one");

      if (!validateExistence("case_one", "a", output1, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!"text".equals(output1.getAttribute("class"))) {
        formatter.format(
            "Unexpected value for '%s' attribute on " + "'%s' %n"
                + "Expected: %s %n" + "Found: '%s' %n",
            "styleclass", "case_one", "text", output1.getAttribute("class"));
      }

      // ---------------------------------------------------------- case 2
      HtmlAnchor output2 = (HtmlAnchor) getElementOfTypeIncludingId(page, "a",
          "case_two");

      if (!validateExistence("case_two", "a", output2, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!"_top".equals(output2.getTargetAttribute())) {
        formatter.format(
            "Unexpected value for '%s' attribute on " + "'%s' %n"
                + "Expected: %s %n" + "Found: '%s' %n",
            "target", "case_two", "_top", output1.getTargetAttribute());
      }

      // ---------------------------------------------------------- case 3
      HtmlAnchor output3 = (HtmlAnchor) getElementOfTypeIncludingId(page, "a",
          "case_three");

      if (!validateExistence("case_three", "a", output3, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!"text".equals(output3.getAttribute("class"))) {
        formatter.format(
            "Unexpected value for '%s' attribute on " + "'%s' %n"
                + "Expected: %s %n" + "Found: '%s' %n",
            "styleclass", "case_three", "text", output3.getAttribute("class"));
      }

      handleTestStatus(messages);
    }
  } // END outputLinkRenderEncodeTest

  /**
   * @testName: outputLinkRenderPassthroughTest
   * @assertion_ids: PENDING
   * @test_Strategy: Ensure the attributes that are considered passthrough by
   *                 the specification are rendered as is.
   * 
   * @since 1.2
   */
  public void outputLinkRenderPassthroughTest() throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    TreeMap<String, String> control = new TreeMap<String, String>();
    control.put("accesskey", "P");
    control.put("charset", "ISO_8859-1:1987");
    control.put("coords", "nothing");
    control.put("dir", "LTR");
    control.put("hreflang", "en");
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
    control.put("rel", "somewhere");
    control.put("rev", "beenthere");
    control.put("shape", "poly");
    control.put("style", "Color: red;");
    control.put("tabindex", "1");
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

      HtmlAnchor output1 = (HtmlAnchor) getElementOfTypeIncludingId(page, "a",
          "output1");

      if (!validateExistence("output1", "a", output1, formatter)) {
        handleTestStatus(messages);
        return;
      }

      validateAttributeSet(control, output1,
          new String[] { "id", "href", "name", "type" }, formatter);

      handleTestStatus(messages);
    }
  } // END outputLinkRenderPassthroughTest
} // END URLClient
