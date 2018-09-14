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
 * $URL$ $LastChangedDate$
 */

package com.sun.ts.tests.jstl.spec.fmt.format.parsenum;

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
   * public methods
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

    setContextRoot("/jstl_fmt_psnum_web");
    setGoldenFileDir("/jstl/spec/fmt/format/parsenum");

    return super.run(args, out, err);
  }

  /*
   * @testName: positivePNValueTest
   * 
   * @assertion_ids: JSTL:SPEC:56; JSTL:SPEC:56.1; JSTL:SPEC:56.1.1
   * 
   * @testStrategy: Validate the action can correctly parse dynamic and static
   * values provided to the value attribute.
   */
  public void positivePNValueTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positivePNValueTest");
    invoke();
  }

  /*
   * @testName: positivePNTypeTest
   * 
   * @assertion_ids: JSTL:SPEC:56.2; JSTL:SPEC:56.2.1; JSTL:SPEC:56.2.2;
   * JSTL:SPEC:56.2.3; JSTL:SPEC:56.2.4; JSTL:SPEC:56.2.6
   * 
   * @testStrategy: Validate the action can properly parse numbers provided in
   * the three supported types (number, currency, percent). Also verify that if
   * type is not provided, that the value is parsed as a number.
   */
  public void positivePNTypeTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positivePNTypeTest");
    invoke();
  }

  /*
   * @testName: positivePNPatternTest
   * 
   * @assertion_ids: JSTL:SPEC:56.3; JSTL:SPEC:56.3.1; JSTL:SPEC:56.20
   * 
   * @testStrategy: Validate that if a pattern is specified that it will be
   * applied to the parsed value no matter the value of type.
   */
  public void positivePNPatternTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positivePNPatternTest");
    invoke();
  }

  /*
   * @testName: positivePNParseLocaleTest
   * 
   * @assertion_ids: JSTL:SPEC:56.4; JSTL:SPEC:56.4.1
   * 
   * @testStrategy: Validate that the action is properly able to parse the
   * provided value based of the parseLocale attribute. Also validate that the
   * attribute can accept either Strings or Locale objects. Additionally
   * validate that the presence of the parseLocale attribute will override the
   * locale of the page.
   */
  public void positivePNParseLocaleTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positivePNParseLocaleTest");
    invoke();
  }

  /*
   * @testName: positivePNIntegerOnlyTest
   * 
   * @assertion_ids: JSTL:SPEC:56.5; JSTL:SPEC:56.5.1; JSTL:SPEC:56.5.3
   * 
   * @testStrategy: Validate that if integerOnly is true, only the integer
   * portion of the value is returned parsed. Also validate that if integerOnly
   * is not specified, that the entire value will be returned parsed (default
   * value of false).
   */
  public void positivePNIntegerOnlyTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positivePNIntegerOnlyTest");
    invoke();
  }

  /*
   * @testName: positivePNVarTest
   * 
   * @assertion_ids: JSTL:SPEC:56.6; JSTL:SPEC:56.6.1
   * 
   * @testStrategy: Validate that if var is specifed the parsed result is
   * exported and is of type java.lang.Number.
   */
  public void positivePNVarTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positivePNVarTest");
    invoke();
  }

  /*
   * @testName: positivePNScopeTest
   * 
   * @assertion_ids: JSTL:SPEC:56.7; JSTL:SPEC:56.7.1; JSTL:SPEC:56.7.2;
   * JSTL:SPEC:56.7.3; JSTL:SPEC:56.7.4; JSTL:SPEC:56.10
   * 
   * @testStrategy: Validate that var is exported to the proper scope as defined
   * by the value of the scope attribute. Also verify that scope is not
   * specified, that var is exported to the page scope.
   */
  public void positivePNScopeTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positivePNScopeTest");
    invoke();
  }

  /*
   * @testName: positivePNBodyValueTest
   * 
   * @assertion_ids: JSTL:SPEC:56.11
   * 
   * @testStrategy: Validate that the value to be parsed can be provided as body
   * content to the action.
   */
  public void positivePNBodyValueTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positivePNBodyValueTest");
    invoke();
  }

  /*
   * @testName: positivePNValueNullEmptyTest
   * 
   * @assertion_ids: JSTL:SPEC:56.9
   * 
   * @testStrategy: Validate that if value is null or empty, the variable
   * referenced by var will be removed from the scoped specified in the action.
   * This will be verfied by setting an exported variable to the 4 given scopes,
   * and calling the action specifying these scopes with value null or empty.
   * After the action completes, use the checkScope tag to validate the variable
   * no longer exists.
   */
  public void positivePNValueNullEmptyTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positivePNValueNullEmptyTest");
    invoke();
  }

  /*
   * @testName: positivePNParseLocaleNullEmptyTest
   * 
   * @assertion_ids: JSTL:SPEC:56.13
   * 
   * @testStrategy: Validate that if the parseLocale attribute is null or empty,
   * the action behaves as of the attribute was not specified. Since setLocale
   * was used, the parse action should use the locale from the locale
   * configuration variable.
   */
  public void positivePNParseLocaleNullEmptyTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positivePNParseLocaleNullEmptyTest");
    invoke();
  }

  /*
   * @testName: positivePNLocalizationContextTest
   * 
   * @assertion_ids: JSTL:SPEC:46.2
   * 
   * @testStrategy: Validate that the action can properly format a date based on
   * the default I18N localization context configuration variable
   * javax.servlet.jsp.jstl.fmt.localizationContext.
   */
  public void positivePNLocalizationContextTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "positivePNLocalizationContextTest");
    TEST_PROPS.setProperty(REQUEST, "positivePNLocalizationContextTest.jsp");
    TEST_PROPS.setProperty(GOLDENFILE, "positivePNLocalizationContextTest.gf");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en-US");
    invoke();
  }

  /*
   * @testName: positivePNFallbackLocaleTest
   * 
   * @assertion_ids: JSTL:SPEC:50.2
   * 
   * @testStrategy: Validate that if no matching locale can be found, that the
   * fallback locale will be used.
   */
  public void positivePNFallbackLocaleTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "positivePNFallbackLocaleTest");
    TEST_PROPS.setProperty(REQUEST, "positivePNFallbackLocaleTest.jsp");
    TEST_PROPS.setProperty(GOLDENFILE, "positivePNFallbackLocaleTest.gf");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: xx-XX");
    invoke();
  }

  /*
   * @testName: negativePNUnableToParseValueTest
   * 
   * @assertion_ids: JSTL:SPEC:56.14; JSTL:SPEC:56.14.1
   * 
   * @testStrategy: Validate that if the formatting action is unable to parse
   * the String value provided, the exception is rethrown as a JspException,
   * with the rethrown unparsable value in the exception text and the original
   * exception set as the root cause of the JspException.
   */
  public void negativePNUnableToParseValueTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "negativePNUnableToParseValueTest");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    invoke();
  }

  /*
   * @testName: negativePNScopeNoVarTest
   * 
   * @assertion_ids: JSTL:SPEC:56.8
   * 
   * @testStrategy: validate that if var is not specified, but scope is, that a
   * fatal translation error occurs.
   */
  public void negativePNScopeNoVarTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "negativePNScopeNoVarTest");
    TEST_PROPS.setProperty(REQUEST, "negativePNScopeNoVarTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }
}
