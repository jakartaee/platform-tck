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
package com.sun.ts.tests.jsf.spec.render.manycheckbox;

import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.TreeMap;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlLabel;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;
import com.sun.ts.tests.jsf.spec.render.common.SelectManyValidator;

public class URLClient extends BaseHtmlUnitClient {

  private static final String CONTEXT_ROOT = "/jsf_render_manycheckbox_web";

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
   * @testName: manycheckboxRenderEncodeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate the rendering of javax.faces.SelectMany.Checkbox
   *                 case 1: - Only the id attribute is defined and two
   *                 checkboxes. Verify that - the correct number of checkboxes
   *                 are rendered - none of the checkboxes are checked - the
   *                 value attributes for each option are correctly rendered.
   *                 case 2: - Attributes for id, enabledClass, and
   *                 disabledClass are defined. Verify that - each option is
   *                 rendered as enabled or disabled as appropriate, and - each
   *                 option is rendered with the correct style class. case 3: -
   *                 Attributes for id, value, and class are defined. case 4,5:
   *                 - ensure the disabled attribute is handled using html
   *                 attribute minimization (only the attribute name is rendered
   *                 when value is true, and nothing rendered when false) case
   *                 6,7: - ensure the readonly attribute is handled using html
   *                 attribute minimization (only the attribute name is rendered
   *                 when value is true, and nothing rendered when false) case
   *                 8,9,10: - Ensure that the layout attribute is handled
   *                 correctly. When not specified make sure the checkboxes are
   *                 going horizontally. Then test for the two expected values.
   *                 pageDirection(vertically) or lineDirection(horizontally).
   *                 case 11: - Ensure that the border attribute is rendered
   *                 correctly in the checkboxes enclosing "
   *                 <Table>
   *                 " element. case 12: - Unsure that the following is Render
   *                 correctly when using the binding attribute. - the correct
   *                 number of checkboxes are rendered - none of the checkboxes
   *                 are checked - the value attributes for each option are
   *                 correctly rendered.
   * 
   * @since 1.2
   */
  public void manycheckboxRenderEncodeTest() throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    HtmlPage page = getPage(CONTEXT_ROOT + "/faces/encodetest.jsp");

    // ----------------------------------------------------------- checkbox1

    HtmlInput checkbox10 = (HtmlInput) getElementOfTypeIncludingId(page,
        "input", "form:checkbox1:0");

    HtmlInput checkbox11 = (HtmlInput) getElementOfTypeIncludingId(page,
        "input", "form:checkbox1:1");

    // Validate checkbox10 & checkbox11
    if (!validateExistence(checkbox10.getId(), "input", checkbox10, formatter)
        && !validateExistence(checkbox11.getId(), "input", checkbox11,
            formatter)) {
      handleTestStatus(messages);
      return;
    }

    // Make sure no checkbox buttons are checked
    String inputedOptions[] = { checkbox10.getCheckedAttribute(),
        checkbox11.getCheckedAttribute() };

    for (int ii = 0; ii < inputedOptions.length; ii++) {
      if (inputedOptions[ii].equals("checked")) {
        String failedbutton = "checkbox" + ii;
        formatter
            .format("checkbox button should not be checked " + failedbutton);
        handleTestStatus(messages);
        return;
      }
    }

    // Test value attribute for checkbox10
    if (!"true".equals(checkbox10.getValueAttribute())) {
      formatter.format(
          "Expected the value attribute of checkbox '%s', "
              + "to be '%s', but instead found '%s'",
          checkbox10.getId(), "true", checkbox10.getValueAttribute());
    }

    // Test value attribute for checkbox11
    if (!"false".equals(checkbox11.getValueAttribute())) {
      formatter.format(
          "Expected the value attribute of checkbox '%s' "
              + "to be '%s', but instead found '%s'",
          checkbox11.getId(), "true", checkbox11.getValueAttribute());
    }

    // ----------------------------------------------------------- checkbox2

    HtmlInput checkbox20 = (HtmlInput) getElementOfTypeIncludingId(page,
        "input", "form:checkbox2:0");

    HtmlInput checkbox21 = (HtmlInput) getElementOfTypeIncludingId(page,
        "input", "form:checkbox2:1");

