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

package com.sun.ts.tests.jsf.api.javax_faces.convert.datetimeconverter;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.convert.DateTimeConverter;
import javax.faces.render.RenderKitFactory;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

public final class TestServlet extends HttpTCKServlet {

  // Valid values for {set,get}{Date,Time}Style()
  private static final String STYLE_DEFAULT = "default";

  private static final String STYLE_SHORT = "short";

  private static final String STYLE_MEDIUM = "medium";

  private static final String STYLE_LONG = "long";

  private static final String STYLE_FULL = "full";

  // Valid values for {set,get}Type()
  private static final String TYPE_DATE = "date";

  private static final String TYPE_TIME = "time";

  private static final String TYPE_BOTH = "both";

  private static final String[] FORMAT_STYLES = { STYLE_DEFAULT, STYLE_SHORT,
      STYLE_MEDIUM, STYLE_LONG, STYLE_FULL };

  private static final int[] DATE_FORMAT_STYLES = { DateFormat.DEFAULT,
      DateFormat.SHORT, DateFormat.MEDIUM, DateFormat.LONG, DateFormat.FULL };

  private static final TimeZone AMERICA_NEW_YORK = TimeZone
      .getTimeZone("America/New_York");

  private static final Date CURRENT_DATE_TIME = new Date();

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }

  // ------------------------------------------- Test Methods ----

  // DateTimeConverter.setDateStyle(String)
  // DateTimeConverter.getDateStyle()
  public void dateTimeConverterGetSetDateStyleTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    DateTimeConverter converter = new DateTimeConverter();

    if (!STYLE_DEFAULT.equals(converter.getDateStyle())) {
      out.println(
          JSFTestUtil.FAIL + " Expected DateTimeConverter.getDateStyle()"
              + " to return 'default' if no style was specified.");
      out.println("Style received: " + converter.getDateStyle());
      return;
    }

    converter.setDateStyle(STYLE_FULL);
    String result = converter.getDateStyle();
    if (!STYLE_FULL.equals(result)) {
      out.println(JSFTestUtil.FAIL
          + " DateTimeConverter.getTimeStyle() didn't returned the same"
          + " value previously set.  Expected 'full', but received '" + result
          + "'");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // DateTimeConverter.setLocale(Locale)
  // DateTimeConverter.getLocale()
  public void dateTimeConverterSetGetLocaleTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIViewRoot root = new UIViewRoot();
    root.setLocale(Locale.US);
    getFacesContext().setViewRoot(root);
    DateTimeConverter converter = new DateTimeConverter();

    if (!new Locale("en", "US").equals(converter.getLocale())) {
      out.println(
          JSFTestUtil.FAIL + " Exepcted locale specified in FacesContext"
              + " to be returned if locale was not explicitly set on"
              + " DateTimeConverter instanced.");
      out.println("Locale received: " + converter.getLocale());
      return;
    }
    converter.setLocale(Locale.CANADA);
    Locale result = converter.getLocale();
    if (!Locale.CANADA.equals(result)) {
      out.println(JSFTestUtil.FAIL
          + " The Locale returned by DateTimeConverter was not the same "
          + " as was previously set.  Expected '" + Locale.CANADA
          + "'.  Received '" + result + "'");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // DateTimeConverter.setTimeZone(Timezone)
  // DateTimeConverter.getTimeZone()
  public void dateTimeConverterSetGetTimeZoneTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    DateTimeConverter converter = new DateTimeConverter();
    TimeZone defaultTimeZone = TimeZone.getTimeZone("GMT");

    if (!defaultTimeZone.equals(converter.getTimeZone())) {
      out.println(JSFTestUtil.FAIL + " Expected DateTimeConverter.getTimeZone()"
          + " to return the default timezone of GMT.");
      out.println("Received: " + converter.getTimeZone().getDisplayName());
      return;
    }

    TimeZone control = TimeZone.getTimeZone("PST");
    converter.setTimeZone(control);
    TimeZone tz = converter.getTimeZone();
    if (!control.equals(tz)) {
      out.println(JSFTestUtil.FAIL
          + " The TimeZone returned by DateTimeConverter was not the same "
          + " as was previously set.  Expected '" + control.getDisplayName()
          + "'.  Received '" + tz.getDisplayName() + "'");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // DateTimeConverter.setPattern(String)
  // DateTimeConverter.getPattern()
  public void dateTimeConverterSetGetPatternTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    DateTimeConverter converter = new DateTimeConverter();
    String pattern = "yyyy.MM.dd G 'at' HH:mm:ss Z";
    converter.setPattern(pattern);
    String result = converter.getPattern();
    if (!result.equals(pattern)) {
      out.println(JSFTestUtil.FAIL
          + " The Pattern returned by DateTimeConverter was not the same "
          + " as was previously set.  Expected '" + pattern + "'.  Received '"
          + result + "'");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // DateTimeConverter.setTimeStyle(String)
  // DateTimeConverter.getTimeStyle()
  public void dateTimeConverterSetGetTimeStyleTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    DateTimeConverter converter = new DateTimeConverter();

    if (!STYLE_DEFAULT.equals(converter.getTimeStyle())) {
      out.println(
          JSFTestUtil.FAIL + " Expected DateTimeConverter.getDateStyle()"
              + " to return 'default' if no style was specified.");
      out.println("Style received: " + converter.getTimeStyle());
      return;
    }

    converter.setTimeStyle(STYLE_SHORT);
    String result = converter.getTimeStyle();
    if (!result.equals(STYLE_SHORT)) {
      out.println(JSFTestUtil.FAIL
          + " The time style returned by DateTimeConverter was not the same "
          + " as was previously set.  Expected '" + STYLE_SHORT
          + "'.  Received '" + result + "'");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // DateTimeConverter.setType(String)
  // DateTimeConverter.getType(String)
  public void dateTimeConverterSetGetTypeTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    DateTimeConverter converter = new DateTimeConverter();

    if (!TYPE_DATE.equals(converter.getType())) {
      out.println(JSFTestUtil.FAIL + " Expected DateTimeConverter.getType()"
          + " to return 'date' when type was specified.");
      out.println("Received: " + converter.getType());
      return;
    }
    converter.setType(TYPE_BOTH);
    String result = converter.getType();
    if (!TYPE_BOTH.equals(result)) {
      out.println(JSFTestUtil.FAIL
          + " The type returned by DateTimeConverter was not the same "
          + " as was previously set.  Expected '" + TYPE_BOTH + "'.  Received '"
          + result + "'");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // DateTimeConverter.isTransient()
  // DateTimeConverter.setTransient()
  public void dateTimeConverterIsSetTransientTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    DateTimeConverter converter = new DateTimeConverter();

    // by default DateTimeConverter is not transient
    if (converter.isTransient()) {
      out.println(JSFTestUtil.FAIL + " Expected DateTimeConverter.isT"
          + "ransient() to return false on a newly created instance.");
      return;
    }

    converter.setTransient(false);
    if (converter.isTransient()) {
      out.println(JSFTestUtil.FAIL + " Expected DateTimeConverter.isTransient()"
          + " to return false when DateTimeConverter.setTransient(false) was"
          + " called.");
      return;
    }

    converter.setTransient(true);
    if (!converter.isTransient()) {
      out.println(JSFTestUtil.FAIL + " Expected DateTimeConverter.isTransient()"
          + " to return true when DateTimeConverter.setTransient(true) was"
          + " called.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // Test getAsObject() with a specified date style
  public void dateTimeConverterGetAsObjectDateStyleTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    Date current = new Date();
    DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT,
        Locale.US);
    dateFormat.setTimeZone(AMERICA_NEW_YORK);
    DateTimeConverter converter = new DateTimeConverter();
    converter.setTimeZone(AMERICA_NEW_YORK);
    String control = dateFormat.format(current);
    Object controlObj;
    try {
      controlObj = dateFormat.parse(control);
    } catch (ParseException e) {
      throw new ServletException(e);
    }
    UIComponent component = getApplication()
        .createComponent(UIInput.COMPONENT_TYPE);
    FacesContext context = getFacesContext();
    UIViewRoot root = new UIViewRoot();
    root.setLocale(Locale.US);
    context.setViewRoot(root);

    // in test order

    Object result;

    // if date style is not specified, 'default' will be used.
    try {
      result = converter.getAsObject(context, component, control);
    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + " Unexpected Exception when calling"
          + " DateTimeConverter.getAsObject() with a input value of" + " '"
          + control + "'");
      out.println("Exception: " + e.toString());
      return;
    }

    if (!controlObj.equals(result)) {
      out.println(JSFTestUtil.FAIL + " The result returned by DateTimeConverter"
          + ".getAsObject() didn't yield the expected result.");
      out.println("Expected: " + current.toString());
      out.println("Received: " + result.toString());
      return;
    }

    // loop through the known styles and ensure the results are as expected
    boolean failed = false;
    for (int i = 0; i < DATE_FORMAT_STYLES.length; i++) {
      dateFormat = DateFormat.getDateInstance(DATE_FORMAT_STYLES[i], Locale.US);
      dateFormat.setTimeZone(AMERICA_NEW_YORK);
      control = dateFormat.format(current);
      try {
        controlObj = dateFormat.parse(control);
      } catch (Exception e) {
        throw new ServletException(e);
      }
      converter.setDateStyle(FORMAT_STYLES[i]);
      try {
        result = converter.getAsObject(context, component, control);
      } catch (Exception e) {
        out.println(JSFTestUtil.FAIL + " Unexpected Exception when calling"
            + " DateTimeConverter.getAsObject() with a input value of" + " '"
            + control + "' using a style of '" + FORMAT_STYLES[i] + "'");
        out.println("Exception: " + e.toString());
        return;
      }

      if (!controlObj.equals(result)) {
        out.println(
            JSFTestUtil.FAIL + " The result returned by DateTimeConverter"
                + ".getAsObject didn't yield the expected result using "
                + "formatting style '" + FORMAT_STYLES[i] + "'");
        out.println("Expected: " + current.toString());
        out.println("Received: " + result.toString());
        failed = true;
      }
    }
    if (failed)
      return;
    else
      out.println(JSFTestUtil.PASS);
  }

  // Test getAsString() with a specified date style
  public void dateTimeConverterGetAsStringDateStyleTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT,
        Locale.US);
    dateFormat.setTimeZone(AMERICA_NEW_YORK);
    DateTimeConverter converter = new DateTimeConverter();
    String control = dateFormat.format(CURRENT_DATE_TIME);
    Object controlObj;
    try {
      controlObj = dateFormat.parse(control);
    } catch (ParseException e) {
      throw new ServletException(e);
    }
    UIComponent component = getApplication()
        .createComponent(UIInput.COMPONENT_TYPE);
    FacesContext context = getFacesContext();
    UIViewRoot root = new UIViewRoot();
    root.setLocale(Locale.US);
    context.setViewRoot(root);

    // in test order

    String result;

    // if date style is not specified, 'default' will be used.
    try {
      result = converter.getAsString(context, component, controlObj);
    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + " Unexpected Exception when calling"
          + " DateTimeConverter.getAsString() with a input value of" + " '"
          + controlObj.toString() + "'");
      out.println("Exception: " + e.toString());
      return;
    }

    if (!control.equals(result)) {
      out.println(JSFTestUtil.FAIL + " The result returned by DateTimeConverter"
          + ".getAsString() didn't yield the expected result.");
      out.println("Expected: " + control);
      out.println("Received: " + result);
      return;
    }

    // loop through the known styles and ensure the results are as expected
    boolean failed = false;
    for (int i = 0; i < DATE_FORMAT_STYLES.length; i++) {
      dateFormat = DateFormat.getDateInstance(DATE_FORMAT_STYLES[i], Locale.US);
      dateFormat.setTimeZone(AMERICA_NEW_YORK);
      control = dateFormat.format(CURRENT_DATE_TIME);
      try {
        controlObj = dateFormat.parse(control);
      } catch (Exception e) {
        throw new ServletException(e);
      }
      converter.setDateStyle(FORMAT_STYLES[i]);
      converter.setType(TYPE_DATE);
      try {
        result = converter.getAsString(context, component, controlObj);
      } catch (Exception e) {
        out.println(JSFTestUtil.FAIL + " Unexpected Exception when calling"
            + " DateTimeConverter.getAsString() with a input value of" + " '"
            + controlObj.toString() + "' using a style of '" + FORMAT_STYLES[i]
            + "'");
        out.println("Exception: " + e.toString());
        return;
      }

      if (!control.equals(result)) {
        out.println(
            JSFTestUtil.FAIL + " The result returned by DateTimeConverter"
                + ".getAsString() didn't yield the expected result using "
                + "formatting style '" + FORMAT_STYLES[i] + "'");
        out.println("Expected: " + control);
        out.println("Received: " + result);
        failed = true;
      }
    }
    if (failed)
      return;
    else
      out.println(JSFTestUtil.PASS);
  }

  // Test getAsObject() with a specified time style
  public void dateTimeConverterGetAsObjectTimeStyleTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    Date current = new Date();
    DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.DEFAULT,
        Locale.US);
    dateFormat.setTimeZone(AMERICA_NEW_YORK);
    DateTimeConverter converter = new DateTimeConverter();
    converter.setType(TYPE_TIME);
    converter.setTimeZone(AMERICA_NEW_YORK);
    String control = dateFormat.format(current);
    Object controlObj;

    try {
      controlObj = dateFormat.parse(control);
    } catch (ParseException e) {
      throw new ServletException(e);
    }

    UIComponent component = getApplication()
        .createComponent(UIInput.COMPONENT_TYPE);
    FacesContext context = getFacesContext();
    UIViewRoot root = new UIViewRoot();
    root.setLocale(Locale.US);
    root.setRenderKitId(RenderKitFactory.HTML_BASIC_RENDER_KIT);
    root.getChildren().add(component);
    context.setViewRoot(root);

    // in test order
    Object result;

    // if time style is not specified, 'default' will be used.
    try {
      result = converter.getAsObject(context, component, control);
    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + " Unexpected Exception when calling"
          + " DateTimeConverter.getAsObject() with a input value of" + " '"
          + control + "'");
      out.println("Exception: " + e.toString());
      return;
    }

    if (!controlObj.equals(result)) {
      out.println(JSFTestUtil.FAIL + " The result returned by DateTimeConverter"
          + ".getAsObject() didn't yield the expected result.");
      out.println("Expected: " + controlObj.toString());
      out.println("Received: " + result.toString());
      return;
    }

    // loop through the known styles and ensure the results are as expected
    boolean failed = false;
    for (int i = 0; i < DATE_FORMAT_STYLES.length; i++) {
      if (System.getProperty("java.version").startsWith("1.5.")
          && ("long".equals(FORMAT_STYLES[i])
              || "full".equals(FORMAT_STYLES[i]))) {
        continue; // ADDED DUE TO JDK 1.5.0 BUG 6247963 (REGRESSION)
      }
      dateFormat = DateFormat.getTimeInstance(DATE_FORMAT_STYLES[i], Locale.US);
      dateFormat.setTimeZone(AMERICA_NEW_YORK);
      control = dateFormat.format(current);
      try {
        controlObj = dateFormat.parse(control);
      } catch (Exception e) {
        throw new ServletException(e);
      }
      converter.setType(TYPE_TIME);
      converter.setTimeStyle(FORMAT_STYLES[i]);
      converter.setTimeZone(AMERICA_NEW_YORK);
      try {
        result = converter.getAsObject(context, component, control);
      } catch (Exception e) {
        out.println(JSFTestUtil.FAIL + " Unexpected Exception when calling"
            + " DateTimeConverter.getAsObject() with a input value of" + " '"
            + control + "' using a style of '" + FORMAT_STYLES[i] + "'");
        return;
      }

      if (!controlObj.equals(result)) {
        out.println(
            JSFTestUtil.FAIL + " The result returned by DateTimeConverter"
                + ".getAsObject didn't yield the expected result using "
                + "formatting style '" + FORMAT_STYLES[i] + "'");
        out.println("Expected: " + controlObj.toString());
        out.println("Received: " + result.toString());
        failed = true;
      }
    }
    if (!failed) {
      out.println(JSFTestUtil.PASS);
    }
  }

  // Test getAsString() with a specified time style
  public void dateTimeConverterGetAsStringTimeStyleTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.DEFAULT,
        Locale.US);
    dateFormat.setTimeZone(AMERICA_NEW_YORK);
    DateTimeConverter converter = new DateTimeConverter();
    converter.setType(TYPE_TIME);
    converter.setTimeZone(AMERICA_NEW_YORK);
    String control = dateFormat.format(CURRENT_DATE_TIME);
    Object controlObj;
    try {
      controlObj = dateFormat.parse(control);
    } catch (ParseException e) {
      throw new ServletException(e);
    }
    UIComponent component = getApplication()
        .createComponent(UIInput.COMPONENT_TYPE);
    FacesContext context = getFacesContext();
    UIViewRoot root = new UIViewRoot();
    root.setLocale(Locale.US);
    context.setViewRoot(root);

    // in test order
    String result;

    // if time style is not specified, 'default' will be used.
    try {
      result = converter.getAsString(context, component, controlObj);
    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + " Unexpected Exception when calling"
          + " DateTimeConverter.getAsString() with a input value of" + " '"
          + controlObj.toString() + "'");
      out.println("Exception: " + e.toString());
      return;
    }

    if (!control.equals(result)) {
      out.println(JSFTestUtil.FAIL + " The result returned by DateTimeConverter"
          + ".getAsString() didn't yield the expected result.");
      out.println("Expected: " + control);
      out.println("Received: " + result);
      return;
    }

    // loop through the known styles and ensure the results are as expected
    boolean failed = false;
    for (int i = 0; i < DATE_FORMAT_STYLES.length; i++) {
      dateFormat = DateFormat.getTimeInstance(DATE_FORMAT_STYLES[i], Locale.US);
      dateFormat.setTimeZone(AMERICA_NEW_YORK);
      control = dateFormat.format(CURRENT_DATE_TIME);
      try {
        controlObj = dateFormat.parse(control);
      } catch (Exception e) {
        throw new ServletException(e);
      }
      converter.setTimeStyle(FORMAT_STYLES[i]);
      converter.setType(TYPE_TIME);
      converter.setTimeZone(AMERICA_NEW_YORK);
      converter.setLocale(Locale.US);
      try {
        result = converter.getAsString(context, component, CURRENT_DATE_TIME);
      } catch (Exception e) {
        out.println(JSFTestUtil.FAIL + " Unexpected Exception when calling"
            + " DateTimeConverter.getAsString() with a input value of" + " '"
            + controlObj.toString() + "' using a style of '" + FORMAT_STYLES[i]
            + "'");
        out.println("Exception: " + e.toString());
        return;
      }

      if (!control.equals(result)) {
        out.println(
            JSFTestUtil.FAIL + " The result returned by DateTimeConverter"
                + ".getAsString() didn't yield the expected result using "
                + "formatting style '" + FORMAT_STYLES[i] + "'");
        out.println("Expected: " + control);
        out.println("Received: " + result);
        failed = true;
      }
    }
    if (failed) {
      return;
    } else {
      out.println(JSFTestUtil.PASS);
    }
  }

  // Test getAsObject() with a style set to both
  public void dateTimeConverterGetAsObjectBothStyleTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    Date current = new Date();
    DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT,
        DateFormat.DEFAULT, Locale.US);
    dateFormat.setTimeZone(AMERICA_NEW_YORK);
    DateTimeConverter converter = new DateTimeConverter();
    converter.setType(TYPE_BOTH);
    converter.setTimeZone(AMERICA_NEW_YORK);
    String control = dateFormat.format(current);
    Object controlObj;
    try {
      controlObj = dateFormat.parse(control);
    } catch (ParseException e) {
      throw new ServletException(e);
    }
    UIComponent component = getApplication()
        .createComponent(UIInput.COMPONENT_TYPE);
    FacesContext context = getFacesContext();
    UIViewRoot root = new UIViewRoot();
    root.setLocale(Locale.US);
    context.setViewRoot(root);

    // in test order
    Object result;

    // if time style is not specified, 'default' will be used.
    try {
      result = converter.getAsObject(context, component, control);
    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + " Unexpected Exception when calling"
          + " DateTimeConverter.getAsObject() with a input value of" + " '"
          + control + "'");
      out.println("Exception: " + e.toString());
      return;
    }

    if (!controlObj.equals(result)) {
      out.println(JSFTestUtil.FAIL + " The result returned by DateTimeConverter"
          + ".getAsObject() didn't yield the expected result.");
      out.println("Expected: " + controlObj.toString());
      out.println("Received: " + result.toString());
      return;
    }

    // loop through the known styles and ensure the results are as expected
    boolean failed = false;
    for (int i = 0, j = 4; i < DATE_FORMAT_STYLES.length; i++, j--) {
      dateFormat = DateFormat.getDateTimeInstance(DATE_FORMAT_STYLES[i],
          DATE_FORMAT_STYLES[j], Locale.US);
      dateFormat.setTimeZone(AMERICA_NEW_YORK);
      control = dateFormat.format(current);
      try {
        controlObj = dateFormat.parse(control);
      } catch (Exception e) {
        throw new ServletException(e);
      }
      converter.setTimeStyle(FORMAT_STYLES[j]);
      converter.setType(TYPE_BOTH);
      converter.setDateStyle(FORMAT_STYLES[i]);
      converter.setTimeZone(AMERICA_NEW_YORK);
      try {
        result = converter.getAsObject(context, component, control);
      } catch (Exception e) {
        out.println(JSFTestUtil.FAIL + " Unexpected Exception when calling"
            + " DateTimeConverter.getAsObject() with a input value of" + " '"
            + control + "' using a date style of '" + FORMAT_STYLES[i]
            + "' and a time style of '" + FORMAT_STYLES[j] + "'");
        out.println("Exception: " + e.toString());
        return;
      }

      if (!controlObj.equals(result)) {
        out.println(
            JSFTestUtil.FAIL + " The result returned by DateTimeConverter"
                + ".getAsObject didn't yield the expected result using "
                + "date style '" + FORMAT_STYLES[i] + "' and time style" + " '"
                + FORMAT_STYLES[j] + "'");
        out.println("Expected: " + controlObj.toString());
        out.println("Received: " + result.toString());
        failed = true;
      }
    }
    if (failed) {
      return;
    } else {
      out.println(JSFTestUtil.PASS);
    }
  }

  // Test getAsString() with a specified style set for both
  public void dateTimeConverterGetAsStringBothStyleTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT,
        DateFormat.DEFAULT, Locale.US);
    dateFormat.setTimeZone(AMERICA_NEW_YORK);
    DateTimeConverter converter = new DateTimeConverter();
    converter.setType(TYPE_BOTH);
    converter.setTimeZone(AMERICA_NEW_YORK);
    String control = dateFormat.format(CURRENT_DATE_TIME);
    Object controlObj;

    try {
      controlObj = dateFormat.parse(control);
    } catch (ParseException e) {
      throw new ServletException(e);
    }

    UIComponent component = getApplication()
        .createComponent(UIInput.COMPONENT_TYPE);
    FacesContext context = getFacesContext();
    UIViewRoot root = new UIViewRoot();
    root.setLocale(Locale.US);
    context.setViewRoot(root);

    // in test order
    String result;

    // if time style is not specified, 'default' will be used.
    try {
      result = converter.getAsString(context, component, controlObj);
    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + " Unexpected Exception when calling"
          + " DateTimeConverter.getAsString() with a input value of" + " '"
          + controlObj.toString() + "'");
      out.println("Exception: " + e.toString());
      return;
    }

    if (!control.equals(result)) {
      out.println(JSFTestUtil.FAIL + " The result returned by DateTimeConverter"
          + ".getAsString() didn't yield the expected result.");
      out.println("Expected: " + control);
      out.println("Received: " + result);
      return;
    }

    // loop through the known styles and ensure the results are as expected
    boolean failed = false;
    for (int i = 0, j = 4; i < DATE_FORMAT_STYLES.length; i++, j--) {
      dateFormat = DateFormat.getDateTimeInstance(DATE_FORMAT_STYLES[i],
          DATE_FORMAT_STYLES[j], Locale.US);
      dateFormat.setTimeZone(AMERICA_NEW_YORK);
      control = dateFormat.format(CURRENT_DATE_TIME);
      try {
        controlObj = dateFormat.parse(control);
      } catch (ParseException e) {
        throw new ServletException(e);
      }
      converter.setTimeStyle(FORMAT_STYLES[j]);
      converter.setDateStyle(FORMAT_STYLES[i]);
      converter.setType(TYPE_BOTH);
      converter.setTimeZone(AMERICA_NEW_YORK);
      converter.setLocale(Locale.US);
      try {
        result = converter.getAsString(context, component, CURRENT_DATE_TIME);
      } catch (Exception e) {
        out.println(JSFTestUtil.FAIL + " Unexpected Exception when calling"
            + " DateTimeConverter.getAsString() with a input value of" + " '"
            + controlObj.toString() + "' using a date style of '"
            + FORMAT_STYLES[i] + "' and a time style of '" + FORMAT_STYLES[j]
            + "'");
        out.println("Exception: " + e.toString());
        return;
      }

      if (!control.equals(result)) {
        out.println(
            JSFTestUtil.FAIL + " The result returned by DateTimeConverter"
                + ".getAsString() didn't yield the expected result using "
                + "date style '" + FORMAT_STYLES[i] + "' and time style" + " '"
                + FORMAT_STYLES[j] + "'");
        out.println("Expected: " + control);
        out.println("Received: " + result);
        failed = true;
      }
    }
    if (failed) {
      return;
    } else {
      out.println(JSFTestUtil.PASS);
    }
  }

  // DateTimeConverter.getAsObject() using a pattern
  public void dateTimeConverterGetAsObjectPatternTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    String pattern = "yyyyy.MMMMM.dd GGG hh:mm aaa";
    SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.US);
    dateFormat.setTimeZone(AMERICA_NEW_YORK);

    DateTimeConverter converter = new DateTimeConverter();
    converter.setLocale(Locale.US);
    converter.setTimeZone(AMERICA_NEW_YORK);
    converter.setPattern(pattern);

    // setting other formatting properties will be ignored
    // if pattern is set
    converter.setType(TYPE_DATE);
    converter.setDateStyle(STYLE_SHORT);
    converter.setTimeStyle(STYLE_SHORT);

    String controlString = dateFormat.format(CURRENT_DATE_TIME);
    Object controlObject;

    try {
      controlObject = dateFormat.parse(controlString);
    } catch (ParseException pe) {
      throw new ServletException(pe);
    }

    Object result;

    try {
      result = converter.getAsObject(getFacesContext(),
          getApplication().createComponent(UIInput.COMPONENT_TYPE),
          controlString);
    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + " Unexpected Exception when calling"
          + " DateTimeConverter.getAsString() with a input value of" + " '"
          + controlString + "'");
      out.println("Exception: " + e.toString());
      return;
    }

    if (!controlObject.equals(result)) {
      out.println(JSFTestUtil.FAIL + " The result returned by DateTimeConverter"
          + ".getAsObject() didn't yield the expected result.");
      out.println("Expected: " + controlObject.toString());
      out.println("Received: " + result.toString());
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // DateTimeConverter.getAsString() using a pattern
  public void dateTimeConverterGetAsStringPatternTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    String pattern = "yyyyy.MMMMM.dd GGG hh:mm aaa";
    SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.US);
    dateFormat.setTimeZone(AMERICA_NEW_YORK);

    DateTimeConverter converter = new DateTimeConverter();
    converter.setLocale(Locale.US);
    converter.setTimeZone(AMERICA_NEW_YORK);
    converter.setPattern(pattern);

    // setting other formatting properties will be ignored
    // if pattern is set
    converter.setType(TYPE_DATE);
    converter.setDateStyle(STYLE_SHORT);
    converter.setTimeStyle(STYLE_SHORT);

    String controlString = dateFormat.format(CURRENT_DATE_TIME);
    Object controlObject;
    try {
      controlObject = dateFormat.parse(controlString);
    } catch (ParseException pe) {
      throw new ServletException(pe);
    }

    Object result;

    try {
      result = converter.getAsString(getFacesContext(),
          getApplication().createComponent(UIInput.COMPONENT_TYPE),
          controlObject);
    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + " Unexpected Exception when calling"
          + " DateTimeConverter.getAsString() with a input value of" + " '"
          + controlObject.toString() + "'");
      out.println("Exception: " + e.toString());
      return;
    }

    if (!controlString.equals(result)) {
      out.println(JSFTestUtil.FAIL + " The result returned by DateTimeConverter"
          + ".getAsObject() didn't yield the expected result.");
      out.println("Expected: " + controlString);
      out.println("Received: " + result);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // DateTimeConverter.{getAsString(),getAsObject()} throws
  // ConverterException with invalid date style.
  public void dateTimeConverterInvalidDateStyleTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    DateTimeConverter converter = new DateTimeConverter();
    converter.setDateStyle("INVALID");
    String result = checkInvalidConfiguration(converter, "date style");
    if (result != null) {
      out.println(result);
    } else {
      out.println(JSFTestUtil.PASS);
    }
  }

  // DateTimeConverter.{getAsString(),getAsObject()} throws
  // ConverterException with invalid pattern.
  public void dateTimeConverterInvalidPatternTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    DateTimeConverter converter = new DateTimeConverter();
    converter.setPattern("INVALID");
    String result = checkInvalidConfiguration(converter, "pattern");
    if (result != null) {
      out.println(result);
    } else {
      out.println(JSFTestUtil.PASS);
    }
  }

  // DateTimeConverter.{getAsString(),getAsObject()} throws
  // ConverterException with invalid time style.
  public void dateTimeConverterInvalidTimeStyleTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    DateTimeConverter converter = new DateTimeConverter();
    converter.setType(TYPE_TIME);
    converter.setTimeStyle("INVALID");
    String result = checkInvalidConfiguration(converter, "time style");
    if (result != null) {
      out.println(result);
    } else {
      out.println(JSFTestUtil.PASS);
    }
  }

  // DateTimeConverter.{getAsString(),getAsObject()} throws
  // ConverterException with invalid type.
  public void dateTimeConverterInvalidTypeTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    DateTimeConverter converter = new DateTimeConverter();
    converter.setType("INVALID");
    String result = checkInvalidConfiguration(converter, "type");
    if (result != null) {
      out.println(result);
    } else {
      out.println(JSFTestUtil.PASS);
    }
  }

  public void dateTimeConverterGetAsStringNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    DateTimeConverter converter = new DateTimeConverter();
    UIComponent component = new UIInput();
    FacesContext context = getFacesContext();

    try {
      converter.getAsString(null, component, "value");
      out.println(JSFTestUtil.FAIL + " NullPointerException not thrown"
          + " when a null value was passed to the 'context' argument of"
          + " getAsString().");
      return;
    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown when a null value"
            + " was passed to the 'context' argument of getAsString(),"
            + " but it wasn't an instance of NullPointerException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    try {
      converter.getAsString(context, null, "value");
      out.println(JSFTestUtil.FAIL + " NullPointerException not thrown"
          + " when a null value was passed to the 'component' argument of"
          + " getAsString().");
      return;
    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown when a null value"
            + " was passed to the 'component' argument of getAsString(),"
            + " but it wasn't an instance of NullPointerException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    try {
      converter.getAsString(null, null, "value");
      out.println(JSFTestUtil.FAIL + " NullPointerException not thrown"
          + " when a null value was passed to the 'component' and "
          + "'context' arguments of getAsString().");
      return;
    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown when a null value"
            + " was passed to the 'component' and 'context' arguments"
            + " of getAsString(), but it wasn't an instance of "
            + "NullPointerException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    out.println(JSFTestUtil.PASS);

  }

  public void dateTimeConverterGetAsObjectNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    DateTimeConverter converter = new DateTimeConverter();
    UIComponent component = new UIInput();
    FacesContext context = getFacesContext();

    try {
      converter.getAsObject(null, component, "value");
      out.println(JSFTestUtil.FAIL + " NullPointerException not thrown"
          + " when a null value was passed to the 'context' argument of"
          + " getAsObject().");
      return;
    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown when a null value"
            + " was passed to the 'context' argument of getAsObject(),"
            + " but it wasn't an instance of NullPointerException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    try {
      converter.getAsObject(context, null, "value");
      out.println(JSFTestUtil.FAIL + " NullPointerException not thrown"
          + " when a null value was passed to the 'component' argument of"
          + " getAsObject().");
      return;
    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown when a null value"
            + " was passed to the 'component' argument of getAsString(),"
            + " but it wasn't an instance of NullPointerException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    try {
      converter.getAsObject(null, null, "value");
      out.println(JSFTestUtil.FAIL + " NullPointerException not thrown"
          + " when a null value was passed to the 'component' and "
          + "'context' arguments of getAsObject().");
      return;
    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown when a null value"
            + " was passed to the 'component' and 'context' arguments"
            + " of getAsObject(), but it wasn't an instance of "
            + "NullPointerException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    out.println(JSFTestUtil.PASS);

  }

  // -------------------------------------------------- Private Methods ------

  // Ensure CoverterException is thrown when getAsString() or
  // getAsObject() is called against a mis-configured
  // DateTimeConverter
  private String checkInvalidConfiguration(DateTimeConverter converter,
      String type) {
    DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT,
        Locale.US);
    dateFormat.setTimeZone(AMERICA_NEW_YORK);
    FacesContext context = getFacesContext();
    UIComponent component = getApplication()
        .createComponent(UIInput.COMPONENT_TYPE);

    try {
      converter.getAsObject(context, component,
          dateFormat.format(CURRENT_DATE_TIME));
      return (JSFTestUtil.FAIL + " No Exception thrown when DateTimeConveter"
          + " was configured with an invalid " + type + " .");
    } catch (Exception e) {
      if (!(e instanceof ConverterException)) {
        return (JSFTestUtil.FAIL + " Exception thrown when DateTime"
            + "Converter was configured with an invalid " + type
            + " but it wasn't an instance of ConverterException.\n"
            + "Exception received: " + e.getClass().getName());
      }
    }

    try {
      converter.getAsString(context, component,
          dateFormat.parse(dateFormat.format(CURRENT_DATE_TIME)));
      return ("Test FAILED [2].  No Exception thrown when DateTimeConveter"
          + " was configured with an invalid " + type + ".");
    } catch (Exception e) {
      if (!(e instanceof ConverterException)) {
        return ("Test FAILED [2].  Exception thrown when DateTime"
            + "Converter was configured with an invalid " + type
            + " but it wasn't an instance of ConverterException.\n"
            + "Exception received: " + e.getClass().getName());
      }
    }
    return null;
  }
}
