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
package com.sun.ts.tests.jsf.common.client;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.TreeMap;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlLabel;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TestUtil;

/**
 * <p>
 * This class provides convenience methods for using HtmlUnit to aid in
 * validating JSF rendered responses.
 * </p>
 */
public class BaseHtmlUnitClient extends EETest {

  static {
    if (TestUtil.traceflag) {
      System.setProperty("org.apache.commons.logging.Log",
          "com.sun.ts.tests.common.webclient.log.WebLog");
      System.setProperty(
          "org.apache.commons.logging.simplelog.log.httpclient.wire", "debug");
    }
  }

  private static final String WEB_SERVER_HOST = "webServerHost";

  private static final String WEB_SERVER_PORT = "webServerPort";

  private WebClient webClient;

  protected String hostName;

  protected int port;

  protected String urlPrefix;

  protected boolean throwExceptionOnFailingStatusCode;

  public void setup(String[] args, Properties p) throws Fault {

    String hostName = p.getProperty(WEB_SERVER_HOST).trim();
    String port = p.getProperty(WEB_SERVER_PORT).trim();

    if (hostName != null && hostName.length() > 0) {
      this.hostName = hostName;
    } else {
      throw new Fault("'webServerHost' not specified in the ts.jte");
    }

    if (port != null && port.length() > 0) {
      this.port = Integer.parseInt(port);
    } else {
      throw new Fault("'webServerPort' not specified in the ts.jte");
    }

    urlPrefix = "http://" + hostName + ':' + port;

    TestUtil.logMsg("Test setup OK");

  } // END setup

  /**
   * <code>cleanup</code> is called by the test harness to cleanup after text
   * execution
   * 
   * @throws Fault
   *           if an error occurs
   */
  public void cleanup() throws Fault {
    TestUtil.logMsg("Test cleanup OK");
  }

  protected HtmlPage getPage(String path) throws Fault {

    return getPage(new WebClient(), path);

  } // END getPage

  protected HtmlPage getPage(WebClient client, String path) throws Fault {
    webClient = client;

    if (!throwExceptionOnFailingStatusCode) {
      client.getOptions().setThrowExceptionOnFailingStatusCode(false);
    } else {
      client.getOptions().setThrowExceptionOnFailingStatusCode(true);
    }

    TestUtil.logMsg("setThrowExceptionOnFailingStatusCode is set to: "
        + webClient.getOptions().isThrowExceptionOnFailingStatusCode());

    try {
      return (HtmlPage) client.getPage(new URL(urlPrefix + path));

    } catch (Exception e) {
      throw new Fault(e);
    }

  } // END getPage

  /**
   * <p>
   * Added to compensate for changes in the HtmlUnit 1.4 API.
   * </p>
   * 
   * @see #getAllElementsOfGivenClass(com.gargoylesoftware.htmlunit.html.HtmlElement,
   *      java.util.List, Class)
   */
  protected List getAllElementsOfGivenClass(HtmlPage root,
      List<HtmlElement> list, Class matchClass) {

    return getAllElementsOfGivenClass(root.getDocumentElement(), list,
        matchClass);

  }

  protected List getAllElementsOfGivenClass(HtmlElement root,
      List<HtmlElement> list, Class matchClass) {
    List<HtmlElement> allElements;

    if (null == root) {
      return list;
    }

    allElements = (null == list) ? new ArrayList<HtmlElement>() : list;

    for (Iterator<HtmlElement> i = root.getHtmlElementDescendants()
        .iterator(); i.hasNext();) {
      getAllElementsOfGivenClass(i.next(), allElements, matchClass);
    }

    if (matchClass.isInstance(root)) {
      if (!allElements.contains(root)) {
        allElements.add(root);
      }
    }
    return allElements;

  }

  protected HtmlInput getInputIncludingId(HtmlPage root, String id) {

    HtmlInput result = null;

    List list = getAllElementsOfGivenClass(root, null, HtmlInput.class);
    for (int i = 0; i < list.size(); i++) {
      result = (HtmlInput) list.get(i);
      if (-1 != result.getId().indexOf(id)) {
        break;
      }
      result = null;
    }
    return result;
  }

