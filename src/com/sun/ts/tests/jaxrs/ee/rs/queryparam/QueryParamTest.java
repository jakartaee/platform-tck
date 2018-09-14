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

package com.sun.ts.tests.jaxrs.ee.rs.queryparam;

import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.sun.ts.tests.jaxrs.ee.rs.ParamEntityThrowingExceptionGivenByName;
import com.sun.ts.tests.jaxrs.ee.rs.ParamEntityThrowingWebApplicationException;
import com.sun.ts.tests.jaxrs.ee.rs.ParamEntityWithConstructor;
import com.sun.ts.tests.jaxrs.ee.rs.ParamEntityWithFromString;
import com.sun.ts.tests.jaxrs.ee.rs.ParamEntityWithValueOf;
import com.sun.ts.tests.jaxrs.ee.rs.ParamTest;

@Path(value = "/QueryParamTest")
public class QueryParamTest extends ParamTest {

  @DefaultValue("QueryParamTest")
  @QueryParam("FieldParamEntityWithConstructor")
  ParamEntityWithConstructor fieldParamEntityWithConstructor;

  @Encoded
  @DefaultValue("QueryParamTest")
  @QueryParam("FieldParamEntityWithFromString")
  ParamEntityWithFromString fieldParamEntityWithFromString;

  @DefaultValue("QueryParamTest")
  @QueryParam("FieldParamEntityWithValueOf")
  ParamEntityWithValueOf fieldParamEntityWithValueOf;

  @DefaultValue("QueryParamTest")
  @QueryParam("FieldSetParamEntityWithFromString")
  Set<ParamEntityWithFromString> fieldSetParamEntityWithFromString;

  @DefaultValue("QueryParamTest")
  @QueryParam("FieldSortedSetParamEntityWithFromString")
  SortedSet<ParamEntityWithFromString> fieldSortedSetParamEntityWithFromString;

  @DefaultValue("QueryParamTest")
  @QueryParam("FieldListParamEntityWithFromString")
  List<ParamEntityWithFromString> fieldListParamEntityWithFromString;

  @QueryParam("FieldParamEntityThrowingWebApplicationException")
  public ParamEntityThrowingWebApplicationException fieldEntityThrowingWebApplicationException;

  @QueryParam("FieldParamEntityThrowingExceptionGivenByName")
  public ParamEntityThrowingExceptionGivenByName fieldEntityThrowingExceptionGivenByName;

  @GET
  @Produces("text/plain")
  public String stringParamHandling(
      @QueryParam("stringtest") @DefaultValue("abc") String stringheader,
      @QueryParam("stringtest1") @DefaultValue("default") String stringheader1,
      @QueryParam("stringtest2") @DefaultValue("default") String stringheader2,
      @QueryParam("inttest") int intheader,
      @QueryParam("inttest1") int intheader1,
      @QueryParam("inttest2") int intheader2,
      @QueryParam("bytetest") byte byteheader,
      @QueryParam("bytetest1") byte byteheader1,
      @QueryParam("bytetest2") byte byteheader2,
      @QueryParam("doubletest") double doubleheader,
      @QueryParam("doubletest1") double doubleheader1,
      @QueryParam("doubletest2") double doubleheader2,
      @QueryParam("floattest") float floatheader,
      @QueryParam("floattest1") float floatheader1,
      @QueryParam("floattest2") float floatheader2,
      @QueryParam("shorttest") short shortheader,
      @QueryParam("shorttest1") short shortheader1,
      @QueryParam("shorttest2") short shortheader2,
      @QueryParam("longtest") long longheader,
      @QueryParam("longtest1") long longheader1,
      @QueryParam("longtest2") long longheader2,
      @QueryParam("booleantest") boolean booleanheader,
      @QueryParam("booleantest1") boolean booleanheader1,
      @QueryParam("booleantest2") boolean booleanheader2,
      @DefaultValue("QueryParamTest") @QueryParam("ParamEntityWithConstructor") ParamEntityWithConstructor paramEntityWithConstructor,
      @Encoded @DefaultValue("QueryParamTest") @QueryParam("ParamEntityWithFromString") ParamEntityWithFromString paramEntityWithFromString,
      @DefaultValue("QueryParamTest") @QueryParam("ParamEntityWithValueOf") ParamEntityWithValueOf paramEntityWithValueOf,
      @DefaultValue("QueryParamTest") @QueryParam("SetParamEntityWithFromString") Set<ParamEntityWithFromString> setParamEntityWithFromString,
      @DefaultValue("QueryParamTest") @QueryParam("SortedSetParamEntityWithFromString") SortedSet<ParamEntityWithFromString> sortedSetParamEntityWithFromString,
      @DefaultValue("QueryParamTest") @QueryParam("ListParamEntityWithFromString") List<ParamEntityWithFromString> listParamEntityWithFromString,
      @QueryParam("ParamEntityThrowingWebApplicationException") ParamEntityThrowingWebApplicationException paramEntityThrowingWebApplicationException,
      @QueryParam("ParamEntityThrowingExceptionGivenByName") ParamEntityThrowingExceptionGivenByName paramEntityThrowingExceptionGivenByName) {

    noparam = true;

    sb = new StringBuilder();
    if (stringheader == "" || stringheader == null
        || !stringheader.equals("abc"))
      noparam = false;
    sb.append("stringtest=").append(stringheader);

    if (stringheader1 == "" || stringheader1 == null
        || !stringheader1.equals("default"))
      noparam = false;
    sb.append("stringtest1=").append(stringheader1);

    if (stringheader2 == "" || stringheader2 == null
        || !stringheader2.equals("default"))
      noparam = false;
    sb.append("stringtest2=").append(stringheader2);

    appendNonNullSetNoParam("inttest", intheader);
    appendNonNullSetNoParam("inttest1", intheader1);
    appendNonNullSetNoParam("inttest2", intheader2);
    appendNonNullSetNoParam("doubletest", doubleheader);
    appendNonNullSetNoParam("doubletest1", doubleheader1);
    appendNonNullSetNoParam("doubletest2", doubleheader2);
    appendNonNullSetNoParam("floattest", floatheader);
    appendNonNullSetNoParam("floattest1", floatheader1);
    appendNonNullSetNoParam("floattest2", floatheader2);
    appendNonNullSetNoParam("longtest", longheader);
    appendNonNullSetNoParam("longtest1", longheader1);
    appendNonNullSetNoParam("longtest2", longheader2);
    appendNonNullSetNoParam("shorttest", shortheader);
    appendNonNullSetNoParam("shorttest1", shortheader1);
    appendNonNullSetNoParam("shorttest2", shortheader2);
    appendNonNullSetNoParam("bytetest", byteheader);
    appendNonNullSetNoParam("bytetest1", byteheader1);
    appendNonNullSetNoParam("bytetest2", byteheader2);
    appendTrueSetNoParam("booleantest", booleanheader);
    appendTrueSetNoParam("booleantest1", booleanheader1);
    appendTrueSetNoParam("booleantest2", booleanheader2);

    setReturnValues(paramEntityWithConstructor, paramEntityWithFromString,
        paramEntityWithValueOf, setParamEntityWithFromString,
        sortedSetParamEntityWithFromString, listParamEntityWithFromString, "");

    setReturnValues(fieldParamEntityWithConstructor,
        fieldParamEntityWithFromString, fieldParamEntityWithValueOf,
        fieldSetParamEntityWithFromString,
        fieldSortedSetParamEntityWithFromString,
        fieldListParamEntityWithFromString, FIELD);

    if (noparam)
      sb.append("No QueryParam");
    return sb.toString();
  }
}
