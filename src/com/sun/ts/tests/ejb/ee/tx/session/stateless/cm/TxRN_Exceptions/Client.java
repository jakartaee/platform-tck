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
 * @(#)Client.java	1.24 03/05/16
 */

package com.sun.ts.tests.ejb.ee.tx.session.stateless.cm.TxRN_Exceptions;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.util.*;
import javax.ejb.*;
import javax.rmi.*;

import com.sun.javatest.Status;

public class Client extends EETest {

  private static final String testName = "TxRN_Exceptions";

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
   * @assertion_ids: EJB:SPEC:630.2
   *
   * @test_Strategy: AppException from EJB. Create an instance of a stateful
   * session Testbean bean. Create an instance of a stateless session TxBean
   * (RequiresNew) bean. Perform a delete row operation to a table and throw
   * AppException. Ensure that the exception does not cause an automatic
   * rolledback. Ensure that the client receives AppException.
   */
  public void test1() throws Fault {
    try {
      logMsg("Creating EJB TestBean instance");
      beanRef = (TestBean) beanHome.create();

      logMsg("Logging data from server");
      beanRef.initLogging(testProps);

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
   * @assertion_ids: EJB:SPEC:631.2; EJB:SPEC:631.4
   *
   * @test_Strategy: SystemException from EJB. Create an instance of a stateful
   * session Testbean bean. Create an instance of a stateless session TxBean
   * (RequiresNew) bean. Initiate a container managed transaction and perform a
   * delete row operation to a table and throw SystemException. Ensure that the
   * Container does a rollback on the transaction. Ensure that the instance gets
   * discarded. Ensure that the client receives RemoteException.
   */
  public void test2() throws Fault {
    try {
      logMsg("Creating EJB TestBean instance");
      beanRef = (TestBean) beanHome.create();

      logMsg("Logging data from server");
      beanRef.initLogging(testProps);

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
   * @testName: test4
   *
   * @assertion_ids: EJB:SPEC:631.2; EJB:SPEC:631.4
   *
   * @test_Strategy: EJBException from EJB. Create an instance of a stateful
   * session Testbean bean. Create an instance of a stateless session TxBean
   * (RequiresNew) bean. Initiate a container managed transaction and perform a
   * delete row operation to a table and throw SystemException. Ensure that the
   * Container does a rollback on the transaction. Ensure that the instance gets
   * discarded. Ensure that the client receives RemoteException.
   */
  public void test4() throws Fault {
    try {
      logMsg("Creating EJB TestBean instance");
      beanRef = (TestBean) beanHome.create();

      logMsg("Logging data from server");
      beanRef.initLogging(testProps);

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
   * @assertion_ids: EJB:SPEC:631.2; EJB:SPEC:631.4
   *
   * @test_Strategy: Error from EJB. Create an instance of a stateful session
   * Testbean bean. Create an instance of a stateless session TxBean
   * (RequiresNew) bean. Initiate a container managed transaction and perform a
   * delete row operation to a table and throw SystemException. Ensure that the
   * Container does a rollback on the transaction. Ensure that the instance gets
   * discarded. Ensure that the client receives RemoteException.
   */
  public void test5() throws Fault {
    try {
      logMsg("Creating EJB TestBean instance");
      beanRef = (TestBean) beanHome.create();

      logMsg("Logging data from server");
      beanRef.initLogging(testProps);

      boolean testResult = false;

      logMsg("Execute TestBean:test5");
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

  /* Test cleanup: */
  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }
}
