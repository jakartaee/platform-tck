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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPPart;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import com.sun.ts.lib.porting.TSURL;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.saaj.common.SOAP_Util;

import jakarta.activation.DataHandler;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.MimeHeader;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;

public class SOAPPartTestServlet extends HttpServlet {
  private MessageFactory mf = null;

  private SOAPMessage msg = null;

  private SOAPPart sp = null;

  private SOAPEnvelope envelope = null;

  private MimeHeader mh = null;

  private Iterator iterator = null;

  private InputStream in = null;

  private InputStream in2 = null;

  private StreamSource ssrc = null;

  private StreamSource ssrc2 = null;

  private DataHandler dh = null;

  private DataHandler dh2 = null;

  private URL url = null;

  private URL url2 = null;

  private TSURL tsurl = new TSURL();

  private String cntxroot = "/SOAPPart_web";

  private String host = null;

  private int port = 0;

  String soapVersion = null;

  private static final String SOAP12 = "soap12";

  private void setup() throws Exception {
    TestUtil.logTrace("setup");

    SOAP_Util.setup();

    // Create a message from the message factory.
    TestUtil.logMsg("Create message from message factory");
    msg = SOAP_Util.getMessageFactory().createMessage();

    // Message creation takes care of creating the SOAPPart - a
    // required part of the message as per the SOAP 1.1
    // specification.
    sp = msg.getSOAPPart();

    // Remove any MimeHeaders if any
    sp.removeAllMimeHeaders();

    // Get host and port info
    host = SOAP_Util.getHostname();
    port = SOAP_Util.getPortnum();

    // soap 11 envelope
    url = tsurl.getURL("http", host, port, cntxroot + "/attach.xml");
    TestUtil.logMsg("Get DataHandler to xml attachment: attach.xml");
    TestUtil.logMsg("URL = " + url);
    dh = new DataHandler(url);
    TestUtil.logMsg("Get InputStream of DataHandler");
    in = dh.getInputStream();
    TestUtil.logMsg("in = " + in);
    TestUtil.logMsg("Get StreamSource from InputStream");
    ssrc = new StreamSource(in);
    TestUtil.logMsg("ssrc = " + ssrc);

    // soap 12 envelope
    url2 = tsurl.getURL("http", host, port, cntxroot + "/attach12.xml");
    TestUtil.logMsg("Get DataHandler to xml attachment: attach12.xml");
    TestUtil.logMsg("URL = " + url2);
    dh2 = new DataHandler(url2);
    TestUtil.logMsg("Get InputStream of DataHandler");
    in2 = dh2.getInputStream();
    TestUtil.logMsg("in2 = " + in2);
    TestUtil.logMsg("Get StreamSource from InputStream");
    ssrc2 = new StreamSource(in2);
    TestUtil.logMsg("ssrc2 = " + ssrc2);
  }

