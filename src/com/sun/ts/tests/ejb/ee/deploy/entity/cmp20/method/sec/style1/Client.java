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

package com.sun.ts.tests.ejb.ee.deploy.entity.cmp20.method.sec.style1;

import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.porting.TSLoginContext;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;

public class Client extends EETest {

  private static final String prefix = "java:comp/env/ejb/";

  private static final String bean1Lookup = prefix + "TestBean1";

  private static final String bean2Lookup = prefix + "TestBean2";

  private static final String userPropName = "user";

  private static final String pwdPropName = "password";

  private TSNamingContext nctx = null;

  private Properties props = null;

  private String user;

  private String password;

  private TestBeanHome beanHome;

  private TestBean bean;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   * user; password; generateSQL;
   */
  public void setup(String[] args, Properties props) throws Fault {

    try {
      this.props = props;

      TestUtil.logTrace("[Client] Getting TS Naming Context...");
      nctx = new TSNamingContext();

      TestUtil.logTrace("[Client] Getting user/password info...");
      user = props.getProperty(userPropName);
      password = props.getProperty(pwdPropName);
      TestUtil.logTrace("[Client] Log in as " + user + " / " + password);
      TSLoginContext lc = new TSLoginContext();
      lc.login(user, password);

    } catch (Exception e) {
      TestUtil.logErr("[Client] Caught exception: " + e);
      throw new Fault("Setup failed:", e);
    }
  }

  /**
   * @testName: testStyle1Positive
   *
   * @assertion_ids: EJB:SPEC:805; EJB:SPEC:811
   *
   * @test_Strategy: Package a CMP 2.0 Entity bean using a Style1 declaration to
   *                 grant permission to role 'Employee'. Login as a user
   *                 associated to this security role and check that we can call
   *                 a business method on that bean.
   */
  public void testStyle1Positive() throws Fault {
    boolean pass;

    try {
      bean = null;
      TestUtil.logTrace("[Client] Looking up " + bean1Lookup);
      beanHome = (TestBeanHome) nctx.lookup(bean1Lookup, TestBeanHome.class);
      bean = beanHome.create(props, 1, "cappucino", 11);

      TestUtil.logTrace("[Client] Calling test1()...");
      pass = bean.test1();
      if (!pass) {
        throw new Fault("Style1 positive test failed.");
      }
    } catch (Exception e) {
      TestUtil.logErr("[Client] Unexpected exception: " + e);
      throw new Fault("Style1 positive test failed: ", e);
    } finally {
      /* Make sure we always attempt to cleanup the bean. */
      try {
        if (null != bean) {
          TestUtil.logTrace("[Client] Removing bean...");
          bean.remove();
        }
      } catch (Exception e) {
        TestUtil
            .logMsg("[Client] Ignoring Exception on " + "bean remove: " + e);
      }
    }
  }

  /**
   * @testName: testStyle1Negative
   *
   * @assertion_ids: EJB:SPEC:805; EJB:SPEC:811
   *
   * @test_Strategy: Package a CMP 2.0 Entity bean using a Style1 declaration to
   *                 grant permissions to role 'Manager' only. Login as a user
   *                 that is not associated to this security role and check that
   *                 we get a java.rmi.RemoteException when calling a business
   *                 method on that bean.
   */
  public void testStyle1Negative() throws Fault {
    boolean pass;

    try {
      TestUtil.logTrace("[Client] Looking up " + bean2Lookup);
      beanHome = (TestBeanHome) nctx.lookup(bean2Lookup, TestBeanHome.class);
      bean = beanHome.create(props, 2, "mocha", 11);

      TestUtil.logTrace("[Client] Calling test1()...");
      pass = bean.test1();

      /* We should never get there (unsufficient permissions) */
      throw new Fault("Style1 negative test failed: "
          + "We were allowed to can test1 method!");
    } catch (java.rmi.RemoteException e) {
      TestUtil.logTrace(
          "[Client] Caught " + "java.rmi.RemoteException as expected");
      /* Test pass */
    } catch (Exception e) {
      TestUtil.logErr("[Client] Unexpected exception: " + e);
      throw new Fault("Style1 negative test failed: ", e);
    } finally {
      /* Make sure we always attempt to cleanup the bean. */
      try {
        if (null != bean) {
          TestUtil.logTrace("[Client] Removing bean...");
          bean.remove();
        }
      } catch (Exception e) {
        TestUtil
            .logMsg("[Client] Ignoring Exception on " + "bean remove: " + e);
      }
    }
  }

  /**
   * @testName: testStyle2Style1
   *
   * @assertion_ids: EJB:SPEC:805
   *
   * @test_Strategy: Package a CMP 2.0 Entity bean using: - a Style1 declaration
   *                 to grant permissions to role Employee only. - a Style2
   *                 declaration to grant permissions to role Manager only for
   *                 business method test2.
   *
   *                 Login as a user that is associated with the Employee
   *                 security role, but not the Manager's one. Check that we can
   *                 call test2() business method.
   */
  public void testStyle2Style1() throws Fault {
    boolean pass;

    try {
      TestUtil.logTrace("[Client] Looking up " + bean1Lookup);
      beanHome = (TestBeanHome) nctx.lookup(bean1Lookup, TestBeanHome.class);
      bean = beanHome.create(props, 3, "expresso", 11);

      TestUtil.logTrace("[Client] Calling test2()...");
      pass = bean.test2();
      if (!pass) {
        throw new Fault("Style2 union test failed.");
      }
    } catch (Exception e) {
      TestUtil.logErr("[Client] Unexpected exception: " + e);
      throw new Fault("Style2 union test failed: ", e);
    } finally {
      /* Make sure we always attempt to cleanup the bean. */
      try {
        if (null != bean) {
          TestUtil.logTrace("[Client] Removing bean...");
          bean.remove();
        }
      } catch (Exception e) {
        TestUtil
            .logMsg("[Client] Ignoring Exception on " + "bean remove: " + e);
      }
    }
  }

  /**
   * @testName: testStyle3Style1
   *
   * @assertion_ids: EJB:SPEC:805
   *
   * @test_Strategy: Package a CMP 2.0 Entity bean using: - a Style1 declaration
   *                 to grant permissions to role Employee only. - a Style3
   *                 declaration to grant permissions to role Manager only for
   *                 business method test3(int).
   *
   *                 Login as a user that is associated with the Employee
   *                 security role, but not the Manager's one. Check that we can
   *                 call test3 business method on that bean.
   */
  public void testStyle3Style1() throws Fault {
    boolean pass;

    try {
      TestUtil.logTrace("[Client] Looking up " + bean1Lookup);
      beanHome = (TestBeanHome) nctx.lookup(bean1Lookup, TestBeanHome.class);
      bean = beanHome.create(props, 4, "cafe creme", 11);

      TestUtil.logTrace("[Client] Calling test3()...");
      pass = bean.test3(1789);
      if (!pass) {
        throw new Fault("Style2 union test failed.");
      }
    } catch (Exception e) {
      TestUtil.logErr("[Client] Unexpected exception: " + e);
      throw new Fault("Style3 union test failed: ", e);
    } finally {
      /* Make sure we always attempt to cleanup the bean. */
      try {
        if (null != bean) {
          TestUtil.logTrace("[Client] Removing bean...");
          bean.remove();
        }
      } catch (Exception e) {
        TestUtil
            .logMsg("[Client] Ignoring Exception on " + "bean remove: " + e);
      }
    }
  }

  public void cleanup() throws Fault {
    logMsg("[Client] cleanup()");
  }

}
