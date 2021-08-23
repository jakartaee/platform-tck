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

/*
    NOTES:

    Since the JPA Alternate Provider is enabled at all times and returns
    false when Persistence.generateSchema(...) is called, the assertion
    PERSISTENCE:SPEC:2480 is covered.
 */
package com.sun.ts.tests.jpa.se.schemaGeneration.scripts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Client extends PMClientBase {

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
   * @testName: scriptsURLTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1899; PERSISTENCE:SPEC:1899.2;
   * PERSISTENCE:SPEC:1893.2; PERSISTENCE:SPEC:1898.3; PERSISTENCE:SPEC:1898.4;
   * PERSISTENCE:SPEC:1898.8; PERSISTENCE:SPEC:1898.9;
   * 
   * @test_Strategy: create scripts via createEntityManagerFactory using a URL
   * location
   */
  public void scriptsURLTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;

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

    pass1 = findDataInFile(f1, "create table schemaGenSimple");
    pass2 = findDataInFile(f2, "drop table schemaGenSimple");
    if (!pass1 || !pass2) {
      throw new Fault("scriptsURLTest failed");
    }
  }

  /*
   * @testName: scriptsPrintWriterTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1899; PERSISTENCE:SPEC:1899.2;
   * PERSISTENCE:SPEC:1898.8; PERSISTENCE:SPEC:1898.9;
   * 
   * @test_Strategy: create scripts via createEntityManagerFactory using a
   * PrintWriter
   */
  public void scriptsPrintWriterTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;

    TestUtil.logMsg("Create the script(s)");

    final String CREATEFILENAME = schemaGenerationDir + File.separator
        + "create_" + this.sTestCase + ".sql";
    final String DROPFILENAME = schemaGenerationDir + File.separator + "drop_"
        + this.sTestCase + ".sql";

    File f1 = null;
    PrintWriter pw1 = null;
    try {
      f1 = new File(CREATEFILENAME);
      TestUtil.logTrace("Deleting previous create script");
      deleteItem(f1);
      pw1 = new PrintWriter(new File(CREATEFILENAME));
    } catch (Exception ex) {
      TestUtil.logErr("Unexpected exception occurred:" + ex);
    }
    File f2 = null;
    PrintWriter pw2 = null;
    try {
      f2 = new File(DROPFILENAME);
      TestUtil.logTrace("Deleting previous drop script");
      deleteItem(f2);

      pw2 = new PrintWriter(new File(DROPFILENAME));
    } catch (Exception ex) {
      TestUtil.logErr("Unexpected exception occurred:" + ex);
    }

    Properties props = getPersistenceUnitProperties();
    props.put("jakarta.persistence.schema-generation.database.action", "none");
    props.put("jakarta.persistence.schema-generation.scripts.action",
        "drop-and-create");
    props.put("jakarta.persistence.schema-generation.create-database-schemas",
        "false");
    props.put("jakarta.persistence.schema-generation.scripts.create-target", pw1);
    props.put("jakarta.persistence.schema-generation.scripts.drop-target", pw2);

    displayProperties(props);

    TestUtil.logMsg("Executing Persistence.createEntityManagerFactory(...)");
    EntityManagerFactory emf = Persistence
        .createEntityManagerFactory(getPersistenceUnitName(), props);
    emf.close();
    emf = null;

    if (pw1 != null)
      pw1.close();
    if (pw2 != null)
      pw2.close();

    TestUtil.logMsg("Check script(s) content");

    pass1 = findDataInFile(f1, "create table schemaGenSimple");
    pass2 = findDataInFile(f2, "drop table schemaGenSimple");
    if (!pass1 || !pass2) {
      throw new Fault("scriptsPrintWriterTest failed");
    }
  }

  /*
   * @testName: scriptsURL2Test
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1899; PERSISTENCE:JAVADOC:3335;
   * PERSISTENCE:SPEC:1899.1; PERSISTENCE:SPEC:2474; PERSISTENCE:SPEC:2475;
   * PERSISTENCE:SPEC:2476; PERSISTENCE:SPEC:2478; PERSISTENCE:SPEC:2480;
   * PERSISTENCE:SPEC:1915;
   * 
   * @test_Strategy: create scripts via Persistence.generateSchema using a URL
   * location
   */
  public void scriptsURL2Test() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;

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

    TestUtil.logMsg("Executing Persistence.generateSchema(...)");
    Persistence.generateSchema(getPersistenceUnitName(), props);

    TestUtil.logMsg("Check script(s) content");

    pass1 = findDataInFile(f1, "create table schemaGenSimple");
    pass2 = findDataInFile(f2, "drop table schemaGenSimple");
    if (!pass1 || !pass2) {
      throw new Fault("scriptsURL2Test failed");
    }
  }

  /*
   * @testName: scriptsPrintWriter2Test
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1899; PERSISTENCE:JAVADOC:3335;
   * PERSISTENCE:SPEC:1899.1; PERSISTENCE:SPEC:2474; PERSISTENCE:SPEC:2475;
   * PERSISTENCE:SPEC:2476; PERSISTENCE:SPEC:2478; PERSISTENCE:SPEC:2480;
   * PERSISTENCE:SPEC:1915;
   * 
   * @test_Strategy: create scripts via Persistence.generateSchema using a
   * PrintWriter
   */
  public void scriptsPrintWriter2Test() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;

    TestUtil.logMsg("Create the script(s)");

    final String CREATEFILENAME = schemaGenerationDir + File.separator
        + "create_" + this.sTestCase + ".sql";
    final String DROPFILENAME = schemaGenerationDir + File.separator + "drop_"
        + this.sTestCase + ".sql";

    File f1 = null;
    PrintWriter pw1 = null;
    try {
      f1 = new File(CREATEFILENAME);
      TestUtil.logTrace("Deleting previous create script");
      deleteItem(f1);
      pw1 = new PrintWriter(new File(CREATEFILENAME));
    } catch (Exception ex) {
      TestUtil.logErr("Unexpected exception occurred:" + ex);
    }

    File f2 = null;
    PrintWriter pw2 = null;
    try {
      f2 = new File(DROPFILENAME);
      TestUtil.logTrace("Deleting previous drop script");
      deleteItem(f2);
      pw2 = new PrintWriter(new File(DROPFILENAME));
    } catch (Exception ex) {
      TestUtil.logErr("Unexpected exception occurred:" + ex);
    }

    Properties props = getPersistenceUnitProperties();
    props.put("jakarta.persistence.schema-generation.database.action", "none");
    props.put("jakarta.persistence.schema-generation.scripts.action",
        "drop-and-create");
    props.put("jakarta.persistence.schema-generation.create-database-schemas",
        "false");
    props.put("jakarta.persistence.schema-generation.scripts.create-target", pw1);
    props.put("jakarta.persistence.schema-generation.scripts.drop-target", pw2);

    displayProperties(props);

    TestUtil.logMsg("Executing Persistence.generateSchema(...)");
    Persistence.generateSchema(getPersistenceUnitName(), props);

    if (pw1 != null)
      pw1.close();
    if (pw2 != null)
      pw2.close();

    TestUtil.logMsg("Check script(s) content");

    pass1 = findDataInFile(f1, "create table schemaGenSimple");
    pass2 = findDataInFile(f2, "drop table schemaGenSimple");
    if (!pass1 || !pass2) {
      throw new Fault("scriptsPrintWriter2Test failed");
    }
  }

  /*
   * @testName: databaseAndScriptsURLTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1899; PERSISTENCE:SPEC:1899.2;
   * PERSISTENCE:SPEC:1898.3
   * 
   * @test_Strategy: create scripts and generate the schema via
   * createEntityManagerFactory using a URL location
   */
  public void databaseAndScriptsURLTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;

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
    props.put("jakarta.persistence.schema-generation.database.action",
        "drop-and-create");
    props.put("jakarta.persistence.schema-generation.scripts.action",
        "drop-and-create");
    props.put("jakarta.persistence.schema-generation.create-database-schemas",
        "true");
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

    pass2 = findDataInFile(f1, "create table schemaGenSimple");
    pass3 = findDataInFile(f2, "drop table schemaGenSimple");
    clearEMAndEMF();

    try {
      TestUtil.logMsg("Persist some data");
      getEntityTransaction(true).begin();
      Simple s = new Simple(1, "1");
      getEntityManager().persist(s);
      getEntityTransaction().commit();
      clearCache();
      getEntityTransaction().begin();
      Simple s2 = getEntityManager().find(Simple.class, 1);
      getEntityTransaction().commit();
      if (s.equals(s2)) {
        TestUtil.logTrace("Received expected result:" + s.toString());
        pass1 = true;
      } else {
        TestUtil.logErr("Expected:" + s.toString());
        TestUtil.logErr("Actual:" + s2.toString());
      }
    } catch (Throwable t) {
      TestUtil.logErr("Received unexpected exception", t);
    }
    if (!pass1 || !pass2 || !pass3) {
      throw new Fault("databaseAndScriptsURLTest failed");
    }
  }

  /*
   * @testName: databaseAndScriptsPrintWriterTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1899; PERSISTENCE:SPEC:1899.2;
   * 
   * @test_Strategy: create scripts and generate the schema via
   * createEntityManagerFactory using a PrintWriter
   */
  public void databaseAndScriptsPrintWriterTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;

    TestUtil.logMsg("Create the script(s)");

    final String CREATEFILENAME = schemaGenerationDir + File.separator
        + "create_" + this.sTestCase + ".sql";
    final String DROPFILENAME = schemaGenerationDir + File.separator + "drop_"
        + this.sTestCase + ".sql";

    File f1 = null;
    PrintWriter pw1 = null;
    try {
      f1 = new File(CREATEFILENAME);
      TestUtil.logTrace("Deleting previous create script");
      deleteItem(f1);
      pw1 = new PrintWriter(new File(CREATEFILENAME));
    } catch (Exception ex) {
      TestUtil.logErr("Unexpected exception occurred:" + ex);
    }
    File f2 = null;
    PrintWriter pw2 = null;
    try {
      f2 = new File(DROPFILENAME);
      TestUtil.logTrace("Deleting previous drop script");
      deleteItem(f2);
      pw2 = new PrintWriter(new File(DROPFILENAME));
    } catch (Exception ex) {
      TestUtil.logErr("Unexpected exception occurred:" + ex);
    }

    Properties props = getPersistenceUnitProperties();
    props.put("jakarta.persistence.schema-generation.database.action",
        "drop-and-create");
    props.put("jakarta.persistence.schema-generation.scripts.action",
        "drop-and-create");
    props.put("jakarta.persistence.schema-generation.create-database-schemas",
        "true");
    props.put("jakarta.persistence.schema-generation.scripts.create-target", pw1);
    props.put("jakarta.persistence.schema-generation.scripts.drop-target", pw2);

    displayProperties(props);

    TestUtil.logMsg("Executing Persistence.createEntityManagerFactory(...)");
    EntityManagerFactory emf = Persistence
        .createEntityManagerFactory(getPersistenceUnitName(), props);
    emf.close();
    emf = null;
    if (pw1 != null)
      pw1.close();
    if (pw2 != null)
      pw2.close();

    TestUtil.logMsg("Check script(s) content");

    pass2 = findDataInFile(f1, "create table schemaGenSimple");
    pass3 = findDataInFile(f2, "drop table schemaGenSimple");
    clearEMAndEMF();

    try {
      TestUtil.logMsg("Persist some data");
      getEntityTransaction(true).begin();
      Simple s = new Simple(1, "1");
      getEntityManager().persist(s);
      getEntityTransaction().commit();
      clearCache();
      getEntityTransaction().begin();
      Simple s2 = getEntityManager().find(Simple.class, 1);
      getEntityTransaction().commit();
      if (s.equals(s2)) {
        TestUtil.logTrace("Received expected result:" + s.toString());
        pass1 = true;
      } else {
        TestUtil.logErr("Expected:" + s.toString());
        TestUtil.logErr("Actual:" + s2.toString());
      }
    } catch (Throwable t) {
      TestUtil.logErr("Received unexpected exception", t);
    }
    if (!pass1 || !pass2 || !pass3) {
      throw new Fault("databaseAndScriptsPrintWriterTest failed");
    }
  }

  /*
   * @testName: databaseAndScriptsURL2Test
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1899; PERSISTENCE:SPEC:1899.1;
   * PERSISTENCE:SPEC:2474; PERSISTENCE:SPEC:2475; PERSISTENCE:SPEC:2476;
   * PERSISTENCE:SPEC:2478; PERSISTENCE:JAVADOC:3357; PERSISTENCE:SPEC:2480;
   * PERSISTENCE:SPEC:1915;
   * 
   * @test_Strategy: create scripts and generate the schema via
   * Persistence.generateSchema using a URL location
   */
  public void databaseAndScriptsURL2Test() throws Fault {
    boolean pass = false;

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
    props.put("jakarta.persistence.schema-generation.database.action", "create");
    props.put("jakarta.persistence.schema-generation.scripts.action",
        "drop-and-create");
    props.put("jakarta.persistence.schema-generation.create-database-schemas",
        "true");
    props.put("jakarta.persistence.schema-generation.scripts.create-target",
        convertToURI(CREATEFILENAME));
    props.put("jakarta.persistence.schema-generation.scripts.drop-target",
        convertToURI(DROPFILENAME));

    displayProperties(props);

    TestUtil.logMsg("Executing Persistence.generateSchema(...)");
    Persistence.generateSchema(getPersistenceUnitName(), props);
    clearEMAndEMF();

    try {
      TestUtil.logMsg("Persist some data");
      getEntityTransaction(true).begin();
      Simple s = new Simple(1, "1");
      getEntityManager().persist(s);
      getEntityTransaction().commit();
      clearCache();
      getEntityTransaction().begin();
      Simple s2 = getEntityManager().find(Simple.class, 1);
      getEntityTransaction().commit();
      if (s.equals(s2)) {
        TestUtil.logTrace("Received expected result:" + s.toString());
        pass = true;
      } else {
        TestUtil.logErr("Expected:" + s.toString());
        TestUtil.logErr("Actual:" + s2.toString());
      }
    } catch (Throwable t) {
      TestUtil.logErr("Received unexpected exception", t);
    }
    if (!pass) {
      throw new Fault("databaseAndScriptsURL2Test failed");
    }
  }

  /*
   * @testName: databaseAndScriptsPrintWriter2Test
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1899; PERSISTENCE:SPEC:1899.1;
   * PERSISTENCE:SPEC:2474; PERSISTENCE:SPEC:2475; PERSISTENCE:SPEC:2476;
   * PERSISTENCE:SPEC:2478; PERSISTENCE:JAVADOC:3357; PERSISTENCE:SPEC:2480;
   * PERSISTENCE:SPEC:1893; PERSISTENCE:SPEC:1893.1; PERSISTENCE:SPEC:1915;
   * 
   * @test_Strategy: create scripts and generate the schema via
   * Persistence.generateSchema using a PrintWriter
   */
  public void databaseAndScriptsPrintWriter2Test() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;

    TestUtil.logMsg("Create the script(s)");

    final String CREATEFILENAME = schemaGenerationDir + File.separator
        + "create_" + this.sTestCase + ".sql";
    final String DROPFILENAME = schemaGenerationDir + File.separator + "drop_"
        + this.sTestCase + ".sql";

    File f1 = null;
    PrintWriter pw1 = null;
    try {
      f1 = new File(CREATEFILENAME);
      TestUtil.logTrace("Deleting previous create script");
      deleteItem(f1);
      pw1 = new PrintWriter(new File(CREATEFILENAME));
    } catch (Exception ex) {
      TestUtil.logErr("Unexpected exception occurred:" + ex);
    }
    File f2 = null;
    PrintWriter pw2 = null;
    try {
      f2 = new File(DROPFILENAME);
      TestUtil.logTrace("Deleting previous drop script");
      deleteItem(f2);
      pw2 = new PrintWriter(new File(DROPFILENAME));
    } catch (Exception ex) {
      TestUtil.logErr("Unexpected exception occurred:" + ex);
    }

    Properties props = getPersistenceUnitProperties();
    props.put("jakarta.persistence.schema-generation.database.action",
        "drop-and-create");
    props.put("jakarta.persistence.schema-generation.scripts.action",
        "drop-and-create");
    props.put("jakarta.persistence.schema-generation.create-database-schemas",
        "true");
    props.put("jakarta.persistence.schema-generation.scripts.create-target", pw1);
    props.put("jakarta.persistence.schema-generation.scripts.drop-target", pw2);

    displayProperties(props);

    TestUtil.logMsg("Executing Persistence.generateSchema(...)");
    Persistence.generateSchema(getPersistenceUnitName(), props);
    if (pw1 != null)
      pw1.close();
    if (pw2 != null)
      pw2.close();

    clearEMAndEMF();

    try {
      TestUtil.logMsg("Persist some data");
      getEntityTransaction(true).begin();
      Simple s = new Simple(1, "1");
      getEntityManager().persist(s);
      getEntityTransaction().commit();
      clearCache();
      getEntityTransaction().begin();
      Simple s2 = getEntityManager().find(Simple.class, 1);
      getEntityTransaction().commit();
      if (s.equals(s2)) {
        TestUtil.logTrace("Received expected result:" + s.toString());
        pass1 = true;
      } else {
        TestUtil.logErr("Expected:" + s.toString());
        TestUtil.logErr("Actual:" + s2.toString());
      }
    } catch (Throwable t) {
      TestUtil.logErr("Received unexpected exception", t);
    }

    TestUtil.logMsg("Check script(s) content");

    pass2 = findDataInFile(f1, "create table schemaGenSimple");
    pass3 = findDataInFile(f2, "drop table schemaGenSimple");
    if (!pass1 || !pass2 || !pass3) {
      throw new Fault("databaseAndScriptsPrintWriter2Test failed");
    }
  }

  /*
   * @testName: databaseCreateTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1899; PERSISTENCE:SPEC:1899.1;
   * PERSISTENCE:SPEC:2474; PERSISTENCE:SPEC:2475; PERSISTENCE:SPEC:2476;
   * PERSISTENCE:SPEC:2478; PERSISTENCE:JAVADOC:3357; PERSISTENCE:SPEC:2480;
   * PERSISTENCE:SPEC:1898.3; PERSISTENCE:SPEC:1898.4; PERSISTENCE:SPEC:1915;
   * 
   * @test_Strategy: create scripts and generate the schema via
   * Persistence.generateSchema using a PrintWriter
   */
  public void databaseCreateTest() throws Fault {
    boolean pass = false;

    Properties props = getPersistenceUnitProperties();

    props.put("jakarta.persistence.schema-generation.database.action", "create");
    props.put("jakarta.persistence.schema-generation.scripts.action", "none");
    props.put("jakarta.persistence.schema-generation.create-database-schemas",
        "true");

    displayProperties(props);

    TestUtil.logMsg("Executing Persistence.generateSchema(...)");
    Persistence.generateSchema(getPersistenceUnitName(), props);

    clearEMAndEMF();

    try {
      TestUtil.logMsg("Persist some data");
      getEntityTransaction(true).begin();
      Simple s = new Simple(1, "1");
      getEntityManager().persist(s);
      getEntityTransaction().commit();
      clearCache();
      getEntityTransaction().begin();
      Simple s2 = getEntityManager().find(Simple.class, 1);
      getEntityTransaction().commit();
      if (s.equals(s2)) {
        TestUtil.logTrace("Received expected result:" + s.toString());
        pass = true;
      } else {
        TestUtil.logErr("Expected:" + s.toString());
        TestUtil.logErr("Actual:" + s2.toString());
      }
    } catch (Throwable t) {
      TestUtil.logErr("Received unexpected exception", t);
    }

    if (!pass) {
      throw new Fault("databaseCreateTest failed");
    }
  }

  /*
   * @testName: executeCreateScriptURLTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1899; PERSISTENCE:SPEC:1899.1;
   * PERSISTENCE:SPEC:2474; PERSISTENCE:SPEC:2475; PERSISTENCE:SPEC:2476;
   * PERSISTENCE:SPEC:2478; PERSISTENCE:JAVADOC:3357; PERSISTENCE:SPEC:2480;
   * PERSISTENCE:SPEC:1898.4; PERSISTENCE:SPEC:1898.12;
   * PERSISTENCE:SPEC:1898.14; PERSISTENCE:SPEC:1915;
   * 
   * @test_Strategy: create a create script then use a URL location to execute
   * it
   */
  public void executeCreateScriptURLTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;

    TestUtil.logMsg("Create the script(s)");
    final String CREATEFILENAME = schemaGenerationDir + File.separator
        + "create_" + this.sTestCase + ".sql";

    Properties props = getPersistenceUnitProperties();

    File f1 = new File(CREATEFILENAME);
    TestUtil.logTrace("Deleting previous create script");
    deleteItem(f1);

    props.put("jakarta.persistence.schema-generation.database.action", "none");
    props.put("jakarta.persistence.schema-generation.scripts.action", "create");
    props.put("jakarta.persistence.schema-generation.create-database-schemas",
        "false");
    props.put("jakarta.persistence.schema-generation.scripts.create-target",
        convertToURI(CREATEFILENAME));

    displayProperties(props);

    TestUtil.logMsg("Executing Persistence.createEntityManagerFactory(...)");
    EntityManagerFactory emf = Persistence
        .createEntityManagerFactory(getPersistenceUnitName(), props);
    emf.close();
    emf = null;

    TestUtil.logMsg("Check script(s) content");

    pass1 = findDataInFile(f1, "create table schemaGenSimple");

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
      Simple s = new Simple(1, "1");
      getEntityManager().persist(s);
      getEntityTransaction().commit();
      clearCache();
      getEntityTransaction().begin();
      Simple s2 = getEntityManager().find(Simple.class, 1);
      getEntityTransaction().commit();
      if (s.equals(s2)) {
        TestUtil.logTrace("Received expected result:" + s.toString());
        pass2 = true;
      } else {
        TestUtil.logErr("Expected:" + s.toString());
        TestUtil.logErr("Actual:" + s2.toString());
      }
    } catch (Throwable t) {
      TestUtil.logErr("Received unexpected exception", t);
    }
    if (!pass1 || !pass2) {
      throw new Fault("executeCreateScriptURLTest failed");
    }
  }

  /*
   * @testName: executeCreateScriptReaderTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1899; PERSISTENCE:SPEC:1899.1;
   * PERSISTENCE:SPEC:2474; PERSISTENCE:SPEC:2475; PERSISTENCE:SPEC:2476;
   * PERSISTENCE:SPEC:2478; PERSISTENCE:JAVADOC:3357; PERSISTENCE:SPEC:2480;
   * PERSISTENCE:SPEC:1870.1; PERSISTENCE:SPEC:1898.14; PERSISTENCE:SPEC:1915;
   * 
   * @test_Strategy: create a create script then use a Reader to load and
   * execute it
   */
  public void executeCreateScriptReaderTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;

    TestUtil.logMsg("Create the script(s)");

    final String CREATEFILENAME = schemaGenerationDir + File.separator
        + "create_" + this.sTestCase + ".sql";

    File f1 = null;
    PrintWriter pw1 = null;
    try {
      f1 = new File(CREATEFILENAME);
      TestUtil.logTrace("Deleting previous create script");
      deleteItem(f1);
      pw1 = new PrintWriter(new File(CREATEFILENAME));
    } catch (Exception ex) {
      TestUtil.logErr("Unexpected exception occurred:" + ex);
    }

    Properties props = getPersistenceUnitProperties();
    props.put("jakarta.persistence.schema-generation.database.action", "none");
    props.put("jakarta.persistence.schema-generation.scripts.action", "create");
    props.put("jakarta.persistence.schema-generation.create-database-schemas",
        "false");
    props.put("jakarta.persistence.schema-generation.scripts.create-target", pw1);

    displayProperties(props);

    TestUtil.logMsg("Executing Persistence.generateSchema(...)");
    Persistence.generateSchema(getPersistenceUnitName(), props);

    TestUtil.logMsg("Check script(s) content");

    pass1 = findDataInFile(f1, "create table schemaGenSimple");

    TestUtil.logMsg("Execute the script");
    props = getPersistenceUnitProperties();

    Reader reader = null;
    try {
      reader = new BufferedReader(new FileReader(f1));
    } catch (Exception ex) {
      TestUtil.logErr("Unexpected exception occurred:" + ex);
    }

    props.put("jakarta.persistence.schema-generation.database.action", "create");
    props.put("jakarta.persistence.schema-generation.scripts.action", "none");
    props.put("jakarta.persistence.schema-generation.create-database-schemas",
        "true");
    props.put("jakarta.persistence.schema-generation.create-script-source",
        reader);
    displayProperties(props);

    TestUtil.logMsg("Executing Persistence.generateSchema(...)");
    Persistence.generateSchema(getPersistenceUnitName(), props);
    if (pw1 != null)
      pw1.close();

    clearEMAndEMF();

    try {
      TestUtil.logMsg("Persist some data");
      getEntityTransaction(true).begin();
      Simple s = new Simple(1, "1");
      getEntityManager().persist(s);
      getEntityTransaction().commit();
      clearCache();
      getEntityTransaction().begin();
      Simple s2 = getEntityManager().find(Simple.class, 1);
      getEntityTransaction().commit();
      if (s.equals(s2)) {
        TestUtil.logTrace("Received expected result:" + s.toString());
        pass2 = true;
      } else {
        TestUtil.logErr("Expected:" + s.toString());
        TestUtil.logErr("Actual:" + s2.toString());
      }
    } catch (Throwable t) {
      TestUtil.logErr("Received unexpected exception", t);
    }
    if (!pass1 || !pass2) {
      throw new Fault("executeCreateScriptReaderTest failed");
    }
  }

  /*
   * @testName: executeDropScriptURLTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1899; PERSISTENCE:SPEC:1899.1;
   * PERSISTENCE:SPEC:1899.2; PERSISTENCE:SPEC:2474; PERSISTENCE:SPEC:2475;
   * PERSISTENCE:SPEC:2476; PERSISTENCE:SPEC:2478; PERSISTENCE:JAVADOC:3357;
   * PERSISTENCE:SPEC:2480; PERSISTENCE:SPEC:1870.2; PERSISTENCE:SPEC:1898.3;
   * PERSISTENCE:SPEC:1898.4; PERSISTENCE:SPEC:1898.12;
   * PERSISTENCE:SPEC:1898.15; PERSISTENCE:SPEC:1915;
   * 
   * @test_Strategy: create drop script using a URL location and execute it
   */
  public void executeDropScriptURLTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;

    TestUtil.logMsg("Create the script(s)");
    final String DROPFILENAME = schemaGenerationDir + File.separator + "drop_"
        + this.sTestCase + ".sql";

    File f2 = new File(DROPFILENAME);
    TestUtil.logTrace("Deleting previous drop script");
    deleteItem(f2);
    Properties props = getPersistenceUnitProperties();
    props.put("jakarta.persistence.schema-generation.database.action", "none");
    props.put("jakarta.persistence.schema-generation.scripts.action", "drop");
    props.put("jakarta.persistence.schema-generation.scripts.drop-target",
        convertToURI(DROPFILENAME));

    displayProperties(props);

    TestUtil.logMsg("Executing Persistence.createEntityManagerFactory(...)");
    EntityManagerFactory emf = Persistence
        .createEntityManagerFactory(getPersistenceUnitName(), props);
    emf.close();
    emf = null;

    TestUtil.logMsg("Check script(s) content");

    pass1 = findDataInFile(f2, "drop table schemaGenSimple");

    TestUtil.logTrace("Generate schema");
    props = getPersistenceUnitProperties();
    props.put("jakarta.persistence.schema-generation.database.action", "create");
    props.put("jakarta.persistence.schema-generation.scripts.action", "none");
    props.put("jakarta.persistence.schema-generation.create-database-schemas",
        "true");
    displayProperties(props);

    TestUtil.logMsg("Executing Persistence.generateSchema(...)");
    Persistence.generateSchema(getPersistenceUnitName(), props);

    clearEMAndEMF();

    try {
      TestUtil.logMsg("Persist some data");
      getEntityTransaction(true).begin();
      Simple s = new Simple(1, "1");
      getEntityManager().persist(s);
      getEntityTransaction().commit();
      clearCache();
      getEntityTransaction().begin();
      Simple s2 = getEntityManager().find(Simple.class, 1);
      getEntityTransaction().commit();
      if (s.equals(s2)) {
        TestUtil.logTrace("Received expected result:" + s.toString());
        pass2 = true;
      } else {
        TestUtil.logErr("Expected:" + s.toString());
        TestUtil.logErr("Actual:" + s2.toString());
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
      Simple s3 = new Simple(2, "2");
      getEntityManager().persist(s3);
      getEntityTransaction().commit();
      TestUtil.logErr(
          "An exception should have been thrown if drop had occurred successfully");
    } catch (Exception ex) {
      TestUtil.logTrace("Received expected exception");
      pass3 = true;
    }

    if (!pass1 || !pass2 || !pass3) {
      throw new Fault("executeDropScriptURLTest failed");
    }
  }

  /*
   * @testName: executeDropScriptReaderTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1899; PERSISTENCE:SPEC:1899.1;
   * PERSISTENCE:SPEC:2474; PERSISTENCE:SPEC:2475; PERSISTENCE:SPEC:2476;
   * PERSISTENCE:SPEC:2478; PERSISTENCE:JAVADOC:3357; PERSISTENCE:SPEC:2480;
   * PERSISTENCE:SPEC:1898.15; PERSISTENCE:SPEC:1915;
   * 
   * @test_Strategy: create drop script then use a Reader to load and execute
   * it.
   */
  public void executeDropScriptReaderTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;

    TestUtil.logMsg("Create the script(s)");

    final String DROPFILENAME = schemaGenerationDir + File.separator + "drop_"
        + this.sTestCase + ".sql";

    File f2 = null;
    PrintWriter pw2 = null;
    try {
      f2 = new File(DROPFILENAME);
      TestUtil.logTrace("Deleting previous drop script");
      deleteItem(f2);
      pw2 = new PrintWriter(new File(DROPFILENAME));
    } catch (Exception ex) {
      TestUtil.logErr("Unexpected exception occurred:" + ex);
    }

    Properties props = getPersistenceUnitProperties();
    props.put("jakarta.persistence.schema-generation.database.action", "none");
    props.put("jakarta.persistence.schema-generation.scripts.action", "drop");
    props.put("jakarta.persistence.schema-generation.scripts.drop-target", pw2);

    displayProperties(props);

    TestUtil.logMsg("Executing Persistence.generateSchema(...)");
    Persistence.generateSchema(getPersistenceUnitName(), props);

    if (pw2 != null)
      pw2.close();

    TestUtil.logMsg("Check script(s) content");

    pass1 = findDataInFile(f2, "drop table schemaGenSimple");

    TestUtil.logTrace("Generate schema");
    props = getPersistenceUnitProperties();
    props.put("jakarta.persistence.schema-generation.database.action", "create");
    props.put("jakarta.persistence.schema-generation.scripts.action", "none");
    props.put("jakarta.persistence.schema-generation.create-database-schemas",
        "true");
    displayProperties(props);

    TestUtil.logMsg("Executing Persistence.generateSchema(...)");
    Persistence.generateSchema(getPersistenceUnitName(), props);

    clearEMAndEMF();

    try {
      TestUtil.logTrace("Try to persist some data");
      getEntityTransaction(true).begin();
      Simple s = new Simple(1, "1");
      getEntityManager().persist(s);
      getEntityTransaction().commit();
      clearCache();
      getEntityTransaction().begin();
      Simple s2 = getEntityManager().find(Simple.class, 1);
      getEntityTransaction().commit();
      if (s.equals(s2)) {
        TestUtil.logTrace("Received expected result:" + s.toString());
        pass2 = true;
      } else {
        TestUtil.logErr("Expected:" + s.toString());
        TestUtil.logErr("Actual:" + s2.toString());
      }
    } catch (Throwable t) {
      TestUtil.logErr("Received unexpected exception", t);
    }
    clearEMAndEMF();
    TestUtil.logTrace("Execute the drop script");

    Reader reader = null;
    try {
      reader = new BufferedReader(new FileReader(f2));
    } catch (Exception ex) {
      TestUtil.logErr("Unexpected exception occurred:" + ex);
    }
    props = getPersistenceUnitProperties();
    props.put("jakarta.persistence.schema-generation.database.action", "drop");
    props.put("jakarta.persistence.schema-generation.scripts.action", "none");
    props.put("jakarta.persistence.schema-generation.drop-script-source", reader);
    displayProperties(props);

    TestUtil.logMsg("Executing Persistence.generateSchema(...)");
    Persistence.generateSchema(getPersistenceUnitName(), props);
    clearEMAndEMF();

    TestUtil.logMsg(
        "Try to persist an entity, it should fail because table should not exist");
    try {
      getEntityTransaction(true).begin();
      Simple s3 = new Simple(2, "2");
      getEntityManager().persist(s3);
      getEntityTransaction().commit();
      TestUtil.logErr(
          "An exception should have been thrown if drop script executed successfully");
    } catch (Exception ex) {
      TestUtil.logTrace("Received expected exception");
      pass3 = true;
    }
    if (!pass1 || !pass2 || !pass3) {
      throw new Fault("executeDropScriptReaderTest failed");
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
      getEntityManager().createNativeQuery("DROP TABLE SCHEMAGENSIMPLE")
          .executeUpdate();
      getEntityTransaction().commit();
    } catch (Throwable t) {
      TestUtil.logMsg(
          "AN EXCEPTION WAS THROWN DURING DROP TABLE SCHEMAGENSIMPLE, IT MAY OR MAY NOT BE A PROBLEM, "
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
