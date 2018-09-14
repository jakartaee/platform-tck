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
package com.sun.ts.tests.jsf.spec.render.booleancheckbox;

import java.io.PrintWriter;
import java.io.IOException;
import java.util.Formatter;
import java.util.TreeMap;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;
import java.util.ArrayList;
import java.util.List;

public class URLClient extends BaseHtmlUnitClient {

  private static final String CONTEXT_ROOT = "/jsf_render_booleancheckbox_web";

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
   * @testName: booleanCheckboxRenderEncodeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate the rendering of checkboxes (BooleanCheckbox):
   *                 case 1: - Only the id attribute is defined. Test that the
   *                 default value of the value attribute is false, causing the
   *                 checkbox to be rendered as unchecked. case 2,3,4: -
   *                 Attributes for id and value are defined. case 5: -
   *                 Attributes for id, value, and class are defined. case 6,7:
   *                 - ensure the disabled attribute is handled using html
   *                 attribute minimization (only the attribute name is rendered
   *                 when value is true, and nothing rendered when false) case
   *                 8,9: - ensure the readonly attribute is handled using html
   *                 attribute minimization (only the attribute name is rendered
   *                 when value is true, and nothing rendered when false) case
   *                 10: - Use the "binding" attribute to tie the component to a
   *                 backend bean. Verify that setting the "title", "styleClass"
   *                 and "disabled" attributes through their respective method
   *                 calls are all rendered.
   * 
   * @since 1.2
   */
  public void booleanCheckboxRenderEncodeTest() throws Fault {
    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    List<HtmlPage> pages = new ArrayList<HtmlPage>();
    pages.add(getPage(CONTEXT_ROOT + "/faces/encodetest.jsp"));
    pages.add(getPage(CONTEXT_ROOT + "/faces/encodetest_facelet.xhtml"));

    for (HtmlPage page : pages) {
      // --------------------------------------------------------------
      // case 1

      HtmlCheckBoxInput input1 = (HtmlCheckBoxInput) getInputIncludingId(page,
          "checkbox1");

      if (!validateExistence("checkbox1", "input", input1, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (input1.isChecked()) {
        formatter.format("Expected the value attribute of the component"
            + " checkbox1 to be rendered as 'unchecked', but"
            + " found 'checked'");
      }

      // --------------------------------------------------------------
      // case 2

      HtmlCheckBoxInput input2 = (HtmlCheckBoxInput) getInputIncludingId(page,
          "checkbox2");

      if (!validateExistence("checkbox2", "input", input2, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!input2.isChecked()) {
        formatter.format("Expected the value attribute of the component"
            + " checkbox2 to be rendered as 'checked', but"
            + " found 'unchecked'");
      }

      // --------------------------------------------------------------
      // case 3

      HtmlCheckBoxInput input3 = (HtmlCheckBoxInput) getInputIncludingId(page,
          "checkbox3");

      if (!validateExistence("checkbox3", "input", input3, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (input3.isChecked()) {
        formatter.format("Expected the value attribute of the component"
            + " checkbox3 to be rendered as 'unchecked', but"
            + " found 'checked'");
      }

      // --------------------------------------------------------------
      // case 4

      HtmlCheckBoxInput input4 = (HtmlCheckBoxInput) getInputIncludingId(page,
          "checkbox4");

      if (!validateExistence("checkbox4", "input", input4, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (input4.isChecked()) {
        formatter.format("Expected the value attribute of the component"
            + " checkbox4 to be rendered as 'unchecked', but"
            + " found 'checked'");
      }

      // --------------------------------------------------------------
      // case 5
      HtmlCheckBoxInput input5 = (HtmlCheckBoxInput) getInputIncludingId(page,
          "checkbox5");

      if (!validateExistence("checkbox5", "input", input5, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!"text".equals(input5.getAttribute("class"))) {
        formatter.format(
            "Expected the rendered value of the class "
                + "attribute to be 'text' for the text field containing"
                + " ID 'checkbox5', but found '%s' %n",
            input5.getAttribute("class"));
      }

      // --------------------------------------------------------------
      // case 6
      HtmlCheckBoxInput input6 = (HtmlCheckBoxInput) getInputIncludingId(page,
          "checkbox6");

      if (!validateExistence("checkbox6", "input", input6, formatter)) {
        handleTestStatus(messages);
        return;
      } else {
        if (!"disabled".equals(input6.getAttribute("disabled"))) {
          formatter.format(
              "(checkbox6) Expected the disabled attribute "
                  + "to be rendered as '%s', instead found '%s' %n",
              "disabled", input6.getAttribute("disabled"));
        }
      }

      // --------------------------------------------------------------
      // case 7

      HtmlCheckBoxInput input7 = (HtmlCheckBoxInput) getInputIncludingId(page,
          "checkbox7");

      if (!validateExistence("checkbox7", "input", input7, formatter)) {
        handleTestStatus(messages);
        return;
      } else {
        if (!HtmlElement.ATTRIBUTE_NOT_DEFINED
            .equals(input7.getDisabledAttribute())) {
          formatter.format("(checkbox7) Expected the disabled "
              + "attribute to not be rendered when the disabled "
              + "attribute was specified as false in the JSP" + " %n.");
        }
      }

      // --------------------------------------------------------------
      // case 8

      HtmlCheckBoxInput input8 = (HtmlCheckBoxInput) getInputIncludingId(page,
          "checkbox8");

      if (!validateExistence("checkbox8", "input", input8, formatter)) {
        handleTestStatus(messages);
        return;
      } else {
        if (!"readonly".equals(input8.getAttribute("readonly"))) {
          formatter.format(
              "(checkbox8) Expected the readonly attribute "
                  + "to be rendered as '%s', instead found '%s' %n",
              "readonly", input8.getAttribute("readonly"));
        }
      }

      // --------------------------------------------------------------
      // case 9

      HtmlCheckBoxInput input9 = (HtmlCheckBoxInput) getInputIncludingId(page,
          "checkbox9");

      if (!validateExistence("checkbox9", "input", input9, formatter)) {
        handleTestStatus(messages);
        return;
      } else {
        if (!HtmlElement.ATTRIBUTE_NOT_DEFINED
            .equals(input9.getReadOnlyAttribute())) {
          formatter.format("(checkbox9) Expected the readonly attribute "
              + "to not be rendered when the readonly "
              + "attribute was specified as false in the JSP" + " %n.");
        }
      }

      // --------------------------------------------------------- case 10

      HtmlCheckBoxInput input10 = (HtmlCheckBoxInput) getInputIncludingId(page,
          "yesno");

      if (!validateExistence("yesno", "input", input10, formatter)) {
        handleTestStatus(messages);
        return;
      }

      // Test for Styleclass attribute.
      String clazzAttrOne = input10.getAttribute("class");
      if (!"text".equals(clazzAttrOne)) {
        formatter.format(
            "Unexpected rendered value for the "
                + "\"styleClass\" attribute on the '%s' tag with the "
                + "id of '%s'. %nExpected: '%s' %nFound: '%s' %n",
            "h:SelectBooleanCheckbox", "yesno", "text", clazzAttrOne);
      }

      // Test for Disabled attribute.
      String disabledAttrOne = input10.getDisabledAttribute();
      if (!"disabled".equals(disabledAttrOne)) {
        formatter.format(
            "Unexpected rendered value for the "
                + "\"disabled\" attribute on the '%s' tag with the "
                + "id of '%s'. %nExpected: '%s' %nFound: '%s' %n",
            "h:SelectBooleanCheckbox", "yesno", "disabled", disabledAttrOne);
      }

      // Test for Title attribute.
      String titleAttrOne = input10.getAttribute("title");
      if (!"yes&no".equals(titleAttrOne)) {
        formatter.format(
            "Unexpected rendered value for the "
                + "\"title\" attribute on the '%s' tag with the "
                + "id of '%s'. %nExpected: '%s' %nFound: '%s' %n",
            "h:SelectBooleanCheckbox", "yesno", "yes&no", titleAttrOne);
      }
      handleTestStatus(messages);
    }
  } // END booleanCheckboxRenderEncodeTest

  /**
   * @testName: booleanCheckboxRenderDecodeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Ensure the checkbox is updated (checked) by submitting the
   *                 form. The value after the post back should be 'true'. This
   *                 is validated by getting the value of the component after
   *                 the post-back.
   * 
   * @since 1.2
   */
  public void booleanCheckboxRenderDecodeTest() throws Fault {
    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    List<HtmlPage> pages = new ArrayList<HtmlPage>();
    pages.add(getPage(CONTEXT_ROOT + "/faces/decodetest.jsp"));
    pages.add(getPage(CONTEXT_ROOT + "/faces/decodetest_facelet.xhtml"));

    for (HtmlPage page : pages) {
      HtmlCheckBoxInput checkbox1 = (HtmlCheckBoxInput) getInputIncludingId(
          page, "checkbox1");

      if (!validateExistence("checkbox1", "input", checkbox1, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (checkbox1.isChecked()) {
        formatter.format(
            "Before click, checkbox checked attribute is " + "'%s'",
            checkbox1.isChecked());
      }

      checkbox1.setChecked(true);
      HtmlInput button1 = (HtmlInput) getInputIncludingId(page, "button1");

      try {
        page = (HtmlPage) button1.click();
      } catch (IOException e) {
        formatter.format("Unexpected exception clicking button1: %s %n", e);
        handleTestStatus(messages);
        return;
      }

      checkbox1 = (HtmlCheckBoxInput) getInputIncludingId(page, "checkbox1");

      if (!validateExistence("checkbox1", "input", checkbox1, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!checkbox1.isChecked()) {
        formatter.format("After click, checkbox checked attribute is " + "'%s'",
            checkbox1.isChecked());
      }

      handleTestStatus(messages);
    }
  } // END booleanCheckboxRenderDecodeTest

  /**
   * @testName: booleanCheckboxRenderPassthroughTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Ensure the attributes that are considered passthrough by
   *                 the specification for JSP specific pages are rendered as
   *                 is.
   * 
   * @since 1.2
   */
  public void booleanCheckboxRenderPassthroughTest() throws Fault {
    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    TreeMap<String, String> control = new TreeMap<String, String>();
    control.put("accesskey", "P");
    control.put("dir", "LTR");
    control.put("lang", "en");
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

      HtmlCheckBoxInput checkbox1 = (HtmlCheckBoxInput) getInputIncludingId(
          page, "checkbox1");

      if (!validateExistence("checkbox1", "input", checkbox1, formatter)) {
        handleTestStatus(messages);
        return;
      }

      validateAttributeSet(control, checkbox1,
          new String[] { "id", "value", "name", "type" }, formatter);

      handleTestStatus(messages);

    }

  } // END booleanCheckboxRenderPassthroughTest

} // END URLClient
