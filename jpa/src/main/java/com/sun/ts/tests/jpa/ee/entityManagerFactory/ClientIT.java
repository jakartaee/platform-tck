/*
 * Copyright (c) 2007, 2023 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.ee.entityManagerFactory;

import java.util.Properties;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;
import com.sun.ts.tests.jpa.core.entityManagerFactory.Order;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class ClientIT extends PMClientBase {

  Properties props = null;

  public ClientIT() {
  }
  
  @Deployment(testable = false, managed = false)
  public static JavaArchive createDeployment() throws Exception {
     
     String pkgNameWithoutSuffix = ClientIT.class.getPackageName();
     String pkgName = ClientIT.class.getPackageName() + ".";
     String[] classes = { pkgName + "Order"};
     return createDeploymentJar("jpa_ee_entityManagerFactory.jar", pkgNameWithoutSuffix, (String[]) classes);

  }


  @BeforeAll
  public void setupNoData() throws Exception {
    TestUtil.logTrace("setupNoData");
    try {
      super.setup();
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Exception("Setup failed:", e);
    }
  }

  public void setup() throws Exception {
    TestUtil.logTrace("setup");
    try {
      super.setup();
      removeTestData();
      createOrderTestData();
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Exception("Setup failed:", e);
    }
  }

  @AfterAll
  public void cleanupNoData() throws Exception {
    super.cleanup();
  }

  public void cleanup() throws Exception {
    removeTestData();
    TestUtil.logTrace("done cleanup, calling super.cleanup");
    super.cleanup();
  }

  /*
   * 
   * /*
   * 
   * @testName: createEntityManagerFactoryStringTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:146;
   * 
   * @test_Strategy: Create an EntityManagerFactory via String
   */
  @Test
  public void createEntityManagerFactoryStringTest() throws Exception {
    boolean pass = false;

    try {
      EntityManagerFactory emf = Persistence
          .createEntityManagerFactory(getPersistenceUnitName());
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
      throw new Exception("createEntityManagerFactoryStringTest failed");
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
