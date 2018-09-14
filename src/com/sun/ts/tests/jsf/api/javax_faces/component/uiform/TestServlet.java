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

package com.sun.ts.tests.jsf.api.javax_faces.component.uiform;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

import javax.faces.FactoryFinder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;
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
    setRendererType("javax.faces.Form");
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
    UIForm form = new UIForm();
    form.setSubmitted(true);
    return form;
  }

  protected UIComponent createForm(boolean submitted) {
    UIForm form = new UIForm();
    form.setSubmitted(submitted);
    return form;
  }

  // ---------------------------------------------------------------- Test
  // Methods

  // UIForm.{is,set}Submitted()
  public void uiFormIsSetSubmittedTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIForm form = new UIForm();

    // default return value for isSubmitted() is false
    if (form.isSubmitted()) {
      out.println(JSFTestUtil.FAIL + " Expected the default return value"
          + " of UIForm.isSubmitted() to be false.");
      return;
    }

    form.setSubmitted(true);
    if (!form.isSubmitted()) {
      out.println(JSFTestUtil.FAIL + " Expected UIForm.isSubmitted to return"
          + " true after having explicitly set it as such via"
          + " UIForm.setSubmitted().");
      return;
    }

    form.setSubmitted(false);
    if (form.isSubmitted()) {
      out.println(JSFTestUtil.FAIL + " Expected UIForm.isSubmitted to return"
          + " false after having explicitly set it as such via"
          + " UIForm.setSubmitted().");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // StateHolder.saveState(), StateHolder.restoreState()
  // Override parent test - ensure that setSubmitted is set to false
  // what save state is called.
  public void stateHolderSaveRestoreStateTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    // Begin test setup

    UIForm preSave = (UIForm) createComponent();
    preSave.setSubmitted(true);
    UIComponent facet1 = new UIOutput();
    facet1.setId("facet1");
    preSave.getFacets().put("facet1 key", facet1);
    UIComponent facet2 = new UIOutput();
    facet2.setId("facet2");
    preSave.getFacets().put("facet2 key", facet2);
    populateComponent(preSave);

    // Save and restore state and compare the results
    Object state = preSave.saveState(getFacesContext());

    // 1.1 saveState() no longer resets submitted property.
    if (!preSave.isSubmitted()) {
      out.println(JSFTestUtil.FAIL + " Expected the return value of "
          + "UIForm.isSubmitted() to return true after having"
          + " called saveState().");
      return;
    }

    if (state == null) {
      out.println(JSFTestUtil.FAIL + " saveState() failed to returned null");
      return;
    }

    if (!(state instanceof Serializable)) {
      out.println(JSFTestUtil.FAIL + " The Object returned by saveState() was"
          + " not an instance of java.io.Serializable.");
      return;
    }

    UIComponent postSave = createForm(false);
    postSave.restoreState(getFacesContext(), state);

    // information pertaining to the submit state of the form must not
    // have been serialized, therefore, isSubmitted() will return false
    if (((UIForm) postSave).isSubmitted()) {
      out.println(JSFTestUtil.FAIL + " Expected the return value of "
          + "UIForm.isSubmitted() to return false after having"
          + " called restoreState().");
      return;
    }

    StringBuffer buf = new StringBuffer(128);

    checkComponents(preSave.findComponent("componentId"),
        postSave.findComponent("componentId"), buf);

    if (buf.length() > 0) {
      out.println(buf.toString());
    } else {
      out.println(JSFTestUtil.PASS);
    }
  }

  // Ensure that the Form is decoded first, then call into super
  // test class for verification of standard behavior
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

    // build a component tree.
    UIComponent comp = createComponent();
    Renderer origRenderer = renderKit.getRenderer(comp.getFamily(),
        UIForm.COMPONENT_TYPE);
    renderKit.addRenderer(comp.getFamily(), UIForm.COMPONENT_TYPE, renderer);
    renderKit.addRenderer("TCK", "TCK", renderer);
    comp.setId("form");
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

    String result = renderer.getTrace();

    if (!result.startsWith("/DCform")) {
      out.println(JSFTestUtil.FAIL + " Expected Form component to be decoded"
          + " before children components.");
      out.println("Expected renderer trace to begin with '/DCform'");
      out.println("Actual trace: " + result);
      return;
    }

    comp = createComponent();
    comp.setRendered(true);
    TCKInputComponent cchild1 = new TCKInputComponent("child1");
    TCKComponent cchild2 = new TCKComponent("child2");
    cchild2.setRendererType("TCK");
    cchild2.setRendered(false);
    cchild1.setRendered(true);
    TCKComponent cchild1_1 = new TCKComponent("child1_1");
    cchild1_1.setRendered(true);
    TCKComponent ffacet1 = new TCKComponent("facet1");
    ffacet1.setRendered(true);
    TCKInputComponent ffacet1_1 = new TCKInputComponent("facet1_1");
    ffacet1_1.setRendered(true);

    cchild1_1.getFacets().put("facet1_1", ffacet1_1);
    cchild1.getFacets().put("facet1", ffacet1);
    cchild1.getChildren().add(cchild1_1);
    comp.getChildren().add(cchild1);
    comp.getChildren().add(cchild2);

    comp.processDecodes(getFacesContext());

    // Child2 shoudln't have D in the trace as it's parent
    // renders children but isn't rendered itself.
    if (!"/PDchild1/Dchild1".equals(cchild1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + cchild1.getTrace());
      out.println("Expected trace: /PDchild1/Dchild1");
      return;
    }

    if (!"/PDchild1_1/Dchild1_1".equals(cchild1_1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + cchild1_1.getTrace());
      out.println("Expected trace: /PDchild1_1/Dchild1_1");
      return;
    }
    if (!"/PDfacet1/Dfacet1".equals(ffacet1.getTrace())) {
      out.println("Test FAILED.");
      out.println("Component trace: " + ffacet1.getTrace());
      out.println("Expected trace: /PDfacet1/Dfacet1");
      return;
    }

    if (!"/PDfacet1_1/Dfacet1_1".equals(ffacet1_1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + ffacet1_1.getTrace());
      out.println("Expected trace: /PDfacet1_1/Dfacet1_1");
      return;
    }

    if (!"/PDchild2".equals(cchild2.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + ffacet1_1.getTrace());
      out.println("Expected trace: /PDchild2");
      return;
    }

    out.println(JSFTestUtil.PASS);

    renderKit.addRenderer(comp.getFamily(), UIForm.COMPONENT_TYPE,
        origRenderer);

    // super.uiComponentProcessDecodesTest(request, response);
  }

  // UIComponent.processDecodes() -- decode() not called if component is
  // not rendered
  public void uiComponentProcessDecodesNotRenderedTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    // Setup a specialized RenderKit
    RenderKitFactory renderKitFactory = (RenderKitFactory) FactoryFinder
        .getFactory(FactoryFinder.RENDER_KIT_FACTORY);
    RenderKit renderKit = renderKitFactory.getRenderKit(getFacesContext(),
        RenderKitFactory.HTML_BASIC_RENDER_KIT);
    TCKRenderer renderer = new TCKRenderer();
    renderer.setRendersChildren(true);

    // build a component tree.
    UIComponent comp = createComponent();
    Renderer origRenderer = renderKit.getRenderer(comp.getFamily(),
        UIForm.COMPONENT_TYPE);
    renderKit.addRenderer(comp.getFamily(), UIForm.COMPONENT_TYPE, renderer);
    comp.setRendered(true);
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

    renderKit.addRenderer(comp.getFamily(), UIForm.COMPONENT_TYPE,
        origRenderer);
  }

  // UIComponent.processDecodes() - if RuntimeException thrown by decode()
  // then FacesContext.renderResponse must be called
  public void uiComponentProcessDecodesRenderResponseTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    // Setup a specialized RenderKit
    RenderKitFactory renderKitFactory = (RenderKitFactory) FactoryFinder
        .getFactory(FactoryFinder.RENDER_KIT_FACTORY);
    RenderKit renderKit = renderKitFactory.getRenderKit(getFacesContext(),
        RenderKitFactory.HTML_BASIC_RENDER_KIT);
    TCKRenderer renderer = new TCKRenderer();
    renderer.setRendersChildren(true);

    UIComponent comp = createComponent();
    Renderer origRenderer = renderKit.getRenderer(comp.getFamily(),
        UIForm.COMPONENT_TYPE);
    renderKit.addRenderer(comp.getFamily(), UIForm.COMPONENT_TYPE, renderer);
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

    renderKit.addRenderer(comp.getFamily(), UIForm.COMPONENT_TYPE,
        origRenderer);
  }

  // UIForm.processUpdates() only performed on children if isSubmitted()
  // returns true
  public void uiFormProcessUpdatesIsSubmittedTest(HttpServletRequest request,
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
    UIForm comp = (UIForm) createComponent();
    comp.setId("form");
    comp.setRendered(true);
    comp.setSubmitted(true);
    TCKInputComponent child1 = new TCKInputComponent("child1");
    TCKComponent child2 = new TCKComponent("child2");
    child2.setRendererType("TCK");
    child2.setRendered(true);
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

    comp.processUpdates(getFacesContext());

    if (!"/PUchild1/UMchild1".equals(child1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + child1.getTrace());
      out.println("Expected trace: /PUchild1/UMchild1");
      return;
    }

    if (!"/PUchild2".equals(child2.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + child2.getTrace());
      out.println("Expected trace: /PUchild2");
      return;
    }

    if (!"/PUchild1_1".equals(child1_1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + child1_1.getTrace());
      out.println("Expected trace: /PUchild1");
      return;
    }

    if (!"/PUfacet1".equals(facet1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + facet1.getTrace());
      out.println("Expected trace: /PUfacet1");
      return;
    }

    if (!"/PUfacet1_1/UMfacet1_1".equals(facet1_1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + facet1_1.getTrace());
      out.println("Expected trace: /PUfacet1_1/UMfacet1_1");
      return;
    }

    // rebuild the tree and set the submitted property of the form
    // to false. Child components should not be touched.
    child1.resetTraceLog();
    child2.resetTraceLog();
    child1_1.resetTraceLog();
    facet1.resetTraceLog();
    facet1_1.resetTraceLog();

    comp.setSubmitted(false);

    comp.processUpdates(getFacesContext());

    if (!"".equals(child1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + child1.getTrace());
      out.println("Expected no trace");
      return;
    }

    if (!"".equals(child2.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + child2.getTrace());
      out.println("Expected no trace");
      return;
    }

    if (!"".equals(child1_1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + child1_1.getTrace());
      out.println("Expected no trace");
      return;
    }

    if (!"".equals(facet1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + facet1.getTrace());
      out.println("Expected no trace");
      return;
    }

    if (!"".equals(facet1_1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + facet1_1.getTrace());
      out.println("Expected no trace");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIForm.processValidators() only performed on children if isSubmitted()
  // returns true
  public void uiFormProcessValidatorsIsSubmittedTest(HttpServletRequest request,
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
    UIForm comp = (UIForm) createComponent();
    comp.setId("form");
    comp.setRendered(true);
    comp.setSubmitted(true);
    TCKInputComponent child1 = new TCKInputComponent("child1");
    TCKComponent child2 = new TCKComponent("child2");
    child2.setRendererType("TCK");
    child2.setRendered(true);
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

    comp.processValidators(getFacesContext());

    if (!"/PVchild1/vchild1".equals(child1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + child1.getTrace());
      out.println("Expected trace: /PVchild1/Vchild1");
      return;
    }

    if (!"/PVchild2".equals(child2.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + child2.getTrace());
      out.println("Expected trace: /PVchild2");
      return;
    }

    if (!"/PVchild1_1".equals(child1_1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + child1_1.getTrace());
      out.println("Expected trace: /PVchild1");
      return;
    }

    if (!"/PVfacet1".equals(facet1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + facet1.getTrace());
      out.println("Expected trace: /PVfacet1");
      return;
    }

    if (!"/PVfacet1_1/Vfacet1_1".equals(facet1_1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + facet1_1.getTrace());
      out.println("Expected trace: /PVfacet1_1/Vfacet1_1");
      return;
    }

    // rebuild the tree and set the submitted property of the form
    // to false. Child componentns should not be touched.
    child1.resetTraceLog();
    child2.resetTraceLog();
    child1_1.resetTraceLog();
    facet1.resetTraceLog();
    facet1_1.resetTraceLog();

    comp.setSubmitted(false);

    comp.processValidators(getFacesContext());

    if (!"".equals(child1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + child1.getTrace());
      out.println("Expected no trace");
      return;
    }

    if (!"".equals(child2.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + child2.getTrace());
      out.println("Expected no trace");
      return;
    }

    if (!"".equals(child1_1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + child1_1.getTrace());
      out.println("Expected no trace");
      return;
    }

    if (!"".equals(facet1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + facet1.getTrace());
      out.println("Expected no trace");
      return;
    }

    if (!"".equals(facet1_1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + facet1_1.getTrace());
      out.println("Expected no trace");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIForm.createUniqueId()
  public void uiFormCreateUniqueIdTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();

    String seed = "tckId";
    UIForm form = (UIForm) createComponent();
    String uniqueId = form.createUniqueId(context, seed);

    if (!uniqueId.contains(seed)) {
      out.println(JSFTestUtil.FAIL + "Test Value not found in ID!"
          + JSFTestUtil.NL + "Expected Component ID to contain: " + seed
          + JSFTestUtil.NL + "Received: " + uniqueId);

    } else {
      out.println(JSFTestUtil.PASS);
    }

  } // End uiFormCreateUniqueIdTest

  // UIForm.{is,set}PrependId()
  public void uiFormIsSetPrependIdTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIForm form = (UIForm) createComponent();

    form.setPrependId(false);
    boolean result = form.isPrependId();
    if (result) {
      out.println(JSFTestUtil.FAIL
          + " Unexpected value returned from UIForm.isPrependId() !"
          + JSFTestUtil.NL + "Expected: false" + JSFTestUtil.NL + "Received: "
          + result);

    } else {
      out.println(JSFTestUtil.PASS);
    }
  }// end uiFormIsSetPrependIdTest

  // ------------------------------------------------------------- Private
  // Classes

  private static class TCKRenderer extends Renderer {

    private static StringBuffer traceLog;

    private boolean rendersChildren = false;

    private Renderer wrapped;

    /**
     * <p>
     * Creates a new <code>TCKRenderer</code> instance that calls into the
     * {@link Renderer} parent class.
     * </p>
     */
    public TCKRenderer() {
      traceLog = new StringBuffer();
    }

    /**
     * <p>
     * Creates a new <code>TCKRenderer</code> that wraps the provided
     * {@link Renderer}. All calls will be delegated to the wrapped instance.
     * </p>
     * 
     * @param wrapped
     *          the {@link Renderer} to delegate calls to
     */
    public TCKRenderer(Renderer wrapped) {
      this();
      this.wrapped = wrapped;
    }

    /**
     * <p>
     * Render the ending of the current state of the specified
     * {@link UIComponent}, following the rules described for
     * <code>encodeBegin()</code> to acquire the appropriate value to be
     * rendered.
     * </p>
     * 
     * @param context
     *          {@link FacesContext} for the response we are creating
     * @param component
     *          {@link UIComponent} to be rendered
     * @throws IOException
     *           if an input/output error occurs while rendering
     * @throws NullPointerException
     *           if <code>context</code> or <code>component</code> is
     *           <code>null</code>
     */
    public void encodeEnd(FacesContext context, UIComponent component)
        throws IOException {
      if (context == null || component == null) {
        throw new NullPointerException();
      }
      traceLog.append("/EE");
      if (wrapped != null) {
        wrapped.encodeEnd(context, component);
      } else {
        super.encodeEnd(context, component);
      }
    }

    /**
     * <p>
     * Render the child components of this {@link UIComponent}, following the
     * rules described for <code>encodeBegin()</code> to acquire the appropriate
     * value to be rendered. This method will only be called if the
     * <code>rendersChildren</code> property of this component is
     * <code>true</code>.
     * </p>
     * 
     * @param context
     *          {@link FacesContext} for the response we are creating
     * @param component
     *          {@link UIComponent} whose children are to be rendered
     * @throws IOException
     *           if an input/output error occurs while rendering
     * @throws NullPointerException
     *           if <code>context</code> or <code>component</code> is
     *           <code>null</code>
     */
    public void encodeChildren(FacesContext context, UIComponent component)
        throws IOException {

      if (context == null || component == null) {
        throw new NullPointerException();
      }
      traceLog.append("/EC");
      if (wrapped != null) {
        wrapped.encodeChildren(context, component);
      } else {
        super.encodeChildren(context, component);
      }
    }

    /**
     * <p>
     * Convert the component generated client id to a form suitable for
     * transmission to the client.
     * </p>
     * <p/>
     * <p>
     * The default implementation returns the argument <code>clientId</code>
     * unchanged.
     * </p>
     * 
     * @param context
     *          {@link FacesContext} for the current request
     * @param clientId
     *          the client identifier to be converted to client a specific
     *          format.
     * @throws NullPointerException
     *           if <code>context</code> or <code>clientId</code> is
     *           <code>null</code>
     */
    public String convertClientId(FacesContext context, String clientId) {
      if (context == null || clientId == null) {
        throw new NullPointerException();
      }
      if (wrapped != null) {
        return wrapped.convertClientId(context, clientId);
      } else {
        return super.convertClientId(context, clientId);
      }
    }

    /**
     * <p>
     * Return a flag indicating whether this renderer is responsible for
     * rendering the children the component it is asked to render. The default
     * implementation returns <code>false</code>.
     * </p>
     */

    public boolean getRendersChildren() {
      if (wrapped != null) {
        return wrapped.getRendersChildren();
      }
      return rendersChildren;
    }

    /**
     * <p>
     * Decode the current state of the specified {@link UIComponent} from the
     * request contained in the specified {@link FacesContext}, and attempt to
     * convert this state information into an object of the type required for
     * this component (optionally using the registered {@link Converter} for
     * this component, if there is one).
     * </p>
     * <p/>
     * <p>
     * If conversion is successful:
     * </p>
     * <ul>
     * <li>Save the new local value of this component by calling
     * <code>setValue()</code> and passing the new value.</li>
     * <li>Set the <code>value</code> property of this component to
     * <code>true</code>.</li>
     * </ul>
     * <p/>
     * <p>
     * If conversion is not successful:
     * </p>
     * <ul>
     * <li>Save the state information (inside the component) in such a way that
     * encoding can reproduce the previous input (even though it was
     * syntactically or semantically incorrect).</li>
     * <li>Add an appropriate conversion failure error message by calling
     * <code>addMessage()</code> on the specified {@link FacesContext}.</li>
     * <li>Set the <code>valid</code> property of this component to
     * <code>false</code>.</li>
     * </ul>
     * <p/>
     * <p>
     * During decoding, events may be enqueued for later processing (by event
     * listeners that have registered an interest), by calling
     * <code>queueEvent()</code> on the associated {@link UIComponent}.
     * </p>
     * 
     * @param context
     *          {@link FacesContext} for the request we are processing
     * @param component
     *          {@link UIComponent} to be decoded.
     * @throws NullPointerException
     *           if <code>context</code> or <code>component</code> is
     *           <code>null</code>
     */
    public void decode(FacesContext context, UIComponent component) {

      if (context == null || component == null) {
        throw new NullPointerException();
      }
      traceLog.append("/DC" + component.getId());
      if (wrapped != null) {
        wrapped.decode(context, component);
      } else {
        super.decode(context, component);
      }
    }

    /**
     * <p>
     * Render the beginning specified {@link UIComponent} to the output stream
     * or writer associated with the response we are creating. If the conversion
     * attempted in a previous call to <code>decode</code> for this component
     * failed, the state information saved during execution of
     * <code>decode()</code> should be utilized to reproduce the incorrect
     * input. If the conversion was successful, or if there was no previous call
     * to <code>decode()</code>, the value to be displayed should be acquired by
     * calling <code>component.currentValue()</code>, and rendering the value as
     * appropriate.
     * </p>
     * 
     * @param context
     *          {@link FacesContext} for the request we are processing
     * @param component
     *          {@link UIComponent} to be rendered
     * @throws IOException
     *           if an input/output error occurs while rendering
     * @throws NullPointerException
     *           if <code>context</code> or <code>component</code> is null
     */
    public void encodeBegin(FacesContext context, UIComponent component)
        throws IOException {

      if (context == null || component == null) {
        throw new NullPointerException();
      }
      traceLog.append("/EB");
      if (wrapped != null) {
        wrapped.encodeBegin(context, component);
      } else {
        super.encodeBegin(context, component);
      }
    }

    /**
     * <p>
     * Returns the content of the current trace log.
     * </p>
     */
    public String getTrace() {
      return traceLog.toString();
    }

    /**
     * <p>
     * Sets the return value of {@link #getRendersChildren()}.
     * </p>
     */
    public void setRendersChildren(boolean rendersChildren) {
      this.rendersChildren = rendersChildren;
    }
  }

  private static class TCKComponent extends UIComponentBase {

    private StringBuffer traceLog;

    private String id;

    public String getFamily() {
      return "TCK";
    }

    public TCKComponent(String id) {
      super();
      traceLog = new StringBuffer();
      this.id = id;
      this.setId(id);
    }

    public void processDecodes(FacesContext context) {
      trace("/PD" + id);
      super.processDecodes(context);
    }

    public void processRestoreState(FacesContext context, Object state) {
      trace("/PRS" + id);
      super.processRestoreState(context, state);
    }

    public Object processSaveState(FacesContext context) {
      trace("/PSS" + id);
      return super.processSaveState(context);
    }

    public void processUpdates(FacesContext context) {
      trace("/PU" + id);
      super.processUpdates(context);
    }

    public void processValidators(FacesContext context) {
      trace("/PV" + id);
      super.processValidators(context);
    }

    public Object saveState(FacesContext context) {
      trace("/SS" + id);
      return super.saveState(context);
    }

    public void restoreState(FacesContext context, Object state) {
      trace("/RS" + id);
      super.restoreState(context, state);
    }

    public void decode(FacesContext context) {
      trace("/D" + id);
      super.decode(context);
    }

    public String getTrace() {
      return traceLog.toString();
    }

    public void resetTraceLog() {
      traceLog = new StringBuffer();
    }

    private void trace(String message) {
      traceLog.append(message);
    }
  }

  private static class TCKInputComponent extends UIInput {

    private StringBuffer traceLog;

    private String id;

    private boolean throwRuntime;

    private boolean isInvalid;

    public TCKInputComponent(String id) {
      super();
      traceLog = new StringBuffer();
      this.id = id;
      this.setId(id);
    }

    public TCKInputComponent(String id, boolean throwRuntime) {
      this(id);
      this.throwRuntime = throwRuntime;
    }

    public void setInvalidOnValidate(boolean isInvalid) {
      this.isInvalid = isInvalid;
    }

    public void processDecodes(FacesContext context) {
      trace("/PD" + id);
      super.processDecodes(context);
    }

    public void processUpdates(FacesContext context) {
      trace("/PU" + id);
      super.processUpdates(context);
    }

    public void updateModel(FacesContext context) {
      trace("/UM" + id);
      if (throwRuntime) {
        throw new RuntimeException("UpdateModelRTE");
      }
      super.updateModel(context);
    }

    public void processValidators(FacesContext context) {
      trace("/PV" + id);
      super.processValidators(context);
    }

    public void validate(FacesContext context) {
      trace("/V" + id);
      if (throwRuntime) {
        throw new RuntimeException("ValidateRTE");
      }
      super.validate(context);
      if (isInvalid) {
        this.setValid(false);
      }
    }

    public void decode(FacesContext context) {
      trace("/D" + id);
      if (throwRuntime) {
        throw new RuntimeException("DecodeRTE");
      }
      super.decode(context);
    }

    public String getTrace() {
      return traceLog.toString();
    }

    public void resetTraceLog() {
      traceLog = new StringBuffer();
    }

    private void trace(String message) {
      traceLog.append(message);
    }
  }

}