  private void dispatch(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("dispatch");
    soapVersion = SOAP_Util.getHarnessProps().getProperty("SOAPVERSION");
    String testname = SOAP_Util.getHarnessProps().getProperty("TESTNAME");
    if (testname.equals("addMimeHeader1Test")) {
      TestUtil.logMsg("Starting addMimeHeader1Test");
      addMimeHeader1Test(req, res);
    } else if (testname.equals("addMimeHeader2Test")) {
      TestUtil.logMsg("Starting addMimeHeader2Test");
      addMimeHeader2Test(req, res);
    } else if (testname.equals("addMimeHeader3Test")) {
      TestUtil.logMsg("Starting addMimeHeader3Test");
      addMimeHeader3Test(req, res);
    } else if (testname.equals("addMimeHeader4Test")) {
      TestUtil.logMsg("Starting addMimeHeader4Test");
      addMimeHeader4Test(req, res);
    } else if (testname.equals("addMimeHeader5Test")) {
      TestUtil.logMsg("Starting addMimeHeader5Test");
      addMimeHeader5Test(req, res);
    } else if (testname.equals("addMimeHeader6Test")) {
      TestUtil.logMsg("Starting addMimeHeader6Test");
      addMimeHeader6Test(req, res);
    } else if (testname.equals("addMimeHeader7Test")) {
      TestUtil.logMsg("Starting addMimeHeader7Test");
      addMimeHeader7Test(req, res);
    } else if (testname.equals("getAllMimeHeaders1Test")) {
      TestUtil.logTrace("Starting getAllMimeHeaders1Test");
      getAllMimeHeaders1Test(req, res);
    } else if (testname.equals("getAllMimeHeaders2Test")) {
      TestUtil.logTrace("Starting getAllMimeHeaders2Test");
      getAllMimeHeaders2Test(req, res);
    } else if (testname.equals("getAllMimeHeaders3Test")) {
      TestUtil.logTrace("Starting getAllMimeHeaders3Test");
      getAllMimeHeaders3Test(req, res);
    } else if (testname.equals("getAllMimeHeaders4Test")) {
      TestUtil.logTrace("Starting getAllMimeHeaders4Test");
      getAllMimeHeaders4Test(req, res);
    } else if (testname.equals("getContentId1Test")) {
      TestUtil.logMsg("Starting getContentId1Test");
      getContentId1Test(req, res);
    } else if (testname.equals("getContentId2Test")) {
      TestUtil.logMsg("Starting getContentId2Test");
      getContentId2Test(req, res);
    } else if (testname.equals("getContentId3Test")) {
      TestUtil.logMsg("Starting getContentId3Test");
      getContentId3Test(req, res);
    } else if (testname.equals("getContentLocation1Test")) {
      TestUtil.logMsg("Starting getContentLocation1Test");
      getContentLocation1Test(req, res);
    } else if (testname.equals("getContentLocation2Test")) {
      TestUtil.logMsg("Starting getContentLocation2Test");
      getContentLocation2Test(req, res);
    } else if (testname.equals("getContentLocation3Test")) {
      TestUtil.logMsg("Starting getContentLocation3Test");
      getContentLocation3Test(req, res);
    } else if (testname.equals("getContentLocation4Test")) {
      TestUtil.logMsg("Starting getContentLocation4Test");
      getContentLocation4Test(req, res);
    } else if (testname.equals("getEnvelope1Test")) {
      TestUtil.logMsg("Starting getEnvelope1Test");
      getEnvelope1Test(req, res);
    } else if (testname.equals("getMatchingMimeHeaders1Test")) {
      TestUtil.logMsg("Starting getMatchingMimeHeaders1Test");
      getMatchingMimeHeaders1Test(req, res);
    } else if (testname.equals("getMatchingMimeHeaders2Test")) {
      TestUtil.logMsg("Starting getMatchingMimeHeaders2Test");
      getMatchingMimeHeaders2Test(req, res);
    } else if (testname.equals("getMatchingMimeHeaders3Test")) {
      TestUtil.logMsg("Starting getMatchingMimeHeaders3Test");
      getMatchingMimeHeaders3Test(req, res);
    } else if (testname.equals("getMatchingMimeHeaders4Test")) {
      TestUtil.logMsg("Starting getMatchingMimeHeaders4Test");
      getMatchingMimeHeaders4Test(req, res);
    } else if (testname.equals("getMatchingMimeHeaders5Test")) {
      TestUtil.logMsg("Starting getMatchingMimeHeaders5Test");
      getMatchingMimeHeaders5Test(req, res);
    } else if (testname.equals("getMimeHeader1Test")) {
      TestUtil.logMsg("Starting getMimeHeader1Test");
      getMimeHeader1Test(req, res);
    } else if (testname.equals("getMimeHeader2Test")) {
      TestUtil.logMsg("Starting getMimeHeader2Test");
      getMimeHeader2Test(req, res);
    } else if (testname.equals("getMimeHeader3Test")) {
      TestUtil.logMsg("Starting getMimeHeader3Test");
      getMimeHeader3Test(req, res);
    } else if (testname.equals("getMimeHeader4Test")) {
      TestUtil.logMsg("Starting getMimeHeader4Test");
      getMimeHeader4Test(req, res);
    } else if (testname.equals("getNonMatchingMimeHeaders1Test")) {
      TestUtil.logMsg("Starting getNonMatchingMimeHeaders1Test");
      getNonMatchingMimeHeaders1Test(req, res);
    } else if (testname.equals("getNonMatchingMimeHeaders2Test")) {
      TestUtil.logMsg("Starting getNonMatchingMimeHeaders2Test");
      getNonMatchingMimeHeaders2Test(req, res);
    } else if (testname.equals("getNonMatchingMimeHeaders3Test")) {
      TestUtil.logMsg("Starting getNonMatchingMimeHeaders3Test");
      getNonMatchingMimeHeaders3Test(req, res);
    } else if (testname.equals("getNonMatchingMimeHeaders4Test")) {
      TestUtil.logMsg("Starting getNonMatchingMimeHeaders4Test");
      getNonMatchingMimeHeaders4Test(req, res);
    } else if (testname.equals("getNonMatchingMimeHeaders5Test")) {
      TestUtil.logMsg("Starting getNonMatchingMimeHeaders5Test");
      getNonMatchingMimeHeaders5Test(req, res);
    } else if (testname.equals("removeAllMimeHeaders1Test")) {
      TestUtil.logMsg("Starting removeAllMimeHeaders1Test");
      removeAllMimeHeaders1Test(req, res);
    } else if (testname.equals("removeAllMimeHeaders2Test")) {
      TestUtil.logMsg("Starting removeAllMimeHeaders2Test");
      removeAllMimeHeaders2Test(req, res);
    } else if (testname.equals("removeAllMimeHeaders3Test")) {
      TestUtil.logMsg("Starting removeAllMimeHeaders3Test");
      removeAllMimeHeaders3Test(req, res);
    } else if (testname.equals("removeAllMimeHeaders4Test")) {
      TestUtil.logMsg("Starting removeAllMimeHeaders4Test");
      removeAllMimeHeaders4Test(req, res);
    } else if (testname.equals("removeMimeHeader1Test")) {
      TestUtil.logMsg("Starting removeMimeHeader1Test");
      removeMimeHeader1Test(req, res);
    } else if (testname.equals("removeMimeHeader2Test")) {
      TestUtil.logMsg("Starting removeMimeHeader2Test");
      removeMimeHeader2Test(req, res);
    } else if (testname.equals("removeMimeHeader3Test")) {
      TestUtil.logMsg("Starting removeMimeHeader3Test");
      removeMimeHeader3Test(req, res);
    } else if (testname.equals("removeMimeHeader4Test")) {
      TestUtil.logMsg("Starting removeMimeHeader4Test");
      removeMimeHeader4Test(req, res);
    } else if (testname.equals("setContentId1Test")) {
      TestUtil.logMsg("Starting setContentId1Test");
      setContentId1Test(req, res);
    } else if (testname.equals("setContentId2Test")) {
      TestUtil.logMsg("Starting setContentId2Test");
      setContentId2Test(req, res);
    } else if (testname.equals("setContentId3Test")) {
      TestUtil.logMsg("Starting setContentId3Test");
      setContentId3Test(req, res);
    } else if (testname.equals("setContentId4Test")) {
      TestUtil.logMsg("Starting setContentId4Test");
      setContentId4Test(req, res);
    } else if (testname.equals("setContentLocation1Test")) {
      TestUtil.logMsg("Starting setContentLocation1Test");
      setContentLocation1Test(req, res);
    } else if (testname.equals("setContentLocation2Test")) {
      TestUtil.logMsg("Starting setContentLocation2Test");
      setContentLocation2Test(req, res);
    } else if (testname.equals("setContentLocation3Test")) {
      TestUtil.logMsg("Starting setContentLocation3Test");
      setContentLocation3Test(req, res);
    } else if (testname.equals("setContentLocation4Test")) {
      TestUtil.logMsg("Starting setContentLocation4Test");
      setContentLocation4Test(req, res);
    } else if (testname.equals("setContentLocation5Test")) {
      TestUtil.logMsg("Starting setContentLocation5Test");
      setContentLocation5Test(req, res);
    } else if (testname.equals("setContent1Test")) {
      TestUtil.logMsg("Starting setContent1Test");
      setContent1Test(req, res);
    } else if (testname.equals("getContent1Test")) {
      TestUtil.logMsg("Starting getContent1Test");
      getContent1Test(req, res);
    } else if (testname.equals("setMimeHeader1Test")) {
      TestUtil.logMsg("Starting setMimeHeader1Test");
      setMimeHeader1Test(req, res);
    } else if (testname.equals("setMimeHeader2Test")) {
      TestUtil.logMsg("Starting setMimeHeader2Test");
      setMimeHeader2Test(req, res);
    } else if (testname.equals("setMimeHeader3Test")) {
      TestUtil.logMsg("Starting setMimeHeader3Test");
      setMimeHeader3Test(req, res);
    } else if (testname.equals("setMimeHeader4Test")) {
      TestUtil.logMsg("Starting setMimeHeader4Test");
      setMimeHeader4Test(req, res);
    } else if (testname.equals("setMimeHeader5Test")) {
      TestUtil.logMsg("Starting setMimeHeader5Test");
      setMimeHeader5Test(req, res);
    } else {
      throw new ServletException(
          "The testname '" + testname + "' was not found in the test servlet");

    }
  }

