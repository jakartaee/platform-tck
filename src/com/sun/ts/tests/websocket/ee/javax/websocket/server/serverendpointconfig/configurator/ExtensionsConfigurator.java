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

import java.util.List;

import javax.websocket.Extension;
import javax.websocket.server.ServerEndpointConfig.Configurator;

import com.sun.ts.tests.websocket.common.impl.ExtensionImpl;

public class ExtensionsConfigurator extends Configurator {

  private static List<ExtensionImpl> installed;

  private static List<ExtensionImpl> requested;

  private static List<ExtensionImpl> resulted;

  @Override
  public List<Extension> getNegotiatedExtensions(List<Extension> installed,
      List<Extension> requested) {
    List<Extension> resulted = super.getNegotiatedExtensions(installed,
        requested);
    ExtensionsConfigurator
        .setRequested(ExtensionImpl.transformToImpl(requested));
    ExtensionsConfigurator.setResulted(ExtensionImpl.transformToImpl(resulted));
    ExtensionsConfigurator
        .setInstalled(ExtensionImpl.transformToImpl(installed));
    return resulted;
  }

  static List<ExtensionImpl> getInstalled() {
    return installed;
  }

  static void setInstalled(List<ExtensionImpl> installed) {
    ExtensionsConfigurator.installed = installed;
  }

  static List<ExtensionImpl> getRequested() {
    return requested;
  }

  static void setRequested(List<ExtensionImpl> requested) {
    ExtensionsConfigurator.requested = requested;
  }

  static List<ExtensionImpl> getResulted() {
    return resulted;
  }

  static void setResulted(List<ExtensionImpl> resulted) {
    ExtensionsConfigurator.resulted = resulted;
  }

}
