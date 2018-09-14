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
 * @(#)dbMetaClient5.java	1.27 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.dbMeta.dbMeta5;

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
 * The dbMetaClient5 class tests methods of DatabaseMetaData interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class dbMetaClient5 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.dbMeta.dbMeta5";

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
    dbMetaClient5 theTests = new dbMetaClient5();
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
   * @testName: testSupportsGroupByUnrelated
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:894; JDBC:JAVADOC:895;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsGroupByUnrelated() method on that object. It
   * should return a boolean value; either true or false.
   *
   */
  public void testSupportsGroupByUnrelated() throws Fault {
    try {
      // invoke on the supportsGroupByUnrelated
      msg.setMsg("Calling supportsGroupByUnrelated on DatabaseMetaData");
      boolean retValue = dbmd.supportsGroupByUnrelated();
      if (retValue) {
        msg.setMsg("supportsGroupByUnrelated method is supported");
      } else {
        msg.setMsg("supportsGroupByUnrelated method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsGroupByUnrelated is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsGroupByUnrelated is Failed!");

    }
  }

  /*
   * @testName: testSupportsGroupByBeyondSelect
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:896; JDBC:JAVADOC:897;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsGroupByBeyondSelect() method on that object.
   * It should return a boolean value; either true or false.
   *
   */
  public void testSupportsGroupByBeyondSelect() throws Fault {
    try {
      // invoke on the supportsGroupByBeyondSelect
      msg.setMsg("Calling supportsGroupByBeyondSelect on DatabaseMetaData");
      boolean retValue = dbmd.supportsGroupByBeyondSelect();
      if (retValue) {
        msg.setMsg("supportsGroupByBeyondSelect method is supported");
      } else {
        msg.setMsg("supportsGroupByBeyondSelect method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsGroupByBeyondSelect is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsGroupByBeyondSelect is Failed!");

    }
  }

  /*
   * @testName: testSupportsLikeEscapeClause
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:898; JDBC:JAVADOC:899;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsLikeEscapeClause() method on that object. It
   * should return a boolean value; either true or false.
   *
   */
  public void testSupportsLikeEscapeClause() throws Fault {
    try {
      // invoke on the supportsLikeEscapeClause
      msg.setMsg("Calling supportsLikeEscapeClause on DatabaseMetaData");
      boolean retValue = dbmd.supportsLikeEscapeClause();
      if (retValue) {
        msg.setMsg("supportsLikeEscapeClause method is supported");
      } else {
        msg.setMsg("supportsLikeEscapeClause method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsLikeEscapeClause is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsLikeEscapeClause is Failed!");

    }
  }

  /*
   * @testName: testSupportsMultipleResultSets
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:900; JDBC:JAVADOC:901;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsMultipleResultSets() method on that object.
   * It should return a boolean value; either true or false.
   *
   */
  public void testSupportsMultipleResultSets() throws Fault {
    try {
      // invoke on the supportsMultipleResultSets
      msg.setMsg("Calling supportsMultipleResultSets on DatabaseMetaData");
      boolean retValue = dbmd.supportsMultipleResultSets();
      if (retValue) {
        msg.setMsg("supportsMultipleResultSets method is supported");
      } else {
        msg.setMsg("supportsMultipleResultSets method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsMultipleResultSets is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsMultipleResultSets is Failed!");

    }
  }

  /*
   * @testName: testSupportsMultipleTransactions
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:902; JDBC:JAVADOC:903;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsMultipleTransactions() method on that object.
   * It should return a boolean value; either true or false.
   *
   */
  public void testSupportsMultipleTransactions() throws Fault {
    try {
      // invoke on the supportsMultipleTransactions
      msg.setMsg("Calling supportsMultipleTransactions on DatabaseMetaData");
      boolean retValue = dbmd.supportsMultipleTransactions();
      if (retValue) {
        msg.setMsg("supportsMultipleTransactions method is supported");
      } else {
        msg.setMsg("supportsMultipleTransactions method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle,
          "Call to supportsMultipleTransactions is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsMultipleTransactions is Failed!");

    }
  }

  /*
   * @testName: testSupportsNonNullableColumns
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:904; JDBC:JAVADOC:905;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsNonNullableColumns() method on that object.
   * It should return a boolean value; either true or false.
   *
   */
  public void testSupportsNonNullableColumns() throws Fault {
    try {
      // invoke on the supportsNonNullableColumns
      msg.setMsg("Calling supportsNonNullableColumns on DatabaseMetaData");
      boolean retValue = dbmd.supportsNonNullableColumns();
      if (retValue) {
        msg.setMsg("supportsNonNullableColumns method is supported");
      } else {
        msg.setMsg("supportsNonNullableColumns method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsNonNullableColumns is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsNonNullableColumns is Failed!");

    }
  }

  /*
   * @testName: testSupportsMinimumSQLGrammar
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:906; JDBC:JAVADOC:907;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsMinimumSQLGrammar() method on that object. It
   * should return a boolean value; either true or false.
   *
   */
  public void testSupportsMinimumSQLGrammar() throws Fault {
    try {
      // invoke on the supportsMinimumSQLGrammar
      msg.setMsg("Calling supportsMinimumSQLGrammar on DatabaseMetaData");
      boolean retValue = dbmd.supportsMinimumSQLGrammar();
      if (retValue) {
        msg.setMsg("supportsMinimumSQLGrammar method is supported");
      } else {
        msg.setMsg("supportsMinimumSQLGrammar method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsMinimumSQLGrammar is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsMinimumSQLGrammar is Failed!");

    }
  }

  /*
   * @testName: testSupportsCoreSQLGrammar
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:908; JDBC:JAVADOC:909;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsCoreSQLGrammar() method on that object. It
   * should return a boolean value; either true or false.
   *
   */
  public void testSupportsCoreSQLGrammar() throws Fault {
    try {
      // invoke on the supportsCoreSQLGrammar
      msg.setMsg("Calling supportsCoreSQLGrammar on DatabaseMetaData");
      boolean retValue = dbmd.supportsCoreSQLGrammar();
      if (retValue) {
        msg.setMsg("supportsCoreSQLGrammar method is supported");
      } else {
        msg.setMsg("supportsCoreSQLGrammar method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsCoreSQLGrammar is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsCoreSQLGrammar is Failed!");

    }
  }

  /*
   * @testName: testSupportsExtendedSQLGrammar
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:910; JDBC:JAVADOC:911;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsExtendedSQLGrammar() method on that object.
   * It should return a boolean value; either true or false.
   *
   */
  public void testSupportsExtendedSQLGrammar() throws Fault {
    try {
      // invoke on the supportsExtendedSQLGrammar
      msg.setMsg("Calling supportsExtendedSQLGrammar on DatabaseMetaData");
      boolean retValue = dbmd.supportsExtendedSQLGrammar();
      if (retValue) {
        msg.setMsg("supportsExtendedSQLGrammar method is supported");
      } else {
        msg.setMsg("supportsExtendedSQLGrammar method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsExtendedSQLGrammar is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsExtendedSQLGrammar is Failed!");

    }
  }

  /*
   * @testName: testSupportsANSI92EntryLevelSQL
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:912; JDBC:JAVADOC:913;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsANSI92EntryLevelSQL() method on that object.
   * It should return a boolean value; either true or false.
   *
   */
  public void testSupportsANSI92EntryLevelSQL() throws Fault {
    try {
      // invoke on the supportsANSI92EntryLevelSQL
      msg.setMsg("Calling supportsANSI92EntryLevelSQL on DatabaseMetaData");
      boolean retValue = dbmd.supportsANSI92EntryLevelSQL();
      if (retValue) {
        msg.setMsg("supportsANSI92EntryLevelSQL method is supported");
      } else {
        msg.setMsg("supportsANSI92EntryLevelSQL method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsANSI92EntryLevelSQL is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsANSI92EntryLevelSQL is Failed!");

    }
  }

  /*
   * @testName: testSupportsANSI92IntermediateSQL
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:914; JDBC:JAVADOC:915;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsANSI92IntermediateSQL() method on that
   * object. It should return a boolean value; either true or false.
   *
   */
  public void testSupportsANSI92IntermediateSQL() throws Fault {
    try {
      // invoke on the supportsANSI92IntermediateSQL
      msg.setMsg("Calling supportsANSI92IntermediateSQL on DatabaseMetaData");
      boolean retValue = dbmd.supportsANSI92IntermediateSQL();
      if (retValue) {
        msg.setMsg("supportsANSI92IntermediateSQL method is supported");
      } else {
        msg.setMsg("supportsANSI92IntermediateSQL method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle,
          "Call to supportsANSI92IntermediateSQL is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsANSI92IntermediateSQL is Failed!");

    }
  }

  /*
   * @testName: testSupportsANSI92FullSQL
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:916; JDBC:JAVADOC:917;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsANSI92FullSQL() method on that object. It
   * should return a boolean value; either true or false.
   *
   */
  public void testSupportsANSI92FullSQL() throws Fault {
    try {
      // invoke on the supportsANSI92FullSQL
      boolean retValue = dbmd.supportsANSI92FullSQL();
      msg.setMsg("Calling supportsANSI92FullSQL on DatabaseMetaData");
      if (retValue) {
        msg.setMsg("supportsANSI92FullSQL method is supported");
      } else {
        msg.setMsg("supportsANSI92FullSQL method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsANSI92FullSQL is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsANSI92FullSQL is Failed!");

    }
  }

  /*
   * @testName: testSupportsIntegrityEnhancementFacility
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:918; JDBC:JAVADOC:919;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsIntegrityEnhancementFacility() method onn
   * that object. It should return a boolean value; either true or false.
   *
   */
  public void testSupportsIntegrityEnhancementFacility() throws Fault {
    try {
      // invoke on the supportsIntegrityEnhancementFacility
      msg.setMsg(
          "Calling supportsIntegrityEnhancementFacility on DatabaseMetaData");
      boolean retValue = dbmd.supportsIntegrityEnhancementFacility();
      if (retValue) {
        msg.setMsg("supportsIntegrityEnhancementFacility method is supported");
      } else {
        msg.setMsg(
            "supportsIntegrityEnhancementFacility method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle,
          "Call to supportsIntegrityEnhancementFacility is Failed!");

    } catch (Exception e) {
      msg.printError(e,
          "Call to supportsIntegrityEnhancementFacility is Failed!");

    }
  }

  /*
   * @testName: testSupportsOuterJoins
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:920; JDBC:JAVADOC:921;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsOuterJoins() method on that object. It should
   * return a boolean value; either true or false.
   *
   */
  public void testSupportsOuterJoins() throws Fault {
    try {
      // invoke on the supportsOuterJoins
      msg.setMsg("Calling supportsOuterJoins on DatabaseMetaData");
      boolean retValue = dbmd.supportsOuterJoins();
      if (retValue) {
        msg.setMsg("supportsOuterJoins method is supported");
      } else {
        msg.setMsg("supportsOuterJoins method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsOuterJoins is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsOuterJoins is Failed!");

    }
  }

  /*
   * @testName: testSupportsFullOuterJoins
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:922; JDBC:JAVADOC:923;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsFullOuterJoins() method on that object. It
   * should return a boolean value; either true or false.
   *
   */
  public void testSupportsFullOuterJoins() throws Fault {
    try {
      // invoke on the supportsFullOuterJoins
      msg.setMsg("Calling supportsFullOuterJoins on DatabaseMetaData");
      boolean retValue = dbmd.supportsFullOuterJoins();
      if (retValue) {
        msg.setMsg("supportsFullOuterJoins method is supported");
        if (!dbmd.supportsLimitedOuterJoins()) {

          // Must be true if supportsFullOuterJoins()
          // is true;

          throw new Fault("supportsLimitedOuterJoins() must "
              + "be true if supportsFullOuterJoins() " + "is true!");
        }
      } else {
        msg.setMsg("supportsFullOuterJoins method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsFullOuterJoins is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsFullOuterJoins is Failed!");

    }
  }

  /*
   * @testName: testSupportsLimitedOuterJoins
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:924; JDBC:JAVADOC:925;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsLimitedOuterJoins() method on that object. It
   * should return a boolean value; either true or false.
   *
   */
  public void testSupportsLimitedOuterJoins() throws Fault {
    try {
      // invoke on the supportsLimitedOuterJoins
      msg.setMsg("Calling supportsLimitedOuterJoins on DatabaseMetaData");
      boolean retValue = dbmd.supportsLimitedOuterJoins();
      if (retValue) {
        msg.setMsg("supportsLimitedOuterJoins method is supported");
      } else {
        msg.setMsg("supportsLimitedOuterJoins method is not supported");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to supportsLimitedOuterJoins is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to supportsLimitedOuterJoins is Failed!");

    }
  }

  /*
   * @testName: testGetSchemaTerm
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:926; JDBC:JAVADOC:927;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the getSchemaTerm() method on that object. It should
   * return a String and NULL if it cannot be generated.
   *
   */
  public void testGetSchemaTerm() throws Fault {
    try {
      // invoke on the getSchemaTerm
      msg.setMsg("Calling getSchemaTerm on DatabaseMetaData");
      String sRetValue = dbmd.getSchemaTerm();
      if (sRetValue == null) {
        msg.setMsg(
            "getSchemaTerm method does not returns the vendor's preferred term for schema ");
      } else {
        msg.setMsg("getSchemaTerm method returns:  " + sRetValue);
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getSchemaTerm is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getSchemaTerm is Failed!");

    }
  }

  /*
   * @testName: testGetProcedureTerm
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:928; JDBC:JAVADOC:929;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the getProcedureTerm() method on that object. It should
   * return a String and NULL if it cannot be generated.;
   *
   */
  public void testGetProcedureTerm() throws Fault {
    try {
      // invoke on the getProcedureTerm
      msg.setMsg("Calling getProcedureTerm on DatabaseMetaData");
      String sRetValue = dbmd.getProcedureTerm();
      if (sRetValue == null) {
        msg.setMsg(
            "getProcedureTerm method does not returns the vendor's preferred term for procedure ");
      } else {
        msg.setMsg("getProcedureTerm method returns:  " + sRetValue);
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getProcedureTerm is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getProcedureTerm is Failed!");

    }
  }

  /*
   * @testName: testGetCatalogTerm
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:930; JDBC:JAVADOC:931;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the getCatalogTerm() method on that object. It should
   * return a String and NULL if it cannot be returned.
   *
   */
  public void testGetCatalogTerm() throws Fault {
    try {
      // invoke on the getCatalogTerm
      msg.setMsg("Calling getCatalogTerm on DatabaseMetaData");
      String sRetValue = dbmd.getCatalogTerm();
      if (sRetValue == null) {
        msg.setMsg(
            "getCatalogTerm method does not returns the vendor's preferred term for catalog ");
      } else {
        msg.setMsg("getCatalogTerm method returns:  " + sRetValue);
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getCatalogTerm is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getCatalogTerm is Failed!");

    }
  }

  /*
   * @testName: testIsCatalogAtStart
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:932; JDBC:JAVADOC:933;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the isCatalogAtStart() method on that object. It should
   * return a boolean value; either true or false.
   *
   */
  public void testIsCatalogAtStart() throws Fault {
    try {
      // invoke on the isCatalogAtStart
      msg.setMsg("Calling isCatalogAtStart on DatabaseMetaData");
      boolean retValue = dbmd.isCatalogAtStart();
      if (retValue) {
        msg.setMsg(
            "isCatalogAtStart metohd returns catalog appear at the start");
      } else {
        msg.setMsg("isCatalogAtStart metohd returns catalog appear at the end");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to isCatalogAtStart is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to isCatalogAtStart is Failed!");

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
