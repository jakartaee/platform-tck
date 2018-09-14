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
 * $URL$ $LastChangedDate$
 */

package com.sun.ts.tests.interop.ejb.entity.cmp20.clientviewtest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.ejb.ee.bb.entity.cmp20.clientviewtest.*;

import java.io.*;
import java.util.*;
import javax.ejb.*;
import javax.transaction.*;
import javax.rmi.PortableRemoteObject;
import java.rmi.*;

import com.sun.javatest.Status;

public class Client extends EETest {
  private static final String testBean = "java:comp/env/ejb/TestBean";

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
   * @testName: interopEntityCmp20Test1
   * 
   * @assertion_ids: EJB:SPEC:125
   * 
   * @tst_Strategy: Create an Entity CMP20 Bean. Deploy it on the J2EE server.
   *
   */

  public void interopEntityCmp20Test1() throws Fault {
    boolean pass = true;
    try {
      // Get EJB Home ...
      logMsg("Looking up home in interopEntityCmp20Test1");
      beanHome = (TestBeanHome) nctx.lookup(testBean, TestBeanHome.class);

      if (beanHome == null)
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception interopEntityCmp20Test1: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("interopEntityCmp20Test1 failed : " + e);
    }

    if (!pass)
      throw new Fault("interopEntityCmp20Test1 failed");
  }

  /*
   * @testName: interopEntityCmp20Test2
   * 
   * @assertion_ids: EJB:SPEC:126.1
   * 
   * @test_Strategy: Create an Entity CMP20 EJB via the EJBHome interface.
   * Deploy it on the J2EE server.
   */

  public void interopEntityCmp20Test2() throws Fault {

    boolean pass = true;
    TestBean beanRef = null;

    try {
      TestUtil.logMsg("Create EJB instance interopEntityCmp20Test2");
      beanRef = (TestBean) beanHome.create(1, "coffee-1", 1);
      beanRef.initLogging(props);
      beanRef.ping("ping em");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception interopEntityCmp20Test2: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("interopEntityCmp20Test2 failed", e);
    } finally {
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
    }

    if (!pass)
      throw new Fault("interopEntityCmp20Test2 failed");
  }

