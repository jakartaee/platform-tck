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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPMessage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;

import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

import com.sun.ts.lib.porting.TSURL;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.saaj.common.SOAP_Util;

import jakarta.activation.DataHandler;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.soap.AttachmentPart;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.MimeHeaders;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPBodyElement;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.soap.SOAPHeaderElement;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;

public class SOAPMessageTestServlet extends HttpServlet {
  private static final String cntxroot = "/SOAPMessage_web";

  private static final String XML_RESOURCE_FILE = "/attach.xml";

  private static final String TXT_RESOURCE_FILE = "/attach.txt";

  private static final String GIF_RESOURCE_FILE = "/attach.gif";

  private static final String JPEG_RESOURCE_FILE = "/attach.jpeg";

  private static final String HTML_RESOURCE_FILE = "/attach.html";

  private MessageFactory mf = null;

  private SOAPMessage msg = null;

  private SOAPPart sp = null;

  private SOAPEnvelope envelope = null;

  private SOAPHeaderElement she = null;

  ServletContext servletContext = null;

  AttachmentPart ap = null;

  AttachmentPart ap1 = null;

  AttachmentPart ap2 = null;

  AttachmentPart ap3 = null;

  private static final String PROTOCOL = "http";

  private TSURL tsurl = new TSURL();

  URL url1 = null;

  URL url2 = null;

  URL url3 = null;

  URL url4 = null;

  URL url5 = null;

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

    // Want to set an attachment from the following url.
    url1 = tsurl.getURL(PROTOCOL, SOAP_Util.getHostname(),
        SOAP_Util.getPortnum(), cntxroot + XML_RESOURCE_FILE);
    TestUtil.logMsg("URL1 = " + url1);

    // Want to set an attachment from the following url.
    url2 = tsurl.getURL(PROTOCOL, SOAP_Util.getHostname(),
        SOAP_Util.getPortnum(), cntxroot + TXT_RESOURCE_FILE);
    TestUtil.logMsg("URL2 = " + url2);

    // Want to set an attachment from the following url.
    url3 = tsurl.getURL(PROTOCOL, SOAP_Util.getHostname(),
        SOAP_Util.getPortnum(), cntxroot + GIF_RESOURCE_FILE);
    TestUtil.logMsg("URL3 = " + url3);

    TestUtil.logMsg("Create SOAP Attachment 1 (XML document) and add it");
    ap1 = msg.createAttachmentPart(new DataHandler(url1));
    ap1.setContentType("text/xml");
    msg.addAttachmentPart(ap1);

    TestUtil.logMsg("Create SOAP Attachment 2 (XML document) and add it");
    ap2 = msg.createAttachmentPart(new DataHandler(url1));
    ap2.setContentType("text/xml");
    msg.addAttachmentPart(ap2);

