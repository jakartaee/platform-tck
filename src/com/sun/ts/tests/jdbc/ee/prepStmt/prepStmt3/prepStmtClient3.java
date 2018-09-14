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

package com.sun.ts.tests.jdbc.ee.prepStmt.prepStmt3;

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
 * The prepStmtClient3 class tests methods of DatabaseMetaData interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.8, 11/24/00
 */

public class prepStmtClient3 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.prepStmt.prepStmt3";

  // Naming specific member variables
  private TSNamingContextInterface jc = null;

  // Harness requirements
  private transient Connection conn = null;

  private ResultSet rs = null;

  private Statement stmt = null;

  private DataSource ds1 = null;

  private PreparedStatement pstmt = null;

  private dbSchema dbSch = null;

  private rsSchema rsSch = null;

  private csSchema csSch = null;

  private String drManager = null;

  private transient DatabaseMetaData dbmd = null;

  private Properties sqlp = null;

  private Properties props = null;

  private JDBCTestMsg msg = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    prepStmtClient3 theTests = new prepStmtClient3();
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
        /*
         * sqlp = new Properties(); String sqlStmt =
         * p.getProperty("rsQuery",""); InputStream istr = new
         * FileInputStream(sqlStmt); sqlp.load(istr);
         */
        sqlp = p;
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
        rsSch = new rsSchema();
        csSch = new csSchema();
        msg = new JDBCTestMsg();
      }

      catch (SQLException ex) {
        logErr("SQL Exception : " + ex.getMessage(), ex);
      }
    } catch (Exception e) {
      logErr("Setup Failed!");
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * @testName: testSetString01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:674; JDBC:JAVADOC:675;
   * JDBC:JAVADOC:372; JDBC:JAVADOC:373; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setString(int parameterIndex, String x) method,update
   * the column value with the maximum value of Char_Tab. Call the
   * getString(String columnName) method to retrieve this value. Extract the
   * maximum value from the tssql.stmt file. Compare this value with the value
   * returned by the getString(String columnName) method. Both the values should
   * be equal.
   */

  public void testSetString01() throws Fault {
    String maxStringVal = null;
    String rStringVal = null;

    try {
      rsSch.createTab("Char_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Char_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("to extract the Value of String to be Updated");
      maxStringVal = rsSch.extractVal("Char_Tab", 1, sqlp, conn);
      maxStringVal = maxStringVal.substring(1, maxStringVal.length() - 1);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setString(1, maxStringVal);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Char_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rStringVal = rs.getString(1);
      rStringVal = rStringVal.trim();
      maxStringVal = maxStringVal.trim();

      msg.addOutputMsg(maxStringVal, rStringVal);

      if (rStringVal.equals(maxStringVal)) {
        msg.setMsg(
            "setString Method sets the designated parameter to a String value ");
      } else {
        msg.printTestError(
            "set String Method does not set the designated parameter to a String value ",
            "Call to setString is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    }

    catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setString is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setString Failed!");

    }

    finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        rsSch.dropTab("Char_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetTime01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:680; JDBC:JAVADOC:681;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setTime(int parameterIndex, Time x) method,update the
   * column value with the Non-Null Time value. Call the getTime(int columnno)
   * method to retrieve this value. Extract the Time value from the tssql.stmt
   * file. Compare this value with the value returned by the getTime(int
   * columnno) method. Both the values should be equal
   */

  public void testSetTime01() throws Fault {
    Time brkTimeVal = null;
    Time rTimeVal = null;
    String sbrkTimeVal = null;

    try {
      rsSch.createTab("Time_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Time_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("to extract the Value of Break_Time to be Updated ");
      sbrkTimeVal = rsSch.extractVal("Time_Tab", 1, sqlp, conn);
      sbrkTimeVal = sbrkTimeVal.substring(sbrkTimeVal.indexOf('\'') + 1,
          sbrkTimeVal.lastIndexOf('\''));
      sbrkTimeVal = sbrkTimeVal.trim();

      // to convert the String into Time Val
      brkTimeVal = brkTimeVal.valueOf(sbrkTimeVal);
      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setTime(1, brkTimeVal);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Time_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rTimeVal = rs.getTime(1);

      msg.addOutputMsg("" + brkTimeVal, "" + rTimeVal);
      if (rTimeVal.equals(brkTimeVal)) {
        msg.setMsg(
            "setTime Method sets the designated parameter to a Time value ");
      } else {
        msg.printTestError(
            "setTime Method does not set the designated parameter to a Time value ",
            "Call to setTime is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setTime is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setTime is Failed!");

    }

    finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        rsSch.dropTab("Time_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetTime02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:716; JDBC:JAVADOC:717;
   * JDBC:JAVADOC:616; JDBC:JAVADOC:617; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setTime(int parameterIndex, Time x, Calendar cal)
   * method,update the column value with the Non-Null Time value using the
   * Calendar Object. Call the getTime(int columnno) method to retrieve this
   * value. Extract the Time value from the tssql.stmt file. Compare this value
   * with the value returned by the getTime(int columnno) method. Both the
   * values should be equal.
   */

  public void testSetTime02() throws Fault {
    Time brkTimeVal = null;
    Time rTimeVal = null;
    String sbrkTimeVal = null;
    Calendar cal = null;

    try {
      rsSch.createTab("Time_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Time_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("to extract the Value of Break_Time to be updated");
      sbrkTimeVal = rsSch.extractVal("Time_Tab", 1, sqlp, conn);
      sbrkTimeVal = sbrkTimeVal.substring(sbrkTimeVal.indexOf('\'') + 1,
          sbrkTimeVal.lastIndexOf('\''));
      sbrkTimeVal = sbrkTimeVal.trim();

      // to convert the String into Time Val
      brkTimeVal = brkTimeVal.valueOf(sbrkTimeVal);
      cal = Calendar.getInstance();

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setTime(1, brkTimeVal, cal);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Time_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rTimeVal = rs.getTime(1, cal);

      msg.addOutputMsg("" + brkTimeVal, "" + rTimeVal);
      if ((rTimeVal.toString()).trim().equals((brkTimeVal.toString()).trim())) {
        msg.setMsg(
            "setTime Method sets the designated parameter to a Time value ");
      } else {
        msg.printTestError(
            "setTime Method does not set the designated parameter to a Time value ",
            "Call to setTime is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setTime is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setTime is Failed!");

    }

    finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        rsSch.dropTab("Time_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetTimestamp01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:682; JDBC:JAVADOC:683;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setTimestamp(int parameterIndex, Timestamp x)
   * method,update the column value with the Non-Null Timestamp value. Call the
   * getTimestamp(int columnno) method to retrieve this value. Extract the
   * Timestamp value from the tssql.stmt file. Compare this value with the value
   * returned by the getTimestamp(int columnno) method. Both the values should
   * be equal.
   */

  public void testSetTimestamp01() throws Fault {
    Timestamp inTimeVal = null;
    Timestamp rTimestampVal = null;
    String sinTimeVal = null;

    try {
      rsSch.createTab("Timestamp_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Timestamp_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("to extract the Value of InTime to be updated");
      sinTimeVal = rsSch.extractVal("Timestamp_Tab", 1, sqlp, conn);
      sinTimeVal = sinTimeVal.substring(sinTimeVal.indexOf('\'') + 1,
          sinTimeVal.lastIndexOf('\''));

      // to convert the String into Timestamp Val
      inTimeVal = inTimeVal.valueOf(sinTimeVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setTimestamp(1, inTimeVal);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Timestamp_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rTimestampVal = rs.getTimestamp(1);

      msg.addOutputMsg("" + inTimeVal, "" + rTimestampVal);
      if (rTimestampVal.equals(inTimeVal)) {
        msg.setMsg(
            "setTimestamp Method sets the designated parameter to a Timestamp value ");
      } else {
        msg.printTestError(
            "setTimestamp Method does not set the designated parameter to a Timestamp value ",
            "Call to setTimestamp is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setTimestamp is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setTimestamp is Failed!");

    }

    finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        rsSch.dropTab("Timestamp_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetTimestamp02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:718; JDBC:JAVADOC:719;
   * JDBC:JAVADOC:620; JDBC:JAVADOC:621; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setTimestamp(int parameterIndex, Time x, Calendar cal)
   * method,update the column value with the Non-Null Timestamp value using the
   * Calendar Object. Call the getTimestamp(int columnno) method to retrieve
   * this value. Extract the Timestamp value from the tssql.stmt file. Compare
   * this value with the value returned by the getTimestamp(int columnno)
   * method. Both the values should be equal.
   */

  public void testSetTimestamp02() throws Fault {
    Timestamp inTimeVal = null;
    Timestamp rTimestampVal = null;
    String sinTimeVal = null;
    Calendar cal = null;

    try {
      rsSch.createTab("Timestamp_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Timestamp_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("to extract the Value of InTime to be Updated ");
      sinTimeVal = rsSch.extractVal("Timestamp_Tab", 1, sqlp, conn);
      sinTimeVal = sinTimeVal.substring(sinTimeVal.indexOf('\'') + 1,
          sinTimeVal.lastIndexOf('\''));

      // to convert the String into Timestamp Val
      inTimeVal = inTimeVal.valueOf(sinTimeVal);
      cal = Calendar.getInstance();

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setTimestamp(1, inTimeVal, cal);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Timestamp_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      rTimestampVal = rs.getTimestamp(1, cal);

      msg.addOutputMsg("" + inTimeVal, "" + rTimestampVal);
      if (rTimestampVal.equals(inTimeVal)) {
        msg.setMsg(
            "setTimestamp Method sets the designated parameter to a Timestamp value ");
      } else {
        msg.printTestError(
            "setTimestamp Method does not set the designated parameter to a Timestamp value ",
            "Call to setTimestamp is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setTimestamp is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setTimestamp is Failed!");

    }

    finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        rsSch.dropTab("Timestamp_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetString02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:674; JDBC:JAVADOC:675;
   * JDBC:JAVADOC:372; JDBC:JAVADOC:373; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using setString(int parameterIndex, String x),update the column
   * with the maximum value which is a SQL VARCHAR. Call the getString(int
   * ColumnIndex) method to retrieve this value. Extract the maximum value as a
   * String from the tssql.stmt file. Compare this value with the value returned
   * by the getString method. Both the values should be equal.
   */

  public void testSetString02() throws Fault {
    String maxStringVal = null;
    String rStringVal = null;

    try {
      // to create the Varchar Table
      rsSch.createTab("Varchar_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Varchar_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("to extract the Value of String to be Updated ");
      maxStringVal = rsSch.extractVal("Varchar_Tab", 1, sqlp, conn);
      maxStringVal = maxStringVal.substring(1, maxStringVal.length() - 1);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setString(1, maxStringVal);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Varchar_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rStringVal = rs.getString(1);
      rStringVal = rStringVal.trim();
      maxStringVal = maxStringVal.trim();

      msg.addOutputMsg(maxStringVal, rStringVal);
      if (rStringVal.equals(maxStringVal)) {
        msg.setMsg(
            "setString Method sets the designated parameter to a String value ");
      } else {
        msg.printTestError(
            "setString Method does not set the designated parameter to a String value ",
            "Call to setString is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setString is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setString is Failed!");

    }

    finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        rsSch.dropTab("Varchar_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetFloat01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:668; JDBC:JAVADOC:669;
   * JDBC:JAVADOC:384; JDBC:JAVADOC:385; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using setFloat(int parameterIndex,float x),update the column with
   * the minimum value of Real_Tab. Now execute a query to get the minimum value
   * and retrieve the result of the query using the getFloat(int columnIndex)
   * method.Compare the returned value, with the minimum value extracted from
   * the tssql.stmt file. Both of them should be equal.
   */

  public void testSetFloat01() throws Fault {
    float rFloatVal = 0;
    float minFloatVal = 0;

    try {
      // to create the REAL Table
      rsSch.createTab("Real_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Real_Tab_Max_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("to extract the Value of Minimum value to be Updated");
      String sminFloatVal = rsSch.extractVal("Real_Tab", 2, sqlp, conn);

      // to convert the String into Float value
      minFloatVal = Float.parseFloat(sminFloatVal);
      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setFloat(1, minFloatVal);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Max_Val_Query = sqlp.getProperty("Real_Query_Max", "");
      msg.setMsg(Max_Val_Query);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();
      rFloatVal = rs.getFloat(1);

      msg.addOutputMsg("" + minFloatVal, "" + rFloatVal);
      if (rFloatVal == minFloatVal) {
        msg.setMsg(
            "setFloat Method sets the designated parameter to a Float value ");
      } else {
        msg.printTestError(
            "setFloat Method does not set the designated parameter to a Float value ",
            "Call to setFloat is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setFloat is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setFloat is Failed!");

    }

    finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        rsSch.dropTab("Real_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetFloat02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:674; JDBC:JAVADOC:675;
   * JDBC:JAVADOC:384; JDBC:JAVADOC:385; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using setFloat(int parameterIndex,float x),update the column with
   * the maximum value of Real_Tab. Now execute a query to get the maximum value
   * and retrieve the result of the query using the getFloat(int columnIndex)
   * method.Compare the returned value, with the maximum value extracted from
   * the tssql.stmt file. Both of them should be equal.
   */

  public void testSetFloat02() throws Fault {
    float maxFloatVal = 0;
    float rFloatVal = 0;

    try {
      rsSch.createTab("Real_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Real_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("to extract the Value of Maximum value to be Updated ");
      String smaxFloatVal = rsSch.extractVal("Real_Tab", 1, sqlp, conn);

      // to convert the String into Float value
      maxFloatVal = Float.parseFloat(smaxFloatVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setFloat(1, maxFloatVal);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Real_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rFloatVal = rs.getFloat(1);

      msg.addOutputMsg("" + maxFloatVal, "" + rFloatVal);
      if (rFloatVal == maxFloatVal) {
        msg.setMsg(
            "setFloat Method sets the designated parameter to a Float value ");
      } else {
        msg.printTestError(
            "setFloat Method does not set the designated parameter to a Float value ",
            "Call to setFloat is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setFloat is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setFloat is Failed!");

    }

    finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        rsSch.dropTab("Real_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetBytes01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:676; JDBC:JAVADOC:677;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. execute the precompiled SQL Statement for updating Non-Null value
   * from Binary_Tab by calling setBytes(int parameterIndex, byte[] x) and call
   * the getBytes(int) method to check and it should return a Byte Array
   */

  public void testSetBytes01() throws Fault {
    String binarySize = null;

    try {
      binarySize = props.getProperty("binarySize");
      rsSch.createTab("Binary_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Binary_Tab_Val_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      int bytearrsize = Integer.parseInt(binarySize);
      byte[] bytearr = new byte[bytearrsize];
      String sbyteval = null;

      // to get the bytearray value
      for (int count = 0; count < bytearrsize; count++) {
        sbyteval = Integer.toString(count % 255);
        bytearr[count] = Byte.parseByte(sbyteval);
      }

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);

      // Setting the value
      pstmt.setBytes(1, bytearr);
      pstmt.executeUpdate();

      // query from the database to check the call of pstmt.executeUpdate
      String Binary_Val_Query = sqlp.getProperty("Binary_Query_Val", "");
      msg.setMsg("Query String: " + Binary_Val_Query);
      rs = stmt.executeQuery(Binary_Val_Query);
      rs.next();

      byte[] oRetVal = rs.getBytes(1);
      for (int i = 0; i < bytearrsize; i++) {
        msg.addOutputMsg(Byte.toString(bytearr[i]), Byte.toString(oRetVal[i]));
        if (oRetVal[i] != bytearr[i]) {
          msg.printTestError(
              "setBytes did not set the proper byte array values",
              "Call to setBytes Failed!");

        }
      }
      msg.setMsg("setBytes sets the proper byte array values");
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setBytes is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setBytes is Failed!");

    }

    finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }

        rsSch.dropTab("Binary_Tab", conn);
      } catch (Exception e) {
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetBytes02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:676; JDBC:JAVADOC:677;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. execute the precompiled SQL Statement for updating Non-Null value
   * from Varbinary_Tab by calling setBytes(int parameterIndex, byte[] x) and
   * call the getBytes(int) method to check and it should return a Byte Array
   */

  public void testSetBytes02() throws Fault {
    String varbinarySize = null;

    try {
      rsSch.createTab("Varbinary_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Varbinary_Tab_Val_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);
      varbinarySize = props.getProperty("varbinarySize");

      int bytearrsize = Integer.parseInt(varbinarySize);
      msg.setMsg("Varbinary Size: " + bytearrsize);
      byte[] bytearr = new byte[bytearrsize];
      String sbyteval = null;

      // to get the bytearray value
      for (int count = 0; count < bytearrsize; count++) {
        sbyteval = Integer.toString(count % 255);
        bytearr[count] = Byte.parseByte(sbyteval);
      }

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);

      // Setting the value
      pstmt.setBytes(1, bytearr);
      pstmt.executeUpdate();

      // query from the database to check the call of pstmt.executeUpdate
      String Varbinary_Val_Query = sqlp.getProperty("Varbinary_Query_Val", "");
      msg.setMsg("Query String: " + Varbinary_Val_Query);
      rs = stmt.executeQuery(Varbinary_Val_Query);
      rs.next();

      byte[] oRetVal = rs.getBytes(1);

      for (int i = 0; i < bytearrsize; i++) {
        msg.addOutputMsg(Byte.toString(bytearr[i]), Byte.toString(oRetVal[i]));
        if (oRetVal[i] != bytearr[i]) {
          msg.printTestError(
              "setBytes did not set the proper byte array values",
              "Call to setBytes is Failed!");

        }
      }

      msg.setMsg("setBytes sets the proper byte array values");
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setBytes is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setBytes is Failed!");

    }

    finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        rsSch.dropTab("Varbinary_Tab", conn);
      } catch (Exception e) {
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetBytes03
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:676; JDBC:JAVADOC:677;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. execute the precompiled SQL Statement for updating Non-Null value
   * from Longvarbinary_Tab by calling setBytes(int parameterIndex, byte[] x)
   * and call the getBytes(int) method to check and it should return a Byte
   * Array
   */

  public void testSetBytes03() throws Fault {
    byte retByteArr[] = null;

    try {
      rsSch.createTab("Longvarbinary_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Longvarbinary_Tab_Val_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      // to get the byte array value to be updated
      String binsize = props.getProperty("longvarbinarySize");
      int bytearrsize = Integer.parseInt(binsize);

      byte[] bytearr = new byte[bytearrsize];
      String sbyteval = null;

      // to get the bytearray value
      for (int count = 0; count < bytearrsize; count++) {
        sbyteval = Integer.toString(count % 255);
        bytearr[count] = Byte.parseByte(sbyteval);
      }

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setBytes(1, bytearr);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Longvarbinary_Val_Query = sqlp
          .getProperty("Longvarbinary_Query_Val", "");
      msg.setMsg(Longvarbinary_Val_Query);
      rs = stmt.executeQuery(Longvarbinary_Val_Query);
      rs.next();
      retByteArr = rs.getBytes(1);

      for (int i = 0; i < bytearrsize; i++) {
        msg.addOutputMsg(Byte.toString(bytearr[i]),
            Byte.toString(retByteArr[i]));
        if (retByteArr[i] != bytearr[i]) {
          msg.printTestError(
              "setBytes Method does not set the designated parameter to a byte array",
              "Call to setBytes is Failed!");

        }
      }
      msg.setMsg(
          "setBytes Method sets the designated parameter to a Byte Array Value");
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setBytes is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setBytes is Failed!");

    }

    finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        rsSch.dropTab("Longvarbinary_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_Val with the maximum value of Char_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the maximum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnno) method. Both the values should be equal.
   */

  public void testSetObject01() throws Fault {
    String maxStringVal = null;
    String rStringVal = null;

    try {
      rsSch.createTab("Char_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Char_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("to extract the Value of String to be Updated ");
      maxStringVal = rsSch.extractVal("Char_Tab", 1, sqlp, conn);
      maxStringVal = maxStringVal.substring(1, maxStringVal.length() - 1);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxStringVal);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Char_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rStringVal = rs.getObject(1).toString();
      rStringVal = rStringVal.trim();
      maxStringVal = maxStringVal.trim();

      msg.addOutputMsg(maxStringVal, rStringVal);
      if (rStringVal.equals(maxStringVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "Call to setObject is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

    }

    finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        rsSch.dropTab("Char_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_val with the maximum value of Varchar_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the maximum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnNo) method. Both the values should be equal.
   */

  public void testSetObject02() throws Fault {
    String maxStringVal = null;
    String rStringVal = null;

    try {
      rsSch.createTab("Varchar_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Varchar_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("to extract the Value of String to be Updated");
      maxStringVal = rsSch.extractVal("Varchar_Tab", 1, sqlp, conn);
      maxStringVal = maxStringVal.substring(1, maxStringVal.length() - 1);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxStringVal);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Varchar_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rStringVal = rs.getObject(1).toString();
      rStringVal = rStringVal.trim();
      maxStringVal = maxStringVal.trim();

      msg.addOutputMsg(maxStringVal, rStringVal);
      if (rStringVal.equals(maxStringVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "Call to setObject is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

    }

    finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        rsSch.dropTab("Varchar_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject03
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_val with the maximum value of Longvarchar_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the maximum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnNo) method. Both the values should be equal.
   */

  public void testSetObject03() throws Fault {
    String maxStringVal = null;
    String rStringVal = null;

    try {
      rsSch.createTab("Longvarcharnull_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Longvarchar_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("to extract the Value of String to be Updated");
      maxStringVal = rsSch.extractVal("Longvarcharnull_Tab", 1, sqlp, conn);
      maxStringVal = maxStringVal.substring(1, maxStringVal.length() - 1);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxStringVal);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Longvarchar_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rStringVal = rs.getObject(1).toString();
      rStringVal = rStringVal.trim();
      maxStringVal = maxStringVal.trim();

      msg.addOutputMsg(maxStringVal, rStringVal);
      if (rStringVal.equals(maxStringVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "Call to setObject is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

    }

    finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        rsSch.dropTab("Longvarcharnull_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject04
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_val with the maximum value of Numeric_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the maximum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnNo) method. Both the values should be equal.
   */

  public void testSetObject04() throws Fault {
    BigDecimal maxBigDecimalVal = null;
    BigDecimal rBigDecimalVal = null;
    String smaxBigDecimalVal = null;

    try {
      // to create the Numeric Table
      rsSch.createTab("Numeric_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Numeric_Tab_Min_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("to extract the Maximum Value of BigDecimal to be Updated");
      smaxBigDecimalVal = rsSch.extractVal("Numeric_Tab", 1, sqlp, conn);
      maxBigDecimalVal = new BigDecimal(smaxBigDecimalVal);
      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxBigDecimalVal);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Min_Val_Query = sqlp.getProperty("Numeric_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      rBigDecimalVal = (BigDecimal) rs.getObject(1);

      msg.addOutputMsg("" + maxBigDecimalVal, "" + rBigDecimalVal);
      if (rBigDecimalVal.compareTo(maxBigDecimalVal) == 0) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "Call to setObject is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

    }

    finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        rsSch.dropTab("Numeric_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject05
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_val with the minimum value of Numeric_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the minimum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnNo) method. Both the values should be equal.
   */

  public void testSetObject05() throws Fault {
    BigDecimal minBigDecimalVal = null;
    BigDecimal rBigDecimalVal = null;
    String sminBigDecimalVal = null;

    try {
      // to create the Numeric Table
      rsSch.createTab("Numeric_Tab", sqlp, conn);

      // to update Null value column with Minimum value
      String sPrepStmt = sqlp.getProperty("Numeric_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("to extract the Maximum Value of BigDecimal to be Updated");
      sminBigDecimalVal = rsSch.extractVal("Numeric_Tab", 2, sqlp, conn);
      minBigDecimalVal = new BigDecimal(sminBigDecimalVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, minBigDecimalVal);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Numeric_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rBigDecimalVal = (BigDecimal) rs.getObject(1);

      msg.addOutputMsg("" + minBigDecimalVal, "" + rBigDecimalVal);
      if (rBigDecimalVal.compareTo(minBigDecimalVal) == 0) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "Call to setObject is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

    }

    finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        rsSch.dropTab("Numeric_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject06
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Min_val with the maximum value of Decimal_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the maximum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnNo) method. Both the values should be equal.
   */

  public void testSetObject06() throws Fault {
    BigDecimal maxBigDecimalVal = null;
    BigDecimal rBigDecimalVal = null;
    String smaxBigDecimalVal = null;

    try {
      // to create the Decimal Table
      rsSch.createTab("Decimal_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Decimal_Tab_Min_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("to extract the Maximum Value of BigDecimal to be Updated ");
      smaxBigDecimalVal = rsSch.extractVal("Decimal_Tab", 1, sqlp, conn);
      maxBigDecimalVal = new BigDecimal(smaxBigDecimalVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxBigDecimalVal);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Min_Val_Query = sqlp.getProperty("Decimal_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      rBigDecimalVal = (BigDecimal) rs.getObject(1);

      msg.addOutputMsg("" + maxBigDecimalVal, "" + rBigDecimalVal);
      if (rBigDecimalVal.compareTo(maxBigDecimalVal) == 0) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "Call to setObject is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

    }

    finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        rsSch.dropTab("Decimal_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject07
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_val with the minimum value of Decimal_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the minimum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnNo) method. Both the values should be equal.
   */

  public void testSetObject07() throws Fault {
    BigDecimal minBigDecimalVal = null;
    BigDecimal rBigDecimalVal = null;
    String sminBigDecimalVal = null;

    try {
      // to create the Decimal Table
      rsSch.createTab("Decimal_Tab", sqlp, conn);

      // to update Null value column with Minimum value
      String sPrepStmt = sqlp.getProperty("Decimal_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("to extract the Maximum Value of BigDecimal to be Updated ");
      sminBigDecimalVal = rsSch.extractVal("Decimal_Tab", 2, sqlp, conn);
      minBigDecimalVal = new BigDecimal(sminBigDecimalVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, minBigDecimalVal);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Decimal_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rBigDecimalVal = (BigDecimal) rs.getObject(1);

      msg.addOutputMsg("" + minBigDecimalVal, "" + rBigDecimalVal);
      if (rBigDecimalVal.compareTo(minBigDecimalVal) == 0) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "Call to setObject is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

    }

    finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        rsSch.dropTab("Decimal_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject08
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
   * JDBC:JAVADOC:374; JDBC:JAVADOC:375; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Min_val with the maximum value of Bit_Tab. Call the
   * getBoolean(int columnno) method to retrieve this value. Extract the maximum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getBoolean(int columnNo) method. Both the values should be equal.
   */

  public void testSetObject08() throws Fault {
    Boolean maxBooleanVal = null;
    Boolean rBooleanVal = null;

    try {
      // to create the Bit Table
      rsSch.createTab("Bit_Tab", sqlp, conn);

      // to update Minimum (false) value column with maximum (true)
      String sPrepStmt = sqlp.getProperty("Bit_Tab_Min_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt
          + "to extract the Maximum Value of Boolean to be Updated");

      maxBooleanVal = rsSch.extractValAsBoolObj("Bit_Tab", 1, sqlp, conn);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxBooleanVal);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Min_Val_Query = sqlp.getProperty("Bit_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      rBooleanVal = Boolean.valueOf(rs.getBoolean(1));

      msg.addOutputMsg("" + maxBooleanVal, "" + rBooleanVal);
      if (rBooleanVal.equals(maxBooleanVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "Call to setObject is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

    }

    finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        rsSch.dropTab("Bit_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject09
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
   * JDBC:JAVADOC:374; JDBC:JAVADOC:375; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_val with the minimum value of Bit_Tab. Call the
   * getBoolean(int columnno) method to retrieve this value. Extract the minimum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getBoolean(int columnNo) method. Both the values should be equal.
   */

  public void testSetObject09() throws Fault {
    Boolean minBooleanVal = null;
    Boolean rBooleanVal = null;

    try {
      // to create the Bit Table
      rsSch.createTab("Bit_Tab", sqlp, conn);

      // to update Null value column with Minimum value
      String sPrepStmt = sqlp.getProperty("Bit_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt
          + " to extract the Minimum Value of Boolean to be Updated");

      minBooleanVal = rsSch.extractValAsBoolObj("Bit_Tab", 2, sqlp, conn);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, minBooleanVal);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Bit_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rBooleanVal = Boolean.valueOf(rs.getBoolean(1));

      msg.addOutputMsg("" + minBooleanVal, "" + rBooleanVal);
      if (rBooleanVal.equals(minBooleanVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "Call to setObject is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

    }

    finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        rsSch.dropTab("Bit_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /* cleanup */
  public void cleanup() throws Fault {
    try {
      if (pstmt != null) {
        pstmt.close();
        pstmt = null;
      }
      if (stmt != null) {
        stmt.close();
        stmt = null;
      }
      // Close the database
      rsSch.dbUnConnect(conn);
      logMsg("Cleanup ok;");
    } catch (Exception e) {
      logErr("An error occurred while closing the database connection", e);
    }
  }
}
