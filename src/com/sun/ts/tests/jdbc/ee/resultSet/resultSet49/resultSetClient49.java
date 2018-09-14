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
 * @(#)resultSetClient49.java	1.24 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.resultSet.resultSet49;

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
 * The resultSetClient49 class tests methods of resultSet interface using Sun's
 * J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 99/10/12
 */

public class resultSetClient49 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.resultSet.resultSet49";

  // Naming specific member variables
  private TSNamingContextInterface jc = null;

  // Harness requirements
  private transient Connection conn = null;

  private transient DatabaseMetaData dbmd = null;

  private Statement stmt = null;

  private PreparedStatement pstmt = null;

  private DataSource ds1 = null;

  private String drManager = null;

  private String sqlStmt = null;

  private dbSchema dbSch = null;

  private rsSchema rsSch = null;

  private Properties props = null;

  private Properties sqlp = null;

  private JDBCTestMsg msg = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    resultSetClient49 theTests = new resultSetClient49();
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
        props = p;
        drManager = p.getProperty("DriverManager", "");
        if (drManager.length() == 0)
          throw new Fault("Invalid DriverManager Name");
        /*
         * sqlp=new Properties(); sqlStmt= p.getProperty("rsQuery","");
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
        stmt = conn.createStatement(/*
                                     * ResultSet.TYPE_SCROLL_INSENSITIVE,
                                     * ResultSet.CONCUR_READ_ONLY
                                     */);
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
   * @testName: testGetTime13
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:394;
   * JDBC:JAVADOC:395; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing a query that returns
   * the non-null column of Time_Tab as a Time object.Call the getTime(int
   * columnIndex) method to retrieve this value.Extract the non-null value of
   * Time_Tab from the tssql.stmt file as a String.Convert this value into a
   * Time object.Compare this object with the object returned by the getTime(int
   * columnIndex).Both of them should be equal.
   */
  public void testGetTime13() throws Fault {
    Time retVal;
    Time brkVal;
    ResultSet rs = null;
    String str = null;
    try {
      rsSch.createTab("Time_Tab", sqlp, conn);
      msg.setMsg("perform query to get the value of Time from Time_Tab");
      String Time_Query_Brk = sqlp.getProperty("Time_Query_Brk", null);
      rs = stmt.executeQuery(Time_Query_Brk);
      rs.next();
      msg.setMsg("Calling getTime on Time_Tab");
      retVal = rs.getTime(1);
      msg.setMsg("extract the Value of Time from Time_Tab");
      str = rsSch.extractVal("Time_Tab", 1, sqlp, conn);
      str = getSingleQuoteContent(str);
      // Convert the string str into a Time object
      brkVal = Time.valueOf(str);

      msg.addOutputMsg("" + brkVal, "" + retVal);
      if (retVal.equals(brkVal))
        msg.setMsg("getTime method returns: " + retVal);
      else {

        msg.printTestError(
            "getTime does not return the Time value from Time_Tab",
            "Call to getTime(int columnIndex) failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getTime method has failed!!");

    } catch (Exception e) {
      msg.printError(e, "Call to getTime method has failed!!");

    }

    finally {
      try {
        stmt.close();
        rsSch.dropTab("Time_Tab", conn);
      } catch (Exception eclean) {
      }
    }
  }

  /*
   * @testName: testGetTime14
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:394;
   * JDBC:JAVADOC:395; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing a query that returns
   * null value from Time_Tab as a Time object.Call the getTime(int columnIndex)
   * method. Check if the value returned is null.
   */
  public void testGetTime14() throws Fault {
    Time retVal;
    ResultSet rs = null;
    try {
      rsSch.createTab("Time_Tab", sqlp, conn);
      String Time_Query_Null = sqlp.getProperty("Time_Query_Null", null);
      rs = stmt.executeQuery(Time_Query_Null);
      rs.next();
      msg.setMsg("Calling getTime on Time_Tab");
      retVal = rs.getTime(1);
      if (retVal == null)
        msg.setMsg("getTime method returns :" + retVal);
      else {
        msg.printTestError("getTime method does not return null",
            "test getTime Failed");

      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getTime method has failed!!");

    } catch (Exception e) {
      msg.printError(e, "Call to getTime method has failed!!");

    }

    finally {
      try {
        stmt.close();
        rsSch.dropTab("Time_Tab", conn);
      } catch (Exception eclean) {
      }
    }
  }

  /*
   * @testName: testGetTime16
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:394;
   * JDBC:JAVADOC:395; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing a query that returns
   * null value from Timestamp_Tab as a Time object.Call the getTime(int
   * columnIndex) method. Check if the value returned is null.
   */
  public void testGetTime16() throws Fault {
    Time retVal;
    ResultSet rs = null;
    try {
      rsSch.createTab("Timestamp_Tab", sqlp, conn);
      String Timestamp_Query_Null = sqlp.getProperty("Timestamp_Query_Null",
          null);
      rs = stmt.executeQuery(Timestamp_Query_Null);
      rs.next();
      msg.setMsg("Calling getTime on Timestamp_Tab");
      retVal = rs.getTime(1);
      if (retVal == null)
        msg.setMsg("getTime method returns :" + retVal);
      else {
        msg.printTestError("getTime method does not return null",
            "test getTime Failed");

      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getTime method has failed!!");

    } catch (Exception e) {
      msg.printError(e, "Call to getTime method has failed!!");

    }

    finally {
      try {
        stmt.close();
        rsSch.dropTab("Timestamp_Tab", conn);
      } catch (Exception eclean) {
      }
    }
  }

  /*
   * @testName: testGetTime17
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:426;
   * JDBC:JAVADOC:427; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing a query that returns
   * non null column of Time_Tab as a Time object.Call the getTime(String
   * columnName) to retrieve this value.Extract the non-null value ofTime_Tab
   * from the tssql.stmt file as a String.Convert this value into a Time
   * object.Compare this object with the object returned by the getTime(String
   * columnName) method. Both of them should be equal.
   */
  public void testGetTime17() throws Fault {
    Time retVal;
    Time brkVal;
    ResultSet rs = null;
    ResultSetMetaData rsmd = null;
    String columname = null;
    String str = null;
    try {
      rsSch.createTab("Time_Tab", sqlp, conn);
      msg.setMsg("perform query to get the value of Time from Time_Tab");
      String Time_Query_Brk = sqlp.getProperty("Time_Query_Brk", null);
      rs = stmt.executeQuery(Time_Query_Brk);
      rsmd = rs.getMetaData();
      rs.next();
      msg.setMsg("Calling getTime on Time_Tab");
      columname = rsmd.getColumnName(1);
      retVal = rs.getTime(columname);
      msg.setMsg("extract the Value of Time from Time_Tab");
      str = rsSch.extractVal("Time_Tab", 1, sqlp, conn);
      str = getSingleQuoteContent(str);
      // Convert the string str into a Time object
      brkVal = Time.valueOf(str);

      msg.addOutputMsg("" + brkVal, "" + retVal);
      if (retVal.equals(brkVal))
        msg.setMsg("getTime method returns: " + retVal);
      else {

        msg.printTestError(
            "getTime does not return the Time value from Char_Tab",
            "test getTime(int columnIndex) failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getTime method has failed!!");

    } catch (Exception e) {
      msg.printError(e, "Call to getTime method has failed!!");

    }

    finally {
      try {
        stmt.close();
        rsSch.dropTab("Time_Tab", conn);
      } catch (Exception eclean) {
      }
    }
  }

  /*
   * @testName: testGetTime18
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:426;
   * JDBC:JAVADOC:427; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing a query that returns
   * null value from Time_Tab as a Time object.Call the getTime(String
   * columnName) method. Check if the value returned is null.
   */
  public void testGetTime18() throws Fault {
    Time retVal;
    ResultSet rs = null;
    ResultSetMetaData rsmd = null;
    String columname = null;
    try {
      rsSch.createTab("Time_Tab", sqlp, conn);
      String Time_Query_Null = sqlp.getProperty("Time_Query_Null", null);
      rs = stmt.executeQuery(Time_Query_Null);
      rsmd = rs.getMetaData();
      rs.next();
      msg.setMsg("Calling getTime on Time_Tab");
      columname = rsmd.getColumnName(1);
      retVal = rs.getTime(columname);
      if (retVal == null)
        msg.setMsg("getTime method returns :" + retVal);
      else {
        msg.printTestError("getTime method does not return null",
            "test getTime Failed!");

      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getTime method has failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getTime method has failed!");

    }

    finally {
      try {
        stmt.close();
        rsSch.dropTab("Time_Tab", conn);
      } catch (Exception eclean) {
      }
    }
  }

  /*
   * @testName: testGetTimestamp01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:396;
   * JDBC:JAVADOC:397; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using this,update the non-null column of Char_Tab table with the
   * non-null value of Timestamp_Tab.Execute a query that returns the non-null
   * column of Char_Tab. table.Call the getTimeStamp(int columnIndex) to
   * retrieve this value.Compare the value returned with the non null column
   * value of Timestamp_Tab table. Both of them should be equal.
   */
  public void testGetTimestamp01() throws Fault {
    Timestamp retVal;
    Timestamp brkVal;
    ResultSet rs = null;
    String str = null;
    try {
      rsSch.createTab("Char_Tab", sqlp, conn);
      msg.setMsg("extract the Value of Timestamp from Timestamp_Tab");
      str = rsSch.extractVal("Timestamp_Tab", 1, sqlp, conn);

      TestUtil.logMsg("Table String is " + str);

      str = getSingleQuoteContent(str);

      TestUtil.logMsg("Modified String is " + str);

      String sPrepStatement = sqlp.getProperty("Char_Tab_Name_Update", null);
      PreparedStatement pstmt = conn.prepareStatement(sPrepStatement);
      // Convert the string str into a Timestamp object
      brkVal = Timestamp.valueOf(str);
      pstmt.setString(1, str);
      pstmt.executeUpdate();
      msg.setMsg(
          "perform query to get the value of Timestamp from Timestamp_Tab");
      String Char_Query_Name = sqlp.getProperty("Char_Query_Name", null);
      rs = stmt.executeQuery(Char_Query_Name);
      rs.next();
      msg.setMsg("Calling getTime on Char_Tab");
      retVal = rs.getTimestamp(1);

      msg.addOutputMsg("" + brkVal, "" + retVal);
      if (retVal.equals(brkVal))
        msg.setMsg("getTimestamp method returns: " + retVal);
      else {
        msg.printTestError(
            "getTimestamp does not return the Timestamp value from Char_Tab",
            "test getTimestamp(int columnIndex) Failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getTimestamp method has failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getTimestamp method has failed!");

    }

    finally {
      try {
        stmt.close();
        pstmt.close();
        rsSch.dropTab("Char_Tab", conn);
      } catch (Exception eclean) {
      }
    }
  }

  /*
   * @testName: testGetTimestamp13
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:396;
   * JDBC:JAVADOC:397; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing a query that returns
   * null value from from Char_Tab.Call the getTimestamp(String columnIndex)
   * method.Check if it returns null.
   */
  public void testGetTimestamp13() throws Fault {
    Timestamp retVal;
    ResultSet rs = null;
    try {
      rsSch.createTab("Char_Tab", sqlp, conn);
      String Char_Query_Null = sqlp.getProperty("Char_Query_Null", null);
      rs = stmt.executeQuery(Char_Query_Null);
      rs.next();
      msg.setMsg("Calling getTimestamp on Char_Tab");
      retVal = rs.getTimestamp(1);
      if (retVal == null)
        msg.setMsg("getTimestamp method returns :" + retVal);
      else {
        msg.printTestError("getTimestamp method does not return null",
            "test getTimestamp Failed!");

      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getTimestamp method has failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getTimestamp method has failed!");

    }

    finally {
      try {
        stmt.close();
        rsSch.dropTab("Char_Tab", conn);
      } catch (Exception eclean) {
      }
    }
  }

  /*
   * @testName: testGetTimestamp03
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:396;
   * JDBC:JAVADOC:397; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using this,update the non-null column of Varchar_Tab table with
   * the non-null value of Timestamp_Tab.Execute a query that returns the
   * non-null column of Varchar_Tab. table.Call the getTimeStamp(int
   * columnIndex) to retrieve this value.Compare the value returned with the non
   * null column value of Timestamp_Tab table. Both of them should be equal.
   */
  public void testGetTimestamp03() throws Fault {
    Timestamp retVal;
    Timestamp brkVal;
    ResultSet rs = null;
    String str = null;
    try {
      rsSch.createTab("Varchar_Tab", sqlp, conn);
      msg.setMsg("extract the Value of Timestamp from Timestamp_Tab");
      str = rsSch.extractVal("Timestamp_Tab", 1, sqlp, conn);

      str = getSingleQuoteContent(str);

      String sPrepStatement = sqlp.getProperty("Varchar_Tab_Name_Update", null);
      PreparedStatement pstmt = conn.prepareStatement(sPrepStatement);
      // Convert the string str into a Timestamp object
      brkVal = Timestamp.valueOf(str);
      pstmt.setString(1, str);
      pstmt.executeUpdate();
      msg.setMsg(
          "perform query to get the value of Timestamp from Timestamp_Tab");
      String Varchar_Query_Name = sqlp.getProperty("Varchar_Query_Name", null);
      rs = stmt.executeQuery(Varchar_Query_Name);
      rs.next();
      msg.setMsg("Calling getTime on Varchar_Tab");
      retVal = rs.getTimestamp(1);
      // Compare the values of retVal & brkVal

      msg.addOutputMsg("" + brkVal, "" + retVal);
      if (retVal.equals(brkVal)) {
        msg.setMsg("getTimestamp method returns: " + retVal);
      } else {
        msg.printTestError(
            "getTimestamp does not return the Timestamp value from Varchar_Tab",
            "Call to getTimestamp(int columnIndex) failed!!!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getTimestamp method has failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getTimestamp method has failed!");

    } finally {
      try {
        stmt.close();
        pstmt.close();
        rsSch.dropTab("Varchar_Tab", conn);
      } catch (Exception eclean) {
      }
    }
  }

  /*
   * @testName: testGetTimestamp04
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:396;
   * JDBC:JAVADOC:397; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing a query that returns
   * null value from from Varchar_Tab.Call the getTimestamp(int columnIndex)
   * method.Check if it returns null.
   */
  public void testGetTimestamp04() throws Fault {
    Timestamp retVal;
    ResultSet rs = null;
    try {
      rsSch.createTab("Varchar_Tab", sqlp, conn);
      String Varchar_Query_Null = sqlp.getProperty("Varchar_Query_Null", null);
      rs = stmt.executeQuery(Varchar_Query_Null);
      rs.next();
      msg.setMsg("Calling getTime on Varchar_Tab");
      retVal = rs.getTimestamp(1);
      if (retVal == null)
        msg.setMsg("getTimestamp method returns :" + retVal);
      else {
        msg.printTestError("getTimestamp method does not return null",
            "call to getTimestamp failed");

      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getTimestamp method has failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getTimestamp method has failed!");

    }

    finally {
      try {
        stmt.close();
        rsSch.dropTab("Varchar_Tab", conn);
      } catch (Exception eclean) {
      }
    }
  }

  /*
   * @testName: testGetTimestamp12
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:396;
   * JDBC:JAVADOC:397; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using this,update the non-null column of Longvarchar_Tab table
   * with the non-null value of Timestamp_Tab.Execute a query that returns the
   * non-null column of Longvarchar_Tab. table.Call the getTimeStamp(String
   * columnIndex) to retrieve this value.Compare the value returned with the non
   * null column value of Timestamp_Tab table. Both of them should be equal.
   */
  public void testGetTimestamp12() throws Fault {
    Timestamp retVal;
    Timestamp brkVal;
    ResultSet rs = null;
    String str = null;
    try {
      rsSch.createTab("Longvarchar_Tab", sqlp, conn);
      msg.setMsg("extract the Value of Timestamp from Timestamp_Tab");
      str = rsSch.extractVal("Timestamp_Tab", 1, sqlp, conn);

      str = getSingleQuoteContent(str);

      String sPrepStatement = sqlp.getProperty("Longvarchar_Tab_Name_Update",
          null);
      PreparedStatement pstmt = conn.prepareStatement(sPrepStatement);
      // Convert the string str into a Timestamp object
      brkVal = Timestamp.valueOf(str);
      pstmt.setString(1, str);
      pstmt.executeUpdate();
      msg.setMsg(
          "perform query to get the value of Timestamp from Timestamp_Tab");
      String Longvarchar_Query_Name = sqlp.getProperty("Longvarchar_Query_Name",
          null);
      rs = stmt.executeQuery(Longvarchar_Query_Name);
      rs.next();
      msg.setMsg("Calling getTimestamp on Longvarchar_Tab");
      retVal = rs.getTimestamp(1);
      // Compare the values of retVal & brkVal
      msg.addOutputMsg("" + brkVal, "" + retVal);
      if (retVal.equals(brkVal))
        msg.setMsg("getTimestamp method returns: " + retVal);
      else {
        msg.printTestError(
            "getTimestamp does not return the Timestamp value from Longvarchar_Tab",
            "test getTimestamp Failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getTimestamp method has failed!!");

    } catch (Exception e) {
      msg.printError(e, "Call to getTimestamp method has failed!!");

    } finally {
      try {
        stmt.close();
        pstmt.close();
        rsSch.dropTab("Longvarchar_Tab", conn);
      } catch (Exception eclean) {
      }
    }
  }

  /*
   * convenience method to help us facilitate the extracting of strings that are
   * encased within single quotes ('). If the passed in string contains more
   * than 2 single quotes, we will only extract the portion of the string that
   * is between the first 2 single quotes we find.
   */
  private String getSingleQuoteContent(String str) {
    int index1 = str.indexOf("'"); // get 1st quote
    String retStr = "";

    if (index1 < 0) {
      // no single quotes so just return
      logErr("Error - No single quotes found in :  " + str);
      return "";
    } else {
      int index2 = str.indexOf("'", index1 + 1); // get 2nd quote
      if (index2 < 0) {
        // missing a closing quote so return empty string
        logErr("Error - No closing quote found in : " + str);
        return "";
      }

      retStr = str.substring(index1 + 1, index2);
      logTrace("Found the properly quoted string: \"" + retStr + "\"");
    }

    return retStr;
  }

  /* cleanup */
  public void cleanup() throws Fault {
    try {
      // Close the database
      rsSch.dbUnConnect(conn);
      logMsg("Cleanup ok;");
    } catch (Exception e) {
      logErr("An error occurred while closing the database connection", e);
    }
  }
}
