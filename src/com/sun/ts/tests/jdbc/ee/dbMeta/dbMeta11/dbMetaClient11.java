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
 * @(#)dbMetaClient11.java	1.30 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.dbMeta.dbMeta11;

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
 * The dbMetaClient11 class tests methods of DatabaseMetaData interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class dbMetaClient11 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.dbMeta.dbMeta11";

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
    dbMetaClient11 theTests = new dbMetaClient11();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * A private method to compare the Column Names & No of Columns Specific to
   * the test
   */
  private boolean columnCompare(String[] sColumnNames, ResultSet rs)
      throws SQLException {

    ResultSetMetaData rsmd = null;
    boolean test_status = false;
    boolean statusColumnCount = true;
    boolean statusColumnMatch = true;

    int iColumnNamesLength = sColumnNames.length;
    rsmd = rs.getMetaData();
    int iCount = rsmd.getColumnCount();
    msg.setMsg("iColumnNamesLength " + iColumnNamesLength);
    msg.setMsg("iCount " + iCount);
    msg.setMsg("Minimum Column Count is:" + iColumnNamesLength);

    // Comparing Column Lengths
    if (iColumnNamesLength <= iCount) {
      iCount = iColumnNamesLength;
      msg.setMsg("Setting iCount to " + iColumnNamesLength);
      statusColumnCount = true;
    } else {
      statusColumnCount = false;
    }

    msg.setMsg("Comparing Column Names...");
    while (iColumnNamesLength > 0) {
      msg.setMsg("sColumnNames[iColumnNamesLength-1] "
          + sColumnNames[iColumnNamesLength - 1] + " and iColumnNamesLength "
          + iColumnNamesLength);
      msg.setMsg("rsmd.getColumnName(iCount) " + rsmd.getColumnName(iCount));
      if (sColumnNames[iColumnNamesLength - 1]
          .equalsIgnoreCase(rsmd.getColumnName(iCount))) {
        statusColumnMatch = true;
      } else {
        statusColumnMatch = false;
        break;
      }
      iCount--;
      iColumnNamesLength--;
    }
    msg.setMsg("statusColumnCount " + statusColumnCount
        + " and statusColumnMatch " + statusColumnMatch);
    if ((statusColumnCount == true) && (statusColumnMatch == true))
      test_status = true;

    return test_status;
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
   * @testName: testSupportsTransactionIsolationLevel2
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1032; JDBC:JAVADOC:1033;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the supportsTransactionIsolationLevel(int isolevel)
   * method on that object with the isolation level TRANSACTION_READ_COMMITTED.
   * It should return a boolean value; either true or false.
   *
   */
  public void testSupportsTransactionIsolationLevel2() throws Fault {
    try {
      msg.setMsg(
          "Calling DatabaseMetaData.supportsTransactionIsolationLevel(TRANSACTION_READ_COMMITTED)");
      // invoke supportsTransactionIsolationLevel method
      boolean retValue = dbmd.supportsTransactionIsolationLevel(
          Connection.TRANSACTION_READ_COMMITTED);
      if (retValue)
        msg.setMsg(
            "supportsTransactionIsolationLevel(TRANSACTION_READ_COMMITTED) is supported");
      else
        msg.setMsg(
            "supportsTransactionIsolationLevel(TRANSACTION_READ_COMMITTED) is not supported");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle,
          "Call to supportsTransactionIsolationLevel2 is Failed!");

    } catch (Exception e) {
      msg.printError(e,
          "Call to supportsTransactionIsolationLevel2 is Failed!");

    }
  }

  /*
   * @testName: testSupportsTransactionIsolationLevel3
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1032; JDBC:JAVADOC:1033;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the supportsTransactionIsolationLevel(int isolevel)
   * method on that object with the isolation level
   * TRANSACTION_READ_UNCOMMITTED. It should return a boolean value; either true
   * or false.
   *
   */
  public void testSupportsTransactionIsolationLevel3() throws Fault {
    try {
      msg.setMsg(
          "Calling DatabaseMetaData.supportsTransactionIsolationLevel(TRANSACTION_READ_UNCOMMITTED)");
      // invoke supportsTransactionIsolationLevel method
      boolean retValue = dbmd.supportsTransactionIsolationLevel(
          Connection.TRANSACTION_READ_UNCOMMITTED);
      if (retValue)
        msg.setMsg(
            "supportsTransactionIsolationLevel(TRANSACTION_READ_UNCOMMITTED) is supported");
      else
        msg.setMsg(
            "supportsTransactionIsolationLevel(TRANSACTION_READ_UNCOMMITTED ) is not supported");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle,
          "Call to supportsTransactionIsolationLevel3 is Failed!");

    } catch (Exception e) {
      msg.printError(e,
          "Call to supportsTransactionIsolationLevel3 is Failed!");

    }
  }

  /*
   * @testName: testSupportsTransactionIsolationLevel4
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1032; JDBC:JAVADOC:1033;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the supportsTransactionIsolationLevel(int isolevel)
   * method on that object with isolation level TRANSACTION_REPEATABLE_READ. It
   * should return a boolean value; either true or false.
   *
   */
  public void testSupportsTransactionIsolationLevel4() throws Fault {
    try {
      msg.setMsg(
          "Calling DatabaseMetaData.supportsTransactionIsolationLevel(TRANSACTION_REPEATABLE_READ)");
      // invoke supportsTransactionIsolationLevel method
      boolean retValue = dbmd.supportsTransactionIsolationLevel(
          Connection.TRANSACTION_REPEATABLE_READ);
      if (retValue)
        msg.setMsg(
            "supportsTransactionIsolationLevel(TRANSACTION_REPEATABLE_READ) is supported");
      else
        msg.setMsg(
            "supportsTransactionIsolationLevel(TRANSACTION_REPEATABLE_READ) is not supported");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle,
          "Call to supportsTransactionIsolationLevel4 is Failed!");

    } catch (Exception e) {
      msg.printError(e,
          "Call to supportsTransactionIsolationLevel4 is Failed!");

    }
  }

  /*
   * @testName: testSupportsTransactionIsolationLevel5
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1032; JDBC:JAVADOC:1033;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the supportsTransactionIsolationLevel(int isolevel)
   * method on that object with isolation level TRANSACTION_SERIALIZABLE. It
   * should return a boolean value; either true or false.
   *
   */
  public void testSupportsTransactionIsolationLevel5() throws Fault {
    try {
      msg.setMsg(
          "Calling DatabaseMetaData.supportsTransactionIsolationLevel(TRANSACTION_SERIALIZABLE)");
      // invoke supportsTransactionIsolationLevel method
      boolean retValue = dbmd.supportsTransactionIsolationLevel(
          Connection.TRANSACTION_SERIALIZABLE);
      if (retValue)
        msg.setMsg(
            "supportsTransactionIsolationLevel(TRANSACTION_SERIALIZABLE) is supported");
      else
        msg.setMsg(
            "supportsTransactionIsolationLevel(TRANSACTION_SERIALIZABLE) is not supported");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle,
          "Call to supportsTransactionIsolationLevel5 is Failed!");

    } catch (Exception e) {
      msg.printError(e,
          "Call to supportsTransactionIsolationLevel5 is Failed!");

    }
  }

  /*
   * @testName: testGetColumnPrivileges
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1056; JDBC:JAVADOC:1057;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getColumnPrivileges() method on that object. It
   * should return a ResultSet object. Validate the column names and column
   * ordering.
   */
  public void testGetColumnPrivileges() throws Fault {
    try {
      String sColumnNames[] = { "TABLE_CAT", "TABLE_SCHEM", "TABLE_NAME",
          "COLUMN_NAME", "GRANTOR", "GRANTEE", "PRIVILEGE", "IS_GRANTABLE" };
      boolean test_status = true;

      msg.setMsg("Calling DatabaseMetaData.getColumnPrivileges");
      // invoke getColumnPrivileges method
      ResultSet oRet_ResultSet = dbmd.getColumnPrivileges(sCatalogName,
          sSchemaName, sFtable, "%");

      msg.setMsg("Call to columnCompare for the exact match of column names");
      test_status = columnCompare(sColumnNames, oRet_ResultSet);

      if (test_status == false) {
        msg.printTestError(
            "Columns return are not same either in order or name",
            "Call to getColumnPrivileges Failed!");

      }

      if (oRet_ResultSet.next())
        msg.setMsg("getColumnPrivileges returned some column names");
      else
        msg.setMsg("getColumnPrivileges did not return any column names");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getColumnPrivileges Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getColumnPrivileges Failed!");

    }
  }

  /*
   * @testName: testGetTablePrivileges
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1058; JDBC:JAVADOC:1059;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getTablePrivileges() method on that object. It should
   * return a ResultSet object. Validate the column names and column ordering.
   */
  public void testGetTablePrivileges() throws Fault {
    try {
      String sColumnNames[] = { "TABLE_CAT", "TABLE_SCHEM", "TABLE_NAME",
          "GRANTOR", "GRANTEE", "PRIVILEGE", "IS_GRANTABLE" };
      boolean test_status = true;

      msg.setMsg("Calling DatabaseMetaData.getTablePrivileges");
      // invoke getTablePrivileges method
      msg.setMsg("sCatalogName " + sCatalogName);
      msg.setMsg("sSchemaName " + sSchemaName);
      ResultSet oRet_ResultSet = dbmd.getTablePrivileges(sCatalogName,
          sSchemaName, sFtable);
      String sRetStr = new String();
      sRetStr = "";

      msg.setMsg("Call to columnCompare for the exact match of column names");
      test_status = columnCompare(sColumnNames, oRet_ResultSet);

      if (test_status == false) {
        msg.printTestError(
            "Columns return are not same either in order or name",
            "Call to getTablePrivileges Failed!");

      }

      // Store all the privileges returned
      while (oRet_ResultSet.next())
        sRetStr += oRet_ResultSet.getString(6) + ",";
      if (sRetStr == "")
        msg.setMsg("getTablePrivileges did not return any privileges");
      else
        msg.setMsg("The privileges returned Are : "
            + sRetStr.substring(0, sRetStr.length() - 1));

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getTablePrivileges Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getTablePrivileges Failed!");

    }
  }

  /*
   * @testName: testGetBestRowIdentifier1
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1060; JDBC:JAVADOC:1061;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getBestRowIdentifier() method on that object. It
   * should return a ResultSet object.
   *
   */
  public void testGetBestRowIdentifier1() throws Fault {
    try {
      msg.setMsg(
          "Calling DatabaseMetaData.getBestRowIdentifier with scope bestRowTemporary with nullable columns");
      // invoke getBestRowIdentifier method
      ResultSet oRet_ResultSet = dbmd.getBestRowIdentifier(sCatalogName,
          sSchemaName, sFtable, DatabaseMetaData.bestRowTemporary, true);
      String sRetStr = new String();
      sRetStr = "";
      msg.setMsg("Store all the Columns returned");
      while (oRet_ResultSet.next())
        sRetStr += oRet_ResultSet.getString(2) + ",";
      if (sRetStr == "")
        msg.setMsg(
            "getBestRowIdentifier with scope bestRowTemporary did not return any columns");
      else
        msg.setMsg("The columns returned Are : "
            + sRetStr.substring(0, sRetStr.length() - 1));

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getBestRowIdentifier1 is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getBestRowIdentifier1 is Failed!");

    }
  }

  /*
   * @testName: testGetBestRowIdentifier2
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1060; JDBC:JAVADOC:1061;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getBestRowIdentifier() method on that object. It
   * should return a ResultSet object.
   *
   */
  public void testGetBestRowIdentifier2() throws Fault {
    try {
      msg.setMsg(
          "Calling DatabaseMetaData.getBestRowIdentifier with scope bestRowTransaction with nullable columns");
      // invoke getBestRowIdentifier method
      ResultSet oRet_ResultSet = dbmd.getBestRowIdentifier(sCatalogName,
          sSchemaName, sFtable, DatabaseMetaData.bestRowTransaction, true);
      String sRetStr = new String();
      sRetStr = "";
      msg.setMsg("Store all the Columns returned");
      while (oRet_ResultSet.next())
        sRetStr += oRet_ResultSet.getString(2) + ",";
      if (sRetStr == "")
        msg.setMsg(
            "getBestRowIdentifier with scope bestRowTransaction did not return any columns");
      else
        msg.setMsg("The columns returned Are : "
            + sRetStr.substring(0, sRetStr.length() - 1));

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getBestRowIdentifier2 is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getBestRowIdentifier2 is Failed!");

    }
  }

  /*
   * @testName: testGetBestRowIdentifier3
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1060; JDBC:JAVADOC:1061;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getBestRowIdentifier() method on that object. It
   * should return a ResultSet object.
   *
   */
  public void testGetBestRowIdentifier3() throws Fault {
    try {
      msg.setMsg(
          "Calling DatabaseMetaData.getBestRowIdentifier with scope bestRowSession with nullable columns");
      // invoke getBestRowIdentifier method
      ResultSet oRet_ResultSet = dbmd.getBestRowIdentifier(sCatalogName,
          sSchemaName, sFtable, DatabaseMetaData.bestRowSession, true);
      String sRetStr = new String();
      sRetStr = "";
      // Store all the Columns returned
      while (oRet_ResultSet.next())
        sRetStr += oRet_ResultSet.getString(2) + ",";
      if (sRetStr == "")
        msg.setMsg(
            "getBestRowIdentifier with scope bestRowSession did not return any columns");
      else
        msg.setMsg("The columns returned Are : "
            + sRetStr.substring(0, sRetStr.length() - 1));

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getBestRowIdentifier3 is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getBestRowIdentifier3 is Failed!");

    }
  }

  /*
   * @testName: testGetBestRowIdentifier4
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1060; JDBC:JAVADOC:1061;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getBestRowIdentifier() method on that object. It
   * should return a ResultSet object.
   *
   */
  public void testGetBestRowIdentifier4() throws Fault {
    try {
      msg.setMsg(
          "Calling DatabaseMetaData.getBestRowIdentifier with scope bestRowTemporary without nullable columns");
      // invoke getBestRowIdentifier method
      ResultSet oRet_ResultSet = dbmd.getBestRowIdentifier(sCatalogName,
          sSchemaName, sFtable, DatabaseMetaData.bestRowTemporary, false);
      String sRetStr = new String();
      sRetStr = "";
      // Store all the Columns returned
      while (oRet_ResultSet.next())
        sRetStr += oRet_ResultSet.getString(2) + ",";
      if (sRetStr == "")
        msg.setMsg(
            "getBestRowIdentifier with scope bestRowTemporary did not return any columns");
      else
        msg.setMsg("The columns returned Are : "
            + sRetStr.substring(0, sRetStr.length() - 1));

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getBestRowIdentifier4 is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getBestRowIdentifier4 is Failed!");

    }
  }

  /*
   * @testName: testGetBestRowIdentifier5
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1060; JDBC:JAVADOC:1061;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getBestRowIdentifier() method on that object. It
   * should return a ResultSet object.
   *
   */
  public void testGetBestRowIdentifier5() throws Fault {
    try {
      msg.setMsg(
          "Calling DatabaseMetaData.getBestRowIdentifier with scope bestRowTransaction without nullable columns");
      // invoke getBestRowIdentifier method
      ResultSet oRet_ResultSet = dbmd.getBestRowIdentifier(sCatalogName,
          sSchemaName, sFtable, DatabaseMetaData.bestRowTransaction, true);
      String sRetStr = new String();
      sRetStr = "";
      // Store all the Columns returned
      while (oRet_ResultSet.next())
        sRetStr += oRet_ResultSet.getString(2) + ",";
      if (sRetStr == "")
        msg.setMsg(
            "getBestRowIdentifier with scope bestRowTransaction did not return any columns");
      else
        msg.setMsg("The columns returned Are : "
            + sRetStr.substring(0, sRetStr.length() - 1));

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getBestRowIdentifier5 is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getBestRowIdentifier5 is Failed!");

    }
  }

  /*
   * @testName: testGetBestRowIdentifier6
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1060; JDBC:JAVADOC:1061;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getBestRowIdentifier() method on that object. It
   * should return a ResultSet object
   *
   */
  public void testGetBestRowIdentifier6() throws Fault {
    try {
      msg.setMsg(
          "Calling DatabaseMetaData.getBestRowIdentifier with scope bestRowSession without nullable columns");
      // invoke getBestRowIdentifier method
      ResultSet oRet_ResultSet = dbmd.getBestRowIdentifier(sCatalogName,
          sSchemaName, sFtable, DatabaseMetaData.bestRowSession, true);
      String sRetStr = new String();
      sRetStr = "";
      // Store all the Columns returned
      while (oRet_ResultSet.next())
        sRetStr += oRet_ResultSet.getString(2) + ",";
      if (sRetStr == "")
        msg.setMsg(
            "getBestRowIdentifier with scope bestRowSession did not return any columns");
      else
        msg.setMsg("The columns returned Are : "
            + sRetStr.substring(0, sRetStr.length() - 1));

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getBestRowIdentifier6 is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getBestRowIdentifier6 is Failed!");

    }
  }

  /*
   * @testName: testGetBestRowIdentifier7
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1060; JDBC:JAVADOC:1061;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getBestRowIdentifier() method on that object. It
   * should return a ResultSet object. Validate the column names and column
   * ordering.
   */
  public void testGetBestRowIdentifier7() throws Fault {
    try {
      String sColumnNames[] = { "SCOPE", "COLUMN_NAME", "DATA_TYPE",
          "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS",
          "PSEUDO_COLUMN" };
      boolean test_status = true;

      msg.setMsg(
          "Calling DatabaseMetaData.getBestRowIdentifier with scope bestRowSession without nullable columns");

      // invoke getBestRowIdentifier method
      ResultSet oRet_ResultSet = dbmd.getBestRowIdentifier(sCatalogName,
          sSchemaName, sFtable, DatabaseMetaData.bestRowSession, true);

      msg.setMsg("Call columnCompare for the exact match of column names");
      test_status = columnCompare(sColumnNames, oRet_ResultSet);
      if (test_status == false) {
        msg.printTestError(
            "Columns return are not same either in order or name",
            "Call to getBestRowIdentifier7 Failed!");

      }

      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getBestRowIdentifier7 Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getBestRowIdentifier7 Failed!");

    }
  }

  /*
   * @testName: testGetVersionColumns
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1062; JDBC:JAVADOC:1063;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getVersionColumns() method on that object. It should
   * return a ResultSet object.Compare the column names Validate the column
   * names and column ordering.
   */
  public void testGetVersionColumns() throws Fault {
    try {
      String sColumnNames[] = { "SCOPE", "COLUMN_NAME", "DATA_TYPE",
          "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS",
          "PSEUDO_COLUMN" };
      boolean test_status = true;

      msg.setMsg("Calling DatabaseMetaData.getVersionColumns");
      // invoke getVersionColumns method
      ResultSet oRet_ResultSet = dbmd.getVersionColumns(sCatalogName,
          sSchemaName, sFtable);
      String sRetStr = new String();
      sRetStr = "";

      msg.setMsg("Call to columnCompare for the exact match of column names");
      test_status = columnCompare(sColumnNames, oRet_ResultSet);
      if (test_status == false) {
        msg.printTestError(
            "Columns return are not same either in order or name",
            "Call to getVersionColumns Failed!");

      }

      msg.setMsg("Store all the columns returned");
      while (oRet_ResultSet.next())
        sRetStr += oRet_ResultSet.getString(2) + ",";
      if (sRetStr == "")
        msg.setMsg("getVersionColumns did not return any columns");
      else
        msg.setMsg("The columns returned Are : "
            + sRetStr.substring(0, sRetStr.length() - 1));

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getVersionColumns Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getVersionColumns Failed!");

    }
  }

  /*
   * @testName: testGetPrimaryKeys
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1064; JDBC:JAVADOC:1065;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getPrimaryKeys() method on that object. It should
   * return a ResultSet object. Validate the column names and column ordering.
   */
  public void testGetPrimaryKeys() throws Fault {
    try {
      String sColumnNames[] = { "TABLE_CAT", "TABLE_SCHEM", "TABLE_NAME",
          "COLUMN_NAME", "KEY_SEQ", "PK_NAME" };
      boolean test_status = true;

      msg.setMsg("Calling DatabaseMetaData.getPrimaryKeys");
      // invoke getPrimaryKeys method
      ResultSet oRet_ResultSet = dbmd.getPrimaryKeys(sCatalogName, sSchemaName,
          sFtable);
      String sRetStr = new String();
      sRetStr = "";

      msg.setMsg("Call to columnCompare for the exact match of column names");
      test_status = columnCompare(sColumnNames, oRet_ResultSet);
      if (test_status == false) {
        msg.printTestError(
            "Columns return are not same either in order or name",
            "Call to getPrimaryKeys Failed!");

      }
      msg.setMsg("Store all the columns returned");
      while (oRet_ResultSet.next())
        sRetStr += oRet_ResultSet.getString(4) + ",";
      if (sRetStr == "")
        msg.setMsg("getPrimaryKeys did not return any columns");
      else
        msg.setMsg("The columns returned Are : "
            + sRetStr.substring(0, sRetStr.length() - 1));

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getPrimaryKeys Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getPrimaryKeys Failed!");

    }
  }

  /*
   * @testName: testGetImportedKeys
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1066; JDBC:JAVADOC:1067;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getImportedKeys() method on that object. It should
   * return a ResultSet object. Validate the column names and column ordering.
   */
  public void testGetImportedKeys() throws Fault {
    try {
      String sColumnNames[] = { "PKTABLE_CAT", "PKTABLE_SCHEM", "PKTABLE_NAME",
          "PKCOLUMN_NAME", "FKTABLE_CAT", "FKTABLE_SCHEM", "FKTABLE_NAME",
          "FKCOLUMN_NAME", "KEY_SEQ", "UPDATE_RULE", "DELETE_RULE", "FK_NAME",
          "PK_NAME", "DEFERRABILITY" };
      boolean test_status = true;

      msg.setMsg("Calling DatabaseMetaData.getImportedKeys");
      // invoke getImportedKeys method
      ResultSet oRet_ResultSet = dbmd.getImportedKeys(sCatalogName, sSchemaName,
          sFtable);
      String sRetStr = new String();
      sRetStr = "";

      msg.setMsg("Call columnCompare for the exact match of column names");
      test_status = columnCompare(sColumnNames, oRet_ResultSet);
      if (test_status == false) {
        msg.printTestError(
            "Columns return are not same either in order or name",
            "Call to getImportedKeys Failed!");

      }

      msg.setMsg("Store all the columns returned");
      while (oRet_ResultSet.next())
        sRetStr += oRet_ResultSet.getString(4) + ",";
      if (sRetStr == "")
        msg.setMsg("getImportedKeys did not return any columns");
      else
        msg.setMsg("The columns returned Are : "
            + sRetStr.substring(0, sRetStr.length() - 1));

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getImportedKeys Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getImportedKeys Failed!");

    }
  }

  /*
   * @testName: testGetExportedKeys
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1068; JDBC:JAVADOC:1069;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getExportedKeys() method on that object. It should
   * return a ResultSet object. Validate the column names and column ordering.
   */
  public void testGetExportedKeys() throws Fault {
    try {
      String sColumnNames[] = { "PKTABLE_CAT", "PKTABLE_SCHEM", "PKTABLE_NAME",
          "PKCOLUMN_NAME", "FKTABLE_CAT", "FKTABLE_SCHEM", "FKTABLE_NAME",
          "FKCOLUMN_NAME", "KEY_SEQ", "UPDATE_RULE", "DELETE_RULE", "FK_NAME",
          "PK_NAME", "DEFERRABILITY" };
      boolean test_status = true;

      msg.setMsg("Calling DatabaseMetaData.getExportedKeys");
      // invoke getExportedKeys method
      ResultSet oRet_ResultSet = dbmd.getExportedKeys(sCatalogName, sSchemaName,
          sPtable);
      String sRetStr = new String();
      sRetStr = "";

      msg.setMsg("Call columnCompare for the exact match of column names");
      test_status = columnCompare(sColumnNames, oRet_ResultSet);
      if (test_status == false) {
        msg.printTestError(
            "Columns return are not same either in order or name",
            "Call to getExportedKeys Failed!");

      }

      msg.setMsg("Store all the columns returned");
      while (oRet_ResultSet.next())
        sRetStr += oRet_ResultSet.getString(8) + ",";
      if (sRetStr == "")
        msg.setMsg("getExportedKeys did not return any columns");
      else
        msg.setMsg("The columns returned Are : "
            + sRetStr.substring(0, sRetStr.length() - 1));

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getExportedKeys is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getExportedKeys is Failed!");

    }
  }

  /*
   * @testName: testGetCrossReference
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1070; JDBC:JAVADOC:1071;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getCrossReference() method on that object. It should
   * return a ResultSet object. Validate the column names and column ordering.
   */
  public void testGetCrossReference() throws Fault {
    try {
      String sColumnNames[] = { "PKTABLE_CAT", "PKTABLE_SCHEM", "PKTABLE_NAME",
          "PKCOLUMN_NAME", "FKTABLE_CAT", "FKTABLE_SCHEM", "FKTABLE_NAME",
          "FKCOLUMN_NAME", "KEY_SEQ", "UPDATE_RULE", "DELETE_RULE", "FK_NAME",
          "PK_NAME", "DEFERRABILITY" };
      boolean test_status = true;

      msg.setMsg("Calling DatabaseMetaData.getCrossReference");
      // invoke getCrossReference method
      ResultSet oRet_ResultSet = dbmd.getCrossReference(sCatalogName,
          sSchemaName, sPtable, sCatalogName, sSchemaName, sFtable);
      String sRetStr = new String();
      sRetStr = "";

      msg.setMsg("Call columnCompare for the exact match of column names");
      test_status = columnCompare(sColumnNames, oRet_ResultSet);
      if (test_status == false) {
        msg.printTestError(
            "Columns return are not same either in order or name",
            "Call to getCrossReference Failed!");

      }

      msg.setMsg("Store all the columns returned");
      while (oRet_ResultSet.next())
        sRetStr += oRet_ResultSet.getString(8) + ",";
      if (sRetStr == "")
        msg.setMsg("getCrossReference did not return any columns");
      else
        msg.setMsg("The columns returned Are : "
            + sRetStr.substring(0, sRetStr.length() - 1));

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getCrossReference Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getCrossReference Failed!");

    }
  }

  /*
   * @testName: testGetIndexInfo1
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1074; JDBC:JAVADOC:1075;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getIndexInfo() method on that object. It should
   * return a ResultSet object.
   *
   */
  public void testGetIndexInfo1() throws Fault {
    try {
      msg.setMsg(
          "Calling DatabaseMetaData.getIndexInfo with approximate data and without accurate results");
      // invoke getIndexInfo method
      ResultSet oRet_ResultSet = dbmd.getIndexInfo(sCatalogName, sSchemaName,
          sFtable, true, true);
      String sRetStr = new String();
      sRetStr = "";
      msg.setMsg("Store all the index name returned");
      while (oRet_ResultSet.next())
        sRetStr += oRet_ResultSet.getString(6) + ",";
      if (sRetStr == "")
        msg.setMsg("getIndexInfo did not return any index names");
      else
        msg.setMsg("The index names returned Are : "
            + sRetStr.substring(0, sRetStr.length() - 1));

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getIndexInfo1 is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getIndexInfo1 is Failed!");

    }
  }

  /*
   * @testName: testGetIndexInfo2
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1074; JDBC:JAVADOC:1075;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getIndexInfo() method on that object. It should
   * return a ResultSet object.
   *
   */
  public void testGetIndexInfo2() throws Fault {
    try {
      msg.setMsg(
          "Calling DatabaseMetaData.getIndexInfo with approximate data and with accurate results");
      // invoke getIndexInfo method
      ResultSet oRet_ResultSet = dbmd.getIndexInfo(sCatalogName, sSchemaName,
          sFtable, true, false);
      String sRetStr = new String();
      sRetStr = "";

      msg.setMsg("Store all the index name returned");
      while (oRet_ResultSet.next())
        sRetStr += oRet_ResultSet.getString(6) + ",";
      if (sRetStr == "")
        msg.setMsg("getIndexInfo did not return any index names");
      else
        msg.setMsg("The index names returned Are : "
            + sRetStr.substring(0, sRetStr.length() - 1));

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getIndexInfo2 is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getIndexInfo2 is Failed!");

    }
  }

  /*
   * @testName: testGetIndexInfo3
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1074; JDBC:JAVADOC:1075;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getIndexInfo() method on that object. It should
   * return a ResultSet object.
   *
   */
  public void testGetIndexInfo3() throws Fault {
    try {
      msg.setMsg(
          "Calling DatabaseMetaData.getIndexInfo without approximate data and with accurate results");
      // invoke getIndexInfo method
      ResultSet oRet_ResultSet = dbmd.getIndexInfo(sCatalogName, sSchemaName,
          sFtable, false, false);
      String sRetStr = new String();
      sRetStr = "";
      msg.setMsg("Store all the index name returned");
      while (oRet_ResultSet.next())
        sRetStr += oRet_ResultSet.getString(6) + ",";
      if (sRetStr == "")
        msg.setMsg("getIndexInfo did not return any index names");
      else
        msg.setMsg("The index names returned Are : "
            + sRetStr.substring(0, sRetStr.length() - 1));

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getIndexInfo3 is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getIndexInfo3 is Failed!");

    }
  }

  /*
   * @testName: testGetIndexInfo4
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1074; JDBC:JAVADOC:1075;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getIndexInfo() method on that object. It should
   * return a ResultSet object.
   *
   */
  public void testGetIndexInfo4() throws Fault {
    try {
      msg.setMsg(
          "Calling DatabaseMetaData.getIndexInfo without approximate data and without accurate results");
      // invoke getIndexInfo method
      ResultSet oRet_ResultSet = dbmd.getIndexInfo(sCatalogName, sSchemaName,
          sFtable, false, true);
      String sRetStr = new String();
      sRetStr = "";
      msg.setMsg("Store all the index name returned");
      while (oRet_ResultSet.next())
        sRetStr += oRet_ResultSet.getString(6) + ",";
      if (sRetStr == "")
        msg.setMsg("getIndexInfo did not return any index names");
      else
        msg.setMsg("The index names returned Are : "
            + sRetStr.substring(0, sRetStr.length() - 1));

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getIndexInfo4 is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getIndexInfo4 is Failed!");

    }
  }

  /*
   * @testName: testGetIndexInfo5
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1074; JDBC:JAVADOC:1075;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getIndexInfo() method on that object. It should
   * return a ResultSet object. Validate the column names and column ordering.
   */
  public void testGetIndexInfo5() throws Fault {
    try {
      String sColumnNames[] = { "TABLE_CAT", "TABLE_SCHEM", "TABLE_NAME",
          "NON_UNIQUE", "INDEX_QUALIFIER", "INDEX_NAME", "TYPE",
          "ORDINAL_POSITION", "COLUMN_NAME", "ASC_OR_DESC", "CARDINALITY",
          "PAGES", "FILTER_CONDITION" };
      boolean test_status = true;

      msg.setMsg(
          "Calling DatabaseMetaData.getIndexInfo without approximate data and without accurate results");
      // invoke getIndexInfo method
      ResultSet oRet_ResultSet = dbmd.getIndexInfo(sCatalogName, sSchemaName,
          sFtable, false, true);

      msg.setMsg("Call to columnCompare for the exact match of column names");
      test_status = columnCompare(sColumnNames, oRet_ResultSet);
      if (test_status == false) {
        msg.printTestError(
            "Columns return are not same either in order or name",
            "Call to getIndexInfo4 Failed!");

      }

      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getIndexInfo4 is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getIndexInfo4 is Failed!");

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
