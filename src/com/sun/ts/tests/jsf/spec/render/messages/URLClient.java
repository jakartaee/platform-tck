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
package com.sun.ts.tests.jsf.spec.render.messages;

import java.io.PrintWriter;
import java.io.IOException;
import java.util.Formatter;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.TreeSet;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableDataCell;
import com.gargoylesoftware.htmlunit.html.HtmlUnorderedList;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;
import java.util.ArrayList;
import java.util.List;

public class URLClient extends BaseHtmlUnitClient {

  private static final String CONTEXT_ROOT = "/jsf_render_messages_web";

  private static final String SUMMARY_MESSAGE = "INFO: Summary Message";

  private static final String DETAILED_MESSAGE = "INFO: Detailed Message";

  private String firstMessage;

  private String secondMessage;

  private String testCaseName = ""; // This is only used for output messages.

  private String[] sevs = { "MESSAGES_INFO", "MESSAGES_WARN", "MESSAGES_ERROR",
      "MESSAGES_FATAL" };

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
   * @testName: messagesRenderEncodeTest
   * @assertion_ids: PENDING
   * @test_Strategy: Validate the rendering of javax.faces.Messages
   * 
   *                 case 1: - The id, showSummary, and showDetail attributes
   *                 are defined. The layout attribute is not defined.(default
   *                 layout=list) Verifiy for each message that we - render the
   *                 correct value for "showSummary". - render the correct value
   *                 for "showDetail". - that we render "id" and its value is
   *                 correct. case 2: - The styleClass attribute is defined. The
   *                 layout attribute is not defined.(default layout=list)
   *                 Verifiy that the
   *                 <ul>
   *                 element - renders "styleClass"(as class) with the expected
   *                 value.
   * 
   *                 case 3: - The infoStyle and infoClass attributes are
   *                 defined. - The layout attribute is not defined.(default
   *                 layout=list) Verifiy for each message(
   *                 <li>elment) that we - render "infoStyle"(as style) with the
   *                 expected value. - render "infoClass"(as class) with the
   *                 expected value. case 4: - The warnStyle and warnClass
   *                 attributes are defined. The layout attribute is not
   *                 defined.(default layout=list) Verifiy for each message(
   *                 <li>elment) that we - render "warnStyle"(as style) with the
   *                 expected value. - render "warnClass"(as class) with the
   *                 expected value. case 5: - The errorStyle and errorClass are
   *                 defined. The layout attribute is not defined.(default
   *                 layout=list) Verifiy that we - render "errorStyle"(as
   *                 style) with the expect value. - render "errorClass"(as
   *                 class) with expected value. case 6: - The id, fatalStyle,
   *                 and fatalClass are defined. The layout attribute is not
   *                 defined.(default layout=list) Verifiy that we - render
   *                 "fatalStyle"(as style) with the expect value. - render
   *                 "fatalClass"(as class) with expected value. case 7: -
   *                 Validate that we can have all message severity style
   *                 attributes set and we can get all severity messages to
   *                 adhere to the correct style attribute. - The layout
   *                 attribute is not defined.(default layout=list) Style
   *                 Attributes: -infoStyle -warnStyle -errorStyle -fatalStyle
   *                 case 8: - Validate that we can have all message severity
   *                 class attributes set and we can get all severity messages
   *                 to adhere to the correct class attribute. - The layout
   *                 attribute is not defined.(default layout=list) Class
   *                 Attributes: -infoClass -warnClass -errorClass -fatalClass
   * 
   *                 case 9: - Validate that we render only the message summary
   *                 for both messages. - The layout attribute is not
   *                 defined.(default layout=list) -showSummary = true
   *                 -showDetail = false
   * 
   * 
   *                 case 10: - Validate that we render only the message detail.
   *                 - The layout attribute is not defined.(default layout=list)
   *                 -showSummary = false -showDetail = true
   * 
   *                 case 11: - Validate that we do not render a message at all.
   *                 - The layout attribute is not defined.(default layout=list)
   *                 -showSummary = false -showDetail = false case 12: - The id,
   *                 showSummary, and showDetail attributes are defined. The
   *                 layout attribute is defined.(layout=table) Verifiy for each
   *                 message(
   *                 <td>element) that we - render the correct value for
   *                 "showSummary". - render the correct value for "showDetail".
   *                 - that we render "id" and its value is correct. case 13: -
   *                 The styleClass attribute is defined. The layout attribute
   *                 is defined.(layout=table) Verifiy that the
   *                 <table>
   *                 element - renders "styleClass"(as class) with the expected
   *                 value. case 14: - Validate that we render only the message
   *                 summary for both messages. - The layout attribute
   *                 defined.(default layout=table) -showSummary = true
   *                 -showDetail = false
   * 
   * 
   *                 case 15: - Validate that we render only the message detail.
   *                 - The layout attribute defined.(default layout=table)
   *                 -showSummary = false -showDetail = true
   * 
   *                 case 16: - Validate that we do not render a message at all.
   *                 - The layout attribute defined.(default layout=table)
   *                 -showSummary = false -showDetail = false
   * 
   * @since 1.2
   */
  public void messagesRenderEncodeTest() throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    List<HtmlPage> pages = new ArrayList<HtmlPage>();
    pages.add(getPage(CONTEXT_ROOT + "/faces/encodetest.jsp"));
    pages.add(getPage(CONTEXT_ROOT + "/faces/encodetest_facelet.xhtml"));

