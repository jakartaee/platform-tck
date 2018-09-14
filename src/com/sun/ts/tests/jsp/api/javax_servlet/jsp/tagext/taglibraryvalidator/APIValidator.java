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

package com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.taglibraryvalidator;

import com.sun.ts.tests.jsp.common.util.JspTestUtil;

import javax.servlet.jsp.tagext.TagLibraryValidator;
import javax.servlet.jsp.tagext.ValidationMessage;
import javax.servlet.jsp.tagext.PageData;
import java.util.Map;
import java.util.Arrays;

public class APIValidator extends TagLibraryValidator {

  /**
   * Count to track the number of times the validation method has been called.
   */
  public static int callCount = 0;

  /**
   * The prefix expected to be passed by the container.
   */
  private static final String PREFIX = "tlv1";

  /**
   * The URI expected to be passed by the container.
   */
  private static final String URI = "http://java.sun.com/tck/jsp/tlv";

  /**
   * Indicates that setInitParameters() has been called by the container.
   */
  private boolean _setInitParametersCalled = false;

  /**
   * Default constructor
   */
  public APIValidator() {
  }

  /**
   * Validates the interaction between the TLV class, and the container.
   * 
   * @param prefix
   *          - the taglib prefix
   * @param uri
   *          - the taglib URI
   * @param pageData
   *          - PageData object
   * @return - Null or an Empty array of ValidationMessages.
   */
  public ValidationMessage[] validate(String prefix, String uri,
      PageData pageData) {
    callCount++;

    // check the arguments passed in. Prefix should be one
    // of the values in PREFIXES, uri, should be the same as URI.
    // The PageData object should be non-null.
    if (pageData == null) {
      return JspTestUtil.getValidationMessage(null,
          "Test FAILED.  Container passed a null PageData object.");
    }

    if (!URI.equals(uri)) {
      return JspTestUtil.getValidationMessage(null,
          "Test FAILED.  Container passed in an unexpted taglib URI.  Expected: "
              + URI + ", recieved: " + uri);
    }

    if (!PREFIX.equals(prefix)) {
      return JspTestUtil.getValidationMessage(null,
          "Test FAILED.  Container passed in an unexpected taglib prefix."
              + "  Expected 'tlv1', received: " + prefix);
    }

    // check that setInitParamteres was indeed called.
    if (!_setInitParametersCalled) {
      return JspTestUtil.getValidationMessage(null,
          "Test FAILED.  setInitParameters() was not called on the TLV.");
    }

    // check the initialization parameters that are passed are correct.
    Map map = this.getInitParameters();
    if (map != null) {
      String parValue = (String) map.get("initParam");
      if (!"paramValue".equals(parValue)) {
        return JspTestUtil.getValidationMessage(null,
            "Test FAILED.  getInitParameters returned a non-null value,"
                + "but an unexpected init param value was returned.  Expected 'paramValue' "
                + "Received: " + parValue);
      }
    } else {
      return JspTestUtil.getValidationMessage(null,
          "Test FAILED.  getInitParameters" + " returned null.");
    }

    // The TLV for should be called for each taglib directive.
    // The test page has two such directives.
    if (callCount == 2) {
      System.out.println(
          "[APIValidator] " + "Returning empty ValidationMessage Array.");
      return new ValidationMessage[] {};
    }
    System.out.println("[APIValidator] Returning null.");
    return null;

  }

  /**
   * Returns the initialization parameters for this TLV.
   * 
   * @return a map containing this TLV's initialziation parameters
   */
  public Map getInitParameters() {
    return super.getInitParameters();
  }

  /**
   * Called by the container to set this TLV's initialization parameters.
   * 
   * @param map
   *          - map containing the parameters
   */
  public void setInitParameters(Map map) {
    super.setInitParameters(map);
    _setInitParametersCalled = true;
  }
}
