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

package com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.tagvariableinfo;

import com.sun.ts.tests.jsp.common.util.BaseTCKExtraInfo;

import javax.servlet.jsp.tagext.*;

/**
 * A TagExtraInfo implementation to validate the methods of
 * <tt>javax.servlet.jsp.tagext.TagVariableInfo.</tt>
 */
public class TagVarInfoTEI extends BaseTCKExtraInfo {

  /**
   * This is var1.
   */
  private TagVariableInfo _var1 = null;

  /**
   * This is var2.
   */
  private TagVariableInfo _var2 = null;

  /**
   * This is var3.
   */
  private TagVariableInfo _var3 = null;

  /**
   * This is a variable based of the value determined at translation time.
   */
  private TagVariableInfo _var4 = null;

  /**
   * Returns any variables defined by this TEI.
   * 
   * @param data
   *          - TagData
   * @return - returns null so a translation error isn't raised.
   */
  public VariableInfo[] getVariableInfo(TagData data) {
    return null;
  }

  // ---------------------------------- Begin Tests -----------------

  /**
   * Validates TagVariableInfo.getClassName().
   * 
   * @return null if the test passes, otherwise a String indicating the cause of
   *         the failure.
   */
  public String getClassName() {
    initVariableInfos(this.getTagInfo().getTagVariableInfos());
    // this should be java.lang.Integer
    String class1 = _var1.getClassName();

    // this should be the default of java.lang.String
    String class2 = _var2.getClassName();

    if (!"java.lang.Integer".equals(class1)) {
      return "Test FAILED.  Expected getClassName() for var1 to return 'java.lang.Integer'.  "
          + "Received: " + class1;
    }

    if (!"java.lang.String".equals(class2)) {
      return "Test FAILED.  Expected getClassName() for var2 to return 'java.lang.String.  "
          + "Received: " + class2;
    }
    return null;
  }

  /**
   * Validates TagVariableInfo.getDeclare().
   * 
   * @return null if the test passes, otherwise a String indicating the cause of
   *         the failure.
   */
  public String getDeclare() {
    /*
     * var1 should return true var2 should return false var3 not specified, so
     * return true
     */
    initVariableInfos(this.getTagInfo().getTagVariableInfos());
    if (_var1.getDeclare()) {
      if (!_var2.getDeclare()) {
        if (_var3.getDeclare()) {
          return null;
        } else {
          return "Test FAILED.  Expected getDeclare for var3 to return true.";
        }
      } else {
        return "Test FAILED.  Expected getDeclare() for var2 to return false.";
      }
    } else {
      return "Test FAILED.  Expected getDeclare() for var1 to return true.";
    }
  }

  /**
   * Validates TagVariableInfo.getNameFromAttribute().
   * 
   * @return null if the test passes, otherwise a String indicating the cause of
   *         the failure.
   */
  public String getNameFromAttribute() {
    initVariableInfos(this.getTagInfo().getTagVariableInfos());
    if (!"test".equals(_var4.getNameFromAttribute())) {
      return "Test FAILED.  Expected getNameFromAttribute() to return 'test'.  "
          + "Received: " + _var4.getNameFromAttribute();
    }
    return null;
  }

  /**
   * Validates TagVariableInfo.getNameGiven().
   * 
   * @return null if the test passes, otherwise a String indicating the cause of
   *         the failure.
   */
  public String getNameGiven() {
    initVariableInfos(this.getTagInfo().getTagVariableInfos());
    if (!"var1".equals(_var1.getNameGiven())) {
      return "Test FAILED.  Expected getNameGiven() for var1 to return 'var1'.  "
          + "Received: " + _var1.getNameGiven();
    }
    return null;
  }

  /**
   * Validates TagVariableInfo.getScope().
   * 
   * @return null if the test passes, otherwise a String indicating the cause of
   *         the failure.
   */
  public String getScope() {
    initVariableInfos(this.getTagInfo().getTagVariableInfos());
    // not declared, so it must be NESTED
    int var1Scope = _var1.getScope();
    // should be AT_END
    int var2Scope = _var2.getScope();
    // should be NESTED
    int var3Scope = _var3.getScope();
    // should be AT_BEGIN
    int var4Scope = _var4.getScope();

    if (var1Scope != VariableInfo.NESTED) {
      return "Test FAILED.  Expected getScope() for var1 to return NESTED.  "
          + "Received: " + getVariableScope(var1Scope);
    }

    if (var2Scope != VariableInfo.AT_END) {
      return "Test FAILED.  Expected getScope for var2 to return AT_END.  "
          + "Received: " + getVariableScope(var2Scope);
    }

    if (var3Scope != VariableInfo.NESTED) {
      return "Test FAILED.  Expected getScope for var3 to return NESTED"
          + "Received: " + getVariableScope(var3Scope);
    }

    if (var4Scope != VariableInfo.AT_BEGIN) {
      return "Test FAILED.  Expected getScope for var4 to return AT_BEGIN"
          + "Received: " + getVariableScope(var3Scope);
    }
    return null;
  }

  /**
   * Utility method to get a meaningful representation of the provided scope.
   * 
   * @param scope
   *          - the variable scope
   * @return a String representation of the provided scope
   */
  private String getVariableScope(int scope) {
    if (scope == VariableInfo.NESTED) {
      return "NESTED";
    } else if (scope == VariableInfo.AT_BEGIN) {
      return "AT_BEGIN";
    } else if (scope == VariableInfo.AT_END) {
      return "AT_END";
    } else {
      return "UNKNOWN SCOPE";
    }
  }

  /**
   * Initializes the VariableInfo instance variables used by the test.
   * 
   * @param varInfo
   *          - an array of TagVariableInfo objects
   * @return - null if the initialization process succeeds, otherwise a String
   *         indicating the failure.
   */
  private String initVariableInfos(TagVariableInfo[] varInfo) {
    if (varInfo.length != 4) {
      return "Test FAILED."
          + "  Expected a TagVariableInfo[] length of 4, but the actual length "
          + "was " + varInfo.length;
    }

    for (int i = 0; i < varInfo.length; i++) {
      if ("var1".equals(varInfo[i].getNameGiven())) {
        _var1 = varInfo[i];
      } else if ("var2".equals(varInfo[i].getNameGiven())) {
        _var2 = varInfo[i];
      } else if ("var3".equals(varInfo[i].getNameGiven())) {
        _var3 = varInfo[i];
      } else {
        _var4 = varInfo[i];
      }
    }
    return null;
  }
}
