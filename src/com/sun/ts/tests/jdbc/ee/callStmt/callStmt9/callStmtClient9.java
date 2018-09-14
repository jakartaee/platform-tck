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
 * @(#)callStmtClient9.java	1.18 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.callStmt.callStmt9;

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
 * The callStmtClient9 class tests methods of CallableStatement interface (to
 * check the Support for IN, OUT and INOUT parameters of Stored Procedure) using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class callStmtClient9 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.callStmt.callStmt9";

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
  // private ResultSet rs = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    callStmtClient9 theTests = new callStmtClient9();
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
        csSch = new csSchema();
        msg = new JDBCTestMsg();
        stmt = conn.createStatement(/*
                                     * ResultSet.TYPE_SCROLL_INSENSITIVE,
                                     * ResultSet.CONCUR_READ_ONLY
                                     */);
        dbmd = conn.getMetaData();
      } catch (SQLException ex) {
        logErr("SQL Exception : " + ex.getMessage(), ex);
      }
    } catch (Exception e) {
      logErr("Setup Failed!");
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * @testName: testSetObject21
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set Float object for SQL Type REAL and
   * call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Float object that is
   * been set. Compare the result with the extracted value from the tssql.stmt
   * file. Both the values should be equal. *
   */
  public void testSetObject21() throws Fault {
    ResultSet rs = null;
    Float oRetVal = null;
    String sRetVal = null;
    try {
      rsSch.createTab("Real_Tab", sqlp, conn);

      String sMaxRealVal = rsSch.extractVal("Real_Tab", 1, sqlp, conn);
      Float maxReal = new Float(sMaxRealVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Real_In_Min(?)}");
      cstmt.setObject(1, maxReal);
      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Min_Val_Query = sqlp.getProperty("Real_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();
      sRetVal = "" + rs.getObject(1);
      oRetVal = new Float(sRetVal);

      msg.addOutputMsg("" + maxReal, "" + oRetVal);

      if (oRetVal.compareTo(maxReal) == 0) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed !");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject Method is Failed !");

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
        rsSch.dropTab("Real_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject22
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set Float object for SQL Type REAL and
   * call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Float object that is
   * been set. Compare the result with the extracted value from the tssql.stmt
   * file. Both the values should be equal. *
   */
  public void testSetObject22() throws Fault {
    ResultSet rs = null;
    Float oRetVal = null;
    String sRetVal = null;
    try {
      rsSch.createTab("Real_Tab", sqlp, conn);

      String sMinRealVal = rsSch.extractVal("Real_Tab", 2, sqlp, conn);
      Float minReal = new Float(sMinRealVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Real_In_Null(?)}");
      cstmt.setObject(1, minReal);
      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Null_Val_Query = sqlp.getProperty("Real_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      sRetVal = "" + rs.getObject(1);
      oRetVal = new Float(sRetVal);

      msg.addOutputMsg("" + minReal, "" + oRetVal);
      if (oRetVal.compareTo(minReal) == 0) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed !");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject Method is Failed !");

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
        rsSch.dropTab("Real_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject23
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set Integer object for SQL Type TINYINT
   * and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Integer object that is
   * been set. Compare the result with the extracted value from the tssql.stmt
   * file. Both the values should be equal. *
   */
  public void testSetObject23() throws Fault {
    ResultSet rs = null;
    String sRetVal = null;
    try {
      rsSch.createTab("Tinyint_Tab", sqlp, conn);

      String sMaxTinyintVal = rsSch.extractVal("Tinyint_Tab", 1, sqlp, conn);
      Integer maxTinyint = new Integer(sMaxTinyintVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Tinyint_In_Min(?)}");
      cstmt.setObject(1, maxTinyint);
      msg.setMsg("execute the procedure");
      ;
      cstmt.executeUpdate();

      String Min_Val_Query = sqlp.getProperty("Tinyint_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();
      sRetVal = "" + rs.getObject(1);

      msg.addOutputMsg(sMaxTinyintVal, sRetVal);
      if (sRetVal.trim().equals(sMaxTinyintVal.trim())) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed !");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject Method is Failed !");

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
        rsSch.dropTab("Tinyint_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject24
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set Integer object for SQL Type TINYINT
   * and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Integer object that is
   * been set. Compare the result with the extracted value from the tssql.stmt
   * file. Both the values should be equal. *
   *
   */
  public void testSetObject24() throws Fault {
    ResultSet rs = null;
    String sRetVal = null;
    try {
      rsSch.createTab("Tinyint_Tab", sqlp, conn);

      String sMinTinyintVal = rsSch.extractVal("Tinyint_Tab", 2, sqlp, conn);
      Integer minTinyint = new Integer(sMinTinyintVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Tinyint_In_Null(?)}");
      cstmt.setObject(1, minTinyint);
      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Null_Val_Query = sqlp.getProperty("Tinyint_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      sRetVal = "" + rs.getObject(1);
      msg.addOutputMsg(sMinTinyintVal, sRetVal);

      if (sRetVal.trim().equals(sMinTinyintVal.trim())) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed !");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject Method is Failed !");

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
        rsSch.dropTab("Tinyint_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject25
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set Integer object for SQL Type
   * SMALLINT and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Integer object that is
   * been set. Compare the result with the extracted value from the tssql.stmt
   * file. Both the values should be equal. *
   *
   */
  public void testSetObject25() throws Fault {
    ResultSet rs = null;
    String sRetVal = null;
    try {
      rsSch.createTab("Smallint_Tab", sqlp, conn);

      String sMaxSmallintVal = rsSch.extractVal("Smallint_Tab", 1, sqlp, conn);
      Integer maxSmallint = new Integer(sMaxSmallintVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Smallint_In_Min(?)}");
      cstmt.setObject(1, maxSmallint);
      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Min_Val_Query = sqlp.getProperty("Smallint_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();
      sRetVal = "" + rs.getObject(1);

      msg.addOutputMsg(sMaxSmallintVal, sRetVal);
      if (sRetVal.trim().equals(sMaxSmallintVal.trim())) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed !");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject Method is Failed !");

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
        rsSch.dropTab("Smallint_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject26
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set Integer object for SQL Type
   * SMALLINT and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Integer object that is
   * been set. Compare the result with the extracted value from the tssql.stmt
   * file. Both the values should be equal. *
   *
   */
  public void testSetObject26() throws Fault {
    ResultSet rs = null;
    String sRetVal = null;
    try {
      rsSch.createTab("Smallint_Tab", sqlp, conn);

      String sMinSmallintVal = rsSch.extractVal("Smallint_Tab", 2, sqlp, conn);
      Integer minSmallint = new Integer(sMinSmallintVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Smallint_In_Null(?)}");
      cstmt.setObject(1, minSmallint);
      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Null_Val_Query = sqlp.getProperty("Smallint_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      sRetVal = "" + rs.getObject(1);

      msg.addOutputMsg(sMinSmallintVal, sRetVal);

      if (sRetVal.trim().equals(sMinSmallintVal.trim())) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject method failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed !");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject Method is Failed !");

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
        rsSch.dropTab("Smallint_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject27
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set Byte array object for SQL Type
   * Binary and call statement.executeQuery method and call getObject method of
   * ResultSet. It should return a Byte Array object that is been set.
   *
   */
  public void testSetObject27() throws Fault {
    ResultSet rs = null;
    byte[] oRetVal = null;
    byte[] bytearrVal = null;
    String binarySize = null;

    try {
      binarySize = sqlp.getProperty("binarySize");
      msg.setMsg("Binary Table Size : " + binarySize);

      rsSch.createTab("Binary_Tab", sqlp, conn);

      int bytearrsize = Integer.parseInt(binarySize);
      msg.setMsg("Binary Size : " + bytearrsize);

      byte[] bytearr = new byte[bytearrsize];
      String sbyteval = null;

      for (int count = 0; count < bytearrsize; count++) {
        sbyteval = Integer.toString(count % 255);
        bytearr[count] = Byte.parseByte(sbyteval);
      }

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Binary_Proc_In(?)}");
      cstmt.setObject(1, bytearr);
      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Binary_Val_Query = sqlp.getProperty("Binary_Query_Val", "");
      msg.setMsg(Binary_Val_Query);
      rs = stmt.executeQuery(Binary_Val_Query);
      rs.next();
      oRetVal = (byte[]) rs.getObject(1);

      for (int i = 0; i < bytearrsize; i++) {
        msg.addOutputMsg(Byte.toString(bytearr[i]), Byte.toString(oRetVal[i]));
        if (oRetVal[i] != bytearr[i]) {
          msg.printTestError(
              "setObject Method does not set the designated parameter with the object",
              "test setObject failed");

        }
      }
      msg.setMsg(
          "setObject Method sets the designated parameter with the object");
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed !");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject Method is Failed !");

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
      }
    }
  }

  /*
   * @testName: testSetObject28
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set Byte array object for SQL Type
   * Varbinary and call statement.executeQuery method and call getObject method
   * of ResultSet. It should return a Varbinary object that is been set.
   *
   */
  public void testSetObject28() throws Fault {
    ResultSet rs = null;
    byte[] oRetVal = null;
    byte[] bytearrVal = null;
    String varbinarySize = null;

    try {
      varbinarySize = sqlp.getProperty("varbinarySize");
      msg.setMsg("Varbinary Table Size : " + varbinarySize);

      rsSch.createTab("Varbinary_Tab", sqlp, conn);

      int bytearrsize = Integer.parseInt(varbinarySize);
      msg.setMsg("Varbinary Size : " + bytearrsize);

      byte[] bytearr = new byte[bytearrsize];
      String sbyteval = null;

      msg.setMsg("to get the bytearray value");
      for (int count = 0; count < bytearrsize; count++) {
        sbyteval = Integer.toString(count % 255);
        bytearr[count] = Byte.parseByte(sbyteval);
      }

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Varbinary_Proc_In(?)}");
      cstmt.setObject(1, bytearr);
      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Varbinary_Val_Query = sqlp.getProperty("Varbinary_Query_Val", "");
      msg.setMsg(Varbinary_Val_Query);
      rs = stmt.executeQuery(Varbinary_Val_Query);
      rs.next();
      oRetVal = (byte[]) rs.getObject(1);

      for (int i = 0; i < bytearrsize; i++) {
        msg.addOutputMsg(Byte.toString(bytearr[i]), Byte.toString(oRetVal[i]));
        if (oRetVal[i] != bytearr[i]) {
          msg.printTestError(
              "setObject Method does not set the designated parameter with the object",
              "test setObject failed");

        }
      }
      msg.setMsg(
          "setObject Method sets the designated parameter with the object");
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed !");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject Method is Failed !");

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
      }
    }
  }

  /*
   * @testName: testSetObject29
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set Byte array object for SQL Type
   * Longvarbinary and call statement.executeQuery method and call getObject
   * method of ResultSet. It should return a Varbinary object that is been set.
   *
   */
  public void testSetObject29() throws Fault {
    ResultSet rs = null;
    byte[] oRetVal = null;
    byte[] bytearrVal = null;
    String longvarbinarySize = null;

    try {
      longvarbinarySize = sqlp.getProperty("longvarbinarySize");
      msg.setMsg("Longvarbinary Table Size : " + longvarbinarySize);

      rsSch.createTab("Longvarbinary_Tab", sqlp, conn);

      int bytearrsize = Integer.parseInt(longvarbinarySize);
      msg.setMsg("Varbinary Size : " + bytearrsize);

      byte[] bytearr = new byte[bytearrsize];
      String sbyteval = null;

      msg.setMsg("to get the bytearray value");
      for (int count = 0; count < bytearrsize; count++) {
        sbyteval = Integer.toString(count % 255);
        bytearr[count] = Byte.parseByte(sbyteval);
      }

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Longvarbinary_In(?)}");
      cstmt.setObject(1, bytearr);
      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Longvarbinary_Val_Query = sqlp
          .getProperty("Longvarbinary_Query_Val", "");
      msg.setMsg(Longvarbinary_Val_Query);
      rs = stmt.executeQuery(Longvarbinary_Val_Query);
      rs.next();
      oRetVal = (byte[]) rs.getObject(1);

      for (int i = 0; i < bytearrsize; i++) {

        msg.addOutputMsg(Byte.toString(bytearr[i]), Byte.toString(oRetVal[i]));
        if (oRetVal[i] != bytearr[i]) {
          msg.printTestError(
              "setObject Method does not set the designated parameter with the object",
              "test setObject failed");

        }
      }
      msg.setMsg(
          "setObject Method sets the designated parameter with the object");
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed !");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject Method is Failed !");

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
        rsSch.dropTab("Longvarbinary_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject30
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set String object for SQL
   * Type TINYINT and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Integer object that is
   * been set. Compare the result with the extracted value from the tssql.stmt
   * file. Both the values should be equal.
   *
   *
   */
  public void testSetObject30() throws Fault {
    ResultSet rs = null;
    String sRetVal = null;
    Integer maxTinyintVal = null;
    String sMaxTinyintVal = null;

    try {
      rsSch.createTab("Tinyint_Tab", sqlp, conn);

      sMaxTinyintVal = rsSch.extractVal("Tinyint_Tab", 1, sqlp, conn);

      maxTinyintVal = new Integer(sMaxTinyintVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Tinyint_In_Min(?)}");
      cstmt.setObject(1, sMaxTinyintVal, java.sql.Types.TINYINT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Min_Val_Query = sqlp.getProperty("Tinyint_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();
      sRetVal = "" + rs.getObject(1);

      msg.addOutputMsg(sMaxTinyintVal, sRetVal);

      if (sRetVal.trim().equals(sMaxTinyintVal.trim())) {
        msg.setMsg(
            "setObject method sets the designated parameter with the object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the object",
            "test setObject failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed !");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject Method is Failed !");

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
        rsSch.dropTab("Tinyint_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject31
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set String object for SQL
   * Type TINYINT and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Integer object that is
   * been set. Compare the result with the extracted value from the tssql.stmt
   * file. Both the values should be equal.
   *
   */
  public void testSetObject31() throws Fault {
    ResultSet rs = null;
    String sRetVal = null;
    Integer minTinyintVal = null;
    String sMinTinyintVal = null;

    try {
      rsSch.createTab("Tinyint_Tab", sqlp, conn);

      sMinTinyintVal = rsSch.extractVal("Tinyint_Tab", 2, sqlp, conn);

      minTinyintVal = new Integer(sMinTinyintVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Tinyint_In_Null(?)}");
      cstmt.setObject(1, sMinTinyintVal, java.sql.Types.TINYINT);
      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Min_Val_Query = sqlp.getProperty("Tinyint_Query_Null", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();
      sRetVal = "" + rs.getObject(1);

      msg.addOutputMsg(sMinTinyintVal, sRetVal);
      if (sRetVal.trim().equals(sMinTinyintVal.trim())) {
        msg.setMsg(
            "setObject method sets the designated parameter with the object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the object",
            "test setObject failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed !");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject Method is Failed !");

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
        rsSch.dropTab("Tinyint_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject32
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set String object for SQL
   * Type SMALLINT and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Integer object that is
   * been set. Compare the result with the extracted value from the tssql.stmt
   * file. Both the values should be equal.
   *
   */
  public void testSetObject32() throws Fault {
    ResultSet rs = null;
    String sRetVal = null;
    Integer maxSmallintVal = null;
    String sMaxSmallintVal = null;

    try {
      rsSch.createTab("Smallint_Tab", sqlp, conn);

      sMaxSmallintVal = rsSch.extractVal("Smallint_Tab", 1, sqlp, conn);

      maxSmallintVal = new Integer(sMaxSmallintVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Smallint_In_Min(?)}");
      cstmt.setObject(1, sMaxSmallintVal, java.sql.Types.SMALLINT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Min_Val_Query = sqlp.getProperty("Smallint_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();
      sRetVal = "" + rs.getObject(1);

      msg.addOutputMsg(sMaxSmallintVal, sRetVal);
      if (sRetVal.trim().equals(sMaxSmallintVal.trim())) {
        msg.setMsg(
            "setObject method sets the designated parameter with the object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the object",
            "test setObject failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed !");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject Method is Failed !");

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
        rsSch.dropTab("Smallint_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject33
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set String object for SQL
   * Type SMALLINT and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Integer object that is
   * been set. Compare the result with the extracted value from the tssql.stmt
   * file. Both the values should be equal.
   *
   */
  public void testSetObject33() throws Fault {
    ResultSet rs = null;
    String sRetVal = null;
    Integer minSmallintVal = null;
    String sMinSmallintVal = null;

    try {
      rsSch.createTab("Smallint_Tab", sqlp, conn);

      sMinSmallintVal = rsSch.extractVal("Smallint_Tab", 2, sqlp, conn);

      minSmallintVal = new Integer(sMinSmallintVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Smallint_In_Null(?)}");
      cstmt.setObject(1, sMinSmallintVal, java.sql.Types.SMALLINT);
      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Min_Val_Query = sqlp.getProperty("Smallint_Query_Null", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();
      sRetVal = "" + rs.getObject(1);

      msg.addOutputMsg(sMinSmallintVal, sRetVal);
      if (sRetVal.trim().equals(sMinSmallintVal.trim())) {
        msg.setMsg(
            "setObject method sets the designated parameter with the object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the object",
            "test setObject failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed !");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject Method is Failed !");

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
        rsSch.dropTab("Smallint_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject34
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set String object for SQL
   * Type INTEGER and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Integer object that is
   * been set. Compare the result with the extracted value from the tssql.stmt
   * file. Both the values should be equal.
   *
   */
  public void testSetObject34() throws Fault {
    ResultSet rs = null;
    String sRetVal = null;
    Integer maxIntegerVal = null;
    String sMaxIntegerVal = null;

    try {
      rsSch.createTab("Integer_Tab", sqlp, conn);

      sMaxIntegerVal = rsSch.extractVal("Integer_Tab", 1, sqlp, conn);

      maxIntegerVal = new Integer(sMaxIntegerVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Integer_In_Min(?)}");
      cstmt.setObject(1, sMaxIntegerVal, java.sql.Types.INTEGER);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Min_Val_Query = sqlp.getProperty("Integer_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();
      sRetVal = "" + rs.getObject(1);

      msg.addOutputMsg(sMaxIntegerVal, sRetVal);
      if (sRetVal.trim().equals(sMaxIntegerVal.trim())) {
        msg.setMsg(
            "setObject method sets the designated parameter with the object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the object",
            "test setObject failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed !");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject Method is Failed !");

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
        rsSch.dropTab("Integer_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject35
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set String object for SQL
   * Type INTEGER and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Integer object that is
   * been set. Compare the result with the extracted value from the tssql.stmt
   * file. Both the values should be equal.
   *
   */
  public void testSetObject35() throws Fault {
    ResultSet rs = null;
    String sRetVal = null;
    Integer minIntegerVal = null;
    String sMinIntegerVal = null;

    try {
      rsSch.createTab("Integer_Tab", sqlp, conn);

      sMinIntegerVal = rsSch.extractVal("Integer_Tab", 2, sqlp, conn);

      minIntegerVal = new Integer(sMinIntegerVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Integer_In_Null(?)}");
      cstmt.setObject(1, sMinIntegerVal, java.sql.Types.INTEGER);
      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Min_Val_Query = sqlp.getProperty("Integer_Query_Null", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();
      sRetVal = "" + rs.getObject(1);
      msg.addOutputMsg(sMinIntegerVal, sRetVal);

      if (sRetVal.trim().equals(sMinIntegerVal.trim())) {
        msg.setMsg(
            "setObject method sets the designated parameter with the object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the object",
            "test setObject failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed !");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject Method is Failed !");

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
        rsSch.dropTab("Integer_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject36
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set String object for SQL
   * Type BIGINT and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Long object that is
   * been set. Compare the result with the extracted value from the tssql.stmt
   * file. Both the values should be equal.
   *
   */
  public void testSetObject36() throws Fault {
    ResultSet rs = null;
    String sRetVal = null;
    Long maxBigintVal = null;
    String sMaxBigintVal = null;

    try {
      rsSch.createTab("Bigint_Tab", sqlp, conn);

      sMaxBigintVal = rsSch.extractVal("Bigint_Tab", 1, sqlp, conn);

      maxBigintVal = new Long(sMaxBigintVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Bigint_In_Min(?)}");
      cstmt.setObject(1, sMaxBigintVal, java.sql.Types.BIGINT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Min_Val_Query = sqlp.getProperty("Bigint_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();
      sRetVal = "" + rs.getObject(1);
      msg.addOutputMsg(sMaxBigintVal, sRetVal);

      if (sRetVal.trim().equals(sMaxBigintVal.trim())) {
        msg.setMsg(
            "setObject method sets the designated parameter with the object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the object",
            "test setObject failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed !");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject Method is Failed !");

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
        rsSch.dropTab("Bigint_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject37
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set String object for SQL
   * Type BIGINT and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Long object that is
   * been set. Compare the result with the extracted value from the tssql.stmt
   * file. Both the values should be equal.
   *
   */
  public void testSetObject37() throws Fault {
    ResultSet rs = null;
    String sRetVal = null;
    Long minBigintVal = null;
    String sMinBigintVal = null;

    try {
      rsSch.createTab("Bigint_Tab", sqlp, conn);

      sMinBigintVal = rsSch.extractVal("Bigint_Tab", 2, sqlp, conn);

      minBigintVal = new Long(sMinBigintVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Bigint_In_Null(?)}");
      cstmt.setObject(1, sMinBigintVal, java.sql.Types.BIGINT);
      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Min_Val_Query = sqlp.getProperty("Bigint_Query_Null", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();
      sRetVal = "" + rs.getObject(1);
      msg.addOutputMsg(sMinBigintVal, sRetVal);

      if (sRetVal.trim().equals(sMinBigintVal.trim())) {
        msg.setMsg(
            "setObject method sets the designated parameter with the object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the object",
            "test setObject failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed !");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject Method is Failed !");

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
        rsSch.dropTab("Bigint_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject38
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set String object for SQL
   * Type REAL and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Float object that is
   * been set. Compare the result with the extracted value from the tssql.stmt
   * file. Both the values should be equal.
   *
   */
  public void testSetObject38() throws Fault {
    ResultSet rs = null;
    String sRetVal = null;
    Float oRetVal = null;
    Float maxRealVal = null;
    String sMaxRealVal = null;

    try {
      rsSch.createTab("Real_Tab", sqlp, conn);

      sMaxRealVal = rsSch.extractVal("Real_Tab", 1, sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Real_In_Min(?)}");
      cstmt.setObject(1, sMaxRealVal, java.sql.Types.REAL);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Min_Val_Query = sqlp.getProperty("Real_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      sRetVal = "" + rs.getObject(1);
      maxRealVal = new Float(sMaxRealVal);
      oRetVal = new Float(sRetVal);

      msg.addOutputMsg(sMaxRealVal, sRetVal);

      if (oRetVal.compareTo(maxRealVal) == 0) {
        msg.setMsg(
            "setObject method sets the designated parameter with the object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the object",
            "test setObject failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed !");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject Method is Failed !");

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
        rsSch.dropTab("Real_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject39
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set String object for SQL
   * Type REAL and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Float object that is
   * been set. Compare the result with the extracted value from the tssql.stmt
   * file. Both the values should be equal.
   *
   */
  public void testSetObject39() throws Fault {
    ResultSet rs = null;
    Float oRetVal = null;
    Float minRealVal = null;
    String sMinRealVal = null;
    String sRetVal = null;

    try {
      rsSch.createTab("Real_Tab", sqlp, conn);

      sMinRealVal = rsSch.extractVal("Real_Tab", 2, sqlp, conn);

      minRealVal = new Float(sMinRealVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Real_In_Null(?)}");
      cstmt.setObject(1, sMinRealVal, java.sql.Types.REAL);
      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Min_Val_Query = sqlp.getProperty("Real_Query_Null", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();
      sRetVal = "" + rs.getObject(1);
      oRetVal = new Float(sRetVal);
      msg.addOutputMsg(sMinRealVal, sRetVal);

      if (oRetVal.equals(minRealVal)) {
        msg.setMsg(
            "setObject method sets the designated parameter with the object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the object",
            "test setObject failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed !");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject Method is Failed !");

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
        rsSch.dropTab("Real_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject40
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set String object for SQL
   * Type FLOAT and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Double object that is
   * been set. Compare the result with the extracted value from the tssql.stmt
   * file. Both the values should be equal.
   *
   */
  public void testSetObject40() throws Fault {
    ResultSet rs = null;
    Float oRetVal = null;
    Float maxFloatVal = null;
    String sMaxFloatVal = null;

    try {
      rsSch.createTab("Float_Tab", sqlp, conn);

      sMaxFloatVal = rsSch.extractVal("Float_Tab", 1, sqlp, conn);

      maxFloatVal = new Float(sMaxFloatVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Float_In_Min(?)}");
      cstmt.setObject(1, sMaxFloatVal, java.sql.Types.FLOAT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Min_Val_Query = sqlp.getProperty("Float_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();
      Object objRetVal = rs.getObject(1);
      oRetVal = new Float(objRetVal.toString());
      msg.addOutputMsg("" + maxFloatVal, "" + oRetVal);

      if (oRetVal.compareTo(maxFloatVal) == 0) {
        msg.setMsg(
            "setObject method sets the designated parameter with the object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the object",
            "test setObject failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed !");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject Method is Failed !");

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
