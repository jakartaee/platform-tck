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
 * @(#)Client.java	1.12 03/05/16
 */

package com.sun.ts.tests.ejb.ee.tx.entityLocal.pm.bm.Tx_Single;

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
   * @assertion_ids: EJB:SPEC:583.1; EJB:SPEC:583.2
   *
   * @test_Strategy: Bean managed Tx commit - Required Entity EJBs. Start a user
   * Transaction Create an instance of an Entity EJB (TxEPMBeanLocal) bean.
   * Update some Data in the bean. Commit the transaction. Create an instance of
   * the previously created bean, by doing a findByPrimaryKey(). Ensure that the
   * instance data reflects the update that occurred to the previous bean. This
   * assures us that the database was also updated
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
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
  }

  /*
   * @testName: test2
   *
   * @assertion_ids: EJB:SPEC:583.1; EJB:SPEC:583.2
   *
   * @test_Strategy: Bean managed Tx Rolled Back - Required Entity EJBs. Start a
   * user Transaction Create an instance of an Entity EJB (TxEPMBeanLocal) bean.
   * Commit the creation of the bean. Start another transaction Update some Data
   * in the bean. Rollback the transaction. Create an instance of the previously
   * created bean, by doing a findByPrimaryKey(). Ensure that the instance data
   * reflects the original values of the previous bean. This assures us that the
   * database was also rolledback.
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
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
  }

  /*
   * @testName: test3
   *
   * @assertion_ids: EJB:SPEC:583.1; EJB:SPEC:583.2
   *
   * @test_Strategy: Bean managed Tx commit - Required Entity EJBs. Start a user
   * Transaction Create an instance of an Entity EJB (TxEPMBeanLocal) bean.
   * Commit the transaction. Start a user Transaction Create an instance of the
   * previously created bean, by doing a findByPrimaryKey(). Update some Data in
   * the bean. Commit the transaction. Create an instance of the previously
   * created bean, by doing a findByPrimaryKey(). Ensure that the instance data
   * reflects the update that occurred to the previous bean. This assures us
   * that the database was also updated via the second bean which was created
   * using findByPrimaryKey().
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
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
  }

  /*
   * @testName: test4
   *
   * @assertion_ids: EJB:SPEC:583.1; EJB:SPEC:583.2
   *
   * @test_Strategy: Bean managed Tx Rolled back - Required Entity EJBs. Start a
   * user Transaction Create an instance of an Entity EJB (TxEPMBeanLocal) bean.
   * Commit the transaction. Start a user Transaction Create an instance of the
   * previously created bean, by doing a findByPrimaryKey(). Update some Data in
   * the bean. Rollback the transaction. Create an instance of the previously
   * created bean, by doing a findByPrimaryKey(). Ensure that the instance data
   * reflects the rollback that occurred to the previous bean by checking to see
   * if the values are equal to the original values. This assures us that the
   * database was also rolled back via the second bean which was created using
   * findByPrimaryKey()
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
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
  }

  /*
   * @testName: test5
   *
   * @assertion_ids:
   *
   * @test_Strategy: Bean managed Tx commit - RequiresNew Entity EJBs. Start a
   * user Transaction Create an instance of an Entity EJB (TxEPMBeanLocal) bean.
   * Perform an update to the Entity EJB's instance data. Commit the
   * transaction. Create an instance of the previously created bean, by doing a
   * findByPrimaryKey(). Ensure that the instance data reflects the update that
   * occurred to the previous bean. This assures us that the database was also
   * updated Verify the instance data updates as well as the Database data is
   * updated
   *
   */
  public void test5() throws Fault {
    try {
      logMsg("Creating EJB TestBean instance");
      beanRef = (TestBean) beanHome.create(testProps);

      boolean testResult = false;

      logMsg("Execute TestBean:test7");
      testResult = beanRef.test5();

      if (!testResult)
        throw new Fault("test5 failed");
      else
        logMsg("test5 passed");
    } catch (Exception e) {
      throw new Fault("test5 failed", e);
    } finally {
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
  }

  /*
   * @testName: test6
   *
   * @assertion_ids:
   *
   * @test_Strategy: Bean managed Tx rollback - RequiresNew Entity EJBs. Start a
   * user Transaction Create an instance of an Entity EJB (TxEPMBeanLocal) bean.
   * Commit the creation of the bean. Start another transaction Update some Data
   * in the bean. Rollback the transaction. Create an instance of the previously
   * created bean, by doing a findByPrimaryKey(). Ensure that the instance data
   * reflects the original values of the previous bean. This assures us that the
   * database was also rolledback.
   *
   */
  public void test6() throws Fault {
    try {
      logMsg("Creating EJB TestBean instance");
      beanRef = (TestBean) beanHome.create(testProps);

      boolean testResult = false;

      logMsg("Execute TestBean:test8");
      testResult = beanRef.test6();

      if (!testResult)
        throw new Fault("test6 failed");
      else
        logMsg("test6 passed");
    } catch (Exception e) {
      throw new Fault("test6 failed", e);
    } finally {
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
  }

  /* Test cleanup: */
  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }
}
