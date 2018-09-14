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

package com.sun.ts.tests.jaxrs.ee.rs.client.asyncinvoker;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.sun.ts.tests.jaxrs.common.impl.TRACE;

@Path("resource")
public class Resource {

  public static final long SLEEP_TIME = 2000L;

  @GET
  @Path("get")
  public String get() {
    return "get";
  }

  @GET
  @Path("getandwait")
  public String getAndWait() throws InterruptedException {
    Thread.sleep(SLEEP_TIME);
    return "get";
  }

  @GET
  @Path("getnotok")
  public Response getReturnsStatusNotOk() {
    return Response.status(Status.NOT_ACCEPTABLE).build();
  }

  @HEAD
  @Path("head")
  public String head() {
    return "head";
  }

  @HEAD
  @Path("headandwait")
  public String headAndWait() throws InterruptedException {
    Thread.sleep(SLEEP_TIME);
    return "head";
  }

  @HEAD
  @Path("headnotok")
  public Response headReturnsStatusNotOk() {
    return Response.status(Status.NOT_ACCEPTABLE).build();
  }

  @PUT
  @Path("put")
  public String put(String value) {
    return value;
  }

  @PUT
  @Path("putandwait")
  public String putAndWait(String value) throws InterruptedException {
    Thread.sleep(SLEEP_TIME);
    return value;
  }

  @PUT
  @Path("putnotok")
  public Response putReturnsStatusNotOk(String value) {
    return Response.status(Status.NOT_ACCEPTABLE).build();
  }

  @POST
  @Path("post")
  public String post(String value) {
    return value;
  }

  @POST
  @Path("postandwait")
  public String postAndWait(String value) throws InterruptedException {
    Thread.sleep(SLEEP_TIME);
    return value;
  }

  @POST
  @Path("postnotok")
  public Response postReturnsStatusNotOk(String string) {
    return Response.status(Status.NOT_ACCEPTABLE).build();
  }

  @DELETE
  @Path("delete")
  public String delete() {
    return "delete";
  }

  @DELETE
  @Path("deleteandwait")
  public String deleteAndWait() throws InterruptedException {
    Thread.sleep(SLEEP_TIME);
    return "delete";
  }

  @DELETE
  @Path("deletenotok")
  public Response deleteReturnsStatusNotOk() {
    return Response.status(Status.NOT_ACCEPTABLE).build();
  }

  @OPTIONS
  @Path("options")
  public String options() {
    return "options";
  }

  @OPTIONS
  @Path("optionsandwait")
  public String optionsAndWait() throws InterruptedException {
    Thread.sleep(SLEEP_TIME);
    return "options";
  }

  @OPTIONS
  @Path("optionsnotok")
  public Response optionsReturnsStatusNotOk() {
    return Response.status(Status.NOT_ACCEPTABLE).build();
  }

  @TRACE
  @Path("trace")
  public String trace() {
    return "trace";
  }

  @TRACE
  @Path("tracenotok")
  public Response traceReturnsStatusNotOk() {
    return Response.status(Status.NOT_ACCEPTABLE).build();
  }

  @TRACE
  @Path("traceandwait")
  public String traceAndWait() throws InterruptedException {
    Thread.sleep(SLEEP_TIME);
    return trace();
  }

}
