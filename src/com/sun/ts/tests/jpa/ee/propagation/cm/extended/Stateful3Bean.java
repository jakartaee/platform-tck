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

package com.sun.ts.tests.jpa.ee.propagation.cm.extended;

import com.sun.ts.lib.util.RemoteLoggingInitException;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.ee.common.Account;
import com.sun.ts.tests.jpa.ee.common.B;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.Properties;

@Stateful(name = "Stateful3Bean")
@Remote({ Stateful3IF.class })
public class Stateful3Bean implements Stateful3IF {

  /*
   * If multiple persistence units exist the unitName element must be specified.
   * In this archive, only one persistence unit exists; thus, unitName is
   * omitted.
   */

  @PersistenceContext(type = PersistenceContextType.EXTENDED, unitName = "CTS-EXT-UNIT")
  private EntityManager entityManager;

  public SessionContext sessionContext;

  private static final B bRef[] = new B[5];

  @EJB(beanName = "TellerBean", beanInterface = Teller.class)
  private Teller beanRef;

  @Resource
  public void setSessionContext(SessionContext sessionContext) {
    this.sessionContext = sessionContext;
  }

  public void init(final Properties p) {
    TestUtil.logTrace("init");
    try {
      TestUtil.init(p);
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    }
  }

  public void createTestData() {
    try {

      TestUtil.logTrace("createTestData");

      TestUtil.logTrace("Create 5 Bees");
      bRef[0] = new B("1", "customerB1", 1);
      bRef[1] = new B("2", "customerB2", 2);
      bRef[2] = new B("3", "customerB3", 3);
      bRef[3] = new B("4", "customerB4", 4);
      bRef[4] = new B("5", "customerB5", 5);

      TestUtil.logTrace("Start to persist Bees ");
      for (B b : bRef) {
        if (b != null) {
          entityManager.persist(b);
          TestUtil.logTrace("persisted B " + b);
        }
      }
      entityManager.flush();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected while creating test data:" + e);
    }
  }

  public void removeTestData() {
    TestUtil.logTrace("removeTestData");
    try {
      entityManager.createNativeQuery("DELETE FROM BEJB_1X1_BI_BTOB")
          .executeUpdate();
      entityManager.createNativeQuery("DELETE FROM AEJB_1X1_BI_BTOB")
          .executeUpdate();
    } catch (Exception e) {
      TestUtil.logErr("Exception encountered while removing entities:", e);
    }
    // clear the cache if the provider supports caching otherwise
    // the evictAll is ignored.
    TestUtil.logTrace("Clearing cache");
    entityManager.getEntityManagerFactory().getCache().evictAll();

  }

  public boolean test1() {

    TestUtil.logTrace("Begin test1");
    boolean pass = false;

    try {
      removeTestData();

      createTestData();
      B anotherB = entityManager.find(B.class, "3");

      if (anotherB != null) {
        TestUtil.logTrace("newB found" + anotherB.getName());
        pass = true;
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception :", e);
      pass = false;
    } finally {
      removeTestData();
    }
    return pass;
  }

  /*
   * test_Strategy: getTransaction will throw an IllegalStateException if
   * invoked on a Container-Managed JTA EM
   *
   */

  public boolean test2() {

    TestUtil.logTrace("Begin test2");
    boolean pass = false;

    try {
      entityManager.getTransaction();
    } catch (IllegalStateException ise) {
      pass = true;
      TestUtil.logTrace("IllegalStateException Caught as Expected: " + ise);
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception :", e);
    }
    return pass;
  }

  public boolean test3() {
    String accounts;
    boolean pass = false;

    try {
      beanRef.removeTestData();
      TestUtil.logTrace("DEBUG:  createAccountData");
      beanRef.createTestData();

      accounts = beanRef.getAllAccounts();

      if (accounts != null) {
        TestUtil.logTrace(accounts);
      }

      Account ACCOUNT = entityManager.find(Account.class, 1075);

      pass = beanRef.checkAccountStatus(ACCOUNT);

    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected Exception:", e);
    } finally {
      beanRef.removeTestData();
    }
    return pass;
  }

  public boolean test4() {
    Double EXPECTED_BALANCE = 10540.75D;
    Double balance;
    boolean pass = false;

    try {
      beanRef.removeTestData();
      System.out.println("DEBUG:  createAccountData");
      beanRef.createTestData();

      Account ACCOUNT = entityManager.find(Account.class, 1075);

      balance = beanRef.balance(ACCOUNT.id());
      balance = beanRef.deposit(ACCOUNT.id(), 100.0);
      balance = beanRef.withdraw(ACCOUNT.id(), 50.0);

      if (EXPECTED_BALANCE.equals(balance)) {
        TestUtil.logTrace("Expected balance received.");
        pass = true;
      } else {
        TestUtil.logErr(" Did not get Expected balance, got:" + balance
            + "Expected: " + EXPECTED_BALANCE);
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception:", e);
    } finally {
      beanRef.removeTestData();
    }
    return pass;
  }

  public boolean test5() {
    boolean pass = false;
    boolean pass1 = false;

    try {
      removeTestData();
      TestUtil.logTrace("createTestData");
      createTestData();
      TestUtil.logTrace("find customerB");
      B customerB = entityManager.find(B.class, "4");

      if (null != customerB) {
        TestUtil.logTrace("check customer status");
        pass1 = beanRef.checkCustomerStatus(customerB);
      }

      entityManager.refresh(customerB);

      if ((pass1) && (customerB.getA().getName().equals("customerA9"))) {
        pass = true;
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception:", e);
    } finally {
      removeTestData();
    }
    return pass;
  }

  public boolean test6() {
    boolean pass = false;

    try {

      TestUtil.logTrace("find customerB");
      B customerB = entityManager.find(B.class, "3");

      if (null != customerB) {
        TestUtil.logTrace("customer is not null, call rollbackStatus()");
        pass = beanRef.rollbackStatus(customerB);
      }

    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected Exception:", e);
    }
    return pass;
  }

  public boolean verifyTest6() {
    boolean pass = false;

    try {

      TestUtil.logTrace("verifyTest6:  find customerB");
      B customerB = entityManager.find(B.class, "3");

      if ((customerB.getName().equals("customerB3"))) {
        pass = true;
      } else {
        TestUtil.logErr(" did not get the expected result.  Expected"
            + " customerB3, got: " + customerB.getName());
      }

    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected Exception in verifyTest6:", e);
    } finally {
      removeTestData();

    }
    return pass;
  }

  public boolean test7() {
    boolean pass = false;
    boolean pass1 = false;

    try {
      removeTestData();
      TestUtil.logTrace("createTestData");
      createTestData();
      B customerB = entityManager.find(B.class, "5");

      if (null != customerB) {
        TestUtil.logTrace("customer is not null, call flushStatus()");
        pass1 = beanRef.flushStatus(customerB);

        TestUtil.logTrace(
            "refresh customerB entity to be sure to get actual state");
        entityManager.refresh(customerB);

        if ((pass1) && (customerB.getName().equals("flushB"))) {
          pass = true;
        } else {
          TestUtil.logErr(" did not get the expected result.  Expected"
              + " flushB, got: " + customerB.getName());
        }
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected Exception:", e);
    } finally {
      removeTestData();

    }
    return pass;
  }

}
