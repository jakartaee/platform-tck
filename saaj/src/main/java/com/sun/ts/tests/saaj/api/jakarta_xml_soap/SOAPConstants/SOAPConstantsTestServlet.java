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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPConstants;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import javax.xml.namespace.QName;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.saaj.common.SOAP_Util;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPBodyElement;
import jakarta.xml.soap.SOAPConstants;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.soap.SOAPHeaderElement;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;

public class SOAPConstantsTestServlet extends HttpServlet {
  private String EXP_DEFAULT_SOAP_PROTOCOL = "SOAP 1.1 Protocol";

  private String EXP_SOAP_1_1_Protocol = "SOAP 1.1 Protocol";

  private String EXP_SOAP_1_1_CONTENT_TYPE = "text/xml";

  private String EXP_URI_NS_SOAP_ENCODING = "http://schemas.xmlsoap.org/soap/encoding/";

  private String EXP_URI_NS_SOAP_ENVELOPE = "http://schemas.xmlsoap.org/soap/envelope/";

  private String EXP_URI_NS_SOAP_1_1_ENVELOPE = EXP_URI_NS_SOAP_ENVELOPE;

  private String EXP_URI_SOAP_ACTOR_NEXT = "http://schemas.xmlsoap.org/soap/actor/next";

  private String EXP_DYNAMIC_SOAP_PROTOCOL = "Dynamic Protocol";

  private String EXP_SOAP_1_2_Protocol = "SOAP 1.2 Protocol";

  private String EXP_SOAP_1_2_CONTENT_TYPE = "application/soap+xml";

  private String EXP_URI_NS_SOAP_1_2_ENCODING = "http://www.w3.org/2003/05/soap-encoding";

  private String EXP_URI_NS_SOAP_1_2_ENVELOPE = "http://www.w3.org/2003/05/soap-envelope";

  private String EXP_URI_SOAP_1_2_ROLE_NEXT = EXP_URI_NS_SOAP_1_2_ENVELOPE
      + "/role/next";

  private String EXP_URI_SOAP_1_2_ROLE_NONE = EXP_URI_NS_SOAP_1_2_ENVELOPE
      + "/role/none";

  private String EXP_URI_SOAP_1_2_ROLE_ULTIMATE_RECEIVER = EXP_URI_NS_SOAP_1_2_ENVELOPE
      + "/role/ultimateReceiver";

  private QName EXP_DATAENCODINGUNKNOWN_FAULT = new QName(
      EXP_URI_NS_SOAP_1_2_ENVELOPE, "DataEncodingUnknown");

  private QName EXP_MUSTUNDERSTAND_FAULT = new QName(
      EXP_URI_NS_SOAP_1_2_ENVELOPE, "MustUnderstand");

  private QName EXP_RECEIVER_FAULT = new QName(EXP_URI_NS_SOAP_1_2_ENVELOPE,
      "Receiver");

  private QName EXP_SENDER_FAULT = new QName(EXP_URI_NS_SOAP_1_2_ENVELOPE,
      "Sender");

  private QName EXP_VERSIONMISMATCH_FAULT = new QName(
      EXP_URI_NS_SOAP_1_2_ENVELOPE, "VersionMismatch");

  private MessageFactory mf = null;

  private SOAPMessage msg = null;

  private SOAPPart sp = null;

  private SOAPEnvelope envelope = null;

  private SOAPHeader hdr = null;

  private SOAPHeaderElement she = null;

  private SOAPBody body = null;

