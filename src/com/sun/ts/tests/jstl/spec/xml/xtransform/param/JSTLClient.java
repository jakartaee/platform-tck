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

package com.sun.ts.tests.jstl.spec.xml.xtransform.param;

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

    setContextRoot("/jstl_xml_xformparam_web");
    setGoldenFileDir("/jstl/spec/xml/xtransform/param");

    return super.run(args, out, err);
  }

  /*
   * @testName: positiveXParamNameTest
   * 
   * @assertion_ids: JSTL:SPEC:74; JSTL:SPEC:74.1; JSTL:SPEC:74.1.1
   * 
   * @testStrategy: Validate the name attribute of the x:param action is able to
   * accept both static and dynamic values.
   */
  public void positiveXParamNameTest() throws Fault {
    // TEST_PROPS.setProperty(STANDARD, "positiveXParamNameTest");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_xml_xformparam_web/positiveXParamNameTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "10pt|Param properly used");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "REPLACE");
    invoke();
  }

  /*
   * @testName: positiveXParamValueTest
   * 
   * @assertion_ids: JSTL:SPEC:74; JSTL:SPEC:74.2; JSTL:SPEC:74.2.1
   * 
   * @testStrategy: Validate the value attribute of the x:param action is able
   * to accept both static and dynamic values.
   */
  public void positiveXParamValueTest() throws Fault {
    // TEST_PROPS.setProperty(STANDARD, "positiveXParamValueTest");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_xml_xformparam_web/positiveXParamValueTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "13pt|Param properly used");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "REPLACE");
    invoke();
  }

  /*
   * @testName: positiveXParamBodyValueTest
   * 
   * @assertion_ids: JSTL:SPEC:74; JSTL:SPEC:74.3
   * 
   * @testStrategy: Validate the value of the param can be provided as body
   * content to the action.
   */
  public void positiveXParamBodyValueTest() throws Fault {
    // TEST_PROPS.setProperty(STANDARD, "positiveXParamBodyValueTest");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_xml_xformparam_web/positiveXParamBodyValueTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "8pt|Param properly used");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "REPLACE");
    invoke();
  }

}
