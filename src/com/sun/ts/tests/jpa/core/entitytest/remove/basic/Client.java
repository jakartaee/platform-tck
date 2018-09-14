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

package com.sun.ts.tests.jpa.core.entitytest.remove.basic;

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
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);

    }
  }

  /*
   * BEGIN Test Cases
   */

  /*
   * @testName: removeBasicTest1
   * 
   * @assertion_ids: PERSISTENCE:SPEC:617; PERSISTENCE:SPEC:628;
   * PERSISTENCE:SPEC:629
   * 
   * @test_Strategy: A managed entity instance becomes removed by invoking the
   * remove method on it or by cascading the remove operation. The semantics of
   * the remove operation, applied to an entity X are as follows:
   *
   * If X is a new entity, it is ignored by the remove operation.
   *
   * Invoke remove on a new entity.
   *
   */

  public void removeBasicTest1() throws Fault {
    TestUtil.logTrace("Begin removeBasicTest1");
    boolean pass = false;
    final A a1 = new A("1", "a1", 1);

    try {
      getEntityTransaction().begin();
      getEntityManager().remove(a1);
      pass = true;
      getEntityTransaction().commit();
    } catch (Exception fe) {
      TestUtil.logErr(
          "Unexpected Exception during remove operation.  Should have been ignored.",
          fe);
      pass = false;
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
      throw new Fault("removeBasicTest1 failed");
  }

  /*
   * @testName: removeBasicTest2
   * 
   * @assertion_ids: PERSISTENCE:SPEC:617; PERSISTENCE:SPEC:632
   * 
   * @test_Strategy: If X is a managed entity, the remove operation causes it to
   * transition to the removed state. Invoke remove on a managed entity.
   *
   */

  public void removeBasicTest2() throws Fault {
    TestUtil.logTrace("Begin removeBasicTest2");
    boolean pass = false;
    final A a1 = new A("2", "a2", 2);
    createA(a1);

    getEntityTransaction().begin();
    try {
      final A newA = findA("2");
      getEntityManager().remove(newA);
      TestUtil.logTrace("Call contains after remove()");
      pass = (!getEntityManager().contains(newA));
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
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
      throw new Fault("removeBasicTest2 failed");
  }

  /*
   * @testName: removeBasicTest3
   * 
   * @assertion_ids: PERSISTENCE:SPEC:617; PERSISTENCE:SPEC:636
   * 
   * @test_Strategy: If X is a removed entity, invoking the remove method on it
   * will be ignored.
   *
   * Invoke remove on a removed entity.
   *
   */

  public void removeBasicTest3() throws Fault {
    final A a1 = new A("4", "a4", 4);
    boolean pass = false;

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("Persist Instance");
      getEntityManager().persist(a1);

      if (getEntityManager().contains(a1)) {
        try {
          getEntityManager().remove(a1);
          getEntityManager().flush();

          final A stillExists = findA("4");

          if (stillExists == null) {
            getEntityManager().remove(a1);
            pass = true;
          }
        } catch (Exception e) {
          TestUtil.logErr("Unexpected exception caught trying to remove"
              + " a removed entity, should have been ignored", e);
          pass = false;
        }
      } else {
        TestUtil.logTrace("entity not managed, unexpected, test fails.");
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
        TestUtil.logErr("Unexpected Exception in rollback:", re);
      }
    }

    if (!pass)
      throw new Fault("removeBasicTest3 failed");
  }

  /*
   * @testName: removeBasicTest4
   * 
   * @assertion_ids: PERSISTENCE:SPEC:617; PERSISTENCE:SPEC:637;
   * PERSISTENCE:SPEC:641; PERSISTENCE:SPEC:648
   * 
   * @test_Strategy: A removed entity will be removed from the database at or
   * before transaction commit or as a result of a flush operation.
   *
   * Remove an entity. Verify the entity is removed from the database at as a
   * result of the flush operation.
   *
   * The flush method can be used for force synchronization. The semantics of
   * the flush operation applied to an entity X is as follows:
   *
   * If X is a removed entity, it is removed from the database.
   *
   */

  public void removeBasicTest4() throws Fault {
    TestUtil.logTrace("Begin removeBasicTest4");
    boolean pass = false;
    final A a1 = new A("5", "a5", 5);
    getEntityTransaction().begin();
    getEntityManager().persist(a1);

    try {
      A newA = findA("5");
      if (null != newA) {
        TestUtil.logTrace("Found newA, try Remove");
        getEntityManager().remove(newA);
        getEntityManager().flush();

        TestUtil.logTrace(
            "Removed, try to find and verify the entity has been removed");
        newA = findA("5");
        if (null == newA) {
          TestUtil.logTrace("NewA is Null as expected");
          pass = true;
        }
      } else {
        TestUtil.logErr("Could not find persisted entity.");
        pass = false;
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr(
          "Unexpected Exception attempting to find removed entity:" + e);
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
      throw new Fault("removeBasicTest4 failed");
  }

  /*
   * @testName: removeBasicTest5
   * 
   * @assertion_ids: PERSISTENCE:SPEC:671; PERSISTENCE:SPEC:673
   * 
   * @test_Strategy: The contains method [used to determine whether an entity
   * instance is in the managed state] returns false:
   *
   * If the remove method has been called on the entity.
   *
   */

  public void removeBasicTest5() throws Fault {
    TestUtil.logTrace("Begin removeBasicTest5");
    boolean pass = false;
    final A a1 = new A("6", "a6", 6);
    createA(a1);

    try {
      getEntityTransaction().begin();

      final A a2 = findA("6");
      getEntityManager().remove(a2);

      if (!getEntityManager().contains(a2)) {
        pass = true;
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected Exception caught:" + e);
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
      throw new Fault("removeBasicTest5 failed");
  }

  /*
   * @testName: removeMergeBasicTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:661
   * 
   * @test_Strategy: To merge a removed entity will result in
   * IllegalArgumentException, or commit will fail.
   */

  public void removeMergeBasicTest() throws Fault {
    TestUtil.logTrace("Begin removeMergeBasicTest");
    boolean pass = false;
    String reason = null;
    final String aId = "7";
    final A a1 = new A(aId, "a7", 7);
    try {
      getEntityTransaction().begin();
      getEntityManager().persist(a1);
      getEntityManager().flush();
      getEntityManager().remove(a1);

      try {
        getEntityManager().merge(a1);
      } catch (IllegalArgumentException e) {
        TestUtil.logTrace(
            "Got expected exception when merging a removed entity:" + e);
        pass = true;
      }
      if (!pass) {
        getEntityTransaction().commit();
      }
    } catch (Exception e) {
      TestUtil.logTrace("This exception may be expected, but we need to check"
          + " if the commit really failed:" + e);
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
      A aFound = findA(aId);
      if (aFound == null) {
        pass = true;
        TestUtil.logTrace("No entity with id " + aId
            + " was found.  The commit " + "must have failed, as expected.");
      } else {
        reason = "Entity with id " + aId + " was found: " + aFound
            + ".  It means the previous commit unexpectedly succeeded.";
        TestUtil.logTrace(reason);
      }
    }
    if (!pass)
      throw new Fault("removeMergeBasicTest failed, reason: " + reason);
  }

  /*
   * Business Methods to set up data for Test Cases
   */

  private void createA(final A a) {
    TestUtil.logTrace("Entered createA method");
    getEntityTransaction().begin();
    getEntityManager().persist(a);
    getEntityTransaction().commit();

  }

  private A findA(final String id) {
    TestUtil.logTrace("Entered findA method");
    return getEntityManager().find(A.class, id);
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
      getEntityManager().createNativeQuery("DELETE FROM AEJB_1X1_BI_BTOB")
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
