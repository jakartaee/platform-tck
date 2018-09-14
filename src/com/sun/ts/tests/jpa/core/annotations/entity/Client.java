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

package com.sun.ts.tests.jpa.core.annotations.entity;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class Client extends PMClientBase {

  private static Coffee cRef[] = new Coffee[5];

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
      TestUtil.logTrace("Create Test data");
      createTestData();
      TestUtil.logTrace("Done creating test data");

    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  /*
   * @testName: annotationEntityTest1
   * 
   * @assertion_ids: PERSISTENCE:SPEC:993; PERSISTENCE:SPEC:995;
   * PERSISTENCE:JAVADOC:29; PERSISTENCE:SPEC:762; PERSISTENCE:SPEC:402;
   * PERSISTENCE:SPEC:404;
   * 
   * @test_Strategy: The name annotation element defaults to the unqualified
   * name of the entity class. This name is used to refer to the entities in
   * queries.
   * 
   * Name the entity using a lower case name and ensure the query can be
   * executed with the lower case entity name as the abstract schema name.
   * 
   */

  public void annotationEntityTest1() throws Fault {

    TestUtil.logTrace("Begin annotationEntityTest1");
    boolean pass = true;
    List c = null;

    try {
      getEntityTransaction().begin();
      final String[] expectedBrands = new String[] { "vanilla creme", "mocha",
          "hazelnut", "decaf", "breakfast blend" };

      TestUtil.logTrace("find coffees by brand name");
      c = getEntityManager()
          .createQuery(
              "Select c.brandName from cof c ORDER BY c.brandName DESC")
          .setMaxResults(10).getResultList();

      final String[] result = (String[]) (c.toArray(new String[c.size()]));
      TestUtil.logTrace("Compare results of Coffee Brand Names");
      pass = Arrays.equals(expectedBrands, result);

      if (!pass) {
        TestUtil.logErr("Did not get expected results.  Expected 5 Coffees : "
            + "vanilla creme, mocha, hazelnut, decaf, breakfast blend. "
            + " Received: " + c.size());
        Iterator it = c.iterator();
        while (it.hasNext()) {
          TestUtil.logTrace(" Coffee Brand Name: " + it.next());
        }
      } else {
        TestUtil.logTrace("Expected results received");
      }

      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception in rollback:", re);
      }
    }

    if (!pass)
      throw new Fault("annotationEntityTest1 failed");
  }

  /*
   * @testName: annotationEntityTest2
   * 
   * @assertion_ids: PERSISTENCE:SPEC:993; PERSISTENCE:SPEC:995;
   * PERSISTENCE:JAVADOC:29
   * 
   * @test_Strategy: The name annotation element defaults to the unqualified
   * name of the entity class. This name is used to refer to the entities in
   * queries.
   * 
   * Name the entity using a different name than the entity class name and
   * ensure the query can be executed with the lower case entity name as the
   * abstract schema name selecting teh
   * 
   */

  public void annotationEntityTest2() throws Fault {

    TestUtil.logTrace("Begin annotationEntityTest2");
    boolean pass1 = true;
    boolean pass2 = false;
    List c = null;

    try {
      getEntityTransaction().begin();
      final Integer[] expectedPKs = new Integer[] { 21, 22, 23, 24, 25 };

      TestUtil.logTrace("find all coffees");
      c = getEntityManager().createQuery("Select c from cof c")
          .setMaxResults(10).getResultList();

      if (c.size() != 5) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 5 references, got: "
                + c.size());
        pass1 = false;
      } else if (pass1) {
        TestUtil.logTrace("Expected size received, verify contents . . . ");
        Iterator i = c.iterator();
        int foundCof = 0;
        while (i.hasNext()) {
          TestUtil.logTrace("Check List for expected coffees");
          Coffee o = (Coffee) i.next();
          for (int l = 0; l < 5; l++) {
            if (expectedPKs[l].equals(o.getId())) {
              TestUtil.logTrace("Found coffee with PK: " + o.getId());
              foundCof++;
              break;
            }
          }
        }
        if (foundCof != 5) {
          TestUtil.logErr("anotationEntityTest2: Did not get expected results");
          pass2 = false;
        } else {
          TestUtil.logTrace("Expected results received");
          pass2 = true;
        }
      }

      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass2 = false;
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception in rollback:", re);
      }
    }

    if (!pass1 || !pass2)
      throw new Fault("annotationEntityTest1 failed");
  }

  /*
   * 
   * Business Methods to set up data for Test Cases
   */

  private void createTestData() throws Exception {
    try {

      TestUtil.logTrace("createTestData");

      getEntityTransaction().begin();
      TestUtil.logTrace("Create 5 Coffees");
      cRef[0] = new Coffee(21, "hazelnut", 1.0F);
      cRef[1] = new Coffee(22, "vanilla creme", 2.0F);
      cRef[2] = new Coffee(23, "decaf", 3.0F);
      cRef[3] = new Coffee(24, "breakfast blend", 4.0F);
      cRef[4] = new Coffee(25, "mocha", 5.0F);

      TestUtil.logTrace("Start to persist coffees ");
      for (Coffee coffee : cRef) {
        getEntityManager().persist(coffee);
        TestUtil.logTrace("persisted coffee " + coffee);
      }
      getEntityManager().flush();
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception creating test data:", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception in rollback:", re);
      }
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
      getEntityManager().createNativeQuery("DELETE FROM COFFEE")
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
