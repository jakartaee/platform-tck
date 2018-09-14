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

package com.sun.ts.tests.jstl.spec.etu.uri;

import java.io.PrintWriter;
import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.jstl.common.client.AbstractUrlClient;

public class JSTLClient extends AbstractUrlClient {

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /** Creates new URLClient */
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

    setContextRoot("/jstl_etu_uri_web");

    return super.run(args, out, err);
  }

  /*
   * @testName: positiveJSTLURITest
   * 
   * @assertion_ids: JSTL:SPEC:1; JSTL:SPEC:2; JSTL:SPEC:3; JSTL:SPEC:4;
   * JSTL:SPEC:16; JSTL:SPEC:17; JSTL:SPEC:18; JSTL:SPEC:19
   * 
   * @testStrategy: Import all defined taglib URI definitions for both EL and RT
   * tags. If defined correctly, a fatal translation error should not occur (
   * per section 7.3.6.2 of the JavaServer Pages 1.2 Specification.
   */
  public void positiveJSTLURITest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "positiveJSTLURITest");
    TEST_PROPS.setProperty(REQUEST, "positiveJSTLURITest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    invoke();
  }
}
