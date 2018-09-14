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

package com.sun.ts.tests.jpa.core.annotations.mapkey;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.SetupMethod;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import java.util.*;

public class Client extends PMClientBase {

  public Client() {
  }

  private Employee empRef[] = new Employee[10];

  private Employee2 empRef2;

  private Employee3 empRef3;

  private Employee4 empRef4;

  private static Department deptRef[] = new Department[5];

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

  public void setupCreateTestData(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup");
    try {
      super.setup(args, p);
      removeTestData();
      createTestData();
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  public void setupCreateTestData2(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup");
    try {
      super.setup(args, p);
      removeTestData();
      createTestData2();
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  /*
   * @testName: annotationMapKeyTest1
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:114; PERSISTENCE:SPEC:1100;
   * PERSISTENCE:SPEC:1101; PERSISTENCE:SPEC:518; PERSISTENCE:SPEC:1980;
   * 
   * @test_Strategy: The MapKey annotation is used to specify the map key for
   * associations of type java.util.Map.
   *
   * The name element designates the name of the persistence property or field
   * of the associated entity that is used as the map key.
   * 
   * Execute a query returning Employees objects.
   * 
   */
  @SetupMethod(name = "setupCreateTestData")
  public void annotationMapKeyTest1() throws Fault {

    boolean pass = true;

    try {
      List<Integer> expected = new ArrayList<Integer>();
      List<Integer> actual = new ArrayList<Integer>();

      expected.add(empRef[0].getId());
      expected.add(empRef[2].getId());
      expected.add(empRef[4].getId());

      getEntityTransaction().begin();

      TestUtil.logTrace("find Employees belonging to Department: Marketing");
      List l = getEntityManager()
          .createQuery(
              "Select e from Employee e where e.department.name = 'Marketing'")
          .getResultList();

      for (Object o : l) {
        Employee e = (Employee) o;
        actual.add(e.getId());
      }

      Collections.sort(actual);
      if (expected.equals(actual)) {
        TestUtil.logTrace("Received expected employees");
      } else {
        TestUtil.logErr("Expected id values were:");
        for (Integer i : expected) {
          TestUtil.logErr("id: " + i);
        }
        TestUtil.logErr("actual id values were:");
        Collections.sort(actual);
        for (Integer i : actual) {
          TestUtil.logErr("id: " + i);
        }
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

    if (!pass)
      throw new Fault("annotationMapKeyTest1 failed");
  }

  /*
   * @testName: annotationMapKeyTest2
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:114; PERSISTENCE:SPEC:1100;
   * PERSISTENCE:SPEC:1101
   * 
   * @test_Strategy: The MapKey annotation is used to specify the map key for
   * associations of type java.util.Map.
   *
   * The name element designates the name of the persistence property or field
   * of the associated entity that is used as the map key.
   *
   * Execute a query returning Employee IDs.
   */
  @SetupMethod(name = "setupCreateTestData")
  public void annotationMapKeyTest2() throws Fault {

    boolean pass = true;

    try {
      List<Integer> expected = new ArrayList<Integer>();
      List<Integer> actual = new ArrayList<Integer>();

      expected.add(empRef[1].getId());
      expected.add(empRef[3].getId());

      getEntityTransaction().begin();

      TestUtil.logTrace("find Employees belonging to Department: Marketing");
      List l = getEntityManager().createQuery(
          "Select e.id from Employee e where e.department.name = 'Administration' ORDER BY e.id DESC")
          .getResultList();

      for (Object o : l) {
        Integer i = (Integer) o;
        actual.add(i);
      }

      Collections.sort(actual);
      if (expected.equals(actual)) {
        TestUtil.logTrace("Received expected employees");
      } else {
        TestUtil.logErr("Expected id values were:");
        for (Integer i : expected) {
          TestUtil.logErr("id: " + i);
        }
        TestUtil.logErr("actual id values were:");
        Collections.sort(actual);
        for (Integer i : actual) {
          TestUtil.logErr("id: " + i);
        }
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

    if (!pass)
      throw new Fault("annotationMapKeyTest2 failed");
  }

  /*
   * @testName: joinColumnInsertable
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:90
   *
   * @test_Strategy: The JoinColumn annotation with an attribute of insertable
   * used to specify the mapping for the fk column to a second entity Execute a
   * query returning Employees objects.
   */
  @SetupMethod(name = "setupCreateTestData2")
  public void joinColumnInsertable() throws Fault {
    boolean pass = true;

    try {
      getEntityTransaction().begin();
      clearCache();
      TestUtil.logTrace("find employee2");
      Employee2 emp2 = getEntityManager().find(Employee2.class, 6);
      TestUtil
          .logTrace("Name:" + emp2.getFirstName() + " " + emp2.getLastName());
      Department dept = emp2.getDepartment();

      if (dept == null) {
        TestUtil.logTrace("Received expected null department for employee2");
      } else {
        pass = false;
        TestUtil.logErr("Expected null department, actual:" + dept.getName());
      }
      clearCache();

      TestUtil.logTrace("--------------");
      TestUtil.logTrace("find employee3");
      Employee3 emp3 = getEntityManager().find(Employee3.class, 7);
      TestUtil
          .logTrace("Name:" + emp3.getFirstName() + " " + emp3.getLastName());
      dept = emp3.getDepartment();

      if (dept != null && dept.getName().equals(deptRef[0].getName())) {
        TestUtil.logTrace(
            "Received expected department for employee3:" + dept.getName());
      } else {
        pass = false;
        if (dept != null) {
          TestUtil.logErr("Expected department:" + deptRef[0].getName()
              + ", actual:" + dept.getName());
        } else {
          TestUtil.logErr(
              "Expected department:" + deptRef[0].getName() + ", actual:null");
        }
      }

      clearCache();
      TestUtil.logTrace("--------------");
      TestUtil.logTrace("find employee4");
      Employee4 emp4 = getEntityManager().find(Employee4.class, 8);
      TestUtil
          .logTrace("Name:" + emp4.getFirstName() + " " + emp4.getLastName());
      dept = emp4.getDepartment();

      if (dept == null) {
        TestUtil.logTrace("Received expected null department for employee4");
      } else {
        pass = false;
        TestUtil.logErr("Expected department: null, actual:" + dept.getName());
      }
      getEntityTransaction().commit();

    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Received unexpected exception", e);
    }

    if (!pass) {
      throw new Fault("joinColumnInsertable Failed");
    }
  }

  /*
   * @testName: joinColumnUpdatable
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:96
   *
   * @test_Strategy: The JoinColumn annotation with an attribute of updatable
   * used to specify the mapping for the fk column to a second entity Execute a
   * query returning Employees objects.
   */
  @SetupMethod(name = "setupCreateTestData2")
  public void joinColumnUpdatable() throws Fault {
    boolean pass = true;

    try {
      getEntityTransaction().begin();
      clearCache();
      TestUtil.logTrace("find employee2");
      Employee2 emp2 = getEntityManager().find(Employee2.class, 6);
      TestUtil
          .logTrace("Name:" + emp2.getFirstName() + " " + emp2.getLastName());
      TestUtil.logTrace("set department to:" + deptRef[1].getId() + ", "
          + deptRef[1].getName());
      emp2.setDepartment(deptRef[1]);
      getEntityManager().merge(emp2);
      getEntityManager().flush();
      clearCache();
      TestUtil.logTrace("find employee2 again");
      emp2 = getEntityManager().find(Employee2.class, 6);
      TestUtil
          .logTrace("Name:" + emp2.getFirstName() + " " + emp2.getLastName());
      Department dept = emp2.getDepartment();
      if (dept == null) {
        TestUtil.logTrace("Received expected null department");
      } else {
        pass = false;
        TestUtil.logErr("Expected null department, actual:" + dept.getName());
      }

      clearCache();
      TestUtil.logTrace("--------------");
      TestUtil.logTrace("find employee3");
      Employee3 emp3 = getEntityManager().find(Employee3.class, 7);
      TestUtil
          .logTrace("Name:" + emp3.getFirstName() + " " + emp3.getLastName());
      TestUtil.logTrace("Department:" + emp3.getDepartment().getId() + ", "
          + emp3.getDepartment().getName());
      TestUtil.logTrace("set department to:" + deptRef[1].getId() + ", "
          + deptRef[1].getName());
      emp3.setDepartment(deptRef[1]);
      getEntityManager().merge(emp3);
      getEntityManager().flush();
      clearCache();
      TestUtil.logTrace("find employee3 again");
      emp3 = getEntityManager().find(Employee3.class, 7);
      TestUtil
          .logTrace("Name:" + emp3.getFirstName() + " " + emp3.getLastName());
      dept = emp3.getDepartment();
      if (dept != null && dept.getName().equals(deptRef[0].getName())) {
        TestUtil.logTrace("Received expected department:" + dept.getName());
      } else {
        pass = false;
        if (dept != null) {
          TestUtil.logErr("Expected department:" + deptRef[0].getName()
              + ", actual:" + dept.getName());
        } else {
          TestUtil.logErr(
              "Expected department:" + deptRef[0].getName() + ", actual:null");
        }
      }

      clearCache();
      TestUtil.logTrace("--------------");
      TestUtil.logTrace("find employee4");
      Employee4 emp4 = getEntityManager().find(Employee4.class, 8);
      TestUtil
          .logTrace("Name:" + emp4.getFirstName() + " " + emp4.getLastName());
      if (emp4.getFirstName() != null) {
        TestUtil.logErr(
            "Expected first name to be null, actual:" + emp4.getFirstName());
        pass = false;
      }
      if (emp4.getDepartment() != null) {
        TestUtil.logErr("Expected Department to be null, actual:"
            + emp4.getDepartment().toString());
        pass = false;
      }
      TestUtil.logTrace("set department to:" + deptRef[1].getId() + ", "
          + deptRef[1].getName());
      emp4.setDepartment(deptRef[1]);
      getEntityManager().merge(emp4);
      getEntityManager().flush();
      clearCache();
      TestUtil.logTrace("find employee4 again");
      emp4 = getEntityManager().find(Employee4.class, 8);
      TestUtil
          .logTrace("Name:" + emp4.getFirstName() + " " + emp4.getLastName());
      dept = emp4.getDepartment();
      if (dept != null && dept.getName().equals(deptRef[1].getName())) {
        TestUtil.logTrace("Received expected department:" + dept.getName());
      } else {
        pass = false;
        if (dept != null) {
          TestUtil.logErr("Expected " + deptRef[1].getName()
              + " department, actual:" + dept.getName());
        } else {
          TestUtil.logErr(
              "Expected " + deptRef[1].getName() + " department, actual:null");
        }
      }

      getEntityTransaction().commit();

    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Received unexpected exception", e);
    }

    if (!pass) {
      throw new Fault("joinColumnUpdatable Failed");
    }
  }

  /*
   * @testName: columnInsertable
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:12
   *
   * @test_Strategy: The JoinColumn annotation with an attribute of insertable
   * used to specify the mapping for the fk column to a second entity Execute a
   * query returning Employees objects.
   */
  @SetupMethod(name = "setupCreateTestData2")
  public void columnInsertable() throws Fault {
    boolean pass = true;

    try {

      getEntityTransaction().begin();
      clearCache();
      TestUtil.logTrace("find employee2");
      Employee2 emp2 = getEntityManager().find(Employee2.class, 6);
      String firstName = emp2.getFirstName();
      TestUtil.logTrace("Name:" + firstName + " " + emp2.getLastName());

      if (firstName == null) {
        TestUtil.logTrace("Received expected null firstName");
      } else {
        pass = false;
        TestUtil.logErr("Expected firstName: null, actual:" + firstName);
      }

      clearCache();
      TestUtil.logTrace("--------------");
      TestUtil.logTrace("find employee3");
      Employee3 emp3 = getEntityManager().find(Employee3.class, 7);
      firstName = emp3.getFirstName();
      TestUtil.logTrace("Name:" + firstName + " " + emp3.getLastName());

      if (firstName != null && firstName.equals("Paul")) {
        TestUtil.logTrace("Received expected firstName:" + firstName);
      } else {
        pass = false;
        TestUtil.logErr("Expected firstName: Paul, actual: null");
      }

      clearCache();
      TestUtil.logTrace("--------------");
      TestUtil.logTrace("find employee4");
      Employee4 emp4 = getEntityManager().find(Employee4.class, 8);
      firstName = emp4.getFirstName();
      TestUtil.logTrace("Name:" + firstName + " " + emp4.getLastName());
      if (firstName == null) {
        TestUtil.logTrace("Received expected null firstName");
      } else {
        pass = false;
        TestUtil.logErr("Expected firstName: null, actual:" + firstName);
      }
      getEntityTransaction().commit();

    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Received unexpected exception", e);
    }

    if (!pass) {
      throw new Fault("columnInsertable Failed");
    }
  }

  /*
   * @testName: columnUpdatable
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:20
   *
   * @test_Strategy: The JoinColumn annotation with an attribute of updatable
   * used to specify the mapping for the fk column to a second entity Execute a
   * query returning Employees objects.
   */
  @SetupMethod(name = "setupCreateTestData2")
  public void columnUpdatable() throws Fault {
    boolean pass = true;

    try {
      getEntityTransaction().begin();
      clearCache();
      TestUtil.logTrace("find employee2");
      Employee2 emp2 = getEntityManager().find(Employee2.class, 6);
      TestUtil
          .logTrace("Name:" + emp2.getFirstName() + " " + emp2.getLastName());
      TestUtil.logTrace("set firstName and save");
      emp2.setFirstName("foo");
      getEntityManager().merge(emp2);
      getEntityManager().flush();
      clearCache();
      TestUtil.logTrace("find employee2 again");
      emp2 = getEntityManager().find(Employee2.class, 6);
      TestUtil
          .logTrace("Name:" + emp2.getFirstName() + " " + emp2.getLastName());
      String firstName = emp2.getFirstName();
      if (firstName == null) {
        TestUtil.logTrace("Received expected null firstName");
      } else {
        pass = false;
        TestUtil.logErr("Expected firstName: null, actual:" + firstName);
      }

      clearCache();
      TestUtil.logTrace("--------------");
      TestUtil.logTrace("find employee3");
      Employee3 emp3 = getEntityManager().find(Employee3.class, 7);
      TestUtil
          .logTrace("Name:" + emp3.getFirstName() + " " + emp3.getLastName());
      TestUtil.logTrace("set firstName and save");
      emp3.setFirstName("foo");
      getEntityManager().merge(emp3);
      getEntityManager().flush();
      clearCache();
      TestUtil.logTrace("find employee3 again");
      emp3 = getEntityManager().find(Employee3.class, 7);
      TestUtil
          .logTrace("Name:" + emp3.getFirstName() + " " + emp3.getLastName());
      firstName = emp3.getFirstName();
      if (firstName != null && firstName.equals("Paul")) {
        TestUtil.logTrace("Received expected firstName:" + firstName);
      } else {
        pass = false;
        TestUtil.logErr("Expected firstName: Paul, actual: null");
      }

      clearCache();
      TestUtil.logTrace("--------------");
      TestUtil.logTrace("find employee4");
      Employee4 emp4 = getEntityManager().find(Employee4.class, 8);
      TestUtil
          .logTrace("Name:" + emp4.getFirstName() + " " + emp4.getLastName());
      TestUtil.logTrace("set firstName and save");
      emp4.setFirstName("foo");
      getEntityManager().merge(emp4);
      getEntityManager().flush();
      clearCache();
      TestUtil.logTrace("find employee4 again");
      emp4 = getEntityManager().find(Employee4.class, 8);
      TestUtil
          .logTrace("Name:" + emp4.getFirstName() + " " + emp4.getLastName());
      firstName = emp4.getFirstName();
      if (firstName != null && firstName.equals("foo")) {
        TestUtil.logTrace("Received expected firstName:" + firstName);
      } else {
        pass = false;
        TestUtil.logErr("Expected firstName: foo, actual: null");
      }

      getEntityTransaction().commit();

    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Received unexpected exception", e);
    }

    if (!pass) {
      throw new Fault("columnUpdatable Failed");
    }

  }

  /*
   * 
   * Business Methods to set up data for Test Cases
   */

  public void createTestDataCommon() throws Exception {
    try {

      TestUtil.logTrace("createTestDataCommon");

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

  public void createTestData() throws Exception {
    try {

      TestUtil.logTrace("createTestData");
      createTestDataCommon();
      getEntityTransaction().begin();

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

      Map<String, Employee> link = new HashMap<String, Employee>();
      link.put(empRef[0].getLastName(), empRef[0]);
      link.put(empRef[2].getLastName(), empRef[2]);
      link.put(empRef[4].getLastName(), empRef[4]);
      deptRef[0].setLastNameEmployees(link);

      Map<String, Employee> link1 = new HashMap<String, Employee>();
      link1.put(empRef[1].getLastName(), empRef[1]);
      link1.put(empRef[3].getLastName(), empRef[3]);
      deptRef[1].setLastNameEmployees(link1);

      TestUtil.logTrace("Start to persist employees ");
      for (Employee emp : empRef) {
        if (emp != null) {
          getEntityManager().persist(emp);
          TestUtil.logTrace("persisted employee " + emp.getId());
        }
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

  public void createTestData2() throws Exception {
    try {

      TestUtil.logTrace("createTestData2");
      createTestDataCommon();
      getEntityTransaction().begin();

      // insertable = false, updatable = false
      TestUtil.logTrace("Create and persist employee2 ");
      empRef2 = new Employee2(6, "John", "Smith");
      empRef2.setDepartment(deptRef[0]);
      getEntityManager().persist(empRef2);

      // insertable = true, updatable = false
      TestUtil.logTrace("Create and persist employee3 ");
      empRef3 = new Employee3(7, "Paul", "Jones");
      empRef3.setDepartment(deptRef[0]);
      getEntityManager().persist(empRef3);

      // insertable = false, updatable = true
      TestUtil.logTrace("Create and persist employee4 ");
      empRef4 = new Employee4(8, "Thomas", "Brady");
      empRef4.setDepartment(deptRef[0]);
      getEntityManager().persist(empRef4);
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
      getEntityManager().createNativeQuery("Delete from EMPLOYEE")
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
        TestUtil.logErr("Unexpected Exception in RemoveSchemaData:", re);
      }
    }
  }
}
