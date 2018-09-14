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

package com.sun.ts.tests.securityapi.ham.sam.obtainbean;

import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.config.ServerAuthConfig;
import javax.security.auth.message.config.ServerAuthContext;
import javax.security.auth.message.module.ServerAuthModule;

public class TestServerAuthConfig implements ServerAuthConfig {

  private String layer;

  private String appContext;

  private CallbackHandler handler;

  private Map<String, String> providerProperties;

  private ServerAuthModule serverAuthModule;

  public TestServerAuthConfig(String layer, String appContext,
      CallbackHandler handler, Map<String, String> providerProperties,
      ServerAuthModule serverAuthModule) {
    this.layer = layer;
    this.appContext = appContext;
    this.handler = handler;
    this.providerProperties = providerProperties;
    this.serverAuthModule = serverAuthModule;
  }

  @Override
  public ServerAuthContext getAuthContext(String authContextID,
      Subject serviceSubject, @SuppressWarnings("rawtypes") Map properties)
      throws AuthException {
    return new TestServerAuthContext(handler, serverAuthModule);
  }

  @Override
  public String getMessageLayer() {
    return layer;
  }

  @Override
  public String getAuthContextID(MessageInfo messageInfo) {
    return appContext;
  }

  @Override
  public String getAppContext() {
    return appContext;
  }

  @Override
  public void refresh() {
  }

  @Override
  public boolean isProtected() {
    return false;
  }

  public Map<String, String> getProviderProperties() {
    return providerProperties;
  }

}
