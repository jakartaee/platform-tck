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

package com.sun.ts.tests.jpa.core.metamodelapi.type;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import javax.persistence.metamodel.*;
import java.util.Properties;
import java.util.Set;

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
   * @testName: getPersistenceType
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1472
   *
   * @test_Strategy:
   *
   */
  public void getPersistenceType() throws Fault {
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
      throw new Fault("getPersistenceType Test  failed");
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
  public void getEmbeddablePersistenceType() throws Fault {
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
      throw new Fault("getEmbeddablePersistenceType Test  failed");
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
  public void getJavaType() throws Fault {
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
      throw new Fault("getJavaType Test  failed");
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
