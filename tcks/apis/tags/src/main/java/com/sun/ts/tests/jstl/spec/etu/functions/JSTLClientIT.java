/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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


package com.sun.ts.tests.jstl.spec.etu.functions;

import java.io.IOException;

import com.sun.ts.tests.jstl.common.client.AbstractUrlClient;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.jboss.shrinkwrap.api.asset.UrlAsset;

@Tag("jstl")
@Tag("platform")
@Tag("web")
@ExtendWith(ArquillianExtension.class)
public class JSTLClientIT extends AbstractUrlClient {

  public static String packagePath = JSTLClientIT.class.getPackageName().replace(".", "/");


  /** Creates new JSTLClient */
  public JSTLClientIT() {
    setContextRoot("/jstl_etu_func_web");
  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jstl_etu_func_web.war");
    archive.setWebXML(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/jstl_etu_func_web.xml"));

    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsTrimTest.jsp")), "functionsTrimTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsTrimNullStringTest.jsp")), "functionsTrimNullStringTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsToUpperCaseTest.jsp")), "functionsToUpperCaseTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsToUpperCaseNullStringTest.jsp")), "functionsToUpperCaseNullStringTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsToLowerCaseTest.jsp")), "functionsToLowerCaseTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsToLowerCaseNullStringTest.jsp")), "functionsToLowerCaseNullStringTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsSubstringTest.jsp")), "functionsSubstringTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsSubstringNullStringTest.jsp")), "functionsSubstringNullStringTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsSubstringNegativeEndIndexTest.jsp")), "functionsSubstringNegativeEndIndexTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsSubstringNegativeBeginIndexTest.jsp")), "functionsSubstringNegativeBeginIndexTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsSubstringEndIndexLTBeginIndexTest.jsp")), "functionsSubstringEndIndexLTBeginIndexTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsSubstringEndIndexGTStringLengthTest.jsp")), "functionsSubstringEndIndexGTStringLengthTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsSubstringBeginIndexGTLastIndexTest.jsp")), "functionsSubstringBeginIndexGTLastIndexTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsSubstringBeforeTest.jsp")), "functionsSubstringBeforeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsSubstringBeforeNullSubstringTest.jsp")), "functionsSubstringBeforeNullSubstringTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsSubstringBeforeNullStringTest.jsp")), "functionsSubstringBeforeNullStringTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsSubstringBeforeEmptySubstringTest.jsp")), "functionsSubstringBeforeEmptySubstringTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsSubstringAfterTest.jsp")), "functionsSubstringAfterTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsSubstringAfterNullSubstringTest.jsp")), "functionsSubstringAfterNullSubstringTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsSubstringAfterNullStringTest.jsp")), "functionsSubstringAfterNullStringTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsSubstringAfterEmptySubstringTest.jsp")), "functionsSubstringAfterEmptySubstringTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsStartsWithTest.jsp")), "functionsStartsWithTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsStartsWithPrefixStringEqualTest.jsp")), "functionsStartsWithPrefixStringEqualTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsStartsWithNullStringTest.jsp")), "functionsStartsWithNullStringTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsStartsWithNullPrefixTest.jsp")), "functionsStartsWithNullPrefixTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsStartsWithEmptyPrefixStringTest.jsp")), "functionsStartsWithEmptyPrefixStringTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsSplitTest.jsp")), "functionsSplitTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsSplitNullInputStringTest.jsp")), "functionsSplitNullInputStringTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsSplitNullDelimiterTest.jsp")), "functionsSplitNullDelimiterTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsSplitEmptyInputStringTest.jsp")), "functionsSplitEmptyInputStringTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsSplitEmptyDelimiterTest.jsp")), "functionsSplitEmptyDelimiterTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsReplaceTest.jsp")), "functionsReplaceTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsReplaceNullInputStringTest.jsp")), "functionsReplaceNullInputStringTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsReplaceNullBeforeStringTest.jsp")), "functionsReplaceNullBeforeStringTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsReplaceNullAfterStringTest.jsp")), "functionsReplaceNullAfterStringTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsReplaceEmptyInputStringTest.jsp")), "functionsReplaceEmptyInputStringTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsReplaceEmptyBeforeStringTest.jsp")), "functionsReplaceEmptyBeforeStringTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsReplaceEmptyAfterStringTest.jsp")), "functionsReplaceEmptyAfterStringTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsLengthTest.jsp")), "functionsLengthTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsLengthNullInputTest.jsp")), "functionsLengthNullInputTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsLengthEmptyStringInputTest.jsp")), "functionsLengthEmptyStringInputTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsJoinTest.jsp")), "functionsJoinTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsJoinNullSeparatorTest.jsp")), "functionsJoinNullSeparatorTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsJoinNullArrayTest.jsp")), "functionsJoinNullArrayTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsJoinEmptySeparatorTest.jsp")), "functionsJoinEmptySeparatorTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsIndexOfTest.jsp")), "functionsIndexOfTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsIndexOfNullSubstringTest.jsp")), "functionsIndexOfNullSubstringTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsIndexOfNullEmptyStringTest.jsp")), "functionsIndexOfNullEmptyStringTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsIndexOfEmptySubstringTest.jsp")), "functionsIndexOfEmptySubstringTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsEscapeXmlTest.jsp")), "functionsEscapeXmlTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsEscapeXmlNullStringTest.jsp")), "functionsEscapeXmlNullStringTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsEndsWithTest.jsp")), "functionsEndsWithTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsEndsWithSuffixStringEqualTest.jsp")), "functionsEndsWithSuffixStringEqualTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsEndsWithNullSuffixTest.jsp")), "functionsEndsWithNullSuffixTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsEndsWithNullStringTest.jsp")), "functionsEndsWithNullStringTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsEndsWithEmptySuffixStringTest.jsp")), "functionsEndsWithEmptySuffixStringTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsContainsTest.jsp")), "functionsContainsTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsContainsStringSubstringEqualTest.jsp")), "functionsContainsStringSubstringEqualTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsContainsNullSubstringTest.jsp")), "functionsContainsNullSubstringTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsContainsNullStringTest.jsp")), "functionsContainsNullStringTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsContainsIgnoreCaseTest.jsp")), "functionsContainsIgnoreCaseTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsContainsIgnoreCaseStringSubstringEqualTest.jsp")), "functionsContainsIgnoreCaseStringSubstringEqualTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsContainsIgnoreCaseNullSubstringTest.jsp")), "functionsContainsIgnoreCaseNullSubstringTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/functionsContainsIgnoreCaseNullStringTest.jsp")), "functionsContainsIgnoreCaseNullStringTest.jsp");

    archive.addAsLibrary(getCommonJarArchive());

    return archive;
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
  @Test
  public void functionsContainsTest() throws Exception {
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
  @Test
  public void functionsContainsStringSubstringEqualTest() throws Exception {
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
  @Test
  public void functionsContainsNullStringTest() throws Exception {
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
  @Test
  public void functionsContainsNullSubstringTest() throws Exception {
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
  @Test
  public void functionsContainsIgnoreCaseTest() throws Exception {
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
  @Test
  public void functionsContainsIgnoreCaseStringSubstringEqualTest()
      throws Exception {
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
  @Test
  public void functionsContainsIgnoreCaseNullStringTest() throws Exception {
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
  @Test
  public void functionsContainsIgnoreCaseNullSubstringTest() throws Exception {
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
  @Test
  public void functionsEscapeXmlTest() throws Exception {
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
  @Test
  public void functionsEscapeXmlNullStringTest() throws Exception {
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
  @Test
  public void functionsIndexOfTest() throws Exception {
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
  @Test
  public void functionsIndexOfNullEmptyStringTest() throws Exception {
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
  @Test
  public void functionsIndexOfNullSubstringTest() throws Exception {
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
  @Test
  public void functionsIndexOfEmptySubstringTest() throws Exception {
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
  @Test
  public void functionsLengthTest() throws Exception {
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
  @Test
  public void functionsLengthNullInputTest() throws Exception {
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
  @Test
  public void functionsLengthEmptyStringTest() throws Exception {
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
  @Test
  public void functionsStartsWithTest() throws Exception {
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
  @Test
  public void functionsStartsWithNullPrefixTest() throws Exception {
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
  @Test
  public void functionsStartsWithNullStringTest() throws Exception {
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
  @Test
  public void functionsStartsWithEmptyPrefixTest() throws Exception {
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
  @Test
  public void functionsStartsWithPrefixStringEqualTest() throws Exception {
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
  @Test
  public void functionsEndsWithTest() throws Exception {
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
  @Test
  public void functionsEndsWithNullSuffixTest() throws Exception {
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
  @Test
  public void functionsEndsWithNullStringTest() throws Exception {
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
  @Test
  public void functionsEndsWithEmptySuffixTest() throws Exception {
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
  @Test
  public void functionsEndsWithSuffixStringEqualTest() throws Exception {
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
  @Test
  public void functionsSubstringTest() throws Exception {
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
  @Test
  public void functionsSubstringNullStringTest() throws Exception {
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
  @Test
  public void functionsSubstringNegativeBeginIndexTest() throws Exception {
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
  @Test
  public void functionsSubstringNegativeEndIndexTest() throws Exception {
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
  @Test
  public void functionsSubstringEndIndexGTStringLengthTest() throws Exception {
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
  @Test
  public void functionsSubstringBeginIndexGTLastIndexTest() throws Exception {
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
  @Test
  public void functionsSubstringEndIndexLTBeginIndexTest() throws Exception {
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
  @Test
  public void functionsSubstringAfterTest() throws Exception {
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
  @Test
  public void functionsSubstringAfterNullStringTest() throws Exception {
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
  @Test
  public void functionsSubstringAfterNullSubstringTest() throws Exception {
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
  @Test
  public void functionsSubstringAfterEmptySubstringTest() throws Exception {
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
  @Test
  public void functionsSubstringBeforeTest() throws Exception {
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
  @Test
  public void functionsSubstringBeforeNullStringTest() throws Exception {
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
  @Test
  public void functionsSubstringBeforeNullSubstringTest() throws Exception {
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
  @Test
  public void functionsSubstringBeforeEmptySubstringTest() throws Exception {
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
  @Test
  public void functionsToLowerCaseTest() throws Exception {
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
  @Test
  public void functionsToLowerCaseNullStringTest() throws Exception {
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
  @Test
  public void functionsToUpperCaseTest() throws Exception {
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
  @Test
  public void functionsToUpperCaseNullStringTest() throws Exception {
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
  @Test
  public void functionsTrimTest() throws Exception {
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
  @Test
  public void functionsTrimNullStringTest() throws Exception {
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
  @Test
  public void functionsJoinTest() throws Exception {
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
  @Test
  public void functionsJoinNullArrayTest() throws Exception {
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
  @Test
  public void functionsJoinNullSeparatorTest() throws Exception {
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
  @Test
  public void functionsJoinEmptySeparatorTest() throws Exception {
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
  @Test
  public void functionsReplaceTest() throws Exception {
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
  @Test
  public void functionsReplaceNullInputStringTest() throws Exception {
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
  @Test
  public void functionsReplaceEmptyInputStringTest() throws Exception {
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
  @Test
  public void functionsReplaceNullBeforeStringTest() throws Exception {
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
  @Test
  public void functionsReplaceEmptyBeforeStringTest() throws Exception {
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
  @Test
  public void functionsReplaceNullAfterStringTest() throws Exception {
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
  @Test
  public void functionsReplaceEmptyAfterStringTest() throws Exception {
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
  @Test
  public void functionsSplitTest() throws Exception {
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
  @Test
  public void functionsSplitNullInputStringTest() throws Exception {
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
  @Test
  public void functionsSplitEmptyInputStringTest() throws Exception {
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
  @Test
  public void functionsSplitNullDelimiterTest() throws Exception {
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
  @Test
  public void functionsSplitEmptyDelimiterTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_func_web//functionsSplitEmptyDelimiterTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }
}
