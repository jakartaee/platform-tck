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
package com.sun.ts.tests.jsf.spec.render.oneradio;

import java.io.PrintWriter;
import java.io.IOException;
import java.util.Formatter;
import java.util.TreeMap;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlLabel;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;
import java.util.ArrayList;
import java.util.List;

public class URLClient extends BaseHtmlUnitClient {

  private static final String CONTEXT_ROOT = "/jsf_render_oneradio_web";

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
   * @testName: oneradioRenderEncodeTest
   * @assertion_ids: PENDING
   * @test_Strategy: Validate the rendering of javax.faces.SelectOne.Radio case
   *                 1: - Only the id attribute is defined and two radio
   *                 buttons. Verify that - the correct number of radio's are
   *                 rendered - none of the radio's are checked - the value
   *                 attributes for each option are correctly rendered. case 2:
   *                 - Attributes for id, enabledClass, and disabledClass are
   *                 defined. Verify that - each option is rendered as enabled
   *                 or disabled as appropriate, and - each option is rendered
   *                 with the correct style class. case 3: - Attributes for id,
   *                 value, and class are defined. case 4,5: - ensure the
   *                 disabled attribute is handled using html attribute
   *                 minimization (only the attribute name is rendered when
   *                 value is true, and nothing rendered when false) case 6,7: -
   *                 ensure the readonly attribute is handled using html
   *                 attribute minimization (only the attribute name is rendered
   *                 when value is true, and nothing rendered when false) case
   *                 8,9,10: - Ensure that the layout attribute is handled
   *                 correctly. When not specified make sure the radio buttons
   *                 are going horizontally. Then test for the two expected
   *                 values. pageDirection(vertically) or
   *                 lineDirection(horizontally). case 11 - Ensure that the
   *                 border attribute is rendered correctly in the radio's
   *                 enclosing "
   *                 <Table>
   *                 " element. - Ensure that the "escape" attribute on a given
   *                 selectItem renders correctly either set to true(default) or
   *                 false. case 12: - Validate the binding attribute through
   *                 the following. Verify that - the correct number of radio's
   *                 are rendered - none of the radio's are checked - the value
   *                 attributes for each option are correctly rendered.
   * 
   * @since 1.2
   */
  public void oneradioRenderEncodeTest() throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    List<HtmlPage> pages = new ArrayList<HtmlPage>();
    pages.add(getPage(CONTEXT_ROOT + "/faces/encodetest.jsp"));
    pages.add(getPage(CONTEXT_ROOT + "/faces/encodetest_facelet.xhtml"));

