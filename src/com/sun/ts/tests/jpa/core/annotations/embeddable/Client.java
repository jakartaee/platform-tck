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

package com.sun.ts.tests.jpa.core.annotations.embeddable;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
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
   * @testName: EM1XMTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:553; PERSISTENCE:SPEC:557;
   * PERSISTENCE:SPEC:1239; PERSISTENCE:SPEC:1190; PERSISTENCE:SPEC:1191;
   * 
   * @test_Strategy: Use Nested embeddable class in Query
   *
   */
  public void EM1XMTest() throws Fault {
    TestUtil.logTrace("Begin EM1XMTest2");
    boolean pass = false;
    EntityManager em = getEntityManager();
    EntityTransaction et = getEntityTransaction();

    try {
      et.begin();
      TestUtil.logTrace("New instances");

      final ZipCode z1 = new ZipCode("01801", "1234");

      Address addr1 = new Address("1 Network Drive", "Burlington", "MA");

      addr1.setZipCode(z1);

      B b1 = new B("1", "b1", 1);
      b1.setAddress(addr1);

      em.persist(b1);
      em.flush();

      B newB = findB("1");
      em.refresh(newB);

      final String newStreet = (String) em
          .createQuery("Select b.address.street from B b ").getSingleResult();
      final String newState = (String) em
          .createQuery("Select b.address.state from B b ").getSingleResult();
      final String newCity = (String) em
          .createQuery("Select b.address.city from B b ").getSingleResult();
      final String newPlusFour = (String) em
          .createQuery("Select b.address.zipCode.plusFour from B b ")
          .getSingleResult();
      final String newZip = (String) em
          .createQuery("Select b.address.zipCode.zip from B b ")
          .getSingleResult();

      boolean pass1 = false;
      boolean pass2 = false;
      boolean pass3 = false;
      boolean pass4 = false;
      boolean pass5 = false;

      // Verify Embedded contents
      if (addr1.getStreet().equals(newStreet)) {
        pass1 = true;
        TestUtil.logTrace("Received Street match");
      }

      if (addr1.getState().equals(newState)) {
        pass2 = true;
        TestUtil.logTrace("Received State match");
      }

      if (addr1.getCity().equals(newCity)) {
        pass3 = true;
        TestUtil.logTrace("Received City match");
      }

      if (addr1.getZipCode().getPlusFour().equals(newPlusFour)) {
        pass4 = true;
        TestUtil.logTrace("Received zipCode PlusFour match");
      }

      if (addr1.getZipCode().getZip().equals(newZip)) {
        pass5 = true;
        TestUtil.logTrace("Received zipCode zip match");
      }

      if (pass1 && pass2 && pass3 && pass4 && pass5) {
        pass = true;
        TestUtil.logTrace("Received Address match");

      } else {
        TestUtil.logTrace("Received incorrect data");

      }

      et.commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (et.isActive()) {
          et.rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }

    }
    if (!pass) {
      throw new Fault("EM1XMTest failed");
    }
  }

  private B findB(String id) {
    // TestUtil.logTrace("Entered findB method");
    return getEntityManager().find(B.class, id);
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
      getEntityManager().createNativeQuery("Delete from B_EMBEDDABLE")
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
