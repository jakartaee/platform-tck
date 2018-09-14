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
 * @(#)AccessJSPBean.java	1.11 03/05/16
 */

package com.sun.ts.tests.interop.tx.webclient.jsp;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.ejb.ee.tx.txbean.*;

import java.util.*;
import java.rmi.RemoteException;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.transaction.*;

public class AccessJSPBean {

  private TSNamingContext nctx = null;

  private Properties testProps = null;

  // The TxBean variables
  private static final String[] beanName = { "java:comp/env/ejb/TxBeanManaged",
      "java:comp/env/ejb/TxNotSupported", "java:comp/env/ejb/TxSupports",
      "java:comp/env/ejb/TxRequired", "java:comp/env/ejb/TxRequiresNew",
      "java:comp/env/ejb/TxMandatory", "java:comp/env/ejb/TxNever" };

  private TxBeanHome[] beanHome = new TxBeanHome[7];

  private TxBean beanRef = null;

  private UserTransaction ut = null;

  private String tName1 = null;

  private String tName2 = null;

  private Integer tSize = null;

  private Integer fromKey1 = null;

  private Integer fromKey2 = null;

  private Integer toKey2 = null;

  public AccessJSPBean() throws Exception {
    try {
      TestUtil.logMsg("Getting Naming Context");
      nctx = new TSNamingContext();

      // Looking up home interfaces
      for (int i = 0; i < 7; i++) {
        TestUtil
            .logMsg("Looking up the TxBean Home interface of " + beanName[i]);
        beanHome[i] = (TxBeanHome) nctx.lookup(beanName[i], TxBeanHome.class);
      }

      // Get the table sizes
      this.tSize = (Integer) nctx.lookup("java:comp/env/size");
      TestUtil.logTrace("tSize: " + this.tSize);

      this.fromKey1 = (Integer) nctx.lookup("java:comp/env/fromKey1");
      TestUtil.logTrace("fromKey1: " + this.fromKey1);

      this.fromKey2 = (Integer) nctx.lookup("java:comp/env/fromKey2");
      TestUtil.logTrace("fromKey2: " + this.fromKey2);

      this.toKey2 = (Integer) nctx.lookup("java:comp/env/toKey2");
      TestUtil.logTrace("toKey2: " + this.toKey2);

    } catch (Exception e) {
      TestUtil.logErr("Create exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    }

  }

  public Properties doTest(Properties p) {

    boolean pass = false;

    Properties resultProps = new Properties();

    TestUtil.logTrace("Doing test...");

    try {
      if (p != null) {
        testProps = p;
        TestUtil.init(p);

        // Get the table names
        this.tName1 = TestUtil
            .getTableName(TestUtil.getProperty("TxBean_Tab1_Delete"));
        TestUtil.logTrace("tName1: " + this.tName1);

        this.tName2 = TestUtil
            .getTableName(TestUtil.getProperty("TxBean_Tab2_Delete"));
        TestUtil.logTrace("tName2: " + this.tName2);
      }

      TestUtil.logTrace("Getting the UserTransaction interface");
      ut = (UserTransaction) nctx.lookup("java:comp/UserTransaction",
          UserTransaction.class);

      if (p.getProperty("TESTNAME").equals("test1")) {
        TestUtil.logMsg("Starting test1...");
        pass = test1();
        resultProps.setProperty("RESULT", "" + pass);
      }

      else if (p.getProperty("TESTNAME").equals("test2")) {
        TestUtil.logMsg("Starting test2...");
        pass = test2();
        resultProps.setProperty("RESULT", "" + pass);
      }

      else if (p.getProperty("TESTNAME").equals("test3")) {
        TestUtil.logMsg("Starting test 3...");
        pass = test3();
        resultProps.setProperty("RESULT", "" + pass);
      }

      else if (p.getProperty("TESTNAME").equals("test4")) {
        TestUtil.logMsg("Starting test 4...");
        pass = test4();
        resultProps.setProperty("RESULT", "" + pass);
      }

      else if (p.getProperty("TESTNAME").equals("test5")) {
        TestUtil.logMsg("Starting test 5...");
        pass = test5();
        resultProps.setProperty("RESULT", "" + pass);
      }

      else if (p.getProperty("TESTNAME").equals("test6")) {
        TestUtil.logMsg("Starting test 6...");
        pass = test6();
        resultProps.setProperty("RESULT", "" + pass);
      }

      else if (p.getProperty("TESTNAME").equals("test7")) {
        TestUtil.logMsg("Starting test 7...");
        pass = test7();
        resultProps.setProperty("RESULT", "" + pass);
      }

      else if (p.getProperty("TESTNAME").equals("test8")) {
        TestUtil.logMsg("Starting test 8...");
        pass = test8();
        resultProps.setProperty("RESULT", "" + pass);
      }

      else if (p.getProperty("TESTNAME").equals("test9")) {
        TestUtil.logMsg("Starting test 9...");
        pass = test9();
        resultProps.setProperty("RESULT", "" + pass);
      }

      else if (p.getProperty("TESTNAME").equals("test10")) {
        TestUtil.logMsg("Starting test 10...");
        pass = test10();
        resultProps.setProperty("RESULT", "" + pass);
      }

      else if (p.getProperty("TESTNAME").equals("test11")) {
        TestUtil.logMsg("Starting test 11...");
        pass = test11();
        resultProps.setProperty("RESULT", "" + pass);
      }

      else if (p.getProperty("TESTNAME").equals("test12")) {
        TestUtil.logMsg("Starting test 12...");
        pass = test12();
        resultProps.setProperty("RESULT", "" + pass);
      }

      else if (p.getProperty("TESTNAME").equals("test13")) {
        TestUtil.logMsg("Starting test 13...");
        pass = test13();
        resultProps.setProperty("RESULT", "" + pass);
      }
    } catch (Exception e) {
      TestUtil.logErr("AccessJSPBean: Exception occurred - " + e, e);
      pass = false;
      resultProps.setProperty("RESULT", "" + pass);
    }

    return resultProps;
  }

  // actual tests
  public boolean test1() throws RemoteException {
    TestUtil.logTrace("test1");
    TestUtil.logTrace("Insert/Delete followed by a commit to a single table");

    Vector dbResults = new Vector();
    boolean testResult = false;
    boolean b1, b2;
    b1 = b2 = false;
    String tName = this.tName1;
    int size = this.tSize.intValue();
    int tRng = this.fromKey1.intValue();

    try {
      TestUtil.logTrace("Creating EJB instance of " + beanName[0]);
      beanRef = (TxBean) beanHome[0].create();

      TestUtil.logTrace("Logging data from server");
      beanRef.initLogging(testProps);

      TestUtil.logTrace("Creating the table");
      ut.begin();
      beanRef.createData(tName);
      ut.commit();

      TestUtil.logTrace("Insert and delete some rows");
      ut.begin();
      TestUtil.logTrace("Inserting 2 new rows");
      if (beanRef.insert(tName, size + 1))
        size++;
      if (beanRef.insert(tName, size + 1))
        size++;
      TestUtil.logTrace("Deleting a row");
      beanRef.delete(tName, tRng, tRng);
      ut.commit();

      TestUtil.logTrace("Get test results");
      dbResults = beanRef.getResults(tName);

      TestUtil.logTrace("Verifying the test results");
      if (!dbResults.contains(new Integer(tRng)))
        b1 = true;

      for (int i = 1; i <= size; i++) {
        if (i == tRng)
          continue;
        else {
          if (dbResults.contains(new Integer(i)))
            b2 = true;
          else {
            b2 = false;
            break;
          }
        }
      }

      if (b1 && b2)
        testResult = true;

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    } finally {
      // cleanup the bean
      try {
        beanRef.destroyData(tName);
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    return testResult;
  }

  public boolean test2() throws RemoteException {
    TestUtil.logTrace("test2");
    TestUtil.logTrace("Insert/Delete followed by a rollback to a single table");

    Vector dbResults = new Vector();
    boolean testResult = false;
    boolean b1, b2;
    b1 = b2 = false;
    String tName = this.tName2;
    int size = this.tSize.intValue();
    int sizeOrig = this.tSize.intValue();
    int tRngFrom = this.fromKey2.intValue();
    int tRngTo = this.toKey2.intValue();

    try {
      TestUtil.logTrace("Creating EJB instance of " + beanName[0]);
      beanRef = (TxBean) beanHome[0].create();

      TestUtil.logTrace("Logging data from server");
      beanRef.initLogging(testProps);

      TestUtil.logTrace("Creating the table");
      ut.begin();
      beanRef.createData(tName);
      ut.commit();

      TestUtil.logTrace("Insert and delete some rows");
      ut.begin();
      TestUtil.logTrace("Inserting 2 new rows");
      if (beanRef.insert(tName, size + 1))
        size++;
      if (beanRef.insert(tName, size + 1))
        size++;
      TestUtil.logTrace("Deleting a row");
      beanRef.delete(tName, tRngFrom, tRngTo);
      ut.rollback();

      TestUtil.logTrace("Get test results");
      dbResults = beanRef.getResults(tName);

      TestUtil.logTrace("Verifying the test results");
      for (int i = tRngFrom; i <= tRngTo; i++) {
        if (!dbResults.contains(new Integer(i))) {
          b1 = true;
        } else {
          b1 = false;
          break;
        }
      }

      for (int i = 1; i <= size; i++) {
        if (i >= tRngFrom && i <= tRngTo)
          continue;
        else {
          if (dbResults.contains(new Integer(i))) {
            b2 = true;
          } else {
            b2 = false;
            break;
          }
        }
      }

      if (b1 && b2)
        testResult = true;

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    } finally {
      // cleanup the bean
      try {
        beanRef.destroyData(tName);
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    return testResult;
  }

  public boolean test3() throws RemoteException {
    TestUtil.logTrace("test3");
    TestUtil.logTrace("Insert/Delete followed by a commit to a single table");

    Vector dbResults = new Vector();
    boolean testResult = false;
    boolean b1, b2, b3;
    b1 = b2 = b3 = false;
    String tName = this.tName1;
    int size = this.tSize.intValue();
    int tRng = this.fromKey1.intValue();

    try {
      TestUtil.logTrace("Creating EJB instance of " + beanName[1]);
      beanRef = (TxBean) beanHome[1].create();

      TestUtil.logTrace("Logging data from server");
      beanRef.initLogging(testProps);

      TestUtil.logTrace("Creating the table");
      ut.begin();
      beanRef.createData(tName);

      TestUtil.logTrace("Insert and delete some rows");
      TestUtil.logTrace("Inserting 2 new rows");
      if (beanRef.insert(tName, size + 1))
        size++;
      if (beanRef.insert(tName, size + 1))
        size++;
      TestUtil.logTrace("Deleting a row");
      beanRef.delete(tName, tRng, tRng);

      TestUtil.logTrace("Get test results");
      dbResults = beanRef.getResults(tName);

      TestUtil.logTrace("Verifying the test results");
      if (!dbResults.contains(new Integer(tRng)))
        b1 = true;

      for (int i = 1; i <= size; i++) {
        if (i == tRng)
          continue;
        else {
          if (dbResults.contains(new Integer(i)))
            b2 = true;
          else {
            b2 = false;
            break;
          }
        }
      }
      beanRef.destroyData(tName);
      ut.commit();

      dbResults = beanRef.getResults(tName);
      if (dbResults.isEmpty())
        b3 = true;
      else
        TestUtil.logMsg("Error:dbResults size should be 0 but returned:"
            + dbResults.size());

      /*
       * try { dbResults = beanRef.getResults(tName); } catch
       * (java.rmi.RemoteException re) { // Table or view should not exist
       * TestUtil.logTrace("Caught expected RemoteException"); b3 = true; }
       */

      if (b1 && b2 && b3)
        testResult = true;

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    } finally {
      // cleanup the bean
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    return testResult;
  }

  public boolean test4() throws RemoteException {
    TestUtil.logTrace("test4");
    TestUtil.logTrace("Insert/Delete followed by a rollback to a single table");

    Vector dbResults = new Vector();
    boolean testResult = false;
    boolean b1, b2, b3;
    b1 = b2 = b3 = false;
    String tName = this.tName2;
    int size = this.tSize.intValue();
    int tRng = this.fromKey1.intValue();

    try {
      TestUtil.logTrace("Creating EJB instance of " + beanName[1]);
      beanRef = (TxBean) beanHome[1].create();

      TestUtil.logTrace("Logging data from server");
      beanRef.initLogging(testProps);

      TestUtil.logTrace("Creating the table");
      ut.begin();
      beanRef.createData(tName);

      TestUtil.logTrace("Insert and delete some rows");
      TestUtil.logTrace("Inserting 2 new rows");
      if (beanRef.insert(tName, size + 1))
        size++;
      if (beanRef.insert(tName, size + 1))
        size++;
      TestUtil.logTrace("Deleting a row");
      beanRef.delete(tName, tRng, tRng);

      TestUtil.logTrace("Get test results");
      dbResults = beanRef.getResults(tName);

      TestUtil.logTrace("Verifying the test results");
      if (!dbResults.contains(new Integer(tRng)))
        b1 = true;

      for (int i = 1; i <= size; i++) {
        if (i == tRng)
          continue;
        else {
          if (dbResults.contains(new Integer(i)))
            b2 = true;
          else {
            b2 = false;
            break;
          }
        }
      }
      beanRef.destroyData(tName);
      ut.rollback();

      dbResults = beanRef.getResults(tName);
      if (dbResults.isEmpty())
        b3 = true;
      else
        TestUtil.logMsg("Error:dbResults size should be 0 but returned:"
            + dbResults.size());

      /*
       * try { dbResults = beanRef.getResults(tName); } catch
       * (java.rmi.RemoteException re) { // Table or view should not exist
       * TestUtil.logTrace("Caught expected RemoteException"); b3 = true; }
       */

      if (b1 && b2 && b3)
        testResult = true;

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    } finally {
      // cleanup the bean
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    return testResult;
  }

  public boolean test5() throws RemoteException {
    TestUtil.logTrace("test5");
    TestUtil.logTrace("Insert/Delete followed by a commit to a single table");

    Vector dbResults = new Vector();
    boolean testResult = false;
    boolean b1, b2;
    b1 = b2 = false;
    String tName = this.tName1;
    int size = this.tSize.intValue();
    int tRng = this.fromKey1.intValue();

    try {
      TestUtil.logTrace("Creating EJB instance of " + beanName[2]);
      beanRef = (TxBean) beanHome[2].create();

      TestUtil.logTrace("Logging data from server");
      beanRef.initLogging(testProps);

      boolean EJBServer1TxInteropEnabled = new Boolean(
          testProps.getProperty("EJBServer1TxInteropEnabled")).booleanValue();
      boolean EJBServer2TxInteropEnabled = new Boolean(
          testProps.getProperty("EJBServer2TxInteropEnabled")).booleanValue();

      TestUtil.logTrace(
          "EJBServer1TxInteropEnabled = " + EJBServer1TxInteropEnabled);
      TestUtil.logTrace(
          "EJBServer2TxInteropEnabled = " + EJBServer2TxInteropEnabled);

      if (!EJBServer1TxInteropEnabled || !EJBServer2TxInteropEnabled) {
        try {
          ut.begin();
          int level = beanRef.getDefaultTxIsolationLevel(tName);
          TestUtil.logErr("Did not get expected RemoteException");
          testResult = false;
        } catch (RemoteException e) {
          TestUtil.logTrace("Caught expected RemoteException");
          testResult = true;
        } catch (Exception e) {
          TestUtil.printStackTrace(e);
        } finally {
          try {
            ut.commit();
          } catch (Exception e) {
            TestUtil.printStackTrace(e);
          }
        }
      } else {

        TestUtil.logTrace("Creating the table");
        ut.begin();
        beanRef.createData(tName);
        ut.commit();

        TestUtil.logTrace("Insert and delete some rows");
        ut.begin();
        TestUtil.logTrace("Inserting 2 new rows");
        if (beanRef.insert(tName, size + 1))
          size++;
        if (beanRef.insert(tName, size + 1))
          size++;
        TestUtil.logTrace("Deleting a row");
        beanRef.delete(tName, tRng, tRng);
        ut.commit();

        TestUtil.logTrace("Get test results");
        dbResults = beanRef.getResults(tName);

        TestUtil.logTrace("Verifying the test results");
        if (!dbResults.contains(new Integer(tRng)))
          b1 = true;

        for (int i = 1; i <= size; i++) {
          if (i == tRng)
            continue;
          else {
            if (dbResults.contains(new Integer(i)))
              b2 = true;
            else {
              b2 = false;
              break;
            }
          }
        }

        if (b1 && b2)
          testResult = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    } finally {
      // cleanup the bean
      try {
        beanRef.destroyData(tName);
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    return testResult;
  }

  public boolean test6() throws RemoteException {
    TestUtil.logTrace("test6");
    TestUtil.logTrace("Insert/Delete followed by a rollback to a single table");

    Vector dbResults = new Vector();
    boolean testResult = false;
    boolean b1, b2;
    b1 = b2 = false;
    String tName = this.tName1;
    int size = this.tSize.intValue();
    int sizeOrig = this.tSize.intValue();
    int tRngFrom = this.fromKey2.intValue();
    int tRngTo = this.toKey2.intValue();

    try {
      TestUtil.logTrace("Creating EJB instance of " + beanName[2]);
      beanRef = (TxBean) beanHome[2].create();

      TestUtil.logTrace("Logging data from server");
      beanRef.initLogging(testProps);

      boolean EJBServer1TxInteropEnabled = new Boolean(
          testProps.getProperty("EJBServer1TxInteropEnabled")).booleanValue();
      boolean EJBServer2TxInteropEnabled = new Boolean(
          testProps.getProperty("EJBServer2TxInteropEnabled")).booleanValue();

      TestUtil.logTrace(
          "EJBServer1TxInteropEnabled = " + EJBServer1TxInteropEnabled);
      TestUtil.logTrace(
          "EJBServer2TxInteropEnabled = " + EJBServer2TxInteropEnabled);

      if (!EJBServer1TxInteropEnabled || !EJBServer2TxInteropEnabled) {
        try {
          ut.begin();
          int level = beanRef.getDefaultTxIsolationLevel(tName);
          TestUtil.logErr("Did not get expected RemoteException");
          testResult = false;
        } catch (RemoteException e) {
          TestUtil.logTrace("Caught expected RemoteException");
          testResult = true;
        } catch (Exception e) {
          TestUtil.printStackTrace(e);
        } finally {
          try {
            ut.commit();
          } catch (Exception e) {
            TestUtil.printStackTrace(e);
          }
        }
      } else {

        TestUtil.logTrace("Creating the table");
        ut.begin();
        beanRef.createData(tName);
        ut.commit();

        TestUtil.logTrace("Insert and delete some rows");
        ut.begin();
        TestUtil.logTrace("Inserting 2 new rows");
        if (beanRef.insert(tName, size + 1))
          size++;
        if (beanRef.insert(tName, size + 1))
          size++;
        TestUtil.logTrace("Deleting a row");
        beanRef.delete(tName, tRngFrom, tRngTo);
        ut.rollback();

        TestUtil.logTrace("Get test results");
        dbResults = beanRef.getResults(tName);

        TestUtil.logTrace("Verifying the test results");
        for (int i = 1; i <= sizeOrig; i++) {
          if (dbResults.contains(new Integer(i))) {
            b1 = true;
          } else {
            b1 = false;
            break;
          }
        }
        for (int j = size; j > sizeOrig; j--) {
          if (dbResults.contains(new Integer(j))) {
            b2 = false;
            break;
          } else {
            b2 = true;
          }
        }

        if (b1)
          TestUtil.logTrace("b1 true");
        if (b2)
          TestUtil.logTrace("b2 true");

        if (b1 && b2)
          testResult = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    } finally {
      // cleanup the bean
      try {
        beanRef.destroyData(tName);
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    return testResult;
  }

  public boolean test7() throws RemoteException {
    TestUtil.logTrace("test7");
    TestUtil.logTrace("Insert/Delete followed by a commit to a single table");

    Vector dbResults = new Vector();
    boolean testResult = false;
    boolean b1, b2;
    b1 = b2 = false;
    String tName = this.tName1;
    int size = this.tSize.intValue();
    int tRng = this.fromKey1.intValue();

    try {
      TestUtil.logTrace("Creating EJB instance of " + beanName[3]);
      beanRef = (TxBean) beanHome[3].create();

      TestUtil.logTrace("Logging data from server");
      beanRef.initLogging(testProps);

      boolean EJBServer1TxInteropEnabled = new Boolean(
          testProps.getProperty("EJBServer1TxInteropEnabled")).booleanValue();
      boolean EJBServer2TxInteropEnabled = new Boolean(
          testProps.getProperty("EJBServer2TxInteropEnabled")).booleanValue();

      TestUtil.logTrace(
          "EJBServer1TxInteropEnabled = " + EJBServer1TxInteropEnabled);
      TestUtil.logTrace(
          "EJBServer2TxInteropEnabled = " + EJBServer2TxInteropEnabled);

      if (!EJBServer1TxInteropEnabled || !EJBServer2TxInteropEnabled) {
        try {
          ut.begin();
          int level = beanRef.getDefaultTxIsolationLevel(tName);
          TestUtil.logErr("Did not get expected RemoteException");
          testResult = false;
        } catch (RemoteException e) {
          TestUtil.logTrace("Caught expected RemoteException");
          testResult = true;
        } catch (Exception e) {
          TestUtil.printStackTrace(e);
          ;
        } finally {
          try {
            ut.commit();
          } catch (Exception e) {
            TestUtil.printStackTrace(e);
          }
        }
      } else {

        TestUtil.logTrace("Creating the table");
        ut.begin();
        beanRef.createData(tName);
        ut.commit();

        TestUtil.logTrace("Insert and delete some rows");
        ut.begin();
        TestUtil.logTrace("Inserting 2 new rows");
        if (beanRef.insert(tName, size + 1))
          size++;
        if (beanRef.insert(tName, size + 1))
          size++;
        TestUtil.logTrace("Deleting a row");
        beanRef.delete(tName, tRng, tRng);
        ut.commit();

        TestUtil.logTrace("Get test results");
        dbResults = beanRef.getResults(tName);

        TestUtil.logTrace("Verifying the test results");
        if (!dbResults.contains(new Integer(tRng)))
          b1 = true;

        for (int i = 1; i <= size; i++) {
          if (i == tRng)
            continue;
          else {
            if (dbResults.contains(new Integer(i)))
              b2 = true;
            else {
              b2 = false;
              break;
            }
          }
        }

        if (b1 && b2)
          testResult = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    } finally {
      // cleanup the bean
      try {
        beanRef.destroyData(tName);
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    return testResult;
  }

  public boolean test8() throws RemoteException {
    TestUtil.logTrace("test8");
    TestUtil.logTrace("Insert/Delete followed by a rollback to a single table");

    Vector dbResults = new Vector();
    boolean testResult = false;
    boolean b1, b2;
    b1 = b2 = false;
    String tName = this.tName2;
    int size = this.tSize.intValue();
    int sizeOrig = this.tSize.intValue();
    int tRngFrom = this.fromKey2.intValue();
    int tRngTo = this.toKey2.intValue();

    try {
      TestUtil.logTrace("Creating EJB instance of " + beanName[3]);
      beanRef = (TxBean) beanHome[3].create();

      TestUtil.logTrace("Logging data from server");
      beanRef.initLogging(testProps);

      boolean EJBServer1TxInteropEnabled = new Boolean(
          testProps.getProperty("EJBServer1TxInteropEnabled")).booleanValue();
      boolean EJBServer2TxInteropEnabled = new Boolean(
          testProps.getProperty("EJBServer2TxInteropEnabled")).booleanValue();

      TestUtil.logTrace(
          "EJBServer1TxInteropEnabled = " + EJBServer1TxInteropEnabled);
      TestUtil.logTrace(
          "EJBServer2TxInteropEnabled = " + EJBServer2TxInteropEnabled);

      if (!EJBServer1TxInteropEnabled || !EJBServer2TxInteropEnabled) {
        try {
          ut.begin();
          int level = beanRef.getDefaultTxIsolationLevel(tName);
          TestUtil.logErr("Did not get expected RemoteException");
          testResult = false;
        } catch (RemoteException e) {
          TestUtil.logTrace("Caught expected RemoteException");
          testResult = true;
        } catch (Exception e) {
          TestUtil.printStackTrace(e);
        } finally {
          try {
            ut.commit();
          } catch (Exception e) {
            TestUtil.printStackTrace(e);
          }
        }
      } else {

        TestUtil.logTrace("Creating the table");
        ut.begin();
        beanRef.createData(tName);
        ut.commit();

        TestUtil.logTrace("Insert and delete some rows");
        ut.begin();
        TestUtil.logTrace("Inserting 2 new rows");
        if (beanRef.insert(tName, size + 1))
          size++;
        if (beanRef.insert(tName, size + 1))
          size++;
        TestUtil.logTrace("Deleting a row");
        beanRef.delete(tName, tRngFrom, tRngTo);
        ut.rollback();

        TestUtil.logTrace("Get test results");
        dbResults = beanRef.getResults(tName);

        TestUtil.logTrace("Verifying the test results");
        for (int i = 1; i <= sizeOrig; i++) {
          if (dbResults.contains(new Integer(i))) {
            b1 = true;
          } else {
            b1 = false;
            break;
          }
        }
        for (int j = size; j > sizeOrig; j--) {
          if (dbResults.contains(new Integer(j))) {
            b2 = false;
            break;
          } else {
            b2 = true;
          }
        }

        if (b1)
          TestUtil.logTrace("b1 true");
        if (b2)
          TestUtil.logTrace("b2 true");

        if (b1 && b2)
          testResult = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    } finally {
      // cleanup the bean
      try {
        beanRef.destroyData(tName);
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    return testResult;
  }

  public boolean test9() throws RemoteException {
    TestUtil.logTrace("test9");
    TestUtil.logTrace("Insert/Delete followed by a commit to a single table");

    Vector dbResults = new Vector();
    boolean testResult = false;
    boolean b1, b2, b3;
    b1 = b2 = b3 = false;
    String tName = this.tName1;
    int size = this.tSize.intValue();
    int tRng = this.fromKey1.intValue();

    try {
      TestUtil.logTrace("Creating EJB instance of " + beanName[4]);
      beanRef = (TxBean) beanHome[4].create();

      TestUtil.logTrace("Logging data from server");
      beanRef.initLogging(testProps);

      TestUtil.logTrace("Creating the table");
      ut.begin();
      beanRef.createData(tName);

      TestUtil.logTrace("Insert and delete some rows");
      TestUtil.logTrace("Inserting 2 new rows");
      if (beanRef.insert(tName, size + 1))
        size++;
      if (beanRef.insert(tName, size + 1))
        size++;
      TestUtil.logTrace("Deleting a row");
      beanRef.delete(tName, tRng, tRng);

      TestUtil.logTrace("Get test results");
      dbResults = beanRef.getResults(tName);

      TestUtil.logTrace("Verifying the test results");
      if (!dbResults.contains(new Integer(tRng)))
        b1 = true;

      for (int i = 1; i <= size; i++) {
        if (i == tRng)
          continue;
        else {
          if (dbResults.contains(new Integer(i)))
            b2 = true;
          else {
            b2 = false;
            break;
          }
        }
      }
      beanRef.destroyData(tName);
      ut.commit();

      dbResults = beanRef.getResults(tName);
      if (dbResults.isEmpty())
        b3 = true;
      else
        TestUtil.logMsg("Error:dbResults size should be 0 but returned:"
            + dbResults.size());

      /*
       * try { dbResults = beanRef.getResults(tName); } catch
       * (java.rmi.RemoteException re) { // Table or view should not exist
       * TestUtil.logTrace("Caught expected RemoteException"); b3 = true; }
       */

      if (b1 && b2 && b3)
        testResult = true;

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    } finally {
      // cleanup the bean
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    return testResult;
  }

  public boolean test10() throws RemoteException {
    TestUtil.logTrace("test10");
    TestUtil.logTrace("Insert/Delete followed by a rollback to a single table");

    Vector dbResults = new Vector();
    boolean testResult = false;
    boolean b1, b2, b3;
    b1 = b2 = b3 = false;
    String tName = this.tName1;
    int size = this.tSize.intValue();
    int tRng = this.fromKey1.intValue();

    try {
      TestUtil.logTrace("Creating EJB instance of " + beanName[4]);
      beanRef = (TxBean) beanHome[4].create();

      TestUtil.logTrace("Logging data from server");
      beanRef.initLogging(testProps);

      TestUtil.logTrace("Creating the table");
      ut.begin();
      beanRef.createData(tName);

      TestUtil.logTrace("Insert and delete some rows");
      TestUtil.logTrace("Inserting 2 new rows");
      if (beanRef.insert(tName, size + 1))
        size++;
      if (beanRef.insert(tName, size + 1))
        size++;
      TestUtil.logTrace("Deleting a row");
      beanRef.delete(tName, tRng, tRng);

      TestUtil.logTrace("Get test results");
      dbResults = beanRef.getResults(tName);

      TestUtil.logTrace("Verifying the test results");
      if (!dbResults.contains(new Integer(tRng)))
        b1 = true;

      for (int i = 1; i <= size; i++) {
        if (i == tRng)
          continue;
        else {
          if (dbResults.contains(new Integer(i)))
            b2 = true;
          else {
            b2 = false;
            break;
          }
        }
      }
      beanRef.destroyData(tName);
      ut.rollback();
      // See exception tests for RequiresNew auto-rollback.

      dbResults = beanRef.getResults(tName);
      if (dbResults.isEmpty())
        b3 = true;
      else
        TestUtil.logMsg("Error:dbResults size should be 0 but returned:"
            + dbResults.size());

      /*
       * try { dbResults = beanRef.getResults(tName); } catch
       * (java.rmi.RemoteException re) { // Table or view should not exist
       * TestUtil.logTrace("Caught expected RemoteException"); b3 = true; }
       */

      if (b1 && b2 && b3)
        testResult = true;

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    } finally {
      // cleanup the bean
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    return testResult;
  }

  public boolean test11() throws RemoteException {
    TestUtil.logTrace("test11");
    TestUtil.logTrace("Insert/Delete followed by a commit to a single table");

    Vector dbResults = new Vector();
    boolean testResult = false;
    boolean b1, b2;
    b1 = b2 = false;
    String tName = this.tName1;
    int size = this.tSize.intValue();
    int tRng = this.fromKey1.intValue();

    try {
      TestUtil.logTrace("Creating EJB instance of " + beanName[5]);
      beanRef = (TxBean) beanHome[5].create();

      TestUtil.logTrace("Logging data from server");
      beanRef.initLogging(testProps);

      boolean EJBServer1TxInteropEnabled = new Boolean(
          testProps.getProperty("EJBServer1TxInteropEnabled")).booleanValue();
      boolean EJBServer2TxInteropEnabled = new Boolean(
          testProps.getProperty("EJBServer2TxInteropEnabled")).booleanValue();

      TestUtil.logTrace(
          "EJBServer1TxInteropEnabled = " + EJBServer1TxInteropEnabled);
      TestUtil.logTrace(
          "EJBServer2TxInteropEnabled = " + EJBServer2TxInteropEnabled);

      if (!EJBServer1TxInteropEnabled || !EJBServer2TxInteropEnabled) {
        try {
          ut.begin();
          int level = beanRef.getDefaultTxIsolationLevel(tName);
          TestUtil.logErr("Did not get expected RemoteException");
          testResult = false;
        } catch (RemoteException e) {
          TestUtil.logTrace("Caught expected RemoteException");
          testResult = true;
        } catch (Exception e) {
          TestUtil.printStackTrace(e);
        } finally {
          try {
            ut.commit();
          } catch (Exception e) {
            TestUtil.printStackTrace(e);
          }
        }
      } else {

        TestUtil.logTrace("Creating the table");
        ut.begin();
        beanRef.createData(tName);
        ut.commit();

        TestUtil.logTrace("Insert and delete some rows");
        ut.begin();
        TestUtil.logTrace("Inserting 2 new rows");
        if (beanRef.insert(tName, size + 1))
          size++;
        if (beanRef.insert(tName, size + 1))
          size++;
        TestUtil.logTrace("Deleting a row");
        beanRef.delete(tName, tRng, tRng);
        ut.commit();

        TestUtil.logTrace("Get test results");
        ut.begin();
        dbResults = beanRef.getResults(tName);

        TestUtil.logTrace("Verifying the test results");
        if (!dbResults.contains(new Integer(tRng)))
          b1 = true;

        for (int i = 1; i <= size; i++) {
          if (i == tRng)
            continue;
          else {
            if (dbResults.contains(new Integer(i)))
              b2 = true;
            else {
              b2 = false;
              break;
            }
          }
        }
        ut.commit();

        if (b1 && b2)
          testResult = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    } finally {
      // cleanup the bean
      try {
        ut.begin();
        ut.commit();
        beanRef.destroyData(tName);
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    return testResult;
  }

  public boolean test12() throws RemoteException {
    TestUtil.logTrace("test2");
    TestUtil.logTrace("Insert/Delete followed by a rollback to a single table");

    Vector dbResults = new Vector();
    boolean testResult = false;
    boolean b1, b2;
    b1 = b2 = false;
    String tName = this.tName2;
    int size = this.tSize.intValue();
    int sizeOrig = this.tSize.intValue();
    int tRngFrom = this.fromKey2.intValue();
    int tRngTo = this.toKey2.intValue();

    try {
      TestUtil.logTrace("Creating EJB instance of " + beanName[5]);
      beanRef = (TxBean) beanHome[5].create();

      TestUtil.logTrace("Logging data from server");
      beanRef.initLogging(testProps);

      boolean EJBServer1TxInteropEnabled = new Boolean(
          testProps.getProperty("EJBServer1TxInteropEnabled")).booleanValue();
      boolean EJBServer2TxInteropEnabled = new Boolean(
          testProps.getProperty("EJBServer2TxInteropEnabled")).booleanValue();

      TestUtil.logTrace(
          "EJBServer1TxInteropEnabled = " + EJBServer1TxInteropEnabled);
      TestUtil.logTrace(
          "EJBServer2TxInteropEnabled = " + EJBServer2TxInteropEnabled);

      if (!EJBServer1TxInteropEnabled || !EJBServer2TxInteropEnabled) {
        try {
          ut.begin();
          int level = beanRef.getDefaultTxIsolationLevel(tName);
          TestUtil.logErr("Did not get expected RemoteException");
          testResult = false;
        } catch (RemoteException e) {
          TestUtil.logTrace("Caught expected RemoteException");
          testResult = true;
        } catch (Exception e) {
          TestUtil.printStackTrace(e);
        } finally {
          try {
            ut.commit();
          } catch (Exception e) {
            TestUtil.printStackTrace(e);
          }
        }
      } else {

        TestUtil.logTrace("Creating the table");
        ut.begin();
        beanRef.createData(tName);
        ut.commit();

        TestUtil.logTrace("Insert and delete some rows");
        ut.begin();
        TestUtil.logTrace("Inserting 2 new rows");
        if (beanRef.insert(tName, size + 1))
          size++;
        if (beanRef.insert(tName, size + 1))
          size++;
        TestUtil.logTrace("Deleting a row");
        beanRef.delete(tName, tRngFrom, tRngTo);
        ut.rollback();

        TestUtil.logTrace("Get test results");
        ut.begin();
        dbResults = beanRef.getResults(tName);

        TestUtil.logTrace("Verifying the test results");
        for (int i = 1; i <= sizeOrig; i++) {
          if (dbResults.contains(new Integer(i))) {
            b1 = true;
          } else {
            b1 = false;
            break;
          }
        }
        for (int j = size; j > sizeOrig; j--) {
          if (dbResults.contains(new Integer(j))) {
            b2 = false;
            break;
          } else {
            b2 = true;
          }
        }
        ut.commit();

        if (b1)
          TestUtil.logTrace("b1 true");
        if (b2)
          TestUtil.logTrace("b2 true");

        if (b1 && b2)
          testResult = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    } finally {
      // cleanup the bean
      try {
        beanRef.destroyData(tName);
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    return testResult;
  }

  public boolean test13() throws RemoteException {
    TestUtil.logTrace("test13");
    TestUtil.logTrace("Ensure java.rmi.RemoteException is thrown for TX_NEVER");

    Vector dbResults = new Vector();
    boolean testResult = false;
    String tName = this.tName1;
    int size = this.tSize.intValue();
    int tRng = this.fromKey1.intValue();

    try {
      TestUtil.logTrace("Creating EJB instance of " + beanName[6]);
      beanRef = (TxBean) beanHome[6].create();

      TestUtil.logTrace("Logging data from server");
      beanRef.initLogging(testProps);

      TestUtil.logTrace("BEGIN transaction");
      ut.begin();

      int level = beanRef.getDefaultTxIsolationLevel(tName);

      TestUtil.logErr("Did not get expected RemoteException");
      testResult = false;
    } catch (java.rmi.RemoteException re) {
      TestUtil.logTrace("Got expected RemoteException");
      testResult = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    } finally {
      // cleanup the bean
      try {
        ut.commit();
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    return testResult;
  }

}
