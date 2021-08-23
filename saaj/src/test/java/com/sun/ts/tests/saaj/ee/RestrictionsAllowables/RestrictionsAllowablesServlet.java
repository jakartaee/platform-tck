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

package com.sun.ts.tests.saaj.ee.RestrictionsAllowables;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import com.sun.ts.lib.porting.TSURL;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.saaj.common.SOAP_Util;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.soap.Name;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPBodyElement;
import jakarta.xml.soap.SOAPConnection;
import jakarta.xml.soap.SOAPConstants;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.soap.SOAPHeaderElement;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;

public class RestrictionsAllowablesServlet extends HttpServlet {
  private SOAPConnection con = null;

  private Properties harnessProps = null;

  private boolean debug = false;

  private String hostname = "localhost";

  private int portnum = 8080;

  private static final String cntxroot = "/RestrictionsAllowables_web";

  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private static final String SOAPVERSION = "SOAPVERSION";

  private static final String NS_PREFIX = "ns-prefix";

  private static final String NS_URI = "ns-uri";

  private static final String PROTOCOL = "http";

  private String soapVersion = null;

  private TSURL tsurl = new TSURL();

  private SOAPMessage message = null;

  private SOAPPart sp = null;

  private SOAPEnvelope envelope = null;

  private SOAPHeader hdr = null;

  private SOAPHeaderElement she = null;

  private SOAPBody body = null;

  private SOAPBodyElement bodye = null;

  private SOAPElement se = null;

  public void init(ServletConfig servletConfig) throws ServletException {
    super.init(servletConfig);
    System.out.println("RestrictionsAllowablesServlet:init (Entering)");
    try {
      SOAP_Util.setup();
      con = SOAP_Util.getSOAPConnection();
    } catch (Exception e) {
      System.err.println("Exception occurred: " + e.getMessage());
      e.printStackTrace(System.err);
      throw new ServletException("Exception occurred: " + e.getMessage());
    }
    System.out.println("RestrictionsAllowablesServlet:init (Leaving)");
  }

