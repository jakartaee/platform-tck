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

package com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.taginfo;

import com.sun.ts.tests.jsp.common.util.BaseTCKExtraInfo;
import com.sun.ts.tests.jsp.common.util.JspTestUtil;

import java.util.Arrays;
import javax.servlet.jsp.tagext.TagInfo;
import javax.servlet.jsp.tagext.TagAttributeInfo;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.TagVariableInfo;
import javax.servlet.jsp.tagext.TagLibraryInfo;

/**
 * A TagExtraInfo implementation to validate the methods of
 * <tt>javax.servlet.jsp.tagext.TagInfo.</tt>
 */
public class TagInfoTEI extends BaseTCKExtraInfo {

  /**
   * Test tag
   */
  private static final String TEST_TAG = "test";

  /**
   * Test tag 2
   */
  private static final String TEST_TAG2 = "test2";

  /**
   * Default Constructor.
   */
  public TagInfoTEI() {
  }

  // ------------------------------------------- Test Definitions ----------

  /**
   * Validates TagInfo.getTagName().
   * 
   * @return null if the test passes, otherwise a String with a failure message.
   */
  public String getTagName() {
    debug("in getTagName()");
    TagInfo[] info = this.getTagInfo().getTagLibrary().getTags();
    String message = null;

    if (info != null && info.length == 2) {
      debug("TagInfo length: " + info.length);
      String tagName = info[0].getTagName();
      if (!TEST_TAG.equals(tagName) && !TEST_TAG2.equals(tagName)) {
        message = "Test FAILED.  Expected a tag name of 'test' or 'test2'"
            + " to be returned.  Received: " + tagName;
      }
    } else {
      message = "Test FAILED.  The TagInfo array provided to test method"
          + " was null or did not have a length of 2.";
    }
    return message;
  }

  /**
   * Validates TagInfo.getAttributes().
   * 
   * @return null if the test passes, otherwise a String with a failure message.
   */
  public String getAttributes() {
    debug("in getAttributes()");
    TagInfo[] info = this.getTagInfo().getTagLibrary().getTags();
    String message = null;
    debug("TagInfo length: " + info.length);

    if (info.length == 2) {

      for (int i = 0; i < info.length; i++) {
        TagInfo inf = info[i];
        if (TEST_TAG.equals(inf.getTagName())) {
          TagAttributeInfo[] attrInfo = inf.getAttributes();
          if (attrInfo != null) {
            if (attrInfo.length != 1) {
              message = "Test FAILED.  Expected an attribute count for"
                  + " 'test' to be 1, but the actual count was: "
                  + attrInfo.length;
              break;
            }
          } else {
            message = "Test FAILED.  Expected a non-null value to be returned"
                + " from TagInfo.getAttributes() for 'test'.";
          }
        } else if (TEST_TAG2.equals(inf.getTagName())) {
          TagAttributeInfo[] attrInfo = inf.getAttributes();
          if (attrInfo == null) {
            message = "Test FAILED.  Expected TagInfo.getAttributes() to"
                + " return an empty array for 'test2' as no attributes were defined,"
                + " but null is returned ";
            break;
          } else if (attrInfo.length != 0) {
            message = "Test FAILED.  Expected TagInfo.getAttributes() to"
                + " return an empty array for 'test2' as no attributes were defined,"
                + " but got: " + Arrays.asList(attrInfo).toString();
            break;
          }
        } else {
          message = "Test FAILED.  Unexpected tag returned: "
              + info[i].getTagName();
          break;
        }
      }
    } else {
      message = "Test FAILED.  Expected a TagInfo array length of 2, but was "
          + "actually " + info.length;
    }

    return message;
  }

  /**
   * Validates TagInfo.getTagExtraInfo(). This also indirectly validates
   * TagInfo.setTagExtraInfo() as this must be called by container in order for
   * getTagExtraInfo() to return anything valid.
   * 
   * @return null if the test passes, otherwise a String with a failure message.
   */
  public String getTagExtraInfo() {
    debug("in getTagExtraInfo()");
    TagInfo[] info = this.getTagInfo().getTagLibrary().getTags();
    String message = null;

    if (info != null) {
      if (info.length == 2) {
        for (int i = 0; i < info.length; i++) {
          String tagName = info[i].getTagName();
          if (TEST_TAG.equals(tagName)) {
            TagExtraInfo tei = info[i].getTagExtraInfo();
            if (tei == null) {
              message = "Test FAILED.  Expected a non-null value "
                  + "to be returned from TagInfo.getTagExtraInfo() for tag 'test'.";
              break;
            }
          } else if (TEST_TAG2.equals(tagName)) {
            if (info[i].getTagExtraInfo() != null) {
              message = "Test FAILED.  TagInfo.getTagExtraInfo() returned a non-null"
                  + " value for tag 'test2' when null was expected.";
              break;
            }
          } else {
            message = "Test FAILED.  Unexpected tag returned: "
                + info[i].getTagName();
            break;
          }
        }
      } else {
        message = "Test FAILED.  Expected a TagInfo array length of 2, but was "
            + "actually " + info.length;
      }
    } else {
      message = "Test FAILED.  The TagInfo array provided to test method"
          + " was null";
    }
    return message;
  }

