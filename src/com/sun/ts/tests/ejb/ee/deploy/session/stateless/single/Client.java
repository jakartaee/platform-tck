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
 * @(#)Client.java	1.28 03/05/16
 */

package com.sun.ts.tests.ejb.ee.deploy.session.stateless.single;

import java.util.Properties;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.harness.EETest;
import com.sun.javatest.Status;

public class Client extends EETest {

  /** Name used to do a JNDI lookup for the bean */
  private static final String lookupName = "java:comp/env/ejb/TestBean";

  /** Fully qualified name of the bean Remote interface */
  private static final String beanClassName = "com.sun.ts.tests.ejb.ee.deploy.session.stateless.single.TestBean";

  private TSNamingContext nctx = null;

  private TestBeanHome home = null;

  private TestBean bean = null;

  private Properties props = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /**
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   *
   */
  public void setup(String[] args, Properties p) throws Fault {
    props = p;

    try {
      nctx = new TSNamingContext();
      logMsg("Client: Looked up home!");

      home = (TestBeanHome) nctx.lookup(lookupName, TestBeanHome.class);
      logMsg("Client: Setup OK!");
    } catch (Exception e) {
      throw new Fault("Client: Setup failed:" + e, e);
    }
  }

  /**
   * @testName: testIsSession
   *
   * @assertion_ids: EJB:JAVADOC:71
   *
   * @test_Strategy: Deploy a Stateless Session Bean. Invoke
   *                 getEJBMetaData().isSession() method at runtime on the Home
   *                 interface. Check that returned value is 'true'.
   */
  public void testIsSession() throws Fault {
    try {
      if (!home.getEJBMetaData().isSession()) {
        throw new Fault("isSession() test failed");
      }
    } catch (Exception e) {
      throw new Fault("isSession() test failed: " + e, e);
    }
  }

  /**
   * @testName: testIsStateless
   *
   * @assertion_ids: EJB:JAVADOC:72
   *
   * @test_Strategy: Deploy and create a Stateless Session Bean. Invoke
   *                 getEJBMetaData().isStatelessSession() method at runtime and
   *                 check that returned value is 'true'.
   */

  public void testIsStateless() throws Fault {
    boolean pass;

    try {
      pass = home.getEJBMetaData().isStatelessSession();
      if (!pass) {
        logErr("Client: isStatelessSession() reported '" + pass + "'");
        throw new Fault("isStatelessSession() test failed");
      }
    } catch (Exception e) {
      throw new Fault("isStatelessSession() test failed: " + e, e);
    }
  }

  /**
   * @testName: testGetPrimaryKey
   *
   * @assertion_ids: EJB:SPEC:39; EJB:JAVADOC:78
   *
   * @test_Strategy: Deploy and create a Stateless Session Bean. Attempt to call
   *                 getPrimaryKey() and check that we catch a
   *                 java.rmi.RemoteException.
   */

  public void testGetPrimaryKey() throws Fault {
    Object pkey = null;

    try {
      bean = (TestBean) home.create();
      bean.initLogging(props);
      pkey = bean.getPrimaryKey();
      /* We should never get there */
      bean.remove();
      throw new Fault("getPrimaryKey() test failed");
    } catch (java.rmi.RemoteException e) {
      /* Test succeeds */
    } catch (Exception e) {
      throw new Fault("getPrimaryKey() test failed: " + e, e);
    }
  }

  /**
   * @testName: testIdentity
   *
   * @assertion_ids: EJB:SPEC:40; EJB:JAVADOC:146
   *
   * @test_Strategy: Create two Stateless Session Bean using the same home.
   *                 Check that they have the same identity, using the
   *                 EJBObject.isIdentical() method.
   */
  public void testIdentity() throws Fault {
    TestBean bean1 = null;
    TestBean bean2 = null;
    boolean pass = false;

    try {
      bean1 = (TestBean) home.create();
      bean1.initLogging(props);

      bean2 = (TestBean) home.create();
      bean2.initLogging(props);

      pass = bean1.isIdentical(bean1) && bean2.isIdentical(bean2)
          && bean1.isIdentical(bean2) && bean2.isIdentical(bean1);

      bean1.remove();
      bean2.remove();

      if (!pass) {
        throw new Fault("isIdentical() test failed.");
      }
    } catch (Exception e) {
      throw new Fault("isIdentical() test failed: " + e, e);
    }
  }

  /**
   * @testName: testHomeInterface
   *
   * @assertion_ids: EJB:JAVADOC:68
   *
   * @test_Strategy: Deploy a Stateless Session Bean. Invoke
   *                 getEJBMetaData().getHomeInterfaceClass() on its home
   *                 interface and check that the name of the returned Class
   *                 correspond with the one specified in the DD.
   */

  public void testHomeInterface() throws Fault {
    Class runtimeHome = null;
    boolean pass = false;

    try {
      runtimeHome = home.getEJBMetaData().getHomeInterfaceClass();
      pass = runtimeHome.getName().equals(beanClassName + "Home");
      if (!pass) {
        throw new Fault("getHomeInterface() test failed ");
      }
    } catch (Exception e) {
      throw new Fault("getHomeInterface() test failed: " + e, e);
    }
  }

  /**
   * @testName: testRemoteInterface
   *
   * @assertion_ids: EJB:JAVADOC:70
   *
   * @test_Strategy: Deploy a Stateless Session Bean. Invoke
   *                 getEJBMetaData().getRemoteInterfaceClass() on its home
   *                 interface and check that the name of the returned Class
   *                 correspond with the one specified in the DD.
   */
  public void testRemoteInterface() throws Fault {
    Class runtimeRemote = null;
    boolean pass = false;

    try {
      runtimeRemote = home.getEJBMetaData().getRemoteInterfaceClass();
      pass = runtimeRemote.getName().equals(beanClassName);
      if (!pass) {
        throw new Fault("getRemoteInterface() test failed ");
      }
    } catch (Exception e) {
      throw new Fault("getRemoteInterface() test failed: " + e, e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("Client: cleanup()");
  }
}
