/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.pagedata;

import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.helpers.DefaultHandler;

import javax.servlet.jsp.tagext.TagLibraryValidator;
import javax.servlet.jsp.tagext.ValidationMessage;
import javax.servlet.jsp.tagext.PageData;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import com.sun.ts.tests.jsp.common.util.JspTestUtil;

/**
 * Validate the PageData object provided to a configured TagLibraryValidator.
 */
public class PageDataValidator extends TagLibraryValidator {

  /**
   * Flag that indicates the TLV has been called by the container.
   */
  private static boolean _called = false;

  /**
   * SAX parser factory used to get parser instances.
   */
  private SAXParserFactory _factory = null;

  /**
   * Initializes the parser factory when the TLV is first created.
   */
  public PageDataValidator() {
  }

  /**
   * Validates the XML view of the JSP page using the provided PageData object.
   * 
   * @param prefix
   *          - the tag library prefix
   * @param uri
   *          - the tag library URI
   * @param page
   *          - the PageData object.
   * @return null if no validation errors occurred, otherwise an non-zero-length
   *         array of ValidationMessages.
   */
  public ValidationMessage[] validate(String prefix, String uri,
      PageData page) {
    debug("In validate()");
    _called = true;
    debug("Set _called to 'true'.");
    InputStream input = null;
    SAXParser parser;
    PageDataValidator.PageHandler handler = new PageDataValidator.PageHandler();
    try {
      initializeSaxParserFactory();
      parser = _factory.newSAXParser();
      displayXmlView(page.getInputStream());
      input = page.getInputStream();
      parser.parse(input, handler);
    } catch (Throwable t) {
      return JspTestUtil.getValidationMessage(null,
          "Test Setup FAILED: " + t.toString());
    } finally {
      if (input != null) {
        try {
          input.close();
        } catch (IOException e) {
          debug("IOException caught when closing steam.");
        }
      }
    }
    return handler.getParsingResults();
  }

  /**
   * Releases any state associated with this TLV.
   */
  public void release() {
    _factory = null;
    super.release();
  }

  /**
   * Returns the TVL invocation status.
   * 
   * @return true if the TLV has been called, otherwise false
   */
  public static boolean wasCalled() {
    return _called;
  }

  /**
   * Resets the TLV invocation status.
   */
  public static void reset() {
    _called = false;
    debug("Set _called to 'false'");
  }

  /**
   * Initializes the SAXParserFactory. The parser will not perform any
   * validation, but will be namespace aware and will provide namespace prefixes
   * in the attribute lists of elements.
   *
   * @throws ParserConfigurationException
   *           - if the parser could not be configured
   * @throws SAXNotRecognizedException
   *           - if namespace-prefixes is not recognized
   * @throws SAXNotSupportedException
   *           - if namespace-prefixes is not supported
   */
  private void initializeSaxParserFactory() throws ParserConfigurationException,
      SAXNotRecognizedException, SAXNotSupportedException {
    debug(
        "Initializing SAXParserFactory [ validating: false, NS aware: true, NS Prefixes: true ]");
    _factory = SAXParserFactory.newInstance();
    _factory.setValidating(false);
    _factory.setNamespaceAware(true);
    _factory.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
  }

  /**
   * Displays the XML view of a JSP page as seen by the container.
   * 
   * @param in
   *          - the InputStream from PageData
   */
  private static void displayXmlView(InputStream in) {
    if (JspTestUtil.DEBUG) {
      InputStream bin = null;
      ByteArrayOutputStream out = null;
      try {
        bin = new BufferedInputStream(in);
        out = new ByteArrayOutputStream();

        for (int i = bin.read(); i != -1; i = bin.read()) {
          out.write(i);
        }

        debug("*************** XML View of JSP Page ***************");
        debug(out.toString());
        debug("****************************************************");
      } catch (Throwable t) {
        debug("[INFO] Unexpected exception while trying to display XML view "
            + "of the JSP page.");
      } finally {
        try {
          bin.close();
          out.close();
        } catch (Throwable t) {
          debug("[INFO] Unexpected exception closing I/O streams.");
        }
      }
    }
  }

  /**
   * Utility method to wrap JspTestUtil.debug(). This method will add this
   * class' name to the provide message and then delegate to JspTestUtil.
   * 
   * @param message
   *          - a debug message
   */
  private static void debug(String message) {
    JspTestUtil.debug("[PageDataValidator] " + message);
  }

  /**
   * An extension to the DefaultHandler to process the elements within the XML
   * view of a JSP page.
   */
  private static class PageHandler extends DefaultHandler {

    /**
     * The <tt>jsp:root</tt> element.
     */
    private static final String JSP_ROOT_QN = "jsp:root";

