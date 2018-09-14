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

package com.sun.ts.tests.jsp.common.util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.*;
import javax.servlet.jsp.el.ELException;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.io.IOException;
import java.util.*;

/**
 * JSP TCK Utility class.
 */

public class JspTestUtil {

  /**
   * Flag to enabled the printing of debug statements.
   */
  public static boolean DEBUG = true;

  /**
   * <code>TEST_HEADER</code> is the constant for the <code>testname</code>
   * header.
   */
  private static final String TEST_HEADER = "testname";

  /**
   * <code>TEST_ARGS</code> is an array of Classes used during reflection.
   */
  private static final Class[] TEST_ARGS = { HttpServletRequest.class,
      HttpServletResponse.class, JspWriter.class };

  /**
   * Private as this class will only have static methods and members.
   */
  private JspTestUtil() {
  }

  /**
   * Utility method to invoke JSP API Tests.
   * 
   * @param testObject
   *          - the JSP test object
   * @param req
   *          - the incoming client request
   * @param res
   *          - response to the client
   * @param out
   *          - used to write the response
   * @throws ServletException
   *           - if an error occurs
   */
  public static void invokeTest(Object testObject, HttpServletRequest req,
      HttpServletResponse res, JspWriter out) throws ServletException {
    String test = req.getParameter(TEST_HEADER);
    debug("[JspTestUtil] Test to invoke: " + test);
    try {
      Method method = testObject.getClass().getMethod(test, TEST_ARGS);
      method.invoke(testObject, new Object[] { req, res, out });
    } catch (InvocationTargetException ite) {
      throw new ServletException(ite.getTargetException().toString());
    } catch (NoSuchMethodException nsme) {
      throw new ServletException("Test: " + test + " does not exist");
    } catch (Throwable t) {
      throw new ServletException("Error executing test: " + test, t);
    }
  }

  /**
   * Utility method to handle exceptions from tests in a generic fashion.
   * 
   * @param t
   *          - offending Throwable instance
   * @param out
   *          - JspWriter to write the error info to
   * @param identifier
   *          - a test name or other logical identifier
   * @throws IOException
   *           if an I/O error occurs when writing to the stream.
   */
  public static void handleThrowable(Throwable t, JspWriter out,
      String identifier) throws IOException {
    out.println("Test FAILED.  " + identifier + " Unexpected Throwable caught");
    out.println("Type: " + t.getClass().getName());
    out.println("Message: " + t.getMessage());
    if (t instanceof ELException) {
      Throwable sub = ((ELException) t).getRootCause();
      if (sub != null) {
        out.println("Root Cause: " + sub);
        out.println("Root Cause Message: " + sub.getMessage());
      }
    }
  }

  /**
   * Compares the String values in an Enumeration against the provides String
   * array of values. The number of elements in the enumeration must be the same
   * as the size of the array, or false will be returned. False will also be
   * returned if the provided Enumeration or String array is null.
   *
   * If all values are found, true will be returned.
   *
   * The comparison is performed in a case sensitive manner.
   * 
   * @param e
   *          - Enumeration to validate
   * @param values
   *          - the values expected to be found in the Enumeration
   * @return true if all the expected values are found, otherwise false.
   */
  public static boolean checkEnumeration(Enumeration e, String[] values) {
    if (e == null || values == null) {
      return false;
    }

    boolean valuesFound = true;
    Arrays.sort(values);
    for (int i = 0; i < values.length; i++) {
      String val;
      try {
        val = (String) e.nextElement();
      } catch (NoSuchElementException nsee) {
        debug("[JspTestUtil] There were less elements in the "
            + "Enumeration than expected");
        valuesFound = false;
        break;
      }
      debug("[JspTestUtil] Looking for '" + val + "' in values: "
          + getAsString(values));
      if (Arrays.binarySearch(values, val) < 0) {
        debug("[JspTestUtil] Value '" + val + "' not found.");
        valuesFound = false;
        break;
      }
    }
    if (e.hasMoreElements()) {
      // more elements than should have been.
      debug("[JspTestUtil] There were more elements in the Enumeration "
          + "than expected.");
      valuesFound = false;
    }

    return valuesFound;
  }

  /**
   * Returns the provided String array in the following format:
   * <tt>[n1,n2,n...]</tt>
   * 
   * @param sArray
   *          - an array of String values
   * @return - a String based off the values in the array
   */
  public static String getAsString(String[] sArray) {
    if (sArray == null) {
      return null;
    }
    StringBuffer buf = new StringBuffer();
    buf.append("[");
    for (int i = 0; i < sArray.length; i++) {
      buf.append(sArray[i]);
      if ((i + 1) != sArray.length) {
        buf.append(",");
      }
    }
    buf.append("]");
    return buf.toString();
  }

