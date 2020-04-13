/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates and others.
 * All rights reserved.
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

package com.sun.ts.tests.websocket.ee.jakarta.websocket.server.serverendpoint;

import java.lang.annotation.Annotation;

import jakarta.websocket.OnMessage;
import jakarta.websocket.server.ServerEndpoint;
import jakarta.websocket.server.ServerEndpointConfig.Configurator;

import com.sun.ts.tests.websocket.common.util.IOUtil;

@ServerEndpoint(value = "/countone", configurator = CountingConfigurator.class)
public class WSCountConfiguratorInstancesFirstServer {

  @OnMessage
  public String getCountingConfiguratorValue(String entity) {
    Annotation ann = getClass().getAnnotations()[0];
    ServerEndpoint endpoint = (ServerEndpoint) ann;
    Class<? extends Configurator> config = endpoint.configurator();
    try {
      CountingConfigurator cc = (CountingConfigurator) config
          .getConstructor(boolean.class).newInstance(true);
      return String.valueOf(cc.getCounterValue());
    } catch (Exception e) {
      return IOUtil.printStackTrace(e);
    }
  }
}