  /*
   * @testName: interopEntityCmp20Test3
   * 
   * @assertion_ids: EJB:SPEC:126.3; EJB:SPEC:136; EJB:JAVADOC:166;
   * EJB:SPEC:144.5; EJB:SPEC:137;
   * 
   * @test_Strategy: Create an Entity CMP20 EJB. Deploy it on the J2EE server.
   * Obtain handle, then remove object, attempt make call to the removed object
   * 
   */
  public void interopEntityCmp20Test3() throws Fault {
    boolean pass = false;
    TestBean beanRef = null;

    try {
      TestUtil.logMsg("Create EJB instance interopEntityCmp20Test3");
      beanRef = (TestBean) beanHome.create(1, "coffee-1", 1);
      beanRef.initLogging(props);

      // Get object handle and remove instance
      TestUtil.logMsg("Get object handle for bean instance");
      Handle handle = beanRef.getHandle();
      TestUtil.logMsg("handle= " + handle);

      if (handle == null) {
        TestUtil.logErr("handle for EJB is null");
        throw new Fault("interopEntityCmp20Test3 failed");
      } else {
        TestUtil.logMsg("remove EJB via handle");
        beanHome.remove(handle);
        try {
          TestUtil.logMsg("Calling beanRef.ping()");
          beanRef.ping("ping em");
        } catch (NoSuchObjectException e) {
          TestUtil.logMsg(
              "Caught inner NoSuchObjectException in interopEntityCmp20Test3 as expected");
          pass = true;
        } catch (RemoteException e) {
          TestUtil.logMsg("RemoteException received as expected " + e);
          pass = true;
        } catch (Exception e) {
          TestUtil
              .logErr("Caught inner exception interopEntityCmp20Test3: " + e);
          TestUtil.printStackTrace(e);
          throw new Fault("interopEntityCmp20Test3 failed : " + e);
        }
      }

      if (!pass)
        throw new Fault("interopEntityCmp20Test3 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception interopEntityCmp20Test3: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("interopEntityCmp20Test3 failed : " + e);
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
   * @testName: interopEntityCmp20Test4
   * 
   * @assertion_ids: EJB:SPEC:148; EJB:JAVADOC:139; EJB:JAVADOC:140;
   * EJB:SPEC:144.4
   * 
   * @test_Strategy: Create an Entity CMP20 Bean. Deploy it on the J2EE server.
   * Try to Obtain primary key.
   *
   */

  public void interopEntityCmp20Test4() throws Fault {
    boolean pass = false;
    TestBean beanRef = null;
    Object primaryKey = null;

    try {

      // create EJB instance
      TestUtil.logMsg("Create EJB instance interopEntityCmp20Test4");
      beanRef = (TestBean) beanHome.create(1, "coffee-1", 1);
      beanRef.initLogging(props);

      try {
        TestUtil.logMsg("This is before the beanRef.getPrimaryKey() ");
        primaryKey = beanRef.getPrimaryKey();
        pass = true;
        TestUtil.logMsg("This is after the beanRef.getPrimaryKey() ");
      } catch (RemoteException e) {
        TestUtil.logErr(
            "Caught RemoteException interopEntityCmp20Test4 unexpected: " + e,
            e);
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
        throw new Fault("interopEntityCmp20Test4 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception interopEntityCmp20Test4: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("interopEntityCmp20Test4 failed", e);
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
   * @testName: interopEntityCmp20Test5
   * 
   * @assertion_ids: EJB:SPEC:126.3; EJB:SPEC:137; EJB:JAVADOC:169
   * 
   * @test_Strategy: Create an Entity CMP20 Bean. Deploy it on the J2EE server.
   * Try to Obtain primary Key. Try to remove the object via the primary key.
   * Check to see if object still exists.
   *
   */

  public void interopEntityCmp20Test5() throws Fault {
    boolean pass = false;
    TestBean beanRef = null;

    try {

      // create EJB instance
      TestUtil.logMsg("Create EJB instance interopEntityCmp20Test5");
      beanRef = (TestBean) beanHome.create(1, "coffee-1", 1);

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
              "Caught inner NoSuchObjectException in interopEntityCmp20Test5 as expected");
          pass = true;
        } catch (RemoteException e) {
          TestUtil.logMsg("RemoteException received as expected " + e);
          pass = true;
        }
      } catch (Exception e) {
        TestUtil.logErr("Caught inner exception interopEntityCmp20Test5: " + e);
        TestUtil.printStackTrace(e);
        throw new Fault("interopEntityCmp20Test5 failed : " + e);
      }

      if (!pass)
        throw new Fault("interopEntityCmp20Test5 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception interopEntityCmp20Test5: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("interopEntityCmp20Test5 failed", e);
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
   * @testName: interopEntityCmp20Test6
   * 
   * @assertion_ids: EJB:SPEC:126.4; EJB:JAVADOC:172
   * 
   * @test_Strategy: Obtain EJBMetaData via the EJBHome interface Verify
   * EJBMetaData was obtained.
   *
   */
  public void interopEntityCmp20Test6() throws Fault {
    boolean pass = false;
    TestBean beanRef = null;

    try {
      TestUtil.logMsg("Create EJB instance interopEntityCmp20Test6");
      beanRef = (TestBean) beanHome.create(1, "coffee-1", 1);
      beanRef.initLogging(props);
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
        throw new Fault("interopEntityCmp20Test6 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception interopEntityCmp20Test6: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("interopEntityCmp20Test6 failed", e);
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
   * @testName: interopEntityCmp20Test6a
   * 
   * @assertion_ids: EJB:SPEC:126.5; EJB:SPEC:159; EJB:JAVADOC:174
   * 
   * @test_Strategy: Obtain HomeHandle via the EJBHome interface Verify
   * HomeHandle was obtained.
   *
   */
  public void interopEntityCmp20Test6a() throws Fault {
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
        throw new Fault("interopEntityCmp20Test6a failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception interopEntityCmp20Test6a: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("interopEntityCmp20Test6a failed", e);
    }
  }

  /*
   * @testName: interopEntityCmp20Test7
   * 
   * @assertion_ids: EJB:SPEC:152.1; EJB:SPEC:144.2; EJB:JAVADOC:137
   * 
   * @test_Strategy: Create an Entity CMP20 EJB. Deploy it on the J2EE server.
   * Obtain the EJBHome Interface. Verify the EJBHome Interface was created.
   */
  public void interopEntityCmp20Test7() throws Fault {
    boolean pass = false;
    TestBean beanRef = null;

    try {

      // create EJB instance
      TestUtil.logMsg("Create EJB instance interopEntityCmp20Test7");
      beanRef = (TestBean) beanHome.create(1, "coffee-1", 1);
      beanRef.initLogging(props);

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
        throw new Fault("interopEntityCmp20Test7 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception interopEntityCmp20Test7: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("interopEntityCmp20Test7 failed", e);
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
   * @testName: interopEntityCmp20Test8
   * 
   * @assertion_ids: EJB:SPEC:152.3; EJB:SPEC:157; EJB:JAVADOC:144
   * 
   * @test_Strategy: Create an Entity CMP20 EJB. Deploy it on the J2EE server.
   * Obtain handle via the EJBObject, Verify the handle was obtained.
   */

  public void interopEntityCmp20Test8() throws Fault {

    boolean pass = false;
    TestBean beanRef = null;

    try {

      // create EJB instance
      TestUtil.logMsg("Create EJB instance interopEntityCmp20Test8");
      beanRef = (TestBean) beanHome.create(1, "coffee-1", 1);
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
        throw new Fault("interopEntityCmp20Test8 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception interopEntityCmp20Test8: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("interopEntityCmp20Test8 failed", e);
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
   * @testName: interopEntityCmp20Test9
   * 
   * @assertion_ids: EJB:SPEC:152.2; EJB:JAVADOC:141
   * 
   * @test_Strategy: Create an Entity CMP20 EJB. Deploy it on the J2EE server.
   * Remove the Object via the EJBObject. Verify the object no longer exists.
   */

  public void interopEntityCmp20Test9() throws Fault {

    boolean pass = false;
    TestBean beanRef = null;

    try {

      // create EJB instance
      TestUtil.logMsg("Create EJB instance interopEntityCmp20Test9");
      beanRef = (TestBean) beanHome.create(1, "coffee-1", 1);
      beanRef.initLogging(props);

      // Run true test
      TestUtil.logMsg("This is before the beanRef.remove() ");
      beanRef.remove();
      TestUtil.logMsg("This is after the beanRef.remove() ");

      beanRef.ping("ping em");

    } catch (NoSuchObjectException e) {
      TestUtil.logMsg(
          "Caught NoSuchObjectException in interopEntityCmp20Test9 as expected");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("interopEntityCmp20Test9 failed : " + e);
    } finally {
      try {
        beanRef.remove();
        if (!pass)
          throw new Fault("interopEntityCmp20Test9 failed");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
    }

  }

  /*
   * @testName: interopEntityCmp20Test10
   * 
   * @assertion_ids: EJB:SPEC:150; EJB:JAVADOC:146
   * 
   * @test_Strategy: Create a Entity CMP20 EJB. Deploy it on the J2EE server.
   * Call the isIdentical(SameObject). verify that the result is true.
   */

  public void interopEntityCmp20Test10() throws Fault {

    boolean pass = false;
    TestBean beanRef = null;

    try {

      // create EJB instance
      TestUtil.logMsg("Create beanRef instance interopEntityCmp20Test10");
      beanRef = (TestBean) beanHome.create(1, "coffee-1", 1);
      beanRef.initLogging(props);

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
        throw new Fault("interopEntityCmp20Test10 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception interopEntityCmp20Test10: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("interopEntityCmp20Test10 failed", e);
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
   * @testName: interopEntityCmp20Test11
   * 
   * @assertion_ids: EJB:SPEC:150; EJB:JAVADOC:146
   * 
   * @test_Strategy: Create an Entity CMP20 EJB. Deploy it on the J2EE server.
   * Call the isIdentical(newObject). verify that the result is false.
   */

  public void interopEntityCmp20Test11() throws Fault {

    boolean pass = false;

    TestBean beanRef2 = null;
    TestBean beanRef1 = null;

    try {

      // create EJB instance
      TestUtil.logMsg("Create beanRef1 instance interopEntityCmp20Test11");
      beanRef1 = (TestBean) beanHome.create(1, "coffee-1", 1);

      TestUtil.logMsg("Create beanRef2 instance interopEntityCmp20Test11");
      beanRef2 = (TestBean) beanHome.create(2, "coffee-2", 2);

      beanRef1.initLogging(props);
      beanRef2.initLogging(props);

      TestUtil.logMsg("Getting ready to do the compare 2 different objects");

      if (beanRef1.isIdentical(beanRef2)) {
        TestUtil.logMsg("The EJBObjects are Identical");
        pass = false;
      } else {
        TestUtil.logMsg("The EJBObjects are not Identical");
        pass = true;
      }

      if (!pass)
        throw new Fault("interopEntityCmp20Test11 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception interopEntityCmp20Test11: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("interopEntityCmp20Test11 failed", e);
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
   * @testName: interopEntityCmp20Test12
   * 
   * @assertion_ids: EJB:SPEC:144.1
   * 
   * @test_Strategy: Create an Entity CMP20 EJB. Deploy it on the J2EE server.
   * Call objects Business Method. verify that a exception isn't raised
   */

  public void interopEntityCmp20Test12() throws Fault {

    boolean pass = false;
    TestBean beanRef = null;

    try {
      // create EJB instance
      TestUtil.logMsg("Create EJB instance interopEntityCmp20Test12");
      beanRef = (TestBean) beanHome.create(1, "coffee-1", 1);
      beanRef.initLogging(props);

      // Run the true test
      TestUtil.logMsg("This is before the beanRef.businessMethod() ");
      TestUtil.logMsg(beanRef
          .ping("This is the message inside the beanRef.businessMethod() "));
      TestUtil.logMsg("This is after the beanRef.businessMethod() ");

      pass = true;

    } catch (Exception e) {
      TestUtil.logErr("Caught exception interopEntityCmp20Test12: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("interopEntityCmp20Test12 failed", e);
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
   * @testName: interopEntityCmp20Test13
   * 
   * @assertion_ids: EJB:SPEC:144.3
   * 
   * @test_Strategy: Create an Entity CMP20 EJB. Deploy it on the J2EE server.
   * Call the objects business method with and instance of itself as the
   * parameter.
   * 
   */

  public void interopEntityCmp20Test13() throws Fault {

    boolean pass = false;
    TestBean beanRef = null;

    try {
      // create EJB instance
      TestUtil.logMsg("Create EJB instance interopEntityCmp20Test13");
      beanRef = (TestBean) beanHome.create(1, "coffee-1", 1);
      beanRef.initLogging(props);

      // Run the true test
      TestUtil.logMsg("This is before the echoMethod(EJBObject) ");
      pass = beanRef.isIdentical(echoMethod(beanRef));
      TestUtil.logMsg("This is after the echoMethod(EJBObject) := " + pass);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception interopEntityCmp20Test13: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("interopEntityCmp20Test13 failed", e);
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
   * @testName: interopEntityCmp20Test14
   * 
   * @assertion_ids: EJB:SPEC:126.4; EJB:JAVADOC:148
   * 
   * @test_Strategy: Obtain EJBMetaData Obtain EJBHome interface Verify EJBHome
   * interface was obtained
   */

  public void interopEntityCmp20Test14() throws Fault {
    boolean pass = false;
    TestBean beanRef = null;

    try {

      // create EJB instance
      TestUtil.logMsg("Create EJB instance interopEntityCmp20Test14");
      beanRef = (TestBean) beanHome.create(1, "coffee-1", 1);
      beanRef.initLogging(props);

      TestUtil.logMsg(
          "Before the call to get MetaData() interopEntityCmp20Test14!");
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
        throw new Fault("interopEntityCmp20Test14 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception interopEntityCmp20Test14: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("interopEntityCmp20Test14 failed", e);
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
   * @testName: interopEntityCmp20Test15
   * 
   * @assertion_ids: EJB:SPEC:126.4; EJB:JAVADOC:149
   * 
   * @test_Strategy: Obtain metaData from beanHome Obtain EJB home Interface
   * Verify that EJB home Interface was obtained
   */

  public void interopEntityCmp20Test15() throws Fault {
    boolean pass = false;
    TestBean beanRef = null;

    try {
      // create EJB instance
      TestUtil.logMsg("Create EJB instance interopEntityCmp20Test15");
      beanRef = (TestBean) beanHome.create(1, "coffee-1", 1);
      beanRef.initLogging(props);

      TestUtil.logMsg(
          "Before the call to get MetaData() interopEntityCmp20Test15!");
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
        throw new Fault("interopEntityCmp20Test15 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception interopEntityCmp20Test15: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("interopEntityCmp20Test15 failed", e);
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
   * @testName: interopEntityCmp20Test16
   * 
   * @assertion_ids: EJB:SPEC:126.4; EJB:JAVADOC:151
   * 
   * @test_Strategy: Obtain EJBMetaData Obtain PrimaryKeyClass via EJBMetaData.
   * Verify that PrimaryKeyClass was obtained.
   */

  public void interopEntityCmp20Test16() throws Fault {
    boolean pass = false;
    TestBean beanRef = null;

    try {
      // create EJB instance
      TestUtil.logMsg("Create EJB instance interopEntityCmp20Test16");
      beanRef = (TestBean) beanHome.create(1, "coffee-1", 1);
      beanRef.initLogging(props);

      TestUtil.logMsg(
          "Before the call to get MetaData() interopEntityCmp20Test16!");
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
        TestUtil.logErr(
            "Caught RuntimeException in interopEntityCmp20Test16 unexpected: "
                + e,
            e);
      } catch (Exception e) {
        TestUtil.logErr("Caught Exception interopEntityCmp20Test16 : " + e);
        TestUtil.printStackTrace(e);
      }
    } catch (RemoteException e) {
      TestUtil.logErr("Caught RemoteException interopEntityCmp20Test16: " + e);
      TestUtil.printStackTrace(e);
    } catch (Exception e) {
      TestUtil.logErr("Caught Exception interopEntityCmp20Test16: " + e);
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
      throw new Fault("interopEntityCmp20Test16 failed");
  }

  /*
   * @testName: interopEntityCmp20Test17
   * 
   * @assertion_ids: EJB:SPEC:126.4; EJB:JAVADOC:150
   * 
   * @test_Strategy: Get MetaData from EJBHome Use MetaData to get ClassObject
   * Verify that the ClassObject was obtained.
   */

  public void interopEntityCmp20Test17() throws Fault {
    boolean pass = false;
    TestBean beanRef = null;

    try {
      // create EJB instance
      TestUtil.logMsg("Create EJB instance interopEntityCmp20Test17");
      beanRef = (TestBean) beanHome.create(1, "coffee-1", 1);
      beanRef.initLogging(props);

      TestUtil.logMsg(
          "Before the call to get MetaData() interopEntityCmp20Test17!");
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
        TestUtil.logErr(
            "getRemoteInterfaceClass Returned a null value interopEntityCmp20Test17");
        pass = false;
      }

      if (!pass)
        throw new Fault("interopEntityCmp20Test17 failed");

    } catch (RemoteException e) {
      TestUtil.logErr("Caught exception interopEntityCmp20Test17: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("interopEntityCmp20Test17 failed", e);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception interopEntityCmp20Test17: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("interopEntityCmp20Test17 failed", e);
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
   * @testName: interopEntityCmp20Test18
   * 
   * @assertion_ids: EJB:SPEC:126.4; EJB:JAVADOC:152
   * 
   * @test_Strategy: Obtain EJBMetaData Call isSession on the Entity CMP20 Bean
   * Verify that the result is false
   */

  public void interopEntityCmp20Test18() throws Fault {

    boolean pass = false;
    TestBean beanRef = null;

    try {
      // create EJB instance
      TestUtil.logMsg("Create EJB instance interopEntityCmp20Test18");
      beanRef = (TestBean) beanHome.create(1, "coffee-1", 1);
      beanRef.initLogging(props);

      TestUtil.logMsg(
          "Before the call to get MetaData() interopEntityCmp20Test18!");
      EJBMetaData metaData = beanHome.getEJBMetaData();
      TestUtil.logMsg("metaData=" + metaData);

      // Run the true test
      TestUtil.logMsg("This is before the metaData.isSession()");
      boolean isSession = metaData.isSession();

      TestUtil.logMsg("This is after the metaData.isSession() ");

      TestUtil.logMsg("isSession =" + isSession);
      pass = isSession;

      if (pass)
        throw new Fault("interopEntityCmp20Test18 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception interopEntityCmp20Test18: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("interopEntityCmp20Test18 failed", e);
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
   * @testName: interopEntityCmp20Test19
   * 
   * @assertion_ids: EJB:SPEC:132; EJB:SPEC:133; EJB:SPEC:126.2
   * 
   * @test_Strategy: Create several Entity CMP20 EJB's via the EJBHome
   * interface. Deploy them on the J2EE server. Perform a find of one of the
   * Entity EJB's and verify that the correct Entity EJB was found.
   */

  public void interopEntityCmp20Test19() throws Fault {

    boolean pass = true;
    TestBean[] beanRef = new TestBean[NUMEJBS];
    TestBean beanRef2 = null;

    try {
      TestUtil.logMsg("Create EJB instances for interopEntityCmp20Test19");
      for (int i = 0, j = 1; i < NUMEJBS; i++, j++) {
        logMsg("Creating entity EJB #" + j + " with Primary Key = " + j
            + " and Price = " + j);
        beanRef[i] = (TestBean) beanHome.create(j, "coffee-" + j, j);
        beanRef[i].initLogging(props);
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
      TestUtil.logErr("Caught exception interopEntityCmp20Test19: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("interopEntityCmp20Test19 failed", e);
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
      throw new Fault("interopEntityCmp20Test19 failed");
  }

  /*
   * @testName: interopEntityCmp20Test20
   * 
   * @assertion_ids: EJB:SPEC:272; EJB:SPEC:126.2; EJB:SPEC:129
   * 
   * @test_Strategy: Create several Entity CMP20 EJB's via the EJBHome
   * interface. Deploy them on the J2EE server. Perform a find of one of the
   * Entity EJB's and verify that the correct Entity EJB was found.
   */

  public void interopEntityCmp20Test20() throws Fault {

    boolean pass = true;
    TestBean[] beanRef = new TestBean[NUMEJBS];
    TestBean beanRef2 = null;

    try {
      TestUtil.logMsg("Create EJB instances for interopEntityCmp20Test20");
      for (int i = 0, j = 1; i < NUMEJBS; i++, j++) {
        logMsg("Creating entity EJB #" + j + " with Primary Key = " + j
            + " and Price = " + j);
        beanRef[i] = (TestBean) beanHome.create(j, "coffee-" + j, j);
        beanRef[i].initLogging(props);
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
      TestUtil.logErr("Caught exception interopEntityCmp20Test20: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("interopEntityCmp20Test20 failed", e);
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
      throw new Fault("interopEntityCmp20Test20 failed");
  }

  /*
   * @testName: interopEntityCmp20Test21
   * 
   * @assertion_ids: EJB:SPEC:272; EJB:SPEC:143.2
   * 
   * @test_Strategy: Create several Entity CMP20 EJB's via the EJBHome
   * interface. Deploy them on the J2EE server. Perform a find of one of the
   * Entity EJB's and verify that the correct Entity EJB was found.
   */

  public void interopEntityCmp20Test21() throws Fault {

    boolean pass = true;
    TestBean[] beanRef = new TestBean[NUMEJBS];
    TestBean beanRef2 = null;

    try {
      TestUtil.logMsg("Create EJB instances for interopEntityCmp20Test21");
      for (int i = 0, j = 1; i < NUMEJBS; i++, j++) {
        logMsg("Creating entity EJB #" + j + " with Primary Key = " + j
            + " and Price = " + j);
        beanRef[i] = (TestBean) beanHome.create(j, "coffee-" + j, j);
        beanRef[i].initLogging(props);
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
      TestUtil.logErr("Caught exception interopEntityCmp20Test21: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("interopEntityCmp20Test21 failed", e);
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
      throw new Fault("interopEntityCmp20Test21 failed");
  }

  /*
   * @testName: interopEntityCmp20Test22
   * 
   * @assertion_ids: EJB:SPEC:272; EJB:SPEC:126.2; EJB:SPEC:129
   * 
   * @test_Strategy: Create several Entity CMP20 EJB's via the EJBHome
   * interface. Deploy them on the J2EE server. Perform a find of a range of
   * Entity EJB's and verify that the correct Entity EJB's were found.
   */

  public void interopEntityCmp20Test22() throws Fault {

    boolean pass = true;
    TestBean[] beanRef = new TestBean[NUMEJBS];
    TestBean[] beanRef2 = new TestBean[NUMEJBS];

    try {
      TestUtil.logMsg("Create EJB instances for interopEntityCmp20Test22");
      for (int i = 0, j = 1; i < NUMEJBS; i++, j++) {
        logMsg("Creating entity EJB #" + j + " with Primary Key = " + j
            + " and Price = " + j);
        beanRef[i] = (TestBean) beanHome.create(j, "coffee-" + j, j);
        beanRef[i].initLogging(props);
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
      TestUtil.logErr("Caught exception interopEntityCmp20Test22: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("interopEntityCmp20Test22 failed", e);
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
      throw new Fault("interopEntityCmp20Test22 failed");
  }

  /*
   * @testName: interopEntityCmp20Test23
   * 
   * @assertion_ids: EJB:SPEC:272
   * 
   * @test_Strategy: Create several Entity CMP20 EJB's via the EJBHome
   * interface. Deploy them on the J2EE server. Perform a find of a range of
   * Entity EJB's and verify that the correct Entity EJB's were found.
   */

  public void interopEntityCmp20Test23() throws Fault {

    boolean pass = true;
    TestBean[] beanRef = new TestBean[NUMEJBS];
    TestBean[] beanRef2 = new TestBean[NUMEJBS];

    try {
      TestUtil.logMsg("Create EJB instances for interopEntityCmp20Test23");
      for (int i = 0, j = 1; i < NUMEJBS; i++, j++) {
        logMsg("Creating entity EJB #" + j + " with Primary Key = " + j
            + " and Price = " + j);
        beanRef[i] = (TestBean) beanHome.create(j, "coffee-" + j, j);
        beanRef[i].initLogging(props);
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
      TestUtil.logErr("Caught exception interopEntityCmp20Test23: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("interopEntityCmp20Test23 failed", e);
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
      throw new Fault("interopEntityCmp20Test23 failed");
  }

  /*
   * @testName: interopEntityCmp20Test24
   * 
   * @assertion_ids: EJB:SPEC:272; EJB:SPEC:284
   * 
   * @test_Strategy: Create several Entity CMP20 EJB's via the EJBHome
   * interface. Deploy them on the J2EE server. Perform a find of a non-existant
   * Entity EJB and verify that an EMPTY collection is returned.
   */

  public void interopEntityCmp20Test24() throws Fault {

    boolean pass = true;
    TestBean[] beanRef = new TestBean[NUMEJBS];

    try {
      TestUtil.logMsg("Create EJB instances for interopEntityCmp20Test24");
      for (int i = 0, j = 1; i < NUMEJBS; i++, j++) {
        logMsg("Creating entity EJB #" + j + " with Primary Key = " + j
            + " and Price = " + j);
        beanRef[i] = (TestBean) beanHome.create(j, "coffee-" + j, j);
        beanRef[i].initLogging(props);
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
        TestUtil.logErr("findByPrice returned EMPTY collection - expected");
        pass = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception interopEntityCmp20Test24: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("interopEntityCmp20Test24 failed", e);
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
      throw new Fault("interopEntityCmp20Test24 failed");
  }

  /*
   * @testName: interopEntityCmp20Test25
   * 
   * @assertion_ids: EJB:SPEC:285
   * 
   * @test_Strategy: Create several Entity CMP20 EJB's via the EJBHome
   * interface. Deploy them on the J2EE server. Perform a find of a non-existent
   * Entity EJB and verify that the ObjectNotFoundException is returned.
   */

  public void interopEntityCmp20Test25() throws Fault {

    boolean pass = true;
    TestBean[] beanRef = new TestBean[NUMEJBS];
    TestBean beanRef2 = null;

    try {
      TestUtil.logMsg("Create EJB instances for interopEntityCmp20Test25");
      for (int i = 0, j = 1; i < NUMEJBS; i++, j++) {
        logMsg("Creating entity EJB #" + j + " with Primary Key = " + j
            + " and Price = " + j);
        beanRef[i] = (TestBean) beanHome.create(j, "coffee-" + j, j);
        beanRef[i].initLogging(props);
      }

      TestUtil.logMsg(
          "Attempt single-object finder to find non-existant EJB reference should return ObjectNotFoundException (subclass of FinderException)");
      beanRef2 = beanHome.findByNameSingle("coffee-foo");
      TestUtil.logErr(
          "No exception occurred from single-object finder whoase Entity does not exist");
      pass = false;
    } catch (ObjectNotFoundException e) {
      TestUtil.logMsg("ObjectNotFoundException caught as expected");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception interopEntityCmp20Test25: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("interopEntityCmp20Test25 failed", e);
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
      throw new Fault("interopEntityCmp20Test25 failed");
  }

  /*
   * @testName: interopEntityCmp20Test26
   * 
   * @assertion_ids: EJB:SPEC:268
   * 
   * @test_Strategy: Create several Entity EJB's via the EJBHome interface.
   * Deploy them on the J2EE server. Perform a find by name where 2 entites have
   * the same name and verify that FinderException is returned.
   */

  public void interopEntityCmp20Test26() throws Fault {

    boolean pass = true;
    TestBean[] beanRef = new TestBean[NUMEJBS];
    TestBean beanRef2 = null;

    try {
      TestUtil.logMsg("Create EJB instances for interopEntityCmp20Test26");
      beanRef[0] = (TestBean) beanHome.create(0, "coffee-1", 1);
      beanRef[0].initLogging(props);
      for (int i = 1, j = 1; i < NUMEJBS; i++, j++) {
        logMsg("Creating entity EJB #" + j + " with Primary Key = " + j
            + " and Price = " + j);
        beanRef[i] = (TestBean) beanHome.create(j, "coffee-" + j, j);
        beanRef[i].initLogging(props);
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
      TestUtil.logErr("Caught exception interopEntityCmp20Test26: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("interopEntityCmp20Test26 failed", e);
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
      throw new Fault("interopEntityCmp20Test26 failed");
  }

  /*
   * @testName: interopEntityCmp20Test27
   * 
   * @assertion_ids: EJB:SPEC:267
   * 
   * @test_Strategy: Create several Entity EJB's via the EJBHome interface.
   * Deploy them on the J2EE server. Perform a no-arg finder Entity EJB to
   * return all EJB's in table. Verify collection size returned matches.
   */

  public void interopEntityCmp20Test27() throws Fault {

    boolean pass = true;
    TestBean[] beanRef = new TestBean[NUMEJBS];
    TestBean beanRef2 = null;

    try {
      TestUtil.logMsg("Create EJB instances for interopEntityCmp20Test27");
      for (int i = 0, j = 1; i < NUMEJBS; i++, j++) {
        logMsg("Creating entity EJB #" + j + " with Primary Key = " + j
            + " and Price = " + j);
        beanRef[i] = (TestBean) beanHome.create(j, "coffee-" + j, j);
        beanRef[i].initLogging(props);
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
      TestUtil.logErr("Caught exception interopEntityCmp20Test27: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("interopEntityCmp20Test27 failed", e);
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
      throw new Fault("interopEntityCmp20Test27 failed");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

}
