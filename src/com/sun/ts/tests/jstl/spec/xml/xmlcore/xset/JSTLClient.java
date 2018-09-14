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

package com.sun.ts.tests.jstl.spec.xml.xmlcore.xset;

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

    setContextRoot("/jstl_xml_xset_web");
    setGoldenFileDir("/jstl/spec/xml/xmlcore/xset");

    return super.run(args, out, err);
  }

  /*
   * @testName: positiveSetSelectVarTest
   * 
   * @assertion_ids: JSTL:SPEC:69; JSTL:SPEC:69.1; JSTL:SPEC:69.2;
   * JSTL:SPEC:69.5
   * 
   * @testStrategy: Validate the action properly sets a scoped variable when
   * select is provided a valid XPath expression and the the variable reference
   * by var is available to another action.
   */
  public void positiveSetSelectVarTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveSetSelectVarTest");
    invoke();
  }

  /*
   * @testName: positiveSetScopeTest
   * 
   * @assertion_ids: JSTL:SPEC:69; JSTL:SPEC:69.3; JSTL:SPEC:69.3.1;
   * JSTL:SPEC:69.3.2; JSTL:SPEC:69.3.3; JSTL:SPEC:69.3.4; JSTL:SPEC:69.4;
   * JSTL:SPEC:69.5
   * 
   * @testStrategy: Validate that the presense of the scope attribute properly
   * exports var to the specified scope. Also verify that if scope is not
   * specified, that var is exported to the page scope by default.
   */
  public void positiveSetScopeTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveSetScopeTest");
    invoke();
  }

  /*
   * @testName: negativeSetSelectFailureTest
   * 
   * @assertion_ids: JSTL:SPEC:69; JSTL:SPEC:69.8
   * 
   * @testStrategy: Validate that if the XPath expression fails to evaluate an
   * instance of javax.servet.jsp.JspException is thrown.
   */
  public void negativeSetSelectFailureTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "negativeSetSelectFailureTest");
    invoke();
  }

  /*
   * @testName: negativeSetVarReqAttrTest
   * 
   * @assertion_ids: JSTL:SPEC:69; JSTL:SPEC:69.2.1
   * 
   * @testStrategy: Validate that a fatal translation error occurs if the var
   * attribute is not present.
   */
  public void negativeSetVarReqAttrTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "negativeSetVarReqAttrTest");
    TEST_PROPS.setProperty(REQUEST, "negativeSetVarReqAttrTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeSetSelectReqAttrTest
   * 
   * @assertion_ids: JSTL:SPEC:69; JSTL:SPEC:69.2.1
   * 
   * @testStrategy: Validate that a fatal translation error occurs if the select
   * attribute is not present.
   */
  public void negativeSetSelectReqAttrTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "negativeSetSelectReqAttrTest");
    TEST_PROPS.setProperty(REQUEST, "negativeSetSelectReqAttrTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeSetInvalidScopeTest
   * 
   * @assertion_ids: JSTL:SPEC:69; JSTL:SPEC:69.2.1
   * 
   * @testStrategy: Validate that a fatal translation error occurs if the select
   * attribute is not present.
   */
  public void negativeSetInvalidScopeTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "negativeSetInvalidScopeTest");
    TEST_PROPS.setProperty(REQUEST, "negativeSetInvalidScopeTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

}