    if (!validateExistence(checkbox20.getId(), "input", checkbox20, formatter)
        && !validateExistence(checkbox21.getId(), "input", checkbox21,
            formatter)) {
      handleTestStatus(messages);
      return;
    }

    // Get the labels for checkboxes 2:0 & 2:1 for testing class att.
    HtmlLabel label20 = (HtmlLabel) getLabelIncludingFor(page, "label",
        checkbox20.getId());

    HtmlLabel label21 = (HtmlLabel) getLabelIncludingFor(page, "label",
        checkbox21.getId());

    if (!checkbox20.isDisabled()) {
      formatter.format("Expected checkbox '%s', to be '%s'", checkbox20.getId(),
          "disabled");
    }

    if (!"Color: red;".equals(label20.getAttribute("class"))) {
      formatter.format(
          "Expected the value of the class attribute for"
              + "'%s, to be '%s', but instead found '%s'",
          checkbox20.getId(), "Color: red;", checkbox20.getAttribute("class"));
    }

    if (checkbox21.isDisabled()) {
      formatter.format("Expected checkbox '%s', to be '%s'", checkbox21.getId(),
          "enabled");
    }

    if (!"text".equals(label21.getAttribute("class"))) {
      formatter.format(
          "Expected the value of the class attribute for"
              + " checkbox '%s', to be '%s', but instead found '%s'",
          checkbox21.getId(), "text", checkbox21.getAttribute("class"));
    }

    // ----------------------------------------------------------- checkbox3

    HtmlInput checkbox30 = (HtmlInput) getElementOfTypeIncludingId(page,
        "input", "form:checkbox3:0");

    // Get the parent table element, it holds the classstyle attibute.
    HtmlTable parent30 = (HtmlTable) checkbox30.getEnclosingElement("table");

    if (!validateExistence(checkbox30.getId(), "input", checkbox30,
        formatter)) {
      handleTestStatus(messages);
      return;
    }

    if (!"text".equals(parent30.getAttribute("class"))) {
      formatter.format(
          "Expected the rendered value of the class "
              + "attribute to be '%s', for the enclosing table element of "
              + " checkbox '%s', but instead found '%s'.%n",
          "text", checkbox30.getId(), parent30.getAttribute("class"));
    }

    // ----------------------------------------------------------- checkbox4
    HtmlInput checkbox40 = (HtmlInput) getElementOfTypeIncludingId(page,
        "input", "form:checkbox4:0");

    if (!validateExistence(checkbox40.getId(), "input", checkbox40,
        formatter)) {
      handleTestStatus(messages);
      return;
    }

    if (!"disabled".equals(checkbox40.getDisabledAttribute())) {
      formatter.format(
          "(checkbox40) Expected the disabled attribute "
              + "for checkbox '%s', to be rendered as '%s', instead "
              + "found '%s'.%n",
          checkbox40.getId(), "disabled", checkbox40.getDisabledAttribute());
    }

    // ----------------------------------------------------------- checkbox5

    HtmlInput checkbox50 = (HtmlInput) getElementOfTypeIncludingId(page,
        "input", "form:checkbox5:0");

    if (!validateExistence(checkbox50.getId(), "input", checkbox50,
        formatter)) {
      handleTestStatus(messages);
      return;
    }

    if (!HtmlElement.ATTRIBUTE_NOT_DEFINED
        .equals(checkbox50.getDisabledAttribute())) {
      formatter.format(
          "(checkbox50) Expected the disabled attribute "
              + "for checkbox '%s', not to be rendered when the disabled "
              + "attribute was specified as false in the JSP%n.",
          checkbox50.getId());
    }

    // ----------------------------------------------------------- checkbox6

    HtmlInput checkbox60 = (HtmlInput) getElementOfTypeIncludingId(page,
        "input", "form:checkbox6:0");

    if (!validateExistence(checkbox60.getId(), "input", checkbox60,
        formatter)) {
      handleTestStatus(messages);
      return;
    }

    if (!"readonly".equals(checkbox60.getAttribute("readonly"))) {
      formatter.format(
          "Expected the readonly attribute for checkbox "
              + "'%s', to be rendered as '%s', instead found '%s' %n.",
          checkbox60.getId(), "readonly", checkbox60.getAttribute("readonly"));
    }

    // ----------------------------------------------------------- checkbox7

