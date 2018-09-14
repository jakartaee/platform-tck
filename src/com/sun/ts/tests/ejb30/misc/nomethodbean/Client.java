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

package com.sun.ts.tests.ejb30.misc.nomethodbean;

import java.io.PrintWriter;
import com.sun.javatest.Status;
import com.sun.ts.tests.servlet.common.client.AbstractUrlClient;

public class Client extends AbstractUrlClient {
  public static final String CONTEXT_ROOT = "/ejb3_misc_nomethodbean_web";

  public static final String SERVLET_NAME = "TestServlet";

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  /**
   * Entry point for same-VM execution. In different-VM execution, the main
   * method delegates to this method.
   */
  public Status run(String args[], PrintWriter out, PrintWriter err) {
    setServletName(SERVLET_NAME);
    setContextRoot(CONTEXT_ROOT);
    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /*
   * @testName: noMethodStateless
   * 
   * @test_Strategy: httpclient -> servlet -> (remote + local)SLSB with no
   * business methods
   */
  public void noMethodStateless() throws Fault {
    TEST_PROPS.setProperty(APITEST, "noMethodStateless");
    invoke();
  }

  /*
   * @testName: noMethodStateful
   * 
   * @test_Strategy: httpclient -> servlet -> (remote + local)SFSB with no
   * business methods
   */
  public void noMethodStateful() throws Fault {
    TEST_PROPS.setProperty(APITEST, "noMethodStateful");
    invoke();
  }
}
