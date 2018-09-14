/*
 * Copyright (c) 2012, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxrs.ee.rs.cookieparam.locator;

import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import javax.ws.rs.CookieParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.sun.ts.tests.jaxrs.ee.rs.ParamEntityThrowingExceptionGivenByName;
import com.sun.ts.tests.jaxrs.ee.rs.ParamEntityThrowingWebApplicationException;
import com.sun.ts.tests.jaxrs.ee.rs.ParamEntityWithConstructor;
import com.sun.ts.tests.jaxrs.ee.rs.ParamEntityWithFromString;
import com.sun.ts.tests.jaxrs.ee.rs.ParamEntityWithValueOf;

@Path("resource")
public class LocatorResource extends MiddleResource {

  @Path("locator")
  public MiddleResource locatorHasArguments(@QueryParam("todo") String todo,
      @CookieParam("name1") @DefaultValue("abc") String value,
      @DefaultValue("CookieParamTest") @CookieParam("ParamEntityWithConstructor") ParamEntityWithConstructor paramEntityWithConstructor,
      @DefaultValue("CookieParamTest") @CookieParam("ParamEntityWithFromString") ParamEntityWithFromString paramEntityWithFromString,
      @DefaultValue("CookieParamTest") @CookieParam("ParamEntityWithValueOf") ParamEntityWithValueOf paramEntityWithValueOf,
      @DefaultValue("CookieParamTest") @CookieParam("SetParamEntityWithFromString") Set<ParamEntityWithFromString> setParamEntityWithFromString,
      @DefaultValue("CookieParamTest") @CookieParam("SortedSetParamEntityWithFromString") SortedSet<ParamEntityWithFromString> sortedSetParamEntityWithFromString,
      @DefaultValue("CookieParamTest") @CookieParam("ListParamEntityWithFromString") List<ParamEntityWithFromString> listParamEntityWithFromString,
      @CookieParam("ParamEntityThrowingWebApplicationException") ParamEntityThrowingWebApplicationException paramEntityThrowingWebApplicationException,
      @CookieParam("ParamEntityThrowingExceptionGivenByName") ParamEntityThrowingExceptionGivenByName paramEntityThrowingExceptionGivenByName) {
    return new MiddleResource(todo, value, paramEntityWithConstructor,
        paramEntityWithFromString, paramEntityWithValueOf,
        setParamEntityWithFromString, sortedSetParamEntityWithFromString,
        listParamEntityWithFromString,
        paramEntityThrowingWebApplicationException,
        paramEntityThrowingExceptionGivenByName);
  }

}
