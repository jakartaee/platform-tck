/*
 * Copyright (c) 2009, 2023 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.core.metamodelapi.type;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import jakarta.persistence.metamodel.EmbeddableType;
import jakarta.persistence.metamodel.ManagedType;
import jakarta.persistence.metamodel.Metamodel;
import jakarta.persistence.metamodel.Type;


@ExtendWith(ArquillianExtension.class)
@TestInstance(Lifecycle.PER_CLASS)

public class ClientIT extends PMClientBase {

  public ClientIT() {
  }
  
  @Deployment(testable = false, managed = false)
  public static JavaArchive createDeployment() throws Exception {
     
     String pkgNameWithoutSuffix = ClientIT.class.getPackageName();
     String pkgName = ClientIT.class.getPackageName() + ".";
     String[] classes = {     pkgName + "Address", pkgName + "B", pkgName + "Order", pkgName + "ZipCode"};
     return createDeploymentJar("jpa_core_metamodelapi_type.jar", pkgNameWithoutSuffix, classes);

  }


@BeforeAll
  public void setup() throws Exception {
    TestUtil.logTrace("setup");
    try {
      super.setup();
      removeTestData();
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Exception("Setup failed:", e);
    }
  }

  /*
   * @testName: getPersistenceType
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1472
   *
   * @test_Strategy:
   *
   */
@Test
  public void getPersistenceType() throws Exception {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      ManagedType<Order> mTypeOrder = metaModel
          .managedType(com.sun.ts.tests.jpa.core.metamodelapi.type.Order.class);
      if (mTypeOrder != null) {
        Type.PersistenceType type = mTypeOrder.getPersistenceType();
        TestUtil.logTrace("Obtained Non-null ManagedType");
        if (type.equals(Type.PersistenceType.ENTITY)) {
          pass = true;
        } else {
          TestUtil.logTrace("Persistence type = " + type.name());
        }
      }
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("getPersistenceType Test  failed");
    }
  }

  /*
   * @testName: getEmbeddablePersistenceType
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1472
   *
   * @test_Strategy:
   *
   */
@Test
  public void getEmbeddablePersistenceType() throws Exception {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      EmbeddableType<Address> eTypeAddress = metaModel.embeddable(
          com.sun.ts.tests.jpa.core.metamodelapi.type.Address.class);
      if (eTypeAddress != null) {
        Type.PersistenceType type = eTypeAddress.getPersistenceType();
        TestUtil.logTrace("Obtained Non-null Embeddable Type");
        if (type.equals(Type.PersistenceType.EMBEDDABLE)) {
          pass = true;
        } else {
          TestUtil.logTrace("Persistence type = " + type);
        }
      }
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("getEmbeddablePersistenceType Test  failed");
    }
  }

  /*
   * @testName: getJavaType
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1471
   *
   * @test_Strategy:
   *
   */
@Test
  public void getJavaType() throws Exception {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      ManagedType<Order> mTypeOrder = metaModel
          .managedType(com.sun.ts.tests.jpa.core.metamodelapi.type.Order.class);
      if (mTypeOrder != null) {
        Class javaType = mTypeOrder.getJavaType();
        TestUtil.logTrace("Obtained Non-null ManagedType");
        if (javaType.getName()
            .equals("com.sun.ts.tests.jpa.core.metamodelapi.type.Order")) {
          pass = true;
        } else {
          TestUtil.logTrace("javaType name = " + javaType.getName());
        }
      }
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("getJavaType Test  failed");
    }
  }

@AfterAll
  public void cleanup() throws Exception {
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
  }
}
