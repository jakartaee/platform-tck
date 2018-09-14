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

package com.sun.ts.tests.websocket.platform.javax.websocket.server.handshakerequest.session;

import java.util.Arrays;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Configurator;

public class GetHttpSessionConfigurator extends Configurator {

  @Override
  public void modifyHandshake(ServerEndpointConfig sec,
      HandshakeRequest request, HandshakeResponse response) {
    boolean hasHttpSession = request.getHttpSession() != null;
    response.getHeaders().put(RESPONSE_KEY,
        Arrays.asList(String.valueOf(hasHttpSession)));
    super.modifyHandshake(sec, request, response);
  }

  /*
   * The value of this key is set to be checked on a client and the value should
   * reflect boolean value of having httpSession or not
   */
  static final String RESPONSE_KEY = "hasHttpSession";

}