  private SOAPBodyElement bodye = null;

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
    if (testname.equals("SOAPConstantsTest")) {
      TestUtil.logMsg("Starting SOAPConstantsTest");
      SOAPConstantsTest(req, res);
    } else {
      throw new ServletException(
          "The testname '" + testname + "' was not found in the test servlet");
    }
  }

  public void init(ServletConfig servletConfig) throws ServletException {
    super.init(servletConfig);
    System.out.println("SOAPConstantsTestServlet:init (Entering)");
    SOAP_Util.doServletInit(servletConfig);
    System.out.println("SOAPConstantsTestServlet:init (Leaving)");
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

  private void SOAPConstantsTest(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("SOAPConstantsTest");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Dumping SOAP constants");
      TestUtil.logMsg("SOAP_1_1_PROTOCOL=" + SOAPConstants.SOAP_1_1_PROTOCOL);
      TestUtil.logMsg("SOAP_1_2_PROTOCOL=" + SOAPConstants.SOAP_1_2_PROTOCOL);
      TestUtil.logMsg(
          "DEFAULT_SOAP_PROTOCOL=" + SOAPConstants.DEFAULT_SOAP_PROTOCOL);
      TestUtil.logMsg(
          "DYNAMIC_SOAP_PROTOCOL=" + SOAPConstants.DYNAMIC_SOAP_PROTOCOL);
      TestUtil.logMsg(
          "SOAP_1_1_CONTENT_TYPE=" + SOAPConstants.SOAP_1_1_CONTENT_TYPE);
      TestUtil.logMsg(
          "SOAP_1_2_CONTENT_TYPE=" + SOAPConstants.SOAP_1_2_CONTENT_TYPE);
      TestUtil
          .logMsg("URI_NS_SOAP_ENCODING=" + SOAPConstants.URI_NS_SOAP_ENCODING);
      TestUtil
          .logMsg("URI_NS_SOAP_ENVELOPE=" + SOAPConstants.URI_NS_SOAP_ENVELOPE);
      TestUtil
          .logMsg("URI_SOAP_ACTOR_NEXT=" + SOAPConstants.URI_SOAP_ACTOR_NEXT);
      TestUtil.logMsg(
          "URI_NS_SOAP_1_1_ENVELOPE=" + SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE);
      TestUtil.logMsg(
          "URI_NS_SOAP_1_2_ENCODING=" + SOAPConstants.URI_NS_SOAP_1_2_ENCODING);
      TestUtil.logMsg(
          "URI_NS_SOAP_1_2_ENVELOPE=" + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
      TestUtil.logMsg(
          "URI_SOAP_1_2_ROLE_NEXT=" + SOAPConstants.URI_SOAP_1_2_ROLE_NEXT);
      TestUtil.logMsg(
          "URI_SOAP_1_2_ROLE_NONE=" + SOAPConstants.URI_SOAP_1_2_ROLE_NONE);
      TestUtil.logMsg("URI_SOAP_1_2_ROLE_ULTIMATE_RECEIVER="
          + SOAPConstants.URI_SOAP_1_2_ROLE_ULTIMATE_RECEIVER);
      TestUtil.logMsg("SOAP_ENV_PREFIX=" + SOAPConstants.SOAP_ENV_PREFIX);
      TestUtil.logMsg("SOAP_DATAENCODINGUNKNOWN_FAULT="
          + SOAPConstants.SOAP_DATAENCODINGUNKNOWN_FAULT);
      TestUtil.logMsg("SOAP_MUSTUNDERSTAND_FAULT="
          + SOAPConstants.SOAP_MUSTUNDERSTAND_FAULT);
      TestUtil
          .logMsg("SOAP_RECEIVER_FAULT=" + SOAPConstants.SOAP_RECEIVER_FAULT);
      TestUtil.logMsg("SOAP_SENDER_FAULT=" + SOAPConstants.SOAP_SENDER_FAULT);
      TestUtil.logMsg("SOAP_VERSIONMISMATCH_FAULT="
          + SOAPConstants.SOAP_VERSIONMISMATCH_FAULT);
      TestUtil.logMsg("Verifying SOAP constants");
      if (!SOAPConstants.SOAP_1_1_CONTENT_TYPE
          .equals(EXP_SOAP_1_1_CONTENT_TYPE)) {
        TestUtil.logErr("SOAP_1_1_CONTENT_TYPE has wrong value");
        TestUtil.logErr("Expected: " + EXP_SOAP_1_1_CONTENT_TYPE);
        TestUtil.logErr("Received: " + SOAPConstants.SOAP_1_1_CONTENT_TYPE);
        pass = false;
      }
      if (!SOAPConstants.URI_NS_SOAP_ENCODING
          .equals(EXP_URI_NS_SOAP_ENCODING)) {
        TestUtil.logErr("URI_NS_SOAP_ENCODING has wrong value");
        TestUtil.logErr("Expected: " + EXP_URI_NS_SOAP_ENCODING);
        TestUtil.logErr("Received: " + SOAPConstants.URI_NS_SOAP_ENCODING);
        pass = false;
      }
      if (!SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE
          .equals(EXP_URI_NS_SOAP_1_1_ENVELOPE)) {
        TestUtil.logErr("URI_NS_SOAP_1_1_ENVELOPE has wrong value");
        TestUtil.logErr("Expected: " + EXP_URI_NS_SOAP_1_1_ENVELOPE);
        TestUtil.logErr("Received: " + SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE);
        pass = false;
      }
      if (!SOAPConstants.URI_NS_SOAP_ENVELOPE
          .equals(EXP_URI_NS_SOAP_ENVELOPE)) {
        TestUtil.logErr("URI_NS_SOAP_ENVELOPE has wrong value");
        TestUtil.logErr("Expected: " + EXP_URI_NS_SOAP_ENVELOPE);
        TestUtil.logErr("Received: " + SOAPConstants.URI_NS_SOAP_ENVELOPE);
        pass = false;
      }
      if (!SOAPConstants.URI_SOAP_ACTOR_NEXT.equals(EXP_URI_SOAP_ACTOR_NEXT)) {
        TestUtil.logErr("URI_SOAP_ACTOR_NEXT has wrong value");
        TestUtil.logErr("Expected: " + EXP_URI_SOAP_ACTOR_NEXT);
        TestUtil.logErr("Received: " + SOAPConstants.URI_SOAP_ACTOR_NEXT);
        pass = false;
      }
      if (!SOAPConstants.SOAP_1_2_CONTENT_TYPE
          .equals(EXP_SOAP_1_2_CONTENT_TYPE)) {
        TestUtil.logErr("SOAP_1_2_CONTENT_TYPE has wrong value");
        TestUtil.logErr("Expected: " + EXP_SOAP_1_2_CONTENT_TYPE);
        TestUtil.logErr("Received: " + SOAPConstants.SOAP_1_2_CONTENT_TYPE);
        pass = false;
      }
      if (!SOAPConstants.URI_NS_SOAP_1_2_ENCODING
          .equals(EXP_URI_NS_SOAP_1_2_ENCODING)) {
        TestUtil.logErr("URI_NS_SOAP_1_2_ENCODING has wrong value");
        TestUtil.logErr("Expected: " + EXP_URI_NS_SOAP_1_2_ENCODING);
        TestUtil.logErr("Received: " + SOAPConstants.URI_NS_SOAP_1_2_ENCODING);
        pass = false;
      }
      if (!SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE
          .equals(EXP_URI_NS_SOAP_1_2_ENVELOPE)) {
        TestUtil.logErr("URI_NS_SOAP_1_2_ENVELOPE has wrong value");
        TestUtil.logErr("Expected: " + EXP_URI_NS_SOAP_1_2_ENVELOPE);
        TestUtil.logErr("Received: " + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
        pass = false;
      }
      if (!SOAPConstants.URI_SOAP_1_2_ROLE_NEXT
          .equals(EXP_URI_SOAP_1_2_ROLE_NEXT)) {
        TestUtil.logErr("URI_SOAP_1_2_ROLE_NEXT has wrong value");
        TestUtil.logErr("Expected: " + EXP_URI_SOAP_1_2_ROLE_NEXT);
        TestUtil.logErr("Received: " + SOAPConstants.URI_SOAP_1_2_ROLE_NEXT);
        pass = false;
      }
      if (!SOAPConstants.URI_SOAP_1_2_ROLE_NONE
          .equals(EXP_URI_SOAP_1_2_ROLE_NONE)) {
        TestUtil.logErr("URI_SOAP_1_2_ROLE_NONE has wrong value");
        TestUtil.logErr("Expected: " + EXP_URI_SOAP_1_2_ROLE_NONE);
        TestUtil.logErr("Received: " + SOAPConstants.URI_SOAP_1_2_ROLE_NONE);
        pass = false;
      }
      if (!SOAPConstants.URI_SOAP_1_2_ROLE_ULTIMATE_RECEIVER
          .equals(EXP_URI_SOAP_1_2_ROLE_ULTIMATE_RECEIVER)) {
        TestUtil.logErr("URI_SOAP_1_2_ROLE_ULTIMATE_RECEIVER has wrong value");
        TestUtil.logErr("Expected: " + EXP_URI_SOAP_1_2_ROLE_ULTIMATE_RECEIVER);
        TestUtil.logErr(
            "Received: " + SOAPConstants.URI_SOAP_1_2_ROLE_ULTIMATE_RECEIVER);
        pass = false;
      }
      if (!SOAPConstants.SOAP_DATAENCODINGUNKNOWN_FAULT
          .equals(EXP_DATAENCODINGUNKNOWN_FAULT)) {
        TestUtil.logErr("SOAP_DATAENCODINGUNKNOWN_FAULT has wrong value");
        TestUtil.logErr("Expected: " + EXP_DATAENCODINGUNKNOWN_FAULT);
        TestUtil.logErr(
            "Received: " + SOAPConstants.SOAP_DATAENCODINGUNKNOWN_FAULT);
        pass = false;
      }
      if (!SOAPConstants.SOAP_MUSTUNDERSTAND_FAULT
          .equals(EXP_MUSTUNDERSTAND_FAULT)) {
        TestUtil.logErr("SOAP_MUSTUNDERSTAND_FAULT has wrong value");
        TestUtil.logErr("Expected: " + EXP_MUSTUNDERSTAND_FAULT);
        TestUtil.logErr("Received: " + SOAPConstants.SOAP_MUSTUNDERSTAND_FAULT);
        pass = false;
      }
      if (!SOAPConstants.SOAP_RECEIVER_FAULT.equals(EXP_RECEIVER_FAULT)) {
        TestUtil.logErr("SOAP_RECEIVER_FAULT has wrong value");
        TestUtil.logErr("Expected: " + EXP_RECEIVER_FAULT);
        TestUtil.logErr("Received: " + SOAPConstants.SOAP_RECEIVER_FAULT);
        pass = false;
      }
      if (!SOAPConstants.SOAP_SENDER_FAULT.equals(EXP_SENDER_FAULT)) {
        TestUtil.logErr("SOAP_SENDER_FAULT has wrong value");
        TestUtil.logErr("Expected: " + EXP_SENDER_FAULT);
        TestUtil.logErr("Received: " + SOAPConstants.SOAP_SENDER_FAULT);
        pass = false;
      }
      if (!SOAPConstants.SOAP_VERSIONMISMATCH_FAULT
          .equals(EXP_VERSIONMISMATCH_FAULT)) {
        TestUtil.logErr("SOAP_VERSIONMISMATCH_FAULT has wrong value");
        TestUtil.logErr("Expected: " + EXP_VERSIONMISMATCH_FAULT);
        TestUtil
            .logErr("Received: " + SOAPConstants.SOAP_VERSIONMISMATCH_FAULT);
        pass = false;
      }
      if (pass)
        TestUtil.logMsg("All SOAP constants are correct");
      else
        TestUtil.logErr("Some SOAP constants are incorrect");
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
