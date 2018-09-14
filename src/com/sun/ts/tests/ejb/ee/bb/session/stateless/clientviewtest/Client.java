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
 * @(#)Client.java	1.22 03/05/16
 */

package com.sun.ts.tests.ejb.ee.bb.session.stateless.clientviewtest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.util.*;
import javax.ejb.*;
import javax.transaction.*;
import javax.rmi.PortableRemoteObject;
import java.rmi.*;

import com.sun.javatest.Status;

public class Client extends EETest {
  private static final String testName = "ClientViewTest";

  private static final String testBean = "java:comp/env/ejb/TestBean";

  private TestBean beanRef = null;

  private Properties props = new Properties();

  private TestBeanHome beanHome = null;

  private TSNamingContext nctx = null;

  private boolean setupOK = false;

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
      logMsg("Obtain naming context");
      nctx = new TSNamingContext();

      // Get EJB Home ...
      logMsg("Looking up home interface for EJB: " + testBean);
      beanHome = (TestBeanHome) nctx.lookup(testBean, TestBeanHome.class);
      setupOK = true;
      logMsg("Setup ok");
    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }

  }

  /* Run test */

  /*
   * @testName: test1
   * 
   * @assertion_ids: EJB:SPEC:15
   * 
   * @test_Strategy: Create a stateless Session Bean. Deploy it on the J2EE
   * server. Perform lookup of home interface via JNDI.
   *
   */

  public void test1() throws Fault {
    boolean pass = false;
    try {
      // Get EJB Home ...
      logMsg("Looking up home in test1");
      beanHome = (TestBeanHome) nctx.lookup(testBean, TestBeanHome.class);
      if (beanHome != null)
        pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception test1: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("test1 failed : " + e);
    }

    if (!pass)
      throw new Fault("test1 failed");
  }

  /*
   * @testName: test2
   * 
   * @assertion_ids: EJB:SPEC:19.1
   * 
   * @test_Strategy: Create a stateless session EJB via the EJB home interface.
   * Deploy it on the J2EE server.
   */

  public void test2() throws Fault {

    boolean pass = false;

    try {
      TestUtil.logMsg("Create EJB instance test2");
      TestBean beanRef = (TestBean) beanHome.create();
      beanRef.initLogging(props);
      beanRef.Ping("ping em");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception test2: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("test2 failed", e);
    }

    if (!pass)
      throw new Fault("test2 failed");
  }

  /*
   * @testName: test3
   * 
   * @assertion_ids: EJB:SPEC:19.2; EJB:SPEC:23
   * 
   * @test_Strategy: Create a stateless session EJB. Deploy it on the J2EE
   * server. Obtain handle, then remove object.
   * 
   */
  public void test3() throws Fault {
    boolean pass = false;

    try {
      TestUtil.logMsg("Create EJB instance test3");
      TestBean beanRef = (TestBean) beanHome.create();
      beanRef.initLogging(props);

      // Get object handle and remove instance
      TestUtil.logMsg("Get object handle for bean instance");
      Handle handle = beanRef.getHandle();
      TestUtil.logMsg("This is before the handle print");
      TestUtil.logMsg("handle=" + handle);

      if (handle == null) {
        TestUtil.logErr("handle for EJB is null");
        throw new Fault("test3 failed");
      } else {
        TestUtil.logMsg("remove bean via handle");
        beanHome.remove(handle);
        pass = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception test3: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("test3 failed : " + e);
    }
    if (!pass)
      throw new Fault("test3 failed");
  }

  /*
   * @testName: test4
   * 
   * @assertion_ids: EJB:SPEC:39
   * 
   * @test_Strategy: Create a stateless Session Bean. Deploy it on the J2EE
   * server. Try to Obtain primaryKey. Expect a RemoteException.
   *
   */

  public void test4() throws Fault {
    boolean pass = false;

    try {

      // create EJB instance
      TestUtil.logMsg("Create EJB instance test4");
      TestBean beanRef = (TestBean) beanHome.create();
      beanRef.initLogging(props);

      try {
        TestUtil.logMsg("This is before the beanRef.getPrimaryKey() ");
        Object primaryKey = beanRef.getPrimaryKey();
        pass = false;
      } catch (RemoteException e) {
        TestUtil.logMsg("Caught RemoteException test4 as expected: " + e);
        pass = true;
      }

      if (!pass)
        throw new Fault("test4 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception test4: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("test4 failed", e);
    }
  }

  /*
   * @testName: test5
   * 
   * @assertion_ids: EJB:SPEC:25
   * 
   * @test_Stratregy: Create a stateless Session Bean. Deploy it on the J2EE
   * server. Try to Obtain primary Key. Try to remove the object via the primary
   * key. Check to ensure RemoveException occurs.
   *
   */

  public void test5() throws Fault {
    boolean pass = false;

    try {

      // create EJB instance
      TestUtil.logMsg("Create EJB instance test");
      TestBean beanRef = (TestBean) beanHome.create();
      beanRef.initLogging(props);

      try {
        TestUtil.logMsg("This is before the beanRef.remove(primaryKey) ");
        Integer primaryKey = new Integer(1);
        beanHome.remove(primaryKey);
        pass = false;
      } catch (RemoveException e) {
        TestUtil.logMsg("Caught RemoveException as expected in test5: " + e);
        pass = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logMsg("Unexpected exception: " + e);
        pass = false;
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception test5: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("test5 failed", e);
    }

    if (!pass)
      throw new Fault("test5 failed");
  }

  /*
   * @testName: test6
   * 
   * @assertion_ids: EJB:SPEC:19.3
   * 
   * @test_Strategy: Obtain EJBMetaData via the EJBHome interface Verify
   * EJBMetaData was obtained.
   *
   */
  public void test6() throws Fault {
    boolean pass = false;

    try { // create EJB instance
      TestUtil.logMsg("Create EJB instance test6");
      TestBean beanRef = (TestBean) beanHome.create();
      beanRef.initLogging(props);
      // Get EJBMetaData interface
      TestUtil.logMsg("Get EJBMetaData interface");
      EJBMetaData metaData = beanHome.getEJBMetaData();
      TestUtil.logMsg("metaData=" + metaData);
      if (metaData != null)
        pass = true;
      else {
        TestUtil.logErr("EJBMetaData interface is null");
        pass = false;
      }

      if (!pass)
        throw new Fault("test6 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception test6: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("test6 failed", e);
    }
  }

  /*
   * @testName: test6a
   * 
   * @assertion_ids: EJB:SPEC:19.4
   * 
   * @test_Strategy: Obtain HomeHandle via the EJBHome interface Verify
   * HomeHandle was obtained.
   *
   *
   */
  public void test6a() throws Fault {
    boolean pass = false;
    TestBean beanRef = null;

    try {
      TestUtil.logMsg("Get HomeHandle");
      HomeHandle homeHandle = beanHome.getHomeHandle();
      if (homeHandle != null)
        pass = true;
      else {
        TestUtil.logErr("homeHandle is null");
        pass = false;
      }

      if (!pass)
        throw new Fault("test6a failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception test6a: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("test6a failed", e);
    }
  }

  /*
   * @testName: test7
   * 
   * @assertion_ids: EJB:SPEC:35
   * 
   * @test_Strategy: Create a stateless Session EJBean. Deploy it on the J2EE
   * server. Obtain the EJBHome Interface. Verify the EJBHome Interface was
   * created.
   */
  public void test7() throws Fault {
    boolean pass = false;

    try {

      // create EJB instance
      TestUtil.logMsg("Create EJB instance test7");
      TestBean beanRef = (TestBean) beanHome.create();
      beanRef.initLogging(props);

      // Run true test
      TestUtil.logMsg("This is before the beanRef.getEJBHome() ");

      EJBHome beanHome2 = beanRef.getEJBHome();

      TestUtil.logMsg("This is after the beanRef.getEJBHome() ");

      if (beanHome2 != null)
        pass = true;
      else {
        TestUtil.logErr("Home Interface is null");
        pass = false;
      }

      if (!pass)
        throw new Fault("test7 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception test7: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("test7 failed", e);
    }
  }

  /*
   * @testName: test8
   * 
   * @assertion_ids: EJB:SPEC:42.3
   * 
   * @test_Strategy: Create a stateless Session EJBean. Deploy it on the J2EE
   * server. Obtain handle via the EJBObject, Verify the handle was obtained.
   */

  public void test8() throws Fault {

    boolean pass = false;

    try {

      // create EJB instance
      TestUtil.logMsg("Create EJB instance test8");
      TestBean beanRef = (TestBean) beanHome.create();
      beanRef.initLogging(props);

      // Run true test
      TestUtil.logMsg("This is before the beanRef.getHandle() ");
      Handle handle = beanRef.getHandle();
      TestUtil.logMsg("This is after the beanRef.getHandle() ");

      TestUtil.logMsg("handle= " + handle);

      if (handle != null)
        pass = true;
      else {
        TestUtil.logErr("handle is null");
        pass = false;
      }

      if (!pass)
        throw new Fault("test8 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception test8: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("test8 failed", e);
    }
  }

  /*
   * @testName: test9
   * 
   * @assertion_ids: EJB:SPEC:42.5
   * 
   * @test_Stratregy: Create a stateless Session EJBean. Deploy it on the J2EE
   * server. Remove the Object via the EJBObject.
   */

  public void test9() throws Fault {

    boolean pass = false;

    try {

      // create EJB instance
      TestUtil.logMsg("Create EJB instance test9");
      TestBean beanRef = (TestBean) beanHome.create();
      beanRef.initLogging(props);

      // Run true test
      TestUtil.logMsg("This is before the beanRef.remove() ");
      beanRef.remove();
      TestUtil.logMsg("This is after the beanRef.remove() ");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception test9: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("test9 failed : " + e);
    }
    if (!pass)
      throw new Fault("test9 failed");

  }

  /*
   * @testName: test10
   * 
   * @assertion_ids: EJB:SPEC:49
   * 
   * @test_Strategy: Create a stateless Session EJBean. Deploy it on the J2EE
   * server. Call the isIdentical(SameObject). verify that the result is true.
   */

  public void test10() throws Fault {

    boolean pass = false;

    try {

      // create EJB instance
      TestUtil.logMsg("Create beanRef1 instance test10");
      TestBean beanRef = (TestBean) beanHome.create();

      beanRef.initLogging(props);

      TestUtil.logMsg(
          "SubTest1: Getting ready to do the compare of 2 objects that are identical");

      if (beanRef.isIdentical(beanRef)) {
        TestUtil.logMsg("The EJBObjects are Identical");
        pass = true;
      } else {
        TestUtil.logMsg("The EJBObjects are not Identical");
        pass = false;
      }

      if (!pass)
        throw new Fault("test10 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception test10: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("test10 failed", e);
    }
  }

  /*
   * @testName: test11
   * 
   * @assertion_ids: EJB:SPEC:49
   * 
   * @test_Strategy: Create a stateless Session EJBean. Deploy it on the J2EE
   * server. Call the isIdentical(newObject). verify that the objects are
   * identical.
   */

  public void test11() throws Fault {

    boolean pass = false;

    try {

      // create EJB instance
      TestUtil.logMsg("Create beanRef1 instance test11");
      TestBean beanRef1 = (TestBean) beanHome.create();

      beanRef1.initLogging(props);

      TestUtil.logMsg("Create beanRef2 instance test11");
      TestBean beanRef2 = (TestBean) beanHome.create();
      beanRef2.initLogging(props);

      TestUtil.logMsg(
          "SubTest1: Getting ready to do the compare 2 different objects");

      if (beanRef1.isIdentical(beanRef2)) {
        TestUtil.logMsg("The EJBObjects are Identical");
        pass = true;
      } else {
        TestUtil.logMsg("The EJBObjects are not Identical");
        pass = false;
      }

      if (!pass)
        throw new Fault("test11 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception test11: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("test11 failed", e);
    }
  }

  /*
   * @testName: test12
   * 
   * @assertion_ids: EJB:SPEC:42.1
   * 
   * @test_Strategy: Create a stateless Session EJBean. Deploy it on the J2EE
   * server. Call objects Business Method. Verify that a exception isn't raised
   */

  public void test12() throws Fault {

    boolean pass = false;

    try {
      // create EJB instance
      TestUtil.logMsg("Create EJB instance test12");
      TestBean beanRef = (TestBean) beanHome.create();
      beanRef.initLogging(props);

      // Run the true test
      TestUtil.logMsg("This is before the beanRef.businessMethod() ");
      TestUtil.logMsg(beanRef.Ping("This is the call to the businessMethod()"));
      TestUtil.logMsg("This is after the beanRef.businessMethod() ");

      pass = true;

    } catch (Exception e) {
      TestUtil.logErr("Caught exception test12: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("test12 failed", e);
    }
  }

  public TestBean echoMethod(TestBean tb) {
    try {
      TestUtil.logMsg(tb.Ping("Test Ping"));
      return tb;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception in echoMethod : " + e);
      TestUtil.printStackTrace(e);
      return null;
    }
  }

  /*
   * @testName: test13
   * 
   * @assertion_ids: EJB:SPEC:42.4
   * 
   * @test_Strategy: Create a stateless Session EJBean. Deploy it on the J2EE
   * server. Call the objects business method with and instance of itself as the
   * parameter.
   * 
   */

  public void test13() throws Fault {

    boolean pass = false;

    try {
      // create EJB instance
      TestUtil.logMsg("Create EJB instance test13");
      TestBean beanRef = (TestBean) beanHome.create();
      beanRef.initLogging(props);

      // Run the true test
      TestUtil.logMsg("This is before the echoMethod(EJBObject) ");
      pass = beanRef.isIdentical(echoMethod(beanRef));
      TestUtil.logMsg("This is after the echoMethod(EJBObject) := " + pass);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception test13: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("test13 failed", e);
    }
  }

  /*
   * @testName: test14
   * 
   * @assertion_ids: EJB:SPEC:19.3
   * 
   * @test_Strategy: Obtain EJBMetaData Obtain EJBHome interface Verify EJBHome
   * interface was obtained
   */

  public void test14() throws Fault {
    boolean pass = false;

    try {

      TestUtil.logMsg("Before the call to get MetaData() test14!");
      EJBMetaData metaData = beanHome.getEJBMetaData();
      TestUtil.logMsg("metaData=" + metaData);

      // Run the true test
      TestUtil.logMsg("This is before the metaData.getEJBHome()");

      // TestBeanHome home = (TestBeanHome)metaData.getEJBHome();
      EJBHome home = metaData.getEJBHome();
      TestUtil.logMsg("This is after the metaData.getEJBHome() ");

      if (home != null)
        pass = true;
      else {
        TestUtil.logMsg("Home was null");
        pass = false;
      }

      if (!pass)
        throw new Fault("test14 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception test14: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("test14 failed", e);
    }
  }

  /*
   * @testName: test15
   * 
   * @assertion_ids: EJB:SPEC:19.3
   * 
   * @test_Strategy: Obtain metaData from beanHome Obtain EJB home Interface
   * Verify that EJB home Interface was obtained
   */

  public void test15() throws Fault {
    boolean pass = false;

    try {

      TestUtil.logMsg("Before the call to get MetaData() test15!");
      EJBMetaData metaData = beanHome.getEJBMetaData();
      TestUtil.logMsg("metaData=" + metaData);

      // Run the true test
      TestUtil.logMsg("Before the metaData.getHomeInterfaceClass()");
      Class cls = metaData.getHomeInterfaceClass();

      TestUtil.logMsg("After the metaData.getHomeInterfaceClass() ");

      TestUtil.logMsg("class=" + cls);

      if (cls != null)
        pass = true;
      else {
        TestUtil.logMsg("The getHomeInterfaceClass returned null");
        pass = false;
      }

      if (!pass)
        throw new Fault("test15 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception test15: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("test15 failed", e);
    }
  }

  /*
   * @testName: test16
   * 
   * @assertion_ids: EJB:SPEC:41
   * 
   * @test_Strategy: Obtain EJBMetaData Obtain PrimaryKeyClass via EJBMetaData.
   * Verify that RuntimeException occurred.
   */

  public void test16() throws Fault {
    boolean pass = false;

    try {

      TestUtil.logMsg("Before the call to get MetaData() test16!");
      EJBMetaData metaData = beanHome.getEJBMetaData();
      TestUtil.logMsg("metaData=" + metaData);

      // Run the true test
      TestUtil.logMsg("Before the metaData.getPrimaryKeyClass()");

      try {

        Class cls = metaData.getPrimaryKeyClass();

        TestUtil.logMsg("After the metaData.getPrimaryKeyClass() ");

        TestUtil.logMsg("class=" + cls);

        TestUtil.logMsg(
            "Test failed because SessionBeans shouldn't be able to get primaryKeyClass");

      } catch (RuntimeException e) {
        TestUtil.logMsg("Caught exception as expected test16 : " + e);
        pass = true;
      } catch (Exception e) {
        TestUtil.logMsg("Caught exception test16 : " + e);
        TestUtil.printStackTrace(e);
      }
    } catch (RemoteException e) {
      TestUtil.logErr("Caught exception test16: " + e);
      TestUtil.printStackTrace(e);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception test16: " + e);
      TestUtil.printStackTrace(e);
    } finally {
      if (!pass)
        throw new Fault("test16 failed");
    }
  }

  /*
   * @testName: test17
   * 
   * @assertion_ids: EJB:SPEC:22
   * 
   * @test_Strategy: Create a stateless Session EJBean. Deploy it on the J2EE
   * server. Obtain handle, serialize/deserialize handle, invoke bean object
   * with deserialized handle.
   */

  public void test17() throws Fault {
    boolean pass = false;

    try {
      TestUtil.logMsg("Before the call to get MetaData() test17!");
      EJBMetaData metaData = beanHome.getEJBMetaData();
      TestUtil.logMsg("metaData=" + metaData);

      // Run the true test
      TestUtil.logMsg("Before the metaData.getRemoteInterfaceClass()");
      Class cls = metaData.getRemoteInterfaceClass();

      TestUtil.logMsg("After the metaData.getRemoteInterfaceClass() ");

      TestUtil.logMsg("class=" + cls);
      if (cls != null)
        pass = true;
      else {
        TestUtil.logMsg("getRemoteInterfaceClass Returned a null value test17");
        pass = false;
      }

      if (!pass)
        throw new Fault("test17 failed");

    } catch (RemoteException e) {
      TestUtil.logErr("Caught exception test17: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("test17 failed", e);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception test17: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("test17 failed", e);
    }
  }

  /*
   * @testName: test18
   * 
   * @assertion_ids: EJB:SPEC:21; EJB:JAVADOC:72
   * 
   * @test_Strategy: Obtain EJBMetaData Call isStatelessSession on a Stateless
   * SessionBean Verify that the result is true
   */

  public void test18() throws Fault {

    boolean pass = false;

    try {
      TestUtil.logMsg("Before the call to get MetaData() test18!");
      EJBMetaData metaData = beanHome.getEJBMetaData();
      TestUtil.logMsg("metaData=" + metaData);

      // Run the true test
      TestUtil.logMsg("This is before the metaData.isStatelessSession()");
      boolean isStatelessSession = metaData.isStatelessSession();

      TestUtil.logMsg("This is after the metaData.isStatelessSession() ");

      TestUtil.logMsg("isStatelessSession =" + isStatelessSession);
      pass = isStatelessSession;

      if (!pass)
        throw new Fault("test18 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception test18: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("test18 failed", e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

}
