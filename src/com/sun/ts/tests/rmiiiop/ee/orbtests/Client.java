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

package com.sun.ts.tests.rmiiiop.ee.orbtests;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.javatest.Status;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.ejb.*;
import javax.rmi.PortableRemoteObject;
import javax.rmi.CORBA.*;
import java.rmi.*;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

public class Client extends EETest {
  private static final String testLookup = "java:comp/env/ejb/TestBean";

  private TestBean beanRef = null;

  private TestBeanHome beanHome = null;

  private Properties props = null;

  private TSNamingContext nctx = null;

  private HelloRMIIIOPObjectIntf rmiiiopRef = null;

  private HelloRMIIIOPObjectImpl rmiiiopObj = null;

  private javax.rmi.CORBA.Stub rmiiiopStub = null;

  private org.omg.CORBA.ORB orb = null;

  private org.omg.CORBA.Object obj = null;

  private org.omg.PortableServer.POA rootPOA = null;

  private String ior = null;

  private TSURL ctsurl = new TSURL();

  private URL url = null;

  private URLConnection urlConn = null;

  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private String SERVLET = "/rmiiiop_orbtests_web/RmiiiopOrbtestsServletTest";

  private String webServerHost = "unknown";

  private int webServerPort = 8000;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   * webServerHost; webServerPort;
   */

  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    boolean pass = true;
    try {
      TestUtil.logMsg("Obtain naming context");
      nctx = new TSNamingContext();
      TestUtil.logMsg("Lookup EJBHome for: " + testLookup);
      beanHome = (TestBeanHome) nctx.lookup(testLookup, TestBeanHome.class);
      TestUtil.logMsg("Create EJBObject instance");
      beanRef = beanHome.create(p);
      TestUtil.logMsg("Get webServerHost and webServerPort settings");
      webServerHost = p.getProperty(WEBSERVERHOSTPROP);
      if (webServerHost == null)
        pass = false;
      else if (webServerHost.equals(""))
        pass = false;
      try {
        webServerPort = Integer.parseInt(p.getProperty(WEBSERVERPORTPROP));
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        pass = false;
      }
      TestUtil.logMsg("webServerHost = " + webServerHost);
      TestUtil.logMsg("webServerPort = " + webServerPort);
      if (!pass) {
        TestUtil.logErr(
            "Please specify host & port of web server " + "in ts.jte file: "
                + WEBSERVERHOSTPROP + ", " + WEBSERVERPORTPROP);
        throw new Fault("Setup failed:");
      }
      TestUtil.logMsg("Setup ok");
    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  private void SetupOrbAndRmiiiopObject() throws Exception {
    TestUtil.logTrace("SetupOrbAndRmiiiopObject");
    TestUtil.logMsg("Create an ORB instance using [ORB.init()]");
    orb = ORB.init(new String[0], null);
    TestUtil.logMsg("ORB = " + orb);
    TestUtil.logMsg("Verify some basic ORB functionality");
    TestUtil.logMsg("Look up the RootPOA");
    rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
    TestUtil.logMsg("Activate the RootPOA Manager");
    rootPOA.the_POAManager().activate();
    TestUtil.logMsg("Create rmiiiop object: HelloRMIIIOPObjectImpl");
    rmiiiopObj = new HelloRMIIIOPObjectImpl();
    TestUtil.logMsg("Get Stub for HelloRMIIIOPObjectImpl object");
    rmiiiopStub = (javax.rmi.CORBA.Stub) PortableRemoteObject
        .toStub(rmiiiopObj);
    TestUtil.logMsg("Connect/Attach HelloRMIIIOPObjectImpl object to ORB");
    rmiiiopStub.connect(orb);
    TestUtil.logMsg("Convert HelloRMIIIOPObjectImpl to stringified IOR");
    ior = orb.object_to_string((org.omg.CORBA.Object) rmiiiopStub);
    TestUtil.logMsg("IOR = " + ior);
  }

  /* Test cleanup */

  public void cleanup() throws Fault {
    TestUtil.logMsg("cleanup ok");
  }

  /* Run test */

  /*
   * @testName: ORBVerifyFromAppClientTest1
   * 
   * @assertion_ids: JavaEE:SPEC:285; JavaEE:SPEC:10126; JavaEE:SPEC:10127
   * 
   * @assertion: J2EE applications need to use an instance of org.omg.CORBA.ORB
   * to perform many JavaIDL and RMIIIOP operations. The default ORB returned by
   * a call to ORB.init(new String[0], null) must be usable for such purposes.
   * J2EE Platform Spec (Chapter 6.2.2.4 JavaIDL) (Chapter 6.2.2.6 RMIIIOP)
   * 
   * @test_Strategy: Create an ORB instance using ORB.init(). Attach an RMIIIOP
   * object to the ORB. Convert the RMIIIOP object to an IOR using ORB. Convert
   * the IOR back to an RMIIIOP object using ORB. Call a method on the RMIIIOP
   * object and verify the method was called successfully.
   */

  public void ORBVerifyFromAppClientTest1() throws Fault {
    boolean pass = true;
    String expStr = "Hello from HelloRMIIIOPObjectImpl";

    try {
      TestUtil.logTrace("ORBVerifyFromAppClientTest1");
      TestUtil
          .logMsg("Setup an [ORB instance, RMIIIOP object, and save its IOR].");
      SetupOrbAndRmiiiopObject();
      TestUtil.logMsg("Convert stringified IOR back to a CORBA object");
      obj = orb.string_to_object(ior);
      TestUtil.logMsg(
          "Narrow the CORBA object to interface HelloRMIIIOPObjectIntf");
      rmiiiopRef = (HelloRMIIIOPObjectIntf) PortableRemoteObject.narrow(obj,
          HelloRMIIIOPObjectIntf.class);
      TestUtil.logMsg("Call hello method on interface HelloRMIIIOPObjectIntf");
      String hello = rmiiiopRef.hello();
      TestUtil.logMsg("Verify the method call");
      if (!hello.equals(expStr)) {
        TestUtil.logErr("Wrong message, got [" + hello + "]");
        TestUtil.logErr("Wrong message, expected [" + expStr + "]");
        pass = false;
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
      throw new Fault("ORBVerifyFromAppClientTest1 failed");
  }

  /*
   * @testName: ORBVerifyFromEJBTest1
   * 
   * @assertion_ids: JavaEE:SPEC:285; JavaEE:SPEC:10126; JavaEE:SPEC:10127
   * 
   * @assertion: J2EE applications need to use an instance of org.omg.CORBA.ORB
   * to perform many JavaIDL and RMIIIOP operations. The default ORB returned by
   * a call to ORB.init(new String[0], null) must be usable for such purposes.
   * J2EE Platform Spec (Chapter 6.2.2.4 JavaIDL) (Chapter 6.2.2.6 RMIIIOP)
   * 
   * @test_Strategy: Create an ORB instance using ORB.init(). Attach an RMIIIOP
   * object to the ORB. Convert the RMIIIOP object to an IOR using ORB. Pass the
   * stringified IOR to TestBeanEJB. Verify some basic ORB functionality from
   * TestBeanEJB. Create ORB via ORB.init() in TestBeanEJB. Convert the IOR back
   * to an RMIIIOP object using ORB in TestBeanEJB. Call a method on the RMIIIOP
   * object in TestBeanEJB and verify the method was called successfully.
   */

  public void ORBVerifyFromEJBTest1() throws Fault {
    boolean pass = true;

    try {
      TestUtil.logTrace("ORBVerifyFromEJBTest1");
      TestUtil
          .logMsg("Setup an [ORB instance, RMIIIOP object, and save its IOR].");
      SetupOrbAndRmiiiopObject();
      TestUtil.logMsg("Pass stringified IOR to TestBeanEJB.test1() method");
      TestUtil.logMsg("Verify some basic ORB functionality from TestBeanEJB");
      pass = beanRef.test1(ior);
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
      throw new Fault("ORBVerifyFromEJBTest1 failed");
  }

  /*
   * @testName: ORBVerifyFromEJBTest2
   * 
   * @assertion_ids: JavaEE:SPEC:285; JavaEE:SPEC:286; JavaEE:SPEC:10126;
   * JavaEE:SPEC:10127
   * 
   * @assertion: J2EE applications need to use an instance of org.omg.CORBA.ORB
   * to perform many JavaIDL and RMIIIOP operations. A J2EE product must publish
   * and ORB instance in the JNDI name space under the name "java:comp/ORB" to
   * be usable for such purposes. J2EE Platform Spec (Chapter 6.2.2.4 JavaIDL)
   * (Chapter 6.2.2.6 RMIIIOP)
   * 
   * @test_Strategy: Create an ORB instance using ORB.init(). Attach an RMIIIOP
   * object to the ORB. Convert the RMIIIOP object to an IOR using ORB. Pass the
   * stringified IOR to TestBeanEJB. Verify some basic ORB functionality from
   * TestBeanEJB. Lookup ORB instance in JNDI namespace under the name
   * "java:comp/ORB" in TestBeanEJB. Convert the IOR back to an RMIIIOP object
   * using ORB in TestBeanEJB. Call a method on the RMIIIOP object in
   * TestBeanEJB and verify the method was called successfully.
   */

  public void ORBVerifyFromEJBTest2() throws Fault {
    boolean pass = true;

    try {
      TestUtil.logTrace("ORBVerifyFromEJBTest2");
      TestUtil
          .logMsg("Setup an [ORB instance, RMIIIOP object, and save its IOR].");
      SetupOrbAndRmiiiopObject();
      TestUtil.logMsg("Pass stringified IOR to TestBeanEJB.test2() method");
      TestUtil.logMsg("Verify some basic ORB functionality from TestBeanEJB");
      pass = beanRef.test2(ior);
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
      throw new Fault("ORBVerifyFromEJBTest2 failed");
  }

  /*
   * @testName: ORBVerifyFromServletTest1
   * 
   * @assertion_ids: JavaEE:SPEC:285; JavaEE:SPEC:10126; JavaEE:SPEC:10127
   * 
   * @assertion: J2EE applications need to use an instance of org.omg.CORBA.ORB
   * to perform many JavaIDL and RMIIIOP operations. The default ORB returned by
   * a call to ORB.init(new String[0], null) must be usable for such purposes.
   * J2EE Platform Spec (Chapter 6.2.2.4 JavaIDL) (Chapter 6.2.2.6 RMIIIOP)
   * 
   * @test_Strategy: Create an ORB instance using ORB.init(). Attach an RMIIIOP
   * object to the ORB. Convert the RMIIIOP object to an IOR using ORB. Pass the
   * stringified IOR to Servlet. Verify some basic ORB functionality from
   * Servlet. Create ORB via ORB.init() in Servlet. Convert the IOR back to an
   * RMIIIOP object using ORB in Servlet. Call a method on the RMIIIOP object in
   * Servlet and verify the method was called successfully.
   */

  public void ORBVerifyFromServletTest1() throws Fault {
    boolean pass = true;

    try {
      TestUtil.logTrace("ORBVerifyFromServletTest1");
      TestUtil
          .logMsg("Setup an [ORB instance, RMIIIOP object, and save its IOR].");
      SetupOrbAndRmiiiopObject();
      TestUtil.logMsg("Pass stringified IOR to Servlet for test1");
      TestUtil.logMsg("Verify some basic ORB functionality from Servlet");
      url = ctsurl.getURL("http", webServerHost, webServerPort, SERVLET);
      props.setProperty("TEST", "test1");
      props.setProperty("IOR", "" + ior);
      TestUtil.logMsg("Send request to Servlet");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Get response from Servlet");
      TestUtil.logMsg("Response is ................");
      Properties p = TestUtil.getResponseProperties(urlConn);
      TestUtil.list(p);
      String passStr = p.getProperty("TESTRESULT");
      TestUtil.logMsg("TESTRESULT from Servlet = " + passStr);
      if (passStr.equals("false"))
        pass = false;
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
      throw new Fault("ORBVerifyFromServletTest1 failed");
  }

  /*
   * @testName: ORBVerifyFromServletTest2
   * 
   * @assertion_ids: JavaEE:SPEC:285; JavaEE:SPEC:286; JavaEE:SPEC:10126;
   * JavaEE:SPEC:10127
   * 
   * @assertion: J2EE applications need to use an instance of org.omg.CORBA.ORB
   * to perform many JavaIDL and RMIIIOP operations. A J2EE product must publish
   * and ORB instance in the JNDI name space under the name "java:comp/ORB" to
   * be usable for such purposes. J2EE Platform Spec (Chapter 6.2.2.4 JavaIDL)
   * (Chapter 6.2.2.6 RMIIIOP)
   * 
   * @test_Strategy: Create an ORB instance using ORB.init(). Attach an RMIIIOP
   * object to the ORB. Convert the RMIIIOP object to an IOR using ORB. Pass the
   * stringified IOR to Servlet. Verify some basic ORB functionality from
   * Servlet. Lookup ORB instance in JNDI namespace under the name
   * "java:comp/ORB" in Servlet. Convert the IOR back to an RMIIIOP object using
   * ORB in Servlet. Call a method on the RMIIIOP object in Servlet and verify
   * the method was called successfully.
   */

  public void ORBVerifyFromServletTest2() throws Fault {
    boolean pass = true;

    try {
      TestUtil.logTrace("ORBVerifyFromServletTest2");
      TestUtil
          .logMsg("Setup an [ORB instance, RMIIIOP object, and save its IOR].");
      SetupOrbAndRmiiiopObject();
      TestUtil.logMsg("Pass stringified IOR to Servlet for test2");
      TestUtil.logMsg("Verify some basic ORB functionality from Servlet");
      url = ctsurl.getURL("http", webServerHost, webServerPort, SERVLET);
      props.setProperty("TEST", "test2");
      props.setProperty("IOR", "" + ior);
      TestUtil.logMsg("Send request to Servlet");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Get response from Servlet");
      TestUtil.logMsg("Response is ................");
      Properties p = TestUtil.getResponseProperties(urlConn);
      TestUtil.list(p);
      String passStr = p.getProperty("TESTRESULT");
      TestUtil.logMsg("TESTRESULT from Servlet = " + passStr);
      if (passStr.equals("false"))
        pass = false;
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
      throw new Fault("ORBVerifyFromServletTest2 failed");
  }
}
