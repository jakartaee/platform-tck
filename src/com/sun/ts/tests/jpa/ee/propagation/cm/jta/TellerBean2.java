/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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
 * $Id: TellerBean.java 66539 2012-06-27 12:26:24Z sdimilla $
 */

package com.sun.ts.tests.jpa.ee.propagation.cm.jta;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.ee.common.Account;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.Local;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.Iterator;
import java.util.List;

@Stateful(name = "TellerBean2")
@Local({ Teller.class })
public class TellerBean2 implements Teller {

  public SessionContext sessionContext;

  // instance variables
  private static final int ACCOUNTS[] = { 5555 };

  private static final double BALANCES[] = { 5678.90 };

  private Account accountRef;

  // ===========================================================
  // Initialize Bean

  @PersistenceContext(unitName = "CTS-JTA-UNIT2", type = PersistenceContextType.TRANSACTION)
  private EntityManager entityManager;

  @Resource
  public void setSessionContext(SessionContext sessionContext) {
    this.sessionContext = sessionContext;
  }

  // ===========================================================
  // Teller interface (our business methods)

  public double balance(final int acct) {
    TestUtil.logTrace("balance");
    Account thisAccount = entityManager.find(Account.class, acct);
    double balance;
    try {
      balance = thisAccount.balance();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("Exception occurred in balance: " + e);
    }
    return balance;
  }

  public double deposit(final int acct, final double amt) {
    TestUtil.logTrace("deposit");
    double balance;
    Account thisAccount = entityManager.find(Account.class, acct);
    try {
      balance = thisAccount.deposit(amt);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("Exception occurred in deposit: " + e);
    }
    return balance;
  }

  public double withdraw(final int acct, final double amt) {
    TestUtil.logTrace("withdraw");
    double balance;
    Account thisAccount = entityManager.find(Account.class, acct);
    try {
      balance = thisAccount.withdraw(amt);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("Exception occurred in withdraw: " + e);
    }
    return balance;
  }

  public boolean checkAccountStatus(final Account acct) {
    TestUtil.logTrace("checkAccountStatus");
    Account thisAccount = entityManager.find(Account.class, acct.id());

    if (acct.equals(thisAccount)) {
      return true;
    } else {
      return false;
    }
  }

  public String getAllAccounts() {
    StringBuffer accounts = new StringBuffer();
    List result = null;
    try {
      result = entityManager.createQuery("select a from Account a")
          .getResultList();

      Iterator i = result.iterator();
      while (i.hasNext()) {
        Account a1 = (Account) i.next();
        accounts.append("" + a1.id() + "  " + (double) a1.balance() + "\n");
      }

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("Exception occurred in getAllAccounts: " + e);
    }
    return accounts.toString();
  }

  // ===========================================================
  // Helpers

  public void createTestData() {
    try {

      TestUtil.logTrace("createAccountData");

      TestUtil.logTrace("Create " + ACCOUNTS.length + " Account Entities");
      System.out.println("Create " + ACCOUNTS.length + " Account Entities");

      for (int i = 0; i < ACCOUNTS.length; i++) {
        TestUtil.logTrace(
            "Creating account=" + ACCOUNTS[i] + ", balance=" + BALANCES[i]);
        System.out.println(
            "Creating account=" + ACCOUNTS[i] + ", balance=" + BALANCES[i]);
        accountRef = new Account(ACCOUNTS[i], BALANCES[i]);
        entityManager.persist(accountRef);

      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected while creating test data:" + e);
    }
  }

  public void removeTestData() {
    TestUtil.logTrace("removeTestData");
    try {
      entityManager.createNativeQuery("DELETE FROM ACCOUNT").executeUpdate();
    } catch (Exception e) {
      TestUtil.logErr("Exception encountered while removing entities:", e);
    }
    // clear the cache if the provider supports caching otherwise
    // the evictAll is ignored.
    TestUtil.logTrace("Clearing cache");
    entityManager.getEntityManagerFactory().getCache().evictAll();
  }

}
