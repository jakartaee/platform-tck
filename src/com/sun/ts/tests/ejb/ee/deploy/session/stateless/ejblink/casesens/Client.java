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

package com.sun.ts.tests.ejb.ee.deploy.session.stateless.ejblink.casesens;

import java.util.Properties;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.TSNamingContext;
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

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   *
   */
  public void setup(String[] args, Properties props) throws Fault {
    this.props = props;

    try {
      logTrace("[Client] Getting naming context...");
      nctx = new TSNamingContext();
      logTrace("[Client] Looking up home...");
      home = (TestBeanHome) nctx.lookup(beanName, TestBeanHome.class);
      logMsg("[Client] Setup OK!");
    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  /**
   * @testName: testCaseSensitivity
   *
   * @assertion_ids: EJB:SPEC:872
   *
   * @test_Strategy: Have two Stateless Session beans whose ejb-name's differ
   *                 only by case and whose identity is defined by a String
   *                 environment entry ('myName').
   *
   *                 Another Stateless Session bean (TestBean) references these
   *                 two beans using ejb-link elements. Check that TestBean can
   *                 lookup the two beans and check each bean identity based on
   *                 their value for the 'myName' environment entry. Check that
   *                 this identity correspond to the references specified in the
   *                 DD (validates that the EJB references were resolved
   *                 correctly).
   */
  public void testCaseSensitivity() throws Fault {

    boolean pass = true;
    TestBean bean = null;

    try {
      logTrace("[Client] creating TestBean instance...");
      bean = home.create();
      bean.initLogging(props);
      logTrace("[Client] Calling TestBean...");
      pass = bean.testCaseSensitivity(props);
      logTrace("[Client] Removing TestBean...");
      bean.remove();

      if (!pass) {
        throw new Fault("ejb-link casesens test failed");
      }
    } catch (Exception e) {
      throw new Fault("ejb-link casesens test failed: ", e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("[Client] cleanup");
  }
}
