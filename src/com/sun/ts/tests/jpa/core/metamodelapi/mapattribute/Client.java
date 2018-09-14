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

package com.sun.ts.tests.jpa.core.metamodelapi.mapattribute;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import javax.persistence.metamodel.*;
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
   * @testName: getJavaKeyType
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1415
   *
   * @test_Strategy:
   *
   */
  public void getJavaKeyType() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      ManagedType<Department> mType = metaModel.managedType(Department.class);
      if (mType != null) {
        TestUtil.logTrace("Obtained Non-null ManagedType");
        MapAttribute<Department, String, Employee> mapAttrib = mType
            .getDeclaredMap("lastNameEmployees", java.lang.String.class,
                com.sun.ts.tests.jpa.core.metamodelapi.mapattribute.Employee.class);
        Class javaKeyType = mapAttrib.getKeyJavaType();

        if (javaKeyType.getName().equals("java.lang.String")) {
          TestUtil
              .logTrace("Received Expected Map Attribute's Java Key Type  = "
                  + javaKeyType.getName());
          pass = true;

        } else {
          TestUtil
              .logTrace("Received UnExpected Map Attribute's Java Key Type  = "
                  + javaKeyType.getName());
        }
      }
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getJavaKeyType Test  failed");
    }
  }

  /*
   * @testName: getKeyType
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1416
   *
   * @test_Strategy:
   *
   */
  public void getKeyType() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      ManagedType<Department> mType = metaModel.managedType(Department.class);
      if (mType != null) {
        TestUtil.logTrace("Obtained Non-null ManagedType");
        MapAttribute<Department, String, Employee> mapAttrib = mType
            .getDeclaredMap("lastNameEmployees", java.lang.String.class,
                com.sun.ts.tests.jpa.core.metamodelapi.mapattribute.Employee.class);
        Type keyType = mapAttrib.getKeyType();
        String javaKeyTypeName = keyType.getJavaType().getName();

        if (javaKeyTypeName.equals("java.lang.String")) {
          TestUtil
              .logTrace("Received Expected Map Attribute's Java Key Type  = "
                  + javaKeyTypeName);
          pass = true;

        } else {
          TestUtil
              .logTrace("Received UnExpected Map Attribute's Java Key Type  = "
                  + javaKeyTypeName);
        }
      }
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getKeyType Test  failed");
    }
  }

  /*
   * @testName: getCollectionType
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1417;
   *
   * @test_Strategy:
   *
   */
  public void getCollectionType() throws Fault {
    boolean pass = false;

    String expected = PluralAttribute.CollectionType.MAP.name();

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      ManagedType<Department> mType = metaModel.managedType(Department.class);
      if (mType != null) {
        TestUtil.logTrace("Obtained Non-null ManagedType");
        MapAttribute<Department, String, Employee> mapAttrib = mType
            .getDeclaredMap("lastNameEmployees", java.lang.String.class,
                com.sun.ts.tests.jpa.core.metamodelapi.mapattribute.Employee.class);
        PluralAttribute.CollectionType type = mapAttrib.getCollectionType();
        if (type != null) {
          String name = type.name();

          if (name.equals(expected)) {
            TestUtil.logTrace("Received expected result: " + name);
            pass = true;

          } else {
            TestUtil.logTrace("Expected: " + expected + ", actual: " + name);
          }
        } else {
          TestUtil.logErr("getCollectionType() returned null");
        }
      } else {
        TestUtil.logErr("managedType() returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getCollectionType Test  failed");
    }
  }

  /*
   * @testName: getElementType
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1418;
   *
   * @test_Strategy:
   *
   */
  public void getElementType() throws Fault {
    boolean pass = false;

    String expected = "com.sun.ts.tests.jpa.core.metamodelapi.mapattribute.Employee";

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      ManagedType<Department> mType = metaModel.managedType(Department.class);
      if (mType != null) {
        TestUtil.logTrace("Obtained Non-null ManagedType");
        MapAttribute<Department, String, Employee> mapAttrib = mType
            .getDeclaredMap("lastNameEmployees", java.lang.String.class,
                com.sun.ts.tests.jpa.core.metamodelapi.mapattribute.Employee.class);
        Type type = mapAttrib.getElementType();
        if (type != null) {
          String name = type.getJavaType().getName();

          if (name.equals(expected)) {
            TestUtil.logTrace("Received expected result: " + name);
            pass = true;

          } else {
            TestUtil.logTrace("Expected: " + expected + ", actual: " + name);
          }
        } else {
          TestUtil.logErr("getElementType() returned null");
        }
      } else {
        TestUtil.logErr("managedType() returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getElementType Test  failed");
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
