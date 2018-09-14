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
package com.sun.ts.tests.jsf.spec.render.onelistbox;

import java.io.PrintWriter;
import java.io.IOException;
import java.util.Formatter;
import java.util.TreeMap;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;
import java.util.ArrayList;

public class URLClient extends BaseHtmlUnitClient {

  private static final String CONTEXT_ROOT = "/jsf_render_onelistbox_web";

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
   * @testName: oneListboxRenderEncodeTest
   * @assertion_ids: PENDING
   * @test_Strategy: Validate the rendering of javax.faces.SelectOne.Listbox
   *                 case 1: - Only the id attribute is defined and two options.
   *                 Verify that - the correct number of options are rendered -
   *                 none of the options is selected - the value attributes for
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
   *                 and nothing rendered when false) case 8: - ensure that the
   *                 size of the listbox is maintained, even if there are less
   *                 selectItems then the size attribute states. case 9: -
   *                 ensure that the binding attribute is handled correctly and
   *                 the correct selectItems are rendered.
   * 
   * @since 1.2
   */
  public void oneListboxRenderEncodeTest() throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    List<HtmlPage> pages = new ArrayList<HtmlPage>();
    pages.add(getPage(CONTEXT_ROOT + "/faces/encodetest.jsp"));
    pages.add(getPage(CONTEXT_ROOT + "/faces/encodetest_facelet.xhtml"));

    for (HtmlPage page : pages) {
      // -------------------------------------------------------------
      // listbox1

      HtmlSelect listbox1 = (HtmlSelect) getElementOfTypeIncludingId(page,
          "select", "listbox1");

      if (!validateExistence("listbox1", "select", listbox1, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (listbox1.getOptionSize() != 2) {
        formatter.format(
            "Expected the number of options rendered for the component"
                + " listbox1 to be '%d', but found '%d'",
            2, listbox1.getOptionSize());
      }

      List selectedOptions = listbox1.getSelectedOptions();
      if (selectedOptions == null) {
        formatter.format("List of selected options for listbox1 is null");
        handleTestStatus(messages);
        return;
      }

      if (selectedOptions.size() != 0) {
        formatter.format(
            "Expected the number of selected options rendered"
                + " for the component listbox1 to be '%d', but found '%d'",
            0, selectedOptions.size());
      }

      if (!"2".equals(listbox1.getSizeAttribute())) {
        formatter.format(
            "(listbox1) Expected the value of the size "
                + "attribute to be '2', but found '%s'",
            listbox1.getSizeAttribute());
      }

      if (!"true".equals(listbox1.getOption(0).getValueAttribute())) {
        formatter.format(
            "Expected the value of option '%d' for listbox1"
                + " to be '%s', found '%s'",
            0, "true", listbox1.getOption(0).getValueAttribute());
      }

      if (!"false".equals(listbox1.getOption(1).getValueAttribute())) {
        formatter.format(
            "Expected the value of option '%d' for listbox1"
                + " to be '%s', found '%s'",
            1, "true", listbox1.getOption(1).getValueAttribute());
      }

      // -------------------------------------------------------------
      // listbox2

      HtmlSelect listbox2 = (HtmlSelect) getElementOfTypeIncludingId(page,
          "select", "listbox2");

      if (!validateExistence("listbox2", "select", listbox2, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!"disabled".equals(listbox2.getOption(0).getDisabledAttribute())) {
        formatter.format("Expected option '%d' for listbox2 to be '%s'", 0,
            "disabled");
      }

      if (!"Color: red;".equals(listbox2.getOption(0).getAttribute("class"))) {
        formatter.format(
            "Expected the value of the class attribute for"
                + "option '%d' for listbox2" + " to be '%s', found '%s'",
            0, "Color: red;", listbox2.getOption(0).getAttribute("class"));
      }

      if (listbox2.getOption(1).isDisabled()) {
        formatter.format("Expected option '%d' for listbox2 to be '%s'", 1,
            "enabled");
      }

      if (!"text".equals(listbox2.getOption(1).getAttribute("class"))) {
        formatter.format(
            "Unexpected value for class attribute on %s %n"
                + "Expected: '%s' %n" + "Found: '%s' %n",
            "listbox2", 1, "text", listbox2.getOption(1).getAttribute("class"));
      }

      // ------------------------------------------------------------
      // listbox3

      HtmlSelect listbox3 = (HtmlSelect) getElementOfTypeIncludingId(page,
          "select", "listbox3");

      if (!validateExistence("listbox3", "select", listbox3, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!"text".equals(listbox3.getAttribute("class"))) {
        formatter.format(
            "unexpected value for class attribute on %s %n"
                + "Expected: '%s' %n" + "Found: '%s' %n",
            "listbox3", "text", listbox3.getAttribute("class"));
      }

      // ------------------------------------------------------------
      // listbox4
      HtmlSelect select4 = (HtmlSelect) getElementOfTypeIncludingId(page,
          "select", "listbox4");

      if (!validateExistence("listbox4", "select", select4, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!"disabled".equals(select4.getDisabledAttribute())) {
        formatter.format(
            "Unexpected value for disabled attribute on %s %n"
                + "Expected: '%s' %n" + "Found '%s' %n",
            "listbox4", "disabled", select4.getDisabledAttribute());
      }

      // ------------------------------------------------------------
      // listbox5

      HtmlSelect select5 = (HtmlSelect) getElementOfTypeIncludingId(page,
          "select", "listbox5");

      if (!validateExistence("listbox5", "select", select5, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!HtmlElement.ATTRIBUTE_NOT_DEFINED
          .equals(select5.getDisabledAttribute())) {
        formatter.format("(listbox5) Expected the disabled attribute "
            + "to not be rendered when the disabled "
            + "attribute was specified as false in the JSP %n.");
      }

      // ------------------------------------------------------------
      // listbox6

      HtmlSelect select6 = (HtmlSelect) getElementOfTypeIncludingId(page,
          "select", "listbox6");

      if (!validateExistence("listbox6", "select", select6, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!"readonly".equals(select6.getAttribute("readonly"))) {
        formatter.format(
            "Unexpected value for readonly attribute on %s %n "
                + "Expected: '%s' %n" + "Found: '%s' %n",
            "listbox6", "readonly", select6.getAttribute("readonly"));
      }

      // ------------------------------------------------------------
      // listbox7

      HtmlSelect select7 = (HtmlSelect) getElementOfTypeIncludingId(page,
          "select", "listbox7");

      if (!validateExistence("listbox7", "select", select7, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!HtmlElement.ATTRIBUTE_NOT_DEFINED
          .equals(select7.getAttribute("readonly"))) {
        formatter.format("(listbox7) Expected the readonly attribute "
            + "to not be rendered when the readonly "
            + "attribute was specified as false in the JSP" + "%n.");
      }

      // ------------------------------------------------------------
      // listbox8

      HtmlSelect select8 = (HtmlSelect) getElementOfTypeIncludingId(page,
          "select", "listbox8");

      if (!validateExistence("listbox8", "select", select8, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!"5".equals(select8.getSizeAttribute())) {
        formatter.format(
            "Unexpected the value for size attribute on %s %n"
                + "Expected: '%s' %n" + "Found: '%s' %n",
            "listbox8", "5", select8.getSizeAttribute());
      }

      // ------------------------------------------------------------
      // listbox9

      HtmlSelect select9 = (HtmlSelect) getElementOfTypeIncludingId(page,
          "select", "listbox9");

      if (!validateExistence("listbox9", "select", select9, formatter)) {
        handleTestStatus(messages);
        return;
      }
      if (select9.getOptionSize() != 2) {
        formatter.format("Unexpected number of options rendered for "
            + "the component %s ! %n" + "Expected: '%d' %n" + "Found: '%d' %n",
            "listbox9", 2, select9.getOptionSize());
      }

      List noneSelected = select9.getSelectedOptions();
      if (noneSelected == null) {
        formatter.format("List of selected options for listbox9 is null");
        handleTestStatus(messages);
        return;
      }

      if (noneSelected.size() != 0) {
        formatter.format(
            "Unexpected number of selected items for the " + "%s! %n"
                + "Expected: '%d' %n" + "Found: '%d' %n",
            "listbox9", 0, noneSelected.size());
      }

      if (!"no".equals(select9.getOption(0).getValueAttribute())) {
        formatter.format(
            "Expected the value of option '%d' for %s"
                + " to be '%s', found '%s'",
            0, "listbox9", "yes", select9.getOption(0).getValueAttribute());
      }

      if (!"yes".equals(select9.getOption(1).getValueAttribute())) {
        formatter.format(
            "Expected the value of option '%d' for %s "
                + " to be '%s', found '%s'",
            1, "listbox9", "no", select9.getOption(1).getValueAttribute());
      }

      handleTestStatus(messages);
    }
  } // END oneListboxRenderEncodeTest

  /**
   * @testName: oneListboxRenderDecodeTest
   * @assertion_ids: PENDING
   * @test_Strategy: Create a listbox with two options. Click a button to select
   *                 one option and submit the form. Verify that the correct
   *                 option is selected. Click the button again to select the
   *                 other option. Verify that the correct option is selected
   *                 and that the original option is no longer selected.
   * 
   * @since 1.2
   */
  public void oneListboxRenderDecodeTest() throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    HtmlPage page = getPage(CONTEXT_ROOT + "/faces/decodetest.jsp");

    HtmlSelect listbox1 = (HtmlSelect) getElementOfTypeIncludingId(page,
        "select", "listbox1");

    if (!validateExistence("listbox1", "select", listbox1, formatter)) {
      handleTestStatus(messages);
      return;
    }

    List selectedOptions = listbox1.getSelectedOptions();
    if (selectedOptions == null) {
      formatter.format("List of selected options for listbox1 is null");
      handleTestStatus(messages);
      return;
    }

    if (selectedOptions.size() != 0) {
      formatter.format(
          "Expected the number of selected options rendered"
              + " for the component listbox1 to be '%d', but found '%d'",
          0, selectedOptions.size());
    }

    listbox1.getOption(0).setSelected(true);
    HtmlInput button1 = (HtmlInput) getInputIncludingId(page, "button1");

    try {
      page = (HtmlPage) button1.click();
    } catch (IOException e) {
      formatter.format("Unexpected exception clicking button1: %s%n", e);
      handleTestStatus(messages);
      return;
    }

    listbox1 = (HtmlSelect) getElementOfTypeIncludingId(page, "select",
        "listbox1");

    if (!validateExistence("listbox1", "input", listbox1, formatter)) {
      handleTestStatus(messages);
      return;
    }

    if (!listbox1.getOption(0).isSelected()
        && listbox1.getOption(1).isSelected()) {
      formatter.format(
          "After first click, expected option '%d' selected attribute"
              + " to be '%s', found '%s'; expected option '%d' selected attribute"
              + " to be '%s', found '%s'.",
          0, "true", listbox1.getOption(0).isSelected(), 1, "false",
          listbox1.getOption(1).isSelected());
    }

    listbox1.getOption(1).setSelected(true);
    button1 = (HtmlInput) getInputIncludingId(page, "button1");

    try {
      page = (HtmlPage) button1.click();
    } catch (IOException e) {
      formatter.format("Unexpected exception clicking button1: %s%n", e);
      handleTestStatus(messages);
      return;
    }

    listbox1 = (HtmlSelect) getElementOfTypeIncludingId(page, "select",
        "listbox1");

    if (!validateExistence("listbox1", "input", listbox1, formatter)) {
      handleTestStatus(messages);
      return;
    }

    if (listbox1.getOption(0).isSelected()
        && !listbox1.getOption(1).isSelected()) {
      formatter.format(
          "After second click, expected option '%d' selected attribute"
              + " to be '%s', found '%s'; expected option '%d' selected attribute"
              + " to be '%s', found '%s'.",
          0, "false", listbox1.getOption(0).isSelected(), 1, "true",
          listbox1.getOption(1).isSelected());
    }

    handleTestStatus(messages);

  } // END oneListboxRenderDecodeTest

  /**
   * @testName: oneListboxRenderPassthroughTest
   * @assertion_ids: PENDING
   * @test_Strategy: Ensure the attributes that are considered passthrough by
   *                 the specification are rendered as is.
   * 
   * @since 1.2
   */
  public void oneListboxRenderPassthroughTest() throws Fault {

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

      HtmlSelect listbox1 = (HtmlSelect) getElementOfTypeIncludingId(page,
          "select", "listbox1");
      if (!validateExistence("listbox1", "select", listbox1, formatter)) {
        handleTestStatus(messages);
        return;
      }

      validateAttributeSet(control, listbox1,
          new String[] { "id", "value", "name", "type", "size" }, formatter);

      handleTestStatus(messages);
    }
  } // END oneListboxRenderPassthroughTest
} // END URLClient
