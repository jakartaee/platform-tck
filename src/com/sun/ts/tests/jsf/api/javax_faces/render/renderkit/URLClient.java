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

package com.sun.ts.tests.jsf.api.javax_faces.render.renderkit;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

public final class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_render_renderkit_web";

  public static void main(String[] args) {
    URLClient theTests = new URLClient();
    Status s = theTests.run(args, new PrintWriter(System.out, true),
        new PrintWriter(System.err, true));
    s.exit();
  }

  public Status run(String[] args, PrintWriter out, PrintWriter err) {
    setContextRoot(CONTEXT_ROOT);
    setServletName(DEFAULT_SERVLET_NAME);
    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Test Declarations */

  /**
   * @testName: renderKitAddGetRendererTest
   * @assertion_ids: JSF:JAVADOC:2092; JSF:JAVADOC:2077; JSF:JAVADOC:2084
   * @test_Strategy: Ensure that any Renderer added to the RenderKit can be
   *                 obtained again via getRenderer(). Also validate null is
   *                 returned if an ID is provided that hasn't been previously
   *                 registered.
   */
  public void renderKitAddGetRendererTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "renderKitAddGetRendererTest");
    invoke();
  }

  /**
   * @testName: renderKitGetRendererTypesTest
   * @assertion_ids: JSF:JAVADOC:2092; JSF:JAVADOC:2090
   * @test_Strategy: Ensure that after we add a Renderer to the default
   *                 RenderKit, that we get back that the correct type when
   *                 calling getRendererTypes.
   */
  public void renderKitGetRendererTypesTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "renderKitGetRendererTypesTest");
    invoke();
  }

  /**
   * @testName: renderKitAddGetClientBehaviorRendererTest
   * @assertion_ids: JSF:JAVADOC:2092; JSF:JAVADOC:2079; JSF:JAVADOC:2088
   * @test_Strategy: Ensure that a ClientBehaviorRenderer added to the RenderKit
   *                 can be obtained again via getClientBehaviorRenderer(). Also
   *                 validate null is returned if an ID is provided that hasn't
   *                 been previously registered.
   */
  public void renderKitAddGetClientBehaviorRendererTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "renderKitAddGetClientBehaviorRendererTest");
    invoke();
  }

  /**
   * @testName: renderKitAddRendererNPETest
   * @assertion_ids: JSF:JAVADOC:2092; JSF:JAVADOC:2080
   * @test_Strategy: Ensure NPE is thrown if either of the arguments to
   *                 addRenderer() are null.
   */
  public void renderKitAddRendererNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "renderKitAddRendererNPETest");
    invoke();
  }

  /**
   * @testName: renderKitGetRendererNPETest
   * @assertion_ids: JSF:JAVADOC:2092; JSF:JAVADOC:2089
   * @test_Strategy: Ensure NPE is thrown if the argument passed to
   *                 getRenderer() is null.
   */
  public void renderKitGetRendererNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "renderKitGetRendererNPETest");
    invoke();
  }

  /**
   * @testName: renderKitCreateResponseStreamTest
   * @assertion_ids: JSF:JAVADOC:2092; JSF:JAVADOC:2081
   * @test_Strategy: Ensure a ResponseStream can be successfully created and
   *                 used.
   */
  public void renderKitCreateResponseStreamTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "renderKitCreateResponseStreamTest");
    invoke();
  }

  /**
   * @testName: renderKitCreateResponseWriterTest
   * @assertion_ids: JSF:JAVADOC:2092; JSF:JAVADOC:2082
   * @test_Strategy: Ensure a ResponseWriter, providing a valid content type and
   *                 encoding can be successfully created and used.
   */
  public void renderKitCreateResponseWriterTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "renderKitCreateResponseWriterTest");
    invoke();
  }

  /**
   * @testName: renderKitCreateResponseWriterInvalidContentTypeTest
   * @assertion_ids: JSF:JAVADOC:2092; JSF:JAVADOC:2083
   * @test_Strategy: Ensure an IllegalArgumentException is thrown if an invalid
   *                 content type is passed to createResponseWriter().
   */
  public void renderKitCreateResponseWriterInvalidContentTypeTest()
      throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "renderKitCreateResponseWriterInvalidContentTypeTest");
    invoke();
  }

  /**
   * @testName: renderKitCreateResponseWriterInvalidEncodingTest
   * @assertion_ids: JSF:JAVADOC:2092; JSF:JAVADOC:2083
   * @test_Strategy: Ensure an IllegalArgumentException is thrown if an invalid
   *                 encoding is passed to createResponseWriter().
   */
  public void renderKitCreateResponseWriterInvalidEncodingTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "renderKitCreateResponseWriterInvalidEncodingTest");
    invoke();
  }

  /**
   * @testName: renderKitGetResponseStateManagerTest
   * @assertion_ids: JSF:JAVADOC:2092; JSF:JAVADOC:2091
   * @test_Strategy: Ensure a non-null result from calling
   *                 getResponseStateManger().
   */
  public void renderKitGetResponseStateManagerTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "renderKitGetResponseStateManagerTest");
    invoke();
  }

  /**
   * @testName: renderKitAddClientBehaviorRendererNPETest
   * @assertion_ids: JSF:JAVADOC:2092; JSF:JAVADOC:2078
   * @test_Strategy: Ensure NPE is thrown if either of the arguments to
   *                 addRenderer() are null.
   */
  public void renderKitAddClientBehaviorRendererNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "renderKitAddClientBehaviorRendererNPETest");
    invoke();
  }

  /**
   * @testName: renderKitGetClientBehaviorRendererNPETest
   * @assertion_ids: JSF:JAVADOC:2092; JSF:JAVADOC:2085
   * @test_Strategy: Ensure NPE is thrown if either of the arguments to
   *                 addRenderer() are null.
   */
  public void renderKitGetClientBehaviorRendererNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "renderKitGetClientBehaviorRendererNPETest");
    invoke();
  }

  /**
   * @testName: renderKitGetClientBehaviorRendererTypesTest
   * @assertion_ids: JSF:JAVADOC:2092; JSF:JAVADOC:2086
   * @test_Strategy: Ensure that after we add a ClientBehaviorRenderer to the
   *                 default RenderKit, that we get back that the correct type
   *                 when calling getClientBehaviorRendererTypes.
   */
  public void renderKitGetClientBehaviorRendererTypesTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "renderKitGetClientBehaviorRendererTypesTest");
    invoke();
  }

} // end of URLClient
