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

package com.sun.ts.tests.common.dao;

import java.util.Properties;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;

/**
 * Provide basic functionalities for Data Access Objects (DAOs)
 *
 * This class is not intended to be used "as is", but rather to be sub-classed
 * by other DAO classes specialized for on a particular DB schema.
 */
public class DataSourceDAO implements DAO {

  /** Default JNDI name used to lookup the DataSource */
  protected static final String DEFAULT_DB_LOOKUP = "jdbc/DB1";

  /** Suffix of the SQL property used to clear a table */
  protected static final String SQL_DELETE_ALL = "delete_all";

  /** DB table prefix. Used to get the appropriate SQL properties */
  protected String sqlTablePrefix = null;

  /** Provider we use to get DB connections */
  protected ConnectionProvider provider = null;

  /** Strict policy to catch connection leaks */
  protected boolean strictPolicy = true;

  /** Internal cache for DB connections */
  private transient Connection dbConnection = null;

  /** Internal flag to track session state */
  private boolean activeSession;

  /**
   * Uses a java.sql.DataSource as a connection provider. Supports container
   * managed and bean managed authorization semantics.
   */
  protected static class ConnectionProvider {

    /** Enum values for authorization type */
    public static final int AUTH_CONTAINER = 1;

    public static final int AUTH_BEAN_MANAGED = 2;

    /** Prefix used for JNDI lookups */
    protected static final String ENV_PREFIX = "java:comp/env/";

    /** JNDI name used to lookup the DataSource */
    protected String dbLookupName;

    /** Authorization type to get connections */
    protected int authorizationType;

    /** User name used for bean managed authentication */
    protected String user;

    /** Password used for bean managed authentication */
    protected String password;

    /** Datasource used to get the DB connections */
    protected DataSource ds = null;

    protected ConnectionProvider() throws DAOException {
      this(DEFAULT_DB_LOOKUP, AUTH_CONTAINER, null, null);
    }

    protected ConnectionProvider(String user, String password)
        throws DAOException {

      this(DEFAULT_DB_LOOKUP, AUTH_BEAN_MANAGED, user, password);
    }

    protected ConnectionProvider(String dbLookupName) throws DAOException {
      this(dbLookupName, AUTH_CONTAINER, null, null);
    }

    protected ConnectionProvider(String dbLookupName, String user,
        String password) throws DAOException {

      this(dbLookupName, AUTH_BEAN_MANAGED, user, password);
    }

    private ConnectionProvider(String dbLookupName, int authorizationType,
        String user, String password) throws DAOException {

      TSNamingContext nCtx;

      if (null == dbLookupName || dbLookupName.equals("")) {
        throw new IllegalArgumentException(
            "Invalid dbLookupName" + dbLookupName);
      }

      if (AUTH_BEAN_MANAGED == authorizationType
          && (null == user || null == password)) {
        throw new IllegalArgumentException(
            "Invalid user/password" + user + "/" + password);

      }

      if (!(AUTH_CONTAINER == authorizationType
          || AUTH_BEAN_MANAGED == authorizationType)) {
        throw new IllegalArgumentException(
            "Invalid authization type " + authorizationType);

      }

      try {
        this.dbLookupName = dbLookupName;
        this.authorizationType = authorizationType;
        this.user = user;
        this.password = password;

        TestUtil.logTrace(
            "[ConnectionProvider] Looking up datasource " + dbLookupName);
        nCtx = new TSNamingContext();
        ds = (DataSource) nCtx.lookup(ENV_PREFIX + dbLookupName);
      } catch (Exception e) {
        /* Unfortunately TSNamingContext throws java.lang.Exception */
        throw new DAOException("Naming exception", e);
      }

    }

    public Connection getDBConnection() throws SQLException {
      Connection conn;

      TestUtil.logTrace("[DataSourceDAO] getDBConnection()");
      switch (authorizationType) {
      case AUTH_CONTAINER:
        TestUtil.logTrace("AuthorizationType:AUTH_CONTAINER");
        conn = ds.getConnection();
        break;
      case AUTH_BEAN_MANAGED:
        TestUtil.logTrace("AuthorizationType:AUTH_BEAN_MANAGED");
        conn = ds.getConnection(user, password);
        break;
      default:
        throw new IllegalStateException(
            "Illegal authorization type :" + authorizationType);
      }

      return conn;
    }

  }