    for (HtmlPage page : pages) {
      // ---------------------------------------------------------------Case
      // 1:
      this.testCaseName = "Case_1";
      // Generate the messages
      HtmlInput button1 = (HtmlInput) getElementOfTypeIncludingId(page, "input",
          "form1:button1");
      HtmlInput clientid = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form1:id1");
      HtmlInput severity = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form1:input1");

      try {
        clientid.setValueAttribute("form1:input1");
        severity.setValueAttribute("MESSAGES_INFO");
        page = (HtmlPage) button1.click();
      } catch (IOException e) {
        formatter.format("Unexpected exception clicking button1: %s%n", e);
        handleTestStatus(messages);
        return;
      }

      // Get HtlmUnorderedList element by id and test for validity.
      HtmlUnorderedList ulOne = (HtmlUnorderedList) getElementOfTypeIncludingId(
          page, "ul", "form1:message1");
      if (!validateExistence(ulOne.getId(), "form1:message1", ulOne,
          formatter)) {
        handleTestStatus(messages);
        return;
      }

      // Array of expected test messages.
      firstMessage = SUMMARY_MESSAGE + "_One " + DETAILED_MESSAGE + "_One";
      secondMessage = SUMMARY_MESSAGE + "_Two " + DETAILED_MESSAGE + "_Two";

      // Tree of expected messages
      TreeSet exMessages = new TreeSet();
      exMessages.add(firstMessage);
      exMessages.add(secondMessage);

      // Tree of actual messages
      Iterator liInt = ulOne.getChildElements().iterator();
      TreeSet aMessages = new TreeSet();

      while (liInt.hasNext()) {
        HtmlListItem li = (HtmlListItem) liInt.next();
        aMessages.add(li.asText());
      }

      if (aMessages.size() > 2) {
        formatter.format(testCaseName + ": Unexpected value for "
            + "Messages! %n" + "Recieved more then two messages");
      }

      // Test the two Trees for equality
      if (!(exMessages.equals(aMessages))) {
        String actual = null;
        switch (aMessages.size()) {
        case 0:
          actual = "";
          break;
        case 1:
          actual = (String) aMessages.first();
          break;
        case 2:
          actual = (String) aMessages.first() + " and %n " + aMessages.last();
          break;
        }
        formatter.format(testCaseName + ": Unexpected value for "
            + "Messages! %n" + "Expected Two Messages: " + firstMessage
            + " and %n" + secondMessage + "%n" + "Received: " + actual + "%n");
      }

      // ---------------------------------------------------------------Case
      // 2:
      this.testCaseName = "Case_2";
      // Generate the messages
      HtmlInput button2 = (HtmlInput) getElementOfTypeIncludingId(page, "input",
          "form2:button2");
      HtmlInput clientid2 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form2:id2");
      HtmlInput severity2 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form2:input2");

      try {
        clientid2.setValueAttribute("form2:input2");
        severity2.setValueAttribute("MESSAGES_INFO");
        page = (HtmlPage) button2.click();
      } catch (IOException e) {
        formatter.format("Unexpected exception clicking button2: %s%n", e);
        handleTestStatus(messages);
        return;
      }

      // Get HtlmUnorderedList element by id and test for validity.
      HtmlUnorderedList ulTwo = (HtmlUnorderedList) getElementOfTypeIncludingId(
          page, "ul", "form2:message2");
      if (!validateExistence(ulTwo.getId(), "form2:message2", ulTwo,
          formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!(ulTwo.getAttribute("class").equals("underline"))) {
        formatter.format(testCaseName + ": Unexpected value for class "
            + "attribute! %n" + "Expected: underline %n" + "Received: "
            + ulTwo.getAttribute("class") + "%n");
      }

      // ---------------------------------------------------------------Case
      // 3:
      this.testCaseName = "Case_3";
      // Generate the messages
      HtmlInput button3 = (HtmlInput) getElementOfTypeIncludingId(page, "input",
          "form3:button3");
      HtmlInput clientid3 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form3:id3");
      HtmlInput severity3 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form3:input3");

      try {
        clientid3.setValueAttribute("form3:input3");
        severity3.setValueAttribute("MESSAGES_INFO");
        page = (HtmlPage) button3.click();
      } catch (IOException e) {
        formatter.format("Unexpected exception clicking button3: %s%n", e);
        handleTestStatus(messages);
        return;
      }

      // Get HtlmUnorderedList element by id and test for validity.
      HtmlUnorderedList ulThree = (HtmlUnorderedList) getElementOfTypeIncludingId(
          page, "ul", "form3:message3");
      if (!validateExistence(ulThree.getId(), "form3:message3", ulThree,
          formatter)) {
        handleTestStatus(messages);
        return;
      }

      // Get the messages
      Iterator liIntThree = ulThree.getChildElements().iterator();

      while (liIntThree.hasNext()) {
        HtmlListItem li = (HtmlListItem) liIntThree.next();
        String expStyle = "Color: blue;";
        String expClass = "underline";

        if (!(li.getAttribute("style").equals(expStyle))) {
          formatter.format("Unexpected value " + testCaseName + ", for "
              + "style attribute! %n" + "Expected: " + expStyle + "%n"
              + "Received: " + li.getAttribute("style") + "%n");
        }

        if (!(li.getAttribute("class").equals(expClass))) {
          formatter.format(testCaseName + ": Unexpected value for "
              + "class attribute! %n" + "Expected: " + expClass + "%n"
              + "Received: " + li.getAttribute("class") + "%n");
        }
      }

      // ---------------------------------------------------------------Case
      // 4:
      this.testCaseName = "Case_4";
      // Generate the message
      HtmlInput button4 = (HtmlInput) getElementOfTypeIncludingId(page, "input",
          "form4:button4");
      HtmlInput clientid4 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form4:id4");
      HtmlInput severity4 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form4:input4");

      try {
        clientid4.setValueAttribute("form4:input4");
        severity4.setValueAttribute("MESSAGES_WARN");
        page = (HtmlPage) button4.click();
      } catch (IOException e) {
        formatter.format("Unexpected exception clicking button4: %s%n", e);
        handleTestStatus(messages);
        return;
      }

      // Get HtlmUnorderedList element by id and test for validity.
      HtmlUnorderedList ulFour = (HtmlUnorderedList) getElementOfTypeIncludingId(
          page, "ul", "form4:message4");
      if (!validateExistence(ulFour.getId(), "form4:message4", ulFour,
          formatter)) {
        handleTestStatus(messages);
        return;
      }

      Iterator liIntFour = ulFour.getChildElements().iterator();

      while (liIntFour.hasNext()) {
        HtmlListItem li = (HtmlListItem) liIntFour.next();
        String expStyle = "Color: green;";
        String expClass = "underline";

        if (!(li.getAttribute("style").equals(expStyle))) {
          formatter.format(testCaseName + ": Unexpected value for "
              + "style attribute! %n" + "Expected: " + expStyle + "%n"
              + "Received: " + li.getAttribute("style") + "%n");
        }

        if (!(li.getAttribute("class").equals(expClass))) {
          formatter.format(testCaseName + ": Unexpected value for "
              + "class attribute! %n" + "Expected: " + expClass + "%n"
              + "Received: " + li.getAttribute("class") + "%n");
        }
      }

      // ---------------------------------------------------------------Case
      // 5:
      this.testCaseName = "Case_5";
      // Generate the message
      HtmlInput button5 = (HtmlInput) getElementOfTypeIncludingId(page, "input",
          "form5:button5");
      HtmlInput clientid5 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form5:id5");
      HtmlInput severity5 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form5:input5");

      try {
        clientid5.setValueAttribute("form5:input5");
        severity5.setValueAttribute("MESSAGES_ERROR");
        page = (HtmlPage) button5.click();
      } catch (IOException e) {
        formatter.format("Unexpected exception clicking button5: %s%n", e);
        handleTestStatus(messages);
        return;
      }

      // Get HtlmUnorderedList element by id and test for validity.
      HtmlUnorderedList ulFive = (HtmlUnorderedList) getElementOfTypeIncludingId(
          page, "ul", "form5:message5");
      if (!validateExistence(ulFive.getId(), "form5:message5", ulFive,
          formatter)) {
        handleTestStatus(messages);
        return;
      }

      Iterator liIntFive = ulFive.getChildElements().iterator();

      while (liIntFive.hasNext()) {
        HtmlListItem li = (HtmlListItem) liIntFive.next();
        String expStyle = "Color: yellow;";
        String expClass = "underline";

        if (!(li.getAttribute("style").equals(expStyle))) {
          formatter.format(testCaseName + ": Unexpected value for "
              + "style attribute! %n" + "Expected: " + expStyle + "%n"
              + "Received: " + li.getAttribute("style") + "%n");
        }

        if (!(li.getAttribute("class").equals(expClass))) {
          formatter.format(testCaseName + ": Unexpected value for "
              + "class attribute! %n" + "Expected: " + expClass + "%n"
              + "Received: " + li.getAttribute("class") + "%n");
        }
      }

      // ---------------------------------------------------------------Case
      // 6:
      this.testCaseName = "Case_6";
      // Generate the message
      HtmlInput button6 = (HtmlInput) getElementOfTypeIncludingId(page, "input",
          "form6:button6");
      HtmlInput clientid6 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form6:id6");
      HtmlInput severity6 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form6:input6");

      try {
        clientid6.setValueAttribute("form6:input6");
        severity6.setValueAttribute("MESSAGES_FATAL");
        page = (HtmlPage) button6.click();
      } catch (IOException e) {
        formatter.format("Unexpected exception clicking button6: %s%n", e);
        handleTestStatus(messages);
        return;
      }

      HtmlUnorderedList ulSix = (HtmlUnorderedList) getElementOfTypeIncludingId(
          page, "ul", "form6:message6");
      if (!validateExistence(ulSix.getId(), "form6:message6", ulSix,
          formatter)) {
        handleTestStatus(messages);
        return;
      }

      Iterator liIntSix = ulSix.getChildElements().iterator();

      while (liIntSix.hasNext()) {
        HtmlListItem li = (HtmlListItem) liIntSix.next();
        String expStyle = "Color: red;";
        String expClass = "underline";

        if (!(li.getAttribute("style").equals(expStyle))) {
          formatter.format(testCaseName + ": Unexpected value for "
              + "style attribute! %n" + "Expected: " + expStyle + "%n"
              + "Received: " + li.getAttribute("style") + "%n");
        }

        if (!(li.getAttribute("class").equals(expClass))) {
          formatter.format(testCaseName + ": Unexpected value for "
              + "class attribute! %n" + "Expected: " + expClass + "%n"
              + "Received: " + li.getAttribute("class") + "%n");
        }
      }

      // ---------------------------------------------------------------Case
      // 7:
      this.testCaseName = "Case_7";
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
        if (sevLevel.equals("MESSAGES_INFO")) {
          expectedStyle = "Color: blue;";
        } else if (sevLevel.equals("MESSAGES_WARN")) {
          expectedStyle = "Color: green;";
        } else if (sevLevel.equals("MESSAGES_ERROR")) {
          expectedStyle = "Color: yellow;";
        } else if (sevLevel.equals("MESSAGES_FATAL")) {
          expectedStyle = "Color: red;";
        }

        try {
          clientid7.setValueAttribute("form7:severity7");
          severity7.setValueAttribute(sevLevel);
          page = (HtmlPage) button7.click();
        } catch (IOException e) {
          formatter.format("Unexpected exception clicking button7: " + "%s%n",
              e);
          handleTestStatus(messages);
          return;
        }
        HtmlUnorderedList ulSeven = (HtmlUnorderedList) getElementOfTypeIncludingId(
            page, "ul", "form7:message7");
        if (!validateExistence(ulSeven.getId(), "form7:message7", ulSeven,
            formatter)) {
          handleTestStatus(messages);
          return;
        }

        Iterator liIntSeven = ulSeven.getChildElements().iterator();

        while (liIntSeven.hasNext()) {
          HtmlListItem li = (HtmlListItem) liIntSeven.next();

          if (!(li.getAttribute("style").equals(expectedStyle))) {
            formatter.format(testCaseName + ": Unexpected value for "
                + "style attribute! %n" + "Expected: " + expectedStyle + "%n"
                + "Received: " + li.getAttribute("style") + "%n");
          }
        }
      }

      // --------------------------------------------------------------
      // Case 8:
      this.testCaseName = "Case_8";
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
        if (sevLevel.equals("MESSAGES_INFO")) {
          expectedClass = "class_info";
        } else if (sevLevel.equals("MESSAGES_WARN")) {
          expectedClass = "class_warn";
        } else if (sevLevel.equals("MESSAGES_ERROR")) {
          expectedClass = "class_error";
        } else if (sevLevel.equals("MESSAGES_FATAL")) {
          expectedClass = "class_fatal";
        }

        try {
          clientid8.setValueAttribute("form8:severity8");
          severity8.setValueAttribute(sevLevel);
          page = (HtmlPage) button8.click();
        } catch (IOException e) {
          formatter.format("Unexpected exception clicking button8: %s%n", e);
          handleTestStatus(messages);
          return;
        }

        HtmlUnorderedList ulEight = (HtmlUnorderedList) getElementOfTypeIncludingId(
            page, "ul", "form8:message8");
        if (!validateExistence(ulEight.getId(), "form8:message8", ulEight,
            formatter)) {
          handleTestStatus(messages);
          return;
        }

        Iterator liIntEight = ulEight.getChildElements().iterator();

        while (liIntEight.hasNext()) {
          HtmlListItem li = (HtmlListItem) liIntEight.next();

          if (!(li.getAttribute("class").equals(expectedClass))) {
            formatter.format(testCaseName + ": Unexpected value for "
                + "class attribute! %n" + "Expected: " + expectedClass + "%n"
                + "Received: " + li.getAttribute("style") + "%n");
          }
        }
      }

