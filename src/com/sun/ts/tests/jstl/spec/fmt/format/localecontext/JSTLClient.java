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

package com.sun.ts.tests.jstl.spec.fmt.format.localecontext;

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

    setContextRoot("/jstl_fmt_locctx_web");
    setGoldenFileDir("/jstl/spec/fmt/format/localecontext");

    return super.run(args, out, err);
  }

  /*
   * @testName: positiveFormatLocalizationContextBundleTest
   * 
   * @assertion_ids: JSTL:SPEC:46.1
   * 
   * @testStrategy: If a formatting action is nested within a <fmt:bundle>
   * action, the formatting action will use the locale of the parent action.
   * This will be verified by Setting the
   * javax.servlet.jsp.jstl.fmt.localizationContext scoped variable. The
   * basename attribute will refer to an fr_FR bundle. The fmt:setBundle action
   * that encloses a formatting action will have a single en_US resource bundle,
   * so the resulting locale will be en_US. If the action chooses the proper
   * locale, no parse exception will occur.
   */
  public void positiveFormatLocalizationContextBundleTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME,
        "positiveFormatLocalizationContextBundleTest");
    TEST_PROPS.setProperty(REQUEST,
        "positiveFormatLocalizationContextBundleTest.jsp");
    TEST_PROPS.setProperty(GOLDENFILE,
        "positiveFormatLocalizationContextBundleTest.gf");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en-US");
    invoke();
  }

  /*
   * @testName: positiveFormatLocalizationContextI18NTest
   * 
   * @assertion_ids: JSTL:SPEC:46.2
   * 
   * @testStrategy: If the javax.servlet.jsp.jstl.fmt.localizationContext
   * attribute is present, and the formatting action is not nested in a
   * <fmt:bundle> action, the basename attribute will take precedence over the
   * javax.servlet.jsp.jstl.fmt.locale scoped attribute. This will be verified
   * by setting the localizationContext attribute so that it will resolve to an
   * en_US bundle, and the set the locale attribute to de_DE. If the formatting
   * action correctly uses the locale from the basename attribute, then no parse
   * exception will occur.
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
   * @testName: positiveFormatLocalizationContextLocaleTest
   * 
   * @assertion_ids: JSTL:SPEC:105.1
   * 
   * @testStrategy: If the javax.servlet.jsp.jstl.fmt.locale attribute is set,
   * the locale specified by this attribute will be used vs. those provided by
   * the browser (i.e. preferred locales from an Accept-Language header). This
   * will be verified by setting the locale attribute to en_US and the client's
   * preferred locale to de_DE. If the locale attribute is used, no parse
   * exception will occur.
   */
  public void positiveFormatLocalizationContextLocaleTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME,
        "positiveFormatLocalizationContextLocaleTest");
    TEST_PROPS.setProperty(REQUEST,
        "positiveFormatLocalizationContextLocaleTest.jsp");
    TEST_PROPS.setProperty(GOLDENFILE,
        "positiveFormatLocalizationContextLocaleTest.gf");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: de-DE");
    invoke();
  }

  /*
   * @testName: positiveFormatLocalizationContextBrowserLocaleTest
   * 
   * @assertion_ids: JSTL:SPEC:46.4
   * 
   * @testStrategy: If the formatting action is not wrapped in a <fmt:bundle>
   * action, nor are the javax.servlet.jsp.jstl.fmt.localizationContext or
   * javax.servlet.jsp.jstl.fmt.locale attributes set, the formatting locale
   * will be based on the preferred locales provided by the client (via the
   * Accept-Language request header).
   */
  public void positiveFormatLocalizationContextBrowserLocaleTest()
      throws Fault {
    TEST_PROPS.setProperty(TEST_NAME,
        "positiveFormatLocalizationContextBrowserLocaleTest");
    TEST_PROPS.setProperty(REQUEST,
        "positiveFormatLocalizationContextBrowserLocaleTest.jsp");
    TEST_PROPS.setProperty(GOLDENFILE,
        "positiveFormatLocalizationContextBrowserLocaleTest.gf");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en-US");
    invoke();
  }
}
