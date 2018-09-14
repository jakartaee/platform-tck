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
 * %W% %E%
 */

package com.sun.ts.tests.jdbc.ee.prepStmt.prepStmt1;

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
 * The prepStmtClient1 class tests methods of DatabaseMetaData interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class prepStmtClient1 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.prepStmt.prepStmt1";

  // Naming specific member variables
  private TSNamingContextInterface jc = null;

  // Harness requirements

  private transient Connection conn = null;

  private ResultSet rs = null;

  private Statement stmt = null;

  private DataSource ds1 = null;

  private dbSchema dbSch = null;

  private rsSchema rsSch = null;

  private csSchema csSch = null;

  private String drManager = null;

  private Properties sqlp = null;

  private PreparedStatement pstmt = null;

  private JDBCTestMsg msg = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    prepStmtClient1 theTests = new prepStmtClient1();
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

        rsSch = new rsSchema();
        dbSch = new dbSchema();
        dbSch.createData(p, conn);
        csSch = new csSchema();
        msg = new JDBCTestMsg();

        stmt = conn.createStatement();

      } catch (SQLException ex) {
        logErr("SQL Exception" + ex.getMessage(), ex);
      }
    } catch (Exception e) {
      logErr("Setup Failed!");
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * @testName: testGetMetaData
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:712;
   * JDBC:JAVADOC:713; JDBC:JAVADOC:1143; JDBC:JAVADOC:1144; JavaEE:SPEC:186;
   * JavaEE:SPEC:186.2;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Execute the getMetaData() method and get the number of columns
   * using getColumnCount() method of ResultSetMetaData.Execute a query using
   * executeQuery() method and get the number of columns. Both the values should
   * be equal or it should throw an SQL exception.
   * 
   */

  public void testGetMetaData() throws Fault {
    ResultSetMetaData rsmd = null;
    ResultSetMetaData rsmdPrep = null;

    boolean statflag = false;

    try {
      String sPrepStmt = sqlp.getProperty("SelCoffeeAll", "");

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);

      try {
        msg.setMsg("Getting MetaData from PreparedStatement");
        rsmdPrep = pstmt.getMetaData();

      } catch (SQLException sqe) {
        statflag = true;
        msg.setMsg(
            "#####PreparedStatement.getMetaData() not supported and received the expected SQLException.");
      }

      if (statflag == false) {

        try {
          msg.setMsg("Executing Query : " + sPrepStmt);
          rs = stmt.executeQuery(sPrepStmt);

          msg.setMsg("Getting MetaData from ResultSet");
          rsmd = rs.getMetaData();

        } catch (SQLException sq) {
          msg.printSQLError(sq, "Call to executeQuery method is Failed!");

        }
        msg.addOutputMsg("" + rsmdPrep.getColumnCount(),
            "" + rsmd.getColumnCount());

        if (rsmdPrep.getColumnCount() == rsmd.getColumnCount())
          msg.setMsg("Call to getMetaData Method is Passed");
        else {
          msg.printTestError(
              "getMetaData Method does not return a valid ResultSetMetaData",
              "Call to getMetaData Method is Failed!");

        }
      } else
        msg.setMsg("Call to getMetaData Method is Passed");
      msg.printTestMsg();
      msg.printOutputMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getMetaData is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getMetaData is Failed!");

    } finally {
      try {
        if (rs != null)
          rs.close();
        pstmt.close();
      } catch (Exception e) {
      }

    }
  }

  /*
   * @testName: testClearParameters
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:690;
   * JDBC:JAVADOC:691; JDBC:JAVADOC:1143; JDBC:JAVADOC:1144; JavaEE:SPEC:186;
   * 
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Set the value for the IN parameter of the Prepared Statement
   * object. Call the clearParameters() method.Call the executeQuery() method to
   * check if the call to clearParameters() clears the IN parameter set by the
   * Prepared Statement object.
   */

  public void testClearParameters() throws Fault {
    ResultSet reSet = null;
    boolean sqlexcflag = false;
    try {
      String sPrepStmt = sqlp.getProperty("CoffeeTab_Query", "");
      msg.setMsg("Prepared Statement String :" + sPrepStmt);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setInt(1, 1);
      pstmt.clearParameters();
      try {
        reSet = pstmt.executeQuery();
      } catch (SQLException sqe) {
        TestUtil.printStackTrace(sqe);

        sqlexcflag = true;
      }

      if (sqlexcflag) {
        msg.setMsg("clearParameters Method clears the current Parameters ");
      } else {
        msg.printTestError(
            "clearParameters Method does not clear the current Parameters",
            "Call to clearParameters Method is Failed!");

      }
      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to clearParameters is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to clearParameters is Failed!");

    } finally {
      try {
        if (reSet != null)
          reSet.close();
        pstmt.close();
      } catch (Exception e) {
      }

    }
  }

  /*
   * @testName: testExecute01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:698; JDBC:JAVADOC:699;
   * JDBC:JAVADOC:1143; JDBC:JAVADOC:1144; JavaEE:SPEC:182; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Set the value for the IN parameter of the Prepared Statement
   * object. Execute the precompiled SQL Statement of deleting a row. It should
   * return a boolean value and the value should be equal to false. (See JDK
   * 1.2.2 API documentation)
   *
   */

  public void testExecute01() throws Fault {
    boolean retValue;
    try {
      String sPrepStmt = sqlp.getProperty("CoffeeTab_Delete", "");
      msg.setMsg("Prepared Statement String :" + sPrepStmt);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setInt(1, 1);
      retValue = pstmt.execute();
      msg.addOutputMsg("false", "" + retValue);
      if (!retValue) {
        msg.setMsg("execute Method executes the SQL Statement ");
      } else {
        msg.printTestError("execute Method does not execute the SQL Statment",
            "Call to execute Method is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to execute is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to execute is Failed!");

    } finally {
      try {
        pstmt.close();
      } catch (Exception e) {
      }

    }

  }

  /*
   * @testName: testExecute03
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:698; JDBC:JAVADOC:699;
   * JDBC:JAVADOC:1143; JDBC:JAVADOC:1144; JavaEE:SPEC:182; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Execute the precompiled SQL Statement by calling execute() method
   * without setting the parameters.An SQL Exception must be thrown. (See JDK
   * 1.2.2 API documentation)
   *
   */

  public void testExecute03() throws Fault {
    boolean retValue;
    boolean sqlexcflag = false;
    try {
      String sPrepStmt = sqlp.getProperty("CoffeeTab_Delete", "");
      msg.setMsg("Prepared Statement String :" + sPrepStmt);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      try {
        retValue = pstmt.execute();
      } catch (SQLException sqe) {
        TestUtil.printStackTrace(sqe);

        sqlexcflag = true;
      }
      if (sqlexcflag) {
        msg.setMsg("execute Method executes the SQL Statement ");
      } else {
        msg.printTestError("execute Method does not execute the SQL Statment",
            "Call to execute Method is Failed!");

      }
      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to execute is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to execute is Failed!");

    } finally {
      try {
        pstmt.close();
      } catch (Exception e) {
      }

    }
  }

  /*
   * @testName: testExecuteQuery01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:652; JDBC:JAVADOC:653;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Set the value for the IN parameter of the Prepared Statement
   * object. Execute the precompiled SQL Statement by calling executeQuery()
   * method. It should return a ResultSet object.
   * 
   */

  public void testExecuteQuery01() throws Fault {
    ResultSet reSet = null;
    try {
      String sPrepStmt = sqlp.getProperty("CoffeeTab_Query", "");
      msg.setMsg("Prepared Statement String :" + sPrepStmt);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setInt(1, 1);
      reSet = pstmt.executeQuery();

      if (reSet != null) {
        msg.setMsg("executeQuery Method executes the SQL Statement ");
      } else {
        msg.printTestError(
            "executeQuery Method does not execute the SQL Statment",
            "Call to executeQuery Method is Failed!");

      }
      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to executeQuery is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to executeQuery is Failed!");

    } finally {
      try {
        if (reSet != null)
          reSet.close();
        pstmt.close();
      } catch (Exception e) {
      }

    }
  }

  /*
   * @testName: testExecuteQuery02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:652; JDBC:JAVADOC:653;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Set the value for the IN parameter of the Prepared Statement
   * object. Execute the precompiled SQL Statement by calling executeQuery()
   * method with a non existent row. A call to ResultSet.next() should return a
   * false value.
   */

  public void testExecuteQuery02() throws Fault {
    ResultSet reSet = null;

    try {
      String sPrepStmt = sqlp.getProperty("CoffeeTab_Query", "");
      msg.setMsg("Prepared Statement String :" + sPrepStmt);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setInt(1, 0);
      reSet = pstmt.executeQuery();

      if (!reSet.next()) {
        msg.setMsg("executeQuery Method executes the SQL Statement ");
      } else {
        msg.printTestError(
            "executeQuery Method does not execute the SQL Statment",
            "Call to executeQuery Method is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to executeQuery is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to executeQuery is Failed!");

    } finally {
      try {
        if (reSet != null)
          reSet.close();
        pstmt.close();
      } catch (Exception e) {
      }

    }
  }

  /*
   * @testName: testExecuteQuery03
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:652; JDBC:JAVADOC:653;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Execute the precompiled SQL Statement by calling executeQuery()
   * method without setting the parameters. It should throw a SQL Exception.
   */

  public void testExecuteQuery03() throws Fault {
    ResultSet reSet = null;
    boolean sqlexcflag = false;
    try {
      String sPrepStmt = sqlp.getProperty("CoffeeTab_Query", "");
      msg.setMsg("Prepared Statement String :" + sPrepStmt);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      try {
        reSet = pstmt.executeQuery();
      } catch (SQLException sqe) {
        TestUtil.printStackTrace(sqe);

        sqlexcflag = true;
      }

      if (sqlexcflag) {
        msg.setMsg("executeQuery Method executes the SQL Statement ");
      } else {
        msg.printTestError(
            "executeQuery Method does not execute the SQL Statment",
            "Call to executeQuery Method is Failed!");

      }
      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to executeQuery is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to executeQuery is Failed!");

    } finally {
      try {
        if (reSet != null)
          reSet.close();
        pstmt.close();
      } catch (Exception e) {
      }

    }
  }

  /*
   * @testName: testExecuteUpdate01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:654; JDBC:JAVADOC:655;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Set the value for the IN parameter of the PreparedStatement
   * object. Execute the precompiled SQL Statement by calling executeUpdate()
   * method. It should return an integer value indicating the number of rows
   * that were affected. (The value could be zero if zero rows are affected).
   */

  public void testExecuteUpdate01() throws Fault {
    int retValue;
    try {
      String sPrepStmt = sqlp.getProperty("CoffeeTab_Delete", "");
      msg.setMsg("Prepared Statement String :" + sPrepStmt);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setInt(1, 1);
      retValue = pstmt.executeUpdate();

      if (retValue >= 0) {
        msg.setMsg("executeUpdate Method executes the SQL Statement ");
      } else {
        msg.printTestError(
            "executeUpdate Method does not execute the SQL Statment",
            "Call to executUpdate Method is Failed!");

      }
      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to executeUpdate is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to executeUpdate is Failed!");

    } finally {
      try {
        pstmt.close();
      } catch (Exception e) {
      }

    }
  }

  /*
   * @testName: testExecuteUpdate02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:654; JDBC:JAVADOC:655;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Set the value for the IN parameter of the Prepared Statement
   * object. Execute the precompiled SQL Statement by calling executeUpdate()
   * method with a non existent row. It should return an Integer value.
   */

  public void testExecuteUpdate02() throws Fault {
    int retValue;

    try {
      String sPrepStmt = sqlp.getProperty("CoffeeTab_Delete", "");
      msg.setMsg("Prepared Statement String :" + sPrepStmt);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setInt(1, 0);
      retValue = pstmt.executeUpdate();

      if (retValue >= 0) {
        msg.setMsg("executeUpdate Method executes the SQL Statement ");
      } else {
        msg.printTestError(
            "executeUpdate Method does not execute the SQL Statment",
            "Call to executeUpdate is Failed!");

      }
      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to executeUpdate is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to executeUpdate is Failed!");

    } finally {
      try {
        pstmt.close();
      } catch (Exception e) {
      }

    }
  }

  /*
   * @testName: testExecuteUpdate03
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:654; JDBC:JAVADOC:655;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Execute the precompiled SQL Statement without setting the IN
   * parameter. It should throw an SQL exception.
   */

  public void testExecuteUpdate03() throws Fault {
    int retValue;
    boolean sqlexcflag = false;
    try {
      String sPrepStmt = sqlp.getProperty("CoffeeTab_Delete", "");
      msg.setMsg("Prepared Statement String :" + sPrepStmt);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      try {
        retValue = pstmt.executeUpdate();
      } catch (SQLException sqe) {
        TestUtil.printStackTrace(sqe);

        sqlexcflag = true;
      }

      if (sqlexcflag) {
        msg.setMsg("executeUpdate Method executes the SQL Statement ");
      } else {
        msg.printTestError(
            "executeUpdate Method does not execute the SQL Statment",
            "Call to executeUpdate is Failed!");

      }
      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to executeUpdate is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to executeUpdate is Failed!");

    } finally {
      try {
        pstmt.close();
      } catch (Exception e) {
      }

    }
  }

  /*
   * @testName: testSetBigDecimal01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:672; JDBC:JAVADOC:673;
   * JDBC:JAVADOC:454; JDBC:JAVADOC:455; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Execute the precompiled.SQL Statement by calling the
   * setBigDecimal(int parameterindex, BigDecimal x) method for updating the
   * value of column MIN_VAL in Numeric_Tab.Check first the return value of
   * executeUpdate() method used is equal to 1. Call the
   * ResultSet.getBigDecimal(int columnIndex)method. Check if returns the
   * BigDecimal Value that has been set.
   */

  public void testSetBigDecimal01() throws Fault {
    BigDecimal rBigDecimalVal = null;
    BigDecimal minBigDecimalVal = null;
    try {
      rsSch.createTab("Numeric_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Numeric_Tab_Min_Update", "");
      msg.setMsg("Prepared Statement String :" + sPrepStmt);
      msg.setMsg("extract the Value of Minimum value to be Updated");
      String sminBigDecimalVal = rsSch.extractVal("Numeric_Tab", 2, sqlp, conn);

      // to convert the String into BigDecimal value
      minBigDecimalVal = new BigDecimal(sminBigDecimalVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setBigDecimal(1, minBigDecimalVal);
      int retVal = pstmt.executeUpdate();

      msg.addOutputMsg("1", "" + retVal);
      if (retVal != 1) {
        msg.printTestError(
            "Minimum Value not being updated in the Min_Val column",
            "Call to executeUpdate is Failed!");

      }
      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Min_Val_Query = sqlp.getProperty("Numeric_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();
      rBigDecimalVal = rs.getBigDecimal(1);

      msg.addOutputMsg(" " + minBigDecimalVal, "" + rBigDecimalVal);
      if (rBigDecimalVal.compareTo(minBigDecimalVal) == 0) {
        msg.setMsg(
            "setBigDecimal Method sets the designated parameter to a BigDecimal value ");
      } else {
        msg.printTestError(
            "setBigDecimal Method does not set the designated parameter to a BigDecimal value ",
            "Call to setBigDecimal Method is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setBigDecimal Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setBigDecimal Method is Failed!");

    } finally {
      try {
        if (rs != null)
          rs.close();
        pstmt.close();
        rsSch.dropTab("Numeric_Tab", conn);
      } catch (Exception e) {
      }

    }

  }

  /*
   * @testName: testSetBigDecimal02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:672; JDBC:JAVADOC:673;
   * JDBC:JAVADOC:454; JDBC:JAVADOC:455; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Execute the precompiled SQL Statement by calling the
   * setBigDecimal(int parameterindex, BigDecimal x) method for updating the
   * value of column NULL_VAL in Numeric_Tab. Call the
   * ResultSet.getBigDecimal(int columnIndex) method. Check if returns the
   * BigDecimal Value that has been set.
   */
  public void testSetBigDecimal02() throws Fault {
    BigDecimal maxBigDecimalVal = null;
    BigDecimal rBigDecimalVal = null;
    try {
      rsSch.createTab("Numeric_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Numeric_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String :" + sPrepStmt);
      msg.setMsg("extract the Value of Maximum value to be Updated ");
      String smaxBigDecimalVal = rsSch.extractVal("Numeric_Tab", 1, sqlp, conn);

      // to convert the String into BigDecimal value
      maxBigDecimalVal = new BigDecimal(smaxBigDecimalVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setBigDecimal(1, maxBigDecimalVal);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Numeric_Query_Null", "");
      msg.setMsg(Null_Val_Query);

      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rBigDecimalVal = rs.getBigDecimal(1);

      msg.addOutputMsg(" " + maxBigDecimalVal, "" + rBigDecimalVal);
      if (rBigDecimalVal.compareTo(maxBigDecimalVal) == 0) {
        msg.setMsg(
            "setBigDecimal Method sets the designated parameter to a BigDecimal value ");
      } else {
        msg.printTestError(
            "setBigDecimal Method does not set the designated parameter to a BigDecimal value ",
            "Call to setBigDecimal Method is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setBigDecimal Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setBigDecimal Method is Failed!");

    } finally {
      try {
        if (rs != null)
          rs.close();
        pstmt.close();
        rsSch.dropTab("Numeric_Tab", conn);
      } catch (Exception e) {
      }

    }
  }

  /*
   * @testName: testSetBoolean01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:658; JDBC:JAVADOC:659;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Execute the precompiled SQL Statement by calling the
   * setBoolean(int parameterIndex, boolean x) to set MAX_VAL column of Bit_tab
   * with the MIN_VAL of Bit_Tab. Call the ResultSet.getBoolean(int columnIndex)
   * method to check if it returns the boolean Value that has been set.
   *
   */
  public void testSetBoolean01() throws Fault {
    boolean bminBooleanVal;
    boolean rBooleanVal;
    try {
      rsSch.createTab("Bit_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Bit_Tab_Max_Update", "");
      msg.setMsg("Prepared Statement String :" + sPrepStmt);
      msg.setMsg("extract the Value of Minimum value to be Updated");

      bminBooleanVal = rsSch.extractValAsBoolVal("Bit_Tab", 2, sqlp, conn);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setBoolean(1, bminBooleanVal);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Max_Val_Query = sqlp.getProperty("Bit_Query_Max", "");
      msg.setMsg(Max_Val_Query);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();
      rBooleanVal = rs.getBoolean(1);

      msg.addOutputMsg(" " + bminBooleanVal, "" + rBooleanVal);
      if (rBooleanVal == bminBooleanVal) {
        msg.setMsg(
            "setBoolean Method sets the designated parameter to a Boolean value ");
      } else {
        msg.printTestError(
            "setBoolean Method does not set the designated parameter to a Boolean value ",
            "Call to setBoolean Method is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setBoolean is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setBoolean is Failed!");

    } finally {
      try {
        if (rs != null)
          rs.close();
        pstmt.close();
        rsSch.dropTab("Bit_Tab", conn);
      } catch (Exception e) {
      }

    }
  }

  /*
   * @testName: testSetBoolean02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:658; JDBC:JAVADOC:659;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Execute the precompiled SQL Statement by calling the method
   * setBoolean(int parameterIndex, boolean x) to set NULL_VAL column of Bit_tab
   * with the Max_Val of Bit_Tab. Call the ResultSet.getBoolean(int columnIndex)
   * method to check if it returns the boolean Value that has been set.
   */
  public void testSetBoolean02() throws Fault {
    boolean bmaxBooleanVal;
    boolean rBooleanVal;
    try {
      rsSch.createTab("Bit_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Bit_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String :" + sPrepStmt);
      msg.setMsg("extract the Value of Maximum value to be Updated");

      bmaxBooleanVal = rsSch.extractValAsBoolVal("Bit_Tab", 1, sqlp, conn);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setBoolean(1, bmaxBooleanVal);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Bit_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      rBooleanVal = rs.getBoolean(1);

      msg.addOutputMsg(" " + bmaxBooleanVal, "" + rBooleanVal);
      if (rBooleanVal == bmaxBooleanVal) {
        msg.setMsg(
            "setBoolean Method sets the designated parameter to a Boolean value ");
      } else {
        msg.printTestError(
            "setBoolean Method does not set the designated parameter to a Boolean value ",
            "Call to setBoolean is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setBoolean is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setBoolean is Failed!");

    } finally {
      try {
        if (rs != null)
          rs.close();
        pstmt.close();
        rsSch.dropTab("Bit_Tab", conn);
      } catch (Exception e) {
      }

    }
  }

  /*
   * @testName: testSetByte01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:660; JDBC:JAVADOC:661;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Execute the precompiled SQL Statement by calling the setByte(int
   * parameterindex, byte x) method for updating MAX_VAL column of Tinyint_Tab.
   * Call the ResultSet.getByte(int columnIndex) method to check if it returns
   * the byte Value that has been set.
   */
  public void testSetByte01() throws Fault {
    byte bminByteVal = 0;
    byte rByteVal = 0;
    try {
      rsSch.createTab("Tinyint_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Tinyint_Tab_Max_Update", "");
      msg.setMsg("Prepared Statement String :" + sPrepStmt);
      msg.setMsg("extract the Value of Minimum value to be Updated ");
      String sminByteVal = rsSch.extractVal("Tinyint_Tab", 2, sqlp, conn);

      // to convert the String into byte value
      bminByteVal = Byte.parseByte(sminByteVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setByte(1, bminByteVal);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Max_Val_Query = sqlp.getProperty("Tinyint_Query_Max", "");
      msg.setMsg(Max_Val_Query);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();
      rByteVal = rs.getByte(1);

      msg.addOutputMsg(" " + bminByteVal, "" + rByteVal);
      if (rByteVal == bminByteVal) {
        msg.setMsg(
            "setByte Method sets the designated parameter to a Byte value ");
      } else {
        msg.printTestError(
            "setByte Method does not set the designated parameter to a Byte value ",
            "Call to setByte Method is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setByte Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setByte Method is Failed!");

    } finally {
      try {
        if (rs != null)
          rs.close();
        pstmt.close();
        rsSch.dropTab("Tinyint_Tab", conn);
      } catch (Exception e) {
      }

    }
  }

  /*
   * @testName: testSetByte02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:660; JDBC:JAVADOC:661;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Execute the precompiled SQL Statement by calling the setByte(int
   * parameterindex, byte x) method for updating NULL_VAL column of Tinyint_Tab.
   * Call the ResultSet.getByte(int columnIndex) method,to check if it returns
   * the byte Value that has been set.
   */

  public void testSetByte02() throws Fault {
    byte bmaxByteVal = 0;
    byte rByteVal = 0;
    try {
      rsSch.createTab("Tinyint_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Tinyint_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String :" + sPrepStmt);

      msg.setMsg("extract the Value of Maximum value to be Updated");
      String smaxByteVal = rsSch.extractVal("Tinyint_Tab", 1, sqlp, conn);

      // to convert the String into Byte value
      bmaxByteVal = Byte.parseByte(smaxByteVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setByte(1, bmaxByteVal);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Tinyint_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      rByteVal = rs.getByte(1);

      msg.addOutputMsg(" " + bmaxByteVal, "" + rByteVal);
      if (rByteVal == bmaxByteVal) {
        msg.setMsg(
            "setByte Method sets the designated parameter to a Byte value ");
      } else {
        msg.printTestError(
            "setByte Method does not set the designated parameter to a Byte value ",
            "Call to setByte Method is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setByte is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setByte is Failed!");

    } finally {
      try {
        if (rs != null)
          rs.close();
        pstmt.close();
        rsSch.dropTab("Tinyint_Tab", conn);
      } catch (Exception e) {
      }

    }
  }

  /*
   * @testName: testSetFloat01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:668; JDBC:JAVADOC:669;
   * JavaEE:SPEC:186;
   * 
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database execute the precompiled SQL Statement by calling the setFloat(int
   * parameterindex, float x) method for updating the MAX_VAL column of
   * Float_Tab. Call the ResultSet.getFloat(int columnIndex) method to check if
   * it returns the float Value that has been set.
   *
   */

  public void testSetFloat01() throws Fault {
    float rFloatVal = 0;
    float minFloatVal = 0;
    try {
      rsSch.createTab("Float_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Float_Tab_Max_Update", "");
      msg.setMsg("Prepared Statement String :" + sPrepStmt);
      msg.setMsg("extract the Value of Minimum value to be Updated");
      String sminFloatVal = rsSch.extractVal("Float_Tab", 2, sqlp, conn);

      // to convert the String into float value
      minFloatVal = Float.parseFloat(sminFloatVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setFloat(1, minFloatVal);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Max_Val_Query = sqlp.getProperty("Float_Query_Max", "");
      msg.setMsg(Max_Val_Query);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();
      rFloatVal = rs.getFloat(1);

      msg.addOutputMsg(" " + minFloatVal, "" + rFloatVal);
      if (rFloatVal == minFloatVal) {
        msg.setMsg(
            "setFloat Method sets the designated parameter to a float value ");
      } else {
        msg.printTestError(
            "setFloat Method does not set the designated parameter to a float value ",
            "Call to setFloat Method is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setFloat Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setFloat Method is Failed!");

    } finally {
      try {
        if (rs != null)
          rs.close();
        pstmt.close();
        rsSch.dropTab("Float_Tab", conn);
      } catch (Exception e) {
      }

    }
  }

  /*
   * @testName: testSetFloat02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:668; JDBC:JAVADOC:669;
   * JavaEE:SPEC:186;
   * 
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database execute the precompiled SQL Statement by calling the setFloat(int
   * parameterindex, float x) method for updating the NULL_VAL column of
   * Float_Tab. Call the ResultSet.getFloat(int columnIndex) method to check if
   * it returns the float Value that has been set.
   *
   */
  public void testSetFloat02() throws Fault {
    float maxFloatVal = 0;
    float rFloatVal = 0;
    try {
      rsSch.createTab("Float_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Float_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String :" + sPrepStmt);
      msg.setMsg("extract the Value of Maximum value to be Updated ");
      String smaxFloatVal = rsSch.extractVal("Float_Tab", 1, sqlp, conn);

      // to convert the String into float value
      maxFloatVal = Float.parseFloat(smaxFloatVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setFloat(1, maxFloatVal);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Float_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rFloatVal = rs.getFloat(1);

      msg.addOutputMsg(" " + maxFloatVal, "" + rFloatVal);
      if (rFloatVal == maxFloatVal) {
        msg.setMsg(
            "setFloat Method sets the designated parameter to a float value ");
      } else {
        msg.printTestError(
            "setFloat Method does not set the designated parameter to a float value ",
            "Call to setFloat Method is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setFloat Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setFloat Method is Failed!");

    } finally {
      try {
        if (rs != null)
          rs.close();
        pstmt.close();
        rsSch.dropTab("Float_Tab", conn);
      } catch (Exception e) {
      }

    }
  }

  /*
   * @testName: testSetInt01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:664; JDBC:JAVADOC:665;
   * JavaEE:SPEC:186;
   * 
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database execute the precompiled SQL Statement by calling the setInt(int
   * parameterindex, int x) method for updating the MAX_VAL column of
   * Integer_Tab. Call the ResultSet.getInt(int columnIndex) method to check if
   * it returns the integer Value that has been set.
   *
   */
  public void testSetInt01() throws Fault {
    int rIntegerVal = 0;
    int minIntegerVal = 0;
    try {
      rsSch.createTab("Integer_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Integer_Tab_Max_Update", "");
      msg.setMsg("Prepared Statement String :" + sPrepStmt);
      msg.setMsg("extract the Value of Minimum value to be Updated");
      String sminIntegerVal = rsSch.extractVal("Integer_Tab", 2, sqlp, conn);

      // to convert the String into integer value
      minIntegerVal = Integer.parseInt(sminIntegerVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setInt(1, minIntegerVal);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Max_Val_Query = sqlp.getProperty("Integer_Query_Max", "");
      msg.setMsg(Max_Val_Query);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();
      rIntegerVal = rs.getInt(1);

      msg.addOutputMsg(" " + minIntegerVal, "" + rIntegerVal);
      if (rIntegerVal == minIntegerVal) {
        msg.setMsg(
            "setInt Method sets the designated parameter to a int value ");
      } else {
        msg.printTestError(
            "setInteger Method does not set the designated parameter to a int value ",
            "Call to setInt Method is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setInt	 Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setInt Method is Failed!");

    } finally {
      try {
        if (rs != null)
          rs.close();
        pstmt.close();
        rsSch.dropTab("Integer_Tab", conn);
      } catch (Exception e) {
      }

    }
  }

  /* cleanup */
  public void cleanup() throws Fault {
    try {

      stmt.close();
      pstmt.close();
      dbSch.destroyData(conn);
      // Close the database
      rsSch.dbUnConnect(conn);
      logMsg("Cleanup ok;");
    } catch (Exception e) {
      logErr("An error occurred while closing the database connection", e);
    }
  }
}
