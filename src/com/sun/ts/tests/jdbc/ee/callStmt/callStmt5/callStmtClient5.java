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
 * @(#)callStmtClient5.java	1.18 03/05/16
 */
package com.sun.ts.tests.jdbc.ee.callStmt.callStmt5;

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
 * The callStmtClient5 class tests methods of DatabaseMetaData interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */
public class callStmtClient5 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.callStmt.callStmt5";

  // Naming specific member variables
  private TSNamingContextInterface jc = null;

  // Harness requirements
  private transient Connection conn = null;

  private rsSchema rsSch = null;

  private JDBCTestMsg msg = null;

  private String drManager = null;

  private Properties sqlp = null;

  private transient DatabaseMetaData dbmd = null;

  private CallableStatement cstmt = null;

  private Properties props = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    callStmtClient5 theTests = new callStmtClient5();
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
   * @testName: testGetObject41
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
   * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType,int scale) method. Execute the stored procedure
   * and call the getObject(int parameterIndex) method to retrieve the minimum
   * value of the parameter from Decimal_Tab. Extract the minimum value from the
   * tssql.stmt file.Compare this value with the value returned by the
   * getObject(int parameterIndex) Both the values should be equal.
   */
  public void testGetObject41() throws Fault {
    try {
      rsSch.createTab("Decimal_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Decimal_Proc(?,?,?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.DECIMAL, 15);
      cstmt.registerOutParameter(2, java.sql.Types.DECIMAL, 15);
      cstmt.registerOutParameter(3, java.sql.Types.DECIMAL, 15);

      msg.setMsg("execute the procedure");
      cstmt.execute();

      msg.setMsg("invoke getObject method");
      BigDecimal oRetVal = (BigDecimal) cstmt.getObject(2);
      String sRetStr = rsSch.extractVal("Decimal_Tab", 2, sqlp, conn);
      BigDecimal oExtVal = new BigDecimal(sRetStr);

      msg.addOutputMsg(sRetStr, oRetVal.toString());
      if ((oRetVal.compareTo(oExtVal) == 0))
        msg.setMsg(
            "getObject returns the Minimum value for type BigDecimal(JDBC DECIMAL) ");
      else {
        msg.printTestError(
            "getObject did not return the Minimum value for type BigDecimal(JDBC DECIMAL) ",
            "test getObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getObject Failed!");

    } finally {
      try {
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Decimal_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject42
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
   * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType,int scale) method. Execute the stored procedure
   * and call the getObject(int parameterIndex) method to retrieve the null
   * value from Decimal_Tab.Check if it returns null
   */
  public void testGetObject42() throws Fault {
    try {
      rsSch.createTab("Decimal_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Decimal_Proc(?,?,?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.DECIMAL, 15);
      cstmt.registerOutParameter(2, java.sql.Types.DECIMAL, 15);
      cstmt.registerOutParameter(3, java.sql.Types.DECIMAL, 15);

      msg.setMsg("execute the procedure");
      cstmt.execute();

      msg.setMsg("invoke getObject method");
      BigDecimal oRetVal = (BigDecimal) cstmt.getObject(3);
      msg.addOutputMsg("null", "" + oRetVal);
      if (oRetVal == null)
        msg.setMsg(
            "getObject returns the Null value for type BigDecimal(JDBC DECIMAL) "
                + oRetVal);
      else {
        msg.printTestError(
            "getObject did not return the Null value for type BigDecimal(JDBC DECIMAL)",
            "test getObject failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getObject Failed!");

    } finally {
      try {
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Decimal_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject43
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
   * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Update the column Binary_Val of the Binary_Tab with a byte array
   * using the PreparedStatement.setBytes(int columnIndex) method.Register the
   * parameter using registerOutParameter(int parameterIndex,int sqlType)
   * method. Execute the stored procedure and call the getObject(int
   * parameterIndex) method to retrieve the byte array. It should return the
   * byte array object that has been set.
   */
  public void testGetObject43() throws Fault {
    String binarySize = null;
    PreparedStatement pstmt = null;
    try {
      // to extract the Binary Table size from property file
      binarySize = props.getProperty("binarySize");
      msg.setMsg("Binary Table Size : " + binarySize);
      rsSch.createTab("Binary_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Binary_Tab_Val_Update", "");
      msg.setMsg("Prepared Statement String :" + sPrepStmt);
      int bytearrsize = Integer.parseInt(binarySize);

      byte[] bytearr = new byte[bytearrsize];
      String sbyteval = null;
      msg.setMsg("getting the bytearray value");
      for (int count = 0; count < bytearrsize; count++) {
        sbyteval = Integer.toString(count % 255);
        bytearr[count] = Byte.parseByte(sbyteval);
      }

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setBytes(1, bytearr);
      pstmt.executeUpdate();

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Binary_Proc(?)}");
      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.BINARY);
      msg.setMsg("execute the procedure");
      cstmt.execute();
      msg.setMsg("invoke getObject method");

      byte[] oRetVal = (byte[]) cstmt.getObject(1);
      for (int i = 0; i < bytearrsize; i++) {
        msg.addOutputMsg(Byte.toString(bytearr[i]), Byte.toString(oRetVal[i]));
        if (oRetVal[i] != bytearr[i]) {
          msg.printTestError(
              "getObject did not return the proper byte array values",
              "test getObject Failed!");

        }
      }
      msg.setMsg("getObject returns the proper byte array values");
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getObject Failed!");

    } finally {
      try {
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Binary_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject44
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
   * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType) method. Execute the stored procedure and call
   * the getObject(int parameterIndex) method to retrieve the null value from
   * Binary_Tab.Check if it returns null
   */
  public void testGetObject44() throws Fault {
    String binarySize = null;
    try {
      // to extract the Binary Table size from property file
      binarySize = props.getProperty("binarySize");
      msg.setMsg("Binary Table Size : " + binarySize);
      rsSch.createTab("Binary_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Binary_Proc(?)}");
      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.BINARY);
      msg.setMsg("execute the procedure");
      cstmt.execute();
      msg.setMsg("invoke getObject method");

      byte[] oRetVal = (byte[]) cstmt.getObject(1);
      msg.addOutputMsg("null", "" + oRetVal);

      if (oRetVal == null)
        msg.setMsg("getObject returns the null value for type byte[]");
      else {
        msg.printTestError(
            "getObject did not return the null value for type byte[]",
            "test getObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getObject Failed!");

    } finally {
      try {
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Binary_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject45
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
   * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Update the column Varbinary_Val of the Varbinary_tab with a byte
   * array using the PreparedStatement.setBytes(int columnIndex) method.Register
   * the parameter using registerOutParameter(int parameterIndex,int sqlType,int
   * scale) method. Execute the stored procedure and call the getObject(int
   * parameterIndex) method to retrieve the byte array. It should return the
   * byte array object that has been set.
   */
  public void testGetObject45() throws Fault {
    String varbinarySize = null;
    PreparedStatement pstmt = null;
    try {
      // to extract the varbinary Table size from property file
      varbinarySize = props.getProperty("varbinarySize");
      msg.setMsg("varbinary Table Size : " + varbinarySize);

      rsSch.createTab("Varbinary_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Varbinary_Tab_Val_Update", "");
      msg.setMsg("Prepared Statement String :" + sPrepStmt);

      int bytearrsize = Integer.parseInt(varbinarySize);
      msg.setMsg("varbinary Size : " + bytearrsize);

      byte[] bytearr = new byte[bytearrsize];
      String sbyteval = null;
      msg.setMsg("to get the bytearray value");
      for (int count = 0; count < bytearrsize; count++) {
        sbyteval = Integer.toString(count % 255);
        bytearr[count] = Byte.parseByte(sbyteval);
      }

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setBytes(1, bytearr);
      pstmt.executeUpdate();

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Varbinary_Proc(?)}");
      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.VARBINARY);
      msg.setMsg("execute the procedure");
      cstmt.execute();

      msg.setMsg("invoke getObject method");
      byte[] oRetVal = (byte[]) cstmt.getObject(1);
      for (int i = 0; i < bytearrsize; i++) {
        msg.addOutputMsg(Byte.toString(bytearr[i]), Byte.toString(oRetVal[i]));
        if (oRetVal[i] != bytearr[i]) {
          msg.printTestError(
              "getObject did not return the proper byte array values",
              "test getObject Failed!");

        }
      }
      msg.setMsg("getObject returns the proper byte array values");
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getObject Failed!");

    } finally {
      try {
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Varbinary_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject46
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
   * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType,int scale) method. Execute the stored procedure
   * and call the getObject(int parameterIndex) method to retrieve the null
   * value from Varbinary_Tab.Check if it returns null
   */
  public void testGetObject46() throws Fault {
    String varbinarySize = null;
    try {
      // to extract the varbinary Table size from property file
      varbinarySize = props.getProperty("varbinarySize");
      msg.setMsg("varbinary Table Size : " + varbinarySize);

      rsSch.createTab("Varbinary_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Varbinary_Proc(?)}");
      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.VARBINARY);

      msg.setMsg("execute the procedure");
      cstmt.execute();

      msg.setMsg("invoke getObject method");
      byte[] oRetVal = (byte[]) cstmt.getObject(1);

      msg.addOutputMsg("null", "" + oRetVal);
      if (oRetVal == null)
        msg.setMsg("getObject returns the null value for type byte[]");
      else {
        msg.printTestError(
            "getObject did not return the null value for type byte[]",
            "test getObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getObject Failed!");

    } finally {
      try {
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Varbinary_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject47
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
   * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Update the column Longvarbinary_Val of the Longvarbinary_Tab with
   * a byte array using the PreparedStatement.setBytes(int columnIndex)
   * method.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType) method. Execute the stored procedure and call
   * the getObject(int parameterIndex) method.to retrieve the byte array. It
   * should return the byte array object that has been set.
   */
  public void testGetObject47() throws Fault {
    String longvarbinarySize = null;
    PreparedStatement pstmt = null;
    try {
      rsSch.createTab("Longvarbinary_Tab", sqlp, conn);
      // to extract the longvarbinary Table size from property file
      longvarbinarySize = props.getProperty("longvarbinarySize");
      msg.setMsg("longvarbinary Table Size : " + longvarbinarySize);

      String sPrepStmt = sqlp.getProperty("Longvarbinary_Tab_Val_Update", "");
      msg.setMsg("Prepared Statement String :" + sPrepStmt);
      int bytearrsize = Integer.parseInt(longvarbinarySize);
      msg.setMsg("longvarbinary Size : " + bytearrsize);

      byte[] bytearr = new byte[bytearrsize];
      String sbyteval = null;
      msg.setMsg("getting the bytearray value");
      for (int count = 0; count < bytearrsize; count++) {
        sbyteval = Integer.toString(count % 255);
        bytearr[count] = Byte.parseByte(sbyteval);
      }

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setBytes(1, bytearr);
      pstmt.executeUpdate();

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Longvarbinary_Proc(?)}");
      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.LONGVARBINARY);

      msg.setMsg("execute the procedure");
      cstmt.execute();

      msg.setMsg("invoke getObject method");
      byte[] oRetVal = (byte[]) cstmt.getObject(1);

      for (int i = 0; i < bytearrsize; i++) {
        msg.addOutputMsg(Byte.toString(bytearr[i]), Byte.toString(oRetVal[i]));
        if (oRetVal[i] != bytearr[i]) {
          msg.printTestError(
              "getObject did not return the proper byte array values",
              "test getObject Failed!");

        }
      }
      msg.setMsg("getObject returns the proper byte array values");
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getObject Failed!");

    } finally {
      try {
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Longvarbinary_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject48
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
   * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType) method. Execute the stored procedure and call
   * the getObject(int parameterIndex) method to retrieve the null value from
   * from Longvarbinary_Tab.Check if it returns null
   */
  public void testGetObject48() throws Fault {
    try {
      rsSch.createTab("Longvarbinary_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Longvarbinary_Proc(?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.LONGVARBINARY);

      msg.setMsg("execute the procedure");
      cstmt.execute();

      msg.setMsg("invoke getObject method");
      byte[] oRetVal = (byte[]) cstmt.getObject(1);

      msg.addOutputMsg("null", "" + oRetVal);
      if (oRetVal == null)
        msg.setMsg("getObject returns the null value for type byte[]");
      else {
        msg.printTestError(
            "getObject did not return the null value for type byte[]",
            "test getObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getObject Failed!");

    } finally {
      try {
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Longvarbinary_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetFloat01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1253;
   * JDBC:JAVADOC:1254; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType). Execute the stored procedure and call the
   * getFloat(int parameterIndex) method to retrieve the maximum value from
   * Real_Tab. Extract the maximum value from the tssql.stmt file.Compare this
   * value with the value returned by the getFloat(int parameterIndex). Both the
   * values should be equal.
   */
  public void testGetFloat01() throws Fault {
    try {
      rsSch.createTab("Real_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Real_Proc(?,?,?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.REAL);
      cstmt.registerOutParameter(2, java.sql.Types.REAL);
      cstmt.registerOutParameter(3, java.sql.Types.REAL);

      msg.setMsg("execute the procedure");
      cstmt.execute();

      msg.setMsg("invoke getFloat method");
      float fRetVal = cstmt.getFloat(1);
      String sRetStr = rsSch.extractVal("Real_Tab", 1, sqlp, conn);
      float fExtVal = Float.parseFloat(sRetStr);

      msg.addOutputMsg(sRetStr, new Float(fExtVal).toString());

      if (fRetVal == fExtVal)
        msg.setMsg("getFloat returns the Maximum Value " + fRetVal);
      else {
        msg.printTestError("getFloat did not return the Maximum Value",
            "test getFloat Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getFloat is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getFloat Failed!");

    } finally {
      try {
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Real_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetFloat02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1253;
   * JDBC:JAVADOC:1254; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType). Execute the stored procedure and call the
   * getFloat(int parameterIndex) method to retrieve the minimum value from
   * Real_Tab. Extract the minimum value from the tssql.stmt file.Compare this
   * value with the value returned by the getFloat(int parameterIndex). Both the
   * values should be equal.
   */
  public void testGetFloat02() throws Fault {
    try {
      rsSch.createTab("Real_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Real_Proc(?,?,?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.REAL);
      cstmt.registerOutParameter(2, java.sql.Types.REAL);
      cstmt.registerOutParameter(3, java.sql.Types.REAL);

      msg.setMsg("execute the procedure");
      cstmt.execute();
      msg.setMsg("invoke getFloat method ");
      float fRetVal = cstmt.getFloat(2);
      String sRetStr = rsSch.extractVal("Real_Tab", 2, sqlp, conn);
      float fExtVal = Float.parseFloat(sRetStr);

      msg.addOutputMsg(sRetStr, new Float(fExtVal).toString());

      if (fRetVal == fExtVal)
        msg.setMsg("getFloat returns the Minimum Value " + fRetVal);
      else {
        msg.printTestError("getFloat did not return the Minimum Value ",
            "test getFloat Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getFloat is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getFloat Failed!");

    } finally {
      try {
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Real_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetFloat03
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1253;
   * JDBC:JAVADOC:1254; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType). Execute the stored procedure and call the
   * getFloat(int parameterIndex) method to retrieve the null value from
   * Real_Tab. Check if it returns null
   */
  public void testGetFloat03() throws Fault {
    try {
      rsSch.createTab("Real_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Real_Proc(?,?,?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.REAL);
      cstmt.registerOutParameter(2, java.sql.Types.REAL);
      cstmt.registerOutParameter(3, java.sql.Types.REAL);

      msg.setMsg("execute the procedure");
      cstmt.execute();

      msg.setMsg("invoke getFloat method");
      float fRetVal = cstmt.getFloat(3);
      msg.addOutputMsg("null", new Float(fRetVal).toString());

      if (cstmt.wasNull())
        msg.setMsg("getFloat returns the Null Value " + fRetVal);
      else {
        msg.printTestError("getFloat did not return the Null Value ",
            "test getFloat Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getFloat is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getFloat Failed!");

    } finally {
      try {
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Real_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetString03
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1241;
   * JDBC:JAVADOC:1242; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType). Execute the stored procedure and call the
   * getString(int parameterIndex) method to retrieve a String value from
   * Varchar_Tab Extract the same String value from the tssql.stmt file.Compare
   * this value with the value returned by the getString(int parameterIndex).
   * Both the values should be equal.
   */
  public void testGetString03() throws Fault {
    try {
      rsSch.createTab("Varchar_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Varchar_Proc(?,?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
      cstmt.registerOutParameter(2, java.sql.Types.VARCHAR);

      msg.setMsg("execute the procedure");
      cstmt.execute();

      msg.setMsg("invoke getString method");
      String oRetVal = (String) cstmt.getString(1);
      String oExtVal = rsSch.extractVal("Varchar_Tab", 1, sqlp, conn);
      oExtVal = oExtVal.trim();
      oRetVal = oRetVal.trim();

      msg.addOutputMsg(oExtVal, oRetVal);
      if (oRetVal.equals(oExtVal.substring(1, oExtVal.length() - 1)))
        msg.setMsg("getString returns the Name ");
      else {
        msg.printTestError("getString did not return the Name ",
            "test getString Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getString Failed!");

    } finally {
      try {
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Varchar_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetString04
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1241;
   * JDBC:JAVADOC:1242; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType). Execute the stored procedure and call the
   * getString(int parameterIndex) method to retrieve the null value from
   * Varchar_Tab. Check if it returns null
   */
  public void testGetString04() throws Fault {
    try {
      rsSch.createTab("Varchar_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Varchar_Proc(?,?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
      cstmt.registerOutParameter(2, java.sql.Types.VARCHAR);

      msg.setMsg("execute the procedure");
      cstmt.execute();
      msg.setMsg(
          "Calling CallableStatement.getString(NullValue(JDBC VARCHAR))");

      msg.setMsg("invoke getString method");
      String oRetVal = (String) cstmt.getString(2);

      msg.addOutputMsg("null", oRetVal);
      if (oRetVal == null)
        msg.setMsg("getString returns the null value ");
      else {
        msg.printTestError("getString did not return the null value",
            "test getString Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getString is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getString Failed!");

    } finally {
      try {
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Varchar_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetString05
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1241;
   * JDBC:JAVADOC:1242; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType). Execute the stored procedure and call the
   * getString(int parameterIndex) method to retrieve a String value from
   * Longvarchar_Tab. Extract the same String value from the tssql.stmt
   * file.Compare this value with the value returned by the getString(int
   * parameterIndex). Both the values should be equal.
   */
  public void testGetString05() throws Fault {
    try {
      rsSch.createTab("Longvarchar_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Longvarchar_Proc(?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.LONGVARCHAR);

      msg.setMsg("execute the procedure");
      cstmt.execute();

      msg.setMsg("invoke getString method");
      String oRetVal = (String) cstmt.getString(1);
      String oExtVal = rsSch.extractVal("Longvarchar_Tab", 1, sqlp, conn);
      oExtVal = oExtVal.trim();
      oRetVal = oRetVal.trim();

      msg.addOutputMsg(oExtVal, oRetVal);
      if (oRetVal.equals(oExtVal.substring(1, oExtVal.length() - 1)))
        msg.setMsg("getString returns the Name " + oRetVal);
      else {
        msg.printTestError("getString did not return the Name",
            "test getString Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getString is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getString Failed!");

    } finally {
      try {
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Longvarchar_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetString06
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1241;
   * JDBC:JAVADOC:1242; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType). Execute the stored procedure and call the
   * getString(int parameterIndex) method to retrieve the null value from
   * Longvarchar_Tab. Check if it returns null
   */
  public void testGetString06() throws Fault {
    try {
      rsSch.createTab("Longvarcharnull_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Lvarcharnull_Proc(?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.LONGVARCHAR);

      msg.setMsg("execute the procedure");
      cstmt.execute();
      msg.setMsg(
          "Calling CallableStatement.getString(NullValue(JDBC LONGVARCHAR))");

      msg.setMsg("invoke getString method");
      String oRetVal = (String) cstmt.getString(1);
      msg.addOutputMsg("null", oRetVal);
      if (oRetVal == null)
        msg.setMsg("getString returns the null value ");
      else {
        msg.printTestError("getString did not return the null value",
            "test getString Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getString is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getString Failed!");

    } finally {
      try {
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Longvarcharnull_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetBigDecimal04
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1269;
   * JDBC:JAVADOC:1270; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType,int scale). Execute the stored procedure and
   * call the getBigDecimal(int parameterIndex) method.to retrieve the maximum
   * value from Decimal_Tab. Extract the maximum value from the tssql.stmt
   * file.Compare this value with the value returned by the getBigDecimal(int
   * parameterIndex).Both the values should be equal.
   */
  public void testGetBigDecimal04() throws Fault {
    try {
      rsSch.createTab("Decimal_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Decimal_Proc(?,?,?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.DECIMAL, 15);
      cstmt.registerOutParameter(2, java.sql.Types.DECIMAL, 15);
      cstmt.registerOutParameter(3, java.sql.Types.DECIMAL, 15);

      msg.setMsg("execute the procedure");
      cstmt.execute();

      msg.setMsg("invoke getBigDecimal method");
      BigDecimal oRetVal = cstmt.getBigDecimal(1);
      String sRetStr = rsSch.extractVal("Decimal_Tab", 1, sqlp, conn);
      BigDecimal oExtVal = new BigDecimal(sRetStr);

      msg.addOutputMsg(sRetStr, oRetVal.toString());
      if ((oRetVal.compareTo(oExtVal) == 0))
        msg.setMsg("getBigDecimal returns the Maximum value " + oRetVal);
      else {
        msg.printTestError("getBigDecimal did not return the Maximum value",
            "test getBigDecimal Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getBigDecimal is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getBigDecimal Failed!");

    } finally {
      try {
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Decimal_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetBigDecimal05
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1269;
   * JDBC:JAVADOC:1270; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType,int scale). Execute the stored procedure and
   * call the getBigDecimal(int parameterIndex) method to retrieve the minimum
   * value from Decimal_Tab. Extract the minimum value from the tssql.stmt
   * file.Compare this value with the value returned by the getBigDecimal(int
   * parameterIndex).Both the values should be equal.
   */
  public void testGetBigDecimal05() throws Fault {
    try {
      rsSch.createTab("Decimal_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Decimal_Proc(?,?,?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.DECIMAL, 15);
      cstmt.registerOutParameter(2, java.sql.Types.DECIMAL, 15);
      cstmt.registerOutParameter(3, java.sql.Types.DECIMAL, 15);

      msg.setMsg("execute the procedure");
      cstmt.execute();

      msg.setMsg("invoke getBigDecimal method");
      BigDecimal oRetVal = cstmt.getBigDecimal(2);
      String sRetStr = rsSch.extractVal("Decimal_Tab", 2, sqlp, conn);
      BigDecimal oExtVal = new BigDecimal(sRetStr);

      msg.addOutputMsg(sRetStr, oRetVal.toString());
      if ((oRetVal.compareTo(oExtVal) == 0))
        msg.setMsg("getBigDecimal returns the Minimum value " + oRetVal);
      else {
        msg.printTestError("getBigDecimal did not return the Minimum value",
            "test getBigDecimal Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getBigDecimal is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getBigDecimal Failed!");

    } finally {
      try {
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Decimal_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetBigDecimal06
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1269;
   * JDBC:JAVADOC:1270; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType, int scale). Execute the stored procedure and
   * call the getBigDecimal(int parameterIndex) method to retrieve the null
   * value from Decimal_Tab.Check if it returns null
   */
  public void testGetBigDecimal06() throws Fault {
    try {
      rsSch.createTab("Decimal_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Decimal_Proc(?,?,?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.DECIMAL, 15);
      cstmt.registerOutParameter(2, java.sql.Types.DECIMAL, 15);
      cstmt.registerOutParameter(3, java.sql.Types.DECIMAL, 15);

      msg.setMsg("execute the procedure");
      cstmt.execute();
      msg.setMsg("invoke getBigDecimal method");
      BigDecimal oRetVal = cstmt.getBigDecimal(3);

      msg.addOutputMsg("null", "" + oRetVal);
      if (oRetVal == null)
        msg.setMsg("getBigDecimal returns the Null value " + oRetVal);
      else {
        msg.printTestError("getBigDecimal did not return the Null value",
            "test getBigDecimal Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getBigDecimal is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getBigDecimal Failed!");

    } finally {
      try {
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Decimal_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetBytes01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1259;
   * JDBC:JAVADOC:1260; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType). Update the column Binary_Val of the Binary
   * with a byte array using the PreparedStatement.setBytes(int columnIndex)
   * method.Execute the stored procedure and call the getBytes(int
   * parameterIndex) method to retrieve the byte array. It should return the
   * byte array object that has been set.
   */
  public void testGetBytes01() throws Fault {
    String binarySize = null;
    PreparedStatement pstmt = null;
    try {
      rsSch.createTab("Binary_Tab", sqlp, conn);
      binarySize = props.getProperty("binarySize");

      String sPrepStmt = sqlp.getProperty("Binary_Tab_Val_Update", "");
      msg.setMsg("Prepared Statement String :" + sPrepStmt);

      int bytearrsize = Integer.parseInt(binarySize);
      msg.setMsg("Binary Size : " + bytearrsize);

      byte[] bytearr = new byte[bytearrsize];
      String sbyteval = null;
      msg.setMsg("getting the bytearray value");
      for (int count = 0; count < bytearrsize; count++) {
        sbyteval = Integer.toString(count % 255);
        bytearr[count] = Byte.parseByte(sbyteval);
      }

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setBytes(1, bytearr);
      pstmt.executeUpdate();

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Binary_Proc(?)}");
      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.BINARY);
      msg.setMsg("execute the procedure");
      cstmt.execute();

      msg.setMsg("invoke getBytes method");
      byte[] oRetVal = cstmt.getBytes(1);
      for (int i = 0; i < bytearrsize; i++) {
        msg.addOutputMsg(Byte.toString(bytearr[i]), Byte.toString(oRetVal[i]));
        if (oRetVal[i] != bytearr[i]) {
          msg.printTestError(
              "getBytes did not return the proper byte array values",
              "test getBytes Failed!");

        }
      }
      msg.setMsg("getBytes returns the proper byte array values");
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getBytes is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getBytes Failed!");

    } finally {
      try {
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Binary_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetBytes02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1259;
   * JDBC:JAVADOC:1260; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType). Execute the stored procedure and call the
   * getBytes(int parameterIndex) method to retrieve the null value from
   * Binary_Tab. Check if it returns null.
   */
  public void testGetBytes02() throws Fault {
    String binarySize = null;
    try {
      // to extract the Binary Table size from property file
      binarySize = props.getProperty("binarySize");
      msg.setMsg("Binary Table Size : " + binarySize);

      rsSch.createTab("Binary_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Binary_Proc(?)}");
      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.BINARY);

      msg.setMsg("execute the procedure");
      cstmt.execute();
      msg.setMsg("invoke getBytes method");
      byte[] oRetVal = cstmt.getBytes(1);

      msg.addOutputMsg("null", "" + oRetVal);
      if (oRetVal == null)
        msg.setMsg("getBytes returns the null value");
      else {
        msg.printTestError("getBytes did not return the null value",
            "test getBytes Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getBytes is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getBytes Failed!");

    } finally {
      try {
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Binary_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetBytes03
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1259;
   * JDBC:JAVADOC:1260; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType). Update the column Varbinary_Val of the
   * Varbinary_Tab with a byte array using the PreparedStatement.setBytes(int
   * columnIndex) method.Execute the stored procedure and call the getBytes(int
   * parameterIndex) method.to retrieve the byte array. It should return the
   * byte array object that has been set.
   */
  public void testGetBytes03() throws Fault {
    String varbinarySize = null;
    PreparedStatement pstmt = null;

    try {
      // to extract the varbinary Table size from property file
      varbinarySize = props.getProperty("varbinarySize");
      msg.setMsg("varbinary Table Size : " + varbinarySize);

      rsSch.createTab("Varbinary_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Varbinary_Tab_Val_Update", "");
      msg.setMsg("Prepared Statement String :" + sPrepStmt);

      int bytearrsize = Integer.parseInt(varbinarySize);
      msg.setMsg("varbinary Size : " + bytearrsize);

      byte[] bytearr = new byte[bytearrsize];
      String sbyteval = null;
      msg.setMsg("to get the bytearray value");
      for (int count = 0; count < bytearrsize; count++) {
        sbyteval = Integer.toString(count % 255);
        bytearr[count] = Byte.parseByte(sbyteval);
      }

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setBytes(1, bytearr);
      pstmt.executeUpdate();

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Varbinary_Proc(?)}");
      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.VARBINARY);

      msg.setMsg("execute the procedure");
      cstmt.execute();
      msg.setMsg("invoke getBytes method");
      byte[] oRetVal = cstmt.getBytes(1);
      for (int i = 0; i < bytearrsize; i++) {
        msg.addOutputMsg(Byte.toString(bytearr[i]), Byte.toString(oRetVal[i]));
        if (oRetVal[i] != bytearr[i]) {
          msg.printTestError(
              "getBytes did not return the proper byte array values",
              "test getBytes Failed!");

        }
      }
      msg.setMsg("getBytes returns the proper byte array values");
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getBytes is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getBytes Failed!");

    } finally {
      try {
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Varbinary_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetBytes04
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1259;
   * JDBC:JAVADOC:1260; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType). Execute the stored procedure and call the
   * getBytes(int parameterIndex) method.to retrieve the null value from
   * Varbinary_Tab. Check if it returns null.
   */
  public void testGetBytes04() throws Fault {
    String varbinarySize = null;
    try {
      // to extract the varbinary Table size from property file
      varbinarySize = props.getProperty("varbinarySize");
      msg.setMsg("varbinary Table Size : " + varbinarySize);

      rsSch.createTab("Varbinary_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Varbinary_Proc(?)}");
      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.VARBINARY);
      msg.setMsg("execute the procedure");
      cstmt.execute();
      msg.setMsg("invoke getObject method");
      byte[] oRetVal = cstmt.getBytes(1);

      msg.addOutputMsg("null", "" + oRetVal);
      if (oRetVal == null)
        msg.setMsg("getBytes returns the null value");
      else {
        msg.printTestError("getBytes did not return the null value",
            "test getBytes Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getBytes is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getBytes Failed!");

    } finally {
      try {
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Varbinary_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetBytes05
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1259;
   * JDBC:JAVADOC:1260; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType). Update the column Longvarbinary_Val of the
   * Longvarbinary_Tab with a byte array using the
   * PreparedStatement.setBytes(int columnIndex) method.Execute the stored
   * procedure and call the getBytes(int parameterIndex) method.to retrieve the
   * byte array. It should return the byte array object that has been set.
   */
  public void testGetBytes05() throws Fault {
    String longvarbinarySize = null;
    PreparedStatement pstmt = null;
    try {
      rsSch.createTab("Longvarbinary_Tab", sqlp, conn);
      // to extract the longvarbinary Table size from property file
      longvarbinarySize = props.getProperty("longvarbinarySize");
      msg.setMsg("longvarbinary Table Size : " + longvarbinarySize);

      String sPrepStmt = sqlp.getProperty("Longvarbinary_Tab_Val_Update", "");
      msg.setMsg("Prepared Statement String :" + sPrepStmt);
      int bytearrsize = Integer.parseInt(longvarbinarySize);
      msg.setMsg("varbinary Size : " + bytearrsize);

      byte[] bytearr = new byte[bytearrsize];
      String sbyteval = null;
      msg.setMsg("getting the bytearray value");
      for (int count = 0; count < bytearrsize; count++) {
        sbyteval = Integer.toString(count % 255);
        bytearr[count] = Byte.parseByte(sbyteval);
      }

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setBytes(1, bytearr);
      pstmt.executeUpdate();

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Longvarbinary_Proc(?)}");
      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.LONGVARBINARY);

      msg.setMsg("execute the procedure");
      cstmt.execute();
      msg.setMsg("invoke getBytes method");
      byte[] oRetVal = cstmt.getBytes(1);
      for (int i = 0; i < bytearrsize; i++) {
        msg.addOutputMsg(Byte.toString(bytearr[i]), Byte.toString(oRetVal[i]));
        if (oRetVal[i] != bytearr[i]) {
          msg.printTestError(
              "getBytes did not return the proper byte array values",
              "test getBytes Failed!");

        }
      }
      msg.setMsg("getBytes returns the proper byte array values");
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getBytes is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getBytes Failed!");

    } finally {
      try {
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Longvarbinary_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetBytes06
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1259;
   * JDBC:JAVADOC:1260; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType). Execute the stored procedure and call the
   * getBytes(int parameterIndex) method.to retrieve the null value from
   * Longvarbinary_Tab.Check if it returns null
   *
   */
  public void testGetBytes06() throws Fault {
    try {
      rsSch.createTab("Longvarbinary_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Longvarbinary_Proc(?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.LONGVARBINARY);

      msg.setMsg("execute the procedure");
      cstmt.execute();

      msg.setMsg("invoke getBytes method");
      byte[] oRetVal = cstmt.getBytes(1);

      msg.addOutputMsg("null", "" + oRetVal);
      if (oRetVal == null)
        msg.setMsg("getBytes returns the null value");
      else {
        msg.printTestError("getBytes did not return the null value",
            "test getBytes Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getBytes is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getBytes Failed!");

    } finally {
      try {
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Longvarbinary_Tab", conn);
      } catch (Exception e) {
      }
      ;
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
