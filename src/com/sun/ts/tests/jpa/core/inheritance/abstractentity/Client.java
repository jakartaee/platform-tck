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

package com.sun.ts.tests.jpa.core.inheritance.abstractentity;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class Client extends PMClientBase {

  private static final FullTimeEmployee ftRef[] = new FullTimeEmployee[5];

  private static final PartTimeEmployee ptRef[] = new PartTimeEmployee[5];

  private final Date d1 = getSQLDate(2000, 2, 14);

  private final Date d2 = getSQLDate(2001, 6, 27);

  private final Date d3 = getSQLDate(2002, 7, 7);

  private final Date d4 = getSQLDate(2003, 3, 3);

  private final Date d5 = getSQLDate(2004, 4, 10);

  private final Date d6 = getSQLDate(2005, 2, 18);

  private final Date d7 = getSQLDate(2000, 9, 17);

  private final Date d8 = getSQLDate(2001, 11, 14);

  private final Date d9 = getSQLDate(2002, 10, 4);

  private final Date d10 = getSQLDate(2003, 1, 25);

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
      createTestData();
      TestUtil.logTrace("Done creating test data");

    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  /*
   * @testName: abstractEntityTest1
   * 
   * @assertion_ids: PERSISTENCE:SPEC:589; PERSISTENCE:SPEC:590;
   * PERSISTENCE:SPEC:591; PERSISTENCE:SPEC:588; PERSISTENCE:SPEC:1126;
   * PERSISTENCE:SPEC:1126.1; PERSISTENCE:SPEC:1126.3; PERSISTENCE:SPEC:1126.4;
   * PERSISTENCE:JAVADOC:25; PERSISTENCE:JAVADOC:26; PERSISTENCE:JAVADOC:28;
   * PERSISTENCE:SPEC:1112; PERSISTENCE:SPEC:1113; PERSISTENCE:SPEC:1116;
   * PERSISTENCE:SPEC:1118; PERSISTENCE:JAVADOC:23; PERSISTENCE:JAVADOC:86;
   * PERSISTENCE:JAVADOC:87
   * 
   * @test_Strategy: An entity may have a non-entity superclass which may be
   * either abstract or concrete.
   */

  public void abstractEntityTest1() throws Fault {

    TestUtil.logTrace("Begin abstractEntityTest1");
    boolean pass = false;

    try {
      FullTimeEmployee ftEmp1 = getEntityManager().find(FullTimeEmployee.class,
          1);

      if (ftEmp1.getFullTimeRep().equals("Mabel Murray")) {
        pass = true;
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass)
      throw new Fault("abstractEntityTest1 failed");
  }

  /*
   * @testName: abstractEntityTest2
   * 
   * @assertion_ids: PERSISTENCE:SPEC:589; PERSISTENCE:SPEC:590;
   * PERSISTENCE:SPEC:591; PERSISTENCE:SPEC:588; PERSISTENCE:SPEC:1126;
   * PERSISTENCE:SPEC:1126.1; PERSISTENCE:SPEC:1126.3; PERSISTENCE:SPEC:1126.4;
   * PERSISTENCE:JAVADOC:25; PERSISTENCE:JAVADOC:26; PERSISTENCE:JAVADOC:28;
   * PERSISTENCE:SPEC:1112; PERSISTENCE:SPEC:1113; PERSISTENCE:SPEC:1116;
   * PERSISTENCE:SPEC:1118; PERSISTENCE:JAVADOC:35
   * 
   * @test_Strategy: An entity may have a non-entity superclass which may be
   * either abstract or concrete.
   */

  public void abstractEntityTest2() throws Fault {

    TestUtil.logTrace("Begin abstractEntityTest2");
    boolean pass = false;

    try {
      PartTimeEmployee ptEmp1 = getEntityManager().find(PartTimeEmployee.class,
          6);

      if (ptEmp1.getPartTimeRep().equals("John Cleveland")) {
        pass = true;
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass)
      throw new Fault("abstractEntityTest2 failed");
  }

  /*
   * @testName: abstractEntityTest3
   * 
   * @assertion_ids: PERSISTENCE:SPEC:588.1;PERSISTENCE:SPEC:1352;
   * PERSISTENCE:SPEC:1353; PERSISTENCE:SPEC:1219;
   * 
   * @test_Strategy: An abstract entity is mapped as an entity and can be the
   * target of queries (which will operate over and/or retrieve instances of its
   * concrete subclasses).
   */

  public void abstractEntityTest3() throws Fault {

    TestUtil.logTrace("Begin abstractEntityTest3");
    boolean pass1 = true;
    boolean pass2 = true;
    List result;
    int foundEmp = 0;

    try {
      // getEntityTransaction().begin();
      TestUtil.logTrace("find Employees By ID");
      result = getEntityManager()
          .createQuery(
              "Select e from Employee e where e.id between :param1 and :param2")
          .setParameter("param1", 4).setParameter("param2", 7).setMaxResults(10)
          .getResultList();

      if (result.size() != 4) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 4 references, got: "
                + result.size());
        pass1 = false;
      } else if (pass1) {
        Iterator i = result.iterator();
        while (i.hasNext()) {
          TestUtil.logTrace("Check List for Expected Employees");
          Employee e = (Employee) i.next();
          TestUtil.logTrace("Got Employee: " + e.getLastName());

          if (e.getLastName().equals("Lee") || e.getLastName().equals("Martin")
              || e.getLastName().equals("OClaire")
              || e.getLastName().equals("Rich")) {
            foundEmp++;
          }
        }
      }

      if (foundEmp != 4) {
        TestUtil.logErr("Did not get expected results");
        pass2 = false;
      } else {
        TestUtil.logTrace("Expected results received");
      }

      // getEntityTransaction().commit();

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

    if (!pass1 || !pass2)
      throw new Fault("abstractEntityTest3 failed");
  }

  public void createTestData() {
    TestUtil.logTrace("createTestData");

    try {
      getEntityTransaction().begin();
      ftRef[0] = new FullTimeEmployee(1, "Jonathan", "Smith", d10, 40000.0F);
      ftRef[1] = new FullTimeEmployee(2, "Mary", "Macy", d9, 40000.0F);
      ftRef[2] = new FullTimeEmployee(3, "Sid", "Nee", d8, 40000.0F);
      ftRef[3] = new FullTimeEmployee(4, "Julie", "OClaire", d7, 21000.0F);
      ftRef[4] = new FullTimeEmployee(5, "Steven", "Rich", d6, 60000.0F);

      TestUtil.logTrace("Persist full time employees ");
      for (FullTimeEmployee ft : ftRef) {
        if (ft != null) {
          getEntityManager().persist(ft);
          getEntityManager().flush();
          TestUtil.logTrace("persisted employee " + ft);
        }
      }

      ptRef[0] = new PartTimeEmployee(6, "Kellie", "Lee", d5, 60000.0F);
      ptRef[1] = new PartTimeEmployee(7, "Nicole", "Martin", d4, 60000.0F);
      ptRef[2] = new PartTimeEmployee(8, "Mark", "Francis", d3, 60000.0F);
      ptRef[3] = new PartTimeEmployee(9, "Will", "Forrest", d2, 250000.0F);
      ptRef[4] = new PartTimeEmployee(10, "Katy", "Hughes", d1, 60000.0F);

      TestUtil.logTrace("Persist part time employees ");
      for (PartTimeEmployee pt : ptRef) {
        if (pt != null) {
          getEntityManager().persist(pt);
          getEntityManager().flush();
          TestUtil.logTrace("persisted employee " + pt);
        }
      }
      getEntityTransaction().commit();

      clearCache();

    } catch (Exception re) {
      TestUtil.logErr("Unexpected Exception in createTestData:", re);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception in rollback:", re);
      }
    }
    TestUtil.logTrace("done creating test data ");

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
      getEntityManager().createNativeQuery("DELETE FROM EMPLOYEE")
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
