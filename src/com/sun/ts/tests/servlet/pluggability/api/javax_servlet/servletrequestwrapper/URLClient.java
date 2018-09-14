/*
 * Copyright (c) 2012, 2018 Oracle and/or its affiliates. All rights reserved.
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
 * $Id:$
 */
package com.sun.ts.tests.servlet.pluggability.api.javax_servlet.servletrequestwrapper;

import java.io.PrintWriter;
import com.sun.javatest.Status;
import com.sun.ts.tests.servlet.api.common.request.RequestClient;

public class URLClient extends RequestClient {

  private static final String CONTEXT_ROOT = "/servlet_plu_servletrequestwrapper_web";

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
   * @testName: requestWrapperConstructorTest
   * 
   * @assertion_ids: Servlet:JAVADOC:31
   * 
   * @test_Strategy: Servlet calls wrapper constructor
   */
  public void requestWrapperConstructorTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "requestWrapperConstructorTest");
    invoke();
  }

  /*
   * @testName: requestWrapperGetRequestTest
   * 
   * @assertion_ids: Servlet:JAVADOC:32
   * 
   * @test_Strategy: Servlet gets wrapped request object
   */
  public void requestWrapperGetRequestTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "requestWrapperGetRequestTest");
    invoke();
  }

  /*
   * @testName: requestWrapperSetRequestTest
   * 
   * @assertion_ids: Servlet:JAVADOC:33
   * 
   * @test_Strategy: Servlet sets wrapped request object
   */
  public void requestWrapperSetRequestTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "requestWrapperSetRequestTest");
    invoke();
  }

  /*
   * @testName: requestWrapperSetRequestIllegalArgumentExceptionTest
   * 
   * @assertion_ids: Servlet:JAVADOC:34
   * 
   * @test_Strategy: Servlet sets wrapped request object
   */
  public void requestWrapperSetRequestIllegalArgumentExceptionTest()
      throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "requestWrapperSetRequestIllegalArgumentExceptionTest");
    invoke();
  }

  /*
   * @testName: getAttributeNamesTest
   * 
   * @assertion_ids: Servlet:JAVADOC:36
   * 
   * @test_Strategy: Servlet wraps the request. Servlet then sets some
   * attributes and verifies they can be retrieved.
   */

  /*
   * @testName: getAttributeTest
   * 
   * @assertion_ids: Servlet:JAVADOC:35
   * 
   * @test_Strategy: Servlet wraps the request. Servlet then sets an attribute
   * and retrieves it.
   *
   */
  /*
   * @testName: getCharacterEncodingTest
   * 
   * @assertion_ids: Servlet:JAVADOC:37
   * 
   * @test_Strategy: Client sets an encoding. Servlet wraps the request. Servlet
   * then tries to retrieve it.
   */

  /*
   * @testName: getContentLengthTest
   * 
   * @assertion_ids: Servlet:JAVADOC:40
   * 
   * @test_Strategy: Servlet wraps the request. Servlet then compares this
   * length to the actual length of the content body read in using
   * getInputStream
   *
   */

  /*
   * @testName: getContentTypeTest
   * 
   * @assertion_ids: Servlet:JAVADOC:41; Servlet:SPEC:34;
   * 
   * @test_Strategy: Client sets the content type and servlet reads it.
   *
   */

  /*
   * @testName: getInputStreamTest
   * 
   * @assertion_ids: Servlet:JAVADOC:42
   * 
   * @test_Strategy: Servlet wraps the request. Servlet then tries to read the
   * input stream.
   */
  /*
   * @testName: getLocaleTest
   * 
   * @assertion_ids: Servlet:JAVADOC:58
   * 
   * @test_Strategy: Client specifics a locale, Servlet wraps the request.
   * Servlet then verifies it.
   */

  /*
   * @testName: getLocalesTest
   * 
   * @assertion_ids: Servlet:JAVADOC:59
   * 
   * @test_Strategy: Client specifics 2 locales.Servlet wraps the request.
   * Servlet then verifies it.
   */

  /*
   * @testName: getParameterMapTest
   * 
   * @assertion_ids: Servlet:JAVADOC:45
   * 
   * @test_Strategy: Client sets several parameters.Servlet wraps the request.
   * Servlet then attempts to access them.
   */

  /*
   * @testName: getParameterNamesTest
   * 
   * @assertion_ids: Servlet:JAVADOC:46
   * 
   * @test_Strategy: Client sets several parameters.Servlet wraps the request.
   * Servlet then attempts to access them.
   */

  /*
   * @testName: getParameterTest
   * 
   * @assertion_ids: Servlet:JAVADOC:44
   * 
   * @test_Strategy: Client sets a parameter.Servlet wraps the request. Servlet
   * then retrieves parameter.
   */
  /*
   * @testName: getParameterValuesTest
   * 
   * @assertion_ids: Servlet:JAVADOC:47
   * 
   * @test_Strategy: Client sets a parameter which has 2 values.Servlet wraps
   * the request. Servlet then verifies both values.
   */

  /*
   * @testName: getProtocolTest
   * 
   * @assertion_ids: Servlet:JAVADOC:48
   * 
   * @test_Strategy: Servlet wraps the request. Servlet then verifies the
   * protocol used by the client
   */
  /*
   * @testName: getReaderTest
   * 
   * @assertion_ids: Servlet:JAVADOC:52
   * 
   * @test_Strategy: Client sets some content.Servlet wraps the request. Servlet
   * then reads the content
   */
  /*
   * @testName: getRemoteAddrTest
   * 
   * @assertion_ids: Servlet:JAVADOC:54
   * 
   * @test_Strategy: Servlet wraps the request. Servlet then reads and verifies
   * where the request originated
   */

  /*
   * @testName: getRemoteHostTest
   * 
   * @assertion_ids: Servlet:JAVADOC:55
   * 
   * @test_Strategy: Servlet wraps the request. Servlet then reads and verifies
   * where the request originated
   */

  /*
   * @testName: getRequestDispatcherTest
   * 
   * @assertion_ids: Servlet:JAVADOC:61
   * 
   * @test_Strategy: Servlet wraps the request. Servlet then tries to get a
   * dispatcher
   */
  /*
   * @testName: getSchemeTest
   * 
   * @assertion_ids: Servlet:JAVADOC:49
   * 
   * @test_Strategy: Servlet wraps the request. Servlet then verifies the scheme
   * of the url used in the request
   */
  /*
   * @testName: getServerNameTest
   * 
   * @assertion_ids: Servlet:JAVADOC:50
   * 
   * @test_Strategy: Servlet wraps the request. Servlet then verifies the
   * destination of the request
   */
  /*
   * @testName: getServerPortTest
   * 
   * @assertion_ids: Servlet:JAVADOC:51
   * 
   * @test_Strategy: Servlet wraps the request. Servlet then verifies the
   * destination port of the request
   */
  /*
   * @testName: isSecureTest
   * 
   * @assertion_ids: Servlet:JAVADOC:60
   * 
   * @test_Strategy: Servlet wraps the request. Servlet then verifies the
   * isSecure method for the non-secure case.
   */
  /*
   * @testName: removeAttributeTest
   * 
   * @assertion_ids: Servlet:JAVADOC:57
   * 
   * @test_Strategy: Servlet wraps the request. Servlet then adds then removes
   * an attribute, then verifies it was removed.
   */
  /*
   * @testName: setAttributeTest
   * 
   * @assertion_ids: Servlet:JAVADOC:56
   * 
   * @test_Strategy: Servlet wraps the request. Servlet then adds an attribute,
   * then verifies it was added
   */
  /*
   * @testName: setCharacterEncodingUnsupportedEncodingExceptionTest
   * 
   * @assertion_ids: Servlet:JAVADOC:39
   * 
   * @test_Strategy: Servlet wraps the request. Servlet then tries to set an
   * invalid encoding.
   *
   */
  /*
   * @testName: setCharacterEncodingTest
   * 
   * @assertion_ids: Servlet:JAVADOC:38
   * 
   * @test_Strategy: Servlet wraps the request. Servlet then sets a new encoding
   * and tries to retrieve it. verifies that the new encoding gets set.
   */
  /*
   * @testName: setCharacterEncodingTest1
   * 
   * @assertion_ids: Servlet:JAVADOC:38; Servlet:JAVADOC:37; Servlet:SPEC:28;
   * Servlet:SPEC:213;
   * 
   * @test_Strategy: Servlet wraps the request; ServletRequestWrapper calls
   * getReader(); then sets a new encoding and tries to retrieve it. verifies
   * that the new encoding is ignored.
   */
  /*
   * @testName: getLocalNameTest
   * 
   * @assertion_ids: Servlet:JAVADOC:633
   * 
   * @test_Strategy: Servlet wraps the request. Servlet then verify
   * getLocalName();
   */

  /*
   * @testName: getLocalPortTest
   * 
   * @assertion_ids: Servlet:JAVADOC:636
   * 
   * @test_Strategy: Servlet wraps the request. Servlet then verify
   * getLocalPort();
   */
}
