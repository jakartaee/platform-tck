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
 * @(#)AccessJSPBean.java	1.14 03/05/28
 */

package com.sun.ts.tests.integration.entity.jspejbjdbc;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.Properties;
import java.rmi.RemoteException;
import javax.servlet.*;
import javax.servlet.http.*;

public class AccessJSPBean {

  private static final int ACCOUNT = 1075;

  private static final double BALANCE = 10490.75;

  private static final int ACCOUNTS[] = { 1000, 1075, 40, 30564, 387 };

  private static final double BALANCES[] = { 50000.0, 10490.75, 200.50, 25000.0,
      1000000.0 };

  private static final String ejbRef = "java:comp/env/ejb/AccountBean";

  private AccountHome beanHome = null;

  private Account beanRef = null;

  private TSNamingContext nctx = null;

  private Properties harnessProps = null;

  public AccessJSPBean() throws Exception {
    nctx = new TSNamingContext();
    beanHome = (AccountHome) nctx.lookup(ejbRef, AccountHome.class);
  }

  public void setup(Properties p) {
    TestUtil.logTrace("setup");
    try {
      if (p != null) {
        harnessProps = p;
        TestUtil.init(p);
      }
      TestUtil.logMsg("Create 5 Account EJB's");
      for (int i = 0; i < ACCOUNTS.length; i++) {
        TestUtil.logMsg(
            "Creating account=" + ACCOUNTS[i] + ", balance=" + BALANCES[i]);
        if (i == 0)
          beanRef = (Account) beanHome.create(ACCOUNTS[i], BALANCES[i], true,
              p);
        else
          beanRef = (Account) beanHome.create(ACCOUNTS[i], BALANCES[i], false,
              p);
      }
    } catch (Exception e) {
      TestUtil.logErr("AccessJSPBean: Exception occurred - " + e, e);
    }
  }

  public String getMsg() {
    try {
      if (beanRef == null) {
        return "could not talk to the EJB in method getMsg -> " + ejbRef;
      } else {
        return beanRef.sayHello();
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      return "Got Exception : " + e.getMessage();
    }
  }

  public Properties getTransactionProps() {
    TestUtil.logTrace("getTransactionProps");
    double balance;

    Properties p = new Properties();

    try {
      TestUtil.logMsg("Find Account EJB of account=" + ACCOUNT);
      beanRef = (Account) beanHome.findTheBean(new Integer(ACCOUNT),
          harnessProps);
      TestUtil.logMsg("Operating on account: " + ACCOUNT);
      TestUtil.logMsg("Perform database transactions");
      balance = beanRef.balance();
      p.setProperty("Balance", "" + balance);
      TestUtil.logMsg("Balance: " + balance);
      balance = beanRef.deposit(100.0);
      p.setProperty("Deposit", "" + balance);
      TestUtil.logMsg("Deposit: " + balance);
      balance = beanRef.withdraw(50.0);
      p.setProperty("Withdraw", "" + balance);
      TestUtil.logMsg("Withdraw: " + balance);
    } catch (Exception e) {
      p.setProperty("Exception", "" + e);
      TestUtil.logErr("Exception occurred: " + e, e);
    }
    return p;
  }

  public void cleanTableData() {
    try {
      TestUtil.logMsg("Clean Up Entity Data");
      for (int i = 0; i < ACCOUNTS.length; i++) {
        TestUtil.logMsg("Removing Account EJB's");
        Account beanRef = (Account) beanHome
            .findByPrimaryKey(new Integer(ACCOUNTS[i]));
        if (beanRef != null) {
          TestUtil.logMsg("beanRef removed");
          beanRef.remove();
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception caught removing entity data", e);
    }
  }

}
