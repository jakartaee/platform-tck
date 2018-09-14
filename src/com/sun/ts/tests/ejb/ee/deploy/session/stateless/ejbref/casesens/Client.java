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

package com.sun.ts.tests.ejb.ee.deploy.session.stateless.ejbref.casesens;

import java.util.Properties;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.harness.EETest;
import com.sun.javatest.Status;

public class Client extends EETest {

  private static final String beanName = "java:comp/env/ejb/TestBean";

  private TSNamingContext nctx = null;

  private Properties props = null;

  private TestBeanHome home = null;

  private TestBean bean = null;

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
      logTrace("Client: Getting naming context...");
      nctx = new TSNamingContext();
      logTrace("Client: Looking up home...");
      home = (TestBeanHome) nctx.lookup(beanName, TestBeanHome.class);
      logMsg("Client: Setup OK!");
    } catch (Exception e) {
      throw new Fault("Client: Setup failed:", e);
    }
  }

  /**
   * @testName: testCaseSensitivity
   *
   * @assertion_ids: EJB:SPEC:872
   *
   * @test_Strategy: Deploy a Stateless Session bean (TestBean) with two EJB
   *                 references whose name differ only by case and are assigned
   *                 to two distinct beans (Same type of bean, but the two beans
   *                 are packaged with different values for a String environment
   *                 entry called 'myName').
   *
   *                 Check that TestBean can lookup the two beans. Check that
   *                 their runtime value for the 'myName' env. entry are
   *                 distinct and match the ones specified in the DD (to check
   *                 that the EJB reference were resolved correctly).
   */
  public void testCaseSensitivity() throws Fault {

    boolean pass = true;
    TestBean bean = null;

    try {
      logTrace("Client: creating TestBean instance...");
      bean = home.create();
      bean.initLogging(props);
      logTrace("Client: Calling TestBean...");
      pass = bean.testCaseSensitivity(props);
      logTrace("Client: Removing TestBean...");
      bean.remove();

      if (!pass) {
        throw new Fault("ejb-ref case sensitivity test failed");
      }
    } catch (Exception e) {
      throw new Fault("ejbref case sensitivity test failed: ", e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("Client: cleanup");
  }

}