    HtmlInput checkbox70 = (HtmlInput) getElementOfTypeIncludingId(page,
        "input", "form:checkbox7:0");

    if (!validateExistence(checkbox70.getId(), "input", checkbox70,
        formatter)) {
      handleTestStatus(messages);
      return;
    }

    if (!HtmlElement.ATTRIBUTE_NOT_DEFINED
        .equals(checkbox70.getAttribute("readonly"))) {
      formatter.format("Expected the readonly attribute for checkbox "
          + "'%s', not to be rendered when the readonly attribute "
          + "was specified as false in the JSP %n.", checkbox60.getId());
    }

    // ----------------------------------------------------------- checkbox8

    HtmlInput checkbox80 = (HtmlInput) getElementOfTypeIncludingId(page,
        "input", "form:checkbox8:0");

    // Get the Enclosing Table Row.
    HtmlTableRow tablerow80 = (HtmlTableRow) checkbox80
        .getEnclosingElement("tr");

    if (!validateExistence(checkbox80.getId(), "input", checkbox80,
        formatter)) {
      handleTestStatus(messages);
      return;
    }

    // Validate the default layout will be horizontal.
    if (!(2 == tablerow80.getCells().size())) {
      formatter.format(
          "Expected the default layout(horizontal) of the "
              + "checkboxes in checkbox '%s', for this you need more"
              + " then one cell <td></td> per table row. %n",
          checkbox80.getId());
    }

    // ----------------------------------------------------------- checkbox9

    HtmlInput checkbox90 = (HtmlInput) getElementOfTypeIncludingId(page,
        "input", "form:checkbox9:0");

    // Get the Enclosing Table Row.
    HtmlTableRow tablerow90 = (HtmlTableRow) checkbox90
        .getEnclosingElement("tr");

    if (!validateExistence(checkbox90.getId(), "input", checkbox90,
        formatter)) {
      handleTestStatus(messages);
      return;
    }

    // Validate the default layout will be horizontal.
    if (!(2 == tablerow90.getCells().size())) {
      formatter.format(
          "Expected the default layout(horizontal) of the "
              + "checkboxes in checkbox '%s', for this you need more"
              + " then one cell <td></td> per table row. %n",
          checkbox90.getId());
    }

    // ---------------------------------------------------------- checkbox10

    HtmlInput checkbox100 = (HtmlInput) getElementOfTypeIncludingId(page,
        "input", "form:checkbox10:0");

    // Get the Enclosing Table Row.
    HtmlTableRow tablerow100 = (HtmlTableRow) checkbox100
        .getEnclosingElement("tr");

    if (!validateExistence(checkbox100.getId(), "input", checkbox100,
        formatter)) {
      handleTestStatus(messages);
      return;
    }

    // Validate the layout will be vertical.
    if (!(1 == tablerow100.getCells().size())) {
      int realcell100 = tablerow100.getCells().size();
      formatter.format("(checkbox100) Expected vertical layout of the "
          + "checkboxes there should have been only one cell "
          + "<td></td> per table row. %n" + "REALLY HAVE: " + realcell100);
    }

    // ---------------------------------------------------------- checkbox11

    HtmlInput checkbox110 = (HtmlInput) getElementOfTypeIncludingId(page,
        "input", "form:checkbox11:0");

    // Get the Enclosing Table Row.
    HtmlTable table110 = (HtmlTable) checkbox110.getEnclosingElement("table");

    if (!validateExistence(checkbox110.getId(), "input", checkbox110,
        formatter)) {
      handleTestStatus(messages);
      return;
    }

    // Validate the border attribute
    int bwidth = 11;
    if (bwidth != Integer.parseInt(table110.getBorderAttribute())) {
      formatter.format("(checkbox110) Expected default layout"
          + "(horizontal) of the checkboxes there should have been "
          + "more then one cell <td></td> per table row. %n");
    }

    // ---------------------------------------------------------- checkbox12

    HtmlInput checkbox120 = (HtmlInput) getElementOfTypeIncludingId(page,
        "input", "form:checkbox12:0");

    HtmlInput checkbox121 = (HtmlInput) getElementOfTypeIncludingId(page,
        "input", "form:checkbox12:1");

