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
package com.sun.ts.tests.jsf.spec.render.commandbutton;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Formatter;
import java.util.TreeMap;

import com.gargoylesoftware.htmlunit.html.HtmlImageInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlResetInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;
import java.util.ArrayList;
import java.util.List;

public class URLClient extends BaseHtmlUnitClient {

  private static final String CONTEXT_ROOT = "/jsf_render_cbutton_web";

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
   * @testName: cbuttonRenderEncodeNonPassthroughTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Render several command buttons within a form using
   *                 combinations of the various non-passthrough attributes to
   *                 ensure the command button and the non-passthrough
   *                 attributes are properly rendered.
   * 
   * @since 1.2
   */
  public void cbuttonRenderEncodeNonPassthroughTest() throws Fault {
    StringBuilder messages = new StringBuilder(64);
    Formatter formatter = new Formatter(messages);

    List<HtmlPage> pages = new ArrayList<HtmlPage>();
    pages.add(getPage(CONTEXT_ROOT + "/faces/encodetest.jsp"));
    pages.add(getPage(CONTEXT_ROOT + "/faces/encodetest_facelet.xhtml"));

    for (HtmlPage page : pages) {
      HtmlSubmitInput button1 = (HtmlSubmitInput) getInputIncludingId(page,
          "command1");

      // begin validate command1
      if (button1 == null) {
        formatter.format("Unable to find rendered"
            + " command button containing ID 'command1' %n");
      } else {
        if (!"Click Me".equals(button1.getValueAttribute())) {
          formatter.format(
              "Expected value attribute for command1 to"
                  + " be 'Click Me', found '%s' %n",
              button1.getValueAttribute());
        }

        if (!"submit".equals(button1.getTypeAttribute())) {
          formatter.format("Expected type attribute for command1 to "
              + "be 'submit', found '%s' %n", button1.getTypeAttribute());
        }
      }

      HtmlSubmitInput button2 = (HtmlSubmitInput) getInputIncludingId(page,
          "command2");

      if (button2 == null) {
        formatter.format("Unable to find rendered"
            + " command button containing ID 'command2' %n");
      } else {
        if (!"Click Me".equals(button2.getValueAttribute())) {
          formatter.format(
              "Expected value attribute for command2 to"
                  + " be 'Click Me', found '%s' %n",
              button2.getValueAttribute());
        }

        if (!"submit".equals(button2.getTypeAttribute())) {
          formatter.format("Expected type attribute for command2 to "
              + "be 'submit', found '%s' %n", button2.getTypeAttribute());
        }
      }

      HtmlResetInput button3 = (HtmlResetInput) getInputIncludingId(page,
          "command3");

      if (button3 == null) {
        formatter.format("Unable to find rendered"
            + " command button containing ID 'command3' %n");
      } else {
        if (!"Click Me".equals(button3.getValueAttribute())) {
          formatter.format(
              "Expected value attribute for command3 to"
                  + " be 'Click Me', found '%s' %n",
              button3.getValueAttribute());
        }

        if (!"reset".equals(button3.getTypeAttribute())) {
          formatter.format("Expected type attribute for command3 to "
              + "be 'reset', found '%s' %n", button3.getTypeAttribute());
        }
      }

      HtmlSubmitInput button4 = (HtmlSubmitInput) getInputIncludingId(page,
          "command4");

      if (button4 == null) {
        formatter.format("Unable to find rendered"
            + " command button containing ID 'command4' %n");
      } else {
        if (!"Click Me".equals(button4.getValueAttribute())) {
          formatter.format(
              "Expected value attribute for command4 to"
                  + " be 'Click Me', found '%s' %n",
              button4.getValueAttribute());
        }

        if (!"submit".equals(button4.getTypeAttribute())) {
          formatter.format("Expected type attribute for command4 with an"
              + " invalid type attribute value specified to "
              + "be 'submit', found '%s' %n", button4.getTypeAttribute());
        }
      }

      HtmlSubmitInput button5 = (HtmlSubmitInput) getInputIncludingId(page,
          "command5");

      if (button5 == null) {
        formatter.format("Unable to find rendered"
            + " command button containing ID 'command5' %n");
      } else {
        if (!"Click Me".equals(button5.getValueAttribute())) {
          formatter.format(
              "Expected value attribute for command5 to"
                  + " be 'Click Me', found '%s' %n",
              button5.getValueAttribute());
        }

        if (!"submit".equals(button5.getTypeAttribute())) {
          formatter.format("Expected type attribute for command5 to "
              + "be 'submit', found '%s' %n", button5.getTypeAttribute());
        }

        if (!"Color: red;".equals(button5.getAttribute("class"))) {
          formatter.format(
              "Expected class attribute for command5 to"
                  + " be 'Color: red;', found '%s %n",
              button5.getAttribute("class"));
        }
      }

      HtmlImageInput button6 = (HtmlImageInput) getInputIncludingId(page,
          "command6");

      if (button6 == null) {
        formatter.format("Unable to find rendered"
            + " command button (as image) containing ID " + "'command6' %n");
      } else {
        if ("Click Me".equals(button6.getValueAttribute())) {
          formatter.format(
              "Didn't expect the value attribute for "
                  + "command6 to be rendered, found '%s' %n",
              button6.getValueAttribute());
        }

        if (!"image".equals(button6.getTypeAttribute())) {
          formatter.format("Expected type attribute for command6 "
              + "to be 'image', found '%s' %n", button6.getTypeAttribute());
        }
        String expectedImgSrc = "pnglogo.png";
        if (!button6.getSrcAttribute().contains(expectedImgSrc)) {
          formatter.format(
              "Unexpected result for '%s' Attribute!%n" + "Expected: '%s'%n, "
                  + "Received: '%s'%n",
              "src", expectedImgSrc, button6.getSrcAttribute());
        }
      }

      HtmlSubmitInput button7 = (HtmlSubmitInput) getInputIncludingId(page,
          "command7");

      if (!validateExistence("command7", "input", button7, formatter)) {
        handleTestStatus(messages);
        return;
      } else {
        if (!"disabled".equals(button7.getDisabledAttribute())) {
          formatter.format(
              "(button7) Expected the disabled attribute "
                  + "to be rendered as '%s', instead found '%s' %n",
              "disabled", button7.getDisabledAttribute());
        }
      }

      HtmlSubmitInput button8 = (HtmlSubmitInput) getInputIncludingId(page,
          "command8");

      if (!validateExistence("command8", "input", button8, formatter)) {
        handleTestStatus(messages);
        return;
      } else {
        if (!HtmlElement.ATTRIBUTE_NOT_DEFINED
            .equals(button8.getDisabledAttribute())) {
          formatter.format("(command8) Expected the disabled attribute "
              + "to not be rendered when the disabled "
              + "attribute was specified as false in the JSP" + " %n.");
        }
      }

      HtmlSubmitInput button9 = (HtmlSubmitInput) getInputIncludingId(page,
          "command9");

      if (!validateExistence("command9", "input", button9, formatter)) {
        handleTestStatus(messages);
        return;
      } else {
        if (!"readonly".equals(button9.getReadOnlyAttribute())) {
          formatter.format(
              "(button9) Expected the readonly attribute "
                  + "to be rendered as '%s', instead found '%s' %n",
              "readonly", button9.getReadOnlyAttribute());
        }
      }

      HtmlSubmitInput button10 = (HtmlSubmitInput) getInputIncludingId(page,
          "command10");

      if (!validateExistence("command10", "input", button10, formatter)) {
        handleTestStatus(messages);
        return;
      } else {
        if (!HtmlElement.ATTRIBUTE_NOT_DEFINED
            .equals(button10.getDisabledAttribute())) {
          formatter.format("(button10) Expected the disabled attribute "
              + "to not be rendered when the disabled "
              + "attribute was specified as false in the JSP" + " %n.");
        }
      }

      // ------------------------------------------------------------ case
      // 11
      HtmlSubmitInput button11 = (HtmlSubmitInput) getInputIncludingId(page,
          "command11");

      if (!validateExistence("command11", "input", button11, formatter)) {
        handleTestStatus(messages);
        return;
      }

      // Test for Styleclass attribute.
      String clazzAttrOne = button11.getAttribute("class");
      if (!"blue".equals(clazzAttrOne)) {
        formatter.format(
            "Unexpected rendered value for the "
                + "\"styleClass\" attribute on the '%s' tag with the "
                + "id of '%s'. %nExpected: '%s' %nFound: '%s' %n",
            "h:commandButton", "onoff", "blue", clazzAttrOne);
      }

      // Test for Value attribute.
      String valueAttrOne = button11.getValueAttribute();
      if (!"onoff".equals(valueAttrOne)) {
        formatter.format(
            "Unexpected rendered value for the "
                + "\"title\" attribute on the '%s' tag with the "
                + "id of '%s'. %nExpected: '%s' %nFound: '%s' %n",
            "h:commandButton", "onoff", "onoff", valueAttrOne);
      }

      // Test for Title attribute.
      String titleAttrOne = button11.getAttribute("title");
      if (!"onoff".equals(titleAttrOne)) {
        formatter.format(
            "Unexpected rendered value for the "
                + "\"title\" attribute on the '%s' tag with the "
                + "id of '%s'. %nExpected: '%s' %nFound: '%s' %n",
            "h:commandButton", "onoff", "onoff", titleAttrOne);
      }

      // display test result
      handleTestStatus(messages);
    }

  } // END cbuttonRenderEncodeNonPassthroughTest

