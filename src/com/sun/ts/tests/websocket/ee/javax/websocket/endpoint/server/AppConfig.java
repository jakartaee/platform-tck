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

package com.sun.ts.tests.websocket.ee.javax.websocket.endpoint.server;

import java.util.HashSet;
import java.util.Set;

import javax.websocket.Endpoint;
import javax.websocket.server.ServerApplicationConfig;
import javax.websocket.server.ServerEndpointConfig;

public class AppConfig implements ServerApplicationConfig {

  @Override
  public Set<ServerEndpointConfig> getEndpointConfigs(
      Set<Class<? extends Endpoint>> endpointClasses) {
    ServerEndpointConfig closeConfig = ServerEndpointConfig.Builder
        .create(WSCCloseServerEndpoint.class, "/close").build();

    ServerEndpointConfig errorConfig = ServerEndpointConfig.Builder
        .create(WSCErrorServerEndpoint.class, "/error").build();

    Set<ServerEndpointConfig> set = new HashSet<ServerEndpointConfig>();
    set.add(closeConfig);
    set.add(errorConfig);
    return set;
  }

  @Override
  public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> scanned) {
    Set<Class<?>> set = new HashSet<Class<?>>();
    set.add(WSCMsgServer.class);
    return set;
  }
}
