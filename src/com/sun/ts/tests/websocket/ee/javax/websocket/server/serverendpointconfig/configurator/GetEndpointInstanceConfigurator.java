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

import javax.websocket.server.ServerEndpointConfig.Configurator;

public class GetEndpointInstanceConfigurator extends Configurator {
  private static String instanceName;

  static final String ADDITIONAL_INFORMATION = "Additional information passed to instance";

  @SuppressWarnings("unchecked")
  @Override
  public <T> T getEndpointInstance(Class<T> endpointClass)
      throws InstantiationException {
    T t = super.getEndpointInstance(endpointClass);
    GetEndpointInstanceConfigurator.setInstanceName(t.getClass().getName());
    if (endpointClass == WSCGetEndpointInstanceServer.class)
      t = (T) new WSCGetEndpointInstanceServer(ADDITIONAL_INFORMATION);
    return t;
  }

  static String getInstanceName() {
    return instanceName;
  }

  static void setInstanceName(String instanceName) {
    GetEndpointInstanceConfigurator.instanceName = instanceName;
  }
}
