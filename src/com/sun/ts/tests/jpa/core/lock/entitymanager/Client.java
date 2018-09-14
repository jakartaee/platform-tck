/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.core.lock.entitymanager;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import javax.persistence.LockModeType;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Client extends PMClientBase {

  public Client() {
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup");
    try {
      super.setup(args, p);
      TestUtil.logTrace("Cleanup data");
      removeTestData();
      TestUtil.logTrace("Create Test data");
      createTestData();
      TestUtil.logTrace("Done creating test data");

    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  /*
   * @testName: findTest1
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:325
   * 
   * @test_Strategy:
   * 
   * find(Class entityClass, Object PK, LockModeType lck)
   * 
   * Find by primary key and lock. Search for an entity of the specified class
   * and primary key and lock it with respect to the specified lock type. If the
   * entity instance is contained in the persistence context it is returned from
   * there, and the effect of this method is the same as if the lock method had
   * been called on the entity. If the entity is found within the persistence
   * context and the lock mode type is pessimistic and the entity has a version
   * attribute, the persistence provider must perform optimistic version checks
   * when obtaining the database lock. If these checks fail, the
   * OptimisticLockException will be thrown. If the lock mode type is
   * pessimistic and the entity instance is found but cannot be locked: - the
   * PessimisticLockException will be thrown if the database locking failure
   * causes transaction-level rollback. - the LockTimeoutException will be
   * thrown if the database locking failure causes only statement-level rollback
   * 
   */
  public void findTest1() throws Fault {

    TestUtil.logTrace("Begin findTest1");
    boolean pass = false;

    getEntityTransaction().begin();

    try {

      Coffee coffeeFound = getEntityManager().find(Coffee.class, 1,
          LockModeType.PESSIMISTIC_READ);

      if (coffeeFound != null) {
        TestUtil.logTrace("Found coffee as expected");
        pass = true;
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception in rollback:", re);
      }
    }

    if (!pass) {
      throw new Fault("findTest1 failed");
    }
  }

  /*
   * @testName: findTest2
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:326
   * 
   * @test_Strategy:
   * 
   * find(Class entityClass, Object PK, LockModeType lck, Map<String, Object>
   * properties)
   *
   * 
   * Find by primary key and lock, using specified properties. Search for an
   * entity of the specified class and primary key and lock it with respect to
   * the specified lock type. If the entity instance is contained in the
   * persistence context it is returned from there. If the entity is found
   * within the persistence context and the lock mode type is pessimistic and
   * the entity has a version attribute, the persistence provider must perform
   * optimistic version checks when obtaining the database lock. If these checks
   * fail, the OptimisticLockException will be thrown. If the lock mode type is
   * pessimistic and the entity instance is found but cannot be locked: - the
   * PessimisticLockException will be thrown if the database locking failure
   * causes transaction-level rollback. - the LockTimeoutException will be
   * thrown if the database locking failure causes only statement-level rollback
   * If a vendor-specific property or hint is not recognized, it is silently
   * ignored. Portable applications should not rely on the standard timeout
   * hint. Depending on the database in use and the locking mechanisms used by
   * the provider, the hint may or may not be observed
   * 
   */
  public void findTest2() throws Fault {

    TestUtil.logTrace("Begin findTest2");
    boolean pass = false;

    getEntityTransaction().begin();

    Map<String, Object> myMap = new HashMap<String, Object>();
    myMap.put("some.cts.specific.property", "nothing.in.particular");

    try {

      Coffee coffeeFound = getEntityManager().find(Coffee.class, 1,
          LockModeType.PESSIMISTIC_READ, myMap);

      if (coffeeFound != null) {
        TestUtil.logTrace("Found coffee as expected");
        pass = true;
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception in rollback:", re);
      }
    }

    if (!pass) {
      throw new Fault("findTest2 failed");
    }
  }

  /*
   * @testName: findTest3
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:325
   * 
   * @test_Strategy:
   * 
   * find(Class entityClass, Object PK, LockModeType lck)
   *
   * 1) Create an entity manager 2) Lock entity (coffee) in em1 using
   * PESSIMISTIC_READ lock 3) Try to Lock the same entity for coffee with a
   * PESSIMISTIC_WRITE lock
   *
   */
  public void findTest3() throws Fault {

    TestUtil.logTrace("Begin findTest3");
    boolean pass = false;

    try {

      getEntityTransaction().begin();

      TestUtil.logTrace("locate Entity Coffee in EntityManager em1 and lock");
      Coffee coffeeFound = getEntityManager().find(Coffee.class, 1,
          LockModeType.PESSIMISTIC_READ);

      TestUtil.logTrace("locate Entity Coffee in EntityManager em2 and update");
      Coffee coffeeFound2 = getEntityManager().find(Coffee.class, 1,
          LockModeType.PESSIMISTIC_WRITE);

      coffeeFound2.setPrice(6.0F);
      getEntityTransaction().commit();
      pass = true;

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception in rollback:", re);
      }
    }

    if (!pass) {
      throw new Fault("findTest3 failed");
    }
  }

  /*
   * @testName: findTest4
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:324
   * 
   * @test_Strategy:
   *
   * find(Class entityClass, Object PK, Map<String, Object> properties)
   *
   * Find by primary key, using the specified properties. Search for an entity
   * of the specified class and primary key. If the entity instance is contained
   * in the persistence context, it is returned from there. If a vendor-specific
   * property or hint is not recognized, it is silently ignored.
   *
   */
  public void findTest4() throws Fault {

    TestUtil.logTrace("Begin findTest1");
    boolean pass = false;

    getEntityTransaction().begin();

    Map<String, Object> myMap = new HashMap<String, Object>();
    myMap.put("some.cts.specific.property", "nothing.in.particular");

    try {

      Coffee coffeeFound = getEntityManager().find(Coffee.class, 1, myMap);

      if (coffeeFound != null) {
        TestUtil.logTrace("Found coffee as expected");
        pass = true;
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception in rollback:", re);
      }
    }

    if (!pass) {
      throw new Fault("findTest4 failed");
    }
  }

  /*
   * @testName: lockTest1
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:51
   * 
   * @test_Strategy:
   * 
   * public void lock(Object entity, LockModeType lockMode); 1) Create One
   * entity manager 2) Lock entity (coffee) in em1 using PESSIMISTIC_WRITE lock
   * 3) Try to obtain same Lock for cofee the same object and modify its
   * contents
   *
   */
  public void lockTest1() throws Fault {
    TestUtil.logTrace("Begin lockTest1");
    boolean pass = false;

    try {

      getEntityTransaction().begin();

      TestUtil.logTrace("locate Entity Coffee in EntityManager em1 and lock");
      Coffee coffeeFound = getEntityManager().find(Coffee.class, 1);
      getEntityManager().lock(coffeeFound, LockModeType.PESSIMISTIC_WRITE);

      TestUtil.logTrace("locate Entity Coffee in EntityManager and update");
      Coffee coffeeFound2 = getEntityManager().find(Coffee.class, 1);
      getEntityManager().lock(coffeeFound2, LockModeType.PESSIMISTIC_WRITE);
      coffeeFound2.setPrice(6.0F);
      getEntityTransaction().commit();
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }

      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception in rollback:", re);
      }
    }

    if (!pass) {
      throw new Fault("lockTest1 failed");
    }
  }

  /*
   * @testName: lockTest2
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:332
   * 
   * @test_Strategy: public void lock(Object entity, LockModeType lockMode,
   * Map<String, Object> properties);
   * 
   * 1) Create One entity manager 2) Lock entity (coffee) in em1 using
   * PESSIMISTIC_WRITE lock 3) Try to obtain same Lock for cofee the same object
   * and modify its contents
   * 
   * Note: This test uses lock with property map (unlike lockTest1)
   */
  public void lockTest2() throws Fault {
    TestUtil.logTrace("Begin lockTest2");
    boolean pass = false;

    try {

      getEntityTransaction().begin();

      TestUtil.logTrace("locate Entity Coffee in EntityManager em1 and lock");
      Coffee coffeeFound = getEntityManager().find(Coffee.class, 1);
      Map<String, Object> myMap = new HashMap<String, Object>();
      myMap.put("some.cts.specific.property", "nothing.in.particular");
      getEntityManager().lock(coffeeFound, LockModeType.PESSIMISTIC_WRITE,
          myMap);

      TestUtil.logTrace("locate Entity Coffee in EntityManager em1 and update");
      Coffee coffeeFound2 = getEntityManager().find(Coffee.class, 1);
      getEntityManager().lock(coffeeFound2, LockModeType.PESSIMISTIC_WRITE,
          myMap);
      coffeeFound2.setPrice(6.0F);
      getEntityTransaction().commit();
      pass = true;

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception in rollback:", re);
      }
    }

    if (!pass) {
      throw new Fault("lockTest2 failed");
    }
  }

  /*
   * @testName: refreshTest1
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:334
   * 
   * @test_Strategy:
   * 
   * public void refresh(Object entity, LockModeType lockMode); 1) Create entity
   * manager 2) Find entity and refresh in em1 using PESSIMISTIC_READ lock 3)
   * Try to obtain refresh same entity using PESSIMISTIC_WRITE lock and modify
   * its contents
   *
   */
  public void refreshTest1() throws Fault {

    boolean pass = false;

    try {

      getEntityTransaction().begin();

      TestUtil.logTrace("locate Entity Coffee in EntityManager em1 and lock");
      Coffee coffeeFound = getEntityManager().find(Coffee.class, 1);
      getEntityManager().refresh(coffeeFound, LockModeType.PESSIMISTIC_READ);

      TestUtil.logTrace("locate Entity Coffee in EntityManager  and update");
      Coffee coffeeFound2 = getEntityManager().find(Coffee.class, 1);
      getEntityManager().refresh(coffeeFound2, LockModeType.PESSIMISTIC_WRITE);
      coffeeFound2.setPrice(6.0F);
      getEntityTransaction().commit();
      pass = true;

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }

      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception in rollback:", re);
      }
    }

    if (!pass) {
      throw new Fault("refreshTest1 failed");
    }
  }

  /*
   * @testName: refreshTest2
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:335
   * 
   * @test_Strategy:
   * 
   * public void refresh(Object entity, LockModeType lockMode, Map<String,
   * Object> properties); 1) Create entity manager 2) Find entity and refresh in
   * em1 using PESSIMISTIC_READ lock 3) Try to obtain refresh same entity using
   * PESSIMISTIC_WRITE lock and modify its contents Note: This test uses refresh
   * with property map (unlike refreshTest1)
   *
   */
  public void refreshTest2() throws Fault {

    TestUtil.logTrace("Begin refreshTest2");
    boolean pass = false;

    try {

      getEntityTransaction().begin();

      TestUtil.logTrace("locate Entity Coffee in EntityManager em1 and lock");
      Coffee coffeeFound = getEntityManager().find(Coffee.class, 2);
      Map<String, Object> myMap = new HashMap<String, Object>();
      myMap.put("some.cts.specific.property", "nothing.in.particular");
      getEntityManager().refresh(coffeeFound, LockModeType.PESSIMISTIC_READ,
          myMap);

      TestUtil.logTrace("locate Entity Coffee in EntityManager and update");
      Coffee coffeeFound2 = getEntityManager().find(Coffee.class, 2);
      getEntityManager().refresh(coffeeFound2, LockModeType.PESSIMISTIC_WRITE,
          myMap);
      coffeeFound2.setPrice(6.0F);
      getEntityTransaction().commit();
      pass = true;

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }

      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception in rollback:", re);
      }
    }

    if (!pass) {
      throw new Fault("refreshTest2 failed");
    }
  }

  /*
   * @testName: refreshTest3
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:333
   * 
   * @test_Strategy:
   *
   * public void refresh(Object entity, LockModeType lockMode, Map<String,
   * Object> properties); 1) Create entity manager 2) Find entity and refresh 3)
   * Try to obtain refresh same entity and modify its contents
   *
   */
  public void refreshTest3() throws Fault {

    TestUtil.logTrace("Begin refreshTest3");
    boolean pass = false;

    try {

      getEntityTransaction().begin();

      TestUtil.logTrace("locate Entity Coffee in EntityManager");
      Coffee coffeeFound = getEntityManager().find(Coffee.class, 2);
      Map<String, Object> myMap = new HashMap<String, Object>();
      myMap.put("some.cts.specific.property", "nothing.in.particular");
      getEntityManager().refresh(coffeeFound, myMap);

      TestUtil.logTrace("locate Entity Coffee in EntityManager and update");
      Coffee coffeeFound2 = getEntityManager().find(Coffee.class, 2);
      getEntityManager().refresh(coffeeFound2, myMap);
      coffeeFound2.setPrice(6.0F);
      getEntityTransaction().commit();
      pass = true;

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }

      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception in rollback:", re);
      }
    }

    if (!pass) {
      throw new Fault("refreshTest3 failed");
    }
  }

  /*
   * Business Methods to set up data for Test Cases
   */
  private void createTestData() throws Exception {
    try {

      TestUtil.logTrace("createTestData");

      getEntityTransaction().begin();
      TestUtil.logTrace("Create 5 Coffees");
      Coffee cRef[] = new Coffee[5];
      cRef[0] = new Coffee(1, "hazelnut", 1.0F);
      cRef[1] = new Coffee(2, "vanilla creme", 2.0F);
      cRef[2] = new Coffee(3, "decaf", 3.0F);
      cRef[3] = new Coffee(4, "breakfast blend", 4.0F);
      cRef[4] = new Coffee(5, "mocha", 5.0F);

      TestUtil.logTrace("Start to persist coffees ");
      for (Coffee c : cRef) {
        if (c != null) {
          getEntityManager().persist(c);
          TestUtil.logTrace("persisted coffee " + c);
        }
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception creating test data:", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception in rollback:", re);
      }
    }
  }

  public void cleanup() throws Fault {
    TestUtil.logTrace("cleanup");
    removeTestData();
    TestUtil.logTrace("cleanup complete, calling super.cleanup");
    super.cleanup();
  }

  private void removeTestData() {
    TestUtil.logTrace("removeTestData");
    if (getEntityTransaction().isActive()) {
      getEntityTransaction().rollback();
    }
    try {
      getEntityTransaction().begin();
      getEntityManager().createNativeQuery("DELETE FROM COFFEE")
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
  }
}