  /**
   * Return an @HtmlElement from the given @HtmlPage.
   * 
   * @param root
   *          - HtmlPage to search for the given element.
   * @param tagName
   *          - The HTML tagname to search for.
   * @param id
   *          - The id attribute of the tag that you want to find.
   * 
   * @return - @HtmlElement or null if not found.
   */
  protected HtmlElement getElementOfTypeIncludingId(HtmlPage root,
      String tagName, String id) {

    HtmlElement result = null;

    List list = root.getDocumentElement().getHtmlElementsByTagName(tagName);

    for (Iterator i = list.iterator(); i.hasNext();) {
      HtmlElement element = (HtmlElement) i.next();

      if (element.getId().indexOf(id) > -1) {
        result = element;
      }
    }

    return result;
  }

  /**
   * Return an @HtmlElement from the given @HtmlPage.
   * 
   * @param root
   *          - HtmlPage to search for the given element.
   * @param tagName
   *          - The HTML tagname to search for.
   * @param value
   *          - The value of the tag that you want to find.
   * 
   * @return - @HtmlElement or null if not found.
   */
  protected HtmlElement getElementOfTypeWithValue(HtmlPage root, String tagName,
      String value) {

    HtmlElement result = null;

    List<? extends HtmlElement> list = root.getDocumentElement()
        .getHtmlElementsByTagName(tagName);

    for (Object element : list) {
      HtmlElement myElement = (HtmlElement) element;

      if (myElement.getTextContent().equals(value)) {
        result = myElement;
      }
    }

    return result;
  }

  /**
   * Return an @HtmlElement from the given @HtmlPage.
   * 
   * @param root
   *          - HtmlPage to search for the given element.
   * @param tagName
   *          - The HTML tagname to search for.
   * @param id
   *          - The String to search for an ID that starts with.
   * 
   * @return - @HtmlElement or null if not found.
   */
  protected HtmlElement getElementOfTypeIdStartsWith(HtmlPage root,
      String tagName, String id) {

    HtmlElement result = null;

    List list = root.getDocumentElement().getHtmlElementsByTagName(tagName);

    for (Iterator i = list.iterator(); i.hasNext();) {
      HtmlElement element = (HtmlElement) i.next();
      if (element.getId().startsWith(id)) {
        result = element;
      }
    }

    return result;
  }

  /**
   * Return an @HtmlElement from the given @HtmlPage.
   * 
   * @param root
   *          - HtmlPage to search for the given element.
   * @param tagName
   *          - The HTML tagname to search for.
   * @param title
   *          - The title attribute of the tag that you want to find.
   * 
   * @return - @HtmlElement
   */
  protected HtmlElement getElementOfTypeIncludingTitle(HtmlPage root,
      String tagName, String title) {

    HtmlElement result = root.getDocumentElement()
        .getOneHtmlElementByAttribute(tagName, "title", title);

    return result;
  }

  /**
   * Return an @HtmlElement from the given @HtmlPage.
   * 
   * @param root
   *          - HtmlPage to search for the given element.
   * @param tagName
   *          - The HTML tagname to search for.
   * @param src
   *          - The @String to search for in the src attribute.
   * 
   * @return - The found @HtmlElement or null if not found.
   */
  protected HtmlElement getElementOfTypeIncludingSrc(HtmlPage root,
      String tagName, String src) {

    String test;
    HtmlElement result = null;

    ArrayList<HtmlElement> scripts = (ArrayList<HtmlElement>) root
        .getDocumentElement().getHtmlElementsByTagName(tagName);

    for (HtmlElement e : scripts) {
      test = e.getAttribute("src");
      if (test.contains(src)) {
        result = e;
        break;
      }
    }

    return result;
  }

