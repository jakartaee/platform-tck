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

package com.sun.ts.tests.servlet.api.javax_servlet.servletresponsewrapper;

import java.io.PrintWriter;
import com.sun.javatest.Status;
import com.sun.ts.tests.servlet.api.common.response.HttpResponseClient;

public class URLClient extends HttpResponseClient {

  private static final String CONTEXT_ROOT = "/servlet_js_servletresponsewrapper_web";

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

    setServletName("TestServlet");
    setContextRoot(CONTEXT_ROOT);

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: responseWrapperConstructorTest
   * 
   * @assertion_ids: Servlet:JAVADOC:9
   * 
   * @test_Strategy: Servlet calls wrapper constructor
   */
  public void responseWrapperConstructorTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "responseWrapperConstructorTest");
    invoke();
  }

  /*
   * @testName: responseWrapperGetResponseTest
   * 
   * @assertion_ids: Servlet:JAVADOC:10
   * 
   * @test_Strategy: Servlet gets wrapped response object
   */
  public void responseWrapperGetResponseTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "responseWrapperGetResponseTest");
    invoke();
  }

  /*
   * @testName: responseWrapperSetResponseTest
   * 
   * @assertion_ids: Servlet:JAVADOC:11
   * 
   * @test_Strategy: Servlet sets wrapped response object
   */
  public void responseWrapperSetResponseTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "responseWrapperSetResponseTest");
    invoke();
  }

  /*
   * @testName: responseWrapperSetResponseIllegalArgumentExceptionTest
   * 
   * @assertion_ids: Servlet:JAVADOC:12
   * 
   * @test_Strategy: Servlet sets wrapped response object
   */
  public void responseWrapperSetResponseIllegalArgumentExceptionTest()
      throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "responseWrapperSetResponseIllegalArgumentExceptionTest");
    invoke();
  }

  /*
   * @testName: flushBufferTest
   * 
   * @assertion_ids: Servlet:JAVADOC:24
   * 
   * @test_Strategy: Servlet wraps response. Servlet writes data in the buffer
   * and flushes it
   */

  /*
   * @testName: getBufferSizeTest
   * 
   * @assertion_ids: Servlet:JAVADOC:23
   * 
   * @test_Strategy: Servlet wraps response. Servlet flushes buffer and verifies
   * the size of the buffer
   */

  /*
   * @testName: getLocaleTest
   * 
   * @assertion_ids: Servlet:JAVADOC:30
   * 
   * @test_Strategy: Servlet wraps response. Servlet set Locale and then
   * verifies it
   *
   */

  /*
   * @testName: getOutputStreamTest
   * 
   * @assertion_ids: Servlet:JAVADOC:15
   * 
   * @test_Strategy: Servlet wraps response. Servlet gets an output stream and
   * writes to it.
   */

  /*
   * @testName: getWriterTest
   * 
   * @assertion_ids: Servlet:JAVADOC:17
   * 
   * @test_Strategy: Servlet wraps response. Servlet gets a Writer object, then
   * sets the content type; Verify that content type didn't get set by servlet
   */

  /*
   * @testName: isCommittedTest
   * 
   * @assertion_ids: Servlet:JAVADOC:26
   * 
   * @test_Strategy: Servlet wraps response. Servlet checks before and after
   * response is flushed
   *
   */

  /*
   * @testName: resetBufferTest
   * 
   * @assertion_ids: Servlet:JAVADOC:28
   * 
   * @test_Strategy: Servlet wraps response. Servlet writes data to the
   * response, resets the buffer and then writes new data
   */

  /*
   * @testName: resetTest
   * 
   * @assertion_ids: Servlet:JAVADOC:27
   * 
   * @test_Strategy: Servlet wraps response. Servlet writes data to the
   * response, does a reset, then writes new data
   */

  /*
   * @testName: resetTest1
   * 
   * @assertion_ids: Servlet:JAVADOC:27; Servlet:JAVADOC:162; Servlet:SPEC:31;
   * 
   * @test_Strategy: Servlet writes data to the response, set the Headers, does
   * a reset, then writes new data, set the new Header
   */

  /*
   * @testName: getCharacterEncodingTest
   * 
   * @assertion_ids: Servlet:JAVADOC:14
   * 
   * @test_Strategy: Servlet wraps response. Servlet checks for the default
   * encoding
   */

  /*
   * @testName: setCharacterEncodingTest
   * 
   * @assertion_ids: Servlet:JAVADOC:13
   * 
   * @test_Strategy: Servlet wraps response. Servlet set the encoding and client
   * verifies it
   */

  /*
   * @testName: setBufferSizeTest
   * 
   * @assertion_ids: Servlet:JAVADOC:22
   * 
   * @test_Strategy: Servlet wraps response. Servlet sets the buffer size then
   * verifies it was set
   */

  /*
   * @testName: setContentLengthTest
   * 
   * @assertion_ids: Servlet:JAVADOC:19
   * 
   * @test_Strategy: Servlet wraps response. Servlet sets the content length
   */

  /*
   * @testName: getContentTypeTest
   * 
   * @assertion_ids: Servlet:JAVADOC:21; Servlet:SPEC:34;
   * 
   * @test_Strategy: Servlet wraps response. Servlet verifies the content type
   * sent by the client
   */

  /*
   * @testName: setContentTypeTest
   * 
   * @assertion_ids: Servlet:JAVADOC:20; Servlet:SPEC:34;
   * 
   * @test_Strategy: Servlet wraps response. Servlet sets the content type
   *
   */

  /*
   * @testName: setLocaleTest
   * 
   * @assertion_ids: Servlet:JAVADOC:29
   * 
   * @test_Strategy: Servlet wraps response. Servlet sets the Locale
   */

}
