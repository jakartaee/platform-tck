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

package com.sun.ts.tests.jpa.jpa22.repeatable.mapkeyjoincolumn;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import javax.persistence.EntityManager;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class Client extends PMClientBase {

  private static final long serialVersionUID = 22L;

  private Map<Course, Semester> student7EnrollmentMap;

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
   * @testName: mapKeyJoinColumnTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:364;
   * 
   * @test_Strategy: follow core/annotations/mapkeyclass test but
   * without @mapkeyjoincolumns
   */
  public void mapKeyJoinColumnTest() throws Fault {
    boolean pass = false;

    try {
      getEntityTransaction().begin();
      createTestData();
      getEntityManager().flush();
      clearCache();
      clearCache();

      final Student student = getEntityManager().find(Student.class, 7);
      final Set<Course> courses = student.getCourses();
      if (courses.containsAll(student7EnrollmentMap.keySet())
          && student7EnrollmentMap.keySet().containsAll(courses)
          && courses.size() == student7EnrollmentMap.keySet().size())
        pass = true;
      clearCache();
      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass) {
      throw new Fault("mapKeyJoinColumn Failed");
    }

  }

  public void createTestData() {
    // Create 8 students;
    final Student student1 = new Student(1, "Neo");
    final Student student2 = new Student(2, "Vivek");
    final Student student3 = new Student(3, "Arun");
    final Student student4 = new Student(4, "Ganesh");
    final Student student5 = new Student(5, "Ram");
    final Student student6 = new Student(6, "Rahim");
    final Student student7 = new Student(7, "Joseph");
    final Student student8 = new Student(8, "Krishna");

    // Create 4 Semesters;
    final Semester semester1 = new Semester(1);
    final Semester semester2 = new Semester(2);
    final Semester semester3 = new Semester(3);
    final Semester semester4 = new Semester(4);

    // Create 12 Courses;
    final Course appliedMath = new Course(101, "AppliedMathematics");
    final Course physics = new Course(102, "Physics");
    final Course operationResearch = new Course(103, "OperationResearch");
    final Course statistics = new Course(201, "Statistics");
    final Course operatingSystem = new Course(202, "OperatingSystem");
    final Course numericalMethods = new Course(203, "NumericalMethods");
    final Course graphics = new Course(301, "Graphics");
    final Course accountancy = new Course(302, "Accountancy");
    final Course mis = new Course(303, "ManagementInformationSystem");
    final Course cad = new Course(401, "ComputerAidedDesign");
    final Course compilerDesign = new Course(402, "CompilerDesign");
    final Course ood = new Course(403, "ObjectOrientedDesignAndAnalysis");

    // Create Enrollment map for Student1
    Map<Course, Semester> student1EnrollmentMap = new Hashtable<>();
    student1EnrollmentMap.put(appliedMath, semester1);
    student1EnrollmentMap.put(physics, semester1);
    student1EnrollmentMap.put(operationResearch, semester1);
    student1EnrollmentMap.put(statistics, semester2);
    student1EnrollmentMap.put(operatingSystem, semester2);
    student1EnrollmentMap.put(numericalMethods, semester2);
    // Set Enrollment map for Student1
    student1.setEnrollment(student1EnrollmentMap);

    // Create Enrollment map for Student2
    Map<Course, Semester> student2EnrollmentMap = new Hashtable<>();
    student2EnrollmentMap.put(appliedMath, semester1);
    student2EnrollmentMap.put(physics, semester1);
    student2EnrollmentMap.put(operationResearch, semester1);
    student2EnrollmentMap.put(graphics, semester3);
    student2EnrollmentMap.put(accountancy, semester3);
    student2EnrollmentMap.put(mis, semester3);
    // Set Enrollment map for Student2
    student2.setEnrollment(student2EnrollmentMap);

    // Create Enrollment map for Student3
    Map<Course, Semester> student3EnrollmentMap = new Hashtable<>();
    student3EnrollmentMap.put(statistics, semester2);
    student3EnrollmentMap.put(operatingSystem, semester2);
    student3EnrollmentMap.put(numericalMethods, semester2);
    student3EnrollmentMap.put(graphics, semester3);
    student3EnrollmentMap.put(accountancy, semester3);
    student3EnrollmentMap.put(mis, semester3);
    // Set Enrollment map for Student3
    student3.setEnrollment(student3EnrollmentMap);

    // Create Enrollment map for Student4
    Map<Course, Semester> student4EnrollmentMap = new Hashtable<>();
    student4EnrollmentMap.put(statistics, semester2);
    student4EnrollmentMap.put(operatingSystem, semester2);
    student4EnrollmentMap.put(numericalMethods, semester2);
    student4EnrollmentMap.put(cad, semester4);
    student4EnrollmentMap.put(compilerDesign, semester4);
    student4EnrollmentMap.put(ood, semester4);
    // Set Enrollment map for Student4
    student4.setEnrollment(student4EnrollmentMap);

    // Create Enrollment map for Student5
    Map<Course, Semester> student5EnrollmentMap = new Hashtable<>();
    student5EnrollmentMap.put(graphics, semester3);
    student5EnrollmentMap.put(accountancy, semester3);
    student5EnrollmentMap.put(mis, semester3);
    // Set Enrollment map for Student5
    student5.setEnrollment(student5EnrollmentMap);

    // Create Enrollment map for Student6
    Map<Course, Semester> student6EnrollmentMap = new Hashtable<>();
    student6EnrollmentMap.put(graphics, semester3);
    student6EnrollmentMap.put(accountancy, semester3);
    student6EnrollmentMap.put(mis, semester3);
    student6EnrollmentMap.put(cad, semester4);
    student6EnrollmentMap.put(compilerDesign, semester4);
    student6EnrollmentMap.put(ood, semester4);
    // Set Enrollment map for Student6
    student6.setEnrollment(student6EnrollmentMap);

    // Create Enrollment map for Student7
    student7EnrollmentMap = new Hashtable<>();
    student7EnrollmentMap.put(appliedMath, semester1);
    student7EnrollmentMap.put(physics, semester1);
    student7EnrollmentMap.put(operationResearch, semester1);
    student7EnrollmentMap.put(cad, semester4);
    student7EnrollmentMap.put(compilerDesign, semester4);
    student7EnrollmentMap.put(ood, semester4);
    // Set Enrollment map for Student7
    student7.setEnrollment(student7EnrollmentMap);

    // Create Enrollment map for Student8
    Map<Course, Semester> student8EnrollmentMap = new Hashtable<>();
    student8EnrollmentMap.put(appliedMath, semester2);
    student8EnrollmentMap.put(physics, semester2);
    student8EnrollmentMap.put(operationResearch, semester2);
    student8EnrollmentMap.put(cad, semester4);
    student8EnrollmentMap.put(compilerDesign, semester4);
    student8EnrollmentMap.put(ood, semester4);
    // Set Enrollment map for Student8
    student8.setEnrollment(student8EnrollmentMap);

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

    // persist 4 semesters
    entityManager.persist(semester1);
    entityManager.persist(semester2);
    entityManager.persist(semester3);
    entityManager.persist(semester4);
    TestUtil.logTrace("persisted 4 semesters");

    // persist 12 courses
    entityManager.persist(appliedMath);
    entityManager.persist(physics);
    entityManager.persist(operationResearch);
    entityManager.persist(statistics);
    entityManager.persist(operatingSystem);
    entityManager.persist(numericalMethods);
    entityManager.persist(graphics);
    entityManager.persist(accountancy);
    entityManager.persist(mis);
    entityManager.persist(cad);
    entityManager.persist(compilerDesign);
    entityManager.persist(ood);
    TestUtil.logTrace("persisted 12 Courses");

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
      getEntityManager().createNativeQuery("Delete from ENROLLMENTS")
          .executeUpdate();
      getEntityManager().createNativeQuery("Delete from SEMESTER")
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
}
