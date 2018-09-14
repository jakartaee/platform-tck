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
 * @(#)JSTLClient.java	1.2 03/19/02
 */

package com.sun.ts.tests.jstl.compat.onedotzero;

import java.io.PrintWriter;
import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.jstl.common.client.CompatAbstractUrlClient;

public class JSTLClient extends CompatAbstractUrlClient {

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home; jstl.db.url;
   * jstl.db.user; jstl.db.password; jstl.db.driver;
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

    setGeneralURI("/jstl/compat/onedotzero");
    setContextRoot("/jstl_1_0_compat_web");

    return super.run(args, out, err);
  }

  /*
   * @testName: positiveBundleLocalizationScopeTest
   * 
   * @assertion_ids: JSTL:SPEC:106.3.1
   * 
   * @testStrategy: Validate that if a LocalizationContext was established via a
   * bundle action, that any messages within the body of the bundle action are
   * properly localized and any message actions outside the body are not.
   */
  public void positiveBundleLocalizationScopeTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "positiveBundleLocalizationScopeTest");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_1_0_compat_web/positiveBundleLocalizationScopeTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(GOLDENFILE,
        "positiveBundleLocalizationScopeTest.gf");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    invoke();
  }

  /*
   * @testName: positiveCatchVarTest
   * 
   * @assertion_ids: JSTL:SPEC:42.1
   * 
   * @testStrategy: Validate that the catch action properly stores the Throable
   * into the variable name designated by the var attribute and validate the
   * type of var as it should be the type of the Throwable.
   */
  public void positiveCatchVarTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveCatchVarTest");
    invoke();
  }

  /*
   * @testName: positiveCWOTest
   * 
   * @assertion_ids: JSTL:SPEC:15.1; JSTL:SPEC:15.1.1; JSTL:SPEC:15.1.1.1;
   * JSTL:SPEC:15.1.1.2; JSTL:SPEC:15.2.1.1; JSTL:SPEC:15.2.1
   * 
   * @testStrategy: Validate the behavior/interaction of 'choose', 'when'
   * 'otherwise' actions. - single 'when' action evaluating to true - one 'when'
   * action evaluating to false and two 'when' actions evaluating to true. Only
   * the first when that evaluates to true should process it's body content -
   * single 'when' evaluating to false which should cause the 'otherwise' action
   * to process its body
   */
  public void positiveCWOTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveCWOTest");
    invoke();
  }

  /*
   * @testName: positiveDateParamQueryTimestampTest
   * 
   * @assertion_ids: JSTL:SPEC:94.2
   * 
   * @testStrategy: Validate the behavior of the sql:query action - That a query
   * can be executed using sql:dateParam action specifying a type of 'timestamp'
   * - Validate that you get the expected number of rows back.
   */
  public void positiveDateParamQueryTimestampTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD_COMPAT,
        "positiveDateParamQueryTimestampTest");
    invoke();
  }

  /*
   * @testName: positiveDefaultEncodingTest
   * 
   * @assertion_ids: JSTL:SPEC:45.4.2
   * 
   * @testStrategy: Validate that if no Content-Type header is sent by the
   * client, and the scoped variable,
   * javax.servlet.jsp.jstl.fmt.request.charset, is not present, the default
   * encoding of ISO-8859-1 is used. Validation will be by calling
   * getCharacterEncoding() against the request object after the action has been
   * called.
   */
  public void positiveDefaultEncodingTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveDefaultEncodingTest");
    invoke();
  }

  /*
   * @testName: positiveFDValueTest
   * 
   * @assertion_ids: JSTL:SPEC:48; JSTL:SPEC:57
   * 
   * @testStrategy: Validate that formatDate action can properly format dates
   * provided as String literals or as java.util.Date objects.
   */
  public void positiveFDValueTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveFDValueTest");
    invoke();
  }

  /*
   * @testName: positiveFNScopeTest
   * 
   * @assertion_ids: JSTL:SPEC:58.9.1
   * 
   * @testStrategy: Validate that the action exports var to the specified scope
   * as well as validating that if scope is not specified, var will be exported
   * to the page scope by default.
   */
  public void positiveFNScopeTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveFNScopeTest");
    invoke();
  }

  /*
   * @testName: positiveFormatLocalizationContextI18NTest
   * 
   * @assertion_ids: JSTL:SPEC:46.3
   * 
   * @testStrategy: If the javax.servlet.jsp.jstl.fmt.localizationContext
   * attribute is present, and the formatting action is not nested in a
   * <fmt:bundle> action, the base name attribute will take precedence over the
   * javax.servlet.jsp.jstl.fmt.locale scoped attribute. This will be verified
   * by setting the localizationContext attribute so that it will resolve to an
   * en_US bundle, and the set the locale attribute to de_DE. If the formatting
   * action correctly uses the locale from the base name attribute, then no
   * parse exception will occur.
   */
  public void positiveFormatLocalizationContextI18NTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME,
        "positiveFormatLocalizationContextI18NTest");
    TEST_PROPS.setProperty(REQUEST,
        "positiveFormatLocalizationContextI18NTest.jsp");
    TEST_PROPS.setProperty(GOLDENFILE,
        "positiveFormatLocalizationContextI18NTest.gf");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en-US");
    invoke();
  }

  /*
   * @testName: positiveForTokensTest
   * 
   * @assertion_ids: JSTL:SPEC:22
   * 
   * @testStrategy: Validate that forTokens can properly Iterate over a String
   * provided with specified delimiters.
   */
  public void positiveForTokensTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveForTokensTest");
    invoke();
  }

  /*
   * @testName: positiveIfScopeTest
   * 
   * @assertion_ids: JSTL:SPEC:14.3.1; JSTL:SPEC:14.3.2; JSTL:SPEC:14.3.3;
   * JSTL:SPEC:14.3.4; JSTL:SPEC:14.3.5
   * 
   * @testStrategy: Verify the behavior of the 'if' action when using the scope
   * attribute. If scope is not specified, the exported var should be in the
   * page scope, otherwise the exported var should be in the designated scope.
   */
  public void positiveIfScopeTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveIfScopeTest");
    invoke();
  }

  /*
   * @testName: positiveImportAbsUrlTest
   * 
   * @assertion_ids: JSTL:SPEC:23.1.3.1; JSTL:SPEC:23.1.3.2
   * 
   * @testStrategy: Validate that resources identified by an absolute URL can be
   * imported and displayed. This will test absolute URLs for HTTP and FTP
   */
  public void positiveImportAbsUrlTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveImportAbsUrlTest");
    invoke();
  }

  /*
   * @testName: positiveItemsObjArrayTest
   * 
   * @assertion_ids: JSTL:SPEC:21.2.1
   * 
   * @testStrategy: Validate that arrays of Object types can be handled by
   * 'forEach'.
   */
  public void positiveItemsObjArrayTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveItemsObjArrayTest");
    invoke();
  }

  /*
   * @testName: positiveJSTLURITest
   * 
   * @assertion_ids: JSTL:SPEC:1; JSTL:SPEC:2; JSTL:SPEC:3; JSTL:SPEC:4;
   * JSTL:SPEC:16; JSTL:SPEC:17; JSTL:SPEC:18; JSTL:SPEC:19
   * 
   * @testStrategy: Import all defined taglib URI definitions for both EL and RT
   * tags. If defined correctly, a fatal translation error should not occur (
   * per section 7.3.6.2 of the JavaServer Pages 1.2 Specification.
   */
  public void positiveJSTLURITest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "positiveJSTLURITest");
    TEST_PROPS.setProperty(REQUEST, "positiveJSTLURITest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    invoke();
  }

  /*
   * @testName: positiveMessageKeyTest
   * 
   * @assertion_ids: JSTL:SPEC:44.1
   * 
   * @testStrategy: Validate that the message action is able to properly lookup
   * and display the message associated with the provided key. This will also
   * establish that the key attribute can accept both static and dynamic values.
   */
  public void positiveMessageKeyTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "positiveMessageKeyTest");
    TEST_PROPS.setProperty(GOLDENFILE, "positiveMessageKeyTest.gf");
    TEST_PROPS.setProperty(REQUEST, "positiveMessageKeyTest.jsp");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    invoke();
  }

  /*
   * @testName: positiveOutEscXmlTest
   * 
   * @assertion_ids: JSTL:SPEC:68.3
   * 
   * @testStrategy: Validate that the escaping of XML entities (<,>,&,',") will
   * occur if the escapeXml is not present, or the value is true. Also validate
   * that no escaping occurs if the value is false.
   */
  public void positiveOutEscXmlTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveOutEscXmlTest");
    invoke();
  }

  /*
   * @testName: positiveParamNameValueTest
   * 
   * @assertion_ids: JSTL:SPEC:74.1; JSTL:SPEC:74.2
   * 
   * @testStrategy: Validate the the name and value attributes can accept both
   * dynamic and static values.
   */
  public void positiveParamNameValueTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveParamNameValueTest");
    invoke();
  }

  /*
   * @testName: positiveParamValueTest
   * 
   * @assertion_ids: JSTL:SPEC:75; JSTL:SPEC:75.1; JSTL:SPEC:75.1.1;
   * JSTL:SPEC:75.1.2
   * 
   * @testStrategy: Validate that parametric replacement occurs when param
   * subtags are specified with either dynamic or static values.
   */
  public void positiveParamValueTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "positiveParamValueTest");
    TEST_PROPS.setProperty(GOLDENFILE, "positiveParamValueTest.gf");
    TEST_PROPS.setProperty(REQUEST, "positiveParamValueTest.jsp");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    invoke();
  }

  /*
   * @testName: positiveParseVarTest
   * 
   * @assertion_ids: JSTL:SPEC:67; JSTL:SPEC:73.2.1.4
   * 
   * @testStrategy: Validate that if the parse operation is successfull, and var
   * is specified, the result is available via the PageContext and is of type
   * java.lang.Object.
   */
  public void positiveParseVarTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveParseVarTest");
    invoke();
  }

  /*
   * @testName: positivePDValueTest
   * 
   * @assertion_ids: JSTL:SPEC:57.22
   * 
   * @testStrategy: Validate that parseDate action can properly parse dates
   * provided as dynamic or static String values. Pass the parsed value to
   * formatDate to display (due to possible timezone difference).
   */
  public void positivePDValueTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positivePDValueTest");
    invoke();
  }

  /*
   * @testName: positivePermittedTlvTest
   * 
   * @assertion_ids: JSTL:SPEC:104.2; JSTL:SPEC:104.3
   * 
   * @testStrategy: Validate that if a URI that refers to a specific set of
   * libraries is specified as a paramter to the PermittedTaglibsTLV, that the
   * use of this library doesn't generate a translation error.
   */
  public void positivePermittedTlvTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "positivePermittedTlvTest");
    TEST_PROPS.setProperty(REQUEST, "positivePermittedTlvTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    invoke();
  }

  /*
   * @testName: positivePNValueTest
   * 
   * @assertion_ids: JSTL:SPEC:58
   * 
   * @testStrategy: Validate the action can correctly parse dynamic and static
   * values provided to the value attribute.
   */
  public void positivePNValueTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positivePNValueTest");
    invoke();
  }

  /*
   * @testName: positiveQueryScopeAttributeTest
   * 
   * @assertion_ids: JSTL:SPEC:59.6; JSTL:SPEC:59.6.1; JSTL:SPEC:59.6.2;
   * JSTL:SPEC:59.6.3; JSTL:SPEC:59.6.4; JSTL:SPEC:59.14
   * 
   * @testStrategy: Validate that the action exports var to the specified scope
   * as well as validating that if scope is not specified, var will be exported
   * to the page scope by default.
   */
  public void positiveQueryScopeAttributeTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveQueryScopeAttributeTest");
    invoke();
  }

  /*
   * @testName: positiveRedirectTest
   * 
   * @assertion_ids: JSTL:SPEC:43; JSTL:SPEC:43.1; JSTL:SPEC:43.1.1;
   * JSTL:SPEC:43.1.2
   * 
   * @testStrategy: Validate that the action can properly redirect when the url
   * attribute is provided either a dynamic or static values.
   */
  public void positiveRedirectTest() throws Fault {
    /* EL */
    // abs
    TEST_PROPS.setProperty(TEST_NAME, "positiveRedirectTest");
    TEST_PROPS.setProperty(REQUEST, "positiveRedirectTest.jsp?el1=true");
    TEST_PROPS.setProperty(IGNORE_BODY, "true");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Location:  http://" + _hostname + ":" + _port
            + "/jstl_1_0_compat_web/jstl/core/urlresource/param/import.jsp");
    invoke();
    TEST_PROPS.setProperty(TEST_NAME, "positiveRedirectTest");
    TEST_PROPS.setProperty(IGNORE_BODY, "true");
    TEST_PROPS.setProperty(REQUEST, "positiveRedirectTest.jsp?el2=true");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Location:  http://" + _hostname + ":" + _port
            + "/jstl_1_0_compat_web/jstl/core/urlresource/param/import.jsp");
    invoke();

    // rel
    TEST_PROPS.setProperty(TEST_NAME, "positiveRedirectTest");
    TEST_PROPS.setProperty(IGNORE_BODY, "true");
    TEST_PROPS.setProperty(REQUEST, "positiveRedirectTest.jsp?el3=true");
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "Location:  http://" + _hostname
        + ":" + _port + "/jstl_1_0_compat_web/import.jsp");
    invoke();
    TEST_PROPS.setProperty(TEST_NAME, "positiveRedirectTest");
    TEST_PROPS.setProperty(IGNORE_BODY, "true");
    TEST_PROPS.setProperty(REQUEST, "positiveRedirectTest.jsp?el4=true");
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "Location:  http://" + _hostname
        + ":" + _port + "/jstl_1_0_compat_web/import.jsp");
    invoke();

    // foreign context
    TEST_PROPS.setProperty(TEST_NAME, "positiveRedirectTest");
    TEST_PROPS.setProperty(IGNORE_BODY, "true");
    TEST_PROPS.setProperty(REQUEST, "positiveRedirectTest.jsp?el5=true");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Location:  http://" + _hostname + ":" + _port
            + "/jstl_1_0_compat_web/jstl/core/urlresource/param/import.jsp");
    invoke();
    TEST_PROPS.setProperty(TEST_NAME, "positiveRedirectTest");
    TEST_PROPS.setProperty(IGNORE_BODY, "true");
    TEST_PROPS.setProperty(REQUEST, "positiveRedirectTest.jsp?el6=true");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Location:  http://" + _hostname + ":" + _port
            + "/jstl_1_0_compat_web/jstl/core/urlresource/param/import.jsp");
    invoke();

    /* RT */
    // abs
    TEST_PROPS.setProperty(TEST_NAME, "positiveRedirectTest");
    TEST_PROPS.setProperty(IGNORE_BODY, "true");
    TEST_PROPS.setProperty(REQUEST, "positiveRedirectTest.jsp?rt1=true");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Location:  http://" + _hostname + ":" + _port
            + "/jstl_1_0_compat_web/jstl/core/urlresource/param/import.jsp");
    invoke();
    TEST_PROPS.setProperty(TEST_NAME, "positiveRedirectTest");
    TEST_PROPS.setProperty(IGNORE_BODY, "true");
    TEST_PROPS.setProperty(REQUEST, "positiveRedirectTest.jsp?rt2=true");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Location:  http://" + _hostname + ":" + _port
            + "/jstl_1_0_compat_web/jstl/core/urlresource/param/import.jsp");
    invoke();

    // rel
    TEST_PROPS.setProperty(TEST_NAME, "positiveRedirectTest");
    TEST_PROPS.setProperty(IGNORE_BODY, "true");
    TEST_PROPS.setProperty(REQUEST, "positiveRedirectTest.jsp?rt3=true");
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "Location:  http://" + _hostname
        + ":" + _port + "/jstl_1_0_compat_web/import.jsp");
    invoke();
    TEST_PROPS.setProperty(TEST_NAME, "positiveRedirectTest");
    TEST_PROPS.setProperty(IGNORE_BODY, "true");
    TEST_PROPS.setProperty(REQUEST, "positiveRedirectTest.jsp?rt4=true");
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "Location:  http://" + _hostname
        + ":" + _port + "/jstl_1_0_compat_web/import.jsp");
    invoke();

    // foreign context
    TEST_PROPS.setProperty(TEST_NAME, "positiveRedirectTest");
    TEST_PROPS.setProperty(IGNORE_BODY, "true");
    TEST_PROPS.setProperty(REQUEST, "positiveRedirectTest.jsp?rt5=true");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Location:  http://" + _hostname + ":" + _port
            + "/jstl_1_0_compat_web/jstl/core/urlresource/param/import.jsp");
    invoke();
    TEST_PROPS.setProperty(TEST_NAME, "positiveRedirectTest");
    TEST_PROPS.setProperty(IGNORE_BODY, "true");
    TEST_PROPS.setProperty(REQUEST, "positiveRedirectTest.jsp?rt6=true");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Location:  http://" + _hostname + ":" + _port
            + "/jstl_1_0_compat_web/jstl/core/urlresource/param/import.jsp");
    invoke();

  }

  /*
   * @testName: positiveRemoveScopeVarTest
   * 
   * @assertion_ids: JSTL:SPEC:41.1; JSTL:SPEC:41.2; JSTL:SPEC:41.3
   * 
   * @testStrategy: Validate that the remove action can properly remove a scoped
   * attribute by providing only a value to the var attribute. This should
   * remove the attribute no matter the scope in which it exists. Also validate
   * that if scope is specified, the var is properly removed.
   */
  public void positiveRemoveScopeVarTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveRemoveScopeVarTest");
    invoke();
  }

  /*
   * @testName: positiveResultGetRowsCountTest
   * 
   * @assertion_ids: JSTL:SPEC:59
   * 
   * @testStrategy: Validate that the correct number of rows is returned by
   * Result.getRows().
   */
  public void positiveResultGetRowsCountTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveResultGetRowsCountTest");
    invoke();
  }

  /*
   * @testName: negativeScriptFreeTlvNoDeclTest
   * 
   * @assertion_ids: JSTL:SPEC:22.1
   * 
   * @testStrategy: Validate that if declarations aren't allowed per the
   * configured validator, a translation error occurs if a declaration is
   * encountered.
   */
  public void negativeScriptFreeTlvNoDeclTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "negativeScriptFreeTlvNoDeclTest");
    TEST_PROPS.setProperty(REQUEST, "negativeScriptFreeTlvNoDeclTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: positiveSetBundleScopeVarTest
   * 
   * @assertion_ids: JSTL:SPEC:22.1; JSTL:SPEC:22.1.1
   * 
   * @testStrategy: Validate the behavior of the action when both scope and var
   * are specified. Var should be exported to the scope as specified by the
   * scope attribute. If scope is not present and var is, var will be exported
   * to the page scope by default.
   */
  public void positiveSetBundleScopeVarTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "positiveSetBundleScopeVarTest");
    TEST_PROPS.setProperty(REQUEST, "positiveSetBundleScopeVarTest.jsp");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    TEST_PROPS.setProperty(GOLDENFILE, "positiveSetBundleScopeVarTest.gf");
    invoke();
  }

  /*
   * @testName: positiveSetDataSourceScopeAttributeTest
   * 
   * @assertion_ids: JSTL:SPEC:62; JSTL:SPEC:62.6; JSTL:SPEC:62.6.1
   * 
   * @testStrategy: Validate sql:setDataSource exports the var attribute to the
   * correct scope.
   */
  public void positiveSetDataSourceScopeAttributeTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD_COMPAT,
        "positiveSetDataSourceScopeAttributeTest");
    invoke();
  }

  /*
   * @testName: positiveSetLocaleValueTest
   * 
   * @assertion_ids: JSTL:SPEC:27; JSTL:SPEC:27.1; JSTL:SPEC:28
   * 
   * @testStrategy: Validate value can accept both String representations or
   * locales as well as instances of java.util.Locale.
   */
  public void positiveSetLocaleValueTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveSetLocaleValueTest");
    invoke();
  }

  /*
   * @testName: positiveSetScopeTest
   * 
   * @assertion_ids: JSTL:SPEC:13.4.1; JSTL:SPEC:13.4.2; JSTL:SPEC:13.4.3;
   * JSTL:SPEC:13.4.4; JSTL:SPEC:13.5
   * 
   * @testStrategy: Validated the different scope behaviors (default and
   * explicitly set scopes) by exporting different vairables to the assorted
   * scopes and then print the values using PageContext.getAttribute(String,
   * int).
   */
  public void positiveSetScopeTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveSetScopeTest");
    invoke();
  }

  /*
   * @testName: positiveSetSelectVarTest
   * 
   * @assertion_ids: JSTL:SPEC:69; JSTL:SPEC:69.2
   * 
   * @testStrategy: Validate the action properly sets a scoped variable when
   * select is provided a valid XPath expression and the the variable reference
   * by var is available to another action.
   */
  public void positiveSetSelectVarTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveSetSelectVarTest");
    invoke();
  }

  /*
   * @testName: positiveSetTimezoneValueTest
   * 
   * @assertion_ids: JSTL:SPEC:57.1; JSTL:SPEC:57.1.1; JSTL:SPEC:57.1.2;
   * JSTL:SPEC:57.1.3
   * 
   * @testStrategy: Validate that the value attribute can accept both static
   * values as well as three letter timezones (ex. PST) or fully qualified
   * values (ex. America/Los_Angeles).
   */
  public void positiveSetTimezoneValueTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveSetTimezoneValueTest");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    invoke();
  }

  /*
   * @testName: positiveTimezoneValueTest
   * 
   * @assertion_ids: JSTL:SPEC:57.1; JSTL:SPEC:57.1.1; JSTL:SPEC:57.1.2;
   * JSTL:SPEC:57.1.3
   * 
   * @testStrategy: Validate that the value attribute can accept both static
   * values as well as three letter timezones (ex. PST) or fully qualified
   * values (ex. America/Los_Angeles).
   */
  public void positiveTimezoneValueTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveTimezoneValueTest");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    invoke();
  }

  /*
   * @testName: positiveTransformVarTest
   * 
   * @assertion_ids: JSTL:SPEC:73; JSTL:SPEC:73.2; JSTL:SPEC:73.2.1.1;
   * JSTL:SPEC:73.2.1.2; JSTL:SPEC:73.2.1.3; JSTL:SPEC:73.2.1.4;
   * JSTL:SPEC:73.2.1.5; JSTL:SPEC:73.2.1.6
   * 
   * @testStrategy: Validate that if var is specified, the variable name
   * reference by var is available in the PageContext and is of type
   * org.w3c.dom.Document.
   */
  public void positiveTransformVarTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveTransformVarTest");
    invoke();
  }

  /*
   * @testName: positiveTxQueryCommitTest
   * 
   * @assertion_ids: JSTL:SPEC:59.7; JSTL:SPEC:59.9
   * 
   * @testStrategy: Validate sql:transaction and sql:query actions allow a query
   * to be successfully executed.
   */
  public void positiveTxQueryCommitTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveTxQueryCommitTest");
    invoke();
  }

  /*
   * @testName: positiveUpdateBodyContentTest
   * 
   * @assertion_ids: JSTL:SPEC:60; JSTL:SPEC:60.1
   * 
   * @testStrategy: Validate the sql:update action - That a SQL query can be
   * passed as body content.
   */
  public void positiveUpdateBodyContentTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveUpdateBodyContentTest");
    invoke();
  }

  /*
   * @testName: positiveUrlScopeTest
   * 
   * @assertion_ids: JSTL:SPEC:24; JSTL:SPEC:24.2
   * 
   * @testStrategy: Validate the behavior of the scope attribute with respect to
   * var, both when scope is explicitly defined and when not defined.
   */
  public void positiveUrlScopeTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveUrlScopeTest");
    invoke();
  }

  /*
   * @testName: positiveXParamNameTest
   * 
   * @assertion_ids: JSTL:SPEC:73.13
   * 
   * @testStrategy: Validate the name attribute of the x:param action is able to
   * accept both static and dynamic values.
   */
  public void positiveXParamNameTest() throws Fault {
    // TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveXParamNameTest");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_1_0_compat_web/positiveXParamNameTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "10pt|Param properly used|10pt|Param properly used");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "REPLACE");
    invoke();
  }
}
