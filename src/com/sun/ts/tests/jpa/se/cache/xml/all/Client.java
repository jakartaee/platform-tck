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

package com.sun.ts.tests.jpa.se.cache.xml.all;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.DriverManagerConnection;
import com.sun.ts.tests.jpa.common.PMClientBase;

import jakarta.persistence.Cache;
import jakarta.persistence.CacheRetrieveMode;
import jakarta.persistence.CacheStoreMode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

public class Client extends PMClientBase {

  Properties jpaprops = null;

  public Client() {
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.testArgs: -ap tssql.stmt
   */
  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup");
    try {
      // displayMap(p);
      super.setup(args, p);
      removeTestData();
      jpaprops = p;
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  /*
   * @testName: containsTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1496;
   * 
   * @test_Strategy: Using the xml shared-cache-mode element with a value of ALL
   * persist an entity and verify it is in the cache
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
          if (b) {
            TestUtil.logTrace("Cache returned: " + b
                + ", therefore cache contains order " + order);
            pass = true;
          } else {
            TestUtil.logErr("Cache returned: " + b
                + ", therefore cache does not contain order " + order);
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

  /*
   * @testName: cacheStoreModeBYPASSTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1501;
   * 
   * @test_Strategy: Using the xml shared-cache-mode element with a value of ALL
   * and a Persistence Context property of CacheStoreMode.BYPASS, persist an
   * entity and verify it is not in the cache
   */
  public void cacheStoreModeBYPASSTest() throws Fault {
    Cache cache;
    boolean pass1, pass2, pass3;
    pass1 = pass2 = pass3 = false;
    if (cachingSupported) {
      try {
        TestUtil.logTrace("Persist an order");
        getEntityTransaction().begin();
        getEntityManager().setProperty("jakarta.persistence.cache.storeMode",
            CacheStoreMode.BYPASS);
        Order order = new Order(1, 101);
        getEntityManager().persist(order);
        TestUtil.logTrace("persisted order " + order);

        getEntityManager().flush();
        getEntityTransaction().commit();
        TestUtil.logTrace(
            "Verify the order persisted successfully, but it is not in the cache");
        cache = getEntityManagerFactory().getCache();

        if (cache != null) {
          boolean b = cache.contains(Order.class, 1);
          if (!b) {
            TestUtil.logTrace("Cache returned: " + b
                + ", therefore cache does not contain order " + order);
            pass1 = true;
          } else {
            TestUtil.logErr("Cache returned: " + b
                + ", therefore cache does contain order " + order);
          }
        } else {
          TestUtil.logErr("Cache returned was null");
        }

        int[] result = selectDataVIAJDBC(this.jpaprops, 1);
        Order order2 = new Order(result[0], result[1]);
        if (order.equals(order2)) {
          TestUtil.logTrace("Entity was persisted correctly");
          pass2 = true;
        } else {
          TestUtil.logErr("Entity was not persisted correctly - expected:"
              + order + ", actual:" + order2);
        }

        TestUtil.logTrace(
            "Find the order and verify it is not loaded into the cache");

        getEntityManager().find(Order.class, 1);
        cache = getEntityManagerFactory().getCache();
        if (cache != null) {
          boolean b = cache.contains(Order.class, 1);
          if (!b) {
            TestUtil.logTrace("Cache returned: " + b
                + ", therefore cache does not contain order " + order);
            pass3 = true;
          } else {
            TestUtil.logErr("Cache returned: " + b
                + ", therefore cache does contain order " + order);
          }
        } else {
          TestUtil.logErr("Cache returned was null");
        }
      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred", e);
      }
    } else {
      TestUtil.logMsg("Cache not supported, bypassing test");
      pass1 = pass2 = pass3 = true;
    }
    if (!pass1 || !pass2 || !pass3) {
      throw new Fault("cacheStoreModeBYPASSTest failed");
    }

  }

  /*
   * @testName: cacheStoreModeUSETest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1501; PERSISTENCE:SPEC:1866;
   * 
   * @test_Strategy: Using the xml shared-cache-mode element with a value of ALL
   * and a Persistence Context property of CacheStoreMode.USE, persist an entity
   * and verify it is in the cache
   */
  public void cacheStoreModeUSETest() throws Fault {
    Cache cache;
    boolean pass1, pass2, pass3;
    pass1 = pass2 = pass3 = false;
    if (cachingSupported) {
      try {

        TestUtil.logTrace("Persist an order");
        getEntityTransaction().begin();
        getEntityManager().setProperty("jakarta.persistence.cache.storeMode",
            CacheStoreMode.USE);
        Order order = new Order(1, 101);
        getEntityManager().persist(order);
        TestUtil.logTrace("persisted order " + order);

        getEntityManager().flush();
        getEntityTransaction().commit();
        TestUtil.logTrace(
            "Verify the order persisted successfully, and it is in the cache");
        cache = getEntityManagerFactory().getCache();

        if (cache != null) {
          boolean b = cache.contains(Order.class, 1);
          if (b) {
            TestUtil.logTrace("Cache returned: " + b
                + ", therefore cache does contain order " + order);
            pass1 = true;
          } else {
            TestUtil.logErr("Cache returned: " + b
                + ", therefore cache does not contain order " + order);
          }
        } else {
          TestUtil.logErr("Cache returned was null");
        }

        int[] result = selectDataVIAJDBC(this.jpaprops, 1);
        Order order2 = new Order(result[0], result[1]);
        if (order.equals(order2)) {
          TestUtil.logTrace("Entity was persisted correctly");
          pass2 = true;
        } else {
          TestUtil.logErr("Entity was not persisted correctly - expected:"
              + order + ", actual:" + order2);
        }

        TestUtil
            .logTrace("Find the order and verify it is loaded into the cache");

        getEntityManager().find(Order.class, 1);
        cache = getEntityManagerFactory().getCache();
        if (cache != null) {
          boolean b = cache.contains(Order.class, 1);
          if (b) {
            TestUtil.logTrace("Cache returned: " + b
                + ", therefore cache does contain order " + order);
            pass3 = true;
          } else {
            TestUtil.logErr("Cache returned: " + b
                + ", therefore cache does not contain order " + order);
          }
        } else {
          TestUtil.logErr("Cache returned was null");
        }
      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred", e);
      }
    } else {
      TestUtil.logMsg("Cache not supported, bypassing test");
      pass1 = pass2 = pass3 = true;
    }
    if (!pass1 || !pass2 || !pass3) {
      throw new Fault("cacheStoreModeUSETest failed");
    }

  }

  /*
   * @testName: cacheStoreModeREFRESHTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1501;
   * 
   * @test_Strategy: Using the xml shared-cache-mode element with a value of ALL
   * and a Persistence Context property of CacheStoreMode.REFRESH, persist an
   * entity and verify it is in the cache
   */
  public void cacheStoreModeREFRESHTest() throws Fault {
    Cache cache;
    boolean pass1, pass2, pass3;
    pass1 = pass2 = pass3 = false;
    if (cachingSupported) {
      try {

        TestUtil.logTrace("Persist an order");
        getEntityTransaction().begin();
        getEntityManager().setProperty("jakarta.persistence.cache.storeMode",
            CacheStoreMode.REFRESH);
        Order order = new Order(1, 101);
        getEntityManager().persist(order);
        TestUtil.logTrace("persisted order " + order);

        getEntityManager().flush();
        getEntityTransaction().commit();
        TestUtil.logTrace(
            "Verify the order persisted successfully, and it is in the cache");
        cache = getEntityManagerFactory().getCache();

        if (cache != null) {
          boolean b = cache.contains(Order.class, 1);
          if (b) {
            TestUtil.logTrace("Cache returned: " + b
                + ", therefore cache does contain order " + order);
            pass1 = true;
          } else {
            TestUtil.logErr("Cache returned: " + b
                + ", therefore cache does not contain order " + order);
          }
        } else {
          TestUtil.logErr("Cache returned was null");
        }

        int[] result = selectDataVIAJDBC(this.jpaprops, 1);
        Order order2 = new Order(result[0], result[1]);
        if (order.equals(order2)) {
          TestUtil.logTrace("Entity was persisted correctly");
          pass2 = true;
        } else {
          TestUtil.logErr("Entity was not persisted correctly - expected:"
              + order + ", actual:" + order2);
        }

        TestUtil
            .logTrace("Find the order and verify it is loaded into the cache");

        getEntityManager().find(Order.class, 1);
        cache = getEntityManagerFactory().getCache();
        if (cache != null) {
          boolean b = cache.contains(Order.class, 1);
          if (b) {
            TestUtil.logTrace("Cache returned: " + b
                + ", therefore cache does contain order " + order);
            pass3 = true;
          } else {
            TestUtil.logErr("Cache returned: " + b
                + ", therefore cache does not contain order " + order);
          }
        } else {
          TestUtil.logErr("Cache returned was null");
        }
      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred", e);
      }
    } else {
      TestUtil.logMsg("Cache not supported, bypassing test");
      pass1 = pass2 = pass3 = true;
    }
    if (!pass1 || !pass2 || !pass3) {
      throw new Fault("cacheStoreModeREFRESHTest failed");
    }

  }

  /*
   * @testName: cacheRetrieveModeBYPASSTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1501;
   * 
   * @test_Strategy: Using the xml shared-cache-mode element with a value of ALL
   * and a Persistence Context property of CacheRetrieveMode.BYPASS and
   * CacheStoreMode.BYPASS, persist an entity and verify it is not in the cache
   */
  public void cacheRetrieveModeBYPASSTest() throws Fault {
    Cache cache;
    boolean pass1, pass2;
    pass1 = pass2 = false;
    if (cachingSupported) {
      try {

        TestUtil.logTrace("Persist an order");
        createDataVIAJDBC(this.jpaprops);

        TestUtil.logTrace("Verify order is not in Cache before executing find");
        cache = getEntityManagerFactory().getCache();
        if (cache != null) {
          boolean b = cache.contains(Order.class, 1);
          if (!b) {
            TestUtil.logTrace("Cache returned: " + b
                + ", therefore cache does not contain order ");
            pass1 = true;
          } else {
            TestUtil.logErr("Cache returned: " + b
                + ", therefore cache does contain order ");
          }
        } else {
          TestUtil.logErr("Cache returned was null");
        }

        TestUtil.logTrace(
            "Find the order and verify it is not loaded into the cache");
        getEntityManager().setProperty("jakarta.persistence.cache.retrieveMode",
            CacheRetrieveMode.BYPASS);
        getEntityManager().setProperty("jakarta.persistence.cache.storeMode",
            CacheStoreMode.BYPASS);

        Order order = getEntityManager().find(Order.class, 1);

        cache = getEntityManagerFactory().getCache();
        if (cache != null) {
          boolean b = cache.contains(Order.class, 1);
          if (!b) {
            TestUtil.logTrace("Cache returned: " + b
                + ", therefore cache does not contain order " + order);
            pass2 = true;
          } else {
            TestUtil.logErr("Cache returned: " + b
                + ", therefore cache does contain order " + order);
          }
        } else {
          TestUtil.logErr("Cache returned was null");
        }
      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred", e);
      }
    } else {
      TestUtil.logMsg("Cache not supported, bypassing test");
      pass1 = pass2 = true;
    }
    if (!pass1 || !pass2) {
      throw new Fault("cacheRetrieveModeBYPASSTest failed");
    }

  }

  /*
   * @testName: cacheRetrieveModeUSETest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1501;
   * 
   * @test_Strategy: Using the xml shared-cache-mode element with a value of ALL
   * and a Persistence Context property of CacheRetrieveMode.USE and
   * CacheStoreMode.BYPASS, persist an entity and verify it is in the cache
   */
  public void cacheRetrieveModeUSETest() throws Fault {
    Cache cache;
    boolean pass1, pass2;
    pass1 = pass2 = false;
    if (cachingSupported) {
      try {

        TestUtil.logTrace("Persist an order");
        createDataVIAJDBC(this.jpaprops);

        TestUtil.logTrace("Verify order is not in Cache before executing find");
        cache = getEntityManagerFactory().getCache();
        if (cache != null) {
          boolean b = cache.contains(Order.class, 1);
          if (!b) {
            TestUtil.logTrace("Cache returned: " + b
                + ", therefore cache does not contain order ");
            pass1 = true;
          } else {
            TestUtil.logErr("Cache returned: " + b
                + ", therefore cache does contain order ");
          }
        } else {
          TestUtil.logErr("Cache returned was null");
        }

        TestUtil.logTrace(
            "Find the order and verify it is not loaded into the cache");
        getEntityManager().setProperty("jakarta.persistence.cache.retrieveMode",
            CacheRetrieveMode.USE);
        getEntityManager().setProperty("jakarta.persistence.cache.storeMode",
            CacheStoreMode.BYPASS);
        Order order = getEntityManager().find(Order.class, 1);

        cache = getEntityManagerFactory().getCache();
        if (cache != null) {
          boolean b = cache.contains(Order.class, 1);
          if (!b) {
            TestUtil.logTrace("Cache returned: " + b
                + ", therefore cache does not contain order " + order);
            pass2 = true;
          } else {
            TestUtil.logErr("Cache returned: " + b
                + ", therefore cache does contain order " + order);
          }
        } else {
          TestUtil.logErr("Cache returned was null");
        }
      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred", e);
      }
    } else {
      TestUtil.logMsg("Cache not supported, bypassing test");
      pass1 = pass2 = true;
    }
    if (!pass1 || !pass2) {
      throw new Fault("cacheRetrieveModeUSETest failed");
    }

  }

  /*
   * testName: findOverridesWithBYPASSTest assertion_ids: test_Strategy: Using
   * the xml shared-cache-mode element with a value of ALL and a Persistence
   * Context property of CacheRetrieveMode.USE but find overrides it with
   * BYPASS, verify order is not in the cache
   *
   * public void findOverridesWithBYPASSTest() throws Fault { Cache cache;
   * boolean pass1, pass2; pass1 = pass2 = false; if (cachingSupported) { try {
   * 
   * TestUtil.logTrace("Persist an order"); createDataVIAJDBC(this.props);
   * 
   * TestUtil.logTrace("Verify order is not in Cache before executing find");
   * cache = getEntityManagerFactory().getCache(); if (cache != null) { boolean
   * b = cache.contains(Order.class, 1); if (!b) {
   * TestUtil.logTrace("Cache returned: " + b +
   * ", therefore cache does not contain order "); pass1 = true; } else {
   * TestUtil.logErr("Cache returned: " + b +
   * ", therefore cache does contain order "); } } else {
   * TestUtil.logErr("Cache returned was null"); }
   * getEntityManager().setProperty("jakarta.persistence.cache.retrieveMode",
   * CacheRetrieveMode.USE);
   * getEntityManager().setProperty("jakarta.persistence.cache.storeMode",
   * CacheStoreMode.BYPASS); Map map = new Properties();
   * map.put("jakarta.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
   * Order order = getEntityManager().find(Order.class, 1, map); cache =
   * getEntityManagerFactory().getCache();
   * 
   * if (cache != null) { boolean b = cache.contains(Order.class, 1); if (!b) {
   * TestUtil.logTrace("Cache returned: " + b +
   * ", therefore cache does not contain order " + order); pass2 = true; } else
   * { TestUtil.logErr("Cache returned: " + b +
   * ", therefore cache does contain order " + order); } } else {
   * TestUtil.logErr("Cache returned was null"); } } catch (Exception e) {
   * TestUtil.logErr("Unexpected exception occurred", e); } } else {
   * TestUtil.logMsg("Cache not supported, bypassing test"); pass1 = pass2 =
   * true; } if (!pass1 || !pass2) { throw new
   * Fault("findOverridesWithBYPASSTest failed"); }
   * 
   * }
   */

  public int[] selectDataVIAJDBC(Properties p, int id) {
    int[] params = new int[2];
    displayMap(p);
    Connection conn = null;
    try {

      DriverManagerConnection dmCon = new DriverManagerConnection();
      conn = dmCon.getConnection(p);
      String selectString = p.getProperty("Select_Jpa_Purchase_Order", "");
      TestUtil.logTrace("ASDF:" + selectString);
      PreparedStatement pStmt = conn.prepareStatement(selectString);
      pStmt.setInt(1, id);
      TestUtil.logTrace("SQL to be executed:" + pStmt.toString());
      ResultSet rs = pStmt.executeQuery();
      if (rs.next()) {
        params[0] = rs.getInt(1);
        params[1] = rs.getInt(2);
      } else {
        throw new SQLException("Data not found");
      }
    } catch (SQLException sqlex) {
      TestUtil.logErr("Received SQLException", sqlex);
    } catch (ClassNotFoundException cnfe) {
      TestUtil.logErr("Received ClassNotFoundException", cnfe);
    } catch (Exception ex) {
      TestUtil.logErr("Received Exception", ex);
    } finally {
      try {
        conn.close();
      } catch (Exception ex) {
      }
    }
    return params;
  }

  public void createDataVIAJDBC(Properties p) {
    Connection conn = null;

    try {
      // displayMap(p);
      DriverManagerConnection dmCon = new DriverManagerConnection();
      conn = dmCon.getConnection(p);

      String insertString = p.getProperty("Insert_Jpa_Purchase_Order", "");
      PreparedStatement pStmt = conn.prepareStatement(insertString);
      pStmt.setInt(1, 1);
      pStmt.setInt(2, 101);
      TestUtil.logTrace("SQL to be executed:" + pStmt.toString());
      int rows = pStmt.executeUpdate();
      TestUtil.logTrace("Row inserted:" + rows);
    } catch (SQLException sqlex) {
      TestUtil.logErr("Received SQLException", sqlex);
    } catch (ClassNotFoundException cnfe) {
      TestUtil.logErr("Received ClassNotFoundException", cnfe);
    } catch (Exception ex) {
      TestUtil.logErr("Received Exception", ex);
    } finally {
      try {
        conn.close();
      } catch (Exception ex) {
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
