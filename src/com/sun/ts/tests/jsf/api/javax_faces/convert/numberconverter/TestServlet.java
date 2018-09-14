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

package com.sun.ts.tests.jsf.api.javax_faces.convert.numberconverter;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.convert.NumberConverter;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public final class TestServlet extends HttpTCKServlet {

  private static Class currencyClass;
  static {
    try {
      currencyClass = Thread.currentThread().getContextClassLoader()
          .loadClass("java.util.Currency");
    } catch (ClassNotFoundException cnfe) {
      ;
    }
  }

  private static final String[] TYPES = { "number", "currency", "percent" };

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }

  // ------------------------------------------- Test Methods ----

  // NumberConverter.{get,set}CurrencyCode()
  public void numConverterGetSetCurrencyCodeTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    NumberConverter converter = new NumberConverter();
    String currencyCode = "USD";
    converter.setCurrencyCode(currencyCode);
    if (!currencyCode.equals(converter.getCurrencyCode())) {
      out.println(JSFTestUtil.FAIL + " Expected NumberConverter."
          + "getCurrencyCode() to return the value 'USD' to be "
          + "returned as set by setCurrencyCode().");
      out.println("Received: " + converter.getCurrencyCode());
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // NumberConverter.{get,set}CurrencySymbol()
  public void numConverterGetSetCurrencySymbolTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    NumberConverter converter = new NumberConverter();
    String currencySymbol = "$";
    converter.setCurrencySymbol(currencySymbol);
    if (!currencySymbol.equals(converter.getCurrencySymbol())) {
      out.println(JSFTestUtil.FAIL + " Expected NumberConverter."
          + "getCurrencySymbol() to return the value '$' to be "
          + "returned as set by setCurrencySymbol().");
      out.println("Received: " + converter.getCurrencySymbol());
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // NumberConverter.{get,set}Locale()
  public void numConverterGetSetLocaleTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    NumberConverter converter = new NumberConverter();
    getFacesContext().getViewRoot().setLocale(Locale.CANADA_FRENCH);

    Locale locale = converter.getLocale();
    if (!Locale.CANADA_FRENCH.equals(locale)) {
      out.println(JSFTestUtil.FAIL + " Expected NumberConverter.getLocale()"
          + "to return the locale stored in the UIViewRoot of"
          + " the current FacesContext if not already explicitly" + " set.");
      out.println("Expected: " + Locale.CANADA_FRENCH.getDisplayName());
      out.println(
          "Received: " + locale != null ? locale.getDisplayName() : "null");
      return;
    }

    converter.setLocale(Locale.ENGLISH);
    locale = converter.getLocale();
    if (!Locale.ENGLISH.equals(locale)) {
      out.println(JSFTestUtil.FAIL + " Unexpected return value from Number"
          + "Converter.getLocale() after explicitly setting the "
          + "locake to Locale.ENGLISH.");
      out.println(
          "Received: " + locale != null ? locale.getDisplayName() : "null");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // NumberConverter.{get,set}MaxFractionDigits
  public void numConverterGetSetMaxFractionDigitsTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    NumberConverter converter = new NumberConverter();

    converter.setMaxFractionDigits(3);
    int result = converter.getMaxFractionDigits();

    if (result != 3) {
      out.println(JSFTestUtil.FAIL + " Unexpected value returned from"
          + " getMaxFractionDigits() after explicitly setting "
          + "the value to '3'.");
      out.println("Received: " + result);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // NumberConverter.{get,set}MaxIntegerDigits
  public void numConverterGetSetMaxIntegerDigitsTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    NumberConverter converter = new NumberConverter();

    converter.setMaxIntegerDigits(3);
    int result = converter.getMaxIntegerDigits();

    if (result != 3) {
      out.println(JSFTestUtil.FAIL + " Unexpected value returned from"
          + " getMaxIntegerDigits() after explicitly setting "
          + "the value to '3'.");
      out.println("Received: " + result);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // NumberConverter.{get,set}MinFractionDigits
  public void numConverterGetSetMinFractionDigitsTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    NumberConverter converter = new NumberConverter();

    converter.setMinFractionDigits(3);
    int result = converter.getMinFractionDigits();

    if (result != 3) {
      out.println(JSFTestUtil.FAIL + " Unexpected value returned from"
          + " getMinFractionDigits() after explicitly setting "
          + "the value to '3'.");
      out.println("Received: " + result);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // NumberConverter.{get,set}MinIntegerDigits
  public void numConverterGetSetMinIntegerDigitsTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    NumberConverter converter = new NumberConverter();

    converter.setMinIntegerDigits(3);
    int result = converter.getMinIntegerDigits();

    if (result != 3) {
      out.println(JSFTestUtil.FAIL + " Unexpected value returned from"
          + " getMinIntegerDigits() after explicitly setting "
          + "the value to '3'.");
      out.println("Received: " + result);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // NumberConverter.{get,set}Pattern
  public void numConverterGetSetPatternTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    NumberConverter converter = new NumberConverter();

    String pattern = "#,##0.##";
    converter.setPattern(pattern);
    String result = converter.getPattern();

    if (!pattern.equals(result)) {
      out.println(JSFTestUtil.FAIL + " Unexpected value returned from "
          + "getPattern() after explicitly setting the value to " + pattern);
      out.println("Received: " + result);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // NumberConverter.{get,set}Type
  public void numConverterGetSetTypeTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    NumberConverter converter = new NumberConverter();

    if (!"number".equals(converter.getType())) {
      out.println(JSFTestUtil.FAIL + " Expected default value of "
          + "NumberConverter.getType() to be 'number'.");
      out.println("Received: " + converter.getType());
      return;
    }

    String type = "integer";
    converter.setType(type);
    String result = converter.getType();

    if (!type.equals(result)) {
      out.println(JSFTestUtil.FAIL + " Unexpected value returned from "
          + "getType() after explicitly setting the value to " + type);
      out.println("Received: " + result);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // NumberConverter.{is,set}GroupingUsed
  public void numConverterIsSetGroupingUsedTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    NumberConverter converter = new NumberConverter();

    if (!converter.isGroupingUsed()) {
      out.println(JSFTestUtil.FAIL + " Expected the default value of"
          + " NumberConverter.isGroupingUsed() to be true.");
      return;
    }

    converter.setGroupingUsed(false);
    if (converter.isGroupingUsed()) {
      out.println(JSFTestUtil.FAIL + " Expected isGroupingUsed() to return"
          + " false after having explicitly set it as such.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // NumberConverter.{is,set}IntegerOnly
  public void numConverterIsSetIntegerOnlyTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    NumberConverter converter = new NumberConverter();

    if (converter.isIntegerOnly()) {
      out.println(JSFTestUtil.FAIL + " Expected the default value of"
          + " NumberConverter.isIntegerOnly() to be false.");
      return;
    }

    converter.setGroupingUsed(true);
    if (!converter.isGroupingUsed()) {
      out.println(JSFTestUtil.FAIL + " Expected isIntegerOnly() to return"
          + " true after having explicitly set it as such.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // NumberConverter.{is,set}Transient
  public void numConverterIsSetTransientTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    NumberConverter converter = new NumberConverter();

    converter.setTransient(false);
    if (converter.isTransient()) {
      out.println(JSFTestUtil.FAIL + " Expected isTransient() to return"
          + " false after having explicitly set it as such.");
      return;
    }

    converter.setTransient(true);
    if (!converter.isTransient()) {
      out.println(JSFTestUtil.FAIL + " Expected isTransient() to return"
          + " true after having explicitly set it as such.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // NumberConverter.getAsObject() returns null of input
  // is null or a zero-length String
  public void numConverterGetAsObjectNullZeroLengthTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    NumberConverter converter = new NumberConverter();
    FacesContext context = getFacesContext();
    UIComponent comp = new UIInput();

    Object result = converter.getAsObject(context, comp, null);
    if (result != null) {
      out.println(JSFTestUtil.FAIL + " Expected NumberConverter.getAsObject() "
          + "to return null if a null String value was passed.");
      out.println("Recevied: " + result.toString());
      return;
    }

    result = converter.getAsObject(context, comp, "");
    if (result != null) {
      out.println(JSFTestUtil.FAIL + " Expected NumberConverter.getAsObject() "
          + "to retun null if a zero-length String argument was passed.");
      out.println("Received: " + result.toString());
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // NumberConveter.getAsObject() interaction with Locales
  public void numConverterGetAsObjectLocaleTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    NumberConverter converter = new NumberConverter();

    // If Locale is not explictly set, convert the Number using the Locale
    // from the UIViewRoot
    NumberFormat parser = NumberFormat.getNumberInstance(Locale.FRENCH);
    Object control;
    try {
      control = parser.parse("5.5");
    } catch (ParseException pe) {
      throw new ServletException("Unexpected Exception", pe);
    }

    if (control == null) {
      out.println(JSFTestUtil.FAIL + " Unable to obtain result from"
          + " NumberFormat.parse(String).");
      return;
    }

    FacesContext context = getFacesContext();
    UIComponent comp = new UIInput();
    context.getViewRoot().setLocale(Locale.FRENCH);

    try {
      Object result = converter.getAsObject(context, comp, "5.5");
      if (!control.equals(result)) {
        out.println(JSFTestUtil.FAIL
            + " The value returned by NumberConverter.getAsObject()"
            + " for '5.5' using Locale.FRENCH doesn't match that"
            + " returned by NumberFormat.parse() using the same settings.");
        out.println("Expected: " + control);
        out.println("Received: " + result);
        return;
      }
    } catch (Exception e) {
      throw new ServletException(JSFTestUtil.FAIL + " Unexpected Exception"
          + "thrown when calling NumberConverter." + "getAsObject().", e);
    }

    // If Locale is explicitly set, then the converter should use that
    // locale vs the one in the ViewRoot
    converter.setLocale(Locale.ENGLISH);
    parser = NumberFormat.getNumberInstance(Locale.ENGLISH);
    try {
      control = parser.parse("5.5");
    } catch (ParseException pe) {
      throw new ServletException("Unexpected Exception", pe);
    }

    if (control == null) {
      out.println(JSFTestUtil.FAIL + " Unable to obtain result from"
          + " NumberFormat.parse(String).");
      return;
    }

    try {
      Object result = converter.getAsObject(context, comp, "5.5");
      if (!control.equals(result)) {
        out.println(JSFTestUtil.FAIL
            + " The value returned by NumberConverter.getAsObject()"
            + " for '5.5' using Locale.ENGLISH doesn't match that"
            + " returned by NumberFormat.parse() using the same settings.");
        out.println("Expected: " + control);
        out.println("Received: " + result);
        return;
      }
    } catch (Exception e) {
      throw new ServletException(JSFTestUtil.FAIL + " Unexpected Exception"
          + "thrown when calling NumberConverter." + "getAsObject().", e);
    }

    out.println(JSFTestUtil.PASS);
  }

  // NumberConverter.getAsObject() with pattern specified
  public void numConverterGetAsObjectPatternTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    String pattern = "$#,##0.00;($#.##0.00)";
    String inputValue = "$12.50";
    NumberConverter converter = new NumberConverter();
    converter.setLocale(Locale.ENGLISH);
    converter.setPattern(pattern);

    DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
    DecimalFormat parser = new DecimalFormat(pattern, symbols);

    Object control;
    try {
      control = parser.parse(inputValue);
    } catch (ParseException pe) {
      throw new ServletException("Unexpected Exception", pe);
    }

    if (control == null) {
      out.println(JSFTestUtil.FAIL + " Unable to obtain result from"
          + " DecimalFormat.parse(String).");
      return;
    }

    try {
      Object result = converter.getAsObject(getFacesContext(), new UIInput(),
          inputValue);
      if (!control.equals(result)) {
        out.println(JSFTestUtil.FAIL + " The value returned by NumberConverter"
            + ".getAsObject() using a pattern of '" + pattern
            + " was not the same value as that returned by"
            + " DecimalFormat.");
        out.println("Expected: " + control);
        out.println("Received: " + result);
        return;
      }
    } catch (Exception e) {
      throw new ServletException(JSFTestUtil.FAIL + " Unexpected Exception"
          + "thrown when calling NumberConverter." + "getAsObject().", e);
    }

    // NumberConverter.getAsObject() must ignore the value of the type
    // property if pattern is set
    for (int i = 0; i < TYPES.length; i++) {
      converter.setType(TYPES[i]);
      try {
        Object result = converter.getAsObject(getFacesContext(), new UIInput(),
            inputValue);
        if (!control.equals(result)) {
          out.println("Type seems to have impacted the return value"
              + "of NumberFormat.getAsObject() when a pattern"
              + "has been specified.");
          out.println("Pattern: " + pattern);
          out.println("Type: " + TYPES[i] + " (should have been ignored)");
          out.println("Expected: " + control);
          out.println("Received: " + result);
          return;
        }
      } catch (Exception e) {
        throw new ServletException(JSFTestUtil.FAIL + " Unexpected Exception"
            + "thrown when calling NumberConverter." + "getAsObject().", e);
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  // NumberConverter.getAsObject() with type specified.
  public void numConverterGetAsObjectTypeTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    NumberConverter converter = new NumberConverter();
    Locale locale = new Locale("en", "US");
    converter.setLocale(locale);
    Class[] params = { Locale.class };
    Method[] numMethods;
    try {
      numMethods = new Method[] {
          NumberFormat.class.getDeclaredMethod("getNumberInstance", params),
          NumberFormat.class.getDeclaredMethod("getCurrencyInstance", params),
          NumberFormat.class.getDeclaredMethod("getPercentInstance", params) };
    } catch (NoSuchMethodException nsme) {
      throw new ServletException("Test initialization failed!", nsme);
    }

    String[] inputs = { "1,234", "$1,234.00", "1.5%" };

    for (int i = 0; i < TYPES.length; i++) {
      converter.setType(TYPES[i]);
      NumberFormat parser;
      try {
        parser = (NumberFormat) numMethods[i].invoke(null,
            new Object[] { locale });
      } catch (Exception e) {
        throw new ServletException("Unexpected Exception! " + e.toString(), e);
      }

      if (parser == null) {
        out.println(
            JSFTestUtil.FAIL + " Unable to obtain NumberFormat " + "instance.");
        return;
      }

      Object control;
      try {
        control = parser.parse(inputs[i]);
      } catch (ParseException pe) {
        throw new ServletException("Unexpected Exception", pe);
      }

      if (control == null) {
        out.println(JSFTestUtil.FAIL + " Unable to obtain result from"
            + " DecimalFormat.parse(String).");
        return;
      }

      // Now get the result from the NumberConverter
      try {
        Object result = converter.getAsObject(getFacesContext(), new UIInput(),
            inputs[i]);
        if (!control.equals(result)) {
          out.println("Unexpected result returned from NumberCon"
              + "verter.getAsObject with type specified as" + " '" + TYPES[i]
              + "' with an input value of" + "'" + inputs[i] + "'");
          out.println("Expected: " + control);
          out.println("Received: " + result);
        }
      } catch (Exception e) {
        throw new ServletException(JSFTestUtil.FAIL + " Unexpected Exception"
            + "thrown when calling NumberConverter." + "getAsObject().", e);
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  // NumberConverter.getAsObject() with setParseIntegerOnly()
  public void numConverterGetAsObjectParseIntOnlyTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    NumberConverter converter = new NumberConverter();
    Locale locale = new Locale("en", "US");
    converter.setLocale(locale);
    converter.setIntegerOnly(true);
    Class[] params = { Locale.class };
    Method[] numMethods;
    try {
      numMethods = new Method[] {
          NumberFormat.class.getDeclaredMethod("getNumberInstance", params),
          NumberFormat.class.getDeclaredMethod("getCurrencyInstance", params),
          NumberFormat.class.getDeclaredMethod("getPercentInstance", params) };
    } catch (NoSuchMethodException nsme) {
      throw new ServletException("Test initialization failed!", nsme);
    }

    String[] inputs = { "1,234.56", "$1,234.56",
        // "150%" --> commented out due to J2SE bug 4663895
    };

    String types[] = { "number", "currency" };

    for (int i = 0; i < types.length; i++) {
      converter.setType(TYPES[i]);
      NumberFormat parser;
      try {
        parser = (NumberFormat) numMethods[i].invoke(null,
            new Object[] { locale });
      } catch (Exception e) {
        throw new ServletException("Unexpected Exception! " + e.toString(), e);
      }

      if (parser == null) {
        out.println(
            JSFTestUtil.FAIL + " Unable to obtain NumberFormat " + "instance.");
        return;
      }

      Object control;
      try {
        parser.setParseIntegerOnly(true);
        control = parser.parse(inputs[i]);
      } catch (ParseException pe) {
        throw new ServletException("Unexpected Exception. " + pe.toString(),
            pe);
      }

      if (control == null) {
        out.println(JSFTestUtil.FAIL + " Unable to obtain result from"
            + " DecimalFormat.parse(String).");
        return;
      }

      // Now get the result from the NumberConverter
      try {
        Object result = converter.getAsObject(getFacesContext(), new UIInput(),
            inputs[i]);
        if (!control.equals(result)) {
          out.println("Unexpected result returned from NumberCon"
              + "verter.getAsObject with type specified as" + " '" + types[i]
              + "' with an input value of" + "'" + inputs[i] + "'");
          out.println("Expected: " + control);
          out.println("Received: " + result);
        }
      } catch (Exception e) {
        throw new ServletException(JSFTestUtil.FAIL + " Unexpected Exception"
            + "thrown when calling NumberConverter." + "getAsObject().", e);
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  // ------------------------------------------------------- getAsString

  // NumberConverter.getAsString() returns zero-length String if input
  // is null or a zero-length String
  public void numConverterGetAsStringNullZeroLengthTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    NumberConverter converter = new NumberConverter();
    FacesContext context = getFacesContext();
    UIComponent comp = new UIInput();

    Object result = converter.getAsString(context, comp, null);
    if (!result.equals("")) {
      out.println(JSFTestUtil.FAIL + " Expected NumberConverter.getAsString() "
          + "to return a zero-length String if a null value was passed.");
      out.println("Recevied: " + result.toString());
      return;
    }

    result = converter.getAsString(context, comp, "");
    if (!"".equals(result)) {
      out.println(JSFTestUtil.FAIL + " Expected NumberConverter.getAsString() "
          + "to return a zero-length String if a zero-length String argument was passed.");
      out.println("Received: " + result);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // NumberConverter.getAsString() will convert String arguments to
  // Number (Long or Double) to be formatted.
  public void numConverterGetAsStringStringInputTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    NumberConverter converter = new NumberConverter();
    converter.setLocale(Locale.FRENCH);
    FacesContext context = getFacesContext();
    UIComponent comp = new UIInput();

    NumberFormat formatter = NumberFormat.getNumberInstance(Locale.FRENCH);
    formatter.setGroupingUsed(true);

    // if input is String, the result should be the same value as the
    // input value
    String control = "1235.5";
    try {
      Object result = converter.getAsString(context, comp, "1235.5");
      if (!control.equals(result)) {
        out.println(JSFTestUtil.FAIL + " NumberConverter.getAsString() failed"
            + " to return the same result using the same input arguments"
            + " as that returned by NumberFormat.format().");
        out.println("Expected: " + control);
        out.println("Received: " + result);
        return;
      }
    } catch (Exception e) {
      throw new ServletException("Unexpected Exception: " + e.toString(), e);
    }

    control = "1234";

    try {
      Object result = converter.getAsString(context, comp, "1234");
      if (!control.equals(result)) {
        out.println(JSFTestUtil.FAIL + " NumberConverter.getAsString() failed"
            + " to return the same result using the same input arguments"
            + " as that returned by NumberFormat.format().");
        out.println("Expected: " + control);
        out.println("Received: " + result);
        return;
      }
    } catch (Exception e) {
      throw new ServletException("Unexpected Exception: " + e.toString(), e);
    }

    out.println(JSFTestUtil.PASS);
  }

  // NumberConveter.getAsString() interaction with Locales
  public void numConverterGetAsStringLocaleTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    NumberConverter converter = new NumberConverter();

    // If Locale is not explictly set, convert the Number using the Locale
    // from the UIViewRoot
    NumberFormat formatter = NumberFormat.getNumberInstance(Locale.FRENCH);
    formatter.setGroupingUsed(true);
    String control;

    control = formatter.format(Double.valueOf(5.5));

    if (control == null) {
      out.println(JSFTestUtil.FAIL + " Unable to obtain result from"
          + " NumberFormat.format(Double).");
      return;
    }

    FacesContext context = getFacesContext();
    UIComponent comp = new UIInput();
    context.getViewRoot().setLocale(Locale.FRENCH);

    try {
      Object result = converter.getAsString(context, comp, Double.valueOf(5.5));
      if (!control.equals(result)) {
        out.println(JSFTestUtil.FAIL
            + " The value returned by NumberConverter.getAsString()"
            + " for '5.5' using Locale.FRENCH doesn't match that"
            + " returned by NumberFormat.format() using the same settings.");
        out.println("Expected: " + control);
        out.println("Received: " + result);
        return;
      }
    } catch (Exception e) {
      throw new ServletException(JSFTestUtil.FAIL + " Unexpected Exception"
          + "thrown when calling NumberConverter." + "getAsString().", e);
    }

    // If Locale is explicitly set, then the converter should use that
    // locale vs the one in the ViewRoot
    converter.setLocale(Locale.ENGLISH);
    formatter = NumberFormat.getNumberInstance(Locale.ENGLISH);

    control = formatter.format(Double.valueOf(5.5));

    if (control == null) {
      out.println(JSFTestUtil.FAIL + " Unable to obtain result from"
          + " NumberFormat.format(Double).");
      return;
    }

    try {
      Object result = converter.getAsString(context, comp, Double.valueOf(5.5));
      if (!control.equals(result)) {
        out.println(JSFTestUtil.FAIL
            + " The value returned by NumberConverter.getAsString()"
            + " for '5.5' using Locale.ENGLISH doesn't match that"
            + " returned by NumberFormat.parse() using the same settings.");
        out.println("Expected: " + control);
        out.println("Received: " + result);
        return;
      }
    } catch (Exception e) {
      throw new ServletException(JSFTestUtil.FAIL + " Unexpected Exception"
          + "thrown when calling NumberConverter." + "getAsString().", e);
    }

    out.println(JSFTestUtil.PASS);
  }

  // NumberConverter.getAsString() with pattern specified
  public void numConverterGetAsStringPatternTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    String pattern = "0.###E0";
    Long inputValue = Long.valueOf(1234);
    NumberConverter converter = new NumberConverter();
    converter.setLocale(Locale.ENGLISH);
    converter.setPattern(pattern);

    DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
    DecimalFormat formatter = new DecimalFormat(pattern, symbols);
    formatter.setGroupingUsed(true);

    Object control;

    control = formatter.format(inputValue);

    if (control == null) {
      out.println(JSFTestUtil.FAIL + " Unable to obtain result from"
          + " DecimalFormat.format(String).");
      return;
    }

    try {
      Object result = converter.getAsString(getFacesContext(), new UIInput(),
          Long.valueOf(1234));
      if (!control.equals(result)) {
        out.println(JSFTestUtil.FAIL + " The value returned by NumberConverter"
            + ".getAsString() using a pattern of '" + pattern
            + " was not the same value as that returned by"
            + " DecimalFormat.");
        out.println("Expected: " + control);
        out.println("Received: " + result);
        return;
      }
    } catch (Exception e) {
      throw new ServletException(JSFTestUtil.FAIL + " Unexpected Exception"
          + "thrown when calling NumberConverter." + "getAsString().", e);
    }

    // NumberConverter.getAsString() must ignore the value of the type
    // property if pattern is set
    for (int i = 0; i < TYPES.length; i++) {
      converter.setType(TYPES[i]);
      try {
        Object result = converter.getAsString(getFacesContext(), new UIInput(),
            inputValue);
        if (!control.equals(result)) {
          out.println("Type seems to have impacted the return value"
              + "of NumberFormat.getAsString() when a pattern"
              + "has been specified.");
          out.println("Pattern: " + pattern);
          out.println("Type: " + TYPES[i] + " (should have been ignored)");
          out.println("Expected: " + control);
          out.println("Received: " + result);
          return;
        }
      } catch (Exception e) {
        throw new ServletException(JSFTestUtil.FAIL + " Unexpected Exception"
            + "thrown when calling NumberConverter." + "getAsString().", e);
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  // NumberConverter.getAsString() using specified types
  public void numConverterGetAsStringTypeTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    NumberConverter converter = new NumberConverter();
    Locale locale = new Locale("en", "US");
    converter.setLocale(locale);
    Class[] params = { Locale.class };
    Method[] numMethods;
    try {
      numMethods = new Method[] {
          NumberFormat.class.getDeclaredMethod("getNumberInstance", params),
          NumberFormat.class.getDeclaredMethod("getCurrencyInstance", params),
          NumberFormat.class.getDeclaredMethod("getPercentInstance", params) };
    } catch (NoSuchMethodException nsme) {
      throw new ServletException("Test initialization failed!", nsme);
    }

    Object[] inputs = { Double.valueOf(1234.56), Double.valueOf(45.6666666),
        Long.valueOf(3145192) };

    for (int i = 0; i < TYPES.length; i++) {
      converter.setType(TYPES[i]);
      NumberFormat formatter;
      try {
        formatter = (NumberFormat) numMethods[i].invoke(null,
            new Object[] { locale });
      } catch (Exception e) {
        throw new ServletException("Unexpected Exception! " + e.toString(), e);
      }

      if (formatter == null) {
        out.println(
            JSFTestUtil.FAIL + " Unable to obtain NumberFormat " + "instance.");
        return;
      }
      formatter.setGroupingUsed(true);
      Object control = formatter.format(inputs[i]);

      if (control == null) {
        out.println(JSFTestUtil.FAIL + " Unable to obtain result from"
            + " DecimalFormat.Format().");
        return;
      }

      // Now get the result from the NumberConverter
      try {
        Object result = converter.getAsString(getFacesContext(), new UIInput(),
            inputs[i]);
        if (!control.equals(result)) {
          out.println("Unexpected result returned from NumberCon"
              + "verter.getAsString with type specified as" + " '" + TYPES[i]
              + "' with an input value of" + "'" + inputs[i] + "'");
          out.println("Expected: " + control);
          out.println("Received: " + result);
          return;
        }
      } catch (Exception e) {
        throw new ServletException(JSFTestUtil.FAIL + " Unexpected Exception"
            + "thrown when calling NumberConverter." + "getAsString().", e);
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  // NumberConverter.getAsString() using setGroupingUsed();
  public void numConverterGetAsStringGroupingTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Class[] params = { Locale.class };
    Locale locale = new Locale("en", "US");
    Method[] numMethods;
    try {
      numMethods = new Method[] {
          NumberFormat.class.getDeclaredMethod("getNumberInstance", params),
          NumberFormat.class.getDeclaredMethod("getCurrencyInstance", params),
          NumberFormat.class.getDeclaredMethod("getPercentInstance", params) };
    } catch (NoSuchMethodException nsme) {
      throw new ServletException("Test initialization failed!", nsme);
    }

    Object[] inputs = { Double.valueOf(1234.56), Double.valueOf(45.6666666),
        Long.valueOf(3145192) };

    for (int j = 0; j < 2; j++) {
      NumberConverter converter = new NumberConverter();
      converter.setLocale(locale);
      if (j == 0) {
        converter.setGroupingUsed(true);
      } else {
        converter.setGroupingUsed(false);
      }

      for (int i = 0; i < TYPES.length; i++) {
        converter.setType(TYPES[i]);
        NumberFormat formatter;
        try {
          formatter = (NumberFormat) numMethods[i].invoke(null,
              new Object[] { locale });
        } catch (Exception e) {
          throw new ServletException("Unexpected Exception! " + e.toString(),
              e);
        }

        if (formatter == null) {
          out.println(JSFTestUtil.FAIL + " Unable to obtain NumberFormat "
              + "instance.");
          return;
        }
        if (j == 0) {
          formatter.setGroupingUsed(true);
        } else {
          formatter.setGroupingUsed(false);
        }
        Object control = formatter.format(inputs[i]);

        if (control == null) {
          out.println(JSFTestUtil.FAIL + " Unable to obtain result from"
              + " DecimalFormat.Format().");
          return;
        }

        // Now get the result from the NumberConverter
        try {
          Object result = converter.getAsString(getFacesContext(),
              new UIInput(), inputs[i]);
          if (!control.equals(result)) {
            out.println("Unexpected result returned from NumberCon"
                + "verter.getAsString with type specified as" + " '" + TYPES[i]
                + "' with an input value of" + "'" + inputs[i] + "'");
            out.println("And a grouping set to: " + converter.isGroupingUsed());
            out.println("Expected: " + control);
            out.println("Received: " + result);
            return;
          }
        } catch (Exception e) {
          throw new ServletException(
              JSFTestUtil.FAIL + " Unexpected Exception"
                  + "thrown when calling NumberConverter." + "getAsString().",
              e);
        }
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  // NumberConverter.getAsString() {max,min}IntegerDigits
  public void numConverterGetAsStringMinMaxIntegerTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Class[] params = { Locale.class };
    Locale locale = new Locale("en", "US");
    Method[] numMethods;
    try {
      numMethods = new Method[] {
          NumberFormat.class.getDeclaredMethod("getNumberInstance", params),
          NumberFormat.class.getDeclaredMethod("getCurrencyInstance", params),
          NumberFormat.class.getDeclaredMethod("getPercentInstance", params) };
    } catch (NoSuchMethodException nsme) {
      throw new ServletException("Test initialization failed!", nsme);
    }

    Object[] inputs = { Double.valueOf(1234.56), Double.valueOf(45.6666666),
        Long.valueOf(3145192) };

    for (int j = 0; j < 2; j++) {
      NumberConverter converter = new NumberConverter();
      converter.setLocale(locale);
      if (j == 0) {
        converter.setMaxIntegerDigits(1);
      } else {
        converter.setMinIntegerDigits(8);
      }

      for (int i = 0; i < TYPES.length; i++) {
        converter.setType(TYPES[i]);
        NumberFormat formatter;
        try {
          formatter = (NumberFormat) numMethods[i].invoke(null,
              new Object[] { locale });
        } catch (Exception e) {
          throw new ServletException("Unexpected Exception! " + e.toString(),
              e);
        }

        if (formatter == null) {
          out.println(JSFTestUtil.FAIL + " Unable to obtain NumberFormat "
              + "instance.");
          return;
        }
        formatter.setGroupingUsed(true);
        if (j == 0) {
          formatter.setMaximumIntegerDigits(1);
        } else {
          formatter.setMinimumIntegerDigits(8);
        }
        Object control = formatter.format(inputs[i]);

        if (control == null) {
          out.println(JSFTestUtil.FAIL + " Unable to obtain result from"
              + " NumberFormat.Format().");
          return;
        }

        // Now get the result from the NumberConverter
        try {
          Object result = converter.getAsString(getFacesContext(),
              new UIInput(), inputs[i]);
          if (!control.equals(result)) {
            out.println("Unexpected result returned from NumberCon"
                + "verter.getAsString with type specified as" + " '" + TYPES[i]
                + "' with an input value of" + "'" + inputs[i] + "'");
            if (j == 0) {
              out.println("And with Max int digits set to: "
                  + converter.getMaxIntegerDigits());
            } else {
              out.println("And with Min int digits set to: "
                  + converter.getMinIntegerDigits());
            }
            out.println("Expected: " + control);
            out.println("Received: " + result);
            return;
          }
        } catch (Exception e) {
          throw new ServletException(
              JSFTestUtil.FAIL + " Unexpected Exception"
                  + "thrown when calling NumberConverter." + "getAsString().",
              e);
        }
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  // NumberConverter.getAsString() {max,min}FractionalDigits
  public void numConverterGetAsStringMinMaxFractionTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Class[] params = { Locale.class };
    Locale locale = new Locale("en", "US");
    Method[] numMethods;
    try {
      numMethods = new Method[] {
          NumberFormat.class.getDeclaredMethod("getNumberInstance", params),
          NumberFormat.class.getDeclaredMethod("getCurrencyInstance", params),
          NumberFormat.class.getDeclaredMethod("getPercentInstance", params) };
    } catch (NoSuchMethodException nsme) {
      throw new ServletException("Test initialization failed!", nsme);
    }

    Object[] inputs = { Double.valueOf(1234.56), Double.valueOf(45.6666666),
        Long.valueOf(3145192) };

    for (int j = 0; j < 2; j++) {
      NumberConverter converter = new NumberConverter();
      converter.setLocale(locale);
      if (j == 0) {
        converter.setMaxFractionDigits(3);
      } else {
        converter.setMinFractionDigits(1);
      }

      for (int i = 0; i < TYPES.length; i++) {
        converter.setType(TYPES[i]);
        NumberFormat formatter;
        try {
          formatter = (NumberFormat) numMethods[i].invoke(null,
              new Object[] { locale });
        } catch (Exception e) {
          throw new ServletException("Unexpected Exception! " + e.toString(),
              e);
        }

        if (formatter == null) {
          out.println(JSFTestUtil.FAIL + " Unable to obtain NumberFormat "
              + "instance.");
          return;
        }
        formatter.setGroupingUsed(true);
        if (j == 0) {
          formatter.setMaximumFractionDigits(3);
        } else {
          formatter.setMinimumFractionDigits(1);
        }
        Object control = formatter.format(inputs[i]);

        if (control == null) {
          out.println(JSFTestUtil.FAIL + " Unable to obtain result from"
              + " NumberFormat.Format().");
          return;
        }

        // Now get the result from the NumberConverter
        try {
          Object result = converter.getAsString(getFacesContext(),
              new UIInput(), inputs[i]);
          if (!control.equals(result)) {
            out.println("Unexpected result returned from NumberCon"
                + "verter.getAsString with type specified as" + " '" + TYPES[i]
                + "' with an input value of" + "'" + inputs[i] + "'");
            if (j == 0) {
              out.println("And with Max fraction digits set to: "
                  + converter.getMaxFractionDigits());
            } else {
              out.println("And with Min fraction digits set to: "
                  + converter.getMinFractionDigits());
            }
            out.println("Expected: " + control);
            out.println("Received: " + result);
            return;
          }
        } catch (Exception e) {
          throw new ServletException(
              JSFTestUtil.FAIL + " Unexpected Exception"
                  + "thrown when calling NumberConverter." + "getAsString().",
              e);
        }
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  // NumberConverter.getAsString() currencySymbol
  public void numConverterGetAsStringCurrencySymbolTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    NumberConverter converter = new NumberConverter();
    Locale locale = new Locale("en", "US");
    converter.setLocale(locale);
    converter.setCurrencySymbol("@");
    Class[] params = { Locale.class };
    Method[] numMethods;

    try {
      numMethods = new Method[] {
          NumberFormat.class.getDeclaredMethod("getNumberInstance", params),
          NumberFormat.class.getDeclaredMethod("getCurrencyInstance", params),
          NumberFormat.class.getDeclaredMethod("getPercentInstance", params) };
    } catch (NoSuchMethodException nsme) {
      throw new ServletException("Test initialization failed!", nsme);
    }

    Object[] inputs = { Double.valueOf(1234.56), Double.valueOf(45.6666666),
        Long.valueOf(3145192) };

    for (int i = 0; i < TYPES.length; i++) {
      // Currency symbol should not impact the result
      // if type is number or percent
      converter.setType(TYPES[i]);
      NumberFormat formatter;
      try {
        formatter = (NumberFormat) numMethods[i].invoke(null,
            new Object[] { locale });
      } catch (Exception e) {
        throw new ServletException("Unexpected Exception! " + e.toString(), e);
      }

      if (formatter == null) {
        out.println(
            JSFTestUtil.FAIL + " Unable to obtain NumberFormat " + "instance.");
        return;
      }
      formatter.setGroupingUsed(true);
      if (TYPES[i].equals("currency")) {
        DecimalFormat df = (DecimalFormat) formatter;
        DecimalFormatSymbols symbols = df.getDecimalFormatSymbols();
        symbols.setCurrencySymbol("@");
        df.setDecimalFormatSymbols(symbols);
      }
      Object control = formatter.format(inputs[i]);

      if (control == null) {
        out.println(JSFTestUtil.FAIL + " Unable to obtain result from"
            + " NumberFormat.Format().");
        return;
      }

      // Now get the result from the NumberConverter
      try {
        Object result = converter.getAsString(getFacesContext(), new UIInput(),
            inputs[i]);
        if (!control.equals(result)) {
          out.println("Unexpected result returned from NumberCon"
              + "verter.getAsString with type specified as" + " '" + TYPES[i]
              + "' with an input value of" + "'" + inputs[i] + "'"
              + " and currencySymbol set to '@'");
          out.println("Expected: " + control);
          out.println("Received: " + result);
          return;
        }
      } catch (Exception e) {
        throw new ServletException(JSFTestUtil.FAIL + " Unexpected Exception"
            + "thrown when calling NumberConverter." + "getAsString().", e);
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  // NumberConverter.getAsString() currencyCode
  public void numConverterGetAsStringCurrencyCodeTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Locale locale = new Locale("en", "US");
    String code = "USD";

    Class[] params = { Locale.class };
    Method[] numMethods;

    try {
      numMethods = new Method[] {
          NumberFormat.class.getDeclaredMethod("getNumberInstance", params),
          NumberFormat.class.getDeclaredMethod("getCurrencyInstance", params),
          NumberFormat.class.getDeclaredMethod("getPercentInstance", params) };
    } catch (NoSuchMethodException nsme) {
      throw new ServletException("Test initialization failed!", nsme);
    }

    Object[] inputs = { Double.valueOf(1234.56), Double.valueOf(45.6666666),
        Long.valueOf(3145192) };

    for (int i = 0; i < TYPES.length; i++) {
      NumberConverter converter = new NumberConverter();
      converter.setLocale(locale);
      converter.setCurrencyCode(code);
      converter.setType(TYPES[i]);
      NumberFormat formatter;
      try {
        formatter = (NumberFormat) numMethods[i].invoke(null,
            new Object[] { locale });
      } catch (Exception e) {
        throw new ServletException("Unexpected Exception! " + e.toString(), e);
      }

      if (formatter == null) {
        out.println(
            JSFTestUtil.FAIL + " Unable to obtain NumberFormat " + "instance.");
        return;
      }

      formatter.setGroupingUsed(true);

      if (TYPES[i].equals("currency")) {
        if (currencyClass != null) {
          try {
            Object[] methodArgs = new Object[1];

            /*
             * java.util.Currency.getInstance()
             */
            Method m = currencyClass.getMethod("getInstance",
                new Class[] { String.class });
            methodArgs[0] = code;
            Object currency = m.invoke(null, methodArgs);

            /*
             * java.text.NumberFormat.setCurrency()
             */
            Class[] paramTypes = new Class[1];
            paramTypes[0] = currencyClass;
            Class numberFormatClass = Class.forName("java.text.NumberFormat");
            m = numberFormatClass.getMethod("setCurrency", paramTypes);
            methodArgs[0] = currency;
            m.invoke(formatter, methodArgs);
          } catch (Exception e) {
            throw new ServletException("Unexpected Exception: " + e.toString(),
                e);
          }
        } else {
          DecimalFormat df = (DecimalFormat) formatter;
          DecimalFormatSymbols symbols = df.getDecimalFormatSymbols();
          symbols.setCurrencySymbol(code);
          df.setDecimalFormatSymbols(symbols);
        }
      } else {

      }

      Object control = formatter.format(inputs[i]);

      if (control == null) {
        out.println(JSFTestUtil.FAIL + " Unable to obtain result from"
            + " NumberFormat.Format().");
        return;
      }

      // Now get the result from the NumberConverter
      try {
        Object result = converter.getAsString(getFacesContext(), new UIInput(),
            inputs[i]);
        if (!control.equals(result)) {
          out.println("Unexpected result returned from NumberCon"
              + "verter.getAsString with type specified as" + " '" + TYPES[i]
              + "' with an input value of" + "'" + inputs[i] + "'"
              + " and currencyCode set to 'USD'");
          out.println("Expected: " + control);
          out.println("Received: " + result);
          return;
        }
      } catch (Exception e) {
        throw new ServletException(JSFTestUtil.FAIL + " Unexpected Exception"
            + "thrown when calling NumberConverter." + "getAsString().", e);
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  // NumberConverter.getAsString() currencyCode and currencySymbol
  // both specified
  public void numConverterGetAsStringCurrencyCodeSymbolTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Locale locale = new Locale("en", "us");
    NumberConverter converter = new NumberConverter();
    converter.setLocale(locale);
    converter.setType("currency");
    String symbol = "@";
    String code = "USD";
    converter.setCurrencySymbol(symbol);
    converter.setCurrencyCode(code);

    NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);

    formatter.setGroupingUsed(true);

    if (currencyClass != null) {
      try {
        Object[] methodArgs = new Object[1];

        /*
         * java.util.Currency.getInstance()
         */
        Method m = currencyClass.getMethod("getInstance",
            new Class[] { String.class });
        methodArgs[0] = code;
        Object currency = m.invoke(null, methodArgs);

        /*
         * java.text.NumberFormat.setCurrency()
         */
        Class[] paramTypes = new Class[1];
        paramTypes[0] = currencyClass;
        Class numberFormatClass = Class.forName("java.text.NumberFormat");
        m = numberFormatClass.getMethod("setCurrency", paramTypes);
        methodArgs[0] = currency;
        m.invoke(formatter, methodArgs);
      } catch (Exception e) {
        throw new ServletException("Unexpected Exception: " + e.toString(), e);
      }
    } else {
      DecimalFormat df = (DecimalFormat) formatter;
      DecimalFormatSymbols symbols = df.getDecimalFormatSymbols();
      symbols.setCurrencySymbol(symbol);
      df.setDecimalFormatSymbols(symbols);
    }

    Object control = formatter.format(Double.valueOf(1234.56));

    if (control == null) {
      out.println(JSFTestUtil.FAIL + " Unable to obtain result from"
          + " NumberFormat.Format().");
      return;
    }

    // Now get the result from the NumberConverter
    try {
      Object result = converter.getAsString(getFacesContext(), new UIInput(),
          Double.valueOf(1234.56));
      if (!control.equals(result)) {
        out.println("Unexpected result returned from NumberCon"
            + "verter.getAsString with type specified as" + " 'currency'"
            + " with an input value of" + "'1234.34'"
            + " and currencyCode set to 'USD' and currency"
            + "Symbol set to '@'");
        out.println("Expected: " + control);
        out.println("Received: " + result);
        return;
      }
    } catch (Exception e) {
      throw new ServletException(JSFTestUtil.FAIL + " Unexpected Exception"
          + "thrown when calling NumberConverter." + "getAsString().", e);
    }

    out.println(JSFTestUtil.PASS);
  }

  // NumberConverter.getAsObject() throws NPE if either context or
  // component are null
  public void numConverterGetAsObjectNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    NumberConverter converter = new NumberConverter();

    try {
      converter.getAsObject(null, new UIInput(), "1.23");
      out.println(JSFTestUtil.FAIL + " No Exception thrown when a null"
          + " FacesContext is passed to NumberConveter.getAs" + "Object().");
      return;
    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown when a null"
            + " FacesContext was passed to NumberConverter.get"
            + "asObject(), but it wasn't an instance of"
            + " NullPointerException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    try {
      converter.getAsObject(getFacesContext(), null, "1.23");
      out.println(JSFTestUtil.FAIL + " No Exception thrown when a null"
          + " UIComponent is passed to NumberConveter.getAs" + "Object().");
      return;
    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown when a null"
            + " UIComponent was passed to NumberConverter.get"
            + "asObject(), but it wasn't an instance of"
            + " NullPointerException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  // NumberConverter.getAsString() throws NPE if either context or
  // component are null
  public void numConverterGetAsStringNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    NumberConverter converter = new NumberConverter();

    try {
      converter.getAsString(null, new UIInput(), "1.23");
      out.println(JSFTestUtil.FAIL + " No Exception thrown when a null"
          + " FacesContext is passed to NumberConveter.getAs" + "Object().");
      return;
    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown when a null"
            + " FacesContext was passed to NumberConverter.get"
            + "asObject(), but it wasn't an instance of"
            + " NullPointerException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    try {
      converter.getAsString(getFacesContext(), null, "1.23");
      out.println(JSFTestUtil.FAIL + " No Exception thrown when a null"
          + " UIComponent is passed to NumberConveter.getAs" + "Object().");
      return;
    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown when a null"
            + " UIComponent was passed to NumberConverter.get"
            + "asObject(), but it wasn't an instance of"
            + " NullPointerException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    out.println(JSFTestUtil.PASS);
  }
}
