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
 *  $Id$
 */
package com.sun.ts.tests.jsf.api.javax_faces.context.facescontextwrapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.el.ELContext;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextWrapper;
import javax.faces.context.PartialViewContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.SystemEvent;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.RenderKitWrapper;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

public final class TestServlet extends HttpTCKServlet {

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }

  // ------------------------------------------------ Test Methods -------

  // FacesContextWrapper.getClientIdsWithMessages()
  public void facesCtxWrapperGetClientIdsWithMessagesTest(
      HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    PrintWriter out = response.getWriter();
    FacesContextWrapper context = new TCKFacesContext();
    context.setViewRoot(createViewRoot());

    UIComponent component1 = getApplication()
        .createComponent(UIInput.COMPONENT_TYPE);
    component1.setId("component1");
    UIComponent component2 = getApplication()
        .createComponent(UIOutput.COMPONENT_TYPE);
    component2.setId("component2");
    FacesMessage message1 = new TCKMessage(FacesMessage.SEVERITY_INFO,
        "message1 summary", "message1 detail", "message1");

    context.addMessage(component1.getClientId(context), message1);
    context.addMessage(component2.getClientId(context), message1);

    String[] controlClientIds = { component1.getClientId(context),
        component2.getClientId(context) };

    String[] testClientIds = JSFTestUtil
        .getAsStringArray(context.getClientIdsWithMessages());

    if (controlClientIds.length != testClientIds.length) {
      out.println(
          JSFTestUtil.FAIL + " Unexpected number of client IDs returned by "
              + "FacesContext.getComponentIdsWithMessages().");
      out.println(
          "Exected the number of IDs returned to be 2.  Actual number returned: "
              + testClientIds.length);
      return;
    }

    List<String> list = Arrays.asList(testClientIds);
    for (int i = 0; i < controlClientIds.length; i++) {
      if (!list.contains(controlClientIds[i])) {
        out.println(JSFTestUtil.FAIL + " Unable to find '" + controlClientIds[i]
            + "' in the client IDs returned"
            + " by FacesContext.getClientIdsWithMessages().");
        for (int j = 0; j < testClientIds.length; j++) {
          out.println("Client ID received: " + testClientIds[j]);
        }
        return;
      }
    }

    out.println(JSFTestUtil.PASS);

  } // End facesCtxWrapperGetClientIdsWithMessagesTest

  public void facesCtxWrapperGetClientIdsWithMessagesEmptyTest(
      HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    PrintWriter out = response.getWriter();
    FacesContextWrapper context = new TCKFacesContext();

    Iterator<String> i = context.getClientIdsWithMessages();

    if (i == null) {
      out.println(JSFTestUtil.FAIL
          + " FacesContext.getClientIdsWithMessages() returned null"
          + " when instead of an empty Iterator.");
      return;
    }

    if (i.hasNext()) {
      out.println(JSFTestUtil.FAIL
          + " FacesContext.getClientIdsWithMessages() returned a"
          + " non-empty Iterator even though no messages have been added.");
      return;
    }

    context.addMessage(null, new FacesMessage());
    context.addMessage(null, new FacesMessage());
    i = context.getClientIdsWithMessages();

    if (i == null) {
      out.println("Test FAILED[2]. FacesContext.getClientIds"
          + "WithMessages() returned null when instead of an empty "
          + "Iterator.");
      return;
    }

    if (i.hasNext()) {
      String clientId = i.next();
      if (!(clientId == null)) {
        out.println(
            JSFTestUtil.FAIL + " Non-null value returned from iterator.");
        return;
      }

      if (i.hasNext()) {
        out.println(JSFTestUtil.FAIL + " Iterator contained more than 1"
            + " value after when getClientIdWithMessages() was "
            + "called where a Message existed with no client id.");
        return;
      }
    }

    out.println(JSFTestUtil.PASS);

  } // End facesCtxWrapperGetClientIdsWithMessagesEmptyTest

  // FacesContextWrapper.addMessage(UIComponent, FacesMessage)
  // FacesContextWrapper.getMessages()
  // FacesContextWrapper.getMessages(UIComponent)
  public void facesCtxWrapperAddGetMessagesTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter out = response.getWriter();

    UIComponent component = getApplication()
        .createComponent(UIInput.COMPONENT_TYPE);
    component.setId("input1");
    FacesMessage message1 = new TCKMessage(FacesMessage.SEVERITY_INFO,
        "message1 summary", "message1 detail", "message1");
    FacesMessage message2 = new TCKMessage(FacesMessage.SEVERITY_INFO,
        "message2 summary", "message2 detail", "message2");
    FacesMessage message3 = new TCKMessage(FacesMessage.SEVERITY_INFO,
        "message3 summary", "message3 detail", "message3");
    FacesMessage message4 = new TCKMessage(FacesMessage.SEVERITY_INFO,
        "message4 summary", "message4 detail", "message4");

    FacesContextWrapper context = new TCKFacesContext();

    context.setViewRoot(createViewRoot());

    context.addMessage(component.getClientId(context), message1);
    context.addMessage(component.getClientId(context), message3);
    context.addMessage(null, message2);
    context.addMessage(null, message4);

    FacesMessage[] controlMessagesComp = { message1, message3 };
    FacesMessage[] controlMessagesNoComp = { message1, message3, message2,
        message4 };
    FacesMessage[] controlMessagesNullComp = { message2, message4 };

    FacesMessage[] testMessagesComp = getMessagesAsArray(
        context.getMessages(component.getClientId(context)));

    // the next two should be equivalent
    FacesMessage[] testMessagesNullComp = getMessagesAsArray(
        context.getMessages(null));
    FacesMessage[] testMessages = getMessagesAsArray(context.getMessages());

    // check the messages associated with a component
    if (controlMessagesComp.length != testMessagesComp.length) {
      out.println(JSFTestUtil.FAIL + " An unexpected number of messages"
          + " returned when FacesContext. getMessages(UIComponent)"
          + " was called.");
      out.println("Expected 2 messages to be returned. Actual message "
          + "count: " + testMessagesComp.length);
      return;
    }

    List<FacesMessage> messages = Arrays.asList(testMessagesComp);
    for (int i = 0; i < controlMessagesComp.length; i++) {
      if (!messages.get(i).equals(controlMessagesComp[i])) {
        out.println(JSFTestUtil.FAIL + " Unable to find '"
            + controlMessagesComp[i] + "' in the return value of FacesContext."
            + "getMessages(UIComponent) when the appropriate"
            + " component was provided.");
        out.println(
            "Messages returned: " + JSFTestUtil.getAsString(testMessagesComp));
        return;
      }
    }

    // check the messages returned when a null value is provided
    // to FacesContextWrapper.getMessages(null)
    if (controlMessagesNullComp.length != testMessagesNullComp.length) {
      out.println(JSFTestUtil.FAIL + " An unexpected number of messages "
          + "returned when FacesContext. getMessages(null) was " + "called.");
      out.println("Expected 2 messages to be returned. Actual message "
          + "count: " + testMessagesNullComp.length);
      return;
    }

    messages = Arrays.asList(testMessagesNullComp);
    for (int i = 0; i < controlMessagesNullComp.length; i++) {
      if (!messages.get(i).equals(controlMessagesNullComp[i])) {
        out.println(
            JSFTestUtil.FAIL + " Unable to find '" + controlMessagesNullComp[i]
                + "' in the return value of FacesContext."
                + "getMessages(UIComponent) when null was passed to the"
                + " method.");
        out.println("Messages returned: "
            + JSFTestUtil.getAsString(testMessagesNullComp));
        return;
      }
    }

    // check the messages returned by FacesContext.getMessages()
    if (controlMessagesNoComp.length != testMessages.length) {
      out.println(JSFTestUtil.FAIL + " An unexpected number of messages "
          + "returned when FacesContext." + "getMessages() was called.");
      out.println("Expected 4 messages to be returned. Actual message"
          + " count: " + testMessages.length);
      return;
    }

    messages = Arrays.asList(testMessages);
    for (int i = 0; i < controlMessagesNoComp.length; i++) {
      if (!messages.get(i).equals(controlMessagesNoComp[i])) {
        out.println(
            JSFTestUtil.FAIL + " Unable to find '" + controlMessagesNoComp[i]
                + "' in the return value of FacesContext." + "getMessages()");
        out.println(
            "Messages returned: " + JSFTestUtil.getAsString(testMessages));
        return;
      }
    }
    out.println(JSFTestUtil.PASS);

  } // End facesCtxWrapperAddGetMessagesTest

  // FacesContextWrapper.getMessages(UIComponent)
  // FacesContextWrapper.getMessages()
  public void facesCtxWrapperGetMessagesEmptyTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter out = response.getWriter();
    FacesContextWrapper context = new TCKFacesContext();
    context.setViewRoot(createViewRoot());

    Iterator<FacesMessage> i = context.getMessages();
    if (i != null) {
      if (i.hasNext()) {
        out.println(JSFTestUtil.FAIL + " FacesContext.getMessages() "
            + "returned a non-empty Iterator when no FacesMessage "
            + "instances were added.");
        return;
      }
    } else {
      out.println(JSFTestUtil.FAIL + " FacesContext.getMessages() returned "
          + "null as opposed to an empty Iterator.");
      return;
    }

    i = context.getMessages(getApplication()
        .createComponent(UIInput.COMPONENT_TYPE).getClientId(context));
    if (i != null) {
      if (i.hasNext()) {
        out.println(JSFTestUtil.FAIL + " FacesContext.getMessages("
            + "UIComponent) returned a non-empty Iterator when no "
            + "FacesMessage instances were added.");
        return;
      }
    } else {
      out.println(JSFTestUtil.FAIL + " FacesContext.getMessages(UIComponent)"
          + " returned null as opposed to an empty Iterator.");
      return;
    }

    i = context.getMessages(null);
    if (i != null) {
      if (i.hasNext()) {
        out.println(JSFTestUtil.FAIL + " FacesContext.getMessages(null) "
            + "returned a non-empty Iterator when no FacesMessage"
            + " instances were added.");
        return;
      }
    } else {
      out.println(JSFTestUtil.FAIL + " FacesContext.getMessages(null) "
          + "returned null as opposed to an empty Iterator.");
      return;
    }

    out.println(JSFTestUtil.PASS);

  } // End facesCtxWrapperGetMessagesEmptyTest

  public void facesCtxWrapperGetMessageListTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {

    PrintWriter out = response.getWriter();
    FacesContextWrapper context = new TCKFacesContext();
    this.addTestMessagesToContext(context, null);

    try {
      List<FacesMessage> allMessages = context.getMessageList();
      if (allMessages.size() != 4) {
        out.print("Test FAILED." + JSFTestUtil.NL + "Call to "
            + "FacesContext.getMessageList unexepected size! " + JSFTestUtil.NL
            + "Expected: 4" + JSFTestUtil.NL + "Received: "
            + allMessages.size());
        return;
      }
    } catch (Exception e) {
      out.print("Test FAILED." + JSFTestUtil.NL + e.toString());
    }

    out.println(JSFTestUtil.PASS);

  } // END facesCtxWrappergetMessageListTest

  public void facesCtxWrapperGetMessageListByIdTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {

    PrintWriter out = response.getWriter();
    FacesContextWrapper context = new TCKFacesContext();
    this.addTestMessagesToContext(context, "test_one");

    try {
      List<FacesMessage> allMessages = context.getMessageList("test_one");
      if (allMessages.size() != 2) {
        out.print("Test FAILED." + JSFTestUtil.NL + "Call to "
            + "FacesContext.getMessageList unexepected size! " + JSFTestUtil.NL
            + "Expected: 2" + JSFTestUtil.NL + "Received: "
            + allMessages.size());
        return;
      }
    } catch (Exception e) {
      out.print("Test FAILED." + JSFTestUtil.NL + e.toString());
    }

    out.println(JSFTestUtil.PASS);

  } // END facesCtxWrappergetMessageListByIdTest

  public void facesCtxWrapperIsValidationFailedTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter out = response.getWriter();
    FacesContextWrapper context = new TCKFacesContext();
    Boolean isFailed;

    try {
      isFailed = context.isValidationFailed();
      if (isFailed) {
        out.print("Test FAILED." + JSFTestUtil.NL + "Unexpected result from "
            + "FacesContext.isValadationFailed()" + JSFTestUtil.NL
            + "Expected: false" + JSFTestUtil.NL + "Received: " + isFailed);
        return;
      }

      context.validationFailed();
      isFailed = context.isValidationFailed();
      if (!isFailed) {
        out.print("Test FAILED." + JSFTestUtil.NL + "Unexpected result from "
            + "FacesContext.isValadationFailed() after calling "
            + "FacesContext.validationFailed." + JSFTestUtil.NL
            + "Expected: true" + JSFTestUtil.NL + "Received: " + isFailed);
        return;
      }
    } catch (Exception e) {
      out.print("Test FAILED." + JSFTestUtil.NL + e.toString());
    }

    out.println(JSFTestUtil.PASS);

  } // END facesCtxWrapperIsValidationFailedTest

  public void facesCtxWrapperIsReleasedTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter out = response.getWriter();
    FacesContextWrapper context = new TCKFacesContext();

    // Test before release.
    if (context.isReleased()) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unexpected value returned from FacesContextWrapper.isReleased() "
          + "before FacesContextWrapper.release() was callled." + JSFTestUtil.NL
          + "Expected: FALSE " + JSFTestUtil.NL + "Received: TRUE"
          + JSFTestUtil.NL);
      return;
    }

    out.println(JSFTestUtil.PASS);

  } // END facesCtxWrapperIsReleasedTest

  public void facesCtxWrapperIsPostbackTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter out = response.getWriter();
    FacesContextWrapper context = new TCKFacesContext();

    context.getApplication();
    Boolean isPB;

    try {
      isPB = context.isPostback();
      if (isPB) {
        out.print("Test FAILED." + JSFTestUtil.NL + "Unexpected result from "
            + "FacesContext.isPostback() before calling "
            + "StateManger.writeState()." + JSFTestUtil.NL + "Expected: false"
            + JSFTestUtil.NL + "Received: " + isPB);
        return;
      }

    } catch (Exception e) {
      out.print("Test FAILED." + JSFTestUtil.NL + e.toString());
    }

    out.println(JSFTestUtil.PASS);

  } // END facesCtxWrapperIsPostbackTest

  // FacesContextWrapper.getApplication()
  public void facesCtxWrapperGetApplicationTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter out = response.getWriter();
    Application controlApplication = getApplication();
    FacesContextWrapper context = new TCKFacesContext();

    Application testApplication = context.getApplication();

    if (controlApplication == null) {
      out.println(JSFTestUtil.FAIL + " Application.getCurrentInstance() "
          + "unexpectedly returned null.");
      return;
    }

    if (controlApplication != testApplication) {
      out.println(JSFTestUtil.FAIL + " The Application instance returned by "
          + "Application. getCurrentInstance() differs from the "
          + "Application returned by FacesContext.getApplication().");
      out.println("Application.getCurrentInstance(): " + controlApplication);
      out.println("FacesContext.getApplication():" + testApplication);
      return;
    }

    out.println(JSFTestUtil.PASS);

  } // End facesCtxWrapperGetApplicationTest

  // FacesContextWrapper.getMaximumSeverity()
  public void facesCtxWrapperGetMaximumSeverityTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter out = response.getWriter();
    FacesContextWrapper context = new TCKFacesContext();

    context.setViewRoot(new UIViewRoot());
    FacesMessage message1 = new TCKMessage(FacesMessage.SEVERITY_INFO,
        "message1 summary", "message1 detail", "message1");
    FacesMessage message2 = new TCKMessage(FacesMessage.SEVERITY_WARN,
        "message2 summary", "message2 detail", "message2");
    FacesMessage message3 = new TCKMessage(FacesMessage.SEVERITY_ERROR,
        "message3 summary", "message3 detail", "message3");
    FacesMessage message4 = new TCKMessage(FacesMessage.SEVERITY_FATAL,
        "message4 summary", "message4 detail", "message4");

    /*
     * if no messages have been queued, then FacesContext.getMaximumSeverity
     * should return null
     */
    if (context.getMaximumSeverity() != null) {
      out.println(
          JSFTestUtil.FAIL + " FacesContext.getMaximumSeverity returned a"
              + "non null value when no messages have been added to the"
              + " FacesContext.");
      out.println(
          "Severity received: " + context.getMaximumSeverity().getOrdinal());
      return;
    }

    context.addMessage(null, message1);
    int severity = context.getMaximumSeverity().getOrdinal();
    if (severity != FacesMessage.SEVERITY_INFO.getOrdinal()) {
      out.println(JSFTestUtil.FAIL + " Expected FacesContext."
          + "getMaximumSeverity() to return FacesMessage."
          + "SEVERITY_INFO when only a SEVERITY_INFO message exists"
          + " in the FacesContext.");
      out.println("Severity Recieved: " + severity);
      out.println("Integer value of FacesMessage.SEVERITY_INFO: "
          + FacesMessage.SEVERITY_INFO);
      return;
    }

    context.addMessage(null, message2);
    severity = context.getMaximumSeverity().getOrdinal();
    if (severity != FacesMessage.SEVERITY_WARN.getOrdinal()) {
      out.println(JSFTestUtil.FAIL + " Expected FacesContext."
          + "getMaximumSeverity() to return FacesMessage."
          + "SEVERITY_WARN when both a SEVERITY_INFO and "
          + "SEVERITY_WARN message exist in the FacesContext.");
      out.println("Severity Recieved: " + severity);
      out.println("Integer value of FacesMessage.SEVERITY_WARN: "
          + FacesMessage.SEVERITY_WARN);
      return;
    }

    context.addMessage(null, message3);
    severity = context.getMaximumSeverity().getOrdinal();
    if (severity != FacesMessage.SEVERITY_ERROR.getOrdinal()) {
      out.println(JSFTestUtil.FAIL + " Expected FacesContext."
          + "getMaximumSeverity() to return FacesMessage."
          + "SEVERITY_ERROR when messages with SEVERITY_INFO, "
          + "SEVERITY_WARN, and SEVERITY_ERROR exist in the"
          + " FacesContext.");
      out.println("Severity Recieved: " + severity);
      out.println("Integer value of FacesMessage.SEVERITY_ERROR: "
          + FacesMessage.SEVERITY_ERROR);
      return;
    }

    context.addMessage(null, message4);
    severity = context.getMaximumSeverity().getOrdinal();
    if (severity != FacesMessage.SEVERITY_FATAL.getOrdinal()) {
      out.println(JSFTestUtil.FAIL + " Expected FacesContext."
          + "getMaximumSeverity() to return FacesMessage."
          + "SEVERITY_ERROR when messages with SEVERITY_INFO, "
          + "SEVERITY_WARN, SEVERITY_ERROR, SEVERITY_FATAL exist"
          + " in the FacesContext.");
      out.println("Severity Recieved: " + severity);
      out.println("Integer value of FacesMessage.SEVERITY_FATAL: "
          + FacesMessage.SEVERITY_FATAL);
      return;
    }

    out.println(JSFTestUtil.PASS);

  } // End facesCtxWrapperGetMaximumSeverityTest

  // FacesContextWrapper.getResponseComplete()
  // FacesContextWrapper.responseComplete()
  public void facesCtxWrapperGetResponseCompleteTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter out = response.getWriter();
    FacesContextWrapper context = new TCKFacesContext();
    context.setViewRoot(new UIViewRoot());

    if (context.getResponseComplete()) {
      out.println(JSFTestUtil.FAIL + " Expected FacesContext."
          + "getResponseComplete() to return false if FacesContext."
          + "responseComplete() hasn't been called.");
      return;
    }

    context.responseComplete();

    if (!context.getResponseComplete()) {
      out.println(JSFTestUtil.FAIL + " Expected FacesContext."
          + "getResponseComplete() to return true if"
          + " FacesContext.responseComplete() has been called.");
      return;
    }

    out.println(JSFTestUtil.PASS);

  } // End facesCtxWrapperGetResponseCompleteTest

  // FacesContextWrapper.getRenderResponse()
  // FacesContextWrapper.renderResponse()
  public void facesCtxWrapperRenderResponseTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter out = response.getWriter();
    FacesContextWrapper context = new TCKFacesContext();
    context.setViewRoot(new UIViewRoot());

    if (context.getRenderResponse()) {
      out.println(JSFTestUtil.FAIL + " Expected FacesContext."
          + "getRenderResponse() to return false if"
          + " FacesContext.renderResponse() hasn't been called.");
      return;
    }

    context.renderResponse();

    if (!context.getRenderResponse()) {
      out.println(JSFTestUtil.FAIL + " Expected FacesContext."
          + "getRenderResponse() to return true if"
          + " FacesContext.renderResponse() has been called.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // FacesContextWrapper.setResponseWriter()
  // FacesContextWrapper.getResponseWriter()
  public void facesCtxWrapperSetGetResponseWriterTest(
      HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    PrintWriter out = response.getWriter();
    FacesContextWrapper context = new TCKFacesContext();
    context.setViewRoot(new UIViewRoot());

    ResponseWriter writer = new TCKResponseWriter();

    context.setResponseWriter(writer);
    ResponseWriter test = context.getResponseWriter();

    if (test == null) {
      out.println(JSFTestUtil.FAIL + " FacesContext.getResponseWriter() "
          + "returned null after FacesContext.setResponseWriter("
          + "ResponseWriter) was called.");
      return;
    }

    if (test != writer) {
      out.println(JSFTestUtil.FAIL + " FacesContext.getResponseWriter()"
          + " did not return the expected" + " ResponseWriter instance.");
      out.println("Expected: " + writer);
      out.println("Received: " + test);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // FacesContextWrapper.[get, set]CurrentPhaseId()
  public void facesCtxWrapperSetGetCurrentPhaseIdTest(
      HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    PrintWriter out = response.getWriter();
    FacesContextWrapper context = new TCKFacesContext();

    context.setCurrentPhaseId(PhaseId.ANY_PHASE);
    PhaseId result = context.getCurrentPhaseId();
    if (PhaseId.ANY_PHASE.equals(result)) {
      out.println(JSFTestUtil.PASS);

    } else {
      out.println(
          JSFTestUtil.FAIL + " FacesContext.getCurrentPhaseId() did not "
              + "return the expected PhaseId.");
      out.println("Expected: " + PhaseId.ANY_PHASE);
      out.println("Received: " + result);
    }

  } // End facesCtxWrapperSetGetCurrentPhaseIdTest

  // FacesContextWrapper.[get, set]ResponseStream()
  public void facesCtxWrapperSetGetResponseStreamTest(
      HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    PrintWriter out = response.getWriter();
    FacesContextWrapper context = new TCKFacesContext();
    context.setViewRoot(new UIViewRoot());

    ResponseStream stream = new TCKResponseStream();

    context.setResponseStream(stream);
    ResponseStream test = context.getResponseStream();

    if (test == null) {
      out.println(JSFTestUtil.FAIL + " FacesContext.getResponseStream()"
          + " returned null after FacesContext.setResponseStream("
          + "ResponseStream) was called.");
      return;
    }

    if (test != stream) {
      out.println(JSFTestUtil.FAIL + " FacesContext.getResponseStream() "
          + "did not return the expected ResponseStream instance.");
      out.println("Expected: " + stream);
      out.println("Received: " + test);
      return;
    }

    out.println(JSFTestUtil.PASS);

  }// End facesCtxWrapperSetGetResponseStreamTest

  // FacesContextWrapper.setViewRoot()
  public void facesCtxWrapperGetViewRootTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter out = response.getWriter();
    FacesContextWrapper context = new TCKFacesContext();
    UIViewRoot root = (UIViewRoot) getApplication()
        .createComponent(UIViewRoot.COMPONENT_TYPE);

    String golden = "TCKViewRoot";

    root.setId(golden);
    context.setViewRoot(root);

    String result = context.getViewRoot().getId();

    if (!golden.equals(result)) {
      out.println(JSFTestUtil.FAIL + "Unexpected value returned from "
          + "FacesContextWrapper.getViewRoot()." + JSFTestUtil.NL + "Expected: "
          + golden + JSFTestUtil.NL + "Receieved: " + result);

    } else {
      out.println(JSFTestUtil.PASS);
    }

  }// End facesCtxWrapperGetViewRootTest

  public void facesCtxWrapperGetELContextTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {

    PrintWriter out = response.getWriter();
    FacesContextWrapper context = new TCKFacesContext();

    ELContext elContext = context.getELContext();

    if (elContext == null) {
      out.println(JSFTestUtil.FAIL + " FacesContext.getELContext() returned"
          + " null.");

    } else {
      out.println(JSFTestUtil.PASS);
    }

  } // END facesCtxGetELContextTest

  public void facesCtxWrapperGetAttributesTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {

    PrintWriter out = response.getWriter();
    FacesContextWrapper context = new TCKFacesContext();

    if (!(context.getAttributes() instanceof Map)) {
      out.println("Test FAILED. Unexpected class type returned from"
          + "FacesContext.getAttributes() method." + JSFTestUtil.NL
          + "Expected: java.util.Map" + JSFTestUtil.NL + "Received: "
          + context.getAttributes().getClass().getName());
      return;
    } else {
      try {
        context.getAttributes().put("test_key", "test_value");

      } catch (UnsupportedOperationException usoe) {
        out.print("Test FAILED. Map Not Mutable!");
        out.print(usoe.toString());
      } catch (Exception e) {
        /*
         * Swallow any other exception since we only care if the Map is mutable.
         */
      }
    }

    out.println(JSFTestUtil.PASS);

  } // END facesCtxWrappergetAttributesTest

  public void facesCtxWrapperGetAttributesEmptyTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {

    PrintWriter out = response.getWriter();
    FacesContextWrapper context = new TCKFacesContext();

    try {
      context.getAttributes().put("test_key", "test_value");
      // validate that the attribute was put in.
      HashMap<?, ?> hm = (HashMap<?, ?>) context.getAttributes();
      if (!hm.containsKey("test_key")) {
        out.println(
            "Test FAILED. Attribute not added to context" + "attribute Map!");
        return;
      } else {
        // map should be empty after this call.
        context.release();
        if (!hm.isEmpty()) {
          out.println("Test FAILED. Attributes still found in "
              + "context attribute Map, After 'context.release()"
              + "was called!");
          return;
        }

      }
    } catch (Exception e) {
      out.print("Test FAILED." + JSFTestUtil.NL + e.toString());
    }

    out.println(JSFTestUtil.PASS);

  } // END facesCtxWrappergetAttributesEmptyTest

  public void facesCtxWrapperGetPartialViewContextTest(
      HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    PrintWriter out = response.getWriter();
    FacesContextWrapper context = new TCKFacesContext();

    try {
      PartialViewContext pvc = context.getPartialViewContext();
      if (!(pvc instanceof PartialViewContext)) {
        out.print("Test FAILED." + JSFTestUtil.NL + "Call to "
            + "FacesContext.getPartialViewContext() should have "
            + "returned a PartialViewContext." + JSFTestUtil.NL
            + "Instead Received: " + pvc.getClass().getSimpleName());
        return;
      }
    } catch (Exception e) {
      out.print("Test FAILED." + JSFTestUtil.NL + e.toString());
    }

    out.println(JSFTestUtil.PASS);

  } // END facesCtxWrappergetPartialViewContextTest

  public void facesCtxWrapperGetExceptionHandlerTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter out = response.getWriter();
    FacesContextWrapper context = new TCKFacesContext();

    try {
      ExceptionHandler eh = context.getExceptionHandler();

      if (!(eh instanceof ExceptionHandler)) {
        out.print("Test FAILED." + JSFTestUtil.NL + "Call to "
            + "FacesContext.getExceptionHandler() should have "
            + "returned a ExceptionHandler." + JSFTestUtil.NL
            + "Instead Received: " + eh.getClass().getSimpleName());
        return;
      }

    } catch (Exception e) {
      out.print("Test FAILED." + JSFTestUtil.NL + e.toString());
    }

    out.println(JSFTestUtil.PASS);

  } // END facesCtxWrappergetExceptionHandlerTest

  public void facesCtxWrapperSetExceptionHandlerTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {

    PrintWriter out = response.getWriter();
    FacesContextWrapper context = new TCKFacesContext();

    try {
      context.setExceptionHandler(new TCKExceptionHandler());
      ExceptionHandler eh = context.getExceptionHandler();

      if (!("TCKExceptionHandler".equals(eh.getClass().getSimpleName()))) {
        out.print("Test FAILED." + JSFTestUtil.NL + "Call to "
            + "FacesContext.setExceptionHandler() did not set " + "Handler!"
            + JSFTestUtil.NL + "Expected: TCKExceptionHandler" + JSFTestUtil.NL
            + "Received: " + eh.getClass().getSimpleName());
        return;
      }
    } catch (Exception e) {
      out.print("Test FAILED." + JSFTestUtil.NL + e.toString());
    }

    out.println(JSFTestUtil.PASS);

  } // END facesCtxWrapperSetExceptionHandlerTest

  public void facesCtxWrapperGetExternalContextTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter out = response.getWriter();
    FacesContextWrapper context = new TCKFacesContext();

    try {
      ExternalContext ec = context.getExternalContext();

      if (!(ec instanceof ExternalContext)) {
        out.print("Test FAILED." + JSFTestUtil.NL + "Call to "
            + "FacesContext.getExternalContext did not return"
            + "correct instance type." + JSFTestUtil.NL
            + "Expected: ExternalContext" + JSFTestUtil.NL + "Received: "
            + ec.getClass().getSimpleName());
        return;
      }

    } catch (Exception e) {
      out.print("Test FAILED." + JSFTestUtil.NL + e.toString());
    }

    out.println(JSFTestUtil.PASS);

  } // END facesCtxWrappergetExternalContextTest

  public void facesCtxWrapperGetRenderKitTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {

    PrintWriter out = response.getWriter();
    FacesContextWrapper context = new TCKFacesContext();
    UIViewRoot root = context.getViewRoot();
    String rkn = "TCKRenderKit";

    // Setup renderKit
    RenderKit renderKit = new TCKRenderKit();
    RenderKitFactory factory = (RenderKitFactory) FactoryFinder
        .getFactory(FactoryFinder.RENDER_KIT_FACTORY);
    factory.addRenderKit(rkn, renderKit);

    root.setRenderKitId(rkn);
    RenderKit result = context.getRenderKit();

    if (rkn.equals(result.getClass().getSimpleName())) {
      out.println(JSFTestUtil.PASS);

    } else {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unexpected RenderKit returned from FacesContext.getRenderKit()"
          + JSFTestUtil.NL + "Expected: " + rkn + JSFTestUtil.NL + "Received: "
          + result);
    }
  } // END facesCtxWrapperGetRenderKitTest

  // FacesContextWrapper.[is, set]ProcessingEvents()
  public void facesCtxWrapperIsGetProcessingEventTest(
      HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    PrintWriter out = response.getWriter();
    FacesContextWrapper context = new TCKFacesContext();

    context.setProcessingEvents(true);
    if (!context.isProcessingEvents()) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unexpected value returned from FacesContext.isProcessingEvents()"
          + JSFTestUtil.NL + "Expected: true" + JSFTestUtil.NL + "Received: "
          + context.isProcessingEvents());

    } else {
      context.setProcessingEvents(false);
      if (context.isProcessingEvents()) {
        out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
            + "Unexpected value returned from FacesContext.isProcessingEvents()"
            + JSFTestUtil.NL + "Expected: false" + JSFTestUtil.NL + "Received: "
            + context.isProcessingEvents());

      } else {
        out.println(JSFTestUtil.PASS);
      }
    }

  } // End facesCtxWrapperIsGetProcessingEventTest

  // ------------------------------------------- Private Methods -----------

  private FacesMessage[] getMessagesAsArray(Iterator<FacesMessage> iterator) {
    List<FacesMessage> list = new ArrayList<FacesMessage>();
    while (iterator.hasNext()) {
      list.add(iterator.next());
    }
    return (FacesMessage[]) list.toArray(new FacesMessage[list.size()]);
  }

  /*
   * Add test messages to the given FacesContext.
   */
  private void addTestMessagesToContext(FacesContext context, String compId) {

    UIComponent component = getApplication()
        .createComponent(UIInput.COMPONENT_TYPE);
    if (compId == null) {
      component.setId("nothing");
    } else {
      component.setId(compId);
    }

    FacesMessage message1 = new TCKMessage(FacesMessage.SEVERITY_INFO,
        "message1 summary", "message1 detail", "message1");
    FacesMessage message2 = new TCKMessage(FacesMessage.SEVERITY_INFO,
        "message2 summary", "message2 detail", "message2");
    FacesMessage message3 = new TCKMessage(FacesMessage.SEVERITY_INFO,
        "message3 summary", "message3 detail", "message3");
    FacesMessage message4 = new TCKMessage(FacesMessage.SEVERITY_INFO,
        "message4 summary", "message4 detail", "message4");

    context.setViewRoot(createViewRoot());

    context.addMessage(component.getClientId(context), message1);
    context.addMessage(component.getClientId(context), message3);
    context.addMessage(null, message2);
    context.addMessage(null, message4);
  }

  // ------------------------------------------- Inner Classes -----------
  // skeletal implementation of ResponseStream
  private static final class TCKResponseStream extends ResponseStream {

    @Override
    public void write(int b) throws IOException {
    }
  }
  // skeletal implementation of ResponseWriter

  private static final class TCKResponseWriter extends ResponseWriter {

    public void closeStartTag(UIComponent component) throws IOException {
    }

    @Override
    public String getContentType() {
      return null;
    }

    @Override
    public String getCharacterEncoding() {
      return null;
    }

    @Override
    public void write(char cbuf[], int off, int len) throws IOException {
    }

    @Override
    public void flush() throws IOException {
    }

    @Override
    public void close() throws IOException {
    }

    @Override
    public void startDocument() throws IOException {
    }

    @Override
    public void endDocument() throws IOException {
    }

    @Override
    public void startElement(String name, UIComponent component)
        throws IOException {
    }

    @Override
    public void endElement(String name) throws IOException {
    }

    @Override
    public void writeAttribute(String name, Object value, String property)
        throws IOException {
    }

    @Override
    public void writeURIAttribute(String name, Object value, String property)
        throws IOException {
    }

    @Override
    public void writeComment(Object comment) throws IOException {
    }

    @Override
    public void writeText(Object text, String property) throws IOException {
    }

    @Override
    public void writeText(char text[], int off, int len) throws IOException {
    }

    @Override
    public ResponseWriter cloneWithWriter(Writer writer) {
      return null;
    }
  }

  // simple implementation of FacesMessage
  private static final class TCKMessage extends FacesMessage {

    private FacesMessage.Severity messageSeverity;

    private String messageSummary;

    private String messageDetail;

    String name;

    TCKMessage(FacesMessage.Severity severity, String messageSummary,
        String messageDetail, String name) {
      this.messageSeverity = severity;
      this.messageSummary = messageSummary;
      this.messageDetail = messageDetail;
      this.name = name;
    }

    @Override
    public String getDetail() {
      return messageDetail;
    }

    @Override
    public FacesMessage.Severity getSeverity() {
      return messageSeverity;
    }

    @Override
    public String getSummary() {
      return messageSummary;
    }

    @Override
    public String toString() {
      return name;
    }
  }

  // simple implementation of ExceptionHandler
  private static final class TCKExceptionHandler extends ExceptionHandler {

    @Override
    public void handle() throws FacesException {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ExceptionQueuedEvent getHandledExceptionQueuedEvent() {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Iterable<ExceptionQueuedEvent> getUnhandledExceptionQueuedEvents() {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Iterable<ExceptionQueuedEvent> getHandledExceptionQueuedEvents() {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void processEvent(SystemEvent arg0) throws AbortProcessingException {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isListenerForSource(Object arg0) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Throwable getRootCause(Throwable arg0) {
      throw new UnsupportedOperationException("Not supported yet.");
    }
  }

  // Simple FacesContextWrapper implementation
  private static final class TCKFacesContext extends FacesContextWrapper {

    @Override
    public FacesContext getWrapped() {
      return FacesContext.getCurrentInstance();
    }

  } // EndTCKExceptionHandler

  private static class TCKRenderKit extends RenderKitWrapper {

    @Override
    public RenderKit getWrapped() {
      return FacesContext.getCurrentInstance().getRenderKit();
    }

  } // End TCKRenderKit

}
