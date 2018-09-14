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
 * @(#)stmtClient1.java	1.23 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.stmt.stmt1;

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
 * The stmtClient1 class tests methods of Statement interface using Sun's J2EE
 * Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class stmtClient1 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.stmt.stmt1";

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

  private JDBCTestMsg msg = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    stmtClient1 theTests = new stmtClient1();
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
        stmt = conn.createStatement();
        dbSch = new dbSchema();
        rsSch = new rsSchema();
        dbSch.createData(p, conn);
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
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:139; JDBC:JAVADOC:140;
   * 
   * @test_Strategy: Get a Statement object and call close() method and call
   * executeQuery() method to check and it should throw SQLException
   *
   */
  public void testClose() throws Fault {
    Statement statemt = null;
    boolean sqlExceptFlag = false;
    try {
      statemt = conn.createStatement();

      msg.setMsg("invoking the Close method");
      msg.setMsg("Calling close method");
      statemt.close();

      String sSelCoffee = sqlp.getProperty("SelCoffeeAll", "");
      msg.setMsg("Query String : " + sSelCoffee);

      try {
        rs = statemt.executeQuery(sSelCoffee);
      } catch (SQLException sql) {
        TestUtil.printStackTrace(sql);

        sqlExceptFlag = true;
      }
      if (sqlExceptFlag) {
        msg.setMsg("close method closes the Statement object");
      } else {
        msg.printTestError("close method does not close the Statement object",
            "Call to close method is Failed!");

      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to close is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to close is Failed!");

    }
  }

  /*
   * @testName: testExecute01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:163; JDBC:JAVADOC:164;
   * 
   * @test_Strategy: Call execute(String sql) of updating a row It should return
   * a boolean value and the value should be equal to false
   *
   */
  public void testExecute01() throws Fault {
    boolean executeFlag = false;
    try {
      String sSqlStmt = sqlp.getProperty("Upd_Coffee_Tab", "");
      msg.setMsg("Sql Statement to be executed  " + sSqlStmt);

      msg.setMsg("Calling execute method ");
      executeFlag = stmt.execute(sSqlStmt);

      if (!executeFlag) {
        msg.setMsg("execute method executes the SQL Statement ");
      } else {
        msg.printTestError("execute method does not execute the SQL Statement",
            "Call to execute is Failed!");

      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to execute is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to execute is Failed!");

    }
  }

  /*
   * @testName: testExecute02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:163; JDBC:JAVADOC:164;
   * 
   * @test_Strategy: Get a Statement object and call execute(String sql) of
   * selecting rows from the database It should return a boolean value and the
   * value should be equal to true
   *
   */
  public void testExecute02() throws Fault {
    boolean executeFlag = false;
    try {
      String sSqlStmt = sqlp.getProperty("Sel_Coffee_Tab", "");
      msg.setMsg("Sql Statement to be executed  " + sSqlStmt);

      msg.setMsg("Calling execute method ");
      executeFlag = stmt.execute(sSqlStmt);

      if (executeFlag) {
        msg.setMsg("execute method executes the SQL Statement ");
      } else {
        msg.printTestError("execute method does not execute the SQL Statement",
            "Call to execute is Failed!");

      }
      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to execute is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to execute is Failed!");

    }
  }

  /*
   * @testName: testExecuteQuery01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:135; JDBC:JAVADOC:136;
   * 
   * @test_Strategy: Get a Statement object and call executeQuery(String sql) to
   * select a row from the database It should return a ResultSet object
   *
   */
  public void testExecuteQuery01() throws Fault {
    ResultSet reSet = null;
    try {
      String sSqlStmt = sqlp.getProperty("SelCoffeeAll", "");
      msg.setMsg("SQL Statement to be executed  :  " + sSqlStmt);

      msg.setMsg("invoking the executeQuery");
      msg.setMsg("Calling executeQuery method ");
      reSet = stmt.executeQuery(sSqlStmt);

      if (reSet instanceof ResultSet) {
        msg.setMsg("executeQuery method returns a ResultSet object");
      } else {
        msg.printTestError(
            "executeQuery method does not return a ResultSet object",
            "Call to executeQuery is Failed!");

      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to executeQuery is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to executeQuery is Failed!");

    }
  }

  /*
   * @testName: testExecuteQuery02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:135; JDBC:JAVADOC:136;
   * 
   * @test_Strategy: Get a Statement object and call executeQuery(String sql) to
   * select a non-existent row from the database It should return a ResultSet
   * object which is empty and call ResultSet.next() method to check and it
   * should return a false
   *
   */
  public void testExecuteQuery02() throws Fault {
    ResultSet reSet = null;
    try {
      String sSqlStmt = sqlp.getProperty("SelCoffeeNull", "");
      msg.setMsg("SQL Statement to be executed  :  " + sSqlStmt);

      msg.setMsg("invoking the executeQuery");
      msg.setMsg("Calling executeQuery method ");
      reSet = stmt.executeQuery(sSqlStmt);

      if (!reSet.next()) {
        msg.setMsg(
            "executeQuery method returns an Empty ResultSet for Non-Existent row");
      } else {
        msg.printTestError(
            "executeQuery method does not return an Empty ResultSet for non-existent row",
            "Call to executeQuery is Failed!");

      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to executeQuery is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to executeQuery is Failed!");

    }
  }

  /*
   * @testName: testExecuteQuery03
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:135; JDBC:JAVADOC:136;
   * 
   *
   * @test_Strategy: Get a Statement object and call executeQuery(String sql) to
   * insert a row to the database It should throw SQLException
   *
   */
  public void testExecuteQuery03() throws Fault {
    ResultSet reSet = null;
    boolean sqlExceptFlag = false;
    try {
      String sSqlStmt = sqlp.getProperty("Ins_Coffee_Tab", "");
      msg.setMsg("SQL Statement to be executed  :  " + sSqlStmt);

      try {
        msg.setMsg("invoking the executeQuery with the Insert statement");
        msg.setMsg("Calling executeQuery method ");
        reSet = stmt.executeQuery(sSqlStmt);
      } catch (SQLException sql) {
        TestUtil.printStackTrace(sql);

        sqlExceptFlag = true;
      }
      if (!sqlExceptFlag) {
        msg.printTestError("executeQuery method executes an Insert Statement",
            "Call to executeQuery is Failed!");

      } else {
        msg.setMsg("executeQuery method does not execute an Insert Statement");
      }
      msg.printTestMsg();
    } catch (Exception e) {
      msg.printError(e, "Call to executeQuery is Failed!");

    }
  }

  /*
   * @testName: testExecuteUpdate01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:137; JDBC:JAVADOC:138;
   * 
   * @test_Strategy: Get a Statement object and call executeUpdate(String sql)
   * It should return an int value which is equal to row count
   */
  public void testExecuteUpdate01() throws Fault {
    int updCount = 0;
    int retRowCount = 0;
    try {
      String sSqlStmt = sqlp.getProperty("Upd_Coffee_Tab", "");
      msg.setMsg("Update String  : " + sSqlStmt);

      msg.setMsg("invoking the executeUpdate method");
      msg.setMsg("Calling executeUpdate method ");
      updCount = stmt.executeUpdate(sSqlStmt);

      msg.setMsg("to get the query which returns number of rows affected");
      String countQuery = sqlp.getProperty("Coffee_Updcount_Query", "");
      msg.setMsg("Query String :  " + countQuery);
      rs = stmt.executeQuery(countQuery);
      rs.next();
      retRowCount = rs.getInt(1);
      msg.setMsg("Number of rows in the table with the specified condition  "
          + retRowCount);

      if (updCount == retRowCount) {
        msg.setMsg("executeUpdate executes the SQL Statement ");
      } else {
        msg.printTestError("executeUpdate does not execute the SQL Statement ",
            "Call to executeUpdate is Failed!");

      }
      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to executeUpdate is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to executeUpdate is Failed!");

    }
  }

  /*
   * @testName: testExecuteUpdate03
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:137; JDBC:JAVADOC:138;
   * 
   * @test_Strategy: Get a Statement object and call executeUpdate(String sql)
   * for selecting row from the table It should throw a SQL Exception
   *
   */
  public void testExecuteUpdate03() throws Fault {
    boolean sqlExceptFlag = false;
    int updCount = 0;
    try {
      String sSqlStmt = sqlp.getProperty("Sel_Coffee_Tab", "");
      msg.setMsg("SQL String of non - existent row  :  " + sSqlStmt);

      try {
        msg.setMsg("Calling executeUpdate method ");
        updCount = stmt.executeUpdate(sSqlStmt);
      } catch (SQLException sqle) {
        TestUtil.printStackTrace(sqle);

        sqlExceptFlag = true;
      }

      if (sqlExceptFlag) {
        msg.setMsg(
            "executeUpdate does not execute the SQL statement on non-existent row");
      } else {
        msg.printTestError(
            "executeUpdate executes the SQL statement on non-existent row",
            "Call to executeUpdate is Failed!");

      }
      msg.printTestMsg();

    } catch (Exception e) {
      msg.printError(e, "Call to executeUpdate is Failed!");

    }
  }

  /*
   * @testName: testGetFetchDirection
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:173; JDBC:JAVADOC:174;
   * JDBC:JAVADOC:356;
   * 
   * @test_Strategy: Get a Statement object and call the getFetchDirection()
   * method It should return a int value and the value should be equal to any of
   * the values FETCH_FORWARD or FETCH_REVERSE or FETCH_UNKNOWN
   *
   */
  public void testGetFetchDirection() throws Fault {
    int fetchDirVal = 0;
    try {
      // invoke on the getFetchDirection
      msg.setMsg("Calling getFetchDirection method ");
      fetchDirVal = stmt.getFetchDirection();

      if (fetchDirVal == ResultSet.FETCH_FORWARD) {
        msg.setMsg("getFetchDirection method returns ResultSet.FETCH_FORWARD ");
      } else if (fetchDirVal == ResultSet.FETCH_REVERSE) {
        msg.setMsg("getFetchDirection method returns ResultSet.FETCH_REVERSE");
      } else if (fetchDirVal == ResultSet.FETCH_UNKNOWN) {
        msg.setMsg("getFetchDirection method returns ResultSet.FETCH_UNKNOWN");
      } else {
        msg.printTestError(" getFetchDirection method returns a invalid value",
            "Call to getFetchDirection is Failed");

      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getFetchDirection is Failed");

    } catch (Exception e) {
      msg.printError(e, "Call to getFetchDirection is Failed");

    }
  }

  /*
   * @testName: testGetFetchSize
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:177; JDBC:JAVADOC:178;
   * 
   * @test_Strategy: Get a ResultSet object and call the getFetchSize() method
   * It should return a int value
   *
   */
  public void testGetFetchSize() throws Fault {
    try {
      // invoke on the getFetchSize
      msg.setMsg("Calling getFetchSize on Statement");
      int fetchSizeVal = stmt.getFetchSize();

      if (fetchSizeVal >= 0) {
        msg.setMsg("getFetchSize method returns :" + fetchSizeVal);
      } else {
        msg.printTestError(" getFetchSize method returns a invalid value",
            "Call to getFetchSize is Failed!");

      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getFetchSize is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getFetchSize is Failed!");

    }
  }

  /*
   * @testName: testGetMaxFieldSize
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:141; JDBC:JAVADOC:142;
   * 
   * @test_Strategy: Get a Statement object and call the getMaxFieldSize()
   * method It should return a int value
   *
   */
  public void testGetMaxFieldSize() throws Fault {
    try {
      // invoke on the getMaxFieldSize
      msg.setMsg("Calling getMaxFieldSize on Statement");
      int maxFieldSizeVal = stmt.getMaxFieldSize();

      if (maxFieldSizeVal >= 0) {
        msg.setMsg("getMaxFieldSize method returns :" + maxFieldSizeVal);
      } else {
        msg.printTestError(" getMaxFieldSize method returns a invalid value",
            "Call to getMaxFieldSize is Failed!");

      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getMaxFieldSize is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getMaxFieldSize is Failed!");

    }
  }

  /*
   * @testName: testGetMaxRows
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:145; JDBC:JAVADOC:146;
   * 
   * @test_Strategy: Get a Statement object and call the getMaxRows() method It
   * should return a int value
   *
   */
  public void testGetMaxRows() throws Fault {
    try {
      // invoke on the getFetchSize
      msg.setMsg("Calling getMaxRows on Statement");
      int maxRowsVal = stmt.getMaxRows();

      if (maxRowsVal >= 0) {
        msg.setMsg("getMaxRows method returns :" + maxRowsVal);
      } else {
        msg.printTestError(" getMaxRows method returns a invalid value",
            "Call to getMaxRows is Failed!");

      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getMaxRows is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getMaxRows is Failed!");

    }
  }

  /*
   * @testName: testGetMoreResults01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:169; JDBC:JAVADOC:170;
   * 
   * @test_Strategy: Get a Statement object and call the execute() method for
   * selecting a row and call getMoreResults() method It should return a boolean
   * value
   *
   */
  public void testGetMoreResults01() throws Fault {
    try {
      String sSqlStmt = sqlp.getProperty("SelCoffeeAll", "");
      msg.setMsg("Query String : " + sSqlStmt);

      stmt.executeQuery(sSqlStmt);

      msg.setMsg("invoke on the getMoreResults");
      msg.setMsg("Calling getMoreResults on Statement");
      boolean moreResVal = stmt.getMoreResults();

      if ((moreResVal == true) || (moreResVal == false)) {
        msg.setMsg("getMoreResults method returns :" + moreResVal);
      } else {
        msg.printTestError(" getMoreResults method returns a invalid value",
            "Call to getMoreResults is Failed!");

      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getMoreResults is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getMoreResults is Failed!");

    }
  }

  /*
   * @testName: testGetMoreResults02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:169; JDBC:JAVADOC:170;
   * 
   * @test_Strategy: Get a Statement object and call the execute() method for
   * selecting a non-existent row and call getMoreResults() method It should
   * return a boolean value and the value should be equal to false
   *
   */
  public void testGetMoreResults02() throws Fault {
    try {
      String sSqlStmt = sqlp.getProperty("SelCoffeeNull", "");
      msg.setMsg("Query String : " + sSqlStmt);

      stmt.executeQuery(sSqlStmt);

      // invoke on the getMoreResults
      msg.setMsg("Calling getMoreResults on Statement");
      boolean moreResVal = stmt.getMoreResults();

      if (!moreResVal) {
        msg.setMsg("getMoreResults method returns :" + moreResVal);
      } else {
        msg.printTestError(" getMoreResults method returns a invalid value",
            "Call to getMoreResults is Failed!");

      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getMoreResults is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getMoreResults is Failed!");

    }
  }

  /*
   * @testName: testGetMoreResults03
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:169; JDBC:JAVADOC:170;
   * 
   * @test_Strategy: Get a Statement object and call the execute() method for
   * updating a row and call getMoreResults() method It should return a boolean
   * value and the value should be equal to false
   *
   */
  public void testGetMoreResults03() throws Fault {
    try {
      String sSqlStmt = sqlp.getProperty("Upd_Coffee_Tab", "");
      msg.setMsg("Query String : " + sSqlStmt);

      stmt.executeUpdate(sSqlStmt);

      msg.setMsg("invoke on the getMoreResults");
      msg.setMsg("Calling getMoreResults on Statement");
      boolean moreResVal = stmt.getMoreResults();

      if (!moreResVal) {
        msg.setMsg("getMoreResults method returns :" + moreResVal);
      } else {
        msg.printTestError(" getMoreResults method returns a invalid value",
            "Call to getMoreResults is Failed!");

      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getMoreResults is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getMoreResults is Failed!");

    }
  }

  /*
   * @testName: testGetQueryTimeout
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:151; JDBC:JAVADOC:152;
   * 
   * @test_Strategy: Get a Statement object and call getQueryTimeout() method It
   * should return a int value
   *
   */
  public void testGetQueryTimeout() throws Fault {
    int queryTimeout = 0;
    try {
      // invoking getQueryTimeout method
      msg.setMsg("Calling getQueryTimeout on Statement");
      queryTimeout = stmt.getQueryTimeout();

      if (queryTimeout >= 0) {
        msg.setMsg("getQueryTimeout method returns :" + queryTimeout);
      } else {
        msg.printTestError(" getQueryTimeout method returns a invalid value",
            "Call to getQueryTimeout is Failed!");

      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getQueryTimeout is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getQueryTimeout is Failed!");

    }
  }

  /*
   * @testName: testGetResultSet01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:165; JDBC:JAVADOC:166;
   * 
   * @test_Strategy: Get a Statement object and call execute() method for
   * selecting a row and call getResultSet() method It should return a ResultSet
   * object
   *
   */
  public void testGetResultSet01() throws Fault {
    ResultSet retResSet = null;
    try {

      String sSqlStmt = sqlp.getProperty("SelCoffeeAll", "");
      msg.setMsg("Query String :  " + sSqlStmt);

      // invoking getResultSet method
      msg.setMsg("Calling getResultSet on Statement");
      stmt.executeQuery(sSqlStmt);
      retResSet = stmt.getResultSet();

      if (retResSet instanceof ResultSet) {
        msg.setMsg("getResultSet method returns a ResultSet object ");
      } else {
        msg.printTestError(
            " getResultSet method does not return a ResultSet object",
            "Call to getResultSet is Failed!");

      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getResultSet is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getResultSet is Failed!");

    }
  }

  /* cleanup */
  public void cleanup() throws Fault {
    try {
      if (rs != null) {
        rs.close();
      }
      dbSch.destroyData(conn);
      // Close the Statement object
      stmt.close();
      // close the Database
      dbSch.dbUnConnect(conn);

      logMsg("Cleanup ok;");
    } catch (Exception e) {
      logErr("An error occurred while closing the database connection", e);
    }
  }

}
