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
 * @(#)dbMetaClient4.java	1.27 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.dbMeta.dbMeta4;

import java.io.*;
import java.util.*;

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
 * The dbMetaClient4 class tests methods of DatabaseMetaData interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class dbMetaClient4 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.dbMeta.dbMeta4";

  // Naming specific member variables
  private TSNamingContextInterface jc = null;

  // Harness requirements

  private transient Connection conn = null;

  private transient DatabaseMetaData dbmd = null;

  private DataSource ds1 = null;

  private dbSchema dbSch = null;

  private String dbName = null, dbUser = null, drManager = null;

  private String sCatalogName = null, sSchemaName = null, sPtable = null,
      sFtable = null;

  private JDBCTestMsg msg = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    dbMetaClient4 theTests = new dbMetaClient4();
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
        dbName = p.getProperty("db1", "");
        dbUser = p.getProperty("user1", "");
        sPtable = p.getProperty("ptable", "TSTABLE1");
        sFtable = p.getProperty("ftable", "TSTABLE2");
        if (dbName.length() == 0)
          throw new Fault("Invalid db1  Database Name");
        if (dbUser.length() == 0)
          throw new Fault("Invalid Login Id");
        if (sPtable.length() == 0)
          throw new Fault("Invalid Primary table");
        if (sFtable.length() == 0)
          throw new Fault("Invalid Foreign table");
        if (drManager.length() == 0)
          throw new Fault("Invalid DriverManager Name");

        int nLocdbname = dbName.indexOf('=');
        sCatalogName = dbName.substring(nLocdbname + 1);
        sCatalogName = sCatalogName.trim();
        sSchemaName = dbUser;

        if (drManager.equals("yes")) {
          logTrace("Using DriverManager");
          DriverManagerConnection dmCon = new DriverManagerConnection();
          conn = dmCon.getConnection(p);
        } else {
          logTrace("Using DataSource");
          DataSourceConnection dsCon = new DataSourceConnection();
          conn = dsCon.getConnection(p);
        }
        dbSch = new dbSchema();
        dbSch.createData(p, conn);
        dbmd = conn.getMetaData();
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
   * @testName: testSupportsConvert23
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(STRUCT, VARCHAR) method on that
   * object. It should return a boolean value; either true or false.
   *
   */
  public void testSupportsConvert23() throws Fault {
    try {
      // invoke on the supportsConvert
      msg.setMsg(
          "Calling supportsConvert(STRUCT, VARCHAR) on DatabaseMetaData");
      boolean retValue = dbmd.supportsConvert(Types.STRUCT, Types.VARCHAR);
      if (retValue) {
        msg.setMsg("supportsConvert(STRUCT VARCHAR) method is supported");
      } else {
        msg.setMsg("supportsConvert(STRUCT VARCHAR) method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsConvert is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsConvert is Failed!");

    }
  }

  /*
   * @testName: testSupportsConvert24
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(TIME, VARCHAR) method on that object.
   * It should return a boolean value; either true or false.
   *
   */
  public void testSupportsConvert24() throws Fault {
    try {
      // invoke on the supportsConvert
      msg.setMsg("Calling supportsConvert(TIME, VARCHAR) on DatabaseMetaData");
      boolean retValue = dbmd.supportsConvert(Types.TIME, Types.VARCHAR);
      if (retValue) {
        msg.setMsg("supportsConvert(TIME VARCHAR) method is supported");
      } else {
        msg.setMsg("supportsConvert(TIME VARCHAR) method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsConvert is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsConvert is Failed!");

    }
  }

  /*
   * @testName: testSupportsConvert25
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(TIMESTAMP, VARCHAR) method on that
   * object. It should return a boolean value; either true or false.
   *
   */
  public void testSupportsConvert25() throws Fault {
    try {
      // invoke on the supportsConvert
      msg.setMsg(
          "Calling supportsConvert(TIMESTAMP, VARCHAR) on DatabaseMetaData");
      boolean retValue = dbmd.supportsConvert(Types.TIMESTAMP, Types.VARCHAR);
      if (retValue) {
        msg.setMsg("supportsConvert(TIMESTAMP VARCHAR) method is supported");
      } else {
        msg.setMsg(
            "supportsConvert(TIMESTAMP VARCHAR) method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsConvert is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsConvert is Failed!");

    }
  }

  /*
   * @testName: testSupportsConvert26
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(TINYINT, VARCHAR) method on that
   * object. It should return a boolean value; either true or false.
   *
   */
  public void testSupportsConvert26() throws Fault {
    try {
      // invoke on the supportsConvert
      msg.setMsg(
          "Calling supportsConvert(TINYINT, VARCHAR) on DatabaseMetaData");
      boolean retValue = dbmd.supportsConvert(Types.TINYINT, Types.VARCHAR);
      if (retValue) {
        msg.setMsg("supportsConvert(TINYINT VARCHAR) method is supported");
      } else {
        msg.setMsg("supportsConvert(TINYINT VARCHAR) method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsConvert is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsConvert is Failed!");

    }
  }

  /*
   * @testName: testSupportsConvert27
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(VARBINARY, VARCHAR) method on that
   * object. It should return a boolean value; either true or false.
   *
   */
  public void testSupportsConvert27() throws Fault {
    try {
      // invoke on the supportsConvert
      msg.setMsg(
          "Calling supportsConvert(VARBINARY, VARCHAR) on DatabaseMetaData");
      boolean retValue = dbmd.supportsConvert(Types.VARBINARY, Types.VARCHAR);
      if (retValue) {
        msg.setMsg("supportsConvert(VARBINARY VARCHAR) method is supported");
      } else {
        msg.setMsg(
            "supportsConvert(VARBINARY VARCHAR) method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsConvert is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsConvert is Failed!");

    }
  }

  /*
   * @testName: testSupportsConvert28
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(BIGINT, INTEGER) method on that
   * object. It should return a boolean value; either true or false.
   *
   */
  public void testSupportsConvert28() throws Fault {
    try {
      // invoke on the supportsConvert
      msg.setMsg(
          "Calling supportsConvert(BIGINT, INTEGER) on DatabaseMetaData");
      boolean retValue = dbmd.supportsConvert(Types.BIGINT, Types.INTEGER);
      if (retValue) {
        msg.setMsg("supportsConvert(BIGINT, INTEGER) method is supported");
      } else {
        msg.setMsg("supportsConvert(BIGINT, INTEGER) method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsConvert is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsConvert is Failed!");

    }
  }

  /*
   * @testName: testSupportsConvert29
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(BIT, INTEGER) method on that object.
   * It should return a boolean value; either true or false.
   *
   */
  public void testSupportsConvert29() throws Fault {
    try {
      // invoke on the supportsConvert
      msg.setMsg("Calling supportsConvert(BIT, INTEGER) on DatabaseMetaData");
      boolean retValue = dbmd.supportsConvert(Types.BIT, Types.INTEGER);
      if (retValue) {
        msg.setMsg("supportsConvert(BIT, INTEGER) method is supported");
      } else {
        msg.setMsg("supportsConvert(BIT, INTEGER) method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsConvert is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsConvert is Failed!");

    }
  }

  /*
   * @testName: testSupportsConvert30
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(DATE, INTEGER) method onn that
   * object. It should return a boolean value; either true or false.
   *
   */
  public void testSupportsConvert30() throws Fault {
    try {
      // invoke on the supportsConvert
      msg.setMsg("Calling supportsConvert(DATE, INTEGER) on DatabaseMetaData");
      boolean retValue = dbmd.supportsConvert(Types.DATE, Types.INTEGER);
      if (retValue) {
        msg.setMsg("supportsConvert(DATE, INTEGER) method is supported");
      } else {
        msg.setMsg("supportsConvert(DATE, INTEGER) method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsConvert is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsConvert is Failed!");

    }
  }

  /*
   * @testName: testSupportsConvert31
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(DECIMAL, INTEGER) method on that
   * object. It should return a boolean value; either true or false.
   *
   */
  public void testSupportsConvert31() throws Fault {
    try {
      // invoke on the supportsConvert
      msg.setMsg(
          "Calling supportsConvert(DECIMAL, INTEGER) on DatabaseMetaData");
      boolean retValue = dbmd.supportsConvert(Types.DECIMAL, Types.INTEGER);
      if (retValue) {
        msg.setMsg("supportsConvert(DECIMAL, INTEGER) method is supported");
      } else {
        msg.setMsg("supportsConvert(DECIMAL, INTEGER) method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsConvert is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsConvert is Failed!");

    }
  }

  /*
   * @testName: testSupportsConvert32
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(DOUBLE, INTEGER) method on that
   * object. It should return a boolean value; either true or false.
   *
   */
  public void testSupportsConvert32() throws Fault {
    try {
      // invoke on the supportsConvert
      msg.setMsg(
          "Calling supportsConvert(DOUBLE, INTEGER) on DatabaseMetaData");
      boolean retValue = dbmd.supportsConvert(Types.DOUBLE, Types.INTEGER);
      if (retValue) {
        msg.setMsg("supportsConvert(DOUBLE, INTEGER) method is supported");
      } else {
        msg.setMsg("supportsConvert(DOUBLE, INTEGER) method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsConvert is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsConvert is Failed!");

    }
  }

  /*
   * @testName: testSupportsConvert33
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(FLOAT, INTEGER) method on that
   * object. It should return a boolean value; either true or false.
   *
   */
  public void testSupportsConvert33() throws Fault {
    try {
      // invoke on the supportsConvert
      msg.setMsg("Calling supportsConvert(FLOAT, INTEGER) on DatabaseMetaData");
      boolean retValue = dbmd.supportsConvert(Types.FLOAT, Types.INTEGER);
      if (retValue) {
        msg.setMsg("supportsConvert(FLOAT, INTEGER) method is supported");
      } else {
        msg.setMsg("supportsConvert(FLOAT, INTEGER) method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsConvert is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsConvert is Failed!");

    }
  }

  /*
   * @testName: testSupportsConvert34
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(NUMERIC, INTEGER) method on that
   * object. It should return a boolean value; true or false.
   *
   */
  public void testSupportsConvert34() throws Fault {
    try {
      // invoke on the supportsConvert
      msg.setMsg(
          "Calling supportsConvert(NUMERIC, INTEGER) on DatabaseMetaData");
      boolean retValue = dbmd.supportsConvert(Types.NUMERIC, Types.INTEGER);
      if (retValue) {
        msg.setMsg("supportsConvert(NUMERIC, INTEGER) method is supported");
      } else {
        msg.setMsg("supportsConvert(NUMERIC, INTEGER) method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsConvert is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsConvert is Failed!");

    }
  }

  /*
   * @testName: testSupportsConvert35
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(REAL, INTEGER) method on that object.
   * It should return a boolean value; either true false.
   *
   */
  public void testSupportsConvert35() throws Fault {
    try {
      // invoke on the supportsConvert
      msg.setMsg("Calling supportsConvert(REAL, INTEGER) on DatabaseMetaData");
      boolean retValue = dbmd.supportsConvert(Types.REAL, Types.INTEGER);
      if (retValue) {
        msg.setMsg("supportsConvert(REAL, INTEGER) method is supported");
      } else {
        msg.setMsg("supportsConvert(REAL, INTEGER) method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsConvert is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsConvert is Failed!");

    }
  }

  /*
   * @testName: testSupportsConvert36
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(SMALLINT, INTEGER) method on that
   * object. It should return a boolean value; either true or false.
   *
   */
  public void testSupportsConvert36() throws Fault {
    try {
      // invoke on the supportsConvert
      boolean retValue = dbmd.supportsConvert(Types.SMALLINT, Types.INTEGER);
      msg.setMsg(
          "Calling supportsConvert(SMALLINT, INTEGER) on DatabaseMetaData");
      if (retValue) {
        msg.setMsg("supportsConvert(SMALLINT, INTEGER) method is supported");
      } else {
        msg.setMsg(
            "supportsConvert(SMALLINT, INTEGER) method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsConvert is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsConvert is Failed!");

    }
  }

  /*
   * @testName: testSupportsConvert37
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(TINYINT, INTEGER) method on that
   * object. It should return a boolean value; either true or false.
   *
   */
  public void testSupportsConvert37() throws Fault {
    try {
      // invoke on the supportsConvert
      msg.setMsg(
          "Calling supportsConvert(TINYINT, INTEGER) on DatabaseMetaData");
      boolean retValue = dbmd.supportsConvert(Types.TINYINT, Types.INTEGER);
      if (retValue) {
        msg.setMsg("supportsConvert(TINYINT, INTEGER) method is supported");
      } else {
        msg.setMsg("supportsConvert(TINYINT, INTEGER) method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsConvert is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsConvert is Failed!");

    }
  }

  /*
   * @testName: testSupportsTableCorrelationNames
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:884; JDBC:JAVADOC:885;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsTableCorrelationNames() method on that
   * object. It should return a boolean value; either true or false.
   *
   */
  public void testSupportsTableCorrelationNames() throws Fault {
    try {
      // invoke on the supportsTableCorrelationNames
      msg.setMsg("Calling supportsTableCorrelationNames on DatabaseMetaData");
      boolean retValue = dbmd.supportsTableCorrelationNames();
      if (retValue) {
        msg.setMsg("supportsTableCorrelationNames method is supported");
      } else {
        msg.setMsg("supportsTableCorrelationNames method is not supported");
      }
    } catch (SQLException sqle) {
      msg.printSQLError(sqle,
          "Call to supportsTableCorrelationNames is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsTableCorrelationNames is Failed!");

    }
  }

  /*
   * @testName: testSupportsDifferentTableCorrelationNames
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:886; JDBC:JAVADOC:887;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsDifferentTableCorrelationNames() method on
   * that object. It should return a boolean value; either true or false.
   *
   */
  public void testSupportsDifferentTableCorrelationNames() throws Fault {
    try {
      // invoke on the supportsDifferentTableCorrelationNames
      msg.setMsg(
          "Calling supportsDifferentTableCorrelationNames on DatabaseMetaData");
      boolean retValue = dbmd.supportsDifferentTableCorrelationNames();
      if (retValue) {
        msg.setMsg(
            "supportsDifferentTableCorrelationNames method is supported");
      } else {
        msg.setMsg(
            "supportsDifferentTableCorrelationNames method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle,
          "Call to supportsDifferentTableCorrelationNames is Failed!");

    } catch (Exception e) {
      msg.printError(e,
          "Call to supportsDifferentTableCorrelationNames is Failed!");

    }
  }

  /*
   * @testName: testSupportsExpressionsInOrderBy
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:888; JDBC:JAVADOC:889;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsExpressionsInOrderBy() method on that object.
   * It should return a boolean value; either true or false.
   *
   */
  public void testSupportsExpressionsInOrderBy() throws Fault {
    try {
      // invoke on the supportsExpressionsInOrderBy
      msg.setMsg("Calling supportsExpressionsInOrderBy on DatabaseMetaData");
      boolean retValue = dbmd.supportsExpressionsInOrderBy();
      if (retValue) {
        msg.setMsg("supportsExpressionsInOrderBy method is supported");
      } else {
        msg.setMsg("supportsExpressionsInOrderBy method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle,
          "Call to supportsExpressionsInOrderBy is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsExpressionsInOrderBy is Failed!");

    }
  }

  /*
   * @testName: testSupportsOrderByUnrelated
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:890; JDBC:JAVADOC:891;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsOrderByUnrelated() method on that object. It
   * should return a boolean value; either true or false.
   *
   */
  public void testSupportsOrderByUnrelated() throws Fault {
    try {
      // invoke on the supportsOrderByUnrelated
      msg.setMsg("Calling supportsOrderByUnrelated on DatabaseMetaData");
      boolean retValue = dbmd.supportsOrderByUnrelated();
      if (retValue) {
        msg.setMsg("supportsOrderByUnrelated method is supported");
      } else {
        msg.setMsg("supportsOrderByUnrelated method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsOrderByUnrelated is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsOrderByUnrelated is Failed!");

    }
  }

  /*
   * @testName: testSupportsGroupBy
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:892; JDBC:JAVADOC:893;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsGroupBy() method on that object. It should
   * return a boolean value; either true or false.
   *
   */
  public void testSupportsGroupBy() throws Fault {
    try {
      // invoke on the supportsGroupBy
      msg.setMsg("Calling supportsGroupBy on DatabaseMetaData");
      boolean retValue = dbmd.supportsGroupBy();
      if (retValue) {
        msg.setMsg("supportsGroupBy method is supported");
      } else {
        msg.setMsg("supportsGroupBy method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsGroupBy is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsGroupBy is Failed!");

    }
  }

  /* cleanup */
  public void cleanup() throws Fault {
    try {
      // Close the database
      dbSch.destroyData(conn);
      dbSch.dbUnConnect(conn);
      logMsg("Cleanup ok;");
    } catch (Exception e) {
      logErr("An error occurred while closing the database connection", e);
    }
  }
}
