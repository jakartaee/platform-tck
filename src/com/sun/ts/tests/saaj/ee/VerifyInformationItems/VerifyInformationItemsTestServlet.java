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

package com.sun.ts.tests.saaj.ee.VerifyInformationItems;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import javax.xml.namespace.QName;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.saaj.common.SOAP_Util;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.soap.Detail;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.Name;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPBodyElement;
import jakarta.xml.soap.SOAPConstants;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPFault;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.soap.SOAPHeaderElement;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;

public class VerifyInformationItemsTestServlet extends HttpServlet {
  private MessageFactory mf = null;

  private SOAPMessage msg = null;

  private SOAPPart sp = null;

  private SOAPEnvelope envelope = null;

  private SOAPHeader hdr = null;

  private SOAPHeaderElement she = null;

  private SOAPBody body = null;

  private SOAPBodyElement bodye = null;

  private SOAPFault sf = null;

  private SOAPElement se = null;

  private String envelopePrefix = null;

  private String envelopeURI = null;

  private void setup() throws Exception {
    TestUtil.logTrace("setup");

    SOAP_Util.setup();

    // Create a message from the message factory.
    TestUtil.logMsg("Create message from message factory");
    msg = SOAP_Util.getMessageFactory().createMessage();

    // Message creation takes care of creating the soap part
    // so retrieve the soap part
    TestUtil.logMsg("Get SOAP Part");
    sp = msg.getSOAPPart();

    // Retrieve the envelope from the soap part to start building
    // the soap message.
    TestUtil.logMsg("Get SOAP Envelope");
    envelope = sp.getEnvelope();
    envelopePrefix = envelope.getElementName().getPrefix();
    envelopeURI = envelope.getElementName().getURI();

    // Retrieve the soap header from the envelope.
    TestUtil.logMsg("Get SOAP Header");
    hdr = envelope.getHeader();

    // Retrieve the soap body from the envelope.
    TestUtil.logMsg("Get SOAP Body");
    body = envelope.getBody();
  }

  private void dispatch(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("dispatch");
    String testname = SOAP_Util.getHarnessProps().getProperty("TESTNAME");
    if (testname.equals("VerifyEncodingStyleAttributeInfoItem")) {
      TestUtil.logMsg("Starting VerifyEncodingStyleAttributeInfoItem");
      VerifyEncodingStyleAttributeInfoItem(req, res);
    } else if (testname.equals("VerifyRoleAttributeInfoItem")) {
      TestUtil.logMsg("Starting VerifyRoleAttributeInfoItem");
      VerifyRoleAttributeInfoItem(req, res);
    } else if (testname.equals("VerifyRelayAttributeInfoItem")) {
      TestUtil.logMsg("Starting VerifyRelayAttributeInfoItem");
      VerifyRelayAttributeInfoItem(req, res);
    } else if (testname.equals("VerifyMustUnderstandAttributeInfoItem")) {
      TestUtil.logMsg("Starting VerifyMustUnderstandAttributeInfoItem");
      VerifyMustUnderstandAttributeInfoItem(req, res);
    } else if (testname.equals("VerifyEnvelopeElementInfoItem")) {
      TestUtil.logMsg("Starting VerifyEnvelopeElementInfoItem");
      VerifyEnvelopeElementInfoItem(req, res);
    } else if (testname.equals("VerifyHeaderElementInfoItem")) {
      TestUtil.logMsg("Starting VerifyHeaderElementInfoItem");
      VerifyHeaderElementInfoItem(req, res);
    } else if (testname.equals("VerifyBodyElementInfoItem")) {
      TestUtil.logMsg("Starting VerifyBodyElementInfoItem");
      VerifyBodyElementInfoItem(req, res);
    } else if (testname.equals("VerifyBodyChildElementInfoItem")) {
      TestUtil.logMsg("Starting VerifyBodyChildElementInfoItem");
      VerifyBodyChildElementInfoItem(req, res);
    } else if (testname.equals("VerifyFaultElementInfoItem")) {
      TestUtil.logMsg("Starting VerifyFaultElementInfoItem");
      VerifyFaultElementInfoItem(req, res);
    } else if (testname.equals("VerifyDetailElementInfoItem")) {
      TestUtil.logMsg("Starting VerifyDetailElementInfoItem");
      VerifyDetailElementInfoItem(req, res);
    } else if (testname.equals("VerifyCodeElementInfoItem")) {
      TestUtil.logMsg("Starting VerifyCodeElementInfoItem");
      VerifyCodeElementInfoItem(req, res);
    } else if (testname.equals("VerifySubcodeElementInfoItem")) {
      TestUtil.logMsg("Starting VerifySubcodeElementInfoItem");
      VerifySubcodeElementInfoItem(req, res);
    } else if (testname.equals("VerifyUpgradeElementInfoItem")) {
      TestUtil.logMsg("Starting VerifyUpgradeElementInfoItem");
      VerifyUpgradeElementInfoItem(req, res);
    } else if (testname.equals("VerifyNotUnderstoodElementInfoItem")) {
      TestUtil.logMsg("Starting VerifyNotUnderstoodElementInfoItem");
      VerifyNotUnderstoodElementInfoItem(req, res);
    } else {
      throw new ServletException(
          "The testname '" + testname + "' was not found in the test servlet");
    }
  }

