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

package com.sun.ts.tests.jsp.spec.core_syntax.scripting.el;

import com.sun.ts.tests.jsp.common.util.JspTestUtil;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.http.Cookie;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Tag implementation to perform validation of the implicit objects provided by
 * the JSP 2.0 expression language.
 */
public class CheckObjectTag extends BaseCheckTag {

  /**
   * Validates the following:
   * <ul>
   * <li>If the object under test is an instance of PageContext, make sure it's
   * the same PageContext as the control object.</li>
   * <li>All other implicit objects aside from pageContext must be instances of
   * java.util.Map. Validate the keys and values based on the what is contained
   * in the control object provided.</li>
   * </ul>
   * 
   * @throws JspException
   *           - if an error occurs
   */
  protected void performCheck() throws JspException {
    JspWriter out = pageContext.getOut();
    try {
      if (_object instanceof PageContext) {
        if (_object == _control) {
          out.println("Test PASSED");
        } else {
          out.println("Test FAILED.  The PageContext implicit object,"
              + "is not the same PageContext as that of the current Page.");
        }
      } else {
        // We're dealing with what should be a Map object
        if (_object instanceof Map) {
          Map control = getMapFromString((String) _control);
          out.println(compareMaps(_name, control, (Map) _object));
        } else {
          out.println("The implicit object, '" + _name + "' is not an"
              + " instance of Map as required by the specification");
        }
      }
    } catch (IOException ioe) {
      throw new JspException("Unexpected IOException!", ioe);
    }
  }

  /**
   * Creates a Map object from the provide string. The String can take the
   * following forms:
   * <ul>
   * <li>key,value,key1,value1</li>
   * <li>key,[value1:value2],key2,value2
   * <ul>
   * <p/>
   * In the first case, the string is treated as a comma separated list where
   * each odd numbered element represents a key, and the even elements represent
   * the value of the key. In the second case, the string is again treated as a
   * comma separated list, but if the value is surrounded with '[' and ']' then
   * the string contained between the brackets is parsed and an String array is
   * created. NOTE: the separator for the array elements is ':'.
   * 
   * @param s
   *          - the string to create a Map instance from
   * @return a map based on the provided String
   */
  private static Map getMapFromString(String s) {
    Map map = new HashMap();
    for (StringTokenizer st = new StringTokenizer(s, ","); st
        .hasMoreTokens();) {
      String key = st.nextToken();
      String value = st.nextToken();
      int length = value.length();
      if (value.charAt(0) == '[' && value.charAt(length - 1) == ']') {
        debug("Creating String array for values: " + value);
        value = value.substring(1, length - 1);
        List list = new ArrayList();
        for (StringTokenizer st1 = new StringTokenizer(value, ":"); st1
            .hasMoreTokens();) {
          list.add(st1.nextToken());
        }
        map.put(key, list.toArray(new String[list.size()]));
      } else {
        map.put(key, value);
      }
    }
    return map;
  }

  /**
   * Compares the test mape to the control map. The test map must contain the
   * keys and values defined in the control map.
   * 
   * @param name
   *          - the name of the implicit object currently under test
   * @param control
   *          - the map of keys and values the implicit object must contain
   * @param testMap
   *          - the implicit object provided by the container
   * @return a message containing information about the success or failure of
   *         the comparison.
   */
  private static String compareMaps(String name, Map control, Map testMap) {
    // Verify all the keys in the control map exist in the
    // test map.

    for (Iterator i = control.entrySet().iterator(); i.hasNext();) {
      Map.Entry controlEntry = (Map.Entry) i.next();
      String controlKey = (String) controlEntry.getKey();
      Object controlValue = controlEntry.getValue();
      boolean found = false;

      for (Iterator ii = testMap.entrySet().iterator(); ii.hasNext();) {
        Map.Entry testEntry = (Map.Entry) ii.next();
        String testKey = (String) testEntry.getKey();
        Object testValue = testEntry.getValue();
        boolean keyFound = false;
        // Compare the Keys. If the implicit object under test
        // is 'header' or 'headerValues' do a case insensitive
        // comparison of the keys.
        if (name.equals("header") || name.equals("headerValues")) {

          if (controlKey.equalsIgnoreCase(testKey)) {
            keyFound = true;
          }
        } else {
          if (controlKey.equals(testKey)) {
            keyFound = true;
          }
        }

        if (keyFound) {
          // found a match on the keys...verify the values
          // are equal
          if (controlValue instanceof String[]) {
            String[] cValueArray = (String[]) controlValue;
            String[] tValueArray = (String[]) testValue;
            Arrays.sort(tValueArray);
            Arrays.sort(cValueArray);
            if (!Arrays.equals(cValueArray, tValueArray)) {
              return "Test FAILED.  Unexpected Value returned" + " for key '"
                  + controlKey + "' in the " + name
                  + "implicit object.\nExpected value" + " to be "
                  + JspTestUtil.getAsString(cValueArray) + " but received "
                  + JspTestUtil.getAsString(tValueArray);
            } else {
              found = true;
            }
          } else if (testValue instanceof Cookie) {
            Cookie cookie = (Cookie) testValue;
            if (!controlValue.equals(cookie.getValue())) {
              return "Test FAILED. Unable to find Cookie in Map"
                  + " returned by implementation with value of" + " '"
                  + controlValue + "'.  Name/Values contained"
                  + " in Map returned by container: "
                  + JspTestUtil.getAsString(testMap);
            } else {
              found = true;
            }
          } else {
            if (!controlValue.equals(testValue)) {
              return "Test FAILED.  Unexpected Value returned" + " for key '"
                  + controlKey + "' in the " + name
                  + "implicit object.\nExpected value" + " to be "
                  + controlValue + " but received " + testValue;
            } else {
              found = true;
            }
          }
        }
      }

      if (!found) {
        return "Test FAILED.  Unable to find matching entry.\nKey: "
            + controlKey + ", Value: " + controlValue + "\n"
            + "Map received from container: "
            + JspTestUtil.getAsString(testMap);
      }
    }

    return "Test PASSED";
  }

  /**
   * Wrapper method for JspTestUtil.debug(String). This wrapper will prepend
   * this classes name to the provided message.
   * 
   * @param message
   *          - the debug message
   */
  private static void debug(String message) {
    JspTestUtil.debug("[CheckObjectTag] " + message);
  }
}
