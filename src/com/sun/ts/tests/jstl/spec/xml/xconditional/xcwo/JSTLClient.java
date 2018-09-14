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

package com.sun.ts.tests.jstl.spec.xml.xconditional.xcwo;

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

    setContextRoot("/jstl_xml_xcwo_web");
    setGoldenFileDir("/jstl/spec/xml/xconditional/xcwo");

    return super.run(args, out, err);
  }

  /*
   * @testName: positiveCWOTest
   * 
   * @assertion_ids: JSTL:SPEC:71; JSTL:SPEC:71.3; JSTL:SPEC:71.2
   * 
   * @testStrategy: Validate the following: - The first c:when action that
   * evaluates to true will process it's body content. Subsequent when action
   * that evaluate to true will not be processed. - Validate that if a when
   * action evaluates to true, it's body content is processed and that the body
   * content of an c:otherwise action is not. - Validate that if no c:when
   * action evaluates to true, and no c:otherwise action is present, nothing is
   * written to the current JspWriter. - Validate that if no c:when action
   * evaluates to true, and an c:otherwise action is present, the body content
   * of the action is processed.
   */
  public void positiveCWOTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveCWOTest");
    invoke();
  }

  /*
   * @testName: positiveCWOWhiteSpaceTest
   * 
   * @assertion_ids: JSTL:SPEC:71.5; JSTL:SPEC:71.5.5; JSTL:SPEC:71.5.4
   * 
   * @testStrategy: Validate that the CWO action behaves as expected if
   * whitespace is intermixed with choose's nested when and otherwise actions.
   * No translation error should occur.
   */
  public void positiveCWOWhiteSpaceTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveCWOWhiteSpaceTest");
    invoke();
  }

  /*
   * @testName: negativeCWONoWhenActionsTest
   * 
   * @assertion_ids: JSTL:SPEC:71; JSTL:SPEC:71.6.4
   * 
   * @testStrategy: Validate that a fatal translation error occurs if the choose
   * action has no nested when actions.
   */
  public void negativeCWONoWhenActionsTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "negativeCWONoWhenActionsTest");
    TEST_PROPS.setProperty(REQUEST, "negativeCWONoWhenActionsTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeCWOWhenBeforeOtherwiseTest
   * 
   * @assertion_ids: JSTL:SPEC:71; JSTL:SPEC:71.7
   * 
   * @testStrategy: Validate that a fatal translation error occurs if otherwise
   * appears before when or if when appears after otherwise.
   */
  public void negativeCWOWhenBeforeOtherwiseTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "negativeCWOWhenBeforeOtherwiseTest");
    TEST_PROPS.setProperty(REQUEST, "negativeCWOWhenBeforeOtherwiseTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeCWOWhenNoParentTest
   * 
   * @assertion_ids: JSTL:SPEC:71; JSTL:SPEC:71.5.6
   * 
   * @testStrategy: Validate a fatal translation error occurs if a when action
   * has no choose as an immediate parent.
   */
  public void negativeCWOWhenNoParentTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "negativeCWOWhenNoParentTest");
    TEST_PROPS.setProperty(REQUEST, "negativeCWOWhenNoParentTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeCWOOtherwiseNoParentTest
   * 
   * @assertion_ids: JSTL:SPEC:71; JSTL:SPEC:71.5.6
   * 
   * @testStrategy: Validate a fatal translation error occurs if an otherwise
   * action has no choose as an immediate parent.
   */
  public void negativeCWOOtherwiseNoParentTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "negativeCWOOtherwiseNoParentTest");
    TEST_PROPS.setProperty(REQUEST, "negativeCWOOtherwiseNoParentTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeCWOWhenSelectReqAttrTest
   * 
   * @assertion_ids: JSTL:SPEC:71; JSTL:SPEC:71.5.6
   * 
   * @testStrategy: Validate that a fatal translation error occurs if a nested
   * when action has no select attribute.
   */
  public void negativeCWOWhenSelectReqAttrTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "negativeCWOWhenSelectReqAttrTest");
    TEST_PROPS.setProperty(REQUEST, "negativeCWOWhenSelectReqAttrTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeCWOWhenSelectFailureTest
   * 
   * @assertion_ids: JSTL:SPEC:71; JSTL:SPEC:71.5.6
   * 
   * @testStrategy: Validate that if the XPath expression provided to select of
   * the when action fails to evaluated an instance of
   * javax.servlet.jsp.JspException is thrown.
   */
  public void negativeCWOWhenSelectFailureTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "negativeCWOWhenSelectFailureTest");
    invoke();
  }
}
