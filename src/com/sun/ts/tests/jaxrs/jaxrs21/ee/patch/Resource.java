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

package com.sun.ts.tests.jaxrs.jaxrs21.ee.patch;

import javax.ws.rs.PATCH;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("resource")
public class Resource {
  public static final long SLEEP_TIME = 1500L;

  @PATCH
  @Path("patch")
  public String patch(String in) {
    return in;
  }

  @PATCH
  @Path("patchnotok")
  public Response patchNotOk(String value) {
    return Response.status(Status.NOT_ACCEPTABLE).build();
  }

  @PATCH
  @Path("patchandwait")
  public String postAndWait(String value) throws InterruptedException {
    Thread.sleep(SLEEP_TIME);
    return value;
  }

}
