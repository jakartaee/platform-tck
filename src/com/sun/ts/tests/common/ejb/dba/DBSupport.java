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
 * $Id$
 */

package com.sun.ts.tests.common.ejb.dba;

import java.util.Properties;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;

/**
 * Provide basic DB support for TS deployment tests, using The Datasource
 * referenced as 'jdbc/DB1' in the component environment.
 *
 * This class is not intended to be used "as is", but rather to be sub-classed
 * by other DB support classes focusing on a particular DB schema (and in
 * particular on a type of primary key).
 */
public class DBSupport implements java.io.Serializable {

  /** Prefix used for JNDI lookups */
  protected static final String prefix = "java:comp/env/";

  /** JNDI name used to lookup the DataSource */
  protected static final String DBLookupName = prefix + "jdbc/DB1";

  /** DB table prefix. Used to get the appropriate SQL properties */
  protected String tablePrefix = null;

  protected DataSource ds = null;

  protected transient Connection dbConnection = null;

  protected TSNamingContext nctx = null;

  /*
   * Cached data
   */
  protected int cofID = 0; // Coffee ID (Primary Key)

  protected String cofName = null; // Coffee Name

  protected float cofPrice = 0; // Coffee Price

  /**
   * Create a new DBSupport object. If called from an EJB or a Web component,
   * you must make sure to call TestUtil.init() before creating a new DBSupport
   * object (so that you can safely use TestUtil.getProperty).
   *
   * @param tablePrefix
   *          Prefix to use for SQL properties lookups.
   */
  public DBSupport(String tablePrefix) throws Exception {

    this.tablePrefix = tablePrefix;
    TestUtil.logTrace("[DBSupport] Getting naming context...");
    this.nctx = new TSNamingContext();

    TestUtil.logTrace("[DBSupport] Lookup DataSource " + DBLookupName);
    ds = (DataSource) nctx.lookup(DBLookupName);
  }

  /**
   * Initialize DB table (remove all existing rows).
   * 
   * Method is static so that it can be easily called from the Application
   * Client setup method.
   */
  public static void initTable(String tablePrefix, Properties props)
      throws Exception {

    String cleanupPropName = "DEPLOY_" + tablePrefix + "_Cleanup";
    DataSource ds = null;
    Connection conn = null;
    Statement stmt = null;
    TSNamingContext nctx;
    String sqlStr;

    TestUtil.logTrace("[DBSupport] initTable()");
    try {
      if (null == tablePrefix) { /* Sanity check */
        throw new Exception("tablePrefix cannot be null!");
      }

      TestUtil.logTrace("[DBSupport] Getting naming context...");
      nctx = new TSNamingContext();

      TestUtil.logTrace("[DBSupport] Lookup DataSource " + DBLookupName);
      ds = (DataSource) nctx.lookup(DBLookupName);

      TestUtil.logTrace("[DBSupport] Getting DB connection...");
      conn = ds.getConnection();

      TestUtil.logTrace("[DBSupport] Cleanup table");
      stmt = conn.createStatement();
      TestUtil.logTrace("[DBSupport] Use SQL prop " + cleanupPropName);
      sqlStr = TestUtil.getProperty(cleanupPropName);
      TestUtil.logTrace("[DBSupport] SQL = '" + sqlStr + "'");
      stmt.executeUpdate(sqlStr);
      TestUtil.logTrace("[DBSupport] Table cleaned up!");

    } catch (SQLException e) {
      TestUtil.logErr("[DBSupport] Cannot init table :" + e);
      throw new SQLException("SQL Exception in initTable: " + e);
    } finally {
      try {
        if (null != stmt) {
          stmt.close();
        }
        if (null != conn) {
          conn.close();
        }
      } catch (SQLException e) {
        TestUtil.logTrace("[DBSupport] Ignoring Exception (cleanup): " + e);
      }
    }
  }

  /** Make sure we have a valid DB connection handy */
  public void getDBConnection() throws SQLException {
    TestUtil.logTrace("[DBSupport] getDBConnection()");
    if (null == dbConnection) {
      dbConnection = ds.getConnection();
    }
  }

  /** Close current DB connection, if applicable */
  public void closeDBConnection() throws SQLException {
    TestUtil.logTrace("[DBSupport] closeDBConnection()");
    if (null != dbConnection) {
      dbConnection.close();
      dbConnection = null; /* Detect later we closed it */
    }
  }

  /**
   * Generic method to get a SQL statement for current table.
   *
   * We get the SQL code associated with the DEPLOY_<tablePrefix>_<suffix> TS
   * property.
   */
  public PreparedStatement getStmt(String suffix) throws SQLException {
    PreparedStatement pStmt;
    String sqlPropName;
    String sqlStr;

    TestUtil.logTrace("getStmt()");
    getDBConnection();
    TestUtil.logMsg("connection = " + dbConnection);
    sqlPropName = "DEPLOY_" + tablePrefix + "_" + suffix;
    TestUtil.logTrace("[DBSupport] Get SQL for " + sqlPropName);
    sqlStr = TestUtil.getProperty(sqlPropName);
    TestUtil.logMsg("[DBSupport] SQL = " + sqlStr);
    TestUtil.logMsg("[DBSupport] getStatement: " + dbConnection);
    pStmt = dbConnection.prepareStatement(sqlStr);

    return pStmt;
  }

  /**
   * Close the ResultSet and the PreparedStatement in a safely manner and
   * ignoring any SQLException that could be thrown. This method is designed to
   * be called from a finally block to ensure the release of associated
   * resources.
   */
  public void closeStmt(PreparedStatement pStmt, ResultSet result) {
    try {
      if (null != result) {
        result.close();
      }
    } catch (SQLException e) {
      TestUtil.logTrace(
          "[DBSupport] Ignoring Exception while " + "closing ResultSet: " + e);
    }

    try {
      if (null != pStmt) {
        pStmt.close();
      }
    } catch (SQLException e) {
      TestUtil.logTrace("[DBSupport] Ignoring Exception while "
          + "closing PreparedStatement: " + e);
    }
  }

}
