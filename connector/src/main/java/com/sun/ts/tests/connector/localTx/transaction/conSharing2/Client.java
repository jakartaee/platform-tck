/*
 * Copyright (c) 2007, 2024 Oracle and/or its affiliates. All rights reserved.
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
 * @(#)Client.java	1.15 03/05/16
 */

package com.sun.ts.tests.connector.localTx.transaction.conSharing2;

import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;

public class Client extends EETest {

  private static final String testName = "connectortests";

  private static final String testLookup = "java:comp/env/ejb/TestBean";

  private static final String envProps = "testbean.props";

  private static final String testDir = System.getProperty("user.dir");

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
  public void setup(String[] args, Properties p) throws Exception {
    logMsg("Setup tests");
    this.testProps = p;

    try {
      logMsg("Get the naming context");
      jctx = new TSNamingContext();

      logMsg("Getting the EJB " + testLookup);
      beanRef = (TestBean) jctx.lookup(testLookup, TestBean.class);
      beanRef.initialize();

      logMsg("Setup ok");
    } catch (Exception e) {
      throw new Exception("Setup failed:", e);
    }
  }

  /* Run test */

  /*
   * @testName: testMultiComponentLocalTx
   *
   * @assertion_ids: Connector:SPEC:43; Connector:SPEC:44;
   *
   * @test_Strategy: Create a stateful session TX_BEAN_MANAGED bean. Perform a
   * contanier managed transactions using BeanA. BeanA make a connection to
   * database. BeanA calls BeanB to make another connection to the database.
   * BeanA and BeanB are Container managed stateful session beans with
   * transaction attribute TX_REQUIRED. Both BeanA and BeanB access a single EIS
   * resource manager. BeanB closes the connection then BeanA closes the
   * connection.
   *
   */
  public void testMultiComponentLocalTx() throws Exception {
    try {
      logMsg("Logging data from server");

      boolean testResult = false;

      logMsg("Execute TestBean:test1");
      testResult = beanRef.test1();

      if (!testResult)
        throw new Exception("test1 failed");
      else
        logMsg("test1 passed");
    } catch (Exception e) {
      throw new Exception("test1 failed", e);
    }
  }

  /* Test cleanup: */
  public void cleanup() throws Exception {
    logMsg("cleanup ok");
  }
}
