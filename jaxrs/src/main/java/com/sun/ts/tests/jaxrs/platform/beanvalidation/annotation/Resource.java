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

package com.sun.ts.tests.jaxrs.platform.beanvalidation.annotation;

import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("resource")
public class Resource {

  @POST
  @Path("notshortnorfive")
  public String notShortNorFive(@Valid NotShortNorFiveStringBean bean) {
    return bean.get();
  }

  @POST
  @Path("returnnotshortnorfive")
  @Valid
  public NotShortNorFiveStringBean notShortNorFive(String content) {
    return new NotShortNorFiveStringBean(content);
  }

  @GET
  @Valid
  @Path("returnnull")
  public NotShortNorFiveStringBean returnNull() {
    return new NotShortNorFiveStringBean(null);
  }

  @POST
  @Path("directannotatedarg")
  public String directStringArgument(
      @NotFiveNorShort NotShortNorFiveStringBean content) {
    return content.get();
  }

  @POST
  @Path("directannotatedreturn")
  @NotFiveNorShort
  public NotShortNorFiveStringBean returnDirectString(String content) {
    return new NotShortNorFiveStringBean(content);
  }

  @GET
  @Path("directannotatedreturnnull")
  @NotFiveNorShort
  public NotShortNorFiveStringBean returnDirectNull() {
    return new NotShortNorFiveStringBean(null);
  }

}
