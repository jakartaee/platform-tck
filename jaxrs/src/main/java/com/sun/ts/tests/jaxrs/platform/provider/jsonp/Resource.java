/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxrs.platform.provider.jsonp;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonStructure;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("resource")
public class Resource {

  public static final String[] LABEL = { "type", "number" };

  public static final String[] TYPE = { "home", "fax" };

  public static final String[] PHONE = { "212 555-1234", "646 555-4567" };

  @Path("tostructure")
  @GET
  public JsonStructure toStructure() {
    return toArray();
  }

  @Path("toarray")
  @GET
  public JsonArray toArray() {
    JsonArray array = createArray();
    return array;
  }

  @Path("toobject")
  @GET
  public JsonObject toObject() {
    return createObject(0);
  }

  @Path("fromobject")
  @POST
  public String fromObject(JsonObject object) {
    return object.toString();
  }

  @Path("fromarray")
  @POST
  public String fromArray(JsonArray array) {
    return array.toString();
  }

  @Path("fromstructure")
  @POST
  public String fromArray(JsonStructure struct) {
    return struct.toString();
  }

  public static JsonObject createObject(int id) {
    JsonObject object = Json.createObjectBuilder().add(LABEL[0], TYPE[id])
        .add(LABEL[1], PHONE[id]).build();
    return object;
  }

  public static JsonArray createArray() {
    JsonArray array = Json.createArrayBuilder().add(createObject(0))
        .add(createObject(1)).build();
    return array;
  }

}
