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

package com.sun.ts.tests.jpa.core.entityManagerFactory;

import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceUnitUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.metamodel.Metamodel;

public class Client2IT extends PMClientBase {

  Properties props = null;

  public Client2IT() {
  }


  @BeforeEach
  public void setupNoData() throws Exception {
    TestUtil.logTrace("setupNoData");
    try {
      super.setup();
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Exception("Setup failed:", e);
    }
  }

  @AfterEach
  public void cleanupNoData() throws Exception {
    super.cleanup();
  }


  /*
   * @testName: autoCloseableTest
   *
   * @assertion_ids: PERSISTENCE:SPEC:2517;
   *
   * @test_Strategy: Create EntityManagerFactory in try with resources block
   * and verify whether it's open inside and outside of the try block.
   */
  @Test
  public void autoCloseableTest() throws Exception {
    EntityManagerFactory emf = null;
    try (final EntityManagerFactory emfLocal
                 = Persistence.createEntityManagerFactory(getPersistenceUnitName(), getPersistenceUnitProperties())) {
      emf = emfLocal;
      if (emf == null) {
        throw new Exception("autoCloseableTest failed: createEntityManagerFactory(String) returned null");
      }
      if (!emf.isOpen()) {
        throw new Exception("autoCloseableTest failed: EntityManagerFactory isOpen() returned false in try block");
      }
    } catch (Exception f) {
      throw f;
    } catch (Throwable t) {
      throw new Exception("autoCloseableTest failed with Exception", t);
    } finally {
      if (emf != null && emf.isOpen()) {
        throw new Exception("autoCloseableTest failed: EntityManagerFactory isOpen() returned true outside try block");
      }
    }
  }

  /*
   * @testName: getMetamodelTest
   *
   * @assertion_ids: PERSISTENCE:JAVADOC:340;
   *
   * @test_Strategy: Get a MetaModel Object from the EntityManagerFactory and
   * make sure it is not null
   */
  @Test
  public void getMetamodelTest() throws Exception {
    boolean pass = false;
    try {
      Metamodel mm = getEntityManager().getEntityManagerFactory()
          .getMetamodel();
      if (mm == null) {
        TestUtil.logErr("getMetamodel() returned a null result");
      } else {
        pass = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    if (!pass) {
      throw new Exception("getMetamodelTest failed");
    }
  }

  /*
   * @testName: getPersistenceUnitUtil
   *
   * @assertion_ids: PERSISTENCE:JAVADOC:341;
   *
   * @test_Strategy: Get a PersistenceUnitUtil Object from the
   * EntityManagerFactory an make sure it is not null
   */
  @Test
  public void getPersistenceUnitUtil() throws Exception {
    boolean pass = false;
    try {
      PersistenceUnitUtil puu = getEntityManager().getEntityManagerFactory()
          .getPersistenceUnitUtil();
      if (puu == null) {
        TestUtil.logErr("getPersistenceUnitUtil() returned a null result");
      } else {
        pass = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    if (!pass) {
      throw new Exception("getPersistenceUnitUtil failed");
    }
  }

  /*
   * @testName: getCriteriaBuilderTest
   *
   * @assertion_ids: PERSISTENCE:JAVADOC:339; PERSISTENCE:SPEC:1702;
   *
   * @test_Strategy: access EntityManagerFactory.getCriteriaBuilder and verify
   * it can be used to create a query
   *
   */
  @Test
  public void getCriteriaBuilderTest() throws Exception {
    boolean pass = false;
    try {
      CriteriaBuilder cbuilder = getEntityManager().getEntityManagerFactory()
          .getCriteriaBuilder();
      if (cbuilder != null) {
        getEntityTransaction().begin();
        CriteriaQuery<Object> cquery = cbuilder.createQuery();
        if (cquery != null) {
          TestUtil.logTrace("Obtained Non-null Criteria Query");
          pass = true;
        } else {
          TestUtil.logErr("Failed to get Non-null Criteria Query");
        }

        getEntityTransaction().commit();
      } else {
        TestUtil.logErr("getCriteriaBuilder() returned null");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    if (!pass) {
      throw new Exception("getCriteriaBuilderTest failed");
    }
  }
}
