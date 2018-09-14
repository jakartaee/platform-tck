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
 * @(#)sqlExceptionClient.java	1.16 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.exception.sqlException;

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
 * The sqlExceptionClient class tests methods of SQLException class using Sun's
 * J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class sqlExceptionClient extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.exception.sqlException";

  // Naming specific member variables
  private TSNamingContextInterface jc = null;

  // Harness requirements

  private transient Connection conn = null;

  private ResultSet rs = null;

  private Statement stmt = null;

  private DataSource ds1 = null;

  private dbSchema dbSch = null;

  private rsSchema rsSch = null;

  private String drManager = null;

  private Properties sqlp = null;

  private boolean isThrown = false;

  private String sReason = null;

  private String sSqlState = null;

  private String sVendorCode = null;

  private String sUsr, sPass, sUrl;

  private int vendorCode = 0, maxVal = 0, minVal = 0;

  private int[] updateCount = null;

  private JDBCTestMsg msg = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    sqlExceptionClient theTests = new sqlExceptionClient();
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
        if (drManager.length() == 0)
          throw new Fault("Invalid DriverManager Name");
        sUrl = p.getProperty("db1", "");
        sUsr = p.getProperty("user1", "");
        sPass = p.getProperty("password1", "");
        /*
         * sqlp = new Properties(); String sqlStmt= p.getProperty("rsQuery","");
         * InputStream istr= new FileInputStream(sqlStmt); sqlp.load(istr);
         */
        sqlp = p;
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
        rsSch = new rsSchema();
        dbSch.createData(p, conn);
        // conn.setAutoCommit(false);
        msg = new JDBCTestMsg();
        stmt = conn.createStatement();
        sReason = sqlp.getProperty("Reason_BatUpdExec");
        logTrace("Reason : " + sReason);
        sSqlState = sqlp.getProperty("SQLState_BatUpdExec");
        logTrace("SQLState : " + sSqlState);

        sVendorCode = sqlp.getProperty("VendorCode_BatUpdExec");
        logTrace("VendorCode : " + sVendorCode);

        sVendorCode = sVendorCode.trim();
        vendorCode = Integer.valueOf(sVendorCode).intValue();

      } catch (SQLException ex) {
        logErr("SQL Exception : " + ex.getMessage());
        throw new Fault("Set Up Failed", ex);
      }
    } catch (Exception e) {
      logErr("Setup Failed!");
      TestUtil.printStackTrace(e);
      throw new Fault("Setup Failed");
    }
  }

  /*
   * @testName: testSQLException01
   * 
   * @assertion_ids: JDBC:SPEC:6; JDBC:JAVADOC:68;
   * 
   * @test_Strategy: This method constructs a SQLException Object with no
   * arguments and for that object the reason,SQLState and ErrorCode are checked
   * for default values.
   *
   */
  public void testSQLException01() throws Fault {
    try {
      isThrown = false;
      throw new SQLException();
    } catch (SQLException b) {
      TestUtil.printStackTrace(b);

      isThrown = true;
      if ((b.getMessage() != null) || (b.getSQLState() != null)
          || (b.getErrorCode() != 0)) {
        msg.printTestError("SQLException() Constructor Fails",
            "Call to SQLException() Failed!");

      } else {
        msg.setMsg("SQLException() Constructor is implemented");
      }
      msg.printTestMsg();
    } catch (Exception ex) {
      msg.printError(ex, "Call to SQLException() Failed!");

    }
    if (!isThrown) {
      msg.printTestError("SQLException() not thrown",
          "Call to SQLException() Failed!");

    }
  }

  /*
   * @testName: testSQLException02
   * 
   * @assertion_ids: JDBC:SPEC:6; JDBC:JAVADOC:67;
   * 
   * @test_Strategy: This method constructs a SQLException Object with one
   * argument and for that object the SQLState, ErrorCode are checked for
   * default values.The reason is checked for whatever is been assigned while
   * creating the new instance.
   */

  public void testSQLException02() throws Fault {
    try {
      isThrown = false;
      throw new SQLException(sReason);
    } catch (SQLException b) {
      TestUtil.printStackTrace(b);

      isThrown = true;
      if ((b.getSQLState() != null) || (b.getErrorCode() != 0)) {
        msg.printTestError("SQLException(String) Constructor Fails",
            "Call to SQLException(String) Constructor Fails");

      } else {
        if ((!sReason.equals(b.getMessage()))) {
          msg.printTestError("SQLException(String) Constructor Fails",
              "Call to SQLException(String) Constructor Fails");

        } else {
          msg.setMsg("SQLException Constructor implemented");
        }
      }
      msg.printTestMsg();
    } catch (Exception ex) {
      msg.printError(ex, "Call to SQLException(String) Constructor Fails");

    }

    if (!isThrown) {
      msg.printTestError("SQLException(String) Constructor Fails",
          "Call to SQLException(String) Constructor Fails");

    }

  }

  /*
   * @testName: testSQLException03
   * 
   * @assertion_ids: JDBC:SPEC:6; JDBC:JAVADOC:66;
   * 
   * @test_Strategy: This method constructs a SQLException Object with two
   * arguments and for that object ErrorCode is checked for default values.The
   * reason and SQLState are checked for whatever is been assigned while
   * creating the new instance.
   */
  public void testSQLException03() throws Fault {
    try {
      isThrown = false;
      throw new SQLException(sReason, sSqlState);
    } catch (SQLException b) {
      TestUtil.printStackTrace(b);

      isThrown = true;
      if ((b.getErrorCode() != 0)) {
        msg.printTestError("SQLException(String,String) Constructor Fails",
            "Call to SQLException(String,String) Constructor Fails");

      } else {
        if ((!sSqlState.equals(b.getSQLState()))
            || (!sReason.equals(b.getMessage()))) {
          msg.printTestError("SQLException(String,String) Constructor Fails",
              "Call to SQLException(String,String) Constructor Fails");

        } else {
          msg.setMsg("Call to SQLException Passes");
        }
      }
      msg.printTestMsg();
    } catch (Exception ex) {
      msg.printError(ex,
          "Call to SQLException(String,String) Constructor Fails");

    }

    if (!isThrown) {
      msg.printTestError("SQLException(String,String) not thrown",
          "Call to SQLException(String,String) Constructor Fails");

    }
  }

  /*
   * @testName: testSQLException04
   * 
   * @assertion_ids: JDBC:SPEC:6; JDBC:JAVADOC:65;
   * 
   * @test_Strategy: This method constructs a SQLException Object with three
   * arguments .The reason,SQLState and Errorcode is checked for whatever is
   * been assigned while creating the new instance.
   */
  public void testSQLException04() throws Fault {
    try {
      isThrown = false;
      throw new SQLException(sReason, sSqlState, vendorCode);
    } catch (SQLException b) {
      TestUtil.printStackTrace(b);

      isThrown = true;
      if ((!sReason.equals(b.getMessage()))
          || (!sSqlState.equals(b.getSQLState()))
          || (!(vendorCode == b.getErrorCode()))) {
        msg.printTestError("SQLException(String,String,int) Constructor Fails",
            "Call to SQLException(String,String,int) Constructor Fails");

      } else {
        msg.setMsg(
            "SQLException(String,String,int) Constructor is implemented");
      }
      msg.printTestMsg();
    } catch (Exception ex) {
      msg.printError(ex,
          "Call to SQLException(String,String,int) Constructor is Failed!");

    }

    if (!isThrown) {
      msg.printTestError(
          "SQLException(String,String,int) Constructor not thrown",
          "Call to SQLException(String,String,int) Constructor is Failed!");

    }
  }

  /*
   * @testName: testGetErrorCode
   * 
   * @assertion_ids: JDBC:SPEC:6; JDBC:JAVADOC:70;
   * 
   * @testStartegy: The SQLException object is generated by executing an
   * incomplete SQL Statement and the getErrorCode() method of that object is
   * checked whether it returns an integer.
   */
  public void testGetErrorCode() throws Fault {
    try {
      String sErrorQuery = sqlp.getProperty("Error_Query");
      stmt.executeQuery(sErrorQuery);
    } catch (SQLException b) {
      TestUtil.printStackTrace(b);

      isThrown = true;
      maxVal = Integer
          .parseInt((rsSch.extractVal("Integer_Tab", 1, sqlp, conn)).trim());
      minVal = Integer
          .parseInt((rsSch.extractVal("Integer_Tab", 2, sqlp, conn)).trim());
      if ((b.getErrorCode() <= maxVal) && (b.getErrorCode() >= minVal)) {
        msg.setMsg("getErrorCode() method returns integer value");
      } else {
        msg.printTestError(
            "getErrorCode() Method does not returns integer value",
            "Call to getErrorCode() method fails");

      }
      msg.printTestMsg();
    } catch (Exception ex) {
      msg.printError(ex, "call to getErrorCode() method fails");

    }
    if (!isThrown) {
      msg.printTestError("getErrorCode() Method does not returns integer value",
          "Call to getErrorCode() method fails");

    }
  }

  /*
   * @testName: testGetSQLState
   * 
   * @assertion_ids: JDBC:SPEC:6; JDBC:JAVADOC:69;
   * 
   * @testStartegy: The SQLException object is generated by executing an
   * incomplete SQL Statement and the getSQLState() method of that object is
   * checked whether it is an instance of java.lang.String.
   */

  public void testGetSQLState() throws Fault {
    try {
      String sErrorQuery = sqlp.getProperty("Error_Query");
      stmt.executeQuery(sErrorQuery);
    } catch (SQLException b) {
      TestUtil.printStackTrace(b);

      isThrown = true;
      if ((b.getSQLState() instanceof java.lang.String)) {
        msg.setMsg("getSQLState() method returns String value");
      } else {
        msg.printTestError("getSQLState() method does not returns String Value",
            "call to getSQLState() method fails");

      }
      msg.printTestMsg();
    } catch (Exception ex) {
      msg.printError(ex, "call to getSQLState() method fails");

    }

    if (!isThrown) {
      msg.printTestError("SQLException not thrown",
          "call to getSQLState() method fails");

    }
  }

  /*
   * @testName: testGetNextException
   * 
   * @assertion_ids: JDBC:SPEC:6; JDBC:JAVADOC:71;
   * 
   * @test_Strategy: SQLException object is generated by executing an incomplete
   * SQL Statement and using setNextException method a SQLException object is
   * chained. This is checked using the getNextException method which should
   * return a instanceof SQLException object.
   */
  public void testGetNextException() throws Fault {
    try {
      String sErrorQuery = sqlp.getProperty("Error_Query");
      stmt.executeQuery(sErrorQuery);
    } catch (SQLException b) {
      TestUtil.printStackTrace(b);

      isThrown = true;
      b.setNextException(new SQLException(sReason, sSqlState, vendorCode));
      if ((b.getNextException() instanceof java.sql.SQLException)) {
        msg.setMsg("getNextException() method returns SQLException object");
        msg.setMsg("String is " + b.getMessage());
      } else {
        msg.printTestError(
            "getNextException() mMethod doesnot returns SQLException object",
            "call to getNextException() method fails");

      }
      msg.printTestMsg();
    } catch (Exception ex) {
      msg.printError(ex, "call to getNextException() method fails");

    }

    if (!isThrown) {
      msg.printTestError("SQLException not thrown",
          "call to getNextException() method fails");

    }
  }

  /*
   * @testName: testSetNextException
   * 
   * @assertion_ids: JDBC:SPEC:6; JDBC:JAVADOC:72;
   * 
   * @test_Strategy: SQLException object is obtained by executing a incomplete
   * SQLStatement and setNextException() method on the object will set a chain
   * of SQLException on that object which can be checked by using
   * getNextException() method.
   *
   */
  public void testSetNextException() throws Fault {
    try {
      String sErrorQuery = sqlp.getProperty("Error_Query");
      stmt.executeQuery(sErrorQuery);
    } catch (SQLException b) {
      TestUtil.printStackTrace(b);

      isThrown = true;
      b.setNextException(new SQLException(sReason, sSqlState, vendorCode));
      if ((b.getNextException() instanceof java.sql.SQLException)) {
        msg.setMsg("setNextException() method sets SQLException object");
        msg.setMsg("String is " + b.getMessage());
      } else {
        msg.printTestError(
            "setNextException() Method doesnot sets SQLException object",
            "call to setNextException() method fails");

      }
      msg.printTestMsg();
    } catch (Exception ex) {
      msg.printError(ex, "call to setNextException() method fails");

    }
    if (!isThrown) {
      msg.printTestError("SQLException not thrown",
          "call to setNextException() method fails");

    }
  }

  /* cleanup */
  public void cleanup() throws Fault {
    try {
      // conn.setAutoCommit(true);
      stmt.close();
      dbSch.destroyData(conn);
      // Close the database
      dbSch.dbUnConnect(conn);
      logMsg("Cleanup ok;");
    } catch (Exception e) {
      logErr("An error occurred while closing the database connection", e);
    }
  }
}
