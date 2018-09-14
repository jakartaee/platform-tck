/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxrs.jaxrs21.platform.providers.jsonb;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.TimeZone;

import javax.json.JsonNumber;
import javax.json.JsonString;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("resource")
public class Resource {

  public static final String MESSAGE = "This.is.some.message";

  public static final String URL = "http://tck.cts.oracle.com:12345";

  @Path("tostring")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public String toString() {
    return MESSAGE;
  }

  @Path("tochar")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Character toCharacter() {
    return MESSAGE.charAt(0);
  }

  @Path("tobyte")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Byte toByte() {
    return Byte.MAX_VALUE;
  }

  @Path("toshort")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Short toShort() {
    return Short.MAX_VALUE;
  }

  @Path("toint")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Integer toInteger() {
    return Integer.MAX_VALUE;
  }

  @Path("tolong")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Long toLong() {
    return Long.MAX_VALUE;
  }

  @Path("todouble")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Double toDouble() {
    return Double.MAX_VALUE;
  }

  @Path("toboolean")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Boolean toBoolean() {
    return Boolean.TRUE;
  }

  @Path("tonumber")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Number toNumber() {
    return BigDecimal.valueOf(Long.MAX_VALUE);
  }

  @Path("tobiginteger")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public BigInteger toBigInteger() {
    return BigInteger.valueOf(Long.MAX_VALUE);
  }

  @Path("touri")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public URI toURI() throws URISyntaxException {
    return new URI(URL);
  }

  @Path("tourl")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public URL toURL() throws MalformedURLException {
    return new URL(URL);
  }

  @Path("tooptional")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Optional<String> toOptional() {
    return Optional.of(MESSAGE);
  }

  @Path("tooptionalint")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public OptionalInt toOptionalInt() {
    return OptionalInt.of(Integer.MIN_VALUE);
  }

  @Path("tooptionallong")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public OptionalLong toOptionalLong() {
    return OptionalLong.of(Long.MIN_VALUE);
  }

  @Path("tooptionaldouble")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public OptionalDouble toOptionalDouble() {
    return OptionalDouble.of(Double.MIN_VALUE);
  }

  @Path("tocalendar")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Calendar toCalendar() {
    Calendar c = GregorianCalendar
        .getInstance(TimeZone.getTimeZone(ZoneId.of("Z")), Locale.US);
    c.set(1999, 11, 31);
    return c;
  }

  @Path("totimezone")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public TimeZone toTimeZone() {
    TimeZone tz = TimeZone.getTimeZone(ZoneId.of("Z"));
    return tz;
  }

  @Path("fromstring")
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  public String fromObject(JsonString string) {
    return string.getString();
  }

  @Path("fromnumber")
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  public String fromNumber(JsonNumber number) {
    return number.bigDecimalValue().toString();
  }
}