    TestUtil
        .logMsg("Create SOAP Attachment 3 (PLAIN/TEXT document) and add it");
    ap3 = msg.createAttachmentPart(new DataHandler(url2));
    ap3.setContentType("text/plain");
    msg.addAttachmentPart(ap3);
  }

  private void dispatch(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("dispatch");
    Properties p = SOAP_Util.getHarnessProps();
    String testname = p.getProperty("TESTNAME");
    if (testname.equals("addAttachmentPartTest")) {
      TestUtil.logMsg("Starting addAttachmentPartTest");
      addAttachmentPartTest(req, res);
    } else if (testname.equals("countAttachmentsTest")) {
      TestUtil.logMsg("Starting countAttachmentsTest");
      countAttachmentsTest(req, res);
    } else if (testname.equals("createAttachmentPartTest1")) {
      TestUtil.logMsg("Starting createAttachmentPartTest1");
      createAttachmentPartTest1(req, res);
    } else if (testname.equals("createAttachmentPartTest2")) {
      TestUtil.logMsg("Starting createAttachmentPartTest2");
      createAttachmentPartTest2(req, res);
    } else if (testname.equals("createAttachmentPartTest3")) {
      TestUtil.logMsg("Starting createAttachmentPartTest3");
      createAttachmentPartTest3(req, res);
    } else if (testname.equals("getAttachmentsTest1")) {
      TestUtil.logMsg("Starting getAttachmentsTest1");
      getAttachmentsTest1(req, res);
    } else if (testname.equals("getAttachmentsTest2")) {
      TestUtil.logMsg("Starting getAttachmentsTest2");
      getAttachmentsTest2(req, res);
    } else if (testname.equals("getContentDescriptionTest")) {
      TestUtil.logMsg("Starting getContentDescriptionTest");
      getContentDescriptionTest(req, res);
    } else if (testname.equals("getMimeHeadersTest")) {
      TestUtil.logMsg("Starting getMimeHeadersTest");
      getMimeHeadersTest(req, res);
    } else if (testname.equals("removeAllAttachmentsTest")) {
      TestUtil.logMsg("Starting removeAllAttachmentsTest");
      removeAllAttachmentsTest(req, res);
    } else if (testname.equals("removeAttachmentsTest")) {
      TestUtil.logMsg("Starting removeAttachmentsTest");
      removeAttachmentsTest(req, res);
    } else if (testname.equals("getSOAPPartTest")) {
      TestUtil.logMsg("Starting getSOAPPartTest");
      getSOAPPartTest(req, res);
    } else if (testname.equals("setPropertyTest")) {
      TestUtil.logMsg("Starting setPropertyTest");
      setPropertyTest(req, res);
    } else if (testname.equals("getPropertyTest")) {
      TestUtil.logMsg("Starting getPropertyTest");
      getPropertyTest(req, res);
    } else if (testname.equals("getSOAPBodyTest")) {
      TestUtil.logMsg("Starting getSOAPBodyTest");
      getSOAPBodyTest(req, res);
    } else if (testname.equals("getSOAPHeaderTest")) {
      TestUtil.logMsg("Starting getSOAPHeaderTest");
      getSOAPHeaderTest(req, res);
    } else if (testname.equals("saveRequiredTest1")) {
      TestUtil.logMsg("Starting saveRequiredTest1");
      saveRequiredTest1(req, res);
    } else if (testname.equals("saveRequiredTest2")) {
      TestUtil.logMsg("Starting saveRequiredTest2");
      saveRequiredTest2(req, res);
    } else if (testname.equals("setContentDescriptionTest")) {
      TestUtil.logMsg("Starting setContentDescriptionTest");
      setContentDescriptionTest(req, res);
    } else if (testname.equals("writeToTest1")) {
      TestUtil.logMsg("Starting writeToTest1");
      writeToTest1(req, res);
    } else if (testname.equals("writeToTest2")) {
      TestUtil.logMsg("Starting writeToTest2");
      writeToTest2(req, res);
    } else if (testname.equals("getAttachmentBySwaRefTest1")) {
      TestUtil.logMsg("Starting getAttachmentBySwaRefTest1");
      getAttachmentBySwaRefTest1(req, res);
    } else if (testname.equals("getAttachmentBySwaRefTest2")) {
      TestUtil.logMsg("Starting getAttachmentBySwaRefTest2");
      getAttachmentBySwaRefTest2(req, res);
    } else if (testname.equals("getAttachmentBySwaRefTest3")) {
      TestUtil.logMsg("Starting getAttachmentBySwaRefTest3");
      getAttachmentBySwaRefTest3(req, res);
    } else if (testname.equals("getAttachmentByHrefTest1")) {
      TestUtil.logMsg("Starting getAttachmentByHrefTest1");
      getAttachmentByHrefTest1(req, res);
    } else if (testname.equals("getAttachmentByHrefTest2")) {
      TestUtil.logMsg("Starting getAttachmentByHrefTest2");
      getAttachmentByHrefTest2(req, res);
    } else {
      throw new ServletException(
          "The testname '" + testname + "' was not found in the test servlet");
    }
  }

  public void init(ServletConfig servletConfig) throws ServletException {
    super.init(servletConfig);
    System.out.println("SOAPMessageTestServet:init (Entering)");
    SOAP_Util.doServletInit(servletConfig);
    servletContext = servletConfig.getServletContext();
    System.out.println("SOAPMessageTestServet:init (Leaving)");
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

  private void addAttachmentPartTest(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("addAttachmentPartTest");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      // Add the attachment part to the message.
      TestUtil.logMsg("Add SOAP Attachment (XML document)");
      msg.removeAllAttachments();
      msg.addAttachmentPart(ap1);
      TestUtil.logMsg("Done creating message");

      TestUtil.logMsg("get all attachments");
      Iterator iterator = msg.getAttachments();

      int cnt = SOAP_Util.getIteratorCount(iterator);

      TestUtil.logMsg("number of attachments: " + cnt);

      if (cnt != 1) {
        TestUtil.logErr("only 1 attachment was added, count not correct");
        pass = false;
      }

      iterator = msg.getAttachments();

      AttachmentPart ap2 = (AttachmentPart) iterator.next();

      TestUtil.logMsg("compare attachment received is same as one added");
      if (ap1.equals(ap2)) {
        TestUtil.logMsg("Got AttachmentPart object");
      } else {
        TestUtil.logErr("AttachmentPart object mismatch");
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

  private void countAttachmentsTest(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("countAttachmentsTest");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("get count of number of attachments in message");
      int cnt = msg.countAttachments();

      TestUtil.logMsg("number of attachments: " + cnt);

      TestUtil.logMsg("compare attachment count received is as expected");
      if (cnt != 3) {
        TestUtil.logErr("attachment count expected 3, received " + cnt);
        pass = false;
      } else
        TestUtil.logMsg("attachment count was correct");

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

  private void createAttachmentPartTest1(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("createAttachmentPartTest1");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Create an empty SOAP Attachment");
      ap = msg.createAttachmentPart();

      TestUtil.logMsg("Verify attachment part is created");
      if (ap != null) {
        TestUtil.logMsg("AttachmentPart object created");
      } else {
        TestUtil.logErr("AttachmentPart object not created");
        pass = false;
      }

      if (ap != null) {
        TestUtil.logMsg("Verify attachment part is empty");
        try {
          Object o = ap.getContent();
          TestUtil.logErr("attachment has content - unexpected");
          pass = false;
        } catch (SOAPException e) {
          TestUtil.logMsg("attachment has no content - expected");
        } catch (Exception e) {
          TestUtil.logErr("expected SOAPException, received " + e);
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

  private void createAttachmentPartTest2(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("createAttachmentPartTest2");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil
          .logMsg("Create SOAP Attachment (XML document) using DataHandler");
      msg.removeAllAttachments();
      ap = msg.createAttachmentPart(new DataHandler(url1));
      ap.setContentType("text/xml");

      TestUtil.logMsg("Saving input data to buffer for comparison");
      DataHandler dh = new DataHandler(url1);
      StringBuffer sb1 = SOAP_Util.copyToBuffer(dh.getInputStream());

      TestUtil.logMsg("Verify attachment part is created");
      if (ap != null) {
        TestUtil.logMsg("AttachmentPart object created");
      } else {
        TestUtil.logErr("AttachmentPart object not created");
        pass = false;
      }

      if (ap != null) {
        TestUtil.logMsg(
            "Verify attachment part is not empty and contents are correct");
        try {
          Object o = ap.getContent();
          InputStream is = null;
          if (o == null) {
            TestUtil.logErr("getContent() returned null unexpected");
            pass = false;
          } else {
            TestUtil.logMsg("getContent() returned object=" + o);
            if (o instanceof StreamSource) {
              StreamSource ss = (StreamSource) o;
              is = ss.getInputStream();
            } else {
              TestUtil.logErr("getContent() returned unexpected object");
              TestUtil.logErr("got object: " + o
                  + ", expected object: javax.xml.transform.stream.StreamSource");
              pass = false;
            }
          }

          if (is != null) {
            StringBuffer sb2 = SOAP_Util.copyToBuffer(is);

            String s1 = sb1.toString();
            String s2 = sb2.toString();

            TestUtil.logMsg("Verifying contents ...");
            if (s1.equals(s2)) {
              TestUtil.logMsg("contents are equal - expected");
              TestUtil.logMsg(s1);
            } else {
              TestUtil.logErr("contents not equal - unexpected");
              TestUtil.logErr("expected (" + s1 + ")");
              TestUtil.logErr("received (" + s2 + ")");
              pass = false;
            }
          }
        } catch (Exception e) {
          TestUtil.logErr("attachment has no content - unexpected");
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

  private void createAttachmentPartTest3(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("createAttachmentPartTest3");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg(
          "Create SOAP Attachment (XML document) with specified data and content type");
      InputStream in = (InputStream) servletContext
          .getResourceAsStream(XML_RESOURCE_FILE);
      msg.removeAllAttachments();
      ap = msg.createAttachmentPart(new StreamSource(in), "text/xml");

      TestUtil.logMsg("Saving input data to buffer for comparison");
      in = (InputStream) servletContext.getResourceAsStream(XML_RESOURCE_FILE);
      StringBuffer sb1 = SOAP_Util.copyToBuffer(in);

      TestUtil.logMsg("Verify attachment part is created");
      if (ap != null) {
        TestUtil.logMsg("AttachmentPart object created");
      } else {
        TestUtil.logErr("AttachmentPart object not created");
        pass = false;
      }

      if (ap != null) {
        TestUtil.logMsg(
            "Verify attachment part is not empty and contents are correct");
        try {
          Object o = ap.getContent();
          InputStream is = null;
          if (o == null) {
            TestUtil.logErr("getContent() returned null unexpected");
            pass = false;
          } else {
            TestUtil.logMsg("getContent() returned object=" + o);
            if (o instanceof StreamSource) {
              StreamSource ss = (StreamSource) o;
              is = ss.getInputStream();
            } else {
              TestUtil.logErr("getContent() returned unexpected object");
              TestUtil.logErr("got object: " + o
                  + ", expected object: javax.xml.transform.stream.StreamSource");
              pass = false;
            }
          }

          if (is != null) {
            StringBuffer sb2 = SOAP_Util.copyToBuffer(is);

            String s1 = sb1.toString();
            String s2 = sb2.toString();

            TestUtil.logMsg("Verifying contents ...");
            if (s1.equals(s2)) {
              TestUtil.logMsg("contents are equal - expected");
              TestUtil.logMsg(s1);
            } else {
              TestUtil.logErr("contents not equal - unexpected");
              TestUtil.logErr("expected (" + s1 + ")");
              TestUtil.logErr("received (" + s2 + ")");
              pass = false;
            }
          }
        } catch (Exception e) {
          TestUtil.logErr("attachment has no content - unexpected");
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

  private void getAttachmentsTest1(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getAttachmentsTest1");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("get all attachments");
      Iterator iterator = msg.getAttachments();

      int cnt = SOAP_Util.getIteratorCount(iterator);

      TestUtil.logMsg("number of attachments: " + cnt);

      if (cnt != 3) {
        TestUtil.logErr("only 3 attachments was added, count not correct");
        pass = false;
      }

      iterator = msg.getAttachments();

      while (iterator.hasNext()) {
        AttachmentPart ap = (AttachmentPart) iterator.next();
        TestUtil.logMsg("compare attachment received is same as one added");
        if (ap.equals(ap1) || ap.equals(ap2) || ap.equals(ap3)) {
          TestUtil.logMsg("Got AttachmentPart object");
        } else {
          TestUtil.logErr("AttachmentPart object mismatch");
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

  private void getAttachmentsTest2(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getAttachmentsTest2");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    MimeHeaders mimeHeaders = new MimeHeaders();
    mimeHeaders.addHeader("Content-Type", "text/xml");

    try {
      setup();

      TestUtil
          .logMsg("get all attachments of MimeHeader Content-Type: text/xml");
      Iterator iterator = msg.getAttachments(mimeHeaders);

      int cnt = SOAP_Util.getIteratorCount(iterator);

      TestUtil.logMsg("number of attachments: " + cnt);

      if (cnt != 2) {
        TestUtil.logErr("only 2 attachments was added, count not correct");
        pass = false;
      }

      iterator = msg.getAttachments(mimeHeaders);

      while (iterator.hasNext()) {
        AttachmentPart ap = (AttachmentPart) iterator.next();
        TestUtil.logMsg("compare attachment received is same as one added");
        if (ap.equals(ap1) || ap.equals(ap2)) {
          TestUtil.logMsg("Got AttachmentPart object");
        } else {
          TestUtil.logErr("AttachmentPart object mismatch");
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

  private void getContentDescriptionTest(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getContentDescriptionTest");
    Properties resultProps = new Properties();
    boolean pass = true;
    String description = "This is my SOAPMessage description";

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Set content description of this SOAPMessage");
      TestUtil.logMsg("Description: " + description);
      msg.setContentDescription(description);

      TestUtil.logMsg("Verify setting of content description");
      String received = msg.getContentDescription();

      if (!received.equals(description)) {
        TestUtil.logErr("Content description mismatch: expected " + description
            + ", received " + received);
        pass = false;
      } else
        TestUtil.logMsg("Content description matches - " + description);

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

  private void getMimeHeadersTest(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getMimeHeadersTest");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("get all transport-specific MimeHeaders");
      MimeHeaders mimeHeaders = msg.getMimeHeaders();

      TestUtil.logMsg("Validating MimeHeaders object");
      if (mimeHeaders != null) {
        TestUtil.logMsg("MimeHeaders is not null - expected");
      } else {
        TestUtil.logMsg("MIMEHeaders is null - unexpected");
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

  private void removeAllAttachmentsTest(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("removeAllAttachmentsTest");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    Iterator iterator = null;

    try {
      setup();

      TestUtil.logMsg("get all attachments");
      iterator = msg.getAttachments();

      int cnt = SOAP_Util.getIteratorCount(iterator);

      TestUtil.logMsg("number of attachments: " + cnt);

      if (cnt != 3) {
        TestUtil.logErr("only 3 attachments was added, count not correct");
        pass = false;
      } else
        TestUtil.logMsg("3 attachments exist as expected");

      TestUtil.logMsg("remove all attachments");
      msg.removeAllAttachments();

      TestUtil.logMsg("get all attachments");
      iterator = msg.getAttachments();

      if (iterator.hasNext()) {
        TestUtil.logErr("attachments were not removed - unexpected");
        pass = false;
      } else
        TestUtil.logMsg("attachments were removed - expected");

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

  private void removeAttachmentsTest(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("removeAttachmentsTest");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    Iterator iterator = null;

    try {
      setup();

      TestUtil.logMsg("get all attachments");
      iterator = msg.getAttachments();

      int cnt = SOAP_Util.getIteratorCount(iterator);

      TestUtil.logMsg("number of attachments: " + cnt);

      if (cnt != 3) {
        TestUtil.logErr("only 3 attachments was added, count not correct");
        pass = false;
      } else
        TestUtil.logMsg("3 attachments exist as expected");

      TestUtil.logMsg("remove just the text/xml attachments which are 2");
      MimeHeaders mhs = new MimeHeaders();
      mhs.addHeader("Content-Type", "text/xml");
      msg.removeAttachments(mhs);

      TestUtil.logMsg("get all attachments");
      iterator = msg.getAttachments();

      cnt = SOAP_Util.getIteratorCount(iterator);

      iterator = msg.getAttachments();

      TestUtil.logMsg("number of attachments: " + cnt);

      if (cnt > 1) {
        TestUtil
            .logErr("the 2 text/xml attachments were not removed (unexpected)");
        pass = false;
      } else if (cnt == 1) {
        AttachmentPart ap = (AttachmentPart) iterator.next();
        String ctype = ap.getContentType();
        TestUtil.logMsg("Content-Type of remaining attachment is: " + ctype);
        if (ctype.equals("text/xml")) {
          TestUtil.logErr("one of the text/xml attachments was not removed");
        }
      } else {
        TestUtil.logErr("all attachments were removed (unexpected)");
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

  private void getSOAPPartTest(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("getSOAPPartTest");
    Properties resultProps = new Properties();
    boolean pass = true;
    String contentType;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Get the SOAPPart of this SOAPMessage");
      SOAPPart sp = msg.getSOAPPart();
      if (sp == null) {
        TestUtil.logErr("getSOAPPart() returned null (unexpected)");
        pass = false;
      }
      SOAP_Util.dumpSOAPMessage(msg);
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

  private void getPropertyTest(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("setPropertyTest");
    Properties resultProps = new Properties();
    boolean pass = true;
    String contentType = "text/xml";

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Set some properties for this SOAPMessage");
      TestUtil.logMsg("Setting property: MyProperty1=MyValue1");
      msg.setProperty("MyProperty1", "MyValue1");
      TestUtil.logMsg("Setting property: MyProperty2=MyValue2");
      msg.setProperty("MyProperty2", "MyValue2");

      TestUtil.logMsg("Getting the property MyProperty1");
      String value1 = (String) msg.getProperty("MyProperty1");
      TestUtil.logMsg("Getting the property MyProperty2");
      String value2 = (String) msg.getProperty("MyProperty2");

      TestUtil.logMsg("Verify that first property value is correct");
      if (value1 == null) {
        TestUtil.logErr("getProperty() returned null (unexpected)");
        pass = false;
      } else if (!value1.equals("MyValue1")) {
        TestUtil.logErr("Property value mismatch: expected MyValue1"
            + ", received " + value1);
        pass = false;
      } else
        TestUtil.logMsg("Property value matches: MyValue1");

      TestUtil.logMsg("Verify that second property value is correct");
      if (value2 == null) {
        TestUtil.logErr("getProperty() returned null (unexpected)");
        pass = false;
      } else if (!value2.equals("MyValue2")) {
        TestUtil.logErr("Property value mismatch: expected MyValue2"
            + ", received " + value2);
        pass = false;
      } else
        TestUtil.logMsg("Property value matches: MyValue2");
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

  private void setPropertyTest(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("setPropertyTest");
    Properties resultProps = new Properties();
    boolean pass = true;
    String contentType = "text/xml";

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Set some properties for this SOAPMessage");
      TestUtil.logMsg("Setting property: MyProperty1=MyValue1");
      msg.setProperty("MyProperty1", "MyValue1");
      TestUtil.logMsg("Setting property: MyProperty2=MyValue2");
      msg.setProperty("MyProperty2", "MyValue2");

      TestUtil.logMsg("Setting property again: MyProperty1=MyValue1Again");
      msg.setProperty("MyProperty1", "MyValue1Again");
      TestUtil.logMsg("Setting property again: MyProperty2=MyValue2Again");
      msg.setProperty("MyProperty2", "MyValue2Again");

      TestUtil.logMsg("Getting the property MyProperty1");
      String value1 = (String) msg.getProperty("MyProperty1");
      TestUtil.logMsg("Getting the property MyProperty2");
      String value2 = (String) msg.getProperty("MyProperty2");

      TestUtil.logMsg("Verify that first property value is correct");
      if (value1 == null) {
        TestUtil.logErr("getProperty() returned null (unexpected)");
        pass = false;
      } else if (!value1.equals("MyValue1Again")) {
        TestUtil.logErr("Property value1 mismatch: expected MyValue1Again"
            + ", received " + value1);
        pass = false;
      } else
        TestUtil.logMsg("Property value matches: MyValue1Again");

      TestUtil.logMsg("Verify that second property value is correct");
      if (value2 == null) {
        TestUtil.logErr("getProperty() returned null (unexpected)");
        pass = false;
      } else if (!value2.equals("MyValue2Again")) {
        TestUtil.logErr("Property value2 mismatch: expected MyValue2Again"
            + ", received " + value2);
        pass = false;
      } else
        TestUtil.logMsg("Property value matches: MyValue2Again");
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

  private void getSOAPBodyTest(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("getSOAPBodyTest");
    Properties resultProps = new Properties();
    boolean pass = true;
    String contentType;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Get the SOAPBody of this SOAPMessage");
      SOAPBody sb = msg.getSOAPBody();
      if (sb == null) {
        TestUtil.logErr("getSOAPBody() returned null (unexpected)");
        pass = false;
      }
      SOAP_Util.dumpSOAPMessage(msg);
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

  private void getSOAPHeaderTest(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getSOAPHeaderTest");
    Properties resultProps = new Properties();
    boolean pass = true;
    String contentType;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Get the SOAPHeader of this SOAPMessage");
      SOAPHeader sh = msg.getSOAPHeader();
      if (sh == null) {
        TestUtil.logErr("getSOAPHeader() returned null (unexpected)");
        pass = false;
      }
      SOAP_Util.dumpSOAPMessage(msg);
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

  private void saveRequiredTest1(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("saveRequiredTest1");

    TestUtil.logMsg("test for saveRequired equal to true");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      if (msg.saveRequired()) {
        TestUtil.logMsg("save required is true - expected");
      } else {
        TestUtil.logErr("save required is false - unexpected");
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

  private void saveRequiredTest2(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("saveRequiredTest2");

    TestUtil.logMsg("test for saveRequired equal to false");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Calling SOAPMessage.saveChanges()");
      msg.saveChanges();

      if (!msg.saveRequired()) {
        TestUtil.logMsg("save required is false - expected");
      } else {
        TestUtil.logErr("save required is true - unexpected");
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

  private void setContentDescriptionTest(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("setContentDescriptionTest");
    Properties resultProps = new Properties();
    boolean pass = true;
    String description = "This is my SOAPMessage description";

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Set content description of this SOAPMessage");
      TestUtil.logMsg("Description: " + description);
      msg.setContentDescription(description);

      TestUtil.logMsg("Verify setting of content description");
      String received = msg.getContentDescription();

      if (!received.equals(description)) {
        TestUtil.logErr("Content description mismatch: expected " + description
            + ", received " + received);
        pass = false;
      } else
        TestUtil.logMsg("Content description matches - " + description);

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

  private void writeToTest1(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("writeToTest1");

    TestUtil.logMsg("write SOAPMessage without attachments to OutputStream");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    try {
      setup();

      TestUtil.logMsg("write SOAPMessage object to output stream");
      msg.removeAllAttachments();
      msg.writeTo(baos);
      TestUtil.logMsg("SOAPMessage written, mesage is: ");
      TestUtil.logMsg(baos.toString());

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

  private void writeToTest2(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("writeToTest2");
    TestUtil.logMsg("write SOAPMessage with attachments to OutputStream");

    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    try {
      setup();

      TestUtil.logMsg("Create SOAP Attachment (XML document)");
      msg.removeAllAttachments();
      ap = msg.createAttachmentPart(new DataHandler(url1));
      ap.setContentType("text/xml");

      // Add the attachment part to the message.
      TestUtil.logMsg("Add SOAP Attachment (XML document)");
      msg.addAttachmentPart(ap);

      TestUtil.logMsg("write SOAPMessage object to output stream");
      msg.writeTo(baos);
      TestUtil.logMsg("SOAPMessage written, mesage is: ");
      TestUtil.logMsg(baos.toString());

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

  private void getAttachmentBySwaRefTest1(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();
    String NS_PREFIX = "mypre";
    String NS_URI = "http://myuri.org/";

    try {
      TestUtil.logMsg("Create SOAP message from message factory");
      SOAPMessage msg = SOAP_Util.getMessageFactory().createMessage();

      // Message creation takes care of creating the SOAPPart - a
      // required part of the message as per the SOAP 1.1 spec.
      TestUtil.logMsg("Get SOAP Part");
      SOAPPart sp = msg.getSOAPPart();

      // Retrieve the envelope from the soap part to start building
      // the soap message.
      TestUtil.logMsg("Get SOAP Envelope");
      SOAPEnvelope envelope = sp.getEnvelope();

      // Create a soap body from the envelope.
      TestUtil.logMsg("Create SOAP Body");
      SOAPBody bdy = envelope.getBody();

      // Add a soap body element
      TestUtil.logMsg("Add SOAP BodyElement Body1");
      SOAPBodyElement sbe1 = bdy
          .addBodyElement(envelope.createName("Body1", NS_PREFIX, NS_URI));

      // Add text node to reference the GIF attachment
      sbe1.addTextNode("cid:THEGIF");

      // Add another soap body element
      TestUtil.logMsg("Add SOAP BodyElement Body2");
      SOAPBodyElement sbe2 = bdy
          .addBodyElement(envelope.createName("Body2", NS_PREFIX, NS_URI));

      // Add text node to reference the XML attachment
      sbe2.addTextNode("cid:THEXML");

      TestUtil.logMsg("Add various mime type attachments to SOAP message");
      url1 = tsurl.getURL(PROTOCOL, SOAP_Util.getHostname(),
          SOAP_Util.getPortnum(), cntxroot + XML_RESOURCE_FILE);
      url2 = tsurl.getURL(PROTOCOL, SOAP_Util.getHostname(),
          SOAP_Util.getPortnum(), cntxroot + GIF_RESOURCE_FILE);
      url3 = tsurl.getURL(PROTOCOL, SOAP_Util.getHostname(),
          SOAP_Util.getPortnum(), cntxroot + TXT_RESOURCE_FILE);
      url4 = tsurl.getURL(PROTOCOL, SOAP_Util.getHostname(),
          SOAP_Util.getPortnum(), cntxroot + HTML_RESOURCE_FILE);
      url5 = tsurl.getURL(PROTOCOL, SOAP_Util.getHostname(),
          SOAP_Util.getPortnum(), cntxroot + JPEG_RESOURCE_FILE);

      TestUtil.logMsg("Create SOAP Attachment (XML document)");
      TestUtil.logMsg("URL1=" + url1);
      AttachmentPart ap1 = msg.createAttachmentPart(new DataHandler(url1));

      TestUtil.logMsg("Create SOAP Attachment (GIF image)");
      TestUtil.logMsg("URL2=" + url2);
      AttachmentPart ap2 = msg.createAttachmentPart(new DataHandler(url2));

      TestUtil.logMsg("Create SOAP Attachment (Plain text)");
      TestUtil.logMsg("URL3=" + url3);
      AttachmentPart ap3 = msg.createAttachmentPart(new DataHandler(url3));

      TestUtil.logMsg("Create SOAP Attachment (HTML document)");
      TestUtil.logMsg("URL4=" + url4);
      AttachmentPart ap4 = msg.createAttachmentPart(new DataHandler(url4));

      TestUtil.logMsg("Create SOAP Attachment (JPEG image)");
      TestUtil.logMsg("URL5=" + url5);
      AttachmentPart ap5 = msg.createAttachmentPart(new DataHandler(url5));

      ap1.setContentType("text/xml");
      ap1.setContentId("<THEXML>");
      ap2.setContentType("image/gif");
      ap2.setContentId("<THEGIF>");
      ap3.setContentType("text/plain");
      ap3.setContentId("<THEPLAIN>");
      ap4.setContentType("text/html");
      ap4.setContentId("<THEHTML>");
      ap5.setContentType("image/jpeg");
      ap5.setContentId("<THEJPEG>");

      // Add the attachments to the message.
      TestUtil.logMsg("Add SOAP Attachment (XML document) to SOAP message");
      msg.addAttachmentPart(ap1);
      TestUtil.logMsg("Add SOAP Attachment (GIF image) to SOAP message");
      msg.addAttachmentPart(ap2);
      TestUtil.logMsg("Add SOAP Attachment (Plain text) to SOAP message");
      msg.addAttachmentPart(ap3);
      TestUtil.logMsg("Add SOAP Attachment (HTML document) to SOAP message");
      msg.addAttachmentPart(ap4);
      TestUtil.logMsg("Add SOAP Attachment (JPEG image) to SOAP message");
      msg.addAttachmentPart(ap5);
      msg.saveChanges();
      TestUtil.logMsg("Done creating SOAP message");

      TestUtil.logMsg("Retrieve attachment with swaref=cid:THEGIF");
      AttachmentPart myap = msg.getAttachment(sbe1);
      if (myap == null) {
        TestUtil.logErr("Returned null (unexpected)");
        pass = false;
      } else if (!myap.getContentType().equals("image/gif")) {
        TestUtil.logErr("Wrong attachment was returned: Got Content-Type of "
            + myap.getContentType() + ", Expected Content-Type of image/gif");
        pass = false;
      } else
        TestUtil.logMsg("Correct attachment was returned");

      TestUtil.logMsg("Retrieve attachment with swaref=cid:THEXML");
      myap = msg.getAttachment(sbe2);
      if (myap == null) {
        TestUtil.logErr("Returned null (unexpected)");
        pass = false;
      } else if (!myap.getContentType().equals("text/xml")) {
        TestUtil.logErr("Wrong attachment was returned: Got Content-Type of "
            + myap.getContentType() + ", Expected Content-Type of text/xml");
        pass = false;
      } else
        TestUtil.logMsg("Correct attachment was returned");

      TestUtil
          .logMsg("Retrieve attachment with swaref=cid:boo-hoo (expect null)");
      QName myqname = new QName("boo-hoo");
      SOAPElement myse = SOAP_Util.getSOAPFactory().createElement(myqname);
      myse.addTextNode("<theBooHooAttachment href=\"cid:boo-hoo\"/>");
      myap = msg.getAttachment(myse);
      if (myap == null)
        TestUtil.logMsg("Returned null (expected)");
      else {
        TestUtil.logErr("Returned non null (unexpected)");
        pass = false;
      }

      // Set status code to OK
      res.setStatus(HttpServletResponse.SC_OK);
    } catch (Exception e) {
      TestUtil.logErr("SendVariousMimeAttachmentsTest Exception: " + e);
      TestUtil.printStackTrace(e);
      System.err.println("SendVariousMimeAttachmentsTest Exception: " + e);
      e.printStackTrace(System.err);
      // Set status code to INTERNAL SERVER ERROR
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      pass = false;
    }
    // Send response object and test result back to client
    if (pass)
      resultProps.setProperty("TESTRESULT", "pass");
    else
      resultProps.setProperty("TESTRESULT", "fail");
    TestUtil.logMsg("TESTRESULT=" + resultProps.getProperty("TESTRESULT"));
    resultProps.list(out);
  }

  private void getAttachmentBySwaRefTest2(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();
    String NS_PREFIX = "mypre";
    String NS_URI = "http://myuri.org/";

    try {
      TestUtil.logMsg("Create SOAP message from message factory");
      SOAPMessage msg = SOAP_Util.getMessageFactory().createMessage();

      // Message creation takes care of creating the SOAPPart - a
      // required part of the message as per the SOAP 1.1 spec.
      TestUtil.logMsg("Get SOAP Part");
      SOAPPart sp = msg.getSOAPPart();

      // Retrieve the envelope from the soap part to start building
      // the soap message.
      TestUtil.logMsg("Get SOAP Envelope");
      SOAPEnvelope envelope = sp.getEnvelope();

      // Create a soap body from the envelope.
      TestUtil.logMsg("Create SOAP Body");
      SOAPBody bdy = envelope.getBody();

      // Add a soap body element
      TestUtil.logMsg("Add SOAP BodyElement Body1");
      SOAPBodyElement sbe1 = bdy
          .addBodyElement(envelope.createName("Body1", NS_PREFIX, NS_URI));

      // Add a child element
      TestUtil.logMsg("Add ChildElement TheGifAttachment");
      SOAPElement cse1 = sbe1.addChildElement(
          envelope.createName("TheGifAttachment", NS_PREFIX, NS_URI));
      // Add text node to reference the GIF attachment
      cse1.addTextNode("cid:THEGIF");

      // Add another soap body element
      TestUtil.logMsg("Add SOAP BodyElement Body2");
      SOAPBodyElement sbe2 = bdy
          .addBodyElement(envelope.createName("Body2", NS_PREFIX, NS_URI));

      // Add a child element
      TestUtil.logMsg("Add ChildElement TheXmlAttachment");
      SOAPElement cse2 = sbe2.addChildElement(
          envelope.createName("TheXmlAttachment", NS_PREFIX, NS_URI));
      cse2.addTextNode("cid:THEXML");

      TestUtil.logMsg("Add various mime type attachments to SOAP message");
      url1 = tsurl.getURL(PROTOCOL, SOAP_Util.getHostname(),
          SOAP_Util.getPortnum(), cntxroot + XML_RESOURCE_FILE);
      url2 = tsurl.getURL(PROTOCOL, SOAP_Util.getHostname(),
          SOAP_Util.getPortnum(), cntxroot + GIF_RESOURCE_FILE);
      url3 = tsurl.getURL(PROTOCOL, SOAP_Util.getHostname(),
          SOAP_Util.getPortnum(), cntxroot + TXT_RESOURCE_FILE);
      url4 = tsurl.getURL(PROTOCOL, SOAP_Util.getHostname(),
          SOAP_Util.getPortnum(), cntxroot + HTML_RESOURCE_FILE);
      url5 = tsurl.getURL(PROTOCOL, SOAP_Util.getHostname(),
          SOAP_Util.getPortnum(), cntxroot + JPEG_RESOURCE_FILE);

      TestUtil.logMsg("Create SOAP Attachment (XML document)");
      TestUtil.logMsg("URL1=" + url1);
      AttachmentPart ap1 = msg.createAttachmentPart(new DataHandler(url1));

      TestUtil.logMsg("Create SOAP Attachment (GIF image)");
      TestUtil.logMsg("URL2=" + url2);
      AttachmentPart ap2 = msg.createAttachmentPart(new DataHandler(url2));

      TestUtil.logMsg("Create SOAP Attachment (Plain text)");
      TestUtil.logMsg("URL3=" + url3);
      AttachmentPart ap3 = msg.createAttachmentPart(new DataHandler(url3));

      TestUtil.logMsg("Create SOAP Attachment (HTML document)");
      TestUtil.logMsg("URL4=" + url4);
      AttachmentPart ap4 = msg.createAttachmentPart(new DataHandler(url4));

      TestUtil.logMsg("Create SOAP Attachment (JPEG image)");
      TestUtil.logMsg("URL5=" + url5);
      AttachmentPart ap5 = msg.createAttachmentPart(new DataHandler(url5));

      ap1.setContentType("text/xml");
      ap1.setContentId("<THEXML>");
      ap2.setContentType("image/gif");
      ap2.setContentId("<THEGIF>");
      ap3.setContentType("text/plain");
      ap3.setContentId("<THEPLAIN>");
      ap4.setContentType("text/html");
      ap4.setContentId("<THEHTML>");
      ap5.setContentType("image/jpeg");
      ap5.setContentId("<THEJPEG>");

      // Add the attachments to the message.
      TestUtil.logMsg("Add SOAP Attachment (XML document) to SOAP message");
      msg.addAttachmentPart(ap1);
      TestUtil.logMsg("Add SOAP Attachment (GIF image) to SOAP message");
      msg.addAttachmentPart(ap2);
      TestUtil.logMsg("Add SOAP Attachment (Plain text) to SOAP message");
      msg.addAttachmentPart(ap3);
      TestUtil.logMsg("Add SOAP Attachment (HTML document) to SOAP message");
      msg.addAttachmentPart(ap4);
      TestUtil.logMsg("Add SOAP Attachment (JPEG image) to SOAP message");
      msg.addAttachmentPart(ap5);
      msg.saveChanges();
      TestUtil.logMsg("Done creating SOAP message");

      TestUtil.logMsg("Retrieve attachment with swaref=cid:THEGIF");
      AttachmentPart myap = msg.getAttachment(cse1);
      if (myap == null) {
        TestUtil.logErr("Returned null (unexpected)");
        pass = false;
      } else if (!myap.getContentType().equals("image/gif")) {
        TestUtil.logErr("Wrong attachment was returned: Got Content-Type of "
            + myap.getContentType() + ", Expected Content-Type of image/gif");
        pass = false;
      } else
        TestUtil.logMsg("Correct attachment was returned");

      TestUtil.logMsg("Retrieve attachment with swaref=cid:THEXML");
      myap = msg.getAttachment(cse2);
      if (myap == null) {
        TestUtil.logErr("Returned null (unexpected)");
        pass = false;
      } else if (!myap.getContentType().equals("text/xml")) {
        TestUtil.logErr("Wrong attachment was returned: Got Content-Type of "
            + myap.getContentType() + ", Expected Content-Type of text/xml");
        pass = false;
      } else
        TestUtil.logMsg("Correct attachment was returned");

      TestUtil
          .logMsg("Retrieve attachment with swaref=cid:boo-hoo (expect null)");
      QName myqname = new QName("boo-hoo");
      SOAPElement myse = SOAP_Util.getSOAPFactory().createElement(myqname);
      myse.addTextNode("<theBooHooAttachment href=\"cid:boo-hoo\"/>");
      myap = msg.getAttachment(myse);
      if (myap == null)
        TestUtil.logMsg("Returned null (expected)");
      else {
        TestUtil.logErr("Returned non null (unexpected)");
        pass = false;
      }

      // Set status code to OK
      res.setStatus(HttpServletResponse.SC_OK);
    } catch (Exception e) {
      TestUtil.logErr("SendVariousMimeAttachmentsTest Exception: " + e);
      TestUtil.printStackTrace(e);
      System.err.println("SendVariousMimeAttachmentsTest Exception: " + e);
      e.printStackTrace(System.err);
      // Set status code to INTERNAL SERVER ERROR
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      pass = false;
    }
    // Send response object and test result back to client
    if (pass)
      resultProps.setProperty("TESTRESULT", "pass");
    else
      resultProps.setProperty("TESTRESULT", "fail");
    TestUtil.logMsg("TESTRESULT=" + resultProps.getProperty("TESTRESULT"));
    resultProps.list(out);
  }

  private void getAttachmentBySwaRefTest3(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();
    String NS_PREFIX = "mypre";
    String NS_URI = "http://myuri.org/";

    try {
      TestUtil.logMsg("Create SOAP message from message factory");
      SOAPMessage msg = SOAP_Util.getMessageFactory().createMessage();

      // Message creation takes care of creating the SOAPPart - a
      // required part of the message as per the SOAP 1.1 spec.
      TestUtil.logMsg("Get SOAP Part");
      SOAPPart sp = msg.getSOAPPart();

      // Retrieve the envelope from the soap part to start building
      // the soap message.
      TestUtil.logMsg("Get SOAP Envelope");
      SOAPEnvelope envelope = sp.getEnvelope();

      // Create a soap body from the envelope.
      TestUtil.logMsg("Create SOAP Body");
      SOAPBody bdy = envelope.getBody();

      // Add a soap body element
      TestUtil.logMsg("Add SOAP BodyElement Body1");
      SOAPBodyElement sbe1 = bdy
          .addBodyElement(envelope.createName("Body1", NS_PREFIX, NS_URI));

      // Add another soap body element
      TestUtil.logMsg("Add SOAP BodyElement Body2");
      SOAPBodyElement sbe2 = bdy
          .addBodyElement(envelope.createName("Body2", NS_PREFIX, NS_URI));

      TestUtil.logMsg("Add various mime type attachments to SOAP message");
      url1 = tsurl.getURL(PROTOCOL, SOAP_Util.getHostname(),
          SOAP_Util.getPortnum(), cntxroot + XML_RESOURCE_FILE);
      url2 = tsurl.getURL(PROTOCOL, SOAP_Util.getHostname(),
          SOAP_Util.getPortnum(), cntxroot + GIF_RESOURCE_FILE);
      url3 = tsurl.getURL(PROTOCOL, SOAP_Util.getHostname(),
          SOAP_Util.getPortnum(), cntxroot + TXT_RESOURCE_FILE);
      url4 = tsurl.getURL(PROTOCOL, SOAP_Util.getHostname(),
          SOAP_Util.getPortnum(), cntxroot + HTML_RESOURCE_FILE);
      url5 = tsurl.getURL(PROTOCOL, SOAP_Util.getHostname(),
          SOAP_Util.getPortnum(), cntxroot + JPEG_RESOURCE_FILE);

      // Add text node to reference the GIF attachment using Content-Location
      sbe1.addTextNode(url2.toString());
      // Add text node to reference the XML attachment
      sbe2.addTextNode(url1.toString());

      TestUtil.logMsg("Create SOAP Attachment (XML document)");
      TestUtil.logMsg("URL1=" + url1);
      AttachmentPart ap1 = msg.createAttachmentPart(new DataHandler(url1));

      TestUtil.logMsg("Create SOAP Attachment (GIF image)");
      TestUtil.logMsg("URL2=" + url2);
      AttachmentPart ap2 = msg.createAttachmentPart(new DataHandler(url2));

      TestUtil.logMsg("Create SOAP Attachment (Plain text)");
      TestUtil.logMsg("URL3=" + url3);
      AttachmentPart ap3 = msg.createAttachmentPart(new DataHandler(url3));

      TestUtil.logMsg("Create SOAP Attachment (HTML document)");
      TestUtil.logMsg("URL4=" + url4);
      AttachmentPart ap4 = msg.createAttachmentPart(new DataHandler(url4));

      TestUtil.logMsg("Create SOAP Attachment (JPEG image)");
      TestUtil.logMsg("URL5=" + url5);
      AttachmentPart ap5 = msg.createAttachmentPart(new DataHandler(url5));

      ap1.setContentType("text/xml");
      ap1.setContentId("<THEXML>");
      ap1.setContentLocation(url1.toString());
      ap2.setContentType("image/gif");
      ap2.setContentId("<THEGIF>");
      ap2.setContentLocation(url2.toString());
      ap3.setContentType("text/plain");
      ap3.setContentId("<THEPLAIN>");
      ap3.setContentLocation(url3.toString());
      ap4.setContentType("text/html");
      ap4.setContentId("<THEHTML>");
      ap4.setContentLocation(url4.toString());
      ap5.setContentType("image/jpeg");
      ap5.setContentId("<THEJPEG>");
      ap5.setContentLocation(url5.toString());

      // Add the attachments to the message.
      TestUtil.logMsg("Add SOAP Attachment (XML document) to SOAP message");
      msg.addAttachmentPart(ap1);
      TestUtil.logMsg("Add SOAP Attachment (GIF image) to SOAP message");
      msg.addAttachmentPart(ap2);
      TestUtil.logMsg("Add SOAP Attachment (Plain text) to SOAP message");
      msg.addAttachmentPart(ap3);
      TestUtil.logMsg("Add SOAP Attachment (HTML document) to SOAP message");
      msg.addAttachmentPart(ap4);
      TestUtil.logMsg("Add SOAP Attachment (JPEG image) to SOAP message");
      msg.addAttachmentPart(ap5);
      msg.saveChanges();
      TestUtil.logMsg("Done creating SOAP message");

      TestUtil.logMsg("Retrieve attachment with swaref=THEGIF");
      AttachmentPart myap = msg.getAttachment(sbe1);
      if (myap == null) {
        TestUtil.logErr("Returned null (unexpected)");
        pass = false;
      } else if (!myap.getContentType().equals("image/gif")) {
        TestUtil.logErr("Wrong attachment was returned: Got Content-Type of "
            + myap.getContentType() + ", Expected Content-Type of image/gif");
        pass = false;
      } else
        TestUtil.logMsg("Correct attachment was returned");

      TestUtil.logMsg("Retrieve attachment with swaref=THEXML");
      myap = msg.getAttachment(sbe2);
      if (myap == null) {
        TestUtil.logErr("Returned null (unexpected)");
        pass = false;
      } else if (!myap.getContentType().equals("text/xml")) {
        TestUtil.logErr("Wrong attachment was returned: Got Content-Type of "
            + myap.getContentType() + ", Expected Content-Type of text/xml");
        pass = false;
      } else
        TestUtil.logMsg("Correct attachment was returned");

      TestUtil.logMsg("Retrieve attachment with swaref=boo-hoo (expect null)");
      QName myqname = new QName("boo-hoo");
      SOAPElement myse = SOAP_Util.getSOAPFactory().createElement(myqname);
      myse.addTextNode("<theBooHooAttachment href=\"boo-hoo\"/>");
      myap = msg.getAttachment(myse);
      if (myap == null)
        TestUtil.logMsg("Returned null (expected)");
      else {
        TestUtil.logErr("Returned non null (unexpected)");
        pass = false;
      }

      // Set status code to OK
      res.setStatus(HttpServletResponse.SC_OK);
    } catch (Exception e) {
      TestUtil.logErr("SendVariousMimeAttachmentsTest Exception: " + e);
      TestUtil.printStackTrace(e);
      System.err.println("SendVariousMimeAttachmentsTest Exception: " + e);
      e.printStackTrace(System.err);
      // Set status code to INTERNAL SERVER ERROR
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      pass = false;
    }
    // Send response object and test result back to client
    if (pass)
      resultProps.setProperty("TESTRESULT", "pass");
    else
      resultProps.setProperty("TESTRESULT", "fail");
    TestUtil.logMsg("TESTRESULT=" + resultProps.getProperty("TESTRESULT"));
    resultProps.list(out);
  }

  private void getAttachmentByHrefTest1(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();
    String NS_PREFIX = "mypre";
    String NS_URI = "http://myuri.org/";

    try {
      TestUtil.logMsg("Create SOAP message from message factory");
      SOAPMessage msg = SOAP_Util.getMessageFactory().createMessage();

      // Message creation takes care of creating the SOAPPart - a
      // required part of the message as per the SOAP 1.1 spec.
      TestUtil.logMsg("Get SOAP Part");
      SOAPPart sp = msg.getSOAPPart();

      // Retrieve the envelope from the soap part to start building
      // the soap message.
      TestUtil.logMsg("Get SOAP Envelope");
      SOAPEnvelope envelope = sp.getEnvelope();

      // Create a soap body from the envelope.
      TestUtil.logMsg("Create SOAP Body");
      SOAPBody bdy = envelope.getBody();

      // Add a soap body element
      TestUtil.logMsg("Add SOAP BodyElement Body1");
      SOAPBodyElement sbe1 = bdy
          .addBodyElement(envelope.createName("Body1", NS_PREFIX, NS_URI));

      // Add a child element
      TestUtil.logMsg("Add ChildElement TheGifAttachment");
      sbe1.addChildElement(
          envelope.createName("TheGifAttachment", NS_PREFIX, NS_URI));
      sbe1.setAttribute("href", "cid:THEGIF");

      // Add another soap body element
      TestUtil.logMsg("Add SOAP BodyElement Body2");
      SOAPBodyElement sbe2 = bdy
          .addBodyElement(envelope.createName("Body2", NS_PREFIX, NS_URI));

      // Add a child element
      TestUtil.logMsg("Add ChildElement TheXmlAttachment");
      sbe2.addChildElement(
          envelope.createName("TheXmlAttachment", NS_PREFIX, NS_URI));
      sbe2.setAttribute("href", "cid:THEXML");

      TestUtil.logMsg("Add various mime type attachments to SOAP message");
      url1 = tsurl.getURL(PROTOCOL, SOAP_Util.getHostname(),
          SOAP_Util.getPortnum(), cntxroot + XML_RESOURCE_FILE);
      url2 = tsurl.getURL(PROTOCOL, SOAP_Util.getHostname(),
          SOAP_Util.getPortnum(), cntxroot + GIF_RESOURCE_FILE);
      url3 = tsurl.getURL(PROTOCOL, SOAP_Util.getHostname(),
          SOAP_Util.getPortnum(), cntxroot + TXT_RESOURCE_FILE);
      url4 = tsurl.getURL(PROTOCOL, SOAP_Util.getHostname(),
          SOAP_Util.getPortnum(), cntxroot + HTML_RESOURCE_FILE);
      url5 = tsurl.getURL(PROTOCOL, SOAP_Util.getHostname(),
          SOAP_Util.getPortnum(), cntxroot + JPEG_RESOURCE_FILE);

      TestUtil.logMsg("Create SOAP Attachment (XML document)");
      TestUtil.logMsg("URL1=" + url1);
      AttachmentPart ap1 = msg.createAttachmentPart(new DataHandler(url1));

      TestUtil.logMsg("Create SOAP Attachment (GIF image)");
      TestUtil.logMsg("URL2=" + url2);
      AttachmentPart ap2 = msg.createAttachmentPart(new DataHandler(url2));

      TestUtil.logMsg("Create SOAP Attachment (Plain text)");
      TestUtil.logMsg("URL3=" + url3);
      AttachmentPart ap3 = msg.createAttachmentPart(new DataHandler(url3));

      TestUtil.logMsg("Create SOAP Attachment (HTML document)");
      TestUtil.logMsg("URL4=" + url4);
      AttachmentPart ap4 = msg.createAttachmentPart(new DataHandler(url4));

      TestUtil.logMsg("Create SOAP Attachment (JPEG image)");
      TestUtil.logMsg("URL5=" + url5);
      AttachmentPart ap5 = msg.createAttachmentPart(new DataHandler(url5));

      ap1.setContentType("text/xml");
      ap1.setContentId("<THEXML>");
      ap2.setContentType("image/gif");
      ap2.setContentId("<THEGIF>");
      ap3.setContentType("text/plain");
      ap3.setContentId("<THEPLAIN>");
      ap4.setContentType("text/html");
      ap4.setContentId("<THEHTML>");
      ap5.setContentType("image/jpeg");
      ap5.setContentId("<THEJPEG>");

      // Add the attachments to the message.
      TestUtil.logMsg("Add SOAP Attachment (XML document) to SOAP message");
      msg.addAttachmentPart(ap1);
      TestUtil.logMsg("Add SOAP Attachment (GIF image) to SOAP message");
      msg.addAttachmentPart(ap2);
      TestUtil.logMsg("Add SOAP Attachment (Plain text) to SOAP message");
      msg.addAttachmentPart(ap3);
      TestUtil.logMsg("Add SOAP Attachment (HTML document) to SOAP message");
      msg.addAttachmentPart(ap4);
      TestUtil.logMsg("Add SOAP Attachment (JPEG image) to SOAP message");
      msg.addAttachmentPart(ap5);
      msg.saveChanges();
      TestUtil.logMsg("Done creating SOAP message");

      TestUtil.logMsg("Retrieve attachment with href=cid:THEGIF");
      AttachmentPart myap = msg.getAttachment(sbe1);
      if (myap == null) {
        TestUtil.logErr("Returned null (unexpected)");
        pass = false;
      } else if (!myap.getContentType().equals("image/gif")) {
        TestUtil.logErr("Wrong attachment was returned: Got Content-Type of "
            + myap.getContentType() + ", Expected Content-Type of image/gif");
        pass = false;
      } else
        TestUtil.logMsg("Correct attachment was returned");

      TestUtil.logMsg("Retrieve attachment with href=cid:THEXML");
      myap = msg.getAttachment(sbe2);
      if (myap == null) {
        TestUtil.logErr("Returned null (unexpected)");
        pass = false;
      } else if (!myap.getContentType().equals("text/xml")) {
        TestUtil.logErr("Wrong attachment was returned: Got Content-Type of "
            + myap.getContentType() + ", Expected Content-Type of text/xml");
        pass = false;
      } else
        TestUtil.logMsg("Correct attachment was returned");

      TestUtil
          .logMsg("Retrieve attachment with href=cid:boo-hoo (expect null)");
      QName myqname = new QName("boo-hoo");
      SOAPElement myse = SOAP_Util.getSOAPFactory().createElement(myqname);
      myse.addTextNode("<theBooHooAttachment href=\"cid:boo-hoo\"/>");
      myap = msg.getAttachment(myse);
      if (myap == null)
        TestUtil.logMsg("Returned null (expected)");
      else {
        TestUtil.logErr("Returned non null (unexpected)");
        pass = false;
      }

      // Set status code to OK
      res.setStatus(HttpServletResponse.SC_OK);
    } catch (Exception e) {
      TestUtil.logErr("SendVariousMimeAttachmentsTest Exception: " + e);
      TestUtil.printStackTrace(e);
      System.err.println("SendVariousMimeAttachmentsTest Exception: " + e);
      e.printStackTrace(System.err);
      // Set status code to INTERNAL SERVER ERROR
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      pass = false;
    }
    // Send response object and test result back to client
    if (pass)
      resultProps.setProperty("TESTRESULT", "pass");
    else
      resultProps.setProperty("TESTRESULT", "fail");
    TestUtil.logMsg("TESTRESULT=" + resultProps.getProperty("TESTRESULT"));
    resultProps.list(out);
  }

  private void getAttachmentByHrefTest2(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();
    String NS_PREFIX = "mypre";
    String NS_URI = "http://myuri.org/";

    try {
      TestUtil.logMsg("Create SOAP message from message factory");
      SOAPMessage msg = SOAP_Util.getMessageFactory().createMessage();

      // Message creation takes care of creating the SOAPPart - a
      // required part of the message as per the SOAP 1.1 spec.
      TestUtil.logMsg("Get SOAP Part");
      SOAPPart sp = msg.getSOAPPart();

      // Retrieve the envelope from the soap part to start building
      // the soap message.
      TestUtil.logMsg("Get SOAP Envelope");
      SOAPEnvelope envelope = sp.getEnvelope();

      // Create a soap body from the envelope.
      TestUtil.logMsg("Create SOAP Body");
      SOAPBody bdy = envelope.getBody();

      // Add a soap body element
      TestUtil.logMsg("Add SOAP BodyElement Body1");
      SOAPBodyElement sbe1 = bdy
          .addBodyElement(envelope.createName("Body1", NS_PREFIX, NS_URI));

      // Add a child element
      TestUtil.logMsg("Add ChildElement TheGifAttachment");
      sbe1.addChildElement(
          envelope.createName("TheGifAttachment", NS_PREFIX, NS_URI));

      // Add another soap body element
      TestUtil.logMsg("Add SOAP BodyElement Body2");
      SOAPBodyElement sbe2 = bdy
          .addBodyElement(envelope.createName("Body2", NS_PREFIX, NS_URI));

      // Add a child element
      TestUtil.logMsg("Add ChildElement TheXmlAttachment");
      sbe2.addChildElement(
          envelope.createName("TheXmlAttachment", NS_PREFIX, NS_URI));

      TestUtil.logMsg("Add various mime type attachments to SOAP message");
      url1 = tsurl.getURL(PROTOCOL, SOAP_Util.getHostname(),
          SOAP_Util.getPortnum(), cntxroot + XML_RESOURCE_FILE);
      url2 = tsurl.getURL(PROTOCOL, SOAP_Util.getHostname(),
          SOAP_Util.getPortnum(), cntxroot + GIF_RESOURCE_FILE);
      url3 = tsurl.getURL(PROTOCOL, SOAP_Util.getHostname(),
          SOAP_Util.getPortnum(), cntxroot + TXT_RESOURCE_FILE);
      url4 = tsurl.getURL(PROTOCOL, SOAP_Util.getHostname(),
          SOAP_Util.getPortnum(), cntxroot + HTML_RESOURCE_FILE);
      url5 = tsurl.getURL(PROTOCOL, SOAP_Util.getHostname(),
          SOAP_Util.getPortnum(), cntxroot + JPEG_RESOURCE_FILE);

      // Set href on body elements using Content-Location headers and relative
      // URI's
      sbe1.setAttribute("href", url2.toString());
      sbe2.setAttribute("href", url1.toString());

      TestUtil.logMsg("Create SOAP Attachment (XML document)");
      TestUtil.logMsg("URL1=" + url1);
      AttachmentPart ap1 = msg.createAttachmentPart(new DataHandler(url1));

      TestUtil.logMsg("Create SOAP Attachment (GIF image)");
      TestUtil.logMsg("URL2=" + url2);
      AttachmentPart ap2 = msg.createAttachmentPart(new DataHandler(url2));

      TestUtil.logMsg("Create SOAP Attachment (Plain text)");
      TestUtil.logMsg("URL3=" + url3);
      AttachmentPart ap3 = msg.createAttachmentPart(new DataHandler(url3));

      TestUtil.logMsg("Create SOAP Attachment (HTML document)");
      TestUtil.logMsg("URL4=" + url4);
      AttachmentPart ap4 = msg.createAttachmentPart(new DataHandler(url4));

      TestUtil.logMsg("Create SOAP Attachment (JPEG image)");
      TestUtil.logMsg("URL5=" + url5);
      AttachmentPart ap5 = msg.createAttachmentPart(new DataHandler(url5));

      ap1.setContentType("text/xml");
      ap1.setContentId("<THEXML>");
      ap1.setContentLocation(url1.toString());
      ap2.setContentType("image/gif");
      ap2.setContentId("<THEGIF>");
      ap2.setContentLocation(url2.toString());
      ap3.setContentType("text/plain");
      ap3.setContentId("<THEPLAIN>");
      ap3.setContentLocation(url3.toString());
      ap4.setContentType("text/html");
      ap4.setContentId("<THEHTML>");
      ap4.setContentLocation(url4.toString());
      ap5.setContentType("image/jpeg");
      ap5.setContentId("<THEJPEG>");
      ap5.setContentLocation(url5.toString());

      // Add the attachments to the message.
      TestUtil.logMsg("Add SOAP Attachment (XML document) to SOAP message");
      msg.addAttachmentPart(ap1);
      TestUtil.logMsg("Add SOAP Attachment (GIF image) to SOAP message");
      msg.addAttachmentPart(ap2);
      TestUtil.logMsg("Add SOAP Attachment (Plain text) to SOAP message");
      msg.addAttachmentPart(ap3);
      TestUtil.logMsg("Add SOAP Attachment (HTML document) to SOAP message");
      msg.addAttachmentPart(ap4);
      TestUtil.logMsg("Add SOAP Attachment (JPEG image) to SOAP message");
      msg.addAttachmentPart(ap5);
      msg.saveChanges();
      TestUtil.logMsg("Done creating SOAP message");

      TestUtil.logMsg("Retrieve attachment with href=THEGIF");
      AttachmentPart myap = msg.getAttachment(sbe1);
      if (myap == null) {
        TestUtil.logErr("Returned null (unexpected)");
        pass = false;
      } else if (!myap.getContentType().equals("image/gif")) {
        TestUtil.logErr("Wrong attachment was returned: Got Content-Type of "
            + myap.getContentType() + ", Expected Content-Type of image/gif");
        pass = false;
      } else
        TestUtil.logMsg("Correct attachment was returned");

      TestUtil.logMsg("Retrieve attachment with href=THEXML");
      myap = msg.getAttachment(sbe2);
      if (myap == null) {
        TestUtil.logErr("Returned null (unexpected)");
        pass = false;
      } else if (!myap.getContentType().equals("text/xml")) {
        TestUtil.logErr("Wrong attachment was returned: Got Content-Type of "
            + myap.getContentType() + ", Expected Content-Type of text/xml");
        pass = false;
      } else
        TestUtil.logMsg("Correct attachment was returned");

      TestUtil.logMsg("Retrieve attachment with href=boo-hoo (expect null)");
      QName myqname = new QName("boo-hoo");
      SOAPElement myse = SOAP_Util.getSOAPFactory().createElement(myqname);
      myse.addTextNode("<theBooHooAttachment href=\"boo-hoo\"/>");
      myap = msg.getAttachment(myse);
      if (myap == null)
        TestUtil.logMsg("Returned null (expected)");
      else {
        TestUtil.logErr("Returned non null (unexpected)");
        pass = false;
      }

      // Set status code to OK
      res.setStatus(HttpServletResponse.SC_OK);
    } catch (Exception e) {
      TestUtil.logErr("SendVariousMimeAttachmentsTest Exception: " + e);
      TestUtil.printStackTrace(e);
      System.err.println("SendVariousMimeAttachmentsTest Exception: " + e);
      e.printStackTrace(System.err);
      // Set status code to INTERNAL SERVER ERROR
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      pass = false;
    }
    // Send response object and test result back to client
    if (pass)
      resultProps.setProperty("TESTRESULT", "pass");
    else
      resultProps.setProperty("TESTRESULT", "fail");
    TestUtil.logMsg("TESTRESULT=" + resultProps.getProperty("TESTRESULT"));
    resultProps.list(out);
  }
}
