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

import javax.servlet.jsp.el.FunctionMapper;
import java.lang.reflect.Method;

/**
 * Simple Function mapper.
 */

public class TSFunctionMapper implements FunctionMapper {

  private boolean _resolveCalled = false;

  private String _prefixUsed = null;

  private String _methodCalled = null;

  private static final Class FUNCTIONS = com.sun.ts.tests.jsp.common.util.JspFunctions.class;

  /**
   * Creates a new FuntionMapper instance.
   */
  public TSFunctionMapper() {
  }

  /**
   * Resolves the specified local name and prefix into a java.lang.Method.
   * Returns null if the prefix and local name are not found.
   *
   * This class does nothing more than perform refection against the
   * JspFunctions class and return the methods.
   *
   * @param prefix
   *          - method refernce prefix
   * @param localName
   *          - local name to identify the method
   * @return - Resolve function as a java.lang.Method or null if unresolvable.
   */
  public Method resolveFunction(String prefix, String localName) {
    System.out.println("FUNCTION MAPPER CALLED");
    _resolveCalled = true;
    _prefixUsed = prefix;
    _methodCalled = localName;
    if (prefix != null || localName != null) {
      try {
        System.out.println("RETURNING METHOD");
        Method meth = FUNCTIONS.getMethod(localName,
            new Class[] { java.lang.String.class });
        return meth;
      } catch (Throwable t) {
        return null;
      }
    }
    return null;
  }

  /**
   * Has the resolveFunction method been called?
   * 
   * @return - true if resolveFunction has been called otherwise false.
   */
  public boolean hasResolved() {
    return _resolveCalled;
  }

  /**
   * Returns the prefix provided to the resolveFunction call.
   * 
   * @return the prefix of the most recent resolveFunction call
   */
  public String getPrefixUsed() {
    return _prefixUsed;
  }

  /**
   * Returns the method provided to the resolveFunction call.
   * 
   * @return the method of the most recent resolveFunction call
   */
  public String getMethodCalled() {
    return _methodCalled;
  }

  /**
   * Resets the state of this FunctionMapper
   */
  public void reset() {
    _resolveCalled = false;
    _prefixUsed = null;
    _methodCalled = null;
  }

}
