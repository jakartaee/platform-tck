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
 * @(#)dbMetaClient6.java	1.26 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.dbMeta.dbMeta6;

import java.io.*;
import java.util.*;

import java.sql.*;
import javax.sql.*;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.rmi.RemoteException;

import com.sun.javatest.Status;
import com.sun.ts.tests.jdbc.ee.common.*;

// Merant DataSource class
//import com.merant.sequelink.jdbcx.datasource.*;

/**
 * The dbMetaClient6 class tests methods of DatabaseMetaData interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class dbMetaClient6 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.dbMeta.dbMeta6";

  // Naming specific member variables
  private TSNamingContextInterface jc = null;

  // Harness requirements

  private transient Connection conn = null;

  private transient DatabaseMetaData dbmd = null;

  private DataSource ds1 = null;

  private dbSchema dbSch = null;

  private String dbName = null, dbUser = null, drManager = null;

  private String sCatalogName = null, sSchemaName = null, sPtable = null,
      sFtable = null;

  private JDBCTestMsg msg = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    dbMetaClient6 theTests = new dbMetaClient6();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup: */
  /*
   * @class.setup_props: Driver, the Driver name; db1, the database name with
   * url; user1, the database user name; password1, the database password; db2,
   * the database name with url; user2, the database user name; password2, the
   * database password; DriverManager, flag for DriverManager; ptable, the
   * primary table; ftable, the foreign table; cofSize, the initial size of the
   * ptable; cofTypeSize, the initial size of the ftable; binarySize, size of
   * binary data type; varbinarySize, size of varbinary data type;
   * longvarbinarySize, size of longvarbinary data type;
   * 
   * @class.testArgs: -ap tssql.stmt
   */

  public void setup(String[] args, Properties p) throws Fault {
    try {
      try {
        drManager = p.getProperty("DriverManager", "");
        dbName = p.getProperty("db1", "");
        dbUser = p.getProperty("user1", "");
        sPtable = p.getProperty("ptable", "TSTABLE1");
        sFtable = p.getProperty("ftable", "TSTABLE2");
        if (dbName.length() == 0)
          throw new Fault("Invalid db1  Database Name");
        if (dbUser.length() == 0)
          throw new Fault("Invalid Login Id");
        if (sPtable.length() == 0)
          throw new Fault("Invalid Primary table");
        if (sFtable.length() == 0)
          throw new Fault("Invalid Foreign table");
        if (drManager.length() == 0)
          throw new Fault("Invalid DriverManager Name");

        int nLocdbname = dbName.indexOf('=');
        sCatalogName = dbName.substring(nLocdbname + 1);
        sCatalogName = sCatalogName.trim();
        sSchemaName = dbUser;

        if (drManager.equals("yes")) {
          logTrace("Using DriverManager");
          DriverManagerConnection dmCon = new DriverManagerConnection();
          conn = dmCon.getConnection(p);
        } else {
          logTrace("Using DataSource");
          DataSourceConnection dsCon = new DataSourceConnection();
          conn = dsCon.getConnection(p);
        }
        dbSch = new dbSchema();
        dbSch.createData(p, conn);
        dbmd = conn.getMetaData();
        msg = new JDBCTestMsg();

      } catch (SQLException ex) {
        logErr("SQL Exception : " + ex.getMessage(), ex);
      }
    } catch (Exception e) {
      logErr("Setup Failed!");
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * @testName: testGetCatalogSeparator
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:934; JDBC:JAVADOC:935;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase. Make a call to DatabaseMetadata.getCatalogSeparator() on that
   * object. It should return a String and NULL if it is not supported.
   *
   */
  public void testGetCatalogSeparator() throws Fault {
    try {
      // invoke getCatalogSeparator method
      msg.setMsg("Calling DatabaseMetaData.getCatalogSeparator");
      String sRetValue = dbmd.getCatalogSeparator();
      if (sRetValue == null)
        msg.setMsg("getCatalogSeparator is not supported");
      else
        msg.setMsg("getCatalogSeparator returns " + sRetValue);

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getCatalogSeparator is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getCatalogSeparator is Failed!");

    }
  }

  /*
   * @testName: testSupportsSchemasInDataManipulation
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:936; JDBC:JAVADOC:937;
   * JavaEE:SPEC:193;
   * 
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase. Make a call to
   * DatabaseMetadata.supportsSchemasInDataManipulation() on that object. It
   * should return a boolean value either true or false.
   *
   */
  public void testSupportsSchemasInDataManipulation() throws Fault {
    try {
      // invoke supportsSchemasInDataManipulation method
      msg.setMsg("Calling DatabaseMetaData.supportsSchemasInDataManipulation");
      boolean retValue = dbmd.supportsSchemasInDataManipulation();
      if (retValue)
        msg.setMsg("supportsSchemasInDataManipulation is supported");
      else
        msg.setMsg("supportsSchemasInDataManipulation is not supported");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle,
          "Call to supportsSchemasInDataManipulation is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsSchemasInDataManipulation is Failed!");

    }
  }

  /*
   * @testName: testSupportsSchemasInProcedureCalls
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:938; JDBC:JAVADOC:939;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase. Make a call to DatabaseMetadata.supportsSchemasInProcedureCalls()
   * on that object. It should return a boolean value; either true or false
   *
   */
  public void testSupportsSchemasInProcedureCalls() throws Fault {
    try {
      // invoke supportsSchemasInProcedureCalls method
      msg.setMsg("Calling DatabaseMetaData.supportsSchemasInProcedureCalls");
      boolean retValue = dbmd.supportsSchemasInProcedureCalls();
      if (retValue)
        msg.setMsg("supportsSchemasInProcedureCalls is supported");
      else
        msg.setMsg("supportsSchemasInProcedureCalls is not supported");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle,
          "Call to supportsSchemasInProcedureCalls is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsSchemasInProcedureCalls is Failed!");

    }
  }

  /*
   * @testName: testSupportsSchemasInTableDefinitions
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:940; JDBC:JAVADOC:941;
   * JavaEE:SPEC:193;
   * 
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase. Make a call to
   * DatabaseMetadata.supportsSchemasInTableDefinitions() on that object.It
   * should return a boolean value; either true or false.
   *
   */
  public void testSupportsSchemasInTableDefinitions() throws Fault {
    try {
      // invoke supportsSchemasInTableDefinitions method
      msg.setMsg("Calling DatabaseMetaData.supportsSchemasInTableDefinitions");
      boolean retValue = dbmd.supportsSchemasInTableDefinitions();
      if (retValue)
        msg.setMsg("supportsSchemasInTableDefinitions is supported");
      else
        msg.setMsg("supportsSchemasInTableDefinitions is not supported");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle,
          "Call to supportsSchemasInTableDefinitions is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsSchemasInTableDefinitions is Failed!");

    }
  }

  /*
   * @testName: testSupportsSchemasInIndexDefinitions
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:942; JDBC:JAVADOC:943;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase. Call to supportsSchemasInIndexDefinitions() on that object. It
   * should return a boolean value; either true or false.
   *
   */
  public void testSupportsSchemasInIndexDefinitions() throws Fault {
    try {
      // invoke supportsSchemasInIndexDefinitions method
      msg.setMsg("Calling DatabaseMetaData.supportsSchemasInIndexDefinitions");
      boolean retValue = dbmd.supportsSchemasInIndexDefinitions();
      if (retValue)
        msg.setMsg("supportsSchemasInIndexDefinitions is supported");
      else
        msg.setMsg("supportsSchemasInIndexDefinitions is not supported");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle,
          "Call to supportsSchemasInIndexDefinitions is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsSchemasInIndexDefinitions is Failed!");

    }
  }

  /*
   * @testName: testSupportsSchemasInPrivilegeDefinitions
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:944; JDBC:JAVADOC:945;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase. Call to supportsSchemasInPrivilegeDefinitions() on that object.
   * It should return a boolean value; either true or false.
   *
   */
  public void testSupportsSchemasInPrivilegeDefinitions() throws Fault {
    try {
      // invoke supportsSchemasInPrivilegeDefinitions method
      msg.setMsg(
          "Calling DatabaseMetaData.supportsSchemasInPrivilegeDefinitions");
      boolean retValue = dbmd.supportsSchemasInPrivilegeDefinitions();
      if (retValue)
        msg.setMsg("supportsSchemasInPrivilegeDefinitions is supported");
      else
        msg.setMsg("supportsSchemasInPrivilegeDefinitions is not supported");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle,
          "Call to supportsSchemasInPrivilegeDefinitions is Failed!");

    } catch (Exception e) {
      msg.printError(e,
          "Call to supportsSchemasInPrivilegeDefinitions is Failed!");

    }
  }

  /*
   * @testName: testSupportsCatalogsInDataManipulation
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:946; JDBC:JAVADOC:947;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase. Call to supportsCatalogsInDataManipulation()on that object. It
   * should return a boolean value; either true or false.
   *
   */
  public void testSupportsCatalogsInDataManipulation() throws Fault {
    try {
      // invoke supportsCatalogsInDataManipulation method
      msg.setMsg("Calling DatabaseMetaData.supportsCatalogsInDataManipulation");
      boolean retValue = dbmd.supportsCatalogsInDataManipulation();
      if (retValue)
        msg.setMsg("supportsCatalogsInDataManipulation is supported");
      else
        msg.setMsg("supportsCatalogsInDataManipulation is not supported");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle,
          "Call to supportsCatalogsInDataManipulation is Failed!");

    } catch (Exception e) {
      msg.printError(e,
          "Call to supportsCatalogsInDataManipulation is Failed!");

    }
  }

  /*
   * @testName: testSupportsCatalogsInProcedureCalls
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:948; JDBC:JAVADOC:949;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase. Call to supportsCatalogsInProcedureCalls() on that object. It
   * should return a boolean value; either true or false.
   *
   */
  public void testSupportsCatalogsInProcedureCalls() throws Fault {
    try {
      // invoke supportsCatalogsInProcedureCalls method
      msg.setMsg("Calling DatabaseMetaData.supportsCatalogsInProcedureCalls");
      boolean retValue = dbmd.supportsCatalogsInProcedureCalls();
      if (retValue)
        msg.setMsg("supportsCatalogsInProcedureCalls is supported");
      else
        msg.setMsg("supportsCatalogsInProcedureCalls is not supported");
    } catch (SQLException sqle) {
      msg.printSQLError(sqle,
          "Call to supportsCatalogsInProcedureCalls is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsCatalogsInProcedureCalls is Failed!");

    }
  }

  /*
   * @testName: testSupportsCatalogsInTableDefinitions
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:950; JDBC:JAVADOC:951;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database Call the supportsCatalogsInTableDefinitions() method on that
   * object. It should return a boolean value; either true or false.
   *
   */
  public void testSupportsCatalogsInTableDefinitions() throws Fault {
    try {
      msg.setMsg(
          "Calling DatabaseMetaData.supportsCatalogsInTableDefinitions()");
      // Invoke supportsCatalogsInTableDefinitions method
      boolean retValue = dbmd.supportsCatalogsInTableDefinitions();
      if (retValue)
        msg.setMsg("supportsCatalogsInTableDefinitions is supported");
      else
        msg.setMsg("supportsCatalogsInTableDefinitions is not supported");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle,
          "Call to supportsCatalogsInTableDefinitions is Failed!");

    } catch (Exception e) {
      msg.printError(e,
          "Call to supportsCatalogsInTableDefinitions is Failed!");

    }
  }

  /*
   * @testName: testSupportsCatalogsInIndexDefinitions
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:952; JDBC:JAVADOC:953;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database Call the supportsCatalogsInIndexDefinitions() method on that
   * object. It should return a boolean value; either true or false.
   *
   */
  public void testSupportsCatalogsInIndexDefinitions() throws Fault {
    try {
      msg.setMsg("Calling DatabaseMetaData.supportsCatalogsInIndexDefinitions");
      // invoke supportsCatalogsInIndexDefinitions method
      boolean retValue = dbmd.supportsCatalogsInIndexDefinitions();
      if (retValue)
        msg.setMsg("supportsCatalogsInIndexDefinitions is supported");
      else
        msg.setMsg("supportsCatalogsInIndexDefinitions is not supported");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle,
          "Call to supportsCatalogsInIndexDefinitions is Failed!");

    } catch (Exception e) {
      msg.printError(e,
          "Call to supportsCatalogsInIndexDefinitions is Failed!");

    }
  }

  /*
   * @testName: testSupportsCatalogsInPrivilegeDefinitions
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:954; JDBC:JAVADOC:955;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the supportsCatalogsInPrivilegeDefinitions() method on
   * that object. It should return a boolean value; either true or false
   *
   */
  public void testSupportsCatalogsInPrivilegeDefinitions() throws Fault {
    try {
      msg.setMsg(
          "Calling DatabaseMetaData.supportsCatalogsInPrivilegeDefinitions");
      // invoke supportsCatalogsInPrivilegeDefinitions method
      boolean retValue = dbmd.supportsCatalogsInPrivilegeDefinitions();
      if (retValue)
        msg.setMsg("supportsCatalogsInPrivilegeDefinitions is supported");
      else
        msg.setMsg("supportsCatalogsInPrivilegeDefinitions is not supported");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle,
          "Call to supportsCatalogInPrivilegeDefinitions is Failed!");

    } catch (Exception e) {
      msg.printError(e,
          "Call to supportsCatalogInPrivilegeDefinitions is Failed!");

    }
  }

  /*
   * @testName: testSupportsPositionedDelete
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:956; JDBC:JAVADOC:957;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the supportsPositionedDelete() method on that object. It
   * should return a boolean value; either true or false.
   *
   */
  public void testSupportsPositionedDelete() throws Fault {
    try {
      msg.setMsg("Calling DatabaseMetaData.supportsPositionedDelete");
      // invoke supportsPositionedDelete method
      boolean retValue = dbmd.supportsPositionedDelete();
      if (retValue)
        msg.setMsg("supportsPositionedDelete is supported");
      else
        msg.setMsg("supportsPositionedDelete is not supported");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsPositionedDelete is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsPositionedDelete is Failed!");

    }
  }

  /*
   * @testName: testSupportsPositionedUpdate
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:958; JDBC:JAVADOC:959;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the supportsPositionedUpdate() method on that object. It
   * should return a boolean value; either true or false.
   *
   */
  public void testSupportsPositionedUpdate() throws Fault {
    try {
      msg.setMsg("Calling DatabaseMetaData.supportsPositionedUpdate");
      // invoke supportsPositionedUpdate method
      boolean retValue = dbmd.supportsPositionedUpdate();
      if (retValue)
        msg.setMsg("supportsPositionedUpdate is supported");
      else
        msg.setMsg("supportsPositionedUpdate is not supported");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsPositionedUpdate is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsPositionedUpdate is Failed!");

    }
  }

  /*
   * @testName: testSupportsSelectForUpdate
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:960; JDBC:JAVADOC:961;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the supportsSelectForUpdate() method on that object. It
   * should return a boolean value; either true or false.
   *
   */
  public void testSupportsSelectForUpdate() throws Fault {
    try {
      msg.setMsg("Calling DatabaseMetaData.supportsSelectForUpdate");
      // invoke supportsSelectForUpdate method
      boolean retValue = dbmd.supportsSelectForUpdate();
      if (retValue)
        msg.setMsg("supportsSelectForUpdate is supported");
      else
        msg.setMsg("supportsSelectForUpdate is not supported");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsSelectForUpdate is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsSelectForUpdate is Failed!");

    }
  }

  /*
   * @testName: testSupportsSubqueriesInComparisons
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:964; JDBC:JAVADOC:965;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the supportsSubqueriesInComparisons() method on that
   * object. It should return a boolean value; either true or false.
   *
   */
  public void testSupportsSubqueriesInComparisons() throws Fault {
    try {
      msg.setMsg("Calling DatabaseMetaData.supportsSubqueriesInComparisons");
      // invoke supportsSubqueriesInComparisons method
      boolean retValue = dbmd.supportsSubqueriesInComparisons();
      if (retValue)
        msg.setMsg("supportsSubqueriesInComparisons is supported");
      else
        msg.setMsg("supportsSubqueriesInComparisons is not supported");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle,
          "Call to supportsSubqueriesInComparisons is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsSubqueriesInComparisons is Failed!");

    }
  }

  /*
   * @testName: testSupportsSubqueriesInExists
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:966; JDBC:JAVADOC:967;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the supportsSubqueriesInExists() method on that object.
   * It should return a boolean value; either true or false.
   *
   */
  public void testSupportsSubqueriesInExists() throws Fault {
    try {
      msg.setMsg("Calling DatabaseMetaData.supportsSubqueriesInExists");
      // invoke supportsSubqueriesInExists method
      boolean retValue = dbmd.supportsSubqueriesInExists();
      if (retValue)
        msg.setMsg("supportsSubqueriesInExists is supported");
      else
        msg.setMsg("supportsSubqueriesInExists is not supported");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsSubqueriesInExists is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsSubqueriesInExists is Failed!");

    }
  }

  /*
   * @testName: testSupportsSubqueriesInIns
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:968; JDBC:JAVADOC:969;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the supportsSubqueriesInIns() method on that object. It
   * should return a boolean value either true or false.
   *
   */
  public void testSupportsSubqueriesInIns() throws Fault {
    try {
      msg.setMsg("Calling DatabaseMetaData.supportsSubqueriesInIns");
      // invoke supportsSubqueriesInIns method
      boolean retValue = dbmd.supportsSubqueriesInIns();
      if (retValue)
        msg.setMsg("supportsSubqueriesInIns is supported");
      else
        msg.setMsg("supportsSubqueriesInIns is not supported");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsSubqueriesInIns is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsSubqueriesInIns is Failed!");

    }
  }

  /*
   * @testName: testSupportsSubqueriesInQuantifieds
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:970; JDBC:JAVADOC:971;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the supportsSubqueriesInQuantifieds() method on that
   * object. It should return a boolean value either true or false.
   *
   */
  public void testSupportsSubqueriesInQuantifieds() throws Fault {
    try {
      msg.setMsg("Calling DatabaseMetaData.supportsSubqueriesInQuantifieds");
      // invoke supportsSubqueriesInQuantifieds method
      boolean retValue = dbmd.supportsSubqueriesInQuantifieds();
      if (retValue)
        msg.setMsg("supportsSubqueriesInQuantifieds is supported");
      else
        msg.setMsg("supportsSubqueriesInQuantifieds is not supported");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle,
          "Call to supportsSubqueriesInQuantifieds is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsSubqueriesInQuantifieds is Failed!");

    }
  }

  /*
   * @testName: testSupportsCorrelatedSubqueries
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:972; JDBC:JAVADOC:973;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the supportsCorrelatedSubqueries() method on that object.
   * It should return a boolean value; either true or false.
   *
   */
  public void testSupportsCorrelatedSubqueries() throws Fault {
    try {
      msg.setMsg("Calling DatabaseMetaData.supportsCorrelatedSubqueries");
      // invoke supportsCorrelatedSubqueries method
      boolean retValue = dbmd.supportsCorrelatedSubqueries();
      if (retValue)
        msg.setMsg("supportsCorrelatedSubqueries is supported");
      else
        msg.setMsg("supportsCorrelatedSubqueries is not supported");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle,
          "Call to supportsCorrelatedSubqueries is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsCorrelatedSubqueries is Failed!");

    }
  }

  /*
   * @testName: testSupportsUnion
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:974; JDBC:JAVADOC:975;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the supportsUnion() method on that object. It should
   * return a boolean value; either true or false.
   *
   */
  public void testSupportsUnion() throws Fault {
    try {
      msg.setMsg("Calling DatabaseMetaData.supportsUnion");
      // invoke supportsUnion method
      boolean retValue = dbmd.supportsUnion();
      if (retValue)
        msg.setMsg("supportsUnion is supported");
      else
        msg.setMsg("supportsUnion is not supported");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsUnion is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsUnion is Failed!");

    }
  }

  /* cleanup */
  public void cleanup() throws Fault {
    try {
      // Close the database
      dbSch.destroyData(conn);
      dbSch.dbUnConnect(conn);
      logMsg("Cleanup ok;");
    } catch (Exception e) {
      logErr("An error occurred while closing the database connection", e);
    }
  }
}