  public void doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("RestrictionsAllowablesServlet:doGet");
    System.out.println("RestrictionsAllowablesServlet:doGet");
    if (harnessProps.getProperty("TESTNAME")
        .equals("encodingStyleAttrSOAP11Test1")) {
      TestUtil.logMsg("Starting encodingStyleAttrSOAP11Test1");
      System.out.println("Starting encodingStyleAttrSOAP11Test1");
      encodingStyleAttrSOAP11Test1(req, res);
    } else if (harnessProps.getProperty("TESTNAME")
        .equals("encodingStyleAttrSOAP11Test2")) {
      TestUtil.logMsg("Starting encodingStyleAttrSOAP11Test2");
      System.out.println("Starting encodingStyleAttrSOAP11Test2");
      encodingStyleAttrSOAP11Test2(req, res);
    } else if (harnessProps.getProperty("TESTNAME")
        .equals("noTrailingBlockBodySOAP11Test")) {
      TestUtil.logMsg("Starting noTrailingBlockBodySOAP11Test");
      System.out.println("Starting noTrailingBlockBodySOAP11Test");
      noTrailingBlockBodySOAP11Test(req, res);
    } else if (harnessProps.getProperty("TESTNAME")
        .equals("enforcedQNameBodyElemSOAP11Test")) {
      TestUtil.logMsg("Starting enforcedQNameBodyElemSOAP11Test");
      System.out.println("Starting enforcedQNameBodyElemSOAP11Test");
      enforcedQNameBodyElemSOAP11Test(req, res);
    } else if (harnessProps.getProperty("TESTNAME")
        .equals("encodingStyleAttrSOAP12Test1")) {
      TestUtil.logMsg("Starting encodingStyleAttrSOAP12Test1");
      System.out.println("Starting encodingStyleAttrSOAP12Test1");
      encodingStyleAttrSOAP12Test1(req, res);
    } else if (harnessProps.getProperty("TESTNAME")
        .equals("encodingStyleAttrSOAP12Test2")) {
      TestUtil.logMsg("Starting encodingStyleAttrSOAP12Test2");
      System.out.println("Starting encodingStyleAttrSOAP12Test2");
      encodingStyleAttrSOAP12Test2(req, res);
    } else if (harnessProps.getProperty("TESTNAME")
        .equals("noTrailingBlockBodySOAP12Test")) {
      TestUtil.logMsg("Starting noTrailingBlockBodySOAP12Test");
      System.out.println("Starting noTrailingBlockBodySOAP12Test");
      noTrailingBlockBodySOAP12Test(req, res);
    } else if (harnessProps.getProperty("TESTNAME")
        .equals("enforcedQNameHdrElemTest1")) {
      TestUtil.logMsg("Starting enforcedQNameHdrElemTest1");
      System.out.println("Starting enforcedQNameHdrElemTest1");
      enforcedQNameHdrElemTest1(req, res);
    } else if (harnessProps.getProperty("TESTNAME")
        .equals("enforcedQNameHdrElemTest2")) {
      TestUtil.logMsg("Starting enforcedQNameHdrElemTest2");
      System.out.println("Starting enforcedQNameHdrElemTest2");
      enforcedQNameHdrElemTest2(req, res);
    } else if (harnessProps.getProperty("TESTNAME")
        .equals("enforcedQNameBodyElemSOAP12Test")) {
      TestUtil.logMsg("Starting enforcedQNameBodyElemSOAP12Test");
      System.out.println("Starting enforcedQNameBodyElemSOAP12Test");
      enforcedQNameBodyElemSOAP12Test(req, res);
    }
  }

  public void doPost(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("RestrictionsAllowablesServlet:doPost");
    System.out.println("RestrictionsAllowablesServlet:doPost");
    SOAP_Util.doServletPost(req, res);
    hostname = SOAP_Util.getHostname();
    portnum = SOAP_Util.getPortnum();
    soapVersion = SOAP_Util.getSOAPVersion();
    harnessProps = SOAP_Util.getHarnessProps();
    doGet(req, res);
  }

  private void setup() throws Exception {
    TestUtil.logTrace("setup");

    SOAP_Util.setup();

    // Create a message from the message factory.
    TestUtil.logMsg("Create message from message factory");
    message = SOAP_Util.getMessageFactory().createMessage();

    // Message creation takes care of creating the SOAPPart
    TestUtil.logMsg("Get SOAP Part");
    sp = message.getSOAPPart();

    // Retrieve the envelope from the soap part to start building
    // the soap message.
    TestUtil.logMsg("Get SOAP Envelope");
    envelope = sp.getEnvelope();

    // Retrieve the soap header from the envelope.
    TestUtil.logMsg("Get SOAP Header");
    hdr = envelope.getHeader();

    // Retrieve the soap header from the envelope.
    TestUtil.logMsg("Get SOAP Body");
    body = envelope.getBody();
  }

  /*
   * Test to verify encodingStyle attribute can be set on Envelope. This is
   * allowed for the SOAP1.1 protocol.
   */
  private void encodingStyleAttrSOAP11Test1(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    boolean pass = true;
    Properties resultProps = new Properties();

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();
      TestUtil.logMsg("SOAP1.1 allows the encodingStyle attribute"
          + " to be set on Envelope");
      TestUtil.logMsg("Call SOAPEnvelope.setEncodingStyle() and "
          + "(expect SOAPException)");
      envelope.setEncodingStyle("http://example.com/MyEncodings");
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception: " + e.getMessage());
      pass = false;
    }
    // Send response object and test result back to client
    if (pass)
      resultProps.setProperty("TESTRESULT", "pass");
    else
      resultProps.setProperty("TESTRESULT", "fail");
    resultProps.list(out);
  }

  /*
   * Test to verify encodingStyle attribute can be set on Envelope. This is
   * allowed for the SOAP1.1 protocol.
   */
  private void encodingStyleAttrSOAP11Test2(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    boolean pass = true;
    Properties resultProps = new Properties();

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();
      TestUtil.logMsg("SOAP1.1 does not allow encodingStyle attribute"
          + " to be set on Envelope");
      TestUtil.logMsg(
          "Call SOAPEnvelope.addAttribute() and " + "(expect SOAPException)");
      Name encodingStyle = envelope.createName("encodingStyle", "es",
          SOAPConstants.URI_NS_SOAP_ENVELOPE);
      envelope.addAttribute(encodingStyle, "http://example.com/MyEncodings");
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception: " + e.getMessage());
      pass = false;
    }
    // Send response object and test result back to client
    if (pass)
      resultProps.setProperty("TESTRESULT", "pass");
    else
      resultProps.setProperty("TESTRESULT", "fail");
    resultProps.list(out);
  }

  /*
   * Test to verify that trailing blocks are allowed after Body. This is allowed
   * for the SOAP1.1 protocol.
   */
  private void noTrailingBlockBodySOAP11Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    boolean pass = true;
    Properties resultProps = new Properties();

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();
      TestUtil.logMsg("SOAP1.1 allows trailing blocks after" + "the Body");
      TestUtil.logMsg(
          "Call SOAPEnvelope.addChildElement() and " + "(expect success)");
      Name afterBody = envelope.createName("AfterBody", "e", "some-uri");
      envelope.addChildElement(afterBody);
      TestUtil.logMsg("Successfully added trailing block after Body");
    } catch (SOAPException e) {
      TestUtil.logErr("Unexpected SOAPException: " + e.getMessage());
      pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception: " + e.getMessage());
      pass = false;
    }
    // Send response object and test result back to client
    if (pass)
      resultProps.setProperty("TESTRESULT", "pass");
    else
      resultProps.setProperty("TESTRESULT", "fail");
    resultProps.list(out);
  }

  /*
   * Test to verify non qualified QNames in BodyElements. This is allowed for
   * the SOAP1.1 protocol.
   */
  private void enforcedQNameBodyElemSOAP11Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    boolean pass = true;
    Properties resultProps = new Properties();

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();
      TestUtil.logMsg("SOAP1.1 does not require Body elements to be"
          + " namespace qualified");
      TestUtil.logMsg("Specifying no namespace on Body should succeed");
      body.addChildElement("nouri-just-localname");
      TestUtil
          .logMsg("Successfully created BodyElement with unqualified QName");
    } catch (SOAPException e) {
      TestUtil.logErr("Unexpected SOAPException: " + e.getMessage());
      pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception: " + e.getMessage());
      pass = false;
    }
    // Send response object and test result back to client
    if (pass)
      resultProps.setProperty("TESTRESULT", "pass");
    else
      resultProps.setProperty("TESTRESULT", "fail");
    resultProps.list(out);
  }

  /*
   * Test to verify encodingStyle attribute cannot be set on Envelope. This is
   * restricted for the SOAP1.2 protocol.
   */
  private void encodingStyleAttrSOAP12Test1(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    boolean pass = true;
    Properties resultProps = new Properties();

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();
      TestUtil.logMsg("SOAP1.2 does not allow encodingStyle attribute"
          + " to be set on Envelope");
      TestUtil.logMsg("Call SOAPEnvelope.setEncodingStyle() and "
          + "(expect SOAPException)");
      envelope.setEncodingStyle("http://example.com/MyEncodings");
      TestUtil.logErr("Did not throw expected SOAPException");
      pass = false;
    } catch (SOAPException e) {
      TestUtil.logMsg("Did throw expected SOAPException");
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception: " + e.getMessage());
      pass = false;
    }
    // Send response object and test result back to client
    if (pass)
      resultProps.setProperty("TESTRESULT", "pass");
    else
      resultProps.setProperty("TESTRESULT", "fail");
    resultProps.list(out);
  }

  /*
   * Test to verify encodingStyle attribute cannot be set on Envelope. This is
   * restricted for the SOAP1.2 protocol.
   */
  private void encodingStyleAttrSOAP12Test2(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    boolean pass = true;
    Properties resultProps = new Properties();

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();
      TestUtil.logMsg("SOAP1.2 does not allow encodingStyle attribute"
          + " to be set on Envelope");
      TestUtil.logMsg(
          "Call SOAPEnvelope.addAttribute() and " + "(expect SOAPException)");
      Name encodingStyle = envelope.createName("encodingStyle", "es",
          SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
      envelope.addAttribute(encodingStyle, "http://example.com/MyEncodings");
      TestUtil.logErr("Did not throw expected SOAPException");
      pass = false;
    } catch (SOAPException e) {
      TestUtil.logMsg("Did throw expected SOAPException");
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception: " + e.getMessage());
      pass = false;
    }
    // Send response object and test result back to client
    if (pass)
      resultProps.setProperty("TESTRESULT", "pass");
    else
      resultProps.setProperty("TESTRESULT", "fail");
    resultProps.list(out);
  }

  /*
   * Test to verify that no trailing blocks are allowed after Body. This is
   * restricted for the SOAP1.2 protocol.
   */
  private void noTrailingBlockBodySOAP12Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    boolean pass = true;
    Properties resultProps = new Properties();

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();
      TestUtil
          .logMsg("SOAP1.2 does not allow trailing blocks after" + "the Body");
      TestUtil.logMsg("Call SOAPEnvelope.addChildElement() and "
          + "(expect SOAPException)");
      Name afterBody = envelope.createName("AfterBody", "e", "some-uri");
      envelope.addChildElement(afterBody);
      TestUtil.logErr("Did not throw expected SOAPException");
      pass = false;
    } catch (SOAPException e) {
      TestUtil.logMsg("Did throw expected SOAPException");
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception: " + e.getMessage());
      pass = false;
    }
    // Send response object and test result back to client
    if (pass)
      resultProps.setProperty("TESTRESULT", "pass");
    else
      resultProps.setProperty("TESTRESULT", "fail");
    resultProps.list(out);
  }

  /*
   * Test to verify that unqualified QNames are not allowed on HeaderElements
   * for both SOAP 1.1 and SOAP 1.2 protocols.
   */
  private void enforcedQNameHdrElemTest1(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    boolean pass = true;
    Properties resultProps = new Properties();

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    // Try to add a headerelement not belonging to any namespace.
    try {
      setup();
      TestUtil.logMsg("SOAP1.1 and SOAP1.2 requires all HeaderElements to be"
          + " namespace qualified");
      TestUtil.logMsg("Try adding HeaderElement with unqualified QName "
          + "not belonging to any namespace (expect SOAPException)");
      TestUtil.logMsg("No URI and no PREFIX in QName");
      hdr.addHeaderElement(envelope.createName("Transaction"));
      TestUtil.logErr("Did not throw expected SOAPException");
      pass = false;
    } catch (SOAPException e) {
      TestUtil.logMsg("Did throw expected SOAPException");
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception: " + e.getMessage());
      pass = false;
    }
    // Send response object and test result back to client
    if (pass)
      resultProps.setProperty("TESTRESULT", "pass");
    else
      resultProps.setProperty("TESTRESULT", "fail");
    resultProps.list(out);
  }

  /*
   * Test to verify that unqualified QNames are not allowed for Header
   * ChildElements for both SOAP 1.1 and SOAP 1.2 protocols.
   */
  private void enforcedQNameHdrElemTest2(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    boolean pass = true;
    Properties resultProps = new Properties();

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();
      TestUtil
          .logMsg("SOAP1.1 and SOAP1.2 requires all Header ChildElements to be"
              + " namespace qualified");
      TestUtil
          .logMsg("Try adding ChildElement to Header with unqualified QName "
              + "not belonging to any namespace (expect SOAPException)");
      TestUtil.logMsg("No URI and no PREFIX in QName");
      hdr.addChildElement("MyChildElement");
      TestUtil.logErr("Did not throw expected SOAPException");
      pass = false;
    } catch (SOAPException e) {
      TestUtil.logMsg("Did throw expected SOAPException");
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception: " + e.getMessage());
      pass = false;
    }
    // Send response object and test result back to client
    if (pass)
      resultProps.setProperty("TESTRESULT", "pass");
    else
      resultProps.setProperty("TESTRESULT", "fail");
    resultProps.list(out);
  }

  /*
   * Test to verify non qualified QNames in BodyElements. According to the SOAP
   * 1.2 spec, body elements need not be namespace qualified, but header
   * elements must be. This is allowed for the SOAP1.2 protocol.
   */
  private void enforcedQNameBodyElemSOAP12Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    boolean pass = true;
    Properties resultProps = new Properties();

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();
      TestUtil.logMsg("SOAP1.2 does not require Body elements to be"
          + " namespace qualified");
      TestUtil.logMsg("Specifying no namespace on Body should succeed");
      body.addChildElement("nouri-just-localname");
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception: " + e.getMessage());
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
