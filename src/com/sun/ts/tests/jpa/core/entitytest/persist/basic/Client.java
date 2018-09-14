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

package com.sun.ts.tests.jpa.core.entitytest.persist.basic;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

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
      removeTestData();
    } catch (Exception e) {
      throw new Fault("Setup failed:", e);

    }
  }

  /*
   * BEGIN Test Cases
   */

  /*
   * @testName: persistBasicTest1
   * 
   * @assertion_ids: PERSISTENCE:SPEC:613; PERSISTENCE:SPEC:614;
   * PERSISTENCE:SPEC:671; PERSISTENCE:SPEC:675
   * 
   * @test_Strategy: A new entity bean instance has no persistent identity and
   * is not yet associated to a persistent context.
   *
   * The contains method [used to determine whether an entity instance is in the
   * managed state in the current persistence context ] returns false:
   *
   * If the instance is new and the persist method has not been on the entity.
   *
   * Instantiate an entity and verify the contains method returns false.
   */

  public void persistBasicTest1() throws Fault {

    TestUtil.logTrace("Begin persistBasicTest1");

    boolean pass = false;
    A aRef;
    try {

      aRef = new A("1", "A1", 1);

      getEntityTransaction().begin();
      if (!getInstanceStatus(aRef)) {
        pass = true;
      }
      getEntityTransaction().commit();

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

    if (!pass)
      throw new Fault("persistBasicTest1 failed");

  }

  /*
   * @testName: persistBasicTest2
   * 
   * @assertion_ids: PERSISTENCE:SPEC:613; PERSISTENCE:SPEC:615;
   * PERSISTENCE:SPEC:619; PERSISTENCE:SPEC:667; PERSISTENCE:SPEC:669
   * 
   * @test_Strategy: The new entity bean instance becomes both managed and
   * persistent by invoking the persist method on it. The semantics of the
   * persist operation as applied to entity X is as follows:
   *
   * If X is a new entity, it becomes managed.
   *
   * Invoke persist on the new entity. Find the entity instance and ensure it is
   * managed by calling contains() verifying it returns true.
   */

  public void persistBasicTest2() throws Fault {

    TestUtil.logTrace("Begin persistBasicTest2");

    boolean pass = false;
    A aRef;

    try {
      TestUtil.logTrace("new A");
      aRef = new A("2", "a2", 2);
      createA(aRef);

      A newA = findA("2");

      if (newA != null) {
        TestUtil.logTrace("A IS NOT NULL");
      }

      getEntityTransaction().begin();
      pass = getInstanceStatus(findA("2"));
      getEntityTransaction().commit();

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

    if (!pass)
      throw new Fault("persistBasicTest2 failed");

  }

  /*
   * @testName: persistBasicTest3
   * 
   * @assertion_ids: PERSISTENCE:SPEC:613; PERSISTENCE:SPEC:618;
   * PERSISTENCE:SPEC:620
   * 
   * @test_Strategy: The new entity bean instance becomes both managed and
   * persistent by invoking the persist method on it. The semantics of the
   * persist operation as applied to entity X is as follows:
   *
   * The entity X will be entered into the database at or before transaction
   * commit or as a result of the flush operation.
   *
   * Create a new entity instance, invoke flush(), then attempt to access the
   * entity by find and invoking a query on it.
   *
   */

  public void persistBasicTest3() throws Fault {

    TestUtil.logTrace("Begin persistBasicTest3");

    boolean pass = false;
    Object result;
    A a1;

    try {
      getEntityTransaction().begin();
      a1 = new A("3", "a3", 3);
      TestUtil.logTrace("Persist Instance");
      getEntityManager().persist(a1);
      getEntityManager().flush();

      TestUtil.logTrace("find By Name");
      result = (A) findByName("a3");

      TestUtil.logTrace("Check to see that the entities are identical");
      if (result == a1) {
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back transaction", fe);
      }
    }

    if (!pass)
      throw new Fault("persistBasicTest3 failed");

  }

  /*
   * @testName: persistBasicTest4
   * 
   * @assertion_ids: PERSISTENCE:SPEC:613; PERSISTENCE:SPEC:618;
   * PERSISTENCE:SPEC:621
   * 
   * @test_Strategy: The new entity bean instance becomes both managed and
   * persistent by invoking the persist method on it. The semantics of the
   * persist operation as applied to entity X is as follows:
   *
   * If X is preexisting managed entity, it is ignored by the persist operation.
   *
   * Invoke persist on an already managed instance. Ensure no exception is
   * thrown and that the entity is still persisted and managed.
   *
   */

  public void persistBasicTest4() throws Fault {

    TestUtil.logTrace("Begin persistBasicTest4");
    final A aRef = new A("4", "a4", 4);

    boolean pass = false;
    try {

      TestUtil.logTrace("Persist Instance");
      createA(aRef);

      getEntityTransaction().begin();
      TestUtil.logTrace("get Instance Status ");
      if (getInstanceStatus(findA("4"))) {
        try {
          TestUtil.logTrace("entity is managed, try to persist again ");
          A newA = findA("4");
          getEntityManager().persist(newA);
          TestUtil.logTrace(
              "Persist ignored on an already persisted entity as expected");
          pass = true;
        } catch (Exception ee) {
          TestUtil.logErr("Unexpected exception trying to persist an"
              + " already persisted entity", ee);
          pass = false;
        }

      } else {
        TestUtil.logTrace("Instance is not managed. Test Fails.");
        pass = false;
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected exception rolling back transaction", re);
      }
    }

    if (!pass)
      throw new Fault("persistBasicTest4 failed");
  }

  /*
   * @testName: persistBasicTest5
   * 
   * @assertion_ids: PERSISTENCE:SPEC:613; PERSISTENCE:SPEC:618;
   * PERSISTENCE:SPEC:641; PERSISTENCE:SPEC:642
   * 
   * @test_Strategy: The flush method can be used for force synchronization. The
   * semantics of the flush operation applied to an entity X is as follows:
   *
   * If X is a managed entity, it is synchronized to the database.
   *
   * Execute flush on a managed entity and ensure the database reflects the
   * change.
   *
   */

  public void persistBasicTest5() throws Fault {

    TestUtil.logTrace("Begin persistBasicTest5");
    final A aRef = new A("5", "a5", 5);
    A a2;

    boolean pass = false;
    try {

      TestUtil.logTrace("Persist Instance");
      createA(aRef);

      getEntityTransaction().begin();
      TestUtil.logTrace("get Instance Status ");
      if (getInstanceStatus(findA("5"))) {
        try {
          TestUtil.logTrace("entity is managed, try to change name and flush ");
          a2 = findA("5");
          a2.setAName("a2");
          getEntityManager().flush();
          if (a2.getAName().equals("a2")) {
            TestUtil.logTrace("sync to database successful");
            pass = true;
          }
        } catch (Exception ee) {
          TestUtil.logErr(
              "Unexpected exception trying to flush a" + "persisted entity",
              ee);
          pass = false;
        }

      } else {
        TestUtil.logTrace("Instance is not already persisted. Test Fails.");
        pass = false;
      }
      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected exception rolling back transaction", re);
      }

    }

    if (!pass)
      throw new Fault("persistBasicTest5 failed");
  }

  /*
   * Business Methods for Test Cases
   */

  private void createA(final A a) {
    TestUtil.logTrace("Entered createA method");
    getEntityTransaction().begin();
    getEntityManager().persist(a);
    // WORKAROUND
    getEntityManager().flush();
    getEntityTransaction().commit();
  }

  private A findA(final String id) {
    TestUtil.logTrace("Entered findA method");
    return getEntityManager().find(A.class, id);
  }

  private Object findByName(final String name) {
    TestUtil.logTrace("Entered findByName method");
    return getEntityManager()
        .createQuery("select a from A a where a.name = :name")
        .setParameter("name", name).getSingleResult();
  }

  private boolean getInstanceStatus(final Object o) {
    TestUtil.logTrace("Entered getInstanceStatus method");
    return getEntityManager().contains(o);
  }

  public void cleanup() throws Fault {
    TestUtil.logTrace("Cleanup data");
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
      getEntityManager().createNativeQuery("DELETE FROM AEJB_1XM_BI_BTOB")
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
