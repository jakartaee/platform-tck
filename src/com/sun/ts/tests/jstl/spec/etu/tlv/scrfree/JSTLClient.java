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

package com.sun.ts.tests.jstl.spec.etu.tlv.scrfree;

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

    setContextRoot("/jstl_etu_tlv_scrfree_web");
    setGoldenFileDir("/jstl/spec/etu/tlv/scrfree");

    return super.run(args, out, err);
  }

  /*
   * @testName: positiveScriptFreeTlvNoDeclTest
   * 
   * @assertion_ids: JSTL:SPEC:104; JSTL:SPEC:104.1
   * 
   * @testStrategy: Validate that if the validator specifies JSP declarations
   * aren't allowed, that scriptlets, expressions and RT expressions still work
   * as expected.
   */
  public void positiveScriptFreeTlvNoDeclTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "positiveScriptFreeTlvNoDeclTest");
    TEST_PROPS.setProperty(REQUEST, "positiveScriptFreeTlvNoDeclTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    invoke();
  }

  /*
   * @testName: positiveScriptFreeTlvNoScrTest
   * 
   * @assertion_ids: JSTL:SPEC:104; JSTL:SPEC:104.2
   * 
   * @testStrategy: Validate that if the validator specifies JSP scriptlets
   * aren't allowed, that declarations, expressions and RT expressions still
   * work as expected.
   */
  public void positiveScriptFreeTlvNoScrTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "positiveScriptFreeTlvNoScrTest");
    TEST_PROPS.setProperty(REQUEST, "positiveScriptFreeTlvNoScrTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    invoke();
  }

  /*
   * @testName: positiveScriptFreeTlvNoExprTest
   * 
   * @assertion_ids: JSTL:SPEC:104; JSTL:SPEC:104.3
   * 
   * @testStrategy: Validate that if the validator specifies JSP expressions
   * aren't allowed, that scriptlets, declarations and RT expressions still work
   * as expected.
   */
  public void positiveScriptFreeTlvNoExprTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "positiveScriptFreeTlvNoExprTest");
    TEST_PROPS.setProperty(REQUEST, "positiveScriptFreeTlvNoExprTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    invoke();
  }

  /*
   * @testName: positiveScriptFreeTlvNoRTExprTest
   * 
   * @assertion_ids: JSTL:SPEC:104; JSTL:SPEC:104.4
   * 
   * @testStrategy: Validate that if the validator specifies JSP RT expressions
   * aren't allowed, that declarations, scriptlets, and expressions still work
   * as expected.
   */
  public void positiveScriptFreeTlvNoRTExprTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "positiveScriptFreeTlvNoRTExprTest");
    TEST_PROPS.setProperty(REQUEST, "positiveScriptFreeTlvNoRTExprTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    invoke();
  }

  /*
   * @testName: negativeScriptFreeTlvNoDeclTest
   * 
   * @assertion_ids: JSTL:SPEC:104; JSTL:SPEC:104.1
   * 
   * @testStrategy: Validate that if declarations aren't allowed per the
   * configured validator, that a translation error occurs if a declaration
   * exists.
   */
  public void negativeScriptFreeTlvNoDeclTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "negativeScriptFreeTlvNoDeclTest");
    TEST_PROPS.setProperty(REQUEST, "negativeScriptFreeTlvNoDeclTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeScriptFreeTlvNoScrTest
   * 
   * @assertion_ids: JSTL:SPEC:104; JSTL:SPEC:104.2
   * 
   * @testStrategy: Validate that if scriptlets aren't allowed per the
   * configured validator, that a translation error occurs if a scriptlet
   * exists.
   */
  public void negativeScriptFreeTlvNoScrTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "negativeScriptFreeTlvNoScrTest");
    TEST_PROPS.setProperty(REQUEST, "negativeScriptFreeTlvNoScrTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeScriptFreeTlvNoExprTest
   * 
   * @assertion_ids: JSTL:SPEC:104; JSTL:SPEC:104.3
   * 
   * @testStrategy: Validate that if expressions aren't allowed per the
   * configured validator, that a translation error occurs if an expression
   * exists.
   */
  public void negativeScriptFreeTlvNoExprTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "negativeScriptFreeTlvNoExprTest");
    TEST_PROPS.setProperty(REQUEST, "negativeScriptFreeTlvNoExprTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeScriptFreeTlvNoRTExprTest
   * 
   * @assertion_ids: JSTL:SPEC:104; JSTL:SPEC:104.4
   * 
   * @testStrategy: Validate that if RT expressions aren't allowed per the
   * configured validator, that a translation error occurs if an RT expression
   * exists.
   */
  public void negativeScriptFreeTlvNoRTExprTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "negativeScriptFreeTlvNoRTExprTest");
    TEST_PROPS.setProperty(REQUEST, "negativeScriptFreeTlvNoRTExprTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }
}
