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

import javax.annotation.Resource;
import javax.enterprise.concurrent.ContextService;
import javax.enterprise.concurrent.ManagedTask;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/TxServlet")
public class TxServlet extends HttpServlet {

  @Resource(lookup = "java:comp/DefaultContextService")
  private ContextService cx;

  @Resource(lookup = "java:comp/UserTransaction")
  private UserTransaction ut;

  private static final String METHOD_PARAM_NAME = "methodname";

  private String tableName;

  private String username;

  private String password;

  private String sqlTemplate;

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    String methodName = req.getParameter(METHOD_PARAM_NAME);
    Map<String, String> params = null;
    try {
      params = formatMap(req);
    } catch (UnsupportedEncodingException e1) {
      e1.printStackTrace();
    }
    username = params.get(Constants.USERNAME);
    password = params.get(Constants.PASSWORD);
    tableName = params.get(Constants.TABLE_P);
    sqlTemplate = params.get(Constants.SQL_TEMPLATE);

    String s = (String) invoke(this, methodName, new Class[] {},
        new Object[] {});

    resp.getWriter().write(s);
  }

  public String TransactionOfExecuteThreadAndCommitTest()
      throws ServletException {
    PreparedStatement pStmt = null;
    Connection conn = null;
    Connection conn2 = null;

    try {
      int originCount = Util.getCount(tableName, username, password);
      ut.begin();
      conn = Util.getConnection(false, username, password);
      pStmt = conn.prepareStatement(sqlTemplate);
      pStmt.setInt(1, 99);
      pStmt.setString(2, "Type-99");
      pStmt.addBatch();
      pStmt.setInt(1, 100);
      pStmt.setString(2, "Type-100");
      pStmt.addBatch();
      pStmt.executeBatch();

      TestWorkInterface work = new TestTxWork();
      work.setUserName(username);
      work.setPassword(password);
      work.setSQLTemplate(sqlTemplate);
      Map<String, String> m = new HashMap();
      m.put(ManagedTask.TRANSACTION,
          ManagedTask.USE_TRANSACTION_OF_EXECUTION_THREAD);
      TestWorkInterface proxy = cx.createContextualProxy(work, m,
          TestWorkInterface.class);
      proxy.doSomeWork();
      ut.commit();
      int afterTransacted = Util.getCount(tableName, username, password);

      return String.valueOf(afterTransacted - originCount);
    } catch (Exception e) {
      throw new ServletException(e);
    } finally {
      try {
        if (pStmt != null)
          pStmt.close();
        if (conn != null)
          conn.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public String TransactionOfExecuteThreadAndRollbackTest()
      throws ServletException {
    PreparedStatement pStmt = null;
    Connection conn = null;
    Connection conn2 = null;

    try {
      int originCount = Util.getCount(tableName, username, password);
      ut.begin();
      conn = Util.getConnection(false, username, password);
      pStmt = conn.prepareStatement(sqlTemplate);
      pStmt.setInt(1, 99);
      pStmt.setString(2, "Type-99");
      pStmt.addBatch();
      pStmt.setInt(1, 100);
      pStmt.setString(2, "Type-100");
      pStmt.addBatch();
      pStmt.executeBatch();

      TestWorkInterface work = new TestTxWork();
      work.setUserName(username);
      work.setPassword(password);
      work.setSQLTemplate(sqlTemplate);
      Map<String, String> m = new HashMap();
      m.put(ManagedTask.TRANSACTION,
          ManagedTask.USE_TRANSACTION_OF_EXECUTION_THREAD);
      TestWorkInterface proxy = cx.createContextualProxy(work, m,
          TestWorkInterface.class);
      proxy.doSomeWork();
      ut.rollback();
      int afterTransacted = Util.getCount(tableName, username, password);

      return String.valueOf(afterTransacted - originCount);
    } catch (Exception e) {
      throw new ServletException(e);
    } finally {
      try {
        if (pStmt != null)
          pStmt.close();
        if (conn != null)
          conn.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public String SuspendAndCommitTest() throws ServletException {
    PreparedStatement pStmt = null;
    Connection conn = null;
    Connection conn2 = null;

    try {
      int originCount = Util.getCount(tableName, username, password);
      ut.begin();
      conn = Util.getConnection(false, username, password);
      pStmt = conn.prepareStatement(sqlTemplate);
      pStmt.setInt(1, 99);
      pStmt.setString(2, "Type-99");
      pStmt.addBatch();
      pStmt.setInt(1, 100);
      pStmt.setString(2, "Type-100");
      pStmt.addBatch();
      pStmt.executeBatch();
      TestWorkInterface work = new TestTxWork();
      work.setUserName(username);
      work.setPassword(password);
      work.setSQLTemplate(sqlTemplate);
      work.needBeginTx(true);
      work.needCommit(true);
      Map<String, String> m = new HashMap();
      m.put(ManagedTask.TRANSACTION, ManagedTask.SUSPEND);
      TestWorkInterface proxy = cx.createContextualProxy(work, m,
          TestWorkInterface.class);
      proxy.doSomeWork();
      ut.rollback();
      int afterTransacted = Util.getCount(tableName, username, password);

      return String.valueOf(afterTransacted - originCount);
    } catch (Exception e) {
      throw new ServletException(e);
    } finally {
      try {
        if (pStmt != null)
          pStmt.close();
        if (conn != null)
          conn.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public String SuspendAndRollbackTest() throws ServletException {
    PreparedStatement pStmt = null;
    Connection conn = null;
    Connection conn2 = null;

    try {
      int originCount = Util.getCount(tableName, username, password);
      ut.begin();
      conn = Util.getConnection(false, username, password);
      pStmt = conn.prepareStatement(sqlTemplate);
      pStmt.setInt(1, 99);
      pStmt.setString(2, "Type-99");
      pStmt.addBatch();
      pStmt.setInt(1, 100);
      pStmt.setString(2, "Type-100");
      pStmt.addBatch();
      pStmt.executeBatch();
      TestWorkInterface work = new TestTxWork();
      work.setUserName(username);
      work.setPassword(password);
      work.setSQLTemplate(sqlTemplate);
      work.needBeginTx(true);
      work.needRollback(true);
      Map<String, String> m = new HashMap();
      m.put(ManagedTask.TRANSACTION, ManagedTask.SUSPEND);
      TestWorkInterface proxy = cx.createContextualProxy(work, m,
          TestWorkInterface.class);
      proxy.doSomeWork();
      ut.commit();
      int afterTransacted = Util.getCount(tableName, username, password);

      return String.valueOf(afterTransacted - originCount);
    } catch (Exception e) {
      throw new ServletException(e);
    } finally {
      try {
        if (pStmt != null)
          pStmt.close();
        if (conn != null)
          conn.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public String DefaultAndCommitTest() throws ServletException {
    PreparedStatement pStmt = null;
    Connection conn = null;
    Connection conn2 = null;

    try {
      int originCount = Util.getCount(tableName, username, password);
      ut.begin();
      conn = Util.getConnection(false, username, password);
      pStmt = conn.prepareStatement(sqlTemplate);
      pStmt.setInt(1, 99);
      pStmt.setString(2, "Type-99");
      pStmt.addBatch();
      pStmt.setInt(1, 100);
      pStmt.setString(2, "Type-100");
      pStmt.addBatch();
      pStmt.executeBatch();
      TestWorkInterface work = new TestTxWork();
      work.setUserName(username);
      work.setPassword(password);
      work.setSQLTemplate(sqlTemplate);
      work.needBeginTx(true);
      work.needCommit(true);
      TestWorkInterface proxy = cx.createContextualProxy(work,
          TestWorkInterface.class);
      proxy.doSomeWork();
      ut.rollback();
      // int afterTransacted = Util.getCount(tableName, conn);
      int afterTransacted = Util.getCount(tableName, username, password);

      return String.valueOf(afterTransacted - originCount);
    } catch (Exception e) {
      throw new ServletException(e);
    } finally {
      try {
        if (pStmt != null)
          pStmt.close();
        if (conn != null)
          conn.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public static Object invoke(Object o, String methodName, Class[] paramTypes,
      Object[] args) throws ServletException {

    try {
      if (o == null || methodName == null || "".equals(methodName.trim())) {
        throw new IllegalArgumentException(
            "Object and methodName must not be null");
      }
      Method method = null;
      if (paramTypes != null && paramTypes.length > 0) {
        method = o.getClass().getMethod(methodName, paramTypes);
      } else {
        method = o.getClass().getMethod(methodName);
      }

      Object result = null;
      if (method != null) {
        if (args != null && args.length > 0) {
          result = method.invoke(o, args);
        } else {
          result = method.invoke(o);
        }
      }

      return result;

    } catch (NoSuchMethodException e) {
      throw new ServletException(e);
    } catch (InvocationTargetException e) {
      throw new ServletException(e);
    } catch (IllegalArgumentException e) {
      throw new ServletException(e);
    } catch (IllegalAccessException e) {
      throw new ServletException(e);
    }
  }

  private Map<String, String> formatMap(HttpServletRequest req)
      throws UnsupportedEncodingException {
    Map<String, String> props = new HashMap<String, String>();
    Enumeration<String> params = req.getParameterNames();
    while (params.hasMoreElements()) {
      String name = (String) params.nextElement();
      String value = req.getParameter(name);
      props.put(name, URLDecoder.decode(value, "utf8"));
    }
    return props;
  }
}
