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

package com.sun.ts.tests.jsf.api.javax_faces.convert.numberconverter;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

import java.io.PrintWriter;

public final class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_convert_number_web";

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
   * @testName: numConverterGetSetCurrencyCodeTest
   * @assertion_ids: JSF:JAVADOC:1679; JSF:JAVADOC:1665; JSF:JAVADOC:1682
   * @test_Strategy: Validate behavior of {get,set}CurrencyCode().
   */
  public void numConverterGetSetCurrencyCodeTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "numConverterGetSetCurrencyCodeTest");
    invoke();
  }

  /**
   * @testName: numConverterGetSetCurrencySymbolTest
   * @assertion_ids: JSF:JAVADOC:1679; JSF:JAVADOC:1666; JSF:JAVADOC:1683
   * @test_Strategy: Validate behavior of {get,set}CurrencySymbol().
   */
  public void numConverterGetSetCurrencySymbolTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "numConverterGetSetCurrencySymbolTest");
    invoke();
  }

  /**
   * @testName: numConverterGetSetLocaleTest
   * @assertion_ids: JSF:JAVADOC:1679; JSF:JAVADOC:1667; JSF:JAVADOC:1686
   * @test_Strategy: Ensure that if the Locale is not explicitly set, that the
   *                 Locale in the ViewRoot of the FacesContext is used. Next,
   *                 ensure that the Locale explicitly set on the Converter is
   *                 returned.
   */
  public void numConverterGetSetLocaleTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "numConverterGetSetLocaleTest");
    invoke();
  }

  /**
   * @testName: numConverterGetSetMaxFractionDigitsTest
   * @assertion_ids: JSF:JAVADOC:1679; JSF:JAVADOC:1687; JSF:JAVADOC:1668
   * @test_Strategy: Validate behavior of {get,set}MaxFractionDigits().
   */
  public void numConverterGetSetMaxFractionDigitsTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "numConverterGetSetMaxFractionDigitsTest");
    invoke();
  }

  /**
   * @testName: numConverterGetSetMaxIntegerDigitsTest
   * @assertion_ids: JSF:JAVADOC:1679; JSF:JAVADOC:1669; JSF:JAVADOC:1688
   * @test_Strategy: Validate behavior of {get,set}MaxIntegerDigits().
   */
  public void numConverterGetSetMaxIntegerDigitsTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "numConverterGetSetMaxIntegerDigitsTest");
    invoke();
  }

  /**
   * @testName: numConverterGetSetMinFractionDigitsTest
   * @assertion_ids: JSF:JAVADOC:1679; JSF:JAVADOC:1670; JSF:JAVADOC:1689
   * @test_Strategy: Validate behavior of {get,set}MinFractionDigits().
   */
  public void numConverterGetSetMinFractionDigitsTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "numConverterGetSetMinFractionDigitsTest");
    invoke();
  }

  /**
   * @testName: numConverterGetSetMinIntegerDigitsTest
   * @assertion_ids: JSF:JAVADOC:1679; JSF:JAVADOC:1671; JSF:JAVADOC:1690
   * @test_Strategy: Validate behavior of {get,set}MaxIntegerDigits().
   */
  public void numConverterGetSetMinIntegerDigitsTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "numConverterGetSetMinIntegerDigitsTest");
    invoke();
  }

  /**
   * @testName: numConverterGetSetPatternTest
   * @assertion_ids: JSF:JAVADOC:1679; JSF:JAVADOC:1672; JSF:JAVADOC:1691
   * @test_Strategy: Validate behavior of {get,set}Pattern().
   */
  public void numConverterGetSetPatternTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "numConverterGetSetPatternTest");
    invoke();
  }

  /**
   * @testName: numConverterGetSetTypeTest
   * @assertion_ids: JSF:JAVADOC:1679; JSF:JAVADOC:1673; JSF:JAVADOC:1693
   * @test_Strategy: Validate behavior of {get,set}Type().
   */
  public void numConverterGetSetTypeTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "numConverterGetSetTypeTest");
    invoke();
  }

  /**
   * @testName: numConverterIsSetGroupingUsedTest
   * @assertion_ids: JSF:JAVADOC:1679; JSF:JAVADOC:1675; JSF:JAVADOC:1684
   * @test_Strategy: Confirm the default value is true, then ensure that to
   *                 proper value is returned by isGroupingUsed() when
   *                 setGroupingUsed() has been called.
   */
  public void numConverterIsSetGroupingUsedTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "numConverterIsSetGroupingUsedTest");
    invoke();
  }

  /**
   * @testName: numConverterIsSetIntegerOnlyTest
   * @assertion_ids: JSF:JAVADOC:1679; JSF:JAVADOC:1676; JSF:JAVADOC:1685
   * @test_Strategy: Confirm the default value is false, then ensure that to
   *                 proper value is returned by isIntegerOnly() when
   *                 setIntegerOnly() has been called.
   */
  public void numConverterIsSetIntegerOnlyTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "numConverterIsSetIntegerOnlyTest");
    invoke();
  }

  /**
   * @testName: numConverterIsSetTransientTest
   * @assertion_ids: JSF:JAVADOC:1679; JSF:JAVADOC:1677; JSF:JAVADOC:1692
   * @test_Strategy: Ensure that isTransient() returns the expected values after
   *                 setTransient() has been called.
   */
  public void numConverterIsSetTransientTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "numConverterIsSetTransientTest");
    invoke();
  }

  /**
   * @testName: numConverterGetAsObjectNullZeroLengthTest
   * @assertion_ids: JSF:JAVADOC:1679; JSF:JAVADOC:1659
   * @test_Strategy: Ensure null is returned if the input value is null or a
   *                 zero-length String, null is returned.
   */
  public void numConverterGetAsObjectNullZeroLengthTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "numConverterGetAsObjectNullZeroLengthTest");
    invoke();
  }

  /**
   * @testName: numConverterGetAsObjectLocaleTest
   * @assertion_ids: JSF:JAVADOC:1679; JSF:JAVADOC:1667; JSF:JAVADOC:1659
   * @test_Strategy: Ensure getAsObject() picks up the Locale from the
   *                 appropriate locations and uses it to create its value.
   */
  public void numConverterGetAsObjectLocaleTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "numConverterGetAsObjectLocaleTest");
    invoke();
  }

  /**
   * @testName: numConverterGetAsObjectPatternTest
   * @assertion_ids: JSF:JAVADOC:1659; JSF:JAVADOC:1679; JSF:JAVADOC:1672
   * @test_Strategy: Ensure the specified pattern is used when creating the
   *                 return value of getAsObject().
   */
  public void numConverterGetAsObjectPatternTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "numConverterGetAsObjectPatternTest");
    invoke();
  }

  /**
   * @testName: numConverterGetAsObjectTypeTest
   * @assertion_ids: JSF:JAVADOC:1659; JSF:JAVADOC:1679; JSF:JAVADOC:1673
   * @test_Strategy: Ensure the different specifications of type impact the
   *                 result of getAsObject().
   */
  public void numConverterGetAsObjectTypeTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "numConverterGetAsObjectTypeTest");
    invoke();
  }

  /**
   * @testName: numConverterGetAsObjectParseIntOnlyTest
   * @assertion_ids: JSF:JAVADOC:1659; JSF:JAVADOC:1679; JSF:JAVADOC:1676
   * @test_Strategy: Ensure that getAsObject() only parsed Integer values when
   *                 this is set.
   */
  public void numConverterGetAsObjectParseIntOnlyTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "numConverterGetAsObjectParseIntOnlyTest");
    invoke();
  }

  /**
   * @testName: numConverterGetAsStringNullZeroLengthTest
   * @assertion_ids: JSF:JAVADOC:1679; JSF:JAVADOC:1662
   * @test_Strategy: Ensure a zero-length String is returned by getAsString()
   *                 when the value to be converted is either null or a
   *                 zero-length String.
   */
  public void numConverterGetAsStringNullZeroLengthTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "numConverterGetAsStringNullZeroLengthTest");
    invoke();
  }

  /**
   * @testName: numConverterGetAsStringStringInputTest
   * @assertion_ids: JSF:JAVADOC:1679; JSF:JAVADOC:1662; JSF:JAVADOC:1674
   * @test_Strategy: Ensure getAsString() can properly handle input values of
   *                 type String.
   */
  public void numConverterGetAsStringStringInputTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "numConverterGetAsStringStringInputTest");
    invoke();
  }

  /**
   * @testName: numConverterGetAsStringLocaleTest
   * @assertion_ids: JSF:JAVADOC:1679; JSF:JAVADOC:1662; JSF:JAVADOC:1667
   * @test_Strategy: Ensure getAsString() uses the locale from the approriate
   *                 location when creating the return value.
   */
  public void numConverterGetAsStringLocaleTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "numConverterGetAsStringLocaleTest");
    invoke();
  }

  /**
   * @testName: numConverterGetAsStringPatternTest
   * @assertion_ids: JSF:JAVADOC:1679; JSF:JAVADOC:1662; JSF:JAVADOC:1672
   * @test_Strategy: Ensure a specified pattern is used to create the result if
   *                 getAsString().
   */
  public void numConverterGetAsStringPatternTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "numConverterGetAsStringPatternTest");
    invoke();
  }

  /**
   * @testName: numConverterGetAsStringTypeTest
   * @assertion_ids: JSF:JAVADOC:1679; JSF:JAVADOC:1662; JSF:JAVADOC:1673
   * @test_Strategy: Ensure the specification of different types results in the
   *                 expected result from getAsString().
   */
  public void numConverterGetAsStringTypeTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "numConverterGetAsStringTypeTest");
    invoke();
  }

  /**
   * @testName: numConverterGetAsStringGroupingTest
   * @assertion_ids: JSF:JAVADOC:1679; JSF:JAVADOC:1662; JSF:JAVADOC:1675
   * @test_Strategy: Ensure that the expected result is returned when grouping
   *                 is enabled or disabled.
   */
  public void numConverterGetAsStringGroupingTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "numConverterGetAsStringGroupingTest");
    invoke();
  }

  /**
   * @testName: numConverterGetAsStringMinMaxIntegerTest
   * @assertion_ids: JSF:JAVADOC:1679; JSF:JAVADOC:1662; JSF:JAVADOC:1669;
   *                 JSF:JAVADOC:1671
   * @test_Strategy: Ensure that the expected result is returned when a max or
   *                 min integer is specified.
   */
  public void numConverterGetAsStringMinMaxIntegerTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "numConverterGetAsStringMinMaxIntegerTest");
    invoke();
  }

  /**
   * @testName: numConverterGetAsStringMinMaxFractionTest
   * @assertion_ids: JSF:JAVADOC:1679; JSF:JAVADOC:1662; JSF:JAVADOC:1668;
   *                 JSF:JAVADOC:1670
   * @test_Strategy: Ensure that the expected result is returned when a max or
   *                 min fractional is specified.
   */
  public void numConverterGetAsStringMinMaxFractionTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "numConverterGetAsStringMinMaxFractionTest");
    invoke();
  }

  /**
   * @testName: numConverterGetAsStringCurrencySymbolTest
   * @assertion_ids: JSF:JAVADOC:1679; JSF:JAVADOC:1662; JSF:JAVADOC:1683
   * @test_Strategy: Ensure that the expected result is returned when currency
   *                 symbol is specified.
   */
  public void numConverterGetAsStringCurrencySymbolTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "numConverterGetAsStringCurrencySymbolTest");
    invoke();
  }

  /**
   * @testName: numConverterGetAsStringCurrencyCodeTest
   * @assertion_ids: JSF:JAVADOC:1679; JSF:JAVADOC:1662; JSF:JAVADOC:1682
   * @test_Strategy: Ensure that the expected result is returned when currency
   *                 code is specified taking into account the version of the VM
   *                 in which the test is running in.
   */
  public void numConverterGetAsStringCurrencyCodeTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "numConverterGetAsStringCurrencyCodeTest");
    invoke();
  }

  /**
   * @testName: numConverterGetAsStringCurrencyCodeSymbolTest
   * @assertion_ids: JSF:JAVADOC:1679; JSF:JAVADOC:1662; JSF:JAVADOC:1683;
   *                 JSF:JAVADOC:1682
   * @test_Strategy: Ensure that the expected result is returned when both
   *                 currency code and currency symbol are specified, taking
   *                 into account the version of the VM in which the test is
   *                 running in.
   */
  public void numConverterGetAsStringCurrencyCodeSymbolTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "numConverterGetAsStringCurrencyCodeSymbolTest");
    invoke();
  }

  /**
   * @testName: numConverterGetAsObjectNPETest
   * @assertion_ids: JSF:JAVADOC:1679; JSF:JAVADOC:1662; JSF:JAVADOC:1661
   * @test_Strategy: Ensure an NPE is thrown if either context or component
   *                 arguments are null.
   */
  public void numConverterGetAsObjectNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "numConverterGetAsObjectNPETest");
    invoke();
  }

  /**
   * @testName: numConverterGetAsStringNPETest
   * @assertion_ids: JSF:JAVADOC:1679; JSF:JAVADOC:1662; JSF:JAVADOC:1664
   * @test_Strategy: Ensure an NPE is thrown if either context or component
   *                 arguments are null.
   */
  public void numConverterGetAsStringNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "numConverterGetAsStringNPETest");
    invoke();
  }

} // end of URLClient
