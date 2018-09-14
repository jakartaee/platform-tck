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

package com.sun.ts.tests.jpa.ee.propagation.cm.jta;

import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.ee.common.Account;
import com.sun.ts.tests.jpa.ee.util.HttpTCKServlet;
import com.sun.ts.tests.jpa.ee.util.Data;

import java.io.StringWriter;
import java.io.PrintWriter;

import javax.persistence.*;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Status;
import javax.transaction.UserTransaction;
import java.io.IOException;
import java.lang.String;
import java.util.Map;
import java.util.HashMap;

@PersistenceContexts({
    @PersistenceContext(name = "persistence/cmpropagation", unitName = "CTS-JTA-UNIT"),
    @PersistenceContext(name = "persistence/cmpropagation2", unitName = "CTS-JTA-UNIT2") })
public class ServletTest extends HttpTCKServlet {

  private UserTransaction ut;

  private EntityManager entityManager;

  private EntityManager entityManager2;

  private static final String EM_LOOKUP_NAME = "java:comp/env/persistence/cmpropagation";

  private static final String EM_LOOKUP_NAME2 = "java:comp/env/persistence/cmpropagation2";

  @EJB(beanName = "TellerBean")
  private Teller beanRef;

  @EJB(beanName = "TellerBean2")
  private Teller beanRef2;

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

  private void getEM() {
    try {
      if (this.entityManager == null) {
        TSNamingContext nctx = new TSNamingContext();
        this.entityManager = (EntityManager) nctx.lookup(EM_LOOKUP_NAME);
      }
    } catch (Exception e) {
      System.out.println("Exception caught looking up EntityManager");
      e.printStackTrace();
    }

  }

  private void getEM2() {
    try {
      if (this.entityManager2 == null) {
        TSNamingContext nctx = new TSNamingContext();
        this.entityManager2 = (EntityManager) nctx.lookup(EM_LOOKUP_NAME2);
      }
    } catch (Exception e) {
      System.out.println("Exception caught looking up EntityManager");
      e.printStackTrace();
    }
  }

  public String convertExceptionToString(Exception ex) {
    StringWriter str = new StringWriter();
    PrintWriter writer = new PrintWriter(str);
    try {
      ex.printStackTrace(writer);
      return str.getBuffer().toString();
    } finally {
      try {
        str.close();
        writer.close();
      } catch (IOException e) {
        // ignore
      }
    }
  }

  public void test1(final HttpServletRequest req, final HttpServletResponse res)
      throws ServletException, IOException {
    String accounts;
    PrintWriter pw = res.getWriter();

    try {
      ut = getTx();
      ut.begin();

      getEM();

      beanRef.removeTestData();
      System.out.println("test1: createAccountData");
      beanRef.createTestData();

      accounts = beanRef.getAllAccounts();

      if (accounts != null) {
        System.out.println(accounts);
      }

      Account ACCOUNT = entityManager.find(Account.class, 1075);

      boolean status = beanRef.checkAccountStatus(ACCOUNT);

      ut.commit();

      if (status) {
        System.out.println("test1: TEST PASSED - STATUS IS TRUE");
        pw.println(Data.PASSED + ".  Account entities are identical");
      } else {
        System.out.println("test1: TEST FAILED - STATUS IS FALSE");
        pw.println(
            Data.FAILED + ".  ERROR: Account entities are not identical");
      }
    } catch (Exception e) {
      System.out.println("Unexpected exception caught in test1:"
          + convertExceptionToString(e));
      try {
        if (ut.getStatus() != Status.STATUS_NO_TRANSACTION)
          ut.rollback();
      } catch (Exception se) {
        System.out.println(
            "Unexpected exception caught in test1 while checking TX status:"
                + convertExceptionToString(se));
      }
    } finally {
      try {
        ut.begin();
        beanRef.removeTestData();
        ut.commit();
      } catch (Exception e) {
        System.out.println(
            "Unexpected exception caught cleaning up test data in test1:"
                + convertExceptionToString(e));
        try {
          if (ut.getStatus() != Status.STATUS_NO_TRANSACTION)
            ut.rollback();
        } catch (Exception se) {
          System.out.println(
              "Unexpected exception caught in test1 while checking TX status :"
                  + convertExceptionToString(e));
        }
      }
    }
  }

