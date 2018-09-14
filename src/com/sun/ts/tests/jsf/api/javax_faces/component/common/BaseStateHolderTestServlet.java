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
package com.sun.ts.tests.jsf.api.javax_faces.component.common;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;
import com.sun.ts.tests.jsf.common.beans.TestBean;

import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIOutput;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

/**
 * <p>
 * Base test Servlet for the {@link StateHolder} interface.
 * </p>
 */
public abstract class BaseStateHolderTestServlet extends HttpTCKServlet {

  protected static final String COMPONENT_REF = "#{requestScope.TestBean.component}";

  protected static final String REF_NAME = "tckRef";

  protected ServletContext servletContext;

  // -------------------------------------------------------------- Public
  // Methods
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
  public void init(ServletConfig config) throws ServletException {
    servletContext = config.getServletContext();
    super.init(config);
  }

  /**
   * <p>
   * Creates a new {@link UIComponent} instance.
   * </p>
   *
   * @return a new {@link UIComponent} instance.
   */
  protected abstract UIComponentBase createComponent();

  // --------------------------------------------------------------Private
  // Methods
  // Populate a pristine component to be used in state holder tests
  protected void populateComponent(UIComponent component) {

    component.getAttributes().put("key1", "value 1");
    component.getAttributes().put("key2", "value 2");
    component.setValueBinding(REF_NAME, new TCKValueBinding(
        getApplication().createValueBinding(COMPONENT_REF), COMPONENT_REF));
    component.setId("componentId");
    component.getClientId(getFacesContext()); // Forces evaluation
    component.setRendered(false);
    component.setRendererType(null); // Since we have no renderers

  }

  // Check that the properties on the specified components are equal
  protected void checkProperties(UIComponent orig, UIComponent restored,
      StringBuffer buf) {
    if (!((TCKValueBinding) orig.getValueBinding(REF_NAME)).getRef().equals(
        ((TCKValueBinding) restored.getValueBinding(REF_NAME)).getRef())) {
      buf.append(JSFTestUtil.FAIL + " ComponentRefs are not equal.\n");
      buf.append("Original componentRef: ")
          .append(((TCKValueBinding) orig.getValueBinding(REF_NAME)).getRef());
      buf.append("\nRestored componentRef: ").append(
          ((TCKValueBinding) restored.getValueBinding(REF_NAME)).getRef());
      buf.append("\n\n");
    }

    if (!orig.getClientId(getFacesContext())
        .equals(restored.getClientId(getFacesContext()))) {
      buf.append(JSFTestUtil.FAIL + " Client IDs are not equal.\n");
      buf.append("Original client ID: ")
          .append(orig.getClientId(getFacesContext()));
      buf.append("\nRestored client ID: ")
          .append(restored.getClientId(getFacesContext()));
      buf.append("\n\n");
    }

    if (!orig.getId().equals(restored.getId())) {
      buf.append(JSFTestUtil.FAIL + " Component IDs are not equal.\n");
      buf.append("Original component ID: ").append(orig.getId());
      buf.append("\nRestored component ID: ").append(restored.getId());
      buf.append("\n\n");
    }

    if (orig.isRendered() != restored.isRendered()) {
      buf.append(
          JSFTestUtil.FAIL + " Return value of isRendered() is not equal.\n");
      buf.append("Original value for isRendered(): ").append(orig.isRendered());
      buf.append("\nRestored value for isRendered(): ")
          .append(restored.isRendered());
      buf.append("\n\n");
    }

    if (orig.getRendererType() != restored.getRendererType()) {
      buf.append(JSFTestUtil.FAIL + " Renderer type is not equal.\n");
      buf.append("Original renderer type to be null.\n");
      buf.append("\nRestored renderer type: ")
          .append(restored.getRendererType());
      buf.append("\n\n");
    }

    if (orig.getRendersChildren() != restored.getRendersChildren()) {
      buf.append(JSFTestUtil.FAIL
          + " Return value of getRendersChildren() is not " + "equal.\n");
      buf.append("Original value for getRendersChildren(): ")
          .append(orig.getRendersChildren());
      buf.append("\nRestored value for getRendersChildren(): ")
          .append(restored.getRendersChildren());
      buf.append("\n\n");
    }

  }

  // Check that the attributes on the specified components are equal
  protected void checkAttributes(UIComponent orig, UIComponent restored,
      StringBuffer buf) {
    if (!orig.getAttributes().equals(restored.getAttributes())) {
      buf.append(JSFTestUtil.FAIL + " Attributes between original component"
          + " and those of the restored component are not equal.\n");
      buf.append("Original Attributes: \n");
      buf.append(JSFTestUtil.getAsString(orig.getAttributes()));
      buf.append("Restored Attributes: \n");
      buf.append(JSFTestUtil.getAsString(restored.getAttributes()));
    }

  }

