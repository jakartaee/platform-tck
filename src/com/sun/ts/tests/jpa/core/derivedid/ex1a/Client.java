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

package com.sun.ts.tests.jpa.core.derivedid.ex1a;

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
   * @assertion_ids: PERSISTENCE:SPEC:1183;
   * PERSISTENCE:SPEC:1184;PERSISTENCE:SPEC:1185;
   *
   * @test_Strategy: Derived Identifier The parent entity ( DID1Employee ) has a
   * simple primary key Case (a): The dependent entity (DID1Dependent ) uses
   * IdClass to represent a composite key:
   */
  public void DIDTest() throws Fault {
    boolean pass = false;

    try {

      getEntityTransaction().begin();

      final DID1Employee employee1 = new DID1Employee(1L, "Duke");
      final DID1Employee employee2 = new DID1Employee(2L, "foo");

      final DID1Dependent dep1 = new DID1Dependent("Obama", employee1);
      final DID1Dependent dep2 = new DID1Dependent("Michelle", employee1);
      final DID1Dependent dep3 = new DID1Dependent("John", employee2);

      getEntityManager().persist(dep1);
      getEntityManager().persist(dep2);
      getEntityManager().persist(dep3);
      getEntityManager().persist(employee1);
      getEntityManager().persist(employee2);

      TestUtil.logTrace("persisted Employees and Dependents");
      getEntityManager().flush();

      // Refresh Employee and Dependents
      for (int i = 1; i < 3; i++) {
        DID1Employee newEmployee = getEntityManager().find(DID1Employee.class,
            Long.valueOf(i));
        if (newEmployee != null) {
          getEntityManager().refresh(newEmployee);
        }
      }

      DID1Dependent newDependent = getEntityManager().find(DID1Dependent.class,
          new DID1DependentId("Obama", 1L));
      if (newDependent != null) {
        getEntityManager().refresh(newDependent);
      }

      List depList = getEntityManager().createQuery(
          "Select d from DID1Dependent d where d.name='Obama' and d.emp.name='Duke'")
          .getResultList();
      newDependent = null;
      if (depList.size() > 0) {
        newDependent = (DID1Dependent) depList.get(0);
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
      getEntityManager().createNativeQuery("DELETE FROM DID1DEPENDENT")
          .executeUpdate();
      getEntityManager().createNativeQuery("DELETE FROM DID1EMPLOYEE")
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
