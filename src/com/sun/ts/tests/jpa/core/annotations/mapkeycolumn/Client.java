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

package com.sun.ts.tests.jpa.core.annotations.mapkeycolumn;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import java.util.*;

public class Client extends PMClientBase {

  public Client() {
  }

  private static Employee empRef[] = new Employee[10];

  private static Department deptRef[] = new Department[5];

  private static Department2 deptRef2[] = new Department2[5];

  public Map<String, Employee> link = new HashMap<String, Employee>();

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
   * @testName: annotationMapKeyColumnTest1
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:114; PERSISTENCE:SPEC:1100;
   * PERSISTENCE:SPEC:1101; PERSISTENCE:SPEC:518; PERSISTENCE:SPEC:1202;
   * PERSISTENCE:JAVADOC:90; PERSISTENCE:JAVADOC:92; PERSISTENCE:JAVADOC:96
   *
   * @test_Strategy: The MapKeyColumn annotation is used to specify the mapping
   * for the key column of a map whose map key is a basic type.
   *
   * The name element designates the name of the persistence property or field
   * of the associated entity that is used as the map key.
   *
   * Execute a query returning Employees objects.
   *
   */
  public void annotationMapKeyColumnTest1() throws Fault {

    boolean pass = false;
    List e = null;

    try {
      getEntityTransaction().begin();

      TestUtil.logTrace("find Employees belonging to Department: Marketing");
      e = getEntityManager()
          .createQuery(
              "Select e from Employee e where e.department.name = 'Marketing'")
          .setMaxResults(10).getResultList();

      if (e.size() != 3) {
        TestUtil.logErr("Did not get expected results"
            + "Expected 3 Employees, Received: " + e.size());
      } else {
        TestUtil
            .logTrace("annotationMapKeyColumnTest1: Expected results received. "
                + "Expected 3 Employees, Received: " + e.size());
        pass = true;
      }

      getEntityTransaction().commit();

    } catch (Exception ex) {
      TestUtil.logErr("Unexpected exception occurred", ex);
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
      throw new Fault("annotationMapKeyColumnTest1 failed");
    }
  }

