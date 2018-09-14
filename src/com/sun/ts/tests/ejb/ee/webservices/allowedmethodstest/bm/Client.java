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

package com.sun.ts.tests.ejb.ee.webservices.allowedmethodstest.bm;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.util.*;
import javax.ejb.*;
import javax.transaction.*;
import java.rmi.*;
import javax.xml.rpc.*;
import com.sun.javatest.Status;

public class Client extends EETest {
  private static final String testName = "AllowedmethodsTest";

  private static final String testLookup = "java:comp/env/ejb/TestBean";

  private static final String serviceLookup = "java:comp/env/service/TestService";

  private TestBean beanRef = null;

  private Service svc = null;

  private Test port = null;

  private TestBeanHome beanHome = null;

  private Properties props = null;

  private TSNamingContext nctx = null;

  private String user_value;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   * user; authuser; authpassword;
   * 
   * @class.testArgs: -ap tssql.stmt
   */

  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    user_value = props.getProperty("user");
    String authUsername = props.getProperty("authuser");
    String authPassword = props.getProperty("authpassword");
    TestUtil.logMsg("user_value= " + user_value);
    TestUtil.logMsg("auth_username= " + authUsername);
    TestUtil.logMsg("auth_password= " + authPassword);

    try {
      TestUtil.logMsg("Obtain naming context");
      nctx = new TSNamingContext();

      TSLoginContext lc = new TSLoginContext();

      // login as authuser(javajoe)
      lc.login(authUsername, authPassword);

      // Get EJB Home ...
      TestUtil.logMsg("Looking up home interface for EJB: " + testLookup);
      beanHome = (TestBeanHome) nctx.lookup(testLookup, TestBeanHome.class);

      TestUtil.logMsg("Looking up service: " + serviceLookup);
      svc = (javax.xml.rpc.Service) nctx.lookup(serviceLookup);
      TestUtil.logMsg("Obtained service");

      port = (Test) svc.getPort(Test.class);
      TestUtil.logMsg("Obtained port");

      TestUtil.logMsg("Setup ok");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("Setup failed:", e);
    }
  }

  /* Run test */

  /*
   * @testName: wsbmAllowedMethodsTest1
   * 
   * @assertion_ids: EJB:SPEC:121; EJB:SPEC:121.1; EJB:SPEC:121.2;
   * EJB:SPEC:121.3; EJB:SPEC:121.4; EJB:SPEC:121.5; EJB:SPEC:121.6;
   * EJB:SPEC:121.7; EJB:SPEC:121.10; EJB:SPEC:121.11; EJB:SPEC:121.12;
   * EJB:SPEC:121.13; EJB:SPEC:121.14; EJB:SPEC:121.15; EJB:SPEC:121.16;
   * EJB:JAVADOC:172; EJB:SPEC:10; EJB:SPEC:7; EJB:SPEC:67
   * 
   * @test_Strategy: Operations allowed and not allowed in the business method
   * method from a component interface of a stateless session bean with bean-
   * managed transaction demarcation are: o getEJBHome - allowed o
   * getCallerPrincipal - allowed o isCallerInRole - allowed o getEJBObject -
   * allowed o JNDI_Access - allowed o getUserTransaction - allowed o
   * UserTransaction_Methods_Test1 - allowed o UserTransaction_Methods_Test2 -
   * allowed o UserTransaction_Methods_Test3 - allowed o
   * UserTransaction_Methods_Test4 - allowed o getEJBLocalHome - allowed o
   * getEJBLocalObject - allowed o getTimerService - allowed o
   * Timer_Service_Methods - allowed o getMessageContext - allowed o
   * getRollbackOnly - not allowed o setRollbackOnly - not allowed
   *
   * Create a stateless Session Bean. Deploy it on the J2EE server. Verify
   * correct operations.
   *
   */

  public void wsbmAllowedMethodsTest1() throws Fault {
    TestUtil.logTrace("Operation Tests for businessMethod");
    boolean pass = true;
    boolean response = false;

    try {
      // create EJB instance
      TestUtil.logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create();
      TestUtil.logMsg("Initialize remote logging");
      beanRef.initLogging(props);
      TestUtil.logMsg(
          "Calling EJB business method from Web Service Endpoint Interface");
      response = port.businessMethod();
      if (!response) {
        TestUtil.logErr("INCORRECT RESULTS RECEIVED");
        pass = false;
      }
    } catch (Exception e) {
      throw new Fault("wsbmAllowedMethodsTest1 failed", e);
    }

    try {
      beanRef.remove();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception caught removing bean:" + e, e);
    }

    if (!pass)
      throw new Fault("wsbmAllowedMethodsTest1 failed");
  }

  /*
   * @testName: wsbmAllowedMethodsTest2
   * 
   * @assertion_ids: EJB:SPEC:121.2; EJB:SPEC:823
   * 
   * @test_Strategy: Verify Principal reference is returned for SSL Bean
   * Webservice Endpoint with security-identity as use-caller-identity - never
   * null.
   *
   */

  public void wsbmAllowedMethodsTest2() throws Fault {
    boolean pass = true;
    boolean response1 = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create();
      beanRef.initLogging(props);

      logMsg("Calling method with user: " + user_value);
      response1 = port.getCallerPrincipalTest(user_value);
      if (!response1) {
        TestUtil.logErr("INCORRECT RESULTS RECEIVED");
        pass = false;
      }
    } catch (Exception e) {
      throw new Fault("wsbmAllowedMethodsTest2 failed", e);
    }
    if (!pass)
      throw new Fault("wsbmAllowedMethodsTest2 failed");
  }

  public void cleanup() throws Fault {
    TestUtil.logMsg("cleanup ok");
  }
}
