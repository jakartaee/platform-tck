/*
 * Copyright (c) 2007, 2024 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.appclient.deploy.compat9_10;

import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;

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
  public void setup(String[] args, Properties props) throws Exception {

    try {
      this.props = props;
      nctx = new TSNamingContext();
      logMsg("[Client] Setup succeed (got naming context).");
    } catch (Exception e) {
      throw new Exception("Setup failed:", e);
    }
  }

  /**
   * @testName: test910DD
   *
   * @assertion_ids: JavaEE:SPEC:283; JavaEE:SPEC:284; JavaEE:SPEC:10109;
   *                 JavaEE:SPEC:10110; JavaEE:SPEC:10111
   *
   * @test_Strategy: Package an ejb-jar file using a Jakarta EE 10.0 DD
   *
   *                 Package an .ear file (Jakarta EE 10.0 DD's) including this
   *                 ejb-jar and an application client (Jakarta EE 9.0 DD's). This
   *                 application client references a bean in this ejb-jar
   *                 module.
   *
   *                 Deploy the .ear file.
   *
   *                 Run the client and check we can call a business method on
   *                 the referenced bean at runtime.
   */
  public void test910DD() throws Exception {
    TestBean bean = null;
    String beanName;
    boolean pass = false;

    try {
      TestUtil.logTrace("[Client] Looking up '" + beanLookup + "'...");
      bean = (TestBean) nctx.lookup(beanLookup, TestBean.class);
      bean.initLogging(props);
      pass = bean.ping();

      if (!pass) {
        throw new Exception("appclient compat9_10 test failed!");
      }
    } catch (Exception e) {
      TestUtil.logErr("appclient compat9_10 test failed: " + e);
      throw new Exception("appclient compat9_10 test failed: ", e);
    }
  }

  public void cleanup() throws Exception {
    logMsg("[Client] cleanup()");
  }

}
