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

package com.sun.ts.tests.concurrency.spec.ContextService.tx;

import com.sun.ts.lib.util.TestUtil;

import javax.enterprise.concurrent.ContextService;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.transaction.Transaction;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Util {

  private Util() {
  }

  public static ContextService lookupDefaultContextService()
      throws NamingException {
    try {
      InitialContext ctx = new InitialContext();
      ContextService cs = (ContextService) ctx
          .lookup("java:comp/DefaultContextService");
      return cs;

    } catch (NamingException e) {
      throw e;
    }
  }

  public static Transaction lookupUserTransaction() throws NamingException {
    try {
      InitialContext ctx = new InitialContext();
      Transaction tx = (Transaction) ctx.lookup("java:comp/UserTransaction");
      return tx;

    } catch (NamingException e) {
      throw e;
    }
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

  public static int getCount(String tableName, Connection conn) {
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
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return -1;
  }

  public static int getResults(String tableName, Connection conn) {
    Statement stmt = null;
    try {
      final String queryStr = "select * from " + tableName;
      stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery(queryStr);
      while (rs.next()) {
        System.out.println(rs.getString(1));
        System.out.println(rs.getString(2));
      }
    } catch (SQLException e1) {
      e1.printStackTrace();
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return -1;
  }

  public static Connection getConnection(boolean autoCommit, String username,
      String password) {
    DataSource ds = Util.lookup("jdbc/DB1");
    Connection conn = Util.getConnection(ds, username, password, autoCommit);
    return conn;
  }
}
