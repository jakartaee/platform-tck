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

package com.sun.ts.tests.jstl.spec.etu.tlv.permitted;

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

    setContextRoot("/jstl_etu_tlv_perm_web");
    setGoldenFileDir("/jstl/spec/etu/tlv/permitted");

    return super.run(args, out, err);
  }

  /*
   * @testName: positivePermittedTlvTest
   * 
   * @assertion_ids: JSTL:SPEC:109; JSTL:SPEC:104; JSTL:SPEC:104.1;
   * JSTL:SPEC:104.2; JSTL:SPEC:104.3; JSTL:SPEC:104.4
   * 
   * @testStrategy: Validate that if a URI that refers to a specific set of
   * libraries is specified as a parameter to the PermittedTaglibsTLV, that the
   * use of this library doesn't generate a translation error.
   */
  public void positivePermittedTlvTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "positivePermittedTlvTest");
    TEST_PROPS.setProperty(REQUEST, "positivePermittedTlvTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    invoke();
  }

  /*
   * @testName: negativePermittedTlvTest
   * 
   * @assertion_ids: JSTL:SPEC:109; JSTL:SPEC:104; JSTL:SPEC:104.5
   * 
   * @testStrategy: Validate that if a URI that refers to a specific set of
   * libraries is not specified as a parameter to the PermittedTaglibsTLV, that
   * the use of this library generates a translation error.
   */
  public void negativePermittedTlvTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "negativePermittedTlvTest");
    TEST_PROPS.setProperty(REQUEST, "negativePermittedTlvTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }
}