    // Validate checkbox120 & checkbox121
    if (!validateExistence(checkbox120.getId(), "input", checkbox120, formatter)
        && !validateExistence(checkbox121.getId(), "input", checkbox121,
            formatter)) {
      handleTestStatus(messages);
      return;
    }

    // Make sure no checkbox buttons are checked
    String isSelected[] = { checkbox120.getCheckedAttribute(),
        checkbox121.getCheckedAttribute() };

    for (int ii = 0; ii < isSelected.length; ii++) {
      if (isSelected[ii].equals("checked")) {
        String failedbutton = "checkbox" + ii;
        formatter
            .format("checkbox button should not be checked " + failedbutton);
        handleTestStatus(messages);
        return;
      }
    }

    // Test value attribute for checkbox120
    if (!"no".equals(checkbox120.getValueAttribute())) {
      formatter.format("Unexpected value for the 'value' attribute of "
          + "ManyCheckbox '%s'. %n" + "Expected: '%s' %n" + "Received: '%s' %n",
          checkbox120.getId(), "no", checkbox120.getValueAttribute());
    }

    // Test value attribute for checkbox121
    if (!"yes".equals(checkbox121.getValueAttribute())) {
      formatter.format("Unexpected value for the 'value' attribute of "
          + "ManyCheckbox '%s'. %n" + "Expected: '%s' %n" + "Received: '%s' %n",
          checkbox121.getId(), "yes", checkbox121.getValueAttribute());
    }

