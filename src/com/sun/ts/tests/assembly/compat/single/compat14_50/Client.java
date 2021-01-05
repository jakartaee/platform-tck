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
 * %W %E
 */

package com.sun.ts.tests.assembly.compat.single.compat14_50;

import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TSNamingContext;

public class Client extends EETest {

  /** JNDI Name we use to lookup the bean */
  public static final String lookupName = "java:comp/env/ejb/TestBean";

  private TestBeanHome home;

  private TSNamingContext nctx = null;

  private Properties props = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   *
   */
  public void setup(String[] args, Properties p) throws Fault {
    props = p;

    try {
      logMsg("[Client] setup(): getting Naming Context...");
      nctx = new TSNamingContext();

      logTrace("[Client] Looking up bean Home...");
      home = (TestBeanHome) nctx.lookup(lookupName, TestBeanHome.class);

      logMsg("[Client] Setup OK!");
    } catch (Exception e) {
      throw new Fault("[Client] Setup failed:" + e, e);
    }
  }

  /**
   * @testName: test14Compat
   *
   * @assertion_ids: JavaEE:SPEC:283; JavaEE:SPEC:284
   *
   * @test_Strategy: Package an application with an application client and an
   *                 EJB jar file. Use JavaEE 5.0 DD for the 2 modules, and a
   *                 JavaEE 1.4 DD for the application DD.
   *
   *                 Check that: - we can deploy the application, - the
   *                 application client can lookup a bean - the application
   *                 client can create a bean instance - the application client
   *                 can invoke a business method on that instance
   */
  public void test14Compat() throws Fault {
    TestBean bean;
    boolean pass;

    try {
      logTrace("[Client] Creating bean instance...");
      bean = home.create();
      bean.initLogging(props);

      logTrace("[Client] Calling bean...");
      pass = bean.ping();
      if (!pass) {
        throw new Fault("Compat single test failed");
      }
    } catch (Exception e) {
      throw new Fault("Compat single test failed: " + e, e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("[Client] Cleanup()");
  }

}
