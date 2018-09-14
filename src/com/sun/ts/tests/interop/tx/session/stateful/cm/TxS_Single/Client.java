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

package com.sun.ts.tests.interop.tx.session.stateful.cm.TxS_Single;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.util.*;
import javax.ejb.*;
import javax.rmi.*;

import com.sun.javatest.Status;

public class Client extends EETest {

  private static final String testName = "TxS_Single";

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
   * @assertion: EJB Containers supporting transaction interoperability must
   * behave in a defined manner. EJB Containers not supporting transaction
   * interoperability must also behave in a defined manner.
   *
   * Test the three scenarios for EJB Containers supporting transaction
   * interoperability in section 19.5.2.2.1.
   *
   * EJB Containers supporting transaction interoperability 1) If there is no
   * OTS transaction context in the IIOP message then follow defined behavior in
   * section 17.6. 2) If there is a valid OTS transaction context in the IIOP
   * message then follow defined behavior in section 17.6. 3) If there is a null
   * transaction context in the IIOP message then follow defined behavior in
   * table 19.5.2.2.1.
   *
   * Test the three scenarios for EJB Containers not supporting transaction
   * interoperability in section 19.5.2.2.2.
   *
   * EJB Containers not supporting transaction interoperability 1) If there is
   * no OTS transaction context in the IIOP message then follow defined behavior
   * in section 17.6. 2) If there is a valid OTS transaction context in the IIOP
   * message then follow defined behavior in table 19.5.2.2.2. 3) If there is a
   * null transaction context in the IIOP message then follow defined behavior
   * in table 19.5.2.2.1.
   *
   * Test using container-managed transactions.
   *
   * (Section: 17.6 container-managed transaction demarcation) (Section:
   * 19.5.2.2.1 EJB containers supporting transaction interoperability)
   * (Section: 19.5.2.2.2 EJB containers not supporting transaction
   * interoperability)
   *
   * @test_Strategy: Create an instance of a stateful session Testbean bean.
   * Create an instance of a stateful session TxBean (Supports) bean. Perform
   * Insert/Delete operations to a single table and ensure that these operations
   * are commited automatically after each method completes.
   *
   * The TxBean EJB Container is configured to support transaction
   * interoperability. The following TS properties must be set in the ts.jte
   * file for the following 2 test configurations:
   *
   * Test Config #1: EJBServer1TxInteropEnabled=false
   * EJBServer2TxInteropEnabled=true
   *
   * Test Config #2: EJBServer1TxInteropEnabled=true
   * EJBServer2TxInteropEnabled=true
   *
   * The TxBean EJB Container is configured to not support transaction
   * interoperability. The following TS properties must be set in the ts.jte
   * file for the following 2 test configurations:
   *
   * Test Config #3: EJBServer1TxInteropEnabled=false
   * EJBServer2TxInteropEnabled=false
   *
   * Test Config #4: EJBServer1TxInteropEnabled=true
   * EJBServer2TxInteropEnabled=false
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
