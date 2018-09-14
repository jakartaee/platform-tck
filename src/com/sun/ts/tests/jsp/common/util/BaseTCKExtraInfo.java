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

import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.ValidationMessage;
import javax.servlet.jsp.tagext.TagData;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

public abstract class BaseTCKExtraInfo extends TagExtraInfo {

  /**
   * Flag to determine if TEI was indeed called by the container.
   */
  private static boolean _wasCalled = false;

  /**
   * TagData instanced passed to validate method.
   */
  private TagData _data = null;

  /**
   * Default constructor.
   */
  public BaseTCKExtraInfo() {
  }

  /**
   * Called by the container to validate the page. This implementation will
   * inspect the <tt>test</tt> attribute and invoke a method based on its value.
   * These test methods will exist in subclasses of this base class.
   * 
   * @param tagData
   *          - A TagData instance provided by the container.
   * @return - null if the test passes, or a non-zero lenght array of
   *         ValidationMessages if the test fails.
   */
  public ValidationMessage[] validate(TagData tagData) {
    debug("In validate().  Setting _wasCalled to true.");
    _wasCalled = true;
    _data = tagData;
    String message = null;
    String testMethod = tagData.getAttributeString("test");
    ValidationMessage[] msg = null;

    // We have the test name, use reflection and invoke the test.
    try {
      Method tMethod = this.getClass().getMethod(testMethod);
      debug("Invoking test method " + testMethod + "() on "
          + this.getClass().getName());
      String result = (String) tMethod.invoke(this);
      if (result != null) {
        msg = JspTestUtil.getValidationMessage(tagData.getId(), result);
      }
    } catch (Throwable t) {
      if (t instanceof InvocationTargetException) {
        Throwable root = ((InvocationTargetException) t).getTargetException();
        message = "Test FAILED.  Unexpected TargetInvocationException.  Root cause: "
            + root.toString() + " : " + root.getMessage();
      } else {
        message = "Test FAILED.  Unexpected exception during test invocation.  "
            + t.toString() + " : " + t.getMessage();
      }
      msg = JspTestUtil.getValidationMessage(tagData.getId(), message);
    }

    // Everything worked as expected. Return null.
    return msg;
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
   * Returns this TEI's TagData instance.
   * 
   * @return this TEI's TagData instance.
   */
  protected TagData getTagData() {
    return _data;
  }

  /**
   * Utility method that calls JspTestUtil.debug. This method adds this class
   * name to the message provided.
   * 
   * @param message
   *          - a debug message
   */
  private static void debug(String message) {
    JspTestUtil.debug("[BaseTCKExtraInfo] " + message);
  }
}
