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
 * @(#)Client.java	1.11 03/05/16
 */

package com.sun.ts.tests.ejb.ee.bb.session.lrapitest;

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
   * @assertion_ids: EJB:JAVADOC:166; EJB:JAVADOC:168
   * 
   * @test_Strategy: Perform the following operations via the EJBContext
   * interface for an Session Bean with both a Local and Remote interface: o
   * context.getEJBObject() o context.getEJBLocalObject() Verify EJBContext
   * methods work as specified.
   */

  public void test1() throws Fault {
    logTrace("test1");
    boolean pass = false;
    try {
      logMsg("Create EJB instance");
      bRef = bHome.create(props);
      logMsg("Perform Local/Remote object access");
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
   * @assertion_ids: EJB:JAVADOC:167
   * 
   * @test_Strategy: Perform the following operations via the EJBContext
   * interface for an Session Bean with a Remote interface: o
   * context.getEJBObject() A bean instance cannot perform the following
   * operation via the EJBContext interface for an Session Bean if it only has a
   * Remote interface: o context.getEJBLocalObject() Verify EJBContext methods
   * work as specified and throws an exception of type IllegalStateException if
   * interface is not supported.
   */

  public void test2() throws Fault {
    logTrace("test2");
    boolean pass = false;
    try {
      logMsg("Create EJB instance");
      bRef = bHome.create(props);
      logMsg("Attempt Remote/Local access to Session Bean when only Remote");
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
   * @assertion_ids: EJB:JAVADOC:169
   * 
   * @test_Strategy: Perform the following operations via the EJBContext
   * interface for an Session Bean with a Local interface: o
   * context.getEJBLocalObject() A bean instance cannot perform the following
   * operation via the EJBContext interface for an Session Bean if it only has a
   * Local interface: o context.getEJBObject() Verify EJBContext methods work as
   * specified and throws an exception of type IllegalStateException if
   * interface is not supported.
   */

  public void test3() throws Fault {
    logTrace("test3");
    boolean pass = false;
    try {
      logMsg("Create EJB instance");
      bRef = bHome.create(props);
      logMsg("Attempt Remote/Local access to Session Bean when only Local");
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
   * @assertion_ids: EJB:JAVADOC:151; EJB:JAVADOC:55; EJB:SPEC:34
   * 
   * @test_Strategy: Perform the following operations from the LocalHome
   * interface: o create A bean instance cannot perform a remove operation
   * passing the primary key since session objects do not have an identity and
   * so a RemoveException should occur: o remove(Object pk) Verify all
   * operations through the LocalHome interface.
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
   * EJB:JAVADOC:58; EJB:SPEC:54.1; EJB:SPEC:50
   * 
   * @test_Strategy: A bean instance can perform the following operations from
   * the EJBLocalObject interface: o getPrimaryKey() o
   * isIdentical(EJBLocalObject o) o remove() o getEJBLocalHome() o invoke
   * business method
   *
   * Verify all operations through the EJBLocalObject interface.
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
   * @assertion_ids: EJB:SPEC:49.4; EJB:SPEC:52.3; EJB:SPEC:45.3
   * 
   * @test_Strategy: A bean instance can pass both the Remote and Local object
   * references via the EJBContext interface for a Session Bean with both a
   * Local and Remote interface. Create a Session Bean. Deploy it on the J2EE
   * server. Verify object reference passing of both local and remote
   * references.
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
   * @assertion_ids: EJB:SPEC:53; EJB:SPEC:46
   * 
   * @test_Strategy: Create a local stateful session bean. Remove local object
   * reference and attempt to call the bean again. Expect the following
   * exception javax.ejb.NoSuchObjectLocalException.
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
   * @assertion_ids: EJB:SPEC:78; EJB:JAVADOC:65
   * 
   * @test_Strategy: If a session bean is participating in a transaction, it is
   * an error for a client to invoke the remove method on the session object's
   * home or component interface. The container must detect such an attempt and
   * throw the javax.ejb.RemoveException.
   *
   * Create a local stateful session bean. Invoke business method and attempt to
   * invoke the remove method on the local session object's component interface
   * while participating in a transaction. Expect javax.ejb.RemoveException.
   */

  public void test8() throws Fault {
    logTrace("test8");
    boolean pass = false;
    try {
      logMsg("Create EJB instance");
      bRef = bHome.create(props);
      logMsg("Test for javax.ejb.RemoveException on Local Interface");
      pass = bRef.test8();
    } catch (Exception e) {
      throw new Fault("test8 failed", e);
    } finally {
      try {
        bRef.cleanUpStatefulBean();
        bRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }

    if (!pass)
      throw new Fault("test8 failed");

  }

  /*
   * @testName: test9
   * 
   * @assertion_ids: EJB:SPEC:78; EJB:JAVADOC:65
   * 
   * @test_Strategy: If a session bean is participating in a transaction, it is
   * an error for a client to invoke the remove method on the session object's
   * home or component interface. The container must detect such an attempt and
   * throw the javax.ejb.RemoveException.
   *
   * Create an remote stateful session bean. Invoke business method and attempt
   * to invoke the remove method on the remote session object's component
   * interface while participating in a transaction. Expect
   * javax.ejb.RemoveException.
   */

  public void test9() throws Fault {
    logTrace("test9");
    boolean pass = false;
    try {
      logMsg("Create EJB instance");
      bRef = bHome.create(props);
      logMsg("Test for javax.ejb.RemoveException on Remote Interface");
      pass = bRef.test9();
    } catch (Exception e) {
      throw new Fault("test9 failed", e);
    } finally {
      try {
        bRef.cleanUpStatefulBean();
        bRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }

    if (!pass)
      throw new Fault("test9 failed");

  }

  /*
   * @testName: test10
   * 
   * @assertion_ids: EJB:SPEC:78; EJB:JAVADOC:55
   * 
   * @test_Strategy: If a session bean is participating in a transaction, it is
   * an error for a client to invoke the remove method on the session object's
   * home or component interface. The container must detect such an attempt and
   * throw the javax.ejb.RemoveException.
   *
   * Create an local stateful session bean. Invoke business method and attempt
   * to invoke the remove method on the session object's local home interface
   * while participating in a transaction.
   */

  public void test10() throws Fault {
    logTrace("test10");
    boolean pass = false;
    try {
      logMsg("Create EJB instance");
      bRef = bHome.create(props);
      logMsg("Test for javax.ejb.RemoveException on Local Home Interface");
      pass = bRef.test10();
    } catch (Exception e) {
      throw new Fault("test10 failed", e);
    } finally {
      try {
        bRef.cleanUpStatefulBean();
        bRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }

    if (!pass)
      throw new Fault("test10 failed");

  }

  /*
   * @testName: test11
   * 
   * @assertion_ids: EJB:SPEC:78; EJB:JAVADOC:53
   * 
   * @test_Strategy: If a session bean is participating in a transaction, it is
   * an error for a client to invoke the remove method on the session object's
   * home or component interface. The container must detect such an attempt and
   * throw the javax.ejb.RemoveException.
   *
   * Create a remote stateful session bean. Invoke business method and attempt
   * to invoke the remove method on the session object's remote home interface
   * while participating in a transaction.
   */

  public void test11() throws Fault {
    logTrace("test11");
    boolean pass = false;
    try {
      logMsg("Create EJB instance");
      bRef = bHome.create(props);
      logMsg("Test for javax.ejb.RemoveException on Remote Home Interface");
      pass = bRef.test11();
    } catch (Exception e) {
      throw new Fault("test11 failed", e);
    } finally {
      try {
        bRef.cleanUpStatefulBean();
        bRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }

    if (!pass)
      throw new Fault("test11 failed");

  }

  /*
   * @testName: test12
   * 
   * @assertion_ids: EJB:SPEC:78; EJB:JAVADOC:53
   * 
   * @test_Strategy: If a session bean is participating in a transaction, it is
   * an error for a client to invoke the remove method on the session object's
   * home or component interface. The container must detect such an attempt and
   * throw the javax.ejb.RemoveException. The container should not mark the
   * Transaction for rollback, thus allowing the client to recover.
   *
   * Create local SSF bean and remove, create remote SSF bean and remove
   * ensuring that the TX is not rolled back.
   */

  public void test12() throws Fault {
    logTrace("test12");
    boolean pass = false;
    try {
      logMsg("Create EJB instance");
      bRef = bHome.create(props);
      logMsg("Test the RemoveException allows client to recover");
      pass = bRef.test12();
    } catch (Exception e) {
      throw new Fault("test12 failed", e);
    } finally {
      try {
        bRef.cleanUpStatefulBean();
        bRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }

    if (!pass)
      throw new Fault("test12 failed");

  }

}
