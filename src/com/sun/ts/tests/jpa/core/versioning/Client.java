/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

/*
  * $Id$
 */

package com.sun.ts.tests.jpa.core.versioning;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import java.math.BigInteger;
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
   * @testName: versionTest1
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1068; PERSISTENCE:SPEC:690;
   * PERSISTENCE:SPEC:666; PERSISTENCE:JAVADOC:13; PERSISTENCE:JAVADOC:16;
   * PERSISTENCE:JAVADOC:17; PERSISTENCE:JAVADOC:18; PERSISTENCE:SPEC:1400;
   * 
   * @test_Strategy: The version annotation specifies the version field or
   * property of an entity class that serves as an optimistic lock value. The
   * version is used to ensure integrity when performing the merge operation and
   * for optimistic concurrency control.
   *
   * positive test with sequential tx
   * 
   */

  public void versionTest1() throws Fault {

    TestUtil.logTrace("Begin versionTest1");
    boolean pass1 = true;
    boolean pass2 = true;
    boolean pass3 = true;
    final BigInteger donation = new BigInteger("5000000");

    try {
      getEntityTransaction().begin();
      Member m = new Member(1, "Jie Leng", true);
      getEntityManager().persist(m);
      getEntityManager().flush();
      getEntityTransaction().commit();

      // prior to writing to database, Member may not have any version value.
      // After writing to database, version must have a value.

      Member newMember = getEntityManager().find(Member.class, 1);
      if (newMember.getVersion() == null) {
        TestUtil.logErr("version after persistence is null.");
        pass1 = false;
      } else {
        TestUtil.logTrace(
            "Correct non-null version after create: " + newMember.getVersion());
      }

      // update member
      getEntityTransaction().begin();
      Member newMember2 = getEntityManager().find(Member.class, 1);
      int oldVersion = newMember2.getVersion();
      newMember2.setDonation(donation);
      getEntityManager().merge(newMember2);
      getEntityManager().flush();
      getEntityTransaction().commit();

      Member newMember3 = getEntityManager().find(Member.class, 1);
      if (newMember3.getVersion() <= oldVersion) {
        TestUtil.logErr("Wrong version after update: " + newMember3.getVersion()
            + ", old version: " + oldVersion);
        pass2 = false;
      } else {
        TestUtil.logTrace("Correct version after update: "
            + newMember3.getVersion() + ", old version: " + oldVersion);
      }

      oldVersion = newMember3.getVersion();
      // select member
      getEntityTransaction().begin();
      getEntityManager()
          .createQuery("SELECT m FROM Member m where m.memberName = :name")
          .setParameter("name", "Jie Leng").getResultList();
      getEntityManager().flush();
      getEntityTransaction().commit();

      Member newMember4 = getEntityManager().find(Member.class, 1);
      if (newMember4.getVersion() != oldVersion) {
        TestUtil.logErr("Wrong version after query, expected " + oldVersion
            + ", got " + newMember4.getVersion());
        pass3 = false;
      } else {
        TestUtil.logTrace("Correct version after query, expected " + oldVersion
            + ", got:" + newMember4.getVersion());
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass1 = false;
      pass2 = false;
      pass3 = false;
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception in rollback:", re);
      }
    }

    if (!pass1 || !pass2 || !pass3)
      throw new Fault("versionTest1 failed");
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
      getEntityManager().createNativeQuery("DELETE FROM MEMBER")
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
