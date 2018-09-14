/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;
import java.util.Properties;

public class Client extends PMClientBase {

  public Client() {
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup");
    try {
      super.setup(args, p);
      removeTestData();
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
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
  public void getName() throws Fault {
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
      throw new Fault("getName Test  failed");
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
  public void getPersistentAttributeType() throws Fault {
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
      throw new Fault("getPersistentAttributeType Test  failed");
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
  public void getDeclaringType() throws Fault {
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
      throw new Fault("getDeclaringType Test  failed");
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
  public void getJavaType() throws Fault {
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
      throw new Fault("getJavaType Test  failed");
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
  public void getJavaMember() throws Fault {
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
      throw new Fault("getJavaMember Test  failed");
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
  public void isAssociation() throws Fault {
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
      throw new Fault("isAssociation Test  failed");
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
  public void isCollection() throws Fault {
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
      throw new Fault("isCollection Test  failed");
    }
  }

  public void cleanup() throws Fault {
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
