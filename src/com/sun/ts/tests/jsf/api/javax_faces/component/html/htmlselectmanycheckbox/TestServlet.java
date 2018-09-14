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

package com.sun.ts.tests.jsf.api.javax_faces.component.html.htmlselectmanycheckbox;

import com.sun.ts.tests.jsf.api.javax_faces.component.common.TCKValueChangeListener;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlSelectManyCheckbox;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.PhaseId;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public final class TestServlet extends
    com.sun.ts.tests.jsf.api.javax_faces.component.uiselectmany.TestServlet {

  private static final String[] attrNames = { "accesskey", "border", "dir",
      "disabledClass", "enabledClass", "lang", "layout", "onblur", "onchange",
      "onclick", "ondblclick", "onfocus", "onkeydown", "onkeypress", "onkeyup",
      "onmousedown", "onmousemove", "onmouseout", "onmouseover", "onmouseup",
      "onselect", "style", "styleClass", "tabindex", "title", "disabled",
      "readonly", "disabledClass", "enabledClass" };

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
    super.init(config);
    setRendererType("javax.faces.Checkbox");
    setAttributeNames(attrNames);
  }

  /**
   * <p>
   * Creates a new {@link UIComponent} instance.
   * </p>
   * 
   * @return a new {@link UIComponent} instance.
   */
  protected UIComponentBase createComponent() {
    return new HtmlSelectManyCheckbox();
  }

  // ------------------------------------------- Test Methods ----

  // Test event queuing and broadcasting (any phase listeners)
  public void uiInputBroadcastTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    FacesContext facesContext = getFacesContext();
    UIInput input = (UIInput) createComponent();
    input.setRendererType(null);
    UIViewRoot root = facesContext.getApplication().getViewHandler()
        .createView(facesContext, "/root");
    root.getChildren().add(input);
    ValueChangeEvent event = new ValueChangeEvent(input, null, null);
    event.setPhaseId(PhaseId.PROCESS_VALIDATIONS);

    // Register three listeners
    input.addValueChangeListener(new TCKValueChangeListener("AP0"));
    input.addValueChangeListener(new TCKValueChangeListener("AP1"));
    input.addValueChangeListener(new TCKValueChangeListener("AP2"));

    // Fire events and evaluate results
    TCKValueChangeListener.trace(null);
    input.queueEvent(event);
    root.processDecodes(facesContext);
    root.processValidators(facesContext);
    root.processApplication(facesContext);
    String trace = TCKValueChangeListener.trace();
    String expectedTrace = "/AP0@PROCESS_VALIDATIONS/AP1@PROCESS_VALIDATIONS/AP2@PROCESS_VALIDATIONS";
    if (!expectedTrace.equals(trace)) {
      out.println(JSFTestUtil.FAIL + " Unexpected listener trace.");
      out.println("Expected trace: " + expectedTrace);
      out.println("Trace received: " + trace);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void uiInputBroadcastValueChangeListenerTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    FacesContext facesContext = getFacesContext();
    UIInput input = (UIInput) createComponent();
    input.setRendererType(null);
    UIViewRoot root = new UIViewRoot();
    root.getChildren().add(input);

    TCKValueChangeListener listener = new TCKValueChangeListener("VCLR");

    MethodBinding binding = getApplication().createMethodBinding(
        "#{requestScope.reqVCL.processValueChange}",
        new Class[] { ValueChangeEvent.class });
    request.setAttribute("reqVCL", listener);
    input.setValueChangeListener(binding);

    ValueChangeEvent event = new ValueChangeEvent(input, null, null);
    event.setPhaseId(PhaseId.PROCESS_VALIDATIONS);
    TCKValueChangeListener.trace(null);
    input.queueEvent(event);
    root.processDecodes(facesContext);
    root.processValidators(facesContext);
    root.processApplication(facesContext);

    String trace = TCKValueChangeListener.trace();

    if (trace.length() == 0) {
      out.println(JSFTestUtil.FAIL + " The ValueChangeListener as referenced"
          + " by ValueChangeListenerRef 'requestScope.reqVCL.processValueChange'"
          + " was not invoked.");
      return;
    }

    if (!"/VCLR@PROCESS_VALIDATIONS".equals(trace)) {
      out.println(JSFTestUtil.FAIL + " Unexpected Listener trace.");
      out.println("Expected: /VCLR@PROCESS_VALIDATIONS");
      out.println("Received: " + trace);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

}