  public void test1a(final HttpServletRequest req,
      final HttpServletResponse res) throws ServletException, IOException {
    String accounts;
    PrintWriter pw = res.getWriter();

    try {
      ut = getTx();
      ut.begin();

      getEM2();

      beanRef2.removeTestData();
      System.out.println("test1a: createAccountData");
      beanRef2.createTestData();
      ut.commit();

      accounts = beanRef2.getAllAccounts();

      if (accounts != null) {
        System.out.println("Accounts[" + accounts + "]");
      }

      Account account = entityManager2.find(Account.class, 5555);

      boolean status = beanRef2.checkAccountStatus(account);
      System.out.println("test1a: status[" + status + "]");

      if (status) {
        System.out.println("test1a: TEST PASSED - STATUS IS TRUE");
        pw.println(Data.PASSED + ".  Account entities are identical");
      } else {
        System.out.println("test1a: TEST FAILED - STATUS IS FALSE");
        pw.println(
            Data.FAILED + ".  ERROR: Account entities are not identical");
      }
    } catch (Exception e) {
      System.out.println("Unexpected exception caught in test1a:"
          + convertExceptionToString(e));
      try {
        if (ut.getStatus() != Status.STATUS_NO_TRANSACTION)
          ut.rollback();
      } catch (Exception se) {
        System.out.println(
            "Unexpected exception caught in test1a while checking TX status:"
                + convertExceptionToString(se));
      }
    } finally {
      try {
        ut.begin();
        beanRef2.removeTestData();
        ut.commit();
      } catch (Exception e) {
        System.out.println(
            "Unexpected exception caught cleaning up test data in test1a:"
                + convertExceptionToString(e));
        try {
          if (ut.getStatus() != Status.STATUS_NO_TRANSACTION)
            ut.rollback();
        } catch (Exception se) {
          System.out.println(
              "Unexpected exception caught in test1a while checking TX status:"
                  + convertExceptionToString(se));
        }
      }
    }
  }

  public void test2(final HttpServletRequest req, final HttpServletResponse res)
      throws ServletException, IOException {

    PrintWriter pw = res.getWriter();

    Double EXPECTED_BALANCE = 10540.75D;
    Double balance;

    try {
      getEM();

      ut = getTx();

      ut.begin();
      beanRef.removeTestData();
      System.out.println("test2: createAccountData");
      beanRef.createTestData();
      ut.commit();
      ut.begin();

      Account ACCOUNT = entityManager.find(Account.class, 1075);

      balance = beanRef.balance(ACCOUNT.id());
      System.out.println("test2: INITIAL BALANCE IS: " + balance);
      balance = beanRef.deposit(ACCOUNT.id(), 100.0);
      System.out.println("test2: BALANCE AFTER DEPOSIT IS: " + balance);
      System.out.println(
          "test2: BALANCE FROM DB AFTER DEPOSIT IS: " + ACCOUNT.balance());
      balance = beanRef.withdraw(ACCOUNT.id(), 50.0);
      System.out.println("test2: BALANCE AFTER WITHDRAW IS: " + balance);
      System.out.println(
          "test2: BALANCE FROM DB AFTER WITHDRAW IS: " + ACCOUNT.balance());

      ut.commit();
      System.out.println("Clearing cache");
      entityManager.getEntityManagerFactory().getCache().evictAll();

      Account updatedAccount = entityManager.find(Account.class, 1075);
      balance = updatedAccount.balance();

      if (EXPECTED_BALANCE.compareTo(balance) == 0) {
        System.out.println("test2: TEST PASSED. BALANCE IS: " + balance);
        pw.println(Data.PASSED + ".  Balance of account as expected");
      } else {
        System.out.println(
            "test2: TEST FAILED - Account balance is not correct.  Expected: "
                + EXPECTED_BALANCE + "got: " + balance);
        pw.println(Data.FAILED
            + ".  ERROR: Account balance is not correct.  Expected: "
            + EXPECTED_BALANCE + "got: " + balance);
      }

    } catch (Exception e) {
      System.out.println("Unexpected exception caught in test2:"
          + convertExceptionToString(e));
      try {
        if (ut.getStatus() != Status.STATUS_NO_TRANSACTION)
          ut.rollback();
      } catch (Exception se) {
        System.out.println(
            "Unexpected exception caught in test2 while checking TX status:"
                + convertExceptionToString(se));
      }
    } finally {
      try {
        ut.begin();
        beanRef.removeTestData();
        ut.commit();
      } catch (Exception e) {
        System.out.println(
            "Unexpected exception caught in test2 while cleaning up test data:"
                + convertExceptionToString(e));
        try {
          if (ut.getStatus() != Status.STATUS_NO_TRANSACTION)
            ut.rollback();
        } catch (Exception se) {
          System.out.println(
              "Unexpected exception caught in test2 while checking TX status :"
                  + convertExceptionToString(se));
        }
      }
    }
  }

