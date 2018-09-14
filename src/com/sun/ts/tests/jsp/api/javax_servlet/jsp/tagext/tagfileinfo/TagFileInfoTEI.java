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

package com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.tagfileinfo;

import com.sun.ts.tests.jsp.common.util.BaseTCKExtraInfo;
import com.sun.ts.tests.jsp.common.util.JspTestUtil;

import javax.servlet.jsp.tagext.TagFileInfo;
import javax.servlet.jsp.tagext.TagInfo;

public class TagFileInfoTEI extends BaseTCKExtraInfo {

  /**
   * Default Constructor
   */
  public TagFileInfoTEI() {
  }

  // ------------------------------------------- Test Definitions ----------

  /**
   * Validate TagFileInfo.getName().
   * 
   * @return null if the test passes, or a string containing the cause of test
   *         failure.
   */
  public String getName() {
    debug("in getName()");
    String message = null;
    TagFileInfo[] info = this.getTagInfo().getTagLibrary().getTagFiles();
    if (info != null && info.length == 1) {
      String name = info[0].getName();
      if (!"TagFile1".equals(name)) {
        message = "Test FAILED.  Expected TagFileInfo.getName() to return "
            + "'TagFile1'.  Received: '" + name + "'";
      }
    } else {
      message = "Test FAILED.  TagFileInfo array was null or length was not 1.";
    }
    return message;
  }

  /**
   * Validate TagFileInfo.getPath().
   * 
   * @return null if the test passes, or a string containing the cause of test
   *         failure.
   */
  public String getPath() {
    debug("in getPath()");
    String message = null;
    TagFileInfo[] info = this.getTagInfo().getTagLibrary().getTagFiles();
    if (info != null && info.length == 1) {
      String path = info[0].getPath();
      if (!"/WEB-INF/tags/tagfileinfo/TagFile1.tag".equals(path)) {
        message = "Test FAILED.  Expected TagFileInfo.getPath to return "
            + "'/WEB-INF/tags/TagFile1'.  Received: '" + path + "'";
      }
    } else {
      message = "Test FAILED.  TagFileInfo array was null or length was not 1.";
    }
    return message;
  }

  /**
   * Validate TagFileInfo.getTagInfo(). In addition, this will verify That the
   * directives in the Tag file are properly parsed and the TagInfo object
   * returned by TagFileInfo.getTagInfo() is as expected.
   * 
   * @return null if the test passes, or a string containing the cause of test
   *         failure.
   */
  public String getTagInfoTest() {
    debug("in getTagInfo()");
    String message = null;
    TagFileInfo[] info = this.getTagInfo().getTagLibrary().getTagFiles();
    if (info != null && info.length == 1) {
      TagInfo tInfo = info[0].getTagInfo();
      if (tInfo != null) {
        String dispName = tInfo.getDisplayName();
        if (!"TagFile1".equals(dispName)) {
          message = "Test FAILED.  TagInfo returned by TagFileInfo.getTagInfo()"
              + " didn't return the expected value 'TagFile1' for "
              + "the display-name attribute.  Received: " + dispName;
        }

        String content = tInfo.getBodyContent().toLowerCase();
        if (!"scriptless".equals(content)) {
          message = "Test FAILED.  TagInfo returned by TagFileInfo.getTagInfo()"
              + " didn't return the expected value 'scriptless' for the "
              + "body-content attribute.  Received: " + content;
        }

        if (!tInfo.hasDynamicAttributes()) {
          message = "Test FAILED.  TagInfo returned by TagFileInfo.getTagInfo()"
              + " didn't return the expected value of 'true' for "
              + "the dynamic-attributes attribute.";
        }

        String sIcon = tInfo.getSmallIcon();
        if (!"/16/icon.jpg".equals(sIcon)) {
          message = "Test FAILED.  TagInfo returned by TagFileInfo.getTagInfo()"
              + " didn't returned the expected value of '/16/icon.jpg' "
              + "for the small-icon attribute.  Received: " + sIcon;
        }

        String lIcon = tInfo.getLargeIcon();
        if (!"/32/icon.jpg".equals(lIcon)) {
          message = "Test FAILED.  TagInfo returned by TagFileInfo.getTagInfo()"
              + " didn't returned the expected value of '/32/icon.jpg' "
              + "for the large-icon attribute.  Received: " + lIcon;
        }

        String desc = tInfo.getInfoString();
        if (!"Simple Tag File".equals(desc)) {
          message = "Test FAILED.  TagInfo returned by TagFileInfo.getTagInfo()"
              + " didn't returned the expected value of 'Simple Tag File' "
              + "for the description attribute.  Received: " + desc;
        }
      } else {
        message = "Test FAILED. TagFileInfo.getTagFile() returned null.";
      }
    } else {
      message = "Test FAILED.  TagFileInfo array was null or length was not 1.";
    }
    return message;
  }

  /**
   * Calls JspTestUtil.debug() and includes this class name in the debug
   * message.
   * 
   * @param message
   *          - debug message
   */
  private static void debug(String message) {
    JspTestUtil.debug("[TagFileInfoTEI] " + message);
  }
}
