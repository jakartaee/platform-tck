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

package com.sun.ts.tests.jpa.core.metamodelapi.singularattribute;

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
   * @testName: isId
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1458
   *
   * @test_Strategy:
   *
   */
  public void isId() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      ManagedType<B> mTypeB = metaModel.managedType(
          com.sun.ts.tests.jpa.core.metamodelapi.singularattribute.B.class);
      if (mTypeB != null) {
        TestUtil.logTrace("Obtained Non-null ManagedType");
        SingularAttribute<B, Address> singAttrib = mTypeB
            .getDeclaredSingularAttribute("address",
                com.sun.ts.tests.jpa.core.metamodelapi.singularattribute.Address.class);

        if (!singAttrib.isId()) {
          TestUtil.logTrace("Received expected result singular attribute isId ="
              + singAttrib.isId());
          pass = true;
        } else {
          TestUtil
              .logTrace("Received UnExpected result singular attribute isId ="
                  + singAttrib.isId());
        }
      }
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("isId Test  failed");
    }
  }

  /*
   * @testName: isVersion
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1460
   *
   * @test_Strategy:
   *
   */
  public void isVersion() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      ManagedType<B> mTypeB = metaModel.managedType(
          com.sun.ts.tests.jpa.core.metamodelapi.singularattribute.B.class);
      if (mTypeB != null) {
        TestUtil.logTrace("Obtained Non-null ManagedType");
        SingularAttribute<B, Address> singAttrib = mTypeB
            .getDeclaredSingularAttribute("address",
                com.sun.ts.tests.jpa.core.metamodelapi.singularattribute.Address.class);

        if (!singAttrib.isVersion()) {
          TestUtil.logTrace(
              "Received expected result singular attribute isVersion ="
                  + singAttrib.isVersion());
          pass = true;
        } else {
          TestUtil.logTrace(
              "Received UnExpected result singular attribute isVersion ="
                  + singAttrib.isVersion());
        }
      }
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("isVersion Test  failed");
    }
  }

  /*
   * @testName: isOptional
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1459
   *
   * @test_Strategy:
   *
   */
  public void isOptional() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      ManagedType<B> mTypeB = metaModel.managedType(
          com.sun.ts.tests.jpa.core.metamodelapi.singularattribute.B.class);
      if (mTypeB != null) {
        TestUtil.logTrace("Obtained Non-null ManagedType");
        SingularAttribute<B, Address> singAttrib = mTypeB
            .getDeclaredSingularAttribute("address",
                com.sun.ts.tests.jpa.core.metamodelapi.singularattribute.Address.class);

        if (singAttrib.isOptional()) {
          TestUtil.logTrace(
              "Received expected result singular attribute isOptional ="
                  + singAttrib.isOptional());
          pass = true;
        } else {
          TestUtil.logTrace(
              "Received UnExpected result singular attribute isOptional ="
                  + singAttrib.isOptional());
        }
      }
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("isOptional Test  failed");
    }
  }

  /*
   * @testName: getType
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1457
   *
   * @test_Strategy:
   *
   */
  public void getType() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      ManagedType<B> mTypeB = metaModel.managedType(
          com.sun.ts.tests.jpa.core.metamodelapi.singularattribute.B.class);
      if (mTypeB != null) {
        TestUtil.logTrace("Obtained Non-null ManagedType");
        SingularAttribute<B, Address> singAttrib = mTypeB
            .getDeclaredSingularAttribute("address",
                com.sun.ts.tests.jpa.core.metamodelapi.singularattribute.Address.class);

        Type attributeType = singAttrib.getType();
        String attributeJavaTypeName = attributeType.getJavaType().getName();
        if (attributeJavaTypeName.equals(
            "com.sun.ts.tests.jpa.core.metamodelapi.singularattribute.Address")) {
          TestUtil
              .logTrace("Received expected result singular attribute JavaType ="
                  + attributeJavaTypeName);
          pass = true;
        } else {
          TestUtil
              .logErr("Received Unexpected result singular attribute JavaType ="
                  + attributeJavaTypeName);
        }
      }
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getType Test  failed");
    }
  }

  /*
   * @testName: isCollection
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1467;
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
      ManagedType<B> mTypeB = metaModel.managedType(
          com.sun.ts.tests.jpa.core.metamodelapi.singularattribute.B.class);
      if (mTypeB != null) {
        TestUtil.logTrace("Obtained Non-null ManagedType");
        SingularAttribute<B, Address> singAttrib = mTypeB
            .getDeclaredSingularAttribute("address",
                com.sun.ts.tests.jpa.core.metamodelapi.singularattribute.Address.class);

        boolean b = singAttrib.isCollection();
        if (!b) {
          TestUtil.logTrace("Received expected result:" + b);
          pass = true;
        } else {
          TestUtil.logErr("Expected: false, actual: " + b);
        }
      } else {
        TestUtil.logErr("managedType() returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }
    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("isCollection Test failed");
    }
  }

  /*
   * @testName: isAssociation
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1466;
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
      ManagedType<B> mTypeB = metaModel.managedType(
          com.sun.ts.tests.jpa.core.metamodelapi.singularattribute.B.class);
      if (mTypeB != null) {
        TestUtil.logTrace("Obtained Non-null ManagedType");
        SingularAttribute<B, Address> singAttrib = mTypeB
            .getDeclaredSingularAttribute("address",
                com.sun.ts.tests.jpa.core.metamodelapi.singularattribute.Address.class);

        boolean b = singAttrib.isAssociation();
        if (!b) {
          TestUtil.logTrace("Received expected result:" + b);
          pass = true;
        } else {
          TestUtil.logErr("Expected: false, actual: " + b);
        }
      } else {
        TestUtil.logErr("managedType() returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }
    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("isAssociation Test failed");
    }
  }

  /*
   * @testName: getPersistentAttributeType
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1465;
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
      ManagedType<B> mTypeB = metaModel.managedType(
          com.sun.ts.tests.jpa.core.metamodelapi.singularattribute.B.class);
      if (mTypeB != null) {
        TestUtil.logTrace("Obtained Non-null ManagedType");
        SingularAttribute<B, Address> singAttrib = mTypeB
            .getDeclaredSingularAttribute("address",
                com.sun.ts.tests.jpa.core.metamodelapi.singularattribute.Address.class);
        if (singAttrib != null) {
          Attribute.PersistentAttributeType pAttribType = singAttrib
              .getPersistentAttributeType();
          if (pAttribType == Attribute.PersistentAttributeType.EMBEDDED) {
            TestUtil.logTrace("Received expected result " + pAttribType);
            pass = true;

          } else {
            TestUtil.logErr("Expected: "
                + Attribute.PersistentAttributeType.EMBEDDED.toString()
                + ", actual:" + pAttribType);
          }
        }
      }
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getPersistentAttributeType Test failed");
    }
  }

  /*
   * @testName: getName
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1464;
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
      ManagedType<B> mTypeB = metaModel.managedType(
          com.sun.ts.tests.jpa.core.metamodelapi.singularattribute.B.class);
      if (mTypeB != null) {
        TestUtil.logTrace("Obtained Non-null ManagedType");
        SingularAttribute<B, Address> singAttrib = mTypeB
            .getDeclaredSingularAttribute("address",
                com.sun.ts.tests.jpa.core.metamodelapi.singularattribute.Address.class);
        if (singAttrib != null) {
          String name = singAttrib.getName();
          if (name.equals("address")) {
            TestUtil.logTrace("Received expected result" + name);
            pass = true;

          } else {
            TestUtil.logErr("Expected: address, actual:" + name);
          }
        }
      }
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getName Test failed");
    }
  }

  /*
   * @testName: getJavaType
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1463;
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
      ManagedType<B> mTypeB = metaModel.managedType(
          com.sun.ts.tests.jpa.core.metamodelapi.singularattribute.B.class);
      if (mTypeB != null) {
        TestUtil.logTrace("Obtained Non-null ManagedType");
        SingularAttribute<B, Address> singAttrib = mTypeB
            .getDeclaredSingularAttribute("address",
                com.sun.ts.tests.jpa.core.metamodelapi.singularattribute.Address.class);
        if (singAttrib != null) {
          Class pSingAttribJavaType = singAttrib.getJavaType();
          if (pSingAttribJavaType.getName().equals(
              "com.sun.ts.tests.jpa.core.metamodelapi.singularattribute.Address")) {
            TestUtil
                .logTrace("Received expected result " + pSingAttribJavaType);
            pass = true;
          } else {
            TestUtil.logErr("Expected: address, actual:" + pSingAttribJavaType);
          }
        }
      }
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getJavaType Test failed");
    }
  }

  /*
   * @testName: getJavaMember
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1462;
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
      ManagedType<B> mTypeB = metaModel.managedType(
          com.sun.ts.tests.jpa.core.metamodelapi.singularattribute.B.class);
      if (mTypeB != null) {
        TestUtil.logTrace("Obtained Non-null ManagedType");
        SingularAttribute<B, Address> singAttrib = mTypeB
            .getDeclaredSingularAttribute("address",
                com.sun.ts.tests.jpa.core.metamodelapi.singularattribute.Address.class);
        if (singAttrib != null) {
          TestUtil.logTrace("Singular attribute JavaMember = "
              + singAttrib.getJavaMember().getName());
          java.lang.reflect.Member javaMember = singAttrib.getJavaMember();
          if (javaMember.getName().equals("address")) {
            TestUtil
                .logTrace("Received expected result " + javaMember.getName());
            pass = true;
          } else {
            TestUtil
                .logErr("Expected: address, actual:" + javaMember.getName());
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
   * @testName: getDeclaringType
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1461
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
      ManagedType<B> mTypeB = metaModel.managedType(
          com.sun.ts.tests.jpa.core.metamodelapi.singularattribute.B.class);
      if (mTypeB != null) {
        TestUtil.logTrace("Obtained Non-null ManagedType");
        SingularAttribute<B, Address> singAttrib = mTypeB
            .getDeclaredSingularAttribute("address",
                com.sun.ts.tests.jpa.core.metamodelapi.singularattribute.Address.class);
        if (singAttrib != null) {
          TestUtil.logTrace("attribute Name = " + singAttrib.getName());
          ManagedType<B> newTypeOrder = singAttrib.getDeclaringType();
          if (newTypeOrder != null) {
            Class javaType = newTypeOrder.getJavaType();
            if (javaType.getName().equals(
                "com.sun.ts.tests.jpa.core.metamodelapi.singularattribute.B")) {
              TestUtil
                  .logTrace("Received expected result:" + javaType.getName());
              pass = true;
            } else {
              TestUtil.logErr(
                  "Expected: com.sun.ts.tests.jpa.core.metamodelapi.singularattribute.B, actual:"
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
   * @testName: getBindableType
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1469;
   *
   * @test_Strategy:
   *
   */
  public void getBindableType() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      ManagedType<B> mTypeB = metaModel.managedType(
          com.sun.ts.tests.jpa.core.metamodelapi.singularattribute.B.class);
      if (mTypeB != null) {
        TestUtil.logTrace("Obtained Non-null ManagedType");
        SingularAttribute<B, Address> singAttrib = mTypeB
            .getDeclaredSingularAttribute("address",
                com.sun.ts.tests.jpa.core.metamodelapi.singularattribute.Address.class);
        if (singAttrib != null) {
          TestUtil.logTrace("attribute Name = " + singAttrib.getName());
          Bindable.BindableType bType = singAttrib.getBindableType();
          if (bType != null) {

            if (bType.name()
                .equals(Bindable.BindableType.SINGULAR_ATTRIBUTE.name())) {
              TestUtil.logTrace("Received expected result:" + bType.name());
              pass = true;
            } else {
              TestUtil.logErr(
                  "Expected: " + Bindable.BindableType.SINGULAR_ATTRIBUTE.name()
                      + ", actual:" + bType.name());
            }
          }
        }
      }
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getBindableType Test  failed");
    }
  }

  /*
   * @testName: getBindableJavaType
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1468;
   *
   * @test_Strategy:
   *
   */
  public void getBindableJavaType() throws Fault {
    boolean pass = false;
    String expected = "com.sun.ts.tests.jpa.core.metamodelapi.singularattribute.Address";
    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      ManagedType<B> mTypeB = metaModel.managedType(
          com.sun.ts.tests.jpa.core.metamodelapi.singularattribute.B.class);
      if (mTypeB != null) {
        TestUtil.logTrace("Obtained Non-null ManagedType");
        SingularAttribute<B, Address> singAttrib = mTypeB
            .getDeclaredSingularAttribute("address",
                com.sun.ts.tests.jpa.core.metamodelapi.singularattribute.Address.class);
        if (singAttrib != null) {
          TestUtil.logTrace("attribute Name = " + singAttrib.getName());
          Class cType = singAttrib.getBindableJavaType();
          if (cType != null) {
            if (cType.getName().equals(expected)) {
              TestUtil.logTrace("Received expected result:" + cType.getName());
              pass = true;
            } else {
              TestUtil.logErr(
                  "Expected: " + expected + ", actual:" + cType.getName());
            }
          }
        }
      }
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getBindableJavaType Test  failed");
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
