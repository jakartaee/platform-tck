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

package com.sun.ts.tests.jsf.api.javax_faces.convert.longconverter;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

import java.io.PrintWriter;

public final class URLClient extends AbstractUrlClient {

  private static final String TYPE = "long";

  private static final String CONTEXT_ROOT = "/jsf_convert_long_web";

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

  /**
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /** Test Declarations */

  /**
   * @testName: longConverterGetAsObjectTest
   * @assertion_ids: JSF:JAVADOC:1574; JSF:JAVADOC:1657; JSF:JAVADOC:1651
   * @test_Strategy: Ensure the proper result is returned when passing in a
   *                 valid value, null, or a zero-length String.
   */
  public void longConverterGetAsObjectTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "converterGetAsObjectTest&type=" + TYPE);
    invoke();
  }

  /**
   * @testName: longConverterGetAsObjectNPETest
   * @assertion_ids: JSF:JAVADOC:1576; JSF:JAVADOC:1657; JSF:JAVADOC:1653
   * @test_Strategy: Ensure an NPE is thrown if either the context or component
   *                 parameters are null.
   */
  public void longConverterGetAsObjectNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "converterGetAsObjectNPETest&type=" + TYPE);
    invoke();
  }

  /**
   * @testName: longConverterGetAsStringTest
   * @assertion_ids: JSF:JAVADOC:1577; JSF:JAVADOC:1657; JSF:JAVADOC:1654
   * @test_Strategy: Ensure the proper result is returned when passing in a
   *                 valid value, null, or a zero-length String.
   */
  public void longConverterGetAsStringTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "converterGetAsStringTest&type=" + TYPE);
    invoke();
  }

  /**
   * @testName: longConverterGetAsStringNPETest
   * @assertion_ids: JSF:JAVADOC:1579; JSF:JAVADOC:1657; JSF:JAVADOC:1656
   * @test_Strategy: Ensure an NPE is thrown if either the context or component
   *                 parameters are null.
   */
  public void longConverterGetAsStringNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "converterGetAsStringNPETest&type=" + TYPE);
    invoke();
  }

} // end of URLClient
