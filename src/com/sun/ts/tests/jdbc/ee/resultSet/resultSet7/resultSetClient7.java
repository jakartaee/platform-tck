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
 * @(#)resultSetClient7.java	1.25 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.resultSet.resultSet7;

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
 * The resultSetClient7 class tests methods of resultSet interface using Sun's
 * J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 9/9/99
 */

public class resultSetClient7 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.resultSet.resultSet7";

  // Naming specific member variables
  private TSNamingContextInterface jc = null;

  // Harness requirements
  private transient Connection coffeeCon = null;

  private Statement stmt = null;

  private DataSource ds1 = null;

  private String drManager = null;

  private Properties props = null;

  private rsSchema rsSch = null;

  private JDBCTestMsg msg = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    resultSetClient7 theTests = new resultSetClient7();
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

        props = p;
        if (drManager.equals("yes")) {
          logTrace("Using DriverManager");
          DriverManagerConnection dmCon = new DriverManagerConnection();
          coffeeCon = dmCon.getConnection(p);

        } else {
          logTrace("Using DataSource");
          DataSourceConnection dsCon = new DataSourceConnection();
          coffeeCon = dsCon.getConnection(p);
        }
        rsSch = new rsSchema();
        stmt = coffeeCon.createStatement();
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
   * @testName: testGetObject61
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:444;
   * JDBC:JAVADOC:445; JDBC:JAVADOC:366; JDBC:JAVADOC:367; JavaEE:SPEC:191;
   * 
   * @test_Strategy: Get a ResultSet object from the Connection to the database.
   * Call the getObject(int column index) method with the SQL null column of
   * JDBC datatype SMALLINT. It should return null Integer object.
   */
  public void testGetObject61() throws Fault {
    Integer retValue = null;
    ResultSet rs = null;
    String Null_Val_Query = null;
    try {
      rsSch.createTab("Smallint_Tab", props, coffeeCon);
      msg.setMsg("to get the query string");
      Null_Val_Query = props.getProperty("Smallint_Query_Null", "");
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      // invoke on the getObject
      msg.setMsg("Calling  getObject on ResultSet");
      retValue = (Integer) rs.getObject(1);

      if (retValue == null) {
        msg.setMsg("getObject method returns : " + retValue);
      } else {
        msg.printTestError("getObject method does not return the Null Value ",
            "test getObject Failed!");

      }
      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getObject is Failed!");

    } finally {
      try {
        rs.close();
        rsSch.dropTab("Smallint_Tab", coffeeCon);
      } catch (Exception e) {
      }
      ;
    }

  }

  /*
   * @testName: testGetObject69
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:444;
   * JDBC:JAVADOC:445; JDBC:JAVADOC:366; JDBC:JAVADOC:367; JavaEE:SPEC:191;
   * 
   * @test_Strategy: Get a ResultSet object from the Connection to the database.
   * Call the getObject(String column index) method with the column of JDBC
   * datatype SMALLINT. It should return an Integer object that has been set as
   * the maximum value of SMALLINT.
   */
  public void testGetObject69() throws Fault {
    String retValue = null;
    String extractVal = null;
    String smaxShortVal = null;
    String Max_Val_Query = null;
    ResultSet rs = null;
    try {
      rsSch.createTab("Smallint_Tab", props, coffeeCon);
      // to get the query string
      Max_Val_Query = props.getProperty("Smallint_Query_Max", "");
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();

      // invoke on the getObject
      msg.setMsg("Calling  getObject on ResultSet");
      retValue = ("" + rs.getObject(1)).trim();

      msg.setMsg("Calling extractVal to get the Maximum Value of Short");
      smaxShortVal = rsSch.extractVal("Smallint_Tab", 1, props, coffeeCon);
      extractVal = new String(smaxShortVal);

      msg.addOutputMsg(extractVal, retValue);
      if (extractVal.equals(retValue)) {
        msg.setMsg("getObject method returns : " + retValue);
      } else {
        msg.printTestError(
            "getObject method does not return the Maximum Value ",
            "Call to getObject is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getObject is Failed!");

    } finally {
      try {
        rs.close();
        rsSch.dropTab("Smallint_Tab", coffeeCon);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject70
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:444;
   * JDBC:JAVADOC:445; JDBC:JAVADOC:366; JDBC:JAVADOC:367; JavaEE:SPEC:191;
   * 
   * @test_Strategy: Get a ResultSet object from the Connection to the database.
   * Call the getObject(String column index) method with the SQL column of JDBC
   * datatype SMALLINT. It should return an Integer object that has been set as
   * the minimum value of SMALLINT.
   */
  public void testGetObject70() throws Fault {
    String retValue = null;
    String extractVal = null;
    ResultSet rs = null;
    String sminShortVal = null;
    String Min_Val_Query = null;
    try {
      rsSch.createTab("Smallint_Tab", props, coffeeCon);
      // to get the query string
      Min_Val_Query = props.getProperty("Smallint_Query_Min", "");
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      // invoke on the getObject
      msg.setMsg("Calling  getObject on ResultSet");
      retValue = ("" + rs.getObject(1)).trim();

      msg.setMsg("Calling extractVal to get the Minimum Value of Short");
      sminShortVal = rsSch.extractVal("Smallint_Tab", 2, props, coffeeCon);
      extractVal = new String(sminShortVal);

      msg.addOutputMsg(extractVal, retValue);
      if (retValue.equals(extractVal)) {
        msg.setMsg("getObject method returns : " + retValue);
      } else {
        msg.printTestError(
            "getObject method does not return the Minimum Value ",
            "Call to getObject is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getObject is Failed!");

    } finally {
      try {
        rs.close();
        rsSch.dropTab("Smallint_Tab", coffeeCon);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject62
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:446;
   * JDBC:JAVADOC:447; JDBC:JAVADOC:366; JDBC:JAVADOC:367; JDBC:JAVADOC:442;
   * JDBC:JAVADOC:443; JavaEE:SPEC:191;
   * 
   * @test_Strategy: Get a ResultSet object from the Connection to the database.
   * Call the getObject(String columnName) method with the column of JDBC
   * datatype SMALLINT. It should return an Integer object that has been set as
   * the maximum value of SMALLINT.
   */
  public void testGetObject62() throws Fault {
    String retValue = null;
    String extractVal = null;
    String smaxShortVal = null;
    String str = null;
    String Max_Val_Query = null;
    ResultSet rs = null;
    ResultSetMetaData rsmd = null;
    try {
      rsSch.createTab("Smallint_Tab", props, coffeeCon);
      msg.setMsg("to get the query string");
      Max_Val_Query = props.getProperty("Smallint_Query_Max", "");
      rs = stmt.executeQuery(Max_Val_Query);
      rsmd = rs.getMetaData();
      rs.next();

      // invoke on the getObject
      msg.setMsg("Calling  getObject on ResultSet");
      str = rsmd.getColumnName(1);
      retValue = ("" + rs.getObject(str)).trim();

      msg.setMsg("Calling extractVal to get the Maximum Value of Short");
      smaxShortVal = rsSch.extractVal("Smallint_Tab", 1, props, coffeeCon);
      extractVal = new String(smaxShortVal);

      msg.addOutputMsg(extractVal, retValue);
      if (extractVal.equals(retValue)) {
        msg.setMsg("getObject method returns : " + retValue);
      } else {
        msg.printTestError(
            "getObject method does not return the Maximum Value ",
            "test getObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getObject is Failed!");

    } finally {
      try {
        rs.close();
        rsSch.dropTab("Smallint_Tab", coffeeCon);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject63
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:446;
   * JDBC:JAVADOC:447; JDBC:JAVADOC:442; JDBC:JAVADOC:443; JavaEE:SPEC:191;
   * 
   * @test_Strategy: Get a ResultSet object from the Connection to the database.
   * Call the getObject(String columnName) method with the SQL column of JDBC
   * datatype SMALLINT. It should return an Integer object that has been set as
   * the minimum value of SMALLINT.
   */
  public void testGetObject63() throws Fault {
    String retValue = null;
    String extractVal = null;
    ResultSet rs = null;
    ResultSetMetaData rsmd = null;
    String str = null;
    String sminShortVal = null;
    String Min_Val_Query = null;
    try {
      rsSch.createTab("Smallint_Tab", props, coffeeCon);
      msg.setMsg("to get the query string");
      Min_Val_Query = props.getProperty("Smallint_Query_Min", "");
      rs = stmt.executeQuery(Min_Val_Query);
      rsmd = rs.getMetaData();
      rs.next();

      // invoke on the getObject
      msg.setMsg("Calling  getObject on ResultSet");
      str = rsmd.getColumnName(1);
      retValue = ("" + rs.getObject(str)).trim();

      msg.setMsg("Calling extractVal to get the Minimum Value of Short");
      sminShortVal = rsSch.extractVal("Smallint_Tab", 2, props, coffeeCon);
      extractVal = new String(sminShortVal);

      msg.addOutputMsg(extractVal, retValue);
      if (retValue.equals(extractVal)) {
        msg.setMsg("getObject method returns : " + retValue);
      } else {
        msg.printTestError(
            "getObject method does not return the Minimum Value ",
            "test getObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getObject is Failed!");

    } finally {
      try {
        rs.close();
        rsSch.dropTab("Smallint_Tab", coffeeCon);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject64
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:446;
   * JDBC:JAVADOC:447; JDBC:JAVADOC:442; JDBC:JAVADOC:443; JavaEE:SPEC:191;
   * 
   * @test_Strategy: Get a ResultSet object from the Connection to the database.
   * Call the getObject(String columnName) method with the SQL null column of
   * JDBC datatype SMALLINT. It should return null Integer object.
   */
  public void testGetObject64() throws Fault {
    Integer retValue = null;
    ResultSet rs = null;
    ResultSetMetaData rsmd = null;
    String str = null;
    String Null_Val_Query = null;
    try {
      rsSch.createTab("Smallint_Tab", props, coffeeCon);
      msg.setMsg("to get the query string");
      Null_Val_Query = props.getProperty("Smallint_Query_Null", "");
      rs = stmt.executeQuery(Null_Val_Query);
      rsmd = rs.getMetaData();
      rs.next();
      // invoke on the getObject
      msg.setMsg("Calling  getObject on ResultSet");
      str = rsmd.getColumnName(1);
      retValue = (Integer) rs.getObject(str);

      if (retValue == null) {
        msg.setMsg("getObject method returns : " + retValue);
      } else {
        msg.printTestError("getObject method does not return the Null Value ",
            "test getObject Failed!");

      }
      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getObject is Failed!");

    } finally {
      try {
        rs.close();
        rsSch.dropTab("Smallint_Tab", coffeeCon);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject65
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:444;
   * JDBC:JAVADOC:445; JavaEE:SPEC:191;
   * 
   * @test_Strategy: Get a ResultSet object from the Connection to the database.
   * Call the getObject(int columnIndex) method with the SQL column of JDBC
   * datatype VARCHAR. It should return an String object that has been set.
   */
  public void testGetObject65() throws Fault {
    String retValue = null;
    String Str_Query = null;
    ResultSet rs = null;
    String str = null;
    String strVal = null;
    try {
      rsSch.createTab("Varchar_Tab", props, coffeeCon);
      msg.setMsg("to get the query string");
      Str_Query = props.getProperty("Varchar_Query_Name", "");
      rs = stmt.executeQuery(Str_Query);
      rs.next();

      // invoke on the Float
      msg.setMsg("Calling  getObject on ResultSet");
      retValue = (String) rs.getObject(1);

      msg.setMsg("Calling extractVal to get the  Value of VarChar");
      strVal = rsSch.extractVal("Varchar_Tab", 1, props, coffeeCon);

      strVal = strVal.substring(1, strVal.length() - 1);

      msg.addOutputMsg(strVal, retValue);
      if (strVal.equals(retValue.trim())) {
        msg.setMsg("getObject method returns : " + retValue);
      } else {
        msg.printTestError(
            "getObject method does not return the VarChar Value ",
            "test getObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getObject is Failed!");

    } finally {
      try {
        rs.close();
        rsSch.dropTab("Varchar_Tab", coffeeCon);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject66
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:444;
   * JDBC:JAVADOC:445; JavaEE:SPEC:191;
   * 
   * @test_Strategy: Get a ResultSet object from the Connection to the database.
   * Call the getObject(int column index) method with the SQL null column of
   * JDBC datatype VARCHAR. It should return null String object.
   */
  public void testGetObject66() throws Fault {
    String retValue = null;
    ResultSet rs = null;
    String Str_Null_Query = null;
    try {
      rsSch.createTab("Varchar_Tab", props, coffeeCon);
      msg.setMsg("to get the query string");
      Str_Null_Query = props.getProperty("Varchar_Query_Null", "");
      rs = stmt.executeQuery(Str_Null_Query);
      rs.next();
      // invoke on the getObject
      msg.setMsg("Calling  getObject on ResultSet");
      retValue = (String) rs.getObject(1);

      if (retValue == null) {
        msg.setMsg("getObject method returns : " + retValue);
      } else {
        msg.printTestError("getObject method does not return the Null Value ",
            "test getObject Failed!");

      }
      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getObject is Failed!");

    } finally {
      try {
        rs.close();
        rsSch.dropTab("Varchar_Tab", coffeeCon);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject67
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:446;
   * JDBC:JAVADOC:447; JDBC:JAVADOC:442; JDBC:JAVADOC:443; JavaEE:SPEC:191;
   * 
   * @test_Strategy: Get a ResultSet object from the Connection to the database.
   * Call the getObject(String columnName) method with the SQL column of JDBC
   * datatype VARCHAR. It should return an String object that has been set.
   */
  public void testGetObject67() throws Fault {
    String retValue = null;
    String Str_Query = null;
    ResultSet rs = null;
    ResultSetMetaData rsmd = null;
    String str = null;
    String strVal = null;
    try {
      rsSch.createTab("Varchar_Tab", props, coffeeCon);
      msg.setMsg("to get the query string");
      Str_Query = props.getProperty("Varchar_Query_Name", "");
      rs = stmt.executeQuery(Str_Query);
      rsmd = rs.getMetaData();
      rs.next();

      // invoke on the Float
      msg.setMsg("Calling  getObject on ResultSet");
      str = rsmd.getColumnName(1);
      retValue = (String) rs.getObject(str);

      msg.setMsg("Calling extractVal to get the  Value of VarChar");
      strVal = rsSch.extractVal("Varchar_Tab", 1, props, coffeeCon);

      strVal = strVal.substring(1, strVal.length() - 1);

      msg.addOutputMsg(strVal, retValue);
      if (strVal.equals(retValue.trim())) {
        msg.setMsg("getObject method returns : " + retValue);
      } else {
        msg.printTestError(
            "getObject method does not return the VarChar Value ",
            "test getObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getObject is Failed!");

    } finally {
      try {
        rs.close();
        rsSch.dropTab("Varchar_Tab", coffeeCon);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject68
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:446;
   * JDBC:JAVADOC:447; JavaEE:SPEC:191;
   * 
   * @test_Strategy: Get a ResultSet object from the Connection to the database.
   * Call the getObject(String columnName) method with the SQL null column of
   * JDBC datatype VARCHAR. It should return null String object.
   */
  public void testGetObject68() throws Fault {
    String retValue = null;
    ResultSet rs = null;
    String str = null;
    ResultSetMetaData rsmd = null;
    String Str_Null_Query = null;
    try {
      rsSch.createTab("Varchar_Tab", props, coffeeCon);
      msg.setMsg("to get the query string");
      Str_Null_Query = props.getProperty("Varchar_Query_Null", "");
      rs = stmt.executeQuery(Str_Null_Query);
      rsmd = rs.getMetaData();
      rs.next();
      // invoke on the getObject
      msg.setMsg("Calling  getObject on ResultSet");
      str = rsmd.getColumnName(1);
      retValue = (String) rs.getObject(str);

      if (retValue == null) {
        msg.setMsg("getObject method returns : " + retValue);
      } else {
        msg.printTestError("getObject method does not return the Null Value ",
            "test getObject Failed!");

      }
      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getObject is Failed!");

    } finally {
      try {
        rs.close();
        rsSch.dropTab("Varchar_Tab", coffeeCon);
      } catch (Exception e) {
      }
      ;
    }
  }

  /* cleanup */
  public void cleanup() throws Fault {
    try {
      stmt.close();
      // Close the database
      rsSch.dbUnConnect(coffeeCon);
      logMsg("Cleanup ok;");
    } catch (Exception e) {
      logErr("An error occurred while closing the database connection", e);
    }
  }
}
