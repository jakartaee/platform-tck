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

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.faces.context.ExternalContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

public final class TestServlet extends HttpTCKServlet {

  private static final String TCK_ATTRIBUTE = "tckattribute";

  private static final String TCK_VALUE = "tckValue";

  private static final String NO_SUCH_ATTRIBUTE = "no_such_attribute_could_possibly_exist";

  private static final String NO_SUCH_VALUE = "no_such_value_could_possibly_exist";

  private ServletContext servletContext;

  private static final String TCK_KEY = "tckKey1";

  private static final String TCK_VALUE1 = "tckValue1";

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    servletContext = config.getServletContext();
  }

  // ExternalContext.dispatch(String)
  public void extContextDispatchTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    getFacesContext().getExternalContext().dispatch("/target");
  }

  // ExternalContext.encodeActionURL(null) throws NullPointerException
  public void extContextEncodeActionURLNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    ExternalContext ec = getFacesContext().getExternalContext();

    JSFTestUtil.checkForNPE(ec, "encodeActionURL",
        new Class<?>[] { String.class }, new Object[] { null }, out);

  }// End extContextEncodeActionURLNPETest

  // ExternalContext.encodePartialActionURL(null) throws NullPointerException
  public void extContextEncodePartialActionURLNPETest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    ExternalContext ec = getFacesContext().getExternalContext();

    JSFTestUtil.checkForNPE(ec, "encodePartialActionURL",
        new Class<?>[] { String.class }, new Object[] { null }, out);

  }// End extContextEncodePartialActionURLNPETest

  // ExternalContext.encodeResourceURL(null) throws NullPointerException
  public void extContextEncodeResourceURLNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    ExternalContext ec = getFacesContext().getExternalContext();

    JSFTestUtil.checkForNPE(ec, "encodeResourceURL",
        new Class<?>[] { String.class }, new Object[] { null }, out);

  }// End extContextEncodeResourceURLNPETest

  // ExternalContext.getResource(null) throws NullPointerException
  public void extContextGetResourceNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    ExternalContext ec = getFacesContext().getExternalContext();

    JSFTestUtil.checkForNPE(ec, "getResource", new Class<?>[] { String.class },
        new Object[] { null }, out);

  }// End extContextGetResourceNPETest

  // ExternalContext.getResourceAsStream(null) throws NullPointerException
  public void extContextGetResourceAsStreamNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    ExternalContext ec = getFacesContext().getExternalContext();

    JSFTestUtil.checkForNPE(ec, "getResourceAsStream",
        new Class<?>[] { String.class }, new Object[] { null }, out);

  }// End extContextGetResourceAsStreamNPETest

  // ExternalContext.getResourcePaths(null) throws NullPointerException
  public void extContextGetResourcePathsNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    ExternalContext ec = getFacesContext().getExternalContext();

    JSFTestUtil.checkForNPE(ec, "getResourcePaths",
        new Class<?>[] { String.class }, new Object[] { null }, out);

  }// End extContextGetResourcePathsNPETest

  // ExternalContext.getInitParameter(null) throws NullPointerException
  public void extContextGetInitParameterNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    ExternalContext ec = getFacesContext().getExternalContext();

    JSFTestUtil.checkForNPE(ec, "getInitParameter",
        new Class<?>[] { String.class }, new Object[] { null }, out);

  }// End extContextGetInitParameterNPETest

  // ExternalContext.redirect(String)
  public void extContextRedirectTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    getFacesContext().getExternalContext().redirect("/target");
  }

  // ExternalContext.getSession(boolean)
  public void extContextGetSessionTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ExternalContext context = getFacesContext().getExternalContext();
    // no current session, so false will result in null
    Object session = context.getSession(false);
    if (session != null) {
      out.println(JSFTestUtil.FAIL
          + " No session has been established - ExternalContext."
          + "getSession(false) should have returned null.");
      return;
    }

    session = context.getSession(true);
    if (session == null) {
      out.println(JSFTestUtil.FAIL
          + " No session was returned whne calling ExternalContext."
          + "getSession(true).");
      return;
    }
    out.println(JSFTestUtil.PASS);
  }

  // ExtenalContext.getContext()
  // Test CANNOT run in a Portlet env.
  public void extContextGetContextTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Object context = getFacesContext().getExternalContext().getContext();
    if (context != null) {
      if (context instanceof ServletContext) {
        if (servletContext != context) {
          out.println(JSFTestUtil.FAIL
              + " ExternalContext.getContext() returned a ServletContext,"
              + " but it was not the ServletContext instance that was expected.");
          out.println("Expected: " + servletContext);
          out.println("Received: " + context);
          return;
        }
      } else {
        out.println(JSFTestUtil.FAIL
            + " ExternalContext.getContext() did not return a ServletContext"
            + " as expected.");
        out.println(
            "Class of object received: " + context.getClass().getName());
        return;
      }
    } else {
      out.println(
          JSFTestUtil.FAIL + " ExternalContext.getContext() returned null.");
      return;
    }
    out.println(JSFTestUtil.PASS);
  }

  // ExternalContext.getRequest()
  // Test CANNOT run in a Portlet env.
  public void extContextGetRequestTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    request.setAttribute(TCK_ATTRIBUTE, TCK_VALUE);
    Object req = getFacesContext().getExternalContext().getRequest();
    if (req != null) {
      if (req instanceof ServletRequest) {
        if (!TCK_VALUE
            .equals(((ServletRequest) req).getAttribute(TCK_ATTRIBUTE))) {
          out.println(JSFTestUtil.FAIL
              + " ExternalContext.getRequest() returned a ServletRequest,"
              + " but it was not the same ServletRequest used by the current request.");
          return;
        }
      } else {
        out.println(JSFTestUtil.FAIL
            + " ExternalContext.getRequest() did not return a ServletRequest"
            + " as expected.");
        out.println("Class of object received: " + req.getClass().getName());
      }
    } else {
      out.println(
          JSFTestUtil.FAIL + " ExternalContext.getRequest() returned null.");
    }
    out.println(JSFTestUtil.PASS);
  }

  // ExternalContext.getResponse()
  // Test CANNOT run in a Portlet env.
  public void extContextGetResponseTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    response.addHeader(TCK_ATTRIBUTE, TCK_VALUE);
    Object res = getFacesContext().getExternalContext().getResponse();
    if (res != null) {
      if (res instanceof ServletResponse) {
        if (!((HttpServletResponse) res).containsHeader(TCK_ATTRIBUTE)) {
          out.println(JSFTestUtil.FAIL
              + " ExternalContext.getResponse() returned a ServletResponse,"
              + " but it was not the same ServletResponse used by the current request.");
          return;
        }
      } else {
        out.println(JSFTestUtil.FAIL
            + " ExternalContext.getRequest() did not return a ServletResponse"
            + " as expected.");
        out.println("Class of object received: " + res.getClass().getName());
      }
    } else {
      out.println(
          JSFTestUtil.FAIL + " ExternalContext.getResponse() returned null.");
    }
    out.println(JSFTestUtil.PASS);
  }

  // ExternalContext.getApplicationMap()
  public void extContextGetApplicationMapTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    servletContext.setAttribute(TCK_ATTRIBUTE, TCK_VALUE);
    Map applMap = getFacesContext().getExternalContext().getApplicationMap();

    if (applMap == null) {
      out.println(JSFTestUtil.FAIL
          + " ExternalContext.getApplicationMap() returned null");
      return;
    }

    String result = validateMutableMap(applMap, TCK_VALUE);

    if (result != null) {
      out.println("Test FAILED. " + result);
      return;
    }

    if (!TCK_VALUE1.equals(servletContext.getAttribute(TCK_KEY))) {
      out.println(
          "Unable to find expected value for attribute 'tckKey1' in ServletContext");
      out.println("value: " + servletContext.getAttribute(TCK_KEY));
      return;
    }

    // cleanup
    servletContext.removeAttribute(TCK_ATTRIBUTE);
    servletContext.removeAttribute(TCK_KEY);

    out.println(JSFTestUtil.PASS);
  }

  // ExternalContext.getSessionMap()
  public void extContextGetSessionMapTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    HttpSession session = request.getSession(true);
    session.setAttribute(TCK_ATTRIBUTE, TCK_VALUE);
    Map sessMap = getFacesContext().getExternalContext().getSessionMap();

    if (sessMap == null) {
      out.println(
          JSFTestUtil.FAIL + " ExternalContext.getSessionMap() returned null");
      return;
    }

    String result = validateMutableMap(sessMap, TCK_VALUE);

    if (result != null) {
      out.println("Test FAILED. " + result);
      return;
    }

    if (!TCK_VALUE1.equals(session.getAttribute(TCK_KEY))) {
      out.println(
          "Unable to find expected value for attribute 'tckKey1' in Session");
      out.println("value: " + session.getAttribute(TCK_KEY));
      return;
    }

    // cleanup
    session.removeAttribute(TCK_ATTRIBUTE);
    session.removeAttribute(TCK_KEY);

    out.println(JSFTestUtil.PASS);
  }

  // ExternalContext.getRequestMap()
  public void extContextGetRequestMapTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    request.setAttribute(TCK_ATTRIBUTE, TCK_VALUE);
    Map applMap = getFacesContext().getExternalContext().getRequestMap();

    if (applMap == null) {
      out.println(
          JSFTestUtil.FAIL + " ExternalContext.getRequestMap() returned null");
      return;
    }

    String result = validateMutableMap(applMap, TCK_VALUE);

    if (result != null) {
      out.println("Test FAILED. " + result);
      return;
    }

    if (!TCK_VALUE1.equals(request.getAttribute(TCK_KEY))) {
      out.println(
          "Unable to find expected value for attribute 'tckKey1' in Request");
      out.println("value: " + request.getAttribute(TCK_KEY));
      return;
    }

    // cleanup
    request.removeAttribute(TCK_ATTRIBUTE);
    request.removeAttribute(TCK_KEY);

    out.println(JSFTestUtil.PASS);
  }

  // ExternalContext.getInitParameterMap()
  public void extContextGetInitParameterMapTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Map initMap = getFacesContext().getExternalContext().getInitParameterMap();

    if (initMap == null) {
      out.println(
          JSFTestUtil.FAIL + " ExternalContext.getRequestMap() returned null");
      return;
    }

    String result = validateNonMutableMap(initMap, TCK_VALUE, false);

    if (result != null) {
      out.println("Test FAILED. " + result);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // ExternalContext.getRequestHeaderMap()
  public void extContextGetRequestHeaderMapTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Map headerMap = getFacesContext().getExternalContext()
        .getRequestHeaderMap();

    if (headerMap == null) {
      out.println(JSFTestUtil.FAIL
          + " ExternalContext.getRequestHeaderMap() returned null");
      return;
    }

    String result = validateNonMutableMap(headerMap, TCK_VALUE, true);

    if (result != null) {
      out.println("Test FAILED. " + result);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // ExternalContext.getRequestParameterMap()
  public void extContextGetRequestParameterMapTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Map parameterMap = getFacesContext().getExternalContext()
        .getRequestParameterMap();

    if (parameterMap == null) {
      out.println(JSFTestUtil.FAIL
          + " ExternalContext.getRequestParameterMap() returned null");
      return;
    }

    String result = validateNonMutableMap(parameterMap, TCK_VALUE, false);

    if (result != null) {
      out.println("Test FAILED. " + result);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // ExternalContext.getRequestParameterValuesMap()
  public void extContextGetRequestParameterValuesMapTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Map parameterValuesMap = getFacesContext().getExternalContext()
        .getRequestParameterValuesMap();

    if (parameterValuesMap == null) {
      out.println(JSFTestUtil.FAIL
          + " ExternalContext.getRequestParameterValuesMap() returned null");
      return;
    }

    String result = validateNonMutableMap(parameterValuesMap, TCK_VALUE, false);

    if (result != null) {
      out.println("Test FAILED. " + result);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // ExternalContext.getRequestCookieMap()
  public void extContextGetRequestCookieMapTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Map<String, Object> cookieMap = getFacesContext().getExternalContext()
        .getRequestCookieMap();

    if (cookieMap == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "ExternalContext.getCookieMap() returned null");
      return;
    }

    Cookie[] cookies = request.getCookies();
    Cookie controlCookie = null;
    for (int i = 0; i < cookies.length; i++) {
      if (cookies[i].getName().equals(TCK_ATTRIBUTE)) {
        controlCookie = cookies[i];
        break;
      }
    }

    if (controlCookie == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Expected cookie not found in request.");
      return;
    }

    String result = validateNonMutableMap(cookieMap, controlCookie, false);

    if (result != null) {
      out.println("Test FAILED. " + result);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // ExternalContext.getInitParamter(String)
  public void extContextGetInitParameterTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    String value = getFacesContext().getExternalContext()
        .getInitParameter(TCK_ATTRIBUTE);

    if (!TCK_VALUE.equals(value)) {
      out.println(JSFTestUtil.FAIL + " Expected a value of '" + TCK_VALUE
          + "' for Context init parameter" + " '" + TCK_ATTRIBUTE + "'.");
      out.println("Received: " + value);
      return;
    }
    out.println(JSFTestUtil.PASS);
  }

  // ExternalContext.isUserInRole(String)
  public void extContextIsUserInRoleNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();

    JSFTestUtil.checkForNPE(getFacesContext().getExternalContext(),
        "isUserInRole", new Class<?>[] { String.class }, new Object[] { null },
        pw);

  }// End extContextIsUserInRoleNPETest

  // ExternalContext.log(String)
  // ExternalContext.log(String, Throwable)
  public void extContextLogNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();

    pw.println("Testing: ExternalContext.log(null)");
    JSFTestUtil.checkForNPE(getFacesContext().getExternalContext(), "log",
        new Class<?>[] { String.class }, new Object[] { null }, pw);

    pw.println("Testing: ExternalContext.log(null, Exception)");
    JSFTestUtil.checkForNPE(getFacesContext().getExternalContext(), "log",
        new Class<?>[] { String.class, Throwable.class },
        new Object[] { null, new Exception() }, pw);

    pw.println("Testing: ExternalContext.log(String, null)");
    JSFTestUtil.checkForNPE(getFacesContext().getExternalContext(), "log",
        new Class<?>[] { String.class, Throwable.class },
        new Object[] { "message", null }, pw);

  }// End extContextLogNPETest

  // ExternalContext.getMimeType(String)
  public void extContextGetMimeTypeTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    String fileName = "duke-boxer.gif";
    String expected = "image/gif";
    String value = getFacesContext().getExternalContext().getMimeType(fileName);

    if (!expected.equals(value)) {
      out.println(JSFTestUtil.FAIL + " Unexpected value return from "
          + "ExternalContext.getMimeType(" + fileName + ")" + JSFTestUtil.NL
          + "Expected: " + expected + JSFTestUtil.NL + "Received: " + value
          + JSFTestUtil.NL);
      return;
    }
    out.println(JSFTestUtil.PASS);
  }

  // ExternalContext.getRemoteUser()
  // Security is optional in ServletContainers, so expect null from this
  // method
  public void extContextGetRemoteUserTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    String result = getFacesContext().getExternalContext().getRemoteUser();
    if (result != null) {
      out.println(JSFTestUtil.FAIL
          + " No 'login' performed, yet ExternalContext.getRemoveUser() returned"
          + " a non-null value.");
      out.println("Received: " + result);
      return;
    }
    out.println(JSFTestUtil.PASS);
  }

  // ExternalContext.getResourcePaths()
  public void extContextGetResourcePathsTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Set resourcePaths = servletContext.getResourcePaths("/");
    Set extResourcePaths = getFacesContext().getExternalContext()
        .getResourcePaths("/");
    if (!resourcePaths.equals(extResourcePaths)) {
      out.println(JSFTestUtil.FAIL
          + " The Set returned by ServletContext.getResourcePaths(String),"
          + " was not the same as the Set returned by ExternalContext.getResourcePaths(String).");
      out.println(
          "Expected: " + JSFTestUtil.getAsString(resourcePaths.iterator()));
      out.println(
          "Received: " + JSFTestUtil.getAsString(extResourcePaths.iterator()));
      return;
    }
    out.println(JSFTestUtil.PASS);
  }

  // ExternalContext.getResourceAsStream()
  public void extContextGetResourceAsStreamTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    InputStream in = getFacesContext().getExternalContext()
        .getResourceAsStream("/WEB-INF/web.xml");
    if (in == null) {
      out.println(
          "ExternalContext.getResourceAsStream(\"/WEB-INF/web.xml\") returned null.");
      return;
    }
    out.println(JSFTestUtil.PASS);
  }

  // ExternalContext.getRequestContextPath()
  public void extContextGetRequestContextPathTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    String expectedPath = request.getContextPath();
    String testPath = getFacesContext().getExternalContext()
        .getRequestContextPath();

    if (!expectedPath.equals(testPath)) {
      out.println(JSFTestUtil.FAIL
          + " The value returned by ExternalContext.getRequestContextPath()"
          + " returned a different result than that from ServletRequest.getContextPath().");
      out.println("Expected: " + expectedPath);
      out.println("Received: " + testPath);
      return;
    }
    out.println(JSFTestUtil.PASS);
  }

  // ExternalContext.getRequestLocale()
  public void extContextGetRequestLocaleTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Locale controlLocale = request.getLocale();
    Locale testLocale = getFacesContext().getExternalContext()
        .getRequestLocale();

    if (!controlLocale.equals(testLocale)) {
      out.println(JSFTestUtil.FAIL
          + " The values returned by ExternalContext.getRequestLocale() "
          + "and ServletRequest.getLocale() differ.");
      out.println("Expected: " + controlLocale);
      out.println("Received:" + testLocale);
      return;
    }
    out.println(JSFTestUtil.PASS);
  }

  // ExternalContext.getRequestParameterNames
  public void extContextGetRequestParameterNamesTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    String[] controlNames = JSFTestUtil
        .getAsStringArray(request.getParameterNames());
    String[] testNames = JSFTestUtil.getAsStringArray(
        getFacesContext().getExternalContext().getRequestParameterNames());

    if (controlNames.length != testNames.length) {
      out.println(JSFTestUtil.FAIL
          + " The number of parameter names returned by ServletRequest."
          + "getParameterNames() differs from what was returned by ExternalContext."
          + "getParameterNames().");
      return;
    }

    for (int i = 0; i < controlNames.length; i++) {
      if (Arrays.binarySearch(testNames, controlNames[i]) < 0) {
        out.println(JSFTestUtil.FAIL + " Unable to find the parameter name '"
            + controlNames[i] + "'"
            + " in the set of parameter names returned by ExternalContext.getParameterNames().");
        out.println("ExternalContext.getParameterNames() returned: "
            + JSFTestUtil.getAsString(testNames));
        return;
      }
    }
    out.println(JSFTestUtil.PASS);
  }

  // ExternalContext.getRequestPathInfo()
  public void extContextGetRequestPathInfoTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    String controlPath = request.getPathInfo();
    String testPath = getFacesContext().getExternalContext()
        .getRequestPathInfo();

    if (!controlPath.equals(testPath)) {
      out.println(JSFTestUtil.FAIL
          + " The path info returned by ExternalContext.getRequestPathInfo()"
          + " didn't match the value returned by ServletRequest.getPathInfo().");
      out.println("Expected: " + controlPath);
      out.println("Received: " + testPath);
      return;
    }
    out.println(JSFTestUtil.PASS);
  }

  // ExternalContext.setSessionMaxInactiveInterval(int)
  public void setGetSessionMaxInactiveIntervalTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ExternalContext econtext = getFacesContext().getExternalContext();
    int result;

    ArrayList<Integer> testValues = new ArrayList<Integer>();
    {
      testValues.add(-1);
      testValues.add(0);
      testValues.add(1);
    }

    ListIterator<Integer> litr = testValues.listIterator();
    while (litr.hasNext()) {
      Integer tv = (Integer) litr.next();

      try {
        econtext.setSessionMaxInactiveInterval(tv);
      } catch (Exception e) {
        out.println("Test FAILED. Unexpected Exception when setting "
            + "MaxInactiveInterval to " + tv + JSFTestUtil.NL);
        e.printStackTrace();
      }

      result = econtext.getSessionMaxInactiveInterval();
      if (result != tv) {
        out.println("Test FAILED. " + JSFTestUtil.NL
            + "Unexpected value returned from getSessionMaxInactiveInterval()."
            + JSFTestUtil.NL + "Expected: " + tv + JSFTestUtil.NL
            + "Received:  " + result);
        return;
      }
    }

    out.println(JSFTestUtil.PASS);

  }// End setGetSessionMaxInactiveIntervalTest

  // ------------------------------------------------------ Private Methods
  // --------
  private String validateCommonMethods(Map map, Object value,
      boolean ignoreCase) {

    if (!value.equals(map.get(TCK_ATTRIBUTE))) {
      return "Map.get(\"" + TCK_ATTRIBUTE
          + "\") didn't return the expected value, '" + TCK_VALUE + "'.\n"
          + "  Received: " + map.get(TCK_ATTRIBUTE);
    }

    if (map.get(NO_SUCH_ATTRIBUTE) != null) {
      return "Map.get() for a non-existent attribute returned a non-null value.";
    }

    if (!map.containsKey(TCK_ATTRIBUTE)) {
      return "Map.containsKey(\"" + TCK_ATTRIBUTE + "\") returned false";
    }

    if (map.containsKey(NO_SUCH_ATTRIBUTE)) {
      return "Map.containsKey(Object) returned true for a key that doesn't exist in the map.";
    }

    Set entrySet = map.entrySet();
    if (entrySet == null) {
      return "Map.entrySet() returned null.";
    }

    boolean found = false;
    for (Iterator i = entrySet.iterator(); i.hasNext();) {
      Map.Entry entry = (Map.Entry) i.next();
      boolean test = (ignoreCase)
          ? TCK_ATTRIBUTE.equalsIgnoreCase(entry.getKey().toString())
          : TCK_ATTRIBUTE.equals(entry.getKey());
      if (test) {
        if (value.equals(entry.getValue())) {
          found = true;
          break;
        }
      }
    }

    if (!found) {
      return "Map.entrySet() did not return a Map.Entry for a key/value pair.\n"
          + "Key " + TCK_ATTRIBUTE + ", Value: " + value;
    }

    if (!(map.equals(map))) {
      return "Map.equals(Object) returned false when passing the same map as an argument.";
    }

    if (map.equals(new HashMap())) {
      return "Map.equals(new HashMap()) returned true when the two instances were not equal";
    }

    if (!map.containsValue(value)) {
      return "Map.containsValue(\"" + value + "\") returned false";
    }

    if (map.containsValue(NO_SUCH_VALUE)) {
      return "Map.containsvalue() returned true for a value that doesn't exist in the map.";
    }

    if (map.hashCode() != map.hashCode()) {
      return "Map.hasCode() returned different values each time it was called on the "
          + "same Map instance.";
    }

    if (map.isEmpty()) {
      return "Map.isEmtpy() returned true when values exist in the Map.";
    }

    Set keySet = map.keySet();
    if (!keySet.contains(TCK_ATTRIBUTE)) {
      return "Map.keySet() returned a Set that didn't contain one of the expected keys: "
          + TCK_ATTRIBUTE;
    }

    if (!(map.size() > 0)) {
      return "Map.size() returned a value less than or equal to zero when there are entries in the Map.";
    }

    Collection values = map.values();
    if (!values.contains(value)) {
      return "Map.values() returned a Collection missing one of the values expected to be found in the Map.";
    }

    if (values.contains(NO_SUCH_VALUE)) {
      return "Map.values() returned a Collection with a value that should not have been present";
    }

    return null;
  }

  private String validateMutableMap(Map map, Object value) {
    map.remove(NO_SUCH_ATTRIBUTE);

    String result = validateCommonMethods(map, value, false);
    if (result != null) {
      return result;
    }

    map.put(TCK_KEY, TCK_VALUE1);
    if (!"tckValue1".equals(map.get("tckKey1"))) {
      return "Unable to find value for key 'tckKey1' that was just added to the Map.";
    }

    map.remove(TCK_KEY);
    if (map.get("tckKey1") != null) {
      return "Map.remove() didn't remove the key/value pair from map.";
    }

    map.put(TCK_KEY, TCK_VALUE1);

    return null;
  }

  private String validateNonMutableMap(Map map, Object value,
      boolean ignoreCase) {

    String result = validateCommonMethods(map, value, ignoreCase);
    if (result != null) {
      return result;
    }

    try {
      map.remove(NO_SUCH_ATTRIBUTE);
      return "Map.remove() should not be supported by this Map.";
    } catch (Throwable t) {
      ;
    }

    try {
      map.put("key1", "val1");
      return "Map.put() should not be supported by this Map.";
    } catch (Throwable t) {
      ;
    }

    return null;
  }
}
