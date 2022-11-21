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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPHeader;

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
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.Name;
import jakarta.xml.soap.SOAPConstants;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.soap.SOAPHeaderElement;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;

public class SOAPHeaderTestServlet extends HttpServlet {
  private MessageFactory mf = null;

  private SOAPMessage msg = null;

  private SOAPPart sp = null;

  private SOAPEnvelope envelope = null;

  private SOAPHeader hdr = null;

  private SOAPHeaderElement she = null;

  private SOAPHeaderElement she1 = null;

  private SOAPHeaderElement she2 = null;

  private SOAPHeaderElement she3 = null;

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
  }

  private void dispatch(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("dispatch");
    String testname = SOAP_Util.getHarnessProps().getProperty("TESTNAME");
    if (testname.equals("addHeaderElementTest1")) {
      TestUtil.logMsg("Starting addHeaderElementTest1");
      addHeaderElementTest1(req, res);
    } else if (testname.equals("addHeaderElementTest2")) {
      TestUtil.logMsg("Starting addHeaderElementTest2");
      addHeaderElementTest2(req, res);
    } else if (testname.equals("examineAllHeaderElementsTest1")) {
      TestUtil.logMsg("Starting examineAllHeaderElementsTest1");
      examineAllHeaderElementsTest1(req, res);
    } else if (testname.equals("examineAllHeaderElementsTest2")) {
      TestUtil.logMsg("Starting examineAllHeaderElementsTest2");
      examineAllHeaderElementsTest2(req, res);
    } else if (testname.equals("examineAllHeaderElementsTest3")) {
      TestUtil.logMsg("Starting examineAllHeaderElementsTest3");
      examineAllHeaderElementsTest3(req, res);
    } else if (testname.equals("examineHeaderElementsTest1")) {
      TestUtil.logMsg("Starting examineHeaderElementsTest1");
      examineHeaderElementsTest1(req, res);
    } else if (testname.equals("examineHeaderElementsTest2")) {
      TestUtil.logMsg("Starting examineHeaderElementsTest2");
      examineHeaderElementsTest2(req, res);
    } else if (testname.equals("examineHeaderElementsTest3")) {
      TestUtil.logMsg("Starting examineHeaderElementsTest3");
      examineHeaderElementsTest3(req, res);
    } else if (testname.equals("examineHeaderElementsTest4")) {
      TestUtil.logMsg("Starting examineHeaderElementsTest4");
      examineHeaderElementsTest4(req, res);
    } else if (testname.equals("examineMustUnderstandHeaderElementsTest1")) {
      TestUtil.logMsg("Starting examineMustUnderstandHeaderElementsTest1");
      examineMustUnderstandHeaderElementsTest1(req, res);
    } else if (testname.equals("examineMustUnderstandHeaderElementsTest2")) {
      TestUtil.logMsg("Starting examineMustUnderstandHeaderElementsTest2");
      examineMustUnderstandHeaderElementsTest2(req, res);
    } else if (testname.equals("examineMustUnderstandHeaderElementsTest3")) {
      TestUtil.logMsg("Starting examineMustUnderstandHeaderElementsTest3");
      examineMustUnderstandHeaderElementsTest3(req, res);
    } else if (testname.equals("extractAllHeaderElementsTest1")) {
      TestUtil.logMsg("Starting extractAllHeaderElementsTest1");
      extractAllHeaderElementsTest1(req, res);
    } else if (testname.equals("extractAllHeaderElementsTest2")) {
      TestUtil.logMsg("Starting extractAllHeaderElementsTest2");
      extractAllHeaderElementsTest2(req, res);
    } else if (testname.equals("extractAllHeaderElementsTest3")) {
      TestUtil.logMsg("Starting extractAllHeaderElementsTest3");
      extractAllHeaderElementsTest3(req, res);
    } else if (testname.equals("extractHeaderElementsTest1")) {
      TestUtil.logMsg("Starting extractHeaderElementsTest1");
      extractHeaderElementsTest1(req, res);
    } else if (testname.equals("extractHeaderElementsTest2")) {
      TestUtil.logMsg("Starting extractHeaderElementsTest2");
      extractHeaderElementsTest2(req, res);
    } else if (testname.equals("extractHeaderElementsTest3")) {
      TestUtil.logMsg("Starting extractHeaderElementsTest3");
      extractHeaderElementsTest3(req, res);
    } else if (testname.equals("addNotUnderstoodHeaderElementSOAP11Test")) {
      TestUtil.logMsg("Starting addNotUnderstoodHeaderElementSOAP11Test");
      addNotUnderstoodHeaderElementSOAP11Test(req, res);
    } else if (testname.equals("addNotUnderstoodHeaderElementSOAP12Test")) {
      TestUtil.logMsg("Starting addNotUnderstoodHeaderElementSOAP12Test");
      addNotUnderstoodHeaderElementSOAP12Test(req, res);
    } else if (testname.equals("addUpgradeHeaderElementTest1")) {
      TestUtil.logMsg("Starting addUpgradeHeaderElementTest1");
      addUpgradeHeaderElementTest1(req, res);
    } else if (testname.equals("addUpgradeHeaderElementTest2")) {
      TestUtil.logMsg("Starting addUpgradeHeaderElementTest2");
      addUpgradeHeaderElementTest2(req, res);
    } else if (testname.equals("addUpgradeHeaderElementTest3")) {
      TestUtil.logMsg("Starting addUpgradeHeaderElementTest3");
      addUpgradeHeaderElementTest3(req, res);
    } else {
      throw new ServletException(
          "The testname '" + testname + "' was not found in the test servlet");
    }
  }

  public void init(ServletConfig servletConfig) throws ServletException {
    super.init(servletConfig);
    System.out.println("SOAPHeaderTestServlet:init (Entering)");
    SOAP_Util.doServletInit(servletConfig);
    System.out.println("SOAPHeaderTestServlet:init (Leaving)");
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

  private void addNotUnderstoodHeaderElementSOAP11Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("addNotUnderstoodHeaderElementSOAP11Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Calling addNotUnderstoodHeaderElement() should"
          + " throw UnsupportedOperationException");
      she = hdr.addNotUnderstoodHeaderElement(
          new QName("http://foo.org", "foo", "f"));
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

  private void addNotUnderstoodHeaderElementSOAP12Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("addNotUnderstoodHeaderElementSOAP12Test");
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
        TestUtil.logMsg("QualifiedName = " + name.getQualifiedName());
        TestUtil.logMsg("Prefix = " + name.getPrefix());
        TestUtil.logMsg("LocalName = " + name.getLocalName());
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

  private void addUpgradeHeaderElementTest1(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("addUpgradeHeaderElementTest1");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      // create a list of supported URIs.
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
        TestUtil.logMsg("QualifiedName = " + name.getQualifiedName());
        TestUtil.logMsg("Prefix = " + name.getPrefix());
        TestUtil.logMsg("LocalName = " + name.getLocalName());
        String uri = name.getURI();
        String localName = name.getLocalName();
        if (SOAP_Util.getSOAPVersion().equals("soap11")) {
          TestUtil.logMsg("Validate the URI which must be "
              + SOAPConstants.URI_NS_SOAP_ENVELOPE);
          if (!uri.equals(SOAPConstants.URI_NS_SOAP_ENVELOPE)) {
            TestUtil.logErr("Got URI: " + uri + "\nExpected URI: "
                + SOAPConstants.URI_NS_SOAP_ENVELOPE);
            pass = false;
          }
        } else {
          TestUtil.logMsg("Validate the URI which must be "
              + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
          if (!uri.equals(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE)) {
            TestUtil.logErr("Got URI: " + uri + "\nExpected URI: "
                + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
            pass = false;
          }
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

  private void addUpgradeHeaderElementTest2(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("addUpgradeHeaderElementTest2");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      // create a String array of supported URIs.
      String[] supported = new String[2];
      supported[0] = SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE;
      supported[1] = SOAPConstants.URI_NS_SOAP_ENVELOPE;

      TestUtil.logMsg("Creating Upgrade SOAPHeaderElement");
      she = hdr.addUpgradeHeaderElement(supported);

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
        TestUtil.logMsg("QualifiedName = " + name.getQualifiedName());
        TestUtil.logMsg("Prefix = " + name.getPrefix());
        TestUtil.logMsg("LocalName = " + name.getLocalName());
        String uri = name.getURI();
        String localName = name.getLocalName();
        if (SOAP_Util.getSOAPVersion().equals("soap11")) {
          TestUtil.logMsg("Validate the URI which must be "
              + SOAPConstants.URI_NS_SOAP_ENVELOPE);
          if (!uri.equals(SOAPConstants.URI_NS_SOAP_ENVELOPE)) {
            TestUtil.logErr("Got URI: " + uri + "\nExpected URI: "
                + SOAPConstants.URI_NS_SOAP_ENVELOPE);
            pass = false;
          }
        } else {
          TestUtil.logMsg("Validate the URI which must be "
              + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
          if (!uri.equals(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE)) {
            TestUtil.logErr("Got URI: " + uri + "\nExpected URI: "
                + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
            pass = false;
          }
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

  private void addUpgradeHeaderElementTest3(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("addUpgradeHeaderElementTest3");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      // create a String to a supported URI.
      String supported;
      supported = SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE;

      TestUtil.logMsg("Creating Upgrade SOAPHeaderElement");
      she = hdr.addUpgradeHeaderElement(supported);

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
        TestUtil.logMsg("QualifiedName = " + name.getQualifiedName());
        TestUtil.logMsg("Prefix = " + name.getPrefix());
        TestUtil.logMsg("LocalName = " + name.getLocalName());
        String uri = name.getURI();
        String localName = name.getLocalName();
        if (SOAP_Util.getSOAPVersion().equals("soap11")) {
          TestUtil.logMsg("Validate the URI which must be "
              + SOAPConstants.URI_NS_SOAP_ENVELOPE);
          if (!uri.equals(SOAPConstants.URI_NS_SOAP_ENVELOPE)) {
            TestUtil.logErr("Got URI: " + uri + "\nExpected URI: "
                + SOAPConstants.URI_NS_SOAP_ENVELOPE);
            pass = false;
          }
        } else {
          TestUtil.logMsg("Validate the URI which must be "
              + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
          if (!uri.equals(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE)) {
            TestUtil.logErr("Got URI: " + uri + "\nExpected URI: "
                + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
            pass = false;
          }
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

  private void addHeaderElementTest1(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("addHeaderElementTest1");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating SOAPHeaderElement");
      Name name = envelope.createName("foo", "f", "foo-URI");
      she = hdr.addHeaderElement(name);

      TestUtil.logMsg("Validating SOAPHeaderElement object creation ...");
      if (she == null) {
        TestUtil.logErr("SOAPHeaderElement is null");
        pass = false;
      } else {
        TestUtil.logMsg("SOAPHeaderElement was created");
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

  private void addHeaderElementTest2(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("addHeaderElementTest2");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating SOAPHeaderElement");
      QName name = new QName("foo-URI", "foo", "f");
      she = hdr.addHeaderElement(name);

      TestUtil.logMsg("Validating SOAPHeaderElement object creation ...");
      if (she == null) {
        TestUtil.logErr("SOAPHeaderElement is null");
        pass = false;
      } else {
        TestUtil.logMsg("SOAPHeaderElement was created");
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

  private void examineAllHeaderElementsTest1(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("examineAllHeaderElementsTest1");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    SOAPHeaderElement she = null;

    try {
      setup();

      TestUtil.logMsg("Creating SOAPHeaderElement 1");
      she1 = hdr
          .addHeaderElement(envelope.createName("foo1", "f1", "foo1-URI"));

      if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
        TestUtil.logMsg("Set the actor associated with SOAPHeaderElement");
        she1.setActor("actor1-URI");
      } else {
        TestUtil.logMsg("Set the role associated with SOAPHeaderElement");
        she1.setRole("role1-URI");
      }

      TestUtil.logMsg("Examing all SOAPHeaderElements");
      Iterator iterator = hdr.examineAllHeaderElements();

      TestUtil.logMsg("Validating SOAPHeaderElements ...");
      int cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        she = (SOAPHeaderElement) iterator.next();
        if (!she.equals(she1)) {
          TestUtil.logErr("SOAPHeaderElement does not match");
          pass = false;
        } else {
          TestUtil.logMsg("SOAPHeaderElement does match");
        }
      }

      if (cnt != 1) {
        TestUtil.logErr(
            "SOAPHeaderElement count mismatch: expected 1, received " + cnt);
        pass = false;
      } else
        TestUtil
            .logMsg("SOAPHeaderElement count matches expected # of elements 1");

      TestUtil.logMsg("Examing all SOAPHeaderElements again ...");
      iterator = hdr.examineAllHeaderElements();
      if (!iterator.hasNext()) {
        TestUtil.logErr("no elements in iterator - unexpected");
        pass = false;
      } else
        TestUtil.logMsg("elements in iterator - expected");

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

  private void examineAllHeaderElementsTest2(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("examineAllHeaderElementsTest2");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    SOAPHeaderElement she = null;

    try {
      setup();

      TestUtil.logMsg("Creating SOAPHeaderElement 1");
      she1 = hdr
          .addHeaderElement(envelope.createName("foo1", "f1", "foo1-URI"));

      if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
        TestUtil.logMsg("Set the actor associated with SOAPHeaderElement");
        she1.setActor("actor-URI");
      } else {
        TestUtil.logMsg("Set the role associated with SOAPHeaderElement");
        she1.setRole("role-URI");
      }

      TestUtil.logMsg("Creating SOAPHeaderElement 2");
      she2 = hdr
          .addHeaderElement(envelope.createName("foo2", "f2", "foo2-URI"));

      if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
        TestUtil.logMsg("Set the actor associated with SOAPHeaderElement");
        she2.setActor("actor-URI");
      } else {
        TestUtil.logMsg("Set the role associated with SOAPHeaderElement");
        she2.setRole("role-URI");
      }

      TestUtil.logMsg("Examing all SOAPHeaderElements");
      Iterator iterator = hdr.examineAllHeaderElements();

      TestUtil.logMsg("Validating SOAPHeaderElements ...");
      int cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        she = (SOAPHeaderElement) iterator.next();
        if (!she.equals(she1) && !she.equals(she2)) {
          TestUtil.logErr("SOAPHeaderElement does not match");
          pass = false;
        } else {
          TestUtil.logMsg("SOAPHeaderElement does match");
        }
      }

      if (cnt != 2) {
        TestUtil.logErr(
            "SOAPHeaderElement count mismatch: expected 2, received " + cnt);
        pass = false;
      } else
        TestUtil
            .logMsg("SOAPHeaderElement count matches expected # of elements 2");

      TestUtil.logMsg("Examing all SOAPHeaderElements again ...");
      iterator = hdr.examineAllHeaderElements();
      if (!iterator.hasNext()) {
        TestUtil.logErr("no elements in iterator - unexpected");
        pass = false;
      } else
        TestUtil.logMsg("elements in iterator - expected");

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

  private void examineAllHeaderElementsTest3(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("examineAllHeaderElementsTest3");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    SOAPHeaderElement she = null;

    try {
      setup();

      TestUtil.logMsg("Creating SOAPHeaderElement 1");
      she1 = hdr
          .addHeaderElement(envelope.createName("foo1", "f1", "foo1-URI"));

      if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
        TestUtil.logMsg("Set the actor associated with SOAPHeaderElement");
        she1.setActor("actor1-URI");
      } else {
        TestUtil.logMsg("Set the role associated with SOAPHeaderElement");
        she1.setRole("role1-URI");
      }

      TestUtil.logMsg("Creating SOAPHeaderElement 2");
      she2 = hdr
          .addHeaderElement(envelope.createName("foo2", "f2", "foo2-URI"));

      if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
        TestUtil.logMsg("Set the actor associated with SOAPHeaderElement");
        she2.setActor("actor2-URI");
      } else {
        TestUtil.logMsg("Set the role associated with SOAPHeaderElement");
        she2.setRole("role2-URI");
      }

      TestUtil.logMsg("Examing all SOAPHeaderElements");
      Iterator iterator = hdr.examineAllHeaderElements();

      TestUtil.logMsg("Validating SOAPHeaderElements ...");
      int cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        she = (SOAPHeaderElement) iterator.next();
        if (!she.equals(she1) && !she.equals(she2)) {
          TestUtil.logErr("SOAPHeaderElement does not match");
          pass = false;
        } else {
          TestUtil.logMsg("SOAPHeaderElement does match");
        }
      }

      if (cnt != 2) {
        TestUtil.logErr(
            "SOAPHeaderElement count mismatch: expected 2, received " + cnt);
        pass = false;
      } else
        TestUtil
            .logMsg("SOAPHeaderElement count matches expected # of elements 2");

      TestUtil.logMsg("Examing all SOAPHeaderElements again ...");
      iterator = hdr.examineAllHeaderElements();
      if (!iterator.hasNext()) {
        TestUtil.logErr("no elements in iterator - unexpected");
        pass = false;
      } else
        TestUtil.logMsg("elements in iterator - expected");

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

  private void examineHeaderElementsTest1(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("examineHeaderElementsTest1");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    SOAPHeaderElement she = null;

    try {
      setup();

      TestUtil.logMsg("Creating SOAPHeaderElement 1");
      she1 = hdr
          .addHeaderElement(envelope.createName("foo1", "f1", "foo1-URI"));

      Iterator iterator = null;
      if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
        TestUtil.logMsg("Set the actor associated with SOAPHeaderElement");
        she1.setActor("actor-URI");
        TestUtil.logMsg("Examing SOAPHeaderElements with actor actor-URI");
        iterator = hdr.examineHeaderElements("actor-URI");
      } else {
        TestUtil.logMsg("Set the role associated with SOAPHeaderElement");
        she1.setRole("role-URI");
        TestUtil.logMsg("Examing SOAPHeaderElements with role role-URI");
        iterator = hdr.examineHeaderElements("role-URI");
      }

      TestUtil.logMsg("Validating SOAPHeaderElements ...");
      int cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        she = (SOAPHeaderElement) iterator.next();
        if (!she.equals(she1)) {
          TestUtil.logErr("SOAPHeaderElement does not match");
          pass = false;
        } else {
          TestUtil.logMsg("SOAPHeaderElement does match");
        }
      }

      if (cnt != 1) {
        TestUtil.logErr(
            "SOAPHeaderElement count mismatch: expected 1, received " + cnt);
        pass = false;
      } else
        TestUtil
            .logMsg("SOAPHeaderElement count matches expected # of elements 1");

      if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
        TestUtil.logMsg(
            "Examing SOAPHeaderElements with actor actor-URI again ...");
        iterator = hdr.examineHeaderElements("actor-URI");
      } else {
        TestUtil
            .logMsg("Examing SOAPHeaderElements with role role-URI again ...");
        iterator = hdr.examineHeaderElements("role-URI");
      }
      if (!iterator.hasNext()) {
        TestUtil.logErr("no elements in iterator - unexpected");
        pass = false;
      } else
        TestUtil.logMsg("elements in iterator - expected");

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

  private void examineHeaderElementsTest2(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("examineHeaderElementsTest2");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    SOAPHeaderElement she = null;

    try {
      setup();

      TestUtil.logMsg("Creating SOAPHeaderElement 1");
      she1 = hdr
          .addHeaderElement(envelope.createName("foo1", "f1", "foo1-URI"));

      if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
        TestUtil.logMsg("Set the actor associated with SOAPHeaderElement");
        she1.setActor("actor-URI");
      } else {
        TestUtil.logMsg("Set the role associated with SOAPHeaderElement");
        she1.setRole("role-URI");
      }

      TestUtil.logMsg("Creating SOAPHeaderElement 2");
      she2 = hdr
          .addHeaderElement(envelope.createName("foo2", "f2", "foo2-URI"));

      if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
        TestUtil.logMsg("Set the actor associated with SOAPHeaderElement");
        she2.setActor("actor-URI");
      } else {
        TestUtil.logMsg("Set the role associated with SOAPHeaderElement");
        she2.setRole("role-URI");
      }

      Iterator iterator = null;
      if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
        TestUtil.logMsg("Examing SOAPHeaderElements with actor actor-URI");
        iterator = hdr.examineHeaderElements("actor-URI");
      } else {
        TestUtil.logMsg("Examing SOAPHeaderElements with role role-URI");
        iterator = hdr.examineHeaderElements("role-URI");
      }

      TestUtil.logMsg("Validating SOAPHeaderElements ...");
      int cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        she = (SOAPHeaderElement) iterator.next();
        if (!she.equals(she1) && !she.equals(she2)) {
          TestUtil.logErr("SOAPHeaderElement does not match");
          pass = false;
        } else {
          TestUtil.logMsg("SOAPHeaderElement does match");
        }
      }

      if (cnt != 2) {
        TestUtil.logErr(
            "SOAPHeaderElement count mismatch: expected 2, received " + cnt);
        pass = false;
      } else
        TestUtil
            .logMsg("SOAPHeaderElement count matches expected # of elements 2");

      if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
        TestUtil.logMsg(
            "Examing SOAPHeaderElements with actor actor-URI again ...");
        iterator = hdr.examineHeaderElements("actor-URI");
      } else {
        TestUtil
            .logMsg("Examing SOAPHeaderElements with role role-URI again ...");
        iterator = hdr.examineHeaderElements("role-URI");
      }
      if (!iterator.hasNext()) {
        TestUtil.logErr("no elements in iterator - unexpected");
        pass = false;
      } else
        TestUtil.logMsg("elements in iterator - expected");

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

  private void examineHeaderElementsTest3(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("examineHeaderElementsTest3");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    SOAPHeaderElement she = null;

    try {
      setup();

      TestUtil.logMsg("Creating SOAPHeaderElement 1");
      she1 = hdr
          .addHeaderElement(envelope.createName("foo1", "f1", "foo1-URI"));

      Iterator iterator = null;
      if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
        TestUtil.logMsg("Set the actor associated with SOAPHeaderElement");
        she1.setActor("actor-URI");

        TestUtil.logMsg("Examing SOAPHeaderElements with actor of actor1-URI");
        iterator = hdr.examineHeaderElements("actor1-URI");
      } else {
        TestUtil.logMsg("Set the role associated with SOAPHeaderElement");
        she1.setRole("role-URI");

        TestUtil.logMsg("Examing SOAPHeaderElements with role of role1-URI");
        iterator = hdr.examineHeaderElements("role1-URI");
      }

      int cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        iterator.next();
      }

      if (cnt != 0) {
        TestUtil.logErr(
            "SOAPHeaderElement count mismatch: expected 0, received " + cnt);
        pass = false;
      } else
        TestUtil
            .logMsg("SOAPHeaderElement count matches expected # of elements");
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

  private void examineHeaderElementsTest4(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("examineHeaderElementsTest4");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    SOAPHeaderElement she = null;

    try {
      setup();

      // Add some soap header elements
      TestUtil.logMsg("Add SOAP HeaderElement Header1");
      SOAPElement se = hdr
          .addHeaderElement(
              envelope.createName("Header1", "prefix", "http://myuri"))
          .addTextNode("This is Header1");
      she = (SOAPHeaderElement) se;
      she.setMustUnderstand(true);

      TestUtil.logMsg("Add SOAP HeaderElement Header2");
      se = hdr
          .addHeaderElement(
              envelope.createName("Header2", "prefix", "http://myuri"))
          .addTextNode("This is Header2");
      she = (SOAPHeaderElement) se;
      she.setMustUnderstand(false);

      TestUtil.logMsg("Add SOAP HeaderElement Header3");
      se = hdr
          .addHeaderElement(
              envelope.createName("Header3", "prefix", "http://myuri"))
          .addTextNode("This is Header3");
      she = (SOAPHeaderElement) se;
      she.setMustUnderstand(true);

      TestUtil.logMsg("Add SOAP HeaderElement Header4");
      se = hdr
          .addHeaderElement(
              envelope.createName("Header4", "prefix", "http://myuri"))
          .addTextNode("This is Header4");
      she = (SOAPHeaderElement) se;
      she.setMustUnderstand(false);

      TestUtil.logMsg("Examing all SOAPHeaderElements");
      Iterator iterator = hdr.examineAllHeaderElements();

      TestUtil.logMsg("Validating Iterator count .... should be 4");
      int cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        she = (SOAPHeaderElement) iterator.next();
      }
      if (cnt != 4) {
        TestUtil.logErr(
            "SOAPHeaderElement count mismatch: expected 4, received " + cnt);
        pass = false;
      }

      TestUtil.logMsg("Examing SOAPHeaderElements passing actor next uri");
      iterator = hdr.examineHeaderElements(SOAPConstants.URI_SOAP_ACTOR_NEXT);

      TestUtil.logMsg("Validating Iterator count .... should now be 0");
      cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        she = (SOAPHeaderElement) iterator.next();
      }
      if (cnt != 0) {
        TestUtil.logErr(
            "SOAPHeaderElement count mismatch: expected 0, received " + cnt);
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

  private void examineMustUnderstandHeaderElementsTest1(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {

    TestUtil.logTrace("examineMustUnderstandHeaderElementsTest1");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    SOAPHeaderElement she = null;

    try {
      setup();

      TestUtil.logMsg("Creating SOAPHeaderElement 1");
      she1 = hdr
          .addHeaderElement(envelope.createName("foo1", "f1", "foo1-URI"));

      if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
        TestUtil.logMsg("Set the actor associated with SOAPHeaderElement");
        she1.setActor("actor-URI");
      } else {
        TestUtil.logMsg("Set the role associated with SOAPHeaderElement");
        she1.setRole("role-URI");
      }
      she1.setMustUnderstand(true);

      TestUtil.logMsg("Examing MustUnderstand SOAPHeaderElements");
      Iterator iterator;
      if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11))
        iterator = hdr.examineMustUnderstandHeaderElements("actor-URI");
      else
        iterator = hdr.examineMustUnderstandHeaderElements("role-URI");

      TestUtil.logMsg("Validating SOAPHeaderElements ...");
      int cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        she = (SOAPHeaderElement) iterator.next();
        if (!she.equals(she1)) {
          TestUtil.logErr("SOAPHeaderElement does not match");
          pass = false;
        } else {
          TestUtil.logMsg("SOAPHeaderElement does match");
        }
      }

      if (cnt != 1) {
        TestUtil.logErr(
            "SOAPHeaderElement count mismatch: expected 1, received " + cnt);
        pass = false;
      } else
        TestUtil
            .logMsg("SOAPHeaderElement count matches expected # of elements 1");

      TestUtil.logMsg("Examing all SOAPHeaderElements again ...");
      iterator = hdr.examineAllHeaderElements();
      if (!iterator.hasNext()) {
        TestUtil.logErr("no elements in iterator - unexpected");
        pass = false;
      } else
        TestUtil.logMsg("elements in iterator - expected");

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

  private void examineMustUnderstandHeaderElementsTest2(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {

    TestUtil.logTrace("examineMustUnderstandHeaderElementsTest2");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    SOAPHeaderElement she = null;

    try {
      setup();

      TestUtil.logMsg("Creating SOAPHeaderElement 1");
      she1 = hdr
          .addHeaderElement(envelope.createName("foo1", "f1", "foo1-URI"));

      if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
        TestUtil.logMsg("Set the actor associated with SOAPHeaderElement");
        she1.setActor("actor-URI");
      } else {
        TestUtil.logMsg("Set the role associated with SOAPHeaderElement");
        she1.setRole("role-URI");
      }
      she1.setMustUnderstand(false);

      TestUtil.logMsg("Examing MustUnderstand SOAPHeaderElements");
      Iterator iterator;
      if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11))
        iterator = hdr.examineMustUnderstandHeaderElements("actor-URI");
      else
        iterator = hdr.examineMustUnderstandHeaderElements("role-URI");
      if (iterator.hasNext()) {
        TestUtil
            .logErr("MustUnderstand is false, expected no SOAPHeaderElements");
        pass = false;
      } else {
        TestUtil
            .logMsg("Good no SOAPHeaderElements - MustUnderstand is false.");
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

  private void examineMustUnderstandHeaderElementsTest3(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {

    TestUtil.logTrace("examineMustUnderstandHeaderElementsTest3");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    SOAPHeaderElement she = null;

    try {
      setup();

      TestUtil.logMsg("Creating SOAPHeaderElement 1");
      she1 = hdr
          .addHeaderElement(envelope.createName("foo1", "f1", "foo1-URI"));

      if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
        TestUtil.logMsg("Set the actor associated with SOAPHeaderElement");
        she1.setActor("actor-URI1");
      } else {
        TestUtil.logMsg("Set the role associated with SOAPHeaderElement");
        she1.setRole("role-URI1");
      }
      she1.setMustUnderstand(true);

      TestUtil.logMsg("Creating SOAPHeaderElement 2");
      she2 = hdr
          .addHeaderElement(envelope.createName("foo2", "f2", "foo2-URI"));

      if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
        TestUtil.logMsg("Set the actor associated with SOAPHeaderElement");
        she2.setActor("actor-URI2");
      } else {
        TestUtil.logMsg("Set the role associated with SOAPHeaderElement");
        she2.setRole("role-URI2");
      }
      she2.setMustUnderstand(false);

      TestUtil.logMsg("Creating SOAPHeaderElement 3");
      she3 = hdr
          .addHeaderElement(envelope.createName("foo3", "f3", "foo3-URI"));

      if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
        TestUtil.logMsg("Set the actor associated with SOAPHeaderElement");
        she3.setActor("actor-URI3");
      } else {
        TestUtil.logMsg("Set the role associated with SOAPHeaderElement");
        she3.setRole("role-URI3");
      }
      she3.setMustUnderstand(false);

      TestUtil.logMsg("Examing all SOAPHeaderElements");
      Iterator iterator;
      if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11))
        iterator = hdr.examineMustUnderstandHeaderElements("actor-URI1");
      else
        iterator = hdr.examineMustUnderstandHeaderElements("role-URI1");

      TestUtil.logMsg("Validating SOAPHeaderElements ...");
      int cnt = 0;
      while (iterator.hasNext()) {
        SOAPHeaderElement myShe = (SOAPHeaderElement) iterator.next();
        if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
          TestUtil.logMsg("Got this actor: " + myShe.getActor());
          if (!(myShe.getActor().equals("actor-URI1")))
            pass = false;
        } else {
          TestUtil.logMsg("Got this role: " + myShe.getRole());
          if (!(myShe.getRole().equals("role-URI1")))
            pass = false;
        }
        cnt++;
      }

      if (cnt != 1) {
        TestUtil.logErr(
            "SOAPHeaderElement count mismatch: expected 1 , received " + cnt);
        pass = false;
      } else
        TestUtil.logMsg(
            "SOAPHeaderElement count matches expected # of elements 1 ");

      TestUtil.logMsg("Examing all SOAPHeaderElements again ...");
      iterator = hdr.examineAllHeaderElements();
      if (!iterator.hasNext()) {
        TestUtil.logErr("no elements in iterator - unexpected");
        pass = false;
      } else
        TestUtil.logMsg("elements in iterator - expected");

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

  private void extractAllHeaderElementsTest1(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("extractAllHeaderElementsTest1");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    SOAPHeaderElement she = null;

    try {
      setup();

      TestUtil.logMsg("Creating SOAPHeaderElement 1");
      she1 = hdr
          .addHeaderElement(envelope.createName("foo1", "f1", "foo1-URI"));

      if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
        TestUtil.logMsg("Set the actor associated with SOAPHeaderElement");
        she1.setActor("actor-URI");
      } else {
        TestUtil.logMsg("Set the role associated with SOAPHeaderElement");
        she1.setRole("role-URI");
      }

      TestUtil.logMsg("Extract all SOAPHeaderElements");
      Iterator iterator = hdr.extractAllHeaderElements();

      TestUtil.logMsg("Validating SOAPHeaderElements ...");
      int cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        she = (SOAPHeaderElement) iterator.next();
        if (!she.equals(she1)) {
          TestUtil.logErr("SOAPHeaderElement does not match");
          pass = false;
        } else {
          TestUtil.logMsg("SOAPHeaderElement does match");
        }
      }

      if (cnt != 1) {
        TestUtil.logErr(
            "SOAPHeaderElement count mismatch: expected 1, received " + cnt);
        pass = false;
      } else
        TestUtil
            .logMsg("SOAPHeaderElement count matches expected # of elements 1");

      TestUtil.logMsg("Extract all SOAPHeaderElements again ...");
      iterator = hdr.extractAllHeaderElements();
      if (iterator.hasNext()) {
        TestUtil.logErr("elements in iterator - unexpected");
        pass = false;
      } else
        TestUtil.logMsg("no elements in iterator - expected");

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

  private void extractAllHeaderElementsTest2(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("extractAllHeaderElementsTest2");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    SOAPHeaderElement she = null;

    try {
      setup();

      TestUtil.logMsg("Creating SOAPHeaderElement 1");
      she1 = hdr
          .addHeaderElement(envelope.createName("foo1", "f1", "foo1-URI"));

      if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
        TestUtil.logMsg("Set the actor associated with SOAPHeaderElement");
        she1.setActor("actor-URI");
      } else {
        TestUtil.logMsg("Set the role associated with SOAPHeaderElement");
        she1.setRole("role-URI");
      }

      TestUtil.logMsg("Creating SOAPHeaderElement 2");
      she2 = hdr
          .addHeaderElement(envelope.createName("foo2", "f2", "foo2-URI"));

      if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
        TestUtil.logMsg("Set the actor associated with SOAPHeaderElement");
        she2.setActor("actor-URI");
      } else {
        TestUtil.logMsg("Set the role associated with SOAPHeaderElement");
        she2.setRole("role-URI");
      }

      TestUtil.logMsg("Extract all SOAPHeaderElements");
      Iterator iterator = hdr.extractAllHeaderElements();

      TestUtil.logMsg("Validating SOAPHeaderElements ...");
      int cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        she = (SOAPHeaderElement) iterator.next();
        if (!she.equals(she1) && !she.equals(she2)) {
          TestUtil.logErr("SOAPHeaderElement does not match");
          pass = false;
        } else {
          TestUtil.logMsg("SOAPHeaderElement does match");
        }
      }

      if (cnt != 2) {
        TestUtil.logErr(
            "SOAPHeaderElement count mismatch: expected 2, received " + cnt);
        pass = false;
      } else
        TestUtil
            .logMsg("SOAPHeaderElement count matches expected # of elements 2");

      TestUtil.logMsg("Extract all SOAPHeaderElements again ...");
      iterator = hdr.extractAllHeaderElements();
      if (iterator.hasNext()) {
        TestUtil.logErr("elements in iterator - unexpected");
        pass = false;
      } else
        TestUtil.logMsg("no elements in iterator - expected");

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

  private void extractAllHeaderElementsTest3(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("extractAllHeaderElementsTest3");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    SOAPHeaderElement she = null;

    try {
      setup();

      TestUtil.logMsg("Creating SOAPHeaderElement 1");
      she1 = hdr
          .addHeaderElement(envelope.createName("foo1", "f1", "foo1-URI"));

      if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
        TestUtil.logMsg("Set the actor associated with SOAPHeaderElement");
        she1.setActor("actor1-URI");
      } else {
        TestUtil.logMsg("Set the role associated with SOAPHeaderElement");
        she1.setRole("role1-URI");
      }

      TestUtil.logMsg("Creating SOAPHeaderElement 2");
      she2 = hdr
          .addHeaderElement(envelope.createName("foo2", "f2", "foo2-URI"));

      if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
        TestUtil.logMsg("Set the actor associated with SOAPHeaderElement");
        she2.setActor("actor2-URI");
      } else {
        TestUtil.logMsg("Set the role associated with SOAPHeaderElement");
        she2.setRole("role2-URI");
      }

      TestUtil.logMsg("Extract all SOAPHeaderElements");
      Iterator iterator = hdr.extractAllHeaderElements();

      TestUtil.logMsg("Validating SOAPHeaderElements ...");
      int cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        she = (SOAPHeaderElement) iterator.next();
        if (!she.equals(she1) && !she.equals(she2)) {
          TestUtil.logErr("SOAPHeaderElement does not match");
          pass = false;
        } else {
          TestUtil.logMsg("SOAPHeaderElement does match");
        }
      }

      if (cnt != 2) {
        TestUtil.logErr(
            "SOAPHeaderElement count mismatch: expected 2, received " + cnt);
        pass = false;
      } else
        TestUtil
            .logMsg("SOAPHeaderElement count matches expected # of elements 2");

      TestUtil.logMsg("Extract all SOAPHeaderElements again ...");
      iterator = hdr.extractAllHeaderElements();
      if (iterator.hasNext()) {
        TestUtil.logErr("elements in iterator - unexpected");
        pass = false;
      } else
        TestUtil.logMsg("no elements in iterator - expected");

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

  private void extractHeaderElementsTest1(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("extractHeaderElementsTest1");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    SOAPHeaderElement she = null;

    try {
      setup();

      TestUtil.logMsg("Creating SOAPHeaderElement 1");
      she1 = hdr
          .addHeaderElement(envelope.createName("foo1", "f1", "foo1-URI"));

      Iterator iterator = null;
      if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
        TestUtil.logMsg("Set the actor associated with SOAPHeaderElement");
        she1.setActor("actor-URI");

        TestUtil.logMsg("Extract SOAPHeaderElements with actor actor-URI");
        iterator = hdr.extractHeaderElements("actor-URI");
      } else {
        TestUtil.logMsg("Set the role associated with SOAPHeaderElement");
        she1.setRole("role-URI");

        TestUtil.logMsg("Extract SOAPHeaderElements with role role-URI");
        iterator = hdr.extractHeaderElements("role-URI");
      }

      TestUtil.logMsg("Validating SOAPHeaderElements ...");
      int cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        she = (SOAPHeaderElement) iterator.next();
        if (!she.equals(she1)) {
          TestUtil.logErr("SOAPHeaderElement does not match");
          pass = false;
        } else {
          TestUtil.logMsg("SOAPHeaderElement does match");
        }
      }

      if (cnt != 1) {
        TestUtil.logErr(
            "SOAPHeaderElement count mismatch: expected 1, received " + cnt);
        pass = false;
      } else
        TestUtil
            .logMsg("SOAPHeaderElement count matches expected # of elements 1");

      if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
        TestUtil.logMsg(
            "Extract SOAPHeaderElements with actor actor-URI again ...");
        iterator = hdr.extractHeaderElements("actor-URI");
      } else {
        TestUtil
            .logMsg("Extract SOAPHeaderElements with role role-URI again ...");
        iterator = hdr.extractHeaderElements("role-URI");
      }
      if (iterator.hasNext()) {
        TestUtil.logErr("elements in iterator - unexpected");
        pass = false;
      } else
        TestUtil.logMsg("no elements in iterator - expected");

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

  private void extractHeaderElementsTest2(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("extractHeaderElementsTest2");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    SOAPHeaderElement she = null;

    try {
      setup();

      TestUtil.logMsg("Creating SOAPHeaderElement 1");
      she1 = hdr
          .addHeaderElement(envelope.createName("foo1", "f1", "foo1-URI"));

      if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
        TestUtil.logMsg("Set the actor associated with SOAPHeaderElement");
        she1.setActor("actor-URI");
      } else {
        TestUtil.logMsg("Set the role associated with SOAPHeaderElement");
        she1.setRole("role-URI");
      }

      TestUtil.logMsg("Creating SOAPHeaderElement 2");
      she2 = hdr
          .addHeaderElement(envelope.createName("foo2", "f2", "foo2-URI"));

      if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
        TestUtil.logMsg("Set the actor associated with SOAPHeaderElement");
        she2.setActor("actor-URI");
      } else {
        TestUtil.logMsg("Set the role associated with SOAPHeaderElement");
        she2.setRole("role-URI");
      }

      Iterator iterator = null;
      if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
        TestUtil.logMsg("Extract SOAPHeaderElements with actor actor-URI");
        iterator = hdr.extractHeaderElements("actor-URI");
      } else {
        TestUtil.logMsg("Extract SOAPHeaderElements with role role-URI");
        iterator = hdr.extractHeaderElements("role-URI");
      }

      TestUtil.logMsg("Validating SOAPHeaderElements ...");
      int cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        she = (SOAPHeaderElement) iterator.next();
        if (!she.equals(she1) && !she.equals(she2)) {
          TestUtil.logErr("SOAPHeaderElement does not match");
          pass = false;
        } else {
          TestUtil.logMsg("SOAPHeaderElement does match");
        }
      }

      if (cnt != 2) {
        TestUtil.logErr(
            "SOAPHeaderElement count mismatch: expected 2, received " + cnt);
        pass = false;
      } else
        TestUtil
            .logMsg("SOAPHeaderElement count matches expected # of elements 2");

      if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
        TestUtil.logMsg(
            "Extract SOAPHeaderElements with actor actor-URI again ...");
        iterator = hdr.extractHeaderElements("actor-URI");
      } else {
        TestUtil
            .logMsg("Extract SOAPHeaderElements with role role-URI again ...");
        iterator = hdr.extractHeaderElements("role-URI");
      }

      if (iterator.hasNext()) {
        TestUtil.logErr("elements in iterator - unexpected");
        pass = false;
      } else
        TestUtil.logMsg("no elements in iterator - expected");

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

  private void extractHeaderElementsTest3(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("extractHeaderElementsTest3");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    SOAPHeaderElement she = null;

    try {
      setup();

      TestUtil.logMsg("Creating SOAPHeaderElement 1");
      she1 = hdr
          .addHeaderElement(envelope.createName("foo1", "f1", "foo1-URI"));

      Iterator iterator = null;
      if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
        TestUtil.logMsg("Set the actor associated with SOAPHeaderElement");
        she1.setActor("actor-URI");

        TestUtil.logMsg("Extract SOAPHeaderElements with actor of actor1-URI");
        iterator = hdr.extractHeaderElements("actor1-URI");
      } else {
        TestUtil.logMsg("Set the role associated with SOAPHeaderElement");
        she1.setRole("role-URI");

        TestUtil.logMsg("Extract SOAPHeaderElements with role of role1-URI");
        iterator = hdr.extractHeaderElements("role1-URI");
      }

      int cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        iterator.next();
      }

      if (cnt != 0) {
        TestUtil.logErr(
            "SOAPHeaderElement count mismatch: expected 0, received " + cnt);
        pass = false;
      } else
        TestUtil
            .logMsg("SOAPHeaderElement count matches expected # of elements");
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
