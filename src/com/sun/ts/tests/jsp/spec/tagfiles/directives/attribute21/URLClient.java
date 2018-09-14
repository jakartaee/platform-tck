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
 * $Id$
 */

package com.sun.ts.tests.jsp.spec.tagfiles.directives.attribute21;

import java.io.PrintWriter;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

public class URLClient extends AbstractUrlClient {
  private static final String CONTEXT_ROOT = "/jsp_tagfile_directives_attribute21_web";

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    URLClient theTests = new URLClient();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  /**
   * Entry point for same-VM execution. In different-VM execution, the main
   * method delegates to this method.
   */
  public Status run(String args[], PrintWriter out, PrintWriter err) {

    setContextRoot(CONTEXT_ROOT);

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: onlyOneOfDeferredValueOrDeferredMethodTest
   * 
   * @assertion_ids: JSP:SPEC:230.8.1
   * 
   * @test_Strategy: [OnlyOneOfDeferredValueOrMethod] A translation error must
   * result when both deferredValue and deferredMethod appear in the same tag.
   */

  public void onlyOneOfDeferredValueOrDeferredMethodTest() throws Fault {
    String testName = "onlyOneOfDeferredValueOrDeferredMethod";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: defaultDeferredValue1Test
   * 
   * @assertion_ids: JSP:SPEC:230.8.2
   * 
   * @test_Strategy: [DefaultDeferredValue] Specify an attribute tag with no
   * deferredValue attribute and a deferredValueType attribute. Verify that the
   * attribute's value represents a deferred value expression.
   */
  public void defaultDeferredValue1Test() throws Fault {
    String testName = "defaultDeferredValue1";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: defaultDeferredValue2Test
   * 
   * @assertion_ids: JSP:SPEC:230.8.2
   * 
   * @test_Strategy: [DefaultDeferredValue] Specify an attribute tag with no
   * deferredValue attribute and no deferredValueType attribute. Verify that the
   * attribute's value does not represent a deferred value expression by
   * generating a translation error when '#{' is used.
   */
  public void defaultDeferredValue2Test() throws Fault {
    String testName = "defaultDeferredValue2";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: deferredValueTypeNotSpecifiedTest
   * 
   * @assertion_ids: JSP:SPEC:230.9.1
   * 
   * @test_Strategy: [DeferredValueTypeNotSpecified] Specify an attribute tag
   * with a deferredValue attribute and no deferredValueType attribute. Verify
   * that the type resulting from the evaluation of the attribute's value
   * expression is java.lang.Object.
   */
  public void deferredValueTypeNotSpecifiedTest() throws Fault {
    String testName = "deferredValueTypeNotSpecified";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: bothDeferredValueTypeAndDeferredValueTest
   * 
   * @assertion_ids: JSP:SPEC:230.9.2
   * 
   * @test_Strategy: [BothDeferredValueTypeAndDeferredValue] A translation error
   * must result when both deferredValueType and and deferredValue appear in the
   * same tag where deferredValue is set to false.
   */
  public void bothDeferredValueTypeAndDeferredValueTest() throws Fault {
    String testName = "bothDeferredValueTypeAndDeferredValue";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: defaultDeferredMethod1Test
   * 
   * @assertion_ids: JSP:SPEC:230.10.1
   * 
   * @test_Strategy: [DefaultDeferredMethod] Specify an attribute tag with no
   * deferredMethod attribute and a deferredMethodSignature attribute. Verify
   * that the attribute's value represents a deferred method expression.
   */
  public void defaultDeferredMethod1Test() throws Fault {
    String testName = "defaultDeferredMethod1";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: defaultDeferredMethod2Test
   * 
   * @assertion_ids: JSP:SPEC:230.10.1
   * 
   * @test_Strategy: [DefaultDeferredMethod] Specify an attribute tag with no
   * deferredMethod attribute and no deferredMethodSignature attribute. Verify
   * that the attribute's value does not represent a deferred method expression
   * by generating a translation error when '#{' is used.
   */
  public void defaultDeferredMethod2Test() throws Fault {
    String testName = "defaultDeferredMethod2";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: bothDeferredMethodAndSignatureTest
   * 
   * @assertion_ids: JSP:SPEC:230.11.1
   * 
   * @test_Strategy: [BothDeferredMethodAndSignature] A translation error must
   * result when both deferredMethodSignature and and deferredMethod appear in
   * the same tag where deferredMethod is set to false.
   */
  public void bothDeferredMethodAndSignatureTest() throws Fault {
    String testName = "bothDeferredMethodAndSignature";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: defaultDeferredMethodSignatureTest
   * 
   * @assertion_ids: JSP:SPEC:230.11.2
   * 
   * @test_Strategy: [DefaultDeferredMethodSignature] Specify an attribute tag
   * with a deferredMethod attribute set to true and no
   * deferredMethodMethodSignature attribute. Verify that the method signature
   * defaults to void methodname().
   */
  public void defaultDeferredMethodSignatureTest() throws Fault {
    String testName = "defaultDeferredMethodSignature";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

}
