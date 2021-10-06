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

package com.sun.ts.tests.jpa.se.schemaGeneration.annotations.orderColumn;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

public class Client extends PMClientBase {

  private static final long serialVersionUID = 22L;

  String schemaGenerationDir = null;

  Employee expectedEmployee = null;

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
   * @testName: orderColumnTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:2118.14; PERSISTENCE:SPEC:2118.9;
   * 
   * @test_Strategy: Test the @OrderColumn annotation
   */
  public void orderColumnTest() throws Fault {
    boolean pass1a = false;
    boolean pass1b = false;
    boolean pass1c = false;
    boolean pass2a = false;
    boolean pass2b = false;
    boolean pass2c = false;
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
    expected.add("CREATE TABLE SCHEMAGENEMP");
    expected.add("EMPID");
    expected.add("FK_DEPT");
    expected.add("THEORDERCOLUMN");
    expected.add("PRIMARY KEY (EMPID)");
    pass1a = findDataInFile(f1, expected);

    expected.clear();
    expected.add("CREATE TABLE SCHEMAGENDEPT");
    expected.add("DEPTID");
    expected.add("PRIMARY KEY (DEPTID)");
    pass1b = findDataInFile(f1, expected);

    pass1c = findDataInFile(f1,
        "ALTER TABLE SCHEMAGENEMP ADD CONSTRAINT MYCONSTRANT FOREIGN KEY (FK_DEPT) REFERENCES SCHEMAGENDEPT (DEPTID)");

    /*
     * CREATE TABLE SCHEMAGENDEPT (DEPTID INTEGER NOT NULL, PRIMARY KEY
     * (DEPTID)) CREATE TABLE SCHEMAGENEMP (EMPID INTEGER NOT NULL, FK_DEPT
     * INTEGER, THEORDERCOLUMN INTEGER, PRIMARY KEY (EMPID)) ALTER TABLE
     * SCHEMAGENEMP ADD CONSTRAINT MYCONSTRANT FOREIGN KEY (FK_DEPT) REFERENCES
     * SCHEMAGENDEPT (DEPTID)
     */

    pass2a = findDataInFile(f2, "DROP TABLE SCHEMAGENEMP");
    pass2b = findDataInFile(f2, "DROP TABLE SCHEMAGENDEPT");
    pass2c = findDataInFile(f2, "ALTER TABLE SCHEMAGENEMP DROP");

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

      Department d1 = new Department(50);
      getEntityManager().persist(d1);

      expectedEmployee = new Employee(20, d1);
      final Employee e2 = new Employee(40, d1);
      final Employee e3 = new Employee(60, d1);
      getEntityManager().persist(expectedEmployee);
      getEntityManager().persist(e2);
      getEntityManager().persist(e3);

      List<Employee> expectedEmployees = new ArrayList<Employee>();
      expectedEmployees.add(e3);
      expectedEmployees.add(expectedEmployee);
      expectedEmployees.add(e2);

      d1.setEmployees(expectedEmployees);
      getEntityManager().merge(d1);

      TestUtil.logTrace("persisted Entity Data");
      getEntityManager().flush();
      getEntityTransaction().commit();
      clearCache();
      getEntityTransaction(true).begin();
      TypedQuery<Employee> q = getEntityManager().createQuery(
          "SELECT e FROM Department d JOIN d.employees e WHERE d.deptId = 50 AND INDEX(e) = 1",
          Employee.class);
      Employee e = q.getSingleResult();
      if (e != null) {
        if (e.equals(expectedEmployee)) {
          TestUtil.logTrace("Received expected:" + expectedEmployee);
          pass3 = true;
        } else {
          TestUtil.logErr("expected:" + expectedEmployee + ", actual:" + e);
        }
      } else {
        TestUtil.logErr("Query of INDEX(1) returned null result");
      }
      getEntityTransaction().commit();
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
      Employee e2 = new Employee(2);
      getEntityManager().persist(e2);
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
      Department d = new Department(2);
      getEntityManager().persist(d);
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
    TestUtil.logTrace("pass2a:" + pass2a);
    TestUtil.logTrace("pass2b:" + pass2b);
    TestUtil.logTrace("pass2c:" + pass2c);
    TestUtil.logTrace("pass3:" + pass3);
    TestUtil.logTrace("pass4:" + pass4);
    TestUtil.logTrace("pass5:" + pass5);
    if (!pass1a || !pass1b || !pass1c || !pass2a || !pass2b || !pass2c || !pass3
        || !pass4 || !pass5) {
      throw new Fault("orderColumnTest failed");
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
      getEntityManager().createNativeQuery("DROP TABLE SCHEMAGENEMP")
          .executeUpdate();
      getEntityManager().createNativeQuery("DROP TABLE SCHEMAGENDEPT")
          .executeUpdate();
      getEntityTransaction().commit();
    } catch (Throwable t) {
      TestUtil.logMsg(
          "AN EXCEPTION WAS THROWN DURING DROP TABLE, IT MAY OR MAY NOT BE A PROBLEM, "
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
