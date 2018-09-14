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
package com.sun.ts.tests.jsf.spec.render.message;

import java.io.PrintWriter;
import java.io.IOException;
import java.util.Formatter;
import java.util.TreeMap;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;
import java.util.ArrayList;
import java.util.List;

public class URLClient extends BaseHtmlUnitClient {

  private static final String CONTEXT_ROOT = "/jsf_render_message_web";

  private String[] sevs = { "INFO", "WARN", "ERROR", "FATAL" };

  private String testCaseName = ""; // This is only used for output messages.

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
   * @testName: messageRenderEncodeTest
   * @assertion_ids: PENDING
   * @test_Strategy: Validate the rendering of javax.faces.Message
   * 
   *                 case 1: - The id, showSummary, showDetail, and title
   *                 attributes are defined. Verifiy that we - render the
   *                 correct value for "showSummary". - render the correct value
   *                 for "showDetail". - that we render "id" and its value is
   *                 correct. case 2: - The style and styleClass attributes are
   *                 defined. Verifiy that we - render "styleClass" with the
   *                 expected value.
   * 
   *                 case 3: - The infoStyle and infoClass attributes are
   *                 defined. Verifiy that we - render "infoStyle" with the
   *                 expected value. - render "infoClass" with the expected
   *                 value. case 4: - The warnStyle and warnClass attributes are
   *                 defined. Verifiy that we - render "warnStyle" with the
   *                 expected value. - render "warnClass" with the expected
   *                 value. case 5: - The errorStyle and errorClass are defined.
   *                 Verifiy that we - render "errorStyle" with the expect
   *                 value. - render "errorClass" with expected value. case 6: -
   *                 The id, fatalStyle, and fatalClass are defined. Verifiy
   *                 that we - render "fatalStyle" with the expect value. -
   *                 render "fatalClass" with expected value. case 7: - Validate
   *                 that we can have all severity style attributes set and we
   *                 can get all severity messages to adhere to the correct
   *                 style attribute. Style Attributes: -infoStyle -warnStyle
   *                 -errorStyle -fatalStyle case 8: - Validate that we can have
   *                 all severity class attributes set and we can get all
   *                 severity messages to adhere to the correct class attribute.
   *                 Class Attributes: -infoClass -warnClass -errorClass
   *                 -fatalClass case 9: - Validate that we render both the
   *                 message summary and message detail. -showSummary = true
   *                 -showDetail = true case 10: - Validate that we render only
   *                 the message summary. -showSummary = true -showDetail =
   *                 false case 11: - Validate that we render only the message
   *                 detail. -showSummary = false -showDetail = true case 12: -
   *                 Validate that we do not render a message at all.
   *                 -showSummary = false -showDetail = false
   * @since 1.2
   */
  public void messageRenderEncodeTest() throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    List<HtmlPage> pages = new ArrayList<HtmlPage>();
    pages.add(getPage(CONTEXT_ROOT + "/faces/encodetest.jsp"));
    pages.add(getPage(CONTEXT_ROOT + "/faces/encodetest_facelet.xhtml"));

