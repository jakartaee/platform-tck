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

import java.io.IOException;
import java.io.PrintWriter;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.ValueHolder;
import javax.faces.convert.Converter;
import javax.faces.convert.NumberConverter;
import javax.faces.el.MethodBinding;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;
import javax.faces.validator.LengthValidator;
import javax.faces.validator.Validator;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

public abstract class BaseEditableValueHolderTestServlet
    extends BaseValueHolderTestServlet {

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

  // ------------------------------------------------------------ Test Methods

  // ------------------------------------------------------------- ValueHolder

  // ValueHolder.{get,set}Converter
  public void valueHolderGetSetConverterTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ValueHolder holder = (ValueHolder) createComponent();

    Converter converter = new NumberConverter();
    holder.setConverter(converter);

    Object result = holder.getConverter();
    if (!converter.equals(result)) {
      out.println(JSFTestUtil.FAIL + " Expected getConverter() to return the"
          + " Converter set via setConverter().");
      out.println("Value expected: " + converter);
      out.println("value received: " + result);
      return;
    }

    // make sure we can set null
    holder.setConverter(null);
    if (holder.getConverter() != null) {
      out.println(JSFTestUtil.FAIL + " Expected getConverter() to return null"
          + " as set via setConverter().");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void valueHolderGetSetValueTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ValueHolder holder = (ValueHolder) createComponent();
    holder.setValue("value");

    if (!"value".equals(holder.getValue())) {
      out.println(JSFTestUtil.FAIL + " Value returned by getValue() was"
          + " not the same as set via setValue().");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // ----------------------------------------------------- EditableValueHolder

  public void editableValueHolderIsSetValidTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    EditableValueHolder holder = (EditableValueHolder) createComponent();

    holder.setValid(false);
    if (holder.isValid()) {
      out.println(JSFTestUtil.FAIL + " Expected isValid() to return false "
          + "after having called setValid(true).");
      return;
    }

    holder.setValid(true);
    if (!holder.isValid()) {
      out.println(JSFTestUtil.FAIL + " Expected isValid() to return true "
          + "after having called setValid(false).");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void editableValueHolderAddGetRemoveValueChangeListenerTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    EditableValueHolder holder = (EditableValueHolder) createComponent();

    TCKValueChangeListener listener = new TCKValueChangeListener("id");

    holder.addValueChangeListener(listener);

    ValueChangeListener[] listeners = holder.getValueChangeListeners();
    if (listeners.length != 1) {
      out.println("Test FAILED[1].  Expected the length of the array returned"
          + " by getValueChangedListeners() to be one.");
      out.println("Actual length: " + listeners.length);
      return;
    }

    holder.removeValueChangeListener(new TCKValueChangeListener("id2"));
    listeners = holder.getValueChangeListeners();
    if (listeners.length != 1) {
      out.println("Test FAILED[2].  Expected the length of the array returned"
          + " by getValueChangedListeners() to be one.");
      out.println("Actual length: " + listeners.length);
      return;
    }

    holder.removeValueChangeListener(listener);
    listeners = holder.getValueChangeListeners();
    if (listeners.length != 0) {
      out.println(
          JSFTestUtil.FAIL + " Expected the length of the array returned"
              + " by getValueChangedListeners() to be zero after removing"
              + " the only listener.");
      out.println("Actual length: " + listeners.length);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void editableValueHolderGetSetValueChangeListenerTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    EditableValueHolder holder = (EditableValueHolder) createComponent();

    String ref = "requestScope.tckValueChangeRef.processValueChange";

    MethodBinding binding = getApplication().createMethodBinding(
        "#{requestScope.tckValueChangeRef.processValueChange}",
        new Class[] { ValueChangeEvent.class });
    holder.setValueChangeListener(binding);

    if (binding != holder.getValueChangeListener()) {
      out.println(JSFTestUtil.FAIL + " getValueChangeListener() didn't return"
          + " the value as set via setValueChangeListener().");
      out.println("Expected: " + ref);
      out.println("Received: " + holder.getValueChangeListener());
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void editableValueHolderAddGetRemoveValidatorTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    EditableValueHolder holder = (EditableValueHolder) createComponent();

    Validator validator = new LengthValidator(10);

    holder.addValidator(validator);

    Validator[] validators = holder.getValidators();

    if (validators.length != 1) {
      out.println(JSFTestUtil.FAIL + " After adding one Validator, expected"
          + " the length of the array of Validators returned by "
          + "getValidators() to be 1.");
      out.println("Length received: " + validators.length);
      return;
    }

    if (validator != validators[0]) {
      out.println(JSFTestUtil.FAIL + " The Validator in the array returned"
          + " by getValidators() is not the same validator instance"
          + " added by addValidator().");
      out.println("Expected: " + validator);
      out.println("Received: " + validators[0]);
      return;
    }

    holder.removeValidator(validator);

    validators = holder.getValidators();

    if (validators.length != 0) {
      out.println(JSFTestUtil.FAIL + " After removing the single Validator, "
          + "expected instance, the length of the array returned by"
          + "getValidators() should have been zero.");
      out.println("Length received: " + validators.length);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void editableValueHolderIsSetRequiredTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    EditableValueHolder holder = (EditableValueHolder) createComponent();

    holder.setRequired(false);

    if (holder.isRequired()) {
      out.println(JSFTestUtil.FAIL + " Expected isRequired() to return false"
          + " after having explicitly set it as such via setRequired().");
      return;
    }

    holder.setRequired(true);
    if (!holder.isRequired()) {
      out.println(JSFTestUtil.FAIL + " Expected isRequired() to return true"
          + " after having explicitly set it as such via setRequired().");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void editableValueHolderGetSetSubmittedValueTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    EditableValueHolder holder = (EditableValueHolder) createComponent();

    holder.setSubmittedValue(Boolean.TRUE);

    if (!((Boolean) holder.getSubmittedValue())) {
      out.println(
          JSFTestUtil.FAIL + " Value returned by getSubmittedValue() was"
              + " not the same as set by setSubmittedValue().");
      out.println("Expected: Boolean.TRUE");
      out.println("Received: Boolean.FALSE");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // EditableValueHolder.addValidator() throws NullPointerException()
  public void editableValueHolderAddValidatorNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    EditableValueHolder holder = (EditableValueHolder) createComponent();

    JSFTestUtil.checkForNPE(holder.getClass(), "addValidator",
        new Class<?>[] { Validator.class }, new Object[] { null }, out);
  }

  // EditableValueHolder.addValueChangeListener() throws
  // NullPointerException()
  public void editableValueHolderAddValueChangeListenerNPETest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    EditableValueHolder holder = (EditableValueHolder) createComponent();

    JSFTestUtil.checkForNPE(holder.getClass(), "addValueChangeListener",
        new Class<?>[] { ValueChangeListener.class }, new Object[] { null },
        out);
  }

  // EditableValueHolder.removeValueChangeListener() throws
  // NullPointerException()
  public void editableValueHolderRemoveValueChangeListenerNPETest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    EditableValueHolder holder = (EditableValueHolder) createComponent();

    JSFTestUtil.checkForNPE(holder.getClass(), "removeValueChangeListener",
        new Class<?>[] { ValueChangeListener.class }, new Object[] { null },
        out);
  }

  public void editableValueHolderIsSetImmediateTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    EditableValueHolder holder = (EditableValueHolder) createComponent();

    if (holder.isImmediate()) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Default setting for 'isImmediate() " + " MUST be false!");

    } else {
      holder.setImmediate(true);
      if (!holder.isImmediate()) {
        out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
            + "Unxpected value returned from isImmediate()" + JSFTestUtil.NL
            + "Expected: true" + JSFTestUtil.NL + "Received: false");

      } else {
        out.println(JSFTestUtil.PASS);
      }
    }
  }

  public void editableValueHolderIsSetLocalValueSetTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    EditableValueHolder holder = (EditableValueHolder) createComponent();
    boolean golden = false;
    holder.setLocalValueSet(golden);

    // Initial call should receive 'false'.
    boolean result = holder.isLocalValueSet();
    if (result) {
      out.println(JSFTestUtil.FAIL
          + " Value returned by EditableValueHolder.isLocalValueSet() was"
          + " not the same as set by EditableValueHolder.setLocalValueSet().");
      out.println("Expected: " + golden);
      out.println("Received: " + result);
      return;
    }

    // Second call should receive 'true', after calling setValue().
    holder.setValue("test_value");
    result = holder.isLocalValueSet();
    if (!result) {
      out.println(JSFTestUtil.FAIL
          + " Value returned by EditableValueHolder.isLocalValueSet() was"
          + " not the same as set by EditableValueHolder.setLocalValueSet().");
      out.println("Expected: true");
      out.println("Received: " + result);
      return;
    }

    out.println(JSFTestUtil.PASS);

  } // editableValueHolderIsSetLocalValueSetTest

}
