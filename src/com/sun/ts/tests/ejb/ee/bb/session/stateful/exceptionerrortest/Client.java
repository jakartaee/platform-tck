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
 * @(#)Client.java	1.25 03/05/16
 */

package com.sun.ts.tests.ejb.ee.bb.session.stateful.exceptionerrortest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.util.*;
import javax.ejb.*;
import java.rmi.*;

import com.sun.javatest.Status;

public class Client extends EETest {
  private static final String testName = "ExceptionlogErrorTest";

  private static final String testLookup = "java:comp/env/ejb/TestBean";

  private static final String testProps = "exceptionerrortest.properties";

  private static final String testDir = System.getProperty("user.dir");

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
   */

  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    try {
      TestUtil.logMsg("Obtain naming context");
      nctx = new TSNamingContext();

      // Get EJB Home ...
      TestUtil.logMsg("Looking up home interface for EJB: " + testLookup);
      beanHome = (TestBeanHome) nctx.lookup(testLookup, TestBeanHome.class);
      TestUtil.logMsg("Setup ok");
    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  /* Run test */

  /*
   * @testName: test1
   * 
   * @assertion_ids: EJB:SPEC:632.1
   * 
   * @test_Strategy: Create a stateful Session Bean (NotSupported). Call a
   * business method which throws an application exception. Verify application
   * exception received in client.
   */

  public void test1() throws Fault {
    boolean pass = true;
    try {
      // create EJB instance
      TestUtil.logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, false);
      try {
        beanRef.throwMyApplicationException();
        TestUtil.logErr("no MyApplicationException occurred");
        pass = false;
      } catch (MyApplicationException e) {
        TestUtil.logMsg("MyApplicationException received as expected " + e);
        if (!e.getMessage().equals("an application exception")) {
          pass = false;
          TestUtil.logErr("application exception message not same");
        } else
          TestUtil.logMsg("application exception message same");
      } catch (Exception e) {
        TestUtil.logErr("unexpected exception: " + e);
        TestUtil.printStackTrace(e);
        pass = false;
      }
      beanRef.remove();
    } catch (Exception e) {
      throw new Fault("test1 failed", e);
    }
    if (!pass)
      throw new Fault("test1 failed");
    else
      TestUtil.logMsg("test1 passed");
  }

  /*
   * @testName: test2
   * 
   * @assertion_ids: EJB:SPEC:633.2; EJB:SPEC:633.3
   * 
   * @test_Strategy: Create a stateful Session Bean (NotSupported). Deploy it on
   * the J2EE server. Call a business method which throws an unchecked
   * exception. Verify all the instance was discarded and RemoteException is
   * thrown.
   */

  public void test2() throws Fault {
    boolean pass = true;
    try {
      // create EJB instance
      TestUtil.logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, false);
      try {
        beanRef.throwEJBException();
        TestUtil.logErr("no RemoteException occurred - unexpected");
        pass = false;
      } catch (RemoteException e) {
        TestUtil.logMsg("RemoteException received as expected " + e);
      } catch (Exception e) {
        TestUtil.logErr("unexpected exception: " + e);
        TestUtil.printStackTrace(e);
        pass = false;
      }
      TestUtil.logMsg("Check to make sure instance was discarded");
      try {
        beanRef.remove();
        TestUtil.logMsg("instance not discarded - unexpected");
        pass = false;
      } catch (NoSuchObjectException e) {
        TestUtil.logMsg("instance discarded - expected");
      }
    } catch (Exception e) {
      throw new Fault("test2 failed", e);
    }
    if (!pass)
      throw new Fault("test2 failed");
    else
      TestUtil.logMsg("test2 passed");
  }

  /*
   * @testName: test4
   * 
   * @assertion_ids: EJB:JAVADOC:11
   * 
   * @test_Strategy: Create a stateful Session Bean (NotSupported). Deploy it on
   * the J2EE server. Upon creation in ejbCreate throw a CreateException
   */

  public void test4() throws Fault {
    boolean pass = true;
    try {
      // create EJB instance
      TestUtil.logMsg("Create EJB instance");
      try {
        beanRef = (TestBean) beanHome.create(props, true);
        TestUtil.logErr("no CreateException occurred");
        pass = false;
      } catch (CreateException e) {
        TestUtil.logMsg("CreateException received as expected " + e);
        if (!e.getMessage().equals("a create exception")) {
          pass = false;
          TestUtil.logErr("create exception message not same");
        } else
          TestUtil.logErr("create exception message same");
      } catch (Exception e) {
        TestUtil.logErr("unexpected exception: " + e);
        TestUtil.printStackTrace(e);
        pass = false;
      }
      TestUtil.logMsg("Check to make sure instance not created");
      if (beanRef != null) {
        TestUtil.logErr("bean reference is not null - unexpected");
        pass = false;
        try {
          beanRef.remove();
        } catch (Exception e) {
          TestUtil.printStackTrace(e);
        }
        ;
      } else {
        TestUtil.logMsg("bean reference is null - expected");
      }
    } catch (Exception e) {
      throw new Fault("test4 failed", e);
    }
    if (!pass)
      throw new Fault("test4 failed");
    else
      TestUtil.logMsg("test4 passed");
  }

  /*
   * @testName: test5
   * 
   * @assertion_ids: EJB:SPEC:633.2; EJB:SPEC:633.3
   * 
   * @test_Strategy: Create a stateful session bean. Deploy it on the J2EE
   * server. Call a business method which throws an unchecked exception. Verify
   * all the instance was discarded and RemoteException is thrown.
   * 
   */

  public void test5() throws Fault {
    boolean pass = true;
    try {
      // create EJB instance
      TestUtil.logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, false);
      try {
        beanRef.throwEJBException();
        TestUtil.logErr("no RemoteException occurred - unexpected");
        pass = false;
      } catch (RemoteException e) {
        TestUtil.logMsg("RemoteException received as expected " + e);
      } catch (Exception e) {
        TestUtil.logErr("unexpected exception: " + e);
        TestUtil.printStackTrace(e);
        pass = false;
      }
      TestUtil.logMsg("Check to make sure instance was discarded");
      try {
        beanRef.remove();
        TestUtil.logMsg("instance not discarded - unexpected");
        pass = false;
      } catch (NoSuchObjectException e) {
        TestUtil.logMsg("instance discarded - expected");
      }
    } catch (Exception e) {
      throw new Fault("test5 failed", e);
    }
    if (!pass)
      throw new Fault("test5 failed");
    else
      TestUtil.logMsg("test5 passed");
  }

  /*
   * @testName: test6
   * 
   * @assertion_ids: EJB:SPEC:633.2; EJB:SPEC:633.3
   * 
   * @test_Strategy: Create a stateful Session Bean (NotSupported). Deploy it on
   * the J2EE server. Call a business method which throws an logError. Verify an
   * exception was thrown and the instance was discarded.
   */

  public void test6() throws Fault {
    boolean pass = true;
    try {
      // create EJB instance
      TestUtil.logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, false);
      try {
        beanRef.throwError();
        TestUtil.logErr("no RemoteException occurred - unexpected");
        pass = false;
      } catch (RemoteException e) {
        TestUtil.logMsg("RemoteException received as expected " + e);
      } catch (Exception e) {
        TestUtil.logErr("unexpected exception: " + e);
        TestUtil.printStackTrace(e);
        pass = false;
      }
      TestUtil.logMsg("Check to make sure instance was discarded");
      try {
        beanRef.remove();
        TestUtil.logMsg("instance not discarded - unexpected");
        pass = false;
      } catch (NoSuchObjectException e) {
        TestUtil.logMsg("instance discarded - expected");
      }
    } catch (Exception e) {
      throw new Fault("test6 failed", e);
    }
    if (!pass)
      throw new Fault("test6 failed");
    else
      TestUtil.logMsg("test6 passed");
  }

  public void cleanup() throws Fault {
    beanRef = null;
    TestUtil.logMsg("cleanup ok");
  }
}
