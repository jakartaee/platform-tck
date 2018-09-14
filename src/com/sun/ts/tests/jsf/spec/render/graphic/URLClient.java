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
package com.sun.ts.tests.jsf.spec.render.graphic;

import java.io.PrintWriter;
import java.util.Formatter;
import java.util.TreeMap;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import java.util.ArrayList;
import java.util.List;

public class URLClient extends BaseHtmlUnitClient {

  private static final String CONTEXT_ROOT = "/jsf_render_img_web";

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
   * @testName: graphicRenderEncodeTest
   * @assertion_ids: PENDING
   * @test_Strategy: Ensure the proper rendering of the src, ismap, and class
   *                 attributes. The value of the src attribute is obtained from
   *                 the value of the component and encoded as needed. The value
   *                 of the class attribute is obtained from styleClass The
   *                 ismap attribute must be rendered using html attribute
   *                 minimization if the value in the JSP is true, otherwise not
   *                 rendered at all if false.
   * 
   * @since 1.2
   */
  public void graphicRenderEncodeTest() throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    final String expectedSrc = "pnglogo.png";

    List<HtmlPage> pages = new ArrayList<HtmlPage>();
    pages.add(getPage(CONTEXT_ROOT + "/faces/encodetest.jsp"));
    pages.add(getPage(CONTEXT_ROOT + "/faces/encodetest_facelet.xhtml"));

    for (HtmlPage page : pages) {
      HtmlImage image1 = (HtmlImage) getElementOfTypeIncludingId(page, "img",
          "img1");

      if (!validateExistence("img1", "img", image1, formatter)) {
        handleTestStatus(messages);
        return;
      }

      // validate the src attribute
      if (!image1.getSrcAttribute().contains(expectedSrc)) {
        formatter.format("Unexpected value for the src attribute for"
            + " image containing ID 'img1'.  Expected '%s', but"
            + " found '%s'%n", expectedSrc, image1.getSrcAttribute());
      }

      if (!HtmlElement.ATTRIBUTE_NOT_DEFINED
          .equals(image1.getAttribute("class"))) {
        formatter.format("Renderer incorrectly rendered class "
            + "attribute on image containing ID 'img1' when no "
            + "styleClass attribute was defined for that image in "
            + "the JSP%n");
      }

      // -------------------------------------------------------------
      // img2

      HtmlImage image2 = (HtmlImage) getElementOfTypeIncludingId(page, "img",
          "img2");

      // validate the src attribute
      if (!image2.getSrcAttribute().contains(expectedSrc)) {
        formatter.format("Unexpected value for the src attribute for"
            + " image containing ID 'img2'.  Expected '%s', but"
            + " found '%s'%n", expectedSrc, image2.getSrcAttribute());
      }

      final String expectedImgClass = "newBorder";

      if (!expectedImgClass.equals(image2.getAttribute("class"))) {
        formatter.format(
            "Unexpected value for the class attribute "
                + "for image containing ID 'img2'.  Expected '%s', but "
                + "found '%s'%n",
            expectedImgClass, image2.getAttribute("class"));
      }

      // ----------------------------------------------------------- img 3

      HtmlImage image3 = (HtmlImage) getElementOfTypeIncludingId(page, "img",
          "img3");

      if (!validateExistence("img3", "img", image3, formatter)) {
        handleTestStatus(messages);
        return;
      } else {
        if (!"ismap".equals(image3.getIsmapAttribute())) {
          formatter.format(
              "(img3) Expected the ismap attribute to "
                  + "to be rendered as '%s', instead found '%s'%n",
              "ismap", image3.getIsmapAttribute());
        }
      }

      // ----------------------------------------------------------- img 4

      HtmlImage image4 = (HtmlImage) getElementOfTypeIncludingId(page, "img",
          "img4");

      if (!validateExistence("img4", "img", image4, formatter)) {
        handleTestStatus(messages);
        return;
      } else {
        if (!HtmlElement.ATTRIBUTE_NOT_DEFINED
            .equals(image4.getIsmapAttribute())) {
          formatter.format("(img4) Expected the ismap attribute "
              + "to not be rendered when the ismap "
              + "attribute was specified as false in the JSP" + "%n.");
        }
      }

      // ----------------------------------------------------------- img 5
      HtmlImage image5 = (HtmlImage) getElementOfTypeIncludingId(page, "img",
          "img5");

      if (!validateExistence("img5", "img", image5, formatter)) {
        handleTestStatus(messages);
        return;
      }

      // test for title attribute.
      if (!"myIMG".equals(image5.getAttribute("title"))) {
        formatter
            .format(
                "Unexpected value for the title attribute %n"
                    + "Expected: '%s'%n" + "Received: '%s'%n",
                "myIMG", image5.getAttribute("title"));
      }

      // test for class attribute.
      if (!"newBorder".equals(image5.getAttribute("class"))) {
        formatter.format(
            "Unexpected value for the title attribute %n" + "Expected: '%s'%n"
                + "Received: '%s'%n",
            "newBorder", image5.getAttribute("class"));
      }

      // ----------------------------------------------------------- img 6
      String testImage = "img";
      HtmlImage image6 = (HtmlImage) getElementOfTypeIncludingId(page, "img",
          testImage);

      if (!validateExistence(testImage, "img", image6, formatter)) {
        handleTestStatus(messages);
        return;
      }

      // test for title attribute.
      if (HtmlElement.ATTRIBUTE_NOT_DEFINED.equals(image6.getSrcAttribute())
          || "".equals(image6.getSrcAttribute())) {
        formatter.format(
            "No resource found. Failed to find image "
                + "with EL Expression. %n Expression: '%s' %n",
            "#{resource['duke-boxer.gif']}");
      }

      handleTestStatus(messages);
    }
  } // END graphicRenderEncodeTest

  /**
   * @testName: graphicRenderPassthroughTest
   * @assertion_ids: PENDING
   * @test_Strategy: Ensure all passthrough attributes defined fo the renderer
   *                 are rendered with the value untouched.
   * 
   * @since 1.2
   */
  public void graphicRenderPassthroughTest() throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    TreeMap<String, String> control = new TreeMap<String, String>();
    control.put("alt", "alt description");
    control.put("dir", "LTR");
    control.put("height", "10");
    control.put("lang", "en");
    control.put("longdesc", "description");
    control.put("onclick", "js1");
    control.put("ondblclick", "js2");
    control.put("onkeydown", "js3");
    control.put("onkeypress", "js4");
    control.put("onkeyup", "js5");
    control.put("onmousedown", "js6");
    control.put("onmousemove", "js7");
    control.put("onmouseout", "js8");
    control.put("onmouseover", "js9");
    control.put("onmouseup", "js10");
    control.put("style", "Color: red;");
    control.put("title", "title");
    control.put("usemap", "map");
    control.put("width", "10");

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

      HtmlImage img1 = (HtmlImage) getElementOfTypeIncludingId(page, "img",
          "img1");
      if (!validateExistence("img1", "img", img1, formatter)) {
        handleTestStatus(messages);
        return;
      }

      validateAttributeSet(control, img1, new String[] { "id", "src" },
          formatter);

      handleTestStatus(messages);
    }

  } // END graphicRenderPassthroughTest
} // END URLClient
