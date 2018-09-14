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

package com.sun.ts.tests.servlet.api.javax_servlet_http.httpservletresponsewrapper;

import java.io.PrintWriter;
import com.sun.javatest.Status;
import com.sun.ts.tests.servlet.api.common.response.HttpResponseClient;

public class URLClient extends HttpResponseClient {

  private static final String CONTEXT_ROOT = "/servlet_jsh_HSRespWrapper_web";

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

  // ------------------ ServletResponseWrapper
  // -----------------------------------

  /*
   * @testName: flushBufferTest
   * 
   * @assertion_ids: Servlet:JAVADOC:348
   * 
   * @test_Strategy: Servlet wraps response. Servlet writes data in the buffer
   * and flushes it
   */

  /*
   * @testName: getBufferSizeTest
   * 
   * @assertion_ids: Servlet:JAVADOC:347
   * 
   * @test_Strategy: Servlet wraps response. Servlet flushes buffer and verifies
   * the size of the buffer
   */

  /*
   * @testName: getLocaleTest
   * 
   * @assertion_ids: Servlet:JAVADOC:354
   * 
   * @test_Strategy: Servlet wraps response. Servlet set Locale and then
   * verifies it
   *
   */

  /*
   * @testName: getOutputStreamTest
   * 
   * @assertion_ids: Servlet:JAVADOC:339
   * 
   * @test_Strategy: Servlet wraps response. Servlet gets an output stream and
   * writes to it.
   */

  /*
   * @testName: getWriterTest
   * 
   * @assertion_ids: Servlet:JAVADOC:341
   * 
   * @test_Strategy: Servlet wraps response. Servlet gets a Writer object; then
   * sets the content type. verify that content type didn't get set by servlet
   */

  /*
   * @testName: isCommittedTest
   * 
   * @assertion_ids: Servlet:JAVADOC:350
   * 
   * @test_Strategy: Servlet wraps response. Servlet checks before and after
   * response is flushed
   *
   */

  /*
   * @testName: resetBufferTest
   * 
   * @assertion_ids: Servlet:JAVADOC:352
   * 
   * @test_Strategy: Servlet wraps response. Servlet writes data to the
   * response, resets the buffer and then writes new data
   */

  /*
   * @testName: resetTest
   * 
   * @assertion_ids: Servlet:JAVADOC:351
   * 
   * @test_Strategy: Servlet wraps response. Servlet writes data to the
   * response, does a reset, then writes new data
   */

  /*
   * @testName: resetTest1
   * 
   * @assertion_ids: Servlet:JAVADOC:351; Servlet:SPEC:31;
   * 
   * @test_Strategy: Servlet writes data to the response, set the Headers, does
   * a reset, then writes new data, set the new Header
   */

  /*
   * @testName: getCharacterEncodingTest
   * 
   * @assertion_ids: Servlet:JAVADOC:338
   * 
   * @test_Strategy: Servlet wraps response. Servlet checks for the default
   * encoding
   */

  /*
   * @testName: setCharacterEncodingTest
   * 
   * @assertion_ids: Servlet:JAVADOC:337
   * 
   * @test_Strategy: Servlet wraps response. Servlet set the encoding and client
   * verifies it
   */

  /*
   * @testName: setBufferSizeTest
   * 
   * @assertion_ids: Servlet:JAVADOC:346
   * 
   * @test_Strategy: Servlet wraps response. Servlet sets the buffer size then
   * verifies it was set
   */

  /*
   * @testName: setContentLengthTest
   * 
   * @assertion_ids: Servlet:JAVADOC:343
   * 
   * @test_Strategy: Servlet wraps response. Servlet sets the content length
   */

  /*
   * @testName: getContentTypeTest
   * 
   * @assertion_ids: Servlet:JAVADOC:345; Servlet:SPEC:34;
   * 
   * @test_Strategy: Servlet wraps response. Servlet verifies the content type
   * sent by the client
   */

  /*
   * @testName: setContentTypeTest
   * 
   * @assertion_ids: Servlet:JAVADOC:344; Servlet:SPEC:34;
   * 
   * @test_Strategy: Servlet wraps response. Servlet sets the content type
   *
   */

  /*
   * @testName: setLocaleTest
   * 
   * @assertion_ids: Servlet:JAVADOC:353
   * 
   * @test_Strategy: Servlet wraps response. Servlet sets the Locale
   */

  // ----------------------- END ServletResponseWrapper
  // --------------------------

  // --------------------- HttpServletResponseWrapper
  // ----------------------------