  public void init(ServletConfig servletConfig) throws ServletException {
    super.init(servletConfig);
    System.out.println("AddHeaderTestServlet:init (Entering)");
    SOAP_Util.doServletInit(servletConfig);
    System.out.println("AddHeaderTestServlet:init (Leaving)");
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

  private void displayArray(String[] array) {
    int len = array.length;
    for (int i = 0; i < len; i++) {
      TestUtil.logMsg("array[" + i + "]=" + array[i]);
    }
  }

  private void displayHeaders() {
    TestUtil.logMsg("Getting all MimeHeaders");
    iterator = sp.getAllMimeHeaders();
    while (iterator.hasNext()) {
      mh = (MimeHeader) iterator.next();
      String name = mh.getName();
      String value = mh.getValue();
      TestUtil.logMsg("received: name=" + name + ", value=" + value);
    }
  }

  private void addMimeHeader1Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("addMimeHeader1Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Adding MimeHeader");
      sp.addMimeHeader("Content-Description", "some text");

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

  private void addMimeHeader2Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("addMimeHeader2Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Adding 2 MimeHeaders");
      sp.addMimeHeader("Content-Description", "some text");
      sp.addMimeHeader("Content-Id", "id@abc.com");

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

  private void addMimeHeader3Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("addMimeHeader3Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Adding 2 MimeHeaders");
      sp.addMimeHeader("Content-Description", "some text");
      sp.addMimeHeader("Content-Description", "some text2");

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

  private void addMimeHeader4Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("addMimeHeader4Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Adding MimeHeader with invalid arguments");
      sp.addMimeHeader("", "");

      TestUtil.logErr(
          "Error: expected java.lang.IllegalArgumentException to be thrown");
      pass = false;
    } catch (java.lang.IllegalArgumentException ia) {
      // test passed
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

  private void addMimeHeader5Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("addMimeHeader5Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Adding MimeHeader with invalid arguments");
      sp.addMimeHeader("", "some text");

      TestUtil.logErr(
          "Error: expected java.lang.IllegalArgumentException to be thrown");
      pass = false;
    } catch (java.lang.IllegalArgumentException ia) {
      // test passed
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

  private void addMimeHeader6Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("addMimeHeader6Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Adding MimeHeader with null value");
      sp.addMimeHeader("Content-Description", null);

      TestUtil.logMsg("Adding MimeHeader with null string value");
      sp.addMimeHeader("Content-Description", "");
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

  private void addMimeHeader7Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("addMimeHeader7Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Adding MimeHeader with invalid arguments");
      sp.addMimeHeader(null, null);

      TestUtil.logErr(
          "Error: expected java.lang.IllegalArgumentException to be thrown");
      pass = false;
    } catch (java.lang.IllegalArgumentException ia) {
      // test passed
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

  private void getAllMimeHeaders1Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getAllMimeHeaders1Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Removing all MimeHeaders");
      sp.removeAllMimeHeaders();

      TestUtil.logMsg("Adding MimeHeader");
      sp.addMimeHeader("Content-Description", "some text");

      TestUtil.logMsg("Getting all MimeHeaders");
      Iterator iterator = sp.getAllMimeHeaders();
      int cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        mh = (MimeHeader) iterator.next();
        String name = mh.getName();
        String value = mh.getValue();
        if (name.equals("Content-Description") && value.equals("some text")) {
          TestUtil.logMsg("MimeHeader did match");
        } else {
          TestUtil.logErr("MimeHeader did not match");
          TestUtil.logErr("received: name=" + name + ", value=" + value);
          pass = false;
        }
      }

