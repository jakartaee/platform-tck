/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPBody;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.saaj.common.SOAP_Util;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.Name;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPBodyElement;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPFault;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.soap.SOAPHeaderElement;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;

public class SOAPBodyTestServlet extends HttpServlet {
  private MessageFactory mf = null;

  private SOAPMessage msg = null;

  private SOAPPart sp = null;

  private SOAPEnvelope envelope = null;

  private SOAPHeader hdr = null;

  private SOAPHeaderElement she = null;

  private SOAPBody body = null;

  private SOAPBodyElement bodye = null;

  private SOAPFault fault = null;

  private SOAPFault fault1 = null;

  private SOAPFault fault2 = null;

  private String prefix = null;

  private String uri = null;

  private void setup() throws Exception {
    TestUtil.logTrace("setup");

    SOAP_Util.setup();

    // Create a message from the message factory.
    TestUtil.logMsg("Create message from message factory");
    msg = SOAP_Util.getMessageFactory().createMessage();

    // Message creation takes care of creating the SOAPPart - a
    // required part of the message as per the SOAP 1.1 spec.
    TestUtil.logMsg("Get SOAP Part");
    sp = msg.getSOAPPart();

    // Retrieve the envelope from the soap part to start building
    // the soap message.
    TestUtil.logMsg("Get SOAP Envelope");
    envelope = sp.getEnvelope();
    prefix = envelope.getElementName().getPrefix();
    uri = envelope.getElementName().getURI();

    // Retrieve the soap header from the envelope.
    TestUtil.logMsg("Get SOAP Header");
    hdr = envelope.getHeader();

    // Retrieve the soap header from the envelope.
    TestUtil.logMsg("Get SOAP Body");
    body = envelope.getBody();
  }

  private void dispatch(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("dispatch");
    String testname = SOAP_Util.getHarnessProps().getProperty("TESTNAME");
    if (testname.equals("addBodyElementTest1")) {
      TestUtil.logMsg("Starting addBodyElementTest1");
      addBodyElementTest1(req, res);
    } else if (testname.equals("addBodyElementTest2")) {
      TestUtil.logMsg("Starting addBodyElementTest2");
      addBodyElementTest2(req, res);
    } else if (testname.equals("addFaultTest1")) {
      TestUtil.logMsg("Starting addFaultTest1");
      addFaultTest1(req, res);
    } else if (testname.equals("addFaultTest2")) {
      TestUtil.logMsg("Starting addFaultTest2");
      addFaultTest2(req, res);
    } else if (testname.equals("addFaultTest3")) {
      TestUtil.logMsg("Starting addFaultTest3");
      addFaultTest3(req, res);
    } else if (testname.equals("addFaultTest4")) {
      TestUtil.logMsg("Starting addFaultTest4");
      addFaultTest4(req, res);
    } else if (testname.equals("addFaultTest5")) {
      TestUtil.logMsg("Starting addFaultTest5");
      addFaultTest5(req, res);
    } else if (testname.equals("addDocumentTest")) {
      TestUtil.logMsg("Starting addDocumentTest");
      addDocumentTest(req, res);
    } else if (testname.equals("getFaultTest")) {
      TestUtil.logMsg("Starting getFaultTest");
      getFaultTest(req, res);
    } else if (testname.equals("hasFaultTest")) {
      TestUtil.logMsg("Starting hasFaultTest");
      hasFaultTest(req, res);
    } else if (testname.equals("extractContentAsDocumentTest1")) {
      TestUtil.logMsg("Starting extractContentAsDocumentTest1");
      extractContentAsDocumentTest1(req, res);
    } else if (testname.equals("extractContentAsDocumentTest2")) {
      TestUtil.logMsg("Starting extractContentAsDocumentTest2");
      extractContentAsDocumentTest2(req, res);
    } else {
      throw new ServletException(
          "The testname '" + testname + "' was not found in the test servlet");

    }

  }

  public void init(ServletConfig servletConfig) throws ServletException {
    super.init(servletConfig);
    System.out.println("SOAPBodyTestServlet:init (Entering)");
    SOAP_Util.doServletInit(servletConfig);
    System.out.println("SOAPBodyTestServlet:init (Leaving)");
  }

