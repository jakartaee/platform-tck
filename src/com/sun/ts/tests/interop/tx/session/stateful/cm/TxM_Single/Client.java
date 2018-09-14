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

package com.sun.ts.tests.interop.tx.session.stateful.cm.TxM_Single;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.util.*;
import javax.ejb.*;
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
   * @assertion_ids: EJB:SPEC:587.1; EJB:SPEC:587.3; EJB:SPEC:715;
   * EJB:SPEC:10717
   *
   * @test_Strategy: EJB Containers supporting transaction interoperability must
   * behave in a defined manner. EJB Containers not supporting transaction
   * interoperability must also behave in a defined manner.
   *
   * for EJB Containers supporting transaction interoperability:
   *
   * If there is no OTS transaction context in the IIOP message then follow
   * defined behavior in Support for Transactions: Container Provider
   * Responsibilities.
   *
   * For EJB Containers not supporting transaction interoperability:
   *
   * If there is no OTS transaction context in the IIOP message follow defined
   * behavior in Support for Distribution and Interoperability: Requirements for
   * EJB Containers Not Supporting Transaction Interoperability.
   *
   * Create a stateful session bean (TxBean) with container- managed transaction
   * demarcation.
   *
   * If the TxBean EJB Container is configured to support transaction
   * interoperability. properties must be set in the ts.jte file as follows:
   *
   * Test Config #1: EJBServer1TxInteropEnabled=false
   * EJBServer2TxInteropEnabled=true
   *
   * Test Config #2: EJBServer1TxInteropEnabled=true
   * EJBServer2TxInteropEnabled=true
   *
   * Call a TxBean method whose transaction attribute is Mandatory without a
   * transaction context. Verify that the TxBean EJB Container throws the
   * TransactionRequiredException.
   *
   * If the TxBean EJB Container is configured to not support transaction
   * interoperability, properties must be set in the ts.jte file as follows:
   *
   * Test Config #3: EJBServer1TxInteropEnabled=false
   * EJBServer2TxInteropEnabled=false
   *
   * Test Config #4: EJBServer1TxInteropEnabled=true
   * EJBServer2TxInteropEnabled=false
   * 
   * Call a TxBean method whose transaction attribute is Mandatory without a
   * transaction context. Verify that the TxBean EJB Container throws the
   * TransactionRequiredException.
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

  /*
   * @testName: test2
   *
   * @assertion_ids: EJB:SPEC:568.3.1; EJB:SPEC:715; EJB:SPEC:10717
   *
   * @test_Strategy: EJB Containers supporting transaction interoperability must
   * behave in a defined manner. EJB Containers not supporting transaction
   * interoperability must also behave in a defined manner.
   *
   * for EJB Containers supporting transaction interoperability:
   *
   * If there is no OTS transaction context in the IIOP message then follow
   * defined behavior in Support for Transactions: Container Provider
   * Responsibilities.
   *
   * For EJB Containers not supporting transaction interoperability:
   *
   * If there is no OTS transaction context in the IIOP message follow defined
   * behavior in Support for Distribution and Interoperability: Requirements for
   * EJB Containers Not Supporting Transaction Interoperability.
   *
   * Create a stateful session bean (TxBean) with container- managed transaction
   * demarcation.
   *
   * If the TxBean EJB Container is configured to support transaction
   * interoperability. properties must be set in the ts.jte file as follows:
   *
   * Test Config #1: EJBServer1TxInteropEnabled=false
   * EJBServer2TxInteropEnabled=true
   *
   * Test Config #2: EJBServer1TxInteropEnabled=true
   * EJBServer2TxInteropEnabled=true
   *
   * Call a method in the TxBean that uses the Supports transaction attribute in
   * a Style 3 declaration and the Mandatory transaction attribute in a Style 2
   * declaration. The call occurs without a global transaction context.
   *
   * Ensure that this operation executes successfully and that the
   * javax.transaction.TransactionRequiredException exception is not thrown.
   *
   * Call a method with the same name but a different parameter list that has no
   * Style 3 declaration (hence the Style 2 declaration applies). Ensure that
   * this operation throws the javax.transaction.TransactionRequiredException
   * exception.
   *
   * If the TxBean EJB Container is configured to not support transaction
   * interoperability, properties must be set in the ts.jte file as follows:
   *
   * Test Config #3: EJBServer1TxInteropEnabled=false
   * EJBServer2TxInteropEnabled=false
   *
   * Test Config #4: EJBServer1TxInteropEnabled=true
   * EJBServer2TxInteropEnabled=false
   * 
   * Call a method in the TxBean that uses the Supports transaction attribute in
   * a Style 3 declaration and the Mandatory transaction attribute in a Style 2
   * declaration. The call occurs without a global transaction context.
   *
   * Ensure that this operation executes successfully and that the
   * javax.transaction.TransactionRequiredException exception is not thrown.
   *
   * Call a method with the same name but a different parameter list that has no
   * Style 3 declaration (hence the Style 2 declaration applies). Ensure that
   * this operation throws the javax.transaction.TransactionRequiredException
   * exception.
   *
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

  /* Test cleanup: */
  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }
}
