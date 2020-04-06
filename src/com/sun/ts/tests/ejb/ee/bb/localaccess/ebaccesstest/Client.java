/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb.ee.bb.localaccess.ebaccesstest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.util.*;
import jakarta.ejb.*;
import java.rmi.*;

import com.sun.javatest.Status;

public class Client extends EETest {

  private static final String testBean = "java:comp/env/ejb/TestBean";

  private TestBean bRef = null;

  private TestBeanHome bHome = null;

  private TSNamingContext nctx = null;

  private Properties props = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   * generateSQL;
   * 
   * @class.testArgs: -ap tssql.stmt
   */

  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    try {
      logMsg("Obtain naming context");
      nctx = new TSNamingContext();

      // Get EJB Home ...
      logMsg("Looking up home interface for EJB: " + testBean);
      bHome = (TestBeanHome) nctx.lookup(testBean, TestBeanHome.class);
      logMsg("Setup ok");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("Setup failed:", e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /* Run test */

  /*
   * @testName: test1
   * 
   * @assertion_ids: EJB:SPEC:2; EJB:SPEC:2.2; EJB:SPEC:123.2
   * 
   * @test_Strategy: Create an Entity Bean that accesses a Local Entity Bean
   * within the same EAR. Deploy EAR on the J2EE server. Verify local access
   * from Entity Bean to a local Entity Bean (CMP).
   */

  public void test1() throws Fault {
    logTrace("test1");
    boolean pass = false;
    try {
      logMsg("Create EJB instance");
      bRef = bHome.create(props, 1, "coffee-1", 1);
      logMsg("Perform Local object access test from SB to Local EB (CMP)");
      pass = bRef.test1();
    } catch (Exception e) {
      throw new Fault("test1 failed", e);
    } finally {
      try {
        bRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }

    if (!pass)
      throw new Fault("test1 failed");
  }

  /*
   * @testName: test2
   * 
   * @assertion_ids: EJB:SPEC:2.1; EJB:SPEC:123.1
   * 
   * @test_Strategy: An entity bean instance has access to the LocalHome and
   * Local Interfaces of a Local stateless Session Bean. Verify local access
   * from Entity Bean to a local stateful session bean.
   */

  public void test2() throws Fault {
    logTrace("test2");
    boolean pass = false;
    try {
      logMsg("Create EJB instance");
      bRef = bHome.create(props, 1, "coffee-1", 1);
      logMsg("Perform Local object access test from SB to Local SB (SF)");
      pass = bRef.test2();
    } catch (Exception e) {
      throw new Fault("test2 failed", e);
    } finally {
      try {
        bRef.cleanUpStatefulBean();
        bRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }

    if (!pass)
      throw new Fault("test2 failed");
  }

  /*
   * @testName: test3
   * 
   * @assertion_ids: EJB:SPEC:2.2; EJB:SPEC:123.2
   * 
   * @test_Strategy: An entity bean instance has access to the LocalHome and
   * Local Interfaces of a Local Entity Bean (BMP). Verify local access from
   * Entity Bean to a local Entity Bean (BMP).
   */

  public void test3() throws Fault {
    logTrace("test3");
    boolean pass = false;
    try {
      logMsg("Create EJB instance");
      bRef = bHome.create(props, 1, "coffee-1", 1);
      logMsg("Perform Local object access test from SB to Local EB (BMP)");
      pass = bRef.test3(props);
    } catch (Exception e) {
      throw new Fault("test3 failed", e);
    } finally {
      try {
        bRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }

    if (!pass)
      throw new Fault("test3 failed");
  }

  /*
   * @testName: test4
   * 
   * @assertion_ids: EJB:SPEC:2.1; EJB:SPEC:123.1
   * 
   * @test_Strategy: An entity bean instance has access to the LocalHome and
   * Local Interfaces of a Local stateful Session Bean. Verify local access from
   * Entity Bean to a local stateless session bean.
   */

  public void test4() throws Fault {
    logTrace("test4");
    boolean pass = false;
    try {
      logMsg("Create EJB instance");
      bRef = bHome.create(props, 1, "coffee-1", 1);
      logMsg("Perform Local object access test from SB to Local SB (SF)");
      pass = bRef.test4();
    } catch (Exception e) {
      throw new Fault("test4 failed", e);
    } finally {
      try {
        bRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }

    if (!pass)
      throw new Fault("test4 failed");
  }
}