  protected void handleTestStatus(StringBuilder messages) throws Fault {
    if (messages.length() > 0) {
      TestUtil.logErr("Test FAILED");
      TestUtil.logErr("--------------------------");
      String[] msgs = messages.toString().split("\n");
      for (String message : msgs) {
        TestUtil.logErr(message);
      }
      TestUtil.logErr("--------------------------");
      throw new Fault("Test FAILED.  Check messages for details.");
    } else {
      TestUtil.logMsg("Test PASSED");
    }
  }

  /**
   * Validate that the given element(element) has been Rendered.
   * 
   * @param id
   *          - The value of the id attribute for the given element.
   * @param elementName
   *          - The element name.
   * @param element
   *          - The element to validate.
   * @param formatter
   *          - used for messages.
   * @return
   */
  protected boolean validateExistence(String id, String elementName,
      HtmlElement element, Formatter formatter) {

    if (element == null) {
      formatter.format(
          "Unable to find rendered '%s' element containing " + "the ID '%s'%n",
          elementName, id);
      return false;
    }

    return true;
  }

  protected void validateAttributeSet(TreeMap<String, String> control,
      HtmlElement underTest, String[] ignoredAttributes, Formatter formatter) {

    Arrays.sort(ignoredAttributes);
    TreeMap<String, String> fromPage = new TreeMap<String, String>();
    for (Iterator i = underTest.getAttributesMap().values().iterator(); i
        .hasNext();) {
      DomAttr domEntry = (DomAttr) i.next();
      String key = domEntry.getName();
      if (Arrays.binarySearch(ignoredAttributes, key) > -1) {
        continue;
      }
      // fromPage.put(key, entry.getValue());
      fromPage.put(key, domEntry.getValue());
    }

    if (!control.equals(fromPage)) {
      formatter.format("%n Unexpected result when validating "
          + "passthrough attributes received for the rendered "
          + "'%s' in the response.%n", underTest.getTagName());
      formatter.format("%nExpected attribute key/value pairs:%n%s",
          control.toString());
      formatter.format(
          "%nAttribute key/value pairs from the " + "response:%n%s",
          fromPage.toString());
    }

  } // END validateAttributeSet

  /*
   * In a given htmlpage(root) look for and return the child Label element from
   * the given parent element(tagName), That matches the givin String(forName)
   * to its "for" attribute.
   * 
   * Return null if no label element matches.
   */
  protected HtmlLabel getLabelIncludingFor(HtmlPage root, String tagName,
      String forName) {

    HtmlLabel result = null;

    List list = root.getDocumentElement().getHtmlElementsByTagName(tagName);

    for (Iterator i = list.iterator(); i.hasNext();) {
      HtmlLabel label = (HtmlLabel) i.next();
      if (label.getForAttribute().indexOf(forName) > -1) {
        result = label;
      }
    }

    return result;
  }// End getLabelIncludingFor

  /*
   * In a given htmlpage(root) look for and return the child HtmlSelect element
   * from the given List, that has the same id name as the given id.
   * 
   * Return null if no label element matches.
   */
  protected HtmlSelect getHtmlSelectForId(List<HtmlSelect> selects, String id) {

    for (HtmlSelect select : selects) {
      if (select.getId().contains(id)) {
        return select;
      }

    }

    return null;

  }

  /**
   * In a given htmlpage(root) look for and return the children label elements
   * that contain a given string.
   * 
   * @param root
   *          - the HtmlPage to search.
   * @param myLabel
   *          - The contents on which to search for.
   * 
   * @return - A List of HtmlLabels.
   * 
   */
  protected List<HtmlLabel> getLabelsContaining(HtmlPage root, String myLabel) {
    List<HtmlLabel> result = new ArrayList<HtmlLabel>();

    List<HtmlLabel> labels = root.getDocumentElement()
        .getHtmlElementsByTagName("label");

    for (HtmlLabel label : labels) {
      if (label.asText().contains(myLabel)) {
        result.add(label);
      }
    }

    if ((result.size() == 0)) {
      result = null;
    }

    return result;
  }// End getLabelsContaining

