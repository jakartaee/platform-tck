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

package com.sun.ts.tests.jpa.core.metamodelapi.basictype;

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
   * @testName: getJavaType
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1222
   *
   * @test_Strategy: Get the javaType of the ID
   *
   */
  public void getJavaType() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      ManagedType<Order> mTypeOrder = metaModel.managedType(Order.class);
      if (mTypeOrder != null) {
        TestUtil.logTrace("Obtained Non-null ManagedType");
        Attribute attrib = mTypeOrder.getDeclaredAttribute("id");
        if (attrib != null) {
          String type = attrib.getJavaType().getName();
          if (type.equals("int")) {
            TestUtil.logTrace("Received expected type: int");
            pass = true;
          } else {
            TestUtil.logErr("Expected: int, actual:" + type);
          }
        } else {
          TestUtil.logErr("getDeclaredAttribute(...) returned null");
        }
      } else {
        TestUtil.logErr("managedType(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getJavaType Test failed");
    }
  }

  /*
   * @testName: getPersistenceType
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1223
   *
   * @test_Strategy: Get the persistent type of the Order class
   *
   */
  public void getPersistenceType() throws Fault {
    boolean pass = false;

    String expected = Type.PersistenceType.ENTITY.name();
    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      EntityType<Order> eType = metaModel.entity(Order.class);
      if (eType != null) {
        Type.PersistenceType type = eType.getPersistenceType();
        if (type != null) {
          String sType = type.name();
          if (sType.equals(expected)) {
            TestUtil.logTrace("Received:" + sType);
            pass = true;
          } else {
            TestUtil.logErr("Expected: " + expected + ", actual:" + sType);
          }
        } else {
          TestUtil.logErr("getPersistenceType() returned null");
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getPersistenceType failed");
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
