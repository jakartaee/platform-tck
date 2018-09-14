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

package com.sun.ts.tests.jstl.spec.fmt.i18n.bundle;

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

    setContextRoot("/jstl_fmt_bundle_web");
    setGoldenFileDir("/jstl/spec/fmt/i18n/bundle");

    return super.run(args, out, err);
  }

  /*
   * @testName: positiveBundleBasenameTest
   * 
   * @assertion_ids: JSTL:SPEC:29; JSTL:SPEC:29.1; JSTL:SPEC:29.1.1
   * 
   * @testStrategy: Validate that ResourceBundles can be properly located by the
   * specifed basename attribute and that the appropriate messages are printed.
   */
  public void positiveBundleBasenameTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "positiveBundleBasenameTest");
    TEST_PROPS.setProperty(REQUEST, "positiveBundleBasenameTest.jsp");
    TEST_PROPS.setProperty(GOLDENFILE, "positiveBundleBasenameTest.gf");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    invoke();
  }

  /*
   * @testName: positiveBundlePrefixTest
   * 
   * @assertion_ids: JSTL:SPEC:29.2; JSTL:SPEC:29.2.1
   * 
   * @testStragegy: Validate that messages can be properly looked up and
   * displayed when a prefix is specified.
   */
  public void positiveBundlePrefixTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "positiveBundlePrefixTest");
    TEST_PROPS.setProperty(REQUEST, "positiveBundlePrefixTest.jsp");
    TEST_PROPS.setProperty(GOLDENFILE, "positiveBundlePrefixTest.gf");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    invoke();
  }

  /*
   * @testName: positiveBundleBasenameNullEmptyTest
   * 
   * @assertion_ids: JSTL:SPEC:29.9
   * 
   * @testStrategy: Validate that if basename is null or empty that any messages
   * that are displayed are in the form of "???<key>???" where <key> is the name
   * of the key that failed the lookup
   */
  public void positiveBundleBasenameNullEmptyTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "positiveBundleBasenameNullEmptyTest");
    TEST_PROPS.setProperty(REQUEST, "positiveBundleBasenameNullEmptyTest.jsp");
    TEST_PROPS.setProperty(GOLDENFILE,
        "positiveBundleBasenameNullEmptyTest.gf");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    invoke();
  }

  /*
   * @testName: positiveBundleLocalizationScopeTest
   * 
   * @assertion_ids: JSTL:SPEC:29; JSTL:SPEC:29.7
   * 
   * @testStrategy: Validate that if a LocalizationContext was established via a
   * bundle action, that any messages within the body of the bundle action are
   * properly localized and any message actions outside the body are not.
   */
  public void positiveBundleLocalizationScopeTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "positiveBundleLocalizationScopeTest");
    TEST_PROPS.setProperty(REQUEST, "positiveBundleLocalizationScopeTest.jsp");
    TEST_PROPS.setProperty(GOLDENFILE,
        "positiveBundleLocalizationScopeTest.gf");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    invoke();
  }

  /*
   * @testName: positiveBundleLocaleConfigurationTest
   * 
   * @assertion_ids: JSTL:SPEC:29; JSTL:SPEC:29.7
   * 
   * @testStrategy: Validate that if the javax.servlet.jsp.jstl.fmt.locale
   * configuration variable is available, that the bundle action, given a
   * basename, can properly load a resource bundle and that any nested message
   * actions can properly localize a message based on the parent bundle action.
   * To try to throw a wrench in things, the client will send a preferred locale
   * across the wire that, if used, will not resolve to any ResourceBundle (no
   * fallback defined).
   */
  public void positiveBundleLocaleConfigurationTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "positiveBundleLocaleConfigurationTest");
    TEST_PROPS.setProperty(REQUEST,
        "positiveBundleLocaleConfigurationTest.jsp");
    TEST_PROPS.setProperty(GOLDENFILE,
        "positiveBundleLocaleConfigurationTest.gf");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: ja");
    invoke();
  }

  /*
   * @testName: positiveBundleFallbackLocaleTest
   * 
   * @assertion_ids: JSTL:SPEC:29; JSTL:SPEC:29.1.3
   * 
   * @testStrategy: Validate that if the bundle action is unable to determine a
   * locale based of the locale configuration variable or the client's preferred
   * locales, it will use the fallbackLocale configuration variable. To try to
   * throw a wrench in things, the client will send a preferred locale across
   * the wire that, if used, will not resolve to any ResourceBundle (no fallback
   * defined). Additionally verify that the fallbackLocale variable can be
   * configured using a String representation of a locale as well as an Instance
   * of Locale.
   */
  public void positiveBundleFallbackLocaleTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "positiveBundleFallbackLocaleTest");
    TEST_PROPS.setProperty(GOLDENFILE, "positiveBundleFallbackLocaleTest.gf");
    TEST_PROPS.setProperty(REQUEST, "positiveBundleFallbackLocaleTest.jsp");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: ja");
    invoke();
  }
}
