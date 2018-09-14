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

package com.sun.ts.tests.jpa.core.derivedid.ex6b;

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

  /**
   * @testName: DIDTest
   * @assertion_ids: PERSISTENCE:SPEC:1182;PERSISTENCE:SPEC:1183;
   *                 PERSISTENCE:SPEC:1184; PERSISTENCE:SPEC:1185;
   * @test_Strategy: Derived Identifier
   *                 <p/>
   *                 The parent entity uses EmbeddedId. The dependent's primary
   *                 key is of the same type as that of the parent.
   *                 <p/>
   *                 Case (a): The dependent class uses EmbeddedId
   */
  public void DIDTest() throws Fault {
    boolean pass = false;
    boolean pass1 = false;
    boolean pass2 = false;

    try {

      getEntityTransaction().begin();

      final DID6bPersonId personId = new DID6bPersonId("Java", "DUKE");
      final DID6bPerson person = new DID6bPerson(personId, "123456789");
      final DID6bMedicalHistory mHistory = new DID6bMedicalHistory(personId,
          person, "drFoo");

      getEntityManager().persist(person);
      getEntityManager().persist(mHistory);

      TestUtil.logTrace("persisted Patient and MedicalHistory");
      getEntityManager().flush();

      // Refresh MedicalHistory
      DID6bMedicalHistory newMHistory = getEntityManager()
          .find(DID6bMedicalHistory.class, personId);
      if (newMHistory != null) {
        getEntityManager().refresh(newMHistory);
      }

      final List depList = getEntityManager().createQuery(
          "Select m from DID6bMedicalHistory m where m.patient.id.firstName='Java'")
          .getResultList();
      newMHistory = null;
      if (depList.size() > 0) {
        newMHistory = (DID6bMedicalHistory) depList.get(0);
        if (newMHistory.getPatient() == person) {
          pass1 = true;
          TestUtil.logTrace("Received Expected Patient");
        } else {
          TestUtil.logErr("Searched Patient not found");
        }
      }

      List depList2 = getEntityManager()
          .createQuery(
              "Select m from DID6bMedicalHistory m where m.id.firstName='Java'")
          .getResultList();
      DID6bMedicalHistory newMHistory2 = null;
      if (depList2.size() > 0) {
        newMHistory2 = (DID6bMedicalHistory) depList.get(0);
        if (newMHistory2 != null) {
          if (newMHistory2.getPatient() == person) {
            pass = true;
            TestUtil.logTrace("Received Expected Patient");
          } else {
            TestUtil.logErr("Searched Patient not found");
          }
        } else {
          TestUtil.logErr("getEntityManager().createQuery returned null entry");
        }
      } else {
        TestUtil.logErr("getEntityManager().createQuery returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      getEntityManager().getTransaction().rollback();
    }

    if (pass1 & pass2) {
      pass = true;
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
      getEntityManager().createNativeQuery("DELETE FROM DID6BMEDICALHISTORY")
          .executeUpdate();
      getEntityManager().createNativeQuery("DELETE FROM DID6BPERSON")
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
