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

package com.sun.ts.tests.jpa.core.annotations.mapsid;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
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
   * BEGIN Test Cases
   */

  /*
   * @testName: persistMX1Test1
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1090; PERSISTENCE:SPEC:1091;
   * PERSISTENCE:SPEC:1070; PERSISTENCE:SPEC:1071; PERSISTENCE:SPEC:618;
   * PERSISTENCE:SPEC:622; PERSISTENCE:JAVADOC:372;
   * 
   * @test_Strategy: The new entity bean instance becomes both managed and
   * persistent by invoking the persist method on it. The semantics of the
   * persist operation as applied to entity X is as follows: The perist
   * operation is cascaded to entities referenced by X, if the relationship from
   * X to these other entities is annotated with cascade=PERSIST annotation
   * member.
   *
   * Invoke persist on a ManyToOne relationship from X annotated with
   * cascade=PERSIST and ensure the persist operation is cascaded.
   *
   */
  public void persistMX1Test1() throws Fault {
    TestUtil.logTrace("Begin persistMX1Test1");
    boolean pass = false;
    EntityManager em = null;
    EntityTransaction et = null;
    em = getEntityManager();
    et = getEntityTransaction();
    et.begin();
    try {

      final DID1bEmployee employee1 = new DID1bEmployee(1L, "Duke");
      final DID1bEmployee employee2 = new DID1bEmployee(2L, "foo");

      final DID1bDependent dep1 = new DID1bDependent(
          new DID1bDependentId("Obama", 1L), employee1);
      final DID1bDependent dep2 = new DID1bDependent(
          new DID1bDependentId("Michelle", 1L), employee1);
      final DID1bDependent dep3 = new DID1bDependent(
          new DID1bDependentId("John", 2L), employee2);

      em.persist(dep1);
      em.persist(dep2);
      em.persist(dep3);
      em.persist(employee1);
      em.persist(employee2);

      TestUtil.logTrace("persisted Employees and Dependents");
      em.flush();

      // Refresh dependent
      DID1bDependent newDependent = em.find(DID1bDependent.class,
          new DID1bDependentId("Obama", 1L));
      if (newDependent != null) {
        em.refresh(newDependent);
      }

      final List depList = em.createQuery(
          "Select d from DID1bDependent d where d.id.name='Obama' and d.emp.name='Duke'")
          .getResultList();

      newDependent = null;
      if (depList.size() > 0) {
        newDependent = (DID1bDependent) depList.get(0);

      }

      if (newDependent == dep1) {
        pass = true;
        TestUtil.logTrace("Received Expected Dependent");
      } else {
        TestUtil.logTrace("searched Dependent not found");
      }

      et.commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      et.rollback();
    }

    if (!pass) {
      throw new Fault("persistMX1Test1 failed");
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
      getEntityManager().createNativeQuery("Delete from DID1BDEPENDENT")
          .executeUpdate();
      getEntityManager().createNativeQuery("Delete from DID1BEMPLOYEE")
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
