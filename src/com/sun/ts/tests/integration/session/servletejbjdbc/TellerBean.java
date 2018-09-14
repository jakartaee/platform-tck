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
 * @(#)TellerBean.java	1.24 03/05/16
 */

package com.sun.ts.tests.integration.session.servletejbjdbc;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import com.sun.ts.tests.integration.util.*;

import javax.ejb.*;
import java.rmi.*;
import java.sql.*;
import javax.sql.*;
import java.util.*;

public class TellerBean implements SessionBean {

  private SessionContext sctx = null;

  // Database Support Access Object
  private DBSupport DB;

  // state managed variables
  public String tellerName;

  public void ejbCreate(String name, Properties p) throws CreateException {
    TestUtil.logTrace("ejbCreate");
    this.tellerName = name;
    if (p != null) {
      try {
        TestUtil.logTrace("initialize remote logging");
        TestUtil.init(p);
        DB.initDB(true, true);
      } catch (RemoteLoggingInitException e) {
        TestUtil.printStackTrace(e);
        throw new CreateException(e.getMessage());
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        throw new CreateException("unable to initialize DB table " + e);
      }
    }
  }

  public void setSessionContext(SessionContext sc) {
    TestUtil.logTrace("setSessionContext");
    this.sctx = sc;
    // Initialize DB Support Access Object
    try {
      TestUtil.logTrace("initialize database support access object");
      DB = new DBSupport();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("Unable to initialize DB. Exception: " + e);
    }
  }

  public void ejbRemove() {
    TestUtil.logTrace("ejbRemove");
  }

  public void ejbActivate() {
    TestUtil.logTrace("ejbActivate");
  }

  public void ejbPassivate() {
    TestUtil.logTrace("ejbPassivate");
  }

  // ===========================================================
  // Teller interface (our business methods)

  public void transfer(int from, int to, double amt) {
    TestUtil.logTrace("transfer");
    withdraw(from, amt);
    deposit(to, amt);
  }

  public double balance(int acct) {
    double balance;
    TestUtil.logTrace("balance");
    try {
      balance = DB.balance(acct);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("Exception occurred in balance: " + e);
    }
    return balance;
  }

  public double deposit(int acct, double amt) {
    double balance;
    TestUtil.logTrace("deposit");
    try {
      balance = DB.deposit(acct, amt);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("Exception occurred in deposit: " + e);
    }
    return balance;
  }

  public double withdraw(int acct, double amt) {
    double balance;
    TestUtil.logTrace("withdraw");
    try {
      balance = DB.withdraw(acct, amt);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("Exception occurred in withdraw: " + e);
    }
    return balance;
  }

  public String getAllAccounts() {
    String accounts = null;
    try {
      accounts = DB.getAllAccounts();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("Exception occurred in getAllAccounts: " + e);
    }
    return accounts;
  }

  // ===========================================================
}
