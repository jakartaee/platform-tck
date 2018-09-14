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

package com.sun.ts.tests.jsf.api.javax_faces.application.statemanager;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;

import javax.faces.FactoryFinder;
import javax.faces.application.StateManager;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.validator.LengthValidator;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

public class TestServlet extends HttpTCKServlet {

  /**
   * <p>
   * Initializes this {@link javax.servlet.Servlet}.
   * </p>
   * 
   * @param config
   *          this Servlet's configuration
   * @throws javax.servlet.ServletException
   *           if an error occurs
   */
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }

  // ---------------------------------------------------------------- Test
  // Methods

  // Validation of return value will be performed on the client side.
  public void stateManagerIsSavingStateInClientTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    out.println(getApplication().getStateManager()
        .isSavingStateInClient(getFacesContext()));
  }

  // Validate NPE is thrown when context is null
  public void stateManagerIsSavingStateInClientNPETest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    PrintWriter out = response.getWriter();

    try {
      getApplication().getStateManager().isSavingStateInClient(null);
      out.println("Test FAILED  No Exception Thrown!");

    } catch (NullPointerException npe) {
      out.println(JSFTestUtil.PASS);
    } catch (Exception e) {
      out.println("Test FAILED  Unexpected Exception thrown." + JSFTestUtil.NL
          + "Expected: NullPointerException" + JSFTestUtil.NL + "Received: "
          + JSFTestUtil.NL + e.toString());
    }
  }

  public void stateManagerSaveSerializedViewTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    UIViewRoot root = getApplication().getViewHandler().createView(context,
        "/root");
    StateManager manager = getApplication().getStateManager();
    context.setViewRoot(root);

    // If saving state in server saveSerializedView will return
    // always return null
    // If saving state in client, then there will be a non null
    // return value if there is state to save.
    root.setTransient(true);
    out.print(
        (manager.saveSerializedView(context) == null) ? "null" : "not null");
    out.print('|');

    root.setTransient(false);
    UIInput input = new UIInput();
    input.setId("input1");
    UIOutput output = new UIOutput();
    output.setId("output");
    root.getChildren().add(input);
    root.getChildren().add(output);

    out.print(
        (manager.saveSerializedView(context) == null) ? "null" : "not null");
  }

  public void stateManagerSaveSerializedViewDupIdTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    UIViewRoot root = getApplication().getViewHandler().createView(context,
        "/root");
    StateManager manager = getApplication().getStateManager();
    context.setViewRoot(root);

    UIOutput output = new UIOutput();
    output.setId("output");
    UIOutput output1 = new UIOutput();
    output1.setId("output");
    root.getChildren().add(output);
    root.getChildren().add(output1);

    try {
      manager.saveSerializedView(context);
      out.println(
          "Test FAILED[1]. No Exception thrown when View contained duplicate"
              + " IDs.");
      return;
    } catch (Exception e) {
      if (!(e instanceof IllegalStateException)) {
        out.println("Test FAILED[1].  Exception thrown when View contained"
            + " duplicated IDs, but it wasn't an instance of "
            + "IllegalStateException.");
        return;
      }
    }

    root = getApplication().getViewHandler().createView(context, "/root");
    context.setViewRoot(root);

    output = new UIOutput();
    output.setId("output");
    UIOutput facet = new UIOutput();
    facet.setId("output");
    output.getFacets().put("facet", facet);
    root.getChildren().add(output);

    try {
      manager.saveSerializedView(context);
      out.println(
          "Test FAILED[2]. No Exception thrown when View contained duplicate"
              + " IDs.");
      return;
    } catch (Exception e) {
      if (!(e instanceof IllegalStateException)) {
        out.println("Test FAILED[2].  Exception thrown when View contained"
            + " duplicated IDs, but it wasn't an instance of "
            + "IllegalStateException.");
        return;
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  public void stateManagerWriteState(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    FacesContext context = getFacesContext();
    UIViewRoot root = getApplication().getViewHandler().createView(context,
        "/root");
    StateManager manager = getApplication().getStateManager();
    context.setViewRoot(root);
    RenderKitFactory factory = (RenderKitFactory) FactoryFinder
        .getFactory(FactoryFinder.RENDER_KIT_FACTORY);
    RenderKit renderKit = factory.getRenderKit(context,
        RenderKitFactory.HTML_BASIC_RENDER_KIT);
    ResponseWriter writer = renderKit.createResponseWriter(new StringWriter(),
        "text/html", "ISO-8859-1");
    context.setResponseWriter(writer);
    root.setLocale(Locale.US);

    UIOutput output = new UIOutput();
    output.setValue("label");
    output.setId("label");
    UIInput input = new UIInput();
    input.setId("input");
    input.setValue("value");
    input.addValidator(new LengthValidator(10, 1));
    root.getChildren().add(output);
    root.getChildren().add(input);
    manager.writeState(context, manager.saveSerializedView(context));
  }

}