    for (HtmlPage page : pages) {
      // ---------------------------------------------------------------Case
      // 1:
      testCaseName = "Case 1";
      // Generate the message
      HtmlInput button1 = (HtmlInput) getElementOfTypeIncludingId(page, "input",
          "form1:button1");
      HtmlInput clientid = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form1:id1");
      HtmlInput severity = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form1:input1");

      try {
        clientid.setValueAttribute("form1:input1");
        severity.setValueAttribute("INFO");
        page = (HtmlPage) button1.click();
      } catch (IOException e) {
        formatter.format(
            testCaseName + ": Unexpected exception clicking " + "button1: %s%n",
            e);
        handleTestStatus(messages);
        return;
      }

      // Get HtlmSpan element by id and test for validity.
      HtmlSpan span1 = (HtmlSpan) getElementOfTypeIncludingId(page, "span",
          "form1:message1");
      if (!validateExistence(span1.getId(), "form1:message1", span1,
          formatter)) {
        handleTestStatus(messages);
        return;
      }

      String message1 = span1.asText().trim();
      if (!"INFO: Summary Message INFO: Detailed Message".equals(message1)) {
        formatter
            .format(testCaseName + ": Unexpected value for " + "message1! %n"
                + "Expected: INFO: Summary Message INFO: Detailed Message "
                + "%n Received: " + message1 + "%n");
      }

      // ---------------------------------------------------------------Case
      // 2:
      testCaseName = "Case 2";
      // Generate the message
      HtmlInput button2 = (HtmlInput) getElementOfTypeIncludingId(page, "input",
          "form2:button2");
      HtmlInput clientid2 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form2:id2");
      HtmlInput severity2 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form2:input2");

      try {
        clientid2.setValueAttribute("form2:input2");
        severity2.setValueAttribute("INFO");
        page = (HtmlPage) button2.click();
      } catch (IOException e) {
        formatter.format(
            testCaseName + ": Unexpected exception clicking " + "button2: %s%n",
            e);
        handleTestStatus(messages);
        return;
      }

      // Get HtlmSpan element by id and test for validity.
      HtmlSpan span2 = (HtmlSpan) getElementOfTypeIncludingId(page, "span",
          "form2:message2");
      if (!validateExistence(span2.getId(), "form2:message2", span2,
          formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!(span2.getAttribute("class").equals("underline"))) {
        formatter.format(testCaseName + " :Unexpected value for class "
            + "attribute! %n" + "Expected: underline %n" + "Received: "
            + span2.getAttribute("class") + "%n");
      }

      // ---------------------------------------------------------------Case
      // 3:
      testCaseName = "Case 3";
      // Generate the message
      HtmlInput button3 = (HtmlInput) getElementOfTypeIncludingId(page, "input",
          "form3:button3");
      HtmlInput clientid3 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form3:id3");
      HtmlInput severity3 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form3:input3");

      try {
        clientid3.setValueAttribute("form3:input3");
        severity3.setValueAttribute("INFO");
        page = (HtmlPage) button3.click();
      } catch (IOException e) {
        formatter.format(
            testCaseName + ": Unexpected exception clicking " + "button3: %s%n",
            e);
        handleTestStatus(messages);
        return;
      }

      // Get HtlmSpan element by id and test for validity.
      HtmlSpan span3 = (HtmlSpan) getElementOfTypeIncludingId(page, "span",
          "form3:message3");
      if (!validateExistence(span3.getId(), "form3:message3", span3,
          formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!(span3.getAttribute("style").equals("Color: blue;"))) {
        formatter.format(testCaseName + ": Unexpected value for style "
            + "attribute! %n" + "Expected: Color: blue; %n" + "Received: "
            + span3.getAttribute("style") + "%n");
      }

      if (!(span3.getAttribute("class").equals("underline"))) {
        formatter.format(testCaseName + ": Unexpected value for class "
            + "attribute! %n" + "Expected: underline %n" + "Received: "
            + span3.getAttribute("class") + "%n");
      }

      // ---------------------------------------------------------------Case
      // 4:
      testCaseName = "Case 4";
      // Generate the message
      HtmlInput button4 = (HtmlInput) getElementOfTypeIncludingId(page, "input",
          "form4:button4");
      HtmlInput clientid4 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form4:id4");
      HtmlInput severity4 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form4:input4");

      try {
        clientid4.setValueAttribute("form4:input4");
        severity4.setValueAttribute("WARN");
        page = (HtmlPage) button4.click();
      } catch (IOException e) {
        formatter.format(
            testCaseName + ": Unexpected exception clicking " + "button4: %s%n",
            e);
        handleTestStatus(messages);
        return;
      }

      // Get HtlmSpan element by id and test for validity.
      HtmlSpan span4 = (HtmlSpan) getElementOfTypeIncludingId(page, "span",
          "form4:message4");
      if (!validateExistence(span4.getId(), "form4:message4", span4,
          formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!(span4.getAttribute("style").equals("Color: green;"))) {
        formatter.format(testCaseName + ": Unexpected value for style "
            + "attribute! %n" + "Expected: Color: green; %n" + "Received: "
            + span4.getAttribute("style") + "%n");
      }

      if (!(span4.getAttribute("class").equals("underline"))) {
        formatter.format(testCaseName + ": Unexpected value for class "
            + "attribute! %n" + "Expected: underline %n" + "Received: "
            + span4.getAttribute("class") + "%n");
      }

      // ---------------------------------------------------------------Case
      // 5:
      testCaseName = "Case 5";
      // Generate the message
      HtmlInput button5 = (HtmlInput) getElementOfTypeIncludingId(page, "input",
          "form5:button5");
      HtmlInput clientid5 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form5:id5");
      HtmlInput severity5 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form5:input5");

      try {
        clientid5.setValueAttribute("form5:input5");
        severity5.setValueAttribute("ERROR");
        page = (HtmlPage) button5.click();
      } catch (IOException e) {
        formatter.format(
            testCaseName + ": Unexpected exception clicking " + "button5: %s%n",
            e);
        handleTestStatus(messages);
        return;
      }

      // Get HtlmSpan element by id and test for validity.
      HtmlSpan span5 = (HtmlSpan) getElementOfTypeIncludingId(page, "span",
          "form5:message5");
      if (!validateExistence(span5.getId(), "form5:message5", span5,
          formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!(span5.getAttribute("style").equals("Color: yellow;"))) {
        formatter.format(testCaseName + ": Unexpected value for style "
            + "attribute! %n" + "Expected: Color: yellow; %n" + "Received: "
            + span5.getAttribute("style") + "%n");
      }

      if (!(span5.getAttribute("class").equals("underline"))) {
        formatter.format(testCaseName + ": Unexpected value for class "
            + "attribute! %n" + "Expected: underline %n" + "Received: "
            + span5.getAttribute("class") + "%n");
      }

      // ---------------------------------------------------------------Case
      // 6:
      testCaseName = "Case 6";
      // Generate the message
      HtmlInput button6 = (HtmlInput) getElementOfTypeIncludingId(page, "input",
          "form6:button6");
      HtmlInput clientid6 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form6:id6");
      HtmlInput severity6 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form6:input6");

      try {
        clientid6.setValueAttribute("form6:input6");
        severity6.setValueAttribute("FATAL");
        page = (HtmlPage) button6.click();
      } catch (IOException e) {
        formatter.format(
            testCaseName + ": Unexpected exception clicking " + "button6: %s%n",
            e);
        handleTestStatus(messages);
        return;
      }

      // Get HtlmSpan element by id and test for validity.
      HtmlSpan span6 = (HtmlSpan) getElementOfTypeIncludingId(page, "span",
          "form6:message6");
      if (!validateExistence(span6.getId(), "form6:message6", span6,
          formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!(span6.getAttribute("style").equals("Color: red;"))) {
        formatter.format(testCaseName + ": Unexpected value for style "
            + "attribute! %n" + "Expected: Color: red; %n" + "Received: "
            + span6.getAttribute("style") + "%n");
      }

      if (!(span6.getAttribute("class").equals("underline"))) {
        formatter.format(testCaseName + ": Unexpected value for class "
            + "attribute! %n" + "Expected: underline %n" + "Received: "
            + span6.getAttribute("class") + "%n");
      }

      // ---------------------------------------------------------------Case
      // 7:
      testCaseName = "Case 7";
      // Generate a message.
      HtmlInput button7 = (HtmlInput) getElementOfTypeIncludingId(page, "input",
          "form7:button7");
      HtmlInput clientid7 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form7:id7");
      HtmlInput severity7 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form7:severity7");

      for (int i = 0; i < sevs.length; i++) {
        String sevLevel = sevs[i];
        String expectedStyle = null;

        // Based on the input level set the expected style.
        if (sevLevel.equals("INFO")) {
          expectedStyle = "Color: blue;";
        } else if (sevLevel.equals("WARN")) {
          expectedStyle = "Color: green;";
        } else if (sevLevel.equals("ERROR")) {
          expectedStyle = "Color: yellow;";
        } else if (sevLevel.equals("FATAL")) {
          expectedStyle = "Color: red;";
        }

        try {
          clientid7.setValueAttribute("form7:severity7");
          severity7.setValueAttribute(sevLevel);
          page = (HtmlPage) button7.click();
        } catch (IOException e) {
          formatter.format(testCaseName + ": Unexpected exception "
              + "clicking button7: %s%n", e);
          handleTestStatus(messages);
          return;
        }

        // Now retrieve the message
        HtmlSpan messagespan7 = (HtmlSpan) getElementOfTypeIncludingId(page,
            "span", "form7:message7");
        if (!validateExistence(messagespan7.getId(), "form7:message7",
            messagespan7, formatter)) {
          handleTestStatus(messages);
          return;
        }

        // Test the attribute for its validity.
        String style = messagespan7.getAttribute("style");
        if (!expectedStyle.equals(style)) {
          formatter.format(testCaseName + ": Unexpected style "
              + "attribute. %n" + "Expected: " + expectedStyle + "%n"
              + "Received: " + style + "%n");
        }
      }

      // --------------------------------------------------------------
      // Case 8:
      testCaseName = "Case 8";
      // Generate a message.
      HtmlInput button8 = (HtmlInput) getElementOfTypeIncludingId(page, "input",
          "form8:button8");
      HtmlInput clientid8 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form8:id8");
      HtmlInput severity8 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form8:severity8");

      for (int i = 0; i < sevs.length; i++) {
        String sevLevel = sevs[i];
        String expectedClass = null;

        // Based on the input level set the expected style.
        if (sevLevel.equals("INFO")) {
          expectedClass = "class_info";
        } else if (sevLevel.equals("WARN")) {
          expectedClass = "class_warn";
        } else if (sevLevel.equals("ERROR")) {
          expectedClass = "class_error";
        } else if (sevLevel.equals("FATAL")) {
          expectedClass = "class_fatal";
        }

        try {
          clientid8.setValueAttribute("form8:severity8");
          severity8.setValueAttribute(sevLevel);
          page = (HtmlPage) button8.click();
        } catch (IOException e) {
          formatter.format(testCaseName + ": Unexpected exception "
              + "clicking button8: %s%n", e);
          handleTestStatus(messages);
          return;
        }

        // Now retrieve the message
        HtmlSpan messagespan8 = (HtmlSpan) getElementOfTypeIncludingId(page,
            "span", "form8:message8");
        if (!validateExistence(messagespan8.getId(), "form8:message8",
            messagespan8, formatter)) {
          handleTestStatus(messages);
          return;
        }

        // Test the attribute for its validity.
        String messclass = messagespan8.getAttribute("class");
        if (!expectedClass.equals(messclass)) {
          formatter.format(testCaseName + ": Unexpected class "
              + "attribute! %n" + "Expected: " + expectedClass + "%n"
              + "Recieved: " + messclass);
        }
      }

      // --------------------------------------------------------------
      // Case 9:
      testCaseName = "Case 9";
      // Generate message.(summary = true, detail = true)
      HtmlInput button9 = (HtmlInput) getElementOfTypeIncludingId(page, "input",
          "form9:button9");
      HtmlInput clientid9 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form9:id9");
      HtmlInput severity9 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form9:severity9");

      try {
        clientid9.setValueAttribute("form9:severity9");
        severity9.setValueAttribute("INFO");
        page = (HtmlPage) button9.click();
      } catch (IOException e) {
        formatter.format(
            testCaseName + ": Unexpected exception clicking " + "button9: %s%n",
            e);
        handleTestStatus(messages);
        return;
      }

      // Now retrieve the message
      HtmlSpan span9 = (HtmlSpan) getElementOfTypeIncludingId(page, "span",
          "form9:message9");
      if (!validateExistence(span9.getId(), "form9:message9", span9,
          formatter)) {
        handleTestStatus(messages);
        return;
      }

      // Validate the message itself.
      String sdMessage = span9.asText().trim();
      if (!"INFO: Summary Message INFO: Detailed Message".equals(sdMessage)) {
        formatter.format(testCaseName + ": Unexpected value for message! "
            + "%n" + "Expected: INFO: Summary Message INFO: Detailed Message"
            + " %n" + "Received: " + sdMessage + "%n");
      }

      // -------------------------------------------------------------
      // Case 10:
      testCaseName = "Case 10";
      // Generate a message.(summary = true, detail = false)
      HtmlInput button10 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form10:button10");
      HtmlInput clientid10 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form10:id10");
      HtmlInput severity10 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form10:severity10");

      try {
        clientid10.setValueAttribute("form10:severity10");
        severity10.setValueAttribute("INFO");
        page = (HtmlPage) button10.click();
      } catch (IOException e) {
        formatter.format(testCaseName + ": Unexpected exception clicking "
            + "button10: %s%n", e);
        handleTestStatus(messages);
        return;
      }

      // Now retrieve the message
      HtmlSpan span10 = (HtmlSpan) getElementOfTypeIncludingId(page, "span",
          "form10:message10");
      if (!validateExistence(span10.getId(), "form10:message10", span10,
          formatter)) {
        handleTestStatus(messages);
        return;
      }

      // Validate the message itself.
      String sMessage = span10.asText().trim();
      if (!"INFO: Summary Message".equals(sMessage)) {
        formatter.format(testCaseName + ": Unexpected value for message!" + "%n"
            + "Expected: INFO: Summary Message %n" + "Received: " + sMessage
            + "%n");
        handleTestStatus(messages);
        return;
      }

      // -------------------------------------------------------------
      // Case 11:
      testCaseName = "Case 11";
      // Generate a message.(summary = false, detail = true)
      HtmlInput button11 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form11:button11");
      HtmlInput clientid11 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form11:id11");
      HtmlInput severity11 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form11:severity11");

      try {
        clientid11.setValueAttribute("form11:severity11");
        severity11.setValueAttribute("INFO");
        page = (HtmlPage) button11.click();
      } catch (IOException e) {
        formatter.format(testCaseName + ": Unexpected exception clicking "
            + "button11: %s%n", e);
        handleTestStatus(messages);
        return;
      }

      // Now retrieve the message
      HtmlSpan span11 = (HtmlSpan) getElementOfTypeIncludingId(page, "span",
          "form11:message11");
      if (!validateExistence(span11.getId(), "form11:message11", span11,
          formatter)) {
        handleTestStatus(messages);
        return;
      }

      // Validate the message itself.
      String dMessage = span11.asText().trim();
      if (!"INFO: Detailed Message".equals(dMessage)) {
        formatter.format(testCaseName + ": Unexpected value for message! "
            + "%n" + "Expected: INFO: Detailed Message %n" + "Received: "
            + dMessage + "%n");
      }

      // -------------------------------------------------------------
      // Case 12:
      testCaseName = "Case 12";
      // Generate a message.(summary = false, detail = false)
      HtmlInput button12 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form12:button12");
      HtmlInput clientid12 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form12:id12");
      HtmlInput severity12 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form12:severity12");

      try {
        clientid12.setValueAttribute("form12:severity12");
        severity12.setValueAttribute("INFO");
        page = (HtmlPage) button12.click();
      } catch (IOException e) {
        formatter.format(testCaseName + ": Unexpected exception clicking "
            + "button12: %s%n", e);
        handleTestStatus(messages);
        return;
      }

      // Now retrieve the message
      HtmlSpan span12 = (HtmlSpan) getElementOfTypeIncludingId(page, "span",
          "form12:message12");
      if (!validateExistence(span12.getId(), "form12:message12", span6,
          formatter)) {
        handleTestStatus(messages);
        return;
      }

      // Validate the message itself.
      String noMessage = span12.asText().trim();
      if (!"".equals(noMessage)) {
        formatter.format(testCaseName + ": Unexpected value for message! "
            + "%n" + "Expected: %n" + "Received: " + noMessage + "%n");
      }

      handleTestStatus(messages);
    }
  } // END messageEncodeTest

  /**
   * @testName: messageRenderPassthroughTest
   * @assertion_ids: PENDING
   * @test_Strategy: Ensure the attributes that are considered passthrough by
   *                 the specification are rendered as is.
   * 
   * @since 1.2
   */
  public void messageRenderPassthroughTest() throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    TreeMap<String, String> control = new TreeMap<String, String>();
    control.put("dir", "LTR");
    control.put("lang", "en");
    control.put("style", "Color: red;");
    control.put("title", "title");

    List<HtmlPage> pages = new ArrayList<HtmlPage>();
    pages.add(getPage(CONTEXT_ROOT + "/faces//faces/passthroughtest.jsp"));
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

      // Generate the message.
      HtmlInput button = (HtmlInput) getElementOfTypeIncludingId(page, "input",
          "testform:button1");
      try {
        page = (HtmlPage) button.click();
      } catch (IOException e) {
        formatter.format("Unexpected exception clicking button1: %s%n", e);
        handleTestStatus(messages);
        return;
      }

      // Now retrieve the message
      HtmlSpan messagespan = (HtmlSpan) getElementOfTypeIncludingId(page,
          "span", "testform:message");
      if (!validateExistence(messagespan.getId(), "testform:message",
          messagespan, formatter)) {
        handleTestStatus(messages);
        return;
      }

      validateAttributeSet(control, messagespan,
          new String[] { "id", "value", "name", "type", "size", "multiple" },
          formatter);

      handleTestStatus(messages);
    }
  } // END messageRenderPassthroughTest
} // END URLClient
