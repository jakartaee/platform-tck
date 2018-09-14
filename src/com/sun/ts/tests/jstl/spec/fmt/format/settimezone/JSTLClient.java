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
 * @(#)JSTLClient.java	1.1 04/02/02
 */

package com.sun.ts.tests.jstl.spec.fmt.format.settimezone;

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

    setContextRoot("/jstl_fmt_stz_web");
    setGoldenFileDir("/jstl/spec/fmt/format/settimezone");

    return super.run(args, out, err);
  }

  /*
   * @testName: positiveSetTimezoneValueTest
   * 
   * @assertion_ids: JSTL:SPEC:93; JSTL:SPEC:93.1; JSTL:SPEC:93.1.1;
   * JSTL:SPEC:93.1.2; JSTL:SPEC:93.1.3; JSTL:SPEC:93.1.4; JSTL:SPEC:93.1.5;
   * JSTL:SPEC:93.1.6
   * 
   * @testStrategy: Validate that the value attribute can accept dynamic values
   * as well as three letter timezones (ex. PST) or fully qualified values (ex.
   * America/Los_Angeles).
   */
  public void positiveSetTimezoneValueTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveSetTimezoneValueTest");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    invoke();
  }

  /*
   * @testName: positiveSetTimezoneVarTest
   * 
   * @assertion_ids: JSTL:SPEC:93.2; JSTL:SPEC:93.2.1
   * 
   * @testStrategy: Validate that a scoped variable of type java.util.TimeZone
   * is properly set and associated with the variable name specified by var.
   */
  public void positiveSetTimezoneVarTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveSetTimezoneVarTest");
    invoke();
  }

  /*
   * @testName: positiveSetTimezoneScopeTest
   * 
   * @assertion_ids: JSTL:SPEC:93.3; JSTL:SPEC:93.3.1; JSTL:SPEC:93.3.2;
   * JSTL:SPEC:93.3.3; JSTL:SPEC:93.3.4; JSTL:SPEC:93.3.5
   * 
   * @testStrategy: Validate that the through explicit use of the scope
   * attribute, var is exported to the appropriate scope. Additionally, validate
   * that if var is specified and scope is not, that var is exported to the page
   * scope by default.
   */
  public void positiveSetTimezoneScopeTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveSetTimezoneScopeTest");
    invoke();
  }

  /*
   * @testName: positiveSetTimezoneValueNullEmptyTest
   * 
   * @assertion_ids: JSTL:SPEC:93.7
   * 
   * @testStrategy: Validate that if the value attribute is null or empty, the
   * GMT+0 timezone is used by the formatting actions that rely on timezone.
   */
  public void positiveSetTimezoneValueNullEmptyTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveSetTimezoneValueNullEmptyTest");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    invoke();
  }

  /*
   * @testName: positiveSetTimezoneSetAttrTest
   * 
   * @assertion_ids: JSTL:SPEC:93.4
   * 
   * @testStrategy: Validate that if var is not set, the scoped variable
   * javax.servlet.jsp.jstl.fmt.timeZone is properly set.
   */
  public void positiveSetTimezoneSetAttrTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveSetTimezoneSetAttrTest");
    invoke();
  }

  /*
   * @testName: positiveSetTimezoneAttrScopeTest
   * 
   * @assertion_ids: JSTL:SPEC:93.3; JSTL:SPEC:93.3.1; JSTL:SPEC:93.3.2;
   * JSTL:SPEC:93.3.3; JSTL:SPEC:93.3.4; JSTL:SPEC:93.3.5; JSTL:SPEC:93.4
   * 
   * @testStrategy: Validate that if var is not specified, but scope is, that
   * the scoped variable, javax.servlet.jsp.jstl.fmt.timeZone is exported to the
   * appropriate scope.
   */
  public void positiveSetTimezoneAttrScopeTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveSetTimezoneAttrScopeTest");
    invoke();
  }

}
