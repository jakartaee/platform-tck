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

package com.sun.ts.tests.jpa.core.metamodelapi.bindable;

import java.util.Set;

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

import jakarta.persistence.metamodel.Bindable;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;

@ExtendWith(ArquillianExtension.class)
@TestInstance(Lifecycle.PER_CLASS)

public class ClientIT extends PMClientBase {

  public ClientIT() {
  }
  
  @Deployment(testable = false, managed = false)
	public static JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = ClientIT.class.getPackageName();
		String pkgName = ClientIT.class.getPackageName() + ".";
		String[] classes = { pkgName + "A", pkgName + "Address", 
				 pkgName + "ZipCode" };
		return createDeploymentJar("jpa_core_metamodelapi_bindable.jar", pkgNameWithoutSuffix, classes);

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
   * @testName: getBindableType
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1438; PERSISTENCE:JAVADOC:1225
   *
   * @test_Strategy:
   * 
   */
@Test
  public void getBindableType() throws Exception {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      Set<EntityType<?>> aSet = metaModel.getEntities();
      if (aSet != null) {
        TestUtil.logTrace("Obtained Non-null Set of EntityType");
        for (EntityType eType : aSet) {
          TestUtil.logTrace(
              "entity's BindableType is  = " + eType.getBindableType());
          if (eType.getBindableType()
              .equals(Bindable.BindableType.ENTITY_TYPE)) {
            TestUtil.logTrace("as Expected BindableType is ENTITY_TYPE");
            pass = true;
          } else {
            TestUtil.logTrace("bindableType is non ENTITY_TYPE");
          }
        }
      }
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("getBindableType failed");
    }
  }

  /*
   * @testName: getBindableJavaType
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1224
   *
   * @test_Strategy:
   *
   */
@Test
  public void getBindableJavaType() throws Exception {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      Set<EntityType<?>> aSet = metaModel.getEntities();
      if (aSet != null) {
        TestUtil.logTrace("Obtained Non-null Set of EntityType");
        for (EntityType eType : aSet) {
          TestUtil.logTrace("entity's BindableJavaType is  = "
              + eType.getBindableJavaType().getName());
          String bindableJavaType = eType.getBindableJavaType().getName();

          if (bindableJavaType != null) {
            if (bindableJavaType
                .equals("com.sun.ts.tests.jpa.core.metamodelapi.bindable.A")) {
              TestUtil.logTrace(
                  "as Expected BindableJavaType for A is " + bindableJavaType);
              pass = true;
            } else {
              TestUtil.logTrace("bindableJavaType is incorrect");
            }
          }
        }
      }
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("getBindableJavaType failed");
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
