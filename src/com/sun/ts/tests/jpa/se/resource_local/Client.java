/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.se.resource_local;

import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.CleanupMethod;
import com.sun.ts.lib.harness.SetupMethod;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.RollbackException;
import jakarta.persistence.SynchronizationType;

public class Client extends PMClientBase {

  private EntityManager entityManager;

  private EntityTransaction entityTransaction;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Fault {
    try {

      super.setup(args, p);
      removeTestData();
    } catch (Exception e) {
      throw new Fault("Setup Failed!", e);
    }
  }

  public void setupOnly(String[] args, Properties p) throws Fault {
    try {
      super.setup(args, p);
    } catch (Exception e) {
      throw new Fault("Setup Failed!", e);
    }
  }
  /*
   * @testName: test1
   * 
   * @assertion_ids: PERSISTENCE:SPEC:859; PERSISTENCE:SPEC:860;
   * PERSISTENCE:JAVADOC:66; PERSISTENCE:JAVADOC:59
   * 
   * @test_Strategy: EntityTransactionInterface
   *
   * begin() starts a resource_transaction
   */

  public void test1() throws Fault {

    TestUtil.logTrace("Begin test1");
    boolean pass = false;

    try {
      entityTransaction = getEntityTransaction();
      entityTransaction.begin();

      pass = entityTransaction.isActive();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    } finally {
      try {
        if (entityTransaction.isActive()) {
          entityTransaction.rollback();
        }
      } catch (PersistenceException e) {
        TestUtil.logErr("Unexpected Exception rolling back TX", e);
      }
    }

    if (!pass)
      throw new Fault("test1 failed");
  }

  /*
   * @testName: test2
   * 
   * @assertion_ids: PERSISTENCE:SPEC:860; PERSISTENCE:JAVADOC:66
   * 
   * @test_Strategy: EntityTransactionInterface
   *
   * begin() throws an IllegalStateException if isActive() is true
   */

  public void test2() throws Fault {

    TestUtil.logTrace("Begin test2");
    boolean pass = false;

    try {
      entityTransaction = getEntityTransaction();
      entityTransaction.begin();

      if (entityTransaction.isActive()) {
        entityTransaction.begin();
      }

    } catch (IllegalStateException iae) {
      TestUtil.logTrace("Caught Expected IllegalStateException");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception Caught", e);
    } finally {
      try {
        if (entityTransaction.isActive()) {
          entityTransaction.rollback();
        }
      } catch (PersistenceException e) {
        TestUtil.logErr("Unexpected Exception rolling back TX", e);
      }
    }

    if (!pass)
      throw new Fault("test2 failed");
  }

  /*
   * @testName: test3
   * 
   * @assertion_ids: PERSISTENCE:SPEC:860; PERSISTENCE:JAVADOC:67
   * 
   * @test_Strategy: EntityTransactionInterface
   *
   * commit() commits the current transaction
   */