  public void test3(final HttpServletRequest req, final HttpServletResponse res)
      throws ServletException, IOException {

    PrintWriter pw = res.getWriter();
    Double INITIAL_BALANCE = 10490.75D;
    Double balance;

    try {

      getEM();

      ut = getTx();

      ut.begin();
      beanRef.removeTestData();
      System.out.println("test3: createAccountData");
      beanRef.createTestData();
      ut.commit();

      ut.begin();
      Account ACCOUNT = entityManager.find(Account.class, 1075);

      balance = beanRef.balance(ACCOUNT.id());
      System.out.println("test3: INITIAL BALANCE IS: " + balance);
      balance = beanRef.deposit(ACCOUNT.id(), 100.0);
      System.out.println("test3: BALANCE AFTER DEPOSIT IS: " + balance);
      System.out.println(
          "test3: BALANCE FROM DB AFTER DEPOSIT IS: " + ACCOUNT.balance());
      balance = beanRef.withdraw(ACCOUNT.id(), 50.0);
      System.out.println("test3: BALANCE AFTER WITHDRAW IS: " + balance);
      System.out.println(
          "test3: BALANCE FROM DB AFTER WITHDRAW IS: " + ACCOUNT.balance());

      ut.rollback();
      System.out.println("Clearing cache");
      entityManager.getEntityManagerFactory().getCache().evictAll();
      Account updatedAccount = entityManager.find(Account.class, 1075);
      balance = updatedAccount.balance();

      if (INITIAL_BALANCE.compareTo(balance) == 0) {
        System.out.println("test3: TEST PASSED. BALANCE IS: " + balance);
        pw.println(Data.PASSED + ".  Balance of account as expected");
      } else {
        System.out.println(
            "test3: TEST FAILED - Account balance is not correct.  Expected: "
                + INITIAL_BALANCE + "got: " + balance);
        pw.println(Data.FAILED
            + ".  ERROR: Account balance is not correct.  Expected: "
            + INITIAL_BALANCE + "got: " + balance);
      }
    } catch (Exception e) {
      System.out.println("Unexpected exception caught in test3:"
          + convertExceptionToString(e));
      try {
        if (ut.getStatus() != Status.STATUS_NO_TRANSACTION)
          ut.rollback();
      } catch (Exception se) {
        System.out.println(
            "Unexpected exception caught in test3 while checking TX status:"
                + convertExceptionToString(se));
      }
    } finally {
      try {
        ut.begin();
        beanRef.removeTestData();
        ut.commit();
      } catch (Exception e) {
        System.out.println(
            "Unexpected exception caught in test3 while cleaning up test data:"
                + convertExceptionToString(e));
        try {
          if (ut.getStatus() != Status.STATUS_NO_TRANSACTION)
            ut.rollback();
        } catch (Exception se) {
          System.out.println(
              "Unexpected exception caught in test3 while checking TX status :"
                  + convertExceptionToString(se));
        }
      }
    }
  }

  public void getTransactionIllegalStateException(final HttpServletRequest req,
      final HttpServletResponse res) throws ServletException, IOException {
    boolean pass = true;
    PrintWriter pw = res.getWriter();
    String testName = "getTransactionIllegalStateException";
    try {
      getEM();

      entityManager.getTransaction();
      pass = false;
      System.out.println(testName + ": Did not throw IllegalStateException");
      pw.println(testName + ": Did not throw IllegalStateException");
    } catch (IllegalStateException ise) {
      System.out
          .println(testName + ": RECEIVED EXPECTED IllegalStateException");
      pw.println(testName + ": RECEIVED EXPECTED IllegalStateException");
    } catch (Exception e) {
      pass = false;
      System.out.println(testName + ": Unexpected exception caught in "
          + testName + ":" + convertExceptionToString(e));
      pw.println(testName + ": Unexpected exception caught in " + testName + ":"
          + convertExceptionToString(e));

    }
    if (pass) {
      System.out.println(testName + ": TEST PASSED");
      pw.println(Data.PASSED + ". TEST PASSED");
    } else {
      System.out.println(testName + ": TEST FAILED");
      pw.println(Data.FAILED + ". TEST FAILED");
    }
  }