      // --------------------------------------------------------------
      // Case 9:
      this.testCaseName = "Case_9";
      // Generate message.(summary = true, detail = false)
      HtmlInput button9 = (HtmlInput) getElementOfTypeIncludingId(page, "input",
          "form9:button9");
      HtmlInput clientid9 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form9:id9");
      HtmlInput severity9 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form9:severity9");

      try {
        clientid9.setValueAttribute("form9:severity9");
        severity9.setValueAttribute("MESSAGES_INFO");
        page = (HtmlPage) button9.click();
      } catch (IOException e) {
        formatter.format("Unexpected exception clicking button9: %s%n", e);
        handleTestStatus(messages);
        return;
      }

      // Get HtlmUnorderedList element by id and test for validity.
      HtmlUnorderedList ulNine = (HtmlUnorderedList) getElementOfTypeIncludingId(
          page, "ul", "form9:message9");
      if (!validateExistence(ulNine.getId(), "form9:message9", ulNine,
          formatter)) {
        handleTestStatus(messages);
        return;
      }

      // Array of expected test messages.
      firstMessage = SUMMARY_MESSAGE + "_One";
      secondMessage = SUMMARY_MESSAGE + "_Two";

      // Tree of expected messages
      TreeSet expNine = new TreeSet();
      expNine.add(firstMessage);
      expNine.add(secondMessage);

