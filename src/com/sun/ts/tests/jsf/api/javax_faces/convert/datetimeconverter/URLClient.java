/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsf.api.javax_faces.convert.datetimeconverter;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

public final class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_convert_datetime_web";

  public static void main(String[] args) {
    URLClient theTests = new URLClient();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  public Status run(String args[], PrintWriter out, PrintWriter err) {
    setContextRoot(CONTEXT_ROOT);
    setServletName(DEFAULT_SERVLET_NAME);
    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Test Declarations */

  /**
   * @testName: dateTimeConverterGetSetDateStyleTest
   * @assertion_ids: JSF:JAVADOC:1588; JSF:JAVADOC:1595; JSF:JAVADOC:1606
   * @test_Strategy: Ensure the default value of 'default' is returned if the
   *                 value is not explicitly set using setDateStyle(). Then
   *                 verify the value passed to setDateStyle() is returned when
   *                 getDateStyle() is called.
   */
  public void dateTimeConverterGetSetDateStyleTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "dateTimeConverterGetSetDateStyleTest");
    invoke();
  }

  /**
   * @testName: dateTimeConverterSetGetLocaleTest
   * @assertion_ids: JSF:JAVADOC:1588; JSF:JAVADOC:1596; JSF:JAVADOC:1607
   * @test_Strategy: Ensure the locale of the FacesContext is returned if not
   *                 explicitly set via setLocale(). Also verify that the Locale
   *                 set by setLocale() is returned when getLocale() is called.
   */
  public void dateTimeConverterSetGetLocaleTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "dateTimeConverterSetGetLocaleTest");
    invoke();
  }

  /**
   * @testName: dateTimeConverterSetGetTimeZoneTest
   * @assertion_ids: JSF:JAVADOC:1588; JSF:JAVADOC:1599; JSF:JAVADOC:1610
   * @test_Strategy: Ensure the default TimeZone is returned if the TimeZone was
   *                 not explicitly set via setTimeZone. Also verify that the
   *                 TimeZone set by setTimeZone() is returned when
   *                 getTimeZone() is called.
   */
  public void dateTimeConverterSetGetTimeZoneTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "dateTimeConverterSetGetTimeZoneTest");
    invoke();
  }

  /**
   * @testName: dateTimeConverterSetGetPatternTest
   * @assertion_ids: JSF:JAVADOC:1588; JSF:JAVADOC:1597; JSF:JAVADOC:1608
   * @test_Strategy: Ensure the pattern set via setPattern() is returned when
   *                 getPattern() is called.
   */
  public void dateTimeConverterSetGetPatternTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "dateTimeConverterSetGetPatternTest");
    invoke();
  }

  /**
   * @testName: dateTimeConverterSetGetTimeStyleTest
   * @assertion_ids: JSF:JAVADOC:1588; JSF:JAVADOC:1598; JSF:JAVADOC:1609
   * @test_Strategy: Ensure the default value of 'default' is returned if the
   *                 value is not explicitly set using setTimeStyle(). Then
   *                 verify the value passed to setTimeStyle() is returned when
   *                 getTimeStyle() is called.
   */
  public void dateTimeConverterSetGetTimeStyleTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "dateTimeConverterSetGetTimeStyleTest");
    invoke();
  }

  /**
   * @testName: dateTimeConverterSetGetTypeTest
   * @assertion_ids: JSF:JAVADOC:1588; JSF:JAVADOC:1600; JSF:JAVADOC:1612
   * @test_Strategy: Ensure the default value of 'date' is returned if the value
   *                 is not explicitly set using setType(). Then verify the
   *                 value passed to setType() is returned when getType() is
   *                 called.
   */
  public void dateTimeConverterSetGetTypeTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "dateTimeConverterSetGetTypeTest");
    invoke();
  }

  /**
   * @testName: dateTimeConverterIsSetTransientTest
   * @assertion_ids: JSF:JAVADOC:1588; JSF:JAVADOC:1602; JSF:JAVADOC:1611
   * @test_Strategy: Ensure the default value of isTransient() is indeed false.
   *                 Then call setTransient() and validate isTransient() returns
   *                 true.
   */
  public void dateTimeConverterIsSetTransientTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "dateTimeConverterIsSetTransientTest");
    invoke();
  }

  /**
   * @testName: dateTimeConverterGetAsObjectDateStyleTest
   * @assertion_ids: JSF:JAVADOC:1588; JSF:JAVADOC:1589
   * @test_Strategy: Ensure getAsObject() behaves as expected when calling
   *                 getAsObject when date style is not explicitly set and when
   *                 setting all of the valid values for date style. This also
   *                 verifies the locale and timezones are used.
   */
  public void dateTimeConverterGetAsObjectDateStyleTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "dateTimeConverterGetAsObjectDateStyleTest");
    invoke();
  }

  /**
   * @testName: dateTimeConverterGetAsStringDateStyleTest
   * @assertion_ids: JSF:JAVADOC:1588; JSF:JAVADOC:1592
   * @test_Strategy: Ensure getAsString() behaves as expected when calling
   *                 getAsObject when date style is not explicitly set and when
   *                 setting all of the valid values for date style. This also
   *                 verifies the locale and timezones are used.
   */
  public void dateTimeConverterGetAsStringDateStyleTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "dateTimeConverterGetAsStringDateStyleTest");
    invoke();
  }

  /**
   * @testName: dateTimeConverterGetAsObjectTimeStyleTest
   * @assertion_ids: JSF:JAVADOC:1589; JSF:JAVADOC:1598
   * @test_Strategy: Ensure getAsObject() behaves as expected when calling
   *                 getAsObject when time style is not explicitly set and when
   *                 setting all of the valid values for time style. This also
   *                 verifies the locale and timezones are used.
   */
  public void dateTimeConverterGetAsObjectTimeStyleTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "dateTimeConverterGetAsObjectTimeStyleTest");
    invoke();
  }

  /**
   * @testName: dateTimeConverterGetAsStringTimeStyleTest
   * @assertion_ids: JSF:JAVADOC:1598; JSF:JAVADOC:1592
   * @test_Strategy: Ensure getAsString() behaves as expected when calling
   *                 getAsObject when time style is not explicitly set and when
   *                 setting all of the valid values for time style. This also
   *                 verifies the locale and timezones are used.
   */
  public void dateTimeConverterGetAsStringTimeStyleTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "dateTimeConverterGetAsStringTimeStyleTest");
    invoke();
  }

  /**
   * @testName: dateTimeConverterGetAsObjectBothStyleTest
   * @assertion_ids: JSF:JAVADOC:1589; JSF:JAVADOC:1598
   * @test_Strategy: Ensure getAsObject() behaves as expected when calling
   *                 getAsObject when both date and time styles are not
   *                 explicitly and type is set to both. Then cycle through all
   *                 of the styles for both time and date to ensure the possible
   *                 valid values work in combination. This also verifies the
   *                 locale and timezones are used.
   */
  public void dateTimeConverterGetAsObjectBothStyleTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "dateTimeConverterGetAsObjectBothStyleTest");
    invoke();
  }

  /**
   * @testName: dateTimeConverterGetAsStringBothStyleTest
   * @assertion_ids: JSF:JAVADOC:1592; JSF:JAVADOC:1598
   * @test_Strategy: Ensure getAsString() behaves as expected when calling
   *                 getAsObject when both date and time styles are not
   *                 explicitly and type is set to both. Then cycle through all
   *                 of the styles for both time and date to ensure the possible
   *                 valid values work in combination. This also verifies the
   *                 locale and timezones are used.
   */
  public void dateTimeConverterGetAsStringBothStyleTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "dateTimeConverterGetAsStringBothStyleTest");
    invoke();
  }

  /**
   * @testName: dateTimeConverterGetAsObjectPatternTest
   * @assertion_ids: JSF:JAVADOC:1589; JSF:JAVADOC:1597
   * @test_Strategy: Ensure getAsObject() behaves as expected when a pattern is
   *                 specified and that values for date style, time style, and
   *                 type are ignored when specified. This also verifies the
   *                 locale and timezones are used.
   */
  public void dateTimeConverterGetAsObjectPatternTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "dateTimeConverterGetAsObjectPatternTest");
    invoke();
  }

  /**
   * @testName: dateTimeConverterGetAsStringPatternTest
   * @assertion_ids: JSF:JAVADOC:1590; JSF:JAVADOC:1597
   * @test_Strategy: Ensure getAsString() behaves as expected when a pattern is
   *                 specified and that values for date style, time style, and
   *                 type are ignored when specified. This also verifies the
   *                 locale and timezones are used.
   */
  public void dateTimeConverterGetAsStringPatternTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "dateTimeConverterGetAsStringPatternTest");
    invoke();
  }

  /**
   * @testName: dateTimeConverterInvalidDateStyleTest
   * @assertion_ids: JSF:JAVADOC:1590; JSF:JAVADOC:1593
   * @test_Strategy: Ensure ConverterException is thrown when getAsObject() or
   *                 getAsString() is called with an invalid date style.
   */
  public void dateTimeConverterInvalidDateStyleTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "dateTimeConverterInvalidDateStyleTest");
    invoke();
  }

  /**
   * @testName: dateTimeConverterInvalidPatternTest
   * @assertion_ids: JSF:JAVADOC:1590; JSF:JAVADOC:1593
   * @test_Strategy: Ensure ConverterException is thrown when getAsObject() or
   *                 getAsString() is called with an invalid pattern.
   */
  public void dateTimeConverterInvalidPatternTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "dateTimeConverterInvalidPatternTest");
    invoke();
  }

  /**
   * @testName: dateTimeConverterInvalidTimeStyleTest
   * @assertion_ids: JSF:JAVADOC:1590; JSF:JAVADOC:1593
   * @test_Strategy: Ensure ConverterException is thrown when getAsObject() or
   *                 getAsString() is called with an invalid time style.
   */
  public void dateTimeConverterInvalidTimeStyleTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "dateTimeConverterInvalidTimeStyleTest");
    invoke();
  }

  /**
   * @testName: dateTimeConverterInvalidTypeTest
   * @assertion_ids: JSF:JAVADOC:1590; JSF:JAVADOC:1593
   * @test_Strategy: Ensure ConverterException is thrown when getAsObject() or
   *                 getAsString() is called with an invalid type.
   */
  public void dateTimeConverterInvalidTypeTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "dateTimeConverterInvalidTypeTest");
    invoke();
  }

  /**
   * @testName: dateTimeConverterGetAsStringNPETest
   * @assertion_ids: JSF:JAVADOC:1593; JSF:JAVADOC:1594
   * @test_Strategy: Ensure DateTimeConverter.getAsString() throws an NPE if
   *                 either the <code>context<code> or <code>component<code>
   *                 arguments are null.
   * @since 1.2
   */
  public void dateTimeConverterGetAsStringNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "dateTimeConverterGetAsStringNPETest");
    invoke();
  }

  /**
   * @testName: dateTimeConverterGetAsObjectNPETest
   * @assertion_ids: JSF:JAVADOC:1591
   * @test_Strategy: Ensure DateTimeConverter.getAsString() throws an NPE if
   *                 either the <code>context<code> or <code>component<code>
   *                 arguments are null.
   * @since 1.2
   */
  public void dateTimeConverterGetAsObjectNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "dateTimeConverterGetAsObjectNPETest");
    invoke();
  }

} // end of URLClient
