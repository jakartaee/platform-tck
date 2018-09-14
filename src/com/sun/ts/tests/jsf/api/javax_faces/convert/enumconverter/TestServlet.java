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

package com.sun.ts.tests.jsf.api.javax_faces.convert.enumconverter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.convert.EnumConverter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

public final class TestServlet extends HttpTCKServlet {

  enum Suit {
    SPADES, CLUBS, HEARTS, DIAMONDS
  }

  enum Royal {
    JACK, QUEEN, KING
  }

  // EnumConverter.isTransient()
  // EnumConverter.setTransient()
  public void enumConverterIsSetTransientTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    EnumConverter noArgsConverter = new EnumConverter();
    EnumConverter argConverter = new EnumConverter(Suit.class);

    // by default EnumConverter is not transient
    if (noArgsConverter.isTransient()) {
      out.println("Test FAILED. noArgsConverter: Expected EnumConverter.isT"
          + "ransient() to return false on a newly created instance.");
      return;
    }

    if (argConverter.isTransient()) {
      out.println(JSFTestUtil.FAIL + " argConverter: Expected EnumConverter.isT"
          + "ransient() to return false on a newly created instance.");
      return;
    }

    noArgsConverter.setTransient(false);
    argConverter.setTransient(false);

    if (noArgsConverter.isTransient()) {
      out.println(
          "Test FAILED. noArgsConverter: Expected EnumConverter.isTransient()"
              + " to return false when EnumConverter.setTransient(false) was"
              + " called.");
      return;
    }

    if (argConverter.isTransient()) {
      out.println(
          "Test FAILED. argConverter: Expected EnumConverter.isTransient()"
              + " to return false when EnumConverter.setTransient(false) was"
              + " called.");
      return;
    }

    noArgsConverter.setTransient(true);
    argConverter.setTransient(true);

    if (!noArgsConverter.isTransient()) {
      out.println(
          "Test FAILED. noArgsConverter: Expected EnumConverter.isTransient()"
              + " to return true when EnumConverter.setTransient(true) was"
              + " called.");
      return;
    }

    if (!argConverter.isTransient()) {
      out.println(
          "Test FAILED. argConverter: Expected EnumConverter.isTransient()"
              + " to return true when EnumConverter.setTransient(false) was"
              + " called.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void enumConverterGetAsObjectTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    UIComponent component = getApplication()
        .createComponent(UIInput.COMPONENT_TYPE);

    EnumConverter converter = new EnumConverter(Suit.class);
    Object obj = converter.getAsObject(context, component, "HEARTS");

    if (!Suit.HEARTS.equals(obj)) {
      out.println("Test FAILED. Bad conversion of String to enum object.");
      return;
    }

    if (converter.getAsObject(context, component, null) != null) {
      out.println(
          "Test FAILED. Non-null value returned when String to be converted is null");
      return;
    }

    if (converter.getAsObject(context, component, "") != null) {
      out.println("Test FAILED. Non-null value returned when String to be "
          + "converted is zero-length");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void enumConverterGetAsObjectCETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    boolean pass1 = false;
    boolean pass2 = false;

    EnumConverter noArgsConverter = new EnumConverter();
    EnumConverter argConverter = new EnumConverter(Suit.class);

    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    UIComponent component = getApplication()
        .createComponent(UIInput.COMPONENT_TYPE);

    // ConverterException when target class is null
    try {
      noArgsConverter.getAsObject(context, component, "some string");
    } catch (ConverterException ce) {
      out.println(
          "Conversion of value for converter constructed with no target "
              + "class failed as expected");
      pass1 = true;
    }

    // ConverterException when value to be converted does not exist in the enum
    // class
    try {
      argConverter.getAsObject(context, component, "SMARTS");
    } catch (ConverterException ce) {
      out.println("Conversion of invalid value failed as expected");
      pass2 = true;
    }

    out.println((pass1 && pass2) ? "Test PASSED," : "Test FAILED.");
  }

  public void enumConverterGetAsObjectNPE1Test(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    UIComponent component = getApplication()
        .createComponent(UIInput.COMPONENT_TYPE);

    EnumConverter converter = new EnumConverter(Suit.class);
    try {
      converter.getAsObject(null, component, "SPADES");
    } catch (NullPointerException npe) {
      out.println(
          "Test PASSED. Conversion with null FacesContext failed as expected");
      return;
    }

    out.println("Test FAILED. Conversion with null FacesContext was allowed.");
  }

  public void enumConverterGetAsObjectNPE2Test(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();

    EnumConverter converter = new EnumConverter(Suit.class);
    try {
      converter.getAsObject(context, null, "SPADES");
    } catch (NullPointerException npe) {
      out.println(
          "Test PASSED. Conversion with null component failed as expected");
      return;
    }

    out.println("Test FAILED. Conversion with null component was allowed.");
  }

  public void enumConverterGetAsStringTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    UIComponent component = getApplication()
        .createComponent(UIInput.COMPONENT_TYPE);

    EnumConverter converter = new EnumConverter(Suit.class);
    String str = converter.getAsString(context, component, Suit.CLUBS);

    if (!str.equals("CLUBS")) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Bad conversion of enum object to String." + JSFTestUtil.NL
          + "Expected value = CLUBS" + JSFTestUtil.NL + "Computed value = "
          + str);
      return;
    }