  /*
   * @testName: httpResponseWrapperGetResponseTest
   * 
   * @assertion_ids: Servlet:JAVADOC:334
   * 
   * @test_Strategy: Servlet gets wrapped response object
   */
  public void httpResponseWrapperGetResponseTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "httpResponseWrapperGetResponseTest");
    invoke();
  }

  /*
   * @testName: httpResponseWrapperSetResponseTest
   * 
   * @assertion_ids: Servlet:JAVADOC:335
   * 
   * @test_Strategy: Servlet sets wrapped response object
   */
  public void httpResponseWrapperSetResponseTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "httpResponseWrapperSetResponseTest");
    invoke();
  }

  /*
   * @testName: httpResponseWrapperSetResponseIllegalArgumentExceptionTest
   * 
   * @assertion_ids: Servlet:JAVADOC:336
   * 
   * @test_Strategy: Servlet sets wrapped response object
   */
  public void httpResponseWrapperSetResponseIllegalArgumentExceptionTest()
      throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "httpResponseWrapperSetResponseIllegalArgumentExceptionTest");
    invoke();
  }

  /*
   * @testName: httpResponseWrapperConstructorTest
   * 
   * @assertion_ids: Servlet:JAVADOC:313
   * 
   * @test_Strategy: Validate constuctor of HttpServletResponseWrapper.
   */
  public void httpResponseWrapperConstructorTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "httpResponseWrapperConstructorTest");
    invoke();
  }

  /*
   * @testName: addCookieTest
   * 
   * @assertion_ids: Servlet:JAVADOC:314
   * 
   * @test_Strategy: Servlet wrappers response and calls test
   */

  /*
   * @testName: addDateHeaderTest
   * 
   * @assertion_ids: Servlet:JAVADOC:327
   * 
   * @test_Strategy: Servlet wrappers response and calls test
   */

  /*
   * @testName: addHeaderTest
   * 
   * @assertion_ids: Servlet:JAVADOC:329
   * 
   * @test_Strategy: Servlet wrappers response and calls test
   */

  /*
   * @testName: addIntHeaderTest
   * 
   * @assertion_ids: Servlet:JAVADOC:331
   * 
   * @test_Strategy: Servlet wrappers response and calls test
   */

  /*
   * @testName: containsHeaderTest
   * 
   * @assertion_ids: Servlet:JAVADOC:315
   * 
   * @test_Strategy: Servlet wrappers response and calls test
   */

  /*
   * @testName: sendErrorClearBufferTest
   * 
   * @assertion_ids: Servlet:JAVADOC:322; Servlet:SPEC:39;
   * 
   * @test_Strategy: Servlet wrappers response and calls test
   */

  /*
   * @testName: sendError_StringTest
   * 
   * @assertion_ids: Servlet:JAVADOC:320
   * 
   * @test_Strategy: Servlet wrappers response and calls test
   */

  /*
   * @testName: sendRedirectTest
   * 
   * @assertion_ids: Servlet:JAVADOC:324
   * 
   * @test_Strategy: Servlet wrappers response and calls test
   */
  public void sendRedirectTest() throws Fault {
    String testName = "sendRedirectWithLeadingSlashTest";
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST, "GET " + getContextRoot() + "/"
        + getServletName() + "?testname=" + testName + " HTTP/1.1");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Location: http://" + _hostname + ":" + _port + "/RedirectedTest");
    TEST_PROPS.setProperty(STATUS_CODE, MOVED_TEMPORARY);
    invoke();
    testName = "sendRedirectWithoutLeadingSlashTest";
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST, "GET " + getContextRoot() + "/"
        + getServletName() + "?testname=" + testName + " HTTP/1.1");
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "Location: http://" + _hostname
        + ":" + _port + "" + getContextRoot() + "/RedirectedTest");
    TEST_PROPS.setProperty(STATUS_CODE, MOVED_TEMPORARY);
    invoke();
  }

  /*
   * @testName: setDateHeaderTest
   * 
   * @assertion_ids: Servlet:JAVADOC:326
   * 
   * @test_Strategy: Servlet wrappers response and calls test
   */

  /*
   * @testName: setHeaderTest
   * 
   * @assertion_ids: Servlet:JAVADOC:328
   * 
   * @test_Strategy: Servlet wrappers response and calls test
   */

  /*
   * @testName: setMultiHeaderTest
   *
   * @assertion_ids: Servlet:SPEC:183; Servlet:JAVADOC:523; Servlet:JAVADOC:525;
   * Servlet:JAVADOC:524
   *
   * @test_Strategy: Servlet sets the multivalues for the same header; verify
   * that setHeader clear all with new value
   */

  /*
   * @testName: setIntHeaderTest
   * 
   * @assertion_ids: Servlet:JAVADOC:330
   * 
   * @test_Strategy: Servlet wrappers response and calls test
   */

  /*
   * @testName: setStatusTest
   * 
   * @assertion_ids: Servlet:JAVADOC:332
   * 
   * @test_Strategy: Testing Servlet API
   * HttpServletResponseWrapper.setStatus(SC_OK)
   */

  /*
   * @testName: setStatusTest1
   * 
   * @assertion_ids: Servlet:JAVADOC:332
   * 
   * @test_Strategy: Testing Servlet API
   * HttpServletResponseWrapper.setStatus(SC_NOT_FOUND)
   */

  // ------------------- END HttpServletResponseWrapper
  // --------------------------

}
