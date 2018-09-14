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

import java.util.List;

import javax.websocket.server.ServerEndpointConfig.Configurator;

public class GetNegotiatedSubprotocolConfigurator extends Configurator {
  private static List<String> supported;

  private static List<String> requested;

  private static String resulted;

  @Override
  public String getNegotiatedSubprotocol(List<String> supported,
      List<String> requested) {
    GetNegotiatedSubprotocolConfigurator.setRequested(requested);
    GetNegotiatedSubprotocolConfigurator.setSupported(supported);
    GetNegotiatedSubprotocolConfigurator
        .setResulted(super.getNegotiatedSubprotocol(supported, requested));
    return GetNegotiatedSubprotocolConfigurator.getResulted();
  }

  static List<String> getSupported() {
    return supported;
  }

  static List<String> getRequested() {
    return requested;
  }

  static String getResulted() {
    return resulted;
  }

  private static void setSupported(List<String> supported) {
    GetNegotiatedSubprotocolConfigurator.supported = supported;
  }

  private static void setRequested(List<String> requested) {
    GetNegotiatedSubprotocolConfigurator.requested = requested;
  }

  private static void setResulted(String resulted) {
    GetNegotiatedSubprotocolConfigurator.resulted = resulted;
  }
}
