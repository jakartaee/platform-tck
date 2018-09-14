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
 * @(#)JSTLClient.java	1.4 03/21/03
 */

package com.sun.ts.tests.jstl.spec.etu.functions;

import java.io.PrintWriter;
import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.jstl.common.client.AbstractUrlClient;

public class JSTLClient extends AbstractUrlClient {

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /** Creates new JSTLClient */
  public JSTLClient() {
  }

  /*
   * public functions
   * ========================================================================
   */

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    JSTLClient theTests = new JSTLClient();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  /**
   * Entry point for same-VM execution. In different-VM execution, the main
   * method delegates to this method.
   */
  public Status run(String args[], PrintWriter out, PrintWriter err) {

    setContextRoot("/jstl_etu_func_web");
    setGoldenFileDir("/jstl/spec/etu/functions");

    return super.run(args, out, err);
  }

  /*
   * @testName: functionsContainsTest
   * 
   * @assertion_ids: JSTL:SPEC:76.1; JSTL:SPEC:76.1.1; JSTL:SPEC:76.1.3;
   * JSTL:SPEC:76.2; JSTL:SPEC:76.2.1
   * 
   * @test_Strategy: Validate the contains function returns the proper value,
   * true or false, depending on whether the provided substring is found in the
   * provided string value.
   */
  public void functionsContainsTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web/functionsContainsTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsContainsStringSubstringEqualTest
   * 
   * @assertion_ids: JSTL:SPEC:76.2; JSTL:SPEC:76.2.1
   * 
   * @test_Strategy: Validate that true is returned when both the 'string' and
   * 'substring' arguments are equal.
   */
  public void functionsContainsStringSubstringEqualTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web/functionsContainsStringSubstringEqualTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: functionsContainsNullStringTest
   * 
   * @assertion_ids: JSTL:SPEC:76.1.2; JSTL:SPEC:76.2.1
   * 
   * @test_Strategy: Validate that false is returned if the 'string' argument is
   * provided a null value.
   */
  public void functionsContainsNullStringTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web/functionsContainsNullStringTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: functionsContainsNullSubstringTest
   * 
   * @assertion_ids: JSTL:SPEC:76.1.4; JSTL:SPEC:76.2; JSTL:SPEC:76.2.2
   * 
   * @test_Strategy: Validate that true is returned if the 'substring' argument
   * is provided a null value.
   */
  public void functionsContainsNullSubstringTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web/functionsContainsNullSubstringTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: functionsContainsIgnoreCaseTest
   * 
   * @assertion_ids: JSTL:SPEC:77.1; JSTL:SPEC:77.1.1; JSTL:SPEC:77.1.3;
   * JSTL:SPEC:77.2; JSTL:SPEC:77.2.1
   * 
   * @test_Strategy: Validate the containsIgnoreCase function returns the proper
   * value, true or false, depending on whether the provided substring is found
   * in the provided string value.
   */
  public void functionsContainsIgnoreCaseTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web/functionsContainsIgnoreCaseTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "Test PASSED|Test PASSED|Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsContainsIgnoreCaseStringSubstringEqualTest
   * 
   * @assertion_ids: JSTL:SPEC:77.2; JSTL:SPEC:77.2.1
   * 
   * @test_Strategy: Validate that true is returned when both the 'string' and
   * 'substring' arguments are equal.
   */
  public void functionsContainsIgnoreCaseStringSubstringEqualTest()
      throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web/functionsContainsIgnoreCaseStringSubstringEqualTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: functionsContainsIgnoreCaseNullStringTest
   * 
   * @assertion_ids: JSTL:SPEC:77.1.2; JSTL:SPEC:77.2.1
   * 
   * @test_Strategy: Validate that false is returned if the 'string' argument is
   * provided a null value.
   */
  public void functionsContainsIgnoreCaseNullStringTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web/functionsContainsIgnoreCaseNullStringTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: functionsContainsIgnoreCaseNullSubstringTest
   * 
   * @assertion_ids: JSTL:SPEC:77.1.4; JSTL:SPEC:77.2; JSTL:SPEC:77.2.2
   * 
   * @test_Strategy: Validate that true is returned if the 'substring' argument
   * is provided a null value.
   */
  public void functionsContainsIgnoreCaseNullSubstringTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web/functionsContainsIgnoreCaseNullSubstringTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: functionsEscapeXmlTest
   * 
   * @assertion_ids: JSTL:SPEC:79.1; JSTL:SPEC:79.2
   * 
   * @test_Strategy: Validate that escapeXml(String) escapes the characters
   * defined in section 4.2 of the JSTL specification.
   */
  public void functionsEscapeXmlTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web/functionsEscapeXmlTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsEscapeXmlNullStringTest
   * 
   * @assertion_ids: JSTL:SPEC:79.3; JSTL:SPEC:79.4
   * 
   * @test_Strategy: Validate that if escapeXml(String) is provided a null
   * value, an empty String is returned.
   */
  public void functionsEscapeXmlNullStringTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web/functionsEscapeXmlNullStringTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsIndexOfTest
   * 
   * @assertion_ids: JSTL:SPEC:80.1.1; JSTL:SPEC:80.1.3; JSTL:SPEC:80.2;
   * JSTL:SPEC:80.2.1; JSTL:SPEC:80.2.2
   * 
   * @test_Strategy: Validate that indexOf(string, substring) returns the proper
   * index value when the substring is and is not found.
   */
  public void functionsIndexOfTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web/functionsIndexOfTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED|Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsIndexOfNullEmptyStringTest
   * 
   * @assertion_ids: JSTL:SPEC:80.1.2; JSTL:SPEC:80.2.2
   * 
   * @test_Strategy: Validate that -1 is returned by indexOf(string, substring)
   * when the 'string' argument is either null or empty.
   */
  public void functionsIndexOfNullEmptyStringTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web/functionsIndexOfNullEmptyStringTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED|Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsIndexOfNullSubstringTest
   * 
   * @assertion_ids: JSTL:SPEC:80.1.4; JSTL:SPEC:80.2.3
   * 
   * @test_Strategy: Validate that 0 is returned by indexOf(string, substring)
   * when the substring argument is null.
   */
  public void functionsIndexOfNullSubstringTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web/functionsIndexOfNullSubstringTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsIndexOfEmptySubstringTest
   * 
   * @assertion_ids: JSTL:SPEC:80.2.3
   * 
   * @test_Strategy: Validate that 0 is returned by indexOf(string, substring)
   * when the substring argument is empty.
   */
  public void functionsIndexOfEmptySubstringTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web/functionsIndexOfEmptySubstringTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsLengthTest
   * 
   * @assertion_ids: JSTL:SPEC:82.1; JSTL:SPEC:82.2
   * 
   * @test_Strategy: Validate that the length(input) function returns the length
   * for the following: Arrays, Collections, Iterator, Enumeration, Map and
   * Strings
   *
   */
  public void functionsLengthTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web/functionsLengthTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "Test PASSED|Test PASSED|Test PASSED|Test PASSED|Test PASSED|Test PASSED|Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsLengthNullInputTest
   * 
   * @assertion_ids: JSTL:SPEC:82.3
   * 
   * @test_Strategy: Validate that if a null input value is provided to
   * length(index) the function returns 0.
   */
  public void functionsLengthNullInputTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsLengthNullInputTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsLengthEmptyStringTest
   * 
   * @assertion_ids: JSTL:SPEC:82.4
   * 
   * @test_Strategy: Validate that if a an empty string is provided as input to
   * the length(input) function, 0 is returned.
   */
  public void functionsLengthEmptyStringTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web/functionsLengthEmptyStringInputTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsStartsWithTest
   * 
   * @assertion_ids: JSTL:SPEC:85.1; JSTL:SPEC:85.1.1; JSTL:SPEC:85.1.3;
   * JSTL:SPEC:85.2; JSTL:SPEC:85.2.1
   * 
   * @test_Strategy: Validate that startsWith returns the expected values based
   * on the input values.
   */
  public void functionsStartsWithTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsStartsWithTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED|Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsStartsWithNullPrefixTest
   * 
   * @assertion_ids: JSTL:SPEC:85.1.4; JSTL:SPEC:85.2.2
   * 
   * @test_Strategy: Validate that true is returned if the prefix provided is
   * null.
   */
  public void functionsStartsWithNullPrefixTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsStartsWithNullPrefixTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsStartsWithNullStringTest
   * 
   * @assertion_ids: JSTL:SPEC:85.1.2; JSTL:SPEC:85.2.1
   * 
   * @test_Strategy: Validate that false is returned if the input string
   * provided is null.
   */
  public void functionsStartsWithNullStringTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsStartsWithNullStringTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsStartsWithEmptyPrefixTest
   * 
   * @assertion_ids: JSTL:SPEC:85.2.2
   * 
   * @test_Strategy: Validate that true is returned when the prefix is an empty
   * String.
   */
  public void functionsStartsWithEmptyPrefixTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsStartsWithEmptyPrefixStringTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsStartsWithPrefixStringEqualTest
   * 
   * @assertion_ids: JSTL:SPEC:85.2.1
   * 
   * @test_Strategy: Validate that true is returned when the input string and
   * the prefix are equal.
   */
  public void functionsStartsWithPrefixStringEqualTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsStartsWithPrefixStringEqualTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsEndsWithTest
   * 
   * @assertion_ids: JSTL:SPEC:78.1; JSTL:SPEC:78.1.1; JSTL:SPEC:78.1.3;
   * JSTL:SPEC:78.2; JSTL:SPEC:78.2.1
   * 
   * @test_Strategy: Validate that endsWith returns the expected values based on
   * the input values.
   */
  public void functionsEndsWithTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsEndsWithTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED|Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsEndsWithNullSuffixTest
   * 
   * @assertion_ids: JSTL:SPEC:78.1.4; JSTL:SPEC:78.2.2
   * 
   * @test_Strategy: Validate that true is returned if the suffix provided is
   * null.
   */
  public void functionsEndsWithNullSuffixTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsEndsWithNullSuffixTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsEndsWithNullStringTest
   * 
   * @assertion_ids: JSTL:SPEC:78.1.2; JSTL:SPEC:78.2.1
   * 
   * @test_Strategy: Validate that false is returned if the input string
   * provided is null.
   */
  public void functionsEndsWithNullStringTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsEndsWithNullStringTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsEndsWithEmptySuffixTest
   * 
   * @assertion_ids: JSTL:SPEC:78.2.2
   * 
   * @test_Strategy: Validate that true is returned when the suffix is an empty
   * String.
   */
  public void functionsEndsWithEmptySuffixTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsEndsWithEmptySuffixStringTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsEndsWithSuffixStringEqualTest
   * 
   * @assertion_ids: JSTL:SPEC:78.2.1
   * 
   * @test_Strategy: Validate that true is returned when the input string and
   * the suffix are equal.
   */
  public void functionsEndsWithSuffixStringEqualTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsEndsWithSuffixStringEqualTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsSubstringTest
   * 
   * @assertion_ids: JSTL:SPEC:86.1; JSTL:SPEC:86.1.1; JSTL:SPEC:86.1.3;
   * JSTL:SPEC:86.1.5; JSTL:SPEC:86.2; JSTL:SPEC:86.2.1
   * 
   * @test_Strategy: Validate the return value of the substring function.
   */
  public void functionsSubstringTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsSubstringTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsSubstringNullStringTest
   * 
   * @assertion_ids: JSTL:SPEC:86.1.2; JSTL:SPEC:86.2.2
   * 
   * @test_Strategy: Validate that if a null string is provided to the
   * substring() function, an empty string is returned.
   */
  public void functionsSubstringNullStringTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsSubstringNullStringTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsSubstringNegativeBeginIndexTest
   * 
   * @assertion_ids: JSTL:SPEC:86.1.4
   * 
   * @test_Strategy: Validate that if value less than zero is provided as the
   * begin index, the begin index will be considered as zero.
   */
  public void functionsSubstringNegativeBeginIndexTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsSubstringNegativeBeginIndexTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsSubstringNegativeEndIndexTest
   * 
   * @assertion_ids: JSTL:SPEC:86.1.6
   * 
   * @test_Strategy: Validate that an end index less than zero will be converted
   * to the length of the input string.
   */
  public void functionsSubstringNegativeEndIndexTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsSubstringNegativeEndIndexTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsSubstringEndIndexGTStringLengthTest
   * 
   * @assertion_ids: JSTL:SPEC:86.1.6
   * 
   * @test_Strategy: Validate that an end index that is greater than the length
   * of the input string will be 'adjusted' to be the length of the string.
   */
  public void functionsSubstringEndIndexGTStringLengthTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsSubstringEndIndexGTStringLengthTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsSubstringBeginIndexGTLastIndexTest
   * 
   * @assertion_ids: JSTL:SPEC:86.2.2
   * 
   * @test_Strategy: Validate an emtpy string is returned when the begin index
   * is greater than the last index of the string.
   */
  public void functionsSubstringBeginIndexGTLastIndexTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsSubstringBeginIndexGTLastIndexTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsSubstringEndIndexLTBeginIndexTest
   * 
   * @assertion_ids: JSTL:SPEC:86.2.3
   * 
   * @test_Strategy: Validate that an end index that is less than the begin
   * index results in the function returning an emtpy string.
   */
  public void functionsSubstringEndIndexLTBeginIndexTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsSubstringEndIndexLTBeginIndexTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsSubstringAfterTest
   * 
   * @assertion_ids: JSTL:SPEC:87.1; JSTL:SPEC:87.1.1; JSTL:SPEC:87.1.3;
   * JSTL:SPEC:87.2.1; JSTL:SPEC:87.2.4
   * 
   * @test_Strategy: Validate the proper behavior of substringAfter() with valid
   * input values(when the substring is found and when it is not).
   */
  public void functionsSubstringAfterTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsSubstringAfterTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED|Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsSubstringAfterNullStringTest
   * 
   * @assertion_ids: JSTL:SPEC:87.1.2; JSTL:SPEC:87.2.2
   * 
   * @test_Strategy: Validate a null input string will result in an empty string
   * returned.
   */
  public void functionsSubstringAfterNullStringTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsSubstringAfterNullStringTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsSubstringAfterNullSubstringTest
   * 
   * @assertion_ids: JSTL:SPEC:87.1.4; JSTL:SPEC:87.2.3
   * 
   * @test_Strategy: Validate a null substring will result in the input string
   * being returned.
   */
  public void functionsSubstringAfterNullSubstringTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsSubstringAfterNullSubstringTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsSubstringAfterEmptySubstringTest
   * 
   * @assertion_ids: JSTL:SPEC:87.2.3
   * 
   * @test_Strategy: Validate an empty substring will result in the input string
   * being returned.
   */
  public void functionsSubstringAfterEmptySubstringTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsSubstringAfterEmptySubstringTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsSubstringBeforeTest
   * 
   * @assertion_ids: JSTL:SPEC:88.1; JSTL:SPEC:88.1.1; JSTL:SPEC:88.1.3;
   * JSTL:SPEC:88.2.1; JSTL:SPEC:88.2.4
   * 
   * @test_Strategy: Validate the proper behavior of substringBefore() with
   * valid input values(when the substring is found and when it is not).
   */
  public void functionsSubstringBeforeTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsSubstringBeforeTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED|Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsSubstringBeforeNullStringTest
   * 
   * @assertion_ids: JSTL:SPEC:88.1.2; JSTL:SPEC:88.2.2
   * 
   * @test_Strategy: Validate a null input string will result in an empty string
   * returned.
   */
  public void functionsSubstringBeforeNullStringTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsSubstringBeforeNullStringTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsSubstringBeforeNullSubstringTest
   * 
   * @assertion_ids: JSTL:SPEC:88.1.4; JSTL:SPEC:88.2.3
   * 
   * @test_Strategy: Validate a null substring will result in the input string
   * being returned.
   */
  public void functionsSubstringBeforeNullSubstringTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsSubstringBeforeNullSubstringTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsSubstringBeforeEmptySubstringTest
   * 
   * @assertion_ids: JSTL:SPEC:88.2.3
   * 
   * @test_Strategy: Validate an empty substring will result in the input string
   * being returned.
   */
  public void functionsSubstringBeforeEmptySubstringTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsSubstringBeforeEmptySubstringTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsToLowerCaseTest
   * 
   * @assertion_ids: JSTL:SPEC:89.1; JSTL:SPEC:89.2
   * 
   * @test_Strategy: Validate the toLowerCase function will lower case the
   * provided input string.
   */
  public void functionsToLowerCaseTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsToLowerCaseTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsToLowerCaseNullStringTest
   * 
   * @assertion_ids: JSTL:SPEC:89.2
   * 
   * @test_Strategy: Validate the toLowerCase function returns an empty string
   * if a null string was provided as the input value.
   */
  public void functionsToLowerCaseNullStringTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsToLowerCaseNullStringTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsToUpperCaseTest
   * 
   * @assertion_ids: JSTL:SPEC:90.1; JSTL:SPEC:90.2
   * 
   * @test_Strategy: Validate the toUpperCase function will lower case the
   * provided input string.
   */
  public void functionsToUpperCaseTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsToUpperCaseTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsToUpperCaseNullStringTest
   * 
   * @assertion_ids: JSTL:SPEC:90.3
   * 
   * @test_Strategy: Validate the toUpperCase function returns an empty string
   * if an empty string was provided as the input value.
   */
  public void functionsToUpperCaseNullStringTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsToUpperCaseNullStringTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsTrimTest
   * 
   * @assertion_ids: JSTL:SPEC:91.1; JSTL:SPEC:91.2
   * 
   * @test_Strategy: Validate the behavior of the trim function. Leading and
   * trailing whitespace must be removed from the result.
   */
  public void functionsTrimTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsTrimTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsTrimNullStringTest
   * 
   * @assertion_ids: JSTL:SPEC:91.3
   * 
   * @test_Strategy: Validate an empty string is returned if the input string is
   * empty.
   */
  public void functionsTrimNullStringTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsTrimNullStringTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsJoinTest
   * 
   * @assertion_ids: JSTL:SPEC:81.1; JSTL:SPEC:81.1.1; JSTL:SPEC:81.1.2;
   * JSTL:SPEC:81.2
   * 
   * @test_Strategy: Validate the join(String[], String) function properly joins
   * the elements of the String array using the specified separator.
   */
  public void functionsJoinTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsJoinTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsJoinNullArrayTest
   * 
   * @assertion_ids: JSTL:SPEC:81.2.1
   * 
   * @test_Strategy: Validate an empty string is returned in the case a null
   * value is provided to the array argument.
   */
  public void functionsJoinNullArrayTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsJoinNullArrayTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsJoinNullSeparatorTest
   * 
   * @assertion_ids: JSTL:SPEC:81.1.3; JSTL:SPEC:81.2.2
   * 
   * @test_Strategy: Validate no separator is used if the value provided to the
   * separator argument is null.
   */
  public void functionsJoinNullSeparatorTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsJoinNullSeparatorTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsJoinEmptySeparatorTest
   * 
   * @assertion_ids: JSTL:SPEC:81.2.2
   * 
   * @test_Strategy: Validate no separator is used if the value provided to the
   * separator argument is an empty String.
   */
  public void functionsJoinEmptySeparatorTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsJoinEmptySeparatorTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsReplaceTest
   * 
   * @assertion_ids: JSTL:SPEC:83.1; JSTL:SPEC:83.1.1; JSTL:SPEC:83.1.3;
   * JSTL:SPEC:83.1.5; JSTL:SPEC:83.2
   * 
   * @test_Strategy: Validate the replace function the following: - if the
   * beforeString is found in the input string, all occurances of the
   * beforeString will be replaced with the afterString. - if the beforeString
   * is not found, the inputString is returned.
   */
  public void functionsReplaceTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsReplaceTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED|Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsReplaceNullInputStringTest
   * 
   * @assertion_ids: JSTL:SPEC:83.1.2; JSTL:SPEC:83.2.1
   * 
   * @test_Strategy: Validate an empty String is returned by the replace
   * function if a null inputString is provided.
   */
  public void functionsReplaceNullInputStringTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsReplaceNullInputStringTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsReplaceEmptyInputStringTest
   * 
   * @assertion_ids: JSTL:SPEC:83.2.1
   * 
   * @test_Strategy: Validate an empty String is returned by the replace
   * function if an empty inputString is provided.
   */
  public void functionsReplaceEmptyInputStringTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsReplaceEmptyInputStringTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsReplaceNullBeforeStringTest
   * 
   * @assertion_ids: JSTL:SPEC:83.1.4; JSTL:SPEC:83.2.2
   * 
   * @test_Strategy: Validate the inputString is returned if the beforeString is
   * null.
   */
  public void functionsReplaceNullBeforeStringTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsReplaceNullBeforeStringTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsReplaceEmptyBeforeStringTest
   * 
   * @assertion_ids: JSTL:SPEC:83.2.2
   * 
   * @test_Strategy: Validate the inputString is returned if the beforeString is
   * empty.
   */
  public void functionsReplaceEmptyBeforeStringTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsReplaceEmptyBeforeStringTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsReplaceNullAfterStringTest
   * 
   * @assertion_ids: JSTL:SPEC:83.1.6; JSTL:SPEC:83.2.3
   * 
   * @test_Strategy: Validate all occurrences of beforeString are removed from
   * the inputString if the afterString value is null.
   */
  public void functionsReplaceNullAfterStringTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsReplaceNullAfterStringTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsReplaceEmptyAfterStringTest
   * 
   * @assertion_ids: JSTL:SPEC:83.2.3
   * 
   * @test_Strategy: Validate all occurrences of beforeString are removed from
   * the inputString if the afterString value is an empty string.
   */
  public void functionsReplaceEmptyAfterStringTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsReplaceEmptyAfterStringTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsSplitTest
   * 
   * @assertion_ids: JSTL:SPEC:84.1; JSTL:SPEC:84.1.1; JSTL:SPEC:84.1.3;
   * JSTL:SPEC:84.2.1; JSTL:SPEC:84.2.4
   * 
   * @test_Strategy: Validate the split function returns an array of elements
   * from an input string with a designated delimiter. The delimiter will not
   * appear in any of the tokens returned.
   */
  public void functionsSplitTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsSplitTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsSplitNullInputStringTest
   * 
   * @assertion_ids: JSTL:SPEC:84.1.2; JSTL:SPEC:84.2.2
   * 
   * @test_Strategy: Validate an array consisting of a single element with an
   * empty string as that element's value is returned if the inputString is
   * null.
   */
  public void functionsSplitNullInputStringTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsSplitNullInputStringTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsSplitEmptyInputStringTest
   * 
   * @assertion_ids: JSTL:SPEC:84.2.2
   * 
   * @test_Strategy: Validate an array consisting of a single element with an
   * empty string as that element's value is returned if the inputString is
   * empty.
   */
  public void functionsSplitEmptyInputStringTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsSplitEmptyInputStringTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsSplitNullDelimiterTest
   * 
   * @assertion_ids: JSTL:SPEC:84.1.4; JSTL:SPEC:84.2.3
   * 
   * @test_Strategy: Validate an array consisting of a single element with the
   * inputString as that element's value is returned if the delimiter specified
   * is null.
   */
  public void functionsSplitNullDelimiterTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsSplitNullDelimiterTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionsSplitEmptyDelimiterTest
   * 
   * @assertion_ids: JSTL:SPEC:84.2.3
   * 
   * @test_Strategy: Validate an array consisting of a single element with the
   * inputString as that element's value is returned if the delimiter specified
   * is empty.
   */
  public void functionsSplitEmptyDelimiterTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsSplitEmptyDelimiterTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }
}
