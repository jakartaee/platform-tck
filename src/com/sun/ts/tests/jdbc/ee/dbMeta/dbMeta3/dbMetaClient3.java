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
 * @(#)dbMetaClient3.java	1.27 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.dbMeta.dbMeta3;

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
 * The dbMetaClient3 class tests methods of DatabaseMetaData interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class dbMetaClient3 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.dbMeta.dbMeta3";

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
    dbMetaClient3 theTests = new dbMetaClient3();
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
   * @testName: testSupportsConvert03
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(BINARY, VARCHAR) method on that
   * object. It should return a boolean value; either true or false.
   *
   */
  public void testSupportsConvert03() throws Fault {
    try {
      // invoke on the supportsConvert
      boolean retValue = dbmd.supportsConvert(Types.BINARY, Types.VARCHAR);
      msg.setMsg(
          "Calling supportsConvert(BINARY, VARCHAR) on DatabaseMetaData");
      if (retValue) {
        msg.setMsg("supportsConvert(BINARY, VARCHAR) method is supported");
      } else {
        msg.setMsg("supportsConvert(BINARY, VARCHAR) method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsConvert is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsConvert is Failed!");

    }
  }

  /*
   * @testName: testSupportsConvert04
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   * 
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(BIT, VARCHAR) method on that object.
   * It should return a boolean value; either true or false.
   *
   */
  public void testSupportsConvert04() throws Fault {
    try {
      // invoke on the supportsConvert
      msg.setMsg("Calling supportsConvert(BIT, VARCHAR) on DatabaseMetaData");
      boolean retValue = dbmd.supportsConvert(Types.BIT, Types.VARCHAR);
      if (retValue) {
        msg.setMsg("supportsConvert(BIT, VARCHAR) method is supported");
      } else {
        msg.setMsg("supportsConvert(BIT, VARCHAR) method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsConvert is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsConvert is Failed!");

    }
  }

  /*
   * @testName: testSupportsConvert05
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(BLOB, VARCHAR) method on that object.
   * It should return a boolean value; either true or false.
   *
   */
  public void testSupportsConvert05() throws Fault {
    try {
      // invoke on the supportsConvert
      msg.setMsg("Calling supportsConvert(BLOB, VARCHAR) on DatabaseMetaData");
      boolean retValue = dbmd.supportsConvert(Types.BLOB, Types.VARCHAR);
      if (retValue) {
        msg.setMsg("supportsConvert(BLOB, VARCHAR) method is supported");
      } else {
        msg.setMsg("supportsConvert(BLOB, VARCHAR) method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsConvert is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsConvert is Failed!");

    }
  }

  /*
   * @testName: testSupportsConvert06
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(CHAR, VARCHAR) method on that object.
   * It should return a boolean value; either true or false.
   *
   */
  public void testSupportsConvert06() throws Fault {
    try {
      // invoke on the supportsConvert
      msg.setMsg("Calling supportsConvert(CHAR, VARCHAR) on DatabaseMetaData");
      boolean retValue = dbmd.supportsConvert(Types.CHAR, Types.VARCHAR);
      if (retValue) {
        msg.setMsg("supportsConvert(CHAR, VARCHAR) method is supported");
      } else {
        msg.setMsg("supportsConvert(CHAR, VARCHAR) method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsConvert is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsConvert is Failed!");

    }
  }

  /*
   * @testName: testSupportsConvert07
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(CLOB, VARCHAR) method on that object.
   * It should return a boolean value; either true or false.
   *
   */
  public void testSupportsConvert07() throws Fault {
    try {
      // invoke on the supportsConvert
      msg.setMsg("Calling supportsConvert(CLOB, VARCHAR) on DatabaseMetaData");
      boolean retValue = dbmd.supportsConvert(Types.CLOB, Types.VARCHAR);
      if (retValue) {
        msg.setMsg("supportsConvert(CLOB, VARCHAR) method is supported");
      } else {
        msg.setMsg("supportsConvert(CLOB, VARCHAR) method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsConvert is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsConvert is Failed!");

    }
  }

  /*
   * @testName: testSupportsConvert08
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(DATE, VARCHAR) method on that object.
   * It should return a boolean value; either true or false.
   *
   */
  public void testSupportsConvert08() throws Fault {
    try {
      // invoke on the supportsConvert
      msg.setMsg("Calling supportsConvert(DATE, VARCHAR) on DatabaseMetaData");
      boolean retValue = dbmd.supportsConvert(Types.DATE, Types.VARCHAR);
      if (retValue) {
        msg.setMsg("supportsConvert(DATE, VARCHAR) method is supported");
      } else {
        msg.setMsg("supportsConvert(DATE, VARCHAR) method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsConvert is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsConvert is Failed!");

    }
  }

  /*
   * @testName: testSupportsConvert09
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(DECIMAL, VARCHAR) method on that
   * object. It should return a boolean value; either true or false.
   *
   */
  public void testSupportsConvert09() throws Fault {
    try {
      // invoke on the supportsConvert
      msg.setMsg(
          "Calling supportsConvert(DECIMAL, VARCHAR) on DatabaseMetaData");
      boolean retValue = dbmd.supportsConvert(Types.DECIMAL, Types.VARCHAR);
      if (retValue) {
        msg.setMsg("supportsConvert(DECIMAL, VARCHAR) method is supported");
      } else {
        msg.setMsg("supportsConvert(DECIMAL, VARCHAR) method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsConvert is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsConvert is Failed!");

    }
  }

  /*
   * @testName: testSupportsConvert10
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(DISTINCT, VARCHAR) method on that
   * object. It should return a boolean value; either true or false.
   *
   */
  public void testSupportsConvert10() throws Fault {
    try {
      // invoke on the supportsConvert
      msg.setMsg(
          "Calling supportsConvert(DISTINCT, VARCHAR) on DatabaseMetaData");
      boolean retValue = dbmd.supportsConvert(Types.DISTINCT, Types.VARCHAR);
      if (retValue) {
        msg.setMsg("supportsConvert(DISTINCT, VARCHAR) method is supported");
      } else {
        msg.setMsg(
            "supportsConvert(DISTINCT, VARCHAR) method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsConvert is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsConvert is Failed!");

    }
  }

  /*
   * @testName: testSupportsConvert11
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(DOUBLE, VARCHAR) method on that
   * object. It should return a boolean value; either true or false.
   *
   */
  public void testSupportsConvert11() throws Fault {
    try {
      // invoke on the supportsConvert
      msg.setMsg(
          "Calling supportsConvert(DOUBLE, VARCHAR) on DatabaseMetaData");
      boolean retValue = dbmd.supportsConvert(Types.DOUBLE, Types.VARCHAR);
      if (retValue) {
        msg.setMsg("supportsConvert(DOUBLE, VARCHAR) method is supported");
      } else {
        msg.setMsg("supportsConvert(DOUBLE, VARCHAR) method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsConvert is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsConvert is Failed!");

    }
  }

  /*
   * @testName: testSupportsConvert12
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(FLOAT, VARCHAR) method on that
   * object. It should return a boolean value; either true or false.
   *
   */
  public void testSupportsConvert12() throws Fault {
    try {
      // invoke on the supportsConvert
      msg.setMsg("Calling supportsConvert(FLOAT, VARCHAR) on DatabaseMetaData");
      boolean retValue = dbmd.supportsConvert(Types.FLOAT, Types.VARCHAR);
      if (retValue) {
        msg.setMsg("supportsConvert(FLOAT, VARCHAR) method is supported");
      } else {
        msg.setMsg("supportsConvert(FLOAT, VARCHAR) method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsConvert is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsConvert is Failed!");

    }
  }

  /*
   * @testName: testSupportsConvert13
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(INTEGER, VARCHAR) method on that
   * object. It should return a boolean value; either true or false.
   *
   */
  public void testSupportsConvert13() throws Fault {
    try {
      // invoke on the supportsConvert
      msg.setMsg(
          "Calling supportsConvert(INTEGER, VARCHAR) on DatabaseMetaData");
      boolean retValue = dbmd.supportsConvert(Types.INTEGER, Types.VARCHAR);
      if (retValue) {
        msg.setMsg("supportsConvert(INTEGER, VARCHAR) method is supported");
      } else {
        msg.setMsg("supportsConvert(INTEGER, VARCHAR) method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsConvert is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsConvert is Failed!");

    }
  }

  /*
   * @testName: testSupportsConvert14
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(JAVA_OBJECT, VARCHAR) method on that
   * object. It should return a boolean value; either true or false.
   *
   */
  public void testSupportsConvert14() throws Fault {
    try {
      // invoke on the supportsConvert
      msg.setMsg(
          "Calling supportsConvert(JAVA_OBJECT, VARCHAR) on DatabaseMetaData");
      boolean retValue = dbmd.supportsConvert(Types.JAVA_OBJECT, Types.VARCHAR);
      if (retValue) {
        msg.setMsg("supportsConvert(JAVA_OBJECT, VARCHAR) method is supported");
      } else {
        msg.setMsg(
            "supportsConvert(JAVA_OBJECT, VARCHAR) method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsConvert is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsConvert is Failed!");

    }
  }

  /*
   * @testName: testSupportsConvert15
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(LONGVARBINARY, VARCHAR) method on
   * that object. It should return a boolean value; either true or false.
   *
   */
  public void testSupportsConvert15() throws Fault {
    try {
      // invoke on the supportsConvert
      msg.setMsg(
          "Calling supportsConvert(LONGVARBINARY, VARCHAR) on DatabaseMetaData");
      boolean retValue = dbmd.supportsConvert(Types.LONGVARBINARY,
          Types.VARCHAR);
      if (retValue) {
        msg.setMsg(
            "supportsConvert(LONGVARBINARY, VARCHAR) method is supported");
      } else {
        msg.setMsg(
            "supportsConvert(LONGVARBINARY, VARCHAR) method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsConvert is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsConvert is Failed!");

    }
  }

  /*
   * @testName: testSupportsConvert16
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(LONGVARCHAR, VARCHAR) method on that
   * object. It should return a boolean value; either true or false.
   *
   */
  public void testSupportsConvert16() throws Fault {
    try {
      // invoke on the supportsConvert
      msg.setMsg(
          "Calling supportsConvert(LONGVARCHAR, VARCHAR) on DatabaseMetaData");
      boolean retValue = dbmd.supportsConvert(Types.LONGVARCHAR, Types.VARCHAR);
      if (retValue) {
        msg.setMsg("supportsConvert(LONGVARCHAR, VARCHAR) method is supported");
      } else {
        msg.setMsg(
            "supportsConvert(LONGVARCHAR, VARCHAR) method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsConvert is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsConvert is Failed!");

    }
  }

  /*
   * @testName: testSupportsConvert17
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(NULL, VARCHAR) on that object. It
   * should return a boolean value; either true or false.
   *
   */
  public void testSupportsConvert17() throws Fault {
    try {
      // invoke on the supportsConvert
      msg.setMsg("Calling supportsConvert(NULL, VARCHAR) on DatabaseMetaData");
      boolean retValue = dbmd.supportsConvert(Types.NULL, Types.VARCHAR);
      if (retValue) {
        msg.setMsg("supportsConvert(NULL, VARCHAR) method is supported");
      } else {
        msg.setMsg("supportsConvert(NULL, VARCHAR) method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsConvert is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsConvert is Failed!");

    }
  }

  /*
   * @testName: testSupportsConvert18
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(NUMERIC, VARCHAR) method on that
   * object It should return a boolean value; either true or false
   *
   */
  public void testSupportsConvert18() throws Fault {
    try {
      // invoke on the supportsConvert
      msg.setMsg(
          "Calling supportsConvert(NUMERIC, VARCHAR) on DatabaseMetaData");
      boolean retValue = dbmd.supportsConvert(Types.NUMERIC, Types.VARCHAR);
      if (retValue) {
        msg.setMsg("supportsConvert(NUMERIC, VARCHAR) method is supported");
      } else {
        msg.setMsg("supportsConvert(NUMERIC, VARCHAR) method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsConvert is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsConvert is Failed!");

    }
  }

  /*
   * @testName: testSupportsConvert19
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(OTHER, VARCHAR) method on that object
   * It should return a boolean value; either true or false
   *
   */
  public void testSupportsConvert19() throws Fault {
    try {
      // invoke on the supportsConvert
      msg.setMsg("Calling supportsConvert(OTHER, VARCHAR) on DatabaseMetaData");
      boolean retValue = dbmd.supportsConvert(Types.OTHER, Types.VARCHAR);
      if (retValue) {
        msg.setMsg("supportsConvert(OTHER, VARCHAR) method is supported");
      } else {
        msg.setMsg("supportsConvert(OTHER, VARCHAR) method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsConvert is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsConvert is Failed!");

    }
  }

  /*
   * @testName: testSupportsConvert20
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(REAL, VARCHAR) method on that object.
   * It should return a boolean value; either true or false.
   *
   */
  public void testSupportsConvert20() throws Fault {
    try {
      // invoke on the supportsConvert
      boolean retValue = dbmd.supportsConvert(Types.REAL, Types.VARCHAR);
      msg.setMsg("Calling supportsConvert(REAL, VARCHAR) on DatabaseMetaData");
      if (retValue) {
        msg.setMsg("supportsConvert(REAL, VARCHAR) method is supported");
      } else {
        msg.setMsg("supportsConvert(REAL, VARCHAR) method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsConvert is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsConvert is Failed!");

    }
  }

  /*
   * @testName: testSupportsConvert21
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(REF, VARCHAR) method on that object.
   * It should return a boolean value; either true or false.
   *
   */
  public void testSupportsConvert21() throws Fault {
    try {
      // invoke on the supportsConvert
      boolean retValue = dbmd.supportsConvert(Types.REF, Types.VARCHAR);
      msg.setMsg("Calling supportsConvert(REF, VARCHAR) on DatabaseMetaData");
      if (retValue) {
        msg.setMsg("supportsConvert(REF VARCHAR) method is supported");
      } else {
        msg.setMsg("supportsConvert(REF VARCHAR) method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsConvert is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsConvert is Failed!");

    }
  }

  /*
   * @testName: testSupportsConvert22
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(SMALLINT, VARCHAR) method on that
   * object. It should return a boolean value; either true or false.
   *
   */
  public void testSupportsConvert22() throws Fault {
    try {
      // invoke on the supportsConvert
      boolean retValue = dbmd.supportsConvert(Types.SMALLINT, Types.VARCHAR);
      msg.setMsg(
          "Calling supportsConvert(SMALLINT, VARCHAR) on DatabaseMetaData");
      if (retValue) {
        msg.setMsg("supportsConvert(SMALLINT VARCHAR) method is supported");
      } else {
        msg.setMsg("supportsConvert(SMALLINT VARCHAR) method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsConvert is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsConvert is Failed!");

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
