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

package com.sun.ts.tests.jsf.api.javax_faces.convert.booleanconverter;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

import java.io.PrintWriter;

public final class URLClient extends AbstractUrlClient {

  private static final String TYPE = "boolean";

  private static final String CONTEXT_ROOT = "/jsf_convert_boolean_web";

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
   * @testName: booleanConverterGetAsObjectTest
   * @assertion_ids: JSF:JAVADOC:1553; JSF:JAVADOC:1554; JSF:JAVADOC:1574
   * @test_Strategy: Ensure the proper result is returned when passing in a
   *                 valid value, null, or a zero-length String.
   */
  public void booleanConverterGetAsObjectTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "converterGetAsObjectTest&type=" + TYPE);
    invoke();
  }

  /**
   * @testName: booleanConverterGetAsObjectNPETest
   * @assertion_ids: JSF:JAVADOC:1553; JSF:JAVADOC:1556; JSF:JAVADOC:1576
   * @test_Strategy: Ensure an NPE is thrown if either the context or component
   *                 parameters are null.
   */
  public void booleanConverterGetAsObjectNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "converterGetAsObjectNPETest&type=" + TYPE);
    invoke();
  }

  /**
   * @testName: booleanConverterGetAsStringTest
   * @assertion_ids: JSF:JAVADOC:1553; JSF:JAVADOC:1557; JSF:JAVADOC:1577
   * @test_Strategy: Ensure the proper result is returned when passing in a
   *                 valid value, null, or a zero-length String.
   */
  public void booleanConverterGetAsStringTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "converterGetAsStringTest&type=" + TYPE);
    invoke();
  }

  /**
   * @testName: booleanConverterGetAsStringNPETest
   * @assertion_ids: JSF:JAVADOC:1553; JSF:JAVADOC:1559; JSF:JAVADOC:1579
   * @test_Strategy: Ensure an NPE is thrown if either the context or component
   *                 parameters are null.
   */
  public void booleanConverterGetAsStringNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "converterGetAsStringNPETest&type=" + TYPE);
    invoke();
  }

} // end of URLClient
