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
 * @(#)Client.java	1.17 03/05/16
 */

package com.sun.ts.tests.ejb.ee.bb.entity.cmp20.nonreentranttest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.util.*;
import javax.ejb.*;
import java.rmi.*;

import com.sun.javatest.Status;

public class Client extends EETest {
  private static final String testBean = "java:comp/env/ejb/TestBean";

  private static final String sBean = "java:comp/env/ejb/SBean";

  private TestBean beanRef = null;

  private TestBeanHome beanHome = null;

  private TestBean beanRef1 = null;

  private TestBean beanRef2 = null;

  private SBean sBeanRef = null;

  private SBean sb1 = null;

  private SBean sb2 = null;

  private SBeanHome sBeanHome = null;

  private Properties props = null;

  private TSNamingContext nctx = null;

  private int successes = 0;

  private int failures = 0;

  private int testNum = 0;

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
      successes = failures = 0;
      logMsg("Obtain naming context");
      nctx = new TSNamingContext();

      // Get EJB Home ...
      logMsg("Looking up home interface for EJB: " + testBean);
      beanHome = (TestBeanHome) nctx.lookup(testBean, TestBeanHome.class);
      // Get EJB Home ...
      logMsg("Looking up home interface for EJB: " + sBean);
      sBeanHome = (SBeanHome) nctx.lookup(sBean, SBeanHome.class);
      logMsg("Setup ok");
    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  /* Run test */

  /*
   * @testName: test2
   * 
   * @assertion_ids: EJB:SPEC:286
   * 
   * @test_Strategy: It is not possible for an application to make loopback
   * calls to an entity instance that is marked as non-reentrant. The Container
   * must throw java.rmi.RemoteException. This test uses same bean instance.
   * Deploy it on the J2EE server. Call loopback test on same bean. Self
   * referential test.
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
   * @assertion_ids: EJB:SPEC:286
   * 
   * @test_Strategy: It is not possible for an application to make loopback
   * calls to a entity instance that is marked as non-reentrant. The Container
   * must throw java.rmi.RemoteException. This test uses another bean instance.
   * Deploy it on the J2EE server. Call loopback test on first bean which in
   * turn calls a second bean which then calls first bean again.
   *
   *
   */

  public void test3() throws Fault {
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 3, "coffee-3", 3);
      logMsg("Calling loopback test via different bean");
      boolean pass = beanRef.loopBackAnotherBean(props);
      if (!pass)
        throw new Fault("test3 failed");
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
   * @assertion_ids: EJB:SPEC:286
   * 
   * @test_Strategy: It is not possible for an application to make loopback
   * calls to an entity instance that is marked as non-reentrant. The Container
   * must throw javax.ejb.EJBException if access is through a Local interface
   * method. This test uses same bean instance. Deploy it on the J2EE server.
   * Call loopback test on same bean. Self referential test.
   *
   */

  public void test4() throws Fault {
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      sBeanRef = (SBean) sBeanHome.create(props);
      logMsg("Calling loopback test via same bean - local interface");
      boolean pass = sBeanRef.loopBackSameBeanLocal();
      if (!pass)
        throw new Fault("test4 failed");
    } catch (Exception e) {
      throw new Fault("test4 failed", e);
    } finally {
      try {
        sBeanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
  }

  /*
   * @testName: test5
   * 
   * @assertion_ids: EJB:SPEC:286
   * 
   * @test_Strategy: It is not possible for an application to make loopback
   * calls to a entity instance that is marked as non-reentrant. The Container
   * must throw javax.ejb.EJBException if access is through a Local interface
   * method. This test uses another bean instance. Deploy it on the J2EE server.
   * Call loopback test on first bean which in turn calls a second bean which
   * then calls first bean again.
   *
   *
   */

  public void test5() throws Fault {
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      sBeanRef = (SBean) sBeanHome.create(props);
      logMsg("Calling loopback test via same bean - local interface");
      boolean pass = sBeanRef.loopBackAnotherBeanLocal();
      if (!pass)
        throw new Fault("test5 failed");
    } catch (Exception e) {
      throw new Fault("test5 failed", e);
    } finally {
      try {
        sBeanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }
}
