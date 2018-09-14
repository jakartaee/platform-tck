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

/*
 * $Id: ServletClient.java 52684 2009-04-15 04:30:10Z adf $
 */

package com.sun.ts.tests.webservices13.servlet.WSWebServiceRefLookup.client;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.jaxws.common.*;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.util.*;
import java.net.*;
import javax.xml.ws.*;
import javax.xml.ws.soap.*;

@WebServlet("/ServletTest")
public class ServletClient extends HttpServlet {

  private Properties harnessProps = null;

  private static final boolean debug = false;

  private String urlString;

  @WebServiceRef(name = "service/wswsreflookupservice")
  transient EchoService service = null;

  Echo port = null;

  @WebServiceRef(lookup = "java:comp/env/service/wswsreflookupservice")
  transient EchoService service2 = null;

  Echo port2 = null;

  private void getPort() throws Exception {
    TestUtil.logMsg("ServletClient DEBUG: service=" + service);
    port = (Echo) service.getPort(Echo.class);
    TestUtil.logMsg("ServletClient DEBUG: Obtained port");
    TestUtil.logMsg("ServletClient DEBUG: port=" + port);
    getTargetEndpointAddress(port);
    TestUtil.logMsg("ServletClient DEBUG: service2=" + service2);
    TestUtil.logMsg("ServletClient DEBUG: Obtained port");
    port2 = (Echo) service2.getPort(Echo.class);
    TestUtil.logMsg("ServletClient DEBUG: port2=" + port2);
  }

  private void getTargetEndpointAddress(Object port) throws Exception {
    TestUtil.logMsg("Get Target Endpoint Address for port=" + port);
    String urlString = JAXWS_Util.getTargetEndpointAddress(port);
    TestUtil.logMsg("Target Endpoint Address=" + urlString);
  }

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    try {
      getPort();
    } catch (Exception e) {
    }
    System.out.println("DEBUG ServletClient:init()");
    System.out.println("ServletClient DEBUG: service=" + service);
    System.out.println("ServletClient DEBUG: port=" + port);
    System.out.println("ServletClient DEBUG: service2=" + service2);
    System.out.println("ServletClient DEBUG: port2=" + port2);
    if (service == null || service2 == null || port == null || port2 == null) {
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
      urlString = harnessProps.getProperty("ENDPOINTURL");
      JAXWS_Util.setTargetEndpointAddress(port2, urlString);
      getTargetEndpointAddress(port2);
      System.out.println("doGet: test to execute is: " + test);
      if (test.equals("testwsreflookup")) {
        if (testwsreflookup())
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

  private boolean testwsreflookup() {
    TestUtil.logMsg("testwsreflookup");
    boolean pass = true;
    return stringTest();
  }

  private boolean stringTest() {
    TestUtil.logMsg("stringTest");
    boolean pass = true;
    String request = "Mary";

    try {
      String response = port2.echoString(request);
      if (!JAXWS_Data.compareValues(request, response, "String"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

}
