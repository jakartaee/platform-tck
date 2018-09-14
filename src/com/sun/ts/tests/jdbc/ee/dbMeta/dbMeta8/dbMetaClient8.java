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
 * @(#)dbMetaClient8.java	1.28 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.dbMeta.dbMeta8;

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
 * The dbMetaClient8 class tests methods of DatabaseMetaData interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class dbMetaClient8 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.dbMeta.dbMeta8";

  // Naming specific member variables
  private TSNamingContextInterface jc = null;

  // Harness requirements

  private transient Connection conn = null;

  private transient DatabaseMetaData dbmd = null;

  private DataSource ds1 = null;

  private dbSchema dbSch = null;

  private String dbUser = null, drManager = null;

  private String sCatalogName = null, sSchemaName = null, sPtable = null,
      sFtable = null;

  private JDBCTestMsg msg = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    dbMetaClient8 theTests = new dbMetaClient8();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * A private method to compare the Column Names & No of Columns Specific to
   * the test
   */
  private boolean columnCompare(String[] sColumnNames, ResultSet rs)
      throws SQLException, Fault {

    ResultSetMetaData rsmd = null;
    boolean test_status = false;
    boolean statusColumnCount = true;
    boolean statusColumnMatch = true;

    int iColumnNamesLength = sColumnNames.length;

    rsmd = rs.getMetaData();
    int iCount = rsmd.getColumnCount();
    msg.setMsg("iCount - number of columns in this ResultSet " + iCount);
    msg.setMsg("Minimum Column Count is:" + iColumnNamesLength);

    // Comparing Column Lengths
    if (iColumnNamesLength <= iCount) {
      msg.setMsg("Setting iCount to " + iColumnNamesLength);
      iCount = iColumnNamesLength;
    } else {
      statusColumnCount = false;
    }

    msg.setMsg("Comparing Column Names...");
    while (iColumnNamesLength > 0) {
      msg.setMsg("sColumnNames[iColumnNamesLength-1] "
          + sColumnNames[iColumnNamesLength - 1] + " and iColumnNamesLength "
          + iColumnNamesLength);
      msg.setMsg("rsmd.getColumnName(iCount) " + rsmd.getColumnName(iCount));
      if (!sColumnNames[iColumnNamesLength - 1]
          .equalsIgnoreCase(rsmd.getColumnName(iCount))) {
        statusColumnMatch = false;
        msg.setMsg("*** Error Expected Column name:"
            + sColumnNames[iColumnNamesLength - 1] + ", Received column:"
            + rsmd.getColumnName(iCount));
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
        dbUser = p.getProperty("user1", "");
        sPtable = p.getProperty("ptable", "TSTABLE1");
        sFtable = p.getProperty("ftable", "TSTABLE2");
        if (dbUser.length() == 0)
          throw new Fault("Invalid Login Id");
        if (sPtable.length() == 0)
          throw new Fault("Invalid Primary table");
        if (sFtable.length() == 0)
          throw new Fault("Invalid Foreign table");
        if (drManager.length() == 0)
          throw new Fault("Invalid DriverManager Name");
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
   * @testName: testDoesMaxRowSizeIncludeBlobs
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1016; JDBC:JAVADOC:1017;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the doesMaxRowSizeIncludeBlobs() method. It should return
   * a boolean value
   *
   */
  public void testDoesMaxRowSizeIncludeBlobs() throws Fault {
    try {
      msg.setMsg("Calling  DatabaseMetaData.doesMaxRowSizeIncludeBlobs");
      // invoke doesMaxRowSizeIncludeBlobs method
      boolean retValue = dbmd.doesMaxRowSizeIncludeBlobs();
      if (retValue)
        msg.setMsg("MaxRowSize includes blobs");
      else
        msg.setMsg("MaxRowSize does not include blobs");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to doesMaxRowSizeIncludeBlobs is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to doesMaxRowSizeIncludeBlobs is Failed!");

    }
  }

  /*
   * @testName: testGetMaxStatementLength
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1018; JDBC:JAVADOC:1019;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getMaxStatementLength() method. It should return an
   * integer value
   *
   */
  public void testGetMaxStatementLength() throws Fault {
    try {
      msg.setMsg("Calling DatabaseMetaData.getMaxStatementLength");
      // invoke getMaxStatementLength method
      int nRetval = dbmd.getMaxStatementLength();
      if (nRetval < 0)
        msg.setMsg("getMaxStatementLength returns a negative value");
      else
        msg.setMsg("getMaxStatementLength returns " + nRetval);
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getMaxStatementLength is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getMaxStatementLength is Failed!");

    }
  }

  /*
   * @testName: testGetMaxStatements
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1020; JDBC:JAVADOC:1021;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getMaxStatements() method. It should return an
   * integer value
   *
   */
  public void testGetMaxStatements() throws Fault {
    try {
      msg.setMsg("Calling DatabaseMetaData.getMaxStatements");
      // invoke getMaxStatements method
      int nRetval = dbmd.getMaxStatements();
      if (nRetval < 0)
        msg.setMsg("getMaxStatements returns a negative value");
      else
        msg.setMsg("getMaxStatements returns " + nRetval);

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getMaxStatements is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getMaxStatements is Failed!");

    }
  }

  /*
   * @testName: testGetMaxTableNameLength
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1022; JDBC:JAVADOC:1023;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getMaxTableNameLength() method. It should return an
   * integer value
   *
   */
  public void testGetMaxTableNameLength() throws Fault {
    try {
      msg.setMsg("Calling DatabaseMetaData.getMaxTableNameLength");
      // invoke getMaxTableNameLength method
      int nRetval = dbmd.getMaxTableNameLength();
      if (nRetval < 0)
        msg.setMsg("getMaxTableNameLength returns a negative value");
      else
        msg.setMsg("getMaxTableNameLength returns " + nRetval);

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getMaxTableNameLength is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getMaxTableNameLength is Failed!");

    }
  }

  /*
   * @testName: testGetMaxTablesInSelect
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1024; JDBC:JAVADOC:1025;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getMaxTablesInSelect() method. It should return an
   * integer value
   *
   */
  public void testGetMaxTablesInSelect() throws Fault {
    try {
      msg.setMsg("Calling DatabaseMetaData.getMaxTablesInSelect");
      // invoke getMaxTablesInSelect method
      int nRetval = dbmd.getMaxTablesInSelect();
      if (nRetval < 0)
        msg.setMsg("getMaxTablesInSelect returns a negative value");
      else
        msg.setMsg("getMaxTablesInSelect returns " + nRetval);

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getMaxTablesInSelect is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getMaxTablesInSelect is Failed!");

    }
  }

  /*
   * @testName: testGetMaxUserNameLength
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1026; JDBC:JAVADOC:1027;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getMaxUserNameLength() method. It should return an
   * integer value
   *
   */
  public void testGetMaxUserNameLength() throws Fault {
    try {
      msg.setMsg("Calling DatabaseMetaData.getMaxUserNameLength");
      // invoke getMaxUserNameLength method
      int nRetval = dbmd.getMaxUserNameLength();
      if (nRetval < 0)
        msg.setMsg("getMaxUserNameLength returns a negative value");
      else
        msg.setMsg("getMaxUserNameLength returns " + nRetval);

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getMaxUserNameLength is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getMaxUserNameLength is Failed!");

    }
  }

  /*
   * @testName: testGetDefaultTransactionIsolation
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1028; JDBC:JAVADOC:1029;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getDefaultTransactionIsolation() method. It should
   * return an integer value
   *
   */
  public void testGetDefaultTransactionIsolation() throws Fault {
    try {
      msg.setMsg("Calling DatabaseMetaData.getDefaultTransactionIsolation");
      // invoke getDefaultTransactionIsolation method
      int nRetval = dbmd.getDefaultTransactionIsolation();
      if ((nRetval != conn.TRANSACTION_NONE)
          && (nRetval != conn.TRANSACTION_READ_UNCOMMITTED)
          && (nRetval != conn.TRANSACTION_READ_COMMITTED)
          && (nRetval != conn.TRANSACTION_REPEATABLE_READ)
          && (nRetval != conn.TRANSACTION_SERIALIZABLE)) {
        msg.printTestError(
            "getDefaultTransactionIsolation returns an invalid value",
            "Call to getDefaultTransactionIsolation is Failed!");

      } else
        msg.setMsg(
            "getDefaultTransactionIsolation returns a valid Isolation level"
                + nRetval);

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle,
          "Call to getDefaultTransactionIsolation is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getDefaultTransactionIsolation is Failed!");

    }
  }

  /*
   * @testName: testSupportsTransactions
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1030; JDBC:JAVADOC:1031;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the supportsTransactions() method. It should return a
   * boolean value
   *
   */
  public void testSupportsTransactions() throws Fault {
    try {
      msg.setMsg("Calling DatabaseMetaData.supportsTransactions");
      // invoke supportsTransactions method
      boolean retValue = dbmd.supportsTransactions();
      if (retValue)
        msg.setMsg("supportsTransactions is supported");
      else
        msg.setMsg("supportsTransactions is not supported");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsTransactions is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsTransactions is Failed!");

    }
  }

  /*
   * @testName: testSupportsBatchUpdates
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1098; JDBC:JAVADOC:1099;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the supportsBatchUpdates() method. It should return a
   * boolean value
   *
   */
  public void testSupportsBatchUpdates() throws Fault {
    try {
      msg.setMsg("Calling DatabaseMetaData.supportsBatchUpdates");
      // invoke supportsBatchUpdates method
      boolean retValue = dbmd.supportsBatchUpdates();
      if (retValue)
        msg.setMsg("supportsBatchUpdates is supported");
      else
        msg.setMsg("supportsBatchUpdates is not supported");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsBatchUpdates is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsBatchUpdates is Failed!");

    }
  }

  /*
   * @testName: testSupportsDataDefinitionAndDataManipulationTransactions
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1034; JDBC:JAVADOC:1035;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the
   * supportsDataDefinitionAndDataManipulationTransactions() method. It should
   * return a boolean value
   *
   */
  public void testSupportsDataDefinitionAndDataManipulationTransactions()
      throws Fault {
    try {
      msg.setMsg(
          "Calling DatabaseMetaData.supportsDataDefinitionAndDataManipulationTransactions");
      // invoke supportsDataDefinitionAndDataManipulationTransactions method
      boolean retValue = dbmd
          .supportsDataDefinitionAndDataManipulationTransactions();
      if (retValue)
        msg.setMsg(
            "supportsDataDefinitionAndDataManipulationTransactions is supported");
      else
        msg.setMsg(
            "supportsDataDefinitionAndDataManipulationTransactions is not supported");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle,
          "Call to supportsDataDefinitionAndDataManipulationTransactions is Failed!");

    } catch (Exception e) {
      msg.printError(e,
          "Call to supportsDataDefinitionAndDataManipulationTransactions is Failed!");

    }
  }

  /*
   * @testName: testSupportsDataManipulationTransactionsOnly
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1036; JDBC:JAVADOC:1037;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the supportsDataManipulationTransactionsOnly() method. It
   * should return a boolean value
   *
   */
  public void testSupportsDataManipulationTransactionsOnly() throws Fault {
    try {
      msg.setMsg(
          "Calling DatabaseMetaData.supportsDataManipulationTransactionsOnly");
      // invoke supportsDataManipulationTransactionsOnly method
      boolean retValue = dbmd.supportsDataManipulationTransactionsOnly();
      if (retValue)
        msg.setMsg("supportsDataManipulationTransactionsOnly is supported");
      else
        msg.setMsg("supportsDataManipulationTransactionsOnly is not supported");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle,
          "Call to supportsDataManipulationTransactionsOnly is Failed!");

    } catch (Exception e) {
      msg.printError(e,
          "Call to supportsDataManipulationTransactionsOnly is Failed!");

    }
  }

  /*
   * @testName: testDataDefinitionCausesTransactionCommit
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1038; JDBC:JAVADOC:1039;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the dataDefinitionCausesTransactionCommit() method. It
   * should return a boolean value
   *
   */
  public void testDataDefinitionCausesTransactionCommit() throws Fault {
    try {
      msg.setMsg(
          "Calling DatabaseMetaData.dataDefinitionCausesTransactionCommit");
      // invoke dataDefinitionCausesTransactionCommit method
      boolean retValue = dbmd.dataDefinitionCausesTransactionCommit();
      if (retValue)
        msg.setMsg(
            "Data definition statement forces the transaction to commit");
      else
        msg.setMsg(
            "Data definition statement does not forces the transaction to commit");
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle,
          "Call to dataDefinitionCausesTransactionCommit is Failed!");

    } catch (Exception e) {
      msg.printError(e,
          "Call to dataDefinitionCausesTransactionCommit is Failed!");

    }
  }

  /*
   * @testName: testDataDefinitionIgnoredInTransactions
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1040; JDBC:JAVADOC:1041;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the dataDefinitionIgnoredInTransactions() method. It
   * should return a boolean value
   *
   */
  public void testDataDefinitionIgnoredInTransactions() throws Fault {
    try {
      msg.setMsg(
          "Calling DatabaseMetaData.dataDefinitionIgnoredInTransactions");
      // invoke dataDefinitionIgnoredInTransactions method
      boolean retValue = dbmd.dataDefinitionIgnoredInTransactions();
      if (retValue)
        msg.setMsg("Data definition statement is ignored in a transaction");
      else
        msg.setMsg("Data definition statement is not ignored in a transaction");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle,
          "Call to dataDefinitionIgnoredInTransactions is Failed!");

    } catch (Exception e) {
      msg.printError(e,
          "Call to dataDefinitionIgnoredInTransactions is Failed!");

    }
  }

  /*
   * @testName: testGetProcedures
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1042; JDBC:JAVADOC:1043;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getProcedures() method. It should return a ResultSet
   * object Validate the column names and column ordering.
   */
  public void testGetProcedures() throws Fault {
    try {
      String sColumnNames[] = { "PROCEDURE_CAT", "PROCEDURE_SCHEM",
          "PROCEDURE_NAME", "RESERVED1", "RESERVED2", "RESERVED3", "REMARKS",
          "PROCEDURE_TYPE" };

      ResultSetMetaData rsmd = null;

      boolean statusColumnCount = true;
      boolean statusColumnMatch = true;

      int iColumnNamesLength = sColumnNames.length;

      msg.setMsg("Calling DatabaseMetaData.getProcedures");
      // invoke getProcedures method
      ResultSet oRet_ResultSet = dbmd.getProcedures(sCatalogName, sSchemaName,
          "%");
      String sRetStr = new String();
      msg.setMsg("Store all the procedure names returned");
      sRetStr = "";

      rsmd = oRet_ResultSet.getMetaData();
      int iCount = rsmd.getColumnCount();

      msg.setMsg("Minimum Column Count is:" + iColumnNamesLength);

      msg.setMsg("Comparing Column Lengths");
      if (iColumnNamesLength <= iCount) {
        iCount = iColumnNamesLength;
        statusColumnCount = true;
      } else {
        statusColumnCount = false;
      }

      msg.setMsg("Comparing Column Names...");

      while (iColumnNamesLength > 0) {

        if (iCount < 4 || iCount > 6) {
          if (sColumnNames[iColumnNamesLength - 1]
              .equalsIgnoreCase(rsmd.getColumnName(iCount))) {
            statusColumnMatch = true;
          } else {
            statusColumnMatch = false;
            break;
          }
        }
        iCount--;
        iColumnNamesLength--;
      }

      if ((statusColumnCount == false) || (statusColumnMatch == false)) {
        msg.printTestError(
            "Columns return are not same either in order or name",
            "Call to getProcedures Failed!");

      }

      while (oRet_ResultSet.next())
        sRetStr += oRet_ResultSet.getString(3) + ",";
      if (sRetStr == "")
        msg.setMsg("getProcedures did not return any procedure names");
      else
        msg.setMsg("The Procedure names returned are : "
            + sRetStr.substring(0, sRetStr.length() - 1));

      msg.printTestMsg();
    }

    catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getProcedures is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getProcedures is Failed!");

    }
  }

  /*
   * @testName: testGetProcedureColumns
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1044; JDBC:JAVADOC:1045;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getProcedureColumns() method. It should return a
   * ResultSet object Validate the column names and column ordering.
   */
  public void testGetProcedureColumns() throws Fault {
    try {
      String sColumnNames[] = { "PROCEDURE_CAT", "PROCEDURE_SCHEM",
          "PROCEDURE_NAME", "COLUMN_NAME", "COLUMN_TYPE", "DATA_TYPE",
          "TYPE_NAME", "PRECISION", "LENGTH", "SCALE", "RADIX", "NULLABLE",
          "REMARKS" };
      boolean test_status = true;

      msg.setMsg("Calling DatabaseMetaData.getProcedureColumns");
      // invoke getProcedureColumns method
      ResultSet oRet_ResultSet = dbmd.getProcedureColumns(sCatalogName,
          sSchemaName, "%", "%");
      String sRetStr = new String();
      sRetStr = "";

      msg.setMsg("Call columnCompare for the exact match of column names");
      test_status = columnCompare(sColumnNames, oRet_ResultSet);

      if (test_status == false) {

        msg.printTestError(
            "Columns return are not same either in order or name",
            "Call to getProcedureColumns Failed!");
      }

      if (oRet_ResultSet.next())
        msg.setMsg("getProcedureColumns returned some column names");
      else
        msg.setMsg("getProcedureColumns did not return any column names");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getProcedureColumns is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getProcedureColumns is Failed!");

    }

  }

  /*
   * @testName: testGetTables
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1046; JDBC:JAVADOC:1047;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getTables() method. It should return a ResultSet
   * object Validate the column names and column ordering.
   */
  public void testGetTables() throws Fault {
    try {
      String sColumnNames[] = { "TABLE_CAT", "TABLE_SCHEM", "TABLE_NAME",
          "TABLE_TYPE", "REMARKS" };
      boolean test_status = true;

      msg.setMsg("Calling DatabaseMetaData.getTables");
      // invoke getTables method
      ResultSet oRet_ResultSet = dbmd.getTables(sCatalogName, sSchemaName, "%",
          null);
      String sRetStr = new String();
      sRetStr = "";

      msg.setMsg("Call columnCompare for the exact match of column names");
      test_status = columnCompare(sColumnNames, oRet_ResultSet);

      if (test_status == false) {
        msg.printTestError(
            "Columns return are not same either in order or name",
            "Call to getTables Failed!");

      }

      msg.setMsg("Store all the table names returned");
      while (oRet_ResultSet.next())
        sRetStr += oRet_ResultSet.getString(3) + ",";
      if (sRetStr == "")
        msg.setMsg("getTables did not return any table names");
      else
        msg.setMsg("The Table names returned are : "
            + sRetStr.substring(0, sRetStr.length() - 1));

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getTables is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getTables is Failed!");

    }
  }

  /*
   * @testName: testGetSchemas
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1048; JDBC:JAVADOC:1049;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getSchemas() method. It should return a ResultSet
   * object Validate the column names and column ordering.
   */
  public void testGetSchemas() throws Fault {
    try {
      String sColumnNames[] = { "TABLE_SCHEM", "TABLE_CATALOG" };
      boolean test_status = true;

      msg.setMsg("Calling DatabaseMetaData.getSchemas");
      // invoke getSchemas method
      ResultSet oRet_ResultSet = dbmd.getSchemas();
      String sRetStr = new String();
      sRetStr = "";

      msg.setMsg("Call columnCompare for the exact match of column names");
      test_status = columnCompare(sColumnNames, oRet_ResultSet);

      if (test_status == false) {
        msg.printTestError(
            "Columns return are not same either in order or name",
            "Call to getSchemas Failed!");

      }

      msg.setMsg("Store all the Schema names returned");
      while (oRet_ResultSet.next())
        sRetStr += oRet_ResultSet.getString(1) + ",";
      if (sRetStr == "")
        msg.setMsg("getSchemas did not return any schema names");
      else
        msg.setMsg("The Schema names returned Are : "
            + sRetStr.substring(0, sRetStr.length() - 1));

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getSchemas is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getSchemas is Failed!");

    }
  }

  /*
   * @testName: testGetCatalogs
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1050; JDBC:JAVADOC:1051;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getCatalogs() method. It should return a ResultSet
   * object Validate the column names and column ordering.
   */
  public void testGetCatalogs() throws Fault {
    try {
      String sColumnNames[] = { "TABLE_CAT" };
      boolean test_status = true;

      msg.setMsg("Calling DatabaseMetaData.getCatalogs");
      // invoke getCatalogs method
      ResultSet oRet_ResultSet = dbmd.getCatalogs();
      String sRetStr = new String();
      sRetStr = "";

      msg.setMsg("Call columnCompare for the exact match of column names");
      test_status = columnCompare(sColumnNames, oRet_ResultSet);
      if (test_status == false) {
        msg.printTestError(
            "Columns return are not same either in order or name",
            "Call to getCatalogs Failed!");

      }

      msg.setMsg("Store all the Catalog names returned");
      while (oRet_ResultSet.next())
        sRetStr += oRet_ResultSet.getString(1) + ",";
      if (sRetStr == "")
        msg.setMsg("getCatalogs did not return any catalog names");
      else
        msg.setMsg("The Catalog names returned Are : "
            + sRetStr.substring(0, sRetStr.length() - 1));

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getCatalogs is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getCatalogs is Failed!");

    }
  }

  /*
   * @testName: testGetTableTypes
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1052; JDBC:JAVADOC:1053;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getTableTypes() method. It should return a ResultSet
   * object Validate the column names and column ordering.
   */
  public void testGetTableTypes() throws Fault {
    try {
      String sColumnNames[] = { "TABLE_TYPE" };
      boolean test_status = true;

      msg.setMsg("Calling DatabaseMetaData.getTableTypes");
      // invoke getTableTypes method
      ResultSet oRet_ResultSet = dbmd.getTableTypes();
      String sRetStr = new String();
      sRetStr = "";

      msg.setMsg("Call columnCompare for the exact match of column names");
      test_status = columnCompare(sColumnNames, oRet_ResultSet);

      if (test_status == false) {
        msg.printTestError(
            "Columns return are not same either in order or name",
            "Call to getTableTypes Failed!");

      }

      msg.setMsg("Store all the Table Types returned");
      while (oRet_ResultSet.next())
        sRetStr += oRet_ResultSet.getString(1) + ",";
      if (sRetStr == "")
        msg.setMsg("getTableTypes did not return any table types");
      else
        msg.setMsg("The Table Types returned Are : "
            + sRetStr.substring(0, sRetStr.length() - 1));

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getTableTypes is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getTableTypes is Failed!");

    }
  }

  /*
   * @testName: testGetColumns
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1054; JDBC:JAVADOC:1055;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getColumns() method. It should return a ResultSet
   * object Validate the column names and column ordering.
   */
  public void testGetColumns() throws Fault {
    try {
      String sColumnNames[] = { "TABLE_CAT", "TABLE_SCHEM", "TABLE_NAME",
          "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE",
          "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE",
          "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB",
          "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE",
          "SCOPE_CATALOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE",
          "IS_AUTOINCREMENT", "IS_GENERATEDCOLUMN" };
      boolean test_status = true;

      msg.setMsg("Calling DatabaseMetaData.getColumns");
      msg.setMsg("Catalog Name " + sCatalogName);
      msg.setMsg("Schema Name " + sSchemaName);
      // invoke getColumns method
      ResultSet oRet_ResultSet = dbmd.getColumns(sCatalogName, sSchemaName, "%",
          "%");

      msg.setMsg("Call columnCompare for the exact match of column names");
      test_status = columnCompare(sColumnNames, oRet_ResultSet);
      if (test_status == false) {
        msg.printTestError(
            "Columns return are not same either in order or name",
            "Call to getColumns Failed!");

      }

      if (oRet_ResultSet.next())
        msg.setMsg("getColumns returned some column names");
      else
        msg.setMsg("getColumns did not return any column names");

      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getColumns is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getColumns is Failed!");

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
