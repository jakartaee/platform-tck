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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.Detail;

import java.io.IOException;
import java.io.PrintWriter;
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
import jakarta.xml.soap.DetailEntry;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.Name;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPBodyElement;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPFault;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.soap.SOAPHeaderElement;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;

public class DetailTestServlet extends HttpServlet {
  private MessageFactory mf = null;

  private SOAPMessage msg = null;

  private SOAPPart sp = null;

  private SOAPEnvelope envelope = null;

  private SOAPHeader hdr = null;

  private SOAPHeaderElement she = null;

  private SOAPBody body = null;

  private SOAPBodyElement bodye = null;

  private SOAPElement se = null;

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

    // Retrieve the soap body from the envelope.
    TestUtil.logMsg("Get SOAP Body");
    body = envelope.getBody();
  }

  private void dispatch(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("dispatch");
    String testname = SOAP_Util.getHarnessProps().getProperty("TESTNAME");
    if (testname.equals("addDetailEntryTest1")) {
      TestUtil.logMsg("Starting addDetailEntryTest1");
      addDetailEntryTest1(req, res);
    } else if (testname.equals("addDetailEntryTest2")) {
      TestUtil.logMsg("Starting addDetailEntryTest2");
      addDetailEntryTest2(req, res);
    } else if (testname.equals("getDetailEntriesTest")) {
      TestUtil.logMsg("Starting getDetailEntriesTest");
      getDetailEntriesTest(req, res);
    } else {
      throw new ServletException(
          "The testname '" + testname + "' was not found in the test servlet");
    }
  }

  public void init(ServletConfig servletConfig) throws ServletException {
    super.init(servletConfig);
    System.out.println("DetailTestServlet:init (Entering)");
    SOAP_Util.doServletInit(servletConfig);
    System.out.println("DetailTestServlet:init (Leaving)");
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

  private void addDetailEntryTest1(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("addDetailEntryTest1");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();
      TestUtil.logMsg("Add a SOAPFault object to the SOAPBody");
      SOAPFault sf = body.addFault();
      TestUtil.logMsg("Add a Detail object to the SOAPFault object");
      Detail d = sf.addDetail();
      Name name = envelope.createName("GetLastTradePrice", "WOMBAT",
          "http://www.wombat.org/trader");
      TestUtil.logMsg("Add a DetailEntry object to the Detail object");
      DetailEntry de = d.addDetailEntry(name);
      TestUtil.logMsg("Successfully created DetailEntry object");
      if (de == null) {
        TestUtil.logErr("addDetailEntry() returned null");
        pass = false;
      } else
        TestUtil.logMsg("Successfully created DetailEntry object");
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

  private void addDetailEntryTest2(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("addDetailEntryTest2");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();
      TestUtil.logMsg("Add a SOAPFault object to the SOAPBody");
      SOAPFault sf = body.addFault();
      TestUtil.logMsg("Add a Detail object to the SOAPFault object");
      Detail d = sf.addDetail();
      QName name = new QName("http://www.wombat.org/trader",
          "GetLastTradePrice", "WOMBAT");
      TestUtil.logMsg("Add a DetailEntry object to the Detail object");
      DetailEntry de = d.addDetailEntry(name);
      TestUtil.logMsg("Successfully created DetailEntry object");
      if (de == null) {
        TestUtil.logErr("addDetailEntry() returned null");
        pass = false;
      } else
        TestUtil.logMsg("Successfully created DetailEntry object");
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

  private void getDetailEntriesTest(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getDetailEntriesTest");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();
      TestUtil.logMsg("Add a SOAPFault object to the SOAPBody");
      SOAPFault sf = body.addFault();
      TestUtil.logMsg("Add a Detail object to the SOAPFault object");
      Detail d = sf.addDetail();
      Name name1 = envelope.createName("GetLastTradePrice", "WOMBAT",
          "http://www.wombat.org/trader");
      Name name2 = envelope.createName("GetCurrentTradePrice", "WOMBAT",
          "http://www.wombat.org/trader");
      TestUtil.logMsg("Add DetailEntry object GetLastTradePrice");
      d.addDetailEntry(name1);
      TestUtil.logMsg("Add DetailEntry object GetCurrentTradePrice");
      d.addDetailEntry(name2);
      TestUtil.logMsg("Get iterator of DetailEntry objects (should get 2)");
      Iterator i = d.getDetailEntries();
      int count = SOAP_Util.getIteratorCount(i);
      i = d.getDetailEntries();
      if (count != 2) {
        TestUtil.logErr(
            "Wrong iterator count returned of " + count + ", expected 2");
        pass = false;
      } else {
        boolean foundName1 = false;
        boolean foundName2 = false;
        TestUtil.logMsg("Verify the DetailEntry objects");
        while (i.hasNext()) {
          DetailEntry de = (DetailEntry) i.next();
          TestUtil.logMsg("Got DetailEntry = " + de.toString());
          String s = de.getElementName().getLocalName();
          if (s.equals("GetLastTradePrice"))
            foundName1 = true;
          else if (s.equals("GetCurrentTradePrice"))
            foundName2 = true;
          else {
            TestUtil.logErr("Unexpected DetailEntry of " + s);
            pass = false;
          }
        }
        if (!foundName1 || !foundName2) {
          TestUtil.logErr("Did not find all expected DetailEntry objects");
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
