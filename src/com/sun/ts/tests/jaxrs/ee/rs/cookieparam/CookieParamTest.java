/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxrs.ee.rs.cookieparam;

import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import javax.ws.rs.CookieParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.sun.ts.tests.jaxrs.ee.rs.ParamEntityThrowingExceptionGivenByName;
import com.sun.ts.tests.jaxrs.ee.rs.ParamEntityThrowingWebApplicationException;
import com.sun.ts.tests.jaxrs.ee.rs.ParamEntityWithConstructor;
import com.sun.ts.tests.jaxrs.ee.rs.ParamEntityWithFromString;
import com.sun.ts.tests.jaxrs.ee.rs.ParamEntityWithValueOf;
import com.sun.ts.tests.jaxrs.ee.rs.ParamTest;

@Path(value = "/CookieParamTest")
public class CookieParamTest extends ParamTest {

  @DefaultValue("CookieParamTest")
  @CookieParam("FieldParamEntityWithConstructor")
  ParamEntityWithConstructor fieldParamEntityWithConstructor;

  @DefaultValue("CookieParamTest")
  @CookieParam("FieldParamEntityWithFromString")
  ParamEntityWithFromString fieldParamEntityWithFromString;

  @DefaultValue("CookieParamTest")
  @CookieParam("FieldParamEntityWithValueOf")
  ParamEntityWithValueOf fieldParamEntityWithValueOf;

  @DefaultValue("CookieParamTest")
  @CookieParam("FieldSetParamEntityWithFromString")
  Set<ParamEntityWithFromString> fieldSetParamEntityWithFromString;

  @DefaultValue("CookieParamTest")
  @CookieParam("FieldSortedSetParamEntityWithFromString")
  SortedSet<ParamEntityWithFromString> fieldSortedSetParamEntityWithFromString;

  @DefaultValue("CookieParamTest")
  @CookieParam("FieldListParamEntityWithFromString")
  List<ParamEntityWithFromString> fieldListParamEntityWithFromString;

  @CookieParam("FieldParamEntityThrowingWebApplicationException")
  public ParamEntityThrowingWebApplicationException fieldParamEntityThrowingWebApplicationException;

  @CookieParam("FieldParamEntityThrowingExceptionGivenByName")
  public ParamEntityThrowingExceptionGivenByName fieldParamEntityThrowingExceptionGivenByName;

  @GET
  public Response cookieParamHandling(@QueryParam("todo") String todo,
      @CookieParam("name1") @DefaultValue("abc") String value,
      @DefaultValue("CookieParamTest") @CookieParam("ParamEntityWithConstructor") ParamEntityWithConstructor paramEntityWithConstructor,
      @DefaultValue("CookieParamTest") @CookieParam("ParamEntityWithFromString") ParamEntityWithFromString paramEntityWithFromString,
      @DefaultValue("CookieParamTest") @CookieParam("ParamEntityWithValueOf") ParamEntityWithValueOf paramEntityWithValueOf,
      @DefaultValue("CookieParamTest") @CookieParam("SetParamEntityWithFromString") Set<ParamEntityWithFromString> setParamEntityWithFromString,
      @DefaultValue("CookieParamTest") @CookieParam("SortedSetParamEntityWithFromString") SortedSet<ParamEntityWithFromString> sortedSetParamEntityWithFromString,
      @DefaultValue("CookieParamTest") @CookieParam("ListParamEntityWithFromString") List<ParamEntityWithFromString> listParamEntityWithFromString,
      @CookieParam("ParamEntityThrowingWebApplicationException") ParamEntityThrowingWebApplicationException paramEntityThrowingWebApplicationException,
      @CookieParam("ParamEntityThrowingExceptionGivenByName") ParamEntityThrowingExceptionGivenByName paramEntityThrowingExceptionGivenByName) {

    sb = new StringBuilder();
    Response.ResponseBuilder respb = Response.status(200);

    if (todo == null) {
      sb.append("todo=null");
    } else if (todo.equalsIgnoreCase("setCookie")) {
      String cookie_name = "name1";
      String cookie_value = "value1";
      Cookie ck = new Cookie(cookie_name, cookie_value);
      NewCookie nck = new NewCookie(ck);
      respb = respb.cookie(nck);
      sb.append("setCookie=done");
    } else if (todo.equalsIgnoreCase("verifycookie")) {
      sb.append("name1" + "=" + value);
      sb.append("verifyCookie=done");
    } else if (todo.equals("")) {
      setReturnValues(paramEntityWithConstructor, paramEntityWithFromString,
          paramEntityWithValueOf, setParamEntityWithFromString,
          sortedSetParamEntityWithFromString, listParamEntityWithFromString,
          "");
      setReturnValues(fieldParamEntityWithConstructor,
          fieldParamEntityWithFromString, fieldParamEntityWithValueOf,
          fieldSetParamEntityWithFromString,
          fieldSortedSetParamEntityWithFromString,
          fieldListParamEntityWithFromString, FIELD);
    } else if (todo.contains("ParamEntity")) {
      setNewCookie(respb, todo);
      setReturnValues(paramEntityWithConstructor, paramEntityWithFromString,
          paramEntityWithValueOf, setParamEntityWithFromString,
          sortedSetParamEntityWithFromString, listParamEntityWithFromString,
          "");
      setReturnValues(fieldParamEntityWithConstructor,
          fieldParamEntityWithFromString, fieldParamEntityWithValueOf,
          fieldSetParamEntityWithFromString,
          fieldSortedSetParamEntityWithFromString,
          fieldListParamEntityWithFromString, FIELD);
    } else {
      sb.append("other stuff");
    }

    return respb.entity(sb.toString()).build();
  }

  private static void setNewCookie(ResponseBuilder rb, String queryParam) {
    if (!queryParam.contains("="))
      return;
    String[] split = queryParam.split("=");
    Cookie cookie = new Cookie(split[0], split[1]);
    NewCookie newCookie = new NewCookie(cookie);
    rb.cookie(newCookie);
  }

}
