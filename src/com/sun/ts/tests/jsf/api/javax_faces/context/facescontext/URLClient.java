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
package com.sun.ts.tests.jsf.api.javax_faces.context.facescontext;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

import java.io.PrintWriter;

public final class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_ctx_facesctx_web";

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
   * @testName: facesCtxAddGetMessagesTest
   * @assertion_ids: JSF:JAVADOC:1270; JSF:JAVADOC:1280; JSF:JAVADOC:1294;
   *                 JSF:JAVADOC:1273
   * @test_Strategy: Verify the behavior of addMessage(UIComponent,
   *                 FacesMessage), FacesContext.getMessage(), and
   *                 FacesContext.getMessages(UIComponent). Add four messages to
   *                 the FacesContext, two of them have a client ID and the
   *                 others have no client ID. - call getMessages(clientID) -
   *                 ensure the expected 2 messages are returned. - call
   *                 getMessages(null) - ensure the expected 2 messages are
   *                 returned. - call getMessages() - ensure the expected 4
   *                 messages are returned. This will additionally ensure that
   *                 the messages returned by the implementations Iterator
   *                 preserves the order in which the messages were added.
   */
  public void facesCtxAddGetMessagesTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxAddGetMessagesTest");
    invoke();
  }

  /**
   * @testName: facesCtxGetClientIdsWithMessagesEmptyTest
   * @assertion_ids: JSF:JAVADOC:1273; JSF:JAVADOC:1280; JSF:JAVADOC:1278
   * @test_Strategy: Ensure an empty Iterator is returned if there are no
   *                 messages queued. Additionally, if there are messages with
   *                 no particular client ID, the iterator will contain a null
   *                 value.
   */
  public void facesCtxGetClientIdsWithMessagesEmptyTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "facesCtxGetClientIdsWithMessagesEmptyTest");
    invoke();
  }

  /**
   * @testName: facesCtxGetClientIdsWithMessagesTest
   * @assertion_ids: JSF:JAVADOC:1273; JSF:JAVADOC:1280; JSF:JAVADOC:1278;
   *                 JSF:JAVADOC:1296
   * @test_Strategy: Ensure getClientIdsWithMessages returns the expected values
   *                 based on messages added to the faces context that may or
   *                 may not have an ID.
   */
  public void facesCtxGetClientIdsWithMessagesTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxGetClientIdsWithMessagesTest");
    invoke();
  }

  /**
   * @testName: facesCtxGetMessagesEmptyTest
   * @assertion_ids: JSF:JAVADOC:1273; JSF:JAVADOC:1280; JSF:JAVADOC:1294
   * @test_Strategy: Ensure both getMessages(UIComponent) and getMessages()
   *                 returns an empty Iterator if no messages are queued within
   *                 the FacesContext.
   */
  public void facesCtxGetMessagesEmptyTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxGetMessagesEmptyTest");
    invoke();
  }

  /**
   * @testName: facesCtxGetApplicationTest
   * @assertion_ids: JSF:JAVADOC:1273; JSF:JAVADOC:1274
   * @test_Strategy: Ensure the Application instance returned by
   *                 getApplication() is the same instance as that returned by
   *                 Application.getCurrentInstance().
   */
  public void facesCtxGetApplicationTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxGetApplicationTest");
    invoke();
  }

  /**
   * @testName: facesCtxAddMessageNPETest
   * @assertion_ids: JSF:JAVADOC:1273; JSF:JAVADOC:1272
   * @test_Strategy: Ensure a NullPointerException is thrown if the FacesContext
   *                 argument provided to addMessage(String, FacesContext) is
   *                 null.
   */
  public void facesCtxAddMessageNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxAddMessageNPETest");
    invoke();
  }

  /**
   * @testName: facesCtxIsProjectStageNPETest
   * @assertion_ids: JSF:JAVADOC:1273; JSF:JAVADOC:1317
   * @test_Strategy: Ensure a NullPointerException is thrown if the
   *                 FacesContext.isPrejectStage(null) is called.
   * 
   * @since 2.0
   */
  public void facesCtxIsProjectStageNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxIsProjectStageNPETest");
    invoke();
  }

  /**
   * @testName: facesCtxSetGetCurrentInstanceTest
   * @assertion_ids: JSF:JAVADOC:1280
   * @test_Strategy: Ensure the FacesContext instance can be set and returned by
   *                 calling {set,get}CurrentInstance.
   */
  public void facesCtxSetGetCurrentInstanceTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxSetGetCurrentInstanceTest");
    invoke();
  }

  /**
   * @testName: facesCtxGetMaximumSeverityTest
   * @assertion_ids: JSF:JAVADOC:1273; JSF:JAVADOC:1288
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
   */
  public void facesCtxGetMaximumSeverityTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxGetMaximumSeverityTest");
    invoke();
  }

  /**
   * @testName: facesCtxResponseCompleteTest
   * @assertion_ids: JSF:JAVADOC:1273; JSF:JAVADOC:1324
   * @test_Strategy: Ensure getResponseComplete() returns false on a new
   *                 FacesContext instance. Call responseComplete() and ensure
   *                 getResponseComplete() returns true.
   */
  public void facesCtxResponseCompleteTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxResponseCompleteTest");
    invoke();
  }

  /**
   * @testName: facexCtxRenderResponseTest
   * @assertion_ids: JSF:JAVADOC:1273; JSF:JAVADOC:1322
   * @test_Strategy: Ensure getRenderResponse() returns false on a new
   *                 FacesContext instance. Call renderResponse() and ensure
   *                 getRenderResponse() returns true.
   */
  public void facexCtxRenderResponseTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facexCtxRenderResponseTest");
    invoke();
  }

  /**
   * @testName: facesCtxSetGetResponseWriterTest
   * @assertion_ids: JSF:JAVADOC:1273; JSF:JAVADOC:1380; JSF:JAVADOC:1333
   * @test_Strategy: Ensure the Writer set by setResponseWriter() is returned by
   *                 getResponseWriter().
   */
  public void facesCtxSetGetResponseWriterTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxSetGetResponseWriterTest");
    invoke();
  }

  /**
   * @testName: facesCtxSetGetResponseStreamTest
   * @assertion_ids: JSF:JAVADOC:1273; JSF:JAVADOC:1306; JSF:JAVADOC:1330
   * @test_Strategy: Ensure the Stream set by setResponseStream() is returned by
   *                 getResponseStream().
   */
  public void facesCtxSetGetResponseStreamTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxSetGetResponseStreamTest");
    invoke();
  }

  /**
   * @testName: facesCtxSetResponseStreamNPETest
   * @assertion_ids: JSF:JAVADOC:1273; JSF:JAVADOC:1331
   * @test_Strategy: Validate a NullPointerException is thrown when response
   *                 stream is null
   */
  public void facesCtxSetResponseStreamNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxSetResponseStreamNPETest");
    invoke();
  }

  /**
   * @testName: facesCtxSetResponseWriterNPETest
   * @assertion_ids: JSF:JAVADOC:1273; JSF:JAVADOC:1335
   * @test_Strategy: Validate a NullPointerException is thrown when response
   *                 stream is null
   */
  public void facesCtxSetResponseWriterNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxSetResponseWriterNPETest");
    invoke();
  }

  /**
   * @testName: facesCtxSetGetViewRootTest
   * @assertion_ids: JSF:JAVADOC:1273; JSF:JAVADOC:1310; JSF:JAVADOC:1336
   * @test_Strategy: Ensure the UIViewRoot set by setViewRoot() is properly
   *                 returned by getViewRoot().
   */
  public void facesCtxSetGetViewRootTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxSetGetViewRootTest");
    invoke();
  }

  /**
   * @testName: facesCtxSetViewRootNPETest
   * @assertion_ids: JSF:JAVADOC:1273; JSF:JAVADOC:1338
   * @test_Strategy: Ensure an NPE is thrown if a null argument is passed to
   *                 FacesContext.setViewRoot();
   * 
   * @since 1.2
   */
  public void facesCtxSetViewRootNPETest() throws Fault {

    TEST_PROPS.setProperty(APITEST, "facesCtxSetViewRootNPETest");
    invoke();

  } // END facesCtxSetViewRootNPETest

  /**
   * @testName: facesCtxSetGetCurrentPhaseIdTest
   * @assertion_ids: JSF:JAVADOC:1273; JSF:JAVADOC:1281; JSF:JAVADOC:1326
   * @test_Strategy: Validate that we can can set and retrieve the current
   *                 PhaseId.
   * 
   * @since 2.0
   */
  public void facesCtxSetGetCurrentPhaseIdTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxSetGetCurrentPhaseIdTest");
    invoke();
  }

  /**
   * @testName: facesCtxISEAfterReleaseTest
   * @assertion_ids: JSF:JAVADOC:1273; JSF:JAVADOC:1337; JSF:JAVADOC:1271;
   *                 JSF:JAVADOC:1275; JSF:JAVADOC:1279; JSF:JAVADOC:1282;
   *                 JSF:JAVADOC:1284; JSF:JAVADOC:1287; JSF:JAVADOC:1289;
   *                 JSF:JAVADOC:1291; JSF:JAVADOC:1293; JSF:JAVADOC:1295;
   *                 JSF:JAVADOC:1297; JSF:JAVADOC:1299; JSF:JAVADOC:1301;
   *                 JSF:JAVADOC:1303; JSF:JAVADOC:1305; JSF:JAVADOC:1307;
   *                 JSF:JAVADOC:1309; JSF:JAVADOC:1311; JSF:JAVADOC:1313;
   *                 JSF:JAVADOC:1316; JSF:JAVADOC:1319; JSF:JAVADOC:1321;
   *                 JSF:JAVADOC:1323; JSF:JAVADOC:1325; JSF:JAVADOC:1327;
   *                 JSF:JAVADOC:1332; JSF:JAVADOC:1334; JSF:JAVADOC:1337;
   *                 JSF:JAVADOC:1340; JSF:JAVADOC:1277; JSF:JAVADOC:2720
   * @test_Strategy: Ensure all methods, with the exception of the following
   *                 throw an IllegalStateException if called after
   *                 FacesContext.release() has been called.
   * 
   *                 Excluding the following methods: release() isReleased()
   *                 setCurrentInstance() getCurrentInstance()
   *                 getCurrentPhaseId() setExceptionHandler()
   *                 getExceptionHandler() setProcessingEvents()
   *                 isProcessingEvents()
   */
  public void facesCtxISEAfterReleaseTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxISEAfterReleaseTest");
    invoke();
  }

  /**
   * @testName: facesCtxGetELContextTest
   * @assertion_ids: JSF:JAVADOC:1273; JSF:JAVADOC:1283
   * @test_Strategy: Ensure we get a non-null return when calling this method.
   * 
   * @since 1.2
   */
  public void facesCtxGetELContextTest() throws Fault {

    TEST_PROPS.setProperty(APITEST, "facesCtxGetELContextTest");
    invoke();

  } // END facesCtxGetELContextTest

  /**
   * @testName: facesCtxGetAttributesEmptyTest
   * @assertion_ids: JSF:JAVADOC:1273; JSF:JAVADOC:1276
   * @test_Strategy: Validate that a the Attribute map is empty after
   *                 FacesContext.release() is called.
   * 
   * @since 2.0
   */
  public void facesCtxGetAttributesEmptyTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxGetAttributesEmptyTest");
    invoke();
  }

  /**
   * @testName: facesCtxGetAttributesTest
   * @assertion_ids: JSF:JAVADOC:1273; JSF:JAVADOC:1276
   * @test_Strategy: Validate that a returned mutable Map is returned.
   * 
   * @since 2.0
   */
  public void facesCtxGetAttributesTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxGetAttributesTest");
    invoke();
  }

  /**
   * @testName: facesCtxGetPartialViewContextTest
   * @assertion_ids: JSF:JAVADOC:1273; JSF:JAVADOC:1298
   * @test_Strategy: Validate that a PartialViewContext is returned for this
   *                 request. This method must return a new PartialViewContext
   *                 if one does not already exist.
   * 
   * @since 2.0
   */
  public void facesCtxGetPartialViewContextTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxGetPartialViewContextTest");
    invoke();
  }

  /**
   * @testName: facesCtxSetGetExceptionHandlerTest
   * @assertion_ids: JSF:JAVADOC:1273; JSF:JAVADOC:1285; JSF:JAVADOC:1328
   * @test_Strategy: Validate that a ExceptionHandler is returned for this
   *                 request.
   * @since 2.0
   */
  public void facesCtxSetGetExceptionHandlerTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxSetGetExceptionHandlerTest");
    invoke();
  }

  /**
   * @testName: facesCtxGetExternalContextTest
   * @assertion_ids: JSF:JAVADOC:1273; JSF:JAVADOC:1286
   * @test_Strategy: Validate that we get an instance of ExternalContext.
   * @since 2.0
   */
  public void facesCtxGetExternalContextTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxGetExternalContextTest");
    invoke();
  }

  /**
   * @testName: facesCtxGetMessageListTest
   * @assertion_ids: JSF:JAVADOC:1273; JSF:JAVADOC:1290
   * @test_Strategy: Validate that we get the correct number of messages from
   *                 the context.
   * @since 2.0
   */
  public void facesCtxGetMessageListTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxGetMessageListTest");
    invoke();
  }

  /**
   * @testName: facesCtxGetMessageListByIdTest
   * @assertion_ids: JSF:JAVADOC:1273; JSF:JAVADOC:1292
   * @test_Strategy: Validate that we get the correct number of messages from
   *                 the context when passing in with UIComponent ID.
   * @since 2.0
   */
  public void facesCtxGetMessageListByIdTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxGetMessageListByIdTest");
    invoke();
  }

  /**
   * @testName: facesCtxisValidationFailedTest
   * @assertion_ids: JSF:JAVADOC:1273; JSF:JAVADOC:1318
   * @test_Strategy: Validate that: - if a call to isValidationFailed comes
   *                 before a call to validationFailed that 'false' is returned.
   *                 - if a call to isValidationFailed comes after a call to
   *                 validationFailed that 'true' is returned.
   * 
   * @since 2.0
   */
  public void facesCtxisValidationFailedTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxisValidationFailedTest");
    invoke();
  }

  /**
   * @testName: facesCtxisPostbackTest
   * @assertion_ids: JSF:JAVADOC:1273; JSF:JAVADOC:1312
   * @test_Strategy: Validate that we get the correct number of messages from
   *                 the context.
   * @since 2.0
   */
  public void facesCtxisPostbackTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxisPostbackTest");
    invoke();
  }

  /**
   * @testName: facesCtxisReleasedTest
   * @assertion_ids: JSF:JAVADOC:2589; JSF:JAVADOC:1320
   * @test_Strategy: Validate that if the resources associated with this
   *                 FacesContext instance have been released.
   * 
   * @since 2.1
   */
  public void facesCtxisReleasedTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxisReleasedTest");
    invoke();
  }

  /**
   * @testName: facesCtxGetRenderKitTest
   * @assertion_ids: JSF:JAVADOC:2589; JSF:JAVADOC:1300
   * @test_Strategy: Validate that FacesContext.getRenderKit() returns the
   *                 current RenderKit that has been set for the current
   *                 UIViewRoot.
   * 
   * @since 1.2
   */
  public void facesCtxGetRenderKitTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxGetRenderKitTest");
    invoke();
  }

  /**
   * @testName: facesCtxIsGetProcessingEventTest
   * @assertion_ids: JSF:JAVADOC:1314; JSF:JAVADOC:1329
   * @test_Strategy: Validate that we can set the processing event flag and we
   *                 we get the correct value returned.
   * 
   * @since 1.2
   */
  public void facesCtxIsGetProcessingEventTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesCtxIsGetProcessingEventTest");
    invoke();
  }

} // end of URLClient