    /**
     * The <tt>jsp:scriptlet</tt> element.
     */
    private static final String JSP_SCR_QN = "jsp:scriptlet";

    /**
     * The <tt>jsp:declaration</tt> element.
     */
    private static final String JSP_DECL_QN = "jsp:declaration";

    /**
     * The <tt>jsp:declaration</tt> element.
     */
    private static final String JSP_EXPR_QN = "jsp:expression";

    /**
     * The <tt>jsp:text</tt> element.
     */
    private static final String JSP_TEXT_QN = "jsp:text";

    /**
     * The <tt>pagedata:test</tt> element.
     */
    private static final String TCK_TAG_QN = "pagedata:test";

    /**
     * The <tt>jsp:directive.page</tt> element.
     */
    private static final String JSP_PAGE_QN = "jsp:directive.page";

    /**
     * The <tt>jsp:directive.include</tt> element. <tt>NOTE:</tt> this should
     * <tt>not</tt> appear in the XML view of a JSP page.
     */
    private static final String JSP_INCL_QN = "jsp:directive.include";

    /**
     * Counter indicating that the <tt>jsp:scriptlet</tt> element was found.
     */
    private short _scrWasFound = 0;

    /**
     * Counter indicating that the <tt>jsp:declaration</tt> element was found.
     */
    private short _declWasFound = 0;

    /**
     * Counter indicating that the <tt>jsp:expression</tt> element was found.
     */
    private short _exprWasFound = 0;

    /**
     * Counter indicating that the <tt>pagedata:test</tt> element was found.
     */
    private short _tagLibWasFound = 0;

    /**
     * Counter indicating that the taglib declaration as an xmlns attribute of
     * the <tt>jsp:root</tt> element was found.
     */
    private short _tagDeclWasFound = 0;

    /**
     * Counter indicating that the <tt>jsp:root</tt> element was found.
     */
    private short _jspRootWasFound = 0;

    /**
     * Counter indicating that the <tt>jsp:text</tt> element was found.
     */
    private short _textWasFound = 0;

    /**
     * Counter indicating that the <tt>jsp:directive.page</tt> element was found
     */
    private short _pageDirectiveFound = 0;

    /**
     * Counter indicating that the jsp namespace as an xmlns attribute of the
     * <tt>jsp:root</tt> element was found.
     */
    private short _jspNameSpaceFound = 0;

    /**
     * Counter indicating that the <tt>jsp:directive.include</tt> element was
     * found.
     */
    private short _includeDirectiveFound = 0;

    /**
     * Counter indicating that the RT expression present in the
     * <tt>pagedata.test</tt> element was properly converted from standard JSP
     * syntax to syntax supported by XML.
     */
    private short _rtExprFound = 0;

    /**
     * Counter indicating that the <tt>version</tt> attribute of the
     * <tt>jsp:root</tt> element was found.
     */
    private short _jspRootVersionFound = 0;

    /**
     * Flag to indicate if an element doesn't have a jsp:id attribute.
     */
    private boolean _jspIdAttributesFound = true;

    /**
     * List to hold names of elements that do not contain jsp:id attributes.
     */
    private Map elements = null;

    /**
     * Default constructor.
     */
    public PageHandler() {
      elements = new HashMap();
    }

