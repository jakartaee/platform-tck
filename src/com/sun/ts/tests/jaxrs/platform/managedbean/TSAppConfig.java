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

import java.util.HashSet;
import java.util.Set;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import com.sun.ts.tests.jaxrs.common.provider.PrintingErrorHandler;

@ManagedBean("application")
public class TSAppConfig extends Application {

  private int value = 99;

  public java.util.Set<java.lang.Class<?>> getClasses() {
    Set<Class<?>> resources = new HashSet<Class<?>>();
    resources.add(ManagedBeanRootResource.class);
    resources.add(StringBuilderProvider.class);
    resources.add(PrintingErrorHandler.class);
    return resources;
  }

  @Override
  public Set<Object> getSingletons() {
    Set<Object> set = new HashSet<Object>();
    set.add(new ApplicationHolderSingleton(this));
    return set;
  }

  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
  }

  // <JAXRS:SPEC:53.1 ----------------------------------------------->
  @Context
  private UriInfo info;

  private boolean isUriInfoInjectedBeforePostConstruct;

  public boolean isUriInfoInjectedBeforePostConstruct() {
    return isUriInfoInjectedBeforePostConstruct;
  }

  @PostConstruct
  public void postConstruct() {
    value++;
    isUriInfoInjectedBeforePostConstruct = info != null;
  }
  // </JAXRS:SPEC:53.1 ----------------------------------------------->
}
