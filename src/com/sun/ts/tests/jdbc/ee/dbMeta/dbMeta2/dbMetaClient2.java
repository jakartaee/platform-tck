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
 * @(#)dbMetaClient2.java	1.26 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.dbMeta.dbMeta2;

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
 * The dbMetaClient2 class tests methods of DatabaseMetaData interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class dbMetaClient2 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.dbMeta.dbMeta2";

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
    dbMetaClient2 theTests = new dbMetaClient2();
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
   * @testName: testStoresMixedCaseIdentifiers
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:846; JDBC:JAVADOC:847;
   * JavaEE:SPEC:193;
   * 
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the storesMixedCaseIdentifiers() method It should return
   * a boolean value
   *
   */
  public void testStoresMixedCaseIdentifiers() throws Fault {
    try {
      // invoke on the storesMixedCaseIdentifiers
      msg.setMsg("Calling storesMixedCaseIdentifiers on DatabaseMetaData");
      boolean retValue = dbmd.storesMixedCaseIdentifiers();
      if (retValue) {
        msg.setMsg(
            "storesMixedCaseIdentifiers method returns unquoted SQL identifiers stored as mixed case");
      } else {
        msg.setMsg(
            "storesMixedCaseIdentifiers method returns unquoted SQL identifiers not stored as mixed case");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to storesMixedCaseIdentifiers is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to storesMixedCaseIdentifiers is Failed!");

    }
  }

  /*
   * @testName: testSupportsMixedCaseQuotedIdentifiers
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:848; JDBC:JAVADOC:849;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsMixedCaseQuotedIdentifiers() method It should
   * return a boolean value
   *
   */
  public void testSupportsMixedCaseQuotedIdentifiers() throws Fault {
    try {
      // invoke on the supportsMixedCaseQuotedIdentifiers
      msg.setMsg(
          "Calling supportsMixedCaseQuotedIdentifiers on DatabaseMetaData");
      boolean retValue = dbmd.supportsMixedCaseQuotedIdentifiers();
      if (retValue) {
        msg.setMsg("supportsMixedCaseQuotedIdentifiers method is supported");
      } else {
        msg.setMsg(
            "supportsMixedCaseQuotedIdentifiers method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle,
          "Call to supportsMixedCaseQuotedIdentifiers is Failed!");

    } catch (Exception e) {
      msg.printError(e,
          "Call to supportsMixedCaseQuotedIdentifiers is Failed!");

    }
  }

  /*
   * @testName: testStoresUpperCaseQuotedIdentifiers
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:850; JDBC:JAVADOC:851;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the storesUpperCaseQuotedIdentifiers() method It should
   * return a boolean value
   *
   */
  public void testStoresUpperCaseQuotedIdentifiers() throws Fault {
    try {
      // invoke on the storesUpperCaseQuotedIdentifiers
      msg.setMsg(
          "Calling storesUpperCaseQuotedIdentifiers on DatabaseMetaData");
      boolean retValue = dbmd.storesUpperCaseQuotedIdentifiers();
      if (retValue) {
        msg.setMsg(
            "storesUpperCaseQuotedIdentifiers method returns SQL identifiers stored as upper case");
      } else {
        msg.setMsg(
            "storesUpperCaseQuotedIdentifiers method returns SQL identifiers not stored as upper case");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle,
          "Call to storesUpperCaseQuotedIdentifiers is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to storesUpperCaseQuotedIdentifiers is Failed!");

    }
  }

  /*
   * @testName: testStoresLowerCaseQuotedIdentifiers
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:852; JDBC:JAVADOC:853;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the storesLowerCaseQuotedIdentifiers() method It should
   * return a boolean value
   *
   */
  public void testStoresLowerCaseQuotedIdentifiers() throws Fault {
    try {
      // invoke on the storesLowerCaseQuotedIdentifiers
      msg.setMsg(
          "Calling storesLowerCaseQuotedIdentifiers on DatabaseMetaData");
      boolean retValue = dbmd.storesLowerCaseQuotedIdentifiers();
      if (retValue) {
        msg.setMsg(
            "storesLowerCaseQuotedIdentifiers method returns SQL identifiers stored as lower case");
      } else {
        msg.setMsg(
            "storesLowerCaseQuotedIdentifiers method returns SQL identifiers not stored as lower case");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle,
          "Call to storesLowerCaseQuotedIdentifiers is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to storesLowerCaseQuotedIdentifiers is Failed!");

    }
  }

  /*
   * @testName: testStoresMixedCaseQuotedIdentifiers
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:854; JDBC:JAVADOC:855;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the storesMixedCaseQuotedIdentifiers() method It should
   * return a boolean value
   *
   */
  public void testStoresMixedCaseQuotedIdentifiers() throws Fault {
    try {
      // invoke on the storesMixedCaseQuotedIdentifiers
      msg.setMsg(
          "Calling storesMixedCaseQuotedIdentifiers on DatabaseMetaData");
      boolean retValue = dbmd.storesMixedCaseQuotedIdentifiers();
      if (retValue) {
        msg.setMsg(
            "storesMixedCaseQuotedIdentifiers method returns SQL identifiers stored as mixed case");
      } else {
        msg.setMsg(
            "storesMixedCaseQuotedIdentifiers method returns SQL identifiers not stored as mixed case");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle,
          "Call to storesMixedCaseQuotedIdentifiers is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to storesMixedCaseQuotedIdentifiers is Failed!");

    }
  }

  /*
   * @testName: testGetIdentifierQuoteString
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:856; JDBC:JAVADOC:857;
   * JavaEE:SPEC:193;
   * 
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the getIdentifierQuoteString() method It should return a
   * String
   *
   */
  public void testGetIdentifierQuoteString() throws Fault {
    try {
      // invoke on the getIdentifierQuoteString
      msg.setMsg("Calling getIdentifierQuoteString on DatabaseMetaData");
      String sRetValue = dbmd.getIdentifierQuoteString();

      if (sRetValue.equals("")) {
        msg.setMsg("The database does not support quoting identifiers");
      } else if (sRetValue instanceof String) {
        msg.setMsg("getIdentifierQuoteString method returns " + sRetValue);
      } else if (sRetValue == null) {
        msg.printTestError("getIdentifierQuoteString returns an Invalid value",
            "Call to getIdentfierQuoteString is Failed!");

      }
      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getIdentifierQuoteString is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getIdentifierQuoteString is Failed!");

    }
  }

  /*
   * @testName: testGetSQLKeywords
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:858; JDBC:JAVADOC:859;
   * JavaEE:SPEC:193;
   * 
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the getSQLKeywords() method It should return a String
   *
   */
  public void testGetSQLKeywords() throws Fault {
    try {
      // invoke on the getSQLKeywords
      msg.setMsg("Calling getSQLKeywords on DatabaseMetaData");
      String sRetValue = dbmd.getSQLKeywords();

      if (sRetValue == null) {
        msg.setMsg(
            "getSQLKeywords method does not returns the list of SQLKeywords ");
      } else {
        msg.setMsg("getSQLKeywords method returns: " + sRetValue);
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getSQLKeywords is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getSQLKeywords is Failed!");

    }
  }

  /*
   * @testName: testGetNumericFunctions
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:860; JDBC:JAVADOC:861;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the getNumericFunctions() method It should return a
   * String
   *
   */
  public void testGetNumericFunctions() throws Fault {
    try {
      // invoke on the getNumericFunctions
      msg.setMsg("Calling getNumericFunctions on DatabaseMetaData");
      String sRetValue = dbmd.getNumericFunctions();
      if (sRetValue == null) {
        msg.setMsg(
            "getNumericFunctions method does not returns the comma-separated list of math functions ");
      } else {
        msg.setMsg("getNumericFunctions method returns: " + sRetValue);
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getNumericFunctions is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getNumericFunctions is Failed!");

    }
  }

  /*
   * @testName: testGetStringFunctions
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:862; JDBC:JAVADOC:863;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the getStringFunctions() method It should return a String
   *
   */
  public void testGetStringFunctions() throws Fault {
    try {
      // invoke on the getStringFunctions
      msg.setMsg("Calling getStringFunctions on DatabaseMetaData");
      String sRetValue = dbmd.getStringFunctions();
      if (sRetValue == null) {
        msg.setMsg(
            "getStringFunctions method does not returns the comma-separated list of string functions ");
      } else {
        msg.setMsg("getStringFunctions method returns: " + sRetValue);
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getStringFunctions is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getStringFunctions is Failed!");

    }
  }

  /*
   * @testName: testGetSystemFunctions
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:864; JDBC:JAVADOC:865;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the getSystemFunctions() method It should return a String
   *
   */
  public void testGetSystemFunctions() throws Fault {
    try {
      // invoke on the getSystemFunctions
      msg.setMsg("Calling getSystemFunctions on DatabaseMetaData");
      String sRetValue = dbmd.getSystemFunctions();
      if (sRetValue == null) {
        msg.setMsg(
            "getSystemFunctions methd does not returns the comma-separated list of system functions ");
      } else {
        msg.setMsg("getSystemFunctions method returns: " + sRetValue);
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getSystemFunctions is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getSystemFunctions is Failed!");

    }
  }

  /*
   * @testName: testGetTimeDateFunctions
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:866; JDBC:JAVADOC:867;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the getTimeDateFunctions() method It should return a
   * String
   *
   */
  public void testGetTimeDateFunctions() throws Fault {
    try {
      // invoke on the getTimeDateFunctions
      msg.setMsg("Calling getTimeDateFunctions on DatabaseMetaData");
      String sRetValue = dbmd.getTimeDateFunctions();
      if (sRetValue == null) {
        msg.setMsg(
            "getTimeDateFunctions method does not returns the comma-separated list of time and date functions ");
      } else {
        msg.setMsg("getTimeDateFunctions method returns: " + sRetValue);
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getTimeDateFunctions is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getTimeDateFunctions is Failed!");

    }
  }

  /*
   * @testName: testGetSearchStringEscape
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:868; JDBC:JAVADOC:869;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the getSearchStringEscape() method It should return a
   * String
   *
   */
  public void testGetSearchStringEscape() throws Fault {
    try {
      // invoke on the getSearchStringEscape
      msg.setMsg("Calling getSearchStringEscape on DatabaseMetaData");
      String sRetValue = dbmd.getSearchStringEscape();
      if (sRetValue == null) {
        msg.setMsg(
            "getSearchStringEscape  method does not returns the string used to escape wildcard characters ");
      } else {
        msg.setMsg("getSearchStringEscape method returns: " + sRetValue);
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getTimeDateFunctions is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getTimeDateFunctions is Failed!");

    }
  }

  /*
   * @testName: testGetExtraNameCharacters
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:870; JDBC:JAVADOC:871;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the getExtraNameCharacters() method It should return a
   * String
   *
   */
  public void testGetExtraNameCharacters() throws Fault {
    try {
      // invoke on the getExtraNameCharacters
      msg.setMsg("Calling getExtraNameCharacters on DatabaseMetaData");
      String sRetValue = dbmd.getExtraNameCharacters();
      if (sRetValue == null) {
        msg.setMsg(
            "getExtraNameCharacters method does not returns the string containing the extra characters ");
      } else {
        msg.setMsg("getExtraNameCharacters method returns: " + sRetValue);
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getExtraNameCharacters is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getExtraNameCharacters is Failed!");

    }
  }

  /*
   * @testName: testSupportsAlterTableWithAddColumn
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:872; JDBC:JAVADOC:873;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsAlterTableWithAddColumn() method It should
   * return a boolean value
   *
   */
  public void testSupportsAlterTableWithAddColumn() throws Fault {
    try {
      // invoke on the supportsAlterTableWithAddColumn
      msg.setMsg("Calling supportsAlterTableWithAddColumn on DatabaseMetaData");
      boolean retValue = dbmd.supportsAlterTableWithAddColumn();
      if (retValue) {
        msg.setMsg("supportsAlterTableWithAddColumn is supported");
      } else {
        msg.setMsg("supportsAlterTableWithAddColumn is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle,
          "Call to supportsAlterTableWithAddColumn is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsAlterTableWithAddColumn is Failed!");

    }
  }

  /*
   * @testName: testSupportsAlterTableWithDropColumn
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:874; JDBC:JAVADOC:875;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsAlterTableWithDropColumn() method It should
   * return a boolean value
   *
   */
  public void testSupportsAlterTableWithDropColumn() throws Fault {
    try {
      // invoke on the supportsAlterTableWithDropColumn
      msg.setMsg(
          "Calling supportsAlterTableWithDropColumn on DatabaseMetaData");
      boolean retValue = dbmd.supportsAlterTableWithDropColumn();
      if (retValue) {
        msg.setMsg("supportsAlterTableWithDropColumn is supported");
      } else {
        msg.setMsg("supportsAlterTableWithDropColumn is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle,
          "Call to supportsAlterTableWithDropColumn is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsAlterTableWithDropColumn is Failed!");

    }
  }

  /*
   * @testName: testSupportsColumnAliasing
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:876; JDBC:JAVADOC:877;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsColumnAliasing() method It should return a
   * true value
   *
   */
  public void testSupportsColumnAliasing() throws Fault {
    try {
      // invoke on the supportsColumnAliasing
      msg.setMsg("Calling supportsColumnAliasing on DatabaseMetaData");
      boolean retValue = dbmd.supportsColumnAliasing();
      logTrace("A JDBC CompliantTM driver always returns true");
      if (retValue) {
        msg.setMsg("supportsColumnAliasing is supported");
      } else {
        msg.printTestError("supportsColumnAliasing is not supported",
            "supportsColumnAliasing should always return true!");

      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsColumnAliasing is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsColumnAliasing is Failed!");

    }
  }

  /*
   * @testName: testNullPlusNonNullIsNull
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:878; JDBC:JAVADOC:879;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the nullPlusNonNullIsNull() method It should return a
   * boolean value
   *
   */
  public void testNullPlusNonNullIsNull() throws Fault {
    try {
      // invoke on the nullPlusNonNullIsNull
      msg.setMsg("Calling nullPlusNonNullIsNull on DatabaseMetaData");
      boolean retValue = dbmd.nullPlusNonNullIsNull();
      logTrace("A JDBC CompliantTM driver always returns true");
      if (retValue) {
        msg.setMsg(
            "nullPlusNonNullIsNull method returns a NULL value for the concatenations between NULL and non-NULL");
      } else {
        msg.printTestError(
            "nullPlusNonNullIsNull method does not returns a NULL value for the concatenations between NULL and non-NULL",
            "nullPlusNonNullIsNull method should always return true!");

      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to nullPlusNonNullIsNull is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to nullPlusNonNullIsNull is Failed!");

    }
  }

  /*
   * @testName: testSupportsConvert
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:880; JDBC:JAVADOC:881;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert() method It should return a boolean
   * value
   *
   */
  public void testSupportsConvert() throws Fault {
    try {
      // invoke on the supportsConvert
      msg.setMsg("Calling supportsConvert on DatabaseMetaData");
      boolean retValue = dbmd.supportsConvert();
      if (retValue) {
        msg.setMsg("supportsConvert method is supported");
      } else {
        msg.setMsg("supportsConvert method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsConvert is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsConvert is Failed!");

    }
  }

  /*
   * @testName: testSupportsConvert01
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(ARRAY, VARCHAR) method It should
   * return a boolean value
   *
   */
  public void testSupportsConvert01() throws Fault {
    try {
      // invoke on the supportsConvert
      msg.setMsg("Calling supportsConvert(ARRAY, VARCHAR) on DatabaseMetaData");
      boolean retValue = dbmd.supportsConvert(Types.ARRAY, Types.VARCHAR);
      if (retValue) {
        msg.setMsg("supportsConvert(ARRAY, VARCHAR) method is supported");
      } else {
        msg.setMsg("supportsConvert(ARRAY, VARCHAR) method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsConvert is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsConvert is Failed!");

    }
  }

  /*
   * @testName: testSupportsConvert02
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(BIGINT, VARCHAR) method It should
   * return a boolean value
   *
   */
  public void testSupportsConvert02() throws Fault {
    try {
      // invoke on the supportsConvert
      msg.setMsg(
          "Calling supportsConvert(BIGINT, VARCHAR) on DatabaseMetaData");
      boolean retValue = dbmd.supportsConvert(Types.BIGINT, Types.VARCHAR);
      if (retValue) {
        msg.setMsg("supportsConvert(BIGINT, VARCHAR) method is supported");
      } else {
        msg.setMsg("supportsConvert(BIGINT, VARCHAR) method is not supported");
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
