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

package com.sun.ts.tests.jsf.api.javax_faces.component.uiselectone;

import java.io.IOException;
import java.io.PrintWriter;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectOne;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jsf.api.javax_faces.component.common.TCKConverter;
import com.sun.ts.tests.jsf.api.javax_faces.component.common.TCKValidator;
import com.sun.ts.tests.jsf.api.javax_faces.component.common.TCKValueChangeListener;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;
import com.sun.ts.tests.jsf.common.beans.TestBean;

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
    setRendererType("javax.faces.Menu");
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
    return new UISelectOne();
  }

  // ------------------------------------------- Test Methods ----

  @Override
  public void uiInputValidate3aTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIInput input = (UIInput) createComponent();
    UISelectItem item1 = new UISelectItem();
    item1.setItemValue("new");
    UISelectItem item2 = new UISelectItem();
    item2.setItemValue("new2");
    input.getChildren().add(item1);
    input.getChildren().add(item2);
    FacesContext context = getFacesContext();
    UIViewRoot root = getApplication().getViewHandler().createView(context,
        "/root");
    context.setViewRoot(root);
    root.getChildren().add(input);

    // Verify that a UIInput instance that has a local value,
    // and the valid property is true all validators associated
    // with the component are invoked, if the component is marked
    // invalid by a validator, no listener is invoked.
    input.setSubmittedValue("new");

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
    input.setSubmittedValue("new2");

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
    UISelectItem item1 = new UISelectItem();
    item1.setItemValue("new");
    UISelectItem item2 = new UISelectItem();
    item2.setItemValue("new2");
    input.getChildren().add(item1);
    input.getChildren().add(item2);
    FacesContext context = getFacesContext();
    UIViewRoot root = getApplication().getViewHandler().createView(context,
        "/root");
    context.setViewRoot(root);
    root.getChildren().add(input);

    // Verify that a UIInput instance that has a local value,
    // and the valid property is true all validators associated
    // with the component are invoked, if the component is marked
    // invalid by a validator, no listener is invoked.
    input.setSubmittedValue("new");

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
    input.setSubmittedValue("new2");

    TCKValueChangeListener.trace(null);
    TCKValidator.clearTrace();

    // All validators succeed, and the value differs from the previous.
    // ensure the listener was invoked.
    validator2.markInvalid(false);
    TCKValueChangeListener.trace(null);
    TCKValidator.clearTrace();
    input.setValid(true);

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
    UISelectItem item1 = new UISelectItem();
    item1.setItemValue("new");
    UISelectItem item2 = new UISelectItem();
    item2.setItemValue("new2");
    input.getChildren().add(item1);
    input.getChildren().add(item2);
    FacesContext context = getFacesContext();
    UIViewRoot root = getApplication().getViewHandler().createView(context,
        "/root");
    context.setViewRoot(root);
    root.getChildren().add(input);

    // Verify that a UIInput instance that has a local value,
    // and the valid property is true all validators associated
    // with the component are invoked, if the component is marked
    // invalid by a validator, no listener is invoked.
    input.setSubmittedValue("new");

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
    input.setSubmittedValue("new2");

    TCKValueChangeListener.trace(null);
    TCKValidator.clearTrace();

    // finally, if the new and previous values do not differ,
    // the listener will not be invoked.
    input.setSubmittedValue("new");
    input.setValue("new");
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
  public void uiInputValidate4Test(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIInput input = (UIInput) createComponent();
    input.setRendererType(null);
    UISelectItem item1 = new UISelectItem();
    item1.setItemValue("converted");
    UISelectItem item2 = new UISelectItem();
    item2.setItemValue("converted2");
    input.getChildren().add(item1);
    input.getChildren().add(item2);
    FacesContext context = getFacesContext();
    UIViewRoot root = getApplication().getViewHandler().createView(context,
        "/root");
    context.setViewRoot(root);
    root.getChildren().add(input);

    Converter converter = new TCKConverter();
    input.setConverter(converter);

    input.setSubmittedValue("new");

    input.setValid(true);
    input.setRequired(true);
    input.validate(context);

    if (!"converted".equals(input.getValue())) {
      out.println(JSFTestUtil.FAIL + " Converter available to the component"
          + " via getConverter(), but the converter wasn't called.");
      out.println("Expected the value after conversion to be 'converted'");
      out.println("Value received: " + input.getValue());
      return;
    }

    // now verify the proper behavior if conversion fails
    input.setSubmittedValue("fail");
    input.validate(context);

    if (input.isValid()) {
      out.println(JSFTestUtil.FAIL + " Conversion failure should have resulted"
          + " in the component in question being marked as invalid.");
      return;
    }

    if (JSFTestUtil.getAsArray(context.getMessages()).length < 1) {
      out.println(JSFTestUtil.FAIL + " No messages queued as required in the "
          + "case of conversion failure.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  @Override
  public void uiInputValidate5Test(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIInput input = (UIInput) createComponent();
    input.setRendererType(null);
    UISelectItem item1 = new UISelectItem();
    item1.setItemValue("converted");
    UISelectItem item2 = new UISelectItem();
    item2.setItemValue("converted2");
    input.getChildren().add(item1);
    input.getChildren().add(item2);
    FacesContext context = getFacesContext();
    Application application = getApplication();
    UIViewRoot root = application.getViewHandler().createView(context, "/root");
    context.setViewRoot(root);
    root.getChildren().add(input);

    application.addConverter(Boolean.class,
        "com.sun.ts.tests.jsf.api.javax_faces.component.common.TCKConverter");

    // setup value binding
    request.setAttribute("simple", new TestBean());
    ValueBinding binding = application
        .createValueBinding("#{requestScope.simple.boolWrapProp}");
    input.setValueBinding("value", binding);

    // run through validation
    input.setSubmittedValue("new");

    input.setValid(true);
    input.setRequired(true);
    input.validate(context);

    if (!"converted".equals(input.getValue())) {
      out.println(JSFTestUtil.FAIL + " Converter available to the component"
          + " via getConverter(), but the converter wasn't called.");
      out.println("Expected the value after conversion to be 'converted'");
      out.println("Value received: " + input.getValue());
      return;
    }

    // now verify the proper behavior if conversion fails
    input.setSubmittedValue("fail");
    input.validate(context);

    if (input.isValid()) {
      out.println(JSFTestUtil.FAIL + " Conversion failure should have resulted"
          + " in the component in question being marked as invalid.");
      return;
    }

    if (JSFTestUtil.getAsArray(context.getMessages()).length < 1) {
      out.println(JSFTestUtil.FAIL + " No messages queued as required in the "
          + "case of conversion failure.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void uiInputValidate6Test(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIInput input = (UIInput) createComponent();
    UISelectItem item1 = new UISelectItem();
    item1.setItemValue("converted");
    UISelectItem item2 = new UISelectItem();
    item2.setItemValue("converted2");
    input.getChildren().add(item1);
    input.getChildren().add(item2);
    FacesContext context = getFacesContext();
    UIViewRoot root = getApplication().getViewHandler().createView(context,
        "/root");
    root.getChildren().add(input);

    // if the item selected doens't match the list of
    // available items, then queue a message and mark the component
    // invalid.

    input.setSubmittedValue("invalid");
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

}
