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

package com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.taglibraryinfo;

import java.util.Arrays;

import javax.servlet.jsp.tagext.FunctionInfo;
import javax.servlet.jsp.tagext.TagFileInfo;
import javax.servlet.jsp.tagext.TagInfo;
import javax.servlet.jsp.tagext.TagLibraryInfo;

import com.sun.ts.tests.jsp.common.util.BaseTCKExtraInfo;
import com.sun.ts.tests.jsp.common.util.JspTestUtil;

/**
 * A TagExtraInfo implementation to validate the methods of
 * javax.servlet.jsp.tagext.TagLibraryInfo.
 */
public class TagLibraryInfoTEI extends BaseTCKExtraInfo {

  /**
   * Default constructor.
   */
  public TagLibraryInfoTEI() {
  }

  // ------------------------------------------- Test Definitions ----------

  // TagLibraryInfo.getURI()
  public String getURI() {
    TagLibraryInfo info = this.getTagInfo().getTagLibrary();
    JspTestUtil.debug("[TagLibraryInfoTEI] In getURL()");
    String uri = info.getURI();
    if (!"http://java.sun.com/tck/jsp/taglibinfo".equals(uri)) {
      return "Test FAILED.  TagLibraryInfo.getURI() returned an unexpected "
          + "value.  Expected: 'http://java.sun.com/tck/jsp/taglibinfo', "
          + "received: " + uri;
    }
    return null;
  }

  // TagLibraryInfo.getTagDir()
  // public String getTagDir(Object o) {
  // TagLibraryInfo info = (TagLibraryInfo) o;
  // JspTestUtil.debug("[TagLibraryInfoTEI] In getTagDir()");
  // String tagDir = info.getTag("TagFile1").getTagLibrary().getTagdir();
  // if (!"/WEB-INF/tags/taglibinfo".equals(tagDir)) {
  // return "Test FAILED. TagLibraryInfo.getTagDir() returned an unexpected " +
  // "value. Expected: '/WEB-INF/tags/taglibinfo', received: " + tagDir;
  // }
  // return null;
  // }

  // TagLibraryInfo.getPrefixString()
  public String getPrefixString() {
    TagLibraryInfo info = this.getTagInfo().getTagLibrary();
    JspTestUtil.debug("[TagLibraryInfoTEI] In getPrefixString()");
    String tagPrefix = info.getPrefixString();
    if (!"taglibinfo".equals(tagPrefix)) {
      return "Test FAILED.  TagLibraryInfo.getPrefixString() returned an "
          + "unexpected value.  Expected 'test', received: " + tagPrefix;
    }
    return null;
  }

  // TagLibraryInfo.getShortName()
  public String getShortName() {
    TagLibraryInfo info = this.getTagInfo().getTagLibrary();
    JspTestUtil.debug("[TagLibraryInfoTEI] In getShortName()");
    String shortName = info.getShortName();
    if (!"taglibinfo".equals(shortName)) {
      return "Test FAILED.  TagLibraryInfo.getShortName() returned an "
          + "unexpected value.  Expected 'taglibinfo', received: " + shortName;
    }
    return null;
  }

  // TagLibraryInfo.getReliableURN()
  public String getReliableURN() {
    TagLibraryInfo info = this.getTagInfo().getTagLibrary();
    JspTestUtil.debug("[TagLibraryInfoTEI] In getReliableURN()");
    String urn = info.getReliableURN();
    if (!"http://java.sun.com/tck/jsp/taglibinfo".equals(urn)) {
      return "Test FAILED.  TagLibraryInfo.getReliableURN() returned an unexpected "
          + "value.  Expected: 'http://java.sun.com/tck/jsp/taglibinfo', "
          + "received: " + urn;
    }
    return null;
  }

  // TagLibraryInfo.getInfoString()
  public String getInfoString() {
    TagLibraryInfo info = this.getTagInfo().getTagLibrary();
    JspTestUtil.debug("[TagLibraryInfoTEI] In getInfoString()");
    String infoString = info.getInfoString();
    if (!"TagLibraryInfo Validation".equals(infoString)) {
      return "Test FAILED.  TagLibraryInfo.getInfoString() returned an unexpected "
          + "value.  Expected: 'TagLibraryInfo Validation', received: "
          + infoString;
    }
    return null;
  }

  // TagLibraryInfo.getRequiredVersion()
  public String getRequiredVersion() {
    TagLibraryInfo info = this.getTagInfo().getTagLibrary();
    JspTestUtil.debug("[TagLibraryInfoTEI] In getRequiredVersion()");
    String version = info.getRequiredVersion();
    if (!"2.0".equals(version)) {
      return "Test FAILED.  TagLibraryInfo.getRequiredVersion() returned an "
          + "unexpected value.  Expected: '2.0', received: " + version;
    }
    return null;
  }

