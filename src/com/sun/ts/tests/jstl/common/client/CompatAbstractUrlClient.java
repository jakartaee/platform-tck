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

package com.sun.ts.tests.jstl.common.client;

import java.util.Properties;
import java.util.Enumeration;
import com.sun.ts.tests.jstl.common.JstlTckConstants;
import com.sun.ts.tests.common.webclient.BaseUrlClient;
import com.sun.ts.tests.common.webclient.WebTestCase;
import com.sun.ts.tests.common.webclient.http.HttpRequest;

public class CompatAbstractUrlClient extends BaseUrlClient
    implements JstlTckConstants {

  protected Properties dbArgs = new Properties();

  protected static final String STANDARD_COMPAT = "standardCompat";

  public void setup(String[] args, Properties p) throws Fault {
    for (int i = 0; i < JSTL_DB_PROPS.length; i++) {
      String s = p.getProperty(JSTL_DB_PROPS[i]);
      if (s != null) {
        dbArgs.setProperty(JSTL_DB_PROPS[i], s.trim());
      }
    }
    super.setup(args, p);
  }

  /**
   * Sets the test properties for this testCase.
   *
   * @param testCase
   *          - the current test case
   */
  protected void setTestProperties(WebTestCase testCase) {
    setStandardProperties(TEST_PROPS.getProperty(STANDARD_COMPAT), testCase);
    if (testCase.getRequest() == null) {
      String test = TEST_PROPS.getProperty(REQUEST);
      if (test.indexOf("HTTP/") < 0) {
        StringBuffer sb = new StringBuffer(25);
        sb.append(GET).append(getContextRoot()).append(SL).append(test)
            .append(HTTP11);
        HttpRequest req = new HttpRequest(sb.toString(), _hostname, _port);
        testCase.setRequest(req);
      }
    }

    super.setTestProperties(testCase);
    HttpRequest httpReq = testCase.getRequest();
    Enumeration enumm = dbArgs.propertyNames();
    for (; enumm.hasMoreElements();) {
      String name = (String) enumm.nextElement();
      String value = dbArgs.getProperty(name);
      httpReq.addRequestHeader(name, value);
    }
  }

  /**
   * Sets the goldenfile directory
   * 
   * @param goldenDir
   *          goldenfile directory based off test directory
   */
  public void setGoldenFileDir(String goldenDir) {
    GOLDENFILEDIR = goldenDir;
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

    // A standard test sets consists of a testname
    // a request, and a goldenfile
    StringBuffer sb = new StringBuffer(50);

    // set the testname
    _testName = testValue;

    // set the request
    sb.append(GET).append(_contextRoot).append(SL);
    sb.append(testValue).append(JSP_SUFFIX).append(HTTP11);
    // setRequest(sb.toString());
    HttpRequest req = new HttpRequest(sb.toString(), _hostname, _port);
    testCase.setRequest(req);

    // set the goldenfile
    sb = new StringBuffer(50);
    sb.append(_tsHome).append(GOLDENFILEDIR);
    sb.append(_generalURI).append(SL);
    sb.append(testValue).append(GF_SUFFIX);
    testCase.setGoldenFilePath(sb.toString());
  }

}
