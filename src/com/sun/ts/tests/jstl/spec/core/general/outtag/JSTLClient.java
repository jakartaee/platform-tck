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

package com.sun.ts.tests.jstl.spec.core.general.outtag;

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

    setContextRoot("/jstl_core_gen_out_web");
    setGoldenFileDir("/jstl/spec/core/general/outtag");

    return super.run(args, out, err);
  }

  /*
   * @testName: positiveOutValueAttributeTest
   * 
   * @assertion_ids: JSTL:SPEC:12.1; JSTL:SPEC:12.1.1
   * 
   * @testStrategy: Validate the the 'value' attribute of the out action can
   * accept both EL and static values.
   */
  public void positiveOutValueAttributeTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveOutValueAttributeTest");
    invoke();
  }

  /*
   * @testName: positiveOutDefaultAttributeTest
   * 
   * @assertion_ids: JSTL:SPEC:12.3; JSTL:SPEC:12.3.1
   * 
   * @testStrategy: Validate that the 'default' attribute of the out action can
   * accept both EL and static values.
   */
  public void positiveOutDefaultAttributeTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveOutDefaultAttributeTest");
    invoke();
  }

  /*
   * @testName: positiveOutBodyBehaviorTest
   * 
   * @assertion_ids: JSTL:SPEC:12.5
   * 
   * @testStrategy: Validate the default value returned in case of an expression
   * failure, or a null value returned, can be specified in the body of the
   * action.
   *
   */
  public void positiveOutBodyBehaviorTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveOutBodyBehaviorTest");
    invoke();
  }

  /*
   * @testName: positiveOutEscXmlDefaultTest
   * 
   * @assertion_ids: JSTL:SPEC:12.2.1; JSTL:SPEC:12.2.10
   * 
   * @testStrategy: Validate that if escapeXml is not specified, the escaping of
   * <, >, ', ", & will be performed by default.
   */
  public void positiveOutEscXmlDefaultTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveOutEscXmlDefaultTest");
    invoke();
  }

  /*
   * @testName: positiveOutEscXmlTest
   * 
   * @assertion_ids: JSTL:SPEC:12.2.2; JSTL:SPEC:12.2.3; JSTL:SPEC:12.2.5;
   * JSTL:SPEC:12.2.6; JSTL:SPEC:12.2.7; JSTL:SPEC:12.2.8; JSTL:SPEC:12.2.9
   * 
   * @testStrategy: Validate that escapeXml behaves as specified when the
   * setting is true or false. If true, <, >, ', ", and & will be escaped, and
   * if false, no escaping is performed.
   */
  public void positiveOutEscXmlTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveOutEscXmlTest");
    invoke();
  }

  /*
   * @testName: negativeOutBodyContentExcTest
   * 
   * @assertion_ids: JSTL:SPEC:12.7
   * 
   * @testStrategy: Validate that an exception caused by the body content is
   * properly propagated and not handled by the action.
   */
  public void negativeOutBodyContentExcTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "negativeOutBodyContentExcTest");
    invoke();
  }

  /*
   * @testName: positiveOutReaderTest
   * 
   * @assertion_ids: JSTL:SPEC:12.1.4
   * 
   * @test_Strategy: Validate that if a java.io.Reader object is provided to the
   * value attribute of the out tag, that the contents of the reader are emitted
   * to the current JspWriter object.
   */
  public void positiveOutReaderTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_core_gen_out_web/positiveOutReaderTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }
}