  // TagLibraryInfo.getTags()
  public String getTags() {
    TagLibraryInfo info = this.getTagInfo().getTagLibrary();
    JspTestUtil.debug("[TagLibraryInfoTEI] In getTags()");
    TagInfo[] tagInfo = info.getTags();
    if (tagInfo == null) {
      return "Test FAILED.  TagLibraryInfo.getTags() returned a null "
          + "value.";
    }
    if (tagInfo.length != 2) {
      return "Test FAILED.  Expected the TagInfo array length as returned "
          + "by TagLibraryInfo.getTags() to be 2, actual length was: "
          + tagInfo.length;
    }
    return null;
  }

  // TagLibraryInfo.getTagFiles()
  public String getTagFiles() {
    TagLibraryInfo info = this.getTagInfo().getTagLibrary();
    JspTestUtil.debug("[TagLibraryInfoTEI] In getTagFiles()");
    TagFileInfo[] tagInfo = info.getTagFiles();
    if (tagInfo == null) {
      return "Test FAILED.  TagLibraryInfo.getTagFiles() returned a null "
          + "value.";
    }
    if (tagInfo.length != 2) {
      return "Test FAILED.  Expected the TagFileInfo array length as returned "
          + "by TagLibraryInfo.getTagFiles() to be 2, actual length was: "
          + tagInfo.length;
    }
    return null;
  }

  // TagLibraryInfo.getTag()
  public String getTag() {
    TagLibraryInfo info = this.getTagInfo().getTagLibrary();
    JspTestUtil.debug("[TagLibraryInfoTEI] In getTag()");
    TagInfo tagInfo1 = info.getTag("tag1");
    TagInfo tagInfo2 = info.getTag("tag2");
    if (tagInfo1 == null) {
      return "Test FAILED.  TagLibraryInfo.getTag('tag1') returned a null "
          + "value.";
    }
    if (tagInfo2 == null) {
      return "Test FAILED.  TagLibraryInfo.getTag('tag2') returned a null "
          + "value.";
    }
    return null;
  }

  // TagLibraryInfo.getTagFile()
  public String getTagFile() {
    TagLibraryInfo info = this.getTagInfo().getTagLibrary();
    JspTestUtil.debug("[TagLibraryInfoTEI] In getTagFile()");
    TagFileInfo tagInfo1 = info.getTagFile("TagFile1");
    TagFileInfo tagInfo2 = info.getTagFile("TagFile2");
    if (tagInfo1 == null) {
      return "Test FAILED.  TagLibraryInfo.getTagFile('TagFile1') returned a null "
          + "value.";
    }
    if (tagInfo2 == null) {
      return "Test FAILED.  TagLibraryInfo.getTagFile('TagFile2') returned a null "
          + "value.";
    }
    return null;
  }

  // TagLibraryInfo.getFunctions()
  public String getFunctions() {
    TagLibraryInfo info = this.getTagInfo().getTagLibrary();
    JspTestUtil.debug("[TagLibraryInfoTEI] In getFunctions()");
    FunctionInfo[] funcInfo = info.getFunctions();
    if (funcInfo == null) {
      return "Test FAILED.  TagLibraryInfo.getFunctions() returned a null "
          + "value.";
    }

    if (funcInfo.length != 2) {
      return "Test FAILED.  Expected the FunctionInfo array length as returned "
          + "by TagLibraryInfo.getFunctions() to be 2, actual length was: "
          + funcInfo.length;
    }
    return null;
  }

  // TagLibraryInfo.getFunction()
  public String getFunction() {
    TagLibraryInfo info = this.getTagInfo().getTagLibrary();
    JspTestUtil.debug("[TagLibraryInfoTEI] In getFunction()");
    FunctionInfo func1 = info.getFunction("lowerCase");
    FunctionInfo func2 = info.getFunction("upperCase");
    if (func1 == null) {
      return "Test FAILED.  TagLibraryInfo.getFunction('lowerCase') returned a null "
          + "value.";
    }
    if (func2 == null) {
      return "Test FAILED.  TagLibraryInfo.getFunction('upperCase') returned a null "
          + "value.";
    }
    return null;
  }

  // TagLibraryInfo.getTagLibraryInfos()
  public String getTagLibraryInfos() {
    TagLibraryInfo[] infos = this.getTagInfo().getTagLibrary()
        .getTagLibraryInfos();
    if (infos.length != 2) {
      return "Test FAILED.  Expected length of array returned by getTagLibraryInfos()"
          + " to be 2, but the actual length was " + infos.length;
    }

    String[] controlUris = { "http://java.sun.com/tck/jsp/taglibinfo",
        "http://java.sun.com/tck/jsp/taglibinfo2" };

    String[] testUris = new String[2];

    for (int i = 0, size = infos.length; i < size; i++) {
      testUris[i] = infos[i].getReliableURN();
    }

    Arrays.sort(controlUris);
    Arrays.sort(testUris);

    if (!Arrays.equals(controlUris, testUris)) {
      return "Test FAILED.  Unexpected TagLibrary information returned by"
          + "getTagLibraryInfos().\n  Expected to find two TagLibraryInfo instances"
          + " with reliable URNs of 'http://java.sun.com/tck/jsp/taglibraryinfo'"
          + " and 'http://java.sun.com/tck/jsp/taglibinfo2'.\nThe test found the"
          + " following URIs '" + testUris[0] + "', '" + testUris[1] + "'.";
    }

    return null;
  }
}