    /**
     * Handles the start elements found in the XML stream by the parser.
     * 
     * @param uri
     *          - the uri of the namespace
     * @param localName
     *          - the name of the element minus the namespace
     * @param qName
     *          - the name including the namespace
     * @param attributes
     *          - the attributes of this element
     * @throws SAXException
     *           - if an error occurs
     */
    public void startElement(String uri, String localName, String qName,
        Attributes attributes) throws SAXException {
      debug("Values passed to startElement[uri: " + uri + ", localname: "
          + localName + ", qname: " + qName + "]");

      if (qName.trim().equals(JSP_DECL_QN)) {
        debug(JSP_DECL_QN + " was found!");
        _declWasFound++;
        checkJspIdAttribute(JSP_DECL_QN, attributes);
      } else if (qName.trim().equals(JSP_EXPR_QN)) {
        debug(JSP_EXPR_QN + " was found!");
        _exprWasFound++;
        checkJspIdAttribute(JSP_EXPR_QN, attributes);
      } else if (qName.trim().equals(JSP_ROOT_QN)) {
        debug(JSP_ROOT_QN + " was found!");
        _jspRootWasFound++;
        // extra handling to see if the taglib directive was
        // properly handled.
        if (hasAttribute("xmlns:jsp", "http://java.sun.com/JSP/Page",
            attributes)) {
          _jspNameSpaceFound++;
        }
        if (hasAttribute("xmlns:pagedata",
            "http://java.sun.com/tck/jsp/pagedata", attributes)) {
          _tagDeclWasFound++;
        }
        if (hasAttribute("version", "2.0", attributes)) {
          _jspRootVersionFound++;
        }
        checkJspIdAttribute(JSP_ROOT_QN, attributes);

      } else if (qName.equals(TCK_TAG_QN)) {
        debug(TCK_TAG_QN + " was found!");
        _tagLibWasFound++;
        checkJspIdAttribute(TCK_TAG_QN, attributes);

        // process the attributes to find the one RTExpression
        int length = attributes.getLength();
        for (int i = 0; i < length; i++) {
          String name = attributes.getLocalName(i).trim();
          debug("pagedata:test attribute name: " + name);
          if (name.equals("dynAttribute")) {
            String value = attributes.getValue(i).trim();
            debug("pagedata:test attribute '" + name + "' value: " + value);
            if (value.startsWith("%=") && (value.endsWith("%"))) {
              debug("Converted JSP RT attribute expression was found!");
              _rtExprFound++;
            }
          }
        }
      } else if (qName.trim().equals(JSP_SCR_QN)) {
        debug(JSP_SCR_QN + " was found!");
        _scrWasFound++;
        checkJspIdAttribute(JSP_SCR_QN, attributes);
      } else if (qName.trim().equals(JSP_TEXT_QN)) {
        debug(JSP_TEXT_QN + " was found!");
        _textWasFound++;
        checkJspIdAttribute(JSP_TEXT_QN, attributes);
      } else if (qName.trim().equals(JSP_PAGE_QN)) {
        // process the attributes to find the one RTExpression
        int length = attributes.getLength();
        for (int i = 0; i < length; i++) {
          String name = attributes.getLocalName(i).trim();
          debug("jsp:directive.page attribute name: " + name);
          if (name.equals("import")) {
            debug(JSP_PAGE_QN + " was found!");
            _pageDirectiveFound++;
          }
        }
        checkJspIdAttribute(JSP_PAGE_QN, attributes);
      } else if (qName.trim().equals(JSP_INCL_QN)) {
        debug(JSP_INCL_QN + " was found!  This is a problem...");
        _includeDirectiveFound++;
      }
    }

    /**
     * Checks the instance variables used to count the elements processed and
     * based on the values, will return failure messages.
     * 
     * @return null of no errors occurred, othersise an array of
     *         ValidationMessages noting the failures.
     */
    public ValidationMessage[] getParsingResults() {
      debug("Processing the results of the parse operation...");
      ValidationMessage[] vMessages = null;
      List messageList = new ArrayList();
      if (_scrWasFound == 0) {
        messageList.add(
            "Unable to find the jsp:scriptlet element in theXML view of the processed JSP page.\n");
      }
      if (_scrWasFound > 1) {
        messageList.add("Found " + _scrWasFound
            + " jsp:scriplet elements in the XML view of the processed JSP page when 1 was expected.\n");
      }
      if (_declWasFound == 0) {
        messageList.add(
            "Unable to find the jsp:declaration element in the XML view of the processed JSP page.\n");
      }
      if (_declWasFound > 1) {
        messageList.add("Found " + _declWasFound
            + " jsp:declaration elements in the XML view of the processed JSP page when 1 was expected.\n");
      }
      if (_exprWasFound == 0) {
        messageList.add(
            "Unable to find the jsp:expression element in the XML view of the JSP page.\n");
      }
      if (_exprWasFound > 1) {
        messageList.add("Found " + _exprWasFound
            + " jsp:scriplet elements in the XML view of the processed JSP page when 1 was expected.\n");
      }
      if (_tagLibWasFound == 0) {
        messageList.add(
            "Unable to find the taglib call pagedata:test element in the XML view of the JSP page.\n");
      }
      if (_tagLibWasFound > 1) {
        messageList.add("Found " + _tagLibWasFound
            + " pagedata:test elements in the XML view of the processed JSP page when 1 was expected.\n");
      }
      if (_tagDeclWasFound == 0) {
        messageList.add(
            "Unable to find the taglib declaration for uri 'http://java.sun.com/tck/jsp/pagedata' in "
                + "the XML view of the JSP page.\n");
      }
      if (_tagDeclWasFound > 1) {
        messageList.add("Found " + _tagDeclWasFound
            + " xmlns:pagedata attributes of the jsp:root element  in the XML view of the processed JSP page when 1 was expected.\n");
      }
      if (_jspRootWasFound == 0) {
        messageList.add(
            "Unable to find the jsp:root element in the XML view of the JSP page.\n");
      }
      if (_jspRootWasFound > 1) {
        messageList.add("Found " + _jspRootWasFound
            + " jsp:root elements in the XML view of the processed JSP page when 1 was expected.\n");
      }
      if (_textWasFound == 0) {
        messageList.add(
            "Unable to find the jsp:text element in the XML view of the JSP page.\n");
      }
      if (_pageDirectiveFound == 0) {
        messageList.add(
            "Unable to find the jsp:page.directive element in the XML view of the JSP page.\n");
      }
      if (_pageDirectiveFound > 1) {
        messageList.add("Found " + _pageDirectiveFound
            + " jsp:directive.page elements in the XML view of the processed JSP page when 1 was expected.\n");
      }
      if (_includeDirectiveFound != 0) {
        messageList.add(
            "Unexpectedly found a jsp:directive.include element in the XML view of the JSP page.\n");
      }
      if (_jspNameSpaceFound == 0) {
        messageList.add(
            "Unable to find JSP namespace xmlns:jsp with URI of 'http://java.sun.com/JSP/Page'"
                + " in the XML view of the JSP page.\n");
      }
      if (_jspNameSpaceFound > 1) {
        messageList.add("Found " + _jspNameSpaceFound
            + " JSP namespace attributes within the jsp:root element in the XML view of the processed JSP page when 1 was expected.\n");
      }
      if (_rtExprFound == 0) {
        messageList.add(
            "Unable to find rt expression attribute in the 'dynAttribute' of pagedata:test tag invocation.\n");
      }
      if (_rtExprFound > 1) {
        messageList.add("Found " + _rtExprFound
            + " rt expressions in the XML view of the JSP page, expected only 1.\n");
      }
      if (_jspRootVersionFound == 0) {
        messageList.add(
            "Unable to find the 'version' attribute with value '2.0' in the jsp:root element.\n");
      }
      if (_jspRootVersionFound > 1) {
        messageList.add("The jsp:root element contained " + _jspRootVersionFound
            + " attributes, where only 1 was expected.");
      }
      if (!_jspIdAttributesFound) {
        messageList
            .add("The following elements did not contain a jsp:id attribute: "
                + getStringFromMap(elements));
      }
      if (messageList.size() != 0) {
        List list = new ArrayList();
        for (Iterator i = messageList.iterator(); i.hasNext();) {
          list.add(new ValidationMessage(null, (String) i.next()));
        }
        vMessages = (ValidationMessage[]) list
            .toArray(new ValidationMessage[list.size()]);
      }
      return vMessages;
    }

