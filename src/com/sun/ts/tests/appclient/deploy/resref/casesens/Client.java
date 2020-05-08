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
 * @(#)Client.java	1.10 03/05/16
 */

package com.sun.ts.tests.appclient.deploy.resref.casesens;

import java.util.Properties;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.tests.assembly.util.shared.resref.casesens.TestCode;
import com.sun.javatest.Status;

public class Client extends EETest {

  private Properties props = null;

  private TSNamingContext nctx = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /**
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   *                     webServerHost; webServerPort; mailuser1;
   */
  public void setup(String[] args, Properties props) throws Fault {
    this.props = props;

    try {
      nctx = new TSNamingContext();
    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  /**
   * @testName: testCaseSensitivity
   *
   * @assertion_ids: JavaEE:SPEC:279
   *
   * @test_Strategy: Deploy an application client declaring two resource
   *                 references whose names differ only by case and are assigned
   *                 to two distinct factory types: a
   *                 jakarta.jms.QueueConnectionFactory and a
   *                 jakarta.jms.TopicConnectionFactory.
   *
   *                 Check that the application client can lookup the two
   *                 factories, cast them to their respective Java types, and
   *                 create a connection (corresponding to the factory type).
   *                 This validates that the resource references were resolved
   *                 correctly.
   */
  public void testCaseSensitivity() throws Fault {
    boolean pass;

    try {
      pass = TestCode.testCaseSensitivity(nctx);
      if (!pass) {
        throw new Fault("casesens res-ref test failed!");
      }
    } catch (Exception e) {
      TestUtil.logErr("[Client] Caught exception: " + e);
      throw new Fault("casesens res-ref test failed!", e);
    }
  }

  public void cleanup() {
    logTrace("[Client] cleanup()");
  }

}
