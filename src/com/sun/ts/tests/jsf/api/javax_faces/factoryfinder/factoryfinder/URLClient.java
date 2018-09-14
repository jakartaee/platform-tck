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
package com.sun.ts.tests.jsf.api.javax_faces.factoryfinder.factoryfinder;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

import java.io.PrintWriter;

public class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_factoryfinder_web";

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

  /* Run test */
  /*
   * @testName: getFactoryTest
   * 
   * @assertion_ids: JSF:JAVADOC:7
   * 
   * @test_Strategy: Create (if necessary) and return a per-web-application
   * instance of the appropriate implementation class for the specified
   * JavaServer Faces factory class, based on the discovery algorithm described
   * in the class description.
   */
  public void getFactoryTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getFactoryTest");
    invoke();
  }

  /*
   * @testName: getFactoryNullPointerExceptionTest
   * 
   * @assertion_ids: JSF:JAVADOC:11
   * 
   * @test_Strategy: Validate a NullPointerException is thrown if a null
   * argument is passed to FactoryFinder.getFactor();
   */
  public void getFactoryNullPointerExceptionTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getFactoryNullPointerExceptionTest");
    invoke();
  }

  /*
   * @testName: getFactoryNoConfiguredIllegalArgumentExceptionTest
   * 
   * @assertion_ids: JSF:JAVADOC:9
   * 
   * @test_Strategy: Validate an IllegalArgumentException is thrown when an
   * unknown factory name is provided to FactoryFinder.findFactory().
   */
  public void getFactoryNoConfiguredIllegalArgumentExceptionTest()
      throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "getFactoryNoConfiguredIllegalArgumentExceptionTest");
    invoke();
  }

  /*
   * @testName: setFactoryNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:16
   * 
   * @test_Strategy: Validate a NullPointerException if factoryName is null in
   * FactoryFinder.setFactory(factoryName, implName);
   */
  public void setFactoryNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "setFactoryNPETest");
    invoke();
  }

  /*
   * @testName: setFactoryIAETest
   * 
   * @assertion_ids: JSF:JAVADOC:15
   * 
   * @test_Strategy: Validate a IllegalArgumentException if factoryName does not
   * identify a standard JavaServer Faces factory name in
   * FactoryFinder.setFactory(factoryName, implName);
   */
  public void setFactoryIAETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "setFactoryIAETest");
    invoke();
  }
}
