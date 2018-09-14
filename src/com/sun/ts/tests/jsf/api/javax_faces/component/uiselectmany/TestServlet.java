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

package com.sun.ts.tests.jsf.api.javax_faces.component.uiselectmany;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectMany;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jsf.api.javax_faces.component.common.BufferedResponseWrapper;
import com.sun.ts.tests.jsf.api.javax_faces.component.common.TCKValidator;
import com.sun.ts.tests.jsf.api.javax_faces.component.common.TCKValueChangeListener;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

public class TestServlet
    extends com.sun.ts.tests.jsf.api.javax_faces.component.uiinput.TestServlet {

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
    setRendererType("javax.faces.Listbox");
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
    return new UISelectMany();
  }

  // ------------------------------------------- Test Methods ----

  @Override
  public void uiComponentGetSetValueExpressionTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    BufferedResponseWrapper wrapper = new BufferedResponseWrapper(response);

    super.uiComponentGetSetValueExpressionTest(request, wrapper);
    String result = wrapper.getBufferedWriter().toString();

    PrintWriter out = response.getWriter();

    if (result.indexOf(JSFTestUtil.PASS) == -1) {
      out.println(result);
      return;
    }

    List<String> values = new ArrayList<String>();
    values.add("value1");
    values.add("value2");

    // default processing of get,setValueExpression is ok, now validate
    // processing specific to UIGraphic
    request.setAttribute("values", values);

    ExpressionFactory factory = JSFTestUtil
        .getExpressionFactory(servletContext);
    ValueExpression expression = factory.createValueExpression(
        getFacesContext().getELContext(), "#{requestScope.values}",
        java.util.List.class);

    UISelectMany many = (UISelectMany) createComponent();

    many.setValueExpression("selectedValues", expression);

    if (many.getValue() != values) {
      out.println(JSFTestUtil.FAIL + " getValue() returned unexpected value"
          + " after having called setValueExpression().");
      out.println("Expected: " + values);
      out.println("Received: " + many.getValue());
    }
    out.println(JSFTestUtil.PASS);

  }

  // Test event queuing and broadcasting (any phase listeners)
  @Override
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

  @Override
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

  @Override
  public void uiInputValidate3aTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIInput input = (UIInput) createComponent();
    input.setRendererType(null);
    UISelectItem item1 = new UISelectItem();
    item1.setItemValue("new");
    UISelectItem item2 = new UISelectItem();
    item2.setItemValue("new2");
    UISelectItem item3 = new UISelectItem();
    item3.setItemValue("new3");
    input.getChildren().add(item1);
    input.getChildren().add(item2);
    input.getChildren().add(item3);
    FacesContext context = getFacesContext();
    UIViewRoot root = getApplication().getViewHandler().createView(context,
        "/root");
    context.setViewRoot(root);
    root.getChildren().add(input);

    // Verify that a UIInput instance that has a local value,
    // and the valid property is true all validators associated
    // with the component are invoked, if the component is marked
    // invalid by a validator, no listener is invoked.
    input.setSubmittedValue(new String[] { "new" });

    // Setup the validators
    TCKValidator validator1 = new TCKValidator("VL1", false);
    TCKValidator validator2 = new TCKValidator("VL2", true);

    input.addValidator(validator1);
    request.setAttribute("TCKValidator", validator2);

    MethodBinding binding = getApplication().createMethodBinding(
        "#{requestScope.TCKValidator.validate}",
        new Class[] { FacesContext.class, UIComponent.class, Object.class });
    input.setValidator(binding);

    // Setup the listeners
    TCKValueChangeListener listener = new TCKValueChangeListener("VCL1");

    input.addValueChangeListener(listener);

    TCKValueChangeListener.trace(null);
    TCKValidator.clearTrace();

    root.processValidators(context);

    String valTrace = TCKValidator.getTrace();
    if (!"/VL1/VL2".equals(valTrace)) {
      out.println(JSFTestUtil.FAIL + " Validator trace did not return as "
          + "expected.");
      out.println("Traced expected: /VL1/VL2");
      out.println("Trace received: " + valTrace);
      return;
    }

    String listenerTrace = TCKValueChangeListener.trace();
    if (listenerTrace.length() != 0) {
      out.println(JSFTestUtil.FAIL + " ValueChangeListener was incorrectly"
          + " invoked after a Validator marked the component as invalid.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  @Override
  public void uiInputValidate3bTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIInput input = (UIInput) createComponent();
    input.setRendererType(null);
    UISelectItem item1 = new UISelectItem();
    item1.setItemValue("new");
    UISelectItem item2 = new UISelectItem();
    item2.setItemValue("new2");
    UISelectItem item3 = new UISelectItem();
    item3.setItemValue("new3");
    input.getChildren().add(item1);
    input.getChildren().add(item2);
    input.getChildren().add(item3);
    FacesContext context = getFacesContext();
    UIViewRoot root = getApplication().getViewHandler().createView(context,
        "/root");
    context.setViewRoot(root);
    root.getChildren().add(input);

    // Verify that a UIInput instance that has a local value,
    // and the valid property is true all validators associated
    // with the component are invoked, if the component is marked
    // invalid by a validator, no listener is invoked.
    input.setSubmittedValue(new String[] { "new" });

    // Setup the validators
    TCKValidator validator1 = new TCKValidator("VL1", false);
    TCKValidator validator2 = new TCKValidator("VL2", true);

    input.addValidator(validator1);
    request.setAttribute("TCKValidator", validator2);

    MethodBinding binding = getApplication().createMethodBinding(
        "#{requestScope.TCKValidator.validate}",
        new Class[] { FacesContext.class, UIComponent.class, Object.class });
    input.setValidator(binding);

    // Setup the listeners
    TCKValueChangeListener listener = new TCKValueChangeListener("VCL1");

    input.addValueChangeListener(listener);

    TCKValueChangeListener.trace(null);
    TCKValidator.clearTrace();

    // All validators succeed, and the value differs from the previous.
    // ensure the listener was invoked.
    validator2.markInvalid(false);
    TCKValueChangeListener.trace(null);
    TCKValidator.clearTrace();
    input.setValid(true);
    input.setSubmittedValue(new String[] { "new3" });

    root.processValidators(context);

    String valTrace = TCKValidator.getTrace();
    if (!"/VL1/VL2".equals(valTrace)) {
      out.println(JSFTestUtil.FAIL + " Validator trace did not return as "
          + "expected.");
      out.println("Traced expected: /VL1/VL2");
      out.println("Trace received: " + valTrace);
      return;
    }

    String listenerTrace = TCKValueChangeListener.trace();
    if (!"/VCL1@ANY_PHASE".equals(listenerTrace)) {
      out.println(JSFTestUtil.FAIL + " Unexpected listener trace.");
      out.println("Expected: /VCL1@ANY_PHASE");
      out.println("Received: " + listenerTrace);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  @Override
  public void uiInputValidate3cTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIInput input = (UIInput) createComponent();
    input.setRendererType(null);
    UISelectItem item1 = new UISelectItem();
    item1.setItemValue("new");
    UISelectItem item2 = new UISelectItem();
    item2.setItemValue("new2");
    UISelectItem item3 = new UISelectItem();
    item3.setItemValue("new3");
    input.getChildren().add(item1);
    input.getChildren().add(item2);
    input.getChildren().add(item3);
    FacesContext context = getFacesContext();
    UIViewRoot root = getApplication().getViewHandler().createView(context,
        "/root");
    context.setViewRoot(root);
    root.getChildren().add(input);

    // Verify that a UIInput instance that has a local value,
    // and the valid property is true all validators associated
    // with the component are invoked, if the component is marked
    // invalid by a validator, no listener is invoked.
    input.setSubmittedValue(new String[] { "new" });

    // Setup the validators
    TCKValidator validator1 = new TCKValidator("VL1", false);
    TCKValidator validator2 = new TCKValidator("VL2", true);

    input.addValidator(validator1);
    request.setAttribute("TCKValidator", validator2);

    MethodBinding binding = getApplication().createMethodBinding(
        "#{requestScope.TCKValidator.validate}",
        new Class[] { FacesContext.class, UIComponent.class, Object.class });
    input.setValidator(binding);

    // Setup the listeners
    TCKValueChangeListener listener = new TCKValueChangeListener("VCL1");

    input.addValueChangeListener(listener);

    TCKValueChangeListener.trace(null);
    TCKValidator.clearTrace();

    // finally, if the new and previous values do not differ,
    // the listener will not be invoked.
    input.setSubmittedValue(new String[] { "new2" });
    input.setValue(new String[] { "new2" });
    TCKValueChangeListener.trace(null);
    TCKValidator.clearTrace();

    root.processValidators(context);

    String valTrace = TCKValidator.getTrace();
    if (!"/VL1/VL2".equals(valTrace)) {
      out.println(JSFTestUtil.FAIL + " Validator trace did not return as "
          + "expected.");
      out.println("Expected no trace");
      out.println("Trace received: " + valTrace);
      return;
    }

    String listenerTrace = TCKValueChangeListener.trace();
    if (listenerTrace.length() != 0) {
      out.println(JSFTestUtil.FAIL + " ValueChangeListener was incorrectly"
          + " invoked after a Validator marked the component as invalid.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void uiInputValidate6Test(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIInput input = (UIInput) createComponent();
    UISelectItem item1 = new UISelectItem();
    item1.setItemValue("new");
    UISelectItem item2 = new UISelectItem();
    item2.setItemValue("new2");
    UISelectItem item3 = new UISelectItem();
    item3.setItemValue("new3");
    input.getChildren().add(item1);
    input.getChildren().add(item2);
    input.getChildren().add(item3);
    FacesContext context = getFacesContext();
    UIViewRoot root = getApplication().getViewHandler().createView(context,
        "/root");
    root.getChildren().add(input);

    // lastly, if the item selected doens't match the list of
    // available items, then queue a message and mark the component
    // invalid.

    input.setSubmittedValue(new String[] { "invalid" });
    input.setValid(true);
    TCKValueChangeListener.trace(null);
    TCKValidator.clearTrace();

    root.processValidators(context);

    int messageCount = checkMessages();
    if (messageCount != 1) {
      out.println(JSFTestUtil.FAIL + " No message queued into the FacesContext"
          + " when the item selected doesn't match the list of available"
          + " items.");
      return;
    }

    if (input.isValid()) {
      out.println(JSFTestUtil.FAIL + " UIInput instance was not marked invalid"
          + " when the item selected didn't match the list of available"
          + " values.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // ------------------- selectMany Tests

  public void uiSelectManyGetSetSelectedValuesTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    UISelectMany many = (UISelectMany) createComponent();

    String[] test = new String[] { "red", "white", "blue" };

    many.setSelectedValues(test);
    String[] result = (String[]) many.getSelectedValues();

    int resultSize = result.length;

    for (int i = 0; i < resultSize; i++) {
      if (!Arrays.asList(test).contains(result[i])) {
        out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
            + "Expected returned array of selected items to have the below value in it!"
            + JSFTestUtil.NL + "Missing: " + result[i]);
        return;
      }
    }

    out.println(JSFTestUtil.PASS);

  } // end uiComponentGetSetSelectedValuesTest

}