  /**
   * Test for a the give @String "expectedValue" to match the rendered value of
   * the given @HtmlElement "element".
   * 
   * @param element
   *          - HtmlElement under test.
   * @param expectedValue
   *          - The expected result.
   * @param formatter
   *          - used to gather test result output.
   */
  protected void validateElementValue(HtmlElement element, String expectedValue,
      Formatter formatter) {

    String result = element.asText().trim();
    if (!expectedValue.equals(result)) {
      formatter.format("Unexpected Test Result For %s! %n" + "Expected: %s %n"
          + "Found: %s %n", element.getId(), expectedValue, result);
    }
  }// End validateElementValue

  /**
   * Test a given HtmlSelect's options to see if they validate against a
   * test @List (expected).
   * 
   * @param select
   *          - HtmlSelect element under test.
   * @param expected
   *          - The expected result.
   * @param formatter
   *          - used to gather test result output.
   */
  protected void validateSelectOptions(HtmlSelect select, List<String> expected,
      Formatter formatter) {

    List options = select.getOptions();

    if (expected.size() != options.size()) {
      formatter.format(
          "Unexpected Size For %s! %n" + "Expected: %s %n" + "Found: %s %n",
          select.getId(), expected.size(), options.size());
    } else {
      String expSong, recSong;
      HtmlOption opt;
      for (int i = 0; i < options.size(); i++) {
        opt = (HtmlOption) options.get(i);
        expSong = expected.get(i).trim();
        recSong = opt.asText().trim();
        if (!recSong.equals(expSong)) {
          formatter.format("Unexpected Value Test Value! %n" + "Expected: %s %n"
              + "Found: %s %n", expSong, recSong);
        }
      }
    }

  }// End validateSelectOptions

  /**
   * This test method is used for testing to List<String> objects.
   * 
   * @param page
   *          - The HtmlPage that has the the test List Rendered.
   * @param myLabels
   *          - name of the HTMLLabel rendered.
   * @param expNum
   *          - The expected number
   * @param expValue
   *          - The List of expected Strings.
   * @param formatter
   *          - the Formatter that we want test output to be stored.
   * @param ordered
   *          - If true test the List object for ordering.
   */
  protected void testList(HtmlPage page, String myLabels, List<String> expValue,
      int expSize, Formatter formatter, boolean ordered) {

    List<HtmlLabel> renderedList = getLabelsContaining(page, myLabels);
    String[] result;
    String rec;

    if (renderedList != null && !renderedList.isEmpty()) {
      int ii = renderedList.size();
      if (ii != expSize) {
        formatter.format("Unexpected Number of Items Rendered for " + myLabels
            + "! %n" + "Expected: %d %n" + "Received: %d %n", expSize, ii);
      }
    } else {
      formatter.format("No Labels found containing " + myLabels + "!");
    }

    if (ordered) {
      int i = 0;
      for (HtmlLabel label : renderedList) {
        result = label.asText().split(":");
        rec = result[1].trim();
        String expectedValue = expValue.get(i);
        if (!(expectedValue.equals(rec))) {
          formatter.format("Unexpected Value Rendered! %n" + "Found: %s %n"
              + "Expected: %s %n", rec, expectedValue);
        }
        i++;
      }
    } else {
      for (HtmlLabel label : renderedList) {
        result = label.asText().split(":");
        rec = result[1].trim();
        if (!(expValue.contains(rec))) {
          formatter.format("Unexpected Value Rendered! %n" + "Found: %s %n",
              rec);
        }
      }
    }
  }

  /**
   * This test method is used for testing to List<String> objects.
   * 
   * @param page
   *          - The HtmlPage that has the the test List Rendered.
   * @param myLabels
   *          - name of the HTMLLabel rendered.
   * @param expNum
   *          - The expected number
   * @param expValue
   *          - The List of expected Strings.
   * @param formatter
   *          - the Formatter that we want test output to be stored.
   */
  protected void testList(HtmlPage page, String myLabels, List<String> expValue,
      int expSize, Formatter formatter) {

    this.testList(page, myLabels, expValue, expSize, formatter, Boolean.FALSE);

  }

