/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.concurrency.spec.ManagedThreadFactory.tx;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.enterprise.concurrent.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.DriverManager;
import java.util.Properties;
import java.util.Enumeration;
import java.util.Set;

import com.sun.ts.lib.util.TestUtil;

public class Util {

  private static final String MANAGED_THREAD_FACTORY_SVC_JNDI_NAME = "java:comp/DefaultManagedThreadFactory";

  private Connection conn;

  private Util() {
  }

  public static <T> T waitForTaskComplete(final Future<T> future,
      final int maxTaskWaitTime)
      throws InterruptedException, ExecutionException, TimeoutException {
    T result = null;
    result = future.get(maxTaskWaitTime, TimeUnit.SECONDS);
    return result;
  }

  public static ManagedThreadFactory getManagedThreadFactory() {
    return lookup(MANAGED_THREAD_FACTORY_SVC_JNDI_NAME);
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
      conn = ds.getConnection(); // Try without user password for EE case
      if (conn == null) {
        conn = ds.getConnection(user, pwd); // For standalone cases
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

  public static Connection getConnection(String dbURL, String propsString,
      boolean autoCommit) {
    Connection conn = null;
    try {

      conn = DriverManager.getConnection(dbURL, strToProps(propsString));
      if (conn != null) {
        conn.setAutoCommit(autoCommit);
      }

    } catch (SQLException sqle) {
      TestUtil.logErr("failed to get connection.", sqle);
    }
    return conn;
  }

  public static Properties strToProps(String strProps) {

    Properties props = new Properties();
    TestUtil.logTrace("Props String = " + strProps);
    String strArray[] = strProps.split(":"); // Split the given string into
                                             // array of key value pairs

    for (String keyValuePair : strArray) {
      String strArray2[] = keyValuePair.split("="); // Take the key value pair
                                                    // and store it into
                                                    // properties
      TestUtil
          .logTrace("Setting property " + strArray2[0] + " = " + strArray2[1]);
      props.setProperty(strArray2[0], strArray2[1]);
    }

    // printProperties(props);
    return props;

  }

  private static void printProperties(Properties props) {
    Set<String> propertyNames = props.stringPropertyNames();
    for (String key : propertyNames) {
      TestUtil.logTrace(key + " = " + props.getProperty(key));
    }
  }

  public static void waitForTransactionBegan(CancelledTransactedTask pp,
      long maxListenerWaitTime, int poolInterval) {
    final long stopTime = System.currentTimeMillis() + maxListenerWaitTime;
    while (!pp.transactionBegin() && System.currentTimeMillis() < stopTime) {
      TestUtil.sleep(poolInterval);
    }
  }

  public static void waitTillThreadFinish(Thread thread) {
    long start = System.currentTimeMillis();

    while (thread.isAlive()) {
      try {
        Thread.sleep(5 * 1000);
      } catch (InterruptedException ignore) {
      }

      if ((System.currentTimeMillis() - start) > 30 * 1000) {
        throw new RuntimeException("wait task timeout");
      }
    }
  }
}
