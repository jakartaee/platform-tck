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

package com.sun.ts.tests.jpa.core.metamodelapi.metamodel;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import javax.persistence.metamodel.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.Set;

public class Client extends PMClientBase {

  public Client() {
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
   * @testName: getMetamodel
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:330
   *
   * @test_Strategy:
   * 
   */
  public void getMetamodel() throws Fault {
    boolean pass = false;

    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      pass = true;
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
    }

    if (!pass) {
      throw new Fault("getMetamodeltest failed");
    }
  }

  /*
   * @testName: getEntities
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1438
   *
   * @test_Strategy:
   *
   */
  public void getEntities() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      Set<EntityType<?>> orderSet = metaModel.getEntities();
      if (orderSet != null) {
        TestUtil.logTrace("Obtained Non-null Set of EntityType");
        for (EntityType eType : orderSet) {
          TestUtil.logTrace("entityType Name = " + eType.getName());
          pass = true;
        }

      }
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getEntities Test  failed");
    }
  }

  /*
   * @testName: getManagedTypes
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1439
   *
   * @test_Strategy:
   *
   */
  public void getManagedTypes() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      Set<ManagedType<?>> orderSet = metaModel.getManagedTypes();
      if (orderSet != null) {
        TestUtil.logTrace("Obtained Non-null Set of ManagedType");
        for (ManagedType mType : orderSet) {
          Set<Attribute<Order, ?>> attribSet = mType.getDeclaredAttributes();
          if (attribSet != null) {
            for (Attribute attrib : attribSet) {
              TestUtil.logTrace("attribute Name = " + attrib.getName());
            }
            pass = true;
          }
        }
      }
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getManagedTypes Test  failed");
    }
  }

  /*
   * @testName: getEmbeddables
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1437
   *
   * @test_Strategy:
   *
   */
  public void getEmbeddables() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      Set<EmbeddableType<?>> addrSet = metaModel.getEmbeddables();
      if (addrSet != null) {
        TestUtil.logTrace("Obtained Non-null Set of EmbeddableType");
        for (EmbeddableType eType : addrSet) {
          Set<Attribute<Address, ?>> attribSet = eType.getDeclaredAttributes();
          if (attribSet != null) {
            for (Attribute attrib : attribSet) {
              TestUtil.logTrace("attribute Name = " + attrib.getName());
            }
            pass = true;
          }
        }
      }
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getEmbeddables Test  failed");
    }
  }

  /*
   * @testName: managedType
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1440
   *
   * @test_Strategy:
   *
   */
  public void managedType() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {

      TestUtil.logMsg("Test entity");
      String expected = "com.sun.ts.tests.jpa.core.metamodelapi.metamodel.Order";
      ManagedType mType = metaModel.managedType(
          com.sun.ts.tests.jpa.core.metamodelapi.metamodel.Order.class);
      if (mType != null) {
        ManagedType<Order> mTypeOrder = mType;
        String cActual = mType.getJavaType().getName();
        if (cActual.equals(expected)) {
          Set<Attribute<Order, ?>> attribSet = mTypeOrder
              .getDeclaredAttributes();
          if (attribSet != null) {
            for (Attribute attrib : attribSet) {
              TestUtil.logTrace("attribute Name = " + attrib.getName());
            }
            pass1 = true;
          } else {
            TestUtil.logErr("getDeclaredAttributes() returned null");
          }
        } else {
          TestUtil.logErr("Expected:" + expected + ", actual:" + cActual);
        }
      }
      TestUtil.logMsg("Test embeddable");
      expected = "com.sun.ts.tests.jpa.core.metamodelapi.metamodel.Address";
      mType = metaModel.managedType(
          com.sun.ts.tests.jpa.core.metamodelapi.metamodel.Address.class);
      if (mType != null) {
        ManagedType<Address> mTypeAddress = mType;
        String cActual = mType.getJavaType().getName();
        if (cActual.equals(expected)) {
          Set<Attribute<Address, ?>> attribSet = mTypeAddress
              .getDeclaredAttributes();
          if (attribSet != null) {
            for (Attribute attrib : attribSet) {
              TestUtil.logTrace("attribute Name = " + attrib.getName());
            }
            pass2 = true;
          } else {
            TestUtil.logErr("getDeclaredAttributes() returned null");
          }
        } else {
          TestUtil.logErr("Expected:" + expected + ", actual:" + cActual);
        }
      }

      TestUtil.logMsg("Test superclass");
      expected = "com.sun.ts.tests.jpa.core.metamodelapi.metamodel.Employee";
      mType = metaModel.managedType(
          com.sun.ts.tests.jpa.core.metamodelapi.metamodel.Employee.class);
      if (mType != null) {
        ManagedType<Employee> mTypeEmployee = mType;
        String cActual = mType.getJavaType().getName();
        if (cActual.equals(expected)) {
          Set<Attribute<Employee, ?>> attribSet = mTypeEmployee
              .getDeclaredAttributes();
          if (attribSet != null) {
            for (Attribute attrib : attribSet) {
              TestUtil.logTrace("attribute Name = " + attrib.getName());
            }
            pass3 = true;
          } else {
            TestUtil.logErr("getDeclaredAttributes() returned null");
          }
        } else {
          TestUtil.logErr("Expected:" + expected + ", actual:" + cActual);
        }
      }
    }

    getEntityTransaction().commit();

    if (!pass1 || !pass2 || !pass3) {
      throw new Fault("managedType Test failed");
    }
  }

  /*
   * @testName: managedTypeIllegalArgumentException
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1441
   *
   * @test_Strategy:
   *
   */
  public void managedTypeIllegalArgumentException() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      try {
        metaModel.managedType(
            com.sun.ts.tests.jpa.core.metamodelapi.metamodel.Client.class);
        TestUtil.logErr("Did not throw IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
        pass = true;
      } catch (Exception e) {
        TestUtil.logErr("Received unexpected exception", e);
      }
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("managedTypeIllegalArgumentException failed");
    }
  }

  /*
   * @testName: entity
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1435
   *
   * @test_Strategy:
   *
   */
  public void entity() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      EntityType<Order> eTypeOrder = metaModel
          .entity(com.sun.ts.tests.jpa.core.metamodelapi.metamodel.Order.class);
      if (eTypeOrder != null) {
        TestUtil.logTrace("Obtained Non-null EntityType");
        Set<Attribute<Order, ?>> attribSet = eTypeOrder.getDeclaredAttributes();
        if (attribSet != null) {
          for (Attribute attrib : attribSet) {
            TestUtil.logTrace("attribute Name = " + attrib.getName());
          }
          pass = true;
        }
      }
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("entity Test  failed");
    }
  }

  /*
   * @testName: entityIllegalArgumentException
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1436
   *
   * @test_Strategy:
   *
   */
  public void entityIllegalArgumentException() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      try {
        metaModel.entity(
            com.sun.ts.tests.jpa.core.metamodelapi.metamodel.Client.class);
        TestUtil.logErr("Did not throw IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
        pass = true;
      } catch (Exception e) {
        TestUtil.logErr("Received unexpected exception", e);
      }
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("entityIllegalArgumentException  failed");
    }
  }

  /*
   * @testName: embeddable
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1433
   *
   * @test_Strategy:
   *
   */
  public void embeddable() throws Fault {
    boolean pass = true;
    Collection<String> expected = new ArrayList<String>();
    expected.add("zipcode");
    expected.add("street");
    expected.add("state");
    expected.add("city");

    try {

      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        EmbeddableType<Address> eTypeOrder = metaModel.embeddable(
            com.sun.ts.tests.jpa.core.metamodelapi.metamodel.Address.class);
        if (eTypeOrder != null) {
          TestUtil.logTrace("Obtained Non-null EmbeddableType");
          Set<Attribute<Address, ?>> attribSet = eTypeOrder
              .getDeclaredAttributes();
          if (attribSet != null) {
            if (attribSet.size() != expected.size()) {
              pass = false;
              TestUtil.logErr("Received wrong number of results");
            }
            for (Attribute attrib : attribSet) {
              String name = attrib.getName();
              if (expected.contains(name)) {
                TestUtil.logTrace("received attribute Name = " + name);
              } else {
                TestUtil.logErr("Received unexpected result" + name);
                pass = false;
              }
            }
          } else {
            pass = false;
            TestUtil.logErr("getDeclaredAttributes() returned null");
          }
        } else {
          pass = false;
          TestUtil.logErr("embeddable() returned null");
        }
      } else {
        pass = false;
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Received unxpected exception", e);
    }
  }

  /*
   * @testName: embeddableIllegalArgumentException
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1434
   *
   * @test_Strategy:
   *
   */
  public void embeddableIllegalArgumentException() throws Fault {
    boolean pass = false;

    try {

      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        try {
          metaModel.embeddable(
              com.sun.ts.tests.jpa.core.metamodelapi.metamodel.Client.class);
          TestUtil.logErr("Did not throw IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
          TestUtil.logTrace("Received expected IllegalArgumentException");
          pass = true;
        } catch (Exception e) {
          TestUtil.logErr("Received unexpected exception", e);
        }
      } else {
        pass = false;
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass) {
      throw new Fault("embeddableIllegalArgumentException failed");
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
