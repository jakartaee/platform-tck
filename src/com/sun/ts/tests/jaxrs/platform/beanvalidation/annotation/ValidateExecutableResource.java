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
import jakarta.validation.executable.ExecutableType;
import jakarta.validation.executable.ValidateOnExecution;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("resource/executable")
@ValidateOnExecution(type = ExecutableType.NON_GETTER_METHODS)
public class ValidateExecutableResource {

  @Path("nogetter")
  @GET
  @Valid
  public NotShortNorFiveStringBean stringBean() {
    return new NotShortNorFiveStringBean("1");
  }

  @Path("getter")
  @GET
  @Valid
  public NotShortNorFiveStringBean getStringBean() {
    return new NotShortNorFiveStringBean("1");
  }

}
