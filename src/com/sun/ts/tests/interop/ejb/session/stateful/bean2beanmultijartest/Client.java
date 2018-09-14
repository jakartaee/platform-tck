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

package com.sun.ts.tests.interop.ejb.session.stateful.bean2beanmultijartest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.interop.ejb.session.stateful.bean2beanmultijartest.bean1.*;

import java.io.*;
import java.util.*;
import javax.ejb.*;
import javax.transaction.*;
import java.rmi.*;

import com.sun.javatest.Status;

public class Client extends EETest {
  private static final String testBean1 = "java:comp/env/ejb/TestBean1";

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
   * @assertion_ids: EJB:SPEC:665; EJB:SPEC:666; EJB:SPEC:667; EJB:SPEC:668
   * 
   * @test_Strategy: Create 2 stateful Session Beans. Deploy bean1 on J2EE
   * server #1. Deploy bean2 on J2EE server #2. Client calls bean1, bean1 calls
   * bean2. Pass String object from bean1 to bean2. Demonstrates bean to bean
   * interoperability using multiple jar files and passing value objects. Pass
   * String object from bean to bean.
   *
   */

  public void test1() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean1) beanHome.create(props);
      pass = beanRef.passBean1String();
    } catch (Exception e) {
      TestUtil.logErr("Exception caught in test1", e);
      TestUtil.printStackTrace(e);
    } finally {
      try {
        if (beanRef != null) {
          beanRef.remove();
        }
      } catch (Exception e) {
        TestUtil.logTrace("Exception caught while removing bean in test1", e);
      }
    }

    if (!pass)
      throw new Fault("test1 failed");
  }

  /*
   * @testName: test2
   * 
   * @assertion_ids: EJB:SPEC:665; EJB:SPEC:666; EJB:SPEC:667; EJB:SPEC:668;
   * EJB:SPEC:678.1
   * 
   * @test_Strategy: Demonstrates bean to bean interoperability using multiple
   * jar files and passing value objects. Pass Handle object from bean to bean.
   * Create 2 stateful Session Beans. Deploy bean1 on J2EE server #1. Deploy
   * bean2 on J2EE server #2. Client calls bean1, bean1 calls bean2. Pass Handle
   * object from bean1 to bean2. Verify that the Handle is what was expected and
   * can successfully be used.
   *
   */

  public void test2() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean1) beanHome.create(props);
      pass = beanRef.passBean1Handle();
    } catch (Exception e) {
      TestUtil.logErr("Exception caught in test2", e);
      TestUtil.printStackTrace(e);
    } finally {
      try {
        if (beanRef != null) {
          beanRef.remove();
        }
      } catch (Exception e) {
        TestUtil.logTrace("Exception caught while removing bean in test2", e);
      }
    }

    if (!pass)
      throw new Fault("test2 failed");
  }

  /*
   * @testName: test3
   * 
   * @assertion_ids: EJB:SPEC:665; EJB:SPEC:666; EJB:SPEC:667; EJB:SPEC:668;
   * EJB:SPEC:678.1
   * 
   * @test_Strategy: Demonstrates bean to bean interoperability using multiple
   * jar files and passing value objects. Pass HomeHandle object from bean to
   * bean. Create 2 stateful Session Beans. Deploy bean1 on J2EE server #1.
   * Deploy bean2 on J2EE server #2. Client calls bean1, bean1 calls bean2. Pass
   * HomeHandle object from bean1 to bean2. Verify that the HomeHandle is what
   * was expected and can successfully be used.
   *
   */

  public void test3() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean1) beanHome.create(props);
      pass = beanRef.passBean1HomeHandle();
    } catch (Exception e) {
      TestUtil.logErr("Exception caught in test3", e);
      TestUtil.printStackTrace(e);
    } finally {
      try {
        if (beanRef != null) {
          beanRef.remove();
        }
      } catch (Exception e) {
        TestUtil.logTrace("Exception caught while removing bean in test3", e);
      }
    }

    if (!pass)
      throw new Fault("test3 failed");
  }

  /*
   * @testName: test4
   * 
   * @assertion_ids: EJB:SPEC:665; EJB:SPEC:666; EJB:SPEC:667; EJB:SPEC:668;
   * EJB:SPEC:678.1
   * 
   * @test_Strategy: Demonstrates bean to bean interoperability using multiple
   * jar files and passing value objects. Pass EJBMetaData object from bean to
   * bean. Create 2 stateful Session Beans. Deploy bean1 on J2EE server #1.
   * Deploy bean2 on J2EE server #2. Client calls bean1, bean1 calls bean2. Pass
   * EJBMetaData object from bean1 to bean2. Verify that the EJBMetaData is what
   * was expected and can successfully be used.
   *
   */

  public void test4() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean1) beanHome.create(props);
      pass = beanRef.passBean1EJBMetaData();
    } catch (Exception e) {
      TestUtil.logErr("Exception caught in test4", e);
      TestUtil.printStackTrace(e);
    } finally {
      try {
        if (beanRef != null) {
          beanRef.remove();
        }
      } catch (Exception e) {
        TestUtil.logTrace("Exception caught while removing bean in test4", e);
      }
    }

    if (!pass)
      throw new Fault("test4 failed");
  }

  /*
   * @testName: test5
   * 
   * @assertion_ids: EJB:SPEC:665; EJB:SPEC:666; EJB:SPEC:667; EJB:SPEC:668
   * 
   * @test_Strategy: Create 2 stateful Session Beans. Deploy bean1 on J2EE
   * server #1. Deploy bean2 on J2EE server #2. Client calls bean1, bean1 calls
   * bean2. Return String object from bean2 to bean1. Verify that the string is
   * what was expected.
   *
   */

  public void test5() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean1) beanHome.create(props);
      pass = beanRef.returnBean2String();
    } catch (Exception e) {
      TestUtil.logErr("Exception caught in test5", e);
      TestUtil.printStackTrace(e);
    } finally {
      try {
        if (beanRef != null) {
          beanRef.remove();
        }
      } catch (Exception e) {
        TestUtil.logTrace("Exception caught while removing bean in test5", e);
      }
    }

    if (!pass)
      throw new Fault("test5 failed");
  }

  /*
   * @testName: test6
   * 
   * @assertion_ids: EJB:SPEC:665; EJB:SPEC:666; EJB:SPEC:667; EJB:SPEC:668;
   * EJB:SPEC:678.1
   * 
   * @test_Strategy: Create 2 stateful Session Beans. Deploy bean1 on J2EE
   * server #1. Deploy bean2 on J2EE server #2. Client calls bean1, bean1 calls
   * bean2. Return Handle object from bean2 to bean1. Verify that the Handle is
   * what was expected and can successfully be used.
   *
   */

  public void test6() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean1) beanHome.create(props);
      pass = beanRef.returnBean2Handle();
    } catch (Exception e) {
      TestUtil.logErr("Exception caught in test6", e);
      TestUtil.printStackTrace(e);
    } finally {
      try {
        if (beanRef != null) {
          beanRef.remove();
        }
      } catch (Exception e) {
        TestUtil.logTrace("Exception caught while removing bean in test6", e);
      }
    }

    if (!pass)
      throw new Fault("test6 failed");
  }

  /*
   * @testName: test7
   * 
   * @assertion_ids: EJB:SPEC:665; EJB:SPEC:666; EJB:SPEC:667; EJB:SPEC:668;
   * EJB:SPEC:678.1
   * 
   * @test_Strategy: Create 2 stateful Session Beans. Deploy bean1 on J2EE
   * server #1. Deploy bean2 on J2EE server #2. Client calls bean1, bean1 calls
   * bean2. Return HomeHandle object from bean2 to bean1. Verify that the
   * HomeHandle is what was expected and can successfully be used.
   *
   */

  public void test7() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean1) beanHome.create(props);
      pass = beanRef.returnBean2HomeHandle();
    } catch (Exception e) {
      TestUtil.logErr("Exception caught in test7", e);
      TestUtil.printStackTrace(e);
    } finally {
      try {
        if (beanRef != null) {
          beanRef.remove();
        }
      } catch (Exception e) {
        TestUtil.logTrace("Exception caught while removing bean in test7", e);
      }
    }

    if (!pass)
      throw new Fault("test7 failed");
  }

  /*
   * @testName: test8
   * 
   * @assertion_ids: EJB:SPEC:665; EJB:SPEC:666; EJB:SPEC:667; EJB:SPEC:668;
   * EJB:SPEC:678.1
   * 
   * @test_Strategy: Create 2 stateful Session Beans. Deploy bean1 on J2EE
   * server #1. Deploy bean2 on J2EE server #2. Client calls bean1, bean1 calls
   * bean2. Return EJBMetaData object from bean2 to bean1. Verify that the
   * EJBMetaData is what was expected and can successfully be used.
   *
   */

  public void test8() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean1) beanHome.create(props);
      pass = beanRef.returnBean2EJBMetaData();
    } catch (Exception e) {
      TestUtil.logErr("Exception caught in test8", e);
      TestUtil.printStackTrace(e);
    } finally {
      try {
        if (beanRef != null) {
          beanRef.remove();
        }
      } catch (Exception e) {
        TestUtil.logTrace("Exception caught while removing bean in test8", e);
      }
    }

    if (!pass)
      throw new Fault("test8 failed");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }
}
