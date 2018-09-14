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

package com.sun.ts.tests.websocket.ee.javax.websocket.clientendpoint;

import com.sun.ts.tests.websocket.common.impl.ClientConfigurator;

/**
 * The findbugs compliant ClientConfigurator accessible out of WebSocket impl
 */
public class ClientConfiguratorHolderClientConfigurator
    extends ClientConfigurator {
  private static ClientConfigurator configurator;

  public ClientConfiguratorHolderClientConfigurator() {
    super();
    ClientConfiguratorHolderClientConfigurator.setConfigurator(this);
  }

  static ClientConfigurator getConfigurator() {
    return configurator;
  }

  private static void setConfigurator(ClientConfigurator configurator) {
    ClientConfiguratorHolderClientConfigurator.configurator = configurator;
  }
}
