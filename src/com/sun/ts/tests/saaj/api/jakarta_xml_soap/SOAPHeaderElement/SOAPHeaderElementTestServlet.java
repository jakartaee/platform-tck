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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPHeaderElement;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.saaj.common.SOAP_Util;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.soap.SOAPHeaderElement;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;

public class SOAPHeaderElementTestServlet extends HttpServlet {
  private MessageFactory mf = null;

  private SOAPMessage msg = null;

  private SOAPPart sp = null;

  private SOAPEnvelope envelope = null;

  private SOAPHeader hdr = null;

  private SOAPHeaderElement she = null;

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

    TestUtil.logMsg("Creating SOAPHeaderElement");
    she = hdr.addHeaderElement(envelope.createName("foo", "f", "foo-URI"));

  }

  private void dispatch(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("dispatch");
    String testname = SOAP_Util.getHarnessProps().getProperty("TESTNAME");
    if (testname.equals("setRelaySOAP11Test")) {
      TestUtil.logMsg("Starting setRelaySOAP11Test");
      setRelaySOAP11Test(req, res);
    } else if (testname.equals("setRelaySOAP12Test")) {
      TestUtil.logMsg("Starting setRelaySOAP12Test");
      setRelaySOAP12Test(req, res);
    } else if (testname.equals("getRelaySOAP11Test")) {
      TestUtil.logMsg("Starting getRelaySOAP11Test");
      getRelaySOAP11Test(req, res);
    } else if (testname.equals("getRelaySOAP12Test")) {
      TestUtil.logMsg("Starting getRelaySOAP12Test");
      getRelaySOAP12Test(req, res);
    } else if (testname.equals("getActorTest")) {
      TestUtil.logMsg("Starting getActorTest");
      getActorTest(req, res);
    } else if (testname.equals("getRoleSOAP11Test")) {
      TestUtil.logMsg("Starting getRoleSOAP11Test");
      getRoleSOAP11Test(req, res);
    } else if (testname.equals("getRoleSOAP12Test")) {
      TestUtil.logMsg("Starting getRoleSOAP12Test");
      getRoleSOAP12Test(req, res);
    } else if (testname.equals("getMustUnderstandTrueTest")) {
      TestUtil.logMsg("Starting getMustUnderstandTrueTest");
      getMustUnderstandTrueTest(req, res);
    } else if (testname.equals("getMustUnderstandFalseTest")) {
      TestUtil.logMsg("Starting getMustUnderstandFalseTest");
      getMustUnderstandFalseTest(req, res);
    } else if (testname.equals("setActorTest")) {
      TestUtil.logMsg("Starting setActorTest");
      setActorTest(req, res);
    } else if (testname.equals("setRoleSOAP11Test")) {
      TestUtil.logMsg("Starting setRoleSOAP11Test");
      setRoleSOAP11Test(req, res);
    } else if (testname.equals("setRoleSOAP12Test")) {
      TestUtil.logMsg("Starting setRoleSOAP12Test");
      setRoleSOAP12Test(req, res);
    } else if (testname.equals("setMustUnderstandTrueTest")) {
      TestUtil.logMsg("Starting setMustUnderstandTrueTest");
      setMustUnderstandTrueTest(req, res);
    } else if (testname.equals("setMustUnderstandFalseTest")) {
      TestUtil.logMsg("Starting setMustUnderstandFalseTest");
      setMustUnderstandFalseTest(req, res);
    } else {
      throw new ServletException(
          "The testname '" + testname + "' was not found in the test servlet");
    }
  }

  public void init(ServletConfig servletConfig) throws ServletException {
    super.init(servletConfig);
    System.out.println("SOAPHeaderElementTestServlet:init (Entering)");
    System.out.println("SOAPHeaderElementTestServlet:init (Leaving)");
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

  private void setRelaySOAP11Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("setRelaySOAP11Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg(
          "Calling setRelay() should throw UnsupportedOperationException");
      she.setRelay(true);
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

  private void setRelaySOAP12Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("setRelaySOAP12Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Set relay attribute for this SOAPHeaderElement to true");
      she.setRelay(true);

      TestUtil
          .logMsg("Set relay attribute for this SOAPHeaderElement to false");
      she.setRelay(false);
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

  private void getRelaySOAP11Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getRelaySOAP11Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg(
          "Calling getRelay() should throw UnsupportedOperationException");
      boolean relay = she.getRelay();
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

  private void getRelaySOAP12Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getRelaySOAP12Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Set relay attribute for this SOAPHeaderElement to true");
      she.setRelay(true);

      TestUtil.logMsg("Get relay attribute for this SOAPHeaderElement");
      boolean relay = she.getRelay();
      TestUtil.logMsg("Verify that relay attribute is true");
      if (relay) {
        TestUtil.logMsg("SOAPHeaderElement relay attribute is true (expected)");
      } else {
        TestUtil
            .logErr("SOAPHeaderElement relay attribute is false (unexpected)");
        pass = false;
      }

      TestUtil
          .logMsg("Set relay attribute for this SOAPHeaderElement to false");
      she.setRelay(false);

      TestUtil.logMsg("Get relay attribute for this SOAPHeaderElement");
      relay = she.getRelay();
      TestUtil.logMsg("Verify that relay attribute is false");
      if (!relay) {
        TestUtil
            .logMsg("SOAPHeaderElement relay attribute is false (expected)");
      } else {
        TestUtil
            .logErr("SOAPHeaderElement relay attribute is true (unexpected)");
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

  private void getActorTest(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("getActorTest");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Set the actor associated with SOAPHeaderElement");
      she.setActor("actor-URI");

      TestUtil.logMsg("Validating actor associated with SOAPHeaderElement");
      String actor = she.getActor();
      if (actor.equals("actor-URI")) {
        TestUtil.logMsg("SOAPHeaderElement actor setting is actor-URI");
      } else {
        TestUtil.logErr("SOAPHeaderElement actor setting: expected "
            + "actor-URI" + ", received " + actor);
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

  private void getRoleSOAP11Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getRoleSOAP11Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg(
          "Calling getRole() should throw UnsupportedOperationException");
      String role = she.getRole();
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

  private void getRoleSOAP12Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getRoleSOAP12Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Set the role associated with SOAPHeaderElement");
      she.setRole("role-URI");

      TestUtil.logMsg("Validating role associated with SOAPHeaderElement");
      String role = she.getRole();
      if (role.equals("role-URI")) {
        TestUtil.logMsg("SOAPHeaderElement role setting is role-URI");
      } else {
        TestUtil.logErr("SOAPHeaderElement role setting: expected " + "role-URI"
            + ", received " + role);
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

  private void getMustUnderstandTrueTest(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getMustUnderstandTrueTest");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil
          .logMsg("Set the SOAPHeaderElement mustunderstand attribute to true");
      she.setMustUnderstand(true);

      TestUtil.logMsg(
          "Validating SOAPHeaderElement mustunderstand attribute setting to true ...");
      if (she.getMustUnderstand()) {
        TestUtil.logMsg(
            "SOAPHeaderElement mustunderstand attribute setting is true");
      } else {
        TestUtil.logErr("SOAPHeaderElement mustunderstand attribute is false");
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

  private void getMustUnderstandFalseTest(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getMustUnderstandFalseTest");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg(
          "Set the SOAPHeaderElement mustunderstand attribute to false");
      she.setMustUnderstand(false);

      TestUtil.logMsg(
          "Validating SOAPHeaderElement mustunderstand attribute setting to false ...");
      if (!she.getMustUnderstand()) {
        TestUtil.logMsg(
            "SOAPHeaderElement mustunderstand attribute setting is false");
      } else {
        TestUtil.logErr("SOAPHeaderElement mustunderstand attribute is true");
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

  private void setActorTest(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("setActorTest");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Set the actor associated with SOAPHeaderElement");
      she.setActor("actor-URI");

      TestUtil.logMsg("Validating actor associated with SOAPHeaderElement");
      String actor = she.getActor();
      if (actor.equals("actor-URI")) {
        TestUtil.logMsg("SOAPHeaderElement actor setting is actor-URI");
      } else {
        TestUtil.logErr("SOAPHeaderElement actor setting: expected "
            + "actor-URI" + ", received " + actor);
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

  private void setRoleSOAP11Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("setRoleSOAP11Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg(
          "Calling setRole() should throw UnsupportedOperationException");
      she.setRole("role-URI");
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

  private void setRoleSOAP12Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("setRoleSOAP12Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Set the role associated with SOAPHeaderElement");
      she.setRole("role-URI");

      TestUtil.logMsg("Validating role associated with SOAPHeaderElement");
      String role = she.getRole();
      if (role.equals("role-URI")) {
        TestUtil.logMsg("SOAPHeaderElement role setting is role-URI");
      } else {
        TestUtil.logErr("SOAPHeaderElement role setting: expected " + "role-URI"
            + ", received " + role);
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

  private void setMustUnderstandTrueTest(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("setMustUnderstandTrueTest");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil
          .logMsg("Set the SOAPHeaderElement mustunderstand attribute to true");
      she.setMustUnderstand(true);

      TestUtil.logMsg(
          "Validating SOAPHeaderElement mustunderstand attribute setting to true ...");
      if (she.getMustUnderstand()) {
        TestUtil.logMsg(
            "SOAPHeaderElement mustunderstand attribute setting is true");
      } else {
        TestUtil.logErr("SOAPHeaderElement mustunderstand attribute is false");
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

  private void setMustUnderstandFalseTest(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("setMustUnderstandFalseTest");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg(
          "Set the SOAPHeaderElement mustunderstand attribute to false");
      she.setMustUnderstand(false);

      TestUtil.logMsg(
          "Validating SOAPHeaderElement mustunderstand attribute setting to false ...");
      if (!she.getMustUnderstand()) {
        TestUtil.logMsg(
            "SOAPHeaderElement mustunderstand attribute setting is false");
      } else {
        TestUtil.logErr("SOAPHeaderElement mustunderstand attribute is true");
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
}
