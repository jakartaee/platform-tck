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
 * @(#)Client.java	1.12 03/05/16
 */

package com.sun.ts.tests.appclient.deploy.enventry.scope;

import java.util.Properties;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.tests.assembly.util.shared.enventry.scope.TestCode;
import com.sun.javatest.Status;

public class Client extends EETest {

  /** Env. entry name for JNDI lookup */
  private static final String entryName = "Duende";

  /** Reference value for the env. entry (as specified in DD). */
  private static final String entryRef = "Paco de Lucia";

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
   * @testName: testScope
   *
   * @assertion_ids: JavaEE:SPEC:102
   *
   * @test_Strategy: We package in the same .ear file 2 application clients
   *                 (_client and _another_client). Both use the same
   *                 env-entry-name to declare two distinct String environment
   *                 entry values.
   *
   *                 We check that: - We can deploy the application. - One of
   *                 the application clients (_client) can be run and can lookup
   *                 its String environment entry. - The runtime value of this
   *                 entry correspond to the one declared in the Deployment
   *                 Descriptor.
   */
  public void testScope() throws Fault {
    boolean pass;

    try {
      pass = TestCode.checkEntry(nctx, entryName, entryRef);
      if (!pass) {
        throw new Fault("Env entry scope test failed!");
      }
    } catch (Exception e) {
      throw new Fault("Env entry scope test failed: " + e, e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("[Client] cleanup()");
  }
}