  /*
   * @testName: annotationMapKeyColumnTest2
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:114; PERSISTENCE:SPEC:1100;
   * PERSISTENCE:SPEC:1101; PERSISTENCE:JAVADOC:90; PERSISTENCE:JAVADOC:92;
   * PERSISTENCE:JAVADOC:96
   * 
   * @test_Strategy: The MapKeyColumn annotation is used to specify the mapping
   * for the key column of a map whose map key is a basic type.
   *
   * The name element designates the name of the persistence property or field
   * of the associated entity that is used as the map key.
   *
   * Execute a query returning Employee IDs.
   */
  public void annotationMapKeyColumnTest2() throws Fault {

    boolean pass = false;
    List e = null;

    try {
      getEntityTransaction().begin();
      final Integer[] expectedEmps = new Integer[] { 4, 2 };

      TestUtil.logTrace("find Employees belonging to Department: Marketing");
      e = getEntityManager().createQuery(
          "Select e.id from Employee e where e.department.name = 'Administration' ORDER BY e.id DESC")
          .setMaxResults(10).getResultList();

      final Integer[] result = (Integer[]) (e.toArray(new Integer[e.size()]));
      TestUtil.logTrace("Compare results of Employee Ids ");
      pass = Arrays.equals(expectedEmps, result);

      if (!pass) {
        TestUtil.logErr("Did not get expected results.  Expected 2 Employees : "
            + " Received: " + e.size());
        Iterator it = e.iterator();
        while (it.hasNext()) {
          TestUtil.logTrace(" Employee PK : " + it.next());
        }
      } else {
        TestUtil.logTrace("Expected results received");
      }

      getEntityTransaction().commit();

    } catch (Exception ex) {
      TestUtil.logErr("Unexpected exception occurred", ex);
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

    if (!pass) {
      throw new Fault("annotationMapKeyColumnTest2 failed");
    }
  }

  /*
   * @testName: annotationMapKeyColumnTest3
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:352; PERSISTENCE:JAVADOC:353;
   * PERSISTENCE:JAVADOC:354; PERSISTENCE:JAVADOC:355; PERSISTENCE:JAVADOC:359;
   *
   * @test_Strategy: The MapKeyColumn annotation is used to specify the mapping
   * for the key column of a map whose map key is a basic type.
   *
   * The name element designates the name of the persistence property or field
   * of the associated entity that is used as the map key.
   *
   * Execute a query returning Employees objects.
   */
  public void annotationMapKeyColumnTest3() throws Fault {
    boolean pass = false;

    List<Integer> expected = new ArrayList<Integer>();
    expected.add(0);
    expected.add(2);
    expected.add(4);

    List<Integer> actual = new ArrayList<Integer>();
    try {
      getEntityTransaction().begin();
      Employee emp = getEntityManager().find(Employee.class, 1);
      TestUtil.logTrace("Name:" + emp.getFirstName() + " " + emp.getLastName());
      Department dept = emp.getDepartment();
      TestUtil.logTrace("Dept=" + dept.getName());
      Map<String, Employee> emps = dept.getLastNameEmployees();
      if (emps.size() == 3) {
        TestUtil.logTrace("number of employees=" + emps.size());
        for (Map.Entry<String, Employee> entry : emps.entrySet()) {
          TestUtil.logTrace("id=" + entry.getValue().getId() + ", Name="
              + entry.getValue().getFirstName() + " "
              + entry.getValue().getLastName());
          actual.add(entry.getValue().getId() - 1);
        }

        Collections.sort(actual);
        if (expected.equals(actual)) {
          TestUtil.logTrace("Received expected employees");
          pass = true;
        } else {
          TestUtil.logErr("Did not get correct employees");
          TestUtil.logErr("Expected:");
          for (Integer i : expected) {
            TestUtil.logTrace("id=" + empRef[i].getId() + ", Name="
                + empRef[i].getFirstName() + " " + empRef[i].getLastName());
          }
          TestUtil.logErr("Actual:");
          for (Integer i : actual) {
            TestUtil.logTrace("id=" + empRef[i].getId() + ", Name="
                + empRef[i].getFirstName() + " " + empRef[i].getLastName());
          }
        }

      } else {
        TestUtil.logErr("Expected 3 employees, actual:" + emps.size());
      }
      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }

    if (!pass) {
      throw new Fault("annotationMapKeyColumnTest3 Failed");
    }

  }

  /*
   * @testName: mapKeyColumnInsertableFalseTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:352
   *
   * @test_Strategy: The MapKeyColumn annotation with an attribute of
   * insertable=false is used to specify the mapping for the fk column to a
   * second entity Execute a query returning Employees objects.
   */
  public void mapKeyColumnInsertableFalseTest() throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      clearCache();
      Department2 dept = getEntityManager().find(Department2.class, 3);
      if (dept != null) {
        TestUtil.logTrace("Dept=" + dept.getName());
        Map<String, Employee> emps = dept.getLastNameEmployees();
        if (emps.size() == 0) {
          TestUtil.logTrace("Received expected number of employees");
          pass = true;
        } else {
          TestUtil.logErr("Expected 0 employees, actual:" + emps.size());
          TestUtil.logErr("Actual:");
          for (Map.Entry<String, Employee> entry : emps.entrySet()) {
            TestUtil.logErr("id=" + entry.getValue().getId() + ", Name="
                + entry.getValue().getFirstName() + " "
                + entry.getValue().getLastName());
          }
        }
      } else {
        TestUtil.logErr("Department2 returned was null");
      }
      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }

