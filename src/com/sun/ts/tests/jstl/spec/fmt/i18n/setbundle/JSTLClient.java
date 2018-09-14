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

package com.sun.ts.tests.jstl.spec.fmt.i18n.setbundle;

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

    setContextRoot("/jstl_fmt_setbundle_web");
    setGoldenFileDir("/jstl/spec/fmt/i18n/setbundle");

    return super.run(args, out, err);
  }

  /*
   * @testName: positiveSetBundleBasenameTest
   * 
   * @assertion_ids: JSTL:SPEC:92; JSTL:SPEC:92.1; JSTL:SPEC:92.1.1;
   * JSTL:SPEC:92.2; JSTL:SPEC:92.5
   * 
   * @testStrategy: Validate the behavior of the setBundle action when basename
   * is present and var is not. The configuration variable
   * javax.servlet.jsp.jstl.fmt.localizationContext will be set with the
   * LocalizationContext returned from the lookup algorithm.
   */
  public void positiveSetBundleBasenameTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "positiveSetBundleBasenameTest");
    TEST_PROPS.setProperty(REQUEST, "positiveSetBundleBasenameTest.jsp");
    TEST_PROPS.setProperty(GOLDENFILE, "positiveSetBundleBasenameTest.gf");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    invoke();
  }

  /*
   * @testName: positiveSetBundleVarTest
   * 
   * @assertion_ids: JSTL:SPEC:92.4
   * 
   * @testStrategy: Validate that if var is specified, the result of the
   * resource lookup is stored in the PageContext accessible by referencing var
   * and is of type javax.servlet.jsp.jstl.fmt.LocalizationContext.
   */
  public void positiveSetBundleVarTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "positiveSetBundleVarTest");
    TEST_PROPS.setProperty(REQUEST, "positiveSetBundleVarTest.jsp");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    TEST_PROPS.setProperty(GOLDENFILE, "positiveSetBundleVarTest.gf");
    invoke();
  }

  /*
   * @testName: positiveSetBundleScopeVarTest
   * 
   * @assertion_ids: JSTL:SPEC:92.6; JSTL:SPEC:92.6.1; JSTL:SPEC:92.6.2;
   * JSTL:SPEC:92.6.3; JSTL:SPEC:92.6.4; JSTL:SPEC:92.6.6
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
   * @testName: positiveSetBundleScopeLocCtxTest
   * 
   * @assertion_ids: JSTL:SPEC:92.5; JSTL:SPEC:92.6; JSTL:SPEC:92.6.1;
   * JSTL:SPEC:92.6.2; JSTL:SPEC:92.6.3; JSTL:SPEC:92.6.4; JSTL:SPEC:92.6.6
   * 
   * @testStrategy: Validate the behavior of the action when scope is specified
   * and var is not. The action should properly export the configuration
   * variable javax.servlet.jsp.jstl.fmt.localizationContext to the scope
   * specified. If scope is not specified, then the configuration variable will
   * be exported to the page scope by default.
   */
  public void positiveSetBundleScopeLocCtxTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "positiveSetBundleScopeLocCtxTest");
    TEST_PROPS.setProperty(REQUEST, "positiveSetBundleScopeLocCtxTest.jsp");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    TEST_PROPS.setProperty(GOLDENFILE, "positiveSetBundleScopeLocCtxTest.gf");
    invoke();
  }

  /*
   * @testName: positiveSetBundleBasenameNullEmptyTest
   * 
   * @assertion_ids: JSTL:SPEC:92.3
   * 
   * @testStrategy: Validate that if basename is null or empty, that no
   * exception occurs and the result of the action is a null
   * LocalizationContext.
   */
  public void positiveSetBundleBasenameNullEmptyTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "positiveSetBundleBasenameNullEmptyTest");
    TEST_PROPS.setProperty(REQUEST,
        "positiveSetBundleBasenameNullEmptyTest.jsp");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    TEST_PROPS.setProperty(GOLDENFILE,
        "positiveSetBundleBasenameNullEmptyTest.gf");
    invoke();
  }

  /*
   * @testName: positiveSetBundleLocaleConfigurationTest
   * 
   * @assertion_ids: JSTL:SPEC:92.2
   * 
   * @testStrategy: Validate that if the javax.servlet.jsp.jstl.fmt.locale
   * configuration variable is present, that setBundle, both when it sets the
   * javax.servlet.jsp.jstl.fmt.localizationContext configuration variable and
   * when it exports a LocalizationContext, is able to lookup the proper
   * ResourceBundle. To try to throw a wrench in things, the client will send a
   * preferred locale across the wire that, if used, will not resolve to any
   * ResourceBundle (no fallback defined).
   */
  public void positiveSetBundleLocaleConfigurationTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME,
        "positiveSetBundleLocaleConfigurationTest");
    TEST_PROPS.setProperty(REQUEST,
        "positiveSetBundleLocaleConfigurationTest.jsp");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: ja");
    TEST_PROPS.setProperty(GOLDENFILE,
        "positiveSetBundleLocaleConfigurationTest.gf");
    invoke();
  }

  /*
   * @testName: positiveSetBundleFallbackLocaleTest
   * 
   * @assertion_ids: JSTL:SPEC:92.2
   * 
   * @testStrategy: Validate that if the setBundle action is unable to determine
   * a locale based on the locale configuration variable or the client's
   * preferred locales, it will use the fallbackLocale configuration variable.
   * To try to throw a wrench in things, the client will send a preferred locale
   * across the wire that, if used, will not resolve to any ResourceBundle (no
   * fallback defined). Additionally verify that the fallbackLocale variable can
   * be configured using a String representation of a locale as well as an
   * instance of Locale.
   */
  public void positiveSetBundleFallbackLocaleTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "positiveSetBundleFallbackLocaleTest");
    TEST_PROPS.setProperty(GOLDENFILE,
        "positiveSetBundleFallbackLocaleTest.gf");
    TEST_PROPS.setProperty(REQUEST, "positiveSetBundleFallbackLocaleTest.jsp");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: ja");
    invoke();
  }
}
