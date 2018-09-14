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

package com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.functioninfo;

import com.sun.ts.tests.jsp.common.util.BaseTCKExtraInfo;
import com.sun.ts.tests.jsp.common.util.JspTestUtil;

import javax.servlet.jsp.tagext.FunctionInfo;

/**
 * A TagExtraInfo implementation to validate the methods of
 * javax.servlet.jsp.tagext.FunctionInfo.
 */
public class FunctionInfoTEI extends BaseTCKExtraInfo {

  /**
   * Default constructor.
   */
  public FunctionInfoTEI() {
  }

  // ------------------------------------------- Test Definitions ----------

  /**
   * Validate the behavior of FunctionInfo.getName().
   * 
   * @return null if the test passes, otherwise a String describing the cause of
   *         failure.
   */
  public String getName() {
    debug("in getName()");
    FunctionInfo[] info = this.getTagInfo().getTagLibrary().getFunctions();
    String message = null;
    if (info != null) {
      if (info.length == 1) {
        String name = info[0].getName().trim();
        if (!"upperCase".equals(name)) {
          message = "Test FAILED.  FunctionInfo.getName() returned " + "'"
              + name + "', expected: 'upperCase'";
        }
      } else {
        message = "Test FAILED.  FunctionInfo array length was " + info.length
            + ", expected the length to be 1.";
      }
    } else {
      message = "Test FAILED.  FunctionInfo was null.";
    }
    return message;
  }

  /**
   * Validate the behavior of FunctionInfo.getFunctionClass().
   * 
   * @return null if the test passes, otherwise a String describing the cause of
   *         failure.
   */
  public String getFunctionClass() {
    debug("in getFunctionClass()");
    FunctionInfo[] info = this.getTagInfo().getTagLibrary().getFunctions();
    String message = null;
    if (info != null) {
      if (info.length == 1) {
        String clazz = info[0].getFunctionClass().trim();
        String expClass = "com.sun.ts.tests.jsp.common.util.JspFunctions";
        if (!expClass.equals(clazz)) {
          message = "Test FAILED.  Expected FunctionInfo.getFunctionClass()"
              + " to return '" + expClass + "'.  Received: '" + clazz + "'";
        }
      } else {
        message = "Test FAILED.  FunctionInfo array length was " + info.length
            + ", expected the length to be 1.";
      }
    } else {
      message = "Test FAILED.  FunctionInfo was null.";
    }
    return message;
  }

  /**
   * Validate the behavior of FunctionInfo.getFunctionSignature().
   * 
   * @return null if the test passes, otherwise a String describing the cause of
   *         failure.
   */
  public String getFunctionSignature() {
    debug("in getFunctionSignature()");
    FunctionInfo[] info = this.getTagInfo().getTagLibrary().getFunctions();
    String message = null;
    if (info != null) {
      if (info.length == 1) {
        String sig = info[0].getFunctionSignature().trim();
        String expSig = "java.lang.String upperCase(java.lang.String)";
        if (!expSig.equals(sig)) {
          message = "Test FAILED.  Expected FunctionInfo.getFunctionClass()"
              + " to return '" + expSig + "'.  Received: '" + sig + "'";
        }
      } else {
        message = "Test FAILED.  FunctionInfo array length was " + info.length
            + ", expected the length to be 1.";
      }
    } else {
      message = "Test FAILED.  FunctionInfo was null.";
    }
    return message;
  }

  /**
   * Utility method that calls JspTestUtil.debug after prepending this class
   * name to the message.
   * 
   * @param message
   *          - a debug message
   */
  private static void debug(String message) {
    JspTestUtil.debug("[FunctionInfoTEI] " + message);
  }
}