      // Tree of actual messages
      Iterator liIntNine = ulNine.getChildElements().iterator();
      TreeSet actNine = new TreeSet();

      while (liIntNine.hasNext()) {
        HtmlListItem li = (HtmlListItem) liIntNine.next();
        actNine.add(li.asText());
      }

      if (expNine.size() > 2) {
        formatter.format(testCaseName + ": Unexpected value for "
            + "Messages! %n" + "Recieved more then two messages");
      }

      // Test the two Trees for equality
      if (!(expNine.equals(actNine))) {
        String actual = null;
        switch (aMessages.size()) {
        case 0:
          actual = "";
          break;
        case 1:
          actual = (String) actNine.first();
          break;
        case 2:
          actual = (String) actNine.first() + " and %n " + actNine.last();
          break;
        }
        formatter.format(testCaseName + ": Unexpected value for "
            + "Messages! %n" + "Expected Two Messages: " + firstMessage
            + " and %n" + secondMessage + "%n" + "Received: " + actual + "%n");
      }

      // -------------------------------------------------------------
      // Case 10:
      this.testCaseName = "Case_10";
      // Generate a message.(summary = false, detail = true)
      HtmlInput button10 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form10:button10");
      HtmlInput clientid10 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form10:id10");
      HtmlInput severity10 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form10:severity10");

