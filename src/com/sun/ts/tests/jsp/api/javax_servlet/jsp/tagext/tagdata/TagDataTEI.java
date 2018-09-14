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

package com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.tagdata;

import com.sun.ts.tests.jsp.common.util.BaseTCKExtraInfo;
import com.sun.ts.tests.jsp.common.util.JspTestUtil;

import javax.servlet.jsp.tagext.TagData;

public class TagDataTEI extends BaseTCKExtraInfo {

  /**
   * Default constructor.
   */
  public TagDataTEI() {
  }

  // ------------------------------------------- Test Definitions ----------

  /**
   * Validate TagData.getId();
   * 
   * @return null if the test passes, or a String containing the reason for
   *         failure.
   */
  public String getId() {
    TagData data = this.getTagData();
    String message = null;
    if (data != null) {

    } else {
      message = "Test FAILED.  TagData was null.";
    }
    return message;
  }

  /**
   * Validate TagData.getAttribute().
   * 
   * @return null if the test passes, or a String containing the reason for
   *         failure.
   */
  public String getAttribute() {
    debug("in getAttribute()");
    TagData data = this.getTagData();
    String message = null;
    if (data != null) {
      String attr = (String) data.getAttribute("test");
      if (!"getAttribute".equals(attr)) {
        message = "Test FAILED.  Expected TagData.getAttribute() to return"
            + " 'getAttribute' for attribute 'test'.  Received: " + attr;
      }

      Object o = data.getAttribute("dynAttribute");
      if (o != TagData.REQUEST_TIME_VALUE) {
        message = "Test FAILED.  Expected TagData.getAttribute to return"
            + " the distinquished TagData.REQUEST_TIME_VALUE object for a dynamic"
            + " attribute defined in the TLD.";

      }

      o = data.getAttribute("dynAttribute2");
      if (o != TagData.REQUEST_TIME_VALUE) {
        message = "Test FAILED.  Expected TagData.getAttribute to return"
            + " the distinquished TagData.REQUEST_TIME_VALUE object for a dynamic"
            + " attribute defined in the TLD.";
      }

      attr = (String) data.getAttribute("dynAttribute3");
      if (!"static".equals(attr)) {
        message = "Test FAILED.  Expected TagData.getAttribute() to return"
            + " 'static' for attribute 'dynAttribute3'.  Received: " + attr;
      }

      if (data.getAttribute("dynAttribute4") != null) {
        message = "Test FAILED.  Expected null to be returned for an attribute "
            + "not specified in the action.";
      }

    } else {
      message = "Test FAILED.  TagData was null.";
    }
    return message;
  }

  /**
   * Validate TagData.setAttribute().
   * 
   * @return null if the test passes, or a String containing the reason for
   *         failure.
   */
  public String setAttribute() {
    debug("in setAttribute()");
    TagData data = this.getTagData();
    String message = null;
    if (data != null) {
      String val = "attributeSet";
      data.setAttribute("test", val);
      String nVal = (String) data.getAttribute("test");
      if (!val.equals(nVal)) {
        message = "Test FAILED.  Attempted to set the 'test' attribute value "
            + "to 'attributeSet'.  Value of 'test' attribute after the call "
            + "to setAttribute() was: " + nVal;
      }
    } else {
      message = "Test FAILED.  TagData was null.";
    }
    return message;
  }

  /**
   * Validate TagData.getAttributeString().
   * 
   * @return null if the test passes, or a String containing the reason for
   *         failure.
   */
  public String getAttributeString() {
    debug("in getAttributeString()");
    TagData data = this.getTagData();
    String message = null;
    if (data != null) {
      String val = data.getAttributeString("test");
      if (!"getAttributeString".equals(val)) {
        message = "Test FAILED.  TagData.getAttributeString() returned "
            + "a String object, but the value, '" + val + "' is incorrected."
            + "  Expected: " + val;
      }

      data.setAttribute("test", new Object());
      try {
        data.getAttributeString("test");
      } catch (Throwable t) {
        if (!(t instanceof ClassCastException)) {
          message = "Test FAILED.  Exception thrown when calling "
              + "TagData.getAttributeString(), but it was not an instance "
              + "of ClassCastException.  Exception returned: "
              + t.getClass().getName();
        }
        return message;
      }
      message = "Test FAILED.  No Exception thrown when calling TagData.getAttributeString()"
          + " on an attribute whose value is not a String.  "
          + data.getAttribute("test").toString();
    } else {
      message = "Test FAILED.  TagData was null.";
    }
    return message;
  }

  /**
   * Validate TagData.getAttributes().
   * 
   * @return null if the test passes, or a String containing the reason for
   *         failure.
   */
  public String getAttributes() {
    debug("in getAttributes()");
    TagData data = this.getTagData();
    String message = null;
    String[] expValues = { "test", "dynAttribute" };
    if (data != null) {
      if (!JspTestUtil.checkEnumeration(data.getAttributes(), expValues)) {
        message = "Test FAILED.  The Enumeration returned by TagData.getAttributes()"
            + " didn't contain the expected values of 'test' and 'dynAttribute."
            + "  Received: " + JspTestUtil.getAsString(data.getAttributes());
      }
    } else {
      message = "Test FAILED.  TagData was null.";
    }
    return message;
  }

  /**
   * Validate TagData(Object[][])
   * 
   * @return null if the test passes, or a String containing the reason for
   *         failure.
   */
  public String constructorTest() {
    debug("in constructorTest()");
    String message = null;
    Object[][] att = null;
    Object[][] att2 = { { "connection", "conn0" }, { "id", "query0" } };
    TagData data = null;
    TagData data2 = null;
    try {
      data = new TagData(att);
      data2 = new TagData(att2);
    } catch (Throwable th) {
      message = "Test FAILED. Throwable caught when invoking TagData(Object[][]):"
          + th.getMessage();
    }
    if (data == null) {
      message = "Test FAILED. TagaData data was null.";
    }
    if (data2 == null) {
      message = "Test FAILED. TagaData data2 was null.";
    }
    return message;
  }

  private static void debug(String message) {
    JspTestUtil.debug("[TagDataTEI] " + message);
  }
}
