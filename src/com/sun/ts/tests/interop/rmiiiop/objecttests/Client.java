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

package com.sun.ts.tests.interop.rmiiiop.objecttests;

import com.sun.ts.tests.rmiiiop.ee.objecttests.*;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import java.io.*;
import java.util.*;
import javax.ejb.*;
import java.rmi.*;
import javax.rmi.PortableRemoteObject;
import javax.rmi.CORBA.*;
import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

public class Client extends EETest {
  private static final String testLookup = "java:comp/env/ejb/TestBean";

  private TestBean beanRef = null;

  private TestBeanHome beanHome = null;

  private TestBean beanRef2 = null;

  private TestBeanHome beanHome2 = null;

  private Properties props = null;

  private TSNamingContext nctx = null;

  private HelloRMIIIOPObjectImpl rmiiiopObj = null;

  private HelloJAVAIDLObjectIntf javaidlObj = null;

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
      TestUtil.logMsg("Looking up home interface for EJB: " + testLookup);
      beanHome = (TestBeanHome) nctx.lookup(testLookup, TestBeanHome.class);
      beanRef = beanHome.create(p);
      setupJavaidlRmiiiopObjects(p);
      TestUtil.logMsg("Setup ok");
    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  private void setupJavaidlRmiiiopObjects(Properties p) throws Exception {
    HelloJAVAIDLObjectImpl helloImpl = null;
    ORB orb = null;
    POA rootPOA = null;
    org.omg.CORBA.Object ref = null;
    TestUtil.logMsg("Looking up the ORB and getting RootPOA");
    orb = (ORB) nctx.lookup("java:comp/ORB");
    rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
    rootPOA.the_POAManager().activate();
    TestUtil.logMsg("Create javaidl object: HelloJAVAIDLObjectImpl");
    helloImpl = new HelloJAVAIDLObjectImpl();
    TestUtil.logMsg("Get the reference from the servant");
    ref = rootPOA.servant_to_reference(helloImpl);
    TestUtil.logMsg("Narrow the javaidl object reference");
    javaidlObj = HelloJAVAIDLObjectIntfHelper.narrow(ref);
    TestUtil.logMsg("Create rmiiiop object: HelloRMIIIOPObjectImpl");
    rmiiiopObj = new HelloRMIIIOPObjectImpl();
    TestUtil.logMsg("Get HelloRMIIIOPObjectImpl Stub object");
    javax.rmi.CORBA.Stub rmiiiopStub = (javax.rmi.CORBA.Stub) PortableRemoteObject
        .toStub(rmiiiopObj);
    TestUtil.logMsg("Connect/Attach HelloRMIIIOPObjectImpl object to ORB");
    rmiiiopStub.connect(orb);
    rmiiiopObj.passProperties(p);
  }

  /* Test cleanup */

  public void cleanup() throws Fault {
    rmiiiopObj = null;
    javaidlObj = null;
    TestUtil.logMsg("cleanup ok");
  }

  /* Run test */

  /*
   * @testName: RmiiiopObjectTest01
   * 
   * @assertion_ids: JavaEE:SPEC:287;
   * 
   * @assertion: A J2EE product must be able to pass a rmiiiop object reference
   * to an enterprise bean and be able to invoke methods on the object reference
   * passed. J2EE Platform Spec (Chapter 6.4 EJB Requirements) EJB Spec (Chapter
   * 19) [See EJB assertions]
   * 
   * @test_Strategy: Create a stateful Session Bean. Deploy it on the J2EE
   * server. Pass a rmiiiop object reference to a session bean and have the
   * session bean invoke methods on the passed object refernce.
   *
   */

