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
 * @(#)Client.java	1.16 03/05/16
 */

package com.sun.ts.tests.interop.ejb.session.stateful.handletest;

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
import com.sun.ts.tests.ejb.ee.bb.common.EJBHandleObjectInputStream;

public class Client extends EETest {
  private static final String testName = "HandleTest";

  private static final String testLookup = "java:comp/env/ejb/TestBean";

  private static final String testProps = "handletest.properties";

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
      logMsg("Obtain naming context");
      nctx = new TSNamingContext();

      // Get EJB Home ...
      logMsg("Looking up home interface for EJB: " + testLookup);
      beanHome = (TestBeanHome) nctx.lookup(testLookup, TestBeanHome.class);
      logMsg("Setup ok");
    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  /* Run test */

  /*
   * @testName: test1
   * 
   * @assertion_ids: EJB:SPEC:665; EJB:SPEC:666; EJB:SPEC:667; EJB:SPEC:668;
   * EJB:SPEC:685; EJB:SPEC:678.1
   * 
   * @test_Strategy: Create a stateful Session Bean. Deploy it on the J2EE
   * server. Obtain handle and ensure it is serializable.
   *
   */

  public void test1() throws Fault {
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props);
      logMsg("get handle for object");
      Handle handle = beanRef.getHandle();
      boolean pass = true;
      if (handle == null) {
        pass = false;
        logErr("getHandle() returned null reference");
      } else if (!(handle instanceof Serializable)) {
        pass = false;
        ;
        logErr("getHandle() is not serializable");
      } else
        logMsg("got handle and handle is serializable");
      if (!pass)
        throw new Fault("test1 failed");
      beanRef.remove();
    } catch (Exception e) {
      throw new Fault("test1 failed", e);
    }
  }

  /*
   * @testName: test2
   * 
   * @assertion_ids: EJB:SPEC:665; EJB:SPEC:666; EJB:SPEC:667; EJB:SPEC:668;
   * EJB:SPEC:685; EJB:SPEC:678.1
   * 
   * @test_Strategy: Create a stateful Session EJBean. Deploy it on the J2EE
   * server. Obtain handle, serialize/deserialize handle, invoke bean object
   * with deserialized handle.
   */

  public void test2() throws Fault {
    ObjectInputStream is = null;
    ObjectOutputStream os = null;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props);

      logMsg("ping object");
      int pingCount = beanRef.ping(1);

      logMsg("get handle for object");
      Handle handle = beanRef.getHandle();

      logMsg("write object output stream to byte array");
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      os = new ObjectOutputStream(baos);
      os.writeObject(handle);

      logMsg("read object output stream from byte array");
      byte[] b = baos.toByteArray();
      ByteArrayInputStream bais = new ByteArrayInputStream(b);
      is = new EJBHandleObjectInputStream(bais);
      Handle deserializedHandle = (Handle) is.readObject();

      logMsg("create object reference from deserialized handle");
      EJBObject ejbObject = deserializedHandle.getEJBObject();
      TestBean beanRef2 = (TestBean) PortableRemoteObject.narrow(ejbObject,
          TestBean.class);

      boolean pass = true;

      if (!beanRef.isIdentical(beanRef2)) {
        logErr("bean references not equal - unexpected");
        pass = false;
      } else
        logMsg("bean references equal - expected");

      logMsg("ping object via deserialized object reference");
      int ping2Count = beanRef2.ping(0);

      if (ping2Count != pingCount && pingCount != 1) {
        logMsg(
            "ping count: expected " + pingCount + ", received " + ping2Count);
        pass = false;
      }

      if (!pass)
        throw new Fault("test2 failed");
      beanRef2.remove();
    } catch (Exception e) {
      throw new Fault("test2 failed", e);
    } finally {
      try {
        logMsg("closing object streams");
        if (is != null)
          is.close();
        if (os != null)
          os.close();
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: test3
   * 
   * @assertion_ids: EJB:SPEC:665; EJB:SPEC:666; EJB:SPEC:667; EJB:SPEC:668;
   * EJB:SPEC:685; EJB:SPEC:678.1
   * 
   * @test_Strategy: Create a stateful Session EJBean. Deploy it on the J2EE
   * server. Obtain handle, serialize/deserialize handle, invoke bean object
   * with deserialized handle. A NoSuchObject exception should occur since the
   * bean object no longer exists.
   */

  public void test3() throws Fault {
    ObjectInputStream is = null;
    ObjectOutputStream os = null;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props);

      logMsg("ping object");
      beanRef.ping(0);

      logMsg("get handle for object");
      Handle handle = beanRef.getHandle();

      logMsg("write object output stream to byte array");
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      os = new ObjectOutputStream(baos);
      os.writeObject(handle);

      logMsg("removing object instance of EJB");
      beanRef.remove();

      logMsg("read object output stream from byte array");
      byte[] b = baos.toByteArray();
      ByteArrayInputStream bais = new ByteArrayInputStream(b);
      is = new EJBHandleObjectInputStream(bais);
      Handle deserializedHandle = (Handle) is.readObject();

      boolean pass = true;

      try {
        logMsg("create object reference from deserialized handle");
        EJBObject ejbObject = deserializedHandle.getEJBObject();
        TestBean beanRef2 = (TestBean) PortableRemoteObject.narrow(ejbObject,
            TestBean.class);
        logMsg("ping object via deserialized object reference");
        int ping2Count = beanRef2.ping(0);
        logErr("No exception occurred - unexpected");
        pass = false;
      } catch (NoSuchObjectException e) {
        logMsg("Caught NoSuchObjectException - expected");
      } catch (RemoteException e) {
        logMsg("Caught RemoteException - expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logMsg("Unexpected exception - " + e);
        pass = false;
      }

      if (!pass)
        throw new Fault("test3 failed");
    } catch (Exception e) {
      throw new Fault("test3 failed", e);
    } finally {
      try {
        logMsg("closing object streams");
        if (is != null)
          is.close();
        if (os != null)
          os.close();
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: test4
   * 
   * @assertion_ids: EJB:SPEC:665; EJB:SPEC:666; EJB:SPEC:667; EJB:SPEC:668;
   * EJB:SPEC:685; EJB:SPEC:678.1
   * 
   * @test_Strategy: Create a Session Bean. Deploy it on the J2EE server. Obtain
   * handle and ensure it is serializable.
   *
   */

  public void test4() throws Fault {
    boolean pass = true;
    try {
      logMsg("get handle for home");
      HomeHandle handle = beanHome.getHomeHandle();
      if (handle == null) {
        pass = false;
        logErr("getHomeHandle() returned null reference");
      } else if (!(handle instanceof Serializable)) {
        pass = false;
        ;
        logErr("getHomeHandle() is not serializable");
      } else
        logMsg("got home handle and handle is serializable");
    } catch (Exception e) {
      throw new Fault("test4 failed", e);
    }

    if (!pass)
      throw new Fault("test4 failed");
  }

  /*
   * @testName: test5
   * 
   * @assertion_ids: EJB:SPEC:665; EJB:SPEC:666; EJB:SPEC:667; EJB:SPEC:668;
   * EJB:SPEC:685; EJB:SPEC:678.1
   * 
   * @test_Strategy: Deploy a Session Bean on the J2EE server. Obtain home
   * handle, serialize/deserialize handle, invoke bean home with deserialized
   * handle.
   */

  public void test5() throws Fault {
    ObjectInputStream is = null;
    ObjectOutputStream os = null;
    boolean pass = true;
    try {
      logMsg("get handle for home");
      HomeHandle handle = beanHome.getHomeHandle();

      logMsg("write object output stream to byte array");
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      os = new ObjectOutputStream(baos);
      os.writeObject(handle);

      logMsg("read object output stream from byte array");
      byte[] b = baos.toByteArray();
      ByteArrayInputStream bais = new ByteArrayInputStream(b);
      is = new EJBHandleObjectInputStream(bais);
      logMsg("deserialize home handle");
      HomeHandle deserializedHandle = (HomeHandle) is.readObject();

      logMsg("getEJBHome from HomeHandle");
      TestBeanHome beanHome2 = (TestBeanHome) PortableRemoteObject
          .narrow(deserializedHandle.getEJBHome(), TestBeanHome.class);

      // create EJB instance
      logMsg("Create EJB instance from deserialized home handle");
      beanRef = (TestBean) beanHome2.create(props);

      logMsg("ping object");
      beanRef.ping(0);

      logMsg("remove object");
      beanRef.remove();
    } catch (Exception e) {
      throw new Fault("test5 failed", e);
    } finally {
      try {
        logMsg("closing object streams");
        if (is != null)
          is.close();
        if (os != null)
          os.close();
      } catch (Exception e) {
      }
    }

    if (!pass)
      throw new Fault("test5 failed");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }
}
