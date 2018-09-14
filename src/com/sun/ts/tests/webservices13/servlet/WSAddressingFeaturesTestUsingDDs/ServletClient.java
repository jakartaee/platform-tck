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

package com.sun.ts.tests.webservices13.servlet.WSAddressingFeaturesTestUsingDDs;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.jaxws.common.*;
import com.sun.ts.tests.jaxws.wsa.common.W3CAddressingConstants;
import com.sun.ts.tests.jaxws.wsa.common.WsaSOAPUtils;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import javax.xml.ws.*;
import javax.xml.ws.soap.*;
import javax.naming.InitialContext;

public class ServletClient extends HttpServlet {

  private Properties harnessProps = null;

  private static final boolean debug = false;

  Echo defaultEchoPort = null;

  Echo enabledEchoPort = null;

  Echo requiredEchoPort = null;

  Echo disabledEchoPort = null;

  Echo2 defaultEcho2Port = null;

  Echo2 enabledEcho2Port = null;

  Echo2 requiredEcho2Port = null;

  Echo2 disabledEcho2Port = null;

  Echo3 anonymousEcho3Port = null;

  Echo4 nonanonymousEcho4Port = null;

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    try {
      System.out.println("ServletClient:init()");
      InitialContext ctx = new InitialContext();
      System.out.println(
          "JNDI lookup java:comp/env/service/wsaddrfeaturestestusingddsdefaultechoport");
      defaultEchoPort = (Echo) ctx.lookup(
          "java:comp/env/service/wsaddrfeaturestestusingddsdefaultechoport");
      System.out.println(
          "JNDI lookup java:comp/env/service/wsaddrfeaturestestusingddsenabledechoport");
      enabledEchoPort = (Echo) ctx.lookup(
          "java:comp/env/service/wsaddrfeaturestestusingddsenabledechoport");
      System.out.println(
          "JNDI lookup java:comp/env/service/wsaddrfeaturestestusingddsrequiredechoport");
      requiredEchoPort = (Echo) ctx.lookup(
          "java:comp/env/service/wsaddrfeaturestestusingddsrequiredechoport");
      System.out.println(
          "JNDI lookup java:comp/env/service/wsaddrfeaturestestusingddsdisabledechoport");
      disabledEchoPort = (Echo) ctx.lookup(
          "java:comp/env/service/wsaddrfeaturestestusingddsdisabledechoport");
      System.out.println(
          "JNDI lookup java:comp/env/service/wsaddrfeaturestestusingddsdefaultecho2port");
      defaultEcho2Port = (Echo2) ctx.lookup(
          "java:comp/env/service/wsaddrfeaturestestusingddsdefaultecho2port");
      System.out.println(
          "JNDI lookup java:comp/env/service/wsaddrfeaturestestusingddsenabledecho2port");
      enabledEcho2Port = (Echo2) ctx.lookup(
          "java:comp/env/service/wsaddrfeaturestestusingddsenabledecho2port");
      System.out.println(
          "JNDI lookup java:comp/env/service/wsaddrfeaturestestusingddsrequiredecho2port");
      requiredEcho2Port = (Echo2) ctx.lookup(
          "java:comp/env/service/wsaddrfeaturestestusingddsrequiredecho2port");
      System.out.println(
          "JNDI lookup java:comp/env/service/wsaddrfeaturestestusingddsdisabledecho2port");
      disabledEcho2Port = (Echo2) ctx.lookup(
          "java:comp/env/service/wsaddrfeaturestestusingddsdisabledecho2port");
      System.out.println(
          "JNDI lookup java:comp/env/service/wsaddrfeaturestestusingddsanonymousecho3port");
      anonymousEcho3Port = (Echo3) ctx.lookup(
          "java:comp/env/service/wsaddrfeaturestestusingddsanonymousecho3port");
      System.out.println(
          "JNDI lookup java:comp/env/service/wsaddrfeaturestestusingddsnonanonymousecho4port");
      nonanonymousEcho4Port = (Echo4) ctx.lookup(
          "java:comp/env/service/wsaddrfeaturestestusingddsnonanonymousecho4port");
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
      System.out.println(
          "ServletClient DEBUG: requiredEcho2Port=" + requiredEcho2Port);
      System.out.println(
          "ServletClient DEBUG: disabledEcho2Port=" + disabledEcho2Port);
      System.out.println(
          "ServletClient DEBUG: anonymousEcho3Port=" + anonymousEcho3Port);
      System.out.println("ServletClient DEBUG: nonanonymousEcho4Port="
          + nonanonymousEcho4Port);
    } catch (Exception e) {
      System.err.println("ServletClient:init() Exception: " + e);
      e.printStackTrace();
    }
    if (defaultEchoPort == null || enabledEchoPort == null
        || requiredEchoPort == null || disabledEchoPort == null
        || defaultEcho2Port == null || enabledEcho2Port == null
        || requiredEcho2Port == null || disabledEcho2Port == null
        || anonymousEcho3Port == null || nonanonymousEcho4Port == null) {
      throw new ServletException(
          "init() failed: injection or JNDI lookup failure");
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
      } else if (test.equals("VerifyAddrHeadersMayExistForEnabledEcho2Port")) {
        if (VerifyAddrHeadersMayExistForEnabledEcho2Port())
          p.setProperty("TESTRESULT", "pass");
        else
          p.setProperty("TESTRESULT", "fail");
      } else if (test.equals("testAnonymousResponsesAssertion")) {
        if (testAnonymousResponsesAssertion())
          p.setProperty("TESTRESULT", "pass");
        else
          p.setProperty("TESTRESULT", "fail");
      } else if (test.equals("testNonAnonymousResponsesAssertion")) {
        if (testNonAnonymousResponsesAssertion())
          p.setProperty("TESTRESULT", "pass");
        else
          p.setProperty("TESTRESULT", "fail");
      } else
        p.setProperty("TESTRESULT", "fail");
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
      System.out.println("ServletClient invoking EchoService echo() method");
      System.out.println(
          "Addressing headers MUST be present on the SOAPRequest/SOAPResponse");
      System.out.println("VerifyAddrHeadersExistForRequiredEchoPort");
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
      System.out.println("ServletClient invoking EchoService echo() method");
      System.out.println(
          "Addressing headers MUST NOT be present on the SOAPRequest/SOAPresponse");
      System.out.println("VerifyAddrHeadersDoNotExistForDisabledEchoPort");
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

  private boolean testAnonymousResponsesAssertion() {
    try {
      TestUtil.logMsg("ServletClient invoking EchoService echo() method");
      TestUtil.logMsg("testAnonymousResponsesAssertion");
      Holder<String> testName = new Holder("testAnonymousResponsesAssertion");
      anonymousEcho3Port.echo("Echo from ServletClient on anonymousEcho3Port",
          testName);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private boolean testNonAnonymousResponsesAssertion() {
    try {
      TestUtil.logMsg("ServletClient invoking EchoService echo() method");
      TestUtil.logMsg("testNonAnonymousResponsesAssertion");
      Holder<String> testName = new Holder(
          "testNonAnonymousResponsesAssertion");
      nonanonymousEcho4Port
          .echo("Echo from ServletClient on nonanonymousEcho4Port", testName);
      return true;
    } catch (Exception e) {
      return true;
    }
  }
}
