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

package com.sun.ts.tests.jsf.api.javax_faces.component.uiinput;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.el.MethodBinding;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jsf.api.javax_faces.component.common.BaseComponentTestServlet;
import com.sun.ts.tests.jsf.api.javax_faces.component.common.TCKConverter;
import com.sun.ts.tests.jsf.api.javax_faces.component.common.TCKValidator;
import com.sun.ts.tests.jsf.api.javax_faces.component.common.TCKValueChangeListener;
import com.sun.ts.tests.jsf.common.beans.TestBean;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;
import javax.el.ValueExpression;

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
    setRendererType("javax.faces.Text");
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
    return new UIInput();
  }

  // --------------------------------------------------------- Private Methods

  // Check that the number of queued messages equals the expected count
  protected int checkMessages() {

    int n = 0;
    Iterator messages = getFacesContext().getMessages();
    while (messages.hasNext()) {
      FacesMessage message = (FacesMessage) messages.next();
      n++;
      System.err.println(message.getSummary());
    }
    return n;
  }

  // ------------------------------------------- Test Methods ----

  // ----------------------------------------------------------------- UIInput

  // Test event queuing and broadcasting (any phase listeners)
  public void uiInputBroadcastTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    FacesContext facesContext = getFacesContext();
    UIInput input = (UIInput) createComponent();
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

  public void uiInputUpdateModelNoActionTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIInput input = (UIInput) createComponent();
    input.setValue(Boolean.FALSE);
    TestBean testBean = new TestBean();
    testBean.setBoolProp(true);

    FacesContext context = getFacesContext();

    request.setAttribute("TestBean", testBean);

    // if valid property is false, then no action is taken
    input.setValid(false);
    input.setValueBinding("attr", context.getApplication()
        .createValueBinding("#{requestScope.TestBean.boolProp}"));
    input.updateModel(context);

    int messageCount = checkMessages();
    if (messageCount != 0) {
      out.println(JSFTestUtil.FAIL + " Didn't expect to find any messages "
          + "in the FacesContext.");
      out.println("Message count: " + messageCount);
    }

    if (!testBean.getBoolProp()) {
      out.println(JSFTestUtil.FAIL + " UIInput instance's valid property was "
          + "false.  The updateModel() method should not have updated the value"
          + " referenced by the set ValueBinding.");
      return;
    }

    // If there is no value binding, nothing should occur
    input = (UIInput) createComponent();
    input.setValue(Boolean.FALSE);
    input.setValid(true);

    input.updateModel(context);

    messageCount = checkMessages();
    if (messageCount != 0) {
      out.println(JSFTestUtil.FAIL + " Didn't expect to find any messages "
          + "in the FacesContext.");
      out.println("Message count: " + messageCount);
    }

    if (!testBean.getBoolProp()) {
      out.println(JSFTestUtil.FAIL + " UIInput instance has no ValueBinding.  "
          + "The updateModel() method should not have performed any action.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void uiInputUpdateModelSucceedsTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIInput input = (UIInput) createComponent();
    TestBean testBean = new TestBean();
    testBean.setBoolProp(false);
    request.setAttribute("TestBean", testBean);

    FacesContext context = getFacesContext();

    input.setValueExpression("value", this.getVExpression(context,
        "#{requestScope.TestBean.boolProp}", boolean.class));

    input.setValue(Boolean.TRUE);

    input.updateModel(context);

    int messageCount = checkMessages();
    if (messageCount != 0) {
      out.println(JSFTestUtil.FAIL + " Didn't expect to find any messages "
          + "in the FacesContext.");
      out.println("Message count: " + messageCount);
      return;
    }

    if (testBean.getBoolProp() != true) {
      out.println(
          JSFTestUtil.FAIL + " updateModel() call failed.  Boolean property"
              + " of TestBean instance was not updated.");
      return;
    }

    if (input.getLocalValue() != null) {
      out.println(JSFTestUtil.FAIL + " The local value of the component was not"
          + " set to null after updateModel() succeeded updating the "
          + "model tier.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void uiInputUpdateModelFailsTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIInput input = (UIInput) createComponent();
    TestBean testBean = new TestBean();
    request.setAttribute("TestBean", testBean);

    FacesContext context = getFacesContext();

    input.setValueExpression("value", this.getVExpression(context,
        "#{requestScope.TestBean.stkTrace}", String.class));

    // This will trigger an exception to be thrown to the ExceptionHandler
    input.setValue("Nothing");

    input.updateModel(context);

    if (context.getExceptionHandler()
        .getUnhandledExceptionQueuedEvents() == null) {
      out.println("Test FAILED. Expected to find an Unhandled "
          + "ExceptionEvent in the FacesContext to reflect the "
          + "conversion failure.");
      return;
    }

    if (!"false".equals(testBean.getStkTrace())) {
      out.println(JSFTestUtil.FAIL + " updateModel() call failed. Boolean "
          + "property of TestBean instance was altered.");
      out.println("Expected value to be: false");
      out.println("Actual value: " + testBean.getBoolProp());
      return;
    }

    if (!input.getLocalValue().equals("Nothing")) {
      out.println(JSFTestUtil.FAIL + " The local value of the UIInput "
          + "instance was modified.");
      out.println("Expected: String Nothing");
      out.println("Received: " + input.getLocalValue());
      return;
    }

    if (input.isValid()) {
      out.println(JSFTestUtil.FAIL + " Expected the valid property of the "
          + "UIInput instance to be false after the updateModel() "
          + "call failed.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIInput.updateModel() throws NullPointerException
  public void uiInputUpdateModelNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    JSFTestUtil.checkForNPE(createComponent().getClass(), "updateModel",
        new Class<?>[] { FacesContext.class }, new Object[] { null }, out);

  }// End uiInputUpdateModelNPETest

  // UIInput.validate() throws NullPointerException
  public void uiInputValidateNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    JSFTestUtil.checkForNPE(createComponent().getClass(), "validate",
        new Class<?>[] { FacesContext.class }, new Object[] { null }, out);

  }// End uiInputValidateNPETest

  public void uiInputValidate1Test(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIInput input = (UIInput) createComponent();
    FacesContext context = getFacesContext();
    UIViewRoot root = getApplication().getViewHandler().createView(context,
        "/root");
    context.setViewRoot(root);
    root.getChildren().add(input);

    RenderKitFactory rFactory = (RenderKitFactory) FactoryFinder
        .getFactory(FactoryFinder.RENDER_KIT_FACTORY);
    RenderKit rKit = rFactory.getRenderKit(context,
        RenderKitFactory.HTML_BASIC_RENDER_KIT);
    String rendererType = input.getRendererType();
    String family = input.getFamily();
    Renderer orig = rKit.getRenderer(family, rendererType);
    Renderer1 renderer1 = new Renderer1();
    rKit.addRenderer(family, rendererType, renderer1);

    input.setValid(true);
    input.setRequired(true);
    input.setSubmittedValue("null");

    input.validate(context);

    if (!renderer1.wasCalled()) {
      out.println(JSFTestUtil.FAIL + " A Renderer was associated with the "
          + "component, but the getConvertedValue() method was not invoked"
          + " on the Renderer.");
      return;
    }

    if (orig != null)
      rKit.addRenderer(family, rendererType, orig);

    out.println(JSFTestUtil.PASS);
  }

  public void uiInputValidate2Test(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIInput input = (UIInput) createComponent();
    FacesContext context = getFacesContext();
    UIViewRoot root = getApplication().getViewHandler().createView(context,
        "/root");
    context.setViewRoot(root);
    root.getChildren().add(input);

    RenderKitFactory rFactory = (RenderKitFactory) FactoryFinder
        .getFactory(FactoryFinder.RENDER_KIT_FACTORY);
    RenderKit rKit = rFactory.getRenderKit(context,
        RenderKitFactory.HTML_BASIC_RENDER_KIT);
    String rendererType = input.getRendererType();
    String family = input.getFamily();
    Renderer orig = rKit.getRenderer(family, rendererType);
    Renderer1 renderer1 = new Renderer1();
    rKit.addRenderer(family, rendererType, renderer1);

    // null or "" should result in a message being queued and the component
    // being marked as invalid
    input.setValid(true);
    input.setRequired(true);
    // setting to "null" as string cuases custom Renderer to return null
    input.setSubmittedValue("null");

    input.validate(context);

    Object[] messages = JSFTestUtil.getAsArray(context.getMessages());

    if (messages.length != 1) {
      out.println(JSFTestUtil.FAIL + " Expected one message to be queued to"
          + " the FacesContext if the UIInput being validated had a "
          + "null value.");
      out.println("Number of messages: " + messages.length);
      return;
    }

    if (input.isValid()) {
      out.println(JSFTestUtil.FAIL + " Component was not marked invalid after"
          + " attempting to validate with a null value.");
      return;
    }

    input.setValid(true);
    input.setSubmittedValue("");

    input.validate(context);

    messages = JSFTestUtil.getAsArray(context.getMessages());

    if (messages.length != 2) {
      out.println(JSFTestUtil.FAIL + " Expected two messages to be queued to"
          + " the FacesContext if the UIInput being validated had a "
          + "zero-length string as the value.");
      out.println("Number of messages: " + messages.length);
      return;
    }

    if (input.isValid()) {
      out.println(JSFTestUtil.FAIL + " Component was not marked invalid after"
          + " attempting to validate with a zero-length string as the"
          + " value.");
      return;
    }

    // restore original Renderer
    if (orig != null)
      rKit.addRenderer(family, rendererType, orig);

    out.println(JSFTestUtil.PASS);
  }

  public void uiInputValidate3aTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIInput input = (UIInput) createComponent();
    FacesContext context = getFacesContext();
    UIViewRoot root = getApplication().getViewHandler().createView(context,
        "/root");
    context.setViewRoot(root);
    root.getChildren().add(input);

    // Verify that a UIInput instance that has a local value,
    // and the valid property is true all validators associated
    // with the component are invoked, if the component is marked
    // invalid by a validator, no listener is invoked.
    input.setSubmittedValue("previous");

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
    input.setSubmittedValue("new-value");

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

  public void uiInputValidate3bTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIInput input = (UIInput) createComponent();
    FacesContext context = getFacesContext();
    UIViewRoot root = getApplication().getViewHandler().createView(context,
        "/root");
    context.setViewRoot(root);
    root.getChildren().add(input);

    // Verify that a UIInput instance that has a local value,
    // and the valid property is true all validators associated
    // with the component are invoked, if the component is marked
    // invalid by a validator, no listener is invoked.
    input.setSubmittedValue("previous");

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
    input.setSubmittedValue("new-value");

    TCKValueChangeListener.trace(null);
    TCKValidator.clearTrace();

    root.processValidators(context);

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

  public void uiInputValidate3cTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIInput input = (UIInput) createComponent();
    FacesContext context = getFacesContext();
    UIViewRoot root = getApplication().getViewHandler().createView(context,
        "/root");
    context.setViewRoot(root);
    root.getChildren().add(input);

    // Verify that a UIInput instance that has a local value,
    // and the valid property is true all validators associated
    // with the component are invoked, if the component is marked
    // invalid by a validator, no listener is invoked.
    input.setSubmittedValue("previous");
    // input.setValue("new");

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
    input.setSubmittedValue("new-value");

    TCKValueChangeListener.trace(null);
    TCKValidator.clearTrace();

    // finally, if the new and previous values do not differ,
    // the listener will not be invoked.
    input.setSubmittedValue("value");
    input.setValue("value");
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

  public void uiInputValidate4Test(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIInput input = (UIInput) createComponent();
    input.setRendererType(null);
    FacesContext context = getFacesContext();
    UIViewRoot root = getApplication().getViewHandler().createView(context,
        "/root");
    context.setViewRoot(root);
    root.getChildren().add(input);

    Converter converter = new TCKConverter();
    input.setConverter(converter);

    input.setSubmittedValue("value");

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

    if (JSFTestUtil.getAsArray(context.getMessages()).length != 1) {
      out.println(JSFTestUtil.FAIL + " No messages queued as required in the "
          + "case of conversion failure.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void uiInputValidate5Test(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIInput input = (UIInput) createComponent();
    input.setRendererType(null);
    FacesContext context = getFacesContext();
    Application application = getApplication();
    UIViewRoot root = application.getViewHandler().createView(context, "/root");
    context.setViewRoot(root);
    root.getChildren().add(input);

    application.addConverter(Boolean.class,
        "com.sun.ts.tests.jsf.api.javax_faces.component.common.TCKConverter");

    // setup value binding
    request.setAttribute("simple", new TestBean());

    input.setValueExpression("value", this.getVExpression(context,
        "#{requestScope.simple.boolWrapProp}", boolean.class));

    // run through validation
    input.setSubmittedValue("value");

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

    if (JSFTestUtil.getAsArray(context.getMessages()).length != 1) {
      out.println(JSFTestUtil.FAIL + " No messages queued as required in the "
          + "case of conversion failure.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  /* @since 1.2 */
  public void uiInputResetValueTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    UIInput input = (UIInput) createComponent();

    input.setValue("currentValue");
    input.setSubmittedValue("submittedValue");
    input.setLocalValueSet(true);
    input.setValid(false);

    input.resetValue();

    // resetValue() must pass null to setValue() and setSubmittedValue()
    // resetValue() must pass false to setLocaleValueSet()
    // resetValue() must pass true to setValid()

    boolean passed = true;
    if (input.getValue() != null) {
      out.println(JSFTestUtil.FAIL + " resetValue() called, but getValue()"
          + "returned a non-null value.");
      passed = false;
    }

    if (input.getSubmittedValue() != null) {
      out.println(JSFTestUtil.FAIL + " resetValue() called, but "
          + "getSubmittedValue() returned a non-null value.");
      passed = false;
    }

    if (input.isLocalValueSet()) {
      out.println(JSFTestUtil.FAIL + " resetValue() called, but "
          + "isLocalValueSet() returned true.");
      passed = false;
    }

    if (!input.isValid()) {
      out.println(JSFTestUtil.FAIL + " resetValue() called, but "
          + "isValid() returned false");
      passed = false;
    }

    if (passed) {
      out.println(JSFTestUtil.PASS);
    }

  } // END uiInputResetValueTest

  // --------------------------------------------------------- Private Methods

  private ValueExpression getVExpression(FacesContext context, String expr,
      Class expType) {
    ValueExpression ve = context.getApplication().getExpressionFactory()
        .createValueExpression(FacesContext.getCurrentInstance().getELContext(),
            expr, expType);

    return ve;

  }

  public void uiInputSetGetConverterMessTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIInput input = (UIInput) createComponent();

    String golden = "Bytor";
    input.setConverterMessage(golden);
    String result = input.getConverterMessage();

    if (!result.equals(golden)) {
      out.println(JSFTestUtil.FAIL
          + " Unexpected Value returned from getConverterMessage() "
          + JSFTestUtil.NL + "Expected: " + golden + JSFTestUtil.NL
          + "Received: " + result);
    } else {
      out.println(JSFTestUtil.PASS);
    }

  } // end uiInputSetGetConverterMessTest

  public void uiInputSetGetValidatorMessTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIInput input = (UIInput) createComponent();

    String golden = "SnowDog";
    input.setValidatorMessage(golden);
    String result = input.getValidatorMessage();

    if (!result.equals(golden)) {
      out.println(JSFTestUtil.FAIL
          + " Unexpected Value returned from getValidatorMessage() "
          + JSFTestUtil.NL + "Expected: " + golden + JSFTestUtil.NL
          + "Received: " + result);
    } else {
      out.println(JSFTestUtil.PASS);
    }

  } // end uiInputSetGetValidatorMessTest

  public void uiInputSetGetRequiredMessTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIInput input = (UIInput) createComponent();

    String golden = "ZoSo";
    input.setRequiredMessage(golden);
    String result = input.getRequiredMessage();

    if (!result.equals(golden)) {
      out.println(JSFTestUtil.FAIL
          + " Unexpected Value returned from getRequiredMessage() "
          + JSFTestUtil.NL + "Expected: " + golden + JSFTestUtil.NL
          + "Received: " + result);
    } else {
      out.println(JSFTestUtil.PASS);
    }

  } // end uiInputSetGetRequiredMessTest

  // --------------------------------------------------------- Private Classes

  private static class Renderer1 extends Renderer {

    boolean called;

    public Object getConvertedValue(FacesContext context, UIComponent component,
        Object submittedValue) throws ConverterException {
      called = true;
      if (submittedValue.equals("null"))
        return null;

      return submittedValue;
    }

    public void reset() {
      called = false;
    }

    public boolean wasCalled() {
      return called;
    }
  }

}
