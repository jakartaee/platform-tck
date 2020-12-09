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

package com.sun.ts.tests.jpa.se.entityManagerFactory;

import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.CleanupMethod;
import com.sun.ts.lib.harness.SetupMethod;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;

public class Client extends PMClientBase {

  Properties props = null;

  public Client() {
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setupNoData(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setupNoData");
    this.props = p;
    try {
      super.setup(args, p);
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup");
    this.props = p;
    try {
      super.setup(args, p);
      removeTestData();
      createOrderTestData();
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  public void setupMember(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup");
    this.props = p;
    try {
      super.setup(args, p);
      removeTestData();
      createMemberTestData();
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  public void cleanupNoData() throws Fault {
    super.cleanup();
  }

  public void cleanup() throws Fault {
    removeTestData();
    TestUtil.logTrace("done cleanup, calling super.cleanup");
    super.cleanup();
  }

  /*
   * @testName: getMetamodelIllegalStateExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:536;
   * 
   * @test_Strategy: Close the EntityManagerFactory, then call
   * emf.getMetaModel()
   */
  @SetupMethod(name = "setupNoData")
  @CleanupMethod(name = "cleanupNoData")
  public void getMetamodelIllegalStateExceptionTest() throws Fault {
    boolean pass = false;
    try {
      EntityManagerFactory emf = getEntityManager().getEntityManagerFactory();
      emf.close();
      try {
        emf.getMetamodel();
        TestUtil.logErr("IllegalStateException not thrown");
      } catch (IllegalStateException ise) {
        TestUtil.logTrace("Received expected IllegalStateException");
        pass = true;
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    if (!pass) {
      throw new Fault("getMetamodelIllegalStateExceptionTest failed");
    }
  }

  /*
   * @testName: createEntityManagerFactoryNoBeanValidatorTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1291; PERSISTENCE:SPEC:1914;
   * 
   * @test_Strategy: Instantiate createEntityManagerFactory when there is no
   * Bean Validation provider present in the environment
   */
  @SetupMethod(name = "setupNoData")
  @CleanupMethod(name = "cleanupNoSuper")
  public void createEntityManagerFactoryNoBeanValidatorTest() throws Fault {
    boolean pass = false;
    myProps.put("jakarta.persistence.validation.mode", "callback");
    displayMap(myProps);
    try {
      EntityManagerFactory emf = Persistence
          .createEntityManagerFactory(getPersistenceUnitName(), myProps);
      EntityManager em = emf.createEntityManager();
      em.getTransaction().begin();
      em.persist(new Order(1, 111));
      TestUtil.logErr("Did not receive expected PersistenceException");
    } catch (PersistenceException pe) {
      TestUtil.logTrace("Received expected PersistenceException");
      pass = true;
    } catch (Exception ex) {
      TestUtil.logErr("Received unexpected Exception", ex);
    }
    if (!pass) {
      throw new Fault("createEntityManagerFactoryNoBeanValidatorTest failed");
    }
  }

  /*
   * @testName: createEntityManagerFactoryStringMapTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1480;
   * 
   * @test_Strategy: Create an EntityManagerFactory via String,Map
   */
  @SetupMethod(name = "setupNoData")
  @CleanupMethod(name = "cleanupNoSuper")
  public void createEntityManagerFactoryStringMapTest() throws Fault {
    boolean pass = false;

    try {
      EntityManagerFactory emf = Persistence.createEntityManagerFactory(
          getPersistenceUnitName(), getPersistenceUnitProperties());
      if (emf != null) {
        TestUtil.logTrace("Received non-null EntityManagerFactory");
        pass = true;
      } else {
        TestUtil.logErr("Received null EntityManagerFactory");
      }
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass) {
      throw new Fault("createEntityManagerFactoryStringTest failed");
    }
  }

  private void createOrderTestData() {

    try {
      getEntityTransaction().begin();
      Order[] orders = new Order[5];
      orders[0] = new Order(1, 111);
      orders[1] = new Order(2, 222);
      orders[2] = new Order(3, 333);
      orders[3] = new Order(4, 444);
      orders[4] = new Order(5, 555);

      for (Order o : orders) {
        TestUtil.logTrace("Persisting order:" + o.toString());
        getEntityManager().persist(o);
      }
      getEntityManager().flush();
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

  private void createMemberTestData() {

    try {
      getEntityTransaction().begin();

      Member[] members = new Member[5];
      members[0] = new Member(1, "1");
      members[1] = new Member(2, "2");
      members[2] = new Member(3, "3");
      members[3] = new Member(4, "4");
      members[4] = new Member(5, "5");

      for (Member m : members) {
        TestUtil.logTrace("Persisting member:" + m.toString());
        getEntityManager().persist(m);
      }
      getEntityManager().flush();

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
      clearCache();
      getEntityManager().createNativeQuery("DELETE FROM PURCHASE_ORDER")
          .executeUpdate();
      getEntityManager().createNativeQuery("DELETE FROM MEMBER")
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
