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

package com.sun.ts.tests.jstl.spec.core.urlresource.url;

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

    setContextRoot("/jstl_core_url_web");
    setGoldenFileDir("/jstl/spec/core/urlresource/url");

    return super.run(args, out, err);
  }

  /*
   * @testName: positiveUrlDisplayUrlTest
   * 
   * @assertion_ids: JSTL:SPEC:24.7
   * 
   * @testStrategy: Validate that if var is not specified, the resulting value
   * of the url action is written to the current JspWriter.
   */
  public void positiveUrlDisplayUrlTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "positiveUrlDisplayUrlTest");
    TEST_PROPS.setProperty(REQUEST, "positiveUrlDisplayUrlTest.jsp");
    TEST_PROPS.setProperty(SEARCH_STRING, "/jstl_core_url_web/jstl");
    invoke();
  }

  /*
   * @testName: positiveUrlValueVarTest
   * 
   * @assertion_ids: JSTL:SPEC:24.2; JSTL:SPEC:24.6
   * 
   * @testStrategy: Validate that the result of encoding the value of the url
   * attribute is properly associated with a variable designated by var. Compare
   * the result with that returned by response.encodeUrl().
   */
  public void positiveUrlValueVarTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveUrlValueVarTest");
    invoke();
  }

  /*
   * @testName: positiveUrlScopeTest
   * 
   * @assertion_ids: JSTL:SPEC:24.3; JSTL:SPEC:24.3.1; JSTL:SPEC:24.3.1;
   * JSTL:SPEC:24.3.2; JSTL:SPEC:24.3.3; JSTL:SPEC:24.3.4; JSTL:SPEC:24.3.5
   * 
   * @testStrategy: Validate the behavior of the scope attribute with respect to
   * var, both when scope is explicitly defined and when not defined.
   */
  public void positiveUrlScopeTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveUrlScopeTest");
    invoke();
  }

  /*
   * @testName: positiveUrlNoCharEncodingTest
   * 
   * @assertion_ids: JSTL:SPEC:24.12
   * 
   * @testStrategy: Validate that if the URL to be encoded contains special
   * characters, that they are not encoded by the action.
   */
  public void positiveUrlNoCharEncodingTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveUrlNoCharEncodingTest");
    invoke();
  }

  /*
   * @testName: positiveUrlParamsBodyTest
   * 
   * @assertion_ids: JSTL:SPEC:25
   * 
   * @testStrategy: Validate the URL action can properly interact with nested
   * param subtags.
   */
  public void positiveUrlParamsBodyTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveUrlParamsBodyTest");
    invoke();
  }

  /*
   * @testName: positiveUrlAbsUrlNotRewrittenTest
   * 
   * @assertion_ids: JSTL:SPEC:24.5
   * 
   * @testStrategy: Validate that if an absolute URL is provided to the URL
   * action, the result is not rewritten.
   */
  public void positiveUrlAbsUrlNotRewrittenTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveUrlAbsUrlNotRewrittenTest");
    invoke();
  }

  /*
   * @testName: negativeUrlExcBodyContentTest
   * 
   * @assertion_ids: JSTL:SPEC:24.9
   * 
   * @testStrategy: Validate that an Exception is thrown if the body content of
   * the action causes an exception.
   */
  public void negativeUrlExcBodyContentTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "negativeUrlExcBodyContentTest");
    invoke();
  }

  /*
   * @testName: negativeUrlContextUrlInvalidValueTest
   * 
   * @assertion_ids: JSTL:SPEC:24.11.4; JSTL:SPEC:24.11.2
   * 
   * @testStrategy: Validate that an Exception occurs if the value provided to
   * context or url (when context is specified) doesn't begin with a leading
   * slash.
   */
  public void negativeUrlContextUrlInvalidValueTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "negativeUrlContextUrlInvalidValueTest");
    invoke();
  }
}
