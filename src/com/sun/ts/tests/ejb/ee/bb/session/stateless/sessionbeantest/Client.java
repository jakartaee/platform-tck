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

package com.sun.ts.tests.ejb.ee.bb.session.stateless.sessionbeantest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.util.*;
import javax.ejb.*;
import javax.transaction.*;
import java.rmi.*;

import com.sun.javatest.Status;

//*****************************************************
//SessionBean Lifecyle Test for STATELESS session beans
//
//Refer to Figure 11 (Lifecycle of a STATELESS session
//bean instance)
//*****************************************************

public class Client extends EETest {
  private static final String testName = "SessionBeanTest";

  private static final String tBean = "java:comp/env/ejb/TestBean";

  private static final String cBean = "java:comp/env/ejb/CallBack";

  private Properties props = null;

  private TestBean beanRef = null;

  private TestBeanHome beanHome = null;

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
      logMsg("Looking up home interface for EJB: " + tBean);
      beanHome = (TestBeanHome) nctx.lookup(tBean, TestBeanHome.class);
      logMsg("Setup ok");
    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  /* Run test */

  /*
   * @testName: test1
   * 
   * @assertion_ids: EJB:SPEC:102
   * 
   * @test_Strategy: Create a stateless Session Bean. Deploy it on the J2EE
   * server. Check creation life cycle call flow occurs.
   *
   */

  public void test1() throws Fault {
    boolean pass = true;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create();
      logMsg("initialize remote logging");
      beanRef.initLogging(props);
      logMsg("check if proper lifecycle creation order was called in the bean");
      pass = beanRef.isCreateLifeCycle();
      beanRef.remove();
    } catch (Exception e) {
      throw new Fault("test1 failed", e);
    }
    if (!pass)
      throw new Fault("test1 failed");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }
}
