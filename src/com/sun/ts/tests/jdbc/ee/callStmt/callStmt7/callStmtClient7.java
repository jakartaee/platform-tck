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
 * @(#)callStmtClient7.java	1.16 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.callStmt.callStmt7;

import java.io.*;
import java.util.*;
import java.math.*;

import java.sql.*;
import javax.sql.*;
import java.sql.Types;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.rmi.RemoteException;

import com.sun.javatest.Status;
import com.sun.ts.tests.jdbc.ee.common.*;

// Merant DataSource class
//import com.merant.sequelink.jdbcx.datasource.*;

/**
 * The callStmtClient7 class tests methods of CallableStatement interface (to
 * check the Support for IN, OUT and INOUT parameters of Stored Procedure) using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class callStmtClient7 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.callStmt.callStmt7";

  // Naming specific member variables
  private TSNamingContextInterface jc = null;

  // Harness requirements

  private transient Connection conn = null;

  private DataSource ds1 = null;

  private csSchema csSch = null;

  private rsSchema rsSch = null;

  private JDBCTestMsg msg = null;

  private String drManager = null;

  private Properties sqlp = null;

  private CallableStatement cstmt = null;

  private transient DatabaseMetaData dbmd = null;

  private Statement stmt = null;

  private ResultSet rs = null;

  private Properties props = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    callStmtClient7 theTests = new callStmtClient7();
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
        csSch = new csSchema();
        msg = new JDBCTestMsg();
        stmt = conn.createStatement(/*
                                     * ResultSet.TYPE_SCROLL_INSENSITIVE,
                                     * ResultSet.CONCUR_READ_ONLY
                                     */);
      } catch (SQLException ex) {
        logErr("SQL Exception : " + ex.getMessage(), ex);
      }
    } catch (Exception e) {
      logErr("Setup Failed!");
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * @testName: testSetFloat01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:668;
   * JDBC:JAVADOC:669; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using setFloat(int parameterIndex,float x),update the column the
   * minimum value of Float_Tab. Now execute a query to get the minimum value
   * and retrieve the result of the query using the getFloat(int columnIndex)
   * method.Compare the returned value, with the minimum value extracted from
   * the tssql.stmt file. Both of them should be equal.
   */
  public void testSetFloat01() throws Fault {
    float minFloatVal;
    float rFloatVal;
    String minStringVal = null;
    String sminStringVal = null;
    try {
      rsSch.createTab("Float_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Float_In_Max(?)}");

      msg.setMsg("to extract the Minimum Value of float to be Updated");
      sminStringVal = rsSch.extractVal("Float_Tab", 2, sqlp, conn);
      minStringVal = new String(sminStringVal);
      minFloatVal = Float.parseFloat(sminStringVal);
      msg.setMsg("Minimum flaot Value to be updated :" + minFloatVal);

      cstmt.setFloat(1, minFloatVal);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");

      // to get the query string
      String Max_Val_Query = sqlp.getProperty("Float_Query_Max", "");
      msg.setMsg(Max_Val_Query);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();
      rFloatVal = rs.getFloat(1);

      msg.addOutputMsg("" + minFloatVal, "" + rFloatVal);

      if (rFloatVal == minFloatVal) {
        msg.setMsg(
            "setFloat Method sets the designated parameter to a float value ");
      } else {
        msg.printTestError(
            "setFloat Method does not set the designated parameter to a float value ",
            "test setFloat failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setFloat is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setFloat is Failed!");
      ;

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Float_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetFloat02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:668;
   * JDBC:JAVADOC:669; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using setFloat(int parameterIndex,float x),update the column the
   * maximum value of Float_Tab. Now execute a query to get the maximum value
   * and retrieve the result of the query using the getFloat(int columnIndex)
   * method.Compare the returned value, with the maximum value extracted from
   * the tssql.stmt file. Both of them should be equal.
   */
  public void testSetFloat02() throws Fault {
    float maxFloatVal;
    float rFloatVal;
    String maxStringVal = null;
    String smaxStringVal = null;
    try {
      rsSch.createTab("Float_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Float_In_Null(?)}");

      msg.setMsg("to extract the Minimum Value of float to be Updated");
      smaxStringVal = rsSch.extractVal("Float_Tab", 1, sqlp, conn);
      maxStringVal = new String(smaxStringVal);
      maxFloatVal = Float.parseFloat(smaxStringVal);
      msg.setMsg("Maximum float Value to be updated :" + maxFloatVal);

      cstmt.setFloat(1, maxFloatVal);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Float_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      rFloatVal = rs.getFloat(1);

      msg.addOutputMsg("" + maxFloatVal, "" + rFloatVal);
      if (rFloatVal == maxFloatVal) {
        msg.setMsg(
            "setFloat Method sets the designated parameter to a float value ");
      } else {
        msg.printTestError(
            "setFloat Method does not set the designated parameter to a float value ",
            "test setFloat failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setFloat is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setFloat is Failed!");
      ;

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Float_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetDouble01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:670;
   * JDBC:JAVADOC:671; JDBC:JAVADOC:386; JDBC:JAVADOC:387; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using setDouble(int parameterIndex,double x),update the column
   * the minimum value of Double_Tab. Now execute a query to get the minimum
   * value and retrieve the result of the query using the getDouble(int
   * columnIndex) method.Compare the returned value, with the minimum value
   * extracted from the tssql.stmt file. Both of them should be equal.
   */
  public void testSetDouble01() throws Fault {
    double minDoubleVal;
    double rDoubleVal;
    String minStringVal = null;
    String sminStringVal = null;
    try {
      rsSch.createTab("Double_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Double_In_Max(?)}");

      msg.setMsg("to extract the Minimum Value of double to be Updated");
      sminStringVal = rsSch.extractVal("Double_Tab", 2, sqlp, conn);
      minStringVal = new String(sminStringVal);
      minDoubleVal = Double.parseDouble(sminStringVal);
      msg.setMsg("Minimum double Value to be updated :" + minDoubleVal);

      // to set the Double
      cstmt.setDouble(1, minDoubleVal);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      // to get the query string
      String Max_Val_Query = sqlp.getProperty("Double_Query_Max", "");
      msg.setMsg(Max_Val_Query);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();
      rDoubleVal = rs.getDouble(1);
      msg.addOutputMsg("" + minDoubleVal, "" + rDoubleVal);

      if (rDoubleVal == minDoubleVal) {
        msg.setMsg(
            "setDouble Method sets the designated parameter to a double value ");
      } else {
        msg.printTestError(
            "setDouble Method does not set the designated parameter to a double value ",
            "test setDouble failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setDouble is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setDouble is Failed!");

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Double_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetDouble02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:670;
   * JDBC:JAVADOC:671; JDBC:JAVADOC:386; JDBC:JAVADOC:387; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using setDouble(int parameterIndex,double x),update the column
   * the maximum value of Double_Tab. Now execute a query to get the maximum
   * value and retrieve the result of the query using the getDouble(int
   * columnIndex) method.Compare the returned value, with the maximum value
   * extracted from the tssql.stmt file. Both of them should be equal.
   */
  public void testSetDouble02() throws Fault {
    double maxDoubleVal;
    double rDoubleVal;
    String maxStringVal = null;
    String smaxStringVal = null;
    try {
      rsSch.createTab("Double_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Double_In_Null(?)}");

      msg.setMsg("to extract the Maximum Value of double to be Updated");
      smaxStringVal = rsSch.extractVal("Double_Tab", 1, sqlp, conn);
      maxStringVal = new String(smaxStringVal);
      maxDoubleVal = Double.parseDouble(smaxStringVal);
      msg.setMsg("Maximum double Value to be updated :" + maxDoubleVal);

      cstmt.setDouble(1, maxDoubleVal);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Double_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      rDoubleVal = rs.getDouble(1);

      msg.addOutputMsg("" + maxDoubleVal, "" + rDoubleVal);

      if (rDoubleVal == maxDoubleVal) {
        msg.setMsg(
            "setDouble Method sets the designated parameter to a double value ");
      } else {
        msg.printTestError(
            "setDouble Method does not set the designated parameter to a double value ",
            "test setDouble failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setDouble is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setDouble is Failed!");
      ;

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Double_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetBytes01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:676;
   * JDBC:JAVADOC:677; JavaEE:SPEC:186;
   * 
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Binary_Val of Binary_Tab with a byte array.Execute
   * a query to get the byte array and retrieve the result of the query using
   * the getBytes(int parameterIndex) method.It should return the byte array
   * object that has been set.
   */
  public void testSetBytes01() throws Fault {
    String binarySize = null;
    try {
      rsSch.createTab("Binary_Tab", sqlp, conn);
      binarySize = props.getProperty("binarySize");

      int bytearrsize = Integer.parseInt(binarySize);
      msg.setMsg("Binary Size : " + bytearrsize);
      byte[] bytearr = new byte[bytearrsize];
      String sbyteval = null;
      msg.setMsg(" get the bytearray value");
      for (int count = 0; count < bytearrsize; count++) {
        sbyteval = Integer.toString(count % 255);
        bytearr[count] = Byte.parseByte(sbyteval);
      }

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Binary_Proc_In(?)}");
      cstmt.setBytes(1, bytearr);
      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      // query from the database to check the call of cstmt.executeUpdate
      String Binary_Val_Query = sqlp.getProperty("Binary_Query_Val", "");
      msg.setMsg("Query String :" + Binary_Val_Query);
      rs = stmt.executeQuery(Binary_Val_Query);
      rs.next();

      // invoke getBytes method
      byte[] oRetVal = rs.getBytes(1);
      for (int i = 0; i < bytearrsize; i++) {
        msg.addOutputMsg(Byte.toString(bytearr[i]), Byte.toString(oRetVal[i]));
        if (oRetVal[i] != bytearr[i]) {
          msg.printTestError(
              "setBytes did not set the proper byte array values",
              "test setBytes failed");

        }
      }
      msg.setMsg("setBytes sets the proper byte array values");
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setBytes is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setBytes is Failed!");
      ;

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
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
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:676;
   * JDBC:JAVADOC:677; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database Using the IN parameter of that
   * object,update the column Varbinary_Val of Varbinary_Tab with a byte
   * array.Execute a query to get the byte array and retrieve the result of the
   * query using the getBytes(int parameterIndex) method.It should return the
   * byte array object that has been set.
   */
  public void testSetBytes02() throws Fault {
    String varbinarySize = null;
    try {
      rsSch.createTab("Varbinary_Tab", sqlp, conn);
      varbinarySize = props.getProperty("varbinarySize");

      int bytearrsize = Integer.parseInt(varbinarySize);
      msg.setMsg("Varbinary Size : " + bytearrsize);
      byte[] bytearr = new byte[bytearrsize];
      String sbyteval = null;
      msg.setMsg(" get the bytearray value");
      for (int count = 0; count < bytearrsize; count++) {
        sbyteval = Integer.toString(count % 255);
        bytearr[count] = Byte.parseByte(sbyteval);
      }

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Varbinary_Proc_In(?)}");

      cstmt.setBytes(1, bytearr);
      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      // query from the database to check the call of cstmt.executeUpdate
      String Varbinary_Val_Query = sqlp.getProperty("Varbinary_Query_Val", "");
      msg.setMsg("Query String :" + Varbinary_Val_Query);
      rs = stmt.executeQuery(Varbinary_Val_Query);
      rs.next();

      byte[] oRetVal = rs.getBytes(1);
      for (int i = 0; i < bytearrsize; i++) {
        msg.addOutputMsg(Byte.toString(bytearr[i]), Byte.toString(oRetVal[i]));
        if (oRetVal[i] != bytearr[i]) {
          msg.printTestError(
              "setBytes did not set the proper byte array values",
              "test setBytes failed");

        }
      }
      msg.setMsg("setBytes sets the proper byte array values");
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setBytes is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setBytes is Failed!");

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Varbinary_Tab", conn);
      } catch (Exception e) {
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetDate01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:678;
   * JDBC:JAVADOC:679; JDBC:JAVADOC:392; JDBC:JAVADOC:393; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Mfg_Date of Date_Tab with the null value.Execute a
   * query to get the null value and retrieve the result of the query using the
   * getDate(int parameterIndex) method.Check if it is null.
   */
  public void testSetDate01() throws Fault {
    try {
      rsSch.createTab("Date_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Date_In_Mfg(?)}");

      cstmt.setNull(1, java.sql.Types.DATE);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      // query from the database to check the call of cstmt.executeUpdate
      String Date_Mfg_Query = sqlp.getProperty("Date_Query_Mfg", "");
      msg.setMsg("Query String :" + Date_Mfg_Query);
      rs = stmt.executeQuery(Date_Mfg_Query);
      rs.next();
      java.sql.Date oRetVal = rs.getDate(1);
      msg.addOutputMsg("null", "" + oRetVal);

      if (oRetVal == null) {
        msg.setMsg("setDate sets the Null value " + oRetVal);
      } else {
        msg.printTestError("setDate did not set the Null value",
            "test setDate failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setFloat is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setFloat is Failed!");
      ;

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Date_Tab", conn);
      } catch (Exception e) {
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetDate02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:678;
   * JDBC:JAVADOC:679; JDBC:JAVADOC:392; JDBC:JAVADOC:393; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of Date_Tab with a non null Date value
   * extracted from tssql.stmt file Execute a query to get the non null Date
   * value and retrieve the result of the query using the getDate(int
   * parameterIndex) method. Compare the returned value with the value extracted
   * from tssql.stmt file. Both of them should be equal.
   */
  public void testSetDate02() throws Fault {
    try {
      rsSch.createTab("Date_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Date_In_Null(?)}");
      msg.setMsg("CallableStatement created");
      msg.setMsg("extract the Date Value to be Updated");
      String sRetStr = rsSch.extractVal("Date_Tab", 1, sqlp, conn);
      sRetStr = sRetStr.substring(sRetStr.indexOf('\'') + 1,
          sRetStr.lastIndexOf('\''));
      sRetStr = sRetStr.trim();
      java.sql.Date oExtVal = java.sql.Date.valueOf(sRetStr);

      msg.setMsg("Date : " + sRetStr);

      cstmt.setDate(1, oExtVal);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      // query from the database to check the call of cstmt.executeUpdate
      String Date_Null_Query = sqlp.getProperty("Date_Query_Null", "");
      msg.setMsg("Query String :" + Date_Null_Query);
      rs = stmt.executeQuery(Date_Null_Query);
      rs.next();
      java.sql.Date oRetVal = rs.getDate(1);

      msg.addOutputMsg(oExtVal.toString(), oRetVal.toString());
      if (oRetVal.toString().equals(oExtVal.toString())) {
        msg.setMsg("setDate sets the non null Date value " + oRetVal);
      } else {
        msg.printTestError("setDate did not set the non null Date value",
            "test setDate Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setDate is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setDate is Failed!");
      ;

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Date_Tab", conn);
      } catch (Exception e) {
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetDate03
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:678;
   * JDBC:JAVADOC:679; JDBC:JAVADOC:612; JDBC:JAVADOC:613;JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Mfg_Date of Date_Tab with the null value.Execute a
   * query to get the null value and retrieve the result of the query using the
   * getDate(int parameterIndex,Calender cal) method.Check if it is null.
   */
  public void testSetDate03() throws Fault {
    try {
      rsSch.createTab("Date_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Date_In_Mfg(?)}");

      cstmt.setNull(1, java.sql.Types.DATE);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      Calendar oCalDefault = Calendar.getInstance();

      // query from the database to check the call of cstmt.executeUpdate
      String Date_Mfg_Query = sqlp.getProperty("Date_Query_Mfg", "");
      msg.setMsg("Query String :" + Date_Mfg_Query);
      rs = stmt.executeQuery(Date_Mfg_Query);
      rs.next();
      java.sql.Date oRetVal = rs.getDate(1, oCalDefault);
      msg.addOutputMsg("null", "" + oRetVal);

      if (oRetVal == null) {
        msg.setMsg("setDate sets the Null value ");
      } else {
        msg.printTestError("setDate did not set the Null value",
            "test setDate Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setDate is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setDate is Failed!");
      ;

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Date_Tab", conn);
      } catch (Exception e) {
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetDate04
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:714;
   * JDBC:JAVADOC:715; JDBC:JAVADOC:612; JDBC:JAVADOC:613; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database Using the IN parameter of that
   * object,update the column Null_Val of Date_Tab with a non null Date value
   * extracted from tssql.stmt file. Execute a query to get the non null Date
   * value and retrieve the result of the query using the getDate(int
   * parameterIndex,Calender cal) method.Compare the returned value with the
   * value extracted from tssql.stmt file.Both of them should be equal.
   */
  public void testSetDate04() throws Fault {
    try {
      rsSch.createTab("Date_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Date_In_Null(?)}");

      msg.setMsg("extract the Date Value to be Updated");
      String sRetStr = rsSch.extractVal("Date_Tab", 1, sqlp, conn);
      sRetStr = sRetStr.substring(sRetStr.indexOf('\'') + 1,
          sRetStr.lastIndexOf('\''));
      sRetStr = sRetStr.trim();
      java.sql.Date oExtVal = java.sql.Date.valueOf(sRetStr);
      Calendar oCalDefault = Calendar.getInstance();

      // Setting the value
      cstmt.setDate(1, oExtVal, oCalDefault);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "query from the database to check the call of cstmt.executeUpdate");
      String Date_Null_Query = sqlp.getProperty("Date_Query_Null", "");
      msg.setMsg("Query String :" + Date_Null_Query);
      rs = stmt.executeQuery(Date_Null_Query);
      rs.next();
      java.sql.Date oRetVal = rs.getDate(1, oCalDefault);
      msg.addOutputMsg(oExtVal.toString(), oRetVal.toString());

      if (oRetVal.toString().equals(oExtVal.toString())) {
        msg.setMsg("setDate sets the non null Date value ");
      } else {
        msg.printTestError("setDate did not set the non null Date value",
            "test setDate Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setDate is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setDate is Failed!");
      ;

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Date_Tab", conn);
      } catch (Exception e) {
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetTime01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:680;
   * JDBC:JAVADOC:681; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setTime(int parameterIndex, Time x) method,update the
   * column value with the Non-Null Time value. Call the getTime(int columnno)
   * method to retrieve this value. Extract the Time value from the tssql.stmt
   * file. Compare this value with the value returned by the getTime(int
   * columnno) method. Both the values should be equal.
   */

  public void testSetTime01() throws Fault {
    Time brkTimeVal = null;
    Time rTimeVal = null;
    String sbrkTimeVal = null;
    try {
      rsSch.createTab("Time_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Time_In_Null(?)}");

      msg.setMsg("extract the Value of Break_Time to be Updated");
      sbrkTimeVal = rsSch.extractVal("Time_Tab", 1, sqlp, conn);
      sbrkTimeVal = sbrkTimeVal.substring(sbrkTimeVal.indexOf('\'') + 1,
          sbrkTimeVal.lastIndexOf('\''));
      sbrkTimeVal = new String(sbrkTimeVal);
      sbrkTimeVal = sbrkTimeVal.trim();

      // to convert the String into Time Val
      brkTimeVal = brkTimeVal.valueOf(sbrkTimeVal);
      msg.setMsg("Time Value :" + brkTimeVal);

      cstmt.setTime(1, brkTimeVal);
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Time_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rTimeVal = rs.getTime(1);

      msg.addOutputMsg(sbrkTimeVal, rTimeVal.toString());

      if (rTimeVal.toString().trim().equals(sbrkTimeVal)) {
        msg.setMsg(
            "setTime Method sets the designated parameter to a Time value ");
      } else {
        msg.printTestError(
            "setTime Method does not set the designated parameter to a Time value ",
            "test setTime Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setTime is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setTime is Failed!");
      ;

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Time_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetTime02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:716;
   * JDBC:JAVADOC:717; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setTime(int parameterIndex, Time x, Calendar cal)
   * method,update the column value with the Non-Null Time value using the
   * Calendar Object. Call the getTime(int columnno) method to retrieve this
   * value. Extract the Time value from the tssql.stmt file. Compare this value
   * with the value returned by the getTime(int columnno) method. Both the
   * values should be equal.
   */
  public void testSetTime02() throws Fault {
    Time inTimeVal = null;
    Time rTimeVal = null;
    String sinTimeVal = null;
    Calendar cal = null;
    try {
      rsSch.createTab("Time_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Time_In_Null(?)}");

      // to extract the Value of Break_Time to be Updated
      sinTimeVal = rsSch.extractVal("Time_Tab", 1, sqlp, conn);
      sinTimeVal = sinTimeVal.substring(sinTimeVal.indexOf('\'') + 1,
          sinTimeVal.lastIndexOf('\''));
      sinTimeVal = new String(sinTimeVal);
      sinTimeVal = sinTimeVal.trim();

      // to convert the String into Time Val
      inTimeVal = inTimeVal.valueOf(sinTimeVal);
      msg.setMsg("Time Value :" + inTimeVal);
      cal = Calendar.getInstance();
      cstmt.setTime(1, inTimeVal, cal);
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      msg.setMsg("get the query string");
      String Null_Val_Query = sqlp.getProperty("Time_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rTimeVal = rs.getTime(1, cal);

      msg.addOutputMsg(sinTimeVal, rTimeVal.toString());
      if (rTimeVal.toString().trim().equals(sinTimeVal)) {
        msg.setMsg(
            "setTime Method sets the designated parameter to a Time value ");
      } else {
        msg.printTestError(
            "setTime Method does not set the designated parameter to a Time value ",
            "test setTime Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setTime is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setTime is Failed!");
      ;

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Time_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetTimestamp01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:682;
   * JDBC:JAVADOC:683; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setTimestamp(int parameterIndex, Timestamp x)
   * method,update the column value with the Non-Null Timestamp value. Call the
   * getTimestamp(int columnno) method to retrieve this value. Extract the
   * Timestamp value from the tssql.stmt file. Compare this value with the value
   * returned by the getTimestamp(int columnno) method. Both the values should
   * be equal.
   */
  public void testSetTimestamp01() throws Fault {
    Timestamp brkTimeVal = null;
    Timestamp rTimeVal = null;
    String sbrkTimeVal = null;
    try {
      rsSch.createTab("Timestamp_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Timestamp_In_Null(?)}");

      msg.setMsg("extract the Value of Break_Timestamp to be Updated");
      sbrkTimeVal = rsSch.extractVal("Timestamp_Tab", 1, sqlp, conn);
      sbrkTimeVal = sbrkTimeVal.substring(sbrkTimeVal.indexOf('\'') + 1,
          sbrkTimeVal.lastIndexOf('\''));
      sbrkTimeVal = new String(sbrkTimeVal);
      sbrkTimeVal = sbrkTimeVal.trim();

      // to convert the String into Timestamp Val
      brkTimeVal = brkTimeVal.valueOf(sbrkTimeVal);
      msg.setMsg("Timestamp Value :" + brkTimeVal);

      cstmt.setTimestamp(1, brkTimeVal);
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");

      String Null_Val_Query = sqlp.getProperty("Timestamp_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rTimeVal = rs.getTimestamp(1);

      msg.addOutputMsg(brkTimeVal.toString(), rTimeVal.toString());
      if (rTimeVal.compareTo(brkTimeVal) == 0) {
        msg.setMsg(
            "setTimestamp Method sets the designated parameter to a Timestamp value ");
      } else {
        msg.printTestError(
            "setTimestamp Method does not set the designated parameter to a Timestamp value ",
            "test setTimestamp Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setTimestamp is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setTimestamp is Failed!");
      ;

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Timestamp_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetTimestamp02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:718;
   * JDBC:JAVADOC:719; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setTimestamp(int parameterIndex, Time x, Calendar cal)
   * method,update the column value with the Non-Null Timestamp value using the
   * Calendar Object. Call the getTimestamp(int columnno) method to retrieve
   * this value. Extract the Timestamp value from the tssql.stmt file. Compare
   * this value with the value returned by the getTimestamp(int columnno)
   * method. Both the values should be equal.
   */
  public void testSetTimestamp02() throws Fault {
    Timestamp inTimeVal = null;
    Timestamp rTimeVal = null;
    String sinTimeVal = null;
    Calendar cal = null;
    try {
      rsSch.createTab("Timestamp_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Timestamp_In_Null(?)}");

      // to extract the Value of Break_Timestamp to be Updated
      sinTimeVal = rsSch.extractVal("Timestamp_Tab", 1, sqlp, conn);
      sinTimeVal = sinTimeVal.substring(sinTimeVal.indexOf('\'') + 1,
          sinTimeVal.lastIndexOf('\''));
      sinTimeVal = new String(sinTimeVal);
      sinTimeVal = sinTimeVal.trim();

      // to convert the String into Timestamp Val
      inTimeVal = inTimeVal.valueOf(sinTimeVal);
      msg.setMsg("get the Calendar Instance");
      cal = Calendar.getInstance();
      cstmt.setTimestamp(1, inTimeVal, cal);
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Timestamp_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rTimeVal = rs.getTimestamp(1, cal);
      msg.addOutputMsg(inTimeVal.toString(), rTimeVal.toString());

      if (rTimeVal.compareTo(inTimeVal) == 0) {
        msg.setMsg(
            "setTimestamp Method sets the designated parameter to a Timestamp value ");
      } else {
        msg.printTestError(
            "setTimestamp Method does not set the designated parameter to a Timestamp value ",
            "test setTimestamp Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setTimestamp is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setTimestamp is Failed!");
      ;

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Timestamp_Tab", conn);
      } catch (Exception e) {
      }
    }
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
