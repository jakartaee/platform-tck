/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.core.derivedid.ex4a;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

public class ClientIT extends PMClientBase {

  public ClientIT() {
  }

@BeforeEach
  public void setup() throws Exception {
    TestUtil.logTrace("setup");
    try {
      super.setup();
      removeTestData();
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Exception("Setup failed:", e);
    }
  }

  /*
   * @testName: DIDTest
   *
   * @assertion_ids: PERSISTENCE:SPEC:1339; PERSISTENCE:SPEC:1179
   *
   * @test_Strategy: Derived Identifier
   * 
   * The parent entity has a simple primary key Case (a): The dependent entity
   * has a single relationship attribute corresponding to the parents primary
   * key. The primary key of MedicalHistory is of type String.
   */
@Test
  public void DIDTest() throws Exception {
    boolean pass = false;

    try {

      getEntityTransaction().begin();
      final DID4Person person = new DID4Person("123456789", "DUKE");
      final DID4MedicalHistory mHistory = new DID4MedicalHistory(person,
          "drFoo");

      getEntityManager().persist(person);
      getEntityManager().persist(mHistory);

      TestUtil.logTrace("persisted Patient and MedicalHistory");
      getEntityManager().flush();

      // Refresh MedicalHistory
      DID4MedicalHistory newMHistory = getEntityManager()
          .find(DID4MedicalHistory.class, "123456789");
      if (newMHistory != null) {
        getEntityManager().refresh(newMHistory);
      }

      final List depList = getEntityManager().createQuery(
          "Select m from DID4MedicalHistory m where m.patient.ssn='123456789'")
          .getResultList();
      newMHistory = null;
      if (depList.size() > 0) {
        newMHistory = (DID4MedicalHistory) depList.get(0);
        if (newMHistory != null) {
          if (newMHistory.getPatient() == person) {
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
      getEntityTransaction().rollback();
    }

    if (!pass) {
      throw new Exception("DTDTest failed");
    }
  }

@AfterEach
  public void cleanup() throws Exception {
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
      getEntityManager().createNativeQuery("DELETE FROM DID4MEDICALHISTORY")
          .executeUpdate();
      getEntityManager().createNativeQuery("DELETE FROM DID4PERSON")
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
