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
package com.sun.ts.tests.jsf.spec.render.inputtext;

import java.io.PrintWriter;
import java.io.IOException;
import java.util.Formatter;
import java.util.TreeMap;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;
import java.util.ArrayList;
import java.util.List;

public class URLClient extends BaseHtmlUnitClient {

  private static final String CONTEXT_ROOT = "/jsf_render_inputtext_web";

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
   * @testName: inputTextRenderEncodeTest
   * @assertion_ids: PENDING
   * @test_Strategy: Validate the rendering of text fields (InputText): case 1:
   *                 - Only the id and value attributes are defined. case 2: -
   *                 Attributes for id, value, and class are defined. case 3: -
   *                 Attributes for id, value, and autocomplete are defined. -
   *                 the autocomplete attribute is rendered with a value of
   *                 'off' case 4: - Attributes for id, value, and autocomplete
   *                 are defined. - the autocomplete attribute isn't rendered as
   *                 the attribute was defined in the JSP with a value of 'on'.
   *                 case 5,6: - ensure the disabled attribute is handled using
   *                 html attribute minimization (only the attribute name is
   *                 rendered when value is true, and nothing rendered when
   *                 false) case 7,8: - ensure the readonly attribute is handled
   *                 using html attribute minimization (only the attribute name
   *                 is rendered when value is true, and nothing rendered when
   *                 false)
   * 
   * @since 1.2
   */
  public void inputTextRenderEncodeTest() throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);
    String inputName;

    List<HtmlPage> pages = new ArrayList<HtmlPage>();
    pages.add(getPage(CONTEXT_ROOT + "/faces/encodetest.jsp"));
    pages.add(getPage(CONTEXT_ROOT + "/faces/encodetest_facelet.xhtml"));

    for (HtmlPage page : pages) {
      // -------------------------------------------------------------
      // text1

      HtmlInput input1 = (HtmlInput) getInputIncludingId(page, "text1");

      if (!validateExistence("text1", "input", input1, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!"text value".equals(input1.getValueAttribute())) {
        formatter.format(
            "Expected the rendered value for the value attribute"
                + " of the text field with ID containing 'text1'"
                + " to be 'text value', but found '%s'%n",
            input1.getValueAttribute());
      }

      // -------------------------------------------------------------
      // text2

      HtmlInput input2 = (HtmlInput) getInputIncludingId(page, "text2");

      if (!validateExistence("text2", "input", input2, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!(input2.getValueAttribute().length() > 0)) {
        formatter.format("Expected a rendered value for value attribute"
            + " of the text field with ID containing "
            + "'text2', but found none%n");
      }

      if (!"text".equals(input2.getAttribute("class"))) {
        formatter.format(
            "Expected the rendered value of the class attribute"
                + " to be 'text' for the text field containing"
                + " ID 'text2', but found '%s'%n",
            input2.getAttribute("class"));
      }

      // -------------------------------------------------------------
      // text3

      HtmlInput input3 = (HtmlInput) getInputIncludingId(page, "text3");

      if (!validateExistence("text3", "input", input3, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!(input3.getValueAttribute().length() > 0)) {
        formatter.format("Expected a rendered value for value attribute"
            + " of the text field with ID containing "
            + "'text3', but found none%n");
      }

      if (!"off".equals(input3.getAttribute("autocomplete"))) {
        formatter.format(
            "Expected the rendered value for the autocomplete"
                + " attribute to be 'off', for the text field"
                + " with ID containing 'text3', but found '%s'%n",
            input3.getAttribute("autocomplete"));
      }

      // -------------------------------------------------------------
      // text4

      HtmlInput input4 = (HtmlInput) getInputIncludingId(page, "text4");

      if (!validateExistence("text4", "input", input4, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!(input4.getValueAttribute().length() > 0)) {
        formatter.format("Expected a rendered value for value attribute"
            + " of the text field with ID containing "
            + "'text4', but found none%n");
      }

      if (!HtmlElement.ATTRIBUTE_NOT_DEFINED
          .equals(input4.getAttribute("autocomplete"))) {
        formatter.format("Expected the autocomplete attribute to not be "
            + "rendered when the value in the JSP was defined" + " as 'on'.%n");
      }

      // -------------------------------------------------------------
      // text5

      HtmlInput input5 = (HtmlInput) getInputIncludingId(page, "text5");

      if (!validateExistence("text5", "input", input5, formatter)) {
        handleTestStatus(messages);
        return;
      } else {
        if (!"disabled".equals(input5.getAttribute("disabled"))) {
          formatter.format(
              "(text5) Expected the disabled attribute "
                  + "to be rendered as '%s', instead found '%s'%n",
              "disabled", input5.getAttribute("disabled"));
        }
      }

      // -------------------------------------------------------------
      // text6

      HtmlInput input6 = (HtmlInput) getInputIncludingId(page, "text6");

      if (!validateExistence("text6", "input", input6, formatter)) {
        handleTestStatus(messages);
        return;
      } else {
        if (!HtmlElement.ATTRIBUTE_NOT_DEFINED
            .equals(input6.getDisabledAttribute())) {
          formatter.format("(text6) Expected the disabled attribute "
              + "to not be rendered when the disabled "
              + "attribute was specified as false in the JSP" + "%n.");
        }
      }

      // -------------------------------------------------------------
      // text7

      HtmlInput input7 = (HtmlInput) getInputIncludingId(page, "text7");

      if (!validateExistence("text7", "input", input7, formatter)) {
        handleTestStatus(messages);
        return;
      } else {
        if (!"readonly".equals(input7.getAttribute("readonly"))) {
          formatter.format(
              "(text7) Expected the readonly attribute "
                  + "to be rendered as '%s', instead found '%s'%n",
              "readonly", input7.getAttribute("readonly"));
        }
      }

      // -------------------------------------------------------------
      // text8

      HtmlInput input8 = (HtmlInput) getInputIncludingId(page, "text8");

      if (!validateExistence("text8", "input", input8, formatter)) {
        handleTestStatus(messages);
        return;
      } else {
        if (!HtmlElement.ATTRIBUTE_NOT_DEFINED
            .equals(input8.getReadOnlyAttribute())) {
          formatter.format("(text8) Expected the readonly attribute "
              + "to not be rendered when the readonly "
              + "attribute was specified as false in the JSP" + "%n.");
        }
      }

      // -------------------------------------------------------------
      // text9
      inputName = "text9";

      HtmlInput input9 = (HtmlInput) getInputIncludingId(page, inputName);

      if (!validateExistence(inputName, "input", input9, formatter)) {
        handleTestStatus(messages);
        return;
      }

      // Test for Value attribute.
      if (!"hello".equals(input9.getValueAttribute())) {
        formatter.format("Unexpected for value attribute" + "Expected: '%s' %n"
            + "Received: '%s' %n", "hello", input9.getValueAttribute());
      }

      // test for size attribute.
      if (!"10".equals(input9.getSizeAttribute())) {
        formatter.format("Unexpected for size attribute" + "Expected: '%s' %n"
            + "Received: '%s' %n", "10", input9.getSizeAttribute());
      }

      // test for class attribute.
      if (!"text".equals(input2.getAttribute("class"))) {
        formatter.format("Unexpected for class attribute" + "Expected: '%s' %n"
            + "Received: '%s' %n", "text", input9.getAttribute("class"));
      }

      handleTestStatus(messages);
    }
  } // END inputTextRenderEncodeTest

  /**
   * @testName: inputTextRenderDecodeTest
   * @assertion_ids: PENDING
   * @test_Strategy: Ensure the value of the text field is updated by submitting
   *                 the form. The value after the post back should be
   *                 'newSubmittedValue'. This is validated by getting the value
   *                 of the component after the post-back (can't validate that
   *                 (setSubmittedValue() is called since after the validations
   *                 are processed setValue() will have been called and the
   *                 submittedValue reset to null).
   * 
   * @since 1.2
   */
  public void inputTextRenderDecodeTest() throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    List<HtmlPage> pages = new ArrayList<HtmlPage>();
    pages.add(getPage(CONTEXT_ROOT + "/faces/decodetest.jsp"));
    pages.add(getPage(CONTEXT_ROOT + "/faces/decodetest_facelet.xhtml"));

    for (HtmlPage page : pages) {
      HtmlInput text1 = (HtmlInput) getInputIncludingId(page, "text1");

      if (!validateExistence("text1", "input", text1, formatter)) {
        handleTestStatus(messages);
        return;
      }

      text1.setValueAttribute("newSubmittedValue");

      HtmlSubmitInput button1 = (HtmlSubmitInput) getInputIncludingId(page,
          "button1");

      try {
        page = (HtmlPage) button1.click();
      } catch (IOException e) {
        formatter.format("Unexpected exception clicking button1: %s%n", e);
        handleTestStatus(messages);
        return;
      }

      HtmlInput shadow = (HtmlInput) getInputIncludingId(page, "result");
      String result = shadow.getValueAttribute();

      if (!"newSubmittedValue".equals(result)) {
        formatter.format("Unexpected submitted value for text1.  Expected"
            + " 'test', but found '%s'", result);
      }

      handleTestStatus(messages);
    }
  } // END inputTextRenderDecodeTest

  /**
   * @testName: inputTextRenderPassthroughTest
   * @assertion_ids: PENDING
   * @test_Strategy: Ensure the attributes that are considered passthrough by
   *                 the specification are rendered as is.
   * 
   * @since 1.2
   */
  public void inputTextRenderPassthroughTest() throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    TreeMap<String, String> control = new TreeMap<String, String>();
    control.put("accesskey", "P");
    control.put("alt", "alt description");
    control.put("dir", "LTR");
    control.put("lang", "en");
    control.put("maxlength", "15");
    control.put("onblur", "js1");
    control.put("onchange", "js2");
    control.put("onclick", "js3");
    control.put("ondblclick", "js4");
    control.put("onfocus", "js5");
    control.put("onkeydown", "js6");
    control.put("onkeypress", "js7");
    control.put("onkeyup", "js8");
    control.put("onmousedown", "js9");
    control.put("onmousemove", "js10");
    control.put("onmouseout", "js11");
    control.put("onmouseover", "js12");
    control.put("onmouseup", "js13");
    control.put("onselect", "js14");
    control.put("size", "15");
    control.put("style", "Color: red;");
    control.put("tabindex", "0");
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

      HtmlInput input1 = (HtmlInput) getInputIncludingId(page, "input1");
      if (!validateExistence("input1", "input", input1, formatter)) {
        handleTestStatus(messages);
        return;
      }

      validateAttributeSet(control, input1,
          new String[] { "id", "value", "name", "type" }, formatter);

      handleTestStatus(messages);
    }
  } // END inputTextRenderPassthroughTest
} // END URLClient
