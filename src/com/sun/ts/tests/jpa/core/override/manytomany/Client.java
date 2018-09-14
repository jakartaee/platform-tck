/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.core.override.manytomany;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class Client extends PMClientBase {

  private static final Integer COURSE1_ID = 203;

  private static final String COURSE1_NAME = "Math";

  private static final Integer COURSE2_ID = 275;

  private static final String COURSE2_NAME = "Science";

  private static final Integer STUDENT1_ID = 12345;

  private static final String STUDENT1_NAME = "Scott";

  private static final Integer STUDENT2_ID = 67890;

  private static final String STUDENT2_NAME = "Jonathan";

  private static final Integer STUDENT3_ID = 24680;

  private static final String STUDENT3_NAME = "Eliot";

  public Client() {
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  @Override
  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup");
    try {
      super.setup(args, p);
      removeTestData();
    } catch (Exception e) {
      TestUtil.logErr("Exception:test failed ", e);
    }
  }

  /*
   * @testName: testNoManyToManyAnnotation
   * 
   * @assertion_ids: PERSISTENCE:SPEC:574; PERSISTENCE:SPEC:1098;
   * PERSISTENCE:SPEC:1099; PERSISTENCE:SPEC:1207; PERSISTENCE:SPEC:1209;
   * PERSISTENCE:SPEC:1254; PERSISTENCE:SPEC:1255; PERSISTENCE:SPEC:1256;
   * 
   * @test_Strategy: Many-to-Many is tested without using its annotation;instead
   * it is overridden in orm.xml.
   */
  public void testNoManyToManyAnnotation() throws Fault {

    getEntityTransaction().begin();
    Course mathCourse = createCourse(COURSE1_ID, COURSE1_NAME);
    Course scienceCourse = createCourse(COURSE2_ID, COURSE2_NAME);
    Student student1 = createStudent(STUDENT1_ID, STUDENT1_NAME);
    student1.addCourse(mathCourse);
    student1.addCourse(scienceCourse);
    Student student2 = createStudent(STUDENT2_ID, STUDENT2_NAME);
    student2.addCourse(mathCourse);
    Student student3 = createStudent(STUDENT3_ID, STUDENT3_NAME);
    student3.addCourse(scienceCourse);

    Set mathStudents = new HashSet();
    mathStudents.add(student1);
    mathStudents.add(student2);
    Set scienceStudents = new HashSet();
    scienceStudents.add(student3);
    scienceStudents.add(student1);

    mathCourse.setStudents(mathStudents);
    scienceCourse.setStudents(scienceStudents);

    getEntityManager().persist(mathCourse);
    getEntityManager().persist(scienceCourse);
    getEntityManager().persist(student1);
    getEntityManager().persist(student2);
    getEntityManager().persist(student3);
    getEntityManager().flush();
    try {
      Course retrieveMath = getEntityManager().find(Course.class, COURSE1_ID);
      Course retrieveScience = getEntityManager().find(Course.class,
          COURSE2_ID);

      if (retrieveMath.getStudents().size() == 2
          && retrieveScience.getStudents().size() == 2) {
        TestUtil.logTrace("Test Passed");
      } else {
        throw new Fault("Expected many to many relationship between course "
            + "and student to have been set. Expected 2 students in Math "
            + "and 2 in Science, Actual - " + retrieveMath.getStudents().size()
            + " - " + retrieveScience.getStudents().size());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      throw new Fault(
          "Exception thrown while testing testNoManyToManyAnnotation" + e);
    }
  }

  private Student createStudent(final int id, final String name) {
    Student student = new Student();
    student.setId(id);
    student.setName(name);
    return student;
  }

  private Course createCourse(final int id, final String courseName) {
    Course course = new Course();
    course.setId(id);
    course.setName(courseName);
    return course;
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
      getEntityManager().createNativeQuery("DELETE FROM COURSE_2")
          .executeUpdate();
      getEntityManager().createNativeQuery("DELETE FROM STUDENT_2")
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
