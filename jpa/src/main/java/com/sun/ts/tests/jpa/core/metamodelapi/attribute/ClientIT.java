/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.core.metamodelapi.attribute;

import org.jboss.arquillian.junit5.ArquillianExtension;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.ManagedType;
import jakarta.persistence.metamodel.Metamodel;

@ExtendWith(ArquillianExtension.class)
@TestInstance(Lifecycle.PER_CLASS)

public class ClientIT extends PMClientBase {

  public ClientIT() {
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
   * @testName: getName
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1216
   *
   * @test_Strategy:
   *
   */
@Test
  public void getName() throws Exception {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      ManagedType<Order> mTypeOrder = metaModel.managedType(
          com.sun.ts.tests.jpa.core.metamodelapi.attribute.Order.class);
      if (mTypeOrder != null) {
        TestUtil.logTrace("Obtained Non-null ManagedType");
        Attribute<Order, ?> attrib = mTypeOrder.getDeclaredAttribute("total");
        if (attrib != null) {
          TestUtil.logTrace("attribute Name = " + attrib.getName());
          if (attrib.getName() != null) {

            if (attrib.getName().equals("total")) {
              TestUtil.logTrace("Received expected result:" + attrib.getName());
              pass = true;
            } else {
              TestUtil.logErr("Expected: "
                  + Attribute.PersistentAttributeType.BASIC.toString()
                  + ", actual:" + attrib.getName());
            }
          }
        }
      }
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("getName Test  failed");
    }
  }

  /*
   * @testName: getPersistentAttributeType
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1217
   *
   * @test_Strategy:
   *
   */
@Test
  public void getPersistentAttributeType() throws Exception {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      ManagedType<Order> mTypeOrder = metaModel.managedType(
          com.sun.ts.tests.jpa.core.metamodelapi.attribute.Order.class);
      if (mTypeOrder != null) {
        TestUtil.logTrace("Obtained Non-null ManagedType");
        Attribute<Order, ?> attrib = mTypeOrder.getDeclaredAttribute("total");
        if (attrib != null) {
          TestUtil.logTrace("attribute Name = " + attrib.getName());
          Attribute.PersistentAttributeType pAttribType = attrib
              .getPersistentAttributeType();
          if (pAttribType == Attribute.PersistentAttributeType.BASIC) {
            TestUtil.logTrace("Received expected result:" + pAttribType);
            pass = true;

          } else {
            TestUtil.logErr("Expected: "
                + Attribute.PersistentAttributeType.BASIC.toString()
                + ", actual:" + pAttribType);
          }
        }
      }
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("getPersistentAttributeType Test  failed");
    }
  }

  /*
   * @testName: getDeclaringType
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1213
   *
   * @test_Strategy:
   *
   */
@Test
  public void getDeclaringType() throws Exception {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      ManagedType<Order> mTypeOrder = metaModel.managedType(
          com.sun.ts.tests.jpa.core.metamodelapi.attribute.Order.class);
      if (mTypeOrder != null) {
        TestUtil.logTrace("Obtained Non-null ManagedType");
        Attribute<Order, ?> attrib = mTypeOrder.getDeclaredAttribute("total");
        if (attrib != null) {
          TestUtil.logTrace("attribute Name = " + attrib.getName());
          ManagedType<Order> newTypeOrder = attrib.getDeclaringType();
          if (newTypeOrder != null) {
            Class javaType = newTypeOrder.getJavaType();
            if (javaType.getName().equals(
                "com.sun.ts.tests.jpa.core.metamodelapi.attribute.Order")) {
              TestUtil
                  .logTrace("Received expected result:" + javaType.getName());
              pass = true;
            } else {
              TestUtil.logErr(
                  "Expected: com.sun.ts.tests.jpa.core.metamodelapi.attribute.Order, actual:"
                      + javaType.getName());
            }
          }
        }
      }
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("getDeclaringType Test  failed");
    }
  }

  /*
   * @testName: getJavaType
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1215
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
      ManagedType<Order> mTypeOrder = metaModel.managedType(
          com.sun.ts.tests.jpa.core.metamodelapi.attribute.Order.class);
      if (mTypeOrder != null) {
        TestUtil.logTrace("Obtained Non-null ManagedType");
        Attribute<Order, ?> attrib = mTypeOrder.getDeclaredAttribute("total");
        if (attrib != null) {
          TestUtil.logTrace("attribute JavaType = " + attrib.getJavaType());
          Class pAttribJavaType = attrib.getJavaType();
          if (pAttribJavaType.getName().equals("int")) {
            TestUtil.logTrace("Received expected result:" + pAttribJavaType);
            pass = true;
          } else {
            TestUtil.logErr("Expected: int, actual:" + pAttribJavaType);
          }
        }
      }
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("getJavaType Test  failed");
    }
  }

  /*
   * @testName: getJavaMember
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1214
   *
   * @test_Strategy:
   *
   */
@Test
  public void getJavaMember() throws Exception {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      ManagedType<Order> mTypeOrder = metaModel.managedType(
          com.sun.ts.tests.jpa.core.metamodelapi.attribute.Order.class);
      if (mTypeOrder != null) {
        TestUtil.logTrace("Obtained Non-null ManagedType");
        Attribute<Order, ?> attrib = mTypeOrder.getDeclaredAttribute("total");
        if (attrib != null) {
          TestUtil.logTrace(
              "attribute JavaMember = " + attrib.getJavaMember().getName());
          java.lang.reflect.Member javaMember = attrib.getJavaMember();
          if (javaMember.getName().equals("getTotal")) {
            TestUtil
                .logTrace("Received expected result:" + javaMember.getName());
            pass = true;
          } else {
            TestUtil
                .logErr("Expected: getTotal, actual:" + javaMember.getName());
          }
        }
      }
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("getJavaMember Test  failed");
    }
  }

  /*
   * @testName: isAssociation
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1218
   *
   * @test_Strategy:
   *
   */
@Test
  public void isAssociation() throws Exception {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      ManagedType<Order> mTypeOrder = metaModel.managedType(
          com.sun.ts.tests.jpa.core.metamodelapi.attribute.Order.class);
      if (mTypeOrder != null) {
        TestUtil.logTrace("Obtained Non-null ManagedType");
        Attribute<Order, ?> attrib = mTypeOrder.getDeclaredAttribute("total");
        if (attrib != null) {
          TestUtil
              .logTrace("attribute IsAssociation = " + attrib.isAssociation());
          if (!attrib.isAssociation()) {
            TestUtil
                .logTrace("Received expected result:" + attrib.isAssociation());
            pass = true;
          } else {
            TestUtil.logErr(
                "Received unexpected result: " + attrib.isAssociation());
          }
        }
      }
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("isAssociation Test  failed");
    }
  }

  /*
   * @testName: isCollection
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1219
   *
   * @test_Strategy:
   *
   */
@Test
  public void isCollection() throws Exception {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      ManagedType<Order> mTypeOrder = metaModel.managedType(
          com.sun.ts.tests.jpa.core.metamodelapi.attribute.Order.class);
      if (mTypeOrder != null) {
        TestUtil.logTrace("Obtained Non-null ManagedType");
        Attribute<Order, ?> attrib = mTypeOrder.getDeclaredAttribute("total");
        if (attrib != null) {
          TestUtil
              .logTrace("attribute IsCollection = " + attrib.isCollection());
          if (!attrib.isCollection()) {
            TestUtil
                .logTrace("Received expected result:" + attrib.isCollection());
            pass = true;
          } else {
            TestUtil
                .logErr("Received unexpected result: " + attrib.isCollection());
          }
        }
      }
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("isCollection Test  failed");
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
