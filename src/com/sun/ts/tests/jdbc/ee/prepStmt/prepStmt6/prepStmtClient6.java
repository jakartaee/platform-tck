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

package com.sun.ts.tests.jdbc.ee.prepStmt.prepStmt6;

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
 * The prepStmtClient6 class tests methods of PreparedStatement interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.8, 11/24/00
 */

public class prepStmtClient6 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.prepStmt.prepStmt6";

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
    prepStmtClient6 theTests = new prepStmtClient6();
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
        logErr("SQL Exception: " + ex.getMessage(), ex);
      }
    } catch (Exception e) {
      logErr("Setup Failed!");
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * @testName: testSetObject43
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, int
   * targetSqlType, int scale) method,update the column Null_Val with the
   * minimum value of Double_Tab. Call the getObject(int columnno) method to
   * retrieve this value. Extract the minimum value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */

  public void testSetObject43() throws Fault {
    Double minDoubleVal = null;
    String minStringVal = null;
    Double rDoubleVal = null;
    String sminStringVal = null;

    try {
      // to create the Double Table
      rsSch.createTab("Double_Tab", sqlp, conn);

      // to update Null value column with Minimum value
      String sPrepStmt = sqlp.getProperty("Double_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Minimum Value of Double to be Updated ");
      sminStringVal = rsSch.extractVal("Double_Tab", 2, sqlp, conn);

      minStringVal = new String(sminStringVal);
      minDoubleVal = new Double(sminStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, minStringVal, java.sql.Types.DOUBLE);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Double_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rDoubleVal = (Double) rs.getObject(1);
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
        rsSch.dropTab("Double_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject44
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:692; JDBC:JAVADOC:693;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, int
   * targetSqlType, int scale) method,update the column NUll_Val with the
   * maximum value of Decimal_Tab. Call the getObject(int columnno) method to
   * retrieve this value. Extract the maximum value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */

  public void testSetObject44() throws Fault {

    BigDecimal maxDecimalVal = null;
    String maxStringVal = null;
    BigDecimal rDecimalVal = null;
    String smaxStringVal = null;

    try {
      // to create the Decimal Table
      rsSch.createTab("Decimal_Tab", sqlp, conn);

      // to update Minimum value column with Maximum value
      String sPrepStmt = sqlp.getProperty("Decimal_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Maximum Value of Decimal to be Updated ");
      smaxStringVal = rsSch.extractVal("Decimal_Tab", 1, sqlp, conn);

      maxStringVal = new String(smaxStringVal);
      maxDecimalVal = new BigDecimal(smaxStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxStringVal, java.sql.Types.DECIMAL, 15);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Decimal_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rDecimalVal = (BigDecimal) rs.getObject(1);

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
   * @testName: testSetObject45
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:692; JDBC:JAVADOC:693;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType,
   * int scale) method,update the column Null_Val with the minimum value of
   * Decimal_Tab. Call the getObject(int columnno) method to retrieve this
   * value. Extract the minimum value from the tssql.stmt file. Compare this
   * value with the value returned by the getObject(int columnno) method. Both
   * the values should be equal.
   */

  public void testSetObject45() throws Fault {
    BigDecimal minDecimalVal = null;
    String minStringVal = null;
    BigDecimal rDecimalVal = null;
    String sminStringVal = null;

    try {
      // to create the Decimal Table
      rsSch.createTab("Decimal_Tab", sqlp, conn);

      // to update Null value column with Minimum value
      String sPrepStmt = sqlp.getProperty("Decimal_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Minimum Value of Decimal to be Updated ");
      sminStringVal = rsSch.extractVal("Decimal_Tab", 2, sqlp, conn);

      minStringVal = new String(sminStringVal);
      minDecimalVal = new BigDecimal(sminStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, minStringVal, java.sql.Types.DECIMAL, 15);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Decimal_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rDecimalVal = (BigDecimal) rs.getObject(1);

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
   * @testName: testSetObject46
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:692; JDBC:JAVADOC:693;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, int
   * targetSqlType, int scale) method,update the column Null_Val with the
   * maximum value of Numeric_Tab. Call the getObject(int columnno) method to
   * retrieve this value. Extract the maximum value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */

  public void testSetObject46() throws Fault {
    BigDecimal maxNumericVal = null;
    String maxStringVal = null;
    BigDecimal rNumericVal = null;
    String smaxStringVal = null;

    try {
      // to create the Numeric Table
      rsSch.createTab("Numeric_Tab", sqlp, conn);

      // to update Minimum value column with Maximum value
      String sPrepStmt = sqlp.getProperty("Numeric_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Maximum Value of Numeric to be Updated ");
      smaxStringVal = rsSch.extractVal("Numeric_Tab", 1, sqlp, conn);

      maxStringVal = new String(smaxStringVal);
      maxNumericVal = new BigDecimal(smaxStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxStringVal, java.sql.Types.NUMERIC, 15);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Numeric_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rNumericVal = (BigDecimal) rs.getObject(1);

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
   * @testName: testSetObject47
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:692; JDBC:JAVADOC:693;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType,
   * int scale) method,update the column Null_Val with the minimum value of
   * Numeric_Tab. Call the getObject(int columnno) method to retrieve this
   * value. Extract the minimum value from the tssql.stmt file. Compare this
   * value with the value returned by the getObject(int columnno) method. Both
   * the values should be equal.
   */

  public void testSetObject47() throws Fault {

    BigDecimal minNumericVal = null;
    String minStringVal = null;
    BigDecimal rNumericVal = null;
    String sminStringVal = null;

    try {
      // to create the Numeric Table
      rsSch.createTab("Numeric_Tab", sqlp, conn);

      // to update Null value column with Minimum value
      String sPrepStmt = sqlp.getProperty("Numeric_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Minimum Value of Numeric to be Updated");
      sminStringVal = rsSch.extractVal("Numeric_Tab", 2, sqlp, conn);

      minStringVal = new String(sminStringVal);
      minNumericVal = new BigDecimal(sminStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, minStringVal, java.sql.Types.NUMERIC, 15);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Numeric_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rNumericVal = (BigDecimal) rs.getObject(1);

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
   * @testName: testSetObject48
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JDBC:JAVADOC:1;JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, int
   * targetSqlType) method,update the column Min_Val with the maximum value of
   * Bit_Tab. Call the getBoolean(int columnno) method to retrieve this value.
   * Extract the maximum value from the tssql.stmt file. Compare this value with
   * the value returned by the getBoolean(int columnno) method. Both the values
   * should be equal.
   */

  public void testSetObject48() throws Fault {
    Boolean rBooleanVal = null;

    try {
      // to create the Bit Table
      rsSch.createTab("Bit_Tab", sqlp, conn);

      // to update Minimum (false) value column with maximum (true)
      String sPrepStmt = sqlp.getProperty("Bit_Tab_Min_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);
      msg.setMsg("extract the Maximum Value of Boolean to be Updated");

      Boolean maxBooleanVal = rsSch.extractValAsBoolObj("Bit_Tab", 1, sqlp,
          conn);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxBooleanVal, java.sql.Types.BIT);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
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
        rsSch.dropTab("Bit_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject49
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JDBC:JAVADOC:1; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, int
   * targetSqlType) method,update the column Null_Val with the minimum value of
   * Bit_Tab. Call the getBoolean(int columnno) method to retrieve this value.
   * Extract the minimum value from the tssql.stmt file. Compare this value with
   * the value returned by the getBoolean(int columnno) method. Both the values
   * should be equal.
   */

  public void testSetObject49() throws Fault {
    Boolean minBooleanVal = null;
    Boolean rBooleanVal = null;

    try {
      // to create the Bit Table
      rsSch.createTab("Bit_Tab", sqlp, conn);

      // to update Null value column with Minimum value
      String sPrepStmt = sqlp.getProperty("Bit_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Minimum Value of Boolean to be Updated");

      minBooleanVal = rsSch.extractValAsBoolObj("Bit_Tab", 2, sqlp, conn);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, minBooleanVal, java.sql.Types.BIT);
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
        rsSch.dropTab("Bit_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject50
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, int
   * targetSqlType) method,update the column Null_Val with the maximum value of
   * Char_Tab. Call the getObject(int columnno) method to retrieve this value.
   * Extract the maximum value from the tssql.stmt file. Compare this value with
   * the value returned by the getObject(int columnno) method. Both the values
   * should be equal.
   */

  public void testSetObject50() throws Fault {
    String maxStringVal = null;
    String rStringVal = null;

    try {
      // to create the Char table
      rsSch.createTab("Char_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Char_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Value of String to be Updated");
      maxStringVal = rsSch.extractVal("Char_Tab", 1, sqlp, conn);
      maxStringVal = maxStringVal.substring(1, maxStringVal.length() - 1);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxStringVal, java.sql.Types.CHAR);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
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
   * @testName: testSetObject51
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, int
   * targetSqlType) method,update the column Null_Val with the minimum value of
   * Char_Tab. Call the getObject(int columnno) method to retrieve this value.
   * Extract the minimum value from the tssql.stmt file. Compare this value with
   * the value returned by the getObject(int columnno) method. Both the values
   * should be equal.
   */

  public void testSetObject51() throws Fault {
    String maxStringVal = null;
    String rStringVal = null;

    try {
      // to create the Char table
      rsSch.createTab("Char_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Char_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Value of String to be Updated");
      maxStringVal = rsSch.extractVal("Char_Tab", 2, sqlp, conn);
      maxStringVal = maxStringVal.substring(1, maxStringVal.length() - 1);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxStringVal, java.sql.Types.CHAR);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
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
   * @testName: testSetObject52
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, int
   * targetSqlType) method,update the column Null_Val with the maximum value of
   * Varchar_Tab. Call the getObject(int columnno) method to retrieve this
   * value. Extract the maximum value from the tssql.stmt file. Compare this
   * value with the value returned by the getObject(int columnno) method. Both
   * the values should be equal.
   */

  public void testSetObject52() throws Fault {
    String maxStringVal = null;
    String rStringVal = null;

    try {
      // to create the Varchar table
      rsSch.createTab("Varchar_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Varchar_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Value of String to be Updated");
      maxStringVal = rsSch.extractVal("Varchar_Tab", 1, sqlp, conn);
      maxStringVal = maxStringVal.substring(1, maxStringVal.length() - 1);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxStringVal, java.sql.Types.VARCHAR);
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
   * @testName: testSetObject53
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, int
   * targetSqlType) method,update the column Null_Val with the minimum value of
   * Varchar_Tab. Call the getObject(int columnno) method to retrieve this
   * value. Extract the minimum value from the tssql.stmt file. Compare this
   * value with the value returned by the getObject(int columnno) method. Both
   * the values should be equal.
   */

  public void testSetObject53() throws Fault {
    String maxStringVal = null;
    String rStringVal = null;

    try {
      // to create the Varchar table
      rsSch.createTab("Varchar_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Varchar_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Value of String to be Updated");
      maxStringVal = rsSch.extractVal("Varchar_Tab", 2, sqlp, conn);
      maxStringVal = maxStringVal.substring(1, maxStringVal.length() - 1);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxStringVal, java.sql.Types.VARCHAR);
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
   * @testName: testSetObject54
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, int
   * targetSqlType) method,update the column Null_Val with the maximum value of
   * Longvarchar_Tab. Call the getObject(int columnno) method to retrieve this
   * value. Extract the maximum value from the tssql.stmt file. Compare this
   * value with the value returned by the getObject(int columnno) method. Both
   * the values should be equal.
   */

  public void testSetObject54() throws Fault {
    String maxStringVal = null;
    String rStringVal = null;

    try {
      // to create the Longvarchar table
      rsSch.createTab("Longvarcharnull_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Longvarchar_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Value of String to be Updated");
      maxStringVal = rsSch.extractVal("Longvarchar_Tab", 1, sqlp, conn);
      maxStringVal = maxStringVal.substring(1, maxStringVal.length() - 1);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxStringVal, java.sql.Types.LONGVARCHAR);
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
   * @testName: testSetObject59
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, int
   * targetSqlType) method,update the column Null_Val with the Non Null value of
   * Date_Tab. Call the getObject(int columnno) method to retrieve this value.
   * Extract the Non Null value from the tssql.stmt file. Compare this value
   * with the value returned by the getObject(int columnno) method. Both the
   * values should be equal.
   */

  public void testSetObject59() throws Fault {
    java.sql.Date mfgDateVal = null;
    String mfgStringVal = null;
    String sMfgStringVal = null;
    java.sql.Date rDateVal = null;

    try {
      rsSch.createTab("Date_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Date_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Value of Date to be Updated ");
      sMfgStringVal = rsSch.extractVal("Date_Tab", 1, sqlp, conn);
      sMfgStringVal = sMfgStringVal.substring(sMfgStringVal.indexOf('\'') + 1,
          sMfgStringVal.lastIndexOf('\''));

      // to convert String value into java.sql.Date value
      mfgDateVal = java.sql.Date.valueOf(sMfgStringVal);

      mfgStringVal = sMfgStringVal;

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, mfgStringVal, java.sql.Types.DATE);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Date_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      java.util.Date urDateVal = (java.util.Date) rs.getObject(1);
      long ld = urDateVal.getTime();

      rDateVal = new java.sql.Date(ld);

      msg.addOutputMsg("" + mfgDateVal, "" + rDateVal);

      if (rDateVal.compareTo(mfgDateVal) == 0) {
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
        rsSch.dropTab("Date_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject60
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, int
   * targetSqlType) method,update the column Null_Val with the Non Null value of
   * Time_Tab. Call the getObject(int columnno) method to retrieve this value.
   * Extract the Non Null value from the tssql.stmt file. Compare this value
   * with the value returned by the getObject(int columnno) method. Both the
   * values should be equal.
   */

  public void testSetObject60() throws Fault {
    java.sql.Time brkTimeVal = null;
    String brkStringVal = null;
    String sBrkTimeVal = null;
    java.sql.Time rTimeVal = null;

    try {
      rsSch.createTab("Time_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Time_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Value of Time to be Updated ");
      sBrkTimeVal = rsSch.extractVal("Time_Tab", 1, sqlp, conn);
      sBrkTimeVal = sBrkTimeVal.substring(sBrkTimeVal.indexOf('\'') + 1,
          sBrkTimeVal.lastIndexOf('\''));

      // to convert String value into java.sql.Time value
      brkTimeVal = Time.valueOf(sBrkTimeVal);
      brkStringVal = new String(sBrkTimeVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, brkStringVal, java.sql.Types.TIME);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Time_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      java.util.Date urTimeVal = (java.util.Date) rs.getObject(1);
      long lt = urTimeVal.getTime();

      rTimeVal = new java.sql.Time(lt);

      msg.addOutputMsg("" + brkTimeVal, "" + rTimeVal);
      if (rTimeVal.toString().trim().equals(brkTimeVal.toString().trim())) {
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
        rsSch.dropTab("Time_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject61
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, int
   * targetSqlType) method,update the column Null_Val with the Non Null value of
   * Timestamp_Tab. Call the getObject(int columnno) method to retrieve this
   * value. Extract the Non Null value from the tssql.stmt file. Compare this
   * value with the value returned by the getObject(int columnno) method. Both
   * the values should be equal.
   */

  public void testSetObject61() throws Fault {
    java.sql.Timestamp inTimeVal = null;
    String sInTimeVal = null;
    String inStringVal = null;
    java.sql.Timestamp rTimestampVal = null;

    try {
      // to create the Timestamp table
      rsSch.createTab("Timestamp_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Timestamp_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Value of Timestamp to be Updated");
      sInTimeVal = rsSch.extractVal("Timestamp_Tab", 1, sqlp, conn);
      sInTimeVal = sInTimeVal.substring(sInTimeVal.indexOf('\'') + 1,
          sInTimeVal.lastIndexOf('\''));

      // to convert String value into java.sql.Timestamp
      inStringVal = new String(sInTimeVal);
      inTimeVal = Timestamp.valueOf(sInTimeVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, inStringVal, java.sql.Types.TIMESTAMP);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Timestamp_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rTimestampVal = (java.sql.Timestamp) rs.getObject(1);

      msg.addOutputMsg("" + inTimeVal, "" + rTimestampVal);
      if (rTimestampVal.equals(inTimeVal)) {
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
        rsSch.dropTab("Timestamp_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject62
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Min_Val with the maximum value of Tinyint_Tab.
   * Call the getObject(int columnno) method to retrieve this value. Extract the
   * maximum value from the tssql.stmt file. Compare this value with the value
   * returned by the getObject(int columnno) method. Both the values should be
   * equal.
   */

  public void testSetObject62() throws Fault {
    Integer maxTinyintVal = null;
    BigDecimal maxBigDecimalVal = null;
    Integer rTinyintVal = null;
    String smaxStringVal = null;

    try {
      // to create the Tinyint Table
      rsSch.createTab("Tinyint_Tab", sqlp, conn);

      // to update Minimum value column with maximum
      String sPrepStmt = sqlp.getProperty("Tinyint_Tab_Min_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Maximum Value of Tinyint to be Updated ");
      smaxStringVal = rsSch.extractVal("Tinyint_Tab", 1, sqlp, conn);

      maxBigDecimalVal = new BigDecimal(smaxStringVal);
      maxTinyintVal = new Integer(smaxStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxBigDecimalVal, java.sql.Types.TINYINT);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Min_Val_Query = sqlp.getProperty("Tinyint_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      Object orTinyintVal = rs.getObject(1);
      rTinyintVal = new Integer(orTinyintVal.toString());

      msg.addOutputMsg("" + rTinyintVal, "" + maxTinyintVal);
      if (rTinyintVal.compareTo(maxTinyintVal) == 0) {
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
      // close the Database
      rsSch.dbUnConnect(conn);
      logMsg("Cleanup ok;");
    } catch (Exception e) {
      logErr("An error occurred while closing the database connection", e);
    }
  }
}
