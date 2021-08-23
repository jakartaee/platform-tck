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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPFault;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Locale;
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
import jakarta.xml.soap.SOAPConstants;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPFactory;
import jakarta.xml.soap.SOAPFault;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;

public class SOAPFaultTestServlet extends HttpServlet {
  private MessageFactory mf = null;

  private SOAPFactory sfactory = null;

  private SOAPMessage msg = null;

  private SOAPPart sp = null;

  private SOAPEnvelope envelope = null;

  private SOAPBody body = null;

  private SOAPFault sf = null;

  private String prefix = null;

  private void setup() throws Exception {
    TestUtil.logTrace("setup");

    SOAP_Util.setup();

    // Create a message from the message factory.
    TestUtil.logMsg("Create message from message factory");
    msg = SOAP_Util.getMessageFactory().createMessage();
    sfactory = SOAP_Util.getSOAPFactory();

    // Message creation takes care of creating the SOAPPart - a
    // required part of the message as per the SOAP 1.1 spec.
    TestUtil.logMsg("Get SOAP Part");
    sp = msg.getSOAPPart();

    // Retrieve the envelope from the soap part to start building
    // the soap message.
    TestUtil.logMsg("Get SOAP Envelope");
    envelope = sp.getEnvelope();
    prefix = envelope.getElementName().getPrefix();

    // Retrieve the soap header from the envelope.
    TestUtil.logMsg("Get SOAP Body");
    body = envelope.getBody();

    TestUtil.logMsg("Creating SOAPFault");
    sf = body.addFault();
  }

  private void dispatch(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("dispatch");
    String testname = SOAP_Util.getHarnessProps().getProperty("TESTNAME");
    if (testname.equals("SetGetFaultString1Test")) {
      TestUtil.logMsg("Starting SetGetFaultString1Test");
      SetGetFaultString1Test(req, res);
    } else if (testname.equals("SetGetFaultCode1Test")) {
      TestUtil.logMsg("Starting SetGetFaultCode1Test");
      SetGetFaultCode1Test(req, res);
    } else if (testname.equals("SetGetFaultActor1Test")) {
      TestUtil.logMsg("Starting SetGetFaultActor1Test");
      SetGetFaultActor1Test(req, res);
    } else if (testname.equals("AddGetDetail1Test")) {
      TestUtil.logMsg("Starting AddGetDetail1Test");
      AddGetDetail1Test(req, res);
    } else if (testname.equals("addFaultReasonText1Test")) {
      TestUtil.logMsg("Starting addFaultReasonText1Test");
      addFaultReasonText1Test(req, res);
    } else if (testname.equals("addFaultReasonText2Test")) {
      TestUtil.logMsg("Starting addFaultReasonText2Test");
      addFaultReasonText2Test(req, res);
    } else if (testname.equals("addFaultReasonText3Test")) {
      TestUtil.logMsg("Starting addFaultReasonText3Test");
      addFaultReasonText3Test(req, res);
    } else if (testname.equals("addFaultReasonText4Test")) {
      TestUtil.logMsg("Starting addFaultReasonText4Test");
      addFaultReasonText4Test(req, res);
    } else if (testname.equals("getFaultReasonText1Test")) {
      TestUtil.logMsg("Starting getFaultReasonText1Test");
      getFaultReasonText1Test(req, res);
    } else if (testname.equals("getFaultReasonText2Test")) {
      TestUtil.logMsg("Starting getFaultReasonText2Test");
      getFaultReasonText2Test(req, res);
    } else if (testname.equals("getFaultReasonText3Test")) {
      TestUtil.logMsg("Starting getFaultReasonText3Test");
      getFaultReasonText3Test(req, res);
    } else if (testname.equals("getFaultReasonTexts1Test")) {
      TestUtil.logMsg("Starting getFaultReasonTexts1Test");
      getFaultReasonTexts1Test(req, res);
    } else if (testname.equals("getFaultReasonTexts2Test")) {
      TestUtil.logMsg("Starting getFaultReasonTexts2Test");
      getFaultReasonTexts2Test(req, res);
    } else if (testname.equals("getFaultReasonTexts3Test")) {
      TestUtil.logMsg("Starting getFaultReasonTexts3Test");
      getFaultReasonTexts3Test(req, res);
    } else if (testname.equals("setFaultNode1Test")) {
      TestUtil.logMsg("Starting setFaultNode1Test");
      setFaultNode1Test(req, res);
    } else if (testname.equals("SetGetFaultNode1Test")) {
      TestUtil.logMsg("Starting SetGetFaultNode1Test");
      SetGetFaultNode1Test(req, res);
    } else if (testname.equals("SetGetFaultNode2Test")) {
      TestUtil.logMsg("Starting SetGetFaultNode2Test");
      SetGetFaultNode2Test(req, res);
    } else if (testname.equals("getFaultNode1Test")) {
      TestUtil.logMsg("Starting getFaultNode1Test");
      getFaultNode1Test(req, res);
    } else if (testname.equals("getFaultNode2Test")) {
      TestUtil.logMsg("Starting getFaultNode2Test");
      getFaultNode2Test(req, res);
    } else if (testname.equals("setFaultRole1Test")) {
      TestUtil.logMsg("Starting setFaultRole1Test");
      setFaultRole1Test(req, res);
    } else if (testname.equals("SetGetFaultRole1Test")) {
      TestUtil.logMsg("Starting SetGetFaultRole1Test");
      SetGetFaultRole1Test(req, res);
    } else if (testname.equals("getFaultRole1Test")) {
      TestUtil.logMsg("Starting getFaultRole1Test");
      getFaultRole1Test(req, res);
    } else if (testname.equals("getFaultRole2Test")) {
      TestUtil.logMsg("Starting getFaultRole2Test");
      getFaultRole2Test(req, res);
    } else if (testname.equals("SetGetFaultStringLocale1Test")) {
      TestUtil.logMsg("Starting SetGetFaultStringLocale1Test");
      SetGetFaultStringLocale1Test(req, res);
    } else if (testname.equals("setFaultStringLocale1Test")) {
      TestUtil.logMsg("Starting setFaultStringLocale1Test");
      setFaultStringLocale1Test(req, res);
    } else if (testname.equals("getFaultStringLocale1SOAP11Test")) {
      TestUtil.logMsg("Starting getFaultStringLocale1SOAP11Test");
      getFaultStringLocale1SOAP11Test(req, res);
    } else if (testname.equals("getFaultStringLocale1SOAP12Test")) {
      TestUtil.logMsg("Starting getFaultStringLocale1SOAP12Test");
      getFaultStringLocale1SOAP12Test(req, res);
    } else if (testname.equals("SetGetFaultCodeAsName1Test")) {
      TestUtil.logMsg("Starting SetGetFaultCodeAsName1Test");
      SetGetFaultCodeAsName1Test(req, res);
    } else if (testname.equals("SetGetFaultCodeAsQName1Test")) {
      TestUtil.logMsg("Starting SetGetFaultCodeAsQName1Test");
      SetGetFaultCodeAsQName1Test(req, res);
    } else if (testname.equals("getFaultReasonLocales1Test")) {
      TestUtil.logMsg("Starting getFaultReasonLocales1Test");
      getFaultReasonLocales1Test(req, res);
    } else if (testname.equals("getFaultReasonLocales2Test")) {
      TestUtil.logMsg("Starting getFaultReasonLocales2Test");
      getFaultReasonLocales2Test(req, res);
    } else if (testname.equals("appendFaultSubcode1Test")) {
      TestUtil.logMsg("Starting appendFaultSubcode1Test");
      appendFaultSubcode1Test(req, res);
    } else if (testname.equals("appendFaultSubcode2Test")) {
      TestUtil.logMsg("Starting appendFaultSubcode2Test");
      appendFaultSubcode2Test(req, res);
    } else if (testname.equals("getFaultSubcodes1Test")) {
      TestUtil.logMsg("Starting getFaultSubcodes1Test");
      getFaultSubcodes1Test(req, res);
    } else if (testname.equals("getFaultSubcodes2Test")) {
      TestUtil.logMsg("Starting getFaultSubcodes2Test");
      getFaultSubcodes2Test(req, res);
    } else if (testname.equals("hasDetail1Test")) {
      TestUtil.logMsg("Starting hasDetail1Test");
      hasDetail1Test(req, res);
    } else if (testname.equals("hasDetail2Test")) {
      TestUtil.logMsg("Starting hasDetail2Test");
      hasDetail2Test(req, res);
    } else if (testname.equals("removeAllFaultSubcodes1Test")) {
      TestUtil.logMsg("Starting removeAllFaultSubcodes1Test");
      removeAllFaultSubcodes1Test(req, res);
    } else if (testname.equals("removeAllFaultSubcodes2Test")) {
      TestUtil.logMsg("Starting removeAllFaultSubcodes2Test");
      removeAllFaultSubcodes2Test(req, res);
    } else if (testname.equals("SetFaultCodeNameSOAPExceptionTest")) {
      TestUtil.logMsg("Starting SetFaultCodeNameSOAPExceptionTest");
      SetFaultCodeNameSOAPExceptionTest(req, res);
    } else if (testname.equals("SetFaultCodeQNameSOAPExceptionTest")) {
      TestUtil.logMsg("Starting SetFaultCodeQNameSOAPExceptionTest");
      SetFaultCodeQNameSOAPExceptionTest(req, res);
    } else if (testname.equals("SetFaultCodeStringSOAPExceptionTest")) {
      TestUtil.logMsg("Starting SetFaultCodeStringSOAPExceptionTest");
      SetFaultCodeStringSOAPExceptionTest(req, res);
    } else if (testname.equals("AppendFaultSubcodeSOAPExceptionTest")) {
      TestUtil.logMsg("Starting AppendFaultSubcodeSOAPExceptionTest");
      AppendFaultSubcodeSOAPExceptionTest(req, res);
    } else if (testname.equals("AddDetailSOAPExceptionTest")) {
      TestUtil.logMsg("Starting AddDetailSOAPExceptionTest");
      AddDetailSOAPExceptionTest(req, res);
    } else {
      throw new ServletException(
          "The testname '" + testname + "' was not found in the test servlet");
    }
  }