  public void RmiiiopObjectTest01() throws Fault {
    boolean pass = true;

    try {
      TestUtil.logTrace("RmiiiopObjectTest01");
      TestUtil.logMsg("Pass a rmiiiop object to TestBeanEJB");
      pass = beanRef.pass_a_rmiiiop_object(rmiiiopObj);
      if (!pass) {
        TestUtil.logErr("Could not pass a rmiiiop object to TestBeanEJB");
        TestUtil.logErr("Wrong return, got false, expected true");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      TestUtil.logErr(e.toString());
      pass = false;
    } finally {
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    if (!pass)
      throw new Fault("RmiiiopObjectTest01 failed");
  }

  /*
   * @testName: RmiiiopObjectTest02
   * 
   * @assertion_ids: JavaEE:SPEC:287;
   * 
   * @assertion: A J2EE product must be able to return a rmiiiop object
   * reference from an enterprise bean and be able to invoke methods on the
   * returned object reference. J2EE Platform Spec (Chapter 6.4 EJB
   * Requirements) EJB Spec (Chapter 19) [See EJB assertions]
   * 
   * @test_Strategy: Create a stateful Session Bean. Deploy it on the J2EE
   * server. Return a rmiiiop object reference from a session bean and invoke
   * methods on the returned object reference.
   *
   */

  public void RmiiiopObjectTest02() throws Fault {
    boolean pass = true;
    String expStr = "Hello from HelloRMIIIOPObjectImpl";

    try {
      TestUtil.logTrace("RmiiiopObjectTest02");
      TestUtil.logMsg("Return a rmiiiop object from TestBeanEJB");
      HelloRMIIIOPObjectIntf rmiiiopRef = beanRef
          .return_a_rmiiiop_object(rmiiiopObj);
      if (rmiiiopRef == null) {
        TestUtil.logErr("Could not return a rmiiiop object (ref is null)");
        pass = false;
      } else {
        try {
          TestUtil.logMsg("Invoke methods on the rmiiiop object reference");
          String hello = rmiiiopRef.hello();
          if (!hello.equals(expStr)) {
            TestUtil.logErr("Wrong message, got [" + hello + "]");
            TestUtil.logErr("Wrong message, expected [" + expStr + "]");
            pass = false;
          }
        } catch (Exception e) {
          TestUtil.logErr("Unable to invoke methods of the rmiiiop object");
          TestUtil.printStackTrace(e);
          TestUtil.logErr(e.toString());
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      TestUtil.logErr(e.toString());
      pass = false;
    } finally {
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    if (!pass)
      throw new Fault("RmiiiopObjectTest02 failed");
  }

  /*
   * @testName: RmiiiopObjectTest03
   * 
   * @assertion_ids: JavaEE:SPEC:288;
   * 
   * @assertion: A J2EE product must be able to pass an EJBHome object reference
   * to a rmiiiop object and the rmiiiop object must be able to use that EJBHome
   * object reference. J2EE Platform Spec (Chapter 6.4 EJB Requirements) EJB
   * Spec (Chapter 19) [See EJB assertions]
   * 
   * @test_Strategy: Create a stateful Session Bean. Deploy it on the J2EE
   * server. Pass an EJBHome object reference to a rmiiiop object and then use
   * that EJBHome object reference from within the rmiiiop object.
   *
   */

  public void RmiiiopObjectTest03() throws Fault {
    boolean pass = true;

    try {
      TestUtil.logTrace("RmiiiopObjectTest03");
      TestUtil.logMsg("Pass a EJBHome object to rmiiiop object");
      pass = rmiiiopObj.passEjbHome(beanHome);
      if (!pass) {
        TestUtil.logErr("Could not pass a EJBHome object to rmiiiop object");
        TestUtil.logErr("Wrong return, got false, expected true");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      TestUtil.logErr(e.toString());
      pass = false;
    } finally {
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    if (!pass)
      throw new Fault("RmiiiopObjectTest03 failed");
  }

  /*
   * @testName: RmiiiopObjectTest04
   * 
   * @assertion_ids: JavaEE:SPEC:288;
   * 
   * @assertion: A J2EE product must be able to pass an EJBObject object
   * reference to a rmiiiop object and the rmiiiop object must be able to use
   * that EJBObject object reference. J2EE Platform Spec (Chapter 6.4 EJB
   * Requirements) EJB Spec (Chapter 19) [See EJB assertions]
   * 
   * @test_Strategy: Create a stateful Session Bean. Deploy it on the J2EE
   * server. Pass an EJBObject object reference to a rmiiiop object and then use
   * that EJBObject object reference from within the rmiiiop object.
   *
   */

  public void RmiiiopObjectTest04() throws Fault {
    boolean pass = true;

    try {
      TestUtil.logTrace("RmiiiopObjectTest04");
      TestUtil.logMsg("Pass a EJBObject object to rmiiiop object");
      pass = rmiiiopObj.passEjbObject(beanRef);
      if (!pass) {
        TestUtil.logErr("Could not pass a EJBObject object to rmiiiop object");
        TestUtil.logErr("Wrong return, got false, expected true");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      TestUtil.logErr(e.toString());
      pass = false;
    } finally {
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    if (!pass)
      throw new Fault("RmiiiopObjectTest04 failed");
  }

  /*
   * @testName: RmiiiopObjectTest05
   * 
   * @assertion_ids: JavaEE:SPEC:288;
   * 
   * @assertion: A J2EE product must be able to return an EJBHome object
   * reference from a rmiiiop object and it must be able to use that EJBHome
   * object reference. J2EE Platform Spec (Chapter 6.4 EJB Requirements) EJB
   * Spec (Chapter 19) [See EJB assertions]
   * 
   * @test_Strategy: Create a stateful Session Bean. Deploy it on the J2EE
   * server. Return an EJBHome object reference from a rmiiiop object and then
   * use that EJBHome object reference.
   *
   */

  public void RmiiiopObjectTest05() throws Fault {
    boolean pass = true;
    String expStr = "Hello from TestBeanEJB";

    try {
      TestUtil.logTrace("RmiiiopObjectTest05");
      TestUtil.logMsg("Return a EJBHome object from a rmiiiop object");
      beanHome2 = rmiiiopObj.returnEjbHome(beanHome);
      if (beanHome2 == null) {
        TestUtil.logErr("Could not return EJBHome object (ref is null)");
        pass = false;
      } else {
        try {
          TestUtil.logMsg("Invoke methods on the EJBHome object reference");
          TestUtil.logMsg("Create EJB instance from EJBHome object reference");
          beanRef2 = beanHome2.create(props);
          String hello = beanRef2.hello();
          if (!hello.equals(expStr)) {
            TestUtil.logErr("Wrong message, got [" + hello + "]");
            TestUtil.logErr("Wrong message, expected [" + expStr + "]");
            pass = false;
          }
        } catch (Exception e) {
          TestUtil.logErr("Unable to invoke methods of the EJBHome object");
          TestUtil.printStackTrace(e);
          TestUtil.logErr(e.toString());
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      TestUtil.logErr(e.toString());
      pass = false;
    } finally {
      try {
        beanRef.remove();
        beanRef2.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    if (!pass)
      throw new Fault("RmiiiopObjectTest05 failed");
  }

  /*
   * @testName: RmiiiopObjectTest06
   * 
   * @assertion_ids: JavaEE:SPEC:288;
   * 
   * @assertion: A J2EE product must be able to return an EJBObject object
   * reference from a rmiiiop object and it must be able to use that EJBObject
   * object reference. J2EE Platform Spec (Chapter 6.4 EJB Requirements) EJB
   * Spec (Chapter 19) [See EJB assertions]
   * 
   * @test_Strategy: Create a stateful Session Bean. Deploy it on the J2EE
   * server. Return an EJBObject object reference from a rmiiiop object and then
   * use that EJBObject object reference.
   *
   */

  public void RmiiiopObjectTest06() throws Fault {
    boolean pass = true;
    String expStr = "Hello from TestBeanEJB";

    try {
      TestUtil.logTrace("RmiiiopObjectTest06");
      TestUtil.logMsg("Return a EJBObject object from a rmiiiop object");
      beanRef = rmiiiopObj.returnEjbObject(beanRef);
      if (beanRef == null) {
        TestUtil.logErr("Could not return EJBObject object (ref is null)");
        pass = false;
      } else {
        try {
          TestUtil.logMsg("Invoke methods on the EJBObject object reference");
          String hello = beanRef.hello();
          if (!hello.equals(expStr)) {
            TestUtil.logErr("Wrong message, got [" + hello + "]");
            TestUtil.logErr("Wrong message, expected [" + expStr + "]");
            pass = false;
          }
        } catch (Exception e) {
          TestUtil.logErr("Unable to invoke methods of the EJBObject object");
          TestUtil.printStackTrace(e);
          TestUtil.logErr(e.toString());
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      TestUtil.logErr(e.toString());
      pass = false;
    } finally {
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    if (!pass)
      throw new Fault("RmiiiopObjectTest06 failed");
  }

  /**
   * @testName: JavaidlObjectTest01
   * @assertion_ids: JavaEE:SPEC:287;
   * @assertion: A J2EE product must be able to pass a javaidl object reference
   *             to an enterprise bean and be able to invoke methods on the
   *             object reference passed. J2EE Platform Spec (Chapter 6.4 EJB
   *             Requirements) EJB Spec (Chapter 19) [See EJB assertions]
   *
   * @test_Strategy: Create a stateful Session Bean. Deploy it on the J2EE
   *                 server. Pass a javaidl object reference to a session bean
   *                 and have the session bean invoke methods on the passed
   *                 object refernce.
   *
   */
  public void JavaidlObjectTest01() throws Fault {
    boolean pass = true;

    try {
      TestUtil.logTrace("JavaidlObjectTest01");
      TestUtil.logMsg("Pass a javaidl object to TestBeanEJB");
      pass = beanRef.pass_a_javaidl_object(javaidlObj);
      if (!pass) {
        TestUtil.logErr("Could not pass a javaidl object to TestBeanEJB");
        TestUtil.logErr("Wrong return, got false, expected true");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      TestUtil.logErr(e.toString());
      pass = false;
    } finally {
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    if (!pass)
      throw new Fault("JavaidlObjectTest01 failed");
  }

  /**
   * @testName: JavaidlObjectTest02
   * @assertion_ids: JavaEE:SPEC:287;
   * @assertion: A J2EE product must be able to return a javaidl object
   *             reference from an enterprise bean and be able to invoke methods
   *             on the returned object reference. J2EE Platform Spec (Chapter
   *             6.4 EJB Requirements) EJB Spec (Chapter 19) [See EJB
   *             assertions]
   *
   * @test_Strategy: Create a stateful Session Bean. Deploy it on the J2EE
   *                 server. Return a javaidl object reference from a session
   *                 bean and invoke methods on the returned object reference.
   *
   */
  public void JavaidlObjectTest02() throws Fault {
    boolean pass = true;
    String expStr = "Hello from HelloJAVAIDLObjectImpl";

    try {
      TestUtil.logTrace("JavaidlObjectTest02");
      TestUtil.logMsg("Return a javaidl object from TestBeanEJB");
      HelloJAVAIDLObjectIntf javaidlRef = beanRef
          .return_a_javaidl_object(javaidlObj);
      if (javaidlRef == null) {
        TestUtil.logErr("Could not return javaidl object (ref is null)");
        pass = false;
      } else {
        try {
          TestUtil.logMsg("Invoke methods on the javaidl object reference");
          String hello = javaidlRef.hello();
          if (!hello.equals(expStr)) {
            pass = false;
            TestUtil.logErr("Wrong message, got [" + hello + "]"
                + ", expected [" + expStr + "]");
          }
        } catch (Exception e) {
          TestUtil.logErr("Unable to invoke methods of the javaidl object");
          TestUtil.printStackTrace(e);
          TestUtil.logErr(e.toString());
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      TestUtil.logErr(e.toString());
      pass = false;
    } finally {
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    if (!pass)
      throw new Fault("JavaidlObjectTest02 failed");
  }

}
