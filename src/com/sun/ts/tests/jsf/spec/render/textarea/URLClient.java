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
package com.sun.ts.tests.jsf.spec.render.textarea;

import com.gargoylesoftware.htmlunit.html.HtmlInput;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Formatter;
import java.util.TreeMap;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;
import java.util.ArrayList;
import java.util.List;

public class URLClient extends BaseHtmlUnitClient {

  private static final String CONTEXT_ROOT = "/jsf_render_textarea_web";

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
   * @testName: textareaRenderEncodeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate the rendering of textarea fields case 1: - Only
   *                 the id and value attributes are defined. case 2: -
   *                 Attributes for id, value, and class are defined. case 4,5:
   *                 - ensure the disabled attribute is handled using html
   *                 attribute minimization (only the attribute name is rendered
   *                 when value is true, and nothing rendered when false) case
   *                 6,7: - ensure the readonly attribute is handled using html
   *                 attribute minimization (only the attribute name is rendered
   *                 when value is true, and nothing rendered when false)
   * 
   * @since 1.2
   */
  public void textareaRenderEncodeTest() throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    List<HtmlPage> pages = new ArrayList<HtmlPage>();
    pages.add(getPage(CONTEXT_ROOT + "/faces/encodetest.jsp"));
    pages.add(getPage(CONTEXT_ROOT + "/faces/encodetest_facelet.xhtml"));

