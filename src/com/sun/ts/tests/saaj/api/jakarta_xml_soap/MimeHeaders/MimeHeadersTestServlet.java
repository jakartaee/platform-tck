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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.MimeHeaders;

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
import jakarta.xml.soap.MimeHeader;
import jakarta.xml.soap.MimeHeaders;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;

public class MimeHeadersTestServlet extends HttpServlet {
  private MessageFactory mf = null;

  private SOAPMessage msg = null;

  private SOAPPart sp = null;

  private MimeHeaders mimeHeaders = null;

  private MimeHeader mh = null;

  private void setup() throws Exception {
    TestUtil.logTrace("setup");

    SOAP_Util.setup();

    // Create a message from the message factory.
    TestUtil.logTrace("Create message from message factory");
    msg = SOAP_Util.getMessageFactory().createMessage();

    // Message creation takes care of creating the SOAPPart - a
    // required part of the message as per the SOAP 1.1
    // specification.
    sp = msg.getSOAPPart();

  }

  private void displayArray(String[] array) {
    int len = array.length;
    for (int i = 0; i < len; i++) {
      TestUtil.logTrace("array[" + i + "]=" + array[i]);
    }
  }

  private void dispatch(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("dispatch");
    String testname = SOAP_Util.getHarnessProps().getProperty("TESTNAME");
    if (testname.equals("addHeader1Test")) {
      TestUtil.logTrace("Starting addHeader1Test");
      addHeader1Test(req, res);
    } else if (testname.equals("addHeader2Test")) {
      TestUtil.logTrace("Starting addHeader2Test");
      addHeader2Test(req, res);
    } else if (testname.equals("addHeader3Test")) {
      TestUtil.logTrace("Starting addHeader3Test");
      addHeader3Test(req, res);
    } else if (testname.equals("addHeader4Test")) {
      TestUtil.logTrace("Starting addHeader4Test");
      addHeader4Test(req, res);
    } else if (testname.equals("addHeader5Test")) {
      TestUtil.logTrace("Starting addHeader5Test");
      addHeader5Test(req, res);
    } else if (testname.equals("addHeader6Test")) {
      TestUtil.logTrace("Starting addHeader6Test");
      addHeader6Test(req, res);
    } else if (testname.equals("addHeader1Test")) {
      TestUtil.logTrace("Starting addHeader1Test");
      addHeader1Test(req, res);
    } else if (testname.equals("addHeader2Test")) {
      TestUtil.logTrace("Starting addHeader2Test");
      addHeader2Test(req, res);
    } else if (testname.equals("addHeader3Test")) {
      TestUtil.logTrace("Starting addHeader3Test");
      addHeader3Test(req, res);
    } else if (testname.equals("addHeader4Test")) {
      TestUtil.logTrace("Starting addHeader4Test");
      addHeader4Test(req, res);
    } else if (testname.equals("addHeader5Test")) {
      TestUtil.logTrace("Starting addHeader5Test");
      addHeader5Test(req, res);
    } else if (testname.equals("addHeader6Test")) {
      TestUtil.logTrace("Starting addHeader6Test");
      addHeader6Test(req, res);
    } else if (testname.equals("getAllHeaders1Test")) {
      TestUtil.logTrace("Starting getAllHeaders1Test");
      getAllHeaders1Test(req, res);
    } else if (testname.equals("getAllHeaders2Test")) {
      TestUtil.logTrace("Starting getAllHeaders2Test");
      getAllHeaders2Test(req, res);
    } else if (testname.equals("getAllHeaders3Test")) {
      TestUtil.logTrace("Starting getAllHeaders3Test");
      getAllHeaders3Test(req, res);
    } else if (testname.equals("getAllHeaders4Test")) {
      TestUtil.logTrace("Starting getAllHeaders4Test");
      getAllHeaders4Test(req, res);
    } else if (testname.equals("getHeader1Test")) {
      TestUtil.logTrace("Starting getHeader1Test");
      getHeader1Test(req, res);
    } else if (testname.equals("getHeader2Test")) {
      TestUtil.logTrace("Starting getHeader2Test");
      getHeader2Test(req, res);
    } else if (testname.equals("getHeader3Test")) {
      TestUtil.logTrace("Starting getHeader3Test");
      getHeader3Test(req, res);
    } else if (testname.equals("getHeader4Test")) {
      TestUtil.logTrace("Starting getHeader4Test");
      getHeader4Test(req, res);
    } else if (testname.equals("getMatchingHeaders1Test")) {
      TestUtil.logTrace("Starting getMatchingHeaders1Test");
      getMatchingHeaders1Test(req, res);
    } else if (testname.equals("getMatchingHeaders2Test")) {
      TestUtil.logTrace("Starting getMatchingHeaders2Test");
      getMatchingHeaders2Test(req, res);
    } else if (testname.equals("getMatchingHeaders3Test")) {
      TestUtil.logTrace("Starting getMatchingHeaders3Test");
      getMatchingHeaders3Test(req, res);
    } else if (testname.equals("getMatchingHeaders4Test")) {
      TestUtil.logTrace("Starting getMatchingHeaders4Test");
      getMatchingHeaders4Test(req, res);
    } else if (testname.equals("getMatchingHeaders5Test")) {
      TestUtil.logTrace("Starting getMatchingHeaders5Test");
      getMatchingHeaders5Test(req, res);
    } else if (testname.equals("getNonMatchingHeaders1Test")) {
      TestUtil.logTrace("Starting getNonMatchingHeaders1Test");
      getNonMatchingHeaders1Test(req, res);
    } else if (testname.equals("getNonMatchingHeaders2Test")) {
      TestUtil.logTrace("Starting getNonMatchingHeaders2Test");
      getNonMatchingHeaders2Test(req, res);
    } else if (testname.equals("getNonMatchingHeaders3Test")) {
      TestUtil.logTrace("Starting getNonMatchingHeaders3Test");
      getNonMatchingHeaders3Test(req, res);
    } else if (testname.equals("getNonMatchingHeaders4Test")) {
      TestUtil.logTrace("Starting getNonMatchingHeaders4Test");
      getNonMatchingHeaders4Test(req, res);
    } else if (testname.equals("getNonMatchingHeaders5Test")) {
      TestUtil.logTrace("Starting getNonMatchingHeaders5Test");
      getNonMatchingHeaders5Test(req, res);
    } else if (testname.equals("removeAllHeaders1Test")) {
      TestUtil.logTrace("Starting removeAllHeaders1Test");
      removeAllHeaders1Test(req, res);
    } else if (testname.equals("removeAllHeaders2Test")) {
      TestUtil.logTrace("Starting removeAllHeaders2Test");
      removeAllHeaders2Test(req, res);
    } else if (testname.equals("removeAllHeaders3Test")) {
      TestUtil.logTrace("Starting removeAllHeaders3Test");
      removeAllHeaders3Test(req, res);
    } else if (testname.equals("removeAllHeaders4Test")) {
      TestUtil.logTrace("Starting removeAllHeaders4Test");
      removeAllHeaders4Test(req, res);
    } else if (testname.equals("removeHeader1Test")) {
      TestUtil.logTrace("Starting removeHeader1Test");
      removeHeader1Test(req, res);
    } else if (testname.equals("removeHeader2Test")) {
      TestUtil.logTrace("Starting removeHeader2Test");
      removeHeader2Test(req, res);
    } else if (testname.equals("removeHeader3Test")) {
      TestUtil.logTrace("Starting removeHeader3Test");
      removeHeader3Test(req, res);
    } else if (testname.equals("removeHeader4Test")) {
      TestUtil.logTrace("Starting removeHeader4Test");
      removeHeader4Test(req, res);
    } else if (testname.equals("setHeader1Test")) {
      TestUtil.logMsg("Starting setHeader1Test");
      setHeader1Test(req, res);
    } else if (testname.equals("setHeader2Test")) {
      TestUtil.logMsg("Starting setHeader2Test");
      setHeader2Test(req, res);
    } else if (testname.equals("setHeader3Test")) {
      TestUtil.logMsg("Starting setHeader3Test");
      setHeader3Test(req, res);
    } else if (testname.equals("setHeader4Test")) {
      TestUtil.logMsg("Starting setHeader4Test");
      setHeader4Test(req, res);
    } else if (testname.equals("setHeader5Test")) {
      TestUtil.logMsg("Starting setHeader5Test");
      setHeader5Test(req, res);
    } else if (testname.equals("setHeader6Test")) {
      TestUtil.logMsg("Starting setHeader6Test");
      setHeader6Test(req, res);
    } else {
      throw new ServletException(
          "The testname '" + testname + "' was not found in the test servlet");

    }
  }