      try {
        clientid10.setValueAttribute("form10:severity10");
        severity10.setValueAttribute("MESSAGES_INFO");
        page = (HtmlPage) button10.click();
      } catch (IOException e) {
        formatter.format("Unexpected exception clicking button10: %s%n", e);
        handleTestStatus(messages);
        return;
      }

      // Get HtlmUnorderedList element by id and test for validity.
      HtmlUnorderedList ulTen = (HtmlUnorderedList) getElementOfTypeIncludingId(
          page, "ul", "form10:message10");
      if (!validateExistence(ulTen.getId(), "form10:message10", ulTen,
          formatter)) {
        handleTestStatus(messages);
        return;
      }
      // Array of expected test messages.
      firstMessage = DETAILED_MESSAGE + "_One";
      secondMessage = DETAILED_MESSAGE + "_Two";

      // Tree of expected messages
      TreeSet expTen = new TreeSet();
      expTen.add(firstMessage);
      expTen.add(secondMessage);

      // Tree of actual messages
      Iterator liIntTen = ulTen.getChildElements().iterator();
      TreeSet actTen = new TreeSet();

      while (liIntTen.hasNext()) {
        HtmlListItem li = (HtmlListItem) liIntTen.next();
        actTen.add(li.asText());
      }

      if (expTen.size() > 2) {
        formatter.format(testCaseName + ": Unexpected value for "
            + "Messages! %n" + "Recieved more then two messages");
      }

      // Test the two Trees for equality
      if (!(expTen.equals(actTen))) {
        String actual = null;
        switch (actTen.size()) {
        case 0:
          actual = "";
          break;
        case 1:
          actual = (String) actTen.first();
          break;
        case 2:
          actual = (String) actTen.first() + " and %n " + actTen.last();
          break;
        }
        formatter.format(testCaseName + ": Unexpected value for "
            + "Messages! %n" + "Expected Two Messages: " + firstMessage
            + " and %n" + secondMessage + "%n" + "Received: " + actual + "%n");
      }

      // -------------------------------------------------------------
      // Case 11:
      this.testCaseName = "Case_11";
      // Generate a message.(summary = false, detail = false)
      HtmlInput button11 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form11:button11");
      HtmlInput clientid11 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form11:id11");
      HtmlInput severity11 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form11:severity11");

