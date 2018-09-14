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
 * @(#)ServletTest.java	1.1 06/02/25
 */

package com.sun.ts.tests.jpa.ee.packaging.web.standalone;

import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.tests.jpa.ee.common.Account;
import com.sun.ts.tests.jpa.ee.util.HttpTCKServlet;
import com.sun.ts.tests.jpa.ee.util.Data;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Status;
import javax.transaction.UserTransaction;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class ServletTest extends HttpTCKServlet {

  private static final int ACCOUNT = 1075;

  private static final int ACCOUNTS[] = { 1000, 1075, 40, 30564, 387 };

  private static final double BALANCES[] = { 50000.0, 10490.75, 200.50, 25000.0,
      1000000.0 };

  private static final String emfRef = "java:comp/env/persistence/MyPersistenceUnit";

  private EntityManager entityManager;

  private EntityManagerFactory entityManagerFactory;

  private UserTransaction ut;

  private Account accountRef;

  private Map myMap = new HashMap();

  public UserTransaction getTx() {
    try {
      TSNamingContext nctx = new TSNamingContext();
      ut = (UserTransaction) nctx.lookup("java:comp/UserTransaction");
    } catch (Exception e) {
      System.out.println("Naming service exception: " + e.getMessage());
      e.printStackTrace();
    }
    return ut;
  }

  public void test1(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {

    PrintWriter pw = res.getWriter();
    Double EXPECTED_BALANCE = 10540.75D;
    Double balance;

    try {

      ut = getTx();
      TSNamingContext nctx = new TSNamingContext();
      System.out.println("Lookup EntityManagerFactory: " + emfRef);
      entityManagerFactory = (EntityManagerFactory) nctx.lookup(emfRef);

      System.out.println("createEntityManager with Map of Props");
      entityManager = entityManagerFactory.createEntityManager(myMap);

      System.out.println("Begin TX to create Entities");
      ut.begin();
      entityManager.joinTransaction();
      System.out.println("Create 5 Account Entities");
      for (int i = 0; i < ACCOUNTS.length; i++) {
        System.out.println(
            "Creating account=" + ACCOUNTS[i] + ", balance=" + BALANCES[i]);

        accountRef = new Account(ACCOUNTS[i], BALANCES[i]);
        entityManager.persist(accountRef);

      }

      System.out.println("Commit TX to persist Entities");
      ut.commit();

      System.out.println("In next TX,  modify entity and commit");

      ut.begin();
      entityManager.joinTransaction();
      accountRef = entityManager.find(Account.class, ACCOUNT);
      System.out.println("Operating on account: " + ACCOUNT);
      balance = accountRef.balance();

      System.out.println("Balance Before Deposit: " + balance);
      balance = accountRef.deposit(100.0);
      System.out.println("Balance After Deposit: " + balance);

      balance = accountRef.withdraw(50.0);
      System.out.println("Balance After Withdraw: " + balance);

      ut.commit();

      System.out.println("Retrieve Entity after commit and check balance");
      Account updatedAccount = entityManager.find(Account.class, ACCOUNT);
      balance = updatedAccount.balance();

      if (EXPECTED_BALANCE.compareTo(balance) == 0) {
        System.out.println(" TEST PASSED. BALANCE IS: " + balance);
        pw.println(Data.PASSED + ".  Balance of account as expected");
      } else {
        System.out.println(
            " TEST FAILED - Account balance is not correct.  Expected: "
                + EXPECTED_BALANCE + "got: " + balance);
        pw.println(Data.FAILED
            + ".  ERROR: Account balance is not correct.  Expected: "
            + EXPECTED_BALANCE + "got: " + balance);
      }
    } catch (Exception e) {
      System.out.println("Unexpected exception caught in test1");
      pw.println(
          Data.FAILED + ".  ERROR: Unexpected Exception caught in test1");
      e.printStackTrace();
      try {
        if (ut.getStatus() != Status.STATUS_NO_TRANSACTION)
          ut.rollback();
      } catch (Exception se) {
        System.out.println(
            "Unexpected exception caught in test1 while checking TX status ");
        se.printStackTrace();
      }
    } finally {
      try {
        ut.begin();
        entityManager.joinTransaction();
        for (int i = 0; i < ACCOUNTS.length; i++) {
          System.out.println("Removing Account Entities");
          accountRef = entityManager.find(Account.class, ACCOUNTS[i]);
          if (accountRef != null) {
            entityManager.remove(accountRef);
          }
        }

        /*
         * If the [EntityManager] close method is invoked when a transaction is
         * active, the persistence context remains managed until the transaction
         * completes.
         */

        entityManager.close();
        ut.commit();

      } catch (Exception e) {
        System.out.println(
            "Unexpected exception caught in test1 while cleaning up test data");
        e.printStackTrace();
        try {
          if (ut.getStatus() != Status.STATUS_NO_TRANSACTION)
            ut.rollback();
        } catch (Exception se) {
          System.out.println(
              "Unexpected exception caught in test1 while checking TX status ");
          se.printStackTrace();
        }
      }
    }
    // clear the cache if the provider supports caching otherwise
    // the evictAll is ignored.
    System.out.println("Clearing cache");
    entityManagerFactory.getCache().evictAll();
  }
}
