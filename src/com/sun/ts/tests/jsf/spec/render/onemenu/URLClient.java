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
package com.sun.ts.tests.jsf.spec.render.onemenu;

import java.io.PrintWriter;
import java.io.IOException;
import java.util.Formatter;
import java.util.TreeMap;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;
import java.util.ArrayList;

public class URLClient extends BaseHtmlUnitClient {

  private static final String CONTEXT_ROOT = "/jsf_render_onemenu_web";

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
   * @testName: oneMenuRenderEncodeTest
   * @assertion_ids: PENDING
   * @test_Strategy: Validate the rendering of javax.faces.SelectOne.Menu case
   *                 1: - Only the id attribute is defined and two options.
   *                 Verify that - the correct number of options are rendered -
   *                 one option is selected.(default) - the value attributes for
   *                 each option are correctly rendered. case 2: - Attributes
   *                 for id, enabledClass, and disabledClass are defined. Verify
   *                 that - each option is rendered as enabled or disabled as
   *                 appropriate, and - each option is rendered with the correct
   *                 style class. case 3: - Attributes for id, value, and class
   *                 are defined. case 4,5: - ensure the disabled attribute is
   *                 handled using html attribute minimization (only the
   *                 attribute name is rendered when value is true, and nothing
   *                 rendered when false) case 6,7: - ensure the readonly
   *                 attribute is handled using html attribute minimization
   *                 (only the attribute name is rendered when value is true,
   *                 and nothing rendered when false) case 8: - Using the
   *                 Binding Attribute make sure of the following. Verify that -
   *                 the correct number of options are rendered - one option is
   *                 selected.(default) - the value attributes for each option
   *                 are correctly rendered.
   * 
   * @since 1.2
   */
  public void oneMenuRenderEncodeTest() throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    List<HtmlPage> pages = new ArrayList<HtmlPage>();
    pages.add(getPage(CONTEXT_ROOT + "/faces/encodetest.jsp"));
    pages.add(getPage(CONTEXT_ROOT + "/faces/encodetest_facelet.xhtml"));

