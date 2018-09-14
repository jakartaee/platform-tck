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

package com.sun.ts.tests.jaxrs.platform.managedbean;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.interceptor.Interceptors;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

@Provider
@ManagedBean("provider")
public class StringBuilderProvider implements MessageBodyWriter<StringBuilder> {

  private int value = 999;

  public int getValue() {
    return value;
  }

  @Interceptors(InterceptorSingleton.class)
  public String getInterceptedValue() {
    return String.valueOf(value);
  }

  @Override
  public long getSize(StringBuilder arg0, Class<?> arg1, Type arg2,
      Annotation[] arg3, MediaType arg4) {
    return 13;
  }

  @Override
  public boolean isWriteable(Class<?> type, Type genericType,
      Annotation[] annotations, MediaType mediaType) {
    return type == StringBuilder.class;
  }

  @Override
  public void writeTo(StringBuilder t, Class<?> type, Type genericType,
      Annotation[] annotations, MediaType mediaType,
      MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
      throws IOException, WebApplicationException {
    entityStream.write(t.toString().getBytes());
  }

  // <JAXRS:SPEC:53.1 ----------------------------------------------->
  @Context
  private Application application;

  private boolean isApplicationInjectedBeforePostConstruct;

  public boolean isApplicationInjectedBeforePostConstruct() {
    return isApplicationInjectedBeforePostConstruct;
  }

  @PostConstruct
  public void postConstruct() {
    value++;
    isApplicationInjectedBeforePostConstruct = application != null;
  }
  // </JAXRS:SPEC:53.1 ----------------------------------------------->

}
