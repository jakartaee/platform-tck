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

package com.sun.ts.tests.jpa.core.relationship.bidironexmany;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import java.util.Properties;
import java.util.Vector;

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
   * @testName: biDir1XMTest1
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
  public void biDir1XMTest1() throws Fault {
    TestUtil.logTrace("Begin BiDir1X1Test1");
    boolean pass = false;
    try {
      getEntityTransaction().begin();

      BiDir1XMProject project1 = new BiDir1XMProject(1L, "JavaEE", 500.0F);
      BiDir1XMProject project2 = new BiDir1XMProject(2L, "Identity", 500.0F);
      BiDir1XMPerson person = new BiDir1XMPerson(1L, "Duke");

      // getEntityManager().persist(person);
      TestUtil.logTrace("persisted Person Entity");

      Vector<BiDir1XMProject> projects = new Vector<BiDir1XMProject>();
      projects.add(project1);
      projects.add(project2);

      TestUtil.logTrace("set Projects to Person");
      person.setProjects(projects);

      TestUtil.logTrace("set Person to Projects");
      project1.setBiDir1XMPerson(person);
      project2.setBiDir1XMPerson(person);

      getEntityManager().persist(person);
      TestUtil.logTrace("persisted Person Entity");

      getEntityManager().flush();
      getEntityTransaction().commit();

      getEntityTransaction().begin();

      boolean pass1 = false;
      boolean pass2 = false;

      // Lookup Project1
      BiDir1XMProject newProject1 = getEntityManager()
          .find(BiDir1XMProject.class, 1L);
      if (newProject1 != null) {
        if (newProject1.getName().equals("JavaEE")) {
          BiDir1XMPerson newPerson = newProject1.getBiDir1XMPerson();
          if (newPerson != null) {
            if (newPerson.getName().equals("Duke")) {
              TestUtil.logTrace("Found Expected Person Entity");
              pass1 = true;
            }
          } else {
            TestUtil.logTrace("searched Person not Found");
          }
        }

      } else {
        TestUtil.logTrace("searched Project not Found");
      }

      // Lookup Project2
      BiDir1XMProject newProject2 = getEntityManager()
          .find(BiDir1XMProject.class, 2L);
      if (newProject2 != null) {
        if (newProject2.getName().equals("Identity")) {
          BiDir1XMPerson newPerson = newProject2.getBiDir1XMPerson();
          if (newPerson != null) {
            if (newPerson.getName().equals("Duke")) {
              TestUtil.logTrace("Found Expected Person Entity");
              pass2 = true;
            }
          } else {
            TestUtil.logTrace("searched Person not Found");
          }
        }

      } else {
        TestUtil.logTrace("searched Project not Found");
      }

      // Alternative Search mechanism
      /*
       * BiDir1XMPerson newPerson =
       * getEntityManager().find(BiDir1XMPerson.class, 1L);
       * 
       * if (newPerson != null) {
       * 
       * Collection<BiDir1XMProject> newProjects = newPerson.getProjects(); for
       * (BiDir1XMProject prj : newProjects) { if
       * (prj.getName().equals("Identity")) { BiDir1XMPerson p =
       * prj.getBiDir1XMPerson(); if (p != null) { if
       * (p.getName().equals("Duke")) {
       * TestUtil.logTrace("Found Expected Person Entity"); pass1 = true; }
       * 
       * } else { TestUtil.logTrace("searched Person not Found"); }
       * 
       * } else if (prj.getName().equals("JavaEE")) { BiDir1XMPerson p2 =
       * prj.getBiDir1XMPerson(); if (p2 != null) { if
       * (p2.getName().equals("Duke")) {
       * TestUtil.logTrace("Found Expected Person Entity"); pass2 = true; }
       * 
       * } else { TestUtil.logTrace("searched Person not Found"); } } } } else {
       * TestUtil.logTrace("searched Person not Found"); }
       */

      if (pass1 && pass2) {
        TestUtil.logTrace("biDir1X1Test1: Expected results received");
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
      throw new Fault("biDir1XMTest1 failed");
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
      getEntityManager().createNativeQuery("DELETE FROM BIDIR1XMPROJECT")
          .executeUpdate();
      getEntityManager().createNativeQuery("DELETE FROM BIDIR1XMPERSON")
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
