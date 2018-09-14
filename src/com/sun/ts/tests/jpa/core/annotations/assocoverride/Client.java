/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.core.annotations.assocoverride;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

public class Client extends PMClientBase {

  private static PartTimeEmployee ptRef[] = new PartTimeEmployee[5];

  private static Address aRef[] = new Address[5];

  final private Date d1 = getSQLDate(2000, 2, 14);

  final private Date d2 = getSQLDate(2001, 6, 27);

  final private Date d3 = getSQLDate(2002, 7, 7);

  final private Date d4 = getSQLDate(2003, 3, 3);

  final private Date d5 = getSQLDate(2004, 4, 10);

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
   * @testName: associationOverride
   * 
   * @assertion_ids: PERSISTENCE:SPEC:593; PERSISTENCE:SPEC:596;
   * PERSISTENCE:SPEC:597; PERSISTENCE:SPEC:598; PERSISTENCE:SPEC:599;
   * PERSISTENCE:SPEC:1130; PERSISTENCE:SPEC:1131; PERSISTENCE:SPEC:1132;
   * PERSISTENCE:SPEC:1133; PERSISTENCE:SPEC:1061; PERSISTENCE:SPEC:1062;
   * PERSISTENCE:JAVADOC:4; PERSISTENCE:JAVADOC:5; PERSISTENCE:JAVADOC:6;
   * PERSISTENCE:JAVADOC:2; PERSISTENCE:JAVADOC:1; PERSISTENCE:SPEC:1964;
   * PERSISTENCE:SPEC:1965; PERSISTENCE:SPEC:1969;
   * 
   * @test_Strategy: An entity may have a mapped superclass which provides
   * persistent entity state and mapping information. Use AssociationOverride
   * annotation
   */
  public void associationOverride() throws Fault {

    TestUtil.logTrace("Begin AssociationOverride");
    boolean pass = false;

    try {
      PartTimeEmployee ptEmp1 = getEntityManager().find(PartTimeEmployee.class,
          6);

      if (ptEmp1.getAddress().getStreet().equals("1 Network Drive")) {
        pass = true;
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass) {
      throw new Fault("test2 failed");
    }
  }

  public void createTestData() {

    try {

      getEntityTransaction().begin();

      ptRef[0] = new PartTimeEmployee(6, "Kellie", "Lee", d5, 60000.0F);
      ptRef[1] = new PartTimeEmployee(7, "Nicole", "Martin", d4, 60000.0F);
      ptRef[2] = new PartTimeEmployee(8, "Mark", "Francis", d3, 60000.0F);
      ptRef[3] = new PartTimeEmployee(9, "Will", "Forrest", d2, 60000.0F);
      ptRef[4] = new PartTimeEmployee(10, "Katy", "Hughes", d1, 60000.0F);

      // create Addresses
      aRef[0] = new Address("100", "1 Network Drive", "Burlington", "MA",
          "01803");
      aRef[1] = new Address("200", "4150 Network Drive", "Santa Clara", "CA",
          "95054");
      aRef[2] = new Address("300", "2 Network Drive", "Burlington", "MA",
          "01803");
      aRef[3] = new Address("400", "5150 Network Drive", "Santa Clara", "CA",
          "95054");
      aRef[4] = new Address("500", "3 Network Drive", "Burlington", "MA",
          "01803");

      // Set Addresse to Employee
      ptRef[0].setAddress(aRef[0]);
      ptRef[1].setAddress(aRef[1]);
      ptRef[2].setAddress(aRef[2]);
      ptRef[3].setAddress(aRef[3]);
      ptRef[4].setAddress(aRef[4]);

      TestUtil.logTrace("Persist part time employees ");
      for (int i = 0; i < 5; i++) {
        getEntityManager().persist(aRef[i]);
        TestUtil.logTrace("persisted Address " + aRef[i]);
        getEntityManager().persist(ptRef[i]);
        TestUtil.logTrace("persisted employee " + ptRef[i]);
      }
      getEntityTransaction().commit();
    } catch (Exception re) {
      TestUtil.logErr("Unexpected Exception creating test data:", re);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception rolling back TX:", re);
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
      getEntityManager().createNativeQuery("Delete from EMPLOYEE")
          .executeUpdate();
      getEntityManager().createNativeQuery("Delete from PARTTIMEEMPLOYEE")
          .executeUpdate();
      getEntityManager().createNativeQuery("Delete from ADDRESS")
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
