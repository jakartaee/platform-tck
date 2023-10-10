/*
 * Copyright (c) 2018, 2023 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.core.override.attributeoverride;

import java.util.List;

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


@ExtendWith(ArquillianExtension.class)
@TestInstance(Lifecycle.PER_CLASS)

public class ClientIT extends PMClientBase {

  private static final int ENTITY_ID = 3039;

  private static final String NAME = "Cheese";

  private static final String PUBLISHER = "Johnson";

  private static final int COST = 20;

  public ClientIT() {
  }
  
  @Deployment(testable = false, managed = false)
  public static JavaArchive createDeployment() throws Exception {
     
     String pkgNameWithoutSuffix = ClientIT.class.getPackageName();
     String pkgName = ClientIT.class.getPackageName() + ".";
     String[] classes = {     pkgName + "Book", pkgName + "LawBook"};
     return createDeploymentJar("jpa_core_override_attributeoverride.jar", pkgNameWithoutSuffix, classes);

  }


@BeforeAll
  public void setup() throws Exception {
    TestUtil.logTrace("setup");
    try {
      super.setup();
      removeTestData();
    } catch (Exception e) {
      TestUtil.logErr("Exception:test failed ", e);
    }
  }

  /*
   * @testName: testNoAttributeOverrideAnnotation
   * 
   * @assertion_ids: PERSISTENCE:SPEC:594; PERSISTENCE:SPEC:596;
   * PERSISTENCE:SPEC:597; PERSISTENCE:SPEC:598; PERSISTENCE:SPEC:599;
   * PERSISTENCE:SPEC:600; PERSISTENCE:SPEC:601;
   * 
   * @test_Strategy: LawBook is an entity which extends a class Book. A column
   * "name" is overriden in Orm.xml as "BOOK_NAME". The following test tests for
   * the same.
   */
@Test
  public void testNoAttributeOverrideAnnotation() throws Exception {

    LawBook book = new LawBook();
    getEntityTransaction().begin();
    book.setCategory("Motivational");
    book.setId(ENTITY_ID);
    book.setName(NAME);
    book.setPublisher(PUBLISHER);
    book.setCost(COST);
    getEntityManager().persist(book);
    getEntityManager().flush();
    try {
      List result = getEntityManager()
          .createQuery("SELECT b FROM LawBook b where b.name= " + ":name")
          .setParameter("name", NAME).getResultList();
      if (result.size() == 1) {
        TestUtil.logTrace("test Overriding Attributes passed");
      } else {
        throw new Exception(
            "Expected the size to be 1 " + " but it is -" + result.size());
      }
    } catch (Exception e) {
      throw new Exception(
          "Exception thrown while testing testNoAttributeOverrideAnnotation"
              + e);
    } finally {
      getEntityManager().remove(book);
      getEntityTransaction().commit();
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
    try {
      getEntityTransaction().begin();
      getEntityManager().createNativeQuery("DELETE FROM LAWBOOK")
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
