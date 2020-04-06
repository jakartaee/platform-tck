/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.concurrency.spec.ManagedScheduledExecutorService.tx;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import jakarta.enterprise.concurrent.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.sun.ts.lib.util.TestUtil;

public class Util {

  private static final String MANAGED_SCHEDULED_EXECUTOR_SVC_JNDI_NAME = "java:comp/DefaultManagedScheduledExecutorService";

  private Util() {
  }

  public static <T> T waitForTaskComplete(final Future<T> future,
      final int maxTaskWaitTime)
      throws InterruptedException, ExecutionException, TimeoutException {
    T result = null;
    result = future.get(maxTaskWaitTime, TimeUnit.SECONDS);
    return result;
  }

  public static ManagedScheduledExecutorService getManagedExecutorService() {
    return lookup(MANAGED_SCHEDULED_EXECUTOR_SVC_JNDI_NAME);
  }

  public static <T> T lookup(String jndiName) {
    Context ctx = null;
    T targetObject = null;
    try {
      ctx = new InitialContext();
      targetObject = (T) ctx.lookup(jndiName);
    } catch (Exception e) {
    } finally {
      try {
        ctx.close();
      } catch (NamingException e) {
        TestUtil.logErr("failed to lookup resource.", e);
      }
    }
    return targetObject;
  }

  public static Connection getConnection(DataSource ds, String user, String pwd,
      boolean autoCommit) {
    Connection conn = null;
    try {
      conn = ds.getConnection();
      if (conn == null) {
        conn = ds.getConnection(user, pwd);
      }
      if (null != conn) {
        conn.setAutoCommit(autoCommit);
      }
    } catch (SQLException e) {
      TestUtil.logErr("failed to get connection.", e);
    }
    return conn;
  }

  public static String getUrl(String servletUri, String host, int port) {
    return "http://" + host + ":" + port + Constants.CONTEXT_PATH + servletUri;
  }

  public static int getCount(String tableName, String username,
      String password) {
    Connection conn = getConnection(true, username, password);
    Statement stmt = null;
    try {
      final String queryStr = "select count(*) from " + tableName;
      stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery(queryStr);
      if (rs.next()) {
        return rs.getInt(1);
      }
    } catch (SQLException e1) {
      e1.printStackTrace();
    } finally {
      try {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return -1;
  }

  /**
   * get count by specifying connection. the caller should take care of closing
   * connection.
   * 
   * @param conn
   * @param tableName
   * @param username
   * @param password
   * @return
   */
  public static int getCount(Connection conn, String tableName, String username,
      String password) {
    Statement stmt = null;
    try {
      final String queryStr = "select count(*) from " + tableName;
      stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery(queryStr);
      if (rs.next()) {
        return rs.getInt(1);
      }
    } catch (SQLException e1) {
      e1.printStackTrace();
    } finally {
      try {
        stmt.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return -1;
  }

  public static Connection getConnection(boolean autoCommit, String username,
      String password) {
    DataSource ds = Util.lookup(Constants.DS_JNDI_NAME);
    Connection conn = Util.getConnection(ds, username, password, autoCommit);
    return conn;
  }

  public static void waitForTransactionBegan(CancelledTransactedTask pp,
      long maxListenerWaitTime, int poolInterval) {
    final long stopTime = System.currentTimeMillis() + maxListenerWaitTime;
    while (!pp.transactionBegin() && System.currentTimeMillis() < stopTime) {
      TestUtil.sleep(poolInterval);
    }
  }
}
