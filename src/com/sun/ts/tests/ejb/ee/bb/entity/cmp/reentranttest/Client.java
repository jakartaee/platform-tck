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
 * @(#)Client.java	1.27 03/05/16
 */

package com.sun.ts.tests.ejb.ee.bb.entity.cmp.reentranttest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.util.*;
import javax.ejb.*;
import javax.transaction.*;
import java.rmi.*;

import com.sun.javatest.Status;

public class Client extends EETest {
  private static final String testName = "ReEntrantTest";

  private static final String testBean = "java:comp/env/ejb/TestBean";

  private static final String loopBack = "java:comp/env/ejb/LoopBackBean";

  private static final String testProps = "reentranttest.properties";

  private TestBean beanRef = null;

  private TestBeanHome beanHome = null;

  private Properties props = null;

  private TSNamingContext nctx = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   * generateSQL;
   */

  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    try {
      logMsg("Obtain naming context");
      nctx = new TSNamingContext();

      // Get EJB Home ...
      logMsg("Looking up home interface for EJB: " + testBean);
      beanHome = (TestBeanHome) nctx.lookup(testBean, TestBeanHome.class);
      logMsg("Setup ok");
    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  /* Run test */

  /*
   * @testName: test2
   * 
   * @assertion_ids: EJB:SPEC:473
   * 
   * @test_Strategy: Create a CMP 1.1 Entity Bean. Deploy it on the J2EE server.
   * Call loopback test on same bean. Verify the Container allows the loopback
   * call. Self referential test.
   *
   */

  public void test2() throws Fault {
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 2, "coffee-2", 2);
      logMsg("Calling loopback test via same bean");
      boolean pass = beanRef.loopBackSameBean();
      if (!pass)
        throw new Fault("test2 failed");
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
   * @assertion_ids: EJB:SPEC:473
   * 
   * @test_Strategy: Create a CMP 1.1 Entity Bean. Deploy it on the J2EE server.
   * Verify the Container allows the loopback call. This test uses another bean
   * instance.
   *
   */

  public void test3() throws Fault {
    LoopBackHome loopHome = null;
    LoopBack loopRef = null;

    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 3, "coffee-3", 3);

      // Get EJB Home for LoopBack ...
      logMsg("Looking up home interface for EJB: " + loopBack);
      loopHome = (LoopBackHome) nctx.lookup(loopBack, LoopBackHome.class);

      logMsg("Creating EJB for: " + loopBack);
      loopRef = (LoopBack) loopHome.create(beanRef);

      logMsg("Calling loopback test via different bean");
      boolean pass = beanRef.loopBackAnotherBean(loopRef);
      if (!pass)
        throw new Fault("test3 failed");
    } catch (Exception e) {
      throw new Fault("test3 failed", e);
    } finally {
      try {
        loopRef.remove();
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }
}
