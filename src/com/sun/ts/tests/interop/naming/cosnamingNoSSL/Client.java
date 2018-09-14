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
 * @(#)Client.java	1.13 03/05/16
 */

package com.sun.ts.tests.interop.naming.cosnamingNoSSL;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.util.*;
import javax.ejb.*;
import javax.rmi.PortableRemoteObject;
import javax.naming.*;
import java.rmi.*;

import com.sun.javatest.Status;

public class Client extends ServiceEETest {
  private final String testSbean1 = "java:comp/env/ejb/TestSessionBean1";

  private final String testSbean2 = "java:comp/env/ejb/TestSessionBean2";

  private final String testSbean3 = "java:comp/env/ejb/TestSessionBean3";

  private final String testSbean4 = "java:comp/env/ejb/TestSessionBean4";

  private final String testSbean5 = "java:comp/env/ejb/TestSessionBean5";

  private final String testSbean6 = "java:comp/env/ejb/TestSessionBean6";

  private final String testSbean7 = "java:comp/env/ejb/TestSessionBean7";

  private final String testSbean8 = "java:comp/env/ejb/TestSessionBean8";

  private final String testSbean9 = "java:comp/env/ejb/TestSessionBean9";

  private final String testSbean10 = "java:comp/env/ejb/TestSessionBean10";

  private final String testEbean1 = "java:comp/env/ejb/TestEntityBean1";

  private final String testEbean2 = "java:comp/env/ejb/TestEntityBean2";

  private final String testEbean3 = "java:comp/env/ejb/TestEntityBean3";

  private final String testEbean4 = "java:comp/env/ejb/TestEntityBean4";

  private final String testEbean5 = "java:comp/env/ejb/TestEntityBean5";

  private final String testEbean6 = "java:comp/env/ejb/TestEntityBean6";

  private final String testEbean7 = "java:comp/env/ejb/TestEntityBean7";

  private final String testEbean8 = "java:comp/env/ejb/TestEntityBean8";

  private final String testEbean9 = "java:comp/env/ejb/TestEntityBean9";

  private final String testEbean10 = "java:comp/env/ejb/TestSessionBean10";

  private final String objectEjbE = "interop_cosnamingNoSSL_TestEntityBean";

  private final String objectEjbS = "interop_cosnamingNoSSL_TestSessionBean";

  private final String objectKey = "/NameService";

  private String namingServiceHost = null;

  private String namingServicePort = null;

  private TestSessionBean beanSref = null;

  private TestSessionBeanHome beanShome = null;

  private TestEntityBean beanEref = null;

  private TestEntityBeanHome beanEhome = null;

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
   * namingServiceHost2; namingServicePort2; generateSQL;
   */

  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    try {
      TestUtil.logMsg("Obtain naming context");
      nctx = new TSNamingContext();
      TestUtil.logMsg("Read namingServiceHost2 from property object");
      namingServiceHost = props.getProperty("namingServiceHost2");
      TestUtil.logMsg("CosNaming Service Host = " + namingServiceHost);
      TestUtil.logMsg("Read namingServicePort2 from property object");
      namingServicePort = props.getProperty("namingServicePort2");
      TestUtil.logMsg("CosNaming Service Port = " + namingServicePort);
      TestUtil.logMsg("Setup ok");
    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  /* Test cleanup */

  public void cleanup() throws Fault {
    TestUtil.logMsg("cleanup ok");
  }

  /* Run test */

  /*
   * @testName: CosNamingNoSSLTest01
   * 
   * @assertion_ids: EJB:SPEC:664; EJB:SPEC:720; EJB:SPEC:721; EJB:SPEC:722;
   * EJB:SPEC:723
   * 
   * @test_Strategy: Perform name service lookup using valid corbaname URL.
   * Construct valid corbaname URL and lookup remote ejb. Create valid corbaname
   * URL. URL="corbaname:iiop:host:port#ejbhome" Perform the remote ejb lookup
   * using no SSL. Verify that correct object reference is returned.
   *
   */
  public void CosNamingNoSSLTest01() throws Fault {
    boolean pass = true;
    String lookupEjbS = "corbaname:iiop:" + namingServiceHost + ":"
        + namingServicePort + "#" + objectEjbS;
    try {
      TestUtil.logMsg("CosNamingNoSSLTest01");
      TestUtil.logMsg("Test lookup of session ejb");
      TestUtil.logMsg("Test valid corbaname URL");
      TestUtil.logMsg("Lookup Name: " + testSbean1);
      TestUtil.logMsg("Lookup Value: " + lookupEjbS);
      beanShome = (TestSessionBeanHome) nctx.lookup(testSbean1,
          TestSessionBeanHome.class);
      TestUtil.logMsg("Successfully looked up session ejbhome");
      try {
        beanSref = (TestSessionBean) beanShome.create(props);
        TestUtil.logMsg("Successfully created session ejbobject");
      } catch (Exception e) {
        TestUtil.logErr("Unsuccessfully created session ejbobject");
        TestUtil.logErr("Caught exception: " + e.getMessage());
        TestUtil.printStackTrace(e);
        TestUtil.logErr(e.toString());
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unsuccessfully looked up session ejbhome");
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      TestUtil.logErr(e.toString());
      pass = false;
    } finally {
      try {
        beanSref.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    if (!pass)
      throw new Fault("CosNamingNoSSLTest01 failed");
  }

  /*
   * @testName: CosNamingNoSSLTest02
   * 
   * @assertion_ids: EJB:SPEC:664; EJB:SPEC:720; EJB:SPEC:721; EJB:SPEC:722;
   * EJB:SPEC:723
   * 
   * @test_Strategy: Perform name service lookup using valid corbaname URL.
   * Construct valid corbaname URL and lookup remote ejb. Create valid corbaname
   * URL. URL="corbaname::host:port#ejbhome" Perform the remote ejb lookup using
   * no SSL. Verify that correct object reference is returned.
   *
   */
  public void CosNamingNoSSLTest02() throws Fault {
    boolean pass = true;
    String lookupEjbS = "corbaname::" + namingServiceHost + ":"
        + namingServicePort + "#" + objectEjbS;
    try {
      TestUtil.logMsg("CosNamingNoSSLTest02");
      TestUtil.logMsg("Test lookup of session ejb");
      TestUtil.logMsg("Test valid corbaname URL");
      TestUtil.logMsg("Lookup Name: " + testSbean2);
      TestUtil.logMsg("Lookup Value: " + lookupEjbS);
      beanShome = (TestSessionBeanHome) nctx.lookup(testSbean2,
          TestSessionBeanHome.class);
      TestUtil.logMsg("Successfully looked up session ejbhome");
      try {
        beanSref = (TestSessionBean) beanShome.create(props);
        TestUtil.logMsg("Successfully created session ejbobject");
      } catch (Exception e) {
        TestUtil.logErr("Unsuccessfully created session ejbobject");
        TestUtil.logErr("Caught exception: " + e.getMessage());
        TestUtil.printStackTrace(e);
        TestUtil.logErr(e.toString());
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unsuccessfully looked up session ejbhome");
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      TestUtil.logErr(e.toString());
      pass = false;
    } finally {
      try {
        beanSref.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    if (!pass)
      throw new Fault("CosNamingNoSSLTest02 failed");
  }

  /*
   * @testName: CosNamingNoSSLTest03
   * 
   * @assertion_ids: EJB:SPEC:664; EJB:SPEC:720; EJB:SPEC:721; EJB:SPEC:722;
   * EJB:SPEC:723
   * 
   * @test_Strategy: Perform name service lookup using valid corbaname URL.
   * Construct valid corbaname URL and lookup remote ejb. Create valid corbaname
   * URL. URL="corbaname:iiop:1.2@host:port#ejbhome" Perform the remote ejb
   * lookup using no SSL. Verify that correct object reference is returned.
   *
   */
  public void CosNamingNoSSLTest03() throws Fault {
    boolean pass = true;
    String lookupEjbS = "corbaname:iiop:1.2@" + namingServiceHost + ":"
        + namingServicePort + "#" + objectEjbS;

    try {
      TestUtil.logMsg("CosNamingNoSSLTest03");
      TestUtil.logMsg("Test lookup of session ejb");
      TestUtil.logMsg("Test valid corbaname URL");
      TestUtil.logMsg("Lookup Name: " + testSbean3);
      TestUtil.logMsg("Lookup Value: " + lookupEjbS);
      beanShome = (TestSessionBeanHome) nctx.lookup(testSbean3,
          TestSessionBeanHome.class);
      TestUtil.logMsg("Successfully looked up session ejbhome");
      try {
        beanSref = (TestSessionBean) beanShome.create(props);
        TestUtil.logMsg("Successfully created session ejbobject");
      } catch (Exception e) {
        TestUtil.logErr("Unsuccessfully created session ejbobject");
        TestUtil.logErr("Caught exception: " + e.getMessage());
        TestUtil.printStackTrace(e);
        TestUtil.logErr(e.toString());
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unsuccessfully looked up session ejbhome");
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      TestUtil.logErr(e.toString());
      pass = false;
    } finally {
      try {
        beanSref.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    if (!pass)
      throw new Fault("CosNamingNoSSLTest03 failed");
  }

  /*
   * @testName: CosNamingNoSSLTest04
   * 
   * @assertion_ids: EJB:SPEC:664; EJB:SPEC:720; EJB:SPEC:721; EJB:SPEC:722;
   * EJB:SPEC:723
   * 
   * @test_Strategy: Perform name service lookup using valid corbaname URL.
   * Construct valid corbaname URL and lookup remote ejb. Create valid corbaname
   * URL. URL="corbaname::1.2@host:port#ejbhome" Perform the remote ejb lookup
   * using no SSL. Verify that correct object reference is returned.
   *
   */
  public void CosNamingNoSSLTest04() throws Fault {
    boolean pass = true;
    String lookupEjbS = "corbaname::1.2@" + namingServiceHost + ":"
        + namingServicePort + "#" + objectEjbS;

    try {
      TestUtil.logMsg("CosNamingNoSSLTest04");
      TestUtil.logMsg("Test lookup of session ejb");
      TestUtil.logMsg("Test valid corbaname URL");
      TestUtil.logMsg("Lookup Name: " + testSbean4);
      TestUtil.logMsg("Lookup Value: " + lookupEjbS);
      beanShome = (TestSessionBeanHome) nctx.lookup(testSbean4,
          TestSessionBeanHome.class);
      TestUtil.logMsg("Successfully looked up session ejbhome");
      try {
        beanSref = (TestSessionBean) beanShome.create(props);
        TestUtil.logMsg("Successfully created session ejbobject");
      } catch (Exception e) {
        TestUtil.logErr("Unsuccessfully created session ejbobject");
        TestUtil.logErr("Caught exception: " + e.getMessage());
        TestUtil.printStackTrace(e);
        TestUtil.logErr(e.toString());
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unsuccessfully looked up session ejbhome");
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      TestUtil.logErr(e.toString());
      pass = false;
    } finally {
      try {
        beanSref.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    if (!pass)
      throw new Fault("CosNamingNoSSLTest04 failed");
  }

  /*
   * @testName: CosNamingNoSSLTest05
   * 
   * @assertion_ids: EJB:SPEC:664; EJB:SPEC:720; EJB:SPEC:721; EJB:SPEC:722;
   * EJB:SPEC:723
   * 
   * @test_Strategy: Construct bad corbaname URL and lookup remote ejb. Create
   * bad corbaname URL (Bad URL). URL="corbaname::,:1.2@bad//host:port#ejbhome"
   * Perform the remote ejb lookup using no SSL. Verify that an exception is
   * thrown.
   *
   */
  public void CosNamingNoSSLTest05() throws Fault {
    boolean pass = true;
    String lookupEjbS = "corbaname::,:1.2@bad//" + namingServiceHost + ":"
        + namingServicePort + "#" + objectEjbS;

    try {
      TestUtil.logMsg("CosNamingNoSSLTest05");
      TestUtil.logMsg("Test lookup of session ejb");
      TestUtil.logMsg("Test bad corbaname URL (Bad URL)");
      TestUtil.logMsg("Lookup Name: " + testSbean5);
      TestUtil.logMsg("Lookup Value: " + lookupEjbS);
      beanShome = (TestSessionBeanHome) nctx.lookup(testSbean5,
          TestSessionBeanHome.class);
      TestUtil.logErr("Did not get expected NamingException");
      pass = false;
    } catch (NamingException e) {
      TestUtil.logMsg("Did get expected NamingException");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      TestUtil.logErr(e.toString());
      pass = false;
    }
    if (!pass)
      throw new Fault("CosNamingNoSSLTest05 failed");
  }

  /*
   * @testName: CosNamingNoSSLTest06
   * 
   * @assertion_ids: EJB:SPEC:664; EJB:SPEC:720; EJB:SPEC:721; EJB:SPEC:722;
   * EJB:SPEC:723
   * 
   * @test_Strategy: Perform name service lookup using bad corbaname URL.
   * Construct bad corbaname URL and lookup remote ejb. Create bad corbaname URL
   * (Bad EJBHome). URL="corbaname:iiop:host:port#badejbhome" Perform the remote
   * ejb lookup using no SSL. Verify that an exception is thrown.
   *
   */
  public void CosNamingNoSSLTest06() throws Fault {
    boolean pass = true;
    String lookupEjbS = "corbaname:iiop:" + namingServiceHost + ":"
        + namingServicePort + "#badejb";

    try {
      TestUtil.logMsg("CosNamingNoSSLTest06");
      TestUtil.logMsg("Test lookup of session ejb");
      TestUtil.logMsg("Test bad corbaname URL (Bad EJBHome)");
      TestUtil.logMsg("Lookup Name: " + testSbean6);
      TestUtil.logMsg("Lookup Value: " + lookupEjbS);
      beanShome = (TestSessionBeanHome) nctx.lookup(testSbean6,
          TestSessionBeanHome.class);
      TestUtil.logErr("Did not get expected NamingException");
      pass = false;
    } catch (NamingException e) {
      TestUtil.logMsg("Did get expected NamingException");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      TestUtil.logErr(e.toString());
      pass = false;
    }
    if (!pass)
      throw new Fault("CosNamingNoSSLTest06 failed");
  }

  /*
   * @testName: CosNamingNoSSLTest07
   * 
   * @assertion: Perform name service lookup using bad corbaname URL.
   * 
   * @test_Strategy: Construct bad corbaname URL and lookup remote ejb. Create
   * bad corbaname URL (Bad Host). URL="corbaname:iiop:badhost:port#ejbhome"
   * Perform the remote ejb lookup using no SSL. Verify that an exception is
   * thrown.
   *
   */
  public void CosNamingNoSSLTest07() throws Fault {
    boolean pass = true;
    String lookupEjbS = "corbaname:iiop:badhost" + ":" + namingServicePort + "#"
        + objectEjbS;

    try {
      TestUtil.logMsg("CosNamingNoSSLTest07");
      TestUtil.logMsg("Test lookup of session ejb");
      TestUtil.logMsg("Test bad corbaname URL (Bad Host)");
      TestUtil.logMsg("Lookup Name: " + testSbean7);
      TestUtil.logMsg("Lookup Value: " + lookupEjbS);
      beanShome = (TestSessionBeanHome) nctx.lookup(testSbean7,
          TestSessionBeanHome.class);
      TestUtil.logErr("Did not get expected NamingException");
      pass = false;
    } catch (NamingException e) {
      TestUtil.logMsg("Did get expected NamingException");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      TestUtil.logErr(e.toString());
      pass = false;
    }
    if (!pass)
      throw new Fault("CosNamingNoSSLTest07 failed");
  }

  /*
   * @testName: CosNamingNoSSLTest08
   * 
   * @assertion: Perform name service lookup using bad corbaname URL.
   * 
   * @test_Strategy: Construct bad corbaname URL and lookup remote ejb. Create
   * bad corbaname URL (Bad Port). URL="corbaname:iiop:host:badport#ejbhome"
   * Perform the remote ejb lookup using no SSL. Verify that an exception is
   * thrown.
   *
   */
  public void CosNamingNoSSLTest08() throws Fault {
    boolean pass = true;
    String lookupEjbS = "corbaname:iiop:" + namingServiceHost + ":badport" + "#"
        + objectEjbS;

    try {
      TestUtil.logMsg("CosNamingNoSSLTest08");
      TestUtil.logMsg("Test lookup of session ejb");
      TestUtil.logMsg("Test bad corbaname URL (Bad Port)");
      TestUtil.logMsg("Lookup Name: " + testSbean8);
      TestUtil.logMsg("Lookup Value: " + lookupEjbS);
      beanShome = (TestSessionBeanHome) nctx.lookup(testSbean8,
          TestSessionBeanHome.class);
      TestUtil.logErr("Did not get expected NamingException");
      pass = false;
    } catch (NamingException e) {
      TestUtil.logMsg("Did get expected NamingException");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      TestUtil.logErr(e.toString());
      pass = false;
    }
    if (!pass)
      throw new Fault("CosNamingNoSSLTest08 failed");
  }

  /*
   * @testName: CosNamingNoSSLTest09
   * 
   * @assertion: Perform name service lookup using bad corbaname URL.
   * 
   * @test_Strategy: Construct bad corbaname URL and lookup remote ejb. Create
   * bad corbaname URL (Bad Key). URL="corbaname:iiop:host:port/badkey#ejbhome"
   * Perform the remote ejb lookup using no SSL. Verify that an exception is
   * thrown.
   *
   */
  public void CosNamingNoSSLTest09() throws Fault {
    boolean pass = true;
    String lookupEjbS = "corbaname:iiop:" + namingServiceHost + ":"
        + namingServicePort + "/badkey" + "#" + objectEjbS;

    try {
      TestUtil.logMsg("CosNamingNoSSLTest09");
      TestUtil.logMsg("Test lookup of session ejb");
      TestUtil.logMsg("Test bad corbaname URL (Bad Key)");
      TestUtil.logMsg("Lookup Name: " + testSbean9);
      TestUtil.logMsg("Lookup Value: " + lookupEjbS);
      beanShome = (TestSessionBeanHome) nctx.lookup(testSbean9,
          TestSessionBeanHome.class);
      TestUtil.logErr("Did not get expected NamingException");
      pass = false;
    } catch (NamingException e) {
      TestUtil.logMsg("Did get expected NamingException");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      TestUtil.logErr(e.toString());
      pass = false;
    }
    if (!pass)
      throw new Fault("CosNamingNoSSLTest09 failed");
  }

  /*
   * @testName: CosNamingNoSSLTest10
   * 
   * @assertion: Perform name service lookup using bad corbaname URL.
   * 
   * @test_Strategy: Construct bad corbaname URL and lookup remote ejb. Create
   * bad corbaname URL (Bad Protocol).
   * URL="corbaname:badprotocol:host:port#ejbhome" Perform the remote ejb lookup
   * using no SSL.
   *
   */
  public void CosNamingNoSSLTest10() throws Fault {
    boolean pass = true;
    String lookupEjbS = "corbaname:badprotocol:" + namingServiceHost + ":"
        + namingServicePort + "#" + objectEjbS;

    try {
      TestUtil.logMsg("CosNamingNoSSLTest10");
      TestUtil.logMsg("Test lookup of session ejb");
      TestUtil.logMsg("Test bad corbaname URL (Bad Protocol)");
      TestUtil.logMsg("Lookup Name: " + testSbean10);
      TestUtil.logMsg("Lookup Value: " + lookupEjbS);
      beanShome = (TestSessionBeanHome) nctx.lookup(testSbean10,
          TestSessionBeanHome.class);
      TestUtil.logErr("Did not get expected NamingException");
      pass = false;
    } catch (NamingException e) {
      TestUtil.logMsg("Did get expected NamingException");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      TestUtil.logErr(e.toString());
      pass = false;
    }
    if (!pass)
      throw new Fault("CosNamingNoSSLTest10 failed");
  }

  /*
   * @testName: CosNamingNoSSLTest11
   * 
   * @assertion_ids: EJB:SPEC:664; EJB:SPEC:720; EJB:SPEC:721; EJB:SPEC:722;
   * EJB:SPEC:723
   * 
   * @test_Strategy: Perform name service lookup using valid corbaname URL.
   * Construct valid corbaname URL and lookup remote ejb. Create valid corbaname
   * URL. URL="corbaname:iiop:host:port#ejbhome" Perform the remote ejb lookup
   * using no SSL. Verify that correct object reference is returned.
   *
   */
  public void CosNamingNoSSLTest11() throws Fault {
    boolean pass = true;
    String lookupEjbE = "corbaname:iiop:" + namingServiceHost + ":"
        + namingServicePort + "#" + objectEjbE;
    try {
      TestUtil.logMsg("CosNamingNoSSLTest11");
      TestUtil.logMsg("Test lookup of entity ejb");
      TestUtil.logMsg("Test valid corbaname URL");
      TestUtil.logMsg("Lookup Name: " + testEbean1);
      TestUtil.logMsg("Lookup Value: " + lookupEjbE);
      beanEhome = (TestEntityBeanHome) nctx.lookup(testEbean1,
          TestEntityBeanHome.class);
      TestUtil.logMsg("Successfully looked up entity ejbhome");
      try {
        beanEref = (TestEntityBean) beanEhome.create(props, 1, "coffee-1", 1);
        TestUtil.logMsg("Successfully created entity ejbobject");
      } catch (Exception e) {
        TestUtil.logErr("Unsuccessfully created entity ejbobject");
        TestUtil.logErr("Caught exception: " + e.getMessage());
        TestUtil.printStackTrace(e);
        TestUtil.logErr(e.toString());
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unsuccessfully looked up entity ejbhome");
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      TestUtil.logErr(e.toString());
      pass = false;
    } finally {
      try {
        beanEref.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    if (!pass)
      throw new Fault("CosNamingNoSSLTest11 failed");
  }

  /*
   * @testName: CosNamingNoSSLTest12
   * 
   * @assertion_ids: EJB:SPEC:664; EJB:SPEC:720; EJB:SPEC:721; EJB:SPEC:722;
   * EJB:SPEC:723
   * 
   * @test_Strategy: Perform name service lookup using valid corbaname URL.
   * Construct valid corbaname URL and lookup remote ejb. Create valid corbaname
   * URL. URL="corbaname::host:port#ejbhome" Perform the remote ejb lookup using
   * no SSL. Verify that correct object reference is returned.
   *
   */
  public void CosNamingNoSSLTest12() throws Fault {
    boolean pass = true;
    String lookupEjbE = "corbaname::" + namingServiceHost + ":"
        + namingServicePort + "#" + objectEjbE;
    try {
      TestUtil.logMsg("CosNamingNoSSLTest12");
      TestUtil.logMsg("Test lookup of entity ejb");
      TestUtil.logMsg("Test valid corbaname URL");
      TestUtil.logMsg("Lookup Name: " + testEbean2);
      TestUtil.logMsg("Lookup Value: " + lookupEjbE);
      beanEhome = (TestEntityBeanHome) nctx.lookup(testEbean2,
          TestEntityBeanHome.class);
      TestUtil.logMsg("Successfully looked up entity ejbhome");
      try {
        beanEref = (TestEntityBean) beanEhome.create(props, 1, "coffee-1", 1);
        TestUtil.logMsg("Successfully created entity ejbobject");
      } catch (Exception e) {
        TestUtil.logErr("Unsuccessfully created entity ejbobject");
        TestUtil.logErr("Caught exception: " + e.getMessage());
        TestUtil.printStackTrace(e);
        TestUtil.logErr(e.toString());
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unsuccessfully looked up entity ejbhome");
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      TestUtil.logErr(e.toString());
      pass = false;
    } finally {
      try {
        beanEref.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    if (!pass)
      throw new Fault("CosNamingNoSSLTest12 failed");
  }

  /*
   * @testName: CosNamingNoSSLTest13
   * 
   * @assertion_ids: EJB:SPEC:664; EJB:SPEC:720; EJB:SPEC:721; EJB:SPEC:722;
   * EJB:SPEC:723
   * 
   * @test_Strategy: Perform name service lookup using valid corbaname URL.
   * Construct valid corbaname URL and lookup remote ejb. Create valid corbaname
   * URL. URL="corbaname:iiop:1.2@host:port#ejbhome" Perform the remote ejb
   * lookup using no SSL. Verify that correct object reference is returned.
   *
   */
  public void CosNamingNoSSLTest13() throws Fault {
    boolean pass = true;
    String lookupEjbE = "corbaname:iiop:1.2@" + namingServiceHost + ":"
        + namingServicePort + "#" + objectEjbE;

    try {
      TestUtil.logMsg("CosNamingNoSSLTest13");
      TestUtil.logMsg("Test lookup of entity ejb");
      TestUtil.logMsg("Test valid corbaname URL");
      TestUtil.logMsg("Lookup Name: " + testEbean3);
      TestUtil.logMsg("Lookup Value: " + lookupEjbE);
      beanEhome = (TestEntityBeanHome) nctx.lookup(testEbean3,
          TestEntityBeanHome.class);
      TestUtil.logMsg("Successfully looked up entity ejbhome");
      try {
        beanEref = (TestEntityBean) beanEhome.create(props, 1, "coffee-1", 1);
        TestUtil.logMsg("Successfully created entity ejbobject");
      } catch (Exception e) {
        TestUtil.logErr("Unsuccessfully created entity ejbobject");
        TestUtil.logErr("Caught exception: " + e.getMessage());
        TestUtil.printStackTrace(e);
        TestUtil.logErr(e.toString());
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unsuccessfully looked up entity ejbhome");
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      TestUtil.logErr(e.toString());
      pass = false;
    } finally {
      try {
        beanEref.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    if (!pass)
      throw new Fault("CosNamingNoSSLTest13 failed");
  }

  /*
   * @testName: CosNamingNoSSLTest14
   * 
   * @assertion_ids: EJB:SPEC:664; EJB:SPEC:720; EJB:SPEC:721; EJB:SPEC:722;
   * EJB:SPEC:723
   * 
   * @test_Strategy: Perform name service lookup using valid corbaname URL.
   * Construct valid corbaname URL and lookup remote ejb. Create valid corbaname
   * URL. URL="corbaname::1.2@host:port#ejbhome" Perform the remote ejb lookup
   * using no SSL. Verify that correct object reference is returned.
   *
   */
  public void CosNamingNoSSLTest14() throws Fault {
    boolean pass = true;
    String lookupEjbE = "corbaname::1.2@" + namingServiceHost + ":"
        + namingServicePort + "#" + objectEjbE;

    try {
      TestUtil.logMsg("CosNamingNoSSLTest14");
      TestUtil.logMsg("Test lookup of entity ejb");
      TestUtil.logMsg("Test valid corbaname URL");
      TestUtil.logMsg("Lookup Name: " + testEbean4);
      TestUtil.logMsg("Lookup Value: " + lookupEjbE);
      beanEhome = (TestEntityBeanHome) nctx.lookup(testEbean4,
          TestEntityBeanHome.class);
      TestUtil.logMsg("Successfully looked up entity ejbhome");
      try {
        beanEref = (TestEntityBean) beanEhome.create(props, 1, "coffee-1", 1);
        TestUtil.logMsg("Successfully created entity ejbobject");
      } catch (Exception e) {
        TestUtil.logErr("Unsuccessfully created entity ejbobject");
        TestUtil.logErr("Caught exception: " + e.getMessage());
        TestUtil.printStackTrace(e);
        TestUtil.logErr(e.toString());
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unsuccessfully looked up entity ejbhome");
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      TestUtil.logErr(e.toString());
      pass = false;
    } finally {
      try {
        beanEref.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    if (!pass)
      throw new Fault("CosNamingNoSSLTest14 failed");
  }

  /*
   * @testName: CosNamingNoSSLTest15
   * 
   * @assertion_ids: EJB:SPEC:664; EJB:SPEC:720; EJB:SPEC:721; EJB:SPEC:722;
   * EJB:SPEC:723
   * 
   * @test_Strategy: Perform name service lookup using bad corbaname URL.
   * Construct bad corbaname URL and lookup remote ejb. Create bad corbaname URL
   * (Bad URL). URL="corbaname::,:1.2@bad//host:port#ejbhome" Perform the remote
   * ejb lookup using no SSL. Verify that an exception is thrown.
   *
   */
  public void CosNamingNoSSLTest15() throws Fault {
    boolean pass = true;
    String lookupEjbE = "corbaname::,:1.2@bad//" + namingServiceHost + ":"
        + namingServicePort + "#" + objectEjbE;

    try {
      TestUtil.logMsg("CosNamingNoSSLTest15");
      TestUtil.logMsg("Test lookup of entity ejb");
      TestUtil.logMsg("Test bad corbaname URL (Bad URL)");
      TestUtil.logMsg("Lookup Name: " + testEbean5);
      TestUtil.logMsg("Lookup Value: " + lookupEjbE);
      beanEhome = (TestEntityBeanHome) nctx.lookup(testEbean5,
          TestEntityBeanHome.class);
      TestUtil.logErr("Did not get expected NamingException");
      pass = false;
    } catch (NamingException e) {
      TestUtil.logMsg("Did get expected NamingException");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      TestUtil.logErr(e.toString());
      pass = false;
    }
    if (!pass)
      throw new Fault("CosNamingNoSSLTest15 failed");
  }

  /*
   * @testName: CosNamingNoSSLTest16
   * 
   * @assertion_ids: EJB:SPEC:664; EJB:SPEC:720; EJB:SPEC:721; EJB:SPEC:722;
   * EJB:SPEC:723
   * 
   * @test_Strategy: Perform name service lookup using bad corbaname URL.
   * Construct bad corbaname URL and lookup remote ejb. Create bad corbaname URL
   * (Bad EJBHome). URL="corbaname:iiop:host:port#badejbhome" Perform the remote
   * ejb lookup using no SSL. Verify that an exception is thrown.
   *
   */
  public void CosNamingNoSSLTest16() throws Fault {
    boolean pass = true;
    String lookupEjbE = "corbaname:iiop:" + namingServiceHost + ":"
        + namingServicePort + "#badejb";

    try {
      TestUtil.logMsg("CosNamingNoSSLTest16");
      TestUtil.logMsg("Test lookup of entity ejb");
      TestUtil.logMsg("Test bad corbaname URL (Bad EJBHome)");
      TestUtil.logMsg("Lookup Name: " + testEbean6);
      TestUtil.logMsg("Lookup Value: " + lookupEjbE);
      beanEhome = (TestEntityBeanHome) nctx.lookup(testEbean6,
          TestEntityBeanHome.class);
      TestUtil.logErr("Did not get expected NamingException");
      pass = false;
    } catch (NamingException e) {
      TestUtil.logMsg("Did get expected NamingException");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      TestUtil.logErr(e.toString());
      pass = false;
    }
    if (!pass)
      throw new Fault("CosNamingNoSSLTest16 failed");
  }

  /*
   * @testName: CosNamingNoSSLTest17
   * 
   * @assertion_ids: EJB:SPEC:664; EJB:SPEC:720; EJB:SPEC:721; EJB:SPEC:722;
   * EJB:SPEC:723
   * 
   * @test_Strategy: Perform name service lookup using bad corbaname URL.
   * Construct bad corbaname URL and lookup remote ejb. Create bad corbaname URL
   * (Bad Host). URL="corbaname:iiop:badhost:port#ejbhome" Perform the remote
   * ejb lookup using no SSL. Verify that an exception is thrown.
   *
   */
  public void CosNamingNoSSLTest17() throws Fault {
    boolean pass = true;
    String lookupEjbE = "corbaname:iiop:badhost" + ":" + namingServicePort + "#"
        + objectEjbE;

    try {
      TestUtil.logMsg("CosNamingNoSSLTest17");
      TestUtil.logMsg("Test lookup of entity ejb");
      TestUtil.logMsg("Test bad corbaname URL (Bad Host)");
      TestUtil.logMsg("Lookup Name: " + testEbean7);
      TestUtil.logMsg("Lookup Value: " + lookupEjbE);
      beanEhome = (TestEntityBeanHome) nctx.lookup(testEbean7,
          TestEntityBeanHome.class);
      TestUtil.logErr("Did not get expected NamingException");
      pass = false;
    } catch (NamingException e) {
      TestUtil.logMsg("Did get expected NamingException");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      TestUtil.logErr(e.toString());
      pass = false;
    }
    if (!pass)
      throw new Fault("CosNamingNoSSLTest17 failed");
  }

  /*
   * @testName: CosNamingNoSSLTest18
   * 
   * @assertion_ids: EJB:SPEC:664; EJB:SPEC:720; EJB:SPEC:721; EJB:SPEC:722;
   * EJB:SPEC:723
   * 
   * @test_Strategy: Perform name service lookup using bad corbaname URL.
   * Construct bad corbaname URL and lookup remote ejb. Create bad corbaname URL
   * (Bad Port). URL="corbaname:iiop:host:badport#ejbhome" Perform the remote
   * ejb lookup using no SSL. Verify that an exception is thrown.
   *
   */
  public void CosNamingNoSSLTest18() throws Fault {
    boolean pass = true;
    String lookupEjbE = "corbaname:iiop:" + namingServiceHost + ":badport" + "#"
        + objectEjbE;

    try {
      TestUtil.logMsg("CosNamingNoSSLTest18");
      TestUtil.logMsg("Test lookup of entity ejb");
      TestUtil.logMsg("Test bad corbaname URL (Bad Port)");
      TestUtil.logMsg("Lookup Name: " + testEbean8);
      TestUtil.logMsg("Lookup Value: " + lookupEjbE);
      beanEhome = (TestEntityBeanHome) nctx.lookup(testEbean8,
          TestEntityBeanHome.class);
      TestUtil.logErr("Did not get expected NamingException");
      pass = false;
    } catch (NamingException e) {
      TestUtil.logMsg("Did get expected NamingException");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      TestUtil.logErr(e.toString());
      pass = false;
    }
    if (!pass)
      throw new Fault("CosNamingNoSSLTest18 failed");
  }

  /*
   * @testName: CosNamingNoSSLTest19
   * 
   * @assertion_ids: EJB:SPEC:664; EJB:SPEC:720; EJB:SPEC:721; EJB:SPEC:722;
   * EJB:SPEC:723
   * 
   * @test_Strategy: Perform name service lookup using bad corbaname URL.
   * Construct bad corbaname URL and lookup remote ejb. Create bad corbaname URL
   * (Bad Key). URL="corbaname:iiop:host:port/badkey#ejbhome" Perform the remote
   * ejb lookup using no SSL. Verify that an exception is thrown.
   *
   */
  public void CosNamingNoSSLTest19() throws Fault {
    boolean pass = true;
    String lookupEjbE = "corbaname:iiop:" + namingServiceHost + ":"
        + namingServicePort + "/badkey" + "#" + objectEjbE;

    try {
      TestUtil.logMsg("CosNamingNoSSLTest19");
      TestUtil.logMsg("Test lookup of entity ejb");
      TestUtil.logMsg("Test bad corbaname URL (Bad Key)");
      TestUtil.logMsg("Lookup Name: " + testEbean9);
      TestUtil.logMsg("Lookup Value: " + lookupEjbE);
      beanEhome = (TestEntityBeanHome) nctx.lookup(testEbean9,
          TestEntityBeanHome.class);
      TestUtil.logErr("Did not get expected NamingException");
      pass = false;
    } catch (NamingException e) {
      TestUtil.logMsg("Did get expected NamingException");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      TestUtil.logErr(e.toString());
      pass = false;
    }
    if (!pass)
      throw new Fault("CosNamingNoSSLTest19 failed");
  }

  /*
   * @testName: CosNamingNoSSLTest20
   * 
   * @assertion_ids: EJB:SPEC:664; EJB:SPEC:720; EJB:SPEC:721; EJB:SPEC:722;
   * EJB:SPEC:723
   * 
   * @test_Strategy: Perform name service lookup using bad corbaname URL.
   * Construct bad corbaname URL and lookup remote ejb. Create bad corbaname URL
   * (Bad Protocol). URL="corbaname:badprotocol:host:port#ejbhome" Perform the
   * remote ejb lookup using no SSL.
   *
   */
  public void CosNamingNoSSLTest20() throws Fault {
    boolean pass = true;
    String lookupEjbE = "corbaname:badprotocol:" + namingServiceHost + ":"
        + namingServicePort + "#" + objectEjbE;

    try {
      TestUtil.logMsg("CosNamingNoSSLTest20");
      TestUtil.logMsg("Test lookup of entity ejb");
      TestUtil.logMsg("Test bad corbaname URL (Bad Protocol)");
      TestUtil.logMsg("Lookup Name: " + testEbean10);
      TestUtil.logMsg("Lookup Value: " + lookupEjbE);
      beanEhome = (TestEntityBeanHome) nctx.lookup(testEbean10,
          TestEntityBeanHome.class);
      TestUtil.logErr("Did not get expected NamingException");
      pass = false;
    } catch (NamingException e) {
      TestUtil.logMsg("Did get expected NamingException");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      TestUtil.logErr(e.toString());
      pass = false;
    }
    if (!pass)
      throw new Fault("CosNamingNoSSLTest20 failed");
  }
}
