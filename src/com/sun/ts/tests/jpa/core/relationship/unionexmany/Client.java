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

package com.sun.ts.tests.jpa.core.relationship.unionexmany;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import java.util.Collection;
import java.util.Properties;
import java.util.Vector;

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
   * @testName: uni1XMTest1
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1094; PERSISTENCE:JAVADOC:135;
   * PERSISTENCE:JAVADOC:91; PERSISTENCE:SPEC:561; PERSISTENCE:SPEC:562;
   * PERSISTENCE:SPEC:567; PERSISTENCE:SPEC:570; PERSISTENCE:SPEC:571;
   * PERSISTENCE:SPEC:573; PERSISTENCE:SPEC:961; PERSISTENCE:SPEC:1028;
   * PERSISTENCE:SPEC:1037; PERSISTENCE:SPEC:1038; PERSISTENCE:SPEC:1039
   * 
   * @test_Strategy: RelationShip OneToMany Mapping
   *
   */
  public void uni1XMTest1() throws Fault {
    TestUtil.logTrace("Begin uni1X1Test1");
    boolean pass = false;
    try {
      getEntityTransaction().begin();

      Uni1XMProject project1 = new Uni1XMProject(1L, "JavaEE", 500.0F);
      Uni1XMProject project2 = new Uni1XMProject(2L, "Identity", 500.0F);
      Uni1XMPerson person = new Uni1XMPerson(1L, "Duke");

      getEntityManager().persist(project1);
      getEntityManager().persist(project2);
      getEntityManager().persist(person);
      TestUtil.logTrace("persisted Person and Projects");

      Vector<Uni1XMProject> projects = new Vector<Uni1XMProject>();
      projects.add(project1);
      projects.add(project2);

      person.setProjects(projects);
      getEntityManager().merge(person);
      TestUtil.logTrace("merged Contents of Person Entity");

      Uni1XMPerson newPerson = getEntityManager().find(Uni1XMPerson.class, 1L);
      boolean pass1 = false;
      boolean pass2 = false;

      if (newPerson != null) {

        Collection<Uni1XMProject> newProjects = newPerson.getProjects();
        for (Uni1XMProject prj : newProjects) {
          if (prj.getName().equals("Identity")) {
            pass1 = true;
          } else if (prj.getName().equals("JavaEE")) {
            pass2 = true;
          }
        }
      }

      if (pass1 && pass2) {
        TestUtil.logTrace("Expected results received");
        pass = true;
      } else {
        TestUtil.logErr("Unexpected results received");
        pass = false;
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
      throw new Fault("uni1XMTest1 failed");
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
      getEntityManager()
          .createNativeQuery("DELETE FROM UNI1XMPERSON_UNI1XMPROJECT")
          .executeUpdate();
      getEntityManager().createNativeQuery("DELETE FROM UNI1XMPROJECT")
          .executeUpdate();
      getEntityManager().createNativeQuery("DELETE FROM UNI1XMPERSON")
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
