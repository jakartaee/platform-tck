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

package com.sun.ts.tests.jpa.core.relationship.unionexone;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import java.util.Properties;

/**
 *
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

    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  /*
   * @testName: uni1X1Test1
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1094; PERSISTENCE:JAVADOC:135;
   * PERSISTENCE:JAVADOC:91; PERSISTENCE:SPEC:561; PERSISTENCE:SPEC:562;
   * PERSISTENCE:SPEC:567; PERSISTENCE:SPEC:570; PERSISTENCE:SPEC:571;
   * PERSISTENCE:SPEC:573; PERSISTENCE:SPEC:961; PERSISTENCE:SPEC:1028;
   * PERSISTENCE:SPEC:1037; PERSISTENCE:SPEC:1038; PERSISTENCE:SPEC:1039
   * 
   * @test_Strategy: RelationShip OneToOne Mapping
   *
   */
  public void uni1X1Test1() throws Fault {
    TestUtil.logTrace("Begin uni1X1Test1");
    boolean pass = false;
    try {
      getEntityTransaction().begin();

      Uni1X1Project project = new Uni1X1Project(1L, "JavaEE", 500.0F);
      Uni1X1Person person = new Uni1X1Person(1L, "Duke");
      person.setProject(project);

      getEntityManager().persist(person);
      TestUtil.logTrace("persisted Person and Project");

      Uni1X1Person newPerson = getEntityManager().find(Uni1X1Person.class, 1L);
      if (newPerson.getProject().getName().equals("JavaEE")) {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();

    } catch (Exception e) {

      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception in rollback:", re);
      }
    }

    if (!pass) {
      throw new Fault("uni1X1Test1 failed");
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
      getEntityManager().createNativeQuery("DELETE FROM UNI1X1PERSON")
          .executeUpdate();
      getEntityManager().createNativeQuery("DELETE FROM UNI1X1PROJECT")
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
