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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.SAAJResult;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.System.Logger;
import java.util.Properties;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.saaj.common.SOAP_Util;
import com.sun.ts.tests.saaj.common.XMLUtils;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.Name;
import jakarta.xml.soap.Node;
import jakarta.xml.soap.SAAJResult;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPBodyElement;
import jakarta.xml.soap.SOAPConstants;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.soap.SOAPHeaderElement;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;

public class SAAJResultTestServlet extends HttpServlet {
	
	  private static final Logger logger = (Logger) System.getLogger(SAAJResultTestServlet.class.getName());

  private MessageFactory mf = null;

  private SOAPMessage msg = null;

  private SOAPPart sp = null;

  private SOAPEnvelope envelope = null;

  private SOAPHeader hdr = null;

  private SOAPBody body = null;

  private SOAPElement se = null;

  private static final String NS_PREFIX = "ns-prefix";

  private static final String NS_URI = "ns-uri";

  private void setup() throws Exception {
    TestUtil.logTrace("setup");

    SOAP_Util.setup();

    // Create a message from the message factory.
    logger.log(Logger.Level.INFO,"Create message from message factory");
    msg = SOAP_Util.getMessageFactory().createMessage();

    // Message creation takes care of creating the SOAPPart - a
    // required part of the message as per the SOAP 1.1 spec.
    logger.log(Logger.Level.INFO,"Get SOAP Part");
    sp = msg.getSOAPPart();

    // Retrieve the envelope from the soap part to start building
    // the soap message.
    logger.log(Logger.Level.INFO,"Get SOAP Envelope");
    envelope = sp.getEnvelope();

    // Retrieve the soap header from the envelope.
    logger.log(Logger.Level.INFO,"Get SOAP Header");
    hdr = envelope.getHeader();

    // Retrieve the soap header from the envelope.
    logger.log(Logger.Level.INFO,"Get SOAP Body");
    body = envelope.getBody();

    Name name = envelope.createName("MyAttr1");
    String value = "MyValue1";
    se = body.addAttribute(name, value);
  }

  private void dispatch(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("dispatch");
    String testname = SOAP_Util.getHarnessProps().getProperty("TESTNAME");
    if (testname.equals("SAAJResultConstructorTest1")) {
      logger.log(Logger.Level.INFO,"Starting SAAJResultConstructorTest1");
      SAAJResultConstructorTest1(req, res);
    } else if (testname.equals("SAAJResultConstructorTest2")) {
      logger.log(Logger.Level.INFO,"Starting SAAJResultConstructorTest2");
      SAAJResultConstructorTest2(req, res);
    } else if (testname.equals("SAAJResultConstructorTest3")) {
      logger.log(Logger.Level.INFO,"Starting SAAJResultConstructorTest3");
      SAAJResultConstructorTest3(req, res);
    } else if (testname.equals("SAAJResultConstructorTest4")) {
      logger.log(Logger.Level.INFO,"Starting SAAJResultConstructorTest4");
      SAAJResultConstructorTest4(req, res);
    } else if (testname.equals("getResultTest1")) {
      logger.log(Logger.Level.INFO,"Starting getResultTest1");
      getResultTest1(req, res);
    } else if (testname.equals("getResultTest2")) {
      logger.log(Logger.Level.INFO,"Starting getResultTest2");
      getResultTest2(req, res);
    } else {
      throw new ServletException(
          "The testname '" + testname + "' was not found in the test servlet");
    }
  }