    str = converter.getAsString(context, component, null);

    if (!str.isEmpty()) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Expected empty String whenpassing in null as value."
          + JSFTestUtil.NL + "Received: " + str);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void enumConverterGetAsStringCETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    String ExpectedException = "ConverterException";
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;

    EnumConverter argsConverter = new EnumConverter(Suit.class);
    EnumConverter noArgsConverter = new EnumConverter();

    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    UIComponent component = getApplication()
        .createComponent(UIInput.COMPONENT_TYPE);

    // ConverterException when value to be converted is not an enum constant
    try {
      argsConverter.getAsString(context, component, "some string");
      out.println("Conversion to String of value that is not an enum constant "
          + "class did thorw a " + ExpectedException);

    } catch (ConverterException ce) {
      pass1 = true;

    } catch (Exception e) {
      out.println(
          "Conversion to String of value that is not an enum constant throw incorrect "
              + "Exception: " + JSFTestUtil.NL + "Expected: "
              + ExpectedException + JSFTestUtil.NL + "Received: "
              + e.getClass().getSimpleName());
      e.printStackTrace();
    }

    // ConverterException when value to be converted is a constant from a
    // different enum class
    try {
      argsConverter.getAsString(context, component, Royal.JACK);
      out.println(
          "Conversion to String of constant from a different enum class "
              + "did thorw a " + ExpectedException);

    } catch (ConverterException ce) {
      pass2 = true;

    } catch (Exception e) {
      out.println(
          "Conversion to String of constant from a different enum class throw incorrect "
              + "Exception: " + JSFTestUtil.NL + "Expected: "
              + ExpectedException + JSFTestUtil.NL + "Received: "
              + e.getClass().getSimpleName());
      e.printStackTrace();
    }

    // ConverterException when target class is null
    try {
      noArgsConverter.getAsString(context, component, Suit.DIAMONDS);
      out.println("Conversion to String of constant when target class is null "
          + "did thorw a " + ExpectedException);

    } catch (ConverterException ce) {
      pass3 = true;

    } catch (Exception e) {
      out.println(
          "Conversion to String of constant when target class is null throw incorrect "
              + "Exception: " + JSFTestUtil.NL + "Expected: "
              + ExpectedException + JSFTestUtil.NL + "Received: "
              + e.getClass().getSimpleName());
      e.printStackTrace();
    }

    out.println((pass1 && pass2 && pass3) ? "Test PASSED," : "Test FAILED.");

  } // End enumConverterGetAsStringCETest

  public void enumConverterGetAsStringNPE1Test(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    UIComponent component = getApplication()
        .createComponent(UIInput.COMPONENT_TYPE);

    EnumConverter converter = new EnumConverter(Suit.class);
    try {
      converter.getAsString(null, component, Suit.SPADES);
    } catch (NullPointerException npe) {
      out.println(
          "Test PASSED. Conversion to String with null FacesContext failed as expected");
      return;
    }

    out.println(
        "Test FAILED. Conversion to String with null FacesContext was allowed.");
  }

  public void enumConverterGetAsStringNPE2Test(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();

    EnumConverter converter = new EnumConverter(Suit.class);
    try {
      converter.getAsString(context, null, Suit.SPADES);
    } catch (NullPointerException npe) {
      out.println(
          "Test PASSED. Conversion to String with null component failed as expected");
      return;
    }

    out.println(
        "Test FAILED. Conversion to String with null component was allowed.");
  }
}
