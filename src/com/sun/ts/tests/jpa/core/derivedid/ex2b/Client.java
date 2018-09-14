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

package com.sun.ts.tests.jpa.core.derivedid.ex2b;

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
   * @assertion_ids: PERSISTENCE:SPEC:1182.1; PERSISTENCE:SPEC:1339
   *
   * @test_Strategy: Derived Identifier The parent entity uses IdClass Case (b):
   * The dependent entity uses EmbeddedId. The type of the empPK attribute is
   * the same as that of the primary key of Employee.
   */
  public void DIDTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;

    try {

      getEntityTransaction().begin();

      final DID2bEmployeeId eId1 = new DID2bEmployeeId("Java", "Duke");
      final DID2bEmployeeId eId2 = new DID2bEmployeeId("C", "foo");
      final DID2bEmployee employee1 = new DID2bEmployee(eId1);
      final DID2bEmployee employee2 = new DID2bEmployee(eId2);
      final DID2bDependent dep1 = new DID2bDependent(
          new DID2bDependentId("Obama", eId1), employee1);
      final DID2bDependent dep2 = new DID2bDependent(
          new DID2bDependentId("Michelle", eId1), employee1);
      final DID2bDependent dep3 = new DID2bDependent(
          new DID2bDependentId("John", eId2), employee2);

      getEntityManager().persist(dep1);
      getEntityManager().persist(dep2);
      getEntityManager().persist(dep3);

      getEntityManager().persist(employee1);
      getEntityManager().persist(employee2);
      getEntityManager().flush();
      TestUtil.logTrace("persisted Employees and Dependents");

      // Refresh Dependent
      DID2bDependent newDependent = getEntityManager().find(
          DID2bDependent.class,
          new DID2bDependentId("Obama", new DID2bEmployeeId("Java", "Duke")));
      if (newDependent != null) {
        getEntityManager().refresh(newDependent);
      }

      List depList = getEntityManager().createQuery(
          "Select d from DID2bDependent d where d.id.name='Obama' and d.emp.firstName='Java'")
          .getResultList();
      newDependent = null;
      if (depList.size() > 0) {
        newDependent = (DID2bDependent) depList.get(0);
        if (newDependent == dep1) {
          pass1 = true;
          TestUtil.logTrace("Received Expected Dependent");
        } else {
          TestUtil.logErr("Searched Dependent not found");
        }
      } else {
        TestUtil.logErr("getEntityManager().createQuery returned null entry");
      }

      List depList2 = getEntityManager().createQuery(
          "Select d from DID2bDependent d where d.id.name='Obama' and d.id.empPK.firstName='Java'")
          .getResultList();
      DID2bDependent newDependent2 = null;
      if (depList2.size() > 0) {
        newDependent2 = (DID2bDependent) depList2.get(0);
        if (newDependent2 == dep1) {
          pass2 = true;
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

    if (!(pass1 && pass2)) {
      throw new Fault("DTDTest failed");
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
      getEntityManager().createNativeQuery("DELETE FROM DID2BDEPENDENT")
          .executeUpdate();
      getEntityManager().createNativeQuery("DELETE FROM DID2BEMPLOYEE")
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
