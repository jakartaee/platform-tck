/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.webservices13.servlet.WSAddressingFeaturesTestUsingAnnotations;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.jaxws.common.*;
import com.sun.ts.tests.jaxws.wsa.common.W3CAddressingConstants;
import com.sun.ts.tests.jaxws.wsa.common.WsaSOAPUtils;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.util.*;
import javax.xml.ws.*;
import javax.xml.ws.soap.*;

@WebServlet("/ServletTest")
public class ServletClient extends HttpServlet {

  private Properties harnessProps = null;

  private static final boolean debug = false;

  @Addressing
  @WebServiceRef(name = "service/wsaddrfeaturestestusingannotationsdefaultechoport", value = EchoService.class)
  Echo defaultEchoPort = null;

  @Addressing(enabled = true)
  @WebServiceRef(name = "service/wsaddrfeaturestestusingannotationsenabledechoport", value = EchoService.class)
  Echo enabledEchoPort = null;

  @Addressing(enabled = true, required = true)
  @WebServiceRef(name = "service/wsaddrfeaturestestusingannotationsrequiredechoport", value = EchoService.class)
  Echo requiredEchoPort = null;

  @Addressing(enabled = false)
  @WebServiceRef(name = "service/wsaddrfeaturestestusingannotationsdisabledechoport", value = EchoService.class)
  Echo disabledEchoPort = null;

  @Addressing
  @WebServiceRef(name = "service/wsaddrfeaturestestusingannotationsdefaultecho2port", value = EchoService.class)
  Echo2 defaultEcho2Port = null;

  @Addressing(enabled = true)
  @WebServiceRef(name = "service/wsaddrfeaturestestusingannotationsenabledecho2port", value = EchoService.class)
  Echo2 enabledEcho2Port = null;

  @Addressing(enabled = true, required = true)
  @WebServiceRef(name = "service/wsaddrfeaturestestusingannotationsrequiredecho2port", value = EchoService.class)
  Echo2 requiredEcho2Port = null;

  @Addressing(enabled = false)
  @WebServiceRef(name = "service/wsaddrfeaturestestusingannotationsdisabledecho2port", value = EchoService.class)
  Echo2 disabledEcho2Port = null;