  public void test3() throws Fault {

    TestUtil.logTrace("Begin test3");
    boolean pass = false;

    try {
      A newA = new A("3", "test3", 3);
      entityTransaction = getEntityTransaction();

      entityTransaction.begin();
      TestUtil.logTrace("Persist Entity newA");
      getEntityManager().persist(newA);
      entityTransaction.commit();

      entityTransaction.begin();
      TestUtil.logTrace("find newA");
      A modifiedA = getEntityManager().find(A.class, "3");

      if ((null != modifiedA) && (modifiedA.getName().equals("test3"))) {
        TestUtil.logTrace("setName");
        modifiedA.setName("test3Modified");
      }
      getEntityManager().merge(modifiedA);
      entityTransaction.commit();

      A committedA = getEntityManager().find(A.class, "3");

      TestUtil.logTrace("check results");
      if (committedA.getName().equals("test3Modified")) {
        TestUtil.logTrace("PASSED");
        pass = true;
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception Caught", e);
    } finally {
      try {
        if (entityTransaction.isActive()) {
          entityTransaction.rollback();
        }
      } catch (PersistenceException e) {
        TestUtil.logErr("Unexpected Exception rolling back TX", e);
      }
    }

    if (!pass)
      throw new Fault("test3 failed");
  }

  /*
   * @testName: test4
   * 
   * @assertion_ids: PERSISTENCE:SPEC:860; PERSISTENCE:JAVADOC:67
   * 
   * @test_Strategy: EntityTransactionInterface
   *
   * commit() throws an IllegalStateException if isActive() is false
   */

  public void test4() throws Fault {

    TestUtil.logTrace("Begin test4");
    boolean pass = false;

    try {
      entityTransaction = getEntityTransaction();

      if (!entityTransaction.isActive()) {
        entityTransaction.commit();
      }

    } catch (IllegalStateException iae) {
      TestUtil.logTrace("Caught Expected IllegalStateException");
      pass = true;
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected Exception Caught", e);
    } finally {
      try {
        if (entityTransaction.isActive()) {
          entityTransaction.rollback();
        }
      } catch (PersistenceException e) {
        TestUtil.logErr("Unexpected Exception rolling back TX", e);
      }
    }

    if (!pass)
      throw new Fault("test4 failed");
  }

  /*
   * @testName: test5
   * 
   * @assertion_ids: PERSISTENCE:SPEC:860; PERSISTENCE:JAVADOC:67;
   * PERSISTENCE:JAVADOC:190; PERSISTENCE:JAVADOC:185
   * 
   * @test_Strategy: EntityTransactionInterface
   *
   * commit() throws a RollbackException if commit fails
   */

  public void test5() throws Fault {

    TestUtil.logTrace("Begin test5");
    boolean pass = false;

    try {
      entityTransaction = getEntityTransaction();
      A newA = new A("5", "test5", 5);

      entityTransaction.begin();
      getEntityManager().persist(newA);
      entityTransaction.setRollbackOnly();
      entityTransaction.commit();

    } catch (RollbackException re) {
      TestUtil.logTrace("RollbackException Caught as Expected");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception Caught", e);
    } finally {
      try {
        if (entityTransaction.isActive()) {
          entityTransaction.rollback();
        }
      } catch (PersistenceException e) {
        TestUtil.logErr("Unexpected Exception rolling back TX", e);
      }
    }

    if (!pass)
      throw new Fault("test5 failed");
  }

  /*
   * @testName: test6
   * 
   * @assertion_ids: PERSISTENCE:SPEC:860; PERSISTENCE:JAVADOC:70
   * 
   * @test_Strategy: EntityTransactionInterface
   *
   * rollback() rolls back the current transaction
   */

  public void test6() throws Fault {

    TestUtil.logTrace("Begin test6");
    boolean pass = false;

    try {
      entityTransaction = getEntityTransaction();
      A newA = new A("6", "test6", 6);

      entityTransaction.begin();
      getEntityManager().persist(newA);
      entityTransaction.commit();

      entityTransaction.begin();
      A modifiedA = getEntityManager().find(A.class, "6");

      if ((null != modifiedA) && (modifiedA.getName().equals("test6"))) {
        modifiedA.setName("test6Modified");
      }
      getEntityManager().merge(modifiedA);
      entityTransaction.rollback();

      A rolledBackA = getEntityManager().find(A.class, "6");

      if (rolledBackA.getName().equals("test6")) {
        pass = true;
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception Caught", e);
    } finally {
      try {
        if (entityTransaction.isActive()) {
          entityTransaction.rollback();
        }
      } catch (PersistenceException e) {
        TestUtil.logErr("Unexpected Exception rolling back TX", e);
      }
    }

    if (!pass)
      throw new Fault("test6 failed");
  }

  /*
   * @testName: test7
   * 
   * @assertion_ids: PERSISTENCE:SPEC:860; PERSISTENCE:JAVADOC:71
   * 
   * @test_Strategy: EntityTransactionInterface
   *
   * setRollbackOnly() marks the current transaction so the only outcome is for
   * the transaction to be rolled back
   */

  public void test7() throws Fault {

    TestUtil.logTrace("Begin test7");
    boolean pass = false;

    try {
      A newA = new A("7", "test7", 7);
      entityTransaction = getEntityTransaction();

      entityTransaction.begin();
      getEntityManager().persist(newA);

      entityTransaction.setRollbackOnly();

      if ((entityTransaction.isActive())
          && (entityTransaction.getRollbackOnly())) {
        pass = true;
      }

      entityTransaction.rollback();

    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected Exception Caught", e);
    } finally {
      try {
        if (entityTransaction.isActive()) {
          entityTransaction.rollback();
        }
      } catch (PersistenceException e) {
        TestUtil.logErr("Unexpected Exception rolling back TX", e);
      }
    }

    if (!pass)
      throw new Fault("test7 failed");
  }

  /*
   * @testName: test8
   * 
   * @assertion_ids: PERSISTENCE:SPEC:860; PERSISTENCE:JAVADOC:71
   * 
   * @test_Strategy: EntityTransactionInterface
   *
   * setRollbackOnly throws IllegalStateException if isActive() is false
   */

  public void test8() throws Fault {

    TestUtil.logTrace("Begin test8");
    boolean pass = false;

    try {
      entityTransaction = getEntityTransaction();

      if (!entityTransaction.isActive()) {
        entityTransaction.setRollbackOnly();
        pass = true;
      }

    } catch (IllegalStateException iae) {
      TestUtil.logTrace("Caught Expected IllegalStateException");
      pass = true;
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (entityTransaction.isActive()) {
          entityTransaction.rollback();
        }
      } catch (PersistenceException e) {
        TestUtil.logErr("Unexpected Exception rolling back TX", e);
      }
    }

    if (!pass)
      throw new Fault("test8 failed");
  }

  /*
   * @testName: test9
   * 
   * @assertion_ids: PERSISTENCE:SPEC:860; PERSISTENCE:JAVADOC:68
   * 
   * @test_Strategy: EntityTransactionInterface
   *
   * getRollbackOnly determines if the current transaction has been marked for
   * rollback test getRollbackOnly when isActive() is true and TX has been
   * marked for rollback, so getRollbackOnly will return true
   */

  public void test9() throws Fault {

    TestUtil.logTrace("Begin test9");
    boolean pass = false;

    try {
      A newA = new A("9", "test9", 9);
      entityTransaction = getEntityTransaction();

      entityTransaction.begin();
      getEntityManager().persist(newA);
      entityTransaction.setRollbackOnly();

      if ((entityTransaction.isActive())
          && (entityTransaction.getRollbackOnly())) {
        pass = true;
      }

      entityTransaction.rollback();

    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected Exception Caught", e);
    } finally {
      try {
        if (entityTransaction.isActive()) {
          entityTransaction.rollback();
        }
      } catch (PersistenceException e) {
        TestUtil.logErr("Unexpected Exception rolling back TX", e);
      }
    }

    if (!pass)
      throw new Fault("test9 failed");
  }

  /*
   * @testName: test10
   * 
   * @assertion_ids: PERSISTENCE:SPEC:860; PERSISTENCE:JAVADOC:68
   * 
   * @test_Strategy: EntityTransactionInterface
   *
   * getRollbackOnly throws IllegalStateException if isActive() is false
   */

  public void test10() throws Fault {

    TestUtil.logTrace("Begin test10");
    boolean pass = false;

    try {
      entityTransaction = getEntityTransaction();

      if (!entityTransaction.isActive()) {
        entityTransaction.getRollbackOnly();
      }

    } catch (IllegalStateException iae) {
      TestUtil.logTrace("Caught Expected IllegalStateException");
      pass = true;

    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception Caught", e);
    } finally {
      try {
        if (entityTransaction.isActive()) {
          entityTransaction.rollback();
        }
      } catch (PersistenceException e) {
        TestUtil.logErr("Unexpected Exception rolling back TX", e);
      }
    }

    if (!pass)
      throw new Fault("test10 failed");
  }

  /*
   * @testName: test11
   * 
   * @assertion_ids: PERSISTENCE:SPEC:860; PERSISTENCE:JAVADOC:68
   * 
   * @test_Strategy: EntityTransactionInterface test getRollbackOnly when
   * isActive() is true and TX has not been marked for rollback, so
   * getRollbackOnly will return false
   *
   */

  public void test11() throws Fault {

    TestUtil.logTrace("Begin test11");
    boolean pass = false;

    try {
      A newA = new A("11", "test11", 11);
      entityTransaction = getEntityTransaction();

      entityTransaction.begin();
      getEntityManager().persist(newA);

      if ((entityTransaction.isActive())
          && (!entityTransaction.getRollbackOnly())) {
        pass = true;
      }

      entityTransaction.rollback();

    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected Exception Caught", e);
    } finally {
      try {
        if (entityTransaction.isActive()) {
          entityTransaction.rollback();
        }
      } catch (PersistenceException e) {
        TestUtil.logErr("Unexpected Exception rolling back TX", e);
      }
    }

    if (!pass)
      throw new Fault("test11 failed");
  }

  /*
   * @testName: test12
   * 
   * @assertion_ids: PERSISTENCE:SPEC:860; PERSISTENCE:JAVADOC:69;
   * PERSISTENCE:JAVADOC:157
   * 
   * @test_Strategy: EntityTransactionInterface
   *
   * isActive() indicates whether a transaction is in progress Try when TX is
   * active
   */

  public void test12() throws Fault {

    TestUtil.logTrace("Begin test12");
    boolean pass = false;

    try {
      entityTransaction = getEntityTransaction();

      entityTransaction.begin();

      pass = entityTransaction.isActive();

      entityTransaction.rollback();

    } catch (PersistenceException e) {
      pass = false;
      TestUtil.logErr("Unexpected PersistenceException Caught", e);
    } finally {
      try {
        if (entityTransaction.isActive()) {
          entityTransaction.rollback();
        }
      } catch (PersistenceException e) {
        TestUtil.logErr("Unexpected PersistenceException rolling back TX", e);
      }
    }

    if (!pass)
      throw new Fault("test12 failed");
  }

  /*
   * @testName: test13
   * 
   * @assertion_ids: PERSISTENCE:SPEC:860; PERSISTENCE:JAVADOC:69
   * 
   * @test_Strategy: EntityTransactionInterface
   *
   * isActive() indicates whether a transaction is in progress Try when TX is
   * not active
   */

  public void test13() throws Fault {

    TestUtil.logTrace("Begin test13");
    boolean pass = false;

    try {
      entityTransaction = getEntityTransaction();

      pass = !entityTransaction.isActive();

    } catch (PersistenceException e) {
      pass = false;
      TestUtil.logErr("Unexpected PersistenceException Caught", e);
    } finally {
      try {
        if (entityTransaction.isActive()) {
          entityTransaction.rollback();
        }
      } catch (PersistenceException e) {
        TestUtil.logErr("Unexpected PersistenceException rolling back TX", e);
      }
    }

    if (!pass)
      throw new Fault("test13 failed");
  }

  /*
   * @testName: test14
   * 
   * @assertion_ids: PERSISTENCE:SPEC:860; PERSISTENCE:JAVADOC:72;
   * PERSISTENCE:SPEC:741
   * 
   * @test_Strategy: EntityTransactionInterface
   *
   * When a resource-local entity manager is used, and the persistence provider
   * runtime throws an exception defined to cause a transaction rollback, it
   * must mark the transaction for rollback.
   */

  public void test14() throws Fault {

    TestUtil.logTrace("Begin test14");
    boolean pass = false;

    try {
      A newA = new A("14", "test14", 14);
      entityTransaction = getEntityTransaction();

      entityTransaction.begin();
      getEntityManager().persist(newA);
      getEntityManager().flush();
      entityTransaction.commit();

      A newA2 = new A("14", "test14_2", 14);
      entityTransaction = getEntityTransaction();

      entityTransaction.begin();
      try {
        getEntityManager().persist(newA2);
        getEntityManager().flush();
      } catch (Exception ex) {
        TestUtil.logTrace("newA2 has the same PK as newA");
        pass = entityTransaction.getRollbackOnly();
      }
      entityTransaction.rollback();
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected Exception Caught", e);
    } finally {
      try {
        if (entityTransaction.isActive()) {
          entityTransaction.rollback();
        }
      } catch (PersistenceException e) {
        TestUtil.logErr("Unexpected PersistenceException rolling back TX", e);
      }
    }

    if (!pass)
      throw new Fault("test14 failed");
  }

  /*
   * @testName: test15
   * 
   * @assertion_ids: PERSISTENCE:SPEC:882; PERSISTENCE:JAVADOC:48;
   * PERSISTENCE:JAVADOC:49
   * 
   * @test_Strategy: After the close method has been invoked, all methods on the
   * EntityManager instance will throw the IllegalStateException except for
   * getTransaction.
   *
   * 
   */

  public void test15() throws Fault {

    TestUtil.logTrace("Begin test15");
    boolean pass = false;
    EntityTransaction et = null;
    entityManager = getEntityManager();

    if (!entityManager.isOpen()) {
      this.entityManager = getEntityManager();
    }

    try {
      entityManager.close();

      if (!entityManager.isOpen()) {
        et = entityManager.getTransaction();
      }
      if (et != null) {
        pass = true;
      } else {
        TestUtil.logErr("EntityTransaction returned was null");
      }

    } catch (IllegalStateException ise) {
      TestUtil.logErr("Unexpected exception", ise);
    } finally {
      try {
        if (et != null) {
          if (et.isActive()) {
            et.rollback();
          }
        } else {
          TestUtil.logErr("EntityTransaction is null");
        }
      } catch (PersistenceException e) {
        TestUtil.logErr("Unexpected PersistenceException rolling back TX", e);
      }
    }

    if (!pass)
      throw new Fault("test15 failed");
  }

  /*
   * @testName:
   * createEntityManagerSynchronizationTypeMapIllegalStateExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:3319;
   * 
   * @test_Strategy:
   */
  public void createEntityManagerSynchronizationTypeMapIllegalStateExceptionTest()
      throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;

    Properties p = getPersistenceUnitProperties();
    TestUtil.logMsg("Testing for resource-local entity managers");
    EntityManagerFactory emf = Persistence
        .createEntityManagerFactory(getPersistenceUnitName(), p);

    displayMap(emf.getProperties());
    try {
      emf.createEntityManager(SynchronizationType.UNSYNCHRONIZED, p);
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException iae) {
      TestUtil.logTrace("Caught Expected IllegalStateException");
      pass1 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    TestUtil.logMsg("Testing when EMF is closed");
    emf = Persistence.createEntityManagerFactory(getPersistenceUnitName(), p);
    displayMap(emf.getProperties());
    try {
      emf.close();
      emf.createEntityManager(SynchronizationType.UNSYNCHRONIZED, p);
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException iae) {
      TestUtil.logTrace("Caught Expected IllegalStateException");
      pass2 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass1 || !pass2) {
      throw new Fault(
          "createEntityManagerSynchronizationTypeMapIllegalStateExceptionTest failed");
    }
  }

  /*
   * @testName: createEntityManagerSynchronizationTypeIllegalStateExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:3323;
   * 
   * @test_Strategy:
   */
  @SetupMethod(name = "setupOnly")
  @CleanupMethod(name = "cleanupOnly")

  public void createEntityManagerSynchronizationTypeIllegalStateExceptionTest()
      throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    Properties p = getPersistenceUnitProperties();
    TestUtil.logMsg("Testing for resource-local entity managers");
    EntityManagerFactory emf = Persistence
        .createEntityManagerFactory(getPersistenceUnitName(), p);

    displayMap(emf.getProperties());
    try {
      emf.createEntityManager(SynchronizationType.UNSYNCHRONIZED);
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException iae) {
      TestUtil.logTrace("Caught Expected IllegalStateException");
      pass1 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    TestUtil.logMsg("Testing when EMF is closed");
    emf = Persistence.createEntityManagerFactory(getPersistenceUnitName(), p);
    displayMap(emf.getProperties());
    try {
      emf.close();
      emf.createEntityManager(SynchronizationType.UNSYNCHRONIZED);
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException iae) {
      TestUtil.logTrace("Caught Expected IllegalStateException");
      pass2 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass1 || !pass2) {
      throw new Fault(
          "createEntityManagerSynchronizationTypeIllegalStateExceptionTest failed");
    }
  }

  public void cleanupOnly() throws Fault {
    TestUtil.logTrace("cleanupOnly");
    super.cleanup();
  }

  public void cleanup() throws Fault {
    TestUtil.logTrace("cleanup");
    removeTestData();
    TestUtil.logTrace("cleanup complete, calling super.cleanup");
    super.cleanup();
  }

  private void removeTestData() {
    TestUtil.logTrace("removeTestData");
    if (getEntityManager().isOpen()) {
      if (getEntityTransaction().isActive()) {
        getEntityTransaction().rollback();
      }
      try {
        getEntityTransaction().begin();
        getEntityManager().createNativeQuery("DELETE FROM AEJB_1X1_BI_BTOB")
            .executeUpdate();
        getEntityManager().createNativeQuery("DELETE FROM BEJB_1X1_BI_BTOB")
            .executeUpdate();
        getEntityTransaction().commit();
      } catch (Exception e) {
        TestUtil.logErr("Exception encountered while removing entities:", e);
      } finally {
        try {
          if (getEntityTransaction().isActive()) {
            getEntityTransaction().rollback();
          }
        } catch (Exception re) {
          TestUtil.logErr("Unexpected Exception in removeTestData:", re);
        }
      }
    } else {
      TestUtil.logTrace("EntityManager is closed. No need to remove data");
    }
  }

}
