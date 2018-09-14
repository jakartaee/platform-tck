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

package com.sun.ts.tests.common.web;

import java.util.Properties;
import java.util.Enumeration;
import java.lang.reflect.Method;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.TSNamingContext;

/**
 * Factorize code used by JSP and Servlets.
 *
 * @see com.sun.ts.tests.common.web.ServletWrapper
 * @see com.sun.ts.common.util.TestUtil
 */
public class WebUtil {

  /** Name of property used to send back test result to client */
  public static final String RESULT_PROP = "ctsWebTestResult";

  /** Name of the property used to get the test method name */
  public static final String TEST_NAME_PROP = "ctsWebTestName";

  public static void logMsg(String msg) {
    TestUtil.logMsg(msg);
    System.out.println(msg);
  }

  public static void logTrace(String msg) {
    TestUtil.logTrace(msg);
    System.out.println(msg);
  }

  public static void logErr(String msg) {
    TestUtil.logErr(msg);
    System.out.println(msg);
  }

  public static void logErr(String msg, Exception e) {
    TestUtil.logErr(msg, e);
    System.out.println(msg + " : " + e);
    e.printStackTrace();
  }

  /**
   * Use information provided in a HTTPRequest to execute a test method on a
   * testDriver object. The test method is "discovered" at runtime using the
   * reflection API. The testDriver could be any java object.
   */
  public static Properties executeTest(Object testDriver, TSNamingContext nctx,
      HttpServletRequest req) throws ServletException {

    Boolean pass = Boolean.FALSE;
    Properties webProps = null;
    Class testDriverClass;
    String testName;
    Method testMethod;
    Class params[] = { Properties.class };
    Object args[] = new Object[1];

    logTrace("[WebUtil] executeTestOnInstance()");

    /*
     * Get harness properties and initialize logging.
     */
    try {
      logTrace("[WebUtil] Retrieving harness props...");

      webProps = new Properties();
      Enumeration names = req.getParameterNames();
      while (names.hasMoreElements()) {
        String name = (String) names.nextElement();
        String value = req.getParameter(name);
        webProps.setProperty(name, value);
      }

      logTrace("[WebUtil] Initializing Remote Logging...");
      TestUtil.init(webProps);
    } catch (Exception e) {
      System.out.println("Exception: " + e);
      e.printStackTrace();
      throw new ServletException("Cannot Initialize CST Logging");
    }

    /*
     * Get name of the test method.
     */
    try {
      logTrace("[WebUtil] Getting test name...");
      testName = webProps.getProperty(TEST_NAME_PROP);
    } catch (Exception e) {
      logErr("[WebUtil] Unexpected exception", e);
      throw new ServletException("Cannot get test name");
    }
    if (null == testName || testName.equals("")) {
      logErr("[WebUtil] Invalid test name : " + testName);
      throw new ServletException("Invalid test name");
    }

    /*
     * Invoke test method on current instance.
     */
    try {
      logTrace("[WebUtil] Invoke test '" + testName + "'");
      testDriverClass = testDriver.getClass();
      testMethod = testDriverClass.getMethod(testName, params);
      args[0] = webProps;
      pass = (Boolean) testMethod.invoke(testDriver, args);
    } catch (NoSuchMethodException e) {
      logErr("[WebUtil] Cannot find method '" + testName
          + "' make sure it is defined in sub-class", e);
      throw new ServletException("Test method is not defined");
    } catch (java.lang.reflect.InvocationTargetException e) {
      logErr("[WebUtil] InvocationTargetException '" + testName + "' caused by "
          + e.getTargetException());
      throw new ServletException("Test method is not defined");
    } catch (Exception e) {
      logErr("[WebUtil] Unexpected exception", e);
      throw new ServletException("Cannot Invoke test method");
    }

    /*
     * Set test result and send all properties back to client.
     */
    webProps.setProperty(RESULT_PROP, pass.toString());

    return webProps;
  }

  /**
   * Convert Java Properties to a String, as expected by on the client side
   * (WebServer)
   */
  public static String propsToString(Properties props) {
    StringBuffer sb = new StringBuffer(1000);
    Enumeration keys;
    String name;

    if (null == props) {
      throw new IllegalArgumentException("null props!");
    }

    keys = props.keys();
    while (keys.hasMoreElements()) {
      name = (String) keys.nextElement();
      sb.append(name + "=" + props.getProperty(name) + "\n");
    }

    return sb.toString();
  }

}
