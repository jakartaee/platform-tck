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

package com.sun.ts.tests.ejb.ee.tx.entity.bmp.bm.Tx_Single;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.util.*;
import javax.ejb.*;
import javax.rmi.*;

import com.sun.javatest.Status;

public class Client extends EETest {

  private static final String testName = "Tx_Single";

  private static final String testLookup = "java:comp/env/ejb/TestBean";

  private static final String envProps = "testbean.props";

  private static final String testDir = System.getProperty("user.dir");

  private TestBeanHome beanHome = null;

  private TestBean beanRef = null;

  private Properties testProps = new Properties();

  private TSNamingContext jctx = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup: */

  /*
   * @class.setup_props: java.naming.factory.initial;
   *
   * @class.testArgs: -ap tssql.stmt
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

      logMsg("Setup ok");
    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  /* Run test */

  /*
   * @testName: test1
   *
   *
   * @assertion_ids: EJB:SPEC:583.1; EJB:SPEC:583.2
   *
   * @test_Strategy: Bean managed Tx commit - Required Entity EJBs. Start a user
   * Transaction Create an instance of an Entity EJB (TxEBean) bean. Commit the
   * transaction and Ensure that the instance data is updated. Ensure that the
   * database fields are updated.
   *
   */
  public void test1() throws Fault {
    try {
      logMsg("Creating EJB TestBean instance");
      beanRef = (TestBean) beanHome.create(testProps);

      boolean testResult = false;

      logMsg("Execute TestBean:test1");
      testResult = beanRef.test1();

      if (!testResult)
        throw new Fault("test1 failed");
      else
        logMsg("test1 passed");
    } catch (Exception e) {
      throw new Fault("test1 failed", e);
    } finally {
      try {
        if (beanRef != null) {
          beanRef.remove();
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception removing bean: " + e.getMessage(), e);
      }
    }
  }

  /*
   * @testName: test2
   *
   * @assertion_ids: EJB:SPEC:583.1; EJB:SPEC:583.2
   *
   * @test_Strategy: Bean managed Tx rollback - Required Entity EJBs. Start a
   * user Transaction Create an instance of an Entity EJB (TxEBean) bean.
   * Rollback the transaction. Attempt to update the DB data Verify that a
   * RemoteException gets thrown
   *
   */
  public void test2() throws Fault {
    try {
      logMsg("Creating EJB TestBean instance");
      beanRef = (TestBean) beanHome.create(testProps);

      boolean testResult = false;

      logMsg("Execute TestBean:test2");
      testResult = beanRef.test2();

      if (!testResult)
        throw new Fault("test2 failed");
      else
        logMsg("test2 passed");
    } catch (Exception e) {
      throw new Fault("test2 failed", e);
    } finally {
      try {
        if (beanRef != null) {
          beanRef.remove();
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception removing bean: " + e.getMessage(), e);
      }
    }
  }

  /*
   * @testName: test3
   *
   * @assertion_ids: EJB:SPEC:583.1; EJB:SPEC:583.2
   *
   * @test_Strategy: Bean managed Tx commit - Required Entity EJBs. Create an
   * instance of the Entity EJB (TxEBean) bean. Then create another instance of
   * that bean using FindByPrimaryKey().. Perform updates to the found Entity
   * EJB's instance data. Commit the transaction and Ensure that the instance
   * data is updated on method return. Ensure that the database fields are
   * updated.
   *
   */
  public void test3() throws Fault {
    try {
      logMsg("Creating EJB TestBean instance");
      beanRef = (TestBean) beanHome.create(testProps);

      boolean testResult = false;

      logMsg("Execute TestBean:test3");
      testResult = beanRef.test3();

      if (!testResult)
        throw new Fault("test3 failed");
      else
        logMsg("test3 passed");
    } catch (Exception e) {
      throw new Fault("test3 failed", e);
    } finally {
      try {
        if (beanRef != null) {
          beanRef.remove();
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception removing bean: " + e.getMessage(), e);
      }
    }
  }

  /*
   * @testName: test4
   *
   * @assertion_ids: EJB:SPEC:583.1; EJB:SPEC:583.2
   *
   * @test_Strategy: Bean managed Tx rollback - Required Entity EJBs. Create an
   * instances of an Entity EJB (TxEBean) bean. Then find another instance of
   * that bean using FindByPrimaryKey().. Perform updates to the found Entity
   * EJB's instance data. rollback the transaction and Ensure that the database
   * fields are not updated.
   *
   */

  public void test4() throws Fault {
    try {
      logMsg("Creating EJB TestBean instance");
      beanRef = (TestBean) beanHome.create(testProps);

      boolean testResult = false;

      logMsg("Execute TestBean:test4");
      testResult = beanRef.test4();

      if (!testResult)
        throw new Fault("test4 failed");
      else
        logMsg("test4 passed");
    } catch (Exception e) {
      throw new Fault("test4 failed", e);
    } finally {
      try {
        if (beanRef != null) {
          beanRef.remove();
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception removing bean: " + e.getMessage(), e);
      }
    }
  }

  /*
   * @testName: test7
   *
   * @assertion_ids:
   *
   * @test_Strategy: Bean managed Tx commit - RequiresNew Entity EJBs. Create an
   * instance of an Entity EJB (TxEBean) bean. Perform an update to the Entity
   * EJB's instance data. Commit the transaction and Verify the instance data
   * updates as well as the Database data is updated
   *
   */
  public void test7() throws Fault {
    try {
      logMsg("Creating EJB TestBean instance");
      beanRef = (TestBean) beanHome.create(testProps);

      boolean testResult = false;

      logMsg("Execute TestBean:test7");
      testResult = beanRef.test7();

      if (!testResult)
        throw new Fault("test7 failed");
      else
        logMsg("test7 passed");
    } catch (Exception e) {
      throw new Fault("test7 failed", e);
    } finally {
      try {
        if (beanRef != null) {
          beanRef.remove();
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception removing bean: " + e.getMessage(), e);
      }
    }
  }

  /*
   * @testName: test8
   *
   * @assertion_ids:
   *
   * @test_Strategy: Bean managed Tx rollback - RequiresNew Entity EJBs. Create
   * an instance of an Entity EJB (TxEBean) bean. Perform an update to the
   * Entity EJB's instance data. Rollback the transaction and Verify the
   * instance data updates as well as the Database data is updated
   *
   */
  public void test8() throws Fault {
    try {
      logMsg("Creating EJB TestBean instance");
      beanRef = (TestBean) beanHome.create(testProps);

      boolean testResult = false;

      logMsg("Execute TestBean:test8");
      testResult = beanRef.test8();

      if (!testResult)
        throw new Fault("test8 failed");
      else
        logMsg("test8 passed");
    } catch (Exception e) {
      throw new Fault("test8 failed", e);
    } finally {
      try {
        if (beanRef != null) {
          beanRef.remove();
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception removing bean: " + e.getMessage(), e);
      }
    }
  }

  /* Test cleanup: */
  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }
}
