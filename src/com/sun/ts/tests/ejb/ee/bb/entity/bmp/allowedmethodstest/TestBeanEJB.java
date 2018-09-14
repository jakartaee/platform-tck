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

package com.sun.ts.tests.ejb.ee.bb.entity.bmp.allowedmethodstest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import javax.ejb.*;
import javax.naming.*;
import java.rmi.*;
import java.sql.*;
import javax.sql.DataSource;

public class TestBeanEJB implements EntityBean, TimedObject {
  private EntityContext ectx = null;

  private TSNamingContext nctx = null;

  private Helper helperRef = null;

  private String role = "Administrator";

  private Hashtable table = new Hashtable();

  private static final String testLookup = "java:comp/env/ejb/Helper";

  private static final String DATASOURCE = "java:comp/env/jdbc/DBTimer";

  private transient Connection dbConnection = null;

  private String dsname = null; // dataSourcename

  private DataSource ds = null; // dataSource

  private int cofID = 0; // Coffee ID (Primary Key)

  private String cofName = null; // Coffee Name

  private float cofPrice = 0; // Coffee Price

  // These are the method tests
  private static final String tests[] = { "ejbCreate", "ejbPostCreate",
      "ejbFindTheBean", "ejbLoad", "ejbStore", "setEntityContext",
      "businessMethod", "ejbHomeDoTest" };

  // This is the results of the operation tests
  private static final Properties methodList[] = { new Properties(),
      new Properties(), new Properties(), new Properties(), new Properties(),
      new Properties(), new Properties(), new Properties() };

  private boolean ejbLoadCalled = false;

  private boolean ejbStoreCalled = false;

  public Integer ejbCreate(Properties p, Helper ref, int coffeeID,
      String coffeName, float coffeePrice, int flag) throws CreateException {
    TestUtil.logTrace("ejbCreate");
    try {
      TestUtil.logMsg("Initialize remote logging");
      TestUtil.init(p);

      tableInit();
      createNewRow(coffeeID, coffeName, coffeePrice);
    } catch (RemoteLoggingInitException e) {
      throw new CreateException(e.getMessage());
    } catch (Exception e) {
      throw new CreateException(
          "Unexpected Exception occurred in ejbCreate: " + e);
    }

    if (flag == 1)
      doOperationTests("ejbCreate");
    try {
      ref.setData(table);
    } catch (Exception e) {
      TestUtil.logErr("Caught Unexpected Exception in ejbCreate");
      throw new CreateException("Exception occurred: " + e);
    }

    return new Integer(coffeeID);
  }

  public void ejbPostCreate(Properties p, Helper ref, int coffeeID,
      String coffeName, float coffeePrice, int flag) {
    TestUtil.logTrace("ejbPostCreate");
    if (flag == 2)
      doOperationTests("ejbPostCreate");
    try {
      ref.setData(table);
    } catch (Exception e) {
      TestUtil.logErr("Caught Unexpected Exception in ejbPostCreate");
      throw new EJBException("Exception occurred: " + e);
    }
  }

  public Integer ejbCreate(Properties p, int coffeeID, String coffeName,
      float coffeePrice) throws CreateException {
    TestUtil.logTrace("ejbCreate");
    try {
      TestUtil.logMsg("Initialize remote logging");
      TestUtil.init(p);

      tableInit();
      createNewRow(coffeeID, coffeName, coffeePrice);
    } catch (RemoteLoggingInitException e) {
      throw new CreateException(e.getMessage());
    } catch (Exception e) {
      throw new CreateException(
          "Unexpected Exception occurred in ejbCreate: " + e);
    }

    return new Integer(coffeeID);
  }

  public void ejbPostCreate(Properties p, int coffeeID, String coffeName,
      float coffeePrice) {
    TestUtil.logTrace("ejbPostCreate");
  }

