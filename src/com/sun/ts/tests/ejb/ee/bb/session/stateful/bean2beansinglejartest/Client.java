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

package com.sun.ts.tests.ejb.ee.bb.session.stateful.bean2beansinglejartest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.util.*;
import javax.ejb.*;
import javax.transaction.*;
import java.rmi.*;
import com.sun.ts.tests.ejb.ee.bb.session.stateful.bean2beansinglejartest.bean1.*;

import com.sun.javatest.Status;

public class Client extends EETest {
  private static final String testName = "Bean2BeanSingleJarTest";

  private static final String testBean1 = "java:comp/env/ejb/TestBean1";

  private static final String testProps = "bean2beansinglejartest.properties";

  private static final String testDir = System.getProperty("user.dir");

  private TestBean1 beanRef = null;

  private TestBean1Home beanHome = null;

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
      logMsg("Looking up home interface for EJB: " + testBean1);
      beanHome = (TestBean1Home) nctx.lookup(testBean1, TestBean1Home.class);
      logMsg("Setup ok");
    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  /* Run test */

  /*
   * @testName: test1
   * 
   * @assertion_ids: EJB:SPEC:14
   * 
   * @test_Strategy: The test demonstrates bean to bean calls in same
   * application EAR. Satisfy intra-ear calls of application components. Create
   * 2 stateful Session Beans. Deploy them on the J2EE server. Client call
   * bean1, bean1 call bean2.
   *
   */

  public void test1() throws Fault {
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean1) beanHome.create(props);
      boolean pass = beanRef.callOtherBeanTest();
      if (!pass)
        throw new Fault("test1 failed");
      beanRef.remove();
    } catch (Exception e) {
      throw new Fault("test1 failed", e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }
}
