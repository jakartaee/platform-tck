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

package com.sun.ts.tests.jpa.se.entityManagerFactory;

import java.util.Properties;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;

public class ClientIT extends PMClientBase {

  Properties props = null;

  public ClientIT() {
  }

  @Deployment(testable = false, managed = false)
  public static JavaArchive createDeployment() throws Exception {
     
     String pkgNameWithoutSuffix = ClientIT.class.getPackageName();
     String pkgName = ClientIT.class.getPackageName() + ".";
     String[] classes = { pkgName + "Member", pkgName + "Member_", pkgName + "Order", pkgName + "Order_"};
     return createDeploymentJar("jpa_jpa22_se_entityManagerFactory.jar", pkgNameWithoutSuffix, (String[]) classes);

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

  @AfterAll
  public void cleanupNoData() throws Exception {
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
  @Test
  public void getMetamodelIllegalStateExceptionTest() throws Exception {
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
      throw new Exception("getMetamodelIllegalStateExceptionTest failed");
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
  @Test
  public void createEntityManagerFactoryNoBeanValidatorTest() throws Exception {
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
      throw new Exception("createEntityManagerFactoryNoBeanValidatorTest failed");
    }
  }

  /*
   * @testName: createEntityManagerFactoryStringMapTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1480;
   * 
   * @test_Strategy: Create an EntityManagerFactory via String,Map
   */
  @Test
  public void createEntityManagerFactoryStringMapTest() throws Exception {
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
      throw new Exception("createEntityManagerFactoryStringTest failed");
    }
  }


}
