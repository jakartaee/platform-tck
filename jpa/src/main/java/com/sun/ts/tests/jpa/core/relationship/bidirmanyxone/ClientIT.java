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

package com.sun.ts.tests.jpa.core.relationship.bidirmanyxone;

import java.util.ArrayList;
import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

@ExtendWith(ArquillianExtension.class)
@TestInstance(Lifecycle.PER_CLASS)

public class ClientIT extends PMClientBase {

  public ClientIT() {
  }
  
  @Deployment(testable = false, managed = false)
  public static JavaArchive createDeployment() throws Exception {
     
     String pkgNameWithoutSuffix = ClientIT.class.getPackageName();
     String pkgName = ClientIT.class.getPackageName() + ".";
     String[] classes = { pkgName + "BiDirMX1Person", pkgName + "BiDirMX1Project"};
     return createDeploymentJar("jpa_core_relationship_bidirmanyxone.jar", pkgNameWithoutSuffix, classes);

  }


@BeforeAll
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
   * @testName: biDirMX1Test1
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1094; PERSISTENCE:JAVADOC:135;
   * PERSISTENCE:JAVADOC:91; PERSISTENCE:SPEC:561; PERSISTENCE:SPEC:562;
   * PERSISTENCE:SPEC:567; PERSISTENCE:SPEC:570; PERSISTENCE:SPEC:571;
   * PERSISTENCE:SPEC:573; PERSISTENCE:SPEC:961; PERSISTENCE:SPEC:1028;
   * PERSISTENCE:SPEC:1037; PERSISTENCE:SPEC:1038; PERSISTENCE:SPEC:1039
   *
   * @test_Strategy: Bi-Directional RelationShip ManyToOne Mapping
   *
   */
@Test
  public void biDirMX1Test1() throws Exception {
    TestUtil.logTrace("Begin biDirMX1Test1");
    boolean pass = false;
    try {

      getEntityTransaction().begin();

      BiDirMX1Project project1 = new BiDirMX1Project(1L, "JavaEE", 500.0F);

      BiDirMX1Person person1 = new BiDirMX1Person(1L, "Duke");
      BiDirMX1Person person2 = new BiDirMX1Person(2L, "Foo");

      getEntityManager().persist(project1);
      getEntityManager().persist(person1);
      getEntityManager().persist(person2);
      TestUtil.logTrace("persisted Persons and Project");

      person1.setProject(project1);
      person2.setProject(project1);

      List<BiDirMX1Person> list = new ArrayList<BiDirMX1Person>();
      list.add(person1);
      list.add(person2);
      project1.setBiDirMX1Persons(list);

      getEntityManager().merge(person1);
      TestUtil.logTrace("merged contents of Person1");
      getEntityManager().merge(person2);
      TestUtil.logTrace("merged contents of Person2");

      getEntityManager().flush();
      getEntityTransaction().commit();

      boolean pass1 = false;
      boolean pass2 = false;

      getEntityTransaction().begin();
      BiDirMX1Project newProject = getEntityManager()
          .find(BiDirMX1Project.class, 1L);
      if (newProject != null) {
        List<BiDirMX1Person> persons = newProject.getBiDirMX1Persons();
        for (BiDirMX1Person person : persons) {
          if (person.getName().equals("Duke")) {
            TestUtil.logTrace("Found Searched Person");
            pass1 = true;
          } else if (person.getName().equals("Foo")) {
            TestUtil.logTrace("Found Searched Person");
            pass2 = true;

          } else {
            TestUtil.logTrace("searched Person not Found");
          }

        }

      } else {
        TestUtil.logTrace("searched Project not Found");

      }

      if (pass1 && pass2) {
        TestUtil.logTrace("biDirMX1Test1: Expected results received");
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
      throw new Exception("biDirMX1Test1 failed");
    }
  }

@AfterAll
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
      getEntityManager().createNativeQuery("DELETE FROM BIDIRMX1PERSON")
          .executeUpdate();
      getEntityManager().createNativeQuery("DELETE FROM BIDIRMX1PROJECT")
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
