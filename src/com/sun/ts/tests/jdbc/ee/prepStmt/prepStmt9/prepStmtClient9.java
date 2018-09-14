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

package com.sun.ts.tests.jdbc.ee.prepStmt.prepStmt9;

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
 * The prepStmtClient9 class tests methods of PreparedStatement interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.8, 24/11/00
 */

public class prepStmtClient9 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.prepStmt.prepStmt9";

  // Naming specific member variables
  private TSNamingContextInterface jc = null;

  // Harness requirements

  private transient Connection conn = null;

  private ResultSet rs = null;

  private Statement stmt = null;

  private transient DatabaseMetaData dbmd = null;

  private DataSource ds1 = null;

  private dbSchema dbSch = null;

  private rsSchema rsSch = null;

  private csSchema csSch = null;

  private String drManager = null;

  private Properties sqlp = null;

  private Properties props = null;

  private PreparedStatement pstmt = null;

  private JDBCTestMsg msg = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    prepStmtClient9 theTests = new prepStmtClient9();
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
        dbmd = conn.getMetaData();
        rsSch = new rsSchema();
        csSch = new csSchema();
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
   * @testName: testSetObject103
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:692; JDBC:JAVADOC:693;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType,
   * int scale) method,update the column Null_Val of Decimal_Tab with the
   * minimum (false) value of Bit_Tab. Call the getObject(int columnno) method
   * to retrieve this value. Extract the minimum (false) value from the
   * tssql.stmt file. Compare this value with the value returned by the
   * getObject(int columnno) method. Both the values should be equal.
   */

  public void testSetObject103() throws Fault {
    try {
      // to create the Decimal Table
      rsSch.createTab("Decimal_Tab", sqlp, conn);

      // to update Minimum value column with minimum value of Double
      String sPrepStmt = sqlp.getProperty("Decimal_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg(
          "extract the Minimum Value of Boolean to be Updated in Decimal Table");
      String sminDecimalVal = rsSch.extractValAsNumericString("Bit_Tab", 2,
          sqlp, conn);
      msg.setMsg("extracted Minimum value from Bit_Tab: " + sminDecimalVal);
      BigDecimal minDecimalVal = new BigDecimal(sminDecimalVal);
      Boolean oMinDecimalVal = rsSch.extractValAsBoolObj("Bit_Tab", 2, sqlp,
          conn);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, oMinDecimalVal, java.sql.Types.DECIMAL, 2);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Decimal_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      BigDecimal rDecimalVal = (BigDecimal) rs.getObject(1);

      msg.addOutputMsg("" + minDecimalVal, "" + rDecimalVal);
      if (rDecimalVal.compareTo(minDecimalVal) == 0) {
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
   * @testName: testSetObject104
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:692; JDBC:JAVADOC:693;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType,
   * int scale) method, update the column Null_Val of Numeric_Tab with the
   * maximum (true) value of Bit_Tab. Call the getObject(int columnno) method to
   * retrieve this value. Extract the maximum (true) value from the tssql.stmt
   * file. Compare this value with the value returned by the getObject(int
   * columnno) method. Both the values should be equal.
   */

  public void testSetObject104() throws Fault {
    try {

      // to create the Numeric Table
      rsSch.createTab("Numeric_Tab", sqlp, conn);

      // to update Null value column with maximum value of Double
      String sPrepStmt = sqlp.getProperty("Numeric_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);
      msg.setMsg(
          "extract the Maximum Value of Boolean to be Updated in Numeric Table");

      String smaxNumericVal = rsSch.extractValAsNumericString("Bit_Tab", 1,
          sqlp, conn);
      msg.setMsg("extracted maximum value from Bit_Tab: " + smaxNumericVal);
      BigDecimal maxNumericVal = new BigDecimal(smaxNumericVal);
      Boolean oMaxNumericVal = rsSch.extractValAsBoolObj("Bit_Tab", 1, sqlp,
          conn);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, oMaxNumericVal, java.sql.Types.NUMERIC, 2);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Numeric_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      BigDecimal rNumericVal = (BigDecimal) rs.getObject(1);

      msg.addOutputMsg("" + maxNumericVal, "" + rNumericVal);

      if (rNumericVal.compareTo(maxNumericVal) == 0) {
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
   * @testName: testSetObject105
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:692; JDBC:JAVADOC:693;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType,
   * int scale) method,update the column Null_Val of Numeric_Tab with the
   * minimum (false) value of Bit_Tab. Call the getObject(int columnno) method
   * to retrieve this value. Extract the minimum (false) value from the
   * tssql.stmt file. Compare this value with the value returned by the
   * getObject(int columnno) method. Both the values should be equal.
   */

  public void testSetObject105() throws Fault {
    try {

      // to create the Numeric Table
      rsSch.createTab("Numeric_Tab", sqlp, conn);

      // to update Minimum value column with minimum value of Double
      String sPrepStmt = sqlp.getProperty("Numeric_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg(
          "extract the Minimum Value of Boolean to be Updated in Numeric Table");
      String sminNumericVal = rsSch.extractValAsNumericString("Bit_Tab", 2,
          sqlp, conn);
      msg.setMsg("extracted Minimum value from Bit_Tab: " + sminNumericVal);
      BigDecimal minNumericVal = new BigDecimal(sminNumericVal);
      Boolean oMinNumericVal = rsSch.extractValAsBoolObj("Bit_Tab", 2, sqlp,
          conn);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, oMinNumericVal, java.sql.Types.NUMERIC, 2);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Numeric_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      BigDecimal rNumericVal = (BigDecimal) rs.getObject(1);
      msg.addOutputMsg("" + minNumericVal, "" + rNumericVal);

      if (rNumericVal.compareTo(minNumericVal) == 0) {
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
   * @testName: testSetObject108
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Null_Val of Char_Tab with the maximum (true) value
   * of Bit_Tab. Call the getObject(int columnno) method to retrieve this value.
   * Extract the maximum (true) value from the tssql.stmt file. Compare this
   * value with the value returned by the getObject(int columnno) method. Both
   * the values should be equal.
   */

  public void testSetObject108() throws Fault {
    try {
      // to create the Char Table
      rsSch.createTab("Char_Tab", sqlp, conn);

      // to update Null value column of Char table with maximum value of Double
      String sPrepStmt = sqlp.getProperty("Char_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);
      msg.setMsg(
          "extract the Maximum Value of Boolean to be Updated in Char Table");

      String smaxBooleanVal = rsSch.extractValAsNumericString("Bit_Tab", 1,
          sqlp, conn);
      msg.setMsg("extracted maximum value from Bit_Tab: " + smaxBooleanVal);
      Boolean oMaxBooleanVal = rsSch.extractValAsBoolObj("Bit_Tab", 1, sqlp,
          conn);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, oMaxBooleanVal, java.sql.Types.CHAR);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Char_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      String rStringVal = (String) rs.getObject(1);
      rStringVal = rStringVal.trim();

      // Tests will pass for three possibilities.
      // 1. A driver supports boolean and saves boolean as "true" in char_tab
      // 2. A driver doesn't support boolean , saves boolean as "1" in char_tab
      // 3. A driver doesn't support boolean , saves boolean as "true" in
      // char_tab

      if (rStringVal.equals(oMaxBooleanVal.toString())
          || rStringVal.equals(smaxBooleanVal.trim())) {
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
   * @testName: testSetObject109
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Null_Val of Char_Tab with the minimum (false)
   * value of Bit_Tab. Call the getObject(int columnno) method to retrieve this
   * value. Extract the minimum (false) value from the tssql.stmt file. Compare
   * this value with the value returned by the getObject(int columnno) method.
   * Both the values should be equal.
   */

  public void testSetObject109() throws Fault {
    try {

      // to create the Char Table
      rsSch.createTab("Char_Tab", sqlp, conn);

      // to update Minimum value column with minimum value of Float
      String sPrepStmt = sqlp.getProperty("Char_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);
      msg.setMsg(
          "extract the Minimum Value of Boolean to be Updated in Char Table");

      String sminBooleanVal = rsSch.extractValAsNumericString("Bit_Tab", 2,
          sqlp, conn);
      msg.setMsg("extracted Minimum value from Bit_Tab: " + sminBooleanVal);
      Boolean oMinBooleanVal = rsSch.extractValAsBoolObj("Bit_Tab", 2, sqlp,
          conn);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, oMinBooleanVal, java.sql.Types.CHAR);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Char_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      String rStringVal = (String) rs.getObject(1);
      rStringVal = rStringVal.trim();

      // Tests will pass for three possibilities.
      // 1. A driver supports boolean and saves boolean as "true" in char_tab
      // 2. A driver doesn't support boolean , saves boolean as "1" in char_tab
      // 3. A driver doesn't support boolean , saves boolean as "true" in
      // char_tab

      if (rStringVal.equals(oMinBooleanVal.toString())
          || rStringVal.equals(sminBooleanVal.trim())) {
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
   * @testName: testSetObject110
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Null_Val of Varchar_Tab with the maximum (true)
   * value of Bit_Tab. Call the getObject(int columnno) method to retrieve this
   * value. Extract the maximum (true) value from the tssql.stmt file. Compare
   * this value with the value returned by the getObject(int columnno) method.
   * Both the values should be equal.
   */

  public void testSetObject110() throws Fault {
    try {

      // to create the Varchar Table
      rsSch.createTab("Varchar_Tab", sqlp, conn);

      // to update Null value column of Varchar table
      String sPrepStmt = sqlp.getProperty("Varchar_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg(
          "extract the Maximum Value of Boolean to be Updated in Varchar Table");
      String smaxBooleanVal = rsSch.extractValAsNumericString("Bit_Tab", 1,
          sqlp, conn);
      msg.setMsg("extracted maximum value from Bit_Tab: " + smaxBooleanVal);
      Boolean oMaxBooleanVal = rsSch.extractValAsBoolObj("Bit_Tab", 1, sqlp,
          conn);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, oMaxBooleanVal, java.sql.Types.VARCHAR);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Varchar_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      String rStringVal = (String) rs.getObject(1);
      rStringVal = rStringVal.trim();

      // Tests will pass for three possibilities.
      // 1. A driver supports boolean and saves boolean as "true" in char_tab
      // 2. A driver doesn't support boolean , saves boolean as "1" in char_tab
      // 3. A driver doesn't support boolean , saves boolean as "true" in
      // char_tab

      if (rStringVal.equals(oMaxBooleanVal.toString())
          || rStringVal.equals(smaxBooleanVal.trim())) {
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
   * @testName: testSetObject111
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Null_Val of Varchar_Tab with the minimum (false)
   * value of Bit_Tab. Call the getObject(int columnno) method to retrieve this
   * value. Extract the minimum (false) value from the tssql.stmt file. Compare
   * this value with the value returned by the getObject(int columnno) method.
   * Both the values should be equal.
   */

  public void testSetObject111() throws Fault {
    try {

      // to create the Varchar Table
      rsSch.createTab("Varchar_Tab", sqlp, conn);

      // to update Null value column with Minimum value of Float
      String sPrepStmt = sqlp.getProperty("Varchar_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);
      msg.setMsg(
          "extract the Minimum Value of Boolean to be Updated in Varchar Table");

      String sminBooleanVal = rsSch.extractValAsNumericString("Bit_Tab", 2,
          sqlp, conn);
      msg.setMsg("extracted Minimum value from Bit_Tab: " + sminBooleanVal);
      Boolean oMinBooleanVal = rsSch.extractValAsBoolObj("Bit_Tab", 2, sqlp,
          conn);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, oMinBooleanVal, java.sql.Types.VARCHAR);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Varchar_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      String rStringVal = (String) rs.getObject(1);
      rStringVal = rStringVal.trim();

      // Tests will pass for three possibilities.
      // 1. A driver supports boolean and saves boolean as "true" in char_tab
      // 2. A driver doesn't support boolean , saves boolean as "1" in char_tab
      // 3. A driver doesn't support boolean , saves boolean as "true" in
      // char_tab

      if (rStringVal.equals(oMinBooleanVal.toString())
          || rStringVal.equals(sminBooleanVal.trim())) {
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
   * @testName: testSetObject112
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Null_Val of Longvarchar_Tab with the maximum
   * (true) value of Bit_Tab. Call the getObject(int columnno) method to
   * retrieve this value. Extract the maximum (true) value from the tssql.stmt
   * file. Compare this value with the value returned by the getObject(int
   * columnno) method. Both the values should be equal.
   */

  public void testSetObject112() throws Fault {
    try {

      // to create the Longvarchar Table
      rsSch.createTab("Longvarcharnull_Tab", sqlp, conn);

      // to update Null value column of Longvarchar table
      String sPrepStmt = sqlp.getProperty("Longvarchar_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg(
          "extract the Maximum Value of Boolean to be Updated in Longvarchar Table");
      String smaxBooleanVal = rsSch.extractValAsNumericString("Bit_Tab", 1,
          sqlp, conn);
      msg.setMsg("extracted maximum value from Bit_Tab: " + smaxBooleanVal);
      Boolean oMaxBooleanVal = rsSch.extractValAsBoolObj("Bit_Tab", 1, sqlp,
          conn);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, oMaxBooleanVal, java.sql.Types.LONGVARCHAR);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Longvarchar_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      String rStringVal = (String) rs.getObject(1);
      rStringVal = rStringVal.trim();

      // Tests will pass for three possibilities.
      // 1. A driver supports boolean and saves boolean as "true" in char_tab
      // 2. A driver doesn't support boolean , saves boolean as "1" in char_tab
      // 3. A driver doesn't support boolean , saves boolean as "true" in
      // char_tab

      if (rStringVal.equals(oMaxBooleanVal.toString())
          || rStringVal.equals(smaxBooleanVal.trim())) {
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
        rsSch.dropTab("Longvarchar_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject113
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Null_Val of Longvarchar_Tab with the minimum
   * (false) value of Bit_Tab. Call the getObject(int columnno) method to
   * retrieve this value. Extract the minimum (false) value from the tssql.stmt
   * file. Compare this value with the value returned by the getObject(int
   * columnno) method. Both the values should be equal.
   */

  public void testSetObject113() throws Fault {
    try {

      // to create the Longvarchar Table
      rsSch.createTab("Longvarcharnull_Tab", sqlp, conn);

      // to update Null value column with Minimum value of Float
      String sPrepStmt = sqlp.getProperty("Longvarchar_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg(
          "extract the Minimum Value of Boolean to be Updated in Longvarchar Table");
      String sminBooleanVal = rsSch.extractValAsNumericString("Bit_Tab", 2,
          sqlp, conn);
      msg.setMsg("extracted Minimum value from Bit_Tab: " + sminBooleanVal);
      Boolean oMinBooleanVal = rsSch.extractValAsBoolObj("Bit_Tab", 2, sqlp,
          conn);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, oMinBooleanVal, java.sql.Types.LONGVARCHAR);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Longvarchar_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      String rStringVal = (String) rs.getObject(1);
      rStringVal = rStringVal.trim();

      // Tests will pass for three possibilities.
      // 1. A driver supports boolean and saves boolean as "true" in char_tab
      // 2. A driver doesn't support boolean , saves boolean as "1" in char_tab
      // 3. A driver doesn't support boolean , saves boolean as "true" in
      // char_tab

      if (rStringVal.equals(oMinBooleanVal.toString())
          || rStringVal.equals(sminBooleanVal.trim())) {
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
        rsSch.dropTab("Longvarchar_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject114
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Min_Val of Tinyint_Tab with the maximum (true)
   * value of Tinyint_Tab. Call the getObject(int columnno) method to retrieve
   * this value. Extract the maximum (true) value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */

  public void testSetObject114() throws Fault {
    Integer maxIntegerVal = null;
    Integer rTinyintVal = null;
    String smaxStringVal = null;

    try {

      // to create the Tinyint Table
      rsSch.createTab("Tinyint_Tab", sqlp, conn);

      // to update Minimum value column with maximum
      String sPrepStmt = sqlp.getProperty("Tinyint_Tab_Min_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Maximum Value of Tinyint to be Updated");
      smaxStringVal = rsSch.extractVal("Tinyint_Tab", 1, sqlp, conn);

      maxIntegerVal = new Integer(smaxStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxIntegerVal, java.sql.Types.TINYINT);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Min_Val_Query = sqlp.getProperty("Tinyint_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      Object orTinyintVal = rs.getObject(1);
      rTinyintVal = new Integer(orTinyintVal.toString());

      msg.addOutputMsg("" + maxIntegerVal, "" + rTinyintVal);

      if (rTinyintVal.equals(maxIntegerVal)) {
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
        rsSch.dropTab("Tinyint_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject115
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Null_Val of Tinyint_Tab with the minimum (false)
   * value of Tinyint_Tab. Call the getObject(int columnno) method to retrieve
   * this value. Extract the minimum (false) value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */

  public void testSetObject115() throws Fault {
    Integer minIntegerVal = null;
    Integer rTinyintVal = null;
    String sminStringVal = null;

    try {
      // to create the String Table
      rsSch.createTab("Tinyint_Tab", sqlp, conn);

      // to update Null value column with Minimum value
      String sPrepStmt = sqlp.getProperty("Tinyint_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Minimum Value of Tinyint to be Updated ");
      sminStringVal = rsSch.extractVal("Tinyint_Tab", 2, sqlp, conn);

      minIntegerVal = new Integer(sminStringVal);
      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, minIntegerVal, java.sql.Types.TINYINT);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Tinyint_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      Object orTinyintVal = rs.getObject(1);
      rTinyintVal = new Integer(orTinyintVal.toString());

      msg.addOutputMsg("" + minIntegerVal, "" + rTinyintVal);

      if (rTinyintVal.equals(minIntegerVal)) {
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
      msg.printSQLError(sqle, "Call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

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
        rsSch.dropTab("Tinyint_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject116
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   * 
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Min_Val of Smallint_Tab with the maximum (true)
   * value of Smallint_Tab. Call the getObject(int columnno) method to retrieve
   * this value. Extract the maximum (true) value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */

  public void testSetObject116() throws Fault {
    Integer maxIntegerVal = null;
    Integer rSmallintVal = null;
    String smaxStringVal = null;

    try {
      // to create the Smallint Table
      rsSch.createTab("Smallint_Tab", sqlp, conn);

      // to update Minimum value column with maximum
      String sPrepStmt = sqlp.getProperty("Smallint_Tab_Min_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Maximum Value of Smallint to be Updated ");
      smaxStringVal = rsSch.extractVal("Smallint_Tab", 1, sqlp, conn);

      maxIntegerVal = new Integer(smaxStringVal);
      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxIntegerVal, java.sql.Types.SMALLINT);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Min_Val_Query = sqlp.getProperty("Smallint_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      Object orSmallintVal = rs.getObject(1);
      rSmallintVal = new Integer(orSmallintVal.toString());

      msg.addOutputMsg("" + maxIntegerVal, "" + rSmallintVal);
      if (rSmallintVal.equals(maxIntegerVal)) {
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
        rsSch.dropTab("Smallint_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject117
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Null_Val of Smallint_Tab with the minimum (false)
   * value of Smallint_Tab. Call the getObject(int columnno) method to retrieve
   * this value. Extract the minimum (false) value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */

  public void testSetObject117() throws Fault {
    Integer minIntegerVal = null;
    Integer rSmallintVal = null;
    String sminStringVal = null;

    try {

      // to create the Smallint Table
      rsSch.createTab("Smallint_Tab", sqlp, conn);

      // to update Null value column with Minimum value
      String sPrepStmt = sqlp.getProperty("Smallint_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Minimum Value of Smallint to be Updated");
      sminStringVal = rsSch.extractVal("Smallint_Tab", 2, sqlp, conn);

      minIntegerVal = new Integer(sminStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, minIntegerVal, java.sql.Types.SMALLINT);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Smallint_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      Object orSmallintVal = rs.getObject(1);
      rSmallintVal = new Integer(orSmallintVal.toString());

      msg.addOutputMsg("" + minIntegerVal, "" + rSmallintVal);

      if (rSmallintVal.equals(minIntegerVal)) {
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
        rsSch.dropTab("Smallint_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject118
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Min_Val of Integer_Tab with the maximum (true)
   * value of Integer_Tab. Call the getObject(int columnno) method to retrieve
   * this value. Extract the maximum (true) value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */

  public void testSetObject118() throws Fault {

    Integer maxIntegerVal = null;
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

      maxIntegerVal = new Integer(smaxStringVal);
      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxIntegerVal, java.sql.Types.INTEGER);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Min_Val_Query = sqlp.getProperty("Integer_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      Object orIntegerVal = rs.getObject(1);
      rIntegerVal = new Integer(orIntegerVal.toString());

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
        rsSch.dropTab("Integer_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject119
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Null_Val of Integer_Tab with the minimum (false)
   * value of Integer_Tab. Call the getObject(int columnno) method to retrieve
   * this value. Extract the minimum (false) value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */

  public void testSetObject119() throws Fault {
    Integer minIntegerVal = null;
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

      minIntegerVal = new Integer(sminStringVal);
      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, minIntegerVal, java.sql.Types.INTEGER);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Integer_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      Object orIntegerVal = rs.getObject(1);
      rIntegerVal = new Integer(orIntegerVal.toString());

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
        rsSch.dropTab("Integer_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject120
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Min_Val of Bigint_Tab with the maximum (true)
   * value of Integer_Tab. Call the getObject(int columnno) method to retrieve
   * this value. Extract the maximum (true) value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */

  public void testSetObject120() throws Fault {
    Long maxBigintVal = null;
    Integer maxIntegerVal = null;
    Long rBigintVal = null;
    String smaxStringVal = null;

    try {
      // to create the Bigint Table
      rsSch.createTab("Bigint_Tab", sqlp, conn);

      // to update Minimum value column with Maximum value
      String sPrepStmt = sqlp.getProperty("Bigint_Tab_Min_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Maximum Value of Bigint to be Updated ");
      smaxStringVal = rsSch.extractVal("Integer_Tab", 1, sqlp, conn);

      maxBigintVal = new Long(smaxStringVal);
      maxIntegerVal = new Integer(smaxStringVal);
      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxIntegerVal, java.sql.Types.BIGINT);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Min_Val_Query = sqlp.getProperty("Bigint_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      Object orBigintVal = rs.getObject(1);
      rBigintVal = new Long(orBigintVal.toString());

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
        rsSch.dropTab("Bigint_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject121
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Null_Val of Bigint_Tab with the minimum (false)
   * value of Integer_Tab. Call the getObject(int columnno) method to retrieve
   * this value. Extract the minimum (false) value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */

  public void testSetObject121() throws Fault {
    Long minBigintVal = null;
    Integer minIntegerVal = null;
    Long rBigintVal = null;
    String sminStringVal = null;

    try {
      // to create the Bigint Table
      rsSch.createTab("Bigint_Tab", sqlp, conn);

      // to update Null value column with Minimum value
      String sPrepStmt = sqlp.getProperty("Bigint_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Minimum Value of Bigint to be Updated ");
      sminStringVal = rsSch.extractVal("Integer_Tab", 2, sqlp, conn);

      minIntegerVal = new Integer(sminStringVal);
      minBigintVal = new Long(sminStringVal);
      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, minIntegerVal, java.sql.Types.BIGINT);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Bigint_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      Object orBigintVal = rs.getObject(1);
      rBigintVal = new Long(orBigintVal.toString());

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
        rsSch.dropTab("Bigint_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject122
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Min_Val of Real_Tab with the maximum (true) value
   * of Integer_Tab. Call the getObject(int columnno) method to retrieve this
   * value. Extract the maximum (true) value from the tssql.stmt file. Compare
   * this value with the value returned by the getObject(int columnno) method.
   * Both the values should be equal.
   */

  public void testSetObject122() throws Fault {
    Float maxRealVal = null;
    Integer maxIntegerVal = null;
    Float rRealVal = null;
    String smaxStringVal = null;

    try {
      // to create the Real Table
      rsSch.createTab("Real_Tab", sqlp, conn);

      // to update Minimum value column with Maximum value
      String sPrepStmt = sqlp.getProperty("Real_Tab_Min_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Maximum Value of Float to be Updated");
      smaxStringVal = rsSch.extractVal("Integer_Tab", 1, sqlp, conn);

      maxIntegerVal = new Integer(smaxStringVal);
      maxRealVal = new Float(smaxStringVal);
      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxIntegerVal, java.sql.Types.REAL);
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
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed");

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
        rsSch.dropTab("Real_Tab", conn);
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
