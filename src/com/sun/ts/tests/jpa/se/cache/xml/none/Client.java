/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.se.cache.xml.none;

import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import jakarta.persistence.Cache;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

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
   * @testName: containsTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1497; PERSISTENCE:SPEC:1866;
   * 
   * @test_Strategy: Using the xml shared-cache-mode element with a value of
   * NONE persist an entity and verify it is not the cache
   */
  public void containsTest() throws Fault {
    Cache cache;
    boolean pass = false;
    if (cachingSupported) {
      try {

        EntityManager em2 = getEntityManager();
        EntityTransaction et = getEntityTransaction();

        et.begin();

        Order order = new Order(1, 101);
        em2.persist(order);
        TestUtil.logTrace("persisted order " + order);

        em2.flush();
        et.commit();

        EntityManagerFactory emf = getEntityManagerFactory();
        cache = emf.getCache();

        if (cache != null) {
          boolean b = cache.contains(Order.class, 1);
          if (!b) {
            TestUtil.logTrace("Cache returned: " + b
                + ", therefore cache does not contain order " + order);
            pass = true;
          } else {
            TestUtil.logErr("Cache returned: " + b
                + ", therefore cache does incorrectly contain order " + order);
          }
        } else {
          TestUtil.logErr("Cache returned was null");
        }
      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred", e);
      }
    } else {
      TestUtil.logMsg("Cache not supported, bypassing test");
      pass = true;
    }
    if (!pass) {
      throw new Fault("containsTest failed");
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
      getEntityManager().createNativeQuery("DELETE FROM PURCHASE_ORDER")
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
