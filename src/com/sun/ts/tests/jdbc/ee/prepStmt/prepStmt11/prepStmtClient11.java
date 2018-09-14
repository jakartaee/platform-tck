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
 * @(#)prepStmtClient11.java	1.16 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.prepStmt.prepStmt11;

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
 * The prepStmtClient11 class tests methods of PreparedStatement interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.8, 11/27/00
 */

public class prepStmtClient11 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.prepStmt.prepStmt11";

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
    prepStmtClient11 theTests = new prepStmtClient11();
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
   * @testName: testSetObject143
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_Val of Smallint_Tab with the minimum value of Smallint_Tab.
   * Call the getObject(int columnno) method to retrieve this value. Extract the
   * minimum value from the tssql.stmt file. Compare this value with the value
   * returned by the getObject(int columnno) method. Both the values should be
   * equal.
   */

  public void testSetObject143() throws Fault {
    Integer minIntegerVal = null;
    Long minLongVal = null;
    Integer rSmallintVal = null;
    String sminStringVal = null;

    try {
      // to create the Smallint Table
      rsSch.createTab("Smallint_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Smallint_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Minimum Value of Smallint to be Updated");
      sminStringVal = rsSch.extractVal("Smallint_Tab", 2, sqlp, conn);

      minLongVal = new Long(sminStringVal);
      minIntegerVal = new Integer(sminStringVal);
      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, minLongVal, java.sql.Types.SMALLINT);
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
   * @testName: testSetObject144
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Min_Val of Integer_Tab with the maximum value of Integer_Tab.
   * Call the getObject(int columnno) method to retrieve this value. Extract the
   * maximum value from the tssql.stmt file. Compare this value with the value
   * returned by the getObject(int columnno) method. Both the values should be
   * equal.
   */

  public void testSetObject144() throws Fault {

    Integer maxIntegerVal = null;
    Integer rIntegerVal = null;
    Long maxLongVal = null;
    String smaxStringVal = null;

    try {
      // to create the Integer Table
      rsSch.createTab("Integer_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Integer_Tab_Min_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Maximum Value of Integer to be Updated");
      smaxStringVal = rsSch.extractVal("Integer_Tab", 1, sqlp, conn);

      maxIntegerVal = new Integer(smaxStringVal);
      maxLongVal = new Long(smaxStringVal);
      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxLongVal, java.sql.Types.INTEGER);
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
   * @testName: testSetObject145
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_Val of Integer_Tab with the minimum value of Integer_Tab.
   * Call the getObject(int columnno) method to retrieve this value. Extract the
   * minimum value from the tssql.stmt file. Compare this value with the value
   * returned by the getObject(int columnno) method. Both the values should be
   * equal.
   */

  public void testSetObject145() throws Fault {
    Integer minIntegerVal = null;
    Integer rIntegerVal = null;
    Long minLongVal = null;
    String sminStringVal = null;

    try {
      // to create the Integer Table
      rsSch.createTab("Integer_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Integer_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Minimum Value of Integer to be Updated");
      sminStringVal = rsSch.extractVal("Integer_Tab", 2, sqlp, conn);

      minIntegerVal = new Integer(sminStringVal);
      minLongVal = new Long(sminStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, minLongVal, java.sql.Types.INTEGER);
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
        rsSch.dropTab("Integer_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject146
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Min_Val of Bigint_Tab with the maximum value of Bigint_Tab. Call
   * the getObject(int columnno) method to retrieve this value. Extract the
   * maximum value from the tssql.stmt file. Compare this value with the value
   * returned by the getObject(int columnno) method. Both the values should be
   * equal.
   */

  public void testSetObject146() throws Fault {
    Long maxBigintVal = null;
    Long rBigintVal = null;
    String smaxStringVal = null;

    try {
      // to create the Bigint Table
      rsSch.createTab("Bigint_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Bigint_Tab_Min_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Maximum Value of Bigint to be Updated");
      smaxStringVal = rsSch.extractVal("Bigint_Tab", 1, sqlp, conn);

      maxBigintVal = new Long(smaxStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxBigintVal, java.sql.Types.BIGINT);
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
   * @testName: testSetObject147
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_Val of Bigint_Tab with the minimum value of Bigint_Tab.
   * Call the getObject(int columnno) method to retrieve this value. Extract the
   * minimum value from the tssql.stmt file. Compare this value with the value
   * returned by the getObject(int columnno) method. Both the values should be
   * equal.
   */

  public void testSetObject147() throws Fault {
    Long minBigintVal = null;
    Long rBigintVal = null;
    String sminStringVal = null;

    try {
      // to create the Bigint Table
      rsSch.createTab("Bigint_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Bigint_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Minimum Value of Bigint to be Updated ");
      sminStringVal = rsSch.extractVal("Bigint_Tab", 2, sqlp, conn);

      minBigintVal = new Long(sminStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, minBigintVal, java.sql.Types.BIGINT);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
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
   * @testName: testSetObject148
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Min_Val of Real_Tab with the maximum value of Bigint_Tab. Call
   * the getObject(int columnno) method to retrieve this value. Extract the
   * maximum value from the tssql.stmt file. Compare this value with the value
   * returned by the getObject(int columnno) method. Both the values should be
   * equal.
   */

  public void testSetObject148() throws Fault {
    Float maxRealVal = null;
    Long maxLongVal = null;
    Float rRealVal = null;
    String smaxStringVal = null;

    try {
      // to create the Real Table
      rsSch.createTab("Real_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Real_Tab_Min_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Maximum Value of Float to be Updated");
      smaxStringVal = rsSch.extractVal("Bigint_Tab", 1, sqlp, conn);

      maxLongVal = new Long(smaxStringVal);
      maxRealVal = new Float(smaxStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxLongVal, java.sql.Types.REAL);
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

  /*
   * @testName: testSetObject149
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_Val of Real_Tab with the minimum value of Bigint_Tab. Call
   * the getObject(int columnno) method to retrieve this value. Extract the
   * minimum value from the tssql.stmt file. Compare this value with the value
   * returned by the getObject(int columnno) method. Both the values should be
   * equal.
   */

  public void testSetObject149() throws Fault {
    Float minRealVal = null;
    Long minLongVal = null;
    Float rRealVal = null;
    String sminStringVal = null;

    try {
      // to create the Real Table
      rsSch.createTab("Real_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Real_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Minimum Value of Float to be Updated");
      sminStringVal = rsSch.extractVal("Bigint_Tab", 2, sqlp, conn);

      minLongVal = new Long(sminStringVal);
      minRealVal = new Float(sminStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, minLongVal, java.sql.Types.REAL);
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

  /*
   * @testName: testSetObject150
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column min_Val of Float_Tab with the maximum value of Float_Tab. Call
   * the getObject(int columnno) method to retrieve this value. Extract the
   * maximum value from the tssql.stmt file. Compare this value with the value
   * returned by the getObject(int columnno) method. Both the values should be
   * equal.
   */

  public void testSetObject150() throws Fault {
    Float maxFloatVal = null;
    Float rFloatVal = null;
    BigDecimal maxBigDecimalVal = null;
    String smaxStringVal = null;

    try {
      // to create the Float Table
      rsSch.createTab("Float_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Float_Tab_Min_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Maximum Value of Float to be Updated");
      smaxStringVal = rsSch.extractVal("Float_Tab", 1, sqlp, conn);

      maxFloatVal = new Float(smaxStringVal);
      maxBigDecimalVal = new BigDecimal(smaxStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxBigDecimalVal, java.sql.Types.FLOAT);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Min_Val_Query = sqlp.getProperty("Float_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      Object orFloatVal = rs.getObject(1);
      rFloatVal = new Float(orFloatVal.toString());

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

  /*
   * @testName: testSetObject151
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_Val of Float_Tab with the minimum value of Float_Tab. Call
   * the getObject(int columnno) method to retrieve this value. Extract the
   * minimum value from the tssql.stmt file. Compare this value with the value
   * returned by the getObject(int columnno) method. Both the values should be
   * equal.
   */

  public void testSetObject151() throws Fault {
    Float minFloatVal = null;
    Float rFloatVal = null;
    BigDecimal minBigDecimalVal = null;
    String sminStringVal = null;

    try {
      // to create the Float Table
      rsSch.createTab("Float_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Float_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Maximum Value of Float to be Updated");
      sminStringVal = rsSch.extractVal("Float_Tab", 2, sqlp, conn);

      minFloatVal = new Float(sminStringVal);
      minBigDecimalVal = new BigDecimal(sminStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, minBigDecimalVal, java.sql.Types.FLOAT);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Float_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      Object orFloatVal = rs.getObject(1);
      rFloatVal = new Float(orFloatVal.toString());

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

  /*
   * @testName: testSetObject152
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method, update
   * the column Min_Val of Double_Tab with the maximum value of Double_Tab. Call
   * the getObject(int columnno) method to retrieve this value. Extract the
   * minimum value from the tssql.stmt file. Compare this value with the value
   * returned by the getObject(int columnno) method. Both the values should be
   * equal.
   */

  public void testSetObject152() throws Fault {
    BigDecimal maxBigDecimalVal = null;
    Double maxDoubleVal = null;
    Double rDoubleVal = null;
    String smaxStringVal = null;

    try {
      // to create the Double Table
      rsSch.createTab("Double_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Double_Tab_Min_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Minimum Value of Double to be Updated ");
      smaxStringVal = rsSch.extractVal("Double_Tab", 1, sqlp, conn);

      maxDoubleVal = new Double(smaxStringVal);
      maxBigDecimalVal = new BigDecimal(smaxStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxBigDecimalVal, java.sql.Types.DOUBLE);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Min_Val_Query = sqlp.getProperty("Double_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      Object orDoubleVal = rs.getObject(1);
      rDoubleVal = new Double(orDoubleVal.toString());

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
   * @testName: testSetObject153
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_Val of Double_Tab with the minimum value of Double_Tab.
   * Call the getObject(int columnno) method to retrieve this value. Extract the
   * minimum value from the tssql.stmt file. Compare this value with the value
   * returned by the getObject(int columnno) method. Both the values should be
   * equal.
   */

  public void testSetObject153() throws Fault {
    BigDecimal minBigDecimalVal = null;
    Double minDoubleVal = null;
    Double rDoubleVal = null;
    String sminStringVal = null;

    try {
      // to create the Double Table
      rsSch.createTab("Double_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Double_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Minimum Value of Double to be Updated ");
      sminStringVal = rsSch.extractVal("Double_Tab", 2, sqlp, conn);

      minDoubleVal = new Double(sminStringVal);
      minBigDecimalVal = new BigDecimal(sminStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, minBigDecimalVal, java.sql.Types.DOUBLE);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Double_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      Object orDoubleVal = rs.getObject(1);
      rDoubleVal = new Double(orDoubleVal.toString());

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
        rsSch.dropTab("Double_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject154
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:692; JDBC:JAVADOC:693;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_Val of Decimal_Tab with the maximum value of Integer_Tab.
   * Call the getObject(int columnno) method to retrieve this value. Extract the
   * maximum value from the tssql.stmt file. Compare this value with the value
   * returned by the getObject(int columnno) method. Both the values should be
   * equal.
   */

  public void testSetObject154() throws Fault {
    BigDecimal maxDecimalVal = null;
    BigDecimal rDecimalVal = null;
    Long maxLongVal = null;
    String smaxStringVal = null;

    try {
      // to create the Decimal Table
      rsSch.createTab("Decimal_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Decimal_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Maximum Value of Decimal to be Updated ");
      smaxStringVal = rsSch.extractVal("Integer_Tab", 1, sqlp, conn);

      maxLongVal = new Long(smaxStringVal);
      maxDecimalVal = new BigDecimal(smaxStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxLongVal, java.sql.Types.DECIMAL, 2);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
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
   * @testName: testSetObject155
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:692; JDBC:JAVADOC:693;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_Val of Decimal_Tab with the minimum value of Integer_Tab.
   * Call the getObject(int columnno) method to retrieve this value. Extract the
   * minimum value from the tssql.stmt file. compare this value with the value
   * returned by the getObject(int columnno) method. Both the values should be
   * equal.
   */

  public void testSetObject155() throws Fault {
    BigDecimal minDecimalVal = null;
    BigDecimal rDecimalVal = null;
    Long minLongVal = null;
    String sminStringVal = null;

    try {
      // to create the Decimal Table
      rsSch.createTab("Decimal_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Decimal_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Minimum Value of Decimal to be Updated");
      sminStringVal = rsSch.extractVal("Integer_Tab", 2, sqlp, conn);

      minDecimalVal = new BigDecimal(sminStringVal);
      minLongVal = new Long(sminStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, minLongVal, java.sql.Types.DECIMAL, 2);
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
   * @testName: testSetObject156
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:692; JDBC:JAVADOC:693;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Min_Val of Numeric_Tab with the maximum value of Integer_Tab.
   * Call the getObject(int columnno) method to retrieve this value. Extract the
   * maximum value from the tssql.stmt file. Compare this value with the value
   * returned by the getObject(int columnno) method. Both the values should be
   * equal.
   */

  public void testSetObject156() throws Fault {
    BigDecimal maxNumericVal = null;
    BigDecimal rNumericVal = null;
    Long maxLongVal = null;
    String smaxStringVal = null;

    try {
      // to create the Numeric Table
      rsSch.createTab("Numeric_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Numeric_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Minimum Value of Numeric to be Updated ");
      smaxStringVal = rsSch.extractVal("Integer_Tab", 1, sqlp, conn);

      maxNumericVal = new BigDecimal(smaxStringVal);
      maxLongVal = new Long(smaxStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxLongVal, java.sql.Types.NUMERIC, 2);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
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
   * @testName: testSetObject157
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:692; JDBC:JAVADOC:693;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_Val of Numeric_Tab with the minimum value of Integer_Tab.
   * Call the getObject(int columnno) method to retrieve this value. Extract the
   * minimum value from the tssql.stmt file. Compare this value with the value
   * returned by the getObject(int columnno) method. Both the values should be
   * equal.
   */

  public void testSetObject157() throws Fault {

    BigDecimal minNumericVal = null;
    BigDecimal rNumericVal = null;
    Long minLongVal = null;
    String sminStringVal = null;

    try {
      // to create the Numeric Table
      rsSch.createTab("Numeric_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Numeric_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Maximum Value of Numeric to be Updated ");
      sminStringVal = rsSch.extractVal("Integer_Tab", 2, sqlp, conn);

      minNumericVal = new BigDecimal(sminStringVal);
      minLongVal = new Long(sminStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, minLongVal, java.sql.Types.NUMERIC, 2);
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
   * @testName: testSetObject160
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_Val of Char_Tab with the maximum value of Bigint_Tab. Call
   * the getObject(int columnno) method to retrieve this value. Extract the
   * maximum value from the tssql.stmt file. Compare this value with the value
   * returned by the getObject(int columnno) method. Both the values should be
   * equal.
   */

  public void testSetObject160() throws Fault {
    String maxStringVal = null;
    String rStringVal = null;
    Long maxLongVal = null;

    try {
      rsSch.createTab("Char_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Char_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg(
          "extract the Maximum Value of Bigint, to be Updated for NULL value of Char Table");
      maxStringVal = rsSch.extractVal("Bigint_Tab", 1, sqlp, conn);
      maxLongVal = new Long(maxStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxLongVal, java.sql.Types.CHAR);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
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
   * @testName: testSetObject161
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_Val of Char_Tab with the minimum value of Bigint_Tab. Call
   * the getObject(int columnno) method to retrieve this value. Extract the
   * minimum value from the tssql.stmt file. Compare this value with the value
   * returned by the getObject(int columnno) method. Both the values should be
   * equal.
   */

  public void testSetObject161() throws Fault {
    String maxStringVal = null;
    String rStringVal = null;
    Long maxLongVal = null;

    try {
      rsSch.createTab("Char_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Char_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);
      msg.setMsg(
          "extract the Minimum Value of Bigint, to be Updated for NULL value of Char Table ");
      maxStringVal = rsSch.extractVal("Bigint_Tab", 2, sqlp, conn);
      maxLongVal = new Long(maxStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxLongVal, java.sql.Types.CHAR);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
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
   * @testName: testSetObject162
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_Val of Varchar_Tab with the maximum value of Bigint_Tab.
   * Call the getObject(int columnno) method to retrieve this value. Extract the
   * maximum value from the tssql.stmt file. Compare this value with the value
   * returned by the getObject(int columnno) method. Both the values should be
   * equal.
   */

  public void testSetObject162() throws Fault {
    Long maxLongVal = null;
    String maxStringVal = null;
    String rStringVal = null;

    try {
      rsSch.createTab("Varchar_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Varchar_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg(
          "extract the Maximum Value of Bigint to be Updated for NULL value of Varchar Table");
      maxStringVal = rsSch.extractVal("Bigint_Tab", 1, sqlp, conn);
      maxLongVal = new Long(maxStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxLongVal, java.sql.Types.VARCHAR);
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
