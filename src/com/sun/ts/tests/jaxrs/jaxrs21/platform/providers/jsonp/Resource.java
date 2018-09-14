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

package com.sun.ts.tests.jaxrs.jaxrs21.platform.providers.jsonp;

import java.math.BigDecimal;

import javax.json.Json;
import javax.json.JsonNumber;
import javax.json.JsonString;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("resource")
public class Resource {

  public static final String MESSAGE = "This.is.some.message.to.be.sent.as.json";

  @Path("tostring")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public JsonString toStructure() {
    return Json.createValue(MESSAGE);
  }

  @Path("tonumber")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public JsonNumber toNumber() {
    return Json.createValue(BigDecimal.valueOf(Long.MIN_VALUE));
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
