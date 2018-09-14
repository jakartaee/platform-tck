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

package com.sun.ts.tests.jstl.spec.xml.xmlcore.xout;

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

    setContextRoot("/jstl_xml_xout_web");
    setGoldenFileDir("/jstl/spec/xml/xmlcore/xout");

    return super.run(args, out, err);
  }

  /*
   * @testName: positiveOutSelectTest
   * 
   * @assertion_ids: JSTL:SPEC:68; JSTL:SPEC:68.1; JSTL:SPEC:68.6
   * 
   * @testStrategy: Validate that the action properly displays the result of an
   * XPath expression provided to the select attribute.
   */
  public void positiveOutSelectTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveOutSelectTest");
    invoke();
  }

  /*
   * @testName: positiveOutEscXmlTest
   * 
   * @assertion_ids: JSTL:SPEC:68; JSTL:SPEC:68.2; JSTL:SPEC:68.2.1;
   * JSTL:SPEC:68.2.2; JSTL:SPEC:68.2.3; JSTL:SPEC:68.2.4; JSTL:SPEC:68.2.5;
   * JSTL:SPEC:68.2.6
   * 
   * @testStrategy: Validate that the escaping of XML entities (<,>,&,',") will
   * occur if the escapeXml is not present, or the value is true. Also validate
   * that no escaping occurs if the value is false.
   */
  public void positiveOutEscXmlTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveOutEscXmlTest");
    invoke();
  }

  /*
   * @testName: negativeOutSelectFailureTest
   * 
   * @assertion_ids: JSTL:SPEC:68; JSTL:SPEC:68.5; JSTL:SPEC:68.7
   * 
   * @testStrategy: Validate that a javax.servlet.jsp.JspException is thrown if
   * the expression language fails to evaluate the provided XPath expression.
   */
  public void negativeOutSelectFailureTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "negativeOutSelectFailureTest");
    invoke();
  }

  /*
   * @testName: negativeOutSelectReqAttrTest
   * 
   * @assertion_ids: JSTL:SPEC:68; JSTL:SPEC:68.6
   * 
   * @testStrategy: Validate that a fatal translation error is generated if the
   * select attribute is not present in the out action.
   */
  public void negativeOutSelectReqAttrTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "negativeOutSelectReqAttrTest");
    TEST_PROPS.setProperty(REQUEST, "negativeOutSelectReqAttrTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }
}
