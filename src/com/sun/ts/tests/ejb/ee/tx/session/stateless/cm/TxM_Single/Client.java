/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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
 * @(#)Client.java	1.22 03/05/16
 */

package com.sun.ts.tests.ejb.ee.tx.session.stateless.cm.TxM_Single;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.util.*;
import jakarta.ejb.*;
import javax.rmi.*;

import com.sun.javatest.Status;

public class Client extends EETest {

  private static final String testName = "TxM_Single";

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
   * @assertion_ids: EJB:SPEC:587.1; EJB:SPEC:587.3
   *
   * @test_Strategy: Create an instance of a stateful session Testbean bean.
   * Create an instance of a stateless session TxBean (Mandatory) bean. Access a
   * TX_MANDATORY EJB without a global transaction context and ensure that this
   * operation throws the jakarta.transaction.TransactionRequiredException
   * exception.
   *
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

  /* Test cleanup: */
  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }
}
