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

package com.sun.ts.tests.jpa.core.cache.basicTests;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
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
   * @testName: getcacheTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:500; PERSISTENCE:SPEC:501;
   * PERSISTENCE:SPEC:503; PERSISTENCE:SPEC:504; PERSISTENCE:SPEC:505;
   * PERSISTENCE:SPEC:506; PERSISTENCE:SPEC:507; PERSISTENCE:SPEC:508;
   * PERSISTENCE:SPEC:932; PERSISTENCE:SPEC:936; PERSISTENCE:SPEC:939;
   * PERSISTENCE:SPEC:943; PERSISTENCE:SPEC:946; PERSISTENCE:SPEC:930;
   * PERSISTENCE:SPEC:1018; PERSISTENCE:SPEC:1019; PERSISTENCE:SPEC:1020;
   * PERSISTENCE:SPEC:1021; PERSISTENCE:SPEC:1023; PERSISTENCE:SPEC:1025;
   * PERSISTENCE:SPEC:848; PERSISTENCE:SPEC:856; PERSISTENCE:SPEC:908;
   * PERSISTENCE:SPEC:909; PERSISTENCE:SPEC:915; PERSISTENCE:SPEC:925;
   * PERSISTENCE:SPEC:918; PERSISTENCE:SPEC:928; PERSISTENCE:SPEC:929;
   * PERSISTENCE:JAVADOC:149; PERSISTENCE:JAVADOC:152; PERSISTENCE:JAVADOC:163;
   * PERSISTENCE:SPEC:846; PERSISTENCE:JAVADOC:338
   * 
   * @test_Strategy: With basic entity requirements, persist/remove an entity.
   */
  public void getcacheTest() throws Fault {
    Cache cache;
    boolean pass = false;
    final int count = 5;
    if (cachingSupported) {
      try {

        EntityManager em2 = getEntityManager();
        EntityTransaction et = getEntityTransaction();

        Order[] orders = new Order[count];
        et.begin();

        for (int i = 1; i < count; i++) {
          orders[i] = new Order(i, 100 * i);
          em2.persist(orders[i]);
          TestUtil.logTrace("persisted order " + orders[i]);
        }
        em2.flush();

        EntityManagerFactory emf = getEntityManagerFactory();
        cache = emf.getCache();

        if (cache != null) {
          pass = true;
        } else {
          TestUtil.logErr("Cache returned was null");
        }

        for (int i = 1; i < count; i++) {
          em2.remove(orders[i]);
          TestUtil.logTrace("Removed order " + orders[i]);
        }

        et.commit();

      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred", e);
      }
    } else {
      TestUtil.logMsg("Cache not supported, bypassing test");
      pass = true;
    }
    if (!pass) {
      throw new Fault("getcacheTest failed");
    }

  }

  /*
   * @testName: evictTest1
   * 
   * @assertion_ids: PERSISTENCE:SPEC:500; PERSISTENCE:SPEC:501;
   * PERSISTENCE:SPEC:503; PERSISTENCE:SPEC:504; PERSISTENCE:SPEC:505;
   * PERSISTENCE:SPEC:506; PERSISTENCE:SPEC:507; PERSISTENCE:SPEC:508;
   * PERSISTENCE:SPEC:932; PERSISTENCE:SPEC:936; PERSISTENCE:SPEC:939;
   * PERSISTENCE:SPEC:943; PERSISTENCE:SPEC:946; PERSISTENCE:SPEC:930;
   * PERSISTENCE:SPEC:1018; PERSISTENCE:SPEC:1019; PERSISTENCE:SPEC:1020;
   * PERSISTENCE:SPEC:1021; PERSISTENCE:SPEC:1023; PERSISTENCE:SPEC:1025;
   * PERSISTENCE:SPEC:848; PERSISTENCE:SPEC:856; PERSISTENCE:SPEC:908;
   * PERSISTENCE:SPEC:909; PERSISTENCE:SPEC:915; PERSISTENCE:SPEC:925;
   * PERSISTENCE:SPEC:918; PERSISTENCE:SPEC:928; PERSISTENCE:SPEC:929;
   * PERSISTENCE:JAVADOC:149; PERSISTENCE:JAVADOC:152; PERSISTENCE:JAVADOC:163;
   * PERSISTENCE:SPEC:846; PERSISTENCE:JAVADOC:304; PERSISTENCE:JAVADOC:305
   * 
   * @test_Strategy: Persist data, evict class and specific PK
   */
  public void evictTest1() throws Fault {
    Cache cache;
    final int count = 5;
    boolean pass1 = false;
    boolean pass2 = false;
    if (cachingSupported) {

      try {

        getEntityTransaction().begin();
        TestUtil.logTrace("Transaction status after begin:"
            + getEntityTransaction().isActive());
        Order[] orders = new Order[count];

        for (int i = 1; i < count; i++) {
          orders[i] = new Order(i, 100 * i);
          getEntityManager().persist(orders[i]);
          TestUtil.logTrace("persisted order " + orders[i]);
        }
        getEntityManager().flush();
        getEntityTransaction().commit();
        TestUtil.logTrace("Transaction status after commit:"
            + getEntityTransaction().isActive());

        cache = getEntityManagerFactory().getCache();

        if (cache != null) {
          pass1 = true;
          TestUtil.logTrace("cache was successfully obtained");

          boolean cacheContains = cache.contains(Order.class, 1);

          if (cacheContains) {
            TestUtil.logTrace("Order 1 found, evicting it from cache");
            cache.evict(Order.class, 1);

            // Recheck whether the removed entity is still in cache
            cacheContains = cache.contains(Order.class, 1);

            // if not found then evict was successful
            if (!cacheContains) {
              pass2 = true;
              TestUtil.logTrace("Order 1 was successfully evicted");
            }
          } else {
            TestUtil.logErr("cache did not contain Order 1");
          }
        } else {
          TestUtil.logErr(
              "Cache returned was null, eventhough Cache is supported.");
        }
      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred", e);
      }
    } else {
      TestUtil.logMsg("Cache not supported, bypassing test");
      pass1 = true;
      pass2 = true;
    }
    if (!pass1 || !pass2) {
      throw new Fault("evictTest1 failed");
    }

  }

  /*
   * @testName: evictTest2
   * 
   * @assertion_ids: PERSISTENCE:SPEC:500; PERSISTENCE:SPEC:501;
   * PERSISTENCE:SPEC:503; PERSISTENCE:SPEC:504; PERSISTENCE:SPEC:505;
   * PERSISTENCE:SPEC:506; PERSISTENCE:SPEC:507; PERSISTENCE:SPEC:508;
   * PERSISTENCE:SPEC:932; PERSISTENCE:SPEC:936; PERSISTENCE:SPEC:939;
   * PERSISTENCE:SPEC:943; PERSISTENCE:SPEC:946; PERSISTENCE:SPEC:930;
   * PERSISTENCE:SPEC:1018; PERSISTENCE:SPEC:1019; PERSISTENCE:SPEC:1020;
   * PERSISTENCE:SPEC:1021; PERSISTENCE:SPEC:1023; PERSISTENCE:SPEC:1025;
   * PERSISTENCE:SPEC:848; PERSISTENCE:SPEC:856; PERSISTENCE:SPEC:908;
   * PERSISTENCE:SPEC:909; PERSISTENCE:SPEC:915; PERSISTENCE:SPEC:925;
   * PERSISTENCE:SPEC:918; PERSISTENCE:SPEC:928; PERSISTENCE:SPEC:929;
   * PERSISTENCE:JAVADOC:149; PERSISTENCE:JAVADOC:152; PERSISTENCE:JAVADOC:163;
   * PERSISTENCE:SPEC:846; PERSISTENCE:JAVADOC:304; PERSISTENCE:JAVADOC:306
   *
   * @test_Strategy: Persist data, evict class
   */
  public void evictTest2() throws Fault {
    Cache cache;
    final int count = 5;
    boolean pass1, pass2 = false;
    pass1 = false;
    if (cachingSupported) {

      try {

        Order[] orders = new Order[count];
        getEntityTransaction().begin();

        for (int i = 1; i < count; i++) {
          orders[i] = new Order(i, 100 * i);
          getEntityManager().persist(orders[i]);
          TestUtil.logTrace("persisted order " + orders[i]);
        }
        getEntityManager().flush();
        getEntityTransaction().commit();

        cache = getEntityManagerFactory().getCache();

        if (cache != null) {
          pass1 = true;
          TestUtil.logTrace("cache was successfully obtained");

          boolean cacheContains = cache.contains(Order.class, 1);

          if (cacheContains) {
            TestUtil.logTrace("evicting Order 1 from cache");
            cache.evict(Order.class);

            // Recheck whether the removed entity is still in cache
            cacheContains = cache.contains(Order.class, 1);

            // if not found then evict was successful
            if (!cacheContains) {
              pass2 = true;
              TestUtil.logTrace("Order 1 was successfully evicted");
            }
          } else {
            TestUtil.logErr("Cache did not contain Order 1");
          }
        } else {
          TestUtil.logErr(
              "Cache returned was null, eventhough Cache is supported.");
        }
      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred", e);
      }
    } else {
      TestUtil.logMsg("Cache not supported, bypassing test");
      pass1 = true;
      pass2 = true;
    }
    if (!pass1 || !pass2) {
      throw new Fault("evictTest2 failed");
    }
  }

  /*
   * @testName: evictallTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:500; PERSISTENCE:SPEC:501;
   * PERSISTENCE:SPEC:503; PERSISTENCE:SPEC:504; PERSISTENCE:SPEC:505;
   * PERSISTENCE:SPEC:506; PERSISTENCE:SPEC:507; PERSISTENCE:SPEC:508;
   * PERSISTENCE:SPEC:932; PERSISTENCE:SPEC:936; PERSISTENCE:SPEC:939;
   * PERSISTENCE:SPEC:943; PERSISTENCE:SPEC:946; PERSISTENCE:SPEC:930;
   * PERSISTENCE:SPEC:1018; PERSISTENCE:SPEC:1019; PERSISTENCE:SPEC:1020;
   * PERSISTENCE:SPEC:1021; PERSISTENCE:SPEC:1023; PERSISTENCE:SPEC:1025;
   * PERSISTENCE:SPEC:848; PERSISTENCE:SPEC:856; PERSISTENCE:SPEC:908;
   * PERSISTENCE:SPEC:909; PERSISTENCE:SPEC:915; PERSISTENCE:SPEC:925;
   * PERSISTENCE:SPEC:918; PERSISTENCE:SPEC:928; PERSISTENCE:SPEC:929;
   * PERSISTENCE:JAVADOC:149; PERSISTENCE:JAVADOC:152; PERSISTENCE:JAVADOC:163;
   * PERSISTENCE:SPEC:846; PERSISTENCE:JAVADOC:304; PERSISTENCE:JAVADOC:307
   *
   * @test_Strategy: Persist data, evict all
   */
  public void evictallTest() throws Fault {
    Cache cache;
    final int count = 5;
    boolean pass1 = false;
    boolean pass2 = false;
    int pass2Count = 0;
    Order[] orders = new Order[count];
    int[] ids = new int[count];
    if (cachingSupported) {

      try {
        getEntityTransaction().begin();

        for (int i = 1; i < count; i++) {
          orders[i] = new Order(i, 100 * i);
          ids[i] = orders[i].getId();
          getEntityManager().persist(orders[i]);
          TestUtil.logTrace("persisted order " + orders[i]);
        }
        getEntityManager().flush();
        getEntityTransaction().commit();

        cache = getEntityManagerFactory().getCache();

        if (cache != null) {
          pass1 = true;
          TestUtil.logTrace(
              "cache was successfully obtained, evicting all Orders from cache");
          cache.evictAll();
          for (int i : ids) {
            // Recheck whether the evicted entities are still in cache
            TestUtil.logTrace("Testing order:" + i);
            boolean cacheContains = cache.contains(Order.class, i);

            if (!cacheContains) {
              pass2Count++;
              TestUtil.logTrace("Order:" + i + " was successfully evicted");
            }
          }
          if (pass2Count == orders.length) {
            pass2 = true;
          } else {
            TestUtil.logErr("Not all orders were evicted.");
          }
        } else {
          TestUtil.logErr(
              "Cache returned was null, eventhough Cache is supported.");
        }
      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred", e);
      }
    } else {
      TestUtil.logMsg("Cache not supported, bypassing test");
      pass1 = true;
      pass2 = true;
    }
    if (!pass1 || !pass2) {
      throw new Fault("evictallTest failed");
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
