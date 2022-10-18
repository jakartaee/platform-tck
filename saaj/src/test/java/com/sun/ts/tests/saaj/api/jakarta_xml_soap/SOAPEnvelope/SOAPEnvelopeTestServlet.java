/*
 * Copyright (c) 2007, 2022 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPEnvelope;

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
import jakarta.xml.soap.Name;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPBodyElement;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.soap.SOAPHeaderElement;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;

public class SOAPEnvelopeTestServlet extends HttpServlet {
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

    // Retrieve the soap header from the envelope.
    TestUtil.logMsg("Get SOAP Body");
    body = envelope.getBody();
  }

  private void dispatch(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("dispatch");
    String testname = SOAP_Util.getHarnessProps().getProperty("TESTNAME");
    if (testname.equals("addBodyTest")) {
      TestUtil.logMsg("Starting addBodyTest");
      addBodyTest(req, res);
    } else if (testname.equals("getBodyTest")) {
      TestUtil.logMsg("Starting getBodyTest");
      getBodyTest(req, res);
    } else if (testname.equals("addHeaderTest")) {
      TestUtil.logMsg("Starting addHeaderTest");
      addHeaderTest(req, res);
    } else if (testname.equals("getHeaderTest")) {
      TestUtil.logMsg("Starting getHeaderTest");
      getHeaderTest(req, res);
    } else if (testname.equals("createNameTest1")) {
      TestUtil.logMsg("Starting createNameTest1");
      createNameTest1(req, res);
    } else if (testname.equals("createNameTest2")) {
      TestUtil.logMsg("Starting createNameTest2");
      createNameTest2(req, res);
    } else if (testname.equals("createNameTest3")) {
      TestUtil.logMsg("Starting createNameTest3");
      createNameTest3(req, res);
    } else {
      throw new ServletException(
          "The testname '" + testname + "' was not found in the test servlet");
    }
  }

  public void init(ServletConfig servletConfig) throws ServletException {
    super.init(servletConfig);
    System.out.println("SOAPEnvelopeTestServlet:init (Entering)");
    SOAP_Util.doServletInit(servletConfig);
    System.out.println("SOAPEnvelopeTestServlet:init (Leaving)");
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

  private void addBodyTest(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("addBodyTest");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();
      try {
        TestUtil.logMsg("Add SOAPBody to envelope that already"
            + " has a SOAPBody (expect SOAPException)");
        SOAPBody mybdy = envelope.addBody();
        TestUtil.logErr("Did not get expected SOAPException");
        pass = false;
      } catch (SOAPException e) {
        TestUtil.logMsg("Got expected SOAPException");
      }
      TestUtil.logMsg("Detach SOAPBody from this envelope");
      envelope.getBody().detachNode();
      TestUtil.logMsg("Add a SOAPBody to this envelope");
      SOAPBody mybdy = envelope.addBody();
      if (mybdy == null) {
        TestUtil.logErr("SOAPBody return value is null");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (pass)
      TestUtil.logMsg("addBodyTest() test PASSED");
    else
      TestUtil.logErr("addBodyTest() test FAILED");
    // Send response object and test result back to client
    if (pass)
      resultProps.setProperty("TESTRESULT", "pass");
    else
      resultProps.setProperty("TESTRESULT", "fail");
    resultProps.list(out);
  }

  private void getBodyTest(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("getBodyTest");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      SOAPBody mybdy = null;
      SOAPBody mybdy2 = null;

      setup();
      TestUtil.logMsg("Detach SOAPBody from this envelope");
      envelope.getBody().detachNode();
      TestUtil.logMsg("Get SOAPBody from envelope that has no"
          + " SOAPBody (expect return of null)");
      mybdy = envelope.getBody();
      if (mybdy != null) {
        TestUtil.logErr("Did not get return of null");
        pass = false;
      }
      TestUtil.logMsg("Add a SOAPBody to this envelope");
      mybdy = envelope.addBody();
      TestUtil.logMsg("Get the SOAPBody of this envelope");
      mybdy2 = envelope.getBody();
      if (mybdy2 == null) {
        TestUtil.logErr("SOAPBody return value is null");
        pass = false;
      } else if (!mybdy2.equals(mybdy)) {
        TestUtil.logErr("SOAPBody mybdy2 not equal to SOAPBody mybdy");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (pass)
      TestUtil.logMsg("getBodyTest() test PASSED");
    else
      TestUtil.logErr("getBodyTest() test FAILED");
    // Send response object and test result back to client
    if (pass)
      resultProps.setProperty("TESTRESULT", "pass");
    else
      resultProps.setProperty("TESTRESULT", "fail");
    resultProps.list(out);
  }

  private void addHeaderTest(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("addHeaderTest");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();
      try {
        TestUtil.logMsg("Add SOAPHeader to envelope that already"
            + " has a SOAPHeader (expect SOAPException)");
        SOAPHeader myhdr = envelope.addHeader();
        TestUtil.logErr("Did not get expected SOAPException");
        pass = false;
      } catch (SOAPException e) {
        TestUtil.logMsg("Got expected SOAPException");
      }
      TestUtil.logMsg("Detach SOAPHeader from this envelope");
      envelope.getHeader().detachNode();
      TestUtil.logMsg("Add a SOAPHeader to this envelope");
      SOAPHeader myhdr = envelope.addHeader();
      if (myhdr == null) {
        TestUtil.logErr("SOAPHeader return value is null");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (pass)
      TestUtil.logMsg("addHeaderTest() test PASSED");
    else
      TestUtil.logErr("addHeaderTest() test FAILED");
    // Send response object and test result back to client
    if (pass)
      resultProps.setProperty("TESTRESULT", "pass");
    else
      resultProps.setProperty("TESTRESULT", "fail");
    resultProps.list(out);
  }

  private void getHeaderTest(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("getHeaderTest");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      SOAPHeader myhdr = null;
      SOAPHeader myhdr2 = null;

      setup();
      TestUtil.logMsg("Detach SOAPHeader from this envelope");
      envelope.getHeader().detachNode();
      TestUtil.logMsg("Get SOAPHeader from envelope that has no"
          + " SOAPHeader (expect return of null)");
      myhdr = envelope.getHeader();
      if (myhdr != null) {
        TestUtil.logErr("Did not get expected return of null");
        pass = false;
      }
      TestUtil.logMsg("Add a SOAPHeader to this envelope");
      myhdr = envelope.addHeader();
      TestUtil.logMsg("Get the SOAPHeader of this envelope");
      myhdr2 = envelope.getHeader();
      if (myhdr2 == null) {
        TestUtil.logErr("SOAPHeader return value is null");
        pass = false;
      } else if (!myhdr2.equals(myhdr)) {
        TestUtil.logErr("SOAPHeader myhdr2 not equal to SOAPHeader myhdr");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (pass)
      TestUtil.logMsg("getHeaderTest() test PASSED");
    else
      TestUtil.logErr("getHeaderTest() test FAILED");
    // Send response object and test result back to client
    if (pass)
      resultProps.setProperty("TESTRESULT", "pass");
    else
      resultProps.setProperty("TESTRESULT", "fail");
    resultProps.list(out);
  }

  private void createNameTest1(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("createNameTest1");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();
      TestUtil.logMsg("Create name element localName=MyName1");
      Name name = envelope.createName("MyName1");
      if (name == null) {
        TestUtil.logErr("createName() returned null");
        pass = false;
      } else {
        String localName = name.getLocalName();
        String prefix = name.getPrefix();
        String uri = name.getURI();
        TestUtil.logMsg("localName=" + localName);
        TestUtil.logMsg("prefix=" + prefix);
        TestUtil.logMsg("uri=" + uri);
        if (localName == null) {
          TestUtil.logErr("localName is null (expected MyName1)");
          pass = false;
        } else if (!localName.equals("MyName1")) {
          TestUtil.logErr("localName is wrong (expected MyName1)");
          pass = false;
        } else if (prefix != null && !prefix.equals("")) {
          TestUtil.logErr("prefix is wrong (expected null or null string)");
          pass = false;
        } else if (uri != null && !uri.equals("")) {
          TestUtil.logErr("uri is wrong (expected null or null string)");
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (pass)
      TestUtil.logMsg("createNameTest1() test PASSED");
    else
      TestUtil.logErr("createNameTest1() test FAILED");
    // Send response object and test result back to client
    if (pass)
      resultProps.setProperty("TESTRESULT", "pass");
    else
      resultProps.setProperty("TESTRESULT", "fail");
    resultProps.list(out);
  }

  private void createNameTest2(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("createNameTest2");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();
      TestUtil.logMsg("Create name element localName=MyName1, "
          + "prefix=MyPrefix1, uri=MyUri1");
      Name name = envelope.createName("MyName1", "MyPrefix1", "MyUri1");
      if (name == null) {
        TestUtil.logErr("createName() returned null");
        pass = false;
      } else {
        String localName = name.getLocalName();
        String prefix = name.getPrefix();
        String uri = name.getURI();
        TestUtil.logMsg("localName=" + localName);
        TestUtil.logMsg("prefix=" + prefix);
        TestUtil.logMsg("uri=" + uri);
        if (localName == null) {
          TestUtil.logErr("localName is null (expected MyName1)");
          pass = false;
        } else if (!localName.equals("MyName1")) {
          TestUtil.logErr("localName is wrong (expected MyName1)");
          pass = false;
        } else if (prefix == null) {
          TestUtil.logErr("prefix is null (expected MyPrefix1)");
          pass = false;
        } else if (!prefix.equals("MyPrefix1")) {
          TestUtil.logErr("prefix is wrong (expected MyPrefix1)");
          pass = false;
        } else if (uri == null) {
          TestUtil.logErr("uri is null (expected MyUri1)");
          pass = false;
        } else if (!uri.equals("MyUri1")) {
          TestUtil.logErr("uri is wrong (expected MyUri1)");
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (pass)
      TestUtil.logMsg("createNameTest2() test PASSED");
    else
      TestUtil.logErr("createNameTest2() test FAILED");
    // Send response object and test result back to client
    if (pass)
      resultProps.setProperty("TESTRESULT", "pass");
    else
      resultProps.setProperty("TESTRESULT", "fail");
    resultProps.list(out);
  }

  private void createNameTest3(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("createNameTest3");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();
      TestUtil.logMsg("Create name element localName=MyName1, "
          + "uri=MyUri1");
      Name name = envelope.createName("MyName1", "MyUri1");
      if (name == null) {
        TestUtil.logErr("createName() returned null");
        pass = false;
      } else {
        String localName = name.getLocalName();
        String uri = name.getURI();
        TestUtil.logMsg("localName=" + localName);
        TestUtil.logMsg("uri=" + uri);
        if (localName == null) {
          TestUtil.logErr("localName is null (expected MyName1)");
          pass = false;
        } else if (!localName.equals("MyName1")) {
          TestUtil.logErr("localName is wrong (expected MyName1)");
          pass = false;
        } else if (uri == null) {
          TestUtil.logErr("uri is null (expected MyUri1)");
          pass = false;
        } else if (!uri.equals("MyUri1")) {
          TestUtil.logErr("uri is wrong (expected MyUri1)");
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (pass)
      TestUtil.logMsg("createNameTest3() test PASSED");
    else
      TestUtil.logErr("createNameTest3() test FAILED");
    // Send response object and test result back to client
    if (pass)
      resultProps.setProperty("TESTRESULT", "pass");
    else
      resultProps.setProperty("TESTRESULT", "fail");
    resultProps.list(out);
  }

}
