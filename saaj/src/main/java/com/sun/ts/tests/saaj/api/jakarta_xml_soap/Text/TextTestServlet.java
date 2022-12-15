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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
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
import jakarta.xml.soap.Name;
import jakarta.xml.soap.Node;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPBodyElement;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.soap.SOAPHeaderElement;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;
import jakarta.xml.soap.Text;

public class TextTestServlet extends HttpServlet {
  private MessageFactory mf = null;

  private SOAPMessage msg = null;

  private SOAPPart sp = null;

  private SOAPEnvelope envelope = null;

  private SOAPHeader hdr = null;

  private SOAPHeaderElement she = null;

  private SOAPBody body = null;

  private SOAPBodyElement bodye = null;

  private SOAPElement se = null;

  private PrintStream ps = null;

  private ByteArrayOutputStream baos = null;

  Name name = null;

  private void setup() throws Exception {
    TestUtil.logTrace("setup");

    SOAP_Util.setup();

    // Create a message from the message factory.
    TestUtil.logTrace("Create message from message factory");
    msg = SOAP_Util.getMessageFactory().createMessage();

    // Message creation takes care of creating the SOAPPart - a
    // required part of the message as per the SOAP 1.1 spec.
    TestUtil.logTrace("Get SOAP Part");
    sp = msg.getSOAPPart();

    // Retrieve the envelope from the soap part to start building
    // the soap message.
    TestUtil.logTrace("Get SOAP Envelope");
    envelope = sp.getEnvelope();

    // Retrieve the soap header from the envelope.
    TestUtil.logTrace("Get SOAP Header");
    hdr = envelope.getHeader();

    // Retrieve the soap header from the envelope.
    TestUtil.logTrace("Get SOAP Body");
    body = envelope.getBody();

    name = envelope.createName("foo", "f", "foo-URI");

    TestUtil.logMsg("Creating SOAPHeaderElement");
    she = hdr.addHeaderElement(envelope.createName("foo", "f", "foo-URI"));

    TestUtil.logMsg("Creating SOAPBodyElement");
    bodye = body.addBodyElement(envelope.createName("foo", "f", "foo-URI"));

    baos = new ByteArrayOutputStream();
    ps = new PrintStream(baos);
  }

  private void dispatch(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("dispatch");
    String testname = SOAP_Util.getHarnessProps().getProperty("TESTNAME");
    if (testname.equals("Text1Test")) {
      TestUtil.logTrace("Starting Text1Test");
      Text1Test(req, res);
    } else if (testname.equals("Text2Test")) {
      TestUtil.logTrace("Starting Text2Test");
      Text2Test(req, res);
    } else {
      throw new ServletException(
          "The testname '" + testname + "' was not found in the test servlet");
    }
  }

  public void init(ServletConfig servletConfig) throws ServletException {
    super.init(servletConfig);
    System.out.println("IsCommentTestServlet:init (Entering)");
    SOAP_Util.doServletInit(servletConfig);
    System.out.println("IsCommentTestServlet:init (Leaving)");
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

  private void Text1Test(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("Text1Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();
      TestUtil.logMsg("Add text node with text [This is text]");
      SOAPElement se = body.addBodyElement(envelope.createName("mytext"))
          .addTextNode("This is text");
      msg.writeTo(ps);
      TestUtil.logMsg("Dumping SOAPMessage\n" + baos.toString());
      TestUtil.logMsg("get child elements");
      Iterator iterator = se.getChildElements();
      Node n = null;
      if (!iterator.hasNext())
        TestUtil.logMsg("no child elements");
      while (iterator.hasNext()) {
        n = (Node) iterator.next();
        TestUtil.logMsg("Node is: " + n);
        if (n instanceof Text)
          break;
      }

      if (!(n instanceof Text)) {
        TestUtil.logErr("no Text element was added - unexpected");
        pass = false;
      } else {
        Text t = (Text) n;
        TestUtil.logMsg("Executing IsComment");
        boolean bResult = t.isComment();
        if (bResult == true) {
          TestUtil.logErr("addTextNodeTest() test FAILED");
          TestUtil.logErr("isComment() did not return false");
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

  private void Text2Test(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("Text2Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();
      TestUtil.logMsg("Add text node with text <!-- This is text -->");
      SOAPElement se = body.addTextNode("<!-- This is text -->");
      msg.writeTo(ps);
      TestUtil.logMsg("Dumping SOAPMessage\n" + baos.toString());
      TestUtil.logMsg("get child elements");
      Iterator iterator = se.getChildElements();
      Node n = null;
      if (!iterator.hasNext())
        TestUtil.logMsg("no child elements");
      while (iterator.hasNext()) {
        n = (Node) iterator.next();
        TestUtil.logMsg("Node is: " + n);
        if (n instanceof Text)
          break;
      }

      if (!(n instanceof Text)) {
        TestUtil.logErr("no Text element was added - unexpected");
        pass = false;
      } else {
        Text t = (Text) n;
        TestUtil.logMsg("Executing IsComment");
        boolean bResult = t.isComment();
        if (bResult == false) {
          TestUtil.logErr("addTextNodeTest() test FAILED");
          TestUtil.logErr("isComment() did not return true");
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
