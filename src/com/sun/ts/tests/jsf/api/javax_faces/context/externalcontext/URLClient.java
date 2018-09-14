/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsf.api.javax_faces.context.externalcontext;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

import java.io.PrintWriter;

public final class URLClient extends AbstractUrlClient {

  private static final String TCK_ATTRIBUTE = "tckattribute";

  private static final String TCK_VALUE = "tckValue";

  private static final String CONTEXT_ROOT = "/jsf_ctx_extctx_web";

  public static void main(String[] args) {
    URLClient theTests = new URLClient();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  public Status run(String args[], PrintWriter out, PrintWriter err) {
    setContextRoot(CONTEXT_ROOT);
    setServletName(DEFAULT_SERVLET_NAME);
    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Test Declarations */

  /**
   * @testName: extContextDispatchTest
   * 
   * @assertion_ids: JSF:SPEC:85; JSF:SPEC:86; JSF:JAVADOC:1094
   * 
   * @test_Strategy: Verify that dispatch(), in a Servlet environment, can be
   *                 successfully used to dispatch the current request to
   *                 another resource within the web application.
   */
  public void extContextDispatchTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "extContextDispatchTest");
    invoke();
  }

  /**
   * @testName: extContextEncodeActionURLNPETest
   * 
   * @assertion_ids: JSF:SPEC:85; JSF:SPEC:86; JSF:JAVADOC:1100
   * 
   * @test_Strategy: Verify that a NullPointerException is thrown if url is null
   */
  public void extContextEncodeActionURLNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "extContextEncodeActionURLNPETest");
    invoke();
  }

  /**
   * @testName: extContextEncodePartialActionURLNPETest
   * 
   * @assertion_ids: JSF:SPEC:85; JSF:SPEC:86; JSF:JAVADOC:1105
   * 
   * @test_Strategy: Verify that a NullPointerException is thrown if url is null
   */
  public void extContextEncodePartialActionURLNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "extContextEncodePartialActionURLNPETest");
    invoke();
  }

  /**
   * @testName: extContextEncodeResourceURLNPETest
   * 
   * @assertion_ids: JSF:SPEC:85; JSF:SPEC:86; JSF:JAVADOC:1108
   * 
   * @test_Strategy: Verify that a NullPointerException is thrown if url is null
   */
  public void extContextEncodeResourceURLNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "extContextEncodeResourceURLNPETest");
    invoke();
  }

  /**
   * @testName: extContextGetResourceNPETest
   * 
   * @assertion_ids: JSF:SPEC:85; JSF:SPEC:86; JSF:JAVADOC:1142
   * 
   * @test_Strategy: Verify that a NullPointerException is thrown if arg is null
   */
  public void extContextGetResourceNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "extContextGetResourceNPETest");
    invoke();
  }

  /**
   * @testName: extContextGetResourceAsStreamNPETest
   * 
   * @assertion_ids: JSF:SPEC:85; JSF:SPEC:86; JSF:JAVADOC:1144
   * 
   * @test_Strategy: Verify that a NullPointerException is thrown if arg is null
   */
  public void extContextGetResourceAsStreamNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "extContextGetResourceAsStreamNPETest");
    invoke();
  }

  /**
   * @testName: extContextGetResourcePathsNPETest
   * 
   * @assertion_ids: JSF:SPEC:85; JSF:SPEC:86; JSF:JAVADOC:1146
   * 
   * @test_Strategy: Verify that a NullPointerException is thrown if arg is null
   */
  public void extContextGetResourcePathsNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "extContextGetResourcePathsNPETest");
    invoke();
  }

  /**
   * @testName: extContextIsUserInRoleNPETest
   * 
   * @assertion_ids: JSF:SPEC:85; JSF:SPEC:86; JSF:JAVADOC:1161
   * 
   * @test_Strategy: Verify that a NullPointerException is thrown if arg is null
   */
  public void extContextIsUserInRoleNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "extContextIsUserInRoleNPETest");
    invoke();
  }

  /**
   * @testName: extContextLogNPETest
   * 
   * @assertion_ids: JSF:SPEC:85; JSF:SPEC:86; JSF:JAVADOC:1163;
   *                 JSF:JAVADOC:1165
   * 
   * @test_Strategy: Verify that a NullPointerException is thrown if arg is null
   */
  public void extContextLogNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "extContextLogNPETest");
    invoke();
  }

  /**
   * @testName: extContextGetInitParameterNPETest
   * 
   * @assertion_ids: JSF:SPEC:85; JSF:SPEC:86; JSF:JAVADOC:1116
   * 
   * @test_Strategy: Verify that a NullPointerException is thrown if name is
   *                 null
   */
  public void extContextGetInitParameterNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "extContextGetInitParameterNPETest");
    invoke();
  }

  /**
   * @testName: extContextRedirectTest
   * @assertion_ids: JSF:SPEC:85; JSF:SPEC:86
   * @test_Strategy: Verify that redirect(), in a Servlet environment, can be
   *                 successfully used to redirect the current request to
   *                 another resource. The client will verify that the Location
   *                 header is present in the response with the appropriate
   *                 host, port, and path.
   */
  public void extContextRedirectTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST, "GET " + CONTEXT_ROOT
        + "/TestServlet?testname=extContextRedirectTest HTTP/1.0");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Location:http://" + _hostname + ":" + _port + "/target");
    invoke();
  }

  /**
   * @testName: extContextGetSessionTest
   * @assertion_ids: JSF:JAVADOC:1155; JSF:JAVADOC:1109
   * @test_Strategy: Ensure the behavior of getSession(). - if no session
   *                 established with client, getSession(false) returns null. -
   *                 getSession(true) will create a new session
   */
  public void extContextGetSessionTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "extContextGetSessionTest");
    invoke();
  }

  /**
   * @testName: extContextGetContextTest
   * @assertion_ids: JSF:JAVADOC:1113; JSF:JAVADOC:1109
   * @test_Strategy: Ensure getContext() returns the ServletContext of the
   *                 current web application instance.
   */
  public void extContextGetContextTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "extContextGetContextTest");
    invoke();
  }

  /**
   * @testName: extContextGetRequestTest
   * @assertion_ids: JSF:JAVADOC:1109; JSF:JAVADOC:1121
   * @test_Strategy: Ensure getRequest() returns the same ServletRequest as what
   *                 was used to invoke the test servlet.
   */
  public void extContextGetRequestTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "extContextGetRequestTest");
    invoke();
  }

  /**
   * @testName: extContextGetResponseTest
   * @assertion_ids: JSF:JAVADOC:1109; JSF:JAVADOC:1147
   * @test_Strategy: Ensure getResponse() returns the same ServletResponse as
   *                 what was used to invoke the test servlet.
   */
  public void extContextGetResponseTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "extContextGetResponseTest");
    invoke();
  }

  /**
   * @testName: extContextGetApplicationMapTest
   * @assertion_ids: JSF:JAVADOC:1109; JSF:JAVADOC:1110
   * @test_Strategy: Verify attributes added to the Map can be retrieved from
   *                 the application map instance. Additionally verify the
   *                 required specialized behavior of the map (mutable) as well
   *                 as verifying that the methods of the Map work as expected.
   */
  public void extContextGetApplicationMapTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "extContextGetApplicationMapTest");
    invoke();
  }

  /**
   * @testName: extContextGetSessionMapTest
   * @assertion_ids: JSF:JAVADOC:1109; JSF:JAVADOC:1156
   * @test_Strategy: Verify attributes added to the Map can be retrieved from
   *                 the session map instance. Additionally verify the required
   *                 specialized behavior of the map (mutable) as well as
   *                 verifying that the methods of the Map work as expected.
   */
  public void extContextGetSessionMapTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "extContextGetSessionMapTest");
    invoke();
  }

  /**
   * @testName: extContextGetRequestMapTest
   * @assertion_ids: JSF:JAVADOC:1109; JSF:JAVADOC:1131
   * @test_Strategy: Verify attributes added to the Map can be retrieved from
   *                 the request map instance. Additionally verify the required
   *                 specialized behavior of the map (mutable) as well as
   *                 verifying that the methods of the Map work as expected.
   */
  public void extContextGetRequestMapTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "extContextGetRequestMapTest");
    invoke();
  }

  /**
   * @testName: extContextGetInitParameterMapTest
   * @assertion_ids: JSF:JAVADOC:1109; JSF:JAVADOC:1115
   * @test_Strategy: Verify context init parameters can be retrieved from the
   *                 init parameter map. Additionally ensure that the map is
   *                 immutable and the methods that don't affect its content
   *                 work as expected.
   */
  public void extContextGetInitParameterMapTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "extContextGetInitParameterMapTest");
    invoke();
  }

  /**
   * @testName: extContextGetRequestHeaderMapTest
   * @assertion_ids: JSF:JAVADOC:1109; JSF:JAVADOC:1127
   * @test_Strategy: Verify request headers can be retrieved from the init
   *                 parameter map. Additionally ensure that the map is
   *                 immutable and the methods that don't affect its content
   *                 work as expected.
   */
  public void extContextGetRequestHeaderMapTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "extContextGetRequestHeaderMapTest");
    TEST_PROPS.setProperty(REQUEST_HEADERS, TCK_ATTRIBUTE + ":" + TCK_VALUE);
    invoke();
  }

  /**
   * @testName: extContextGetRequestParameterMapTest
   * @assertion_ids: JSF:JAVADOC:1109; JSF:JAVADOC:1132
   * @test_Strategy: Verify request parameters can be retrieved from the init
   *                 parameter map. Additionally ensure that the map is
   *                 immutable and the methods that don't affect its content
   *                 work as expected.
   */
  public void extContextGetRequestParameterMapTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsf_ctx_extctx_web/TestServlet?testname=extContextGetRequestParameterMapTest&"
            + TCK_ATTRIBUTE + "=" + TCK_VALUE + " HTTP/1.0");
    invoke();
  }

  /**
   * @testName: extContextGetRequestParameterValuesMapTest
   * @assertion_ids: JSF:JAVADOC:1109; JSF:JAVADOC:1134
   * @test_Strategy: Verify request parameter values can be retrieved from the
   *                 init parameter map. Additionally ensure that the map is
   *                 immutable and the methods that don't affect its content
   *                 work as expected.
   */
  public void extContextGetRequestParameterValuesMapTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsf_ctx_extctx_web/TestServlet?testname=extContextGetRequestParameterValuesMapTest&"
            + TCK_ATTRIBUTE + "=" + TCK_VALUE + " HTTP/1.0");
    invoke();
  }

  /**
   * @testName: extContextGetRequestCookieMapTest
   * @assertion_ids: JSF:JAVADOC:1109; JSF:JAVADOC:1126
   * @test_Strategy: Verify request cookies can be retrieved from the init
   *                 parameter map. Additionally ensure that the map is
   *                 immutable and the methods that don't affect its content
   *                 work as expected.
   */
  public void extContextGetRequestCookieMapTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "extContextGetRequestCookieMapTest");
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "Cookie: $Version=1; " + TCK_ATTRIBUTE + "=" + TCK_VALUE + "; $Domain="
            + _hostname + "; $Path=" + getContextRoot());
    invoke();
  }

  /**
   * @testName: extContextGetInitParameterTest
   * @assertion_ids: JSF:JAVADOC:1109; JSF:JAVADOC:1115
   * @test_Strategy: Ensure context initialization parameters are correctly
   *                 returned by getInitParameter().
   */
  public void extContextGetInitParameterTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "extContextGetInitParameterTest");
    invoke();
  }

  /**
   * @testName: extContextGetMimeTypeTest
   * @assertion_ids: JSF:JAVADOC:1109; JSF:JAVADOC:1118
   * @test_Strategy: Ensure when getMimeType(String) is called that the correct
   *                 mime type is returned.
   */
  public void extContextGetMimeTypeTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "extContextGetMimeTypeTest");
    invoke();
  }

  /**
   * @testName: extContextGetRemoteUserTest
   * @assertion_ids: JSF:JAVADOC:1109; JSF:JAVADOC:1120
   * @test_Strategy: Ensure getRemoveUser() returns null as no login
   *                 authentication is performed.
   */
  public void extContextGetRemoteUserTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "extContextGetRemoteUserTest");
    invoke();
  }

  /**
   * @testName: extContextGetResourcePathsTest
   * @assertion_ids: JSF:JAVADOC:1109; JSF:JAVADOC:1145
   * @test_Strategy: Ensure the Set returned by
   *                 ExternalContext.getResourcePaths() is equal to the Set
   *                 returned by ServletContext.getResourcePaths().
   */
  public void extContextGetResourcePathsTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "extContextGetResourcePathsTest");
    invoke();
  }

  /**
   * @testName: extContextGetResourceAsStreamTest
   * @assertion_ids: JSF:JAVADOC:1109; JSF:JAVADOC:1143
   * @test_Strategy: Ensure a non-null stream is returned when calling
   *                 ExternalContext.getResourceAsStream() passing the path to
   *                 the web application's deployment descriptor.
   */
  public void extContextGetResourceAsStreamTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "extContextGetResourceAsStreamTest");
    invoke();
  }

  /**
   * @testName: extContextGetRequestContextPathTest
   * @assertion_ids: JSF:JAVADOC:1109; JSF:JAVADOC:1125
   * @test_Strategy: Ensure the path returned by getRequestContextPath() is the
   *                 same as that returned by ServletContext.getContextPath().
   */
  public void extContextGetRequestContextPathTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "extContextGetRequestContextPathTest");
    invoke();
  }

  /**
   * @testName: extContextGetRequestLocaleTest
   * @assertion_ids: JSF:JAVADOC:1109; JSF:JAVADOC:1129
   * @test_Strategy: Ensure the Locale returned by getRequestLocale() is the
   *                 same as the Locale returned by ServletRequest.getLocale().
   */
  public void extContextGetRequestLocaleTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "extContextGetRequestLocaleTest");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en-IE-EURO");
    invoke();
  }

  /**
   * @testName: extContextGetRequestParameterNamesTest
   * @assertion_ids: JSF:JAVADOC:1109; JSF:JAVADOC:1133
   * @test_Strategy: Ensure the parameter names returned by
   *                 getRequestParameterNames() are the same as those returned
   *                 by ServletRequest.getParameterNames().
   */
  public void extContextGetRequestParameterNamesTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "extContextGetRequestParameterNamesTest");
    invoke();
  }

  /**
   * @testName: extContextGetRequestPathInfoTest
   * @assertion_ids: JSF:JAVADOC:1109; JSF:JAVADOC:1135
   * @test_Strategy: Ensure the path info returned by getRequestPathInfo() is
   *                 the same as that returned by ServletRequest.getPathInfo().
   */
  public void extContextGetRequestPathInfoTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsf_ctx_extctx_web/TestServlet/pathInfo?testname=extContextGetRequestPathInfoTest"
            + " HTTP/1.0");
    invoke();
  }

  /**
   * @testName: setGetSessionMaxInactiveIntervalTest
   * @assertion_ids: JSF:JAVADOC:1109; JSF:JAVADOC:2582
   * @test_Strategy: Validate that we can set/get the Inactive value. Test
   *                 values used are ( 1, 0, -1).
   * 
   * @since 2.1
   */
  public void setGetSessionMaxInactiveIntervalTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsf_ctx_extctx_web/TestServlet/pathInfo?testname=setGetSessionMaxInactiveIntervalTest"
            + " HTTP/1.0");
    invoke();
  }

} // end of URLClient
