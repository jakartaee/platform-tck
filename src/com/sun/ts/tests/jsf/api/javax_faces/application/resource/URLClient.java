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
package com.sun.ts.tests.jsf.api.javax_faces.application.resource;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

public final class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_appl_resource_web";

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
   * @testName: resourceGetContentTypeTest
   * @assertion_ids: JSF:JAVADOC:266; JSF:JAVADOC:258
   * @test_Strategy: Validate that the correct MIME type is returned.
   * 
   * @since 2.0
   */
  public void resourceGetContentTypeTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "resourceGetContentTypeTest");
    invoke();
  }

  /**
   * @testName: resourceGetInputStreamTest
   * @assertion_ids: JSF:JAVADOC:266; JSF:JAVADOC:259
   * @test_Strategy: Validate That an InputStream is returned if the request is
   *                 a resource request.
   * 
   * @since 2.0
   */
  public void resourceGetInputStreamTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "resourceGetInputStreamTest");
    invoke();
  }

  /**
   * @testName: resourceGetLibraryNameTest
   * @assertion_ids: JSF:JAVADOC:266; JSF:JAVADOC:261
   * @test_Strategy: Validate that the correct Library name is returned when the
   *                 following have been set on the resource.
   * 
   *                 -resourceName -libraryName
   * 
   * @since 2.0
   */
  public void resourceGetLibraryNameTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "resourceGetLibraryNameTest");
    invoke();
  }

  /**
   * @testName: resourceSetContentTypeTest
   * @assertion_ids: JSF:JAVADOC:266; JSF:JAVADOC:267
   * @test_Strategy: Validate that the correct MIME type gets set.
   * 
   * @since 2.0
   */
  public void resourceSetContentTypeTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "resourceSetContentTypeTest");
    invoke();
  }

  /**
   * @testName: resourceSetLibraryNameTest
   * @assertion_ids: JSF:JAVADOC:266; JSF:JAVADOC:268
   * @test_Strategy: Validate that the correct Library name is returned when the
   *                 following have been set on the resource. And the name has
   *                 been reset.
   * 
   *                 -resourceName -libraryName
   * 
   * @since 2.0
   */
  public void resourceSetLibraryNameTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "resourceSetLibraryNameTest");
    invoke();
  }

  /**
   * @testName: resourceGetLibraryNameNullTest
   * @assertion_ids: JSF:JAVADOC:266; JSF:JAVADOC:261
   * @test_Strategy: Validate that the correct Library name is returned when the
   *                 following have been set on resource.
   * 
   *                 -resourceName -libraryName(set to null)
   * 
   * @since 2.0
   */
  public void resourceGetLibraryNameNullTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "resourceGetLibraryNameNullTest");
    invoke();
  }

  /**
   * @testName: resourceGetRequestPathTest
   * @assertion_ids: JSF:JAVADOC:266; JSF:JAVADOC:262
   * @test_Strategy: Validate that the correct resource path is returned.
   * 
   * @since 2.0
   */
  public void resourceGetRequestPathTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "resourceGetRequestPathTest");
    invoke();
  }

  /**
   * @testName: resourceGetRequestPathLibTest
   * @assertion_ids: JSF:JAVADOC:266; JSF:JAVADOC:262
   * @test_Strategy: Validate that the correct resource path is returned when a
   *                 libraryName is given during the creation of the resource.
   * 
   * @since 2.0
   */
  public void resourceGetRequestPathLibTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "resourceGetRequestPathLibTest");
    invoke();
  }

  /**
   * @testName: resourceGetURLTest
   * @assertion_ids: JSF:JAVADOC:266; JSF:JAVADOC:265
   * @test_Strategy: Validate that the correct URL is returned.
   * 
   * @since 2.0
   */
  public void resourceGetURLTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "resourceGetURLTest");
    invoke();
  }

  /**
   * @testName: resourceToStringTest
   * @assertion_ids: JSF:JAVADOC:266; JSF:JAVADOC:271
   * @test_Strategy: Validate that the correct String represention is returned.
   * 
   * @since 2.0
   */
  public void resourceToStringTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "resourceToStringTest");
    invoke();
  }

  /**
   * @testName: resourceUserAgentNeedsUpdateTrueTest
   * @assertion_ids: JSF:JAVADOC:266; JSF:JAVADOC:272
   * @test_Strategy: Validate that the user-agent requesting the resource needs
   *                 an update. (Return true)
   * 
   * @since 2.0
   */
  public void resourceUserAgentNeedsUpdateTrueTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "If-Modified-Since: Sat, 29 Oct 1994 19:43:31 GMT");
    TEST_PROPS.setProperty(APITEST, "resourceUserAgentNeedsUpdateTrueTest");
    invoke();
  }

  /**
   * @testName: resourceGetResponseHeadersTest
   * @assertion_ids: JSF:JAVADOC:266; JSF:JAVADOC:264
   * @test_Strategy: Validate that a Map Object is returned.
   * 
   * @since 2.0
   */
  public void resourceGetResponseHeadersTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "resourceGetResponseHeadersTest");
    invoke();
  }

  /**
   * @testName: resourceGetResourceNameTest
   * @assertion_ids: JSF:JAVADOC:266; JSF:JAVADOC:263
   * @test_Strategy: Validate that the correct Resource Name is returned.
   * 
   * @since 2.0
   */
  public void resourceGetResourceNameTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "resourceGetResourceNameTest");
    invoke();
  }

  /**
   * @testName: resourceSetResourceNameTest
   * @assertion_ids: JSF:JAVADOC:266; JSF:JAVADOC:269
   * @test_Strategy: Validate that the correct Resource Name is returned after
   *                 setting it.
   * 
   * @since 2.0
   */
  public void resourceSetResourceNameTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "resourceSetResourceNameTest");
    invoke();
  }

  /**
   * @testName: resourceSetResourceNameNPETest
   * @assertion_ids: JSF:JAVADOC:266; JSF:JAVADOC:270
   * @test_Strategy: Validate when a null is passed in, a NullPointerException
   *                 is thrown.
   * 
   * @since 2.0
   */
  public void resourceSetResourceNameNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "resourceSetResourceNameNPETest");
    invoke();
  }

  //
  // TODO: Need to add getInputStreamIOETest...
  //
} // end of URLClient
