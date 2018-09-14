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
 *  $Id$
 */
package com.sun.ts.tests.jsf.api.javax_faces.context.facescontext;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.ProjectStage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.event.PhaseId;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.RenderKitWrapper;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jsf.common.resolver.TCKELResolver;
import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

public final class TestServlet extends HttpTCKServlet {

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }
  // ------------------------------------------------ Test Methods -------
  // FacesContext.getClientIdsWithMessages()

  public void facesCtxGetClientIdsWithMessagesTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
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

    List list = Arrays.asList(testClientIds);
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
  }
  // FacesContext.getClientIdsWithMessages() returning an empty Iterator when no
  // components are found.

  public void facesCtxGetClientIdsWithMessagesEmptyTest(
      HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();

    Iterator i = context.getClientIdsWithMessages();

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
      while (i.hasNext()) {
        UIComponent comp = (UIComponent) i.next();
        out.println("Component: " + ((comp == null) ? null : comp.getId()));
      }
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
      UIComponent comp = (UIComponent) i.next();
      if (comp != null) {
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
  }
  // FacesContext.addMessage(UIComponent, FacesMessage)
  // FacesContext.getMessages()
  // FacesContext.getMessages(UIComponent)
  // 7/18/05 updated test to ensure message order is retained by returned
  // iterators.

  public void facesCtxAddGetMessagesTest(HttpServletRequest request,
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

    FacesContext context = getFacesContext();
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

    List messages = Arrays.asList(testMessagesComp);
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
    // to FacesContext.getMessages(null)
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
  }
  // FacesContext.getMessages(UIComponent)
  // FacesContext.getMessages()
  // - both return an empty Iterator if no messages were added.

  public void facesCtxGetMessagesEmptyTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    context.setViewRoot(createViewRoot());

    Iterator i = context.getMessages();
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
  }

  public void facesCtxGetMessageListTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {

    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    this.addTestMessagesToContext(context, null);

    try {
      List allMessages = context.getMessageList();
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

  } // END facesCtxgetMessageListTest

  public void facesCtxGetMessageListByIdTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {

    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    this.addTestMessagesToContext(context, "test_one");

    try {
      List allMessages = context.getMessageList("test_one");
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

  } // END facesCtxgetMessageListByIdTest

  public void facesCtxisValidationFailedTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {

    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
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

  } // END facesCtxisValidationFailedTest

  public void facesCtxisPostbackTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {

    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
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

  } // END facesCtxisPostbackTest

  // FacesContext.getApplication()
  public void facesCtxGetApplicationTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter out = response.getWriter();
    Application controlApplication = getApplication();
    Application testApplication = getFacesContext().getApplication();

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
  }
  // FacesContext.addMessage(UIComponent, FacesMessage) throws NPE if
  // FacesMessage is null

  public void facesCtxAddMessageNPETest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter out = response.getWriter();

    JSFTestUtil.checkForNPE(getFacesContext(), "addMessage",
        new Class<?>[] { String.class, FacesMessage.class },
        new Object[] { "id", null }, out);
  }

  // FacesContext.isProjectStage(ProjectStage stage) throws NPE if stage is null
  public void facesCtxIsProjectStageNPETest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter out = response.getWriter();

    JSFTestUtil.checkForNPE(getFacesContext(), "isProjectStage",
        new Class<?>[] { ProjectStage.class }, new Object[] { null }, out);
  }

  // FacesContext.getCurrentInstance()
  public void facesCtxSetGetCurrentInstanceTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter out = response.getWriter();
    FacesContext controlContext = getFacesContext();
    FacesContext testContext = FacesContext.getCurrentInstance();

    if (controlContext != testContext) {
      out.println(JSFTestUtil.FAIL + " FacesContext.getCurrentInstance() "
          + "returned a FacesContext instance that differs from the "
          + "one that is currently services this request.");
      out.println("Current FacesContext: " + controlContext);
      out.println("FacesContext.getCurrentInstance(): " + testContext);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // FacesContext.getMaximumSeverity()
  public void facesCtxGetMaximumSeverityTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
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
  }
  // FacesContext.getResponseComplete()
  // FacesContext.responseComplete()

  public void facesCtxResponseCompleteTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
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
  }
  // FacesContext.getRenderResponse()
  // FacesContext.renderResponse()

  public void facexCtxRenderResponseTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
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
  // FacesContext.setResponseWriter()
  // FacesContext.getResponseWriter()

  public void facesCtxSetGetResponseWriterTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
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

  // FacesContext.[get, set]ResponseStream()
  public void facesCtxSetGetResponseStreamTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
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

  }// End facesCtxSetGetResponseStreamTest

  public void facesCtxSetResponseStreamNPETest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter out = response.getWriter();

    JSFTestUtil.checkForNPE(getFacesContext(), "setResponseStream",
        new Class<?>[] { ResponseStream.class }, new Object[] { null }, out);

  }// End facesCtxSetResponseStreamNPETest

  public void facesCtxSetResponseWriterNPETest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter out = response.getWriter();

    JSFTestUtil.checkForNPE(getFacesContext(), "setResponseWriter",
        new Class<?>[] { ResponseWriter.class }, new Object[] { null }, out);

  }// End facesCtxSetResponseWriterNPETest

  // FacesContext.[get, set]ViewRoot()
  public void facesCtxSetGetViewRootTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    UIViewRoot root = (UIViewRoot) getApplication()
        .createComponent(UIViewRoot.COMPONENT_TYPE);

    context.setViewRoot(root);
    UIViewRoot test = context.getViewRoot();

    if (test == null) {
      out.println(JSFTestUtil.FAIL + " FacesContext.getViewRoot() returned "
          + "null after FacesContext.setViewRoot(UIViewRoot) " + "was called.");
      return;
    }

    if (test != root) {
      out.println(JSFTestUtil.FAIL + " FacesContext.getViewRoot() did not "
          + "return the expected UIViewRoot instance.");
      out.println("Expected: " + root);
      out.println("Received: " + test);
      return;
    }

    out.println(JSFTestUtil.PASS);

  } // End facesCtxSetGetViewRootTest

  // FacesContext.[is, set]ProcessingEvents()
  public void facesCtxIsGetProcessingEventTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();

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

  } // End facesCtxIsGetProcessingEventTest

  // FacesContext.[get, set]CurrentPhaseId()
  public void facesCtxSetGetCurrentPhaseIdTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();

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

  } // End facesCtxSetGetCurrentPhaseIdTest

  // Ensure NPE is thrown if a null argument is passed to
  // FacesContext.setViewRoot().
  public void facesCtxSetViewRootNPETest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter out = response.getWriter();

    JSFTestUtil.checkForNPE(getFacesContext(), "setViewRoot",
        new Class<?>[] { UIViewRoot.class }, new Object[] { null }, out);
  }

  // All methods of FacesContext must throw an IllegalStateException
  // if called after FacesContext.release() has been called with the
  // Exception of those referenced in the exMethods List.
  public void facesCtxISEAfterReleaseTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();

    // Excluded methods that do not throw IllegalStateException.
    List<String> exMethods = new ArrayList<String>();
    exMethods.add("release");
    exMethods.add("isReleased");
    exMethods.add("getComponentModificationManager");
    exMethods.add("setCurrentInstance");
    exMethods.add("getCurrentInstance");
    exMethods.add("getCurrentPhaseId");
    exMethods.add("setExceptionHandler");
    exMethods.add("getExceptionHandler");
    exMethods.add("setProcessingEvents");
    exMethods.add("isProcessingEvents");

    Method[] methods = FacesContext.class.getDeclaredMethods();
    // loop through the remaining methods an ensure they all
    // throw an IllegalStateException
    context.release();

    for (int i = 0; i < methods.length; i++) {
      Method method = methods[i];
      String name = method.getName();

      if (!isExcluded(name, exMethods)) {
        Class<?>[] types = method.getParameterTypes();
        Object[] params = new Object[types.length];

        if (name.equals("isProjectStage")) {
          params[0] = ProjectStage.Development;
        }

        JSFTestUtil.checkForISE(context, name, types, params, out);

      }
    }
  }

  public void facesCtxGetELContextTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {

    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();

    ELContext elContext = context.getELContext();

    if (elContext == null) {
      out.println(JSFTestUtil.FAIL + " FacesContext.getELContext() returned"
          + " null.");
      return;
    }

    out.println(JSFTestUtil.PASS);

  } // END facesCtxGetELContextTest

  public void facesCtxGetAttributesTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {

    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();

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

  } // END facesCtxgetAttributesTest

  public void facesCtxGetAttributesEmptyTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {

    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();

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

  } // END facesCtxgetAttributesEmptyTest

  public void facesCtxGetPartialViewContextTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {

    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();

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

  } // END facesCtxgetPartialViewContextTest

  public void facesCtxSetGetExceptionHandlerTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {

    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    ExceptionHandler eh = new TCKExceptionHandler();
    context.setExceptionHandler(eh);

    String golden = "TCKExceptionHandler";
    String result = context.getExceptionHandler().getClass().getSimpleName();

    if (golden.equals(result)) {
      out.println(JSFTestUtil.PASS);

    } else {
      out.print(
          "Test FAILED." + JSFTestUtil.NL + " Unexpected value returned from"
              + "FacesContext.getExceptionHandler()!" + JSFTestUtil.NL
              + "Expected: " + golden + JSFTestUtil.NL + "Received: " + result);
    }

  } // END facesCtxSetGetExceptionHandlerTest

  public void facesCtxsetExceptionHandlerTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {

    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();

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

  } // END facesCtxgetExceptionHandlerTest

  public void facesCtxGetExternalContextTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {

    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();

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

  } // END facesCtxgetExternalContextTest

  public void facesCtxisReleasedTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();

    // Test before release.
    if (context.isReleased()) {
      out.println("Test FAILED");
      out.println("Unexpected value returned from FacesContext.isReleased() "
          + "before FacesContext.release() was callled." + JSFTestUtil.NL
          + "Expected: FALSE " + JSFTestUtil.NL + "Received: TRUE"
          + JSFTestUtil.NL);
      return;
    }

    // Test After release
    context.release();
    if (!context.isReleased()) {
      out.println("Test FAILED");
      out.println("Unexpected value returned from FacesContext.isReleased() "
          + "after FacesContext.release() was called. " + JSFTestUtil.NL
          + "Expected: TRUE " + JSFTestUtil.NL + "Received: FALSE"
          + JSFTestUtil.NL);
      return;
    }

    out.println(JSFTestUtil.PASS);

  } // END facesCtxisReleasedTest

  public void facesCtxGetRenderKitTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {

    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
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
  } // END facesCtxGetRenderKitTest

  // ------------------------------------------- Private Methods -----------
  private String validateIllegalStateException(Method methodToInvoke,
      Object target, Object[] params) {
    String result = null;

    try {
      methodToInvoke.invoke(target, params);
      result = JSFTestUtil.FAIL + " No Exception thrown when calling '"
          + methodToInvoke.getName() + "'"
          + " after FacesContext.release() was called.";

    } catch (Throwable throwable) {
      Throwable t = throwable;
      if (throwable instanceof InvocationTargetException) {
        t = ((InvocationTargetException) throwable).getTargetException();
      }

      if (!(t instanceof IllegalStateException)) {
        StringBuffer sb = new StringBuffer(100);
        sb.append(JSFTestUtil.FAIL + " Exception thrown when '")
            .append(methodToInvoke.getName());
        sb.append("' was called after FacesContext.release(), but it "
            + "wasn't an instance of");
        sb.append(" IllegalStateException.\nException received: ")
            .append(t.getClass().getName());
        result = sb.toString();
      }
    }
    return result;
  }

  private boolean isExcluded(String methodName, List<String> excludeList) {
    boolean result = false;

    for (String excluded : excludeList) {
      if (excluded.equals(methodName) || methodName.charAt(0) == '$') {
        result = true;
      }
    }

    return result;
  }

  private FacesMessage[] getMessagesAsArray(Iterator iterator) {
    List list = new ArrayList();
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
  private static final class TCKExceptionHandler
      extends ExceptionHandlerWrapper {

    @Override
    public ExceptionHandler getWrapped() {
      // TODO Auto-generated method stub
      return null;
    }

  } // End TCKExceptionHandler

  private static class TCKRenderKit extends RenderKitWrapper {

    @Override
    public RenderKit getWrapped() {
      return FacesContext.getCurrentInstance().getRenderKit();
    }

  } // End TCKRenderKit

}
