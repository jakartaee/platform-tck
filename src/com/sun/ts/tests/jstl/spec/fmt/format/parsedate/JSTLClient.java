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
 * @(#)JSTLClient.java	1.1 04/06/02
 */

package com.sun.ts.tests.jstl.spec.fmt.format.parsedate;

import java.util.Locale;
import java.util.GregorianCalendar;
import java.util.Date;
import java.util.TimeZone;
import java.text.DateFormat;
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

    setContextRoot("/jstl_fmt_psdate_web");
    setGoldenFileDir("/jstl/spec/fmt/format/parsedate");

    return super.run(args, out, err);
  }

  /*
   * @testName: positivePDValueTest
   * 
   * @assertion_ids: JSTL:SPEC:58; JSTL:SPEC:58.1; JSTL:SPEC:58.1.1
   * 
   * @testStrategy: Validate that parseDate action can properly parse dates
   * provided as dynamic or static String values. Pass the parsed value to
   * formatDate to display (due to possible timezone difference).
   */
  public void positivePDValueTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positivePDValueTest");
    invoke();
  }

  /*
   * @testName: positivePDTypeTest
   * 
   * @assertion_ids: JSTL:SPEC:58.2; JSTL:SPEC:58.2.1; JSTL:SPEC:58.2.2;
   * JSTL:SPEC:58.2.3; JSTL:SPEC:58.2.4; JSTL:SPEC:58.15
   * 
   * @testStrategy: Validate the following: - If type is not specified, the
   * action properly parses a String with the only the date component. - The
   * value is properly parsed when explicity using time, date, or both. Pass the
   * parsed value to formatDate to display (due to possible timezone
   * difference).
   */
  public void positivePDTypeTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positivePDTypeTest");
    invoke();
  }

  /*
   * @testName: positivePDDateStyleTest
   * 
   * @assertion_ids: JSTL:SPEC:58.3; JSTL:SPEC:58.3.1; JSTL:SPEC:58.3.2;
   * JSTL:SPEC:58.3.3; JSTL:SPEC:58.3.4; JSTL:SPEC:58.3.5; JSTL:SPEC:58.3.6;
   * JSTL:SPEC:58.3.7; JSTL:SPEC:58.16
   * 
   * @testStrategy: Validate the following: - Dates are parsed correctly for all
   * values of the dateStyle attribute. - If dateStyle is present it will only
   * be applied when type is not specified, or is set to date or both. - If
   * dateStyle is not present, the action will assume the date to be parsed is
   * in the default style.
   */
  public void positivePDDateStyleTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "positivePDDateStyleTest");
    TEST_PROPS.setProperty(REQUEST, "positivePDDateStyleTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    invoke();
  }

  /*
   * @testName: positivePDTimeStyleTest
   * 
   * @assertion_ids: JSTL:SPEC:58.4; JSTL:SPEC:58.4.1; JSTL:SPEC:58.4.2;
   * JSTL:SPEC:58.4.3; JSTL:SPEC:58.4.4; JSTL:SPEC:58.4.5; JSTL:SPEC:58.4.6;
   * JSTL:SPEC:58.17
   * 
   * @testStrategy: Validate the following: - Times are parsed correctly for all
   * values of the timeStyle attribute. - If timeStyle is present is will only
   * be applied when type is set to time or both. - If timeStyle is not present,
   * the action will assume the time to be parsed is in the default style.
   */
  public void positivePDTimeStyleTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "positivePDTimeStyleTest");
    TEST_PROPS.setProperty(REQUEST, "positivePDTimeStyleTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    invoke();
  }

  /*
   * @testName: positivePDParseLocaleTest
   * 
   * @assertion_ids: JSTL:SPEC:58.7; JSTL:SPEC:58.7.1
   * 
   * @testStrategy: Validate the action will properly parse a date using the
   * locale provided via the parseLocale attribute. This will be confirmed by
   * setting the locale of the page to de_DE, and pass en_US to the action
   * itself. No parsing exceptions should occur.
   */
  public void positivePDParseLocaleTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "positivePDParseLocaleTest");
    TEST_PROPS.setProperty(REQUEST, "positivePDParseLocaleTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    invoke();
  }

  /*
   * @testName: positivePDPatternTest
   * 
   * @assertion_ids: JSTL:SPEC:58.5; JSTL:SPEC:58.5.1
   * 
   * @testStragegy: Validate that if pattern is present, it is properly applied
   * when parsing the date value provided. Pass the parsed value to formatDate
   * to display (due to possible timezone difference).
   */
  public void positivePDPatternTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positivePDPatternTest");
    invoke();
  }

  /*
   * @testName: positivePDVarTest
   * 
   * @assertion_ids: JSTL:SPEC:58.8; JSTL:SPEC:58.8.1; JSTL:SPEC:58.14
   * 
   * @testStrategy: Validate that the if var is specified and scope is not, the
   * parsed value is not written to the current JspWriter, but is instead
   * available via the PageContext and is of type java.util.Date (validation
   * performed via custom action).
   */
  public void positivePDVarTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positivePDVarTest");
    invoke();
  }

  /*
   * @testName: positivePDScopeTest
   * 
   * @assertion_ids: JSTL:SPEC:58.9; JSTL:SPEC:58.9.1; JSTL:SPEC:58.9.2;
   * JSTL:SPEC:58.9.3; JSTL:SPEC:58.9.4; JSTL:SPEC:58.14
   * 
   * @testStrategy: Validate that var is properly exported to the scopes as
   * specified by the scope attribute. Also verify that if scope is not present,
   * that var is exported to the page scope by default.
   */
  public void positivePDScopeTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positivePDScopeTest");
    invoke();
  }

  /*
   * @testName: positivePDValueNullEmptyTest
   * 
   * @assertion_ids: JSTL:SPEC:58.11
   * 
   * @testStrategy: Validate that if value is null or empty, that the scoped
   * variable referenced by var is indeed removed. This will be validated by
   * setting a scoped variable before calling the action with a null or empty
   * value, and then verifying that the var is no longer available. Pass the
   * parsed value to formatDate to display (due to possible timezone
   * difference).
   */
  public void positivePDValueNullEmptyTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positivePDValueNullEmptyTest");
    invoke();
  }

  /*
   * @testName: positivePDBodyValueTest
   * 
   * @assertion_ids: JSTL:SPEC:58.12
   * 
   * @testStrategy: Validate the action can properly parse a date string
   * provided as body content to the action.
   */
  public void positivePDBodyValueTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positivePDBodyValueTest");
    invoke();
  }

  /*
   * @testName: positivePDParseLocaleNullEmptyTest
   * 
   * @assertion_ids: JSTL:SPEC:58.26
   * 
   * @testStrategy: Validate that if the parseLocale attribute is null or empty,
   * the action behaves as of the attribute was not specified. Since setLocale
   * was used, the parse action should use the locale from the locale
   * configuration variable. Pass the parsed value to formatDate to display (due
   * to possible timezone difference).
   */
  public void positivePDParseLocaleNullEmptyTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positivePDParseLocaleNullEmptyTest");
    invoke();
  }

  /*
   * @testName: positivePDLocalizationContextTest
   * 
   * @assertion_ids: JSTL:SPEC:46.2
   * 
   * @testStrategy: Validate that the action can properly format a date based on
   * the default I18N localization context configuration variable
   * javax.servlet.jsp.jstl.fmt.localizationContext. Pass the parsed value to
   * formatDate to display (due to possible timezone difference).
   */
  public void positivePDLocalizationContextTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "positivePDLocalizationContextTest");
    TEST_PROPS.setProperty(REQUEST, "positivePDLocalizationContextTest.jsp");
    TEST_PROPS.setProperty(GOLDENFILE, "positivePDLocalizationContextTest.gf");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en-US");
    invoke();
  }

  /*
   * @testName: positivePDFallbackLocaleTest
   * 
   * @assertion_ids: JSTL:SPEC:50.2
   * 
   * @testStrategy: Validate that if no matching locale can be found, that the
   * fallback locale will be used. Pass the parsed value to formatDate to
   * display (due to possible timezone difference).
   */
  public void positivePDFallbackLocaleTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "positivePDFallbackLocaleTest");
    TEST_PROPS.setProperty(REQUEST, "positivePDFallbackLocaleTest.jsp");
    TEST_PROPS.setProperty(GOLDENFILE, "positivePDFallbackLocaleTest.gf");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: xx-XX");
    invoke();
  }

  /*
   * @testName: negativePDUnableToParseValueTest
   * 
   * @assertion_ids: JSTL:SPEC:58.23; JSTL:SPEC:58.23.1
   * 
   * @testStrategy: Validate that if the formatting action is unable to parse
   * the String value provided, the exception is rethrown as a JspException,
   * with the rethrown unparsable value in the exception text and the original
   * exception set as the root cause of the JspException.
   */
  public void negativePDUnableToParseValueTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "negativePDUnableToParseValueTest");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    invoke();
  }

  /*
   * @testName: positivePDTimeZoneTest
   * 
   * @assertion_ids: JSTL:SPEC:58.6; JSTL:SPEC:58.6.1
   * 
   * @testStrategy: Validate that the timeZone attribute is able to accept both
   * String literals representing a time zone, as well as java.util.TimeZone
   * objects. Pass the parsed value to formatDate to display (due to possible
   * timezone difference).
   */
  public void positivePDTimeZoneTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positivePDTimeZoneTest");
    invoke();
  }

  /*
   * @testName: positivePDTimeZoneNullEmptyTest
   * 
   * @assertion_ids: JSTL:SPEC:58.25
   * 
   * @testStrategy: Validate that if timeZone is null or empty, the value will
   * be formatted as if it was missing. In this case, the time will be formatted
   * using the page's time zone of EST. Pass the parsed value to formatDate to
   * display (due to possible timezone difference).
   */
  public void positivePDTimeZoneNullEmptyTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positivePDTimeZoneNullEmptyTest");
    invoke();
  }

  /*
   * @testName: positivePDTimeZonePrecedenceTest
   * 
   * @assertion_ids: JSTL:SPEC:58.19; JSTL:SPEC:58.19; JSTL:SPEC:58.21
   * 
   * @testStrategy: Validate that following order for determining the time zone
   * to be used when formatting date/times. In order of precedence: - If
   * present, use the value of the timeZone attribute. - If wrapped in a
   * <fmt:setTimeZone> action, use the timeZone of that action. - If no timeZone
   * attribute present, and not wrapped by the fmt:setTimeZone action, use the
   * value of the scoped attribute javax.servlet.jsp.jstl.fmt.timeZone Pass the
   * parsed value to formatDate to display (due to possible timezone
   * difference).
   */
  public void positivePDTimeZonePrecedenceTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positivePDTimeZonePrecedenceTest");
    invoke();
  }

  /*
   * @testName: negativePDScopeNoVarTest
   * 
   * @assertion_ids: JSTL:SPEC:58.10
   * 
   * @testStrategy: validate that if var is not specified, but scope is, that a
   * fatal translation error occurs.
   */
  public void negativePDScopeNoVarTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "negativePDScopeNoVarTest");
    TEST_PROPS.setProperty(REQUEST, "negativePDScopeNoVarTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }
}
