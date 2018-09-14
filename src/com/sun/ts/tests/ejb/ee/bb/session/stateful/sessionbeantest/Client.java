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

package com.sun.ts.tests.ejb.ee.bb.session.stateful.sessionbeantest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.util.*;
import javax.ejb.*;
import javax.transaction.*;
import java.rmi.*;

import com.sun.javatest.Status;

//****************************************************
//SessionBean Lifecyle Test for STATEFUL session beans
//****************************************************

public class Client extends EETest {
  private static final String testName = "SessionBeanTest";

  private static final String tBean = "java:comp/env/ejb/TestBean";

  private static final String cBean = "java:comp/env/ejb/CallBack";

  private static final String testProps = "sessionbeantest.properties";

  private static final String testDir = System.getProperty("user.dir");

  private Properties props = null;

  private TestBean beanRef = null;

  private TestBeanHome beanHome = null;

  private TSNamingContext nctx = null;

  private CallBack cRef = null;

  private CallBackHome cHome = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   * user1, the database username; password1, the database password;
   * 
   * @class.testArgs: -ap tssql.stmt
   */

  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    try {
      logMsg("Obtain naming context");
      nctx = new TSNamingContext();

      // Get EJB Home ...
      logMsg("Looking up home interface for EJB: " + tBean);
      beanHome = (TestBeanHome) nctx.lookup(tBean, TestBeanHome.class);

      logMsg("Looking up home interface for EJB: " + cBean);
      cHome = (CallBackHome) nctx.lookup(cBean, CallBackHome.class);
      logMsg("creating callback bean");
      cRef = cHome.create();

      logMsg("Setup ok");
    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  /* Run test */

  /*
   * @testName: test1
   * 
   * @assertion_ids: EJB:SPEC:75
   * 
   * @test_Strategy: Create a stateful Session Bean. Deploy it on the J2EE
   * server. Check creation life cycle call flow occurs.
   *
   */

  public void test1() throws Fault {
    try {
      cRef.reset();
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create();
      logMsg("initialize remote logging");
      beanRef.initLogging(props);
      logMsg("check if proper lifecycle creation order was called in the bean");
      boolean pass = beanRef.isCreateLifeCycle1();
      beanRef.remove();
      if (!pass)
        throw new Fault("test1 failed");
    } catch (Exception e) {
      throw new Fault("test1 failed", e);
    }
  }

  /*
   * @testName: test2
   * 
   * @assertion_ids: EJB:SPEC:75
   * 
   * @Test_Strategy: Create a stateful Session Bean. Deploy it on the J2EE
   * server. Check creation life cycle call flow occurs.
   *
   */

  public void test2() throws Fault {
    try {
      cRef.reset();
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(cRef);
      logMsg("initialize remote logging");
      beanRef.initLogging(props);
      logMsg("check if proper lifecycle creation order was called in the bean");
      boolean pass = beanRef.isCreateLifeCycle2();
      beanRef.remove();
      if (!pass)
        throw new Fault("test2 failed");
    } catch (Exception e) {
      throw new Fault("test2 failed", e);
    }
  }

  /*
   * @testName: test3
   * 
   * @assertion_ids: EJB:SPEC:76
   * 
   * @test_Strategy: Create a stateful Session EJBean. Deploy it on the J2EE
   * server. Call ejbRemove() and check a CallBack object was notified to ensure
   * ejbRemove was called. Check for NoSuchObjectException or RemoteException on
   * subsequent remove operation.
   *
   */

  public void test3() throws Fault {
    try {
      cRef.reset();
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(cRef);
      logMsg("initialize remote logging");
      beanRef.initLogging(props);
      logMsg("Remove bean and check ejbRemove() was called in the bean");
      beanRef.remove();
      boolean pass = cRef.isRemove();
      if (pass)
        logMsg("ejbRemove was called - expected");
      else {
        logMsg("ejbRemove was not called - unexpected");
        throw new Fault("test3 failed");
      }
      logMsg("attempt to invoke bean after removal");
      try {
        beanRef.remove();
        logErr("object was not removed, was able to invoke object");
        throw new Fault("test3 failed");
      } catch (NoSuchObjectException e) {
        logMsg("object was removed as expected: " + e);
      } catch (RemoteException e) {
        logMsg("object was removed as expected: " + e);
      } catch (Exception e) {
        throw new Fault("test3 failed", e);
      }
    } catch (Exception e) {
      throw new Fault("test3 failed", e);
    }
  }

  /*
   * @testName: test3a
   * 
   * @assertion_ids: EJB:SPEC:76
   * 
   * @test_Strategy: Create a stateful Session EJBean. Deploy it on the J2EE
   * server. Invoke remove through EJBHome interface twice. Call ejbRemove() and
   * check a CallBack object was notified to ensure ejbRemove was called. Check
   * for NoSuchObjectException or RemoteException on subsequent remove
   * operation.
   *
   */

  public void test3a() throws Fault {
    try {
      cRef.reset();
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(cRef);
      logMsg("initialize remote logging");
      beanRef.initLogging(props);
      logMsg("Remove bean and check ejbRemove() was called in the bean");
      Handle handle = beanRef.getHandle();
      beanHome.remove(handle);
      boolean pass = cRef.isRemove();
      if (pass)
        logMsg("ejbRemove was called - expected");
      else {
        logMsg("ejbRemove was not called - unexpected");
        throw new Fault("test3a failed");
      }
      logMsg("attempt to invoke bean after removal");
      try {
        beanHome.remove(handle);
        logErr("object was not removed, was able to invoke object");
        throw new Fault("test3a failed");
      } catch (RemoteException e) {
        logMsg("a remote exception occurred as expected: " + e);
      } catch (Exception e) {
        throw new Fault("test3a failed", e);
      }
    } catch (Exception e) {
      throw new Fault("test3a failed", e);
    }
  }

  /*
   * @testName: test3b
   * 
   * @assertion_ids: EJB:SPEC:76
   * 
   * @test_Strategy: Create a stateful Session EJBean. Deploy it on the J2EE
   * server. Invoke remove through EJBObject interface and check to ensure
   * NoSuchObjectException or RemoteException occurs when attempting to invoke a
   * business method after bean removal.
   *
   */

  public void test3b() throws Fault {
    try {
      cRef.reset();
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(cRef);
      logMsg("initialize remote logging");
      beanRef.initLogging(props);
      logMsg("Remove bean and check ejbRemove() was called in the bean");
      beanRef.remove();
      boolean pass = cRef.isRemove();
      if (pass)
        logMsg("ejbRemove was called - expected");
      else {
        logMsg("ejbRemove was not called - unexpected");
        throw new Fault("test3b failed");
      }
      logMsg("attempt to invoke bean after removal");
      try {
        beanRef.ping();
        logErr("object was not removed, was able to invoke object");
        throw new Fault("test3b failed");
      } catch (NoSuchObjectException e) {
        logMsg("object was removed as expected: " + e);
      } catch (RemoteException e) {
        logMsg("object was removed as expected: " + e);
      } catch (Exception e) {
        throw new Fault("test3b failed", e);
      }
    } catch (Exception e) {
      throw new Fault("test3b failed", e);
    }
  }

  /*
   * @testName: test3c
   * 
   * @assertion_ids: EJB:SPEC:76
   * 
   * @test_Strategy: Create a stateful Session EJBean. Deploy it on the J2EE
   * server. Call ejbRemove() through EJBHome and check a CallBack object was
   * notified to ensure ejbRemove was called. Check to ensure
   * NoSuchObjectException or RemoteException occurs when attempting to invoke a
   * business method after bean removal.
   *
   */

  public void test3c() throws Fault {
    try {
      cRef.reset();
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(cRef);
      logMsg("initialize remote logging");
      beanRef.initLogging(props);
      logMsg("Remove bean and check ejbRemove() was called in the bean");
      Handle handle = beanRef.getHandle();
      beanHome.remove(handle);
      boolean pass = cRef.isRemove();
      if (pass)
        logMsg("ejbRemove was called - expected");
      else {
        logMsg("ejbRemove was not called - unexpected");
        throw new Fault("test3c failed");
      }
      logMsg("attempt to invoke bean after removal");
      try {
        beanRef.ping();
        logErr("object was not removed, was able to invoke object");
        throw new Fault("test3c failed");
      } catch (NoSuchObjectException e) {
        logMsg("object was removed as expected: " + e);
      } catch (RemoteException e) {
        logMsg("object was removed as expected: " + e);
      } catch (Exception e) {
        throw new Fault("test3c failed", e);
      }
    } catch (Exception e) {
      throw new Fault("test3c failed", e);
    }
  }

  /*
   * @testName: test6
   * 
   * @assertion_ids: EJB:SPEC:62; EJB:JAVADOC:174; EJB:JAVADOC:180;
   * EJB:JAVADOC:177
   * 
   * @test_Strategy: Create a stateful Session Bean. Deploy it on the J2EE
   * server. Check session synchronization life cycle call flow occurs. This
   * tests a committed transaction. Required transaction attribute set.
   *
   */

  public void test6() throws Fault {
    try {
      cRef.reset();
      logMsg(
          "Required: Testing transaction notification synchronization - commit");
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(cRef);
      logMsg("initialize remote logging");
      beanRef.initLogging(props);
      logMsg("check for proper session synchronization lifecycle call order");
      boolean pass = beanRef.isSyncLifeCycle1("TxRequired");
      beanRef.remove();
      if (!pass)
        throw new Fault("test6 failed");
    } catch (Exception e) {
      throw new Fault("test6 failed", e);
    }
  }

  /*
   * @testName: test7
   * 
   * @assertion_ids: EJB:SPEC:62; EJB:JAVADOC:174; EJB:JAVADOC:180;
   * EJB:JAVADOC:177
   * 
   * @test_Strategy: Create a stateful Session Bean. Deploy it on the J2EE
   * server. Check session synchronization life cycle call flow occurs. This
   * tests a non-committed transaction (rollback). Required transaction
   * attribute set.
   *
   */

  public void test7() throws Fault {
    try {
      cRef.reset();
      logMsg(
          "Required: Testing transaction notification synchronization - rollback");
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(cRef);
      logMsg("initialize remote logging");
      beanRef.initLogging(props);
      logMsg("check for proper session synchronization lifecycle call order");
      boolean pass = beanRef.isSyncLifeCycle3("TxRequired", true, true);
      beanRef.remove();
      if (!pass)
        throw new Fault("test7 failed");
    } catch (Exception e) {
      throw new Fault("test7 failed", e);
    }
  }

  /*
   * @testName: test7a
   * 
   * @assertion_ids: EJB:SPEC:66; EJB:JAVADOC:174; EJB:JAVADOC:180;
   * EJB:JAVADOC:177
   * 
   * @test_Strategy: Create a stateful Session Bean. Deploy it on the J2EE
   * server. Check session synchronization life cycle call flow occurs. This
   * tests a non-committed transaction (rollback). Required transaction
   * attribute set.
   *
   */

  public void test7a() throws Fault {
    try {
      cRef.reset();
      logMsg(
          "Required: Testing transaction notification synchronization - rollback");
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(cRef);
      logMsg("initialize remote logging");
      beanRef.initLogging(props);
      logMsg("check for proper session synchronization lifecycle call order");
      boolean pass = beanRef.isSyncLifeCycle2("TxRequired", true, false);
      beanRef.remove();
      if (!pass)
        throw new Fault("test7a failed");
    } catch (Exception e) {
      throw new Fault("test7a failed", e);
    }
  }

  /*
   * @testName: test8
   * 
   * @assertion_ids: EJB:SPEC:62; EJB:JAVADOC:174; EJB:JAVADOC:180;
   * EJB:JAVADOC:177
   * 
   * @test_Strategy: Create a stateful Session Bean. Deploy it on the J2EE
   * server. Check session synchronization life cycle call flow occurs. This
   * tests a committed transaction. RequiresNew transaction attribute set.
   *
   */

  public void test8() throws Fault {
    try {
      cRef.reset();
      logMsg(
          "RequiresNew: Testing transaction notification synchronization - commit");
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(cRef);
      logMsg("initialize remote logging");
      beanRef.initLogging(props);
      logMsg("check for proper session synchronization lifecycle call order");
      boolean pass = beanRef.isSyncLifeCycle1("TxRequiresNew");
      beanRef.remove();
      if (!pass)
        throw new Fault("test8 failed");
    } catch (Exception e) {
      throw new Fault("test8 failed", e);
    }
  }

  /*
   * @testName: test9
   * 
   * @assertion_ids: EJB:SPEC:62; EJB:JAVADOC:174; EJB:JAVADOC:180;
   * EJB:JAVADOC:177
   * 
   * @test_Strategy: Create a stateful Session Bean. Deploy it on the J2EE
   * server. Check session synchronization life cycle call flow occurs. This
   * tests a non-committed transaction (rollback). RequiresNew transaction
   * attribute set.
   *
   */

  public void test9() throws Fault {
    try {
      cRef.reset();
      logMsg(
          "RequiresNew: Testing transaction notification synchronization - rollback");
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(cRef);
      logMsg("initialize remote logging");
      beanRef.initLogging(props);
      logMsg("check for proper session synchronization lifecycle call order");
      boolean pass = beanRef.isSyncLifeCycle3("TxRequiresNew", true, true);
      beanRef.remove();
      if (!pass)
        throw new Fault("test9 failed");
    } catch (Exception e) {
      throw new Fault("test9 failed", e);
    }
  }

  /*
   * @testName: test9a
   * 
   * @assertion_ids: EJB:SPEC:66; EJB:JAVADOC:174; EJB:JAVADOC:180;
   * EJB:JAVADOC:177
   * 
   * @test_Strategy: Create a stateful Session Bean. Deploy it on the J2EE
   * server. Check session synchronization life cycle call flow occurs. This
   * tests a non-committed transaction (rollback). RequiresNew transaction
   * attribute set.
   *
   */

  public void test9a() throws Fault {
    try {
      cRef.reset();
      logMsg(
          "RequiresNew: Testing transaction notification synchronization - rollback");
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(cRef);
      logMsg("initialize remote logging");
      beanRef.initLogging(props);
      logMsg("check for proper session synchronization lifecycle call order");
      boolean pass = beanRef.isSyncLifeCycle2("TxRequiresNew", true, false);
      beanRef.remove();
      if (!pass)
        throw new Fault("test9a failed");
    } catch (Exception e) {
      throw new Fault("test9a failed", e);
    }
  }

  /*
   * @testName: test10
   * 
   * @assertion_ids: EJB:SPEC:62
   * 
   * @test_Strategy: Create a stateful Session Bean. Deploy it on the J2EE
   * server. Check session synchronization life cycle call flow occurs. Test a
   * committed transaction. Supports transaction attribute set.
   *
   */

  public void test10() throws Fault {
    try {
      cRef.reset();
      logMsg("Supports: Testing transaction - commit");
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(cRef);
      logMsg("initialize remote logging");
      beanRef.initLogging(props);
      logMsg("check for transaction commit");
      beanRef.beginTransaction();
      boolean pass = beanRef.okay("TxSupports");
      boolean status = beanRef.commitTransaction();
      beanRef.remove();
      if (!pass || !status)
        throw new Fault("test10 failed");
    } catch (Exception e) {
      throw new Fault("test10 failed", e);
    }
  }

  /*
   * @testName: test11
   * 
   * @assertion_ids: EJB:SPEC:62
   * 
   * @test_Strategy: Create a stateful Session Bean. Deploy it on the J2EE
   * server. Check session synchronization life cycle call flow occurs. Test for
   * non-committed transaction (rollback). Supports transaction attribute set.
   *
   */

  public void test11() throws Fault {
    try {
      cRef.reset();
      logMsg("Supports: transaction - rollback");
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(cRef);
      logMsg("initialize remote logging");
      beanRef.initLogging(props);
      logMsg("check for transaction rollback");
      beanRef.beginTransaction();
      boolean pass = beanRef.not_okay("TxSupports");
      boolean status = beanRef.commitTransaction();
      beanRef.remove();
      if (!pass || status)
        throw new Fault("test11 failed");
    } catch (Exception e) {
      throw new Fault("test11 failed", e);
    }
  }

  /*
   * @testName: test12
   * 
   * @assertion_ids: EJB:SPEC:62
   * 
   * @test_Strategy: Create a stateful Session Bean. Deploy it on the J2EE
   * server. Check session synchronization life cycle call flow occurs.
   * NotSupported transaction attribute set.
   *
   */

  public void test12() throws Fault {
    try {
      cRef.reset();
      logMsg("NotSupported: ensure no transaction");
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(cRef);
      logMsg("initialize remote logging");
      beanRef.initLogging(props);
      logMsg("check to ensure no transaction");
      boolean pass = beanRef.okay("TxNotSupported");
      beanRef.remove();
      if (!pass)
        throw new Fault("test12 failed");
    } catch (Exception e) {
      throw new Fault("test12 failed", e);
    }
  }

  /*
   * @testName: test13
   * 
   * @assertion_ids: EJB:SPEC:62
   * 
   * @test_Strategy: Create a stateful Session Bean. Deploy it on the J2EE
   * server. Check session synchronization life cycle call flow occurs. Never
   * transaction attribute set.
   *
   */

  public void test13() throws Fault {
    try {
      cRef.reset();
      logMsg("Never: ensure no transaction");
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(cRef);
      logMsg("initialize remote logging");
      beanRef.initLogging(props);
      logMsg("check to ensure no transaction");
      boolean pass = beanRef.okay("TxNever");
      beanRef.remove();
      if (!pass)
        throw new Fault("test13 failed");
    } catch (Exception e) {
      throw new Fault("test13 failed", e);
    }
  }

  /*
   * @testName: test14
   * 
   * @assertion_ids: EJB:SPEC:62
   * 
   * @test_Strategy: Create a stateful Session Bean. Deploy it on the J2EE
   * server. Check session synchronization life cycle call flow occurs. This
   * tests a committed transaction. Mandatory transaction attribute set.
   *
   */

  public void test14() throws Fault {
    try {
      cRef.reset();
      logMsg(
          "Mandatory: Testing transaction notification synchronization - commit");
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(cRef);
      logMsg("initialize remote logging");
      beanRef.initLogging(props);
      logMsg("check for proper session synchronization lifecycle call order");
      beanRef.beginTransaction();
      boolean pass = beanRef.isSyncLifeCycle1("TxMandatory");
      beanRef.commitTransaction();
      beanRef.remove();
      if (!pass)
        throw new Fault("test14 failed");
    } catch (Exception e) {
      throw new Fault("test14 failed", e);
    }
  }

  /*
   * @testName: test15
   * 
   * @assertion_ids: EJB:SPEC:62
   * 
   * @test_Strategy: Create a stateful Session Bean. Deploy it on the J2EE
   * server. Check session synchronization life cycle call flow occurs. This
   * tests a non-committed transaction (rollback). Mandatory transaction
   * attribute set.
   */

  public void test15() throws Fault {
    try {
      cRef.reset();
      logMsg(
          "Mandatory: Testing transaction notification synchronization - rollback");
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(cRef);
      logMsg("initialize remote logging");
      beanRef.initLogging(props);
      logMsg("check for proper session synchronization lifecycle call order");
      beanRef.beginTransaction();
      boolean pass = beanRef.isSyncLifeCycle2("TxMandatory", false, false);
      beanRef.commitTransaction();
      beanRef.remove();
      if (!pass)
        throw new Fault("test15 failed");
    } catch (Exception e) {
      throw new Fault("test15 failed", e);
    }
  }

  /*
   * @testName: test16
   * 
   * @assertion_ids: EJB:SPEC:64; EJB:SPEC:65
   * 
   * @test_Strategy: Create a stateful Session Bean. Deploy it on the J2EE
   * server. Check session synchronization life cycle call flow occurs. This
   * tests a committed transaction. Required transaction attribute set.
   *
   */

  public void test16() throws Fault {
    try {
      cRef.reset();
      logMsg(
          "Required: Testing transaction notification synchronization - commit");
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(cRef);
      logMsg("initialize remote logging");
      beanRef.initLogging(props);
      logMsg("check for proper session synchronization lifecycle call order");
      beanRef.beginTransaction();
      boolean pass = beanRef.isSyncLifeCycle1("TxRequired");
      beanRef.commitTransaction();
      beanRef.remove();
      if (!pass)
        throw new Fault("test16 failed");
    } catch (Exception e) {
      throw new Fault("test16 failed", e);
    }
  }

  /*
   * @testName: test17
   * 
   * @assertion_ids: EJB:SPEC:64; EJB:SPEC:65
   * 
   * @test_Strategy: Create a stateful Session Bean. Deploy it on the J2EE
   * server. Check session synchronization life cycle call flow occurs. This
   * tests a non-committed transaction (rollback). Required transaction
   * attribute set.
   *
   */

  public void test17() throws Fault {
    try {
      cRef.reset();
      logMsg(
          "Required: Testing transaction notification synchronization - rollback");
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(cRef);
      logMsg("initialize remote logging");
      beanRef.initLogging(props);
      logMsg("check for proper session synchronization lifecycle call order");
      beanRef.beginTransaction();
      boolean pass = beanRef.isSyncLifeCycle2("TxRequired", false, false);
      beanRef.commitTransaction();
      beanRef.remove();
      if (!pass)
        throw new Fault("test17 failed");
    } catch (Exception e) {
      throw new Fault("test17 failed", e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }
}
