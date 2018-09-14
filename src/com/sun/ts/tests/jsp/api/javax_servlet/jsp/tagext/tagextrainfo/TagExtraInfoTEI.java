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

package com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.tagextrainfo;

import com.sun.ts.tests.jsp.common.util.JspTestUtil;

import javax.servlet.jsp.tagext.ValidationMessage;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;

public class TagExtraInfoTEI extends TagExtraInfo {

  /**
   * Flag to determine if TEI was indeed called by the container.
   */
  private static boolean _wasCalled = false;

  private boolean _wasIsValidCalled = false;

  private boolean _returnFalse = false;

  /**
   * Default Constructor.
   */
  public TagExtraInfoTEI() {
  }

  /**
   * Validates that TagExtraInfo instances are properly handled by the
   * container.
   * 
   * @param tagData
   * @return
   */
  public ValidationMessage[] validate(TagData tagData) {

    // Checked within the JSP to make sure the TEI was
    // called by the container.
    _wasCalled = true;

    // Validate that TagExtraInfo.getTagInfo() returns a non-null
    // value. This ensures that TagExtraInfo.setTagInfo() was
    // called by the container prior to calling validate().
    if (this.getTagInfo() == null) {
      return JspTestUtil.getValidationMessage(
          "Test FAILED.  " + tagData.getId(),
          "TagExtraInfo.getTagInfo() returned null meaning "
              + "TagExtraInfo.setTagInfo() was not called by the container.");
    }

    String retType = tagData.getAttributeString("test");

    if ("null".equals(retType)) {
      // validating that a null return value doesn't cause a translation
      // error
      return null;
    } else if ("empty".equals(retType)) {
      // validating that an empty array of ValidationMessages doesn't cause
      // a translation error
      return new ValidationMessage[] {};
    } else if ("nonempty".equals(retType)) {
      // validating that a non-empty array of ValidationMessages causes
      // a translation error
      return JspTestUtil.getValidationMessage(tagData.getId(), "Test PASSED");
    } else if ("isValid".equals(retType)) {
      // validates the default behavior of the validate() method
      ValidationMessage[] message = null;
      message = super.validate(tagData);
      if (_wasIsValidCalled) {
        _wasIsValidCalled = false;
        if (message != null && message.length > 0) {
          return JspTestUtil.getValidationMessage(tagData.getId(),
              "Test FAILED.  TagExtraInfo."
                  + "validate() did call isValid(), but a non-null, or non-zero-length array"
                  + " of ValidationMessages was returned when isValid returned true.");
        }
      } else {
        return JspTestUtil.getValidationMessage(tagData.getId(),
            "Test FAILED.  Default implementation"
                + "of TagExtraInfo.validate() must call TagExtraInfo.isValid(), but this did not occur.");
      }
      _returnFalse = true;
      message = super.validate(tagData);
      if (_wasIsValidCalled) {
        _wasIsValidCalled = false;
        if (message == null || message.length == 0) {
          return JspTestUtil.getValidationMessage(tagData.getId(),
              "Test FAILED.  TagExtraInfo."
                  + "validate() did call isValid(), but a null, or zero-length array"
                  + "of ValidationMessages was returned when isValid returned false.");
        }
      } else {
        return JspTestUtil.getValidationMessage(tagData.getId(),
            "Test FAILED.  Default implementation"
                + "of TagExtraInfo.validate() must call TagExtraInfo.isValid(), but this did not occur.");
      }
    } else {
      return JspTestUtil.getValidationMessage(
          "Test FAILED.  Unexpected value" + ", '" + retType
              + "' returned by container for attribute 'test'.",
          tagData.getId());
    }
    return null;
  }

  /**
   * Used to validate the proper default behavior of TagExtraInfo.validate().
   * 
   * @param data
   *          - a TagData instance
   * @return - true of _returnFalse is false, otherwise true.
   */
  public boolean isValid(TagData data) {
    _wasIsValidCalled = true;
    if (_returnFalse) {
      return false;
    } else {
      return true;
    }
  }

  /**
   * Called within the test pages to verify that the TEI was indeed called.
   * 
   * @return true if called, otherwise false
   */
  public static boolean teiWasCalled() {
    return _wasCalled;
  }

  /**
   * Resets the state of this TEI.
   */
  public static void reset() {
    debug("Resetting _wasCalled to false.");
    _wasCalled = false;
  }

  /**
   * Wraps JspTestUtil.debug. This also prepends this TEI's classname to the
   * debug message.
   * 
   * @param message
   *          - the debug message
   */
  private static void debug(String message) {
    JspTestUtil.debug("[TagExtraInfoTEI] " + message);
  }
}