    for (HtmlPage page : pages) {
      // ---------------------------------------------------------- radio1

      HtmlInput radio10 = (HtmlInput) getElementOfTypeIncludingId(page, "input",
          "form:radio1:0");

      HtmlInput radio11 = (HtmlInput) getElementOfTypeIncludingId(page, "input",
          "form:radio1:1");

      // Validate radio10 & radio11
      if (!validateExistence(radio10.getId(), "input", radio10, formatter)
          && !validateExistence(radio11.getId(), "input", radio11, formatter)) {
        handleTestStatus(messages);
        return;
      }

      // Make sure no radio buttons are checked
      String inputedOptions[] = { radio10.getCheckedAttribute(),
          radio11.getCheckedAttribute() };

      for (int ii = 0; ii < inputedOptions.length; ii++) {
        if ("checked".equals(inputedOptions[ii])) {
          String failedbutton = "radio" + ii;
          formatter
              .format("radio button should not be checked " + failedbutton);
          handleTestStatus(messages);
          return;
        }
      }

      // Test value attribute for radio10
      if (!"true".equals(radio10.getValueAttribute())) {
        formatter.format(
            "Expected the value of option '%d' for "
                + "radio10 to be '%s', found '%s'",
            0, "true", radio10.getValueAttribute());
      }

      // Test value attribute for radio11
      if (!"false".equals(radio11.getValueAttribute())) {
        formatter.format(
            "Expected the value of option '%d' for "
                + "radio11 to be '%s', found '%s'",
            1, "true", radio11.getValueAttribute());
      }

      // ---------------------------------------------------------- radio2

      HtmlInput radio20 = (HtmlInput) getElementOfTypeIncludingId(page, "input",
          "form:radio2:0");

      HtmlInput radio21 = (HtmlInput) getElementOfTypeIncludingId(page, "input",
          "form:radio2:1");

      if (!validateExistence(radio20.getId(), "input", radio20, formatter)
          && !validateExistence(radio21.getId(), "input", radio21, formatter)) {
        handleTestStatus(messages);
        return;
      }

      // Get the labels for radio button 2:0 & 2:1 for testing class att.
      HtmlLabel label20 = (HtmlLabel) getLabelIncludingFor(page, "label",
          radio20.getId());

      HtmlLabel label21 = (HtmlLabel) getLabelIncludingFor(page, "label",
          radio21.getId());

      if (!radio20.isDisabled()) {
        formatter.format("Expected radio20 to be disabled", 0, "disabled");
      }

      if (!"Color: red;".equals(label20.getAttribute("class"))) {
        formatter.format(
            "Expected the value of the class attribute " + "for radio20"
                + " to be '%s', found '%s'",
            "Color: red;", radio20.getAttribute("class"));
      }

      if (radio21.isDisabled()) {
        formatter.format("Expected option '%d' for radio2 to be '%s'", 1,
            "enabled");
      }

      if (!"text".equals(label21.getAttribute("class"))) {
        formatter.format(
            "Expected the value of the class attribute " + "for radio21"
                + " to be '%s', found '%s'",
            "text", radio21.getAttribute("class"));
      }

      // ---------------------------------------------------------- radio3

      HtmlInput radio30 = (HtmlInput) getElementOfTypeIncludingId(page, "input",
          "form:radio3:0");

      // Get the parent table element, it holds the classstyle attibute.
      HtmlTable parent30 = (HtmlTable) radio30.getEnclosingElement("table");

      if (!validateExistence(radio30.getId(), "input", radio30, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!"text".equals(parent30.getAttribute("class"))) {
        formatter.format(
            "Expected the rendered value of the class "
                + "attribute to be 'text' for the text field containing"
                + " ID 'radio30', but found '%s'%n",
            radio30.getAttribute("class"));
      }

      // ---------------------------------------------------------- radio4
      HtmlInput radio40 = (HtmlInput) getElementOfTypeIncludingId(page, "input",
          "form:radio4:0");

      if (!validateExistence(radio40.getId(), "input", radio40, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!"disabled".equals(radio40.getDisabledAttribute())) {
        formatter.format(
            "(radio40) Expected the disabled attribute "
                + "to be rendered as '%s', instead found '%s'%n",
            "disabled", radio40.getDisabledAttribute());
      }

      // ---------------------------------------------------------- radio5

      HtmlInput radio50 = (HtmlInput) getElementOfTypeIncludingId(page, "input",
          "form:radio5:0");

      if (!validateExistence(radio50.getId(), "input", radio50, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!HtmlElement.ATTRIBUTE_NOT_DEFINED
          .equals(radio50.getDisabledAttribute())) {
        formatter.format("(radio50) Expected the disabled attribute "
            + "to not be rendered when the disabled "
            + "attribute was specified as false in the JSP%n.");
      }

      // ---------------------------------------------------------- radio6

      HtmlInput radio60 = (HtmlInput) getElementOfTypeIncludingId(page, "input",
          "form:radio6:0");

      if (!validateExistence(radio60.getId(), "input", radio60, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!"readonly".equals(radio60.getAttribute("readonly"))) {
        formatter.format(
            "(radio60) Expected the readonly attribute "
                + "to be rendered as '%s', instead found '%s'%n",
            "readonly", radio60.getAttribute("readonly"));
      }

      // ---------------------------------------------------------- radio7

      HtmlInput radio70 = (HtmlInput) getElementOfTypeIncludingId(page, "input",
          "form:radio7:0");

      if (!validateExistence(radio70.getId(), "input", radio70, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!HtmlElement.ATTRIBUTE_NOT_DEFINED
          .equals(radio70.getAttribute("readonly"))) {
        formatter.format("(radio70) Expected the readonly attribute "
            + "to not be rendered when the readonly "
            + "attribute was specified as false in the JSP" + "%n.");
      }

      // ---------------------------------------------------------- radio8

      HtmlInput radio80 = (HtmlInput) getElementOfTypeIncludingId(page, "input",
          "form:radio8:0");

      // Get the Enclosing Table Row.
      HtmlTableRow tablerow80 = (HtmlTableRow) radio80
          .getEnclosingElement("tr");

      if (!validateExistence(radio80.getId(), "input", radio80, formatter)) {
        handleTestStatus(messages);
        return;
      }

      // Validate the default layout will be horizontal.
      if (!(2 == tablerow80.getCells().size())) {
        formatter.format("(radio80) Expected default layout(horizontal)"
            + "of the radio buttons there should have been more then "
            + "one cell <td></td> per table row. %n");
      }

      // ---------------------------------------------------------- radio9

      HtmlInput radio90 = (HtmlInput) getElementOfTypeIncludingId(page, "input",
          "form:radio9:0");

      // Get the Enclosing Table Row.
      HtmlTableRow tablerow90 = (HtmlTableRow) radio90
          .getEnclosingElement("tr");

      if (!validateExistence(radio90.getId(), "input", radio90, formatter)) {
        handleTestStatus(messages);
        return;
      }

      // Validate the default layout will be horizontal.
      if (!(2 == tablerow90.getCells().size())) {
        formatter.format("(radio90) Expected horizontal layout of the "
            + "radio buttons there should have been more then one cell "
            + "<td></td> per table row. %n");
      }

      // --------------------------------------------------------- radio10

      HtmlInput radio100 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form:radio10:0");

      // Get the Enclosing Table Row.
      HtmlTableRow tablerow100 = (HtmlTableRow) radio100
          .getEnclosingElement("tr");

      if (!validateExistence(radio100.getId(), "input", radio100, formatter)) {
        handleTestStatus(messages);
        return;
      }

      // Validate the layout will be vertical.
      if (!(1 == tablerow100.getCells().size())) {
        int realcell100 = tablerow100.getCells().size();
        formatter.format("(radio100) Expected vertical layout of the "
            + "radio buttons there should have been only one cell "
            + "<td></td> per table row. %n" + "REALLY HAVE: " + realcell100);
      }

      // --------------------------------------------------------- radio11

      HtmlInput radio110 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form:radio11:0");
      HtmlInput radio111 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form:radio11:1");

      // Get the Enclosing Table Row.
      HtmlTable table110 = (HtmlTable) radio110.getEnclosingElement("table");

      // Get Labels for each Radio Button.
      HtmlLabel label0 = (HtmlLabel) getLabelIncludingFor(page, "label",
          radio110.getId());
      HtmlLabel label1 = (HtmlLabel) getLabelIncludingFor(page, "label",
          radio111.getId());

      // Get webresponse for Html source testing.
      String wr = page.getWebResponse().getContentAsString();

      // test for .jsp syntax
      if ("encodetest".equals(page.getTitleText())) {
        if (!("&foo".equals(label0.asText().trim()) && wr
            .contains("<label for=\"form:radio11:0\"> " + "&foo</label>"))) {
          formatter.format(
              "Unexpected value in JSP page for Radio " + "Label: %s %n"
                  + "Expected: &foo and <label for=\"form:radio11:0\">"
                  + " &foo</label> %n" + "Recieved: %s and " + wr + "%n",
              radio110.getId(), label0.asText().trim());
        }

        if (!("&bar".equals(label1.asText().trim()) && wr.contains(
            "<label for=\"form:radio11:1\">" + " &amp;bar</label>"))) {
          formatter.format(
              "Unexpected value in JSP page for Radio " + "Label: %s %n"
                  + "Expected: &bar and <label for=\"form:radio11:1\">"
                  + " &amp;bar</label> %n" + "Recieved: %s and " + wr + "%n",
              radio111.getId(), label1.asText().trim());
        }
      }

      // test for facelet syntax
      else if ("encodetest_facelet".equals(page.getTitleText())) {
        if (!("&foo".equals(label0.asText().trim()) && wr
            .contains("<label for=\"form:radio11:0\"> " + "&foo</label>"))) {
          formatter.format(
              "Unexpected value in Facelet page " + "for Radio Label: %s %n "
                  + "Expected: %s and <label for=\"form:radio11:0\">"
                  + " &foo</label>%n" + "Recieved: %s " + wr + "%n",
              radio110.getId(), "&foo", label0.asText().trim());
        }

        if (!("&bar".equals(label1.asText().trim()) && wr.contains(
            "<label for=\"form:radio11:1\">" + " &amp;bar</label>"))) {
          formatter.format(
              "Unexpected value in Facelet page " + "for Radio Label: %s %n"
                  + "Expected: %s and <label for=\"form:radio11:1\">"
                  + " &amp;bar</label> %n" + "Recieved: %s " + wr + "%n",
              radio111.getId(), "&bar", label1.asText().trim());
        }

      } else {
        formatter.format("Unable to find either page title! %n"
            + "'encodetest_facelet' for Facelet style test! %n"
            + "'encodetest' for JSP style test! %n"
            + "Test FAILED. Due to Unknown page!");
      }

      if (!validateExistence(radio110.getId(), "input", radio110, formatter)) {
        handleTestStatus(messages);
        return;
      }

      // Validate the border attribute
      int bwidth = 11;
      if (bwidth != Integer.parseInt(table110.getBorderAttribute())) {
        formatter.format("(radio110) Expected default layout(horizontal)"
            + "of the radio buttons there should have been more then "
            + "one cell <td></td> per table row. %n");
      }

      // --------------------------------------------------------- radio12
      HtmlInput radio120 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form:radio12:0");

      HtmlInput radio121 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form:radio12:1");

      // Validate radio120 & radio121
      if (!validateExistence(radio120.getId(), "input", radio120, formatter)
          && !validateExistence(radio121.getId(), "input", radio121,
              formatter)) {
        handleTestStatus(messages);
        return;
      }

      // Make sure no radio buttons are checked
      String io[] = { radio120.getCheckedAttribute(),
          radio121.getCheckedAttribute() };

      for (int ii = 0; ii < io.length; ii++) {
        if ("checked".equals(io[ii])) {
          String failedbutton = "radio" + ii;
          formatter
              .format("radio button should not be checked " + failedbutton);
          handleTestStatus(messages);
          return;
        }
      }

      // Test value attribute for radio120
      if (!"no".equals(radio120.getValueAttribute())) {
        formatter.format(
            "Unexpected value for option '%d' on %s %n" + "Expected: '%s' %n"
                + "Found: '%s' %n",
            0, "radio12", "no", radio120.getValueAttribute());
      }

      // Test value attribute for radio11
      if (!"yes".equals(radio121.getValueAttribute())) {
        formatter.format(
            "Unexpected value for option '%d' on %s! %n" + "Expected: '%s' %n"
                + "Found '%s' %n",
            1, "radio12", "yes", radio121.getValueAttribute());
      }

      handleTestStatus(messages);
    }
  } // END oneradioRenderEncodeTest

  /**
   * @testName: oneradioRenderDecodeTest
   * @assertion_ids: PENDING
   * @test_Strategy: Create two radio buttons test to make sure none are
   *                 checked. Then click a button to input one option and submit
   *                 the form. Verify that the correct option is checked. Click
   *                 the button again to input the other option. Verify that the
   *                 correct option is inputed and that the original option is
   *                 no longer inputed.
   * 
   * @since 1.2
   */
  public void oneradioRenderDecodeTest() throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    List<HtmlPage> pages = new ArrayList<HtmlPage>();
    pages.add(getPage(CONTEXT_ROOT + "/faces/decodetest.jsp"));
    pages.add(getPage(CONTEXT_ROOT + "/faces/decodetest_facelet.xhtml"));

    for (HtmlPage page : pages) {
      HtmlInput radio10 = (HtmlInput) getElementOfTypeIncludingId(page, "input",
          "form1:radio1:0");

      HtmlInput radio11 = (HtmlInput) getElementOfTypeIncludingId(page, "input",
          "form1:radio1:1");

      if (!validateExistence(radio10.getId(), "input", radio10, formatter)
          && !validateExistence(radio11.getId(), "input", radio11, formatter)) {
        handleTestStatus(messages);
        return;
      }

      // No radio buttons are checked
      String inputedOptions[] = { radio10.getCheckedAttribute(),
          radio11.getCheckedAttribute() };

      for (int ii = 0; ii < inputedOptions.length; ii++) {
        String failedbutton = "radio" + ii;
        if ("checked".equals(inputedOptions[ii])) {
          formatter
              .format("radio button should not be checked " + failedbutton);
          handleTestStatus(messages);
          return;
        }
      }

      // Check radio1:0
      radio10.setChecked(true);
      HtmlInput button1 = (HtmlInput) getInputIncludingId(page, "button1");

      try {
        page = (HtmlPage) button1.click();
      } catch (IOException e) {
        formatter.format("Unexpected exception clicking button1: %s%n", e);
        handleTestStatus(messages);
        return;
      }

      radio10 = (HtmlInput) getElementOfTypeIncludingId(page, "input",
          "form1:radio1:0");

      radio11 = (HtmlInput) getElementOfTypeIncludingId(page, "input",
          "form1:radio1:1");

      if (!validateExistence(radio10.getId(), "input", radio10, formatter)
          && !validateExistence(radio11.getId(), "input", radio11, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!radio10.isChecked() || radio11.isChecked()) {
        formatter.format(
            "After first click, expected option '%d' "
                + "inputed attribute to be '%s', found '%s'; expected "
                + "option '%d' inputed attribute to be '%s', found '%s'.",
            0, "true", radio10.isChecked(), 1, "false", radio11.isChecked());
      }

      // Check radio1:1
      radio11.setChecked(true);
      button1 = (HtmlInput) getInputIncludingId(page, "button1");

      try {
        page = (HtmlPage) button1.click();
      } catch (IOException e) {
        formatter.format("Unexpected exception clicking button1: %s%n", e);
        handleTestStatus(messages);
        return;
      }

      radio10 = (HtmlInput) getElementOfTypeIncludingId(page, "input",
          "form1:radio1:0");

      radio11 = (HtmlInput) getElementOfTypeIncludingId(page, "input",
          "form1:radio1:1");

      if (!validateExistence(radio10.getId(), "input", radio10, formatter)
          && !validateExistence(radio11.getId(), "input", radio11, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (radio10.isChecked() || !radio11.isChecked()) {
        formatter.format(
            "After first click, expected option '%d' "
                + "inputed attribute to be '%s', found '%s'; expected "
                + "option '%d' inputed attribute to be '%s', found '%s'.",
            0, "false", radio10.isChecked(), 1, "true", radio11.isChecked());
      }

      handleTestStatus(messages);
    }
  } // END oneradioRenderDecodeTest

  /**
   * @testName: oneradioRenderPassthroughTest
   * @assertion_ids: PENDING
   * @test_Strategy: Ensure the attributes that are considered passthrough by
   *                 the specification are rendered as is.
   * 
   * @since 1.2
   */
  public void oneradioRenderPassthroughTest() throws Fault {

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

      HtmlInput radio10 = (HtmlInput) getElementOfTypeIncludingId(page, "input",
          "radio1:0");
      if (!validateExistence(radio10.getId(), "input", radio10, formatter)) {
        handleTestStatus(messages);
        return;
      }

      validateAttributeSet(control, radio10,
          new String[] { "id", "value", "name", "type" }, formatter);

      handleTestStatus(messages);
    }
  } // END oneradioRenderPassthroughTest
} // END URLClient
