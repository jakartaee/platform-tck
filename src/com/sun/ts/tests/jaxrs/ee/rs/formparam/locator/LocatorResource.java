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

package com.sun.ts.tests.jaxrs.ee.rs.formparam.locator;

import javax.ws.rs.Consumes;
import javax.ws.rs.Encoded;
import javax.ws.rs.FormParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

@Path("resource")
public class LocatorResource extends MiddleResource {

  @Path("locator/{id1}")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public MiddleResource locatorHasArguments(@PathParam("id1") String id1,
      @FormParam("default_argument") String arg) throws Exception {
    return new MiddleResource(id1, arg);
  }

  @Path("locatorencoded/{id1}")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public MiddleResource locatorHasEncodedArguments(@PathParam("id1") String id1,
      @Encoded @FormParam("default_argument") String arg) throws Exception {
    return new MiddleResource(id1, arg);
  }
}
