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
package com.sun.ts.tests.jsf.spec.render.manylistbox;

import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.TreeMap;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;
import com.sun.ts.tests.jsf.spec.render.common.SelectManyValidator;

public class URLClient extends BaseHtmlUnitClient {

  private static final String CONTEXT_ROOT = "/jsf_render_manylistbox_web";

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
   * @testName: manyListboxRenderEncodeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate the rendering of javax.faces.SelectOne.Listbox
   *                 case 1: - Only the id attribute is defined and two options.
   *                 Verify that - the correct number of options are rendered -
   *                 none of the options are selected - the value attributes for
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
   *                 and nothing rendered when false) case 8: - Ensure that the
   *                 size and multiple attrubute are render correctly. case 9:
   *                 Ensure when using the binding attribute that the following
   *                 holds true. - Only the id attribute is defined and two
   *                 options. Verify that - the correct number of options are
   *                 rendered - none of the options are selected - the value
   *                 attributes for each option are correctly rendered.
   * 
   * @since 1.2
   */
  public void manyListboxRenderEncodeTest() throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    HtmlPage page = getPage(CONTEXT_ROOT + "/faces/encodetest.jsp");

    // ------------------------------------------------------------ listbox1

    HtmlSelect listbox1 = (HtmlSelect) getElementOfTypeIncludingId(page,
        "select", "form:listbox1");

    if (!validateExistence(listbox1.getId(), "select", listbox1, formatter)) {
      handleTestStatus(messages);
      return;
    }

    if (listbox1.getOptionSize() != 2) {
      formatter.format(
          "Expected the number of options rendered for"
              + " the component listbox1 to be '%d', but found '%d'",
          2, listbox1.getOptionSize());
    }

    List<HtmlOption> selectedOptions = listbox1.getSelectedOptions();
    if (selectedOptions == null) {
      formatter.format("List of selected options for listbox1 is null");
      handleTestStatus(messages);
      return;
    }

    if (selectedOptions.size() != 0) {
      formatter.format("Expected the number of selected options "
          + "rendered for the component listbox1 to be '%d', but "
          + "found '%d'", 0, selectedOptions.size());
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

    // ------------------------------------------------------------ listbox2

    HtmlSelect listbox2 = (HtmlSelect) getElementOfTypeIncludingId(page,
        "select", "form:listbox2");

    if (!validateExistence(listbox2.getId(), "select", listbox2, formatter)) {
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
          "Expected the value of the class attribute for"
              + "option '%d' for listbox2" + " to be '%s', found '%s'",
          1, "text", listbox2.getOption(1).getAttribute("class"));
    }

    // ------------------------------------------------------------ listbox3

    HtmlSelect listbox3 = (HtmlSelect) getElementOfTypeIncludingId(page,
        "select", "form:listbox3");

    if (!validateExistence(listbox3.getId(), "select", listbox3, formatter)) {
      handleTestStatus(messages);
      return;
    }

    if (!"text".equals(listbox3.getAttribute("class"))) {
      formatter.format(
          "Expected the rendered value of the class "
              + "attribute to be 'text' for the text field containing"
              + " ID 'listbox3', but found '%s'%n",
          listbox3.getAttribute("class"));
    }

    // ------------------------------------------------------------ listbox4
    HtmlSelect listbox4 = (HtmlSelect) getElementOfTypeIncludingId(page,
        "select", "form:listbox4");

    if (!validateExistence(listbox4.getId(), "select", listbox4, formatter)) {
      handleTestStatus(messages);
      return;
    }

    if (!"disabled".equals(listbox4.getDisabledAttribute())) {
      formatter.format(
          "(listbox4) Expected the disabled attribute "
              + "to be rendered as '%s', instead found '%s'%n",
          "disabled", listbox4.getDisabledAttribute());
    }

    // ------------------------------------------------------------ listbox5

    HtmlSelect listbox5 = (HtmlSelect) getElementOfTypeIncludingId(page,
        "select", "form:listbox5");

    if (!validateExistence(listbox5.getId(), "select", listbox5, formatter)) {
      handleTestStatus(messages);
      return;
    }

    if (!HtmlElement.ATTRIBUTE_NOT_DEFINED
        .equals(listbox5.getDisabledAttribute())) {
      formatter.format("(listbox5) Expected the disabled attribute "
          + "to not be rendered when the disabled "
          + "attribute was specified as false in the JSP%n.");
    }

    // ------------------------------------------------------------ listbox6

    HtmlSelect listbox6 = (HtmlSelect) getElementOfTypeIncludingId(page,
        "select", "form:listbox6");

    if (!validateExistence(listbox6.getId(), "select", listbox6, formatter)) {
      handleTestStatus(messages);
      return;
    }

    if (!"readonly".equals(listbox6.getAttribute("readonly"))) {
      formatter.format(
          "(listbox6) Expected the readonly attribute "
              + "to be rendered as '%s', instead found '%s'%n",
          "readonly", listbox6.getAttribute("readonly"));
    }

    // ------------------------------------------------------------ listbox7

    HtmlSelect listbox7 = (HtmlSelect) getElementOfTypeIncludingId(page,
        "select", "form:listbox7");

    if (!validateExistence(listbox7.getId(), "select", listbox7, formatter)) {
      handleTestStatus(messages);
      return;
    }

    if (!HtmlElement.ATTRIBUTE_NOT_DEFINED
        .equals(listbox7.getAttribute("readonly"))) {
      formatter.format("(listbox7) Expected the readonly attribute "
          + "to not be rendered when the readonly "
          + "attribute was specified as false in the JSP" + "%n");
    }

    // ------------------------------------------------------------ listbox8

    HtmlSelect listbox8 = (HtmlSelect) getElementOfTypeIncludingId(page,
        "select", "form:listbox8");

    if (!validateExistence(listbox8.getId(), "select", listbox8, formatter)) {
      handleTestStatus(messages);
      return;
    }

    if (!"multiple".equals(listbox8.getMultipleAttribute())) {
      formatter.format(
          "Expected the rendered value of the multiple "
              + "attribute to be '%s', instead found '%s'%n",
          "mulitple", listbox8.getMultipleAttribute());
    }

    if ("".equals(listbox8.getSizeAttribute())
        || 5 != Integer.parseInt(listbox8.getSizeAttribute())) {
      formatter.format("(listbox8) Expected the size attribute "
          + "to be rendered as the value of '%s', instead found " + "'%s' %n.",
          5, listbox8.getSizeAttribute());
    }

    // ------------------------------------------------------------ listbox9

    HtmlSelect listbox9 = (HtmlSelect) getElementOfTypeIncludingId(page,
        "select", "form:listbox9");

    if (!validateExistence(listbox9.getId(), "select", listbox9, formatter)) {
      handleTestStatus(messages);
      return;
    }

    if (listbox9.getOptionSize() != 2) {
      formatter.format(
          "Unexpected number of options rendered for"
              + " the component '%s' %n." + "Expected: '%d' %n"
              + "Recieved: '%d' %n",
          listbox9.getId(), 2, listbox9.getOptionSize());
    }

    List<HtmlOption> selected = listbox9.getSelectedOptions();

    if (selected.size() != 0) {
      formatter.format("Unexpected the number of selected options "
          + "rendered for the component '%s' %n." + "Expected: '%d' %n"
          + "Received: '%d'", listbox9.getId(), 0, selectedOptions.size());
    }

    if (!"no".equals(listbox9.getOption(0).getValueAttribute())) {
      formatter.format(
          "Unexpected value for option '%d' for '%s' %n" + "Expected: '%s' '%n'"
              + "Received: '%s'",
          0, "no", listbox9.getOption(0).getValueAttribute(), listbox9.getId());
    }

    if (!"yes".equals(listbox9.getOption(1).getValueAttribute())) {
      formatter.format(
          "Unexpected value for option '%d' for '%s' %n" + "Expected: '%s' '%n'"
              + "Received: '%s'",
          0, "yes", listbox9.getOption(1).getValueAttribute(),
          listbox9.getId());
    }

    handleTestStatus(messages);
  } // END manyListboxRenderEncodeTest

  /**
   * @testName: manyListboxRenderDecodeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Create a listbox with three options. Click a button to
   *                 select one option and submit the form. Verify that the
   *                 correct option is selected. Click the button again to
   *                 select another option. Verify that the correct option is
   *                 selected and that the original option is no longer
   *                 selected. Select the first and last options in the list,
   *                 click the button. Verify teh correct options are selected.
   *                 Then select all the options and submit the form, Verify all
   *                 options are selected.
   * 
   * @since 1.2
   */
  public void manyListboxRenderDecodeTest() throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    HtmlPage page = getPage(CONTEXT_ROOT + "/faces/decodetest.jsp");

    HtmlSelect listbox1 = (HtmlSelect) getElementOfTypeIncludingId(page,
        "select", "listbox1");

    if (!validateExistence(listbox1.getId(), "select", listbox1, formatter)) {
      handleTestStatus(messages);
      return;
    }

    List<HtmlOption> selectedOptions = listbox1.getSelectedOptions();

    if (selectedOptions.size() != 0) {
      formatter.format("Expected the number of selected options "
          + "rendered for the component listbox1 to be '%d', but "
          + "found '%d'", 0, selectedOptions.size());
    }

    listbox1.getOption(0).setSelected(true);
    listbox1.getOption(1).setSelected(false);
    listbox1.getOption(2).setSelected(false);

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
        || listbox1.getOption(1).isSelected()
        || listbox1.getOption(2).isSelected()) {
      formatter.format(
          "After first click, expected option '%d' "
              + "selected attribute to be '%s', found '%s'; expected "
              + "option '%d' selected attribute to be '%s', found '%s';"
              + "expected option '%d' selected attribute to be '%s', "
              + "found '%s'.",
          0, "true", listbox1.getOption(0).isSelected(), 1, "false",
          listbox1.getOption(1).isSelected(), 2, "false",
          listbox1.getOption(2).isSelected());
    }

    listbox1.getOption(0).setSelected(false);
    listbox1.getOption(1).setSelected(true);
    listbox1.getOption(2).setSelected(false);

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
        || !listbox1.getOption(1).isSelected()
        || listbox1.getOption(2).isSelected()) {
      formatter.format(
          "After second click, expected option '%d' "
              + "selected attribute to be '%s', found '%s'; expected "
              + "option '%d' selected attribute to be '%s', found '%s'; "
              + "expected option '%d' selected attribute to be '%s', "
              + "found '%s'.",
          0, "false", listbox1.getOption(0).isSelected(), 1, "true",
          listbox1.getOption(1).isSelected(), 2, "false",
          listbox1.getOption(0).isSelected());
    }

    listbox1.getOption(0).setSelected(true);
    listbox1.getOption(1).setSelected(false);
    listbox1.getOption(2).setSelected(true);

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

    if (!listbox1.getOption(0).isSelected()
        || listbox1.getOption(1).isSelected()
        || !listbox1.getOption(2).isSelected()) {
      formatter.format(
          "After third click, expected option '%d' "
              + "selected attribute to be '%s', found '%s'; expected "
              + "option '%d' selected attribute to be '%s', found '%s';"
              + "expected option '%d' selected attribute to be '%s', "
              + "found '%s'.",
          0, "true", listbox1.getOption(0).isSelected(), 1, "false",
          listbox1.getOption(1).isSelected(), 2, "true",
          listbox1.getOption(2).isSelected());
    }

    listbox1.getOption(0).setSelected(true);
    listbox1.getOption(1).setSelected(true);
    listbox1.getOption(2).setSelected(true);

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

    if (!listbox1.getOption(0).isSelected()
        || !listbox1.getOption(1).isSelected()
        || !listbox1.getOption(2).isSelected()) {
      formatter.format(
          "After third click, expected option '%d' "
              + "selected attribute to be '%s', found '%s'; expected "
              + "option '%d' selected attribute to be '%s', found '%s';"
              + "expected option '%d' selected attribute to be '%s', "
              + "found '%s'.",
          0, "true", listbox1.getOption(0).isSelected(), 1, "true",
          listbox1.getOption(1).isSelected(), 2, "true",
          listbox1.getOption(2).isSelected());
    }

    /*
     * Validate Conversion Types (PDL == jsp)
     */
    SelectManyValidator smv = new SelectManyValidator();
    HtmlForm form2 = (HtmlForm) getElementOfTypeIncludingId(page, "form",
        "selectmany01");

    if (!smv.validate(page, formatter, form2)) {
      handleTestStatus(messages);
      return;
    }

    handleTestStatus(messages);

  } // END manyListboxRenderDecodeTest

  /**
   * @testName: manyListboxSelectMany01Test
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: If modelType is a concrete class, let
   *                 targetForConvertedValues be a new instance of that class.
   *                 Otherwise, the concrete type for targetForConvertedValues
   *                 is taken from the following table. All classes are in the
   *                 java.util package. All collections must be created with an
   *                 initial capacity equal to the length of the values array
   *                 from the request.
   * 
   *                 modelType targetForConvertedValues
   *                 -------------------------------------------- SortedSet
   *                 TreeSet Queue LinkedList Set HashSet anything else
   *                 ArrayList
   * 
   * @since 2.0
   */
  public void manyListboxSelectMany01Test() throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    HtmlPage page = getPage(CONTEXT_ROOT + "/faces/selectmany01.xhtml");
    SelectManyValidator smv = new SelectManyValidator();

    HtmlForm form = (HtmlForm) getElementOfTypeIncludingId(page, "form",
        "selectmany01");

    if (!smv.validate(page, formatter, form)) {
      handleTestStatus(messages);
      return;
    }

    handleTestStatus(messages);

  } // END manyListboxSelectMany01Test

  /**
   * @testName: manyListboxRenderPassthroughTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Ensure the attributes that are considered passthrough by
   *                 the specification are rendered as is.
   * 
   * @since 1.2
   */
  public void manyListboxRenderPassthroughTest() throws Fault {

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
          new String[] { "id", "value", "name", "type", "size", "multiple" },
          formatter);

      handleTestStatus(messages);
    }

  } // END oneListboxRenderPassthroughTest
} // END URLClient
