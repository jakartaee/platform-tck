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

package com.sun.ts.tests.jpa.core.derivedid.ex1b;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import java.util.List;
import java.util.Properties;

/**
 * @author Raja Perumal
 */
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
   * @testName: DIDTest
   *
   * @assertion_ids: PERSISTENCE:SPEC:1335; PERSISTENCE:SPEC:1336;
   * PERSISTENCE:SPEC:1337;
   *
   * @test_Strategy: Derived Identifier The parent entity ( DID1bEmployee ) has
   * a simple primary key Case(b): The dependent entity uses EmbeddedId to
   * represent a composite key
   */
  public void DIDTest() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    try {

      final DID1bEmployee employee1 = new DID1bEmployee(1L, "Duke");
      final DID1bEmployee employee2 = new DID1bEmployee(2L, "foo");

      final DID1bDependentId depId1 = new DID1bDependentId("Obama", 1L);
      final DID1bDependentId depId2 = new DID1bDependentId("Michelle", 1L);
      final DID1bDependentId depId3 = new DID1bDependentId("John", 2L);

      final DID1bDependent dep1 = new DID1bDependent(depId1, employee1);
      final DID1bDependent dep2 = new DID1bDependent(depId2, employee1);
      final DID1bDependent dep3 = new DID1bDependent(depId3, employee2);

      getEntityManager().persist(dep1);
      getEntityManager().persist(dep2);
      getEntityManager().persist(dep3);
      getEntityManager().persist(employee1);
      getEntityManager().persist(employee2);

      TestUtil.logTrace("persisted Employees and Dependents");
      getEntityManager().flush();

      // Refresh Dependent
      DID1bDependent newDependent = getEntityManager()
          .find(DID1bDependent.class, depId1);
      if (newDependent != null) {
        getEntityManager().refresh(newDependent);
      }

      final List depList = getEntityManager().createQuery(
          "Select d from DID1bDependent d where d.id.name='Obama' and d.emp.name='Duke'")
          .getResultList();

      newDependent = null;
      if (depList.size() > 0) {
        newDependent = (DID1bDependent) depList.get(0);
        if (newDependent == dep1) {
          pass = true;
          TestUtil.logTrace("Received Expected Dependent");
        } else {
          TestUtil.logErr("Searched Dependent not found");
        }
      } else {
        TestUtil.logErr("getEntityManager().createQuery returned null entry");
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      getEntityTransaction().rollback();
    }

    if (!pass) {
      throw new Fault("DIDTest failed");
    }
  }

  public void cleanup() throws Fault {
    TestUtil.logTrace("cleanup");
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
      getEntityManager().createNativeQuery("DELETE FROM DID1BDEPENDENT")
          .executeUpdate();
      getEntityManager().createNativeQuery("DELETE FROM DID1BEMPLOYEE")
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
