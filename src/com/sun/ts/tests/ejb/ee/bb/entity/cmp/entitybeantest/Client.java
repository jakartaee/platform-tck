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
 * @(#)Client.java	1.32 03/05/16
 */

package com.sun.ts.tests.ejb.ee.bb.entity.cmp.entitybeantest;

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

//****************************************************
//EntityBean Lifecyle Test for CMP entity beans
//****************************************************

public class Client extends EETest {
  private static final String testName = "EntityBeanTest";

  private static final String testBean = "java:comp/env/ejb/TestBean";

  private static final String helperBean = "java:comp/env/ejb/HelperBean";

  private static final String testProps = "entitybeantest.properties";

  private static final String testDir = System.getProperty("user.dir");

  private TestBean beanRef = null;

  private TestBeanHome beanHome = null;

  private Properties props = null;

  private TSNamingContext nctx = null;

  private Helper helperRef = null;

  private HelperHome helperHome = null;

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

      logMsg("Looking up home interface for EJB: " + helperBean);
      helperHome = (HelperHome) nctx.lookup(helperBean, HelperHome.class);

      logMsg("Creating helper EJB");
      helperRef = (Helper) helperHome.create(props);

      logMsg("Setup ok");
    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  /* Run test */

  /*
   * @testName: test1
   * 
   * @assertion_ids: EJB:SPEC:431
   * 
   * @test_Strategy: Create an CMP 1.1 entity bean. Deploy it on the J2EE
   * server. Check creation life cycle call flow occurs. Verifies proper
   * ejbCreate/ejbPostCreate method called and correct lifecycle creation call
   * flow.
   *
   */

  public void test1() throws Fault {
    boolean pass = true;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 1, "coffee-1", 2, helperRef);
      logMsg("check if proper lifecycle creation order was called in the bean");
      pass = helperRef.isCreateLifeCycle1();
      beanRef.remove();
    } catch (Exception e) {
      throw new Fault("test1 failed", e);
    }
    if (!pass)
      throw new Fault("test1 failed");
  }

  /*
   * @testName: test2
   * 
   * @assertion_ids: EJB:SPEC:431
   * 
   * @test_Strategy: Create an CMP 1.1 entity bean. Deploy it on the J2EE
   * server. Check creation life cycle call flow occurs. Verifies proper
   * ejbCreate/ejbPostCreate method called and correct lifecycle creation call
   * flow. This test uses a different create method than above.
   *
   */

  public void test2() throws Fault {
    boolean pass = true;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 1, helperRef);
      logMsg("check if proper lifecycle creation order was called in the bean");
      pass = helperRef.isCreateLifeCycle2();
      beanRef.remove();
    } catch (Exception e) {
      throw new Fault("test2 failed", e);
    }
    if (!pass)
      throw new Fault("test2 failed");
  }

  /*
   * @testName: test3
   * 
   * @assertion_ids: EJB:SPEC:144.6; EJB:SPEC:145
   * 
   * @test_Strategy: Create an entity CMP 1.1 bean. Deploy it on the J2EE
   * server. Call ejbRemove() and check a Helper object was notified to ensure
   * ejbRemove was called. Invoke remove via EJBObject interface twice. A
   * subsequent call to remove should return NoSuchObjectException or
   * RemoteException.
   *
   */

  public void test3() throws Fault {
    boolean pass = true;
    try {
      helperRef.reset();
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 1, helperRef);
      logMsg("Remove bean and check ejbRemove() was called in the bean");
      beanRef.remove();
      // Bean may not have handle on helperRef
      pass = helperRef.isRemove();
      if (pass)
        logMsg("ejbRemove was called - expected");
      else {
        try {
          TestBean theBean = beanHome.findByPrimaryKey(new Integer(1));
          pass = false;
          logErr("findByPrimaryKey succeeded after entity removal");
        } catch (FinderException e) {
          TestUtil.printStackTrace(e);
          logMsg("FinderException occurs, entity was removed");
          pass = true;
        } catch (RemoteException e) {
          logErr("RemoteException occurs, expected FinderException");
          pass = false;
        }
      }
      logMsg("attempt to invoke bean after removal");
      try {
        beanRef.remove();
        logErr("object was not removed, was able to invoke object");
        pass = false;
      } catch (NoSuchObjectException e) {
        logMsg("NoSuchObjectException received as expected");
      } catch (RemoteException e) {
        logMsg("RemoteException received as expected");
      } catch (Exception e) {
        logErr("Unexpected exception: " + e);
        throw new Fault("test3 failed", e);
      }
    } catch (Exception e) {
      throw new Fault("test3 failed", e);
    }
    if (!pass)
      throw new Fault("test3 failed");
  }

  /*
   * @testName: test3a
   * 
   * @assertion_ids: EJB:SPEC:144.6; EJB:SPEC:145
   * 
   * @test_Strategy: Create a CMP 1.1 entity bean. Deploy it on the J2EE server.
   * Call ejbRemove() and check a Helper object was notified to ensure ejbRemove
   * was called. Invoke remove through EJBHome via handle twice. A subsequent
   * call to remove should return NoSuchObjectException or RemoteException.
   *
   */

  public void test3a() throws Fault {
    boolean pass = true;
    try {
      helperRef.reset();
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 1, helperRef);
      logMsg("Remove bean and check ejbRemove() was called in the bean");
      Handle handle = beanRef.getHandle();
      beanHome.remove(handle);
      // Bean may not have handle on helperRef
      pass = helperRef.isRemove();
      if (pass)
        logMsg("ejbRemove was called - expected");
      else {
        try {
          TestBean theBean = beanHome.findByPrimaryKey(new Integer(1));
          pass = false;
          logErr("findByPrimaryKey succeeded after entity removal");
        } catch (FinderException e) {
          TestUtil.printStackTrace(e);
          logMsg("FinderException occurs, entity was removed");
          pass = true;
        } catch (RemoteException e) {
          logErr("RemoteException occurs, expected FinderException");
          pass = false;
        }
      }
      logMsg("attempt to invoke bean after removal");
      try {
        beanHome.remove(handle);
        logErr("object was not removed, was able to invoke object");
        pass = false;
      } catch (NoSuchObjectException e) {
        logMsg("NoSuchObjectException received as expected");
      } catch (RemoteException e) {
        logMsg("RemoteException received as expected");
      } catch (Exception e) {
        logErr("Unexpected exception: " + e);
        throw new Fault("test3a failed", e);
      }
    } catch (Exception e) {
      throw new Fault("test3a failed", e);
    }
    if (!pass)
      throw new Fault("test3a failed");

  }

  /*
   * @testName: test3b
   * 
   * @assertion_ids: EJB:SPEC:144.6; EJB:SPEC:145
   * 
   * @test_Strategy: Create a CMP 1.1 entity bean. Deploy it on the J2EE server.
   * Call ejbRemove() and check a Helper object was notified to ensure ejbRemove
   * was called. Invoke remove through EJBHome via PKEY twice. A subsequent call
   * to remove should return NoSuchObjectException or RemoteException.
   *
   */

  public void test3b() throws Fault {
    boolean pass = true;
    try {
      helperRef.reset();
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 1, helperRef);
      logMsg("Remove bean and check ejbRemove() was called in the bean");
      Object pkey = beanRef.getPrimaryKey();
      beanHome.remove(pkey);
      // Bean may not have handle on helperRef
      pass = helperRef.isRemove();
      if (pass)
        logMsg("ejbRemove was called - expected");
      else {
        try {
          TestBean theBean = beanHome.findByPrimaryKey(new Integer(1));
          pass = false;
          logErr("findByPrimaryKey succeeded after entity removal");
        } catch (FinderException e) {
          TestUtil.printStackTrace(e);
          logMsg("FinderException occurs, entity was removed");
          pass = true;
        } catch (RemoteException e) {
          logErr("RemoteException occurs, expected FinderException");
          pass = false;
        }
      }
      logMsg("attempt to invoke bean after removal");
      try {
        beanHome.remove(pkey);
        logErr("object was not removed, was able to invoke object");
        pass = false;
      } catch (NoSuchObjectException e) {
        logMsg("NoSuchObjectException received as expected");
      } catch (RemoteException e) {
        logMsg("RemoteException received as expected");
      } catch (Exception e) {
        logErr("Unexpected exception: " + e);
        throw new Fault("test3b failed", e);
      }
    } catch (Exception e) {
      throw new Fault("test3b failed", e);
    }
    if (!pass)
      throw new Fault("test3b failed");

  }

  /*
   * @testName: test3c
   * 
   * @assertion_ids: EJB:SPEC:431
   * 
   * @test_Strategy: Container provides notification to bean of removal via
   * callback ejbRemove(). Invoke remove through EJBObject interface and attempt
   * to call a business method afterwards. Create a CMP 1.1 entity bean. Deploy
   * it on the J2EE server. Call ejbRemove() and check a Helper object was
   * notified to ensure ejbRemove was called.
   *
   */

  public void test3c() throws Fault {
    boolean pass = true;
    try {
      helperRef.reset();
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 1, helperRef);
      logMsg("Remove bean and check ejbRemove() was called in the bean");
      beanRef.remove();
      // Bean may not have handle on helperRef
      pass = helperRef.isRemove();
      if (pass)
        logMsg("ejbRemove was called - expected");
      else {
        try {
          TestBean theBean = beanHome.findByPrimaryKey(new Integer(1));
          pass = false;
          logErr("findByPrimaryKey succeeded after entity removal");
        } catch (FinderException e) {
          TestUtil.printStackTrace(e);
          logMsg("FinderException occurs, entity was removed");
          pass = true;
        } catch (RemoteException e) {
          logErr("RemoteException occurs, expected FinderException");
          pass = false;
        }
      }
      logMsg("attempt to invoke bean after removal");
      try {
        beanRef.ping();
        logErr("object was not removed, was able to invoke object");
        pass = false;
      } catch (NoSuchObjectException e) {
        logMsg("NoSuchObjectException received as expected: " + e);
      } catch (RemoteException e) {
        logMsg("RemoteException received as expected: " + e);
      }
    } catch (Exception e) {
      throw new Fault("test3c failed", e);
    }
    if (!pass)
      throw new Fault("test3c failed");
  }

  /*
   * @testName: test3d
   * 
   * @assertion_ids: EJB:SPEC:431
   * 
   * @test_Strategy: Container provides notification to bean of removal via
   * callback ejbRemove(). Invoke remove through EJBObject interface via handle
   * and attempt to call a business method afterwards. Create a CMP 1.1 entity
   * bean. Deploy it on the J2EE server. Call ejbRemove() and check a Helper
   * object was notified to ensure ejbRemove was called.
   *
   */

  public void test3d() throws Fault {
    boolean pass = true;
    try {
      helperRef.reset();
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 1, helperRef);
      logMsg("Remove bean and check ejbRemove() was called in the bean");
      Handle handle = beanRef.getHandle();
      beanHome.remove(handle);
      // Bean may not have handle on helperRef
      pass = helperRef.isRemove();
      if (pass)
        logMsg("ejbRemove was called - expected");
      else {
        try {
          TestBean theBean = beanHome.findByPrimaryKey(new Integer(1));
          pass = false;
          logErr("findByPrimaryKey succeeded after entity removal");
        } catch (FinderException e) {
          TestUtil.printStackTrace(e);
          logMsg("FinderException occurs, entity was removed");
          pass = true;
        } catch (RemoteException e) {
          logErr("RemoteException occurs, expected FinderException");
          pass = false;
        }
      }
      logMsg("attempt to invoke bean after removal");
      try {
        beanRef.ping();
        logErr("object was not removed, was able to invoke object");
        pass = false;
      } catch (NoSuchObjectException e) {
        logMsg("NoSuchObjectException received as expected: " + e);
      } catch (RemoteException e) {
        logMsg("RemoteException received as expected: " + e);
      }
    } catch (Exception e) {
      throw new Fault("test3d failed", e);
    }
    if (!pass)
      throw new Fault("test3d failed");
  }

  /*
   * @testName: test3e
   * 
   * @assertion_ids: EJB:SPEC:431
   * 
   * @test_Strategy: Container provides notification to bean of removal via
   * callback ejbRemove(). Invoke remove through EJBObject via PKEY twice.
   * Create a CMP 1.1 entity bean. Deploy it on the J2EE server. Call
   * ejbRemove() and check a Helper object was notified to ensure ejbRemove was
   * called.
   *
   */

  public void test3e() throws Fault {
    boolean pass = true;
    try {
      helperRef.reset();
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 1, helperRef);
      logMsg("Remove bean and check ejbRemove() was called in the bean");
      Object pkey = beanRef.getPrimaryKey();
      beanHome.remove(pkey);
      // Bean may not have handle on helperRef
      pass = helperRef.isRemove();
      if (pass)
        logMsg("ejbRemove was called - expected");
      else {
        try {
          TestBean theBean = beanHome.findByPrimaryKey(new Integer(1));
          pass = false;
          logErr("findByPrimaryKey succeeded after entity removal");
        } catch (FinderException e) {
          TestUtil.printStackTrace(e);
          logMsg("FinderException occurs, entity was removed");
          pass = true;
        } catch (RemoteException e) {
          logErr("RemoteException occurs, expected FinderException");
          pass = false;
        }
      }
      logMsg("attempt to invoke bean after removal");
      try {
        beanRef.ping();
        logErr("object was not removed, was able to invoke object");
        pass = false;
      } catch (NoSuchObjectException e) {
        logMsg("NoSuchObjectException received as expected: " + e);
      } catch (RemoteException e) {
        logMsg("RemoteException received as expected: " + e);
      }
    } catch (Exception e) {
      throw new Fault("test3e failed", e);
    }
    if (!pass)
      throw new Fault("test3e failed");
  }

  /*
   * @testName: test4
   * 
   * @assertion_ids: EJB:SPEC:135
   * 
   * @test_Strategy: Create an entity bean. Deploy it on the J2EE server.
   * Perform a findByPrimaryKey and ensure entity object is obtained.
   *
   */

  public void test4() throws Fault {
    boolean pass = true;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 2, helperRef);
      logMsg("Perform a find by primary key on new entity");
      TestBean beanRef2 = beanHome.findByPrimaryKey(new Integer(2));
      if (beanRef2 == null) {
        TestUtil.logErr("find by primaryKey did not yield entity bean");
        pass = false;
      } else
        TestUtil.logMsg("find by primaryKey did yield entity bean");

      TestUtil.logMsg("Check if primary keys match");
      if (((Integer) beanRef2.getPrimaryKey()).intValue() != 2)
        TestUtil.logErr("primary key does not match expected");
      else
        TestUtil.logMsg("primary key does match expected");

      beanRef.remove();
    } catch (Exception e) {
      throw new Fault("test4 failed", e);
    }
    if (!pass)
      throw new Fault("test4 failed");
  }

  /*
   * @testName: test7
   * 
   * @assertion_ids: EJB:SPEC:633.2; EJB:SPEC:633.3
   * 
   * @test_Strategy: Create an entity bean. Deploy it on the J2EE server. Throw
   * a EJBException instance will be discarded.
   *
   */

  public void test7() throws Fault {
    boolean pass = true;
    try {
      helperRef.reset();
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 1, helperRef);
      logMsg("invoke bean to throw EJBException");
      try {
        beanRef.throwEJBException();
        logErr("No EJBException occurred");
        pass = false;
      } catch (RemoteException e) {
        logMsg("RemoteException received as expected: " + e);
      } catch (Exception e) {
        logErr("Exception received - unexpected: " + e, e);
        pass = false;
      }
      if (beanRef.iAmDestroyed()) {
        TestUtil.logErr("instance has not been destroyed");
        pass = false;
      } else
        TestUtil.logMsg("instance has been destroyed");
    } catch (Exception e) {
      throw new Fault("test7 failed", e);
    } finally {
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    if (!pass)
      throw new Fault("test7 failed");
  }

  /*
   * @testName: test8
   * 
   * @assertion_ids: EJB:SPEC:633.2; EJB:SPEC:633.3
   * 
   * @test_Strategy: Create an entity bean. Deploy it on the J2EE server. Throw
   * a Error instance will be discarded.
   *
   */

  public void test8() throws Fault {
    boolean pass = true;
    try {
      helperRef.reset();
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 1, helperRef);
      logMsg("invoke bean to throw Error");
      try {
        beanRef.throwError();
        logErr("No Error occurred");
        pass = false;
      } catch (RemoteException e) {
        logMsg("RemoteException received as expected: " + e);
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logMsg("Exception received - unexpected: " + e);
        pass = false;
      }
      if (beanRef.iAmDestroyed()) {
        TestUtil.logErr("instance has not been destroyed");
        pass = false;
      } else
        TestUtil.logMsg("instance has been destroyed");
    } catch (Exception e) {
      throw new Fault("test8 failed", e);
    } finally {
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    if (!pass)
      throw new Fault("test8 failed");
  }

  /*
   * @testName: test10
   * 
   * @assertion_ids: EJB:SPEC:431; EJB:SPEC:502
   * 
   * @test_Strategy: Create an entity bean. Deploy it on the J2EE server. Check
   * that ejbStore was called after invoking business method.
   *
   */

  public void test10() throws Fault {
    boolean pass = true;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 1, "coffee-1", 2, helperRef);
      helperRef.reset();
      logMsg("check if ejbStore was invoked");
      beanRef.loadOrStoreTest(helperRef);
      if (helperRef.isStore())
        logMsg("ejbStore was called");
      else {
        logErr("ejbStore was not called");
        pass = false;
      }
    } catch (Exception e) {
      throw new Fault("test10 failed", e);
    } finally {
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    if (!pass)
      throw new Fault("test10 failed");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }
}
