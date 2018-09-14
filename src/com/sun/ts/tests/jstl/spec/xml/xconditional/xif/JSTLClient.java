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

package com.sun.ts.tests.jstl.spec.xml.xconditional.xif;

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

    setContextRoot("/jstl_xml_xif_web");
    setGoldenFileDir("/jstl/spec/xml/xconditional/xif");

    return super.run(args, out, err);
  }

  /*
   * @testName: positiveIfSelectTest
   * 
   * @assertion_ids: JSTL:SPEC:70; JSTL:SPEC:70.1
   * 
   * @testStrategy: Validate that, providing a valid XPath expression to the
   * select attribute, will, depending on the result cause the <x:if> action to
   * process its body content.
   */
  public void positiveIfSelectTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveIfSelectTest");
    invoke();
  }

  /*
   * @testName: positiveIfVarTest
   * 
   * @assertion_ids: JSTL:SPEC:70; JSTL:SPEC:70.2
   * 
   * @testStrategy: Validate that the following: - if var is present, the
   * Boolean result of the XPath evaluation is exported and available using the
   * name provided to var. - if var is present, and the action has a body, the
   * body is still processed.
   */
  public void positiveIfVarTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveIfVarTest");
    invoke();
  }

  /*
   * @testName: positiveIfScopeTest
   * 
   * @assertion_ids: JSTL:SPEC:70; JSTL:SPEC:70.3; JSTL:SPEC:70.3.1;
   * JSTL:SPEC:70.4
   * 
   * @testStrategy: Validate that var is exported to the scope as specified by
   * the scope attribute. If scope is not specified, var will be exported to the
   * page scope by default.
   */
  public void positiveIfScopeTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveIfScopeTest");
    invoke();
  }

  /*
   * @testName: negativeIfInvalidScopeTest
   * 
   * @assertion_ids: JSTL:SPEC:70; JSTL:SPEC:70.3; JSTL:SPEC:70.3.5
   * 
   * @testStrategy: Validate that if scope is provided an invalid value, a fatal
   * translation error occurs.
   */
  public void negativeIfInvalidScopeTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "negativeIfInvalidScopeTest");
    TEST_PROPS.setProperty(REQUEST, "negativeIfInvalidScopeTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeIfSelectReqAttrTest
   * 
   * @assertion_ids: JSTL:SPEC:70; JSTL:SPEC:70.6; JSTL:SPEC:70.7
   * 
   * @testStrategy: Validate that 'select' is indeed a required attribute by
   * having an <x:if> action with no select. A fatal translation error should
   * occur.
   */
  public void negativeIfSelectReqAttrTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "negativeIfSelectReqAttrTest");
    TEST_PROPS.setProperty(REQUEST, "negativeIfSelectReqAttrTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeIfScopeVarTest
   * 
   * @assertion_ids: JSTL:SPEC:70; JSTL:SPEC:70.4; JSTL:SPEC:70.7
   * 
   * @testStrategy: Validate a fatal translation error occurs if scope is
   * specified by an action, but var is not.
   */
  public void negativeIfScopeVarTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "negativeIfScopeVarTest");
    TEST_PROPS.setProperty(REQUEST, "negativeIfScopeVarTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeIfSelectFailureTest
   * 
   * @assertion_ids: JSTL:SPEC:70; JSTL:SPEC:70.5
   * 
   * @testStrategy: Validate that an instance of javax.servlet.jsp.JspException
   * is thrown if the XPath expression provided to the select attribute fails to
   * evaluate.
   */
  public void negativeIfSelectFailureTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "negativeIfSelectFailureTest");
    invoke();
  }
}
