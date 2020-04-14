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

package com.sun.ts.tests.jaxrs.ee.rs.ext.interceptor.containerreader.interceptorcontext;

import java.io.IOException;
import java.io.InputStreamReader;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.sun.ts.tests.jaxrs.common.util.JaxrsUtil;

@Path("resource")
public class Resource {

  @POST
  @Path("string")
  public String post(@NotNull @Size(min = 2) String entity) {
    return entity;
  }

  @POST
  @Path("inputstreamreader")
  public String post(InputStreamReader reader) throws IOException {
    String entity = JaxrsUtil.readFromReader(reader);
    reader.close();
    return entity;
  }

}
