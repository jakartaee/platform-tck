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
package com.sun.ts.tests.jsf.api.javax_faces.application.resourcehandler;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

public final class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_appl_resourcehandler_web";

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
   * @testName: resourceHandlerCreateResourceNTest
   * @assertion_ids: JSF:JAVADOC:288; JSF:JAVADOC:289; JSF:JAVADOC:277
   * @test_Strategy: Validate that a Resource is created when: - Passing name
   *                 Only.(N)
   *
   *                 createResource(String resourceName)
   *
   * @since 2.0
   */
  public void resourceHandlerCreateResourceNTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "resourceHandlerCreateResourceNTest");
    invoke();
  }

  /**
   * @testName: resourceHandlerCreateResourceNLTest
   * @assertion_ids: JSF:JAVADOC:288; JSF:JAVADOC:289; JSF:JAVADOC:279
   * @test_Strategy: Validate that a Resource is created when: - Passing name
   *                 and libraryName.(NL)
   *
   *                 createResource(String resourceName, String libraryName)
   *
   * @since 2.0
   */
  public void resourceHandlerCreateResourceNLTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "resourceHandlerCreateResourceNLTest");
    invoke();
  }

  /**
   * @testName: resourceHandlerCreateResourceNLCTest
   * @assertion_ids: JSF:JAVADOC:288; JSF:JAVADOC:289; JSF:JAVADOC:281
   * @test_Strategy: Validate that a Resource is created when: - Passing name,
   *                 libraryName, contentType.(NLC)
   *
   *                 createResource(String resourceName, String libraryName,
   *                 String contentType)
   *
   * @since 2.0
   */
  public void resourceHandlerCreateResourceNLCTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "resourceHandlerCreateResourceNLCTest");
    invoke();
  }

  /**
   * @testName: resourceHandlerCreateResourceNPETest
   * @assertion_ids: JSF:JAVADOC:278;JSF:JAVADOC:280;JSF:JAVADOC:282
   * @test_Strategy: Validate that a NullPointerException is thrown when: -
   *                 Passing 'null'. Method(s) Covered:
   *                 ResourceHandler.createResource(null)
   *                 ResourceHandler.createResource(null, libraryName)
   *                 ResourceHandler.createResource(null, libraryName,
   *                 contentType)
   *
   * @since 2.0
   */
  public void resourceHandlerCreateResourceNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "resourceHandlerCreateResourceNPETest");
    invoke();
  }

  /**
   * @testName: resourceHandlerCreateResourceContentNullTest
   * @assertion_ids: JSF:JAVADOC:288; JSF:JAVADOC:289; JSF:JAVADOC:281
   * @test_Strategy: Validate that null can be passed into ContentType.
   *
   * @since 2.0
   */
  public void resourceHandlerCreateResourceContentNullTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "resourceHandlerCreateResourceContentNullTest");
    invoke();
  }

  /**
   * @testName: resourceHandlerCreateResourceLibNullTest
   * @assertion_ids: JSF:JAVADOC:288; JSF:JAVADOC:289; JSF:JAVADOC:281
   * @test_Strategy: Validate that null can be passed into LibraryName.
   *
   * @since 2.0
   */
  public void resourceHandlerCreateResourceLibNullTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "resourceHandlerCreateResourceLibNullTest");
    invoke();
  }

  /**
   * @testName: resourceHandlerCreateResourceBothNullTest
   * @assertion_ids: JSF:JAVADOC:288; JSF:JAVADOC:289; JSF:JAVADOC:281
   * @test_Strategy: Validate that null can be passed into ContentType &
   *                 LibraryName.
   *
   * @since 2.0
   */
  public void resourceHandlerCreateResourceBothNullTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "resourceHandlerCreateResourceBothNullTest");
    invoke();
  }

  /**
   * @testName: resourceHandlerHandleResourceRequestTest
   * @assertion_ids: JSF:JAVADOC:288; JSF:JAVADOC:289; JSF:JAVADOC:284
   * @test_Strategy: Validate that a call to handleResourceRequest(FacesContext)
   *                 does not throw an Exception.
   *
   * @since 2.0
   */
  public void resourceHandlerHandleResourceRequestTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "resourceHandlerHandleResourceRequestTest");
    invoke();
  }

  /**
   * @testName: resourceHandlergetRendererTypeForResourceNameTest
   * @assertion_ids: JSF:JAVADOC:288; JSF:JAVADOC:289; JSF:JAVADOC:283
   * @test_Strategy: Validate that a call to
   *                 getRendererTypeForResourceName(ResourceName) returns the
   *                 correct RenderedType.
   *
   * @since 2.0
   */
  public void resourceHandlergetRendererTypeForResourceNameTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "resourceHandlergetRendererTypeForResourceNameTest");
    invoke();
  }

  /**
   * @testName: resourceHandlerlibraryExistsTest
   * @assertion_ids: JSF:JAVADOC:288; JSF:JAVADOC:289; JSF:JAVADOC:287
   * @test_Strategy: Validate that a call to libraryExists(libraryName) returns
   *                 true. If the Library exists and false if not.
   * 
   * @since 2.0
   */
  public void resourceHandlerlibraryExistsTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "resourceHandlerlibraryExistsTest");
    invoke();
  }

  /**
   * @testName: resourceHandlerIsResourceRenderedTest
   * @assertion_ids: PENDING
   * 
   * @since 2.0
   */
  public void resourceHandlerIsResourceRenderedTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "resourceHandlerIsResourceRenderedTest");
    invoke();
  }

  /**
   * @testName: resourceHandlerMarkResourceRenderedTest
   * @assertion_ids: PENDING
   * 
   * @since 2.0
   */
  public void resourceHandlerMarkResourceRenderedTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "resourceHandlerMarkResourceRenderedTest");
    invoke();
  }
} // end of URLClient
