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
 * @(#)Client.java	1.28 03/05/16
 */

package com.sun.ts.tests.ejb.ee.bb.entity.cmp.clientviewtest;

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

  private static final String testProps = "clientviewtest.properties";

  private static final String testDir = System.getProperty("user.dir");

  private TestBean beanRef = null;

  private Properties props = new Properties();

  private TestBeanHome beanHome = null;

  private TSNamingContext nctx = null;

  private boolean setupOK = false;

  private static final int NUMEJBS = 5;

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
   * @assertion_ids: EJB:SPEC:125
   * 
   * @test_Strategy: Create an CMP 1.1 Entity Bean. Deploy it on the J2EE
   * server. Ensure the client can access EJBHome interface for EJB via JNDI
   *
   */

  public void test1() throws Fault {
    boolean pass = true;
    try {
      // Get EJB Home ...
      logMsg("Looking up home in test1");
      beanHome = (TestBeanHome) nctx.lookup(testBean, TestBeanHome.class);

      if (beanHome == null)
        pass = false;
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
   * @assertion_ids: EJB:SPEC:126.1
   * 
   * @test_Strategy: Create an CMP 1.1 Entity EJB via the EJBHome interface.
   * Deploy it on the J2EE server.
   */

  public void test2() throws Fault {

    boolean pass = true;
    TestBean beanRef = null;

    try {
      TestUtil.logMsg("Create EJB instance test2");
      beanRef = (TestBean) beanHome.create(props, 1, "coffee-1", 1);
      beanRef.ping("ping em");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception test2: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("test2 failed", e);
    } finally {
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
    }

    if (!pass)
      throw new Fault("test2 failed");
  }

  /*
   * @testName: test3
   * 
   * @assertion_ids: EJB:SPEC:126.3
   * 
   * @test_Strategy: Create an CMP 1.1 Entity EJBean. Deploy it on the J2EE
   * server. Obtain handle, then remove object, attempt make call to the removed
   * object
   * 
   */
  public void test3() throws Fault {
    boolean pass = false;
    TestBean beanRef = null;

    try {
      TestUtil.logMsg("Create EJB instance test3");
      beanRef = (TestBean) beanHome.create(props, 1, "coffee-1", 1);

      // Get object handle and remove instance
      TestUtil.logMsg("Get object handle for bean instance");
      Handle handle = beanRef.getHandle();
      TestUtil.logMsg("handle= " + handle);

      if (handle == null) {
        TestUtil.logErr("handle for EJB is null");
        throw new Fault("test3 failed");
      } else {
        TestUtil.logMsg("remove EJB via handle");
        beanHome.remove(handle);
        try {
          TestUtil.logMsg("Calling beanRef.ping()");
          beanRef.ping("ping em");
        } catch (NoSuchObjectException e) {
          TestUtil.logMsg(
              "Caught inner NoSuchObjectException in test3 as expected");
          pass = true;
        } catch (RemoteException e) {
          TestUtil.logMsg("RemoteException received as expected " + e);
          pass = true;
        } catch (Exception e) {
          TestUtil.logErr("Caught inner exception test3: " + e);
          TestUtil.printStackTrace(e);
          throw new Fault("test3 failed : " + e);
        }
      }

      if (!pass)
        throw new Fault("test3 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception test3: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("test3 failed : " + e);
    } finally {
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
    }
  }

  /*
   * @testName: test4
   * 
   * @assertion_ids: EJB:SPEC:148
   * 
   * @test_Strategy: Create an CMP 1.1 Entity Bean. Deploy it on the J2EE
   * server. Try to Obtain primaryKey.
   *
   */

  public void test4() throws Fault {
    boolean pass = false;
    TestBean beanRef = null;
    Object primaryKey = null;

    try {

      // create EJB instance
      TestUtil.logMsg("Create EJB instance test4");
      beanRef = (TestBean) beanHome.create(props, 1, "coffee-1", 1);

      try {
        TestUtil.logMsg("This is before the beanRef.getPrimaryKey() ");
        primaryKey = beanRef.getPrimaryKey();
        pass = true;
        TestUtil.logMsg("This is after the beanRef.getPrimaryKey() ");
      } catch (RemoteException e) {
        TestUtil.logErr("Caught RemoteException test4 unexpected: " + e, e);
        pass = false;
      }

      TestUtil.logMsg("Check if primary key object is proper class type");
      if (!(primaryKey instanceof Integer)) {
        TestUtil.logErr("primary key not an Integer object - unexpected");
        pass = false;
      } else {
        TestUtil.logMsg("primary key is an Integer object - expected");
        Integer i = (Integer) primaryKey;
        if (i.intValue() != 1) {
          TestUtil.logErr("primaryKey value is not 1 - unexpected");
          pass = false;
        } else {
          TestUtil.logMsg("primaryKey value is 1 - expected");
          pass = true;
        }
      }

      if (!pass)
        throw new Fault("test4 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception test4: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("test4 failed", e);
    } finally {
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
    }
  }

  /*
   * @testName: test5
   * 
   * @assertion_ids: EJB:SPEC:126.3
   * 
   * @test_Strategy: Create an CMP 1.1 Entity Bean. Deploy it on the J2EE
   * server. Try to Obtain primary Key. Try to remove the object via the primary
   * key. Check to see if object still exists.
   *
   */

  public void test5() throws Fault {
    boolean pass = false;
    TestBean beanRef = null;

    try {

      // create EJB instance
      TestUtil.logMsg("Create EJB instance test5");
      beanRef = (TestBean) beanHome.create(props, 1, "coffee-1", 1);

      beanRef.initLogging(props);

      try {
        TestUtil.logMsg("This is before the beanRef.getPrimaryKey()");
        Object primaryKey = beanRef.getPrimaryKey();
        TestUtil.logMsg("This is before the beanRef.remove(primaryKey)");
        beanHome.remove(primaryKey);

        try {
          TestUtil.logMsg("This is before the ping of removed method");
          beanRef.ping("Is Bean still alive");
          pass = false;
        } catch (NoSuchObjectException e) {
          TestUtil.logMsg(
              "Caught inner NoSuchObjectException in test5 as expected");
          pass = true;
        } catch (RemoteException e) {
          TestUtil.logMsg("RemoteException received as expected " + e);
          pass = true;
        }
      } catch (Exception e) {
        TestUtil.logErr("Caught inner exception test5: " + e);
        TestUtil.printStackTrace(e);
        throw new Fault("test5 failed : " + e);
      }

      if (!pass)
        throw new Fault("test5 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception test5: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("test5 failed", e);
    } finally {
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
    }
  }

  /*
   * @testName: test6
   * 
   * @assertion_ids: EJB:SPEC:126.4
   * 
   * @test_Strategy: Obtain EJBMetaData via the EJBHome interface Verify
   * EJBMetaData was obtained.
   *
   */
  public void test6() throws Fault {
    boolean pass = false;
    TestBean beanRef = null;

    try {
      TestUtil.logMsg("Create EJB instance test6");
      beanRef = (TestBean) beanHome.create(props, 1, "coffee-1", 1);
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
    } finally {
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
    }

  }

  /*
   * @testName: test6a
   * 
   * @assertion_ids: EJB:SPEC:126.5; EJB:JAVADOC:47
   * 
   * @test_Strategy: Obtain HomeHandle via the EJBHome interface Verify
   * HomeHandle was obtained.
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
   * @assertion_ids: EJB:SPEC:152.1; EJB:JAVADOC:22
   * 
   * @test_Strategy: Create an CMP 1.1 Entity EJBean. Deploy it on the J2EE
   * server. Obtain the EJBHome Interface. Varify the EJBHome Interface was
   * created.
   */
  public void test7() throws Fault {
    boolean pass = false;
    TestBean beanRef = null;

    try {

      // create EJB instance
      TestUtil.logMsg("Create EJB instance test7");
      beanRef = (TestBean) beanHome.create(props, 1, "coffee-1", 1);

      // Run true test
      TestUtil.logMsg("This is before the beanRef.getEJBHome() ");

      EJBHome beanHome2 = (EJBHome) beanRef.getEJBHome();

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
    } finally {
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
    }
  }

  /*
   * @testName: test8
   * 
   * @assertion_ids: EJB:SPEC:152.3; EJB:JAVADOC:75
   * 
   * @test_Strategy: Create an CMP 1.1 Entity EJBean. Deploy it on the J2EE
   * server. Obtain handle via the EJBObject, Verify the handle was obtained.
   */

  public void test8() throws Fault {

    boolean pass = false;
    TestBean beanRef = null;

    try {

      // create EJB instance
      TestUtil.logMsg("Create EJB instance test8");
      beanRef = (TestBean) beanHome.create(props, 1, "coffee-1", 1);

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
    } finally {
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
    }
  }

  /*
   * @testName: test9
   * 
   * @assertion_ids: EJB:SPEC:152.2
   * 
   * @test_Strategy: Create an CMP 1.1 Entity EJBean. Deploy it on the J2EE
   * server. Remove the Object via the EJBObject. Varify the object no longer
   * exists.
   */

  public void test9() throws Fault {

    boolean pass = false;
    TestBean beanRef = null;

    try {

      // create EJB instance
      TestUtil.logMsg("Create EJB instance test9");
      beanRef = (TestBean) beanHome.create(props, 1, "coffee-1", 1);

      // Run true test
      TestUtil.logMsg("This is before the beanRef.remove() ");
      beanRef.remove();
      TestUtil.logMsg("This is after the beanRef.remove() ");

      beanRef.ping("ping em");

    } catch (NoSuchObjectException e) {
      TestUtil.logMsg("Caught NoSuchObjectException in test9 as expected");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("test9 failed : " + e);
    } finally {
      try {
        beanRef.remove();
        if (!pass)
          throw new Fault("test9 failed");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
    }

  }

  /*
   * @testName: test10
   * 
   * @assertion_ids: EJB:SPEC:150; EJB:JAVADOC:79
   * 
   * @test_Strategy: Create a CMP 1.1 Entity EJBean. Deploy it on the J2EE
   * server. Call the isIdentical(SameObject). verify that the result is true.
   */

  public void test10() throws Fault {

    boolean pass = false;
    TestBean beanRef = null;

    try {

      // create EJB instance
      TestUtil.logMsg("Create beanRef instance test10");
      beanRef = (TestBean) beanHome.create(props, 1, "coffee-1", 1);

      TestUtil.logMsg(
          "Getting ready to do the compare of 2 objects that are identical");
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
    } finally {
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
    }
  }

  /*
   * @testName: test11
   * 
   * @assertion_ids: EJB:SPEC:150; EJB:JAVADOC:79
   * 
   * @test_Strategy: Create an CMP 1.1 Entity EJBean. Deploy it on the J2EE
   * server. Call the isIdentical(newObject). verify that the result is false.
   */

  public void test11() throws Fault {

    boolean pass = false;

    TestBean beanRef2 = null;
    TestBean beanRef1 = null;

    try {

      // create EJB instance
      TestUtil.logMsg("Create beanRef1 instance test11");
      beanRef1 = (TestBean) beanHome.create(props, 1, "coffee-1", 1);

      TestUtil.logMsg("Create beanRef2 instance test11");
      beanRef2 = (TestBean) beanHome.create(props, 2, "coffee-2", 2);

      TestUtil.logMsg("Getting ready to do the compare 2 different objects");

      if (beanRef1.isIdentical(beanRef2)) {
        TestUtil.logMsg("The EJBObjects are Identical");
        pass = false;
      } else {
        TestUtil.logMsg("The EJBObjects are not Identical");
        pass = true;
      }

      if (!pass)
        throw new Fault("test11 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception test11: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("test11 failed", e);
    } finally {
      try {
        beanRef1.remove();
        beanRef2.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
    }
  }

  /*
   * @testName: test12
   * 
   * @assertion_ids: EJB:SPEC:144.1
   * 
   * @test_Strategy: Create an CMP 1.1 Entity EJBean. Deploy it on the J2EE
   * server. Call objects Business Method. verify that a exception isn't raised
   */

  public void test12() throws Fault {

    boolean pass = false;
    TestBean beanRef = null;

    try {
      // create EJB instance
      TestUtil.logMsg("Create EJB instance test12");
      beanRef = (TestBean) beanHome.create(props, 1, "coffee-1", 1);

      // Run the true test
      TestUtil.logMsg("This is before the beanRef.businessMethod() ");
      TestUtil.logMsg(beanRef
          .ping("This is the message inside the beanRef.businessMethod() "));
      TestUtil.logMsg("This is after the beanRef.businessMethod() ");

      pass = true;

    } catch (Exception e) {
      TestUtil.logErr("Caught exception test12: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("test12 failed", e);
    } finally {
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
    }
  }

  public TestBean echoMethod(TestBean tb) {
    try {
      TestUtil.logMsg(tb.ping("Test Ping"));
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
   * @assertion_ids: EJB:SPEC:144.3
   * 
   * @test_Strategy: Create an CMP 1.1 Entity EJBean. Deploy it on the J2EE
   * server. Call the objects business method with and instance of itself as the
   * parameter.
   * 
   */

  public void test13() throws Fault {

    boolean pass = false;
    TestBean beanRef = null;

    try {
      // create EJB instance
      TestUtil.logMsg("Create EJB instance test13");
      beanRef = (TestBean) beanHome.create(props, 1, "coffee-1", 1);

      // Run the true test
      TestUtil.logMsg("This is before the echoMethod(EJBObject) ");
      pass = beanRef.isIdentical(echoMethod(beanRef));
      TestUtil.logMsg("This is after the echoMethod(EJBObject) := " + pass);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception test13: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("test13 failed", e);
    } finally {
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
    }
  }

  /*
   * @testName: test14
   * 
   * @assertion_ids: EJB:SPEC:126.4; EJB:JAVADOC:67
   * 
   * @test_Strategy: Obtain EJBMetaData Obtain EJBHome interface Verify EJBHome
   * interface was obtained
   */

  public void test14() throws Fault {
    boolean pass = false;
    TestBean beanRef = null;

    try {

      // create EJB instance
      TestUtil.logMsg("Create EJB instance test14");
      beanRef = (TestBean) beanHome.create(props, 1, "coffee-1", 1);

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
        TestUtil.logErr("Home is = null");
        pass = false;
      }

      if (!pass)
        throw new Fault("test14 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception test14: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("test14 failed", e);
    } finally {
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
    }
  }

  /*
   * @testName: test15
   * 
   * @assertion_ids: EJB:SPEC:126.4; EJB:JAVADOC:68
   * 
   * @test_Strategy: Obtain metaData from beanHome Obtain EJB home Interface
   * Verify that EJB home Interface was obtained
   */

  public void test15() throws Fault {
    boolean pass = false;
    TestBean beanRef = null;

    try {
      // create EJB instance
      TestUtil.logMsg("Create EJB instance test15");
      beanRef = (TestBean) beanHome.create(props, 1, "coffee-1", 1);

      TestUtil.logMsg("Before the call to get MetaData() test15!");
      EJBMetaData metaData = beanHome.getEJBMetaData();
      TestUtil.logMsg("metaData=" + metaData);

      // Run the true test
      TestUtil.logMsg("Before the metaData.getHomeInterfaceClass()");
      Class cls = metaData.getHomeInterfaceClass();

      TestUtil.logMsg("After the metaData.getHomeInterfaceClass() ");

      TestUtil.logMsg("class=" + cls);

      if (cls.isInstance(beanHome))
        pass = true;
      else {
        TestUtil.logErr("The getHomeInterfaceClass returned null");
        pass = false;
      }

      if (!pass)
        throw new Fault("test15 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception test15: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("test15 failed", e);
    } finally {
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
    }
  }

  /*
   * @testName: test16
   * 
   * @assertion_ids: EJB:SPEC:126.4; EJB:JAVADOC:69
   * 
   * @test_Strategy: Obtain EJBMetaData Obtain PrimaryKeyClass via EJBMetaData.
   * Varify that PrimaryKeyClass was obtained.
   */

  public void test16() throws Fault {
    boolean pass = false;
    TestBean beanRef = null;

    try {
      // create EJB instance
      TestUtil.logMsg("Create EJB instance test16");
      beanRef = (TestBean) beanHome.create(props, 1, "coffee-1", 1);

      TestUtil.logMsg("Before the call to get MetaData() test16!");
      EJBMetaData metaData = beanHome.getEJBMetaData();
      TestUtil.logMsg("metaData=" + metaData);

      // Run the true test
      TestUtil.logMsg("Before the metaData.getPrimaryKeyClass()");

      try {

        Class cls = metaData.getPrimaryKeyClass();

        TestUtil.logMsg("After the metaData.getPrimaryKeyClass() ");

        TestUtil.logMsg("class=" + cls);

        if (cls.isInstance(new Integer(1)))
          pass = true;

      } catch (RuntimeException e) {
        TestUtil.logErr("Caught RuntimeException in test16 unexpected: " + e,
            e);
      } catch (Exception e) {
        TestUtil.logErr("Caught Exception test16 : " + e);
        TestUtil.printStackTrace(e);
      }
    } catch (RemoteException e) {
      TestUtil.logErr("Caught RemoteException test16: " + e);
      TestUtil.printStackTrace(e);
    } catch (Exception e) {
      TestUtil.logErr("Caught Exception test16: " + e);
      TestUtil.printStackTrace(e);
    } finally {
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
    }
    if (!pass)
      throw new Fault("test16 failed");
  }

  /*
   * @testName: test17
   * 
   * @assertion_ids: EJB:SPEC:126.4; EJB:JAVADOC:70
   * 
   * @test_Strategy: Get MetaData from EJBHome Use MetaData to get ClassObject
   * Verify that the ClassObject was obtained.
   */

  public void test17() throws Fault {
    boolean pass = false;
    TestBean beanRef = null;

    try {
      // create EJB instance
      TestUtil.logMsg("Create EJB instance test17");
      beanRef = (TestBean) beanHome.create(props, 1, "coffee-1", 1);

      TestUtil.logMsg("Before the call to get MetaData() test17!");
      EJBMetaData metaData = beanHome.getEJBMetaData();
      TestUtil.logMsg("metaData=" + metaData);

      // Run the true test
      TestUtil.logMsg("Before the metaData.getRemoteInterfaceClass()");
      Class cls = metaData.getRemoteInterfaceClass();

      TestUtil.logMsg("After the metaData.getRemoteInterfaceClass() ");

      TestUtil.logMsg("class=" + cls);
      if (cls.isInstance(beanRef))
        pass = true;
      else {
        TestUtil.logErr("getRemoteInterfaceClass Returned a null value test17");
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
    } finally {
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
    }
  }

  /*
   * @testName: test18
   * 
   * @assertion_ids: EJB:SPEC:126.4; EJB:JAVADOC:71
   * 
   * @test_Strategy: Obtain EJBMetaData Call isSession on the CMP 1.1 Entity
   * Bean Varify that the result is false
   */

  public void test18() throws Fault {

    boolean pass = false;
    TestBean beanRef = null;

    try {
      // create EJB instance
      TestUtil.logMsg("Create EJB instance test18");
      beanRef = (TestBean) beanHome.create(props, 1, "coffee-1", 1);

      TestUtil.logMsg("Before the call to get MetaData() test18!");
      EJBMetaData metaData = beanHome.getEJBMetaData();
      TestUtil.logMsg("metaData=" + metaData);

      // Run the true test
      TestUtil.logMsg("This is before the metaData.isSession()");
      boolean isSession = metaData.isSession();

      TestUtil.logMsg("This is after the metaData.isSession() ");

      TestUtil.logMsg("isSession =" + isSession);
      pass = isSession;

      if (pass)
        throw new Fault("test18 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception test18: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("test18 failed", e);
    } finally {
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
    }
  }

  /*
   * @testName: test19
   * 
   * @assertion_ids: EJB:SPEC:133; EJB:SPEC:132
   * 
   * @test_Strategy: Create several CMP 1.1 Entity EJB's via the EJBHome
   * interface. Deploy them on the J2EE server. Perform a find of one of the CMP
   * 1.1 Entity EJB's via findByPrimaryKey. Verify that the correct CMP 1.1
   * Entity EJB was found.
   */

  public void test19() throws Fault {

    boolean pass = true;
    TestBean[] beanRef = new TestBean[NUMEJBS];
    TestBean beanRef2 = null;

    try {
      TestUtil.logMsg("Create EJB instances for test19");
      for (int i = 0, j = 1; i < NUMEJBS; i++, j++) {
        logMsg("Creating entity EJB #" + j + " with Primary Key = " + j
            + " and Price = " + j);
        beanRef[i] = (TestBean) beanHome.create(props, j, "coffee-" + j, j);
      }

      TestUtil.logMsg("Find EJB reference with Primary Key = 3");
      beanRef2 = (TestBean) beanHome.findByPrimaryKey(new Integer(3));
      TestUtil.logMsg("Check if we found the correct EJB reference");
      if (beanRef[2].isIdentical(beanRef2)) {
        TestUtil.logMsg("findByPrimaryKey returned correct reference");
        pass = true;
      } else {
        TestUtil.logErr("findByPrimaryKey returned incorrect reference");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception test19: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("test19 failed", e);
    } finally {
      try {
        for (int i = 0; i < NUMEJBS; i++)
          beanRef[i].remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
    }

    if (!pass)
      throw new Fault("test19 failed");
  }

  /*
   * @testName: test20
   * 
   * @assertion_ids: EJB:SPEC:470; EJB:SPEC:471
   * 
   * @test_Strategy: Create several CMP 1.1 Entity EJB's via the EJBHome
   * interface. Deploy them on the J2EE server. Perform a find of one of the CMP
   * 1.1 Entity EJB's and verify that the correct CMP 1.1 Entity EJB was found.
   */

  public void test20() throws Fault {

    boolean pass = true;
    TestBean[] beanRef = new TestBean[NUMEJBS];
    TestBean beanRef2 = null;

    try {
      TestUtil.logMsg("Create EJB instances for test20");
      for (int i = 0, j = 1; i < NUMEJBS; i++, j++) {
        logMsg("Creating entity EJB #" + j + " with Primary Key = " + j
            + " and Price = " + j);
        beanRef[i] = (TestBean) beanHome.create(props, j, "coffee-" + j, j);
      }

      TestUtil.logMsg("Find EJB reference with Coffee Name = coffee-3");
      Collection c = beanHome.findByName("coffee-3");
      TestUtil.logMsg("Number of EJB references returned = " + c.size());
      if (c.size() != 1) {
        TestUtil.logErr("findByName returned " + c.size()
            + " references, expected 1 reference");
        pass = false;
      } else {
        TestUtil.logMsg("Check if we found the correct EJB reference");
        Iterator i = c.iterator();
        beanRef2 = (TestBean) PortableRemoteObject.narrow(i.next(),
            TestBean.class);
        if (beanRef[2].isIdentical(beanRef2)) {
          TestUtil.logMsg("findByName returned correct reference");
          pass = true;
        } else {
          TestUtil.logErr("findByName returned incorrect reference");
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception test20: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("test20 failed", e);
    } finally {
      try {
        for (int i = 0; i < NUMEJBS; i++)
          beanRef[i].remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
    }

    if (!pass)
      throw new Fault("test20 failed");
  }

  /*
   * @testName: test21
   * 
   * @assertion_ids: EJB:SPEC:470; EJB:SPEC:471
   * 
   * @test_Strategy: Create several CMP 1.1 Entity EJB's via the EJBHome
   * interface. Deploy them on the J2EE server. Perform a find of one of the CMP
   * 1.1 Entity EJB's and verify that the correct CMP 1.1 Entity EJB was found.
   */

  public void test21() throws Fault {

    boolean pass = true;
    TestBean[] beanRef = new TestBean[NUMEJBS];
    TestBean beanRef2 = null;

    try {
      TestUtil.logMsg("Create EJB instances for test21");
      for (int i = 0, j = 1; i < NUMEJBS; i++, j++) {
        logMsg("Creating entity EJB #" + j + " with Primary Key = " + j
            + " and Price = " + j);
        beanRef[i] = (TestBean) beanHome.create(props, j, "coffee-" + j, j);
      }

      TestUtil.logMsg("Find EJB reference with Coffee Price = 3.0");
      Collection c = beanHome.findByPrice(3);
      TestUtil.logMsg("Number of EJB references returned = " + c.size());
      if (c.size() != 1) {
        TestUtil.logErr("findByPrice returned " + c.size()
            + " references, expected 1 reference");
        pass = false;
      } else {
        TestUtil.logMsg("Check if we found the correct EJB reference");
        Iterator i = c.iterator();
        beanRef2 = (TestBean) PortableRemoteObject.narrow(i.next(),
            TestBean.class);
        if (beanRef[2].isIdentical(beanRef2)) {
          TestUtil.logMsg("findByPrice returned correct reference");
          pass = true;
        } else {
          TestUtil.logErr("findByPrice returned incorrect reference");
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception test21: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("test21 failed", e);
    } finally {
      try {
        for (int i = 0; i < NUMEJBS; i++)
          beanRef[i].remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
    }

    if (!pass)
      throw new Fault("test21 failed");
  }

  /*
   * @testName: test22
   * 
   * @assertion_ids: EJB:SPEC:470; EJB:SPEC:471
   * 
   * @test_Strategy: Create several CMP 1.1 Entity EJB's via the EJBHome
   * interface. Deploy them on the J2EE server. Perform a find of a range of CMP
   * 1.1 Entity EJB's and Verify that the correct CMP 1.1 Entity EJB's were
   * found.
   */

  public void test22() throws Fault {

    boolean pass = true;
    TestBean[] beanRef = new TestBean[NUMEJBS];
    TestBean[] beanRef2 = new TestBean[NUMEJBS];

    try {
      TestUtil.logMsg("Create EJB instances for test22");
      for (int i = 0, j = 1; i < NUMEJBS; i++, j++) {
        logMsg("Creating entity EJB #" + j + " with Primary Key = " + j
            + " and Price = " + j);
        beanRef[i] = (TestBean) beanHome.create(props, j, "coffee-" + j, j);
      }

      TestUtil.logMsg("Find EJB references for Price Range of [2-4]");
      Collection c = beanHome.findWithinPriceRange(2, 4);
      TestUtil.logMsg("Check if we found the correct EJB references");
      TestUtil.logMsg("Number of EJB references returned = " + c.size());
      Iterator i = c.iterator();
      int j = 0;
      while (i.hasNext())
        beanRef2[j++] = (TestBean) PortableRemoteObject.narrow(i.next(),
            TestBean.class);
      if (c.size() != 3) {
        TestUtil.logErr("findWithinPriceRange returned " + c.size()
            + " references, expected 3 references");
        pass = false;
      }
      for (int k = 0; k < c.size(); k++) {
        boolean found = false;
        for (int l = 1; l < 4; l++) {
          if (beanRef[l].isIdentical(beanRef2[k])) {
            found = true;
            break;
          }
        }
        if (!found) {
          TestUtil.logErr(
              "findWithinPriceRange returned incorrect reference for k=" + k);
          pass = false;
        } else {
          TestUtil.logMsg(
              "findWithinPriceRange returned correct reference for k=" + k);
        }
      }
      if (beanRef2[0].isIdentical(beanRef2[1])
          || beanRef2[0].isIdentical(beanRef2[2])
          || beanRef2[1].isIdentical(beanRef2[0])
          || beanRef2[1].isIdentical(beanRef2[2])
          || beanRef2[2].isIdentical(beanRef2[0])
          || beanRef2[2].isIdentical(beanRef2[1])) {
        TestUtil
            .logErr("findWithinPriceRange returned references not all unique");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception test22: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("test22 failed", e);
    } finally {
      try {
        for (int i = 0; i < NUMEJBS; i++)
          beanRef[i].remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
    }

    if (!pass)
      throw new Fault("test22 failed");
  }

  /*
   * @testName: test23
   * 
   * @assertion_ids: EJB:SPEC:470; EJB:SPEC:471
   * 
   * @test_Strategy: Create several CMP 1.1 Entity EJB's via the EJBHome
   * interface. Deploy them on the J2EE server. Perform a find of a range of CMP
   * 1.1 Entity EJB's and verify that the correct CMP 1.1 Entity EJB's were
   * found.
   */

  public void test23() throws Fault {

    boolean pass = true;
    TestBean[] beanRef = new TestBean[NUMEJBS];
    TestBean[] beanRef2 = new TestBean[NUMEJBS];

    try {
      TestUtil.logMsg("Create EJB instances for test23");
      for (int i = 0, j = 1; i < NUMEJBS; i++, j++) {
        logMsg("Creating entity EJB #" + j + " with Primary Key = " + j
            + " and Price = " + j);
        beanRef[i] = (TestBean) beanHome.create(props, j, "coffee-" + j, j);
      }

      TestUtil.logMsg("Find EJB references for Primary Key Range of [2-4]");
      Integer min = new Integer(2);
      Integer max = new Integer(4);
      Collection c = beanHome.findWithinPrimaryKeyRange(min, max);
      TestUtil.logMsg("Check if we found the correct EJB references");
      TestUtil.logMsg("Number of EJB references returned = " + c.size());
      Iterator i = c.iterator();
      int j = 0;
      while (i.hasNext())
        beanRef2[j++] = (TestBean) PortableRemoteObject.narrow(i.next(),
            TestBean.class);
      if (c.size() != 3) {
        TestUtil.logErr("findWithinPrimaryKeyRange returned " + c.size()
            + " references, expected 3 references");
        pass = false;
      }
      for (int k = 0; k < c.size(); k++) {
        boolean found = false;
        for (int l = 1; l < 4; l++) {
          if (beanRef[l].isIdentical(beanRef2[k])) {
            found = true;
            break;
          }
        }
        if (!found) {
          TestUtil.logErr("findWithinPrimaryKeyRange returned "
              + "incorrect reference for k=" + k);
          pass = false;
        } else {
          TestUtil.logMsg("findWithinPrimaryKeyRange returned "
              + "correct reference for k=" + k);
        }
      }
      if (beanRef2[0].isIdentical(beanRef2[1])
          || beanRef2[0].isIdentical(beanRef2[2])
          || beanRef2[1].isIdentical(beanRef2[0])
          || beanRef2[1].isIdentical(beanRef2[2])
          || beanRef2[2].isIdentical(beanRef2[0])
          || beanRef2[2].isIdentical(beanRef2[1])) {
        TestUtil.logErr("findWithinPrimaryKeyRange returned "
            + "references not all unique");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception test23: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("test23 failed", e);
    } finally {
      try {
        for (int i = 0; i < NUMEJBS; i++)
          beanRef[i].remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
    }

    if (!pass)
      throw new Fault("test23 failed");
  }

  /*
   * @testName: test24
   * 
   * @assertion_ids: EJB:SPEC:470; EJB:SPEC:471
   * 
   * @test_Strategy: Create several CMP 1.1 Entity EJB's via the EJBHome
   * interface. Deploy them on the J2EE server. Perform a find of a non-existant
   * CMP 1.1 Entity EJB and verify that an EMPTY collection is returned.
   */

  public void test24() throws Fault {

    boolean pass = true;
    TestBean[] beanRef = new TestBean[NUMEJBS];

    try {
      TestUtil.logMsg("Create EJB instances for test24");
      for (int i = 0, j = 1; i < NUMEJBS; i++, j++) {
        logMsg("Creating entity EJB #" + j + " with Primary Key = " + j
            + " and Price = " + j);
        beanRef[i] = (TestBean) beanHome.create(props, j, "coffee-" + j, j);
      }

      TestUtil.logMsg(
          "Attempt multi-object finder to find non-existant EJB should return EMPTY collection");
      Collection c = beanHome.findByPrice((float) 1.50);
      TestUtil.logMsg("Number of EJB references returned = " + c.size());
      if (!c.isEmpty()) {
        TestUtil
            .logErr("findByPrice returned non-EMPTY collection - unexpected");
        pass = false;
      } else {
        TestUtil.logMsg("findByPrice returned EMPTY collection - expected");
        pass = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception test24: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("test24 failed", e);
    } finally {
      try {
        for (int i = 0; i < NUMEJBS; i++)
          beanRef[i].remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
    }

    if (!pass)
      throw new Fault("test24 failed");
  }

  /*
   * @testName: test25
   * 
   * @assertion_ids: EJB:SPEC:468; EJB:JAVADOC:146
   * 
   * @test_Strategy: Create several CMP 1.1 Entity EJB's via the EJBHome
   * interface. Deploy them on the J2EE server. Perform a find of a non-existant
   * CMP 1.1 Entity EJB and verify that the ObjectNotFoundException is returned.
   */

  public void test25() throws Fault {

    boolean pass = true;
    TestBean[] beanRef = new TestBean[NUMEJBS];
    TestBean beanRef2 = null;

    try {
      TestUtil.logMsg("Create EJB instances for test25");
      for (int i = 0, j = 1; i < NUMEJBS; i++, j++) {
        logMsg("Creating entity EJB #" + j + " with Primary Key = " + j
            + " and Price = " + j);
        beanRef[i] = (TestBean) beanHome.create(props, j, "coffee-" + j, j);
      }

      TestUtil.logMsg(
          "Attempt single-object finder to find non-existant EJB reference should return ObjectNotFoundException (subclass of FinderException)");
      beanRef2 = beanHome.findByNameSingle("coffee-foo");
      TestUtil.logErr(
          "No exception occurred from single-object finder whose Entity does not exist");
      pass = false;
    } catch (ObjectNotFoundException e) {
      TestUtil.logMsg("ObjectNotFoundException caught as expected");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception test25: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("test25 failed", e);
    } finally {
      try {
        for (int i = 0; i < NUMEJBS; i++)
          beanRef[i].remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
    }

    if (!pass)
      throw new Fault("test25 failed");
  }

  /*
   * @testName: test26
   * 
   * @assertion_ids: EJB:SPEC:468; EJB:JAVADOC:119
   * 
   * @test_Strategy: Create several CMP 1.1 Entity EJB's via the EJBHome
   * interface. Deploy them on the J2EE server. Perform a find by name where 2
   * entites have the same name and verify that FinderException is returned.
   */

  public void test26() throws Fault {

    boolean pass = true;
    TestBean[] beanRef = new TestBean[NUMEJBS];
    TestBean beanRef2 = null;

    try {
      TestUtil.logMsg("Create EJB instances for test26");
      beanRef[0] = (TestBean) beanHome.create(props, 0, "coffee-1", 1);
      for (int i = 1, j = 1; i < NUMEJBS; i++, j++) {
        logMsg("Creating entity EJB #" + j + " with Primary Key = " + j
            + " and Price = " + j);
        beanRef[i] = (TestBean) beanHome.create(props, j, "coffee-" + j, j);
      }
      TestUtil.logMsg(
          "Attempt a single-object finder that matches more than 1 Entity instance should return FinderException");
      beanRef2 = beanHome.findByNameSingle("coffee-1");
      TestUtil.logErr(
          "No exception occurred from single-object finder whose Entity does not exist");
      pass = false;
    } catch (FinderException e) {
      TestUtil.logMsg("FinderException caught as expected");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception test26: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("test26 failed", e);
    } finally {
      try {
        for (int i = 0; i < NUMEJBS; i++)
          beanRef[i].remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
    }

    if (!pass)
      throw new Fault("test26 failed");
  }

  /*
   * @testName: test27
   * 
   * @assertion_ids: EJB:SPEC:503
   * 
   * @test_Strategy: Create several CMP 1.1 Entity EJB's via the EJBHome
   * interface. Deploy them on the J2EE server. Perform a no-arg finder Entity
   * EJB to return all EJB's in table. Verify collection size returned matches.
   */

  public void test27() throws Fault {

    boolean pass = true;
    TestBean[] beanRef = new TestBean[NUMEJBS];
    TestBean beanRef2 = null;

    try {
      TestUtil.logMsg("Create EJB instances for test27");
      for (int i = 0, j = 1; i < NUMEJBS; i++, j++) {
        logMsg("Creating entity EJB #" + j + " with Primary Key = " + j
            + " and Price = " + j);
        beanRef[i] = (TestBean) beanHome.create(props, j, "coffee-" + j, j);
      }

      Collection c = beanHome.findAllBeans();
      if (c.size() != NUMEJBS) {
        pass = false;
        TestUtil.logErr("Collection size, expected: " + NUMEJBS + ", received: "
            + c.size());
      } else {
        pass = true;
        TestUtil.logMsg("Collection size, expected: " + NUMEJBS + ", received: "
            + c.size());
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception test27: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("test27 failed", e);
    } finally {
      try {
        for (int i = 0; i < NUMEJBS; i++)
          beanRef[i].remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
    }

    if (!pass)
      throw new Fault("test27 failed");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

}
