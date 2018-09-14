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
 * $URL$ $LastChangedDate$
 */

package com.sun.ts.tests.jstl.common.wrappers;

import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.NClob;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLXML;
import java.sql.Struct;
import java.util.Properties;
import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Executor;

/**
 * <pre>
 * Simple Connection wrapper class performs two operations.
 *        - Log all life cycle events the spec defines for
 *          connection/transaction management.
 *        - Delegate all calls received to the underlying connection
 *          object.
 * </pre>
 */

public class TckConnectionWrapper implements java.sql.Connection {

  /* offsets in our ArrayList for LifeCycle */
  private static final int GET_TX_ISOLATION = 0;

  private static final int SET_AUTOCOMMIT = 1;

  private static final int SET_TX_ISOLATION = 2;

  private static final int COMMIT_ROLLBACK = 3;

  private static final int RESET_TX_ISOLATION = 4;

  private static final int RESET_AUTOCOMMIT = 5;

  private static final int CLOSE_CONNECTION = 6;

  private static final int MAXLIFECYCLE_SIZE = CLOSE_CONNECTION + 1;

  /**
   * The ServletContext in which to export the result of the lifecycle test.
   */
  private ServletContext _context = null;

  /**
   * Connection to the underlying RDBMS.
   */
  private Connection _conn = null;

  /**
   * The object to "log" events to.
   */
  private ArrayList _log = null;

  private int _autoCommitMode;

  private boolean _setIsolationLevel = false;

  private boolean _resetIsolationLevel = false;

  private int _newIsolationSetting = -1;

  /**
   * Creates new TckConnectionWrapper
   *
   * @param context
   *          context in which to export log results
   * @param conn
   *          connection to the underlying RDBMS
   */
  public TckConnectionWrapper(ServletContext context, Connection conn) {
    _context = context;
    _conn = conn;
    _log = new ArrayList(MAXLIFECYCLE_SIZE);

    for (int i = 0; i < MAXLIFECYCLE_SIZE; i++)
      _log.add(i, "");

    _setIsolationLevel = false;
    _resetIsolationLevel = false;
    _autoCommitMode = SET_AUTOCOMMIT;
    int isoLevel = 0;

    try {
      isoLevel = conn.getTransactionIsolation();
    } catch (Exception e) {
      ;
    }

    Integer newIsoLevel = (Integer) context.getAttribute("isoLevel");

    if (newIsoLevel != null)
      _newIsolationSetting = newIsoLevel.intValue();
    else
      _newIsolationSetting = isoLevel;
  }

  /*
   * public methods
   * ========================================================================
   */

  /**
   * @see java.sql.Connection
   */
  public void clearWarnings() throws java.sql.SQLException {
    _conn.clearWarnings();
  }

  /**
   * @see java.sql.Connection
   */
  public void close() throws java.sql.SQLException {
    _log.set(CLOSE_CONNECTION, "Connection.close()");
    ArrayList lifeCycle = new ArrayList();
    Iterator i = _log.iterator();

    /* prune out any unitialized entries */
    while (i.hasNext()) {
      String buf = (String) i.next();
      if (!buf.equals(""))
        lifeCycle.add(buf);
    }
    _context.setAttribute("connLog", lifeCycle);
    _conn.close();
  }

  /**
   * The event will be logged.
   * 
   * @see java.sql.Connection
   */
  public void commit() throws java.sql.SQLException {
    _log.set(COMMIT_ROLLBACK, "Connection.commit()");
    _conn.commit();
  }

  /**
   * @see java.sql.Connection
   */
  public java.sql.Statement createStatement() throws java.sql.SQLException {
    return _conn.createStatement();
  }

  /**
   * @see java.sql.Connection
   */
  public java.sql.Statement createStatement(int param, int param1)
      throws java.sql.SQLException {
    return _conn.createStatement(param, param1);
  }

  /**
   * @see java.sql.Connection
   */
  public java.sql.Statement createStatement(int param, int param1, int param2)
      throws java.sql.SQLException {
    return _conn.createStatement(param, param1, param2);
  }

  /**
   * @see java.sql.Connection
   */
  public boolean getAutoCommit() throws java.sql.SQLException {
    return _conn.getAutoCommit();
  }

  /**
   * @see java.sql.Connection
   */
  public java.lang.String getCatalog() throws java.sql.SQLException {
    return _conn.getCatalog();
  }

  /**
   * @throws SQLException
   * @return
   */
  public int getHoldability() throws java.sql.SQLException {
    return _conn.getHoldability();
  }

  /**
   * @see java.sql.Connection
   */
  public java.sql.DatabaseMetaData getMetaData() throws java.sql.SQLException {
    return _conn.getMetaData();
  }