  @WebServiceRef(name = "service/wsaddrfeaturestestusingannotationsservice")
  EchoService service = null;

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    System.out.println("ServletClient:init()");
    System.out.println("ServletClient DEBUG: service=" + service);
    System.out
        .println("ServletClient DEBUG: defaultEchoPort=" + defaultEchoPort);
    System.out
        .println("ServletClient DEBUG: enabledEchoPort=" + enabledEchoPort);
    System.out
        .println("ServletClient DEBUG: requiredEchoPort=" + requiredEchoPort);
    System.out
        .println("ServletClient DEBUG: disabledEchoPort=" + disabledEchoPort);
    System.out
        .println("ServletClient DEBUG: defaultEcho2Port=" + defaultEcho2Port);
    System.out
        .println("ServletClient DEBUG: enabledEcho2Port=" + enabledEcho2Port);
    System.out
        .println("ServletClient DEBUG: requiredEcho2Port=" + requiredEcho2Port);
    System.out
        .println("ServletClient DEBUG: disabledEcho2Port=" + disabledEcho2Port);
    if (service == null || defaultEchoPort == null || enabledEchoPort == null
        || requiredEchoPort == null || disabledEchoPort == null
        || defaultEcho2Port == null || enabledEcho2Port == null
        || requiredEcho2Port == null || disabledEcho2Port == null) {
      throw new ServletException("init() failed: port injection failed");
    }
  }

  public void doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    boolean pass = true;
    Properties p = new Properties();
    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      String test = harnessProps.getProperty("TEST");
      System.out.println("doGet: test to execute is: " + test);
      if (test.equals("VerifyAddrHeadersExistForRequiredEchoPort")) {
        if (VerifyAddrHeadersExistForRequiredEchoPort())
          p.setProperty("TESTRESULT", "pass");
        else
          p.setProperty("TESTRESULT", "fail");
      } else if (test
          .equals("VerifyAddrHeadersDoNotExistForDisabledEchoPort")) {
        if (VerifyAddrHeadersDoNotExistForDisabledEchoPort())
          p.setProperty("TESTRESULT", "pass");
        else
          p.setProperty("TESTRESULT", "fail");
      } else if (test.equals("VerifyAddrHeadersMayExistForEnabledEchoPort")) {
        if (VerifyAddrHeadersMayExistForEnabledEchoPort())
          p.setProperty("TESTRESULT", "pass");
        else
          p.setProperty("TESTRESULT", "fail");
      } else if (test.equals("VerifyExceptionThrownForRequiredEcho2Port")) {
        if (VerifyExceptionThrownForRequiredEcho2Port())
          p.setProperty("TESTRESULT", "pass");
        else
          p.setProperty("TESTRESULT", "fail");
      } else if (test
          .equals("VerifyAddrHeadersDoNotExistForDisabledEcho2Port")) {
        if (VerifyAddrHeadersDoNotExistForDisabledEcho2Port())
          p.setProperty("TESTRESULT", "pass");
        else
          p.setProperty("TESTRESULT", "fail");
      } else {
        if (VerifyAddrHeadersMayExistForEnabledEcho2Port())
          p.setProperty("TESTRESULT", "pass");
        else
          p.setProperty("TESTRESULT", "fail");
      }
      p.list(out);
    } catch (Exception e) {
      TestUtil.logErr("doGet: Exception: " + e);
      e.printStackTrace(out);
      System.out.println("doGet: Exception: " + e);
      e.printStackTrace();
      p.setProperty("TESTRESULT", "fail");
      p.list(out);
    }
    out.close();
  }

  public void doPost(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    harnessProps = new Properties();
    Enumeration enumlist = req.getParameterNames();
    while (enumlist.hasMoreElements()) {
      String name = (String) enumlist.nextElement();
      String value = req.getParameter(name);
      harnessProps.setProperty(name, value);
    }

    try {
      TestUtil.init(harnessProps);
      if (debug) {
        System.out.println("Remote logging intialized for Servlet");
        System.out.println("Here are the harness props");
        harnessProps.list(System.out);
      }
    } catch (Exception e) {
      System.out.println("doPost: Exception: " + e);
      e.printStackTrace();
      throw new ServletException("unable to initialize remote logging");
    }
    doGet(req, res);
    harnessProps = null;
  }

  private boolean VerifyAddrHeadersExistForRequiredEchoPort() {
    try {
      TestUtil.logMsg("ServletClient invoking EchoService echo() method");
      TestUtil.logMsg(
          "Addressing headers MUST be present on the SOAPRequest/SOAPResponse");
      TestUtil.logMsg("VerifyAddrHeadersExistForRequiredEchoPort");
      Holder<String> testName = new Holder(
          "VerifyAddrHeadersExistForRequiredEchoPort");
      requiredEchoPort.echo("Echo from ServletClient on requiredEchoPort",
          testName);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private boolean VerifyAddrHeadersDoNotExistForDisabledEchoPort() {
    try {
      TestUtil.logMsg("ServletClient invoking EchoService echo() method");
      TestUtil.logMsg(
          "Addressing headers MUST NOT be present on the SOAPRequest/SOAPresponse");
      TestUtil.logMsg("VerifyAddrHeadersDoNotExistForDisabledEchoPort");
      Holder<String> testName = new Holder(
          "VerifyAddrHeadersDoNotExistForDisabledEchoPort");
      disabledEchoPort.echo("Echo from ServletClient on disabledEchoPort",
          testName);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private boolean VerifyAddrHeadersMayExistForEnabledEchoPort() {
    try {
      TestUtil.logMsg("ServletClient invoking EchoService echo() method");
      TestUtil.logMsg(
          "Addressing headers MAY be present on the SOAPRequest/SOAPresponse");
      TestUtil.logMsg("VerifyAddrHeadersMayExistForEnabledEchoPort");
      Holder<String> testName = new Holder(
          "VerifyAddrHeadersMayExistForEnabledEchoPort");
      enabledEchoPort.echo("Echo from ServletClient on enabledEchoPort",
          testName);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private boolean VerifyExceptionThrownForRequiredEcho2Port() {
    try {
      TestUtil.logMsg("ServletClient invoking EchoService echo() method");
      TestUtil.logMsg("Expect a WebServiceException to be thrown back");
      TestUtil.logMsg("VerifyExceptionThrownForRequiredEcho2Port");
      Holder<String> testName = new Holder(
          "VerifyExceptionThrownForRequiredEcho2Port");
      requiredEcho2Port.echo("Echo from ServletClient on requiredEcho2Port",
          testName);
      TestUtil.logErr("WebServiceException was not thrown back");
      return false;
    } catch (WebServiceException e) {
      TestUtil.logMsg("Caught expected WebServiceException: " + e.getMessage());
      return true;
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      return false;
    }
  }

  private boolean VerifyAddrHeadersDoNotExistForDisabledEcho2Port() {
    try {
      TestUtil.logMsg("ServletClient invoking EchoService echo() method");
      TestUtil.logMsg(
          "Addressing headers MUST NOT be present on the SOAPRequest/SOAPResponse");
      TestUtil.logMsg("VerifyAddrHeadersDoNotExistForDisabledEcho2Port");
      Holder<String> testName = new Holder(
          "VerifyAddrHeadersDoNotExistForDisabledEcho2Port");
      disabledEcho2Port.echo("Echo from ServletClient on disabledEcho2Port",
          testName);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private boolean VerifyAddrHeadersMayExistForEnabledEcho2Port() {
    try {
      TestUtil.logMsg("ServletClient invoking EchoService echo() method");
      TestUtil.logMsg(
          "Addressing headers MAY be present on the SOAPRequest/SOAPresponse");
      TestUtil.logMsg("VerifyAddrHeadersMayExistForEnabledEcho2Port");
      Holder<String> testName = new Holder(
          "VerifyAddrHeadersMayExistForEnabledEcho2Port");
      enabledEcho2Port.echo("Echo from ServletClient on enabledEcho2Port",
          testName);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