  public void init(ServletConfig servletConfig) throws ServletException {
    super.init(servletConfig);
    System.out.println("SOAPFaultTestServlet:init (Entering)");
    SOAP_Util.doServletInit(servletConfig);
    System.out.println("SOAPFaultTestServlet:init (Leaving)");
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

  private void SetGetFaultString1Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("SetGetFaultString1Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      String expected = "this is the fault string";
      TestUtil.logMsg("Setting fault string");
      sf.setFaultString(expected);

      TestUtil.logMsg("Getting fault string");
      String result = sf.getFaultString();
      if (result != null) {
        if (!result.equals(expected)) {
          TestUtil
              .logErr("setFaultString()/getFaultString() behaved incorrectly");
          TestUtil.logErr("expected=" + expected);
          pass = false;
        } else {
          TestUtil.logMsg("result=" + result);
        }
      } else {
        TestUtil.logErr("getFaultString() returned a null result");
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

  private void SetGetFaultCode1Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("SetGetFaultCode1Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
        String expected = prefix + ":Server";
        TestUtil.logMsg("Setting fault code to:" + expected);
        sf.setFaultCode(expected);
        TestUtil.logMsg("Getting fault code");
        String result = sf.getFaultCode();
        if (result != null) {
          if (!result.equals(expected)) {
            TestUtil
                .logErr("setFaultCode()/getFaultCode() behaved incorrectly");
            pass = false;
          }
          TestUtil.logMsg("result=" + result);
        } else {
          TestUtil.logErr("getFaultCode() returned a null");
          pass = false;
        }
      } else {
        QName qname = SOAPConstants.SOAP_SENDER_FAULT;
        String expected = prefix + ":" + qname.getLocalPart();
        TestUtil.logMsg("Setting fault code to:" + expected);
        sf.setFaultCode(expected);
        TestUtil.logMsg("Getting fault code");
        String result = sf.getFaultCode();
        if (result != null) {
          if (!result.equals(expected)) {
            TestUtil
                .logErr("setFaultCode()/getFaultCode() behaved incorrectly");
            pass = false;
          }
          TestUtil.logMsg("result=" + result);
        } else {
          TestUtil.logErr("getFaultCode() returned a null");
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

  private void SetGetFaultActor1Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("SetGetFaultActor1Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      String expected = "http://www.my.org/faultActorURI";
      TestUtil.logMsg("Calling setFaultActor()");
      sf.setFaultActor(expected);
      TestUtil.logMsg("Calling getFaultActor()");
      String result = sf.getFaultActor();
      if (result != null) {
        if (!result.equals(expected)) {
          TestUtil
              .logErr("setFaultActor()/getFaultActor() behaved incorrectly");
          TestUtil.logErr(
              "setFaultActor=" + expected + ", getFaultActor=" + result);
          pass = false;
        } else
          TestUtil.logMsg("setFaultActor()/getFaultActor() behaved correctly");
      } else {
        TestUtil.logErr(
            "getFaultActor() returned a null result, eventhough an actor was set");
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

  private void getFaultCode1Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getFaultCode1Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Setting fault code");
      sf.setFaultCode(prefix + ":Server");

      TestUtil.logMsg("Getting fault code");
      String result = sf.getFaultCode();
      if (result != null) {
        if (!result.equals(prefix + ":Server")) {
          TestUtil.logErr("Error: getFaultCode() returned an incorrect result");
          TestUtil.logErr("result=" + result);
          pass = false;
        } else {
          TestUtil.logMsg("result=" + result);
        }
      } else {
        TestUtil.logErr("getFaultCode() returned a null");
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

  private void AddGetDetail1Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("AddGetDetail1Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      Detail d1 = null;
      d1 = sf.addDetail();
      Name name = envelope.createName("GetLastTradePrice", "WOMBAT",
          "http://www.wombat.org/trader");
      d1.addDetailEntry(name);

      Detail d2 = sf.getDetail();
      if (d2 == null) {
        TestUtil.logErr("getDetail() returned null");
        pass = false;
      } else {
        TestUtil.logMsg("getDetail() returned a non-null detail");
        Iterator i = d2.getDetailEntries();
        int count = SOAP_Util.getIteratorCount(i);
        i = body.getAllAttributes();
        if (count != 1) {
          TestUtil.logErr(
              "Wrong iterator count returned of " + count + ", expected 1");
          pass = false;
        } else {
          boolean foundName1 = false;
          while (i.hasNext()) {
            DetailEntry de = (DetailEntry) i.next();
            TestUtil.logMsg("Got DetailEntry = " + de.toString());
            String s = de.getValue();
            if (s.equals("GetLastTradePrice"))
              foundName1 = true;
            else {
              TestUtil.logErr("Bad DetailEntry has name of " + s);
              pass = false;
            }
          }
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

  private void addFaultReasonText1Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("addFaultReasonText1Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Calling addFaultReasonText() must"
          + " throw UnsupportedOperationException");
      sf.addFaultReasonText("Its my fault", Locale.ENGLISH);
      TestUtil.logErr("Did not throw UnsupportedOperationException");
      pass = false;
    } catch (UnsupportedOperationException e) {
      TestUtil.logMsg("Did throw UnsupportedOperationException");
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

  private void addFaultReasonText2Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("addFaultReasonText2Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      String expected1 = "Its my fault";
      boolean found1 = false;

      TestUtil.logMsg("Adding FaultReasonText to SOAPFault");
      sf.addFaultReasonText(expected1, Locale.ENGLISH);

      TestUtil.logMsg("Getting FaultReasonTexts from SOAPFault");
      Iterator i = sf.getFaultReasonTexts();
      int j = 0;
      while (i.hasNext()) {
        Object o = i.next();
        if (o instanceof String) {
          String actual = (String) o;
          if (actual.equals(expected1)) {
            if (!found1) {
              found1 = true;
              TestUtil.logMsg("Reason= '" + actual + "'");
            } else {
              TestUtil
                  .logErr("Received a duplicate Reason text:'" + actual + "'");
              pass = false;
            }
          } else {
            TestUtil.logErr("Did not receive expected reason text:");
            TestUtil.logErr("expected= '" + expected1 + "'");
            TestUtil.logErr("actual= '" + actual + "'");
            pass = false;
          }
        } else {
          TestUtil.logErr(
              "An object that is not an instance of String was returned");
          TestUtil.logErr("The object is:" + o);
          pass = false;
        }
        j++;
      }
      if (j < 1) {
        TestUtil.logErr("No reason text was returned");
        pass = false;
      }
      if (j > 1) {
        TestUtil.logErr("More than one reason text was returned");
        pass = false;
      }
      if (!found1) {
        TestUtil.logErr(
            "The following Reason text was not received: '" + expected1 + "'");
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

  private void addFaultReasonText3Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("addFaultReasonText3Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      String expected1 = "Its my fault";
      String expected2 = "Its my fault again";
      boolean found1 = false;
      boolean found2 = false;
      TestUtil.logMsg("Adding FaultReasonText to SOAPFault");
      sf.addFaultReasonText(expected1, Locale.UK);
      TestUtil.logMsg("Adding another FaultReasonText to SOAPFault");
      sf.addFaultReasonText(expected2, Locale.ENGLISH);
      TestUtil.logMsg("Getting FaultReasonTexts from SOAPFault");
      Iterator i = sf.getFaultReasonTexts();
      int j = 0;
      while (i.hasNext()) {
        Object o = i.next();
        if (o instanceof String) {
          String actual = (String) o;
          if (actual.equals(expected1)) {
            if (!found1) {
              found1 = true;
              TestUtil.logMsg("Reason= '" + actual + "'");
            } else {
              TestUtil
                  .logErr("Received a duplicate Reason text:'" + actual + "'");
              pass = false;
            }
          } else if (actual.equals(expected2)) {
            if (!found2) {
              found2 = true;
              TestUtil.logMsg("Reason= '" + actual + "'");
            } else {
              TestUtil
                  .logErr("Received a duplicate Reason text:'" + actual + "'");
              pass = false;
            }

          } else {
            TestUtil.logErr("Did not receive expected reason text:");
            TestUtil
                .logErr("expected= '" + expected1 + "' or '" + expected2 + "'");
            TestUtil.logErr("actual= '" + actual + "'");
            pass = false;
          }
        } else {
          TestUtil.logErr(
              "An object that is not an instance of String was returned");
          TestUtil.logErr("The object is:" + o);
          pass = false;
        }
        j++;
      }
      if (j < 1) {
        TestUtil.logErr("No reason text was returned");
        pass = false;
      }
      if (j > 2) {
        TestUtil.logErr("More than two reason texts were returned");
        pass = false;
      }
      if (!found1) {
        TestUtil.logErr(
            "The following Reason text was not received: '" + expected1 + "'");
        pass = false;
      }
      if (!found2) {
        TestUtil.logErr(
            "The following Reason text was not received: '" + expected2 + "'");
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

  private void addFaultReasonText4Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("addFaultReasonText4Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      String expected = "Its my fault again";
      boolean found = false;
      TestUtil.logMsg("Adding FaultReasonText to SOAPFault");
      sf.addFaultReasonText("Its my fault", Locale.ENGLISH);
      TestUtil.logMsg("Adding another FaultReasonText to SOAPFault");
      sf.addFaultReasonText(expected, Locale.ENGLISH);
      TestUtil.logMsg("Getting FaultReasonTexts from SOAPFault");
      Iterator i = sf.getFaultReasonTexts();
      int j = 0;
      while (i.hasNext()) {
        Object o = i.next();
        if (o instanceof String) {
          String actual = (String) o;
          if (actual.equals(expected)) {
            if (!found) {
              found = true;
              TestUtil.logMsg("Reason= '" + actual + "'");
            } else {
              TestUtil
                  .logErr("Received a duplicate Reason text:'" + actual + "'");
              pass = false;
            }

          } else {
            TestUtil.logErr("Did not receive expected reason text:");
            TestUtil.logErr("expected= '" + expected + "'");
            TestUtil.logErr("actual= '" + actual + "'");
            pass = false;
          }
        } else {
          TestUtil.logErr(
              "An object that is not an instance of String was returned");
          TestUtil.logErr("The object is:" + o);
          pass = false;
        }
        j++;
      }
      if (j < 1) {
        TestUtil.logErr("No reason text was returned");
        pass = false;
      }
      if (j > 1) {
        TestUtil.logErr("More than one reason text was returned");
        pass = false;
      }
      if (!found) {
        TestUtil.logErr(
            "The following Reason text was not received: '" + expected + "'");
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

  private void getFaultReasonLocales1Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getFaultReasonLocales1Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();
      TestUtil.logMsg("Calling getFaultReasonLocales() must"
          + " throw UnsupportedOperationException");
      sf.getFaultReasonLocales();
      TestUtil.logErr("Did not throw UnsupportedOperationException");
      pass = false;
    } catch (UnsupportedOperationException e) {
      TestUtil.logMsg("Did throw UnsupportedOperationException");
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

  private void getFaultReasonLocales2Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getFaultReasonLocales2Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      Locale expected1 = Locale.ENGLISH;
      Locale expected2 = Locale.UK;
      Locale expected3 = Locale.GERMAN;
      boolean found1 = false;
      boolean found2 = false;
      boolean found3 = false;

      TestUtil.logMsg("Adding FaultReasonText to SOAPFault");
      sf.addFaultReasonText("Its my fault1", expected1);
      sf.addFaultReasonText("Its my fault2", expected2);
      sf.addFaultReasonText("Its my fault3", expected3);
      TestUtil.logMsg("Getting FaultReasonLocales from SOAPFault");
      Iterator i = sf.getFaultReasonLocales();
      TestUtil.logMsg("Locale iterator count=" + SOAP_Util.getIteratorCount(i));
      i = sf.getFaultReasonLocales();
      int j = 0;
      while (i.hasNext()) {
        Object o = i.next();
        if (o instanceof Locale) {
          Locale actual = (Locale) o;
          if (actual.equals(expected1)) {
            if (!found1) {
              found1 = true;
              TestUtil.logMsg("Locale= '" + actual + "'");
            } else {
              TestUtil.logErr("Received a duplicate Locale:'" + actual + "'");
              pass = false;
            }
          } else if (actual.equals(expected2)) {
            if (!found2) {
              found2 = true;
              TestUtil.logMsg("Locale '" + actual + "'");
            } else {
              TestUtil.logErr("Received a duplicate Locale:'" + actual + "'");
              pass = false;
            }
          } else if (actual.equals(expected3)) {
            if (!found3) {
              found3 = true;
              TestUtil.logMsg("Locale '" + actual + "'");
            } else {
              TestUtil.logErr("Received a duplicate Locale:'" + actual + "'");
              pass = false;
            }
          } else {
            TestUtil.logErr("Did not receive expected reason text:");
            TestUtil.logErr("expected= '" + expected1 + "' or '" + expected2
                + "' or '" + expected3 + "'");
            TestUtil.logErr("actual= '" + actual + "'");
            pass = false;
          }
        } else {
          TestUtil.logErr(
              "An object that is not an instance of Locale was returned");
          TestUtil.logErr("The object is:" + o);
          pass = false;
        }
        j++;
      }
      if (j < 1) {
        TestUtil.logErr("No reason text was returned");
        pass = false;
      }
      if (j > 3) {
        TestUtil.logErr("More than 3 Locales were returned");
        pass = false;
      }
      if (!found1) {
        TestUtil.logErr(
            "The following Locale was not received: '" + expected1 + "'");
        pass = false;
      }
      if (!found2) {
        TestUtil.logErr(
            "The following Locale was not received: '" + expected2 + "'");
        pass = false;
      }
      if (!found3) {
        TestUtil.logErr(
            "The following Locale was not received: '" + expected3 + "'");
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

  private void getFaultReasonText1Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getFaultReasonText1Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();
      TestUtil.logMsg("Calling getFaultReasonText(Locale) must"
          + " throw UnsupportedOperationException");

      String result = sf.getFaultReasonText(Locale.ENGLISH);
      TestUtil.logErr("Did not throw UnsupportedOperationException");
      pass = false;
    } catch (UnsupportedOperationException e) {
      TestUtil.logMsg("Did throw UnsupportedOperationException");
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

  private void getFaultReasonText2Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getFaultReasonText2Test");
    Properties resultProps = new Properties();
    boolean pass = true;
    String expected = "Its my fault1";

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Adding FaultReasonText to SOAPFault");
      sf.addFaultReasonText("Its my fault", Locale.ENGLISH);
      sf.addFaultReasonText("Its my fault2", Locale.ENGLISH);
      sf.addFaultReasonText(expected, Locale.UK);
      TestUtil.logMsg("Getting FaultReasonText from SOAPFault");
      String actual = sf.getFaultReasonText(Locale.UK);
      if (actual != null) {
        if (actual.equals(expected)) {
          TestUtil.logMsg("Reason = " + actual);
        } else {
          TestUtil.logErr("An incorrect result was returned:");
          TestUtil.logErr("expected :" + expected);
          TestUtil.logErr("actual :" + actual);
          pass = false;
        }
      } else {
        TestUtil.logErr("Null result was returned(unexpected)");
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

  private void getFaultReasonText3Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getFaultReasonText3Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();
    String expected = "Its my fault2";

    try {
      setup();

      TestUtil.logMsg("Adding a FaultReasonText to SOAPFault");
      sf.addFaultReasonText("Its my fault1", Locale.ENGLISH);
      TestUtil.logMsg("Adding another FaultReasonText to SOAPFault");
      sf.addFaultReasonText(expected, Locale.ENGLISH);
      TestUtil.logMsg("All FaultReasonTexts were added to SOAPFault");
      String actual = sf.getFaultReasonText(Locale.ENGLISH);
      if (actual != null) {
        if (actual.equals(expected)) {
          TestUtil.logMsg("Reason = " + actual);
        } else {
          TestUtil.logErr("An incorrect result was returned:");
          TestUtil.logErr("expected :" + expected);
          TestUtil.logErr("actual :" + actual);
          pass = false;
        }
      } else {
        TestUtil.logErr("A null was returned for the reason text");
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

  private void getFaultReasonTexts1Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getFaultReasonTexts1Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Calling getFaultReasonTexts() must"
          + " throw UnsupportedOperationException");
      sf.getFaultReasonTexts();
      TestUtil.logErr("Did not throw UnsupportedOperationException");
      pass = false;
    } catch (UnsupportedOperationException e) {
      TestUtil.logMsg("Did throw UnsupportedOperationException");
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

  private void getFaultReasonTexts2Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getFaultReasonTexts2Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();
      String expected = "Its my fault";
      TestUtil.logMsg("Adding FaultReasonText to SOAPFault");
      sf.addFaultReasonText(expected, Locale.ENGLISH);
      TestUtil.logMsg("Getting FaultReasonTexts from SOAPFault");
      Iterator i = sf.getFaultReasonTexts();
      int j = 0;
      while (i.hasNext()) {
        Object o = i.next();
        if (o instanceof String) {
          String actual = (String) o;
          if (actual.equals(expected)) {
            TestUtil.logMsg("Reason= '" + actual + "'");
          } else {
            TestUtil.logErr("Did not receive expected reason text:");
            TestUtil.logErr("expected= '" + expected + "'");
            TestUtil.logErr("actual= '" + actual + "'");
            pass = false;
          }
        } else {
          TestUtil.logErr(
              "An object that is not an instance of String was returned");
          TestUtil.logErr("The object is:" + o);
          pass = false;
        }
        j++;
      }
      if (j < 1) {
        TestUtil.logErr("No reason text was returned");
        pass = false;
      }
      if (j > 1) {
        TestUtil.logErr("More than one reason text was returned");
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

  private void getFaultReasonTexts3Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getFaultReasonTexts3Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      String expected1 = "Its my fault";
      String expected2 = "Its my fault again";
      TestUtil.logMsg("Adding FaultReasonText to SOAPFault");
      sf.addFaultReasonText(expected1, Locale.ENGLISH);
      TestUtil.logMsg("Adding another FaultReasonText to SOAPFault");
      sf.addFaultReasonText(expected2, Locale.UK);
      TestUtil.logMsg("Getting FaultReasonTexts from SOAPFault");
      Iterator i = sf.getFaultReasonTexts();
      int j = 0;
      while (i.hasNext()) {
        Object o = i.next();
        if (o instanceof String) {
          String actual = (String) o;
          if ((actual.equals(expected1)) || (actual.equals(expected2))) {
            TestUtil.logMsg("Reason= '" + actual + "'");
          } else {
            TestUtil.logErr("Did not receive expected reason text:");
            TestUtil
                .logErr("expected='" + expected1 + "' or '" + expected2 + "'");
            TestUtil.logErr("actual= '" + actual + "'");
            pass = false;
          }
        } else {
          TestUtil.logErr(
              "An object that is not an instance of String was returned");
          TestUtil.logErr("The object is:" + o);
          pass = false;
        }
        j++;
      }
      if (j < 1) {
        TestUtil.logErr("No reason text was returned");
        pass = false;
      }
      if (j > 2) {
        TestUtil.logErr("More than two reason text's were returned");
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

  private void setFaultNode1Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("setFaultNode1Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Calling setFaultNode() must"
          + " throw UnsupportedOperationException");
      sf.setFaultNode("http://faultnode.com");
      TestUtil.logErr("Did not throw UnsupportedOperationException");
      pass = false;
    } catch (UnsupportedOperationException e) {
      TestUtil.logMsg("Did throw UnsupportedOperationException");
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

  private void SetGetFaultNode1Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("SetGetFaultNode1Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      String expected = "http://faultnode.com";
      TestUtil.logMsg("Calling setFaultNode()");
      sf.setFaultNode("http://faultnode.com");
      String result = sf.getFaultNode();
      if (result != null) {
        if (!result.equals(expected)) {
          TestUtil.logErr("The wrong node was returned");
          TestUtil.logErr("expected = " + expected);
          pass = false;
        }
        TestUtil.logMsg("result = " + result);
      } else {
        TestUtil
            .logErr("getFaultNode returned a null when a node was configured");
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

  private void SetGetFaultNode2Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("SetGetFaultNode2Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      String expected1 = "http://faultnode.com";
      TestUtil.logMsg("Calling setFaultNode()");
      sf.setFaultNode(expected1);

      String expected2 = "http://www.faultnode.com";
      TestUtil.logMsg("Calling setFaultNode() a second time");
      sf.setFaultNode(expected2);

      String result = sf.getFaultNode();
      if (result != null) {
        if (!result.equals(expected2)) {
          TestUtil.logErr("The wrong node was returned");
          TestUtil.logErr("expected = " + expected2);
          pass = false;
        }
        TestUtil.logMsg("result = " + result);
      } else {
        TestUtil
            .logErr("getFaultNode returned a null when a node was configured");
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

  private void getFaultNode1Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getFaultNode1Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Calling getFaultNode() must"
          + " throw UnsupportedOperationException");
      sf.getFaultNode();
      TestUtil.logErr("Did not throw UnsupportedOperationException");
      pass = false;
    } catch (UnsupportedOperationException e) {
      TestUtil.logMsg("Did throw UnsupportedOperationException");
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

  private void getFaultNode2Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getFaultNode2Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Calling getFaultNode() to retrieve Fault Node");
      String result = sf.getFaultNode();
      if (result == null) {
        TestUtil.logMsg("getFaultNode returned null (expected)");
      } else {
        TestUtil.logErr("Calling getFaultNode returned a non null node");
        TestUtil.logErr("result = " + result);
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

  private void setFaultRole1Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("setFaultRole1Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Calling setFaultRole() must"
          + " throw UnsupportedOperationException");
      sf.setFaultRole("http://faultrole.com");
      TestUtil.logErr("Did not throw UnsupportedOperationException");
      pass = false;
    } catch (UnsupportedOperationException e) {
      TestUtil.logMsg("Did throw UnsupportedOperationException");
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

  private void SetGetFaultRole1Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("SetGetFaultRole1Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();
      String expected = "http://faultrole.com";
      TestUtil.logMsg("Calling setFaultRole()");
      sf.setFaultRole(expected);
      TestUtil.logMsg("Calling getFaultRole()");
      String result = sf.getFaultRole();
      if (result != null) {
        if (!result.equals(expected)) {
          TestUtil.logErr("setFaultRole()/getFaultRole() behaved incorrectly");
          TestUtil
              .logErr("setFaultRole=" + expected + ", getFaultRole=" + result);
          pass = false;
        } else
          TestUtil.logMsg("setFaultRole()/getFaultRole() behaved correctly");
      } else {
        TestUtil.logErr("getFaultRole() returned a null");
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

  private void getFaultRole1Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getFaultRole1Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Calling getFaultRole() must"
          + " throw UnsupportedOperationException");
      sf.getFaultRole();
      TestUtil.logErr("Did not throw UnsupportedOperationException");
      pass = false;
    } catch (UnsupportedOperationException e) {
      TestUtil.logMsg("Did throw UnsupportedOperationException");
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

  private void getFaultRole2Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getFaultRole2Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Calling getFaultRole() to retrieve Fault Role");
      String s = sf.getFaultRole();
      if (s == null) {
        TestUtil.logMsg("Calling getFaultRole returned null (expected)");
      } else {
        TestUtil.logErr("Calling getFaultRole returned a non null");
        TestUtil.logErr("getFaultRole result=" + s);
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

  private void SetGetFaultCodeAsName1Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("SetGetFaultCodeAsName1Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
        TestUtil.logMsg("Creating Name of: <myfault, flt, http://example.com>");
        Name name = sfactory.createName("myfault", "flt", "http://example.com");
        TestUtil
            .logMsg("Calling setFaultCode() with " + name.getQualifiedName());
        sf.setFaultCode(name);
        TestUtil.logMsg("Calling getFaultCodeAsName() to retrieve Fault Code");
        Name name2 = sf.getFaultCodeAsName();
        if (name2 != null)
          TestUtil.logMsg("Qualified name=" + name2.getQualifiedName());
        if (name2 == null) {
          TestUtil.logErr(
              "Calling getFaultCodeAsName " + "returned null (unexpected)");
          pass = false;
        } else if (!name2.getLocalName().equals(name.getLocalName())
            || !name2.getPrefix().equals(name.getPrefix())
            || !name2.getURI().equals(name.getURI())) {
          TestUtil.logErr(
              "Calling getFaultCodeAsName returned <" + name2.getLocalName()
                  + "," + name2.getPrefix() + "," + name2.getURI()
                  + ">, expected " + "<myfault,flt,http://example.com>");
          pass = false;
        }
      } else {
        QName qname = SOAPConstants.SOAP_SENDER_FAULT;
        TestUtil.logMsg("Creating Name of: " + qname);
        Name name = SOAP_Util.getSOAPFactory().createName(qname.getLocalPart(),
            qname.getPrefix(), qname.getNamespaceURI());
        TestUtil
            .logMsg("Calling setFaultCode() with " + name.getQualifiedName());
        sf.setFaultCode(name);
        TestUtil.logMsg("Calling getFaultCodeAsName() to retrieve Fault Code");
        Name name2 = sf.getFaultCodeAsName();
        if (name2 != null)
          TestUtil.logMsg("Qualified name=" + name2.getQualifiedName());
        if (name2 == null) {
          TestUtil.logErr(
              "Calling getFaultCodeAsName " + "returned null (unexpected)");
          pass = false;
        } else if (!name2.getLocalName().equals(name.getLocalName())
            || !name2.getPrefix().equals(name.getPrefix())
            || !name2.getURI().equals(name.getURI())) {
          TestUtil.logErr("Calling getFaultCodeAsName returned <"
              + name2.getLocalName() + "," + name2.getPrefix() + ","
              + name2.getURI() + ">, expected " + "<" + name.getLocalName()
              + "," + name.getPrefix() + "," + name.getURI() + ">");
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

  private void SetGetFaultCodeAsQName1Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("SetGetFaultCodeAsQName1Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
        TestUtil
            .logMsg("Creating QName of: <myfault, flt, http://example.com>");
        QName name = new QName("http://example.com", "myfault", "flt");
        TestUtil.logMsg("Calling setFaultCode() with " + name.toString());
        sf.setFaultCode(name);
        TestUtil.logMsg("Calling getFaultCodeAsQName() to retrieve Fault Code");
        QName name2 = sf.getFaultCodeAsQName();
        if (name2 != null)
          TestUtil.logMsg("Qualified name=" + name2.toString());
        if (name2 == null) {
          TestUtil.logErr(
              "Calling getFaultCodeAsQName " + "returned null (unexpected)");
          pass = false;
        } else if (!name2.getLocalPart().equals(name.getLocalPart())
            || !name2.getPrefix().equals(name.getPrefix())
            || !name2.getNamespaceURI().equals(name.getNamespaceURI())) {
          TestUtil.logErr(
              "Calling getFaultCodeAsQName returned <" + name2.getLocalPart()
                  + "," + name2.getPrefix() + "," + name2.getNamespaceURI()
                  + ">, expected " + "<myfault,flt,http://example.com>");
          pass = false;
        }
      } else {
        QName name = SOAPConstants.SOAP_SENDER_FAULT;
        TestUtil.logMsg("Creating QName of: " + name);
        TestUtil.logMsg("Calling setFaultCode() with " + name.toString());
        sf.setFaultCode(name);
        TestUtil.logMsg("Calling getFaultCodeAsQName() to retrieve Fault Code");
        QName name2 = sf.getFaultCodeAsQName();
        if (name2 != null)
          TestUtil.logMsg("Qualified name=" + name2.toString());
        if (name2 == null) {
          TestUtil.logErr(
              "Calling getFaultCodeAsQName " + "returned null (unexpected)");
          pass = false;
        } else if (!name2.getLocalPart().equals(name.getLocalPart())
            || !name2.getPrefix().equals(name.getPrefix())
            || !name2.getNamespaceURI().equals(name.getNamespaceURI())) {
          TestUtil.logErr(
              "Calling getFaultCodeAsQName returned <" + name2.getLocalPart()
                  + "," + name2.getPrefix() + "," + name2.getNamespaceURI()
                  + ">, expected " + "<" + name.getLocalPart() + ","
                  + name.getPrefix() + name.getNamespaceURI() + ">");
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

  private void SetGetFaultStringLocale1Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("SetGetFaultStringLocale1Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      Locale expected = Locale.ENGLISH;
      TestUtil.logMsg("Setting fault string to Locale " + expected);
      sf.setFaultString("this is the fault string", expected);

      TestUtil.logMsg("calling getFaultStringLocale()");
      Locale result = sf.getFaultStringLocale();
      if (result != null) {
        if (!result.equals(expected)) {
          TestUtil.logErr(
              "setFaultString(string,Locale)/getFaultStringLocale behaved incorrectly");
          TestUtil.logErr("expected=" + expected);
          pass = false;
        }
        TestUtil.logMsg("result=" + result);
      } else {
        TestUtil.logErr(
            "getFaultStringLocale() returned a null result, eventhough the fault string has a locale");
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

  private void setFaultStringLocale1Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("setFaultStringLocale2Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      String expected1 = "this is the fault string one";
      TestUtil.logMsg("Setting fault string one to Locale " + Locale.ENGLISH);
      sf.addFaultReasonText(expected1, Locale.ENGLISH);

      String expected2 = "this is the fault string two";
      TestUtil.logMsg("Setting fault string two to Locale " + Locale.ENGLISH);
      sf.setFaultString(expected2, Locale.ENGLISH);

      TestUtil.logMsg("Getting FaultReasonTexts from SOAPFault");
      Iterator i = sf.getFaultReasonTexts();
      boolean found = false;
      int j = 0;
      while (i.hasNext()) {
        Object o = i.next();
        if (o instanceof String) {
          String actual = (String) o;
          if (actual.equals(expected2)) {
            if (!found) {
              found = true;
              TestUtil.logMsg("Reason= '" + actual + "'");
            } else {
              TestUtil
                  .logErr("Received a duplicate Reason text:'" + actual + "'");
              pass = false;
            }
          } else {
            TestUtil.logErr("Did not receive expected reason text:");
            TestUtil.logErr("expected= '" + expected1 + "'");
            TestUtil.logErr("actual= '" + actual + "'");
            pass = false;
          }
        } else {
          TestUtil.logErr(
              "An object that is not an instance of String was returned");
          TestUtil.logErr("The object is:" + o);
          pass = false;
        }
        j++;
      }
      if (j < 1) {
        TestUtil.logErr("No reason text was returned");
        pass = false;
      }
      if (j > 1) {
        TestUtil.logErr("More than one reason text was returned");
        pass = false;
      }
      if (!found) {
        TestUtil.logErr(
            "The following Reason text was not received: '" + expected1 + "'");
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

  private void getFaultStringLocale1SOAP11Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getFaultStringLocale1SOAP12Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Setting fault string with no Locale");
      sf.setFaultString("this is the fault string");

      TestUtil.logMsg("calling getFaultStringLocale()");
      Locale result = sf.getFaultStringLocale();
      if (result == null) {
        TestUtil.logMsg("null Locale returned (expected)");
      } else {
        TestUtil.logErr("getFaultStringLocale() returned a non-null result");
        TestUtil.logErr("result=" + result);
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

  private void getFaultStringLocale1SOAP12Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getFaultStringLocale1SOAP12Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Setting fault string with no Locale");
      sf.setFaultString("this is the fault string");

      TestUtil.logMsg("calling getFaultStringLocale()");
      Locale result = sf.getFaultStringLocale();
      if (result == null) {
        TestUtil.logErr("null Locale returned (unexpected)");
        pass = false;
      } else {
        TestUtil.logMsg(
            "getFaultStringLocale() returned a non-null result (expected)");
        TestUtil.logMsg("result=" + result);
        if (!result.equals(Locale.getDefault())) {
          TestUtil
              .logErr("Got: " + result + ", Expected: " + Locale.getDefault());
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

  private void appendFaultSubcode1Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("appendFaultSubcode1Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Appending fault Subcode");
      QName expected1 = new QName("http://example.com", "myfault1", "flt1");
      sf.appendFaultSubcode(expected1);
      TestUtil.logErr("Did not throw UnsupportedOperationException");
      pass = false;
    } catch (UnsupportedOperationException e) {
      TestUtil.logMsg("Did throw UnsupportedOperationException");
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

  private void appendFaultSubcode2Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("appendFaultSubcode2Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {

      setup();

      QName expected1 = new QName("http://example.com", "myfault1", "flt1");
      QName expected2 = new QName("http://example.com", "myfault2", "flt2");
      boolean found1 = false;
      boolean found2 = false;

      TestUtil.logMsg("Appending fault Subcode");
      sf.appendFaultSubcode(expected1);
      TestUtil.logMsg("Appending a second fault Subcode");
      sf.appendFaultSubcode(expected2);

      TestUtil.logMsg("Getting FaultSubCodes from SOAPFault");
      Iterator i = sf.getFaultSubcodes();
      int j = 0;
      while (i.hasNext()) {
        Object o = i.next();
        if (o instanceof QName) {
          QName actual = (QName) o;
          if (actual.equals(expected1)) {
            if (!found1) {
              found1 = true;
              TestUtil.logMsg("Subcode= '" + actual + "'");
            } else {
              TestUtil.logErr("Received a duplicate Subcode :'" + actual + "'");
              pass = false;
            }
          } else if (actual.equals(expected2)) {
            if (!found2) {
              found2 = true;
              TestUtil.logMsg("Subcode= '" + actual + "'");
            } else {
              TestUtil.logErr("Received a duplicate Subcode :'" + actual + "'");
              pass = false;
            }

          } else {
            TestUtil.logErr("Did not receive expected Subcodes:");
            TestUtil
                .logErr("expected= '" + expected1 + "' or '" + expected2 + "'");
            TestUtil.logErr("actual= '" + actual + "'");
            pass = false;
          }
        } else {
          TestUtil.logErr(
              "An object that is not an instance of QName was returned");
          TestUtil.logErr("The object is:" + o);
          pass = false;
        }
        j++;
      }
      if (j < 1) {
        TestUtil.logErr("No Subcode was returned");
        pass = false;
      }
      if (j > 2) {
        TestUtil.logErr("More than two Subcodes were returned");
        pass = false;
      }
      if (!found1) {
        TestUtil.logErr(
            "The following Subcode was not received: '" + expected1 + "'");
        pass = false;
      }
      if (!found2) {
        TestUtil.logErr(
            "The following Subcode was not received: '" + expected2 + "'");
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

  private void getFaultSubcodes1Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getFaultSubcodes1Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Appending fault Subcode");
      sf.getFaultSubcodes();
      TestUtil.logErr("Did not throw UnsupportedOperationException");
      pass = false;
    } catch (UnsupportedOperationException e) {
      TestUtil.logMsg("Did throw UnsupportedOperationException");
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

  private void getFaultSubcodes2Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getFaultSubcodes2Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {

      setup();

      QName expected1 = new QName("http://example.com", "myfault1", "flt1");
      QName expected2 = new QName("http://example.com", "myfault2", "flt2");
      boolean found1 = false;
      boolean found2 = false;

      TestUtil.logMsg("Appending fault Subcode");
      sf.appendFaultSubcode(expected1);
      TestUtil.logMsg("Appending a second fault Subcode");
      sf.appendFaultSubcode(expected2);
      TestUtil
          .logMsg("Getting FaultSubCodes from SOAPFault for the first time");
      Iterator i = sf.getFaultSubcodes();
      int j = 0;
      while (i.hasNext()) {
        Object o = i.next();
        if (o instanceof QName) {
          QName actual = (QName) o;
          if (actual.equals(expected1)) {
            if (!found1) {
              found1 = true;
              TestUtil.logMsg("Subcode= '" + actual + "'");
            } else {
              TestUtil.logErr("Received a duplicate Subcode :'" + actual + "'");
              pass = false;
            }
          } else if (actual.equals(expected2)) {
            if (!found2) {
              found2 = true;
              TestUtil.logMsg("Subcode= '" + actual + "'");
            } else {
              TestUtil.logErr("Received a duplicate Subcode :'" + actual + "'");
              pass = false;
            }

          } else {
            TestUtil.logErr("Did not receive expected Subcodes:");
            TestUtil
                .logErr("expected= '" + expected1 + "' or '" + expected2 + "'");
            TestUtil.logErr("actual= '" + actual + "'");
            pass = false;
          }
        } else {
          TestUtil.logErr(
              "An object that is not an instance of QName was returned");
          TestUtil.logErr("The object is:" + o);
          pass = false;
        }
        j++;
      }
      if (j < 1) {
        TestUtil.logErr("No Subcode was returned");
        pass = false;
      }
      if (j > 2) {
        TestUtil.logErr("More than two Subcodes were returned");
        pass = false;
      }
      if (!found1) {
        TestUtil.logErr(
            "The following Subcode was not received: '" + expected1 + "'");
        pass = false;
      }
      if (!found2) {
        TestUtil.logErr(
            "The following Subcode was not received: '" + expected2 + "'");
        pass = false;
      }

      TestUtil
          .logMsg("Getting FaultSubCodes from SOAPFault for the second time");
      i = sf.getFaultSubcodes();
      j = 0;
      found1 = false;
      found2 = false;
      while (i.hasNext()) {
        Object o = i.next();
        if (o instanceof QName) {
          QName actual = (QName) o;
          if (actual.equals(expected1)) {
            if (!found1) {
              found1 = true;
              TestUtil.logMsg("Subcode= '" + actual + "'");
            } else {
              TestUtil.logErr("Received a duplicate Subcode :'" + actual + "'");
              pass = false;
            }
          } else if (actual.equals(expected2)) {
            if (!found2) {
              found2 = true;
              TestUtil.logMsg("Subcode= '" + actual + "'");
            } else {
              TestUtil.logErr("Received a duplicate Subcode :'" + actual + "'");
              pass = false;
            }

          } else {
            TestUtil.logErr("Did not receive expected Subcodes:");
            TestUtil
                .logErr("expected= '" + expected1 + "' or '" + expected2 + "'");
            TestUtil.logErr("actual= '" + actual + "'");
            pass = false;
          }
        } else {
          TestUtil.logErr(
              "An object that is not an instance of QName was returned");
          TestUtil.logErr("The object is:" + o);
          pass = false;
        }
        j++;
      }
      if (j < 1) {
        TestUtil.logErr("No Subcode was returned");
        pass = false;
      }
      if (j > 2) {
        TestUtil.logErr("More than two Subcodes were returned");
        pass = false;
      }
      if (!found1) {
        TestUtil.logErr(
            "The following Subcode was not received: '" + expected1 + "'");
        pass = false;
      }
      if (!found2) {
        TestUtil.logErr(
            "The following Subcode was not received: '" + expected2 + "'");
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

  private void hasDetail1Test(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("hasDetail1Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      boolean result = sf.hasDetail();
      if (result == false) {
        TestUtil.logMsg("hasDetail() returned false");
      } else {
        TestUtil.logErr("hasDetail() returned true when no detail existed");
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

  private void hasDetail2Test(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("hasDetail2Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      Detail d = null;
      d = sf.addDetail();
      if (d == null) {
        TestUtil.logErr("addDetail() returned null");
        pass = false;
      } else {
        TestUtil.logMsg("addDetail() called successfully");
        boolean result = sf.hasDetail();
        if (result == false) {
          TestUtil.logErr("hasDetail() returned false when a detail did exist");
          pass = false;
        } else {
          TestUtil.logMsg("hasDetail() returned true");
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

  private void removeAllFaultSubcodes1Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("removeAllFaultSubcodes1Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      sf.removeAllFaultSubcodes();
      TestUtil.logErr("Did not throw UnsupportedOperationException");
      pass = false;
    } catch (UnsupportedOperationException e) {
      TestUtil.logMsg("Did throw UnsupportedOperationException");
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

  private void removeAllFaultSubcodes2Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("removeAllFaultSubcodes2Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {

      setup();
      QName expected1 = new QName("http://example.com", "myfault1", "flt1");
      QName expected2 = new QName("http://example.com", "myfault2", "flt2");

      TestUtil.logMsg("Appending fault Subcode");
      sf.appendFaultSubcode(expected1);
      TestUtil.logMsg("Appending a second fault Subcode");
      sf.appendFaultSubcode(expected2);

      TestUtil.logMsg("Removing all  FaultSubCodes from SOAPFault");
      sf.removeAllFaultSubcodes();

      TestUtil.logMsg("Getting FaultSubCodes from SOAPFault");
      Iterator i = sf.getFaultSubcodes();
      if (i.hasNext() == false) {
        TestUtil.logMsg("All FaultSubcodes were removed");
      } else {
        TestUtil.logMsg("Not all FaultSubcodes were removed:");
        while (i.hasNext()) {
          Object o = i.next();
          if (o instanceof QName) {
            QName actual = (QName) o;
            TestUtil.logErr("Received unexpected Subcodes:");
            TestUtil.logErr("actual= '" + actual + "'");
            pass = false;
          } else {
            TestUtil.logErr(
                "An object that is not an instance of QName was returned");
            TestUtil.logErr("The object is:" + o);
            pass = false;
          }
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

  private void SetFaultCodeNameSOAPExceptionTest(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("SetFaultCodeNameSOAPExceptionTest");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();
      if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
        try {
          TestUtil.logMsg("Calling setFaultCode() with unqualified Name object"
              + " may succeed for SOAP1.1 protocol");
          Name name = sfactory.createName("myfault");
          sf.setFaultCode(name);
        } catch (SOAPException e) {
        }
      } else {
        TestUtil.logMsg("Calling setFaultCode() with unqualified Name object"
            + " must throw SOAPException for SOAP1.2 protocol");
        Name name = sfactory.createName("myfault");
        sf.setFaultCode(name);
        TestUtil.logErr("Did not throw SOAPException");
        pass = false;
      }
    } catch (SOAPException e) {
      TestUtil.logMsg("Did throw SOAPException");
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

  private void SetFaultCodeQNameSOAPExceptionTest(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("SetFaultCodeQNameSOAPExceptionTest");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();
      if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
        try {
          TestUtil.logMsg("Calling setFaultCode() with unqualified QName object"
              + " may succeed for SOAP1.1 protocol");
          sf.setFaultCode(new QName("myfault"));
        } catch (SOAPException e) {
        }
      } else {
        TestUtil.logMsg("Calling setFaultCode() with unqualified QName object"
            + " must throw SOAPException for SOAP1.2 protocol");
        sf.setFaultCode(new QName("myfault"));
        TestUtil.logErr("Did not throw SOAPException");
        pass = false;
      }
    } catch (SOAPException e) {
      TestUtil.logMsg("Did throw SOAPException");
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

  private void SetFaultCodeStringSOAPExceptionTest(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("SetFaultCodeStringSOAPExceptionTest");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();
      if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
        try {
          TestUtil
              .logMsg("Calling setFaultCode() with unqualified String object"
                  + " may succeed for SOAP1.1 protocol");
          sf.setFaultCode("Server");
        } catch (SOAPException e) {
        }
      } else {
        TestUtil.logMsg("Calling setFaultCode() with unqualified String object"
            + " must throw SOAPException for SOAP1.2 protocol");
        sf.setFaultCode("Server");
        TestUtil.logErr("Did not throw SOAPException");
        pass = false;
      }
    } catch (SOAPException e) {
      TestUtil.logMsg("Did throw SOAPException");
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

  private void AppendFaultSubcodeSOAPExceptionTest(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("AppendFaultSubcodeSOAPExceptionTest");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();
      TestUtil
          .logMsg("Calling appendFaultSubcode() with unqualified QName object"
              + " must throw SOAPException");
      QName name = new QName("mysubcode");
      sf.appendFaultSubcode(name);
      TestUtil.logErr("Did not throw SOAPException");
      pass = false;
    } catch (SOAPException e) {
      TestUtil.logMsg("Did throw SOAPException");
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

  private void AddDetailSOAPExceptionTest(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("AddDetailSOAPExceptionTest");
    Properties resultProps = new Properties();
    boolean pass = false;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();
      Detail d1 = null;
      d1 = sf.addDetail();
      if (d1 == null) {
        TestUtil.logErr("addDetail() returned null");
        pass = false;
      } else {
        TestUtil.logMsg("addDetail() returned a non-null detail");
      }

      try {
        sf.addDetail();
        TestUtil.logErr("A SOAPException should have been thrown");
        pass = false;
      } catch (SOAPException se) {
        pass = true;
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