    for (HtmlPage page : pages) {
      // ------------------------------------------------------- textarea1

      HtmlTextArea textarea1 = (HtmlTextArea) getElementOfTypeIncludingId(page,
          "textarea", "textarea1");

      if (!validateExistence("textarea1", "textarea", textarea1, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!"textarea value".equals(textarea1.getText())) {
        formatter.format("Expected the rendered value for the value "
            + "attribute of the textarea field with ID containing "
            + "'texttextarea1'" + " to be 'textarea value', but found '%s'%n",
            textarea1.getText());
      }

      // ------------------------------------------------------- textarea2

      HtmlTextArea textarea2 = (HtmlTextArea) getElementOfTypeIncludingId(page,
          "textarea", "textarea2");

      if (!validateExistence("textarea2", "textarea", textarea2, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!(textarea2.getText().length() > 0)) {
        formatter.format("Expected a rendered value for value attribute"
            + " of the textarea field with ID containing "
            + "'textarea2', but found none%n");
      }

      if (!"textarea".equals(textarea2.getAttribute("class"))) {
        formatter.format(
            "Expected the rendered value of the class "
                + "attribute to be 'textarea' for the textarea field "
                + "containing ID 'textarea2' %n" + "but found '%s'%n",
            textarea2.getAttribute("class"));
      }

      // ------------------------------------------------------- textarea3

      HtmlTextArea textarea3 = (HtmlTextArea) getElementOfTypeIncludingId(page,
          "textarea", "textarea3");

      if (!validateExistence("textarea3", "textarea", textarea3, formatter)) {
        handleTestStatus(messages);
        return;
      } else {
        if (!"disabled".equals(textarea3.getDisabledAttribute())) {
          formatter.format("(textarea3) Expected the disabled "
              + "attribute to be rendered as '%s'%n" + "instead found '%s'%n",
              "disabled", textarea3.getDisabledAttribute());
        }
      }

      // ------------------------------------------------------- textarea4

      HtmlTextArea textarea4 = (HtmlTextArea) getElementOfTypeIncludingId(page,
          "textarea", "textarea4");

      if (!validateExistence("textarea4", "textarea", textarea4, formatter)) {
        handleTestStatus(messages);
        return;
      } else {
        if (!HtmlElement.ATTRIBUTE_NOT_DEFINED
            .equals(textarea4.getDisabledAttribute())) {
          formatter.format("(textarea4) Expected the disabled "
              + "attribute to not be rendered when the disabled "
              + "attribute was specified as false!" + "%n.");
        }
      }

      // ------------------------------------------------------- textarea5

      HtmlTextArea textarea5 = (HtmlTextArea) getElementOfTypeIncludingId(page,
          "textarea", "textarea5");

      if (!validateExistence("textarea5", "textarea", textarea5, formatter)) {
        handleTestStatus(messages);
        return;
      } else {
        if (!"readonly".equals(textarea5.getReadOnlyAttribute())) {
          formatter.format("(textarea5) Expected the readonly "
              + "attribute to be rendered as '%s'%n " + "instead found '%s'%n",
              "readonly", textarea5.getReadOnlyAttribute());
        }
      }

      // ------------------------------------------------------- textarea6

      HtmlTextArea textarea6 = (HtmlTextArea) getElementOfTypeIncludingId(page,
          "textarea", "textarea6");

      if (!validateExistence("textarea6", "textarea", textarea6, formatter)) {
        handleTestStatus(messages);
        return;
      } else {
        if (!HtmlElement.ATTRIBUTE_NOT_DEFINED
            .equals(textarea6.getReadOnlyAttribute())) {
          formatter.format("(textarea6) Expected the readonly "
              + "attribute to not be rendered when the readonly "
              + "attribute was specified as false!" + "%n.");
        }
      }

      handleTestStatus(messages);
    }
  } // END textareaRenderEncodeTest

  /**
   * @testName: textareaRenderDecodeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Ensure the value of the textarea field is updated by
   *                 submitting the form. The value after the post back should
   *                 be 'newSubmittedValue'. This is validated by getting the
   *                 value of the component after the post-back (can't validate
   *                 that (setSubmittedValue() is called since after the
   *                 validations are processed setValue() will have been called
   *                 and the submittedValue reset to null).
   * 
   * @since 1.2
   */
  public void textareaRenderDecodeTest() throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    List<HtmlPage> pages = new ArrayList<HtmlPage>();
    pages.add(getPage(CONTEXT_ROOT + "/faces/decodetest.jsp"));
    pages.add(getPage(CONTEXT_ROOT + "/faces/decodetest_facelet.xhtml"));

    for (HtmlPage page : pages) {
      HtmlTextArea textarea1 = (HtmlTextArea) getElementOfTypeIncludingId(page,
          "textarea", "textarea1");
      if (!validateExistence("textarea1", "textarea", textarea1, formatter)) {
        handleTestStatus(messages);
        return;
      }

      textarea1.setText("newSubmittedValue");

      HtmlSubmitInput button1 = (HtmlSubmitInput) getInputIncludingId(page,
          "button1");

      try {
        page = (HtmlPage) button1.click();
      } catch (IOException e) {
        formatter.format("Unexpected exception clicking button1: " + "%s%n", e);
        handleTestStatus(messages);
        return;
      }

      HtmlInput shadow = (HtmlInput) getInputIncludingId(page, "result");
      String result = shadow.getValueAttribute();

      if (!"newSubmittedValue".equals(result)) {
        formatter.format("Unexpected submitted value for text " + "textarea1.%n"
            + "Expected : '%s'%n" + "Found: '%s'%n", "test", result);
      }

      handleTestStatus(messages);
    }
  } // END hiddenRenderDecodeTest

  /**
   * @testName: textareaRenderPassthroughTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Ensure the attributes that are considered passthrough by
   *                 the specification are rendered as is.
   * 
   * @since 1.2
   */
  public void textareaRenderPassthroughTest() throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    TreeMap<String, String> control = new TreeMap<String, String>();
    control.put("accesskey", "P");
    control.put("dir", "LTR");
    control.put("lang", "en");
    control.put("onblur", "js1");
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

      HtmlTextArea textarea1 = (HtmlTextArea) getElementOfTypeIncludingId(page,
          "textarea", "textarea1");
      if (!validateExistence("textarea1", "textarea", textarea1, formatter)) {
        handleTestStatus(messages);
        return;
      }

      validateAttributeSet(control, textarea1,
          new String[] { "id", "value", "name", "type" }, formatter);

      handleTestStatus(messages);
    }
  } // END textareaRenderPassthroughTest
} // END URLClient