      try {
        clientid11.setValueAttribute("form11:severity11");
        severity11.setValueAttribute("MESSAGES_INFO");
        page = (HtmlPage) button11.click();
      } catch (IOException e) {
        formatter.format("Unexpected exception clicking button11: %s%n", e);
        handleTestStatus(messages);
        return;
      }

      // Get HtlmUnorderedList element by id and test for validity.
      HtmlUnorderedList ulEleven = (HtmlUnorderedList) getElementOfTypeIncludingId(
          page, "ul", "form11:message11");
      if (!validateExistence(ulEleven.getId(), "form11:message11", ulEleven,
          formatter)) {
        handleTestStatus(messages);
        return;
      }
      // Array of expected test messages.
      firstMessage = "";
      secondMessage = "";

      // Tree of expected messages
      TreeSet expEleven = new TreeSet();
      expEleven.add(firstMessage);
      expEleven.add(secondMessage);

      // Tree of actual messages
      Iterator liIntEleven = ulEleven.getChildElements().iterator();
      TreeSet actEleven = new TreeSet();

      while (liIntEleven.hasNext()) {
        HtmlListItem li = (HtmlListItem) liIntEleven.next();
        actEleven.add(li.asText());
      }

      if (expEleven.size() > 2) {
        formatter.format(testCaseName + ": Unexpected value for "
            + "Messages! %n" + "Recieved more then two messages");
      }

      // Test the two Trees for equality
      if (!(expEleven.equals(actEleven))) {
        String actual = null;
        switch (actEleven.size()) {
        case 0:
          actual = "";
          break;
        case 1:
          actual = (String) actEleven.first();
          break;
        case 2:
          actual = (String) actEleven.first() + " and %n " + actEleven.last();
          break;
        }
        formatter.format(testCaseName + ": Unexpected value for "
            + "Messages! %n" + "Expected Two Messages: " + firstMessage
            + " and %n" + secondMessage + "%n" + "Received: " + actual + "%n");
      }

      // -------------------------------------------------------------
      // Case 12:
      this.testCaseName = "Case_12";
      // Generate the messages
      HtmlInput button12 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form12:button12");
      HtmlInput clientid12 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form12:id12");
      HtmlInput severity12 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form12:input12");

      try {
        clientid12.setValueAttribute("form12:input12");
        severity12.setValueAttribute("MESSAGES_INFO");
        page = (HtmlPage) button12.click();
      } catch (IOException e) {
        formatter.format("Unexpected exception clicking button12: %s%n", e);
        handleTestStatus(messages);
        return;
      }

      // Get HtlmTable element by id and test for validity.
      HtmlTable tableOne = (HtmlTable) getElementOfTypeIncludingId(page,
          "table", "form12:message12");
      if (!validateExistence(tableOne.getId(), "form12:message12", tableOne,
          formatter)) {
        handleTestStatus(messages);
        return;
      }

      // Array of expected test messages.
      firstMessage = SUMMARY_MESSAGE + "_One " + DETAILED_MESSAGE + "_One";
      secondMessage = SUMMARY_MESSAGE + "_Two " + DETAILED_MESSAGE + "_Two";

      // Tree of expected messages
      TreeSet expTwelve = new TreeSet();
      expTwelve.add(firstMessage);
      expTwelve.add(secondMessage);

      // Tree of actual messages
      TreeSet actualTwelve = new TreeSet();

      for (HtmlElement element : tableOne.getHtmlElementDescendants()) {
        // Only add the html <td> elements to the list.
        if ("td".equals(((HtmlElement) element).getTagName())) {
          HtmlTableDataCell datacell = (HtmlTableDataCell) element;
          actualTwelve.add(datacell.asText());
        }
      }

      // Make sure that the list is not greater the the expected message
      // list.
      if (actualTwelve.size() > 2) {
        formatter.format(testCaseName + ": Unexpected value for "
            + "Messages! %n" + "Recieved more then two messages");
        handleTestStatus(messages);
        return;
      }

      // Test the two Trees for equality
      if (!(expTwelve.equals(actualTwelve))) {
        String actual = "";
        switch (actualTwelve.size()) {
        case 0:
          actual = "";
          break;
        case 1:
          actual = (String) actualTwelve.first();
          break;
        case 2:
          actual = (String) actualTwelve.first() + " and %n "
              + actualTwelve.last();
          break;
        }
        formatter.format(testCaseName + ": Unexpected value for "
            + "Messages! %n" + "Expected Two Messages: " + firstMessage
            + " and %n" + secondMessage + "%n" + "Received: " + actual + "%n");
      }

      // -------------------------------------------------------------
      // Case 13:
      this.testCaseName = "Case_13";
      // Generate the messages
      HtmlInput button13 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form13:button13");
      HtmlInput clientid13 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form13:id13");
      HtmlInput severity13 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form13:input13");

