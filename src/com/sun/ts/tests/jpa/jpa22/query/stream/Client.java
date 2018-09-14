/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.jpa22.query.stream;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import javax.persistence.*;
import java.sql.Date;

import java.util.*;
import java.util.stream.Stream;

public class Client extends PMClientBase {
  private static final long serialVersionUID = 22L;

  private final Employee empRef[] = new Employee[21];

  private final Date d1 = getSQLDate("2000-02-14");

  final Department deptRef[] = new Department[5];

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
      TestUtil.logErr("Unexpected Exception caught in Setup: ", e);
      throw new Fault("Setup failed:", e);

    }
  }

  /*
   * BEGIN Test Cases
   */

  /*
   * @testName: getResultStreamTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:3487;
   * 
   * @test_Strategy: iterateStream
   */
  public void getResultStreamTest() throws Fault {
    List<Integer> expected = new LinkedList<>();
    for (int i = 1; i != 22; i++)
      expected.add(i);

    Query query = getEntityManager().createQuery("select e.id from Employee e");

    @SuppressWarnings("unchecked")
    List<Integer> ids = query.getResultList();
    assertTrue(ids.size() == 21, "Unexpected result count:" + ids.size());
    assertTrue(checkEntityPK(ids, expected, false), "Unexpected PKs received");

    Stream<?> s1 = query.getResultStream();
    try {
      s1.forEach(this::checkPK);
    } catch (RuntimeException e) {
      throw new Fault(e.getMessage());
    }

    ObjectCounter oc = new ObjectCounter();
    s1 = query.getResultStream();
    s1.forEach(oc::increment);
    assertTrue(oc.getCounter() == 21,
        "Unexpected streamed objects found:" + oc.getCounter());
    TestUtil.logTrace("Query.getResultStream() returned expected ids");
  }

  /*
   * @testName: getTypedResultStreamTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:3490;
   * 
   * @test_Strategy: iterateStream
   */
  public void getTypedResultStreamTest() throws Fault {
    List<Employee> expected = Arrays.asList(empRef);

    TypedQuery<Employee> query = getEntityManager()
        .createQuery("select e from Employee e", Employee.class);

    List<Employee> received = query.getResultList();
    assertTrue(received.size() == 21,
        "Unexpected result count:" + received.size());
    assertTrue(checkEntityPK(received, expected, false),
        "Unexpected PKs received");

    Stream<Employee> s1 = query.getResultStream();
    try {
      s1.forEach(this::checkPK);
    } catch (RuntimeException e) {
      throw new Fault(e.getMessage());
    }

    ObjectCounter oc = new ObjectCounter();
    s1 = query.getResultStream();
    s1.forEach(oc::increment);
    assertTrue(oc.getCounter() == 21,
        "Unexpected streamed objects found:" + oc.getCounter());
    TestUtil.logTrace("TypedQuery.getResultStream() returned expected ids");
  }

  private <T> void checkPK(T t) {
    if (Integer.class.isInstance(t)) {
      checkPK(((Integer) t).intValue());
    } else if (Employee.class.isInstance(t)) {
      checkPK(((Employee) t).getId());
    }
  }

  private void checkPK(int pk) {
    if (pk < 1 || pk > 21)
      throw new RuntimeException("Unexepcted pk: " + pk);
  }

  private void createTestData() throws Exception {
    TestUtil.logTrace("createTestData");

    final Insurance insRef[] = new Insurance[5];
    final Date d2 = getSQLDate("2001-06-27");
    final Date d3 = getSQLDate("2002-07-07");
    final Date d4 = getSQLDate("2003-03-03");
    final Date d5 = getSQLDate("2004-04-10");
    final Date d6 = getSQLDate("2005-02-18");
    final Date d7 = getSQLDate("2000-09-17");
    final Date d8 = getSQLDate("2001-11-14");
    final Date d9 = getSQLDate("2002-10-04");
    final Date d10 = getSQLDate("2003-01-25");
    final Date d11 = getSQLDate();

    try {

      getEntityTransaction().begin();

      // TestUtil.logTrace("Create 5 Departments");
      deptRef[0] = new Department(1, "Engineering");
      deptRef[1] = new Department(2, "Marketing");
      deptRef[2] = new Department(3, "Sales");
      deptRef[3] = new Department(4, "Accounting");
      deptRef[4] = new Department(5, "Training");

      TestUtil.logTrace("Start to persist departments ");
      for (Department d : deptRef) {
        if (d != null) {
          getEntityManager().persist(d);
          TestUtil.logTrace("persisted department " + d);
        }
      }

      // TestUtil.logTrace("Create 3 Insurance Carriers");
      insRef[0] = new Insurance(1, "Prudential");
      insRef[1] = new Insurance(2, "Cigna");
      insRef[2] = new Insurance(3, "Sentry");

      TestUtil.logTrace("Start to persist insurance ");
      for (Insurance i : insRef) {
        if (i != null) {
          getEntityManager().persist(i);
          TestUtil.logTrace("persisted insurance " + i);
        }
      }

      // TestUtil.logTrace("Create 20 employees");
      empRef[0] = new Employee(1, "Alan", "Frechette", d1, (float) 35000.0);
      empRef[0].setDepartment(deptRef[0]);
      empRef[0].setInsurance(insRef[0]);

      empRef[1] = new Employee(2, "Arthur", "Frechette", d2, (float) 35000.0);
      empRef[1].setDepartment(deptRef[1]);
      empRef[1].setInsurance(insRef[1]);

      empRef[2] = new Employee(3, "Shelly", "McGowan", d3, (float) 50000.0);
      empRef[2].setDepartment(deptRef[2]);
      empRef[2].setInsurance(insRef[2]);

      empRef[3] = new Employee(4, "Robert", "Bissett", d4, (float) 55000.0);
      empRef[3].setDepartment(deptRef[3]);
      empRef[3].setInsurance(insRef[0]);

      empRef[4] = new Employee(5, "Stephen", "DMilla", d5, (float) 25000.0);
      empRef[4].setDepartment(deptRef[4]);
      empRef[4].setInsurance(insRef[1]);

      empRef[5] = new Employee(6, "Karen", "Tegan", d6, (float) 80000.0);
      empRef[5].setDepartment(deptRef[0]);
      empRef[5].setInsurance(insRef[2]);

      empRef[6] = new Employee(7, "Stephen", "Cruise", d7, (float) 90000.0);
      empRef[6].setDepartment(deptRef[1]);
      empRef[6].setInsurance(insRef[0]);

      empRef[7] = new Employee(8, "Irene", "Caruso", d8, (float) 20000.0);
      empRef[7].setDepartment(deptRef[2]);
      empRef[7].setInsurance(insRef[1]);

      empRef[8] = new Employee(9, "William", "Keaton", d9, (float) 35000.0);
      empRef[8].setDepartment(deptRef[3]);
      empRef[8].setInsurance(insRef[2]);

      empRef[9] = new Employee(10, "Kate", "Hudson", d10, (float) 20000.0);
      empRef[9].setDepartment(deptRef[4]);
      empRef[9].setInsurance(insRef[0]);

      empRef[10] = new Employee(11, "Jonathan", "Smith", d10, 40000.0F);
      empRef[10].setDepartment(deptRef[0]);
      empRef[10].setInsurance(insRef[1]);

      empRef[11] = new Employee(12, "Mary", "Macy", d9, 40000.0F);
      empRef[11].setDepartment(deptRef[1]);
      empRef[11].setInsurance(insRef[2]);

      empRef[12] = new Employee(13, "Cheng", "Fang", d8, 40000.0F);
      empRef[12].setDepartment(deptRef[2]);
      empRef[12].setInsurance(insRef[0]);

      empRef[13] = new Employee(14, "Julie", "OClaire", d7, 60000.0F);
      empRef[13].setDepartment(deptRef[3]);
      empRef[13].setInsurance(insRef[1]);

      empRef[14] = new Employee(15, "Steven", "Rich", d6, 60000.0F);
      empRef[14].setDepartment(deptRef[4]);
      empRef[14].setInsurance(insRef[2]);

      empRef[15] = new Employee(16, "Kellie", "Lee", d5, 60000.0F);
      empRef[15].setDepartment(deptRef[0]);
      empRef[15].setInsurance(insRef[0]);

      empRef[16] = new Employee(17, "Nicole", "Martin", d4, 60000.0F);
      empRef[16].setDepartment(deptRef[1]);
      empRef[16].setInsurance(insRef[1]);

      empRef[17] = new Employee(18, "Mark", "Francis", d3, 60000.0F);
      empRef[17].setDepartment(deptRef[2]);
      empRef[17].setInsurance(insRef[2]);

      empRef[18] = new Employee(19, "Will", "Forrest", d2, 60000.0F);
      empRef[18].setDepartment(deptRef[3]);
      empRef[18].setInsurance(insRef[0]);

      empRef[19] = new Employee(20, "Katy", "Hughes", d1, 60000.0F);
      empRef[19].setDepartment(deptRef[4]);
      empRef[19].setInsurance(insRef[1]);

      empRef[20] = new Employee(21, "Jane", "Smmith", d11, 60000.0F);
      empRef[20].setDepartment(deptRef[0]);
      empRef[20].setInsurance(insRef[2]);

      // TestUtil.logTrace("Start to persist employees ");
      for (Employee e : empRef) {
        if (e != null) {
          getEntityManager().persist(e);
          TestUtil.logTrace("persisted employee " + e);
        }
      }

      getEntityTransaction().commit();
      TestUtil.logTrace("Created TestData");

    } catch (Exception re) {
      TestUtil.logErr("Unexpected Exception in createTestData:", re);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr(
            "Unexpected Exception in createTestData while rolling back TX:",
            re);
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
      getEntityManager().createNativeQuery("DELETE FROM EMPLOYEE")
          .executeUpdate();
      getEntityManager().createNativeQuery("DELETE FROM DEPARTMENT")
          .executeUpdate();
      getEntityManager().createNativeQuery("DELETE FROM INSURANCE")
          .executeUpdate();
      getEntityManager().createNativeQuery("DELETE FROM DATATYPES2")
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

  private void assertTrue(boolean condition, String message) throws Fault {
    if (!condition)
      throw new Fault(message);
  }

  static class ObjectCounter {
    int counter = 0;

    void increment(Object o) {
      counter++;
    }

    int getCounter() {
      return counter;
    }
  }
}
