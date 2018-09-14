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
package com.sun.ts.tests.jsf.api.javax_faces.application.resourcewrapper;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

public final class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_appl_resourcewrapper_web";

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
   * @testName: resourceWrapperGetInputStreamTest
   * @assertion_ids: JSF:JAVADOC:299; JSF:JAVADOC:305
   * @test_Strategy: Validate that a call to ResourceWrapper.getInputStream
   *                 returns with no errors.
   * 
   * @since 2.0
   */
  public void resourceWrapperGetInputStreamTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "resourceWrapperGetInputStreamTest");
    invoke();
  }

  /**
   * @testName: resourceWrapperGetRequestPathTest
   * @assertion_ids: JSF:JAVADOC:301; JSF:JAVADOC:304; JSF:JAVADOC:305
   * @test_Strategy: Validate that the correct path is returned.
   * 
   * @since 2.0
   */
  public void resourceWrapperGetRequestPathTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "resourceWrapperGetRequestPathTest");
    invoke();
  }

  /**
   * @testName: resourceWrapperGetRequestPathLibTest
   * @assertion_ids: JSF:JAVADOC:301; JSF:JAVADOC:304; JSF:JAVADOC:305
   * @test_Strategy: Validate that the correct path is returned.
   * 
   * @since 2.0
   */
  public void resourceWrapperGetRequestPathLibTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "resourceWrapperGetRequestPathLibTest");
    invoke();
  }

  /**
   * @testName: resourceWrapperGetURLTest
   * @assertion_ids: JSF:JAVADOC:303; JSF:JAVADOC:304; JSF:JAVADOC:305
   * @test_Strategy: Validate that the correct URL is returned.
   * 
   * @since 2.0
   */
  public void resourceWrapperGetURLTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "resourceWrapperGetURLTest");
    invoke();
  }

  /**
   * @testName: resourceWrapperGetResponseHeadersTest
   * @assertion_ids: JSF:JAVADOC:302; JSF:JAVADOC:304; JSF:JAVADOC:305
   * @test_Strategy: Validate that a Map Object is returned.
   * 
   * @since 2.0
   */
  public void resourceWrapperGetResponseHeadersTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "resourceWrapperGetResponseHeadersTest");
    invoke();
  }

  //
  // TODO: Add resourceWrapperInputStreamIOETest()
  //
} // end of URLClient
