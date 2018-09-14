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

package com.sun.ts.tests.jstl.spec.fmt.i18n.requestencoding;

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

    setContextRoot("/jstl_fmt_reqenc_web");
    setGoldenFileDir("/jstl/spec/fmt/i18n/requestencoding");

    return super.run(args, out, err);
  }

  /*
   * @testName: positiveReqEncodingValueTest
   * 
   * @assertion_ids: JSTL:SPEC:45; JSTL:SPEC:45.1; JSTL:SPEC:45.1.1
   * 
   * @testStrategy: Validate that setting the value of the requestEncoding
   * action correctly sets the encoding of the page.
   */
  public void positiveReqEncodingValueTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveReqEncodingValueTest");
    invoke();
  }

  /*
   * @testName: positiveContentTypeEncodingTest
   * 
   * @assertion_ids: JSTL:SPEC:45.4.1
   * 
   * @testStrategy : Validate that if a Content-Type header is present in the
   * client's request, the request encoding is properly set to the value
   * provided by the header. Validation will be by calling
   * getCharacterEncoding() against the request object after the action has been
   * called.
   */
  public void positiveContentTypeEncodingTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "positiveContentTypeEncodingTest");
    TEST_PROPS.setProperty(REQUEST, "positiveContentTypeEncodingTest.jsp");
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "Content-Type: text/plain; charset=UTF-8");
    TEST_PROPS.setProperty(GOLDENFILE, "positiveContentTypeEncodingTest.gf");
    invoke();
  }

  /*
   * @testName: positiveScopedAttrEncodingTest
   * 
   * @assertion_ids: JSTL:SPEC:45.4.2
   * 
   * @testStrategy: Validate that if no Content-Type header is sent by the
   * client, that the value of the scoped variable,
   * javax.servlet.jsp.jstl.fmt.request.charset, is used to set the character
   * encoding of the request. Validation will be by calling
   * getCharacterEncoding() against the request object after the action has been
   * called.
   */
  public void positiveScopedAttrEncodingTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveScopedAttrEncodingTest");
    invoke();
  }

  /*
   * @testName: positiveDefaultEncodingTest
   * 
   * @assertion_ids: JSTL:SPEC:45.4.3
   * 
   * @testStrategy: Validate that if no Content-Type header is sent by the
   * client, and the scoped variable,
   * javax.servlet.jsp.jstl.fmt.request.charset, is not present, the default
   * encoding of ISO-8859-1 is used. Validation will be by calling
   * getCharacterEncoding() against the request object after the action has been
   * called.
   */
  public void positiveDefaultEncodingTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveDefaultEncodingTest");
    invoke();
  }
}