  /**
   * Create a new DatasourceDAO object using container managed authorization.
   *
   * This constructor use the DataSource referenced as "java:comp/env/jdbc/DB1"
   * (in the component naming environment) to get DB connections.
   *
   * @param sqlTablePrefix
   *          Prefix to use for SQL properties lookups.
   */
  public DataSourceDAO(String sqlTablePrefix) throws DAOException {
    this(sqlTablePrefix, new ConnectionProvider());
  }

  /**
   * Create a new DatasourceDAO object using bean managed authorization.
   *
   * This constructor use the DataSource referenced as "java:comp/env/jdbc/DB1"
   * (in the component naming environment) to get DB connections.
   *
   * @param sqlTablePrefix
   *          Prefix to use for SQL properties lookups.
   *
   * @param user
   *          User name used for bean managed connection authorization.
   *
   * @param password
   *          Password used for bean managed connection authorization.
   */
  public DataSourceDAO(String sqlTablePrefix, String user, String password)
      throws DAOException {

    this(sqlTablePrefix, new ConnectionProvider(user, password));
  }

  /**
   * Create a new DatasourceDAO object using container managed authorization.
   *
   * This constructor use the DataSource referenced as
   * "java:comp/env/<dbLookupName>" (in the component naming environment) to get
   * DB connections.
   *
   * @param dbLookupName
   *          Name used to lookup the DataSource in the component environment.
   *
   * @param sqlTablePrefix
   *          Prefix to use for SQL properties lookups.
   */
  public DataSourceDAO(String dbLookupName, String sqlTablePrefix)
      throws DAOException {

    this(sqlTablePrefix, new ConnectionProvider(dbLookupName));
  }

  /**
   * Create a new DatasourceDAO object using bean managed authorization.
   *
   * This constructor use the DataSource referenced as
   * "java:comp/env/<dbLookupName>" (in the component naming environment) to get
   * DB connections.
   *
   * @param dbLookupName
   *          Name used to lookup the DataSource in the component environment.
   *
   * @param sqlTablePrefix
   *          Prefix to use for SQL properties lookups.
   *
   * @param user
   *          User name used for bean managed connection authorization.
   *
   * @param password
   *          Password used for bean managed connection authorization.
   */
  public DataSourceDAO(String dbLookupName, String sqlTablePrefix, String user,
      String password) throws DAOException {

    this(sqlTablePrefix, new ConnectionProvider(dbLookupName, user, password));
  }

  /**
   * Create a new DataSourceDAO object. If called from an EJB or a Web
   * component, you must make sure to call TestUtil.init() before creating a new
   * DataSourceDAO object (so that you can safely use TestUtil.getProperty).
   *
   * @param dbLookupName
   *          This class will use the Datasource referenced as
   *          "java:comp/env/<dbLookupName>" in the component environment to
   *          connect to the DB.
   *
   * @param sqlTablePrefix
   *          Prefix to use for SQL properties lookups.
   */
  protected DataSourceDAO(String sqlTablePrefix, ConnectionProvider provider)
      throws DAOException {

    if (null == sqlTablePrefix || sqlTablePrefix.equals("")) {
      throw new IllegalArgumentException(
          "Invalid sqlTablePrefix" + sqlTablePrefix);
    }
    if (null == provider) {
      throw new IllegalArgumentException("Null provider");
    }

    this.activeSession = false;
    this.provider = provider;
    this.sqlTablePrefix = sqlTablePrefix;
  }

  /**
   * Initialize external resources. Must be called before calling calling any
   * other non static method on this object.
   */
  public void startSession() throws DAOException {
    if (activeSession || (null != dbConnection)) {
      /*
       * Pending connection. This should *NOT* happen. Client code did not stop
       * the session :o(.
       */
      if (strictPolicy) {
        /* To ease development we inform client code there is a bug */
        throw new IllegalStateException("CONNECTION LEAK: "
            + "Nested session detected (startSession() for DAO " + this);
      } else {
        closeDBConnection();
      }
    }

    try {
      getDBConnection();
    } catch (Exception e) {
      throw new DAOException("Cannot start DAO session", e);
    }

    activeSession = true;
  }

  /**
   * Release external resources (they can be reinitialized later by calling
   * startSession()). If the DAO is used from an EJB, the EJB code must make
   * sure to close the session before any serialization of the EJB occurs.
   * Typically and EJB will start a new session when entering an EJB callback
   * and close it before exiting the callback.
   */
  public void stopSession() {
    /*
     * We allow multiple close() even if there is no current session. This is to
     * simplify client code.
     */

    activeSession = false;

    closeDBConnection();
  }