  public void setEntityContext(EntityContext c) {
    TestUtil.logTrace("setEntityContext");
    ectx = c;
    try {
      TestUtil.logMsg("Obtain naming context");
      nctx = new TSNamingContext();

      // Lookup DataSource from JNDI
      TestUtil.logMsg("Lookup DataSource from JNDI : " + DATASOURCE);
      this.dsname = DATASOURCE;
      this.ds = (DataSource) nctx.lookup(this.dsname);
      TestUtil.logMsg("dsname=" + this.dsname + "ds=" + this.ds);
    } catch (NamingException e) {
      TestUtil.logErr("Unexpected NamingException ... ");
      throw new EJBException("Unable to obtain naming context:" + e);
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception ... ");
      throw new EJBException(
          "Unexpected Exception occurred in setEntityContext" + e);
    }
    doOperationTests("setEntityContext");
  }

  public void unsetEntityContext() {
    TestUtil.logTrace("unsetEntityContext");
  }

  public void ejbLoad() {
    TestUtil.logTrace("ejbLoad");
    if (ejbLoadCalled)
      return;
    doOperationTests("ejbLoad");
    ejbLoadCalled = true;
    ejbStoreCalled = false;
  }

  public void ejbStore() {
    TestUtil.logTrace("ejbStore");
    if (ejbStoreCalled)
      return;
    doOperationTests("ejbStore");
    ejbStoreCalled = true;
    ejbLoadCalled = false;
  }

  public Integer ejbFindTheBean(Properties p, Integer key, Helper ref)
      throws FinderException {
    TestUtil.logTrace("ejbFindTheBean");
    try {
      doOperationTests("ejbFindTheBean");
      ref.setData(table);
    } catch (Exception e) {
      throw new EJBException(e.getMessage());
    }
    return new Integer(10);
  }

  public Integer ejbFindByPrimaryKey(Integer key) throws FinderException {
    TestUtil.logTrace("ejbFindByPrimaryKey");

    try {
      TestUtil.logMsg("Check if Primary Key Exists");
      boolean foundKey = keyExists(key.intValue());
      if (foundKey)
        return key;
      else
        throw new FinderException("Key not found: " + key);
    } catch (Exception e) {
      throw new FinderException("Exception occurred: " + e);
    }
  }

  public void ejbRemove() throws RemoveException {
    TestUtil.logTrace("ejbRemove");
    TestUtil.logMsg("PrimaryKey=" + ectx.getPrimaryKey());
    try {
      removeRow(((Integer) ectx.getPrimaryKey()).intValue());
    } catch (Exception e) {
      throw new RemoveException("Unexpected Exception occurred in ejbRemove");
    }
  }

  public void ejbActivate() {
    TestUtil.logTrace("ejbActivate");
  }

  public void ejbPassivate() {
    TestUtil.logTrace("ejbPassivate");
  }

  public void ejbTimeout(javax.ejb.Timer timer) {
    TestUtil.logTrace("ejbTimeout");
  }

  public TestBean ejbHomeDoTest(Helper ref) {
    TestUtil.logTrace("ejbHomeDoTest");
    try {
      doOperationTests("ejbHomeDoTest");
      ref.setData(table);
    } catch (Exception e) {
      throw new EJBException(e.getMessage());
    }
    return null;
  }

  // ===========================================================
  // TestBean interface (our business methods)

  public Hashtable getResults() {
    TestUtil.logTrace("getResults");
    return table;
  }

  public void businessMethod(Helper ref) {
    TestUtil.logTrace("businessMethod");
    try {
      doOperationTests("businessMethod");
      ref.setData(table);
    } catch (Exception e) {
      throw new EJBException(e.getMessage());
    }
  }

  // ===========================================================
  // Private methods

  private int testIndex(String s) {
    TestUtil.logTrace("testIndex");
    for (int i = 0; i < tests.length; i++)
      if (s.equals(tests[i]))
        return i;
    return -1;
  }

