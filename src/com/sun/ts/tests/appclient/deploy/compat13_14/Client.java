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

package com.sun.ts.tests.appclient.deploy.compat13_14;

import java.util.Properties;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.tests.assembly.util.refbean.*;
import com.sun.javatest.Status;

public class Client extends EETest {

  private static final String prefix = "java:comp/env/ejb/";

  /** Bean lookup */
  private static final String beanLookup = prefix + "TestBean";

  private TSNamingContext nctx = null;

  private Properties props = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /**
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   */
  public void setup(String[] args, Properties props) throws Fault {

    try {
      this.props = props;
      nctx = new TSNamingContext();
      logMsg("[Client] Setup succeed (got naming context).");
    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  /**
   * @testName: test13DD
   *
   * @assertion_ids: JavaEE:SPEC:283; JavaEE:SPEC:284
   *
   * @test_Strategy: Package an ejb-jar file using a J2EE 1.4 DD
   *
   *                 Package an .ear file (J2EE 1.4 DD's) including this ejb-jar
   *                 and an application client (J2EE 1.3 DD's). This application
   *                 client references a bean in this ejb-jar module.
   *
   *                 Deploy the .ear file.
   *
   *                 Run the client and check we can call a business method on
   *                 the referenced bean at runtime.
   */
  public void test13DD() throws Fault {
    TestBeanHome home = null;
    TestBean bean = null;
    String beanName;
    boolean pass = false;

    try {
      TestUtil.logTrace("[Client] Looking up '" + beanLookup + "'...");
      home = (TestBeanHome) nctx.lookup(beanLookup, TestBeanHome.class);
      bean = home.create();
      bean.initLogging(props);
      pass = bean.ping();

      if (!pass) {
        throw new Fault("appclient compat13_14 test failed!");
      }
    } catch (Exception e) {
      TestUtil.logErr("appclient compat13_14 test failed: " + e);
      throw new Fault("appclient compat13_14 test failed: ", e);
    } finally {
      try {
        if (null != bean) {
          TestUtil.logTrace("[Client] Removing bean...");
          bean.remove();
        }
      } catch (Exception e) {
        TestUtil.logErr("[Client] Ignoring Exception on " + "bean remove: " + e,
            e);
      }
    }
  }

  public void cleanup() throws Fault {
    logMsg("[Client] cleanup()");
  }

}