  public void doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("doGet");
    dispatch(req, res);
  }

  public void doPost(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("doPost");
    SOAP_Util.doServletPost(req, res);
    doGet(req, res);
  }

  private void addBodyElementTest1(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("addBodyElementTest1");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      // Add a soap body element to the soap body
      TestUtil.logMsg("Add Name SOAPBodyElement to SOAPBody object");
      Name name = envelope.createName("GetLastTradePrice", "ztrade",
          "http://wombat.ztrade.com");
      bodye = body.addBodyElement(name);

      TestUtil.logMsg("Validating SOAPBodyElement object creation ...");
      if (bodye == null) {
        TestUtil.logErr("SOAPBodyElement is null");
        pass = false;
      } else {
        TestUtil.logMsg("SOAPBodyElement was created");
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    }
    // Send response object and test result back to client
    if (pass)
      resultProps.setProperty("TESTRESULT", "pass");
    else
      resultProps.setProperty("TESTRESULT", "fail");
    resultProps.list(out);
  }

  private void addBodyElementTest2(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("addBodyElementTest2");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      // Add a soap body element to the soap body
      TestUtil.logMsg("Add QName SOAPBodyElement to SOAPBody object");
      QName name = new QName("http://wombat.ztrade.com", "GetLastTradePrice",
          "ztrade");
      bodye = body.addBodyElement(name);

      TestUtil.logMsg("Validating SOAPBodyElement object creation ...");
      if (bodye == null) {
        TestUtil.logErr("SOAPBodyElement is null");
        pass = false;
      } else {
        TestUtil.logMsg("SOAPBodyElement was created");
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    }
    // Send response object and test result back to client
    if (pass)
      resultProps.setProperty("TESTRESULT", "pass");
    else
      resultProps.setProperty("TESTRESULT", "fail");
    resultProps.list(out);
  }

  private void addFaultTest1(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("addFaultTest1");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      // Add a soap fault to the soap body
      TestUtil.logMsg("Add SOAPFault to SOAPBody object");
      fault = body.addFault();

      TestUtil.logMsg("Validating SOAPFault object creation ...");
      if (fault == null) {
        TestUtil.logErr("SOAPFault is null");
        pass = false;
      } else {
        TestUtil.logMsg("SOAPFault was created");
        TestUtil.logMsg("SOAPFault = " + fault);
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    }
    // Send response object and test result back to client
    if (pass)
      resultProps.setProperty("TESTRESULT", "pass");
    else
      resultProps.setProperty("TESTRESULT", "fail");
    resultProps.list(out);
  }

  private void addFaultTest2(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("addFaultTest2");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      // Add a soap fault to the soap body
      Name name = envelope.createName("Server", prefix, uri);
      TestUtil.logMsg("Add Name object SOAPFault to SOAPBody object");
      fault = body.addFault(name, "This is a Server fault");

      TestUtil.logMsg("Validating SOAPFault object creation ...");
      if (fault == null) {
        TestUtil.logErr("SOAPFault is null");
        pass = false;
      } else {
        TestUtil.logMsg("SOAPFault was created");
        TestUtil.logMsg("SOAPFault = " + fault);
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    }
    // Send response object and test result back to client
    if (pass)
      resultProps.setProperty("TESTRESULT", "pass");
    else
      resultProps.setProperty("TESTRESULT", "fail");
    resultProps.list(out);
  }

  private void addFaultTest3(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("addFaultTest3");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      // Add a soap fault to the soap body
      Name name = envelope.createName("Server", prefix, uri);
      TestUtil
          .logMsg("Add Name object SOAPFault to SOAPBody object with Locale");
      Locale l = new Locale("en", "US");
      fault = body.addFault(name, "This is a Server fault", l);

      TestUtil.logMsg("Validating SOAPFault object creation ...");
      if (fault == null) {
        TestUtil.logErr("SOAPFault is null");
        pass = false;
      } else {
        TestUtil.logMsg("SOAPFault was created");
        TestUtil.logMsg("SOAPFault = " + fault);
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    }
    // Send response object and test result back to client
    if (pass)
      resultProps.setProperty("TESTRESULT", "pass");
    else
      resultProps.setProperty("TESTRESULT", "fail");
    resultProps.list(out);
  }

  private void addFaultTest4(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("addFaultTest4");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      // Add a soap fault to the soap body
      TestUtil.logMsg("Add QName object SOAPFault to SOAPBody object");
      QName name = new QName(uri, "Server", prefix);
      fault = body.addFault(name, "This is a Server fault");

      TestUtil.logMsg("Validating SOAPFault object creation ...");
      if (fault == null) {
        TestUtil.logErr("SOAPFault is null");
        pass = false;
      } else {
        TestUtil.logMsg("SOAPFault was created");
        TestUtil.logMsg("SOAPFault = " + fault);
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    }
    // Send response object and test result back to client
    if (pass)
      resultProps.setProperty("TESTRESULT", "pass");
    else
      resultProps.setProperty("TESTRESULT", "fail");
    resultProps.list(out);
  }

  private void addFaultTest5(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("addFaultTest5");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      // Add a soap fault to the soap body
      TestUtil
          .logMsg("Add QName object SOAPFault to SOAPBody object with Locale");
      QName name = new QName(uri, "Server", prefix);
      Locale l = new Locale("en", "US");
      fault = body.addFault(name, "This is a Server fault", l);

      TestUtil.logMsg("Validating SOAPFault object creation ...");
      if (fault == null) {
        TestUtil.logErr("SOAPFault is null");
        pass = false;
      } else {
        TestUtil.logMsg("SOAPFault was created");
        TestUtil.logMsg("SOAPFault = " + fault);
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    }
    // Send response object and test result back to client
    if (pass)
      resultProps.setProperty("TESTRESULT", "pass");
    else
      resultProps.setProperty("TESTRESULT", "fail");
    resultProps.list(out);
  }

  private void getFaultTest(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("getFaultTest");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      // Add a soap fault to the soap body
      TestUtil.logMsg("Add SOAPFault fault1 to SOAPBody object");
      fault1 = body.addFault();

      // Get soap fault from the soap body
      TestUtil.logMsg("Get SOAPFault fault2 from SOAPBody object");
      fault2 = body.getFault();

      TestUtil.logMsg("Validating SOAPFault fault1 and fault2 equality ...");
      if (fault1 == null || fault2 == null) {
        TestUtil.logErr("SOAPFault fault1 or fault2 is null");
        pass = false;
      } else if (!(fault1 instanceof SOAPFault)
          || !(fault2 instanceof SOAPFault)) {
        TestUtil.logErr("SOAPFault fault1 or fault2 not instance of SOAPFault");
        pass = false;
      } else {
        TestUtil.logMsg("SOAPFault fault1 was created");
        TestUtil.logMsg("SOAPFault fault2 was created");
        TestUtil.logMsg("SOAPFault fault1 = " + fault1);
        TestUtil.logMsg("SOAPFault fault2 = " + fault2);
        if (!fault1.equals(fault2)) {
          TestUtil.logErr("SOAPFault fault1 and fault2 are not equal");
          pass = false;
        }
      }

    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    }
    // Send response object and test result back to client
    if (pass)
      resultProps.setProperty("TESTRESULT", "pass");
    else
      resultProps.setProperty("TESTRESULT", "fail");
    resultProps.list(out);
  }

  private void hasFaultTest(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("hasFaultTest");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      // Test if soap body has a soap fault
      TestUtil.logMsg("Test that SOAPBody does not have a SOAPFault");
      if (body.hasFault()) {
        TestUtil.logErr("SOAPBody has a fault (unexpected)");
        pass = false;
      } else {
        TestUtil.logMsg("SOAPBody does not have a SOAPFault (expected)");
      }

      // Now add a soap fault to the soap body
      TestUtil.logMsg("Add SOAPFault to SOAPBody object");
      fault = body.addFault();

      // Test if soap body has a soap fault
      TestUtil.logMsg("Test that SOAPBody does have a SOAPFault");
      if (body.hasFault()) {
        TestUtil.logMsg("SOAPBody has a fault (expected)");
      } else {
        TestUtil.logErr("SOAPBody does not have a SOAPFault (unexpected)");
        pass = false;
      }

    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    }
    // Send response object and test result back to client
    if (pass)
      resultProps.setProperty("TESTRESULT", "pass");
    else
      resultProps.setProperty("TESTRESULT", "fail");
    resultProps.list(out);
  }

  private void addDocumentTest(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("addDocumentTest");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      // Add a document to the soap body
      TestUtil.logMsg("Add Document to SOAPBody object");
      Document document = null;
      org.w3c.dom.Document myDomDocument = makeDocument();
      SOAPBodyElement sbe = null;
      if (myDomDocument == null) {
        pass = false;
      } else {
        sbe = body.addDocument(myDomDocument);
        SOAP_Util.dumpSOAPMessage(msg);
      }
      TestUtil.logMsg("Validating addDocument object creation ...");
      if (sbe == null) {
        TestUtil.logErr("SOAPBodyElement is null");
        pass = false;
      } else {
        TestUtil.logMsg("SOAPBodyElement was created");
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    }
    // Send response object and test result back to client
    if (pass)
      resultProps.setProperty("TESTRESULT", "pass");
    else
      resultProps.setProperty("TESTRESULT", "fail");
    resultProps.list(out);
  }

  private Document makeDocument() {

    DocumentBuilder docBuilder = null;
    try {
      docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e);
      TestUtil.printStackTrace(e);
      return null;
    }
    DOMImplementation domImpl = docBuilder.getDOMImplementation();
    DocumentType docType = domImpl.createDocumentType("root",
        "//Test/addDocument", null);
    Document document = domImpl.createDocument(null, "root", docType);
    return document;
  }

  private void extractContentAsDocumentTest1(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("extractContentAsDocumentTest1");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      // Add a child soap element to the soap body
      QName qname1 = new QName("http://wombat.ztrade.com", "GetLastTradePrice",
          "ztrade");
      TestUtil.logMsg("Qname1=" + qname1);
      TestUtil.logMsg("Add child soap element QName1 to the SOAPBody");
      body.addChildElement(qname1);
      SOAP_Util.dumpSOAPMessage(msg);

      // Extract soap body content as a DOM document
      TestUtil.logMsg("Extract SOAPBody content as a DOM Document");
      Document document = body.extractContentAsDocument();

      TestUtil.logMsg("Validate that a DOM Document was returned");
      if (document == null) {
        TestUtil.logErr("Document is null");
        pass = false;
      } else {
        TestUtil.logMsg("DOM Document was created");
        TestUtil.logMsg("Get the Element from the DOM Document");
        Element element = document.getDocumentElement();
        TestUtil.logMsg("Now get the Element Name");
        String elementName = element.getTagName();
        TestUtil.logMsg("Element Name=" + elementName);
      }

      TestUtil.logMsg("Retreive the children of the SOAPBody (should be none)");
      Iterator i = body.getChildElements();
      int count = SOAP_Util.getIteratorCount(i);
      if (count != 0) {
        TestUtil.logErr(
            "Wrong iterator count returned of " + count + ", expected 0");
        TestUtil.logErr("The child soap element was not removed");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    }
    // Send response object and test result back to client
    if (pass)
      resultProps.setProperty("TESTRESULT", "pass");
    else
      resultProps.setProperty("TESTRESULT", "fail");
    resultProps.list(out);
  }

  private void extractContentAsDocumentTest2(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("extractContentAsDocumentTest2");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();
    try {
      setup();

      // Add a 2 child elements to the soap body
      QName qname1 = new QName("http://wombat.ztrade.com", "GetLastTradePrice",
          "ztrade");
      QName qname2 = new QName("foo");
      TestUtil.logMsg("Qname1=" + qname1);
      TestUtil.logMsg("Qname2=" + qname2);
      TestUtil.logMsg("Add child soap element QName1 to the SOAPBody");
      body.addChildElement(qname1);
      TestUtil.logMsg("Add another child soap element QName2 to the SOAPBody");
      body.addChildElement(qname2);
      SOAP_Util.dumpSOAPMessage(msg);
      TestUtil.logMsg("The SOAPBody content cannot be extraced "
          + "if more than 1 child element");
      try {
        // Extract soap body content as a DOM document
        TestUtil.logMsg("Extract SOAPBody content as a DOM Document");
        TestUtil.logMsg("Expect a SOAPException to be thrown");
        body.extractContentAsDocument();
        TestUtil.logErr("Did not throw expected SOAPException");
        pass = false;
      } catch (SOAPException e) {
        TestUtil.logMsg("Caught expected SOAPException");
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    }

    // Send response object and test result back to client
    if (pass)
      resultProps.setProperty("TESTRESULT", "pass");
    else
      resultProps.setProperty("TESTRESULT", "fail");
    resultProps.list(out);
  }
}