    /**
     * Checks that the jsp:id attribute exists in the attribute list of the
     * specified element. If it doesn't it adds this element name to a list of
     * elements that have a similar issue.
     * 
     * @param elementName
     *          - name of the element
     * @param attributes
     *          - attribute list
     */
    private void checkJspIdAttribute(String elementName,
        Attributes attributes) {
      if (!hasAttribute("jsp:id", null, attributes)) {
        Short sho = (Short) elements.get(elementName);
        if (sho == null) {
          elements.put(elementName, Short.valueOf((short) 1));
        } else {
          short s = sho.shortValue();
          elements.put(elementName, Short.valueOf(++s));
        }
        _jspIdAttributesFound = false;
      }
    }

    /**
     * Scans the provided attribute object for the specified attribute name and
     * value. If value is null, then only the attribute name is checked.
     * 
     * @param attrName
     *          - name of the attribute to check for
     * @param attrValue
     *          - the expected value of this attribute
     * @param attributes
     *          - the element attributes
     * @return - true if found otherwise false
     */
    private boolean hasAttribute(String attrName, String attrValue,
        Attributes attributes) {
      debug("Scanning for attribute: [ " + attrName + ", " + attrValue + " ]");
      boolean found = false;
      int length = attributes.getLength();
      for (int i = 0; i < length; i++) {
        String name = attributes.getQName(i).trim();
        String val = attributes.getValue(i).trim();
        debug("Available attribute: [ " + name + ", " + val + " ]");
        if (attrValue == null) {
          if (name.equals(attrName)) {
            debug("Attribute found!");
            found = true;
          }
        } else {
          if (name.equals(attrName) && val.equals(attrValue)) {
            debug("Attribute found!");
            found = true;
          }
        }
      }
      return found;
    }

    /**
     * Returns a string representation of the Map containing elements and the
     * occurrence count.
     * 
     * @param map
     *          - element map
     * @return String representation of the map
     */
    private String getStringFromMap(Map<String, Short> map) {
      StringBuffer sb = new StringBuffer();
      Iterator<Map.Entry<String, Short>> itr = map.entrySet().iterator();

      sb.append("[");
      while (itr.hasNext()) {
        Map.Entry<String, Short> entry = itr.next();
        sb.append(entry.getKey() + " (" + entry.getValue() + ")");

        if (itr.hasNext()) {
          sb.append(", ");
        }
      }

      sb.append("]");
      return sb.toString();
    }
  }
}
