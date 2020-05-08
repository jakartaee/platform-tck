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

package com.sun.ts.tests.jaxrs.platform.managedbean299;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.MatrixParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.ext.MessageBodyWriter;
import jakarta.ws.rs.ext.Providers;

@Path("resource")
@RequestScoped
public class Resource {

  private int value = 0;

  @Path("root")
  @GET
  public String root() {
    return String.valueOf(value);
  }

  @Inject
  private Provider<StringBuilderProvider> sbprovider;

  @Path("provider")
  @POST
  public String provider() {
    return String.valueOf(sbprovider.get().getValue());
  }

  @Path("app")
  @GET
  public String app(@Context Application application) {
    ApplicationHolderSingleton holder = (ApplicationHolderSingleton) application
        .getSingletons().iterator().next();
    return String.valueOf(holder.getValue());
  }

  @PostConstruct
  public void post() {
    value = 999;
    isJaxrsInjectedPriorToPostConstruct = injectedApplication != null;
  }

  // <JAXRS:SPEC:53.1 ----------------------------------------------->
  @Context
  private Application injectedApplication;

  private boolean isJaxrsInjectedPriorToPostConstruct = false;

  @Path("priorroot")
  @GET
  public String jaxrsInjectPriorPostConstructOnRootResource() {
    return String.valueOf(isJaxrsInjectedPriorToPostConstruct);
  }

  @Path("priorapp")
  @GET
  public String jaxrsInjectPriorPostConstructOnApplication() {
    ApplicationHolderSingleton holder = (ApplicationHolderSingleton) injectedApplication
        .getSingletons().iterator().next();
    return String.valueOf(holder.isUriInfoInjectedBeforePostConstruct());
  }

  @Path("priorprovider")
  @GET
  public String jaxrsInjectPriorPostConstructOnProvider(
      @Context Providers providers) {
    MessageBodyWriter<StringBuilder> mbw;
    mbw = providers.getMessageBodyWriter(StringBuilder.class, null, null,
        MediaType.WILDCARD_TYPE);
    StringBuilderProvider sbp = (StringBuilderProvider) mbw;
    return String.valueOf(sbp.isApplicationInjectedBeforePostConstruct());
  }

  // </JAXRS:SPEC:53.1 ----------------------------------------------->

  // <JAXRS:SPEC:53.3 ------------------------------------------------>
  @MatrixParam(value = "matrix")
  String matrix;

  @Path("nokeyword")
  @GET
  public String noInjectOrResourceKeyword() {
    return matrix;
  }
  // </JAXRS:SPEC:53.3 ----------------------------------------------->

}