  public void init(ServletConfig servletConfig) throws ServletException {
    super.init(servletConfig);
    System.out.println("MimeHeadersTestServlet:init (Entering)");
    SOAP_Util.doServletInit(servletConfig);
    System.out.println("MimeHeadersTestServlet:init (Leaving)");
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

  private void addHeader1Test(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("addHeader1Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating MimeHeaders object ...");
      mimeHeaders = new MimeHeaders();

      TestUtil.logMsg("Adding header");
      mimeHeaders.addHeader("Content-Description", "some text");

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

  private void addHeader2Test(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("addHeader2Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating MimeHeaders object ...");
      mimeHeaders = new MimeHeaders();

      TestUtil.logMsg("Adding header");
      mimeHeaders.addHeader("Content-Description", "some text");
      mimeHeaders.addHeader("Content-Id", "id@abc.com");

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

  private void addHeader3Test(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("addHeader3Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating MimeHeaders object ...");
      mimeHeaders = new MimeHeaders();

      TestUtil.logMsg("Adding header");
      mimeHeaders.addHeader("Content-Description", "some text");
      mimeHeaders.addHeader("Content-Description", "some text2");

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

  private void addHeader4Test(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("addHeader4Test");
    Properties resultProps = new Properties();
    boolean pass = false;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating MimeHeaders object ...");
      mimeHeaders = new MimeHeaders();

      TestUtil.logMsg("Adding header");
      mimeHeaders.addHeader(null, "some text");

      TestUtil.logErr(
          "Error: expected java.lang.IllegalArgumentException to be thrown");
      pass = false;
    } catch (java.lang.IllegalArgumentException ia) {
      pass = true;
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

  private void addHeader5Test(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("addHeader5Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating MimeHeaders object ...");
      mimeHeaders = new MimeHeaders();

      TestUtil.logMsg("Adding header with null value");
      mimeHeaders.addHeader("Content-Description", null);
    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    }
    try {
      setup();

      TestUtil.logMsg("Creating MimeHeaders object ...");
      mimeHeaders = new MimeHeaders();

      TestUtil.logMsg("Adding header with null string value");
      mimeHeaders.addHeader("Content-Description", "");
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

  private void addHeader6Test(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("addHeader6Test");
    Properties resultProps = new Properties();
    boolean pass = false;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating MimeHeaders object ...");
      mimeHeaders = new MimeHeaders();

      TestUtil.logMsg("Adding header");
      mimeHeaders.addHeader(null, null);

      TestUtil.logErr(
          "Error: expected java.lang.IllegalArgumentException to be thrown");
      pass = false;
    } catch (java.lang.IllegalArgumentException ia) {
      pass = true;
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

  private void getAllHeaders1Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getAllHeaders1Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating MimeHeaders object ...");
      mimeHeaders = new MimeHeaders();

      TestUtil.logMsg("Adding header");
      mimeHeaders.addHeader("Content-Description", "some text");

      Iterator iterator = mimeHeaders.getAllHeaders();
      int cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        mh = (MimeHeader) iterator.next();
        String name = mh.getName();
        String value = mh.getValue();
        if (!(name.equals("Content-Description")
            && value.equals("some text"))) {
          TestUtil.logErr("Mimeheader did not match");
          TestUtil.logErr("received: name=" + name + ", value=" + value);
          pass = false;
        } else {
          TestUtil.logMsg("MimeHeader did match");
        }
      }

      if (cnt != 1) {
        TestUtil.logErr(
            "Error: expected only one item to be returned, got a total of:"
                + cnt);
        pass = false;
      }

      TestUtil.logMsg("Try a second time");
      Iterator iterator2 = mimeHeaders.getAllHeaders();
      int cnt2 = 0;
      while (iterator2.hasNext()) {
        cnt2++;
        mh = (MimeHeader) iterator2.next();
        String name = mh.getName();
        String value = mh.getValue();
        if (!(name.equals("Content-Description")
            && value.equals("some text"))) {
          TestUtil.logErr("Mimeheader did not match, second time through");
          TestUtil.logErr("received: name=" + name + ", value=" + value);
          pass = false;
        } else {
          TestUtil.logMsg("MimeHeader did match second time through");
        }
      }
      if (cnt2 != 1) {
        TestUtil.logErr(
            "Error: expected only one item to be returned second time through, got a total of:"
                + cnt2);
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

  private void getAllHeaders2Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getAllHeaders2Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating MimeHeaders object ...");
      mimeHeaders = new MimeHeaders();

      TestUtil.logMsg("Adding header");
      mimeHeaders.addHeader("Content-Description", "some text");
      mimeHeaders.addHeader("Content-Id", "id@abc.com");

      Iterator iterator = mimeHeaders.getAllHeaders();
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
            TestUtil.logErr("Error: Received the same header1 header twice");
            TestUtil.logErr("received: name=" + name + ", value=" + value);
            pass = false;
          }
        } else if (name.equals("Content-Id") && value.equals("id@abc.com")) {
          if (!foundHeader2) {
            foundHeader2 = true;
            TestUtil.logMsg("MimeHeaders do match for header2");
            TestUtil.logMsg("receive: name=" + name + ", value=" + value);
          } else {
            TestUtil.logErr("Error: Received the same header2 header twice");
            TestUtil.logErr("received: name=" + name + ", value=" + value);
            pass = false;
          }
        } else {
          TestUtil.logErr("Error: Received an invalid header");
          TestUtil.logErr("received: name=" + name + ", value=" + value);
          pass = false;
        }
      }

      if (!(foundHeader1 && foundHeader2)) {
        TestUtil.logErr("Error: did not receive both headers");
        pass = false;
      }

      if (cnt != 2) {
        TestUtil.logErr(
            "Error: expected two items to be returned, got a total of:" + cnt);
        pass = false;
      }

      TestUtil.logMsg("Try a second time");
      Iterator iterator2 = mimeHeaders.getAllHeaders();
      int cnt2 = 0;
      foundHeader1 = false;
      foundHeader2 = false;
      while (iterator2.hasNext()) {
        cnt2++;
        mh = (MimeHeader) iterator2.next();
        String name = mh.getName();
        String value = mh.getValue();
        if (name.equals("Content-Description") && value.equals("some text")) {
          if (!foundHeader1) {
            foundHeader1 = true;
            TestUtil.logMsg(
                "MimeHeaders do match for header1, second time through");
            TestUtil.logMsg("receive: name=" + name + ", value=" + value);
          } else {
            TestUtil.logErr(
                "Error: Received the same header1 header twice, second time through");
            TestUtil.logErr("received: name=" + name + ", value=" + value);
            pass = false;
          }
        } else if (name.equals("Content-Id") && value.equals("id@abc.com")) {
          if (!foundHeader2) {
            foundHeader2 = true;
            TestUtil.logMsg(
                "MimeHeaders do match for header2, second time through");
            TestUtil.logMsg("receive: name=" + name + ", value=" + value);
          } else {
            TestUtil.logErr(
                "Error: Received the same header2 header twice, second time through");
            TestUtil.logErr("received: name=" + name + ", value=" + value);
            pass = false;
          }
        } else {
          TestUtil.logErr(
              "Error: Received an invalid header , the second time through");
          TestUtil.logErr("received: name=" + name + ", value=" + value);
          pass = false;
        }
      }

      if (!(foundHeader1 && foundHeader2)) {
        TestUtil
            .logErr("Error: did not receive both headers, second time through");
        pass = false;
      }

      if (cnt != 2) {
        TestUtil.logErr(
            "Error: expected two items to be returned second time through, got a total of:"
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

  private void getAllHeaders3Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getAllHeaders3Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating MimeHeaders object ...");
      mimeHeaders = new MimeHeaders();

      TestUtil.logMsg("Adding header");
      mimeHeaders.addHeader("Content-Description", "some text");
      mimeHeaders.addHeader("Content-Description", "some text2");

      Iterator iterator = mimeHeaders.getAllHeaders();
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
            TestUtil.logErr("Error: Received the same header1 header twice");
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
            TestUtil.logErr("Error: Received the same header2 header twice");
            TestUtil.logErr("received: name=" + name + ", value=" + value);
            pass = false;
          }
        } else {
          TestUtil.logErr("Error: Received an invalid header");
          TestUtil.logErr("received: name=" + name + ", value=" + value);
          pass = false;
        }
      }

      if (!(foundHeader1 && foundHeader2)) {
        TestUtil.logErr("Error: did not receive both headers");
        pass = false;
      }
      if (cnt != 2) {
        TestUtil.logErr(
            "Error: expected two items to be returned, got a total of:" + cnt);
        pass = false;
      }

      TestUtil.logMsg("Try a second time");
      Iterator iterator2 = mimeHeaders.getAllHeaders();
      int cnt2 = 0;
      foundHeader1 = false;
      foundHeader2 = false;
      while (iterator2.hasNext()) {
        cnt2++;
        mh = (MimeHeader) iterator2.next();
        String name = mh.getName();
        String value = mh.getValue();
        if (name.equals("Content-Description") && value.equals("some text")) {
          if (!foundHeader1) {
            foundHeader1 = true;
            TestUtil.logMsg(
                "MimeHeaders do match for header1, second time through");
            TestUtil.logMsg("receive: name=" + name + ", value=" + value);
          } else {
            TestUtil.logErr(
                "Error: Received the same header1 header twice, second time through");
            TestUtil.logErr("received: name=" + name + ", value=" + value);
            pass = false;
          }
        } else if (name.equals("Content-Description")
            && value.equals("some text2")) {
          if (!foundHeader2) {
            foundHeader2 = true;
            TestUtil.logMsg(
                "MimeHeaders do match for header2, second time through");
            TestUtil.logMsg("receive: name=" + name + ", value=" + value);
          } else {
            TestUtil.logErr(
                "Error: Received the same header2 header twice, second time through");
            TestUtil.logErr("received: name=" + name + ", value=" + value);
            pass = false;
          }
        } else {
          TestUtil.logErr(
              "Error: Received an invalid header , the second time through");
          TestUtil.logErr("received: name=" + name + ", value=" + value);
          pass = false;
        }
      }

      if (!(foundHeader1 && foundHeader2)) {
        TestUtil
            .logErr("Error: did not receive both headers, second time through");
        pass = false;
      }

      if (cnt != 2) {
        TestUtil.logErr(
            "Error: expected two items to be returned second time through, got a total of:"
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

  private void getAllHeaders4Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getAllHeaders4Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating MimeHeaders object ...");
      mimeHeaders = new MimeHeaders();

      Iterator iterator = mimeHeaders.getAllHeaders();
      int cnt = 0;
      boolean foundHeader1 = false;
      boolean foundHeader2 = false;
      while (iterator.hasNext()) {
        cnt++;
        mh = (MimeHeader) iterator.next();
        String name = mh.getName();
        String value = mh.getValue();
        TestUtil.logErr("Error: Received an invalid header");
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

  private void getHeader1Test(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("getHeader1Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating MimeHeaders object ...");
      mimeHeaders = new MimeHeaders();

      TestUtil.logMsg("Adding header");
      mimeHeaders.addHeader("Content-Description", "some text");

      String sArray[] = mimeHeaders.getHeader("Content-Description");
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
              "Error: received invalid value from getHeader(Content-Description)");
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

  private void getHeader2Test(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("getHeader2Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating MimeHeaders object ...");
      mimeHeaders = new MimeHeaders();

      TestUtil.logMsg("Adding header");
      mimeHeaders.addHeader("Content-Description", "some text");
      mimeHeaders.addHeader("Content-Id", "id@abc.com");
      String sArray[] = mimeHeaders.getHeader("Content-Description");
      int len = sArray.length;
      if (len != 1) {
        TestUtil.logErr(
            "Error: expected only one item to be returned for getHeader(Content-Description), got a total of:"
                + len);
        pass = false;
      }

      for (int i = 0; i < len; i++) {
        String temp = sArray[i];
        if (!temp.equals("some text")) {
          TestUtil.logErr(
              "Error: received invalid value from getHeader(Content-Description)");
          TestUtil.logErr("expected result: some text");
          TestUtil.logErr("actual result:" + temp);
          pass = false;
        }
      }

      sArray = mimeHeaders.getHeader("Content-Id");
      len = sArray.length;
      if (len != 1) {
        TestUtil.logErr(
            "Error: expected only one item to be returned for getHeader(Content-Id), got a total of:"
                + len);
        pass = false;
      }

      for (int i = 0; i < len; i++) {
        String temp = sArray[i];
        if (!temp.equals("id@abc.com")) {
          TestUtil.logErr(
              "Error: received invalid value from getHeader(Content-Id)");
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

  private void getHeader3Test(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("getHeader3Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating MimeHeaders object ...");
      mimeHeaders = new MimeHeaders();

      TestUtil.logMsg("Adding header");
      mimeHeaders.addHeader("Content-Description", "some text");
      mimeHeaders.addHeader("Content-Description", "some text2");
      String sArray[] = mimeHeaders.getHeader("Content-Description");
      int len = sArray.length;
      if (len != 2) {
        TestUtil.logErr(
            "Error: expected only one item to be returned for getHeader(Content-Description), got a total of:"
                + len);
        pass = false;
      }

      for (int i = 0; i < len; i++) {
        String temp = sArray[i];
        if (!temp.equals("some text") && !temp.equals("some text2")) {
          TestUtil.logErr(
              "Error: received invalid value from getHeader(Content-Description)");
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

  private void getHeader4Test(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("getHeader4Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating MimeHeaders object ...");
      mimeHeaders = new MimeHeaders();

      TestUtil.logMsg("Adding header");
      mimeHeaders.addHeader("Content-Description", "some text");

      TestUtil.logMsg("Getting non-existent header");
      String sArray[] = mimeHeaders.getHeader("doesnotexist");
      if (sArray != null && sArray.length > 0) {
        TestUtil.logErr("Error: was able to get a non-existent Header");
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

  private void getMatchingHeaders1Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getMatchingHeaders1Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating MimeHeaders object ...");
      mimeHeaders = new MimeHeaders();

      TestUtil.logMsg("Adding header");
      mimeHeaders.addHeader("Content-Description", "some text");

      Iterator iterator = null;
      int cnt = 0;

      TestUtil.logMsg("Getting all headers");
      iterator = mimeHeaders.getAllHeaders();
      cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        mh = (MimeHeader) iterator.next();
        String name = mh.getName();
        String value = mh.getValue();
        TestUtil.logMsg("received: name=" + name + ", value=" + value);
      }

      String sArray[] = { "Content-Description" };
      TestUtil.logMsg("List of Matching headers contains:");
      displayArray(sArray);

      TestUtil.logMsg("Getting matching headers");
      iterator = mimeHeaders.getMatchingHeaders(sArray);
      cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        mh = (MimeHeader) iterator.next();
        String name = mh.getName();
        String value = mh.getValue();
        if (name.equals("Content-Description") && value.equals("some text")) {
          TestUtil.logMsg("MimeHeaders do match ");
          TestUtil.logMsg("receive: name=" + name + ", value=" + value);
        } else {
          TestUtil.logErr("Error: Received an invalid header");
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

  private void getMatchingHeaders2Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getMatchingHeaders2Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating MimeHeaders object ...");
      mimeHeaders = new MimeHeaders();

      TestUtil.logMsg("Adding header");
      mimeHeaders.addHeader("Content-Description", "some text");
      mimeHeaders.addHeader("Content-Id", "id@abc.com");

      Iterator iterator = null;
      int cnt = 0;

      TestUtil.logMsg("Getting all headers");
      iterator = mimeHeaders.getAllHeaders();
      cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        mh = (MimeHeader) iterator.next();
        String name = mh.getName();
        String value = mh.getValue();
        TestUtil.logMsg("received: name=" + name + ", value=" + value);
      }

      String sArray[] = { "Content-Description" };
      TestUtil.logMsg("List of Matching headers contains:");
      displayArray(sArray);

      TestUtil.logMsg("Getting matching headers");
      iterator = mimeHeaders.getMatchingHeaders(sArray);
      cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        mh = (MimeHeader) iterator.next();
        String name = mh.getName();
        String value = mh.getValue();
        if (name.equals("Content-Description") && value.equals("some text")) {
          TestUtil.logMsg("MimeHeaders do match ");
          TestUtil.logMsg("receive: name=" + name + ", value=" + value);
        } else {
          TestUtil.logErr("Error: Received an invalid header");
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

  private void getMatchingHeaders3Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getMatchingHeaders3Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating MimeHeaders object ...");
      mimeHeaders = new MimeHeaders();

      TestUtil.logMsg("Adding header");
      mimeHeaders.addHeader("Content-Description", "some text");
      mimeHeaders.addHeader("Content-Description", "some text2");

      Iterator iterator = null;
      int cnt = 0;

      TestUtil.logMsg("Getting all headers");
      iterator = mimeHeaders.getAllHeaders();
      cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        mh = (MimeHeader) iterator.next();
        String name = mh.getName();
        String value = mh.getValue();
        TestUtil.logMsg("received: name=" + name + ", value=" + value);
      }

      String sArray[] = { "Content-Description" };
      TestUtil.logMsg("List of Matching headers contains:");
      displayArray(sArray);

      TestUtil.logMsg("Getting matching headers");
      iterator = mimeHeaders.getMatchingHeaders(sArray);
      cnt = 0;
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
            TestUtil.logErr("Error: Received the same header1 header twice");
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
            TestUtil.logErr("Error: Received the same header2 header twice");
            TestUtil.logErr("received: name=" + name + ", value=" + value);
            pass = false;
          }
        } else {
          TestUtil.logErr("Error: Received an invalid header");
          TestUtil.logErr("received: name=" + name + ", value=" + value);
          pass = false;
        }
      }

      if (!(foundHeader1 && foundHeader2)) {
        TestUtil.logErr("Error: did not receive both headers");
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

  private void getMatchingHeaders4Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getMatchingHeaders4Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating MimeHeaders object ...");
      mimeHeaders = new MimeHeaders();

      TestUtil.logMsg("Adding header");
      mimeHeaders.addHeader("Content-Description", "some text");

      Iterator iterator = null;
      int cnt = 0;

      TestUtil.logMsg("Getting all headers");
      iterator = mimeHeaders.getAllHeaders();
      cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        mh = (MimeHeader) iterator.next();
        String name = mh.getName();
        String value = mh.getValue();
        TestUtil.logMsg("received: name=" + name + ", value=" + value);
      }

      String sArray[] = { "doesnotexist" };
      TestUtil.logMsg("List of Matching headers contains:");
      displayArray(sArray);

      TestUtil.logMsg("Getting non-existent header");
      iterator = mimeHeaders.getMatchingHeaders(sArray);
      cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        mh = (MimeHeader) iterator.next();
        String name = mh.getName();
        String value = mh.getValue();
        TestUtil.logErr("Error: Received an invalid header");
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

  private void getMatchingHeaders5Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getMatchingHeaders5Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating MimeHeaders object ...");
      mimeHeaders = new MimeHeaders();

      TestUtil.logMsg("Adding header");
      mimeHeaders.addHeader("Content-Description", "some text");
      mimeHeaders.addHeader("Content-Description", "some text2");
      mimeHeaders.addHeader("Content-Id", "id@abc.com");

      Iterator iterator = null;
      int cnt = 0;

      TestUtil.logMsg("Getting all headers");
      iterator = mimeHeaders.getAllHeaders();
      cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        mh = (MimeHeader) iterator.next();
        String name = mh.getName();
        String value = mh.getValue();
        TestUtil.logMsg("received: name=" + name + ", value=" + value);
      }

      String sArray[] = { "Content-Description", "Content-Location" };
      TestUtil.logMsg("List of Matching headers contains:");
      displayArray(sArray);

      TestUtil.logMsg("Getting matching headers");
      iterator = mimeHeaders.getMatchingHeaders(sArray);
      cnt = 0;
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
            TestUtil.logErr("Error: Received the same header1 header twice");
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
            TestUtil.logErr("Error: Received the same header2 header twice");
            TestUtil.logErr("received: name=" + name + ", value=" + value);
            pass = false;
          }
        } else {
          TestUtil.logErr("Error: Received an invalid header");
          TestUtil.logErr("received: name=" + name + ", value=" + value);
          pass = false;
        }
      }

      if (!(foundHeader1 && foundHeader2)) {
        TestUtil.logErr("Error: did not receive both headers");
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

  private void getNonMatchingHeaders1Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getNonMatchingHeaders1Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating MimeHeaders object ...");
      mimeHeaders = new MimeHeaders();

      TestUtil.logMsg("Adding header");
      mimeHeaders.addHeader("Content-Description", "some text");

      Iterator iterator = null;
      int cnt = 0;

      TestUtil.logMsg("Getting all headers");
      iterator = mimeHeaders.getAllHeaders();
      cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        mh = (MimeHeader) iterator.next();
        String name = mh.getName();
        String value = mh.getValue();
        TestUtil.logMsg("received: name=" + name + ", value=" + value);
      }

      String sArray[] = { "Content-Description" };
      TestUtil.logMsg("List of Non Matching headers contains:");
      displayArray(sArray);

      TestUtil.logMsg("Getting non matching headers");
      iterator = mimeHeaders.getNonMatchingHeaders(sArray);
      cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        mh = (MimeHeader) iterator.next();
        String name = mh.getName();
        String value = mh.getValue();
        TestUtil.logErr("Error: Received an invalid header");
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

  private void getNonMatchingHeaders2Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getNonMatchingHeaders2Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating MimeHeaders object ...");
      mimeHeaders = new MimeHeaders();

      TestUtil.logMsg("Adding header");
      mimeHeaders.addHeader("Content-Description", "some text");
      mimeHeaders.addHeader("Content-Id", "id@abc.com");

      Iterator iterator = null;
      int cnt = 0;

      TestUtil.logMsg("Getting all headers");
      iterator = mimeHeaders.getAllHeaders();
      cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        mh = (MimeHeader) iterator.next();
        String name = mh.getName();
        String value = mh.getValue();
        TestUtil.logMsg("received: name=" + name + ", value=" + value);
      }

      String sArray[] = { "Content-Id" };
      TestUtil.logMsg("List of Non Matching headers contains:");
      displayArray(sArray);

      TestUtil.logMsg("Getting non matching headers");
      iterator = mimeHeaders.getNonMatchingHeaders(sArray);
      cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        mh = (MimeHeader) iterator.next();
        String name = mh.getName();
        String value = mh.getValue();
        if (name.equals("Content-Description") && value.equals("some text")) {
          TestUtil.logMsg("MimeHeaders do match ");
          TestUtil.logMsg("receive: name=" + name + ", value=" + value);
        } else {
          TestUtil.logErr("Error: Received an invalid header");
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

  private void getNonMatchingHeaders3Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getNonMatchingHeaders3Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating MimeHeaders object ...");
      mimeHeaders = new MimeHeaders();

      TestUtil.logMsg("Adding header");
      mimeHeaders.addHeader("Content-Description", "some text");
      mimeHeaders.addHeader("Content-Description", "some text2");

      Iterator iterator = null;
      int cnt = 0;

      TestUtil.logMsg("Getting all headers");
      iterator = mimeHeaders.getAllHeaders();
      cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        mh = (MimeHeader) iterator.next();
        String name = mh.getName();
        String value = mh.getValue();
        TestUtil.logMsg("received: name=" + name + ", value=" + value);
      }

      String sArray[] = { "Content-Id" };
      TestUtil.logMsg("List of Non Matching headers contains:");
      displayArray(sArray);

      TestUtil.logMsg("Getting non matching headers");
      iterator = mimeHeaders.getNonMatchingHeaders(sArray);
      cnt = 0;
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
            TestUtil.logErr("Error: Received the same header1 header twice");
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
            TestUtil.logErr("Error: Received the same header2 header twice");
            TestUtil.logErr("received: name=" + name + ", value=" + value);
            pass = false;
          }
        } else {
          TestUtil.logErr("Error: Received an invalid header");
          TestUtil.logErr("received: name=" + name + ", value=" + value);
          pass = false;
        }
      }

      if (!(foundHeader1 && foundHeader2)) {
        TestUtil.logErr("Error: did not receive both headers");
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

  private void getNonMatchingHeaders4Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getNonMatchingHeaders4Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating MimeHeaders object ...");
      mimeHeaders = new MimeHeaders();

      TestUtil.logMsg("Adding header");
      mimeHeaders.addHeader("Content-Description", "some text");

      Iterator iterator = null;
      int cnt = 0;

      TestUtil.logMsg("Getting all headers");
      iterator = mimeHeaders.getAllHeaders();
      cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        mh = (MimeHeader) iterator.next();
        String name = mh.getName();
        String value = mh.getValue();
        TestUtil.logMsg("received: name=" + name + ", value=" + value);
      }

      String sArray[] = { "Content-Description" };
      TestUtil.logMsg("List of Non Matching headers contains:");
      displayArray(sArray);

      TestUtil.logMsg("Getting non matching headers");
      iterator = mimeHeaders.getNonMatchingHeaders(sArray);
      cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        mh = (MimeHeader) iterator.next();
        String name = mh.getName();
        String value = mh.getValue();
        TestUtil.logErr("Error: Received an invalid header");
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

  private void getNonMatchingHeaders5Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("getNonMatchingHeaders5Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating MimeHeaders object ...");
      mimeHeaders = new MimeHeaders();

      TestUtil.logMsg("Adding header");
      mimeHeaders.addHeader("Content-Description", "some text");
      mimeHeaders.addHeader("Content-Description", "some text2");
      mimeHeaders.addHeader("Content-Id", "id@abc.com");

      Iterator iterator = null;
      int cnt = 0;

      TestUtil.logMsg("Getting all headers");
      iterator = mimeHeaders.getAllHeaders();
      cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        mh = (MimeHeader) iterator.next();
        String name = mh.getName();
        String value = mh.getValue();
        TestUtil.logMsg("received: name=" + name + ", value=" + value);
      }

      String sArray[] = { "Content-Id", "Content-Location" };
      TestUtil.logMsg("List of Non Matching headers contains:");
      displayArray(sArray);

      TestUtil.logMsg("Getting non matching headers");
      iterator = mimeHeaders.getNonMatchingHeaders(sArray);
      cnt = 0;
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
            TestUtil.logErr("Error: Received the same header1 header twice");
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
            TestUtil.logErr("Error: Received the same header2 header twice");
            TestUtil.logErr("received: name=" + name + ", value=" + value);
            pass = false;
          }
        } else {
          TestUtil.logErr("Error: Received an invalid header");
          TestUtil.logErr("received: name=" + name + ", value=" + value);
          pass = false;
        }
      }

      if (!(foundHeader1 && foundHeader2)) {
        TestUtil.logErr("Error: did not receive both headers");
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

  private void removeAllHeaders1Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("removeAllHeaders1Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating MimeHeaders object ...");
      mimeHeaders = new MimeHeaders();

      TestUtil.logMsg("Adding header");
      mimeHeaders.addHeader("Content-Description", "some text");

      TestUtil.logMsg("Removing all MimeHeaders object ...");
      mimeHeaders.removeAllHeaders();

      TestUtil.logMsg("Getting all MimeHeaders object ...");
      Iterator iterator = mimeHeaders.getAllHeaders();
      int cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        mh = (MimeHeader) iterator.next();
        String name = mh.getName();
        String value = mh.getValue();
        TestUtil.logErr("Received invalid Mimeheader");
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

  private void removeAllHeaders2Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("removeAllHeaders2Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating MimeHeaders object ...");
      mimeHeaders = new MimeHeaders();

      TestUtil.logMsg("Adding header");
      mimeHeaders.addHeader("Content-Description", "some text");
      mimeHeaders.addHeader("Content-Id", "id@abc.com");

      TestUtil.logMsg("Removing all MimeHeaders object ...");
      mimeHeaders.removeAllHeaders();

      TestUtil.logMsg("Getting all MimeHeaders object ...");
      Iterator iterator = mimeHeaders.getAllHeaders();
      int cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        mh = (MimeHeader) iterator.next();
        String name = mh.getName();
        String value = mh.getValue();
        TestUtil.logErr("Received invalid Mimeheader");
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

  private void removeAllHeaders3Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("removeAllHeaders3Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating MimeHeaders object ...");
      mimeHeaders = new MimeHeaders();

      TestUtil.logMsg("Adding header");
      mimeHeaders.addHeader("Content-Description", "some text");
      mimeHeaders.addHeader("Content-Description", "some text2");

      TestUtil.logMsg("Removing all MimeHeaders object ...");
      mimeHeaders.removeAllHeaders();

      TestUtil.logMsg("Getting all MimeHeaders object ...");
      Iterator iterator = mimeHeaders.getAllHeaders();
      int cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        mh = (MimeHeader) iterator.next();
        String name = mh.getName();
        String value = mh.getValue();
        TestUtil.logErr("Received invalid Mimeheader");
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

  private void removeAllHeaders4Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("removeAllHeaders4Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating MimeHeaders object ...");
      mimeHeaders = new MimeHeaders();

      TestUtil.logMsg("Removing all MimeHeaders object ...");
      mimeHeaders.removeAllHeaders();

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

  private void removeHeader1Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("removeHeader1Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating MimeHeaders object ...");
      mimeHeaders = new MimeHeaders();

      TestUtil.logMsg("Adding header");
      mimeHeaders.addHeader("Content-Description", "some text");

      TestUtil.logMsg("Removing header");
      mimeHeaders.removeHeader("Content-Description");

      TestUtil.logMsg("Getting headers");
      Iterator iterator = mimeHeaders.getAllHeaders();
      int cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        mh = (MimeHeader) iterator.next();
        String name = mh.getName();
        String value = mh.getValue();
        TestUtil.logErr("Received invalid Mimeheader");
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

  private void removeHeader2Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("removeHeader2Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating MimeHeaders object ...");
      mimeHeaders = new MimeHeaders();

      TestUtil.logMsg("Adding header");
      mimeHeaders.addHeader("Content-Description", "some text");
      mimeHeaders.addHeader("Content-Id", "id@abc.com");

      TestUtil.logMsg("Removing header");
      mimeHeaders.removeHeader("Content-Id");

      TestUtil.logMsg("Getting headers");
      Iterator iterator = mimeHeaders.getAllHeaders();
      int cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        mh = (MimeHeader) iterator.next();
        String name = mh.getName();
        String value = mh.getValue();
        if (!(name.equals("Content-Description")
            && value.equals("some text"))) {
          TestUtil.logErr("Mimeheader did not match");
          TestUtil.logErr("received: name=" + name + ", value=" + value);
          pass = false;
        } else {
          TestUtil.logMsg("MimeHeader did match");
        }
      }

      if (cnt != 1) {
        TestUtil.logErr(
            "Error: expected only one item to be returned, got a total of:"
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

  private void removeHeader3Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("removeHeader3Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating MimeHeaders object ...");
      mimeHeaders = new MimeHeaders();

      TestUtil.logMsg("Adding header");
      mimeHeaders.addHeader("Content-Description", "some text");
      mimeHeaders.addHeader("Content-Description", "some text2");

      TestUtil.logMsg("Removing header");
      mimeHeaders.removeHeader("Content-Description");

      TestUtil.logMsg("Getting headers");
      Iterator iterator = mimeHeaders.getAllHeaders();
      int cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        mh = (MimeHeader) iterator.next();
        String name = mh.getName();
        String value = mh.getValue();
        TestUtil.logErr("Received invalid Mimeheader");
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

  private void removeHeader4Test(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    TestUtil.logTrace("removeHeader4Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating MimeHeaders object ...");
      mimeHeaders = new MimeHeaders();

      TestUtil.logMsg("Removing header");
      mimeHeaders.removeHeader("doesnotexist");

      TestUtil.logMsg("Getting header");
      Iterator iterator = mimeHeaders.getAllHeaders();
      int cnt = 0;
      while (iterator.hasNext()) {
        cnt++;
        mh = (MimeHeader) iterator.next();
        String name = mh.getName();
        String value = mh.getValue();
        TestUtil.logErr("Received invalid Mimeheader");
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

  private void setHeader1Test(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("setHeader1Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating MimeHeaders object ...");
      mimeHeaders = new MimeHeaders();

      TestUtil.logMsg("Adding header");
      mimeHeaders.addHeader("Content-Description", "some text");

      TestUtil.logMsg("Setting header");
      mimeHeaders.setHeader("Content-Description", "some text2");

      TestUtil.logMsg("Getting header");
      String sArray[] = mimeHeaders.getHeader("Content-Description");
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
              "Error: received invalid value from setHeader(Content-Description)");
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

  private void setHeader2Test(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("setHeader2Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating MimeHeaders object ...");
      mimeHeaders = new MimeHeaders();

      TestUtil.logMsg("Adding header");
      mimeHeaders.addHeader("Content-Description", "some text");
      mimeHeaders.addHeader("Content-Id", "id@abc.com");

      TestUtil.logMsg("Setting header");
      mimeHeaders.setHeader("Content-Description", "some text2");

      TestUtil.logMsg("Getting header");
      String sArray[] = mimeHeaders.getHeader("Content-Description");
      int len = sArray.length;
      if (len != 1) {
        TestUtil.logErr(
            "Error: expected only one item to be returned for getHeader(Content-Description), got a total of:"
                + len);
        pass = false;
      }

      for (int i = 0; i < len; i++) {
        String temp = sArray[i];
        if (!temp.equals("some text2")) {
          TestUtil.logErr(
              "Error: received invalid value from setHeader(Content-Description)");
          TestUtil.logErr("expected result: some text2");
          TestUtil.logErr("actual result:" + temp);
          pass = false;
        }
      }

      TestUtil.logMsg("Getting header");
      sArray = mimeHeaders.getHeader("Content-Id");
      len = sArray.length;
      if (len != 1) {
        TestUtil.logErr(
            "Error: expected only one item to be returned for getHeader(name2), got a total of:"
                + len);
        pass = false;
      }

      for (int i = 0; i < len; i++) {
        String temp = sArray[i];
        if (!temp.equals("id@abc.com")) {
          TestUtil.logErr(
              "Error: received invalid value from getHeader(Content-Id)");
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

  private void setHeader3Test(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("setHeader3Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating MimeHeaders object ...");
      mimeHeaders = new MimeHeaders();

      TestUtil.logMsg("Adding header");
      mimeHeaders.setHeader("Content-Description", "some text");
      mimeHeaders.setHeader("Content-Description", "some text2");

      TestUtil.logMsg("Setting header");
      mimeHeaders.setHeader("Content-Description", "image/jpeg");

      TestUtil.logMsg("Getting header");
      String sArray[] = mimeHeaders.getHeader("Content-Description");
      int len = sArray.length;
      if (len != 1) {
        TestUtil.logErr(
            "Error: expected only one item to be returned for getHeader(Content-Description), got a total of:"
                + len);
        pass = false;
      }

      for (int i = 0; i < len; i++) {
        String temp = sArray[i];
        if (!temp.equals("image/jpeg")) {
          TestUtil.logErr(
              "Error: received invalid value from getHeader(Content-Description)");
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

  private void setHeader4Test(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("setHeader4Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating MimeHeaders object ...");
      mimeHeaders = new MimeHeaders();

      TestUtil.logMsg("Setting header");
      mimeHeaders.setHeader("Content-Description", "some text");

      TestUtil.logMsg("Getting header");
      String sArray[] = mimeHeaders.getHeader("Content-Description");
      int len = sArray.length;
      if (len != 1) {
        TestUtil.logErr(
            "Error: expected only one item to be returned for getHeader(Content-Description), got a total of:"
                + len);
        pass = false;
      }

      for (int i = 0; i < len; i++) {
        String temp = sArray[i];
        if (!temp.equals("some text")) {
          TestUtil.logErr(
              "Error: received invalid value from getHeader(Content-Description)");
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

  private void setHeader5Test(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("setHeader5Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating MimeHeaders object ...");
      mimeHeaders = new MimeHeaders();

      TestUtil.logMsg("Adding header");
      mimeHeaders.addHeader("Content-Description", "some text");

      TestUtil.logMsg("Setting header");
      mimeHeaders.setHeader("Content-Description", "some text2");

      TestUtil.logMsg("Setting header again");
      mimeHeaders.setHeader("Content-Description", "impage/jpeg");

      TestUtil.logMsg("Getting header");
      String sArray[] = mimeHeaders.getHeader("Content-Description");
      int len = sArray.length;
      if (len != 1) {
        TestUtil.logErr(
            "Error: expected only one item to be returned for getHeader(Content-Description), got a total of:"
                + len);
        pass = false;
      }

      for (int i = 0; i < len; i++) {
        String temp = sArray[i];
        if (!temp.equals("impage/jpeg")) {
          TestUtil.logErr(
              "Error: received invalid value from getHeader(Content-Description)");
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

  private void setHeader6Test(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("setHeader6Test");
    Properties resultProps = new Properties();
    boolean pass = true;

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      setup();

      TestUtil.logMsg("Creating MimeHeaders object ...");
      mimeHeaders = new MimeHeaders();

      TestUtil.logMsg("Setting header with null value");
      mimeHeaders.setHeader("Content-Description", null);
    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    }
    try {
      setup();

      TestUtil.logMsg("Creating MimeHeaders object ...");
      mimeHeaders = new MimeHeaders();

      TestUtil.logMsg("Setting header with null string value");
      mimeHeaders.setHeader("Content-Description", "");
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
