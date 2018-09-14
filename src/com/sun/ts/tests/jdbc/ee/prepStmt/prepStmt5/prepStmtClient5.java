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
 * @(#)prepStmtClient5.java	1.18 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.prepStmt.prepStmt5;

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
//Merant DataSource class
//import com.merant.sequelink.jdbcx.datasource.*;

/**
 * The prepStmtClient5 class tests methods of PreparedStatement interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.8, 11/24/00
 */

public class prepStmtClient5 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.prepStmt.prepStmt5";

  // Naming specific member variables
  private TSNamingContextInterface jc = null;

  // Harness requirements
  private transient Connection conn = null;

  private ResultSet rs = null;

  private Statement stmt = null;

  private PreparedStatement pstmt = null;

  private transient DatabaseMetaData dbmd = null;

  private DataSource ds1 = null;

  private dbSchema dbSch = null;

  private rsSchema rsSch = null;

  private String drManager = null;

  private Properties sqlp = null;

  private Properties props = null;

  private JDBCTestMsg msg = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    prepStmtClient5 theTests = new prepStmtClient5();
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
         * sqlp = new Properties(); String sqlStmt= p.getProperty("rsQuery","");
         * InputStream istr= new FileInputStream(sqlStmt); sqlp.load(istr);
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
        dbmd = conn.getMetaData();
        rsSch = new rsSchema();
        msg = new JDBCTestMsg();
      } catch (SQLException ex) {
        logErr("SQL Exception: : " + ex.getMessage(), ex);
      }
    } catch (Exception e) {
      logErr("Setup Failed!");
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * @testName: testSetNull12
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:656; JDBC:JAVADOC:657;
   * JDBC:JAVADOC:12;JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. execute the precompiled SQL Statement to set the value as SQL
   * Null for Varchar Type and retrieve the same value by executing a query.
   * Call the ResultSet.wasNull() method to check it. It should return a true
   * value.
   *
   */

  public void testSetNull12() throws Fault {
    boolean nullFlag;
    String rStringVal = null;

    try {
      rsSch.createTab("Varchar_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Varchar_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setNull(1, java.sql.Types.VARCHAR);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Varchar_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rStringVal = rs.getString(1);
      nullFlag = rs.wasNull();
      msg.addOutputMsg("true", "" + nullFlag);

      if (nullFlag) {
        msg.setMsg(
            "setNull Method sets the designated parameter to a SQL Null");
      } else {
        msg.printTestError(
            "set Null Method does not set the designated parameter to a SQL Null ",
            "Call to setNull Method is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setNull Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setNull Method is Failed!");

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
   * @testName: testSetNull13
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:656; JDBC:JAVADOC:657;
   * JDBC:JAVADOC:13; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. execute the precompiled SQL Statement to set the value as SQL
   * Null for LONGVARCHAR Type and retrieve the same value by executing a query.
   * Call the ResultSet.wasNull() method to check it. It should return a true
   * value.
   *
   */

  public void testSetNull13() throws Fault {
    boolean nullFlag;
    String rStringVal = null;

    try {
      rsSch.createTab("Longvarcharnull_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Longvarchar_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setNull(1, java.sql.Types.LONGVARCHAR);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Longvarchar_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rStringVal = rs.getString(1);
      nullFlag = rs.wasNull();

      msg.addOutputMsg("true", "" + nullFlag);
      if (nullFlag) {
        msg.setMsg(
            "setNull Method sets the designated parameter to a SQL Null");
      } else {
        msg.printTestError(
            "set Null Method does not set the designated parameter to a SQL Null ",
            "Call to setNull Method is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setNull Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setNull Method is Failed!");

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
   * @testName: testSetNull14
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:656; JDBC:JAVADOC:657;
   * JDBC:JAVADOC:7; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. execute the precompiled SQL Statement to set the value as SQL
   * Null for REAL Type and retrieve the same value by executing a query. Call
   * the ResultSet.wasNull() method to check it. It should return a true value.
   *
   */

  public void testSetNull14() throws Fault {
    boolean nullFlag;
    float rFloatVal = 0;

    try {
      rsSch.createTab("Real_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Real_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setNull(1, java.sql.Types.REAL);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Real_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rFloatVal = rs.getFloat(1);
      nullFlag = rs.wasNull();

      msg.addOutputMsg("true", "" + nullFlag);
      if (nullFlag) {
        msg.setMsg(
            "setNull Method sets the designated parameter to a SQL Null");
      } else {
        msg.printTestError(
            "set Null Method does not set the designated parameter to a SQL Null ",
            "Call to setNull Method is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setNull is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setNull is Failed!");

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
   * @testName: testSetNull15
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:656; JDBC:JAVADOC:657;
   * JDBC:JAVADOC:10; JDBC:JAVADOC:454; JDBC:JAVADOC:455; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. execute the precompiled SQL Statement to set the value as SQL
   * Null for DECIMAL Type and retrieve the same value by executing a query.
   * Call the ResultSet.wasNull() method to check it. It should return a true
   * value.
   *
   */

  public void testSetNull15() throws Fault {
    boolean nullFlag;
    BigDecimal rBigDecimalVal = null;

    try {
      rsSch.createTab("Decimal_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Decimal_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setNull(1, java.sql.Types.DECIMAL);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Decimal_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rBigDecimalVal = rs.getBigDecimal(1);
      nullFlag = rs.wasNull();

      msg.addOutputMsg("true", "" + nullFlag);
      if (nullFlag) {
        msg.setMsg(
            "setNull Method sets the designated parameter to a SQL Null");
      } else {
        msg.printTestError(
            "set Null Method does not set the designated parameter to a SQL Null ",
            "Call to setNull is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setNull is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setNull is Failed!");

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
   * @testName: testSetNull16
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:656; JDBC:JAVADOC:657;
   * JDBC:JAVADOC:17; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. execute the precompiled SQL Statement to set the value as SQL
   * Null for BINARY Type and retrieve the same value by executing a query. Call
   * the ResultSet.wasNull() method to check it. It should return a true value.
   *
   */

  public void testSetNull16() throws Fault {
    byte retByteArr[] = null;
    String binarySize = null;

    try {
      rsSch.createTab("Binary_Tab", sqlp, conn);

      // to extract the Binary Table size from property file
      binarySize = props.getProperty("binarySize");
      msg.setMsg("Binary Table Size : " + binarySize);

      String sPrepStmt = sqlp.getProperty("Binary_Tab_Val_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      int bytearrsize = Integer.parseInt(binarySize);
      msg.setMsg("Binary Size : " + bytearrsize);

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

      // to update the Column with the Null
      pstmt.setNull(1, java.sql.Types.BINARY);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Binary_Val_Query = sqlp.getProperty("Binary_Query_Val", "");
      msg.setMsg(Binary_Val_Query);
      rs = stmt.executeQuery(Binary_Val_Query);
      rs.next();

      retByteArr = (byte[]) rs.getBytes(1);

      if (retByteArr == null) {
        msg.setMsg(
            "setNull Method sets the designated parameter to a SQL Null ");
      } else {
        msg.printTestError(
            "set Null Method does not set the designated parameter to a SQL Null ",
            "Call to setNull is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setNull is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setNull is Failed!");

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
        rsSch.dropTab("Binary_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetNull17
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:656; JDBC:JAVADOC:657;
   * JDBC:JAVADOC:18; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. execute the precompiled SQL Statement to set the value as SQL
   * Null for VARBINARY Type and retrieve the same value by executing a query.
   * Call the ResultSet.wasNull() method to check it. It should return a true
   * value.
   *
   */

  public void testSetNull17() throws Fault {
    byte retByteArr[] = null;
    String varbinarySize = null;

    try {
      rsSch.createTab("Varbinary_Tab", sqlp, conn);

      // to extract the Varbinary Table size from property file
      varbinarySize = props.getProperty("varbinarySize");
      msg.setMsg("Varbinary Table Size : " + varbinarySize);

      String sPrepStmt = sqlp.getProperty("Varbinary_Tab_Val_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      int bytearrsize = Integer.parseInt(varbinarySize);
      msg.setMsg("Varbinary Size : " + bytearrsize);

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

      // to update the Column with the Null
      pstmt.setNull(1, java.sql.Types.VARBINARY);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Varbinary_Val_Query = sqlp.getProperty("Varbinary_Query_Val", "");
      msg.setMsg(Varbinary_Val_Query);
      rs = stmt.executeQuery(Varbinary_Val_Query);
      rs.next();

      retByteArr = (byte[]) rs.getBytes(1);

      if (retByteArr == null) {
        msg.setMsg(
            "setNull Method sets the designated parameter to a SQL Null ");
      } else {
        msg.printTestError(
            "set Null Method does not set the designated parameter to a SQL Null ",
            "Call to setNull is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setNull is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setNull is Failed!");

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
        rsSch.dropTab("Varbinary_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetNull18
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:656; JDBC:JAVADOC:657;
   * JDBC:JAVADOC:19; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. execute the precompiled SQL Statement to set the value as SQL
   * Null for LONGVARBINARY Type and retrieve the same value by executing a
   * query. Call the ResultSet.wasNull() method to check it. It should return a
   * true value.
   *
   */

  public void testSetNull18() throws Fault {
    byte retByteArr[] = null;
    boolean byteArrFlag = false;

    try {
      rsSch.createTab("Longvarbinary_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Longvarbinary_Tab_Val_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      // to get the byte array value to be updated
      String binsize = props.getProperty("longvarbinarySize");
      int bytearrsize = Integer.parseInt(binsize);
      msg.setMsg("Longvarbinary Size : " + bytearrsize);

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

      // to set the Longvarbinary Column as SQL Null
      pstmt.setNull(1, java.sql.Types.LONGVARBINARY);
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

      if (retByteArr == null) {
        msg.setMsg(
            "setNull Method sets the designated parameter to a SQL Null ");
      } else {
        msg.printTestError(
            "set Null Method does not set the designated parameter to a SQL Null ",
            "Call to setNull Method is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setNull Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setNull Method is Failed!");

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
   * @testName: testSetObject30
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Min_Val with the maximum value of Tinyint_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the maximum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnno) method. Both the values should be equal.
   */

  public void testSetObject30() throws Fault {
    Integer maxTinyintVal = null;
    String maxStringVal = null;
    Integer rTinyintVal = null;
    String smaxStringVal = null;

    try {
      // to create the Tinyint Table
      rsSch.createTab("Tinyint_Tab", sqlp, conn);

      // to update Minimum value column with maximum value
      String sPrepStmt = sqlp.getProperty("Tinyint_Tab_Min_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Maximum Value of Tinyint to be Updated");
      smaxStringVal = rsSch.extractVal("Tinyint_Tab", 1, sqlp, conn);

      maxStringVal = new String(smaxStringVal);
      maxTinyintVal = new Integer(smaxStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxStringVal, java.sql.Types.TINYINT);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Min_Val_Query = sqlp.getProperty("Tinyint_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      Object orTinyintVal = rs.getObject(1);

      msg.addOutputMsg(smaxStringVal, orTinyintVal.toString());

      if ((orTinyintVal.toString().trim()).equals(smaxStringVal.trim())) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "set Object Method does not set the designated parameter with the Object",
            "Call to setObject Method is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject Method is Failed!");

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
        rsSch.dropTab("Tinyint_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject31
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_Val with the minimum value of Smallint_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the minimum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnno) method. Both the values should be equal.
   */

  public void testSetObject31() throws Fault {
    Integer minTinyintVal = null;
    String minStringVal = null;
    Integer rTinyintVal = null;
    String sminStringVal = null;

    try {
      // to create the Tinyint Table
      rsSch.createTab("Tinyint_Tab", sqlp, conn);

      // to update Null value column with Minimum value
      String sPrepStmt = sqlp.getProperty("Tinyint_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Minimum Value of Tinyint to be Updated ");
      sminStringVal = rsSch.extractVal("Tinyint_Tab", 2, sqlp, conn);

      minStringVal = new String(sminStringVal);
      minTinyintVal = new Integer(minStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, minStringVal, java.sql.Types.TINYINT);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Tinyint_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      Object orTinyintVal = rs.getObject(1);

      msg.addOutputMsg(sminStringVal, orTinyintVal.toString());

      if (orTinyintVal.toString().trim().equals(sminStringVal.trim())) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "set Object Method does not set the designated parameter with the Object",
            "Call to setObject Method is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject Method is Failed!");

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
        rsSch.dropTab("Tinyint_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject32
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Min_Val with the maximum value of Smallint_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the maximum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnno) method. Both the values should be equal.
   */

  public void testSetObject32() throws Fault {
    Integer maxSmallintVal = null;
    String maxStringVal = null;
    Integer rSmallintVal = null;
    String smaxStringVal = null;

    try {
      // to create the Smallint Table
      rsSch.createTab("Smallint_Tab", sqlp, conn);

      // to update Minimum value column with maximum
      String sPrepStmt = sqlp.getProperty("Smallint_Tab_Min_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Maximum Value of Smallint to be Updated");
      smaxStringVal = rsSch.extractVal("Smallint_Tab", 1, sqlp, conn);

      maxStringVal = new String(smaxStringVal);
      maxSmallintVal = new Integer(smaxStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxStringVal, java.sql.Types.SMALLINT);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Min_Val_Query = sqlp.getProperty("Smallint_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      Object orSmallintVal = rs.getObject(1);

      msg.addOutputMsg(smaxStringVal, orSmallintVal.toString());
      if (orSmallintVal.toString().trim().equals(smaxStringVal.trim())) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "set Object Method does not set the designated parameter with the Object",
            "Call to setObject Method is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject Method is Failed!");

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
        rsSch.dropTab("Smallint_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject33
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_Val with the minimum value of Smallint_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the minimum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnno) method. Both the values should be equal.
   */

  public void testSetObject33() throws Fault {
    Integer minSmallintVal = null;
    String minStringVal = null;
    Integer rSmallintVal = null;
    String sminStringVal = null;

    try {
      // to create the Smallint Table
      rsSch.createTab("Smallint_Tab", sqlp, conn);

      // to update Null value column with Minimum value
      String sPrepStmt = sqlp.getProperty("Smallint_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Minimum Value of Smallint to be Updated ");
      sminStringVal = rsSch.extractVal("Smallint_Tab", 2, sqlp, conn);

      minStringVal = new String(sminStringVal);
      minSmallintVal = new Integer(sminStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, minStringVal, java.sql.Types.SMALLINT);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Smallint_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      Object orSmallintVal = rs.getObject(1);

      msg.addOutputMsg(sminStringVal, orSmallintVal.toString());
      if (orSmallintVal.toString().trim().equals(sminStringVal.trim())) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "set Object Method does not set the designated parameter with the Object",
            "Call to setObject Method is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject Method is Failed!");

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
        rsSch.dropTab("Smallint_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject34
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Min_Val with the maximum value of Integer_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the maximum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnno) method. Both the values should be equal.
   */

  public void testSetObject34() throws Fault {

    Integer maxIntegerVal = null;
    String maxStringVal = null;
    Integer rIntegerVal = null;
    String smaxStringVal = null;

    try {
      // to create the Integer Table
      rsSch.createTab("Integer_Tab", sqlp, conn);

      // to update Minimum value column with Maximum value
      String sPrepStmt = sqlp.getProperty("Integer_Tab_Min_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Maximum Value of Integer to be Updated");
      smaxStringVal = rsSch.extractVal("Integer_Tab", 1, sqlp, conn);

      maxStringVal = new String(smaxStringVal);
      maxIntegerVal = new Integer(smaxStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxStringVal, java.sql.Types.INTEGER);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Min_Val_Query = sqlp.getProperty("Integer_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      Object orIntegerVal = rs.getObject(1);

      msg.addOutputMsg(smaxStringVal, orIntegerVal.toString());
      if (orIntegerVal.toString().trim().equals(smaxStringVal.trim())) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "set Object Method does not set the designated parameter with the Object",
            "Call to setObject Method is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject Method is Failed!");

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
        rsSch.dropTab("Integer_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject35
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_Val with the minimum value of Integer_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the minimum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnno) method. Both the values should be equal.
   */

  public void testSetObject35() throws Fault {
    Integer minIntegerVal = null;
    String minStringVal = null;
    Integer rIntegerVal = null;
    String sminStringVal = null;

    try {
      // to create the Integer Table
      rsSch.createTab("Integer_Tab", sqlp, conn);

      // to update Null value column with Minimum value
      String sPrepStmt = sqlp.getProperty("Integer_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Minimum Value of Integer to be Updated");
      sminStringVal = rsSch.extractVal("Integer_Tab", 2, sqlp, conn);

      minStringVal = new String(sminStringVal);
      minIntegerVal = new Integer(sminStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, minStringVal, java.sql.Types.INTEGER);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Integer_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      Object orIntegerVal = rs.getObject(1);

      msg.addOutputMsg(sminStringVal, orIntegerVal.toString());
      if (orIntegerVal.toString().trim().equals(sminStringVal.trim())) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "set Object Method does not set the designated parameter",
            "Call to setObject Method is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject Method is Failed!");

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
        rsSch.dropTab("Integer_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject36
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Min_Val with the maximum value of Bigint_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the maximum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnno) method. Both the values should be equal.
   */

  public void testSetObject36() throws Fault {
    Long maxBigintVal = null;
    String maxStringVal = null;
    Long rBigintVal = null;
    String smaxStringVal = null;

    try {
      // to create the Bigint Table
      rsSch.createTab("Bigint_Tab", sqlp, conn);

      // to update Minimum value column with Maximum value
      String sPrepStmt = sqlp.getProperty("Bigint_Tab_Min_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Maximum Value of Bigint to be Updated");
      smaxStringVal = rsSch.extractVal("Bigint_Tab", 1, sqlp, conn);

      maxStringVal = new String(smaxStringVal);
      maxBigintVal = new Long(smaxStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxStringVal, java.sql.Types.BIGINT);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Min_Val_Query = sqlp.getProperty("Bigint_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      Object orBigintVal = rs.getObject(1);

      msg.addOutputMsg(smaxStringVal, orBigintVal.toString());
      if (orBigintVal.toString().trim().equals(smaxStringVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "set Object Method does not set the designated parameter with the Object",
            "Call to setObject Method is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject Method is Failed!");

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
        rsSch.dropTab("Bigint_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject37
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_Val with the minimum value of Bigint_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the minimum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnno) method. Both the values should be equal.
   */

  public void testSetObject37() throws Fault {
    Long minBigintVal = null;
    String minStringVal = null;
    Long rBigintVal = null;
    String sminStringVal = null;
    try {
      // to create the Bigint Table
      rsSch.createTab("Bigint_Tab", sqlp, conn);

      // to update Null value column with Minimum value
      String sPrepStmt = sqlp.getProperty("Bigint_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Minimum Value of Bigint to be Updated ");
      sminStringVal = rsSch.extractVal("Bigint_Tab", 2, sqlp, conn);

      minStringVal = new String(sminStringVal);
      minBigintVal = new Long(sminStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, minStringVal, java.sql.Types.BIGINT);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Bigint_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      Object orBigintVal = rs.getObject(1);

      msg.addOutputMsg(sminStringVal, orBigintVal.toString());
      if (orBigintVal.toString().trim().equals(sminStringVal.trim())) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "set Object Method does not set the designated parameter with the Object",
            "Call to setObject Method is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject Method is Failed!");

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
        rsSch.dropTab("Bigint_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject38
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Min_Val with the maximum value of Real_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the maximum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnno) method. Both the values should be equal.
   */

  public void testSetObject38() throws Fault {
    Float maxRealVal = null;
    String maxStringVal = null;
    Float rRealVal = null;
    String smaxStringVal = null;

    try {
      // to create the Real Table
      rsSch.createTab("Real_Tab", sqlp, conn);

      // to update Minimum value column with Maximum value
      String sPrepStmt = sqlp.getProperty("Real_Tab_Min_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Maximum Value of Real to be Updated ");
      smaxStringVal = rsSch.extractVal("Real_Tab", 1, sqlp, conn);

      maxStringVal = new String(smaxStringVal);
      maxRealVal = new Float(smaxStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxStringVal, java.sql.Types.REAL);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Min_Val_Query = sqlp.getProperty("Real_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      Object orRealVal = rs.getObject(1);
      rRealVal = new Float(orRealVal.toString());

      msg.addOutputMsg("" + maxRealVal, "" + rRealVal);

      if (rRealVal.equals(maxRealVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "set Object Method does not set the designated parameter with the Object",
            "Call to setObject Method is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject Method is Failed!");

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
   * @testName: testSetObject39
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_Val with the minimum value of Real_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the minimum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnno) method. Both the values should be equal.
   */

  public void testSetObject39() throws Fault {
    Float minRealVal = null;
    String minStringVal = null;
    Float rRealVal = null;
    String sminStringVal = null;

    try {
      // to create the Real Table
      rsSch.createTab("Real_Tab", sqlp, conn);

      // to update Null value column with Minimum value
      String sPrepStmt = sqlp.getProperty("Real_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Minimum Value of Real to be Updated ");
      sminStringVal = rsSch.extractVal("Real_Tab", 2, sqlp, conn);

      minStringVal = new String(sminStringVal);
      minRealVal = new Float(sminStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, minStringVal, java.sql.Types.REAL);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Real_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      Object orRealVal = rs.getObject(1);
      rRealVal = new Float(orRealVal.toString());

      msg.addOutputMsg("" + minRealVal, "" + rRealVal);
      if (rRealVal.equals(minRealVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "set Object Method does not set the designated parameter with the Object",
            "Call to setObject Method is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject Method is Failed!");

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
   * @testName: testSetObject40
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Min_Val with the maximum value of Float_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the maximum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnno) method. Both the values should be equal.
   */

  public void testSetObject40() throws Fault {
    Double maxFloatVal = null;
    String maxStringVal = null;
    Double rFloatVal = null;
    String smaxStringVal = null;

    try {
      // to create the Float Table
      rsSch.createTab("Float_Tab", sqlp, conn);

      // to update Minimum value column with Maximum value
      String sPrepStmt = sqlp.getProperty("Float_Tab_Min_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Maximum Value of Float to be Updated ");
      smaxStringVal = rsSch.extractVal("Float_Tab", 1, sqlp, conn);

      maxStringVal = new String(smaxStringVal);
      maxFloatVal = new Double(smaxStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxStringVal, java.sql.Types.FLOAT);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Min_Val_Query = sqlp.getProperty("Float_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      Object orFloatVal = rs.getObject(1);
      rFloatVal = new Double(orFloatVal.toString());

      msg.addOutputMsg("" + maxFloatVal, "" + rFloatVal);
      if (rFloatVal.equals(maxFloatVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "set Object Method does not set the designated parameter with the Object",
            "Call to setObject Method is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject Method is Failed!");

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
        rsSch.dropTab("Float_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject41
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_Val with the minimum value of Float_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the minimum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnno) method. Both the values should be equal.
   */

  public void testSetObject41() throws Fault {
    Double minFloatVal = null;
    String minStringVal = null;
    Double rFloatVal = null;
    String sminStringVal = null;

    try {
      // to create the Float Table
      rsSch.createTab("Float_Tab", sqlp, conn);

      // to update Null value column with Minimum value
      String sPrepStmt = sqlp.getProperty("Float_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Minimum Value of Float to be Updated ");
      sminStringVal = rsSch.extractVal("Float_Tab", 2, sqlp, conn);

      minStringVal = new String(sminStringVal);
      minFloatVal = new Double(sminStringVal);
      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, minStringVal, java.sql.Types.FLOAT);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Float_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rFloatVal = (Double) rs.getObject(1);

      msg.addOutputMsg("" + minFloatVal, "" + rFloatVal);
      if (rFloatVal.equals(minFloatVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "set Object Method does not set the designated parameter with the Object",
            "Call to setObject Method is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject Method is Failed!");

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
        rsSch.dropTab("Float_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject42
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Min_Val with the maximum value of Double_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the maximum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnno) method. Both the values should be equal.
   */

  public void testSetObject42() throws Fault {
    Double maxDoubleVal = null;
    String maxStringVal = null;
    Double rDoubleVal = null;
    String smaxStringVal = null;

    try {
      // to create the Double Table
      rsSch.createTab("Double_Tab", sqlp, conn);

      // to update Minimum value column with Maximum value
      String sPrepStmt = sqlp.getProperty("Double_Tab_Min_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Maximum Value of Double to be Updated");
      smaxStringVal = rsSch.extractVal("Double_Tab", 1, sqlp, conn);

      maxStringVal = new String(smaxStringVal);
      maxDoubleVal = new Double(smaxStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxStringVal, java.sql.Types.DOUBLE);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Min_Val_Query = sqlp.getProperty("Double_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      rDoubleVal = (Double) rs.getObject(1);

      msg.addOutputMsg("" + maxDoubleVal, "" + rDoubleVal);
      if (rDoubleVal.equals(maxDoubleVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "set Object Method does not set the designated parameter with the Object",
            "Call to setObject Method is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject Method is Failed!");

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
        rsSch.dropTab("Double_Tab", conn);
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
      // Close the database connection
      rsSch.dbUnConnect(conn);
      logMsg("Cleanup ok;");
    } catch (Exception e) {
      logErr("An error occurred while closing the database connection", e);
    }
  }
}
