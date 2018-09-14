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
package com.sun.ts.tests.jsf.spec.render.form;

import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.TreeMap;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;

public class URLClient extends BaseHtmlUnitClient {

  private static final String CONTEXT_ROOT = "/jsf_render_form_web";

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
   * @testName: formRenderEncodeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate the resulting markup of the form renderer: - Form1
   *                 - method attribute is 'post' - enctype attribute is
   *                 'application/x-www-form-urlencoded' - action attribute is
   *                 as expected based off calling
   *                 ExternalContext.encodeResourceURL(
   *                 ViewHandler.getResourceURL(viewId) - no class attribute is
   *                 rendered since styleClass isn't specified in the JSP - the
   *                 name and id attributes have the same value - Form2 - The
   *                 same as form1 except in the case of the class attribute.
   *                 styleClass is specified for this form in the JSP, so ensure
   *                 the rendered class attribute has the expected value - Form3
   *                 - This form is using the "binding" attribute to tie into a
   *                 backing bean. Validate that the following attribute(s) are
   *                 rendered correctly. - styleClass
   * 
   * @since 1.2
   */
  public void formRenderEncodeTest() throws Fault {

    final String methodValue = "post";
    final String encTypeValue = "application/x-www-form-urlencoded";

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    HtmlPage page = getPage(CONTEXT_ROOT + "/faces/encodetest.jsp");

    HtmlForm form1 = (HtmlForm) getElementOfTypeIncludingId(page, "form",
        "form1");

    if (!validateExistence("form1", "form", form1, formatter)) {
      handleTestStatus(messages);
      return;
    }

    // validate the method attribute
    if (!methodValue.equals(form1.getMethodAttribute())) {
      formatter.format(
          "Expected the method attribute to have a value "
              + "of '%s' for the form containing ID 'form1'.  "
              + "Value found was '%s'%n",
          methodValue, form1.getMethodAttribute());
    }

    // enctype is not specified, ensure it is rendered
    // with the default value.
    if (!encTypeValue.equals(form1.getEnctypeAttribute())) {
      formatter.format(
          "Expected the enctype attribute to have a value"
              + " of '%s' for the form containing ID 'form1'.  "
              + "Value found was '%s'%n",
          encTypeValue, form1.getEnctypeAttribute());
    }

    //
    // Commented out due to Bug ID:6485582
    //
    // // validate the action attribute value is expected by getting
    // // the value of expectedActionUrl response header
    // String expectedActionUrl =
    // page.getWebResponse().
    // getResponseHeaderValue("expectedActionUrl");
    //
    // if (expectedActionUrl == null || expectedActionUrl.length() == 0) {
    // formatter.format("Unable to find the 'expectedActionUrl' " +
    // "response header.%n");
    // handleTestStatus(messages);
    // return;
    // }
    //
    // if (!expectedActionUrl.equals(form1.getActionAttribute())) {
    // formatter.format("Expected the action attribute to have a value" +
    // " of '%s' for form containing ID 'form1'. " +
    // "Value found was '%s'%n",
    // expectedActionUrl,
    // form1.getActionAttribute());
    // }

    // no class attribute should have been rendered
    if (!HtmlElement.ATTRIBUTE_NOT_DEFINED
        .equals(form1.getAttribute("class"))) {
      formatter.format("Unexpected class attribute found in the rendered"
          + " form containing ID 'form1'.  styleClass was not"
          + " specified in the JSP page. %n");
    }

    // ensure the name and id attribute values are equal
    if (!form1.getId().equals(form1.getNameAttribute())) {
      formatter.format(
          "Expected the rendered values for the 'id' and"
              + " 'name' attributes to be equal.  The expected "
              + " value for the name attribute was '%s', but found" + " '%s'%n",
          form1.getId(), form1.getNameAttribute());
    }

    // -------------------------------------------------------------- Form 2

    // validate the styleClass attribute, when specified, results in
    // the class attribute being rendered within the form element
    final String expectedClass = "fancy";
    HtmlForm form2 = (HtmlForm) getElementOfTypeIncludingId(page, "form",
        "form2");

    if (!validateExistence("form2", "form", form2, formatter)) {
      handleTestStatus(messages);
      return;
    }

    // validate the method attribute
    if (!methodValue.equals(form2.getMethodAttribute())) {
      formatter.format(
          "Expected the method attribute to have a value "
              + "of '%s' for the form containing ID 'form2'.  "
              + "Value found was '%s'%n",
          methodValue, form2.getMethodAttribute());
    }

    // enctype is not specified, ensure it is rendered
    // with the default value.
    if (!encTypeValue.equals(form2.getEnctypeAttribute())) {
      formatter.format(
          "Expected the enctype attribute to have a value"
              + " of '%s' for the form containing ID 'form2'.  "
              + "Value found was '%s'%n",
          encTypeValue, form2.getEnctypeAttribute());
    }
    //
    // Commented out due to Bug ID:6485582
    //
    // if (!expectedActionUrl.equals(form2.getActionAttribute())) {
    // formatter.format("Expected the action attribute to have a value" +
    // " of '%s' for form containing ID 'form2'. " +
    // "Value found was '%s'%n",
    // expectedActionUrl,
    // form2.getActionAttribute());
    // }

    // ensure the name and id attribute values are equal
    if (!form2.getId().equals(form2.getNameAttribute())) {
      formatter.format(
          "Expected the rendered values for the 'id' and"
              + " 'name' attributes to be equal.  The expected "
              + " value for the name attribute was '%s', but found" + " '%s'%n",
          form2.getId(), form2.getNameAttribute());
    }

    if (!expectedClass.equals(form2.getAttribute("class"))) {
      formatter.format(
          "Unexpected value for class attribute in form"
              + " containing ID 'form2'.  Expected '%s', but " + "found '%s'%n",
          expectedClass, form2.getAttribute("class"));
    }
    // -------------------------------------------------------------- Form 3

    HtmlForm form3 = (HtmlForm) getElementOfTypeIncludingId(page, "form",
        "form3");

    if (!validateExistence("form3", "form", form3, formatter)) {
      handleTestStatus(messages);
      return;
    }

    // Validate the class attribute.
    if (!expectedClass.equals(form3.getAttribute("class"))) {
      formatter
          .format(
              "Unexpected value for the class Attribute!%n" + "Expected: '%s'%n"
                  + "Received: '%s'%n",
              expectedClass, form3.getAttribute("class"));
    }

    handleTestStatus(messages);

  } // END formRenderEncodeTest

  /**
   * @testName: formRenderDecodeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Ensure that decode process correctly identifies the
   *                 submitted form and calls UIForm.setSubmitted(true). this
   *                 test will validate the proper action occurs when submitting
   *                 different forms within the same page.
   * 
   * @since 1.2
   */
  public void formRenderDecodeTest() throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    HtmlPage page = getPage(CONTEXT_ROOT + "/faces/decodetest.jsp");

    HtmlInput formOne = (HtmlInput) getElementOfTypeIncludingId(page, "input",
        "form1:id1");
    HtmlInput formTwo = (HtmlInput) getElementOfTypeIncludingId(page, "input",
        "form2:id2");

    HtmlSubmitInput submit1 = (HtmlSubmitInput) getInputIncludingId(page,
        "button1");
    HtmlSubmitInput submit2 = (HtmlSubmitInput) getInputIncludingId(page,
        "button2");

    HtmlInput StatusOne;
    HtmlInput StatusTwo;
    String result1;
    String result2;

    // Submit form1
    try {
      formOne.setValueAttribute("CLICKED");
      page = (HtmlPage) submit1.click();
    } catch (IOException e) {
      formatter.format("Unexpected exception clicking button1: %s%n", e);
      handleTestStatus(messages);
      return;
    }

    StatusOne = (HtmlInput) getElementOfTypeIncludingId(page, "input",
        "result1");
    StatusTwo = (HtmlInput) getElementOfTypeIncludingId(page, "input",
        "result2");

    // Test results to make sure that form1 submitted and form2 was not.
    result1 = StatusOne.getValueAttribute();
    result2 = StatusTwo.getValueAttribute();
    if (!("CLICKED".equals(result1) && "".equals(result2))) {
      formatter.format("Unexpected value for Form One's status! %n"
          + "Expected: CLICKED %n" + "Received: " + result1 + "%n");
      handleTestStatus(messages);
      return;
    }

    // Submit form2
    try {
      formTwo.setValueAttribute("CLICKED");
      page = (HtmlPage) submit2.click();
    } catch (IOException e) {
      formatter.format("Unexpected exception clicking button2: %s%n", e);
      handleTestStatus(messages);
      return;
    }

    StatusOne = (HtmlInput) getElementOfTypeIncludingId(page, "input",
        "result1");
    StatusTwo = (HtmlInput) getElementOfTypeIncludingId(page, "input",
        "result2");

    // Test results to make sure that form1 submitted and form2 was not.
    result1 = StatusOne.getValueAttribute();
    result2 = StatusTwo.getValueAttribute();
    if (!("".equals(result1) && "CLICKED".equals(result2))) {
      formatter.format("Unexpected value for Form Two's status! %n"
          + "Expected: CLICKED %n" + "Received: " + result2 + "%n");
      handleTestStatus(messages);
      return;
    }

    handleTestStatus(messages);

  } // END formRenderDecodeTest

  /**
   * @testName: formRenderPassthroughTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: test all passthrough attributes....
   * 
   * @since 1.2
   */
  public void formRenderPassthroughTest() throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    TreeMap<String, String> control = new TreeMap<String, String>();
    control.put("accept", "text/html");
    control.put("accept-charset", "ISO-8859-1");
    control.put("dir", "LTR");
    control.put("enctype", "noneDefault");
    control.put("lang", "en");
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
    control.put("onreset", "js11");
    control.put("onsubmit", "js12");
    control.put("style", "Color: red;");
    control.put("target", "frame1");
    control.put("title", "FormTitle");

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

      HtmlForm data1 = (HtmlForm) getElementOfTypeIncludingId(page, "form",
          "form1");
      if (!validateExistence("data1", "table", data1, formatter)) {
        handleTestStatus(messages);
        return;
      }

      validateAttributeSet(control, data1,
          new String[] { "id", "name", "action", "method" }, formatter);

      handleTestStatus(messages);
    }

  } // END formRenderPassthroughTest
} // END URLClient
