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
 * %W %E
 */

package com.sun.ts.tests.ejb.ee.deploy.session.stateful.compat13_14;

import java.util.Properties;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.javatest.Status;

public class Client extends EETest {

  /** JNDI Name we use to lookup the bean */
  public static final String lookupName = "java:comp/env/ejb/TestBean";

  private TSNamingContext nctx = null;

  private TestBeanHome home;

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
  public void setup(String[] args, Properties props) throws Fault {
    this.props = props;

    try {
      logMsg("[Client] setup(): getting Naming Context...");
      nctx = new TSNamingContext();

      logTrace("[Client] Looking up bean Home...");
      home = (TestBeanHome) nctx.lookup(lookupName, TestBeanHome.class);

      logMsg("[Client] Setup OK!");
    } catch (Exception e) {
      throw new Fault("[Client] Setup failed: " + e, e);
    }
  }

  /**
   * @testName: test13DD
   *
   * @assertion_ids: JavaEE:SPEC:283; EJB:SPEC:871
   *
   * @test_Strategy: Package an ejb-jar file using a J2EE 1.3 DD
   *
   *                 Package an .ear file (J2EE 1.4 DD's) including this ejb-jar
   *                 and an application client (J2EE 1.4 DD's). This application
   *                 client references a Stateful Session bean in this ejb-jar
   *                 module.
   *
   *                 Deploy the .ear file.
   *
   *                 Run the client and check that we can call a business method
   *                 on the referenced bean at runtime.
   */
  public void test13DD() throws Fault {
    TestBean bean;
    boolean pass;

    try {
      logTrace("[Client] Creating bean instance...");
      bean = home.create(props);

      logTrace("[Client] Calling bean...");
      pass = bean.ping();
      if (!pass) {
        throw new Fault("Stateful compat13_14 test failed");
      }
    } catch (Exception e) {
      throw new Fault("Stateful compat13_14 test failed: " + e, e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("[Client] cleanup()");
  }

}
