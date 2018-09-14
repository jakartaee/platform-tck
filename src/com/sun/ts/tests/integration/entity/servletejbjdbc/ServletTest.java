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
 * @(#)ServletTest.java	1.14 03/05/28
 */

package com.sun.ts.tests.integration.entity.servletejbjdbc;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import javax.ejb.*;
import java.rmi.*;
import com.sun.ts.tests.integration.entity.servletejbjdbc.*;

public class ServletTest extends HttpServlet {

  private static final int ACCOUNT = 1075;

  private static final double BALANCE = 10490.75;

  private static final int ACCOUNTS[] = { 1000, 1075, 40, 30564, 387 };

  private static final double BALANCES[] = { 50000.0, 10490.75, 200.50, 25000.0,
      1000000.0 };

  private static TSNamingContext nctx = null;

  private static final String ejbRef = "java:comp/env/ejb/AccountBean";

  private static AccountHome beanHome = null;

  private static Account beanRef = null;

  private Properties harnessProps = null;

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    System.out.println("init");
    TestUtil.logTrace("init");
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
      TestUtil.logMsg("Create 5 Account EJB's");
      System.out.println("Create 5 Account EJB's");
      for (int i = 0; i < ACCOUNTS.length; i++) {
        TestUtil.logMsg(
            "Creating account=" + ACCOUNTS[i] + ", balance=" + BALANCES[i]);
        System.out.println(
            "Creating account=" + ACCOUNTS[i] + ", balance=" + BALANCES[i]);
        if (i == 0)
          beanRef = (Account) beanHome.create(ACCOUNTS[i], BALANCES[i], true,
              harnessProps);
        else
          beanRef = (Account) beanHome.create(ACCOUNTS[i], BALANCES[i], false,
              harnessProps);
      }
      TestUtil.logMsg("Find Account EJB of account=" + ACCOUNT);
      System.out.println("Find Account EJB of account=" + ACCOUNT);
      beanRef = (Account) beanHome.findTheBean(new Integer(ACCOUNT),
          harnessProps);
      TestUtil.logMsg("Operating on account: " + ACCOUNT);
      System.out.println("Operating on account: " + ACCOUNT);
      balance = beanRef.balance();
      p.setProperty("Balance", "" + balance);
      System.out.println("Balance: " + balance);
      TestUtil.logMsg("Balance: " + balance);
      balance = beanRef.deposit(100.0);
      p.setProperty("Deposit", "" + balance);
      System.out.println("Deposit: " + balance);
      TestUtil.logMsg("Deposit: " + balance);
      balance = beanRef.withdraw(50.0);
      p.setProperty("Withdraw", "" + balance);
      System.out.println("Withdraw: " + balance);
      TestUtil.logMsg("Withdraw: " + balance);
      p.list(out);
      TestUtil.logMsg("Clean Up Entity Data");
      for (int i = 0; i < ACCOUNTS.length; i++) {
        TestUtil.logMsg("Removing Account EJB's");
        beanRef = (Account) beanHome.findByPrimaryKey(new Integer(ACCOUNTS[i]));
        if (beanRef != null) {
          beanRef.remove();
        }
      }

    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e);
      TestUtil.printStackTrace(e);
      System.out.println("Exception: " + e);
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
      TestUtil.logMsg("Remote logging intialized for Servlet");
    } catch (Exception e) {
      System.out.println("doPost Exception: " + e);
      TestUtil.printStackTrace(e);
      throw new ServletException("unable to initialize remote logging");
    }

    try {
      if (nctx == null || beanHome == null) {
        TestUtil.logMsg("Obtain naming context");
        System.out.println("Obtain naming context");
        nctx = new TSNamingContext();
        TestUtil.logMsg("Lookup bean: " + ejbRef);
        System.out.println("Lookup bean: " + ejbRef);
        beanHome = (AccountHome) nctx.lookup(ejbRef, AccountHome.class);
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e);
      TestUtil.printStackTrace(e);
      System.out.println("init Exception: " + e);
      e.printStackTrace();
      throw new ServletException("unable to initialize servlet properly");
    }

    doGet(req, res);
    harnessProps = null;
  }
}
