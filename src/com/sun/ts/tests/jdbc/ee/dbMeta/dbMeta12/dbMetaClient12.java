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
 * @(#)dbMetaClient12.java	1.2 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.dbMeta.dbMeta12;

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

/**
 * The dbMetaClient class tests methods of DatabaseMetaData interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.0, 16/09/2002
 */

public class dbMetaClient12 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.dbMeta.dbMeta12";

  // Naming specific member variables
  private TSNamingContextInterface jc = null;

  // Harness requirements

  private transient Connection conn = null;

  private transient DatabaseMetaData dbmd = null;

  private DataSource ds1 = null;

  private dbSchema dbSch = null;

  private String drManager = null;

  private JDBCTestMsg msg = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    dbMetaClient12 theTests = new dbMetaClient12();
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

        if (drManager.length() == 0)
          throw new Fault("Invalid DriverManager Name");

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
   * @testName: testGetSQLStateType
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1130; JDBC:JAVADOC:1131;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getSQLStateType() method on that object. It should
   * return an integer value.
   * 
   */

  public void testGetSQLStateType() throws Fault {
    try

    {
      msg.setMsg("Calling DatabaseMetaData.getSQLStateType");
      int SQLState = dbmd.getSQLStateType();

      msg.setMsg("getSQLStateType methods returns SQLStateType: " + SQLState);

      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getSQLStateType is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getSQLStateType is Failed!");

    }

  }

  /*
   * @testName: testGetDatabaseMinorVersion
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1124; JDBC:JAVADOC:1125;
   * JavaEE:SPEC:193;
   * 
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getDatabaseMinorVersion() method on that object. It
   * should return an integer value.
   * 
   */

  public void testGetDatabaseMinorVersion() throws Fault {
    try

    {
      msg.setMsg("Calling DatabaseMetaData.getDatabaseMinorVersion");
      int dbMinorVersion = dbmd.getDatabaseMinorVersion();

      msg.setMsg(
          "getDatabaseMinorVersion methods returns DatabaseMinorVersion: "
              + dbMinorVersion);

      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getDatabaseMinorVersion is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getDatabaseMinorVersion is Failed!");

    }

  }

  /*
   * @testName: testGetDatabaseMajorVersion
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1122; JDBC:JAVADOC:1123;
   * JavaEE:SPEC:193;
   * 
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getDatabaseMajorVersion() method on that object. It
   * should return an integer value.
   * 
   */

  public void testGetDatabaseMajorVersion() throws Fault {
    try

    {
      msg.setMsg("Calling DatabaseMetaData.getDatabaseMajorVersion");
      int dbMajorVersion = dbmd.getDatabaseMajorVersion();

      msg.setMsg(
          "getDatabaseMajorVersion methods returns DatabaseMajorVersion: "
              + dbMajorVersion);

      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getDatabaseMajorVersion is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getDatabaseMajorVersion is Failed!");

    }

  }

  /*
   * @testName: testGetJDBCMinorVersion
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1128; JDBC:JAVADOC:1129;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getJDBCMinorVersion() method on that object. It
   * should return an integer value.
   * 
   */

  public void testGetJDBCMinorVersion() throws Fault {
    try

    {
      msg.setMsg("Calling DatabaseMetaData.getJDBCMinorVersion");
      int jdbcMinorVersion = dbmd.getJDBCMinorVersion();

      msg.setMsg("getJDBCMinorVersion methods returns JDBCMinorVersion: "
          + jdbcMinorVersion);
      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getJDBCMinorVersion is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getJDBCMinorVersion is Failed!");

    }

  }

  /*
   * @testName: testGetJDBCMajorVersion
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1126; JDBC:JAVADOC:1127;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getJDBCMajorVersion() method on that object. It
   * should return an integer value.
   * 
   */

  public void testGetJDBCMajorVersion() throws Fault {
    try

    {
      msg.setMsg("Calling DatabaseMetaData.getJDBCMajorVersion");
      int jdbcMajorVersion = dbmd.getJDBCMajorVersion();

      msg.setMsg("getJDBCMajorVersion methods returns JDBCMajorrVersion : "
          + jdbcMajorVersion);

      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getJDBCMajorVersion is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getJDBCMajorVersion is Failed!");

    }

  }

  /*
   * @testName: testSupportsSavepoints
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1104; JDBC:JAVADOC:1105;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the supportsSavepoints() method. It should return a
   * boolean value
   *
   */
  public void testSupportsSavepoints() throws Fault {
    try {
      msg.setMsg("Calling DatabaseMetaData.supportsBatchUpdates");
      // invoke supportsBatchUpdates method
      boolean retValue = dbmd.supportsSavepoints();
      if (retValue)
        msg.setMsg("supportsSavepoints is supported");
      else
        msg.setMsg("supportsSavepoints is not supported");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsSavepoints is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsSavepoints is Failed!");

    }
  }

  /*
   * @testName: testSupportsNamedParameters
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1106; JDBC:JAVADOC:1107;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the supportsSavepoints() method. It should return a
   * boolean value
   *
   */
  public void testSupportsNamedParameters() throws Fault {
    try {
      msg.setMsg("Calling DatabaseMetaData.supportsNamedParameters");

      boolean retValue = dbmd.supportsNamedParameters();
      if (retValue)
        msg.setMsg("supportsNamedParameters is supported");
      else
        msg.setMsg("supportsNamedParameters is not supported");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsNamedParameters is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsNamedParameters is Failed!");

    }
  }

  /*
   * @testName: testSupportsMultipleOpenResults
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1108; JDBC:JAVADOC:1109;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the supportsMultipleOpenResults() method. It should
   * return a boolean value
   *
   */
  public void testSupportsMultipleOpenResults() throws Fault {
    try {
      msg.setMsg("Calling DatabaseMetaData.supportsMultipleOpenResults");

      boolean retValue = dbmd.supportsMultipleOpenResults();
      if (retValue)
        msg.setMsg("supportsMultipleOpenResults is supported");
      else
        msg.setMsg("supportsMultipleOpenResults is not supported");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsMultipleOpenResults is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsMultipleOpenResults is Failed!");

    }
  }

  /*
   * @testName: testSupportsGetGeneratedKeys
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1110; JDBC:JAVADOC:1111;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the supportsGetGeneratedKeys() method. It should return a
   * boolean value
   *
   */
  public void testSupportsGetGeneratedKeys() throws Fault {
    try {
      msg.setMsg("Calling DatabaseMetaData.supportsGetGeneratedKeys");

      boolean retValue = dbmd.supportsGetGeneratedKeys();
      if (retValue)
        msg.setMsg("supportsGetGeneratedKeys is supported");
      else
        msg.setMsg("supportsGetGeneratedKeys is not supported");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsGetGeneratedKeys is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsGetGeneratedKeys is Failed!");

    }
  }

  /*
   * @testName: testSupportsResultSetHoldability01
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1118; JDBC:JAVADOC:1119;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the supportsResultSetHoldability(int holdability) method.
   * It should return a boolean value
   *
   */
  public void testSupportsResultSetHoldability01() throws Fault {
    try {
      msg.setMsg("Calling DatabaseMetaData.supportsResultSetHoldability");

      boolean retValue = dbmd
          .supportsResultSetHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
      if (retValue)
        msg.setMsg(
            "supportsResultSetHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT) is supported");
      else
        msg.setMsg(
            "supportsResultSetHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT) is not supported");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle,
          "Call to supportsResultSetHoldability is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsResultSetHoldability is Failed!");

    }
  }

  /*
   * @testName: testSupportsResultSetHoldability02
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1118; JDBC:JAVADOC:1119;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the supportsResultSetHoldability(int holdability) method.
   * It should return a boolean value
   *
   */
  public void testSupportsResultSetHoldability02() throws Fault {
    try {
      msg.setMsg("Calling DatabaseMetaData.supportsResultSetHoldability");

      boolean retValue = dbmd
          .supportsResultSetHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT);
      if (retValue)
        msg.setMsg(
            "supportsResultSetHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT) is supported");
      else
        msg.setMsg(
            "supportsResultSetHoldability (ResultSet.CLOSE_CURSORS_AT_COMMIT)is not supported");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle,
          "Call to supportsResultSetHoldability is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsResultSetHoldability is Failed!");

    }
  }

  /*
   * @testName: testGetResultSetHoldability
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1120; JDBC:JAVADOC:1121;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the supportsResultSetHoldability(int holdability) method.
   * It should return a boolean value
   *
   */
  public void testGetResultSetHoldability() throws Fault {
    try {
      msg.setMsg("Calling DatabaseMetaData.supportsResultSetHoldability");

      int holdability = dbmd.getResultSetHoldability();

      if ((holdability == ResultSet.CLOSE_CURSORS_AT_COMMIT)
          || (holdability == ResultSet.HOLD_CURSORS_OVER_COMMIT)) {
        msg.setMsg("getResultSetHoldability returns: " + holdability);
      } else {
        msg.printTestError(
            "getResultSetHoldability does not return default value: ",
            "call to getResultSetHoldability fails");
      }

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getResultSetHoldability is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getResultSetHoldability is Failed!");

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
