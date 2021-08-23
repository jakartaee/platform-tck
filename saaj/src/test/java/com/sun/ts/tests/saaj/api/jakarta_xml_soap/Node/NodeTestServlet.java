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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.Node;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Properties;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.saaj.common.SOAP_Util;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.soap.SOAPHeaderElement;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;

public class NodeTestServlet extends HttpServlet {
  private MessageFactory mf = null;

  private SOAPMessage msg = null;

  private SOAPPart sp = null;

  private SOAPEnvelope envelope = null;

  private SOAPHeader hdr = null;

  private SOAPBody bdy = null;

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

    // Retrieve the soap body from the envelope.
    TestUtil.logMsg("Get SOAP Body");
    bdy = envelope.getBody();

    TestUtil.logMsg("Creating SOAPHeaderElement");
    she = hdr.addHeaderElement(envelope.createName("foo", "f", "foo-URI"));

    if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
      TestUtil.logMsg("Set the actor associated with SOAPHeaderElement");
      she.setActor("actor-URI");
    } else {
      TestUtil.logMsg("Set the role associated with SOAPHeaderElement");
      she.setRole("role-URI");
    }

  }

  private void dispatch(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("dispatch");
    String testname = SOAP_Util.getHarnessProps().getProperty("TESTNAME");
    if (testname.equals("detachNodeTest")) {
      TestUtil.logMsg("Starting detachNodeTest");
      detachNodeTest(req, res);
    } else if (testname.equals("getParentElementTest1")) {
      TestUtil.logMsg("Starting getParentElementTest1");
      getParentElementTest1(req, res);
    } else if (testname.equals("getParentElementTest2")) {
      TestUtil.logMsg("Starting getParentElementTest2");
      getParentElementTest2(req, res);
    } else if (testname.equals("setParentElementTest1")) {
      TestUtil.logMsg("Starting setParentElementTest1");
      setParentElementTest1(req, res);
    } else if (testname.equals("setParentElementTest2")) {
      TestUtil.logMsg("Starting setParentElementTest2");
      setParentElementTest2(req, res);
    } else if (testname.equals("setParentElementTest3")) {
      TestUtil.logMsg("Starting setParentElementTest3");
      setParentElementTest3(req, res);
    } else if (testname.equals("recycleNodeTest")) {
      TestUtil.logMsg("Starting recycleNodeTest");
      recycleNodeTest(req, res);
    } else if (testname.equals("getValueTest1")) {
      TestUtil.logMsg("Starting getValueTest1");
      getValueTest1(req, res);
    } else if (testname.equals("getValueTest2")) {
      TestUtil.logMsg("Starting getValueTest2");
      getValueTest2(req, res);
    } else if (testname.equals("setValueTest1")) {
      TestUtil.logMsg("Starting setValueTest1");
      setValueTest1(req, res);
    } else if (testname.equals("setValueTest2")) {
      TestUtil.logMsg("Starting setValueTest2");
      setValueTest2(req, res);
    } else if (testname.equals("setValueTest3")) {
      TestUtil.logMsg("Starting setValueTest3");
      setValueTest3(req, res);
    } else {
      throw new ServletException(
          "The testname '" + testname + "' was not found in the test servlet");
    }
  }

  public void init(ServletConfig servletConfig) throws ServletException {
    super.init(servletConfig);
    System.out.println("NodeTestServlet:init (Entering)");
    SOAP_Util.doServletInit(servletConfig);
    System.out.println("NodeTestServlet:init (Leaving)");
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

  private void detachNodeTest(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("detachNodeTest");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Detach SOAPHeaderElement Node ...");
      she.detachNode();

      TestUtil.logMsg("Validate SOAPHeaderElement Node was detached ...");

      // Examine the soap header element from the SOAPHeader.
      Iterator iterator = null;
      if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
        TestUtil.logMsg("Examine SOAPHeaderElements with actor actor-URI");
        iterator = hdr.examineHeaderElements("actor-URI");
      } else {
        TestUtil.logMsg("Examine SOAPHeaderElements with role role-URI");
        iterator = hdr.examineHeaderElements("role-URI");
      }
      if (iterator.hasNext()) {
        TestUtil.logErr("SOAPHeader element is not detached - unexpected");
        pass = false;
      } else {
        TestUtil.logMsg("SOAPHeader element is detached - expected");
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

  private void recycleNodeTest(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("recycleNodeTest");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Detach SOAPHeaderElement Node ...");
      she.detachNode();

      TestUtil.logMsg("Recycle Node that was just detached ...");
      she.recycleNode();

      TestUtil.logMsg("Node has been recycled ...");
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

  private void getParentElementTest1(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getParentElementTest1");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Get Parent Element of SOAPHeaderElement ...");
      SOAPElement se = she.getParentElement();

      TestUtil.logMsg("Validate SOAPElement Node is the SOAPHeader ...");
      if (!(se instanceof SOAPHeader)) {
        TestUtil.logErr("SOAPHeader element not returned - unexpected");
        pass = false;
      } else {
        SOAPHeader sh = (SOAPHeader) se;
        if (!sh.equals(hdr)) {
          TestUtil.logErr("SOAPHeader element does not match");
          pass = false;
        } else
          TestUtil.logMsg("SOAPHeader element does match");
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

  private void getParentElementTest2(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getParentElementTest2");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Detach SOAPHeaderElement Node ...");
      she.detachNode();

      TestUtil.logMsg("Get Parent Element of SOAPHeaderElement ...");
      SOAPElement se = she.getParentElement();

      TestUtil.logMsg("Validate Parent Element is null ...");
      if (se != null) {
        TestUtil.logErr("Parent Element is not null - unexpected");
        pass = false;
      } else {
        TestUtil.logMsg("Parent Element is null - expected");
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

  private void setParentElementTest1(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("setParentElementTest1");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Detach SOAPHeaderElement Node ...");
      she.detachNode();

      TestUtil.logMsg("Set Parent Element of SOAPHeaderElement ...");
      she.setParentElement(hdr);

      TestUtil.logMsg("Get Parent Element of SOAPHeaderElement ...");
      SOAPElement se = she.getParentElement();

      TestUtil.logMsg("Validate Parent Element is set ...");
      if (!(se instanceof SOAPHeader)) {
        TestUtil.logErr("SOAPHeader element not returned - unexpected");
        pass = false;
      } else {
        SOAPHeader sh = (SOAPHeader) se;
        if (!sh.equals(hdr)) {
          TestUtil.logErr("SOAPHeader element does not match");
          pass = false;
        } else
          TestUtil.logMsg("SOAPHeader element does match");
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

  private void setParentElementTest2(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("setParentElementTest2");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg(
          "Attempt to set parent element of SOAPHeaderElement to a body parent ...");
      try {
        she.setParentElement(bdy);
        TestUtil.logErr("no exception occurred, unexpected ...");
        pass = false;
      } catch (IllegalArgumentException e) {
        TestUtil.logMsg("IllegalArgumentException occurred");
      } catch (SOAPException e) {
        TestUtil.logMsg("SOAPException occurred");
      } catch (Exception e) {
        TestUtil.logErr(
            "no IllegalArgumentException or SOAPException, received exception "
                + e);
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

  private void setParentElementTest3(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("setParentElementTest3");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg(
          "Attempt to set parent element of SOAPHeaderElement to a null parent ...");
      try {
        she.setParentElement(null);
        TestUtil.logErr("no exception occurred, unexpected ...");
        pass = false;
      } catch (IllegalArgumentException e) {
        TestUtil.logMsg("IllegalArgumentException occurred");
      } catch (SOAPException e) {
        TestUtil.logMsg("SOAPException occurred");
      } catch (Exception e) {
        TestUtil.logErr(
            "no IllegalArgumentException or SOAPException, received exception "
                + e);
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

  private void getValueTest1(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("getValueTest1");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("adding child element to SOAPHeaderElement ...");
      she.addChildElement("foo-bar");

      TestUtil.logMsg(
          "get value of non-existant child Text Node should return null ...");
      String value = she.getValue();
      if (value != null) {
        TestUtil.logErr("value is not null - unexpected");
        TestUtil.logErr("value=" + value);
        pass = false;
      } else
        TestUtil.logMsg("value is null - expected");

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

  private void getValueTest2(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("getValueTest2");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("adding Text Node \"foo-bar\" to SOAPHeaderElement ...");
      she.addTextNode("foo-bar");
      TestUtil.logMsg(
          "get value of existant child Text Node should return the text nodes value (foo-bar) ...");
      String value = she.getValue();
      if (value == null) {
        TestUtil.logErr("value is null - unexpected");
        pass = false;
      } else if (!value.equals("foo-bar")) {
        TestUtil.logErr("value incorrect - expected: " + "foo-bar"
            + ", received: " + value);
        pass = false;
      } else
        TestUtil.logMsg("value returned is correct: " + value);
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

  private void setValueTest1(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("setValueTest1");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg(
          "Add a Text Node to SOAPHeaderElement initialized to \"foo-bar\"");
      she.addTextNode("foo-bar");
      TestUtil.logMsg(
          "Getting value of SOAPHeaderElement should return \"foo-bar\"");
      String value = she.getValue();
      if (value == null) {
        TestUtil.logErr("value is null - unexpected");
        pass = false;
      } else if (!value.equals("foo-bar")) {
        TestUtil.logErr("value incorrect - expected: " + "foo-bar"
            + ", received: " + value);
        pass = false;
      } else
        TestUtil.logMsg("value returned is correct: " + value);
      TestUtil.logMsg("Resetting value of SOAPHeaderElement to \"foo-bar2\"");
      she.setValue("foo-bar2");
      TestUtil.logMsg(
          "Getting value of SOAPHeaderElement should return \"foo-bar2\"");
      value = she.getValue();
      if (value == null) {
        TestUtil.logErr("value is null - unexpected");
        pass = false;
      } else if (!value.equals("foo-bar2")) {
        TestUtil.logErr("value incorrect - expected: " + "foo-bar2"
            + ", received: " + value);
        pass = false;
      } else
        TestUtil.logMsg("value returned is correct: " + value);
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

  private void setValueTest2(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("setValueTest2");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Adding child element \"child1\" to SOAPHeaderElement");
      SOAPElement se = she.addChildElement("child1");
      TestUtil.logMsg("Add a Text Node to child element \"child1\""
          + " initialized to \"foo-bar\"");
      se.addTextNode("foo-bar");
      TestUtil.logMsg("Getting value of child element should be \"foo-bar\"");
      String value = se.getValue();
      if (value == null) {
        TestUtil.logErr("value is null - unexpected");
        pass = false;
      } else if (!value.equals("foo-bar")) {
        TestUtil.logErr("value incorrect - expected: " + "foo-bar"
            + ", received: " + value);
        pass = false;
      } else
        TestUtil.logMsg("value returned is correct: " + value);
      TestUtil.logMsg("Resetting value of child element to \"foo-bar2\"");
      se.setValue("foo-bar2");
      TestUtil
          .logMsg("Getting value of child element should return \"foo-bar2\"");
      value = se.getValue();
      if (value == null) {
        TestUtil.logErr("value is null - unexpected");
        pass = false;
      } else if (!value.equals("foo-bar2")) {
        TestUtil.logErr("value incorrect - expected: " + "foo-bar2"
            + ", received: " + value);
        pass = false;
      } else
        TestUtil.logMsg("value returned is correct: " + value);
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

  private void setValueTest3(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("setValueTest3");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg(
          "Try setting a value on SOAPHeaderElement which is not a Text Node");
      TestUtil.logMsg("This should throw an IllegalStateException");
      she.setValue("foo-bar");
      TestUtil.logMsg("Adding a child element \"child1\" to SOAPHeaderElement");
      SOAPElement se = she.addChildElement("child1");
      TestUtil.logMsg("Try setting a value on a Node that is not a Text Node");
      she.setValue("foo-bar");
      TestUtil.logErr("Did not throw expected IllegalStateException");
    } catch (IllegalStateException e) {
      TestUtil.logMsg("Caught expected IllegalStateException");
    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    }

    try {
      TestUtil.logMsg("Adding a child element \"child1\" to SOAPHeaderElement");
      SOAPElement se = she.addChildElement("child1");
      TestUtil.logMsg("Try setting a value on a Node that is not a Text Node");
      TestUtil.logMsg(
          "Try setting a value on child element which is not a Text Node");
      TestUtil.logMsg("This should throw an IllegalStateException");
      se.setValue("foo-bar");
      TestUtil.logErr("Did not throw expected IllegalStateException");
    } catch (IllegalStateException e) {
      TestUtil.logMsg("Caught expected IllegalStateException");
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
