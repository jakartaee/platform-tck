/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.webservices12.ejb.descriptors.WSEjbOverrideWSRefHCWithDDsTest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.jaxws.common.*;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import javax.xml.ws.*;
import javax.jws.*;

public class ServletClient extends HttpServlet {

  private Properties harnessProps = null;

  private static final boolean debug = false;

  @WebServiceRef(name = "service/wsejboverridewsrefhcwithddstest", type = java.lang.Object.class, value = javax.xml.ws.Service.class)
  HelloService service = null;

  private Hello port;

  private void getPort() throws Exception {
    System.out.println(
        "Get wsejboverridewsrefhcwithddstest Service via @WebServiceRef annotation");
    System.out.println(
        "Uses name attribute @WebServiceRef(name=\"service/wsejboverridewsrefhcwithddstest\")");
    System.out.println("service=" + service);
    System.out.println("Get port from service");
    port = (Hello) service.getHello();
    System.out.println("port=" + port);
    System.out.println("Port obtained");
    JAXWS_Util.dumpTargetEndpointAddress(port);
  }

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    System.out.println("DEBUG init: service=" + service);
    try {
      getPort();
    } catch (Exception e) {
      System.err.println("init Exception: " + e);
    }
  }

  public void doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    boolean pass = true;
    Properties p = new Properties();
    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    System.out.println("DEBUG doGet");
    try {
      System.out.println("Invoke the webservice endpoint from Web container");
      String serverSideMsgs = port.helloEcho("firstcall");
      serverSideMsgs = port.helloEcho("secondcall");
      String clientSideMsgs = HandlerTracker.getMessages1();
      HandlerTracker.purge();
      System.out.println("-----------------------------------");
      System.out.println("Dumping ClientSide Handler messages");
      System.out.println("-----------------------------------");
      System.out.println(clientSideMsgs);
      System.out.println("Verify client side handler callbacks");
      if (!VerifyHandlerCallBacks("client", clientSideMsgs)) {
        System.err.println("ClientSide Handler CallBacks (incorrect)");
        pass = false;
      } else {
        System.out.println("ClientSide Handler CallBacks (correct)");
      }

      System.out.println("-----------------------------------");
      System.out.println("Dumping ServerSide Handler messages");
      System.out.println("-----------------------------------");
      System.out.println(serverSideMsgs);
      System.out.println("Verify server side handler callbacks");
      if (!VerifyHandlerCallBacks("server", serverSideMsgs)) {
        System.err.println("ServerSide Handler CallBacks (incorrect)");
        pass = false;
      } else {
        System.out.println("ServerSide Handler CallBacks (correct)");
      }
      if (pass)
        p.setProperty("TESTRESULT", "pass");
      else
        p.setProperty("TESTRESULT", "fail");
      p.list(out);
    } catch (Exception e) {
      System.err.println("doGet Exception: " + e);
    }
    out.close();
  }

  public void doPost(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    System.out.println("doPost");

    harnessProps = new Properties();
    Enumeration enumlist = req.getParameterNames();
    while (enumlist.hasMoreElements()) {
      String name = (String) enumlist.nextElement();
      String value = req.getParameter(name);
      harnessProps.setProperty(name, value);
    }

    try {
      TestUtil.init(harnessProps);
      System.out.println("Remote logging intialized for Servlet");
      if (debug) {
        System.out.println("Here are the harness props");
        harnessProps.list(System.out);
      }
    } catch (Exception e) {
      System.err.println("doPost Exception: " + e);
      throw new ServletException("unable to initialize remote logging");
    }
    doGet(req, res);
    harnessProps = null;
  }

  private boolean VerifyHandlerCallBacks(String who, String calls) {
    int startIdx = 0;
    int endIdx = 0;
    boolean pass = true;

    if (who.equals("client")) {
      if (calls == null) {
        System.err.println("Callback string is null (unexpected)");
        return false;
      }
      if (calls.indexOf("ClientHandler1.handleMessage().doInbound()") == -1) {
        System.err.println(
            "ClientHandler1.handleMessage().doInbound() was not called");
        pass = false;
      }
      if (calls.indexOf("ClientHandler2.handleMessage().doInbound()") == -1) {
        System.err.println(
            "ClientHandler2.handleMessage().doInbound() was not called");
        pass = false;
      }
      if (calls.indexOf("ClientHandler1.handleMessage().doOutbound()") == -1) {
        System.err.println(
            "ClientHandler1.handleMessage().doOutbound() was not called");
        pass = false;
      }
      if (calls.indexOf("ClientHandler2.handleMessage().doOutbound()") == -1) {
        System.err.println(
            "ClientHandler2.handleMessage().doOutbound() was not called");
        pass = false;
      }
      if (calls.indexOf("ClientHandler1.close()") == -1) {
        System.err.println("ClientHandler1.close() was not called");
        pass = false;
      }
      if (calls.indexOf("ClientHandler2.close()") == -1) {
        System.err.println("ClientHandler2.close() was not called");
        pass = false;
      }
    } else {
      if (calls == null) {
        System.err.println("Callback string is null (unexpected)");
        return false;
      }
      if (calls.indexOf("ServerHandler1.handleMessage().doInbound()") == -1) {
        System.err.println(
            "ServerHandler1.handleMessage().doInbound() was not called");
        pass = false;
      }
      if (calls.indexOf("ServerHandler2.handleMessage().doInbound()") == -1) {
        System.err.println(
            "ServerHandler2.handleMessage().doInbound() was not called");
        pass = false;
      }
      if (calls.indexOf("ServerHandler1.handleMessage().doOutbound()") == -1) {
        System.err.println(
            "ServerHandler1.handleMessage().doOutbound() was not called");
        pass = false;
      }
      if (calls.indexOf("ServerHandler2.handleMessage().doOutbound()") == -1) {
        System.err.println(
            "ServerHandler2.handleMessage().doOutbound() was not called");
        pass = false;
      }
      if (calls.indexOf("ServerHandler1.close()") == -1) {
        System.err.println("ServerHandler1.close() was not called");
        pass = false;
      }
      if (calls.indexOf("ServerHandler2.close()") == -1) {
        System.err.println("ServerHandler2.close() was not called");
        pass = false;
      }
    }
    return pass;
  }
}
