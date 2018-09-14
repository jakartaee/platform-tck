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

/*
 * @(#)URLClient.java	1.2 10/09/02
 */

package com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.taglibraryinfo;

import java.io.PrintWriter;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

/**
 * Test client for TagLibraryInfo. Implementation note, all tests are performed
 * within a TagExtraInfo class. If the test fails, a translation error will be
 * generated and a ValidationMessage array will be returned.
 */
public class URLClient extends AbstractUrlClient {

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    URLClient theTests = new URLClient();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  /**
   * Entry point for same-VM execution. In different-VM execution, the main
   * method delegates to this method.
   */
  public Status run(String args[], PrintWriter out, PrintWriter err) {

    setContextRoot("/jsp_tagvarinfo_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Run tests */

  // ============================================ Tests ======

  /*
   * @testName: tagLibraryInfoGetURITest
   * 
   * @assertion_ids: JSP:JAVADOC:216
   * 
   * @test_Strategy: Validate that TagLibaryInfo.getURI() returns the URI as
   * defined by the taglib directive within a JSP page.
   */
  public void tagLibraryInfoGetURITest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taglibinfo_web/GetURITest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagLibraryInfoGetPrefixStringTest
   * 
   * @assertion_ids: JSP:JAVADOC:218
   * 
   * @testStrategy: Validate that TagLibraryInfo.getPrefixString() returns the
   * prefix as specified by the prefix attribute of the taglib directive within
   * a JSP page.
   */
  public void tagLibraryInfoGetPrefixStringTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taglibinfo_web/GetPrefixStringTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagLibraryInfoGetShortNameTest
   * 
   * @assertion_ids: JSP:JAVADOC:219
   * 
   * @testStrategy: Validate that TagLibraryInfo.getShortName() returns the
   * short name of the tag library as specified by the &lt;short-name&gt;
   * element in a TLD.
   */
  public void tagLibraryInfoGetShortNameTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taglibinfo_web/GetShortNameTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagLibraryInfoGetReliableURNTest
   * 
   * @assertion_ids: JSP:JAVADOC:220
   * 
   * @testStrategy: Validate that TagLibraryInfo.getShortName() returns the uri
   * of the tag library as specified by the &lt;uri&gt; element in a TLD.
   */
  public void tagLibraryInfoGetReliableURNTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taglibinfo_web/GetReliableURNTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagLibraryInfoGetInfoStringTest
   * 
   * @assertion_ids: JSP:JAVADOC:221
   * 
   * @testStrategy: Validate that TagLibraryInfo.getInfoString() returns the
   * description of the tag library as specified by the &lt;description&gt;
   * element in a TLD.
   */
  public void tagLibraryInfoGetInfoStringTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taglibinfo_web/GetInfoStringTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagLibraryInfoGetRequiredVersionTest
   * 
   * @assertion_ids: JSP:JAVADOC:222
   * 
   * @testStrategy: Validate that TagLibraryInfo.getRequiredVersion() returns
   * the require version of the tag library as specified by the
   * &lt;required-version&gt; element in a TLD.
   */
  public void tagLibraryInfoGetRequiredVersionTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taglibinfo_web/GetRequiredVersionTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagLibraryInfoGetTagsTest
   * 
   * @assertion_ids: JSP:JAVADOC:223
   * 
   * @testStrategy: Validate that TagLibraryInfo.getTags() returns an array of
   * TagInfo objects based off the tags defined by the &lt;tag&gt; elements in a
   * TLD.
   */
  public void tagLibraryInfoGetTagsTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taglibinfo_web/GetTagsTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagLibraryInfoGetTagFilesTest
   * 
   * @assertion_ids: JSP:JAVADOC:224
   * 
   * @testStrategy: Validate that TagLibraryInfo.getTagFiles() returns an array
   * of TagFileInfo objects based off the tags files defined by the
   * &lt;tag-file&gt; elements in a TLD.
   */
  public void tagLibraryInfoGetTagFilesTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taglibinfo_web/GetTagFilesTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagLibraryInfoGetTagTest
   * 
   * @assertion_ids: JSP:JAVADOC:225
   * 
   * @testStrategy: Validate that TagLibraryInfo.getTag() returns a TagInfo
   * object based off the provided discriminate.
   */
  public void tagLibraryInfoGetTagTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taglibinfo_web/GetTagTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagLibraryInfoGetTagFileTest
   * 
   * @assertion_ids: JSP:JAVADOC:226
   * 
   * @testStrategy: Validate that TagLibraryInfo.getTagFile() returns a
   * TagFileInfo object based off the provided discriminate.
   */
  public void tagLibraryInfoGetTagFileTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taglibinfo_web/GetTagFileTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagLibraryInfoGetFunctionsTest
   * 
   * @assertion_ids: JSP:JAVADOC:227
   * 
   * @testStrategy: Validate that TagLibraryInfo.getFunctions() returns an array
   * of FunctionInfo objects based off the functions defined by the
   * &lt;function&gt; elements in a TLD.
   */
  public void tagLibraryInfoGetFunctionsTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taglibinfo_web/GetFunctionsTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagLibraryInfoGetFunctionTest
   * 
   * @assertion_ids: JSP:JAVADOC:228
   * 
   * @testStrategy: Validate that TagLibraryInfo.getFunction() returns a
   * FunctionInfo object based off the provided discriminate.
   */
  public void tagLibraryInfoGetFunctionTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taglibinfo_web/GetFunctionTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagLibraryInfoGetTagLibraryInfosTest
   * 
   * @assertion_ids: JSP:JAVADOC:440
   * 
   * @testStrategy: Validate TagLibaryInfo.getTagLibraryInfos() returns an array
   * of TagLibraryInfo objects for all taglibraries declared in the JSP.
   */
  public void tagLibraryInfoGetTagLibraryInfosTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taglibinfo_web/GetTagLibraryInfosTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }
}