  /**
   * Validates TagInfo.getTagClassName().
   * 
   * @return null if the test passes, otherwise a String with a failure message.
   */
  public String getTagClassName() {
    debug("in getTagClassName");
    TagInfo[] info = this.getTagInfo().getTagLibrary().getTags();
    String message = null;

    if (info != null && info.length == 2) {
      String className = "com.sun.ts.tests.jsp.common.tags.tck.SimpleTag";
      String tagName = info[0].getTagName();
      if (!className.equals(info[0].getTagClassName())) {
        message = "Test FAILED.  Expected a class name of '" + className
            + "' for tag '" + tagName + "'";
      }
    } else {
      message = "Test FAILED.  The TagInfo array provided to test method"
          + " was null or did not have a length of 2.";
    }
    return message;
  }

  /**
   * Validates TagInfo.getBodyContent().
   * 
   * @return null if the test passes, otherwise a String with a failure message.
   */
  public String getBodyContent() {
    debug("in getBodyContent");
    TagInfo[] info = this.getTagInfo().getTagLibrary().getTags();
    String message = null;

    if (info != null) {
      if (info.length == 2) {
        for (int i = 0; i < info.length; i++) {
          if (TEST_TAG.equals(info[i].getTagName())) {
            String bodyContent = info[i].getBodyContent();
            if (!"JSP".equals(bodyContent)) {
              message = "Test FAILED.  Expected TagInfo.getBodyContent() "
                  + "to return 'JSP'.  Received: " + bodyContent;
              break;
            }
          } else if (TEST_TAG2.equals(info[i].getTagName())) {
            String bodyContent = info[i].getBodyContent().toLowerCase();
            if (!"tagdependent".equals(bodyContent)) {
              message = "Test FAILED.  Expected TagInfo.getBodyContent() "
                  + "to return 'tagdependent'.  Received: " + bodyContent;
              break;
            }
          } else {
            message = "Test FAILED.  Unexpected tag returned: "
                + info[i].getTagName();
            break;
          }
        }
      } else {
        message = "Test FAILED.  Expected a TagInfo array length of 2, but was "
            + "actually " + info.length;
      }
    } else {
      message = "Test FAILED.  The TagInfo array provided to test method"
          + " was null";
    }
    return message;
  }

  /**
   * Validates TagInfo.getInfoString().
   * 
   * @return null if the test passes, otherwise a String with a failure message.
   */
  public String getInfoString() {
    debug("in getInfoString()");
    TagInfo[] info = this.getTagInfo().getTagLibrary().getTags();
    String message = null;

    if (info != null && info.length == 2) {
      String description = info[0].getInfoString();
      if (!"simple test tag".equals(description)) {
        message = "Test FAILED.  Expected TagInfo.getInfoString() to "
            + "return 'simple test tag'.  Received: " + description;
      }
    } else {
      message = "Test FAILED.  The TagInfo array provided to test method"
          + " was null or did not have a length of 2.";
    }

    return message;
  }

  /**
   * Validates TagInfo.getTagLibrary(). This also indirectly validates
   * TagInfo.setTagLibrary() as this must be called by container in order for
   * getTagLibrary() to return anything valid.
   * 
   * @return null if the test passes, otherwise a String with a failure message.
   */
  public String getTagLibrary() {
    debug("in getTagLibrary");
    TagInfo[] info = this.getTagInfo().getTagLibrary().getTags();
    String message = null;

    if (info != null && info.length == 2) {
      if (info[0].getTagLibrary() == null) {
        message = "Test FAILED.  Expected TagInfo.getTagLibrary() to return "
            + "a non-null value.";
      }
    } else {
      message = "Test FAILED.  The TagInfo array provided to test method"
          + " was null or did not have a length of 2.";
    }
    return message;
  }

