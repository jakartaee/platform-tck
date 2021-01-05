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

package com.sun.ts.tests.jpa.core.relationship.unimanyxone;

import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

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
   * @testName: uniMX1Test1
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
  public void uniMX1Test1() throws Fault {
    TestUtil.logTrace("Begin uniMX1Test1");
    boolean pass = false;
    try {
      getEntityTransaction().begin();

      UniMX1Project project1 = new UniMX1Project(1L, "JavaEE", 500.0F);

      UniMX1Person person1 = new UniMX1Person(1L, "Duke");
      UniMX1Person person2 = new UniMX1Person(2L, "Foo");

      getEntityManager().persist(project1);
      getEntityManager().persist(person1);
      getEntityManager().persist(person2);

      person1.setProject(project1);
      person2.setProject(project1);

      getEntityManager().merge(person1);
      getEntityManager().merge(person2);

      TestUtil.logTrace("persisted Persons and Project");

      boolean pass1 = false;
      boolean pass2 = false;

      UniMX1Person newPerson = getEntityManager().find(UniMX1Person.class, 1L);

      if (newPerson != null) {

        UniMX1Project newProject = newPerson.getProject();

        if (newProject.getName().equals("JavaEE")) {
          pass1 = true;
        }

      }

      UniMX1Person newPerson2 = getEntityManager().find(UniMX1Person.class, 2L);

      if (newPerson2 != null) {

        UniMX1Project newProject2 = newPerson2.getProject();

        if (newProject2.getName().equals("JavaEE")) {
          pass2 = true;
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
      throw new Fault("uniMX1Test1 failed");
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
      getEntityManager().createNativeQuery("DELETE FROM UNIMX1PERSON")
          .executeUpdate();
      getEntityManager().createNativeQuery("DELETE FROM UNIMX1PROJECT")
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
