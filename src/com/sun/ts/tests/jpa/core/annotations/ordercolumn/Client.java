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

package com.sun.ts.tests.jpa.core.annotations.ordercolumn;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.CleanupMethod;
import com.sun.ts.lib.harness.SetupMethod;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Client extends PMClientBase {

  private List<Student> expectedResults;

  private List<Employee> expectedEmployees;

  private List<Employee2> expectedEmployees2;

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
      createStudentTestData();
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  public void setupEmployee(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup");
    try {

      super.setup(args, p);
      removeEmployeeTestData();
      createEmployeeTestData();
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  /*
   * @testName: orderColumn
   * 
   * @assertion_ids: PERSISTENCE:SPEC:500; PERSISTENCE:SPEC:501;
   * PERSISTENCE:SPEC:503; PERSISTENCE:SPEC:504; PERSISTENCE:SPEC:505;
   * PERSISTENCE:SPEC:506; PERSISTENCE:SPEC:507; PERSISTENCE:SPEC:508;
   * PERSISTENCE:SPEC:932; PERSISTENCE:SPEC:936; PERSISTENCE:SPEC:939;
   * PERSISTENCE:SPEC:943; PERSISTENCE:SPEC:946; PERSISTENCE:SPEC:930;
   * PERSISTENCE:SPEC:1018; PERSISTENCE:SPEC:1019; PERSISTENCE:SPEC:1020;
   * PERSISTENCE:SPEC:1021; PERSISTENCE:SPEC:1023; PERSISTENCE:SPEC:1025;
   * PERSISTENCE:SPEC:848; PERSISTENCE:SPEC:856; PERSISTENCE:SPEC:908;
   * PERSISTENCE:SPEC:909; PERSISTENCE:SPEC:915; PERSISTENCE:SPEC:925;
   * PERSISTENCE:SPEC:918; PERSISTENCE:SPEC:928; PERSISTENCE:SPEC:929;
   * PERSISTENCE:JAVADOC:149; PERSISTENCE:JAVADOC:152; PERSISTENCE:JAVADOC:163;
   * PERSISTENCE:SPEC:846; PERSISTENCE:SPEC:1204; PERSISTENCE:JAVADOC:378;
   * PERSISTENCE:JAVADOC:379; PERSISTENCE:JAVADOC:377; PERSISTENCE:JAVADOC:380;
   *
   * @test_Strategy: With basic entity requirements, persist/remove an entity.
   */
  public void orderColumn() throws Fault {
    boolean pass = false;
    final String expectedStudentName = "Joseph";
    final int expectedListSize = 4;
    final int courseNumber = 101;
    List<Student> students;
    int numStudents = 0;

    try {
      getEntityTransaction().begin();

      final Course course = getEntityManager().find(Course.class, courseNumber);

      if (course != null) {
        // force students to be read
        numStudents = course.getStudents().size();
      } else {
        TestUtil.logErr("course from find() is NULL!");
      }

      if (numStudents == expectedListSize) {
        students = course.getStudents();
        if (students.get(0).equals(expectedResults.get(0))
            && students.get(1).equals(expectedResults.get(1))
            && students.get(2).equals(expectedResults.get(2))
            && students.get(3).equals(expectedResults.get(3))) {
          TestUtil.logTrace("****Current order of students returned via "
              + "getStudents()\nNow checking via JPQL");

          Query q = getEntityManager().createQuery("SELECT s.studentName "
              + "FROM Course c JOIN c.students s where c.courseName ='Physics' and INDEX(s) = 1");
          final String result = (String) q.getSingleResult();
          if (result.equals(expectedStudentName)) {
            TestUtil.logTrace(
                "+++Received expected Name via Query:" + expectedStudentName);
            pass = true;
          } else {
            TestUtil.logErr("Did NOT get expected name via Query: "
                + expectedStudentName + ", received: " + result);
          }

        } else {
          TestUtil.logErr("Failed to return the correct order of "
              + "students via getStudents()");
        }

      } else {
        TestUtil.logErr("course.getStudents() returned wrong number!");
      }

      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
    }

    if (!pass) {
      throw new Fault("orderColumn test failed");
    }

  }

  /*
   * @testName: propertyAccessWithNameTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:2097; PERSISTENCE:SPEC:2104;
   * PERSISTENCE:SPEC:2102;
   *
   * @test_Strategy: name is specified while using property access.
   */
  @SetupMethod(name = "setupEmployee")
  @CleanupMethod(name = "cleanupEmployee")
  public void propertyAccessWithNameTest() throws Fault {
    boolean pass = false;

    try {
      getEntityTransaction().begin();
      clearCache();
      Department d = getEntityManager().find(Department.class, 50);
      TestUtil.logMsg("Display find data");

      for (Employee e : d.getEmployees()) {
        TestUtil.logMsg("Employee:" + e);
      }

      List<Employee> actual = new ArrayList<Employee>();
      TypedQuery<Employee> q = getEntityManager().createQuery(
          "SELECT e FROM Department d JOIN d.employees e WHERE d.id = 50 AND INDEX(e) = 0",
          Employee.class);
      Employee emp = q.getSingleResult();
      if (emp != null) {
        actual.add(emp);
      } else {
        TestUtil.logErr("Query of INDEX(0) returned null result");
      }
      q = getEntityManager().createQuery(
          "SELECT e FROM Department d JOIN d.employees e WHERE d.id = 50 AND INDEX(e) = 1",
          Employee.class);
      emp = q.getSingleResult();
      if (emp != null) {
        actual.add(emp);
      } else {
        TestUtil.logErr("Query of INDEX(1) returned null result");
      }
      q = getEntityManager().createQuery(
          "SELECT e FROM Department d JOIN d.employees e WHERE d.id = 50 AND INDEX(e) = 2",
          Employee.class);
      emp = q.getSingleResult();
      if (emp != null) {
        actual.add(emp);
      } else {
        TestUtil.logErr("Query of INDEX(2) returned null result");
      }

      if (actual.size() == expectedEmployees.size()) {
        int count = 0;
        for (int i = 0; i < expectedEmployees.size(); i++) {
          TestUtil.logTrace("Testing - expected[" + expectedEmployees.get(i)
              + "], actual[" + actual.get(i) + "]");

          if (expectedEmployees.get(i).equals(actual.get(i))) {
            count++;
          }
        }

        if (count == expectedEmployees.size()) {
          pass = true;
        } else {
          TestUtil.logTrace(
              "count=" + count + ", expected size:" + expectedEmployees.size());
          for (Employee e : expectedEmployees) {
            TestUtil.logErr("expected:" + e);
          }
          TestUtil.logErr("------------");
          for (Employee e : actual) {
            TestUtil.logErr("actual:" + e);
          }
        }
      } else {
        TestUtil.logErr("Expected list size:" + expectedEmployees.size()
            + ", actual size:" + actual.size());
        for (Employee e : expectedEmployees) {
          TestUtil.logErr("expected:" + e);
        }
        TestUtil.logErr("------------");
        for (Employee e : actual) {
          TestUtil.logErr("actual:" + e);
        }
      }

      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
    }

    if (!pass) {
      throw new Fault("propertyAccessWithNameTest test failed");
    }

  }

  /*
   * @testName: fieldAccessWithNameTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:2097; PERSISTENCE:SPEC:2104;
   * PERSISTENCE:SPEC:2102; PERSISTENCE:SPEC:2101;
   *
   * @test_Strategy: name is specified while using property access.
   */
  @SetupMethod(name = "setupEmployee")
  @CleanupMethod(name = "cleanupEmployee")
  public void fieldAccessWithNameTest() throws Fault {
    boolean pass = false;

    try {
      getEntityTransaction().begin();
      clearCache();
      Department2 d = getEntityManager().find(Department2.class, 55);
      TestUtil.logMsg("Display find data");

      for (Employee2 e : d.getEmployees()) {
        TestUtil.logMsg("Employee2:" + e);
      }

      List<Employee2> actual = new ArrayList<Employee2>();
      TypedQuery<Employee2> q = getEntityManager().createQuery(
          "SELECT e FROM Department2 d JOIN d.employees e WHERE d.id = 55 AND INDEX(e) = 0",
          Employee2.class);
      Employee2 emp = q.getSingleResult();
      if (emp != null) {
        actual.add(emp);
      } else {
        TestUtil.logErr("Query of INDEX(0) returned null result");
      }
      q = getEntityManager().createQuery(
          "SELECT e FROM Department2 d JOIN d.employees e WHERE d.id = 55 AND INDEX(e) = 1",
          Employee2.class);
      emp = q.getSingleResult();
      if (emp != null) {
        actual.add(emp);
      } else {
        TestUtil.logErr("Query of INDEX(1) returned null result");
      }
      q = getEntityManager().createQuery(
          "SELECT e FROM Department2 d JOIN d.employees e WHERE d.id = 55 AND INDEX(e) = 2",
          Employee2.class);
      emp = q.getSingleResult();
      if (emp != null) {
        actual.add(emp);
      } else {
        TestUtil.logErr("Query of INDEX(2) returned null result");
      }

      if (actual.size() == expectedEmployees2.size()) {
        int count = 0;
        for (int i = 0; i < expectedEmployees2.size(); i++) {
          TestUtil.logTrace("Testing - expected[" + expectedEmployees2.get(i)
              + "], actual[" + actual.get(i) + "]");

          if (expectedEmployees2.get(i).equals(actual.get(i))) {
            count++;
          }
        }

        if (count == expectedEmployees.size()) {
          pass = true;
        } else {
          TestUtil.logTrace("count=" + count + ", expected size:"
              + expectedEmployees2.size());
          for (Employee2 e : expectedEmployees2) {
            TestUtil.logErr("expected:" + e);
          }
          TestUtil.logErr("------------");
          for (Employee2 e : actual) {
            TestUtil.logErr("actual:" + e);
          }
        }
      } else {
        TestUtil.logErr("Expected list size:" + expectedEmployees2.size()
            + ", actual size:" + actual.size());
        for (Employee2 e : expectedEmployees2) {
          TestUtil.logErr("expected:" + e);
        }
        TestUtil.logErr("------------");
        for (Employee2 e : actual) {
          TestUtil.logErr("actual:" + e);
        }
      }

      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
    }

    if (!pass) {
      throw new Fault("fieldAccessWithNameTest test failed");
    }

  }

  public void createStudentTestData() {
    try {
      TestUtil.logTrace("createTestData");
      getEntityTransaction().begin();

      // Create 8 students;
      Student student1 = new Student(1, "Neo");
      Student student2 = new Student(2, "Vivek");
      Student student3 = new Student(3, "Arun");
      Student student4 = new Student(4, "Ganesh");
      Student student5 = new Student(5, "Ram");
      Student student6 = new Student(6, "Rahim");
      Student student7 = new Student(7, "Joseph");
      Student student8 = new Student(8, "Krishna");

      // Create 12 Courses;
      Course appliedMath = new Course(101, "AppliedMathematics");
      Course physics = new Course(102, "Physics");
      Course operationResearch = new Course(103, "OperationResearch");
      Course statistics = new Course(201, "Statistics");
      Course operatingSystem = new Course(202, "OperatingSystem");

      // Set enrolled students for each course
      List<Student> studentList1 = new ArrayList<Student>();
      studentList1.add(student1);
      studentList1.add(student7);
      studentList1.add(student2);
      studentList1.add(student8);

      expectedResults = new ArrayList<Student>();
      expectedResults.addAll(studentList1);

      // Set enrolled students for each course
      List<Student> studentList2 = new ArrayList<Student>();
      studentList2.add(student3);
      studentList2.add(student4);

      // Set enrolled students for each course
      List<Student> studentList3 = new ArrayList<Student>();
      studentList3.add(student5);
      studentList3.add(student6);

      // Set enrolled students for each course
      List<Student> studentList4 = new ArrayList<Student>();
      studentList4.add(student7);
      studentList4.add(student8);

      appliedMath.setStudents(studentList1);
      physics.setStudents(studentList1);
      operationResearch.setStudents(studentList1);

      statistics.setStudents(studentList2);
      operatingSystem.setStudents(studentList2);

      // Set Courses for first semester
      List<Course> firstSemCourses = new ArrayList<Course>();
      firstSemCourses.add(appliedMath);
      firstSemCourses.add(physics);
      firstSemCourses.add(operationResearch);

      // Set Courses for Second semester
      List<Course> secondSemCourses = new ArrayList<Course>();
      secondSemCourses.add(statistics);
      secondSemCourses.add(operatingSystem);

      // Set Courses for each student
      student1.setCourses(firstSemCourses);
      student2.setCourses(firstSemCourses);
      student3.setCourses(secondSemCourses);
      student4.setCourses(secondSemCourses);
      student5.setCourses(secondSemCourses);
      student6.setCourses(secondSemCourses);
      student7.setCourses(firstSemCourses);
      student8.setCourses(firstSemCourses);

      EntityManager entityManager = getEntityManager();

      // persist 8 students

      entityManager.persist(student1);
      entityManager.persist(student2);
      entityManager.persist(student3);
      entityManager.persist(student4);
      entityManager.persist(student5);
      entityManager.persist(student6);
      entityManager.persist(student7);
      entityManager.persist(student8);
      TestUtil.logTrace("persisted 8 students");

      // persist courses
      entityManager.persist(appliedMath);
      entityManager.persist(physics);
      entityManager.persist(operationResearch);
      entityManager.persist(statistics);
      entityManager.persist(operatingSystem);
      TestUtil.logTrace("persisted 5 Courses");

      TestUtil.logTrace("persisted Entity Data");
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

  private void createEmployeeTestData() throws Exception {
    try {
      TestUtil.logTrace("createEmployeeTestData");
      getEntityTransaction().begin();

      Department d1 = new Department(50, "Dept1");
      Department2 d2 = new Department2(55, "Dept2");
      getEntityManager().persist(d1);

      final Employee e1 = new Employee(20, "Jie", "Leng", 0.0F, d1);
      final Employee e2 = new Employee(40, "Zoe", "Leng", 0.0F, d1);
      final Employee e3 = new Employee(60, "John", "Smith", 0.0F, d1);
      final Employee2 e4 = new Employee2(80, "Song", "Leng", 0.0F, d2);
      final Employee2 e5 = new Employee2(100, "May", "Leng", 0.0F, d2);
      final Employee2 e6 = new Employee2(120, "Donny", "Oz", 0.0F, d2);
      getEntityManager().persist(e1);
      getEntityManager().persist(e2);
      getEntityManager().persist(e3);
      getEntityManager().persist(e4);
      getEntityManager().persist(e5);
      getEntityManager().persist(e6);

      expectedEmployees = new ArrayList<Employee>();
      expectedEmployees.add(e3);
      expectedEmployees.add(e1);
      expectedEmployees.add(e2);

      d1.setEmployees(expectedEmployees);
      getEntityManager().merge(d1);

      expectedEmployees2 = new ArrayList<Employee2>();
      expectedEmployees2.add(e6);
      expectedEmployees2.add(e4);
      expectedEmployees2.add(e5);

      d2.setEmployees(expectedEmployees2);
      getEntityManager().merge(d2);

      TestUtil.logTrace("persisted Entity Data");
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

  public void cleanupEmployee() throws Fault {
    TestUtil.logTrace("cleanupEmployee");
    // removeEmployeeTestData();
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
      getEntityManager().createNativeQuery("Delete from COURSE_STUDENT")
          .executeUpdate();
      getEntityManager().createNativeQuery("Delete from STUDENT")
          .executeUpdate();
      getEntityManager().createNativeQuery("Delete from COURSE")
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

  private void removeEmployeeTestData() {
    TestUtil.logTrace("removeEmployeeTestData");
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
        TestUtil.logErr("Unexpected Exception in removeTestData:", re);
      }
    }
  }
}
