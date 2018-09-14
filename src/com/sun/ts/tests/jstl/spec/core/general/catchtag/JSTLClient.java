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
 * $URL$ $LastChangedDate$
 */

package com.sun.ts.tests.jstl.spec.core.general.catchtag;

import java.io.PrintWriter;
import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.jstl.common.client.AbstractUrlClient;

public class JSTLClient extends AbstractUrlClient {

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /** Creates new JSTLClient */
  public JSTLClient() {
  }

  /*
   * public methods
   * ========================================================================
   */

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    JSTLClient theTests = new JSTLClient();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  /**
   * Entry point for same-VM execution. In different-VM execution, the main
   * method delegates to this method.
   */
  public Status run(String args[], PrintWriter out, PrintWriter err) {

    setContextRoot("/jstl_core_gen_catch_web");
    setGoldenFileDir("/jstl/spec/core/general/catchtag");

    return super.run(args, out, err);
  }

  /*
   * @testName: positiveCatchTest
   * 
   * @assertion_ids: JSTL:SPEC:42.3
   * 
   * @testStrategy: Validate that the catch action, with no var attribute
   * specified, will catch the Throwable and allow the page to continue
   * processing.
   */
  public void positiveCatchTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveCatchTest");
    invoke();
  }

  /*
   * @testName: positiveCatchVarTest
   * 
   * @assertion_ids: JSTL:SPEC:42.1
   * 
   * @testStrategy: Validate that the catch action properly stores the Throable
   * into the variable name designated by the var attribute and validate the
   * type of var as it should be the type of the Throwable.
   */
  public void positiveCatchVarTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveCatchVarTest");
    invoke();
  }
}
