/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.se.schemaGeneration.annotations.joinTable;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Client extends PMClientBase {

  private static final long serialVersionUID = 22L;

  String schemaGenerationDir = null;

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

      schemaGenerationDir = System.getProperty("user.dir");
      if (!schemaGenerationDir.endsWith(File.separator)) {
        schemaGenerationDir += File.separator;
      }
      schemaGenerationDir += "schemaGeneration";
      TestUtil.logMsg("schemaGenerationDir=" + this.schemaGenerationDir);

      File f = new File(schemaGenerationDir);
      TestUtil.logMsg("Delete existing directory ");
      deleteItem(f);
      TestUtil.logMsg("Create new directory ");
      if (!f.mkdir()) {
        String msg = "Could not mkdir:" + f.getAbsolutePath();
        throw new Fault(msg);
      }
      removeTestData();
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  /*
   * @testName: joinTableTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:2118.10; PERSISTENCE:SPEC:2118.9;
   * 
   * @test_Strategy: Test the @JoinTable annotation
   */
  public void joinTableTest() throws Fault {
    boolean pass1a = false;
    boolean pass1b = false;
    boolean pass1c = false;
    boolean pass1d = false;
    boolean pass1e = false;

    boolean pass2a = false;
    boolean pass2b = false;
    boolean pass2c = false;
    boolean pass2d = false;
    boolean pass2e = false;
    boolean pass3 = false;
    boolean pass4 = false;
    boolean pass5 = false;

    TestUtil.logMsg("Create the script(s)");
    final String CREATEFILENAME = schemaGenerationDir + File.separator
        + "create_" + this.sTestCase + ".sql";
    final String DROPFILENAME = schemaGenerationDir + File.separator + "drop_"
        + this.sTestCase + ".sql";

    File f1 = new File(CREATEFILENAME);
    TestUtil.logTrace("Deleting previous create script");
    deleteItem(f1);
    File f2 = new File(DROPFILENAME);
    TestUtil.logTrace("Deleting previous drop script");
    deleteItem(f2);

    Properties props = getPersistenceUnitProperties();
    props.put("jakarta.persistence.schema-generation.database.action", "none");
    props.put("jakarta.persistence.schema-generation.scripts.action",
        "drop-and-create");
    props.put("jakarta.persistence.schema-generation.create-database-schemas",
        "false");
    props.put("jakarta.persistence.schema-generation.scripts.create-target",
        convertToURI(CREATEFILENAME));
    props.put("jakarta.persistence.schema-generation.scripts.drop-target",
        convertToURI(DROPFILENAME));

    displayProperties(props);

    TestUtil.logMsg("Executing Persistence.createEntityManagerFactory(...)");
    EntityManagerFactory emf = Persistence
        .createEntityManagerFactory(getPersistenceUnitName(), props);
    emf.close();
    emf = null;

    TestUtil.logMsg("Check script(s) content");
    List<String> expected = new ArrayList<String>();
    expected.add("CREATE TABLE SCHEMAGENCOURSE");
    expected.add("COURSEID");
    expected.add("COURSENAME");
    expected.add("PRIMARY KEY (COURSEID)");
    pass1a = findDataInFile(f1, expected);

    expected.clear();
    expected.add("CREATE TABLE SCHEMAGENSTUDENT");
    expected.add("STUDENTID");
    expected.add("STUDENTNAME");
    expected.add("PRIMARY KEY (STUDENTID)");
    pass1b = findDataInFile(f1, expected);

    expected.clear();
    expected.add("CREATE TABLE SCHEMAGEN_COURSE_STUDENT");
    expected.add("COURSE_ID");
    expected.add("STUDENT_ID");
    // bug id 27382206
    // Based on JPA 2.2 specification section 2.10.4, the join table is not
    // required to have a primary key.
    // expected.add("PRIMARY KEY (COURSE_ID, STUDENT_ID)");
    pass1c = findDataInFile(f1, expected);

    expected.clear();
    expected.add("ALTER TABLE SCHEMAGEN_COURSE_STUDENT ADD");
    expected.add("CONSTRAINT STUDENTIDCONSTRAINT");
    expected.add("SCHEMAGENSTUDENT");
    pass1d = findDataInFile(f1, expected);

    expected.clear();
    expected.add("ALTER TABLE SCHEMAGEN_COURSE_STUDENT ADD");
    expected.add("CONSTRAINT COURSEIDCONSTRAINT");
    expected.add("SCHEMAGENCOURSE");
    pass1e = findDataInFile(f1, expected);

    /*
     * CREATE TABLE SCHEMAGENCOURSE (COURSEID INTEGER NOT NULL, COURSENAME
     * VARCHAR(255), PRIMARY KEY (COURSEID)) CREATE TABLE SCHEMAGENSTUDENT
     * (STUDENTID INTEGER NOT NULL, STUDENTNAME VARCHAR(255), PRIMARY KEY
     * (STUDENTID)) CREATE TABLE COURSE_STUDENT (COURSE_ID INTEGER NOT NULL,
     * STUDENT_ID INTEGER NOT NULL, PRIMARY KEY (COURSE_ID, STUDENT_ID)) ALTER
     * TABLE COURSE_STUDENT ADD CONSTRAINT STUDENTIDCONSTRAINT FOREIGN KEY
     * (STUDENT_ID) REFERENCES SCHEMAGENSTUDENT (STUDENTID) ALTER TABLE
     * COURSE_STUDENT ADD CONSTRAINT COURSEIDCONSTRAINT FOREIGN KEY (COURSE_ID)
     * REFERENCES SCHEMAGENCOURSE (COURSEID)
     */

    pass2a = findDataInFile(f2, new LinkedList<String>() {
      private static final long serialVersionUID = 22L;
      {
        add("ALTER TABLE SCHEMAGEN_COURSE_STUDENT DROP");
        add("STUDENTIDCONSTRAINT");
      }
    });
    pass2b = findDataInFile(f2, new LinkedList<String>() {
      private static final long serialVersionUID = 22L;
      {
        add("ALTER TABLE SCHEMAGEN_COURSE_STUDENT DROP");
        add("COURSEIDCONSTRAINT");
      }
    });
    pass2c = findDataInFile(f2, "DROP TABLE SCHEMAGEN_COURSE_STUDENT");
    pass2d = findDataInFile(f2, "DROP TABLE SCHEMAGENCOURSE");
    pass2e = findDataInFile(f2, "DROP TABLE SCHEMAGENSTUDENT");

    TestUtil.logTrace("Execute the create script");
    props = getPersistenceUnitProperties();

    props.put("jakarta.persistence.schema-generation.database.action", "create");
    props.put("jakarta.persistence.schema-generation.scripts.action", "none");
    props.put("jakarta.persistence.schema-generation.create-database-schemas",
        "true");
    props.put("jakarta.persistence.schema-generation.create-script-source",
        convertToURI(CREATEFILENAME));
    displayProperties(props);

    TestUtil.logMsg("Executing Persistence.generateSchema(...)");
    Persistence.generateSchema(getPersistenceUnitName(), props);

    clearEMAndEMF();
    try {
      TestUtil.logMsg("Persist some data");
      getEntityTransaction(true).begin();

      Student expectedStudent = new Student(1, "Neo");

      // Create 12 Courses;
      Course appliedMath = new Course(101, "AppliedMathematics");
      Course physics = new Course(102, "Physics");

      // Set enrolled students for each course
      List<Student> studentList = new ArrayList<Student>();

      studentList.add(expectedStudent);

      appliedMath.setStudents(studentList);
      physics.setStudents(studentList);

      // Set Courses for first semester
      List<Course> firstSemCourses = new ArrayList<Course>();
      firstSemCourses.add(appliedMath);
      firstSemCourses.add(physics);

      // Set Courses for each student
      expectedStudent.setCourses(firstSemCourses);

      // persist student
      getEntityManager().persist(expectedStudent);

      // persist courses
      getEntityManager().persist(appliedMath);
      getEntityManager().persist(physics);

      getEntityManager().flush();
      getEntityTransaction().commit();
      clearCache();

      Course course = getEntityManager().find(Course.class, 101);
      if (course != null) {
        if (course.courseName.equals(appliedMath.courseName)) {
          if (course.getStudents().size() == 1) {
            Student s1 = course.getStudents().get(0);
            if (expectedStudent.equals(s1)) {
              TestUtil.logTrace("Received expected result:" + s1);
              pass3 = true;
            } else {
              TestUtil.logErr("Expected:" + expectedStudent.toString());
              TestUtil.logErr("Actual:" + s1.toString());
            }
          } else {
            TestUtil.logErr("Did not get list of students of size 1, actual:"
                + course.getStudents().size());
          }
        } else {
          TestUtil.logErr("Expected Course:" + appliedMath.courseName);
          TestUtil.logErr("Actual Course:" + course.courseName);
        }
      } else {
        TestUtil.logErr("Received null result from find");
      }
    } catch (Throwable t) {
      TestUtil.logErr("Received unexpected exception", t);
    }
    clearEMAndEMF();

    TestUtil.logTrace("Execute the drop script");
    props = getPersistenceUnitProperties();
    props.put("jakarta.persistence.schema-generation.database.action", "drop");
    props.put("jakarta.persistence.schema-generation.scripts.action", "none");
    props.put("jakarta.persistence.schema-generation.drop-script-source",
        convertToURI(DROPFILENAME));
    displayProperties(props);

    TestUtil.logMsg("Executing Persistence.generateSchema(...)");
    Persistence.generateSchema(getPersistenceUnitName(), props);
    clearEMAndEMF();

    TestUtil.logMsg("Try to persist an entity, it should fail");
    try {
      getEntityTransaction(true).begin();
      Student joe = new Student(2, "Joe");
      getEntityManager().persist(joe);
      getEntityManager().flush();
      getEntityTransaction().commit();
      TestUtil.logErr(
          "An exception should have been thrown if drop had occurred successfully");
    } catch (Exception ex) {
      TestUtil.logTrace("Receive expected exception");
      pass4 = true;
    }
    try {
      getEntityTransaction(true).begin();
      Course accounting = new Course(303, "Accounting");
      getEntityManager().persist(accounting);
      getEntityManager().flush();
      getEntityTransaction().commit();
      TestUtil.logErr(
          "An exception should have been thrown if drop had occurred successfully");
    } catch (Exception ex) {
      TestUtil.logTrace("Receive expected exception");
      pass5 = true;
    }

    TestUtil.logTrace("pass1a:" + pass1a);
    TestUtil.logTrace("pass1b:" + pass1b);
    TestUtil.logTrace("pass1c:" + pass1c);
    TestUtil.logTrace("pass1d:" + pass1d);
    TestUtil.logTrace("pass1e:" + pass1e);
    TestUtil.logTrace("pass2a:" + pass2a);
    TestUtil.logTrace("pass2b:" + pass2b);
    TestUtil.logTrace("pass2c:" + pass2c);
    TestUtil.logTrace("pass2d:" + pass2d);
    TestUtil.logTrace("pass2e:" + pass2e);
    TestUtil.logTrace("pass3:" + pass3);
    TestUtil.logTrace("pass4:" + pass4);
    TestUtil.logTrace("pass5:" + pass5);
    if (!pass1a || !pass1b || !pass1c || !pass1d || !pass1e || !pass2a
        || !pass2b || !pass2c || !pass2d || !pass2e || !pass3 || !pass4
        || !pass5) {
      throw new Fault("joinTableTest failed");
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
      TestUtil.logMsg("Try to drop table SCHEMAGENSIMPLE");
      getEntityManager().createNativeQuery("DROP TABLE SCHEMAGENCOURSE")
          .executeUpdate();
      getEntityManager().createNativeQuery("DROP TABLE SCHEMAGENSTUDENT")
          .executeUpdate();
      getEntityManager()
          .createNativeQuery("DROP TABLE SCHEMAGEN_COURSE_STUDENT")
          .executeUpdate();
      getEntityTransaction().commit();
    } catch (Throwable t) {
      TestUtil.logMsg(
          "AN EXCEPTION WAS THROWN DURING DROPS, IT MAY OR MAY NOT BE A PROBLEM, "
              + t.getMessage());
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
        clearEntityTransaction();

        // ensure that we close the EM and EMF before proceeding.
        clearEMAndEMF();
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception in removeTestData:", re);
      }
    }
  }

}
