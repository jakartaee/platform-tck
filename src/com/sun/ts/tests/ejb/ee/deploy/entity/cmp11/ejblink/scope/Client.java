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

package com.sun.ts.tests.ejb.ee.deploy.entity.cmp11.ejblink.scope;

import java.util.Properties;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.javatest.Status;

public class Client extends EETest {

  private static final String prefix = "java:comp/env/ejb/";

  private static final String lookupName = prefix + "TestBean";

  private TestBeanHome home = null;

  private TestBean bean = null;

  private Properties props = null;

  private TSNamingContext nctx = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /**
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   *                     generateSQL;
   * 
   */
  public void setup(String[] args, Properties props) throws Fault {
    this.props = props;

    try {
      logTrace("[Client] Getting Naming Context...");
      nctx = new TSNamingContext();
      logTrace("[Client] Looking up " + lookupName);
      home = (TestBeanHome) nctx.lookup(lookupName, TestBeanHome.class);
    } catch (Exception e) {
      logErr("[Client] Failed to obtain Naming Context:" + e);
      throw new Fault("[Client] Setup failed:" + e, e);
    }
  }

  /**
   * @testName: testScope
   *
   * @assertion_ids: EJB:SPEC:766
   *
   * @test_Strategy: A CMP 1.1. Entity bean references two other beans. One in
   *                 the same JAR file, the other in a distinct JAR file. Both
   *                 referenced bean use the same ejb-name in their respective
   *                 JAR file, and they are identified by a String environment
   *                 entry ('myName').
   *
   *                 The ejb-link for the external bean is in the form
   *                 'external.jar#BeanName'.
   *
   *                 Check that we can deploy the application, that the
   *                 referencing bean can lookup the two other ones. Check that
   *                 referenced bean identities (as reported by the String env.
   *                 entry) match the ones specified in the DD.
   */
  public void testScope() throws Fault {
    boolean pass = false;

    try {
      logTrace("[Client] Creating bean...");
      bean = home.create(props, 1, "columbian", 8);
      logTrace("[Client] Checking referenced EJB...");
      pass = bean.testSimpleLinkScope(props);
      bean.remove();

      if (!pass) {
        throw new Fault("[Client] EJB reference scope test failed!");
      }
    } catch (Exception e) {
      logErr("[Client] Caught exception: " + e);
      throw new Fault("[Client] EJB link scope test failed!" + e, e);
    }
  }

  public void cleanup() {
    logTrace("[Client] Cleanup()");
  }

}
