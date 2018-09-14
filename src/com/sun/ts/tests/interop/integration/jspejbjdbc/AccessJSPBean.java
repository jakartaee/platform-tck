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
 * @(#)AccessJSPBean.java	1.8 03/05/16
 */

package com.sun.ts.tests.interop.integration.jspejbjdbc;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.Properties;
import java.rmi.RemoteException;
import javax.servlet.*;
import javax.servlet.http.*;
import com.sun.ts.tests.integration.session.jspejbjdbc.*;

public class AccessJSPBean {

  private static final int ACCOUNT = 1075;

  private static final String TELLERNAME = "joe";

  private static final String ejbRef = "java:comp/env/ejb/TellerBean";

  private TellerHome beanHome = null;

  private Teller beanRef = null;

  private TSNamingContext nctx = null;

  public AccessJSPBean() throws Exception {
    nctx = new TSNamingContext();
    beanHome = (TellerHome) nctx.lookup(ejbRef, TellerHome.class);
  }

  public void setup(Properties p) {
    TestUtil.logTrace("setup");
    try {
      if (p != null)
        TestUtil.init(p);
      beanRef = beanHome.create(TELLERNAME, p);
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
      if (beanRef == null) {
        return p;
      } else {
        TestUtil.logTrace("Perform database transactions");
        balance = beanRef.balance(ACCOUNT);
        p.setProperty("Balance", "" + balance);
        TestUtil.logTrace("Balance is " + balance);

        balance = beanRef.deposit(ACCOUNT, 100.0);
        p.setProperty("Deposit", "" + balance);
        TestUtil.logTrace("Deposit 100, balance is " + balance);

        balance = beanRef.withdraw(ACCOUNT, 50.0);
        p.setProperty("Withdraw", "" + balance);
        TestUtil.logTrace("Withdraw 50, balance is " + balance);
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e, e);
    }
    return p;
  }
}
