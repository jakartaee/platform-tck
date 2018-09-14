/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.websocket.ee.javax.websocket.server.serverendpointconfig.configurator;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Configurator;

public class ModifyHandshakeConfigurator extends Configurator {
  private static ServerEndpointConfig config;

  private static HandshakeRequest request;

  private static HandshakeResponse response;

  private boolean isCheckedOrigin = false;

  private static boolean isCheckedOriginBeforeModifyHandshake = false;

  @Override
  public boolean checkOrigin(String originHeaderValue) {
    isCheckedOrigin = true;
    return super.checkOrigin(originHeaderValue);
  }

  @Override
  public void modifyHandshake(ServerEndpointConfig sec,
      HandshakeRequest request, HandshakeResponse response) {
    ModifyHandshakeConfigurator
        .setCheckedOriginBeforeModifyHandshake(isCheckedOrigin);
    ModifyHandshakeConfigurator.setConfig(sec);
    ModifyHandshakeConfigurator.setRequest(request);
    ModifyHandshakeConfigurator.setResponse(response);
    super.modifyHandshake(sec, request, response);
  }

  static ServerEndpointConfig getConfig() {
    return config;
  }

  static HandshakeRequest getRequest() {
    return request;
  }

  static HandshakeResponse getResponse() {
    return response;
  }

  static boolean isCheckedOriginBeforeModifyHandshake() {
    return isCheckedOriginBeforeModifyHandshake;
  }

  private static void setConfig(ServerEndpointConfig config) {
    ModifyHandshakeConfigurator.config = config;
  }

  private static void setRequest(HandshakeRequest request) {
    ModifyHandshakeConfigurator.request = request;
  }

  private static void setResponse(HandshakeResponse response) {
    ModifyHandshakeConfigurator.response = response;
  }

  private static void setCheckedOriginBeforeModifyHandshake(
      boolean isCheckedOriginBeforeModifyHandshake) {
    ModifyHandshakeConfigurator.isCheckedOriginBeforeModifyHandshake = isCheckedOriginBeforeModifyHandshake;
  }
}