  /**
   * Click the given button.
   * 
   * @param button
   * 
   * @return - The HtmlPage.
   */
  protected HtmlPage doClick(HtmlSubmitInput button) {
    HtmlPage page = null;

    if (!throwExceptionOnFailingStatusCode) {
      webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
    } else {
      webClient.getOptions().setThrowExceptionOnFailingStatusCode(true);
    }

    TestUtil.logMsg("setThrowExceptionOnFailingStatusCode is set to: "
        + webClient.getOptions().isThrowExceptionOnFailingStatusCode());

    try {

      page = (HtmlPage) button.click();

    } catch (IOException e) {
      e.printStackTrace();
    }

    return page;

  }

  /**
   * Does the give HtmlPage(pg) contain the give String(str).
   * 
   * @param pg
   *          - HtmlPage we want to search through.
   * @param str
   *          - String that we are searching for.
   * @param sb
   *          - StringBuffer for error logging.
   * 
   * @throws Fault
   */
  protected void doesPageContain(HtmlPage pg, String str, StringBuilder sb)
      throws Fault {
    this.contains(pg, str, sb, false);
  }

  /**
   * Does the give HtmlPage(pg) contain the give String(str), and if so fail the
   * test.
   * 
   * @param pg
   *          - HtmlPage we want to search through.
   * @param str
   *          - String that we are searching for.
   * @param sb
   *          - StringBuffer for error logging.
   * 
   * @throws Fault
   */
  protected void doesPageNotContain(HtmlPage pg, String str, StringBuilder sb)
      throws Fault {
    this.contains(pg, str, sb, true);
  }

  /**
   * Does the given HtmlPage(pg) match the given String(str)
   * 
   * @param pg
   *          - HtmlPage we want to search through.
   * @param str
   *          - String that we are searching for.
   * @param sb
   *          - StringBuffer for error logging.
   * @param match
   *          - true/false, Set false for negative testing.
   * 
   * @throws Fault
   */
  protected void doesPageMatch(HtmlPage pg, String str, StringBuilder sb,
      Boolean match) throws Fault {

    Formatter formatter = new Formatter(sb);

    if (match == true) {
      if (!pg.asText().matches(str)) {
        formatter.format("Page text should match: %s %n!", str);
      }

    } else if (match == false) {
      if (pg.asText().matches(str)) {
        formatter.format("Page text should 'NOT' match: %s %n!", str);
      }
    }

    handleTestStatus(sb);
    formatter.close();

  }

  /**
   * Validate that the given HtmlPage has a Span elment and that its value is
   * the expected value passed in.
   * 
   * @param pg
   *          - HtmlPage(pg) The page we want to search through.
   * @param spanId
   *          - String(spanId) To look for.
   * @param expected
   *          - String(expected) The expected value of the Span element.
   * @param sb
   *          - StringBuffer for error logging.
   * 
   * @throws Fault
   */
  protected void checkSpanValue(HtmlPage pg, String spanId, String expected,
      StringBuilder sb) throws Fault {

    Formatter formatter = new Formatter(sb);

    HtmlSpan span = (HtmlSpan) getElementOfTypeIncludingId(pg, "span", spanId);

    if (!validateExistence(spanId, "span", span, formatter)) {
      handleTestStatus(sb);
      return;
    }

    String result = span.getTextContent();

    if (!expected.equals(result)) {
      formatter.format("Unexpected Value for Span Element %s! %n "
          + "Expected: %s %n " + "Received: %s %n", spanId, expected, result);
    }

    handleTestStatus(sb);
    formatter.close();

  }

  // ------------------------ private methods

  private void contains(HtmlPage pg, String str, StringBuilder sb,
      Boolean negTest) throws Fault {

    Formatter formatter = new Formatter(sb);

    if (negTest == false) {
      if (!pg.asText().contains(str)) {
        formatter.format("Page should contain: %s %n!", str);
      }

    } else if (negTest == true) {
      if (pg.asText().contains(str)) {
        formatter.format("Page should 'NOT' contain: %s %n!", str);
      }
    }

    handleTestStatus(sb);
    formatter.close();

  }
} // END BaseHtmlUnitClient