      try {
        clientid13.setValueAttribute("form2:input2");
        severity13.setValueAttribute("MESSAGES_INFO");
        page = (HtmlPage) button13.click();
      } catch (IOException e) {
        formatter.format("Unexpected exception clicking button13: %s%n", e);
        handleTestStatus(messages);
        return;
      }

      // Get HtlmTable element by id and test for validity.
      HtmlTable tableTwo = (HtmlTable) getElementOfTypeIncludingId(page,
          "table", "form13:message13");
      if (!validateExistence(tableTwo.getId(), "form13:message13", tableTwo,
          formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!(tableTwo.getAttribute("class").equals("underline"))) {
        formatter.format(testCaseName + ": Unexpected value for class "
            + "attribute! %n" + "Expected: underline %n" + "Received: "
            + tableTwo.getAttribute("class") + "%n");
      }

      // -------------------------------------------------------------
      // Case 14:
      this.testCaseName = "Case_14";
      // Generate the messages
      HtmlInput button14 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form14:button14");
      HtmlInput clientid14 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form14:id14");
      HtmlInput severity14 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form14:input14");

      try {
        clientid14.setValueAttribute("form14:input14");
        severity14.setValueAttribute("MESSAGES_INFO");
        page = (HtmlPage) button14.click();
      } catch (IOException e) {
        formatter.format("Unexpected exception clicking button14: %s%n", e);
        handleTestStatus(messages);
        return;
      }

      // Get HtlmTable element by id and test for validity.
      HtmlTable tableFourteen = (HtmlTable) getElementOfTypeIncludingId(page,
          "table", "form14:message14");
      if (!validateExistence(tableFourteen.getId(), "form14:message14",
          tableFourteen, formatter)) {
        handleTestStatus(messages);
        return;
      }

      // Array of expected test messages.
      firstMessage = SUMMARY_MESSAGE + "_One";
      secondMessage = SUMMARY_MESSAGE + "_Two";

      // Tree of expected messages
      TreeSet expFourteen = new TreeSet();
      expFourteen.add(firstMessage);
      expFourteen.add(secondMessage);

      // Tree of actual messages
      TreeSet actualFourteen = new TreeSet();
      for (HtmlElement element : tableFourteen.getHtmlElementDescendants()) {
        // Only add the html <td> elements to the list.
        if ("td".equals(((HtmlElement) element).getTagName())) {
          HtmlTableDataCell datacell = (HtmlTableDataCell) element;
          actualFourteen.add(datacell.asText());
        }
      }

      // Make sure that the list is not greater the the expected message
      // list.
      if (actualFourteen.size() > 2) {
        formatter.format(testCaseName + ": Unexpected value for "
            + "Messages! %n" + "Recieved more then two messages");
        handleTestStatus(messages);
        return;
      }

      // Test the two Trees for equality
      if (!(expFourteen.equals(actualFourteen))) {
        String actual = "";
        switch (actualFourteen.size()) {
        case 0:
          actual = "";
          break;
        case 1:
          actual = (String) actualFourteen.first();
          break;
        case 2:
          actual = (String) actualFourteen.first() + " and %n "
              + actualFourteen.last();
          break;
        }
        formatter.format(testCaseName + ": Unexpected value for "
            + "Messages! %n" + "Expected Two Messages: " + firstMessage
            + " and %n" + secondMessage + "%n" + "Received: " + actual + "%n");
      }

      // -------------------------------------------------------------
      // Case 15:
      this.testCaseName = "Case_15";
      // Generate the messages
      HtmlInput button15 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form15:button15");
      HtmlInput clientid15 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form15:id15");
      HtmlInput severity15 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form15:input15");

      try {
        clientid15.setValueAttribute("form15:input15");
        severity15.setValueAttribute("MESSAGES_INFO");
        page = (HtmlPage) button15.click();
      } catch (IOException e) {
        formatter.format("Unexpected exception clicking button15: %s%n", e);
        handleTestStatus(messages);
        return;
      }

      // Get HtlmTable element by id and test for validity.
      HtmlTable tableFifteen = (HtmlTable) getElementOfTypeIncludingId(page,
          "table", "form15:message15");
      if (!validateExistence(tableFifteen.getId(), "form15:message15",
          tableFifteen, formatter)) {
        handleTestStatus(messages);
        return;
      }

      // Array of expected test messages.
      firstMessage = DETAILED_MESSAGE + "_One";
      secondMessage = DETAILED_MESSAGE + "_Two";

      // Tree of expected messages
      TreeSet expFifteen = new TreeSet();
      expFifteen.add(firstMessage);
      expFifteen.add(secondMessage);

      TreeSet actualFifteen = new TreeSet();

      // while (fifTeenTRInt.hasNext()) {
      for (HtmlElement element : tableFifteen.getHtmlElementDescendants()) {
        // Only add the html <td> elements to the list.
        if ("td".equals(((HtmlElement) element).getTagName())) {
          HtmlTableDataCell datacell = (HtmlTableDataCell) element;
          actualFifteen.add(datacell.asText());
        }
      }

      // Make sure that the list is not greater the the expected message
      // list.
      if (actualFifteen.size() > 2) {
        formatter.format(testCaseName + ": Unexpected value for "
            + "Messages! %n" + "Recieved more then two messages");
        handleTestStatus(messages);
        return;
      }

      // Test the two Trees for equality
      if (!(expFifteen.equals(actualFifteen))) {
        String actual = "";
        switch (actualFifteen.size()) {
        case 0:
          actual = "";
          break;
        case 1:
          actual = (String) actualFifteen.first();
          break;
        case 2:
          actual = (String) actualFifteen.first() + " and %n "
              + actualFourteen.last();
          break;
        }
        formatter.format(testCaseName + ": Unexpected value for "
            + "Messages! %n" + "Expected Two Messages: " + firstMessage
            + " and %n" + secondMessage + "%n" + "Received: " + actual + "%n");
      }

      // -------------------------------------------------------------
      // Case 16:
      this.testCaseName = "Case_16";
      // Generate the messages
      HtmlInput button16 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form16:button16");
      HtmlInput clientid16 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form16:id16");
      HtmlInput severity16 = (HtmlInput) getElementOfTypeIncludingId(page,
          "input", "form16:input16");

      try {
        clientid16.setValueAttribute("form16:input16");
        severity16.setValueAttribute("MESSAGES_INFO");
        page = (HtmlPage) button16.click();
      } catch (IOException e) {
        formatter.format("Unexpected exception clicking button16: %s%n", e);
        handleTestStatus(messages);
        return;
      }

      // Get HtlmTable element by id and test for validity.
      HtmlTable tableSixteen = (HtmlTable) getElementOfTypeIncludingId(page,
          "table", "form16:message16");
      if (!validateExistence(tableSixteen.getId(), "form16:message16",
          tableSixteen, formatter)) {
        handleTestStatus(messages);
        return;
      }

      // Array of expected test messages.
      firstMessage = "";
      secondMessage = "";

      // Tree of expected messages
      TreeSet expSixteen = new TreeSet();
      expSixteen.add(firstMessage);
      expSixteen.add(secondMessage);

      // Tree of actual messages
      TreeSet actualSixteen = new TreeSet();
      for (HtmlElement element : tableSixteen.getHtmlElementDescendants()) {
        // Only add the html <td> elements to the list.
        if ("td".equals(((HtmlElement) element).getTagName())) {
          HtmlTableDataCell datacell = (HtmlTableDataCell) element;
          actualSixteen.add(datacell.asText());
        }
      }

      // Make sure that the list is not greater the the expected message
      // list.
      if (actualSixteen.size() > 2) {
        formatter.format(testCaseName + ": Unexpected value for "
            + "Messages! %n" + "Recieved more then two messages");
        handleTestStatus(messages);
        return;
      }

      // Test the two Trees for equality
      if (!(expSixteen.equals(actualSixteen))) {
        String actual = "";
        switch (actualSixteen.size()) {
        case 0:
          actual = "";
          break;
        case 1:
          actual = (String) actualSixteen.first();
          break;
        case 2:
          actual = (String) actualSixteen.first() + " and %n "
              + actualSixteen.last();
          break;
        }
        formatter.format(testCaseName + ": Unexpected value for "
            + "Messages! %n" + "Expected Two Messages: " + firstMessage
            + " and %n" + secondMessage + "%n" + "Received: " + actual + "%n");
      }

      handleTestStatus(messages);
    }
  }// END messagesRenderEncodeTest

  /**
   * @testName: messagesRenderPassthroughTest
   * @assertion_ids: PENDING
   * @test_Strategy: Ensure the attributes that are considered passthrough by
   *                 the specification are rendered as is.
   * 
   * @since 1.2
   */
  public void messagesRenderPassthroughTest() throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    TreeMap<String, String> control = new TreeMap<String, String>();
    control.put("dir", "LTR");
    control.put("lang", "en");
    control.put("style", "Color: red;");
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

      // Generate the message.
      HtmlInput button = (HtmlInput) getElementOfTypeIncludingId(page, "input",
          "testform:button1");
      try {
        page = (HtmlPage) button.click();
      } catch (IOException e) {
        formatter.format("Unexpected exception clicking button1: " + "%s%n", e);
        handleTestStatus(messages);
        return;
      }

      // Now retrieve the message
      HtmlUnorderedList ul = (HtmlUnorderedList) getElementOfTypeIncludingId(
          page, "ul", "testform:messages");
      if (!validateExistence(ul.getId(), "ul", ul, formatter)) {
        handleTestStatus(messages);
        return;
      }

      validateAttributeSet(control, ul,
          new String[] { "id", "value", "name", "type", "size", "multiple" },
          formatter);

      handleTestStatus(messages);
    }
  } // END messagesRenderPassthroughTest
} // END URLClient