    if (!pass) {
      throw new Fault("mapKeyColumnInsertableFalseTest Failed");
    }

  }

  /*
   * @testName: mapKeyColumnUpdatableFalseTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:360
   *
   * @test_Strategy: The MapKeyColumn annotation with an attribute of
   * updatable=false is used to specify the mapping for the fk column to a
   * second entity Execute a query returning Employees objects.
   */
  public void mapKeyColumnUpdatableFalseTest() throws Fault {
    boolean pass = false;

    try {
      getEntityTransaction().begin();
      clearCache();

      // department 3
      TestUtil.logTrace("find Department");
      Department2 dept = getEntityManager().find(Department2.class,
          deptRef2[0].getId());
      TestUtil.logTrace("Dept=" + dept.getName());
      link = new HashMap<String, Employee>();
      link.put("OFF-006", empRef[6]);
      TestUtil.logTrace("set last names of employees and save");
      dept.setLastNameEmployees(link);
      getEntityManager().merge(dept);
      getEntityManager().flush();
      clearCache();
      TestUtil.logTrace("find Department again");
      dept = getEntityManager().find(Department2.class, deptRef2[0].getId());
      Map<String, Employee> emps = dept.getLastNameEmployees();
      if (emps.size() == 0) {
        TestUtil
            .logTrace("Received expected number of employees for department: "
                + deptRef2[0].getId());
        pass = true;
      } else {
        TestUtil.logErr("Expected 0 employees, actual:" + emps.size());
        TestUtil.logErr("Actual:");
        for (Map.Entry<String, Employee> entry : emps.entrySet()) {
          TestUtil.logErr("id=" + entry.getValue().getId() + ", Name="
              + entry.getValue().getFirstName() + " "
              + entry.getValue().getLastName());
        }
      }

      getEntityTransaction().commit();

    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass) {
      throw new Fault("mapKeyColumnUpdatableFalseTest Failed");
    }

  }

  /*
   * @testName: criteriaBuilderKeysValuesTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:791; PERSISTENCE:JAVADOC:875
   *
   * @test_Strategy:
   *
   */
  public void criteriaBuilderKeysValuesTest() throws Fault {

    boolean pass1 = false;
    boolean pass2 = false;
    Set<String> expected = new HashSet<String>();
    expected.add("OFF-000");
    expected.add("OFF-002");
    expected.add("OFF-004");

    try {
      getEntityTransaction().begin();
      Employee emp = getEntityManager().find(Employee.class, 1);
      System.out
          .println("Name:" + emp.getFirstName() + " " + emp.getLastName());
      Department dept = emp.getDepartment();
      System.out.println("Dept=" + dept.getName());
      Map<String, Employee> emps = dept.getLastNameEmployees();
      if (TestUtil.traceflag) {
        for (Map.Entry<String, Employee> entry : emps.entrySet()) {
          TestUtil.logTrace("map:" + entry.getKey() + ", "
              + entry.getValue().getId() + " " + entry.getValue().getFirstName()
              + " " + entry.getValue().getLastName());
        }
      }
      Set<String> keys = emps.keySet();
      for (String key : keys) {
        TestUtil.logTrace("key:" + key);
      }
      if (expected.containsAll(keys) && keys.containsAll(expected)
          && expected.size() == keys.size()) {
        TestUtil.logTrace("Received expected keys");
        pass1 = true;
      } else {
        TestUtil.logErr("Did not received expected keys");
        TestUtil.logErr("Expected:");
        for (String key : expected) {
          TestUtil.logTrace("key:" + key);
        }
        TestUtil.logErr("Actual:");
        for (String key : keys) {
          TestUtil.logTrace("key:" + key);
        }
      }

      Set<Employee> sExpected = new HashSet<Employee>();
      sExpected.add(empRef[0]);
      sExpected.add(empRef[2]);
      sExpected.add(empRef[4]);

      Collection<Employee> employees = emps.values();
      for (Employee e : employees) {
        TestUtil.logTrace("values:" + e.getId() + " " + e.getFirstName() + " "
            + e.getLastName());
      }
      if (sExpected.containsAll(employees) && employees.containsAll(sExpected)
          && sExpected.size() == employees.size()) {
        TestUtil.logTrace("Received expected values");
        pass2 = true;
      } else {
        TestUtil.logErr("Did not received expected values");
        TestUtil.logErr("Expected:");
        for (Employee e : sExpected) {
          TestUtil.logTrace("Employee:" + e.getId() + " " + e.getFirstName()
              + " " + e.getLastName());
        }
        TestUtil.logErr("Actual:");
        for (Employee e : employees) {
          TestUtil.logTrace("Employee:" + e.getId() + " " + e.getFirstName()
              + " " + e.getLastName());
        }
      }
      getEntityTransaction().commit();

    } catch (Exception ex) {
      TestUtil.logErr("Unexpected exception occurred", ex);
    }

    if (!pass1 || !pass2) {
      throw new Fault("criteriaBuilderKeysValuesTest failed");
    }
  }

  /*
   * Business Methods to set up data for Test Cases
   */
  private void createTestData() throws Exception {
    TestUtil.logTrace("createTestData");
    try {
      getEntityTransaction().begin();

      TestUtil.logTrace("Create 2 Departments");
      deptRef[0] = new Department(1, "Marketing");
      deptRef[1] = new Department(2, "Administration");

      TestUtil.logTrace("Start to persist departments ");
      for (Department dept : deptRef) {
        if (dept != null) {
          getEntityManager().persist(dept);
          TestUtil.logTrace("persisted department " + dept.getName());
        }
      }

      TestUtil.logTrace("Create 2 Department2 ");
      deptRef2[0] = new Department2(3, "IT");

      TestUtil.logTrace("Start to persist Department2 ");
      for (Department2 dept : deptRef2) {
        if (dept != null) {
          getEntityManager().persist(dept);
          TestUtil.logTrace("persisted department " + dept.getName());
        }
      }

      TestUtil.logTrace("Create 5 employees");
      empRef[0] = new Employee(1, "Alan", "Frechette");
      empRef[0].setDepartment(deptRef[0]);

      empRef[1] = new Employee(2, "Arthur", "Frechette");
      empRef[1].setDepartment(deptRef[1]);

      empRef[2] = new Employee(3, "Shelly", "McGowan");
      empRef[2].setDepartment(deptRef[0]);

      empRef[3] = new Employee(4, "Robert", "Bissett");
      empRef[3].setDepartment(deptRef[1]);

      empRef[4] = new Employee(5, "Stephen", "DMilla");
      empRef[4].setDepartment(deptRef[0]);

      link.put("OFF-000", empRef[0]);
      link.put("OFF-002", empRef[2]);
      link.put("OFF-004", empRef[4]);
      deptRef[0].setLastNameEmployees(link);

      link = new HashMap<String, Employee>();
      link.put("OFF-001", empRef[1]);
      link.put("OFF-003", empRef[3]);
      deptRef[1].setLastNameEmployees(link);

      link = new HashMap<String, Employee>();
      link.put("OFF-005", empRef[5]);
      deptRef2[0].setLastNameEmployees(link);

      TestUtil.logTrace("Start to persist employees ");
      for (Employee emp : empRef) {
        if (emp != null) {
          getEntityManager().persist(emp);
          TestUtil.logTrace("persisted employee " + emp.getId());
        }
      }

      // Merge Department
      TestUtil.logTrace("Start to Merge department ");
      for (Department dept : deptRef) {
        if (dept != null) {
          getEntityManager().merge(dept);
          TestUtil.logTrace("merged department " + dept.getName());

        }
      }

      // Merge Department2
      TestUtil.logTrace("Start to Merge department ");
      for (Department2 dept : deptRef2) {
        if (dept != null) {
          getEntityManager().merge(dept);
          TestUtil.logTrace("merged department " + dept.getName());

        }
      }

      TestUtil.logTrace("Start to persist employees ");
      for (Employee emp : empRef) {
        if (emp != null) {
          getEntityManager().persist(emp);
          TestUtil.logTrace("persisted employee " + emp.getId());
        }
      }

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
      getEntityManager().createNativeQuery("Delete from EMP_MAPKEYCOL")
          .executeUpdate();
      getEntityManager().createNativeQuery("Delete from DEPARTMENT")
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
