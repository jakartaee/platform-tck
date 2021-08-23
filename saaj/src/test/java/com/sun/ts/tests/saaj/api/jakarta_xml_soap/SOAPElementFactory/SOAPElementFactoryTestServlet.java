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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPElementFactory;

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
import jakarta.xml.soap.SOAPElementFactory;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.soap.SOAPHeaderElement;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;

public class SOAPElementFactoryTestServlet extends HttpServlet {
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
    if (testname.equals("newInstanceTest")) {
      TestUtil.logMsg("Starting newInstanceTest");
      newInstanceTest(req, res);
    } else if (testname.equals("createTest1")) {
      TestUtil.logMsg("Starting createTest1");
      createTest1(req, res);
    } else if (testname.equals("createTest2")) {
      TestUtil.logMsg("Starting createTest2");
      createTest2(req, res);
    } else if (testname.equals("createTest3")) {
      TestUtil.logMsg("Starting createTest3");
      createTest3(req, res);
    } else {
      throw new ServletException(
          "The testname '" + testname + "' was not found in the test servlet");
    }
  }

  public void init(ServletConfig servletConfig) throws ServletException {
    super.init(servletConfig);
    System.out.println("SOAPElementFactoryTestServlet:init (Entering)");
    SOAP_Util.doServletInit(servletConfig);
    System.out.println("SOAPElementFactoryTestServlet:init (Leaving)");
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

  private void newInstanceTest(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("newInstanceTest");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();
      TestUtil.logMsg("Create SOAPElementFactory object");
      SOAPElementFactory sef = SOAPElementFactory.newInstance();
      if (sef == null) {
        TestUtil.logErr("SOAPElementFactory.newInstance() returned null");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (pass)
      TestUtil.logMsg("newInstanceTest() test PASSED");
    else
      TestUtil.logErr("newInstanceTest() test FAILED");
    // Send response object and test result back to client
    if (pass)
      resultProps.setProperty("TESTRESULT", "pass");
    else
      resultProps.setProperty("TESTRESULT", "fail");
    resultProps.list(out);
  }

  private void createTest1(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("createTest1");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();
      SOAPElementFactory sef = SOAPElementFactory.newInstance();
      if (sef == null) {
        TestUtil
            .logErr("createTest1() could not create SOAPElementFactory object");
        pass = false;
      }
      TestUtil.logMsg("Create Name object with localName=MyName1, "
          + "prefix=MyPrefix1, uri=MyUri1");
      Name name = envelope.createName("MyName1", "MyPrefix1", "MyUri1");
      TestUtil.logMsg("Create SOAPElement object with above Name object");
      SOAPElement se = sef.create(name);
      if (se == null) {
        TestUtil.logErr("createTest1() could not create SOAPElement object");
        pass = false;
      } else {
        name = se.getElementName();
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
      TestUtil.logMsg("createTest1() test PASSED");
    else
      TestUtil.logErr("createTest1() test FAILED");
    // Send response object and test result back to client
    if (pass)
      resultProps.setProperty("TESTRESULT", "pass");
    else
      resultProps.setProperty("TESTRESULT", "fail");
    resultProps.list(out);
  }

  private void createTest2(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("createTest2");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();
      SOAPElementFactory sef = SOAPElementFactory.newInstance();
      if (sef == null) {
        TestUtil
            .logErr("createTest2() could not create SOAPElementFactory object");
        pass = false;
      }
      TestUtil.logMsg("Create SOAPElement object with localName=MyName1");
      SOAPElement se = sef.create("MyName1");
      if (se == null) {
        TestUtil.logErr("createTest2() could not create SOAPElement object");
        pass = false;
      } else {
        Name name = se.getElementName();
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
      TestUtil.logMsg("createTest2() test PASSED");
    else
      TestUtil.logErr("createTest2() test FAILED");
    // Send response object and test result back to client
    if (pass)
      resultProps.setProperty("TESTRESULT", "pass");
    else
      resultProps.setProperty("TESTRESULT", "fail");
    resultProps.list(out);
  }

  private void createTest3(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("createTest3");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();
      SOAPElementFactory sef = SOAPElementFactory.newInstance();
      if (sef == null) {
        TestUtil
            .logErr("createTest3() could not create SOAPElementFactory object");
        pass = false;
      }
      TestUtil.logMsg("Create SOAPElement object with localName=MyName1"
          + ", prefix=MyPrefix1, uri=MyUri1");
      SOAPElement se = sef.create("MyName1", "MyPrefix1", "MyUri1");
      if (se == null) {
        TestUtil.logErr("createTest3() could not create SOAPElement object");
        pass = false;
      } else {
        Name name = se.getElementName();
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
      TestUtil.logMsg("createTest3() test PASSED");
    else
      TestUtil.logErr("createTest3() test FAILED");
    // Send response object and test result back to client
    if (pass)
      resultProps.setProperty("TESTRESULT", "pass");
    else
      resultProps.setProperty("TESTRESULT", "fail");
    resultProps.list(out);
  }
}
