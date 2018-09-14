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
 * @(#)Client.java	1.14 03/05/16
 */

package com.sun.ts.tests.appclient.deploy.enventry.casesens;

import java.util.Properties;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.tests.assembly.util.shared.enventry.casesens.TestCode;
import com.sun.javatest.Status;

public class Client extends EETest {

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
  public void setup(String[] args, Properties props) throws Fault {
    this.props = props;

    try {
      nctx = new TSNamingContext();
      logMsg("[Client] Setup succeed (got naming context).");
    } catch (Exception e) {
      throw new Fault("[Client] Setup failed:", e);
    }
  }

  /**
   * @testName: testCaseSensitivity
   *
   * @assertion_ids: JavaEE:SPEC:279
   *
   * @test_Strategy: Deploy an application client with two String environment
   *                 entries whose name differ only by case and are assigned to
   *                 two distinct values. Check that we can lookup the two
   *                 environment entries. Check that their runtime values are
   *                 distinct and match the ones specified in the DD.
   */
  public void testCaseSensitivity() throws Fault {
    boolean pass;

    try {
      pass = TestCode.testCaseSensitivity(nctx);
      if (!pass) {
        throw new Fault("Case sensitivity test failed!");
      }
    } catch (Exception e) {
      throw new Fault("Case sensitivity test failed: " + e, e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("[Client] cleanup()");
  }

}
