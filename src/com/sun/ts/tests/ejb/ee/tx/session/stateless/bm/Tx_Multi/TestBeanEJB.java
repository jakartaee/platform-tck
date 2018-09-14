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
 * @(#)TestBeanEJB.java	1.18 03/05/16
 */

package com.sun.ts.tests.ejb.ee.tx.session.stateless.bm.Tx_Multi;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.ejb.ee.tx.txbean.*;

import java.util.*;
import java.rmi.*;
import javax.ejb.*;
import javax.transaction.*;

public class TestBeanEJB implements SessionBean {

  // beanProps represent the bean specific properties of the TestBean
  // testProps represent the test specific properties passed in
  // from the test harness.
  private Properties testProps = null;

  // The TSNamingContext abstracts away the underlying distribution protocol.
  private TSNamingContext jctx = null;

  private SessionContext sctx = null;

  private String tName1 = null;

  private String tName2 = null;

  private Integer tSize = null;

  private Integer fromKey1 = null;

  private Integer fromKey2 = null;

  private Integer toKey2 = null;

  // The TxBean variables
  private TxBeanHome beanHome = null;

  private TxBeanHome beanHome2 = null;

  private TxBean beanRef = null;

  private TxBean beanRef2 = null;

  // The requiredEJB methods
  public void ejbCreate() throws CreateException {
    TestUtil.logTrace("ejbCreate");
    try {
      TestUtil.logMsg("Getting Naming Context");
      jctx = new TSNamingContext();

      // Get the table sizes
      this.tSize = (Integer) jctx.lookup("java:comp/env/size");
      TestUtil.logTrace("tSize: " + this.tSize);

      this.fromKey1 = (Integer) jctx.lookup("java:comp/env/fromKey1");
      TestUtil.logTrace("fromKey1: " + this.fromKey1);

      this.fromKey2 = (Integer) jctx.lookup("java:comp/env/fromKey2");
      TestUtil.logTrace("fromKey2: " + this.fromKey2);

      this.toKey2 = (Integer) jctx.lookup("java:comp/env/toKey2");
      TestUtil.logTrace("toKey2: " + this.toKey2);

    } catch (Exception e) {
      TestUtil.logErr("Create exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    }
  }

  public void setSessionContext(SessionContext sc) {
    TestUtil.logTrace("setSessionContext");
    this.sctx = sc;
  }

  public void ejbRemove() {
    TestUtil.logTrace("ejbRemove");
  }

  public void ejbActivate() {
    TestUtil.logTrace("ejbActivate");
  }

  public void ejbPassivate() {
    TestUtil.logTrace("ejbPassivate");
  }

  // ===========================================================
  // TestBean interface (our business methods)

  public boolean test1() {
    TestUtil.logTrace("test1");
    TestUtil.logTrace("Multi table inserts and deletes - commit Tx");

    String txBeanRequired = "java:comp/env/ejb/TxRequired.STATELESS";

    Vector dbResults = new Vector();
    boolean testResult = false;
    boolean b1, b2, b3, b4;
    b1 = b2 = b3 = b4 = false;
    String tName1 = this.tName1;
    String tName2 = this.tName2;
    int size1 = this.tSize.intValue();
    int size2 = this.tSize.intValue();
    int tRng1 = this.fromKey1.intValue();
    int tRng2 = this.fromKey2.intValue();
    UserTransaction ut = null;

    try {
      TestUtil
          .logMsg("Looking up the TxBean Home interface of " + txBeanRequired);
      beanHome = (TxBeanHome) jctx.lookup(txBeanRequired, TxBeanHome.class);

      TestUtil.logTrace("Creating EJB instance of " + txBeanRequired);
      beanRef = (TxBean) beanHome.create();

      TestUtil.logTrace("Logging data from server");
      beanRef.initLogging(testProps);

      TestUtil.logTrace("Getting the UserTransaction interface");
      ut = sctx.getUserTransaction();

      TestUtil.logTrace("Creating the tables");
      ut.begin();
      beanRef.createData(tName1);
      beanRef.createData(tName2);
      ut.commit();

      TestUtil.logTrace("Insert and delete some rows");
      ut.begin();
      TestUtil.logTrace("Connecting to the resources");

      TestUtil.logTrace("Inserting rows into the tables");
      if (beanRef.insert(tName1, size1 + 1))
        size1++;
      if (beanRef.insert(tName2, size2 + 1))
        size2++;

      if (beanRef.insert(tName1, size1 + 1))
        size1++;
      if (beanRef.insert(tName2, size2 + 1))
        size2++;

      TestUtil.logTrace("Deleting rows from the tables");
      beanRef.delete(tName1, tRng1, tRng1);
      beanRef.delete(tName2, tRng2, tRng2);

      TestUtil.logTrace("Releasing the resources");
      TestUtil.logTrace("Commiting the transaction");
      ut.commit();

      TestUtil.logTrace("Get the test results/Connecting to the resources");

      dbResults = beanRef.getResults(tName1);

      TestUtil.logTrace("Verifying first set of test results");
      if (!dbResults.contains(new Integer(tRng1)))
        b1 = true;

      for (int i = 1; i <= size1; i++) {
        if (i == tRng1)
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

      dbResults = beanRef.getResults(tName2);

      TestUtil.logTrace("Verifying second set of test results");
      if (!dbResults.contains(new Integer(tRng2)))
        b3 = true;

      for (int i = 1; i <= size2; i++) {
        if (i == tRng2)
          continue;
        else {
          if (dbResults.contains(new Integer(i)))
            b4 = true;
          else {
            b4 = false;
            break;
          }
        }
      }

      TestUtil.logTrace("Releasing the resources");
      beanRef.destroyData(tName1);
      beanRef.destroyData(tName2);

      if (b1 && b2 && b3 && b4)
        testResult = true;

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    } finally {
      // cleanup the bean and destroy the tables
      try {
        beanRef.destroyData(tName1);
        beanRef.destroyData(tName2);
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
    }
    return testResult;
  }

  public boolean test2() {
    TestUtil.logTrace("test2");
    TestUtil.logTrace("Multi table inserts and deletes - rollback Tx");

    String txBeanRequired = "java:comp/env/ejb/TxRequired.STATELESS";

    Vector dbResults = new Vector();
    boolean testResult = false;
    boolean b1, b2, b3, b4;
    b1 = b2 = b3 = b4 = false;
    int size1 = this.tSize.intValue();
    int size2 = this.tSize.intValue();
    int sizeOrig1 = this.tSize.intValue();
    int sizeOrig2 = this.tSize.intValue();
    int tRng1 = this.fromKey1.intValue();
    int tRng2 = this.fromKey2.intValue();
    UserTransaction ut = null;

    try {
      TestUtil
          .logMsg("Looking up the TxBean Home interface of " + txBeanRequired);
      beanHome = (TxBeanHome) jctx.lookup(txBeanRequired, TxBeanHome.class);

      TestUtil.logTrace("Creating EJB instance of " + txBeanRequired);
      beanRef = (TxBean) beanHome.create();

      TestUtil.logTrace("Logging data from server");
      beanRef.initLogging(testProps);

      TestUtil.logTrace("Getting the UserTransaction interface");
      ut = sctx.getUserTransaction();

      TestUtil.logTrace("Creating the tables");
      ut.begin();
      beanRef.createData(tName1);
      beanRef.createData(tName2);
      ut.commit();

      TestUtil.logTrace("Insert and delete some rows");
      ut.begin();
      TestUtil.logTrace("Connecting to the resources");

      TestUtil.logTrace("Inserting rows into the tables");
      if (beanRef.insert(tName1, size1 + 1))
        size1++;
      if (beanRef.insert(tName2, size2 + 1))
        size2++;

      TestUtil.logTrace("Deleting rows from the tables");
      beanRef.delete(tName1, tRng1, tRng1);
      beanRef.delete(tName2, tRng2, tRng2);

      TestUtil.logTrace("Releasing the resources");
      TestUtil.logTrace("Rolling back the transaction");
      ut.rollback();

      TestUtil.logTrace("Get the test results/Connecting to the resources");

      dbResults = beanRef.getResults(tName1);

      TestUtil.logTrace("Verifying first set of test results");
      for (int i = 1; i <= sizeOrig1; i++) {
        if (dbResults.contains(new Integer(i)))
          b1 = true;
        else {
          b1 = false;
          break;
        }
      }

      if (!dbResults.contains(new Integer(size1)))
        b2 = true;

      dbResults = beanRef.getResults(tName2);

      TestUtil.logTrace("Verifying second set of test results");
      for (int i = 1; i <= sizeOrig2; i++) {
        if (dbResults.contains(new Integer(i)))
          b3 = true;
        else {
          b3 = false;
          break;
        }
      }

      if (!dbResults.contains(new Integer(size2)))
        b4 = true;

      TestUtil.logTrace("Releasing the resources");
      beanRef.destroyData(tName1);
      beanRef.destroyData(tName2);

      if (b1 && b2 && b3 && b4)
        testResult = true;

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    } finally {
      // cleanup the bean and destroy the tables
      try {
        beanRef.destroyData(tName1);
        beanRef.destroyData(tName2);
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    return testResult;
  }

  public boolean test4() {
    TestUtil.logTrace("test4");
    TestUtil.logTrace(
        "Multi Bean, Multi table inserts and deletes - rollback both Tx.");

    String txBeanRequired = "java:comp/env/ejb/TxRequired.STATELESS";
    String txBeanSupports = "java:comp/env/ejb/TxSupports.STATELESS";

    Vector dbResults = new Vector();
    boolean testResult = false;
    boolean b1, b2, b3, b4;
    b1 = b2 = b3 = b4 = false;
    String tName1 = this.tName1;
    String tName2 = this.tName2;
    int size1 = this.tSize.intValue();
    int size2 = this.tSize.intValue();
    int sizeOrig1 = this.tSize.intValue();
    int sizeOrig2 = this.tSize.intValue();
    int tRng1 = this.fromKey1.intValue();
    int tRng2 = this.fromKey2.intValue();
    UserTransaction ut = null;

    try {
      TestUtil
          .logMsg("Looking up the TxBean Home interface of " + txBeanRequired);
      beanHome = (TxBeanHome) jctx.lookup(txBeanRequired, TxBeanHome.class);

      TestUtil.logTrace("Creating EJB instance of " + txBeanRequired);
      beanRef = (TxBean) beanHome.create();

      TestUtil
          .logMsg("Looking up the TxBean Home interface of " + txBeanSupports);
      beanHome2 = (TxBeanHome) jctx.lookup(txBeanSupports, TxBeanHome.class);
      TestUtil.logMsg("beanHome2=" + beanHome2);

      TestUtil.logTrace("Creating EJB instance of " + txBeanSupports);
      beanRef2 = (TxBean) beanHome2.create();
      TestUtil.logTrace("beanRef2=" + beanRef2);

      TestUtil.logTrace("Logging data from server");
      beanRef.initLogging(testProps);
      beanRef2.initLogging(testProps);

      TestUtil.logTrace("Getting the UserTransaction interface");
      ut = sctx.getUserTransaction();

      TestUtil.logTrace("Creating the tables");
      ut.begin();
      beanRef.createData(tName1);
      beanRef2.createData(tName2);
      ut.commit();

      TestUtil.logTrace("Insert and delete some rows");
      ut.begin();
      TestUtil.logTrace("Connecting to the resources");

      TestUtil.logTrace("Inserting rows into the tables");
      if (beanRef.insert(tName1, size1 + 1))
        size1++;
      if (beanRef2.insert(tName2, size2 + 1))
        size2++;

      TestUtil.logTrace("Deleting rows from the tables");
      beanRef.delete(tName1, tRng1, tRng1);
      beanRef2.delete(tName2, tRng2, tRng2);

      TestUtil.logTrace("Releasing the resources");
      TestUtil.logTrace("Rolling back the transaction");
      ut.rollback();

      TestUtil.logTrace("Get the test results/Connecting to the resources");

      dbResults = beanRef.getResults(tName1);

      TestUtil.logTrace("Verifying first set of test results");
      for (int i = 1; i <= sizeOrig1; i++) {
        if (dbResults.contains(new Integer(i)))
          b1 = true;
        else {
          b1 = false;
          break;
        }
      }

      if (!dbResults.contains(new Integer(size1)))
        b2 = true;

      dbResults = beanRef2.getResults(tName2);
      // beanRef2.listTableData(dbResults);

      TestUtil.logTrace("Verifying second set of test results");
      for (int i = 1; i <= sizeOrig2; i++) {
        if (dbResults.contains(new Integer(i)))
          b3 = true;
        else {
          b3 = false;
          break;
        }
      }

      if (!dbResults.contains(new Integer(size2)))
        b4 = true;

      TestUtil.logTrace("Releasing the resources");
      beanRef.destroyData(tName1);
      beanRef2.destroyData(tName2);

      if (b1 && b2 && b3 && b4)
        testResult = true;

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    } finally {
      // cleanup the bean and destroy the tables
      try {
        beanRef.destroyData(tName1);
        beanRef2.destroyData(tName2);
        beanRef.remove();
        beanRef2.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    return testResult;
  }

  public boolean test5() {
    TestUtil.logTrace("test5");
    TestUtil.logTrace("Multi Bean, Multi table inserts and deletes - "
        + "commit one Tx, rollback the other Tx.");

    String txBeanRequired = "java:comp/env/ejb/TxRequired.STATELESS";
    String txBeanSupports = "java:comp/env/ejb/TxSupports.STATELESS";

    Vector dbResults = new Vector();
    boolean testResult = false;
    boolean b1, b2, b3, b4;
    b1 = b2 = b3 = b4 = false;
    String tName1 = this.tName1;
    String tName2 = this.tName2;
    int size1 = this.tSize.intValue();
    int size2 = this.tSize.intValue();
    int sizeOrig1 = this.tSize.intValue();
    int sizeOrig2 = this.tSize.intValue();
    int tRng1 = this.fromKey1.intValue();
    int tRng2 = this.fromKey2.intValue();
    UserTransaction ut = null;
    UserTransaction ut2 = null;

    try {
      TestUtil
          .logMsg("Looking up the TxBean Home interface of " + txBeanRequired);
      beanHome = (TxBeanHome) jctx.lookup(txBeanRequired, TxBeanHome.class);

      TestUtil.logTrace("Creating EJB instance of " + txBeanRequired);
      beanRef = (TxBean) beanHome.create();

      TestUtil
          .logMsg("Looking up the TxBean Home interface of " + txBeanSupports);
      beanHome2 = (TxBeanHome) jctx.lookup(txBeanSupports, TxBeanHome.class);
      TestUtil.logMsg("beanHome2=" + beanHome2);

      TestUtil.logTrace("Creating EJB instance of " + txBeanSupports);
      beanRef2 = (TxBean) beanHome2.create();
      TestUtil.logTrace("beanRef2=" + beanRef2);

      TestUtil.logTrace("Logging data from server");
      beanRef.initLogging(testProps);
      beanRef2.initLogging(testProps);

      TestUtil.logTrace("Getting the UserTransaction interface");
      ut = sctx.getUserTransaction();
      ut2 = sctx.getUserTransaction();

      TestUtil.logTrace("Creating the tables");
      ut.begin();
      beanRef.createData(tName1);
      ut.commit();

      ut2.begin();
      beanRef2.createData(tName2);
      ut2.commit();

      TestUtil.logTrace("Insert and delete some rows");
      ut.begin();
      TestUtil.logTrace("Connecting to the first resource");

      TestUtil.logTrace("Inserting rows into the table");
      if (beanRef.insert(tName1, size1 + 1))
        size1++;

      TestUtil.logTrace("Deleting rows from the tables");
      beanRef.delete(tName1, tRng1, tRng1);

      TestUtil.logTrace("Releasing the resource");
      TestUtil.logTrace("Commiting the transaction");
      ut.commit();

      ut2.begin();
      TestUtil.logTrace("Connecting to the second resource");

      TestUtil.logTrace("Inserting rows into the table");
      if (beanRef2.insert(tName2, size2 + 1))
        size2++;

      TestUtil.logTrace("Deleting rows from the table");
      beanRef2.delete(tName2, tRng2, tRng2);

      TestUtil.logTrace("Releasing the resource");
      TestUtil.logTrace("Rolling back the transaction");
      ut2.rollback();

      TestUtil.logTrace("Get the test results/Connecting to the resources");

      dbResults = beanRef.getResults(tName1);

      TestUtil.logTrace("Verifying first set of test results");
      if (!dbResults.contains(new Integer(tRng1)))
        b1 = true;

      for (int i = 1; i <= size1; i++) {
        if (i == tRng1)
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

      dbResults = beanRef2.getResults(tName2);
      // beanRef2.listTableData(dbResults);

      TestUtil.logTrace("Verifying second set of test results");
      for (int i = 1; i <= sizeOrig2; i++) {
        if (dbResults.contains(new Integer(i)))
          b3 = true;
        else {
          b3 = false;
          break;
        }
      }

      if (!dbResults.contains(new Integer(size2)))
        b4 = true;

      TestUtil.logTrace("Releasing the resources");
      beanRef.destroyData(tName1);
      beanRef2.destroyData(tName2);

      if (b1 && b2 && b3 && b4)
        testResult = true;

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    } finally {
      // cleanup the bean and destroy the tables
      try {
        beanRef.destroyData(tName1);
        beanRef2.destroyData(tName2);
        beanRef.remove();
        beanRef2.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
    }
    return testResult;
  }

  public void initLogging(Properties p) {
    TestUtil.logTrace("initLogging");
    this.testProps = p;
    try {
      TestUtil.init(p);
      // Get the table names
      this.tName1 = TestUtil
          .getTableName(TestUtil.getProperty("TxBean_Tab1_Delete"));
      TestUtil.logTrace("tName1: " + this.tName1);
      this.tName2 = TestUtil
          .getTableName(TestUtil.getProperty("TxBean_Tab2_Delete"));
      TestUtil.logTrace("tName2: " + this.tName2);

    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    }
  }

}
