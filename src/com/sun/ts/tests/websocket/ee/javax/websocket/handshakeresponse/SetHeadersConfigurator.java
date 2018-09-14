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

package com.sun.ts.tests.websocket.ee.javax.websocket.handshakeresponse;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Configurator;

public class SetHeadersConfigurator extends Configurator {
  static final String KEY = "aFirstKey";

  static final String[] HEADERS = { "header1", "header2", "header3", "header4",
      "header5", "header6", "header7", "header8" };

  @Override
  public void modifyHandshake(ServerEndpointConfig sec,
      HandshakeRequest request, HandshakeResponse response) {
    Map<String, List<String>> map = request.getHeaders();
    response.getHeaders().putAll(map);
    response.getHeaders().put(KEY, Arrays.asList(HEADERS));
  }
}
