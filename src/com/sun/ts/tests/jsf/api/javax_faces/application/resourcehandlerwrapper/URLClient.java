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
package com.sun.ts.tests.jsf.api.javax_faces.application.resourcehandlerwrapper;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

public final class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_appl_resourcehandlerwrapper_web";

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
   * @testName: resourceHandlerWrapperCreateResourceNTest
   * @assertion_ids: JSF:JAVADOC:293; JSF:JAVADOC:289; JSF:JAVADOC:298
   * @test_Strategy: Validate that a Resource is created when the following is
   *                 passed in. -resourceName
   * 
   * @since 2.0
   */
  public void resourceHandlerWrapperCreateResourceNTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "resourceHandlerWrapperCreateResourceNTest");
    invoke();
  }

  /**
   * @testName: resourceHandlerWrapperCreateResourceNLTest
   * @assertion_ids: JSF:JAVADOC:293; JSF:JAVADOC:290; JSF:JAVADOC:298
   * @test_Strategy: Validate that a Resource is created when the following is
   *                 passed in. -resourceName -libraryName
   * 
   * @since 2.0
   */
  public void resourceHandlerWrapperCreateResourceNLTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "resourceHandlerWrapperCreateResourceNLTest");
    invoke();
  }

  /**
   * @testName: resourceHandlerWrapperCreateResourceNLTTest
   * @assertion_ids: JSF:JAVADOC:293; JSF:JAVADOC:291; JSF:JAVADOC:298
   * @test_Strategy: Validate that a Resource is created when the following is
   *                 passed in. -resourceName -libraryName -contentType
   * 
   * @since 2.0
   */
  public void resourceHandlerWrapperCreateResourceNLTTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "resourceHandlerWrapperCreateResourceNLTTest");
    invoke();
  }

  /**
   * @testName: resourceHandlerWrapperCreateResourceNullTest
   * @assertion_ids: JSF:JAVADOC:293; JSF:JAVADOC:291; JSF:JAVADOC:298
   * @test_Strategy: Validate that a Resource is created when the following is
   *                 passed in. -resourceName -null -null
   * 
   * @since 2.0
   */
  public void resourceHandlerWrapperCreateResourceNullTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "resourceHandlerWrapperCreateResourceNullTest");
    invoke();
  }

  /**
   * @testName: resourceHandlerWrappergetRendererTypeTest
   * @assertion_ids: JSF:JAVADOC:293; JSF:JAVADOC:292; JSF:JAVADOC:298
   * @test_Strategy: Validate that the correct renderer-type is returned
   * 
   * @since 2.0
   */
  public void resourceHandlerWrappergetRendererTypeTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "resourceHandlerWrappergetRendererTypeTest");
    invoke();
  }

  /**
   * @testName: resourceHandlerWrapperHandleResourceRequestTest
   * @assertion_ids: JSF:JAVADOC:293; JSF:JAVADOC:294; JSF:JAVADOC:298
   * @test_Strategy: Validate that ResourceWrapperHandle
   * 
   * @since 2.0
   */
  public void resourceHandlerWrapperHandleResourceRequestTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "resourceHandlerWrapperHandleResourceRequestTest");
    invoke();
  }

  /**
   * @testName: resourceHandlerWrapperLibraryExistsTest
   * @assertion_ids: JSF:JAVADOC:293; JSF:JAVADOC:297; JSF:JAVADOC:298
   * @test_Strategy: Validate that a call to libraryExists(libraryName) returns
   *                 false if the library does not exist..
   * 
   * @since 2.0
   */
  public void resourceHandlerWrapperLibraryExistsTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "resourceHandlerWrapperLibraryExistsTest");
    invoke();
  }
} // end of URLClient
