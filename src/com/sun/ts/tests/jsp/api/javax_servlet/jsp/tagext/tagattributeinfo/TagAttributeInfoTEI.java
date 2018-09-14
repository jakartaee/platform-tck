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

package com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.tagattributeinfo;

import com.sun.ts.tests.jsp.common.util.BaseTCKExtraInfo;
import com.sun.ts.tests.jsp.common.util.JspTestUtil;

import javax.servlet.jsp.tagext.TagAttributeInfo;
import java.util.Arrays;

/**
 * A TagExtraInfo implementation to validate the methods of
 * <tt>javax.servlet.jsp.tagext.TagAttributeInfo.</tt>
 *
 */
public class TagAttributeInfoTEI extends BaseTCKExtraInfo {

  /**
   * TagAttributeInfo for attribute <tt>test</tt>
   */
  private TagAttributeInfo attr1 = null;

  /**
   * TagAttributeInfo for attribute <tt>dynAttribute</tt>
   */
  private TagAttributeInfo attr2 = null;

  /**
   * TagAttributeInfo for attribute <tt>fragAttribute</tt>
   */
  private TagAttributeInfo attr3 = null;

  /**
   * Default constructor.
   */
  public TagAttributeInfoTEI() {
  }

  // ------------------------------------------- Test Definitions ----------

  /**
   * Validate the behavior of <tt>TagAttributeInfo.getName()</tt>.
   * 
   * @return null if the test passes, otherwise a String containing the cause of
   *         the failure.
   */
  public String getName() {
    TagAttributeInfo[] info = this.getTagInfo().getAttributes();
    String message = null;

    if (info != null) {
      if (info.length == 3) {
        String[] names = { info[0].getName(), info[1].getName(),
            info[2].getName() };
        Arrays.sort(names);
        if (Arrays.binarySearch(names, "test") < 0) {
          message = "Test FAILED.  Expected attribute 'test' to be present"
              + " in the TagAttributeInfo array returned by the container."
              + "  Attributes returned: " + JspTestUtil.getAsString(names);
        } else if (Arrays.binarySearch(names, "dynAttribute") < 0) {
          message = "Test FAILED.  Expected attribute 'dynAttribute' to be present"
              + " in the TagAttributeInfo array returned by the container."
              + "  Attributes returned: " + JspTestUtil.getAsString(names);
        } else if (Arrays.binarySearch(names, "fragAttribute") < 0) {
          message = "Test FAILED.  Expected attribute 'fragAttribute' to be present"
              + " in the TagAttributeInfo array returned by the container."
              + "  Attributes returned: " + JspTestUtil.getAsString(names);
        }
      } else {
        message = "Test FAILED.  Expected the TagAttributeInfo array length"
            + " to be 3, but was actually " + info.length;
      }
    } else {
      message = "Test FAILED.  TagAttributeInfo was unexpectedly null.";
    }
    return message;
  }

  /**
   * Validate the behavior of <tt>TagAttributeInfo.getTypeName()</tt>.
   * 
   * @return null if the test passes, otherwise a String containing the cause of
   *         the failure.
   */
  public String getTypeName() {
    TagAttributeInfo[] info = this.getTagInfo().getAttributes();
    String message = null;

    if (info != null) {
      if (info.length == 3) {
        initTagAttributeInfos(info);
        String typeName = attr1.getTypeName();
        if (!"java.lang.String".equals(typeName)) {
          message = "Test FAILED.  Expected attribute type for attribute 'test' "
              + "to be 'java.lang.String'.  Received: " + typeName;
          return message;
        }
        typeName = attr2.getTypeName();
        if (!"java.lang.Integer".equals(typeName)) {
          message = "Test FAILED.  Expected attribue type for attribute 'dynAttribute' "
              + "to be 'java.lang.Integer'.  Received: " + typeName;
          return message;
        }
        typeName = attr3.getTypeName();
        if (!"javax.servlet.jsp.tagext.JspFragment".equals(typeName)) {
          message = "Test FAILED.  Expected attribue type for attribute 'fragAttribute' "
              + "to be 'javax.servlet.jsp.tagext.JspFragment'.  Received: "
              + typeName;
          return message;
        }
      } else {
        message = "Test FAILED.  Expected the TagAttributeInfo array length"
            + " to be 3, but was actually " + info.length;
      }
    } else {
      message = "Test FAILED.  TagAttributeInfo was unexpectedly null.";
    }
    return message;
  }

  /**
   * Validate the behavior of <tt>TagAttributeInfo.canBeRequestTime()</tt>.
   * 
   * @return null if the test passes, otherwise a String containing the cause of
   *         the failure.
   */
  public String canBeRequestTime() {
    TagAttributeInfo[] info = this.getTagInfo().getAttributes();
    String message = null;

    if (info != null) {
      if (info.length == 3) {
        initTagAttributeInfos(info);
        // should return false
        if (attr1.canBeRequestTime()) {
          message = "Test FAILED.  Expected TagAttributeInfo.canBeRequestTime() "
              + "to return 'false' for attribute 'test'.";
          return message;
        }

        if (!attr2.canBeRequestTime()) {
          message = "Test FAILED.  Expected TagAttributeInfo.canBeRequestTime() "
              + "to return 'true' for attribute 'dynAttribute'.";
          return message;
        }
      } else {
        message = "Test FAILED.  Expected the TagAttributeInfo array length"
            + " to be 3, but was actually " + info.length;
      }
    } else {
      message = "Test FAILED.  TagAttributeInfo was unexpectedly null.";
    }
    return message;
  }

