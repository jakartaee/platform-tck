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
 * @(#)connectionClient1.java	1.22 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.connection.connection1;

import java.io.*;
import java.util.*;
import java.math.*;

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
 * The connectionClient1 class tests methods of Connection interface using Sun's
 * J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class connectionClient1 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.connection.connection1";

  // Naming specific member variables
  private TSNamingContextInterface jc = null;

  // Harness requirements

  private transient Connection conn = null;

  private ResultSet rs = null;

  private Statement stmt = null;

  private transient DatabaseMetaData dbmd = null;

  private DataSource ds1 = null;

  private dbSchema dbSch = null;

  private rsSchema rsSch = null;

  private String drManager = null;

  private Properties props = null;

  private String sVehicle = null;

  private JDBCTestMsg msg = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    connectionClient1 theTests = new connectionClient1();
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
        sVehicle = p.getProperty("vehicle");
        if (drManager.length() == 0)
          throw new Fault("Invalid DriverManager Name");
        props = p;

        if (drManager.equals("yes")) {
          logTrace("Using DriverManager");
          DriverManagerConnection dmCon = new DriverManagerConnection();
          conn = dmCon.getConnection(p);
        } else {
          logTrace("Using DataSource");
          DataSourceConnection dsCon = new DataSourceConnection();
          conn = dsCon.getConnection(p);
        }
        stmt = conn.createStatement();
        dbSch = new dbSchema();
        dbSch.createData(p, conn);
        dbmd = conn.getMetaData();
        rsSch = new rsSchema();
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
   * @testName: testClose
   * 
   * @assertion_ids: JavaEE:SPEC:179; JDBC:JAVADOC:1157; JDBC:JAVADOC:1158;
   * JDBC:JAVADOC:1159; JDBC:JAVADOC:1160;
   *
   * @test_Strategy: Get a Connection object and call close() method and call
   * isClosed() method and it should return a true value
   *
   */
  public void testClose() throws Fault {
    boolean closeFlag = false;
    Connection con = null;
    try {
      if (drManager.equals("yes")) {
        DriverManagerConnection dmCon = new DriverManagerConnection();
        con = dmCon.getConnection(props);
      } else {
        DataSourceConnection dsCon = new DataSourceConnection();
        con = dsCon.getConnection(props);
      }

      // invoking the close method
      msg.setMsg("Calling Close() method ");
      con.close();

      closeFlag = con.isClosed();

      if (closeFlag) {
        msg.setMsg("close method closes the Connection object ");
      } else {
        msg.printTestError("close method does not close the Connection object",
            "test Close method Failed!");

      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to Close is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to Close is Failed!");

    }
  }

  /*
   * @testName: testCreateStatement01
   * 
   * @assertion_ids: JavaEE:SPEC:179; JDBC:JAVADOC:1141; JDBC:JAVADOC:1142;
   *
   * @test_Strategy: Get a Connection object and call createStatement() method
   * and call instanceof to check It should return a Statement object
   */
  public void testCreateStatement01() throws Fault {
    Statement statemt = null;
    try {
      // invoking the createStatement method
      msg.setMsg("Calling createStatement() method ");
      statemt = conn.createStatement();

      if (statemt instanceof Statement) {
        msg.setMsg("createStatement method creates a Statement object");
      } else {
        msg.printTestError(
            "createStatement method does not create a Statement object",
            "test createStatement Failed!");

      }
      statemt.close();
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to createStatement is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to createStatement is Failed!");

    }
  }

  /*
   * @testName: testGetCatalog
   * 
   * @assertion_ids: JavaEE:SPEC:179; JDBC:JAVADOC:1169; JDBC:JAVADOC:1170;
   * 
   * @test_Strategy: Get a Connection object and call getCatalog() method It
   * should return a String value The getCatalogs() method in Databasemeta data
   * object will return a Resultset object that contains the catalog name in the
   * column TABLE_CAT .The String returned by Connection.getCatalog() method
   * will be checked against these column values.
   */
  public void testGetCatalog() throws Fault {
    String catalogName = null;
    String url = null;
    String retValue = null;
    String extValue = null;
    boolean flag = false;
    try {

      // to retrieve the database from the URL of the propertyFile
      msg.setMsg("Calling getCatalog() method ");
      retValue = conn.getCatalog();

      msg.setMsg("Catalog Name   " + retValue);

      if (retValue != null) {
        ResultSet rs = dbmd.getCatalogs();

        while (rs.next()) {
          extValue = rs.getString("TABLE_CAT");

          msg.setMsg("Catalog Name   " + extValue);
          if (retValue.equals(extValue)) {
            flag = true;
            break;
          }
        }
        if (flag) {
          msg.setMsg("getCatalog returns the Catalog name" + retValue);
        } else {
          msg.printTestError("getCatalog does not return the catalog name",
              "test getCatalog Failed!");

        }
      } else {
        msg.setMsg("getCatalog returns a null String Object");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getCatalog is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getCatalog is Failed!");

    }
  }

  /*
   * @testName: testGetMetaData
   * 
   * @assertion_ids: JavaEE:SPEC:179; JDBC:JAVADOC:1161; JDBC:JAVADOC:1162;
   *
   * @test_Strategy: Get a Connection object and call getMetaData() method and
   * call instanceof method to check It should return a DatabaseMetaData object
   *
   */
  public void testGetMetaData() throws Fault {
    DatabaseMetaData rsmd = null;
    try {
      // invoking the getMetaData method
      msg.setMsg("Calling getMetaData() method ");
      rsmd = conn.getMetaData();

      if (rsmd instanceof DatabaseMetaData) {
        msg.setMsg("getMetaData returns the DatabaseMetaData object ");
      } else {
        msg.printTestError(
            "getMetaData does not return the DatabaseMetaData object",
            "test getMetaData Failed!");

      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getMetaData is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getMetaData is Failed!");

    }
  }

  /*
   * @testName: testGetTransactionIsolation
   * 
   * @assertion_ids: JavaEE:SPEC:179; JDBC:JAVADOC:1173; JDBC:JAVADOC:1174;
   * JDBC:SPEC:16; JDBC:SPEC:15
   *
   * @test_Strategy: Get a Connection object and call getTransactionIsolation()
   * method It should return a Integer value and must be equal to the value of
   * TRANSACTION_NONE or TRANSACTION_READ_COMMITTED or
   * TRANSACTION_READ_UNCOMMITTED or TRANSACTION_REPEATABLE_READ or
   * TRANSACTION_SERIALIZABLE which is default set by the driver
   * 
   *
   */
  public void testGetTransactionIsolation() throws Fault {
    int transIsolateVal = 0;
    try {
      // invoking the getTransactionIsolation method
      msg.setMsg("Calling getTransactionIsolation() method ");
      transIsolateVal = conn.getTransactionIsolation();

      if (transIsolateVal == Connection.TRANSACTION_NONE) {
        msg.setMsg(
            "getTransactionIsolation method returns Transaction Isolation mode as "
                + transIsolateVal);
      } else if (transIsolateVal == Connection.TRANSACTION_READ_COMMITTED) {
        msg.setMsg(
            "getTransactionIsolation method returns Transaction Isolation mode as "
                + transIsolateVal);
      } else if (transIsolateVal == Connection.TRANSACTION_READ_UNCOMMITTED) {
        msg.setMsg(
            "getTransactionIsolation method returns Transaction Isolation mode as "
                + transIsolateVal);
      } else if (transIsolateVal == Connection.TRANSACTION_REPEATABLE_READ) {
        msg.setMsg(
            "getTransactionIsolation method returns Transaction Isolation mode as "
                + transIsolateVal);
      } else if (transIsolateVal == Connection.TRANSACTION_SERIALIZABLE) {
        msg.setMsg(
            "getTransactionIsolation method returns Transaction Isolation mode as "
                + transIsolateVal);
      } else {
        msg.printTestError(
            "getTransactionIsolation method returns an invalid value",
            "test getTransactionIsolation is Failed!");

      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getTransactionIsolation is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getTransactionIsolation is Failed!");

    }
  }

  /*
   * @testName: testIsClosed01
   * 
   * @assertion_ids: JavaEE:SPEC:179; JDBC:JAVADOC:1159; JDBC:JAVADOC:1160;
   *
   * @test_Strategy: Get a Connection object and call isClosed() method It
   * should return a boolean value and the value should be equal to false
   *
   */
  public void testIsClosed01() throws Fault {
    boolean closeFlag = false;
    Connection con = null;
    try {
      if (drManager.equals("yes")) {
        DriverManagerConnection dmCon = new DriverManagerConnection();
        con = dmCon.getConnection(props);
      } else {
        DataSourceConnection dsCon = new DataSourceConnection();
        con = dsCon.getConnection(props);
      }

      msg.setMsg("Calling isClosed Method");
      closeFlag = con.isClosed();

      if (!closeFlag) {
        msg.setMsg("isClosed method returns  " + closeFlag);
      } else {
        msg.printTestError("isClosed method returns an invalid value",
            "test isClosed Failed!");

      }
      con.close();
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to isClosed is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to isClosed is Failed!");

    }
  }

  /*
   * @testName: testIsClosed02
   * 
   * @assertion_ids: JavaEE:SPEC:179; JDBC:JAVADOC:1157; JDBC:JAVADOC:1158;
   * JDBC:JAVADOC:1159; JDBC:JAVADOC:1160;
   *
   * @test_Strategy: Get a Connection object and call close() method and call
   * isClosed() method It should return a boolean value and the value should be
   * equal to true
   *
   */
  public void testIsClosed02() throws Fault {
    boolean closeFlag = false;
    Connection con = null;
    try {
      if (drManager.equals("yes")) {
        DriverManagerConnection dmCon = new DriverManagerConnection();
        con = dmCon.getConnection(props);
      } else {
        DataSourceConnection dsCon = new DataSourceConnection();
        con = dsCon.getConnection(props);
      }

      // invoking the close method
      con.close();

      msg.setMsg("Calling isClosed() method ");
      closeFlag = con.isClosed();

      if (closeFlag) {
        msg.setMsg("isClosed method returns  " + closeFlag);
      } else {
        msg.printTestError("isClosed method returns an invalid value",
            "test isClosed Failed");

      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to isClosed is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to isClosed is Failed!");

    }
  }

  /*
   * @testName: testIsReadOnly
   * 
   * @assertion_ids: JavaEE:SPEC:179; JDBC:JAVADOC:1165; JDBC:JAVADOC:1166;
   * JDBC:JAVADOC:1163; JDBC:JAVADOC:1164;
   *
   * @test_Strategy: Get a Connection object and call setReadOnly(boolean b)
   * method and call isReadOnly() method It should return a boolean value that
   * is been set
   *
   */

  public void testIsReadOnly() throws Fault {
    boolean errorFlag = true;
    try {
      msg.setMsg("Testing in " + sVehicle + " vehicle");

      if (sVehicle.equalsIgnoreCase("ejb")) {
        // In case of EJB vehicle connection will be in a transaction and
        // it wont be possible to set the ReadOnly property. If the api can
        // return a boolean it is successful.
        boolean b = conn.isReadOnly();
        errorFlag = false;
      } else {
        msg.setMsg("invoking the setReadOnly method");
        conn.setReadOnly(false);
        msg.setMsg("Calling isReadOnly() method ");
        if (conn.isReadOnly()) {
          errorFlag = true;
        } else {
          errorFlag = false;
        }
      }

      if (!errorFlag) {
        msg.setMsg("isReadOnly method is Successful");
      } else {
        msg.printTestError("isReadOnly method returns an invalid value",
            "test isReadOnly Failed");

      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to isReadOnly is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to isReadOnly is Failed!");

    }
  }

  /*
   * @testName: testNativeSQL
   * 
   * @assertion_ids: JavaEE:SPEC:179; JDBC:JAVADOC:1147; JDBC:JAVADOC:1148;
   *
   * @test_Strategy: Get a Connection object and call nativeSQL(String sql)
   * method It should return a String value which represents native SQL grammar
   * implementation of the SQL statement if the driver supports else it returns
   * the actual SQL statement as a String.This is checked by using instanceof
   * method
   */
  public void testNativeSQL() throws Fault {
    String sSqlStmt = null;
    String nativeSql = null;
    try {
      sSqlStmt = props.getProperty("Escape_Seq_Query", "");
      msg.setMsg("SQL Statement with Escape Syntax" + sSqlStmt);

      // invoking the nativeSQL method
      msg.setMsg("Calling nativeSQL method ");
      nativeSql = conn.nativeSQL(sSqlStmt);

      if (nativeSql instanceof String) {
        msg.setMsg("nativeSQL method returns :  " + nativeSql);
      } else {
        msg.printTestError(
            "nativeSQL method does not return the System native SQL grammar",
            "Call to nativeSQL is Failed!");

      }
      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to nativeSQL is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to nativeSQL is Failed!");

    }
  }

  /* cleanup */
  public void cleanup() throws Fault {
    try {
      dbSch.destroyData(conn);
      // Close the Statement object
      stmt.close();
      // close the Database
      rsSch.dbUnConnect(conn);
      logMsg("Cleanup ok;");
    } catch (Exception e) {
      logErr("An error occurred while closing the database connection", e);
    }
  }

}
