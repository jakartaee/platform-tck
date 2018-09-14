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
 * @(#)ServletTest.java	1.8 03/05/16
 */

package com.sun.ts.tests.interop.integration.servletejbjdbc;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import javax.ejb.*;
import java.rmi.*;
import com.sun.ts.tests.integration.session.servletejbjdbc.*;

public class ServletTest extends HttpServlet {

  private static final int ACCOUNT = 1075;

  private static final String TELLERNAME = "joe";

  private static final String ejbRef = "java:comp/env/ejb/TellerBean";

  private static TSNamingContext nctx = null;

  private static TellerHome beanHome = null;

  private static Teller beanRef = null;

  private Properties harnessProps = null;

  private static final boolean debug = false;

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    System.out.println("init");
    TestUtil.logTrace("init " + this.getClass().getName() + " ...");
    try {
      TestUtil.logTrace("Obtain naming context");
      System.out.println("Obtain naming context");
      nctx = new TSNamingContext();
      TestUtil.logTrace("Lookup bean: " + ejbRef);
      System.out.println("Lookup bean: " + ejbRef);
      beanHome = (TellerHome) nctx.lookup(ejbRef, TellerHome.class);
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
    TestUtil.logTrace("doGet");
    System.out.println("doGet");

    double balance;
    String accounts;

    Properties p = new Properties();

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      TestUtil.logTrace("Create bean");
      beanRef = beanHome.create(TELLERNAME, harnessProps);
      TestUtil.logTrace("List all accounts in database");
      System.out.println("List all accounts in database");
      TestUtil.logTrace("ACCOUNTS Database");
      TestUtil.logTrace("-------- --------");
      System.out.println("ACCOUNTS Database");
      System.out.println("-------- --------");
      accounts = beanRef.getAllAccounts();
      if (accounts != null) {
        TestUtil.logTrace(accounts);
        System.out.println(accounts);
      }
      TestUtil.logTrace("Operating on account: " + ACCOUNT);
      System.out.println("Operating on account: " + ACCOUNT);
      balance = beanRef.balance(ACCOUNT);
      p.setProperty("Balance", "" + balance);
      TestUtil.logTrace("Balance: " + balance);
      System.out.println("Balance: " + balance);
      balance = beanRef.deposit(ACCOUNT, 100.0);
      p.setProperty("Deposit", "" + balance);
      TestUtil.logTrace("Deposit: " + balance);
      System.out.println("Deposit: " + balance);
      balance = beanRef.withdraw(ACCOUNT, 50.0);
      p.setProperty("Withdraw", "" + balance);
      TestUtil.logTrace("Withdraw: " + balance);
      System.out.println("Withdraw: " + balance);
      TestUtil.logTrace("list all accounts in database");
      System.out.println("list all accounts in database");
      TestUtil.logTrace("ACCOUNTS Database");
      TestUtil.logTrace("-------- --------");
      System.out.println("ACCOUNTS Database");
      System.out.println("-------- --------");
      accounts = beanRef.getAllAccounts();
      if (accounts != null) {
        TestUtil.logTrace(accounts);
        System.out.println(accounts);
      }
      p.list(out);
    } catch (Exception e) {
      TestUtil.logErr("doGet Exception: " + e);
      TestUtil.printStackTrace(e);
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
    Enumeration pEnum = req.getParameterNames();
    while (pEnum.hasMoreElements()) {
      String name = (String) pEnum.nextElement();
      String value = req.getParameter(name);
      harnessProps.setProperty(name, value);
    }

    try {
      TestUtil.init(harnessProps);
      TestUtil.logTrace("Remote logging intialized for Servlet");
      if (debug) {
        TestUtil.logTrace("Here are the harness props");
        TestUtil.list(harnessProps);
      }
    } catch (Exception e) {
      TestUtil.logErr("doPost Exception: " + e);
      System.out.println("doPost Exception: " + e);
      TestUtil.printStackTrace(e);
      throw new ServletException("unable to initialize remote logging");
    }
    doGet(req, res);
    harnessProps = null;
  }
}
