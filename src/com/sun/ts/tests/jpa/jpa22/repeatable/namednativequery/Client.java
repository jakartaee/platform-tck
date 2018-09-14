/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.jpa22.repeatable.namednativequery;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import java.util.Properties;

public class Client extends PMClientBase {
  private static final long serialVersionUID = 22L;

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
   * @testName: findTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:117; PERSISTENCE:JAVADOC:118;
   * PERSISTENCE:JAVADOC:119; PERSISTENCE:JAVADOC:199; PERSISTENCE:JAVADOC:200;
   * 
   * @test_Strategy:
   * 
   * find(Class entityClass, Object PK, LockModeType lck)
   * 
   */
  public void findTest() throws Fault {

    TestUtil.logTrace("Begin findTest1");
    boolean pass = false;

    getEntityTransaction().begin();

    try {
      for (int i = 1; i != 5; i++) {
        Coffee coffeeFound = getEntityManager().find(Coffee.class, i);
        assertTrue(coffeeFound != null, "cofee id = " + i + " not found");
        assertTrue(coffeeFound.getId() == i,
            "Unexpected id found:" + coffeeFound.getId() + " expected " + i);
      }
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
      throw new Fault("findTest1 failed");
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

  private void assertTrue(boolean b, String message) throws Fault {
    if (!b)
      throw new Fault(message);
  }
}
