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
 * @(#)Client.java	1.2 03/05/16
 */

package com.sun.ts.tests.ejb.ee.webservices.allowedmethodstest.cm;

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

  private Hashtable results = null;

  private boolean SKIP = false;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   * 
   * @class.testArgs: -ap tssql.stmt
   */

  public void setup(String[] args, Properties p) throws Fault {
    props = p;

    try {
      TestUtil.logMsg("Obtain naming context");
      nctx = new TSNamingContext();

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
   * @testName: wscmAllowedMethodsTest1
   * 
   * @assertion_ids: EJB:SPEC:120; EJB:SPEC:120.1; EJB:SPEC:120.2;
   * EJB:SPEC:120.3; EJB:SPEC:120.4; EJB:SPEC:120.5; EJB:SPEC:120.6;
   * EJB:SPEC:120.7; EJB:SPEC:120.10; EJB:SPEC:120.11; EJB:SPEC:120.12;
   * EJB:SPEC:120.13; EJB:SPEC:120.14; EJB:SPEC:120.15; EJB:SPEC:67;
   * EJB:JAVADOC:172
   * 
   * @test_Strategy: Operations allowed and not allowed in the business method
   * method from a web service endpoint interface of a stateless session bean
   * with container- managed transaction demarcation are: o getEJBHome - allowed
   * o getCallerPrincipal - allowed o isCallerInRole - allowed o getEJBObject -
   * allowed o JNDI_Access - allowed o getUserTransaction - not allowed o
   * UserTransaction_Methods_Test1 - not allowed o UserTransaction_Methods_Test2
   * - not allowed o UserTransaction_Methods_Test3 - not allowed o
   * UserTransaction_Methods_Test4 - not allowed o UserTransaction_Methods_Test5
   * - not allowed o UserTransaction_Methods_Test6 - not allowed o
   * getEJBLocalHome - allowed o getEJBLocalObject - allowed o getTimerService -
   * allowed o Timer_Service_Methods - allowed o getMessageContext - allowed o
   * getRollbackOnly - allowed o setRollbackOnly - allowed
   *
   * Create a stateless Session Bean. Deploy it on the J2EE server. Verify
   * correct operations. Ensure getCallerPrincipal does not return null.
   */

  public void wscmAllowedMethodsTest1() throws Fault {
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
      throw new Fault("wscmAllowedMethodsTest1 failed", e);
    }

    try {
      if (beanRef != null) {
        beanRef.remove();
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      TestUtil.logErr("Unexpected Exception caught removing bean:" + e, e);
    }

    if (!pass)
      throw new Fault("wscmAllowedMethodsTest1 failed");
  }

  public void cleanup() throws Fault {
    TestUtil.logMsg("cleanup ok");
  }
}
