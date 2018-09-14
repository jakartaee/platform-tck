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

package com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.taginfo;

import java.io.PrintWriter;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

/**
 * Test client for TagInfo. Implementation note, all tests are performed within
 * a TagExtraInfo class. If the test fails, a translation error will be
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

    setContextRoot("/jsp_taginfo_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Run tests */

  // ============================================ Tests ======

  /*
   * @testName: tagInfoGetTagNameTest
   * 
   * @assertion_ids: JSP:JAVADOC:232
   * 
   * @test_Strategy: Validate TagInfo.getTagName() returns the expected values
   * based on what is defined in the TLD.
   */
  public void tagInfoGetTagNameTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taginfo_web/GetTagNameTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagInfoGetAttributesTest
   * 
   * @assertion_ids: JSP:JAVADOC:233
   * 
   * @test_Strategy: Validate TagInfo.getAttributes() returns the expected
   * values based on what is defined in the TLD.
   */
  public void tagInfoGetAttributesTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taginfo_web/GetAttributesTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagInfoGetTagExtraInfoTest
   * 
   * @assertion_ids: JSP:JAVADOC:237;JSP:JAVADOC:238
   * 
   * @test_Strategy: Validate TagInfo.getTagExtraInfo() returns the expected
   * values based on what is defined in the TLD. This implicitly tests
   * TagInfo.setTagExtraInfo().
   */
  public void tagInfoGetTagExtraInfoTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taginfo_web/GetTagExtraInfoTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagInfoGetTagClassNameTest
   * 
   * @assertion_ids: JSP:JAVADOC:239
   * 
   * @test_Strategy: Validate TagInfo.getTagClassName() returns the expected
   * values based on what is defined in the TLD.
   */
  public void tagInfoGetTagClassNameTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taginfo_web/GetTagClassNameTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagInfoGetBodyContentTest
   * 
   * @assertion_ids: JSP:JAVADOC:240
   * 
   * @test_Strategy: Validate TagInfo.getBodyContent() returns the expected
   * values based on what is defined in the TLD.
   */
  public void tagInfoGetBodyContentTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taginfo_web/GetBodyContentTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagInfoGetInfoStringTest
   * 
   * @assertion_ids: JSP:JAVADOC:241
   * 
   * @test_Strategy: Validate TagInfo.getInfoString() returns the expected
   * values based on what is defined in the TLD.
   */
  public void tagInfoGetInfoStringTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taginfo_web/GetInfoStringTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagInfoGetTagLibraryTest
   * 
   * @assertion_ids: JSP:JAVADOC:242;JSP:JAVADOC:243
   * 
   * @test_Strategy: Validate TagInfo.getAttributes() returns the expected
   * values based on what is defined in the TLD. This implicitly tests
   * TagInfo.setTagLibrary().
   */
  public void tagInfoGetTagLibraryTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taginfo_web/GetTagLibraryTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagInfoGetDisplayNameTest
   * 
   * @assertion_ids: JSP:JAVADOC:244
   * 
   * @test_Strategy: Validate TagInfo.getDisplayName() returns the expected
   * values based on what is defined in the TLD.
   */
  public void tagInfoGetDisplayNameTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taginfo_web/GetDisplayNameTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagInfoGetSmallIconTest
   * 
   * @assertion_ids: JSP:JAVADOC:245
   * 
   * @test_Strategy: Validate TagInfo.getSmallIconName() returns the expected
   * values based on what is defined in the TLD.
   */
  public void tagInfoGetSmallIconTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taginfo_web/GetSmallIconTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagInfoGetLargeIconTest
   * 
   * @assertion_ids: JSP:JAVADOC:246
   * 
   * @test_Strategy: Validate TagInfo.getLargeIconName() returns the expected
   * values based on what is defined in the TLD.
   */
  public void tagInfoGetLargeIconTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taginfo_web/GetLargeIconTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagInfoGetTagVariableInfosTest
   * 
   * @assertion_ids: JSP:JAVADOC:247
   * 
   * @test_Strategy: Validate TagInfo.getTagVariableInfos() returns the expected
   * values based on what is defined in the TLD.
   */
  public void tagInfoGetTagVariableInfosTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taginfo_web/GetTagVariableInfosTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagInfoHasDynamicAttributesTest
   * 
   * @assertion_ids: JSP:JAVADOC:248
   * 
   * @test_Strategy: Validate TagInfo.hasDynamicAttributes() returns the
   * expected values based on what is defined in the TLD.
   */
  public void tagInfoHasDynamicAttributesTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taginfo_web/HasDynamicAttributesTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * testName: tagInfoIsValidTest assertion_ids: JSP:JAVADOC:235 test_Strategy:
   * Translation-time validation of the attributes. This is a convenience method
   * on the associated TagExtraInfo class. This method should be exercised with
   * a jsp 1.1 tld. See tagInfoConstructor11Test.
   */
  public void tagInfoIsValidTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taginfo_web/IsValidTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagInfoSetTagExtraInfoTest
   * 
   * @assertion_ids: JSP:JAVADOC:237
   * 
   * @test_Strategy: Set the instance for extra tag information
   */
  public void tagInfoSetTagExtraInfoTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taginfo_web/SetTagExtraInfoTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagInfoSetTagLibraryTest
   * 
   * @assertion_ids: JSP:JAVADOC:242
   * 
   * @test_Strategy: Set the TagLibraryInfo property.
   */
  public void tagInfoSetTagLibraryTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taginfo_web/SetTagLibraryTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

}
