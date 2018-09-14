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
 * $Id$
 */

package com.sun.ts.tests.jsf.api.javax_faces.context.externalcontextwrapper;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

import java.io.PrintWriter;

public final class URLClient extends AbstractUrlClient {

  private static final String TCK_ATTRIBUTE = "tckattribute";

  private static final String TCK_VALUE = "tckValue";

  private static final String CONTEXT_ROOT = "/jsf_ctx_extctxwrapper_web";

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
   * @testName: extContextWrapperDispatchTest
   * @assertion_ids: JSF:JAVADOC:1202; JSF:JAVADOC:1193
   * @test_Strategy: Verify that dispatch(), in a Servlet environment, can be
   *                 successfully used to dispatch the current request to
   *                 another resource within the web application.
   */
  public void extContextWrapperDispatchTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "extContextWrapperDispatchTest");
    invoke();
  }

  /**
   * @testName: extContextWrapperRedirectTest
   * @assertion_ids: JSF:JAVADOC:1202; JSF:SPEC:1253
   * @test_Strategy: Verify that redirect(), in a Servlet environment, can be
   *                 successfully used to redirect the current request to
   *                 another resource. The client will verify that the Location
   *                 header is present in the response with the appropriate
   *                 host, port, and path.
   */
  public void extContextWrapperRedirectTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST, "GET " + CONTEXT_ROOT
        + "/TestServlet?testname=extContextWrapperRedirectTest HTTP/1.0");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Location:http://" + _hostname + ":" + _port + "/target");
    invoke();
  }

  /**
   * @testName: extContextWrapperGetSessionTest
   * @assertion_ids: JSF:JAVADOC:1155; JSF:JAVADOC:1244
   * @test_Strategy: Ensure the behavior of getSession(). - if no session
   *                 established with client, getSession(false) returns null. -
   *                 getSession(true) will create a new session
   */
  public void extContextWrapperGetSessionTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "extContextWrapperGetSessionTest");
    invoke();
  }

  /**
   * @testName: extContextWrapperGetContextTest
   * @assertion_ids: JSF:JAVADOC:1205; JSF:JAVADOC:1202
   * @test_Strategy: Ensure getContext() returns the ServletContext of the
   *                 current web application instance.
   */
  public void extContextWrapperGetContextTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "extContextWrapperGetContextTest");
    invoke();
  }

  /**
   * @testName: extContextWrapperGetRequestTest
   * @assertion_ids: JSF:JAVADOC:1202; JSF:JAVADOC:1213
   * @test_Strategy: Ensure getRequest() returns the same ServletRequest as what
   *                 was used to invoke the test servlet.
   */
  public void extContextWrapperGetRequestTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "extContextWrapperGetRequestTest");
    invoke();
  }

  /**
   * @testName: extContextWrapperGetResponseTest
   * @assertion_ids: JSF:JAVADOC:1202; JSF:JAVADOC:1236
   * @test_Strategy: Ensure getResponse() returns the same ServletResponse as
   *                 what was used to invoke the test servlet.
   */
  public void extContextWrapperGetResponseTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "extContextWrapperGetResponseTest");
    invoke();
  }

  /**
   * @testName: extContextWrapperGetApplicationMapTest
   * @assertion_ids: JSF:JAVADOC:1202; JSF:JAVADOC:1203
   * @test_Strategy: Verify attributes added to the Map can be retrieved from
   *                 the application map instance. Additionally verify the
   *                 required specialized behavior of the map (mutable) as well
   *                 as verifying that the methods of the Map work as expected.
   */
  public void extContextWrapperGetApplicationMapTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "extContextWrapperGetApplicationMapTest");
    invoke();
  }

  /**
   * @testName: extContextWrapperGetSessionMapTest
   * @assertion_ids: JSF:JAVADOC:1202; JSF:JAVADOC:1245
   * @test_Strategy: Verify attributes added to the Map can be retrieved from
   *                 the session map instance. Additionally verify the required
   *                 specialized behavior of the map (mutable) as well as
   *                 verifying that the methods of the Map work as expected.
   */
  public void extContextWrapperGetSessionMapTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "extContextWrapperGetSessionMapTest");
    invoke();
  }

  /**
   * @testName: extContextWrapperGetRequestMapTest
   * @assertion_ids: JSF:JAVADOC:1202; JSF:JAVADOC:1223
   * @test_Strategy: Verify attributes added to the Map can be retrieved from
   *                 the request map instance. Additionally verify the required
   *                 specialized behavior of the map (mutable) as well as
   *                 verifying that the methods of the Map work as expected.
   */
  public void extContextWrapperGetRequestMapTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "extContextWrapperGetRequestMapTest");
    invoke();
  }

  /**
   * @testName: extContextWrapperGetInitParameterMapTest
   * @assertion_ids: JSF:JAVADOC:1202; JSF:JAVADOC:1209
   * @test_Strategy: Verify context init parameters can be retrieved from the
   *                 init parameter map. Additionally ensure that the map is
   *                 immutable and the methods that don't affect its content
   *                 work as expected.
   */
  public void extContextWrapperGetInitParameterMapTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "extContextWrapperGetInitParameterMapTest");
    invoke();
  }

  /**
   * @testName: extContextWrapperGetRequestHeaderMapTest
   * @assertion_ids: JSF:JAVADOC:1202; JSF:JAVADOC:1219
   * @test_Strategy: Verify request headers can be retrieved from the init
   *                 parameter map. Additionally ensure that the map is
   *                 immutable and the methods that don't affect its content
   *                 work as expected.
   */
  public void extContextWrapperGetRequestHeaderMapTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "extContextWrapperGetRequestHeaderMapTest");
    TEST_PROPS.setProperty(REQUEST_HEADERS, TCK_ATTRIBUTE + ":" + TCK_VALUE);
    invoke();
  }

  /**
   * @testName: extContextWrapperGetRequestParameterMapTest
   * @assertion_ids: JSF:JAVADOC:1202; JSF:JAVADOC:1224
   * @test_Strategy: Verify request parameters can be retrieved from the init
   *                 parameter map. Additionally ensure that the map is
   *                 immutable and the methods that don't affect its content
   *                 work as expected.
   */
  public void extContextWrapperGetRequestParameterMapTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsf_ctx_extctxwrapper_web/TestServlet?testname="
            + "extContextWrapperGetRequestParameterMapTest&" + TCK_ATTRIBUTE
            + "=" + TCK_VALUE + " HTTP/1.0");
    invoke();
  }

  /**
   * @testName: extContextWrapperGetRequestParameterValuesMapTest
   * @assertion_ids: JSF:JAVADOC:1202; JSF:JAVADOC:1226
   * @test_Strategy: Verify request parameter values can be retrieved from the
   *                 init parameter map. Additionally ensure that the map is
   *                 immutable and the methods that don't affect its content
   *                 work as expected.
   */
  public void extContextWrapperGetRequestParameterValuesMapTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsf_ctx_extctxwrapper_web/TestServlet?testname="
            + "extContextWrapperGetRequestParameterValuesMapTest&"
            + TCK_ATTRIBUTE + "=" + TCK_VALUE + " HTTP/1.0");
    invoke();
  }

  /**
   * @testName: extContextWrapperGetRequestCookieMapTest
   * @assertion_ids: JSF:JAVADOC:1202; JSF:JAVADOC:1218
   * @test_Strategy: Verify request cookies can be retrieved from the init
   *                 parameter map. Additionally ensure that the map is
   *                 immutable and the methods that don't affect its content
   *                 work as expected.
   */
  public void extContextWrapperGetRequestCookieMapTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "extContextWrapperGetRequestCookieMapTest");
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "Cookie: $Version=1; " + TCK_ATTRIBUTE + "=" + TCK_VALUE + "; $Domain="
            + _hostname + "; $Path=" + getContextRoot());
    invoke();
  }

  /**
   * @testName: extContextWrapperGetInitParameterTest
   * @assertion_ids: JSF:JAVADOC:1202; JSF:JAVADOC:1208
   * @test_Strategy: Ensure context initialization parameters are correctly
   *                 returned by getInitParameter().
   */
  public void extContextWrapperGetInitParameterTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "extContextWrapperGetInitParameterTest");
    invoke();
  }

  /**
   * @testName: extContextWrapperGetMimeTypeTest
   * @assertion_ids: JSF:JAVADOC:1202; JSF:JAVADOC:1210
   * @test_Strategy: Ensure when getMimeType(String) is called that the correct
   *                 mime type is returned.
   */
  public void extContextWrapperGetMimeTypeTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "extContextWrapperGetMimeTypeTest");
    invoke();
  }

  /**
   * @testName: extContextWrapperGetRemoteUserTest
   * @assertion_ids: JSF:JAVADOC:1202; JSF:JAVADOC:1212
   * @test_Strategy: Ensure getRemoveUser() returns null as no login
   *                 authentication is performed.
   */
  public void extContextWrapperGetRemoteUserTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "extContextWrapperGetRemoteUserTest");
    invoke();
  }

  /**
   * @testName: extContextWrapperGetResourcePathsTest
   * @assertion_ids: JSF:JAVADOC:1202; JSF:JAVADOC:1235
   * @test_Strategy: Ensure the Set returned by
   *                 ExternalContext.getResourcePaths() is equal to the Set
   *                 returned by ServletContext.getResourcePaths().
   */
  public void extContextWrapperGetResourcePathsTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "extContextWrapperGetResourcePathsTest");
    invoke();
  }

  /**
   * @testName: extContextWrapperGetResourceAsStreamTest
   * @assertion_ids: JSF:JAVADOC:1202; JSF:JAVADOC:1234
   * @test_Strategy: Ensure a non-null stream is returned when calling
   *                 ExternalContext.getResourceAsStream() passing the path to
   *                 the web application's deployment descriptor.
   */
  public void extContextWrapperGetResourceAsStreamTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "extContextWrapperGetResourceAsStreamTest");
    invoke();
  }

  /**
   * @testName: extContextWrapperGetRequestContextPathTest
   * @assertion_ids: JSF:JAVADOC:1202; JSF:JAVADOC:1217
   * @test_Strategy: Ensure the path returned by getRequestContextPath() is the
   *                 same as that returned by ServletContext.getContextPath().
   */
  public void extContextWrapperGetRequestContextPathTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "extContextWrapperGetRequestContextPathTest");
    invoke();
  }

  /**
   * @testName: extContextWrapperGetRequestLocaleTest
   * @assertion_ids: JSF:JAVADOC:1202; JSF:JAVADOC:1221
   * @test_Strategy: Ensure the Locale returned by getRequestLocale() is the
   *                 same as the Locale returned by ServletRequest.getLocale().
   */
  public void extContextWrapperGetRequestLocaleTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "extContextWrapperGetRequestLocaleTest");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en-IE-EURO");
    invoke();
  }

  /**
   * @testName: extContextWrapperGetRequestParameterNamesTest
   * @assertion_ids: JSF:JAVADOC:1202; JSF:JAVADOC:1225
   * @test_Strategy: Ensure the parameter names returned by
   *                 getRequestParameterNames() are the same as those returned
   *                 by ServletRequest.getParameterNames().
   */
  public void extContextWrapperGetRequestParameterNamesTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "extContextWrapperGetRequestParameterNamesTest");
    invoke();
  }

  /**
   * @testName: extContextWrapperGetRequestPathInfoTest
   * @assertion_ids: JSF:JAVADOC:1202; JSF:JAVADOC:1227
   * @test_Strategy: Ensure the path info returned by getRequestPathInfo() is
   *                 the same as that returned by ServletRequest.getPathInfo().
   */
  public void extContextWrapperGetRequestPathInfoTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsf_ctx_extctxwrapper_web/TestServlet/pathInfo?testname="
            + "extContextWrapperGetRequestPathInfoTest HTTP/1.0");
    invoke();
  }

  /**
   * @testName: extContextWrapperSetGetSessionMaxInactiveIntervalTest
   * @assertion_ids: JSF:JAVADOC:1202; JSF:JAVADOC:2586
   * @test_Strategy: Validate that we can set/get the Inactive value. Test
   *                 values used are ( 1, 0, -1).
   * 
   * @since 2.1
   */
  public void extContextWrapperSetGetSessionMaxInactiveIntervalTest()
      throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsf_ctx_extctxwrapper_web/TestServlet/pathInfo?testname="
            + "extContextWrapperSetGetSessionMaxInactiveIntervalTest HTTP/1.0");
    invoke();
  }

} // end of URLClient