  public void init(ServletConfig servletConfig) throws ServletException {
    super.init(servletConfig);
    System.out.println("VerifyInformationItemsTestServlet:init (Entering)");
    SOAP_Util.doServletInit(servletConfig);
    System.out.println("VerifyInformationItemsTestServlet:init (Leaving)");
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

  private void VerifyEncodingStyleAttributeInfoItem(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("VerifyEncodingStyleAttributeInfoItem");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Add encodingStyle attribute to SOAP Header element");
      Name name1 = envelope.createName("foo", "f", "http://foo.org/foo");
      Name name2 = envelope.createName("encodingStyle", envelopePrefix,
          envelopeURI);
      SOAPHeaderElement she = hdr.addHeaderElement(name1);
      she.addAttribute(name2, SOAPConstants.URI_NS_SOAP_1_2_ENCODING);

      SOAP_Util.dumpSOAPMessage(msg);

      Name name = null;
      Iterator i = she.getAllAttributes();
      if (!i.hasNext()) {
        TestUtil.logErr("No attributes (unexpected)");
        pass = false;
      } else {
        name = (Name) i.next();
      }

      if (pass) {
        TestUtil.logMsg("Validating encodingStyle attribute");
        TestUtil.logMsg("URI = " + name.getURI());
        TestUtil.logMsg("Prefix = " + name.getPrefix());
        TestUtil.logMsg("LocalName = " + name.getLocalName());
        TestUtil.logMsg("QualifiedName = " + name.getQualifiedName());
        String uri = name.getURI();
        String localName = name.getLocalName();
        TestUtil.logMsg("Validate the URI which must be "
            + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
        if (!uri.equals(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE)) {
          TestUtil.logErr("Got URI: " + uri + "\nExpected URI: "
              + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
          pass = false;
        }
        TestUtil.logMsg("Validate the LocalName which must be encodingStyle");
        if (!localName.equals("encodingStyle")) {
          TestUtil.logErr("Got LocalName: " + localName
              + ", Expected LocalName: encodingStyle");
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

  private void VerifyRoleAttributeInfoItem(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("VerifyRoleAttributeInfoItem");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Add role attribute to SOAP Header element");
      Name name1 = envelope.createName("foo", "f", "http://foo.org/foo");
      Name name2 = envelope.createName("role", envelopePrefix, envelopeURI);
      SOAPHeaderElement she = hdr.addHeaderElement(name1);
      she.addAttribute(name2, SOAPConstants.URI_SOAP_1_2_ROLE_NEXT);

      SOAP_Util.dumpSOAPMessage(msg);

      Name name = null;
      Iterator i = she.getAllAttributes();
      if (!i.hasNext()) {
        TestUtil.logErr("No attributes (unexpected)");
        pass = false;
      } else {
        name = (Name) i.next();
      }

      if (pass) {
        TestUtil.logMsg("Validating role attribute");
        TestUtil.logMsg("URI = " + name.getURI());
        TestUtil.logMsg("Prefix = " + name.getPrefix());
        TestUtil.logMsg("LocalName = " + name.getLocalName());
        TestUtil.logMsg("QualifiedName = " + name.getQualifiedName());
        String uri = name.getURI();
        String localName = name.getLocalName();
        TestUtil.logMsg("Validate the URI which must be "
            + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
        if (!uri.equals(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE)) {
          TestUtil.logErr("Got URI: " + uri + "\nExpected URI: "
              + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
          pass = false;
        }
        TestUtil.logMsg("Validate the LocalName which must be role");
        if (!localName.equals("role")) {
          TestUtil.logErr(
              "Got LocalName: " + localName + ", Expected LocalName: role");
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

  private void VerifyRelayAttributeInfoItem(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("VerifyRelayAttributeInfoItem");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Add relay attribute to SOAP Header element");
      Name name1 = envelope.createName("foo", "f", "http://foo.org/foo");
      Name name2 = envelope.createName("relay", envelopePrefix, envelopeURI);
      SOAPHeaderElement she = hdr.addHeaderElement(name1);
      she.addAttribute(name2,
          SOAPConstants.URI_SOAP_1_2_ROLE_ULTIMATE_RECEIVER);

      SOAP_Util.dumpSOAPMessage(msg);

      Name name = null;
      Iterator i = she.getAllAttributes();
      if (!i.hasNext()) {
        TestUtil.logErr("No attributes (unexpected)");
        pass = false;
      } else {
        name = (Name) i.next();
      }

      if (pass) {
        TestUtil.logMsg("Validating relay SOAPElement Name");
        TestUtil.logMsg("URI = " + name.getURI());
        TestUtil.logMsg("Prefix = " + name.getPrefix());
        TestUtil.logMsg("LocalName = " + name.getLocalName());
        TestUtil.logMsg("QualifiedName = " + name.getQualifiedName());
        String uri = name.getURI();
        String localName = name.getLocalName();
        TestUtil.logMsg("Validate the URI which must be "
            + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
        if (!uri.equals(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE)) {
          TestUtil.logErr("Got URI: " + uri + "\nExpected URI: "
              + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
          pass = false;
        }
        TestUtil.logMsg("Validate the LocalName which must be relay");
        if (!localName.equals("relay")) {
          TestUtil.logErr(
              "Got LocalName: " + localName + ", Expected LocalName: relay");
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

  private void VerifyMustUnderstandAttributeInfoItem(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("VerifyMustUnderstandAttributeInfoItem");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Add mustUnderstand attribute to SOAP Header element");
      Name name1 = envelope.createName("foo", "f", "http://foo.org/foo");
      Name name2 = envelope.createName("mustUnderstand", envelopePrefix,
          envelopeURI);
      SOAPHeaderElement she = hdr.addHeaderElement(name1);
      she.addAttribute(name2, "true");

      SOAP_Util.dumpSOAPMessage(msg);

      Name name = null;
      Iterator i = she.getAllAttributes();
      if (!i.hasNext()) {
        TestUtil.logErr("No attributes (unexpected)");
        pass = false;
      } else {
        name = (Name) i.next();
      }

      if (pass) {
        TestUtil.logMsg("Validating mustUnderstand SOAPElement Name");
        TestUtil.logMsg("URI = " + name.getURI());
        TestUtil.logMsg("Prefix = " + name.getPrefix());
        TestUtil.logMsg("LocalName = " + name.getLocalName());
        TestUtil.logMsg("QualifiedName = " + name.getQualifiedName());
        String uri = name.getURI();
        String localName = name.getLocalName();
        TestUtil.logMsg("Validate the URI which must be "
            + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
        if (!uri.equals(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE)) {
          TestUtil.logErr("Got URI: " + uri + "\nExpected URI: "
              + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
          pass = false;
        }
        TestUtil.logMsg("Validate the LocalName which must be mustUnderstand");
        if (!localName.equals("mustUnderstand")) {
          TestUtil.logErr("Got LocalName: " + localName
              + ", Expected LocalName: mustUnderstand");
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

  private void VerifyEnvelopeElementInfoItem(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("VerifyEnvelopeElementInfoItem");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating SOAP Envelope element");
      se = (SOAPElement) sp.getEnvelope();

      SOAP_Util.dumpSOAPMessage(msg);

      TestUtil.logMsg("Validating SOAPElement object creation");
      if (se == null) {
        TestUtil.logErr("SOAPElement is null");
        pass = false;
      } else {
        TestUtil.logMsg("SOAPElement was created");
      }

      if (pass) {
        TestUtil.logMsg("Validating Envelope SOAPElement Name");
        TestUtil.logMsg("Get the ElementName");
        Name name = se.getElementName();
        TestUtil.logMsg("URI = " + name.getURI());
        TestUtil.logMsg("Prefix = " + name.getPrefix());
        TestUtil.logMsg("LocalName = " + name.getLocalName());
        TestUtil.logMsg("QualifiedName = " + name.getQualifiedName());
        String uri = name.getURI();
        String localName = name.getLocalName();
        TestUtil.logMsg("Validate the URI which must be "
            + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
        if (!uri.equals(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE)) {
          TestUtil.logErr("Got URI: " + uri + "\nExpected URI: "
              + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
          pass = false;
        }
        TestUtil.logMsg("Validate the LocalName which must be Envelope");
        if (!localName.equals("Envelope")) {
          TestUtil.logErr(
              "Got LocalName: " + localName + ", Expected LocalName: Envelope");
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

  private void VerifyHeaderElementInfoItem(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("VerifyHeaderElementInfoItem");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating SOAP Header element");
      se = (SOAPElement) envelope.getHeader();

      SOAP_Util.dumpSOAPMessage(msg);

      TestUtil.logMsg("Validating SOAPElement object creation");
      if (se == null) {
        TestUtil.logErr("SOAPElement is null");
        pass = false;
      } else {
        TestUtil.logMsg("SOAPElement was created");
      }

      if (pass) {
        TestUtil.logMsg("Validating Header SOAPElement Name");
        TestUtil.logMsg("Get the ElementName");
        Name name = se.getElementName();
        TestUtil.logMsg("URI = " + name.getURI());
        TestUtil.logMsg("Prefix = " + name.getPrefix());
        TestUtil.logMsg("LocalName = " + name.getLocalName());
        TestUtil.logMsg("QualifiedName = " + name.getQualifiedName());
        String uri = name.getURI();
        String localName = name.getLocalName();
        TestUtil.logMsg("Validate the URI which must be "
            + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
        if (!uri.equals(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE)) {
          TestUtil.logErr("Got URI: " + uri + "\nExpected URI: "
              + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
          pass = false;
        }
        TestUtil.logMsg("Validate the LocalName which must be Header");
        if (!localName.equals("Header")) {
          TestUtil.logErr(
              "Got LocalName: " + localName + ", Expected LocalName: Header");
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

  private void VerifyBodyElementInfoItem(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("VerifyBodyElementInfoItem");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating SOAP Body element");
      se = (SOAPElement) envelope.getBody();

      SOAP_Util.dumpSOAPMessage(msg);

      TestUtil.logMsg("Validating SOAPElement object creation");
      if (se == null) {
        TestUtil.logErr("SOAPElement is null");
        pass = false;
      } else {
        TestUtil.logMsg("SOAPElement was created");
      }

      if (pass) {
        TestUtil.logMsg("Validating Body SOAPElement Name");
        TestUtil.logMsg("Get the ElementName");
        Name name = se.getElementName();
        TestUtil.logMsg("URI = " + name.getURI());
        TestUtil.logMsg("Prefix = " + name.getPrefix());
        TestUtil.logMsg("LocalName = " + name.getLocalName());
        TestUtil.logMsg("QualifiedName = " + name.getQualifiedName());
        String uri = name.getURI();
        String localName = name.getLocalName();
        TestUtil.logMsg("Validate the URI which must be "
            + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
        if (!uri.equals(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE)) {
          TestUtil.logErr("Got URI: " + uri + "\nExpected URI: "
              + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
          pass = false;
        }
        TestUtil.logMsg("Validate the LocalName which must be Body");
        if (!localName.equals("Body")) {
          TestUtil.logErr(
              "Got LocalName: " + localName + ", Expected LocalName: Body");
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

  private void VerifyBodyChildElementInfoItem(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("VerifyBodyChildElementInfoItem");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating SOAP Body Child element");
      se = body
          .addChildElement(envelope.createName("MyName", "MyPrefix", "MyURI"));

      SOAP_Util.dumpSOAPMessage(msg);

      TestUtil.logMsg("Validating SOAPElement object creation");
      if (se == null) {
        TestUtil.logErr("SOAPElement is null");
        pass = false;
      } else {
        TestUtil.logMsg("SOAPElement was created");
      }

      if (pass) {
        TestUtil.logMsg("Validating BodyChild SOAPElement Name");
        TestUtil.logMsg("Get the ElementName");
        Name name = se.getElementName();
        TestUtil.logMsg("URI = " + name.getURI());
        TestUtil.logMsg("Prefix = " + name.getPrefix());
        TestUtil.logMsg("LocalName = " + name.getLocalName());
        TestUtil.logMsg("QualifiedName = " + name.getQualifiedName());
        String uri = name.getURI();
        String localName = name.getLocalName();
        TestUtil.logMsg("Validate the URI which must be MyURI");
        if (!uri.equals("MyURI")) {
          TestUtil.logErr("Got URI: " + uri + "\nExpected URI: " + "MyURI");
          pass = false;
        }
        TestUtil.logMsg("Validate the LocalName which must be MyName");
        if (!localName.equals("MyName")) {
          TestUtil.logErr(
              "Got LocalName: " + localName + ", Expected LocalName: MyName");
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

  private void VerifyFaultElementInfoItem(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("VerifyFaultElementInfoItem");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating SOAP Fault element");
      se = (SOAPElement) body.addFault();

      SOAP_Util.dumpSOAPMessage(msg);

      TestUtil.logMsg("Validating SOAPElement object creation");
      if (se == null) {
        TestUtil.logErr("SOAPElement is null");
        pass = false;
      } else {
        TestUtil.logMsg("SOAPElement was created");
      }

      if (pass) {
        TestUtil.logMsg("Validating Fault SOAPElement Name");
        TestUtil.logMsg("Get the ElementName");
        Name name = se.getElementName();
        TestUtil.logMsg("URI = " + name.getURI());
        TestUtil.logMsg("Prefix = " + name.getPrefix());
        TestUtil.logMsg("LocalName = " + name.getLocalName());
        TestUtil.logMsg("QualifiedName = " + name.getQualifiedName());
        String uri = name.getURI();
        String localName = name.getLocalName();
        TestUtil.logMsg("Validate the URI which must be "
            + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
        if (!uri.equals(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE)) {
          TestUtil.logErr("Got URI: " + uri + "\nExpected URI: "
              + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
          pass = false;
        }
        TestUtil.logMsg("Validate the LocalName which must be Fault");
        if (!localName.equals("Fault")) {
          TestUtil.logErr(
              "Got LocalName: " + localName + ", Expected LocalName: Fault");
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

  private void VerifyCodeElementInfoItem(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("VerifyCodeElementInfoItem");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating SOAP Fault Code element");
      SOAPFault sf = body.addFault(SOAPConstants.SOAP_VERSIONMISMATCH_FAULT,
          "This is the fault string (boo hoo hoo)");

      SOAP_Util.dumpSOAPMessage(msg);

      if (pass) {
        TestUtil.logMsg("Validating Code SOAPElement");
        TestUtil.logMsg("Get the Code SOAPElement");
        QName name = sf.getFaultCodeAsQName();
        TestUtil.logMsg("URI = " + name.getNamespaceURI());
        TestUtil.logMsg("Prefix = " + name.getPrefix());
        TestUtil.logMsg("LocalName = " + name.getLocalPart());
        TestUtil.logMsg("QualifiedName = " + name.toString());
        String uri = name.getNamespaceURI();
        String localName = name.getLocalPart();
        TestUtil.logMsg("Validate the URI which must be "
            + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
        if (!uri.equals(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE)) {
          TestUtil.logErr("Got URI: " + uri + "\nExpected URI: "
              + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
          pass = false;
        }
        TestUtil.logMsg("Validate the LocalName which must be "
            + SOAPConstants.SOAP_VERSIONMISMATCH_FAULT.getLocalPart());
        if (!localName
            .equals(SOAPConstants.SOAP_VERSIONMISMATCH_FAULT.getLocalPart())) {
          TestUtil
              .logErr("Got LocalName: " + localName + ", Expected LocalName: "
                  + SOAPConstants.SOAP_VERSIONMISMATCH_FAULT.getLocalPart());
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

  private void VerifySubcodeElementInfoItem(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("VerifySubcodeElementInfoItem");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating SOAP Fault Subcode element");
      SOAPFault sf = body.addFault(SOAPConstants.SOAP_VERSIONMISMATCH_FAULT,
          "This is the fault string (boo hoo hoo)");
      QName subcode = new QName("http://www.w3.org/2003/05/soap-rpc",
          "BadArguments", "rpc");
      sf.appendFaultSubcode(subcode);

      SOAP_Util.dumpSOAPMessage(msg);

      if (pass) {
        TestUtil.logMsg("Validating Subcode SOAPElement");
        TestUtil.logMsg("Get the Subcode SOAPElement");
        Iterator i = sf.getFaultSubcodes();
        QName name = (QName) i.next();
        TestUtil.logMsg("URI = " + name.getNamespaceURI());
        TestUtil.logMsg("Prefix = " + name.getPrefix());
        TestUtil.logMsg("LocalName = " + name.getLocalPart());
        TestUtil.logMsg("QualifiedName = " + name.toString());
        String uri = name.getNamespaceURI();
        String localName = name.getLocalPart();
        TestUtil.logMsg("Validate the URI which must be "
            + "http://www.w3.org/2003/05/soap-rpc");
        if (!uri.equals("http://www.w3.org/2003/05/soap-rpc")) {
          TestUtil.logErr("Got URI: " + uri + "\nExpected URI: "
              + "http://www.w3.org/2003/05/soap-rpc");
          pass = false;
        }
        TestUtil.logMsg("Validate the LocalName which must be Subcode");
        if (!localName.equals("BadArguments")) {
          TestUtil.logErr("Got LocalName: " + localName
              + ", Expected LocalName: BadArguments");
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

  private void VerifyDetailElementInfoItem(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("VerifyDetailElementInfoItem");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating a Detail element");
      SOAPFault sf = body.addFault();
      Detail d = sf.addDetail();

      TestUtil.logMsg("Validating Detail element creation");
      if (d == null) {
        TestUtil.logErr("Detail element is null");
        pass = false;
      } else {
        TestUtil.logMsg("Detail element was created");
      }
      SOAPElement se = (SOAPElement) d;

      if (pass) {
        TestUtil.logMsg("Validating Detail SOAPElement Name");
        TestUtil.logMsg("Get the ElementName");
        Name name = se.getElementName();
        TestUtil.logMsg("URI = " + name.getURI());
        TestUtil.logMsg("Prefix = " + name.getPrefix());
        TestUtil.logMsg("LocalName = " + name.getLocalName());
        TestUtil.logMsg("QualifiedName = " + name.getQualifiedName());
        String uri = name.getURI();
        String localName = name.getLocalName();
        TestUtil.logMsg("Validate the URI which must be "
            + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
        if (!uri.equals(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE)) {
          TestUtil.logErr("Got URI: " + uri + "\nExpected URI: "
              + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
          pass = false;
        }
        TestUtil.logMsg("Validate the LocalName which must be Detail");
        if (!localName.equals("Detail")) {
          TestUtil.logErr(
              "Got LocalName: " + localName + ", Expected LocalName: Detail");
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

  private void VerifyUpgradeElementInfoItem(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("VerifyUpgradeElementInfoItem");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      // Create a list of supported URIs.
      ArrayList supported = new ArrayList();
      supported.add(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
      supported.add(SOAPConstants.URI_NS_SOAP_ENVELOPE);

      TestUtil.logMsg("Creating Upgrade SOAPHeaderElement");
      she = hdr.addUpgradeHeaderElement(supported.iterator());

      TestUtil.logMsg("Validating SOAPHeaderElement object creation");
      if (she == null) {
        TestUtil.logErr("SOAPHeaderElement is null");
        pass = false;
      } else {
        TestUtil.logMsg("SOAPHeaderElement was created");
      }

      if (pass) {
        TestUtil.logMsg("Validating Upgrade SOAPHeaderElement Name");
        TestUtil.logMsg("Get the ElementName");
        Name name = she.getElementName();
        TestUtil.logMsg("URI = " + name.getURI());
        TestUtil.logMsg("Prefix = " + name.getPrefix());
        TestUtil.logMsg("LocalName = " + name.getLocalName());
        TestUtil.logMsg("QualifiedName = " + name.getQualifiedName());
        String uri = name.getURI();
        String localName = name.getLocalName();
        TestUtil.logMsg("Validate the URI which must be "
            + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
        if (!uri.equals(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE)) {
          TestUtil.logErr("Got URI: " + uri + "\nExpected URI: "
              + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
          pass = false;
        }
        TestUtil.logMsg("Validate the LocalName which must be Upgrade");
        if (!localName.equals("Upgrade")) {
          TestUtil.logErr(
              "Got LocalName: " + localName + ", Expected LocalName: Upgrade");
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

  private void VerifyNotUnderstoodElementInfoItem(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("VerifyNotUnderstoodElementInfoItem");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating NotUnderstood SOAPHeaderElement");
      she = hdr.addNotUnderstoodHeaderElement(
          new QName("http://foo.org", "foo", "f"));

      TestUtil.logMsg("Validating SOAPHeaderElement object creation");
      if (she == null) {
        TestUtil.logErr("SOAPHeaderElement is null");
        pass = false;
      } else {
        TestUtil.logMsg("SOAPHeaderElement was created");
      }

      if (pass) {
        TestUtil.logMsg("Validating NotUnderstood SOAPHeaderElement Name");
        TestUtil.logMsg("Get the ElementName");
        Name name = she.getElementName();
        TestUtil.logMsg("URI = " + name.getURI());
        TestUtil.logMsg("Prefix = " + name.getPrefix());
        TestUtil.logMsg("LocalName = " + name.getLocalName());
        TestUtil.logMsg("QualifiedName = " + name.getQualifiedName());
        String uri = name.getURI();
        String localName = name.getLocalName();
        TestUtil.logMsg("Validate the URI which must be "
            + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
        if (!uri.equals(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE)) {
          TestUtil.logErr("Got URI: " + uri + "\nExpected URI: "
              + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
          pass = false;
        }
        TestUtil.logMsg("Validate the LocalName which must be NotUnderstood");
        if (!localName.equals("NotUnderstood")) {
          TestUtil.logErr("Got LocalName: " + localName
              + ", Expected LocalName: NotUnderstood");
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
}
