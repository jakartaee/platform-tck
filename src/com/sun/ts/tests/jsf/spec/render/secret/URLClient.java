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
package com.sun.ts.tests.jsf.spec.render.secret;

import com.gargoylesoftware.htmlunit.html.HtmlInput;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Formatter;
import java.util.TreeMap;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;
import java.util.ArrayList;
import java.util.List;

public class URLClient extends BaseHtmlUnitClient {

  private static final String CONTEXT_ROOT = "/jsf_render_secret_web";

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
   * @testName: secretRenderEncodeTest
   * @assertion_ids: PENDING
   * @test_Strategy: Validate the rendering of password fields (InputSecret):
   *                 case 1: - no value defined for the value attribute as the
   *                 redisplay attribute isn't defined in the JSP case 2: - the
   *                 value attribtue has a value as the redisplay attribute is
   *                 defined in the JSP with a value of true case 3: - no value
   *                 defined for the value attribute as the redisplay attribute
   *                 is defined as false - the value of the class attribute is
   *                 defined as 'class' case 4: - no value defined for the value
   *                 attribute as the redisplay attribute isn't defined in the
   *                 JSP - the autocomplete attribute is rendered with a value
   *                 of 'off' case 5: - no value defined for the value attribute
   *                 as the redisplay attribute isn't defined in the JSP - the
   *                 autocomplete attribute isn't rendered as the attribute was
   *                 defined in the JSP with a value of 'on'. case 6,7: - ensure
   *                 the disabled attribute is handled using html attribute
   *                 minimization (only the attribute name is rendered when
   *                 value is true, and nothing rendered when false) case 8,9: -
   *                 ensure the readonly attribute is handled using html
   *                 attribute minimization (only the attribute name is rendered
   *                 when value is true, and nothing rendered when false)
   * 
   * @since 1.2
   */
  public void secretRenderEncodeTest() throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    List<HtmlPage> pages = new ArrayList<HtmlPage>();
    pages.add(getPage(CONTEXT_ROOT + "/faces/encodetest.jsp"));
    pages.add(getPage(CONTEXT_ROOT + "/faces/encodetest_facelet.xhtml"));

