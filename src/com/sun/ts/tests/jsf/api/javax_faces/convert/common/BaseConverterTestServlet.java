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
package com.sun.ts.tests.jsf.api.javax_faces.convert.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.convert.BigDecimalConverter;
import javax.faces.convert.BigIntegerConverter;
import javax.faces.convert.BooleanConverter;
import javax.faces.convert.ByteConverter;
import javax.faces.convert.CharacterConverter;
import javax.faces.convert.Converter;
import javax.faces.convert.DoubleConverter;
import javax.faces.convert.FloatConverter;
import javax.faces.convert.IntegerConverter;
import javax.faces.convert.LongConverter;
import javax.faces.convert.ShortConverter;
import javax.faces.convert.DateTimeConverter;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

/**
 * <p>
 * Common test Servlet for testing convertions to Object and String using
 * standard Java boxed types.
 * </p>
 */
public final class BaseConverterTestServlet extends HttpTCKServlet {

  private Map<Object, Object> objectMap;

  private Map<String, String> stringMap;

  private Map<Object, Object> converterMap;

  private UIComponent component;

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    objectMap = new HashMap<Object, Object>();
    stringMap = new HashMap<String, String>();
    converterMap = new HashMap<Object, Object>();
    component = new UIInput();

    objectMap.put("boolean", Boolean.TRUE);
    stringMap.put("boolean", objectMap.get("boolean").toString());
    converterMap.put("boolean", new BooleanConverter());

    objectMap.put("byte", Byte.valueOf((byte) 'a'));
    stringMap.put("byte", objectMap.get("byte").toString());
    converterMap.put("byte", new ByteConverter());

    objectMap.put("character", Character.valueOf('b'));
    stringMap.put("character", objectMap.get("character").toString());
    converterMap.put("character", new CharacterConverter());

    objectMap.put("short", Short.valueOf("1"));
    stringMap.put("short", objectMap.get("short").toString());
    converterMap.put("short", new ShortConverter());

    objectMap.put("integer", Integer.valueOf(2));
    stringMap.put("integer", objectMap.get("integer").toString());
    converterMap.put("integer", new IntegerConverter());

    objectMap.put("long", Long.valueOf(3l));
    stringMap.put("long", objectMap.get("long").toString());
    converterMap.put("long", new LongConverter());

    objectMap.put("float", Float.valueOf(4.1f));
    stringMap.put("float", objectMap.get("float").toString());
    converterMap.put("float", new FloatConverter());

    objectMap.put("double", Double.valueOf(5.1d));
    stringMap.put("double", objectMap.get("double").toString());
    converterMap.put("double", new DoubleConverter());

    objectMap.put("bigdecimal", BigDecimal.valueOf(5.1d));
    stringMap.put("bigdecimal", objectMap.get("bigdecimal").toString());
    converterMap.put("bigdecimal", new BigDecimalConverter());

    objectMap.put("biginteger", BigInteger.valueOf(6));
    stringMap.put("biginteger", objectMap.get("biginteger").toString());
    converterMap.put("biginteger", new BigIntegerConverter());

    objectMap.put("localtime", LocalTime.parse("00:00"));
    DateTimeConverter localtimeConverter = new DateTimeConverter();
    localtimeConverter.setType("localTime");
    localtimeConverter.setPattern("HH:mm");
    stringMap.put("localtime", "00:00");
    converterMap.put("localtime", localtimeConverter);

    objectMap.put("localdate", LocalDate.parse("2017-02-03"));
    DateTimeConverter localDateConverter = new DateTimeConverter();
    localDateConverter.setType("localDate");
    localDateConverter.setPattern("yyyy-MM-dd");
    stringMap.put("localdate", "2017-02-03");
    converterMap.put("localdate", localDateConverter);

    objectMap.put("localdatetime", LocalDateTime.parse("2017-02-03T12:34:56"));
    DateTimeConverter localDateTimeConverter = new DateTimeConverter();
    localDateTimeConverter.setType("localDateTime");
    localDateTimeConverter.setPattern("yyyy-MM-dd HH:mm:ss");
    stringMap.put("localdatetime", "2017-02-03 12:34:56");
    converterMap.put("localdatetime", localDateTimeConverter);

    objectMap.put("offsettime", OffsetTime.parse("10:15:30+01:00"));
    DateTimeConverter offsettimeConverter = new DateTimeConverter();
    offsettimeConverter.setType("offsetTime");
    offsettimeConverter.setPattern("HH:mm:ssXXX");
    stringMap.put("offsettime", "10:15:30+01:00");
    converterMap.put("offsettime", offsettimeConverter);