  // Check that the specified components are equal
  protected void checkComponents(UIComponent orig, UIComponent restored,
      StringBuffer buf) {
    checkAttributes(orig, restored, buf);
    checkProperties(orig, restored, buf);
  }

  // ---------------------------------------------------------------- Test
  // Methods
  // StateHolder.isTransient(), StateHolder.setTransient();
  public void stateHolderIsSetTransientTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    StateHolder holder = createComponent();

    holder.setTransient(false);

    if (holder.isTransient()) {
      out.println(JSFTestUtil.FAIL + " Expected isTransient() to return"
          + " false after having explicitly setting it as such via"
          + " setTransient().");
      return;
    }

    holder.setTransient(true);

    if (!holder.isTransient()) {
      out.println(JSFTestUtil.FAIL + " Expected isTransient() to return true"
          + " after having explicitly setting it as such via"
          + " setTransient().");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // StateHolder.saveState(), StateHolder.restoreState()
  public void stateHolderSaveRestoreStateTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    // Begin test setup
    TestBean bean = new TestBean();
    request.setAttribute("TestBean", bean);
    UIComponent preSave = createComponent();
    UIComponent facet1 = new UIOutput();
    facet1.setId("facet1");
    preSave.getFacets().put("facet1 key", facet1);
    UIComponent facet2 = new UIOutput();
    facet2.setId("facet2");
    preSave.getFacets().put("facet2 key", facet2);
    populateComponent(preSave);

    // Save and restore state and compare the results
    Object state = preSave.saveState(getFacesContext());

    if (state == null) {
      out.println(JSFTestUtil.FAIL + " saveState() failed to returned null");
      return;
    }

    if (!(state instanceof Serializable)) {
      out.println(JSFTestUtil.FAIL + " The Object returned by saveState() was"
          + " not an instance of java.io.Serializable.");
      return;
    }

    UIComponent postSave = createComponent();
    postSave.restoreState(getFacesContext(), state);

    StringBuffer buf = new StringBuffer(128);

    checkComponents(preSave.findComponent("componentId"),
        postSave.findComponent("componentId"), buf);

    if (buf.length() > 0) {
      out.println(buf.toString());
    } else {
      out.println(JSFTestUtil.PASS);
    }
  }

  // StateHolder.restoreState() throws NullPointerException
  public void stateHolderRestoreStateNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    StringBuffer buf = new StringBuffer(128);

    // Begin test setup
    TestBean bean = new TestBean();
    request.setAttribute("TestBean", bean);
    UIComponent preSave = createComponent();
    UIComponent facet1 = new UIOutput();
    facet1.setId("facet1");
    preSave.getFacets().put("facet1 key", facet1);
    UIComponent facet2 = new UIOutput();
    facet2.setId("facet2");
    preSave.getFacets().put("facet2 key", facet2);
    populateComponent(preSave);

    // Save and restore state and compare the results
    Object state = preSave.saveState(getFacesContext());

    UIComponent postSave = createComponent();

    // Null for FaceContext
    try {
      postSave.restoreState(null, state);
      buf.append("Test FAILED" + JSFTestUtil.NL
          + "Expected NullPointerException to be thrown when "
          + "FacesContext is null!" + JSFTestUtil.NL);
    } catch (NullPointerException npe) {
      // do nothing test passes.
    } catch (Exception e) {
      buf.append("Test FAILED" + JSFTestUtil.NL
          + "Expected a NulPointerException to be thrown when "
          + "FacesContext is null!" + JSFTestUtil.NL + "Instead Received: "
          + JSFTestUtil.NL + e.toString());
    }

    // Removed check for state being null based on JSF Spec Issue:640
    // https://javaserverfaces-spec-public.dev.java.net/issues/show_bug.cgi?id=640

    if (buf.length() > 0) {
      out.println(buf.toString());
    } else {
      out.println(JSFTestUtil.PASS);
    }
  }

  // StateHolder.saveState() throws NullPointerException
  public void stateHolderSaveStateNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIComponent preSave = createComponent();

    try {
      Object state = preSave.saveState(null);
      out.println("Test FAILED expected a NullPointerException to be "
          + "thrown when context is null.");

    } catch (NullPointerException npe) {
      // do nothing test passed
    } catch (Exception e) {
      out.println(
          "Test FAILED" + JSFTestUtil.NL + "Unexpected Exception thrown, "
              + "expected NullPointerException instead Received"
              + JSFTestUtil.NL + e.toString());
    }

    out.println(JSFTestUtil.PASS);

  }
}