  /**
   * This event will be logged.
   * 
   * @see java.sql.Connection
   */
  public int getTransactionIsolation() throws java.sql.SQLException {
    _log.set(GET_TX_ISOLATION, "Connection.getTransactionIsolation()");
    int isolationSetting = _conn.getTransactionIsolation();
    return isolationSetting;
  }

  /**
   * @see java.sql.Connection
   */
  public java.util.Map getTypeMap() throws java.sql.SQLException {
    return _conn.getTypeMap();
  }

  /**
   * @see java.sql.Connection
   */
  public java.sql.SQLWarning getWarnings() throws java.sql.SQLException {
    return _conn.getWarnings();
  }

  /**
   * @see java.sql.Connection
   */
  public boolean isClosed() throws java.sql.SQLException {
    return _conn.isClosed();
  }

  /**
   * @see java.sql.Connection
   */
  public boolean isReadOnly() throws java.sql.SQLException {
    return _conn.isReadOnly();
  }

  /**
   * @see java.sql.Connection
   */
  public java.lang.String nativeSQL(java.lang.String str)
      throws java.sql.SQLException {
    return _conn.nativeSQL(str);
  }

  /**
   * @see java.sql.Connection
   */
  public java.sql.CallableStatement prepareCall(java.lang.String str)
      throws java.sql.SQLException {
    return _conn.prepareCall(str);
  }

  /**
   * @see java.sql.Connection
   */
  public java.sql.CallableStatement prepareCall(java.lang.String str, int param,
      int param2) throws java.sql.SQLException {
    return _conn.prepareCall(str, param, param2);
  }

  /**
   * @see java.sql.Connection
   */
  public java.sql.CallableStatement prepareCall(java.lang.String str, int param,
      int param2, int param3) throws java.sql.SQLException {
    return _conn.prepareCall(str, param, param2, param3);
  }

  /**
   * @see java.sql.Connection
   */
  public java.sql.PreparedStatement prepareStatement(java.lang.String str)
      throws java.sql.SQLException {
    return _conn.prepareStatement(str);
  }

  /**
   * @see java.sql.Connection
   */
  public java.sql.PreparedStatement prepareStatement(java.lang.String str,
      int param) throws java.sql.SQLException {
    return _conn.prepareStatement(str, param);
  }

  /**
   * @see java.sql.Connection
   */
  public java.sql.PreparedStatement prepareStatement(java.lang.String str,
      int[] values) throws java.sql.SQLException {
    return _conn.prepareStatement(str, values);
  }

  /**
   * @see java.sql.Connection
   */
  public java.sql.PreparedStatement prepareStatement(java.lang.String str,
      java.lang.String[] str1) throws java.sql.SQLException {
    return _conn.prepareStatement(str, str1);
  }

  /**
   * @see java.sql.Connection
   */
  public java.sql.PreparedStatement prepareStatement(java.lang.String str,
      int param, int param2) throws java.sql.SQLException {
    return _conn.prepareStatement(str, param, param2);
  }

  /**
   * @see java.sql.Connection
   */
  public java.sql.PreparedStatement prepareStatement(java.lang.String str,
      int param, int param2, int param3) throws java.sql.SQLException {
    return _conn.prepareStatement(str, param, param2, param3);
  }

  /**
   * @see java.sql.Connection
   */
  public void releaseSavepoint(java.sql.Savepoint savepoint)
      throws java.sql.SQLException {
    _conn.releaseSavepoint(savepoint);
  }

  /**
   * This event will be logged.
   * 
   * @see java.sql.Connection
   */
  public void rollback() throws java.sql.SQLException {
    _log.set(COMMIT_ROLLBACK, "Connection.rollback()");
    _conn.rollback();
  }

  /**
   * @see java.sql.Connection
   */
  public void rollback(java.sql.Savepoint savepoint)
      throws java.sql.SQLException {
    _conn.rollback(savepoint);
  }

  /**
   * This event will be logged.
   * 
   * @see java.sql.Connection
   */
  public void setAutoCommit(boolean param) throws java.sql.SQLException {
    _log.set(_autoCommitMode, "Connection.setAutoCommit(" + param + ")");
    _conn.setAutoCommit(param);
    _autoCommitMode = RESET_AUTOCOMMIT;
  }

  /**
   * @see java.sql.Connection
   */
  public void setCatalog(java.lang.String str) throws java.sql.SQLException {
    _conn.setCatalog(str);
  }

  /**
   * @see java.sql.Connection
   */
  public void setHoldability(int param) throws java.sql.SQLException {
    _conn.setHoldability(param);
  }