  /**
   * Returns the provided Enumeration as a String in the following format:
   * <tt>[n1,n2,n...]</tt>
   * 
   * @param e
   *          - an Enumeration
   * @return - a printable version of the contents of the Enumeration
   */
  public static String getAsString(Enumeration e) {
    return getAsString(getAsStringArray(e));
  }

  /**
   * Returnes the provides Enumeration as an Array of String Arguments.
   * 
   * @param e
   *          - an Enumeration
   * @return - the elements of the Enumeration as an array of Strings
   */
  public static String[] getAsStringArray(Enumeration e) {
    List list = new ArrayList();
    while (e.hasMoreElements()) {
      list.add(e.nextElement());
    }
    return (String[]) list.toArray(new String[list.size()]);
  }

  public static String[] getAsStringArray(Iterator i) {
    List list = new ArrayList();
    while (i.hasNext()) {
      Object o = i.next();
      if (o instanceof String[]) {
        o = getAsString((String[]) o);
      }
      list.add(o);
    }
    return (String[]) list.toArray(new String[list.size()]);
  }

  public static String[] getAsStringArray(String s) {
    StringTokenizer st = new StringTokenizer(s, ",");
    List list = new ArrayList();
    while (st.hasMoreTokens()) {
      list.add(st.nextToken());
    }
    return (String[]) list.toArray(new String[list.size()]);
  }

  /**
   * Writes the provided message to System.out when the <tt>debug</tt> is set.
   * 
   * @param message
   *          - the message to write to System.out
   */
  public static void debug(String message) {
    if (DEBUG) {
      System.out.println(message);
    }
  }

  /**
   * Creates an array of ValidationMessages.
   * 
   * @param id
   *          - the jsp:id
   * @param message
   *          - the error message
   * @return an arrary of ValidationMessages, or null if message is null.
   */
  public static ValidationMessage[] getValidationMessage(String id,
      String message) {
    if (message == null) {
      return null;
    } else {
      return new ValidationMessage[] { new ValidationMessage(id, message) };
    }
  }

  /**
   * <p>
   * Returns a String representation of the {@link Map} provided.
   * </p>
   *
   * @param map
   *          input map
   * @return String representation of the Map
   */
  public static String getAsString(Map map) {
    StringBuffer sb = new StringBuffer(32);
    Set entrySet = map.entrySet();
    sb.append("Map Entries\n----------------\n");
    for (Iterator i = entrySet.iterator(); i.hasNext();) {
      Map.Entry entry = (Map.Entry) i.next();
      sb.append(entry.getKey()).append(", ");
      sb.append(entry.getValue() instanceof Cookie
          ? ((Cookie) entry.getValue()).getValue()
          : entry.getValue());
      sb.append('\n');
    }
    sb.append('\n');
    return sb.toString();
  }

  /**
   * Utility class to get a String version of a Tag return value based on the
   * int value, the type, and the method it was returned from.
   * 
   * @param method
   *          - the Tag method returning the int status
   * @param type
   *          - the tag interface type (i.e. Tag, IterationTag)
   * @param status
   *          - the return value from the method
   * @return a String representation of the information provided
   */
  public static String getTagStatusAsString(String method, String type,
      int status) {
    String value = "UNIDENTIFIED VALUE";
    if ("doStartTag".equals(method)) {
      switch (status) {
      case Tag.EVAL_BODY_INCLUDE:
        value = "EVAL_BODY_INCLUDE";
        break;
      case Tag.SKIP_BODY:
        value = "SKIP_BODY";
        break;
      }
    } else if ("doEndTag".equals(method)) {
      switch (status) {
      case Tag.SKIP_PAGE:
        value = "SKIP_PAGE";
        break;
      case Tag.EVAL_PAGE:
        value = "EVAL_PAGE";
        break;
      }
    } else if ("doAfterBody".equals(method) && "IterationTag".equals(type)) {
      switch (status) {
      case Tag.SKIP_BODY:
        value = "SKIP_BODY";
        break;
      case IterationTag.EVAL_BODY_AGAIN:
        value = "EVAL_BODY_AGAIN";
        break;
      }
    } else if ("doAfterBody".equals(method) && "BodyTag".equals(type)) {
      switch (status) {
      case Tag.SKIP_BODY:
        value = "SKIP_BODY";
        break;
      case BodyTag.EVAL_BODY_BUFFERED:
        value = "EVAL_BODY_BUFFERED";
        break;
      }
    }
    return value;
  }

  public static String getScopeName(int scope) {
    final String UNKNOWN_SCOPE = "Unknown Scope";
    switch (scope) {
    case PageContext.PAGE_SCOPE:
      return "Page";
    case PageContext.REQUEST_SCOPE:
      return "Request";
    case PageContext.SESSION_SCOPE:
      return "Session";
    case PageContext.APPLICATION_SCOPE:
      return "Application";
    default:
      return UNKNOWN_SCOPE;
    }
  }
}