      if (cnt != 1) {
        TestUtil.logErr(
            "Error: expected one items to be returned, got a total of:" + cnt);
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

  private void getAllMimeHeaders2Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getAllMimeHeaders2Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Removing all MimeHeaders");
      sp.removeAllMimeHeaders();

      TestUtil.logMsg("Adding 2 MimeHeaders");
      sp.addMimeHeader("Content-Description", "some text");
      sp.addMimeHeader("Content-Id", "id@abc.com");

      TestUtil.logMsg("Getting all MimeHeaders");
      Iterator iterator = sp.getAllMimeHeaders();
      int cnt = 0;
      boolean foundHeader1 = false;
      boolean foundHeader2 = false;
      while (iterator.hasNext()) {
        cnt++;
        mh = (MimeHeader) iterator.next();
        String name = mh.getName();
        String value = mh.getValue();
        if (name.equals("Content-Description") && value.equals("some text")) {
          if (!foundHeader1) {
            foundHeader1 = true;
            TestUtil.logMsg("MimeHeaders do match for header1");
            TestUtil.logMsg("receive: name=" + name + ", value=" + value);
          } else {
            TestUtil.logErr(
                "Error: Received the same header1 MimeHeader again (unexpected)");
            TestUtil.logErr("received: name=" + name + ", value=" + value);
            pass = false;
          }
        } else if (name.equals("Content-Id") && value.equals("id@abc.com")) {
          if (!foundHeader2) {
            foundHeader2 = true;
            TestUtil.logMsg("MimeHeaders do match for header2");
            TestUtil.logMsg("receive: name=" + name + ", value=" + value);
          } else {
            TestUtil.logErr(
                "Error: Received the same header2 MimeHeader again (unexpected)");
            TestUtil.logErr("received: name=" + name + ", value=" + value);
            pass = false;
          }
        } else {
          TestUtil.logErr("Error: Received an unexpected MimeHeader");
          TestUtil.logErr("received: name=" + name + ", value=" + value);
          pass = false;
        }
      }

      if (!(foundHeader1 && foundHeader2)) {
        TestUtil.logErr("Error: did not receive all MimeHeaders");
        pass = false;
      }

      if (cnt != 2) {
        TestUtil.logErr(
            "Error: expected two items to be returned, got a total of:" + cnt);
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

  private void getAllMimeHeaders3Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getAllMimeHeaders3Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Removing all MimeHeaders");
      sp.removeAllMimeHeaders();

      TestUtil.logMsg("Adding 2 MimeHeaders");
      sp.addMimeHeader("Content-Description", "some text");
      sp.addMimeHeader("Content-Description", "some text2");

      TestUtil.logMsg("Getting all MimeHeaders");
      Iterator iterator = sp.getAllMimeHeaders();
      int cnt = 0;
      boolean foundHeader1 = false;
      boolean foundHeader2 = false;
      boolean foundDefaultHeader = false;
      while (iterator.hasNext()) {
        cnt++;
        mh = (MimeHeader) iterator.next();
        String name = mh.getName();
        String value = mh.getValue();
        if (name.equals("Content-Description") && value.equals("some text")) {
          if (!foundHeader1) {
            foundHeader1 = true;
            TestUtil.logMsg("MimeHeaders do match for header1");
            TestUtil.logMsg("receive: name=" + name + ", value=" + value);
          } else {
            TestUtil.logErr(
                "Error: Received the same header1 MimeHeader again (unexpected)");
            TestUtil.logErr("received: name=" + name + ", value=" + value);
            pass = false;
          }
        } else if (name.equals("Content-Description")
            && value.equals("some text2")) {
          if (!foundHeader2) {
            foundHeader2 = true;
            TestUtil.logMsg("MimeHeaders do match for header2");
            TestUtil.logMsg("receive: name=" + name + ", value=" + value);
          } else {
            TestUtil.logErr(
                "Error: Received the same header2 MimeHeader again (unexpected)");
            TestUtil.logErr("received: name=" + name + ", value=" + value);
            pass = false;
          }
        } else {
          TestUtil.logErr("Error: Received an unexpected MimeHeader");
          TestUtil.logErr("received: name=" + name + ", value=" + value);
          pass = false;
        }
      }

      if (!(foundHeader1 && foundHeader2)) {
        TestUtil.logErr("Error: did not receive all MimeHeaders");
        pass = false;
      }
      if (cnt != 2) {
        TestUtil.logErr(
            "Error: expected two items to be returned, got a total of:" + cnt);
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

  private void getAllMimeHeaders4Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getAllMimeHeaders4Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Getting all MimeHeaders");
      Iterator iterator = sp.getAllMimeHeaders();
      int cnt = 0;
      boolean foundDefaultHeader = false;
      while (iterator.hasNext()) {
        cnt++;
        mh = (MimeHeader) iterator.next();
        String name = mh.getName();
        String value = mh.getValue();
        if (name.equals("Content-Type") && value.equals("text/xml")) {
          if (!foundDefaultHeader) {
            foundDefaultHeader = true;
            TestUtil.logMsg("MimeHeaders do match for the default MimeHeader ");
            TestUtil.logMsg("receive: name=" + name + ", value=" + value);
          } else {
            TestUtil.logErr(
                "Error: Received the same the default MimeHeader again (unexpected)");
            TestUtil.logErr("received: name=" + name + ", value=" + value);
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

  private void getContentId1Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getContentId1Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Setting Content Id");
      sp.setContentId("id@abc.com");

      TestUtil.logMsg("Getting Content Id");
      String result = sp.getContentId();

      if (!result.equals("id@abc.com")) {
        TestUtil.logErr("Error: received invalid value from getContentId()");
        TestUtil.logErr("expected result: id@abc.com");
        TestUtil.logErr("actual result:" + result);
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

  private void getContentId2Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getContentId2Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Setting Content Id");
      sp.setContentId("id@abc.com");

      TestUtil.logMsg("Setting Content Id again");
      sp.setContentId("id2@abc2.com");

      TestUtil.logMsg("Getting Content Id");
      String result = sp.getContentId();

      if (!result.equals("id2@abc2.com")) {
        TestUtil.logErr("Error: received invalid value from getContentId()");
        TestUtil.logErr("expected result: id2@abc2.com");
        TestUtil.logErr("actual result:" + result);
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

  private void getContentId3Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getContentId3Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      String result = null;
      TestUtil.logMsg("Remove all MimeHeader's");
      sp.removeAllMimeHeaders();
      TestUtil.logMsg("Getting Content Id");
      result = sp.getContentId();

      if (result != null && !result.equals("")) {
        TestUtil.logErr("Error: received invalid value from getContentId()");
        TestUtil.logErr("expected result: null or null string");
        TestUtil.logErr("actual result:" + result);
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

  private void getContentLocation1Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getContentLocation1Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Setting Content Location");
      sp.setContentLocation("http://localhost/someapp");

      TestUtil.logMsg("Getting Content Location");
      String result = sp.getContentLocation();

      if (!result.equals("http://localhost/someapp")) {
        TestUtil.logErr(
            "Error: received invalid Location value from getContentLocation()");
        TestUtil.logErr("expected result: http://localhost/someapp");
        TestUtil.logErr("actual result:" + result);
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

  private void getContentLocation2Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getContentLocation2Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Getting Content Location");
      String result = sp.getContentLocation();

      if (result != null) {
        TestUtil.logErr(
            "Error: received invalid Location value from getContentLocation()");
        TestUtil.logErr("expected result: null");
        TestUtil.logErr("actual result:" + result);
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

  private void getContentLocation3Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getContentLocation3Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Setting Content Location");
      sp.setContentLocation("/WEB-INF/somefile");

      TestUtil.logMsg("Getting Content Location");
      String result = sp.getContentLocation();

      if (!result.equals("/WEB-INF/somefile")) {
        TestUtil.logErr(
            "Error: received invalid Location value from getContentLocation()");
        TestUtil.logErr("expected result: /WEB-INF/somefile");
        TestUtil.logErr("actual result:" + result);
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

  private void getContentLocation4Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getContentLocation4Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Setting Content Location");
      sp.setContentLocation("http://localhost/someapp");

      TestUtil.logMsg("Setting Content Location again");
      sp.setContentLocation("/WEB-INF/somefile");

      TestUtil.logMsg("Getting Content Location");
      String result = sp.getContentLocation();

      if (!result.equals("/WEB-INF/somefile")) {
        TestUtil.logErr(
            "Error: received invalid Location value from getContentLocation()");
        TestUtil.logErr("expected result: /WEB-INF/somefile");
        TestUtil.logErr("actual result:" + result);
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

  private void getEnvelope1Test(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("getEnvelope1Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      envelope = sp.getEnvelope();
      if (envelope == null) {
        TestUtil.logErr("Error: received null value from getEnvelope()");
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

  private void getMatchingMimeHeaders1Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getMatchingMimeHeaders1Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Adding MimeHeader");
      sp.addMimeHeader("Content-Description", "some text");

      displayHeaders();

      String sArray[] = { "Content-Description" };
      TestUtil.logMsg("List of matching MimeHeaders contains:");
      displayArray(sArray);

      TestUtil.logMsg("Getting matching MimeHeaders");
      iterator = sp.getMatchingMimeHeaders(sArray);
      int cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        mh = (MimeHeader) iterator.next();
        String name = mh.getName();
        String value = mh.getValue();
        if (name.equals("Content-Description") && value.equals("some text")) {
          TestUtil.logMsg("MimeHeaders do match ");
          TestUtil.logMsg("receive: name=" + name + ", value=" + value);
        } else {
          TestUtil.logErr("Error: Received an unexpected MimeHeader");
          TestUtil.logErr("received: name=" + name + ", value=" + value);
          pass = false;
        }
      }

      if (cnt != 1) {
        TestUtil.logErr(
            "Error: expected one item to be returned, got a total of:" + cnt);
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

  private void getMatchingMimeHeaders2Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getMatchingMimeHeaders2Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Adding 2 MimeHeaders");
      sp.addMimeHeader("Content-Description", "some text");
      sp.addMimeHeader("Content-Id", "id@abc.com");

      displayHeaders();

      String sArray[] = { "Content-Description" };
      TestUtil.logMsg("List of matching MimeHeaders contains:");
      displayArray(sArray);

      TestUtil.logMsg("Getting matching MimeHeaders");
      iterator = sp.getMatchingMimeHeaders(sArray);
      int cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        mh = (MimeHeader) iterator.next();
        String name = mh.getName();
        String value = mh.getValue();
        if (name.equals("Content-Description") && value.equals("some text")) {
          TestUtil.logMsg("MimeHeaders do match ");
          TestUtil.logMsg("receive: name=" + name + ", value=" + value);
        } else {
          TestUtil.logErr("Error: Received an unexpected MimeHeader");
          TestUtil.logErr("received: name=" + name + ", value=" + value);
          pass = false;
        }
      }

      if (cnt != 1) {
        TestUtil.logErr(
            "Error: expected one item to be returned, got a total of:" + cnt);
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

  private void getMatchingMimeHeaders3Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getMatchingMimeHeaders3Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Adding 2 MimeHeaders");
      sp.addMimeHeader("Content-Description", "some text");
      sp.addMimeHeader("Content-Description", "some text2");

      displayHeaders();

      String sArray[] = { "Content-Description" };
      TestUtil.logMsg("List of matching MimeHeaders contains:");
      displayArray(sArray);

      TestUtil.logMsg("Getting matching MimeHeaders");
      iterator = sp.getMatchingMimeHeaders(sArray);
      int cnt = 0;
      boolean foundHeader1 = false;
      boolean foundHeader2 = false;
      while (iterator.hasNext()) {
        cnt++;
        mh = (MimeHeader) iterator.next();
        String name = mh.getName();
        String value = mh.getValue();
        if (name.equals("Content-Description") && value.equals("some text")) {
          if (!foundHeader1) {
            foundHeader1 = true;
            TestUtil.logMsg("MimeHeaders do match for header1");
            TestUtil.logMsg("receive: name=" + name + ", value=" + value);
          } else {
            TestUtil.logErr(
                "Error: Received the same header1 MimeHeader again (unexpected)");
            TestUtil.logErr("received: name=" + name + ", value=" + value);
            pass = false;
          }
        } else if (name.equals("Content-Description")
            && value.equals("some text2")) {
          if (!foundHeader2) {
            foundHeader2 = true;
            TestUtil.logMsg("MimeHeaders do match for header2");
            TestUtil.logMsg("receive: name=" + name + ", value=" + value);
          } else {
            TestUtil.logErr(
                "Error: Received the same header2 MimeHeader again (unexpected)");
            TestUtil.logErr("received: name=" + name + ", value=" + value);
            pass = false;
          }
        } else {
          TestUtil.logErr("Error: Received an unexpected MimeHeader");
          TestUtil.logErr("received: name=" + name + ", value=" + value);
          pass = false;
        }
      }

      if (!(foundHeader1 && foundHeader2)) {
        TestUtil.logErr("Error: did not receive all MimeHeaders");
        pass = false;
      }
      if (cnt != 2) {
        TestUtil.logErr(
            "Error: expected three items to be returned, got a total of:"
                + cnt);
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

  private void getMatchingMimeHeaders4Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getMatchingMimeHeaders4Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Adding MimeHeader");
      sp.addMimeHeader("Content-Description", "some text");

      displayHeaders();

      String sArray[] = { "doesnotexist" };
      TestUtil.logMsg("List of matching MimeHeaders contains:");
      displayArray(sArray);

      TestUtil.logMsg("Getting non-existent MimeHeader");
      iterator = sp.getMatchingMimeHeaders(sArray);
      int cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        mh = (MimeHeader) iterator.next();
        String name = mh.getName();
        String value = mh.getValue();
        TestUtil.logErr("Error: Received an unexpected MimeHeader");
        TestUtil.logErr("received: name=" + name + ", value=" + value);
        pass = false;
      }

      if (cnt != 0) {
        TestUtil.logErr(
            "Error: expected no items to be returned, got a total of:" + cnt);
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

  private void getMatchingMimeHeaders5Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getMatchingMimeHeaders5Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Adding 3 MimeHeaders");
      sp.addMimeHeader("Content-Description", "some text");
      sp.addMimeHeader("Content-Description", "some text2");
      sp.addMimeHeader("Content-Id", "id@abc.com");

      displayHeaders();

      String sArray[] = { "Content-Description", "Content-Id",
          "Content-Location" };
      TestUtil.logMsg("List of matching MimeHeaders contains:");
      displayArray(sArray);

      TestUtil.logMsg("Getting matching MimeHeaders");
      iterator = sp.getMatchingMimeHeaders(sArray);
      int cnt = 0;
      boolean foundHeader1 = false;
      boolean foundHeader2 = false;
      boolean foundHeader3 = false;
      while (iterator.hasNext()) {
        cnt++;
        mh = (MimeHeader) iterator.next();
        String name = mh.getName();
        String value = mh.getValue();
        if (name.equals("Content-Description") && value.equals("some text")) {
          if (!foundHeader1) {
            foundHeader1 = true;
            TestUtil.logMsg("MimeHeaders do match for header1");
            TestUtil.logMsg("receive: name=" + name + ", value=" + value);
          } else {
            TestUtil.logErr(
                "Error: Received the same header1 MimeHeader again (unexpected)");
            TestUtil.logErr("received: name=" + name + ", value=" + value);
            pass = false;
          }
        } else if (name.equals("Content-Description")
            && value.equals("some text2")) {
          if (!foundHeader2) {
            foundHeader2 = true;
            TestUtil.logMsg("MimeHeaders do match for header2");
            TestUtil.logMsg("receive: name=" + name + ", value=" + value);
          } else {
            TestUtil.logErr(
                "Error: Received the same header2 MimeHeader again (unexpected)");
            TestUtil.logErr("received: name=" + name + ", value=" + value);
            pass = false;
          }
        } else if (name.equals("Content-Id") && value.equals("id@abc.com")) {
          if (!foundHeader3) {
            foundHeader3 = true;
            TestUtil.logMsg("MimeHeaders do match for header3");
            TestUtil.logMsg("receive: name=" + name + ", value=" + value);
          } else {
            TestUtil.logErr(
                "Error: Received the same header3 MimeHeader again (unexpected)");
            TestUtil.logErr("received: name=" + name + ", value=" + value);
            pass = false;
          }
        } else {
          TestUtil.logErr("Error: Received an unexpected header");
          TestUtil.logErr("received: name=" + name + ", value=" + value);
          pass = false;
        }
      }

      if (!(foundHeader1 && foundHeader2 && foundHeader3)) {
        TestUtil.logErr("Error: did not receive all MimeHeaders");
        pass = false;
      }
      if (cnt != 3) {
        TestUtil.logErr(
            "Error: expected three items to be returned, got a total of:"
                + cnt);
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

  private void getMimeHeader1Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getMimeHeader1Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Adding MimeHeader");
      sp.addMimeHeader("Content-Description", "some text");

      TestUtil.logMsg("Getting MimeHeader");
      String sArray[] = sp.getMimeHeader("Content-Description");
      int len = sArray.length;
      if (len != 1) {
        TestUtil.logErr(
            "Error: expected only one item to be returned, got a total of:"
                + len);
        pass = false;
      }

      for (int i = 0; i < len; i++) {
        String temp = sArray[i];
        if (!temp.equals("some text")) {
          TestUtil.logErr(
              "Error: received invalid value from getMimeHeader(Content-Description)");
          TestUtil.logErr("expected result: some text");
          TestUtil.logErr("actual result:" + temp);
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

  private void getMimeHeader2Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getMimeHeader2Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Adding 2 MimeHeaders");
      sp.addMimeHeader("Content-Description", "some text");
      sp.addMimeHeader("Content-Id", "id@abc.com");
      TestUtil.logMsg("Getting MimeHeader");
      String sArray[] = sp.getMimeHeader("Content-Description");
      int len = sArray.length;
      if (len != 1) {
        TestUtil.logErr(
            "Error: expected only one item to be returned for getMimeHeader(Content-Description), got a total of:"
                + len);
        pass = false;
      }

      for (int i = 0; i < len; i++) {
        String temp = sArray[i];
        if (!temp.equals("some text")) {
          TestUtil.logErr(
              "Error: received invalid value from getMimeHeader(Content-Description)");
          TestUtil.logErr("expected result: some text");
          TestUtil.logErr("actual result:" + temp);
          pass = false;
        }
      }

      sArray = sp.getMimeHeader("Content-Id");
      len = sArray.length;
      if (len != 1) {
        TestUtil.logErr(
            "Error: expected only one item to be returned for getMimeHeader(Content-Id), got a total of:"
                + len);
        pass = false;
      }

      for (int i = 0; i < len; i++) {
        String temp = sArray[i];
        if (!temp.equals("id@abc.com")) {
          TestUtil.logErr(
              "Error: received invalid value from getMimeHeader(Content-Id)");
          TestUtil.logErr("expected result: id@abc.com");
          TestUtil.logErr("actual result:" + temp);
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

  private void getMimeHeader3Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getMimeHeader3Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Adding 2 MimeHeaders");
      sp.addMimeHeader("Content-Description", "some text");
      sp.addMimeHeader("Content-Description", "some text2");
      TestUtil.logMsg("Getting MimeHeader");
      String sArray[] = sp.getMimeHeader("Content-Description");
      int len = sArray.length;
      if (len != 2) {
        TestUtil.logErr(
            "Error: expected only one item to be returned for getMimeHeader(Content-Description), got a total of:"
                + len);
        pass = false;
      }

      for (int i = 0; i < len; i++) {
        String temp = sArray[i];
        if (!temp.equals("some text") && !temp.equals("some text2")) {
          TestUtil.logErr(
              "Error: received invalid value from getMimeHeader(Content-Description)");
          TestUtil.logErr("expected result: some text or some text2");
          TestUtil.logErr("actual result:" + temp);
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

  private void getMimeHeader4Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getMimeHeader4Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Adding MimeHeader");
      sp.addMimeHeader("Content-Description", "some text");

      TestUtil.logMsg("Getting non-existent MimeHeader");
      String sArray[] = sp.getMimeHeader("doesnotexist");
      if (sArray != null && sArray.length > 0) {
        TestUtil.logErr("Error: was able to get a non-existent MimeHeader");
        pass = false;
        int len = sArray.length;
        for (int i = 0; i < len; i++) {
          TestUtil.logErr("actual result:" + sArray[i]);
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

  private void getNonMatchingMimeHeaders1Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getNonMatchingMimeHeaders1Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Removing all MimeHeaders");
      sp.removeAllMimeHeaders();

      TestUtil.logMsg("Adding MimeHeader");
      sp.addMimeHeader("Content-Description", "some text");

      displayHeaders();

      String sArray[] = { "Content-Description" };
      TestUtil.logMsg("List of non matching MimeHeaders contains:");
      displayArray(sArray);

      TestUtil.logMsg("Getting non matching MimeHeaders");
      iterator = sp.getNonMatchingMimeHeaders(sArray);
      int cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        mh = (MimeHeader) iterator.next();
        String name = mh.getName();
        String value = mh.getValue();
        TestUtil.logErr("Error: Received an invalid MimeHeader");
        TestUtil.logErr("received: name=" + name + ", value=" + value);
        pass = false;
      }

      if (cnt != 0) {
        TestUtil.logErr(
            "Error: expected no items to be returned, got a total of:" + cnt);
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

  private void getNonMatchingMimeHeaders2Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getNonMatchingMimeHeaders2Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Removing all MimeHeaders");
      sp.removeAllMimeHeaders();

      TestUtil.logMsg("Adding 2 MimeHeaders");
      sp.addMimeHeader("Content-Description", "some text");
      sp.addMimeHeader("Content-Id", "id@abc.com");

      displayHeaders();

      String sArray[] = { "Content-Id" };
      TestUtil.logMsg("List of non matching MimeHeaders contains:");
      displayArray(sArray);

      TestUtil.logMsg("Getting non matching MimeHeaders");
      iterator = sp.getNonMatchingMimeHeaders(sArray);
      int cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        mh = (MimeHeader) iterator.next();
        String name = mh.getName();
        String value = mh.getValue();
        if (name.equals("Content-Description") && value.equals("some text")) {
          TestUtil.logMsg("MimeHeaders do match ");
          TestUtil.logMsg("receive: name=" + name + ", value=" + value);
        } else {
          TestUtil.logErr("Error: Received unexpected MimeHeader");
          TestUtil.logErr("received: name=" + name + ", value=" + value);
          pass = false;
        }
      }

      if (cnt != 1) {
        TestUtil.logErr(
            "Error: expected one item to be returned, got a total of:" + cnt);
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

  private void getNonMatchingMimeHeaders3Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getNonMatchingMimeHeaders3Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      // Remove any MimeHeaders if any
      sp.removeAllMimeHeaders();

      TestUtil.logMsg("Adding 2 MimeHeaders");
      sp.addMimeHeader("Content-Description", "some text");
      sp.addMimeHeader("Content-Description", "some text2");

      displayHeaders();

      String sArray[] = { "Content-Id" };
      TestUtil.logMsg("List of non matching MimeHeaders contains:");
      displayArray(sArray);

      TestUtil.logMsg("Getting non matching MimeHeaders");
      iterator = sp.getNonMatchingMimeHeaders(sArray);
      int cnt = 0;
      boolean foundHeader1 = false;
      boolean foundHeader2 = false;
      while (iterator.hasNext()) {
        cnt++;
        mh = (MimeHeader) iterator.next();
        String name = mh.getName();
        String value = mh.getValue();
        if (name.equals("Content-Description") && value.equals("some text")) {
          if (!foundHeader1) {
            foundHeader1 = true;
            TestUtil.logMsg("MimeHeaders do match for header1");
            TestUtil.logMsg("receive: name=" + name + ", value=" + value);
          } else {
            TestUtil.logErr(
                "Error: Received the same header1 MimeHeader again (unexpected)");
            TestUtil.logErr("received: name=" + name + ", value=" + value);
            pass = false;
          }
        } else if (name.equals("Content-Description")
            && value.equals("some text2")) {
          if (!foundHeader2) {
            foundHeader2 = true;
            TestUtil.logMsg("MimeHeaders do match for header2");
            TestUtil.logMsg("receive: name=" + name + ", value=" + value);
          } else {
            TestUtil.logErr(
                "Error: Received the same header2 MimeHeader again (unexpected)");
            TestUtil.logErr("received: name=" + name + ", value=" + value);
            pass = false;
          }
        } else {
          TestUtil.logErr("Error: Received an invalid MimeHeader");
          TestUtil.logErr("received: name=" + name + ", value=" + value);
          pass = false;
        }
      }

      if (!(foundHeader1 && foundHeader2)) {
        TestUtil.logErr("Error: did not receive all MimeHeaders");
        pass = false;
      }
      if (cnt != 2) {
        TestUtil.logErr(
            "Error: expected two items to be returned, got a total of:" + cnt);
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

  private void getNonMatchingMimeHeaders4Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getNonMatchingMimeHeaders4Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      // Remove any MimeHeaders if any
      sp.removeAllMimeHeaders();

      TestUtil.logMsg("Adding MimeHeader");
      sp.addMimeHeader("Content-Description", "some text");

      displayHeaders();

      String sArray[] = { "Content-Description" };
      TestUtil.logMsg("List of non matching MimeHeaders contains:");
      displayArray(sArray);

      TestUtil.logMsg("Getting non matching MimeHeaders");
      iterator = sp.getNonMatchingMimeHeaders(sArray);
      int cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        mh = (MimeHeader) iterator.next();
        String name = mh.getName();
        String value = mh.getValue();
        TestUtil.logErr("Error: Received an invalid MimeHeader");
        TestUtil.logErr("received: name=" + name + ", value=" + value);
        pass = false;
      }

      if (cnt != 0) {
        TestUtil.logErr(
            "Error: expected no items to be returned, got a total of:" + cnt);
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

  private void getNonMatchingMimeHeaders5Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getNonMatchingMimeHeaders5Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      // Remove any MimeHeaders if any
      sp.removeAllMimeHeaders();

      TestUtil.logMsg("Adding 3 MimeHeaders");
      sp.addMimeHeader("Content-Description", "some text");
      sp.addMimeHeader("Content-Description", "some text2");
      sp.addMimeHeader("Content-Id", "id@abc.com");

      displayHeaders();

      String sArray[] = { "Content-Id", "Content-Location" };
      TestUtil.logMsg("List of non matching MimeHeaders contains:");
      displayArray(sArray);

      TestUtil.logMsg("Getting non matching MimeHeaders");
      iterator = sp.getNonMatchingMimeHeaders(sArray);
      int cnt = 0;
      boolean foundHeader1 = false;
      boolean foundHeader2 = false;
      while (iterator.hasNext()) {
        cnt++;
        mh = (MimeHeader) iterator.next();
        String name = mh.getName();
        String value = mh.getValue();
        if (name.equals("Content-Description") && value.equals("some text")) {
          if (!foundHeader1) {
            foundHeader1 = true;
            TestUtil.logMsg("MimeHeaders do match for header1");
            TestUtil.logMsg("receive: name=" + name + ", value=" + value);
          } else {
            TestUtil.logErr(
                "Error: Received the same header1 MimeHeader again (unexpected)");
            TestUtil.logErr("received: name=" + name + ", value=" + value);
            pass = false;
          }
        } else if (name.equals("Content-Description")
            && value.equals("some text2")) {
          if (!foundHeader2) {
            foundHeader2 = true;
            TestUtil.logMsg("MimeHeaders do match for header2");
            TestUtil.logMsg("receive: name=" + name + ", value=" + value);
          } else {
            TestUtil.logErr(
                "Error: Received the same header2 MimeHeader again (unexpected)");
            TestUtil.logErr("received: name=" + name + ", value=" + value);
            pass = false;
          }
        } else {
          TestUtil.logErr("Error: Received an invalid MimeHeader");
          TestUtil.logErr("received: name=" + name + ", value=" + value);
          pass = false;
        }
      }

      if (!(foundHeader1 && foundHeader2)) {
        TestUtil.logErr("Error: did not receive all MimeHeaders");
        pass = false;
      }
      if (cnt != 2) {
        TestUtil.logErr(
            "Error: expected two items to be returned, got a total of:" + cnt);
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

  private void removeAllMimeHeaders1Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("removeAllMimeHeaders1Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Adding MimeHeader");
      sp.addMimeHeader("Content-Description", "some text");

      TestUtil.logMsg("Removing all MimeHeaders...");
      sp.removeAllMimeHeaders();

      TestUtil.logMsg("Getting all MimeHeaders...");
      Iterator iterator = sp.getAllMimeHeaders();
      int cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        mh = (MimeHeader) iterator.next();
        String name = mh.getName();
        String value = mh.getValue();
        TestUtil.logErr("Received unexpected Mimeheader");
        TestUtil.logErr("received: name=" + name + ", value=" + value);
        pass = false;
      }

      if (cnt != 0) {
        TestUtil.logErr(
            "Error: expected no items to be returned, got a total of:" + cnt);
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

  private void removeAllMimeHeaders2Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("removeAllMimeHeaders2Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Adding MimeHeader");
      sp.addMimeHeader("Content-Description", "some text");
      sp.addMimeHeader("Content-Id", "id@abc.com");

      TestUtil.logMsg("Removing all MimeHeaders...");
      sp.removeAllMimeHeaders();

      TestUtil.logMsg("Getting all MimeHeaders...");
      Iterator iterator = sp.getAllMimeHeaders();
      int cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        mh = (MimeHeader) iterator.next();
        String name = mh.getName();
        String value = mh.getValue();
        TestUtil.logErr("Received unexpected Mimeheader");
        TestUtil.logMsg("receive: name=" + name + ", value=" + value);
        pass = false;
      }

      if (cnt != 0) {
        TestUtil.logErr(
            "Error: expected no items to be returned, got a total of:" + cnt);
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

  private void removeAllMimeHeaders3Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("removeAllMimeHeaders3Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Adding MimeHeader");
      sp.addMimeHeader("Content-Description", "some text");
      sp.addMimeHeader("Content-Description", "some text2");

      TestUtil.logMsg("Removing all MimeHeaders...");
      sp.removeAllMimeHeaders();

      TestUtil.logMsg("Getting all MimeHeaders...");
      Iterator iterator = sp.getAllMimeHeaders();
      int cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        mh = (MimeHeader) iterator.next();
        String name = mh.getName();
        String value = mh.getValue();
        TestUtil.logErr("Received unexpected Mimeheader");
        TestUtil.logMsg("receive: name=" + name + ", value=" + value);
        pass = false;
      }

      if (cnt != 0) {
        TestUtil.logErr(
            "Error: expected no items to be returned, got a total of:" + cnt);
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

  private void removeAllMimeHeaders4Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("removeAllMimeHeaders4Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Removing all MimeHeaders...");
      sp.removeAllMimeHeaders();

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

  private void removeMimeHeader1Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("removeMimeHeader1Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Adding MimeHeader");
      sp.addMimeHeader("Content-Description", "some text");

      TestUtil.logMsg("Removing MimeHeader");
      sp.removeMimeHeader("Content-Description");

      TestUtil.logMsg("Getting all MimeHeaders");
      Iterator iterator = sp.getAllMimeHeaders();
      int cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        mh = (MimeHeader) iterator.next();
        String name = mh.getName();
        String value = mh.getValue();
        TestUtil.logErr("Received unexpected MimeHeader");
        TestUtil.logErr("receive: name=" + name + ", value=" + value);
        pass = false;
      }

      if (cnt != 0) {
        TestUtil.logErr(
            "Error: expected no items to be returned, got atotal of:" + cnt);
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

  private void removeMimeHeader2Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("removeMimeHeader2Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Adding MimeHeader");
      sp.addMimeHeader("Content-Description", "some text");
      sp.addMimeHeader("Content-Id", "id@abc.com");

      TestUtil.logMsg("Removing MimeHeader");
      sp.removeMimeHeader("Content-Id");

      TestUtil.logMsg("Getting all MimeHeaders");
      Iterator iterator = sp.getAllMimeHeaders();
      int cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        mh = (MimeHeader) iterator.next();
        String name = mh.getName();
        String value = mh.getValue();
        if (name.equals("Content-Description") && value.equals("some text")) {
          TestUtil.logMsg("MimeHeader did match");
        } else {
          TestUtil.logErr("MimeHeader did not match");
          TestUtil.logErr("received: name=" + name + ", value=" + value);
          pass = false;
        }
      }

      if (cnt != 1) {
        TestUtil.logErr(
            "Error: expected one item to be returned, got a total of:" + cnt);
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

  private void removeMimeHeader3Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("removeMimeHeader3Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Adding MimeHeader");
      sp.addMimeHeader("Content-Description", "some text");
      sp.addMimeHeader("Content-Description", "some text2");

      TestUtil.logMsg("Removing MimeHeader");
      sp.removeMimeHeader("Content-Description");

      TestUtil.logMsg("Getting all MimeHeaders");
      Iterator iterator = sp.getAllMimeHeaders();
      int cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        mh = (MimeHeader) iterator.next();
        String name = mh.getName();
        String value = mh.getValue();
        TestUtil.logErr("Received unexpected MimeHeader");
        TestUtil.logMsg("receive: name=" + name + ", value=" + value);
        pass = false;
      }

      if (cnt != 0) {
        TestUtil.logErr(
            "Error: expected no items to be returned, got a total of:" + cnt);
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

  private void removeMimeHeader4Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("removeMimeHeader4Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Removing MimeHeader");
      sp.removeMimeHeader("doesnotexist");

      TestUtil.logMsg("Getting all MimeHeaders");
      Iterator iterator = sp.getAllMimeHeaders();
      int cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        mh = (MimeHeader) iterator.next();
        String name = mh.getName();
        String value = mh.getValue();
        TestUtil.logErr("Received unexpected MimeHeader");
        TestUtil.logMsg("receive: name=" + name + ", value=" + value);
        pass = false;
      }

      if (cnt != 0) {
        TestUtil.logErr(
            "Error: expected no items to be returned, got a total of:" + cnt);
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

  private void setContentId1Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("setContentId1Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logTrace("Setting Content Id");
      sp.setContentId("name@abc.com");

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

  private void setContentId2Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("setContentId2Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();
      TestUtil.logTrace("Setting Content Id to null value");
      sp.setContentId(null);
      TestUtil.logTrace("Setting Content Id to null string value");
      sp.setContentId("");
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

  private void setContentId3Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("setContentId3Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logTrace("Setting Content Id");
      sp.setContentId("id@abc.com");

      TestUtil.logTrace("Setting Content Id again");
      sp.setContentId("id2@abc2.com");

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

  private void setContentId4Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("setContentId4Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logTrace("Setting Content Id");
      sp.setContentId("id@abc.com");

      TestUtil.logTrace("Setting Content Id again");
      sp.setContentId("id@cde.com");

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

  private void setContentLocation1Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("setContentLocation1Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logTrace("Setting Content Location");
      sp.setContentLocation("http://localhost/someapp");

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

  private void setContentLocation2Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("setContentLocation2Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logTrace("Setting Content Location to null string value");
      sp.setContentLocation("");
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

  private void setContentLocation3Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("setContentLocation3Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logTrace("Setting Content Location again");
      sp.setContentLocation("/WEB-INF/somefile");

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

  private void setContentLocation4Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("setContentLocation4Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logTrace("Setting Content Location again");
      sp.setContentLocation("/WEB-INF/somefile");

      TestUtil.logTrace("Setting Content Location again");
      sp.setContentLocation("http://localhost/someapp");

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

  private void setContentLocation5Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("setContentLocation5Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logTrace("Setting Content Location to null value");
      sp.setContentLocation(null);
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

  private void setContent1Test(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("setContent1Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();
      TestUtil.logTrace("Setting Content ");
      if (!soapVersion.equals(SOAP12)) {
        sp.setContent(ssrc);
      } else {
        sp.setContent(ssrc2);
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

  private void getContent1Test(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("getContent1Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logTrace("Setting Content ");
      if (!soapVersion.equals(SOAP12)) {
        sp.setContent(ssrc);
      } else {
        sp.setContent(ssrc2);
      }

      TestUtil.logTrace("Getting Content ");
      Source ssrc3 = null;
      ssrc3 = sp.getContent();
      if (ssrc3 == null) {
        TestUtil.logErr("Error: getContent() returned null");
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

  private void setMimeHeader1Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("setMimeHeader1Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logTrace("Adding MimeHeader");
      sp.addMimeHeader("Content-Description", "some text");

      TestUtil.logTrace("Setting MimeHeader");
      sp.setMimeHeader("Content-Description", "some text2");

      TestUtil.logTrace("Getting MimeHeader");
      String sArray[] = sp.getMimeHeader("Content-Description");
      int len = sArray.length;
      if (len != 1) {
        TestUtil.logErr(
            "Error: expected only one item to be returned, got a total of:"
                + len);
        pass = false;
      }

      for (int i = 0; i < len; i++) {
        String temp = sArray[i];
        if (!temp.equals("some text2")) {
          TestUtil.logErr(
              "Error: received invalid value from setMimeHeader(Content-Description)");
          TestUtil.logErr("expected result: some text2");
          TestUtil.logErr("actual result:" + temp);
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

  private void setMimeHeader2Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("setMimeHeader2Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logTrace("Adding MimeHeader");
      sp.addMimeHeader("Content-Description", "some text");
      sp.addMimeHeader("Content-Id", "id@abc.com");

      TestUtil.logTrace("Setting MimeHeader");
      sp.setMimeHeader("Content-Description", "some text2");

      TestUtil.logTrace("Getting MimeHeader");
      String sArray[] = sp.getMimeHeader("Content-Description");
      int len = sArray.length;
      if (len != 1) {
        TestUtil.logErr(
            "Error: expected only one item to be returned for getMimeHeader(Content-Description), got a total of:"
                + len);
        pass = false;
      }

      for (int i = 0; i < len; i++) {
        String temp = sArray[i];
        if (!temp.equals("some text2")) {
          TestUtil.logErr(
              "Error: received invalid value from setMimeHeader(Content-Description)");
          TestUtil.logErr("expected result: some text2");
          TestUtil.logErr("actual result:" + temp);
          pass = false;
        }
      }

      TestUtil.logTrace("Getting MimeHeader");
      sArray = sp.getMimeHeader("Content-Id");
      len = sArray.length;
      if (len != 1) {
        TestUtil.logErr(
            "Error: expected only one item to be returned for getMimeHeader(name2), got a total of:"
                + len);
        pass = false;
      }

      for (int i = 0; i < len; i++) {
        String temp = sArray[i];
        if (!temp.equals("id@abc.com")) {
          TestUtil.logErr(
              "Error: received invalid value from getMimeHeader(Content-Id)");
          TestUtil.logErr("expected result: id@abc.com");
          TestUtil.logErr("actual result:" + temp);
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

  private void setMimeHeader3Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("setMimeHeader3Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logTrace("Adding MimeHeader");
      sp.setMimeHeader("Content-Description", "some text");
      sp.setMimeHeader("Content-Description", "some text2");

      TestUtil.logTrace("Setting MimeHeader");
      sp.setMimeHeader("Content-Description", "image/jpeg");

      TestUtil.logTrace("Getting MimeHeader");
      String sArray[] = sp.getMimeHeader("Content-Description");
      int len = sArray.length;
      if (len != 1) {
        TestUtil.logErr(
            "Error: expected only one item to be returned for getMimeHeader(Content-Description), got a total of:"
                + len);
        pass = false;
      }

      for (int i = 0; i < len; i++) {
        String temp = sArray[i];
        if (!temp.equals("image/jpeg")) {
          TestUtil.logErr(
              "Error: received invalid value from getMimeHeader(Content-Description)");
          TestUtil.logErr("expected result: image/jpeg");
          TestUtil.logErr("actual result:" + temp);
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

  private void setMimeHeader4Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("setMimeHeader4Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logTrace("Setting MimeHeader");
      sp.setMimeHeader("Content-Description", "some text");

      TestUtil.logTrace("Getting MimeHeader");
      String sArray[] = sp.getMimeHeader("Content-Description");
      int len = sArray.length;
      if (len != 1) {
        TestUtil.logErr(
            "Error: expected only one item to be returned for getMimeHeader(Content-Description), got a total of:"
                + len);
        pass = false;
      }

      for (int i = 0; i < len; i++) {
        String temp = sArray[i];
        if (!temp.equals("some text")) {
          TestUtil.logErr(
              "Error: received invalid value from getMimeHeader(Content-Description)");
          TestUtil.logErr("expected result: some text");
          TestUtil.logErr("actual result:" + temp);
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

  private void setMimeHeader5Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("setMimeHeader5Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logTrace("Adding MimeHeader");
      sp.addMimeHeader("Content-Description", "some text");

      TestUtil.logTrace("Setting MimeHeader");
      sp.setMimeHeader("Content-Description", "some text2");

      TestUtil.logTrace("Setting MimeHeader again");
      sp.setMimeHeader("Content-Description", "impage/jpeg");

      TestUtil.logTrace("Getting MimeHeader");
      String sArray[] = sp.getMimeHeader("Content-Description");
      int len = sArray.length;
      if (len != 1) {
        TestUtil.logErr(
            "Error: expected only one item to be returned for getMimeHeader(Content-Description), got a total of:"
                + len);
        pass = false;
      }

      for (int i = 0; i < len; i++) {
        String temp = sArray[i];
        if (!temp.equals("impage/jpeg")) {
          TestUtil.logErr(
              "Error: received invalid value from getMimeHeader(Content-Description)");
          TestUtil.logErr("expected result: impage/jpeg");
          TestUtil.logErr("actual result:" + temp);
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