  /**
   * Validate the behavior of <tt>TagAttributeInfo.isRequired()</tt>.
   * 
   * @return null if the test passes, otherwise a String containing the cause of
   *         the failure.
   */
  public String isRequired() {
    TagAttributeInfo[] info = this.getTagInfo().getAttributes();
    String message = null;

    if (info != null) {
      if (info.length == 3) {
        initTagAttributeInfos(info);
        if (!attr1.isRequired()) {
          message = "Test FAILED.  Expected TagAttributeInfo.isRequired() "
              + "to return 'true' for attribute 'test'.";
          return message;
        }
        if (attr2.isRequired()) {
          message = "Test FAILED.  Expected TagAttributeInfo.isRequired() "
              + "to return 'false' for attribute 'dynAttribute'.";
          return message;
        }
      } else {
        message = "Test FAILED.  Expected the TagAttributeInfo array length"
            + " to be 3, but was actually " + info.length;
      }
    } else {
      message = "Test FAILED.  TagAttributeInfo was unexpectedly null.";
    }
    return message;
  }

  /**
   * Validate the behavior of <tt>TagAttributeInfo.getIdAttribute()</tt>.
   * 
   * @return null if the test passes, otherwise a String containing the cause of
   *         the failure.
   */
  public String getIdAttributeTest() {
    TagAttributeInfo[] info = this.getTagInfo().getAttributes();
    String message = null;

    if (info != null) {
      if (info.length == 3) {
        initTagAttributeInfos(info);
        TagAttributeInfo idAtt = TagAttributeInfo.getIdAttribute(info);
        if (idAtt != null) {
          message = "Test FAILED. Expected TagAttributeInfo.getIdAttribute(info) to return null, but got "
              + idAtt.toString();
        }
      } else {
        message = "Test FAILED.  Expected the TagAttributeInfo array length"
            + " to be 3, but was actually " + info.length;
      }
    } else {
      message = "Test FAILED.  TagAttributeInfo[] was unexpectedly null.";
    }
    return message;
  }

  /**
   * Validate the behavior of <tt>TagAttributeInfo.isFragment()</tt>.
   * 
   * @return null if the test passes, otherwise a String containing the cause of
   *         the failure.
   */
  public String isFragment() {
    TagAttributeInfo[] info = this.getTagInfo().getAttributes();
    String message = null;

    if (info != null) {
      if (info.length == 3) {
        initTagAttributeInfos(info);
        if (attr1.isFragment()) {
          message = "Test FAILED.  Expected TagAttributeInfo.isFragment() "
              + "to return 'false' for attribute 'test'.";
          return message;
        }

        if (!attr3.isFragment()) {
          message = "Test FAILED.  Expected TagAttributeInfo.isFragment() "
              + "to return 'true' for attribute 'fragAttribute'.";
          return message;
        }
      } else {
        message = "Test FAILED.  Expected the TagAttributeInfo array length"
            + " to be 3, but was actually " + info.length;
      }
    } else {
      message = "Test FAILED.  TagAttributeInfo was unexpectedly null.";
    }
    return message;
  }

  /**
   * Validate the behavior of <tt>TagAttributeInfo.toString()</tt>.
   * 
   * @return null if the test passes, otherwise a String containing the cause of
   *         the failure.
   */
  public String toStringTest() {
    TagAttributeInfo[] info = this.getTagInfo().getAttributes();
    String message = null;

    if (info != null) {
      if (info.length == 3) {
        initTagAttributeInfos(info);
        if (attr1.toString() == null) {
          message = "Test FAILED.  TagAttributeInfo.toString() unexpectedly "
              + "returned null for attribute 'test'.";
          return message;
        }
        if (attr2.toString() == null) {
          message = "Test FAILED.  TagAttributeInfo.toString() unexpectedly "
              + "returned null for attribute 'dynAttribute'.";
          return message;
        }
        if (attr3.toString() == null) {
          message = "Test FAILED.  TagAttributeInfo.toString() unexpectedly "
              + "returned null for attribute 'fragAttribute'.";
          return message;
        }
      } else {
        message = "Test FAILED.  Expected the TagAttributeInfo array length"
            + " to be 3, but was actually " + info.length;
      }
    } else {
      message = "Test FAILED.  TagAttributeInfo was unexpectedly null.";
    }
    return message;
  }

  /**
   * Initialize the TagAttributeInfo instance variables based on the provided
   * array.
   * 
   * @param infos
   *          - an array of TagAttributeInfo objects.
   */
  private void initTagAttributeInfos(TagAttributeInfo[] infos) {
    for (int i = 0; i < infos.length; i++) {
      if ("test".equals(infos[i].getName())) {
        attr1 = infos[i];
      }
      if ("dynAttribute".equals(infos[i].getName())) {
        attr2 = infos[i];
      }
      if ("fragAttribute".equals(infos[i].getName())) {
        attr3 = infos[i];
      }
    }
  }
}