  public void closeObjectTransactionRequiredExceptionTest(
      final HttpServletRequest req, final HttpServletResponse res)
      throws ServletException, IOException {
    boolean pass = false;
    PrintWriter pw = res.getWriter();
    String testName = "closeObjectTransactionRequiredExceptionTest";
    try {

      getEM();

      entityManager.close();
      System.out.println(testName + ":Did not throw IllegalStateException");
      pw.println(testName + ":Did not throw IllegalStateException");
    } catch (IllegalStateException ise) {
      pass = true;
      System.out
          .println(testName + ": RECEIVED EXPECTED IllegalStateException");
      pw.println(testName + ": RECEIVED EXPECTED IllegalStateException");
    } catch (Exception e) {
      System.out.println(testName + ":Unexpected exception caught in "
          + testName + ":" + convertExceptionToString(e));
      pw.println(testName + ":Unexpected exception caught in " + testName + ":"
          + convertExceptionToString(e));

    }
    if (pass) {
      System.out.println("DEBUG: TEST PASSED");
      pw.println(Data.PASSED + ". TEST PASSED");
    } else {
      System.out.println("DEBUG: TEST FAILED");
      pw.println(Data.FAILED + ". TEST FAILED");
    }
  }

  public void mergeObjectTransactionRequiredExceptionTest(
      final HttpServletRequest req, final HttpServletResponse res)
      throws ServletException, IOException {
    PrintWriter pw = res.getWriter();
    boolean pass = false;
    String testName = "mergeObjectTransactionRequiredExceptionTest";

    String desc;
    try {

      getEM();

      ut = getTx();

      ut.begin();
      beanRef.removeTestData();
      System.out.println(testName + ": createAccountData");
      beanRef.createTestData();
      ut.commit();

      Account account = entityManager.find(Account.class, 1075);
      // try merge without a transaction being active
      try {
        entityManager.merge(account);

        desc = testName + ":Did not throw TransactionRequiredException";
        TestUtil.logErr(desc);

      } catch (TransactionRequiredException tre) {
        desc = testName + ": Received expected TransactionRequiredException";
        TestUtil.logTrace(desc);
        pass = true;
      } catch (Exception e) {
        desc = testName + ":Received unexpected Exception:"
            + convertExceptionToString(e);
        TestUtil.logErr(desc);
      }

      if (pass) {
        System.out.println(testName + ": TEST PASSED - " + desc);
        pw.println(Data.PASSED + ".  " + desc);
      } else {
        System.out.println(testName + ": TEST FAILED - " + desc);
        pw.println(Data.FAILED + ".  ERROR: " + desc);
      }
    } catch (Exception e) {
      System.out.println(testName + ": Unexpected exception caught");
      e.printStackTrace();

    }
  }

  public void persistObjectTransactionRequiredExceptionTest(
      final HttpServletRequest req, final HttpServletResponse res)
      throws ServletException, IOException {
    PrintWriter pw = res.getWriter();
    boolean pass = false;
    String testName = "persistObjectTransactionRequiredExceptionTest";
    String desc;
    try {

      getEM();

      ut = getTx();

      ut.begin();
      beanRef.removeTestData();
      ut.commit();
      // try persist without a transaction being active
      try {
        Account account = new Account(999, 9.99);
        entityManager.persist(account);

        desc = testName + ":Did not throw TransactionRequiredException";
        TestUtil.logErr(desc);

      } catch (TransactionRequiredException tre) {
        desc = testName + ":Received expected TransactionRequiredException";
        TestUtil.logTrace(desc);
        pass = true;
      } catch (Exception e) {
        desc = testName + ":Received unexpected Exception:"
            + convertExceptionToString(e);
        TestUtil.logErr(desc);
      }

      if (pass) {
        System.out.println(testName + ": TEST PASSED - " + desc);
        pw.println(Data.PASSED + ".  " + desc);
      } else {
        System.out.println(testName + ": TEST FAILED - " + desc);
        pw.println(Data.FAILED + ".  ERROR: " + desc);
      }
    } catch (Exception e) {
      System.out.println(testName + ":Unexpected exception caught");
      e.printStackTrace();

    }
  }