  /**
   * @testName: cbuttonRenderDecodeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Ensure that if a commandbutton is clicked, the Faces
   *                 implementation properly decodes the request and invokes the
   *                 specified actionListener.
   * 
   * @since 1.2
   */
  public void cbuttonRenderDecodeTest() throws Fault {

    StringBuilder messages = new StringBuilder(64);
    Formatter formatter = new Formatter(messages);

    List<HtmlPage> pages = new ArrayList<HtmlPage>();
    pages.add(getPage(CONTEXT_ROOT + "/faces/decodetest.jsp"));
    pages.add(getPage(CONTEXT_ROOT + "/faces/decodetest_facelet.xhtml"));

    for (HtmlPage page : pages) {
      HtmlSubmitInput button1 = (HtmlSubmitInput) getInputIncludingId(page,
          "command1");

      try {
        HtmlPage postBack = (HtmlPage) button1.click();
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
  } // END cbuttonRenderDecodeTest

  /**
   * @testName: cbuttonRenderPassthroughTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Ensure those attributes marked as passthrough are indeed
   *                 visible in the rendered markup as specified in the
   *                 JSP/Facelet page.
   * 
   * @since 1.2
   */
  public void cbuttonRenderPassthroughTest() throws Fault {

    TreeMap<String, String> control = new TreeMap<String, String>();
    control.put("accesskey", "U");
    control.put("alt", "passthrough");
    control.put("dir", "LTR");
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
    control.put("onselect", "js13");
    control.put("style", "Color: red;");
    control.put("tabindex", "0");
    control.put("title", "sample");

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
      }

      HtmlSubmitInput button1 = (HtmlSubmitInput) getInputIncludingId(page,
          "command1");

      if (button1 == null) {
        formatter.format("Unable to find rendered input button with "
            + "ID containing 'button11' %n");
        return;
      }

      validateAttributeSet(control, button1,
          new String[] { "name", "id", "value", "type", "onclick" }, formatter);

      handleTestStatus(messages);
    }

  } // END cbuttonRenderPassthroughTest

} // END URLClient
