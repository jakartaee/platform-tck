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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import java.util.List;
import java.util.StringTokenizer;
import java.util.ArrayList;

/**
 * Simple bean to validate a set of values passed in via the PageContext against
 * a user configured set of values.
 */
public class MethodValidatorBean {

  private String _methods = null;

  private PageContext _context = null;

  private String _name = null;

  /**
   * Default constructor.
   */
  public MethodValidatorBean() {
  }

  /**
   * Gets the methods used to validated against the methods added to the
   * PageContext.
   * 
   * @return - a comma separated list of methods
   */
  public String getMethods() {
    return _methods;
  }

  /**
   * Sets the methods used to validated against the methods added to the
   * PageContext.
   * 
   * @param methods
   *          - the methods to validate
   */
  public void setMethods(String methods) {
    this._methods = methods;
  }

  /**
   * Gets the PageContext for this bean.
   * 
   * @return this bean's PageContext
   */
  public PageContext getContext() {
    return _context;
  }

  /**
   * Sets this bean's PageContext.
   * 
   * @param context
   *          - the PageContext for this bean
   */
  public void setContext(PageContext context) {
    this._context = context;
  }

  public String getName() {
    return _name;
  }

  public void setName(String name) {
    _name = name;
  }

  /**
   * Obtains a page scoped List object from the PageContext that obtains a list
   * of methods that have been called by a particular tag handler. This List
   * will be compared with the methods passed to this handler via the methods
   * attribute.
   * 
   * @return a String representing the result of the check.
   * @throws javax.servlet.jsp.JspException
   */
  public String getResult() throws JspException {
    String message = null;
    List list = (List) _context.getAttribute(_name,
        PageContext.APPLICATION_SCOPE);

    if (list != null) {
      String[] calledMethods = getMethodsFromList(list);
      String[] expectedMethods = getMethodsAsArray();

      // arrays should be of equal length elements common
      // between the two arrays have the same index.
      if (calledMethods.length == expectedMethods.length) {
        for (int i = 0; i < expectedMethods.length; i++) {
          if (calledMethods[i].equals(expectedMethods[i])) {
            message = "Test PASSED";
          } else {
            message = "Test FAILED.  Expected the following method "
                + "method sequence to be called against the tag handler"
                + " by the container: [ " + _methods + " ]\n"
                + "Actual sequence: " + JspTestUtil.getAsString(calledMethods);
          }
        }
      } else {
        if (calledMethods.length > expectedMethods.length) {
          message = "Test FAILED.  The container called more methods"
              + " against the tag handler than expected. " + "Expected: [ "
              + _methods + " ]\n" + "Actual: "
              + JspTestUtil.getAsString(calledMethods);
        } else {
          message = "Test FAILED.  The container called fewer methods"
              + " against the tag handler than expected. " + "Expected: [ "
              + _methods + " ]\n" + "Actual: "
              + JspTestUtil.getAsString(calledMethods);
        }
      }
    } else {
      message = "Test FAILED.  Unable to obtain List of methods from "
          + "the PageContext.";
    }

    // reset for the next test run
    _context.removeAttribute(_name, PageContext.APPLICATION_SCOPE);
    return message;
  }

  /**
   * Returns the current value of _methods as a String array.
   * 
   * @return a String array of values based on _methods
   */
  private String[] getMethodsAsArray() {
    if (_methods == null) {
      return new String[0];
    }
    List list = new ArrayList();
    for (StringTokenizer st = new StringTokenizer(_methods, ","); st
        .hasMoreTokens();) {
      list.add(st.nextToken());
    }
    return getMethodsFromList(list);
  }

  /**
   * Returns an array of String values based on values in a List.
   * 
   * @param list
   *          - List to extract the String values from
   * @return a String array of values
   */
  private static String[] getMethodsFromList(List list) {
    if (list == null) {
      return new String[0];
    }
    return (String[]) list.toArray(new String[list.size()]);
  }
}
