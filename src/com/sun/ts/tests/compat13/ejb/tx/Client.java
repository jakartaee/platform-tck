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

package com.sun.ts.tests.compat13.ejb.tx;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.util.*;
import javax.ejb.*;
import javax.rmi.*;

import com.sun.javatest.Status;

public class Client extends EETest {

  private static final String testName = "EJBQL_Tx";

  private static final String testLookup = "java:comp/env/ejb/TestBean";

  private static final String txRLookup = "java:comp/env/ejb/TxRequired";

  private static final String envProps = "testbean.props";

  private static final String testDir = System.getProperty("user.dir");

  private TestBeanHome beanHome = null;

  private TxCommonBeanHome txHomeR = null;

  private TestBean beanRef = null;

  private TxCommonBean tRef = null;

  private Properties testProps = new Properties();

  private TSNamingContext jctx = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup: */

  /*
   * @class.setup_props: java.naming.factory.initial; generateSQL;
   *
   */
  public void setup(String[] args, Properties p) throws Fault {
    logMsg("Setup tests");
    this.testProps = p;

    try {
      logMsg("Get the naming context");
      jctx = new TSNamingContext();

      logMsg("Getting the EJB Home interface for " + testLookup);
      beanHome = (TestBeanHome) jctx.lookup(testLookup, TestBeanHome.class);

      logMsg("Getting the EJB Home interface for " + txRLookup);
      txHomeR = (TxCommonBeanHome) jctx.lookup(txRLookup,
          TxCommonBeanHome.class);

      logTrace("Check for existing data before test run");
      prepareTestData();

      logMsg("Setup ok");
    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  /*
   * @testName: txCompat13Test1
   *
   * @assertion_ids: JavaEE:SPEC:283
   *
   * @test_Strategy: Create an instance of a CMP 2.0 entity bean. Begin a
   * transaction with the transaction attribute set to TX_REQUIRED. Update the
   * data and ensure that the update is visible within the transaction context
   * by invoking a finder method with associated EJB-QL. Commit the transaction
   * and verify the commit was successful.
   */

  public void txCompat13Test1() throws Fault {
    try {
      logMsg("Creating EJB TestBean instance");
      beanRef = (TestBean) beanHome.create(testProps);

      beanRef.initLogging(testProps);

      boolean testResult = false;

      logMsg("Execute TestBean: txCompat13Test1");
      testResult = beanRef.txCompat13Test1();

      if (!testResult)
        throw new Fault("txCompat13Test1 failed");
      else
        logMsg("txCompat13Test1 passed");
    } catch (Exception e) {
      throw new Fault("txCompat13Test1 failed", e);
    } finally {
      try {
        beanRef.remove();
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: txCompat13Test2
   *
   * @assertion_ids: JavaEE:SPEC:283
   * 
   * @test_Strategy: Create an instance of a CMP 2.0 entity bean. Begin a
   * transaction with the transaction attribute set to TX_REQUIRED. Update the
   * data and ensure that the update is visible within the transaction context
   * by invoking a finder method with associated EJB-QL. Rollback the
   * transaction and verify the rollback was successful.
   *
   */

  public void txCompat13Test2() throws Fault {
    try {
      logMsg("Creating EJB TestBean instance");
      beanRef = (TestBean) beanHome.create(testProps);

      beanRef.initLogging(testProps);

      boolean testResult = false;

      logMsg("Execute TestBean: txCompat13Test2");
      testResult = beanRef.txCompat13Test2();

      if (!testResult)
        throw new Fault("txCompat13Test2 failed");
      else
        logMsg("txCompat13Test2 passed");
    } catch (Exception e) {
      throw new Fault("txCompat13Test2 failed", e);
    } finally {
      try {
        beanRef.remove();
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: txCompat13Test3
   *
   * @assertion_ids: JavaEE:SPEC:283
   *
   * @test_Strategy: Create an instance of a CMP 2.0 entity bean. Begin a
   * transaction with the transaction attribute set to TX_REQUIRES_NEW. Update
   * the data and ensure that the update is visible within the transaction
   * context by invoking a finder method with associated EJB-QL. Commit the
   * transaction and verify the commit was successful.
   *
   */

  public void txCompat13Test3() throws Fault {
    try {
      logMsg("Creating EJB TestBean instance");
      beanRef = (TestBean) beanHome.create(testProps);

      beanRef.initLogging(testProps);

      boolean testResult = false;

      logMsg("Execute TestBean: txCompat13Test3");
      testResult = beanRef.txCompat13Test3();

      if (!testResult)
        throw new Fault("txCompat13Test3 failed");
      else
        logMsg("txCompat13Test3 passed");
    } catch (Exception e) {
      throw new Fault("txCompat13Test3 failed", e);
    } finally {
      try {
        beanRef.remove();
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: txCompat13Test4
   *
   * @assertion_ids: JavaEE:SPEC:283
   *
   * @test_Strategy: Create an instance of a CMP 2.0 entity bean. Begin a
   * transaction with the transaction attribute set to TX_REQUIRES_NEW. Update
   * the data and ensure that the update is visible within the transaction
   * context by invoking a finder method with associated EJB-QL. Rollback the
   * transaction and verify the rollback was successful.
   *
   */

  public void txCompat13Test4() throws Fault {
    try {
      logMsg("Creating EJB TestBean instance");
      beanRef = (TestBean) beanHome.create(testProps);

      beanRef.initLogging(testProps);

      boolean testResult = false;

      logMsg("Execute TestBean: txCompat13Test4");
      testResult = beanRef.txCompat13Test4();

      if (!testResult)
        throw new Fault("txCompat13Test4 failed");
      else
        logMsg("txCompat13Test4 passed");
    } catch (Exception e) {
      throw new Fault("txCompat13Test4 failed", e);
    } finally {
      try {
        beanRef.remove();
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: txCompat13Test5
   *
   * @assertion_ids: JavaEE:SPEC:283
   *
   * @test_Strategy: Create an instance of a CMP 2.0 entity bean. Begin a
   * transaction with the transaction attribute set to TX_MANDATORY. Update the
   * data and ensure that the update is visible within the transaction context
   * by invoking a finder method with associated EJB-QL. Commit the transaction
   * and verify the commit was successful.
   *
   */

  public void txCompat13Test5() throws Fault {
    try {
      logMsg("Creating EJB TestBean instance");
      beanRef = (TestBean) beanHome.create(testProps);

      beanRef.initLogging(testProps);

      boolean testResult = false;

      logMsg("Execute TestBean: txCompat13Test5");
      testResult = beanRef.txCompat13Test5();

      if (!testResult)
        throw new Fault("txCompat13Test5 failed");
      else
        logMsg("txCompat13Test5 passed");
    } catch (Exception e) {
      throw new Fault("txCompat13Test5 failed", e);
    } finally {
      try {
        beanRef.remove();
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: txCompat13Test6
   *
   * @assertion_ids: JavaEE:SPEC:283
   *
   * @test_Strategy: Create an instance of a CMP 2.0 entity bean. Begin a
   * transaction with the transaction attribute set to TX_MANDATORY. Update the
   * data and ensure that the update is visible within the transaction context
   * by invoking a finder method with associated EJB-QL. Rollback the
   * transaction and verify the rollback was successful.
   *
   */

  public void txCompat13Test6() throws Fault {
    try {
      logMsg("Creating EJB TestBean instance");
      beanRef = (TestBean) beanHome.create(testProps);

      beanRef.initLogging(testProps);

      boolean testResult = false;

      logMsg("Execute TestBean: txCompat13Test6");
      testResult = beanRef.txCompat13Test6();

      if (!testResult)
        throw new Fault("txCompat13Test6 failed");
      else
        logMsg("txCompat13Test6 passed");
    } catch (Exception e) {
      throw new Fault("txCompat13Test6 failed", e);
    } finally {
      try {
        beanRef.remove();
      } catch (Exception e) {
      }
    }
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  private void prepareTestData() {
    TestUtil.logMsg("Entering method prepareTestData");
    Collection c1 = null;

    try {
      c1 = txHomeR.findAllBeans();
      if (!c1.isEmpty()) {
        TestUtil.logTrace("Collection not empty.  Size is: " + c1.size());
        Iterator i1 = c1.iterator();
        while (i1.hasNext()) {
          Object o = i1.next();
          TxCommonBean tRef = (TxCommonBean) PortableRemoteObject.narrow(o,
              TxCommonBean.class);
          for (int l = 0; l < c1.size(); l++)
            tRef.remove();
        }
      } else {
        TestUtil.logTrace("No entity data to clean up");
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception caught preparing test data:" + e);
    }
    TestUtil.logTrace("Exiting method prepareTestData");
  }

}