  public void init(ServletConfig servletConfig) throws ServletException {
    super.init(servletConfig);
    System.out.println("SAAJResultTestServlet:init (Entering)");
    SOAP_Util.doServletInit(servletConfig);
    System.out.println("SAAJResultTestServlet:init (Leaving)");
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

  private void SAAJResultConstructorTest1(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("SAAJResultConstructorTest1");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      logger.log(Logger.Level.INFO,"Create a SAAJResult object using constructor");
      SAAJResult sr = new SAAJResult();

      logger.log(Logger.Level.INFO,"Validating SAAJResult object creation");
      if (sr == null) {
        logger.log(Logger.Level.ERROR,"SAAJResult is null");
        pass = false;
      } else {
        logger.log(Logger.Level.INFO,"SAAJResult was created");
      }
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Exception: " + e);
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

  private void SAAJResultConstructorTest2(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("SAAJResultConstructorTest2");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      logger.log(Logger.Level.INFO,"Create a SAAJResult object using constructor");
      SAAJResult sr = new SAAJResult(se);

      logger.log(Logger.Level.INFO,"Validating SAAJResult object creation");
      if (sr == null) {
        logger.log(Logger.Level.ERROR,"SAAJResult is null");
        pass = false;
      } else {
        logger.log(Logger.Level.INFO,"SAAJResult was created");
      }
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Exception: " + e);
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

  private void SAAJResultConstructorTest3(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("SAAJResultConstructorTest3");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      logger.log(Logger.Level.INFO,"Create a SAAJResult object using constructor");
      SAAJResult sr = new SAAJResult(msg);

      logger.log(Logger.Level.INFO,"Validating SAAJResult object creation");
      if (sr == null) {
        logger.log(Logger.Level.ERROR,"SAAJResult is null");
        pass = false;
      } else {
        logger.log(Logger.Level.INFO,"SAAJResult was created");
      }
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Exception: " + e);
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

  private void SAAJResultConstructorTest4(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("SAAJResultConstructorTest4");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      logger.log(Logger.Level.INFO,"Create a SAAJResult object using constructor");
      SAAJResult sr = new SAAJResult(SOAPConstants.SOAP_1_1_PROTOCOL);

      logger.log(Logger.Level.INFO,"Validating SAAJResult object creation");
      if (sr == null) {
        logger.log(Logger.Level.ERROR,"SAAJResult is null");
        pass = false;
      } else {
        logger.log(Logger.Level.INFO,"SAAJResult was created");
      }
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Exception: " + e);
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

  private void getResultTest1(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("getResultTest1");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      logger.log(Logger.Level.INFO,"Create a SAAJResult object using constructor");
      SAAJResult sr = new SAAJResult();

      logger.log(Logger.Level.INFO,"Validating getResult object creation");
      Node node = sr.getResult();
      if (node == null) {
        logger.log(Logger.Level.INFO,"Node is null (expected)");
      } else {
        logger.log(Logger.Level.INFO,"Node tree was returned");
        XMLUtils.XmlDumpDOMNodes(node);
      }
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Exception: " + e);
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

  private void getResultTest2(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("getResultTest2");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      logger.log(Logger.Level.INFO,"Create SOAP message from message factory");
      SOAPMessage msg = SOAP_Util.getMessageFactory().createMessage();

      // Message creation takes care of creating the SOAPPart - a
      // required part of the message as per the SOAP 1.1 spec.
      logger.log(Logger.Level.INFO,"Get SOAP Part");
      SOAPPart sp = msg.getSOAPPart();

      // Retrieve the envelope from the soap part to start building
      // the soap message.
      logger.log(Logger.Level.INFO,"Get SOAP Envelope");
      SOAPEnvelope envelope = sp.getEnvelope();

      // Create a soap header from the envelope.
      logger.log(Logger.Level.INFO,"Create SOAP Header");
      SOAPHeader hdr = envelope.getHeader();

      // Create a soap body from the envelope.
      logger.log(Logger.Level.INFO,"Create SOAP Body");
      SOAPBody bdy = envelope.getBody();

      // Add some soap header elements
      logger.log(Logger.Level.INFO,"Add SOAP HeaderElement Header1");
      SOAPElement se = hdr
          .addHeaderElement(envelope.createName("Header1", NS_PREFIX, NS_URI))
          .addTextNode("This is Header1");
      SOAPHeaderElement she = (SOAPHeaderElement) se;
      she.setMustUnderstand(true);

      logger.log(Logger.Level.INFO,"Add SOAP HeaderElement Header2");
      se = hdr
          .addHeaderElement(envelope.createName("Header2", NS_PREFIX, NS_URI))
          .addTextNode("This is Header2");
      she = (SOAPHeaderElement) se;
      she.setMustUnderstand(false);

      logger.log(Logger.Level.INFO,"Add SOAP HeaderElement Header3");
      se = hdr
          .addHeaderElement(envelope.createName("Header3", NS_PREFIX, NS_URI))
          .addTextNode("This is Header3");
      she = (SOAPHeaderElement) se;
      she.setMustUnderstand(true);

      logger.log(Logger.Level.INFO,"Add SOAP HeaderElement Header4");
      se = hdr
          .addHeaderElement(envelope.createName("Header4", NS_PREFIX, NS_URI))
          .addTextNode("This is Header4");
      she = (SOAPHeaderElement) se;
      she.setMustUnderstand(false);

      // Add a soap body element
      logger.log(Logger.Level.INFO,"Add SOAP BodyElement Body1");
      SOAPBodyElement sbe = bdy
          .addBodyElement(envelope.createName("Body1", NS_PREFIX, NS_URI));

      // Add a some child elements
      logger.log(Logger.Level.INFO,"Add ChildElement Child1");
      sbe.addChildElement(envelope.createName("Child1", NS_PREFIX, NS_URI))
          .addTextNode("This is Child1");
      logger.log(Logger.Level.INFO,"Add ChildElement Child2");
      sbe.addChildElement(envelope.createName("Child2", NS_PREFIX, NS_URI))
          .addTextNode("This is Child2");
      logger.log(Logger.Level.INFO,"Done creating SOAP message");

      logger.log(Logger.Level.INFO,"Create a SAAJResult object using constructor");
      SAAJResult sr = new SAAJResult(msg);

      logger.log(Logger.Level.INFO,"Validating getResult object creation");
      Node node = sr.getResult();
      if (node == null) {
        logger.log(Logger.Level.ERROR,"Node is null");
        pass = false;
      } else {
        logger.log(Logger.Level.INFO,"Node tree was returned");
      }
      XMLUtils.XmlDumpDOMNodes(node);
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Exception: " + e);
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