  private void setTestList(int i) {
    TestUtil.logTrace("setTestList");
    methodList[i].setProperty("getEJBHome", "true");
    methodList[i].setProperty("getCallerPrincipal", "true");
    methodList[i].setProperty("isCallerInRole", "true");
    methodList[i].setProperty("getEJBObject", "true");
    methodList[i].setProperty("JNDI_Access", "true");
    methodList[i].setProperty("getPrimaryKey", "true");
    methodList[i].setProperty("getEJBLocalHome", "true");
    methodList[i].setProperty("getEJBLocalObject", "true");
    methodList[i].setProperty("getTimerService", "true");
    methodList[i].setProperty("TimerService_Methods_Test1", "true");
    methodList[i].setProperty("TimerService_Methods_Test2", "true");
    methodList[i].setProperty("TimerService_Methods_Test3", "true");
    methodList[i].setProperty("TimerService_Methods_Test4", "true");
    methodList[i].setProperty("TimerService_Methods_Test5", "true");
    methodList[i].setProperty("TimerService_Methods_Test6", "true");
    methodList[i].setProperty("TimerService_Methods_Test7", "true");
    methodList[i].setProperty("getRollbackOnly", "true");
  }

  private void doOperationTests(String s) {
    TestUtil.logTrace("doOperationTests");
    int i = testIndex(s);
    TestUtil.logMsg("index for " + s + " is " + i);
    TestUtil.logMsg("methodList length=" + methodList.length);
    TestUtil.logMsg("tests length=" + tests.length);
    setTestList(i);
    TestUtil.logMsg("Operations testing for " + s + " method ...");

    // getEJBHome test
    try {
      ectx.getEJBHome();
      TestUtil.logMsg("Operations test: getEJBHome() - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("getEJBHome", "false");
      TestUtil.logMsg("Operations test: getEJBHome() - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("getEJBHome", "unexpected");
      TestUtil.logMsg(
          "Operations test: getEJBHome() - not allowed (Unexpected Exception) - "
              + e);
    }

    // getCallerPrincipal test
    try {
      ectx.getCallerPrincipal();
      TestUtil.logMsg("Operations test: getCallerPrincipal() - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("getCallerPrincipal", "false");
      TestUtil.logMsg("Operations test: getCallerPrincipal() - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("getCallerPrincipal", "unexpected");
      TestUtil.logMsg(
          "Operations test: getCallerPrincipal() - not allowed (Unexpected Exception) - "
              + e);
    }

    // isCallerInRole test
    try {
      ectx.isCallerInRole(role);
      TestUtil.logMsg("Operations test: isCallerInRole() - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("isCallerInRole", "false");
      TestUtil.logMsg("Operations test: isCallerInRole() - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("isCallerInRole", "unexpected");
      TestUtil.logMsg(
          "Operations test: isCallerInRole() - not allowed (Unexpected Exception) - "
              + e);
    }

    // getEJBObject test
    try {
      ectx.getEJBObject();
      TestUtil.logMsg("Operations test: getEJBObject() - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("getEJBObject", "false");
      TestUtil.logMsg("Operations test: getEJBObject() - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("getEJBObject", "unexpected");
      TestUtil.logMsg(
          "Operations test: getEJBObject() - not allowed (Unexpected Exception) - "
              + e);
    }

    // JNDI Access test
    try {
      Context ctx = (Context) nctx.lookup("java:comp/env");
      TestUtil.logMsg("Operations test: JNDI_Access - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("JNDI_Access", "false");
      TestUtil.logMsg("Operations test: JNDI_Access() - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("JNDI_Access", "unexpected");
      TestUtil.logMsg(
          "Operations test: JNDI_Access() - not allowed (Unexpected Exception) - "
              + e);
    }

    // getPrimaryKey test
    try {
      ectx.getPrimaryKey();
      TestUtil.logMsg("Operations test: getPrimaryKey() - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("getPrimaryKey", "false");
      TestUtil.logMsg("Operations test: getPrimaryKey() - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("getPrimaryKey", "unexpected");
      TestUtil.logMsg(
          "Operations test: getPrimaryKey() - not allowed (Unexpected Exception) - "
              + e);
    }

    // getEJBLocalHome test
    try {
      ectx.getEJBLocalHome();
      TestUtil.logMsg("Operations test: getEJBLocalHome() - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("getEJBLocalHome", "false");
      TestUtil.logMsg("Operations test: getEJBLocalHome() - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("getEJBLocalHome", "unexpected");
      TestUtil.logMsg(
          "Operations test: getEJBLocalHome() - not allowed (Unexpected Exception) - "
              + e);
    }

    // getEJBLocalObject test
    try {
      ectx.getEJBLocalObject();
      TestUtil.logMsg("Operations test: getEJBLocalObject() - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("getEJBLocalObject", "false");
      TestUtil.logMsg("Operations test: getEJBLocalObject() - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("getEJBLocalObject", "unexpected");
      TestUtil.logMsg(
          "Operations test: getEJBLocalObject() - not allowed (Unexpected Exception) - "
              + e);
    }

    // getTimerService test
    try {
      ectx.getTimerService();
      TestUtil.logMsg("Operations test: getTimerService() - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("getTimerService", "false");
      TestUtil.logMsg("Operations test: getTimerService() - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("getTimerService", "unexpected");
      TestUtil.logMsg(
          "Operations test: getTimerService() - not allowed (Unexpected Exception) - "
              + e);
    }

    // TimerService_Methods_Test1
    try {
      javax.ejb.TimerService timesrv = ectx.getTimerService();
      javax.ejb.Timer tt = timesrv.createTimer((long) 10000, "test1");
      tt.cancel();
      TestUtil
          .logMsg("Operations test: TimerService_Methods_Test1() - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("TimerService_Methods_Test1", "false");
      TestUtil.logMsg(
          "Operations test: TimerService_Methods_Test1() - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("TimerService_Methods_Test1", "unexpected");
      TestUtil.logMsg(
          "Operations test: TimerService_Methods_Test1() - not allowed (Unexpected Exception) - "
              + e);
    }

    // TimerService_Methods_Test2
    try {
      javax.ejb.TimerService timesrv2 = ectx.getTimerService();
      javax.ejb.Timer t2 = timesrv2.createTimer((long) 10000, (long) 10000,
          "test2");
      t2.cancel();
      TestUtil
          .logMsg("Operations test: TimerService_Methods_Test2() - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("TimerService_Methods_Test2", "false");
      TestUtil.logMsg(
          "Operations test: TimerService_Methods_Test2() - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("TimerService_Methods_Test2", "unexpected");
      TestUtil.logMsg(
          "Operations test: TimerService_Methods_Test2() - not allowed (Unexpected Exception) - "
              + e);
    }
    // TimerService_Methods_Test3
    try {
      long expiration = (System.currentTimeMillis() + (long) 900000);
      java.util.Date d = new java.util.Date(expiration);
      javax.ejb.TimerService timesrv3 = ectx.getTimerService();
      javax.ejb.Timer t3 = timesrv3.createTimer(d, "test3");
      t3.cancel();
      TestUtil
          .logMsg("Operations test: TimerService_Methods_Test3() - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("TimerService_Methods_Test3", "false");
      TestUtil.logMsg(
          "Operations test: TimerService_Methods_Test3() - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("TimerService_Methods_Test3", "unexpected");
      TestUtil.logMsg(
          "Operations test: TimerService_Methods_Test3() - not allowed (Unexpected Exception) - "
              + e);
    }

    // TimerService_Methods_Test4
    try {
      long expiration = (System.currentTimeMillis() + (long) 900000);
      java.util.Date d = new java.util.Date(expiration);
      javax.ejb.TimerService timesrv4 = ectx.getTimerService();
      javax.ejb.Timer t4 = timesrv4.createTimer(d, (long) 10000, "test4");
      t4.cancel();
      TestUtil
          .logMsg("Operations test: TimerService_Methods_Test4() - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("TimerService_Methods_Test4", "false");
      TestUtil.logMsg(
          "Operations test: TimerService_Methods_Test4() - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("TimerService_Methods_Test4", "unexpected");
      TestUtil.logMsg(
          "Operations test: TimerService_Methods_Test4() - not allowed (Unexpected Exception) - "
              + e);
    }

    // TimerService_Methods_Test5
    try {
      javax.ejb.TimerService ts = ectx.getTimerService();
      Collection ccol = ts.getTimers();
      TestUtil
          .logMsg("Operations test: TimerService_Methods_Test5() - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("TimerService_Methods_Test5", "false");
      TestUtil.logMsg(
          "Operations test: TimerService_Methods_Test5() - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("TimerService_Methods_Test5", "unexpected");
      TestUtil.logMsg(
          "Operations test: TimerService_Methods_Test5() - not allowed (Unexpected Exception) - "
              + e);
    }

    // TimerService_Methods_Test6
    try {
      javax.ejb.TimerService timesrv6 = ectx.getTimerService();
      javax.ejb.Timer t6 = timesrv6.createTimer((long) 10000, "test6");
      t6.getHandle();
      t6.cancel();
      TestUtil
          .logMsg("Operations test: TimerService_Methods_Test6() - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("TimerService_Methods_Test6", "false");
      TestUtil.logMsg(
          "Operations test: TimerService_Methods_Test6() - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("TimerService_Methods_Test6", "unexpected");
      TestUtil.logMsg(
          "Operations test: TimerService_Methods_Test6() - not allowed (Unexpected Exception) - "
              + e);
    }

    // TimerService_Methods_Test7
    try {
      javax.ejb.TimerService timesrv7 = ectx.getTimerService();
      javax.ejb.Timer t7 = timesrv7.createTimer((long) 10000, "test7");
      t7.cancel();
      TestUtil
          .logMsg("Operations test: TimerService_Methods_Test7() - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("TimerService_Methods_Test7", "false");
      TestUtil.logMsg(
          "Operations test: TimerService_Methods_Test7() - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("TimerService_Methods_Test7", "unexpected");
      TestUtil.logMsg(
          "Operations test: TimerService_Methods_Test7() - not allowed (Unexpected Exception) - "
              + e);
    }

    // getRollbackOnly test
    try {
      ectx.getRollbackOnly();
      TestUtil.logMsg("Operations test: getRollbackOnly() - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("getRollbackOnly", "false");
      TestUtil.logMsg("Operations test: getRollbackOnly() - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("getRollbackOnly", "unexpected");
      TestUtil.logMsg(
          "Operations test: getRollbackOnly() - not allowed (Unexpected Exception) - "
              + e);
    }

    table.put(s, methodList[i]);
  }

  // ===========================================================

  public void doTimer(long duration, String info) {
    TestUtil.logTrace("doTimer");
    javax.ejb.TimerService ts = ectx.getTimerService();
    TestUtil.logTrace("create Timer");
    javax.ejb.Timer t = ts.createTimer(duration, info);
  }

  public void findAndCancelTimer() {
    Collection ccol = null;
    try {
      TestUtil.logTrace("findAndCancelTimer method entered");
      javax.ejb.TimerService ts = ectx.getTimerService();
      TestUtil.logTrace("Get Timers");
      ccol = ts.getTimers();
      if (ccol.size() != 0) {
        TestUtil.logTrace("Collection size is: " + ccol.size());
        Iterator i = ccol.iterator();
        while (i.hasNext()) {
          TestUtil.logTrace("Get next timer");
          javax.ejb.Timer t = (javax.ejb.Timer) i.next();
          TestUtil.logTrace("Next timer to Cancel: " + t.getInfo());
          t.cancel();
        }
      } else {
        TestUtil.logTrace("Timer Collection is null");
      }
    } catch (Exception e) {
      throw new EJBException("findAndCancelTimer: " + e);
    }
  }

  // ===========================================================
  // DB Access methods

  private void getDBConnection() throws SQLException {
    TestUtil.logTrace("getDBConnection");
    if (dbConnection != null) {
      try {
        closeDBConnection();
      } catch (Exception e) {
        TestUtil.logErr("Exception occurred trying to close DB Connection", e);
      }
    }
    dbConnection = ds.getConnection();
  } // end getDBConnection

  private void closeDBConnection() throws SQLException {
    TestUtil.logTrace("closeDBConnection");
    if (dbConnection != null) {
      dbConnection.close();
      dbConnection = null;
    }
  } // end closeDBConnection

  private void tableInit() throws SQLException {
    Statement stmt = null;
    TestUtil.logTrace("tableInit");

    getDBConnection();

    try {
      stmt = dbConnection.createStatement();
      String sqlStr = TestUtil.getProperty("BB_Tab_Delete");
      stmt.executeUpdate(sqlStr);
      TestUtil.logMsg(
          "Deleted all rows from table " + TestUtil.getTableName(sqlStr));
    } catch (SQLException s) {
      throw new SQLException("SQL Exception in tableInit:" + s.getMessage());
    } finally {
      try {
        if (stmt != null)
          stmt.close();
        closeDBConnection();
      } catch (SQLException e) {
        TestUtil.logErr("SQLException occurred closing DB Connection", e);
      }
    }
  } /* end tableInit */

  private void createNewRow(int cofID, String cofName, float cofPrice)
      throws CreateException, SQLException {
    PreparedStatement pStmt = null;
    TestUtil.logTrace("createNewRow");

    getDBConnection();

    try {
      String sqlStr = TestUtil.getProperty("BB_Insert1");
      TestUtil.logMsg(sqlStr);
      pStmt = dbConnection.prepareStatement(sqlStr);
      pStmt.setInt(1, cofID);
      pStmt.setString(2, cofName);
      pStmt.setFloat(3, cofPrice);
      if (pStmt.executeUpdate() != 1) {
        throw new CreateException("SQL INSERT failed in createNewRow");
      } else {
        this.cofID = cofID;
        this.cofName = cofName;
        this.cofPrice = cofPrice;
      }
    } catch (SQLException e) {
      throw new SQLException("SQL Exception in createNewRow" + e.getMessage());
    } finally {
      try {
        if (pStmt != null)
          pStmt.close();
        closeDBConnection();
      } catch (SQLException e) {
        TestUtil.logErr("SQLException occurred in createNewRow", e);
      }
    }
  } /* end createNewRow */

  private boolean keyExists(int pkey) throws SQLException {
    PreparedStatement pStmt = null;
    ResultSet result = null;

    TestUtil.logTrace("keyExists");

    getDBConnection();

    try {
      String sqlStr = TestUtil.getProperty("BB_Select1");
      TestUtil.logMsg(sqlStr);
      pStmt = dbConnection.prepareStatement(sqlStr);
      pStmt.setInt(1, pkey);
      result = pStmt.executeQuery();
      if (!result.next())
        return false;
      else
        return true;
    } catch (SQLException e) {
      throw new SQLException(
          "Caught SQL Exception in keyExists" + e.getMessage());
    } finally {
      try {
        if (result != null)
          result.close();
        if (pStmt != null)
          pStmt.close();
        closeDBConnection();
      } catch (SQLException e) {
        TestUtil.logErr("SQLException occurred in keyExists", e);
      }
    }
  } /* end keyExists */

  private void removeRow(int pkey) throws SQLException {
    PreparedStatement pStmt = null;

    TestUtil.logTrace("removeRow");

    getDBConnection();

    try {
      String sqlStr = TestUtil.getProperty("BB_Delete1");
      TestUtil.logMsg(sqlStr);
      pStmt = dbConnection.prepareStatement(sqlStr);
      pStmt.setInt(1, pkey);
      if (pStmt.executeUpdate() != 1) {
        throw new SQLException("SQL DELETE failed in removeRow");
      }
    } catch (SQLException e) {
      throw new SQLException("SQL Exception in removeRow:" + e.getMessage());
    } finally {
      try {
        if (pStmt != null)
          pStmt.close();
        closeDBConnection();
      } catch (SQLException e) {
        TestUtil.logErr("SQLException occurred in removeRow", e);
      }
    }
  } /* removeRow */

}
