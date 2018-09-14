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
 * @(#)dbMetaClient1.java	1.27 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.dbMeta.dbMeta1;

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
// import com.merant.sequelink.jdbcx.datasource.*;

/**
 * The dbMetaClient1 class tests methods of DatabaseMetaData interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class dbMetaClient1 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.dbMeta.dbMeta1";

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
    dbMetaClient1 theTests = new dbMetaClient1();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

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
        if (drManager.length() == 0)
          throw new Fault("Invalid DriverManager Name");
        if (dbName.length() == 0)
          throw new Fault("Invalid db1  Database Name");
        if (dbUser.length() == 0)
          throw new Fault("Invalid Login Id");
        if (sPtable.length() == 0)
          throw new Fault("Invalid Primary table");
        if (sFtable.length() == 0)
          throw new Fault("Invalid Foreign table");

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
   * @testName: testSupportsStoredProcedures
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:962; JDBC:JAVADOC:963;
   * JavaEE:SPEC:193; JavaEE:SPEC:180;
   * 
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsStoredprocedures() method It should return
   * true value
   *
   */
  public void testSupportsStoredProcedures() throws Fault {
    try {
      // invoke on the supportsStoredProcedures
      msg.setMsg("Calling supportsStoredProcedures on DatabaseMetaData");
      boolean retValue = dbmd.supportsStoredProcedures();
      if (retValue) {
        msg.setMsg("SupportsStoredProcedures is supported");
      } else {
        msg.printTestError("SupportsStoredProcedures is not supported",
            "supportsStoredProcedures should always return true!");

      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsStoredProcedures is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsStoredProcedures is Failed!");

    }
  }

  /*
   * @testName: testAllProceduresAreCallable
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:808; JDBC:JAVADOC:809;
   * JavaEE:SPEC:193;
   * 
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the allProceduresAreCallable() method It should return a
   * boolean value
   *
   */
  public void testAllProceduresAreCallable() throws Fault {
    try {
      // invoke on the allProceduresAreCallable
      msg.setMsg("Calling allProceduresAreCallable on DatabaseMetaData");
      boolean retValue = dbmd.allProceduresAreCallable();
      if (retValue) {
        msg.setMsg(
            "allProceduresAreCallable method called by the current user");
      } else {
        msg.setMsg(
            "allProceduresAreCallable method not called by the current user");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to allProceduresAreCallable is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to allProceduresAreCallable is Failed!");

    }
  }

  /*
   * @testName: testAllTablesAreSelectable
   * 
   * @assertion_ids: JDBC:SPEC:8 ; JDBC:JAVADOC:810 ; JDBC:JAVADOC:811;
   * JavaEE:SPEC:193;
   * 
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the allTablesAreSelectable() method It should return a
   * boolean value
   *
   */
  public void testAllTablesAreSelectable() throws Fault {
    try {
      // invoke on the allTablesAreSelectable
      msg.setMsg("Calling allTablesAreSelectable on DatabaseMetaData");
      boolean retValue = dbmd.allTablesAreSelectable();
      if (retValue) {
        msg.setMsg(
            "allTablesAreSelectable method SELECTed by the current user");
      } else {
        msg.setMsg(
            "allTablesAreSelectable method not SELECTed by the current user");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to allTablesAreSelectable is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to allTablesAreSelectable is Failed!");

    }
  }

  /*
   * @testName: testGetURL
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:812; JDBC:JAVADOC:813;
   * JavaEE:SPEC:193;
   * 
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the getURL() method It should return a String or null if
   * it cannot be generated
   *
   */
  public void testGetURL() throws Fault {
    try {
      // invoke on the getURL
      msg.setMsg("Calling getURL on DatabaseMetaData");
      String sRetValue = dbmd.getURL();
      logTrace("getURL returns null if it cannot be generated");
      if (sRetValue == null) {
        msg.setMsg("getURL method return a null value ");
      } else {
        msg.setMsg("getURL method returns:  " + sRetValue);
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getURL is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getURL is Failed!");

    }
  }

  /*
   * @testName: testGetUserName
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:814; JDBC:JAVADOC:815;
   * JavaEE:SPEC:193;
   * 
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the getUserName() method It should return a String
   *
   */
  public void testGetUserName() throws Fault {
    try {
      // invoke on the getUserName
      msg.setMsg("Calling getUserName on DatabaseMetaData");
      String sRetValue = dbmd.getUserName();
      if (sRetValue == null) {
        msg.setMsg("getUserName method does not returns user name ");
      } else {
        msg.setMsg("getUserName method returns: " + sRetValue);
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getUserName is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getUserName is Failed!");

    }
  }

  /*
   * @testName: testIsReadOnly
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:816; JDBC:JAVADOC:817;
   * JavaEE:SPEC:193;
   * 
   * 
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the isReadOnly() method It should return a boolean value
   *
   */
  public void testIsReadOnly() throws Fault {
    try {
      // invoke on the isReadOnly
      msg.setMsg("Calling isReadOnly on DatabaseMetaData");
      boolean retValue = dbmd.isReadOnly();
      if (retValue) {
        msg.setMsg("IsReadOnly method is in read-only mode");
      } else {
        msg.setMsg("IsReadOnly method is not in read-only mode");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to isReadOnly is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to isReadOnly is Failed!");

    }
  }

  /*
   * @testName: testNullsAreSortedHigh
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:818; JDBC:JAVADOC:819;
   * JavaEE:SPEC:193;
   * 
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the nullsAreSortedHigh() method It should return a
   * boolean value
   *
   */
  public void testNullsAreSortedHigh() throws Fault {
    try {
      // invoke on the nullsAreSortedHigh
      msg.setMsg("Calling nullsAreSortedHigh on DatabaseMetaData");
      boolean retValue = dbmd.nullsAreSortedHigh();
      if (retValue) {
        msg.setMsg(
            "nullsAreSortedHigh method returns NULL values sorted  high");
      } else {
        msg.setMsg(
            "nullsAreSortedHigh method returns NULL values not sorted high");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to nullsAreSortedHigh is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to nullsAreSortedHigh is Failed!");

    }
  }

  /*
   * @testName: testNullsAreSortedLow
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:820; JDBC:JAVADOC:821;
   * JavaEE:SPEC:193;
   * 
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the nullsAreSortedLow() method It should return a boolean
   * value
   *
   */
  public void testNullsAreSortedLow() throws Fault {
    try {
      // invoke on the nullsAreSortedLow
      msg.setMsg("Calling nullsAreSortedLow on DatabaseMetaData");
      boolean retValue = dbmd.nullsAreSortedLow();
      if (retValue) {
        msg.setMsg("nullsAreSortedLow method returns NULL values sorted low");
      } else {
        msg.setMsg(
            "nullsAreSortedLow method returns NULL values not sorted low");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to nullsAreSortedLow is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to nullsAreSortedLow is Failed!");

    }
  }

  /*
   * @testName: testNullsAreSortedAtStart
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:822; JDBC:JAVADOC:823;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the nullsAreSortedAtStart() method It should return a
   * boolean value
   *
   */
  public void testNullsAreSortedAtStart() throws Fault {
    try {
      // invoke on the nullsAreSortedAtStart
      msg.setMsg("Calling nullsAreSortedAtStart on DatabaseMetaData");
      boolean retValue = dbmd.nullsAreSortedAtStart();
      if (retValue) {
        msg.setMsg(
            "nullsAreSortedAtStart method returns NULL values sorted at the start");
      } else {
        msg.setMsg(
            "nullsAreSortedAtStart method returns NULL values not sorted at the start");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to nullsAreSortedAtStart is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to nullsAreSortedAtStart is Failed!");

    }
  }

  /*
   * @testName: testNullsAreSortedAtEnd
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:824; JDBC:JAVADOC:825;
   * JavaEE:SPEC:193;
   * 
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the nullsAreSortedAtEnd() method It should return a
   * boolean value
   *
   */
  public void testNullsAreSortedAtEnd() throws Fault {
    try {
      // invoke on the nullsAreSortedAtStart
      msg.setMsg("Calling NullsAreSortedAtEnd on DatabaseMetaData");
      boolean retValue = dbmd.nullsAreSortedAtEnd();
      if (retValue) {
        msg.setMsg(
            "nullsAreSortedAtEnd method returns NULL values sorted at the end");
      } else {
        msg.setMsg(
            "nullsAreSortedAtEnd method returns NULL values not sorted at the end");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to nullsAreSortedAtEnd is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to nullsAreSortedAtEnd is Failed!");

    }
  }

  /*
   * @testName: testGetDatabaseProductName
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:826; JDBC:JAVADOC:827;
   * JavaEE:SPEC:193;
   * 
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the getDatabaseProductName() method It should return a
   * String
   *
   */
  public void testGetDatabaseProductName() throws Fault {
    try {
      // invoke on the getDatabaseProductName
      msg.setMsg("Calling getDatabaseProductName on DatabaseMetaData");
      String sRetValue = dbmd.getDatabaseProductName();
      if (sRetValue == null) {
        msg.setMsg(
            "getDatabaseProductName method does not returns database product name ");
      } else {
        msg.setMsg("getDatabaseProductName method returns:  " + sRetValue);
      }
      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getDatabaseProductName is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getDatabaseProductName is Failed!");

    }
  }

  /*
   * @testName: testGetDatabaseProductVersion
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:828; JDBC:JAVADOC:829;
   * JavaEE:SPEC:193;
   * 
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the getDatabaseProductVersion() method It should return a
   * String
   *
   */
  public void testGetDatabaseProductVersion() throws Fault {
    try {
      // invoke on the getDatabaseProductVersion
      msg.setMsg("Calling getDatabaseProductVersion on DatabaseMetaData");
      String sRetValue = dbmd.getDatabaseProductVersion();
      if (sRetValue == null) {
        msg.setMsg(
            "getDatabaseProductVersion  method does not returns a database product version ");
      } else {
        msg.setMsg("getDatabaseProductVersion method returns:  " + sRetValue);
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getDatabaseProductVersion is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getDatabaseProductVersion is Failed!");

    }
  }

  /*
   * @testName: testGetDriverName
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:830; JDBC:JAVADOC:831;
   * JavaEE:SPEC:193;
   * 
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the getDriverName() method It should return a String
   *
   */
  public void testGetDriverName() throws Fault {
    try {
      // invoke on the getDriverName
      msg.setMsg("Calling getDriverName on DatabaseMetaData");
      String sRetValue = dbmd.getDriverName();
      if (sRetValue == null) {
        msg.setMsg("getDriverName method does not returns a JDBC Driver Name ");
      } else {
        msg.setMsg("getDriverName method returns:  " + sRetValue);
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getDriverName is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getDriverName is Failed!");

    }
  }

  /*
   * @testName: testGetDriverVersion
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:832; JDBC:JAVADOC:833;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the getDriverVersion() method It should return a String
   *
   */
  public void testGetDriverVersion() throws Fault {
    try {
      // invoke on the getDriverVersion
      msg.setMsg("Calling getDriverVersion on DatabaseMetaData");
      String sRetValue = dbmd.getDriverVersion();
      if (sRetValue == null) {
        msg.setMsg(
            "getDriverVersion method does not returns a Driver Version ");
      } else {
        msg.setMsg("getDriverVersion returns:  " + sRetValue);
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getDriverVersion is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getDriverVersion is Failed!");

    }
  }

  /*
   * @testName: testGetDriverMajorVersion
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:834; JavaEE:SPEC:193;
   * 
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the getDriverMajorVersion() method It should return a
   * Integer value
   *
   */
  public void testGetDriverMajorVersion() throws Fault {
    try {
      // invoke on the getDriverMajorVersion
      msg.setMsg("Calling getDriverMajorVersion on DatabaseMetaData");
      int drMajorVersion = dbmd.getDriverMajorVersion();
      if (drMajorVersion >= 0) {
        msg.setMsg("getDriverMajorVersion method returns: " + drMajorVersion);
      } else {
        msg.setMsg(" getDriverMajorVersion method returns a negative value");
      }
      msg.printTestMsg();
    } catch (Exception e) {
      msg.printError(e, "Call to getDriverMajorVersion is Failed!");

    }
  }

  /*
   * @testName: testGetDriverMinorVersion
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:835; JavaEE:SPEC:193;
   * 
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the getDriverMinorVersion() method It should return a
   * Integer value
   *
   */
  public void testGetDriverMinorVersion() throws Fault {
    try {
      // invoke on the getDriverMinorVersion
      msg.setMsg("Calling getDriverMinorVersion on DatabaseMetaData");
      int drMinorVersion = dbmd.getDriverMinorVersion();
      if (drMinorVersion >= 0) {
        msg.setMsg(" getDriverMinorVersion method returns: " + drMinorVersion);
      } else {
        msg.setMsg(" getDriverMinorVersion method returns a negative value: "
            + drMinorVersion);
      }
      msg.printTestMsg();
    } catch (Exception e) {
      msg.printError(e, "Call to getDriverMinorVersion Failed!");

    }
  }

  /*
   * @testName: testUsesLocalFilePerTable
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:838; JDBC:JAVADOC:839;
   * JavaEE:SPEC:193;
   * 
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the usesLocalFilePerTable() method It should return a
   * boolean value
   *
   */
  public void testUsesLocalFilePerTable() throws Fault {
    try {
      // invoke on the usesLocalFilePerTable
      msg.setMsg("Calling usesLocalFilePerTable on DatabaseMetaData");
      boolean retValue = dbmd.usesLocalFilePerTable();
      if (retValue) {
        msg.setMsg(
            "usesLocalFilePerTable method returns database uses a local file");
      } else {
        msg.setMsg(
            "usesLocalFilePerTable method returns database not uses a local file");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to usesLocalFilePerTable is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to usesLocalFilePerTable is Failed!");

    }
  }

  /*
   * @testName: testSupportsMixedCaseIdentifiers
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:840; JDBC:JAVADOC:841;
   * JavaEE:SPEC:193;
   * 
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsMixedCaseIdentifiers() method It should
   * return a boolean value
   *
   */
  public void testSupportsMixedCaseIdentifiers() throws Fault {
    try {
      // invoke on the supportsMixedCaseIdentifiers
      msg.setMsg("Calling supportsMixedCaseIdentifiers on DatabaseMetaData");
      boolean retValue = dbmd.supportsMixedCaseIdentifiers();
      if (retValue) {
        msg.setMsg("supportsMixedCaseIdentifiers method is supported");
      } else {
        msg.setMsg("supportsMixedCaseIdentifiers method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle,
          "Call to supportsMixedCaseIdentifiers is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsMixedCaseIdentifiers Failed!");

    }
  }

  /*
   * @testName: testStoresUpperCaseIdentifiers
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:842; JDBC:JAVADOC:843;
   * JavaEE:SPEC:193;
   * 
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the storesUpperCaseIdentifiers() method It should return
   * a boolean value
   *
   */
  public void testStoresUpperCaseIdentifiers() throws Fault {
    try {
      // invoke on the storesUpperCaseIdentifiers
      msg.setMsg("Calling storesUpperCaseIdentifiers on DatabaseMetaData");
      boolean retValue = dbmd.storesUpperCaseIdentifiers();
      if (retValue) {
        msg.setMsg(
            "storesUpperCaseIdentifiers method returns unquoted SQL identifiers stored as upper case");
      } else {
        msg.setMsg(
            "storesUpperCaseIdentifiers method returns unquoted SQL identifiers not stored as upper case");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to storesUpperCaseIdentifiers is Failed");

    } catch (Exception e) {
      msg.printError(e, "Call to storesUpperCaseIdentifiers is Failed");

    }
  }

  /*
   * @testName: testStoresLowerCaseIdentifiers
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:844; JDBC:JAVADOC:845;
   * JavaEE:SPEC:193;
   * 
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the storesLowerCaseIdentifiers() method It should return
   * a boolean value
   *
   */
  public void testStoresLowerCaseIdentifiers() throws Fault {
    try {
      // invoke on the storesLowerCaseIdentifiers
      msg.setMsg("Calling storesLowerCaseIdentifiers on DatabaseMetaData");
      boolean retValue = dbmd.storesLowerCaseIdentifiers();
      if (retValue) {
        msg.setMsg(
            "storesLowerCaseIdentifiers method returns unquoted SQL identifiers stored as lower case");
      } else {
        msg.setMsg(
            "storesLowerCaseIdentifiers returns unquoted SQL identifiers not stored as lower case");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to storesLowerCaseIdentifiers is Failed");

    } catch (Exception e) {
      msg.printError(e, "Call to storesLowerCaseIdentifiers is Failed");

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
