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

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import javax.faces.context.ExternalContext;
import javax.faces.context.ExternalContextWrapper;
import javax.faces.context.FacesContext;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Cookie;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.InputStream;
import java.util.*;

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
  public void extContextWrapperDispatchTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    ExternalContextWrapper ecw = new TCKExternalContext();
    ecw.dispatch("/target");
  }

  // ExternalContext.redirect(String)
  public void extContextWrapperRedirectTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    ExternalContextWrapper ecw = new TCKExternalContext();
    ecw.redirect("/target");
  }

  // ExternalContext.getSession(boolean)
  public void extContextWrapperGetSessionTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ExternalContextWrapper context = new TCKExternalContext();

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
  public void extContextWrapperGetContextTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ExternalContextWrapper ecw = new TCKExternalContext();
    Object context = ecw.getContext();

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
  public void extContextWrapperGetRequestTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    request.setAttribute(TCK_ATTRIBUTE, TCK_VALUE);
    ExternalContextWrapper ecw = new TCKExternalContext();
    Object req = ecw.getRequest();

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
  public void extContextWrapperGetResponseTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    response.addHeader(TCK_ATTRIBUTE, TCK_VALUE);
    ExternalContextWrapper ecw = new TCKExternalContext();
    Object res = ecw.getResponse();

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
  public void extContextWrapperGetApplicationMapTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    servletContext.setAttribute(TCK_ATTRIBUTE, TCK_VALUE);
    ExternalContextWrapper ecw = new TCKExternalContext();
    Map<String, Object> applMap = ecw.getApplicationMap();

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
  public void extContextWrapperGetSessionMapTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    HttpSession session = request.getSession(true);
    session.setAttribute(TCK_ATTRIBUTE, TCK_VALUE);
    ExternalContextWrapper ecw = new TCKExternalContext();
    Map<String, Object> sessMap = ecw.getSessionMap();

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
  public void extContextWrapperGetRequestMapTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    request.setAttribute(TCK_ATTRIBUTE, TCK_VALUE);
    ExternalContextWrapper ecw = new TCKExternalContext();
    Map<String, Object> applMap = ecw.getRequestMap();

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
  public void extContextWrapperGetInitParameterMapTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ExternalContextWrapper ecw = new TCKExternalContext();
    Map<?, ?> initMap = ecw.getInitParameterMap();

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
  public void extContextWrapperGetRequestHeaderMapTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ExternalContextWrapper ecw = new TCKExternalContext();
    Map<String, String> headerMap = ecw.getRequestHeaderMap();

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
  public void extContextWrapperGetRequestParameterMapTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ExternalContextWrapper ecw = new TCKExternalContext();
    Map<String, String> parameterMap = ecw.getRequestParameterMap();

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
  public void extContextWrapperGetRequestParameterValuesMapTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ExternalContextWrapper ecw = new TCKExternalContext();
    Map<String, String[]> parameterValuesMap = ecw
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
  public void extContextWrapperGetRequestCookieMapTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ExternalContextWrapper ecw = new TCKExternalContext();
    Map<String, Object> cookieMap = ecw.getRequestCookieMap();

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
  public void extContextWrapperGetInitParameterTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ExternalContextWrapper ecw = new TCKExternalContext();
    String value = ecw.getInitParameter(TCK_ATTRIBUTE);

    if (!TCK_VALUE.equals(value)) {
      out.println(JSFTestUtil.FAIL + " Expected a value of '" + TCK_VALUE
          + "' for Context init parameter" + " '" + TCK_ATTRIBUTE + "'."
          + JSFTestUtil.NL + "Received: " + value);

    } else {
      out.println(JSFTestUtil.PASS);
    }
  }

  // ExternalContext.getMimeType(String)
  public void extContextWrapperGetMimeTypeTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    String fileName = "duke-boxer.gif";
    String expected = "image/gif";
    ExternalContextWrapper ecw = new TCKExternalContext();
    String value = ecw.getMimeType(fileName);

    if (!expected.equals(value)) {
      out.println(JSFTestUtil.FAIL + " Unexpected value return from "
          + "ExternalContext.getMimeType(" + fileName + ")" + JSFTestUtil.NL
          + "Expected: " + expected + JSFTestUtil.NL + "Received: " + value
          + JSFTestUtil.NL);

    } else {
      out.println(JSFTestUtil.PASS);
    }
  }

  // ExternalContext.getRemoteUser()
  // Security is optional in ServletContainers, so expect null from this method
  public void extContextWrapperGetRemoteUserTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ExternalContextWrapper ecw = new TCKExternalContext();
    String result = ecw.getRemoteUser();

    if (result != null) {
      out.println(JSFTestUtil.FAIL
          + " No 'login' performed, yet ExternalContext.getRemoveUser() returned"
          + " a non-null value." + JSFTestUtil.NL + "Received: " + result);

    } else {
      out.println(JSFTestUtil.PASS);
    }
  }

  // ExternalContext.getResourcePaths()
  public void extContextWrapperGetResourcePathsTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Set<String> resourcePaths = servletContext.getResourcePaths("/");
    ExternalContextWrapper ecw = new TCKExternalContext();
    Set<String> extResourcePaths = ecw.getResourcePaths("/");

    if (!resourcePaths.equals(extResourcePaths)) {
      out.println(JSFTestUtil.FAIL
          + " The Set returned by ServletContext.getResourcePaths(String),"
          + " was not the same as the Set returned by ExternalContext.getResourcePaths(String)."
          + JSFTestUtil.NL + "Expected: "
          + JSFTestUtil.getAsString(resourcePaths.iterator()) + "Received: "
          + JSFTestUtil.getAsString(extResourcePaths.iterator()));

    } else {
      out.println(JSFTestUtil.PASS);
    }

  }

  // ExternalContext.getResourceAsStream()
  public void extContextWrapperGetResourceAsStreamTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ExternalContextWrapper ecw = new TCKExternalContext();
    InputStream in = ecw.getResourceAsStream("/WEB-INF/web.xml");

    if (in == null) {
      out.println(
          "ExternalContext.getResourceAsStream(\"/WEB-INF/web.xml\") returned null.");
    } else {
      out.println(JSFTestUtil.PASS);
    }

  }

  // ExternalContext.getRequestContextPath()
  public void extContextWrapperGetRequestContextPathTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    String expectedPath = request.getContextPath();
    ExternalContextWrapper ecw = new TCKExternalContext();
    String testPath = ecw.getRequestContextPath();

    if (!expectedPath.equals(testPath)) {
      out.println(JSFTestUtil.FAIL
          + " The value returned by ExternalContext.getRequestContextPath()"
          + " returned a different result than that from ServletRequest.getContextPath()."
          + JSFTestUtil.NL + "Expected: " + expectedPath + JSFTestUtil.NL
          + "Received: " + testPath);

    } else {
      out.println(JSFTestUtil.PASS);
    }

  }

  // ExternalContext.getRequestLocale()
  public void extContextWrapperGetRequestLocaleTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Locale controlLocale = request.getLocale();
    ExternalContextWrapper ecw = new TCKExternalContext();
    Locale testLocale = ecw.getRequestLocale();

    if (!controlLocale.equals(testLocale)) {
      out.println(JSFTestUtil.FAIL + " The values returned by "
          + "ExternalContext.getRequestLocale() and ServletRequest.getLocale() differ."
          + JSFTestUtil.NL + "Expected: " + controlLocale + JSFTestUtil.NL
          + "Received:" + testLocale);

    } else {
      out.println(JSFTestUtil.PASS);
    }
  }

  // ExternalContext.getRequestParameterNames
  public void extContextWrapperGetRequestParameterNamesTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ExternalContextWrapper ecw = new TCKExternalContext();

    String[] controlNames = JSFTestUtil
        .getAsStringArray(request.getParameterNames());

    String[] testNames = JSFTestUtil
        .getAsStringArray(ecw.getRequestParameterNames());

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
            + " in the set of parameter names returned by "
            + "ExternalContext.getParameterNames()." + JSFTestUtil.NL
            + "ExternalContext.getParameterNames() returned: "
            + JSFTestUtil.getAsString(testNames));
        return;
      }
    }
    out.println(JSFTestUtil.PASS);
  }

  // ExternalContext.getRequestPathInfo()
  public void extContextWrapperGetRequestPathInfoTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    String controlPath = request.getPathInfo();
    ExternalContextWrapper ecw = new TCKExternalContext();
    String testPath = ecw.getRequestPathInfo();

    if (!controlPath.equals(testPath)) {
      out.println(JSFTestUtil.FAIL
          + " The path info returned by ExternalContext.getRequestPathInfo()"
          + " didn't match the value returned by ServletRequest.getPathInfo()."
          + JSFTestUtil.NL + "Expected: " + controlPath + JSFTestUtil.NL
          + "Received: " + testPath);

    } else {
      out.println(JSFTestUtil.PASS);
    }
  }

  // ExternalContext.setSessionMaxInactiveInterval(int)
  public void extContextWrapperSetGetSessionMaxInactiveIntervalTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ExternalContextWrapper econtext = new TCKExternalContext();
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

  }// End extContextWrapperSetGetSessionMaxInactiveIntervalTest

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

  // --------------------------------------------------------- private classes

  private static class TCKExternalContext extends ExternalContextWrapper {

    @Override
    public ExternalContext getWrapped() {
      return FacesContext.getCurrentInstance().getExternalContext();
    }

  } // End TCKExternalContext

}