    for (HtmlPage page : pages) {
      // ----------------------------------------------------------- Menu1

      HtmlSelect menu1 = (HtmlSelect) getElementOfTypeIncludingId(page,
          "select", "form:menu1");

      if (!validateExistence(menu1.getId(), "select", menu1, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (menu1.getOptionSize() != 2) {
        formatter.format(
            "Expected the number of options rendered "
                + "for the component menu1 to be '%d', but found '%d'",
            2, menu1.getOptionSize());
      }

      List selectedOptions = menu1.getSelectedOptions();
      if (selectedOptions == null) {
        formatter.format("List of selected options for menu1 is null");
        handleTestStatus(messages);
        return;
      }

      if (selectedOptions.size() != 1) {
        formatter.format("Expected the number of selected options "
            + "rendered for the component menu1 to be '%d', but "
            + "found '%d'", 1, selectedOptions.size());
      }

      if (!"true".equals(menu1.getOption(0).getValueAttribute())) {
        formatter.format(
            "Expected the value of option '%d' for menu1"
                + " to be '%s', found '%s'",
            0, "true", menu1.getOption(0).getValueAttribute());
      }

      if (!"false".equals(menu1.getOption(1).getValueAttribute())) {
        formatter.format(
            "Expected the value of option '%d' for menu1"
                + " to be '%s', found '%s'",
            1, "true", menu1.getOption(1).getValueAttribute());
      }

      // ----------------------------------------------------------- Menu2

      HtmlSelect menu2 = (HtmlSelect) getElementOfTypeIncludingId(page,
          "select", "form:menu2");

      if (!validateExistence(menu2.getId(), "select", menu2, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!"disabled".equals(menu2.getOption(0).getDisabledAttribute())) {
        formatter.format("Expected option '%d' for menu2 to be '%s'", 0,
            "disabled");
      }

      if (!"Color: red;".equals(menu2.getOption(0).getAttribute("class"))) {
        formatter.format(
            "Expected the value of the class attribute "
                + "for option '%d' for menu2" + " to be '%s', found '%s'",
            0, "Color: red;", menu2.getOption(0).getAttribute("class"));
      }

      if (menu2.getOption(1).isDisabled()) {
        formatter.format("Expected option '%d' for menu2 to be '%s'", 1,
            "enabled");
      }

      if (!"text".equals(menu2.getOption(1).getAttribute("class"))) {
        formatter.format(
            "Expected the value of the class attribute "
                + "for option '%d' for menu2" + " to be '%s', found '%s'",
            1, "text", menu2.getOption(1).getAttribute("class"));
      }

      // ----------------------------------------------------------- Menu3

      HtmlSelect menu3 = (HtmlSelect) getElementOfTypeIncludingId(page,
          "select", "form:menu3");

      if (!validateExistence(menu3.getId(), "select", menu3, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!"text".equals(menu3.getAttribute("class"))) {
        formatter.format("Expected the rendered value of the class "
            + "attribute to be 'text' for the text field containing"
            + " ID 'menu3', but found '%s'%n", menu3.getAttribute("class"));
      }

      // ----------------------------------------------------------- Menu4
      HtmlSelect menu4 = (HtmlSelect) getElementOfTypeIncludingId(page,
          "select", "form:menu4");

      if (!validateExistence(menu4.getId(), "select", menu4, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!"disabled".equals(menu4.getDisabledAttribute())) {
        formatter.format(
            "(menu4) Expected the disabled attribute "
                + "to be rendered as '%s', instead found '%s'%n",
            "disabled", menu4.getDisabledAttribute());
      }

      // ----------------------------------------------------------- Menu5

      HtmlSelect menu5 = (HtmlSelect) getElementOfTypeIncludingId(page,
          "select", "form:menu5");

      if (!validateExistence(menu5.getId(), "select", menu5, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!HtmlElement.ATTRIBUTE_NOT_DEFINED
          .equals(menu5.getDisabledAttribute())) {
        formatter.format("(menu5) Expected the disabled attribute "
            + "to not be rendered when the disabled "
            + "attribute was specified as false in the JSP%n.");
      }

      // ----------------------------------------------------------- Menu6

      HtmlSelect menu6 = (HtmlSelect) getElementOfTypeIncludingId(page,
          "select", "form:menu6");

      if (!validateExistence(menu6.getId(), "select", menu6, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!"readonly".equals(menu6.getAttribute("readonly"))) {
        formatter.format(
            "(menu6) Expected the readonly attribute "
                + "to be rendered as '%s', instead found '%s'%n",
            "readonly", menu6.getAttribute("readonly"));
      }

      // ----------------------------------------------------------- Menu7

      HtmlSelect menu7 = (HtmlSelect) getElementOfTypeIncludingId(page,
          "select", "form:menu7");

      if (!validateExistence(menu7.getId(), "select", menu7, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!HtmlElement.ATTRIBUTE_NOT_DEFINED
          .equals(menu7.getAttribute("readonly"))) {
        formatter.format("(menu7) Expected the readonly attribute "
            + "to not be rendered when the readonly "
            + "attribute was specified as false in the JSP" + "%n.");
      }

      // ----------------------------------------------------------- Menu8
      HtmlSelect menu8 = (HtmlSelect) getElementOfTypeIncludingId(page,
          "select", "form:menu8");

      if (!validateExistence(menu8.getId(), "select", menu8, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (menu8.getOptionSize() != 2) {
        formatter.format("Unexpected the number of options rendered "
            + "for the component %s. %n" + "Expected: '%d' %n"
            + "Found: '%d' %n", "menu8", 2, menu8.getOptionSize());
      }

      List slo = menu8.getSelectedOptions();
      if (slo == null) {
        formatter.format("List of selected options for menu8 is null");
        handleTestStatus(messages);
        return;
      }

      if (slo.size() != 1) {
        formatter.format("Unexpected number of selected options "
            + "Expected: '%d' %n" + "Found '%d' %n ", +1,
            selectedOptions.size());
      }

      if (!"no".equals(menu8.getOption(0).getValueAttribute())) {
        formatter.format(
            "Unexpected value for option '%d' on %s " + "Expected: '%s' %n"
                + "Found: '%s' %n",
            0, "menu8", "no", menu8.getOption(0).getValueAttribute());
      }

      if (!"yes".equals(menu8.getOption(1).getValueAttribute())) {
        formatter.format(
            "Unexpected value for option '%d' on %s " + "Expected: '%s' %n"
                + "Found: '%s' %n",
            1, "menu8", "yes", menu8.getOption(1).getValueAttribute());
      }

      handleTestStatus(messages);
    }
  } // END oneMenuRenderEncodeTest

  /**
   * @testName: oneMenuRenderDecodeTest
   * @assertion_ids: PENDING
   * @test_Strategy: Create a Menu with two options. Click a button to select
   *                 one option and submit the form. Verify that the correct
   *                 option is selected. Click the button again to select the
   *                 other option. Verify that the correct option is selected
   *                 and that the original option is no longer selected.
   * 
   * @since 1.2
   */
  public void oneMenuRenderDecodeTest() throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    List<HtmlPage> pages = new ArrayList<HtmlPage>();
    pages.add(getPage(CONTEXT_ROOT + "/faces/decodetest.jsp"));
    pages.add(getPage(CONTEXT_ROOT + "/faces/decodetest_facelet.xhtml"));

    for (HtmlPage page : pages) {
      HtmlSelect menu1 = (HtmlSelect) getElementOfTypeIncludingId(page,
          "select", "form:menu1");

      if (!validateExistence(menu1.getId(), "select", menu1, formatter)) {
        handleTestStatus(messages);
        return;
      }

      List selectedOptions = menu1.getSelectedOptions();
      if (selectedOptions == null) {
        formatter.format("List of selected options for menu1 is null");
        handleTestStatus(messages);
        return;
      }

      if (selectedOptions.size() != 1) {
        formatter.format("Expected the number of selected options "
            + "rendered for the component menu1 to be '%d', but "
            + "found '%d'", 0, selectedOptions.size());
      }

      // Test for the default selected item.
      HtmlOption dselect = (HtmlOption) selectedOptions.get(0);
      if (!"red".equals(dselect.getValueAttribute())) {
        formatter.format("Expected the default selected option to "
            + "have the value of '%d', before any botton click, "
            + "but found '%d'", "true", dselect.getValueAttribute());
      }

      menu1.getOption(0).setSelected(true);
      HtmlInput button1 = (HtmlInput) getInputIncludingId(page, "button1");

      try {
        page = (HtmlPage) button1.click();
      } catch (IOException e) {
        formatter.format("Unexpected exception clicking button1: " + "%s%n", e);
        handleTestStatus(messages);
        return;
      }

      menu1 = (HtmlSelect) getElementOfTypeIncludingId(page, "select",
          "form:menu1");

      if (!validateExistence(menu1.getId(), "input", menu1, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!menu1.getOption(0).isSelected() || menu1.getOption(1).isSelected()) {
        formatter.format(
            "After first click, expected option '%d' "
                + "selected attribute to be '%s', found '%s'; expected "
                + "option '%d' selected attribute" + " to be '%s', "
                + "found  '%s'.",
            0, "true", menu1.getOption(0).isSelected(), 1, "false",
            menu1.getOption(1).isSelected());
      }

      menu1.getOption(1).setSelected(true);
      button1 = (HtmlInput) getInputIncludingId(page, "button1");

      try {
        page = (HtmlPage) button1.click();
      } catch (IOException e) {
        formatter.format("Unexpected exception clicking button1: " + "%s%n", e);
        handleTestStatus(messages);
        return;
      }

      menu1 = (HtmlSelect) getElementOfTypeIncludingId(page, "select",
          "form:menu1");

      if (!validateExistence(menu1.getId(), "input", menu1, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (menu1.getOption(0).isSelected() || !menu1.getOption(1).isSelected()) {
        formatter.format(
            "After second click, expected option '%d' "
                + "selected attribute to be '%s', found '%s'; expected "
                + "option '%d' selected attribute to be '%s', " + "found '%s'.",
            0, "false", menu1.getOption(0).isSelected(), 1, "true",
            menu1.getOption(1).isSelected());
      }

      handleTestStatus(messages);
    }
  } // END oneMenuRenderDecodeTest

  /**
   * @testName: oneMenuRenderPassthroughTest
   * @assertion_ids: PENDING
   * @test_Strategy: Ensure the attributes that are considered passthrough by
   *                 the specification are rendered as is.
   * 
   * @since 1.2
   */
  public void oneMenuRenderPassthroughTest() throws Fault {

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

      HtmlSelect menu1 = (HtmlSelect) getElementOfTypeIncludingId(page,
          "select", "menu1");
      if (!validateExistence(menu1.getId(), "select", menu1, formatter)) {
        handleTestStatus(messages);
        return;
      }

      validateAttributeSet(control, menu1,
          new String[] { "id", "value", "name", "type", "size" }, formatter);

      handleTestStatus(messages);
    }
  } // END oneMenuRenderPassthroughTest
} // END URLClient
