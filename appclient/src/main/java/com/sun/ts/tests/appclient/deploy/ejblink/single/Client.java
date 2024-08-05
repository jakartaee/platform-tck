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
 * @(#)Client.java	1.15 03/05/16
 */

package com.sun.ts.tests.appclient.deploy.ejblink.single;

import java.util.Properties;

import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.assembly.util.shared.ejbref.single.TestCode;

public class Client extends EETest {

  /*
   * Global variables.
   */
  private TSNamingContext nctx = null;

  private Properties props = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /**
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   *                     generateSQL;
   *
   * @class.testArgs: -ap tssql.stmt
   *
   */
  public void setup(String[] args, Properties props) throws Exception {

    try {
      this.props = props;

      logTrace("[Client] Getting naming context...");
      nctx = new TSNamingContext();

      logMsg("[Client] Setup succeed (got naming context).");
    } catch (Exception e) {
      throw new Exception("Setup failed:", e);
    }
  }

  /**
   * @testName: testStateless
   *
   * @assertion_ids: JavaEE:SPEC:10118
   *
   * @test_Strategy: Deploy an application client referencing a Stateless
   *                 Session bean. Check at runtime that the application client
   *                 can do a lookup for the EJB reference and use it to create
   *                 a bean. Then invoke on that bean instance a business method
   *                 to be found only in this particular bean: This is to check
   *                 that the EJB reference was resolved consistently with the
   *                 DD.
   */
  public void testStateless() throws Exception {
    boolean pass;

    try {
      pass = TestCode.testStatelessExternal(nctx, props);
      if (!pass) {
        throw new Exception("ejb-link test failed!");
      }
    } catch (Exception e) {
      throw new Exception("ejb-link test failed: " + e, e);
    }
  }

  /**
   * @testName: testStateful
   *
   * @assertion_ids: JavaEE:SPEC:10118
   *
   * @test_Strategy: Deploy an application client referencing a Stateful Session
   *                 bean. Check at runtime that the application client can do a
   *                 lookup for the EJB reference and use it to create a bean.
   *                 Then invoke on that bean instance a business method to be
   *                 found only in this particular bean: This is to check that
   *                 the EJB reference was resolved consistently with the DD.
   */
  public void testStateful() throws Exception {
    boolean pass;

    try {
      pass = TestCode.testStatefulExternal(nctx, props);
      if (!pass) {
        throw new Exception("ejb-link test failed!");
      }
    } catch (Exception e) {
      throw new Exception("ejb-link test failed: " + e, e);
    }
  }

  public void cleanup() throws Exception {
    logMsg("[Client] cleanup()");
  }

}
