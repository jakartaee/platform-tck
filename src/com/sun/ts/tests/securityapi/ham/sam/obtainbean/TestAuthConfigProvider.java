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

import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.config.AuthConfigFactory;
import javax.security.auth.message.config.AuthConfigProvider;
import javax.security.auth.message.config.ClientAuthConfig;
import javax.security.auth.message.config.ServerAuthConfig;
import javax.security.auth.message.module.ServerAuthModule;

public class TestAuthConfigProvider implements AuthConfigProvider {

  private static final String CALLBACK_HANDLER_PROPERTY_NAME = "authconfigprovider.client.callbackhandler";

  private Map<String, String> providerProperties;

  private ServerAuthModule serverAuthModule;

  public TestAuthConfigProvider(ServerAuthModule serverAuthModule) {
    this.serverAuthModule = serverAuthModule;
  }

  public TestAuthConfigProvider(Map<String, String> properties,
      AuthConfigFactory factory) {
    this.providerProperties = properties;

    if (factory != null) {
      factory.registerConfigProvider(this, null, null, "Auto registration");
    }
  }

  /**
   * The actual factory method that creates the factory used to eventually
   * obtain the delegate for a SAM.
   */
  @Override
  public ServerAuthConfig getServerAuthConfig(String layer, String appContext,
      CallbackHandler handler) throws AuthException, SecurityException {
    return new TestServerAuthConfig(layer, appContext,
        handler == null ? createDefaultCallbackHandler() : handler,
        providerProperties, serverAuthModule);
  }

  @Override
  public ClientAuthConfig getClientAuthConfig(String layer, String appContext,
      CallbackHandler handler) throws AuthException, SecurityException {
    return null;
  }

  @Override
  public void refresh() {
  }

  private CallbackHandler createDefaultCallbackHandler() throws AuthException {
    String callBackClassName = System
        .getProperty(CALLBACK_HANDLER_PROPERTY_NAME);

    if (callBackClassName == null) {
      throw new AuthException(
          "No default handler: " + CALLBACK_HANDLER_PROPERTY_NAME);
    }

    try {
      return (CallbackHandler) Thread.currentThread().getContextClassLoader()
          .loadClass(callBackClassName).newInstance();
    } catch (Exception e) {
      throw new AuthException(e.getMessage());
    }
  }

}
