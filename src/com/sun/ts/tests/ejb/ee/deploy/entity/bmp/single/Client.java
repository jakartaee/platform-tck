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
 * @(#)Client.java	1.35 03/05/16
 */

package com.sun.ts.tests.ejb.ee.deploy.entity.bmp.single;

import java.util.Properties;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.tests.common.dao.DAOFactory;
import com.sun.javatest.Status;

public class Client extends EETest {

  private static final String testLookup = "java:comp/env/ejb/TestBean";

  private static final String testName = "com.sun.ts.tests.ejb.ee.deploy.entity.bmp.single.Single";

  /* String used to identify bean remote interface */
  private static final String refIdString = "Entity Single Remote";

  private TSNamingContext nctx = null;

  private SingleHome home = null;

  private Properties props = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /**
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   *
   * @class.testArgs: -ap tssql.stmt
   *
   */
  public void setup(String[] args, Properties props) throws Fault {

    try {
      this.props = props;

      logMsg("[Client] Getting TS Naming Context...");
      nctx = new TSNamingContext();

      logMsg("[Client] Initializing DB table...");
      DAOFactory.getInstance().getCoffeeDAO().cleanup();

      logMsg("Looking up up " + testLookup + "...");
      home = (SingleHome) nctx.lookup(testLookup, SingleHome.class);
      logMsg("Setup OK!");
    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  /**
   * @testName: testIsSession
   *
   * @assertion_ids: EJB:JAVADOC:71
   *
   * @test_Strategy: Deploy a BMP Entity Bean an lookup its home interface.
   *                 Invoke getEJBMetaData().isSession() method at runtime and
   *                 check that returned value is 'false'.
   */
  public void testIsSession() throws Fault {
    try {
      if (home.getEJBMetaData().isSession()) {
        throw new Fault("isSession() test failed [is true]");
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
   * @test_Strategy: Deploy a BMP Entity Bean and lookup its home interface.
   *                 Invoke getEJBMetaData().isStatelessSession() method at
   *                 runtime and check that returned value is 'false'.
   */
  public void testIsStateless() throws Fault {
    boolean pass;

    try {
      pass = !home.getEJBMetaData().isStatelessSession();
      if (!pass) {
        logErr("[Client] isStatelessSession() reported '" + pass + "'");
        throw new Fault("isStatelessSession() test failed");
      }
    } catch (Exception e) {
      throw new Fault("isStatelessSession() test failed: " + e, e);
    }
  }

  /**
   * @testName: testGetPrimaryKey
   *
   * @assertion_ids: EJB:JAVADOC:78
   *
   * @test_Strategy: Deploy and create a BMP Entity Bean. Attempt to call
   *                 getPrimaryKey() and check that we do not catch any
   *                 Exception.
   */
  public void testGetPrimaryKey() throws Fault {
    Single bean = null;
    Object pkey;

    try {
      TestUtil.logTrace("[Client] Creating bean...");
      bean = home.create(props, 1, "coffee-1", 1);
      TestUtil.logTrace("[Client] Getting primary key...");
      pkey = bean.getPrimaryKey();
    } catch (Exception e) {
      throw new Fault("getPrimaryKey() test failed: " + e, e);
    } finally {
      try {
        if (null != bean) {
          bean.remove();
        }
      } catch (Exception e) {
        TestUtil
            .logTrace("[Client] Ignoring exception on " + "bean remove: " + e);
      }
    }
  }

  /**
   * @testName: testHomeInterface
   *
   * @assertion_ids: EJB:JAVADOC:68
   *
   * @test_Strategy: Deploy a BMP Entity Bean. Invoke
   *                 getEJBMetaData().getHomeInterfaceClass() on its home
   *                 interface and check that the name of the returned Class
   *                 correspond with the one specified in the DD (home element).
   */
  public void testHomeInterface() throws Fault {
    Class runtimeHome = null;
    boolean pass = false;

    try {
      runtimeHome = home.getEJBMetaData().getHomeInterfaceClass();
      pass = runtimeHome.getName().equals(testName + "Home");
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
   * @test_Strategy: Deploy a BMP Entity bean and lookup its interface. Invoke
   *                 getEJBMetaData().getRemoteInterfaceClass() on its home
   *                 interface and check that the name of the returned Class
   *                 correspond with the one specified in the DD (remote
   *                 element).
   */
  public void testRemoteInterface() throws Fault {
    Class runtimeRemote = null;
    boolean pass = false;

    try {
      runtimeRemote = home.getEJBMetaData().getRemoteInterfaceClass();
      pass = runtimeRemote.getName().equals(testName);
      if (!pass) {
        throw new Fault("getRemoteInterface() test failed ");
      }
    } catch (Exception e) {
      throw new Fault("getRemoteInterface() test failed: " + e, e);
    }
  }

  /**
   * @testName: testCreate
   *
   * @assertion_ids: EJB:SPEC:432
   *
   * @test_Strategy: Deploy and create a BMP Entity Bean using the special
   *                 create method.
   */
  public void testCreate() throws Fault {
    Single bean = null;

    try {
      bean = home.create(props, 1, "coffee-1", 1);
      bean.remove();
    } catch (Exception e) {
      throw new Fault("Test create failed: ", e);
    }
  }

  /**
   * @testName: testBusinessMethod
   *
   * @assertion_ids: EJB:SPEC:485
   *
   * @test_Strategy: Deploy and create a BMP Entity bean. Invoke a method of its
   *                 remote interface returning a String (we use to "tag" this
   *                 particular home interface).
   */
  public void testBusinessMethod() throws Fault {
    Single bean = null;
    String id;
    boolean pass;

    try {
      bean = home.create(props, 1, "coffee-1", 1);
      id = bean.getIdString();
      logTrace("[Client] got Identification string '" + id + "'");
      pass = id.equals(refIdString);
      bean.remove();
      if (!pass) {
        logErr("[Client] home interface mismatch!");
        throw new Fault(
            "Remote interface test failed, expected '" + refIdString + "'");
      }
    } catch (Exception e) {
      throw new Fault("Remote interface test failed: " + e, e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("[Client] cleanup()");
  }
}
