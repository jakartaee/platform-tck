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

package com.sun.ts.tests.jsf.api.javax_faces.flow.flowhandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.el.MethodExpression;
import javax.faces.application.Application;
import javax.faces.application.NavigationCase;
import javax.faces.context.FacesContext;
import javax.faces.flow.Flow;
import javax.faces.flow.FlowCallNode;
import javax.faces.flow.FlowHandler;
import javax.faces.flow.FlowNode;
import javax.faces.flow.MethodCallNode;
import javax.faces.flow.Parameter;
import javax.faces.flow.ReturnNode;
import javax.faces.flow.SwitchNode;
import javax.faces.flow.ViewNode;
import javax.faces.lifecycle.ClientWindow;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

public class TestServlet extends HttpTCKServlet {

  public void facesFLowHandlerAddFlowNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    FacesContext context = getFacesContext();
    Application app = context.getApplication();
    FlowHandler fh = app.getFlowHandler();

    if (fh == null) {
      pw.println(JSFTestUtil.FAIL + " getFlowHandler returns null!");
      return;
    }

    // Test for addFlow(null, Flow)
    JSFTestUtil.checkForNPE(fh, "addFlow",
        new Class<?>[] { FacesContext.class, Flow.class },
        new Object[] { null, new TckFlow() }, pw);

