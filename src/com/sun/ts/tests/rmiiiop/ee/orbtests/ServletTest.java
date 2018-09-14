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

package com.sun.ts.tests.rmiiiop.ee.orbtests;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import com.sun.ts.tests.rmiiiop.ee.orbtests.*;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import javax.ejb.*;
import java.rmi.*;
import javax.rmi.PortableRemoteObject;
import javax.rmi.CORBA.Util;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;

public class ServletTest extends HttpServlet {

  private static final String orbLookup = "java:comp/ORB";

  private TSNamingContext nctx = null;

  private Properties harnessProps = null;

  private static final boolean debug = false;

  private HelloRMIIIOPObjectIntf rmiiiopRef = null;

  private org.omg.CORBA.ORB orb = null;

  private org.omg.CORBA.Object obj = null;

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    System.out.println("init");
    TestUtil.logTrace("init " + this.getClass().getName() + " ...");
    try {
      TestUtil.logTrace("Obtain naming context");
      System.out.println("Obtain naming context");
      nctx = new TSNamingContext();
    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e);
      TestUtil.printStackTrace(e);
      System.out.println("init Exception: " + e);
      e.printStackTrace();
      throw new ServletException("unable to initialize servlet properly");
    }
  }

  public void doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    boolean pass = true;
    String expStr = "Hello from HelloRMIIIOPObjectImpl";
    String ior = null;
    ORB orb = null;

    double balance;
    String accounts;

    Properties p = new Properties();

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    TestUtil.logTrace("doGet");
    System.out.println("doGet");

    try {
      if (harnessProps.getProperty("TEST").equals("test1")) {
        TestUtil.logTrace("test1");
        TestUtil.logMsg("Create an ORB instance using [ORB.init()]");
        System.out.println("test1");
        System.out.println("Create an ORB instance using [ORB.init()]");
        orb = ORB.init(new String[0], null);
        TestUtil.logMsg("ORB = " + orb);
        if (orb == null) {
          TestUtil
              .logErr("Unable to create an ORB instance using [ORB.init()]");
          System.err
              .println("Unable to create an ORB instance using [ORB.init()]");
          pass = false;
        }
      } else {
        TestUtil.logTrace("test2");
        TestUtil.logMsg(
            "Lookup ORB instance in JNDI namespace under [java:comp/ORB]");
        System.out.println("test2");
        System.out.println(
            "Lookup ORB instance in JNDI namespace under [java:comp/ORB]");
        try {
          orb = (ORB) nctx.lookup(orbLookup);
          TestUtil.logMsg("ORB = " + orb);
          System.out.println("ORB = " + orb);
        } catch (Exception e) {
          orb = null;
        }
        if (orb == null) {
          TestUtil.logErr("Unable to lookup ORB instance in JNDI namespace");
          System.err.println("Unable to lookup ORB instance in JNDI namespace");
          pass = false;
        }
      }
      if (pass) {
        TestUtil.logMsg("Verify some basic ORB functionality");
        System.out.println("Verify some basic ORB functionality");
        ior = harnessProps.getProperty("IOR");
        TestUtil.logMsg("IOR = " + ior);
        TestUtil.logMsg("Convert stringified IOR to a CORBA object");
        System.out.println("IOR = " + ior);
        System.out.println("Convert stringified IOR to a CORBA object");
        obj = orb.string_to_object(ior);
        TestUtil
            .logMsg("Narrow CORBA object to interface HelloRMIIIOPObjectIntf");
        System.out
            .println("Narrow CORBA object to interface HelloRMIIIOPObjectIntf");
        rmiiiopRef = (HelloRMIIIOPObjectIntf) PortableRemoteObject.narrow(obj,
            HelloRMIIIOPObjectIntf.class);
        TestUtil
            .logMsg("Call hello method on interface HelloRMIIIOPObjectIntf");
        System.out
            .println("Call hello method on interface HelloRMIIIOPObjectIntf");
        String hello = rmiiiopRef.hello();
        TestUtil.logMsg("Verify the method call");
        System.out.println("Verify the method call");
        if (!hello.equals(expStr)) {
          TestUtil.logErr("Wrong message, got [" + hello + "]");
          TestUtil.logErr("Wrong message, expected [" + expStr + "]");
          System.err.println("Wrong message, got [" + hello + "]");
          System.err.println("Wrong message, expected [" + expStr + "]");
          pass = false;
        }
      }
      if (pass)
        p.setProperty("TESTRESULT", "pass");
      else
        p.setProperty("TESTRESULT", "false");
      p.list(out);
    } catch (Exception e) {
      TestUtil.logErr("doGet Exception: " + e);
      e.printStackTrace(out);
      System.out.println("doGet Exception: " + e);
      e.printStackTrace();
    }
    out.close();
  }

  public void doPost(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    TestUtil.logTrace("doPost");
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
      TestUtil.logTrace("Remote logging intialized for Servlet");
      System.out.println("Remote logging intialized for Servlet");
      if (debug) {
        TestUtil.logTrace("Here are the harness props");
        TestUtil.list(harnessProps);
        System.out.println("Here are the harness props");
        harnessProps.list(System.out);
      }
    } catch (Exception e) {
      TestUtil.logErr("doPost Exception: " + e);
      System.out.println("doPost Exception: " + e);
      e.printStackTrace();
      throw new ServletException("unable to initialize remote logging");
    }
    doGet(req, res);
    harnessProps = null;
  }
}
