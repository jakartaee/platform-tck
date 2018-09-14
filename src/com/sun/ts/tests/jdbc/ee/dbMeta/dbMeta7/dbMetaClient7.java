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
 * @(#)dbMetaClient7.java	1.26 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.dbMeta.dbMeta7;

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
 * The dbMetaClient7 class tests methods of DatabaseMetaData interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class dbMetaClient7 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.dbMeta.dbMeta7";

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
    dbMetaClient7 theTests = new dbMetaClient7();
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
   * @testName: testSupportsUnionAll
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:976; JDBC:JAVADOC:977;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the supportsUnionAll() method on that object. It should
   * return a boolean value; eithet true or false.
   *
   */
  public void testSupportsUnionAll() throws Fault {
    try {
      msg.setMsg("Calling DatabaseMetaData.supportsUnionAll");
      // invoke supportsUnionAll method
      boolean retValue = dbmd.supportsUnionAll();
      if (retValue)
        msg.setMsg("supportsUnionAll is supported");
      else
        msg.setMsg("supportsUnionAll is not supported");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsUnionAll is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsUnionAll is Failed!");

    }
  }

  /*
   * @testName: testSupportsOpenCursorsAcrossCommit
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:978; JDBC:JAVADOC:979;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the supportsOpenCursorsAcrossCommit() method on that
   * object. It should return a boolean value; either true or false.
   *
   */
  public void testSupportsOpenCursorsAcrossCommit() throws Fault {
    try {
      msg.setMsg("Calling DatabaseMetaData.supportsOpenCursorsAcrossCommit");
      // invoke supportsOpenCursorsAcrossCommit method
      boolean retValue = dbmd.supportsOpenCursorsAcrossCommit();
      if (retValue)
        msg.setMsg("supportsOpenCursorsAcrossCommit is supported");
      else
        msg.setMsg("supportsOpenCursorsAcrossCommit is not supported");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle,
          "Call to supportsOpenCursorsAcrossCommit is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsOpenCursorsAcrossCommit is Failed!");

    }
  }

  /*
   * @testName: testSupportsOpenCursorsAcrossRollback
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:980; JDBC:JAVADOC:981;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the supportsOpenCursorsAcrossRollback() method on that
   * object. It should return a boolean value; either true or false.
   *
   */
  public void testSupportsOpenCursorsAcrossRollback() throws Fault {
    try {
      msg.setMsg("Calling DatabaseMetaData.supportsOpenCursorsAcrossRollback");
      // invoke supportsOpenCursorsAcrossRollback method
      boolean retValue = dbmd.supportsOpenCursorsAcrossRollback();
      if (retValue)
        msg.setMsg("supportsOpenCursorsAcrossRollback is supported");
      else
        msg.setMsg("supportsOpenCursorsAcrossRollback is not supported");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle,
          "Call to supportsOpenCursorsAcrossRollback is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsOpenCursorsAcrossRollback is Failed!");

    }
  }

  /*
   * @testName: testSupportsOpenStatementsAcrossCommit
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:982; JDBC:JAVADOC:983;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the supportsOpenStatementsAcrossCommit() method on that
   * object. It should return a boolean value; either true or false.
   *
   */
  public void testSupportsOpenStatementsAcrossCommit() throws Fault {
    try {
      msg.setMsg("Calling DatabaseMetaData.supportsOpenStatementsAcrossCommit");
      // invoke supportsOpenStatementsAcrossCommit method
      boolean retValue = dbmd.supportsOpenStatementsAcrossCommit();
      if (retValue)
        msg.setMsg("supportsOpenStatementsAcrossCommit is supported");
      else
        msg.setMsg("supportsOpenStatementsAcrossCommit is not supported");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle,
          "Call to supportsOpenStatementsAcrossCommit is Failed!");

    } catch (Exception e) {
      msg.printError(e,
          "Call to supportsOpenStatementsAcrossCommit is Failed!");

    }
  }

  /*
   * @testName: testSupportsOpenStatementsAcrossRollback
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:984; JDBC:JAVADOC:985;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the supportsOpenStatementsAcrossRollback() method on that
   * object. It should return a boolean value; either true or false.
   *
   */
  public void testSupportsOpenStatementsAcrossRollback() throws Fault {
    try {
      msg.setMsg(
          "Calling DatabaseMetaData.supportsOpenStatementsAcrossRollback");
      // invoke supportsOpenStatementsAcrossRollback method
      boolean retValue = dbmd.supportsOpenStatementsAcrossRollback();
      if (retValue)
        msg.setMsg("supportsOpenStatementsAcrossRollback is supported");
      else
        msg.setMsg("supportsOpenStatementsAcrossRollback is not supported");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle,
          "Call to supportsOpenStatementsAcrossRollback is Failed!");

    } catch (Exception e) {
      msg.printError(e,
          "Call to supportsOpenStatementsAcrossRollback is Failed!");

    }
  }

  /*
   * @testName: testGetMaxBinaryLiteralLength
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:986; JDBC:JAVADOC:987;
   * JavaEE:SPEC:193;
   * 
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getMaxBinaryLiteralLength() method on that object. It
   * should return an integer value
   *
   */
  public void testGetMaxBinaryLiteralLength() throws Fault {
    try {
      msg.setMsg("Calling DatabaseMetaData.getMaxBinaryLiteralLength");
      // invoke getMaxBinaryLiteralLength method
      int nRetval = dbmd.getMaxBinaryLiteralLength();
      if (nRetval < 0)
        msg.setMsg("getMaxBinaryLiteralLength returns a negative value");
      else
        msg.setMsg("getMaxBinaryLiteralLength returns " + nRetval);

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getMaxBinaryLiteralLength is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getMaxBinaryLiteralLength is Failed!");

    }
  }

  /*
   * @testName: testGetMaxCharLiteralLength
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:988; JDBC:JAVADOC:989;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getMaxCharLiteralLength() method on that object. It
   * should return an integer value.
   *
   */
  public void testGetMaxCharLiteralLength() throws Fault {
    try {
      msg.setMsg("Calling DatabaseMetaData.getMaxCharLiteralLength");
      // invoke getMaxCharLiteralLength method
      int nRetval = dbmd.getMaxCharLiteralLength();
      if (nRetval < 0)
        msg.setMsg("getMaxCharLiteralLength returns a negative value");
      else
        msg.setMsg("getMaxCharLiteralLength returns " + nRetval);

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getMaxCharLiteralLength is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getMaxCharLiteralLength is Failed!");

    }
  }

  /*
   * @testName: testGetMaxColumnNameLength
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:990; JDBC:JAVADOC:991;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getMaxColumnNameLength() method on that object. It
   * should return an integer value
   *
   */
  public void testGetMaxColumnNameLength() throws Fault {
    try {
      msg.setMsg("Calling DatabaseMetaData.getMaxColumnNameLength");
      // invoke getMaxColumnNameLength method
      int nRetval = dbmd.getMaxColumnNameLength();
      if (nRetval < 0)
        msg.setMsg("getMaxColumnNameLength returns a negative value");
      else
        msg.setMsg("getMaxColumnNameLength returns " + nRetval);

      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getMaxColumnNameLength is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getMaxColumnNameLength is Failed!");

    }
  }

  /*
   * @testName: testGetMaxColumnsInGroupBy
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:992; JDBC:JAVADOC:993;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getMaxColumnsInGroupBy() method on that object. It
   * should return an integer value.
   *
   */
  public void testGetMaxColumnsInGroupBy() throws Fault {
    try {
      msg.setMsg("Calling DatabaseMetaData.getMaxColumnsInGroupBy");
      // invoke getMaxColumnsInGroupBy method
      int nRetval = dbmd.getMaxColumnsInGroupBy();
      if (nRetval < 0)
        msg.printTestError("getMaxColumnsInGroupBy returns a negative value",
            " testGetMaxColumnsInGroupBy Failed");
      else
        msg.setMsg("getMaxColumnsInGroupBy returns " + nRetval);

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getMaxColumnsInGroupBy is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getMaxColumnsInGroupBy is Failed!");

    }
  }

  /*
   * @testName: testGetMaxColumnsInIndex
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:994; JDBC:JAVADOC:995;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getMaxColumnsInIndex() method on that object. It
   * should return an integer value.
   *
   */
  public void testGetMaxColumnsInIndex() throws Fault {
    try {
      msg.setMsg("Calling DatabaseMetaData.getMaxColumnsInIndex");
      // invoke getMaxColumnsInIndex method
      int nRetval = dbmd.getMaxColumnsInIndex();
      if (nRetval < 0)
        msg.setMsg("getMaxColumnsInIndex returns a negative value");
      else
        msg.setMsg("getMaxColumnsInIndex returns " + nRetval);

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getMaxColumnsInIndex is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getMaxColumnsInIndex is Failed!");

    }
  }

  /*
   * @testName: testGetMaxColumnsInOrderBy
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:996; JDBC:JAVADOC:997;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getMaxColumnsInOrderBy() method on that object. It
   * should return an integer value.
   *
   */
  public void testGetMaxColumnsInOrderBy() throws Fault {
    try {
      msg.setMsg("Calling DatabaseMetaData.getMaxColumnsInOrderBy");
      // invoke getMaxColumnsInOrderBy method
      int nRetval = dbmd.getMaxColumnsInOrderBy();
      if (nRetval < 0)
        msg.setMsg("getMaxColumnsInOrderBy returns a negative value");
      else
        msg.setMsg("getMaxColumnsInOrderBy returns " + nRetval);

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getMaxColumnsInOrderBy is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getMaxColumnsInOrderBy is Failed!");

    }
  }

  /*
   * @testName: testGetMaxColumnsInSelect
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:998; JDBC:JAVADOC:999;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getMaxColumnsInSelect() method on that object. It
   * should return an integer value.
   *
   */
  public void testGetMaxColumnsInSelect() throws Fault {
    try {
      msg.setMsg("Calling DatabaseMetaData.getMaxColumnsInSelect");
      // invoke getMaxColumnsInSelect method
      int nRetval = dbmd.getMaxColumnsInSelect();
      if (nRetval < 0)
        msg.setMsg("getMaxColumnsInSelect returns a negative value");
      else
        msg.setMsg("getMaxColumnsInSelect returns " + nRetval);

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getMaxColumnsInSelect is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getMaxColumnsInSelect is Failed!");

    }
  }

  /*
   * @testName: testGetMaxColumnsInTable
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1000; JDBC:JAVADOC:1001;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getMaxColumnsInTable() method on that object. It
   * should return an integer value.
   *
   */
  public void testGetMaxColumnsInTable() throws Fault {
    try {
      msg.setMsg("Calling DatabaseMetaData.getMaxColumnsInTable");
      // invoke getMaxColumnsInTable method
      int nRetval = dbmd.getMaxColumnsInTable();
      if (nRetval < 0)
        msg.setMsg("getMaxColumnsInTable returns a negative value");
      else
        msg.setMsg("getMaxColumnsInTable returns " + nRetval);

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getMaxColumnsInTable is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getMaxColumnsInTable is Failed!");

    }
  }

  /*
   * @testName: testGetMaxConnections
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1002; JDBC:JAVADOC:1003;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getMaxConnections() method on that object. It should
   * return an integer value
   *
   */
  public void testGetMaxConnections() throws Fault {
    try {
      msg.setMsg("Calling DatabaseMetaData.getMaxConnections");
      // invoke getMaxConnections method
      int nRetval = dbmd.getMaxConnections();
      if (nRetval < 0)
        msg.setMsg("getMaxConnections returns a negative value");
      else
        msg.setMsg("getMaxConnections returns " + nRetval);

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getMaxConnections is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getMaxConnections is Failed!");

    }
  }

  /*
   * @testName: testGetMaxCursorNameLength
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1004; JDBC:JAVADOC:1005;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getMaxCursorNameLength() method on that object. It
   * should return an integer value
   *
   */
  public void testGetMaxCursorNameLength() throws Fault {
    try {
      msg.setMsg("Calling DatabaseMetaData.getMaxCursorNameLength");
      // invoke getMaxCursorNameLength method
      int nRetval = dbmd.getMaxCursorNameLength();
      if (nRetval < 0)
        msg.setMsg("getMaxCursorNameLength returns a negative value");
      else
        msg.setMsg("getMaxCursorNameLength returns " + nRetval);

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getMaxCursorNameLength is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getMaxCursorNameLength is Failed!");

    }
  }

  /*
   * @testName: testGetMaxIndexLength
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1006; JDBC:JAVADOC:1007;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getMaxIndexLength() method on that object. It should
   * return an integer value
   *
   */
  public void testGetMaxIndexLength() throws Fault {
    try {
      msg.setMsg("Calling DatabaseMetaData.getMaxIndexLength");
      // invoke getMaxIndexLength method
      int nRetval = dbmd.getMaxIndexLength();
      if (nRetval < 0)
        msg.setMsg("getMaxIndexLength returns a negative value");
      else
        msg.setMsg("getMaxIndexLength returns " + nRetval);

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getMaxIndexLength is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getMaxIndexLength is Failed!");

    }
  }

  /*
   * @testName: testGetMaxSchemaNameLength
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1008; JDBC:JAVADOC:1009;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getMaxSchemaNameLength() method on that object. It
   * should return an integer value.
   *
   */
  public void testGetMaxSchemaNameLength() throws Fault {
    try {
      msg.setMsg("Calling DatabaseMetaData.getMaxSchemaNameLength");
      // invoke getMaxSchemaNameLength method
      int nRetval = dbmd.getMaxSchemaNameLength();

      if (nRetval < 0)
        msg.setMsg("getMaxSchemaNameLength returns a negative value");
      else
        msg.setMsg("getMaxSchemaNameLength returns " + nRetval);

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getMaxSchemaNameLength is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getMaxSchemaNameLength is Failed!");

    }
  }

  /*
   * @testName: testGetMaxProcedureNameLength
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1010; JDBC:JAVADOC:1011;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getMaxProcedureNameLength() method on that object. It
   * should return an integer value.
   *
   */
  public void testGetMaxProcedureNameLength() throws Fault {
    try {
      msg.setMsg("Calling DatabaseMetaData.getMaxProcedureNameLength");
      // invoke getMaxProcedureNameLength method
      int nRetval = dbmd.getMaxProcedureNameLength();
      if (nRetval < 0)
        msg.setMsg("getMaxProcedureNameLength returns a negative value");
      else
        msg.setMsg("getMaxProcedureNameLength returns " + nRetval);

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getMaxProcedureNameLength is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getMaxProcedureNameLength is Failed!");

    }
  }

  /*
   * @testName: testGetMaxCatalogNameLength
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1012; JDBC:JAVADOC:1013;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getMaxCatalogNameLength() method on that object. It
   * should return an integer value.
   *
   */
  public void testGetMaxCatalogNameLength() throws Fault {
    try {
      msg.setMsg("Calling DatabaseMetaData.getMaxCatalogNameLength");
      // invoke getMaxCatalogNameLength method
      int nRetval = dbmd.getMaxCatalogNameLength();
      if (nRetval < 0)
        msg.setMsg("getMaxCatalogNameLength returns a negative value");
      else
        msg.setMsg("getMaxCatalogNameLength returns " + nRetval);

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getMaxCatalogNameLength is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getMaxCatalogNameLength is Failed!");

    }
  }

  /*
   * @testName: testGetMaxRowSize
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1014; JDBC:JAVADOC:1015;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getMaxRowSize() method on that object. It should
   * return an integer value
   *
   */
  public void testGetMaxRowSize() throws Fault {
    try {
      msg.setMsg("Calling DatabaseMetaData.getMaxRowSize");
      // invoke getMaxRowSize method
      int nRetval = dbmd.getMaxRowSize();
      if (nRetval < 0)
        msg.printTestError("getMaxRowSize returns a negative value",
            "testGetMaxRowSize Failed!");
      else
        msg.setMsg("getMaxRowSize returns " + nRetval);

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getMaxRowSize is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getMaxRowSize is Failed!");

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