    for (HtmlPage page : pages) {
      // --------------------------------------------------------- secret1
      HtmlPasswordInput pass1 = (HtmlPasswordInput) getInputIncludingId(page,
          "secret1");

      if (!validateExistence("secret1", "input", pass1, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (pass1.getValueAttribute().length() != 0) {
        formatter.format("Didn't expect any rendered value for "
            + "value attribute of the password field with ID "
            + "containing 'secret1', but found a rendered value " + "of '%s'%n",
            pass1.getValueAttribute());
      }

      // --------------------------------------------------------- secret2

      HtmlPasswordInput pass2 = (HtmlPasswordInput) getInputIncludingId(page,
          "secret2");

      if (!validateExistence("secret2", "input", pass2, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!"secret value".equals(pass2.getValueAttribute())) {
        formatter.format("Expected the rendered value for the "
            + "value attribute of the password field with ID "
            + "containing 'secret2' to be 'secret value', "
            + "but found '%s'%n", pass2.getValueAttribute());
      }

      // --------------------------------------------------------- secret3

      HtmlPasswordInput pass3 = (HtmlPasswordInput) getInputIncludingId(page,
          "secret3");

      if (!validateExistence("secret3", "input", pass3, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (pass3.getValueAttribute().length() != 0) {
        formatter.format("Didn't expect any rendered value for "
            + "value attribute of the password field with ID "
            + "containing 'secret3', but found a rendered " + "value of '%s'%n",
            pass3.getValueAttribute());
      }

      if (!"secret".equals(pass3.getAttribute("class"))) {
        formatter.format(
            "Expected the rendered value of the "
                + "class attribute to be 'secret' for the password "
                + "field containing ID 'secret3', but found '%s'%n",
            pass3.getAttribute("class"));
      }

      // --------------------------------------------------------- secret4

      HtmlPasswordInput pass4 = (HtmlPasswordInput) getInputIncludingId(page,
          "secret4");

      if (!validateExistence("secret4", "input", pass4, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (pass4.getValueAttribute().length() != 0) {
        formatter.format("Didn't expect any rendered value for "
            + "value attribute of the password field with ID "
            + "containing 'secret4', but found a rendered value " + "of '%s'%n",
            pass4.getValueAttribute());
      }

      if (!"off".equals(pass4.getAttribute("autocomplete"))) {
        formatter.format("Expected the rendered value for the "
            + "autocomplete attribute to be 'off', for the "
            + "password field with ID containing 'secret4', "
            + "but found '%s'%n", pass4.getAttribute("autocomplete"));
      }

      // --------------------------------------------------------- secret5

      HtmlPasswordInput pass5 = (HtmlPasswordInput) getInputIncludingId(page,
          "secret5");

      if (!validateExistence("secret5", "input", pass5, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (pass5.getValueAttribute().length() != 0) {
        formatter.format("Didn't expect any rendered value for "
            + "value attribute of the password field with ID "
            + "containing 'secret5', but found a rendered value of" + " '%s'%n",
            pass5.getValueAttribute());
      }

      if (!HtmlElement.ATTRIBUTE_NOT_DEFINED
          .equals(pass5.getAttribute("autocomplete"))) {
        formatter.format("Expected the autocomplete attribute to "
            + "not be rendered when the value was defined" + " as 'on'.%n");
      }

      // --------------------------------------------------------- secret6

      HtmlPasswordInput pass6 = (HtmlPasswordInput) getInputIncludingId(page,
          "secret6");

      if (!validateExistence("secret6", "input", pass6, formatter)) {
        handleTestStatus(messages);
        return;
      } else {
        if (!"disabled".equals(pass6.getAttribute("disabled"))) {
          formatter.format("(secret6) Expected the disabled "
              + "attribute to be rendered as '%s', instead found " + "'%s'%n",
              "disabled", pass6.getAttribute("disabled"));
        }
      }

      // --------------------------------------------------------- secret7

      HtmlPasswordInput pass7 = (HtmlPasswordInput) getInputIncludingId(page,
          "secret7");

      if (!validateExistence("secret7", "input", pass7, formatter)) {
        handleTestStatus(messages);
        return;
      } else {
        if (!HtmlElement.ATTRIBUTE_NOT_DEFINED
            .equals(pass7.getDisabledAttribute())) {
          formatter.format("(secret7) Expected the disabled "
              + "attribute to not be rendered when the disabled "
              + "attribute was specified as false." + "%n.");
        }
      }

      // --------------------------------------------------------- secret8

      HtmlPasswordInput pass8 = (HtmlPasswordInput) getInputIncludingId(page,
          "secret8");

      if (!validateExistence("secret8", "input", pass8, formatter)) {
        handleTestStatus(messages);
        return;
      } else {
        if (!"readonly".equals(pass8.getAttribute("readonly"))) {
          formatter.format("(secret8) Expected the readonly "
              + "attribute to be rendered as '%s', instead found " + "'%s'%n",
              "readonly", pass8.getAttribute("readonly"));
        }
      }

      // --------------------------------------------------------- secret9

      HtmlPasswordInput pass9 = (HtmlPasswordInput) getInputIncludingId(page,
          "secret9");

      if (!validateExistence("secret9", "input", pass9, formatter)) {
        handleTestStatus(messages);
        return;
      } else {
        if (!HtmlElement.ATTRIBUTE_NOT_DEFINED
            .equals(pass9.getDisabledAttribute())) {
          formatter.format("(secret9) Expected the disabled "
              + "attribute to not be rendered when the disabled "
              + "attribute was specified as false." + "%n.");
        }
      }

      handleTestStatus(messages);
    }
  } // END secretRenderEncodeTest

  /**
   * @testName: secretRenderDecodeTest
   * @assertion_ids: PENDING
   * @test_Strategy: Ensure the value of the password field is updated by
   *                 submitting the form. The value after the post back should
   *                 be 'newSubmittedValue'. This is validated by getting the
   *                 value of the component after the post-back (can't validate
   *                 that (setSubmittedValue() is called since after the
   *                 validations are processed setValue() will have been called
   *                 and the submittedValue reset to null).
   * 
   * @since 1.2
   */
  public void secretRenderDecodeTest() throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    List<HtmlPage> pages = new ArrayList<HtmlPage>();
    pages.add(getPage(CONTEXT_ROOT + "/faces/decodetest.jsp"));
    pages.add(getPage(CONTEXT_ROOT + "/faces/decodetest_facelet.xhtml"));

    for (HtmlPage page : pages) {
      HtmlPasswordInput secret1 = (HtmlPasswordInput) getInputIncludingId(page,
          "secret1");

      if (!validateExistence("secret1", "input", secret1, formatter)) {
        handleTestStatus(messages);
        return;
      }

      secret1.setValueAttribute("newSubmittedValue");

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
        formatter.format("Unexpected submitted value for text1.%n "
            + "Expected: '%s'%n" + "Found: '%s'%n", "test", result);
      }

      handleTestStatus(messages);
    }

  } // END hiddenRenderDecodeTest

  /**
   * @testName: secretRenderPassthroughTest
   * @assertion_ids: PENDING
   * @test_Strategy: Ensure the attributes that are considered passthrough by
   *                 the specification are rendered as is.
   * 
   * @since 1.2
   */
  public void secretRenderPassthroughTest() throws Fault {

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

      HtmlPasswordInput pass1 = (HtmlPasswordInput) getInputIncludingId(page,
          "pass1");
      if (!validateExistence("pass1", "input", pass1, formatter)) {
        handleTestStatus(messages);
        return;

      }

      validateAttributeSet(control, pass1,
          new String[] { "id", "value", "name", "type" }, formatter);

      handleTestStatus(messages);
    }
  } // END secretRenderPassthroughTest
} // END URLClient
