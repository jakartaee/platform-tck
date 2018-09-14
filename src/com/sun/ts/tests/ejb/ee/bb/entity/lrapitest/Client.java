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
 * @(#)Client.java	1.14 03/05/16
 */

package com.sun.ts.tests.ejb.ee.bb.entity.lrapitest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.util.*;
import javax.ejb.*;
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
   * @assertion_ids: EJB:JAVADOC:112; EJB:JAVADOC:114
   * 
   * @test_Strategy: Perform the following operations via the EJBContext
   * interface for an Entity Bean with both a Local and Remote interface: o
   * context.getEJBObject() o context.getEJBLocalObject() Also you can map
   * between Local and Remote using primarykey related to interfaces. o
   * mapLocalToRemote o mapRemoteToLocal Create an Entity Bean Deploy it on the
   * J2EE server. Verify EJBContext methods work as specified
   */

  public void test1() throws Fault {
    logTrace("test1");
    boolean pass = false;
    try {
      logMsg("Create EJB instance");
      bRef = bHome.create(props);
      logMsg("Perform Local/Remote object access and mapping tests");
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
   * @assertion_ids: EJB:JAVADOC:113
   * 
   * @test_Strategy: An entity bean instance cannot perform the following
   * operations via the EJBContext interface for an Entity Bean with only has a
   * Remote interface: o context.getEJBLocalObject() An IllegalStateException
   * results. Verify EJBContext methods work as specified and throws an
   * IllegalStateException.
   */

  public void test2() throws Fault {
    logTrace("test2");
    boolean pass = false;
    try {
      logMsg("Create EJB instance");
      bRef = bHome.create(props);
      logMsg("Attempt Remote/Local access to Entity Bean when only Remote");
      pass = bRef.test2();
    } catch (Exception e) {
      throw new Fault("test2 failed", e);
    } finally {
      try {
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
   * @assertion_ids: EJB:JAVADOC:115
   * 
   * @test_Strategy: An entity bean instance cannot perform the following
   * operations via the EJBContext interface for an Entity Bean with only has a
   * Local interface: o context.getEJBObject() An IllegalStateException results.
   * Verify EJBContext methods work as specified and throws an
   * IllegalStateException. Verify EJBContext methods work as specified and
   * throws an IllegalStateException.
   */

  public void test3() throws Fault {
    logTrace("test3");
    boolean pass = false;
    try {
      logMsg("Create EJB instance");
      bRef = bHome.create(props);
      logMsg("Attempt Remote/Local access to Entity Bean when only Local");
      pass = bRef.test3();
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
   * @assertion_ids: EJB:JAVADOC:58; EJB:JAVADOC:55; EJB:SPEC:147.2;
   * EJB:SPEC:147.5; EJB:SPEC:147.1; EJB:SPEC:146.2; EJB:SPEC:139.1;
   * EJB:SPEC:139.2; EJB:SPEC:139.3; EJB:SPEC:139.4
   * 
   * @test_Strategy: An entity bean instance can perform the following
   * operations from the LocalHome interface: o create o find o remove o execute
   * a home business method Verify all operations through the LocalHome
   * interface.
   */

  public void test4() throws Fault {
    logTrace("test4");
    boolean pass = false;
    try {
      logMsg("Create EJB instance");
      bRef = bHome.create(props);
      logMsg("EJBLocalHome tests ...");
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

  /*
   * @testName: test5
   * 
   * @assertion_ids: EJB:JAVADOC:60; EJB:JAVADOC:62; EJB:JAVADOC:64;
   * EJB:SPEC:147; EJB:SPEC:147.1; EJB:SPEC:147.2; EJB:SPEC:147.4;
   * EJB:SPEC:147.5
   * 
   * @test_Strategy: An entity bean instance can perform the following
   * operations from the EJBLocalObject interface: o getPrimaryKey() o
   * isIdentical(EJBLocalObject o) o remove() o getEJBLocalHome() o invoke
   * business method Verify all operations through the EJBLocalObject interface.
   */

  public void test5() throws Fault {
    logTrace("test5");
    boolean pass = false;
    try {
      logMsg("Create EJB instance");
      bRef = bHome.create(props);
      logMsg("EJBLocalObject tests ...");
      pass = bRef.test5();
    } catch (Exception e) {
      throw new Fault("test5 failed", e);
    } finally {
      try {
        bRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }

    if (!pass)
      throw new Fault("test5 failed");
  }

  /*
   * @testName: test6
   * 
   * @assertion_ids: EJB:SPEC:147.3
   * 
   * @test_Strategy: Create an Entity Bean. Verify object reference passing of
   * both local and remote references.
   */

  public void test6() throws Fault {
    logTrace("test6");
    boolean pass = false;
    try {
      logMsg("Create EJB instance");
      bRef = bHome.create(props);
      logMsg("Perform Local/Remote object passing references");
      pass = bRef.test6();
    } catch (Exception e) {
      throw new Fault("test6 failed", e);
    } finally {
      try {
        bRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }

    if (!pass)
      throw new Fault("test6 failed");
  }

  /*
   * @testName: test7
   * 
   * @assertion_ids: EJB:SPEC:147.6; EJB:JAVADOC:143
   * 
   * @test_Strategy: Create an Entity Bean Deploy it on the J2EE server. Remove
   * local object reference and attempt to call the bean again. Expect the
   * following exception javax.ejb.NoSuchObjectLocalException.
   */

  public void test7() throws Fault {
    logTrace("test7");
    boolean pass = false;
    try {
      logMsg("Create EJB instance");
      bRef = bHome.create(props);
      logMsg("Test for javax.ejb.NoSuchObjectLocalException");
      pass = bRef.test7();
    } catch (Exception e) {
      throw new Fault("test7 failed", e);
    } finally {
      try {
        bRef.remove();
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
   * @assertion_ids: EJB:SPEC:147.1; EJB:SPEC:147.2; EJB:SPEC:147.5;
   * EJB:SPEC:146.2
   * 
   * @test_Strategy: Create an Entity BMP bean. Verify all operations through
   * the LocalHome interface.
   */

  public void test8() throws Fault {
    logTrace("test8");
    boolean pass = false;
    try {
      logMsg("Create EJB instance");
      bRef = bHome.create(props);
      logMsg("EJBLocalHome tests ...");
      pass = bRef.test8();
    } catch (Exception e) {
      throw new Fault("test8 failed", e);
    } finally {
      try {
        bRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }

    if (!pass)
      throw new Fault("test8 failed");
  }
}