    objectMap.put("offsetdatetime",
        OffsetDateTime.parse("2017-02-03T12:34:56+01:00"));
    DateTimeConverter offsetDateTimeConverter = new DateTimeConverter();
    offsetDateTimeConverter.setType("offsetDateTime");
    offsetDateTimeConverter.setPattern("yyyy-MM-dd HH:mm:ssXXX");
    stringMap.put("offsetdatetime", "2017-02-03 12:34:56+01:00");
    converterMap.put("offsetdatetime", offsetDateTimeConverter);

    objectMap.put("zoneddatetime",
        ZonedDateTime.parse("2017-02-03T12:34:56+01:00[Europe/Paris]"));
    DateTimeConverter zonedDateTimeConverter = new DateTimeConverter();
    zonedDateTimeConverter.setType("zonedDateTime");
    zonedDateTimeConverter.setPattern("yyyy-MM-dd HH:mm:ssXXX'['VV']'");
    stringMap.put("zoneddatetime", "2017-02-03 12:34:56+01:00[Europe/Paris]");
    converterMap.put("zoneddatetime", zonedDateTimeConverter);
  }

  // ------------------------------------------- Test Methods ----
  public void converterGetAsObjectTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    String type = request.getParameter("type");

    Converter converter = (Converter) converterMap.get(type);
    String inputValue = (String) stringMap.get(type);
    Object expectedResult = objectMap.get(type);
    Object convResult = converter.getAsObject(getFacesContext(), component,
        inputValue);

    if (!expectedResult.equals(convResult)) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Expected conversion of String value " + inputValue + ": "
          + expectedResult.toString() + JSFTestUtil.NL + "'Received: "
          + convResult.toString());
      return;
    }

    convResult = converter.getAsObject(getFacesContext(), component, "");
    if (convResult != null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + " Expected getAsObject() to return null if value was"
          + " a zero-length String." + JSFTestUtil.NL + "Value returned: "
          + convResult);
      return;
    }

    try {
      convResult = converter.getAsObject(getFacesContext(), component, null);
      if (convResult != null) {
        out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
            + "Expected getAsObject() to return null if value " + "was null."
            + JSFTestUtil.NL + "Value returned: " + convResult);
        return;
      }

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unexpected Exception thrown when " + "passing a null value."
          + JSFTestUtil.NL + "Exception: " + e);
    }

    out.println(JSFTestUtil.PASS);
  }

  public String converterGetAsObjectNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();

    String type = request.getParameter("type");
    Converter converter = (Converter) converterMap.get(type);
    String inputValue = (String) stringMap.get(type);

    JSFTestUtil.checkForNPE(converter, "getAsObject",
        new Class<?>[] { FacesContext.class, UIComponent.class, String.class },
        new Object[] { null, component, inputValue }, pw);

    JSFTestUtil.checkForNPE(converter, "getAsObject",
        new Class<?>[] { FacesContext.class, UIComponent.class, String.class },
        new Object[] { getFacesContext(), null, inputValue }, pw);

    return JSFTestUtil.PASS;
  }

  public void converterGetAsStringTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    String type = request.getParameter("type");
    Converter converter = (Converter) converterMap.get(type);
    Object inputValue = objectMap.get(type);
    Object expectedResult = stringMap.get(type);
    Object convResult = converter.getAsString(getFacesContext(), component,
        inputValue);
    if (!expectedResult.equals(convResult)) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Expected conversion of Object value " + inputValue + ": "
          + expectedResult.toString() + JSFTestUtil.NL + "Result: "
          + convResult.toString());
      return;
    }

    convResult = converter.getAsString(getFacesContext(), component, "");
    if (!"".equals(convResult)) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Expected getAsString() to return a zero-length "
          + "String if value was a zero-length String." + JSFTestUtil.NL
          + "Value returned: " + convResult);
      return;
    }

    try {
      convResult = converter.getAsString(getFacesContext(), component, null);
      if (!"".equals(convResult)) {
        out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
            + "Expected getAsString() to return a zero-length "
            + "String if value was null." + JSFTestUtil.NL + "Value returned: "
            + convResult);
        return;
      }
    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unexpected Exception thrown when passing a null " + "value."
          + JSFTestUtil.NL + "Exception: " + e);
    }

    out.println(JSFTestUtil.PASS);
  }

  public String converterGetAsStringNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();

    String type = request.getParameter("type");
    Converter converter = (Converter) converterMap.get(type);
    Object inputValue = objectMap.get(type);

    JSFTestUtil.checkForNPE(converter, "getAsString",
        new Class<?>[] { FacesContext.class, UIComponent.class, Object.class },
        new Object[] { null, component, inputValue }, pw);

    JSFTestUtil.checkForNPE(converter, "getAsString",
        new Class<?>[] { FacesContext.class, UIComponent.class, Object.class },
        new Object[] { getFacesContext(), null, inputValue }, pw);

    return JSFTestUtil.PASS;
  }

}