  public void refreshObjectTransactionRequiredExceptionTest(
      final HttpServletRequest req, final HttpServletResponse res)
      throws ServletException, IOException {
    PrintWriter pw = res.getWriter();
    boolean pass = false;
    String testName = "refreshObjectTransactionRequiredExceptionTest";

    String desc;
    try {

      getEM();

      ut = getTx();

      ut.begin();
      beanRef.removeTestData();
      System.out.println(testName + ": createAccountData");
      beanRef.createTestData();
      ut.commit();

      Account account = entityManager.find(Account.class, 1075);
      if (account != null) {
        // try refresh without a transaction being active
        try {
          entityManager.refresh(account);

          desc = testName + ":Did not throw TransactionRequiredException";
          TestUtil.logErr(desc);

        } catch (TransactionRequiredException tre) {
          desc = testName + ": Received expected TransactionRequiredException";
          TestUtil.logTrace(desc);
          pass = true;
        } catch (Exception e) {
          desc = testName + ":Received unexpected Exception:"
              + convertExceptionToString(e);
          TestUtil.logErr(desc);
        }

        if (pass) {
          System.out.println(testName + ": TEST PASSED - " + desc);
          pw.println(Data.PASSED + ".  " + desc);
        } else {
          System.out.println(testName + ": TEST FAILED - " + desc);
          pw.println(Data.FAILED + ".  ERROR: " + desc);
        }
      } else {
        System.out.println(testName + ": TEST FAILED - STATUS IS FALSE");
        pw.println(Data.FAILED + ".  ERROR: Account returned by find was null");
      }
    } catch (Exception e) {
      System.out.println(testName + ": Unexpected exception caught");
      e.printStackTrace();

    }
  }

  public void refreshObjectMapTransactionRequiredExceptionTest(
      final HttpServletRequest req, final HttpServletResponse res)
      throws ServletException, IOException {
    PrintWriter pw = res.getWriter();
    boolean pass = false;
    String testName = "refreshObjectMapTransactionRequiredExceptionTest";
    Map<String, Object> myMap = new HashMap<String, Object>();
    myMap.put("some.cts.specific.property", "nothing.in.particular");
    String desc;
    try {

      getEM();

      ut = getTx();

      ut.begin();
      beanRef.removeTestData();
      System.out.println(testName + ": createAccountData");
      beanRef.createTestData();
      ut.commit();

      Account account = entityManager.find(Account.class, 1075);
      // try refresh without a transaction being active
      if (account != null) {
        try {
          entityManager.refresh(account, myMap);

          desc = testName + ":Did not throw TransactionRequiredException";
          TestUtil.logErr(desc);

        } catch (TransactionRequiredException tre) {
          desc = testName + ": Received expected TransactionRequiredException";
          TestUtil.logTrace(desc);
          pass = true;
        } catch (Exception e) {
          desc = testName + ":Received unexpected Exception:"
              + convertExceptionToString(e);
          TestUtil.logErr(desc);
        }

        if (pass) {
          System.out.println(testName + ": TEST PASSED - " + desc);
          pw.println(Data.PASSED + ".  " + desc);
        } else {
          System.out.println(testName + ": TEST FAILED - " + desc);
          pw.println(Data.FAILED + ".  ERROR: " + desc);
        }
      } else {
        System.out.println(testName + ": TEST FAILED - STATUS IS FALSE");
        pw.println(Data.FAILED + ".  ERROR: Account returned by find was null");
      }
    } catch (Exception e) {
      System.out.println(testName + ": Unexpected exception caught");
      e.printStackTrace();

    }
  }

  public void refreshObjectLockModeTypeTransactionRequiredExceptionTest(
      final HttpServletRequest req, final HttpServletResponse res)
      throws ServletException, IOException {
    PrintWriter pw = res.getWriter();
    boolean pass = false;
    String testName = "refreshObjectLockModeTypeTransactionRequiredExceptionTest";
    String desc;
    try {

      getEM();

      ut = getTx();

      ut.begin();
      beanRef.removeTestData();
      System.out.println(testName + ": createAccountData");
      beanRef.createTestData();
      ut.commit();

      Account account = entityManager.find(Account.class, 1075);
      // try refresh without a transaction being active
      if (account != null) {
        try {
          entityManager.refresh(account, LockModeType.OPTIMISTIC);

          desc = testName + ":Did not throw TransactionRequiredException";
          TestUtil.logErr(desc);

        } catch (TransactionRequiredException tre) {
          desc = testName + ": Received expected TransactionRequiredException";
          TestUtil.logTrace(desc);
          pass = true;
        } catch (Exception e) {
          desc = testName + ":Received unexpected Exception:"
              + convertExceptionToString(e);
          TestUtil.logErr(desc);
        }

        if (pass) {
          System.out.println(testName + ": TEST PASSED - " + desc);
          pw.println(Data.PASSED + ".  " + desc);
        } else {
          System.out.println(testName + ": TEST FAILED - " + desc);
          pw.println(Data.FAILED + ".  ERROR: " + desc);
        }
      } else {
        System.out.println(testName + ": TEST FAILED - STATUS IS FALSE");
        pw.println(Data.FAILED + ".  ERROR: Account returned by find was null");
      }
    } catch (Exception e) {
      System.out.println(testName + ": Unexpected exception caught");
      e.printStackTrace();

    }
  }

