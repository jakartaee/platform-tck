/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.jsf.api.javax_faces.context.facescontextwrapper;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

import java.io.PrintWriter;

public final class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_ctx_facesctxwrap_web";

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
   * @testName: facesCtxWrapperAddGetMessagesTest
   * @assertion_ids: JSF:JAVADOC:1347; JSF:JAVADOC:1346; JSF:JAVADOC:1358;
   *                 JSF:JAVADOC:1358
   * @test_Strategy: Verify the default behavior of the following is to call
   *                 through to FacesContext...
   *                 FacesContextWrapper.addMessage(UIComponent, FacesMessage),
   *                 FacesContextWrapper.getMessage(),
   *                 FacesContextWrapper.getMessages(UIComponent).
   * 
   *                 Add four messages to the FacesContext, two of them have a
   *                 client ID and the others have no client ID. - call
   *                 getMessages(clientID) - ensure the expected 2 messages are
   *                 returned. - call getMessages(null) - ensure the expected 2
   *                 messages are returned. - call getMessages() - ensure the
   *                 expected 4 messages are returned.
   * 
   *                 This will additionally ensure that the messages returned by
   *                 the implementations Iterator preserves the order in which
   *                 the messages were added.
   */
  public void facesCtxWrapperAddGetMessagesTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxWrapperAddGetMessagesTest");
    invoke();
  }

  /**
   * @testName: facesCtxWrapperGetClientIdsWithMessagesEmptyTest
   * @assertion_ids: JSF:JAVADOC:1347; JSF:JAVADOC:1350
   * @test_Strategy: Ensure an empty Iterator is returned if there are no
   *                 messages queued. Additionally, if there are messages with
   *                 no particular client ID, the iterator will contain a null
   *                 value.
   */
  public void facesCtxWrapperGetClientIdsWithMessagesEmptyTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "facesCtxWrapperGetClientIdsWithMessagesEmptyTest");
    invoke();
  }

  /**
   * @testName: facesCtxWrapperGetClientIdsWithMessagesTest
   * @assertion_ids: JSF:JAVADOC:1347; JSF:JAVADOC:1350
   * @test_Strategy: Ensure getClientIdsWithMessages returns the expected values
   *                 based on messages added to the faces context that may or
   *                 may not have an ID.
   */
  public void facesCtxWrapperGetClientIdsWithMessagesTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "facesCtxWrapperGetClientIdsWithMessagesTest");
    invoke();
  }

  /**
   * @testName: facesCtxWrapperGetMessagesEmptyTest
   * @assertion_ids: JSF:JAVADOC:1347; JSF:JAVADOC:1358; JSF:JAVADOC:1359
   * @test_Strategy: Ensure both getMessages(UIComponent) and getMessages()
   *                 returns an empty Iterator if no messages are queued within
   *                 the FacesContext.
   */
  public void facesCtxWrapperGetMessagesEmptyTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxWrapperGetMessagesEmptyTest");
    invoke();
  }

  /**
   * @testName: facesCtxWrapperGetApplicationTest
   * @assertion_ids: JSF:JAVADOC:1347; JSF:JAVADOC:1348
   * @test_Strategy: Ensure the Application instance returned by
   *                 FacesContestWrapper.getApplication() is the same instance
   *                 as that returned by Application.getCurrentInstance().
   */
  public void facesCtxWrapperGetApplicationTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxWrapperGetApplicationTest");
    invoke();
  }

  /**
   * @testName: facesCtxWrapperSetExceptionHandlerTest
   * @assertion_ids: JSF:JAVADOC:1347; JSF:JAVADOC:1376
   * @test_Strategy: Ensure the Application instance returned by
   *                 FacesContestWrapper.getApplication() is the same instance
   *                 as that returned by Application.getCurrentInstance().
   */
  public void facesCtxWrapperSetExceptionHandlerTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxWrapperSetExceptionHandlerTest");
    invoke();
  }

  /**
   * @testName: facesCtxWrapperGetMaximumSeverityTest
   * @assertion_ids: JSF:JAVADOC:1347; JSF:JAVADOC:1355
   * @test_Strategy: Validate the severity hierarchy of Messages stored in the
   *                 FacesContext instance. First, if there are no messages
   *                 getMaximumSeverity() must return null. Then add a
   *                 SEVERITY_INFO message and ensure getMaximumSeverity()
   *                 returns the expected message. Add SEVERITY_WARN, and ensure
   *                 that the SEVERITY_WARN message is returned by
   *                 getMaximumSeverity(). Add SEVERITY_ERROR and ensure
   *                 getMaximumSeverity() returns the SEVERITY_ERROR message.
   *                 Finally, add a SEVERITY_FATAL message to the context and
   *                 ensure SEVERITY_FATAL message is returned by
   *                 getMaximumSeverity().
   * 
   */
  public void facesCtxWrapperGetMaximumSeverityTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxWrapperGetMaximumSeverityTest");
    invoke();
  }

  /**
   * @testName: facesCtxWrapperGetResponseCompleteTest
   * @assertion_ids: JSF:JAVADOC:1347; JSF:JAVADOC:1363; JSF:JAVADOC:1373
   * @test_Strategy: Ensure getResponseComplete() returns false on a new
   *                 FacesContext instance. Call responseComplete() and ensure
   *                 getResponseComplete() returns true.
   */
  public void facesCtxWrapperGetResponseCompleteTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxWrapperGetResponseCompleteTest");
    invoke();
  }

  /**
   * @testName: facesCtxWrapperRenderResponseTest
   * @assertion_ids: JSF:JAVADOC:1347; JSF:JAVADOC:1373; JSF:JAVADOC:1362
   * @test_Strategy: Ensure getRenderResponse() returns false on a new
   *                 FacesContext instance. Call renderResponse() and ensure
   *                 getRenderResponse() returns true.
   */
  public void facesCtxWrapperRenderResponseTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxWrapperRenderResponseTest");
    invoke();
  }

  /**
   * @testName: facesCtxWrapperSetGetResponseWriterTest
   * @assertion_ids: JSF:JAVADOC:1347; JSF:JAVADOC:1365; JSF:JAVADOC:1379
   * @test_Strategy: Ensure the Writer set by setResponseWriter() is returned by
   *                 getResponseWriter().
   */
  public void facesCtxWrapperSetGetResponseWriterTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxWrapperSetGetResponseWriterTest");
    invoke();
  }

  /**
   * @testName: facesCtxWrapperSetGetCurrentPhaseIdTest
   * @assertion_ids: JSF:JAVADOC:1347; JSF:JAVADOC:1351; JSF:JAVADOC:1375
   * @test_Strategy: Validate that we can can set and retrieve the current
   *                 PhaseId.
   * 
   * @since 2.0
   */
  public void facesCtxWrapperSetGetCurrentPhaseIdTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxWrapperSetGetCurrentPhaseIdTest");
    invoke();
  }

  /**
   * @testName: facesCtxWrapperSetGetResponseStreamTest
   * @assertion_ids: JSF:JAVADOC:1347; JSF:JAVADOC:1364; JSF:JAVADOC:1378
   * @test_Strategy: Ensure the Stream set by setResponseStream() is returned by
   *                 getResponseStream().
   */
  public void facesCtxWrapperSetGetResponseStreamTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxWrapperSetGetResponseStreamTest");
    invoke();
  }

  /**
   * @testName: facesCtxWrapperGetViewRootTest
   * @assertion_ids: JSF:JAVADOC:1347; JSF:JAVADOC:1366
   * @test_Strategy: Ensure the UIViewRoot set by setViewRoot() is properly
   *                 returned by getViewRoot().
   */
  public void facesCtxWrapperGetViewRootTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxWrapperGetViewRootTest");
    invoke();
  }

  /**
   * @testName: facesCtxWrapperGetELContextTest
   * @assertion_ids: JSF:JAVADOC:1347; JSF:JAVADOC:1352
   * @test_Strategy: Ensure we get a non-null return when calling this method.
   * 
   * @since 1.2
   */
  public void facesCtxWrapperGetELContextTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxWrapperGetELContextTest");
    invoke();
  } // END facesCtxWrapperGetELContextTest

  /**
   * @testName: facesCtxWrapperGetAttributesEmptyTest
   * @assertion_ids: JSF:JAVADOC:1347; JSF:JAVADOC:1349
   * @test_Strategy: Validate that a the Attribute map is empty after
   *                 FacesContextWrapper.release() is called.
   * 
   * @since 2.0
   */
  public void facesCtxWrapperGetAttributesEmptyTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxWrapperGetAttributesEmptyTest");
    invoke();
  }

  /**
   * @testName: facesCtxWrapperGetAttributesTest
   * @assertion_ids: JSF:JAVADOC:1347; JSF:JAVADOC:1349
   * @test_Strategy: Validate that a returned mutable Map is returned.
   * 
   * @since 2.0
   */
  public void facesCtxWrapperGetAttributesTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxWrapperGetAttributesTest");
    invoke();
  }

  /**
   * @testName: facesCtxWrapperGetPartialViewContextTest
   * @assertion_ids: JSF:JAVADOC:1347; JSF:JAVADOC:1360
   * @test_Strategy: Validate that a PartialViewContext is returned for this
   *                 request. This method must return a new PartialViewContext
   *                 if one does not already exist.
   * 
   * @since 2.0
   */
  public void facesCtxWrapperGetPartialViewContextTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxWrapperGetPartialViewContextTest");
    invoke();
  }

  /**
   * @testName: facesCtxWrapperGetExceptionHandlerTest
   * @assertion_ids: JSF:JAVADOC:1347; JSF:JAVADOC:1353
   * @test_Strategy: Validate that a ExceptionHandler is returned for this
   *                 request.
   * 
   * @since 2.0
   */
  public void facesCtxWrapperGetExceptionHandlerTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxWrapperGetExceptionHandlerTest");
    invoke();
  }

  /**
   * @testName: facesCtxWrapperGetExternalContextTest
   * @assertion_ids: JSF:JAVADOC:1347; JSF:JAVADOC:1354
   * @test_Strategy: Validate that we get an instance of ExternalContext.
   * 
   * @since 2.0
   */
  public void facesCtxWrapperGetExternalContextTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxWrapperGetExternalContextTest");
    invoke();
  }

  /**
   * @testName: facesCtxWrapperGetMessageListTest
   * @assertion_ids: JSF:JAVADOC:1347; JSF:JAVADOC:1356; JSF:JAVADOC:1357
   * @test_Strategy: Validate that we get the correct number of messages from
   *                 the context.
   * 
   * @since 2.0
   */
  public void facesCtxWrapperGetMessageListTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxWrapperGetMessageListTest");
    invoke();
  }

  /**
   * @testName: facesCtxWrapperGetMessageListByIdTest
   * @assertion_ids: JSF:JAVADOC:1347; JSF:JAVADOC:1359
   * @test_Strategy: Validate that we get the correct number of messages from
   *                 the context when passing in with UIComponent ID.
   * @since 2.0
   */
  public void facesCtxWrapperGetMessageListByIdTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxWrapperGetMessageListByIdTest");
    invoke();
  }

  /**
   * @testName: facesCtxWrapperIsValidationFailedTest
   * @assertion_ids: JSF:JAVADOC:1374; JSF:JAVADOC:1371
   * @test_Strategy: Validate that: - if a call to isValidationFailed comes
   *                 before a call to validationFailed that 'false' is returned.
   *                 - if a call to isValidationFailed comes after a call to
   *                 validationFailed that 'true' is returned.
   * 
   * @since 2.0
   */
  public void facesCtxWrapperIsValidationFailedTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxWrapperIsValidationFailedTest");
    invoke();
  }

  /**
   * @testName: facesCtxWrapperIsPostbackTest
   * @assertion_ids: JSF:JAVADOC:1347; JSF:JAVADOC:1368
   * @test_Strategy: Validate that we get the correct number of messages from
   *                 the context.
   * @since 2.0
   */
  public void facesCtxWrapperIsPostbackTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxWrapperIsPostbackTest");
    invoke();
  }

  /**
   * @testName: facesCtxWrapperIsReleasedTest
   * @assertion_ids: JSF:JAVADOC:1347; JSF:JAVADOC:1372; JSF:JAVADOC:2590
   * @test_Strategy: Validate that if the resources associated with this
   *                 FacesContext instance have been released.
   * 
   * @since 2.1
   */
  public void facesCtxWrapperIsReleasedTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxWrapperIsReleasedTest");
    invoke();
  }

  /**
   * @testName: facesCtxWrapperGetRenderKitTest
   * @assertion_ids: JSF:JAVADOC:1347; JSF:JAVADOC:1361
   * @test_Strategy: Validate that FacesContextWrapper.getRenderKit() returns
   *                 the current RenderKit that has been set for the current
   *                 UIViewRoot.
   * 
   * @since 1.2
   */
  public void facesCtxWrapperGetRenderKitTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxWrapperGetRenderKitTest");
    invoke();
  }

  /**
   * @testName: facesCtxWrapperIsGetProcessingEventTest
   * @assertion_ids: JSF:JAVADOC:1369; JSF:JAVADOC:1377
   * @test_Strategy: Validate that we can set the processing event flag and we
   *                 we get the correct value returned.
   * 
   * @since 1.2
   */
  public void facesCtxWrapperIsGetProcessingEventTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxWrapperIsGetProcessingEventTest");
    invoke();
  }

} // end of URLClient
