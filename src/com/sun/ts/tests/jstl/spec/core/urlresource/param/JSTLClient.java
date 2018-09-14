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

package com.sun.ts.tests.jstl.spec.core.urlresource.param;

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

    setContextRoot("/jstl_core_url_param_web");
    setGoldenFileDir("/jstl/spec/core/urlresource/param");

    return super.run(args, out, err);
  }

  /*
   * @testName: positiveParamNameValueTest
   * 
   * @assertion_ids: JSTL:SPEC:25; JSTL:SPEC:25.1; JSTL:SPEC:25.1.1;
   * JSTL:SPEC:25.2; JSTL:SPEC:25.2.1
   * 
   * @testStrategy: Validate the the name and value attributes can accept both
   * dynamic and static values.
   */
  public void positiveParamNameValueTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveParamNameValueTest");
    invoke();
  }

  /*
   * @testName: positiveParamEncodingTest
   * 
   * @assertion_ids: JSTL:SPEC:25.4
   * 
   * @testStrategy: Validate that the action properly encodes parameter names
   * and values if it contains characters that require URL encoding.
   */
  public void positiveParamEncodingTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveParamEncodingTest");
    invoke();
  }

  /*
   * @testName: positiveParamBodyValueTest
   * 
   * @assertion_ids: JSTL:SPEC:25.5
   * 
   * @testStrategy: Validate the the param tag can accept a parameter value as
   * body content.
   */
  public void positiveParamBodyValueTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveParamBodyValueTest");
    invoke();
  }

  /*
   * @testName: positiveParamAggregationTest
   * 
   * @assertion_ids: JSTL:SPEC:25.7.1
   * 
   * @testStrategy: Validate that request parameters are properly aggregated if
   * the parent action's URL contains the same parameter name as the name
   * specified in the param tag. The value in the param tag should take
   * precedence.
   */
  public void positiveParamAggregationTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveParamAggregationTest");
    invoke();
  }

  /*
   * @testName: positiveParamNameNullEmptyTest
   * 
   * @assertion_ids: JSTL:SPEC:25.8
   * 
   * @testStrategy: Validate that if name is null or empty, that the action is
   * effectively ignored.
   */
  public void positiveParamNameNullEmptyTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveParamNameNullEmptyTest");
    invoke();
  }

  /*
   * @testName: positiveParamValueNullTest
   * 
   * @assertion_ids: JSTL:SPEC:25.9
   * 
   * @testStrategy: Validate that if value is null, that the value of the param
   * is an empty string ("").
   */
  public void positiveParamValueNullTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveParamValueNullTest");
    invoke();
  }

  /*
   * @testName: negativeParamExcBodyContentTest
   * 
   * @assertion_ids: JSTL:SPEC:25.10
   * 
   * @testStrategy: Validate that if the body content of the action causes an
   * exception, that it is properly propagated.
   */
  public void negativeParamExcBodyContentTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "negativeParamExcBodyContentTest");
    invoke();
  }
}
