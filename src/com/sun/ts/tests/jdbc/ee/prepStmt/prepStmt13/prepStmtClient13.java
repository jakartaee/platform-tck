/*
 * Copyright (c) 2006, 2018 Oracle and/or its affiliates. All rights reserved.
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
 * @(#)prepStmtClient13.java	1.19 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.prepStmt.prepStmt13;

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
 * The prepStmtClient13 class tests methods of PreparedStatement interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.8, 11/24/00
 */

public class prepStmtClient13 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.prepStmt.prepStmt13";

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

  private csSchema csSch = null;

  private String drManager = null;

  private Properties sqlp = null;

  private Properties props = null;

  private JDBCTestMsg msg = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    prepStmtClient13 theTests = new prepStmtClient13();
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
   * @testName: testSetObject183
   * 
   * @assertion_ids: JDBC:SPEC:9;JDBC:SPEC:26; JDBC:JAVADOC:692;
   * JDBC:JAVADOC:693; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType,
   * int scale) method,update the column Null_Val of Numeric_Tab with the
   * minimum value of Numeric_Tab. Call the getObject(int columnno) method to
   * retrieve this value. Extract the minimum value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */

  public void testSetObject183() throws Fault {
    Float oMinNumericVal = null;
    BigDecimal rNumericVal = null;
    String sminNumericVal = null;
    BigDecimal minNumericVal = null;

    try {
      // to create the Numeric Table
      rsSch.createTab("Numeric_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Numeric_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg(
          "extract the Minimum Value of Double to be Updated in Numeric Table");
      sminNumericVal = rsSch.extractVal("Numeric_Tab", 2, sqlp, conn);

      oMinNumericVal = new Float(sminNumericVal);
      minNumericVal = new BigDecimal(sminNumericVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, oMinNumericVal, java.sql.Types.NUMERIC, 15);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Numeric_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      Object orNumericVal = rs.getObject(1);
      rNumericVal = new BigDecimal(orNumericVal.toString());

      msg.addOutputMsg("" + minNumericVal, "" + rNumericVal);
      if (rNumericVal.compareTo(minNumericVal) == 0) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed!");

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
   * @testName: testSetObject186
   * 
   * @assertion_ids: JDBC:SPEC:9;JDBC:SPEC:26; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Null_Val of Char_Tab with the maximum value of
   * Float_Tab. Call the getObject(int columnno) method to retrieve this value.
   * Extract the maximum value from the tssql.stmt file. Compare this value with
   * the value returned by the getObject(int columnno) method. Both the values
   * should be equal.
   */

  public void testSetObject186() throws Fault {
    Float oMaxFloatVal = null;
    String smaxFloatVal = null;
    String rStringVal = null;
    Float rFloatVal = null;

    try {
      // to create the Char Table
      rsSch.createTab("Char_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Char_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg(
          "extract the Maximum Value of Float to be Updated in Char Table");
      smaxFloatVal = rsSch.extractVal("Float_Tab", 1, sqlp, conn);

      oMaxFloatVal = Float.valueOf(smaxFloatVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, oMaxFloatVal, java.sql.Types.CHAR);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Char_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rStringVal = (String) rs.getObject(1);
      rFloatVal = Float.valueOf(rStringVal);

      msg.addOutputMsg("" + oMaxFloatVal, "" + rFloatVal);

      if (Utils.isMatchingFloatingPointVal(oMaxFloatVal, rFloatVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed!");

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
   * @testName: testSetObject187
   * 
   * @assertion_ids: JDBC:SPEC:9;JDBC:SPEC:26; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Null_Val of Char_Tab with the minimum value of
   * Float_Tab. Call the getObject(int columnno) method to retrieve this value.
   * Extract the minimum value from the tssql.stmt file. Compare this value with
   * the value returned by the getObject(int columnno) method. Both the values
   * should be equal.
   */

  public void testSetObject187() throws Fault {
    Float oMinFloatVal = null;
    String sminFloatVal = null;
    String rStringVal = null;
    Float rFloatVal = null;

    try {
      // to create the Char Table
      rsSch.createTab("Char_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Char_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg(
          "extract the Minimum Value of Float to be Updated in Char Table");
      sminFloatVal = rsSch.extractVal("Float_Tab", 2, sqlp, conn);

      oMinFloatVal = new Float(sminFloatVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, oMinFloatVal, java.sql.Types.CHAR);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Char_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rStringVal = (String) rs.getObject(1);
      rFloatVal = new Float(rStringVal);

      msg.addOutputMsg("" + oMinFloatVal, "" + rFloatVal);
      if (rFloatVal.equals(oMinFloatVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed!");

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
   * @testName: testSetObject188
   * 
   * @assertion_ids: JDBC:SPEC:9;JDBC:SPEC:26; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Null_Val of Varchar_Tab with the maximum value of
   * Float_Tab. Call the getObject(int columnno) method to retrieve this value.
   * Extract the maximum value from the tssql.stmt file. Compare this value with
   * the value returned by the getObject(int columnno) method. Both the values
   * should be equal.
   */

  public void testSetObject188() throws Fault {
    Float oMaxFloatVal = null;
    String smaxFloatVal = null;
    String rStringVal = null;
    Float rFloatVal = null;

    try {
      // to create the Varchar Table
      rsSch.createTab("Varchar_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Varchar_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg(
          "extract the Maximum Value of Float to be Updated in Varchar Table");
      smaxFloatVal = rsSch.extractVal("Float_Tab", 1, sqlp, conn);

      oMaxFloatVal = Float.valueOf(smaxFloatVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, oMaxFloatVal, java.sql.Types.VARCHAR);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Varchar_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rStringVal = (String) rs.getObject(1);
      rFloatVal = Float.valueOf(rStringVal);

      msg.addOutputMsg("" + oMaxFloatVal, "" + rFloatVal);
      if (Utils.isMatchingFloatingPointVal(oMaxFloatVal, rFloatVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed!");

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
   * @testName: testSetObject189
   * 
   * @assertion_ids: JDBC:SPEC:9;JDBC:SPEC:26; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Null_Val of Varchar_Tab with the minimum value of
   * Float_Tab. Call the getObject(int columnno) method to retrieve this value.
   * Extract the minimum value from the tssql.stmt file. Compare this value with
   * the value returned by the getObject(int columnno) method. Both the values
   * should be equal.
   */

  public void testSetObject189() throws Fault {
    Float oMinFloatVal = null;
    String sminFloatVal = null;
    String rStringVal = null;
    Float rFloatVal = null;

    try {
      // to create the Varchar Table
      rsSch.createTab("Varchar_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Varchar_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg(
          "extract the Minimum Value of Float to be Updated in Varchar Table");
      sminFloatVal = rsSch.extractVal("Float_Tab", 2, sqlp, conn);

      oMinFloatVal = Float.valueOf(sminFloatVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, oMinFloatVal, java.sql.Types.VARCHAR);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Varchar_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rStringVal = (String) rs.getObject(1);
      rFloatVal = Float.valueOf(rStringVal);

      msg.addOutputMsg("" + oMinFloatVal, "" + rFloatVal);
      if (Utils.isMatchingFloatingPointVal(oMinFloatVal, rFloatVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed!");

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
   * @testName: testSetObject190
   * 
   * @assertion_ids: JDBC:SPEC:9;JDBC:SPEC:26; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Null_Val of Longvarchar_Tab with the maximum value
   * of Float_Tab. Call the getObject(int columnno) method to retrieve this
   * value. Extract the maximum value from the tssql.stmt file. Compare this
   * value with the value returned by the getObject(int columnno) method. Both
   * the values should be equal.
   */

  public void testSetObject190() throws Fault {
    Float oMaxFloatVal = null;
    String smaxFloatVal = null;
    String rStringVal = null;
    Float rFloatVal = null;

    try {
      // to create the Longvarchar Table
      rsSch.createTab("Longvarcharnull_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Longvarchar_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg(
          "extract the Maximum Value of Float to be Updated in Longvarchar Table");
      smaxFloatVal = rsSch.extractVal("Float_Tab", 1, sqlp, conn);

      oMaxFloatVal = new Float(smaxFloatVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, oMaxFloatVal, java.sql.Types.LONGVARCHAR);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Longvarchar_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rStringVal = (String) rs.getObject(1);
      rFloatVal = new Float(rStringVal);
      rStringVal = rStringVal.trim();

      msg.addOutputMsg("" + oMaxFloatVal, "" + rFloatVal);
      if (rFloatVal.equals(oMaxFloatVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed!");

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
   * @testName: testSetObject191
   * 
   * @assertion_ids: JDBC:SPEC:9;JDBC:SPEC:26; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Null_Val of Longvarchar_Tab with the minimum value
   * of Float_Tab. Call the getObject(int columnno) method to retrieve this
   * value. Extract the minimum value from the tssql.stmt file. Compare this
   * value with the value returned by the getObject(int columnno) method. Both
   * the values should be equal.
   */

  public void testSetObject191() throws Fault {
    Float oMinFloatVal = null;
    String sminFloatVal = null;
    String rStringVal = null;
    Float rFloatVal = null;

    try {
      // to create the Longvarchar Table
      rsSch.createTab("Longvarcharnull_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Longvarchar_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg(
          "extract the Minimum Value of Float to be Updated in Longvarchar Table");
      sminFloatVal = rsSch.extractVal("Float_Tab", 2, sqlp, conn);

      oMinFloatVal = new Float(sminFloatVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, oMinFloatVal, java.sql.Types.LONGVARCHAR);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Longvarchar_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rStringVal = (String) rs.getObject(1);
      rFloatVal = new Float(rStringVal);

      msg.addOutputMsg("" + oMinFloatVal, "" + rFloatVal);
      if (rFloatVal.equals(oMinFloatVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed!");

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
   * @testName: testSetObject192
   * 
   * @assertion_ids: JDBC:SPEC:9;JDBC:SPEC:26; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Min_Val of Tinyint_Tab with the maximum value of
   * Tinyint_Tab. Call the getObject(int columnno) method to retrieve this
   * value. Extract the maximum value from the tssql.stmt file. Compare this
   * value with the value returned by the getObject(int columnno) method. Both
   * the values should be equal.
   */

  public void testSetObject192() throws Fault {
    Double oMaxTinyintVal = null;
    Integer rTinyintVal = null;
    String smaxTinyintVal = null;
    Integer maxTinyintVal = null;

    try {
      // to create the Tinyint Table
      rsSch.createTab("Tinyint_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Tinyint_Tab_Min_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Maximum Value of Tinyint to be Updated ");
      smaxTinyintVal = rsSch.extractVal("Tinyint_Tab", 1, sqlp, conn);

      oMaxTinyintVal = new Double(smaxTinyintVal);
      maxTinyintVal = new Integer(smaxTinyintVal);

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
      rTinyintVal = new Integer(orTinyintVal.toString());

      msg.addOutputMsg("" + maxTinyintVal, "" + rTinyintVal);
      if (rTinyintVal.equals(maxTinyintVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed!");

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
   * @testName: testSetObject193
   * 
   * @assertion_ids: JDBC:SPEC:9;JDBC:SPEC:26; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Null_Val of Tinyint_Tab with the minimum value of
   * Tinyint_Tab. Call the getObject(int columnno) method to retrieve this
   * value. Extract the minimum value from the tssql.stmt file. Compare this
   * value with the value returned by the getObject(int columnno) method. Both
   * the values should be equal.
   */

  public void testSetObject193() throws Fault {
    Double oMinTinyintVal = null;
    Integer rTinyintVal = null;
    String sminTinyintVal = null;
    Integer minTinyintVal = null;

    try {
      // to create the Tinyint Table
      rsSch.createTab("Tinyint_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Tinyint_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Minimum Value of Tinyint to be Updated ");
      sminTinyintVal = rsSch.extractVal("Tinyint_Tab", 2, sqlp, conn);

      oMinTinyintVal = new Double(sminTinyintVal);
      minTinyintVal = new Integer(sminTinyintVal);

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
      rTinyintVal = new Integer(orTinyintVal.toString());

      msg.addOutputMsg("" + minTinyintVal, "" + rTinyintVal);
      if (rTinyintVal.equals(minTinyintVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed!");

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
   * @testName: testSetObject194
   * 
   * @assertion_ids: JDBC:SPEC:9;JDBC:SPEC:26; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Min_Val of Smallint_Tab with the maximum value of
   * Smallint_Tab. Call the getObject(int columnno) method to retrieve this
   * value. Extract the maximum value from the tssql.stmt file. Compare this
   * value with the value returned by the getObject(int columnno) method. Both
   * the values should be equal.
   */

  public void testSetObject194() throws Fault {
    Double oMaxSmallintVal = null;
    Integer rSmallintVal = null;
    String smaxSmallintVal = null;
    Integer maxSmallintVal = null;

    try {
      // to create the Smallint Table
      rsSch.createTab("Smallint_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Smallint_Tab_Min_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Maximum Value of Smallint to be Updated ");
      smaxSmallintVal = rsSch.extractVal("Smallint_Tab", 1, sqlp, conn);

      oMaxSmallintVal = new Double(smaxSmallintVal);
      maxSmallintVal = new Integer(smaxSmallintVal);

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
      rSmallintVal = new Integer(orSmallintVal.toString());

      msg.addOutputMsg("" + maxSmallintVal, "" + rSmallintVal);
      if (rSmallintVal.equals(maxSmallintVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed!");

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
   * @testName: testSetObject195
   * 
   * @assertion_ids: JDBC:SPEC:9;JDBC:SPEC:26; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Null_Val of Smallint_Tab with the minimum value of
   * Smallint_Tab. Call the getObject(int columnno) method to retrieve this
   * value. Extract the minimum value from the tssql.stmt file. Compare this
   * value with the value returned by the getObject(int columnno) method. Both
   * the values should be equal.
   */

  public void testSetObject195() throws Fault {
    Double oMinSmallintVal = null;
    Integer rSmallintVal = null;
    String sminSmallintVal = null;
    Integer minSmallintVal = null;

    try {
      // to create the Smallint Table
      rsSch.createTab("Smallint_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Smallint_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Minimum Value of Smallint to be Updated ");
      sminSmallintVal = rsSch.extractVal("Smallint_Tab", 2, sqlp, conn);

      oMinSmallintVal = new Double(sminSmallintVal);
      minSmallintVal = new Integer(sminSmallintVal);

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
      rSmallintVal = new Integer(orSmallintVal.toString());

      msg.addOutputMsg("" + minSmallintVal, "" + rSmallintVal);
      if (rSmallintVal.equals(minSmallintVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed!");

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
   * @testName: testSetObject196
   * 
   * @assertion_ids: JDBC:SPEC:9;JDBC:SPEC:26; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Min_Val of Integer_Tab with the maximum value of
   * Integer_Tab. Call the getObject(int columnno) method to retrieve this
   * value. Extract the maximum value from the tssql.stmt file. Compare this
   * value with the value returned by the getObject(int columnno) method. Both
   * the values should be equal.
   */

  public void testSetObject196() throws Fault {
    Double oMaxIntegerVal = null;
    Integer rIntegerVal = null;
    String smaxIntegerVal = null;
    Integer maxIntegerVal = null;

    try {
      // to create the Integer Table
      rsSch.createTab("Integer_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Integer_Tab_Min_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Maximum Value of Integer to be Updated ");
      smaxIntegerVal = rsSch.extractVal("Integer_Tab", 1, sqlp, conn);

      oMaxIntegerVal = new Double(smaxIntegerVal);
      maxIntegerVal = new Integer(smaxIntegerVal);

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
      rIntegerVal = new Integer(orIntegerVal.toString());

      msg.addOutputMsg("" + maxIntegerVal, "" + rIntegerVal);
      if (rIntegerVal.equals(maxIntegerVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed!");

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
   * @testName: testSetObject197
   * 
   * @assertion_ids: JDBC:SPEC:9;JDBC:SPEC:26; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Null_Val of Integer_Tab with the minimum value of
   * Integer_Tab. Call the getObject(int columnno) method to retrieve this
   * value. Extract the minimum value from the tssql.stmt file. Compare this
   * value with the value returned by the getObject(int columnno) method. Both
   * the values should be equal.
   */

  public void testSetObject197() throws Fault {
    Double oMinIntegerVal = null;
    Integer rIntegerVal = null;
    String sminIntegerVal = null;
    Integer minIntegerVal = null;

    try {
      // to create the Integer Table
      rsSch.createTab("Integer_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Integer_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Minimum Value of Integer to be Updated");
      sminIntegerVal = rsSch.extractVal("Integer_Tab", 2, sqlp, conn);

      oMinIntegerVal = new Double(sminIntegerVal);
      minIntegerVal = new Integer(sminIntegerVal);

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
      rIntegerVal = new Integer(orIntegerVal.toString());

      msg.addOutputMsg("" + minIntegerVal, "" + rIntegerVal);
      if (rIntegerVal.equals(minIntegerVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed!");

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
   * @testName: testSetObject198
   * 
   * @assertion_ids: JDBC:SPEC:9;JDBC:SPEC:26; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method, update the column Min_Val of Bigint_Tab with the a long value after
   * converting that to a Double. Call the getObject(int columnno) method to
   * retrieve this value. Compare this value with the value being sent to
   * database. Both the values should be equal.
   */

  public void testSetObject198() throws Fault {
    Double oMaxBigintVal = null;
    Long rBigintVal = null;
    String smaxBigintVal = null;
    Long maxBigintVal = null;

    try {
      // to create the Long Table
      rsSch.createTab("Bigint_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Bigint_Tab_Min_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      long longVal = 1000;
      smaxBigintVal = new Long(longVal).toString();

      oMaxBigintVal = new Double(smaxBigintVal);
      maxBigintVal = new Long(smaxBigintVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, oMaxBigintVal, java.sql.Types.BIGINT);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
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
            "test setObject Failed!");

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
   * @testName: testSetObject200
   * 
   * @assertion_ids: JDBC:SPEC:9;JDBC:SPEC:26; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Min_Val of Real_Tab with the maximum value of
   * Real_Tab. Call the getObject(int columnno) method to retrieve this value.
   * Extract the maximum value from the tssql.stmt file. Compare this value with
   * the value returned by the getObject(int columnno) method. Both the values
   * should be equal.
   */

  public void testSetObject200() throws Fault {
    Double oMaxRealVal = null;
    Float rRealVal = null;
    String smaxRealVal = null;
    Float maxRealVal = null;

    try {
      // to create the Real Table
      rsSch.createTab("Real_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Real_Tab_Min_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Maximum Value of Float to be Updated ");
      smaxRealVal = rsSch.extractVal("Real_Tab", 1, sqlp, conn);

      oMaxRealVal = new Double(smaxRealVal);
      maxRealVal = new Float(smaxRealVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, oMaxRealVal, java.sql.Types.REAL);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
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
            "test setObject Failed!");

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

  /*
   * @testName: testSetObject201
   * 
   * @assertion_ids: JDBC:SPEC:9;JDBC:SPEC:26; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Null_Val of Real_Tab with the minimum value of
   * Real_Tab. Call the getObject(int columnno) method to retrieve this value.
   * Extract the minimum value from the tssql.stmt file. Compare this value with
   * the value returned by the getObject(int columnno) method. Both the values
   * should be equal.
   */

  public void testSetObject201() throws Fault {
    Double oMinRealVal = null;
    Float rRealVal = null;
    String sminRealVal = null;
    Float minRealVal = null;

    try {
      // to create the Bigint Table
      rsSch.createTab("Real_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Real_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Minimum Value of Long to be Updated");
      sminRealVal = rsSch.extractVal("Real_Tab", 2, sqlp, conn);

      oMinRealVal = new Double(sminRealVal);
      minRealVal = new Float(sminRealVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, oMinRealVal, java.sql.Types.REAL);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
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
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed!");

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

  /*
   * @testName: testSetObject202
   * 
   * @assertion_ids: JDBC:SPEC:9;JDBC:SPEC:26; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Min_Val of Float_Tab with the maximum value of
   * Float_Tab. Call the getObject(int columnno) method to retrieve this value.
   * Extract the maximum value from the tssql.stmt file. Compare this value with
   * the value returned by the getObject(int columnno) method. Both the values
   * should be equal.
   */

  public void testSetObject202() throws Fault {
    Double oMaxFloatVal = null;
    Double rFloatVal = null;
    String smaxFloatVal = null;
    Double maxFloatVal = null;

    try {
      // to create the Float Table
      rsSch.createTab("Float_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Float_Tab_Min_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Maximum Value of Float to be Updated ");
      smaxFloatVal = rsSch.extractVal("Float_Tab", 1, sqlp, conn);

      oMaxFloatVal = new Double(smaxFloatVal);
      maxFloatVal = new Double(smaxFloatVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, oMaxFloatVal, java.sql.Types.FLOAT);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
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
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed!");

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
        rsSch.dropTab("Float_Tab", conn);
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
