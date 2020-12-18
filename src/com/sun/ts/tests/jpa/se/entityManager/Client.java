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

package com.sun.ts.tests.jpa.se.entityManager;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import jakarta.persistence.FlushModeType;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Parameter;
import jakarta.persistence.Query;
import jakarta.persistence.TemporalType;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Root;

public class Client extends PMClientBase {

  Properties props = null;

  Map<String, Object> map = new HashMap<String, Object>();

  Order[] orders = new Order[5];

  public Client() {
  }

  public static void main(String[] args) {
    Client theTests = new Client();

    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup");
    this.props = p;
    try {
      super.setup(args, p);
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  /*
   * setupOrderData() is called before each test
   *
   * @class.setup_props: jdbc.db;
   */
  public void setupOrderData(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setupOrderData");
    this.props = p;
    try {
      super.setup(args, p);
      removeTestData();
      createOrderData();
      map.putAll(getEntityManager().getProperties());
      map.put("foo", "bar");
      displayMap(map);
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  public void cleanup() throws Fault {
    TestUtil.logTrace("cleanup");
    super.cleanup();
  }

  public void cleanupData() throws Fault {
    TestUtil.logTrace("cleanupData");
    removeTestData();
    cleanup();
  }

  /*
   * @testName: persistAfterClose
   * 
   * @assertion_ids: PERSISTENCE:SPEC:881; PERSISTENCE:SPEC:882
   * 
   * @test_Strategy: Call EntityManager.persist() method after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void persistAfterClose() throws Fault {
    boolean pass = false;

    getEntityManager().close();
    try {
      Order order = new Order(6, 666, "desc6");
      getEntityManager().persist(order);

    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("persistAfterClose failed");
    }
  }

  /*
   * @testName: mergeAfterClose
   * 
   * @assertion_ids: PERSISTENCE:SPEC:881; PERSISTENCE:SPEC:882
   * 
   * @test_Strategy: Call EntityManager.merge() method after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void mergeAfterClose() throws Fault {
    boolean pass = false;

    getEntityManager().close();
    try {
      Order order = new Order(6, 666, "desc6");
      getEntityManager().merge(order);
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("mergeAfterClose failed");
    }
  }

  /*
   * @testName: removeAfterClose
   * 
   * @assertion_ids: PERSISTENCE:SPEC:881; PERSISTENCE:SPEC:882
   * 
   * @test_Strategy: Call EntityManager.remove() method after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void removeAfterClose() throws Fault {
    boolean pass = false;

    getEntityManager().close();
    try {
      Order order = new Order(6, 666, "desc6");
      getEntityManager().remove(order);

    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("removeAfterClose failed");
    }
  }

  /*
   * @testName: findAfterClose
   * 
   * @assertion_ids: PERSISTENCE:SPEC:881; PERSISTENCE:SPEC:882
   * 
   * @test_Strategy: Call EntityManager.find() method after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void findAfterClose() throws Fault {
    boolean pass = false;

    getEntityManager().close();
    try {
      getEntityManager().find(Order.class, 0);

    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("findAfterClose failed");
    }
  }

  /*
   * @testName: getReferenceAfterClose
   * 
   * @assertion_ids: PERSISTENCE:SPEC:881; PERSISTENCE:SPEC:882
   * 
   * @test_Strategy: Call EntityManager.getReference() method after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void getReferenceAfterClose() throws Fault {
    boolean pass = false;

    getEntityManager().close();
    try {
      getEntityManager().getReference(Order.class, 0);

    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("getReferenceAfterClose failed");
    }
  }

  /*
   * @testName: flushAfterClose
   * 
   * @assertion_ids: PERSISTENCE:SPEC:881; PERSISTENCE:SPEC:882
   * 
   * @test_Strategy: Call EntityManager.flush() method after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void flushAfterClose() throws Fault {
    boolean pass = false;

    getEntityManager().close();
    try {
      getEntityManager().flush();

    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("flushAfterClose failed");
    }
  }

  /*
   * @testName: setFlushModeAfterClose
   * 
   * @assertion_ids: PERSISTENCE:SPEC:881; PERSISTENCE:SPEC:882
   * 
   * @test_Strategy: Call EntityManager.setFlushMode() method after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void setFlushModeAfterClose() throws Fault {
    boolean pass = false;

    getEntityManager().close();
    try {
      getEntityManager().setFlushMode(FlushModeType.AUTO);

    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("setFlushModeAfterClose failed");
    }
  }

  /*
   * @testName: getFlushModeAfterClose
   * 
   * @assertion_ids: PERSISTENCE:SPEC:881; PERSISTENCE:SPEC:882
   * 
   * @test_Strategy: Call EntityManager.getFlushMode() method after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void getFlushModeAfterClose() throws Fault {
    boolean pass = false;

    getEntityManager().close();
    try {
      getEntityManager().getFlushMode();

    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("getFlushModeAfterClose failed");
    }
  }

  /*
   * @testName: lockAfterClose
   * 
   * @assertion_ids: PERSISTENCE:SPEC:881; PERSISTENCE:SPEC:882
   * 
   * @test_Strategy: Call EntityManager.lock() method after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void lockAfterClose() throws Fault {
    boolean pass = false;

    getEntityManager().close();
    try {
      Order order = new Order(6, 666, "desc6");
      getEntityManager().lock(order, LockModeType.WRITE);

    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("lockAfterClose failed");
    }
  }

  /*
   * @testName: refreshAfterClose
   * 
   * @assertion_ids: PERSISTENCE:SPEC:881; PERSISTENCE:SPEC:882
   * 
   * @test_Strategy: Call EntityManager.refresh() method after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void refreshAfterClose() throws Fault {
    boolean pass = false;

    getEntityManager().close();
    try {
      Order order = new Order(6, 666, "desc6");
      getEntityManager().refresh(order);

    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("refreshAfterClose failed");
    }
  }

  /*
   * @testName: clearAfterClose
   * 
   * @assertion_ids: PERSISTENCE:SPEC:881; PERSISTENCE:SPEC:882
   * 
   * @test_Strategy: Call EntityManager.clear() method after calling
   * EntityManager.close()and expect IllegalStateException
   */

  public void clearAfterClose() throws Fault {
    boolean pass = false;

    getEntityManager().close();
    try {
      getEntityManager().clear();
      TestUtil.logErr("IllegalStateException was not thrown");

    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("clearAfterClose failed");
    }
  }

  /*
   * @testName: containsAfterClose
   * 
   * @assertion_ids: PERSISTENCE:SPEC:881; PERSISTENCE:SPEC:882
   * 
   * @test_Strategy: Call EntityManager.contains() method after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void containsAfterClose() throws Fault {
    boolean pass = false;

    getEntityManager().close();
    try {
      Order order = new Order(6, 666, "desc6");
      getEntityManager().contains(order);

    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("containsAfterClose failed");
    }
  }

  /*
   * @testName: createQueryAfterClose
   * 
   * @assertion_ids: PERSISTENCE:SPEC:881; PERSISTENCE:SPEC:882
   * 
   * @test_Strategy: Call EntityManager.createQuery() method after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void createQueryAfterClose() throws Fault {
    boolean pass = false;

    getEntityManager().close();
    try {
      getEntityManager()
          .createQuery("SELECT Object (orders) FROM Order orders");

    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("createQueryAfterClose failed");
    }
  }

  /*
   * @testName: createNamedQueryAfterClose
   * 
   * @assertion_ids: PERSISTENCE:SPEC:881; PERSISTENCE:SPEC:882
   * 
   * @test_Strategy: Call EntityManager.createNamedQuery() method after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void createNamedQueryAfterClose() throws Fault {
    boolean pass = false;

    getEntityManager().close();
    try {
      getEntityManager().createNamedQuery("CTS NamedQuery");

    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("createNamedQueryAfterClose failed");
    }
  }

  /*
   * @testName: createNativeQueryAfterClose
   * 
   * @assertion_ids: PERSISTENCE:SPEC:881; PERSISTENCE:SPEC:882
   * 
   * @test_Strategy: Call EntityManager.createNativeQuery() method after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void createNativeQueryAfterClose() throws Fault {
    boolean pass = false;

    getEntityManager().close();
    try {
      getEntityManager().createNativeQuery(
          "SELECT o.ID from ORDER o WHERE (o.TOTALPRICE >100");

    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("createNativeQueryAfterClose failed");
    }
  }

  /*
   * @testName: joinTransactionAfterClose
   * 
   * @assertion_ids: PERSISTENCE:SPEC:881; PERSISTENCE:SPEC:882
   * 
   * @test_Strategy: Call EntityManager.joinTransaction() method after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void joinTransactionAfterClose() throws Fault {
    boolean pass = false;

    getEntityManager().close();
    try {
      getEntityManager().joinTransaction();

    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("joinTransactionAfterClose failed");
    }
  }

  /*
   * @testName: getDelegateAfterClose
   * 
   * @assertion_ids: PERSISTENCE:SPEC:881; PERSISTENCE:SPEC:882
   * 
   * @test_Strategy: Call EntityManager.getDelegate() method after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void getDelegateAfterClose() throws Fault {
    boolean pass = false;

    getEntityManager().close();
    try {
      getEntityManager().getDelegate();

    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("getDelegateAfterClose failed");
    }
  }

  /*
   * @testName: closeAfterClose
   * 
   * @assertion_ids: PERSISTENCE:SPEC:881; PERSISTENCE:SPEC:882
   * 
   * @test_Strategy: Call EntityManager.close() method after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void closeAfterClose() throws Fault {
    boolean pass = false;

    getEntityManager().close();
    try {
      getEntityManager().close();

    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("closeAfterClose failed");
    }
  }

  /*
   * @testName: getEntityManagerFactoryIllegalStateExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:482;
   * 
   * @test_Strategy: Get EntityManagerFactory from closed EntityManager
   */
  public void getEntityManagerFactoryIllegalStateExceptionTest() throws Fault {
    boolean pass = false;
    getEntityManager().close();
    try {
      getEntityManager().getEntityManagerFactory();
      TestUtil.logErr("IllegalStateException was not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    if (!pass) {
      throw new Fault(
          "getEntityManagerFactoryIllegalStateExceptionTest failed");
    }
  }

  /*
   * @testName: emGetMetamodelIllegalStateExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:485;
   * 
   * @test_Strategy: Close the EntityManager, then call em.getMetaModel()
   */
  public void emGetMetamodelIllegalStateExceptionTest() throws Fault {
    boolean pass = false;

    getEntityManager().close();
    try {
      getEntityManager().getMetamodel();
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException ise) {
      TestUtil.logTrace("Received expected IllegalStateException");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    if (!pass) {
      throw new Fault("emGetMetamodelIllegalStateExceptionTest failed");
    }
  }

  /*
   * @testName: getCriteriaBuilderIllegalStateExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:481
   * 
   * @test_Strategy: access EntityManager.getCriteriaBuilder when manager is
   * closed and verify exception is thrown
   *
   */
  public void getCriteriaBuilderIllegalStateExceptionTest() throws Fault {
    boolean pass = false;

    getEntityManager().close();
    try {
      getEntityManager().getCriteriaBuilder();
      TestUtil.logErr("IllegalStateException was not thrown");
    } catch (IllegalStateException ise) {
      pass = true;
      TestUtil.logTrace("Received expected IllegalStateException");
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    if (!pass) {
      throw new Fault("getCriteriaBuilderIllegalStateExceptionTest failed");
    }
  }

  /*
   * @testName: entityManagerMethodsAfterClose1Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36;
   * 
   * @test_Strategy: Call EntityManager.createNamedQuery(String, Class) after
   * calling EntityManager.close()and expect IllegalStateException
   */
  public void entityManagerMethodsAfterClose1Test() throws Fault {
    boolean pass = false;

    getEntityManager().close();
    try {
      getEntityManager().createNamedQuery("foo", Employee.class);
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("entityManagerMethodsAfterClose1Test failed");
    }
  }

  /*
   * @testName: entityManagerMethodsAfterClose2Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36;
   * 
   * @test_Strategy: Call EntityManager.createNamedStoredProcedureQuery(String)
   * after calling EntityManager.close()and expect IllegalStateException
   */
  public void entityManagerMethodsAfterClose2Test() throws Fault {
    boolean pass = false;
    getEntityManager().close();
    try {
      getEntityManager().createNamedStoredProcedureQuery("foo");
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("entityManagerMethodsAfterClose2Test failed");
    }

  }

  /*
   * @testName: entityManagerMethodsAfterClose3Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36;
   * 
   * @test_Strategy: Call EntityManager.createNativeQuery(String,Class) after
   * calling EntityManager.close()and expect IllegalStateException
   */
  public void entityManagerMethodsAfterClose3Test() throws Fault {
    boolean pass = false;
    getEntityManager().close();
    try {
      getEntityManager().createNativeQuery("Select * from Employee",
          Employee.class);
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("entityManagerMethodsAfterClose3Test failed");
    }

  }

  /*
   * @testName: entityManagerMethodsAfterClose4Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36;
   * 
   * @test_Strategy: Call EntityManager.createNativeQuery(String, String) after
   * calling EntityManager.close()and expect IllegalStateException
   */
  public void entityManagerMethodsAfterClose4Test() throws Fault {
    boolean pass = false;
    getEntityManager().close();
    try {
      getEntityManager().createNativeQuery("Select * from Employee",
          "resultSetMapping");
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("entityManagerMethodsAfterClose4Test failed");
    }

  }

  // asdf
  /*
   * @testName: entityManagerMethodsAfterClose5Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36;
   * 
   * @test_Strategy: Call EntityManager.createQuery(CriteriaDelete) after
   * calling EntityManager.close()and expect IllegalStateException
   */
  public void entityManagerMethodsAfterClose5Test() throws Fault {
    boolean pass = false;
    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
    CriteriaDelete<Employee> cd = cbuilder.createCriteriaDelete(Employee.class);
    getEntityManager().close();
    try {
      getEntityManager().createQuery(cd);
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("entityManagerMethodsAfterClose5Test failed");
    }

  }

  /*
   * @testName: entityManagerMethodsAfterClose6Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36;
   * 
   * @test_Strategy: Call EntityManager.createQuery(CriteriaQuery) after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void entityManagerMethodsAfterClose6Test() throws Fault {
    boolean pass = false;
    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
    CriteriaQuery<Employee> cq = cbuilder.createQuery(Employee.class);
    getEntityManager().close();
    try {
      getEntityManager().createQuery(cq);
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("entityManagerMethodsAfterClose6Test failed");
    }

  }

  /*
   * @testName: entityManagerMethodsAfterClose7Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36;
   * 
   * @test_Strategy: Call EntityManager.createQuery(CriteriaUpdate) after
   * calling EntityManager.close()and expect IllegalStateException
   */
  public void entityManagerMethodsAfterClose7Test() throws Fault {
    boolean pass = false;
    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
    CriteriaUpdate<Employee> cu = cbuilder.createCriteriaUpdate(Employee.class);
    getEntityManager().close();
    try {
      getEntityManager().createQuery(cu);
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("entityManagerMethodsAfterClose7Test failed");
    }

  }

  /*
   * @testName: entityManagerMethodsAfterClose8Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36;
   * 
   * @test_Strategy: Call EntityManager.createQuery(String, Class) after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void entityManagerMethodsAfterClose8Test() throws Fault {
    boolean pass = false;
    getEntityManager().close();
    try {
      getEntityManager().createQuery("Select * from Employee", Employee.class);
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("entityManagerMethodsAfterClose8Test failed");
    }

  }

  /*
   * @testName: entityManagerMethodsAfterClose9Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36;
   * 
   * @test_Strategy: Call EntityManager.createStoredProcedureQuery(String) after
   * calling EntityManager.close()and expect IllegalStateException
   */
  public void entityManagerMethodsAfterClose9Test() throws Fault {
    boolean pass = false;
    getEntityManager().close();
    try {
      getEntityManager().createStoredProcedureQuery("procedureName");
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("entityManagerMethodsAfterClose9Test failed");
    }

  }

  /*
   * @testName: entityManagerMethodsAfterClose10Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36;
   * 
   * @test_Strategy: Call EntityManager.createStoredProcedureQuery(String,
   * Class) after calling EntityManager.close()and expect IllegalStateException
   */
  public void entityManagerMethodsAfterClose10Test() throws Fault {
    boolean pass = false;
    getEntityManager().close();
    try {
      getEntityManager().createStoredProcedureQuery("procedureName",
          Employee.class);
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("entityManagerMethodsAfterClose10Test failed");
    }

  }

  /*
   * @testName: entityManagerMethodsAfterClose11Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36;
   * 
   * @test_Strategy: Call EntityManager.createStoredProcedureQuery(String,
   * String) after calling EntityManager.close()and expect IllegalStateException
   */
  public void entityManagerMethodsAfterClose11Test() throws Fault {
    boolean pass = false;
    getEntityManager().close();
    try {
      getEntityManager().createStoredProcedureQuery("procedureName",
          "resultSetMappings");
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("entityManagerMethodsAfterClose11Test failed");
    }

  }

  /*
   * @testName: entityManagerMethodsAfterClose12Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36;
   * 
   * @test_Strategy: Call EntityManager.detach(Object) after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void entityManagerMethodsAfterClose12Test() throws Fault {
    boolean pass = false;
    getEntityManager().close();
    try {
      getEntityManager().close();
      Employee emp = new Employee(1, "foo", "bar", getUtilDate("2000-02-14"),
          (float) 35000.0);
      getEntityManager().detach(emp);
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("entityManagerMethodsAfterClose12Test failed");
    }

  }

  /*
   * @testName: entityManagerMethodsAfterClose13Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36;
   * 
   * @test_Strategy: Call EntityManager.find(Class, Object, LockModeType) after
   * calling EntityManager.close()and expect IllegalStateException
   */
  public void entityManagerMethodsAfterClose13Test() throws Fault {
    boolean pass = false;
    getEntityManager().close();
    try {
      getEntityManager().find(Employee.class, 1, LockModeType.NONE);
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("entityManagerMethodsAfterClose13Test failed");
    }

  }

  /*
   * @testName: entityManagerMethodsAfterClose14Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36;
   * 
   * @test_Strategy: Call EntityManager.find(Class, Object, LockModeType, Map)
   * after calling EntityManager.close()and expect IllegalStateException
   */
  public void entityManagerMethodsAfterClose14Test() throws Fault {
    boolean pass = false;
    getEntityManager().close();
    try {
      getEntityManager().find(Employee.class, 1, LockModeType.NONE, map);
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("entityManagerMethodsAfterClose14Test failed");
    }

  }

  /*
   * @testName: entityManagerMethodsAfterClose15Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36;
   * 
   * @test_Strategy: Call EntityManager.find(Class, Object, Map) after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void entityManagerMethodsAfterClose15Test() throws Fault {
    boolean pass = false;
    getEntityManager().close();
    try {
      getEntityManager().find(Employee.class, 1, map);
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("entityManagerMethodsAfterClose15Test failed");
    }

  }

  /*
   * @testName: entityManagerMethodsAfterClose16Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36;
   * 
   * @test_Strategy: Call EntityManager.getCriteriaBuilder() after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void entityManagerMethodsAfterClose16Test() throws Fault {
    boolean pass = false;
    getEntityManager().close();
    try {
      getEntityManager().getCriteriaBuilder();
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("entityManagerMethodsAfterClose16Test failed");
    }

  }

  /*
   * @testName: entityManagerMethodsAfterClose17Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36;
   * 
   * @test_Strategy: Call EntityManager.getEntityManagerFactory() after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void entityManagerMethodsAfterClose17Test() throws Fault {
    boolean pass = false;
    getEntityManager().close();
    try {
      getEntityManager().getEntityManagerFactory();
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("entityManagerMethodsAfterClose17Test failed");
    }

  }

  /*
   * @testName: entityManagerMethodsAfterClose18Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36;
   * 
   * @test_Strategy: Call EntityManager.getLockMode(Object) after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void entityManagerMethodsAfterClose18Test() throws Fault {
    boolean pass = false;
    getEntityManager().close();
    Employee emp = new Employee(1, "foo", "bar", getUtilDate("2000-02-14"),
        (float) 35000.0);

    try {
      getEntityManager().getLockMode(emp);
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("entityManagerMethodsAfterClose18Test failed");
    }

  }

  /*
   * @testName: entityManagerMethodsAfterClose19Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36;
   * 
   * @test_Strategy: Call EntityManager.getMetamodel() after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void entityManagerMethodsAfterClose19Test() throws Fault {
    boolean pass = false;
    getEntityManager().close();
    try {
      getEntityManager().getMetamodel();
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("entityManagerMethodsAfterClose19Test failed");
    }

  }

  /*
   * @testName: entityManagerMethodsAfterClose20Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36;
   * 
   * @test_Strategy: Call EntityManager.isJoinedToTransaction() after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void entityManagerMethodsAfterClose20Test() throws Fault {
    boolean pass = false;
    getEntityManager().close();
    try {
      getEntityManager().isJoinedToTransaction();
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("entityManagerMethodsAfterClose20Test failed");
    }

  }

  /*
   * @testName: entityManagerMethodsAfterClose21Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36;
   * 
   * @test_Strategy: Call EntityManager.lock(Object, LockModeType, Map) after
   * calling EntityManager.close()and expect IllegalStateException
   */
  public void entityManagerMethodsAfterClose21Test() throws Fault {
    boolean pass = false;
    getEntityManager().close();
    Employee emp = new Employee(1, "foo", "bar", getUtilDate("2000-02-14"),
        (float) 35000.0);

    try {
      getEntityManager().lock(emp, LockModeType.NONE, map);
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("entityManagerMethodsAfterClose21Test failed");
    }

  }

  /*
   * @testName: entityManagerMethodsAfterClose22Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36;
   * 
   * @test_Strategy: Call EntityManager.refresh(java.lang.Object, LockModeType)
   * after calling EntityManager.close()and expect IllegalStateException
   */
  public void entityManagerMethodsAfterClose22Test() throws Fault {
    boolean pass = false;
    getEntityManager().close();
    Employee emp = new Employee(1, "foo", "bar", getUtilDate("2000-02-14"),
        (float) 35000.0);

    try {
      getEntityManager().refresh(emp, LockModeType.NONE);
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("entityManagerMethodsAfterClose22Test failed");
    }

  }

  /*
   * @testName: entityManagerMethodsAfterClose23Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36;
   * 
   * @test_Strategy: Call EntityManager.refresh(Object, LockModeType, Map) after
   * calling EntityManager.close()and expect IllegalStateException
   */
  public void entityManagerMethodsAfterClose23Test() throws Fault {
    boolean pass = false;
    getEntityManager().close();
    Employee emp = new Employee(1, "foo", "bar", getUtilDate("2000-02-14"),
        (float) 35000.0);

    try {
      getEntityManager().refresh(emp, LockModeType.NONE, map);
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("entityManagerMethodsAfterClose23Test failed");
    }

  }

  /*
   * @testName: entityManagerMethodsAfterClose24Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36;
   * 
   * @test_Strategy: Call EntityManager.refresh(Object, Map<String,Object) after
   * calling EntityManager.close()and expect IllegalStateException
   */
  public void entityManagerMethodsAfterClose24Test() throws Fault {
    boolean pass = false;

    getEntityManager().close();
    Employee emp = new Employee(1, "foo", "bar", getUtilDate("2000-02-14"),
        (float) 35000.0);
    try {
      getEntityManager().refresh(emp, map);
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("entityManagerMethodsAfterClose24Test failed");
    }

  }

  /*
   * @testName: entityManagerMethodsAfterClose25Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36;
   * 
   * @test_Strategy: Call EntityManager.setProperty(String, Object) after
   * calling EntityManager.close()and expect IllegalStateException
   */
  public void entityManagerMethodsAfterClose25Test() throws Fault {
    boolean pass = false;
    getEntityManager().close();
    try {
      getEntityManager().setProperty("foo", "bar");
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("entityManagerMethodsAfterClose25Test failed");
    }

  }

  /*
   * @testName: queryMethodsAfterClose1Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
   * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
   * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
   * 
   * @test_Strategy: Call Query.executeUpdate() after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void queryMethodsAfterClose1Test() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Query query = getEntityManager().createQuery("select e.id from Employee e");
    getEntityManager().close();
    try {
      query.executeUpdate();
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }
    if (!pass) {
      throw new Fault("queryMethodsAfterClose1Test failed");
    }
  }

  /*
   * @testName: queryMethodsAfterClose2Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
   * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
   * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
   * 
   * @test_Strategy: Call Query.getFirstResult() after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void queryMethodsAfterClose2Test() throws Fault {
    boolean pass = false;
    getEntityTransaction().begin();
    Query query = getEntityManager().createQuery("select e.id from Employee e");
    getEntityManager().close();
    try {
      query.getFirstResult();
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }
    if (!pass) {
      throw new Fault("queryMethodsAfterClose2Test failed");
    }
  }

  /*
   * @testName: queryMethodsAfterClose3Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
   * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
   * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
   * 
   * @test_Strategy: Call Query.getFlushMode() after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void queryMethodsAfterClose3Test() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Query query = getEntityManager().createQuery("select e.id from Employee e");
    getEntityManager().close();
    try {
      query.getFlushMode();
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }
    if (!pass) {
      throw new Fault("queryMethodsAfterClose3Test failed");
    }
  }
  /*
   * @testName: queryMethodsAfterClose4Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
   * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
   * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
   * 
   * @test_Strategy: Call Query.getHints() after calling
   * EntityManager.close()and expect IllegalStateException
   */

  public void queryMethodsAfterClose4Test() throws Fault {
    boolean pass = false;

    TestUtil.logMsg("Testing getHints() )");
    getEntityTransaction().begin();
    Query query = getEntityManager().createQuery("select e.id from Employee e");
    getEntityManager().close();
    try {
      query.getHints();
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }
    if (!pass) {
      throw new Fault("queryMethodsAfterClose4Test failed");
    }
  }

  /*
   * @testName: queryMethodsAfterClose5Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
   * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
   * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
   * 
   * @test_Strategy: Call Query.getLockMode() after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void queryMethodsAfterClose5Test() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Query query = getEntityManager().createQuery("select e.id from Employee e");
    getEntityManager().close();
    try {
      query.getLockMode();
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      if (getEntityTransaction().getRollbackOnly() == true) {
        TestUtil.logErr(
            "Transaction was marked for rollback and should not have been");
      } else {
        pass = true;
        TestUtil.logTrace("Transaction was not marked for rollback");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }
    if (!pass) {
      throw new Fault("queryMethodsAfterClose5Test failed");
    }
  }

  /*
   * @testName: queryMethodsAfterClose6Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
   * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
   * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
   * 
   * @test_Strategy: Call Query.getMaxResults() after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void queryMethodsAfterClose6Test() throws Fault {
    boolean pass = false;
    getEntityTransaction().begin();
    Query query = getEntityManager().createQuery("select e.id from Employee e");
    getEntityManager().close();
    try {
      query.getMaxResults();
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }
    if (!pass) {
      throw new Fault("queryMethodsAfterClose6Test failed");
    }
  }

  /*
   * @testName: queryMethodsAfterClose7Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
   * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
   * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
   * 
   * @test_Strategy: Call Query.getParameter(int) after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void queryMethodsAfterClose7Test() throws Fault {
    boolean pass = false;
    getEntityTransaction().begin();
    Query query = getEntityManager()
        .createQuery("select e.id from Employee e where e.id = ?1");
    query.setParameter(1, 1);
    getEntityManager().close();
    try {
      query.getParameter(1);
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      if (getEntityTransaction().getRollbackOnly() == true) {
        TestUtil.logErr(
            "Transaction was marked for rollback and should not have been");
      } else {
        pass = true;
        TestUtil.logTrace("Transaction was not marked for rollback");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }
    if (!pass) {
      throw new Fault("queryMethodsAfterClose7Test failed");
    }
  }

  /*
   * @testName: queryMethodsAfterClose8Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
   * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
   * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
   * 
   * @test_Strategy: Call Query.getParameter(int position, Class) after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void queryMethodsAfterClose8Test() throws Fault {
    boolean pass = false;
    getEntityTransaction().begin();
    Query query = getEntityManager()
        .createQuery("select e.id from Employee e where e.id = ?1");
    query.setParameter(1, 1);
    getEntityManager().close();
    try {
      query.getParameter(1, Integer.class);
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      if (getEntityTransaction().getRollbackOnly() == true) {
        TestUtil.logErr(
            "Transaction was marked for rollback and should not have been");
      } else {
        pass = true;
        TestUtil.logTrace("Transaction was not marked for rollback");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }
    if (!pass) {
      throw new Fault("queryMethodsAfterClose8Test failed");
    }
  }

  /*
   * @testName: queryMethodsAfterClose9Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
   * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
   * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
   * 
   * @test_Strategy: Call Query.getParameter(String) after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void queryMethodsAfterClose9Test() throws Fault {
    boolean pass = false;
    getEntityTransaction().begin();
    Query query = getEntityManager()
        .createQuery("select e.id from Employee e where e.lastName = :name");
    query.setParameter("name", "Foo");
    getEntityManager().close();
    try {
      query.getParameter("name");
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      if (getEntityTransaction().getRollbackOnly() == true) {
        TestUtil.logErr(
            "Transaction was marked for rollback and should not have been");
      } else {
        pass = true;
        TestUtil.logTrace("Transaction was not marked for rollback");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }
    if (!pass) {
      throw new Fault("queryMethodsAfterClose9Test failed");
    }
  }

  /*
   * @testName: queryMethodsAfterClose10Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
   * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
   * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
   * 
   * @test_Strategy: Call Query.getParameter(String, Class) after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void queryMethodsAfterClose10Test() throws Fault {
    boolean pass = false;
    getEntityTransaction().begin();
    Query query = getEntityManager()
        .createQuery("select e.id from Employee e where e.lastName = :name");
    query.setParameter("name", "Foo");
    getEntityManager().close();
    try {
      query.getParameter("name", String.class);
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      if (getEntityTransaction().getRollbackOnly() == true) {
        TestUtil.logErr(
            "Transaction was marked for rollback and should not have been");
      } else {
        pass = true;
        TestUtil.logTrace("Transaction was not marked for rollback");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }
    if (!pass) {
      throw new Fault("queryMethodsAfterClose10Test failed");
    }
  }

  /*
   * @testName: queryMethodsAfterClose11Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
   * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
   * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
   * 
   * @test_Strategy: Call Query.getParameters() after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void queryMethodsAfterClose11Test() throws Fault {
    boolean pass = false;
    getEntityTransaction().begin();
    Query query = getEntityManager()
        .createQuery("select e.id from Employee e where e.id = ?1");
    query.setParameter(1, 1);
    getEntityManager().close();
    try {
      query.getParameters();
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      if (getEntityTransaction().getRollbackOnly() == true) {
        TestUtil.logErr(
            "Transaction was marked for rollback and should not have been");
      } else {
        pass = true;
        TestUtil.logTrace("Transaction was not marked for rollback");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }
    if (!pass) {
      throw new Fault("queryMethodsAfterClose11Test failed");
    }
  }

  /*
   * @testName: queryMethodsAfterClose12Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
   * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
   * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
   * 
   * @test_Strategy: Call Query.getParameterValue(int) after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void queryMethodsAfterClose12Test() throws Fault {
    boolean pass = false;
    getEntityTransaction().begin();
    Query query = getEntityManager()
        .createQuery("select e.id from Employee e where e.id = ?1");
    query.setParameter(1, 1);
    getEntityManager().close();
    try {
      query.getParameterValue(1);
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      if (getEntityTransaction().getRollbackOnly() == true) {
        TestUtil.logErr(
            "Transaction was marked for rollback and should not have been");
      } else {
        pass = true;
        TestUtil.logTrace("Transaction was not marked for rollback");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }
    if (!pass) {
      throw new Fault("queryMethodsAfterClose12Test failed");
    }
  }

  /*
   * @testName: queryMethodsAfterClose13Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
   * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
   * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
   * 
   * @test_Strategy: Call Query.getParameterValue(Parameter) after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void queryMethodsAfterClose13Test() throws Fault {
    boolean pass = false;
    getEntityTransaction().begin();
    Query query = getEntityManager()
        .createQuery("select e.id from Employee e where e.id = ?1");
    query.setParameter(1, 1);
    Parameter p = query.getParameter(1);
    getEntityManager().close();
    try {
      query.getParameterValue(p);
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      if (getEntityTransaction().getRollbackOnly() == true) {
        TestUtil.logErr(
            "Transaction was marked for rollback and should not have been");
      } else {
        pass = true;
        TestUtil.logTrace("Transaction was not marked for rollback");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }
    if (!pass) {
      throw new Fault("queryMethodsAfterClose13Test failed");
    }
  }

  /*
   * @testName: queryMethodsAfterClose14Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
   * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
   * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
   * 
   * @test_Strategy: Call Query.getParameterValue(String) after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void queryMethodsAfterClose14Test() throws Fault {
    boolean pass = false;
    getEntityTransaction().begin();
    Query query = getEntityManager()
        .createQuery("select e.id from Employee e where e.id = :id");
    query.setParameter("id", 1);
    getEntityManager().close();
    try {
      query.getParameterValue("id");
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      if (getEntityTransaction().getRollbackOnly() == true) {
        TestUtil.logErr(
            "Transaction was marked for rollback and should not have been");
      } else {
        pass = true;
        TestUtil.logTrace("Transaction was not marked for rollback");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }
    if (!pass) {
      throw new Fault("queryMethodsAfterClose14Test failed");
    }
  }

  /*
   * @testName: queryMethodsAfterClose15Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
   * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
   * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
   * 
   * @test_Strategy: Call Query.getResultList() after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void queryMethodsAfterClose15Test() throws Fault {
    boolean pass = false;

    Query query = getEntityManager().createQuery("select e.id from Employee e");
    getEntityManager().close();
    try {
      query.getResultList();
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }
    if (!pass) {
      throw new Fault("queryMethodsAfterClose15Test failed");
    }
  }

  /*
   * @testName: queryMethodsAfterClose16Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
   * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
   * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
   * 
   * @test_Strategy: Call Query.getSingleResult() after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void queryMethodsAfterClose16Test() throws Fault {
    boolean pass = false;

    Query query = getEntityManager().createQuery("select e.id from Employee e");
    getEntityManager().close();
    try {
      query.getSingleResult();
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }
    if (!pass) {
      throw new Fault("queryMethodsAfterClose16Test failed");
    }
  }

  /*
   * @testName: queryMethodsAfterClose17Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
   * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
   * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
   * 
   * @test_Strategy: Call Query.isBound(Parameter) after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void queryMethodsAfterClose17Test() throws Fault {
    boolean pass = false;

    Query query = getEntityManager()
        .createQuery("select e.id from Employee e where e.id = ?1");
    query.setParameter(1, 1);
    Parameter p = query.getParameter(1);
    getEntityManager().close();
    try {
      query.isBound(p);
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }
    if (!pass) {
      throw new Fault("queryMethodsAfterClose17Test failed");
    }
  }

  /*
   * @testName: queryMethodsAfterClose18Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
   * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
   * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
   * 
   * @test_Strategy: Call Query.setFirstResult(int) after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void queryMethodsAfterClose18Test() throws Fault {
    boolean pass = false;

    Query query = getEntityManager().createQuery("select e.id from Employee e");
    getEntityManager().close();
    try {
      query.setFirstResult(1);
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }
    if (!pass) {
      throw new Fault("queryMethodsAfterClose18Test failed");
    }
  }

  /*
   * @testName: queryMethodsAfterClose19Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
   * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
   * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
   * 
   * @test_Strategy: Call Query.setFlushMode(FlushModeType) after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void queryMethodsAfterClose19Test() throws Fault {
    boolean pass = false;

    Query query = getEntityManager().createQuery("select e.id from Employee e");
    getEntityManager().close();
    try {
      query.setFlushMode(FlushModeType.AUTO);
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }
    if (!pass) {
      throw new Fault("queryMethodsAfterClose19Test failed");
    }
  }

  /*
   * @testName: queryMethodsAfterClose20Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
   * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
   * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
   * 
   * @test_Strategy: Call Query.setHint(String, Object) after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void queryMethodsAfterClose20Test() throws Fault {
    boolean pass = false;

    Query query = getEntityManager().createQuery("select e.id from Employee e");
    getEntityManager().close();
    try {
      query.setHint("foo", "bar");
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }
    if (!pass) {
      throw new Fault("queryMethodsAfterClose20Test failed");
    }
  }

  /*
   * @testName: queryMethodsAfterClose21Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
   * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
   * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
   * 
   * @test_Strategy: Call Query.setLockMode(LockModeType) after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void queryMethodsAfterClose21Test() throws Fault {
    boolean pass = false;

    Query query = getEntityManager().createQuery("select e.id from Employee e");
    getEntityManager().close();
    try {
      query.setLockMode(LockModeType.NONE);
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }
    if (!pass) {
      throw new Fault("queryMethodsAfterClose21Test failed");
    }
  }

  /*
   * @testName: queryMethodsAfterClose22Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
   * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
   * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
   * 
   * @test_Strategy: Call Query.setMaxResults(int) after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void queryMethodsAfterClose22Test() throws Fault {
    boolean pass = false;

    Query query = getEntityManager().createQuery("select e.id from Employee e");
    getEntityManager().close();
    try {
      query.setMaxResults(1);
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }
    if (!pass) {
      throw new Fault("queryMethodsAfterClose22Test failed");
    }
  }

  /*
   * @testName: queryMethodsAfterClose23Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
   * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
   * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
   * 
   * @test_Strategy: Call Query.setParameter(int, Calendar, TemporalType) after
   * calling EntityManager.close()and expect IllegalStateException
   */
  public void queryMethodsAfterClose23Test() throws Fault {
    boolean pass = false;

    Query query = getEntityManager()
        .createQuery("select e.id from Employee e where e.hireDate = ?1");
    getEntityManager().close();
    try {
      query.setParameter(1, getCalDate(), TemporalType.DATE);
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }
    if (!pass) {
      throw new Fault("queryMethodsAfterClose23Test failed");
    }
  }

  /*
   * @testName: queryMethodsAfterClose24Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
   * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
   * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
   * 
   * @test_Strategy: Call Query.setParameter(int, Date, TemporalType) after
   * calling EntityManager.close()and expect IllegalStateException
   */
  public void queryMethodsAfterClose24Test() throws Fault {
    boolean pass = false;

    Query query = getEntityManager()
        .createQuery("select e.id from Employee e where e.hireDate = ?1");
    getEntityManager().close();
    try {
      query.setParameter(1, getUtilDate(), TemporalType.DATE);
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }
    if (!pass) {
      throw new Fault("queryMethodsAfterClose24Test failed");
    }
  }

  /*
   * @testName: queryMethodsAfterClose25Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
   * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
   * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
   * 
   * @test_Strategy: Call Query.setParameter(int, Object) after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void queryMethodsAfterClose25Test() throws Fault {
    boolean pass = false;

    Query query = getEntityManager()
        .createQuery("select e.id from Employee e where e.id = ?1");
    getEntityManager().close();
    try {
      query.setParameter(1, 1);
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }
    if (!pass) {
      throw new Fault("queryMethodsAfterClose25Test failed");
    }
  }

  /*
   * @testName: queryMethodsAfterClose26Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
   * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
   * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
   * 
   * @test_Strategy: Call Query.setParameter(Parameter<Calendar>, Calendar,
   * TemporalType) after calling EntityManager.close()and expect
   * IllegalStateException
   */
  public void queryMethodsAfterClose26Test() throws Fault {
    boolean pass = false;

    Query query = getEntityManager()
        .createQuery("select e.id from Employee e where e.hireDate = :date");
    query.setParameter("date", getCalDate(), TemporalType.DATE);
    Parameter p = query.getParameter("date");
    getEntityManager().close();
    try {
      query.setParameter(p, getCalDate(), TemporalType.DATE);
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }
    if (!pass) {
      throw new Fault("queryMethodsAfterClose26Test failed");
    }
  }

  /*
   * @testName: queryMethodsAfterClose27Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
   * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
   * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
   * 
   * @test_Strategy: Call Query.setParameter(Parameter<Date>, Date,
   * TemporalType) after calling EntityManager.close()and expect
   * IllegalStateException
   */
  public void queryMethodsAfterClose27Test() throws Fault {
    boolean pass = false;

    Query query = getEntityManager()
        .createQuery("select e.id from Employee e where e.hireDate = :date");
    query.setParameter("date", getCalDate(), TemporalType.DATE);
    Parameter p = query.getParameter("date", java.util.Date.class);
    getEntityManager().close();
    try {
      query.setParameter(p, getUtilDate(), TemporalType.DATE);
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }
    if (!pass) {
      throw new Fault("queryMethodsAfterClose27Test failed");
    }
  }

  /*
   * @testName: queryMethodsAfterClose28Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
   * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
   * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
   * 
   * @test_Strategy: Call Query.setParameter(Parameter<T>, T) after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void queryMethodsAfterClose28Test() throws Fault {
    boolean pass = false;

    Query query = getEntityManager()
        .createQuery("select e.id from Employee e where e.id = :id");
    query.setParameter("id", 1);
    Parameter p = query.getParameter("id");
    getEntityManager().close();
    try {
      query.setParameter(p, 1);
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }
    if (!pass) {
      throw new Fault("queryMethodsAfterClose28Test failed");
    }
  }

  /*
   * @testName: queryMethodsAfterClose29Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
   * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
   * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
   * 
   * @test_Strategy: Call Query.setParameter(String, Calendar, TemporalType)
   * after calling EntityManager.close()and expect IllegalStateException
   */
  public void queryMethodsAfterClose29Test() throws Fault {
    boolean pass = false;

    Query query = getEntityManager()
        .createQuery("select e.id from Employee e where e.hireDate = :date");
    getEntityManager().close();
    try {
      query.setParameter("date", getCalDate(), TemporalType.DATE);
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }
    if (!pass) {
      throw new Fault("queryMethodsAfterClose29Test failed");
    }
  }

  /*
   * @testName: queryMethodsAfterClose30Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
   * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
   * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
   * 
   * @test_Strategy: Call Query.setParameter(String, Date, TemporalType) after
   * calling EntityManager.close()and expect IllegalStateException
   */
  public void queryMethodsAfterClose30Test() throws Fault {
    boolean pass = false;

    Query query = getEntityManager()
        .createQuery("select e.id from Employee e where e.hireDate = :date");
    getEntityManager().close();
    try {
      query.setParameter("date", getUtilDate(), TemporalType.DATE);
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }
    if (!pass) {
      throw new Fault("queryMethodsAfterClose30Test failed");
    }
  }

  /*
   * @testName: queryMethodsAfterClose31Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
   * PERSISTENCE:SPEC:1302; PERSISTENCE:SPEC:1302.1; PERSISTENCE:SPEC:1302.2;
   * PERSISTENCE:SPEC:1302.3; PERSISTENCE:SPEC:1302.5;
   * 
   * @test_Strategy: Call Query.setParameter(String, Object) after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void queryMethodsAfterClose31Test() throws Fault {
    boolean pass = false;

    Query query = getEntityManager()
        .createQuery("select e.id from Employee e where e.id = :id");
    getEntityManager().close();
    try {
      query.setParameter("id", 1);
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("queryMethodsAfterClose31Test failed");
    }

  }

  /*
   * @testName: typedQueryMethodsAfterClose1Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
   * 
   * @test_Strategy: Call TypedQuery.getResultList() after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void typedQueryMethodsAfterClose1Test() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
    CriteriaQuery<A> cquery = cbuilder.createQuery(A.class);
    Root<A> a = cquery.from(A.class);
    cquery.select(a);
    TypedQuery<A> tquery = getEntityManager().createQuery(cquery);
    getEntityManager().close();
    try {
      tquery.getResultList();

      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("typedQueryMethodsAfterClose1Test failed");
    }

  }

  /*
   * @testName: typedQueryMethodsAfterClose2Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
   * 
   * @test_Strategy: Call TypedQuery.getResultList() after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void typedQueryMethodsAfterClose2Test() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
    CriteriaQuery<A> cquery = cbuilder.createQuery(A.class);
    Root<A> a = cquery.from(A.class);
    cquery.select(a);
    TypedQuery<A> tquery = getEntityManager().createQuery(cquery);
    getEntityManager().close();
    try {
      tquery.getSingleResult();

      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("typedQueryMethodsAfterClose2Test failed");
    }

  }

  /*
   * @testName: typedQueryMethodsAfterClose3Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
   * 
   * @test_Strategy: Call TypedQuery.setFirstResult(int) after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void typedQueryMethodsAfterClose3Test() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
    CriteriaQuery<A> cquery = cbuilder.createQuery(A.class);
    Root<A> a = cquery.from(A.class);
    cquery.select(a);
    TypedQuery<A> tquery = getEntityManager().createQuery(cquery);
    getEntityManager().close();
    try {
      tquery.setFirstResult(1);

      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("typedQueryMethodsAfterClose3Test failed");
    }

  }

  /*
   * @testName: typedQueryMethodsAfterClose4Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
   * 
   * @test_Strategy: Call TypedQuery.setFlushMode(FlushModeType) after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void typedQueryMethodsAfterClose4Test() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
    CriteriaQuery<A> cquery = cbuilder.createQuery(A.class);
    Root<A> a = cquery.from(A.class);
    cquery.select(a);
    TypedQuery<A> tquery = getEntityManager().createQuery(cquery);
    getEntityManager().close();
    try {
      tquery.setFlushMode(FlushModeType.AUTO);

      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("typedQueryMethodsAfterClose4Test failed");
    }

  }

  /*
   * @testName: typedQueryMethodsAfterClose5Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
   * 
   * @test_Strategy: Call TypedQuery.setHint(String, Object) after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void typedQueryMethodsAfterClose5Test() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
    CriteriaQuery<A> cquery = cbuilder.createQuery(A.class);
    Root<A> a = cquery.from(A.class);
    cquery.select(a);
    TypedQuery<A> tquery = getEntityManager().createQuery(cquery);
    getEntityManager().close();
    try {
      tquery.setHint("foo", "bar");

      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("typedQueryMethodsAfterClose5Test failed");
    }

  }

  /*
   * @testName: typedQueryMethodsAfterClose6Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
   * 
   * @test_Strategy: Call TypedQuery.setLockMode(LockModeType) after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void typedQueryMethodsAfterClose6Test() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
    CriteriaQuery<A> cquery = cbuilder.createQuery(A.class);
    Root<A> a = cquery.from(A.class);
    cquery.select(a);
    TypedQuery<A> tquery = getEntityManager().createQuery(cquery);
    getEntityManager().close();
    try {
      tquery.setLockMode(LockModeType.NONE);

      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("typedQueryMethodsAfterClose6Test failed");
    }

  }

  /*
   * @testName: typedQueryMethodsAfterClose7Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
   * 
   * @test_Strategy: Call TypedQuery.setMaxResults(int) after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void typedQueryMethodsAfterClose7Test() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
    CriteriaQuery<A> cquery = cbuilder.createQuery(A.class);
    Root<A> a = cquery.from(A.class);
    cquery.select(a);
    TypedQuery<A> tquery = getEntityManager().createQuery(cquery);
    getEntityManager().close();
    try {
      tquery.setMaxResults(1);

      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("typedQueryMethodsAfterClose7Test failed");
    }

  }

  /*
   * @testName: typedQueryMethodsAfterClose8Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
   * 
   * @test_Strategy: Call TypedQuery.setParameter(int, Calendar, TemporalType)
   * after calling EntityManager.close()and expect IllegalStateException
   */
  public void typedQueryMethodsAfterClose8Test() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    TypedQuery<A> tquery = getEntityManager()
        .createQuery("SELECT a FROM A a WHERE (a.basicCalendar = ?1)", A.class);
    getEntityManager().close();
    try {
      tquery.setParameter(1, getCalDate(), TemporalType.DATE);

      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("typedQueryMethodsAfterClose8Test failed");
    }

  }

  /*
   * @testName: typedQueryMethodsAfterClose9Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
   * 
   * @test_Strategy: Call TypedQuery.setParameter(int, Date, TemporalType) after
   * calling EntityManager.close()and expect IllegalStateException
   */
  public void typedQueryMethodsAfterClose9Test() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    TypedQuery<A> tquery = getEntityManager()
        .createQuery("SELECT a FROM A a WHERE (a.basicDate = ?1)", A.class);
    getEntityManager().close();
    try {
      tquery.setParameter(1, getUtilDate(), TemporalType.DATE);

      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("typedQueryMethodsAfterClose9Test failed");
    }

  }

  /*
   * @testName: typedQueryMethodsAfterClose10Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
   * 
   * @test_Strategy: Call TypedQuery.setParameter(int, Object) after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void typedQueryMethodsAfterClose10Test() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    TypedQuery<A> tquery = getEntityManager()
        .createQuery("SELECT a FROM A a WHERE (a.id = ?1)", A.class);
    getEntityManager().close();
    try {
      tquery.setParameter(1, "1");

      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("typedQueryMethodsAfterClose10Test failed");
    }

  }

  /*
   * @testName: typedQueryMethodsAfterClose11Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
   * 
   * @test_Strategy: Call TypedQuery.setParameter(Parameter<Calendar>, Calendar,
   * TemporalType) after calling EntityManager.close()and expect
   * IllegalStateException
   */
  public void typedQueryMethodsAfterClose11Test() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
    CriteriaQuery<A> cquery = cbuilder.createQuery(A.class);
    Root<A> a = cquery.from(A.class);
    cquery.select(a);
    ParameterExpression<Calendar> param1 = cbuilder.parameter(Calendar.class);
    cquery.where(cbuilder.equal(a.get("basicCalendar"), param1));
    TypedQuery<A> tquery = getEntityManager().createQuery(cquery);
    getEntityManager().close();
    try {
      tquery.setParameter(param1, getCalDate(), TemporalType.DATE);

      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("typedQueryMethodsAfterClose11Test failed");
    }

  }

  /*
   * @testName: typedQueryMethodsAfterClose12Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
   * 
   * @test_Strategy: Call TypedQuery.setParameter(Parameter<Date>, Date,
   * TemporalType) after calling EntityManager.close()and expect
   * IllegalStateException
   */
  public void typedQueryMethodsAfterClose12Test() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
    CriteriaQuery<A> cquery = cbuilder.createQuery(A.class);
    Root<A> a = cquery.from(A.class);
    cquery.select(a);
    ParameterExpression<Date> param1 = cbuilder.parameter(Date.class);
    cquery.where(cbuilder.equal(a.get("basicDate"), param1));
    TypedQuery<A> tquery = getEntityManager().createQuery(cquery);
    getEntityManager().close();
    try {
      tquery.setParameter(param1, getUtilDate(), TemporalType.DATE);

      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("typedQueryMethodsAfterClose12Test failed");
    }

  }

  /*
   * @testName: typedQueryMethodsAfterClose13Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
   * 
   * @test_Strategy: Call TypedQuery.setParameter(Parameter<T>, T) after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void typedQueryMethodsAfterClose13Test() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
    CriteriaQuery<A> cquery = cbuilder.createQuery(A.class);
    Root<A> a = cquery.from(A.class);
    cquery.select(a);
    ParameterExpression<String> param1 = cbuilder.parameter(String.class);
    cquery.where(cbuilder.equal(a.get("id"), param1));
    TypedQuery<A> tquery = getEntityManager().createQuery(cquery);
    getEntityManager().close();
    try {
      tquery.setParameter(param1, "1");

      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("typedQueryMethodsAfterClose13Test failed");
    }

  }

  /*
   * @testName: typedQueryMethodsAfterClose14Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
   * 
   * @test_Strategy: Call TypedQuery.setParameter(String, Calendar,
   * TemporalType) after calling EntityManager.close()and expect
   * IllegalStateException
   */
  public void typedQueryMethodsAfterClose14Test() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
    CriteriaQuery<A> cquery = cbuilder.createQuery(A.class);
    Root<A> a = cquery.from(A.class);
    cquery.select(a);
    ParameterExpression<Calendar> param1 = cbuilder.parameter(Calendar.class,
        "calDate");
    cquery.where(cbuilder.equal(a.get("basicCalendar"), param1));
    TypedQuery<A> tquery = getEntityManager().createQuery(cquery);
    getEntityManager().close();
    try {
      tquery.setParameter("calDate", getCalDate(), TemporalType.DATE);

      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("typedQueryMethodsAfterClose14Test failed");
    }

  }

  /*
   * @testName: typedQueryMethodsAfterClose15Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
   * 
   * @test_Strategy: Call TypedQuery.setParameter(String, Date, TemporalType)
   * after calling EntityManager.close()and expect IllegalStateException
   */
  public void typedQueryMethodsAfterClose15Test() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
    CriteriaQuery<A> cquery = cbuilder.createQuery(A.class);
    Root<A> a = cquery.from(A.class);
    cquery.select(a);
    ParameterExpression<Date> param1 = cbuilder.parameter(Date.class,
        "utilDate");
    cquery.where(cbuilder.equal(a.get("basicDate"), param1));
    TypedQuery<A> tquery = getEntityManager().createQuery(cquery);
    getEntityManager().close();
    try {
      tquery.setParameter("utilDate", getUtilDate(), TemporalType.DATE);

      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("typedQueryMethodsAfterClose15Test failed");
    }

  }

  /*
   * @testName: typedQueryMethodsAfterClose16Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36; PERSISTENCE:SPEC:608;
   * 
   * @test_Strategy: Call TypedQuery.method_name() after calling
   * EntityManager.close()and expect IllegalStateException
   */
  public void typedQueryMethodsAfterClose16Test() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
    CriteriaQuery<A> cquery = cbuilder.createQuery(A.class);
    Root<A> a = cquery.from(A.class);
    cquery.select(a);
    ParameterExpression<Integer> param1 = cbuilder.parameter(Integer.class,
        "idParam");
    cquery.where(cbuilder.equal(a.get("id"), param1));
    TypedQuery<A> tquery = getEntityManager().createQuery(cquery);
    getEntityManager().close();
    try {
      tquery.setParameter("idParam", "1");

      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("typedQueryMethodsAfterClose16Test failed");
    }

  }

