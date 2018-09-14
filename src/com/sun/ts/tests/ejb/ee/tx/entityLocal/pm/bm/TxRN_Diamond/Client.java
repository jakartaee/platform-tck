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

package com.sun.ts.tests.ejb.ee.tx.entityLocal.pm.bm.TxRN_Diamond;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.util.*;
import javax.ejb.*;
import javax.rmi.*;

import com.sun.javatest.Status;

public class Client extends EETest {

  private static final String testName = "TxRN_Diamond";

  private static final String testLookup = "java:comp/env/ejb/BeanA";

  private static final String envProps = "testbean.props";

  private static final String testDir = System.getProperty("user.dir");

  private BeanAHome beanHome = null;

  private BeanA beanRef = null;

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
      beanHome = (BeanAHome) jctx.lookup(testLookup, BeanAHome.class);

      logMsg("Setup ok");
    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  /* Run test */

  /*
   * @testName: test1
   *
   * @assertion_ids: EJB:SPEC:613
   *
   * @test_Strategy: Bean managed Tx commit - RequiresNew Entity EJBs. Create
   * Session EJBs A (bean managed), B (RequiresNew), C (RequiresNew). Create
   * Entity EJB D (RequiresNew). Begin a transaction in Bean A, involving Beans
   * B & C. Beans B & C update data in Bean D. Ensure the DB updates are
   * committed in Beans B & C. Bean A commits the transaction, but this action
   * should be a no-op due to the nature of Bean D which is RequiresNew.
   */
  public void test1() throws Fault {
    try {
      logMsg("Creating EJB BeanA instance");
      beanRef = (BeanA) beanHome.create(testProps);

      boolean testResult = false;

      logMsg("Execute BeanA:test1");
      testResult = beanRef.test1();

      if (!testResult)
        throw new Fault("test1 failed");
      else
        logMsg("test1 passed");
    } catch (Exception e) {
      throw new Fault("test1 failed", e);
    } finally {
      if (beanRef != null)
        try {
          beanRef.remove();
        } catch (Exception e) {
          TestUtil.logErr("Exception removing bean: " + e.getMessage(), e);
        }
    }
  }

  /*
   * @testName: test2
   *
   * @assertion_ids: EJB:SPEC:613
   *
   * @test_Strategy: Bean managed Tx rollback - RequiresNew Entity EJBs. Create
   * Session EJBs A (bean managed), B (RequiresNew), C (RequiresNew). Create
   * Entity EJB D (RequiresNew). Begin a transaction in Bean A, involving Beans
   * B & C. Beans B & C update data in Bean D. Ensure the DB updates are
   * committed in Beans B & C. Bean A rolls back the transaction, but this
   * action should be a no-op due to the nature of Bean D which is RequiresNew.
   */
  public void test2() throws Fault {
    try {
      logMsg("Creating EJB BeanA instance");
      beanRef = (BeanA) beanHome.create(testProps);

      boolean testResult = false;

      logMsg("Execute BeanA:test2");
      testResult = beanRef.test2();

      if (!testResult)
        throw new Fault("test2 failed");
      else
        logMsg("test2 passed");
    } catch (Exception e) {
      throw new Fault("test2 failed", e);
    } finally {
      if (beanRef != null)
        try {
          beanRef.remove();
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