    handleTestStatus(messages);
  } // END manycheckboxRenderEncodeTest

  /**
   * @testName: manycheckboxRenderDecodeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Create two checkboxes and test none are checked. Then click
   *                 the submit button to input one option and submit the form.
   *                 Verify that the correct option is checked. Click the button
   *                 again to input the other option. Verify that the correct
   *                 option is checked and that the original option is no longer
   *                 checked. Click the submit button again with both options
   *                 selected. Verifiy that both options are checked.
   * 
   * @since 1.2
   */
  public void manycheckboxRenderDecodeTest() throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    HtmlPage page = getPage(CONTEXT_ROOT + "/faces/decodetest.jsp");

    HtmlInput checkbox10 = (HtmlInput) getElementOfTypeIncludingId(page,
        "input", "form1:checkbox1:0");

    HtmlInput checkbox11 = (HtmlInput) getElementOfTypeIncludingId(page,
        "input", "form1:checkbox1:1");

    if (!validateExistence(checkbox10.getId(), "input", checkbox10, formatter)
        && !validateExistence(checkbox11.getId(), "input", checkbox11,
            formatter)) {
      handleTestStatus(messages);
      return;
    }

    if (checkbox10.isChecked() || checkbox11.isChecked()) {
      formatter.format(
          "Both checkboxes should display "
              + "false(not checked) %n. checkbox '%s' is marked "
              + "= '%s' %n. checkbox '%s' is marked = '%s' %n.",
          checkbox10.getId(), checkbox10.isChecked(), checkbox11.getId(),
          checkbox11.isChecked());
    }

    // Check checkbox1:0
    checkbox10.setChecked(true);
    HtmlInput button1 = (HtmlInput) getInputIncludingId(page, "button1");

    try {
      page = (HtmlPage) button1.click();
    } catch (IOException e) {
      formatter.format("Unexpected exception clicking button1: %s%n", e);
      handleTestStatus(messages);
      return;
    }

    checkbox10 = (HtmlInput) getElementOfTypeIncludingId(page, "input",
        "form1:checkbox1:0");

    checkbox11 = (HtmlInput) getElementOfTypeIncludingId(page, "input",
        "form1:checkbox1:1");

    if (!validateExistence(checkbox10.getId(), "input", checkbox10, formatter)
        && !validateExistence(checkbox11.getId(), "input", checkbox11,
            formatter)) {
      handleTestStatus(messages);
      return;
    }

    if (!checkbox10.isChecked() || checkbox11.isChecked()) {
      formatter.format(
          "After first click, expected option '%d' "
              + "checked attribute to be '%s', found '%s'; expected "
              + "option '%d' checked attribute to be '%s', found '%s'.",
          0, "true", checkbox10.isChecked(), 1, "false",
          checkbox11.isChecked());
    }

    // Check checkbox1:1 & uncheck checkbox1:0
    checkbox10.setChecked(false);
    checkbox11.setChecked(true);
    button1 = (HtmlInput) getInputIncludingId(page, "button1");

    try {
      page = (HtmlPage) button1.click();
    } catch (IOException e) {
      formatter.format("Unexpected exception clicking button1: %s%n", e);
      handleTestStatus(messages);
      return;
    }

    checkbox10 = (HtmlInput) getElementOfTypeIncludingId(page, "input",
        "form1:checkbox1:0");

    checkbox11 = (HtmlInput) getElementOfTypeIncludingId(page, "input",
        "form1:checkbox1:1");

    if (!validateExistence(checkbox10.getId(), "input", checkbox10, formatter)
        && !validateExistence(checkbox11.getId(), "input", checkbox11,
            formatter)) {
      handleTestStatus(messages);
      return;
    }

    if (checkbox10.isChecked() || !checkbox11.isChecked()) {
      formatter.format(
          "After second click, expected option '%d' "
              + "checked attribute to be '%s', found '%s'; expected "
              + "option '%d' checked attribute to be '%s', found '%s'.",
          0, "false", checkbox10.isChecked(), 1, "true",
          checkbox11.isChecked());
    }

    // Check both chechboxes checkbox1:1 & checkbox1:0
    checkbox10.setChecked(true);
    checkbox11.setChecked(true);
    button1 = (HtmlInput) getInputIncludingId(page, "button1");

    try {
      page = (HtmlPage) button1.click();
    } catch (IOException e) {
      formatter.format("Unexpected exception clicking button1: %s%n", e);
      handleTestStatus(messages);
      return;
    }

    checkbox10 = (HtmlInput) getElementOfTypeIncludingId(page, "input",
        "form1:checkbox1:0");

    checkbox11 = (HtmlInput) getElementOfTypeIncludingId(page, "input",
        "form1:checkbox1:1");

    if (!validateExistence(checkbox10.getId(), "input", checkbox10, formatter)
        && !validateExistence(checkbox11.getId(), "input", checkbox11,
            formatter)) {
      handleTestStatus(messages);
      return;
    }

    if (!checkbox10.isChecked() || !checkbox11.isChecked()) {
      formatter.format(
          "After second click, expected option '%d' "
              + "checked attribute to be '%s', found '%s'; expected "
              + "option '%d' checked attribute to be '%s', found '%s'.",
          0, "true", checkbox10.isChecked(), 1, "true", checkbox11.isChecked());
    }

    /*
     * Validate Conversion Types (PDL == jsp)
     */
    SelectManyValidator smv = new SelectManyValidator();
    HtmlForm form2 = (HtmlForm) getElementOfTypeIncludingId(page, "form",
        "selectmany01");

    if (!smv.validate(page, formatter, form2, true)) {
      handleTestStatus(messages);
      return;
    }

    handleTestStatus(messages);

  } // END manycheckboxRenderDecodeTest

  /**
   * @testName: manyCheckboxSelectMany01Test
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
  public void manyCheckboxSelectMany01Test() throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    HtmlPage page = getPage(CONTEXT_ROOT + "/faces/selectmany01.xhtml");

    /*
     * Validate Conversion Types (PDL == jsp)
     */
    SelectManyValidator smv = new SelectManyValidator();
    HtmlForm form2 = (HtmlForm) getElementOfTypeIncludingId(page, "form",
        "selectmany01");

    if (!smv.validate(page, formatter, form2, true)) {
      handleTestStatus(messages);
      return;
    }

    handleTestStatus(messages);

  } // END manyCheckboxSelectMany01Test

  /**
   * @testName: manycheckboxRenderPassthroughTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Ensure the attributes that are considered passthrough by
   *                 the specification are rendered as is.
   * 
   * @since 1.2
   */
  public void manycheckboxRenderPassthroughTest() throws Fault {

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

      HtmlInput checkbox10 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "checkbox1:0");
      if (!validateExistence(checkbox10.getId(), "input", checkbox10,
          formatter)) {
        handleTestStatus(messages);
        return;
      }

      validateAttributeSet(control, checkbox10,
          new String[] { "id", "value", "name", "type" }, formatter);

      handleTestStatus(messages);
    }

  } // END manycheckboxRenderPassthroughTest
} // END URLClient
