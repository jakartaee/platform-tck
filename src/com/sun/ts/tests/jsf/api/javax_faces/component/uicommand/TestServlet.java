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

package com.sun.ts.tests.jsf.api.javax_faces.component.uicommand;

import java.io.IOException;
import java.io.PrintWriter;

import javax.faces.FactoryFinder;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jsf.api.javax_faces.component.common.BaseComponentTestServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

public class TestServlet extends BaseComponentTestServlet {

  /**
   * <p>
   * Initializes this {@link javax.servlet.Servlet}.
   * </p>
   * 
   * @param config
   *          this Servlet's configuration
   * @throws ServletException
   *           if an error occurs
   */
  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    setRendererType("javax.faces.Button");
  }

  /**
   * <p>
   * Creates a new {@link UIComponent} instance.
   * </p>
   * 
   * @return a new {@link UIComponent} instance.
   */
  @Override
  protected UIComponentBase createComponent() {
    return new UICommand();
  }

  // ------------------------------------------- Test Methods ----

  // -------------------------------------------------------------------
  // UICommand

  public void uiCommandSetGetValueTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UICommand command = (UICommand) createComponent();

    command.setValue("value");

    if (!"value".equals(command.getValue())) {
      out.println(JSFTestUtil.FAIL + " UICommand.getValue() didn't return"
          + " the value as set by UICommand.setValue().");
      out.println("Expected: value");
      out.println("Received: " + command.getValue());
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void uiCommandBroadcastTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    // Ensure listeners are invoked in the proper order
    // and are invoked during the invoke application phase
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    UICommand command = (UICommand) createComponent();
    command.setRendererType(null);

    ActionEvent event = new ActionEvent(command);
    TestActionListener.trace(null);

    // Register three listeners
    command.addActionListener(TestActionListener.withID("AP0"));
    command.addActionListener(TestActionListener.withID("AP1"));
    command.addActionListener(TestActionListener.withID("AP2"));

    UIViewRoot root = new UIViewRoot();
    root.getChildren().add(command);
    command.queueEvent(event);
    root.processDecodes(context);
    root.processValidators(context);
    root.processApplication(context);

    String traceExpected = "/AP0@INVOKE_APPLICATION/AP1@INVOKE_APPLICATION/AP2@INVOKE_APPLICATION";
    String traceReceived = TestActionListener.trace();
    if (!traceExpected.equals(traceReceived)) {
      out.println(JSFTestUtil.FAIL + " Listeners not invoked in the expected"
          + " order or were invoked too many times.");
      out.println("Listener trace expected: " + traceExpected);
      out.println("Listener trace received: " + traceReceived);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void uiCommandBroadcastImmediateTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    // Ensure listeners are invoked in the proper order
    // and since immediate is true, the listeners
    // are processed during APPLY_REQUEST_VALUES
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    UICommand command = (UICommand) createComponent();
    command.setImmediate(true);
    command.setRendererType(null);

    ActionEvent event = new ActionEvent(command);
    TestActionListener.trace(null);
    // Register three listeners
    command.addActionListener(TestActionListener.withID("AP0"));
    command.addActionListener(TestActionListener.withID("AP1"));
    command.addActionListener(TestActionListener.withID("AP2"));

    UIViewRoot root = new UIViewRoot();
    root.getChildren().add(command);
    command.queueEvent(event);
    root.processDecodes(context);
    root.processValidators(context);
    root.processApplication(context);

    String traceExpected = "/AP0@APPLY_REQUEST_VALUES/AP1@APPLY_REQUEST_VALUES/AP2@APPLY_REQUEST_VALUES";
    String traceReceived = TestActionListener.trace();
    if (!traceExpected.equals(traceReceived)) {
      out.println(JSFTestUtil.FAIL + " Listeners not invoked in the expected"
          + " order or were invoked too many times.");
      out.println("Listener trace expected: " + traceExpected);
      out.println("Listener trace received: " + traceReceived);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void uiCommandBroadcastInvokeActionListenerTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // Ensure that for instances of UICommand, that any actionListenerRefs
    // that are associated with this instance are invoked.
    PrintWriter out = response.getWriter();
    UICommand command = (UICommand) createComponent();
    command.setRendererType(null);
    FacesContext context = getFacesContext();
    System.out.println("COMMAND CLASS: " + command.getClass().getName());

    // Defaults to ANY_PHASE
    ActionEvent event = new ActionEvent(command);
    TestActionListener listener = TestActionListener.withID("ALR");

    request.setAttribute("ListRef", listener);

    command.setActionListener(getApplication().createMethodBinding(
        "#{ListRef.processAction}", new Class[] { ActionEvent.class }));
    command.setImmediate(true);
    TestActionListener.trace(null);

    UIViewRoot root = new UIViewRoot();
    root.getChildren().add(command);
    command.queueEvent(event);
    root.processDecodes(context);
    root.processValidators(context);
    root.processApplication(context);

    String traceExpected = "/ALR@APPLY_REQUEST_VALUES";
    String traceReceived = TestActionListener.trace();
    if (!traceExpected.equals(traceReceived)) {
      out.println(JSFTestUtil.FAIL + " Listeners defined via actionListenerRefs"
          + "not invoked.");
      out.println("Listener trace expected: " + traceExpected);
      out.println("Listener trace received: " + traceReceived);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // UICommand.broadcast(FacesContext) throws NullPointerException
  public void uiCommandBroadcastNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    UICommand command = (UICommand) createComponent();
    command.setRendererType(null);

    try {
      command.broadcast(null);
      out.println(JSFTestUtil.FAIL
          + "Expected a NullPointerException to be thrown and it wasn't!");

    } catch (NullPointerException npe) {
      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + "Wrong Exception Thrown!" + JSFTestUtil.NL
          + "Expected: NullPointerException" + JSFTestUtil.NL + "Received: "
          + e.toString());
    }

  }

  // UIComponent.processDecodes()
  public void uiComponentProcessDecodesTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    // Setup a specialized RenderKit
    RenderKitFactory renderKitFactory = (RenderKitFactory) FactoryFinder
        .getFactory(FactoryFinder.RENDER_KIT_FACTORY);
    RenderKit renderKit = renderKitFactory.getRenderKit(getFacesContext(),
        RenderKitFactory.HTML_BASIC_RENDER_KIT);
    TCKRenderer renderer = new TCKRenderer();
    renderer.setRendersChildren(true);
    renderKit.addRenderer("TCK", "TCK", renderer);

    // build a component tree.
    UIComponent comp = createComponent();
    comp.setRendererType(null);
    comp.setRendered(true);
    TCKInputComponent child1 = new TCKInputComponent("child1");
    TCKComponent child2 = new TCKComponent("child2");
    child2.setRendererType("TCK");
    child2.setRendered(false);
    child1.setRendered(true);
    TCKComponent child1_1 = new TCKComponent("child1_1");
    child1_1.setRendered(true);
    TCKComponent facet1 = new TCKComponent("facet1");
    facet1.setRendered(true);
    TCKInputComponent facet1_1 = new TCKInputComponent("facet1_1");
    facet1_1.setRendered(true);

    child1_1.getFacets().put("facet1_1", facet1_1);
    child1.getFacets().put("facet1", facet1);
    child1.getChildren().add(child1_1);
    comp.getChildren().add(child1);
    comp.getChildren().add(child2);

    comp.processDecodes(getFacesContext());

    // Child2 shoudln't have D in the trace as it's parent
    // renders children but isn't rendered itself.
    if (!"/PDchild1/Dchild1".equals(child1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + child1.getTrace());
      out.println("Expected trace: /PDchild1/Dchild1");
      return;
    }

    if (!"/PDchild1_1/Dchild1_1".equals(child1_1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + child1_1.getTrace());
      out.println("Expected trace: /PDchild1_1/Dchild1_1");
      return;
    }
    if (!"/PDfacet1/Dfacet1".equals(facet1.getTrace())) {
      out.println("Test FAILED.");
      out.println("Component trace: " + facet1.getTrace());
      out.println("Expected trace: /PDfacet1/Dfacet1");
      return;
    }

    if (!"/PDfacet1_1/Dfacet1_1".equals(facet1_1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + facet1_1.getTrace());
      out.println("Expected trace: /PDfacet1_1/Dfacet1_1");
      return;
    }

    if (!"/PDchild2".equals(child2.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + facet1_1.getTrace());
      out.println("Expected trace: /PDchild2");
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.processDecodes() -- decode() not called if component is
  // not rendered
  public void uiComponentProcessDecodesNotRenderedTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    // build a component tree.
    UIComponent comp = createComponent();
    comp.setRendered(true);
    comp.setRendererType(null);
    TCKInputComponent child1 = new TCKInputComponent("child1");
    child1.setRendered(true);
    TCKComponent child1_1 = new TCKComponent("child1_1");
    child1_1.setRendered(false);
    TCKInputComponent facet1 = new TCKInputComponent("facet1");
    facet1.setRendered(true);
    TCKInputComponent facet1_1 = new TCKInputComponent("facet1_1");
    facet1_1.setRendered(false);

    child1_1.getFacets().put("facet1_1", facet1_1);
    child1.getFacets().put("facet1", facet1);
    child1.getChildren().add(child1_1);
    comp.getChildren().add(child1);

    comp.processDecodes(getFacesContext());

    // child1_1 and facet1_1 have their rendered properties set to false.
    // This means that child1 will have processDecodes called against it,
    // but it will not call decode() on facet1_1.
    if (!"/PDchild1/Dchild1".equals(child1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + child1.getTrace());
      out.println("Expected trace: /PDchild1/Dchild1");
      return;
    }

    if (!"/PDchild1_1".equals(child1_1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + child1_1.getTrace());
      out.println("Expected trace: /PDchild1_1.");
      return;
    }
    if (!"/PDfacet1/Dfacet1".equals(facet1.getTrace())) {
      out.println("Test FAILED.");
      out.println("Component trace: " + facet1.getTrace());
      out.println("Expected trace: /PDfacet1/Dfacet1");
      return;
    }

    if (!"".equals(facet1_1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + facet1_1.getTrace());
      out.println("Expected no trace to be generated.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.processDecodes() - if RuntimeException thrown by decode()
  // then FacesContext.renderResponse must be called
  public void uiComponentProcessDecodesRenderResponseTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIComponent comp = createComponent();
    comp.setRendererType(null);
    TCKInputComponent tckInput = new TCKInputComponent("in1", true);
    comp.getChildren().add(tckInput);

    try {
      comp.processDecodes(getFacesContext());
    } catch (Exception re) {
      if (!"DecodeRTE".equals(re.getMessage())) {
        out.println(JSFTestUtil.FAIL + " RuntimeException not rethrown up to"
            + " the calling component.");
        out.println("Exception received: " + re.toString());
        out.println("Exception message: " + re.getMessage());
        return;
      }
    }

    if (!getFacesContext().getRenderResponse()) {
      out.println(JSFTestUtil.FAIL + " RuntimeException thrown during decode()"
          + " but FacesContext.renderResponse() was not called.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }
}