  /**
   * @see java.sql.Connection
   */
  public void setReadOnly(boolean param) throws java.sql.SQLException {
    _conn.setReadOnly(param);
  }

  /**
   * @see java.sql.Connection
   */
  public java.sql.Savepoint setSavepoint() throws java.sql.SQLException {
    return _conn.setSavepoint();
  }

  /**
   * @see java.sql.Connection
   */
  public java.sql.Savepoint setSavepoint(java.lang.String str)
      throws java.sql.SQLException {
    return _conn.setSavepoint(str);
  }

  /**
   * This event will be logged.
   * 
   * @see java.sql.Connection
   */
  public void setTransactionIsolation(int param) throws java.sql.SQLException {

    if (!_setIsolationLevel) {
      setTransactionIsolationSetting(SET_TX_ISOLATION, param);
      _setIsolationLevel = true;

    } else if (!_resetIsolationLevel) {
      setTransactionIsolationSetting(RESET_TX_ISOLATION, param);
      _resetIsolationLevel = true;
    }
    _conn.setTransactionIsolation(param);
  }

  /**
   * @see java.sql.Connection
   */
  public void setTypeMap(java.util.Map map) throws java.sql.SQLException {
    _conn.setTypeMap(map);
  }

  private void setTransactionIsolationSetting(int slot, int param) {
    String isolationLevel = null;
    // Connection Transaction levels
    // TRANSACTION_READ_UNCOMMITTED=1 (iso 0)
    // TRANSACTION_READ_COMMITTED=2 (iso 1)
    // TRANSACTION_REPEATABLE_READ=4 (iso 2)
    // TRANSACTION_SERIALIZABLE=8 (iso 3)

    switch (param) {
    case Connection.TRANSACTION_READ_UNCOMMITTED:
      isolationLevel = "TRANSACTION_READ_UNCOMMITTED";
      break;
    case Connection.TRANSACTION_READ_COMMITTED:
      isolationLevel = "TRANSACTION_READ_COMMITTED";
      break;
    case Connection.TRANSACTION_REPEATABLE_READ:
      isolationLevel = "TRANSACTION_REPEATABLE_READ";
      break;
    case Connection.TRANSACTION_SERIALIZABLE:
      isolationLevel = "TRANSACTION_SERIALIZABLE";
      break;
    }
    if (slot == RESET_TX_ISOLATION) {
      isolationLevel = "";
    }
    _log.set(slot,
        "Connection.setTransactionIsolation(" + isolationLevel + ")");
  }

  public Clob createClob() throws SQLException {
    throw new SQLFeatureNotSupportedException("Not supported yet.");
  }

  public Blob createBlob() throws SQLException {
    throw new SQLFeatureNotSupportedException("Not supported yet.");
  }

  public NClob createNClob() throws SQLException {
    throw new SQLFeatureNotSupportedException("Not supported yet.");
  }

  public SQLXML createSQLXML() throws SQLException {
    throw new SQLFeatureNotSupportedException("Not supported yet.");
  }

  public boolean isValid(int timeout) throws SQLException {
    throw new SQLFeatureNotSupportedException("Not supported yet.");
  }

  public void setClientInfo(String name, String value)
      throws SQLClientInfoException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void setClientInfo(Properties properties)
      throws SQLClientInfoException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public String getClientInfo(String name) throws SQLException {
    throw new SQLFeatureNotSupportedException("Not supported yet.");
  }

  public Properties getClientInfo() throws SQLException {
    throw new SQLFeatureNotSupportedException("Not supported yet.");
  }

  public Array createArrayOf(String typeName, Object[] elements)
      throws SQLException {
    throw new SQLFeatureNotSupportedException("Not supported yet.");
  }

  public Struct createStruct(String typeName, Object[] attributes)
      throws SQLException {
    throw new SQLFeatureNotSupportedException("Not supported yet.");
  }

  public <T> T unwrap(Class<T> iface) throws SQLException {
    throw new SQLFeatureNotSupportedException("Not supported yet.");
  }

  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    throw new SQLFeatureNotSupportedException("Not supported yet.");
  }

  public int getNetworkTimeout() throws SQLException {
    throw new SQLFeatureNotSupportedException("Not supported yet.");
  }

  public void setNetworkTimeout(Executor executor, int milliseconds)
      throws SQLException {
    throw new SQLFeatureNotSupportedException("Not supported yet.");
  }

  public void abort(Executor executor) throws SQLException {
    throw new SQLFeatureNotSupportedException("Not supported yet.");
  }

  public String getSchema() throws SQLException {
    throw new SQLFeatureNotSupportedException("Not supported yet.");
  }

  public void setSchema(String schema) throws SQLException {
    throw new SQLFeatureNotSupportedException("Not supported yet.");
  }
}