  /*
   * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback18Test
   * 
   * @assertion_ids: PERSISTENCE:SPEC:611;
   * 
   * @test_Strategy: Call EntityManager.getCriteriaBuilder() that causes
   * RuntimeException and verify Transaction is set for rollback
   */
  public void entityManagerMethodsRuntimeExceptionsCauseRollback18Test()
      throws Fault {
    boolean pass = false;
    getEntityTransaction().begin();
    getEntityManager().close();
    try {
      getEntityManager().getCriteriaBuilder();
      TestUtil.logErr("RuntimeException not thrown");
    } catch (RuntimeException e) {
      TestUtil.logTrace("RuntimeException Caught as Expected.");
      if (!getEntityTransaction().getRollbackOnly()) {
        TestUtil.logErr("Transaction was not marked for rollback");
      } else {
        TestUtil.logTrace("Transaction was marked for rollback");
        pass = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault(
          "entityManagerMethodsRuntimeExceptionsCauseRollback18Test failed");
    }
  }

  /*
   * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback19Test
   * 
   * @assertion_ids: PERSISTENCE:SPEC:611;
   * 
   * @test_Strategy: Call EntityManager.getDelegate() that causes
   * RuntimeException and verify Transaction is set for rollback
   */
  public void entityManagerMethodsRuntimeExceptionsCauseRollback19Test()
      throws Fault {
    boolean pass = false;
    getEntityTransaction().begin();
    getEntityManager().close();
    try {

      getEntityManager().getDelegate();
      TestUtil.logErr("RuntimeException not thrown");
    } catch (RuntimeException e) {
      TestUtil.logTrace("RuntimeException Caught as Expected.");
      if (!getEntityTransaction().getRollbackOnly()) {
        TestUtil.logErr("Transaction was not marked for rollback");
      } else {
        TestUtil.logTrace("Transaction was marked for rollback");
        pass = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault(
          "entityManagerMethodsRuntimeExceptionsCauseRollback19Test failed");
    }
  }

  /*
   * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback20Test
   * 
   * @assertion_ids: PERSISTENCE:SPEC:611;
   * 
   * @test_Strategy: Call EntityManager.getEntityManagerFactory() that causes
   * RuntimeException and verify Transaction is set for rollback
   */
  public void entityManagerMethodsRuntimeExceptionsCauseRollback20Test()
      throws Fault {
    boolean pass = false;
    getEntityTransaction().begin();
    getEntityManager().close();
    try {

      getEntityManager().getEntityManagerFactory();
      TestUtil.logErr("RuntimeException not thrown");
    } catch (RuntimeException e) {
      TestUtil.logTrace("RuntimeException Caught as Expected.");
      if (!getEntityTransaction().getRollbackOnly()) {
        TestUtil.logErr("Transaction was not marked for rollback");
      } else {
        TestUtil.logTrace("Transaction was marked for rollback");
        pass = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault(
          "entityManagerMethodsRuntimeExceptionsCauseRollback20Test failed");
    }
  }

  /*
   * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback22Test
   * 
   * @assertion_ids: PERSISTENCE:SPEC:611;
   * 
   * @test_Strategy: Call EntityManager.getMetamodel() that causes
   * RuntimeException and verify Transaction is set for rollback
   */
  public void entityManagerMethodsRuntimeExceptionsCauseRollback22Test()
      throws Fault {
    boolean pass = false;
    getEntityTransaction().begin();
    getEntityManager().close();
    try {
      getEntityManager().getMetamodel();
      TestUtil.logErr("RuntimeException not thrown");
    } catch (RuntimeException e) {
      TestUtil.logTrace("RuntimeException Caught as Expected.");
      if (!getEntityTransaction().getRollbackOnly()) {
        TestUtil.logErr("Transaction was not marked for rollback");
      } else {
        TestUtil.logTrace("Transaction was marked for rollback");
        pass = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault(
          "entityManagerMethodsRuntimeExceptionsCauseRollback22Test failed");
    }
  }

  /*
   * @testName: storedProcedureQueryMethodsAfterClose1Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36
   *
   * @test_Strategy:
   *
   */
  public void storedProcedureQueryMethodsAfterClose1Test() throws Fault {
    boolean pass = false;

    getEntityManager().close();
    try {
      getEntityManager()
          .createNamedStoredProcedureQuery("get-id-firstname-lastname");
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    if (!pass) {
      throw new Fault("storedProcedureQueryMethodsAfterClose1Test failed");
    }

  }

  /*
   * @testName: storedProcedureQueryMethodsAfterClose2Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36
   *
   * @test_Strategy:
   *
   */
  public void storedProcedureQueryMethodsAfterClose2Test() throws Fault {
    boolean pass = false;

    getEntityManager().close();
    try {
      getEntityManager().createStoredProcedureQuery("GetEmpIdFNameLNameFromRS");
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    if (!pass) {
      throw new Fault("storedProcedureQueryMethodsAfterClose2Test failed");
    }

  }

  /*
   * @testName: storedProcedureQueryMethodsAfterClose3Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36
   *
   * @test_Strategy:
   *
   */
  public void storedProcedureQueryMethodsAfterClose3Test() throws Fault {
    boolean pass = false;

    getEntityManager().close();
    try {
      getEntityManager().createStoredProcedureQuery("GetEmpASCFromRS",
          Employee.class);

      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    if (!pass) {
      throw new Fault("storedProcedureQueryMethodsAfterClose3Test failed");
    }

  }

  /*
   * @testName: storedProcedureQueryMethodsAfterClose4Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:36
   *
   * @test_Strategy:
   *
   */
  public void storedProcedureQueryMethodsAfterClose4Test() throws Fault {
    boolean pass = false;

    getEntityManager().close();
    try {
      getEntityManager().createStoredProcedureQuery("GetEmpIdFNameLNameFromRS",
          "id-firstname-lastname");

      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    if (!pass) {
      throw new Fault("storedProcedureQueryMethodsAfterClose4Test failed");
    }

  }

  private void createOrderData() {

    try {
      getEntityTransaction().begin();
      TestUtil.logMsg("Creating Orders");
      orders[0] = new Order(1, 111, "desc1");
      orders[1] = new Order(2, 222, "desc2");
      orders[2] = new Order(3, 333, "desc3");
      orders[3] = new Order(4, 444, "desc4");
      orders[4] = new Order(5, 555, "desc5");
      for (Order o : orders) {
        TestUtil.logTrace("Persisting order:" + o.toString());
        getEntityManager().persist(o);
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
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }
  }

  private void removeTestData() {
    TestUtil.logTrace("removeTestData");
    if (getEntityTransaction().isActive()) {
      getEntityTransaction().rollback();
    }
    try {
      getEntityTransaction().begin();
      getEntityManager().createNativeQuery("DELETE FROM EMPLOYEE")
          .executeUpdate();
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
