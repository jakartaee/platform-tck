/*
 * Copyright (c) 2010, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxrs.platform.managedbean;

import java.lang.annotation.Annotation;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.interceptor.Interceptors;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Providers;

@Path("/managedbean")
@RequestScoped // TODO: remove in MR when cdi bean manager is available even
               // without this
// either GLASSFISH-19944 or GLASSFISH-19916
@ManagedBean("root")
public class ManagedBeanRootResource {

  private int value = 999;

  @Context
  Providers providers;

  @Context
  Application application;

  @GET
  @Path("resourcevalue")
  public String intValue() {
    return String.valueOf(value);
  }

  @GET
  @Path("providervalue")
  public String providerValue() {
    StringBuilderProvider provider = getStringBuilderProvider();
    return String.valueOf(provider.getValue());
  }

  @GET
  @Path("applicationvalue")
  public String applicationValue() {
    ApplicationHolderSingleton singleton = getAppHolderSingleton();
    return String.valueOf(singleton.getValue());
  }

  @GET
  @Path("interceptedprovidervalue")
  public String interceptedProviderValue() {
    StringBuilderProvider provider = getStringBuilderProvider();
    return provider.getInterceptedValue();
  }

  @GET
  @Path("interceptedresourcevalue")
  @Interceptors(InterceptorSingleton.class)
  public String interceptedResourceValue() {
    return String.valueOf(value);
  }

  @GET
  @Path("lookup")
  public String lookup() throws NamingException {
    ManagedBeanRootResource resource = (ManagedBeanRootResource) lookup(
        "java:module/root");
    return resource.intValue();
  }

  @GET
  @Path("sb")
  public StringBuilder stringbuilder() {
    return new StringBuilder().append("stringbuilder");
  }

  @PostConstruct
  public void postConstruct() {
    value++;
    isJaxrsInjectedPriorToPostConstruct = injectedApplication != null;
  }

  // <JAXRS:SPEC:53.1 ----------------------------------------------->
  @Context
  private Application injectedApplication;

  private boolean isJaxrsInjectedPriorToPostConstruct = false;

  @Path("priorroot")
  @GET
  public String injectPriorPostConstructOnRootResource() {
    return String.valueOf(isJaxrsInjectedPriorToPostConstruct);
  }

  @Path("priorapp")
  @GET
  public String injectPriorPostConstructOnApplication() {
    return String.valueOf(
        getAppHolderSingleton().isUriInfoInjectedBeforePostConstruct());
  }

  @Path("priorprovider")
  @GET
  public String injectPriorPostConstructOnProvider(
      @Context Providers providers) {
    StringBuilderProvider sbp = getStringBuilderProvider();
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

  // //////////////////////////////////////////////////////////////////
  private static Object lookup(String name) throws NamingException {
    InitialContext ic = new InitialContext();
    return ic.lookup(name);
  }

  private StringBuilderProvider getStringBuilderProvider() {
    return (StringBuilderProvider) providers.getMessageBodyWriter(
        StringBuilder.class, StringBuilder.class, (Annotation[]) null,
        MediaType.WILDCARD_TYPE);
  }

  private ApplicationHolderSingleton getAppHolderSingleton() {
    return (ApplicationHolderSingleton) application.getSingletons().iterator()
        .next();
  }

}