    // Test for addFlow(FacesContext, null)
    JSFTestUtil.checkForNPE(fh, "addFlow",
        new Class<?>[] { FacesContext.class, Flow.class },
        new Object[] { context, null }, pw);

  }// End facesFLowHandlerAddFlowNPETest

  public void facesFLowHandlerGetFlowNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    FacesContext context = getFacesContext();
    Application app = context.getApplication();
    FlowHandler fh = app.getFlowHandler();

    if (fh == null) {
      pw.println(JSFTestUtil.FAIL + " getFlowHandler returns null!");
      return;
    }

    // Test for getFlow(null, String, String)
    JSFTestUtil.checkForNPE(fh, "getFlow",
        new Class<?>[] { FacesContext.class, String.class, String.class },
        new Object[] { null, "appOne", "flow-a" }, pw);

    // Test for getFlow(FacesContext, null, String)
    JSFTestUtil.checkForNPE(fh, "getFlow",
        new Class<?>[] { FacesContext.class, String.class, String.class },
        new Object[] { context, null, "flow-a" }, pw);

    // Test for getFlow(FacesContext, null, String)
    JSFTestUtil.checkForNPE(fh, "getFlow",
        new Class<?>[] { FacesContext.class, String.class, String.class },
        new Object[] { context, "appOne", null }, pw);

  }// End facesFLowHandlerGetFlowNPETest

  public void facesFLowHandlerGetCurrentFlowNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    FlowHandler fh = getFacesContext().getApplication().getFlowHandler();

    if (fh == null) {
      pw.println(JSFTestUtil.FAIL + " getFlowHandler returns null!");
      return;
    }

    // Test for addFlow(null)
    JSFTestUtil.checkForNPE(fh, "getCurrentFlow",
        new Class<?>[] { FacesContext.class }, new Object[] { null }, pw);

  }// End facesFLowHandlerGetCurrentFlowNPETest

  public void facesFLowHandlerTransitionNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    FlowHandler fh = getFacesContext().getApplication().getFlowHandler();

    if (fh == null) {
      pw.println(JSFTestUtil.FAIL + " getFlowHandler returns null!");
      return;
    }

    // .transition(null, Class, Class, Class, String)
    JSFTestUtil.checkForNPE(fh, "transition",
        new Class<?>[] { FacesContext.class, Flow.class, Flow.class,
            FlowCallNode.class, String.class },
        new Object[] { null, new TckFlow(), new TckFlow(),
            new TckFlowCallNode(), "flow" },
        pw);

    // .transition(FacesContext, Class, Class, Class, null)
    JSFTestUtil.checkForNPE(fh, "transition",
        new Class<?>[] { FacesContext.class, Flow.class, Flow.class,
            FlowCallNode.class, String.class },
        new Object[] { getFacesContext(), new TckFlow(), new TckFlow(),
            new TckFlowCallNode(), null },
        pw);

  }// End facesFLowHandlerTransitionNPETest

  public void facesFLowHandlerIsActiveNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    FacesContext context = getFacesContext();
    FlowHandler fh = context.getApplication().getFlowHandler();

    if (fh == null) {
      pw.println(JSFTestUtil.FAIL + " getFlowHandler returns null!");
      return;
    }

    // Test for addFlow(null, String, String)
    JSFTestUtil.checkForNPE(fh, "isActive",
        new Class<?>[] { FacesContext.class, String.class, String.class },
        new Object[] { null, "definingDocument", "id" }, pw);

    // Test for addFlow(FacesContext, null, String)
    JSFTestUtil.checkForNPE(fh, "isActive",
        new Class<?>[] { FacesContext.class, String.class, String.class },
        new Object[] { context, null, "id" }, pw);

    // Test for addFlow(FacesContext, String, null)
    JSFTestUtil.checkForNPE(fh, "isActive",
        new Class<?>[] { FacesContext.class, String.class, String.class },
        new Object[] { context, "definingDocument", null }, pw);

  }// End facesFLowHandlerIsActiveNPETest

  public void facesFLowHandlerAddFlowIAETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    FacesContext context = getFacesContext();
    Application app = context.getApplication();
    FlowHandler fh = app.getFlowHandler();

    if (fh == null) {
      pw.println(JSFTestUtil.FAIL + " getFlowHandler returns null!");
      return;
    }

    // Test to see if the id of the provided Flow is null.
    this.checkForIAE(context, fh, new TckFlow(), pw);

    // Test to see if the id of the provided Flow is an empty String.
    this.checkForIAE(context, fh, new TckFlowTwo(), pw);

    // Test to see if the definingDocumentId of the provided Flow is null.
    this.checkForIAE(context, fh, new TckFlowThree(), pw);

  }// End facesFLowHandlerAddFlowIAETest

  public void facesFLowHandlerClientWindowTransitionNPETest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    FlowHandler fh = getFacesContext().getApplication().getFlowHandler();

    if (fh == null) {
      pw.println(JSFTestUtil.FAIL + " getFlowHandler returns null!");
      return;
    }

    // Test for addFlow(null)
    JSFTestUtil.checkForNPE(fh, "clientWindowTransition",
        new Class<?>[] { FacesContext.class }, new Object[] { null }, pw);

  }// End facesFLowHandlerClientWindowTransitionNPETest

  public void facesFLowHandlerGetLastDisplayedViewIdNPETest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    FlowHandler fh = getFacesContext().getApplication().getFlowHandler();

    if (fh == null) {
      pw.println(JSFTestUtil.FAIL + " getFlowHandler returns null!");
      return;
    }

    // Test for addFlow(null)
    JSFTestUtil.checkForNPE(fh, "getLastDisplayedViewId",
        new Class<?>[] { FacesContext.class }, new Object[] { null }, pw);

  }// End facesFLowHandlerGetLastDisplayedViewIdNPETest

  // ------------------------------ private methods
  private void checkForIAE(FacesContext context, FlowHandler handler, Flow flow,
      PrintWriter pw) {

    try {
      handler.addFlow(context, flow);
      pw.println(JSFTestUtil.FAIL + JSFTestUtil.NL + "No Exception thrown!"
          + JSFTestUtil.NL + "Expected: IllegalArgumentException");

    } catch (IllegalArgumentException iea) {
      // do nothing test passes
      pw.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      pw.println(JSFTestUtil.FAIL + JSFTestUtil.NL + "Wrong Exception"
          + "Thrown!" + JSFTestUtil.NL + "Expected: IllegalArgumentException"
          + JSFTestUtil.NL + "Received: " + e.getClass().getSimpleName());
    }
  }

  // ------------------------------ private classes

  private static class TckFlow extends Flow {

    @Override
    public String getId() {
      return null;
    }

    @Override
    public String getDefiningDocumentId() {
      return "notNull";
    }

    @Override
    public String getStartNodeId() {
      return null;
    }

    @Override
    public MethodExpression getFinalizer() {
      return null;
    }

    @Override
    public MethodExpression getInitializer() {
      return null;
    }

    @Override
    public Map<String, Parameter> getInboundParameters() {
      return null;
    }

    @Override
    public List<ViewNode> getViews() {
      return null;
    }

    @Override
    public Map<String, ReturnNode> getReturns() {
      return null;
    }

    @Override
    public Map<String, SwitchNode> getSwitches() {
      return null;
    }

    @Override
    public Map<String, FlowCallNode> getFlowCalls() {
      return null;
    }

    @Override
    public FlowCallNode getFlowCall(Flow targetFlow) {
      return null;
    }

    @Override
    public List<MethodCallNode> getMethodCalls() {
      return null;
    }

    @Override
    public FlowNode getNode(String nodeId) {
      return null;
    }

    @Override
    public String getClientWindowFlowId(ClientWindow curWindow) {
      return null;
    }

    @Override
    public Map<String, Set<NavigationCase>> getNavigationCases() {
      return null;
    }

  }// End TckFlow

  private static class TckFlowTwo extends Flow {

    @Override
    public String getId() {
      return "";
    }

    @Override
    public String getDefiningDocumentId() {
      return "TckFlowTwo";
    }

    @Override
    public String getStartNodeId() {
      return null;
    }

    @Override
    public MethodExpression getFinalizer() {
      return null;
    }

    @Override
    public MethodExpression getInitializer() {
      return null;
    }

    @Override
    public Map<String, Parameter> getInboundParameters() {
      return null;
    }

    @Override
    public List<ViewNode> getViews() {
      return null;
    }

    @Override
    public Map<String, ReturnNode> getReturns() {
      return null;
    }

    @Override
    public Map<String, SwitchNode> getSwitches() {
      return null;
    }

    @Override
    public Map<String, FlowCallNode> getFlowCalls() {
      return null;
    }

    @Override
    public FlowCallNode getFlowCall(Flow targetFlow) {
      return null;
    }

    @Override
    public List<MethodCallNode> getMethodCalls() {
      return null;
    }

    @Override
    public FlowNode getNode(String nodeId) {
      return null;
    }

    @Override
    public String getClientWindowFlowId(ClientWindow curWindow) {
      return null;
    }

    @Override
    public Map<String, Set<NavigationCase>> getNavigationCases() {
      return null;
    }

  }// End TckFlowTwo

  private static class TckFlowThree extends Flow {

    @Override
    public String getId() {
      return "123";
    }

    @Override
    public String getDefiningDocumentId() {
      return null;
    }

    @Override
    public String getStartNodeId() {
      return null;
    }

    @Override
    public MethodExpression getFinalizer() {
      return null;
    }

    @Override
    public MethodExpression getInitializer() {
      return null;
    }

    @Override
    public Map<String, Parameter> getInboundParameters() {
      return null;
    }

    @Override
    public List<ViewNode> getViews() {
      return null;
    }

    @Override
    public Map<String, ReturnNode> getReturns() {
      return null;
    }

    @Override
    public Map<String, SwitchNode> getSwitches() {
      return null;
    }

    @Override
    public Map<String, FlowCallNode> getFlowCalls() {
      return null;
    }

    @Override
    public FlowCallNode getFlowCall(Flow targetFlow) {
      return null;
    }

    @Override
    public List<MethodCallNode> getMethodCalls() {
      return null;
    }

    @Override
    public FlowNode getNode(String nodeId) {
      return null;
    }

    @Override
    public String getClientWindowFlowId(ClientWindow curWindow) {
      return null;
    }

    @Override
    public Map<String, Set<NavigationCase>> getNavigationCases() {
      return null;
    }

  }// End TckFlowThree

  private static class TckFlowCallNode extends FlowCallNode {

    @Override
    public Map<String, Parameter> getOutboundParameters() {
      return null;
    }

    @Override
    public String getCalledFlowDocumentId(FacesContext context) {
      return null;
    }

    @Override
    public String getCalledFlowId(FacesContext context) {
      return null;
    }

    @Override
    public String getId() {
      return null;
    }

  } // End TckFlowCallNode
}
