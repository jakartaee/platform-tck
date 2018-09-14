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

package com.sun.ts.tests.jstl.spec.fmt.i18n.setlocale;

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

    setContextRoot("/jstl_fmt_setlocale_web");
    setGoldenFileDir("/jstl/spec/fmt/i18n/setlocale");

    return super.run(args, out, err);
  }

  /*
   * @testName: positiveSetLocaleValueTest
   * 
   * @assertion_ids: JSTL:SPEC:28; JSTL:SPEC:28.1; JSTL:SPEC:28.1.1
   * 
   * @testStrategy: Validate value can accept both String representations of
   * locales as well as instances of java.util.Locale.
   */
  public void positiveSetLocaleValueTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveSetLocaleValueTest");
    invoke();
  }

  /*
   * @testName: positiveSetLocaleVariantTest
   * 
   * @assertion_ids: JSTL:SPEC:28.2; JSTL:SPEC:28.2.1
   * 
   * @testStrategy: Validate that variant can accept both dynamic and static
   * values as well as validate that the javax.servlet.jsp.jstl.fmt.locale
   * scoped variable is set with the proper value.
   */
  public void positiveSetLocaleVariantTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveSetLocaleVariantTest");
    invoke();
  }

  /*
   * @testName: positiveSetLocaleValueNullEmptyTest
   * 
   * @assertion_ids: JSTL:SPEC:28.6
   * 
   * @testStrategy: Validate that if value is provided with a null or empty
   * value that the runtime default locale is used.
   */
  public void positiveSetLocaleValueNullEmptyTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveSetLocaleValueNullEmptyTest");
    invoke();
  }

  /*
   * @testName: positiveSetLocaleScopeTest
   * 
   * @assertion_ids: JSTL:SPEC:28.1.2; JSTL:SPEC:28.3; JSTL:SPEC:28.3.1;
   * JSTL:SPEC:28.3.2; JSTL:SPEC:28.3.3; JSTL:SPEC:28.3.4; JSTL:SPEC:28.4
   * 
   * @testStrategy: Validate the behvior of the action with regards to scope. If
   * scope is specified, verify the javax.servlet.jsp.jstl.fmt.locale
   * configuration variable is in the expected scope. If scope is not specifed,
   * verify that it is in the page scope.
   */
  public void positiveSetLocaleScopeTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveSetLocaleScopeTest");
    invoke();
  }

  /*
   * @testName: positiveSetLocaleOverrideTest
   * 
   * @assertion_ids: JSTL:SPEC:108
   * 
   * @testStrategy: Validate that browser-based locales from an HTTP client are
   * not considered if the javax.servlet. jsp.jstl.fmt.locale attribute is
   * present. The client will send it's preferred locales of fr and sw, but the
   * page will be set to en_US. The en resources bundle should be used and not
   * the sw bundle.
   */
  public void positiveSetLocaleOverrideTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "positiveSetLocaleOverrideTest");
    TEST_PROPS.setProperty(REQUEST,
        "positiveSetLocaleOverrideTest.jsp?res=AlgoResources5&fall=de");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: sw");
    TEST_PROPS.setProperty(GOLDENFILE, "positiveSetLocaleOverrideTest.gf");
    invoke();
  }

  /*
   * @testName: positiveSetLocaleVariantIgnoreTest
   * 
   * @assertion_ids: JSTL:SPEC:28.7
   * 
   * @testStrategy: Validate that if the value attribute is provided a Locale
   * object, and the variant attribute is specified (using an invalid value),
   * that the variant is ignored and the expected locale of en_US is returned by
   * the test.
   */
  public void positiveSetLocaleVariantIgnoreTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveSetLocaleVariantIgnoreTest");
    invoke();
  }
}