  public void refreshObjectLockModeTypeMapTransactionRequiredExceptionTest(
      final HttpServletRequest req, final HttpServletResponse res)
      throws ServletException, IOException {
    PrintWriter pw = res.getWriter();
    boolean pass = false;
    String testName = "refreshObjectLockModeTypeMapTransactionRequiredExceptionTest";
    String desc;
    Map<String, Object> myMap = new HashMap<String, Object>();
    myMap.put("some.cts.specific.property", "nothing.in.particular");
    try {

      getEM();

      ut = getTx();

      ut.begin();
      beanRef.removeTestData();
      System.out.println(testName + ": createAccountData");
      beanRef.createTestData();
      ut.commit();
      Account account = entityManager.find(Account.class, 1075);
      if (account != null) {
        // try refresh without a transaction being active
        try {
          entityManager.refresh(account, LockModeType.OPTIMISTIC, myMap);

          desc = testName + ":Did not throw TransactionRequiredException";
          TestUtil.logErr(desc);

        } catch (TransactionRequiredException tre) {
          desc = testName + ": Received expected TransactionRequiredException";
          TestUtil.logTrace(desc);
          pass = true;
        } catch (Exception e) {
          desc = testName + ":Received unexpected Exception:"
              + convertExceptionToString(e);
          TestUtil.logErr(desc);
        }

        if (pass) {
          System.out.println(testName + ": TEST PASSED - " + desc);
          pw.println(Data.PASSED + ".  " + desc);
        } else {
          System.out.println(testName + ": TEST FAILED - " + desc);
          pw.println(Data.FAILED + ".  ERROR: " + desc);
        }
      } else {
        System.out.println(testName + ": TEST FAILED - STATUS IS FALSE");
        pw.println(Data.FAILED + ".  ERROR: Account returned by find was null");
      }
    } catch (Exception e) {
      System.out.println(testName + ": Unexpected exception caught");
      e.printStackTrace();

    }
  }

  public void removeObjectTransactionRequiredExceptionTest(
      final HttpServletRequest req, final HttpServletResponse res)
      throws ServletException, IOException {
    PrintWriter pw = res.getWriter();
    boolean pass = false;
    String testName = "removeObjectTransactionRequiredExceptionTest";

    String desc;
    try {
      getEM();

      ut = getTx();

      ut.begin();
      beanRef.removeTestData();
      System.out.println(testName + ": createAccountData");
      beanRef.createTestData();
      ut.commit();
      Account account = entityManager.find(Account.class, 1075);
      if (account != null) {
        // try refresh without a transaction being active
        try {
          entityManager.remove(account);

          desc = testName + ":Did not throw TransactionRequiredException";
          TestUtil.logErr(desc);

        } catch (TransactionRequiredException tre) {
          desc = testName + ": Received expected TransactionRequiredException";
          TestUtil.logTrace(desc);
          pass = true;
        } catch (Exception e) {
          desc = testName + ":Received unexpected Exception:"
              + convertExceptionToString(e);
          TestUtil.logErr(desc);
        }

        if (pass) {
          System.out.println(testName + ": TEST PASSED - " + desc);
          pw.println(Data.PASSED + ".  " + desc);
        } else {
          System.out.println(testName + ": TEST FAILED - " + desc);
          pw.println(Data.FAILED + ".  ERROR: " + desc);
        }
      } else {
        System.out.println(testName + ": TEST FAILED - STATUS IS FALSE");
        pw.println(Data.FAILED + ".  ERROR: Account returned by find was null");
      }
    } catch (Exception e) {
      System.out.println(testName + ": Unexpected exception caught");
      e.printStackTrace();

    }
  }

}
