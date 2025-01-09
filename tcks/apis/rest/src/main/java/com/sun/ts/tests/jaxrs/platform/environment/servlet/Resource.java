/*
 * Copyright (c) 2012, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxrs.platform.environment.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;

@Path("resource")
public class Resource {

  @Path("context")
  @GET
  public String context(@Context ServletConfig config,
      @Context ServletContext context, @Context HttpServletRequest request,
      @Context HttpServletResponse response) {
    StringBuilder sb = new StringBuilder();
    assertNull(sb, "ServletConfig", config);
    assertNull(sb, "ServletContext", context);
    assertNull(sb, "HttpServletRequest", request);
    assertNull(sb, "HttpServletResponse", response);
    return sb.toString();
  }

  private static void assertNull(StringBuilder sb, String objectName,
      Object o) {
    if (o == null)
      sb.append(objectName).append(" is null!").append("\n");
  }

  @Path("streamreader")
  @POST
  public String streamreader(@Context HttpServletRequest request)
      throws IOException {
    return readRequestEntity(request);
  }

  public static final String readRequestEntity(ServletRequest request)
      throws IOException {
    InputStream is = request.getInputStream();
    InputStreamReader isr = new InputStreamReader(is);
    BufferedReader bf = new BufferedReader(isr);
    String txt = bf.readLine();
    // bf.close();
    // isr.close();
    return txt;
  }

  @Path("premature")
  @GET
  public Response premature(@Context HttpServletResponse response)
      throws IOException {
    response.setStatus(200);
    response.flushBuffer();
    return Response.notAcceptable(null).build();
  }

  @Path("consume")
  @POST
  public String consume(@FormParam("entity") String consumed,
      @Context HttpServletRequest request) throws IOException {
    return consumed;
  }

  @Path("query")
  @POST
  public String query(@FormParam("query") String param) {
    return param;
  }
}
