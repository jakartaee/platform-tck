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

package com.sun.ts.tests.jdbc.ee.prepStmt.prepStmt8;

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
 * The prepStmtClient8 class tests methods of PreparedStatement interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class prepStmtClient8 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.prepStmt.prepStmt8";

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

  private transient DatabaseMetaData dbmd = null;

  private Properties sqlp = null;

  private Properties props = null;

  private PreparedStatement pstmt = null;

  private JDBCTestMsg msg = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    prepStmtClient8 theTests = new prepStmtClient8();
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
        // sqlp = new Properties();
        props = p;
        /*
         * String sqlStmt = p.getProperty("rsQuery",""); InputStream istr = new
         * FileInputStream(sqlStmt); sqlp.load(istr);
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

        stmt = conn.createStatement();
        dbmd = conn.getMetaData();
        rsSch = new rsSchema();
        csSch = new csSchema();
        msg = new JDBCTestMsg();
      } catch (SQLException ex) {
        logErr("SQL Exception: " + ex.getMessage(), ex);
      }
    } catch (Exception e) {
      logErr("Setup Failed!");
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * @testName: testSetObject83
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Null_Val of Char_Tab with the minimum value of
   * Decimal_Tab. Call the getObject(int columnno) method to retrieve this
   * value. Extract the minimum value from the tssql.stmt file. Compare this
   * value with the value returned by the getObject(int columnno) method. Both
   * the values should be equal.
   */

  public void testSetObject83() throws Fault {
    String maxStringVal = null;
    String rStringVal = null;
    BigDecimal maxBigDecimalVal = null;
    BigDecimal rsBigDecimalVal = null;

    try {
      rsSch.createTab("Char_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Char_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Value of String to be Updated");
      maxStringVal = rsSch.extractVal("Decimal_Tab", 2, sqlp, conn);
      maxBigDecimalVal = new BigDecimal(maxStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxBigDecimalVal, java.sql.Types.CHAR);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Char_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rStringVal = rs.getObject(1).toString().trim();
      rsBigDecimalVal = new BigDecimal(rStringVal);
      msg.addOutputMsg(maxStringVal, rsBigDecimalVal.toString());

      if (rsBigDecimalVal.compareTo(maxBigDecimalVal) == 0)

      {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed");

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
        rsSch.dropTab("Char_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject84
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Null_Val of Varchar_Tab with the maximum value of
   * Decimal_Tab. Call the getObject(int columnno) method to retrieve this
   * value. Extract the minimum value from the tssql.stmt file. Compare this
   * value with the value returned by the getObject(int columnno) method. Both
   * the values should be equal.
   */

  public void testSetObject84() throws Fault {
    BigDecimal maxBigDecimalVal = null;
    String maxStringVal = null;
    String rStringVal = null;

    try {
      rsSch.createTab("Varchar_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Varchar_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Value of String to be Updated");
      maxStringVal = rsSch.extractVal("Decimal_Tab", 1, sqlp, conn);
      maxBigDecimalVal = new BigDecimal(maxStringVal);
      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxBigDecimalVal, java.sql.Types.VARCHAR);
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
            "test setObject Failed");

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
        rsSch.dropTab("Varchar_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject85
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Null_Val of Varchar_Tab with the minimum value of
   * Decimal_Tab. Call the getObject(int columnno) method to retrieve this
   * value. Extract the minimum value from the tssql.stmt file. Compare this
   * value with the value returned by the getObject(int columnno) method. Both
   * the values should be equal.
   */

  public void testSetObject85() throws Fault {
    BigDecimal maxBigDecimalVal = null;
    BigDecimal rsBigDecimalVal = null;
    String maxStringVal = null;
    String rStringVal = null;

    try {
      rsSch.createTab("Varchar_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Varchar_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Value of String to be Updated");
      maxStringVal = rsSch.extractVal("Decimal_Tab", 2, sqlp, conn);
      maxBigDecimalVal = new BigDecimal(maxStringVal);
      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxBigDecimalVal, java.sql.Types.VARCHAR);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Varchar_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rStringVal = rs.getObject(1).toString().trim();
      rsBigDecimalVal = new BigDecimal(rStringVal);

      msg.addOutputMsg(maxStringVal, rsBigDecimalVal.toString());

      if (rsBigDecimalVal.compareTo(maxBigDecimalVal) == 0) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed");

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
        rsSch.dropTab("Varchar_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject86
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Null_Val of Longvarchar_Tab with the maximum value
   * of Decimal_Tab. Call the getObject(int columnno) method to retrieve this
   * value. Extract the maximum value from the tssql.stmt file. Compare this
   * value with the value returned by the getObject(int columnno) method. Both
   * the values should be equal.
   */

  public void testSetObject86() throws Fault {
    String maxStringVal = null;
    BigDecimal maxBigDecimalVal = null;
    String rStringVal = null;
    try {
      rsSch.createTab("Longvarcharnull_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Longvarchar_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Value of String to be Updated");
      maxStringVal = rsSch.extractVal("Decimal_Tab", 1, sqlp, conn);
      maxBigDecimalVal = new BigDecimal(maxStringVal);
      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxBigDecimalVal, java.sql.Types.LONGVARCHAR);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
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
            "test setObject Failed");

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
        rsSch.dropTab("Longvarcharnull_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject87
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Null_Val of Longvarchar_Tab with the minimum value
   * of Decimal_Tab. Call the getObject(int columnno) method to retrieve this
   * value. Extract the minimum value from the tssql.stmt file. Compare this
   * value with the value returned by the getObject(int columnno) method. Both
   * the values should be equal.
   */

  public void testSetObject87() throws Fault {
    String maxStringVal = null;
    BigDecimal maxBigDecimalVal = null;
    BigDecimal rsBigDecimalVal = null;
    String rStringVal = null;
    try {
      rsSch.createTab("Longvarcharnull_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Longvarchar_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Value of String to be Updated");
      maxStringVal = rsSch.extractVal("Decimal_Tab", 2, sqlp, conn);
      maxBigDecimalVal = new BigDecimal(maxStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxBigDecimalVal, java.sql.Types.LONGVARCHAR);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Longvarchar_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rStringVal = rs.getObject(1).toString().trim();
      rsBigDecimalVal = new BigDecimal(rStringVal);

      msg.addOutputMsg(maxStringVal, rsBigDecimalVal.toString());

      if (rsBigDecimalVal.compareTo(maxBigDecimalVal) == 0) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject Method is Failed!");

    } finally {
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
   * @testName: testSetObject88
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Min_Val of Tinyint_Tab with the maximum value of
   * Bit_Tab. Call the getObject(int columnno) method to retrieve this value.
   * Extract the maximum value from the tssql.stmt file. Compare this value with
   * the value returned by the getObject(int columnno) method. Both the values
   * should be equal.
   */

  public void testSetObject88() throws Fault {
    try {
      // to create the Tinyint Table
      rsSch.createTab("Tinyint_Tab", sqlp, conn);

      // to update Minimum value column with maximum
      String sPrepStmt = sqlp.getProperty("Tinyint_Tab_Min_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Maximum Value of Tinyint to be Updated");
      String smaxTinyintVal = rsSch.extractValAsNumericString("Bit_Tab", 1,
          sqlp, conn);
      Integer maxTinyintVal = Integer.valueOf(smaxTinyintVal);
      Boolean oMaxTinyintVal = rsSch.extractValAsBoolObj("Bit_Tab", 1, sqlp,
          conn);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, oMaxTinyintVal, java.sql.Types.TINYINT);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Min_Val_Query = sqlp.getProperty("Tinyint_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      Object orTinyintVal = rs.getObject(1);
      Integer rTinyintVal = Integer.valueOf(orTinyintVal.toString());

      msg.addOutputMsg("" + maxTinyintVal, "" + rTinyintVal);
      if (rTinyintVal.equals(maxTinyintVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed");

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
   * @testName: testSetObject89
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Null_Val of Tinyint_Tab with the minimum value of
   * Bit_Tab. Call the getObject(int columnno) method to retrieve this value.
   * Extract the minimum value from the tssql.stmt file. Compare this value with
   * the value returned by the getObject(int columnno) method. Both the values
   * should be equal.
   */

  public void testSetObject89() throws Fault {
    try {
      // to create the Tinyint Table
      rsSch.createTab("Tinyint_Tab", sqlp, conn);

      // to update Minimum value column with minimum
      String sPrepStmt = sqlp.getProperty("Tinyint_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt
          + "extract the Minimum Value of Tinyint to be Updated");

      String sminTinyintVal = rsSch.extractValAsNumericString("Bit_Tab", 2,
          sqlp, conn);
      Integer minTinyintVal = Integer.valueOf(sminTinyintVal);
      Boolean oMinTinyintVal = rsSch.extractValAsBoolObj("Bit_Tab", 2, sqlp,
          conn);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, oMinTinyintVal, java.sql.Types.TINYINT);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Tinyint_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      Object orTinyintVal = rs.getObject(1);
      Integer rTinyintVal = Integer.valueOf(orTinyintVal.toString());

      msg.addOutputMsg("" + minTinyintVal, "" + rTinyintVal);
      if (rTinyintVal.equals(minTinyintVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed");

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
   * @testName: testSetObject90
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Min_Val of Smallint_Tab with the maximum (true)
   * value of Bit_Tab. Call the getObject(int columnno) method to retrieve this
   * value. Extract the maximum (true) value from the tssql.stmt file. Compare
   * this value with the value returned by the getObject(int columnno) method.
   * Both the values should be equal.
   */

  public void testSetObject90() throws Fault {
    try {
      // to create the Smallint Table
      rsSch.createTab("Smallint_Tab", sqlp, conn);

      // to update Minimum value column with maximum
      String sPrepStmt = sqlp.getProperty("Smallint_Tab_Min_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt
          + "extract the Maximum Value of Boolean to be Updated in Smallint table");
      String smaxSmallintVal = rsSch.extractValAsNumericString("Bit_Tab", 1,
          sqlp, conn);
      Integer maxSmallintVal = Integer.valueOf(smaxSmallintVal);
      Boolean oMaxSmallintVal = rsSch.extractValAsBoolObj("Bit_Tab", 1, sqlp,
          conn);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, oMaxSmallintVal, java.sql.Types.SMALLINT);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Min_Val_Query = sqlp.getProperty("Smallint_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      Object orSmallintVal = rs.getObject(1);
      Integer rSmallintVal = Integer.valueOf(orSmallintVal.toString());

      msg.addOutputMsg("" + maxSmallintVal, "" + rSmallintVal);
      if (rSmallintVal.equals(maxSmallintVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed");

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
   * @testName: testSetObject91
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Null_Val of Smallint_Tab with the minimum (false)
   * value of Bit_Tab. Call the getObject(int columnno) method to retrieve this
   * value. Extract the minimum (false) value from the tssql.stmt file. Compare
   * this value with the value returned by the getObject(int columnno) method.
   * Both the values should be equal.
   */

  public void testSetObject91() throws Fault {
    try {
      // to create the Smallint Table
      rsSch.createTab("Smallint_Tab", sqlp, conn);

      // to update Null value column with minimum
      String sPrepStmt = sqlp.getProperty("Smallint_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg(
          "extract the Minimum Value of Boolean to be Updated in Smallint table");
      String sminSmallintVal = rsSch.extractValAsNumericString("Bit_Tab", 2,
          sqlp, conn);
      Integer minSmallintVal = Integer.valueOf(sminSmallintVal);
      Boolean oMinSmallintVal = rsSch.extractValAsBoolObj("Bit_Tab", 2, sqlp,
          conn);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, oMinSmallintVal, java.sql.Types.SMALLINT);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Smallint_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      Object orSmallintVal = rs.getObject(1);
      Integer rSmallintVal = Integer.valueOf(orSmallintVal.toString());

      msg.addOutputMsg("" + minSmallintVal, "" + rSmallintVal);
      if (rSmallintVal.equals(minSmallintVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed");

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
   * @testName: testSetObject92
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Min_Val of Integer_Tab with the maximum (true)
   * value of Bit_Tab. Call the getObject(int columnno) method to retrieve this
   * value. Extract the maximum (true) value from the tssql.stmt file. Compare
   * this value with the value returned by the getObject(int columnno) method.
   * Both the values should be equal.
   */

  public void testSetObject92() throws Fault {
    try {
      // to create the Integer Table
      rsSch.createTab("Integer_Tab", sqlp, conn);

      // to update Minimum value column with maximum
      String sPrepStmt = sqlp.getProperty("Integer_Tab_Min_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg(
          "extract the Maximum Value of Boolean to be Updated in Integer table");
      String smaxIntegerVal = rsSch.extractValAsNumericString("Bit_Tab", 1,
          sqlp, conn);
      Integer maxIntegerVal = Integer.valueOf(smaxIntegerVal);
      Boolean oMaxIntegerVal = rsSch.extractValAsBoolObj("Bit_Tab", 1, sqlp,
          conn);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, oMaxIntegerVal, java.sql.Types.INTEGER);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Min_Val_Query = sqlp.getProperty("Integer_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      Object orIntegerVal = rs.getObject(1);
      Integer rIntegerVal = Integer.valueOf(orIntegerVal.toString());

      msg.addOutputMsg("" + maxIntegerVal, "" + rIntegerVal);
      if (rIntegerVal.equals(maxIntegerVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed");

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
   * @testName: testSetObject93
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Null_Val of Integer_Tab with the minimum (false)
   * value of Bit_Tab. Call the getObject(int columnno) method to retrieve this
   * value. Extract the minimum (false) value from the tssql.stmt file. Compare
   * this value with the value returned by the getObject(int columnno) method.
   * Both the values should be equal.
   */

  public void testSetObject93() throws Fault {
    try {

      // to create the Integer Table
      rsSch.createTab("Integer_Tab", sqlp, conn);

      // to update Minimum value column with minimum
      String sPrepStmt = sqlp.getProperty("Integer_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt
          + "extract the Minimum Value of Boolean to be Updated in Integer table");

      String sminIntegerVal = rsSch.extractValAsNumericString("Bit_Tab", 2,
          sqlp, conn);
      Integer minIntegerVal = Integer.valueOf(sminIntegerVal);
      Boolean oMinIntegerVal = rsSch.extractValAsBoolObj("Bit_Tab", 2, sqlp,
          conn);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, oMinIntegerVal, java.sql.Types.INTEGER);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Integer_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      Object orIntegerVal = rs.getObject(1);
      Integer rIntegerVal = Integer.valueOf(orIntegerVal.toString());

      msg.addOutputMsg("" + minIntegerVal, "" + rIntegerVal);
      if (rIntegerVal.equals(minIntegerVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject Method is Failed!");

    } finally {
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
   * @testName: testSetObject94
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Min_Val of Bigint_Tab with the maximum (true)
   * value of Bit_Tab. Call the getObject(int columnno) method to retrieve this
   * value. Extract the maximum (true) value from the tssql.stmt file. Compare
   * this value with the value returned by the getObject(int columnno) method.
   * Both the values should be equal.
   */

  public void testSetObject94() throws Fault {
    try {

      // to create the Long Table
      rsSch.createTab("Bigint_Tab", sqlp, conn);

      // to update Minimum value column with maximum
      String sPrepStmt = sqlp.getProperty("Bigint_Tab_Min_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt
          + "extract the Maximum Value of Boolean to be Updated in Bigint table");

      String smaxBigintVal = rsSch.extractValAsNumericString("Bit_Tab", 1, sqlp,
          conn);
      Long maxBigintVal = Long.valueOf(smaxBigintVal);
      Boolean oMaxBigintVal = rsSch.extractValAsBoolObj("Bit_Tab", 1, sqlp,
          conn);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, oMaxBigintVal, java.sql.Types.BIGINT);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Min_Val_Query = sqlp.getProperty("Bigint_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      Object orBigintVal = rs.getObject(1);
      Long rBigintVal = Long.valueOf(orBigintVal.toString());

      msg.addOutputMsg("" + maxBigintVal, "" + rBigintVal);
      if (rBigintVal.equals(maxBigintVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed");

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
   * @testName: testSetObject95
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Null_Val of Bigint_Tab with the minimum (false)
   * value of Bit_Tab. Call the getObject(int columnno) method to retrieve this
   * value. Extract the minimum (false) value from the tssql.stmt file. Compare
   * this value with the value returned by the getObject(int columnno) method.
   * Both the values should be equal.
   */

  public void testSetObject95() throws Fault {
    try {

      // to create the Bigint Table
      rsSch.createTab("Bigint_Tab", sqlp, conn);

      // to update Minimum value column with minimum
      String sPrepStmt = sqlp.getProperty("Bigint_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg(
          "extract the Minimum Value of Boolean to be Updated in Bigint table");
      String sminBigintVal = rsSch.extractValAsNumericString("Bit_Tab", 2, sqlp,
          conn);
      Long minBigintVal = Long.valueOf(sminBigintVal);
      Boolean oMinBigintVal = rsSch.extractValAsBoolObj("Bit_Tab", 2, sqlp,
          conn);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, oMinBigintVal, java.sql.Types.BIGINT);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Bigint_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      Object orBigintVal = rs.getObject(1);
      Long rBigintVal = Long.valueOf(orBigintVal.toString());

      msg.addOutputMsg("" + minBigintVal, "" + rBigintVal);
      if (rBigintVal.equals(minBigintVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed");

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
   * @testName: testSetObject96
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Min_Val of Real_Tab with the maximum (true) value
   * of Bit_Tab. Call the getObject(int columnno) method to retrieve this value.
   * Extract the maximum (true) value from the tssql.stmt file. Compare this
   * value with the value returned by the getObject(int columnno) method. Both
   * the values should be equal.
   */

  public void testSetObject96() throws Fault {
    try {

      // to create the Real Table
      rsSch.createTab("Real_Tab", sqlp, conn);

      // to update Minimum value column with maximum
      String sPrepStmt = sqlp.getProperty("Real_Tab_Min_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt
          + "extract the Maximum Value of Boolean to be Updated in Real table");

      String smaxRealVal = rsSch.extractValAsNumericString("Bit_Tab", 1, sqlp,
          conn);
      Float maxRealVal = Float.valueOf(smaxRealVal);
      Boolean oMaxRealVal = rsSch.extractValAsBoolObj("Bit_Tab", 1, sqlp, conn);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, oMaxRealVal, java.sql.Types.REAL);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Min_Val_Query = sqlp.getProperty("Real_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      Object orRealVal = rs.getObject(1);
      Float rRealVal = Float.valueOf(orRealVal.toString());

      msg.addOutputMsg("" + maxRealVal, "" + rRealVal);
      if (rRealVal.compareTo(maxRealVal) == 0) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed");

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
   * @testName: testSetObject97
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Null_Val of Real_Tab with the minimum (false)
   * value of Bit_Tab. Call the getObject(int columnno) method to retrieve this
   * value. Extract the minimum (false) value from the tssql.stmt file. Compare
   * this value with the value returned by the getObject(int columnno) method.
   * Both the values should be equal.
   */

  public void testSetObject97() throws Fault {
    try {

      // to create the Bigint Table
      rsSch.createTab("Real_Tab", sqlp, conn);

      // to update Minimum value column with minimum
      String sPrepStmt = sqlp.getProperty("Real_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt
          + "extract the Minimum Value of Boolean to be Updated in Real table");

      String sminRealVal = rsSch.extractValAsNumericString("Bit_Tab", 2, sqlp,
          conn);
      Float minRealVal = Float.valueOf(sminRealVal);
      Boolean oMinRealVal = rsSch.extractValAsBoolObj("Bit_Tab", 2, sqlp, conn);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, oMinRealVal, java.sql.Types.REAL);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Real_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      Object orRealVal = rs.getObject(1);
      Float rRealVal = Float.valueOf(orRealVal.toString());

      msg.addOutputMsg("" + minRealVal, "" + rRealVal);
      if (rRealVal.compareTo(minRealVal) == 0) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject Method is Failed!");

    } finally {
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
   * @testName: testSetObject98
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Min_Val of Float_Tab with the maximum (true) value
   * of Bit_Tab. Call the getObject(int columnno) method to retrieve this value.
   * Extract the maximum (true) value from the tssql.stmt file. Compare this
   * value with the value returned by the getObject(int columnno) method. Both
   * the values should be equal.
   */

  public void testSetObject98() throws Fault {
    try {
      // to create the Float Table
      rsSch.createTab("Float_Tab", sqlp, conn);

      // to update Minimum value column with maximum
      String sPrepStmt = sqlp.getProperty("Float_Tab_Min_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt
          + "extract the Maximum Value of Boolean to be Updated in Float table");
      String smaxFloatVal = rsSch.extractValAsNumericString("Bit_Tab", 1, sqlp,
          conn);
      Double maxFloatVal = Double.valueOf(smaxFloatVal);
      Boolean oMaxFloatVal = rsSch.extractValAsBoolObj("Bit_Tab", 1, sqlp,
          conn);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, oMaxFloatVal, java.sql.Types.FLOAT);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Min_Val_Query = sqlp.getProperty("Float_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      Double rFloatVal = (Double) rs.getObject(1);

      msg.addOutputMsg("" + maxFloatVal, "" + rFloatVal);
      if (rFloatVal.equals(maxFloatVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject Method is Failed!");

    } finally {
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
   * @testName: testSetObject99
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Null_Val of Float_Tab with the minimum (true)
   * value of Bit_Tab. Call the getObject(int columnno) method to retrieve this
   * value. Extract the minimum (true) value from the tssql.stmt file. Compare
   * this value with the value returned by the getObject(int columnno) method.
   * Both the values should be equal.
   */
  public void testSetObject99() throws Fault {
    try {

      // to create the Float Table
      rsSch.createTab("Float_Tab", sqlp, conn);

      // to update Minimum value column with minimum
      String sPrepStmt = sqlp.getProperty("Float_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg(
          "extract the Minimum Value of Boolean to be Updated in Double table");

      String sminFloatVal = rsSch.extractValAsNumericString("Bit_Tab", 2, sqlp,
          conn);
      Double minFloatVal = Double.valueOf(sminFloatVal);
      Boolean oMinFloatVal = rsSch.extractValAsBoolObj("Bit_Tab", 2, sqlp,
          conn);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, oMinFloatVal, java.sql.Types.FLOAT);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Float_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      Double rFloatVal = (Double) rs.getObject(1);

      msg.addOutputMsg("" + minFloatVal, "" + rFloatVal);
      if (rFloatVal.equals(minFloatVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed");

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
   * @testName: testSetObject100
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Min_Val of Double_Tab with the maximum (true)
   * value of Bit_Tab. Call the getObject(int columnno) method to retrieve this
   * value. Extract the maximum (true) value from the tssql.stmt file. Compare
   * this value with the value returned by the getObject(int columnno) method.
   * Both the values should be equal.
   */

  public void testSetObject100() throws Fault {
    try {

      // to create the Double Table
      rsSch.createTab("Double_Tab", sqlp, conn);

      // to update Minimum value column with maximum
      String sPrepStmt = sqlp.getProperty("Double_Tab_Min_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);
      msg.setMsg(
          "extract the Maximum Value of Boolean to be Updated in Double table");

      String smaxDoubleVal = rsSch.extractValAsNumericString("Bit_Tab", 1, sqlp,
          conn);
      Double maxDoubleVal = Double.valueOf(smaxDoubleVal);
      Boolean oMaxDoubleVal = rsSch.extractValAsBoolObj("Bit_Tab", 1, sqlp,
          conn);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, oMaxDoubleVal, java.sql.Types.DOUBLE);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Min_Val_Query = sqlp.getProperty("Double_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      Double rDoubleVal = (Double) rs.getObject(1);

      msg.addOutputMsg("" + maxDoubleVal, "" + rDoubleVal);
      if (rDoubleVal.equals(maxDoubleVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed");

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

  /*
   * @testName: testSetObject101
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Null_Val of Double_Tab with the minimum (false)
   * value of Bit_Tab. Call the getObject(int columnno) method to retrieve this
   * value. Extract the minimum (false) value from the tssql.stmt file. Compare
   * this value with the value returned by the getObject(int columnno) method.
   * Both the values should be equal.
   */

  public void testSetObject101() throws Fault {
    try {

      // to create the Double Table
      rsSch.createTab("Double_Tab", sqlp, conn);

      // to update Minimum value column with minimum
      String sPrepStmt = sqlp.getProperty("Double_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg(
          "extract the Minimum Value of Boolean to be Updated in Double table");

      String sminDoubleVal = rsSch.extractValAsNumericString("Bit_Tab", 2, sqlp,
          conn);
      Double minDoubleVal = Double.valueOf(sminDoubleVal);
      Boolean oMinDoubleVal = rsSch.extractValAsBoolObj("Bit_Tab", 2, sqlp,
          conn);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, oMinDoubleVal, java.sql.Types.DOUBLE);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Double_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      Double rDoubleVal = (Double) rs.getObject(1);

      msg.addOutputMsg("" + minDoubleVal, "" + rDoubleVal);
      if (rDoubleVal.equals(minDoubleVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed");

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

  /*
   * @testName: testSetObject102
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:692; JDBC:JAVADOC:693;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Null_Val of Decimal_Tab with the maximum (true)
   * value of Bit_Tab. Call the getObject(int columnno) method to retrieve this
   * value. Extract the maximum (true) value from the tssql.stmt file. Compare
   * this value with the value returned by the getObject(int columnno) method.
   * Both the values should be equal.
   */

  public void testSetObject102() throws Fault {
    try {

      // to create the Decimal Table
      rsSch.createTab("Decimal_Tab", sqlp, conn);

      // to update Null value column with maximum value of Double
      String sPrepStmt = sqlp.getProperty("Decimal_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg(
          "extract the Maximum Value of Boolean to be Updated in Decimal Table");

      String smaxDecimalVal = rsSch.extractValAsNumericString("Bit_Tab", 1,
          sqlp, conn);
      msg.setMsg("extracted maximum value from Bit_Tab: " + smaxDecimalVal);
      BigDecimal maxDecimalVal = new BigDecimal(smaxDecimalVal);
      Boolean oMaxDecimalVal = rsSch.extractValAsBoolObj("Bit_Tab", 1, sqlp,
          conn);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, oMaxDecimalVal, java.sql.Types.DECIMAL, 2);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Decimal_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      BigDecimal rDecimalVal = (BigDecimal) rs.getObject(1);

      msg.addOutputMsg("" + maxDecimalVal, "" + rDecimalVal);
      if (rDecimalVal.compareTo(maxDecimalVal) == 0) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed");

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
        rsSch.dropTab("Decimal_Tab", conn);
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