  /** Delete all existing entities */
  public void deleteAll() throws DAOException {

    PreparedStatement pStmt = null;
    ResultSet result;

    try {
      TestUtil.logTrace("[CoffeeDAO] deleteAll()");
      pStmt = getStmt(SQL_DELETE_ALL);
      pStmt.executeUpdate();
    } catch (SQLException e) {
      throw new DAOException("SQLException in remove(): " + e);
    } finally {
      closeStmt(pStmt, null);
    }
  }

  /**
   * Convenience method for test setup. Start its own session and delete all
   * pre-existing entities
   */
  public void cleanup() throws DAOException {
    startSession();
    try {
      deleteAll();
    } finally {
      stopSession();
    }
  }

  /** Make sure we have a valid DB connection handy */
  protected void getDBConnection() throws SQLException {
    TestUtil.logTrace("[DataSourceDAO] getDBConnection()");
    if (null == dbConnection) { /* Recycle connections */
      dbConnection = provider.getDBConnection();
    }
    if (dbConnection != null) {
      TestUtil.logTrace("dbConnection:" + dbConnection.toString());
    } else {
      TestUtil.logTrace("dbConnection:null");
    }
  }

  /**
   * Close current DB connection, if applicable.
   */
  protected void closeDBConnection() {
    TestUtil.logTrace("[DataSourceDAO] closeDBConnection()");
    if (null != dbConnection) {
      try {
        dbConnection.close();
      } catch (SQLException e) {
        TestUtil.logMsg("[DataSourceDAO] Closing DB connection: "
            + " Ignoring Exception " + e);
      } finally {
        dbConnection = null; /* Detect later we closed it */
      }
    }
  }

  /**
   * Generic method to get a SQL statement for current table.
   *
   * We get the SQL code associated with the <sqlTablePrefix>_<suffix> TS
   * property.
   */
  protected PreparedStatement getStmt(String suffix)
      throws DAOException, SQLException {

    PreparedStatement pStmt;
    String sqlPropName;
    String sqlStr;
    String msg;

    if (!activeSession) {
      throw new IllegalStateException("No current session");
    }

    TestUtil.logTrace("[DataSourceDAO] getStmt()");
    sqlPropName = sqlTablePrefix + "_" + suffix;
    TestUtil.logTrace("[DataSourceDAO] Get SQL for " + sqlPropName);
    sqlStr = TestUtil.getProperty(sqlPropName);
    if (null == sqlStr) {
      msg = "Cannot find SQL property '" + sqlPropName
          + "' please check tssql.stmt";
      TestUtil.logErr(msg);
      throw new DAOException(msg);
    }
    TestUtil.logMsg("[DataSourceDAO] SQL = " + sqlStr);
    pStmt = dbConnection.prepareStatement(sqlStr);

    return pStmt;
  }

  /**
   * Convenience method to close a ResultSet and a PreparedStatement in a safely
   * manner and ignoring any SQLException that could be thrown. This method is
   * designed to be called from a finally block to ensure the release of
   * associated resources.
   */
  public void closeStmt(PreparedStatement pStmt, ResultSet result) {
    try {
      if (null != result) {
        result.close();
      }
    } catch (SQLException e) {
      TestUtil.logTrace("[DataSourceDAO] Ignoring Exception while "
          + "closing ResultSet: " + e);
    }

    try {
      if (null != pStmt) {
        pStmt.close();
      }
    } catch (SQLException e) {
      TestUtil.logTrace("[DataSourceDAO] Ignoring Exception while "
          + "closing PreparedStatement: " + e);
    }
  }

  public void setPolicy(int policy) {
    if (DAO.STRICT_POLICY == policy) {
      this.strictPolicy = true;
    } else if (DAO.STANDARD_POLICY == policy) {
      this.strictPolicy = true;
    } else {
      throw new IllegalArgumentException("Unsupported policy " + policy);
    }
  }

  protected void finalize() throws Throwable {
    TestUtil.logTrace("[DataSourceDAO] finalize(): conn=" + dbConnection);
    if (activeSession || (null != dbConnection)) {
      /*
       * Pending connection. This should *NOT* happen. Client code did not stop
       * session before getting garbage collected :o(.
       */

      if (strictPolicy) {
        /*
         * We want to try to detect mis-behaving code that would not properly
         * close its DAO session (and underlying DB connections).
         */
        TestUtil.logErr(
            "CONNECTION LEAK: Openned session on GC " + "for DAO " + this);
        throw new IllegalStateException(
            "CONNECTION LEAK: Openned " + "session on GC for DAO " + this);
      }
    }
  }

}