  /**
   * Validates TagInfo.getDisplayName().
   * 
   * @return null if the test passes, otherwise a String with a failure message.
   */
  public String getDisplayName() {
    debug("in getDisplayName");
    TagInfo[] info = this.getTagInfo().getTagLibrary().getTags();
    String message = null;

    if (info != null && info.length == 2) {
      String displayName = info[0].getDisplayName();
      if ("test tag".equals(displayName)) {
        if (info[1].getDisplayName() != null) {
          message = "Test FAILED.  Expected TagInfo.getDisplayName() to return "
              + "null if the tag entry has no display-name element defined.";
        }
      } else {
        message = "Test FAILED.  Expected TagInfo.getDisplayName() to return "
            + "'test tag'.  Received: " + displayName;
      }
    } else {
      message = "Test FAILED.  The TagInfo array provided to test method"
          + " was null or did not have a length of 2.";
    }
    return message;
  }

  /**
   * Validates TagInfo.getSmallIcon().
   * 
   * @return null if the test passes, otherwise a String with a failure message.
   */
  public String getSmallIcon() {
    debug("in getSmallIcon");
    TagInfo[] info = this.getTagInfo().getTagLibrary().getTags();
    String message = null;

    if (info != null && info.length == 2) {
      String icon = info[0].getSmallIcon();
      if ("/16/icon.jpg".equals(icon)) {
        if (info[1].getSmallIcon() != null) {
          message = "Test FAILED.  Expected TagInfo.getSmallIcon() to "
              + "return null of no small-icon is defined for a tag.";
        }
      } else {
        message = "Test FAILED.  Expected TagInfo.getSmallIcon() to return "
            + "'/16/icon.jpg'.  Received: " + icon;
      }
    } else {
      message = "Test FAILED.  The TagInfo array provided to test method"
          + " was null or did not have a length of 2.";
    }
    return message;
  }

  /**
   * Validates TagInfo.getLargeIcon().
   * 
   * @return null if the test passes, otherwise a String with a failure message.
   */
  public String getLargeIcon() {
    debug("in getLargeIcon");
    TagInfo[] info = this.getTagInfo().getTagLibrary().getTags();
    String message = null;

    if (info != null && info.length == 2) {
      String icon = info[0].getLargeIcon();
      if ("/32/icon.jpg".equals(icon)) {
        if (info[1].getLargeIcon() != null) {
          message = "Test FAILED.  Expected TagInfo.getLargeIcon() to "
              + "return null of no large-icon is defined for a tag.";
        }
      } else {
        message = "Test FAILED.  Expected TagInfo.getLargeIcon() to return "
            + "'/32/icon.jpg'.  Received: " + icon;
      }
    } else {
      message = "Test FAILED.  The TagInfo array provided to test method"
          + " was null or did not have a length of 2.";
    }
    return message;
  }

  /**
   * Validates TagInfo.getTagVariableInfos().
   * 
   * @return null if the test passes, otherwise a String with a failure message.
   */
  public String getTagVariableInfos() {
    debug("in getTagVariableInfos");
    TagInfo[] info = this.getTagInfo().getTagLibrary().getTags();
    String message = null;
    boolean processed = false;

    if (info != null && info.length == 2) {
      for (int i = 0; i < info.length; i++) {
        if (TEST_TAG.equals(info[i].getTagName())) {
          TagVariableInfo[] varInfo = info[i].getTagVariableInfos();
          if (varInfo != null) {
            if (varInfo.length != 0) {
              message = "Test FAILED.  Expected TagInfo.getTagVariableInfos() "
                  + "to return zero-length array for a tag that defines no variables."
                  + "  Actual length returned was: " + varInfo.length
                  + " for tag '" + TEST_TAG + "'";
              break;
            }
          } else {
            message = "Test FAILED.  Expected TagInfo.getTagVariableInfos() to return a "
                + "zero-length array and not null for tag '" + TEST_TAG + "'";
            break;
          }
        }
        if (TEST_TAG2.equals(info[i].getTagName())) {
          processed = true;
          TagVariableInfo[] varInfo = info[i].getTagVariableInfos();
          if (varInfo != null) {
            if (varInfo.length != 2) {
              message = "Test FAILED.  TagInfo.getTagVariableInfos() "
                  + "returned a length of " + varInfo.length
                  + " 'test2'.  Expected a length of 2.";
              break;
            }
          } else {
            message = "Test FAILED.  TagInfo.getTagVariableInfos() unexpectedly "
                + "returned null for tag 'test2'.";
            break;
          }
        }
      }
      if (!processed) {
        message = "Test FAILED.  Tag 'test2' was not available in the TagInfo array.";
      }
    } else {
      message = "Test FAILED.  The TagInfo array provided to test method"
          + " was null or did not have a length of 2.";
    }
    return message;
  }

