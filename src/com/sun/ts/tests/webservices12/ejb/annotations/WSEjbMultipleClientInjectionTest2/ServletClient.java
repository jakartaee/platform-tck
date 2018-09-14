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

/*
 * $Id$
 */

package com.sun.ts.tests.webservices12.ejb.annotations.WSEjbMultipleClientInjectionTest2;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.jaxws.common.*;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import javax.xml.ws.*;

public class ServletClient extends HttpServlet {

  private Properties harnessProps = null;

  private static final boolean debug = false;

  private Hello port;

  private String targetEndpointAddress;

  @WebServiceRef(name = "service/wsejbmultipleclientinjectiontest2")
  HelloService service = null;

  private void getPort() throws Exception {
    port = (Hello) service.getHello();
  }

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    System.out.println("DEBUG init: service=" + service);
    try {
      if (service != null)
        getPort();
      System.out.println("DEBUG init: port=" + port);
      BindingProvider bindingprovider = (BindingProvider) port;
      java.util.Map<String, Object> context = bindingprovider
          .getRequestContext();
      targetEndpointAddress = (String) context
          .get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
      System.out.println(
          "DEBUG init: target endpoint address=" + targetEndpointAddress);
    } catch (Exception e) {
      System.err.println("DEBUG init: Exception: " + e);
      e.printStackTrace();
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
      TestUtil.logMsg("doGet: test to execute is: " + test);
      System.out.println("doGet: test to execute is: " + test);
      if (test.equals("test1")) {

        if (service != null)
          p.setProperty("TESTRESULT", "pass");
        else
          p.setProperty("TESTRESULT", "fail");
      } else {
        TestUtil.logMsg("Invoke hello method");
        if (port != null) {
          String txt = port.hello("Hello there");
          if (txt.equals("Hello there to you too!"))
            TestUtil.logMsg("Invoke of hello passed");
          else {
            pass = false;
            TestUtil.logErr("Invoke of hello failed");
          }
        } else
          pass = false;
        TestUtil.logMsg("Invoke bye method");
        if (port != null) {
          String txt = port.bye("Bye");
          if (txt.equals("Bye and take care!"))
            TestUtil.logMsg("Invoke of bye passed");
          else {
            pass = false;
            TestUtil.logErr("Invoke of bye failed");
          }
        } else
          pass = false;
        if (pass)
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
    TestUtil.logMsg("doPost: service=" + service);
    TestUtil.logMsg("doPost: port=" + port);
    TestUtil.logMsg("doPost: target endpoint address=" + targetEndpointAddress);
    System.out.println("doPost: service=" + service);
    System.out.println("doPost: port=" + port);
    System.out
        .println("doPost: target endpoint address=" + targetEndpointAddress);

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
        TestUtil.logMsg("Remote logging intialized for Servlet");
        System.out.println("Remote logging intialized for Servlet");
        TestUtil.logMsg("Here are the harness props");
        TestUtil.list(harnessProps);
        System.out.println("Here are the harness props");
        harnessProps.list(System.out);
      }
    } catch (Exception e) {
      TestUtil.logErr("doPost: Exception: " + e);
      System.out.println("doPost: Exception: " + e);
      e.printStackTrace();
      throw new ServletException("unable to initialize remote logging");
    }
    doGet(req, res);
    harnessProps = null;
  }
}
