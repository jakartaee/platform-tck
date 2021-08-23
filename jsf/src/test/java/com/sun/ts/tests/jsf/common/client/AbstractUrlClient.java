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
package com.sun.ts.tests.jsf.common.client;

import com.sun.ts.tests.common.webclient.BaseUrlClient;
import com.sun.ts.tests.common.webclient.WebTestCase;
import com.sun.ts.tests.common.webclient.http.HttpRequest;

/**
 * Base client for JSF tests.
 */
public abstract class AbstractUrlClient extends BaseUrlClient {

  protected static final String APITEST = "apitest";

  protected static final String DONOTUSEServletName = "NoServletName";

  protected static final String DEFAULT_SERVLET_NAME = "TestServlet";

  private String myLocale;

  private static final String JSF_USER_AGENT = "JavaServer Faces TCK/2.2";

  private String _servlet = null;

  private String myCharacterSet;

  protected void setTestProperties(WebTestCase testCase) {

    setStandardProperties(TEST_PROPS.getProperty(STANDARD), testCase);
    setApiTestProperties(TEST_PROPS.getProperty(APITEST), testCase);
    super.setTestProperties(testCase);
  }

  /**
   * Sets the request, testname, and a search string for test passed. A search
   * is also added for test failure. If found, the test will fail.
   *
   * @param testValue
   *          - a logical test identifier
   * @param testCase
   *          - the current test case
   */
  private void setApiTestProperties(String testValue, WebTestCase testCase) {
    if (testValue == null) {
      return;
    }

    // An API test consists of a request with a request parameter of
    // testname, a search string of Test PASSED, and a logical test name.

    // set the testname
    _testName = testValue;

    // set the request
    StringBuffer sb = new StringBuffer(50);
    if ((_servlet != null)
        && (TEST_PROPS.getProperty(DONOTUSEServletName) == null)) {
      sb.append(GET).append(_contextRoot).append(SL);
      sb.append(_servlet).append("?testname=").append(testValue);
      sb.append(HTTP10);
    } else {
      sb.append(GET).append(_contextRoot).append(SL);
      sb.append(testValue).append(HTTP10);
    }
    System.out.println("REQUEST LINE: " + sb.toString());

    HttpRequest req = new HttpRequest(sb.toString(), _hostname, _port);
    setUserAgent(req);

    // Set Locale if provide.
    if (this.getMyLocale() != null) {
      this.setUserLocale(req);
    }

    // Set charset on Content-Type if provided.
    if (this.getMyCharacterSet() != null) {
      this.setUserCharacterSet(req);
    }

    testCase.setRequest(req);

    if ((TEST_PROPS.getProperty(SEARCH_STRING) == null)
        || ((TEST_PROPS.getProperty(SEARCH_STRING)).equals(""))) {
      testCase.setResponseSearchString("Test PASSED");
      testCase.setUnexpectedResponseSearchString("Test FAILED");
    }

  }

  /**
   * Consists of a test name, a request, and a goldenfile.
   * 
   * @param testValue
   *          - a logical test identifier
   * @param testCase
   *          - the current test case
   */
  private void setStandardProperties(String testValue, WebTestCase testCase) {

    if (testValue == null) {
      return;
    }
    // A standard test sets consists of a testname
    // a request, and a goldenfile. The URI is not used
    // in this case since the JSP's are assumed to be located
    // at the top of the contextRoot
    StringBuffer sb = new StringBuffer(50);

    // set the testname
    _testName = testValue;

    // set the request
    // sb.append(GET).append(_contextRoot).append(SL);
    // sb.append(testValue).append(JSP_SUFFIX).append(HTTP10);
    // setRequest(sb.toString());
    // HttpRequest req = new HttpRequest(sb.toString(), _hostname, _port);
    // testCase.setRequest(req);

    if (_servlet != null) {
      sb.append(GET).append(_contextRoot).append(SL);
      sb.append(_servlet).append("?testname=").append(testValue);
      sb.append(HTTP11);
    } else {
      sb.append(GET).append(_contextRoot).append(SL);
      sb.append(testValue).append(HTTP10);
    }
    System.out.println("REQUEST LINE: " + sb.toString());
    HttpRequest req = new HttpRequest(sb.toString(), _hostname, _port);
    setUserAgent(req);
    testCase.setRequest(req);

    // set the goldenfile
    sb = new StringBuffer(50);
    sb.append(_tsHome).append(GOLDENFILEDIR);
    sb.append(_generalURI).append(SL);
    sb.append(testValue).append(GF_SUFFIX);
    testCase.setGoldenFilePath(sb.toString());
  }

  /**
   * Sets the name of the servlet to use when building a request for a single
   * jsf API test.
   * 
   * @param servlet
   *          - the name of the servlet
   */
  protected void setServletName(String servlet) {
    _servlet = servlet;
  }

  protected String getServletName() {
    return _servlet;
  }

  private void setUserAgent(HttpRequest request) {
    request.addRequestHeader("User-Agent", JSF_USER_AGENT);
  }

  public String getMyLocale() {
    return myLocale;
  }

  /**
   * Sets the locale for a given request.
   * 
   * @param myLocale
   *          - the Locale that you would like to set for the request header.
   */
  public void setMyLocale(String myLocale) {
    this.myLocale = myLocale;
  }

  private void setUserLocale(HttpRequest request) {
    request.addRequestHeader("Accept-Language", myLocale);
  }

  /**
   * Provides the value for the 'charset' attribute in the Content-Type header.
   * 
   * @param myCharacterSet
   *          the myCharacterSet to set
   */
  public void setMyCharacterSet(String myCharacterSet) {
    this.myCharacterSet = myCharacterSet;
  }

  private void setUserCharacterSet(HttpRequest request) {
    request.addRequestHeader("Content-Type", myCharacterSet);
  }

  public String getMyCharacterSet() {
    return myCharacterSet;
  }
}