  /**
   * Validates TagInfo.hasDynamicAttributes().
   * 
   * @return null if the test passes, otherwise a String with a failure message.
   */
  public String hasDynamicAttributes() {
    debug("in getTagVariableInfos");
    TagInfo[] info = this.getTagInfo().getTagLibrary().getTags();
    String message = null;

    if (info != null && info.length == 2) {
      for (int i = 0; i < info.length; i++) {
        if (TEST_TAG.equals(info[i].getTagName())) {
          if (!info[i].hasDynamicAttributes()) {
            message = "Test FAILED.  TagInfo.hasDynamicAttributes() returned "
                + "false for tag 'test'.";
          }
        } else if (TEST_TAG2.equals(info[i].getTagName())) {
          if (info[i].hasDynamicAttributes()) {
            message = "Test FAILED.  TagInfo.hasDynamicAttributes() returned "
                + "true for tag 'test2";
          }
        } else {
          message = "Test FAILED.  Unexpected tag returned from TagInfo array.  "
              + "Tag: " + info[i].getTagName();
        }
      }
    } else {
      message = "Test FAILED.  The TagInfo array provided to test method"
          + " was null or did not have a length of 2.";
    }
    return message;
  }

  /**
   * Validates TagInfo.setTagExtraInfo(TagExtraInfo).
   * 
   * @return null if the test passes, otherwise a String with a failure message.
   */
  public String setTagExtraInfoTest() {
    debug("in setTagExtraInfoTest");
    TagInfo info = this.getTagInfo();
    String message = null;

    if (info != null) {
      try {
        TagExtraInfo tei0 = info.getTagExtraInfo();
        info.setTagExtraInfo(tei0);
        TagExtraInfo tei1 = info.getTagExtraInfo();
        if (null == tei0 && null == tei1) {
          // both are null, and they are same
        } else if (null == tei0 || null == tei1) {
          message = "Test FAILED. Invoking TagInfo.getTagExtraInfo() twice got a null and a non-null TagExtraInfo.";
        } else if (!tei0.equals(tei1)) {
          message = "Test FAILED. Invoking TagInfo.getTagExtraInfo() twice and returned TagExtraInfo instances have different content. tei0:"
              + tei0.toString() + "\n\ntei1:" + tei1.toString();
        }
      } catch (Throwable th) {
        message = "Test FAILED. Throwable caught when invoking TagInfo.getTagExtraInfo() and TagInfo.setTagExtraInfo(TagExtraInfo).";
      }
    } else {
      message = "Test FAILED.  The TagInfo provided to test method was null";
    }
    return message;
  }

  /**
   * Validates TagInfo.setTagLibrary(TagLibraryInfo).
   * 
   * @return null if the test passes, otherwise a String with a failure message.
   */
  public String setTagLibraryTest() {
    debug("in setTagLibraryTest");
    TagInfo info = this.getTagInfo();
    String message = null;

    if (info != null) {
      try {
        TagLibraryInfo tlib0 = info.getTagLibrary();
        info.setTagLibrary(tlib0);
        TagLibraryInfo tlib1 = info.getTagLibrary();
        if (null == tlib0 && null == tlib1) {
          // both are null, and they are same
        } else if (null == tlib0 || null == tlib1) {
          message = "Test FAILED. Invoking TagInfo.getTagLibrary() twice got a null and a non-null TagLibraryInfo.";
        } else if (!tlib0.equals(tlib1)) {
          message = "Test FAILED. Invoking TagInfo.getTagLibrary() twice and returned TagLibraryInfo instances have different content. tlib0:"
              + tlib0.toString() + "\n\ntlib1:" + tlib1.toString();
        }
      } catch (Throwable th) {
        message = "Test FAILED. Throwable caught when invoking TagInfo.getTagLibrary() and TagInfo.setTagLibrary(TagLibraryInfo).";
      }
    } else {
      message = "Test FAILED.  The TagInfo provided to test method was null";
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
    JspTestUtil.debug("[TagInfoTEI] " + message);
  }
}
